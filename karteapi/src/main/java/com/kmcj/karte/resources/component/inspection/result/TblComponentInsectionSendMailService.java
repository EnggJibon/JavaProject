/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result;

import com.kmcj.karte.MailSender;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.component.inspection.result.model.TblComponentInspectionSendMail;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.util.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 * メール通知(出荷/入荷)
 *
 * @author Apeng
 */
@Dependent
public class TblComponentInsectionSendMailService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;
    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private MailSender mailSender;

    private final static List<String> DICT_KEYS;

    static {
        List<String> list = new ArrayList<>();
        list.add("inspection_outgoing_ended_notic");        //出荷検査完了通知
        list.add("inspection_incoming_ended_notic");        //入荷検査完了通知
        list.add("department_members");                     //関係各位
        list.add("component_inspection_description");       //検査実績は以下の通りです
        list.add("inspection_ended_quantity");              //検査完了（%s件）
        list.add("inspect_concession_quantity");            //検査特採（%s件）
        list.add("inspection__confirm_ended_quantity");     //検査確認完了（%s件）
        list.add("inspection__approver_ended_quantity");    //検査承認完了（%s件）
        list.add("component_inspection_list_url");          //検査部品リストのURL
        list.add("inspection_ended_component");             //検査完了部品
        list.add("insp_concession_component");        //検査特採部品
        list.add("inspection_confirm_ended_component");     //検査確認完了部品
        list.add("inspection_approver_ended_component");    //検査承認完了部品
        list.add("inspection_confirm_user_disavow");        //検査確認者-否認
        list.add("contact_members");                        //担当者様
        list.add("inspection_processing_executive");        //下記の処理を実行してください
        list.add("component_inspection_disavow_description");        //説明：対象部品の検査結果を否認します。下記コメントを確認してください
        list.add("component_inspection_table_url");        //検査表へのURL
        list.add("component_inspection_code");              //部品番号
        list.add("component_name");                         //部品名称
        list.add("component_quantity");                     //個数
        list.add("disavow_comment");                        //否認のコメント
        list.add("inspection_approver_user_disavow");        //検査承認者-否認
        list.add("inspection_unqualified_confirm");         //検査不合格：確認依頼
        list.add("component_inspection_unqualified_confirm_description");        //説明：対象部品の検査が不合格となりましたので、確認をお願い致します
        list.add("inspection_unqualified_approver");        //検査不合格：承認依頼
        list.add("component_inspection_unqualified_approver_description");        //説明：対象部品の検査不合格品を確認しましたので、承認をお願い致します
        list.add("inspection_unqualified_ended");           //検査不合格：承認
        list.add("component_inspection_unqualified_ended_description");        //説明：対象部品の検査不合格品を承認します。是正処置を実施してください
        list.add("incoming_inspection_file_denied"); // 【受入検査 帳票確認 否認】
        list.add("file_denied_contact_members"); // 担当者様
        list.add("inspection_file_denied_executive"); //帳票確認において上長より「否認」と判定されました。<br/>対象資料を確認願います。
        list.add("file_denied_component_inspection_table_url"); // 検査表URL
        list.add("inspection_table_create_date"); // 検査表作成日
        list.add("company_code"); // 会社コード
        list.add("outgoing_company_name"); // 出荷元
        list.add("file_denied_comment"); // コメント
        list.add("file_denied_end_greeting"); // よろしくお願いいたします。
        list.add("file_denied_end"); // 以上
        list.add("component_code"); //部品コード
        list.add("inspection_file_denied_confirm"); // 対象資料を確認願います。
        list.add("incoming_inspection_ok");//【受入検査】結果判定［合格］
        list.add("incoming_inspection_contact_members"); //ご担当者様
        list.add("incoming_inspection_ok_executive"); //弊社受入検査で「合格」と判定されました。
        list.add("incoming_inspection_ok_confirm"); //詳細は下記ＵＲＬより確認をお願いいたします。
        list.add("incoming_inspection_ok_end_greeting");// よろしくお願いいたします。
        list.add("incoming_inspection_ok_end");// 以上
        list.add("incoming_inspection_ng"); //【受入検査】結果判定［不合格］
        list.add("incoming_inspection_ng_executive");// 弊社受入検査で「不合格」と判定されました。
        list.add("incoming_inspection_ng_confirm");// 内容を確認いただき、是正処置をお願いいたします。
        list.add("incoming_inspection_ng_table_url");// 検査表URL
        list.add("component_inspection_po");// P/O
        list.add("manufacture_lot");// 製造ロット
        list.add("incoming_inspection_date");// 受入検査日
        list.add("incoming_inspection_ng_comment");// コメント
        list.add("incoming_inspection_ng_end_greeting");// よろしくお願いいたします。
        list.add("incoming_inspection_ng_end");// 以上
        DICT_KEYS = Collections.unmodifiableList(list);
    }
    private final static String FIELD_TITLE = ": ";
    private final static String SPACE = " ";
    private final static String OPERATOR = "&";
    private final static String SLASHES = "／";
    private final static String EQUAL = "=";
    private final static String DIVIDING_LINE = "---------------";
    private final static String OUTGOING_LIST_PATH = "/karte/php/T3000_ComponentInspectionList.php";
    private final static String INCOMING_LIST_PATH = "/karte/php/T3000_ComponentInspectionList.php?functionType=3";
    private final static String DETAIL_PATH = "/karte/php/T3001_ComponentInspectionDetail.php?id=";
    private final static String RETURN_CODE = "\r\n";
    private final String noticeDate = DateFormat.dateToStr(new Date(), DateFormat.DATE_FORMAT_MONTHDAY);
	private final String noticeDateFormat = DateFormat.dateToStr(new Date(), DateFormat.DATE_FORMAT);
    private final HashMap<String, HashMap<String, String>> langDictsMap = new HashMap<>(); //言語別に文言ハッシュマップを保持

    
    private final static String QUERY_GET_MAIL_RECEIVE_EVENT_USER_DEPARTMENT
            = "SELECT m FROM MstUser m "
            + " WHERE (EXISTS ( "
            + " SELECT r FROM MstUserMailReception r "
            + " WHERE m.uuid = r.userUuid "
            + " AND r.receptionFlg = 1 "
            + " AND r.eventUuid = :eventUuid "
            + " ) "
            + " OR (EXISTS ( "
            + " SELECT d FROM MstUserMailReceptionDepartment d "
            + " WHERE m.uuid = d.userUuid "
            + " AND d.eventUuid = :eventUuid2 "
            + " AND d.belongingDepartment = 0 "
            + " ) "
            + " )) "
            + " AND m.mailAddress IS NOT NULL "
            + " AND m.validFlg = 1 "
            + " ORDER BY m.langId ";
    
    // 帳票否認の場合、担当者を取得
    private final static String QUERY_GET_MAIL_RECEIVE_EVENT_USER_FILE_DENIED
            = "SELECT m FROM MstUser m "
            + " WHERE (EXISTS ( "
            + " SELECT r FROM MstUserMailReception r "
            + " WHERE m.uuid = r.userUuid "
            + " AND r.receptionFlg = 1 "
            + " AND r.eventUuid = :eventUuid "
            + " ))"
            + " AND m.uuid IN :userList"
            + " AND m.mailAddress IS NOT NULL "
            + " AND m.validFlg = 1 "
            + " ORDER BY m.langId ";
    
    //メール受信ユーザー取得クエリ(所属別)
    private final static String QUERY_GET_MAIL_RECEIVE_USER =
            " SELECT m FROM MstUser m " +
            " WHERE (EXISTS ( " +
                //所属指定で設定されているとき
                " SELECT r FROM MstUserMailReception r " +
                " JOIN MstUserMailReceptionDepartment d " +
                " ON r.eventUuid = d.eventUuid " +
                " AND r.userUuid = d.userUuid " +
                " WHERE m.uuid = r.userUuid " +
                " AND r.receptionFlg = 1 " +
                " AND r.eventUuid = :eventUuid " +
                " AND d.department in :department " + //設定テーブルの所属が指定に等しい
                " AND d.belongingDepartment = 0 " +
            " ) " +
            " OR (EXISTS ( " +
                //自分の所属する部署で指定されているとき
                " SELECT r FROM MstUserMailReception r " +
                " JOIN MstUserMailReceptionDepartment d " +
                " ON r.eventUuid = d.eventUuid " +
                " AND r.userUuid = d.userUuid " +
                " WHERE m.uuid = r.userUuid " +
                " AND r.receptionFlg = 1 " +
                " AND r.eventUuid = :eventUuid2 " +
                " AND d.belongingDepartment = 1 " +
                " ) AND m.department in :department2 " + //ユーザーマスタの所属が指定に等しい
            " )) " +
            " AND m.mailAddress IS NOT NULL " +
            " AND m.department IS NOT NULL " +
            " AND m.validFlg = 1 " +
            " ORDER BY m.langId ";

    /**
     * メール通知(出荷/入荷共通)
     *
     * @param processType
     * @param resultInfo
     */
    @Transactional
    public void sendNotice(int processType, TblComponentInspectionSendMail resultInfo) {

        //処理種別(processType)よりメール設定で受信すべきユーザーを取得
        String eventUuid = null;
        switch (processType) {
            case CommonConstants.OUTGOING_CHECK_RESULT_OK:
                eventUuid = "mail009";
                break;
            case CommonConstants.CHECK_CONFIRM_DENIAL:
                eventUuid = "mail010";
                break;
            case CommonConstants.CHECK_APPROVE_DENIAL:
                eventUuid = "mail011";
                break;
            case CommonConstants.CHECK_NG_END:
                eventUuid = "mail012";
                break;
            case CommonConstants.CHECK_NG_CONFIRM:
                eventUuid = "mail013";
                break;
            case CommonConstants.CHECK_NG_APPROVE:
                eventUuid = "mail014";
                break;
            case CommonConstants.INCOMING_CHECK_RESULT_OK:
                eventUuid = "mail015";
                break;
            // km-976 帳票確認者の検索を追加 20181122 start
            case CommonConstants.INCOMING_FILE_DENIED:
                eventUuid = "mail018";
                break;
            // km-976 帳票確認者の検索を追加 20181122 end
            // KM-977 受入検査結果(合格)がサプライヤに反映されたことを通知します 2018/11/29 start
            case CommonConstants.INCOMING_INSPECTION_OK:
                eventUuid = "mail019";
                break;
            case CommonConstants.INCOMING_INSPECTION_NG:
                eventUuid = "mail020";
                break;
            // KM-977 受入検査結果(合格)がサプライヤに反映されたことを通知します 2018/11/29 end
        }

        if (StringUtils.isEmpty(eventUuid)) {
            return;
        }

        //メール設定で受信すべきユーザーを取得。該当なしだとしても受付者にはメール送信する
        
        List<MstUser> mailSettingUsers;
        
        switch (processType) {
            case CommonConstants.INCOMING_FILE_DENIED:
                {
                    Query query = entityManager.createQuery(QUERY_GET_MAIL_RECEIVE_EVENT_USER_FILE_DENIED);
                    query.setParameter("eventUuid", eventUuid);
                    query.setParameter("userList", resultInfo.getUserList());
                    mailSettingUsers = query.getResultList();
                    break;
                }
            case CommonConstants.OUTGOING_CHECK_RESULT_OK:
            case CommonConstants.INCOMING_CHECK_RESULT_OK:
            case CommonConstants.CHECK_CONFIRM_DENIAL:
            case CommonConstants.CHECK_NG_CONFIRM:
            case CommonConstants.CHECK_NG_END:
            case CommonConstants.CHECK_APPROVE_DENIAL:
            case CommonConstants.CHECK_NG_APPROVE:
            case CommonConstants.INCOMING_INSPECTION_OK:
            case CommonConstants.INCOMING_INSPECTION_NG:
                {
                    Query query = entityManager.createQuery(QUERY_GET_MAIL_RECEIVE_USER);
                    query.setParameter("eventUuid", eventUuid);
                    query.setParameter("eventUuid2", eventUuid);
                    query.setParameter("department", resultInfo.getUserDepartmentList());
                    query.setParameter("department2", resultInfo.getUserDepartmentList());
                    mailSettingUsers = query.getResultList();
                    break;
                }
            default:
                {
                    Query query = entityManager.createQuery(QUERY_GET_MAIL_RECEIVE_EVENT_USER_DEPARTMENT);
                    query.setParameter("eventUuid", eventUuid);
                    query.setParameter("eventUuid2", eventUuid);
                    mailSettingUsers = query.getResultList();
                    break;
                }
        }
        
        if (mailSettingUsers != null && mailSettingUsers.size() > 0) {
            if (CommonConstants.OUTGOING_CHECK_RESULT_OK == processType
                    || CommonConstants.INCOMING_CHECK_RESULT_OK == processType
                    || CommonConstants.INCOMING_INSPECTION_OK == processType) {

                // 言語別にメール作成送信
                Map<String, List<MstUser>> receiverListByLangId = new HashMap<>();
                for (MstUser user : mailSettingUsers) {
                    List<MstUser> usersDepartment;
                    
                    if(receiverListByLangId.get(user.getLangId()) == null){
                        usersDepartment = new ArrayList<>();
                        usersDepartment.add(user);
                        receiverListByLangId.put(user.getLangId(), usersDepartment);
                    }else {
                        usersDepartment = receiverListByLangId.get(user.getLangId());
                        usersDepartment.add(user);
                    }
                }
                
                for (Map.Entry<String, List<MstUser>> entry : receiverListByLangId.entrySet()) {
                    sendMailByLanguage(processType, resultInfo, entry.getValue(), entry.getKey());
                }
                
            } else {
                List<MstUser> usersDepartment = new ArrayList<>();
                
                for (MstUser user : mailSettingUsers) {
                    usersDepartment.add(user);
                    sendMailByLanguage(processType, resultInfo, usersDepartment, user.getLangId());
                    usersDepartment.clear();
                }
            }
        }

    }

    /**
     * ユーザーの言語別にメール作成・送信
     *
     * @param request
     * @param users
     * @param langId
     */
    private void sendMailByLanguage(int processType, TblComponentInspectionSendMail resultInfo, List<MstUser> users, String langId) {
        //メールに使う文言を取得
        HashMap<String, String> dictMap = getDictionaryMap(langId);
        //件名作成
        String subject = makeMailSubject(processType, resultInfo, dictMap);
        //本文作成
        String body = makeMailBody(processType, resultInfo, dictMap);

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
            mailSender.sendMail(toList, null, subject, body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * メール件名作成
     *
     * @param processType
     * @param tblComponentInspectionResult
     * @param langId
     * @param dictMap
     * @return
     */
    private String makeMailSubject(int processType, TblComponentInspectionSendMail resultInfo, HashMap<String, String> dictMap) {
        StringBuilder subject = new StringBuilder();
        switch (processType) {
            case CommonConstants.OUTGOING_CHECK_RESULT_OK:
                subject.append(dictMap.get("inspection_outgoing_ended_notic"));//出荷検査完了通知
                subject.append(SPACE);
                subject.append(noticeDate);
                subject.append(SPACE);
                break;
            case CommonConstants.INCOMING_CHECK_RESULT_OK:
                subject.append(dictMap.get("inspection_incoming_ended_notic"));//入荷検査完了通知
                subject.append(SPACE);
                subject.append(noticeDate);
                subject.append(SPACE);
                break;
            case CommonConstants.CHECK_CONFIRM_DENIAL:
                subject.append(dictMap.get("inspection_confirm_user_disavow"));// 検査確認者-否認
                subject.append(resultInfo.getComponentCode());
                subject.append(SLASHES);
                subject.append(resultInfo.getComponentName());
                subject.append(SPACE);
                break;
            case CommonConstants.CHECK_APPROVE_DENIAL:
                subject.append(dictMap.get("inspection_approver_user_disavow"));//検査承認者-否認
                subject.append(resultInfo.getComponentCode());
                subject.append(SLASHES);
                subject.append(resultInfo.getComponentName());
                subject.append(SPACE);
                break;
            case CommonConstants.CHECK_NG_END:
                subject.append(dictMap.get("inspection_unqualified_confirm"));//検査不合格：確認依頼
                subject.append(resultInfo.getComponentCode());
                subject.append(SLASHES);
                subject.append(resultInfo.getComponentName());
                subject.append(SPACE);
                break;
            case CommonConstants.CHECK_NG_CONFIRM:
                subject.append(dictMap.get("inspection_unqualified_approver"));//検査不合格：承認依頼
                subject.append(resultInfo.getComponentCode());
                subject.append(SLASHES);
                subject.append(resultInfo.getComponentName());
                subject.append(SPACE);
                break;
            case CommonConstants.CHECK_NG_APPROVE:
                subject.append(dictMap.get("inspection_unqualified_ended"));//検査不合格：承認
                subject.append(resultInfo.getComponentCode());
                subject.append(SLASHES);
                subject.append(resultInfo.getComponentName());
                subject.append(SPACE);
                break;
            // km-976 帳票確認者の検索を追加 20181122 start
            case CommonConstants.INCOMING_FILE_DENIED:
                subject.append(dictMap.get("incoming_inspection_file_denied"));//【受入検査】帳票確認結果［否認］
                subject.append("_");
                subject.append(resultInfo.getComponentCode());
                subject.append(SLASHES);
                subject.append(resultInfo.getComponentName());
                subject.append(SPACE);
                break;
            // km-976 帳票確認者の検索を追加 20181122 end
            // KM-977 受入検査結果(合格)がサプライヤに反映されたことを通知します 2018/11/29 start
            case CommonConstants.INCOMING_INSPECTION_OK:
                subject.append(dictMap.get("incoming_inspection_ok"));//【受入検査】結果判定［合格］
                subject.append("_");
                subject.append(noticeDateFormat);
                subject.append(SPACE);
                break;
            case CommonConstants.INCOMING_INSPECTION_NG:
                subject.append(dictMap.get("incoming_inspection_ng"));//【受入検査】結果判定［不合格］
                subject.append("_");
                subject.append(resultInfo.getComponentCode());
                subject.append(SLASHES);
                subject.append(resultInfo.getComponentName());
                subject.append(SPACE);
                break;
            // KM-977 受入検査結果(合格)がサプライヤに反映されたことを通知します 2018/11/29 end
            default:
                break;
        }

        return subject.toString();
    }

    /**
     * メール本文作成
     *
     * @param processType
     * @param request
     * @param langId
     * @param dictMap
     * @return
     */
    private String makeMailBody(int processType, TblComponentInspectionSendMail resultInfo, HashMap<String, String> dictMap) {
        StringBuilder body = new StringBuilder();
        switch (processType) {
            case CommonConstants.OUTGOING_CHECK_RESULT_OK://出荷検査完了通知
                body.append(dictMap.get("department_members")); //関係各位
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_description")); //検査実績は以下の通りです
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(String.format(dictMap.get("inspection_ended_quantity"), resultInfo.getOutgoingCheckEndQuantity()));//出荷検査完了
                body.append(RETURN_CODE);
                body.append(String.format(dictMap.get("inspect_concession_quantity"), resultInfo.getOutgoingConcessionComp().size()));//出荷検査特採
                body.append(RETURN_CODE);
                body.append(String.format(dictMap.get("inspection__confirm_ended_quantity"), resultInfo.getOutgoingCheckCofirmEndQuantity()));//出荷検査確認完了
                body.append(RETURN_CODE);
                body.append(String.format(dictMap.get("inspection__approver_ended_quantity"), resultInfo.getOutgoingCheckApproveEndQuantity()));//出荷検査承認完了
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_list_url")); //検査部品リストへのURL
                body.append(FIELD_TITLE);
                body.append(resultInfo.getBaseUrl());
                body.append(OUTGOING_LIST_PATH);
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("inspection_ended_component")); //出荷検査完了部品
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                if (resultInfo.getOutgoingCheckEndComponet() != null && resultInfo.getOutgoingCheckEndComponet().size() > 0) {
                    for (String componetCode : resultInfo.getOutgoingCheckEndComponet()) {
                        body.append(componetCode);
                        body.append(RETURN_CODE);
                    }
                }
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("insp_concession_component")); //出荷検査特採
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                resultInfo.getOutgoingConcessionComp().stream().forEach(componentCode->{
                    body.append(componentCode);
                    body.append(RETURN_CODE);
                });
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("inspection_confirm_ended_component")); //出荷検査確認完了部品
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                if (resultInfo.getOutgoingCheckConfirmEndComponet() != null && resultInfo.getOutgoingCheckConfirmEndComponet().size() > 0) {
                    for (String componetCode : resultInfo.getOutgoingCheckConfirmEndComponet()) {
                        body.append(componetCode);
                        body.append(RETURN_CODE);
                    }
                }
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("inspection_approver_ended_component")); //出荷検査承認完了部品
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                if (resultInfo.getOutgoingCheckApproveEndComponet() != null && resultInfo.getOutgoingCheckApproveEndComponet().size() > 0) {
                    for (String componetCode : resultInfo.getOutgoingCheckApproveEndComponet()) {
                        body.append(componetCode);
                        body.append(RETURN_CODE);
                    }
                }
                break;
            case CommonConstants.INCOMING_CHECK_RESULT_OK://入荷検査完了通知
                body.append(dictMap.get("department_members")); //関係各位
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_description")); //検査実績は以下の通りです
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(String.format(dictMap.get("inspection_ended_quantity"), resultInfo.getIncomingCheckEndQuantity()));//入荷検査完了
                body.append(RETURN_CODE);
                body.append(String.format(dictMap.get("inspect_concession_quantity"), resultInfo.getIncomingConcessionComp().size()));//入荷検査特採
                body.append(RETURN_CODE);
                body.append(String.format(dictMap.get("inspection__confirm_ended_quantity"), resultInfo.getIncomingCheckCofirmEndQuantity()));//入荷検査確認完了
                body.append(RETURN_CODE);
                body.append(String.format(dictMap.get("inspection__approver_ended_quantity"), resultInfo.getIncomingCheckApproveEndQuantity()));//入荷検査承認完了
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_list_url")); //検査部品リストへのURL
                body.append(FIELD_TITLE);
                body.append(resultInfo.getBaseUrl());
                body.append(INCOMING_LIST_PATH);
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("inspection_ended_component")); //入荷検査完了部品
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                if (resultInfo.getIncomingCheckEndComponet() != null && resultInfo.getIncomingCheckEndComponet().size() > 0) {
                    for (String componetCode : resultInfo.getIncomingCheckEndComponet()) {
                        body.append(componetCode);
                        body.append(RETURN_CODE);
                    }
                }
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("insp_concession_component")); //受入検査特採
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                resultInfo.getIncomingConcessionComp().stream().forEach(componentCode->{
                    body.append(componentCode);
                    body.append(RETURN_CODE);
                });
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("inspection_confirm_ended_component")); //入荷検査確認完了部品
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                if (resultInfo.getIncomingCheckConfirmEndComponet() != null && resultInfo.getIncomingCheckConfirmEndComponet().size() > 0) {
                    for (String componetCode : resultInfo.getIncomingCheckConfirmEndComponet()) {
                        body.append(componetCode);
                        body.append(RETURN_CODE);
                    }
                }
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("inspection_approver_ended_component")); //入荷検査承認完了部品
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                if (resultInfo.getIncomingCheckApproveEndComponet() != null && resultInfo.getIncomingCheckApproveEndComponet().size() > 0) {
                    for (String componetCode : resultInfo.getIncomingCheckApproveEndComponet()) {
                        body.append(componetCode);
                        body.append(RETURN_CODE);
                    }
                }
                break;
            case CommonConstants.CHECK_CONFIRM_DENIAL:
            case CommonConstants.CHECK_APPROVE_DENIAL:
                body.append(dictMap.get("contact_members")); //担当者様
                body.append(RETURN_CODE);
                body.append(dictMap.get("inspection_processing_executive")); //下記の処理を実行してください
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_disavow_description"));//説明：対象部品の検査結果を否認します。下記コメントを確認してください
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_table_url")); //検査表へのＵＲＬ
                body.append(FIELD_TITLE);
                body.append(resultInfo.getBaseUrl());
                body.append(DETAIL_PATH);
                body.append(resultInfo.getResultId());
                body.append(OPERATOR);
                body.append("act");
                body.append(EQUAL);
                body.append(resultInfo.getAct());
                body.append(OPERATOR);
                body.append("functionType");
                body.append(EQUAL);
                body.append(resultInfo.getFunctionType());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_code")); //部品番号
                body.append(FIELD_TITLE);
                body.append(resultInfo.getComponentCode());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_name")); //部品名称
                body.append(FIELD_TITLE);
                body.append(resultInfo.getComponentName());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_quantity")); //個数
                body.append(FIELD_TITLE);
                body.append(resultInfo.getQuantity());
                body.append(RETURN_CODE);
                body.append(dictMap.get("disavow_comment")); //否認のコメント
                body.append(FIELD_TITLE);
                body.append(resultInfo.getDenialCommentaries() == null ? "" : resultInfo.getDenialCommentaries());
                body.append(RETURN_CODE);
                break;
            case CommonConstants.CHECK_NG_END:
                body.append(dictMap.get("contact_members")); //担当者様
                body.append(RETURN_CODE);
                body.append(dictMap.get("inspection_processing_executive")); //下記の処理を実行してください
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_unqualified_confirm_description"));//説明：対象部品の検査が不合格となりましたので、確認をお願い致します
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_table_url")); //検査表へのＵＲＬ
                body.append(FIELD_TITLE);
                body.append(resultInfo.getBaseUrl());
                body.append(DETAIL_PATH);
                body.append(resultInfo.getResultId());
                body.append(OPERATOR);
                body.append("act");
                body.append(EQUAL);
                body.append(resultInfo.getAct());
                body.append(OPERATOR);
                body.append("functionType");
                body.append(EQUAL);
                body.append(resultInfo.getFunctionType());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_code")); //部品番号
                body.append(FIELD_TITLE);
                body.append(resultInfo.getComponentCode());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_name")); //部品名称
                body.append(FIELD_TITLE);
                body.append(resultInfo.getComponentName());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_quantity")); //個数
                body.append(FIELD_TITLE);
                body.append(resultInfo.getQuantity());
                body.append(RETURN_CODE);
                break;
            case CommonConstants.CHECK_NG_CONFIRM:
                body.append(dictMap.get("contact_members")); //担当者様
                body.append(RETURN_CODE);
                body.append(dictMap.get("inspection_processing_executive")); //下記の処理を実行してください
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_unqualified_approver_description"));//説明：対象部品の検査不合格品を確認しましたので、承認をお願い致します
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_table_url")); //検査表へのＵＲＬ
                body.append(FIELD_TITLE);
                body.append(resultInfo.getBaseUrl());
                body.append(DETAIL_PATH);
                body.append(resultInfo.getResultId());
                body.append(OPERATOR);
                body.append("act");
                body.append(EQUAL);
                body.append(resultInfo.getAct());
                body.append(OPERATOR);
                body.append("functionType");
                body.append(EQUAL);
                body.append(resultInfo.getFunctionType());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_code")); //部品番号
                body.append(FIELD_TITLE);
                body.append(resultInfo.getComponentCode());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_name")); //部品名称
                body.append(FIELD_TITLE);
                body.append(resultInfo.getComponentName());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_quantity")); //個数
                body.append(FIELD_TITLE);
                body.append(resultInfo.getQuantity());
                body.append(RETURN_CODE);
                break;
            case CommonConstants.CHECK_NG_APPROVE:
                body.append(dictMap.get("contact_members")); //担当者様
                body.append(RETURN_CODE);
                body.append(dictMap.get("inspection_processing_executive")); //下記の処理を実行してください
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_unqualified_ended_description"));//説明：対象部品の検査不合格品を承認します。是正処置を実施してください
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_table_url")); //検査表へのＵＲＬ
                body.append(FIELD_TITLE);
                body.append(resultInfo.getBaseUrl());
                body.append(DETAIL_PATH);
                body.append(resultInfo.getResultId());
                body.append(OPERATOR);
                body.append("act");
                body.append(EQUAL);
                body.append(resultInfo.getAct());
                body.append(OPERATOR);
                body.append("functionType");
                body.append(EQUAL);
                body.append(resultInfo.getFunctionType());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_code")); //部品番号
                body.append(FIELD_TITLE);
                body.append(resultInfo.getComponentCode());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_name")); //部品名称
                body.append(FIELD_TITLE);
                body.append(resultInfo.getComponentName());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_quantity")); //個数
                body.append(FIELD_TITLE);
                body.append(resultInfo.getQuantity());
                body.append(RETURN_CODE);
                break;
            // km-976 帳票確認者の検索を追加 20181122 start
            case CommonConstants.INCOMING_FILE_DENIED:
                body.append(dictMap.get("file_denied_contact_members")); //担当者様
                body.append(RETURN_CODE);
                body.append(dictMap.get("inspection_file_denied_executive")); //帳票確認において上長より「否認」と判定されました。
                body.append(RETURN_CODE);
                body.append(dictMap.get("inspection_file_denied_confirm")); // 対象資料を確認願います。
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("file_denied_component_inspection_table_url")); //検査表URL
                body.append(FIELD_TITLE);
                body.append(resultInfo.getBaseUrl());
                body.append(DETAIL_PATH);
                body.append(resultInfo.getResultId());
                body.append(OPERATOR);
                body.append("act");
                body.append(EQUAL);
                body.append(resultInfo.getAct());
                body.append(OPERATOR);
                body.append("functionType");
                body.append(EQUAL);
                body.append(resultInfo.getFunctionType());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_code")); //部品コード
                body.append(FIELD_TITLE);
                body.append(resultInfo.getComponentCode());
                body.append(RETURN_CODE);
                body.append(dictMap.get("inspection_table_create_date")); //検査表作成日
                body.append(FIELD_TITLE);
                body.append(resultInfo.getInspectionCreateDate());
                body.append(RETURN_CODE);
                body.append(dictMap.get("company_code")); //会社コード
                body.append(FIELD_TITLE);
                body.append(resultInfo.getIncomingCompanyCode());
                body.append(RETURN_CODE);
                body.append(dictMap.get("outgoing_company_name")); //出荷元
                body.append(FIELD_TITLE);
                body.append(resultInfo.getOutgoingCompanyName());
                body.append(RETURN_CODE);
                body.append(dictMap.get("file_denied_comment")); //コメント
                body.append(FIELD_TITLE);
                body.append(resultInfo.getIncomingPrivateComment());
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("file_denied_end_greeting")); //よろしくお願いいたします。
                body.append(RETURN_CODE);
                body.append(dictMap.get("file_denied_end")); //以上
                break;
            // km-976 帳票確認者の検索を追加 20181122 end
            // KM-977 受入検査結果(合格)がサプライヤに反映されたことを通知します 2018/11/29 start
            case CommonConstants.INCOMING_INSPECTION_OK:
                body.append(resultInfo.getOutgoingCompanyName());
                body.append(SPACE);
                body.append(dictMap.get("incoming_inspection_contact_members")); //ご担当者様
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("incoming_inspection_ok_executive")); //弊社受入検査で「合格」と判定されました。
                body.append(RETURN_CODE);
                body.append(dictMap.get("incoming_inspection_ok_confirm")); // 詳細は下記ＵＲＬより確認をお願いいたします。
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(resultInfo.getBaseUrl());
                body.append(OUTGOING_LIST_PATH);
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("incoming_inspection_ok_end_greeting")); //よろしくお願いいたします。
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("incoming_inspection_ok_end")); //以上
                break;
            case CommonConstants.INCOMING_INSPECTION_NG:
                body.append(resultInfo.getOutgoingCompanyName());
                body.append(SPACE);
                body.append(dictMap.get("incoming_inspection_contact_members")); //ご担当者様
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("incoming_inspection_ng_executive")); //弊社受入検査で「不合格」と判定されました。
                body.append(RETURN_CODE);
                body.append(dictMap.get("incoming_inspection_ng_confirm")); // 内容を確認いただき、是正処置をお願いいたします。
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("incoming_inspection_ng_table_url")); //検査表URL
                body.append(FIELD_TITLE);
                body.append(resultInfo.getBaseUrl());
                body.append(DETAIL_PATH);
                body.append(resultInfo.getResultId());
                body.append(OPERATOR);
                body.append("act");
                body.append(EQUAL);
                body.append(resultInfo.getAct());
                body.append(OPERATOR);
                body.append("functionType");
                body.append(EQUAL);
                body.append(resultInfo.getFunctionType());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_code")); //部品コード
                body.append(FIELD_TITLE);
                body.append(resultInfo.getComponentCode());
                body.append(RETURN_CODE);
                body.append(dictMap.get("component_inspection_po")); //P/O
                body.append(FIELD_TITLE);
                body.append(resultInfo.getPo());
                body.append(RETURN_CODE);
                body.append(dictMap.get("manufacture_lot")); //製造ロット
                body.append(FIELD_TITLE);
                body.append(resultInfo.getLotNum());
                body.append(RETURN_CODE);
                body.append(dictMap.get("incoming_inspection_date")); //受入検査日
                body.append(FIELD_TITLE);
                body.append(resultInfo.getIncomingInspectionDate());
                body.append(RETURN_CODE);
                body.append(dictMap.get("incoming_inspection_ng_comment")); //コメント
                body.append(FIELD_TITLE);
                body.append(resultInfo.getIncomingComment());
                body.append(RETURN_CODE);
                body.append(DIVIDING_LINE);
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("incoming_inspection_ng_end_greeting")); //よろしくお願いいたします。
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(dictMap.get("incoming_inspection_ng_end")); //以上
                break;
            // KM-977 受入検査結果(合格)がサプライヤに反映されたことを通知します 2018/11/29 end
            default:
                break;
        }

        return body.toString();
    }

    /**
     * 文言ハッシュマップを取得
     *
     * @param langId
     * @return
     */
    private HashMap<String, String> getDictionaryMap(String langId) {
        //その言語の文言をすでに取得済みなら言語別文言ハッシュマップから返す
        HashMap<String, String> dictMap = langDictsMap.get(langId);
        if (dictMap != null) {
            return dictMap;
        }
        dictMap = mstDictionaryService.getDictionaryHashMap(langId, DICT_KEYS);
        langDictsMap.put(langId, dictMap);
        return dictMap;
    }

}
