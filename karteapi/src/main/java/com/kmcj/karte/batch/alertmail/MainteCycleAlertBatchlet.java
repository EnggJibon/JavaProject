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
import com.kmcj.karte.resources.machine.maintenance.recomend.TblMachineMaintenanceRecomend;
import com.kmcj.karte.resources.mold.maintenance.recomend.TblCombinedMaintenanceRecommend;
import com.kmcj.karte.resources.mold.maintenance.recomend.TblMoldMaintenanceRecomend;
import com.kmcj.karte.resources.mold.part.maintenance.recommend.TblMoldPartMaintenanceRecomendDetail;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.mail.reception.MstUserMailReceptionService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author f.kitaoji
 */
@Named
@Dependent
public class MainteCycleAlertBatchlet extends AbstractBatchlet {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    MstDictionaryService mstDictionaryService;
    @Inject
    MstChoiceService mstChoiceService;
    @Inject
    MailSender mailSender;
    @Inject
    MstUserMailReceptionService mstUserMailReceptionService;
    
    private final static Level logLevel = Level.FINE; //テスト時:Level.INFO 本番時:Level.FINE
    private Logger logger = Logger.getLogger(MainteCycleAlertBatchlet.class.getName());
    private String noticeDate;
    private final static String COLUMN_SEPARATOR = ",";
    private final static String RETURN_CODE = "\r\n";
    
    //金型メンテ候補テーブル取得クエリ
    private final static String QUERY_GET_MOLD_MAINTE_RECOMEND = 
        " SELECT moldRecomend FROM TblMoldMaintenanceRecomend moldRecomend JOIN FETCH moldRecomend.mstMold mstMold JOIN FETCH moldRecomend.tblMaintenanceCyclePtn cyclePtn " +
        " WHERE moldRecomend.maintainedFlag = 0 AND (mstMold.mainteStatus = 0 OR mstMold.mainteStatus IS NULL) AND mstMold.department IS NOT NULL " + //所属NULLははじいておく
        " ORDER BY mstMold.department, moldRecomend.alertMainteType, moldRecomend.createDate, mstMold.moldId ";
    
    //金型部品メンテナンス候補表取得クエリ
    private final static String QUERY_GET_MOLD_PART_MAINTE_RECOMEND = 
        " SELECT moldPartRecomend FROM TblMoldPartMaintenanceRecomendDetail moldPartRecomend JOIN FETCH moldPartRecomend.mstMold mstMold JOIN FETCH moldPartRecomend.mstMoldPartRel mstMoldPartRel " +
        " WHERE moldPartRecomend.maintainedFlag = 0 AND (mstMold.mainteStatus = 0 OR mstMold.mainteStatus IS NULL) AND mstMold.department IS NOT NULL " + //所属NULLははじいておく
        "AND NOT EXISTS (SELECT f FROM TblMoldMaintenanceRecomend f WHERE moldPartRecomend.moldUuid = f.moldUuid AND f.maintainedFlag = 0)" + //Not get mold part which mold of this mold part exist in TblMoldMaintenanceRecomend
        " ORDER BY mstMold.department, moldPartRecomend.createDate, mstMold.moldId ";
    
    //設備メンテ候補テーブル取得クエリ
    private final static String QUERY_GET_MAC_MAINTE_RECOMEND = 
        " SELECT macRecomend FROM TblMachineMaintenanceRecomend macRecomend JOIN FETCH macRecomend.mstMachine mstMachine JOIN FETCH macRecomend.tblMaintenanceCyclePtn cyclePtn " +
        " WHERE macRecomend.maintainedFlag = 0 AND (mstMachine.mainteStatus = 0 OR mstMachine.mainteStatus IS NULL) AND mstMachine.department IS NOT NULL " + //所属NULLははじいておく
        " ORDER BY mstMachine.department, macRecomend.alertMainteType, macRecomend.createDate, mstMachine.machineId ";

    //メール受信ユーザー取得クエリ
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
                " AND d.department = :department " + //設定テーブルの所属が指定に等しい
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
                " ) AND m.department = :department2 " + //ユーザーマスタの所属が指定に等しい
            " )) " +
            " AND m.mailAddress IS NOT NULL " +
            " AND m.department IS NOT NULL " +
            " ORDER BY m.langId ";
            
            
    
    @Override
    public String process() {
        //通知日
        noticeDate = DateFormat.dateToStr(new java.util.Date(), DateFormat.DATE_FORMAT);
        processMold();
        processMachine();
        return BatchStatus.COMPLETED.toString();
    }
    
    /**
     * 金型の通知処理
     */
    private void processMold() {
        //金型メンテナンス候補テーブルを取得
        //所属、アラート・メンテ区分別にソートされている
        List<TblMoldMaintenanceRecomend> moldRecomends = getMoldMaintenanceRecomend();
        List<TblMoldPartMaintenanceRecomendDetail> moldPartRecomends = getMoldPartMaintenanceRecomend();
        if (moldRecomends.size() <= 0 && moldPartRecomends.size() <=0) {
            return;
        }
        List<TblCombinedMaintenanceRecommend> moldMaintRecommends = generateCombinedMaintRecommends(moldRecomends);
        List<TblCombinedMaintenanceRecommend> combinedMaintRecommends = combineMaintRecommendToMoldPartRecommend(moldMaintRecommends, moldRecomends, moldPartRecomends);
        
        if(!combinedMaintRecommends.isEmpty()){
            processMoldRecomends(combinedMaintRecommends);
        }
    }
    
    private void processMoldRecomends(List<TblCombinedMaintenanceRecommend> moldRecomends){
        int oldDepartment = moldRecomends.get(0).getMstMold().getDepartment(); //所属ブレーク判定用
        List<TblCombinedMaintenanceRecommend> moldRecomendsDepartment = new ArrayList<>();
        //金型メンテナンス候補レコードループ
        for (TblCombinedMaintenanceRecommend moldRecomend : moldRecomends) {
            logger.log(logLevel, moldRecomend.getMstMold().getMoldName());
            //所属が変わったらブレーク
            if (moldRecomend.getMstMold().getDepartment() != oldDepartment) {
                //メール送信処理へ
                sendMoldMailByDepartment(moldRecomendsDepartment);
                moldRecomendsDepartment.clear();
                moldRecomendsDepartment.add(moldRecomend);
                oldDepartment = moldRecomend.getMstMold().getDepartment();
            }
            else {
                //所属ごとのリストに加える
                moldRecomendsDepartment.add(moldRecomend);
            }
        }
        if (moldRecomendsDepartment.size() > 0) {
            sendMoldMailByDepartment(moldRecomendsDepartment);
        }
    }
    
    /**
     * 部署ごとのメール送信処理(金型)
     * @param moldRecomends 
     */
    private void sendMoldMailByDepartment(List<TblCombinedMaintenanceRecommend> moldRecomends) {
        if (moldRecomends.size() <= 0) return;
        //対象の所属を取得
        int department = moldRecomends.get(0).getMstMold().getDepartment();
        //宛先取得
        //その所属で送信すべきユーザーのリストを取得
        /*
        Query query = entityManager.createQuery(QUERY_GET_MAIL_RECEIVE_USER);
        query.setParameter("eventUuid", "mail001"); //メンテナンスサイクル通知の通知イベントID
        query.setParameter("eventUuid2", "mail001"); //メンテナンスサイクル通知の通知イベントID
        query.setParameter("department", department); //所属Seq
        query.setParameter("department2", String.valueOf(department)); //所属Seq
        List<MstUser> users = query.getResultList();
        */
        List<MstUser> users = mstUserMailReceptionService.getMailReceiveDepartmentUsers("mail001", department);
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
                sendMoldMailByLanguage(moldRecomends, usersDepartment);
                usersDepartment.clear();
                usersDepartment.add(user);
                oldLang = user.getLangId();
            }
            else {
                usersDepartment.add(user);
            }
        }
        if (usersDepartment.size() > 0) {
            sendMoldMailByLanguage(moldRecomends, usersDepartment);
        }
    }
    
    /**
     * 言語別に金型メンテサイクル通知メールを作成
     * @param moldRecomends
     * @param users 
     */
    private void sendMoldMailByLanguage(List<TblCombinedMaintenanceRecommend> moldRecomends, List<MstUser> users) {
        if (users.size() <= 0) return;
        if (moldRecomends.size() <= 0) return;
        //言語取得
        String langId = users.get(0).getLangId();
        //メールに用いる文言取得
        List<String> dictKeys = new ArrayList<>();
        dictKeys.add("msg_daily_mail_mold_mainte_subject"); //件名:金型メンテナンスアラート
        dictKeys.add("msg_alert_email_body"); //以下の%sはメンテナンスの時期が近づいてます。
        dictKeys.add("msg_mainte_email_body");//以下の%sはメンテナンスの時期です。
        dictKeys.add("mold"); //金型
        dictKeys.add("latest_maintenance_date"); //前回メンテナンス日
        dictKeys.add("mold_name"); //金型名称
        dictKeys.add("mold_after_mainte_total_production_time_hour"); //メンテ後生産時間
        dictKeys.add("mold_after_mainte_total_shot_count"); //メンテ後ショット数
        dictKeys.add("mainte_reason_text"); //メンテ事由
        // KM-360 対応 start
        dictKeys.add("hit_condition"); //該当条件
        dictKeys.add("after_mainte_total_expiration_time"); //メンテナンス後経過時間
        dictKeys.add("mold_after_mainte_total_production_time_hour"); //メンテナンス後生産時間
        dictKeys.add("mold_after_mainte_total_shot_count"); //メンテナンス後ショット数
        // KM-360 対応 end
        
        dictKeys.add("mold_part_replace"); // KM-1358
        dictKeys.add("mold_part_repair");
        HashMap<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeys);
        //部署名取得
        int department = moldRecomends.get(0).getMstMold().getDepartment();
        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(department), langId, "mst_user.department");
        String departmentName = choice == null ? "" : choice.getChoice();
        //件名作成: タイトル YYYY/MM/DD 部署名
        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append(dictMap.get("msg_daily_mail_mold_mainte_subject"));
        sbSubject.append(" ");
        sbSubject.append(noticeDate);
        sbSubject.append(" ");
        sbSubject.append(departmentName);
        logger.log(logLevel, sbSubject.toString());
        //本文作成
        StringBuilder sbBody = new StringBuilder();
        //アラート対象金型
        int cnt = 0;
        for (TblCombinedMaintenanceRecommend alertRecomend : moldRecomends) {
            if (alertRecomend.getAlertMainteType() == 1) {
                cnt++;
            }
        }
        if (cnt > 0) {
            String msg = dictMap.get("msg_alert_email_body").replace("%s", dictMap.get("mold"));
            sbBody.append(msg); //以下の金型はメンテナンスの時期が近づいています。
            sbBody.append(RETURN_CODE);
            sbBody.append(RETURN_CODE);
            //対象金型一覧のヘダー
            sbBody.append(makeMoldListHeader(dictMap));
            sbBody.append(RETURN_CODE);
            //対象金型一覧
            for (TblCombinedMaintenanceRecommend alertRecomend : moldRecomends) {
                if (alertRecomend.getAlertMainteType() == 1) {
                    sbBody.append(makeMoldListLine(alertRecomend, dictMap));
//                    // KM-360 対応 start
//                    sbBody.append(COLUMN_SEPARATOR);
//                    sbBody.append(getHitCondition(alertRecomend.getHitCondition(), dictMap));
//                    // KM-360 対応 end
                    sbBody.append(RETURN_CODE);
                }
            }
            sbBody.append(RETURN_CODE);
        }
       
        //メンテナンス対象金型
        cnt = 0;
        for (TblCombinedMaintenanceRecommend mainteRecomend : moldRecomends) {
            if (mainteRecomend.getAlertMainteType() == 2) {
                cnt++;
            }
        }
        if (cnt > 0) {
            String msg = dictMap.get("msg_mainte_email_body").replace("%s", dictMap.get("mold"));
            sbBody.append(msg); //以下の金型はメンテナンスの時期です。
            sbBody.append(RETURN_CODE);
            sbBody.append(RETURN_CODE);
            //対象金型一覧のヘダー
            sbBody.append(makeMoldListHeader(dictMap));
            sbBody.append(RETURN_CODE);
            //対象金型一覧
            for (TblCombinedMaintenanceRecommend mainteRecomend : moldRecomends) {
                if (mainteRecomend.getAlertMainteType() == 2) {
                    sbBody.append(makeMoldListLine(mainteRecomend, dictMap));
                    // KM-360 対応 start
//                    sbBody.append(COLUMN_SEPARATOR);
//                    sbBody.append(getHitCondition(mainteRecomend.getHitCondition(), dictMap));
                    // KM-360 対応 end
                    sbBody.append(RETURN_CODE);
                }
            }
            sbBody.append(RETURN_CODE);
        }
        logger.log(logLevel, sbBody.toString());
        
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
            logger.log(Level.SEVERE, "Failed to send mail to notice mold maintenance alert.");
            e.printStackTrace();
        }
    }
    
    private String makeMoldListHeader(HashMap<String, String> dictMap) {
        StringBuilder sbBody = new StringBuilder();
        sbBody.append(dictMap.get("mold_name"));
        sbBody.append(COLUMN_SEPARATOR);
        sbBody.append(dictMap.get("latest_maintenance_date"));
        sbBody.append(COLUMN_SEPARATOR);
        sbBody.append(dictMap.get("mold_after_mainte_total_production_time_hour"));
        sbBody.append(COLUMN_SEPARATOR);
        sbBody.append(dictMap.get("mold_after_mainte_total_shot_count"));
        sbBody.append(COLUMN_SEPARATOR);
        sbBody.append(dictMap.get("mainte_reason_text"));
        // KM-360 対応 start
        sbBody.append(COLUMN_SEPARATOR);
        sbBody.append(dictMap.get("hit_condition"));
        // KM-360 対応 end
        return sbBody.toString();
    }
    
    private String makeMoldListLine(TblCombinedMaintenanceRecommend moldRecomend, HashMap<String, String> dictMap) {
        StringBuilder sbBody = new StringBuilder();
        sbBody.append(moldRecomend.getMstMold().getMoldName());
        sbBody.append(COLUMN_SEPARATOR);
        // If mold 
        if (moldRecomend.getReplaceOrRepair() == 0) {
            if (moldRecomend.getMstMold().getLastMainteDate() != null) {
                sbBody.append(DateFormat.dateToStr(moldRecomend.getMstMold().getLastMainteDate(), DateFormat.DATE_FORMAT));
            }
            sbBody.append(COLUMN_SEPARATOR);
            if (moldRecomend.getMstMold().getAfterMainteTotalProducingTimeHour() != null) {
                sbBody.append(moldRecomend.getMstMold().getAfterMainteTotalProducingTimeHour());
            }
            sbBody.append(COLUMN_SEPARATOR);
            if (moldRecomend.getMstMold().getAfterMainteTotalShotCount() != null) {
                sbBody.append(moldRecomend.getMstMold().getAfterMainteTotalShotCount());
            }
            sbBody.append(COLUMN_SEPARATOR);
            if (moldRecomend.getTblMaintenanceCyclePtn() != null 
                && moldRecomend.getTblMaintenanceCyclePtn().getMainteReasonText() != null ) { // replaceOrRepair = 0 => mold maintenance recommend
                sbBody.append(moldRecomend.getTblMaintenanceCyclePtn().getMainteReasonText());
            }
            // KM-360 対応 start
            sbBody.append(COLUMN_SEPARATOR);
            sbBody.append(getHitCondition(moldRecomend.getHitCondition(), dictMap));
            // KM-360 対応 end
        } else {
            if (moldRecomend.getReplaceOrRepair() == 1) { //Replace
                if (moldRecomend.getMstMoldPartRel().getLastRplDatetime() != null) {
                    sbBody.append(DateFormat.dateToStr(moldRecomend.getMstMoldPartRel().getLastRplDatetime(), DateFormat.DATE_FORMAT));
                } else {
                    sbBody.append("");
                }
                sbBody.append(COLUMN_SEPARATOR);
                sbBody.append(moldRecomend.getMstMoldPartRel().getAftRplProdTimeHour());
                sbBody.append(COLUMN_SEPARATOR);
                sbBody.append(moldRecomend.getMstMoldPartRel().getAftRplShotCnt());
                sbBody.append(COLUMN_SEPARATOR);
                sbBody.append(dictMap.get("mold_part_replace"));
            } else {  //Repair
                if (moldRecomend.getMstMoldPartRel().getLastRprDatetime() != null) {
                    sbBody.append(DateFormat.dateToStr(moldRecomend.getMstMoldPartRel().getLastRprDatetime(), DateFormat.DATE_FORMAT));
                } else {
                    sbBody.append("");
                }
                sbBody.append(COLUMN_SEPARATOR);
                sbBody.append(moldRecomend.getMstMoldPartRel().getAftRprProdTimeHour());
                sbBody.append(COLUMN_SEPARATOR);
                sbBody.append(moldRecomend.getMstMoldPartRel().getAftRprShotCnt());
                sbBody.append(COLUMN_SEPARATOR);
                sbBody.append(dictMap.get("mold_part_repair"));
            }
            sbBody.append(COLUMN_SEPARATOR);
            String hitCondition = moldRecomend.getMstMoldPartRel().getMstMoldPart().getMoldPartCode() 
                        + "(" 
                        + moldRecomend.getMstMoldPartRel().getLocation()
                        + ")";
            sbBody.append(hitCondition);
        }
        return sbBody.toString();
    }
    
    /**
     * 金型メンテナンス候補取得
     * @return 
     */
    private List<TblMoldMaintenanceRecomend> getMoldMaintenanceRecomend() {
        Query query = entityManager.createQuery(QUERY_GET_MOLD_MAINTE_RECOMEND);
        return query.getResultList();
    }
    
    /**
     * 金型部品メンテナンス候補の取得
     * @return 
     */
    private List<TblMoldPartMaintenanceRecomendDetail> getMoldPartMaintenanceRecomend() {
        Query query = entityManager.createQuery(QUERY_GET_MOLD_PART_MAINTE_RECOMEND);
        return query.getResultList();
    }

    /**
     * 設備通知処理
     */
    private void processMachine() {
        //設備メンテナンス候補テーブルを取得
        //所属、アラート・メンテ区分別にソートされている
        List<TblMachineMaintenanceRecomend> macRecomends = getMacMaintenanceRecomend();
        if (macRecomends.size() <= 0) {
            return;
        }
        int oldDepartment = macRecomends.get(0).getMstMachine().getDepartment(); //所属ブレーク判定用
        List<TblMachineMaintenanceRecomend> macRecomendsDepartment = new ArrayList<>();
        //金型メンテナンス候補レコードループ
        for (TblMachineMaintenanceRecomend macRecomend : macRecomends) {
            logger.log(logLevel, macRecomend.getMstMachine().getMachineName());
            //所属が変わったらブレーク
            if (macRecomend.getMstMachine().getDepartment() != oldDepartment) {
                //メール送信処理へ
                sendMacMailByDepartment(macRecomendsDepartment);
                macRecomendsDepartment.clear();
                macRecomendsDepartment.add(macRecomend);
                oldDepartment = macRecomend.getMstMachine().getDepartment();
            }
            else {
                //所属ごとのリストに加える
                macRecomendsDepartment.add(macRecomend);
            }
        }
        if (macRecomendsDepartment.size() > 0) {
            sendMacMailByDepartment(macRecomendsDepartment);
        }
    }

    /**
     * 設備メンテナンス候補取得
     * @return 
     */
    private List<TblMachineMaintenanceRecomend> getMacMaintenanceRecomend() {
        Query query = entityManager.createQuery(QUERY_GET_MAC_MAINTE_RECOMEND);
        return query.getResultList();
    }

    /**
     * 部署ごとのメール送信処理(金型)
     * @param macRecomends 
     */
    private void sendMacMailByDepartment(List<TblMachineMaintenanceRecomend> macRecomends) {
        if (macRecomends.size() <= 0) return;
        //対象の所属を取得
        int department = macRecomends.get(0).getMstMachine().getDepartment();
        //宛先取得
        //その所属で送信すべきユーザーのリストを取得
        /*
        Query query = entityManager.createQuery(QUERY_GET_MAIL_RECEIVE_USER);
        query.setParameter("eventUuid", "mail001"); //メンテナンスサイクル通知の通知イベントID
        query.setParameter("eventUuid2", "mail001"); //メンテナンスサイクル通知の通知イベントID
        query.setParameter("department", department); //所属Seq
        query.setParameter("department2", String.valueOf(department)); //所属Seq
        List<MstUser> users = query.getResultList();
*/
        List<MstUser> users = mstUserMailReceptionService.getMailReceiveDepartmentUsers("mail001", department);
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
                sendMacMailByLanguage(macRecomends, usersDepartment);
                usersDepartment.clear();
                usersDepartment.add(user);
                oldLang = user.getLangId();
            }
            else {
                usersDepartment.add(user);
            }
        }
        if (usersDepartment.size() > 0) {
            sendMacMailByLanguage(macRecomends, usersDepartment);
        }
    }
    
    /**
     * 言語別に設備メンテサイクル通知メールを作成
     * @param macRecomends
     * @param users 
     */
    private void sendMacMailByLanguage(List<TblMachineMaintenanceRecomend> macRecomends, List<MstUser> users) {
        if (users.size() <= 0) return;
        if (macRecomends.size() <= 0) return;
        //言語取得
        String langId = users.get(0).getLangId();
        //メールに用いる文言取得
        List<String> dictKeys = new ArrayList<>();
        dictKeys.add("msg_daily_mail_machine_mainte_subject"); //件名:設備メンテナンスアラート
        dictKeys.add("msg_alert_email_body"); //以下の%sはメンテナンスの時期が近づいてます。
        dictKeys.add("msg_mainte_email_body");//以下の%sはメンテナンスの時期です。
        dictKeys.add("machine"); //設備
        dictKeys.add("latest_maintenance_date"); //前回メンテナンス日
        dictKeys.add("machine_name"); //設備名称
        dictKeys.add("machine_after_mainte_total_production_time_hour"); //メンテ後生産時間
        dictKeys.add("machine_after_mainte_total_shot_count"); //メンテ後ショット数
        dictKeys.add("mainte_reason_text"); //メンテ事由
        // KM-360 対応 start
        dictKeys.add("hit_condition"); //該当条件
        dictKeys.add("after_mainte_total_expiration_time"); //メンテナンス後経過時間
        dictKeys.add("mold_after_mainte_total_production_time_hour"); //メンテナンス後生産時間
        dictKeys.add("mold_after_mainte_total_shot_count"); //メンテナンス後ショット数
        // KM-360 対応 end
        HashMap<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeys);
        //部署名取得
        int department = macRecomends.get(0).getMstMachine().getDepartment();
        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(department), langId, "mst_user.department");
        String departmentName = choice == null ? "" : choice.getChoice();
        //件名作成: タイトル YYYY/MM/DD 部署名
        StringBuilder sbSubject = new StringBuilder();
        sbSubject.append(dictMap.get("msg_daily_mail_machine_mainte_subject"));
        sbSubject.append(" ");
        sbSubject.append(noticeDate);
        sbSubject.append(" ");
        sbSubject.append(departmentName);
        logger.log(logLevel, sbSubject.toString());
        //本文作成
        StringBuilder sbBody = new StringBuilder();
        //アラート対象設備
        int cnt = 0;
        for (TblMachineMaintenanceRecomend alertRecomend : macRecomends) {
            if (alertRecomend.getAlertMainteType() == 1) {
                cnt++;
            }
        }
        if (cnt > 0) {
            String msg = dictMap.get("msg_alert_email_body").replace("%s", dictMap.get("machine"));
            sbBody.append(msg); //以下の設備はメンテナンスの時期が近づいています。
            sbBody.append(RETURN_CODE);
            sbBody.append(RETURN_CODE);
            //対象設備一覧のヘダー
            sbBody.append(makeMacListHeader(dictMap));
            sbBody.append(RETURN_CODE);
            //対象設備一覧
            for (TblMachineMaintenanceRecomend alertRecomend : macRecomends) {
                if (alertRecomend.getAlertMainteType() == 1) {
                    sbBody.append(makeMacListLine(alertRecomend));
                    // KM-360 対応 start
                    sbBody.append(COLUMN_SEPARATOR);
                    sbBody.append(getHitCondition(alertRecomend.getHitCondition(), dictMap));
                    // KM-360 対応 end
                    sbBody.append(RETURN_CODE);
                }
            }
            sbBody.append(RETURN_CODE);
        }
       
        //メンテナンス対象設備
        cnt = 0;
        for (TblMachineMaintenanceRecomend mainteRecomend : macRecomends) {
            if (mainteRecomend.getAlertMainteType() == 2) {
                cnt++;
            }
        }
        if (cnt > 0) {
            String msg = dictMap.get("msg_mainte_email_body").replace("%s", dictMap.get("machine"));
            sbBody.append(msg); //以下の設備はメンテナンスの時期です。
            sbBody.append(RETURN_CODE);
            sbBody.append(RETURN_CODE);
            //対象設備一覧のヘダー
            sbBody.append(makeMacListHeader(dictMap));
            sbBody.append(RETURN_CODE);
            //対象設備一覧
            for (TblMachineMaintenanceRecomend mainteRecomend : macRecomends) {
                if (mainteRecomend.getAlertMainteType() == 2) {
                    sbBody.append(makeMacListLine(mainteRecomend));
                    // KM-360 対応 start
                    sbBody.append(COLUMN_SEPARATOR);
                    sbBody.append(getHitCondition(mainteRecomend.getHitCondition(), dictMap));
                    // KM-360 対応 end
                    sbBody.append(RETURN_CODE);
                }
            }
            sbBody.append(RETURN_CODE);
        }
        logger.log(logLevel, sbBody.toString());
        
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
            logger.log(Level.SEVERE, "Failed to send mail to notice mold maintenance alert.");
            e.printStackTrace();
        }
    }
    
    private String makeMacListHeader(HashMap<String, String> dictMap) {
        StringBuilder sbBody = new StringBuilder();
        sbBody.append(dictMap.get("machine_name"));
        sbBody.append(COLUMN_SEPARATOR);
        sbBody.append(dictMap.get("latest_maintenance_date"));
        sbBody.append(COLUMN_SEPARATOR);
        sbBody.append(dictMap.get("machine_after_mainte_total_production_time_hour"));
        sbBody.append(COLUMN_SEPARATOR);
        sbBody.append(dictMap.get("machine_after_mainte_total_shot_count"));
        sbBody.append(COLUMN_SEPARATOR);
        sbBody.append(dictMap.get("mainte_reason_text"));
        // KM-360 対応 start
        sbBody.append(COLUMN_SEPARATOR);
        sbBody.append(dictMap.get("hit_condition"));
        // KM-360 対応 end
        return sbBody.toString();
    }
    
    private String makeMacListLine(TblMachineMaintenanceRecomend macRecomend) {
        StringBuilder sbBody = new StringBuilder();
        sbBody.append(macRecomend.getMstMachine().getMachineName());
        sbBody.append(COLUMN_SEPARATOR);
        if (macRecomend.getMstMachine().getLastMainteDate() != null) {
            sbBody.append(DateFormat.dateToStr(macRecomend.getMstMachine().getLastMainteDate(), DateFormat.DATE_FORMAT));
        }
        sbBody.append(COLUMN_SEPARATOR);
        if (macRecomend.getMstMachine().getAfterMainteTotalProducingTimeHour() != null) {
            sbBody.append(macRecomend.getMstMachine().getAfterMainteTotalProducingTimeHour());
        }
        sbBody.append(COLUMN_SEPARATOR);
        if (macRecomend.getMstMachine().getAfterMainteTotalShotCount() != null) {
            sbBody.append(macRecomend.getMstMachine().getAfterMainteTotalShotCount());
        }
        sbBody.append(COLUMN_SEPARATOR);
        if (macRecomend.getTblMaintenanceCyclePtn().getMainteReasonText() != null) {
            sbBody.append(macRecomend.getTblMaintenanceCyclePtn().getMainteReasonText());
        }
        return sbBody.toString();
    }
    
    private String getHitCondition(int hitCondition, HashMap<String, String> dictMap) {

        String rs = "";

        switch (hitCondition) {
        case 1:
            rs = dictMap.get("after_mainte_total_expiration_time");
            break;
        case 2:
            rs = dictMap.get("mold_after_mainte_total_production_time_hour");
            break;
        case 3:
            rs = dictMap.get("mold_after_mainte_total_shot_count");
            break;
        default:
            break;
        }

        return rs;
    }
    
    private List<TblCombinedMaintenanceRecommend> generateCombinedMaintRecommends(List<TblMoldMaintenanceRecomend> moldMaintRecommends) {
        List<TblCombinedMaintenanceRecommend> combinedMaintRecommends = new ArrayList<>();
        for (int i = 0; i < moldMaintRecommends.size(); i++) {
            TblMoldMaintenanceRecomend moldMaintRecommend = (TblMoldMaintenanceRecomend) moldMaintRecommends.get(i);
            
            TblCombinedMaintenanceRecommend combinedMoldPartRecommed = new TblCombinedMaintenanceRecommend();
            combinedMoldPartRecommed.setId(moldMaintRecommend.getId());
            combinedMoldPartRecommed.setMoldUuid(moldMaintRecommend.getMoldUuid());
            combinedMoldPartRecommed.setAlertMainteType(moldMaintRecommend.getAlertMainteType());
            combinedMoldPartRecommed.setReplaceOrRepair(0); // NOTE: Set the value 0 for mold maintenance
            combinedMoldPartRecommed.setHitCondition(moldMaintRecommend.getHitCondition());
            combinedMoldPartRecommed.setMaintainedFlag(moldMaintRecommend.getMaintainedFlag());

            combinedMoldPartRecommed.setCreateDate(moldMaintRecommend.getCreateDate());
            combinedMoldPartRecommed.setUpdateDate(moldMaintRecommend.getUpdateDate());
            combinedMoldPartRecommed.setCreateUserUuid(moldMaintRecommend.getCreateUserUuid());
            combinedMoldPartRecommed.setUpdateUserUuid(moldMaintRecommend.getUpdateUserUuid());

            combinedMoldPartRecommed.setMstMold(moldMaintRecommend.getMstMold());
            combinedMoldPartRecommed.setTblMaintenanceCyclePtn(moldMaintRecommend.getTblMaintenanceCyclePtn());
            
            combinedMaintRecommends.add(combinedMoldPartRecommed);
        }
        return combinedMaintRecommends;
    }
    
    private List<TblCombinedMaintenanceRecommend> combineMaintRecommendToMoldPartRecommend(
            List<TblCombinedMaintenanceRecommend> combinedMaintRecommend,
            List<TblMoldMaintenanceRecomend> moldRecomends,
            List<TblMoldPartMaintenanceRecomendDetail> moldPartMaintRecommends
    ) {
        FileUtil fu = new FileUtil();
        // 1. Get all mold uuid that recommened
        // 2. Get all mold part recommend that aren't belong to mold in the list [*]
        // 3. Group mold part recommend by mold uuid and filter by condition [**] 
        List<String> moldUUIDs = moldRecomends.stream()
                .map(moldRecommend -> moldRecommend.getMoldUuid()).collect(Collectors.toList());
        List<TblMoldPartMaintenanceRecomendDetail> nonMoldRecommendeds = moldPartMaintRecommends.stream()
                .filter(moldPartRed -> {
                   return moldUUIDs.indexOf(moldPartRed.getMoldUuid()) == -1; 
                }).collect(Collectors.toList());
        Map<String, List<TblMoldPartMaintenanceRecomendDetail>> groupedMPMaintRecommendByMoldId = 
                    nonMoldRecommendeds.stream()
                            .collect(Collectors.groupingBy(m -> m.getMoldUuid()));
        Comparator<TblMoldPartMaintenanceRecomendDetail> compare = (TblMoldPartMaintenanceRecomendDetail recommend1, TblMoldPartMaintenanceRecomendDetail recommend2) -> {
            int result = recommend1.getReplaceOrRepair().compareTo(recommend2.getReplaceOrRepair());
            if (result == 0 && null != recommend1.getCreateDate() && null != recommend2.getCreateDate()) {
                result = recommend1.getCreateDate().compareTo(recommend2.getCreateDate());
            }
            if (result == 0) {
                result = recommend1.getMstMoldPartRel().getLocation().compareTo(recommend2.getMstMoldPartRel().getLocation());
            }
            return result;
        };
        
        groupedMPMaintRecommendByMoldId.forEach((key, value) -> {
                if (null != value && !value.isEmpty()) {
                    Collections.sort(value, compare);
                    TblMoldPartMaintenanceRecomendDetail mpMaintRecommend = value.get(0);
                    
                    if (mpMaintRecommend != null) {
                        TblCombinedMaintenanceRecommend newRecommend = new TblCombinedMaintenanceRecommend();
                        newRecommend.setMaintainedFlag(mpMaintRecommend.getMaintainedFlag());
                        newRecommend.setHitCondition(mpMaintRecommend.getHitCondition());
                        newRecommend.setReplaceOrRepair(mpMaintRecommend.getReplaceOrRepair());

                        newRecommend.setMoldUuid(mpMaintRecommend.getMoldUuid());                        
                        mpMaintRecommend.getMstMold().setLastMainteDateStr(fu.getDateFormatForStr(mpMaintRecommend.getMstMold().getLastMainteDate()));
                        newRecommend.setMstMold(mpMaintRecommend.getMstMold());
                        newRecommend.setMoldPartRelId(mpMaintRecommend.getMoldPartRelId());
                        newRecommend.setMstMoldPartRel(mpMaintRecommend.getMstMoldPartRel());
                        newRecommend.setCreateDate(mpMaintRecommend.getCreateDate());
                        newRecommend.setCreateUserUuid(mpMaintRecommend.getCreateUserUuid());
                        newRecommend.setUpdateDate(mpMaintRecommend.getUpdateDate());
                        newRecommend.setUpdateUserUuid(mpMaintRecommend.getUpdateUserUuid());
                        
                        // Set the default value is MAINTENANCE = 2
                        newRecommend.setAlertMainteType(2);
                        
                        combinedMaintRecommend.add(newRecommend);
                    }
                }
            });
        Comparator<TblCombinedMaintenanceRecommend> departmentSort = Comparator.comparing(m -> m.getMstMold().getDepartment());
        
        List<TblCombinedMaintenanceRecommend> finalCombineMaintRecommends = combinedMaintRecommend.stream().sorted(departmentSort).collect(Collectors.toList());
        return finalCombineMaintRecommends;
    }
}
