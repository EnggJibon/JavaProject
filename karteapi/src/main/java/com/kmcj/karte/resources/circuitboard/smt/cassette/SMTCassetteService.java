package com.kmcj.karte.resources.circuitboard.smt.cassette;

import com.kmcj.karte.resources.circuitboard.smt.SMTGraphList;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.graphical.GraphicalAxis;
import com.kmcj.karte.graphical.GraphicalData;
import com.kmcj.karte.graphical.GraphicalItemInfo;
import com.kmcj.karte.resources.circuitboard.automaticmachine.chartdef.choice.MstAutomaticMachineChartChoiceDef;
import com.kmcj.karte.resources.circuitboard.automaticmachine.chartdef.choice.MstAutomaticMachineChartChoiceDefService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.Pager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;

/**
 * Created by xiaozhou.wang on 2017/08/08.
 */
@Dependent
public class SMTCassetteService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstAutomaticMachineChartChoiceDefService mstAutomaticMachineChartChoiceDefService;
    
    private static final String SORT_ASC = "asc";
    private final static Map<String, String> orderKey;
    private final static List<String> sortKey;
    static {
        orderKey = new HashMap<>();
        orderKey.put("productionLineName", " ORDER BY mstProductionLine.productionLineName ");//生産ライン名
        orderKey.put("machineNo", " ORDER BY mstMachine.machineId ");//設備ID
        orderKey.put("machineName", " ORDER BY mstMachine.machineName ");//設備名称
        orderKey.put("componentName", " ORDER BY t.txtcol7 ");//部品名称
        orderKey.put("adhesionNumber", " ORDER BY t.numcol1 ");//吸着回数 
        orderKey.put("adhesionErrorNumber", " ORDER BY t.numcol3 ");//吸着エラー回数
        orderKey.put("leavingAdhesionErrorNumber", " ORDER BY t.numcol4 ");//立ち吸着エラー回数
        orderKey.put("componentRecognitionErrorNumber1", " ORDER BY t.numcol5 ");//部品認識エラー回数1
        orderKey.put("componentRecognitionErrorNumber2", " ORDER BY t.numcol6 ");//部品認識エラー回数2
        orderKey.put("componentRecognitionErrorNumber3", " ORDER BY t.numcol7 ");//部品認識エラー回数3
    }
    static {
        // エラー合計、エラー率、吸着エラー率、立ち吸着エラー率、認識エラー率1、認識エラー率2、認識エラー率3
        sortKey = new ArrayList<String>(){{
            add("totalErrors");
            add("errorRate");
            add("adhesionErrorRate");
            add("leavingAdhesionErrorRate");
            add("recognitionErrorRate1");
            add("recognitionErrorRate2");
            add("recognitionErrorRate3");
        }};
    }
    /**
     * 実装機ログ取得SQL
     *
     * @param lineNo
     * @param isCount
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    private List getTblautomaticMachineLog(
            int lineNo,
            boolean isCount,
            String sidx,//order Key
            String sord,//order 順
            int pageNumber,
            int pageSize) {
        StringBuilder sql = new StringBuilder(" SELECT ");
        if (isCount) {
            sql.append(" 1 ");
        } else {
            sql.append(" t.lineNumber, t.txtcol2, t.txtcol7, SUM(t.numcol1),");
            sql.append(" SUM(t.numcol3), SUM(t.numcol4), SUM(t.numcol5),");
            sql.append(" SUM(t.numcol6), SUM(t.numcol7),");
            sql.append(" mstProductionLine.productionLineName, mstMachine.machineName");
        }
        sql.append(" FROM TblAutomaticMachineLog t "
                + " JOIN FETCH t.mstMachine mstMachine "
                + " LEFT JOIN MstProductionLine mstProductionLine ON t.lineNumber = mstProductionLine.productionLineId"
                + " WHERE 1=1 "
                + " AND t.machineType = :machineType "
                + " AND t.logType = :logType ");

        // 生産ラインNo
        if (lineNo != 0) {
            sql.append(" AND t.lineNumber = :lineNo ");
        }
        
        sql.append(" GROUP BY t.lineNumber, t.txtcol2, t.txtcol7 ");
        
        if (!isCount) {
            if (StringUtils.isNotEmpty(sidx)) {
                if (orderKey.get(sidx) != null) { 
                    sql.append(orderKey.get(sidx));
                    if (StringUtils.isNotEmpty(sord)) {
                        sql.append(sord);
                    }
                }
            }
        }
        
        Query query = entityManager.createQuery(sql.toString());

        // パラメータ
        query.setParameter("machineType", CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT);
        query.setParameter("logType", CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT_C);

        if (lineNo != 0) {
            query.setParameter("lineNo", lineNo);
        }
        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

//    /**
//     * 生産ラインデータを取得
//     *
//     * @param productionLineIdList
//     * @return
//     */
//    private Map<String, MstProductionLine> getProductionLineMap(Set<String> productionLineIdList) {
//        Map<String, MstProductionLine> map = new HashMap<>();
//
//        Query query = entityManager.createNamedQuery("MstProductionLine.findByProductionLineIdList");
//        query.setParameter("productionLineIdList", productionLineIdList);
//
//        List<MstProductionLine> mstProductionLineList = (List<MstProductionLine>) query.getResultList();
//        for (MstProductionLine productionLine : mstProductionLineList) {
//            map.put(productionLine.getProductionLineId(), productionLine);
//        }
//
//        return map;
//    }

    /**
     * カセット別実装機情報を取得
     *
     * @param lineNo
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public SMTGraphList getSearchResult(
            int lineNo,
            String sidx,//order Key
            String sord,//order 順
            int pageNumber,
            int pageSize
    ) {
        SMTGraphList smtGraphList = new SMTGraphList();
        List<SMTCassetteVo> resultList = new ArrayList<>();
        BigDecimal zore = new BigDecimal(BigInteger.ZERO);
        List counts = getTblautomaticMachineLog(lineNo, true,null,null, pageNumber, pageSize);
        // ページをめぐる
        Pager pager = new Pager();
        smtGraphList.setPageNumber(pageNumber);
        Long count =  (long)counts.size();
        smtGraphList.setCount(count);
        smtGraphList.setPageTotal(pager.getTotalPage(pageSize,Integer.parseInt(count+"")));
        // 実装機ログデータを抽出
        List list = getTblautomaticMachineLog(lineNo,false,sidx, sord, pageNumber,pageSize);
        if (list != null && list.size() > 0) {

            Iterator iterator = list.iterator();

            BigDecimal rate = new BigDecimal(100);
            // ログリストを繰り返し、ビューオブジェクトに値を設定する。
            while (iterator.hasNext()) {
                Object[] tblAutomaticMachineLog = (Object[]) iterator.next();
                SMTCassetteVo vo = new SMTCassetteVo();
                vo.setProductionLineName(tblAutomaticMachineLog[9] != null ? String.valueOf(tblAutomaticMachineLog[9]) : ""); // 生産ライン名
                vo.setProductionLineNo(tblAutomaticMachineLog[0] != null ? String.valueOf(tblAutomaticMachineLog[0]) : ""); // 生産ライン
                vo.setMachineNo(tblAutomaticMachineLog[1] == null ? "" : String.valueOf(tblAutomaticMachineLog[1])); // 設備No
                vo.setMachineName(tblAutomaticMachineLog[10] == null ? "" : String.valueOf(tblAutomaticMachineLog[10])); // 設備名称
                vo.setComponentName(tblAutomaticMachineLog[2] == null ? "" : String.valueOf(tblAutomaticMachineLog[2])); // 部品名称
                vo.setAdhesionNumber(tblAutomaticMachineLog[3] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[3]))); // 吸着回数 
                vo.setAdhesionErrorNumber(tblAutomaticMachineLog[4] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[4]))); // 吸着エラー回数
                vo.setLeavingAdhesionErrorNumber(tblAutomaticMachineLog[5] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[5]))); // 立ち吸着エラー回数
                vo.setComponentRecognitionErrorNumber1(tblAutomaticMachineLog[6] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[6]))); // 部品認識エラー回数1
                vo.setComponentRecognitionErrorNumber2(tblAutomaticMachineLog[7] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[7]))); // 部品認識エラー回数2
                vo.setComponentRecognitionErrorNumber3(tblAutomaticMachineLog[8] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[8]))); // 部品認識エラー回数3
                BigDecimal totalError = (vo.getAdhesionErrorNumber())
                        .add(vo.getLeavingAdhesionErrorNumber())
                        .add(vo.getComponentRecognitionErrorNumber1())
                        .add(vo.getComponentRecognitionErrorNumber2())
                        .add(vo.getComponentRecognitionErrorNumber3());
                vo.setTotalErrors(totalError); // エラー合計
                if (vo.getAdhesionNumber().compareTo(zore) == 1) {
                    vo.setErrorRate(totalError
                            .divide(vo.getAdhesionNumber(), 4, BigDecimal.ROUND_DOWN).multiply(rate)); // エラー率
                    vo.setAdhesionErrorRate(vo.getAdhesionErrorNumber()
                            .divide(vo.getAdhesionNumber(), 4, BigDecimal.ROUND_DOWN).multiply(rate)); // 吸着エラー率
                    vo.setLeavingAdhesionErrorRate(vo.getLeavingAdhesionErrorNumber()
                            .divide(vo.getAdhesionNumber(), 4, BigDecimal.ROUND_DOWN).multiply(rate)); // 立ち吸着エラー率
                    vo.setRecognitionErrorRate1(vo.getComponentRecognitionErrorNumber1()
                            .divide(vo.getAdhesionNumber(), 4, BigDecimal.ROUND_DOWN).multiply(rate)); // 認識エラー1率
                    vo.setRecognitionErrorRate2(vo.getComponentRecognitionErrorNumber2()
                            .divide(vo.getAdhesionNumber(), 4, BigDecimal.ROUND_DOWN).multiply(rate)); // 認識エラー2率
                    vo.setRecognitionErrorRate3(vo.getComponentRecognitionErrorNumber3()
                            .divide(vo.getAdhesionNumber(), 4, BigDecimal.ROUND_DOWN).multiply(rate)); // 認識エラー3率
                } else {
                    vo.setErrorRate(zore); // エラー率
                    vo.setAdhesionErrorRate(zore); // 吸着エラー率
                    vo.setLeavingAdhesionErrorRate(zore); // 立ち吸着エラー率
                    vo.setRecognitionErrorRate1(zore); // 認識エラー1率
                    vo.setRecognitionErrorRate2(zore); // 認識エラー2率
                    vo.setRecognitionErrorRate3(zore); // 認識エラー3率
                }

                vo.setAutoMachine(null); // 自動機
                vo.setGraph(null); // グラフ
                resultList.add(vo);
            }
            
            if (sortKey.contains(sidx)) {
                Collections.sort(resultList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        SMTCassetteVo obj1 = (SMTCassetteVo) o1;
                        SMTCassetteVo obj2 = (SMTCassetteVo) o2;

                        if (sidx.equals(sortKey.get(0))) {
                            if (SORT_ASC.equalsIgnoreCase(sord)) {
                                return obj1.getTotalErrors().compareTo(obj2.getTotalErrors());
                            } else {
                                return obj2.getTotalErrors().compareTo(obj1.getTotalErrors());
                            }
                        } else if (sidx.equals(sortKey.get(1))) {
                            if (SORT_ASC.equalsIgnoreCase(sord)) {
                                return obj1.getErrorRate().compareTo(obj2.getErrorRate());
                            } else {
                                return obj2.getErrorRate().compareTo(obj1.getErrorRate());
                            }
                        } else if (sidx.equals(sortKey.get(2))) {
                            if (SORT_ASC.equalsIgnoreCase(sord)) {
                                return obj1.getAdhesionErrorRate().compareTo(obj2.getAdhesionErrorRate());
                            } else {
                                return obj2.getAdhesionErrorRate().compareTo(obj1.getAdhesionErrorRate());
                            }
                        } else if (sidx.equals(sortKey.get(3))) {
                            if (SORT_ASC.equalsIgnoreCase(sord)) {
                                return obj1.getLeavingAdhesionErrorRate().compareTo(obj2.getLeavingAdhesionErrorRate());
                            } else {
                                return obj2.getLeavingAdhesionErrorRate().compareTo(obj1.getLeavingAdhesionErrorRate());
                            }
                        } else if (sidx.equals(sortKey.get(4))) {
                            if (SORT_ASC.equalsIgnoreCase(sord)) {
                                return obj1.getRecognitionErrorRate1().compareTo(obj2.getRecognitionErrorRate1());
                            } else {
                                return obj2.getRecognitionErrorRate1().compareTo(obj1.getRecognitionErrorRate1());
                            }
                        } else if (sidx.equals(sortKey.get(5))) {
                            if (SORT_ASC.equalsIgnoreCase(sord)) {
                                return obj1.getRecognitionErrorRate2().compareTo(obj2.getRecognitionErrorRate2());
                            } else {
                                return obj2.getRecognitionErrorRate2().compareTo(obj1.getRecognitionErrorRate2());
                            }
                        } else {
                            if (SORT_ASC.equalsIgnoreCase(sord)) {
                                return obj1.getRecognitionErrorRate3().compareTo(obj2.getRecognitionErrorRate3());
                            } else {
                                return obj2.getRecognitionErrorRate3().compareTo(obj1.getRecognitionErrorRate3());
                            }
                        }
                    }
                });
            }
        }
        smtGraphList.setSmtCassetteVos(resultList);
        return smtGraphList;
    }

    /**
     * SMT実装機のカセット別の吸着情報 GRAPH SQL
     *
     * @param lineNo
     * @param machineUuid
     * @param columnName
     * @return
     */
    private List getTblautomaticMachineLogGraph(int lineNo, String machineUuid, String columnName) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT t.txtcol7,SUM(t.numcol1),SUM(t.numcol3),SUM(t.numcol4),SUM(t.numcol5),SUM(t.numcol6),SUM(t.numcol7),");
        if (CommonConstants.TBL_AUTOMATIC_MACHINE_CHART_CHOICE_ERROR.equals(columnName)) {
            sql.append(" SUM(t.numcol3+t.numcol4+t.numcol5+t.numcol6+t.numcol7),");
        } else {
            sql.append(" SUM(t.").append(columnName.toLowerCase()).append("),");
        }
        sql.append(" mstmachine.machineName ");
        sql.append(" FROM TblAutomaticMachineLog t ");
        sql.append(" JOIN FETCH t.mstMachine mstmachine ");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND t.machineType = :machineType ");
        sql.append(" AND t.logType = :logType ");

//        if (StringUtils.isNotEmpty(lineNo)) {
//            sql.append(" AND t.txtcol01 = :lineNo ");
//        }
        sql.append(" AND t.tblAutomaticMachineLogPK.machineUuid = :machineUuid ");

        sql = sql.append(" GROUP BY t.lineNumber, t.txtcol2, t.txtcol7");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("machineType", CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT);

        query.setParameter("logType", CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT_C);
//        if (StringUtils.isNotEmpty(lineNo)) {
//            query.setParameter("lineNo", lineNo);
//        }
        query.setParameter("machineUuid", machineUuid);

        return query.getResultList();
    }

    /**
     * SMT実装機のカセット別の吸着情報取得グラフ作成
     *
     * @param lineNo
     * @param machineUuid
     * @param barGraphColName
     * @param langId
     * @return
     */
    public SMTGraphList getSMTCassetteGraphList(int lineNo, String machineUuid, String barGraphColName, String langId) {
        SMTGraphList smtGraphList = new SMTGraphList();
        GraphicalItemInfo graphicalItemInfo = new GraphicalItemInfo();
        List<GraphicalAxis> xAxisList = new ArrayList<>();
        List<GraphicalAxis> yAxisList = new ArrayList<>();
        List<GraphicalData> dataList = new ArrayList<>();

        GraphicalAxis xAxis = new GraphicalAxis();
        GraphicalAxis yAxisLeft = new GraphicalAxis();
        GraphicalAxis yAxisRight = new GraphicalAxis();

        List<String> dataListX = new ArrayList<>();

        GraphicalData dataLeft = new GraphicalData();
        GraphicalData errorRateDataleavingRight = new GraphicalData();
        GraphicalData errorRateDataRight = new GraphicalData();
        GraphicalData errorRate1DataRight = new GraphicalData();
        GraphicalData errorRate2DataRight = new GraphicalData();
        GraphicalData errorRate3DataRight = new GraphicalData();

        List<String> dataListLeft = new ArrayList<>(); // 吸着回数
        List<String> errorRateDataleavingRightList = new ArrayList<>(); // 立ち吸着エラー率
        List<String> errorRateDataListRight = new ArrayList<>(); // 吸着エラー率
        List<String> errorRate1DataListRight = new ArrayList<>(); // 認識エラー1率
        List<String> errorRate2DataListRight = new ArrayList<>(); // 認識エラー2率
        List<String> errorRate3DataListRight = new ArrayList<>(); // 認識エラー3率
        BigDecimal zore = BigDecimal.ZERO;
        BigDecimal maxDataLeftY = zore;
        BigDecimal minDataLeftY = zore;
        BigDecimal maxDataRightY = zore;
        BigDecimal minDataRightY = zore;
        BigDecimal tickDataLeft;

        //文言
        List<String> dictKeyList = Arrays.asList("adsorbe_times", "adsorbe_error_rate", "vertical_adsorbe_error_rate","recognize_error_rate", "component_name", "error_rate", "cassette_graph_title");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        List choiceDefList = mstAutomaticMachineChartChoiceDefService.getMachineChartChoiceDefByPK(
                CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT,
                CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT_C,
                barGraphColName);
        String columnName = "";
        if (choiceDefList != null && choiceDefList.size() > 0) {
            MstAutomaticMachineChartChoiceDef mstAutomaticMachineChartChoiceDef = (MstAutomaticMachineChartChoiceDef) choiceDefList.get(0);
            columnName = mstAutomaticMachineChartChoiceDef.getColumnName();
        }
        List list = getTblautomaticMachineLogGraph(lineNo, machineUuid, columnName);
        String machineName = "";
        List<String> rateList = new ArrayList<>();
        BigDecimal rate = new BigDecimal(100);
        if (list != null && list.size() > 0) {
            BigDecimal leftY = new BigDecimal(String.valueOf(((Object[]) list.get(0))[7]));
            maxDataLeftY = leftY;
            minDataLeftY = leftY;
            for (Object obj : list) {
                Object[] log = (Object[]) obj;
                dataListX.add(String.valueOf(log[0]));// 部品名称
                dataListLeft.add(String.valueOf(log[7])); //吸着回数/吸着エラー回数/エラー合計
                tickDataLeft = new BigDecimal(String.valueOf(log[7]));
                if (tickDataLeft.compareTo(maxDataLeftY) == 1) { //判断の最大値
                    maxDataLeftY = tickDataLeft;
                }
                if (tickDataLeft.compareTo(minDataLeftY) == -1) { //判断の最小値
                    minDataLeftY = tickDataLeft;
                }
                BigDecimal numcol1 = new BigDecimal(String.valueOf(log[1]));
                if (numcol1.compareTo(zore) == 1) {
                    errorRateDataListRight.add(String.valueOf(new BigDecimal(String.valueOf(log[2])).divide(numcol1, 4, BigDecimal.ROUND_DOWN).multiply(rate)));// 吸着エラー率
                    errorRateDataleavingRightList.add(String.valueOf(new BigDecimal(String.valueOf(log[3])).divide(numcol1, 4, BigDecimal.ROUND_DOWN).multiply(rate)));// 立ち吸着エラー率
                    errorRate1DataListRight.add(String.valueOf(new BigDecimal(String.valueOf(log[4])).divide(numcol1, 4, BigDecimal.ROUND_DOWN).multiply(rate)));// 認識エラー1率
                    errorRate2DataListRight.add(String.valueOf(new BigDecimal(String.valueOf(log[5])).divide(numcol1, 4, BigDecimal.ROUND_DOWN).multiply(rate)));// 認識エラー2率
                    errorRate3DataListRight.add(String.valueOf(new BigDecimal(String.valueOf(log[6])).divide(numcol1, 4, BigDecimal.ROUND_DOWN).multiply(rate)));// 認識エラー3率
                } else {
                    errorRateDataListRight.add(String.valueOf(zore));
                    errorRateDataleavingRightList.add(String.valueOf(zore));
                    errorRate1DataListRight.add(String.valueOf(zore));
                    errorRate2DataListRight.add(String.valueOf(zore));
                    errorRate3DataListRight.add(String.valueOf(zore));
                }
                if (StringUtils.isEmpty(machineName)) {
                    machineName = (log[8] == null ? "" : log[8].toString());
                }
            }
        }
        
        rateList.addAll(errorRateDataleavingRightList);
        rateList.addAll(errorRateDataListRight);
        rateList.addAll(errorRate1DataListRight);
        rateList.addAll(errorRate2DataListRight);
        rateList.addAll(errorRate3DataListRight);
        rateList.sort(Comparator.comparing(BigDecimal::new));
        if (rateList.size() > 0) {
            minDataRightY = new BigDecimal(rateList.get(0));//最小値
            maxDataRightY = new BigDecimal(rateList.get(rateList.size() - 1));//最大値
        }

        // x軸表示データ
        xAxis.setTicks(dataListX.toArray(new String[0]));
        xAxis.setTitle(headerMap.get("component_name"));
        xAxisList.add(xAxis);

        // y軸表示データ最大値
        BigDecimal num = maxDataLeftY.subtract(minDataLeftY).multiply(new BigDecimal(0.1));
        yAxisLeft.setMaxTicks(String.valueOf(maxDataLeftY.add(num).setScale(4,BigDecimal.ROUND_DOWN)));//y
        if(minDataLeftY.compareTo(BigDecimal.ZERO) == 0 || minDataLeftY.compareTo(num) != 1) {
            yAxisLeft.setMinTicks(String.valueOf(BigDecimal.ZERO));
        } else {
            yAxisLeft.setMinTicks(String.valueOf(minDataLeftY.subtract(num).setScale(4,BigDecimal.ROUND_DOWN)));
        }
        yAxisLeft.setTitle(headerMap.get("adsorbe_times"));
        yAxisRight.setMaxTicks(String.valueOf(maxDataRightY));
        yAxisRight.setMinTicks(String.valueOf(minDataRightY));//y
        yAxisRight.setTitle(headerMap.get("error_rate")+ "(%)");
        yAxisList.add(yAxisLeft);
        yAxisList.add(yAxisRight);

        // グラフデータの設定
        dataLeft.setDataValue(dataListLeft.toArray(new String[0]));
        errorRateDataleavingRight.setDataValue(errorRateDataleavingRightList.toArray(new String[0]));
        errorRateDataRight.setDataValue(errorRateDataListRight.toArray(new String[0]));
        errorRate1DataRight.setDataValue(errorRate1DataListRight.toArray(new String[0]));
        errorRate2DataRight.setDataValue(errorRate2DataListRight.toArray(new String[0]));
        errorRate3DataRight.setDataValue(errorRate3DataListRight.toArray(new String[0]));

        // グラフ表示タイプ
        dataLeft.setGraphType("bar");
        errorRateDataleavingRight.setGraphType("line");
        errorRateDataRight.setGraphType("line");
        errorRate1DataRight.setGraphType("line");
        errorRate2DataRight.setGraphType("line");
        errorRate3DataRight.setGraphType("line");

        // グラフデータ名称
        dataLeft.setDataName(headerMap.get("adsorbe_times"));//y
        errorRateDataleavingRight.setDataName(headerMap.get("adsorbe_error_rate"));//y
        errorRateDataRight.setDataName(headerMap.get("vertical_adsorbe_error_rate"));//y
        errorRate1DataRight.setDataName(headerMap.get("recognize_error_rate").concat("1"));//y
        errorRate2DataRight.setDataName(headerMap.get("recognize_error_rate").concat("2"));//y
        errorRate3DataRight.setDataName(headerMap.get("recognize_error_rate").concat("3"));//y

        // y軸表示側
        dataLeft.setYaxisFlg(2);
        errorRateDataleavingRight.setYaxisFlg(1);
        errorRateDataRight.setYaxisFlg(1);
        errorRate1DataRight.setYaxisFlg(1);
        errorRate2DataRight.setYaxisFlg(1);
        errorRate3DataRight.setYaxisFlg(1);

        dataList.add(dataLeft);
        dataList.add(errorRateDataleavingRight);
        dataList.add(errorRateDataRight);
        dataList.add(errorRate1DataRight);
        dataList.add(errorRate2DataRight);
        dataList.add(errorRate3DataRight);
        
        graphicalItemInfo.setOptionTitle(machineName + " " + headerMap.get("cassette_graph_title"));
        graphicalItemInfo.setxAxisList(xAxisList);
        graphicalItemInfo.setyAxisList(yAxisList);
        graphicalItemInfo.setDataList(dataList);

        smtGraphList.setGraphicalItemInfo(graphicalItemInfo);

        return smtGraphList;
    }
}
