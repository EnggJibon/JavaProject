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
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodeling;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodeling;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.mail.reception.MstUserMailReceptionService;
import com.kmcj.karte.util.DateFormat;
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
 * 改造仕様未登録アラート送信バッチ
 * @author f.kitaoji
 */
@Named
@Dependent
public class RemodelingSpecAlertBatchlet extends AbstractBatchlet {    
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
    
    private final static String QUERY_REMODELED_MOLD =
        " SELECT remodel FROM TblMoldMaintenanceRemodeling remodel JOIN FETCH remodel.mstMold " +
        " WHERE remodel.mainteOrRemodel = 2 AND remodel.moldSpecHstIdStr IS NULL AND remodel.mstMold.department IS NOT NULL AND remodel.endDatetime IS NOT NULL " + //終了日時が登録されているのに仕様が登録されていない
        " ORDER BY remodel.mstMold.department, remodel.endDatetime DESC ";

    private final static String QUERY_REMODELED_MACHINE =
        " SELECT remodel FROM TblMachineMaintenanceRemodeling remodel JOIN FETCH remodel.mstMachine " +
        " WHERE remodel.mainteOrRemodel = 2 AND remodel.machineSpecHstId IS NULL AND remodel.mstMachine.department IS NOT NULL AND remodel.endDatetime IS NOT NULL " + //終了日時が登録されているのに仕様が登録されていない
        " ORDER BY remodel.mstMachine.department, remodel.endDatetime DESC ";

    
    @Override
    public String process() {
        //通知日
        noticeDate = DateFormat.dateToStr(new java.util.Date(), DateFormat.DATE_FORMAT);
        //金型について処理
        processMold();
        //設備について処理
        processMachine();
        return BatchStatus.COMPLETED.toString();
    }
    
    /**
     * 金型について処理
     */
    private void processMold() {
        //対象の金型データを取得
        Query query = entityManager.createQuery(QUERY_REMODELED_MOLD);
        List<TblMoldMaintenanceRemodeling> remodels = query.getResultList();
        if (remodels.size() <= 0) {
            return;
        }
        //対象金型データは部署でソートされているので部署ごとブレークしてレポート作成
        //１レコード目の部署を保持
        int oldDepartment = remodels.get(0).getMstMold().getDepartment();
        List<TblMoldMaintenanceRemodeling> remodelsDepartment = new ArrayList<>();
        for (TblMoldMaintenanceRemodeling remodel : remodels) {
            logger.log(logLevel, remodel.getMstMold().getMoldName());
            logger.log(logLevel, DateFormat.dateToStr(remodel.getEndDatetime(), DateFormat.DATE_FORMAT));
            
            if (remodel.getMstMold().getDepartment() != oldDepartment) {
                //ブレークしたら部署ごとのメール送信処理へ
                sendMoldMailByDepartment(remodelsDepartment);
                //部署ごとのリストをクリアしブレーク用判定変数も更新
                remodelsDepartment.clear();
                remodelsDepartment.add(remodel);
                oldDepartment = remodel.getMstMold().getDepartment();
            }
            else {
                //部署ごとのリストに加える
                remodelsDepartment.add(remodel);
            }
        }
        if (remodelsDepartment.size() > 0) {
            sendMoldMailByDepartment(remodelsDepartment);
        }
        
    }
    
    /**
     * 金型のメールを部署別に送信
     * @param remodelsDepartment 
     */
    private void sendMoldMailByDepartment(List<TblMoldMaintenanceRemodeling> remodelsDepartment) {
        if (remodelsDepartment.size() <= 0) return;
        //対象の所属を取得
        int department = remodelsDepartment.get(0).getMstMold().getDepartment();
        List<MstUser> users = mstUserMailReceptionService.getMailReceiveDepartmentUsers("mail003", department);
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
                sendMoldMailByLanguage(remodelsDepartment, usersDepartment);
                usersDepartment.clear();
                usersDepartment.add(user);
                oldLang = user.getLangId();
            }
            else {
                usersDepartment.add(user);
            }
        }
        if (usersDepartment.size() > 0) {
            sendMoldMailByLanguage(remodelsDepartment, usersDepartment);
        }
        
    }
    
    private void sendMoldMailByLanguage(List<TblMoldMaintenanceRemodeling> remodelsDepartment, List<MstUser> users) {
        if (remodelsDepartment.size() <= 0) return;
        //対象の所属を取得
        int department = remodelsDepartment.get(0).getMstMold().getDepartment();
        //対象の言語を取得
        if (users.size() <= 0) return;
        String langId = users.get(0).getLangId();
        //メールに用いる文言取得
        List<String> dictKeys = new ArrayList<>();
        dictKeys.add("msg_daily_mail_mold_remodel_spec_subject"); //件名:金型改造仕様登録アラート
        dictKeys.add("mold_name");//金型名称
        dictKeys.add("maintenance_remodeling_end_datetime"); //終了日時
        dictKeys.add("msg_mold_spec_not_registered_after_remodel");//以下の%mold%は改造後の仕様が登録されていません。Webの%tbl_mold_remodeling_result_registration%から登録してください。
        HashMap<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeys);
        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(department), langId, "mst_user.department");
        String departmentName = choice == null ? "" : choice.getChoice();
        //件名作成: タイトル YYYY/MM/DD 部署名
        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append(dictMap.get("msg_daily_mail_mold_remodel_spec_subject"));
        sbSubject.append(" ");
        sbSubject.append(noticeDate);
        sbSubject.append(" ");
        sbSubject.append(departmentName);
        logger.log(logLevel, sbSubject.toString());
        //本文作成
        StringBuilder sbBody = new StringBuilder();
        sbBody.append(dictMap.get("msg_mold_spec_not_registered_after_remodel"));
        sbBody.append(RETURN_CODE);
        sbBody.append(RETURN_CODE);
        sbBody.append(makeMoldRemodelColumns(dictMap));
        sbBody.append(RETURN_CODE);
        sbBody.append(makeMoldRemodelRecords(remodelsDepartment));
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
            logger.log(Level.SEVERE, "Failed to send mail to notice mold remodeling spec unregistered.");
            e.printStackTrace();
        }
    }
    
    /**
     * 金型一覧の項目名文字列取得
     * @param dictMap 
     */
    private String makeMoldRemodelColumns(HashMap<String, String> dictMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(dictMap.get("mold_name")); //金型名称
        sb.append(COLUMN_SEPARATOR);
        sb.append(dictMap.get("maintenance_remodeling_end_datetime")); //終了日時
        return sb.toString();
    }
    
    /**
     * 金型一覧のデータ部作成
     * @param remodelsDepartment
     * @return 
     */
    private String makeMoldRemodelRecords(List<TblMoldMaintenanceRemodeling> remodels) {
        StringBuilder sb = new StringBuilder();
        for (TblMoldMaintenanceRemodeling remodel : remodels) {
            sb.append(remodel.getMstMold().getMoldName());
            sb.append(COLUMN_SEPARATOR);
            sb.append(DateFormat.dateToStr(remodel.getEndDatetime(), DateFormat.DATETIME_FORMAT));
            sb.append(RETURN_CODE);
        }
        return sb.toString();
    }
    
    /**
     * 設備について処理
     */
    private void processMachine() {
        //対象の金型データを取得
        Query query = entityManager.createQuery(QUERY_REMODELED_MACHINE);
        List<TblMachineMaintenanceRemodeling> remodels = query.getResultList();
        if (remodels.size() <= 0) {
            return;
        }
        //対象設備データは部署でソートされているので部署ごとブレークしてレポート作成
        //１レコード目の部署を保持
        int oldDepartment = remodels.get(0).getMstMachine().getDepartment();
        List<TblMachineMaintenanceRemodeling> remodelsDepartment = new ArrayList<>();
        for (TblMachineMaintenanceRemodeling remodel : remodels) {
            logger.log(logLevel, remodel.getMstMachine().getMachineName());
            logger.log(logLevel, DateFormat.dateToStr(remodel.getEndDatetime(), DateFormat.DATE_FORMAT));
            
            if (remodel.getMstMachine().getDepartment() != oldDepartment) {
                //ブレークしたら部署ごとのメール送信処理へ
                sendMachineMailByDepartment(remodelsDepartment);
                //部署ごとのリストをクリアしブレーク用判定変数も更新
                remodelsDepartment.clear();
                remodelsDepartment.add(remodel);
                oldDepartment = remodel.getMstMachine().getDepartment();
            }
            else {
                //部署ごとのリストに加える
                remodelsDepartment.add(remodel);
            }
        }
        if (remodelsDepartment.size() > 0) {
            sendMachineMailByDepartment(remodelsDepartment);
        }
        
    }
    
    /**
     * 設備のメールを部署別に送信
     * @param remodelsDepartment 
     */
    private void sendMachineMailByDepartment(List<TblMachineMaintenanceRemodeling> remodelsDepartment) {
        if (remodelsDepartment.size() <= 0) return;
        //対象の所属を取得
        int department = remodelsDepartment.get(0).getMstMachine().getDepartment();
        List<MstUser> users = mstUserMailReceptionService.getMailReceiveDepartmentUsers("mail003", department);
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
                sendMachineMailByLanguage(remodelsDepartment, usersDepartment);
                usersDepartment.clear();
                usersDepartment.add(user);
                oldLang = user.getLangId();
            }
            else {
                usersDepartment.add(user);
            }
        }
        if (usersDepartment.size() > 0) {
            sendMachineMailByLanguage(remodelsDepartment, usersDepartment);
        }
        
    }
    
    private void sendMachineMailByLanguage(List<TblMachineMaintenanceRemodeling> remodelsDepartment, List<MstUser> users) {
        if (remodelsDepartment.size() <= 0) return;
        //対象の所属を取得
        int department = remodelsDepartment.get(0).getMstMachine().getDepartment();
        //対象の言語を取得
        if (users.size() <= 0) return;
        String langId = users.get(0).getLangId();
        //メールに用いる文言取得
        List<String> dictKeys = new ArrayList<>();
        dictKeys.add("msg_daily_mail_machine_remodel_spec_subject"); //件名:設備改造仕様登録アラート
        dictKeys.add("machine_name");//金型名称
        dictKeys.add("machine_maintenance_remodeling_end_datetime"); //終了日時
        dictKeys.add("msg_machine_spec_not_registered_after_remodel");//以下の%machine%は改造後の仕様が登録されていません。Webの%tbl_machine_remodeling_result_registration%から登録してください。
        HashMap<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeys);
        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(department), langId, "mst_user.department");
        String departmentName = choice == null ? "" : choice.getChoice();
        //件名作成: タイトル YYYY/MM/DD 部署名
        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append(dictMap.get("msg_daily_mail_machine_remodel_spec_subject"));
        sbSubject.append(" ");
        sbSubject.append(noticeDate);
        sbSubject.append(" ");
        sbSubject.append(departmentName);
        logger.log(logLevel, sbSubject.toString());
        //本文作成
        StringBuilder sbBody = new StringBuilder();
        sbBody.append(dictMap.get("msg_machine_spec_not_registered_after_remodel"));
        sbBody.append(RETURN_CODE);
        sbBody.append(RETURN_CODE);
        sbBody.append(makeMachineRemodelColumns(dictMap));
        sbBody.append(RETURN_CODE);
        sbBody.append(makeMachineRemodelRecords(remodelsDepartment));
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
            logger.log(Level.SEVERE, "Failed to send mail to notice machine remodeling spec unregistered.");
            e.printStackTrace();
        }
    }
    
    /**
     * 設備一覧の項目名文字列取得
     * @param dictMap 
     */
    private String makeMachineRemodelColumns(HashMap<String, String> dictMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(dictMap.get("machine_name")); //金型名称
        sb.append(COLUMN_SEPARATOR);
        sb.append(dictMap.get("machine_maintenance_remodeling_end_datetime")); //終了日時
        return sb.toString();
    }
    
    /**
     * 設備一覧のデータ部作成
     * @param remodelsDepartment
     * @return 
     */
    private String makeMachineRemodelRecords(List<TblMachineMaintenanceRemodeling> remodels) {
        StringBuilder sb = new StringBuilder();
        for (TblMachineMaintenanceRemodeling remodel : remodels) {
            sb.append(remodel.getMstMachine().getMachineName());
            sb.append(COLUMN_SEPARATOR);
            sb.append(DateFormat.dateToStr(remodel.getEndDatetime(), DateFormat.DATETIME_FORMAT));
            sb.append(RETURN_CODE);
        }
        return sb.toString();
    }

}
