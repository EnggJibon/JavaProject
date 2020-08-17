/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.lot.acceptance;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.excelhandle.write.WriteExcelList;
import com.kmcj.karte.excelhandle.write.WriteListExcel;
import com.kmcj.karte.graphical.GraphicalAxis;
import com.kmcj.karte.graphical.GraphicalData;
import com.kmcj.karte.graphical.GraphicalItemInfo;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResultService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Lot acceptance report
 * 
 * @author liujiyong
 */
@Dependent
public class TblComponentLotAcceptanceReportService {
    
    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private CnfSystemService cnfSystemService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private TblComponentInspectionResultService tblComponentInspectionResultService;
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;
    
    private static final String DAY = "day";
    private static final String WEEK = "week";
    private static final String MONTH = "month";
    
    /**
     * ロット受入レポートデータ取得
     *
     * @param incomingCompanyId
     * @param componentCode
     * @param periodFlag
     * @param reportDateStart
     * @param reportDateEnd
     * @param cavityPrefix
     * @param cavityNum
     * @param loginUser
     * @return
     */
    public TblComponentLotAcceptanceReportList getLotAcceptanceReportDataList(String incomingCompanyId, String componentCode, String periodFlag, String reportDateStart, String reportDateEnd, String cavityPrefix, int cavityNum, LoginUser loginUser) {
        TblComponentLotAcceptanceReportList tblComponentLotAcceptanceReportList = new TblComponentLotAcceptanceReportList();
        Date sDate;
        Date eDate;
        switch (periodFlag) {
            case WEEK: // 期間種類が週別
                sDate = DateFormat.strToDate(reportDateStart);
                eDate = DateFormat.strToDate(reportDateEnd);
                if (sDate == null || eDate == null) {
                    tblComponentLotAcceptanceReportList.setError(true);
                    tblComponentLotAcceptanceReportList.setErrorCode(ErrorMessages.E201_APPLICATION);
                    tblComponentLotAcceptanceReportList.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                    return tblComponentLotAcceptanceReportList;
                }
                break;
            case MONTH: // 期間種類が月別
                sDate = DateFormat.strToMonth(reportDateStart);
                eDate = DateFormat.strToMonth(reportDateEnd);
                if (sDate == null || eDate == null) {
                    tblComponentLotAcceptanceReportList.setError(true);
                    tblComponentLotAcceptanceReportList.setErrorCode(ErrorMessages.E201_APPLICATION);
                    tblComponentLotAcceptanceReportList.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                    return tblComponentLotAcceptanceReportList;
                }
                break;
            default: // 期間種類が日別
                sDate = DateFormat.strToDatetime(reportDateStart + CommonConstants.SYS_MIN_TIME);
                eDate = DateFormat.strToDatetime(reportDateEnd + CommonConstants.SYS_MAX_TIME);
                if (sDate == null || eDate == null) {
                    tblComponentLotAcceptanceReportList.setError(true);
                    tblComponentLotAcceptanceReportList.setErrorCode(ErrorMessages.E201_APPLICATION);
                    tblComponentLotAcceptanceReportList.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
                    return tblComponentLotAcceptanceReportList;
                }
                break;
        }
        
        List acceptedlist = getLotAcceptanceReportAcceptedSql(incomingCompanyId, componentCode, periodFlag, sDate, eDate, cavityPrefix, cavityNum, true);
        List rejectedlist = getLotAcceptanceReportAcceptedSql(incomingCompanyId, componentCode, periodFlag, sDate, eDate, cavityPrefix, cavityNum, false);
        tblComponentLotAcceptanceReportList = getLotAcceptReportPeriodData(acceptedlist, rejectedlist, periodFlag, reportDateStart, reportDateEnd);
        getLotAcceptReportPeriodGraphData(tblComponentLotAcceptanceReportList);
        tblComponentLotAcceptanceReportList.setCavList(tblComponentInspectionResultService.getLotAcceptanceReportCavNoList(incomingCompanyId, componentCode, periodFlag, sDate, eDate, cavityPrefix, cavityNum));
        return tblComponentLotAcceptanceReportList;
    }
    
    private List getLotAcceptanceReportAcceptedSql(String incomingCompanyId, String componentCode, String periodFlag,
            Date reportDateStart, Date reportDateEnd, String cavityPrefix, int cavityNum, boolean isAccepted) {

        StringBuilder sqlBuilder = new StringBuilder();
        if (null != periodFlag) {
            sqlBuilder.append(" SELECT ");
            switch (periodFlag) {
                case WEEK: // 期間種類が週別
                    sqlBuilder.append(" cir.incomingInspectionWeek, ");
                    break;
                case MONTH: // 期間種類が月別
                    sqlBuilder.append(" cir.incomingInspectionMonth, ");
                    break;
                default: // 期間種類が日別
                    sqlBuilder.append(" cir.incomingInspectionDate, ");
                    break;
            }
            sqlBuilder.append(" count(cir.id) ");
            sqlBuilder.append(" FROM TblComponentInspectionResult cir ");
            sqlBuilder.append(" JOIN FETCH cir.mstComponent component ");
            sqlBuilder.append(" JOIN FETCH cir.mstCompanyIncoming incomingCompany ");
            if (isAccepted) {
                sqlBuilder.append(" WHERE cir.incomingInspectionResult in (1, 3) ");
            } else {
                sqlBuilder.append(" WHERE cir.incomingInspectionResult = 2 ");
            }
            if (StringUtils.isNotEmpty(incomingCompanyId)) {
                sqlBuilder.append(" AND incomingCompany.id = :incomingCompanyId ");
            }
            if (StringUtils.isNotEmpty(componentCode)) {
                sqlBuilder.append(" AND component.componentCode like :componentCode ");
            }
            if (null != cavityPrefix) {
                sqlBuilder.append(" AND cir.cavityPrefix = :cavityPrefix");
            }
            if (0 != cavityNum) {
                sqlBuilder.append(" AND cir.id IN (select d.componentInspectionResultId FROM TblComponentInspectionResultDetail d Where d.cavityNum = :cavityNum)");
            }

            switch (periodFlag) {
                case WEEK: // 期間種類が週別
                    if (reportDateStart != null && reportDateEnd != null) {
                        sqlBuilder.append(" AND cir.incomingInspectionWeek between :reportDateStart and :reportDateEnd ");
                    }
                    sqlBuilder.append(" group by cir.incomingInspectionWeek ");
                    sqlBuilder.append(" order by cir.incomingInspectionWeek asc ");
                    break;
                case MONTH: // 期間種類が月別
                    if (reportDateStart != null && reportDateEnd != null) {
                        sqlBuilder.append(" AND cir.incomingInspectionMonth between :reportDateStart and :reportDateEnd ");
                    }
                    sqlBuilder.append(" group by cir.incomingInspectionMonth ");
                    sqlBuilder.append(" order by cir.incomingInspectionMonth asc ");
                    break;
                default: // 期間種類が日別
                    if (reportDateStart != null && reportDateEnd != null) {
                        sqlBuilder.append(" AND cir.incomingInspectionDate between :reportDateStart and :reportDateEnd ");
                    }
                    sqlBuilder.append(" group by FUNCTION('DATE_FORMAT', cir.incomingInspectionDate, '%Y/%m/%d') ");//FUNCTIONを用いて、entityManager.createNativeQuery()同様、ネイティブ関数をコールしています。
                    break;
            }
            
            Query query = entityManager.createQuery(sqlBuilder.toString());

            if (StringUtils.isNotEmpty(incomingCompanyId)) {
                query.setParameter("incomingCompanyId", incomingCompanyId);
            }

            if (StringUtils.isNotEmpty(componentCode)) {
                query.setParameter("componentCode", "%" + componentCode + "%");
            }
            
            if (null != cavityPrefix) {
                query.setParameter("cavityPrefix", cavityPrefix);
            }
            if (0 != cavityNum) {
                query.setParameter("cavityNum", cavityNum);
            }

            switch (periodFlag) {
                case MONTH: // 期間種類が月別
                    if (reportDateStart != null && reportDateEnd != null) {
                        String[] reportDateStartArray = DateFormat.dateToStrMonth(reportDateStart).split("/");
                        String[] reportDateEndArray = DateFormat.dateToStrMonth(reportDateEnd).split("/");
                        query.setParameter("reportDateStart", Integer.valueOf(reportDateStartArray[0] + reportDateStartArray[1]));
                        query.setParameter("reportDateEnd", Integer.valueOf(reportDateEndArray[0] + reportDateEndArray[1]));
                    }
                    break;
                case WEEK: // 期間種類が週別
                default: // 期間種類が日別
                    if (reportDateStart != null && reportDateEnd != null) {
                        query.setParameter("reportDateStart", reportDateStart);
                        query.setParameter("reportDateEnd", reportDateEnd);
                    }
                    break;
            }

            List list = query.getResultList();

            return list;
        } else {
            return new ArrayList();
        }
    }
    
    /**
     * リストをセット
     * 
     * @param acceptedList
     * @param rejectedList
     * @param periodFlag
     * @param productionDateStart
     * @param productionDateEnd
     * @return 
     */
    private TblComponentLotAcceptanceReportList getLotAcceptReportPeriodData(List acceptedList, List rejectedList, String periodFlag, String productionDateStart, String productionDateEnd) {
        // リターン用
        TblComponentLotAcceptanceReportList tblComponentLotAcceptanceReportList = new TblComponentLotAcceptanceReportList();

        List<Map<String, Map<String, String>>> resultList = new ArrayList();

        switch (periodFlag) {
            case WEEK: // 週別
                List<String> weekList = getDateList(periodFlag, productionDateStart, productionDateEnd);
                setLotAcceptReport(acceptedList, rejectedList, weekList, resultList, periodFlag);
                break;
            case MONTH: // 月別
                List<String> monthList = getDateList(periodFlag, productionDateStart, productionDateEnd);
                setLotAcceptReport(acceptedList, rejectedList, monthList, resultList, periodFlag);
                break;
            default: // 日別
                List<String> dayList = getDateList(periodFlag, productionDateStart, productionDateEnd);
                setLotAcceptReport(acceptedList, rejectedList, dayList, resultList, periodFlag);
                break;
        }

        tblComponentLotAcceptanceReportList.setResultListObj(resultList);

        return tblComponentLotAcceptanceReportList;

    }
    
    /**
     * 表示期間リストを作成
     * 
     * @param period
     * @param reportDateStart
     * @param reportDateEnd
     * @return 
     */
    private List<String> getDateList(String period, String reportDateStart, String reportDateEnd) {
        List<String> dateList = new ArrayList<>();
        switch(period) {
            case WEEK :
                // 週別期間表示リストの作成
                if (StringUtils.isNotEmpty(reportDateStart) && StringUtils.isNotEmpty(reportDateEnd)) {
                    CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
                    int firstDay = Integer.parseInt(cnf.getConfigValue());
                    
                    Date mondayStart = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(reportDateStart));
                    Date mondayEnd = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(reportDateEnd));
//                    Date sundayEnd = DateFormat.getAfterDays(mondayEnd, 6);

                    int weeks = DateFormat.daysBetween(mondayStart, mondayEnd) / 7;
                    for (int i = weeks; i >= 0; i--) {
                        String weekStart = "";
//                        String weekEnd = "";
                        weekStart = DateFormat.dateToStr(DateFormat.getBeforeDays(mondayEnd, i * 7), DateFormat.DATE_FORMAT);
//                        weekEnd = DateFormat.dateToStr(DateFormat.getBeforeDays(sundayEnd, i * 7), DateFormat.DATE_FORMAT);
//                        dateList.add(weekStart.concat(" - ").concat(weekEnd));
                        dateList.add(weekStart);
                    }
                }
                break;
            case MONTH :
                // 月別期間表示リストの作成
                if (StringUtils.isNotEmpty(reportDateStart) && StringUtils.isNotEmpty(reportDateEnd)) {
                    int months = DateFormat.getMonths(reportDateStart, reportDateEnd);

                    String tempDate;
                    for (int j = months; j >= 0; j--) {
                        tempDate = DateFormat.dateToStr(DateFormat.getBeforeMonths(DateFormat.strToDate(reportDateEnd), j), DateFormat.DATE_FORMAT);
                        dateList.add(tempDate.substring(0, 4) + tempDate.substring(5, 7));
                    }
                }
                break;
            default :
                // 日別期間表示リストの作成
                if (StringUtils.isNotEmpty(reportDateStart) && StringUtils.isNotEmpty(reportDateEnd)) {
                    int days = DateFormat.daysBetween(DateFormat.strToDate(reportDateStart), DateFormat.strToDate(reportDateEnd));

                    for (int k = days; k >= 0; k--) {
                        Date beforeDate = DateFormat.getBeforeDays(DateFormat.strToDate(reportDateEnd), k);
                        dateList.add(DateFormat.dateToStr(beforeDate, DateFormat.DATE_FORMAT));
                    }
                }
                break;
        }
        return dateList;
    }
    
    /**
     * 値をセット
     * 
     * @param tblMoldProductionPeriodVos
     * @param tblMoldProductionForDay
     * @param headList
     * @param resultDayMap
     * @param totalHeder
     * @return 
     */
    private void setLotAcceptReport(List acceptedList, List rejectedList, List<String> headList, List resultList, String periodFlag) {
        long accTotal = 0L;
        long rejTotal = 0L;
        List<Object[]> acceptArrList = null;
        List<Object[]> rejectArrList = null;
        if (acceptedList != null && !acceptedList.isEmpty()) {
            acceptArrList = (List<Object[]>)acceptedList;
        }
        if (rejectedList != null && !rejectedList.isEmpty()) {
            rejectArrList = (List<Object[]>)rejectedList;
        }
        for (String header : headList) {
            Map<String, Map<String, String>> headDataMap = new HashMap();
            Map<String, String> dataMap = new HashMap();
            int acc = 0;
            int rej = 0;
            if (acceptArrList != null && !acceptArrList.isEmpty()) {
                for (Object[] acceptArr : acceptArrList) {
                    if (DAY.equals(periodFlag)) {
                        if (header.equals(DateFormat.dateToStr((Date)acceptArr[0], DateFormat.DATE_FORMAT))) {
                            acc = Integer.valueOf(acceptArr[1].toString());
                            break;
                        }
                    } else if (WEEK.equals(periodFlag)) {
                        if (header.substring(0, 10).equals(DateFormat.dateToStr((Date)acceptArr[0], DateFormat.DATE_FORMAT))) {
                            acc = Integer.valueOf(acceptArr[1].toString());
                            break;
                        }
                    } else if (MONTH.equals(periodFlag)) {
                        if (header.equals(acceptArr[0] == null ? "" : acceptArr[0].toString())) {
                            acc = Integer.valueOf(acceptArr[1].toString());
                            break;
                        }
                    }
                }
            }
            if (rejectArrList != null && !rejectArrList.isEmpty()) {
                for (Object[] rejectArr : rejectArrList) {
                    if (DAY.equals(periodFlag)) {
                        if (header.equals(DateFormat.dateToStr((Date)rejectArr[0], DateFormat.DATE_FORMAT))) {
                            rej = Integer.valueOf(rejectArr[1].toString());
                            break;
                        }
                    } else if (WEEK.equals(periodFlag)) {
                        if (header.substring(0, 10).equals(DateFormat.dateToStr((Date)rejectArr[0], DateFormat.DATE_FORMAT))) {
                            rej = Integer.valueOf(rejectArr[1].toString());
                            break;
                        }
                    } else if (MONTH.equals(periodFlag)) {
                        if (header.equals(rejectArr[0] == null ? "" : rejectArr[0].toString())) {
                            rej = Integer.valueOf(rejectArr[1].toString());
                            break;
                        }
                    }
                }
            }
            accTotal += acc;
            rejTotal += rej;
            dataMap.put("acc", acc + "");
            dataMap.put("rej", rej + "");
            dataMap.put("totalLots", (acc + rej) + "");
            
            float accRate;
            if ((acc + rej) != 0) {
                // 合格率
                BigDecimal b = new BigDecimal((float)acc / (float)(acc + rej) * 100);
                accRate = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                dataMap.put("accRate", accRate + "");
                // 不合格率
                dataMap.put("rejRate", String.format("%.1f", 100 - accRate));
            } else {
                // 合格率
                dataMap.put("accRate", 0.0 + "");
                // 不合格率
                dataMap.put("rejRate", 0.0 + "");
            }
            
            if (DAY.equals(periodFlag)) {
                headDataMap.put(header.substring(8, 10), dataMap);
            } else if (WEEK.equals(periodFlag)) {
                headDataMap.put(header.substring(5, 10) + " - ", dataMap);
            } else if (MONTH.equals(periodFlag)) {
                headDataMap.put(header.substring(0, 4) + "/" + header.substring(4, 6), dataMap);
            }
            resultList.add(headDataMap);
        }
        // Total
        Map<String, Map<String, String>> headDataMap = new HashMap();
        Map<String, String> dataMap = new HashMap();
        dataMap.put("acc", accTotal + "");
        dataMap.put("rej", rejTotal + "");
        dataMap.put("totalLots", (accTotal + rejTotal) + "");
        
        float accRate;
        if ((accTotal + rejTotal) != 0) {
            // 合格率
            BigDecimal b = new BigDecimal((float)accTotal / (float)(accTotal + rejTotal) * 100);
            accRate = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            dataMap.put("accRate", accRate + "");
            // 不合格率
            dataMap.put("rejRate", String.format("%.1f", 100 - accRate));
        } else {
            // 合格率
            dataMap.put("accRate", 0.0 + "");
            // 不合格率
            dataMap.put("rejRate", 0.0 + "");
        }
        
        headDataMap.put("total", dataMap);
        resultList.add(headDataMap);
    }
    
    /**
     * ロット受入レポートグラフ設定
     *
     * @param tblComponentLotAcceptanceReportList
     */
    private void getLotAcceptReportPeriodGraphData(TblComponentLotAcceptanceReportList tblComponentLotAcceptanceReportList) {
        GraphicalItemInfo graphicalItemInfo = new GraphicalItemInfo();
        List<GraphicalAxis> xAxisList = new ArrayList<>();
        List<GraphicalAxis> yAxisList = new ArrayList<>();
        List<GraphicalData> dataList = new ArrayList<>();

        GraphicalAxis xAxis = new GraphicalAxis();
        GraphicalAxis yAxisLeft = new GraphicalAxis();
        GraphicalAxis yAxisRight = new GraphicalAxis();

        List<String> dataListX = new ArrayList<>();

        GraphicalData accData = new GraphicalData();
        GraphicalData rejData = new GraphicalData();
        GraphicalData accRateData = new GraphicalData();

        List<String> accDataList = new ArrayList<>(); // Lots acc
        List<String> rejDataList = new ArrayList<>(); // Lots rej
        List<String> accRateDataList = new ArrayList<>(); // Acc%
        long maxDataLeftY = 0L;
        long minDataLeftY = 0L;
        float maxDataRightY = 0F;
        float minDataRightY = 0F;

        List<Map<String, Map<String, String>>> resultList = tblComponentLotAcceptanceReportList.getResultListObj();
        if (resultList != null && resultList.size() > 0) {
            int index = 0;
            for (Map<String, Map<String, String>> map : resultList) {
                if (index == resultList.size() - 1) {
                    continue;
                }
                for (Entry<String, Map<String, String>> entry : map.entrySet()) {
                    dataListX.add(entry.getKey());
                    
                    long accLong = Long.parseLong(entry.getValue().get("acc"));
                    long rejLong = Long.parseLong(entry.getValue().get("rej"));
                    long tickData = accLong + rejLong;
                    
                    float accFloat = Float.parseFloat(entry.getValue().get("accRate"));
                    float rejFloat = Float.parseFloat(entry.getValue().get("rejRate"));
                    float tickRateData = accFloat >= rejFloat ? accFloat : rejFloat;
                    
                    if (tickData > maxDataLeftY) { //判断の最大値
                        maxDataLeftY = tickData;
                    }
                    if (tickData < minDataLeftY) { //判断の最小値
                        minDataLeftY = tickData;
                    }
                    
                    if (tickRateData > maxDataRightY) { //判断の最大値
                        maxDataRightY = tickRateData;
                    }
                    if (tickRateData < minDataRightY) { //判断の最小値
                        minDataRightY = tickRateData;
                    }
                    
                    accDataList.add(entry.getValue().get("acc"));
                    rejDataList.add(entry.getValue().get("rej"));
                    accRateDataList.add(entry.getValue().get("accRate"));
                }
                index++;
            }
        }

        // x軸表示データ
        xAxis.setTicks(dataListX.toArray(new String[0]));
        xAxisList.add(xAxis);

        // y軸表示データ最大値
        double num = (maxDataLeftY - minDataLeftY) * 0.1;
        double num2 = (maxDataRightY - minDataRightY) * 0.1;
        yAxisLeft.setMaxTicks(String.valueOf((maxDataLeftY + (num < 1 ? 1 : num))));//y
        yAxisLeft.setMinTicks(String.valueOf(minDataLeftY));
        yAxisRight.setMaxTicks(String.valueOf(maxDataRightY + num2));
        yAxisRight.setMinTicks(String.valueOf(minDataRightY));
        yAxisList.add(yAxisLeft);
        yAxisList.add(yAxisRight);

        // グラフデータの設定
        accData.setDataValue(accDataList.toArray(new String[0]));
        rejData.setDataValue(rejDataList.toArray(new String[0]));
        accRateData.setDataValue(accRateDataList.toArray(new String[0]));

        // グラフ表示タイプ
        accData.setGraphType("bar");
        rejData.setGraphType("bar");
        accRateData.setGraphType("line");

        // グラフデータ名称

        // y軸表示側
        accData.setYaxisFlg(2);
        rejData.setYaxisFlg(2);
        accRateData.setYaxisFlg(1);

        dataList.add(rejData);
        dataList.add(accData);
        dataList.add(accRateData);
        
        graphicalItemInfo.setxAxisList(xAxisList);
        graphicalItemInfo.setyAxisList(yAxisList);
        graphicalItemInfo.setDataList(dataList);

        tblComponentLotAcceptanceReportList.setGraphicalItemInfo(graphicalItemInfo);
    }
    
    /**
     * ロット受入レポートExcelデータ出力
     * 
     * @param incomingCompanyId
     * @param componentCode
     * @param periodFlag
     * @param reportDateStart
     * @param reportDateEnd
     * @param cavityPrefix
     * @param cavityNum
     * @param loginUser
     * @return 
     */
    public Response getLotAcceptanceReportDataExcel(String incomingCompanyId, String componentCode, String periodFlag, String reportDateStart, String reportDateEnd, String cavityPrefix, int cavityNum, LoginUser loginUser) {
        FileReponse fr = new FileReponse();
        WriteExcelList we = new WriteListExcel();
        Map<String, Object> param = new HashMap();
        String uuid = IDGenerator.generate();
        String outExclePath;
        try {
            outExclePath = FileUtil.outExcelFile(kartePropertyService, uuid);
        } catch (IOException ex) {
            Logger.getLogger(TblComponentLotAcceptanceReportService.class.getName()).log(Level.SEVERE, null, ex);
            Response.ResponseBuilder response = Response.status(500);
            return response.build();
        }
        param.put("outFilePath", outExclePath);
        param.put("workbook", new XSSFWorkbook());
        param.put("isConvertWorkbook", false);
        // ヘッダー
        Map<String, String> excelHeaderMap = getDictionaryList(loginUser.getLangId());
        try {
            /**
             * Header
             */
            List excelList = new ArrayList();

            TblComponentLotAcceptanceReportList componentLot = getLotAcceptanceReportDataList(incomingCompanyId, componentCode, periodFlag,
                    reportDateStart, reportDateEnd, cavityPrefix, cavityNum,loginUser);
            if (componentLot != null && componentLot.getResultListObj() != null && !componentLot.getResultListObj().isEmpty()) {
                // Exceｌ出力Headerを設定
                setExcelExportHeader(excelList, componentLot.getResultListObj(), excelHeaderMap, periodFlag);
                // Exceｌ出力データを設定
                setExcelExportData(excelList, componentLot.getResultListObj(), excelHeaderMap);
                we.write(param, excelList);
            } else {
                Response.ResponseBuilder response = Response.status(500);
                return response.build();
            }
            
            try {
                File file = new File(outExclePath);
                Response.ResponseBuilder response = Response.ok(file);
                StringBuilder fileName = new StringBuilder();
                fileName.append(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "lot_acceptance_report")).append(CommonConstants.EXT_EXCEL);
                String encodeStr = FileUtil.getEncod(fileName.toString());
                if (encodeStr != null) {
                    encodeStr = encodeStr.replace("+", "%20");
                }
                response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + encodeStr + "\"");
                return response.build();
            } catch (Exception e) {
                Logger.getLogger(TblComponentLotAcceptanceReportService.class.getName()).log(Level.SEVERE, null, e);
                Response.ResponseBuilder response = Response.status(500);
                return response.build();
            }
        } catch (IOException | IllegalArgumentException | IllegalAccessException e) {
            Logger.getLogger(TblComponentLotAcceptanceReportService.class.getName()).log(Level.SEVERE, null, e);
            Response.ResponseBuilder response = Response.status(500);
            return response.build();
        }
    }
    
    /**
     *
     * @param langId
     * @return
     */
    private Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("per_day", "per_week", "per_month", "total", "lots_acc", "lots_rej", "lots_total", "rate_acc", "rate_rej");

        Map<String, String> excelHeaderMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        return excelHeaderMap;
    }
    
    /**
     * Excel出力HEADERを設定
     *
     * @param list
     * @param excelHeaderMap
     */
    private void setExcelExportHeader(List list, List<Map<String, Map<String, String>>> headerList, Map<String, String> excelHeaderMap, String periodFlag) {
        
        try {
            Class clz = TblComponentLotAcceptanceReportExcel.class;
            Object obj = clz.newInstance();
            Method reportLabelMethod = clz.getMethod("setReportLabel", String.class);
            if (null != periodFlag) switch (periodFlag) {
                case DAY:
                    reportLabelMethod.invoke(obj, excelHeaderMap.get("per_day"));
                    break;
                case WEEK:
                    reportLabelMethod.invoke(obj, excelHeaderMap.get("per_week"));
                    break;
                case MONTH:
                    reportLabelMethod.invoke(obj, excelHeaderMap.get("per_month"));
                    break;
                default:
                    break;
            }
            
            for (int i = 0; i < headerList.size(); i++) {
                Method method = clz.getMethod("setDate" + (i + 1), String.class);
                if (i == headerList.size() - 1) {
                    method.invoke(obj, excelHeaderMap.get("total"));
                } else {
                    for (Entry<String, Map<String, String>> entry : headerList.get(i).entrySet()) {
                        method.invoke(obj, entry.getKey());
                    }
                }
            }
            
            list.add(obj);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(TblComponentLotAcceptanceReportService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Excel出力データを設定
     *
     * @param list
     * @param assetDisposalRequestList
     */
    private void setExcelExportData(List list, List<Map<String, Map<String, String>>> dataList, Map<String, String> excelHeaderMap) {
        
        try {
            Class clz = TblComponentLotAcceptanceReportExcel.class;
            for (int i = 0; i < 5; i++) {
                Object obj = clz.newInstance();
                Method reportLabelMethod = clz.getMethod("setReportLabel", String.class);
                if (i == 0) {
                    reportLabelMethod.invoke(obj, excelHeaderMap.get("lots_total"));
                    for (int j = 0; j < dataList.size(); j++) {
                        Method method = clz.getMethod("setDate" + (j + 1), String.class);
                        for (Entry<String, Map<String, String>> entry : dataList.get(j).entrySet()) {
                            method.invoke(obj, entry.getValue().get("totalLots"));
                        }
                    }
                }
                if (i == 1) {
                    reportLabelMethod.invoke(obj, excelHeaderMap.get("lots_acc"));
                    for (int j = 0; j < dataList.size(); j++) {
                        Method method = clz.getMethod("setDate" + (j + 1), String.class);
                        for (Entry<String, Map<String, String>> entry : dataList.get(j).entrySet()) {
                            method.invoke(obj, entry.getValue().get("acc"));
                        }
                    }
                }
                if (i == 2) {
                    reportLabelMethod.invoke(obj, excelHeaderMap.get("lots_rej"));
                    for (int j = 0; j < dataList.size(); j++) {
                        Method method = clz.getMethod("setDate" + (j + 1), String.class);
                        for (Entry<String, Map<String, String>> entry : dataList.get(j).entrySet()) {
                            method.invoke(obj, entry.getValue().get("rej"));
                        }
                    }
                }
                if (i == 3) {
                    reportLabelMethod.invoke(obj, excelHeaderMap.get("rate_acc"));
                    for (int j = 0; j < dataList.size(); j++) {
                        Method method = clz.getMethod("setDate" + (j + 1), String.class);
                        for (Entry<String, Map<String, String>> entry : dataList.get(j).entrySet()) {
                            method.invoke(obj, entry.getValue().get("accRate"));
                        }
                    }
                }
                if (i == 4) {
                    reportLabelMethod.invoke(obj, excelHeaderMap.get("rate_rej"));
                    for (int j = 0; j < dataList.size(); j++) {
                        Method method = clz.getMethod("setDate" + (j + 1), String.class);
                        for (Entry<String, Map<String, String>> entry : dataList.get(j).entrySet()) {
                            method.invoke(obj, entry.getValue().get("rejRate"));
                        }
                    }
                }
                list.add(obj);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(TblComponentLotAcceptanceReportService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
