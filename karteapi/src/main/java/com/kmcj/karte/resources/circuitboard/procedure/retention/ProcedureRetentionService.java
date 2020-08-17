/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.procedure.retention;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
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
 * @author zds
 */
@Dependent
public class ProcedureRetentionService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("productName", " ORDER BY c.product_name "); // 機種名
        orderKey.put("componentName", " ORDER BY c.component_code "); // 基板名
        orderKey.put("procedureName", " ORDER BY c.procedure_name "); // 工程名
        orderKey.put("retentionNumber", " ORDER BY c.retention_number "); // 不良件数
    }

    /**
     * 基板工程滞留一覧取得
     *
     * @param productCode
     * @param procedureId
     * @param componentCode
     * @param formatStartDate
     * @param formatEndDate
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param loginUser
     * @return
     */
    public ProcedureRetentionList getProcedureRetentionList(
            String productCode,
            String procedureId,
            String componentCode,
            Date formatStartDate,
            Date formatEndDate,
            String sidx,//order Key
            String sord,//order 順
            int pageNumber,
            int pageSize,
            LoginUser loginUser
    ) {
        ProcedureRetentionList procedureRetentionList = new ProcedureRetentionList();
        List<ProcedureRetentionVo> procedureRetentionVos = new ArrayList();
        
        List counts = getUnionSql(productCode, procedureId, componentCode, formatStartDate, formatEndDate, true, null, null, pageNumber, pageSize);
        // ページをめぐる
        Pager pager = new Pager();
        procedureRetentionList.setPageNumber(pageNumber);
        long count = (long) counts.get(0);
        procedureRetentionList.setCount(count);
        procedureRetentionList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt(count + "")));
        if (count > 0) {
            //自動機/基板検査結果取得
            List list = getUnionSql(productCode, procedureId, componentCode, formatStartDate, formatEndDate, false, sidx, sord, pageNumber, pageSize);

            if (list != null && list.size() > 0) {
                procedureRetentionList.setProcedureRetentionVos(setProcedureRetentionValue(list, procedureRetentionVos));
            }
        }

        return procedureRetentionList;
    }

    /**
     * 基板工程滞留状況一覧値設定
     *
     * @param list
     * @param procedureRetentionVos
     * @return
     */
    private List<ProcedureRetentionVo> setProcedureRetentionValue(
            List list,
            List<ProcedureRetentionVo> procedureRetentionVos
    ) {
        ProcedureRetentionVo procedureRetentionVo;
        
        for (Object obj : list) {
            Object[] procedureRetention = (Object[]) obj;
            procedureRetentionVo = new ProcedureRetentionVo();
            // 機種名
            if (procedureRetention[0] != null && StringUtils.isNotEmpty(String.valueOf(procedureRetention[0]))) {
                procedureRetentionVo.setProductName(String.valueOf(procedureRetention[0]));
            } else {
                procedureRetentionVo.setProductName(null);
            }
            // 基板名
            if (procedureRetention[1] != null && StringUtils.isNotEmpty(String.valueOf(procedureRetention[1]))) {
                procedureRetentionVo.setComponentName(String.valueOf(procedureRetention[1]));
            } else {
                procedureRetentionVo.setComponentName(null);
            }
            // 工程名
            if (procedureRetention[2] != null && StringUtils.isNotEmpty(String.valueOf(procedureRetention[2]))) {
                procedureRetentionVo.setProcedureName(String.valueOf(procedureRetention[2]));
            } else {
                procedureRetentionVo.setProcedureName(null);
            }
            // 台数
            if (procedureRetention[3] != null) {
                procedureRetentionVo.setRetentionNumber(Integer.valueOf(procedureRetention[3].toString()));
            } else {
                procedureRetentionVo.setRetentionNumber(0);
            }
            
            procedureRetentionVos.add(procedureRetentionVo);
        }
        return procedureRetentionVos;
    }

    /**
     * 基板工程滞留状況一覧 SQL
     *
     * @return
     */
    private List getUnionSql(
            String productCode,
            String procedureId,
            String componentCode,
            Date formatStartDate,
            Date formatEndDate,
            boolean isCount,
            String sidx,//order Key
            String sord,//order 順
            int pageNumber,
            int pageSize
    ) {
        int index = 1;
        StringBuilder sql = new StringBuilder(" SELECT");
        if (isCount) {
            sql = sql.append(" COUNT(1) ");
        } else {
            sql = sql.append(" c.product_name,c.component_code,c.procedure_name,c.retention_number");
        }
        sql.append(" FROM ((");
        sql.append("SELECT mstProduct.product_name,mstComponent.component_code,mstCircuitBoardProcedure.procedure_name,"
            + " COUNT(1) retention_number "
            + " FROM tbl_automatic_machine_log log "
            + " LEFT JOIN mst_component mstComponent ON mstComponent.component_code = log.circuit_board_code "
            + " LEFT JOIN mst_product_component mstProductComponent ON mstProductComponent.component_id = mstComponent.id "
            + " LEFT JOIN mst_product mstProduct ON mstProduct.product_id = mstProductComponent.product_id "
            + " LEFT JOIN mst_machine_procedure mstMachineProcedure ON mstMachineProcedure.machine_uuid = log.machine_uuid "
            + " LEFT JOIN mst_circuit_board_procedure mstCircuitBoardProcedure ON mstCircuitBoardProcedure.id = mstMachineProcedure.procedure_id "
            + " WHERE 1=1 ");

        if (!StringUtils.isEmpty(productCode)) {
            sql = sql.append(" AND mstProduct.product_code = ? ");
        }

        if (!StringUtils.isEmpty(procedureId)) {
            sql = sql.append(" AND mstCircuitBoardProcedure.id = ? ");
        }

        if (!StringUtils.isEmpty(componentCode)) {
            sql = sql.append(" AND log.circuit_board_code = ? ");
        }

        if (formatStartDate != null) {
            sql = sql.append(" AND ((log.machine_type = 'SMT' AND log.log_type = 'C' AND log.dtcol2 >= ?) ");
            sql = sql.append(" OR (log.machine_type <> 'SMT' AND log.dtcol1 >= ?)) ");
        }

        if (formatEndDate != null) {
            sql = sql.append(" AND ((log.machine_type = 'SMT' AND log.log_type = 'C' AND log.dtcol2 <= ?) ");
            sql = sql.append(" OR (log.machine_type <> 'SMT' AND log.dtcol1 <= ?)) ");
        }
        
        if (formatStartDate == null && formatEndDate == null) {
            sql = sql.append(" AND ((log.machine_type = 'SMT' AND log.log_type = 'C') OR log.machine_type <> 'SMT') ");
        }

        // 機種名、基板名、工程で組み合わせ集計キーとしての数
        sql = sql.append(" GROUP BY mstProduct.product_name,mstComponent.component_code,mstCircuitBoardProcedure.procedure_name");
        sql.append(") UNION ALL (");
        sql.append(" SELECT mstProduct.product_name,mstComponent.component_code,mstCircuitBoardProcedure.procedure_name,"
                + " COUNT(1) retention_number "
                + " FROM tbl_circuit_board_inspection_result result"
                + " LEFT JOIN mst_component mstComponent ON mstComponent.id = result.component_id "
                + " LEFT JOIN mst_product_component mstProductComponent ON mstProductComponent.component_id = result.component_id"
                + " LEFT JOIN mst_product mstProduct ON mstProduct.product_id = mstProductComponent.product_id "
                + " LEFT JOIN mst_circuit_board_procedure mstCircuitBoardProcedure ON mstCircuitBoardProcedure.id = result.procedure_id "
                + " WHERE 1=1");
        if (!StringUtils.isEmpty(productCode)) {
            sql = sql.append(" AND mstProduct.product_code = ? ");
        }

        if (!StringUtils.isEmpty(procedureId)) {
            sql = sql.append(" AND mstCircuitBoardProcedure.id = ? ");
        }
        
        if (!StringUtils.isEmpty(componentCode)) {
            sql = sql.append(" AND mstComponent.component_code = ? ");
        }

        if (formatStartDate != null) {
            sql = sql.append(" and result.inspection_date >= ? ");
        }

        if (formatEndDate != null) {
            sql = sql.append(" and result.inspection_date <= ? ");
        }
        
        //検査数分组：自動機タイプ、基板コード、検査日組み合わせ集計キーとしての数
        sql = sql.append(" GROUP BY mstProduct.product_name,mstComponent.component_code,mstCircuitBoardProcedure.procedure_name");
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
                // 機種名、基板名の昇順
                sql = sql.append(" ORDER BY c.product_name ASC,c.component_code ASC");
            }
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        if (!StringUtils.isEmpty(productCode)) {
            query.setParameter(index++, productCode.trim());
        }

        if (!StringUtils.isEmpty(procedureId)) {
            query.setParameter(index++, procedureId.trim());
        }

        if (!StringUtils.isEmpty(componentCode)) {
            query.setParameter(index++, componentCode.trim());
        }

        if (formatStartDate != null) {
            query.setParameter(index++, formatStartDate);
            query.setParameter(index++, formatStartDate);
        }

        if (formatEndDate != null) {
            query.setParameter(index++, formatEndDate);
            query.setParameter(index++, formatEndDate);
        }
        
        if (!StringUtils.isEmpty(productCode)) {
            query.setParameter(index++, productCode.trim());
        }

        if (!StringUtils.isEmpty(procedureId)) {
            query.setParameter(index++, procedureId.trim());
        }

        if (!StringUtils.isEmpty(componentCode)) {
            query.setParameter(index++, componentCode.trim());
        }

        if (formatStartDate != null) {
            query.setParameter(index++, formatStartDate);
        }

        if (formatEndDate != null) {
            query.setParameter(index++, formatEndDate);
        }

        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        List list = query.getResultList();

        return list;
    }

    /**
     * 文言
     *
     * @param langId
     * @return
     */
    private Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("product_name", "circuit_board_name",
            "circuit_board_procedure_name", "retention_number");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        return headerMap;
    }

    /**
     * 基板工程滞留状況CSV出力
     *
     * @param productCode
     * @param procedureId
     * @param componentCode
     * @param formatStartDate
     * @param formatEndDate
     * @param loginUser
     * @return
     */
    public FileReponse getProcedureRetentionCsvOutPut(
            String productCode,
            String procedureId,
            String componentCode,
            Date formatStartDate,
            Date formatEndDate,
            LoginUser loginUser
    ) {
        ProcedureRetentionList procedureRetentionList = getProcedureRetentionList(
                productCode,
                procedureId,
                componentCode,
                formatStartDate,
                formatEndDate,
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
        headList.add(headerMap.get("circuit_board_procedure_name")); //工程名
        headList.add(headerMap.get("retention_number")); //台数
        
        gLineList.add(headList);

        if (procedureRetentionList != null && procedureRetentionList.getProcedureRetentionVos()!= null && procedureRetentionList.getProcedureRetentionVos().size() > 0) {
            for (int i = 0; i < procedureRetentionList.getProcedureRetentionVos().size(); i++) {
                ProcedureRetentionVo procedureRetentionVo = procedureRetentionList.getProcedureRetentionVos().get(i);
                lineList = new ArrayList();
                lineList.add(procedureRetentionVo.getProductName()); //機種名
                lineList.add(procedureRetentionVo.getComponentName()); //基板名
                lineList.add(procedureRetentionVo.getProcedureName()); //工程名
                lineList.add(String.valueOf(procedureRetentionVo.getRetentionNumber())); //台数
                gLineList.add(lineList);
            }
        }

        CSVFileUtil.writeCsv(outCsvPath, gLineList);
        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MST_MOLD_ATTRIBUTE);
        tblCsvExport.setExportDate(new Date());
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "circuit_board_procedure_retention_list");
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fileReponse.setFileUuid(uuid);

        return fileReponse;
    }
}
