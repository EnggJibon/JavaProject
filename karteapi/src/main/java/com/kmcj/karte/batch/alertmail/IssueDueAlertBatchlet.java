/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.alertmail;

import com.kmcj.karte.MailSender;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.issue.TblIssue;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.mail.reception.MstUserMailReceptionService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 打上対策期限アラートメール送信バッチ
 * @author f.kitaoji
 */
@Named
@Dependent
public class IssueDueAlertBatchlet extends AbstractBatchlet {    
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    @Inject
    MstUserMailReceptionService mstUserMailReceptionService;
    @Inject
    MstDictionaryService mstDictionaryService;
    @Inject
    MstChoiceService mstChoiceService;
    @Inject
    MailSender mailSender;
    
    private final static Level logLevel = Level.FINE; //テスト時:Level.INFO 本番時:Level.FINE
    private Logger logger = Logger.getLogger(MainteCycleAlertBatchlet.class.getName());
    private String noticeDate;
    private final static String COLUMN_SEPARATOR = ",";
    private final static String RETURN_CODE = "\r\n";
    
    private final static String QUERY_GET_ISSUE =
        " SELECT tblIssue FROM TblIssue tblIssue LEFT JOIN FETCH tblIssue.mstComponent LEFT JOIN FETCH tblIssue.mstMold WHERE tblIssue.measureStatus < :measureStatus AND tblIssue.measureDueDate <= :measureDueDate AND tblIssue.reportDepartment IS NOT NULL " +
        " ORDER BY tblIssue.reportDepartment, tblIssue.measureDueDate DESC ";

    @Override
    public String process() {
        //通知日
        noticeDate = DateFormat.dateToStr(new java.util.Date(), DateFormat.DATE_FORMAT);
        //対象の打上データを取得
        Query query = entityManager.createQuery(QUERY_GET_ISSUE);
        query.setParameter("measureStatus", CommonConstants.ISSUE_MEASURE_STATUS_COMPLETED); //対策ステータスが50：完了になっていない
        query.setParameter("measureDueDate", DateFormat.strToDate(noticeDate));
        List<TblIssue> issues = query.getResultList();
        if (issues.size() <= 0) {
            return BatchStatus.COMPLETED.toString();
        }
        //打上データは打上場所でソートされているので部署ごとブレークしてレポート作成
        //１レコード目の部署を保持
        int oldDepartment = issues.get(0).getReportDepartment();
        List<TblIssue> issuesDepartment = new ArrayList<>();
        for (TblIssue issue : issues) {
            logger.log(logLevel, DateFormat.dateToStr(issue.getMeasureDueDate(), DateFormat.DATE_FORMAT));
            logger.log(logLevel, issue.getIssue());
            logger.log(logLevel, issue.getReportPersonName());
            
            if (issue.getReportDepartment() != oldDepartment) {
                //ブレークしたら部署ごとのメール送信処理へ
                sendMailByDepartment(issuesDepartment);
                //部署ごとのリストをクリアしブレーク用判定変数も更新
                issuesDepartment.clear();
                issuesDepartment.add(issue);
                oldDepartment = issue.getReportDepartment();
            }
            else {
                //部署ごとのリストに加える
                issuesDepartment.add(issue);
            }
        }
        if (issuesDepartment.size() > 0) {
            sendMailByDepartment(issuesDepartment);
        }
        return BatchStatus.COMPLETED.toString();
    }
    
    /**
     * 部署ごとのメール送信処理
     * @param issuesDepartment 
     */
    private void sendMailByDepartment(List<TblIssue> issuesDepartment) {
        if (issuesDepartment.size() <= 0) return;
        //対象の所属を取得
        int department = issuesDepartment.get(0).getReportDepartment();
        List<MstUser> users = mstUserMailReceptionService.getMailReceiveDepartmentUsers("mail002", department);
        if (users.size() <= 0) {
            return;
        }
        //言語別にリストを分割
        String oldLang = users.get(0).getLangId();
        List<MstUser> usersDepartment = new ArrayList<>();
        for (MstUser user : users) {
            logger.log(logLevel, user.getUserName());
            //言語がブレークしたらメール送信処理へ
            if (!user.getLangId().equals(oldLang)) {
                sendMailByLanguage(issuesDepartment, usersDepartment);
                usersDepartment.clear();
                usersDepartment.add(user);
                oldLang = user.getLangId();
            }
            else {
                usersDepartment.add(user);
            }
        }
        if (usersDepartment.size() > 0) {
            sendMailByLanguage(issuesDepartment, usersDepartment);
        }
    }
    
    /**
     * 言語別にメール送信
     * @param issuesDepartment
     * @param users 
     */
    private void sendMailByLanguage(List<TblIssue> issuesDepartment, List<MstUser> users) {
        if (issuesDepartment.size() <= 0) return;
        //対象の所属を取得
        int department = issuesDepartment.get(0).getReportDepartment();
        //対象の言語を取得
        if (users.size() <= 0) return;
        String langId = users.get(0).getLangId();
        //メールに用いる文言取得
        List<String> dictKeys = new ArrayList<>();
        dictKeys.add("msg_daily_mail_issue_due_date_subject"); //件名:打上対策アラート
        dictKeys.add("msg_issue_due_date_today"); //以下の打上は本日が対策期限です。
        dictKeys.add("msg_issue_due_date_past");//以下の打上は対策期限を過ぎています。
        dictKeys.add("issue_report_date"); //打上日
        dictKeys.add("component_code"); //部品コード
        dictKeys.add("issue_report_person_name"); //報告者
        dictKeys.add("issue"); //事象
        dictKeys.add("issue_measure_due_date"); //対策期限
        dictKeys.add("mold_id");
        dictKeys.add("mold_name");
        dictKeys.add("issue_report_category1");
        dictKeys.add("issue_report_category2");
        dictKeys.add("issue_report_category3");
        HashMap<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeys);
        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(department), langId, "mst_user.department");
        String departmentName = choice == null ? "" : choice.getChoice();
        //件名作成: タイトル YYYY/MM/DD 部署名
        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append(dictMap.get("msg_daily_mail_issue_due_date_subject"));
        sbSubject.append(" ");
        sbSubject.append(noticeDate);
        sbSubject.append(" ");
        sbSubject.append(departmentName);
        logger.log(logLevel, sbSubject.toString());
        //本文作成
        StringBuilder sbBody = new StringBuilder();
        List<TblIssue> todayIssues = new ArrayList<>(); //今日が期限の打上
        List<TblIssue> pastIssues = new ArrayList<>(); //期限を過ぎた打上
        for (TblIssue issue : issuesDepartment) {
            if (issue.getMeasureDueDate() != null && DateFormat.dateToStr(issue.getMeasureDueDate(), DateFormat.DATE_FORMAT).equals(noticeDate)) {
                todayIssues.add(issue);
            }
            else {
                pastIssues.add(issue);
            }
        }
        if (todayIssues.size() > 0) {
            sbBody.append(dictMap.get("msg_issue_due_date_today")); //以下の打上は本日が対策期限です。
            sbBody.append(RETURN_CODE);
            sbBody.append(RETURN_CODE);
            sbBody.append(makeIssueColumns(dictMap, false)); //本日期限の対策一覧には対策期限を表示しない
            sbBody.append(RETURN_CODE);
            sbBody.append(makeIssueRecords(todayIssues, false));
            sbBody.append(RETURN_CODE);
            sbBody.append(RETURN_CODE);
        }
        if (pastIssues.size() > 0) {
            sbBody.append(dictMap.get("msg_issue_due_date_past")); //以下の打上は対策期限を過ぎています。
            sbBody.append(RETURN_CODE);
            sbBody.append(RETURN_CODE);
            sbBody.append(makeIssueColumns(dictMap, true)); 
            sbBody.append(RETURN_CODE);
            sbBody.append(makeIssueRecords(pastIssues, true));
            sbBody.append(RETURN_CODE);
            sbBody.append(RETURN_CODE);
        }
        //宛先メールアドレスリスト作成
        List<String> toList = new ArrayList<>();
        for (MstUser user : users) {
            if (user.getMailAddress() != null) {
                toList.add(user.getMailAddress());
            }
        }
        //メール送信
        try {
            mailSender.setMakePlainTextBody(true);
            mailSender.sendMail(toList, null, sbSubject.toString(), sbBody.toString());
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to send mail to notice the due date of issues.");
            e.printStackTrace();
        }
    }
    
    /**
     * 打上一覧の項目名の文字列を作成
     * @param dictMap
     * @param showDueDate
     * @return 
     */
    private String makeIssueColumns(HashMap<String, String> dictMap, boolean showDueDate) {
        StringBuilder sb = new StringBuilder();
        sb.append(dictMap.get("issue_report_date")); //打上日
        sb.append(COLUMN_SEPARATOR);
        sb.append(dictMap.get("mold_id"));
        sb.append(COLUMN_SEPARATOR);
        sb.append(dictMap.get("mold_name"));
        sb.append(COLUMN_SEPARATOR);
        sb.append(dictMap.get("component_code")); //部品コード
        sb.append(COLUMN_SEPARATOR);
        sb.append(dictMap.get("issue_report_category1"));
        sb.append(COLUMN_SEPARATOR);
        sb.append(dictMap.get("issue_report_category2"));
        sb.append(COLUMN_SEPARATOR);
        sb.append(dictMap.get("issue_report_category3"));
        sb.append(COLUMN_SEPARATOR);
        sb.append(dictMap.get("issue_report_person_name")); //報告者
        sb.append(COLUMN_SEPARATOR);
        sb.append(dictMap.get("issue")); //事象
        if (showDueDate) {
            sb.append(COLUMN_SEPARATOR);
            sb.append(dictMap.get("issue_measure_due_date")); //対策期限
        }
        return sb.toString();
    }
    
    /**
     * 打上一覧のデータ部の文字列を作成
     * @param issues
     * @param showDueDate
     * @return 
     */
    private String makeIssueRecords(List<TblIssue> issues, boolean showDueDate) {
        StringBuilder sb = new StringBuilder();
        for (TblIssue issue: issues) {
            sb.append(DateFormat.dateToStr(issue.getReportDate(), DateFormat.DATE_FORMAT));
            sb.append(COLUMN_SEPARATOR);
            sb.append(issue.getMstMold() == null ? "" : issue.getMstMold().getMoldId());
            sb.append(COLUMN_SEPARATOR);
            sb.append(issue.getMstMold() == null ? "" : issue.getMstMold().getMoldName());
            sb.append(COLUMN_SEPARATOR);
            sb.append(issue.getMstComponent() == null ? "" : issue.getMstComponent().getComponentCode());
            sb.append(COLUMN_SEPARATOR);
            sb.append(issue.getReportCategory1Text());
            sb.append(COLUMN_SEPARATOR);
            sb.append(issue.getReportCategory2Text());
            sb.append(COLUMN_SEPARATOR);
            sb.append(issue.getReportCategory3Text());
            sb.append(COLUMN_SEPARATOR);
            //if (issue.getReportPersonName() != null) {
            if (issue.getMstUser() != null && issue.getMstUser().getUserName() != null) {
                //sb.append(issue.getReportPersonName());
                sb.append(issue.getMstUser().getUserName());
            }
            else {
                sb.append("");
            }
            sb.append(COLUMN_SEPARATOR);
            sb.append(FileUtil.removeReturnCode(issue.getIssue()));
            if (showDueDate) {
                sb.append(COLUMN_SEPARATOR);
                if (issue.getMeasureDueDate() != null) {
                    sb.append(DateFormat.dateToStr(issue.getMeasureDueDate(), DateFormat.DATE_FORMAT));
                }
                else {
                    sb.append("");
                }
            }
            sb.append(RETURN_CODE);
        }
        return sb.toString();
    }
    
}
