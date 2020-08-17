/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.component.company.MstComponentCompany;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectLang;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectType;
import com.kmcj.karte.resources.component.inspection.item.model.*;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import static com.kmcj.karte.util.FileUtil.SEPARATOR;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.ZipCompressor;
import java.math.BigDecimal;
import org.apache.commons.lang.StringUtils;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Master Component inspection item service
 *
 * @author duanlin
 */
@Dependent
public class MstComponentInspectionItemsTableService {

    @Inject
    private MstCompanyService mstCompanyService;

    @Inject
    private MstComponentService mstComponentService;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;
    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private MstChoiceService mstChoiceService;
    @Inject
    private KartePropertyService kartePropertyService;
    @Inject
    private CnfSystemService cnfSystemService;
    
    private final static String ITEM_APPROVE_STATUS = "mst_component_inspection_items_table.item_approve_status";
    
    private static final String LANGID = "ja";

    /**
     * Get count for component inspection items table search
     *
     * @param searchCondition
     * @param langId
     * @return
     */
    public long getSearchCount(ComponentInspectionItemsTableSearchCond searchCondition, String langId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(m.id) From MstComponentInspectionItemsTable m");
        queryBuilder.append(" JOIN FETCH m.mstComponent");
        queryBuilder.append(" JOIN FETCH m.mstCompanyOutgoing");
        queryBuilder.append(" JOIN FETCH m.mstCompanyIncoming");
        queryBuilder.append(" JOIN FETCH m.mstComponentInspectLang");
        queryBuilder.append(this.makeWhereClauseByQueryCondition(searchCondition));
        queryBuilder.append(" ORDER BY m.mstComponent.componentCode, m.mstCompanyOutgoing.companyCode, m.version DESC");

        TypedQuery<Long> query = this.entityManager.createQuery(queryBuilder.toString(), Long.class);

        this.setValuesToSearchQuery(query, searchCondition, langId);

        return query.getSingleResult().intValue();
    }

    /**
     * Get component inspection items table info by search condition.
     *
     * @param searchCondition
     * @param langId
     * @return
     */
    public List<ComponentInspectionItemsTableInfo> getInspectionItemsTable(ComponentInspectionItemsTableSearchCond searchCondition, String langId) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m FROM MstComponentInspectionItemsTable m");
        queryBuilder.append(" JOIN FETCH m.mstComponent");
        queryBuilder.append(" JOIN FETCH m.mstCompanyOutgoing");
        queryBuilder.append(" JOIN FETCH m.mstCompanyIncoming");
        queryBuilder.append(" JOIN FETCH m.mstComponentInspectLang");
        queryBuilder.append(this.makeWhereClauseByQueryCondition(searchCondition));
        queryBuilder.append(" ORDER BY m.mstComponent.componentCode, m.mstCompanyOutgoing.companyCode, m.version DESC");

        TypedQuery<MstComponentInspectionItemsTable> query = this.entityManager.createQuery(
                queryBuilder.toString(), MstComponentInspectionItemsTable.class);

        this.setValuesToSearchQuery(query, searchCondition, langId);

        List<MstComponentInspectionItemsTable> headerList = query.getResultList();

        List<ComponentInspectionItemsTableInfo> infoList = new ArrayList<>();
        headerList.stream().forEach(inspectionItemsTable -> {
            infoList.add(this.convertToInspectionItemHeader(inspectionItemsTable, langId));
        });

        return infoList;
    }

    /**
     * Get component inspection items table info by id
     *
     * @param componentInspectionItemsTableId
     * @param langId
     * @return
     */
    public ComponentInspectionItemsTableInfo getInspectionItemsTableDetails(String componentInspectionItemsTableId, String langId) {

        MstComponentInspectionItemsTable inspectionItemsTable = this.entityManager
                .createNamedQuery("MstComponentInspectionItemsTable.findById", MstComponentInspectionItemsTable.class)
                .setParameter("id", componentInspectionItemsTableId)
                .getResultList().stream().findFirst().orElse(null);

        if (inspectionItemsTable != null && StringUtils.isNotEmpty(inspectionItemsTable.getInspectTypeDictKey())) {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT m FROM MstComponentInspectionItemsTable m");
            queryBuilder.append(" JOIN FETCH m.mstComponentInspectLang mstComponentInspectLang");
            queryBuilder.append(" WHERE m.id = :id");
            queryBuilder.append(" AND mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId");

            inspectionItemsTable = this.entityManager
                    .createQuery(queryBuilder.toString(), MstComponentInspectionItemsTable.class)
                    .setParameter("id", componentInspectionItemsTableId)
                    .setParameter("langId", langId)
                    .getResultList().stream().findFirst().orElse(null);
        }

        List<MstComponentInspectionItemsTableDetail> itemDetails = this.getMstCompInspItemsTableDetails(componentInspectionItemsTableId);
        List<MstComponentInspectionItemsFile> itemsFileDetails = this.getMstCompInspItemsFileDetails(inspectionItemsTable);

        ComponentInspectionItemsTableInfo inspectionItemsTableInfo = this.convertToInspectionItemHeader(inspectionItemsTable, langId);
        inspectionItemsTableInfo.setInspectionItemsTableDetails(this.convertToInspectionItemDetailList(itemDetails));
        inspectionItemsTableInfo.setInspectionItemsFileDetails(this.convertToInspectionItemFileList(itemsFileDetails));

        return inspectionItemsTableInfo;
    }

    // KM-394 ADDSインタフェースを作成 2017/11/1 penggd Start
    /**
     * Get component inspection items table info by id
     *
     * @param componentCode
     * @param outgoingCompanyCode
     * @param langId
     * @param incomingCompanyId
     * @return
     */
    public ComponentInspectionItemsTableInfo getInspectionItemsTableDetailsByCondition(String componentCode,
            String outgoingCompanyCode, String langId, String incomingCompanyId) {

        String componentId = "";

        String outgoingCompanyId = "";

        MstComponent mstComponent = mstComponentService.getMstComponent(componentCode);

        if (null != mstComponent) {

            componentId = mstComponent.getId();
        }

        MstCompany mstCompany = mstCompanyService.getMstCompanyByCode(outgoingCompanyCode);

        if (null != mstCompany) {

            outgoingCompanyId = mstCompany.getId();
        }

        int version = getMaxVersion(componentId, outgoingCompanyId, incomingCompanyId);

        MstComponentInspectionItemsTable inspectionItemsTable = this.entityManager
                .createNamedQuery("MstComponentInspectionItemsTable.findByCondition",
                        MstComponentInspectionItemsTable.class)
                .setParameter("componentId", componentId).setParameter("outgoingCompanyId", outgoingCompanyId)
                .setParameter("incomingCompanyId", incomingCompanyId).setParameter("version", version).getResultList()
                .stream().findFirst().orElse(null);

        List<MstComponentInspectionItemsTableDetail> itemDetails = new ArrayList<MstComponentInspectionItemsTableDetail>();

        if (inspectionItemsTable != null && StringUtils.isNotEmpty(inspectionItemsTable.getInspectTypeDictKey())) {

            String componentInspectionItemsTableId = inspectionItemsTable.getId();

            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT m FROM MstComponentInspectionItemsTable m");
            queryBuilder.append(" JOIN FETCH m.mstComponentInspectLang mstComponentInspectLang");
            queryBuilder.append(" WHERE m.id = :id");
            queryBuilder.append(" AND mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId");

            inspectionItemsTable = this.entityManager
                    .createQuery(queryBuilder.toString(), MstComponentInspectionItemsTable.class)
                    .setParameter("id", componentInspectionItemsTableId).setParameter("langId", langId).getResultList()
                    .stream().findFirst().orElse(null);

            itemDetails = this.getMstCompInspItemsTableDetails(componentInspectionItemsTableId);
        }

        ComponentInspectionItemsTableInfo inspectionItemsTableInfo = this
                .convertToInspectionItemHeader(inspectionItemsTable, langId);

        inspectionItemsTableInfo.setInspectionItemsTableDetails(this.convertToInspectionItemDetailList(itemDetails));

        return inspectionItemsTableInfo;
    }
    // KM-394 ADDSインタフェースを作成 2017/11/1 penggd End

    /**
     * Get current inspection items table id
     *
     * @param componentId
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @return
     */
    public String getCurrentInsepectionItemsTableId(String componentId, String outgoingCompanyId, String incomingCompanyId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m FROM MstComponentInspectionItemsTable m");
        queryBuilder.append(" WHERE m.componentId = :componentId");
        queryBuilder.append(" AND m.outgoingCompanyId = :outgoingCompanyId");
        queryBuilder.append(" AND m.incomingCompanyId = :incomingCompanyId");
        queryBuilder.append(" AND m.applyEndDate IS NULL AND m.itemApproveStatus = 1");

        MstComponentInspectionItemsTable inspectionItemsTable = this.entityManager
                .createQuery(queryBuilder.toString(), MstComponentInspectionItemsTable.class)
                .setParameter("componentId", componentId)
                .setParameter("outgoingCompanyId", outgoingCompanyId)
                .setParameter("incomingCompanyId", incomingCompanyId)
                .getResultList().stream().findFirst().orElse(null);

        if (inspectionItemsTable == null) {
            return null;
        }
        return inspectionItemsTable.getId();
    }

    public MstComponentInspectionItemsTable checkCurrentInspectionItemsTable(String componentId, String outgoingCompanyId, String incomingCompanyId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m FROM MstComponentInspectionItemsTable m");
        queryBuilder.append(" WHERE m.componentId = :componentId");
        queryBuilder.append(" AND m.outgoingCompanyId = :outgoingCompanyId");
        queryBuilder.append(" AND m.incomingCompanyId = :incomingCompanyId");
        queryBuilder.append(" AND m.applyEndDate IS NULL AND m.itemApproveStatus = 1");

        MstComponentInspectionItemsTable inspectionItemsTable = this.entityManager
                .createQuery(queryBuilder.toString(), MstComponentInspectionItemsTable.class)
                .setParameter("componentId", componentId)
                .setParameter("outgoingCompanyId", outgoingCompanyId)
                .setParameter("incomingCompanyId", incomingCompanyId)
                .getResultList().stream().findFirst().orElse(null);

        if (inspectionItemsTable == null) {
            return null;
        }
        return inspectionItemsTable;
    }

    /**
     * Get MstComponentInspectionItemsTableDetail by
     * componentInspectionItemsTableId
     *
     * @param componentInspectionItemsTableId
     * @return
     */
    public List<MstComponentInspectionItemsTableDetail> getMstCompInspItemsTableDetails(
            String componentInspectionItemsTableId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(
                "SELECT m FROM MstComponentInspectionItemsTableDetail m WHERE m.componentInspectionItemsTableId = :componentInspectionItemsTableId ");
        queryBuilder.append(" ORDER BY m.measurementType,m.localSeq,m.inspectionItemSno");
        List<MstComponentInspectionItemsTableDetail> itemDetails = this.entityManager
                .createQuery(queryBuilder.toString(), MstComponentInspectionItemsTableDetail.class)
                .setParameter("componentInspectionItemsTableId", componentInspectionItemsTableId).getResultList();
        return itemDetails;
    }

    public List<MstComponentInspectionItemsFile> getMstCompInspItemsFileDetails(MstComponentInspectionItemsTable inspectionItemsTable) {
        if (inspectionItemsTable == null) {
            return null;
        }
        List<MstComponentInspectionItemsFile> itemFileDetails = this.entityManager
                .createNamedQuery("MstComponentInspectionItemsFile.findByConditionSort", MstComponentInspectionItemsFile.class)
                .setParameter("componentId", inspectionItemsTable.getComponentId())
                .setParameter("outgoingCompanyId", inspectionItemsTable.getOutgoingCompanyId())
                .setParameter("incomingCompanyId", inspectionItemsTable.getIncomingCompanyId())
                .getResultList();
        return itemFileDetails;
    }
    
    /**
     * Get MstComponentInspectionItemsTableDetail by
     * componentInspectionItemsTableId (バッチ用)
     *
     * @param componentInspectionItemsTableId
     * @return
     */
    public List<MstComponentInspectionItemsTableDetail> getMstCompInspItemsTableDetailsByBatch(
            String componentInspectionItemsTableId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(
                "SELECT m FROM MstComponentInspectionItemsTableDetail m WHERE m.componentInspectionItemsTableId = :componentInspectionItemsTableId AND m.additionalFlg = :additionalFlg ");
        queryBuilder.append(" ORDER BY m.measurementType,m.localSeq,m.inspectionItemSno");
        List<MstComponentInspectionItemsTableDetail> itemDetails = this.entityManager
                .createQuery(queryBuilder.toString(), MstComponentInspectionItemsTableDetail.class)
                .setParameter("componentInspectionItemsTableId", componentInspectionItemsTableId)
                .setParameter("additionalFlg", CommonConstants.ITEMS_TABLE_ADDITIONAL_FLG_YES.charAt(0))
                .getResultList();
        return itemDetails;
    }

    /**
     * Get MstComponentInspectionItemsTableDetail Map by
     * componentInspectionItemsTableId
     *
     * @param componentInspectionItemsTableId
     * @return
     */
    public Map<String, MstComponentInspectionItemsTableDetail> getMstCompInspItemsTableDetailsMap(String componentInspectionItemsTableId) {
        Map<String, MstComponentInspectionItemsTableDetail> retMap = new HashMap<>();
        List<MstComponentInspectionItemsTableDetail> itemDetails = this.getMstCompInspItemsTableDetails(componentInspectionItemsTableId);
        if (itemDetails == null) {
            return retMap;
        }
        for (MstComponentInspectionItemsTableDetail detail : itemDetails) {
            retMap.put(detail.getInspectionItemSno(), detail);
        }
        return retMap;
    }

    /*
     * Get component inspection item max VERSION info by id
     * 
     * @param componentId
     * @return 
     */
    @Transactional
    public Integer getMaxVersion(String componentId, String outgoingCompanyId, String incomingCompanyId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT MAX(m.version) FROM MstComponentInspectionItemsTable m ");
        queryBuilder.append(" WHERE m.componentId = :componentId ");
        if (StringUtils.isNotEmpty(incomingCompanyId)) {
            queryBuilder.append(" AND m.incomingCompanyId = :incomingCompanyId ");
        }
        if (StringUtils.isNotEmpty(outgoingCompanyId)) {
            queryBuilder.append(" AND m.outgoingCompanyId = :outgoingCompanyId ");
        }
        Query query = entityManager.createQuery(queryBuilder.toString(), Integer.class);

        query.setParameter("componentId", componentId);
        if (StringUtils.isNotEmpty(incomingCompanyId)) {
            query.setParameter("incomingCompanyId", incomingCompanyId);
        }
        if (StringUtils.isNotEmpty(outgoingCompanyId)) {
            query.setParameter("outgoingCompanyId", outgoingCompanyId);
        }
        try {
            int version = (Integer) query.getSingleResult();
            return version;
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * 否認以外の状態の最新バージョンを取得する。
     * @param componentId
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @return 
     */
    private int getMaxNotDeniedVersion(String componentId, String outgoingCompanyId, String incomingCompanyId) {
        String jpql = 
            "SELECT MAX(m.version) FROM MstComponentInspectionItemsTable m "
            + "WHERE "
                + "m.componentId = :componentId AND "
                + "m.incomingCompanyId = :incomingCompanyId AND "
                + "m.outgoingCompanyId = :outgoingCompanyId AND m.itemApproveStatus <> 2";
        List<Integer> results = entityManager.createQuery(jpql, Integer.class)
            .setParameter("componentId", componentId)
            .setParameter("incomingCompanyId", incomingCompanyId)
            .setParameter("outgoingCompanyId", outgoingCompanyId)
            .getResultList();
        return results.isEmpty() ? 0 : results.get(0);
    }

    @Transactional
    public void createMstComponentInspectionItem(MstComponentInspectionItemsTable mstComponentInspectionItem) {
        entityManager.persist(mstComponentInspectionItem);
    }

    /**
     * Set mstComponentInspectionItemDetail
     *
     * @param mstComponentInspectionItemDetail
     */
    @Transactional
    public void setInspectionItemDetail(MstComponentInspectionItemsTableDetail mstComponentInspectionItemDetail) {
        entityManager.persist(mstComponentInspectionItemDetail);
    }

    /*
     * Delete InspectionItemDetail
     * 
     * @param componentId
     * @return 
     */
    public void deleteMstComponentInspectionItemsTableDetail(String componentInspectionItemsTableId) {
        String sql = "DELETE FROM MstComponentInspectionItemsTableDetail m "
                + "WHERE m.componentInspectionItemsTableId = :componentInspectionItemsTableId ";
        this.entityManager
                .createQuery(sql, Integer.class)
                .setParameter("componentInspectionItemsTableId", componentInspectionItemsTableId).executeUpdate();
    }

    /*
     * Delete InspectionItemFile
     *
     * @param componentId
     * @return
     */
    public void deleteMstComponentInspectionItemsFile(String componentInspectionItemsTableId) {
        MstComponentInspectionItemsTable inspectionItemsTable = this.entityManager
                .createNamedQuery("MstComponentInspectionItemsTable.findById", MstComponentInspectionItemsTable.class)
                .setParameter("id", componentInspectionItemsTableId)
                .getResultList().stream().findFirst().orElse(null);

        if (inspectionItemsTable != null ) {
            this.entityManager
                    .createNamedQuery("MstComponentInspectionItemsFile.deleteByInspectionItems", MstComponentInspectionItemsFile.class)
                    .setParameter("componentId", inspectionItemsTable.getComponentId())
                    .setParameter("outgoingCompanyId", inspectionItemsTable.getOutgoingCompanyId())
                    .setParameter("incomingCompanyId", inspectionItemsTable.getIncomingCompanyId())
                    .executeUpdate();
        }
    }

    /*
     * Delete InspectionItem
     * 
     * @param componentId
     * @return 
     */
    public void deleteMstComponentInspectionItemsTable(String id) {
        String sql = "DELETE FROM MstComponentInspectionItemsTable m "
                + "WHERE m.id = :id ";
        this.entityManager
                .createQuery(sql, Integer.class)
                .setParameter("id", id)
                .executeUpdate();
    }

    /**
     * Find By Id
     *
     * @param componentInspectionItemsTableId
     * @return
     */
    public MstComponentInspectionItemsTable findById(String componentInspectionItemsTableId) {
        return this.entityManager
                .createNamedQuery("MstComponentInspectionItemsTable.findById", MstComponentInspectionItemsTable.class)
                .setParameter("id", componentInspectionItemsTableId)
                .getResultList().stream().findFirst().orElse(null);
    }

    public MstComponentInspectionItemsTable getOldVersionItem(MstComponentInspectionItemsTable mstComponentInspectionItemsTable) {
        return getOldVersionItem(
            mstComponentInspectionItemsTable.getComponentId(), 
            mstComponentInspectionItemsTable.getOutgoingCompanyId(), 
            mstComponentInspectionItemsTable.getIncomingCompanyId()
        ).orElse(null);
    }
    
    public Optional<MstComponentInspectionItemsTable> getOldVersionItem(String componentId, String outgoingCompanyId, String incomingCompanyId) {
        String sql = "SELECT m FROM MstComponentInspectionItemsTable m "
                + "WHERE m.componentId = :componentId "
                + "AND m.outgoingCompanyId = :outgoingCompanyId "
                + "AND m.incomingCompanyId = :incomingCompanyId "
                + "AND m.applyEndDate IS NULL "
                + "AND m.itemApproveStatus = 1 "
                + "ORDER BY m.version DESC";
        return this.entityManager
                .createQuery(sql, MstComponentInspectionItemsTable.class)
                .setParameter("componentId", componentId)
                .setParameter("outgoingCompanyId", outgoingCompanyId)
                .setParameter("incomingCompanyId", incomingCompanyId)
                .getResultList().stream().findFirst();
    }
    
    public Map<String, MstComponentInspectionItemsTableDetail> getOldVersionItemDetails(String componentId, String outgoingCompanyId, String incomingCompanyId) {
        Map<String, MstComponentInspectionItemsTableDetail> ret = new HashMap<>();
        getOldVersionItem(componentId, outgoingCompanyId, incomingCompanyId).ifPresent(item->{
            List<MstComponentInspectionItemsTableDetail> list = getMstCompInspItemsTableDetails(item.getId());
            for (MstComponentInspectionItemsTableDetail detail : list) {
                ret.put(toDimensionId(detail), detail);
            }
        });
        return ret;
    }
    
    public Map<String, MstComponentInspectionItemsTableDetail> getVersionItemDetailsMap(String itemsId) {
        Map<String, MstComponentInspectionItemsTableDetail> ret = new HashMap<>();
        List<MstComponentInspectionItemsTableDetail> list = getMstCompInspItemsTableDetails(itemsId);
        for (MstComponentInspectionItemsTableDetail detail : list) {
            ret.put(toDimensionId(detail), detail);
        }
        return ret;
    }
    
    public String toDimensionId(MstComponentInspectionItemsTableDetail detail) {
        return new StringBuilder(nulEsc(detail.getDrawingPage()))
            .append(nulEsc(detail.getDrawingAnnotation()))
            .append(nulEsc(detail.getDrawingMentionNo()))
            .append(nulEsc(detail.getSimilarMultiitem())).toString();
    }
    
    private String nulEsc(String val) {
        return val == null ? "" : val;
    }

    /**
     * Get Not Approve
     *
     * @param componentId
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @return
     */
    public List<MstComponentInspectionItemsTable> getNotApprove(String componentId, String outgoingCompanyId, String incomingCompanyId) {
        String sql = "SELECT m FROM MstComponentInspectionItemsTable m "
                + "WHERE m.componentId = :componentId "
                + "AND m.outgoingCompanyId = :outgoingCompanyId "
                + "AND m.incomingCompanyId = :incomingCompanyId "
                + "AND m.approveDate IS NULL "
                + "AND m.approvePersonUuid IS NULL "
                + "ORDER BY m.version ";
        return this.entityManager
                .createQuery(sql, MstComponentInspectionItemsTable.class)
                .setParameter("componentId", componentId)
                .setParameter("outgoingCompanyId", outgoingCompanyId)
                .setParameter("incomingCompanyId", incomingCompanyId)
                .getResultList();
    }

    /**
     * Check inspec type exists
     * @param inspectTypeId
     * @param seq
     * @return
     */
    public MstComponentInspectType getInspecTypeCheck(String inspectTypeId, Integer seq) {
        String sql = "SELECT m FROM MstComponentInspectType m "
                + "WHERE m.id = :id "
                + "AND m.seq = :seq ";
        return  this.entityManager
                .createQuery(sql, MstComponentInspectType.class)
                .setParameter("id", inspectTypeId)
                .setParameter("seq", seq)
                .getResultList().stream().findFirst().orElse(null);
    }

    /**
     * Update mstComponentInspectionItem
     *
     * @param mstComponentInspectionItemsTable
     */
    public void update(MstComponentInspectionItemsTable mstComponentInspectionItemsTable) {
        entityManager.merge(mstComponentInspectionItemsTable);
    }

    /**
     * Get external component inspection items table.
     *
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @return
     */
    public List<ComponentInspectionItemsTableForBatch> getExternalInspectionItemsTable(String outgoingCompanyId, String incomingCompanyId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m FROM MstComponentInspectionItemsTable m");
        queryBuilder.append(" JOIN FETCH m.mstComponent");
        queryBuilder.append(" WHERE m.outgoingCompanyId = :outgoingCompanyId");
        queryBuilder.append(" AND m.incomingCompanyId = :incomingCompanyId");
        queryBuilder.append(" AND m.itemApproveStatus = 1");
        queryBuilder.append(" AND m.applyEndDate IS NULL");
        queryBuilder.append(" AND m.inspBatchUpdateStatus = :inspBatchUpdateStatus");

        List<MstComponentInspectionItemsTable> itemsTableList = this.entityManager.createQuery(
                queryBuilder.toString(), MstComponentInspectionItemsTable.class)
                .setParameter("outgoingCompanyId", outgoingCompanyId)
                .setParameter("incomingCompanyId", incomingCompanyId)
                .setParameter("inspBatchUpdateStatus", CommonConstants.INSP_BATCH_UPDATE_STATUS_ITEM_NOT_PUSH)
                .getResultList();

        List<ComponentInspectionItemsTableForBatch> resultList = new ArrayList<>();
        itemsTableList.stream().forEach(mst -> {
            ComponentInspectionItemsTableForBatch batchData = new ComponentInspectionItemsTableForBatch();
            batchData.setComponentCode(mst.getMstComponent().getComponentCode());
            mst.setMstComponent(null);
            mst.setMstCompanyIncoming(null);
            mst.setMstCompanyOutgoing(null);
            batchData.setInspectionItemsTable(mst);

            List<MstComponentInspectionItemsTableDetail> itemDetails = this.getMstCompInspItemsTableDetailsByBatch(mst.getId());
            // KM-392 ドメイン内での並び順。(データ連携は行わない) start
            List<MstComponentInspectionItemsTableDetail> resultItemDetails = new ArrayList<>();
            itemDetails.stream().forEach(detail -> {
                detail.setLocalSeq(0);
                detail.setEnableThAlert(true);
                resultItemDetails.add(detail);
            });
            // KM-392 ドメイン内での並び順。(データ連携は行わない) end
            batchData.setInspectionItemsTableDetails(resultItemDetails);

            resultList.add(batchData);
        });
        return resultList;
    }

    /**
     * update mst_component_inspection_items_table table batch update status.
     *
     * @param dataList
     */
    @Transactional
    public void updateInspBatchUpdateStatus(List<ComponentInspectionItemsTableForBatch> dataList) {
        dataList.forEach(action -> {
            MstComponentInspectionItemsTable itemsTable = action.getInspectionItemsTable();
            itemsTable.setInspBatchUpdateStatus(CommonConstants.INSP_BATCH_UPDATE_STATUS_ITEM_PUSHED);
            this.entityManager.merge(itemsTable);
        });
    }

    /**
     * update component inspection item details localSeq
     *
     * @param componentInspectionItemsTableInfo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateComponentInspectionItemsTableDetail(ComponentInspectionItemsTableInfo componentInspectionItemsTableInfo, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        int localSeq;
        Date date = new Date();

        if (componentInspectionItemsTableInfo != null) {
            switch (componentInspectionItemsTableInfo.getActionFlg()) {
                case CommonConstants.ITEMS_TABLE_UPDATE_FLG_FILE:
                    //update item file
                    switch (componentInspectionItemsTableInfo.getFileFlg()) {
                        case CommonConstants.ITEMS_FILE_FLG_UPLOAD:
                            this.logicUploadItemFile(componentInspectionItemsTableInfo);
                            MstComponentInspectionItemsFile mstComponentInspectionItemsFile = new MstComponentInspectionItemsFile();

                            mstComponentInspectionItemsFile.setId(IDGenerator.generate());
                            mstComponentInspectionItemsFile.setComponentId(componentInspectionItemsTableInfo.getComponentId());
                            mstComponentInspectionItemsFile.setOutgoingCompanyId(componentInspectionItemsTableInfo.getOutgoingCompanyId());
                            mstComponentInspectionItemsFile.setIncomingCompanyId(componentInspectionItemsTableInfo.getIncomingCompanyId());
                            mstComponentInspectionItemsFile.setFileUuid(componentInspectionItemsTableInfo.getItemFileUuid());
                            entityManager.persist(mstComponentInspectionItemsFile);
                            break;
                        case CommonConstants.ITEMS_FILE_FLG_REMOVE:
                            this.entityManager
                                .createNamedQuery("MstComponentInspectionItemsFile.deleteByCondition", MstComponentInspectionItemsFile.class)
                                .setParameter("componentId", componentInspectionItemsTableInfo.getComponentId())
                                .setParameter("outgoingCompanyId", componentInspectionItemsTableInfo.getOutgoingCompanyId())
                                .setParameter("incomingCompanyId", componentInspectionItemsTableInfo.getIncomingCompanyId())
                                .setParameter("fileUuid", componentInspectionItemsTableInfo.getItemFileUuid())
                                .executeUpdate();
                            break;
                    }
                    break;
                default:
                    //update item table operation
                    if (componentInspectionItemsTableInfo.getInspectionItemsTableDetails() != null
                            && componentInspectionItemsTableInfo.getInspectionItemsTableDetails().size() > 0) {
                        String componentInspectionItemsTableId = componentInspectionItemsTableInfo.getComponentInspectionItemsTableId();
                        
                        // 検査管理項目の承認コメントを対応 20180919 start
                        MstComponentInspectionItemsTable oldMstComponentInspectionItemsTable = entityManager.createNamedQuery("MstComponentInspectionItemsTable.findById", MstComponentInspectionItemsTable.class)
                                .setParameter("id", componentInspectionItemsTableId).getSingleResult();
                        
                        // 検査管理項目の承認コメントを更新
                        if (null != oldMstComponentInspectionItemsTable) {
                            oldMstComponentInspectionItemsTable.setMeasSampleRatio(componentInspectionItemsTableInfo.getMeasSampleRatio());
                            oldMstComponentInspectionItemsTable.setVisSampleRatio(componentInspectionItemsTableInfo.getVisSampleRatio());
                            oldMstComponentInspectionItemsTable.setItemApproveComment(componentInspectionItemsTableInfo.getItemApproveComment());
                            oldMstComponentInspectionItemsTable.setUpdateDate(date);
                            oldMstComponentInspectionItemsTable.setUpdateUserUuid(loginUser.getUserUuid());
                            entityManager.merge(oldMstComponentInspectionItemsTable);
                        }
                        // 検査管理項目の承認コメントを対応 20180919 end
                        
                        int indexSave = 0;
                        Object itemSql = entityManager.createQuery("SELECT MAX(m.inspectionItemSno) FROM MstComponentInspectionItemsTableDetail m "
                                + "WHERE m.componentInspectionItemsTableId = :componentInspectionItemsTableId")
                                .setParameter("componentInspectionItemsTableId", componentInspectionItemsTableId)
                                .getSingleResult();
                        if (itemSql != null) {
                            String itemSqlStr = itemSql.toString().replace("ITEM", "");
                            if (StringUtils.isNotEmpty(itemSqlStr)) {
                                indexSave = Integer.parseInt(itemSqlStr);
                            }
                        }
                        Map<String, String> resultMap = new HashMap<>();
                        String itemtableDetailMethodId;
                        String containsKey;
                        int deleteCount = 0;
                        Boolean saveInitFlag = true;
                        MstComponentInspectionItemsTableDetail mstComponentInspectionItemsTableDetail;
                        for (ComponentInspectionItemsTableDetail itemDetail : componentInspectionItemsTableInfo.getInspectionItemsTableDetails()) {
                            itemtableDetailMethodId = (itemDetail.getDrawingPage() == null ? "NULL" : itemDetail.getDrawingPage())
                                    + (itemDetail.getDrawingAnnotation() == null ? "NULL" : itemDetail.getDrawingAnnotation())
                                    + (itemDetail.getDrawingMentionNo() == null ? "NULL" : itemDetail.getDrawingMentionNo())
                                    + (itemDetail.getSimilarMultiitem() == null ? "NULL" : itemDetail.getSimilarMultiitem());
                            containsKey = itemtableDetailMethodId.concat("|");
                            if (resultMap.containsKey(containsKey)) {
                                response.setError(true);
                                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "dimension_value_repeat_error");
                                response.setErrorMessage(msg);
                                return response;
                            }
                            resultMap.put(containsKey, containsKey);
                        }

                        for (ComponentInspectionItemsTableDetail itemDetail : componentInspectionItemsTableInfo.getInspectionItemsTableDetails()) {
                            mstComponentInspectionItemsTableDetail = new MstComponentInspectionItemsTableDetail();

                            localSeq = itemDetail.getLocalSeq();

                            if (itemDetail.getOperationFlag() != null) {
                                switch (itemDetail.getOperationFlag()) {
                                    case CommonConstants.OPERATION_FLAG_CREATE:
                                        indexSave++;
                                        if (saveInitFlag) {
                                            indexSave = indexSave - deleteCount;
                                            saveInitFlag = false;
                                        }
                                        mstComponentInspectionItemsTableDetail.setId(IDGenerator.generate());
                                        mstComponentInspectionItemsTableDetail.setComponentInspectionItemsTableId(componentInspectionItemsTableId);
                                        if (indexSave < 10) {
                                            mstComponentInspectionItemsTableDetail.setInspectionItemSno("ITEM00" + indexSave);
                                        } else if (indexSave >= 100) {
                                            mstComponentInspectionItemsTableDetail.setInspectionItemSno("ITEM" + indexSave);
                                        } else {
                                            mstComponentInspectionItemsTableDetail.setInspectionItemSno("ITEM0" + indexSave);
                                        }
                                        mstComponentInspectionItemsTableDetail.setRevisionSymbol(itemDetail.getRevisionSymbol());
                                        mstComponentInspectionItemsTableDetail.setDrawingPage(itemDetail.getDrawingPage());
                                        mstComponentInspectionItemsTableDetail.setDrawingAnnotation(itemDetail.getDrawingAnnotation());
                                        mstComponentInspectionItemsTableDetail.setDrawingMentionNo(itemDetail.getDrawingMentionNo());
                                        mstComponentInspectionItemsTableDetail.setSimilarMultiitem(itemDetail.getSimilarMultiitem());
                                        mstComponentInspectionItemsTableDetail.setDrawingArea(itemDetail.getDrawingArea());
                                        mstComponentInspectionItemsTableDetail.setPqs(itemDetail.getPqs());
                                        mstComponentInspectionItemsTableDetail.setInspectionItemName(itemDetail.getInspectionItemName());
                                        mstComponentInspectionItemsTableDetail.setDimensionValue(itemDetail.getDimensionValue());
                                        mstComponentInspectionItemsTableDetail.setTolerancePlus(itemDetail.getTolerancePlus());
                                        mstComponentInspectionItemsTableDetail.setToleranceMinus(itemDetail.getToleranceMinus());
                                        mstComponentInspectionItemsTableDetail.setMeasurementMethod(itemDetail.getMeasurementMethod());
                                        mstComponentInspectionItemsTableDetail.setOutgoingTrialInspectionObject(itemDetail.getOutgoingTrialObject().charAt(0));
                                        mstComponentInspectionItemsTableDetail.setOutgoingProductionInspectionObject(itemDetail.getOutgoingProductionObject().charAt(0));
                                        mstComponentInspectionItemsTableDetail.setIncomingTrialInspectionObject(itemDetail.getIncomingTrialObject().charAt(0));
                                        mstComponentInspectionItemsTableDetail.setIncomingProductionInspectionObject(itemDetail.getIncomingProductionObject().charAt(0));
                                        mstComponentInspectionItemsTableDetail.setOutgoingFirstMassProductionObject(itemDetail.getOutgoingFirstMassProductionObject().charAt(0));
                                        mstComponentInspectionItemsTableDetail.setIncomingFirstMassProductionObject(itemDetail.getIncomingFirstMassProductionObject().charAt(0));
                                        mstComponentInspectionItemsTableDetail.setMeasurementType(itemDetail.getMeasurementType());
                                        mstComponentInspectionItemsTableDetail.setProcessInspetionObject(itemDetail.getProcessInspetionObject().charAt(0));
                                        mstComponentInspectionItemsTableDetail.setAdditionalFlg(itemDetail.getAdditionalFlg().charAt(0));
                                        mstComponentInspectionItemsTableDetail.setLocalSeq(localSeq);
                                        mstComponentInspectionItemsTableDetail.setEnableThAlert(itemDetail.isEnableThAlert());
                                        mstComponentInspectionItemsTableDetail.setCreateDate(date);
                                        mstComponentInspectionItemsTableDetail.setCreateUserUuid(loginUser.getUserUuid());
                                        mstComponentInspectionItemsTableDetail.setUpdateDate(date);
                                        mstComponentInspectionItemsTableDetail.setUpdateUserUuid(loginUser.getUserUuid());
                                        entityManager.persist(mstComponentInspectionItemsTableDetail);
                                        break;
                                    case CommonConstants.OPERATION_FLAG_DELETE:
                                        entityManager.createQuery("DELETE FROM MstComponentInspectionItemsTableDetail m WHERE m.id = :id")
                                                .setParameter("id", itemDetail.getId())
                                                .executeUpdate();

                                        deleteCount++;
                                        break;
                                    default:
                                        Query query = entityManager.createNamedQuery("MstComponentInspectionItemsTableDetail.findById", MstComponentInspectionItemsTableDetail.class)
                                                .setParameter("id", itemDetail.getId());
                                        mstComponentInspectionItemsTableDetail = (MstComponentInspectionItemsTableDetail) query.getSingleResult();
                                        mstComponentInspectionItemsTableDetail.setRevisionSymbol(itemDetail.getRevisionSymbol());
                                        mstComponentInspectionItemsTableDetail.setDrawingPage(itemDetail.getDrawingPage());
                                        mstComponentInspectionItemsTableDetail.setDrawingAnnotation(itemDetail.getDrawingAnnotation());
                                        mstComponentInspectionItemsTableDetail.setDrawingMentionNo(itemDetail.getDrawingMentionNo());
                                        mstComponentInspectionItemsTableDetail.setSimilarMultiitem(itemDetail.getSimilarMultiitem());
                                        mstComponentInspectionItemsTableDetail.setDrawingArea(itemDetail.getDrawingArea());
                                        mstComponentInspectionItemsTableDetail.setPqs(itemDetail.getPqs());
                                        mstComponentInspectionItemsTableDetail.setInspectionItemName(itemDetail.getInspectionItemName());
                                        mstComponentInspectionItemsTableDetail.setDimensionValue(itemDetail.getDimensionValue());
                                        mstComponentInspectionItemsTableDetail.setTolerancePlus(itemDetail.getTolerancePlus());
                                        mstComponentInspectionItemsTableDetail.setToleranceMinus(itemDetail.getToleranceMinus());
                                        mstComponentInspectionItemsTableDetail.setMeasurementMethod(itemDetail.getMeasurementMethod());
                                        mstComponentInspectionItemsTableDetail.setMeasurementType(itemDetail.getMeasurementType());
                                        mstComponentInspectionItemsTableDetail.setOutgoingFirstMassProductionObject(itemDetail.getOutgoingFirstMassProductionObject().charAt(0));
                                        mstComponentInspectionItemsTableDetail.setProcessInspetionObject(itemDetail.getProcessInspetionObject().charAt(0));
                                        mstComponentInspectionItemsTableDetail.setAdditionalFlg(itemDetail.getAdditionalFlg().charAt(0));
                                        mstComponentInspectionItemsTableDetail.setLocalSeq(localSeq);
                                        mstComponentInspectionItemsTableDetail.setEnableThAlert(itemDetail.isEnableThAlert());
                                        mstComponentInspectionItemsTableDetail.setUpdateUserUuid(loginUser.getUserUuid());
                                        mstComponentInspectionItemsTableDetail.setUpdateDate(date);
                                        entityManager.merge(mstComponentInspectionItemsTableDetail);
                                        break;
                                }
                            }
                        }
                    }
                    break;
            }
        }


        return response;
    }

    private void logicUploadItemFile(ComponentInspectionItemsTableInfo componentInspectionItemsTableInfo) {
        TblUploadFile fileUpload = this.entityManager
                .createNamedQuery("TblUploadFile.findByFileUuid", TblUploadFile.class)
                .setParameter("fileUuid", componentInspectionItemsTableInfo.getItemFileUuid())
                .getResultList().stream().findFirst().orElse(null);

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t FROM TblUploadFile t");
        queryBuilder.append(" LEFT JOIN MstComponentInspectionItemsFile m ON m.fileUuid = t.fileUuid"); //ON m.inspectionItemSno = d.inspectionItemSno
        queryBuilder.append(" WHERE t.uploadFileName = :uploadFileName ");
        queryBuilder.append(" AND m.componentId = :componentId ");
        queryBuilder.append(" AND m.outgoingCompanyId = :outgoingCompanyId ");
        queryBuilder.append(" AND m.incomingCompanyId = :incomingCompanyId ");

        List<TblUploadFile> fileUploadList = this.entityManager
                .createQuery(queryBuilder.toString(), TblUploadFile.class)
                .setParameter("componentId", componentInspectionItemsTableInfo.getComponentId())
                .setParameter("outgoingCompanyId", componentInspectionItemsTableInfo.getOutgoingCompanyId())
                .setParameter("incomingCompanyId", componentInspectionItemsTableInfo.getIncomingCompanyId())
                .setParameter("uploadFileName", fileUpload.getUploadFileName())
                .getResultList();


        if (!fileUploadList.isEmpty()) {
            fileUploadList.stream().forEach(fileUploadId -> {
                this.entityManager
                        .createNamedQuery("MstComponentInspectionItemsFile.deleteByUuid", MstComponentInspectionItemsFile.class)
                        .setParameter("fileUuid", fileUploadId.getFileUuid())
                        .executeUpdate();
            });
        }


    }

    /**
     * Hidden list component inspection item by list ids
     * @param listComponentInspectionItems
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateApplyEndDateComponentInspectionItems(List<ComponentInspectionItemsTableInfo> listComponentInspectionItems, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        Date date = new Date();
        for (ComponentInspectionItemsTableInfo itemsTableInfo : listComponentInspectionItems) {
            Query query = entityManager.createNamedQuery("MstComponentInspectionItemsTable.updateApplyEndDateById");
            query.setParameter("id", itemsTableInfo.getComponentInspectionItemsTableId());
            query.setParameter("applyEndDate", date);
            query.setParameter("updateDate", date);
            query.setParameter("updateUserUuid", loginUser.getUserUuid());
            query.executeUpdate();
        }
        return response;
    }
    
    @Transactional
    public List<String> redesplayItems(List<String> itemids, LoginUser loginUser) {
        List<String> failed = new ArrayList<>();
        for(String itemid : itemids) {
            this.entityManager
                .createNamedQuery("MstComponentInspectionItemsTable.findById", MstComponentInspectionItemsTable.class)
                .setParameter("id", itemid)
                .getResultList().stream().findFirst().ifPresent(item -> {
                    int maxver = getMaxNotDeniedVersion(item.getComponentId(), item.getOutgoingCompanyId(), item.getIncomingCompanyId());
                    if(item.getApplyEndDate() == null || (item.getVersion() != maxver && item.getItemApproveStatus() != 2)) {
                        failed.add(itemid);
                    } else {
                        entityManager.createNamedQuery("MstComponentInspectionItemsTable.updateApplyEndDateById")
                            .setParameter("id", item.getId())
                            .setParameter("applyEndDate", null)
                            .setParameter("updateDate", new Date())
                            .setParameter("updateUserUuid", loginUser.getUserUuid())
                            .executeUpdate();
                    }
                });
            
        }
        return failed;
    }

    /**
     * Import inspection items table data by batch
     *
     * @param inputDataList
     * @param incomingCompanyId
     * @return
     */
    @Transactional
    public ComponentInspectionItemsTableRespVo updateInspetionItemsByBatch(List<ComponentInspectionItemsTableForBatch> inputDataList, String incomingCompanyId) {

        ComponentInspectionItemsTableRespVo response = new ComponentInspectionItemsTableRespVo();

        List<String> componentCodeList = inputDataList.stream()
                .map(mapper -> mapper.getComponentCode()).collect(Collectors.toCollection(ArrayList::new));

        // KM-464 部品番号読み替え対応 2017/12/4 by penggd Start
        MstCompany selfCompany = this.mstCompanyService.getSelfCompany();
        String selfCompanyId = selfCompany.getId();

        List<MstComponent> mstComponentList = new ArrayList<MstComponent>();

        if (componentCodeList.size() > 0) {

            for (int i = 0; i < componentCodeList.size(); i++) {

                String tempComponentCode = componentCodeList.get(i);

                // 会社別部品コードマスタから、部品IDを取得する
                List<MstComponentCompany> mstComponentCompanyList = this.entityManager
                        .createNamedQuery("MstComponentCompany.findByCompanyIdAndComponentCode",
                                MstComponentCompany.class)
                        .setParameter("companyId", incomingCompanyId).setParameter("componentCode", tempComponentCode)
                        .getResultList();

                if (mstComponentCompanyList.size() > 0) {

                    MstComponent tempMstComponent = new MstComponent();

                    tempMstComponent.setId(mstComponentCompanyList.get(0).getMstComponentCompanyPK().getComponentId());
                    tempMstComponent.setComponentCode(tempComponentCode);

                    mstComponentList.add(tempMstComponent);
                } else {

                    // 部品マスタから、部品IDを取得する
                    List<MstComponent> componentList = this.entityManager
                            .createNamedQuery("MstComponent.findByComponentCode", MstComponent.class)
                            .setParameter("componentCode", tempComponentCode).getResultList();

                    if (componentList.size() > 0) {

                        mstComponentList.add(componentList.get(0));

                    }
                }

            }

        }
        // KM-464 部品番号読み替え対応 2017/12/4 by penggd End

        Map<String, String> mstComponentMap = new HashMap<>();
        mstComponentList.stream().forEach(action -> mstComponentMap.put(action.getComponentCode(), action.getId()));

        List<String> noExistCode = new ArrayList();
        List<String> noExistId = new ArrayList();
        for (ComponentInspectionItemsTableForBatch inputData : inputDataList) {
            String componentId = mstComponentMap.get(inputData.getComponentCode());
            if (StringUtils.isEmpty(componentId)) {
                noExistCode.add(inputData.getComponentCode());
                noExistId.add(inputData.getInspectionItemsTable().getId());
                continue;
            }

            // ---update local db---
            MstComponentInspectionItemsTable inputItemsTable = inputData.getInspectionItemsTable();
            MstComponentInspectionItemsTable dbData = this.entityManager
                    .createNamedQuery("MstComponentInspectionItemsTable.findById",
                            MstComponentInspectionItemsTable.class)
                    .setParameter("id", inputItemsTable.getId()).getResultList().stream().findFirst().orElse(null);
            if (dbData != null) {
                continue;
            }
            boolean ukDup = this.entityManager
                    .createNamedQuery("MstComponentInspectionItemsTable.findByCondition", MstComponentInspectionItemsTable.class)
                    .setParameter("componentId", componentId)
                    .setParameter("outgoingCompanyId", selfCompanyId)
                    .setParameter("incomingCompanyId", incomingCompanyId)
                    .setParameter("version", inputItemsTable.getVersion()).getResultList().stream().findFirst().isPresent();
            if (ukDup) {
                continue;
            }

            inputItemsTable.setInspBatchUpdateStatus(null);
            // update own id due to these ids are different between different
            // servers
            inputItemsTable.setComponentId(componentId);
            inputItemsTable.setOutgoingCompanyId(selfCompanyId);
            inputItemsTable.setIncomingCompanyId(incomingCompanyId);

            MstComponentInspectionItemsTable old = this.getOldVersionItem(inputItemsTable);
            
            // 検査管理項目の異常処理 2018/2/27 by penggd Start
            try {
                if (old != null) {
                    Calendar startDate = Calendar.getInstance();
                    startDate.setTime(inputItemsTable.getApplyStartDate());
                    // apply start date minus 1 day
                    startDate.add(Calendar.DATE, -1);
                    // set apply end date
                    old.setApplyEndDate(startDate.getTime());
                    inputItemsTable.setMeasSampleRatio(old.getMeasSampleRatio());
                    inputItemsTable.setVisSampleRatio(old.getVisSampleRatio());
                    // update old inspection table
                    this.entityManager.merge(old);
                } else {
                    BigDecimal measSampleRatio = new BigDecimal(cnfSystemService.findByKey("system", "component_insp_measure_sampling_quantity").getConfigValue());
                    inputItemsTable.setMeasSampleRatio(measSampleRatio);
                    BigDecimal visSampleRatio = new BigDecimal(cnfSystemService.findByKey("system", "component_insp_visual_sampling_quantity").getConfigValue());
                    inputItemsTable.setVisSampleRatio(visSampleRatio);
                }
                
                this.entityManager.persist(inputItemsTable);

                // get data
                List<MstComponentInspectionItemsTableDetail> inputDetails = inputData.getInspectionItemsTableDetails();
                
                Map<String, String> resultMap = new HashMap<>();
                
                if (inputDetails.size() > 0) {
                    Map<String, MstComponentInspectionItemsTableDetail> oldDetails = old != null ? getVersionItemDetailsMap(old.getId()) : new HashMap<>();
                    String itemtableDetailMethodId;
                    String containsKey;

                    for (MstComponentInspectionItemsTableDetail action : inputDetails) {

                        itemtableDetailMethodId = (action.getDrawingPage() == null ? "NULL" : action.getDrawingPage())
                                + (action.getDrawingAnnotation() == null ? "NULL" : action.getDrawingAnnotation())
                                + (action.getDrawingMentionNo() == null ? "NULL" : action.getDrawingMentionNo())
                                + (action.getSimilarMultiitem() == null ? "NULL" : action.getSimilarMultiitem());
                        containsKey = itemtableDetailMethodId.concat("|");
                        resultMap.put(containsKey, containsKey);
                        if(oldDetails.containsKey(toDimensionId(action))) {
                            action.setEnableThAlert(oldDetails.get(toDimensionId(action)).isEnableThAlert());
                        } else {
                            action.setEnableThAlert(true);
                        }

                        this.entityManager.persist(action);
                    }
                }
                
                // 非連携項目をコピュー
                if (null != old) {

                    List<MstComponentInspectionItemsTableDetail> oldItemsDetailList = this.entityManager
                            .createQuery("SELECT mDetail FROM MstComponentInspectionItemsTableDetail mDetail "
                                    + "JOIN FETCH MstComponentInspectionItemsTable m "
                                    + "WHERE mDetail.componentInspectionItemsTableId = m.id "
                                    + "AND m.componentId = :componentId "
                                    + "AND m.outgoingCompanyId = :outgoingCompanyId "
                                    + "AND m.incomingCompanyId = :incomingCompanyId "
                                    + "AND m.version = :version "
                                    + "AND mDetail.additionalFlg = :additionalFlg "
                                    + "ORDER BY mDetail.inspectionItemSno", MstComponentInspectionItemsTableDetail.class)
                            .setParameter("componentId", componentId)
                            .setParameter("outgoingCompanyId", selfCompanyId)
                            .setParameter("incomingCompanyId", incomingCompanyId)
                            .setParameter("version", old.getVersion())
                            .setParameter("additionalFlg", CommonConstants.ITEMS_TABLE_ADDITIONAL_FLG_NO.charAt(0)).getResultList();

                    if (oldItemsDetailList.size() > 0) {
                        int itemCount = inputDetails.size();
                        Date nowDate = new Date();
                        for (MstComponentInspectionItemsTableDetail detail : oldItemsDetailList) {
                            
                            // 寸法IDの重複チェックを追加 20181012 start
                            String itemtableDetailMethodId;
                            itemtableDetailMethodId = (detail.getDrawingPage() == null ? "NULL" : detail.getDrawingPage())
                                    + (detail.getDrawingAnnotation() == null ? "NULL" : detail.getDrawingAnnotation())
                                    + (detail.getDrawingMentionNo() == null ? "NULL" : detail.getDrawingMentionNo())
                                    + (detail.getSimilarMultiitem() == null ? "NULL" : detail.getSimilarMultiitem());
                            String containsKey = itemtableDetailMethodId.concat("|");
                            if (resultMap.containsKey(containsKey)) {
                                continue;
                            }
                            // 寸法IDの重複チェックを追加 20181012 end

                           StringBuilder itemSno = new StringBuilder();

                            itemSno.append("ITEM");
                            itemSno.append(FileUtil.addLeftZeroForNum(String.valueOf(itemCount + 1), 3));
                            MstComponentInspectionItemsTableDetail itemDetail = new MstComponentInspectionItemsTableDetail();
                            itemDetail.setInspectionItemSno(itemSno.toString());
                            itemDetail.setLocalSeq(0);
                            itemDetail.setComponentInspectionItemsTableId(inputItemsTable.getId());
                            itemDetail.setId(IDGenerator.generate());
                            itemDetail.setCreateDate(nowDate);
                            itemDetail.setUpdateDate(nowDate);
                            itemDetail.setRevisionSymbol(detail.getRevisionSymbol());
                            itemDetail.setDrawingPage(detail.getDrawingPage());
                            itemDetail.setDrawingAnnotation(detail.getDrawingAnnotation());
                            itemDetail.setDrawingMentionNo(detail.getDrawingMentionNo());
                            itemDetail.setSimilarMultiitem(detail.getSimilarMultiitem());
                            itemDetail.setDrawingArea(detail.getDrawingArea());
                            itemDetail.setPqs(detail.getPqs());
                            itemDetail.setInspectionItemName(detail.getInspectionItemName());
                            itemDetail.setMeasurementType(detail.getMeasurementType());
                            itemDetail.setMeasurementMethod(detail.getMeasurementMethod());
                            itemDetail.setDimensionValue(detail.getDimensionValue());
                            itemDetail.setTolerancePlus(detail.getTolerancePlus());
                            itemDetail.setToleranceMinus(detail.getToleranceMinus());
                            itemDetail.setOutgoingTrialInspectionObject(detail.getOutgoingTrialInspectionObject());
                            itemDetail.setOutgoingProductionInspectionObject(detail.getOutgoingProductionInspectionObject());
                            itemDetail.setIncomingTrialInspectionObject(detail.getIncomingTrialInspectionObject());
                            itemDetail.setIncomingProductionInspectionObject(detail.getIncomingProductionInspectionObject());
                            itemDetail.setOutgoingFirstMassProductionObject(detail.getOutgoingFirstMassProductionObject());
                            itemDetail.setIncomingFirstMassProductionObject(detail.getIncomingFirstMassProductionObject());
                            itemDetail.setProcessInspetionObject(detail.getProcessInspetionObject());
                            itemDetail.setAdditionalFlg(detail.getAdditionalFlg());
                            itemDetail.setCreateUserUuid(detail.getCreateUserUuid());
                            itemDetail.setUpdateUserUuid(detail.getUpdateUserUuid());
                            itemDetail.setEnableThAlert(detail.isEnableThAlert());
                            
                            this.entityManager.persist(itemDetail);
                            itemCount++;
                        }
                    }
                }
                // 検査用の文言選択肢を取込 2017/10/26 by penggd end
            } catch (Exception e) {
                Logger.getLogger(MstComponentInspectionItemsTableService.class.getName()).log(Level.INFO, null, e);
                continue;
            }
            // 検査管理項目の異常処理 2018/2/27 by penggd End

        }

        if (!noExistCode.isEmpty()) {
            response.setError(true);
            response.setErrorMessage(" [" + StringUtils.join(noExistCode.toArray(), "|") + "] not exist in DB.");
            response.setIdList(noExistId);
        }
        return response;
    }

    /*  private methods
    |========================================================================*/
    /**
     * Make where clause
     *
     * @param searchCondition
     * @return
     */
    private String makeWhereClauseByQueryCondition(ComponentInspectionItemsTableSearchCond searchCondition) {

        StringBuilder queryBuilder = new StringBuilder().append(" WHERE 1 = 1");
        if (StringUtils.isNotBlank(searchCondition.getComponent())) {
            queryBuilder.append(" AND (m.mstComponent.componentCode LIKE :component OR m.mstComponent.componentName LIKE :component)");
        }
        
        queryBuilder.append(" AND m.mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId ");
        if (StringUtils.isNotBlank(searchCondition.getComponentInspectType())) {
            queryBuilder.append(" AND m.inspectTypeDictKey = m.mstComponentInspectLang.mstComponentInspectLangPK.dictKey ");
            queryBuilder.append(" AND m.mstComponentInspectLang.dictValue LIKE :componentInspectType");

        }
        
        if (StringUtils.isNotBlank(searchCondition.getOutgoingCompanyName())) {
            queryBuilder.append(" AND (m.mstCompanyOutgoing.companyCode LIKE :outgoingCompanyName OR m.mstCompanyOutgoing.companyName LIKE :outgoingCompanyName)");
        }
        if (StringUtils.isNotBlank(searchCondition.getIncomingCompanyName())) {
            queryBuilder.append(" AND (m.mstCompanyIncoming.companyCode LIKE :incomingCompanyName OR m.mstCompanyIncoming.companyName LIKE :incomingCompanyName)");
        }
        // 検査管理項目の承認ステータス
        if(StringUtils.isNotBlank(searchCondition.getItemApproveStatus())){
            queryBuilder.append(" AND m.itemApproveStatus = :itemApproveStatus");
        }
        if ("0".equals(searchCondition.isWithHistory())) {
            queryBuilder.append(" AND m.applyEndDate IS NULL");
        }
        return queryBuilder.toString();
    }

    /**
     * Set values to search query
     *
     * @param <T>
     * @param typedQuery
     * @param searchCondition
     * @param langId
     */
    private <T> void setValuesToSearchQuery(TypedQuery<T> typedQuery, ComponentInspectionItemsTableSearchCond searchCondition, String langId) {
        if (StringUtils.isNotBlank(searchCondition.getComponent())) {
            typedQuery.setParameter("component", "%" + searchCondition.getComponent() + "%");
        }
        typedQuery.setParameter("langId", langId);
        if (StringUtils.isNotBlank(searchCondition.getComponentInspectType())) {
            typedQuery.setParameter("componentInspectType", "%" + searchCondition.getComponentInspectType() + "%");
        }
        if (StringUtils.isNotBlank(searchCondition.getOutgoingCompanyName())) {
            typedQuery.setParameter("outgoingCompanyName", "%" + searchCondition.getOutgoingCompanyName() + "%");
        }
        if (StringUtils.isNotBlank(searchCondition.getIncomingCompanyName())) {
            typedQuery.setParameter("incomingCompanyName", "%" + searchCondition.getIncomingCompanyName() + "%");
        }
        if (StringUtils.isNotBlank(searchCondition.getItemApproveStatus())) {
            typedQuery.setParameter("itemApproveStatus", Integer.valueOf(searchCondition.getItemApproveStatus()));
        }
    }

    /**
     * Convert to ComponentInspectionItemInfo
     *
     * @param inspectionItemsTable
     * @param langId
     * @return
     */
    private ComponentInspectionItemsTableInfo convertToInspectionItemHeader(MstComponentInspectionItemsTable inspectionItemsTable, String langId) {

        ComponentInspectionItemsTableInfo inspectionItemInfo = new ComponentInspectionItemsTableInfo();
        if (inspectionItemsTable == null) {
            return inspectionItemInfo;
        }
        inspectionItemInfo.setComponentInspectionItemsTableId(inspectionItemsTable.getId());
        inspectionItemInfo.setComponentId(inspectionItemsTable.getMstComponent().getId());
        inspectionItemInfo.setComponentCode(inspectionItemsTable.getMstComponent().getComponentCode());
        inspectionItemInfo.setComponentName(inspectionItemsTable.getMstComponent().getComponentName());
        inspectionItemInfo.setComponentType(inspectionItemsTable.getMstComponent().getComponentType());
        inspectionItemInfo.setIncomingCompanyId(inspectionItemsTable.getIncomingCompanyId());
        inspectionItemInfo.setIncomingCompanyName(inspectionItemsTable.getMstCompanyIncoming().getCompanyName());
        inspectionItemInfo.setOutgoingCompanyId(inspectionItemsTable.getOutgoingCompanyId());
        inspectionItemInfo.setOutgoingCompanyName(inspectionItemsTable.getMstCompanyOutgoing().getCompanyName());
        if (inspectionItemsTable.getVersion() != null) {
            inspectionItemInfo.setVersion(StringUtils.leftPad(inspectionItemsTable.getVersion().toString(), 2, "0"));
        }
        inspectionItemInfo.setEntryPersonName(inspectionItemsTable.getEntryPersonName());
        inspectionItemInfo.setApprovePersonName(inspectionItemsTable.getApprovePersonName());
        inspectionItemInfo.setApproveDate(inspectionItemsTable.getApproveDate());
        inspectionItemInfo.setApplyStartDate(inspectionItemsTable.getApplyStartDate());
        inspectionItemInfo.setApplyEndDate(inspectionItemsTable.getApplyEndDate());
        //----------------Apeng 20171026 add
        inspectionItemInfo.setInseptionTypeId(inspectionItemsTable.getInspectTypeId());
        inspectionItemInfo.setInseptionTypeKey(inspectionItemsTable.getInspectTypeDictKey());
        if (inspectionItemsTable.getMstComponentInspectLang() != null) {
            inspectionItemInfo.setInseptionTypeValue(inspectionItemsTable.getMstComponentInspectLang().getDictValue());
        }else {
            inspectionItemInfo.setInseptionTypeValue("");
        }
        inspectionItemInfo.setMeasSampleRatio(inspectionItemsTable.getMeasSampleRatio());
        inspectionItemInfo.setVisSampleRatio(inspectionItemsTable.getVisSampleRatio());
        // 検査管理項目承認コメント
        inspectionItemInfo.setItemApproveComment(inspectionItemsTable.getItemApproveComment());
        // 検査管理項目承認ステータス
        inspectionItemInfo.setItemApproveStatus(inspectionItemsTable.getItemApproveStatus());
        // 検査管理項目承認ステータステキスト
        MstChoice mstChoice = mstChoiceService.getBySeqChoice(String.valueOf(inspectionItemsTable.getItemApproveStatus()), langId, ITEM_APPROVE_STATUS);
        if(null != mstChoice){
           inspectionItemInfo.setItemApproveStatusText(mstChoice.getChoice());
        }

        return inspectionItemInfo;
    }

    /**
     * Convert to InspectionItemDetailList for detail page
     *
     * @param inspectionItemsTableDetails
     * @return
     */
    private List<ComponentInspectionItemsTableDetail> convertToInspectionItemDetailList(
            List<MstComponentInspectionItemsTableDetail> inspectionItemsTableDetails) {

        List<ComponentInspectionItemsTableDetail> itemDetails = new ArrayList<>();
        inspectionItemsTableDetails.stream().forEach(inspectionItemDetail -> {
            ComponentInspectionItemsTableDetail itemDetail = new ComponentInspectionItemsTableDetail();
            itemDetail.setId(inspectionItemDetail.getId());
            itemDetail.setInspectionItemSno(inspectionItemDetail.getInspectionItemSno());
            itemDetail.setRevisionSymbol(inspectionItemDetail.getRevisionSymbol());
            itemDetail.setDrawingPage(inspectionItemDetail.getDrawingPage());
            itemDetail.setDrawingAnnotation(inspectionItemDetail.getDrawingAnnotation());
            itemDetail.setDrawingMentionNo(inspectionItemDetail.getDrawingMentionNo());
            itemDetail.setSimilarMultiitem(inspectionItemDetail.getSimilarMultiitem());
            itemDetail.setDrawingArea(inspectionItemDetail.getDrawingArea());
            itemDetail.setPqs(inspectionItemDetail.getPqs());
            //itemDetail.setInspectionItemNum(inspectionItemDetail.getInspectionItemNum());
            itemDetail.setInspectionItemName(inspectionItemDetail.getInspectionItemName());
            itemDetail.setMeasurementMethod(inspectionItemDetail.getMeasurementMethod());
            itemDetail.setDimensionValue(inspectionItemDetail.getDimensionValue());
            itemDetail.setTolerancePlus(inspectionItemDetail.getTolerancePlus());
            itemDetail.setToleranceMinus(inspectionItemDetail.getToleranceMinus());
            itemDetail.setOutgoingTrialObject(inspectionItemDetail.getOutgoingTrialInspectionObject().toString());
            itemDetail.setOutgoingProductionObject(inspectionItemDetail.getOutgoingProductionInspectionObject().toString());
            itemDetail.setIncomingTrialObject(inspectionItemDetail.getIncomingTrialInspectionObject().toString());
            itemDetail.setIncomingProductionObject(inspectionItemDetail.getIncomingProductionInspectionObject().toString());
            itemDetail.setOutgoingFirstMassProductionObject(inspectionItemDetail.getOutgoingFirstMassProductionObject().toString());
            itemDetail.setIncomingFirstMassProductionObject(inspectionItemDetail.getIncomingFirstMassProductionObject().toString());
            itemDetail.setMeasurementType(inspectionItemDetail.getMeasurementType());

            itemDetail.setLocalSeq(inspectionItemDetail.getLocalSeq());
            itemDetail.setEnableThAlert(inspectionItemDetail.isEnableThAlert());
            //Apeng 20180130 add start
            itemDetail.setProcessInspetionObject(inspectionItemDetail.getProcessInspetionObject().toString());
            itemDetail.setAdditionalFlg(inspectionItemDetail.getAdditionalFlg().toString());
            //Apeng 20180130 add end
            itemDetails.add(itemDetail);
        });
        return itemDetails;
    }

    private List<ComponentInspectionItemsFileDetail> convertToInspectionItemFileList (
            List<MstComponentInspectionItemsFile> inspectionItemsFileDetails) {

        List<ComponentInspectionItemsFileDetail> itemFileDetails = new ArrayList<>();
        inspectionItemsFileDetails.stream().forEach(inspectionItemFileDetail -> {
            TblUploadFile fileUpload = this.entityManager
                    .createNamedQuery("TblUploadFile.findByFileUuid", TblUploadFile.class)
                    .setParameter("fileUuid", inspectionItemFileDetail.getFileUuid())
                    .getResultList().stream().findFirst().orElse(null);
            ComponentInspectionItemsFileDetail itemFileDetail = new ComponentInspectionItemsFileDetail();
            itemFileDetail.setId(inspectionItemFileDetail.getId());
            itemFileDetail.setComponentId(inspectionItemFileDetail.getComponentId());
            itemFileDetail.setOutgoingCompanyId(inspectionItemFileDetail.getOutgoingCompanyId());
            itemFileDetail.setIncomingCompanyId(inspectionItemFileDetail.getIncomingCompanyId());
            itemFileDetail.setFileUuid(inspectionItemFileDetail.getFileUuid());

            if (fileUpload != null) {
                itemFileDetail.setUploadDate(fileUpload.getUploadDate());
                itemFileDetail.setFileName(fileUpload.getUploadFileName());
            }
            itemFileDetails.add(itemFileDetail);
        });
        return itemFileDetails;
    }

    /**
     * Get MstComponentInspectLang
     *
     * @return
     */
    public List<MstComponentInspectLang> getMstComponentInspectLang() {

        List<MstComponentInspectLang> inspectLangList = this.entityManager
                .createNamedQuery("MstComponentInspectLang.findAll", MstComponentInspectLang.class).getResultList();

        return inspectLangList;
    }

    /**
     * 検査用の文言の存在チェック
     *
     * @param inspectLang
     * @return
     */
    private boolean chkInspectLang(MstComponentInspectLang inspectLang) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(*) FROM MstComponentInspectLang m");
        queryBuilder.append(
                " WHERE m.mstComponentInspectLangPK.dictKey = :dictKey AND m.mstComponentInspectLangPK.langId = :langId ");

        try {

            MstComponentInspectLang mstComponentInspectLang = this.entityManager
                    .createNamedQuery("MstComponentInspectLang.findByPk", MstComponentInspectLang.class)
                    .setParameter("dictKey", inspectLang.getMstComponentInspectLangPK().getDictKey())
                    .setParameter("langId", inspectLang.getMstComponentInspectLangPK().getLangId()).getSingleResult();

            return true;

        } catch (NoResultException e) {

            return false;
        }

    }
    
    /**
     * 使用されている非連携管理項目の判断
     * 
     * @param componentInspectionItemsTableId
     * @param inspectionItemSno
     * @param langId
     * @return 
     */
    public BasicResponse getTblComponentInspectionResultExist(String componentInspectionItemsTableId,String inspectionItemSno, String langId) {
        BasicResponse response = new BasicResponse();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT COUNT(1) FROM TblComponentInspectionResult t");
        queryBuilder.append(" JOIN FETCH TblComponentInspectionResultDetail d ON d.componentInspectionResultId = t.id");
        queryBuilder.append(" JOIN FETCH MstComponentInspectionItemsTableDetail m ON m.inspectionItemSno = d.inspectionItemSno");
        queryBuilder.append(" WHERE t.componentInspectionItemsTableId = :componentInspectionItemsTableId");
        queryBuilder.append(" AND m.componentInspectionItemsTableId = :componentInspectionItemsTableId");
        queryBuilder.append(" AND m.inspectionItemSno = :inspectionItemSno");

        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("componentInspectionItemsTableId", componentInspectionItemsTableId);
        query.setParameter("inspectionItemSno", inspectionItemSno);
        int count = Integer.parseInt(query.getSingleResult().toString());
        if (count > 0) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(langId, "msg_cannot_delete_used_record");
            response.setErrorMessage(msg);
        }
        return response;
    }
    
    /**
     * 検査管理項目のCSV出力、圧縮処理を行う
     *
     * @param componentInspectionItemsTableInfoList
     * @param langId
     * @return
     */
    public FileReponse getInspectionItemDownloadZip(List<ComponentInspectionItemsTableInfo> componentInspectionItemsTableInfoList, String langId) {

        FileReponse response = new FileReponse();

        String fileUuid = IDGenerator.generate();
        
        // 管理項目のCSV出力用のリスト（部品コード、部品業種、バージョン）
        Collections.sort(componentInspectionItemsTableInfoList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                ComponentInspectionItemsTableInfo obj1 = (ComponentInspectionItemsTableInfo) o1;
                ComponentInspectionItemsTableInfo obj2 = (ComponentInspectionItemsTableInfo) o2;

                // 比較用の部品コード
                String componentCode1 = obj1.getComponentCode();
                String componentCode2 = obj2.getComponentCode();

                // 比較用の業種
                String inseptionTypeValue1 = obj1.getInseptionTypeValue();
                String inseptionTypeValue2 = obj2.getInseptionTypeValue();

                // 比較用のバージョン
                String version1 = obj1.getVersion();
                String version2 = obj2.getVersion();

                if (componentCode1.equals(componentCode2)) {

                    if (inseptionTypeValue1.equals(inseptionTypeValue2)) {

                        return version2.compareTo(version1);

                    } else {

                        return inseptionTypeValue1.compareTo(inseptionTypeValue2);

                    }
                } else {

                    return componentCode1.compareTo(componentCode2);
                }

            }
        });

        String folderPath = kartePropertyService.getDocumentDirectory();
        StringBuilder oupPutPath = new StringBuilder();
        oupPutPath.append(folderPath);
        oupPutPath.append(FileUtil.SEPARATOR);
        oupPutPath.append(CommonConstants.CSV);
        oupPutPath.append(FileUtil.SEPARATOR);
        oupPutPath.append(fileUuid);
        FileUtil fu = new FileUtil();
        fu.createPath(oupPutPath.toString());
        oupPutPath.append(FileUtil.SEPARATOR);

        Map<String, String> headerMap = getDictionaryList(langId);

        /*Head*/
        ArrayList<ArrayList> gLineList;
        ArrayList headList = new ArrayList();

        headList.add(headerMap.get("revision_symbol")); // 改訂記号
        headList.add(headerMap.get("drawing_page")); // 図面頁
        headList.add(headerMap.get("drawing_annotation")); // 図面注記(N)
        headList.add(headerMap.get("drawing_mention_no")); // 図面記載No
        headList.add(headerMap.get("similar_multiitem")); // 同項目複数個所
        headList.add(headerMap.get("drawing_area")); // 図面エリア
        headList.add(headerMap.get("pqs")); // PQS
        headList.add(headerMap.get("inspection_item_names")); // 検査項目
        headList.add(headerMap.get("dimension_value")); // 規格値
        headList.add(headerMap.get("tolerance_plus")); // 公差正
        headList.add(headerMap.get("tolerance_minus")); // 公差負
        headList.add(headerMap.get("measurement_method")); // 測定器
        headList.add(headerMap.get("outgoing_trial_inspection_object")); // 出荷初物検査
        headList.add(headerMap.get("outgoing_production_inspection_object")); // 出荷量産検査
        headList.add(headerMap.get("incoming_trial_inspection_object")); // 受入初物検査
        headList.add(headerMap.get("incoming_production_inspection_object")); // 受入量産検査
        headList.add(headerMap.get("measurement_type")); // 測定タイプ
        headList.add(headerMap.get("procedure_internal_inspection")); // 工程内検査
        headList.add(headerMap.get("additional_flg")); // 非連携フラグ
        headList.add(headerMap.get("outgoing_first_mass_production_object")); // 出荷量産第一ロットフラグ
        headList.add(headerMap.get("incoming_first_mass_production_object")); // 入荷量産第一ロットフラグ
        headList.add(headerMap.get("enable_th_alert"));
        
        // 比較用の部品コード
        String tempComponentCode = "";
        String tempInseptionTypeValue = "";
        int seq = 0;
        // 比較用の部品業種

        for (ComponentInspectionItemsTableInfo componentInspectionItemsTableInfo : componentInspectionItemsTableInfoList) {
            
            if (tempComponentCode.equals(componentInspectionItemsTableInfo.getComponentCode()) && tempInseptionTypeValue.equals(componentInspectionItemsTableInfo.getInseptionTypeValue())) {
                seq++;
            } else {
                seq = 0;
            }

            gLineList = new ArrayList();

            gLineList.add(headList);

            // CSV出力パス
            StringBuilder csvOupPutPath = new StringBuilder();
            csvOupPutPath.append(oupPutPath.toString());

            String sql = "SELECT m FROM MstComponentInspectionItemsTableDetail m WHERE m.componentInspectionItemsTableId = :componentInspectionItemsTableId ORDER BY m.inspectionItemSno";

            List<MstComponentInspectionItemsTableDetail> mstComponentInspectionItemsTableDetailList = entityManager.createQuery(
                    sql, MstComponentInspectionItemsTableDetail.class)
                    .setParameter("componentInspectionItemsTableId", componentInspectionItemsTableInfo.getComponentInspectionItemsTableId()).getResultList();

            /*DATA*/
            ArrayList lineList;

            for (MstComponentInspectionItemsTableDetail mstComponentInspectionItemsTableDetail : mstComponentInspectionItemsTableDetailList) {

                lineList = new ArrayList();

                // 改訂記号
                if (StringUtils.isNotEmpty(mstComponentInspectionItemsTableDetail.getRevisionSymbol())) {
                    lineList.add(mstComponentInspectionItemsTableDetail.getRevisionSymbol());
                } else {
                    lineList.add("");
                }

                // 図面頁
                if (StringUtils.isNotEmpty(mstComponentInspectionItemsTableDetail.getDrawingPage())) {
                    lineList.add(mstComponentInspectionItemsTableDetail.getDrawingPage());
                } else {
                    lineList.add("");
                }

                // 図面注記(N)
                if (StringUtils.isNotEmpty(mstComponentInspectionItemsTableDetail.getDrawingAnnotation())) {
                    lineList.add(mstComponentInspectionItemsTableDetail.getDrawingAnnotation());
                } else {
                    lineList.add("");
                }

                // 図面記載No
                if (StringUtils.isNotEmpty(mstComponentInspectionItemsTableDetail.getDrawingMentionNo())) {
                    lineList.add(mstComponentInspectionItemsTableDetail.getDrawingMentionNo());
                } else {
                    lineList.add("");
                }

                // 同項目複数個所
                if (StringUtils.isNotEmpty(mstComponentInspectionItemsTableDetail.getSimilarMultiitem())) {
                    lineList.add(mstComponentInspectionItemsTableDetail.getSimilarMultiitem());
                } else {
                    lineList.add("");
                }

                // 図面エリア
                if (StringUtils.isNotEmpty(mstComponentInspectionItemsTableDetail.getDrawingArea())) {
                    lineList.add(mstComponentInspectionItemsTableDetail.getDrawingArea());
                } else {
                    lineList.add("");
                }

                // PQS
                if (StringUtils.isNotEmpty(mstComponentInspectionItemsTableDetail.getPqs())) {
                    lineList.add(mstComponentInspectionItemsTableDetail.getPqs());
                } else {
                    lineList.add("");
                }

                // 検査項目
                if (StringUtils.isNotEmpty(mstComponentInspectionItemsTableDetail.getInspectionItemName())) {
                    lineList.add(mstComponentInspectionItemsTableDetail.getInspectionItemName());
                } else {
                    lineList.add("");
                }

                // 規格値
                if (null != mstComponentInspectionItemsTableDetail.getDimensionValue()) {
                    lineList.add(mstComponentInspectionItemsTableDetail.getDimensionValue().toString());
                } else {
                    lineList.add("");
                }

                // 公差正
                if (null != mstComponentInspectionItemsTableDetail.getTolerancePlus()) {
                    lineList.add(mstComponentInspectionItemsTableDetail.getTolerancePlus().toString());
                } else {
                    lineList.add("");
                }

                // 公差負
                if (null != mstComponentInspectionItemsTableDetail.getToleranceMinus()) {
                    lineList.add(mstComponentInspectionItemsTableDetail.getToleranceMinus().toString());
                } else {
                    lineList.add("");
                }

                // 測定器
                if (StringUtils.isNotEmpty(mstComponentInspectionItemsTableDetail.getMeasurementMethod())) {
                    lineList.add(mstComponentInspectionItemsTableDetail.getMeasurementMethod());
                } else {
                    lineList.add("");
                }

                // 出荷初物検査
                lineList.add(String.valueOf(mstComponentInspectionItemsTableDetail.getOutgoingTrialInspectionObject()));

                // 出荷量産検査
                lineList.add(String.valueOf(mstComponentInspectionItemsTableDetail.getOutgoingProductionInspectionObject()));

                // 入荷初物検査
                lineList.add(String.valueOf(mstComponentInspectionItemsTableDetail.getIncomingTrialInspectionObject()));

                // 入荷量産検査
                lineList.add(String.valueOf(mstComponentInspectionItemsTableDetail.getIncomingProductionInspectionObject()));

                // 測定タイプ
                lineList.add(String.valueOf(mstComponentInspectionItemsTableDetail.getMeasurementType()));

                // 工程内検査
                lineList.add(String.valueOf(mstComponentInspectionItemsTableDetail.getProcessInspetionObject()));

                // 非連携フラグ
                lineList.add(String.valueOf(mstComponentInspectionItemsTableDetail.getAdditionalFlg()));
                
                // 出荷量産第一ロット検査
                lineList.add(String.valueOf(mstComponentInspectionItemsTableDetail.getOutgoingFirstMassProductionObject()));

                // 入荷第一量産ロット検査
                lineList.add(String.valueOf(mstComponentInspectionItemsTableDetail.getIncomingFirstMassProductionObject()));
                
                lineList.add(mstComponentInspectionItemsTableDetail.isEnableThAlert() ? "1" : "0");

                gLineList.add(lineList);
            }

            if (0 == seq) {
                csvOupPutPath.append(SEPARATOR).append(componentInspectionItemsTableInfo.getComponentCode())
                        .append("_").append(componentInspectionItemsTableInfo.getInseptionTypeValue())
                        .append(CommonConstants.EXT_CSV);
            } else {
                csvOupPutPath.append(SEPARATOR).append(componentInspectionItemsTableInfo.getComponentCode())
                        .append("_").append(componentInspectionItemsTableInfo.getInseptionTypeValue()).append("_").append(String.valueOf(seq))
                        .append(CommonConstants.EXT_CSV);
            }            
            
            fu.createFile(csvOupPutPath.toString());

            // CSV出力
            CSVFileUtil.writeCsv(csvOupPutPath.toString(), gLineList);
            
            tempComponentCode = componentInspectionItemsTableInfo.getComponentCode();
            tempInseptionTypeValue = componentInspectionItemsTableInfo.getInseptionTypeValue();

        }

        // ZIPファイル名：inspect_results.zipとする
        boolean charsetFlg = false;
        if (LANGID.equalsIgnoreCase(langId)) {
            charsetFlg = true;
        }

        ZipCompressor.zipDirectory(oupPutPath.toString(), fileUuid, charsetFlg);

        response.setFileUuid(fileUuid);

        return response;
    }
    
    /**
     * 検査管理項目のCSV出力用の文言を取得
     *
     * @param searchCondition
     * @param langId
     * @return
     */
    private Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("revision_symbol", "drawing_page", "drawing_annotation", "drawing_mention_no",
                "similar_multiitem", "drawing_area", "pqs", "inspection_item_names", "dimension_value", "tolerance_plus", "tolerance_minus", "measurement_method",
                "outgoing_trial_inspection_object", "outgoing_production_inspection_object", "incoming_trial_inspection_object", "incoming_production_inspection_object",
                "measurement_type", "procedure_internal_inspection", "additional_flg", "outgoing_first_mass_production_object", "incoming_first_mass_production_object", "enable_th_alert");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        return headerMap;
    }
    
     /**
     * getInspectionItemsTableByIdLangId
     *
     * @param componentInspectionItemsTableId
     * @param langId
     * @return
     */
    public MstComponentInspectionItemsTable getInspectionItemsTableByIdLangId(String componentInspectionItemsTableId, String langId) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m FROM MstComponentInspectionItemsTable m");
        queryBuilder.append(" JOIN FETCH m.mstComponentInspectLang mstComponentInspectLang");
        queryBuilder.append(" WHERE m.id = :id");
        queryBuilder.append(" AND mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId");

        MstComponentInspectionItemsTable inspectionItemsTable = this.entityManager
                .createQuery(queryBuilder.toString(), MstComponentInspectionItemsTable.class)
                .setParameter("id", componentInspectionItemsTableId)
                .setParameter("langId", langId)
                .getResultList().stream().findFirst().orElse(null);

        return inspectionItemsTable;

    }
    
    public Integer getCopyVersion(String componentId, String outgoingCompanyId, String incomingCompanyId) {

        Integer version = 0;

        String sql = "SELECT m FROM MstComponentInspectionItemsTable m "
                + "WHERE m.componentId = :componentId "
                + "AND m.outgoingCompanyId = :outgoingCompanyId "
                + "AND m.incomingCompanyId = :incomingCompanyId "
                + "AND m.applyEndDate IS NULL "
                + "AND m.itemApproveStatus = 1 "
                + "ORDER BY m.version DESC";

        MstComponentInspectionItemsTable itemTable = this.entityManager
                .createQuery(sql, MstComponentInspectionItemsTable.class)
                .setParameter("componentId", componentId)
                .setParameter("outgoingCompanyId", outgoingCompanyId)
                .setParameter("incomingCompanyId", incomingCompanyId)
                .getResultList().stream().findFirst().orElse(null);

        if (null != itemTable) {
            return itemTable.getVersion();
        }

        return version;
    }
}
