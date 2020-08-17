/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.machineoperatingrate;

import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.machine.history.TblMachineHistory;
import com.kmcj.karte.resources.machine.history.TblMachineHistoryPK;
import com.kmcj.karte.resources.machine.history.TblMachineHistoryService;
import com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateForDay;
import com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateForDayPK;
import com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateForMonth;
import com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateForMonthPK;
import com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateForWeek;
import com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateForWeekPK;
import com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRatePeriodVo;
import com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateRecalc;
import com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateService;
import com.kmcj.karte.util.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author penggd
 */
@Named
@Dependent
public class MachineOperatingRateBatchlet extends AbstractBatchlet {

    // ジョブ名称を取得するためにJobContextをインジェクション
    @Inject
    JobContext jobContext;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private TblMachineOperatingRateService tblMachineOperatingRateService;

    @Inject
    private TblMachineHistoryService tblMachineHistoryService;

    @Inject
    private CnfSystemService cnfSystemService;
    
    
    private Logger logger = Logger.getLogger(MachineOperatingRateBatchlet.class.getName());
    private final static String BATCH_NAME = "machineOperatingRateBatchlet";
    private final static Level LOG_LEVEL = Level.FINE;

    
    @Override
    @Transactional
    public String process() throws Exception {

        logger.log(Level.INFO, "MachineOperatingRateBatchlet: Started.");
        //再集計対象がないかチェック。
        List<TblMachineOperatingRateRecalc> recalcList = checkRecalc();
        logger.log(LOG_LEVEL, "getUnAggregateMachineOperatingList: 未集計の設備稼働レコードを抽出する Started.");
        List<TblMachineHistory> tblMachineHistoryList = getUnAggregateMachineOperatingList();
        logger.log(LOG_LEVEL, "getUnAggregateMachineOperatingList: 未集計の設備稼働レコードを抽出する Ended.");

        int length = tblMachineHistoryList.size();

        int count = length / 2000;

        int lessCount = length % 2000;

        for (int i = 0; i < count; i++) {

            List<TblMachineHistory> subMachineHistoryList = tblMachineHistoryList.subList(i * 2000, (i + 1) * 2000 - 1);

            logger.log(LOG_LEVEL, "getMachineOperatingRataeList: 設備の稼動時間を集計する Started.");
            List<TblMachineOperatingRatePeriodVo> tblMachineOperatingRatePeriodVos = getMachineOperatingRataeList(
                    subMachineHistoryList);
            logger.log(LOG_LEVEL, "getMachineProductionPerList: 設備の稼動時間を集計する Ended.");

            logger.log(LOG_LEVEL, "insertMachineOperatingRatePerDay: 設備日別稼働率照会テーブルを登録する Started.");
            insertMachineOperatingRatePerDay(tblMachineOperatingRatePeriodVos);
            logger.log(LOG_LEVEL, "insertMachineOperatingRatePerDay: 設備日別稼働率照会テーブルを登録する Ended.");

            logger.log(LOG_LEVEL, "insertMachineOperatingRatePerWeek: 設備週別稼働率照会テーブルを登録する Started.");
            insertMachineOperatingRatePerWeek(tblMachineOperatingRatePeriodVos);
            logger.log(LOG_LEVEL, "insertMachineOperatingRatePerWeek: 設備週別稼働率照会テーブルを登録する Ended.");

            logger.log(LOG_LEVEL, "insertMachineProductionPerMonth: 設備月別稼働率照会テーブルを登録する Started.");
            insertMachineOperatingRatePerMonth(tblMachineOperatingRatePeriodVos);
            logger.log(LOG_LEVEL, "insertMachineProductionPerMonth: 設備月別稼働率照会テーブルを登録する Ended.");

            logger.log(LOG_LEVEL, "updMachinHistory: 設備履歴テーブルの更新処理 Started.");
            updMachinHistory(subMachineHistoryList);
            logger.log(LOG_LEVEL, "updMachinHistory: 設備履歴テーブルの更新処理 Ended.");

        }
        
        if (lessCount > 0) {

            List<TblMachineHistory> subMachineHistoryList = tblMachineHistoryList.subList(count * 2000, length);

            logger.log(LOG_LEVEL, "getMachineOperatingRataeList: 設備の稼動時間を集計する Started.");
            List<TblMachineOperatingRatePeriodVo> tblMachineOperatingRatePeriodVos = getMachineOperatingRataeList(
                    subMachineHistoryList);
            logger.log(LOG_LEVEL, "getMachineProductionPerList: 設備の稼動時間を集計する Ended.");

            logger.log(LOG_LEVEL, "insertMachineOperatingRatePerDay: 設備日別稼働率照会テーブルを登録する Started.");
            insertMachineOperatingRatePerDay(tblMachineOperatingRatePeriodVos);
            logger.log(LOG_LEVEL, "insertMachineOperatingRatePerDay: 設備日別稼働率照会テーブルを登録する Ended.");

            logger.log(LOG_LEVEL, "insertMachineOperatingRatePerWeek: 設備週別稼働率照会テーブルを登録する Started.");
            insertMachineOperatingRatePerWeek(tblMachineOperatingRatePeriodVos);
            logger.log(LOG_LEVEL, "insertMachineOperatingRatePerWeek: 設備週別稼働率照会テーブルを登録する Ended.");

            logger.log(LOG_LEVEL, "insertMachineProductionPerMonth: 設備月別稼働率照会テーブルを登録する Started.");
            insertMachineOperatingRatePerMonth(tblMachineOperatingRatePeriodVos);
            logger.log(LOG_LEVEL, "insertMachineProductionPerMonth: 設備月別稼働率照会テーブルを登録する Ended.");

            logger.log(LOG_LEVEL, "updMachinHistory: 設備履歴テーブルの更新処理 Started.");
            updMachinHistory(subMachineHistoryList);
            logger.log(LOG_LEVEL, "updMachinHistory: 設備履歴テーブルの更新処理 Ended.");

        }
        
        //再集計の指示を削除
        deleteRecalcOrders(recalcList);

        logger.log(Level.INFO, "MachineOperatingRateBatchlet: Ended.");

        return "SUCCESS";
    }

    /**
     * Σ軍師バックアップファイルの再取込により再計算が指示されているものがないかチェック。
     * あれば設備稼働履歴の集計済みフラグをリセットし、集計テーブルを削除する。
     */
    @Transactional
    private List<TblMachineOperatingRateRecalc> checkRecalc() {
        Query query = entityManager.createNamedQuery("TblMachineOperatingRateRecalc.findAll");
        List<TblMachineOperatingRateRecalc> list = (List<TblMachineOperatingRateRecalc>) query.getResultList();
        for (TblMachineOperatingRateRecalc recalc : list) {
            ResetAggregation(recalc.getMachineUuid());
        }
        return list;
    }
    
    /**
     * 再集計が指示された設備の再集計指示を削除する
     * @param recalcList 
     */
    @Transactional
    private void deleteRecalcOrders(List<TblMachineOperatingRateRecalc> recalcList) {
        if (recalcList == null) return;
        for (TblMachineOperatingRateRecalc recalc: recalcList) {
            Query query = entityManager.createQuery(" DELETE FROM TblMachineOperatingRateRecalc t WHERE t.machineUuid = :machineUuid ");
            query.setParameter("machineUuid", recalc.getMachineUuid());
            query.executeUpdate();
        }
    }
    
    /**
     * 再集計が指示された設備について集計テーブルをすべて削除し、設備稼働履歴テーブルの集計済みフラグをすべて落とす
     * @param machineUuid 
     */
    @Transactional
    private void ResetAggregation(String machineUuid) {
        //集計テーブルの削除
        Query query = entityManager.createQuery(" DELETE FROM TblMachineOperatingRateForDay t WHERE t.tblMachineOperatingRateForDayPK.machineUuid = :machineUuid ");
        query.setParameter("machineUuid", machineUuid);
        query.executeUpdate();
        query = entityManager.createQuery(" DELETE FROM TblMachineOperatingRateForWeek t WHERE t.tblMachineOperatingRateForWeekPK.machineUuid = :machineUuid ");
        query.setParameter("machineUuid", machineUuid);
        query.executeUpdate();
        query = entityManager.createQuery(" DELETE FROM TblMachineOperatingRateForMonth t WHERE t.tblMachineOperatingRateForMonthPK.machineUuid = :machineUuid ");
        query.setParameter("machineUuid", machineUuid);
        query.executeUpdate();
        //稼動履歴テーブルの集計フラグを全リセット
        query = entityManager.createQuery(" UPDATE TblMachineHistory t SET t.aggregatedFlg = 0 WHERE t.tblMachineHistoryPK.machineUuid = :machineUuid ");
        query.setParameter("machineUuid", machineUuid);
        query.executeUpdate();
    }

   /**
     * 未集計の設備稼働レコードを抽出する
     *
     */
    private List<TblMachineHistory> getUnAggregateMachineOperatingList() {

        // 未集計の機械日報を取得する(稼働中のレコードのみ抽出する)
        Query query = entityManager.createQuery(
                "SELECT t FROM TblMachineHistory t WHERE t.status = :status AND t.aggregatedFlg = 0 ORDER BY t.tblMachineHistoryPK.startDate ASC");

        query.setParameter("status", "ON");

        List<TblMachineHistory> tblMachineHistoryList = (List<TblMachineHistory>) query.getResultList();

        for (int i = 0; i < tblMachineHistoryList.size(); i++) {

            // 終了時間がNULLの場合
            if (null == tblMachineHistoryList.get(i).getEndDate()) {

                tblMachineHistoryList.get(i).setEndDate(tblMachineHistoryList.get(i).getLastEventDate());

                tblMachineHistoryList.get(i).setDeductionFlg(true);

            } else {// 終了時間が存在の場合

                tblMachineHistoryList.get(i).setDeductionFlg(false);
            }
            
        }

        return tblMachineHistoryList;

    }

    /**
     * 設備生産実績の集計する
     *
     * @param tblMachineDailyReportDetails
     *
     * @return
     *
     */
    private List<TblMachineOperatingRatePeriodVo> getMachineOperatingRataeList(
            List<TblMachineHistory> tblMachineHistoryList) {

        List<TblMachineHistory> tblMachineOperatingRateForDayList = new ArrayList();

        CnfSystem cnfSystem = cnfSystemService.findByKey("system", "business_start_time");

        String businessTime = DateFormat.formatTime(cnfSystem.getConfigValue());

        for (TblMachineHistory tblMachineHistory : tblMachineHistoryList) {

            long day = DateFormat.daysCalculate(
                    DateFormat.dateToStr(tblMachineHistory.getTblMachineHistoryPK().getStartDate(),
                            DateFormat.DATETIME_FORMAT),
                    DateFormat.dateToStr(tblMachineHistory.getEndDate(), DateFormat.DATETIME_FORMAT), businessTime);
            
            // 開始時間の年月日＝終了時間の年月日の場合
            if (0 == day) {
                
                String startDay = DateFormat.dateToStr(tblMachineHistory.getTblMachineHistoryPK().getStartDate(), DateFormat.DATE_FORMAT);
                String startDayTime = DateFormat.dateToStr(tblMachineHistory.getTblMachineHistoryPK().getStartDate(), DateFormat.DATETIME_FORMAT);
              
                String endDayTime = DateFormat.dateToStr(tblMachineHistory.getEndDate(), DateFormat.DATETIME_FORMAT);

                StringBuilder tempBusinessDateTime = new StringBuilder();

                tempBusinessDateTime.append(startDay).append(" ").append(businessTime);

                // 開始時刻　＞＝　業務開始時刻
                if (startDayTime.compareTo(tempBusinessDateTime.toString()) >= 0) {

                    tblMachineOperatingRateForDayList.add(tblMachineHistory);

                } else if (startDayTime.compareTo(tempBusinessDateTime.toString()) < 0) { // 開始時刻　＜　業務開始時刻

                    if (endDayTime.compareTo(tempBusinessDateTime.toString()) >= 0) { // 終了時刻　＞＝　業務開始時刻

                        TblMachineHistory beforeTblMachineHistory = new TblMachineHistory();

                        setMachineHistoryInfo(tblMachineHistory, beforeTblMachineHistory);

                        beforeTblMachineHistory.getTblMachineHistoryPK().setStartDate(DateFormat.getBeforeDay(tblMachineHistory.getTblMachineHistoryPK().getStartDate()));

                        beforeTblMachineHistory.setEndDate(DateFormat.getBeforeDay(DateFormat.strToDatetime(tempBusinessDateTime.toString())));

                        tblMachineOperatingRateForDayList.add(beforeTblMachineHistory);

                        TblMachineHistory tempTblMachineHistory = new TblMachineHistory();

                        setMachineHistoryInfo(tblMachineHistory, tempTblMachineHistory);

                        tempTblMachineHistory.getTblMachineHistoryPK().setStartDate(DateFormat.strToDatetime(tempBusinessDateTime.toString()));

                        tblMachineOperatingRateForDayList.add(tempTblMachineHistory);

                    } else { // 終了時刻　＜　業務開始時刻

                        TblMachineHistory tempTblMachineHistory = new TblMachineHistory();

                        setMachineHistoryInfo(tblMachineHistory, tempTblMachineHistory);

                        tempTblMachineHistory.getTblMachineHistoryPK().setStartDate(DateFormat.getBeforeDay(tblMachineHistory.getTblMachineHistoryPK().getStartDate()));

                        tempTblMachineHistory.setEndDate(DateFormat.getBeforeDay(tblMachineHistory.getEndDate()));
                        
                        tblMachineOperatingRateForDayList.add(tempTblMachineHistory);

                    }
                }

            } else if (day > 0) {// 開始時間の年月日<>終了時間の年月日の場合

                for (int i = 0; i <= day; i++) {

                    TblMachineHistory tempTblMachineHistory = new TblMachineHistory();

                    if (i == 0) {// 一日目

                        setMachineHistoryInfo(tblMachineHistory, tempTblMachineHistory);

                        String startDay = DateFormat.dateToStr(tblMachineHistory.getTblMachineHistoryPK().getStartDate(), DateFormat.DATE_FORMAT);
                        String endDay = DateFormat.dateToStr(DateFormat.getAfterDay(tblMachineHistory.getTblMachineHistoryPK().getStartDate()), DateFormat.DATE_FORMAT);
                        String startDayTime = DateFormat.dateToStr(tblMachineHistory.getTblMachineHistoryPK().getStartDate(), DateFormat.DATETIME_FORMAT);

                        StringBuilder tempBusinessStartDateTime = new StringBuilder();

                        tempBusinessStartDateTime.append(startDay).append(" ").append(businessTime);

                        StringBuilder tempEndDateTime = new StringBuilder();

                        tempEndDateTime.append(endDay).append(" ").append("00:00:00");

                        if (startDayTime.compareTo(tempBusinessStartDateTime.toString()) < 0) {

                            TblMachineHistory beforeTblMachineHistory = new TblMachineHistory();

                            setMachineHistoryInfo(tblMachineHistory, beforeTblMachineHistory);

                            beforeTblMachineHistory.getTblMachineHistoryPK().setStartDate(DateFormat.getBeforeDay(tblMachineHistory.getTblMachineHistoryPK().getStartDate()));

                            beforeTblMachineHistory.setEndDate(DateFormat.getBeforeDay(DateFormat.strToDatetime(tempBusinessStartDateTime.toString())));

                            tblMachineOperatingRateForDayList.add(beforeTblMachineHistory);

                            tempTblMachineHistory.getTblMachineHistoryPK().setStartDate(DateFormat.strToDatetime(tempBusinessStartDateTime.toString()));

                        }

                        tempTblMachineHistory.setEndDate(DateFormat.strToDatetime(tempEndDateTime.toString()));

                        tblMachineOperatingRateForDayList.add(tempTblMachineHistory);

                    } else if (i == day) {// 最終日

                        setMachineHistoryInfo(tblMachineHistory, tempTblMachineHistory);

                        String endDay = DateFormat.dateToStr(tblMachineHistory.getEndDate(), DateFormat.DATE_FORMAT);
                        String endDayTime = DateFormat.dateToStr(tblMachineHistory.getEndDate(), DateFormat.DATETIME_FORMAT);

                        StringBuilder tempBusinessStartDateTime = new StringBuilder();
                        tempBusinessStartDateTime.append(endDay).append(" ").append(businessTime);

                        StringBuilder tempEndDateTime = new StringBuilder();

                        tempEndDateTime.append(endDay).append(" ").append("00:00:00");

                        TblMachineHistory beforeTblMachineHistory = new TblMachineHistory();

                        setMachineHistoryInfo(tblMachineHistory, beforeTblMachineHistory);

                        beforeTblMachineHistory.getTblMachineHistoryPK().setStartDate(DateFormat.getBeforeDay(DateFormat.strToDatetime(tempEndDateTime.toString())));
                        
                        if (endDayTime.compareTo(tempBusinessStartDateTime.toString()) >= 0) {

                            tempTblMachineHistory.getTblMachineHistoryPK().setStartDate(DateFormat.strToDatetime(tempBusinessStartDateTime.toString()));
                            beforeTblMachineHistory.setEndDate(DateFormat.getBeforeDay(DateFormat.strToDatetime(tempBusinessStartDateTime.toString())));
                            
                            tblMachineOperatingRateForDayList.add(tempTblMachineHistory);
                            
                        }else {
                            beforeTblMachineHistory.setEndDate(DateFormat.getBeforeDay(tblMachineHistory.getEndDate()));
                        }
                        
                        tblMachineOperatingRateForDayList.add(beforeTblMachineHistory);

                    } else {

                        setMachineHistoryInfo(tblMachineHistory, tempTblMachineHistory);

                        Date startDay = tempTblMachineHistory.getTblMachineHistoryPK().getStartDate();

                        String tempStartDay = DateFormat.dateToStr(DateFormat.getAfterDays(startDay, i),
                                DateFormat.DATE_FORMAT);

                        String tempEndDay = DateFormat.dateToStr(DateFormat.getAfterDays(startDay, i + 1),
                                DateFormat.DATE_FORMAT);

                        StringBuilder tempStartDateTime = new StringBuilder();
                        StringBuilder tempEndDateTime = new StringBuilder();
                        StringBuilder businessStartDateTime = new StringBuilder();

                        tempStartDateTime.append(tempStartDay).append(" ").append("00:00:00");
                        tempEndDateTime.append(tempEndDay).append(" ").append("00:00:00");
                        businessStartDateTime.append(tempStartDay.substring(0, 10)).append(" ").append(businessTime);
                        
                        TblMachineHistory beforeTblMachineHistory = new TblMachineHistory();

                        setMachineHistoryInfo(tblMachineHistory, beforeTblMachineHistory);
                        
                        beforeTblMachineHistory.getTblMachineHistoryPK().setStartDate(DateFormat.getBeforeDay(DateFormat.strToDatetime(tempStartDateTime.toString())));
                        beforeTblMachineHistory.setEndDate(DateFormat.getBeforeDay(DateFormat.strToDatetime(businessStartDateTime.toString())));
                        tblMachineOperatingRateForDayList.add(beforeTblMachineHistory);

                        tempTblMachineHistory.getTblMachineHistoryPK()
                                .setStartDate(DateFormat.strToDatetime(businessStartDateTime.toString()));
                        tempTblMachineHistory.setEndDate(DateFormat.strToDatetime(tempEndDateTime.toString()));

                        tblMachineOperatingRateForDayList.add(tempTblMachineHistory);

                    }

                }
            }

        }

        // 設備稼働履歴リストのソート処理（生産日、設備UUID）
        Collections.sort(tblMachineOperatingRateForDayList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                TblMachineHistory obj1 = (TblMachineHistory) o1;
                TblMachineHistory obj2 = (TblMachineHistory) o2;

                // 比較用の開始日
                String startDateStr1 = DateFormat.dateToStr(obj1.getTblMachineHistoryPK().getStartDate(),
                        DateFormat.DATE_FORMAT);
                String startDateStr2 = DateFormat.dateToStr(obj2.getTblMachineHistoryPK().getStartDate(),
                        DateFormat.DATE_FORMAT);

                // 比較用設備UUID
                String machineUuid1 = obj1.getTblMachineHistoryPK().getMachineUuid();
                String machineUuid2 = obj2.getTblMachineHistoryPK().getMachineUuid();

                if (machineUuid1.equals(machineUuid2)) {

                    return startDateStr1.compareTo(startDateStr2);

                } else {

                    return machineUuid1.compareTo(machineUuid2);
                }

            }
        });

        List<TblMachineOperatingRatePeriodVo> tblMachineOperatingRatePeriodVos = new ArrayList();

        String tempProductionDateStr = "";
        String tempMachineUuid = "";
        long operatingTime = 0;

        TblMachineOperatingRatePeriodVo tblMachineOperatingRatePeriodVo = new TblMachineOperatingRatePeriodVo();

        // 生産日と設備UUIDをPKとして、稼動時間を集計する
        for (int i = 0; i < tblMachineOperatingRateForDayList.size(); i++) {

            TblMachineHistory tblMachineHistory = tblMachineOperatingRateForDayList.get(i);

            if (i == 0) {

                tempProductionDateStr = DateFormat.dateToStr(tblMachineHistory.getTblMachineHistoryPK().getStartDate(),
                        DateFormat.DATE_FORMAT);
                tempMachineUuid = tblMachineHistory.getTblMachineHistoryPK().getMachineUuid();
                operatingTime = DateFormat.dateDiff(tblMachineHistory.getTblMachineHistoryPK().getStartDate(),
                        tblMachineHistory.getEndDate(), DateFormat.DIFF_SECONDE);

                tblMachineOperatingRatePeriodVo
                        .setProductionDate(tblMachineHistory.getTblMachineHistoryPK().getStartDate());
                tblMachineOperatingRatePeriodVo.setMachineUuid(tempMachineUuid);
                tblMachineOperatingRatePeriodVo.setOperatingTime(operatingTime);
                
                // KM-267対応、控除時間を集計する
                tblMachineOperatingRatePeriodVo.setDeductionFlg(tblMachineHistory.getDeductionFlg());

                if (tblMachineHistory.getDeductionFlg()) {
                    tblMachineOperatingRatePeriodVo.setDeductionTime(operatingTime);
                }
                
            } else {

                String productionDateStr = DateFormat
                        .dateToStr(tblMachineHistory.getTblMachineHistoryPK().getStartDate(), DateFormat.DATE_FORMAT);

                // PK一致しない場合、新しいレコードを作製する
                if (!tempProductionDateStr.equals(productionDateStr)
                        || !tempMachineUuid.equals(tblMachineHistory.getTblMachineHistoryPK().getMachineUuid())) {

                    tblMachineOperatingRatePeriodVos.add(tblMachineOperatingRatePeriodVo);

                    tblMachineOperatingRatePeriodVo = new TblMachineOperatingRatePeriodVo();

                    tempProductionDateStr = DateFormat.dateToStr(
                            tblMachineHistory.getTblMachineHistoryPK().getStartDate(), DateFormat.DATE_FORMAT);
                    tempMachineUuid = tblMachineHistory.getTblMachineHistoryPK().getMachineUuid();
                    operatingTime = DateFormat.dateDiff(tblMachineHistory.getTblMachineHistoryPK().getStartDate(),
                            tblMachineHistory.getEndDate(), DateFormat.DIFF_SECONDE);

                    tblMachineOperatingRatePeriodVo
                            .setProductionDate(tblMachineHistory.getTblMachineHistoryPK().getStartDate());
                    tblMachineOperatingRatePeriodVo.setMachineUuid(tempMachineUuid);
                    tblMachineOperatingRatePeriodVo.setOperatingTime(operatingTime);
                    
                    // KM-267対応、控除時間を集計する
                    tblMachineOperatingRatePeriodVo.setDeductionFlg(tblMachineHistory.getDeductionFlg());
                    
                    if (tblMachineHistory.getDeductionFlg()) {
                        tblMachineOperatingRatePeriodVo.setDeductionTime(operatingTime);
                    }

                } else {// PK一致の場合、完成数の加算する
                    
                    long tempOperatingTime = DateFormat.dateDiff(
                            tblMachineHistory.getTblMachineHistoryPK().getStartDate(), tblMachineHistory.getEndDate(),
                            DateFormat.DIFF_SECONDE);

                    tblMachineOperatingRatePeriodVo
                            .setOperatingTime(tblMachineOperatingRatePeriodVo.getOperatingTime() + tempOperatingTime);

                    // KM-267対応、控除時間を集計する
                    if (tblMachineHistory.getDeductionFlg()) {
                        tblMachineOperatingRatePeriodVo.setDeductionTime(
                                tblMachineOperatingRatePeriodVo.getDeductionTime() + tempOperatingTime);
                    }
                }
            }

            // 最終のレコードを追加する
            if (i == tblMachineOperatingRateForDayList.size() - 1) {
                tblMachineOperatingRatePeriodVos.add(tblMachineOperatingRatePeriodVo);
            }

        }

        return tblMachineOperatingRatePeriodVos;

    }

    /**
     * 設備履歴情報をコピー
     *
     * @param tblMachineHistoryFrom
     * @param tblMachineHistoryTo
     *
     *
     */
    private void setMachineHistoryInfo(TblMachineHistory tblMachineHistoryFrom, TblMachineHistory tblMachineHistoryTo) {

        TblMachineHistoryPK tblMachineHistoryPK = new TblMachineHistoryPK();

        tblMachineHistoryTo.setTblMachineHistoryPK(tblMachineHistoryPK);

        tblMachineHistoryTo.getTblMachineHistoryPK()
                .setMachineUuid(tblMachineHistoryFrom.getTblMachineHistoryPK().getMachineUuid());
        tblMachineHistoryTo.getTblMachineHistoryPK()
                .setFirstEventNo(tblMachineHistoryFrom.getTblMachineHistoryPK().getFirstEventNo());
        tblMachineHistoryTo.getTblMachineHistoryPK()
                .setStartDate(tblMachineHistoryFrom.getTblMachineHistoryPK().getStartDate());
        tblMachineHistoryTo.setLastEventNo(tblMachineHistoryFrom.getLastEventNo());
        tblMachineHistoryTo.setLastEventDate(tblMachineHistoryFrom.getLastEventDate());
        tblMachineHistoryTo.setEndDate(tblMachineHistoryFrom.getEndDate());
        tblMachineHistoryTo.setShotCnt(tblMachineHistoryFrom.getShotCnt());
        tblMachineHistoryTo.setStatus(tblMachineHistoryFrom.getStatus());
        tblMachineHistoryTo.setDeductionFlg(tblMachineHistoryFrom.getDeductionFlg());

    }

    /**
     * 設備日別集計テーブルに登録する
     *
     * @param tblMachineOperatingRatePeriodVos
     *
     *
     */
    @Transactional
    private void insertMachineOperatingRatePerDay(
            List<TblMachineOperatingRatePeriodVo> tblMachineOperatingRatePeriodVos) {

        List<TblMachineOperatingRateForDay> insertTblMachineOperatingRateForDayList = new ArrayList();
        List<TblMachineOperatingRateForDay> updTblMachineOperatingRateForDayList = new ArrayList();

        for (TblMachineOperatingRatePeriodVo tblMachineOperatingRatePeriodVo : tblMachineOperatingRatePeriodVos) {

            TblMachineOperatingRateForDay tblMachineOperatingRateForDay = new TblMachineOperatingRateForDay();
            TblMachineOperatingRateForDay chkTblMachineOperatingRateForDay = tblMachineOperatingRateService
                    .isForDayExsistByPK(tblMachineOperatingRatePeriodVo.getMachineUuid(),
                            tblMachineOperatingRatePeriodVo.getProductionDate());

            if (null == chkTblMachineOperatingRateForDay) {

                setTblMachineOperatingRateForDay(tblMachineOperatingRateForDay, tblMachineOperatingRatePeriodVo);

                tblMachineOperatingRateForDay.setCreateUserUuid(BATCH_NAME);
                tblMachineOperatingRateForDay.setUpdateUserUuid(BATCH_NAME);

                Date nowDate = new Date();
                tblMachineOperatingRateForDay.setCreateDate(nowDate);
                tblMachineOperatingRateForDay.setUpdateDate(nowDate);

                insertTblMachineOperatingRateForDayList.add(tblMachineOperatingRateForDay);

            } else {

                setTblMachineOperatingRateForDay(tblMachineOperatingRateForDay, tblMachineOperatingRatePeriodVo);
                tblMachineOperatingRateForDay.setUpdateUserUuid(BATCH_NAME);
                Date nowDate = new Date();
                
                tblMachineOperatingRateForDay.setOperatingTime(tblMachineOperatingRateForDay.getOperatingTime()
                        + chkTblMachineOperatingRateForDay.getOperatingTime()
                        - chkTblMachineOperatingRateForDay.getDeductionTime());
                
                tblMachineOperatingRateForDay.setUpdateDate(nowDate);
                tblMachineOperatingRateForDay.setCreateDate(chkTblMachineOperatingRateForDay.getCreateDate());
                tblMachineOperatingRateForDay.setCreateUserUuid(chkTblMachineOperatingRateForDay.getCreateUserUuid());

                updTblMachineOperatingRateForDayList.add(tblMachineOperatingRateForDay);

            }
        }

        int insertCount = tblMachineOperatingRateService.batchInsertByType(insertTblMachineOperatingRateForDayList, 1);
        logger.log(LOG_LEVEL, "insertMachineOperatingRatePerDay:設備日別集計テーブルの登録件数：" + insertCount);

        int updateCount = tblMachineOperatingRateService.batchUpdateByType(updTblMachineOperatingRateForDayList, 1);
        logger.log(LOG_LEVEL, "updateMachineOperatingRatePerDay:設備日別集計テーブルの更新件数：" + updateCount);
    }

    /**
     * 設備日別集計テーブル登録用のObjectを設定
     *
     * @param tblMachineOperatingRateForDay
     * @param tblMachineOperatingRatePeriodVo
     *
     *
     */
    private void setTblMachineOperatingRateForDay(TblMachineOperatingRateForDay tblMachineOperatingRateForDay,
            TblMachineOperatingRatePeriodVo tblMachineOperatingRatePeriodVo) {

        TblMachineOperatingRateForDayPK tblMachineOperatingRateForDayPK = new TblMachineOperatingRateForDayPK();

        tblMachineOperatingRateForDayPK.setMachineUuid(tblMachineOperatingRatePeriodVo.getMachineUuid());
        tblMachineOperatingRateForDayPK.setProductionDate(tblMachineOperatingRatePeriodVo.getProductionDate());
        tblMachineOperatingRateForDay.setTblMachineOperatingRateForDayPK(tblMachineOperatingRateForDayPK);
        tblMachineOperatingRateForDay.setOperatingTime(tblMachineOperatingRatePeriodVo.getOperatingTime());
        
        // KM-267対応、控除時間を設定する
        tblMachineOperatingRateForDay.setDeductionTime(tblMachineOperatingRatePeriodVo.getDeductionTime());

    }

    /**
     * 設備週別集計テーブルに登録する
     *
     * @param tblMachineOperatingRatePeriodVos
     *
     *
     */
    @Transactional
    private void insertMachineOperatingRatePerWeek(
            List<TblMachineOperatingRatePeriodVo> tblMachineOperatingRatePeriodVos) {

        List<TblMachineOperatingRateForWeek> tblMachineOperatingRateForWeekList = new ArrayList();

        int listSize = tblMachineOperatingRatePeriodVos.size();

        if (listSize > 0) {

            Date minDate = tblMachineOperatingRatePeriodVos.get(0).getProductionDate();

            String startDayStr = DateFormat.getWeekMonday(minDate);
            String sunDayStr = DateFormat.getWeekSunday(minDate);

            Date startDay = DateFormat.strToDate(startDayStr);
            Date sunDay = DateFormat.strToDate(sunDayStr);

            String tempMachineUuid = "";

            TblMachineOperatingRateForWeek tempTblMachineOperatingRateForWeek = new TblMachineOperatingRateForWeek();

            // 週別生産実績の完成数を集計する
            for (int i = 0; i < listSize; i++) {

                TblMachineOperatingRatePeriodVo tblMachineOperatingRatePeriodVo = tblMachineOperatingRatePeriodVos
                        .get(i);

                if (i == 0) {

                    setTblMachineOperatingRateForWeek(tempTblMachineOperatingRateForWeek,
                            tblMachineOperatingRatePeriodVo, startDay, sunDay);

                    tempMachineUuid = tblMachineOperatingRatePeriodVo.getMachineUuid();

                    tempTblMachineOperatingRateForWeek
                            .setOperatingTime(tblMachineOperatingRatePeriodVo.getOperatingTime());
                    
                    // KM-267対応、控除時間を集計
                    if (tblMachineOperatingRatePeriodVo.getDeductionFlg()) {
                        tempTblMachineOperatingRateForWeek
                                .setDeductionTime(tblMachineOperatingRatePeriodVo.getDeductionTime());
                    }

                } else {

                    Date productionDate = tblMachineOperatingRatePeriodVo.getProductionDate();

                    String tempStartDayStr = DateFormat.getWeekMonday(productionDate);
                    Date tempStartDay = DateFormat.strToDate(tempStartDayStr);

                    String tempSunDayStr = DateFormat.getWeekSunday(productionDate);
                    Date tempSunDay = DateFormat.strToDate(tempSunDayStr);

                    if (!tempMachineUuid.equals(tblMachineOperatingRatePeriodVo.getMachineUuid())
                            || !startDayStr.equals(tempStartDayStr) || !sunDayStr.equals(tempSunDayStr)) {

                        tblMachineOperatingRateForWeekList.add(tempTblMachineOperatingRateForWeek);

                        tempTblMachineOperatingRateForWeek = new TblMachineOperatingRateForWeek();

                        startDay = tempStartDay;
                        sunDay = tempSunDay;
                        startDayStr = tempStartDayStr;
                        sunDayStr = tempSunDayStr;

                        setTblMachineOperatingRateForWeek(tempTblMachineOperatingRateForWeek,
                                tblMachineOperatingRatePeriodVo, startDay, sunDay);

                        tempMachineUuid = tblMachineOperatingRatePeriodVo.getMachineUuid();

                        tempTblMachineOperatingRateForWeek
                                .setOperatingTime(tblMachineOperatingRatePeriodVo.getOperatingTime());
                        
                        // KM-267対応、控除時間を集計
                        if (tblMachineOperatingRatePeriodVo.getDeductionFlg()) {
                            tempTblMachineOperatingRateForWeek
                                    .setDeductionTime(tblMachineOperatingRatePeriodVo.getDeductionTime());
                        }

                    } else {

                        tempTblMachineOperatingRateForWeek
                                .setOperatingTime(tempTblMachineOperatingRateForWeek.getOperatingTime()
                                        + tblMachineOperatingRatePeriodVo.getOperatingTime());
                        
                        // KM-267対応、控除時間を集計
                        if (tblMachineOperatingRatePeriodVo.getDeductionFlg()) {
                            tempTblMachineOperatingRateForWeek
                                    .setDeductionTime(tempTblMachineOperatingRateForWeek.getDeductionTime()
                                            + tblMachineOperatingRatePeriodVo.getDeductionTime());
                        }

                    }

                }

                // 最終のレコードを追加する
                if (i == listSize - 1) {
                    tblMachineOperatingRateForWeekList.add(tempTblMachineOperatingRateForWeek);
                }

            }

            List<TblMachineOperatingRateForWeek> insertTblMachineOperatingRateForWeekList = new ArrayList();
            List<TblMachineOperatingRateForWeek> updateTblMachineOperatingRateForWeekList = new ArrayList();

            for (TblMachineOperatingRateForWeek tblMachineOperatingRateForWeek : tblMachineOperatingRateForWeekList) {

                String machineUuid = tblMachineOperatingRateForWeek.getTblMachineOperatingRateForWeekPK()
                        .getMachineUuid();
                Date startDate = tblMachineOperatingRateForWeek.getTblMachineOperatingRateForWeekPK()
                        .getProductionDateStart();
                Date endDate = tblMachineOperatingRateForWeek.getTblMachineOperatingRateForWeekPK()
                        .getProductionDateEnd();

                TblMachineOperatingRateForWeek chkTblMachineOperatingRateForWeek = tblMachineOperatingRateService
                        .isForWeekExsistByPK(machineUuid, startDate, endDate);

                if (null == chkTblMachineOperatingRateForWeek) {

                    tblMachineOperatingRateForWeek.setCreateUserUuid(BATCH_NAME);
                    tblMachineOperatingRateForWeek.setUpdateUserUuid(BATCH_NAME);

                    Date nowDate = new Date();
                    tblMachineOperatingRateForWeek.setCreateDate(nowDate);
                    tblMachineOperatingRateForWeek.setUpdateDate(nowDate);

                    insertTblMachineOperatingRateForWeekList.add(tblMachineOperatingRateForWeek);

                } else {

                    tblMachineOperatingRateForWeek.setUpdateUserUuid(BATCH_NAME);
                    
                    // KM-267対応、控除時間を集計
                    tblMachineOperatingRateForWeek.setOperatingTime(tblMachineOperatingRateForWeek.getOperatingTime()
                            + chkTblMachineOperatingRateForWeek.getOperatingTime()
                            - chkTblMachineOperatingRateForWeek.getDeductionTime());
                    Date nowDate = new Date();
                    tblMachineOperatingRateForWeek.setUpdateDate(nowDate);
                    tblMachineOperatingRateForWeek.setCreateDate(chkTblMachineOperatingRateForWeek.getCreateDate());
                    tblMachineOperatingRateForWeek
                            .setCreateUserUuid(chkTblMachineOperatingRateForWeek.getCreateUserUuid());

                    updateTblMachineOperatingRateForWeekList.add(tblMachineOperatingRateForWeek);
                }
            }

            int insertCount = tblMachineOperatingRateService.batchInsertByType(insertTblMachineOperatingRateForWeekList,
                    2);
            logger.log(LOG_LEVEL, "insertMachineOperatingRatePerWeek:設備週別集計テーブルの登録件数：" + insertCount);

            int updateCount = tblMachineOperatingRateService.batchUpdateByType(updateTblMachineOperatingRateForWeekList,
                    2);
            logger.log(LOG_LEVEL, "updMachineOperatingRatePerWeek:設備週別集計テーブルの更新件数：" + updateCount);

        }

    }

    /**
     * 設備週別集計テーブル登録用のObjectを設定
     *
     * @param tblMachineOperatingRateForWeek
     * @param tblMachineOperatingRatePeriodVo
     * @param startDay
     * @param endDay
     *
     *
     */
    private void setTblMachineOperatingRateForWeek(TblMachineOperatingRateForWeek tblMachineOperatingRateForWeek,
            TblMachineOperatingRatePeriodVo tblMachineOperatingRatePeriodVo, Date startDay, Date endDay) {

        TblMachineOperatingRateForWeekPK tblMachineOperatingRateForWeekPK = new TblMachineOperatingRateForWeekPK();

        tblMachineOperatingRateForWeekPK.setMachineUuid(tblMachineOperatingRatePeriodVo.getMachineUuid());
        tblMachineOperatingRateForWeekPK.setProductionDateStart(startDay);
        tblMachineOperatingRateForWeekPK.setProductionDateEnd(endDay);
        tblMachineOperatingRateForWeek.setTblMachineOperatingRateForWeekPK(tblMachineOperatingRateForWeekPK);
    }

    /**
     * 設備月別集計テーブルに登録する
     *
     * @param tblMachineOperatingRatePeriodVos
     *
     *
     */
    @Transactional
    private void insertMachineOperatingRatePerMonth(
            List<TblMachineOperatingRatePeriodVo> tblMachineOperatingRatePeriodVos) {

        List<TblMachineOperatingRateForMonth> tblMachineOperatingRateForMonthList = new ArrayList();

        int listSize = tblMachineOperatingRatePeriodVos.size();

        if (listSize > 0) {

            Date minDate = tblMachineOperatingRatePeriodVos.get(0).getProductionDate();

            String firstDayStr = DateFormat.getFirstDay(minDate);
            String lastDayStr = DateFormat.getLastDay(minDate);

            Date firstDay = DateFormat.strToDate(firstDayStr);

            String tempMachineUuid = "";

            TblMachineOperatingRateForMonth tempTblMachineOperatingRateForMonth = new TblMachineOperatingRateForMonth();

            // 月別生産実績の完成数を集計する
            for (int i = 0; i < listSize; i++) {

                TblMachineOperatingRatePeriodVo tblMachineOperatingRatePeriodVo = tblMachineOperatingRatePeriodVos
                        .get(i);

                if (i == 0) {

                    setTblMachineOperatingRateForMonth(tempTblMachineOperatingRateForMonth,
                            tblMachineOperatingRatePeriodVo, firstDay);

                    tempMachineUuid = tblMachineOperatingRatePeriodVo.getMachineUuid();

                    tempTblMachineOperatingRateForMonth
                            .setOperatingTime(tblMachineOperatingRatePeriodVo.getOperatingTime());
                    
                    // KM-267対応、控除時間を集計
                    if (tblMachineOperatingRatePeriodVo.getDeductionFlg()) {
                        tempTblMachineOperatingRateForMonth
                                .setDeductionTime(tblMachineOperatingRatePeriodVo.getDeductionTime());
                    }

                } else {

                    Date productionDate = tblMachineOperatingRatePeriodVo.getProductionDate();

                    String tempFirstDayStr = DateFormat.getFirstDay(productionDate);
                    Date tempFirstDay = DateFormat.strToDate(tempFirstDayStr);

                    String tempLastDayStr = DateFormat.getLastDay(productionDate);

                    if (!tempMachineUuid.equals(tblMachineOperatingRatePeriodVo.getMachineUuid())
                            || !firstDayStr.equals(tempFirstDayStr) || !lastDayStr.equals(tempLastDayStr)) {

                        tblMachineOperatingRateForMonthList.add(tempTblMachineOperatingRateForMonth);

                        tempTblMachineOperatingRateForMonth = new TblMachineOperatingRateForMonth();

                        firstDay = tempFirstDay;
                        firstDayStr = tempFirstDayStr;
                        lastDayStr = tempLastDayStr;

                        setTblMachineOperatingRateForMonth(tempTblMachineOperatingRateForMonth,
                                tblMachineOperatingRatePeriodVo, firstDay);

                        tempMachineUuid = tblMachineOperatingRatePeriodVo.getMachineUuid();

                        tempTblMachineOperatingRateForMonth
                                .setOperatingTime(tblMachineOperatingRatePeriodVo.getOperatingTime());
                        
                        // KM-267対応、控除時間を集計
                        if (tblMachineOperatingRatePeriodVo.getDeductionFlg()) {
                            tempTblMachineOperatingRateForMonth
                                    .setDeductionTime(tblMachineOperatingRatePeriodVo.getDeductionTime());
                        }

                    } else {

                        tempTblMachineOperatingRateForMonth
                                .setOperatingTime(tempTblMachineOperatingRateForMonth.getOperatingTime()
                                        + tblMachineOperatingRatePeriodVo.getOperatingTime());
                        
                        // KM-267対応、控除時間を集計
                        if (tblMachineOperatingRatePeriodVo.getDeductionFlg()) {
                            tempTblMachineOperatingRateForMonth
                                    .setDeductionTime(tempTblMachineOperatingRateForMonth.getDeductionTime()
                                            + tblMachineOperatingRatePeriodVo.getDeductionTime());
                        }

                    }

                }

                // 最終のレコードを追加する
                if (i == listSize - 1) {
                    tblMachineOperatingRateForMonthList.add(tempTblMachineOperatingRateForMonth);
                }

            }

            List<TblMachineOperatingRateForMonth> insertTblMachineOperatingRateForMonthList = new ArrayList();
            List<TblMachineOperatingRateForMonth> updateTblMachineOperatingRateForMonthList = new ArrayList();

            for (TblMachineOperatingRateForMonth tblMachineOperatingRateForMonth : tblMachineOperatingRateForMonthList) {

                String machineUuid = tblMachineOperatingRateForMonth.getTblMachineOperatingRateForMonthPK()
                        .getMachineUuid();
                String productionMonth = tblMachineOperatingRateForMonth.getTblMachineOperatingRateForMonthPK()
                        .getProductionMonth();

                TblMachineOperatingRateForMonth chkTblMachineOperatingRateForMonth = tblMachineOperatingRateService
                        .isForMonthExsistByPK(machineUuid, productionMonth);

                if (null == chkTblMachineOperatingRateForMonth) {

                    tblMachineOperatingRateForMonth.setCreateUserUuid(BATCH_NAME);
                    tblMachineOperatingRateForMonth.setUpdateUserUuid(BATCH_NAME);

                    Date nowDate = new Date();
                    tblMachineOperatingRateForMonth.setCreateDate(nowDate);
                    tblMachineOperatingRateForMonth.setUpdateDate(nowDate);

                    insertTblMachineOperatingRateForMonthList.add(tblMachineOperatingRateForMonth);

                } else {

                    tblMachineOperatingRateForMonth.setUpdateUserUuid(BATCH_NAME);
                    
                    // KM-267対応、控除時間を集計
                    tblMachineOperatingRateForMonth.setOperatingTime(tblMachineOperatingRateForMonth.getOperatingTime()
                            + chkTblMachineOperatingRateForMonth.getOperatingTime()
                            - chkTblMachineOperatingRateForMonth.getDeductionTime());
                    
                    Date nowDate = new Date();
                    tblMachineOperatingRateForMonth.setUpdateDate(nowDate);
                    tblMachineOperatingRateForMonth.setCreateDate(chkTblMachineOperatingRateForMonth.getCreateDate());
                    tblMachineOperatingRateForMonth
                            .setCreateUserUuid(chkTblMachineOperatingRateForMonth.getCreateUserUuid());

                    updateTblMachineOperatingRateForMonthList.add(tblMachineOperatingRateForMonth);
                }
            }

            int insertCount = tblMachineOperatingRateService
                    .batchInsertByType(insertTblMachineOperatingRateForMonthList, 3);
            logger.log(LOG_LEVEL, "insertMachineOperatingRatePerWeek:設備月別集計テーブルの登録件数：" + insertCount);

            int updateCount = tblMachineOperatingRateService
                    .batchUpdateByType(updateTblMachineOperatingRateForMonthList, 3);
            logger.log(LOG_LEVEL, "updMachineOperatingRatePerWeek:設備月別集計テーブルの更新件数：" + updateCount);

        }

    }

    /**
     * 設備月別集計テーブル登録用のObjectを設定
     *
     * @param tblMachineOperatingRateForMonth
     * @param tblMachineProductionPeriodVo
     * @param productionMonth
     *
     *
     */
    private void setTblMachineOperatingRateForMonth(TblMachineOperatingRateForMonth tblMachineOperatingRateForMonth,
            TblMachineOperatingRatePeriodVo tblMachineOperatingRatePeriodVo, Date productionMonth) {

        TblMachineOperatingRateForMonthPK tblMachineOperatingRateForMonthPK = new TblMachineOperatingRateForMonthPK();

        tblMachineOperatingRateForMonthPK.setMachineUuid(tblMachineOperatingRatePeriodVo.getMachineUuid());
        tblMachineOperatingRateForMonthPK
                .setProductionMonth(DateFormat.dateToStr(productionMonth, DateFormat.DATE_FORMAT_MONTH));
        tblMachineOperatingRateForMonth.setTblMachineOperatingRateForMonthPK(tblMachineOperatingRateForMonthPK);
    }

    /**
     * 設備履歴テーブルに更新する
     *
     * @param tblMachineHistoryList
     *
     */
    @Transactional
    private void updMachinHistory(List<TblMachineHistory> tblMachineHistoryList) {

        for (int i = 0; i < tblMachineHistoryList.size(); i++) {

            tblMachineHistoryList.get(i).setAggregatedFlg(1);
            
            // KM-267対応
            if(tblMachineHistoryList.get(i).getDeductionFlg()){
                
                tblMachineHistoryList.get(i).setEndDate(null);
            }
        }

        int count = tblMachineHistoryService.batchUpdate(tblMachineHistoryList);
        logger.log(LOG_LEVEL, "updMachinHistory:設備履歴テーブルの更新件数：" + count);

    }

}
