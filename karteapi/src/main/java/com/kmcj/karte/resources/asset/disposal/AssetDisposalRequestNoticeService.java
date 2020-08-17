/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.disposal;

import com.kmcj.karte.MailSender;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.MstUserService;
import com.kmcj.karte.resources.user.mail.reception.MstUserMailReceptionService;
import com.kmcj.karte.util.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
//import java.util.logging.Level;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author f.kitaoji
 */
@Dependent
public class AssetDisposalRequestNoticeService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    @Inject
    MstUserMailReceptionService mstUserMailReceptionService;
    @Inject
    MstUserService mstUserService;
    @Inject
    MstDictionaryService mstDictionaryService;
    @Inject
    MstChoiceService mstChoiceService;
    @Inject
    MailSender mailSender;
    
    public static final int PT_REQUESTED = 1;
    public static final int PT_RECEIVED = 2;
    public static final int PT_CONFIRMED = 3;
    public static final int PT_AP_CONFIRMED = 4;
    public static final int PT_FINAL_CONFIRMED = 5;
    
    private final static List<String> DICT_KEYS;
    static {
        List<String> list = new ArrayList<>();
        list.add("asset_disposal_mail_subject_registration");               //資産廃棄申請登録通知
        list.add("asset_disposal_mail_body_registration");                  //以下の資産廃棄申請が登録されました。
        list.add("asset_disposal_mail_subject_reception");                  //資産廃棄申請受付通知
        list.add("asset_disposal_mail_body_reception");                     //以下の資産廃棄申請が受付されました。
        list.add("asset_disposal_mail_body_reception_reject");              //以下の資産廃棄申請は却下されました。
        list.add("asset_disposal_mail_body_reception_na");                  //以下の資産廃棄申請は対象となる資産がありませんでした。
        list.add("asset_disposal_mail_subject_confirmation");               //資産廃棄申請確認通知
        list.add("asset_disposal_mail_body_confirmation");                  //以下の資産廃棄申請が確認されました。
        list.add("asset_disposal_mail_subject_ap_confirmation");            //資産廃棄申請AP確認通知
        list.add("asset_disposal_mail_body_ap_confirmation");               //以下の資産廃棄申請のアフターパーツ確認が完了しました。
        list.add("asset_disposal_mail_body_ap_confirmation_reject");        //以下の資産廃棄申請はアフターパーツ確認により却下されました。
        list.add("asset_disposal_mail_subject_final_confirmation");         //資産廃棄申請最終確認通知
        list.add("asset_disposal_mail_body_final_confirmation");            //以下の資産廃棄申請の最終確認が完了しました。廃棄処理の進め方については、別途、担当者よりご連絡申し上げます。
        list.add("asset_disposal_mail_body_final_confirmation_reject");     //以下の資産廃棄申請の最終確認にて却下されました。
        list.add("asset_disposal_request_no");                              //申請番号
        list.add("asset_disposal_request_date");                            //申請日
        list.add("company_name");                                           //会社名称
        list.add("request_user_name");                                      //申請者氏名
        list.add("item_code");                                              //品目コード
        list.add("disposal_reason");                                        //廃棄理由
        list.add("disposal_request_reason_other");                          //その他理由
        list.add("asset_no");                                               //資産番号
        list.add("branch_no");                                              //補助番号
        list.add("receive_date");                                           //受付日
        list.add("receive_user_name");                                      //受付者
        list.add("external_status_dismissal");                              //却下
        list.add("receive_reject_reason");                                  //受付却下理由
        list.add("external_status_no_target");                              //対象無
        list.add("asset_mgmt_confirm_date");                                //確認日
        list.add("asset_mgmt_confirm_user_name");                           //確認者
        list.add("ap_confirm_date");                                        //AP確認日
        list.add("ap_confirm_user_name");                                   //AP確認者
        list.add("ap_reject_reason");                                       //AP却下理由
        list.add("final_reject_reason");                                    //最終却下理由
        list.add("final_reply_date");                                       //最終回答日
        list.add("final_reply_user_name");                                  //最終回答者
	DICT_KEYS = Collections.unmodifiableList(list);
    }
    private final static String FIELD_TITLE = ": ";
    //private final static String FIELD_SEPARATOR = "/";
    private final static String SPACE = " ";
    private final static String RETURN_CODE = "\r\n";
    private HashMap<String, HashMap<String, String>> langDictsMap = new HashMap<>(); //言語別に文言ハッシュマップを保持
    private final static String QUERY_GET_ASSET_DISPOSAL_REQUEST =
        " SELECT t FROM TblAssetDisposalRequest t LEFT JOIN FETCH t.fromMstCompany LEFT JOIN FETCH t.receiveMstUser " +
            " LEFT JOIN FETCH t.assetMgmtConfirmMstUser LEFT JOIN FETCH t.finalReplyMstUser " +
        " WHERE t.uuid = :uuid ";
    
    /**
     * 資産廃棄申請管理メール通知処理メイン
     * @param processType
     * @param requestUuids 
     */
    @Transactional
    public void sendNotice(int processType, String[] requestUuids) {
        if (requestUuids == null || requestUuids.length <= 0) return;
        
        //処理種別(processType)よりメール設定で受信すべきユーザーを取得
        String eventId = null;
        switch (processType) {
            case PT_REQUESTED:
                eventId = "mail004";
                break;
            case PT_RECEIVED:
                eventId = "mail005";
                break;
            case PT_CONFIRMED:
                eventId = "mail006";
                break;
            case PT_AP_CONFIRMED:
                eventId = "mail007";
                break;
            case PT_FINAL_CONFIRMED:
                eventId = "mail008";
                break;
        }
        if (eventId == null) return;
        

        //メール設定で受信すべきユーザーを取得。該当なしだとしても受付者にはメール送信する
        List<MstUser> mailSettingUsers = mstUserMailReceptionService.getMailReceiveDepartmentUsers(eventId);

        //申請データ取得クエリの準備
        Query query = entityManager.createQuery(QUERY_GET_ASSET_DISPOSAL_REQUEST);
        //パラメータで渡された申請UUIDのリストをループ
        for (String requestUuid : requestUuids) {
            //申請レコード取得
            query.setParameter("uuid", requestUuid);
            TblAssetDisposalRequest request = null;
            try {
                request = (TblAssetDisposalRequest) query.getSingleResult();
            }
            catch (NoResultException e) {
                //do nothing
            }
            if (request != null && checkStatus(processType, request)) {
                //メール設定で受信すべきユーザーに受付者が含まれているか？
                MstUser receiveUser = null;
                boolean included = false; //メール設定受信ユーザーに受付者が含まれるか？
                boolean sentReceiveUser = false; //受付者にメール送信をしたか？
                for (MstUser user : mailSettingUsers) {
                    if (request.getReceiveUserUuid() != null && request.getReceiveUserUuid().equals(user.getUuid())) {
                        included = true;
                        break;
                    }
                }
                //含まれていなければ受付者のユーザー情報を取得
                if (!included && request.getReceiveUserUuid() != null) {
                    receiveUser = request.getReceiveMstUser();
                }
                
                //メール作成、送信
                if (mailSettingUsers.isEmpty()) {
                    //メール設定受信ユーザーがいなければ受付者ユーザーだけに送信
                    if (receiveUser != null) {
                        List<MstUser> users = new ArrayList<>();
                        users.add(receiveUser);
                        sendMail(processType, request, users, receiveUser.getLangId());
                        //sentReceiveUser = true;
                    }
                }
                else {
                    //言語別にメール作成送信
                    String oldLangId = mailSettingUsers.get(0).getLangId();
                    List<MstUser> users = new ArrayList<>();
                    for (MstUser user : mailSettingUsers) {
                        //メール受信ユーザ取得クエリは言語でソートされているので、ブレークしたら送信処理実行
                        if (!user.getLangId().equals(oldLangId)) {
                            if (!included && receiveUser != null) {
                                //受付者がメール設定ユーザーに含まれていないとき、同じ言語のリストに追加する
                                if (receiveUser.getLangId().equals(oldLangId)) {
                                    users.add(receiveUser);
                                    sentReceiveUser = true;
                                }
                            }
                            sendMail(processType, request, users, oldLangId);
                            users.clear();
                            users.add(user);
                            oldLangId = user.getLangId();
                        }
                        else {
                            users.add(user);
                        }
                    }
                    if (users.size() > 0) {
                        if (!included && receiveUser != null) {
                            //受付者がメール設定ユーザーに含まれていないとき、同じ言語のリストに追加する
                            if (receiveUser.getLangId().equals(oldLangId)) {
                                users.add(receiveUser);
                                sentReceiveUser = true;
                            }
                        }
                        sendMail(processType, request, users, oldLangId);
                    }
                    if (!sentReceiveUser && !included && receiveUser != null) {
                        users.clear();
                        users.add(receiveUser);
                        sendMail(processType, request, users, receiveUser.getLangId());
                    }
                }
                //メール送信済みフラグを立てる
                updateMailSentFlag(processType, request);
            }
            
        }
    }
    
    /**
     * 申請データのステータスをチェックしメール送信する必要があるかを返す
     * @param request
     * @return 
     */
    private boolean checkStatus(int processType, TblAssetDisposalRequest request) {
        boolean result = false;
        if (processType == PT_REQUESTED) {
            //ステータス=1:申請済　かつ
            //登録通知メール未送信
            result = request.getInternalStatus() == 1 && request.getMailSentRegistration() != 1;
        }
        else if (processType == PT_RECEIVED) {
            //ステータス＝2：受付済、8：対象無、9：却下　かつ
            //受付通知メール未送信
            result = (request.getInternalStatus() == 2 ||
                    request.getInternalStatus() == 8 ||
                    request.getInternalStatus() == 9) && 
                    request.getMailSentReception() != 1;
        }
        else if (processType == PT_CONFIRMED) {
            //ステータス＝3：確認済 かつ
            //確認通知メール未送信
            result = request.getInternalStatus() == 3 && request.getMailSentConfirmation() != 1;
        }
        else if (processType == PT_AP_CONFIRMED) {
            //ステータス＝4：AP確認済、9：却下 かつ
            //AP確認通知メール未送信
            result = (request.getInternalStatus() == 4 ||
                    request.getInternalStatus() == 9) && 
                    request.getMailSentApConfirmation() != 1;
        }
        else if (processType == PT_FINAL_CONFIRMED) {
            //ステータス＝5：最終確認済、6：廃棄処理済み、9：却下 かつ
            //最終確認通知メール未送信
            result = (request.getInternalStatus() == 5 ||
                    request.getInternalStatus() == 6 ||
                    request.getInternalStatus() == 9) && 
                    request.getMailSentFinalConfirmation() != 1;
        }
        return result;
    }
    
    /**
     * ユーザーの言語別にメール作成・送信
     * @param request
     * @param users
     * @param langId 
     */
    private void sendMail(int processType, TblAssetDisposalRequest request, List<MstUser> users, String langId) {
        //メールに使う文言を取得
        HashMap<String, String> dictMap = getDictionaryMap(langId);
        //件名作成
        String subject = makeMailSubject(processType, request, langId, dictMap);        
        //本文作成
        String body = makeMailBody(processType, request, langId, dictMap);        

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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * メール件名作成
     * @param processType
     * @param request
     * @param langId
     * @return 
     */
    private String makeMailSubject(int processType, TblAssetDisposalRequest request, String langId, HashMap<String, String> dictMap) {
        StringBuilder subject = new StringBuilder();
        if (processType == PT_REQUESTED) {
            subject.append(dictMap.get("asset_disposal_mail_subject_registration"));
            subject.append(SPACE);
            subject.append(request.getRequestNo());
        }
        else if (processType == PT_RECEIVED) {
            subject.append(dictMap.get("asset_disposal_mail_subject_reception"));
            subject.append(SPACE);
            subject.append(request.getRequestNo());
            if (request.getInternalStatus() == 8) {
                //ステータスが対象無のとき
                subject.append(SPACE);
                subject.append(dictMap.get("external_status_no_target"));
            }
            else if (request.getInternalStatus() == 9) {
                //ステータスが却下のとき
                subject.append(SPACE);
                subject.append(dictMap.get("external_status_dismissal"));
            }
        }
        else if (processType == PT_CONFIRMED) {
            subject.append(dictMap.get("asset_disposal_mail_subject_confirmation"));
            subject.append(SPACE);
            subject.append(request.getRequestNo());
        }
        else if (processType == PT_AP_CONFIRMED) {
            subject.append(dictMap.get("asset_disposal_mail_subject_ap_confirmation"));
            subject.append(SPACE);
            subject.append(request.getRequestNo());
            if (request.getInternalStatus() == 9) {
                //ステータスが却下のとき
                subject.append(SPACE);
                subject.append(dictMap.get("external_status_dismissal"));
            }
        }
        else if (processType == PT_FINAL_CONFIRMED) {
            subject.append(dictMap.get("asset_disposal_mail_subject_final_confirmation"));
            subject.append(SPACE);
            subject.append(request.getRequestNo());
            if (request.getInternalStatus() == 9) {
                //ステータスが却下のとき
                subject.append(SPACE);
                subject.append(dictMap.get("external_status_dismissal"));
            }
        }
        return subject.toString();
    }
    
    /**
     * メール本文作成
     * @param processType
     * @param request
     * @param langId
     * @param dictMap
     * @return 
     */
    private String makeMailBody(int processType, TblAssetDisposalRequest request, String langId, HashMap<String, String> dictMap) {
        StringBuilder body = new StringBuilder();
        if (processType == PT_REQUESTED) {
            body.append(dictMap.get("asset_disposal_mail_body_registration")); //以下の資産廃棄申請が登録されました。
            body.append(RETURN_CODE);
            body.append(RETURN_CODE);
            body.append(makeMailBodyRequestInfo(request, langId, dictMap));
        }
        else if (processType == PT_RECEIVED) {
            if (request.getInternalStatus() == 8) {
                //ステータスが対象無のとき
                body.append(dictMap.get("asset_disposal_mail_body_reception_na")); //以下の資産廃棄申請は対象となる資産がありませんでした。
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(makeMailBodyRequestInfo(request, langId, dictMap));
                body.append(makeMailBodyReceiveUserInfo(request, dictMap));
            }
            else if (request.getInternalStatus() == 9) {
                //ステータスが却下のとき
                body.append(dictMap.get("asset_disposal_mail_body_reception_reject")); //以下の資産廃棄申請は却下されました。
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(makeMailBodyRequestInfo(request, langId, dictMap));
                body.append(makeMailBodyAssetInfo(request, dictMap));
                body.append(makeMailBodyReceiveUserInfo(request, dictMap));
                body.append(dictMap.get("receive_reject_reason")); //受付却下理由
                body.append(FIELD_TITLE);
                body.append(getReceiveRejectReasonChoice(request.getReceiveRejectReason(), langId));
            }
            else {
                //ステータスが2：受付済のとき
                body.append(dictMap.get("asset_disposal_mail_body_reception")); //以下の資産廃棄申請が受付されました。
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(makeMailBodyRequestInfo(request, langId, dictMap));
                body.append(makeMailBodyAssetInfo(request, dictMap));
                body.append(makeMailBodyReceiveUserInfo(request, dictMap));
            }
        }
        else if (processType == PT_CONFIRMED) {
            body.append(dictMap.get("asset_disposal_mail_body_confirmation")); //以下の資産廃棄申請が確認されました。
            body.append(RETURN_CODE);
            body.append(RETURN_CODE);
            body.append(makeMailBodyRequestInfo(request, langId, dictMap));
            body.append(makeMailBodyAssetInfo(request, dictMap));
            body.append(makeMailBodyReceiveUserInfo(request, dictMap));
            body.append(makeMailBodyConfirmUserInfo(request, dictMap));
        }
        else if (processType == PT_AP_CONFIRMED) {
            if (request.getInternalStatus() == 9) {
                //ステータスが却下のとき
                body.append(dictMap.get("asset_disposal_mail_body_ap_confirmation_reject")); //以下の資産廃棄申請はアフターパーツ確認により却下されました。
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(makeMailBodyRequestInfo(request, langId, dictMap));
                body.append(makeMailBodyAssetInfo(request, dictMap));
                body.append(makeMailBodyReceiveUserInfo(request, dictMap));
                body.append(makeMailBodyConfirmUserInfo(request, dictMap));
                body.append(makeMailBodyApConfirmUserInfo(request, dictMap));
                body.append(dictMap.get("ap_reject_reason")); //AP却下理由
                body.append(FIELD_TITLE);
                body.append(getApRejectReasonChoice(request.getApRejectReason(), langId));
            }
            else {
                body.append(dictMap.get("asset_disposal_mail_body_ap_confirmation")); //以下の資産廃棄申請のアフターパーツ確認が完了しました。
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(makeMailBodyRequestInfo(request, langId, dictMap));
                body.append(makeMailBodyAssetInfo(request, dictMap));
                body.append(makeMailBodyReceiveUserInfo(request, dictMap));
                body.append(makeMailBodyConfirmUserInfo(request, dictMap));
                body.append(makeMailBodyApConfirmUserInfo(request, dictMap));
            }
        }
        else if (processType == PT_FINAL_CONFIRMED) {
            if (request.getInternalStatus() == 9) {
                //ステータスが却下のとき
                body.append(dictMap.get("asset_disposal_mail_body_final_confirmation_reject")); //以下の資産廃棄申請の最終確認にて却下されました。
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(makeMailBodyRequestInfo(request, langId, dictMap));
                body.append(makeMailBodyAssetInfo(request, dictMap));
                body.append(makeMailBodyReceiveUserInfo(request, dictMap));
                body.append(makeMailBodyConfirmUserInfo(request, dictMap));
                body.append(makeMailBodyApConfirmUserInfo(request, dictMap));
                body.append(makeMailBodyFinalConfirmUserInfo(request, dictMap));
                body.append(dictMap.get("final_reject_reason")); //AP却下理由
                body.append(FIELD_TITLE);
                body.append(getFinalRejectReasonChoice(request.getFinalRejectReason(), langId));
            }
            else {
                body.append(dictMap.get("asset_disposal_mail_body_final_confirmation")); //以下の資産廃棄申請の最終確認が完了しました。廃棄処理の進め方については、別途、担当者よりご連絡申し上げます。
                body.append(RETURN_CODE);
                body.append(RETURN_CODE);
                body.append(makeMailBodyRequestInfo(request, langId, dictMap));
                body.append(makeMailBodyAssetInfo(request, dictMap));
                body.append(makeMailBodyReceiveUserInfo(request, dictMap));
                body.append(makeMailBodyConfirmUserInfo(request, dictMap));
                body.append(makeMailBodyApConfirmUserInfo(request, dictMap));
                body.append(makeMailBodyFinalConfirmUserInfo(request, dictMap));
            }
        }
        
        return body.toString();
    }

    /**
     * メール本文に記載する申請情報文字列を取得
     * @param request
     * @param langId
     * @param dictMap
     * @return 
     */
    private String makeMailBodyRequestInfo(TblAssetDisposalRequest request, String langId, HashMap<String, String> dictMap) {
        StringBuilder body = new StringBuilder();
        body.append(dictMap.get("asset_disposal_request_no")); //申請番号
        body.append(FIELD_TITLE);
        body.append(request.getRequestNo());
        body.append(RETURN_CODE);
        body.append(dictMap.get("asset_disposal_request_date")); //申請日
        body.append(FIELD_TITLE);
        body.append(DateFormat.dateToStr(request.getRequestDate(), DateFormat.DATE_FORMAT));
        body.append(RETURN_CODE);
        body.append(dictMap.get("company_name")); //会社名称
        body.append(FIELD_TITLE);
        if (request.getFromMstCompany() != null && request.getFromMstCompany().getCompanyName() != null) {
            body.append(request.getFromMstCompany().getCompanyName());
        }
        body.append(RETURN_CODE);
        body.append(dictMap.get("request_user_name")); //申請者氏名
        body.append(FIELD_TITLE);
        if (request.getRequestUserName() != null) {
            body.append(request.getRequestUserName());
        }
        body.append(RETURN_CODE);
        body.append(dictMap.get("item_code")); //品目コード
        body.append(FIELD_TITLE);
        if (request.getItemCode() != null) {
            body.append(request.getItemCode());
        }
        body.append(RETURN_CODE);
        body.append(dictMap.get("disposal_reason")); //廃棄理由
        body.append(FIELD_TITLE);
        body.append(getDisposalRequestReasonChoice(request.getDisposalRequestReason(), langId));
        body.append(RETURN_CODE);
        body.append(dictMap.get("disposal_request_reason_other")); //その他理由
        body.append(FIELD_TITLE);
        if (request.getDisposalRequestReasonOther() != null) {
            body.append(request.getDisposalRequestReasonOther());
        }
        body.append(RETURN_CODE);
        return body.toString();
    }

    /**
     * メール本文に記載する資産情報文字列を作成
     * @param request
     * @param langId
     * @param dictMap
     * @return 
     */
    private String makeMailBodyAssetInfo(TblAssetDisposalRequest request, HashMap<String, String> dictMap) {
        StringBuilder body = new StringBuilder();
        body.append(dictMap.get("asset_no")); //資産番号
        body.append(FIELD_TITLE);
        if (request.getAssetNo() != null) {
            body.append(request.getAssetNo());
        }
        body.append(RETURN_CODE);
        body.append(dictMap.get("branch_no")); //補助番号
        body.append(FIELD_TITLE);
        if (request.getBranchNo() != null) {
            body.append(request.getBranchNo());
        }
        body.append(RETURN_CODE);
        return body.toString();
    }
    
    /**
     * メール本文に記載する受付者情報を取得
     * @param request
     * @param dictMap
     * @return 
     */
    private String makeMailBodyReceiveUserInfo(TblAssetDisposalRequest request, HashMap<String, String> dictMap) {
        StringBuilder body = new StringBuilder();
        body.append(dictMap.get("receive_user_name")); //受付者
        body.append(FIELD_TITLE);
        if (request.getReceiveMstUser() != null && request.getReceiveMstUser().getUserName() != null) {
            body.append(request.getReceiveMstUser().getUserName());
        }
        body.append(RETURN_CODE);
        body.append(dictMap.get("receive_date")); //受付日
        body.append(FIELD_TITLE);
        if (request.getReceiveDate() != null) {
            body.append(DateFormat.dateToStr(request.getReceiveDate(), DateFormat.DATE_FORMAT));
        }
        body.append(RETURN_CODE);
        return body.toString();
    }
        
    /**
     * メール本文に記載する確認者情報を取得
     * @param request
     * @param dictMap
     * @return 
     */
    private String makeMailBodyConfirmUserInfo(TblAssetDisposalRequest request, HashMap<String, String> dictMap) {
        StringBuilder body = new StringBuilder();
        body.append(dictMap.get("asset_mgmt_confirm_user_name")); //確認者
        body.append(FIELD_TITLE);
        if (request.getAssetMgmtConfirmMstUser() != null && request.getAssetMgmtConfirmMstUser().getUserName() != null) {
            body.append(request.getAssetMgmtConfirmMstUser().getUserName());
        }
        body.append(RETURN_CODE);
        body.append(dictMap.get("asset_mgmt_confirm_date")); //確認日
        body.append(FIELD_TITLE);
        if (request.getAssetMgmtConfirmDate() != null) {
            body.append(DateFormat.dateToStr(request.getAssetMgmtConfirmDate(), DateFormat.DATE_FORMAT));
        }
        body.append(RETURN_CODE);
        return body.toString();
    }
    
    /**
     * メール本文に記載するAP確認者情報を取得
     * @param request
     * @param dictMap
     * @return 
     */
    private String makeMailBodyApConfirmUserInfo(TblAssetDisposalRequest request, HashMap<String, String> dictMap) {
        StringBuilder body = new StringBuilder();
        body.append(dictMap.get("ap_confirm_user_name")); //AP確認者
        body.append(FIELD_TITLE);
        if (request.getApConfirmMstUser() != null && request.getApConfirmMstUser().getUserName() != null) {
            body.append(request.getApConfirmMstUser().getUserName());
        }
        body.append(RETURN_CODE);
        body.append(dictMap.get("ap_confirm_date")); //AP確認日
        body.append(FIELD_TITLE);
        if (request.getApConfirmDate() != null) {
            body.append(DateFormat.dateToStr(request.getApConfirmDate(), DateFormat.DATE_FORMAT));
        }
        body.append(RETURN_CODE);
        return body.toString();
    }

    /**
     * メール本文に記載する最終確認者情報を取得
     * @param request
     * @param dictMap
     * @return 
     */
    private String makeMailBodyFinalConfirmUserInfo(TblAssetDisposalRequest request, HashMap<String, String> dictMap) {
        StringBuilder body = new StringBuilder();
        body.append(dictMap.get("final_reply_user_name")); //最終回答者
        body.append(FIELD_TITLE);
        if (request.getFinalReplyMstUser() != null && request.getFinalReplyMstUser().getUserName() != null) {
            body.append(request.getFinalReplyMstUser().getUserName());
        }
        body.append(RETURN_CODE);
        body.append(dictMap.get("final_reply_user_name")); //最終回答者
        body.append(FIELD_TITLE);
        if (request.getFinalReplyDate() != null) {
            body.append(DateFormat.dateToStr(request.getFinalReplyDate(), DateFormat.DATE_FORMAT));
        }
        body.append(RETURN_CODE);
        return body.toString();
    }

    /**
     * 文言ハッシュマップを取得
     * @param langId
     * @return 
     */
    private HashMap<String, String> getDictionaryMap(String langId) {
        //その言語の文言をすでに取得済みなら言語別文言ハッシュマップから返す
        HashMap<String, String> dictMap = langDictsMap.get(langId);
        if (dictMap != null) {
            return dictMap;
        }
        //取得済みでないならDBより取得
        dictMap = mstDictionaryService.getDictionaryHashMap(langId, DICT_KEYS);
        //言語別文言ハッシュマップに格納
        langDictsMap.put(langId, dictMap);
        return dictMap;
    }
    
    /**
     * 廃棄申請理由選択肢の文言取得
     * @param seq
     * @param langId
     * @return 
     */
    private String getDisposalRequestReasonChoice(int seq, String langId) {
        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(seq), langId, "tbl_asset_disposal_request.disposal_request_reason");
        String result = (choice == null) ? "" : choice.getChoice();
        return result;
    }

    /**
     * 受付却下理由選択肢の文言取得
     * @param seq
     * @param langId
     * @return 
     */
    private String getReceiveRejectReasonChoice(int seq, String langId) {
        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(seq), langId, "tbl_asset_disposal_request.receive_reject_reason");
        String result = (choice == null) ? "" : choice.getChoice();
        return result;
    }

    /**
     * AP却下理由選択肢の文言取得
     * @param seq
     * @param langId
     * @return 
     */
    private String getApRejectReasonChoice(int seq, String langId) {
        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(seq), langId, "tbl_asset_disposal_request.ap_reject_reason");
        String result = (choice == null) ? "" : choice.getChoice();
        return result;
    }
    
    /**
     * 最終却下理由選択肢の文言取得
     * @param seq
     * @param langId
     * @return 
     */
    private String getFinalRejectReasonChoice(int seq, String langId) {
        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(seq), langId, "tbl_asset_disposal_request.final_reject_reason");
        String result = (choice == null) ? "" : choice.getChoice();
        return result;
    }

    /**
     * メール送信済みフラグを申請レコードに立てる
     * @param processType
     * @param request 
     */
    @Transactional
    private void updateMailSentFlag(int processType, TblAssetDisposalRequest request) {
        StringBuilder sbUpdate = new StringBuilder();
        sbUpdate.append("UPDATE TblAssetDisposalRequest ");
        if (processType == PT_REQUESTED) {
            sbUpdate.append("SET mailSentRegistration = 1 ");
        }
        else if (processType == PT_RECEIVED) {
            sbUpdate.append("SET mailSentReception = 1 ");
        }
        else if (processType == PT_CONFIRMED) {
            sbUpdate.append("SET mailSentConfirmation = 1 ");
        }
        else if (processType == PT_AP_CONFIRMED) {
            sbUpdate.append("SET mailSentApConfirmation = 1 ");
        }
        else if (processType == PT_FINAL_CONFIRMED) {
            sbUpdate.append("SET mailSentFinalConfirmation = 1 ");
        }
        else {
            return;
        }
        sbUpdate.append("WHERE uuid = :uuid ");
        Query query = entityManager.createQuery(sbUpdate.toString());
        query.setParameter("uuid", request.getUuid());
        query.executeUpdate();
    }
}
