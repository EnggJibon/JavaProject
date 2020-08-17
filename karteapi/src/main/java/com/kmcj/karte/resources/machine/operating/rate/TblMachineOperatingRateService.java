package com.kmcj.karte.resources.machine.operating.rate;

import com.kmcj.karte.FileReponse;
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
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
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
 * @author apeng
 */
@Dependent
public class TblMachineOperatingRateService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * 設備期間別稼働率テーブル条件検索(設備期間別稼働率明細行取得)
     *
     * @param machineId //設備ID
     * @param machineName //設備名称
     * @param machineType //設備種類
     * @param department //所属
     * @param periodFlag//集計方式のタイプ,0:日,1:周,2:月
     * @param formatOperatingDateStart//検索開始日
     * @param loginUser
     * @return
     */
    public TblMachineOperatingRatePeriodList getTblMachinePeriodList(
            String machineId,
            String machineName,
            Integer machineType,
            Integer department,
            String periodFlag,
            Date formatOperatingDateStart,
            LoginUser loginUser) {
        //apeng 2017.5.4
        TblMachineOperatingRatePeriodList tblMachineOperatingRatePeriodList = new TblMachineOperatingRatePeriodList();
        TblMachineOperatingRateForDay tblMachineOperatingRateForDay;
        TblMachineOperatingRateForWeek tblMachineOperatingRateForWeek;
        TblMachineOperatingRateForMonth tblMachineOperatingRateForMonth;
        List<TblMachineOperatingRatePeriodVos> tblMachineOperatingRatePeriodVoList = new ArrayList<>();
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("machine_working_rate", "producing_time_minutes");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, loginUser.getLangId(), dictKeyList);
        String operatingRateHeader = headerMap.get("machine_working_rate");
        String operatingTimeHeader = headerMap.get("producing_time_minutes");

        List<String> dateList;
        List<String> dateStrList;
        List list = getSql(machineId, machineName, machineType, department, periodFlag, formatOperatingDateStart);
        Map<String, List<TblMachineOperatingRatePeriodVos>> resultMap = new HashMap();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (null != periodFlag) {
                    switch (periodFlag) {
                        case "1": // 週別のデータの照会
                            tblMachineOperatingRateForWeek = (TblMachineOperatingRateForWeek) list.get(i);
                            Date monday = DateFormat.strToDate(DateFormat.getWeekMonday(DateFormat.strToDate(FileUtil.dateFormat(formatOperatingDateStart))));
                            Date sunday = DateFormat.getAfterDays(monday, 6);
                            dateStrList = DateFormat.getWeekList(monday, sunday);
                            tblMachineOperatingRatePeriodVoList = setTblMachineOperatingRateFor(tblMachineOperatingRatePeriodVoList,
                                    resultMap, tblMachineOperatingRateForWeek, dateStrList, operatingRateHeader, operatingTimeHeader);
                            break;
                        case "2": // 月別のデータの照会
                            tblMachineOperatingRateForMonth = (TblMachineOperatingRateForMonth) list.get(i);
                            dateStrList = DateFormat.getMonthList(formatOperatingDateStart);
                            tblMachineOperatingRatePeriodVoList = setTblMachineOperatingRateFor(tblMachineOperatingRatePeriodVoList,
                                    resultMap, tblMachineOperatingRateForMonth, dateStrList, operatingRateHeader, operatingTimeHeader);
                            break;
                        default: // 日別のデータの照会
                            //当面のIDが存在するかどうかの判断だ
                            tblMachineOperatingRateForDay = (TblMachineOperatingRateForDay) list.get(i);

                            dateList = DateFormat.getDayList(formatOperatingDateStart);
                            tblMachineOperatingRatePeriodVoList = setTblMachineOperatingRateFor(tblMachineOperatingRatePeriodVoList,
                                    resultMap, tblMachineOperatingRateForDay, dateList, operatingRateHeader, operatingTimeHeader);
                            break;
                    }
                }
            }
        }

        tblMachineOperatingRatePeriodList.setTblMachineOperatingRatePeriodVoList(tblMachineOperatingRatePeriodVoList);
        return tblMachineOperatingRatePeriodList;
    }

    /**
     * 設備期間別稼働率明細行取得 SQL
     *
     * @param machineId
     * @param machineName
     * @param machineType
     * @param department
     * @param formatOperatingDateStart//検索開始日
     * @param periodFlag
     * @return
     */
    private List getSql(
            String machineId,
            String machineName,
            Integer machineType,
            Integer department,
            String periodFlag,
            Date formatOperatingDateStart
    ) {

        StringBuilder sql;
        switch (periodFlag) {
            case "1": // 週別のデータの照会
                sql = new StringBuilder("SELECT operatingRate FROM TblMachineOperatingRateForWeek operatingRate "
                        + "JOIN FETCH operatingRate.mstMachine mstMachine  WHERE 1=1 ");

                sql.append(" AND operatingRate.tblMachineOperatingRateForWeekPK.productionDateStart BETWEEN :productionDateStart AND :productionDateEnd ");
                break;
            case "2": // 月別のデータの照会
                sql = new StringBuilder("SELECT operatingRate FROM TblMachineOperatingRateForMonth operatingRate "
                        + "JOIN FETCH operatingRate.mstMachine mstMachine  WHERE 1=1 ");

                sql.append(" AND operatingRate.tblMachineOperatingRateForMonthPK.productionMonth BETWEEN :productionDateStart AND :productionDateEnd ");
                break;
            default: // 日別のデータの照会
                sql = new StringBuilder("SELECT operatingRate FROM TblMachineOperatingRateForDay operatingRate "
                        + "JOIN FETCH operatingRate.mstMachine mstMachine  WHERE 1=1 ");

                sql.append(" AND operatingRate.tblMachineOperatingRateForDayPK.productionDate BETWEEN :productionDateStart AND :productionDateEnd ");
                break;
        }

        if (!StringUtils.isEmpty(machineId)) {
            sql = sql.append(" AND operatingRate.mstMachine.machineId like :machineId ");
        }

        if (!StringUtils.isEmpty(machineName)) {
            sql = sql.append(" AND operatingRate.mstMachine.machineName like :machineName ");
        }

        if (machineType != null && machineType > 0) {
            sql = sql.append(" and operatingRate.mstMachine.machineType = :machineType ");
        }

        if (department != null && department > 0) {
            sql = sql.append(" and operatingRate.mstMachine.department = :department ");
        }

        sql = sql.append("order by operatingRate.mstMachine.machineId");

        Query query = entityManager.createQuery(sql.toString());

        if (!StringUtils.isEmpty(machineId)) {
            query.setParameter("machineId", "%" + machineId + "%");
        }

        if (!StringUtils.isEmpty(machineName)) {
            query.setParameter("machineName", "%" + machineName + "%");
        }

        switch (periodFlag) {
            case "1"://期間種類が周別であれば
                Date weekStart = DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateStart));
                Date weekLast = DateFormat.getBeforeDays(DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateStart)), 77);
                query.setParameter("productionDateStart", weekLast);
                query.setParameter("productionDateEnd", weekStart);
                break;
            case "2"://期間種類が月別であれば
                Date monthLast = DateFormat.getBeforeMonths(formatOperatingDateStart, 11);
                query.setParameter("productionDateStart", FileUtil.dateFormatToMonth(monthLast));
                query.setParameter("productionDateEnd", FileUtil.dateFormatToMonth(formatOperatingDateStart));
                break;
            default:  //期間種類が日別であれば
                Date dateLast = DateFormat.getAfterDays(formatOperatingDateStart, -6);
                query.setParameter("productionDateStart", dateLast);
                query.setParameter("productionDateEnd", formatOperatingDateStart);
                break;
        }

        if (machineType != null && machineType > 0) {
            query.setParameter("machineType", machineType);
        }

        if (department != null && department > 0) {
            query.setParameter("department", department);
        }

        List list = query.getResultList();

        return list;

    }

    /**
     * 設備期間別稼働率照会 日,週,月別値をセット
     *
     * @param tblMachineOperatingRatePeriodVoList
     * @param resultMap
     * @param tblMachineOperatingRateFor
     * @param dateList
     * @param operatingRateHeader
     * @param operatingTimeHeader
     * @return
     */
    private List<TblMachineOperatingRatePeriodVos> setTblMachineOperatingRateFor(List<TblMachineOperatingRatePeriodVos> tblMachineOperatingRatePeriodVoList, Map resultMap,
            Object object, List<String> dateList, String operatingRateHeader, String operatingTimeHeader) {

        TblMachineOperatingRatePeriodVos tblMachineOperatingRatePeriodVo = new TblMachineOperatingRatePeriodVos();
        List<TblMachineOperatingRatePeriodVo> tblMachineOperatingRateProducintTimeVosList = new ArrayList();
        TblMachineOperatingRatePeriodVo tblMachineOperatingRateProducintTimeVos;

        String machineUuid = "";
        String machineId = "";
        String machineName = "";
        String machineDate = "";
        String timeStr = "";
        String key = "";

        if (object instanceof TblMachineOperatingRateForDay) {
            TblMachineOperatingRateForDay tblMachineOperatingRateFor = (TblMachineOperatingRateForDay) object;
            machineUuid = FileUtil.getStr(tblMachineOperatingRateFor.getTblMachineOperatingRateForDayPK().getMachineUuid());
            machineId = FileUtil.getStr(tblMachineOperatingRateFor.getMstMachine().getMachineId());
            machineName = FileUtil.getStr(tblMachineOperatingRateFor.getMstMachine().getMachineName());
            timeStr = FileUtil.formatSeconds(tblMachineOperatingRateFor.getOperatingTime());
            machineDate = FileUtil.dateFormat(tblMachineOperatingRateFor.getTblMachineOperatingRateForDayPK().getProductionDate());
            key = machineUuid.concat(machineId);

        } else if (object instanceof TblMachineOperatingRateForWeek) {
            TblMachineOperatingRateForWeek tblMachineOperatingRateFor = (TblMachineOperatingRateForWeek) object;
            machineUuid = FileUtil.getStr(tblMachineOperatingRateFor.getTblMachineOperatingRateForWeekPK().getMachineUuid());
            machineId = FileUtil.getStr(tblMachineOperatingRateFor.getMstMachine().getMachineId());
            machineName = FileUtil.getStr(tblMachineOperatingRateFor.getMstMachine().getMachineName());
            timeStr = FileUtil.formatSeconds(tblMachineOperatingRateFor.getOperatingTime());
            machineDate = FileUtil.dateFormat(tblMachineOperatingRateFor.getTblMachineOperatingRateForWeekPK().getProductionDateStart()) + " - " + FileUtil.dateFormat(tblMachineOperatingRateFor.getTblMachineOperatingRateForWeekPK().getProductionDateEnd());
            key = machineUuid.concat(machineId);

        } else if (object instanceof TblMachineOperatingRateForMonth) {
            TblMachineOperatingRateForMonth tblMachineOperatingRateFor = (TblMachineOperatingRateForMonth) object;
            machineUuid = FileUtil.getStr(tblMachineOperatingRateFor.getTblMachineOperatingRateForMonthPK().getMachineUuid());
            machineId = FileUtil.getStr(tblMachineOperatingRateFor.getMstMachine().getMachineId());
            machineName = FileUtil.getStr(tblMachineOperatingRateFor.getMstMachine().getMachineName());
            timeStr = FileUtil.formatSeconds(tblMachineOperatingRateFor.getOperatingTime());
            machineDate = tblMachineOperatingRateFor.getTblMachineOperatingRateForMonthPK().getProductionMonth();
            key = machineUuid.concat(machineId);
        }
        if (resultMap.containsKey(key)) {
            tblMachineOperatingRatePeriodVo = (TblMachineOperatingRatePeriodVos) resultMap.get(key);
            for (TblMachineOperatingRatePeriodVo vo : tblMachineOperatingRatePeriodVo.getTblMachineOperatingRateProducintTimeVos()) {
                if (vo.getProductionDateStr().equals(machineDate)) {
                    vo.setOperatingTimeStr(timeStr);
                }
            }
        } else {
            tblMachineOperatingRatePeriodVo.setMachineId(machineId);//設備ID
            tblMachineOperatingRatePeriodVo.setMachineName(machineName);//設備名称
            tblMachineOperatingRatePeriodVo.setMachineUuid(machineUuid);//設置UUID
            if (dateList != null && dateList.size() > 0) {
                for (String date : dateList) {
                    tblMachineOperatingRateProducintTimeVos = new TblMachineOperatingRatePeriodVo();
                    tblMachineOperatingRateProducintTimeVos.setOperatingRateHeder(operatingRateHeader);//稼動率を文言から取得
                    tblMachineOperatingRateProducintTimeVos.setOperatingTimeHeder(operatingTimeHeader);//稼動時間を文言から取得
                    if (date.equals(machineDate)) {
                        tblMachineOperatingRateProducintTimeVos.setProductionDateStr(machineDate);//生産日
                        tblMachineOperatingRateProducintTimeVos.setOperatingTimeStr(timeStr);//稼動時間
                    } else {
                        tblMachineOperatingRateProducintTimeVos.setProductionDateStr(date);//生産日
                        tblMachineOperatingRateProducintTimeVos.setOperatingTimeStr("00:00");//稼動時間
                    }
                    tblMachineOperatingRateProducintTimeVosList.add(tblMachineOperatingRateProducintTimeVos);
                }
            }
            tblMachineOperatingRatePeriodVo.setTblMachineOperatingRateProducintTimeVos(tblMachineOperatingRateProducintTimeVosList);
            tblMachineOperatingRatePeriodVoList.add(tblMachineOperatingRatePeriodVo);
        }
        resultMap.put(key, tblMachineOperatingRatePeriodVo);
        return tblMachineOperatingRatePeriodVoList;
    }

    /**
     * 設備期間別稼働率明細行取得グラフ作成
     *
     * @param machineUuids //設備ID
     * @param periodFlag//集計方式のタイプ,0:日,1:周,2:月
     * @param formatOperatingDateStart
     * @param formatOperatingDateEnd
     * @param loginUser
     * @param machineId
     * @param machineName
     * @param machineType
     * @param department
     * @return
     */
    public TblMachineOperatingRatePeriodList getMachineOperatingRateGraphDataList(List<String> machineUuids,
            String periodFlag,
            Date formatOperatingDateStart,
            Date formatOperatingDateEnd,
            LoginUser loginUser,
            String machineId,
            String machineName,
            Integer machineType,
            Integer department
    ) {

        GraphicalAxis graphicalAxisY = new GraphicalAxis();
        GraphicalAxis graphicalAxisX = new GraphicalAxis();
        List<GraphicalAxis> graphicalAxisListX = new ArrayList<>();
        List<GraphicalAxis> graphicalAxisListY = new ArrayList<>();
        List<GraphicalData> graphicalDataList = new ArrayList<>();

        TblMachineOperatingRatePeriodList tblMachineOperatingRatePeriodList = getMachineOperatingRateDataSearchList(machineUuids, formatOperatingDateStart, formatOperatingDateEnd,
                periodFlag, loginUser, "graph", machineId, machineName, machineType, department);
        List<String> dictKeyList = Arrays.asList("machine_operating_rate_per_day", "machine_operating_rate_per_week", "machine_operating_rate_per_month", "machine_working_rate",
                "tbl_machine_production_reference");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, loginUser.getLangId(), dictKeyList);

        List<TblMachineOperatingRatePeriodVos> tblMachineOperatingRatePeriodVos = tblMachineOperatingRatePeriodList.getTblMachineOperatingRatePeriodVoList();

        int maxTicks = 0;
        int minTicks = 0;
        String maxTicksStr = "";
        String minTicksStr = "";
        List<String> dataList;
        List<String> ticksList;
        if (tblMachineOperatingRatePeriodVos != null && tblMachineOperatingRatePeriodVos.size() > 0) {
            for (TblMachineOperatingRatePeriodVos vos : tblMachineOperatingRatePeriodVos) {
                GraphicalItemInfo graphicalItemInfo = new GraphicalItemInfo();
                GraphicalData graphicalData = new GraphicalData();
                dataList = new ArrayList();
                ticksList = new ArrayList();
                int tickStr;
                for (TblMachineOperatingRatePeriodVo vo : vos.getTblMachineOperatingRateProducintTimeVos()) {
                    dataList.add(vo.getProductionDateStr());
                    ticksList.add(vo.getOperatingTimeStr());
                    String[] tickStrs = vo.getOperatingTimeStr().split(":");
                    tickStr = Integer.parseInt(tickStrs[0]) * 60 + Integer.parseInt(tickStrs[1]);
                    if (tickStr > maxTicks) { //判断の最大値
                        maxTicks = tickStr;
                        maxTicksStr = vo.getOperatingTimeStr();
                    }
                    if (tickStr < minTicks) { //判断の最小値
                        minTicks = tickStr;
                        minTicksStr = vo.getOperatingTimeStr();
                    }
                }

                graphicalData.setDataValue(ticksList.toArray(new String[0]));

                if (periodFlag != null) {
                    switch (periodFlag) {
                        case "1"://期間種類が周別であれば
                            graphicalAxisX.setTitle(headerMap.get("machine_operating_rate_per_week"));//x
                            graphicalAxisY.setTitle(headerMap.get("machine_operating_rate_per_week"));//y
                            if (formatOperatingDateStart != null) {
                                graphicalAxisX.setMinTicks(DateFormat.dateToStr(formatOperatingDateStart, DateFormat.DATE_FORMAT));//x
                                graphicalAxisX.setMaxTicks(DateFormat.dateToStr(formatOperatingDateEnd, DateFormat.DATE_FORMAT));//x
                            } else {
                                Date weekLast = DateFormat.getBeforeDays(DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateEnd)), 77);
                                graphicalAxisX.setMinTicks(DateFormat.dateToStr(weekLast, DateFormat.DATE_FORMAT));//x
                                graphicalAxisX.setMaxTicks(DateFormat.dateToStr(formatOperatingDateEnd, DateFormat.DATE_FORMAT));//x
                            }
                            break;
                        case "2"://期間種類が月別であれば
                            graphicalAxisX.setTitle(headerMap.get("machine_operating_rate_per_month"));//x
                            graphicalAxisY.setTitle(headerMap.get("machine_operating_rate_per_month"));//y
                            if (formatOperatingDateStart != null) {
                                graphicalAxisX.setMinTicks(DateFormat.dateToStr(formatOperatingDateStart, DateFormat.DATE_FORMAT));//x
                                graphicalAxisX.setMaxTicks(DateFormat.dateToStr(formatOperatingDateEnd, DateFormat.DATE_FORMAT));//x
                            } else {
                                Date monthLast = DateFormat.getBeforeMonths(formatOperatingDateEnd, 11);
                                graphicalAxisX.setMinTicks(DateFormat.dateToStr(monthLast, DateFormat.DATE_FORMAT));//x
                                graphicalAxisX.setMaxTicks(DateFormat.dateToStr(formatOperatingDateEnd, DateFormat.DATE_FORMAT));//x
                            }
                            break;
                        default:  //期間種類が日別であれば
                            graphicalAxisY.setTitle(headerMap.get("machine_operating_rate_per_day"));//y
                            graphicalAxisX.setTitle(headerMap.get("machine_operating_rate_per_day"));//x
                            if (formatOperatingDateStart != null) {
                                graphicalAxisX.setMinTicks(DateFormat.dateToStr(formatOperatingDateStart, DateFormat.DATE_FORMAT));//x
                                graphicalAxisX.setMaxTicks(DateFormat.dateToStr(formatOperatingDateEnd, DateFormat.DATE_FORMAT));//x
                            } else {
                                Date dateLast = DateFormat.getAfterDays(formatOperatingDateEnd, -6);
                                graphicalAxisX.setMinTicks(DateFormat.dateToStr(dateLast, DateFormat.DATE_FORMAT));//x
                                graphicalAxisX.setMaxTicks(DateFormat.dateToStr(formatOperatingDateEnd, DateFormat.DATE_FORMAT));//x
                            }
                            break;
                    }
                }

                graphicalAxisX.setTicks(dataList.toArray(new String[0]));
                graphicalAxisListX.add(graphicalAxisX);//x

                graphicalAxisY.setMaxTicks(maxTicksStr);//y
                if (StringUtils.isEmpty(minTicksStr)) {
                    graphicalAxisY.setMinTicks("00:00");//y
                } else {
                    graphicalAxisY.setMinTicks(minTicksStr);//y
                }
                graphicalAxisListY.add(graphicalAxisY);//y

                graphicalData.setGraphType("line");
                graphicalData.setDataName(headerMap.get("machine_working_rate"));
                graphicalDataList.add(graphicalData);

                graphicalItemInfo.setOptionTitle(headerMap.get("tbl_machine_production_reference"));
                graphicalItemInfo.setxAxisList(graphicalAxisListX);
                graphicalItemInfo.setyAxisList(graphicalAxisListY);
                graphicalItemInfo.setDataList(graphicalDataList);
                tblMachineOperatingRatePeriodList.setGraphicalItemInfo(graphicalItemInfo);
            }
        }

        return tblMachineOperatingRatePeriodList;
    }

    /**
     * 設備期間別稼働率照会取得
     *
     * @param machineUuids
     * @param formatOperatingDateStart
     * @param formatOperatingDateEnd
     * @param periodFlag
     * @param loginUser
     * @param type
     * @param machineId
     * @param machineName
     * @param machineType
     * @param department
     * @return
     */
    public TblMachineOperatingRatePeriodList getMachineOperatingRateDataSearchList(List<String> machineUuids,
            Date formatOperatingDateStart,
            Date formatOperatingDateEnd,
            String periodFlag,
            LoginUser loginUser,
            String type,
            String machineId,
            String machineName,
            Integer machineType,
            Integer department
    ) {
        TblMachineOperatingRatePeriodList tblMachineOperatingRatePeriodList = new TblMachineOperatingRatePeriodList();
        List<MstMachine> setEmptyList = new ArrayList();
        if (machineUuids != null && machineUuids.size() > 0) {
            setEmptyList = getEmptyListSql(machineUuids, machineId, machineName, machineType, department);
        }
        List list = getSearchDataListSql(machineUuids, formatOperatingDateStart, formatOperatingDateEnd, periodFlag, type, machineId, machineName, machineType, department);

        TblMachineOperatingRateForDay tblMachineOperatingRateForDay;
        TblMachineOperatingRateForWeek tblMachineOperatingRateForWeek;
        TblMachineOperatingRateForMonth tblMachineOperatingRateForMonth;
        List<TblMachineOperatingRatePeriodVos> tblMachineOperatingRatePeriodVoList = new ArrayList<>();
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("machine_working_rate", "producing_time_minutes");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, loginUser.getLangId(), dictKeyList);
        String operatingRateHeader = headerMap.get("machine_working_rate");
        String operatingTimeHeader = headerMap.get("producing_time_minutes");

        Map<String, List<TblMachineOperatingRatePeriodVos>> resultMap = new HashMap();

        if (setEmptyList != null && setEmptyList.size() > 0) {
            for (int i = 0; i < setEmptyList.size(); i++) {
                if (periodFlag != null) {
                    switch (periodFlag) {
                        case "1": // 週別のデータの照会
                            List<String> dateWeekList;
                            if (type != null && "graph".equals(type)) {
                                dateWeekList = getDateList(periodFlag, formatOperatingDateStart, formatOperatingDateEnd);
                            } else {
                                Date monday = DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateStart));
                                Date sunday = DateFormat.getAfterDays(monday, 6);
                                dateWeekList = DateFormat.getWeekList(monday, sunday);
                            }
                            tblMachineOperatingRatePeriodVoList = setTblMachineOperatingRateForEmpty(tblMachineOperatingRatePeriodVoList,
                                    resultMap, setEmptyList.get(i), dateWeekList, operatingRateHeader, operatingTimeHeader);
                            break;
                        case "2": // 月別のデータの照会
                            List<String> dateMonthList;
                            if (type != null && "graph".equals(type)) {
                                dateMonthList = getDateList(periodFlag, formatOperatingDateStart, formatOperatingDateEnd);
                            } else {
                                dateMonthList = DateFormat.getMonthList(formatOperatingDateStart);
                            }
                            tblMachineOperatingRatePeriodVoList = setTblMachineOperatingRateForEmpty(tblMachineOperatingRatePeriodVoList,
                                    resultMap, setEmptyList.get(i), dateMonthList, operatingRateHeader, operatingTimeHeader);
                            break;
                        default: // 日別のデータの照会
                            //当面のIDが存在するかどうかの判断だ
                            List<String> dateList;
                            if (type != null && "graph".equals(type)) {
                                dateList = getDateList(periodFlag, formatOperatingDateStart, formatOperatingDateEnd);
                            } else {
                                dateList = DateFormat.getDayList(formatOperatingDateStart);
                            }

                            tblMachineOperatingRatePeriodVoList = setTblMachineOperatingRateForEmpty(tblMachineOperatingRatePeriodVoList,
                                    resultMap, setEmptyList.get(i), dateList, operatingRateHeader, operatingTimeHeader);
                            break;
                    }
                }
            }
        }

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (null != periodFlag) {
                    switch (periodFlag) {
                        case "1": // 週別のデータの照会
                            tblMachineOperatingRateForWeek = (TblMachineOperatingRateForWeek) list.get(i);
                            List<String> dateWeekList;
                            if (type != null && "graph".equals(type)) {
                                dateWeekList = getDateList(periodFlag, formatOperatingDateStart, formatOperatingDateEnd);
                            } else {
                                Date monday = DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateStart));
                                Date sunday = DateFormat.getAfterDays(monday, 6);
                                dateWeekList = DateFormat.getWeekList(monday, sunday);
                            }
                            tblMachineOperatingRatePeriodVoList = setTblMachineOperatingRateFor(tblMachineOperatingRatePeriodVoList,
                                    resultMap, tblMachineOperatingRateForWeek, dateWeekList, operatingRateHeader, operatingTimeHeader);
                            break;
                        case "2": // 月別のデータの照会
                            tblMachineOperatingRateForMonth = (TblMachineOperatingRateForMonth) list.get(i);
                            List<String> dateMonthList;
                            // 月別期間表示リストの作成
                            if (type != null && "graph".equals(type)) {
                                dateMonthList = getDateList(periodFlag, formatOperatingDateStart, formatOperatingDateEnd);
                            } else {
                                dateMonthList = DateFormat.getMonthList(formatOperatingDateStart);
                            }
                            tblMachineOperatingRatePeriodVoList = setTblMachineOperatingRateFor(tblMachineOperatingRatePeriodVoList,
                                    resultMap, tblMachineOperatingRateForMonth, dateMonthList, operatingRateHeader, operatingTimeHeader);
                            break;
                        default: // 日別のデータの照会
                            tblMachineOperatingRateForDay = (TblMachineOperatingRateForDay) list.get(i);
                            List<String> dateList;
                            if (type != null && "graph".equals(type)) {
                                dateList = getDateList(periodFlag, formatOperatingDateStart, formatOperatingDateEnd);
                            } else {
                                dateList = DateFormat.getDayList(formatOperatingDateStart);
                            }

                            tblMachineOperatingRatePeriodVoList = setTblMachineOperatingRateFor(tblMachineOperatingRatePeriodVoList,
                                    resultMap, tblMachineOperatingRateForDay, dateList, operatingRateHeader, operatingTimeHeader);
                            break;
                    }
                }
            }
        }

        tblMachineOperatingRatePeriodList.setTblMachineOperatingRatePeriodVoList(tblMachineOperatingRatePeriodVoList);
        return tblMachineOperatingRatePeriodList;
    }

    /**
     * 設備期間別稼働率照会取得 SQL
     *
     * @param machineUuids
     * @param formatOperatingDateStart
     * @param formatOperatingDateEnd
     * @param periodFlag
     * @param type
     * @return
     */
    private List getSearchDataListSql(
            List<String> machineUuids,
            Date formatOperatingDateStart,
            Date formatOperatingDateEnd,
            String periodFlag,
            String type,
            String machineId,
            String machineName,
            Integer machineType,
            Integer department
    ) {

        StringBuilder sql;
        switch (periodFlag) {
            case "1": // 週別のデータの照会
                sql = new StringBuilder("SELECT operatingRate FROM TblMachineOperatingRateForWeek operatingRate "
                        + "JOIN FETCH operatingRate.mstMachine mstMachine  WHERE 1=1 ");

                sql.append(" AND operatingRate.tblMachineOperatingRateForWeekPK.productionDateStart BETWEEN :productionDateStart AND :productionDateEnd ");
                break;
            case "2": // 月別のデータの照会
                sql = new StringBuilder("SELECT operatingRate FROM TblMachineOperatingRateForMonth operatingRate "
                        + "JOIN FETCH operatingRate.mstMachine mstMachine  WHERE 1=1 ");

                sql.append(" AND operatingRate.tblMachineOperatingRateForMonthPK.productionMonth BETWEEN :productionDateStart AND :productionDateEnd ");
                break;
            default: // 日別のデータの照会
                sql = new StringBuilder("SELECT operatingRate FROM TblMachineOperatingRateForDay operatingRate "
                        + "JOIN FETCH operatingRate.mstMachine mstMachine  WHERE 1=1 ");

                sql.append(" AND operatingRate.tblMachineOperatingRateForDayPK.productionDate BETWEEN :productionDateStart AND :productionDateEnd ");
                break;
        }

        if (machineUuids != null && machineUuids.size() > 0) {
            sql = sql.append(" AND operatingRate.mstMachine.uuid in :machineUuids ");
        }

        if (!StringUtils.isEmpty(machineId)) {
            sql = sql.append(" AND operatingRate.mstMachine.machineId like :machineId ");
        }

        if (!StringUtils.isEmpty(machineName)) {
            sql = sql.append(" AND operatingRate.mstMachine.machineName like :machineName ");
        }

        if (machineType != null && machineType > 0) {
            sql = sql.append(" and operatingRate.mstMachine.machineType = :machineType ");
        }

        if (department != null && department > 0) {
            sql = sql.append(" and operatingRate.mstMachine.department = :department ");
        }

        sql = sql.append("order by mstMachine.machineId");

        Query query = entityManager.createQuery(sql.toString());

        if (machineUuids != null && machineUuids.size() > 0) {
            query.setParameter("machineUuids", machineUuids);
        }

        if (!StringUtils.isEmpty(machineId)) {
            query.setParameter("machineId", "%" + machineId + "%");
        }

        if (!StringUtils.isEmpty(machineName)) {
            query.setParameter("machineName", "%" + machineName + "%");
        }

        if (machineType != null && machineType > 0) {
            query.setParameter("machineType", machineType);
        }

        if (department != null && department > 0) {
            query.setParameter("department", department);
        }

        switch (periodFlag) {
            case "1"://期間種類が周別であれば
                if (type != null && "graph".equals(type)) {
                    if (formatOperatingDateStart != null && formatOperatingDateEnd != null) {
                        Date weekStart = DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateEnd));
                        Date weekLast = DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateStart));

                        query.setParameter("productionDateStart", weekLast);
                        query.setParameter("productionDateEnd", weekStart);
                    } else {
                        Date weekStart = DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateEnd));
                        Date weekLast = DateFormat.getBeforeDays(DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateEnd)), 77);

                        query.setParameter("productionDateStart", weekLast);
                        query.setParameter("productionDateEnd", weekStart);
                    }
                } else {
                    Date weekStart = DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateStart));
                    Date weekLast = DateFormat.getBeforeDays(DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateStart)), 77);

                    query.setParameter("productionDateStart", weekLast);
                    query.setParameter("productionDateEnd", weekStart);

                }
                break;
            case "2"://期間種類が月別であれば
                if (type != null && "graph".equals(type)) {
                    if (formatOperatingDateStart != null && formatOperatingDateEnd != null) {
                        query.setParameter("productionDateStart", FileUtil.dateFormatToMonth(formatOperatingDateStart));
                        query.setParameter("productionDateEnd", FileUtil.dateFormatToMonth(formatOperatingDateEnd));
                    } else {
                        Date monthLast = DateFormat.getBeforeMonths(formatOperatingDateEnd, 11);
                        query.setParameter("productionDateStart", FileUtil.dateFormatToMonth(monthLast));
                        query.setParameter("productionDateEnd", FileUtil.dateFormatToMonth(formatOperatingDateEnd));
                    }
                } else {
                    Date monthLast = DateFormat.getBeforeMonths(formatOperatingDateStart, 11);
                    query.setParameter("productionDateStart", FileUtil.dateFormatToMonth(monthLast));
                    query.setParameter("productionDateEnd", FileUtil.dateFormatToMonth(formatOperatingDateStart));
                }
                break;
            default:  //期間種類が日別であれば
                if (type != null && "graph".equals(type)) {
                    if (formatOperatingDateStart != null && formatOperatingDateEnd != null) {
                        query.setParameter("productionDateStart", formatOperatingDateStart);
                        query.setParameter("productionDateEnd", formatOperatingDateEnd);
                    } else {
                        Date dateLast = DateFormat.getAfterDays(formatOperatingDateEnd, -6);
                        query.setParameter("productionDateStart", dateLast);
                        query.setParameter("productionDateEnd", formatOperatingDateEnd);
                    }
                } else {
                    Date dateLast = DateFormat.getAfterDays(formatOperatingDateStart, -6);
                    query.setParameter("productionDateStart", dateLast);
                    query.setParameter("productionDateEnd", formatOperatingDateStart);
                }
                break;
        }

        List list = query.getResultList();

        return list;

    }

    /**
     * 設備期間別生産実績データ出力
     *
     * @param tblMachineOperatingRatePeriodList
     * @param loginUser
     * @return
     */
    public FileReponse postMachineOperatingRateDataToCsv(TblMachineOperatingRatePeriodList tblMachineOperatingRatePeriodList, LoginUser loginUser) {
        FileReponse reponse = new FileReponse();
        //CSVファイル出力
        String fileUuid = IDGenerator.generate();
        String langId = loginUser.getLangId();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, fileUuid);
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("machine_id", "machine_name", "production_date", "production_week", "production_month", "machine_working_rate", "producing_time_minutes",
                "machine_operating_rate_per_day", "machine_operating_rate_per_week", "machine_operating_rate_per_month");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        /*Head*/
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        headList.add(headerMap.get("machine_id"));  // 設備ID
        headList.add(headerMap.get("machine_name")); // 設備名称
        String headerStr;
        if (tblMachineOperatingRatePeriodList != null && !StringUtils.isEmpty(tblMachineOperatingRatePeriodList.getHeaderStr())) {
            headerStr = tblMachineOperatingRatePeriodList.getHeaderStr();
            for (String headerDate : headerStr.split(",")) {
                if (tblMachineOperatingRatePeriodList.getPeriodFlag() != null) {
                    switch (tblMachineOperatingRatePeriodList.getPeriodFlag()) {
                        case "1"://期間種類周別
                            headList.add(headerMap.get("production_week"));
                            break;
                        case "2"://期間種類月別
                            headList.add(headerMap.get("production_month"));
                            break;
                        default:  //期間種類日別
                            headList.add(headerMap.get("production_date"));
                            break;
                    }
                } else {
                    headList.add(headerDate);
                }
                headList.add(headerMap.get("producing_time_minutes"));
                headList.add(headerMap.get("machine_working_rate"));
            }
        }
        gLineList.add(headList);

        ArrayList lineList;
        // 設備稼働履歴情報取得
        if (tblMachineOperatingRatePeriodList != null && tblMachineOperatingRatePeriodList.getTblMachineOperatingRatePeriodVoList() != null) {
            if (tblMachineOperatingRatePeriodList.getTblMachineOperatingRatePeriodVoList() != null && tblMachineOperatingRatePeriodList.getTblMachineOperatingRatePeriodVoList().size() > 0) {
                for (int i = 0; i < tblMachineOperatingRatePeriodList.getTblMachineOperatingRatePeriodVoList().size(); i++) {
                    lineList = new ArrayList();
                    TblMachineOperatingRatePeriodVos tblMachineOperatingRatePeriodVo = tblMachineOperatingRatePeriodList.getTblMachineOperatingRatePeriodVoList().get(i);

                    lineList.add(tblMachineOperatingRatePeriodVo.getMachineId());
                    lineList.add(tblMachineOperatingRatePeriodVo.getMachineName());
                    for (TblMachineOperatingRatePeriodVo vo : tblMachineOperatingRatePeriodVo.getTblMachineOperatingRateProducintTimeVos()) {
                        if (vo.getProductionDateStr() != null) {
                            lineList.add(vo.getProductionDateStr());
                        } else {
                            lineList.add("");
                        }
                        lineList.add(String.valueOf(vo.getOperatingTimeStr()));
                        lineList.add(String.valueOf(vo.getOperatingRate()));
                    }
                    gLineList.add(lineList);
                }
            }

            // csv 出力
            CSVFileUtil.writeCsv(outCsvPath, gLineList);

            TblCsvExport tblCsvExport = new TblCsvExport();
            tblCsvExport.setFileUuid(fileUuid);
            tblCsvExport.setExportDate(new Date());
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_TBL_MACHINE_OPERATING_RATE_REFERENCE);
            tblCsvExport.setFunctionId(mstFunction);
            tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
            // ファイル名称
            String fileName = "";
            if (null != tblMachineOperatingRatePeriodList.getPeriodFlag()) {
                switch (tblMachineOperatingRatePeriodList.getPeriodFlag()) {
                    case "1": // 週別をチェックされている場合、ファイル名は設備週別生産実績照会
                        fileName = headerMap.get("machine_operating_rate_per_week");
                        tblCsvExport.setExportTable(CommonConstants.TBL_MACHINE_OPERATING_RATE_PER_WEEK);
                        break;
                    case "2": // 月別をチェックされている場合、ファイル名は設備月別生産実績照会
                        fileName = headerMap.get("machine_operating_rate_per_month");
                        tblCsvExport.setExportTable(CommonConstants.TBL_MACHINE_OPERATING_RATE_PER_MONTH);
                        break;
                    default: // 日別をチェックされている場合、ファイル名は設備日別生産実績照会
                        fileName = headerMap.get("machine_operating_rate_per_day");
                        tblCsvExport.setExportTable(CommonConstants.TBL_MACHINE_OPERATING_RATE_PER_DAY);
                        break;
                }
            }
            tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
            tblCsvExportService.createTblCsvExport(tblCsvExport);
            //csvファイルのUUIDをリターンする
            reponse.setFileUuid(fileUuid);
        }

        return reponse;
    }

    /**
     * PKによる存在チェック（日別）
     *
     * @param machineUuid
     * @param productionDate
     * @return
     */
    public TblMachineOperatingRateForDay isForDayExsistByPK(String machineUuid, Date productionDate) {

        Query query = entityManager.createNamedQuery("TblMachineOperatingRateForDay.findByPk");
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("productionDate", productionDate);

        try {
            return (TblMachineOperatingRateForDay) query.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }

    }

    /**
     * PKによる存在チェック（週別）
     *
     * @param machineUuid
     * @param productionStart
     * @param productionEnd
     * @return
     */
    public TblMachineOperatingRateForWeek isForWeekExsistByPK(String machineUuid, Date productionStart, Date productionEnd) {

        Query query = entityManager.createNamedQuery("TblMachineOperatingRateForWeek.findByPk");
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("productionDateStart", productionStart);
        query.setParameter("productionDateEnd", productionEnd);

        try {
            return (TblMachineOperatingRateForWeek) query.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    /**
     * PKによる存在チェック（月別）
     *
     * @param machineUuid
     * @param productionMonth
     * @return
     */
    public TblMachineOperatingRateForMonth isForMonthExsistByPK(String machineUuid, String productionMonth) {

        Query query = entityManager.createNamedQuery("TblMachineOperatingRateForMonth.findByPk");
        query.setParameter("machineUuid", machineUuid);
        query.setParameter("productionMonth", productionMonth);

        try {
            return (TblMachineOperatingRateForMonth) query.getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    /**
     * 設備期間別稼動時間集計テーブルに登録
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

                    entityManager.persist((TblMachineOperatingRateForDay) list.get(i - 1));

                    break;
                }

                case 2: {

                    entityManager.persist((TblMachineOperatingRateForWeek) list.get(i - 1));

                    break;
                }

                case 3: {

                    entityManager.persist((TblMachineOperatingRateForMonth) list.get(i - 1));

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
     * 設備期間別稼動時間集計テーブルに更新
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

                    entityManager.merge((TblMachineOperatingRateForDay) list.get(i - 1));

                    break;
                }

                case 2: {

                    entityManager.merge((TblMachineOperatingRateForWeek) list.get(i - 1));

                    break;
                }

                case 3: {

                    entityManager.merge((TblMachineOperatingRateForMonth) list.get(i - 1));

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

    /**
     * set a null value SQL
     *
     * @param machineUuids
     * @return
     */
    private List getEmptyListSql(List<String> machineUuids,
            String machineId,
            String machineName,
            Integer machineType,
            Integer department
    ) {

        StringBuilder sql;
        sql = new StringBuilder("SELECT mstMachine FROM MstMachine mstMachine WHERE 1=1 ");

        if (machineUuids != null && machineUuids.size() > 0) {
            sql = sql.append(" AND mstMachine.uuid in :machineUuids ");
        }

        if (!StringUtils.isEmpty(machineId)) {
            sql = sql.append(" AND mstMachine.machineId like :machineId ");
        }

        if (!StringUtils.isEmpty(machineName)) {
            sql = sql.append(" AND mstMachine.machineName like :machineName ");
        }

        if (machineType != null && machineType > 0) {
            sql = sql.append(" and mstMachine.machineType = :machineType ");
        }

        if (department != null && department > 0) {
            sql = sql.append(" and mstMachine.department = :department ");
        }

        sql = sql.append("order by mstMachine.machineId");

        Query query = entityManager.createQuery(sql.toString());

        if (machineUuids != null && machineUuids.size() > 0) {
            query.setParameter("machineUuids", machineUuids);
        }

        if (!StringUtils.isEmpty(machineId)) {
            query.setParameter("machineId", "%" + machineId + "%");
        }

        if (!StringUtils.isEmpty(machineName)) {
            query.setParameter("machineName", "%" + machineName + "%");
        }

        if (machineType != null && machineType > 0) {
            query.setParameter("machineType", machineType);
        }

        if (department != null && department > 0) {
            query.setParameter("department", department);
        }

        List list = query.getResultList();

        return list;

    }

    /**
     * set a null value
     *
     * @param tblMachineOperatingRatePeriodVoList
     * @param resultMap
     * @param mstMachine
     * @param dateList
     * @param operatingRateHeader
     * @param operatingTimeHeader
     * @return
     */
    private List<TblMachineOperatingRatePeriodVos> setTblMachineOperatingRateForEmpty(List<TblMachineOperatingRatePeriodVos> tblMachineOperatingRatePeriodVoList, Map resultMap,
            MstMachine mstMachine, List<String> dateList, String operatingRateHeader, String operatingTimeHeader) {

        TblMachineOperatingRatePeriodVos tblMachineOperatingRatePeriodVo = new TblMachineOperatingRatePeriodVos();
        List<TblMachineOperatingRatePeriodVo> tblMachineOperatingRateProducintTimeVosList = new ArrayList();
        TblMachineOperatingRatePeriodVo tblMachineOperatingRateProducintTimeVos;

        String machineUuid = FileUtil.getStr(mstMachine.getUuid());
        String machineId = FileUtil.getStr(mstMachine.getMachineId());
        String key = machineUuid.concat(machineId);

        tblMachineOperatingRatePeriodVo.setMachineId(machineId);//設備ID
        tblMachineOperatingRatePeriodVo.setMachineName(mstMachine.getMachineName());//設備名称
        tblMachineOperatingRatePeriodVo.setMachineUuid(machineUuid);//設置UUID
        if (dateList != null && dateList.size() > 0) {
            for (String date : dateList) {
                tblMachineOperatingRateProducintTimeVos = new TblMachineOperatingRatePeriodVo();
                tblMachineOperatingRateProducintTimeVos.setOperatingRateHeder(operatingRateHeader);//稼動率を文言から取得
                tblMachineOperatingRateProducintTimeVos.setOperatingTimeHeder(operatingTimeHeader);//稼動時間を文言から取得
                tblMachineOperatingRateProducintTimeVos.setProductionDateStr(date);//生産日
                tblMachineOperatingRateProducintTimeVos.setOperatingTimeStr("00:00");//稼動時間
                tblMachineOperatingRateProducintTimeVosList.add(tblMachineOperatingRateProducintTimeVos);
            }
        }
        tblMachineOperatingRatePeriodVo.setTblMachineOperatingRateProducintTimeVos(tblMachineOperatingRateProducintTimeVosList);
        tblMachineOperatingRatePeriodVoList.add(tblMachineOperatingRatePeriodVo);

        resultMap.put(key, tblMachineOperatingRatePeriodVo);
        return tblMachineOperatingRatePeriodVoList;
    }

    /**
     * 表示期間リストを作成
     *
     * @param periodFlag
     * @param formatOperatingDateStart
     * @param formatOperatingDateEnd
     * @return
     */
    public List<String> getDateList(String periodFlag, Date formatOperatingDateStart, Date formatOperatingDateEnd) {
        List<String> dateList = new ArrayList<>();
        switch (periodFlag) {
            case "1":
                if (formatOperatingDateEnd != null && formatOperatingDateStart != null) {
                    Date mondayStart = DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateStart));
                    Date mondayEnd = DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateEnd));
                    Date sundayEnd = DateFormat.strToDate(DateFormat.getWeekSunday(formatOperatingDateEnd));
                    for (int i = DateFormat.daysBetween(mondayStart, mondayEnd) / 7; i >= 0; i--) {
                        String weekStart = DateFormat.dateToStr(DateFormat.getBeforeDays(mondayEnd, i * 7), DateFormat.DATE_FORMAT);
                        String weekEnd = DateFormat.dateToStr(DateFormat.getBeforeDays(sundayEnd, i * 7), DateFormat.DATE_FORMAT);
                        dateList.add(weekStart.concat(" - ").concat(weekEnd));
                    }
                } else {
                    Date monday = DateFormat.strToDate(DateFormat.getWeekMonday(formatOperatingDateEnd));
                    Date sunday = DateFormat.getAfterDays(monday, 6);
                    dateList = DateFormat.getWeekList(monday, sunday);
                }
                break;
            case "2":
                // 月別期間表示リストの作成
                if (formatOperatingDateEnd != null && formatOperatingDateStart != null) {
                    int months;
                    Calendar cs = Calendar.getInstance();
                    Calendar ce = Calendar.getInstance();

                    cs.setTime(formatOperatingDateStart);
                    ce.setTime(formatOperatingDateEnd);
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
                        dateList.add(DateFormat.dateToStr(DateFormat.getBeforeMonths(formatOperatingDateEnd, j), DateFormat.DATE_FORMAT).substring(0, 7));
                    }
                } else {
                    dateList = DateFormat.getMonthList(formatOperatingDateEnd);
                }
                break;
            default:
                // 日別期間表示リストの作成
                if (formatOperatingDateEnd != null && formatOperatingDateStart != null) {
                    int days = DateFormat.daysBetween(formatOperatingDateStart, formatOperatingDateEnd);
                    for (int k = days; k >= 0; k--) {
                        Date beforeDate = DateFormat.getBeforeDays(formatOperatingDateEnd, k);
                        dateList.add(DateFormat.dateToStr(beforeDate, DateFormat.DATE_FORMAT));
                    }
                } else {
                    dateList = DateFormat.getDayList(formatOperatingDateEnd);
                }
                break;
        }
        return dateList;
    }

}
