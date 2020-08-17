/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.lot.relation;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.lot.TblComponentLot;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 * poサービス
 *
 * @author admin
 */
@Dependent
public class TblComponentLotRelationService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblCsvExportService tblCsvExportService;

    @Inject
    private KartePropertyService kartePropertyService;

    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("componentCode", " ORDER BY mstComponent.componentCode ");//
        orderKey.put("componentName", " ORDER BY mstComponent.componentName ");//
        orderKey.put("procedureCode", " ORDER BY tblComponentLotRelation.procedureCode ");//
        orderKey.put("componentLotNo", " ORDER BY tblComponentLot.lotNo ");//

        orderKey.put("subComponentCode", " ORDER BY subMstComponent.componentCode ");//
        orderKey.put("subComponentName", " ORDER BY subMstComponent.componentName ");//
        orderKey.put("subProcedureCode", " ORDER BY tblComponentLotRelation.subProcedureCode ");//
        orderKey.put("subComponentLotNo", " ORDER BY subTblComponentLot.lotNo ");//

        orderKey.put("createDate", " ORDER BY tblComponentLotRelation.createDate ");//
    }

    /**
     *
     * @param componentCode
     * @param subComponentCode
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @return
     */
    public TblComponentLotRelationVoList getTblComponentLotRelationList(String componentCode,
            String subComponentCode,
            String sidx,
            String sord,
            int pageNumber,
            int pageSize,
            boolean isPage) {

        TblComponentLotRelationVoList response = new TblComponentLotRelationVoList();
        if (isPage) {
            List count = getSql(componentCode, subComponentCode, true, sidx, sord, pageNumber, pageSize);
            // ページをめぐる
            Pager pager = new Pager();
            response.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            response.setCount(counts);
            response.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));
        }

        List list = getSql(componentCode, subComponentCode, false, sidx, sord, pageNumber, pageSize);

        List<TblComponentLotRelationVo> tblComponentLotRelationVoList = new ArrayList<>();
        TblComponentLotRelationVo tblComponentLotRelationVo;

        TblComponentLotRelation tblComponentLotRelation;
        for (int i = 0; i < list.size(); i++) {
            tblComponentLotRelationVo = new TblComponentLotRelationVo();
            tblComponentLotRelation = (TblComponentLotRelation) list.get(i);

            //部品コード    部品名称    部品ID
            MstComponent mstComponent = tblComponentLotRelation.getMstComponent();
            if (mstComponent != null) {
                tblComponentLotRelationVo.setComponentId(mstComponent.getId());
                tblComponentLotRelationVo.setComponentCode(mstComponent.getComponentCode());
                tblComponentLotRelationVo.setComponentName(mstComponent.getComponentName());
            } else {
                tblComponentLotRelationVo.setComponentId("");
                tblComponentLotRelationVo.setComponentCode("");
                tblComponentLotRelationVo.setComponentName("");
            }

            tblComponentLotRelationVo.setProcedureCode(FileUtil.getStringValue(tblComponentLotRelation.getProcedureCode()));

            //部品マスタ部品ID  //部品ロット番号
            TblComponentLot tblComponentLot = tblComponentLotRelation.getTblComponentLot();
            if (tblComponentLot != null) {
                tblComponentLotRelationVo.setComponentLotId(tblComponentLot.getUuid());
                tblComponentLotRelationVo.setComponentLotNo(tblComponentLot.getLotNo());
            } else {
                tblComponentLotRelationVo.setComponentLotId("");
                tblComponentLotRelationVo.setComponentLotNo("");
            }

            //構成部品Code  //構成部品Name
            MstComponent subMstComponent = tblComponentLotRelation.getSubMstComponent();
            if (subMstComponent != null) {
                tblComponentLotRelationVo.setSubComponentId(subMstComponent.getId());
                tblComponentLotRelationVo.setSubComponentCode(subMstComponent.getComponentCode());
                tblComponentLotRelationVo.setSubComponentName(subMstComponent.getComponentName());
            } else {
                tblComponentLotRelationVo.setSubComponentId("");
                tblComponentLotRelationVo.setSubComponentCode("");
                tblComponentLotRelationVo.setSubComponentName("");
            }
            tblComponentLotRelationVo.setSubProcedureCode(FileUtil.getStringValue(tblComponentLotRelation.getSubProcedureCode()));

            //構成部品ロットID  構成部品ロット番号\
            TblComponentLot subTblComponentLot = tblComponentLotRelation.getSubTblComponentLot();
            if (subTblComponentLot != null) {
                tblComponentLotRelationVo.setSubComponentLotId(subTblComponentLot.getUuid());
                tblComponentLotRelationVo.setSubComponentLotNo(subTblComponentLot.getLotNo());
            } else {
                tblComponentLotRelationVo.setSubComponentLotId("");
                tblComponentLotRelationVo.setSubComponentLotNo("");
            }

            //登録日
            tblComponentLotRelationVo.setCreateDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(tblComponentLotRelation.getCreateDate()));

            tblComponentLotRelationVo.setUuid(tblComponentLotRelation.getUuid());
            tblComponentLotRelationVo.setOperationFlag("2");

            tblComponentLotRelationVoList.add(tblComponentLotRelationVo);
        }
        response.setTblComponentLotRelationVos(tblComponentLotRelationVoList);
        return response;
    }

    /**
     *
     * @param componentCode
     * @param subComponentCode
     * @param langId
     * @param userUuid
     * @return
     */
    public FileReponse getTblComponentLotRelationCsv(String componentCode, String subComponentCode, String langId, String userUuid) {
        /**
         * Header
         */
        ArrayList dictKeyList = new ArrayList();
        dictKeyList.add("component_code");
        dictKeyList.add("component_name");
        dictKeyList.add("procedure_code");
        dictKeyList.add("lot_number");
        dictKeyList.add("sub_component_code");
        dictKeyList.add("sub_component_name");
        dictKeyList.add("sub_procedure_code");
        dictKeyList.add("lot_number");
        dictKeyList.add("registration_date");
        dictKeyList.add("component_lot_relation");
        dictKeyList.add("sub_lot_number");

        Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(langId, dictKeyList);

        /*Head*/
        ArrayList headList = new ArrayList();
        headList.add(dictMap.get("component_code"));
        headList.add(dictMap.get("component_name"));
        headList.add(dictMap.get("procedure_code"));
        headList.add(dictMap.get("lot_number"));
        headList.add(dictMap.get("sub_component_code"));
        headList.add(dictMap.get("sub_component_name"));
        headList.add(dictMap.get("sub_procedure_code"));
        headList.add(dictMap.get("sub_lot_number"));
        headList.add(dictMap.get("registration_date"));

        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(headList);

        TblComponentLotRelationVoList tblComponentLotRelationVoList = getTblComponentLotRelationList(componentCode, subComponentCode, "", "", 0, 0, false);

        //明細データを取得
        if (null != tblComponentLotRelationVoList && tblComponentLotRelationVoList.getTblComponentLotRelationVos() != null) {
            ArrayList tempOutList;
            TblComponentLotRelationVo tblComponentLotRelationVo;
            for (int i = 0; i < tblComponentLotRelationVoList.getTblComponentLotRelationVos().size(); i++) {
                tblComponentLotRelationVo = tblComponentLotRelationVoList.getTblComponentLotRelationVos().get(i);
                tempOutList = new ArrayList<>();

                tempOutList.add(String.valueOf(tblComponentLotRelationVo.getComponentCode()));
                tempOutList.add(String.valueOf(tblComponentLotRelationVo.getComponentName()));
                tempOutList.add(String.valueOf(tblComponentLotRelationVo.getProcedureCode()));
                tempOutList.add(String.valueOf(tblComponentLotRelationVo.getComponentLotNo()));
                tempOutList.add(String.valueOf(tblComponentLotRelationVo.getSubComponentCode()));
                tempOutList.add(String.valueOf(tblComponentLotRelationVo.getSubComponentName()));
                tempOutList.add(String.valueOf(tblComponentLotRelationVo.getSubProcedureCode()));
                tempOutList.add(String.valueOf(tblComponentLotRelationVo.getSubComponentLotNo()));
                tempOutList.add(String.valueOf(tblComponentLotRelationVo.getCreateDate()));
                gLineList.add(tempOutList);
            }
        }

        FileReponse fileReponse = new FileReponse();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);

        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable("tbl_component_lot");
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_COMPONENT_LOT_RELATION);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(userUuid);

        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(dictMap.get("component_lot_relation")));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fileReponse.setFileUuid(uuid);

        return fileReponse;
    }

    /**
     *
     * @param tblComponentLotRelationVoList
     * @param langId
     * @param userUuid
     * @return
     */
    @Transactional
    public BasicResponse postTblComponentLotRelation(TblComponentLotRelationVoList tblComponentLotRelationVoList, String langId, String userUuid) {

        if (tblComponentLotRelationVoList != null && tblComponentLotRelationVoList.getTblComponentLotRelationVos() != null) {

            Date sysDate = new Date();
            TblComponentLotRelation tblComponentLotRelation;

            for (int i = 0; i < tblComponentLotRelationVoList.getTblComponentLotRelationVos().size(); i++) {

                TblComponentLotRelationVo tblComponentLotRelationVos = tblComponentLotRelationVoList.getTblComponentLotRelationVos().get(i);

                if (tblComponentLotRelationVos.getOperationFlag() != null) {
                    switch (tblComponentLotRelationVos.getOperationFlag()) {// 1:delete,3:update,4:add
                        case "1": { // 1:delete 
                            deleteTblComponentLotRelation(tblComponentLotRelationVos.getUuid());
                            break;
                        }
                        case "3": { // 3:update
                            TblComponentLotRelation oldTblComponentLotRelation = entityManager.find(TblComponentLotRelation.class, tblComponentLotRelationVos.getUuid());
                            if (oldTblComponentLotRelation != null) {
                                //componentId componentcode
                                if (StringUtils.isNotEmpty(tblComponentLotRelationVos.getComponentId())) {
                                    oldTblComponentLotRelation.setComponentId(tblComponentLotRelationVos.getComponentId());
                                } else {
                                    oldTblComponentLotRelation.setComponentId(null);
                                }
                                //部品ロットID
                                if (StringUtils.isNotEmpty(tblComponentLotRelationVos.getComponentLotId())) {
                                    oldTblComponentLotRelation.setComponentLotId(tblComponentLotRelationVos.getComponentLotId());
                                } else {
                                    oldTblComponentLotRelation.setComponentLotId(null);
                                }
                                //構成部品ID
                                if (StringUtils.isNotEmpty(tblComponentLotRelationVos.getSubComponentId())) {
                                    oldTblComponentLotRelation.setSubComponentId(tblComponentLotRelationVos.getSubComponentId());
                                } else {
                                    oldTblComponentLotRelation.setSubComponentId(null);
                                }
                                //構成部品ロットID
                                if (StringUtils.isNotEmpty(tblComponentLotRelationVos.getSubComponentLotId())) {
                                    oldTblComponentLotRelation.setSubComponentLotId(tblComponentLotRelationVos.getSubComponentLotId());
                                } else {
                                    oldTblComponentLotRelation.setSubComponentLotId(null);
                                }

                                oldTblComponentLotRelation.setProcedureCode(tblComponentLotRelationVos.getProcedureCode());
                                oldTblComponentLotRelation.setSubProcedureCode(tblComponentLotRelationVos.getSubProcedureCode());

                                oldTblComponentLotRelation.setUpdateDate(sysDate);
                                oldTblComponentLotRelation.setUpdateUserUuid(userUuid);
                                //update
                                entityManager.merge(oldTblComponentLotRelation);
                            }
                            break;
                        }
                        case "4": { //4:add
                            //新規
                            tblComponentLotRelation = new TblComponentLotRelation();
                            //componentId componentcode
                            if (StringUtils.isNotEmpty(tblComponentLotRelationVos.getComponentId())) {
                                tblComponentLotRelation.setComponentId(tblComponentLotRelationVos.getComponentId());
                            } else {
                                tblComponentLotRelation.setComponentId(null);
                            }
                            //部品ロットID
                            if (StringUtils.isNotEmpty(tblComponentLotRelationVos.getComponentLotId())) {
                                tblComponentLotRelation.setComponentLotId(tblComponentLotRelationVos.getComponentLotId());
                            } else {
                                tblComponentLotRelation.setComponentLotId(null);
                            }

                            if (StringUtils.isNotEmpty(tblComponentLotRelationVos.getProcedureCode())) {
                                tblComponentLotRelation.setProcedureCode(tblComponentLotRelationVos.getProcedureCode());
                            } else {
                                tblComponentLotRelation.setProcedureCode(null);
                            }

                            //構成部品ID
                            if (StringUtils.isNotEmpty(tblComponentLotRelationVos.getSubComponentId())) {
                                tblComponentLotRelation.setSubComponentId(tblComponentLotRelationVos.getSubComponentId());
                            } else {
                                tblComponentLotRelation.setSubComponentId(null);
                            }
                            //構成部品ロットID
                            if (StringUtils.isNotEmpty(tblComponentLotRelationVos.getSubComponentLotId())) {
                                tblComponentLotRelation.setSubComponentLotId(tblComponentLotRelationVos.getSubComponentLotId());
                            } else {
                                tblComponentLotRelation.setSubComponentLotId(null);
                            }

                            if (StringUtils.isNotEmpty(tblComponentLotRelationVos.getSubProcedureCode())) {
                                tblComponentLotRelation.setSubProcedureCode(tblComponentLotRelationVos.getSubProcedureCode());
                            } else {
                                tblComponentLotRelation.setSubProcedureCode(null);
                            }

                            tblComponentLotRelation.setCreateDate(sysDate);
                            tblComponentLotRelation.setCreateUserUuid(userUuid);
                            tblComponentLotRelation.setUpdateDate(sysDate);
                            tblComponentLotRelation.setUpdateUserUuid(userUuid);

                            tblComponentLotRelation.setUuid(IDGenerator.generate());

                            entityManager.persist(tblComponentLotRelation);
                            break;
                        }
                        default:
                            break;
                    }
                }
            }
        }

        return new BasicResponse();
    }

    /**
     *
     * @param componentCode
     * @param subComponentCode
     * @param action
     * @param pageNumber
     * @param pageSize
     * @return
     */
    private List getSql(String componentCode, String subComponentCode, boolean isCount, String sidx,
            String sord,
            int pageNumber,
            int pageSize) {
        StringBuilder sql;

        if (isCount) {
            sql = new StringBuilder("SELECT COUNT(1) ");
        } else {
            sql = new StringBuilder("SELECT tblComponentLotRelation ");
        }
        sql.append(
                " FROM TblComponentLotRelation tblComponentLotRelation "
                + " LEFT JOIN FETCH tblComponentLotRelation.mstComponent mstComponent "
                + " LEFT JOIN FETCH tblComponentLotRelation.tblComponentLot tblComponentLot "
                + " LEFT JOIN FETCH tblComponentLotRelation.subMstComponent subMstComponent "
                + " LEFT JOIN FETCH tblComponentLotRelation.subTblComponentLot subTblComponentLot "
                + " WHERE 1=1  ");

        if (StringUtils.isNotEmpty(componentCode)) {
            sql = sql.append(" and mstComponent.componentCode like :componentCode ");
        }
        if (StringUtils.isNotEmpty(subComponentCode)) {
            sql = sql.append(" and subMstComponent.componentCode like :subcomponentCode ");
        }

        if (!isCount) {
            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                sql.append(sortStr);
            } else {
                //表示順は部品コードの昇順、登録日の降順
                sql = sql.append(" Order by mstComponent.componentCode asc, tblComponentLotRelation.createDate desc ");
            }
        }

        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode + "%");
        }
        if (StringUtils.isNotEmpty(subComponentCode)) {
            query.setParameter("subcomponentCode", "%" + subComponentCode + "%");
        }

        // 画面改ページを設定する
        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    private void deleteTblComponentLotRelation(String uuid) {
        StringBuilder sql;
        sql = new StringBuilder("DELETE FROM  TblComponentLotRelation tblComponentLotRelation "
                + " WHERE tblComponentLotRelation.uuid =:uuid "
        );

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("uuid", uuid);
        query.executeUpdate();
    }

    public TblComponentLotRelationVoList getTblComponentLotRelationList(String productionDetailId, String macReportId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT relation FROM TblComponentLotRelation relation ");
        sql.append("LEFT JOIN FETCH relation.mstComponent mstComponent ");
        sql.append("WHERE 1=1 ");
        if (StringUtils.isNotEmpty(productionDetailId)) {
            sql.append("AND relation.productionDetailId = :productionDetailId ");
        }
        if (StringUtils.isNotEmpty(macReportId)) {
            sql.append("AND relation.macReportId = :macReportId ");
        }

        Query query = entityManager.createQuery(sql.toString());
        if (StringUtils.isNotEmpty(productionDetailId)) {
            query.setParameter("productionDetailId", productionDetailId);
        }
        if (StringUtils.isNotEmpty(macReportId)) {
            query.setParameter("macReportId", macReportId);
        }

        List<TblComponentLotRelation> list = query.getResultList();
        TblComponentLotRelationVoList tblComponentLotRelationVoList = new TblComponentLotRelationVoList();
        List<TblComponentLotRelationVo> tblComponentLotRelationVos = new ArrayList();
        if (list != null && !list.isEmpty()) {
            for (TblComponentLotRelation relation : list) {
                TblComponentLotRelationVo tblComponentLotRelationVo = new TblComponentLotRelationVo();
                tblComponentLotRelationVo.setSubComponentId(relation.getSubComponentId());
                tblComponentLotRelationVo.setSubProcedureCode(relation.getSubProcedureCode());
                tblComponentLotRelationVo.setSubComponentLotId(relation.getSubComponentLotId());
                tblComponentLotRelationVo.setSubComponentLotNo(relation.getSubTblComponentLot() == null ? null : relation.getSubTblComponentLot().getLotNo());
                tblComponentLotRelationVo.setMacReportId(relation.getMacReportId());
                tblComponentLotRelationVo.setProductionDetailId(relation.getProductionDetailId());
                tblComponentLotRelationVos.add(tblComponentLotRelationVo);
            }
            tblComponentLotRelationVoList.setTblComponentLotRelationVos(tblComponentLotRelationVos);
        }
        return tblComponentLotRelationVoList;
    }

    public TblComponentLotRelationVoList getTblComponentLotRelationListByIdAndLotId(String componentId, String lotId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT relation.componentId, relation.componentLotId, relation.subComponentId FROM TblComponentLotRelation relation ");
        sql.append("WHERE 1=1 ");
        if (StringUtils.isNotEmpty(componentId)) {
            sql.append("AND relation.componentId = :componentId ");
        }
        if (StringUtils.isNotEmpty(lotId)) {
            sql.append("AND relation.componentLotId = :lotId ");
        }

        Query query = entityManager.createQuery(sql.toString());
        if (StringUtils.isNotEmpty(componentId)) {
            query.setParameter("componentId", componentId);
        }
        if (StringUtils.isNotEmpty(lotId)) {
            query.setParameter("lotId", lotId);
        }

        List<TblComponentLotRelation> list = query.getResultList();
        TblComponentLotRelationVoList tblComponentLotRelationVoList = new TblComponentLotRelationVoList();
        List<TblComponentLotRelationVo> tblComponentLotRelationVos = new ArrayList();
        if (list != null && !list.isEmpty()) {
          for (Object obj : list) {
                Object[] objTblStockRelation = (Object[]) obj;
                TblComponentLotRelationVo tblComponentLotRelationVo = new TblComponentLotRelationVo();
                tblComponentLotRelationVo.setSubComponentId(String.valueOf(objTblStockRelation[2]));
                tblComponentLotRelationVos.add(tblComponentLotRelationVo);
            }
            tblComponentLotRelationVoList.setTblComponentLotRelationVos(tblComponentLotRelationVos);
        }
        return tblComponentLotRelationVoList;
    }
    
    /**
     * 生産実績明細IDからロット関連情報取得
     * @param productionDetailId
     * @return 
     */
    public TblComponentLotRelation getComponentLotRelationByProductionDetailId(String productionDetailId) {
        Query query = entityManager.createQuery("SELECT t FROM TblComponentLotRelation t WHERE t.productionDetailId = :productionDetailId");
        query.setParameter("productionDetailId", productionDetailId);
        List<TblComponentLotRelation> resultList = query.getResultList();
        if (resultList != null && resultList.size() > 0) {
            return (TblComponentLotRelation)resultList.get(0);
        } 
        else {
            return null;
        }
    }
}
