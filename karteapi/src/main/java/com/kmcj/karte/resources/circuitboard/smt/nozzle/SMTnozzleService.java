/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.smt.nozzle;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.graphical.GraphicalAxis;
import com.kmcj.karte.graphical.GraphicalData;
import com.kmcj.karte.graphical.GraphicalItemInfo;
import com.kmcj.karte.resources.circuitboard.automaticmachine.chartdef.choice.MstAutomaticMachineChartChoiceDef;
import com.kmcj.karte.resources.circuitboard.automaticmachine.chartdef.choice.MstAutomaticMachineChartChoiceDefService;
import com.kmcj.karte.resources.circuitboard.smt.SMTGraphList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttribute;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.Pager;
import org.apache.commons.lang.StringUtils;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import javax.inject.Inject;

/**
 * @author zf
 */
@Dependent
public class SMTnozzleService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstAutomaticMachineChartChoiceDefService mstAutomaticMachineChartChoiceDefService;

    private static final String SORT_ASC = "asc";
    private static final String HEAD = "1";
    private static final String NOZZLE = "2";
    private final static Map<String, String> orderKey;
    private final static List<String> sortKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("productionLineName", " ORDER BY mstProductionLine.productionLineName "); // 生産ライン名
        orderKey.put("machineId", " ORDER BY mstMachine.machineId ");//設備UUID
        orderKey.put("machineName", " ORDER BY mstMachine.machineName ");//設備名称
        orderKey.put("headno", " ORDER BY t.txtcol8 ");//ヘッドNo
        orderKey.put("injectorno", " ORDER BY t.txtcol9 ");//ノズルNo
        orderKey.put("adhesionNumber", " ORDER BY t.numcol1 ");//吸着回数
        orderKey.put("adhesionErrorNumber", " ORDER BY t.numcol3 ");//吸着エラー回数
        orderKey.put("leavingAdhesionErrorNumber", " ORDER BY t.numcol4 ");//立ち吸着エラー回数
        orderKey.put("componentRecognitionErrorNumber1", " ORDER BY t.numcol5 ");//部品認識エラー回数1
        orderKey.put("componentRecognitionErrorNumber2", " ORDER BY t.numcol6 ");//部品認識エラー回数2
        orderKey.put("componentRecognitionErrorNumber3", " ORDER BY t.numcol7 ");//部品認識エラー回数3
    }

    static {
        // エラー合計、エラー率、吸着エラー率、立ち吸着エラー率、認識エラー率1、認識エラー率2、認識エラー率3
        sortKey = new ArrayList<String>() {
            {
                add("totalErrors");
                add("errorRate");
                add("adhesionErrorRate");
                add("leavingAdhesionErrorRate");
                add("recognitionErrorRate1");
                add("recognitionErrorRate2");
                add("recognitionErrorRate3");
            }
        };
    }

    /**
     * SMT実装機（ノズル別）データ抽出SQL
     *
     * @param lineNo
     * @return
     */
    private List getTblnozzleMachineLog(int lineNo, String sidx, String sord, boolean isCount,
            int pageNumber, int pageSize) {
        StringBuilder sql = new StringBuilder();

        // 件数取得またはデータ取得
        if (isCount) {
            sql.append(" SELECT 1 ");
        } else {
            sql.append(" SELECT t.lineNumber, t.txtcol2, t.txtcol8, t.txtcol9,");
            sql.append(" SUM(t.numcol1), SUM(t.numcol3), SUM(t.numcol4), SUM(t.numcol5),");
            sql.append(" SUM(t.numcol6), SUM(t.numcol7),");
            sql.append(" mstProductionLine.productionLineName, mstMachine.machineName");
        }

        sql.append(" FROM TblAutomaticMachineLog t"
                + " LEFT JOIN FETCH t.mstProductionLine mstProductionLine "
                + " JOIN FETCH t.mstMachine mstMachine "
                + " WHERE 1=1 "
                + " AND t.machineType = :machineType "
                + " AND t.logType = :logType ");

        // 生産ラインNo
        if (lineNo != 0) {
            sql.append(" AND t.lineNumber = :lineNo ");
        }
        
        sql.append(" GROUP BY t.lineNumber, t.txtcol2, t.txtcol8, t.txtcol9 ");

        // データ取得の時、ソート順Sqlの追加
        if (StringUtils.isNotEmpty(sidx) && !isCount) {
            if (orderKey.get(sidx) != null) {
                sql.append(orderKey.get(sidx));
                if (StringUtils.isNotEmpty(sord)) {
                    sql.append(sord);
                }
            }
        }

        Query query = entityManager.createQuery(sql.toString());

        // パラメータ：自動機タイプ SMT
        query.setParameter("machineType", CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT);

        // パラメータ：ログタイプ N
        query.setParameter("logType", CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT_N);

        // パラメータ：生産ラインNo
        if (lineNo != 0) {
            query.setParameter("lineNo", lineNo);
        }

        // データ取得の時、ページ検索Sqlの追加
        if (!isCount) {

            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);

        }
        return query.getResultList();
    }

    /**
     * グラフ表示用データ抽出SQL
     *
     * @param machineUuid
     * @param graphTarget
     * @param attrCode
     * @param columnName
     * @return
     */
    private List getTblnozzleMachineLogByMachineCode(String machineUuid, String graphTarget, String attrCode, String columnName) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT t.txtcol8,t.txtcol9,SUM(t.numcol1),SUM(t.numcol3),SUM(t.numcol4),SUM(t.numcol5),SUM(t.numcol6),SUM(t.numcol7),");
        if (CommonConstants.TBL_AUTOMATIC_MACHINE_CHART_CHOICE_ERROR.equals(columnName)) {
            sql.append(" SUM(t.numcol3+t.numcol4+t.numcol5+t.numcol6+t.numcol7),");
        } else {
            sql.append(" SUM(t.").append(columnName.toLowerCase()).append("),");
        }
        sql.append(" m2.machineName");
        sql.append(" FROM TblAutomaticMachineLog t "
                + " LEFT JOIN FETCH t.mstProductionLine m1 "
                + " JOIN FETCH t.mstMachine m2 "
                + " WHERE 1=1 "
                + " AND t.machineType = :machineType "
                + " AND t.logType = :logType ");

        // 自動機 設備Uuid
        if (machineUuid != null && !"".equals(machineUuid)) {
            sql.append(" AND t.tblAutomaticMachineLogPK.machineUuid = :machineUuid ");
        }

        // x軸表示対象「１．ヘッド」「２．ノズル」
        if (!NOZZLE.equals(graphTarget)) {
            sql.append(" AND t.txtcol9 = :attrCode GROUP BY t.txtcol8 ORDER BY t.txtcol8 + 0 ");
        } else {
            sql.append(" GROUP BY t.txtcol9 ORDER BY t.txtcol9 + 0 ");
        }

        Query query = entityManager.createQuery(sql.toString());

        // パラメータ：自動機タイプ SMT
        query.setParameter("machineType", CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT);

        // パラメータ：ログタイプ N
        query.setParameter("logType", CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT_N);

        // パラメータ：自動機uuid
        if (machineUuid != null && !"".equals(machineUuid)) {
            query.setParameter("machineUuid", machineUuid);
        }

        // パラメータ：x軸表示対象
        if (!NOZZLE.equals(graphTarget)) {
            query.setParameter("attrCode", attrCode);
        }

        return query.getResultList();
    }

    /**
     * ノズル別表示対象選択肢取得SQL
     *
     * @param machineUuid
     * @return
     */
    public List<MstMachineAttribute> getGraphTargetMachine(String machineUuid) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT m2 "
                + " FROM MstMachine m1 "
                + " LEFT JOIN MstMachineAttribute m2 ON m1.machineType = m2.machineType"
                + " WHERE m1.uuid = :machineUuid "
                + " GROUP BY m2.attrCode ");

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("machineUuid", machineUuid);

        return (List<MstMachineAttribute>) query.getResultList();
    }

    /**
     * ノズル別実装機データを取得
     *
     * @param lineNo
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public SMTGraphList getSearchResult(int lineNo, String sidx, String sord,
            int pageNumber, int pageSize) {
        SMTGraphList smtGraphList = new SMTGraphList();
        List<SMTnozzleVo> resultList = new ArrayList<>();
        BigDecimal zore = new BigDecimal(BigInteger.ZERO);
        Pager pager = new Pager();

        List counts = getTblnozzleMachineLog(lineNo, "", "", true, pageNumber, pageSize);
        smtGraphList.setPageNumber(pageNumber);
        Long count =  (long)counts.size();
        smtGraphList.setCount(count);
        smtGraphList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt(count+"")));

        // 実装機ログデータを抽出
        List list = getTblnozzleMachineLog(lineNo, sidx, sord, false,
                pageNumber, pageSize);
        if (list != null && list.size() > 0) {

            Iterator iterator = list.iterator();

            BigDecimal rate = new BigDecimal(100);
            // ログリストを繰り返し、ビューオブジェクトに値を設定する。
            while (iterator.hasNext()) {
                Object[] tblAutomaticMachineLog = (Object[]) iterator.next();
                SMTnozzleVo vo = new SMTnozzleVo();

                vo.setProductionLineName(tblAutomaticMachineLog[10] != null ? String.valueOf(tblAutomaticMachineLog[10]) : ""); // 生産ライン名
                vo.setProductionLineNo(tblAutomaticMachineLog[0] != null ? String.valueOf(tblAutomaticMachineLog[0]) : ""); // 生産ライン
                vo.setMachineId(tblAutomaticMachineLog[1] == null ? "" : String.valueOf(tblAutomaticMachineLog[1])); // 設備No
                vo.setMachineName(tblAutomaticMachineLog[11] == null ? "" : String.valueOf(tblAutomaticMachineLog[11])); // 設備名称
                vo.setHeadno(tblAutomaticMachineLog[2] == null ? "" : String.valueOf(tblAutomaticMachineLog[2]));//ヘッドNo
                vo.setInjectorno(tblAutomaticMachineLog[3] == null ? "" : String.valueOf(tblAutomaticMachineLog[3]));//ノズルNo
                vo.setAdhesionNumber(tblAutomaticMachineLog[4] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[4]))); // 吸着回数
                vo.setAdhesionErrorNumber(tblAutomaticMachineLog[5] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[5]))); // 吸着エラー回数
                vo.setLeavingAdhesionErrorNumber(tblAutomaticMachineLog[6] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[6]))); // 立ち吸着エラー回数
                vo.setComponentRecognitionErrorNumber1(tblAutomaticMachineLog[7] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[7]))); // 部品認識エラー回数1
                vo.setComponentRecognitionErrorNumber2(tblAutomaticMachineLog[8] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[8]))); // 部品認識エラー回数2
                vo.setComponentRecognitionErrorNumber3(tblAutomaticMachineLog[9] == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(tblAutomaticMachineLog[9]))); // 部品認識エラー回数3
                BigDecimal totalError = vo.getAdhesionErrorNumber().add(vo.getLeavingAdhesionErrorNumber())
                        .add(vo.getComponentRecognitionErrorNumber1())
                        .add(vo.getComponentRecognitionErrorNumber2())
                        .add(vo.getComponentRecognitionErrorNumber3());

                vo.setTotalErrors(totalError); // エラー合計
                if (vo.getAdhesionNumber().compareTo(BigDecimal.ZERO) == 1) {

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
                vo.setAutoMachine(vo.getMachineId()); // 自動機
                resultList.add(vo);
            }

            if (sortKey.contains(sidx)) {
                Collections.sort(resultList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        SMTnozzleVo obj1 = (SMTnozzleVo) o1;
                        SMTnozzleVo obj2 = (SMTnozzleVo) o2;

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
                        } else if (SORT_ASC.equalsIgnoreCase(sord)) {
                            return obj1.getRecognitionErrorRate3().compareTo(obj2.getRecognitionErrorRate3());
                        } else {
                            return obj2.getRecognitionErrorRate3().compareTo(obj1.getRecognitionErrorRate3());
                        }
                    }
                });
            }
        }
        smtGraphList.setSMTnozzleVos(resultList);
        return smtGraphList;
    }

    /**
     * 基板SMT実装機ノズル別分析情報グラフの作成
     *
     * @param machineUuid
     * @param graphTarget
     * @param graphType
     * @param attrCode
     * @param langId
     * @return
     */
    public SMTGraphList getSMTNozzleGraph(
            String machineUuid,
            String graphTarget,
            String graphType,
            String attrCode,
            String langId) {
        SMTGraphList smtGraphList = new SMTGraphList();
        GraphicalItemInfo graphicalItemInfo = new GraphicalItemInfo();
        List<GraphicalAxis> xAxisList = new ArrayList<>();
        List<GraphicalAxis> yAxisList = new ArrayList<>();
        List<GraphicalData> dataList = new ArrayList<>();

        GraphicalAxis xAxis = new GraphicalAxis();
        GraphicalAxis yAxisLeft = new GraphicalAxis();
        GraphicalAxis yAxisRight = new GraphicalAxis();

        List<String> xdata = new ArrayList<>();

        GraphicalData adhesionNumberData = new GraphicalData();
        GraphicalData leavingAdhesionErrorRateData = new GraphicalData();
        GraphicalData adhesionErrorRateData = new GraphicalData();
        GraphicalData recognitionErrorRate1Data = new GraphicalData();
        GraphicalData recognitionErrorRate2Data = new GraphicalData();
        GraphicalData recognitionErrorRate3Data = new GraphicalData();

        List<String> adhesionNumberDataList = new ArrayList<>(); // 吸着回数
        List<String> leavingAdhesionErrorRateDataList = new ArrayList<>(); // 立ち吸着エラー率
        List<String> adhesionErrorRateDataList = new ArrayList<>(); // 吸着エラー率
        List<String> recognitionErrorRate1DataList = new ArrayList<>(); // 認識エラー1率
        List<String> recognitionErrorRate2DataList = new ArrayList<>(); // 認識エラー2率
        List<String> recognitionErrorRate3DataList = new ArrayList<>(); // 認識エラー3率
        BigDecimal maxNum = BigDecimal.ZERO;
        BigDecimal maxRate = BigDecimal.ZERO;
        BigDecimal minNum = BigDecimal.ZERO;
        BigDecimal minRate = BigDecimal.ZERO;
        BigDecimal tickDataLeft;
        //文言
        List<String> dictKeyList = Arrays.asList("adsorbe_times", "adsorbe_error_rate", "vertical_adsorbe_error_rate", "recognize_error_rate", "error_rate", "nozzle_graph_title");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);

        List choiceDefList = mstAutomaticMachineChartChoiceDefService.getMachineChartChoiceDefByPK(
                CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT,
                CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_SMT_N,
                graphType);
        String columnName = "";
        if (choiceDefList != null && choiceDefList.size() > 0) {
            MstAutomaticMachineChartChoiceDef mstAutomaticMachineChartChoiceDef = (MstAutomaticMachineChartChoiceDef) choiceDefList.get(0);
            columnName = mstAutomaticMachineChartChoiceDef.getColumnName();
        }
        List logList = getTblnozzleMachineLogByMachineCode(machineUuid, graphTarget, attrCode, columnName);

        String machineName = "";
        List<String> rateList = new ArrayList<>();
        BigDecimal rate = new BigDecimal(100);
        if (logList != null && logList.size() > 0) {
            BigDecimal leftY;
            if (((Object[])logList.get(0))[8] != null) {
                leftY = new BigDecimal(String.valueOf(((Object[]) logList.get(0))[8]));
            } else {
                leftY = BigDecimal.ZERO; 
            }
            maxNum = leftY;
            minNum = leftY;
            String xPoint = null;
            for (Object obj : logList) {
                Object[] log = (Object[]) obj;
                if (HEAD.equals(graphTarget)) {
                    xPoint = log[0] != null ? String.valueOf(log[0]) : "";
                } else {
                    xPoint = log[1] != null ? String.valueOf(log[1]) : "";
                }
                if (StringUtils.isEmpty(xPoint)) {
                    continue;
                } else {
                    xdata.add(xPoint);
                }
                BigDecimal numcol1 = new BigDecimal(log[2] != null ? String.valueOf(log[2]) : "0");

                boolean isRated = numcol1.compareTo(BigDecimal.ZERO) != 0;

                BigDecimal leavingAdhesionErrorRate = isRated ? new BigDecimal(log[4] != null ? String.valueOf(log[4]) : "0")
                        .divide(numcol1, 4, BigDecimal.ROUND_DOWN).multiply(rate) : BigDecimal.ZERO;
                BigDecimal adhesionErrorRate = isRated ? new BigDecimal(log[3] != null ? String.valueOf(log[3]) : "0")
                        .divide(numcol1, 4, BigDecimal.ROUND_DOWN).multiply(rate) : BigDecimal.ZERO;
                BigDecimal recognitionErrorRate1 = isRated ? new BigDecimal(log[5] != null ? String.valueOf(log[5]) : "0")
                        .divide(numcol1, 4, BigDecimal.ROUND_DOWN).multiply(rate) : BigDecimal.ZERO;
                BigDecimal recognitionErrorRate2 = isRated ? new BigDecimal(log[6] != null ? String.valueOf(log[6]) : "0")
                        .divide(numcol1, 4, BigDecimal.ROUND_DOWN).multiply(rate) : BigDecimal.ZERO;
                BigDecimal recognitionErrorRate3 = isRated ? new BigDecimal(log[7] != null ? String.valueOf(log[7]) : "0")
                        .divide(numcol1, 4, BigDecimal.ROUND_DOWN).multiply(rate) : BigDecimal.ZERO;

                leavingAdhesionErrorRateDataList.add(String.valueOf(leavingAdhesionErrorRate));
                adhesionErrorRateDataList.add(String.valueOf(adhesionErrorRate));
                recognitionErrorRate1DataList.add(String.valueOf(recognitionErrorRate1));
                recognitionErrorRate2DataList.add(String.valueOf(recognitionErrorRate2));
                recognitionErrorRate3DataList.add(String.valueOf(recognitionErrorRate3));

                adhesionNumberDataList.add(log[8] != null ? String.valueOf(log[8]) : "0");//吸着回数/吸着エラー回数/エラー合計
                tickDataLeft = new BigDecimal(log[8] != null ? String.valueOf(log[8]) : "0");
                if (tickDataLeft.compareTo(maxNum) == 1) { //判断の最大値
                    maxNum = tickDataLeft;
                }
                if (tickDataLeft.compareTo(minNum) == -1) { //判断の最小値
                    minNum = tickDataLeft;
                }
                if (StringUtils.isEmpty(machineName)) {
                    machineName = (log[9] == null ? "" : log[9].toString());
                }
            }
        }

        rateList.addAll(leavingAdhesionErrorRateDataList);
        rateList.addAll(adhesionErrorRateDataList);
        rateList.addAll(recognitionErrorRate1DataList);
        rateList.addAll(recognitionErrorRate2DataList);
        rateList.addAll(recognitionErrorRate3DataList);
        rateList.sort(Comparator.comparing(BigDecimal::new));

        if (rateList.size() > 0) {
            minRate = new BigDecimal(rateList.get(0));
            maxRate = new BigDecimal(rateList.get(rateList.size() - 1));
        }

        // x軸表示データ
        xAxis.setTicks(xdata.toArray(new String[0]));
        xAxisList.add(xAxis);

        // y軸表示データ最大値
        BigDecimal num = maxNum.subtract(minNum).multiply(new BigDecimal(0.1));
        yAxisLeft.setMaxTicks(String.valueOf(maxNum.add(num).setScale(4,BigDecimal.ROUND_DOWN)));
        if(minNum.compareTo(BigDecimal.ZERO) == 0 || minNum.compareTo(num) != 1) {
            yAxisLeft.setMinTicks(String.valueOf(BigDecimal.ZERO));
        } else {
            yAxisLeft.setMinTicks(String.valueOf(minNum.subtract(num).setScale(4,BigDecimal.ROUND_DOWN)));
        }
        yAxisLeft.setTitle(headerMap.get("adsorbe_times"));
        yAxisRight.setMaxTicks(String.valueOf(maxRate));
        yAxisRight.setMinTicks(String.valueOf(minRate));
        yAxisRight.setTitle(headerMap.get("error_rate") + "(%)");
        yAxisList.add(yAxisLeft);
        yAxisList.add(yAxisRight);

        // グラフデータの設定
        adhesionNumberData.setDataValue(adhesionNumberDataList.toArray(new String[0]));
        leavingAdhesionErrorRateData.setDataValue(leavingAdhesionErrorRateDataList.toArray(new String[0]));
        adhesionErrorRateData.setDataValue(adhesionErrorRateDataList.toArray(new String[0]));
        recognitionErrorRate1Data.setDataValue(recognitionErrorRate1DataList.toArray(new String[0]));
        recognitionErrorRate2Data.setDataValue(recognitionErrorRate2DataList.toArray(new String[0]));
        recognitionErrorRate3Data.setDataValue(recognitionErrorRate3DataList.toArray(new String[0]));

        // グラフ表示タイプ
        adhesionNumberData.setGraphType("bar");
        leavingAdhesionErrorRateData.setGraphType("line");
        adhesionErrorRateData.setGraphType("line");
        recognitionErrorRate1Data.setGraphType("line");
        recognitionErrorRate2Data.setGraphType("line");
        recognitionErrorRate3Data.setGraphType("line");

        // グラフデータ名称
        adhesionNumberData.setDataName(headerMap.get("adsorbe_times"));
        leavingAdhesionErrorRateData.setDataName(headerMap.get("adsorbe_error_rate"));
        adhesionErrorRateData.setDataName(headerMap.get("vertical_adsorbe_error_rate"));
        recognitionErrorRate1Data.setDataName(headerMap.get("recognize_error_rate").concat("1"));
        recognitionErrorRate2Data.setDataName(headerMap.get("recognize_error_rate").concat("2"));
        recognitionErrorRate3Data.setDataName(headerMap.get("recognize_error_rate").concat("3"));

        // y軸表示側
        adhesionNumberData.setYaxisFlg(2);
        leavingAdhesionErrorRateData.setYaxisFlg(1);
        adhesionErrorRateData.setYaxisFlg(1);
        recognitionErrorRate1Data.setYaxisFlg(1);
        recognitionErrorRate2Data.setYaxisFlg(1);
        recognitionErrorRate3Data.setYaxisFlg(1);

        dataList.add(adhesionNumberData);
        dataList.add(leavingAdhesionErrorRateData);
        dataList.add(adhesionErrorRateData);
        dataList.add(recognitionErrorRate1Data);
        dataList.add(recognitionErrorRate2Data);
        dataList.add(recognitionErrorRate3Data);

        graphicalItemInfo.setxAxisList(xAxisList);
        graphicalItemInfo.setyAxisList(yAxisList);
        graphicalItemInfo.setDataList(dataList);
        graphicalItemInfo.setOptionTitle(machineName + " " + headerMap.get("nozzle_graph_title"));

        smtGraphList.setGraphicalItemInfo(graphicalItemInfo);

        return smtGraphList;
    }
}
