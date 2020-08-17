/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.defect.inspection.detail;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
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
 * @author admin
 */
@Dependent
public class DefectInspectionService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;
    
    @Inject
    private MstChoiceService mstChoiceService;
    
    private final static Map<String, String> orderKey;
    static {
        orderKey = new HashMap<>();
        orderKey.put("productName", " ORDER BY mstProduct.productName "); // 機種名
        orderKey.put("circuitBoardName", " ORDER BY mstComponent.componentCode "); // 基板名
        orderKey.put("productionLineName", " ORDER BY mstProductionLine.productionLineName "); // 生産ライン
        orderKey.put("inspectionDateStr", " ORDER BY log.dtcol1 "); // 検索日
        orderKey.put("checkNumber", " ORDER BY checkNumber "); // 検索数
        orderKey.put("aSideBadNumber", " ORDER BY aSideBadNumber "); // A面不良数
        orderKey.put("bSideBadNumber", " ORDER BY bSideBadNumber "); // B面不良数
    }

    /**
     * 基板外観検査不良情報一覧取得
     *
     * @param productCode
     * @param productionLineNo
     * @param componentCode
     * @param formatCheckDateStart
     * @param formatCheckDateEnd
     * @param sidx
     * @param pageNumber
     * @param sord
     * @param pageSize
     * @return
     */
    public DefectInspectionList getDefectInspectionList(
            String productCode,
            int productionLineNo,
            String componentCode,
            Date formatCheckDateStart,
            Date formatCheckDateEnd,
            String sidx,//order Key
            String sord,//order 順
            int pageNumber,
            int pageSize
    ) {
        DefectInspectionList defectInspectionList = new DefectInspectionList();
        DefectInspectionVo defectInspectionVo;
        List<DefectInspectionVo> defectInspectionVos = new ArrayList();
        List counts = getSql(productCode, productionLineNo, componentCode, formatCheckDateStart, formatCheckDateEnd, null, false, 0, true, sidx, sord, pageNumber, pageSize);
        // ページをめぐる
        Pager pager = new Pager();
        defectInspectionList.setPageNumber(pageNumber);
        long count =  (long) counts.size();
        defectInspectionList.setCount(count);
        defectInspectionList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + count)));
        if (count > 0) {
            List list = getSql(productCode, productionLineNo, componentCode, formatCheckDateStart, formatCheckDateEnd, null, false, 0, false, sidx, sord, pageNumber, pageSize);
            if (list != null && list.size() > 0) {
                FileUtil fu = new FileUtil();
                for (Object obj : list) {
                    Object[] appearanceInspectionDefect = (Object[]) obj;
                    defectInspectionVo = new DefectInspectionVo();
                    // 製品コード
                    if (appearanceInspectionDefect[1] != null && StringUtils.isNotEmpty(String.valueOf(appearanceInspectionDefect[1]))) {
                        defectInspectionVo.setProductCode(String.valueOf(appearanceInspectionDefect[1]));
                    } else {
                        defectInspectionVo.setProductCode(null);
                    }
                    // 機種名
                    if (appearanceInspectionDefect[2] != null && StringUtils.isNotEmpty(String.valueOf(appearanceInspectionDefect[2]))) {
                        defectInspectionVo.setProductName(String.valueOf(appearanceInspectionDefect[2]));
                    } else {
                        defectInspectionVo.setProductName(null);
                    }
                    // 基板コード
                    if (appearanceInspectionDefect[3] != null && StringUtils.isNotEmpty(String.valueOf(appearanceInspectionDefect[3]))) {
                        defectInspectionVo.setCircuitBoardCode(String.valueOf(appearanceInspectionDefect[3]));
                    } else {
                        defectInspectionVo.setCircuitBoardCode(null);
                    }
                    // 基板名称
                    if (appearanceInspectionDefect[4] != null && StringUtils.isNotEmpty(String.valueOf(appearanceInspectionDefect[4]))) {
                        defectInspectionVo.setCircuitBoardName(String.valueOf(appearanceInspectionDefect[4]));
                    } else {
                        defectInspectionVo.setCircuitBoardName(null);
                    }
                    // ラインNo
                    if (appearanceInspectionDefect[5] != null && StringUtils.isNotEmpty(String.valueOf(appearanceInspectionDefect[5]))) {
                        defectInspectionVo.setProductionLineNo(String.valueOf(appearanceInspectionDefect[5]));
                    } else {
                        defectInspectionVo.setProductionLineNo(null);
                    }
                    // ライン名称
                    if (appearanceInspectionDefect[6] != null && StringUtils.isNotEmpty(String.valueOf(appearanceInspectionDefect[6]))) {
                        defectInspectionVo.setProductionLineName(String.valueOf(appearanceInspectionDefect[6]));
                    } else {
                        defectInspectionVo.setProductionLineName(null);
                    }
                    // 検査日
                    if (appearanceInspectionDefect[7] != null && StringUtils.isNotEmpty(String.valueOf(appearanceInspectionDefect[7]))) {
                        defectInspectionVo.setInspectionDateStr(fu.getDateFormatForStr(appearanceInspectionDefect[7]));
                        defectInspectionVo.setInspectionDate(fu.getDateTimeFormatForStr(appearanceInspectionDefect[7]));
                    } else {
                        defectInspectionVo.setInspectionDateStr(null);
                        defectInspectionVo.setInspectionDate(null);
                    }
                    // 検査数
                    defectInspectionVo.setCheckNumber(Integer.parseInt(String.valueOf(appearanceInspectionDefect[0])));
                    //A面不良数
                    defectInspectionVo.setaSideBadNumber(Integer.parseInt(String.valueOf(appearanceInspectionDefect[8])));
                    //B面不良数
                    defectInspectionVo.setbSideBadNumber(Integer.parseInt(String.valueOf(appearanceInspectionDefect[9])));
                    defectInspectionVos.add(defectInspectionVo);
                }
                defectInspectionList.setAppearanceInspectionDefectVos(defectInspectionVos);
            }
        }
        return defectInspectionList;
    }

    /**
     * 基板外観検査不良情報一覧 SQL
     *
     * @param productCode
     * @param productionLineNo
     * @param componentCode
     * @param formatCheckDateStart
     * @param formatCheckDateEnd
     * @return
     */
    private List getSql(
            String productCode,
            int productionLineNo,
            String componentCode,
            Date formatCheckDateStart,
            Date formatCheckDateEnd,
            Date detailCheckDate,
            boolean detailFlag,
            int defectMendFlg,
            boolean isCount,
            String sidx,//order Key
            String sord,//order 順
            int pageNumber,
            int pageSize
    ) {

        StringBuilder sql = new StringBuilder();
        if (!detailFlag) {
            if (isCount) {
                sql.append(" SELECT 1 ");
            } else {
                sql.append(" SELECT COUNT(1) AS checkNumber, mstProduct.productCode, mstProduct.productName, mstComponent.componentCode");
                sql.append(", mstComponent.componentCode, log.lineNumber, mstProductionLine.productionLineName");
                sql.append(", log.dtcol1");
                sql.append(", SUM(CASE WHEN log.txtcol6 = 'A' AND log.txtcol8 IS NOT NULL AND log.txtcol8 <> '' AND log.txtcol8 > 0 THEN 1 ELSE 0 END) AS aSideBadNumber");
                sql.append(", SUM(CASE WHEN log.txtcol6 = 'B' AND log.txtcol8 IS NOT NULL AND log.txtcol8 <> '' AND log.txtcol8 > 0 THEN 1 ELSE 0 END) AS bSideBadNumber");
            }
        } else {
            sql.append(" SELECT log.dtcol1, mstProduct.productName, mstComponent.componentCode");
            sql.append(", mstProductionLine.productionLineName");
            sql.append(", log.txtcol6, log.txtcol7");
            if (defectMendFlg == 1) {
                sql.append(", log.txtcol8");
                sql.append(",SUM(CASE WHEN log.txtcol8 IS NOT NULL AND log.txtcol8 <> '' AND log.txtcol8 > 0 THEN 1 ELSE 0 END) AS componentNgNumber");
            } else if (defectMendFlg == 2) {
                sql.append(", log.txtcol10");
                sql.append(",SUM(CASE WHEN log.txtcol11 IS NOT NULL AND log.txtcol11 <> '' THEN 1 ELSE 0 END) AS componentMendNumber");
            }
        }

        sql.append(" FROM TblAutomaticMachineLog log");
        sql.append(" LEFT JOIN MstComponent mstComponent ON mstComponent.componentCode = log.circuitBoardCode");
        sql.append(" LEFT JOIN MstProductComponent mstProductComponent ON mstProductComponent.componentId = mstComponent.id");
        sql.append(" LEFT JOIN MstProduct mstProduct ON mstProduct.productId = mstProductComponent.productId");
        if (!isCount) {
            sql.append(" LEFT JOIN MstProductionLine mstProductionLine ON mstProductionLine.productionLineId = log.lineNumber");
        }
        sql.append(" WHERE log.machineType IN ('");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI);
        sql.append("', '");
        sql.append(CommonConstants.TBL_AUTOMATIC_MACHINE_LOG_TYPE_AOI_NG);
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
        
        sql = sql.append(" AND log.txtcol8 IS NOT NULL AND log.txtcol8 <> '' AND log.txtcol8 > 0  ");

        if (!detailFlag) {
            if (formatCheckDateStart != null) {
                sql = sql.append(" and log.dtcol1 >= :formatCheckDateStart ");
            }

            if (formatCheckDateEnd != null) {
                sql = sql.append(" and log.dtcol1 <= :formatCheckDateEnd ");
            }
            
            // グループ条件：ラインNo、基板コード、検査日組み合わせ
            sql = sql.append(" GROUP BY log.lineNumber, log.circuitBoardCode, log.dtcol1 ");
            if (!isCount) {

                if (StringUtils.isNotEmpty(sidx)) {
                    sql = sql.append(orderKey.get(sidx));
                    if (StringUtils.isNotEmpty(sord)) {
                        sql = sql.append(sord);
                    }
                } else {
                    // 検査日の降順、機種名、基板名の昇順
                    sql = sql.append(" ORDER BY log.dtcol1 DESC,mstProduct.productName ASC,mstComponent.componentCode ASC");
                }
            }
        } else {
            sql = sql.append(" and log.dtcol1 = :detailCheckDate");
            
            // 不良情報の場合
            if (defectMendFlg == 1) {
                // グループ条件：ラインNo、基板コード、検査日、面、部品名、検査機判定NGコード組み合わせ
                sql = sql.append(" GROUP BY log.lineNumber, log.circuitBoardCode, log.dtcol1, log.txtcol6, log.txtcol7, log.txtcol8 ");
            } else if (defectMendFlg == 2) {
                // グループ条件：ラインNo、基板コード、検査日、面、部品名、オペレーター判定結果組み合わせ
                sql = sql.append(" GROUP BY log.lineNumber, log.circuitBoardCode, log.dtcol1, log.txtcol6, log.txtcol7, log.txtcol11 ");
            }
            
            // 部品名の昇順
            sql = sql.append(" ORDER BY log.txtcol7 ASC");
        }

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

        if (formatCheckDateStart != null) {
            query.setParameter("formatCheckDateStart", formatCheckDateStart);
        }

        if (formatCheckDateEnd != null) {
            query.setParameter("formatCheckDateEnd", formatCheckDateEnd);
        }

        if (detailCheckDate != null) {
            query.setParameter("detailCheckDate", detailCheckDate);
        }
        
        if (!isCount && pageNumber != 0 && pageSize != 0) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }
        
        List list = query.getResultList();

        return list;
    }

    /**
     * 基板外観検査不良情報詳細取得
     *
     * @param productCode
     * @param productionLineNo
     * @param componentId
     * @param inspectionDate
     * @param defectMendFlg
     * @param langId
     * @return
     */
    public DefectInspectionList getDefectInspectionDetail(
            String productCode,
            int productionLineNo,
            String componentCode,
            Date inspectionDate,
            int defectMendFlg,
            String langId
    ) {
        DefectInspectionList appearanceInspectionDefectList = new DefectInspectionList();
        DefectInspectionVo defectInspectionVo;
        DefectInspectionDetailVo defectInspectionDetailVo;
        List<DefectInspectionDetailVo> defectInspectionDetailVos = new ArrayList();
        FileUtil fu = new FileUtil();
        List list = getSql(productCode, productionLineNo, componentCode, null, null, inspectionDate, true, defectMendFlg, false, null, null, 0, 0);
        if (list != null && !list.isEmpty()) {
            defectInspectionVo = new DefectInspectionVo();
            // 検査日
            if (((Object[])list.get(0))[0] != null && StringUtils.isNotEmpty(String.valueOf(((Object[])list.get(0))[0]))) {
                defectInspectionVo.setInspectionDateStr(fu.getDateFormatForStr(((Object[])list.get(0))[0]));
                defectInspectionVo.setInspectionDate(fu.getDateTimeFormatForStr(((Object[])list.get(0))[0]));
            } else {
                defectInspectionVo.setInspectionDateStr(null);
                defectInspectionVo.setInspectionDate(null);
            }
            // 機種名
            if (((Object[])list.get(0))[1] != null && StringUtils.isNotEmpty(String.valueOf(((Object[])list.get(0))[1]))) {
                defectInspectionVo.setProductName(String.valueOf(((Object[])list.get(0))[1]));
            } else {
                defectInspectionVo.setProductName(null);
            }
            // 基板名称
            if (((Object[])list.get(0))[2] != null && StringUtils.isNotEmpty(String.valueOf(((Object[])list.get(0))[2]))) {
                defectInspectionVo.setCircuitBoardName(String.valueOf(((Object[])list.get(0))[2]));
            } else {
                defectInspectionVo.setCircuitBoardName(null);
            }
            // ライン名称
            if (((Object[])list.get(0))[3] != null && StringUtils.isNotEmpty(String.valueOf(((Object[])list.get(0))[3]))) {
                defectInspectionVo.setProductionLineName(String.valueOf(((Object[])list.get(0))[3]));
            } else {
                defectInspectionVo.setProductionLineName(null);
            }
            // 不良内容Choice
            MstChoiceList ngEndCodeList = mstChoiceService.getChoice(langId, "tbl_automatic_machine_log.ng_end_code");
            // 不良/修理詳細
            for (Object obj : list) {
                Object[] appearanceInspectionDefect = (Object[]) obj;
                defectInspectionDetailVo = new DefectInspectionDetailVo();
                // 面
                if (appearanceInspectionDefect[4] != null && StringUtils.isNotEmpty(String.valueOf(appearanceInspectionDefect[4]))) {
                    defectInspectionDetailVo.setSide(String.valueOf(appearanceInspectionDefect[4]));
                } else {
                    defectInspectionDetailVo.setSide(null);
                }
                // 不良箇所/対応箇所
                if (appearanceInspectionDefect[5] != null && StringUtils.isNotEmpty(String.valueOf(appearanceInspectionDefect[5]))) {
                    defectInspectionDetailVo.setDefectPlace(String.valueOf(appearanceInspectionDefect[5]));
                    defectInspectionDetailVo.setMendPlace(String.valueOf(appearanceInspectionDefect[5]));
                } else {
                    defectInspectionDetailVo.setDefectPlace(null);
                    defectInspectionDetailVo.setMendPlace(null);
                }
                if (defectMendFlg == 1) {
                    // 不良内容
                    if (appearanceInspectionDefect[6] != null && StringUtils.isNotEmpty(String.valueOf(appearanceInspectionDefect[6]))) {
                        if (ngEndCodeList.getMstChoice() != null && !ngEndCodeList.getMstChoice().isEmpty()) {
                            for (MstChoice mstChoice : ngEndCodeList.getMstChoice()) {
                                if (mstChoice.getMstChoicePK().getSeq().equals(String.valueOf(appearanceInspectionDefect[6]))) {
                                    defectInspectionDetailVo.setDefectContent(mstChoice.getChoice());
                                    break;
                                }
                            }
                        } else {
                            defectInspectionDetailVo.setDefectContent(null);
                        }
                    } else {
                        defectInspectionDetailVo.setDefectContent(null);
                    }
                    // 不良件数
                    if (appearanceInspectionDefect[7] != null && !StringUtils.isEmpty(appearanceInspectionDefect[7].toString())) {
                        defectInspectionDetailVo.setDefectCount(Integer.valueOf(appearanceInspectionDefect[7].toString()));
                    } else {
                        defectInspectionDetailVo.setDefectCount(0);
                    }
                } else if (defectMendFlg == 2) {
                    // 対応内容
                    if (appearanceInspectionDefect[6] != null && StringUtils.isNotEmpty(String.valueOf(appearanceInspectionDefect[6]))) {
                        defectInspectionDetailVo.setMendContent(String.valueOf(appearanceInspectionDefect[6]));
                    } else {
                        defectInspectionDetailVo.setMendContent(null);
                    }
                    // 対応件数
                    if (appearanceInspectionDefect[7] != null && !StringUtils.isEmpty(appearanceInspectionDefect[7].toString())) {
                        defectInspectionDetailVo.setMendCount(Integer.valueOf(appearanceInspectionDefect[7].toString()));
                    } else {
                        defectInspectionDetailVo.setMendCount(0);
                    }
                }

                defectInspectionDetailVos.add(defectInspectionDetailVo);
            }
            appearanceInspectionDefectList.setDefectInspectionVo(defectInspectionVo);
            appearanceInspectionDefectList.setDefectInspectionDetailVos(defectInspectionDetailVos);
        }
        return appearanceInspectionDefectList;

    }

     /**
     * 基板工程別不良情報をCSV出力
     *
     * @param productCode
     * @param productionLineNo
     * @param componentId
     * @param formatCheckDateStart
     * @param formatCheckDateEnd
     * @param loginUser
     * @return
     */
    public FileReponse getAppearanceInspectionDefectCSV(
            String productCode,
            int productionLineNo,
            String componentCode,
            Date formatCheckDateStart,
            Date formatCheckDateEnd,
            LoginUser loginUser
    ) {
        FileReponse reponse = new FileReponse();

        //CSVファイル出力
        String fileUuid = IDGenerator.generate();
        String langId = loginUser.getLangId();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, fileUuid);

        Map<String, String> headerMap = getDictionaryList(langId);

        /*Head*/
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        headList.add(headerMap.get("product_name")); //機種名
        headList.add(headerMap.get("circuit_board_name")); //基板名
        headList.add(headerMap.get("product_line_name")); //生産ライン
        headList.add(headerMap.get("inspection_date")); //検査日
        headList.add(headerMap.get("inspection_count")); //検査数量
        headList.add("A" + headerMap.get("surface_defect_count")); //A面不良数
        headList.add("B" + headerMap.get("surface_defect_count")); //B面不良数
        gLineList.add(headList);

        DefectInspectionList appearanceInspectionDefectList = this.getDefectInspectionList(productCode, productionLineNo, componentCode, formatCheckDateStart, formatCheckDateEnd, null, null, 0, 0);
        ArrayList lineList;
        // 情報取得
        if (appearanceInspectionDefectList != null && appearanceInspectionDefectList.getAppearanceInspectionDefectVos()!= null && !appearanceInspectionDefectList.getAppearanceInspectionDefectVos().isEmpty()) {
            for (int i = 0; i < appearanceInspectionDefectList.getAppearanceInspectionDefectVos().size(); i++) {
                DefectInspectionVo appearanceInspectionDefectVo = appearanceInspectionDefectList.getAppearanceInspectionDefectVos().get(i);
                lineList = new ArrayList();
                lineList.add(appearanceInspectionDefectVo.getProductName()); //機種名
                lineList.add(appearanceInspectionDefectVo.getCircuitBoardName()); //基板名
                lineList.add(appearanceInspectionDefectVo.getProductionLineName()); //生産ライン
                lineList.add(appearanceInspectionDefectVo.getInspectionDateStr()); //検査日
                lineList.add(String.valueOf(appearanceInspectionDefectVo.getCheckNumber())); //検査数量
                lineList.add(String.valueOf(appearanceInspectionDefectVo.getaSideBadNumber())); //A面不良数
                lineList.add(String.valueOf(appearanceInspectionDefectVo.getbSideBadNumber())); //B面不良数
                gLineList.add(lineList);
            }
        }
        // csv 出力
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(fileUuid);
        tblCsvExport.setExportDate(new Date());
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());

        // ファイル名称
        String fileName = mstDictionaryService.getDictionaryValue(langId, "circuit_board_inspection_defect_list");
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        reponse.setFileUuid(fileUuid);
        return reponse;
    }

    private Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("product_name", "circuit_board_name", "product_line_name", "inspection_date", "inspection_count", "surface_defect_count");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        return headerMap;
    }


}
