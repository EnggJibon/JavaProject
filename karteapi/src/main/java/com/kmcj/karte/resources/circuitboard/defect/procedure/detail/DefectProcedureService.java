/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.defect.procedure.detail;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.circuitboard.procedure.MstCircuitBoardProcedure;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Apeng
 */
@Dependent
public class DefectProcedureService {

    private static final int DISPLAY_FLG_YES = 1;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("productName", " ORDER BY c.PRODUCT_NAME "); // 機種名
        orderKey.put("componentName", " ORDER BY c.COMPONENT_CODE "); // 基板名
        orderKey.put("productionLineName", " ORDER BY c.PRODUCTION_LINE_NAME "); // 生産ライン
        orderKey.put("serialNumber", " ORDER BY c.TXTCOL04 "); // シリアルナンバー
        orderKey.put("engineereName", " ORDER BY c.PROCEDURE_NAME "); // 工程名
        orderKey.put("checkDateText", " ORDER BY c.dtcol1 "); // 検索日
        orderKey.put("badNumber", " ORDER BY c.badNumber "); // 不良件数
    }

    /**
     * 基板工程別工程一覧
     *
     * @return
     */
    public DefectProcedureList getProcedureList() {
        DefectProcedureList defectProcedureList = new DefectProcedureList();
        Query query = entityManager.createNamedQuery("MstCircuitBoardProcedure.findByDisplayFlg");
        query.setParameter("displayFlg", DISPLAY_FLG_YES);

        List<MstCircuitBoardProcedure> mstCircuitBoardProcedures = query.getResultList();
        defectProcedureList.setMstCircuitBoardProcedures(mstCircuitBoardProcedures);

        return defectProcedureList;
    }

    /**
     * 基板工程別不良情報一覧取得
     *
     * @param productCode
     * @param productionLineNo
     * @param componentCode
     * @param serialNumber
     * @param formatCheckDateStart
     * @param formatCheckDateEnd
     * @param engineereGrids
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param loginUser
     * @return
     */
    public DefectProcedureList getDefectProcedureList(
            String productCode,
            int productionLineNo,
            String componentCode,
            Date formatCheckDateStart,
            Date formatCheckDateEnd,
            String serialNumber,
            String engineereGrids,
            String sidx,//order Key
            String sord,//order 順
            int pageNumber,
            int pageSize,
            LoginUser loginUser
    ) {
        DefectProcedureList defectProcedureList = new DefectProcedureList();
        List<DefectProcedureVo> defectProcedureVos = new ArrayList();
        String strGrid = "";
        if (StringUtils.isNotEmpty(engineereGrids)) {
            for (String engineereGrid : engineereGrids.split(",")) {
                strGrid = strGrid.concat("'").concat(engineereGrid).concat("',");
            }
            strGrid = strGrid.substring(0, strGrid.length() - 1).concat(")");
        }
        List counts = getUnionSql(productCode, productionLineNo, componentCode, formatCheckDateStart, formatCheckDateEnd, serialNumber, strGrid, true, null, null, pageNumber, pageSize);
        // ページをめぐる
        Pager pager = new Pager();
        defectProcedureList.setPageNumber(pageNumber);
        long count = (long) counts.get(0);
        defectProcedureList.setCount(count);
        defectProcedureList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt(count + "")));
        if (count > 0) {
            //自動機/基板検査結果取得
            List list = getUnionSql(productCode, productionLineNo, componentCode, formatCheckDateStart, formatCheckDateEnd, serialNumber, strGrid, false, sidx, sord, pageNumber, pageSize);

            if (list != null && list.size() > 0) {
                defectProcedureList.setDefectProcedureVos(setDefectProcedureValue(list, 0, defectProcedureVos, false, loginUser.getLangId()));
            }
        }

        return defectProcedureList;
    }

    /**
     * 基板工程別不良情報一覧设值
     *
     * @param list
     * @param defectProcedureVo
     * @param resultType
     * @return
     */
    private List<DefectProcedureVo> setDefectProcedureValue(
            List list,
            int resultType,
            List<DefectProcedureVo> defectProcedureVos,
            boolean detailFlag,
            String langId
    ) {
        DefectProcedureVo defectProcedureVo;
        FileUtil fu = new FileUtil();
        Map<String, String> map = mstChoiceService.getChoiceMap(langId, new String[]{"", ""});
        if (!detailFlag) {
            for (Object obj : list) {
                Object[] defectProcedure = (Object[]) obj;
                defectProcedureVo = new DefectProcedureVo();
                // 機種名
                if (defectProcedure[0] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[0]))) {
                    defectProcedureVo.setProductName(String.valueOf(defectProcedure[0]));
                } else {
                    defectProcedureVo.setProductName(null);
                }
                // 基板名
                if (defectProcedure[1] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[1]))) {
                    defectProcedureVo.setComponentName(String.valueOf(defectProcedure[1]));
                } else {
                    defectProcedureVo.setComponentName(null);
                }
                // 生产ライン名
                if (defectProcedure[2] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[2]))) {
                    defectProcedureVo.setProductionLineName(String.valueOf(defectProcedure[2]));
                } else {
                    defectProcedureVo.setProductionLineName(null);
                }
                // 工程名
                if (defectProcedure[3] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[3]))) {
                    defectProcedureVo.setEngineereName(String.valueOf(defectProcedure[3]));
                } else {
                    defectProcedureVo.setEngineereName(null);
                }
                // シリアルナンバー
                if (defectProcedure[6] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[6]))) {
                    defectProcedureVo.setSerialNumber(String.valueOf(defectProcedure[6]));
                } else {
                    defectProcedureVo.setSerialNumber(null);
                }
                // 検査日
                if (defectProcedure[7] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[7]))) {
                    defectProcedureVo.setCheckDateText(fu.getDateFormatForStr(defectProcedure[7]));
                    defectProcedureVo.setCheckDate(fu.getDateTimeFormatForStr(defectProcedure[7]));
                } else {
                    defectProcedureVo.setCheckDateText(null);
                    defectProcedureVo.setCheckDate(null);
                }
                // 不良数
                defectProcedureVo.setBadNumber(Integer.parseInt(String.valueOf(defectProcedure[10])));
                // 生産ラインNo
                if (defectProcedure[4] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[4]))) {
                    defectProcedureVo.setSearchProductionLineNo(String.valueOf(defectProcedure[4]));
                } else {
                    defectProcedureVo.setSearchProductionLineNo(null);
                }
                // 基板コード
                if (defectProcedure[5] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[5]))) {
                    defectProcedureVo.setSearchComponentCode(String.valueOf(defectProcedure[5]));
                } else {
                    defectProcedureVo.setSearchComponentCode(null);
                }
                // 製品コード
                if (defectProcedure[8] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[8]))) {
                    defectProcedureVo.setSearchProductCode(String.valueOf(defectProcedure[8]));
                } else {
                    defectProcedureVo.setSearchProductCode(null);
                }
                // 自動機ログ/基板検査結果タイプ
                defectProcedureVo.setResultType(Integer.valueOf(defectProcedure[11].toString()));
                defectProcedureVos.add(defectProcedureVo);
            }
        } else {
            // 不良内容Choice
            MstChoiceList ngEndCodeList = mstChoiceService.getChoice(langId, "tbl_automatic_machine_log.ng_end_code");
            String badPlace = null;
            StringBuffer badContent;
            Map<String, String> dictionaryMap = getDictionaryList(langId);
            for (Object obj : list) {
                Object[] defectProcedure = (Object[]) obj;
                defectProcedureVo = new DefectProcedureVo();
                // 機種名
                if (defectProcedure[0] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[0]))) {
                    defectProcedureVo.setProductName(String.valueOf(defectProcedure[0]));
                } else {
                    defectProcedureVo.setProductName(null);
                }
                // 基板名
                if (defectProcedure[1] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[1]))) {
                    defectProcedureVo.setComponentName(String.valueOf(defectProcedure[1]));
                } else {
                    defectProcedureVo.setComponentName(null);
                }
                // 生产ライン名
                if (defectProcedure[2] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[2]))) {
                    defectProcedureVo.setProductionLineName(String.valueOf(defectProcedure[2]));
                } else {
                    defectProcedureVo.setProductionLineName(null);
                }
                // 工程名
                if (defectProcedure[3] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[3]))) {
                    defectProcedureVo.setEngineereName(String.valueOf(defectProcedure[3]));
                } else {
                    defectProcedureVo.setEngineereName(null);
                }
                // シリアルナンバー
                if (defectProcedure[4] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[4]))) {
                    defectProcedureVo.setSerialNumber(String.valueOf(defectProcedure[4]));
                } else {
                    defectProcedureVo.setSerialNumber(null);
                }
                //検査日
                if (defectProcedure[5] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[5]))) {
                    defectProcedureVo.setCheckDateText(fu.getDateFormatForStr(defectProcedure[5]));
                    defectProcedureVo.setCheckDate(fu.getDateTimeFormatForStr(defectProcedure[5]));
                } else {
                    defectProcedureVo.setCheckDateText(null);
                    defectProcedureVo.setCheckDate(null);
                }
                if (resultType == 1) {
                    if (defectProcedure[6] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[6]))) {
                        badContent = new StringBuffer();

                        // 不良箇所と不良内容
                        switch (String.valueOf(defectProcedure[6])) {
                            case CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI_NG:
                            case CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI:
                                if (defectProcedure[8] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[8]))) {
                                    badPlace = String.valueOf(defectProcedure[8]);
                                }
                                if (defectProcedure[9] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[9]))) {
                                    if (ngEndCodeList.getMstChoice() != null && !ngEndCodeList.getMstChoice().isEmpty()) {
                                        for (MstChoice mstChoice : ngEndCodeList.getMstChoice()) {
                                            if (mstChoice.getMstChoicePK().getSeq().equals(String.valueOf(defectProcedure[9]))) {
                                                badContent = badContent.append(mstChoice.getChoice());
                                                break;
                                            }
                                        }
                                    } else {
                                        badContent = null;
                                    }
                                } else {
                                    badContent = null;
                                }
                                break;
                            case CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FCT:
                                if (defectProcedure[7] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[7]))) {
                                    badContent = badContent.append(String.valueOf(defectProcedure[7]));
                                } else {
                                    badContent = null;
                                }
                                break;
                            case CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_ICT:
                                if (defectProcedure[7] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[7]))) {
                                    badPlace = String.valueOf(defectProcedure[7]);
                                }
                                badContent = badContent.append(dictionaryMap.get("inspection_setp")).append(":")
                                        .append(String.valueOf(defectProcedure[8]) == null ? "" : String.valueOf(defectProcedure[8]))
                                        .append(dictionaryMap.get("pin_number")).append("1").append(":")
                                        .append(String.valueOf(defectProcedure[9]) == null ? "" : String.valueOf(defectProcedure[9]))
                                        .append(dictionaryMap.get("pin_number")).append("2").append(":")
                                        .append(String.valueOf(defectProcedure[10]) == null ? "" : String.valueOf(defectProcedure[10]));
                                break;
                            case CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT_NG:
                            case CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT:
                                if (defectProcedure[7] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[7]))) {
                                    badPlace = String.valueOf(defectProcedure[7]);
                                }
                                badContent = badContent.append(dictionaryMap.get("inspection_setp")).append(":")
                                        .append(String.valueOf(defectProcedure[8]) == null ? "" : String.valueOf(defectProcedure[8]))
                                        .append(dictionaryMap.get("measurement_result")).append(":")
                                        .append(String.valueOf(defectProcedure[9]) == null ? "" : String.valueOf(defectProcedure[9]));
                                break;
                            default:
                                badContent = null;
                                break;
                        }

                        defectProcedureVo.setBadPlace(badPlace);
                        if (badContent != null) {
                            defectProcedureVo.setBadContent(badContent.toString());
                        } else {
                            defectProcedureVo.setBadContent(null);
                        }
                    }
                } else {
                    if (defectProcedure[6] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[6]))) {
                        defectProcedureVo.setBadPlace(String.valueOf(defectProcedure[6]));
                        defectProcedureVo.setNativePlace(String.valueOf(defectProcedure[6]));
                    } else {
                        defectProcedureVo.setBadPlace(null);
                        defectProcedureVo.setNativePlace(null);
                    }
                    if (defectProcedure[8] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[8]))) {
                        if (ngEndCodeList.getMstChoice() != null && !ngEndCodeList.getMstChoice().isEmpty()) {
                            for (MstChoice mstChoice : ngEndCodeList.getMstChoice()) {
                                if (mstChoice.getMstChoicePK().getSeq().equals(String.valueOf(defectProcedure[8]))) {
                                    defectProcedureVo.setBadContent(mstChoice.getChoice());
                                    break;
                                }
                            }
                        } else {
                            defectProcedureVo.setBadContent(null);
                        }
                    } else {
                        defectProcedureVo.setBadContent(null);
                    }
                    if (defectProcedure[9] != null && StringUtils.isNotEmpty(String.valueOf(defectProcedure[9]))) {
                        defectProcedureVo.setNativeContent(String.valueOf(defectProcedure[9]));
                    } else {
                        defectProcedureVo.setNativeContent(null);
                    }
                }
                defectProcedureVo.setResultType(resultType);
                defectProcedureVos.add(defectProcedureVo);
            }
        }
        return defectProcedureVos;
    }

    /**
     * 基板工程別不良情報詳細 SQL
     *
     * @param productCode
     * @param productionLineNo
     * @param componentCode
     * @param streamNumber
     * @param engineereGrids
     * @param formatCheckDate
     * @return
     */
    private List getSql(
            String productCode,
            int productionLineNo,
            String componentCode,
            String serialNumber,
            String engineereGrids,
            Date formatCheckDate
    ) {

        StringBuilder sql;
        sql = new StringBuilder("SELECT mstProduct.productName,mstComponent.componentCode,mstProductionLine.productionLineName,mstCircuitBoardProcedure.procedureName,"
                + " log.serialNumber,log.dtcol1,log.machineType,log.txtcol6,log.txtcol7,log.txtcol8,log.txtcol9 ");
        sql.append(" FROM TblAutomaticMachineLog log "
                + " LEFT JOIN MstProductionLine mstProductionLine ON mstProductionLine.productionLineId = log.lineNumber "
                + " LEFT JOIN MstComponent mstComponent ON mstComponent.componentCode = log.circuitBoardCode "
                + " LEFT JOIN MstProductComponent mstProductComponent ON mstProductComponent.componentId = mstComponent.id "
                + " LEFT JOIN MstProduct mstProduct ON mstProduct.productId = mstProductComponent.productId "
                + " LEFT JOIN MstMachineProcedure mstMachineProcedure ON mstMachineProcedure.mstMachineProcedurePK.machineUuid = log.tblAutomaticMachineLogPK.machineUuid "
                + " LEFT JOIN mstMachineProcedure.mstCircuitBoardProcedure mstCircuitBoardProcedure "
                + " WHERE 1=1 AND log.machineType IN ('");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI_NG);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_ICT);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT_NG);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FCT);
        sql.append("')");

        if (!StringUtils.isEmpty(productCode)) {
            sql = sql.append(" AND mstProduct.productCode = :productCode ");
        }

        if (productionLineNo != 0) {
            sql = sql.append(" AND log.lineNumber = :productionLineNo ");
        }

        if (!StringUtils.isEmpty(componentCode)) {
            sql = sql.append(" AND log.circuitBoardCode = :componentCode ");
        }

        if (!StringUtils.isEmpty(serialNumber)) {
            sql = sql.append(" AND log.serialNumber = :serialNumber ");
        }

        if (formatCheckDate != null) {
            sql = sql.append(" and log.dtcol1 = :formatCheckDate ");
        }

        if (!StringUtils.isEmpty(engineereGrids)) {
            sql = sql.append(" AND mstCircuitBoardProcedure.id IN (:engineereList) ");
        }
        sql = sql.append(" AND (((log.machineType = 'AOI' OR log.machineType = 'AOI_NG') AND log.txtcol8 IS NOT NULL AND log.txtcol8 <> '' AND log.txtcol8 > 0) OR "
                + " (log.machineType <> 'AOI' AND log.machineType <> 'AOI_NG' AND log.txtcol5 LIKE '%NG%')) ");
        //検査日の降順、機種名、基板名の昇順
        sql = sql.append(" ORDER BY log.dtcol1 DESC,mstProduct.productName ASC,mstComponent.componentCode ASC");

        Query query = entityManager.createQuery(sql.toString());

        if (!StringUtils.isEmpty(productCode)) {
            query.setParameter("productCode", productCode.trim());
        }

        if (productionLineNo != 0) {
            query.setParameter("productionLineNo", productionLineNo);
        }

        if (!StringUtils.isEmpty(componentCode)) {
            query.setParameter("componentCode", componentCode.trim());
        }

        if (!StringUtils.isEmpty(serialNumber)) {
            query.setParameter("serialNumber", serialNumber.trim());
        }

        if (formatCheckDate != null) {
            query.setParameter("formatCheckDate", formatCheckDate);
        }

        if (!StringUtils.isEmpty(engineereGrids)) {
            query.setParameter("engineereList", engineereGrids);
        }

        List list = query.getResultList();

        return list;
    }

    /**
     * 基板工程別不良情報一覧 SQL
     *
     * @param productCode
     * @param productionLineNo
     * @param componentCode
     * @param streamNumber
     * @param engineereGrids
     * @param formatCheckDate
     * @return
     */
    private List getUnionSql(
            String productCode,
            int productionLineNo,
            String componentCode,
            Date formatCheckDateStart,
            Date formatCheckDateEnd,
            String serialNumber,
            String engineereGrids,
            boolean isCount,
            String sidx,//order Key
            String sord,//order 順
            int pageNumber,
            int pageSize
    ) {
        int index = 1;
        StringBuilder sql = new StringBuilder(" SELECT");
        if (productionLineNo != 0) {
            if (isCount) {
                sql = sql.append(" COUNT(1) ");
            } else {
                sql = sql.append(" c.PRODUCT_NAME,c.COMPONENT_CODE,c.PRODUCTION_LINE_NAME,c.PROCEDURE_NAME,c.LINE_NUMBER,c.CIRCUIT_BOARD_CODE,c.SERIAL_NUMBER,c.DTCOL1,c.PRODUCT_CODE,c.MACHINE_TYPE,c.badNumber,c.resultType");
            }
            sql.append(" FROM (");
            sql.append("SELECT mstProduct.PRODUCT_NAME,mstComponent.COMPONENT_CODE,mstProductionLine.PRODUCTION_LINE_NAME,mstCircuitBoardProcedure.PROCEDURE_NAME,"
                    + " log.LINE_NUMBER,log.CIRCUIT_BOARD_CODE,log.SERIAL_NUMBER,log.dtcol1,mstProduct.PRODUCT_CODE,log.MACHINE_TYPE,"
                    + " SUM(CASE WHEN log.txtcol8 IS NOT NULL AND log.txtcol8 <> '' AND log.txtcol8 > 0 THEN 1 ELSE (CASE WHEN (log.txtcol5 like '%NG%') THEN 1 ELSE 0 END) END) badNumber,"
                    + " '1' resultType"
                    + " FROM tbl_automatic_machine_log log "
                    + " LEFT JOIN mst_production_line mstProductionLine ON mstProductionLine.PRODUCTION_LINE_ID = log.LINE_NUMBER "
                    + " LEFT JOIN mst_component mstComponent ON mstComponent.COMPONENT_CODE = log.CIRCUIT_BOARD_CODE "
                    + " LEFT JOIN mst_product_component mstProductComponent ON mstProductComponent.COMPONENT_ID = mstComponent.ID "
                    + " LEFT JOIN mst_product mstProduct ON mstProduct.PRODUCT_ID = mstProductComponent.PRODUCT_ID "
                    + " LEFT JOIN mst_machine_procedure mstMachineProcedure ON mstMachineProcedure.MACHINE_UUID = log.MACHINE_UUID "
                    + " LEFT JOIN mst_circuit_board_procedure mstCircuitBoardProcedure ON mstCircuitBoardProcedure.ID = mstMachineProcedure.PROCEDURE_ID "
                    + " WHERE log.MACHINE_TYPE IN ('");
            sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI);
            sql.append("', '");
            sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI_NG);
            sql.append("', '");
            sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_ICT);
            sql.append("', '");
            sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT);
            sql.append("', '");
            sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT_NG);
            sql.append("', '");
            sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FCT);
            sql.append("')");

            if (!StringUtils.isEmpty(productCode)) {
                sql = sql.append(" AND mstProduct.product_code = ? ");
            }

            if (productionLineNo != 0) {
                sql = sql.append(" AND log.LINE_NUMBER = ? ");
            }

            if (!StringUtils.isEmpty(componentCode)) {
                sql = sql.append(" AND log.CIRCUIT_BOARD_CODE = ? ");
            }

            if (!StringUtils.isEmpty(serialNumber)) {
                sql = sql.append(" AND log.SERIAL_NUMBER LIKE ? ");
            }

            if (formatCheckDateStart != null) {
                sql = sql.append(" and log.dtcol1 >= ? ");
            }

            if (formatCheckDateEnd != null) {
                sql = sql.append(" and log.dtcol1 <= ? ");
            }

            if (!StringUtils.isEmpty(engineereGrids)) {
                sql = sql.append(" AND mstCircuitBoardProcedure.id IN (");
                sql.append(engineereGrids);
            }
            sql = sql.append(" AND (((log.MACHINE_TYPE = 'AOI' OR log.MACHINE_TYPE = 'AOI_NG') AND log.txtcol8 IS NOT NULL AND log.txtcol8 <> '' AND log.txtcol8 > 0) OR "
                + " (log.MACHINE_TYPE <> 'AOI' AND log.MACHINE_TYPE <> 'AOI_NG' AND log.txtcol5 LIKE '%NG%')) ");
            //検査数分组：自動機タイプ、基板コード、検査日組み合わせ集計キーとしての数
            sql = sql.append(" GROUP BY log.LINE_NUMBER,log.MACHINE_TYPE,log.CIRCUIT_BOARD_CODE,log.SERIAL_NUMBER,log.dtcol1");
            sql.append(" ) c");
            if (!isCount) {
                if (StringUtils.isNotEmpty(sidx)) {
                    if (orderKey.get(sidx) != null) {
                        sql = sql.append(orderKey.get(sidx));
                        if (StringUtils.isNotEmpty(sord)) {
                            sql = sql.append(sord);
                        }
                    }
                } else {
                    //検査日の降順、機種名、基板名の昇順
                    sql = sql.append(" ORDER BY c.dtcol1 DESC,c.PRODUCT_NAME ASC,c.COMPONENT_CODE ASC");
                }
            }

            Query query = entityManager.createNativeQuery(sql.toString());

            if (!StringUtils.isEmpty(productCode)) {
                query.setParameter(index++, productCode.trim());
            }

            if (productionLineNo != 0) {
                query.setParameter(index++, productionLineNo);
            }

            if (!StringUtils.isEmpty(componentCode)) {
                query.setParameter(index++, componentCode.trim());
            }

            if (!StringUtils.isEmpty(serialNumber)) {
                query.setParameter(index++, "%" + serialNumber.trim() + "%");
            }

            if (formatCheckDateStart != null) {
                query.setParameter(index++, formatCheckDateStart);
            }

            if (formatCheckDateEnd != null) {
                query.setParameter(index++, formatCheckDateEnd);
            }

            if (!isCount) {
                Pager pager = new Pager();
                query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
                query.setMaxResults(pageSize);
            }

            List list = query.getResultList();

            return list;
        } else {
            if (isCount) {
                sql = sql.append(" COUNT(1) ");
            } else {
                sql = sql.append(" c.PRODUCT_NAME,c.COMPONENT_CODE,c.PRODUCTION_LINE_NAME,c.PROCEDURE_NAME,c.LINE_NUMBER,c.CIRCUIT_BOARD_CODE,c.SERIAL_NUMBER,c.DTCOL1,c.PRODUCT_CODE,c.MACHINE_TYPE,c.badNumber,c.resultType");
            }
            sql.append(" FROM ((");
            sql.append("SELECT mstProduct.PRODUCT_NAME,mstComponent.COMPONENT_CODE,mstProductionLine.PRODUCTION_LINE_NAME,mstCircuitBoardProcedure.PROCEDURE_NAME,"
                    + " log.LINE_NUMBER,log.CIRCUIT_BOARD_CODE,log.SERIAL_NUMBER,log.dtcol1,mstProduct.PRODUCT_CODE,log.MACHINE_TYPE,"
                    + " SUM(CASE WHEN log.txtcol8 IS NOT NULL AND log.txtcol8 <> '' AND log.txtcol8 > 0 THEN 1 ELSE (CASE WHEN (log.txtcol5 like '%NG%') THEN 1 ELSE 0 END) END) badNumber, "
                    + " '1' resultType"
                    + " FROM tbl_automatic_machine_log log "
                    + " LEFT JOIN mst_production_line mstProductionLine ON mstProductionLine.PRODUCTION_LINE_ID = log.LINE_NUMBER "
                    + " LEFT JOIN mst_component mstComponent ON mstComponent.COMPONENT_CODE = log.CIRCUIT_BOARD_CODE "
                    + " LEFT JOIN mst_product_component mstProductComponent ON mstProductComponent.COMPONENT_ID = mstComponent.ID "
                    + " LEFT JOIN mst_product mstProduct ON mstProduct.PRODUCT_ID = mstProductComponent.PRODUCT_ID "
                    + " LEFT JOIN mst_machine_procedure mstMachineProcedure ON mstMachineProcedure.MACHINE_UUID = log.MACHINE_UUID "
                    + " LEFT JOIN mst_circuit_board_procedure mstCircuitBoardProcedure ON mstCircuitBoardProcedure.ID = mstMachineProcedure.PROCEDURE_ID "
                    + " WHERE log.MACHINE_TYPE IN ('");
            sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI);
            sql.append("', '");
            sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI_NG);
            sql.append("', '");
            sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_ICT);
            sql.append("', '");
            sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT);
            sql.append("', '");
            sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FP_ICT_NG);
            sql.append("', '");
            sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_FCT);
            sql.append("')");

            if (!StringUtils.isEmpty(productCode)) {
                sql = sql.append(" AND mstProduct.product_code = ? ");
            }

            if (productionLineNo != 0) {
                sql = sql.append(" AND log.LINE_NUMBER = ? ");
            }

            if (!StringUtils.isEmpty(componentCode)) {
                sql = sql.append(" AND log.CIRCUIT_BOARD_CODE = ? ");
            }

            if (!StringUtils.isEmpty(serialNumber)) {
                sql = sql.append(" AND log.SERIAL_NUMBER LIKE ? ");
            }

            if (formatCheckDateStart != null) {
                sql = sql.append(" and log.dtcol1 >= ? ");
            }

            if (formatCheckDateEnd != null) {
                sql = sql.append(" and log.dtcol1 <= ? ");
            }

            if (!StringUtils.isEmpty(engineereGrids)) {
                sql = sql.append(" AND mstCircuitBoardProcedure.id IN (");
                sql.append(engineereGrids);
            }
            sql = sql.append(" AND (((log.MACHINE_TYPE = 'AOI' OR log.MACHINE_TYPE = 'AOI_NG') AND log.txtcol8 IS NOT NULL AND log.txtcol8 <> '' AND log.txtcol8 > 0) OR "
                + " (log.MACHINE_TYPE <> 'AOI' AND log.MACHINE_TYPE <> 'AOI_NG' AND log.txtcol5 LIKE '%NG%')) ");
            //検査数分组：自動機タイプ、基板コード、検査日組み合わせ集計キーとしての数
            sql = sql.append(" GROUP BY log.LINE_NUMBER,log.MACHINE_TYPE,log.CIRCUIT_BOARD_CODE,log.SERIAL_NUMBER,log.dtcol1");
            //検査日の降順、機種名、基板名の昇順
//            sql = sql.append(" ORDER BY log.dtcol1 DESC,mstProduct.PRODUCT_NAME ASC,mstComponent.COMPONENT_CODE ASC");
            sql.append(") UNION ALL (");
            sql.append(" SELECT mstProduct.PRODUCT_NAME,mstComponent.COMPONENT_CODE,'',mstCircuitBoardProcedure.PROCEDURE_NAME,'',mstComponent.id,serial.SERIAL_NUMBER,"
                    + " result.INSPECTION_DATE,mstProductComponent.COMPONENT_ID,'',SUM(CASE WHEN result.INSPECTION_RESULT > 0 THEN 1 ELSE 0 END) badNumber, "
                    + " '2' resultType"
                    + " FROM tbl_circuit_board_inspection_result result"
                    + " LEFT JOIN mst_circuit_board_serial_number serial ON serial.SERIAL_NUMBER = result.SERIAL_NUMBER "
                    + " LEFT JOIN mst_component mstComponent ON mstComponent.ID = result.COMPONENT_ID "
                    + " LEFT JOIN mst_product_component mstProductComponent ON mstProductComponent.COMPONENT_ID = result.COMPONENT_ID"
                    + " LEFT JOIN mst_product mstProduct ON mstProduct.PRODUCT_ID = mstProductComponent.PRODUCT_ID "
                    + " LEFT JOIN mst_circuit_board_procedure mstCircuitBoardProcedure ON mstCircuitBoardProcedure.ID = result.PROCEDURE_ID "
                    + " WHERE 1=1");
            if (!StringUtils.isEmpty(productCode)) {
                sql = sql.append(" AND mstProduct.product_code = ? ");
            }

            if (!StringUtils.isEmpty(componentCode)) {
                sql = sql.append(" AND mstComponent.COMPONENT_CODE = ? ");
            }

            if (!StringUtils.isEmpty(serialNumber)) {
                sql = sql.append(" AND serial.SERIAL_NUMBER LIKE ? ");
            }

            if (formatCheckDateStart != null) {
                sql = sql.append(" and result.INSPECTION_DATE >= ? ");
            }

            if (formatCheckDateEnd != null) {
                sql = sql.append(" and result.INSPECTION_DATE <= ? ");
            }

            if (!StringUtils.isEmpty(engineereGrids)) {
                sql = sql.append(" AND mstCircuitBoardProcedure.id IN (");
                sql.append(engineereGrids);
            }
            sql = sql.append(" AND result.INSPECTION_RESULT > 0 ");
            //検査数分组：自動機タイプ、基板コード、検査日組み合わせ集計キーとしての数
            sql = sql.append(" GROUP BY mstComponent.id,serial.SERIAL_NUMBER,result.INSPECTION_DATE,result.PROCEDURE_ID ");
            //検査日の降順、機種名、基板名の昇順
//            sql = sql.append(" ORDER BY result.INSPECTION_DATE DESC,mstProduct.PRODUCT_NAME ASC,mstComponent.COMPONENT_CODE ASC");
            sql.append(" )) c");

            if (!isCount) {
                if (StringUtils.isNotEmpty(sidx)) {
                    if (orderKey.get(sidx) != null) {
                        sql = sql.append(orderKey.get(sidx));
                        if (StringUtils.isNotEmpty(sord)) {
                            sql = sql.append(sord);
                        }
                    }
                } else {
                    //検査日の降順、機種名、基板名の昇順
                    sql = sql.append(" ORDER BY c.dtcol1 DESC,c.PRODUCT_NAME ASC,c.COMPONENT_CODE ASC");
                }
            }

            Query query = entityManager.createNativeQuery(sql.toString());

            int i = 1;
            if (!StringUtils.isEmpty(productCode)) {
                query.setParameter(index++, productCode.trim());
            }

            if (productionLineNo != 0) {
                query.setParameter(index++, productionLineNo);
            }

            if (!StringUtils.isEmpty(componentCode)) {
                query.setParameter(index++, componentCode.trim());
            }

            if (!StringUtils.isEmpty(serialNumber)) {
                query.setParameter(index++, "%" + serialNumber.trim() + "%");
            }

            if (formatCheckDateStart != null) {
                query.setParameter(index++, formatCheckDateStart);
            }

            if (formatCheckDateEnd != null) {
                query.setParameter(index++, formatCheckDateEnd);
            }

            if (!StringUtils.isEmpty(productCode)) {
                query.setParameter(index++, productCode.trim());
            }

            if (!StringUtils.isEmpty(componentCode)) {
                query.setParameter(index++, componentCode.trim());
            }

            if (!StringUtils.isEmpty(serialNumber)) {
                query.setParameter(index++, "%" + serialNumber.trim() + "%");
            }

            if (formatCheckDateStart != null) {
                query.setParameter(index++, formatCheckDateStart);
            }

            if (formatCheckDateEnd != null) {
                query.setParameter(index++, formatCheckDateEnd);
            }

            if (!isCount) {
                Pager pager = new Pager();
                query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
                query.setMaxResults(pageSize);
            }

            List list = query.getResultList();

            return list;
        }
    }

    /**
     * 基板検査結果 SQL
     *
     * @param productCode
     * @param componentId
     * @param streamNumber
     * @param engineereGrids
     * @param formatCheckDate
     * @return
     */
    private List getResultSql(
            String productCode,
            String componentCode,
            String serialNumber,
            String engineereGrids,
            Date formatCheckDate
    ) {

        StringBuilder sql;
        sql = new StringBuilder("SELECT mstProduct.productName,mstComponent.componentCode,'',mstCircuitBoardProcedure.procedureName,serial.serialNumber,"
                + " result.inspectionDate,mstComponent.componentCode,result.inspectionResult,detail.tblCircuitBoardInspectionResultDetailPK.defectiveItemId,detail.repairContent"
                + " FROM TblCircuitBoardInspectionResult result "
                + " LEFT JOIN TblCircuitBoardInspectionResultDetail detail ON detail.tblCircuitBoardInspectionResultDetailPK.circuitBoardInspectionResultId = result.circuitBoardInspectionResultId ");
        sql.append(" LEFT JOIN result.mstCircuitBoardSerialNumber serial "
                + " LEFT JOIN result.mstComponent mstComponent "
                + " LEFT JOIN result.mstProductComponent mstProductComponent "
                + " LEFT JOIN MstProduct mstProduct ON mstProduct.productId = mstProductComponent.productId "
                + " LEFT JOIN result.mstCircuitBoardProcedure mstCircuitBoardProcedure "
                + " WHERE 1=1");

        if (!StringUtils.isEmpty(productCode)) {
            sql = sql.append(" AND mstProduct.productCode = :productCode ");
        }

        if (!StringUtils.isEmpty(componentCode)) {
            sql = sql.append(" AND mstComponent.componentCode = :componentCode ");
        }

        if (!StringUtils.isEmpty(serialNumber)) {
            sql = sql.append(" AND serial.serialNumber = :serialNumber ");
        }

        if (formatCheckDate != null) {
            sql = sql.append(" and result.inspectionDate = :formatCheckDate ");
        }

        if (!StringUtils.isEmpty(engineereGrids)) {
            sql = sql.append(" AND mstCircuitBoardProcedure.id IN (:engineereList) ");
        }
        sql = sql.append(" AND result.inspectionResult > 0");

        //検査日の降順、機種名、基板名の昇順
        sql = sql.append(" ORDER BY result.inspectionDate DESC,mstProduct.productName ASC,mstComponent.componentCode ASC");

        Query query = entityManager.createQuery(sql.toString());

        if (!StringUtils.isEmpty(productCode)) {
            query.setParameter("productCode", productCode.trim());
        }

        if (!StringUtils.isEmpty(componentCode)) {
            query.setParameter("componentCode", componentCode.trim());
        }

        if (!StringUtils.isEmpty(serialNumber)) {
            query.setParameter("serialNumber", serialNumber.trim());
        }

        if (formatCheckDate != null) {
            query.setParameter("formatCheckDate", formatCheckDate);
        }

        if (!StringUtils.isEmpty(engineereGrids)) {
            query.setParameter("engineereList", engineereGrids);
        }

        List list = query.getResultList();

        return list;
    }

    /**
     * 基板工程別不良情報詳細取得
     *
     * @param productCode
     * @param productionLineNo
     * @param componentId
     * @param serialNumber
     * @param formatCheckDate
     * @param resultType
     * @param loginUser
     * @return
     */
    public DefectProcedureList getDefectProcedureDetail(
            String productCode,
            int productionLineNo,
            String componentCode,
            String serialNumber,
            Date formatCheckDate,
            String resultType,
            LoginUser loginUser
    ) {
        DefectProcedureList defectProcedureList = new DefectProcedureList();
        List<DefectProcedureVo> defectProcedureVos = new ArrayList();
        if (StringUtils.isNotEmpty(resultType) && Integer.parseInt(resultType) == 1) {
            //自動機取得
            List list = getSql(productCode, productionLineNo, componentCode, serialNumber, null, formatCheckDate);
            if (list != null && list.size() > 0) {
                defectProcedureList.setDefectProcedureVos(setDefectProcedureValue(list, 1, defectProcedureVos, true, loginUser.getLangId()));
            }
        } else {
            //基板検査結果取得
            List resultList = getResultSql(productCode, componentCode, serialNumber, null, formatCheckDate);

            if (resultList != null && resultList.size() > 0) {
                defectProcedureList.setDefectProcedureVos(setDefectProcedureValue(resultList, 2, defectProcedureVos, true, loginUser.getLangId()));
            }
        }

        return defectProcedureList;
    }

    /**
     * 文言
     *
     * @param langId
     * @return
     */
    private Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("product_name", "inspection_date", "serial_number", "product_line_name", "circuit_board_name",
                "defected_count", "circuit_board_procedure_name", "inspection_setp", "pin_number", "measurement_result");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        return headerMap;
    }

    /**
     * 基板工程別不良情報CSV出力
     *
     * @param productCode
     * @param productionLineNo
     * @param componentCode
     * @param formatCheckDateStart
     * @param formatCheckDateEnd
     * @param serialNumber
     * @param engineereGrids
     * @param loginUser
     * @return
     */
    public FileReponse getDefectProcedureCsvOutPut(
            String productCode,
            int productionLineNo,
            String componentCode,
            Date formatCheckDateStart,
            Date formatCheckDateEnd,
            String serialNumber,
            String engineereGrids,
            LoginUser loginUser
    ) {
        DefectProcedureList defectProcedureList = getDefectProcedureList(
                productCode,
                productionLineNo,
                componentCode,
                formatCheckDateStart,
                formatCheckDateEnd,
                serialNumber,
                engineereGrids,
                null,
                null,
                0,
                0,
                loginUser);
        FileReponse fileReponse = new FileReponse();
        ArrayList lineList;
        String uuid = IDGenerator.generate();
        String langId = loginUser.getLangId();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        // ヘッダー種取得
        Map<String, String> headerMap = getDictionaryList(langId);

        /*Head*/
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        headList.add(headerMap.get("product_name"));  //機種名
        headList.add(headerMap.get("circuit_board_name")); //基板名
        headList.add(headerMap.get("product_line_name"));  //生産ライン
        headList.add(headerMap.get("serial_number")); //シリアルナンバー
        headList.add(headerMap.get("circuit_board_procedure_name")); //工程名
        headList.add(headerMap.get("inspection_date")); //検査日
        headList.add(headerMap.get("defected_count")); //不良件数
        gLineList.add(headList);

        if (defectProcedureList != null && defectProcedureList.getDefectProcedureVos() != null && defectProcedureList.getDefectProcedureVos().size() > 0) {
            for (int i = 0; i < defectProcedureList.getDefectProcedureVos().size(); i++) {
                DefectProcedureVo defectProcedureVo = defectProcedureList.getDefectProcedureVos().get(i);
                lineList = new ArrayList();
                lineList.add(defectProcedureVo.getProductName()); //機種名
                lineList.add(defectProcedureVo.getComponentName()); //基板名
                lineList.add(defectProcedureVo.getProductionLineName()); //生産ライン
                lineList.add(defectProcedureVo.getSerialNumber()); //シリアルナンバー
                lineList.add(defectProcedureVo.getEngineereName()); //工程名
                lineList.add(defectProcedureVo.getCheckDateText()); //検査日
                lineList.add(String.valueOf(defectProcedureVo.getBadNumber())); //不良件数
                gLineList.add(lineList);
            }
        }

        CSVFileUtil.writeCsv(outCsvPath, gLineList);
        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportDate(new Date());
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "circuit_board_procedure_defect_list");
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fileReponse.setFileUuid(uuid);

        return fileReponse;
    }
}
