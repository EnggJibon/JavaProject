/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.production;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.graphical.GraphicalAxis;
import com.kmcj.karte.graphical.GraphicalData;
import com.kmcj.karte.graphical.GraphicalItemInfo;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author lyd
 */
@Dependent
public class TblMachineProductionPeriodService {

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private CnfSystemService cnfSystemService;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    /**
     * 設備期間別生産実績テーブル条件検索(設備期間別生産実績明細行取得)
     *
     * @param machineId
     * @param machineName
     * @param formatMachineType
     * @param formatDepartment
     * @param componentCode
     * @param componentName
     * @param periodFlag
     * @param productionDateStart
     * @param loginUser
     * @return
     */
    public TblMachineProductionPeriodList getMachineProductionPeriodDataList(String machineId, String machineName, Integer formatMachineType, Integer formatDepartment, String componentCode, String componentName, String periodFlag, String productionDateStart, LoginUser loginUser) {
        List list = getSql(machineId, machineName, formatMachineType, formatDepartment, componentCode, componentName, periodFlag, productionDateStart);

        return getMachineProductionPeriodData(list, periodFlag, productionDateStart, "", loginUser);

    }

    /**
     * 設備期間別生産実績テーブル条件検索(前・翌日、前・翌週、前・翌月)(設備期間別生産実績明細行取得)
     *
     * @param paramList //param join
     * @param periodFlag
     * @param productionDateStart
     * @param tblMachineProductionPeriodList
     * @param loginUser
     * @return
     */
    public TblMachineProductionPeriodList getMachineProductionPeriodDataSearchList(List<String> paramList, String periodFlag, String productionDateStart,
            TblMachineProductionPeriodList tblMachineProductionPeriodList, LoginUser loginUser) {

        String productionDateEnd = "";
        if (tblMachineProductionPeriodList.getProductionDateEnd() != null) {
            productionDateEnd = tblMachineProductionPeriodList.getProductionDateEnd();
        }
        List list = getGraphSql(paramList, periodFlag, productionDateStart, productionDateEnd);
        TblMachineProductionPeriodList result = getMachineProductionPeriodData(list, periodFlag, productionDateStart, productionDateEnd, loginUser);

        List<TblMachineProductionPeriodVo> inputList = tblMachineProductionPeriodList.getTblMachineProductionPeriodVos();
        List<TblMachineProductionPeriodVo> returnList = new ArrayList<>();
        List<TblMachineProductionPeriodDetailVo> formatDetailVo = new ArrayList<>();
        List<String> resultDateList = getDateList(periodFlag, productionDateStart, productionDateEnd);

        for (String resultDate : resultDateList) {
            TblMachineProductionPeriodDetailVo vo = new TblMachineProductionPeriodDetailVo();
            vo.setCompletedCount(0);
            vo.setProductionDate(resultDate);
            formatDetailVo.add(vo);
        }
        // 検索結果をマップにセットする。
        Map<String, TblMachineProductionPeriodVo> resultVoMap = new HashMap<>();
        for (TblMachineProductionPeriodVo resultVo : result.getTblMachineProductionPeriodVos()) {
            String moldUuid;
            if (StringUtils.isEmpty(resultVo.getMoldUuid())) {
                moldUuid = "NULL";
            } else {
                moldUuid = resultVo.getMoldUuid();
            }
            resultVoMap.put(resultVo.getMachineUuid().concat(moldUuid).concat(resultVo.getComponentId()), resultVo);
        }

        //　入力リストを繰り返す、検索結果から入力リストの最新データを置換えする。
        if (inputList != null && inputList.size() > 0) {
            for (TblMachineProductionPeriodVo vo : inputList) {
                String keyStr = vo.getMachineUuid().concat(vo.getMoldUuid()).concat(vo.getComponentId());
                if (resultVoMap.containsKey(keyStr)) {
                    returnList.add(resultVoMap.get(keyStr));
                } else {
                    vo.setTblMachineProductionPeriodDetailVos(formatDetailVo);
                    vo.setTotal(0);
                    returnList.add(vo);
                }
            }
        }
        result.setTblMachineProductionPeriodVos(returnList);

        return result;
    }

    /**
     * 設備期間別生産実績データ出力
     *
     * @param tblMachineProductionPeriodList
     * @param loginUser
     * @param productionStartDate
     * @param headerStr
     * @return
     */
    public FileReponse postMachineProductionPeriodDataToCsv(TblMachineProductionPeriodList tblMachineProductionPeriodList, LoginUser loginUser, String productionStartDate, String headerStr) {
        FileReponse reponse = new FileReponse();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String langId = loginUser.getLangId();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("machine_id", "machine_name", "mold_id", "mold_name", "component_code",
                "component_name", "total", "machine_production_period", "machine_production_period_per_week",
                "period_day", "period_week", "period_month", "machine_production_period_per_day", "machine_production_period_per_month");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        /*Head*/
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        headList.add(headerMap.get("machine_id"));  // 設備ID
        headList.add(headerMap.get("machine_name")); // 設備名称
        headList.add(headerMap.get("mold_id"));  // 金型ID
        headList.add(headerMap.get("mold_name")); // 金型名称
        headList.add(headerMap.get("component_code")); // 部品コード
        headList.add(headerMap.get("component_name")); // 部品名称
        for (String headerDate : headerStr.split(",")) {
            headList.add(headerDate);
        }
        headList.add(headerMap.get("total")); // 合計
        gLineList.add(headList);

        ArrayList lineList;

        // 設備情報取得
        if (tblMachineProductionPeriodList != null && tblMachineProductionPeriodList.getTblMachineProductionPeriodVos() != null) {
            for (int i = 0; i < tblMachineProductionPeriodList.getTblMachineProductionPeriodVos().size(); i++) {
                lineList = new ArrayList();
                TblMachineProductionPeriodVo tblMachineProductionPeriodVo = tblMachineProductionPeriodList.getTblMachineProductionPeriodVos().get(i);
                if (tblMachineProductionPeriodVo.getMachineId() != null && !tblMachineProductionPeriodVo.getMachineId().isEmpty()) {
                    lineList.add(tblMachineProductionPeriodVo.getMachineId());
                } else {
                    lineList.add("");
                }
                if (tblMachineProductionPeriodVo.getMachineName() != null && !tblMachineProductionPeriodVo.getMachineName().isEmpty()) {
                    lineList.add(tblMachineProductionPeriodVo.getMachineName());
                } else {
                    lineList.add("");
                }
                if (tblMachineProductionPeriodVo.getMoldId() != null && !tblMachineProductionPeriodVo.getMoldId().isEmpty()) {
                    lineList.add(tblMachineProductionPeriodVo.getMoldId());
                } else {
                    lineList.add("");
                }
                if (tblMachineProductionPeriodVo.getMoldName() != null && !tblMachineProductionPeriodVo.getMoldName().isEmpty()) {
                    lineList.add(tblMachineProductionPeriodVo.getMoldName());
                } else {
                    lineList.add("");
                }
                if (tblMachineProductionPeriodVo.getComponentCode() != null && !tblMachineProductionPeriodVo.getComponentCode().isEmpty()) {
                    lineList.add(tblMachineProductionPeriodVo.getComponentCode());
                } else {
                    lineList.add("");
                }
                if (tblMachineProductionPeriodVo.getComponentName() != null && !tblMachineProductionPeriodVo.getComponentName().isEmpty()) {
                    lineList.add(tblMachineProductionPeriodVo.getComponentName());
                } else {
                    lineList.add("");
                }
                if (null != tblMachineProductionPeriodList.getPeriodFlag()) {
                    for (TblMachineProductionPeriodDetailVo vo : tblMachineProductionPeriodVo.getTblMachineProductionPeriodDetailVos()) {
                        lineList.add(String.valueOf(vo.getCompletedCount()));
                    }
                }
                lineList.add(String.valueOf(tblMachineProductionPeriodVo.getTotal()));
                gLineList.add(lineList);
            }

            // csv 出力
            CSVFileUtil.writeCsv(outCsvPath, gLineList);

            TblCsvExport tblCsvExport = new TblCsvExport();
            tblCsvExport.setFileUuid(uuid);
            tblCsvExport.setExportDate(new Date());
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_TBL_MACHINE_PRODUCTION_REFERENCE);
            // ファイル名称
            String fileName = "";
            if (null != tblMachineProductionPeriodList.getPeriodFlag()) {
                switch (tblMachineProductionPeriodList.getPeriodFlag()) {
                    case "1": // 週別をチェックされている場合、ファイル名は設備週別生産実績照会
                        fileName = headerMap.get("machine_production_period_per_week");
                        tblCsvExport.setExportTable(CommonConstants.TBL_MACHINE_PRODUCTION_PER_WEEK);
                        break;
                    case "2": // 月別をチェックされている場合、ファイル名は設備月別生産実績照会
                        fileName = headerMap.get("machine_production_period_per_month");
                        tblCsvExport.setExportTable(CommonConstants.TBL_MACHINE_PRODUCTION_PER_MONTH);
                        break;
                    default: // 日別をチェックされている場合、ファイル名は設備日別生産実績照会
                        fileName = headerMap.get("machine_production_period_per_day");
                        tblCsvExport.setExportTable(CommonConstants.TBL_MACHINE_PRODUCTION_PER_DAY);
                        break;
                }
            }
            tblCsvExport.setFunctionId(mstFunction);
            tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
            tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
            tblCsvExportService.createTblCsvExport(tblCsvExport);
            //csvファイルのUUIDをリターンする
            reponse.setFileUuid(uuid);

        }

        return reponse;
    }

    /**
     * 設備期間別生産実績データをグラフ用
     *
     * @param periodList
     * @param paramList
     * @param periodFlag
     * @param productionDateStart
     * @param productionDateEnd
     * @param loginUser
     * @return
     */
    public TblMachineProductionPeriodList getMachineProductionPeriodGraphDataList(TblMachineProductionPeriodList periodList,
            List<String> paramList, String periodFlag, String productionDateStart, String productionDateEnd, LoginUser loginUser) {
        GraphicalItemInfo graphicalItemInfo = new GraphicalItemInfo();
        GraphicalData graphicalData; // グラフ表示データ
        GraphicalAxis graphicalAxisY = new GraphicalAxis(); // X軸表示項目
        GraphicalAxis graphicalAxisX = new GraphicalAxis(); // Y軸表示項目
        List<GraphicalAxis> graphicalAxisListX = new ArrayList<>(); // X軸表示リスト 
        List<GraphicalAxis> graphicalAxisListY = new ArrayList<>(); // Y軸表示リスト
        List<GraphicalData> graphicalDataList = new ArrayList<>();  // グラフデータリスト
        TblMachineProductionPeriodList tblMachineProductionPeriodList = getMachineProductionPeriodDataSearchList(paramList, periodFlag, productionDateStart, periodList, loginUser);
        List<TblMachineProductionPeriodVo> tblMachineProductionPeriodVos = tblMachineProductionPeriodList.getTblMachineProductionPeriodVos();

        List<String> dateTicks = new ArrayList<>(); // X軸ー日時リスト
        List<String> dataValues;
        long maxTicks = 0;
        long minTicks = 0;
//        String maxDate = "";
//        String minDate = "";
        boolean axisxFlg = true;

        // 検索結果がnull以外の場合、リスト詳細より軸データとグラフデータを作り込む
        if (tblMachineProductionPeriodVos != null && tblMachineProductionPeriodVos.size() > 0) {
            TblMachineProductionPeriodVo pvo = tblMachineProductionPeriodVos.get(tblMachineProductionPeriodVos.size() - 1);
//            int detailVoSize = pvo.getTblMachineProductionPeriodDetailVos().size();
            // y軸表示期間（最小、最大）
//            minDate = tblMachineProductionPeriodVos.get(tblMachineProductionPeriodVos.size() - 1).getTblMachineProductionPeriodDetailVos().get(0).getProductionDate();
//            maxDate = tblMachineProductionPeriodVos.get(tblMachineProductionPeriodVos.size() - 1).getTblMachineProductionPeriodDetailVos().get(detailVoSize - 1).getProductionDate();

            // 設備期間生産実績テーブルリストを繰り返す
            for (TblMachineProductionPeriodVo vo : tblMachineProductionPeriodVos) {
                graphicalData = new GraphicalData();
                dataValues = new ArrayList<>(); // 日時リスト
                for (TblMachineProductionPeriodDetailVo detailVo : vo.getTblMachineProductionPeriodDetailVos()) {
                    String weekDate = detailVo.getProductionDate();
                    String weekValue = String.valueOf(detailVo.getCompletedCount());
                    if (maxTicks < detailVo.getCompletedCount()) {
                        maxTicks = detailVo.getCompletedCount();
                    }
                    if (minTicks > detailVo.getCompletedCount()) {
                        minTicks = detailVo.getCompletedCount();
                    }

                    // ｘ軸表示リストの作成は一回のみ
                    if (axisxFlg) {
                        dateTicks.add(weekDate);
                    }
                    dataValues.add(weekValue);
                }

                graphicalData.setDataName(vo.getMachineName().concat(vo.getComponentName())); //データ名
                graphicalData.setGraphType("line"); //グラフ表示タイプ
                graphicalData.setDataValue(dataValues.toArray(new String[0]));
                graphicalAxisX.setTicks(dateTicks.toArray(new String[0]));
                axisxFlg = false;

                // グラフ表示データをセット
                graphicalDataList.add(graphicalData);
            }
        }

        // Y軸データをセット
        graphicalAxisX.setMaxTicks(productionDateEnd);
        graphicalAxisX.setMinTicks(productionDateStart);
        graphicalAxisListX.add(graphicalAxisX);
        graphicalAxisY.setMaxTicks(String.valueOf(maxTicks));
        graphicalAxisY.setMinTicks(String.valueOf(minTicks));
        graphicalAxisListY.add(graphicalAxisY);

        graphicalItemInfo.setxAxisList(graphicalAxisListX);
        graphicalItemInfo.setyAxisList(graphicalAxisListY);
        graphicalItemInfo.setDataList(graphicalDataList);
        tblMachineProductionPeriodList.setGraphicalItemInfo(graphicalItemInfo);

        return tblMachineProductionPeriodList;
    }

    /**
     * 表示期間リストを作成
     *
     * @param period
     * @param productionDateStart
     * @param productionDateEnd
     * @return
     */
    public List<String> getDateList(String period, String productionDateStart, String productionDateEnd) {
        List<String> dateList = new ArrayList<>();
        switch (period) {
            case "1":
                CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
                int firstDay = Integer.parseInt(cnf.getConfigValue());
                
                if (productionDateEnd != null && !"".equals(productionDateEnd)) {
                    Date mondayStart = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateStart));
                    Date mondayEnd = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateEnd));
                    Date sundayEnd = DateFormat.getAfterDays(mondayEnd, 6);

                    int weeks = DateFormat.daysBetween(mondayStart, mondayEnd) / 7;
                    for (int i = weeks; i >= 0; i--) {
                        String weekStart = DateFormat.dateToStr(DateFormat.getBeforeDays(mondayEnd, i * 7), DateFormat.DATE_FORMAT);
                        String weekEnd = DateFormat.dateToStr(DateFormat.getBeforeDays(sundayEnd, i * 7), DateFormat.DATE_FORMAT);
                        dateList.add(weekStart.concat(" - ").concat(weekEnd));
                    }
                } else {
                    // 週別期間表示リストの作成
                    Date monday = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateStart));
                    Date sunday = DateFormat.getAfterDays(monday, 6);
                    dateList = DateFormat.getWeekList(monday, sunday);
                }
                break;
            case "2":
                // 月別期間表示リストの作成
                if (productionDateEnd != null && !"".equals(productionDateEnd)) {
                    int months = 0;
                    SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
                    Calendar cs = Calendar.getInstance();
                    Calendar ce = Calendar.getInstance();

                    try {
                        cs.setTime(sdf.parse(productionDateStart));
                        ce.setTime(sdf.parse(productionDateEnd));
                    } catch (ParseException ex) {
                        return null;
                    }
                    int yearStart = cs.get(Calendar.YEAR);
                    int yearEnd = ce.get(Calendar.YEAR);
                    int monthStart = cs.get(Calendar.MONTH);
                    int monthEnd = ce.get(Calendar.MONTH);

                    if (yearStart == yearEnd) {
                        months = monthEnd - monthStart;
                    } else {
                        months = 12 * (yearEnd - yearStart) + (monthEnd - monthStart);
                    }
                    for (int j = months; j >= 0; j--) {
                        dateList.add(DateFormat.dateToStr(DateFormat.getBeforeMonths(DateFormat.strToDate(productionDateEnd), j), DateFormat.DATE_FORMAT).substring(0, 7));
                    }
                } else {
                    dateList = DateFormat.getMonthList(DateFormat.strToDate(productionDateStart));
                }
                break;
            default:
                // 日別期間表示リストの作成
                if (productionDateEnd != null && !"".equals(productionDateEnd)) {
                    int days = DateFormat.daysBetween(DateFormat.strToDate(productionDateStart), DateFormat.strToDate(productionDateEnd));
                    for (int k = days; k >= 0; k--) {
                        Date beforeDate = DateFormat.getBeforeDays(DateFormat.strToDate(productionDateStart), k);
                        dateList.add(DateFormat.dateToStr(beforeDate, DateFormat.DATE_FORMAT));
                    }
                } else {
                    dateList = DateFormat.getDayList(DateFormat.strToDate(productionDateStart));
                }
        }
        return dateList;
    }

    /**
     * 設備期間別生産実績リストをセット
     *
     * @param list
     * @param periodFlag
     * @param productionDateStart
     * @param productionDateEnd
     * @param loginUser
     * @return
     */
    public TblMachineProductionPeriodList getMachineProductionPeriodData(List list, String periodFlag, String productionDateStart, String productionDateEnd, LoginUser loginUser) {
        TblMachineProductionForDay tblMachineProductionForDay;
        TblMachineProductionForWeek tblMachineProductionForWeek;
        TblMachineProductionForMonth tblMachineProductionForMonth;

        // リターン用
        TblMachineProductionPeriodList tblMachineProductionPeriodList = new TblMachineProductionPeriodList();
        List<TblMachineProductionPeriodVo> tblMachineProductionPeriodVos = new ArrayList<>();

        Map<String, List<TblMachineProductionPeriodVo>> resultWeekMap = new HashMap<>();
        Map<String, List<TblMachineProductionPeriodVo>> resultMonthMap = new HashMap<>();
        Map<String, List<TblMachineProductionPeriodVo>> resultDayMap = new HashMap<>();
        String totalHeader = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "total");

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (null != periodFlag) {
                    switch (periodFlag) {
                        case "1": // 週別をチェックされている場合、ファイル名は設備週別生産実績照会
                            
                            tblMachineProductionForWeek = (TblMachineProductionForWeek) list.get(i);
                            CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
                            int firstDay = Integer.parseInt(cnf.getConfigValue());
                            
                            List<String> weekList = new ArrayList<>();
                            if (productionDateEnd != null && !"".equals(productionDateEnd)) {
                                
                            Date mondayEnd = DateFormat.getFirstDayOfWeek(firstDay,
                                    DateFormat.strToDate(productionDateEnd));
                            Date mondayStart = DateFormat.getFirstDayOfWeek(firstDay,
                                    DateFormat.strToDate(productionDateStart));
                            String sundayStart = DateFormat.dateToStr(DateFormat.getAfterDays(mondayStart, 6),
                                    DateFormat.DATE_FORMAT);
                                
                                //Date mondayEnd = DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateEnd)));
                                //Date mondayStart = DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateStart)));
                                //String sundayStart = DateFormat.getWeekSunday(mondayStart);
                                for (int z = 0; z <= DateFormat.daysBetween(mondayStart, mondayEnd) / 7; z++) {
                                    String weekStart = DateFormat.dateToStr(DateFormat.getAfterDays(mondayStart, z * 7), DateFormat.DATE_FORMAT);
                                    String weekEnd = DateFormat.dateToStr(DateFormat.getAfterDays(DateFormat.strToDate(sundayStart), z * 7), DateFormat.DATE_FORMAT);
                                    weekList.add(weekStart.concat(" - ").concat(weekEnd));
                                }
                            } else {
                                // 週別期間表示リストの作成
                                Date monday = DateFormat.getFirstDayOfWeek(firstDay,
                                        DateFormat.strToDate(productionDateStart));
                                Date sunday = DateFormat.getAfterDays(monday, 6);
                                //Date monday = DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateStart)));
                                //Date sunday = DateFormat.getAfterDays(monday, 6);
                                weekList = DateFormat.getWeekList(monday, sunday);
                            }

                            tblMachineProductionPeriodVos = setMachineProductionForWeek(tblMachineProductionPeriodVos,
                                    tblMachineProductionForWeek, weekList, resultWeekMap, loginUser, totalHeader);

                            break;
                        case "2": // 月別をチェックされている場合、ファイル名は設備月別生産実績照会
                            tblMachineProductionForMonth = (TblMachineProductionForMonth) list.get(i);
                            List<String> monthList = new ArrayList<>();

                            // 月別期間表示リストの作成
                            if (productionDateEnd != null && !"".equals(productionDateEnd)) {
                                int months = 0;
                                SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
                                Calendar cs = Calendar.getInstance();
                                Calendar ce = Calendar.getInstance();

                                try {
                                    cs.setTime(sdf.parse(productionDateStart));
                                    ce.setTime(sdf.parse(productionDateEnd));
                                } catch (ParseException ex) {
                                    return null;
                                }
                                int yearStart = cs.get(Calendar.YEAR);
                                int yearEnd = ce.get(Calendar.YEAR);
                                int monthStart = cs.get(Calendar.MONTH);
                                int monthEnd = ce.get(Calendar.MONTH);

                                if (yearStart == yearEnd) {
                                    months = monthEnd - monthStart;
                                } else {
                                    months = 12 * (yearEnd - yearStart) + (monthEnd - monthStart);
                                }
                                for (int j = months; j >= 0; j--) {
                                    monthList.add(DateFormat.dateToStr(DateFormat.getBeforeMonths(DateFormat.strToDate(productionDateEnd), j), DateFormat.DATE_FORMAT).substring(0, 7));
                                }
                            } else {
                                monthList = DateFormat.getMonthList(DateFormat.strToDate(productionDateStart));
                            }

                            tblMachineProductionPeriodVos = setMachineProductionForMonth(tblMachineProductionPeriodVos,
                                    tblMachineProductionForMonth, monthList, resultMonthMap, loginUser, totalHeader);

                            break;
                        default: // 日別をチェックされている場合、ファイル名は設備日別生産実績照会
                            tblMachineProductionForDay = (TblMachineProductionForDay) list.get(i);
                            List<String> dayList = new ArrayList<>();

                            // 日別期間表示リストの作成
                            if (productionDateEnd != null && !"".equals(productionDateEnd)) {
                                int days = DateFormat.daysBetween(DateFormat.strToDate(productionDateStart), DateFormat.strToDate(productionDateEnd));
                                for (int k = days; k >= 0; k--) {
                                    Date beforeDate = DateFormat.getBeforeDays(DateFormat.strToDate(productionDateEnd), k);
                                    dayList.add(DateFormat.dateToStr(beforeDate, DateFormat.DATE_FORMAT));
                                }
                            } else {
                                dayList = DateFormat.getDayList(DateFormat.strToDate(productionDateStart));
                            }

                            tblMachineProductionPeriodVos = setMachineProductionForDay(tblMachineProductionPeriodVos,
                                    tblMachineProductionForDay, dayList, resultDayMap, loginUser, totalHeader);

                            break;
                    }
                }
            }
        }
        tblMachineProductionPeriodList.setTblMachineProductionPeriodVos(tblMachineProductionPeriodVos);

        tblMachineProductionPeriodList.setProductionDateStart(productionDateStart);

        return tblMachineProductionPeriodList;

    }

    private List getGraphSql(
            List<String> paramList,
            String periodFlag,
            String productionDateStart,
            String productionDateEnd
    ) {

        StringBuilder sql = new StringBuilder();
        if (null != periodFlag) {
            switch (periodFlag) {
                case "1": // 期間種類が週別であれば、設備週別完成数集計テーブルから取得
                    sql = sql.append(" SELECT ProductionFor FROM TblMachineProductionForWeek ProductionFor "
                            + " JOIN FETCH ProductionFor.mstMachine "
                            + " JOIN FETCH ProductionFor.mstComponent "
                            + " LEFT JOIN FETCH ProductionFor.mstMold ");

                    if (paramList != null && paramList.size() > 0) {
                        for (int i = 0; i < paramList.size(); i++) {
                            String[] paramStr = paramList.get(i).split(",");

                            if (i == 0) {
                                if (!StringUtils.isEmpty(paramStr[0])) {
                                    sql = sql.append(" WHERE (ProductionFor.tblMachineProductionForWeekPK.machineUuid =:machineUuid").append(i);
                                }

                                if (!StringUtils.isEmpty(paramStr[1])) {
                                    sql = sql.append(" AND ProductionFor.tblMachineProductionForWeekPK.componentId =:componentId").append(i);
                                }

                                if (!StringUtils.isEmpty(paramStr[2])) {
                                    sql = sql.append(" AND ProductionFor.tblMachineProductionForWeekPK.moldUuid =:moldUuid").append(i).append(") ");
                                }
                                sql = sql.append(" AND ProductionFor.tblMachineProductionForWeekPK.productionDateStart <= :DateStart "
                                        + "AND ProductionFor.tblMachineProductionForWeekPK.productionDateStart >= :DateEnd");
                            } else {
                                if (!StringUtils.isEmpty(paramStr[0])) {
                                    sql = sql.append(" OR (ProductionFor.tblMachineProductionForWeekPK.machineUuid =:machineUuid").append(i);
                                }

                                if (!StringUtils.isEmpty(paramStr[1])) {
                                    sql = sql.append(" AND ProductionFor.tblMachineProductionForWeekPK.componentId =:componentId").append(i);
                                }

                                if (!StringUtils.isEmpty(paramStr[2])) {
                                    sql = sql.append(" AND ProductionFor.tblMachineProductionForWeekPK.moldUuid =:moldUuid").append(i).append(") ");
                                }
                                sql = sql.append(" AND ProductionFor.tblMachineProductionForWeekPK.productionDateStart <= :DateStart "
                                        + "AND ProductionFor.tblMachineProductionForWeekPK.productionDateStart >= :DateEnd");
                            }
                        }
                    }

                    break;
                case "2": //期間種類が月別であれば、設備月別完成数集計テーブルから取得
                    sql = sql.append(" SELECT ProductionFor FROM TblMachineProductionForMonth ProductionFor "
                            + "JOIN FETCH ProductionFor.mstMachine "
                            + "JOIN FETCH ProductionFor.mstComponent "
                            + "LEFT JOIN FETCH ProductionFor.mstMold WHERE 1=1");
                    if (paramList != null && paramList.size() > 0) {
                        for (int i = 0; i < paramList.size(); i++) {
                            String[] paramStr = paramList.get(i).split(",");

                            if (i == 0) {
                                if (!StringUtils.isEmpty(paramStr[0])) {
                                    sql = sql.append(" AND (ProductionFor.tblMachineProductionForMonthPK.machineUuid =:machineUuid").append(i);
                                }

                                if (!StringUtils.isEmpty(paramStr[1])) {
                                    sql = sql.append(" AND ProductionFor.tblMachineProductionForMonthPK.componentId =:componentId").append(i);
                                }

                                if (!StringUtils.isEmpty(paramStr[2])) {
                                    sql = sql.append(" AND ProductionFor.tblMachineProductionForMonthPK.moldUuid =:moldUuid").append(i).append(") ");
                                }
                                sql = sql.append(" AND ProductionFor.tblMachineProductionForMonthPK.productionMonth >= :productionDateEnd "
                                        + "AND ProductionFor.tblMachineProductionForMonthPK.productionMonth <= :productionDateStart");
                            } else {
                                if (!StringUtils.isEmpty(paramStr[0])) {
                                    sql = sql.append(" OR (ProductionFor.tblMachineProductionForMonthPK.machineUuid =:machineUuid").append(i);
                                }

                                if (!StringUtils.isEmpty(paramStr[1])) {
                                    sql = sql.append(" AND ProductionFor.tblMachineProductionForMonthPK.componentId =:componentId").append(i);
                                }

                                if (!StringUtils.isEmpty(paramStr[2])) {
                                    sql = sql.append(" AND ProductionFor.tblMachineProductionForMonthPK.moldUuid =:moldUuid").append(i).append(") ");
                                }
                                sql = sql.append(" AND ProductionFor.tblMachineProductionForMonthPK.productionMonth >= :productionDateEnd "
                                        + "AND ProductionFor.tblMachineProductionForMonthPK.productionMonth <= :productionDateStart");
                            }
                        }
                    }

                    break;
                default: // 期間種類が日別であれば、設備日別完成数集計テーブルから取得
                    sql = sql.append(" SELECT ProductionFor FROM TblMachineProductionForDay ProductionFor "
                            + "JOIN FETCH ProductionFor.mstMachine "
                            + "JOIN FETCH ProductionFor.mstComponent "
                            + "LEFT JOIN FETCH ProductionFor.mstMold ");

                    if (paramList != null && paramList.size() > 0) {
                        for (int i = 0; i < paramList.size(); i++) {
                            String[] paramStr = paramList.get(i).split(",");

                            if (i == 0) {
                                if (!StringUtils.isEmpty(paramStr[0])) {
                                    sql = sql.append(" WHERE (ProductionFor.tblMachineProductionForDayPK.machineUuid =:machineUuid").append(i);
                                }

                                if (!StringUtils.isEmpty(paramStr[1])) {
                                    sql = sql.append(" AND ProductionFor.tblMachineProductionForDayPK.componentId =:componentId").append(i);
                                }

                                if (!StringUtils.isEmpty(paramStr[2])) {
                                    sql = sql.append(" AND ProductionFor.tblMachineProductionForDayPK.moldUuid =:moldUuid").append(i).append(") ");
                                }
                                sql = sql.append(" AND ProductionFor.tblMachineProductionForDayPK.productionDate <= :productionDateStart "
                                        + "AND ProductionFor.tblMachineProductionForDayPK.productionDate >= :productionDateEnd ");
                            } else {
                                if (!StringUtils.isEmpty(paramStr[0])) {
                                    sql = sql.append(" OR (ProductionFor.tblMachineProductionForDayPK.machineUuid =:machineUuid").append(i);
                                }

                                if (!StringUtils.isEmpty(paramStr[1])) {
                                    sql = sql.append(" AND ProductionFor.tblMachineProductionForDayPK.componentId =:componentId").append(i);
                                }

                                if (!StringUtils.isEmpty(paramStr[2])) {
                                    sql = sql.append(" AND ProductionFor.tblMachineProductionForDayPK.moldUuid =:moldUuid").append(i).append(") ");
                                }
                                sql = sql.append(" AND ProductionFor.tblMachineProductionForDayPK.productionDate <= :productionDateStart "
                                        + "AND ProductionFor.tblMachineProductionForDayPK.productionDate >= :productionDateEnd ");
                            }
                        }
                    }

                    break;
            }
        }

        sql = sql.append(" order by ProductionFor.mstMachine.machineId");

        Query query = entityManager.createQuery(sql.toString());
        
        CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
        int firstDay = Integer.parseInt(cnf.getConfigValue());
        
        if (null != periodFlag) {
            switch (periodFlag) {
                case "1"://期間種類が周別であれば                        
                    if (productionDateEnd != null && !"".equals(productionDateEnd)) {
                        
                        Date WeekStart = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateEnd));
                       // DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateEnd)));
                        Date WeekEnd = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateStart));
                                //DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateStart)));

                        query.setParameter("DateStart", WeekStart);
                        query.setParameter("DateEnd", WeekEnd);
                    } else {
                        Date WeekStart = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateStart));
                              // DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateStart)));
                        Date WeekEnd = DateFormat.getBeforeDays(WeekStart, 7 * 11);

                        query.setParameter("DateStart", WeekStart);
                        query.setParameter("DateEnd", WeekEnd);
                    }
                    break;
                case "2"://期間種類が月別であれば
                    if (productionDateEnd != null && !"".equals(productionDateEnd)) {
                        String monthStart = DateFormat.dateToStrMonth(DateFormat.strToDate(productionDateEnd));
                        String WeekEnd = DateFormat.dateToStrMonth(DateFormat.strToDate(productionDateStart));

                        query.setParameter("productionDateStart", monthStart);
                        query.setParameter("productionDateEnd", WeekEnd);
                    } else {

                        String monthStarts = DateFormat.dateToStrMonth(DateFormat.strToDate(productionDateStart));
                        String WeekEnds = DateFormat.dateToStrMonth(DateFormat.getBeforeMonths(DateFormat.strToDate(productionDateStart), 11));

                        query.setParameter("productionDateStart", monthStarts);
                        query.setParameter("productionDateEnd", WeekEnds);
                    }
                    break;
                default:  //期間種類が日別であれば
                    if (productionDateEnd != null && !"".equals(productionDateEnd)) {
                        Date dateStart = DateFormat.strToDate(productionDateEnd);
                        Date dateEnd = DateFormat.strToDate(productionDateStart);

                        query.setParameter("productionDateStart", dateStart);
                        query.setParameter("productionDateEnd", dateEnd);

                    } else {
                        Date dateStart = DateFormat.strToDate(productionDateStart);
                        Date dateEnd = DateFormat.getAfterDays(dateStart, -6);
                        query.setParameter("productionDateStart", dateStart);
                        query.setParameter("productionDateEnd", dateEnd);
                    }

                    break;
            }
        }
        if (paramList != null && paramList.size() > 0) {
            for (int i = 0; i < paramList.size(); i++) {
                String[] paramStr = paramList.get(i).split(",");
                if (!StringUtils.isEmpty(paramStr[0])) {
                    query.setParameter("machineUuid" + i, paramStr[0]);
                }
                if (!StringUtils.isEmpty(paramStr[1])) {
                    query.setParameter("componentId" + i, paramStr[1]);
                }
                if (!StringUtils.isEmpty(paramStr[2])) {
                    query.setParameter("moldUuid" + i, paramStr[2]);
                }
            }
        }

        List list = query.getResultList();

        return list;

    }

    private List getSql(
            String machineId,
            String machineName,
            Integer formatMachineType,
            Integer formatDepartment,
            String componentCode,
            String componentName,
            String periodFlag,
            String productionDateStart
    ) {

        StringBuilder sql = new StringBuilder();
        if (null != periodFlag) {
            switch (periodFlag) {
                case "1": // 期間種類が週別であれば、設備週別完成数集計テーブルから取得
                    sql = sql.append(" SELECT ProductionFor FROM TblMachineProductionForWeek ProductionFor "
                            + " JOIN FETCH ProductionFor.mstMachine ma "
                            + " JOIN FETCH ProductionFor.mstComponent co "
                            + " LEFT JOIN FETCH ProductionFor.mstMold "
                    );
                    if (null != productionDateStart && !"".equals(productionDateStart)) {
                        sql = sql.append(" WHERE ProductionFor.tblMachineProductionForWeekPK.productionDateStart <= :DateStart "
                                + "AND ProductionFor.tblMachineProductionForWeekPK.productionDateStart >= :DateEnd");
                    }

                    break;
                case "2": //期間種類が月別であれば、設備月別完成数集計テーブルから取得
                    sql = sql.append(" SELECT ProductionFor FROM TblMachineProductionForMonth ProductionFor "
                            + " JOIN FETCH ProductionFor.mstMachine ma "
                            + " JOIN FETCH ProductionFor.mstComponent co "
                            + " LEFT JOIN FETCH ProductionFor.mstMold "
                    );
                    if (null != productionDateStart && !"".equals(productionDateStart)) {
                        sql = sql.append(" WHERE ProductionFor.tblMachineProductionForMonthPK.productionMonth >= :productionDateEnd "
                                + "AND ProductionFor.tblMachineProductionForMonthPK.productionMonth <= :productionDateStart");
                    }

                    break;
                default: // 期間種類が日別であれば、設備日別完成数集計テーブルから取得
                    sql = sql.append(" SELECT ProductionFor FROM TblMachineProductionForDay ProductionFor "
                            + "JOIN FETCH ProductionFor.mstMachine ma "
                            + "JOIN FETCH ProductionFor.mstComponent co "
                            + "LEFT JOIN FETCH ProductionFor.mstMold "
                    );
                    if (null != productionDateStart && !"".equals(productionDateStart)) {
                        sql.append(" WHERE ProductionFor.tblMachineProductionForDayPK.productionDate <= :productionDateStart "
                                + "AND ProductionFor.tblMachineProductionForDayPK.productionDate >= :productionDateEnd ");
                    }
                    break;
            }
        }

        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" AND ma.machineId like :machineId ");
        }
        if (machineName != null && !"".equals(machineName)) {
            sql = sql.append(" AND ma.machineName like :machineName ");
        }
        if (formatMachineType != null && 0 < formatMachineType) {
            sql = sql.append(" AND ma.machineType = :formatMachineType ");
        }
        if (formatDepartment != null && 0 < formatDepartment) {
            sql = sql.append(" AND ma.department = :formatDepartment "); // 所属
        }
        if (componentCode != null && !"".equals(componentCode)) {
            sql = sql.append(" AND co.componentCode like :componentCode ");
        }
        if (componentName != null && !"".equals(componentName)) {
            sql = sql.append(" AND co.componentName like :componentName ");
        }

        sql.append(" order by ma.machineId ");//表示順は設備IDの昇順。

        Query query = entityManager.createQuery(sql.toString());
        CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
        int firstDay = Integer.parseInt(cnf.getConfigValue());
        if (productionDateStart != null && !"".equals(productionDateStart)) {
            if (null != periodFlag) {
                switch (periodFlag) {
                    case "1"://期間種類が周別であれば
                        Date WeekStart = DateFormat.getFirstDayOfWeek(firstDay, DateFormat.strToDate(productionDateStart));
                        //DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(productionDateStart)));
                        Date WeekEnd = DateFormat.getBeforeDays(WeekStart, 77);
                        query.setParameter("DateStart", WeekStart);
                        query.setParameter("DateEnd", WeekEnd);

                        break;
                    case "2"://期間種類が月別であれば
                        String monthStart = DateFormat.dateToStrMonth(DateFormat.strToDate(productionDateStart));
                        String monthEnd = DateFormat.dateToStrMonth(DateFormat.getBeforeMonths(DateFormat.strToDate(productionDateStart), 11));
                        query.setParameter("productionDateStart", monthStart);
                        query.setParameter("productionDateEnd", monthEnd);

                        break;
                    default:  //期間種類が日別であれば
                        Date dateStart = DateFormat.strToDate(productionDateStart);
                        Date dateEnd = DateFormat.getAfterDays(dateStart, -6);
                        query.setParameter("productionDateStart", dateStart);
                        query.setParameter("productionDateEnd", dateEnd);

                        break;
                }
            }
        }

        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", "%" + machineId + "%");
        }

        if (machineName != null && !"".equals(machineName)) {
            query.setParameter("machineName", "%" + machineName + "%");
        }
        if (formatMachineType != null && 0 < formatMachineType) {
            query.setParameter("formatMachineType", formatMachineType);
        }

        if (formatDepartment != null && 0 < formatDepartment) {
            query.setParameter("formatDepartment", formatDepartment);
        }
        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode + "%");
        }

        if (componentName != null && !"".equals(componentName)) {
            query.setParameter("componentName", "%" + componentName + "%");
        }

        List list = query.getResultList();

        return list;

    }

    /**
     * 日別値をセット
     *
     * @param tblMachineProductionForDay
     * @return
     */
    private List<TblMachineProductionPeriodVo> setMachineProductionForDay(List<TblMachineProductionPeriodVo> tblMachineProductionPeriodVos,
            TblMachineProductionForDay tblMachineProductionForDay, List<String> dayList, Map resultDayMap, LoginUser loginUser, String totalHeader) {
        TblMachineProductionPeriodVo tblMachineProductionPeriodVo = new TblMachineProductionPeriodVo();
        String machineUuid = FileUtil.getStr(tblMachineProductionForDay.getTblMachineProductionForDayPK().getMachineUuid());
        String componentId = FileUtil.getStr(tblMachineProductionForDay.getTblMachineProductionForDayPK().getComponentId());
        String productionDate = DateFormat.dateToStr(tblMachineProductionForDay.getTblMachineProductionForDayPK().getProductionDate(), DateFormat.DATE_FORMAT);
        String moldUuid;
        if (tblMachineProductionForDay.getMstMold() != null) {
            moldUuid = tblMachineProductionForDay.getMstMold().getUuid() == null ? "NULL" : tblMachineProductionForDay.getMstMold().getUuid();
        } else {
            moldUuid = "";
        }
        String key = machineUuid.concat(moldUuid).concat(componentId);

        if (resultDayMap.containsKey(key)) {

            tblMachineProductionPeriodVo = (TblMachineProductionPeriodVo) resultDayMap.get(key);
            for (TblMachineProductionPeriodDetailVo vo : tblMachineProductionPeriodVo.getTblMachineProductionPeriodDetailVos()) {
                if (vo.getProductionDate().equals(productionDate)) {
                    vo.setCompletedCount(tblMachineProductionForDay.getCompletedCount());
                    tblMachineProductionPeriodVo.setTotal(Long.sum(tblMachineProductionPeriodVo.getTotal(), vo.getCompletedCount()));
                }
            }
        } else {
            List<TblMachineProductionPeriodDetailVo> tblMachineProductionPeriodDetailVos = new ArrayList<>();
            //日別:設備マスタ
            if (tblMachineProductionForDay.getMstMachine() != null) {
                tblMachineProductionPeriodVo.setMachineUuid(tblMachineProductionForDay.getMstMachine().getUuid());
                tblMachineProductionPeriodVo.setMachineId(tblMachineProductionForDay.getMstMachine().getMachineId());
                tblMachineProductionPeriodVo.setMachineName(tblMachineProductionForDay.getMstMachine().getMachineName());
            } else {
                tblMachineProductionPeriodVo.setMachineUuid("");
                tblMachineProductionPeriodVo.setMachineId("");
                tblMachineProductionPeriodVo.setMachineName("");
            }

            // 日別:金型マスタ
            if (tblMachineProductionForDay.getMstMold() != null) {
                tblMachineProductionPeriodVo.setMoldUuid(tblMachineProductionForDay.getMstMold().getUuid());
                tblMachineProductionPeriodVo.setMoldId(tblMachineProductionForDay.getMstMold().getMoldId());
                tblMachineProductionPeriodVo.setMoldName(tblMachineProductionForDay.getMstMold().getMoldName());
            } else {
                tblMachineProductionPeriodVo.setMoldUuid("");
                tblMachineProductionPeriodVo.setMoldId("");
                tblMachineProductionPeriodVo.setMoldName("");
            }

            // 日別:部品マスタ
            if (tblMachineProductionForDay.getMstComponent() != null) {
                tblMachineProductionPeriodVo.setComponentId(tblMachineProductionForDay.getMstComponent().getId());
                tblMachineProductionPeriodVo.setComponentCode(tblMachineProductionForDay.getMstComponent().getComponentCode());
                tblMachineProductionPeriodVo.setComponentName(tblMachineProductionForDay.getMstComponent().getComponentName());
            } else {
                tblMachineProductionPeriodVo.setComponentId("");
                tblMachineProductionPeriodVo.setComponentCode("");
                tblMachineProductionPeriodVo.setComponentName("");
            }

            for (String date : dayList) {
                TblMachineProductionPeriodDetailVo MachineProductionPeriodDetailVo = new TblMachineProductionPeriodDetailVo();
                if (date.equals(DateFormat.dateToStr(
                        tblMachineProductionForDay.getTblMachineProductionForDayPK().getProductionDate(),
                        DateFormat.DATE_FORMAT))) {
                    MachineProductionPeriodDetailVo.setProductionDate(productionDate); // 生産日
                    MachineProductionPeriodDetailVo.setCompletedCount(tblMachineProductionForDay.getCompletedCount()); // 完成数
                } else {
                    MachineProductionPeriodDetailVo.setProductionDate(date); // 生産日
                    MachineProductionPeriodDetailVo.setCompletedCount(0); // 完成数
                }
                tblMachineProductionPeriodDetailVos.add(MachineProductionPeriodDetailVo);
            }

            tblMachineProductionPeriodVo.setTblMachineProductionPeriodDetailVos(tblMachineProductionPeriodDetailVos);
            tblMachineProductionPeriodVo.setTotal(tblMachineProductionForDay.getCompletedCount()); // 合計完成数
            tblMachineProductionPeriodVo.setTotalHeder(totalHeader);
            tblMachineProductionPeriodVos.add(tblMachineProductionPeriodVo);
        }
        resultDayMap.put(key, tblMachineProductionPeriodVo);
        return tblMachineProductionPeriodVos;
    }

    /**
     * 週別値をセット
     *
     * @param tblMachineProductionForWeek
     * @return
     */
    private List<TblMachineProductionPeriodVo> setMachineProductionForWeek(List<TblMachineProductionPeriodVo> tblMachineProductionPeriodVos,
            TblMachineProductionForWeek tblMachineProductionForWeek, List<String> weekList, Map resultWeekMap, LoginUser loginUser, String totalHeader) {

        TblMachineProductionPeriodVo tblMachineProductionPeriodVo = new TblMachineProductionPeriodVo();
        String componentId = FileUtil.getStr(tblMachineProductionForWeek.getTblMachineProductionForWeekPK().getComponentId());
        String machineUuid = FileUtil.getStr(tblMachineProductionForWeek.getTblMachineProductionForWeekPK().getMachineUuid());
        Date d1 = tblMachineProductionForWeek.getTblMachineProductionForWeekPK().getProductionDateStart();
        Date d2 = tblMachineProductionForWeek.getTblMachineProductionForWeekPK().getProductionDateEnd();
        String moldUuid;
        String weekDate = DateFormat.dateToStr(d1, DateFormat.DATE_FORMAT).concat(" - ").concat(DateFormat.dateToStr(d2, DateFormat.DATE_FORMAT));// 生産日
        if (tblMachineProductionForWeek.getMstMold() != null) {
            moldUuid = tblMachineProductionForWeek.getMstMold().getUuid() == null ? "NULL" : tblMachineProductionForWeek.getMstMold().getUuid();
        } else {
            moldUuid = "";
        }
        String key = machineUuid.concat(moldUuid).concat(componentId);
        if (resultWeekMap.containsKey(key)) {

            tblMachineProductionPeriodVo = (TblMachineProductionPeriodVo) resultWeekMap.get(key);
            for (TblMachineProductionPeriodDetailVo vo : tblMachineProductionPeriodVo.getTblMachineProductionPeriodDetailVos()) {
                if (vo.getProductionDate().equals(weekDate)) {
                    vo.setCompletedCount(tblMachineProductionForWeek.getCompletedCount());
                    tblMachineProductionPeriodVo.setTotal(Long.sum(tblMachineProductionPeriodVo.getTotal(), vo.getCompletedCount()));
                }
            }
        } else {
            if (tblMachineProductionForWeek.getMstMachine() != null) {
                tblMachineProductionPeriodVo.setMachineUuid(tblMachineProductionForWeek.getMstMachine().getUuid());
                tblMachineProductionPeriodVo.setMachineId(tblMachineProductionForWeek.getMstMachine().getMachineId());
                tblMachineProductionPeriodVo.setMachineName(tblMachineProductionForWeek.getMstMachine().getMachineName());
            } else {
                tblMachineProductionPeriodVo.setMachineUuid("");
                tblMachineProductionPeriodVo.setMachineId("");
                tblMachineProductionPeriodVo.setMachineName("");
            }

            // 週別:金型マスタ
            if (tblMachineProductionForWeek.getMstMold() != null) {
                tblMachineProductionPeriodVo.setMoldUuid(tblMachineProductionForWeek.getMstMold().getUuid());
                tblMachineProductionPeriodVo.setMoldId(tblMachineProductionForWeek.getMstMold().getMoldId());
                tblMachineProductionPeriodVo.setMoldName(tblMachineProductionForWeek.getMstMold().getMoldName());
            } else {
                tblMachineProductionPeriodVo.setMoldUuid("");
                tblMachineProductionPeriodVo.setMoldId("");
                tblMachineProductionPeriodVo.setMoldName("");
            }

            // 週別:部品マスタ
            if (tblMachineProductionForWeek.getMstComponent() != null) {
                tblMachineProductionPeriodVo.setComponentId(tblMachineProductionForWeek.getMstComponent().getId());
                tblMachineProductionPeriodVo.setComponentCode(tblMachineProductionForWeek.getMstComponent().getComponentCode());
                tblMachineProductionPeriodVo.setComponentName(tblMachineProductionForWeek.getMstComponent().getComponentName());
            } else {
                tblMachineProductionPeriodVo.setComponentId("");
                tblMachineProductionPeriodVo.setComponentCode("");
                tblMachineProductionPeriodVo.setComponentName("");
            }
            ArrayList<TblMachineProductionPeriodDetailVo> tblMachineProductionPeriodDetailVos = new ArrayList<>();
            for (String week : weekList) {
                TblMachineProductionPeriodDetailVo MachineProductionPeriodDetailVo = new TblMachineProductionPeriodDetailVo();
                if (week.equals(weekDate)) {
                    MachineProductionPeriodDetailVo.setProductionDate(weekDate); // 生産日
                    MachineProductionPeriodDetailVo.setCompletedCount(tblMachineProductionForWeek.getCompletedCount()); // 完成数
                } else {
                    MachineProductionPeriodDetailVo.setProductionDate(week); // 生産日
                    MachineProductionPeriodDetailVo.setCompletedCount(0); // 完成数
                }
                tblMachineProductionPeriodDetailVos.add(MachineProductionPeriodDetailVo);
            }
            tblMachineProductionPeriodVo.setTblMachineProductionPeriodDetailVos(tblMachineProductionPeriodDetailVos);
            tblMachineProductionPeriodVo.setTotal(tblMachineProductionForWeek.getCompletedCount()); // 合計完成数
            tblMachineProductionPeriodVo.setTotalHeder(totalHeader);
            tblMachineProductionPeriodVos.add(tblMachineProductionPeriodVo);
        }
        resultWeekMap.put(key, tblMachineProductionPeriodVo);

        return tblMachineProductionPeriodVos;
    }

    /**
     * 月別値をセット
     *
     * @param tblMachineProductionForDay
     * @return
     */
    private List<TblMachineProductionPeriodVo> setMachineProductionForMonth(List<TblMachineProductionPeriodVo> tblMachineProductionPeriodVos,
            TblMachineProductionForMonth tblMachineProductionForMonth, List<String> monthList, Map resultMonthMap, LoginUser loginUser, String totalHeader) {
        TblMachineProductionPeriodVo tblMachineProductionPeriodVo = new TblMachineProductionPeriodVo();

        String machineUuid = FileUtil.getStr(tblMachineProductionForMonth.getTblMachineProductionForMonthPK().getMachineUuid());
        String componentId = FileUtil.getStr(tblMachineProductionForMonth.getTblMachineProductionForMonthPK().getComponentId());
        String moldUuid;
        if (tblMachineProductionForMonth.getMstMold() != null) {
            moldUuid = tblMachineProductionForMonth.getMstMold().getUuid() == null ? "NULL" : tblMachineProductionForMonth.getMstMold().getUuid();
        } else {
            moldUuid = "";
        }
        String key = machineUuid.concat(moldUuid).concat(componentId);

        if (resultMonthMap.containsKey(key)) {

            tblMachineProductionPeriodVo = (TblMachineProductionPeriodVo) resultMonthMap.get(key);
            for (TblMachineProductionPeriodDetailVo vo : tblMachineProductionPeriodVo.getTblMachineProductionPeriodDetailVos()) {
                if (vo.getProductionDate().equals(tblMachineProductionForMonth.getTblMachineProductionForMonthPK().getProductionMonth())) {
                    vo.setCompletedCount(tblMachineProductionForMonth.getCompletedCount());
                    tblMachineProductionPeriodVo.setTotal(Long.sum(tblMachineProductionPeriodVo.getTotal(), vo.getCompletedCount()));
                }
            }
        } else {

            //月別:設備マスタ
            if (tblMachineProductionForMonth.getMstMachine() != null) {
                tblMachineProductionPeriodVo.setMachineUuid(tblMachineProductionForMonth.getMstMachine().getUuid());
                tblMachineProductionPeriodVo.setMachineId(tblMachineProductionForMonth.getMstMachine().getMachineId());
                tblMachineProductionPeriodVo.setMachineName(tblMachineProductionForMonth.getMstMachine().getMachineName());
            } else {
                tblMachineProductionPeriodVo.setMachineUuid("");
                tblMachineProductionPeriodVo.setMachineId("");
                tblMachineProductionPeriodVo.setMachineName("");
            }

            // 月別:金型マスタ
            if (tblMachineProductionForMonth.getMstMold() != null) {
                tblMachineProductionPeriodVo.setMoldUuid(tblMachineProductionForMonth.getMstMold().getUuid());
                tblMachineProductionPeriodVo.setMoldId(tblMachineProductionForMonth.getMstMold().getMoldId());
                tblMachineProductionPeriodVo.setMoldName(tblMachineProductionForMonth.getMstMold().getMoldName());
            } else {
                tblMachineProductionPeriodVo.setMoldUuid("");
                tblMachineProductionPeriodVo.setMoldId("");
                tblMachineProductionPeriodVo.setMoldName("");
            }

            // 月別:部品マスタ
            if (tblMachineProductionForMonth.getMstComponent() != null) {
                tblMachineProductionPeriodVo.setComponentId(tblMachineProductionForMonth.getMstComponent().getId());
                tblMachineProductionPeriodVo.setComponentCode(tblMachineProductionForMonth.getMstComponent().getComponentCode());
                tblMachineProductionPeriodVo.setComponentName(tblMachineProductionForMonth.getMstComponent().getComponentName());
            } else {
                tblMachineProductionPeriodVo.setComponentId("");
                tblMachineProductionPeriodVo.setComponentCode("");
                tblMachineProductionPeriodVo.setComponentName("");
            }

            ArrayList<TblMachineProductionPeriodDetailVo> tblMachineProductionPeriodDetailVos = new ArrayList<>();

            for (String month : monthList) {
                // 画面表示のため、未集計のレコードを作成する。
                TblMachineProductionPeriodDetailVo tblMachineProductionPeriodDetailVo = new TblMachineProductionPeriodDetailVo();
                if (month.equals(tblMachineProductionForMonth.getTblMachineProductionForMonthPK().getProductionMonth())) {
                    tblMachineProductionPeriodDetailVo.setProductionDate(tblMachineProductionForMonth.getTblMachineProductionForMonthPK().getProductionMonth()); // 生産月
                    tblMachineProductionPeriodDetailVo.setCompletedCount(tblMachineProductionForMonth.getCompletedCount()); // 完成数
                } else {
                    tblMachineProductionPeriodDetailVo.setProductionDate(month); // 生産日
                    tblMachineProductionPeriodDetailVo.setCompletedCount(0); // 完成数
                }
                tblMachineProductionPeriodDetailVos.add(tblMachineProductionPeriodDetailVo);
            }
            tblMachineProductionPeriodVo.setTblMachineProductionPeriodDetailVos(tblMachineProductionPeriodDetailVos);
            tblMachineProductionPeriodVo.setTotal(tblMachineProductionForMonth.getCompletedCount()); // 合計完成数
            tblMachineProductionPeriodVo.setTotalHeder(totalHeader);
            tblMachineProductionPeriodVos.add(tblMachineProductionPeriodVo);
        }
        resultMonthMap.put(key, tblMachineProductionPeriodVo);
        return tblMachineProductionPeriodVos;
    }

    /**
     * 機械日報詳細を削除する時、集計した完成数をマイナスする
     *
     * @param obj
     * @param completedCount
     * @param loginUser
     */
    public void minusCompletedCount(Object obj, long completedCount, LoginUser loginUser) {
        if (obj instanceof TblMachineProductionForDay) {
            TblMachineProductionForDay tblMachineProductionForDay = (TblMachineProductionForDay) obj;
            tblMachineProductionForDay.setCompletedCount(tblMachineProductionForDay.getCompletedCount() - completedCount);
            tblMachineProductionForDay.setUpdateDate(new Date());
            tblMachineProductionForDay.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(tblMachineProductionForDay);
        } else if (obj instanceof TblMachineProductionForWeek) {
            TblMachineProductionForWeek tblMachineProductionForWeek = (TblMachineProductionForWeek) obj;
            tblMachineProductionForWeek.setCompletedCount(tblMachineProductionForWeek.getCompletedCount() - completedCount);
            tblMachineProductionForWeek.setUpdateDate(new Date());
            tblMachineProductionForWeek.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(tblMachineProductionForWeek);
        } else if (obj instanceof TblMachineProductionForMonth) {
            TblMachineProductionForMonth tblMachineProductionForMonth = (TblMachineProductionForMonth) obj;
            tblMachineProductionForMonth.setCompletedCount(tblMachineProductionForMonth.getCompletedCount() - completedCount);
            tblMachineProductionForMonth.setUpdateDate(new Date());
            tblMachineProductionForMonth.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(tblMachineProductionForMonth);
        }
    }

    /**
     * 日別完成数集計情報1件取得
     *
     * @param machineUuid
     * @param componentId
     * @param moldUuid
     * @param productionDate
     * @return
     */
    public TblMachineProductionForDay getProductionForDaySingleByPK(String machineUuid, String componentId, String moldUuid, Date productionDate) {
        Query query = entityManager.createNamedQuery("TblMachineProductionForDay.findByPk");
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("componentId", componentId);
        if (StringUtils.isEmpty(moldUuid)) {
            query.setParameter("moldUuid", "NULL");
        } else {
            query.setParameter("moldUuid", moldUuid);
        }
        query.setParameter("productionDate", productionDate);
        try {
            return (TblMachineProductionForDay) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 週別完成数集計情報1件取得
     *
     * @param machineUuid
     * @param componentId
     * @param moldUuid
     * @param productionDateStart
     * @param productionDateEnd
     * @return
     */
    public TblMachineProductionForWeek getProductionForWeekSingleByPK(String machineUuid, String componentId, String moldUuid, Date productionDateStart, Date productionDateEnd) {
        Query query = entityManager.createNamedQuery("TblMachineProductionForWeek.findByPk");
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("componentId", componentId);
        if (StringUtils.isEmpty(moldUuid)) {
            query.setParameter("moldUuid", "NULL");
        } else {
            query.setParameter("moldUuid", moldUuid);
        }
        query.setParameter("productionDateStart", productionDateStart);
        query.setParameter("productionDateEnd", productionDateEnd);
        try {
            return (TblMachineProductionForWeek) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 月別完成数集計情報1件取得
     *
     * @param machineUuid
     * @param componentId
     * @param moldUuid
     * @param productionMonth
     * @return
     */
    public TblMachineProductionForMonth getProductionForMonthSingleByPK(String machineUuid, String componentId, String moldUuid, String productionMonth) {
        Query query = entityManager.createNamedQuery("TblMachineProductionForMonth.findByPk");
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("componentId", componentId);
        if (StringUtils.isEmpty(moldUuid)) {
            query.setParameter("moldUuid", "NULL");
        } else {
            query.setParameter("moldUuid", moldUuid);
        }
        query.setParameter("productionMonth", productionMonth);
        try {
            return (TblMachineProductionForMonth) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 設備期間別完成数集計テーブルに登録
     *
     * @param list
     * @param type(1:日別; 2:週別; 3:月別 )
     *
     * @return
     */
    @Transactional
    public int batchInsertByType(List<?> list, int type) {

        int insertCount = 0;

        int count = 0;

        for (int i = 1; i <= list.size(); i++) {

            switch (type) {

                case 1: {

                    entityManager.persist((TblMachineProductionForDay) list.get(i - 1));

                    break;
                }

                case 2: {

                    entityManager.persist((TblMachineProductionForWeek) list.get(i - 1));

                    break;
                }

                case 3: {

                    entityManager.persist((TblMachineProductionForMonth) list.get(i - 1));

                    break;
                }

                default:
                    // nothing
                    break;

            }

            entityManager.persist(list.get(i - 1));

            // 50件毎にDBへ登録する
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();

                insertCount += 50;
            }

            count = i;

        }

        insertCount += count % 50;

        return insertCount;
    }

    /**
     * 設備期間別完成数集計テーブルに更新
     *
     * @param list
     * @param type(1:日別; 2:週別; 3:月別 )
     *
     * @return
     */
    @Transactional
    public int batchUpdateByType(List<?> list, int type) {

        int updateCount = 0;

        int count = 0;

        for (int i = 1; i <= list.size(); i++) {

            switch (type) {

                case 1: {

                    entityManager.merge((TblMachineProductionForDay) list.get(i - 1));

                    break;
                }

                case 2: {

                    entityManager.merge((TblMachineProductionForWeek) list.get(i - 1));

                    break;
                }

                case 3: {

                    entityManager.merge((TblMachineProductionForMonth) list.get(i - 1));

                    break;
                }

                default:
                    // nothing
                    break;

            }

            // 50件毎にDBへ登録する
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();

                updateCount += 50;
            }

            count = i;

        }

        updateCount += count % 50;

        return updateCount;
    }

}
