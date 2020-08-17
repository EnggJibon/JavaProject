/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.component.company.MstComponentCompany;
import com.kmcj.karte.resources.component.inspection.defect.TblComponentInspectionDefect;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectClass;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectClass.PK;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectFile;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectFileService;
import com.kmcj.karte.resources.component.inspection.item.MstComponentInspectionItemsTable;
import com.kmcj.karte.resources.component.inspection.item.MstComponentInspectionItemsTableDetail;
import com.kmcj.karte.resources.component.inspection.item.MstComponentInspectionItemsTableService;
import com.kmcj.karte.resources.component.inspection.item.model.ComponentInspectionItemsFileDetail;
import com.kmcj.karte.resources.component.inspection.item.model.MstComponentInspectionItemsFile;
import com.kmcj.karte.resources.component.inspection.item.model.MstComponentInspectionItemsTableClassVo;
import com.kmcj.karte.resources.component.inspection.referencefile.TblComponentInspectionReferenceFile;
import com.kmcj.karte.resources.component.inspection.referencefile.TblComponentInspectionReferenceFileNewest;
import com.kmcj.karte.resources.component.inspection.referencefile.TblComponentInspectionReferenceFileService;
import com.kmcj.karte.resources.component.inspection.referencefile.model.ComponentInspectionReferenceFile;
import com.kmcj.karte.resources.component.inspection.result.model.*;
import com.kmcj.karte.resources.component.inspection.result.model.ComponentInspectionItemResultDetail.SamplingInspectionOutgoingResult;
import com.kmcj.karte.resources.component.inspection.result.model.ComponentInspectionItemResultDetail.SamplingInspectionResult;
import com.kmcj.karte.resources.component.inspection.result.model.ComponentInspectionItemResultDetail.SeqInspectionOutgoingResult;
import com.kmcj.karte.resources.component.inspection.visualimage.TblComponentInspectionVisualImageFile;
import com.kmcj.karte.resources.component.inspection.visualimage.TblComponentInspectionVisualImageFilePK;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.po.TblPo;
import com.kmcj.karte.resources.po.shipment.TblShipment;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.MstUserService;
import com.kmcj.karte.util.*;
import org.apache.commons.lang.StringUtils;

import static com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResult.ConfirmResult;
import static com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResult.ApproveResult;
import static com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResult.AbortFlg;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Master Component inspection result service
 *
 * @author duanlin
 */
@Dependent
public class TblComponentInspectionResultService {

    private static final String SPLIT_REGEX = "|";

    private final static String TEMPLATE_FOLDER = "template";

    private final static String TEMPLATE_FILE_OUTGOING = "inspect_result_outgoing.xlsx";

    private final static String TEMPLATE_FILE_INCOMING = "inspect_result_incoming.xlsx";

    public final static String INSPECT_RESULT = "inspect_result_";

    private final static String UNDER_YOKO = "_";

    private static final String LANGID = "ja";

    private static final String INSPECT_TYPE = "inspect_type_";
    
    private static final String DAY = "day";
    
    private static final String WEEK = "week";
    
    private static final String MONTH = "month";

    private final static Map<String, String> orderKey = new HashMap<>();
    static {
        orderKey.put("inspectionStatus", " ORDER BY t.inspectionStatus, t.fileConfirmStatus ");
        orderKey.put("fileConfirmStatus", " ORDER BY t.fileConfirmStatus ");
        orderKey.put("componentCode", " ORDER BY t.mstComponent.componentCode ");
        orderKey.put("componentName", " ORDER BY t.mstComponent.componentName ");
        orderKey.put("componentType", " ORDER BY t.mstComponent.componentType ");
        orderKey.put("incomingCompanyName", " ORDER BY t.mstCompanyIncoming.companyName ");
        orderKey.put("outgoingCompanyName", " ORDER BY t.mstCompanyOutgoing.companyName ");
        orderKey.put("firstFlagName", " ORDER BY t.classLang.dictValue ");
        orderKey.put("quantity", " ORDER BY t.quantity ");
        orderKey.put("outgoingMeasSamplingQuantity", " ORDER BY t.outgoingMeasSamplingQuantity ");
        orderKey.put("outgoingVisualSamplingQuantity", " ORDER BY t.outgoingVisualSamplingQuantity ");
        orderKey.put("incomingMeasSamplingQuantity", " ORDER BY t.incomingMeasSamplingQuantity ");
        orderKey.put("incomingVisualSamplingQuantity", " ORDER BY t.incomingVisualSamplingQuantity ");
        orderKey.put("outgoingInspectionResult", " ORDER BY t.outgoingInspectionResult ");
        orderKey.put("outgoingInspectionDate", " ORDER BY t.outgoingInspectionDate ");
        orderKey.put("outgoingInspectionPersonName", " ORDER BY t.outgoingInspectionPersonName ");
        orderKey.put("outgoingConfirmResult", " ORDER BY t.outgoingConfirmResult ");
        orderKey.put("outgoingConfirmDate", " ORDER BY t.outgoingConfirmDate ");
        orderKey.put("outgoingConfirmerName", " ORDER BY t.outgoingConfirmerName ");
        orderKey.put("outgoingApproveResult", " ORDER BY t.outgoingApproveResult ");
        orderKey.put("outgoingInspectionApproveDate", " ORDER BY t.outgoingInspectionApproveDate ");
        orderKey.put("outgoingInspectionApprovePersonName", " ORDER BY t.outgoingInspectionApprovePersonName ");
        orderKey.put("incomingInspectionResult", " ORDER BY t.incomingInspectionResult ");
        orderKey.put("incomingInspectionDate", " ORDER BY t.incomingInspectionDate ");
        orderKey.put("incomingInspectionPersonName", " ORDER BY t.incomingInspectionPersonName ");
        orderKey.put("incomingConfirmResult", " ORDER BY t.incomingConfirmResult ");
        orderKey.put("incomingConfirmDate", " ORDER BY t.incomingConfirmDate ");
        orderKey.put("incomingConfirmerName", " ORDER BY t.incomingConfirmerName ");
        orderKey.put("incomingApproveResult", " ORDER BY t.incomingApproveResult ");
        orderKey.put("incomingInspectionApproveDate", " ORDER BY t.incomingInspectionApproveDate ");
        orderKey.put("incomingInspectionApprovePersonName", " ORDER BY t.incomingInspectionApprovePersonName ");
        orderKey.put("createDate", " ORDER BY t.createDate ");
        orderKey.put("updateDate", " ORDER BY t.updateDate ");
        orderKey.put("cavityCnt", " ORDER BY t.cavityCnt ");
        orderKey.put("cavityPrefixStart", " ORDER BY t.cavityPrefix %sord%, t.cavityStartNum ");
        orderKey.put("componentInspectTypeName", " ORDER BY t.mstTable.mstComponentInspectLang.dictValue ");
        orderKey.put("fileApproverName", " ORDER BY t.fileApproverName ");
    }
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;
    @Inject
    private MstComponentInspectionItemsTableService mstComponentInspectionItemsTableService;
    @Inject
    private MstUserService mstUserService;
    @Inject
    private TblComponentInspectionReferenceFileService tblComponentInspectionReferenceFileService;
    @Inject
    private MstCompanyService mstCompanyService;
    @Inject
    private KartePropertyService kartePropertyService;
    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private TblCsvExportService tblCsvExportService;
    @Inject
    private MstComponentInspectFileService mstComponentInspectFileService;
    @Inject
    private MstComponentService mstComponentService;
    
    @Inject
    private TblComponentInsectionSendMailService tblComponentInsectionSendMailService;

    @Inject
    private MstChoiceService mstChoiceService;
    
    @Inject
    private CnfSystemService cnfSystemService;

    /**
     * Get count for component inspection result search
     *
     * @param searchCondition
     * @param langId
     * @return
     */
    public int getSearchCount(ComponentInspectionResultSearchCond searchCondition, String langId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT COUNT(t.id) FROM TblComponentInspectionResult t");
        queryBuilder.append(" JOIN FETCH t.mstComponent");
        queryBuilder.append(" JOIN FETCH t.mstCompanyOutgoing");
        queryBuilder.append(" JOIN FETCH t.mstCompanyIncoming");
        queryBuilder.append(" JOIN FETCH t.classLang");
        queryBuilder.append(" JOIN FETCH t.mstTable.mstComponentInspectLang");
        if (StringUtils.isNotBlank(searchCondition.getFileConfirmStatus())) {
            queryBuilder.append(" , (SELECT tf.componentInspectionResultId componentInspectionResultId,  "
                    + " ( "
                        + " (CASE WHEN tf.drawingFileFlg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.proofFileFlg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.rohsProofFileFlg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.packageSpecFileFlg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.qcPhaseFileFlg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file06Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file07Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file08Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file09Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file10Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file11Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file12Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file13Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file14Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file15Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file16Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file17Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file18Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file19Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file20Flg = '1' THEN 1 ELSE 0 END) "
                    + " ) mstFileCount, "
                    + " ( "
                        + " (CASE WHEN (tf.drawingFileStatus = 'CONFIRMED' OR tf.drawingFileStatus = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.proofFileStatus = 'CONFIRMED' OR tf.proofFileStatus = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.rohsProofFileStatus = 'CONFIRMED' OR tf.rohsProofFileStatus = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.packageSpecFileStatus = 'CONFIRMED' OR tf.packageSpecFileStatus = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.qcPhaseFileStatus = 'CONFIRMED' OR tf.qcPhaseFileStatus = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file06Status = 'CONFIRMED' OR tf.file06Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file07Status = 'CONFIRMED' OR tf.file07Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file08Status = 'CONFIRMED' OR tf.file08Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file09Status = 'CONFIRMED' OR tf.file09Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file10Status = 'CONFIRMED' OR tf.file10Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file11Status = 'CONFIRMED' OR tf.file11Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file12Status = 'CONFIRMED' OR tf.file12Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file13Status = 'CONFIRMED' OR tf.file13Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file14Status = 'CONFIRMED' OR tf.file14Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file15Status = 'CONFIRMED' OR tf.file15Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file16Status = 'CONFIRMED' OR tf.file16Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file17Status = 'CONFIRMED' OR tf.file17Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file18Status = 'CONFIRMED' OR tf.file18Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file19Status = 'CONFIRMED' OR tf.file19Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file20Status = 'CONFIRMED' OR tf.file20Status = 'SKIP') THEN 1 ELSE 0 END) "
                    + " ) fileConfirmedCount "
                    + " FROM TblComponentInspectionReferenceFile tf ) ref ");
        }
        queryBuilder.append(this.makeWhereClauseByQueryCondition(searchCondition, false));
        queryBuilder.append(" AND t.classLang.mstComponentInspectLangPK.langId = :langId");
        queryBuilder.append(" AND t.mstTable.mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId");
        queryBuilder.append(" ORDER BY t.mstComponent.componentCode");

        TypedQuery<Long> query = this.entityManager.createQuery(queryBuilder.toString(), Long.class);

        this.setValuesToSearchQuery(query, searchCondition, false, langId);

        return query.getSingleResult().intValue();
    }

    /**
     * Get component inspection result list
     *
     * @param searchCondition
     * @param isCsv
     * @param langId
     * @return
     */
    public List<ComponentInspectionResultInfo> getInspectionResults(ComponentInspectionResultSearchCond searchCondition, boolean isCsv, String langId) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t FROM TblComponentInspectionResult t");
        queryBuilder.append(" JOIN FETCH t.mstComponent");
        queryBuilder.append(" JOIN FETCH t.mstCompanyOutgoing");
        queryBuilder.append(" JOIN FETCH t.mstCompanyIncoming");
        queryBuilder.append(" JOIN FETCH t.classLang");
        queryBuilder.append(" JOIN FETCH t.mstTable.mstComponentInspectLang");
        if (StringUtils.isNotBlank(searchCondition.getFileConfirmStatus())) {
            queryBuilder.append(" , (SELECT tf.componentInspectionResultId componentInspectionResultId, "
                    + " ( "
                        + " (CASE WHEN tf.drawingFileFlg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.proofFileFlg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.rohsProofFileFlg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.packageSpecFileFlg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.qcPhaseFileFlg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file06Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file07Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file08Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file09Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file10Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file11Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file12Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file13Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file14Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file15Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file16Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file17Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file18Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file19Flg = '1' THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN tf.file20Flg = '1' THEN 1 ELSE 0 END) "
                    + " ) mstFileCount, "
                    + " ( "
                        + " (CASE WHEN (tf.drawingFileStatus = 'CONFIRMED' OR tf.drawingFileStatus = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.proofFileStatus = 'CONFIRMED' OR tf.proofFileStatus = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.rohsProofFileStatus = 'CONFIRMED' OR tf.rohsProofFileStatus = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.packageSpecFileStatus = 'CONFIRMED' OR tf.packageSpecFileStatus = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.qcPhaseFileStatus = 'CONFIRMED' OR tf.qcPhaseFileStatus = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file06Status = 'CONFIRMED' OR tf.file06Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file07Status = 'CONFIRMED' OR tf.file07Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file08Status = 'CONFIRMED' OR tf.file08Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file09Status = 'CONFIRMED' OR tf.file09Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file10Status = 'CONFIRMED' OR tf.file10Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file11Status = 'CONFIRMED' OR tf.file11Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file12Status = 'CONFIRMED' OR tf.file12Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file13Status = 'CONFIRMED' OR tf.file13Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file14Status = 'CONFIRMED' OR tf.file14Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file15Status = 'CONFIRMED' OR tf.file15Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file16Status = 'CONFIRMED' OR tf.file16Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file17Status = 'CONFIRMED' OR tf.file17Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file18Status = 'CONFIRMED' OR tf.file18Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file19Status = 'CONFIRMED' OR tf.file19Status = 'SKIP') THEN 1 ELSE 0 END) +"
                        + " (CASE WHEN (tf.file20Status = 'CONFIRMED' OR tf.file20Status = 'SKIP') THEN 1 ELSE 0 END) "
                    + " ) fileConfirmedCount "
                    + " FROM TblComponentInspectionReferenceFile tf ) ref ");

        }
        queryBuilder.append(this.makeWhereClauseByQueryCondition(searchCondition, isCsv));
        queryBuilder.append(" AND t.classLang.mstComponentInspectLangPK.langId = :langId");
        queryBuilder.append(" AND t.mstTable.mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId");
        if (isCsv) {
            queryBuilder.append(" ORDER BY t.outgoingInspectionDate ASC,t.incomingInspectionDate ASC");
        } else if (StringUtils.isNotBlank(searchCondition.getSidx()) && orderKey.containsKey(searchCondition.getSidx())) {
            queryBuilder.append(orderKey.get(searchCondition.getSidx()).replace("%sord%", searchCondition.getSord())).append(" ").append(searchCondition.getSord());
        } else {
            queryBuilder.append(" ORDER BY t.createDate DESC");
        }

        TypedQuery<TblComponentInspectionResult> query = this.entityManager.createQuery(
                queryBuilder.toString(), TblComponentInspectionResult.class);

        this.setValuesToSearchQuery(query, searchCondition, isCsv, langId);

        boolean isMergedSort = Arrays.asList("poNumber", "productionLotNum", "fileInputStatusDisplay", "fileConfirmStatusDisplay", "componentInspectTypeName").contains(searchCondition.getSidx());
        if (!isMergedSort) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(searchCondition.getPageNumber(), searchCondition.getPageSize()));
            query.setMaxResults(searchCondition.getPageSize());
        }
        List<TblComponentInspectionResult> resultList = query.getResultList();

        List<ComponentInspectionResultInfo> infoList = new ArrayList<>();
        resultList.stream().forEach(result -> {
            result.setProductionLotNum("NO_PRODUCTION_LOT_NUM".equals(result.getProductionLotNum()) ? "" : result.getProductionLotNum());
            infoList.add(this.convertToInspectionResultInfo(result, langId, false));
        });

        if (isMergedSort) {
            int isAsc = "asc".equals(searchCondition.getSord()) ? 1 : -1;
            if ("poNumber".equals(searchCondition.getSidx())) {
                infoList.sort((r1, r2) -> r1.getPoNumber().compareTo(r2.getPoNumber()) * isAsc);
            } else if ("productionLotNum".equals(searchCondition.getSidx())) {
                infoList.sort((r1, r2) -> r1.getProductionLotNum().compareTo(r2.getProductionLotNum()) * isAsc);
            } else if ("fileInputStatusDisplay".equals(searchCondition.getSidx())) {
                infoList.sort((r1, r2) -> r1.getFileInputStatusDisplay().compareTo(r2.getFileInputStatusDisplay()) * isAsc);
            } else if ("fileConfirmStatusDisplay".equals(searchCondition.getSidx())) {
                Collections.sort(infoList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        ComponentInspectionResultInfo vo1 = (ComponentInspectionResultInfo) o1;
                        ComponentInspectionResultInfo vo2 = (ComponentInspectionResultInfo) o2;
                        //帳票ステータス
                        String fileConfirmStatus1 = vo1.getFileConfirmStatus();
                        String fileConfirmStatus2 = vo2.getFileConfirmStatus();

                        // 帳票進捗ステータス
                        String fileConfirmStatusDisplay1 = vo1.getFileConfirmStatusDisplay();
                        String fileConfirmStatusDisplay2 = vo2.getFileConfirmStatusDisplay();
                        
                        if (fileConfirmStatus1.equals(fileConfirmStatus2)) {
                            return fileConfirmStatusDisplay1.compareTo(fileConfirmStatusDisplay2) * isAsc;
                        } else {
                            return fileConfirmStatus1.compareTo(fileConfirmStatus2) * isAsc;
                        }
                    }
                });
            }
            Pager pager = new Pager();
            int startIdx = pager.getStartRow(searchCondition.getPageNumber(), searchCondition.getPageSize());
            return infoList.subList(startIdx, Math.min(startIdx + searchCondition.getPageSize(), infoList.size()));
        } else {
            return infoList;
        }
    }

    /**
     * Get component inspection result info by id and inspectionType
     *
     * @param inspectionResultId
     * @param inspectionType
     * @param langId
     * @return
     */
    public ComponentInspectionResultInfo getInspectionItemDetails(String inspectionResultId, Integer inspectionType, String langId) {
        // select TblComponentInspectionResult
        TblComponentInspectionResult tblResult = this.getTblInspectionItemResult(inspectionResultId);
        if (tblResult == null) {
            return null;
        }
        ComponentInspectionResultInfo inspectionResultInfo = this.convertToInspectionResultInfo(tblResult, langId, true);
        inspectionResultInfo.setOutgoingInspectionDate(FileUtil.dateFormat(tblResult.getOutgoingInspectionDate()));
        inspectionResultInfo.setOutgoingConfirmDate(FileUtil.dateFormat(tblResult.getOutgoingConfirmDate()));
        inspectionResultInfo.setOutgoingInspectionApproveDate(FileUtil.dateFormat(tblResult.getOutgoingInspectionApproveDate()));
        inspectionResultInfo.setIncomingInspectionDate(FileUtil.dateFormat(tblResult.getIncomingInspectionDate()));
        inspectionResultInfo.setIncomingConfirmDate(FileUtil.dateFormat(tblResult.getIncomingConfirmDate()));
        inspectionResultInfo.setIncomingInspectionApproveDate(FileUtil.dateFormat(tblResult.getIncomingInspectionApproveDate()));

        List<Integer> inspectionTypeList = new ArrayList<>();
        if (inspectionType == null) {
            inspectionTypeList.add(CommonConstants.INSPECTION_TYPE_OUTGOING);
            inspectionTypeList.add(CommonConstants.INSPECTION_TYPE_INCOMING);
        } else {
            inspectionTypeList.add(inspectionType);
        }

        // select MstComponentInspectionItemsTableDetail
        Map<String, MstComponentInspectionItemsTableDetail> inspectionItemsTableDetailMap
                = this.mstComponentInspectionItemsTableService.getMstCompInspItemsTableDetailsMap(inspectionResultInfo.getComponentInspectionItemsTableId());
        if (inspectionItemsTableDetailMap.isEmpty()) {
            return null;
        }

        // select TblComponentInspectionResultDetail
        List<TblComponentInspectionResultDetail> tblResultDetails = this.entityManager
                .createNamedQuery("TblComponentInspectionResultDetail.findByComponentInspectionResultIdAndInspectionTypeList",
                        TblComponentInspectionResultDetail.class)
                .setParameter("componentInspectionResultId", inspectionResultId)
                .setParameter("inspectionType", inspectionTypeList).getResultList();

        Map<String, List<TblComponentInspectionResultDetail>> tblResultDetailMa = this.convertToTblResultMap(tblResultDetails, inspectionItemsTableDetailMap);
        this.setInspectionResultDetailList(inspectionResultInfo, tblResultDetailMa, inspectionItemsTableDetailMap);

        //Apeng 20180202 add start
        List<TblComponentInspectionSampleName> tblComponentInspectionSampleNames
                = entityManager.createQuery("SELECT t FROM TblComponentInspectionSampleName t"
                        + " WHERE t.componentInspectionResultId = :componentInspectionResultId AND t.inspectionType IN :inspectionType",
                TblComponentInspectionSampleName.class)
                .setParameter("componentInspectionResultId", inspectionResultId)
                .setParameter("inspectionType", inspectionTypeList)
                .getResultList();
        List<TblComponentInspectionSampleName> tblComponentInspectionSampleNameMeasures = new ArrayList();
        List<TblComponentInspectionSampleName> tblComponentInspectionSampleNameVisuals = new ArrayList();
        if (tblComponentInspectionSampleNames != null && tblComponentInspectionSampleNames.size() > 0) {
            for (TblComponentInspectionSampleName tblComponentInspectionSampleName : tblComponentInspectionSampleNames) {
                if (CommonConstants.MEASUREMENT_TYPE_MEASURE == tblComponentInspectionSampleName.getMeasurementType()) {
                    tblComponentInspectionSampleNameMeasures.add(tblComponentInspectionSampleName);
                } else if (CommonConstants.MEASUREMENT_TYPE_VISUAL == tblComponentInspectionSampleName.getMeasurementType()) {
                    tblComponentInspectionSampleNameVisuals.add(tblComponentInspectionSampleName);
                }
            }
        }
        inspectionResultInfo.setTblComponentInspectionSampleNameMeasures(tblComponentInspectionSampleNameMeasures);
        inspectionResultInfo.setTblComponentInspectionSampleNameVisuals(tblComponentInspectionSampleNameVisuals);
        //Apeng 20180202 add end

        return inspectionResultInfo;
    }

    /**
     * Get mst component inspection items detail info by id
     *
     * @param inspectionResultId
     * @return
     */
    public Map<String, MstComponentInspectionItemsTableDetail> getMstInspectionItemDetailMap(String inspectionResultId) {

        // select TblComponentInspectionResult
        TblComponentInspectionResult tblResult = this.getTblInspectionItemResult(inspectionResultId);
        if (tblResult == null) {
            return new HashMap<>();
        }
        // select MstComponentInspectionItemsTableDetail
        Map<String, MstComponentInspectionItemsTableDetail> mstDetailMap
                = this.mstComponentInspectionItemsTableService.getMstCompInspItemsTableDetailsMap(tblResult.getComponentInspectionItemsTableId());
        return mstDetailMap;
    }

    /**
     * Create outgoing inspection form
     *
     * @param inputInfo
     * @param loginUser
     * @return
     */
    @Transactional
    public String createOutgoingInspectionForm(ComponentInspectionFormCreateInput inputInfo, LoginUser loginUser) {
        Date sysDate = new Date();
        MstCompany selfCompany = this.mstCompanyService.getSelfCompany();
        // create TblComponentInspectionResult
        TblComponentInspectionResult tblCompInspResult = new TblComponentInspectionResult();
        String componentInspectionResultId = IDGenerator.generate();
        tblCompInspResult.setId(componentInspectionResultId);
        tblCompInspResult.setComponentInspectionItemsTableId(inputInfo.getComponentInspectionItemsTableId());
        tblCompInspResult.setFirstFlag(inputInfo.getFirstFlag());
        getInspClass(inputInfo.getFirstFlag(), inputInfo.getIncomingCompanyId()).ifPresent(inspClass -> {
            tblCompInspResult.setInspClassDictKey(inspClass.getDictKey());
            tblCompInspResult.setInspectPtn(inspClass.getMassFlg());
        });
        tblCompInspResult.setCavityCnt(inputInfo.getCavityCnt());
        tblCompInspResult.setCavityStartNum(inputInfo.getCavityStartNum());
        tblCompInspResult.setCavityPrefix(inputInfo.getCavityPrefix());
        tblCompInspResult.setPoNumber(CommonConstants.PO_NUMBER);
        tblCompInspResult.setProductionLotNum(inputInfo.getProductionLotNum());
        tblCompInspResult.setComponentId(inputInfo.getComponentId());
        tblCompInspResult.setOutgoingCompanyId(selfCompany.getId());
        tblCompInspResult.setIncomingCompanyId(inputInfo.getIncomingCompanyId());
        tblCompInspResult.setQuantity(inputInfo.getQuantity());
        tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_O_INSPECTING);
        tblCompInspResult.setOutgoingMeasSamplingQuantity(inputInfo.getOutgoingMeasSamplingQuantity());
        tblCompInspResult.setOutgoingVisualSamplingQuantity(inputInfo.getOutgoingVisualSamplingQuantity());
        tblCompInspResult.setFileConfirmStatus(TblComponentInspectionResult.FileConfirmStatus.DEFAULT);
        tblCompInspResult.setOutgoingInspectionResult(0);
        tblCompInspResult.setIncomingInspectionResult(0);

        tblCompInspResult.setCreateDate(sysDate);
        tblCompInspResult.setCreateUserUuid(loginUser.getUserUuid());
        tblCompInspResult.setUpdateDate(sysDate);
        tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());

        tblCompInspResult.setMaterial01(inputInfo.getMaterial01());
        tblCompInspResult.setMaterial02(inputInfo.getMaterial02());
        tblCompInspResult.setMaterial03(inputInfo.getMaterial03());
        
        tblCompInspResult.setCavityCnt(inputInfo.getCavityCnt());
        tblCompInspResult.setCavityStartNum(inputInfo.getCavityStartNum());
        tblCompInspResult.setCavityPrefix(inputInfo.getCavityPrefix());

        //Apeng 20180201 add start
        if(Integer.toString(CommonConstants.INSPECTION_PROCESS_MASS_FLAG).charAt(0) == tblCompInspResult.getInspectPtn() || 
            Integer.toString(CommonConstants.INSPECTION_FIRST_MASS_PRODUCTION_FLAG).charAt(0) == tblCompInspResult.getInspectPtn()) {
            String cnfVal = cnfSystemService.findByKey("system", "relation_flg").getConfigValue();
            tblCompInspResult.setDataRelationTgt(cnfVal.length() > 0 ? cnfVal.charAt(0) : '0');
        } else {
            tblCompInspResult.setDataRelationTgt('1');
        }
        //Apeng 20180201 add end

        this.entityManager.persist(tblCompInspResult);

        this.createTblComponentInspectionResultDetail(componentInspectionResultId, inputInfo, loginUser.getUserUuid());

        //create file form M3002
        this.createTblComponentInspectionResultFile(componentInspectionResultId, inputInfo, loginUser.getUserUuid());
        MstComponentInspectionItemsTable itemTbl = mstComponentInspectionItemsTableService.findById(tblCompInspResult.getComponentInspectionItemsTableId());
        searchInspFileByPK(itemTbl.getInspectTypeId(), tblCompInspResult.getFirstFlag(), tblCompInspResult.getIncomingCompanyId()).ifPresent(inspFile -> {
            tblComponentInspectionReferenceFileService.copyFile(inputInfo.getComponentId(), componentInspectionResultId, inspFile, loginUser.getUserUuid(), inputInfo.getIncomingCompanyId());
        });

        return componentInspectionResultId;
    }
    
    private Optional<MstComponentInspectFile> searchInspFileByPK(String typeid, String classid, String incomingCompanyId) {
        Optional<MstComponentInspectFile> ret = mstComponentInspectFileService.findFileByPK(typeid, classid, incomingCompanyId);
        if(ret.isPresent()) {
            return ret;
        }
        return mstComponentInspectFileService.findFileByPK(typeid, classid, "SELF");
    }

    /**
     * 部品検査別参照ファイル更新
     *
     * @param inputFile
     * @param loginUser
     * @return
     */
    @Transactional
    public ComponentInspectionResultFileResponse updateComponentInspectionReferenceFile(ComponentInspectionReferenceFile inputFile, LoginUser loginUser) {
        ComponentInspectionResultFileResponse response = new ComponentInspectionResultFileResponse();
        tblComponentInspectionReferenceFileService.updateFile(inputFile, loginUser.getUserUuid());
        tblComponentInspectionReferenceFileService.copyFileNewest(inputFile.getComponentId(), inputFile.getComponentInspectionResultId(), loginUser.getUserUuid());

        return response;
    }

    /**
     * Update result file
     *
     * @param inputFile
     * @param loginUser
     * @return
     */
    @Transactional
    public ComponentInspectionResultFileResponse updateComponentInspectionResultFile(ComponentInspectionReferenceFile inputFile, LoginUser loginUser) {
        ComponentInspectionResultFileResponse componentInspectionResultFileResponse = new ComponentInspectionResultFileResponse();

        if (inputFile.getActionFileType().equals(CommonConstants.ITEMS_FILE_FLG_UPLOAD)) {
            TblUploadFile fileUpload = this.entityManager
                    .createNamedQuery("TblUploadFile.findByFileUuid", TblUploadFile.class)
                    .setParameter("fileUuid", inputFile.getInspecFileUuid())
                    .getResultList().stream().findFirst().orElse(null);

            //remove file old
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT t FROM TblUploadFile t");
            queryBuilder.append(" WHERE t.uploadFileName = :uploadFileName ");
            List<TblUploadFile> fileUploadList = this.entityManager
                    .createQuery(queryBuilder.toString(), TblUploadFile.class)
                    .setParameter("uploadFileName", fileUpload.getUploadFileName())
                    .getResultList();
            if (!fileUploadList.isEmpty()) {
                fileUploadList.stream().forEach(fileUploadId -> {
                    this.entityManager
                            .createNamedQuery("TblComponentInspectionResultFile.deleteByCondition", TblComponentInspectionResultFile.class)
                            .setParameter("componentInspectionResultId", inputFile.getComponentInspectionResultId())
                            .setParameter("fileUuid", fileUploadId.getFileUuid())
                            .executeUpdate();
                });
            }

            TblComponentInspectionResultFile tblComponentInspectionResultFile = new TblComponentInspectionResultFile();
            tblComponentInspectionResultFile.setId(IDGenerator.generate());
            tblComponentInspectionResultFile.setComponentInspectionResultId(inputFile.getComponentInspectionResultId());
            tblComponentInspectionResultFile.setFileUuid(inputFile.getInspecFileUuid());
            entityManager.persist(tblComponentInspectionResultFile);

            componentInspectionResultFileResponse.setComponentInspectionResultId(inputFile.getComponentInspectionResultId());
            componentInspectionResultFileResponse.setId(tblComponentInspectionResultFile.getId());
            componentInspectionResultFileResponse.setFileUuid(inputFile.getInspecFileUuid());
            componentInspectionResultFileResponse.setUploadDate(fileUpload.getUploadDate());
            componentInspectionResultFileResponse.setFileName(fileUpload.getUploadFileName());

        } else if (inputFile.getActionFileType().equals(CommonConstants.ITEMS_FILE_FLG_REMOVE)) {
            this.entityManager
                    .createNamedQuery("TblComponentInspectionResultFile.deleteByCondition", TblComponentInspectionResultFile.class)
                    .setParameter("componentInspectionResultId", inputFile.getComponentInspectionResultId())
                    .setParameter("fileUuid", inputFile.getInspecFileUuid())
                    .executeUpdate();
        }

        return componentInspectionResultFileResponse;
    }

    /**
     * Create incoming inspection form
     *
     * @param inputInfo
     * @param loginUser
     */
    @Transactional
    public void createIncomingInspectionForm(ComponentInspectionFormCreateInput inputInfo, LoginUser loginUser) {
        TblComponentInspectionResult tblCompInspResult
                = this.getTblInspectionItemResult(inputInfo.getComponentInspectionResultId());

        tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_I_INSPECTING);
        tblCompInspResult.setIncomingMeasSamplingQuantity(inputInfo.getIncomingMeasSamplingQuantity());
        tblCompInspResult.setIncomingVisualSamplingQuantity(inputInfo.getIncomingVisualSamplingQuantity());

        tblCompInspResult.setUpdateDate(new Date());
        tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());
        this.entityManager.merge(tblCompInspResult);

        searchInspFileByPK(tblCompInspResult.getMstTable().getInspectTypeId(), tblCompInspResult.getFirstFlag(), tblCompInspResult.getIncomingCompanyId()).ifPresent(inspFile -> {
            tblComponentInspectionReferenceFileService.copyFile(inputInfo.getComponentId(), inputInfo.getComponentInspectionResultId(), inspFile, loginUser.getUserUuid(), inputInfo.getIncomingCompanyId());
        });
        
        this.createTblComponentInspectionResultDetail(inputInfo.getComponentInspectionResultId(), inputInfo, loginUser.getUserUuid());
    }

    /**
     * confirm
     *
     * @param inputInfo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse confirmInspectionResult(ComponentInspectionActionInput inputInfo, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        if (StringUtils.isNotEmpty(inputInfo.getInspectionResultIds())) {
            Date tzNow = getTZNow(loginUser.getJavaZoneId());
            for (String componentInspectionResultId : inputInfo.getInspectionResultIds().split(",")) {
                TblComponentInspectionResult tblCompInspResult = this.getTblInspectionItemResult(componentInspectionResultId);
                if (tblCompInspResult == null) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_inspection_data_not_exist"));
                    return response;
                }

                Date sysDate = new Date();
                MstUser mstUser = this.mstUserService.getMstUser(loginUser.getUserid());

                if (inputInfo.getFileConfirmStatus() != null) {
                    tblCompInspResult.setFileConfirmStatus(inputInfo.getFileConfirmStatus());
                }

//                String productionLotNum;
//                if (StringUtils.isNotEmpty(inputInfo.getBatchFlag()) && "1".equals(inputInfo.getBatchFlag())) {
//                    productionLotNum = tblCompInspResult.getProductionLotNum() == null ? CommonConstants.PRODUCTION_LOT_NUMBER : tblCompInspResult.getProductionLotNum();
//                } else {
//                    productionLotNum = inputInfo.getProductionLotNum() == null ? CommonConstants.PRODUCTION_LOT_NUMBER : inputInfo.getProductionLotNum();
//                }

                if (this.isOutgoing(inputInfo.getInspectionType())) {
                    if (inputInfo.getAction()) {
                        tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_O_UNAPPROVED);
                        tblCompInspResult.setOutgoingConfirmResult(ConfirmResult.CONFIRMED);
//                        tblCompInspResult.setProductionLotNum(productionLotNum);
                    } else {
                        tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_O_REJECTED);
                        tblCompInspResult.setOutgoingConfirmResult(ConfirmResult.DENIED);
                    }
                    tblCompInspResult.setOutgoingConfirmDate(tzNow);
                    tblCompInspResult.setOutgoingConfirmerUuid(loginUser.getUserUuid());
                    tblCompInspResult.setOutgoingConfirmerName(mstUser.getUserName());
                    
                } else {

                    if (inputInfo.getAction()) {
                        tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_I_UNAPPROVED);
                        tblCompInspResult.setIncomingConfirmResult(ConfirmResult.CONFIRMED);
                    } else {
                        tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_I_AGAIN_REJECTED);
                        tblCompInspResult.setIncomingConfirmResult(ConfirmResult.DENIED);
                        //入荷確認者否認更新batch status
                        if (getInspectionResultDetailByOutgoingExist(componentInspectionResultId)
                                && !this.isInternalOutgoing(tblCompInspResult.getOutgoingCompanyId(), tblCompInspResult.getIncomingCompanyId())) {
                            tblCompInspResult.setInspBatchUpdateStatus(CommonConstants.INSP_BATCH_UPDATE_STATUS_I_RESULT_NOT_PUSH);
                        }
                    }
                    tblCompInspResult.setIncomingConfirmDate(tzNow);
                    tblCompInspResult.setIncomingConfirmerUuid(loginUser.getUserUuid());
                    tblCompInspResult.setIncomingConfirmerName(mstUser.getUserName());
                }

                tblCompInspResult.setUpdateDate(sysDate);
                tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());
                this.entityManager.merge(tblCompInspResult);

                TblComponentInspectionSendMail sendMail = new TblComponentInspectionSendMail();
                MstComponent mstComponent = this.entityManager
                        .createNamedQuery("MstComponent.findById", MstComponent.class)
                        .setParameter("id", tblCompInspResult.getComponentId())
                        .getResultList().stream().findFirst().orElse(null);

                if (mstComponent != null) {
                    sendMail.setComponentCode(mstComponent.getComponentCode());
                    sendMail.setComponentName(mstComponent.getComponentName());
                }
                sendMail.setQuantity(tblCompInspResult.getQuantity());
                sendMail.setBaseUrl(kartePropertyService.getBaseUrl());
                sendMail.setResultId(componentInspectionResultId);
                sendMail.setAct(CommonConstants.ACT_DETAIL);
                
                // KM-10000 検査系メール通知の所属指定を修正 20181210 start
                // メール送信の所属を取得する
                List<Integer> departmentList = new ArrayList<>();
                if (this.isOutgoing(inputInfo.getInspectionType())) {// 出荷の場合
                    MstUser user = mstUserService.getMstUserByUuid(tblCompInspResult.getOutgoingInspectionPersonUuid());
                    if (null != user) {
                        if (StringUtils.isNotEmpty(user.getDepartment())) {
                            departmentList.add(Integer.valueOf(user.getDepartment()));
                        }
                    }
                    sendMail.setUserDepartmentList(departmentList);
                } else {// 入荷の場合
                    MstUser user = mstUserService.getMstUserByUuid(tblCompInspResult.getIncomingInspectionPersonUuid());
                    if (null != user) {
                        if (StringUtils.isNotEmpty(user.getDepartment())) {
                            departmentList.add(Integer.valueOf(user.getDepartment()));
                        }
                    }
                    sendMail.setUserDepartmentList(departmentList);
                }

                if (this.isOutgoing(inputInfo.getInspectionType())) {
                    if (!inputInfo.getAction()) {
                        sendMail.setFunctionType(CommonConstants.FUNCTION_TYPE_OUTGOING);
                        sendMail.setDenialCommentaries(inputInfo.getComment());
                        if (sendMail.getUserDepartmentList().size() > 0) {
                            tblComponentInsectionSendMailService.sendNotice(CommonConstants.CHECK_CONFIRM_DENIAL, sendMail);//出荷確認者否認
                        }
                    } else if (tblCompInspResult.getOutgoingInspectionResult() == CommonConstants.INSPECTION_RESULT_NG) {
                        sendMail.setFunctionType(CommonConstants.FUNCTION_TYPE_OUTGOING);
                        if (sendMail.getUserDepartmentList().size() > 0) {
                            tblComponentInsectionSendMailService.sendNotice(CommonConstants.CHECK_NG_CONFIRM, sendMail);//出荷NG確認
                        }
                    }
                } else if (!inputInfo.getAction()) {
                    sendMail.setFunctionType(CommonConstants.FUNCTION_TYPE_INCOMING);
                    sendMail.setDenialCommentaries(inputInfo.getComment());
                    if (sendMail.getUserDepartmentList().size() > 0) {
                        tblComponentInsectionSendMailService.sendNotice(CommonConstants.CHECK_CONFIRM_DENIAL, sendMail);//入荷確認者否認
                    }
                } else if (tblCompInspResult.getIncomingInspectionResult() == CommonConstants.INSPECTION_RESULT_NG) {
                    sendMail.setFunctionType(CommonConstants.FUNCTION_TYPE_INCOMING);
                    if (sendMail.getUserDepartmentList().size() > 0) {
                        tblComponentInsectionSendMailService.sendNotice(CommonConstants.CHECK_NG_CONFIRM, sendMail);//入荷NG確認
                    }
                }
                // KM-10000 検査系メール通知の所属指定を修正 20181210 end
            }
        }
        return response;
    }

    /**
     * Update TblComponentInspectionResult and
     * TblComponentInspectionResultDetail
     *
     * @param inputInfo
     * @param saveType
     * @param loginUser
     * @return
     */
    @Transactional
    public boolean updateInspectionResult(ComponentInspectionResultSaveInput inputInfo, String saveType, LoginUser loginUser) {
        CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
        String componentInspectionResultId = inputInfo.getComponentInspectionResultId();
        TblComponentInspectionResult tblCompInspResult = this.getTblInspectionItemResult(componentInspectionResultId);
        if (tblCompInspResult == null) {
            return false;
        }

        Date sysDate = new Date();
        if (!inputInfo.isTemporarySave()) {
            MstUser mstUser = this.mstUserService.getMstUser(loginUser.getUserid());
            Date now = getTZNow(loginUser.getJavaZoneId());
            if (this.isOutgoing(inputInfo.getInspectionType())) {
                tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_O_CONFIRM);// 出荷検査確認待ち
                if (inputInfo.getOutgoingInspResult().equals(0)) {
                    tblCompInspResult.setOutgoingInspectionResult(inputInfo.getOutgoingInspResult());
                }
                tblCompInspResult.setOutgoingInspectionComment(inputInfo.getComment());
                tblCompInspResult.setOutgoingPrivateComment(inputInfo.getPrivateComment());
                if(tblCompInspResult.getOutgoingInspectionDate() == null) {
                    tblCompInspResult.setOutgoingInspectionDate(now);
                }
                tblCompInspResult.setOutgoingInspectionPersonUuid(loginUser.getUserUuid());
                tblCompInspResult.setOutgoingInspectionPersonName(mstUser.getUserName());
                tblCompInspResult.setOutgoingConfirmResult(ConfirmResult.UNTREATED);
                tblCompInspResult.setOutgoingConfirmDate(null);
                tblCompInspResult.setOutgoingConfirmerUuid(null);
                tblCompInspResult.setOutgoingConfirmerName(null);
                tblCompInspResult.setOutgoingApproveResult(ApproveResult.UNTREATED);
                tblCompInspResult.setOutgoingInspectionApproveDate(null);
                tblCompInspResult.setOutgoingInspectionApprovePersonUuid(null);
                tblCompInspResult.setOutgoingInspectionApprovePersonName(null);
                tblCompInspResult.setMaterial01(inputInfo.getMaterial01());
                tblCompInspResult.setMaterial02(inputInfo.getMaterial02());
                tblCompInspResult.setMaterial03(inputInfo.getMaterial03());

                if (StringUtils.isNotEmpty(inputInfo.getProductionLotNum())) {
                    tblCompInspResult.setProductionLotNum(inputInfo.getProductionLotNum());
                } else {
                    tblCompInspResult.setProductionLotNum(CommonConstants.PRODUCTION_LOT_NUMBER);
                }

            } else {
                tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_I_CONFIRM);// 入荷検査確認待ち
                if (inputInfo.getIncomingInspResult().equals(0)) {
                    tblCompInspResult.setIncomingInspectionResult(inputInfo.getIncomingInspResult());
                }
                tblCompInspResult.setIncomingInspectionComment(inputInfo.getComment());
                tblCompInspResult.setIncomingPrivateComment(inputInfo.getPrivateComment());
                if(tblCompInspResult.getIncomingInspectionDate() == null) {
                    tblCompInspResult.setIncomingInspectionDate(now);
                    setWeekAndMonth(tblCompInspResult, cnf);
                }
                tblCompInspResult.setIncomingInspectionPersonUuid(loginUser.getUserUuid());
                tblCompInspResult.setIncomingInspectionPersonName(mstUser.getUserName());
                tblCompInspResult.setIncomingConfirmResult(ConfirmResult.UNTREATED);
                tblCompInspResult.setIncomingConfirmDate(null);
                tblCompInspResult.setIncomingConfirmerUuid(null);
                tblCompInspResult.setIncomingConfirmerName(null);
                tblCompInspResult.setIncomingApproveResult(ApproveResult.UNTREATED);
                tblCompInspResult.setIncomingInspectionApproveDate(null);
                tblCompInspResult.setIncomingInspectionApprovePersonUuid(null);
                tblCompInspResult.setIncomingInspectionApprovePersonName(null);
            }

            //31528 save exemption
            if (inputInfo.getIsExempt() == 1) {
                tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_I_EXEMPTED);
                tblCompInspResult.setExemptionApproveDate(sysDate);
                tblCompInspResult.setExemptionApprovePersonUuid(loginUser.getUserUuid());
                tblCompInspResult.setExemptionApprovePersonName(mstUser.getUserName());
            }

            tblCompInspResult.setUpdateDate(sysDate);
            tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());
        } else {
            if (this.isOutgoing(inputInfo.getInspectionType())) {
                tblCompInspResult.setOutgoingInspectionComment(inputInfo.getComment());
                tblCompInspResult.setOutgoingPrivateComment(inputInfo.getPrivateComment());
                tblCompInspResult.setOutgoingInspectionResult(inputInfo.getOutgoingInspResult());
                tblCompInspResult.setQuantity(inputInfo.getQuantity());
            } else {
                tblCompInspResult.setIncomingInspectionComment(inputInfo.getComment());
                tblCompInspResult.setIncomingPrivateComment(inputInfo.getPrivateComment());
                tblCompInspResult.setIncomingInspectionResult(inputInfo.getIncomingInspResult());
            }
            tblCompInspResult.setUpdateDate(sysDate);
            tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());
        }
        //Apeng 20180201 add start
        if (StringUtils.isNotEmpty(inputInfo.getDataRelationTgt())) {
            tblCompInspResult.setDataRelationTgt(inputInfo.getDataRelationTgt().charAt(0));
        } else {
            tblCompInspResult.setDataRelationTgt(Character.MIN_VALUE);
        }
        //Apeng 20180201 add end

        if (inputInfo.getFileConfirmStatus() != null) {
            tblCompInspResult.setFileConfirmStatus(inputInfo.getFileConfirmStatus());
            // km-976 帳票確認者の検索を追加 20181122 start
            if (!TblComponentInspectionResult.FileConfirmStatus.DEFAULT.equals(inputInfo.getFileConfirmStatus())) {
                tblCompInspResult.setFileApproverId(loginUser.getUserUuid());
                tblCompInspResult.setFileApproverName(getUserName(loginUser.getUserUuid()));
            }
            // km-976 帳票確認者の検索を追加 20181122 end
        }

        this.entityManager.merge(tblCompInspResult);

        this.updateInspectionResultDetail(inputInfo, loginUser.getUserUuid());

        if (!inputInfo.isTemporarySave()) {
            TblComponentInspectionSendMail sendMail = new TblComponentInspectionSendMail();
            MstComponent mstComponent = this.entityManager
                    .createNamedQuery("MstComponent.findById", MstComponent.class)
                    .setParameter("id", tblCompInspResult.getComponentId())
                    .getResultList().stream().findFirst().orElse(null);

            if (mstComponent != null) {
                sendMail.setComponentCode(mstComponent.getComponentCode());
                sendMail.setComponentName(mstComponent.getComponentName());
            }
            sendMail.setQuantity(tblCompInspResult.getQuantity());
            sendMail.setBaseUrl(kartePropertyService.getBaseUrl());
            sendMail.setResultId(inputInfo.getComponentInspectionResultId());
            sendMail.setAct(CommonConstants.ACT_DETAIL);

            List<Integer> departmentList = new ArrayList<>();
            if (this.isOutgoing(inputInfo.getInspectionType())) {
                if (tblCompInspResult.getOutgoingInspectionResult() == CommonConstants.INSPECTION_RESULT_NG) {
                    // KM-10000 検査系メール通知の所属指定を修正 20181210 start
                    MstUser user = mstUserService.getMstUserByUuid(tblCompInspResult.getOutgoingInspectionPersonUuid());
                    if (null != user) {
                        if (StringUtils.isNotEmpty(user.getDepartment())) {
                            departmentList.add(Integer.valueOf(user.getDepartment()));
                        }
                    }
                    sendMail.setUserDepartmentList(departmentList);
                    sendMail.setFunctionType(CommonConstants.FUNCTION_TYPE_OUTGOING);
                    if (sendMail.getUserDepartmentList().size() > 0) {
                        tblComponentInsectionSendMailService.sendNotice(CommonConstants.CHECK_NG_END, sendMail);//出荷NG完了
                    }
                    // KM-10000 検査系メール通知の所属指定を修正 20181210 end
                }
            } else if (tblCompInspResult.getIncomingInspectionResult() == CommonConstants.INSPECTION_RESULT_NG) {
                // KM-10000 検査系メール通知の所属指定を修正 20181210 start
                MstUser user = mstUserService.getMstUserByUuid(tblCompInspResult.getIncomingInspectionPersonUuid());
                if (null != user) {
                    if (StringUtils.isNotEmpty(user.getDepartment())) {
                        departmentList.add(Integer.valueOf(user.getDepartment()));
                    }
                }
                sendMail.setUserDepartmentList(departmentList);
                sendMail.setFunctionType(CommonConstants.FUNCTION_TYPE_INCOMING);
                if (sendMail.getUserDepartmentList().size() > 0) {
                    tblComponentInsectionSendMailService.sendNotice(CommonConstants.CHECK_NG_END, sendMail);//入荷NG完了
                }
                // KM-10000 検査系メール通知の所属指定を修正 20181210 end
            }
        }
        
        // km-976 帳票確認者の検索を追加 20181122 start
        // 帳票ステータスは否認の場合、担当者へ送信する(非同期)
        if (StringUtils.isNotEmpty(inputInfo.getFileDeniedFlg()) && "1".equals(inputInfo.getFileDeniedFlg())) {
            TblComponentInspectionSendMail sendMail = new TblComponentInspectionSendMail();
            
            MstComponent mstComponent = this.entityManager
                    .createNamedQuery("MstComponent.findById", MstComponent.class)
                    .setParameter("id", tblCompInspResult.getComponentId())
                    .getResultList().stream().findFirst().orElse(null);
            
            if (mstComponent != null) {
                sendMail.setComponentCode(mstComponent.getComponentCode());
                sendMail.setComponentName(mstComponent.getComponentName());
            }
            sendMail.setInspectionCreateDate(FileUtil.dateFormat(tblCompInspResult.getCreateDate()));
            sendMail.setBaseUrl(kartePropertyService.getBaseUrl());
            sendMail.setResultId(componentInspectionResultId);
            sendMail.setAct(CommonConstants.ACT_CHECK);
            sendMail.setFunctionType(CommonConstants.FUNCTION_TYPE_INCOMING);
            if (null != tblCompInspResult.getMstCompanyIncoming()) {
                sendMail.setIncomingCompanyCode(tblCompInspResult.getMstCompanyIncoming().getCompanyCode());
            } else {
                sendMail.setIncomingCompanyCode("");
            }
            if (null != tblCompInspResult.getMstCompanyOutgoing()) {
                sendMail.setOutgoingCompanyName(tblCompInspResult.getMstCompanyOutgoing().getCompanyName());
            } else {
                sendMail.setOutgoingCompanyName("");
            }
            sendMail.setIncomingPrivateComment(tblCompInspResult.getIncomingPrivateComment());
            
            TblComponentInspectionReferenceFile tblReferenceFile = this.entityManager
                    .createNamedQuery("TblComponentInspectionReferenceFile.findByComponentInspectionResultId", TblComponentInspectionReferenceFile.class)
                    .setParameter("componentInspectionResultId", tblCompInspResult.getId())
                    .getResultList().stream().findFirst().orElse(null);
            
            if (null != tblReferenceFile) {

                List<String> userList = new ArrayList();

                if (tblReferenceFile.getDrawingFileFlg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getDrawingFileConfirmerId())) {
                    userList.add(tblReferenceFile.getDrawingFileConfirmerId());
                }
                if (tblReferenceFile.getProofFileFlg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getProofFileConfirmerId())) {
                    userList.add(tblReferenceFile.getProofFileConfirmerId());
                }
                if (tblReferenceFile.getRohsProofFileFlg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getRohsProofFileConfirmerId())) {
                    userList.add(tblReferenceFile.getRohsProofFileConfirmerId());
                }
                if (tblReferenceFile.getPackageSpecFileFlg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getPackageSpecFileConfirmerId())) {
                    userList.add(tblReferenceFile.getPackageSpecFileConfirmerId());
                }
                if (tblReferenceFile.getQcPhaseFileFlg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getQcPhaseFileConfirmerId())) {
                    userList.add(tblReferenceFile.getQcPhaseFileConfirmerId());
                }
                if (tblReferenceFile.getFile06Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile06ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile06ConfirmerId());
                }
                if (tblReferenceFile.getFile07Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile07ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile07ConfirmerId());
                }
                if (tblReferenceFile.getFile08Flg().equals('0') || tblReferenceFile.getFile08Flg().equals('1')) {
                }
                if (tblReferenceFile.getFile08Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile08ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile08ConfirmerId());
                }
                if (tblReferenceFile.getFile09Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile09ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile09ConfirmerId());
                }
                if (tblReferenceFile.getFile10Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile10ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile10ConfirmerId());
                }
                if (tblReferenceFile.getFile11Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile11ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile11ConfirmerId());
                }
                if (tblReferenceFile.getFile12Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile12ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile12ConfirmerId());
                }
                if (tblReferenceFile.getFile13Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile13ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile13ConfirmerId());
                }
                if (tblReferenceFile.getFile14Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile14ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile14ConfirmerId());
                }
                if (tblReferenceFile.getFile15Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile15ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile15ConfirmerId());
                }
                if (tblReferenceFile.getFile16Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile16ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile16ConfirmerId());
                }
                if (tblReferenceFile.getFile17Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile17ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile17ConfirmerId());
                }
                if (tblReferenceFile.getFile18Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile18ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile18ConfirmerId());
                }
                if (tblReferenceFile.getFile19Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile19ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile19ConfirmerId());
                }
                if (tblReferenceFile.getFile20Flg().equals('1') && StringUtils.isNotEmpty(tblReferenceFile.getFile20ConfirmerId())) {
                    userList.add(tblReferenceFile.getFile20ConfirmerId());
                }
                sendMail.setUserList(userList);
            }

            if (sendMail.getUserList().size() > 0) {

                ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

                cachedThreadPool.execute(new Runnable() {

                    @Override
                    public void run() {

                        //　帳票否認時に、メール送信処理
                        tblComponentInsectionSendMailService.sendNotice(CommonConstants.INCOMING_FILE_DENIED, sendMail);
                    }
                });
            }
            
        }
        // km-976 帳票確認者の検索を追加 20181122 end
        return true;
    }

    private Date getTZNow(String tz) {
        LocalDateTime localNow = LocalDateTime.now(TimeZone.getTimeZone(tz).toZoneId());
        return Date.from(localNow.toInstant(TimeZone.getDefault().toZoneId().getRules().getOffset(localNow)));
    }
    /**
     * Approve
     *
     * @param inputInfo
     * @param loginUser
     * @return
     */  //一括承认
    @Transactional
    public BasicResponse approve(ComponentInspectionActionInput inputInfo, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        if (StringUtils.isNotEmpty(inputInfo.getInspectionResultIds())) {
            Date tzNow = getTZNow(loginUser.getJavaZoneId());
            for (String componentInspectionResultId : inputInfo.getInspectionResultIds().split(",")) {
                TblComponentInspectionResult tblCompInspResult = this.getTblInspectionItemResult(componentInspectionResultId);
                if (tblCompInspResult == null) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_inspection_data_not_exist"));
                    return response;
                }

//                String productionLotNum = tblCompInspResult.getProductionLotNum();
//                if (StringUtils.isNotEmpty(inputInfo.getBatchFlag()) && "1".equals(inputInfo.getBatchFlag())) {
//                    productionLotNum = tblCompInspResult.getProductionLotNum() == null ? CommonConstants.PRODUCTION_LOT_NUMBER : tblCompInspResult.getProductionLotNum();
//                } else {
//                    productionLotNum = inputInfo.getProductionLotNum() == null ? CommonConstants.PRODUCTION_LOT_NUMBER : inputInfo.getProductionLotNum();
//                }

                Date sysDate = new Date();
                MstUser mstUser = this.mstUserService.getMstUser(loginUser.getUserid());

                if (inputInfo.getFileConfirmStatus() != null) {
                    tblCompInspResult.setFileConfirmStatus(inputInfo.getFileConfirmStatus());
                }
                if (this.isOutgoing(inputInfo.getInspectionType())) {
                    if (inputInfo.getAction()) {
                        tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_O_APPROVED);
                        tblCompInspResult.setOutgoingApproveResult(ApproveResult.APPROVED);
//                        if (CommonConstants.PRODUCTION_LOT_NUMBER.equals(productionLotNum)) {
//                            tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_O_MATCH);
//                        } else {
//                            tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_O_APPROVED);
//                        }
//                        tblCompInspResult.setProductionLotNum(productionLotNum);
                    } else {
                        tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_O_REJECTED);
                        tblCompInspResult.setOutgoingApproveResult(ApproveResult.DENIED);
                    }
                    // check whether the record is batch process target
                    if (!this.isInternalOutgoing(tblCompInspResult.getOutgoingCompanyId(), tblCompInspResult.getIncomingCompanyId()) &&
                            CommonConstants.ADDITIONAL_FLG_YES.equals(tblCompInspResult.getDataRelationTgt().toString())) {
                        tblCompInspResult.setInspBatchUpdateStatus(CommonConstants.INSP_BATCH_UPDATE_STATUS_O_RESULT_NOT_SEND);
                    }
                    tblCompInspResult.setOutgoingInspectionApproveDate(tzNow);
                    tblCompInspResult.setOutgoingInspectionApprovePersonUuid(loginUser.getUserUuid());
                    tblCompInspResult.setOutgoingInspectionApprovePersonName(mstUser.getUserName());

                } else {
                    if (inputInfo.getAction()) {
                        if (TblComponentInspectionResult.FileConfirmStatus.DEFAULT.equals(tblCompInspResult.getFileConfirmStatus()) && 
                                tblComponentInspectionReferenceFileService.isAnyFileReqired(tblCompInspResult.getId())) {
                            response.setError(true);
                            response.setErrorCode(ErrorMessages.E201_APPLICATION);
                            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_file_confirm_ng"));
                            break;
                        }
                        tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_I_APPROVED);
                        tblCompInspResult.setIncomingApproveResult(ApproveResult.APPROVED);
                    } else {
                        tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_I_AGAIN_REJECTED);
                        tblCompInspResult.setIncomingApproveResult(ApproveResult.DENIED);
                    }
                    // check whether the record is batch process target
                    if ((getInspectionResultDetailByOutgoingExist(componentInspectionResultId) || !getInspectionResultDetailEmptyExist(componentInspectionResultId)) &&
                            !this.isInternalOutgoing(tblCompInspResult.getOutgoingCompanyId(), tblCompInspResult.getIncomingCompanyId())) {
                        tblCompInspResult.setInspBatchUpdateStatus(CommonConstants.INSP_BATCH_UPDATE_STATUS_I_RESULT_NOT_PUSH);
                    }
                    tblCompInspResult.setIncomingInspectionApproveDate(tzNow);
                    tblCompInspResult.setIncomingInspectionApprovePersonUuid(loginUser.getUserUuid());
                    tblCompInspResult.setIncomingInspectionApprovePersonName(mstUser.getUserName());
                }

                tblCompInspResult.setUpdateDate(sysDate);
                tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());
                this.entityManager.merge(tblCompInspResult);

                TblComponentInspectionSendMail sendMail = new TblComponentInspectionSendMail();
                MstComponent mstComponent = this.entityManager
                        .createNamedQuery("MstComponent.findById", MstComponent.class)
                        .setParameter("id", tblCompInspResult.getComponentId())
                        .getResultList().stream().findFirst().orElse(null);

                if (mstComponent != null) {
                    sendMail.setComponentCode(mstComponent.getComponentCode());
                    sendMail.setComponentName(mstComponent.getComponentName());
                }
                sendMail.setQuantity(tblCompInspResult.getQuantity());
                sendMail.setBaseUrl(kartePropertyService.getBaseUrl());
                sendMail.setResultId(componentInspectionResultId);
                sendMail.setAct(CommonConstants.ACT_DETAIL);
                
                // KM-10000 検査系メール通知の所属指定を修正 20181210 start
                // メール送信の所属を取得する
                List<Integer> departmentList = new ArrayList<>();
                if (this.isOutgoing(inputInfo.getInspectionType())) {// 出荷の場合
                    MstUser user = mstUserService.getMstUserByUuid(tblCompInspResult.getOutgoingInspectionPersonUuid());
                    if (null != user) {
                        if (StringUtils.isNotEmpty(user.getDepartment())) {
                            departmentList.add(Integer.valueOf(user.getDepartment()));
                        }
                    }
                    sendMail.setUserDepartmentList(departmentList);
                } else {// 入荷の場合
                    MstUser user = mstUserService.getMstUserByUuid(tblCompInspResult.getIncomingInspectionPersonUuid());
                    if (null != user) {
                        if (StringUtils.isNotEmpty(user.getDepartment())) {
                            departmentList.add(Integer.valueOf(user.getDepartment()));
                        }
                    }
                    sendMail.setUserDepartmentList(departmentList);
                }

                if (this.isOutgoing(inputInfo.getInspectionType())) {
                    if (!inputInfo.getAction()) {
                        sendMail.setFunctionType(CommonConstants.FUNCTION_TYPE_OUTGOING);
                        sendMail.setDenialCommentaries(inputInfo.getComment());
                        if (sendMail.getUserDepartmentList().size() > 0) {
                            tblComponentInsectionSendMailService.sendNotice(CommonConstants.CHECK_APPROVE_DENIAL, sendMail);//出荷承認者否認
                        }
                    } else if (tblCompInspResult.getOutgoingInspectionResult() == CommonConstants.INSPECTION_RESULT_NG) {
                        sendMail.setFunctionType(CommonConstants.FUNCTION_TYPE_OUTGOING);
                        if (sendMail.getUserDepartmentList().size() > 0) {
                            tblComponentInsectionSendMailService.sendNotice(CommonConstants.CHECK_NG_APPROVE, sendMail);//出荷NG承認
                        }
                    }
                } else if (!inputInfo.getAction()) {
                    sendMail.setFunctionType(CommonConstants.FUNCTION_TYPE_INCOMING);
                    sendMail.setDenialCommentaries(inputInfo.getComment());
                    if (sendMail.getUserDepartmentList().size() > 0) {
                        tblComponentInsectionSendMailService.sendNotice(CommonConstants.CHECK_APPROVE_DENIAL, sendMail);//入荷承認者否認
                    }
                } else if (tblCompInspResult.getIncomingInspectionResult() == CommonConstants.INSPECTION_RESULT_NG) {
                    sendMail.setFunctionType(CommonConstants.FUNCTION_TYPE_INCOMING);
                    if (sendMail.getUserDepartmentList().size() > 0) {
                        tblComponentInsectionSendMailService.sendNotice(CommonConstants.CHECK_NG_APPROVE, sendMail);//入荷NG承認
                    }
                }
                // KM-10000 検査系メール通知の所属指定を修正 20181210 end
            }
        }
        return response;
    }

    /**
     * Accept
     *
     * @param componentInspectionResultId
     * @param accepty
     * @param comment
     * @param privateComment
     * @param loginUser
     * @return
     */
    @Transactional
    public boolean accept(String componentInspectionResultId, Boolean accepty, String comment, String privateComment, LoginUser loginUser) {
        TblComponentInspectionResult tblCompInspResult = this.getTblInspectionItemResult(componentInspectionResultId);
        if (tblCompInspResult == null) {
            return false;
        }

        Date sysDate = new Date();
        MstUser mstUser = this.mstUserService.getMstUser(loginUser.getUserid());
        if (accepty) {
            tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_I_ACCEPTED);
        } else {
            tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_I_REJECTED);
        }
        tblCompInspResult.setIncomingInspectionComment(comment);
        tblCompInspResult.setIncomingPrivateComment(privateComment);
        tblCompInspResult.setAcceptanceDate(sysDate);
        tblCompInspResult.setAcceptancePersonUuid(loginUser.getUserUuid());
        tblCompInspResult.setAcceptancePersonName(mstUser.getUserName());

        tblCompInspResult.setUpdateDate(sysDate);
        tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());
        this.entityManager.merge(tblCompInspResult);
        return true;
    }

    /**
     * Exempt
     *
     * @param componentInspectionResultId
     * @param comment
     * @param loginUser
     * @return
     */
    @Transactional
    public boolean exempt(String componentInspectionResultId, String comment, LoginUser loginUser) {
        TblComponentInspectionResult tblCompInspResult = this.getTblInspectionItemResult(componentInspectionResultId);
        if (tblCompInspResult == null) {
            return false;
        }

        Date sysDate = new Date();
        MstUser mstUser = this.mstUserService.getMstUser(loginUser.getUserid());
        tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_I_EXEMPTED);
        tblCompInspResult.setIncomingInspectionComment(comment);
        tblCompInspResult.setExemptionApproveDate(sysDate);
        tblCompInspResult.setExemptionApprovePersonUuid(loginUser.getUserUuid());
        tblCompInspResult.setExemptionApprovePersonName(mstUser.getUserName());

        // check whether the record is batch process target
        if (!this.isInternalOutgoing(tblCompInspResult.getOutgoingCompanyId(), tblCompInspResult.getIncomingCompanyId())) {
            tblCompInspResult.setInspBatchUpdateStatus(CommonConstants.INSP_BATCH_UPDATE_STATUS_I_RESULT_NOT_PUSH);
        }
        tblCompInspResult.setUpdateDate(sysDate);
        tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());
        this.entityManager.merge(tblCompInspResult);
        return true;
    }

    /**
     * Delete inspection form
     *
     * @param componentInspectionResultId
     * @return
     */
    @Transactional
    public boolean deleteInpsectionForm(String componentInspectionResultId) {
        TblComponentInspectionResult tblCompInspResult = this.getTblInspectionItemResult(componentInspectionResultId);
        if (tblCompInspResult == null) {
            return false;
        }
        this.entityManager.remove(tblCompInspResult);
        return true;
    }

    /**
     * Get TblComponentInspectionResult by id
     *
     * @param inspectionResultId
     * @return
     */
    public TblComponentInspectionResult getTblInspectionItemResult(String inspectionResultId) {
        // select TblComponentInspectionResult
        TblComponentInspectionResult tblResult = this.entityManager
                .createNamedQuery("TblComponentInspectionResult.findById", TblComponentInspectionResult.class)
                .setParameter("id", inspectionResultId)
                .getResultList().stream().findFirst().orElse(null);

        return tblResult;
    }

    /**
     * Get external outgoing inspection result
     *
     * @param incomingCompanyId
     * @return
     */
    public List<ComponentInspectionResultInfoForBatch> getExtOutgoingInspectionResult(String incomingCompanyId) {
        int postMaxSize = Integer.parseInt(cnfSystemService.findByKey("system", "outgoing_insp_post_size").getConfigValue());
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t FROM TblComponentInspectionResult t");
        queryBuilder.append(" JOIN FETCH t.mstComponent");
        queryBuilder.append(" WHERE t.outgoingCompanyId = :outgoingCompanyId");
        queryBuilder.append(" AND t.incomingCompanyId = :incomingCompanyId");
        queryBuilder.append(" AND t.inspectionStatus = :inspectionStatus");
        queryBuilder.append(" AND t.inspBatchUpdateStatus = :inspBatchUpdateStatus");
        queryBuilder.append(" AND t.dataRelationTgt = :dataRelationTgt");
        queryBuilder.append(" ORDER BY t.createDate ");

        List<TblComponentInspectionResult> inspResultList = this.entityManager.createQuery(
                queryBuilder.toString(), TblComponentInspectionResult.class)
                .setParameter("outgoingCompanyId", this.getSelfCompanyId())
                .setParameter("incomingCompanyId", incomingCompanyId)
                .setParameter("inspectionStatus", CommonConstants.INSPECTION_STATUS_O_APPROVED)
                .setParameter("inspBatchUpdateStatus", CommonConstants.INSP_BATCH_UPDATE_STATUS_O_RESULT_NOT_SEND)
                .setParameter("dataRelationTgt", CommonConstants.ADDITIONAL_FLG_YES.charAt(0))
                .setMaxResults(postMaxSize)
                .getResultList();

        List<ComponentInspectionResultInfoForBatch> resultList = new ArrayList<>();
        for (TblComponentInspectionResult inspResult : inspResultList) {
            ComponentInspectionResultInfoForBatch info = new ComponentInspectionResultInfoForBatch();

            // KM-464 部品番号読み替え対応 2017/12/4 by penggd Start
            // 会社別部品コードマスタから、部品コードを取得する
            List<MstComponentCompany> mstComponentCompanyList = this.entityManager
                    .createNamedQuery("MstComponentCompany.findByPk", MstComponentCompany.class)
                    .setParameter("companyId", incomingCompanyId)
                    .setParameter("componentId", inspResult.getComponentId()).getResultList();

            if (mstComponentCompanyList.size() > 0) {

                info.setComponentCode(mstComponentCompanyList.get(0).getOtherComponentCode());
            } else {
                info.setComponentCode(inspResult.getMstComponent().getComponentCode());
            }
            // KM-464 部品番号読み替え対応 2017/12/4 by penggd End

            inspResult.setMstComponent(null);
            inspResult.setMstCompanyOutgoing(null);
            inspResult.setMstCompanyIncoming(null);
            // KM-389 非公開コメントは連携対象外 start
            inspResult.setOutgoingPrivateComment(null);
            // KM-389 非公開コメントは連携対象外 end
            info.setInspectionResult(inspResult);

            // get inspection result details
            String inspectionResultId = inspResult.getId();
            String componentId = inspResult.getComponentId();

            // 検査明細情報非連携の対応 2018/2/8 by penggd Start
            List<TblComponentInspectionResultDetail> tblResultDetails = this.entityManager
                    .createQuery(
                            "SELECT t FROM TblComponentInspectionResultDetail t"
                                    + " INNER JOIN TblComponentInspectionResult rs on rs.id = t.componentInspectionResultId "
                                    + " INNER JOIN MstComponentInspectionItemsTableDetail m on rs.componentInspectionItemsTableId = m.componentInspectionItemsTableId and m.inspectionItemSno= t.inspectionItemSno "
                                    + " WHERE t.componentInspectionResultId = :componentInspectionResultId AND t.inspectionType = :inspectionType AND m.additionalFlg = :additionalFlg "
                                    + " order by m.localSeq,m.inspectionItemSno ",
                            TblComponentInspectionResultDetail.class)
                    .setParameter("componentInspectionResultId", inspectionResultId)
                    .setParameter("inspectionType", CommonConstants.INSPECTION_TYPE_OUTGOING)
                    .setParameter("additionalFlg", CommonConstants.ITEMS_TABLE_ADDITIONAL_FLG_YES.charAt(0)).getResultList();
            // 検査明細情報非連携の対応 2018/2/8 by penggd End

            info.setInspectionResultDetails(tblResultDetails);

            // get inspection sampleName 2018/2/8 by penggd Start
            List<TblComponentInspectionSampleName> inspectionSampleNames = this.entityManager
                    .createNamedQuery("TblComponentInspectionSampleName.findByComponentInspectionResultId",
                            TblComponentInspectionSampleName.class)
                    .setParameter("componentInspectionResultId", inspectionResultId)
                    .getResultList();
            info.setInspectionSampleNames(inspectionSampleNames);
            // get inspection sampleName 2018/2/8 by penggd End

            // get inspection visual image files
            List<String> resultDetailIdList = tblResultDetails.stream()
                    .filter(predicate -> predicate.getSeq() != 0)
                    .map(mapper -> mapper.getId()).distinct()
                    .collect(Collectors.toCollection(ArrayList::new));

            // 検査管理項目がない場合、ソースを改善　by penggd 2018/2/27 Start
            if (!resultDetailIdList.isEmpty()) {

                queryBuilder = new StringBuilder();
                queryBuilder.append("SELECT t FROM TblComponentInspectionVisualImageFile t");
                queryBuilder.append(
                        " WHERE t.tblComponentInspectionVisualImageFilePK.componentInspectionResultDetailId IN :componentInspectionResultDetailId");
                queryBuilder.append(" ORDER BY t.tblComponentInspectionVisualImageFilePK.seq");
                List<TblComponentInspectionVisualImageFile> tblImageFileList = this.entityManager
                        .createQuery(queryBuilder.toString(), TblComponentInspectionVisualImageFile.class)
                        .setParameter("componentInspectionResultDetailId", resultDetailIdList).getResultList();
                info.setInspectionVisualImageFiles(tblImageFileList);
            } else {
                info.setInspectionVisualImageFiles(new ArrayList<>());
            }
            // 検査管理項目がない場合、ソースを改善　by penggd 2018/2/27 End

            // get inspection reference file
            TblComponentInspectionReferenceFile tblReferenceFile = this.entityManager
                    .createNamedQuery("TblComponentInspectionReferenceFile.findByComponentInspectionResultId", TblComponentInspectionReferenceFile.class)
                    .setParameter("componentInspectionResultId", inspectionResultId)
                    .getResultList().stream().findFirst().orElse(null);
            info.setInspectionRefFile(tblReferenceFile);

            // 最新部品検査参照ファイルを取得 2017/10/27 penggd Start
            TblComponentInspectionReferenceFileNewest inspectionRefFileNewest = this.entityManager
                    .createNamedQuery("TblComponentInspectionReferenceFileNewest.findByComponentId", TblComponentInspectionReferenceFileNewest.class)
                    .setParameter("componentId", componentId)
                    .getResultList().stream().findFirst().orElse(null);
            info.setInspectionRefFileNewest(inspectionRefFileNewest);
            // 最新部品検査参照ファイルを取得 2017/10/27 penggd End
            resultList.add(info);
        }

        return resultList;
    }

    /**
     * Get external incoming inspection result
     *
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @return
     */
    public List<ComponentInspectionResultInfoForBatch> getExtIncomingInspectionResult(String outgoingCompanyId, String incomingCompanyId) {
        int postMaxSize = Integer.parseInt(cnfSystemService.findByKey("system", "incoming_insp_post_size").getConfigValue());
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t FROM TblComponentInspectionResult t");
        queryBuilder.append(" JOIN FETCH t.mstComponent");
        queryBuilder.append(" WHERE t.outgoingCompanyId = :outgoingCompanyId");
        queryBuilder.append(" AND t.incomingCompanyId = :incomingCompanyId");
        queryBuilder.append(" AND t.inspBatchUpdateStatus = :inspBatchUpdateStatus");
        queryBuilder.append(" ORDER BY t.createDate ");

        List<TblComponentInspectionResult> inspResultList = this.entityManager.createQuery(
                queryBuilder.toString(), TblComponentInspectionResult.class)
                .setParameter("outgoingCompanyId", outgoingCompanyId)
                .setParameter("incomingCompanyId", incomingCompanyId)
                .setParameter("inspBatchUpdateStatus", CommonConstants.INSP_BATCH_UPDATE_STATUS_I_RESULT_NOT_PUSH)
                .setMaxResults(postMaxSize)
                .getResultList();

        List<ComponentInspectionResultInfoForBatch> resultList = new ArrayList<>();

        if (inspResultList.size() > 0) {

            for (TblComponentInspectionResult action : inspResultList) {

                ComponentInspectionResultInfoForBatch batchData = new ComponentInspectionResultInfoForBatch();

                batchData.setComponentCode(action.getMstComponent().getComponentCode());
                action.setMstComponent(null);
                action.setMstCompanyOutgoing(null);
                action.setMstCompanyIncoming(null);
                // KM-389 非公開コメントは連携対象外 start
                action.setIncomingPrivateComment(null);
                // KM-389 非公開コメントは連携対象外 end
                batchData.setInspectionResult(action);

                // 部品検査別参照ファイルを取得 2017/10/30 penggd Start
                TblComponentInspectionReferenceFile tblReferenceFile = this.entityManager
                        .createNamedQuery("TblComponentInspectionReferenceFile.findByComponentInspectionResultId",
                                TblComponentInspectionReferenceFile.class)
                        .setParameter("componentInspectionResultId", action.getId()).getResultList().stream()
                        .findFirst().orElse(null);
                batchData.setInspectionRefFile(tblReferenceFile);
                // 部品検査別参照ファイルを取得 2017/10/30 penggd End

                // 最新部品検査参照ファイルを取得 2017/10/30 penggd Start
                TblComponentInspectionReferenceFileNewest inspectionRefFileNewest = this.entityManager
                        .createNamedQuery("TblComponentInspectionReferenceFileNewest.findByComponentId",
                                TblComponentInspectionReferenceFileNewest.class)
                        .setParameter("componentId", action.getComponentId()).getResultList().stream().findFirst()
                        .orElse(null);
                batchData.setInspectionRefFileNewest(inspectionRefFileNewest);
                // 最新部品検査参照ファイルを取得 2017/10/30 penggd Start

                // set result detail - start
                batchData.setInspectionResultDetails(this.entityManager
                        .createNamedQuery("TblComponentInspectionResultDetail.findByComponentInspectionResultIdAndInspectionType", TblComponentInspectionResultDetail.class)
                        .setParameter("componentInspectionResultId", action.getId())
                        .setParameter("inspectionType", CommonConstants.INSPECTION_TYPE_INCOMING).getResultList());

                batchData.setInspectionSampleNames(this.entityManager
                        .createNamedQuery("TblComponentInspectionSampleName.findByComponentInspectionResultIdAndInspectionType",TblComponentInspectionSampleName.class)
                        .setParameter("componentInspectionResultId", action.getId())
                        .setParameter("inspectionType", CommonConstants.INSPECTION_TYPE_INCOMING).getResultList());

                List<TblComponentInspectionVisualImageFile> visualImageFileList = 
                    this.entityManager.createNamedQuery("TblComponentInspectionVisualImageFile.findByComponentInspectionResultId", TblComponentInspectionVisualImageFile.class)
                        .setParameter("componentInspectionResultId", action.getId())
                        .setParameter("inspectionType", CommonConstants.INSPECTION_TYPE_INCOMING).getResultList();
                
                batchData.setInspectionVisualImageFiles(visualImageFileList);
                // set result detail - end

                resultList.add(batchData);
            }
        }

        return resultList;
    }

    public TblComponentInspectionResult getInspectionResultById(String id) {
        return this.entityManager
                .createNamedQuery("TblComponentInspectionResult.findById", TblComponentInspectionResult.class)
                .setParameter("id", id).getResultList().stream().findFirst()
                .orElse(null);
    }

    /**
     * 受入検査承認API
     *
     * @param extCompleteInput
     * @param loginUser
     */
    @Transactional
    public void updateComponentInspectionExtComplete(ComponentInspectionExtCompleteInput extCompleteInput, LoginUser loginUser) {
        MstUser user = this.mstUserService.getMstUser(loginUser.getUserid());
        Date tzNow = getTZNow(loginUser.getJavaZoneId());
        CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
        entityManager.createNamedQuery("TblComponentInspectionResult.findById", TblComponentInspectionResult.class)
            .setParameter("id", extCompleteInput.getId()).getResultList().stream().findFirst().ifPresent(result -> {
                result.setInspectionStatus(extCompleteInput.getInspectionStatus());
                result.setIncomingInspectionComment(extCompleteInput.getIncomingInspectionComment());
                result.setUpdateDate(new Date());
                result.setUpdateUserUuid(loginUser.getUserUuid());
                result.setIncomingInspectionResult(extCompleteInput.getIncomingInspectionResult());
                result.setIncomingInspectionDate(tzNow);
                setWeekAndMonth(result, cnf);
                result.setIncomingInspectionPersonUuid(loginUser.getUserUuid());
                result.setIncomingInspectionPersonName(user.getUserName());
                if(extCompleteInput.getInspectionStatus() == CommonConstants.INSPECTION_STATUS_I_APPROVED) {
                    result.setIncomingInspectionApproveDate(tzNow);
                    result.setIncomingInspectionApprovePersonName(user.getUserName());
                    result.setIncomingInspectionApprovePersonUuid(loginUser.getUserUuid());
                    result.setIncomingApproveResult(ApproveResult.APPROVED);
                    if (getInspectionResultDetailByOutgoingExist(extCompleteInput.getId()) &&
                            !isInternalOutgoing(result.getOutgoingCompanyId(), result.getIncomingCompanyId())) {
                        result.setInspBatchUpdateStatus(CommonConstants.INSP_BATCH_UPDATE_STATUS_I_RESULT_NOT_PUSH);
                    }
                }
                entityManager.merge(result);
        });
    }


    /**
     * Import inspection outgoing result data by batch
     *
     * @param inputDataList
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @return
     */
    @Transactional
    public Map<String, String> importOutgoingDataByBatch(List<ComponentInspectionResultInfoForBatch> inputDataList,
                                                         String outgoingCompanyId, String incomingCompanyId) {

        List<String> componentCodeList = inputDataList.stream()
                .map(mapper -> mapper.getComponentCode()).collect(Collectors.toCollection(ArrayList::new));

        List<MstComponent> mstComponentList = this.entityManager
                .createNamedQuery("MstComponent.findByComponentCodeList", MstComponent.class)
                .setParameter("componentCode", componentCodeList)
                .getResultList();
        Map<String, String> mstComponentMap = new HashMap<>();
        mstComponentList.stream().forEach(action -> mstComponentMap.put(action.getComponentCode(), action.getId()));

        Map<String, String> imagePdfUUidMap = new LinkedHashMap<>();
        if (inputDataList.size() > 0) {

            for (ComponentInspectionResultInfoForBatch inputData : inputDataList) {

                if (StringUtils.isEmpty(mstComponentMap.get(inputData.getComponentCode()))) {
                    continue;
                }

                TblComponentInspectionResult inspectionResultInput = inputData.getInspectionResult();

                TblComponentInspectionResult tblResult = this.entityManager
                        .createNamedQuery("TblComponentInspectionResult.findById", TblComponentInspectionResult.class)
                        .setParameter("id", inspectionResultInput.getId()).getResultList().stream().findFirst()
                        .orElse(null);

                if (tblResult == null) {
                    // update own id due to these ids are different between
                    // different servers

                    inspectionResultInput.setComponentId(mstComponentMap.get(inputData.getComponentCode()));
                    inspectionResultInput.setOutgoingCompanyId(outgoingCompanyId);
                    inspectionResultInput.setIncomingCompanyId(incomingCompanyId);
                    inspectionResultInput
                            .setInspBatchUpdateStatus(CommonConstants.INSP_BATCH_UPDATE_STATUS_O_RESULT_GET_NOT_NOTIFY);
                    this.entityManager.persist(inspectionResultInput);

                    List<TblComponentInspectionResultDetail> inspectionResultDetails = inputData
                            .getInspectionResultDetails();
                    inspectionResultDetails.stream().forEach(resultDetail -> {
                        this.entityManager.persist(resultDetail);
                    });

                    // tbl_component_inspection_sample_nameデータを連携 2018/2/8 by penggd Start
                    List<TblComponentInspectionSampleName> inspectionSampleNames = inputData
                            .getInspectionSampleNames();
                    inspectionSampleNames.stream().forEach(inspectionSampleName -> {
                        this.entityManager.persist(inspectionSampleName);
                    });
                    // tbl_component_inspection_sample_nameデータを連携 2018/2/8 by penggd End

                    List<TblComponentInspectionVisualImageFile> inspectionVisualImageFiles = inputData
                            .getInspectionVisualImageFiles();
                    if (inspectionVisualImageFiles != null) {
                        inspectionVisualImageFiles.stream().forEach(visualImageFile -> {
                            this.entityManager.persist(visualImageFile);
                            if (CommonConstants.IMAGEFILE_TYPE_IMAGE == visualImageFile.getFileType()) {
                                imagePdfUUidMap.put(visualImageFile.getFileUuid(), CommonConstants.IMAGE);
                            } else {
                                imagePdfUUidMap.put(visualImageFile.getFileUuid(), CommonConstants.VIDEO);
                                if (StringUtils.isNotBlank(visualImageFile.getThumbnailFileUuid())) {
                                    imagePdfUUidMap.put(visualImageFile.getThumbnailFileUuid(), CommonConstants.IMAGE);
                                }
                            }
                        });
                    }

                    TblComponentInspectionReferenceFile inspectionRefFile = inputData.getInspectionRefFile();
                    if (inspectionRefFile != null) {

                        TblComponentInspectionReferenceFile inspectionRefFileOld = this.entityManager
                                .createNamedQuery(
                                        "TblComponentInspectionReferenceFile.findByComponentInspectionResultId",
                                        TblComponentInspectionReferenceFile.class)
                                .setParameter("componentInspectionResultId",
                                        inspectionRefFile.getComponentInspectionResultId())
                                .getResultList().stream().findFirst().orElse(null);

                        if (null == inspectionRefFileOld) {

                            this.entityManager.persist(inspectionRefFile);
                        } else {

                            inspectionRefFileOld.setDrawingFileUuid(inspectionRefFile.getDrawingFileUuid());
                            inspectionRefFileOld.setProofFileUuid(inspectionRefFile.getProofFileUuid());
                            inspectionRefFileOld.setRohsProofFileUuid(inspectionRefFile.getRohsProofFileUuid());
                            inspectionRefFileOld.setPackageSpecFileUuid(inspectionRefFile.getPackageSpecFileUuid());
                            inspectionRefFileOld.setQcPhaseFileUuid(inspectionRefFile.getQcPhaseFileUuid());
                            inspectionRefFileOld.setUpdateDate(inspectionRefFile.getUpdateDate());
                            inspectionRefFileOld.setUpdateUserUuid(inspectionRefFile.getUpdateUserUuid());

                            this.entityManager.merge(inspectionRefFileOld);
                        }

                        if (StringUtils.isNotBlank(inspectionRefFile.getDrawingFileUuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getDrawingFileUuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getProofFileUuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getProofFileUuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getRohsProofFileUuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getRohsProofFileUuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getPackageSpecFileUuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getPackageSpecFileUuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getQcPhaseFileUuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getQcPhaseFileUuid(), CommonConstants.DOC);
                        }

                        if (StringUtils.isNotBlank(inspectionRefFile.getFile06Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile06Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile07Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile07Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile08Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile08Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile09Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile09Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile10Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile10Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile11Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile11Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile12Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile12Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile13Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile13Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile14Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile14Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile15Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile15Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile16Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile16Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile17Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile17Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile18Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile18Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile19Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile19Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFile.getFile20Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFile.getFile20Uuid(), CommonConstants.DOC);
                        }
                    }

                    // 最新部品検査参照ファイルを設定 2017/10/27 penggd Start
                    TblComponentInspectionReferenceFileNewest inspectionRefFileNewest = inputData
                            .getInspectionRefFileNewest();

                    if (inspectionRefFileNewest != null) {

                        inspectionRefFileNewest.setComponentId(mstComponentMap.get(inputData.getComponentCode()));

                        TblComponentInspectionReferenceFileNewest inspectionRefFileNewestOld = this.entityManager
                                .createNamedQuery("TblComponentInspectionReferenceFileNewest.findByComponentId",
                                        TblComponentInspectionReferenceFileNewest.class)
                                .setParameter("componentId", inspectionRefFileNewest.getComponentId()).getResultList()
                                .stream().findFirst().orElse(null);

                        if (null == inspectionRefFileNewestOld) {

                            this.entityManager.persist(inspectionRefFileNewest);
                        } else {

                            inspectionRefFileNewestOld.setDrawingFileUuid(inspectionRefFileNewest.getDrawingFileUuid());
                            inspectionRefFileNewestOld.setProofFileUuid(inspectionRefFileNewest.getProofFileUuid());
                            inspectionRefFileNewestOld
                                    .setRohsProofFileUuid(inspectionRefFileNewest.getRohsProofFileUuid());
                            inspectionRefFileNewestOld
                                    .setPackageSpecFileUuid(inspectionRefFileNewest.getPackageSpecFileUuid());
                            inspectionRefFileNewestOld.setQcPhaseFileUuid(inspectionRefFileNewest.getQcPhaseFileUuid());
                            inspectionRefFileNewestOld.setFile06Uuid(inspectionRefFileNewest.getFile06Uuid());
                            inspectionRefFileNewestOld.setFile07Uuid(inspectionRefFileNewest.getFile07Uuid());
                            inspectionRefFileNewestOld.setFile08Uuid(inspectionRefFileNewest.getFile08Uuid());
                            inspectionRefFileNewestOld.setFile09Uuid(inspectionRefFileNewest.getFile09Uuid());
                            inspectionRefFileNewestOld.setFile10Uuid(inspectionRefFileNewest.getFile10Uuid());
                            inspectionRefFileNewestOld.setFile11Uuid(inspectionRefFileNewest.getFile11Uuid());
                            inspectionRefFileNewestOld.setFile12Uuid(inspectionRefFileNewest.getFile12Uuid());
                            inspectionRefFileNewestOld.setFile13Uuid(inspectionRefFileNewest.getFile13Uuid());
                            inspectionRefFileNewestOld.setFile14Uuid(inspectionRefFileNewest.getFile14Uuid());
                            inspectionRefFileNewestOld.setFile15Uuid(inspectionRefFileNewest.getFile15Uuid());
                            inspectionRefFileNewestOld.setFile16Uuid(inspectionRefFileNewest.getFile16Uuid());
                            inspectionRefFileNewestOld.setFile17Uuid(inspectionRefFileNewest.getFile17Uuid());
                            inspectionRefFileNewestOld.setFile18Uuid(inspectionRefFileNewest.getFile18Uuid());
                            inspectionRefFileNewestOld.setFile19Uuid(inspectionRefFileNewest.getFile19Uuid());
                            inspectionRefFileNewestOld.setFile20Uuid(inspectionRefFileNewest.getFile20Uuid());
                            inspectionRefFileNewestOld.setUpdateDate(inspectionRefFileNewest.getUpdateDate());
                            inspectionRefFileNewestOld.setUpdateUserUuid(inspectionRefFileNewest.getUpdateUserUuid());

                            this.entityManager.merge(inspectionRefFileNewestOld);

                        }

                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getDrawingFileUuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getDrawingFileUuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getProofFileUuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getProofFileUuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getRohsProofFileUuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getRohsProofFileUuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getPackageSpecFileUuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getPackageSpecFileUuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getQcPhaseFileUuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getQcPhaseFileUuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile06Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile06Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile07Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile07Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile08Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile08Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile09Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile09Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile10Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile10Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile11Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile11Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile12Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile12Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile13Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile13Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile14Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile14Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile15Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile15Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile16Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile16Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile17Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile17Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile18Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile18Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile19Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile19Uuid(), CommonConstants.DOC);
                        }
                        if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile20Uuid())) {
                            imagePdfUUidMap.put(inspectionRefFileNewest.getFile20Uuid(), CommonConstants.DOC);
                        }
                    }
                    // 最新部品検査参照ファイルを設定 2017/10/27 penggd End

                    // KM-463の対応のため、該当ソースを廃止する 2017/12/6　penggd start
                    // 検査データに関連付く、POテーブルを取得 2017/11/3 penggd Start
//                    List<TblComponentInspectionResultPo> inspectionResultPoList = inputData.getInspectionPoList();
//                    inspectionResultPoList.stream().forEach(inspectionResultPo -> {
//                        this.entityManager.persist(inspectionResultPo);
//                    });
                    // 検査データに関連付く、POテーブルを取得 2017/11/3 penggd End
                    // KM-463の対応のため、該当ソースを廃止する 2017/12/6　penggd End
                } else {

                    // 遅延ファイルアップロード対応 2017/11/3 penggd Start
                    int inspectionStatus = tblResult.getInspectionStatus();

                    boolean updFileFlg = true;

                    // 入荷検査承認待ち、入荷検査完了、受入否認、免検の場合、ファイル連携しません。
                    if (CommonConstants.INSPECTION_STATUS_I_UNAPPROVED == inspectionStatus
                            || CommonConstants.INSPECTION_STATUS_I_APPROVED == inspectionStatus
                            || CommonConstants.INSPECTION_STATUS_I_REJECTED == inspectionStatus
                            || CommonConstants.INSPECTION_STATUS_I_EXEMPTED == inspectionStatus) {

                        updFileFlg = false;
                    }

                    if (updFileFlg) {

                        tblResult.setInspBatchUpdateStatus(
                                CommonConstants.INSP_BATCH_UPDATE_STATUS_O_RESULT_GET_NOT_NOTIFY);
                        this.entityManager.merge(tblResult);

                        TblComponentInspectionReferenceFile inspectionRefFile = inputData.getInspectionRefFile();
                        if (inspectionRefFile != null) {

                            this.entityManager.merge(inspectionRefFile);

                            if (StringUtils.isNotBlank(inspectionRefFile.getDrawingFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getDrawingFileUuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getProofFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getProofFileUuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getRohsProofFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getRohsProofFileUuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getPackageSpecFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getPackageSpecFileUuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getQcPhaseFileUuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getQcPhaseFileUuid(), CommonConstants.DOC);
                            }

                            if (StringUtils.isNotBlank(inspectionRefFile.getFile06Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile06Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile07Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile07Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile08Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile08Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile09Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile09Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile10Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile10Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile11Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile11Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile12Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile12Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile13Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile13Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile14Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile14Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile15Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile15Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile16Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile16Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile17Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile17Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile18Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile18Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile19Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile19Uuid(), CommonConstants.DOC);
                            }
                            if (StringUtils.isNotBlank(inspectionRefFile.getFile20Uuid())) {
                                imagePdfUUidMap.put(inspectionRefFile.getFile20Uuid(), CommonConstants.DOC);
                            }
                        }

                        TblComponentInspectionReferenceFileNewest inspectionRefFileNewest = inputData
                                .getInspectionRefFileNewest();
                        if (inspectionRefFileNewest != null) {

                            inspectionRefFileNewest.setComponentId(mstComponentMap.get(inputData.getComponentCode()));

                            TblComponentInspectionReferenceFileNewest inspectionRefFileNewestOld = this.entityManager
                                    .createNamedQuery("TblComponentInspectionReferenceFileNewest.findByComponentId",
                                            TblComponentInspectionReferenceFileNewest.class)
                                    .setParameter("componentId", inspectionRefFileNewest.getComponentId())
                                    .getResultList().stream().findFirst().orElse(null);

                            if (null != inspectionRefFileNewestOld) {

                                inspectionRefFileNewestOld
                                        .setDrawingFileUuid(inspectionRefFileNewest.getDrawingFileUuid());
                                inspectionRefFileNewestOld.setProofFileUuid(inspectionRefFileNewest.getProofFileUuid());
                                inspectionRefFileNewestOld
                                        .setRohsProofFileUuid(inspectionRefFileNewest.getRohsProofFileUuid());
                                inspectionRefFileNewestOld
                                        .setPackageSpecFileUuid(inspectionRefFileNewest.getPackageSpecFileUuid());
                                inspectionRefFileNewestOld
                                        .setQcPhaseFileUuid(inspectionRefFileNewest.getQcPhaseFileUuid());
                                inspectionRefFileNewestOld.setUpdateDate(inspectionRefFileNewest.getUpdateDate());
                                inspectionRefFileNewestOld
                                        .setUpdateUserUuid(inspectionRefFileNewest.getUpdateUserUuid());

                                this.entityManager.merge(inspectionRefFileNewestOld);

                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getDrawingFileUuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getDrawingFileUuid(),
                                            CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getProofFileUuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getProofFileUuid(),
                                            CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getRohsProofFileUuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getRohsProofFileUuid(),
                                            CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getPackageSpecFileUuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getPackageSpecFileUuid(),
                                            CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getQcPhaseFileUuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getQcPhaseFileUuid(),
                                            CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile06Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile06Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile07Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile07Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile08Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile08Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile09Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile09Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile10Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile10Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile11Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile11Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile12Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile12Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile13Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile13Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile14Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile14Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile15Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile15Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile16Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile16Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile17Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile17Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile18Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile18Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile19Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile19Uuid(), CommonConstants.DOC);
                                }
                                if (StringUtils.isNotBlank(inspectionRefFileNewest.getFile20Uuid())) {
                                    imagePdfUUidMap.put(inspectionRefFileNewest.getFile20Uuid(), CommonConstants.DOC);
                                }
                            }

                        }
                    }
                    // 遅延ファイルアップロード対応 2017/11/3 penggd End
                }
            }
        }

        return imagePdfUUidMap;
    }

    /**
     * update inspection incoming result data by batch
     *
     * @param inputDataList
     * @param incomingCompanyId
     * @return
     */
    @Transactional
    public TblComponentInspectionResultVo updateIncomingData(List<ComponentInspectionResultInfoForBatch> inputDataList,
                                                             String incomingCompanyId) {

        TblComponentInspectionResultVo response = new TblComponentInspectionResultVo();
        CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
        if (inputDataList.size() > 0) {

            List<String> componentCodeList = new ArrayList<String>();

            for (ComponentInspectionResultInfoForBatch mapper : inputDataList) {
                componentCodeList.add(mapper.getComponentCode());
            }

            List<MstComponent> mstComponentList = new ArrayList<MstComponent>();

            // KM-464 部品番号読み替え対応 2017/12/4 by penggd Start
            if (null != inputDataList.get(0).getInspectionResult()) {

                if (componentCodeList.size() > 0) {

                    for (int i = 0; i < componentCodeList.size(); i++) {

                        String tempComponentCode = componentCodeList.get(i);

                        // 会社別部品コードマスタから、部品IDを取得する
                        List<MstComponentCompany> mstComponentCompanyList = this.entityManager
                                .createNamedQuery("MstComponentCompany.findByCompanyIdAndComponentCode",
                                        MstComponentCompany.class)
                                .setParameter("companyId", incomingCompanyId)
                                .setParameter("componentCode", tempComponentCode).getResultList();

                        if (mstComponentCompanyList.size() > 0) {

                            MstComponent tempMstComponent = new MstComponent();

                            tempMstComponent
                                    .setId(mstComponentCompanyList.get(0).getMstComponentCompanyPK().getComponentId());
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
            }
            // KM-464 部品番号読み替え対応 2017/12/4 by penggd End

            Map<String, String> mstComponentMap = new HashMap<>();
            List<String> noExistCode = new ArrayList<String>();
            List<String> noExistId = new ArrayList<String>();

            mstComponentList.stream().forEach(action -> mstComponentMap.put(action.getComponentCode(), action.getId()));

            for (ComponentInspectionResultInfoForBatch inputData : inputDataList) {

                if (StringUtils.isEmpty(mstComponentMap.get(inputData.getComponentCode()))) {
                    noExistCode.add(inputData.getComponentCode());
                    noExistId.add(inputData.getInspectionResult().getId());
                    continue;
                }

                TblComponentInspectionResult inspectionResultInput = inputData.getInspectionResult();
                this.entityManager.createNamedQuery("TblComponentInspectionResult.findById", TblComponentInspectionResult.class)
                        .setParameter("id", inspectionResultInput.getId()).getResultList().stream().findFirst().ifPresent(tblResult -> {
                    tblResult.setInspectionStatus(inspectionResultInput.getInspectionStatus());

                    tblResult.setAcceptancePersonName(inspectionResultInput.getAcceptancePersonName());
                    tblResult.setAcceptanceDate(inspectionResultInput.getAcceptanceDate());

                    tblResult.setExemptionApprovePersonName(inspectionResultInput.getExemptionApprovePersonName());
                    tblResult.setExemptionApproveDate(inspectionResultInput.getExemptionApproveDate());

                    tblResult.setIncomingInspectionPersonName(inspectionResultInput.getIncomingInspectionPersonName());
                    tblResult.setIncomingInspectionDate(inspectionResultInput.getIncomingInspectionDate());
                    setWeekAndMonth(tblResult, cnf);
                    tblResult.setIncomingInspectionResult(inspectionResultInput.getIncomingInspectionResult());

                    tblResult.setIncomingInspectionApprovePersonName(inspectionResultInput.getIncomingInspectionApprovePersonName());
                    tblResult.setIncomingInspectionApproveDate(inspectionResultInput.getIncomingInspectionApproveDate());
                    tblResult.setIncomingInspectionComment(inspectionResultInput.getIncomingInspectionComment());
                    tblResult.setIncomingApproveResult(inspectionResultInput.getIncomingApproveResult());

                    // KM-385 入荷確認者を連携する start
                    tblResult.setIncomingConfirmerUuid(inspectionResultInput.getIncomingConfirmerUuid());
                    tblResult.setIncomingConfirmerName(inspectionResultInput.getIncomingConfirmerName());
                    tblResult.setIncomingConfirmDate(inspectionResultInput.getIncomingConfirmDate());
                    tblResult.setIncomingConfirmResult(inspectionResultInput.getIncomingConfirmResult());
                    // KM-385 入荷確認者を連携する start

                    // acv start
                    tblResult.setIncomingMeasSamplingQuantity(inspectionResultInput.getIncomingMeasSamplingQuantity());
                    tblResult.setIncomingVisualSamplingQuantity(inspectionResultInput.getIncomingVisualSamplingQuantity());
                    tblResult.setIncomingInspectionComment(inspectionResultInput.getIncomingInspectionComment());
                    // acv end
                    
                    // 帳票ファイル確認者を連携する
                    tblResult.setFileApproverId(inspectionResultInput.getFileApproverId());
                    tblResult.setFileApproverName(inspectionResultInput.getFileApproverName());
                    tblResult.setFileConfirmStatus(inspectionResultInput.getFileConfirmStatus());

                    this.entityManager.merge(tblResult);

                    // 部品検査別参照ファイルを連携 2017/10/30 penggd Start
                    if (null != inputData.getInspectionRefFile()) {
                        this.entityManager.merge(inputData.getInspectionRefFile());
                    }
                    // 部品検査別参照ファイルを連携 2017/10/30 penggd End

                    // 最新部品検査参照ファイルを連携 2017/10/30 penggd Start
                    if (null != inputData.getInspectionRefFileNewest()) {

                        inputData.getInspectionRefFileNewest()
                                .setComponentId(mstComponentMap.get(inputData.getComponentCode()));

                        TblComponentInspectionReferenceFileNewest inspectionRefFileNewestOld = this.entityManager
                                .createNamedQuery("TblComponentInspectionReferenceFileNewest.findByComponentId", TblComponentInspectionReferenceFileNewest.class)
                                .setParameter("componentId", inputData.getInspectionRefFileNewest().getComponentId())
                                .getResultList().stream().findFirst().orElse(null);

                        if (null == inspectionRefFileNewestOld) {

                            this.entityManager.persist(inputData.getInspectionRefFileNewest());

                        } else {

                            inspectionRefFileNewestOld
                                    .setDrawingFileUuid(inputData.getInspectionRefFileNewest().getDrawingFileUuid());
                            inspectionRefFileNewestOld
                                    .setProofFileUuid(inputData.getInspectionRefFileNewest().getProofFileUuid());
                            inspectionRefFileNewestOld
                                    .setRohsProofFileUuid(inputData.getInspectionRefFileNewest().getRohsProofFileUuid());
                            inspectionRefFileNewestOld.setPackageSpecFileUuid(
                                    inputData.getInspectionRefFileNewest().getPackageSpecFileUuid());
                            inspectionRefFileNewestOld
                                    .setQcPhaseFileUuid(inputData.getInspectionRefFileNewest().getQcPhaseFileUuid());
                            inspectionRefFileNewestOld
                                    .setUpdateDate(inputData.getInspectionRefFileNewest().getUpdateDate());
                            inspectionRefFileNewestOld
                                    .setUpdateUserUuid(inputData.getInspectionRefFileNewest().getUpdateUserUuid());

                            inspectionRefFileNewestOld
                                    .setFile06Uuid(inputData.getInspectionRefFileNewest().getFile06Uuid());
                            inspectionRefFileNewestOld
                                    .setFile07Uuid(inputData.getInspectionRefFileNewest().getFile07Uuid());
                            inspectionRefFileNewestOld
                                    .setFile08Uuid(inputData.getInspectionRefFileNewest().getFile08Uuid());
                            inspectionRefFileNewestOld
                                    .setFile09Uuid(inputData.getInspectionRefFileNewest().getFile09Uuid());
                            inspectionRefFileNewestOld
                                    .setFile10Uuid(inputData.getInspectionRefFileNewest().getFile10Uuid());
                            inspectionRefFileNewestOld
                                    .setFile11Uuid(inputData.getInspectionRefFileNewest().getFile11Uuid());
                            inspectionRefFileNewestOld
                                    .setFile12Uuid(inputData.getInspectionRefFileNewest().getFile12Uuid());
                            inspectionRefFileNewestOld
                                    .setFile13Uuid(inputData.getInspectionRefFileNewest().getFile13Uuid());
                            inspectionRefFileNewestOld
                                    .setFile14Uuid(inputData.getInspectionRefFileNewest().getFile14Uuid());
                            inspectionRefFileNewestOld
                                    .setFile15Uuid(inputData.getInspectionRefFileNewest().getFile15Uuid());
                            inspectionRefFileNewestOld
                                    .setFile16Uuid(inputData.getInspectionRefFileNewest().getFile16Uuid());
                            inspectionRefFileNewestOld
                                    .setFile17Uuid(inputData.getInspectionRefFileNewest().getFile17Uuid());
                            inspectionRefFileNewestOld
                                    .setFile18Uuid(inputData.getInspectionRefFileNewest().getFile18Uuid());
                            inspectionRefFileNewestOld
                                    .setFile19Uuid(inputData.getInspectionRefFileNewest().getFile19Uuid());
                            inspectionRefFileNewestOld
                                    .setFile20Uuid(inputData.getInspectionRefFileNewest().getFile20Uuid());
                            this.entityManager.merge(inspectionRefFileNewestOld);
                        }
                    }
                    // 最新部品検査参照ファイルを連携 2017/10/30 penggd End

                    // save result detail - start
                    List<TblComponentInspectionResultDetail> detailList = inputData.getInspectionResultDetails();
                    List<TblComponentInspectionResultDetail> resultDetails = this.entityManager
                            .createNamedQuery("TblComponentInspectionResultDetail.findByComponentInspectionResultIdAndInspectionType", TblComponentInspectionResultDetail.class)
                            .setParameter("componentInspectionResultId", inputData.getInspectionResult().getId())
                            .setParameter("inspectionType", CommonConstants.INSPECTION_TYPE_INCOMING)
                            .getResultList();
                    Set<String> detailIdSet = resultDetails.stream().map(d -> d.getId()).collect(Collectors.toSet());
                    if (detailList != null) {
                        detailList.forEach(detail -> {
                            if (!detailIdSet.contains(detail.getId())) {
                                this.entityManager.persist(detail);
                            }
                        });
                    }

                    List<TblComponentInspectionVisualImageFile> visualImageFileList = inputData.getInspectionVisualImageFiles();
                    List<TblComponentInspectionVisualImageFile> iVisualImageFiles = this.entityManager
                            .createNamedQuery("TblComponentInspectionVisualImageFile.findByComponentInspectionResultId", TblComponentInspectionVisualImageFile.class)
                            .setParameter("componentInspectionResultId", inputData.getInspectionResult().getId())
                            .setParameter("inspectionType", CommonConstants.INSPECTION_TYPE_INCOMING)
                            .getResultList();
                    Set<TblComponentInspectionVisualImageFilePK> inspectionVisualImageFilePKSet = iVisualImageFiles.stream().map(v -> v.getTblComponentInspectionVisualImageFilePK()).collect(Collectors.toSet());
                    if (visualImageFileList != null) {
                        visualImageFileList.forEach(visualImageFile -> {
                            if (!inspectionVisualImageFilePKSet.contains(visualImageFile.getTblComponentInspectionVisualImageFilePK())) {
                                this.entityManager.persist(visualImageFile);
                            }
                        });
                    }

                    List<TblComponentInspectionSampleName> sampleNameList = inputData.getInspectionSampleNames();
                    List<TblComponentInspectionSampleName> inspectionSampleNames = this.entityManager
                            .createNamedQuery("TblComponentInspectionSampleName.findByComponentInspectionResultIdAndInspectionType", TblComponentInspectionSampleName.class)
                            .setParameter("componentInspectionResultId", inputData.getInspectionResult().getId())
                            .setParameter("inspectionType", CommonConstants.INSPECTION_TYPE_INCOMING)
                            .getResultList();
                    Set<String> sampleNameIdSet = inspectionSampleNames.stream().map(s -> s.getId()).collect(Collectors.toSet());
                    if (sampleNameList != null) {
                        sampleNameList.forEach(sampleName -> {
                            if (!sampleNameIdSet.contains(sampleName.getId())) {
                                this.entityManager.persist(sampleName);
                            }
                        });
                    }
                    // save result detail - end
                });
            }
            
            for (ComponentInspectionResultInfoForBatch inputData : inputDataList) {

                if (StringUtils.isEmpty(mstComponentMap.get(inputData.getComponentCode()))) {
                    continue;
                }
                TblComponentInspectionResult inspectionResultInput = inputData.getInspectionResult();
                this.entityManager.createNamedQuery("TblComponentInspectionResult.findById", TblComponentInspectionResult.class)
                        .setParameter("id", inspectionResultInput.getId()).getResultList().stream().findFirst().ifPresent(tblResult -> {

                    if (inspectionResultInput.getInspectionStatus() == CommonConstants.INSPECTION_STATUS_I_APPROVED
                            && inspectionResultInput.getIncomingInspectionResult() == CommonConstants.INSPECTION_RESULT_NG) {

                        TblComponentInspectionSendMail sendMailNotice = new TblComponentInspectionSendMail();

                        sendMailNotice.setResultId(tblResult.getId());

                        // 出荷会社名称を取得
                        MstCompany outgoingCompany = mstCompanyService.getByCompanyIdName(tblResult.getOutgoingCompanyId());
                        if (null != outgoingCompany) {
                            sendMailNotice.setOutgoingCompanyName(outgoingCompany.getCompanyName());
                        }

                        // 部品情報を取得
                        MstComponent component = mstComponentService.getMstComponentById(tblResult.getComponentId());
                        if (null != component) {
                            sendMailNotice.setComponentCode(component.getComponentCode());
                            sendMailNotice.setComponentName(component.getComponentName());
                        }

                        String poNumber = "";
                        if (StringUtils.isNotEmpty(tblResult.getProductionLotNum()) && !CommonConstants.PRODUCTION_LOT_NUMBER.equals(tblResult.getProductionLotNum())) {
                            TblComponentInspectionResultPoList poList = this.getTblPoOutbound(
                                    tblResult.getComponentId(),
                                    tblResult.getProductionLotNum(),
                                    tblResult.getIncomingCompanyId(),
                                    tblResult.getOutgoingCompanyId());

                            if (poList != null && poList.getTblComponentInspectionResultPoVos() != null && poList.getTblComponentInspectionResultPoVos().size() > 0) {
                                for (TblComponentInspectionResultPoVo poVo : poList.getTblComponentInspectionResultPoVos()) {
                                    poNumber += poVo.getOrderNumber().concat("-").concat(poVo.getItemNumber()) + "/";
                                }
                                if (StringUtils.isNotEmpty(poNumber)) {
                                    poNumber = poNumber.substring(0, poNumber.length() - 1);
                                }
                            }
                            sendMailNotice.setLotNum(tblResult.getProductionLotNum());
                        } else {
                            sendMailNotice.setLotNum("");
                        }
                        sendMailNotice.setPo(poNumber);
                        sendMailNotice.setIncomingInspectionDate(FileUtil.dateFormat(inspectionResultInput.getIncomingInspectionDate()));
                        sendMailNotice.setIncomingComment(inspectionResultInput.getIncomingInspectionComment());
                        // 検査表URL用の情報
                        sendMailNotice.setBaseUrl(kartePropertyService.getBaseUrl());
                        sendMailNotice.setAct("5");
                        sendMailNotice.setFunctionType("3");
                        // 部署情報を設定
                        List<Integer> departmentList = new ArrayList<>();
                        MstUser user = mstUserService.getMstUserByUuid(tblResult.getOutgoingInspectionPersonUuid());
                        if (null != user) {
                            if (StringUtils.isNotEmpty(user.getDepartment())) {
                                departmentList.add(Integer.valueOf(user.getDepartment()));
                            }
                        }
                        sendMailNotice.setUserDepartmentList(departmentList);
                        if (sendMailNotice.getUserDepartmentList().size() > 0) {
                            tblComponentInsectionSendMailService.sendNotice(CommonConstants.INCOMING_INSPECTION_NG, sendMailNotice);//受入検査結果（不合格）
                        }
                    }

                });
            }

            if (!noExistCode.isEmpty()) {
                response.setIdList(noExistId);
            }
        }

        return response;
    }

    /**
     * Get data which had get outgoing result but not yet notify to supplier
     *
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @return
     */
    public List<String> getNotYetNotifyResult(String outgoingCompanyId, String incomingCompanyId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t.id FROM TblComponentInspectionResult t");
        queryBuilder.append(" WHERE t.outgoingCompanyId = :outgoingCompanyId");
        queryBuilder.append(" AND t.incomingCompanyId = :incomingCompanyId");
        queryBuilder.append(" AND t.inspBatchUpdateStatus = :inspBatchUpdateStatus");

        return this.entityManager
                .createQuery(queryBuilder.toString(), String.class)
                .setParameter("outgoingCompanyId", outgoingCompanyId)
                .setParameter("incomingCompanyId", incomingCompanyId)
                .setParameter("inspBatchUpdateStatus", CommonConstants.INSP_BATCH_UPDATE_STATUS_O_RESULT_GET_NOT_NOTIFY)
                .getResultList();
    }

    /**
     * Update inspection batch update status
     *
     * @param resultIdList
     * @param targetInspBatchUpdateStatus
     * @return
     */
    @Transactional
    public BasicResponse updateInspBatchUpdateStatus(List<String> resultIdList, String targetInspBatchUpdateStatus) {
        BasicResponse response = new BasicResponse();
        if (resultIdList == null || resultIdList.isEmpty()) {
            return response;
        }
        this.entityManager.createQuery(
                "UPDATE TblComponentInspectionResult t SET t.inspBatchUpdateStatus = :inspBatchUpdateStatus WHERE t.id IN :idList")
                .setParameter("idList", resultIdList)
                .setParameter("inspBatchUpdateStatus", targetInspBatchUpdateStatus)
                .executeUpdate();

        return response;
    }

    /*  private methods
    |========================================================================*/

    /**
     * Make where clause
     *
     * @param searchCondition
     * @param isCsv
     * @return
     */
    private String makeWhereClauseByQueryCondition(ComponentInspectionResultSearchCond searchCondition, boolean isCsv) {
        StringBuilder queryBuilder = new StringBuilder();
        if (isCsv) {
            queryBuilder.append(" WHERE 1=1 ");
        } else {
            queryBuilder.append(" WHERE (t.outgoingCompanyId = :myCompanyId OR t.incomingCompanyId = :myCompanyId) ");
        }
        
        if(StringUtils.isNotBlank(searchCondition.getFileConfirmStatus())){
            queryBuilder.append(" AND t.id = ref.componentInspectionResultId ");
        }
        
        // km-976 帳票確認者の検索を追加 20181121 start
        // 帳票確認者名前
        if(StringUtils.isNotBlank(searchCondition.getFileApproverName())){
            queryBuilder.append(" AND t.fileApproverName like :fileApproverName ");
        }
        // km-976 帳票確認者の検索を追加 20181121 end
        
        // 部品
        if(StringUtils.isNotBlank(searchCondition.getComponent())){
            queryBuilder.append(" AND (t.mstComponent.componentCode LIKE :component OR t.mstComponent.componentName LIKE :component)");
        }
        // 部品業種
        if (StringUtils.isNotBlank(searchCondition.getComponentInspectType())) {
            queryBuilder.append(" AND t.mstTable.inspectTypeDictKey = t.mstTable.mstComponentInspectLang.mstComponentInspectLangPK.dictKey ");
            queryBuilder.append(" AND t.mstTable.mstComponentInspectLang.dictValue LIKE :componentInspectType");
        }
        
        if (StringUtils.isNotBlank(searchCondition.getProductionLotNum())) {
            queryBuilder.append(" AND t.productionLotNum LIKE :productionLotNum");
        }
        if (StringUtils.isNotBlank(searchCondition.getPoNumber())) {
            queryBuilder.append(" AND (EXISTS");
            queryBuilder.append(" (SELECT shipmentOutbound FROM TblShipmentOutbound shipmentOutbound");
            queryBuilder.append(" JOIN shipmentOutbound.tblPoOutbound poOutbound");
            queryBuilder.append(" WHERE shipmentOutbound.productionLotNumber = t.productionLotNum AND shipmentOutbound.componentId = t.componentId ");
            queryBuilder.append(" AND concat(poOutbound.orderNo,'-',poOutbound.itemNo) LIKE :poNumber) ");
            queryBuilder.append(" OR (EXISTS(SELECT shipment FROM TblShipment shipment");
            queryBuilder.append(" JOIN shipment.tblPo po");
            queryBuilder.append(" WHERE shipment.productionLotNumber = t.productionLotNum AND shipment.componentId = t.componentId ");
            queryBuilder.append("AND concat(po.orderNumber,'-',po.itemNumber) LIKE :poNumber)) )");
        }
        if (StringUtils.isNotBlank(searchCondition.getOutgoingCompanyName())) {
            queryBuilder.append(" AND (t.mstCompanyOutgoing.companyCode LIKE :outgoingCompanyName OR t.mstCompanyOutgoing.companyName LIKE :outgoingCompanyName)");
        }
        if (StringUtils.isNotBlank(searchCondition.getIncomingCompanyName())) {
            queryBuilder.append(" AND (t.mstCompanyIncoming.companyCode LIKE :incomingCompanyName OR t.mstCompanyIncoming.companyName LIKE :incomingCompanyName)");
        }
        
        // 検査ステータス
        if (StringUtils.isNotBlank(searchCondition.getInspectionStatus())) {

            if (StringUtils.isNotBlank(searchCondition.getFileCondition())) {

                queryBuilder.append(" AND (t.inspectionStatus IN :inspectionStatus");

                if ("1".equals(searchCondition.getFileCondition())) {// 検査ステータスは入荷検査承認待ち、且つ帳票ステータス＜＞確認済
                    queryBuilder.append(" OR (t.inspectionStatus = :inpectionStatusUnapproved AND t.fileConfirmStatus <> :confirmed)) ");
                } else if ("2".equals(searchCondition.getFileCondition())) {// 検査ステータスは入荷検査承認待ち、且つ帳票ステータス＝確認済
                    queryBuilder.append(" OR (t.inspectionStatus = :inpectionStatusUnapproved AND t.fileConfirmStatus = :confirmed)) ");
                }
            } else {
                queryBuilder.append(" AND t.inspectionStatus IN :inspectionStatus");
            }

        } else if (StringUtils.isNotBlank(searchCondition.getFileCondition())) {

            if ("1".equals(searchCondition.getFileCondition())) {// 検査ステータスは入荷検査承認待ち、且つ帳票ステータス＜＞確認済
                queryBuilder.append(" AND (t.inspectionStatus = :inpectionStatusUnapproved AND t.fileConfirmStatus <> :confirmed)");
            } else if ("2".equals(searchCondition.getFileCondition())) {// 検査ステータスは入荷検査承認待ち、且つ帳票ステータス＝確認済
                queryBuilder.append(" AND (t.inspectionStatus = :inpectionStatusUnapproved AND t.fileConfirmStatus = :confirmed)");
            }
        }
        
        // 出荷検査結果
        if (StringUtils.isNotBlank(searchCondition.getOutgoingInspectionResult())) {
            queryBuilder.append(" AND t.outgoingInspectionResult = :outgoingInspectionResult");
        }
        // 入荷検査結果
        if (StringUtils.isNotBlank(searchCondition.getIncomingInspectionResult())) {
            queryBuilder.append(" AND t.incomingInspectionResult = :incomingInspectionResult");
        }
        
        if ("1".equals(searchCondition.getFunctionType())) {
            // 検査実施者
            if (StringUtils.isNotBlank(searchCondition.getInspectionPersonName())) {
                queryBuilder.append(" AND t.outgoingInspectionPersonName LIKE :inspectionPersonName");
            }
            // 検査確認者
            if (StringUtils.isNotBlank(searchCondition.getConfirmerName())) {
                queryBuilder.append(" AND t.outgoingConfirmerName LIKE :confirmerName");
            }
            // 検査承認者
            if (StringUtils.isNotBlank(searchCondition.getApprovePersonName())) {
                queryBuilder.append(" AND t.outgoingInspectionApprovePersonName LIKE :approvePersonName");
            }
        } else {
            // 検査実施者
            if (StringUtils.isNotBlank(searchCondition.getInspectionPersonName())) {
                queryBuilder.append(" AND t.incomingInspectionPersonName LIKE :inspectionPersonName");
            }
            // 検査確認者
            if (StringUtils.isNotBlank(searchCondition.getConfirmerName())) {
                queryBuilder.append(" AND t.incomingConfirmerName LIKE :confirmerName");
            }
            // 検査承認者
            if (StringUtils.isNotBlank(searchCondition.getApprovePersonName())) {
                queryBuilder.append(" AND t.incomingInspectionApprovePersonName LIKE :approvePersonName");
            }
        }
        
        // 検査実施日
        if ("4".equals(searchCondition.getFunctionType())) {
            if (searchCondition.getComponentInspectionFrom() != null && searchCondition.getComponentInspectionTo() != null) {
                queryBuilder.append(" AND ((t.outgoingInspectionDate >= :componentInspectionFrom AND t.outgoingInspectionDate <= :componentInspectionTo)");
                queryBuilder.append(" OR (t.incomingInspectionDate >= :componentInspectionFrom AND t.incomingInspectionDate <= :componentInspectionTo))");
            }
            if (searchCondition.getComponentInspectionFrom() != null && searchCondition.getComponentInspectionTo() == null) {
                queryBuilder.append(" AND (t.outgoingInspectionDate >= :componentInspectionFrom");
                queryBuilder.append(" OR t.incomingInspectionDate >= :componentInspectionFrom)");
            }
            if (searchCondition.getComponentInspectionFrom() == null && searchCondition.getComponentInspectionTo() != null) {
                queryBuilder.append(" AND (t.outgoingInspectionDate <= :componentInspectionTo");
                queryBuilder.append(" OR t.incomingInspectionDate <= :componentInspectionTo)");
            }
        } else {
            if (searchCondition.getComponentInspectionFrom() != null && searchCondition.getComponentInspectionTo() != null) {
                if ("1".equals(searchCondition.getFunctionType())) {
                    queryBuilder.append(" AND (t.outgoingInspectionDate >= :componentInspectionFrom AND t.outgoingInspectionDate <= :componentInspectionTo)");
                } else {
                    queryBuilder.append(" AND (t.incomingInspectionDate >= :componentInspectionFrom AND t.incomingInspectionDate <= :componentInspectionTo)");
                }
            }
            if (searchCondition.getComponentInspectionFrom() != null && searchCondition.getComponentInspectionTo() == null) {
                if ("1".equals(searchCondition.getFunctionType())) {
                    queryBuilder.append(" AND (t.outgoingInspectionDate >= :componentInspectionFrom)");
                } else {
                    queryBuilder.append(" AND (t.incomingInspectionDate >= :componentInspectionFrom)");
                }
            }
            if (searchCondition.getComponentInspectionFrom() == null && searchCondition.getComponentInspectionTo() != null) {
                if ("1".equals(searchCondition.getFunctionType())) {
                    queryBuilder.append(" AND (t.outgoingInspectionDate <= :componentInspectionTo)");
                } else {
                    queryBuilder.append(" AND (t.incomingInspectionDate <= :componentInspectionTo)");
                }
            }
        }
        
        // 検査確認日
        if (searchCondition.getCheckImplementDateFrom() != null && searchCondition.getCheckImplementDateTo() != null) {
            if ("1".equals(searchCondition.getFunctionType())) {
                queryBuilder.append(" AND (t.outgoingConfirmDate >= :checkImplementDateFrom AND t.outgoingConfirmDate <= :checkImplementDateTo)");
            } else {
                queryBuilder.append(" AND (t.incomingConfirmDate >= :checkImplementDateFrom AND t.incomingConfirmDate <= :checkImplementDateTo)");
            }
        }
        if (searchCondition.getCheckImplementDateFrom() != null && searchCondition.getCheckImplementDateTo() == null) {
            if ("1".equals(searchCondition.getFunctionType())) {
                queryBuilder.append(" AND (t.outgoingConfirmDate >= :checkImplementDateFrom)");
            } else {
                queryBuilder.append(" AND (t.incomingConfirmDate >= :checkImplementDateFrom)");
            }
        }
        if (searchCondition.getCheckImplementDateFrom() == null && searchCondition.getCheckImplementDateTo() != null) {
            if ("1".equals(searchCondition.getFunctionType())) {
                queryBuilder.append(" AND (t.outgoingConfirmDate <= :checkImplementDateTo)");
            } else {
                queryBuilder.append(" AND (t.incomingConfirmDate <= :checkImplementDateTo)");
            }
        }

        // 検査承認日
        if (searchCondition.getApproveDateFrom() != null && searchCondition.getApproveDateTo() != null) {
            if ("1".equals(searchCondition.getFunctionType())) {
                queryBuilder.append(" AND (t.outgoingInspectionApproveDate >= :approveDateFrom AND t.outgoingInspectionApproveDate <= :approveDateTo)");
            } else {
                queryBuilder.append(" AND (t.incomingInspectionApproveDate >= :approveDateFrom AND t.incomingInspectionApproveDate <= :approveDateTo)");
            }
        }
        if (searchCondition.getApproveDateFrom() != null && searchCondition.getApproveDateTo() == null) {
            if ("1".equals(searchCondition.getFunctionType())) {
                queryBuilder.append(" AND (t.outgoingInspectionApproveDate >= :approveDateFrom)");
            } else {
                queryBuilder.append(" AND (t.incomingInspectionApproveDate >= :approveDateFrom)");
            }
        }
        if (searchCondition.getApproveDateFrom() == null && searchCondition.getApproveDateTo() != null) {
            if ("1".equals(searchCondition.getFunctionType())) {
                queryBuilder.append(" AND (t.outgoingInspectionApproveDate <= :approveDateTo)");
            } else {
                queryBuilder.append(" AND (t.incomingInspectionApproveDate <= :approveDateTo)");
            }
        }
        if (StringUtils.isNotBlank(searchCondition.getMassFlg())) {
            queryBuilder.append(" AND t.inspectPtn IN :massFlgs ");
        }
        
        if(StringUtils.isNotBlank(searchCondition.getFileConfirmStatus())){
            
            String[] fileConfirmStatusList = searchCondition.getFileConfirmStatus().split(",");
            if(fileConfirmStatusList.length>0){
                
                for (int i = 0; i < fileConfirmStatusList.length; i++) {
                    String fileConfirmStatus = fileConfirmStatusList[i];
                    
                    if(i == 0){
                        queryBuilder.append(" AND (");
                    }else {
                        queryBuilder.append(" OR ");
                    }

                    if (null != fileConfirmStatus) {
                        
                        switch (fileConfirmStatus) {

                            case CommonConstants.FILE_STATUS_DEFAULT:
                                queryBuilder.append(" ( t.fileConfirmStatus = "
                                        + "'"+ TblComponentInspectionResult.FileConfirmStatus.DEFAULT + "'"
                                        + " AND ref.mstFileCount <> 0 AND ref.mstFileCount <> ref.fileConfirmedCount ) ");
                                break;
                            case CommonConstants.FILE_STATUS_WAIT:
                                queryBuilder.append(" ( t.fileConfirmStatus = "
                                        + "'"+ TblComponentInspectionResult.FileConfirmStatus.DEFAULT + "'"
                                        + " AND ref.mstFileCount <> 0 AND ref.mstFileCount = ref.fileConfirmedCount ) ");
                                break;
                            case CommonConstants.FILE_STATUS_CONFIRMED:
                                queryBuilder.append(" ( t.fileConfirmStatus = "
                                         + "'"+ TblComponentInspectionResult.FileConfirmStatus.CONFIRMED + "'"
                                        + " ) ");
                                break;
                            case CommonConstants.FILE_STATUS_DENIED:
                                queryBuilder.append(" ( t.fileConfirmStatus = "
                                         + "'"+ TblComponentInspectionResult.FileConfirmStatus.DENIED + "'"
                                        + " ) ");
                                break;
                            default:
                                break;
                        }
                    }
                    
                    if(i == fileConfirmStatusList.length-1){
                        queryBuilder.append(" ) ");
                    }

                }
            
            }

        }

        return queryBuilder.toString();
    }

    /**
     * Set values to search query
     *
     * @param <T>
     * @param typedQuery
     * @param searchCondition
     * @param isCsv
     * @param langId
     */
    private <T> void setValuesToSearchQuery(TypedQuery<T> typedQuery, ComponentInspectionResultSearchCond searchCondition, boolean isCsv, String langId) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        if (!isCsv) {
            typedQuery.setParameter("myCompanyId", searchCondition.getMyCompanyId());
        }
        
        // km-976 帳票確認者の検索を追加 20181121 start
        // 帳票確認者名前
        if (StringUtils.isNotBlank(searchCondition.getFileApproverName())) {
            typedQuery.setParameter("fileApproverName", "%" + searchCondition.getFileApproverName() + "%");
        }
        // km-976 帳票確認者の検索を追加 20181121 end
        
        // 部品
        if (StringUtils.isNotBlank(searchCondition.getComponent())) {
            typedQuery.setParameter("component", "%" + searchCondition.getComponent() + "%");
        }
        // 部品業種
        typedQuery.setParameter("langId", langId);
        if (StringUtils.isNotBlank(searchCondition.getComponentInspectType())) {
            typedQuery.setParameter("componentInspectType", "%" + searchCondition.getComponentInspectType() + "%");
        }
        // PO番号
        if (StringUtils.isNotBlank(searchCondition.getPoNumber())) {
            typedQuery.setParameter("poNumber", searchCondition.getPoNumber().trim() + "%");
        }
        if (StringUtils.isNotBlank(searchCondition.getOutgoingCompanyName())) {
            typedQuery.setParameter("outgoingCompanyName", "%" + searchCondition.getOutgoingCompanyName() + "%");
        }
        if (StringUtils.isNotBlank(searchCondition.getIncomingCompanyName())) {
            typedQuery.setParameter("incomingCompanyName", "%" + searchCondition.getIncomingCompanyName() + "%");
        }
        if (StringUtils.isNotBlank(searchCondition.getInspectionStatus())) {
            typedQuery.setParameter("inspectionStatus", searchCondition.getInspectionStatusList());
        }
        // 帳票ステータス判定条件
        if (StringUtils.isNotBlank(searchCondition.getFileCondition())) {
            typedQuery.setParameter("inpectionStatusUnapproved", CommonConstants.INSPECTION_STATUS_I_UNAPPROVED);
            typedQuery.setParameter("confirmed", TblComponentInspectionResult.FileConfirmStatus.CONFIRMED);
        }
        // 出荷検査結果
        if (StringUtils.isNotBlank(searchCondition.getOutgoingInspectionResult())) {
            typedQuery.setParameter("outgoingInspectionResult", Integer.valueOf(searchCondition.getOutgoingInspectionResult()));
        }
        // 入荷検査結果
        if (StringUtils.isNotBlank(searchCondition.getIncomingInspectionResult())) {
            typedQuery.setParameter("incomingInspectionResult", Integer.valueOf(searchCondition.getIncomingInspectionResult()));
        }
        // 検査実施者
        if (StringUtils.isNotBlank(searchCondition.getInspectionPersonName())) {
            typedQuery.setParameter("inspectionPersonName", "%" + searchCondition.getInspectionPersonName() + "%");
        }
        // 検査確認者
        if (StringUtils.isNotBlank(searchCondition.getConfirmerName())) {
            typedQuery.setParameter("confirmerName", "%" + searchCondition.getConfirmerName() + "%");
        }
        // 検査承認者
        if (StringUtils.isNotBlank(searchCondition.getApprovePersonName())) {
            typedQuery.setParameter("approvePersonName", "%" + searchCondition.getApprovePersonName() + "%");
        }
        // 検査実施日
        if (null != searchCondition.getComponentInspectionFrom()) {
            typedQuery.setParameter("componentInspectionFrom", searchCondition.getComponentInspectionFrom());
        }
        if (null != searchCondition.getComponentInspectionTo()) {
            typedQuery.setParameter("componentInspectionTo", searchCondition.getComponentInspectionTo());
        }
        // 検査確認日
        if (searchCondition.getCheckImplementDateFrom() != null) {
            typedQuery.setParameter("checkImplementDateFrom", searchCondition.getCheckImplementDateFrom());
        }
        if (searchCondition.getCheckImplementDateTo() != null) {
            typedQuery.setParameter("checkImplementDateTo", searchCondition.getCheckImplementDateTo());
        }
        // ロット番号
        if (StringUtils.isNotBlank(searchCondition.getProductionLotNum())) {
            typedQuery.setParameter("productionLotNum", searchCondition.getProductionLotNum().trim() + "%");
        }
        // 検査承認日
        if (searchCondition.getApproveDateFrom() != null) {
            typedQuery.setParameter("approveDateFrom", searchCondition.getApproveDateFrom());
        }
        if (searchCondition.getApproveDateTo() != null) {
            typedQuery.setParameter("approveDateTo", searchCondition.getApproveDateTo());
        }
        if (StringUtils.isNotBlank(searchCondition.getMassFlg())) {
            List<String> massFlgs = Arrays.asList(StringUtils.split(searchCondition.getMassFlg(), ","));
            typedQuery.setParameter("massFlgs", massFlgs);
        }

    }

    /**
     * Convert to ComponentInspectionResultInfo model
     *
     * @param inspectionResult
     * @param langId
     * @param isDetail
     * @return
     */
    private ComponentInspectionResultInfo convertToInspectionResultInfo(TblComponentInspectionResult inspectionResult, String langId, boolean isDetail) {

        ComponentInspectionResultInfo resultInfo = new ComponentInspectionResultInfo();
        if (inspectionResult == null) {
            return resultInfo;
        }
        resultInfo.setComponentInspectionItemsTableId(inspectionResult.getComponentInspectionItemsTableId());
        resultInfo.setComponentId(inspectionResult.getComponentId());
        resultInfo.setComponentInspectionResultId(inspectionResult.getId());
        resultInfo.setComponentCode(inspectionResult.getMstComponent().getComponentCode());
        resultInfo.setComponentName(inspectionResult.getMstComponent().getComponentName());
        resultInfo.setComponentType(inspectionResult.getMstComponent().getComponentType());
        resultInfo.setFirstFlag(inspectionResult.getFirstFlag());
        // マルチCAV型を追加
        resultInfo.setCavityCnt(inspectionResult.getCavityCnt());
        resultInfo.setCavityStartNum(inspectionResult.getCavityStartNum());
        resultInfo.setCavityPrefix(inspectionResult.getCavityPrefix());
        // km-976 帳票確認者の検索を追加 20181121 start
        resultInfo.setFileApproverId(FileUtil.getStringValue(inspectionResult.getFileApproverId()));
        resultInfo.setFileApproverName(FileUtil.getStringValue(inspectionResult.getFileApproverName()));
        // km-976 帳票確認者の検索を追加 20181121 end
        
        // 部品業種を設定
       // MstComponentInspectionItemsTable mstComponentInspectionItemsTable = mstComponentInspectionItemsTableService.getInspectionItemsTableByIdLangId(inspectionResult.getComponentInspectionItemsTableId(), langId);
        if (null != inspectionResult.getMstTable()) {
            resultInfo.setComponentInspectTypeId(inspectionResult.getMstTable().getInspectTypeId());
            if (null != inspectionResult.getMstTable().getMstComponentInspectLang()) {
                resultInfo.setComponentInspectTypeName(inspectionResult.getMstTable().getMstComponentInspectLang().getDictValue());
            }
        }
        String poNumber = "";
        String itemNumber = "";
        if (StringUtils.isNotEmpty(inspectionResult.getProductionLotNum()) && !CommonConstants.PRODUCTION_LOT_NUMBER.equals(inspectionResult.getProductionLotNum())) {
            TblComponentInspectionResultPoList poList = this.getTblPoOutbound(
                    inspectionResult.getComponentId(),
                    inspectionResult.getProductionLotNum(),
                    inspectionResult.getIncomingCompanyId(),
                    inspectionResult.getOutgoingCompanyId());

            if (poList != null && poList.getTblComponentInspectionResultPoVos() != null && poList.getTblComponentInspectionResultPoVos().size() > 0) {
                if (isDetail) {
                    for (TblComponentInspectionResultPoVo poVo : poList.getTblComponentInspectionResultPoVos()) {
                        poNumber += poVo.getOrderNumber().concat("-").concat(poVo.getItemNumber()) + "\n";
                        itemNumber += poVo.getItemNumber() + "\n";
                    }
                } else {
                    for (TblComponentInspectionResultPoVo poVo : poList.getTblComponentInspectionResultPoVos()) {
                        poNumber += poVo.getOrderNumber().concat("-").concat(poVo.getItemNumber()) + "/";
                    }
                    if (StringUtils.isNotEmpty(poNumber)) {
                        poNumber = poNumber.substring(0, poNumber.length() - 1);
                    }
                }
            }
            resultInfo.setProductionLotNum(inspectionResult.getProductionLotNum());
        } else {
            resultInfo.setProductionLotNum(inspectionResult.getProductionLotNum());
        }
        resultInfo.setPoNumber(poNumber);
        resultInfo.setItemNumber(itemNumber);
        
        int inspectionFileCount = 0;
        int mstFileCount = 0;
        int fileInputCount = 0;

        //部品検査別参照ファイル
        ComponentInspectionReferenceFile refFile
                = tblComponentInspectionReferenceFileService.getReferenceFile(inspectionResult.getId(), inspectionResult.getComponentId());

        resultInfo.setComponentInspectionReferenceFile(refFile);
        
        if(null != refFile){
            
            if("CONFIRMED".equals(refFile.getDrawingFileStatus()) || "SKIP".equals(refFile.getDrawingFileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getProofFileStatus()) || "SKIP".equals(refFile.getProofFileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getRohsProofFileStatus()) || "SKIP".equals(refFile.getRohsProofFileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getPackageSpecFileStatus()) || "SKIP".equals(refFile.getPackageSpecFileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getQcPhaseFileStatus()) || "SKIP".equals(refFile.getQcPhaseFileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile06FileStatus()) || "SKIP".equals(refFile.getFile06FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile07FileStatus()) || "SKIP".equals(refFile.getFile07FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile08FileStatus()) || "SKIP".equals(refFile.getFile08FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile09FileStatus()) || "SKIP".equals(refFile.getFile09FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile10FileStatus()) || "SKIP".equals(refFile.getFile10FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile11FileStatus()) || "SKIP".equals(refFile.getFile11FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile12FileStatus()) || "SKIP".equals(refFile.getFile12FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile13FileStatus()) || "SKIP".equals(refFile.getFile13FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile14FileStatus()) || "SKIP".equals(refFile.getFile14FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile15FileStatus()) || "SKIP".equals(refFile.getFile15FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile16FileStatus()) || "SKIP".equals(refFile.getFile16FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile17FileStatus()) || "SKIP".equals(refFile.getFile17FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile18FileStatus()) || "SKIP".equals(refFile.getFile18FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile19FileStatus()) || "SKIP".equals(refFile.getFile19FileStatus())){
                inspectionFileCount++;
            }
            
            if("CONFIRMED".equals(refFile.getFile20FileStatus()) || "SKIP".equals(refFile.getFile20FileStatus())){
                inspectionFileCount++;
            }
        }

        List<ComponentInspectionItemsFileDetail> componentInspectionItemsFileDetails = this.getInspectionResultFile(inspectionResult.getId());
        resultInfo.setComponentInspectionItemsFileDetails(componentInspectionItemsFileDetails);

        MstComponentInspectionItemsTableClassVo mstComponentInspectionItemsTableClassVo = new MstComponentInspectionItemsTableClassVo();

        TblComponentInspectionReferenceFile tblReferenceFile = this.entityManager
            .createNamedQuery("TblComponentInspectionReferenceFile.findByComponentInspectionResultId", TblComponentInspectionReferenceFile.class)
            .setParameter("componentInspectionResultId", inspectionResult.getId())
            .getResultList().stream().findFirst().orElse(null);
        
        MstComponentInspectClass inspClass = getInspClass(inspectionResult.getFirstFlag(), inspectionResult.getIncomingCompanyId()).orElse(null);
        if(tblReferenceFile != null && inspClass != null) {
            Character charFlag = CommonConstants.INSPECTION_CHAR_MASS_FLAG;
            if ((tblReferenceFile.getDrawingFileFlg().equals(charFlag) && StringUtils.isEmpty(refFile.getDrawingFileUuid()))
                    || (tblReferenceFile.getProofFileFlg().equals(charFlag) && StringUtils.isEmpty(refFile.getProofFileUuid()))
                    || (tblReferenceFile.getRohsProofFileFlg().equals(charFlag) && StringUtils.isEmpty(refFile.getRohsProofFileUuid()))
                    || (tblReferenceFile.getPackageSpecFileFlg().equals(charFlag) && StringUtils.isEmpty(refFile.getPackageSpecFileUuid()))
                    || (tblReferenceFile.getQcPhaseFileFlg().equals(charFlag) && StringUtils.isEmpty(refFile.getQcPhaseFileUuid()))) {
                resultInfo.setFileNotEnough(mstDictionaryService.getDictionaryValue(langId, "file_not_enough"));
            } else {
                resultInfo.setFileNotEnough("");
            }

            resultInfo.setFirstFlagName(mstComponentInspectFileService.getInspDictVal(langId, inspClass.getDictKey()));
            resultInfo.setMassFlg(Integer.parseInt(inspClass.getMassFlg().toString()));

            if (tblReferenceFile.getDrawingFileFlg().equals('0') || tblReferenceFile.getDrawingFileFlg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setDrawingFlg(tblReferenceFile.getDrawingFileFlg());
            }
            if (tblReferenceFile.getProofFileFlg().equals('0') || tblReferenceFile.getProofFileFlg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setProofFlg(tblReferenceFile.getProofFileFlg());
            }
            if (tblReferenceFile.getRohsProofFileFlg().equals('0') || tblReferenceFile.getRohsProofFileFlg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setRohsProofFlg(tblReferenceFile.getRohsProofFileFlg());
            }
            if (tblReferenceFile.getPackageSpecFileFlg().equals('0') || tblReferenceFile.getPackageSpecFileFlg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setPackageSpecFlg(tblReferenceFile.getPackageSpecFileFlg());
            }
            if (tblReferenceFile.getQcPhaseFileFlg().equals('0') || tblReferenceFile.getQcPhaseFileFlg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setQcPhaseFlg(tblReferenceFile.getQcPhaseFileFlg());
            }
            if (tblReferenceFile.getFile06Flg().equals('0') || tblReferenceFile.getFile06Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile06Flg(tblReferenceFile.getFile06Flg());
            }
            if (tblReferenceFile.getFile07Flg().equals('0') || tblReferenceFile.getFile07Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile07Flg(tblReferenceFile.getFile07Flg());
            }
            if (tblReferenceFile.getFile08Flg().equals('0') || tblReferenceFile.getFile08Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile08Flg(tblReferenceFile.getFile08Flg());
            }
            if (tblReferenceFile.getFile08Flg().equals('0') || tblReferenceFile.getFile08Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile08Flg(tblReferenceFile.getFile08Flg());
            }
            if (tblReferenceFile.getFile09Flg().equals('0') || tblReferenceFile.getFile09Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile09Flg(tblReferenceFile.getFile09Flg());
            }
            if (tblReferenceFile.getFile10Flg().equals('0') || tblReferenceFile.getFile10Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile10Flg(tblReferenceFile.getFile10Flg());
            }
            if (tblReferenceFile.getFile11Flg().equals('0') || tblReferenceFile.getFile11Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile11Flg(tblReferenceFile.getFile11Flg());
            }
            if (tblReferenceFile.getFile12Flg().equals('0') || tblReferenceFile.getFile12Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile12Flg(tblReferenceFile.getFile12Flg());
            }
            if (tblReferenceFile.getFile13Flg().equals('0') || tblReferenceFile.getFile13Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile13Flg(tblReferenceFile.getFile13Flg());
            }
            if (tblReferenceFile.getFile14Flg().equals('0') || tblReferenceFile.getFile14Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile14Flg(tblReferenceFile.getFile14Flg());
            }
            if (tblReferenceFile.getFile15Flg().equals('0') || tblReferenceFile.getFile15Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile15Flg(tblReferenceFile.getFile15Flg());
            }
            if (tblReferenceFile.getFile16Flg().equals('0') || tblReferenceFile.getFile16Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile16Flg(tblReferenceFile.getFile16Flg());
            }
            if (tblReferenceFile.getFile17Flg().equals('0') || tblReferenceFile.getFile17Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile17Flg(tblReferenceFile.getFile17Flg());
            }
            if (tblReferenceFile.getFile18Flg().equals('0') || tblReferenceFile.getFile18Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile18Flg(tblReferenceFile.getFile18Flg());
            }
            if (tblReferenceFile.getFile19Flg().equals('0') || tblReferenceFile.getFile19Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile19Flg(tblReferenceFile.getFile19Flg());
            }
            if (tblReferenceFile.getFile20Flg().equals('0') || tblReferenceFile.getFile20Flg().equals('1')) {
                mstComponentInspectionItemsTableClassVo.setFile20Flg(tblReferenceFile.getFile20Flg());
            }

            if (tblReferenceFile.getDrawingFileFlg().equals('1')) {
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getDrawingFileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getProofFileFlg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getProofFileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getRohsProofFileFlg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getRohsProofFileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getPackageSpecFileFlg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getPackageSpecFileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getQcPhaseFileFlg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getQcPhaseFileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile06Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile06FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile07Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile07FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile08Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile08FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile09Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile09FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile10Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile10FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile11Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile11FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile12Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile12FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile13Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile13FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile14Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile14FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile15Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile15FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile16Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile16FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile17Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile17FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile18Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile18FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile19Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile19FileUuid())) {
                    fileInputCount++;
                }
            }
            if(tblReferenceFile.getFile20Flg().equals('1')){
                mstFileCount++;
                if (null != refFile && StringUtils.isNotBlank(refFile.getFile20FileUuid())) {
                    fileInputCount++;
                }
            }

            mstComponentInspectionItemsTableClassVo.setDrawingName(tblReferenceFile.getDrawingFileName());
            mstComponentInspectionItemsTableClassVo.setProofName(tblReferenceFile.getProofFileName());
            mstComponentInspectionItemsTableClassVo.setRohsProofName(tblReferenceFile.getRohsProofFileName());
            mstComponentInspectionItemsTableClassVo.setPackageSpecName(tblReferenceFile.getPackageSpecFileName());
            mstComponentInspectionItemsTableClassVo.setQcPhaseName(tblReferenceFile.getQcPhaseFileName());
            mstComponentInspectionItemsTableClassVo.setFile06Name(tblReferenceFile.getFile06Name());
            mstComponentInspectionItemsTableClassVo.setFile07Name(tblReferenceFile.getFile07Name());
            mstComponentInspectionItemsTableClassVo.setFile08Name(tblReferenceFile.getFile08Name());
            mstComponentInspectionItemsTableClassVo.setFile09Name(tblReferenceFile.getFile09Name());
            mstComponentInspectionItemsTableClassVo.setFile10Name(tblReferenceFile.getFile10Name());
            mstComponentInspectionItemsTableClassVo.setFile11Name(tblReferenceFile.getFile11Name());
            mstComponentInspectionItemsTableClassVo.setFile12Name(tblReferenceFile.getFile12Name());
            mstComponentInspectionItemsTableClassVo.setFile13Name(tblReferenceFile.getFile13Name());
            mstComponentInspectionItemsTableClassVo.setFile14Name(tblReferenceFile.getFile14Name());
            mstComponentInspectionItemsTableClassVo.setFile15Name(tblReferenceFile.getFile15Name());
            mstComponentInspectionItemsTableClassVo.setFile16Name(tblReferenceFile.getFile16Name());
            mstComponentInspectionItemsTableClassVo.setFile17Name(tblReferenceFile.getFile17Name());
            mstComponentInspectionItemsTableClassVo.setFile18Name(tblReferenceFile.getFile18Name());
            mstComponentInspectionItemsTableClassVo.setFile19Name(tblReferenceFile.getFile19Name());
            mstComponentInspectionItemsTableClassVo.setFile20Name(tblReferenceFile.getFile20Name());
            
            mstComponentInspectionItemsTableClassVo.setMassFlg(inspClass.getMassFlg());
        } else {
            resultInfo.setFileNotEnough("");
            resultInfo.setFirstFlagName("");
            String flag = "0";
            mstComponentInspectionItemsTableClassVo.setDrawingFlg(flag.charAt(0));
            mstComponentInspectionItemsTableClassVo.setProofFlg(flag.charAt(0));
            mstComponentInspectionItemsTableClassVo.setRohsProofFlg(flag.charAt(0));
            mstComponentInspectionItemsTableClassVo.setPackageSpecFlg(flag.charAt(0));
            mstComponentInspectionItemsTableClassVo.setQcPhaseFlg(flag.charAt(0));
        }
        resultInfo.setMstComponentInspectionItemsTableClassVo(mstComponentInspectionItemsTableClassVo);
        resultInfo.setQuantity(inspectionResult.getQuantity());
        resultInfo.setIncomingCompanyName(inspectionResult.getMstCompanyIncoming().getCompanyName());
        resultInfo.setOutgoingCompanyName(inspectionResult.getMstCompanyOutgoing().getCompanyName());
        resultInfo.setOutgoingCompanyId(inspectionResult.getOutgoingCompanyId());//Apeng 20171102 add
        resultInfo.setIncomingCompanyId(inspectionResult.getIncomingCompanyId());//Apeng 20171102 add
        if (StringUtils.isNotEmpty(inspectionResult.getInspBatchUpdateStatus())) {
            resultInfo.setInspBatchUpdateStatus(inspectionResult.getInspBatchUpdateStatus());//Apeng 20171113 add
        } else {
            resultInfo.setInspBatchUpdateStatus("");//Apeng 20171113 add
        }
        resultInfo.setInspectionStatus(inspectionResult.getInspectionStatus());
        // outgoing
        resultInfo.setOutgoingMeasSamplingQuantity(inspectionResult.getOutgoingMeasSamplingQuantity());
        resultInfo.setOutgoingVisualSamplingQuantity(inspectionResult.getOutgoingVisualSamplingQuantity());
        MstComponentInspectionItemsTable itemsTbl = mstComponentInspectionItemsTableService.findById(inspectionResult.getComponentInspectionItemsTableId());
        resultInfo.setMeasSamplingRatio(itemsTbl.getMeasSampleRatio());
        resultInfo.setVisSamplingRatio(itemsTbl.getVisSampleRatio());
        if (StringUtils.isNotEmpty(inspectionResult.getOutgoingInspectionPersonName())) {
            resultInfo.setOutgoingInspectionPersonName(inspectionResult.getOutgoingInspectionPersonName());
        } else {
            resultInfo.setOutgoingInspectionPersonName("");
        }
        if (inspectionResult.getOutgoingInspectionDate() != null) {
            resultInfo.setOutgoingInspectionDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(inspectionResult.getOutgoingInspectionDate()));
        } else {
            resultInfo.setOutgoingInspectionDate("");
        }
        resultInfo.setOutgoingInspectionResult(inspectionResult.getOutgoingInspectionResult());
        if (StringUtils.isNotEmpty(inspectionResult.getOutgoingInspectionApprovePersonName())) {
            resultInfo.setOutgoingInspectionApprovePersonName(inspectionResult.getOutgoingInspectionApprovePersonName());
        } else {
            resultInfo.setOutgoingInspectionApprovePersonName("");
        }
        if (inspectionResult.getOutgoingInspectionApproveDate() != null) {
            resultInfo.setOutgoingInspectionApproveDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(inspectionResult.getOutgoingInspectionApproveDate()));
        } else {
            resultInfo.setOutgoingInspectionApproveDate("");
        }
        if (StringUtils.isNotEmpty(inspectionResult.getOutgoingInspectionComment())) {
            resultInfo.setOutgoingInspectionComment(inspectionResult.getOutgoingInspectionComment());
        } else {
            resultInfo.setOutgoingInspectionComment("");
        }
        //-------------------Apeng 20171010 add start---------------------------
        resultInfo.setOutgoingConfirmerUuid(inspectionResult.getOutgoingConfirmerUuid());
        if (StringUtils.isNotEmpty(inspectionResult.getOutgoingConfirmerName())) {
            resultInfo.setOutgoingConfirmerName(inspectionResult.getOutgoingConfirmerName());
        } else {
            resultInfo.setOutgoingConfirmerName("");
        }
        if (StringUtils.isNotEmpty(inspectionResult.getOutgoingPrivateComment())) {
            resultInfo.setOutgoingPrivateComment(inspectionResult.getOutgoingPrivateComment());
        } else {
            resultInfo.setOutgoingPrivateComment("");
        }
        if (inspectionResult.getOutgoingConfirmDate() != null) {
            resultInfo.setOutgoingConfirmDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(inspectionResult.getOutgoingConfirmDate()));
        } else {
            resultInfo.setOutgoingConfirmDate("");
        }
        //-------------------Apeng 20171010 add end---------------------------
        // acceptance
        if (StringUtils.isNotEmpty(inspectionResult.getAcceptancePersonName())) {
            resultInfo.setAcceptancePersonName(inspectionResult.getAcceptancePersonName());
        } else {
            resultInfo.setAcceptancePersonName("");
        }
        if (inspectionResult.getAcceptanceDate() != null) {
            resultInfo.setAcceptanceDate(FileUtil.dateFormat(inspectionResult.getAcceptanceDate()));
        } else {
            resultInfo.setAcceptanceDate("");
        }
        if (StringUtils.isNotEmpty(inspectionResult.getExemptionApprovePersonName())) {
            resultInfo.setExemptionApprovePersonName(inspectionResult.getExemptionApprovePersonName());
        } else {
            resultInfo.setExemptionApprovePersonName("");
        }
        if (inspectionResult.getExemptionApproveDate() != null) {
            resultInfo.setExemptionApproveDate(FileUtil.dateFormat(inspectionResult.getExemptionApproveDate()));
        } else {
            resultInfo.setExemptionApproveDate("");
        }
        // incoming
        resultInfo.setIncomingMeasSamplingQuantity(inspectionResult.getIncomingMeasSamplingQuantity());
        resultInfo.setIncomingVisualSamplingQuantity(inspectionResult.getIncomingVisualSamplingQuantity());
        if (StringUtils.isNotEmpty(inspectionResult.getIncomingInspectionPersonName())) {
            resultInfo.setIncomingInspectionPersonName(inspectionResult.getIncomingInspectionPersonName());
        } else {
            resultInfo.setIncomingInspectionPersonName("");
        }
        if (inspectionResult.getIncomingInspectionDate() != null) {
            resultInfo.setIncomingInspectionDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(inspectionResult.getIncomingInspectionDate()));
        } else {
            resultInfo.setIncomingInspectionDate("");
        }
        resultInfo.setIncomingInspectionResult(inspectionResult.getIncomingInspectionResult());
        if (StringUtils.isNotEmpty(inspectionResult.getIncomingInspectionApprovePersonName())) {
            resultInfo.setIncomingInspectionApprovePersonName(inspectionResult.getIncomingInspectionApprovePersonName());
        } else {
            resultInfo.setIncomingInspectionApprovePersonName("");
        }
        if (inspectionResult.getIncomingInspectionApproveDate() != null) {
            resultInfo.setIncomingInspectionApproveDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(inspectionResult.getIncomingInspectionApproveDate()));
        } else {
            resultInfo.setIncomingInspectionApproveDate("");
        }
        if (StringUtils.isNotEmpty(inspectionResult.getIncomingInspectionComment())) {
            resultInfo.setIncomingInspectionComment(inspectionResult.getIncomingInspectionComment());
        } else {
            resultInfo.setIncomingInspectionComment("");
        }
        //-------------------Apeng 20171010 add start---------------------------
        resultInfo.setIncomingConfirmerUuid(inspectionResult.getIncomingConfirmerUuid());
        if (StringUtils.isNotEmpty(inspectionResult.getIncomingConfirmerName())) {
            resultInfo.setIncomingConfirmerName(inspectionResult.getIncomingConfirmerName());
        } else {
            resultInfo.setIncomingConfirmerName("");
        }
        if (StringUtils.isNotEmpty(inspectionResult.getIncomingPrivateComment())) {
            resultInfo.setIncomingPrivateComment(inspectionResult.getIncomingPrivateComment());
        } else {
            resultInfo.setIncomingPrivateComment("");
        }
        if (inspectionResult.getIncomingConfirmDate() != null) {
            resultInfo.setIncomingConfirmDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(inspectionResult.getIncomingConfirmDate()));
        } else {
            resultInfo.setIncomingConfirmDate("");
        }
        //-------------------Apeng 20171010 add end---------------------------
        resultInfo.setDataRelationTgt(inspectionResult.getDataRelationTgt().toString());

        resultInfo.setFileConfirmStatus(inspectionResult.getFileConfirmStatus());
        

         if (mstFileCount == 0) {
            resultInfo.setFileInputStatusDisplay("");
        } else {
            StringBuilder fileInputCountStr = new StringBuilder();
            fileInputCountStr.append(fileInputCount).append("/").append(mstFileCount);
            resultInfo.setFileInputStatusDisplay(fileInputCountStr.toString());
        }

        if (null != inspectionResult.getFileConfirmStatus()) {

            switch (inspectionResult.getFileConfirmStatus()) {

                case CommonConstants.FILE_STATUS_DEFAULT:
                    if (mstFileCount == 0) {
                        resultInfo.setFileConfirmStatusDisplay("");
                    } else if (inspectionFileCount == mstFileCount) {
                        resultInfo.setFileConfirmStatusDisplay(CommonConstants.FILE_STATUS_WAIT);
                    } else {
                        StringBuilder confirmStatusStr = new StringBuilder();
                        confirmStatusStr.append(inspectionFileCount).append("/").append(mstFileCount);
                        resultInfo.setFileConfirmStatusDisplay(confirmStatusStr.toString());
                    }
                    break;
                case CommonConstants.FILE_STATUS_CONFIRMED:
                    resultInfo.setFileConfirmStatusDisplay(CommonConstants.FILE_STATUS_CONFIRMED);
                    break;
                case CommonConstants.FILE_STATUS_DENIED:
                    resultInfo.setFileConfirmStatusDisplay(CommonConstants.FILE_STATUS_DENIED);
                    break;
                default:
                    break;
            }
        }

        resultInfo.setOutgoingApproveResult(inspectionResult.getOutgoingApproveResult().name());
        resultInfo.setOutgoingConfirmResult(inspectionResult.getOutgoingConfirmResult().name());
        resultInfo.setIncomingApproveResult(inspectionResult.getIncomingApproveResult().name());
        resultInfo.setIncomingConfirmResult(inspectionResult.getIncomingConfirmResult().name());

        resultInfo.setMaterial01(inspectionResult.getMaterial01());
        resultInfo.setMaterial02(inspectionResult.getMaterial02());
        resultInfo.setMaterial03(inspectionResult.getMaterial03());

        resultInfo.setCreateDate(inspectionResult.getCreateDate());
        resultInfo.setUpdateDate(inspectionResult.getUpdateDate());
        resultInfo.setAbortFlg(inspectionResult.getAbortFlg());

        return resultInfo;
    }
    
    public Optional<MstComponentInspectClass> getInspClass(String classid, String incomingCompanyId) {
        Optional<MstComponentInspectClass> inspClassOpt = mstComponentInspectFileService.findClassByPK(classid, incomingCompanyId);
        if(inspClassOpt.isPresent()) {
            return inspClassOpt;
        }
        return mstComponentInspectFileService.findClassByPK(classid, "SELF");
    }

    /**
     * Convert to Map from list.
     *
     * @param tblResultDetails
     * @param inspectionItemsTableDetailMap
     * @return
     */
    private Map<String, List<TblComponentInspectionResultDetail>> convertToTblResultMap(
            List<TblComponentInspectionResultDetail> tblResultDetails, Map<String, MstComponentInspectionItemsTableDetail> inspectionItemsTableDetailMap) {

        Map<String, List<TblComponentInspectionResultDetail>> tblResultDetailMap = new LinkedHashMap<>();
        if (tblResultDetails == null) {
            return tblResultDetailMap;
        }

        String mapKey;
        List<TblComponentInspectionResultDetail> tblResultList;
        for (TblComponentInspectionResultDetail tblResultDetail : tblResultDetails) {
            MstComponentInspectionItemsTableDetail mstDetail = inspectionItemsTableDetailMap.get(tblResultDetail.getInspectionItemSno());
            mapKey = tblResultDetail.getInspectionType() + SPLIT_REGEX
                    + mstDetail.getMeasurementType() + SPLIT_REGEX + tblResultDetail.getInspectionItemSno();
            if (tblResultDetailMap.containsKey(mapKey)) {
                tblResultList = tblResultDetailMap.get(mapKey);
                tblResultList.add(tblResultDetail);
            } else {
                tblResultList = new ArrayList<>();
                tblResultList.add(tblResultDetail);
                tblResultDetailMap.put(mapKey, tblResultList);
            }
        }
        return tblResultDetailMap;
    }

    private void setInspectionResultDetailList(ComponentInspectionResultInfo inspectionResultInfo,
                                               Map<String, List<TblComponentInspectionResultDetail>> tableResultDetailMap, Map<String, MstComponentInspectionItemsTableDetail> inspectionItemsTableDetailMap) {

        List<ComponentInspectionItemResultDetail> outgoingMeasureList = new ArrayList<>();
        List<ComponentInspectionItemResultDetail> outgoingVisualList = new ArrayList<>();
        List<ComponentInspectionItemResultDetail> incomingMeasureList = new ArrayList<>();
        List<ComponentInspectionItemResultDetail> incomingVisualList = new ArrayList<>();
        for (Entry<String, List<TblComponentInspectionResultDetail>> entry : tableResultDetailMap.entrySet()) {
            String mapKey = entry.getKey();
            List<TblComponentInspectionResultDetail> tableResultDetailList = entry.getValue();

            String[] keyArray = mapKey.split(SPLIT_REGEX);
            int inspectionType = Integer.valueOf(keyArray[0]);

            ComponentInspectionItemResultDetail oneInspectionItemResult = new ComponentInspectionItemResultDetail();
            List<SamplingInspectionResult> samplingResultList = new ArrayList<>();
            List<SamplingInspectionOutgoingResult> samplingOutgoingResultList = new ArrayList<>();
            List<SeqInspectionOutgoingResult> seqInspectionOutgoingResultList = new ArrayList<>();
            tableResultDetailList.stream().forEach(tblData -> {
                MstComponentInspectionItemsTableDetail mstDetail = inspectionItemsTableDetailMap.get(tblData.getInspectionItemSno());
                if (tblData.getSeq() == 0) {
                    List<TblComponentInspectionResultDetail> outgoingDetailList = new ArrayList<>();
                    if (CommonConstants.INSPECTION_TYPE_INCOMING == inspectionType) {
                        outgoingDetailList = tableResultDetailMap.getOrDefault(CommonConstants.INSPECTION_TYPE_OUTGOING + SPLIT_REGEX + mstDetail.getMeasurementType() + SPLIT_REGEX + tblData.getInspectionItemSno(), new ArrayList<>());
                    }
                    oneInspectionItemResult.setInspectionResultDetailId(tblData.getId());
                    oneInspectionItemResult.setInspectionItemSno(tblData.getInspectionItemSno());
                    oneInspectionItemResult.setInspectionType(tblData.getInspectionType());
                    oneInspectionItemResult.setNote(tblData.getNote());
                    inspectionResultInfo.setCavityNum(tblData.getCavityNum());
                    // 追加列
                    oneInspectionItemResult.setRevisionSymbol(mstDetail.getRevisionSymbol());
                    oneInspectionItemResult.setDrawingPage(mstDetail.getDrawingPage());
                    oneInspectionItemResult.setDrawingAnnotation(mstDetail.getDrawingAnnotation());
                    oneInspectionItemResult.setDrawingMentionNo(mstDetail.getDrawingMentionNo());
                    oneInspectionItemResult.setSimilarMultiitem(mstDetail.getSimilarMultiitem());
                    oneInspectionItemResult.setDrawingArea(mstDetail.getDrawingArea());
                    oneInspectionItemResult.setPqs(mstDetail.getPqs());

                    oneInspectionItemResult.setInspectionItemName(mstDetail.getInspectionItemName());
                    oneInspectionItemResult.setMeasurementType(mstDetail.getMeasurementType());
                    oneInspectionItemResult.setMeasurementMethod(mstDetail.getMeasurementMethod());
                    oneInspectionItemResult.setDimensionValue(mstDetail.getDimensionValue());
                    oneInspectionItemResult.setTolerancePlus(mstDetail.getTolerancePlus());
                    oneInspectionItemResult.setToleranceMinus(mstDetail.getToleranceMinus());
                    oneInspectionItemResult.setItemResult(tblData.getItemResult());
                    oneInspectionItemResult.setCavityNum(tblData.getCavityNum());
                    oneInspectionItemResult.setEnableThAlert(mstDetail.isEnableThAlert());

                    oneInspectionItemResult.setLocalSeq(mstDetail.getLocalSeq());
                    oneInspectionItemResult.setItemResultAuto(tblData.getItemResultAuto());
                    oneInspectionItemResult.setItemResultManual(tblData.getItemResultManual());//手動判定
                    oneInspectionItemResult.setManJudgeComment(tblData.getManJudgeComment());//手動判定コメント
                    if (outgoingDetailList != null && outgoingDetailList.size() > 0) {
                        SeqInspectionOutgoingResult seqInspectionOutgoingResult = new SeqInspectionOutgoingResult();
                        for (TblComponentInspectionResultDetail outgoingDetail : outgoingDetailList) {
                            if (outgoingDetail.getSeq() != 0) {
                                SamplingInspectionOutgoingResult samplingResultOutgoing = new SamplingInspectionOutgoingResult();
                                samplingResultOutgoing.setSeq(outgoingDetail.getSeq());
                                samplingResultOutgoing.setSeqMeasurementResultOutgoing(outgoingDetail.getSeqMeasurementResult());
                                samplingResultOutgoing.setSeqVisualResultOutgoing(outgoingDetail.getSeqVisualResult());
                                samplingOutgoingResultList.add(samplingResultOutgoing);
                            } else {
                                oneInspectionItemResult.setItemResultOutgoing(outgoingDetail.getItemResult());
                                seqInspectionOutgoingResult.setInspectionResultDetailId(outgoingDetail.getId());
                                seqInspectionOutgoingResult.setInspectionItemSno(outgoingDetail.getInspectionItemSno());
                                seqInspectionOutgoingResult.setInspectionType(outgoingDetail.getInspectionType());
                                seqInspectionOutgoingResultList.add(seqInspectionOutgoingResult);
                            }
                        }
                    }
                    //寸法ID Apeng 20171220 add
                    String itemtableDetailMethodId = (mstDetail.getDrawingPage() == null ? "" : mstDetail.getDrawingPage())
                            .concat(mstDetail.getDrawingAnnotation() == null ? "" : mstDetail.getDrawingAnnotation())
                            .concat(mstDetail.getDrawingMentionNo() == null ? "" : mstDetail.getDrawingMentionNo())
                            .concat(mstDetail.getSimilarMultiitem() == null ? "" : mstDetail.getSimilarMultiitem());
                    oneInspectionItemResult.setItemtableDetailMethodId(itemtableDetailMethodId);

                    //記号 bacpd KM816
                    String dimensionCode = (mstDetail.getDrawingPage() == null ? "" : mstDetail.getDrawingPage())
                            .concat(mstDetail.getDrawingAnnotation() == null ? "" : mstDetail.getDrawingAnnotation());
                    oneInspectionItemResult.setDimensionCode(dimensionCode);

                    //No. bacpd Km816
                    String dimensionNo = mstDetail.getDrawingMentionNo() == null ? "" : mstDetail.getDrawingMentionNo()
                            .concat(mstDetail.getSimilarMultiitem() == null ? "" : mstDetail.getSimilarMultiitem());
                    oneInspectionItemResult.setDimensionNo(dimensionNo);

                    //Apeng 20180208 add start
                    oneInspectionItemResult.setItemTableDetailAdditionalFlg(mstDetail.getAdditionalFlg().toString());
                    //Apeng 20180208 add end

                } else {
                    SamplingInspectionResult samplingResult = new SamplingInspectionResult();
                    samplingResult.setSeq(tblData.getSeq());
                    samplingResult.setCavityNum(tblData.getCavityNum());
                    samplingResult.setSeqMeasurementResult(tblData.getSeqMeasurementResult());
                    samplingResult.setSeqVisualResult(tblData.getSeqVisualResult());
                    samplingResultList.add(samplingResult);
                }
            });
            oneInspectionItemResult.setSamplingInspectionResults(samplingResultList);
            oneInspectionItemResult.setSamplingInspectionOutgoingResults(samplingOutgoingResultList);
            oneInspectionItemResult.setSeqInspectionOutgoingResults(seqInspectionOutgoingResultList);

            if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType && !oneInspectionItemResult.isVisualSpection()) {
                outgoingMeasureList.add(oneInspectionItemResult);
            } else if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType && oneInspectionItemResult.isVisualSpection()) {
                outgoingVisualList.add(oneInspectionItemResult);
            } else if (CommonConstants.INSPECTION_TYPE_INCOMING == inspectionType && !oneInspectionItemResult.isVisualSpection()) {
                incomingMeasureList.add(oneInspectionItemResult);
            } else {
                incomingVisualList.add(oneInspectionItemResult);
            }
        }

        // set result detials list
        if (!outgoingMeasureList.isEmpty()) {
            //昇順
            if (outgoingMeasureList.size() > 1) {
                Collections.sort(outgoingMeasureList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        ComponentInspectionItemResultDetail vo1 = (ComponentInspectionItemResultDetail) o1;
                        ComponentInspectionItemResultDetail vo2 = (ComponentInspectionItemResultDetail) o2;
                        //検査項目番号
                        String inspectionItemSno1 = vo1.getInspectionItemSno();
                        String inspectionItemSno2 = vo2.getInspectionItemSno();
                        if (vo1.getLocalSeq() == 0) {
                            return inspectionItemSno1.compareTo(inspectionItemSno2);
                        } else {
                            return vo1.getLocalSeq().compareTo(vo2.getLocalSeq());
                        }
                    }
                });
            }
            inspectionResultInfo.setOutgoingMeasureResultDetails(outgoingMeasureList);
        }
        if (!outgoingVisualList.isEmpty()) {
            //昇順
            if (outgoingVisualList.size() > 1) {
                Collections.sort(outgoingVisualList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        ComponentInspectionItemResultDetail vo1 = (ComponentInspectionItemResultDetail) o1;
                        ComponentInspectionItemResultDetail vo2 = (ComponentInspectionItemResultDetail) o2;
                        //検査項目番号
                        String inspectionItemSno1 = vo1.getInspectionItemSno();
                        String inspectionItemSno2 = vo2.getInspectionItemSno();
                        if (vo1.getLocalSeq() == 0) {
                            return inspectionItemSno1.compareTo(inspectionItemSno2);
                        } else {
                            return vo1.getLocalSeq().compareTo(vo2.getLocalSeq());
                        }
                    }
                });
            }
            inspectionResultInfo.setOutgoingVisualResultDetails(outgoingVisualList);
        }
        if (!incomingMeasureList.isEmpty()) {
            //昇順
            if (incomingMeasureList.size() > 1) {
                Collections.sort(incomingMeasureList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        ComponentInspectionItemResultDetail vo1 = (ComponentInspectionItemResultDetail) o1;
                        ComponentInspectionItemResultDetail vo2 = (ComponentInspectionItemResultDetail) o2;
                        //検査項目番号
                        String inspectionItemSno1 = vo1.getInspectionItemSno();
                        String inspectionItemSno2 = vo2.getInspectionItemSno();
                        if (vo1.getLocalSeq() == 0) {
                            return inspectionItemSno1.compareTo(inspectionItemSno2);
                        } else {
                            return vo1.getLocalSeq().compareTo(vo2.getLocalSeq());
                        }
                    }
                });
            }
            inspectionResultInfo.setIncomingMeasureResultDetails(incomingMeasureList);
        }
        if (!incomingVisualList.isEmpty()) {
            //昇順
            if (incomingVisualList.size() > 1) {
                Collections.sort(incomingVisualList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        ComponentInspectionItemResultDetail vo1 = (ComponentInspectionItemResultDetail) o1;
                        ComponentInspectionItemResultDetail vo2 = (ComponentInspectionItemResultDetail) o2;
                        //検査項目番号
                        String inspectionItemSno1 = vo1.getInspectionItemSno();
                        String inspectionItemSno2 = vo2.getInspectionItemSno();
                        if (vo1.getLocalSeq() == 0) {
                            return inspectionItemSno1.compareTo(inspectionItemSno2);
                        } else {
                            return vo1.getLocalSeq().compareTo(vo2.getLocalSeq());
                        }
                    }
                });
            }
            inspectionResultInfo.setIncomingVisualResultDetails(incomingVisualList);
        }
    }

    /**
     * Create empty TblComponentInspectionResultDetail
     *
     * @param componentInspectionResultId
     * @param inputInfo
     * @param userUuid
     */
    @Transactional
    private void createTblComponentInspectionResultDetail(
            String componentInspectionResultId,
            ComponentInspectionFormCreateInput inputInfo,
            String userUuid) {
        List<MstComponentInspectionItemsTableDetail> inspectionItemsTableDetails
            = this.mstComponentInspectionItemsTableService.getMstCompInspItemsTableDetails(inputInfo.getComponentInspectionItemsTableId());
        getInspClass(inputInfo.getFirstFlag(), inputInfo.getIncomingCompanyId()).ifPresent((inspClass) -> {
            inputInfo.setMassFlg(Character.getNumericValue(inspClass.getMassFlg()));
        });
        Date sysDate = new Date();
        inspectionItemsTableDetails.stream().forEach(inspItemsDetail -> {
            String inspectionTarget = "";
            int measureSamplingQuantity;
            int visualSamplingQuantity;
            if (this.isOutgoing(inputInfo.getInspectionType())) {
                measureSamplingQuantity = inputInfo.getOutgoingMeasSamplingQuantity();
                visualSamplingQuantity = inputInfo.getOutgoingVisualSamplingQuantity();

                if (null != inputInfo.getMassFlg()) {
                    switch (inputInfo.getMassFlg()) {
                        case CommonConstants.INSPECTION_INT_MASS_FLAG:
                            inspectionTarget = inspItemsDetail.getOutgoingTrialInspectionObject().toString();
                            break;
                        case CommonConstants.INSPECTION_PRODUCTION_MASS_FLAG:
                            inspectionTarget = inspItemsDetail.getOutgoingProductionInspectionObject().toString();
                            break;
                        case CommonConstants.INSPECTION_FIRST_MASS_PRODUCTION_FLAG:
                            inspectionTarget = inspItemsDetail.getOutgoingFirstMassProductionObject().toString();
                            break;
                        default:
                            inspectionTarget = inspItemsDetail.getProcessInspetionObject().toString();
                            break;
                    }
                }
            } else {
                measureSamplingQuantity = inputInfo.getIncomingMeasSamplingQuantity();
                visualSamplingQuantity = inputInfo.getIncomingVisualSamplingQuantity();
                if (null != inputInfo.getMassFlg()) {
                    switch (inputInfo.getMassFlg()) {
                        case CommonConstants.INSPECTION_INT_MASS_FLAG:
                            inspectionTarget = inspItemsDetail.getIncomingTrialInspectionObject().toString();
                            break;
                        case CommonConstants.INSPECTION_PRODUCTION_MASS_FLAG:
                            inspectionTarget = inspItemsDetail.getIncomingProductionInspectionObject().toString();
                            break;
                        case CommonConstants.INSPECTION_FIRST_MASS_PRODUCTION_FLAG:
                            inspectionTarget = inspItemsDetail.getIncomingFirstMassProductionObject().toString();
                            break;
                        default:
                            inspectionTarget = inspItemsDetail.getProcessInspetionObject().toString();
                            break;
                    }
                }
            }
            if (CommonConstants.INSPECTION_TARGET_YES.equals(inspectionTarget)) {
                int loopCount;
                if (CommonConstants.MEASUREMENT_TYPE_VISUAL == inspItemsDetail.getMeasurementType()) {
                    loopCount = visualSamplingQuantity;
                } else {
                    loopCount = measureSamplingQuantity;
                }

                for (int i = 0; i <= loopCount; i++) {
                    for (int c = inputInfo.getCavityStartNum(); c < (inputInfo.getCavityStartNum() + inputInfo.getCavityCnt()); c++) {
                        TblComponentInspectionResultDetail tblComInspResultDetail = new TblComponentInspectionResultDetail();
                        tblComInspResultDetail.setId(IDGenerator.generate());
                        tblComInspResultDetail.setComponentInspectionResultId(componentInspectionResultId);
                        // 検査区分
                        tblComInspResultDetail.setInspectionType(inputInfo.getInspectionType());
                        tblComInspResultDetail.setInspectionItemSno(inspItemsDetail.getInspectionItemSno());
                        tblComInspResultDetail.setSeq(i);
                        if (0 == i) {
                            tblComInspResultDetail.setCavityNum(1);
                        } else {
                            tblComInspResultDetail.setCavityNum(c);
                        }
                        tblComInspResultDetail.setCreateDate(sysDate);
                        tblComInspResultDetail.setCreateUserUuid(userUuid);
                        tblComInspResultDetail.setUpdateDate(sysDate);
                        tblComInspResultDetail.setUpdateUserUuid(userUuid);
                        this.entityManager.persist(tblComInspResultDetail);
                        if (0 == i) {
                            break;
                        } 
                    }
                }
            }
        });
    }
    
    @Transactional
    protected void createTblComponentInspectionResultFile(
            String componentInspectionResultId,
            ComponentInspectionFormCreateInput inputInfo,
            String userUuid) {
        List<MstComponentInspectionItemsFile> inspectionItemsFileDetails = this.entityManager
                .createNamedQuery("MstComponentInspectionItemsFile.findByCondition", MstComponentInspectionItemsFile.class)
                .setParameter("componentId", inputInfo.getComponentId())
                .setParameter("outgoingCompanyId", this.getSelfCompanyId())
                .setParameter("incomingCompanyId", inputInfo.getIncomingCompanyId())
                .getResultList();

        inspectionItemsFileDetails.stream().forEach(inspectionItemFileDetail -> {
            TblComponentInspectionResultFile tblComInspResultFile = new TblComponentInspectionResultFile();
            tblComInspResultFile.setId(IDGenerator.generate());
            tblComInspResultFile.setComponentInspectionResultId(componentInspectionResultId);
            tblComInspResultFile.setFileUuid(inspectionItemFileDetail.getFileUuid());
            this.entityManager.persist(tblComInspResultFile);
        });

    }



    /**
     * Update TblComponentInspectionResultDetail
     *
     * @param inputInfo
     * @param userUuid
     */
    @Transactional
    void updateInspectionResultDetail(ComponentInspectionResultSaveInput inputInfo, String userUuid) {
        String componentInspectionResultId = inputInfo.getComponentInspectionResultId();
        Integer inspectionType = inputInfo.getInspectionType();
        List<TblComponentInspectionResultDetail> tblResultDetails = this.entityManager
                .createNamedQuery("TblComponentInspectionResultDetail.findByComponentInspectionResultIdAndInspectionType",
                        TblComponentInspectionResultDetail.class)
                .setParameter("componentInspectionResultId", componentInspectionResultId)
                .setParameter("inspectionType", inspectionType)
                .getResultList();

        Map<String, TblComponentInspectionResultDetail> tblResultDetailMap = new LinkedHashMap<>();
        tblResultDetails.stream().forEach((tblResultDetail) -> {
            String mapKey = tblResultDetail.getInspectionItemSno() + tblResultDetail.getSeq() + tblResultDetail.getCavityNum();
            tblResultDetailMap.put(mapKey, tblResultDetail);
        });

        List<ComponentInspectionItemResultDetail> measureResultDetails = inputInfo.getMeasureResultDetails();
        List<ComponentInspectionItemResultDetail> visualResultDetails = inputInfo.getVisualResultDetails();
        List<ComponentInspectionItemResultDetail> resultInputList = new ArrayList<>();
        if (measureResultDetails != null) {
            resultInputList.addAll(measureResultDetails);
        }
        if (visualResultDetails != null) {
            resultInputList.addAll(visualResultDetails);
        }

        Date sysDate = new Date();
        TblComponentInspectionResultDetail tableData;
        for (ComponentInspectionItemResultDetail resultInput : resultInputList) {

            String inspectionItemSno = resultInput.getInspectionItemSno();
            tableData = tblResultDetailMap.get(inspectionItemSno + "01");
            //2017.10.17
            tableData.setItemResultManual(resultInput.getItemResultManual());
            tableData.setItemResultAuto(resultInput.getItemResultAuto());
            tableData.setManJudgeComment(resultInput.getManJudgeComment());
            tableData.setNote(resultInput.getNote());

            tableData.setItemResult(resultInput.getItemResult());
            if (!Objects.equals(tableData.getItemResult(), resultInput.getItemResult())) {
                tableData.setItemResult(resultInput.getItemResult());
                tableData.setUpdateDate(sysDate);
                tableData.setUpdateUserUuid(userUuid);
                this.entityManager.merge(tableData);

            }

            List<SamplingInspectionResult> samplingResultInputList = resultInput.getSamplingInspectionResults();
            for (SamplingInspectionResult samplingResultInput : samplingResultInputList) {
                tableData = tblResultDetailMap.get(inspectionItemSno + samplingResultInput.getSeq() + samplingResultInput.getCavityNum());
                if (tableData != null && (!Objects.equals(tableData.getSeqMeasurementResult(), samplingResultInput.getSeqMeasurementResult())
                        || !Objects.equals(tableData.getSeqVisualResult(), samplingResultInput.getSeqVisualResult()))) {
                    if (samplingResultInput.getSeqMeasurementResult() != null) {
                        tableData.setSeqMeasurementResult(samplingResultInput.getSeqMeasurementResult().setScale(5, BigDecimal.ROUND_DOWN));
                    } else {
                        tableData.setSeqMeasurementResult(samplingResultInput.getSeqMeasurementResult());
                    }
                    tableData.setCavityNum(samplingResultInput.getCavityNum());
                    tableData.setSeqVisualResult(samplingResultInput.getSeqVisualResult());
                    tableData.setUpdateDate(sysDate);
                    tableData.setUpdateUserUuid(userUuid);
                    this.entityManager.merge(tableData);
                }
            }
        }

    }

    /**
     * 検査結果ダウンロード取得
     *
     * @param searchCondition
     * @param isCsv
     * @param langId
     * @return
     */
    public TblComponentInspectionResultList getTblComponentInspectionResultList(ComponentInspectionResultSearchCond searchCondition, boolean isCsv, String langId) {
        TblComponentInspectionResultList resultList = new TblComponentInspectionResultList();

        List<ComponentInspectionResultInfo> list = getInspectionResults(searchCondition, isCsv, langId);
        List<ComponentInspectionResultInfo> newList = new ArrayList();
        if (list != null && list.size() > 0) {
            for (ComponentInspectionResultInfo inspectionResultInfo : list) {

                List<Integer> inspectionTypeList = new ArrayList<>();
                if(searchCondition.getFunctionType().equals("1")) {
                    inspectionTypeList.add(CommonConstants.INSPECTION_TYPE_OUTGOING);
                } else if(searchCondition.getFunctionType().equals("3")) {
                    inspectionTypeList.add(CommonConstants.INSPECTION_TYPE_INCOMING);
                } else {
                    inspectionTypeList.add(CommonConstants.INSPECTION_TYPE_OUTGOING);
                    inspectionTypeList.add(CommonConstants.INSPECTION_TYPE_INCOMING);
                }

                // select MstComponentInspectionItemsTableDetail
                Map<String, MstComponentInspectionItemsTableDetail> inspectionItemsTableDetailMap
                        = this.mstComponentInspectionItemsTableService.getMstCompInspItemsTableDetailsMap(inspectionResultInfo.getComponentInspectionItemsTableId());
                if (inspectionItemsTableDetailMap.isEmpty()) {
                    return null;
                }

                // select TblComponentInspectionResultDetail
                List<TblComponentInspectionResultDetail> tblResultDetails = this.entityManager
                        .createNamedQuery("TblComponentInspectionResultDetail.findByComponentInspectionResultIdAndInspectionTypeList",
                                TblComponentInspectionResultDetail.class)
                        .setParameter("componentInspectionResultId", inspectionResultInfo.getComponentInspectionResultId())
                        .setParameter("inspectionType", inspectionTypeList)
                        .getResultList();

                Map<String, List<TblComponentInspectionResultDetail>> tblResultDetailMa = this.convertToTblResultMap(tblResultDetails, inspectionItemsTableDetailMap);
                this.setInspectionResultDetailList(inspectionResultInfo, tblResultDetailMa, inspectionItemsTableDetailMap);
                newList.add(inspectionResultInfo);
            }
        }
        resultList.setComponentInspectionResultInfoList(newList);

        return resultList;
    }

    /**
     * 検査結果ダウンロードCSV出力
     *
     * @param searchCondition
     * @param componentId
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param loginUser
     * @return
     */
    public FileReponse postTblComponentInspectionResultCSV(ComponentInspectionResultSearchCond searchCondition,
                                                           String componentId, String outgoingCompanyId, String incomingCompanyId, LoginUser loginUser) {
        FileReponse reponse = new FileReponse();
        //CSVファイル出力
        String fileUuid = IDGenerator.generate();
        String langId = loginUser.getLangId();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, fileUuid);
        // ヘッダー種取得
        Map<String, String> headerMap = getDictionaryList(langId);
        TblComponentInspectionResultList tblComponentInspectionResultList = getTblComponentInspectionResultList(searchCondition, true, loginUser.getLangId());

        /*Head*/
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();

        headList.add(headerMap.get("component_inspection_implementation_date")); //時間
        headList.add(headerMap.get("component_code")); //部品コード
        headList.add(headerMap.get("component_name")); //部品名称
        headList.add(headerMap.get("manufacture_lot")); //製造ロット
        headList.add(headerMap.get("cav_no")); //cav番号
        headList.add(headerMap.get("component_inspection_user")); //検査実施者
        headList.add(headerMap.get("outgoing_company_name")); //出荷元
        headList.add(headerMap.get("incoming_company_name")); //入荷先
        headList.add(headerMap.get("first_flag")); //検査区分
        headList.add(headerMap.get("component_inspection_type")); //検査タイプ
        headList.add(headerMap.get("component_inspection_times")); //測定回数

        /*検査項目*/
        int index = 1;
        List<MstComponentInspectionItemsTableDetail> listItemDetail;
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM mst_component_inspection_items_table_detail m");
        queryBuilder.append(" WHERE m.COMPONENT_INSPECTION_ITEMS_TABLE_ID = ");
        queryBuilder.append(" (SELECT id FROM mst_component_inspection_items_table t");
        queryBuilder.append(" WHERE t.COMPONENT_ID = ? AND t.OUTGOING_COMPANY_ID = ? AND t.INCOMING_COMPANY_ID = ?");
        queryBuilder.append(" ORDER BY version DESC LIMIT 1) ");
        queryBuilder.append(" ORDER BY m.MEASUREMENT_TYPE,m.LOCAL_SEQ,m.INSPECTION_ITEM_SNO ");
        listItemDetail = this.entityManager.createNativeQuery(queryBuilder.toString(), MstComponentInspectionItemsTableDetail.class)
                .setParameter(index++, componentId)
                .setParameter(index++, incomingCompanyId)
                .setParameter(index++, outgoingCompanyId)
                .getResultList();
        List<String> titleList = new ArrayList();
        if (listItemDetail != null && listItemDetail.size() > 0) {
            listItemDetail.stream().forEach((itemDetail) -> {
                String containsKey = (itemDetail.getDrawingPage() == null ? "" : itemDetail.getDrawingPage())
                        + (itemDetail.getDrawingAnnotation() == null ? "" : itemDetail.getDrawingAnnotation())
                        + (itemDetail.getDrawingMentionNo() == null ? "" : itemDetail.getDrawingMentionNo())
                        + (itemDetail.getSimilarMultiitem() == null ? "" : itemDetail.getSimilarMultiitem());
                headList.add(containsKey);
                containsKey += SPLIT_REGEX;
                titleList.add(containsKey);
            });
        }

        gLineList.add(headList);

        /*DATA*/
        ArrayList lineList;
        Map<String, String> containsMap;
        String measurement = headerMap.get("measurement");//測定
        // 検査結果ダウンロード取得
        if (tblComponentInspectionResultList != null && tblComponentInspectionResultList.getComponentInspectionResultInfoList() != null
                && tblComponentInspectionResultList.getComponentInspectionResultInfoList().size() > 0) {
            for (int i = 0; i < tblComponentInspectionResultList.getComponentInspectionResultInfoList().size(); i++) {
                ComponentInspectionResultInfo componentInspectionResultInfo = tblComponentInspectionResultList.getComponentInspectionResultInfoList().get(i);
                //測定(出荷元)
                if (componentInspectionResultInfo.getOutgoingMeasSamplingQuantity() != null) {
                    for (int n = componentInspectionResultInfo.getCavityStartNum(); n < componentInspectionResultInfo.getCavityStartNum() + componentInspectionResultInfo.getCavityCnt(); n++) {
                    for (int row = 0; row < componentInspectionResultInfo.getOutgoingMeasSamplingQuantity(); row++) {
                        lineList = new ArrayList();
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getOutgoingInspectionDate())) {
                            lineList.add(componentInspectionResultInfo.getOutgoingInspectionDate()); //時間
                        } else {
                            lineList.add(""); //時間
                        }
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getComponentCode())) {
                            lineList.add(componentInspectionResultInfo.getComponentCode());
                        } else {
                            lineList.add("");
                        }
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getComponentName())) {
                            lineList.add(componentInspectionResultInfo.getComponentName());
                        } else {
                            lineList.add("");
                        }
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getProductionLotNum())
                                && componentInspectionResultInfo.getMassFlg() != CommonConstants.INSPECTION_INT_MASS_FLAG) {
                            lineList.add(componentInspectionResultInfo.getProductionLotNum());
                        } else {
                            lineList.add("");
                        }
                        
                        //cav番号
                        lineList.add(componentInspectionResultInfo.getCavityPrefix() + String.valueOf(n));
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getOutgoingInspectionPersonName())) {
                            lineList.add(componentInspectionResultInfo.getOutgoingInspectionPersonName()); //検査実施者
                        } else {
                            lineList.add("");
                        }
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getOutgoingCompanyName())) {
                            lineList.add(componentInspectionResultInfo.getOutgoingCompanyName()); //出荷元
                        } else {
                            lineList.add("");
                        }
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getIncomingCompanyName())) {
                            lineList.add(componentInspectionResultInfo.getIncomingCompanyName()); //入荷先
                        } else {
                            lineList.add("");
                        }
                        lineList.add(componentInspectionResultInfo.getFirstFlagName());//検査区分
                        lineList.add(headerMap.get("component_inspection_outgoing")); //出荷
                        lineList.add(measurement + (row + 1)); //測定回数
                        if (componentInspectionResultInfo.getOutgoingMeasureResultDetails() != null && componentInspectionResultInfo.getOutgoingMeasureResultDetails().size() > 0) {
                            containsMap = new HashMap<>();
                            String containsKey;
                            for (ComponentInspectionItemResultDetail detail : componentInspectionResultInfo.getOutgoingMeasureResultDetails()) {
                                containsKey = (detail.getDrawingPage() == null ? "" : detail.getDrawingPage())
                                        + (detail.getDrawingAnnotation() == null ? "" : detail.getDrawingAnnotation())
                                        + (detail.getDrawingMentionNo() == null ? "" : detail.getDrawingMentionNo())
                                        + (detail.getSimilarMultiitem() == null ? "" : detail.getSimilarMultiitem());
                                containsKey = containsKey.concat(SPLIT_REGEX);
                                if (detail.getSamplingInspectionResults() != null && detail.getSamplingInspectionResults().size() > 0) {
                                    for (SamplingInspectionResult samplingInspectionResult : detail.getSamplingInspectionResults()) {
                                        if ((row + 1) == samplingInspectionResult.getSeq() && n == samplingInspectionResult.getCavityNum()) {
                                            if (samplingInspectionResult.getSeqMeasurementResult() != null
                                                    && samplingInspectionResult.getSeqMeasurementResult().compareTo(BigDecimal.ZERO) != 0) {
                                                containsMap.put(containsKey, samplingInspectionResult.getSeqMeasurementResult() + "");
                                            } else {
                                                containsMap.put(containsKey, "");
                                            }
                                        }
                                    }
                                } else {
                                    containsMap.put(containsKey, "");
                                }
                            }
                            for (int j = 0; j < titleList.size(); j++) {
                                if (containsMap.containsKey(titleList.get(j))) {
                                    lineList.add(containsMap.get(titleList.get(j)));
                                } else {
                                    lineList.add("");
                                }
                            }
                        }
                        if (!searchCondition.getFunctionType().equals("3")) {
                            gLineList.add(lineList);
                        }
                    }
                    }
                }

                //測定(入荷元)
                if (componentInspectionResultInfo.getIncomingMeasSamplingQuantity() != null) {
                    for (int n = componentInspectionResultInfo.getCavityStartNum(); n < componentInspectionResultInfo.getCavityStartNum() + componentInspectionResultInfo.getCavityCnt(); n++) {
                    for (int row = 0; row < componentInspectionResultInfo.getIncomingMeasSamplingQuantity(); row++) {
                        lineList = new ArrayList();
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getIncomingInspectionDate())) {
                            lineList.add(componentInspectionResultInfo.getIncomingInspectionDate()); //時間
                        } else {
                            lineList.add(""); //時間
                        }
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getComponentCode())) {
                            lineList.add(componentInspectionResultInfo.getComponentCode());
                        } else {
                            lineList.add("");
                        }
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getComponentName())) {
                            lineList.add(componentInspectionResultInfo.getComponentName());
                        } else {
                            lineList.add("");
                        }
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getProductionLotNum())
                                && componentInspectionResultInfo.getMassFlg() != CommonConstants.INSPECTION_INT_MASS_FLAG) {
                            lineList.add(componentInspectionResultInfo.getProductionLotNum());
                        } else {
                            lineList.add("");
                        }
                        
                        //cav番号
                        lineList.add(componentInspectionResultInfo.getCavityPrefix() + String.valueOf(n));
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getIncomingInspectionPersonName())) {
                            lineList.add(componentInspectionResultInfo.getIncomingInspectionPersonName()); //検査実施者
                        } else {
                            lineList.add("");
                        }
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getOutgoingCompanyName())) {
                            lineList.add(componentInspectionResultInfo.getOutgoingCompanyName()); //出荷元
                        } else {
                            lineList.add("");
                        }
                        if (StringUtils.isNotEmpty(componentInspectionResultInfo.getIncomingCompanyName())) {
                            lineList.add(componentInspectionResultInfo.getIncomingCompanyName()); //入荷先
                        } else {
                            lineList.add("");
                        }
                        lineList.add(componentInspectionResultInfo.getFirstFlagName());//検査区分
                        lineList.add(headerMap.get("component_inspection_incoming")); //入荷
                        lineList.add(measurement + (row + 1)); //測定回数
                        if (componentInspectionResultInfo.getIncomingMeasureResultDetails() != null && componentInspectionResultInfo.getIncomingMeasureResultDetails().size() > 0) {
                            containsMap = new HashMap<>();
                            String containsKey;
                            for (ComponentInspectionItemResultDetail detail : componentInspectionResultInfo.getIncomingMeasureResultDetails()) {
                                containsKey = (detail.getDrawingPage() == null ? "" : detail.getDrawingPage())
                                        + (detail.getDrawingAnnotation() == null ? "" : detail.getDrawingAnnotation())
                                        + (detail.getDrawingMentionNo() == null ? "" : detail.getDrawingMentionNo())
                                        + (detail.getSimilarMultiitem() == null ? "" : detail.getSimilarMultiitem());
                                containsKey = containsKey.concat(SPLIT_REGEX);
                                if (detail.getSamplingInspectionResults() != null && detail.getSamplingInspectionResults().size() > 0) {
                                    for (SamplingInspectionResult samplingInspectionResult : detail.getSamplingInspectionResults()) {
                                        if ((row + 1) == samplingInspectionResult.getSeq() && n == samplingInspectionResult.getCavityNum()) {
                                            if (samplingInspectionResult.getSeqMeasurementResult() != null
                                                    && samplingInspectionResult.getSeqMeasurementResult().compareTo(BigDecimal.ZERO) != 0) {
                                                containsMap.put(containsKey, samplingInspectionResult.getSeqMeasurementResult() + "");
                                            } else {
                                                containsMap.put(containsKey, "");
                                            }
                                        }
                                    }
                                } else {
                                    containsMap.put(containsKey, "");
                                }
                            }
                            for (int j = 0; j < titleList.size(); j++) {
                                if (containsMap.containsKey(titleList.get(j))) {
                                    lineList.add(containsMap.get(titleList.get(j)));
                                } else {
                                    lineList.add("");
                                }
                            }
                        }
                        if (!searchCondition.getFunctionType().equals("1")) {
                            gLineList.add(lineList);
                        }
                    }
                    }
                }

            }

        }
        // csv 出力
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(fileUuid);
        tblCsvExport.setExportDate(new Date());
        MstFunction functionId = new MstFunction();
        functionId.setId(CommonConstants.FUN_ID_TBL_COMPONENT_INSPECTION_RESULT); //検査結果ダウンロード
        tblCsvExport.setFunctionId(functionId);
        tblCsvExport.setExportTable("tbl_component_inspection_result");
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        // ファイル名称
        String fileName = headerMap.get("component_inspection_result_download");
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        reponse.setFileUuid(fileUuid);

        return reponse;
    }

    private Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("component_inspection_implementation_date", "component_code", "component_name", "manufacture_lot", "component_inspection_user", "component_inspection_times",
                "outgoing_company_name", "incoming_company_name", "first_flag", "component_inspection_type", "component_inspection_outgoing", "component_inspection_incoming",
                "component_inspection_result_download", "measurement", "cav_no");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        return headerMap;
    }

    /**
     * 発注POテーブル、PO数量取得
     *
     * @param componentId
     * @param productionLotNumber
     * @param companyId
     * @param outgoingCompanyId
     * @return
     */
    public TblComponentInspectionResultPoList getTblPoOutbound(String componentId, String productionLotNumber, String companyId, String outgoingCompanyId) {
        TblComponentInspectionResultPoList poList = new TblComponentInspectionResultPoList();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT poCode.orderNumber,poCode.itemNumber FROM (");
        queryBuilder.append(" (SELECT poOutbound.order_no orderNumber,poOutbound.ITEM_NO itemNumber FROM tbl_po_outbound poOutbound JOIN tbl_shipment_outbound shipmentOutbound ");
        queryBuilder.append(" ON poOutbound.uuid = shipmentOutbound.po_id WHERE shipmentOutbound.component_id = ? ");
        queryBuilder.append(" AND shipmentOutbound.production_lot_number =? ");
        queryBuilder.append(" AND poOutbound.delivery_src_id = ?) UNION ");
        queryBuilder.append(" (SELECT po.ORDER_NUMBER orderNumber,po.ITEM_NUMBER itemNumber FROM tbl_po po JOIN tbl_shipment shipment ON po.uuid = shipment.po_id ");
        queryBuilder.append(" WHERE shipment.component_id = ? ");
        queryBuilder.append(" AND shipment.production_lot_number =? ");
        queryBuilder.append(" AND po.delivery_dest_id = ?)) poCode ");

        Query query = entityManager.createNativeQuery(queryBuilder.toString());
        int index = 1;
        query.setParameter(index++, componentId);
        query.setParameter(index++, productionLotNumber);
        query.setParameter(index++, outgoingCompanyId);
        query.setParameter(index++, componentId);
        query.setParameter(index++, productionLotNumber);
        query.setParameter(index++, companyId);

        List list = query.getResultList();
        List<TblComponentInspectionResultPoVo> tblComponentInspectionResultPoVos = new ArrayList();
        if (list != null && list.size() > 0) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Object[] resultPo = (Object[]) iterator.next();
                TblComponentInspectionResultPoVo vo = new TblComponentInspectionResultPoVo();
                vo.setOrderNumber(String.valueOf(resultPo[0]) == null ? "" : String.valueOf(resultPo[0]));
                vo.setItemNumber(String.valueOf(resultPo[1]) == null ? "" : String.valueOf(resultPo[1]));
                tblComponentInspectionResultPoVos.add(vo);
            }
        }
        poList.setTblComponentInspectionResultPoVos(tblComponentInspectionResultPoVos);

        return poList;
    }

    private boolean isOutgoing(Integer inspectionType) {
        return CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType;
    }

    private boolean isInternalOutgoing(String outgoingCompanyId, String incomingCompanId) {
        return Objects.equals(outgoingCompanyId, incomingCompanId);
    }

    private String getSelfCompanyId() {
        MstCompany selfCompany = this.mstCompanyService.getSelfCompany();
        return selfCompany.getId();
    }

    // ADDSインタフェースを対応 2017/11/2 penggd Start

    /**
     * extAccept
     * NOTE: inspectionStatus switch from 14 -> 20
     *
     * @param componentCode
     * @param inspectType
     * @param poNumber
     * @param incomingMeasSamplingQuantity
     * @param incomingVisualSamplingQuantity
     * @param loginUser
     * @return
     */
    @Transactional
    public TblComponentInspectionResultVo extAccept(String componentCode, String inspectType, String poNumber,
                                                    String incomingMeasSamplingQuantity, String incomingVisualSamplingQuantity, LoginUser loginUser) {

        TblComponentInspectionResultVo response = new TblComponentInspectionResultVo();

        // 入力のパラメータを使って、検査結果を検索する
        String componentId = "";

        MstComponent mstComponent = mstComponentService.getMstComponent(componentCode);

        if (null != mstComponent) {

            componentId = mstComponent.getId();
        }

//        List<MstComponentInspectClass> mstComponentInspectClassList = mstComponentInspectFileService
//                .getClassSql(loginUser.getLangId(), null, inspectType);
//
//        MstComponentInspectClass mstComponentInspectClass = new MstComponentInspectClass();
//
//        if (mstComponentInspectClassList.size() > 0) {
//
//            mstComponentInspectClass = mstComponentInspectClassList.get(0);
//        } else {
//
//            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist",
//                    loginUser.getLangId());
//
//            return response;
//        }

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t FROM TblComponentInspectionResult t");
        queryBuilder.append(" JOIN FETCH t.classLang mstComponentInspectLang ");
        queryBuilder.append(" LEFT JOIN FETCH TblShipmentOutbound shipmentOutbound ");
        queryBuilder.append(" JOIN shipmentOutbound.tblPoOutbound poOutbound ");
        queryBuilder.append(" ON shipmentOutbound.productionLotNumber = t.productionLotNum");
        queryBuilder.append(" AND shipmentOutbound.componentId = t.componentId ");
        queryBuilder.append(" AND concat(poOutbound.orderNo,'-',poOutbound.itemNo) = :poNumber ");
        queryBuilder.append(" WHERE mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId");
        queryBuilder.append(" AND mstComponentInspectLang.dictValue = :dictValue");
        queryBuilder.append(" AND t.inspectionStatus = :inspectionStatus ");
        queryBuilder.append(" AND t.componentId = :componentId ");
        queryBuilder.append(" ORDER BY t.createDate DESC");

        //switch INSPECTION_STATUS_O_APPROVED -> INSPECTION_STATUS_I_ACCEPTED
        TblComponentInspectionResult tblCompInspResult = this.entityManager
                .createQuery(queryBuilder.toString(), TblComponentInspectionResult.class)
                .setParameter("poNumber", poNumber)
                .setParameter("langId", loginUser.getLangId())
                .setParameter("dictValue", inspectType)
                .setParameter("inspectionStatus", CommonConstants.INSPECTION_STATUS_I_ACCEPTED)
                .setParameter("componentId", componentId).getResultList().stream().findFirst().orElse(null);

        if (tblCompInspResult == null) {

            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist",
                    loginUser.getLangId());

            return response;
        } else if (CommonConstants.INSPECTION_STATUS_I_ACCEPTED != tblCompInspResult.getInspectionStatus()) {

            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION,
                    "msg_error_forbidden_by_outgoing_inspection_end", loginUser.getLangId());

            return response;
        }

        if (FileUtil.isInteger(incomingMeasSamplingQuantity)) {

            if (Integer.parseInt(incomingMeasSamplingQuantity) < 0) {

                this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_value_invalid",
                        loginUser.getLangId());

                return response;
            }

        } else {

            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_value_invalid",
                    loginUser.getLangId());

            return response;
        }

        if (FileUtil.isInteger(incomingVisualSamplingQuantity)) {

            if (Integer.parseInt(incomingVisualSamplingQuantity) < 0) {

                this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_value_invalid",
                        loginUser.getLangId());

                return response;
            }

        } else {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_value_invalid",
                    loginUser.getLangId());

            return response;
        }

        Date sysDate = new Date();
        MstUser mstUser = this.mstUserService.getMstUser(loginUser.getUserid());

        // 入荷検査中を設定します
        tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_I_INSPECTING);
        tblCompInspResult.setIncomingMeasSamplingQuantity(Integer.parseInt(incomingMeasSamplingQuantity));
        tblCompInspResult.setIncomingVisualSamplingQuantity(Integer.parseInt(incomingVisualSamplingQuantity));

        tblCompInspResult.setAcceptanceDate(sysDate);
        tblCompInspResult.setAcceptancePersonUuid(loginUser.getUserUuid());
        tblCompInspResult.setAcceptancePersonName(mstUser.getUserName());

        tblCompInspResult.setUpdateDate(sysDate);
        tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());
        this.entityManager.merge(tblCompInspResult);

        // 入荷検査明細情報を登録する
        ComponentInspectionFormCreateInput inputInfo = new ComponentInspectionFormCreateInput();
        inputInfo.setComponentInspectionItemsTableId(tblCompInspResult.getComponentInspectionItemsTableId());
        inputInfo.setInspectionType(CommonConstants.INSPECTION_TYPE_INCOMING);
        inputInfo.setIncomingMeasSamplingQuantity(tblCompInspResult.getIncomingMeasSamplingQuantity());
        inputInfo.setIncomingVisualSamplingQuantity(tblCompInspResult.getIncomingVisualSamplingQuantity());
        inputInfo.setFirstFlag(tblCompInspResult.getFirstFlag());
        getInspClass(tblCompInspResult.getFirstFlag(), tblCompInspResult.getIncomingCompanyId()).ifPresent(inspClass -> {
            inputInfo.setMassFlg(Character.getNumericValue(inspClass.getMassFlg()));
        });
        // createTblComponentInspectionResultDetail(tblCompInspResult.getId(),
        // inputInfo, loginUser.getUserUuid());
        List<MstComponentInspectionItemsTableDetail> inspectionItemsTableDetails = this.mstComponentInspectionItemsTableService
                .getMstCompInspItemsTableDetails(inputInfo.getComponentInspectionItemsTableId());
        Date nowDate = new Date();

        if (inspectionItemsTableDetails.size() > 0) {

            for (MstComponentInspectionItemsTableDetail inspItemsDetail : inspectionItemsTableDetails) {
                String inspectionTarget = "";
                int measureSamplingQuantity;
                int visualSamplingQuantity;
                if (this.isOutgoing(inputInfo.getInspectionType())) {
                    measureSamplingQuantity = inputInfo.getOutgoingMeasSamplingQuantity();
                    visualSamplingQuantity = inputInfo.getOutgoingVisualSamplingQuantity();

                    if (null != inputInfo.getMassFlg()) {
                        switch (inputInfo.getMassFlg()) {
                            case CommonConstants.INSPECTION_INT_MASS_FLAG:
                                inspectionTarget = inspItemsDetail.getOutgoingTrialInspectionObject().toString();
                                break;
                            case CommonConstants.INSPECTION_PRODUCTION_MASS_FLAG:
                                inspectionTarget = inspItemsDetail.getOutgoingProductionInspectionObject().toString();
                                break;
                            default:
                                inspectionTarget = inspItemsDetail.getProcessInspetionObject().toString();
                                break;
                        }
                    }
                } else {
                    measureSamplingQuantity = inputInfo.getIncomingMeasSamplingQuantity();
                    visualSamplingQuantity = inputInfo.getIncomingVisualSamplingQuantity();

                    if (null != inputInfo.getMassFlg()) {
                        switch (inputInfo.getMassFlg()) {
                            case CommonConstants.INSPECTION_INT_MASS_FLAG:
                                inspectionTarget = inspItemsDetail.getIncomingTrialInspectionObject().toString();
                                break;
                            case CommonConstants.INSPECTION_PRODUCTION_MASS_FLAG:
                                inspectionTarget = inspItemsDetail.getIncomingProductionInspectionObject().toString();
                                break;
                            default:
                                inspectionTarget = inspItemsDetail.getProcessInspetionObject().toString();
                                break;
                        }
                    }
                }
                if (CommonConstants.INSPECTION_TARGET_YES.equals(inspectionTarget)) {
                    int loopCount;
                    if (CommonConstants.MEASUREMENT_TYPE_VISUAL == inspItemsDetail.getMeasurementType()) {
                        loopCount = visualSamplingQuantity;
                    } else {
                        loopCount = measureSamplingQuantity;
                    }

                    for (int i = 0; i <= loopCount; i++) {
                        TblComponentInspectionResultDetail tblComInspResultDetail = new TblComponentInspectionResultDetail();
                        tblComInspResultDetail.setId(IDGenerator.generate());
                        tblComInspResultDetail.setComponentInspectionResultId(tblCompInspResult.getId());
                        // 検査区分
                        tblComInspResultDetail.setInspectionType(inputInfo.getInspectionType());
                        tblComInspResultDetail.setInspectionItemSno(inspItemsDetail.getInspectionItemSno());
                        tblComInspResultDetail.setSeq(i);
                        tblComInspResultDetail.setCreateDate(nowDate);
                        tblComInspResultDetail.setCreateUserUuid(loginUser.getUserUuid());
                        tblComInspResultDetail.setUpdateDate(nowDate);
                        tblComInspResultDetail.setUpdateUserUuid(loginUser.getUserUuid());
                        this.entityManager.persist(tblComInspResultDetail);
                    }
                }
            }
        }

        // 戻り値を設定する
        response.setId(tblCompInspResult.getId());

        return response;
    }
    // ADDSインタフェースを対応 2017/11/2 penggd End

    private void setErrorInfo(BasicResponse response, String errorCode, String msgDictKey, String langId, Object... args) {
        response.setError(true);
        response.setErrorCode(errorCode);

        String msg = mstDictionaryService.getDictionaryValue(langId, msgDictKey);
        if (args != null) {
            msg = String.format(msg, args);
        }
        response.setErrorMessage(msg);
    }

    /**
     * 検査バッチ連携更新ステータス更新
     *
     * @param inputFile
     * @param loginUser
     * @return
     */
    @Transactional
    public boolean updateInspectionResultBatchStatus(ComponentInspectionReferenceFile inputFile, LoginUser loginUser) {
        String componentInspectionResultId = inputFile.getComponentInspectionResultId();
        TblComponentInspectionResult tblCompInspResult = this.getTblInspectionItemResult(componentInspectionResultId);
        if (tblCompInspResult == null) {
            return false;
        }

        if (CommonConstants.INSP_BATCH_UPDATE_STATUS_O_RESULT_SENT.equals(tblCompInspResult.getInspBatchUpdateStatus())) {

            tblCompInspResult.setInspBatchUpdateStatus(CommonConstants.INSP_BATCH_UPDATE_STATUS_O_RESULT_NOT_SEND);// 検査バッチ連携更新ステータス
            tblCompInspResult.setUpdateDate(new Date());
            tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());

            this.entityManager.merge(tblCompInspResult);

        }

        // 部品検査参照ファイル
        updateComponentInspectionReferenceFile(inputFile, loginUser);

        return true;
    }

    /**
     * 検査データに関連付く、POテーブル
     *
     * @param inputInfo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateTblComponentInspectionResultPo(ComponentInspectionActionInput inputInfo, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        String componentInspectionResultId = inputInfo.getInspectionResultIds();
        TblComponentInspectionResult tblCompInspResult = this.getTblInspectionItemResult(componentInspectionResultId);
        if (tblCompInspResult == null) {
            return basicResponse;
        }
        if (CommonConstants.PRODUCTION_LOT_NUMBER.equals(inputInfo.getProductionLotNum())) {
            tblCompInspResult.setProductionLotNum(inputInfo.getProductionLotNum());
            tblCompInspResult.setUpdateDate(new Date());
            tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());

            this.entityManager.merge(tblCompInspResult);

        } else {
            tblCompInspResult.setProductionLotNum(inputInfo.getProductionLotNum());
            if (tblCompInspResult.getInspectionStatus() == CommonConstants.INSPECTION_STATUS_O_MATCH) {
                tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_O_APPROVED);
            }
            tblCompInspResult.setUpdateDate(new Date());
            tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());

            this.entityManager.merge(tblCompInspResult);

        }
        return basicResponse;
    }

    @Transactional
    public BasicResponse updateTblComponentInspectionResultMaterial(ComponentInspectionActionInput inputInfo, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        String componentInspectionResultId = inputInfo.getInspectionResultIds();
        TblComponentInspectionResult tblCompInspResult = this.getTblInspectionItemResult(componentInspectionResultId);
        if (tblCompInspResult == null) {
            return basicResponse;
        }

        tblCompInspResult.setMaterial01(inputInfo.getMaterial01());
        tblCompInspResult.setMaterial02(inputInfo.getMaterial02());
        tblCompInspResult.setMaterial03(inputInfo.getMaterial03());
        tblCompInspResult.setUpdateDate(new Date());
        tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());
        this.entityManager.merge(tblCompInspResult);

        return basicResponse;
    }
    
    @Transactional
    public void updateInspectDate(ComponentInspectionActionInput inputInfo, LoginUser loginUser) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        TblComponentInspectionResult tblCompInspResult = this.getTblInspectionItemResult(inputInfo.getInspectionResultIds());
        if (tblCompInspResult == null) {
            return;
        }
        try {
            Date date = StringUtils.isEmpty(inputInfo.getInspectDate()) ? null : sdf.parse(inputInfo.getInspectDate());
            if(inputInfo.getInspectionType() == CommonConstants.INSPECTION_TYPE_OUTGOING) {
                tblCompInspResult.setOutgoingInspectionDate(date);
            } else if(inputInfo.getInspectionType() == CommonConstants.INSPECTION_TYPE_INCOMING) {
                tblCompInspResult.setIncomingInspectionDate(date);
                if(date == null) {
                    tblCompInspResult.setIncomingInspectionMonth(null);
                    tblCompInspResult.setIncomingInspectionDate(null);
                } else {
                    CnfSystem cnf = cnfSystemService.findByKey("system", "business_start_day_of_week");
                    setWeekAndMonth(tblCompInspResult, cnf);
                }
            }
            tblCompInspResult.setUpdateDate(new Date());
            tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());
        } catch (ParseException ex) {/** ignore wrong format*/}
    }

    // KM-463 PO検査間のデータ構造変更 2017/12/5 by penggd Start

    /**
     * Get external outgoing poInfo
     *
     * @param incomingCompanyId
     * @return
     */
    public ComponentPoInfoForBatch getExtOutgoingPoInfo(String incomingCompanyId) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t FROM TblShipment t");
        queryBuilder.append(" JOIN FETCH t.mstComponent");
        queryBuilder.append(" JOIN FETCH t.tblPo");
        queryBuilder.append(" JOIN FETCH t.tblPo.mstComponent");
        queryBuilder.append(" WHERE t.batchUpdateStatus = :batchUpdateStatus1");
        queryBuilder.append(" AND t.tblPo.batchUpdateStatus = :batchUpdateStatus2");
        queryBuilder.append(" AND t.tblPo.deliveryDestId = :deliveryDestId");
        queryBuilder.append(" ORDER BY t.createDate ");

        List<TblShipment> shipMentList = this.entityManager.createQuery(queryBuilder.toString(), TblShipment.class)
                .setParameter("batchUpdateStatus1", CommonConstants.PO_BATCH_UPDATE_STATUS_UNDONE)
                .setParameter("batchUpdateStatus2", CommonConstants.PO_BATCH_UPDATE_STATUS_UNDONE)
                .setParameter("deliveryDestId", incomingCompanyId).setMaxResults(50).getResultList();

        ComponentPoInfoForBatch result = new ComponentPoInfoForBatch();

        Map<String, TblPo> poMap = new HashMap<>();

        List<TblShipmentOutboundVo> tblShipmentOutboundVoList = new ArrayList<>();
        for (TblShipment shipment : shipMentList) {

            TblShipmentOutboundVo tblShipmentOutboundVo = new TblShipmentOutboundVo();

            // 会社別部品コードマスタから、部品コードを取得する
            List<MstComponentCompany> shipComponentCompanyList = this.entityManager
                    .createNamedQuery("MstComponentCompany.findByPk", MstComponentCompany.class)
                    .setParameter("companyId", incomingCompanyId).setParameter("componentId", shipment.getComponentId())
                    .getResultList();

            if (shipComponentCompanyList.isEmpty()) {

                tblShipmentOutboundVo.setShipmentComponentCode(shipment.getMstComponent().getComponentCode());
            } else {

                tblShipmentOutboundVo.setShipmentComponentCode(shipComponentCompanyList.get(0).getOtherComponentCode());
            }

            tblShipmentOutboundVo.setShipmentOutbound(getTblShipmentOutbound(shipment));

            tblShipmentOutboundVoList.add(tblShipmentOutboundVo);

            poMap.put(shipment.getPoId(), shipment.getTblPo());

        }

        result.setTblShipmentOutboundVoList(tblShipmentOutboundVoList);

        List<TblPoOutboundVo> tblPoOutboundVoList = new ArrayList<>();
        for (Entry<String, TblPo> entry : poMap.entrySet()) {

            TblPoOutboundVo tblPoOutboundVo = new TblPoOutboundVo();

            TblPo po = entry.getValue();

            // 会社別部品コードマスタから、部品コードを取得する
            List<MstComponentCompany> poComponentCompanyList = this.entityManager
                    .createNamedQuery("MstComponentCompany.findByPk", MstComponentCompany.class)
                    .setParameter("companyId", incomingCompanyId)
                    .setParameter("componentId", po.getComponentId()).getResultList();

            if (poComponentCompanyList.isEmpty()) {

                tblPoOutboundVo.setPoComponentCode(po.getMstComponent().getComponentCode());
            } else {

                tblPoOutboundVo.setPoComponentCode(poComponentCompanyList.get(0).getOtherComponentCode());
            }

            tblPoOutboundVo.setPoOutbound(getTblPoOutbound(po));

            tblPoOutboundVoList.add(tblPoOutboundVo);
        }

        result.setTblPoOutboundVoList(tblPoOutboundVoList);

        return result;
    }

    /**
     * Import inspection outgoing result data by batch
     *
     * @param inputData
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @return
     */
    @Transactional
    public Map<String, String> importOutgoingPoInfoByBatch(ComponentPoInfoForBatch inputData, String outgoingCompanyId,
                                                           String incomingCompanyId) {

        Map<String, String> uuidMap = new LinkedHashMap<>();

        // 発注POテーブルの連携 Start
        if (!inputData.getTblPoOutboundVoList().isEmpty()) {

            // 発注POテーブルに対し、部品コードを読み替える Start
            List<String> poComponentCodeList = inputData.getTblPoOutboundVoList().stream()
                    .map(mapper -> mapper.getPoComponentCode()).collect(Collectors.toCollection(ArrayList::new));

            List<MstComponent> mstPoComponentList = this.entityManager
                    .createNamedQuery("MstComponent.findByComponentCodeList", MstComponent.class)
                    .setParameter("componentCode", poComponentCodeList).getResultList();
            Map<String, String> mstPoComponentMap = new HashMap<>();
            mstPoComponentList.stream()
                    .forEach(action -> mstPoComponentMap.put(action.getComponentCode(), action.getId()));
            // 発注POテーブルに対し、部品コードを読み替える End

            for (TblPoOutboundVo poOutboundVo : inputData.getTblPoOutboundVoList()) {

                TblPoOutbound poOutbound = poOutboundVo.getPoOutbound();

                if (StringUtils.isEmpty(mstPoComponentMap.get(poOutboundVo.getPoComponentCode()))) {

                    uuidMap.put(poOutbound.getUuid(), poOutbound.getUuid());
                    continue;
                }

                // PO連携の修正 20171226 by penggd Start
                TblPoOutbound oldPoOutbound = this.entityManager
                        .createNamedQuery("TblPoOutbound.findByUniqueKey", TblPoOutbound.class)
                        .setParameter("deliverySrcId", outgoingCompanyId)
                        .setParameter("orderNo", poOutbound.getOrderNo()).setParameter("itemNo", poOutbound.getItemNo())
                        .getResultList().stream().findFirst().orElse(null);
                // PO連携の修正 20171226 by penggd End

                if (oldPoOutbound == null) {// 新規登録

                    poOutbound.setComponentId(mstPoComponentMap.get(poOutboundVo.getPoComponentCode()));
                    poOutbound.setDeliverySrcId(outgoingCompanyId);

                    this.entityManager.persist(poOutbound);

                } else {// 更新

                    oldPoOutbound.setQuantity(poOutbound.getQuantity());
                    oldPoOutbound.setUnitPrice(poOutbound.getUnitPrice());
                    oldPoOutbound.setOrderDate(poOutbound.getOrderDate());
                    oldPoOutbound.setDueDate(poOutbound.getDueDate());
                    oldPoOutbound.setUpdateDate(poOutbound.getUpdateDate());
                    oldPoOutbound.setUpdateUserName(poOutbound.getUpdateUserName());

                    this.entityManager.merge(oldPoOutbound);
                }
            }
        }
        // 発注POテーブルの連携 End

        // 発注PO(tbl_po_outbound)に関連する出荷テーブルの連携 Start
        if (!inputData.getTblShipmentOutboundVoList().isEmpty()) {

            // 発注PO(tbl_po_outbound)に関連する出荷テーブルに対し、部品コードを読み替える Start
            List<String> shipmentComponentCodeList = inputData.getTblShipmentOutboundVoList().stream()
                    .map(mapper -> mapper.getShipmentComponentCode()).collect(Collectors.toCollection(ArrayList::new));

            List<MstComponent> mstShipmentComponentList = this.entityManager
                    .createNamedQuery("MstComponent.findByComponentCodeList", MstComponent.class)
                    .setParameter("componentCode", shipmentComponentCodeList).getResultList();
            Map<String, String> mstShipmentComponentMap = new HashMap<>();
            mstShipmentComponentList.stream()
                    .forEach(action -> mstShipmentComponentMap.put(action.getComponentCode(), action.getId()));
            // 発注PO(tbl_po_outbound)に関連する出荷テーブルに対し、部品コードを読み替える End

            for (TblShipmentOutboundVo shipmentOutboundVo : inputData.getTblShipmentOutboundVoList()) {

                TblShipmentOutbound shipmentOutbound = shipmentOutboundVo.getShipmentOutbound();

                if (StringUtils.isEmpty(mstShipmentComponentMap.get(shipmentOutboundVo.getShipmentComponentCode()))) {

                    uuidMap.put(shipmentOutbound.getUuid(), shipmentOutbound.getUuid());
                    continue;
                }

                TblShipmentOutbound oldShipmentOutbound = this.entityManager
                        .createNamedQuery("TblShipmentOutbound.findByUuid", TblShipmentOutbound.class)
                        .setParameter("uuid", shipmentOutbound.getUuid()).getResultList().stream().findFirst()
                        .orElse(null);

                if (oldShipmentOutbound == null) {// 新規登録

                    // PO連携の修正 20171226 by penggd Start
                    if (null != shipmentOutbound.getTblPoOutbound()) {

                        TblPoOutbound tempPoOutbound = this.entityManager
                                .createNamedQuery("TblPoOutbound.findByUniqueKey", TblPoOutbound.class)
                                .setParameter("deliverySrcId", outgoingCompanyId)
                                .setParameter("orderNo", shipmentOutbound.getTblPoOutbound().getOrderNo()).setParameter("itemNo", shipmentOutbound.getTblPoOutbound().getItemNo())
                                .getResultList().stream().findFirst().orElse(null);

                        if (null != tempPoOutbound) {

                            shipmentOutbound.setPoId(tempPoOutbound.getUuid());
                        }

                    }
                    // PO連携の修正 20171226 by penggd End

                    shipmentOutbound.setComponentId(mstShipmentComponentMap.get(shipmentOutboundVo.getShipmentComponentCode()));
                    shipmentOutbound.setTblPoOutbound(null);

                    this.entityManager.persist(shipmentOutbound);

                } else {// 更新

                    oldShipmentOutbound.setShipDate(shipmentOutbound.getShipDate());
                    oldShipmentOutbound.setQuantity(shipmentOutbound.getQuantity());
                    oldShipmentOutbound.setProductionLotNumber(shipmentOutbound.getProductionLotNumber());
                    oldShipmentOutbound.setUpdateDate(shipmentOutbound.getUpdateDate());
                    oldShipmentOutbound.setUpdateUserName(shipmentOutbound.getUpdateUserName());

                    this.entityManager.merge(oldShipmentOutbound);
                }
            }

        }
        // 発注PO(tbl_po_outbound)に関連する出荷テーブルの連携 End

        return uuidMap;
    }

    /**
     * 発注PO(tbl_po_outbound)に関連する出荷情報を設定する
     *
     * @param shipment
     * @return
     */
    private TblShipmentOutbound getTblShipmentOutbound(TblShipment shipment) {

        TblShipmentOutbound shipmentOutbound = new TblShipmentOutbound();

        shipmentOutbound.setUuid(shipment.getUuid());
        shipmentOutbound.setPoId(shipment.getPoId());
        shipmentOutbound.setShipDate(shipment.getShipDate());
        shipmentOutbound.setQuantity(shipment.getQuantity());
        shipmentOutbound.setProductionLotNumber(shipment.getProductionLotNumber());
        shipmentOutbound.setCreateDate(shipment.getCreateDate());
        shipmentOutbound.setUpdateDate(shipment.getUpdateDate());
        shipmentOutbound.setCreateUserName(getUserName(shipment.getCreateUserUuid()));
        shipmentOutbound.setUpdateUserName(getUserName(shipment.getUpdateUserUuid()));
        // PO連携の修正 20171226 by penggd Start
        if (null != shipment.getTblPo()) {
            shipmentOutbound.setTblPoOutbound(getTblPoOutbound(shipment.getTblPo()));
        }
        // PO連携の修正 20171226 by penggd End

        return shipmentOutbound;

    }

    /**
     * 発注PO情報を設定する
     *
     * @param tblPo
     * @return
     */
    private TblPoOutbound getTblPoOutbound(TblPo tblPo) {

        TblPoOutbound tblPoOutbound = new TblPoOutbound();

        tblPoOutbound.setUuid(tblPo.getUuid());
        tblPoOutbound.setDeliverySrcId(tblPo.getDeliveryDestId());
        tblPoOutbound.setOrderNo(tblPo.getOrderNumber());
        tblPoOutbound.setItemNo(tblPo.getItemNumber());
        tblPoOutbound.setComponentId(tblPo.getComponentId());
        tblPoOutbound.setQuantity(tblPo.getQuantity());
        tblPoOutbound.setUnitPrice(tblPo.getUnitPrice());
        tblPoOutbound.setOrderDate(tblPo.getOrderDate());
        tblPoOutbound.setDueDate(tblPo.getDueDate());
        tblPoOutbound.setCreateDate(tblPo.getCreateDate());
        tblPoOutbound.setUpdateDate(tblPo.getUpdateDate());
        tblPoOutbound.setCreateUserName(getUserName(tblPo.getCreateUserUuid()));
        tblPoOutbound.setUpdateUserName(getUserName(tblPo.getUpdateUserUuid()));

        return tblPoOutbound;

    }

    /**
     * ユーザー名称を取得する
     *
     * @param userUuid
     * @return
     */
    private String getUserName(String userUuid) {

        MstUser mstUser = this.entityManager
                .createNamedQuery("MstUser.findByUserUuid", MstUser.class)
                .setParameter("uuid", userUuid).getResultList().stream().findFirst()
                .orElse(null);

        if (null != mstUser) {

            return mstUser.getUserName();
        }

        return "";

    }

    /**
     * Update tblPo batch update status
     *
     * @param resultIdList
     * @param batchUpdateStatus
     * @return
     */
    @Transactional
    public BasicResponse updatePoBatchUpdateStatus(List<String> resultIdList, String batchUpdateStatus) {
        BasicResponse response = new BasicResponse();
        if (resultIdList == null || resultIdList.isEmpty()) {
            return response;
        }
        this.entityManager.createQuery(
                "UPDATE TblPo t SET t.batchUpdateStatus = :batchUpdateStatus WHERE t.uuid IN :idList")
                .setParameter("idList", resultIdList)
                .setParameter("batchUpdateStatus", batchUpdateStatus)
                .executeUpdate();

        return response;
    }

    /**
     * Update tblShipment batch update status
     *
     * @param resultIdList
     * @param batchUpdateStatus
     * @return
     */
    @Transactional
    public BasicResponse updateShipmentBatchUpdateStatus(List<String> resultIdList, String batchUpdateStatus) {
        BasicResponse response = new BasicResponse();
        if (resultIdList == null || resultIdList.isEmpty()) {
            return response;
        }
        this.entityManager.createQuery(
                "UPDATE TblShipment t SET t.batchUpdateStatus = :batchUpdateStatus WHERE t.uuid IN :idList")
                .setParameter("idList", resultIdList)
                .setParameter("batchUpdateStatus", batchUpdateStatus)
                .executeUpdate();

        return response;
    }
    // KM-463 PO検査間のデータ構造変更 2017/12/5 by penggd End

    /**
     * 起動モード作成
     *
     * @param inputInfo
     * @param loginUser
     * @return
     */
    @Transactional
    public String createBootModelComponentInspectionResult(ComponentInspectionFormCreateInput inputInfo, LoginUser loginUser) {
        Date sysDate = new Date();
        MstCompany selfCompany = this.mstCompanyService.getSelfCompany();
        //部品検査結果追加
        TblComponentInspectionResult tblCompInspResult = new TblComponentInspectionResult();
        String componentInspectionResultId = IDGenerator.generate();
        tblCompInspResult.setId(componentInspectionResultId);
        tblCompInspResult.setComponentInspectionItemsTableId(inputInfo.getComponentInspectionItemsTableId());
        tblCompInspResult.setFirstFlag(inputInfo.getFirstFlag());
        getInspClass(inputInfo.getFirstFlag(), inputInfo.getIncomingCompanyId()).ifPresent(inspClass -> {
            tblCompInspResult.setInspClassDictKey(inspClass.getDictKey());
            tblCompInspResult.setInspectPtn(inspClass.getMassFlg());
        });
        tblCompInspResult.setCavityCnt(inputInfo.getCavityCnt());
        tblCompInspResult.setCavityStartNum(inputInfo.getCavityStartNum());
        tblCompInspResult.setCavityPrefix(FileUtil.getStringValue(inputInfo.getCavityPrefix()));
        tblCompInspResult.setPoNumber(CommonConstants.PO_NUMBER);
        tblCompInspResult.setProductionLotNum(inputInfo.getProductionLotNum());
        tblCompInspResult.setComponentId(inputInfo.getComponentId());
        tblCompInspResult.setOutgoingCompanyId(inputInfo.getIncomingCompanyId());
        tblCompInspResult.setIncomingCompanyId(selfCompany.getId());
        tblCompInspResult.setQuantity(inputInfo.getQuantity());
        tblCompInspResult.setInspectionStatus(CommonConstants.INSPECTION_STATUS_I_INSPECTING);
        tblCompInspResult.setFileConfirmStatus(TblComponentInspectionResult.FileConfirmStatus.DEFAULT);
        tblCompInspResult.setIncomingMeasSamplingQuantity(inputInfo.getIncomingMeasSamplingQuantity());
        tblCompInspResult.setIncomingVisualSamplingQuantity(inputInfo.getIncomingVisualSamplingQuantity());
        tblCompInspResult.setCreateDate(sysDate);
        tblCompInspResult.setCreateUserUuid(loginUser.getUserUuid());
        tblCompInspResult.setUpdateDate(sysDate);
        tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());
        tblCompInspResult.setOutgoingInspectionResult(0);
        tblCompInspResult.setIncomingInspectionResult(0);

        tblCompInspResult.setMaterial01(inputInfo.getMaterial01());
        tblCompInspResult.setMaterial02(inputInfo.getMaterial02());
        tblCompInspResult.setMaterial03(inputInfo.getMaterial03());
  
        tblCompInspResult.setCavityCnt(inputInfo.getCavityCnt());
        tblCompInspResult.setCavityPrefix(inputInfo.getCavityPrefix());
        tblCompInspResult.setCavityStartNum(inputInfo.getCavityStartNum());

        tblCompInspResult.setDataRelationTgt('0');

        this.entityManager.persist(tblCompInspResult);

        //部品検査結果詳細追加
        this.createTblComponentInspectionResultDetail(componentInspectionResultId, inputInfo, loginUser.getUserUuid());
        MstComponentInspectionItemsTable itemsTbl = mstComponentInspectionItemsTableService.findById(tblCompInspResult.getComponentInspectionItemsTableId());
        searchInspFileByPK(itemsTbl.getInspectTypeId(), tblCompInspResult.getFirstFlag(), tblCompInspResult.getIncomingCompanyId()).ifPresent(inspFile -> {
            tblComponentInspectionReferenceFileService.copyFile(inputInfo.getComponentId(), componentInspectionResultId, inspFile, loginUser.getUserUuid(), inputInfo.getIncomingCompanyId());
        });
        
        if(StringUtils.isEmpty(inputInfo.getPoNumber()) || StringUtils.isEmpty(inputInfo.getItemNumber())) {
            return componentInspectionResultId;
        }
        
        //PO/製造ロット存在チェックを行う
        String shipmentPoId = getTblPoOutboundByPoNumberExist(inputInfo.getComponentId(), inputInfo.getPoNumber(),
                inputInfo.getItemNumber(), inputInfo.getProductionLotNum(), inputInfo.getIncomingCompanyId());

        if (StringUtils.isEmpty(shipmentPoId)) {
            // 発注POテーブル追加
            TblPoOutbound tblPoOutbound = new TblPoOutbound();
            String poId = IDGenerator.generate();
            shipmentPoId = poId;
            tblPoOutbound.setUuid(poId);
            tblPoOutbound.setDeliverySrcId(inputInfo.getIncomingCompanyId());
            tblPoOutbound.setOrderNo(inputInfo.getPoNumber());
            tblPoOutbound.setItemNo(inputInfo.getItemNumber());
            tblPoOutbound.setComponentId(inputInfo.getComponentId());
            tblPoOutbound.setUnitPrice(BigDecimal.ZERO);
            tblPoOutbound.setCreateDate(sysDate);
            tblPoOutbound.setUpdateDate(sysDate);
            tblPoOutbound.setCreateUserName(loginUser.getUserUuid());
            tblPoOutbound.setUpdateUserName(loginUser.getUserUuid());
            entityManager.persist(tblPoOutbound);

        }

        //発注POに関連する出荷テーブル追加
        TblShipmentOutbound shipmentOutbound = new TblShipmentOutbound();
        shipmentOutbound.setUuid(IDGenerator.generate());
        shipmentOutbound.setPoId(shipmentPoId);
        shipmentOutbound.setProductionLotNumber(inputInfo.getProductionLotNum());
        shipmentOutbound.setComponentId(inputInfo.getComponentId());
        shipmentOutbound.setCreateDate(sysDate);
        shipmentOutbound.setUpdateDate(sysDate);
        shipmentOutbound.setCreateUserName(loginUser.getUserUuid());
        shipmentOutbound.setUpdateUserName(loginUser.getUserUuid());
        entityManager.persist(shipmentOutbound);

        return componentInspectionResultId;
    }

    /**
     * PO/製造ロット存在チェックを行う
     *
     * @param componentId
     * @param productionLotNumber
     * @param poNumber
     * @param itemNumber
     * @param companyId
     * @return
     */
    public String getTblPoOutboundByPoNumberExist(String componentId, String poNumber, String itemNumber, String productionLotNumber, String companyId) {

        Query query = entityManager.createNamedQuery("TblPoOutbound.findByUniqueKey");

        query.setParameter("deliverySrcId", companyId);
        query.setParameter("orderNo", poNumber);
        query.setParameter("itemNo", itemNumber);

        try {
            TblPoOutbound tblPo = (TblPoOutbound) query.getSingleResult();

            return tblPo.getUuid();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 部品検査結果詳細出荷存在チェックを行う
     *
     * @param componentInspectionResultId
     * @return
     */
    public boolean getInspectionResultDetailByOutgoingExist(String componentInspectionResultId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT t FROM TblComponentInspectionResultDetail t");
        queryBuilder.append(" WHERE t.componentInspectionResultId = :componentInspectionResultId ");
        queryBuilder.append(" AND t.inspectionType = :inspectionType ");

        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("componentInspectionResultId", componentInspectionResultId);
        query.setParameter("inspectionType", CommonConstants.INSPECTION_TYPE_OUTGOING);

        List list = query.getResultList();
        try {
            if (list != null && list.size() > 0) {
                return true;
            }
        } catch (NoResultException e) {
            return false;
        }
        return false;
    }

    /**
     * TblComponentInspectionResultDetail is empty
     *
     * @param componentInspectionResultId
     * @return
     */
    public boolean getInspectionResultDetailEmptyExist(String componentInspectionResultId) {
        List<Integer> inspectionTypeList = new ArrayList<>();
        inspectionTypeList.add(CommonConstants.INSPECTION_TYPE_OUTGOING);
        inspectionTypeList.add(CommonConstants.INSPECTION_TYPE_INCOMING);
        List list = this.entityManager
                .createNamedQuery("TblComponentInspectionResultDetail.findByComponentInspectionResultIdAndInspectionTypeList")
                .setParameter("componentInspectionResultId", componentInspectionResultId)
                .setParameter("inspectionType", inspectionTypeList).getResultList();

        try {
            if (list != null && list.size() > 0) {
                return true;
            }
        } catch (NoResultException e) {
            return false;
        }
        return false;
    }

    /**
     * 検査結果詳細追加/削除抜取数
     *
     * @param inputInfo
     * @param loginUser
     * @return
     */
    @Transactional
    public boolean updateInspectionResultQuantity(ComponentInspectionResultSaveInput inputInfo, LoginUser loginUser) {
        TblComponentInspectionResult tblCompInspResult = this.getTblInspectionItemResult(inputInfo.getComponentInspectionResultId());
        if (tblCompInspResult == null) {
            return false;
        }

        Date sysDate = new Date();
        if (this.isOutgoing(inputInfo.getInspectionType())) {
            //出荷測定抜取数
            int outgoingMeasSamplingQuantity = tblCompInspResult.getOutgoingMeasSamplingQuantity();
            //出荷目視抜取数
            int outgoingVisualSamplingQuantity = tblCompInspResult.getOutgoingVisualSamplingQuantity();
            if (CommonConstants.OPERATION_FLAG_CREATE.equals(inputInfo.getOperationFlag())) {
                if (inputInfo.getMeasurementType() == CommonConstants.MEASUREMENT_TYPE_MEASURE) {
                    outgoingMeasSamplingQuantity++;
                } else if (inputInfo.getMeasurementType() == CommonConstants.MEASUREMENT_TYPE_VISUAL) {
                    outgoingVisualSamplingQuantity++;
                }
                tblCompInspResult.setOutgoingMeasSamplingQuantity(outgoingMeasSamplingQuantity);
                tblCompInspResult.setOutgoingVisualSamplingQuantity(outgoingVisualSamplingQuantity);

            } else if (CommonConstants.OPERATION_FLAG_DELETE.equals(inputInfo.getOperationFlag())) {
                if (inputInfo.getMeasurementType() == CommonConstants.MEASUREMENT_TYPE_MEASURE) {
                    outgoingMeasSamplingQuantity--;
                } else if (inputInfo.getMeasurementType() == CommonConstants.MEASUREMENT_TYPE_VISUAL) {
                    outgoingVisualSamplingQuantity--;
                }
                tblCompInspResult.setOutgoingMeasSamplingQuantity(outgoingMeasSamplingQuantity);
                tblCompInspResult.setOutgoingVisualSamplingQuantity(outgoingVisualSamplingQuantity);
            }

        } else {
            //入荷測定抜取数
            int incomingMeasSamplingQuantity = tblCompInspResult.getIncomingMeasSamplingQuantity();
            //入荷目視抜取数
            int incomingVisualSamplingQuantity = tblCompInspResult.getIncomingVisualSamplingQuantity();
            if (CommonConstants.OPERATION_FLAG_CREATE.equals(inputInfo.getOperationFlag())) {
                if (inputInfo.getMeasurementType() == CommonConstants.MEASUREMENT_TYPE_MEASURE) {
                    incomingMeasSamplingQuantity++;
                } else if (inputInfo.getMeasurementType() == CommonConstants.MEASUREMENT_TYPE_VISUAL) {
                    incomingVisualSamplingQuantity++;
                }
                tblCompInspResult.setIncomingMeasSamplingQuantity(incomingMeasSamplingQuantity);
                tblCompInspResult.setIncomingVisualSamplingQuantity(incomingVisualSamplingQuantity);

            } else if (CommonConstants.OPERATION_FLAG_DELETE.equals(inputInfo.getOperationFlag())) {
                if (inputInfo.getMeasurementType() == CommonConstants.MEASUREMENT_TYPE_MEASURE) {
                    incomingMeasSamplingQuantity--;
                } else if (inputInfo.getMeasurementType() == CommonConstants.MEASUREMENT_TYPE_VISUAL) {
                    incomingVisualSamplingQuantity--;
                }
                tblCompInspResult.setIncomingMeasSamplingQuantity(incomingMeasSamplingQuantity);
                tblCompInspResult.setIncomingVisualSamplingQuantity(incomingVisualSamplingQuantity);
            }
        }
        tblCompInspResult.setUpdateDate(sysDate);
        tblCompInspResult.setUpdateUserUuid(loginUser.getUserUuid());
        this.entityManager.merge(tblCompInspResult);

        this.updateInspectionResultDetailQuantity(inputInfo, loginUser.getUserUuid());

        return true;
    }

    /**
     * 検査結果詳細データ追加/削除
     *
     * @param inputInfo
     * @param userUuid
     */
    private void updateInspectionResultDetailQuantity(ComponentInspectionResultSaveInput inputInfo, String userUuid) {
        String componentInspectionResultId = inputInfo.getComponentInspectionResultId();
        Integer inspectionType = inputInfo.getInspectionType();
        List<ComponentInspectionItemResultDetail> measureResultDetails = inputInfo.getMeasureResultDetails();
        List<ComponentInspectionItemResultDetail> visualResultDetails = inputInfo.getVisualResultDetails();
        List<ComponentInspectionItemResultDetail> resultInputList = new ArrayList<>();
        if (measureResultDetails != null) {
            resultInputList.addAll(measureResultDetails);
        }
        if (visualResultDetails != null) {
            resultInputList.addAll(visualResultDetails);
        }

        Date sysDate = new Date();
        TblComponentInspectionResultDetail tblComInspResultDetail;
        int indexSql = inputInfo.getSeq();
        if (CommonConstants.OPERATION_FLAG_CREATE.equals(inputInfo.getOperationFlag())) {
            for (ComponentInspectionItemResultDetail resultInput : resultInputList) {
                for (int c = inputInfo.getCavityStartNum(); c < (inputInfo.getCavityStartNum() + inputInfo.getCavityCnt()); c++) {
                    tblComInspResultDetail = new TblComponentInspectionResultDetail();
                    tblComInspResultDetail.setId(IDGenerator.generate());
                    tblComInspResultDetail.setComponentInspectionResultId(componentInspectionResultId);
                    tblComInspResultDetail.setInspectionType(inspectionType);
                    tblComInspResultDetail.setInspectionItemSno(resultInput.getInspectionItemSno());
                    tblComInspResultDetail.setCavityNum(c);
                    tblComInspResultDetail.setSeq(indexSql);
                    tblComInspResultDetail.setCreateDate(sysDate);
                    tblComInspResultDetail.setCreateUserUuid(userUuid);
                    tblComInspResultDetail.setUpdateDate(sysDate);
                    tblComInspResultDetail.setUpdateUserUuid(userUuid);
                    this.entityManager.persist(tblComInspResultDetail);
                    //更新itemResultAuto
                    entityManager.createQuery("UPDATE TblComponentInspectionResultDetail t SET "
                            + " t.itemResultAuto = :itemResultAuto,"
                            + " t.updateUserUuid = :updateUserUuid,"
                            + " t.updateDate = :updateDate"
                            + " WHERE t.componentInspectionResultId = :componentInspectionResultId "
                            + " AND t.seq = 0"
                            + " AND t.inspectionType = :inspectionType"
                            + " AND t.inspectionItemSno = :inspectionItemSno")
                            .setParameter("itemResultAuto", resultInput.getItemResultAuto())
                            .setParameter("updateUserUuid", userUuid)
                            .setParameter("updateDate", sysDate)
                            .setParameter("componentInspectionResultId", inputInfo.getComponentInspectionResultId())
                            .setParameter("inspectionType", inputInfo.getInspectionType())
                            .setParameter("inspectionItemSno", resultInput.getInspectionItemSno())
                            .executeUpdate();
                }
            }
        } else if (CommonConstants.OPERATION_FLAG_DELETE.equals(inputInfo.getOperationFlag())) {
            //検査結果詳細データ更新削除
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append(" SELECT t FROM TblComponentInspectionResultDetail t");
            queryBuilder.append(" WHERE t.componentInspectionResultId = :componentInspectionResultId");
            queryBuilder.append(" AND t.inspectionType = :inspectionType");
            queryBuilder.append(" AND t.seq >= :indexSql");
            queryBuilder.append(" AND EXISTS (SELECT m FROM MstComponentInspectionItemsTableDetail m");
            queryBuilder.append(" WHERE m.inspectionItemSno = t.inspectionItemSno");
            queryBuilder.append(" AND m.componentInspectionItemsTableId = :componentInspectionItemsTableId");
            queryBuilder.append(" AND m.measurementType = :measurementType)");
            queryBuilder.append(" ORDER BY t.seq,t.inspectionItemSno");

            Query query = entityManager.createQuery(queryBuilder.toString(), TblComponentInspectionResultDetail.class);

            query.setParameter("componentInspectionResultId", inputInfo.getComponentInspectionResultId());
            query.setParameter("inspectionType", inputInfo.getInspectionType());
            query.setParameter("indexSql", indexSql);
            query.setParameter("componentInspectionItemsTableId", inputInfo.getComponentInspectionItemsTableId());
            query.setParameter("measurementType", inputInfo.getMeasurementType());

            List<TblComponentInspectionResultDetail> list = query.getResultList();
            if (list != null && list.size() > 0) {
                int seq;
                for (TblComponentInspectionResultDetail tblComponentInspectionResultDetail : list) {
                    if (indexSql == tblComponentInspectionResultDetail.getSeq()) {
                        entityManager.createQuery("DELETE FROM TblComponentInspectionResultDetail t WHERE t.id = :id ")
                                .setParameter("id", tblComponentInspectionResultDetail.getId())
                                .executeUpdate();
                    } else {
                        seq = tblComponentInspectionResultDetail.getSeq() - 1;
                        tblComponentInspectionResultDetail.setSeq(seq);
                        tblComponentInspectionResultDetail.setUpdateDate(sysDate);
                        tblComponentInspectionResultDetail.setUpdateUserUuid(userUuid);
                        entityManager.merge(tblComponentInspectionResultDetail);
                        entityManager.flush();
                        entityManager.clear();
                    }
                }
                //測定名称取得
                List<TblComponentInspectionSampleName> sampleNameList
                        = entityManager.createQuery("SELECT t FROM TblComponentInspectionSampleName t"
                        + " WHERE t.componentInspectionResultId = :componentInspectionResultId "
                        + " AND t.inspectionType = :inspectionType"
                        + " AND t.measurementType = :measurementType"
                        + " AND t.seq >= :indexSql")
                        .setParameter("componentInspectionResultId", inputInfo.getComponentInspectionResultId())
                        .setParameter("inspectionType", inputInfo.getInspectionType())
                        .setParameter("measurementType", inputInfo.getMeasurementType())
                        .setParameter("indexSql", indexSql).getResultList();

                if (sampleNameList != null && sampleNameList.size() > 0) {
                    for (TblComponentInspectionSampleName sampleName : sampleNameList) {
                        if (indexSql == sampleName.getSeq()) {
                            //測定名称削除
                            entityManager.createQuery("DELETE FROM TblComponentInspectionSampleName t WHERE t.id = :id ")
                                    .setParameter("id", sampleName.getId())
                                    .executeUpdate();
                        } else {
                            //測定名称seq更新
                            seq = sampleName.getSeq() - 1;
                            sampleName.setSeq(seq);
                            sampleName.setUpdateDate(sysDate);
                            sampleName.setUpdateUserUuid(userUuid);
                            entityManager.merge(sampleName);
                            entityManager.flush();
                            entityManager.clear();
                        }
                    }
                }
            }
            for (ComponentInspectionItemResultDetail resultInput : resultInputList) {
                //更新itemResultAuto
                entityManager.createQuery("UPDATE TblComponentInspectionResultDetail t SET "
                        + " t.itemResultAuto = :itemResultAuto,"
                        + " t.updateUserUuid = :updateUserUuid,"
                        + " t.updateDate = :updateDate"
                        + " WHERE t.componentInspectionResultId = :componentInspectionResultId "
                        + " AND t.seq = 0"
                        + " AND t.inspectionType = :inspectionType"
                        + " AND t.inspectionItemSno = :inspectionItemSno")
                        .setParameter("itemResultAuto", resultInput.getItemResultAuto())
                        .setParameter("updateUserUuid", userUuid)
                        .setParameter("updateDate", sysDate)
                        .setParameter("componentInspectionResultId", inputInfo.getComponentInspectionResultId())
                        .setParameter("inspectionType", inputInfo.getInspectionType())
                        .setParameter("inspectionItemSno", resultInput.getInspectionItemSno())
                        .executeUpdate();
            }
        }
    }

    /**
     * 測定名称変更
     *
     * @param tblComponentInspectionSampleName
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateInspectionResultMeasureName(TblComponentInspectionSampleName tblComponentInspectionSampleName, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(" SELECT t FROM TblComponentInspectionSampleName t");
        queryBuilder.append(" WHERE t.componentInspectionResultId = :componentInspectionResultId ");
        queryBuilder.append(" AND t.inspectionType = :inspectionType ");
        queryBuilder.append(" AND t.measurementType = :measurementType ");
        queryBuilder.append(" AND t.seq = :seq ");
        queryBuilder.append(" AND t.cavityNum = :cavityNum ");

        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("componentInspectionResultId", tblComponentInspectionSampleName.getComponentInspectionResultId());
        query.setParameter("inspectionType", tblComponentInspectionSampleName.getInspectionType());
        query.setParameter("measurementType", tblComponentInspectionSampleName.getMeasurementType());
        query.setParameter("seq", tblComponentInspectionSampleName.getSeq());
        query.setParameter("cavityNum", tblComponentInspectionSampleName.getCavityNum());
        List list = query.getResultList();

        Date sysDate = new Date();
        if (list != null && list.size() > 0) {
            TblComponentInspectionSampleName sampleName = (TblComponentInspectionSampleName) list.get(0);
            sampleName.setName(tblComponentInspectionSampleName.getName());
            sampleName.setUpdateDate(sysDate);
            sampleName.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.persist(sampleName);
        } else {
            tblComponentInspectionSampleName.setId(IDGenerator.generate());
            tblComponentInspectionSampleName.setCreateDate(sysDate);
            tblComponentInspectionSampleName.setCreateUserUuid(loginUser.getUserUuid());
            tblComponentInspectionSampleName.setUpdateDate(sysDate);
            tblComponentInspectionSampleName.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.persist(tblComponentInspectionSampleName);
        }
        return response;
    }

    /**
     * 測定データ参照
     *
     * @param componentCode
     * @param productionLotNum
     * @param inspectionType
     * @param langId
     * @return
     */
    public ComponentInspectionResultResp getMeasurementDateSearch(String componentCode, String productionLotNum, Integer inspectionType, String langId) {
        ComponentInspectionResultResp componentInspectionResultResp = new ComponentInspectionResultResp();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t FROM TblComponentInspectionResult t");
        queryBuilder.append(" JOIN FETCH t.mstComponent ");
        queryBuilder.append(" WHERE 1=1");
        if (StringUtils.isNotEmpty(componentCode)) {
            queryBuilder.append(" AND t.mstComponent.componentCode LIKE :componentCode");
        }
        if (StringUtils.isNotEmpty(productionLotNum)) {
            queryBuilder.append(" AND t.productionLotNum LIKE :productionLotNum");
        }
        queryBuilder.append(" AND EXISTS (SELECT m FROM TblComponentInspectionResultDetail m");
        queryBuilder.append(" WHERE m.componentInspectionResultId = t.id AND m.inspectionType = :inspectionType)");
        queryBuilder.append(" ORDER BY t.mstComponent.componentCode, t.createDate DESC");

        Query query = this.entityManager.createQuery(
                queryBuilder.toString(), TblComponentInspectionResult.class);

        if (StringUtils.isNotEmpty(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode + "%");
        }
        if (StringUtils.isNotEmpty(productionLotNum)) {
            query.setParameter("productionLotNum", "%" + productionLotNum + "%");
        }
        query.setParameter("inspectionType", inspectionType);
        List<TblComponentInspectionResult> resultList = query.getResultList();

        List<ComponentInspectionResultInfo> infoList = new ArrayList<>();
        if (resultList != null && resultList.size() > 0) {
            resultList.stream().forEach(result -> {
                ComponentInspectionResultInfo resultInfo = new ComponentInspectionResultInfo();
                resultInfo.setComponentInspectionItemsTableId(result.getComponentInspectionItemsTableId());
                resultInfo.setComponentId(result.getComponentId());
                resultInfo.setComponentInspectionResultId(result.getId());
                resultInfo.setComponentCode(result.getMstComponent().getComponentCode());
                resultInfo.setComponentName(result.getMstComponent().getComponentName());
                resultInfo.setProductionLotNum(result.getProductionLotNum());
                resultInfo.setFirstFlagName("");
                resultInfo.setCavityCnt(result.getCavityCnt());
                resultInfo.setCavityStartNum(result.getCavityStartNum());
                resultInfo.setCavityPrefix(result.getCavityPrefix());
                getInspClass(result.getFirstFlag(), result.getIncomingCompanyId()).ifPresent(inspClass -> {
                    resultInfo.setFirstFlagName(mstComponentInspectFileService.getInspDictVal(langId, inspClass.getDictKey()));
                });
                
                // outgoing
                if (StringUtils.isNotEmpty(result.getOutgoingInspectionPersonName())) {
                    resultInfo.setOutgoingInspectionPersonName(result.getOutgoingInspectionPersonName());
                } else {
                    resultInfo.setOutgoingInspectionPersonName("");
                }
                if (result.getOutgoingInspectionDate() != null) {
                    resultInfo.setOutgoingInspectionDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(result.getOutgoingInspectionDate()));
                } else {
                    resultInfo.setOutgoingInspectionDate("");
                }
                // incoming
                if (StringUtils.isNotEmpty(result.getIncomingInspectionPersonName())) {
                    resultInfo.setIncomingInspectionPersonName(result.getIncomingInspectionPersonName());
                } else {
                    resultInfo.setIncomingInspectionPersonName("");
                }
                if (result.getIncomingInspectionDate() != null) {
                    resultInfo.setIncomingInspectionDate(FileUtil.getDateTimeFormatYYYYMMDDHHMMStr(result.getIncomingInspectionDate()));
                } else {
                    resultInfo.setIncomingInspectionDate("");
                }
                infoList.add(resultInfo);
            });
        }
        componentInspectionResultResp.setInspectionResultList(infoList);
        return componentInspectionResultResp;
    }

    /**
     * 検査結果の帳票をExcelで出力する
     *
     * @param inspectionResultId
     * @param inspectionType
     * @param langId
     * @return
     */
    public Response getInspectionResultDownloadExcel(String inspectionResultId, Integer inspectionType, String langId) {
        //ファイル名、ファイルの作成,フォルダ存在判定、存在してないの場合、新規作成する。
        String docDirPath = kartePropertyService.getDocumentDirectory();
        StringBuilder templateFilePath = new StringBuilder();
        templateFilePath.append(docDirPath);
        templateFilePath.append(FileUtil.SEPARATOR);
        templateFilePath.append(TEMPLATE_FOLDER);
        templateFilePath.append(FileUtil.SEPARATOR);
        if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType) {
            templateFilePath.append(TEMPLATE_FILE_OUTGOING);
        } else {
            templateFilePath.append(TEMPLATE_FILE_INCOMING);
        }

        ComponentInspectionResultInfo componentInspectionResultInfo = getDownloadExcelInspectionItemDetails(inspectionResultId, inspectionType, langId);

        Map<String, String> choiceMap = mstChoiceService.getChoiceMap(langId,
                new String[]{"tbl_component_inspection_result_detail.item_result", "mst_component.component_type", "tbl_component_inspection_result.result_type"});

        TblComponentInspectionResultTemplate tblComponentInspectionResultTemplate = new TblComponentInspectionResultTemplate();

        try {
            //検査結果の帳票を作成する
            return tblComponentInspectionResultTemplate.write(componentInspectionResultInfo, templateFilePath.toString(), inspectionType, choiceMap, docDirPath);
        } catch (IOException e) {
            // nothing
            Logger.getLogger(TblComponentInspectionResultService.class.getName()).log(Level.SEVERE, null, e);
            Response.ResponseBuilder response = Response.status(500);
            return response.build();
        }
    }

    public ComponentInspectionResultInfo getDownloadExcelInspectionItemDetails(String inspectionResultId, Integer inspectionType, String langId) {
        // select TblComponentInspectionResult
        TblComponentInspectionResult inspectionResult = this.getTblInspectionItemResult(inspectionResultId);
        if (inspectionResult == null) {
            return null;
        }

        ComponentInspectionResultInfo resultInfo = new ComponentInspectionResultInfo();
        resultInfo.setComponentInspectionItemsTableId(inspectionResult.getComponentInspectionItemsTableId());
        resultInfo.setComponentId(inspectionResult.getComponentId());
        resultInfo.setComponentInspectionResultId(inspectionResult.getId());
        resultInfo.setComponentCode(inspectionResult.getMstComponent().getComponentCode());
        resultInfo.setComponentName(inspectionResult.getMstComponent().getComponentName());
        resultInfo.setComponentType(inspectionResult.getMstComponent().getComponentType());
        resultInfo.setFirstFlag(inspectionResult.getFirstFlag());
        // マルチCAV型を追加
        resultInfo.setCavityCnt(inspectionResult.getCavityCnt());
        resultInfo.setCavityStartNum(inspectionResult.getCavityStartNum());
        resultInfo.setCavityPrefix(inspectionResult.getCavityPrefix());

        String poNumber = "";
        String itemNumber = "";
        if (StringUtils.isNotEmpty(inspectionResult.getProductionLotNum()) && !CommonConstants.PRODUCTION_LOT_NUMBER.equals(inspectionResult.getProductionLotNum())) {
            TblComponentInspectionResultPoList poList = this.getTblPoOutbound(
                    inspectionResult.getComponentId(),
                    inspectionResult.getProductionLotNum(),
                    inspectionResult.getIncomingCompanyId(),
                    inspectionResult.getOutgoingCompanyId());

            if (poList != null && poList.getTblComponentInspectionResultPoVos() != null && poList.getTblComponentInspectionResultPoVos().size() > 0) {
                for (TblComponentInspectionResultPoVo poVo : poList.getTblComponentInspectionResultPoVos()) {
                    poNumber += poVo.getOrderNumber().concat("-").concat(poVo.getItemNumber()) + ",";
                    itemNumber += poVo.getItemNumber() + "\n";
                }
                poNumber = poNumber.substring(0, poNumber.length() - 1);
            }
            resultInfo.setProductionLotNum(inspectionResult.getProductionLotNum());
        } else {
            resultInfo.setProductionLotNum("");
        }
        
        getInspClass(inspectionResult.getFirstFlag(), inspectionResult.getIncomingCompanyId()).ifPresent(inspClass -> {
            resultInfo.setFirstFlagName(mstComponentInspectFileService.getInspDictVal(langId, inspClass.getDictKey()));
        });
        resultInfo.setPoNumber(poNumber);
        resultInfo.setItemNumber(itemNumber);

        resultInfo.setQuantity(inspectionResult.getQuantity());
        resultInfo.setIncomingCompanyName(inspectionResult.getMstCompanyIncoming().getCompanyName());
        resultInfo.setOutgoingCompanyName(inspectionResult.getMstCompanyOutgoing().getCompanyName());
        resultInfo.setIncomingCompanyCode(inspectionResult.getMstCompanyIncoming().getCompanyCode());
        resultInfo.setOutgoingCompanyCode(inspectionResult.getMstCompanyOutgoing().getCompanyCode());
        resultInfo.setOutgoingCompanyId(inspectionResult.getOutgoingCompanyId());
        resultInfo.setIncomingCompanyId(inspectionResult.getIncomingCompanyId());
        if (StringUtils.isNotEmpty(inspectionResult.getInspBatchUpdateStatus())) {
            resultInfo.setInspBatchUpdateStatus(inspectionResult.getInspBatchUpdateStatus());
        } else {
            resultInfo.setInspBatchUpdateStatus("");
        }
        resultInfo.setInspectionStatus(inspectionResult.getInspectionStatus());
        // outgoing
        resultInfo.setOutgoingMeasSamplingQuantity(inspectionResult.getOutgoingMeasSamplingQuantity());
        resultInfo.setOutgoingVisualSamplingQuantity(inspectionResult.getOutgoingVisualSamplingQuantity());
        if (StringUtils.isNotEmpty(inspectionResult.getOutgoingInspectionPersonName())) {
            resultInfo.setOutgoingInspectionPersonName(inspectionResult.getOutgoingInspectionPersonName());
        } else {
            resultInfo.setOutgoingInspectionPersonName("");
        }
        if (inspectionResult.getOutgoingInspectionDate() != null) {
            resultInfo.setOutgoingInspectionDate(FileUtil.dateFormat(inspectionResult.getOutgoingInspectionDate()));
        } else {
            resultInfo.setOutgoingInspectionDate("");
        }
        resultInfo.setOutgoingInspectionResult(inspectionResult.getOutgoingInspectionResult());
        if (StringUtils.isNotEmpty(inspectionResult.getOutgoingInspectionApprovePersonName())) {
            resultInfo.setOutgoingInspectionApprovePersonName(inspectionResult.getOutgoingInspectionApprovePersonName());
        } else {
            resultInfo.setOutgoingInspectionApprovePersonName("");
        }
        if (inspectionResult.getOutgoingInspectionApproveDate() != null) {
            resultInfo.setOutgoingInspectionApproveDate(FileUtil.dateFormat(inspectionResult.getOutgoingInspectionApproveDate()));
        } else {
            resultInfo.setOutgoingInspectionApproveDate("");
        }
        if (StringUtils.isNotEmpty(inspectionResult.getOutgoingInspectionComment())) {
            resultInfo.setOutgoingInspectionComment(inspectionResult.getOutgoingInspectionComment());
        } else {
            resultInfo.setOutgoingInspectionComment("");
        }
        resultInfo.setOutgoingConfirmerUuid(inspectionResult.getOutgoingConfirmerUuid());
        if (StringUtils.isNotEmpty(inspectionResult.getOutgoingConfirmerName())) {
            resultInfo.setOutgoingConfirmerName(inspectionResult.getOutgoingConfirmerName());
        } else {
            resultInfo.setOutgoingConfirmerName("");
        }
        if (StringUtils.isNotEmpty(inspectionResult.getOutgoingPrivateComment())) {
            resultInfo.setOutgoingPrivateComment(inspectionResult.getOutgoingPrivateComment());
        } else {
            resultInfo.setOutgoingPrivateComment("");
        }
        if (inspectionResult.getOutgoingConfirmDate() != null) {
            resultInfo.setOutgoingConfirmDate(FileUtil.dateFormat(inspectionResult.getOutgoingConfirmDate()));
        } else {
            resultInfo.setOutgoingConfirmDate("");
        }
        // acceptance
        if (StringUtils.isNotEmpty(inspectionResult.getAcceptancePersonName())) {
            resultInfo.setAcceptancePersonName(inspectionResult.getAcceptancePersonName());
        } else {
            resultInfo.setAcceptancePersonName("");
        }
        if (inspectionResult.getAcceptanceDate() != null) {
            resultInfo.setAcceptanceDate(FileUtil.dateFormat(inspectionResult.getAcceptanceDate()));
        } else {
            resultInfo.setAcceptanceDate("");
        }
        if (StringUtils.isNotEmpty(inspectionResult.getExemptionApprovePersonName())) {
            resultInfo.setExemptionApprovePersonName(inspectionResult.getExemptionApprovePersonName());
        } else {
            resultInfo.setExemptionApprovePersonName("");
        }
        if (inspectionResult.getExemptionApproveDate() != null) {
            resultInfo.setExemptionApproveDate(FileUtil.dateFormat(inspectionResult.getExemptionApproveDate()));
        } else {
            resultInfo.setExemptionApproveDate("");
        }
        // incoming
        resultInfo.setIncomingMeasSamplingQuantity(inspectionResult.getIncomingMeasSamplingQuantity());
        resultInfo.setIncomingVisualSamplingQuantity(inspectionResult.getIncomingVisualSamplingQuantity());
        if (StringUtils.isNotEmpty(inspectionResult.getIncomingInspectionPersonName())) {
            resultInfo.setIncomingInspectionPersonName(inspectionResult.getIncomingInspectionPersonName());
        } else {
            resultInfo.setIncomingInspectionPersonName("");
        }
        if (inspectionResult.getIncomingInspectionDate() != null) {
            resultInfo.setIncomingInspectionDate(FileUtil.dateFormat(inspectionResult.getIncomingInspectionDate()));
        } else {
            resultInfo.setIncomingInspectionDate("");
        }
        resultInfo.setIncomingInspectionResult(inspectionResult.getIncomingInspectionResult());
        if (StringUtils.isNotEmpty(inspectionResult.getIncomingInspectionApprovePersonName())) {
            resultInfo.setIncomingInspectionApprovePersonName(inspectionResult.getIncomingInspectionApprovePersonName());
        } else {
            resultInfo.setIncomingInspectionApprovePersonName("");
        }
        if (inspectionResult.getIncomingInspectionApproveDate() != null) {
            resultInfo.setIncomingInspectionApproveDate(FileUtil.dateFormat(inspectionResult.getIncomingInspectionApproveDate()));
        } else {
            resultInfo.setIncomingInspectionApproveDate("");
        }
        if (StringUtils.isNotEmpty(inspectionResult.getIncomingInspectionComment())) {
            resultInfo.setIncomingInspectionComment(inspectionResult.getIncomingInspectionComment());
        } else {
            resultInfo.setIncomingInspectionComment("");
        }
        resultInfo.setIncomingConfirmerUuid(inspectionResult.getIncomingConfirmerUuid());
        if (StringUtils.isNotEmpty(inspectionResult.getIncomingConfirmerName())) {
            resultInfo.setIncomingConfirmerName(inspectionResult.getIncomingConfirmerName());
        } else {
            resultInfo.setIncomingConfirmerName("");
        }
        if (StringUtils.isNotEmpty(inspectionResult.getIncomingPrivateComment())) {
            resultInfo.setIncomingPrivateComment(inspectionResult.getIncomingPrivateComment());
        } else {
            resultInfo.setIncomingPrivateComment("");
        }
        if (inspectionResult.getIncomingConfirmDate() != null) {
            resultInfo.setIncomingConfirmDate(FileUtil.dateFormat(inspectionResult.getIncomingConfirmDate()));
        } else {
            resultInfo.setIncomingConfirmDate("");
        }
        resultInfo.setDataRelationTgt(inspectionResult.getDataRelationTgt().toString());

        resultInfo.setMaterial01(inspectionResult.getMaterial01());
        resultInfo.setMaterial02(inspectionResult.getMaterial02());
        resultInfo.setMaterial03(inspectionResult.getMaterial03());

        List<Integer> inspectionTypeList = new ArrayList<>();
        inspectionTypeList.add(inspectionType);

        // select MstComponentInspectionItemsTableDetail
        Map<String, MstComponentInspectionItemsTableDetail> inspectionItemsTableDetailMap
                = this.mstComponentInspectionItemsTableService.getMstCompInspItemsTableDetailsMap(resultInfo.getComponentInspectionItemsTableId());
        if (inspectionItemsTableDetailMap.isEmpty()) {
            return null;
        }

        // select TblComponentInspectionResultDetail
        List<TblComponentInspectionResultDetail> tblResultDetails = this.entityManager
                .createNamedQuery("TblComponentInspectionResultDetail.findByComponentInspectionResultIdAndInspectionTypeList",
                        TblComponentInspectionResultDetail.class)
                .setParameter("componentInspectionResultId", inspectionResultId)
                .setParameter("inspectionType", inspectionTypeList).getResultList();

        Map<String, List<TblComponentInspectionResultDetail>> tblResultDetailMa = this.convertToTblResultMap(tblResultDetails, inspectionItemsTableDetailMap);
        this.setDownloadExcelInspectionResultDetailList(resultInfo, tblResultDetailMa, inspectionItemsTableDetailMap);

        return resultInfo;
    }

    private void setDownloadExcelInspectionResultDetailList(
            ComponentInspectionResultInfo inspectionResultInfo,
            Map<String, List<TblComponentInspectionResultDetail>> tableResultDetailMap,
            Map<String, MstComponentInspectionItemsTableDetail> inspectionItemsTableDetailMap
    ) {

        List<ComponentInspectionItemResultDetail> outgoingMeasureList = new ArrayList<>();
        List<ComponentInspectionItemResultDetail> outgoingVisualList = new ArrayList<>();
        List<ComponentInspectionItemResultDetail> incomingMeasureList = new ArrayList<>();
        List<ComponentInspectionItemResultDetail> incomingVisualList = new ArrayList<>();
        for (Entry<String, List<TblComponentInspectionResultDetail>> entry : tableResultDetailMap.entrySet()) {
            String mapKey = entry.getKey();
            List<TblComponentInspectionResultDetail> tableResultDetailList = entry.getValue();

            String[] keyArray = mapKey.split(SPLIT_REGEX);
            int inspectionType = Integer.valueOf(keyArray[0]);

            ComponentInspectionItemResultDetail oneInspectionItemResult = new ComponentInspectionItemResultDetail();
            List<SamplingInspectionResult> samplingResultList = new ArrayList<>();
            List<SamplingInspectionOutgoingResult> samplingOutgoingResultList = new ArrayList<>();
            List<SeqInspectionOutgoingResult> seqInspectionOutgoingResultList = new ArrayList<>();
            tableResultDetailList.stream().forEach(tblData -> {
                if (tblData.getSeq() == 0) {
                    MstComponentInspectionItemsTableDetail mstDetail = inspectionItemsTableDetailMap.get(tblData.getInspectionItemSno());
                    oneInspectionItemResult.setInspectionResultDetailId(tblData.getId());
                    oneInspectionItemResult.setInspectionItemSno(tblData.getInspectionItemSno());
                    oneInspectionItemResult.setInspectionType(tblData.getInspectionType());
                    oneInspectionItemResult.setNote(tblData.getNote());
                    // 追加列
                    oneInspectionItemResult.setRevisionSymbol(mstDetail.getRevisionSymbol());
                    oneInspectionItemResult.setDrawingPage(mstDetail.getDrawingPage());
                    oneInspectionItemResult.setDrawingAnnotation(mstDetail.getDrawingAnnotation());
                    oneInspectionItemResult.setDrawingMentionNo(mstDetail.getDrawingMentionNo());
                    oneInspectionItemResult.setSimilarMultiitem(mstDetail.getSimilarMultiitem());
                    oneInspectionItemResult.setDrawingArea(mstDetail.getDrawingArea());
                    oneInspectionItemResult.setPqs(mstDetail.getPqs());

                    oneInspectionItemResult.setInspectionItemName(mstDetail.getInspectionItemName());
                    oneInspectionItemResult.setMeasurementType(mstDetail.getMeasurementType());
                    oneInspectionItemResult.setMeasurementMethod(mstDetail.getMeasurementMethod());
                    oneInspectionItemResult.setDimensionValue(mstDetail.getDimensionValue());
                    oneInspectionItemResult.setTolerancePlus(mstDetail.getTolerancePlus());
                    oneInspectionItemResult.setToleranceMinus(mstDetail.getToleranceMinus());
                    oneInspectionItemResult.setItemResult(tblData.getItemResult());

                    oneInspectionItemResult.setLocalSeq(mstDetail.getLocalSeq());
                    oneInspectionItemResult.setItemResultAuto(tblData.getItemResultAuto());
                    oneInspectionItemResult.setItemResultManual(tblData.getItemResultManual());//手動判定
                    oneInspectionItemResult.setManJudgeComment(tblData.getManJudgeComment());//手動判定コメント
                    //Apeng 20180208 add start
                    oneInspectionItemResult.setItemTableDetailAdditionalFlg(mstDetail.getAdditionalFlg().toString());
                    //Apeng 20180208 add end

                } else {
                    SamplingInspectionResult samplingResult = new SamplingInspectionResult();
                    samplingResult.setSeq(tblData.getSeq());
                    samplingResult.setCavityNum(tblData.getCavityNum());
                    samplingResult.setSeqMeasurementResult(tblData.getSeqMeasurementResult());
                    samplingResult.setSeqVisualResult(tblData.getSeqVisualResult());
                    samplingResultList.add(samplingResult);
                }
            });
            oneInspectionItemResult.setSamplingInspectionResults(samplingResultList);
            oneInspectionItemResult.setSamplingInspectionOutgoingResults(samplingOutgoingResultList);
            oneInspectionItemResult.setSeqInspectionOutgoingResults(seqInspectionOutgoingResultList);

            if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType && !oneInspectionItemResult.isVisualSpection()) {
                outgoingMeasureList.add(oneInspectionItemResult);
            } else if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType && oneInspectionItemResult.isVisualSpection()) {
                outgoingVisualList.add(oneInspectionItemResult);
            } else if (CommonConstants.INSPECTION_TYPE_INCOMING == inspectionType && !oneInspectionItemResult.isVisualSpection()) {
                incomingMeasureList.add(oneInspectionItemResult);
            } else {
                incomingVisualList.add(oneInspectionItemResult);
            }
        }

        // set result detials list
        if (!outgoingMeasureList.isEmpty()) {
            //昇順
            if (outgoingMeasureList.size() > 1) {
                Collections.sort(outgoingMeasureList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        ComponentInspectionItemResultDetail vo1 = (ComponentInspectionItemResultDetail) o1;
                        ComponentInspectionItemResultDetail vo2 = (ComponentInspectionItemResultDetail) o2;
                        //検査項目番号
                        String inspectionItemSno1 = vo1.getInspectionItemSno();
                        String inspectionItemSno2 = vo2.getInspectionItemSno();
                        if (vo1.getLocalSeq() == 0) {
                            return inspectionItemSno1.compareTo(inspectionItemSno2);
                        } else {
                            return vo1.getLocalSeq().compareTo(vo2.getLocalSeq());
                        }
                    }
                });
            }
            inspectionResultInfo.setOutgoingMeasureResultDetails(outgoingMeasureList);
        }
        if (!outgoingVisualList.isEmpty()) {
            //昇順
            if (outgoingVisualList.size() > 1) {
                Collections.sort(outgoingVisualList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        ComponentInspectionItemResultDetail vo1 = (ComponentInspectionItemResultDetail) o1;
                        ComponentInspectionItemResultDetail vo2 = (ComponentInspectionItemResultDetail) o2;
                        //検査項目番号
                        String inspectionItemSno1 = vo1.getInspectionItemSno();
                        String inspectionItemSno2 = vo2.getInspectionItemSno();
                        if (vo1.getLocalSeq() == 0) {
                            return inspectionItemSno1.compareTo(inspectionItemSno2);
                        } else {
                            return vo1.getLocalSeq().compareTo(vo2.getLocalSeq());
                        }
                    }
                });
            }
            inspectionResultInfo.setOutgoingVisualResultDetails(outgoingVisualList);
        }
        if (!incomingMeasureList.isEmpty()) {
            //昇順
            if (incomingMeasureList.size() > 1) {
                Collections.sort(incomingMeasureList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        ComponentInspectionItemResultDetail vo1 = (ComponentInspectionItemResultDetail) o1;
                        ComponentInspectionItemResultDetail vo2 = (ComponentInspectionItemResultDetail) o2;
                        //検査項目番号
                        String inspectionItemSno1 = vo1.getInspectionItemSno();
                        String inspectionItemSno2 = vo2.getInspectionItemSno();
                        if (vo1.getLocalSeq() == 0) {
                            return inspectionItemSno1.compareTo(inspectionItemSno2);
                        } else {
                            return vo1.getLocalSeq().compareTo(vo2.getLocalSeq());
                        }
                    }
                });
            }
            inspectionResultInfo.setIncomingMeasureResultDetails(incomingMeasureList);
        }
        if (!incomingVisualList.isEmpty()) {
            //昇順
            if (incomingVisualList.size() > 1) {
                Collections.sort(incomingVisualList, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        ComponentInspectionItemResultDetail vo1 = (ComponentInspectionItemResultDetail) o1;
                        ComponentInspectionItemResultDetail vo2 = (ComponentInspectionItemResultDetail) o2;
                        //検査項目番号
                        String inspectionItemSno1 = vo1.getInspectionItemSno();
                        String inspectionItemSno2 = vo2.getInspectionItemSno();
                        if (vo1.getLocalSeq() == 0) {
                            return inspectionItemSno1.compareTo(inspectionItemSno2);
                        } else {
                            return vo1.getLocalSeq().compareTo(vo2.getLocalSeq());
                        }
                    }
                });
            }
            inspectionResultInfo.setIncomingVisualResultDetails(incomingVisualList);
        }
    }

    /**
     * 検査結果対象は複数の場合、圧縮処理を行う
     *
     * @param componentInspectionFormCreateInput
     * @param langId
     * @return
     */
    public FileReponse getInspectionResultDownloadZip(ComponentInspectionFormCreateInput componentInspectionFormCreateInput, String langId) {
        FileReponse reponse = new FileReponse();
        String fileUuid = IDGenerator.generate();
        if (componentInspectionFormCreateInput == null) {
            reponse.setError(true);
            reponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            reponse.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "mst_error_record_not_found"));
            return reponse;
        }
        Integer inspectionType = componentInspectionFormCreateInput.getInspectionType();
        //ファイル名、ファイルの作成,フォルダ存在判定、存在してないの場合、新規作成する。
        String folderPath = kartePropertyService.getDocumentDirectory();
        StringBuilder templateFilePath = new StringBuilder();
        templateFilePath.append(folderPath);
        templateFilePath.append(FileUtil.SEPARATOR);
        templateFilePath.append(TEMPLATE_FOLDER);
        templateFilePath.append(FileUtil.SEPARATOR);
        if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType) {
            templateFilePath.append(TEMPLATE_FILE_OUTGOING);
        } else {
            templateFilePath.append(TEMPLATE_FILE_INCOMING);
        }
        templateFilePath.append(FileUtil.SEPARATOR);

        StringBuilder zipFilePath = new StringBuilder();
        zipFilePath.append(folderPath);
        zipFilePath.append(FileUtil.SEPARATOR);
        zipFilePath.append(CommonConstants.WORK);
        zipFilePath.append(FileUtil.SEPARATOR);
        zipFilePath.append(CommonConstants.INSPECTION_REPORT);
        zipFilePath.append(FileUtil.SEPARATOR);
        zipFilePath.append(fileUuid);
        zipFilePath.append(FileUtil.SEPARATOR);

        Map<String, String> choiceMap = mstChoiceService.getChoiceMap(langId,
                new String[]{"tbl_component_inspection_result_detail.item_result", "mst_component.component_type", "tbl_component_inspection_result.result_type"});
        String[] strArray = componentInspectionFormCreateInput.getComponentInspectionResultId().split(",");
        for (int i = 0; i < strArray.length; i++) {
            StringBuilder fileName = new StringBuilder(zipFilePath.toString());
            String inspectionResultId = strArray[i];

            try {
                // 検査結果ごとにループし依頼票ファイルを作成
                ComponentInspectionResultInfo componentInspectionResultInfo = getDownloadExcelInspectionItemDetails(inspectionResultId, inspectionType, langId);

                //連番_inspect_result_%部品コード%.xlsx
                if (i < 10) {
                    fileName.append("0");
                }
                fileName.append(i + 1);
                fileName.append(UNDER_YOKO);
                fileName.append(INSPECT_RESULT);
                fileName.append(componentInspectionResultInfo.getComponentCode());
                fileName.append(CommonConstants.EXT_EXCEL);
                //ファイルをコピーしてReNameする
                FileUtil.fileChannelCopy(templateFilePath.toString(), fileName.toString(), false);
                //検査結果の帳票を作成する
                TblComponentInspectionResultTemplate tblComponentInspectionResultTemplate = new TblComponentInspectionResultTemplate();

                tblComponentInspectionResultTemplate.writeExcel(componentInspectionResultInfo, fileName.toString(), inspectionType, choiceMap);
            } catch (IOException e) {
                // nothing
                reponse.setError(true);
                reponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                reponse.setErrorMessage(e.getMessage());
                return reponse;
            }
        }

        //検査結果対象は複数の場合、圧縮処理を行う,ファイル圧縮
        // ZIPファイル名：inspect_results.zipとする
        boolean charsetFlg = false;
        if (LANGID.equalsIgnoreCase(langId)) {
            charsetFlg = true;
        }

        ZipCompressor.zipDirectory(zipFilePath.toString(), fileUuid, charsetFlg);

        reponse.setFileUuid(fileUuid);
        return reponse;
    }

    public List<ComponentInspectionItemsFileDetail> getInspectionResultFile(String componentInspectionResultId) {
        List<ComponentInspectionItemsFileDetail> itemFileDetails = new ArrayList<>();

        List<TblComponentInspectionResultFile> resultFileList = this.entityManager
                .createNamedQuery("TblComponentInspectionResultFile.findByComponentInspectionResultId", TblComponentInspectionResultFile.class)
                .setParameter("componentInspectionResultId", componentInspectionResultId)
                .getResultList();

        resultFileList.stream().forEach(resultFileDetail -> {
            TblUploadFile fileUpload = this.entityManager
                    .createNamedQuery("TblUploadFile.findByFileUuid", TblUploadFile.class)
                    .setParameter("fileUuid", resultFileDetail.getFileUuid())
                    .getResultList().stream().findFirst().orElse(null);
            ComponentInspectionItemsFileDetail itemFileDetail = new ComponentInspectionItemsFileDetail();
            itemFileDetail.setId(resultFileDetail.getId());
            itemFileDetail.setFileUuid(resultFileDetail.getFileUuid());

            if (fileUpload != null) {
                itemFileDetail.setUploadDate(fileUpload.getUploadDate());
                itemFileDetail.setFileName(fileUpload.getUploadFileName());
            }
            itemFileDetails.add(itemFileDetail);
        });
        return itemFileDetails;
    }

    /**
     * Load setting inspection columns
     * @param detailColumnInputs
     * @param userUuid
     */
    @Transactional
    protected void createTblGridColumnMemory(List<ComponentInspectionDetailColumnInput> detailColumnInputs, String userUuid) {
        detailColumnInputs.stream().forEach(inspectionItemDetail -> {
            TblGridColumnMemory tblColumnMemory = new TblGridColumnMemory();
            tblColumnMemory.setUserId(userUuid);
            tblColumnMemory.setColumnId(inspectionItemDetail.getColumnId());
            tblColumnMemory.setGridId(inspectionItemDetail.getGridId());
            tblColumnMemory.setScreenId(inspectionItemDetail.getScreenId());
            tblColumnMemory.setColWidth(inspectionItemDetail.getColWidth());
            tblColumnMemory.setColOrder(inspectionItemDetail.getColOrder());
            this.entityManager.persist(tblColumnMemory);
        });
    }

    /**
     * Get list setting columns exists
     * @param detailColumnInputs
     * @param userUuid
     * @return
     */
    public List<String> getListColumnGridExists(List<ComponentInspectionDetailColumnInput> detailColumnInputs, String userUuid) {
        List<String> listColumns = new ArrayList<String>();
        for (ComponentInspectionDetailColumnInput itemColumns : detailColumnInputs) {
            List<TblGridColumnMemory> listGridColumns = this.entityManager
                    .createNamedQuery("TblGridColumnMemory.findByGridScreenId", TblGridColumnMemory.class)
                    .setParameter("screenId", itemColumns.getScreenId())
                    .setParameter("userId", userUuid)
                    .setParameter("gridId", itemColumns.getGridId())
                    .getResultList();

            listGridColumns.stream().forEach(itemGridExists -> {
                listColumns.add(itemGridExists.getColumnId());
            });
            break;
        }
        return listColumns;
    }

    /**
     * Update inspection columns
     * @param itemColumnInputs
     * @param userUuid
     */
    @Transactional
    public void updateTblGridColumnMemory(ComponentInspectionDetailColumnInput itemColumnInputs, String userUuid) {
        Query query = entityManager.createNamedQuery("TblGridColumnMemory.updateByWidthByColumnScreenId");
        query.setParameter("screenId", itemColumnInputs.getScreenId());
        query.setParameter("colWidth", itemColumnInputs.getColWidth());
        query.setParameter("gridId", itemColumnInputs.getGridId());
        query.setParameter("columnId", itemColumnInputs.getColumnId());
        query.setParameter("userId", userUuid);
        query.executeUpdate();
    }

    /**
     * Get list setting columns
     * @param userUuid
     * @return
     */
    public List<TblGridColumnMemory> getSettingDetailColumns(String userUuid, String screenId) {
        Query query = entityManager.createNamedQuery("TblGridColumnMemory.findByUserScreenId");
        query.setParameter("userId", userUuid);
        query.setParameter("screenId", screenId);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     * 該当製造ロットの最新出荷検査がNGで終了しているかを取得。
     * @param componentID
     * @param lotNum
     * @return 
     */
    public boolean isInspectFailure(String componentID, String lotNum) {
        List<TblComponentInspectionResult> results = entityManager.createNamedQuery("TblComponentInspectionResult.findByLotNum", TblComponentInspectionResult.class)
            .setParameter("componentId", componentID)
            .setParameter("productionLotNum", lotNum)
            .setParameter("outgoingApproveResult", ApproveResult.APPROVED).getResultList();
        return results.size() > 0 ? results.get(0).getOutgoingInspectionResult() == CommonConstants.INSPECTION_RESULT_NG : false;
    }
    
    /** 
     * 該当部品、製造ロットの最終検査が出荷OKの場合trueを返す。<br>ただし、工程内検査のOKは出荷OKと認めない
     * @param componentID。
     * @param lotNum
     * @return 
     */
    public boolean isInspectPassed(String componentID, String lotNum) {
        List<TblComponentInspectionResult> results = entityManager.createNamedQuery("TblComponentInspectionResult.findByLotNum", TblComponentInspectionResult.class)
            .setParameter("componentId", componentID)
            .setParameter("productionLotNum", lotNum)
            .setParameter("outgoingApproveResult", ApproveResult.APPROVED).getResultList();
        if(results.isEmpty()) {
            return false;
        }
        TblComponentInspectionResult lastResult = results.get(0);
        Set<Integer> okOrConcession = new HashSet<>(Arrays.asList(CommonConstants.INSPECTION_RESULT, CommonConstants.INSPECTION_RESULT_SPECIAL));
        return okOrConcession.contains(lastResult.getOutgoingInspectionResult()) && lastResult.getInspectPtn() != '2';
    }
    
    /**
     * 納品先は自社するかどうかのチェック
     *
     * @param inspectionResultId
     * @param incomingCompanyId
     * @param langId
     * @return
     */
    public BasicResponse chkIncomingCompany(String inspectionResultId, String incomingCompanyId, String langId) {
        
        BasicResponse response = new BasicResponse();
        TblComponentInspectionResult tblCompInspResult = entityManager.createQuery("SELECT t FROM TblComponentInspectionResult t WHERE t.id = :id AND t.incomingCompanyId = :incomingCompanyId", TblComponentInspectionResult.class)
                .setParameter("id", inspectionResultId)
                .setParameter("incomingCompanyId", incomingCompanyId).getResultList().stream()
                .findFirst().orElse(null);

        // 納品先が自社ではない
        if (tblCompInspResult == null) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(langId, "msg_error_opp_not_allowed"));
            return response;
        }
        return response;
    }
    
     /**
     * 受入検査日に対して、入検査日の属す週の始まりの日付と属す月を設定
     * @param tblComponentInspectionResult
     * @param cnf
     */
    private void setWeekAndMonth(TblComponentInspectionResult tblComponentInspectionResult, CnfSystem cnf) {
        
        int firstDay = Integer.parseInt(cnf.getConfigValue());

        if (null == tblComponentInspectionResult.getIncomingInspectionDate()) {
            return;
        }

        // 受入検査日の属す週の始まりの日付を設定
        tblComponentInspectionResult.setIncomingInspectionWeek(DateFormat.getFirstDayOfWeek(firstDay, tblComponentInspectionResult.getIncomingInspectionDate()));
        
        // 受入検査日の属す月(YYYYMM)を設定
        String yearMonthStr = DateFormat.dateToStrMonth(tblComponentInspectionResult.getIncomingInspectionDate()).replace("/", "");
        
        if(StringUtils.isEmpty(yearMonthStr)){
            return;
        }
        
        tblComponentInspectionResult.setIncomingInspectionMonth(Integer.valueOf(yearMonthStr));
    }

   /*
     * Get version component inspection item info by loginUser
     * 
     * @param component
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param loginUser
     * @return 
     */
    public List<MstComponentInspectionItemsTable> getVersionInspectionItemsTables(String component, String outgoingCompanyId, String incomingCompanyId, boolean flg,
            LoginUser loginUser) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m FROM MstComponentInspectionItemsTable m");
        queryBuilder.append(" JOIN FETCH m.mstComponent");
        queryBuilder.append(" JOIN FETCH m.mstCompanyOutgoing");
        queryBuilder.append(" JOIN FETCH m.mstCompanyIncoming");
        queryBuilder.append(" JOIN FETCH m.mstComponentInspectLang");
        queryBuilder.append(" WHERE m.mstComponent.componentCode = :component");
        queryBuilder.append(" AND m.mstCompanyIncoming.id = :incomingCompanyId");
        queryBuilder.append(" AND m.mstCompanyOutgoing.id = :outgoingCompanyId");
        queryBuilder.append(" AND m.mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId ");
        queryBuilder.append(" AND m.inspectTypeDictKey = m.mstComponentInspectLang.mstComponentInspectLangPK.dictKey ");
        if (flg) {
            queryBuilder.append(" AND m.applyEndDate IS NULL");
        }
        queryBuilder.append(" AND m.itemApproveStatus = 1");
        queryBuilder.append(" ORDER BY m.version DESC");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("component", component);
        query.setParameter("incomingCompanyId", incomingCompanyId);
        query.setParameter("outgoingCompanyId", outgoingCompanyId);
        query.setParameter("langId", loginUser.getLangId());
        List<MstComponentInspectionItemsTable> itemsList = query.getResultList();
        return itemsList;
    }

    /*
     * Get Process capability indexs info by loginUser
     * 
     * @param component
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param inspectionType
     * @param InspectClass
     * @param productionLot
     * @param inspectionDateStart
     * @param inspectionDateEnd
     * @param cavityPrefix
     * @param cavityNum
     * @param isCount
     * @param loginUser
     * @return 
     */
    public ComponentInspectionResultInfo getProcesscapabilityIndexInfo(String component, String outgoingCompanyId, String incomingCompanyId,
            Integer inspectionType, String inspectClass, String productionLot, String inspectionDateStart, String inspectionDateEnd, String cavityPrefix, int cavityNum, boolean ischeck,
            LoginUser loginUser) {

        List<MstComponentInspectionItemsTable> itemsList = getVersionInspectionItemsTables(component, outgoingCompanyId, incomingCompanyId, true, loginUser);

        ComponentInspectionResultInfo componentInspectionResultInfo = new ComponentInspectionResultInfo();
        componentInspectionResultInfo.setCpGoal(cnfSystemService.findByKey("system", "cp_goal").getConfigValue());
        componentInspectionResultInfo.setCpkGoal(cnfSystemService.findByKey("system", "cpk_goal").getConfigValue());
        componentInspectionResultInfo.setCcGoal(cnfSystemService.findByKey("system", "cc_goal").getConfigValue());
        List<ComponentInspectionItemResultDetail> measureResultDetails = new ArrayList();
        if (null != itemsList && 0 < itemsList.size()) {
            MstComponentInspectionItemsTable mstComponentInspectionItemsTable = itemsList.get(0);
            componentInspectionResultInfo.setComponentInspectionItemsTableId(mstComponentInspectionItemsTable.getId());

            List<MstComponentInspectionItemsTableDetail> mstComponentInspectionItemsTableDetail
                    = getMeasureMstCompInspItemsTableDetails(mstComponentInspectionItemsTable.getId(), inspectionType, inspectClass, mstComponentInspectionItemsTable.getIncomingCompanyId());

            int massFlg = 0;
            List<MstComponentInspectClass> inspectClassList = new ArrayList();
            if (!StringUtils.isEmpty(inspectClass)) {
                massFlg = Character.getNumericValue(getComponentInspectClassList(inspectClass, mstComponentInspectionItemsTable.getIncomingCompanyId(), null).get(0).getMassFlg());
            }else{
                inspectClassList = getComponentInspectClassList(inspectClass, mstComponentInspectionItemsTable.getIncomingCompanyId(), loginUser);
            }

            int index = 0;
            List<String> componentInspectionItemsTableIds = new ArrayList();
            List<MstComponentInspectionItemsTable> items = getVersionInspectionItemsTables(component, outgoingCompanyId, incomingCompanyId, false, loginUser);
            for (MstComponentInspectionItemsTable itemsTable : items) {
                if (!componentInspectionItemsTableIds.contains(itemsTable.getId())) {
                    componentInspectionItemsTableIds.add(itemsTable.getId());
                }

            }
            
            List<TblComponentInspectionResult> tblComponentInspectionResultList = getInsepctListByItemId(componentInspectionItemsTableIds, inspectionType, inspectClass, inspectClassList, massFlg, productionLot, inspectionDateStart,
                    inspectionDateEnd);
            componentInspectionResultInfo.setCavList(getCavInspectList(tblComponentInspectionResultList, cavityPrefix, cavityNum));

            if (ischeck) {
                List detailList = getTblComponentInspectionResultDetailList(componentInspectionItemsTableIds, inspectionType, inspectClass, inspectClassList, massFlg, productionLot, inspectionDateStart, inspectionDateEnd, mstComponentInspectionItemsTableDetail, cavityPrefix, cavityNum, true, loginUser);
                long counts = (long) detailList.get(0);
                CnfSystem cnf = cnfSystemService.findByKey("system", "cpk_max_list_record_count");
                int sysCount = Integer.parseInt(cnf.getConfigValue());
                if (sysCount <= counts) {
                    componentInspectionResultInfo.setCount(counts);
                    componentInspectionResultInfo.setError(true);
                    componentInspectionResultInfo.setErrorCode(ErrorMessages.E201_APPLICATION);
                    String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
                    msg = String.format(msg, sysCount);
                    componentInspectionResultInfo.setErrorMessage(msg);
                    return componentInspectionResultInfo;
                }
            }

            for (MstComponentInspectionItemsTableDetail inspectionItemsTableDetail : mstComponentInspectionItemsTableDetail) {
                index++;
                ComponentInspectionItemResultDetail oneInspectionItemResult = new ComponentInspectionItemResultDetail();
                oneInspectionItemResult.setInspectionResultDetailId(inspectionItemsTableDetail.getId());
                oneInspectionItemResult.setInspectionItemSno(inspectionItemsTableDetail.getInspectionItemSno());
                oneInspectionItemResult.setInspectionItemName(inspectionItemsTableDetail.getInspectionItemName());
                oneInspectionItemResult.setMeasurementType(inspectionItemsTableDetail.getMeasurementType());
                oneInspectionItemResult.setMeasurementMethod(inspectionItemsTableDetail.getMeasurementMethod());
                oneInspectionItemResult.setDimensionValue(inspectionItemsTableDetail.getDimensionValue());
                oneInspectionItemResult.setTolerancePlus(inspectionItemsTableDetail.getTolerancePlus());
                oneInspectionItemResult.setToleranceMinus(inspectionItemsTableDetail.getToleranceMinus());
                oneInspectionItemResult.setDrawingPage(inspectionItemsTableDetail.getDrawingPage());
                oneInspectionItemResult.setDrawingAnnotation(inspectionItemsTableDetail.getDrawingAnnotation());
                oneInspectionItemResult.setDrawingMentionNo(inspectionItemsTableDetail.getDrawingMentionNo());
                oneInspectionItemResult.setSimilarMultiitem(inspectionItemsTableDetail.getSimilarMultiitem());
                oneInspectionItemResult.setDrawingArea(inspectionItemsTableDetail.getDrawingArea());
                oneInspectionItemResult.setGroupNumber(index);

                //寸法ID
                String itemtableDetailMethodId = (inspectionItemsTableDetail.getDrawingPage() == null ? "" : inspectionItemsTableDetail.getDrawingPage())
                        .concat(inspectionItemsTableDetail.getDrawingAnnotation() == null ? "" : inspectionItemsTableDetail.getDrawingAnnotation())
                        .concat(inspectionItemsTableDetail.getDrawingMentionNo() == null ? "" : inspectionItemsTableDetail.getDrawingMentionNo())
                        .concat(inspectionItemsTableDetail.getSimilarMultiitem() == null ? "" : inspectionItemsTableDetail.getSimilarMultiitem());
                oneInspectionItemResult.setItemtableDetailMethodId(itemtableDetailMethodId);
                List<MstComponentInspectionItemsTableDetail> itemsTableDetailList = new ArrayList();
                itemsTableDetailList.add(inspectionItemsTableDetail);
                List<TblComponentInspectionResultDetail> resultDetailLists = getTblComponentInspectionResultDetailList(componentInspectionItemsTableIds, inspectionType, inspectClass, inspectClassList, massFlg, productionLot, inspectionDateStart, inspectionDateEnd, itemsTableDetailList, cavityPrefix, cavityNum, false, loginUser);
                setProcesscapabilityIndexPlotData(resultDetailLists, oneInspectionItemResult, loginUser);
                measureResultDetails.add(oneInspectionItemResult);
            }
            
            BigDecimal processCapabilityindexMax = new BigDecimal(0);
            BigDecimal processCapabilityindexMin = new BigDecimal(0);
            for (ComponentInspectionItemResultDetail componentInspectionItemResultDetail : measureResultDetails) {

                if (componentInspectionItemResultDetail.getSampleCc().compareTo(processCapabilityindexMax) == 1) {
                    processCapabilityindexMax = componentInspectionItemResultDetail.getSampleCc();
                }
                if (componentInspectionItemResultDetail.getSampleCc().compareTo(processCapabilityindexMin) == -1) {
                    processCapabilityindexMin = componentInspectionItemResultDetail.getSampleCc();
                }
                if (componentInspectionItemResultDetail.getSampleCp().compareTo(processCapabilityindexMax) == 1) {
                    processCapabilityindexMax = componentInspectionItemResultDetail.getSampleCp();
                }
                if (componentInspectionItemResultDetail.getSampleCp().compareTo(processCapabilityindexMin) == -1) {
                    processCapabilityindexMin = componentInspectionItemResultDetail.getSampleCp();
                }
                if (componentInspectionItemResultDetail.getSampleCpk().compareTo(processCapabilityindexMax) == 1) {
                    processCapabilityindexMax = componentInspectionItemResultDetail.getSampleCpk();
                }
                if (componentInspectionItemResultDetail.getSampleCpk().compareTo(processCapabilityindexMin) == -1) {
                    processCapabilityindexMin = componentInspectionItemResultDetail.getSampleCpk();
                }
            }
            componentInspectionResultInfo.setProcessCapabilityindexMax(processCapabilityindexMax);
            componentInspectionResultInfo.setProcessCapabilityindexMin(processCapabilityindexMin);          
            if(0 == measureResultDetails.size()){
                componentInspectionResultInfo.setError(true);
                componentInspectionResultInfo.setErrorCode(ErrorMessages.E201_APPLICATION);
                componentInspectionResultInfo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_inspection_data_not_exist"));
            }else{
                componentInspectionResultInfo.setInspectionItemMeasureResultDetails(measureResultDetails);
            }
        }else{
            componentInspectionResultInfo.setError(true);
            componentInspectionResultInfo.setErrorCode(ErrorMessages.E201_APPLICATION);
            componentInspectionResultInfo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_inspection_data_not_exist"));
        }

        return componentInspectionResultInfo;
    }

   /*
     * Get Xbar and RChart info by loginUser
     * 
     * @param component
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param inspectionType
     * @param InspectClass
     * @param productionLot
     * @param inspectionDateStart
     * @param inspectionDateEnd
     * @param n
     * @param a2
     * @param d3
     * @param d4
     * @param cavityPrefix
     * @param cavityNum
     * @param loginUser
     * @return 
     */
    public ComponentInspectionResultInfo getXbarAndRChartInfo(String component, String outgoingCompanyId, String incomingCompanyId,
            Integer inspectionType, String inspectClass, String productionLot, String inspectionDateStart, String inspectionDateEnd, int n, String a2, String d3, String d4, String drawingPage, String drawingAnnotation, String drawingMentionNo, String similarMultiitem, 
            String cavityPrefix, int cavityNum, LoginUser loginUser) {

        List<MstComponentInspectionItemsTable> itemsList = getVersionInspectionItemsTables(component, outgoingCompanyId, incomingCompanyId, true, loginUser);
               
        ComponentInspectionResultInfo componentInspectionResultInfo = new ComponentInspectionResultInfo();
        List<ComponentInspectionItemResultDetail> measureResultDetails = new ArrayList();
        if (null != itemsList && 0 < itemsList.size()) {
            
            MstComponentInspectionItemsTable mstComponentInspectionItemsTable = itemsList.get(0);
            componentInspectionResultInfo.setComponentInspectionItemsTableId(mstComponentInspectionItemsTable.getId());
            List<MstComponentInspectionItemsTableDetail> mstComponentInspectionItemsTableDetail
                    = getMeasureMstCompInspItemsTableDetails(mstComponentInspectionItemsTable.getId(), inspectionType, inspectClass, mstComponentInspectionItemsTable.getIncomingCompanyId());

            int massFlg = 0;
            List<MstComponentInspectClass> inspectClassList = new ArrayList();
            if (!StringUtils.isEmpty(inspectClass)) {
                massFlg = Character.getNumericValue(getComponentInspectClassList(inspectClass, mstComponentInspectionItemsTable.getIncomingCompanyId(), null).get(0).getMassFlg());
            }else{
                inspectClassList = getComponentInspectClassList(inspectClass, mstComponentInspectionItemsTable.getIncomingCompanyId(), loginUser);
            }
            
            int index = 0;
            for (MstComponentInspectionItemsTableDetail inspectionItemsTableDetail : mstComponentInspectionItemsTableDetail) {
                index++;
                ComponentInspectionItemResultDetail oneInspectionItemResult = new ComponentInspectionItemResultDetail();
                oneInspectionItemResult.setInspectionResultDetailId(inspectionItemsTableDetail.getId());
                oneInspectionItemResult.setInspectionItemSno(inspectionItemsTableDetail.getInspectionItemSno());
                oneInspectionItemResult.setInspectionItemName(inspectionItemsTableDetail.getInspectionItemName());
                oneInspectionItemResult.setMeasurementType(inspectionItemsTableDetail.getMeasurementType());
                oneInspectionItemResult.setMeasurementMethod(inspectionItemsTableDetail.getMeasurementMethod());
                oneInspectionItemResult.setDimensionValue(inspectionItemsTableDetail.getDimensionValue());
                oneInspectionItemResult.setTolerancePlus(inspectionItemsTableDetail.getTolerancePlus());
                oneInspectionItemResult.setToleranceMinus(inspectionItemsTableDetail.getToleranceMinus());
                oneInspectionItemResult.setDrawingPage(inspectionItemsTableDetail.getDrawingPage());
                oneInspectionItemResult.setDrawingAnnotation(inspectionItemsTableDetail.getDrawingAnnotation());
                oneInspectionItemResult.setDrawingMentionNo(inspectionItemsTableDetail.getDrawingMentionNo());
                oneInspectionItemResult.setSimilarMultiitem(inspectionItemsTableDetail.getSimilarMultiitem());
                oneInspectionItemResult.setDrawingArea(inspectionItemsTableDetail.getDrawingArea());
                oneInspectionItemResult.setGroupNumber(index);
                //寸法ID
                String itemtableDetailMethodId = (inspectionItemsTableDetail.getDrawingPage() == null ? "" : inspectionItemsTableDetail.getDrawingPage())
                        .concat(inspectionItemsTableDetail.getDrawingAnnotation() == null ? "" : inspectionItemsTableDetail.getDrawingAnnotation())
                        .concat(inspectionItemsTableDetail.getDrawingMentionNo() == null ? "" : inspectionItemsTableDetail.getDrawingMentionNo())
                        .concat(inspectionItemsTableDetail.getSimilarMultiitem() == null ? "" : inspectionItemsTableDetail.getSimilarMultiitem());
                oneInspectionItemResult.setItemtableDetailMethodId(itemtableDetailMethodId);
                measureResultDetails.add(oneInspectionItemResult);
            }

            if(0 == measureResultDetails.size()){
                componentInspectionResultInfo.setError(true);
                componentInspectionResultInfo.setErrorCode(ErrorMessages.E201_APPLICATION);
                componentInspectionResultInfo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_inspection_data_not_exist"));
            }else{
                componentInspectionResultInfo.setInspectionItemMeasureResultDetails(measureResultDetails);
            }

            if(0 != n){
                componentInspectionResultInfo.setN(n);
            }

            if(!StringUtils.isEmpty(a2)){
                componentInspectionResultInfo.setA2(new BigDecimal(a2));
            }

            if(!StringUtils.isEmpty(d3)){
                componentInspectionResultInfo.setD3(new BigDecimal(d3));
            }

            if(!StringUtils.isEmpty(d4)){
                componentInspectionResultInfo.setD4(new BigDecimal(d4));
            }
            
            List<String> componentInspectionItemsTableIds = new ArrayList();
            List<MstComponentInspectionItemsTable> items = getVersionInspectionItemsTables(component, outgoingCompanyId, incomingCompanyId, false, loginUser);
            for (MstComponentInspectionItemsTable itemsTable : items) {
                if (!componentInspectionItemsTableIds.contains(itemsTable.getId())) {
                    componentInspectionItemsTableIds.add(itemsTable.getId());
                }
            }

            List<TblComponentInspectionResult> tblComponentInspectionResultList = getInsepctListByItemId(componentInspectionItemsTableIds, inspectionType, inspectClass, inspectClassList, massFlg, productionLot, inspectionDateStart,
                    inspectionDateEnd);
            componentInspectionResultInfo.setCavList(getCavInspectList(tblComponentInspectionResultList, cavityPrefix, cavityNum));

            if (!StringUtils.isEmpty(drawingPage) || !StringUtils.isEmpty(drawingAnnotation) || !StringUtils.isEmpty(drawingMentionNo) || !StringUtils.isEmpty(similarMultiitem)) {
                List<MstComponentInspectionItemsTableDetail> itemsTableDetailList = new ArrayList();
                MstComponentInspectionItemsTableDetail itemsTableDetail = new MstComponentInspectionItemsTableDetail();
                itemsTableDetail.setDrawingPage(drawingPage);
                itemsTableDetail.setDrawingAnnotation(drawingAnnotation);
                itemsTableDetail.setDrawingMentionNo(drawingMentionNo);
                itemsTableDetail.setSimilarMultiitem(similarMultiitem);
                itemsTableDetailList.add(itemsTableDetail);
                List<TblComponentInspectionResultDetail> resultDetailLists = getTblComponentInspectionResultDetailList(componentInspectionItemsTableIds, inspectionType, inspectClass, inspectClassList, massFlg, productionLot, inspectionDateStart, inspectionDateEnd, itemsTableDetailList, cavityPrefix, cavityNum, false, loginUser);
                setXbarAndRChartPlotData(resultDetailLists, componentInspectionResultInfo, cavityPrefix, cavityNum, loginUser);
            }
        }else{
            componentInspectionResultInfo.setError(true);
            componentInspectionResultInfo.setErrorCode(ErrorMessages.E201_APPLICATION);
            componentInspectionResultInfo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_inspection_data_not_exist"));
        }

        return componentInspectionResultInfo;
    }

    /*
     * Get Xbar and RChart Coefficients by loginUser
     * 
     * @return 
     */
    public ComponentInspectionResultInfo getXbarAndRChartCoefficients() {
        ComponentInspectionResultInfo componentInspectionResultInfo = new ComponentInspectionResultInfo();
        componentInspectionResultInfo.setN(Integer.valueOf(cnfSystemService.findByKey("system", "xbar_rchart_n").getConfigValue()));
        componentInspectionResultInfo.setA2(new BigDecimal(cnfSystemService.findByKey("system", "xbar_rchart_a2").getConfigValue()));
        componentInspectionResultInfo.setD3(new BigDecimal(cnfSystemService.findByKey("system", "xbar_rchart_d3").getConfigValue()));
        componentInspectionResultInfo.setD4(new BigDecimal(cnfSystemService.findByKey("system", "xbar_rchart_d4").getConfigValue()));
        return componentInspectionResultInfo;
    }

    /*
     * Get latest version component inspection result detail info by loginUser
     * 
     * @param componentInspectionItemsTableIds
     * @param inspectionType
     * @param InspectClass
     * @param inspectClassList
     * @param massFlg
     * @param productionLot
     * @param inspectionDateStart
     * @param inspectionDateEnd
     * @param mstComponentInspectionItemsTableDetailList
     * @param cavityPrefix
     * @param cavityNum
     * @param loginUser
     * @return 
     */
    private List getTblComponentInspectionResultDetailList(
            List<String> componentInspectionItemsTableIds,
            Integer inspectionType,
            String inspectClass,
            List<MstComponentInspectClass> inspectClassList,
            int massFlg,
            String productionLot,
            String inspectionDateStart,
            String inspectionDateEnd,
            List<MstComponentInspectionItemsTableDetail> mstComponentInspectionItemsTableDetailList,
            String cavityPrefix, 
            int cavityNum,
            boolean isCount,
            LoginUser loginUser) {
        StringBuilder queryBuilder = new StringBuilder();
        Date inspectionDateStarts = null;
        Date inspectionDateEnds = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        if(isCount){
            queryBuilder.append("SELECT COUNT(d.id) ");
        }else{
            queryBuilder.append("SELECT d ");
        }
        queryBuilder.append(" FROM TblComponentInspectionResult t ");
        queryBuilder.append(" INNER JOIN TblComponentInspectionResultDetail d ON t.id = d.componentInspectionResultId");
        queryBuilder.append(" INNER JOIN MstComponentInspectionItemsTable m ON m.id = t.componentInspectionItemsTableId");
        queryBuilder.append(" INNER JOIN MstComponentInspectionItemsTableDetail md ON md.componentInspectionItemsTableId = m.id");
        queryBuilder.append(" WHERE t.componentInspectionItemsTableId IN :componentInspectionItemsTableIds");
        queryBuilder.append(" AND d.inspectionType = :inspectionType");
        if (!StringUtils.isEmpty(inspectClass)) {
            queryBuilder.append(" AND t.firstFlag = :firstFlag");
            queryBuilder.append(" AND t.inspectPtn = :massFlg");
        } else if (0 < inspectClassList.size()) {
            queryBuilder.append(" AND (");
            for (int i = 1; i < inspectClassList.size() + 1; i++) {
                if (1 < i) {
                    queryBuilder.append(" OR");
                }
                queryBuilder.append(" (t.firstFlag = :firstFlag").append(i);
                queryBuilder.append(" AND t.inspectPtn = :massFlg").append(i).append(")");
            }
            queryBuilder.append(" )");
        }

        if (!StringUtils.isEmpty(productionLot)) {
            queryBuilder.append(" AND t.productionLotNum LIKE :productionLotNum");
        }
        
        if (null != cavityPrefix) {
            queryBuilder.append(" AND t.cavityPrefix = :cavityPrefix");
        }
        
        if (0 != cavityNum) {
            queryBuilder.append(" AND d.cavityNum = :cavityNum");
        }

        try {
            if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType && !StringUtils.isEmpty(inspectionDateStart)) {
                queryBuilder.append(" AND t.outgoingInspectionDate >= :inspectionDateStart");
                inspectionDateStarts = sdf.parse(inspectionDateStart + CommonConstants.SYS_MIN_TIME);
            }
            if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType && !StringUtils.isEmpty(inspectionDateEnd)) {
                queryBuilder.append(" AND t.outgoingInspectionDate <= :inspectionDateEnd");
                inspectionDateEnds = sdf.parse(inspectionDateEnd + CommonConstants.SYS_MAX_TIME);
            }
            if (CommonConstants.INSPECTION_TYPE_INCOMING == inspectionType && !StringUtils.isEmpty(inspectionDateStart)) {
                queryBuilder.append(" AND t.incomingInspectionDate >= :inspectionDateStart");
                inspectionDateStarts = sdf.parse(inspectionDateStart + CommonConstants.SYS_MIN_TIME);
            }
            if (CommonConstants.INSPECTION_TYPE_INCOMING == inspectionType && !StringUtils.isEmpty(inspectionDateEnd)) {
                queryBuilder.append(" AND t.incomingInspectionDate <= :inspectionDateEnd");
                inspectionDateEnds = sdf.parse(inspectionDateEnd + CommonConstants.SYS_MAX_TIME);
            }
        } catch (ParseException e) {
            Logger.getLogger(TblComponentInspectionResultResource.class.getName()).log(Level.INFO, null, e);
        }
        if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType) {
            queryBuilder.append(" AND t.inspectionStatus >= :inspectionStatus AND t.outgoingInspectionResult <> 0");
        }else{
            queryBuilder.append(" AND (t.inspectionStatus = :inspectionStatusApprove OR t.inspectionStatus = :inspectionStatusExempted)");
        }
        queryBuilder.append(" AND d.seqMeasurementResult IS NOT NULL");
        queryBuilder.append(" AND md.inspectionItemSno = d.inspectionItemSno ");

        if (0 < mstComponentInspectionItemsTableDetailList.size()) {
            queryBuilder.append(" AND (");
            for (int i = 1; i < mstComponentInspectionItemsTableDetailList.size() + 1; i++) {
                if (1 < i) {
                    queryBuilder.append(" OR");
                }
                queryBuilder.append(" (md.drawingPage = :drawingPage").append(i);
                queryBuilder.append(" AND md.drawingAnnotation = :drawingAnnotation").append(i);
                queryBuilder.append(" AND md.drawingMentionNo = :drawingMentionNo").append(i);
                queryBuilder.append(" AND md.similarMultiitem = :similarMultiitem").append(i).append(")");
            }
            queryBuilder.append(" )");
        }

        queryBuilder.append(" AND md.additionalFlg = :additionalFlg ");
        queryBuilder.append(" ORDER BY t.createDate ASC,");
        queryBuilder.append(" d.seq ASC");

        Query query = entityManager.createQuery(queryBuilder.toString());

        query.setParameter("componentInspectionItemsTableIds", componentInspectionItemsTableIds);
        query.setParameter("inspectionType", inspectionType);
        if (!StringUtils.isEmpty(inspectClass)) {
            query.setParameter("firstFlag", inspectClass);
            query.setParameter("massFlg", String.valueOf(massFlg).charAt(0));
        }else{
            int index = 0;
            for (MstComponentInspectClass mstComponentInspectClass : inspectClassList) {
                index++;
                query.setParameter("firstFlag" + index, mstComponentInspectClass.getPk().getId());
                query.setParameter("massFlg" + index, mstComponentInspectClass.getMassFlg());
            }
        }

        if (!StringUtils.isEmpty(productionLot)) {
            query.setParameter("productionLotNum", "%" + productionLot + "%");
        }
        if (null != cavityPrefix) {
            query.setParameter("cavityPrefix", cavityPrefix);
        }
        if (0 != cavityNum) {
            query.setParameter("cavityNum", cavityNum);
        }
        if (null != inspectionDateStarts) {
            query.setParameter("inspectionDateStart", inspectionDateStarts);
        }
        if (null != inspectionDateEnds) {
            query.setParameter("inspectionDateEnd", inspectionDateEnds);
        }
        if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType) {
            query.setParameter("inspectionStatus", CommonConstants.INSPECTION_STATUS_O_APPROVED);
        } else {
            query.setParameter("inspectionStatusApprove", CommonConstants.INSPECTION_STATUS_I_APPROVED);
            query.setParameter("inspectionStatusExempted", CommonConstants.INSPECTION_STATUS_I_EXEMPTED);
        }

        int index = 0;
        for (MstComponentInspectionItemsTableDetail mstComponentInspectionItemsTableDetail : mstComponentInspectionItemsTableDetailList) {
            index++;
            query.setParameter("drawingPage" + index, mstComponentInspectionItemsTableDetail.getDrawingPage());
            query.setParameter("drawingAnnotation" + index, mstComponentInspectionItemsTableDetail.getDrawingAnnotation());
            query.setParameter("drawingMentionNo" + index, mstComponentInspectionItemsTableDetail.getDrawingMentionNo());
            query.setParameter("similarMultiitem" + index, mstComponentInspectionItemsTableDetail.getSimilarMultiitem());
        }

        query.setParameter("additionalFlg", CommonConstants.ADDITIONAL_FLG_NO.charAt(0));
        List<TblComponentInspectionResultDetail> tblComponentInspectionResultDetailList = new ArrayList();
        tblComponentInspectionResultDetailList = query.getResultList();
        return tblComponentInspectionResultDetailList;

    }

    /*
     * Get Process capability Index Plot Data info
     * 
     * @param tblComponentInspectionResultDetailList
     * @param oneInspectionItemResult
     * @param loginUser
     * @return 
     */
    private void setProcesscapabilityIndexPlotData(
            List<TblComponentInspectionResultDetail> tblComponentInspectionResultDetailList,
            ComponentInspectionItemResultDetail oneInspectionItemResult,
            LoginUser loginUser) {

        int size = 0;
        BigDecimal sampleMean = new BigDecimal(0);
        BigDecimal sampleStdDev = new BigDecimal(0);
        BigDecimal sampleCp = new BigDecimal(0);
        BigDecimal sampleCpk = new BigDecimal(0);
        BigDecimal sampleCc = new BigDecimal(0);
        BigDecimal sampleSum = new BigDecimal(0);

        if (null != tblComponentInspectionResultDetailList && 0 < tblComponentInspectionResultDetailList.size()) {
            //
            size = tblComponentInspectionResultDetailList.size();
            double[] samples = new double[size];
            int i = 0;
            for (TblComponentInspectionResultDetail tblComponentInspectionResultDetail : tblComponentInspectionResultDetailList) {
                BigDecimal seqMeasurementResult = tblComponentInspectionResultDetail.getSeqMeasurementResult();
                sampleSum = sampleSum.add(seqMeasurementResult);
                samples[i] = seqMeasurementResult.doubleValue();
                i++;
            }
            sampleMean = sampleSum.divide(new BigDecimal(size), 10, BigDecimal.ROUND_HALF_UP).setScale(3, BigDecimal.ROUND_HALF_UP);

            if(1 < size){
                double stdDev = TotalityStandardDeviation(samples);
                sampleStdDev = new BigDecimal(stdDev).setScale(3, BigDecimal.ROUND_HALF_UP);
            }


            BigDecimal dimensionValue = oneInspectionItemResult.getDimensionValue();
            BigDecimal tolerancePlus = oneInspectionItemResult.getTolerancePlus();
            BigDecimal toleranceMinus = oneInspectionItemResult.getToleranceMinus();
            //
            if (dimensionValue.compareTo(BigDecimal.ZERO) == 0) {
                if (sampleStdDev.compareTo(BigDecimal.ZERO) != 0) {
                    sampleCp = tolerancePlus.divide(sampleStdDev.multiply(new BigDecimal(3)), 10, BigDecimal.ROUND_HALF_UP);
                    sampleCpk = tolerancePlus.subtract(sampleMean).divide(sampleStdDev.multiply(new BigDecimal(3)), 10, BigDecimal.ROUND_HALF_UP);
                }
                if(BigDecimal.ZERO.compareTo(tolerancePlus) != 0) {
                    sampleCc = sampleMean.divide(tolerancePlus, 10, BigDecimal.ROUND_HALF_UP);
                }
            } else {
                if (sampleStdDev.compareTo(BigDecimal.ZERO) != 0) {
                    sampleCp = tolerancePlus.subtract(toleranceMinus).divide(sampleStdDev.multiply(new BigDecimal(6)), 10, BigDecimal.ROUND_HALF_UP);
                    BigDecimal date1 = tolerancePlus.add(dimensionValue).subtract(sampleMean);
                    BigDecimal date2 = sampleMean.subtract(toleranceMinus.add(dimensionValue));
                    if (0 > date1.compareTo(date2)) {
                        sampleCpk = date1.divide(sampleStdDev.multiply(new BigDecimal(3)), 10, BigDecimal.ROUND_HALF_UP);
                    } else {
                        sampleCpk = date2.divide(sampleStdDev.multiply(new BigDecimal(3)), 10, BigDecimal.ROUND_HALF_UP);
                    }
                }
                if(BigDecimal.ZERO.compareTo(tolerancePlus.subtract(toleranceMinus)) != 0) {
                    sampleCc = sampleMean.subtract(dimensionValue.add(tolerancePlus.add(toleranceMinus).divide(new BigDecimal(2), 10, BigDecimal.ROUND_HALF_UP))).abs().divide(tolerancePlus.subtract(toleranceMinus).divide(new BigDecimal(2), 10, BigDecimal.ROUND_HALF_UP), 10, BigDecimal.ROUND_HALF_UP);
                }
            }
        }
        oneInspectionItemResult.setSampleMean(sampleMean.setScale(3, BigDecimal.ROUND_HALF_UP));
        oneInspectionItemResult.setSampleSize(new BigDecimal(size));
        oneInspectionItemResult.setSampleStdDev(sampleStdDev.setScale(3, BigDecimal.ROUND_HALF_UP));
        oneInspectionItemResult.setSampleCp(sampleCp.setScale(3, BigDecimal.ROUND_HALF_UP));
        oneInspectionItemResult.setSampleCpk(sampleCpk.setScale(3, BigDecimal.ROUND_HALF_UP));
        oneInspectionItemResult.setSampleCc(sampleCc.setScale(3, BigDecimal.ROUND_HALF_UP));
    }

    /*
     * Get X-bar/R Chart Plot Data info
     * 
     * @param tblComponentInspectionResultDetailList
     * @param oneInspectionItemResult
     * @param cavityPrefix
     * @param cavityNum
     * @param loginUser
     * @return 
     */
    private void setXbarAndRChartPlotData(List<TblComponentInspectionResultDetail> tblComponentInspectionResultDetailList,
            ComponentInspectionResultInfo componentInspectionResultInfo,
            String cavityPrefix, int cavityNum,
            LoginUser loginUser) {
        int size = tblComponentInspectionResultDetailList.size();
        int n = componentInspectionResultInfo.getN();
        if (n > size) {
            componentInspectionResultInfo.setError(true);
            componentInspectionResultInfo.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_inspection_data_not_exist");
            componentInspectionResultInfo.setErrorMessage(msg);
            return ;
        }

        // 生産ロット番号マップの取得
        Map<String, String> productionLotNumMap = new HashMap();
        List<String> idList = new ArrayList();
        for (TblComponentInspectionResultDetail tblComponentInspectionResultDetail : tblComponentInspectionResultDetailList) {
            String componentInspectionResultId = tblComponentInspectionResultDetail.getComponentInspectionResultId();
            if (!idList.contains(componentInspectionResultId)) {
                idList.add(componentInspectionResultId);
            }
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t FROM TblComponentInspectionResult t");
        queryBuilder.append(" WHERE t.id IN :idList");
        if (null != cavityPrefix) {
            queryBuilder.append(" AND t.cavityPrefix = :cavityPrefix");
        }
        if (0 != cavityNum) {
            queryBuilder.append(" AND t.id IN (select d.componentInspectionResultId FROM TblComponentInspectionResultDetail d Where d.cavityNum = :cavityNum)");
        }
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("idList", idList);
        if (null != cavityPrefix) {
            query.setParameter("cavityPrefix", cavityPrefix);
        }
        if (0 != cavityNum) {
            query.setParameter("cavityNum", cavityNum);
        }
        List<TblComponentInspectionResult> inspectionResultList = query.getResultList();
        for (TblComponentInspectionResult tblComponentInspectionResult : inspectionResultList) {
            productionLotNumMap.put(tblComponentInspectionResult.getId(), tblComponentInspectionResult.getProductionLotNum());
        }

        BigDecimal sampleSum = new BigDecimal(0);
        BigDecimal sampleMean = new BigDecimal(0);
        BigDecimal sampleGroupSum = new BigDecimal(0);
        BigDecimal XLCL = new BigDecimal(0);
        BigDecimal XUCL = new BigDecimal(0);
        BigDecimal RLCL = new BigDecimal(0);
        BigDecimal RUCL = new BigDecimal(0);
        BigDecimal rangeSum = new BigDecimal(0);
        BigDecimal sampleGroupRangeMax = new BigDecimal(0);
        BigDecimal sampleGroupRangeMin = new BigDecimal(0);
        BigDecimal rangeMax = new BigDecimal(0);
        BigDecimal rangeMin = new BigDecimal(0);
        BigDecimal meanMax = new BigDecimal(0);
        BigDecimal meanMin = new BigDecimal(0);
        BigDecimal rbar = new BigDecimal(0);
        List<SampleGroupPlotDataInfo> sampleGroupPlotDataInfoList = new ArrayList();
        String productionLotNum = null;
        for (int i = 0; i < (size - (size % n)); i++) {
            BigDecimal seqMeasurementResult = tblComponentInspectionResultDetailList.get(i).getSeqMeasurementResult();
            String componentInspectionResultId = tblComponentInspectionResultDetailList.get(i).getComponentInspectionResultId();

            if (1 == (i + 1) % n || 1 == seqMeasurementResult.compareTo(sampleGroupRangeMax) || 1 == n) {
                sampleGroupRangeMax = seqMeasurementResult;
            }

            if (1 == (i + 1) % n || 0 > seqMeasurementResult.compareTo(sampleGroupRangeMin) || 1 == n) {
                sampleGroupRangeMin = seqMeasurementResult;
                if (1 == (i + 1) % n || 1 == n) {
                    productionLotNum = productionLotNumMap.get(componentInspectionResultId);
                }
            }

            sampleGroupSum = sampleGroupSum.add(seqMeasurementResult);
            sampleSum = sampleSum.add(seqMeasurementResult);
            if (0 == (i + 1) % n) {
                int groupNumber = (i + 1) / n;
                SampleGroupPlotDataInfo sampleGroupPlotDataInfo = new SampleGroupPlotDataInfo();
                sampleGroupPlotDataInfo.setGroupNumber(groupNumber);

                BigDecimal mean = sampleGroupSum.divide(new BigDecimal(n), 3, BigDecimal.ROUND_HALF_UP);
                if (1 == groupNumber || 1 == mean.compareTo(meanMax)) {
                    meanMax = mean;
                }
                if (1 == groupNumber || 0 > mean.compareTo(meanMin)) {
                    meanMin = mean;
                }
                sampleGroupPlotDataInfo.setSampleGroupMean(mean);

                BigDecimal range = sampleGroupRangeMax.subtract(sampleGroupRangeMin).setScale(3, BigDecimal.ROUND_HALF_UP);
                if (1 == groupNumber || 1 == range.compareTo(rangeMax)) {
                    rangeMax = range;
                }
                if (1 == groupNumber || 0 > range.compareTo(rangeMin)) {
                    rangeMin = range;
                }
                sampleGroupPlotDataInfo.setRange(range);

                // 最初の製造ロット
                if (!CommonConstants.PRODUCTION_LOT_NUMBER.equals(productionLotNum)) {
                    sampleGroupPlotDataInfo.setProductionLotNum(productionLotNum);
                }
                rangeSum = rangeSum.add(sampleGroupPlotDataInfo.getRange());
                sampleGroupPlotDataInfoList.add(sampleGroupPlotDataInfo);
                productionLotNum = null;
                sampleGroupSum = new BigDecimal(0);
                sampleGroupRangeMax = new BigDecimal(0);
                sampleGroupRangeMin = new BigDecimal(0);
            }
        }

        componentInspectionResultInfo.setMeanMax(meanMax);
        componentInspectionResultInfo.setRangeMax(rangeMax);
        componentInspectionResultInfo.setMeanMin(meanMin);
        componentInspectionResultInfo.setRangeMin(rangeMin);
        componentInspectionResultInfo.setSampleGroupPlotDataInfoList(sampleGroupPlotDataInfoList);
        sampleMean = sampleSum.divide(new BigDecimal((size - (size % n))), 3, BigDecimal.ROUND_HALF_UP);
        componentInspectionResultInfo.setSampleMean(sampleMean);
        rbar = rangeSum.divide(new BigDecimal(size / n), 3, BigDecimal.ROUND_HALF_UP);
        componentInspectionResultInfo.setRbar(rbar);
        XLCL = sampleMean.subtract(componentInspectionResultInfo.getA2().multiply(rbar)).setScale(3, BigDecimal.ROUND_HALF_UP);
        componentInspectionResultInfo.setXLCL(XLCL);
        XUCL = sampleMean.add(componentInspectionResultInfo.getA2().multiply(rbar)).setScale(3, BigDecimal.ROUND_HALF_UP);
        componentInspectionResultInfo.setXUCL(XUCL);
        RLCL = componentInspectionResultInfo.getD3().multiply(rbar).setScale(3, BigDecimal.ROUND_HALF_UP);
        componentInspectionResultInfo.setRLCL(RLCL);
        RUCL = componentInspectionResultInfo.getD4().multiply(rbar).setScale(3, BigDecimal.ROUND_HALF_UP);
        componentInspectionResultInfo.setRUCL(RUCL);
    }
    /**
     * Get componentinspectclass list
     *
     * @param inspectClass
     * @param incomingCompanyId
     * @param loginUser
     * @return
     */
    private List<MstComponentInspectClass> getComponentInspectClassList(String inspectClass, String incomingCompanyId, LoginUser loginUser) {
        List<MstComponentInspectClass> inspectClassList = new ArrayList();
        if (!StringUtils.isEmpty(inspectClass)) {
            // 自社を取得する
            MstCompany selfCompany = this.mstCompanyService.getSelfCompany();
            String selfCompanyId = selfCompany.getId();
            if (selfCompanyId.equals(incomingCompanyId)) {
                MstComponentInspectClass mstComponentInspectClass = this.entityManager.createNamedQuery("MstComponentInspectClass.findByDictKey", MstComponentInspectClass.class).setParameter("dictKey", INSPECT_TYPE + inspectClass).getSingleResult();
                inspectClassList.add(mstComponentInspectClass);
            } else {
                PK pk = new PK();
                pk.setId(inspectClass);
                pk.setOwnerCompanyId(incomingCompanyId);
                MstComponentInspectClass mstComponentInspectClass = this.entityManager.find(MstComponentInspectClass.class, pk);

                if (null != mstComponentInspectClass) {
                    inspectClassList.add(mstComponentInspectClass);
                } else {
                    mstComponentInspectClass = this.entityManager.createNamedQuery("MstComponentInspectClass.findByDictKey", MstComponentInspectClass.class).setParameter("dictKey", INSPECT_TYPE + inspectClass).getSingleResult();
                    inspectClassList.add(mstComponentInspectClass);
                }
            }
        }else{
            inspectClassList = entityManager.createNamedQuery("MstComponentInspectClass.findByOwner", MstComponentInspectClass.class)
                    .setParameter("ownerCompanyId", incomingCompanyId).setParameter("langId", loginUser.getLangId()).getResultList();
            if (1 > inspectClassList.size()) {
                inspectClassList = entityManager.createNamedQuery("MstComponentInspectClass.findByOwner", MstComponentInspectClass.class)
                        .setParameter("ownerCompanyId", "SELF").setParameter("langId", loginUser.getLangId()).getResultList();
            }
        }
        return inspectClassList;
    }

    /**
     * Get MstComponentInspectionItemsTableDetail by
     * componentInspectionItemsTableId
     *
     * @param componentInspectionItemsTableId
     * @param inspectionType
     * @param inspectClass
     * @param incomingCompanyId
     * @return
     */
    private List<MstComponentInspectionItemsTableDetail> getMeasureMstCompInspItemsTableDetails(String componentInspectionItemsTableId, Integer inspectionType, String inspectClass, String incomingCompanyId) {
        int massFlg = 0;
        if (!StringUtils.isEmpty(inspectClass)) {
            massFlg = Character.getNumericValue(getComponentInspectClassList(inspectClass, incomingCompanyId, null).get(0).getMassFlg());
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m FROM MstComponentInspectionItemsTableDetail m ");
        queryBuilder.append(" WHERE m.componentInspectionItemsTableId = :componentInspectionItemsTableId ");
        queryBuilder.append(" AND m.measurementType = :measurementType ");
        boolean flg = true;
        if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType) {
            if (!StringUtils.isEmpty(inspectClass)) {
                switch (massFlg) {
                    case CommonConstants.INSPECTION_INT_MASS_FLAG:
                        queryBuilder.append(" AND m.outgoingTrialInspectionObject = :inspectionTargetYes ");
                        break;
                    case CommonConstants.INSPECTION_PRODUCTION_MASS_FLAG:
                        queryBuilder.append(" AND m.outgoingProductionInspectionObject = :inspectionTargetYes ");
                        break;
                    case CommonConstants.INSPECTION_PROCESS_MASS_FLAG:
                        queryBuilder.append(" AND m.processInspetionObject = :inspectionTargetYes ");
                        break;
                    case CommonConstants.INSPECTION_FIRST_MASS_PRODUCTION_FLAG:
                        queryBuilder.append(" AND m.outgoingFirstMassProductionObject = :inspectionTargetYes ");
                        break;
                    default:
                        flg = false;
                        break;
                }
            } else {
                queryBuilder.append(" AND (m.outgoingTrialInspectionObject = :inspectionTargetYes ");
                queryBuilder.append(" OR m.outgoingProductionInspectionObject = :inspectionTargetYes ");
                queryBuilder.append(" OR m.processInspetionObject = :inspectionTargetYes ");
                queryBuilder.append(" OR  m.outgoingFirstMassProductionObject = :inspectionTargetYes) ");
            }

        } else {
            if (!StringUtils.isEmpty(inspectClass)) {
                switch (massFlg) {
                    case CommonConstants.INSPECTION_INT_MASS_FLAG:
                        queryBuilder.append(" AND m.incomingTrialInspectionObject = :inspectionTargetYes ");
                        break;
                    case CommonConstants.INSPECTION_PRODUCTION_MASS_FLAG:
                        queryBuilder.append(" AND m.incomingProductionInspectionObject = :inspectionTargetYes ");
                        break;
                    case CommonConstants.INSPECTION_PROCESS_MASS_FLAG:
                        queryBuilder.append(" AND m.processInspetionObject = :inspectionTargetYes ");
                        break;
                    case CommonConstants.INSPECTION_FIRST_MASS_PRODUCTION_FLAG:
                        queryBuilder.append(" AND m.incomingFirstMassProductionObject = :inspectionTargetYes ");
                        break;
                    default:
                        flg = false;
                        break;
                }
            } else {
                queryBuilder.append(" AND (m.incomingTrialInspectionObject = :inspectionTargetYes ");
                queryBuilder.append(" OR  m.incomingProductionInspectionObject = :inspectionTargetYes ");
                queryBuilder.append(" OR  m.processInspetionObject = :inspectionTargetYes ");
                queryBuilder.append(" OR  m.incomingFirstMassProductionObject = :inspectionTargetYes) ");
            }
        }
        queryBuilder.append(" AND m.additionalFlg = :additionalFlg ");
        queryBuilder.append(" ORDER BY m.localSeq,m.inspectionItemSno");
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("componentInspectionItemsTableId", componentInspectionItemsTableId);
        query.setParameter("measurementType", CommonConstants.MEASUREMENT_TYPE_MEASURE);
        if(flg){
            query.setParameter("inspectionTargetYes", CommonConstants.INSPECTION_TARGET_YES.charAt(0));
        }
        query.setParameter("additionalFlg", CommonConstants.ADDITIONAL_FLG_NO.charAt(0));
        List<MstComponentInspectionItemsTableDetail> itemDetails = new ArrayList();
        itemDetails = query.getResultList();
        return itemDetails;
    }

    /**
     * Get TotalityStandardDeviation by sample
     *
     * @param sample
     * @return
     */
    private double TotalityStandardDeviation(double[] sample) {
        if (1 > sample.length) {
            return 0;
        }
        double dSum = 0.0; 
        double dAverage = 0.0; 
        for (int i = 0; i < sample.length; ++i) {
            dSum += sample[i];
        }
        dAverage = dSum / sample.length;

        dSum = 0.0;
        for (int i = 0; i < sample.length; ++i) {
            dSum += (sample[i] - dAverage) * (sample[i] - dAverage);
        }
        double dStdDev = (double)Math.round(Math.sqrt(dSum / (sample.length-1))*1000)/1000;
        return dStdDev;
    }
    
    @Transactional
    public String copyInpsectData(String resultid, int inspectType, LoginUser loginUser) {
        Date now = new Date();
        List<TblComponentInspectionResult> resultOrg = entityManager.createNamedQuery("TblComponentInspectionResult.findById", TblComponentInspectionResult.class)
            .setParameter("id", resultid).getResultList();
        TblComponentInspectionResult resultCopied = copyResult(resultOrg, inspectType, loginUser);
        entityManager.persist(resultCopied);
        
        List<Integer> inspTypes = new ArrayList<>(Arrays.asList(1));
        if(inspectType == CommonConstants.INSPECTION_TYPE_INCOMING) {
            inspTypes.add(2);
        }
        List<TblComponentInspectionResultDetail> detailOrg = entityManager.createNamedQuery("TblComponentInspectionResultDetail.findByComponentInspectionResultIdAndInspectionTypeList", TblComponentInspectionResultDetail.class)
            .setParameter("componentInspectionResultId", resultid).setParameter("inspectionType", inspTypes).getResultList();
        DetailCopyResult detailCopied = copyDetail(detailOrg, resultCopied.getId(), loginUser);
        detailCopied.copied.stream().forEach(detail->entityManager.persist(detail));
        
        List<TblComponentInspectionReferenceFile> refFileOrg = entityManager.createNamedQuery("TblComponentInspectionReferenceFile.findByComponentInspectionResultId", TblComponentInspectionReferenceFile.class)
            .setParameter("componentInspectionResultId", resultid).getResultList();
        List<TblComponentInspectionReferenceFile> refFileCopied = copyRefFile(refFileOrg, resultCopied.getId(), loginUser);
        refFileCopied.stream().forEach(refFile->entityManager.persist(refFile));
        
        List<TblComponentInspectionDefect> defectOrg = entityManager.createNamedQuery("TblComponentInspectionDefect.findByComponentInspectionResultId", TblComponentInspectionDefect.class)
            .setParameter("componentInspectionResultId", resultid).getResultList();
        List<TblComponentInspectionDefect> defectCopied = copyDefect(defectOrg, resultCopied.getId());
        defectCopied.stream().forEach(defect->entityManager.persist(defect));
        
        List<TblComponentInspectionResultFile> resFileOrg = entityManager.createNamedQuery("TblComponentInspectionResultFile.findByComponentInspectionResultId", TblComponentInspectionResultFile.class)
            .setParameter("componentInspectionResultId", resultid).getResultList();
        List<TblComponentInspectionResultFile> resFileCopied = copyResFile(resFileOrg, resultCopied.getId(), loginUser);
        resFileCopied.stream().forEach(resFile->entityManager.persist(resFile));
        
        List<TblComponentInspectionSampleName> sampleNmOrg = entityManager.createNamedQuery("TblComponentInspectionSampleName.findByComponentInspectionResultIdAndInspectionTypes", TblComponentInspectionSampleName.class)
            .setParameter("componentInspectionResultId", resultid).setParameter("inspectionType", inspTypes).getResultList();
        List<TblComponentInspectionSampleName> sampleNmCopied = copySampleName(sampleNmOrg, resultCopied.getId(), loginUser);
        sampleNmCopied.stream().forEach(sampleNm->entityManager.persist(sampleNm));
        
        List<TblComponentInspectionVisualImageFile> visImgOrg = entityManager.createNamedQuery("TblComponentInspectionVisualImageFile.findByComponentInspectionResultIdAndInspectTypes", TblComponentInspectionVisualImageFile.class)
            .setParameter("componentInspectionResultId", resultid).setParameter("inspectionType", inspTypes).getResultList();
        List<TblComponentInspectionVisualImageFile> visImgCopied = copyVisualImgFile(visImgOrg, detailCopied.orgDestIdMap, loginUser);
        visImgCopied.stream().forEach(visImg->entityManager.persist(visImg));
        return resultCopied.getId();
    }
    
    private TblComponentInspectionResult copyResult(List<TblComponentInspectionResult> origin, int inspectType, LoginUser loginUser) {
        Date now = new Date();
        List<TblComponentInspectionResult> result = BeanCopyUtil.copyFieldsInList(origin, TblComponentInspectionResult.class, (org, dest)->{
            dest.setId(IDGenerator.generate());
            if(CommonConstants.INSPECTION_TYPE_OUTGOING == inspectType) {
                dest.setInspectionStatus(CommonConstants.INSPECTION_STATUS_O_INSPECTING);
            } else {
                if(CommonConstants.INSPECTION_STATUS_O_APPROVED == org.getInspectionStatus()) {
                    dest.setInspectionStatus(CommonConstants.INSPECTION_STATUS_O_APPROVED);
                } else {
                    dest.setInspectionStatus(CommonConstants.INSPECTION_STATUS_I_INSPECTING);
                }
            }
            dest.setCreateDate(now);
            dest.setCreateUserUuid(loginUser.getUserUuid());
            dest.setUpdateDate(now);
            dest.setUpdateUserUuid(loginUser.getUserUuid());

            if(CommonConstants.INSPECTION_TYPE_OUTGOING == inspectType) {
                dest.setOutgoingInspectionResult(CommonConstants.INSPECTION_RESULT_UNDEFINED);
                dest.setOutgoingInspectionDate(null);
                dest.setOutgoingInspectionPersonUuid(null);
                dest.setOutgoingInspectionPersonName(null);

                dest.setOutgoingConfirmResult(ConfirmResult.UNTREATED);
                dest.setOutgoingConfirmDate(null);
                dest.setOutgoingConfirmerUuid(null);
                dest.setOutgoingConfirmerName(null);

                dest.setOutgoingApproveResult(ApproveResult.UNTREATED);
                dest.setOutgoingInspectionApproveDate(null);
                dest.setOutgoingInspectionApprovePersonUuid(null);
                dest.setOutgoingInspectionApprovePersonName(null);
                
                dest.setIncomingInspectionComment("");
                dest.setIncomingPrivateComment("");
            }

            dest.setIncomingInspectionResult(CommonConstants.INSPECTION_RESULT_UNDEFINED);
            dest.setIncomingInspectionDate(null);
            dest.setIncomingInspectionPersonUuid(null);
            dest.setIncomingInspectionPersonName(null);

            dest.setIncomingConfirmResult(ConfirmResult.UNTREATED);
            dest.setIncomingConfirmDate(null);
            dest.setIncomingConfirmerUuid(null);
            dest.setIncomingConfirmerName(null);

            dest.setIncomingApproveResult(ApproveResult.UNTREATED);
            dest.setIncomingInspectionApproveDate(null);
            dest.setIncomingInspectionApprovePersonUuid(null);
            dest.setIncomingInspectionApprovePersonName(null);
            
            if(CommonConstants.INSPECTION_TYPE_OUTGOING == inspectType) {
                dest.setInspBatchUpdateStatus(CommonConstants.INSP_BATCH_UPDATE_STATUS_O_RESULT_NOT_SEND);
            } else {
                dest.setInspBatchUpdateStatus(CommonConstants.INSP_BATCH_UPDATE_STATUS_I_RESULT_PUSHED);
            }
            
            dest.setAbortFlg(AbortFlg.RUNNING);
        });
        return result.get(0);
    }
    
    private DetailCopyResult copyDetail(List<TblComponentInspectionResultDetail> origin, String destResultId, LoginUser loginUser) {
        Date now = new Date();
        Map<String, String> orgDestIdMap = new HashMap<>();
        List<TblComponentInspectionResultDetail> copied = BeanCopyUtil.copyFieldsInList(origin, TblComponentInspectionResultDetail.class, (org, dest)->{
            dest.setId(IDGenerator.generate());
            dest.setComponentInspectionResultId(destResultId);
            dest.setCreateDate(now);
            dest.setCreateUserUuid(loginUser.getUserUuid());
            dest.setUpdateDate(now);
            dest.setUpdateUserUuid(loginUser.getUserUuid());
            orgDestIdMap.put(org.getId(), dest.getId());
        });
        return new DetailCopyResult(copied, orgDestIdMap);
    }
    
    private static class DetailCopyResult {
        private List<TblComponentInspectionResultDetail> copied;
        private Map<String, String> orgDestIdMap;
        public DetailCopyResult(List<TblComponentInspectionResultDetail> copied, Map<String, String> orgDestIdMap) {
            this.copied = copied;
            this.orgDestIdMap = orgDestIdMap;
        }
    }
    
    private List<TblComponentInspectionReferenceFile> copyRefFile(List<TblComponentInspectionReferenceFile> origin, String destResultId, LoginUser loginUser) {
        Date now = new Date();
        List<TblComponentInspectionReferenceFile> copied = BeanCopyUtil.copyFieldsInList(origin, TblComponentInspectionReferenceFile.class, (org, dest)->{
            dest.setId(IDGenerator.generate());
            dest.setComponentInspectionResultId(destResultId);
            dest.setCreateDate(now);
            dest.setCreateUserUuid(loginUser.getUserUuid());
            dest.setUpdateDate(now);
            dest.setUpdateUserUuid(loginUser.getUserUuid());
        });
        return copied;
    }
    
    private List<TblComponentInspectionDefect> copyDefect(List<TblComponentInspectionDefect> origin, String destResultId) {
        Date now = new Date();
        List<TblComponentInspectionDefect> copied = BeanCopyUtil.copyFieldsInList(origin, TblComponentInspectionDefect.class, (org, dest)->{
            dest.setId(IDGenerator.generate());
            dest.setComponentInspectionResultId(destResultId);
        });
        return copied;
    }
    
    private List<TblComponentInspectionResultFile> copyResFile(List<TblComponentInspectionResultFile> origin, String destResultId, LoginUser loginUser) {
        Date now = new Date();
        List<TblComponentInspectionResultFile> copied = BeanCopyUtil.copyFieldsInList(origin, TblComponentInspectionResultFile.class, (org, dest)->{
            dest.setId(IDGenerator.generate());
            dest.setComponentInspectionResultId(destResultId);
        });
        return copied;
    }
    
    private List<TblComponentInspectionSampleName> copySampleName(List<TblComponentInspectionSampleName> origin, String destResultId, LoginUser loginUser) {
        Date now = new Date();
        List<TblComponentInspectionSampleName> copied = BeanCopyUtil.copyFieldsInList(origin, TblComponentInspectionSampleName.class, (org, dest)->{
            dest.setId(IDGenerator.generate());
            dest.setComponentInspectionResultId(destResultId);
            dest.setCreateDate(now);
            dest.setCreateUserUuid(loginUser.getUserUuid());
            dest.setUpdateDate(now);
            dest.setUpdateUserUuid(loginUser.getUserUuid());
        });
        return copied;
    }
    
    private List<TblComponentInspectionVisualImageFile> copyVisualImgFile(List<TblComponentInspectionVisualImageFile> origin, Map<String, String> orgDestIdMap, LoginUser loginUser) {
        Date now = new Date();
        List<TblComponentInspectionVisualImageFile> copied = BeanCopyUtil.copyFieldsInList(origin, TblComponentInspectionVisualImageFile.class, (org, dest)->{
            TblComponentInspectionVisualImageFilePK pk = new TblComponentInspectionVisualImageFilePK();
            pk.setComponentInspectionResultDetailId(orgDestIdMap.get(org.getTblComponentInspectionVisualImageFilePK().getComponentInspectionResultDetailId()));
            pk.setSeq(org.getTblComponentInspectionVisualImageFilePK().getSeq());
            dest.setTblComponentInspectionVisualImageFilePK(pk);
            dest.setCreateDate(now);
            dest.setCreateDateUuid(loginUser.getUserUuid());
            dest.setUpdateDate(now);
            dest.setUpdateUserUuid(loginUser.getUserUuid());
        });
        return copied;
    }
    
    @Transactional
    public List<String> abort(List<String> resultIds, AbortFlg abortFlg, LoginUser loginUser) {
        if(resultIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<TblComponentInspectionResult> results = this.entityManager.createNamedQuery("TblComponentInspectionResult.findByIds", TblComponentInspectionResult.class)
            .setParameter("ids", resultIds).getResultList();
        results.stream().filter(result->result.getAbortFlg().equals(AbortFlg.RUNNING)).forEach(result->{
            result.setAbortFlg(abortFlg);
            if(!abortFlg.equals(AbortFlg.RUNNING)) {
                result.setOutgoingInspectionResult(CommonConstants.INSPECTION_RESULT_NG);
                result.setInspectionStatus(CommonConstants.INSPECTION_STATUS_ABORTED);
            }
            if(loginUser != null) {
                result.setUpdateUserUuid(loginUser.getUserUuid());
                result.setUpdateDate(new Date());
            }
        });
        return results.stream().map(result->result.getId()).collect(Collectors.toList());
    }
    
    public List<String> getExtAborting(String incomingCompanyId) {
        return this.entityManager.createNamedQuery("TblComponentInspectionResult.findByAbortFlg", TblComponentInspectionResult.class)
            .setParameter("incomingCompanyId", incomingCompanyId).setParameter("abortFlg", AbortFlg.ABORTING)
            .getResultList().stream().map(result->result.getId()).collect(Collectors.toList());
    }
    
    @Transactional
    public void ackAborted(List<String> abortedIds, String incomingCompanyId) {
        List<TblComponentInspectionResult> results = entityManager.createNamedQuery("TblComponentInspectionResult.findByIds", TblComponentInspectionResult.class)
            .setParameter("ids", abortedIds).getResultList();
        results.stream().filter(result->result.getIncomingCompanyId().equals(incomingCompanyId)).forEach(result->{
            result.setAbortFlg(AbortFlg.ABORTED);
        });
    }
    
    /**
     * Get CavInspectList
     * @param tblComponentInspectionResultlList
     * @param cavityPrefix
     * @param cavityNum
     * @return List<String>
     */
    private List<String> getCavInspectList(List<TblComponentInspectionResult> tblComponentInspectionResultlList, String cavityPrefix, int cavityNum) {

        List<String> cavInspectList = new ArrayList();

        List<String> resultIds = new ArrayList();

        for (int i = 0; i < tblComponentInspectionResultlList.size(); i++) {

            TblComponentInspectionResult tblComponentInspectionResult = tblComponentInspectionResultlList.get(i);

            if (i == 0) {
                resultIds.add(tblComponentInspectionResult.getId());
                for (int j = tblComponentInspectionResult.getCavityStartNum(); j < (tblComponentInspectionResult.getCavityStartNum() + tblComponentInspectionResult.getCavityCnt()); j++) {
                    String cavNo = FileUtil.getStringValue(tblComponentInspectionResult.getCavityPrefix()) + String.valueOf(j);
                    cavInspectList.add(cavNo);
                }
            } else if (!resultIds.contains(tblComponentInspectionResult.getId())) {
                for (int j = tblComponentInspectionResult.getCavityStartNum(); j < (tblComponentInspectionResult.getCavityStartNum() + tblComponentInspectionResult.getCavityCnt()); j++) {
                    String cavNo = FileUtil.getStringValue(tblComponentInspectionResult.getCavityPrefix()) + String.valueOf(j);
                    if (!cavInspectList.contains(cavNo)) {
                        cavInspectList.add(cavNo);
                    }
                }
            }
        }

        if (null != cavityPrefix && 0 != cavityNum) {
            
            String disPlayCavNo = cavityPrefix + String.valueOf(cavityNum);
            if (cavInspectList.contains(disPlayCavNo)) {
                cavInspectList.clear();
                cavInspectList.add(disPlayCavNo);
            }else {
                cavInspectList.clear();
            }
        }
        return cavInspectList;
    }
    
    /**
     * Get InsepctList
     *
     * @param componentInspectionItemsTableIds
     * @param inspectionType
     * @param inspectClass
     * @param inspectClassList
     * @param massFlg
     * @param productionLot
     * @param inspectionDateStart
     * @param inspectionDateEnd
     * @return List<String>
     */
    private List<TblComponentInspectionResult> getInsepctListByItemId(List<String> componentInspectionItemsTableIds, Integer inspectionType, String inspectClass, List<MstComponentInspectClass> inspectClassList, int massFlg, String productionLot, String inspectionDateStart,
            String inspectionDateEnd) {

        StringBuilder queryBuilder = new StringBuilder();
        Date inspectionDateStarts = null;
        Date inspectionDateEnds = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        queryBuilder.append("SELECT t ");
        queryBuilder.append(" FROM TblComponentInspectionResult t ");
        queryBuilder.append(" INNER JOIN MstComponentInspectionItemsTable m ON m.id = t.componentInspectionItemsTableId");
        queryBuilder.append(" WHERE t.componentInspectionItemsTableId IN :componentInspectionItemsTableIds");
        if (!StringUtils.isEmpty(inspectClass)) {
            queryBuilder.append(" AND t.firstFlag = :firstFlag");
            queryBuilder.append(" AND t.inspectPtn = :massFlg");
        } else if (0 < inspectClassList.size()) {
            queryBuilder.append(" AND (");
            for (int i = 1; i < inspectClassList.size() + 1; i++) {
                if (1 < i) {
                    queryBuilder.append(" OR");
                }
                queryBuilder.append(" (t.firstFlag = :firstFlag").append(i);
                queryBuilder.append(" AND t.inspectPtn = :massFlg").append(i).append(")");
            }
            queryBuilder.append(" )");
        }
        if (!StringUtils.isEmpty(productionLot)) {
            queryBuilder.append(" AND t.productionLotNum LIKE :productionLotNum");
        }
        try {
            if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType && !StringUtils.isEmpty(inspectionDateStart)) {
                queryBuilder.append(" AND t.outgoingInspectionDate >= :inspectionDateStart");
                inspectionDateStarts = sdf.parse(inspectionDateStart + CommonConstants.SYS_MIN_TIME);
            }
            if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType && !StringUtils.isEmpty(inspectionDateEnd)) {
                queryBuilder.append(" AND t.outgoingInspectionDate <= :inspectionDateEnd");
                inspectionDateEnds = sdf.parse(inspectionDateEnd + CommonConstants.SYS_MAX_TIME);
            }
            if (CommonConstants.INSPECTION_TYPE_INCOMING == inspectionType && !StringUtils.isEmpty(inspectionDateStart)) {
                queryBuilder.append(" AND t.incomingInspectionDate >= :inspectionDateStart");
                inspectionDateStarts = sdf.parse(inspectionDateStart + CommonConstants.SYS_MIN_TIME);
            }
            if (CommonConstants.INSPECTION_TYPE_INCOMING == inspectionType && !StringUtils.isEmpty(inspectionDateEnd)) {
                queryBuilder.append(" AND t.incomingInspectionDate <= :inspectionDateEnd");
                inspectionDateEnds = sdf.parse(inspectionDateEnd + CommonConstants.SYS_MAX_TIME);
            }
        } catch (ParseException e) {
            Logger.getLogger(TblComponentInspectionResultResource.class.getName()).log(Level.INFO, null, e);
        }
        if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType) {
            queryBuilder.append(" AND t.inspectionStatus >= :inspectionStatus AND t.outgoingInspectionResult <> 0");
        } else {
            queryBuilder.append(" AND (t.inspectionStatus = :inspectionStatusApprove OR t.inspectionStatus = :inspectionStatusExempted)");
        }

        Query query = entityManager.createQuery(queryBuilder.toString());

        query.setParameter("componentInspectionItemsTableIds", componentInspectionItemsTableIds);
        if (!StringUtils.isEmpty(inspectClass)) {
            query.setParameter("firstFlag", inspectClass);
            query.setParameter("massFlg", String.valueOf(massFlg).charAt(0));
        } else {
            int index = 0;
            for (MstComponentInspectClass mstComponentInspectClass : inspectClassList) {
                index++;
                query.setParameter("firstFlag" + index, mstComponentInspectClass.getPk().getId());
                query.setParameter("massFlg" + index, mstComponentInspectClass.getMassFlg());
            }
        }

        if (!StringUtils.isEmpty(productionLot)) {
            query.setParameter("productionLotNum", "%" + productionLot + "%");
        }
        if (null != inspectionDateStarts) {
            query.setParameter("inspectionDateStart", inspectionDateStarts);
        }
        if (null != inspectionDateEnds) {
            query.setParameter("inspectionDateEnd", inspectionDateEnds);
        }
        if (CommonConstants.INSPECTION_TYPE_OUTGOING == inspectionType) {
            query.setParameter("inspectionStatus", CommonConstants.INSPECTION_STATUS_O_APPROVED);
        } else {
            query.setParameter("inspectionStatusApprove", CommonConstants.INSPECTION_STATUS_I_APPROVED);
            query.setParameter("inspectionStatusExempted", CommonConstants.INSPECTION_STATUS_I_EXEMPTED);
        }

        List<TblComponentInspectionResult> tblComponentInspectionResultlList = query.getResultList();
        return tblComponentInspectionResultlList;
    }
    
    /**
     * getLotAcceptanceReportCavNoList
     *
     * @param incomingCompanyId
     * @param componentCode
     * @param periodFlag
     * @param reportDateStart
     * @param reportDateEnd
     * @param cavityPrefix
     * @param cavityNum
     * @return List
     */
    public List<String> getLotAcceptanceReportCavNoList(String incomingCompanyId, String componentCode, String periodFlag,
            Date reportDateStart, Date reportDateEnd, String cavityPrefix, int cavityNum) {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" SELECT cir");
        sqlBuilder.append(" FROM TblComponentInspectionResult cir ");
        sqlBuilder.append(" JOIN FETCH cir.mstComponent component ");
        sqlBuilder.append(" JOIN FETCH cir.mstCompanyIncoming incomingCompany ");
        sqlBuilder.append(" WHERE cir.incomingInspectionResult in (1, 2, 3) ");
        if (StringUtils.isNotEmpty(incomingCompanyId)) {
            sqlBuilder.append(" AND incomingCompany.id = :incomingCompanyId ");
        }
        if (StringUtils.isNotEmpty(componentCode)) {
            sqlBuilder.append(" AND component.componentCode like :componentCode ");
        }

        switch (periodFlag) {
            case WEEK: // 期間種類が週別
                if (reportDateStart != null && reportDateEnd != null) {
                    sqlBuilder.append(" AND cir.incomingInspectionWeek between :reportDateStart and :reportDateEnd ");
                }
                sqlBuilder.append(" order by cir.incomingInspectionWeek asc ");
                break;
            case MONTH: // 期間種類が月別
                if (reportDateStart != null && reportDateEnd != null) {
                    sqlBuilder.append(" AND cir.incomingInspectionMonth between :reportDateStart and :reportDateEnd ");
                }
                sqlBuilder.append(" order by cir.incomingInspectionMonth asc ");
                break;
            default: // 期間種類が日別
                if (reportDateStart != null && reportDateEnd != null) {
                    sqlBuilder.append(" AND cir.incomingInspectionDate between :reportDateStart and :reportDateEnd ");
                }
                break;
        }

        Query query = entityManager.createQuery(sqlBuilder.toString());

        if (StringUtils.isNotEmpty(incomingCompanyId)) {
            query.setParameter("incomingCompanyId", incomingCompanyId);
        }

        if (StringUtils.isNotEmpty(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode + "%");
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
        List<TblComponentInspectionResult> list = query.getResultList();

        return getCavInspectList(list, cavityPrefix, cavityNum);
    }
    
     /**
     * getInspectDefectCavNoList
     *
     * @param incomingCompanyId
     * @param componentId
     * @param formatComponentInspectionFrom
     * @param formatComponentInspectionTo
     * @param cavityPrefix
     * @param cavityNum
     * @return List
     */
    public List<String> getInspectDefectCavNoList(String incomingCompanyId, String componentId, Date formatComponentInspectionFrom, Date formatComponentInspectionTo, String cavityPrefix, int cavityNum) {

        StringBuilder sqlBuilder = new StringBuilder();
        
        sqlBuilder.append("SELECT r"
                + " FROM TblComponentInspectionDefect t"
                + " JOIN FETCH TblComponentInspectionResult r"
                + " JOIN FETCH r.mstComponent"
                + " JOIN FETCH r.mstCompanyIncoming"
                + " WHERE t.componentInspectionResultId = r.id");
        
        if (StringUtils.isNotBlank(incomingCompanyId)) {
            sqlBuilder.append(" AND r.mstCompanyIncoming.companyCode = :incomingCompanyId ");
        }

        if (StringUtils.isNotBlank(componentId)) {
            sqlBuilder.append(" AND r.mstComponent.componentCode = :componentId ");
        }

        if (formatComponentInspectionFrom != null && formatComponentInspectionTo != null) {
            sqlBuilder.append(" AND ((r.outgoingInspectionDate >= :componentInspectionFrom AND r.outgoingInspectionDate <= :componentInspectionTo))");
        }
        
        
         Query query = entityManager.createQuery(sqlBuilder.toString());
         
         if (StringUtils.isNotBlank(incomingCompanyId)) {
            query.setParameter("incomingCompanyId", incomingCompanyId);
        }

        if (StringUtils.isNotBlank(componentId)) {
            query.setParameter("componentId", componentId);
        }
        if (formatComponentInspectionFrom != null && formatComponentInspectionTo != null) {
            query.setParameter("componentInspectionFrom", formatComponentInspectionFrom);
            query.setParameter("componentInspectionTo", formatComponentInspectionTo);
        }
        
        List<TblComponentInspectionResult> list = query.getResultList();

        return getCavInspectList(list, cavityPrefix, cavityNum);
    }
}
