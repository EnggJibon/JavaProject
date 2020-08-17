/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.smt.productline;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.graphical.GraphicalAxis;
import com.kmcj.karte.graphical.GraphicalData;
import com.kmcj.karte.graphical.GraphicalItemInfo;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.circuitboard.smt.SMTGraphList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.FileUtil;

import java.math.BigDecimal;
import java.util.*;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author zf
 */
@Dependent
public class SMTproductionService {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    MstDictionaryService mstDictionaryService;

    private List getTblproductionMachineLog(int lineNo) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" SUM(t.numcol1), SUM(t.numcol2), SUM(t.numcol3), SUM(t.numcol4),");
        sql.append(" SUM(t.numcol5), SUM(t.numcol6), SUM(t.numcol7), SUM(t.numcol8),");
        sql.append(" SUM(t.numcol9), SUM(t.numcol11), m2.machineName");
        sql.append(" FROM TblAutomaticMachineLog t ");
        sql.append(" LEFT JOIN FETCH t.mstProductionLine m1");
        sql.append(" JOIN FETCH t.mstMachine m2");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND t.machineType = :machineType ");
        sql.append(" AND t.logType = :logType ");
        
        if (!"".equals(lineNo)) {
            sql.append(" AND t.lineNumber = :lineNo ");
        }
        
        sql.append(" GROUP BY t.tblAutomaticMachineLogPK.machineUuid ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("machineType", CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT);
        query.setParameter("logType", CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT_P);
        if (!"".equals(lineNo)) {
            query.setParameter("lineNo", lineNo);
        }

        return query.getResultList();

    }
//    public  List  getTblproductionunits(String lineNo ){
//       StringBuilder sql = new StringBuilder();
//       
//       sql.append("SELECT m.productionLineId,COUNT(m.productionLineMachineId) "+
//               " FROM MstProductionLineMachine m"+
//               " WHERE m.productionLineId.productionLineId = :productionLineId"+
//               " GROUP BY  m.productionLineId.");
//       Query query = entityManager.createQuery(sql.toString());
//       query.setParameter("productionLineId", lineNo);
//       return query.getResultList();
//            }

    /**
     * @param lineNo
     * @return
     */

    public SMTGraphList getSearchResult(int lineNo) {
        SMTGraphList smtGraphList = new SMTGraphList();

        SMTproductionVo vo = new SMTproductionVo();
        List<SMTProductionVoDetail> resultList = new ArrayList<>();

        List list = getTblproductionMachineLog(lineNo);
        BigDecimal machineNum = BigDecimal.ZERO;

        for (Object obj : list) {
            Object[] tblAutomaticMachineLog = (Object[]) obj;
            machineNum = machineNum.add(tblAutomaticMachineLog[9] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[9])));
        }
        vo.setLineMachines(machineNum.longValue());

        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Object[] tblAutomaticMachineLog = (Object[]) iterator.next();
            SMTProductionVoDetail detail = new SMTProductionVoDetail();

            detail.setMachineName(tblAutomaticMachineLog[10] == null ? "" : String.valueOf(tblAutomaticMachineLog[10]));
            detail.setPowerTime(tblAutomaticMachineLog[0] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[0])));//電源時間
            detail.setWorkTime(tblAutomaticMachineLog[1] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[1])));//運転時間
            detail.setOperationTime(tblAutomaticMachineLog[2] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[2])));//運転準備時間
            detail.setLoadTime(tblAutomaticMachineLog[3] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[3])));//基板待ち時間(ローダー)
            detail.setUnloadTime(tblAutomaticMachineLog[4] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[4])));//基板待ち時間(アンローダー)
            detail.setMaintenanceTime(tblAutomaticMachineLog[5] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[5])));//メンテナンス時間
            detail.setTroubleStopTime(tblAutomaticMachineLog[6] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[6])));//トラブル停止時間
            detail.setComponentClothNumber(tblAutomaticMachineLog[7] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[7])));//部品切れ停止回数
            detail.setComponentClothTime(tblAutomaticMachineLog[8] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[8])));   //部品切れ停止時間

            //実装タクト
            // 生産台数が０より大きい場合に、運転時間／生産台数の計算を実行する。生産台数が0の場合は0を表示  生産台数
            if (machineNum.compareTo(BigDecimal.ZERO) == 1) {
                detail.setDiviceTakt(detail.getWorkTime()
                        .divide(machineNum, 4, BigDecimal.ROUND_DOWN));
            } else {
                detail.setDiviceTakt(BigDecimal.ZERO);
            }

            // 部品切れ時間/回
            // 部品切れ回数が０より大きい場合に、部品切れ時間／部品切れ回数／60の計算を実行する。部品切れ回数が0の場合は0を表示 部品切れ時間/回
            if (detail.getComponentClothNumber().compareTo(BigDecimal.ZERO) == 1) {
                BigDecimal a = detail.getComponentClothTime().divide(detail.getComponentClothNumber(), 4, BigDecimal.ROUND_DOWN);
                BigDecimal b = new BigDecimal("60");
                detail.setComponentClothRate(a.divide(b, 4, BigDecimal.ROUND_DOWN));
            } else {
                detail.setComponentClothRate(BigDecimal.ZERO);
            }

            // ﾀｸﾄ（電源/台数）
            // 生産台数が０より大きい場合に、電源ON時間／生産台数の計算を実行する。生産台数が0の場合は0を表示  ﾀｸﾄ（電源/台数）
            if (machineNum.compareTo(BigDecimal.ZERO) == 1) {
                detail.setPowerRateTakt(detail.getPowerTime()
                        .divide(machineNum, 4, BigDecimal.ROUND_DOWN));
            } else {
                detail.setPowerRateTakt(BigDecimal.ZERO);
            }

            // 合計の計算
            vo.setTotalPowerTime(vo.getTotalPowerTime().add(detail.getPowerTime()));
            vo.setTotalWorkTime(vo.getTotalWorkTime().add(detail.getWorkTime()));
            vo.setTotalOperationTime(vo.getTotalOperationTime().add(detail.getOperationTime()));
            vo.setTotalLoadTime(vo.getTotalLoadTime().add(detail.getLoadTime()));
            vo.setTotalUnloadTime(vo.getTotalUnloadTime().add(detail.getUnloadTime()));
            vo.setTotalMaintenanceTime(vo.getTotalMaintenanceTime().add(detail.getMaintenanceTime()));
            vo.setTotalTroubleStopTime(vo.getTotalTroubleStopTime().add(detail.getTroubleStopTime()));
            vo.setTotalComponentClothNumber(vo.getTotalComponentClothNumber().add(detail.getComponentClothNumber()));
            vo.setTotalComponentClothTime(vo.getTotalComponentClothTime().add(detail.getComponentClothTime()));
            vo.setTotalDiviceTakt(vo.getTotalDiviceTakt().add(detail.getDiviceTakt()));
            vo.setTotalComponentClothRate(vo.getTotalComponentClothRate().add(detail.getComponentClothRate()));
            vo.setTotalPowerRateTakt(vo.getTotalPowerRateTakt().add(detail.getPowerRateTakt()));

            // 最大値の計算
            vo.setMaxPowerTime(vo.getMaxPowerTime().compareTo(
                    detail.getPowerTime()) == -1 ? detail.getPowerTime() : vo.getMaxPowerTime());
            vo.setMaxWorkTime(vo.getMaxWorkTime().compareTo(
                    detail.getWorkTime()) == -1 ? detail.getWorkTime() : vo.getMaxWorkTime());
            vo.setMaxOperationTime(vo.getMaxOperationTime().compareTo(
                    detail.getOperationTime()) == -1 ? detail.getOperationTime() : vo.getMaxOperationTime());
            vo.setMaxLoadTime(vo.getMaxLoadTime().compareTo(
                    detail.getLoadTime()) == -1 ? detail.getLoadTime() : vo.getMaxLoadTime());
            vo.setMaxUnloadTime(vo.getMaxUnloadTime().compareTo(
                    detail.getUnloadTime()) == -1 ? detail.getUnloadTime() : vo.getMaxUnloadTime());
            vo.setMaxMaintenanceTime(vo.getMaxMaintenanceTime().compareTo(
                    detail.getMaintenanceTime()) == -1 ? detail.getMaintenanceTime() : vo.getMaxMaintenanceTime());
            vo.setMaxTroubleStopTime(vo.getMaxTroubleStopTime().compareTo(
                    detail.getTroubleStopTime()) == -1 ? detail.getTroubleStopTime() : vo.getMaxTroubleStopTime());
            vo.setMaxComponentClothNumber(vo.getMaxComponentClothNumber().compareTo(
                    detail.getComponentClothNumber()) == -1 ? detail.getComponentClothNumber() : vo.getMaxComponentClothNumber());
            vo.setMaxComponentClothTime(vo.getMaxComponentClothTime().compareTo(
                    detail.getComponentClothTime()) == -1 ? detail.getComponentClothTime() : vo.getMaxComponentClothTime());
            vo.setMaxDiviceTakt(vo.getMaxDiviceTakt().compareTo(
                    detail.getDiviceTakt()) == -1 ? detail.getDiviceTakt() : vo.getMaxDiviceTakt());
            vo.setMaxComponentClothRate(vo.getMaxComponentClothRate().compareTo(
                    detail.getComponentClothRate()) == -1 ? detail.getComponentClothRate() : vo.getMaxComponentClothRate());
            vo.setMaxPowerRateTakt(vo.getMaxPowerRateTakt().compareTo(
                    detail.getPowerRateTakt()) == -1 ? detail.getPowerRateTakt() : vo.getMaxPowerRateTakt());

            resultList.add(detail);
        }
        vo.setSmtProductionVos(resultList);
        smtGraphList.setSmtProduction(vo);
        return smtGraphList;
    }

    /**
     * 機種別稼動状況グラフ
     *
     * @param smtGraphList
     * @return
     */
    public SMTGraphList getGraphOfMachines(SMTGraphList smtGraphList, LoginUser loginUser) {

        Map<String, String> smtDicMap = getDictionary(loginUser.getLangId());

        List<GraphicalItemInfo> graphicalItemInfoList = new ArrayList<>();

        SMTproductionVo smtProduction = smtGraphList.getSmtProduction();
        for (SMTProductionVoDetail detail : smtProduction.getSmtProductionVos()) {

            GraphicalItemInfo graphicalItemInfo = new GraphicalItemInfo();
            List<GraphicalAxis> graphicalAxis = new ArrayList<>();
            GraphicalAxis axis = new GraphicalAxis();
            List<String> options = new ArrayList<>();

            options.add(smtDicMap.get("operation_time"));
            options.add(smtDicMap.get("operation_standard_time"));
            options.add(smtDicMap.get("circuit_board_waiting_time_loader"));
            options.add(smtDicMap.get("circuit_board_waiting_time_unloader"));
            options.add(smtDicMap.get("maintenance_time"));
            options.add(smtDicMap.get("trouble_downtime"));
            options.add(smtDicMap.get("component_cutting_time"));

            axis.setTicks(options.toArray(new String[0]));
            graphicalAxis.add(axis);
            graphicalItemInfo.setxAxisList(graphicalAxis);

            List<GraphicalData> dataList = new ArrayList<>();
            GraphicalData pieData = new GraphicalData();
            List<String> datas = new ArrayList<>();

            BigDecimal totalTime = detail.getWorkTime()
                    .add(detail.getOperationTime())
                    .add(detail.getLoadTime())
                    .add(detail.getUnloadTime())
                    .add(detail.getMaintenanceTime())
                    .add(detail.getTroubleStopTime())
                    .add(detail.getComponentClothTime());

            if (totalTime.compareTo(BigDecimal.ZERO) != 0) {
                datas.add(detail.getWorkTime().divide
                        (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 運転時間
                datas.add(detail.getOperationTime().divide
                        (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 運転準時間
                datas.add(detail.getLoadTime().divide
                        (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 基板待ち時間ローダー
                datas.add(detail.getUnloadTime().divide
                        (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 基板待ち時間アンローダー
                datas.add(detail.getMaintenanceTime().divide
                        (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// メンテナンス時間
                datas.add(detail.getTroubleStopTime().divide
                        (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// トラブル時間
                datas.add(detail.getComponentClothTime().divide
                        (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 部品切れ時間
            } else {
                datas.add("0");
                datas.add("0");
                datas.add("0");
                datas.add("0");
                datas.add("0");
                datas.add("0");
                datas.add("0");
                datas.add("0");
            }
            pieData.setDataName(detail.getMachineName());
            pieData.setGraphType("pie");
            pieData.setDataValue(datas.toArray(new String[0]));
            dataList.add(pieData);
            graphicalItemInfo.setOptionSubtitle(detail.getMachineName() + CommonConstants.SMT_TITLE_OPERATION_ANALYSIS);
            graphicalItemInfo.setOptionTitle(detail.getMachineName());
            graphicalItemInfo.setDataList(dataList);
            graphicalItemInfoList.add(graphicalItemInfo);
        }
        smtGraphList.setGraphicalItemInfoList(graphicalItemInfoList);
        return smtGraphList;
    }

    /**
     * 生産ライン稼動状況グラフ
     *
     * @param smtGraphList
     * @param loginUser
     * @return
     */
    public SMTGraphList getGraphOfLine(SMTGraphList smtGraphList, LoginUser loginUser) {

        Map<String, String> smtDicMap = getDictionary(loginUser.getLangId());

        List<GraphicalItemInfo> graphicalItemInfoList = new ArrayList<>();

        SMTproductionVo smtProduction = smtGraphList.getSmtProduction();

        // 生産ライン稼動状況グラフの三つを作成
        for (int i = 0; i < 3; i++) {

            GraphicalItemInfo graphicalItemInfo = new GraphicalItemInfo();
            List<GraphicalAxis> graphicalxAxis = new ArrayList<>();
            List<GraphicalAxis> graphicalyAxis = new ArrayList<>();

            List<GraphicalData> dataList = new ArrayList<>();
            switch (i) {
                // 「ラインバランス」グラフの作成
                case 0:
                    graphicalItemInfo.setOptionTitle(CommonConstants.SMT_TITLE_LINE_BALANCE);
                    graphicalItemInfo.setOptionSubtitle(CommonConstants.SMT_TITLE_LINE_BALANCE);
                    List<GraphicalAxis> balanceGraphicalAxis = new ArrayList<>();
                    GraphicalAxis balanceAxis = new GraphicalAxis();
                    List<String> balanceOptions = new ArrayList<>();

                    for (SMTProductionVoDetail detail : smtProduction.getSmtProductionVos()) {
                        balanceOptions.add(detail.getMachineName());
                        GraphicalData pieData = new GraphicalData();
                        List<String> datas = new ArrayList<>();

                        BigDecimal totalTime = detail.getWorkTime()
                                .add(detail.getOperationTime())
                                .add(detail.getLoadTime())
                                .add(detail.getUnloadTime())
                                .add(detail.getMaintenanceTime())
                                .add(detail.getTroubleStopTime())
                                .add(detail.getComponentClothTime());
                        if (totalTime.compareTo(BigDecimal.ZERO) != 0) {

                            datas.add(detail.getWorkTime().divide
                                    (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 運転時間
                            datas.add(detail.getOperationTime().divide
                                    (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 運転準時間
                            datas.add(detail.getLoadTime().divide
                                    (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 基板待ち時間ローダー
                            datas.add(detail.getUnloadTime().divide
                                    (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 基板待ち時間アンローダー
                            datas.add(detail.getMaintenanceTime().divide
                                    (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// メンテナンス時間
                            datas.add(detail.getTroubleStopTime().divide
                                    (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// トラブル時間
                            datas.add(detail.getComponentClothTime().divide
                                    (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 部品切れ時間
                        } else {
                            datas.add("0");
                            datas.add("0");
                            datas.add("0");
                            datas.add("0");
                            datas.add("0");
                            datas.add("0");
                            datas.add("0");
                        }
                        pieData.setDataName(detail.getMachineName());
                        pieData.setGraphType("bar");
                        pieData.setDataValue(datas.toArray(new String[0]));
                        pieData.setYaxisFlg(2);
                        dataList.add(pieData);
                    }
                    balanceAxis.setTicks(balanceOptions.toArray(new String[0]));
                    balanceGraphicalAxis.add(balanceAxis);
                    graphicalItemInfo.setxAxisList(balanceGraphicalAxis);
                    graphicalItemInfo.setDataList(dataList);
                    break;
                // 「ライン稼動分析」グラフの作成
                case 1:
                    graphicalItemInfo.setOptionTitle(CommonConstants.SMT_TITLE_LINE_OPERATION_ANALYSIS);
                    graphicalItemInfo.setOptionSubtitle(CommonConstants.SMT_TITLE_LINE_OPERATION_ANALYSIS);
                    GraphicalAxis axis = new GraphicalAxis();
                    List<String> options = new ArrayList<>();

                    options.add(smtDicMap.get("operation_time"));
                    options.add(smtDicMap.get("operation_standard_time"));
                    options.add(smtDicMap.get("circuit_board_waiting_time_loader"));
                    options.add(smtDicMap.get("circuit_board_waiting_time_unloader"));
                    options.add(smtDicMap.get("maintenance_time"));
                    options.add(smtDicMap.get("trouble_downtime"));
                    options.add(smtDicMap.get("component_cutting_time"));

                    axis.setTicks(options.toArray(new String[0]));
                    graphicalxAxis.add(axis);
                    dataList = new ArrayList<>();
                    GraphicalData pieData = new GraphicalData();
                    List<String> datas = new ArrayList<>();

                    BigDecimal totalTime = smtProduction.getTotalWorkTime()
                            .add(smtProduction.getTotalOperationTime())
                            .add(smtProduction.getTotalLoadTime())
                            .add(smtProduction.getTotalUnloadTime())
                            .add(smtProduction.getTotalMaintenanceTime())
                            .add(smtProduction.getTotalTroubleStopTime())
                            .add(smtProduction.getTotalComponentClothTime());
                    if (totalTime.compareTo(BigDecimal.ZERO) != 0) {

                        datas.add(smtProduction.getTotalWorkTime().divide
                                (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 運転時間
                        datas.add(smtProduction.getTotalOperationTime().divide
                                (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 運転準時間
                        datas.add(smtProduction.getTotalLoadTime().divide
                                (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 基板待ち時間ローダー
                        datas.add(smtProduction.getTotalUnloadTime().divide
                                (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 基板待ち時間アンローダー
                        datas.add(smtProduction.getTotalMaintenanceTime().divide
                                (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// メンテナンス時間
                        datas.add(smtProduction.getTotalTroubleStopTime().divide
                                (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// トラブル時間
                        datas.add(smtProduction.getTotalComponentClothTime().divide
                                (totalTime, 4, BigDecimal.ROUND_DOWN).toString());// 部品切れ時間
                    } else {
                        datas.add("0");
                        datas.add("0");
                        datas.add("0");
                        datas.add("0");
                        datas.add("0");
                        datas.add("0");
                        datas.add("0");
                    }
                    pieData.setGraphType("pie");
                    pieData.setDataValue(datas.toArray(new String[0]));
                    graphicalItemInfo.setDataList(dataList);
                    dataList.add(pieData);
                    break;
                // 「部品切れ時間/回、実装タクト」グラフの作成
                case 2:
                    graphicalItemInfo.setOptionTitle(CommonConstants.SMT_TITLE_COMPONENT_CUTTING_TIME_TAKT);
                    graphicalItemInfo.setOptionSubtitle(CommonConstants.SMT_TITLE_COMPONENT_CUTTING_TIME_TAKT);

                    List<GraphicalData> taktDataList = new ArrayList<>();
                    GraphicalData componentClothRateData = new GraphicalData();
                    GraphicalData taktData = new GraphicalData();
                    GraphicalData deviceTaktData = new GraphicalData();
                    List<String> componentClothRateDatas = new ArrayList<>();
                    List<String> taktDatas = new ArrayList<>();
                    List<String> deviceTaktDatas = new ArrayList<>();

                    GraphicalAxis taktxAxis = new GraphicalAxis();
                    List<String> xdata = new ArrayList<>();
                    GraphicalAxis yAxisLeft = new GraphicalAxis();
                    GraphicalAxis yAxisRight = new GraphicalAxis();

                    BigDecimal maxLeftTick = BigDecimal.ZERO;
                    BigDecimal minLeftTick = BigDecimal.ZERO;
                    BigDecimal maxRightTick = BigDecimal.ZERO;
                    BigDecimal minRightTick = BigDecimal.ZERO;

                    List<BigDecimal> leftTicksList = new ArrayList<>();
                    List<BigDecimal> rightTicksList = new ArrayList<>();

                    for (SMTProductionVoDetail detail : smtProduction.getSmtProductionVos()) {

                        xdata.add(detail.getMachineName());
                        componentClothRateDatas.add(detail.getComponentClothRate().toString());
                        taktDatas.add(detail.getPowerRateTakt().toString());
                        deviceTaktDatas.add(detail.getDiviceTakt().toString());
                        leftTicksList.add(detail.getComponentClothRate());
                        rightTicksList.add(detail.getPowerRateTakt());
                        rightTicksList.add(detail.getDiviceTakt());
                    }

                    leftTicksList.sort(BigDecimal::compareTo);
                    rightTicksList.sort(BigDecimal::compareTo);

                    if (leftTicksList.size() > 0) {
                        minLeftTick = leftTicksList.get(0);
                        maxLeftTick = leftTicksList.get(leftTicksList.size() - 1);
                    }

                    if (rightTicksList.size() > 0) {
                        minRightTick = rightTicksList.get(0);
                        maxRightTick = rightTicksList.get(rightTicksList.size() - 1);
                    }


                    // x軸表示データ
                    taktxAxis.setTicks(xdata.toArray(new String[0]));
                    graphicalxAxis.add(taktxAxis);

                    // y軸表示データ最大値/最小値
                    BigDecimal num = maxLeftTick.subtract(minLeftTick).multiply(new BigDecimal(0.1));
                    yAxisLeft.setMaxTicks(String.valueOf(maxLeftTick.add(num).setScale(4,BigDecimal.ROUND_DOWN)));
                    if (minLeftTick.compareTo(BigDecimal.ZERO) == 0 || minLeftTick.compareTo(num) != 1) {
                        yAxisLeft.setMinTicks(String.valueOf(BigDecimal.ZERO));
                    } else {
                        yAxisLeft.setMinTicks(String.valueOf(minLeftTick.subtract(num).setScale(4,BigDecimal.ROUND_DOWN)));
                    }
//                    yAxisLeft.setMaxTicks(String.valueOf(maxLeftTick));
//                    yAxisLeft.setMinTicks(String.valueOf(minLeftTick));
                    yAxisRight.setMaxTicks(String.valueOf(maxRightTick));
                    yAxisRight.setMinTicks(String.valueOf(minRightTick));
                    graphicalyAxis.add(yAxisLeft);
                    graphicalyAxis.add(yAxisRight);

                    componentClothRateData.setDataName(CommonConstants.SMT_TITLE_COMPONENT_CUTTING_TIME);
                    componentClothRateData.setGraphType("bar");
                    componentClothRateData.setDataValue(componentClothRateDatas.toArray(new String[0]));
                    taktData.setDataName(CommonConstants.SMT_TITLE_POWER_TAKT);
                    taktData.setGraphType("line");
                    taktData.setDataValue(taktDatas.toArray(new String[0]));
                    deviceTaktData.setDataName(CommonConstants.SMT_TITLE_EQUIPMENT);
                    deviceTaktData.setGraphType("line");
                    deviceTaktData.setDataValue(deviceTaktDatas.toArray(new String[0]));

                    taktDataList.add(componentClothRateData);
                    taktDataList.add(taktData);
                    taktDataList.add(deviceTaktData);
                    graphicalItemInfo.setDataList(taktDataList);
                    break;
            }
            graphicalItemInfo.setxAxisList(graphicalxAxis);
            graphicalItemInfo.setyAxisList(graphicalyAxis);
            graphicalItemInfoList.add(graphicalItemInfo);
        }
        smtGraphList.setGraphicalItemInfoList(graphicalItemInfoList);
        return smtGraphList;
    }

    /**
     * 1:運転時間　2:運転準時間　3:基板待ち時間ローダー　4:基板待ち時間アンローダー
     * 　5:メンテナンス時間　6:トラブル時間　7:部品切れ時間
     *
     * @return
     */
    private Map<String, String> getDictionary(String langId) {
        Map<String, String> dictionariesMap = new HashMap<>();
        List listKey = new ArrayList();
        listKey.add("operation_time");
        listKey.add("operation_standard_time");
        listKey.add("circuit_board_waiting_time_loader");
        listKey.add("circuit_board_waiting_time_unloader");
        listKey.add("maintenance_time");
        listKey.add("trouble_downtime");
        listKey.add("component_cutting_time");
        Map<String, String> map = FileUtil.getDictionaryList(mstDictionaryService, langId, listKey);
        // 運転時間
        dictionariesMap.put(CommonConstants.SMT_OPERATION_TIME, map.get("operation_time"));
        // 運転準時間
        dictionariesMap.put(CommonConstants.SMT_OPERATION_STANDARD_TIME, map.get("operation_standard_time"));
        // 基板待ち時間ローダー
        dictionariesMap.put(CommonConstants.SMT_CIRCUIT_BOARD_WAITING_TIME_LOADER, map.get("circuit_board_waiting_time_loader"));
        // 基板待ち時間アンローダー
        dictionariesMap.put(CommonConstants.SMT_CIRCUIT_BOARD_WAITING_TIME_UNLOADER, map.get("circuit_board_waiting_time_unloader"));
        // メンテナンス時間
        dictionariesMap.put(CommonConstants.SMT_MAINTENANCE_TIME, map.get("maintenance_time"));
        // トラブル時間
        dictionariesMap.put(CommonConstants.SMT_TROUBLE_DOWNTIME, map.get("trouble_downtime"));
        // 部品切れ時間
        dictionariesMap.put(CommonConstants.SMT_COMPONENT_CUTTING_TIME, map.get("component_cutting_time"));

        return dictionariesMap;
    }

}
