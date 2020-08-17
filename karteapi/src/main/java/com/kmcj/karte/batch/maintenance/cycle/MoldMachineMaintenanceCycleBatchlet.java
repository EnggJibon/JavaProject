/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.maintenance.cycle;

/**
 *
 * @author zds
 */
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.machine.MstMachineVo;
import com.kmcj.karte.resources.machine.maintenance.recomend.TblMachineMaintenanceRecomend;
import com.kmcj.karte.resources.machine.maintenance.recomend.TblMachineMaintenanceRecomendService;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtnService;
import com.kmcj.karte.resources.mold.MstMoldDetail;
import com.kmcj.karte.resources.mold.maintenance.recomend.TblMoldMaintenanceRecomend;
import com.kmcj.karte.resources.mold.maintenance.recomend.TblMoldMaintenanceRecomendService;
import com.kmcj.karte.resources.mold.part.MstMoldPartVo;
import com.kmcj.karte.resources.mold.part.maintenance.recommend.TblMoldPartMaintenanceRecomendDetail;
import com.kmcj.karte.resources.mold.part.maintenance.recommend.TblMoldPartMaintenanceRecommendService;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Named
@Dependent
public class MoldMachineMaintenanceCycleBatchlet extends AbstractBatchlet {
// ジョブ名称を取得するためにJobContextをインジェクション

    @Inject
    JobContext jobContext;

    @Inject
    private TblMaintenanceCyclePtnService tblMaintenanceCyclePtnService;

    @Inject
    private TblMoldMaintenanceRecomendService tblMoldMaintenanceRecomendService;
    
    @Inject
    private TblMoldPartMaintenanceRecommendService tblMoldPartMaintenanceRecommendService;

    @Inject
    private TblMachineMaintenanceRecomendService tblMachineMaintenanceRecomendService;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    private Logger logger = Logger.getLogger(MoldMachineMaintenanceCycleBatchlet.class.getName());
    private final static String BATCH_NAME = "moldMachineMaintenanceCycleBatch";
    private final static Level LOG_LEVEL = Level.FINE;
//    private final static String COLON = ":";
//    private final static String WRAP = "<br>";

    @Override
    @Transactional
    public String process() throws Exception {

        logger.log(Level.INFO, "MoldMachineMaintenanceCycleBatch: Started.");
        
        logger.log(LOG_LEVEL, "insertMoldPartMaintenaceRecomend: 金型部品メンテナンス候補表に登録 Started.");
        List<MstMoldPartVo> mstMoldPartDetailList = insertMoldPartMaintenaceRecomend();
        logger.log(LOG_LEVEL, "insertMoldMaintenaceRecomend: 金型部品メンテナンス候補表に登録 Ended.");
        
        logger.log(LOG_LEVEL, "insertMoldMaintenaceRecomend: 金型メンテナンス候補テーブルに登録 Started.");
        List<MstMoldDetail> mstMoldDetailList = insertMoldMaintenaceRecomend();
        logger.log(LOG_LEVEL, "insertMoldMaintenaceRecomend: 金型メンテナンス候補テーブルに登録 Ended.");

        logger.log(LOG_LEVEL, "insertMachineMaintenaceRecomend: 設備メンテナンス候補テーブルに登録 Started.");
        List<MstMachineVo> mstMachineVoList = insertMachineMaintenaceRecomend();
        logger.log(LOG_LEVEL, "insertMachineMaintenaceRecomend: 設備メンテナンス候補テーブルに登録 Ended.");

//        logger.log(LOG_LEVEL, "MoldMachineMaintenanceCycleBatch: メール送信処理 Started.");
//        List<Map<String, String>> noticeList = new ArrayList();
//        noticeList.addAll(getMoldNoticeList(mstMoldDetailList));
//        noticeList.addAll(getMachineNoticeList(mstMachineVoList));
//
//        //メール送信はデイリーレポート形式に変更。MainteCycleAlertBatchlet
//        //sendNoticeMail(noticeList);
//        logger.log(LOG_LEVEL, "MoldMachineMaintenanceCycleBatch: メール送信処理 Ended.");

        logger.log(Level.INFO, "MoldMachineMaintenanceCycleBatch: Ended.");

        return "SUCCESS";
    }

    /**
     * 金型メンテナンス候補テーブルに登録
     *
     */
    @Transactional
    private List<MstMoldDetail> insertMoldMaintenaceRecomend() {

        logger.log(LOG_LEVEL, "insertMoldMaintenaceRecomend: 金型情報抽出処理　Started.");

        // 金型情報を取得する
        TypedQuery<MstMoldForBatch> query = entityManager.createQuery("SELECT m FROM MstMoldForBatch m WHERE m.mainteStatus = 0 AND NOT EXISTS "
                + "(SELECT f FROM TblMoldMaintenanceRecomend f WHERE m.uuid = f.moldUuid AND f.alertMainteType = 2 AND f.maintainedFlag = 0) "
                + "ORDER BY m.uuid", MstMoldForBatch.class);

        List<MstMoldForBatch> moldList = query.getResultList();

        logger.log(LOG_LEVEL, "insertMoldMaintenaceRecomend: 金型情報抽出処理　Ended.");

        if (!moldList.isEmpty()) {
            List<MstMoldDetail> details = BeanCopyUtil.copyFieldsInList(moldList, MstMoldDetail.class, (mold, detail)->{
                detail.setMoldUuid(mold.getUuid());
            });
            
            if (details.size() > 0) {

                logger.log(LOG_LEVEL, "insertMoldMaintenaceRecomend.chkMoldMainte: Started.");
                // 金型メンテナンス/アラート判定
                tblMaintenanceCyclePtnService.chkMoldMainte(details);
                logger.log(LOG_LEVEL, "insertMoldMaintenaceRecomend.chkMoldMainte: Ended.");
            
                logger.log(LOG_LEVEL, "insertMoldMaintenaceRecomend.batchInsert: Started.");
                // 金型メンテナンス候補テーブルに登録
                tblMoldMaintenanceRecomendService.batchInsert(getMoldMaintenanceRecomendList(details));
                logger.log(LOG_LEVEL, "insertMoldMaintenaceRecomend.batchInsert: Ended.");
            }
            return details;
        }
        
        logger.log(LOG_LEVEL, "insertMoldMaintenaceRecomend: 金型情報抽出処理　Ended.");

        return new ArrayList<>();

    }

    /**
     * 金型メンテナンス候補テーブル登録用のリストを作成
     *
     * @param moldVoList
     *
     */
    private List<TblMoldMaintenanceRecomend> getMoldMaintenanceRecomendList(List<MstMoldDetail> moldVoList) {

        List<TblMoldMaintenanceRecomend> moldMaintenanceRecomendLis = new ArrayList();

        for (MstMoldDetail mstMoldDetail : moldVoList) {

            if (1 == mstMoldDetail.getSendFlg() || 2 == mstMoldDetail.getSendFlg()) {

                TblMoldMaintenanceRecomend tblMoldMaintenanceRecomend = new TblMoldMaintenanceRecomend();
                tblMoldMaintenanceRecomend.setId(IDGenerator.generate());
                tblMoldMaintenanceRecomend.setMoldUuid(mstMoldDetail.getMoldUuid());
                tblMoldMaintenanceRecomend.setMainteCycleId(mstMoldDetail.getActualMainteCycleId());
                tblMoldMaintenanceRecomend.setMaintainedFlag(0);
                tblMoldMaintenanceRecomend.setAlertMainteType(mstMoldDetail.getSendFlg());
                // KM-360 対応 start
                tblMoldMaintenanceRecomend.setHitCondition(mstMoldDetail.getHitCondition());
                // KM-360 対応 end
                Date nowDate = new Date();
                tblMoldMaintenanceRecomend.setCreateDate(nowDate);
                tblMoldMaintenanceRecomend.setUpdateDate(nowDate);
                tblMoldMaintenanceRecomend.setCreateUserUuid(BATCH_NAME);
                tblMoldMaintenanceRecomend.setUpdateUserUuid(BATCH_NAME);

                moldMaintenanceRecomendLis.add(tblMoldMaintenanceRecomend);

            }

        }

        return moldMaintenanceRecomendLis;

    }

    /**
     * 設備メンテナンス候補テーブルに登録
     *
     */
    @Transactional
    private List<MstMachineVo> insertMachineMaintenaceRecomend() {

        logger.log(LOG_LEVEL, "insertMachineMaintenaceRecomend: 設備情報抽出処理　Started.");

        // 金型情報を取得する
        TypedQuery<MstMachineForBatch> query = entityManager.createQuery("SELECT m FROM MstMachineForBatch m WHERE m.mainteStatus = 0 AND NOT EXISTS "
                + "(SELECT f FROM TblMachineMaintenanceRecomend f WHERE m.uuid = f.machineUuid AND f.alertMainteType = 2 AND f.maintainedFlag = 0) "
                + "ORDER BY m.uuid", MstMachineForBatch.class);

        List<MstMachineForBatch> machineList = query.getResultList();

        logger.log(LOG_LEVEL, "insertMachineMaintenaceRecomend: 設備情報抽出処理　Ended.");

        if (!machineList.isEmpty()) {
            List<MstMachineVo> voList = BeanCopyUtil.copyFieldsInList(machineList, MstMachineVo.class, (mac, macVo)->{
                macVo.setMachineUuid(mac.getUuid());
            });

            if (voList.size() > 0) {

                logger.log(LOG_LEVEL, "insertMachineMaintenaceRecomend.chkMachineMainte: Started.");
                // 設備メンテナンス/アラート判定
                tblMaintenanceCyclePtnService.chkMachineMainte(voList);
                logger.log(LOG_LEVEL, "insertMachineMaintenaceRecomend.chkMachineMainte: Ended.");

                logger.log(LOG_LEVEL, "insertMachineMaintenaceRecomend.batchInsert: Started.");
                // 設備メンテナンス候補テーブルに登録
                tblMachineMaintenanceRecomendService.batchInsert(getMachineMaintenanceRecomendList(voList));
                logger.log(LOG_LEVEL, "insertMachineMaintenaceRecomend.batchInsert: Ended.");

            }
            return voList;
        }
        
        logger.log(LOG_LEVEL, "insertMachineMaintenaceRecomend: 設備情報抽出処理　Ended.");

        return new ArrayList<>();
    }

    /**
     * 設備メンテナンス候補テーブル登録用のリストを作成
     *
     * @param machineVoList
     *
     */
    private List<TblMachineMaintenanceRecomend> getMachineMaintenanceRecomendList(List<MstMachineVo> machineVoList) {

        List<TblMachineMaintenanceRecomend> machineMaintenanceRecomendLis = new ArrayList();

        for (MstMachineVo mstMachineVo : machineVoList) {

            if (1 == mstMachineVo.getSendFlg() || 2 == mstMachineVo.getSendFlg()) {

                TblMachineMaintenanceRecomend tblMachineMaintenanceRecomend = new TblMachineMaintenanceRecomend();
                tblMachineMaintenanceRecomend.setId(IDGenerator.generate());
                tblMachineMaintenanceRecomend.setMachineUuid(mstMachineVo.getMachineUuid());
                tblMachineMaintenanceRecomend.setMainteCycleId(mstMachineVo.getActualMainteCycleId());
                tblMachineMaintenanceRecomend.setMaintainedFlag(0);
                tblMachineMaintenanceRecomend.setAlertMainteType(mstMachineVo.getSendFlg());
                // KM-360 対応 start
                tblMachineMaintenanceRecomend.setHitCondition(mstMachineVo.getHitCondition());
                // KM-360 対応 end
                Date nowDate = new Date();
                tblMachineMaintenanceRecomend.setCreateDate(nowDate);
                tblMachineMaintenanceRecomend.setUpdateDate(nowDate);
                tblMachineMaintenanceRecomend.setCreateUserUuid(BATCH_NAME);
                tblMachineMaintenanceRecomend.setUpdateUserUuid(BATCH_NAME);

                machineMaintenanceRecomendLis.add(tblMachineMaintenanceRecomend);

            }

        }

        return machineMaintenanceRecomendLis;

    }
    
    /**
     * insertMoldPartMaintenaceRecomend
     *
     */
    @Transactional
    private List<MstMoldPartVo> insertMoldPartMaintenaceRecomend() {

        logger.log(Level.INFO, "insertMoldPartMaintenaceRecomend: Mold part information extraction processing Started.");

        // 金型部品情報を入手する
        TypedQuery<MstMoldPartForBatch> query = entityManager.createQuery("SELECT m FROM MstMoldPartForBatch m WHERE (m.rplClShotCnt != 0 OR "
                +"m.rplClProdTimeHour != 0 OR m.rplClLappsedDay != 0 OR m.rprClShotCnt != 0 OR m.rprClProdTimeHour != 0 OR m.rprClLappsedDay != 0) "
                + "AND NOT EXISTS (SELECT f FROM TblMoldPartMaintenanceRecomendDetail f WHERE m.id = f.moldPartRelId AND f.replaceOrRepair = 1 AND f.maintainedFlag = 0) "
                ,MstMoldPartForBatch.class);
        
        List<MstMoldPartForBatch> moldPartList = query.getResultList();
        logger.log(Level.INFO, "Get Mold Part list");

        logger.log(Level.INFO, "insertMoldPartMaintenaceRecomend: Mold part information extraction processing　Ended.");

        if (!moldPartList.isEmpty()) {
            List<MstMoldPartVo> details = BeanCopyUtil.copyFieldsInList(moldPartList, MstMoldPartVo.class, (moldPart, detail)->{
                detail.setId(moldPart.getId());
            });
            
            if (details.size() > 0) {

                logger.log(LOG_LEVEL, "insertMoldMaintenaceRecomend.chkMoldMainte: Started.");
                // Mold part maintenance / alert judgment
                tblMaintenanceCyclePtnService.chkMoldPartMainte(details);
                logger.log(LOG_LEVEL, "insertMoldMaintenaceRecomend.chkMoldMainte: Ended.");
            
                logger.log(Level.INFO, "insertMoldMaintenaceRecomend.batchInsert: Started.");
                // Registered in mold part maintenance candidate table
                List<TblMoldPartMaintenanceRecomendDetail> tblMoldPartMaintenanceRecomendDetailList = new ArrayList();
                tblMoldPartMaintenanceRecomendDetailList = getMoldPartMaintenanceRecomendList(details);
                tblMoldPartMaintenanceRecommendService.batchInsert(tblMoldPartMaintenanceRecomendDetailList);

                logger.log(Level.INFO, "insertMoldMaintenaceRecomend.batchInsert: Ended.");
            }
            return details;
        }
        
        logger.log(Level.INFO, "insertMoldMaintenaceRecomend: Mold part information extraction processing　Ended.");

        return new ArrayList<>();

    }

    /**
     * Create a list for mold part maintenance candidate table registration
     *
     * @param moldPartVoList
     *
     */
    private List<TblMoldPartMaintenanceRecomendDetail> getMoldPartMaintenanceRecomendList(List<MstMoldPartVo> moldPartVoList) {

        List<TblMoldPartMaintenanceRecomendDetail> tblMoldPartMaintenanceRecomendDetailList = new ArrayList();
        

        for (MstMoldPartVo mstMoldPart : moldPartVoList) {

                TblMoldPartMaintenanceRecomendDetail tblMoldPartMaintenanceRecommend = new TblMoldPartMaintenanceRecomendDetail();
                tblMoldPartMaintenanceRecommend.setId(IDGenerator.generate());
                tblMoldPartMaintenanceRecommend.setMoldUuid(mstMoldPart.getMoldUuid());
                tblMoldPartMaintenanceRecommend.setMoldPartRelId(mstMoldPart.getId());
                tblMoldPartMaintenanceRecommend.setMaintainedFlag(mstMoldPart.getMaintainedFlag());
                Date nowDate = new Date();
                tblMoldPartMaintenanceRecommend.setCreateDate(nowDate);
                tblMoldPartMaintenanceRecommend.setUpdateDate(nowDate);
                tblMoldPartMaintenanceRecommend.setCreateUserUuid(BATCH_NAME);
                tblMoldPartMaintenanceRecommend.setUpdateUserUuid(BATCH_NAME);
                tblMoldPartMaintenanceRecommend.setReplaceOrRepair(mstMoldPart.getReplaceOrRepair());
                tblMoldPartMaintenanceRecommend.setHitCondition(mstMoldPart.getHitCondition());

                tblMoldPartMaintenanceRecomendDetailList.add(tblMoldPartMaintenanceRecommend);

        }
        logger.log(Level.INFO, "MOLD PART INSERT DONE====================>>>>>");
        return tblMoldPartMaintenanceRecomendDetailList;

    }
    

//    /**
//     * 金型送信情報を取得
//     *
//     * @param moldDetailList
//     *
//     * @return
//     *
//     */
//    private List<Map<String, String>> getMoldNoticeList(List<MstMoldDetail> moldDetailList) {
//
//        List<Map<String, String>> noticeList = new ArrayList();
//
//        if (!moldDetailList.isEmpty()) {
//
//            for (MstMoldDetail mstMoldDetail : moldDetailList) {
//
//                Map<String, String> tempMap = new HashMap();
//
//                if (0 != mstMoldDetail.getSendFlg()) {
//
//                    // 送信分類（1：金型；2：設備）
//                    tempMap.put("sendType", "1");
//                    tempMap.put("id", mstMoldDetail.getMoldUuid());
//                    tempMap.put("name", mstMoldDetail.getMoldName());
//                    tempMap.put("department", mstMoldDetail.getDepartment());
//                    tempMap.put("lastMainteDate", DateFormat.dateToStr(mstMoldDetail.getLastMainteDate(), DateFormat.DATE_FORMAT));
//                    tempMap.put("afterMainteTotalProducingTimeHour", String.valueOf(mstMoldDetail.getAfterMainteTotalProducingTimeHour()));
//                    tempMap.put("afterMainteTotalShotCount", String.valueOf(mstMoldDetail.getAfterMainteTotalShotCount()));
//                    tempMap.put("mainteReasonText", mstMoldDetail.getMainteReasonText());
//                    tempMap.put("alertMainteType", String.valueOf(mstMoldDetail.getSendFlg()));
//
//                    noticeList.add(tempMap);
//                }
//
//            }
//
//        }
//
//        return noticeList;
//
//    }
//
//    /**
//     * 設備送信情報を取得
//     *
//     * @param moldDetailList
//     *
//     * @return
//     *
//     */
//    private List<Map<String, String>> getMachineNoticeList(List<MstMachineVo> machineVoList) {
//
//        List<Map<String, String>> noticeList = new ArrayList();
//
//        if (!machineVoList.isEmpty()) {
//
//            for (MstMachineVo mstMachineVo : machineVoList) {
//
//                Map<String, String> tempMap = new HashMap();
//
//                if (0 != mstMachineVo.getSendFlg()) {
//
//                    // 送信分類（1：金型；2：設備）
//                    tempMap.put("sendType", "2");
//                    tempMap.put("id", mstMachineVo.getMachineUuid());
//                    tempMap.put("name", mstMachineVo.getMachineName());
//                    tempMap.put("department", String.valueOf(mstMachineVo.getDepartment()));
//                    tempMap.put("lastMainteDate", DateFormat.dateToStr(mstMachineVo.getLastMainteDate(), DateFormat.DATE_FORMAT));
//                    tempMap.put("afterMainteTotalProducingTimeHour", String.valueOf(mstMachineVo.getAfterMainteTotalProducingTimeHour()));
//                    tempMap.put("afterMainteTotalShotCount", String.valueOf(mstMachineVo.getAfterMainteTotalShotCount()));
//                    tempMap.put("mainteReasonText", mstMachineVo.getMainteReasonText());
//                    // アラート・メンテ区分（1:アラート;2：メンテ）
//                    tempMap.put("alertMainteType", String.valueOf(mstMachineVo.getSendFlg()));
//
//                    noticeList.add(tempMap);
//                }
//
//            }
//
//        }
//
//        return noticeList;
//
//    }

    /**
     * 送信処理
     *
     * @param noticeList
     *
     * @return
     *
    private void sendNoticeMail(List<Map<String, String>> noticeList) {

        if (!noticeList.isEmpty()) {

            List<String> keyList = new ArrayList();

            keyList.add("msg_alert_email_subject");
            keyList.add("msg_alert_email_body");
            keyList.add("msg_mainte_email_subject");
            keyList.add("msg_mainte_email_body");
            keyList.add("latest_maintenance_date");
            keyList.add("mold");
            keyList.add("machine");
            keyList.add("mold_name");
            keyList.add("machine_name");
            keyList.add("machine_after_mainte_total_production_time_hour");
            keyList.add("mold_after_mainte_total_production_time_hour");
            keyList.add("machine_after_mainte_total_shot_count");
            keyList.add("mold_after_mainte_total_shot_count");
            keyList.add("mainte_reason_text");

            // 送信用の文言取得
            Map<String, String> dictionryMap = FileUtil.getDictionaryList(mstDictionaryService, "ja", keyList);

            for (Map<String, String> noticeMap : noticeList) {

                // メール件名設定
                String mailTile = "";

                // アラート送信
                if ("1".equals(noticeMap.get("alertMainteType"))) {

                    mailTile = dictionryMap.get("msg_alert_email_subject");

                } else if ("2".equals(noticeMap.get("alertMainteType"))) {// メンテナンス送信

                    mailTile = dictionryMap.get("msg_mainte_email_subject");

                }

                // メール本文設定
                String mailBody = getMailBody(noticeMap, dictionryMap);

                // 受信者リストを設定する
                List<String> receiverList = new ArrayList();

                // 所属存在
                if (FileUtil.getStrToNum(noticeMap.get("department")) > 0) {

                    // 金型/設備の所属に対して、担当者を検索する
                    List<MstUser> userList = mstUserService.getMstUserByDepartment(noticeMap.get("department"));

                    if (!userList.isEmpty()) {

                        for (MstUser mstUser : userList) {

                            if (StringUtils.isNotEmpty(mstUser.getMailAddress())) {

                                receiverList.add(mstUser.getMailAddress());
                            }

                        }
                    }

                    //メール送信はデイリーレポート形式に変更。MainteCycleAlertBathchletにて一日一回送信
                    //try {

                        //mailSender.sendMail(receiverList, null, mailTile, mailBody);

                    //} catch (IOException ex) {
                    //    Logger.getLogger(MoldMachineMaintenanceCycleBatchlet.class.getName()).log(Level.SEVERE, null, ex);
                    //} catch (MessagingException ex) {
                    //    Logger.getLogger(MoldMachineMaintenanceCycleBatchlet.class.getName()).log(Level.SEVERE, null, ex);
                   // }

                }

            }
        }
    }
     */

//    /**
//     * 送信の本文を取得
//     *
//     * @param noticeMap
//     * @param dictionryMap
//     *
//     * @return
//     *
//     */
//    private String getMailBody(Map<String, String> noticeMap, Map<String, String> dictionryMap) {
//
//        StringBuilder mailBody = new StringBuilder();
//
//        // アラート送信
//        if ("1".equals(noticeMap.get("alertMainteType"))) {
//
//            // 金型の場合
//            if ("1".equals(noticeMap.get("sendType"))) {
//
//                mailBody.append(String.format(dictionryMap.get("msg_alert_email_body"), dictionryMap.get("mold")));
//
//                mailBody.append(getMoldBody(noticeMap, dictionryMap));
//
//            } else if ("2".equals(noticeMap.get("sendType"))) {
//
//                mailBody.append(String.format(dictionryMap.get("msg_alert_email_body"), dictionryMap.get("machine")));
//
//                mailBody.append(getMachineBody(noticeMap, dictionryMap));
//            }
//
//        } else if ("2".equals(noticeMap.get("alertMainteType"))) {// メンテナンス送信
//
//            // 金型の場合
//            if ("1".equals(noticeMap.get("sendType"))) {
//
//                mailBody.append(String.format(dictionryMap.get("msg_mainte_email_body"), dictionryMap.get("mold")));
//
//                mailBody.append(getMoldBody(noticeMap, dictionryMap));
//
//            } else if ("2".equals(noticeMap.get("sendType"))) {
//
//                mailBody.append(String.format(dictionryMap.get("msg_mainte_email_body"), dictionryMap.get("machine")));
//
//                mailBody.append(getMachineBody(noticeMap, dictionryMap));
//            }
//
//        }
//
//        // メンテナンス事由を設定する
//        mailBody.append(WRAP);
//        mailBody.append(dictionryMap.get("mainte_reason_text"));
//        mailBody.append(COLON);
//        mailBody.append(noticeMap.get("mainteReasonText"));
//        mailBody.append(WRAP);
//
//        return mailBody.toString();
//
//    }

//    /**
//     * 金型の本文を取得
//     *
//     * @param noticeMap
//     * @param dictionryMap
//     *
//     * @return
//     *
//     */
//    private String getMoldBody(Map<String, String> noticeMap, Map<String, String> dictionryMap) {
//
//        StringBuilder moldMailBody = new StringBuilder();
//
//        moldMailBody.append(WRAP);
//        moldMailBody.append(WRAP);
//        moldMailBody.append(dictionryMap.get("mold_name"));
//        moldMailBody.append(COLON);
//        moldMailBody.append(FileUtil.getStr(noticeMap.get("name")));
//        moldMailBody.append(WRAP);
//        moldMailBody.append(dictionryMap.get("latest_maintenance_date"));
//        moldMailBody.append(COLON);
//        moldMailBody.append(FileUtil.getStr(noticeMap.get("lastMainteDate")));
//        moldMailBody.append(WRAP);
//        moldMailBody.append(dictionryMap.get("mold_after_mainte_total_production_time_hour"));
//        moldMailBody.append(COLON);
//        moldMailBody.append(noticeMap.get("afterMainteTotalProducingTimeHour"));
//        moldMailBody.append(WRAP);
//        moldMailBody.append(dictionryMap.get("mold_after_mainte_total_shot_count"));
//        moldMailBody.append(COLON);
//        moldMailBody.append(FileUtil.getStr(noticeMap.get("afterMainteTotalShotCount")));
//
//        return moldMailBody.toString();
//    }
//
//    /**
//     * 設備の本文を取得
//     *
//     * @param noticeMap
//     * @param dictionryMap
//     *
//     * @return
//     *
//     */
//    private String getMachineBody(Map<String, String> noticeMap, Map<String, String> dictionryMap) {
//
//        StringBuilder machineMailBody = new StringBuilder();
//
//        machineMailBody.append(WRAP);
//        machineMailBody.append(WRAP);
//        machineMailBody.append(dictionryMap.get("machine_name"));
//        machineMailBody.append(COLON);
//        machineMailBody.append(FileUtil.getStr(noticeMap.get("name")));
//        machineMailBody.append(WRAP);
//        machineMailBody.append(dictionryMap.get("latest_maintenance_date"));
//        machineMailBody.append(COLON);
//        machineMailBody.append(FileUtil.getStr(noticeMap.get("lastMainteDate")));
//        machineMailBody.append(WRAP);
//        machineMailBody.append(dictionryMap.get("machine_after_mainte_total_production_time_hour"));
//        machineMailBody.append(COLON);
//        machineMailBody.append(noticeMap.get("afterMainteTotalProducingTimeHour"));
//        machineMailBody.append(WRAP);
//        machineMailBody.append(dictionryMap.get("machine_after_mainte_total_shot_count"));
//        machineMailBody.append(COLON);
//        machineMailBody.append(FileUtil.getStr(noticeMap.get("afterMainteTotalShotCount")));
//
//        return machineMailBody.toString();
//    }
}
