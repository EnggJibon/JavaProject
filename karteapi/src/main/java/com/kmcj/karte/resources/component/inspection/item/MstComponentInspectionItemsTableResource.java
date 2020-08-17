/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ObjectResponse;
import com.kmcj.karte.resources.component.inspection.item.model.ComponentInspectionItemImportCSVResp;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.apiuser.MstApiUser;
import com.kmcj.karte.resources.apiuser.MstApiUserService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectFileService;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectType;
import com.kmcj.karte.resources.component.inspection.item.model.ComponentInspectionItemsTableForBatch;
import com.kmcj.karte.resources.component.inspection.item.model.ComponentInspectionItemsTableInfo;
import com.kmcj.karte.resources.component.inspection.item.model.ComponentInspectionSearchCondForCSV;
import com.kmcj.karte.resources.component.inspection.item.model.ComponentInspectionItemsTableResp;
import com.kmcj.karte.resources.component.inspection.item.model.ComponentInspectionItemsTableRespVo;
import com.kmcj.karte.resources.component.inspection.item.model.ComponentInspectionItemsTableSearchCond;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.user.MstUserService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;

/**
 * Master Component inspection item resource
 *
 * @author duanlin
 */
@RequestScoped
@Path("component/inspection/item")
public class MstComponentInspectionItemsTableResource {

    @Inject
    private CnfSystemService cnfSystemService;
    @Inject
    private MstComponentInspectionItemsTableService mstComponentInspectionItemService;
    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private KartePropertyService kartePropertyService;
    @Inject
    private TblCsvImportService tblCsvImportService;
    @Inject
    private MstUserService mstUserService;
    @Inject
    private MstApiUserService mstApiUserService;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;
    @Inject
    private MstComponentService mstComponentService;
    @Inject
    private MstComponentInspectFileService mstComponentInspectFileService;
    @Inject
    private MstCompanyService mstCompanyService;

    @Context
    private ContainerRequestContext requestContext;
    private final static int MAX_VALUE_LENGTH = 50;

    /**
     * Get search result count
     *
     * @param component
     * @param componentCode
     * @param componentName
     * @param componentInspectType
     * @param outgoingCompanyName
     * @param incomingCompanyName
     * @param itemApproveStatus
     * @param withHistory
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getSearchResultCount(@QueryParam("component") String component,
            @QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("componentInspectType") String componentInspectType,
            @QueryParam("outgoingCompanyName") String outgoingCompanyName,
            @QueryParam("incomingCompanyName") String incomingCompanyName,
            @QueryParam("itemApproveStatus") String itemApproveStatus,
            @QueryParam("withHistory") String withHistory) {

        CountResponse response = new CountResponse();
        
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        ComponentInspectionItemsTableSearchCond searchCondition = this.convertToSearchCondition(component, componentCode, componentName, componentInspectType, 
                outgoingCompanyName, incomingCompanyName, itemApproveStatus, withHistory);
        long count = this.mstComponentInspectionItemService.getSearchCount(searchCondition, loginUser.getLangId());
        response.setCount(count);

        CnfSystem cnfSystem = cnfSystemService.findByKey("system", "max_list_record_count");
        long sysCount = Long.parseLong(cnfSystem.getConfigValue());
        if (count > sysCount) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
            response.setErrorMessage(String.format(msg, sysCount));
        }
        return response;
    }

    /**
     * Get master component inspection item list
     *
     * @param component
     * @param componentCode
     * @param componentName
     * @param componentInspectType
     * @param outgoingCompanyName
     * @param incomingCompanyName
     * @param itemApproveStatus
     * @param withHistory
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionItemsTableResp getMstInspectionItemHeaderList(@QueryParam("component") String component, 
            @QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("componentInspectType") String componentInspectType,
            @QueryParam("outgoingCompanyName") String outgoingCompanyName,
            @QueryParam("incomingCompanyName") String incomingCompanyName,
            @QueryParam("itemApproveStatus") String itemApproveStatus,
            @QueryParam("withHistory") String withHistory) {
        
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        ComponentInspectionItemsTableSearchCond searchCondition = this.convertToSearchCondition(component, componentCode, componentName, componentInspectType, 
                outgoingCompanyName, incomingCompanyName, itemApproveStatus, withHistory);

        ComponentInspectionItemsTableResp response = new ComponentInspectionItemsTableResp();
        response.setInspectionItemsTableHeaderList(this.mstComponentInspectionItemService.getInspectionItemsTable(searchCondition, loginUser.getLangId()));

        return response;
    }

    /**
     * Get master component inspection item details
     *
     * @param inspectionItemId
     * @return
     */
    @GET
    @Path("{inspectionItemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionItemsTableResp getMstInspectionItemDetails(@PathParam("inspectionItemId") String inspectionItemId) {
        ComponentInspectionItemsTableResp response = new ComponentInspectionItemsTableResp();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        response.setInspectionItemsTableInfo(this.mstComponentInspectionItemService.getInspectionItemsTableDetails(inspectionItemId, loginUser.getLangId()));
        return response;
    }
    
    @GET
    @Path("/latest")
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<MstComponentInspectionItemsTable> getLatestInspectItemTbl(
            @QueryParam("componetid") String componentid, 
            @QueryParam("incompanyid") String inCompanyId, 
            @QueryParam("outcompanyid") String outCompanyId) {
        ObjectResponse<MstComponentInspectionItemsTable> res = new ObjectResponse<>();
        MstComponentInspectionItemsTable itemTbl = this.mstComponentInspectionItemService.checkCurrentInspectionItemsTable(componentid, outCompanyId, inCompanyId);
        if(itemTbl != null) {
            itemTbl.setMstCompanyOutgoing(null);
            itemTbl.setMstCompanyIncoming(null);
            itemTbl.setMstComponent(null);
            res.setObj(itemTbl);
        }
        return res;
    }

    // KM-394 ADDSインタフェースを作成 2017/11/1 penggd Start
    /**
     * Get master component inspection item details
     *
     * @param componentCode
     * @param outgoingCompanyCode
     * @return
     */
    @GET
    @Path("searchdetails/{componentCode}/{outgoingCompanyCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionItemsTableResp getMstInspectionItemDetailsByCondition(
            @PathParam("componentCode") String componentCode,
            @PathParam("outgoingCompanyCode") String outgoingCompanyCode) {

        ComponentInspectionItemsTableResp response = new ComponentInspectionItemsTableResp();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        MstCompany selfCompany = this.mstCompanyService.getSelfCompany();

        response.setInspectionItemsTableInfo(
                this.mstComponentInspectionItemService.getInspectionItemsTableDetailsByCondition(componentCode,
                        outgoingCompanyCode, loginUser.getLangId(), selfCompany.getId()));
        return response;
    }
    // KM-394 ADDSインタフェースを作成 2017/11/1 penggd End

    /**
     * Delete master component inspection item details
     *
     * @param inspectionItemId
     * @return
     */
    @DELETE
    @Path("{inspectionItemId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public BasicResponse delete(@PathParam("inspectionItemId") String inspectionItemId) {
        BasicResponse response = new BasicResponse();
        this.mstComponentInspectionItemService.deleteMstComponentInspectionItemsTableDetail(inspectionItemId);
        this.mstComponentInspectionItemService.deleteMstComponentInspectionItemsTable(inspectionItemId);
        mstComponentInspectFileService.deleteMstComponentInspectionItemsTableClass(inspectionItemId);
        return response;
    }

    @POST
    @Path("delete/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public BasicResponse deleteListInspectionItem(List<ComponentInspectionItemsTableInfo> listComponentInspectionItemsTableInfo) {
        BasicResponse response = new BasicResponse();
        for (ComponentInspectionItemsTableInfo itemsTableInfo : listComponentInspectionItemsTableInfo) {
            this.mstComponentInspectionItemService.deleteMstComponentInspectionItemsTableDetail(itemsTableInfo.getComponentInspectionItemsTableId());
            this.mstComponentInspectionItemService.deleteMstComponentInspectionItemsFile(itemsTableInfo.getComponentInspectionItemsTableId());
            this.mstComponentInspectionItemService.deleteMstComponentInspectionItemsTable(itemsTableInfo.getComponentInspectionItemsTableId());
            mstComponentInspectFileService.deleteMstComponentInspectionItemsTableClass(itemsTableInfo.getComponentInspectionItemsTableId());
        }

        return response;
    }

    /**
     * update component inspection item details localSeq
     *
     * @param componentInspectionItemsTableInfo
     * @return
     */
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateComponentInspectionItemsTableDetail(ComponentInspectionItemsTableInfo componentInspectionItemsTableInfo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentInspectionItemService.updateComponentInspectionItemsTableDetail(componentInspectionItemsTableInfo, loginUser);
    }

    /**
     * Hidden list component inspection item table
     * @param listComponentInspectionItemsTableInfo
     * @return
     */
    @PUT
    @Path("/hidden")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse hiddenComponentInspectionItemsTableDetail(List<ComponentInspectionItemsTableInfo> listComponentInspectionItemsTableInfo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentInspectionItemService.updateApplyEndDateComponentInspectionItems(listComponentInspectionItemsTableInfo, loginUser);
    }
    
    @POST
    @Path("/redisplay")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<String> redisplay(List<String> itemids) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        ObjectResponse<String> resp = new ObjectResponse<>();
        List<String> failed = mstComponentInspectionItemService.redesplayItems(itemids, loginUser);
        if(failed.size() > 0) {
            resp.setObj("ContainsNotRedisplayable");
        } else {
            resp.setObj("success");
        }
        return resp;
    }
    
    /**
     * Approve
     *
     * @param mstComponentInspectionItemsTables
     * @return
     */
    @POST
    @Path("/approve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public BasicResponse approve(List<MstComponentInspectionItemsTableList> mstComponentInspectionItemsTables) {

        BasicResponse response = new BasicResponse();

        for (MstComponentInspectionItemsTableList mstComponentInspectionItemsTableList : mstComponentInspectionItemsTables) {

            MstComponentInspectionItemsTable mstComponentInspectionItemsTable = this.mstComponentInspectionItemService.findById(mstComponentInspectionItemsTableList.getInspectionItemId());
            if (mstComponentInspectionItemsTable != null) {
                Date sysDate = new Date();
                LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
                if (CommonConstants.ITEM_APPROVE_YES == Integer.valueOf(mstComponentInspectionItemsTableList.getItemApproveStatus())) {
                    MstComponentInspectionItemsTable mstComponentInspectionItemsTableForOldVersion
                            = this.mstComponentInspectionItemService.getOldVersionItem(mstComponentInspectionItemsTable);
                    // 検査管理項目の承認/否認を対応 20180919 start
                    if (mstComponentInspectionItemsTableForOldVersion != null) {
                        mstComponentInspectionItemsTableForOldVersion.setUpdateDate(sysDate);
                        mstComponentInspectionItemsTableForOldVersion.setUpdateUserUuid(loginUser.getUserUuid());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar c = Calendar.getInstance();
                        c.setTime(sysDate);
                        c.add(Calendar.DATE, -1);
                        mstComponentInspectionItemsTableForOldVersion.setApplyEndDate(c.getTime());
                        this.mstComponentInspectionItemService.update(mstComponentInspectionItemsTableForOldVersion);
                    }
                    mstComponentInspectionItemsTable.setApplyStartDate(sysDate);
                }
                mstComponentInspectionItemsTable.setApprovePersonUuid(loginUser.getUserUuid());
                mstComponentInspectionItemsTable.setApprovePersonName(mstUserService.getMstUser(loginUser.getUserid()).getUserName());
                mstComponentInspectionItemsTable.setApproveDate(sysDate);
                mstComponentInspectionItemsTable.setUpdateDate(sysDate);
                mstComponentInspectionItemsTable.setUpdateUserUuid(loginUser.getUserUuid());
                
                // 承認ステータス
                mstComponentInspectionItemsTable.setItemApproveStatus(Integer.valueOf(mstComponentInspectionItemsTableList.getItemApproveStatus()));
                // 承認コメント
                mstComponentInspectionItemsTable.setItemApproveComment(mstComponentInspectionItemsTableList.getItemApproveComment());
                this.mstComponentInspectionItemService.update(mstComponentInspectionItemsTable);
                // 検査管理項目の承認/否認を対応 20180919 end

                if (mstComponentInspectionItemsTableList.getMstComponentInspectionItemsTableDetailList() != null
                        && mstComponentInspectionItemsTableList.getMstComponentInspectionItemsTableDetailList().size() > 0) {
                    for (MstComponentInspectionItemsTableDetail itemDetail : mstComponentInspectionItemsTableList.getMstComponentInspectionItemsTableDetailList()) {
                        if (itemDetail != null && StringUtils.isNotEmpty(itemDetail.getId())) {
                            int localSeq = itemDetail.getLocalSeq();
                            boolean enableThAlert = itemDetail.isEnableThAlert();
                            Query query = entityManager.createNamedQuery("MstComponentInspectionItemsTableDetail.findById", MstComponentInspectionItemsTableDetail.class)
                                    .setParameter("id", itemDetail.getId());
                            itemDetail = (MstComponentInspectionItemsTableDetail) query.getSingleResult();
                            itemDetail.setLocalSeq(localSeq);
                            itemDetail.setEnableThAlert(enableThAlert);
                            itemDetail.setUpdateUserUuid(loginUser.getUserUuid());
                            itemDetail.setUpdateDate(sysDate);
                            entityManager.merge(itemDetail);
                            entityManager.flush();
                            entityManager.clear();
                        } else {
                            response.setError(true);
                            response.setErrorCode(ErrorMessages.E201_APPLICATION);
                            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found");
                            response.setErrorMessage(msg);
                        }
                    }
                }
            }

        }
        return response;
    }

    @POST
    @Path("/importcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public ComponentInspectionItemImportCSVResp importCsv(ComponentInspectionSearchCondForCSV componentInspectionItemCSV) {
        ComponentInspectionItemImportCSVResp response = new ComponentInspectionItemImportCSVResp();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        boolean errorFlag = false;
        long succeededCount = 0;
        long addedCount = 0;
        long updatedCount = 0;
        long deletedCount = 0;
        long failedCount = 0;
        String logFileUuid = IDGenerator.generate();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, componentInspectionItemCSV.getFileUuid());
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            response.setErrorMessage(msg);
            return response;
        }
        CSVFileUtil csvFileUtil = null;
        ArrayList readList = new ArrayList();
        try {
            csvFileUtil = new CSVFileUtil(csvFile);
            boolean readEnd = false;
            do {
                String readLine = csvFileUtil.readLine();
                if (StringUtils.isEmpty(readLine)) {
                    readEnd = true;
                } else {
                    readList.add(CSVFileUtil.fromCSVLinetoArray(readLine));
                }
            } while (!readEnd);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }
        if (readList.size() <= 1) {
            return response;
        } else {
            Date sysDate = new Date();
            String mstComponentInspectionItemId = IDGenerator.generate();
            BasicResponse checkResult;

            String userLangId = loginUser.getLangId();
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
            String lineNo = mstDictionaryService.getDictionaryValue(userLangId, "row_number");
            String error = mstDictionaryService.getDictionaryValue(userLangId, "error");
            String errorContents = mstDictionaryService.getDictionaryValue(userLangId, "error_detail");
            // 追加列
            String revisionSymbol = mstDictionaryService.getDictionaryValue(userLangId, "revision_symbol");
            String drawingPage = mstDictionaryService.getDictionaryValue(userLangId, "drawing_page");
            String drawingAnnotation = mstDictionaryService.getDictionaryValue(userLangId, "drawing_annotation");
            String drawingMentionNo = mstDictionaryService.getDictionaryValue(userLangId, "drawing_mention_no");
            String similarMultiitem = mstDictionaryService.getDictionaryValue(userLangId, "similar_multiitem");
            String drawingArea = mstDictionaryService.getDictionaryValue(userLangId, "drawing_area");
            String pqs = mstDictionaryService.getDictionaryValue(userLangId, "pqs");

            String inspectionItemNames = mstDictionaryService.getDictionaryValue(userLangId, "inspection_item_names");
            String dimensionValue = mstDictionaryService.getDictionaryValue(userLangId, "dimension_value");
            String tolerancePlus = mstDictionaryService.getDictionaryValue(userLangId, "tolerance_plus");
            String toleranceMinus = mstDictionaryService.getDictionaryValue(userLangId, "tolerance_minus");
            String measurementMethod = mstDictionaryService.getDictionaryValue(userLangId, "measurement_method");
            String outgoingTrial = mstDictionaryService.getDictionaryValue(userLangId, "outgoing_trial_inspection_object");
            String outgoingProduction = mstDictionaryService.getDictionaryValue(userLangId, "outgoing_production_inspection_object");
            String incomingTrial = mstDictionaryService.getDictionaryValue(userLangId, "incoming_trial_inspection_object");
            String incomingProduction = mstDictionaryService.getDictionaryValue(userLangId, "incoming_production_inspection_object");
            String measurementType = mstDictionaryService.getDictionaryValue(userLangId, "measurement_type");
            String dimensionValueErrorMsg = mstDictionaryService.getDictionaryValue(userLangId, "dimension_value_repeat_error");
            String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.MST_COMPONENT_INSPECTION_ITEM_DETAIL);
            Map<String, String> logParm = new HashMap<>();
            logParm.put("lineNo", lineNo);
            logParm.put("revisionSymbol", revisionSymbol);
            logParm.put("drawingPage", drawingPage);
            logParm.put("drawingAnnotation", drawingAnnotation);
            logParm.put("drawingMentionNo", drawingMentionNo);
            logParm.put("similarMultiitem", similarMultiitem);
            logParm.put("drawingArea", drawingArea);
            logParm.put("pqs", pqs);
            logParm.put("inspectionItemNames", inspectionItemNames);
            logParm.put("dimensionValue", dimensionValue);
            logParm.put("tolerancePlus", tolerancePlus);
            logParm.put("toleranceMinus", toleranceMinus);
            logParm.put("measurementMethod", measurementMethod);
            logParm.put("outgoingTrial", outgoingTrial);
            logParm.put("outgoingProduction", outgoingProduction);
            logParm.put("incomingTrial", incomingTrial);
            logParm.put("incomingProduction", incomingProduction);
            logParm.put("measurementType", measurementType);
            logParm.put("error", error);
            logParm.put("errorContents", errorContents);
            
            //Apeng 20180125 add start
            String processInspetionObject = mstDictionaryService.getDictionaryValue(userLangId, "procedure_internal_inspection");//工程内検査
            logParm.put("processInspetionObject", processInspetionObject);
            //Apeng 20180125 add end
            
            // CSV取込の場合、非連携フラグを追加 start
            String additionalFlg = mstDictionaryService.getDictionaryValue(userLangId, "additional_flg");//非連携フラグ
            logParm.put("additionalFlg", additionalFlg);
            // CSV取込の場合、非連携フラグを追加 end
            
            // 量産第一ロット対象を追加
            String outgoingFirstMass = mstDictionaryService.getDictionaryValue(userLangId, "outgoing_first_mass_production_object");
            logParm.put("outgoingFirstMass", outgoingFirstMass);
            String incomingFirstMass = mstDictionaryService.getDictionaryValue(userLangId, "incoming_first_mass_production_object");
            logParm.put("incomingFirstMass", incomingFirstMass);
            
            Map<String, MstComponentInspectionItemsTableDetail> oldVerDetail = mstComponentInspectionItemService.getOldVersionItemDetails(componentInspectionItemCSV.getComponentId(), componentInspectionItemCSV.getOutgoingCompanyId(), componentInspectionItemCSV.getIncomingCompanyId());

            List<MstComponentInspectionItemsTableDetail> mstComponentInspectionItemDetailList = new ArrayList<>();
            FileUtil fu = new FileUtil();
            Map<String, String> resultMap = new HashMap<>();
            String strRevisionSymbol;
            String strDrawingPage;
            String strDrawingAnnotation;
            String strDrawingMentionNo;
            String strSimilarMultiitem;
            String strDrawingArea;
            String strPqs;
            String strInspectionItemName;
            String itemtableDetailMethodId;
            String containsKey;
            for (int i = 1; i < readList.size(); i++) {
                ArrayList comList = (ArrayList) readList.get(i);
                int comListSize = comList.size();
                if (comListSize != 17 && comListSize != 18 && comListSize != 19 && comListSize != 21 && comListSize != 22) {
                    //エラー情報をログファイルに記入
                    fu.writeInfoToFile(logFile, fu.outValue(
                            lineNo, i, "", "", error, 1, errorContents, mstDictionaryService.getDictionaryValue(userLangId, "csv_column_error_msg")));
                    errorFlag = true;
                    continue;
                }

                strRevisionSymbol = convertToStringForCSV(comList.get(0));
                strDrawingPage = convertToStringForCSV(comList.get(1));
                strDrawingAnnotation = convertToStringForCSV(comList.get(2));
                strDrawingMentionNo = convertToStringForCSV(comList.get(3));
                strSimilarMultiitem = convertToStringForCSV(comList.get(4));
                strDrawingArea = convertToStringForCSV(comList.get(5));
                strPqs = convertToStringForCSV(comList.get(6));
                strInspectionItemName = convertToStringForCSV(comList.get(7));

                //寸法ID Apeng 20180111 add
                itemtableDetailMethodId = (strDrawingPage == null ? "NULL" : strDrawingPage)
                        + (strDrawingAnnotation == null ? "NULL" : strDrawingAnnotation)
                        + (strDrawingMentionNo == null ? "NULL" : strDrawingMentionNo)
                        + (strSimilarMultiitem == null ? "NULL" : strSimilarMultiitem);
                containsKey = itemtableDetailMethodId.concat("|");
                if (resultMap.containsKey(containsKey)) {
                    //エラー情報をログファイルに記入
                    fu.writeInfoToFile(logFile, fu.outValue(
                            lineNo, i, "", "", error, 1, errorContents, dimensionValueErrorMsg));
                    errorFlag = true;
                    continue;
                }
                resultMap.put(containsKey, containsKey);

                MstComponentInspectionItemsTableDetail mstComponentInspectionItemDetail = new MstComponentInspectionItemsTableDetail();
                mstComponentInspectionItemDetail.setId(IDGenerator.generate());
                mstComponentInspectionItemDetail.setComponentInspectionItemsTableId(mstComponentInspectionItemId);
                if (i < 10) {
                    mstComponentInspectionItemDetail.setInspectionItemSno("ITEM00" + i);
                } else if (i >= 100) {
                    mstComponentInspectionItemDetail.setInspectionItemSno("ITEM" + i);
                } else {
                    mstComponentInspectionItemDetail.setInspectionItemSno("ITEM0" + i);
                }

                // 追加列
                mstComponentInspectionItemDetail.setRevisionSymbol(strRevisionSymbol);
                mstComponentInspectionItemDetail.setDrawingPage(strDrawingPage);
                mstComponentInspectionItemDetail.setDrawingAnnotation(strDrawingAnnotation);
                mstComponentInspectionItemDetail.setDrawingMentionNo(strDrawingMentionNo);
                mstComponentInspectionItemDetail.setSimilarMultiitem(strSimilarMultiitem);
                mstComponentInspectionItemDetail.setDrawingArea(strDrawingArea);
                mstComponentInspectionItemDetail.setPqs(strPqs);
                mstComponentInspectionItemDetail.setInspectionItemName(strInspectionItemName);

                mstComponentInspectionItemDetail.setDimensionValue(convertDoubleToBigDecimalForCSV(comList.get(8)));
                mstComponentInspectionItemDetail.setTolerancePlus(convertDoubleToBigDecimalForCSV(comList.get(9)));
                mstComponentInspectionItemDetail.setToleranceMinus(convertDoubleToBigDecimalForCSV(comList.get(10)));
                mstComponentInspectionItemDetail.setMeasurementMethod(convertToStringForCSV(comList.get(11)));
                mstComponentInspectionItemDetail.setOutgoingTrialInspectionObject(convertToCharForCSV(comList.get(12)));
                mstComponentInspectionItemDetail.setOutgoingProductionInspectionObject(convertToCharForCSV(comList.get(13)));
                mstComponentInspectionItemDetail.setIncomingTrialInspectionObject(convertToCharForCSV(comList.get(14)));
                mstComponentInspectionItemDetail.setIncomingProductionInspectionObject(convertToCharForCSV(comList.get(15)));
                mstComponentInspectionItemDetail.setMeasurementType(convertToIntegerForCSV(comList.get(16)));
                mstComponentInspectionItemDetail.setCreateDate(sysDate);
                mstComponentInspectionItemDetail.setCreateUserUuid(loginUser.getUserUuid());
                mstComponentInspectionItemDetail.setUpdateDate(sysDate);
                mstComponentInspectionItemDetail.setUpdateUserUuid(loginUser.getUserUuid());
                String dimId = mstComponentInspectionItemService.toDimensionId(mstComponentInspectionItemDetail);
                if(oldVerDetail.containsKey(dimId)) {
                    mstComponentInspectionItemDetail.setEnableThAlert(oldVerDetail.get(dimId).isEnableThAlert());
                }
                
                //Apeng 20180130 add start
                if (comListSize == 17) {
                    mstComponentInspectionItemDetail.setProcessInspetionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                    mstComponentInspectionItemDetail.setAdditionalFlg(CommonConstants.ITEMS_TABLE_ADDITIONAL_FLG_YES.charAt(0));
                    mstComponentInspectionItemDetail.setOutgoingFirstMassProductionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                    mstComponentInspectionItemDetail.setIncomingFirstMassProductionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                } else if (comListSize == 18) {
                    mstComponentInspectionItemDetail.setProcessInspetionObject(convertToCharForCSV(comList.get(17)));
                    mstComponentInspectionItemDetail.setAdditionalFlg(CommonConstants.ITEMS_TABLE_ADDITIONAL_FLG_YES.charAt(0));
                    mstComponentInspectionItemDetail.setOutgoingFirstMassProductionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                    mstComponentInspectionItemDetail.setIncomingFirstMassProductionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                } else if (comListSize == 19){
                    mstComponentInspectionItemDetail.setProcessInspetionObject(convertToCharForCSV(comList.get(17)));
                    mstComponentInspectionItemDetail.setAdditionalFlg(convertToCharForCSV(comList.get(18)));
                    mstComponentInspectionItemDetail.setOutgoingFirstMassProductionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                    mstComponentInspectionItemDetail.setIncomingFirstMassProductionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                }else {
                    mstComponentInspectionItemDetail.setProcessInspetionObject(convertToCharForCSV(comList.get(17)));
                    mstComponentInspectionItemDetail.setAdditionalFlg(convertToCharForCSV(comList.get(18)));
                    mstComponentInspectionItemDetail.setOutgoingFirstMassProductionObject(convertToCharForCSV(comList.get(19)));
                    mstComponentInspectionItemDetail.setIncomingFirstMassProductionObject(convertToCharForCSV(comList.get(20)));
                }
                if(comListSize > 21) {
                    mstComponentInspectionItemDetail.setEnableThAlert("1".equals(comList.get(21)));
                }
                //Apeng 20180130 add end
                
                checkResult = componentInspectionCSVCheck(mstComponentInspectionItemDetail, userLangId, fu, logFile, i, logParm, false);
                if (checkResult.isError()) {
                    errorFlag = true;
                    continue;
                }
                mstComponentInspectionItemDetailList.add(mstComponentInspectionItemDetail);
            }
            if (!errorFlag) {
                addedCount = readList.size() - 1;
                succeededCount = addedCount;

                MstComponentInspectionItemsTable mstComponentInspectionItem = new MstComponentInspectionItemsTable();
                mstComponentInspectionItem.setId(mstComponentInspectionItemId);
                mstComponentInspectionItem.setComponentId(componentInspectionItemCSV.getComponentId());
                mstComponentInspectionItem.setOutgoingCompanyId(componentInspectionItemCSV.getOutgoingCompanyId());
                // 検査管理項目作成の場合、納品先を追加　20180925 start
                if (StringUtils.isNotEmpty(componentInspectionItemCSV.getIncomingCompanyId())) {
                    mstComponentInspectionItem.setIncomingCompanyId(componentInspectionItemCSV.getIncomingCompanyId());
                } else {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "inspection_item_no_incoming_company");
                    response.setErrorMessage(msg);
                    return response;
                }
                // 検査管理項目作成の場合、納品先を追加　20180925 end
                Integer version = mstComponentInspectionItemService.getMaxVersion(
                        componentInspectionItemCSV.getComponentId(),
                        componentInspectionItemCSV.getOutgoingCompanyId(),
                        componentInspectionItemCSV.getIncomingCompanyId()
                );
                mstComponentInspectionItem.setVersion(version == null ? 1 : version + 1);
                mstComponentInspectionItem.setEntryPersonUuid(loginUser.getUserUuid());
                mstComponentInspectionItem.setEntryPersonName(mstUserService.getMstUser(loginUser.getUserid()).getUserName());
                // check whether the record is batch process target
                if (!Objects.equals(mstComponentInspectionItem.getOutgoingCompanyId(), mstComponentInspectionItem.getIncomingCompanyId())) {
                    mstComponentInspectionItem.setInspBatchUpdateStatus(CommonConstants.INSP_BATCH_UPDATE_STATUS_ITEM_NOT_PUSH);
                }
                mstComponentInspectionItem.setCreateDate(sysDate);
                mstComponentInspectionItem.setUpdateDate(sysDate);
                mstComponentInspectionItem.setCreateUserUuid(loginUser.getUserUuid());
                mstComponentInspectionItem.setUpdateUserUuid(loginUser.getUserUuid());
                mstComponentInspectionItem.setItemApproveStatus(CommonConstants.ITEM_APPROVE_UNTREATED);

                //部品業種取得
                List<MstComponentInspectType> inseptionTypeList = mstComponentInspectFileService.getTypeSql(loginUser.getLangId(),
                        componentInspectionItemCSV.getInseptionTypeKey(), componentInspectionItemCSV.getInseptionTypeValue(), null, null);
                MstComponentInspectType mstComponentInspectType;
                if (inseptionTypeList != null && inseptionTypeList.size() > 0) {
                    mstComponentInspectType = inseptionTypeList.get(0);
                    mstComponentInspectionItem.setInspectTypeId(mstComponentInspectType.getId());//Apeng 20171026 ADD
                    mstComponentInspectionItem.setInspectTypeDictKey(mstComponentInspectType.getDictKey());//Apeng 20171026 ADD
                } else {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found");
                    response.setErrorMessage(msg);
                    return response;
                }
                MstComponentInspectionItemsTable prev = mstComponentInspectionItemService.getOldVersionItem(mstComponentInspectionItem);
                if(prev != null) {
                    mstComponentInspectionItem.setMeasSampleRatio(prev.getMeasSampleRatio());
                    mstComponentInspectionItem.setVisSampleRatio(prev.getVisSampleRatio());
                } else {
                    BigDecimal measSampleRatio = new BigDecimal(cnfSystemService.findByKey("system", "component_insp_measure_sampling_quantity").getConfigValue());
                    mstComponentInspectionItem.setMeasSampleRatio(measSampleRatio);
                    BigDecimal visSampleRatio = new BigDecimal(cnfSystemService.findByKey("system", "component_insp_visual_sampling_quantity").getConfigValue());
                    mstComponentInspectionItem.setVisSampleRatio(visSampleRatio);
                }
                
                mstComponentInspectionItemService.createMstComponentInspectionItem(mstComponentInspectionItem);
                
                // 非連携管理項目をコピュー 20180918 start
                copyItemsDetail(mstComponentInspectionItemDetailList, mstComponentInspectionItemId, componentInspectionItemCSV.getComponentId(), componentInspectionItemCSV.getOutgoingCompanyId(), componentInspectionItemCSV.getIncomingCompanyId(), addedCount, resultMap);
                // 非連携管理項目をコピュー 20180918 end

                for (int i = 0; i < mstComponentInspectionItemDetailList.size(); i++) {
                    MstComponentInspectionItemsTableDetail detail = mstComponentInspectionItemDetailList.get(i);
                    this.mstComponentInspectionItemService.setInspectionItemDetail(detail);
                }
                response.setInspectionItemId(mstComponentInspectionItemId);
            } else {
                failedCount = readList.size() - 1;
            }
            response.setTotalCount(readList.size() - 1);
            response.setSucceededCount(succeededCount);
            response.setAddedCount(addedCount);
            response.setUpdatedCount(updatedCount);
            response.setDeletedCount(deletedCount);
            response.setFailedCount(failedCount);
            response.setLog(logFileUuid);

            //アップロードログをテーブルに書き出し
            TblCsvImport tblCsvImport = new TblCsvImport();
            tblCsvImport.setImportUuid(IDGenerator.generate());
            tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
            tblCsvImport.setImportDate(new Date());
            tblCsvImport.setImportTable(CommonConstants.MST_COMPONENT_INSPECTION_ITEM_DETAIL);
            TblUploadFile tblUploadFile = new TblUploadFile();
            tblUploadFile.setFileUuid(componentInspectionItemCSV.getFileUuid());
            tblCsvImport.setUploadFileUuid(tblUploadFile);
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_INSPECTION_ITEM);
            tblCsvImport.setFunctionId(mstFunction);
            tblCsvImport.setRecordCount(readList.size() - 1);
            tblCsvImport.setSuceededCount(convertToIntegerForCSV(succeededCount));
            tblCsvImport.setAddedCount(convertToIntegerForCSV(addedCount));
            tblCsvImport.setUpdatedCount(convertToIntegerForCSV(updatedCount));
            tblCsvImport.setDeletedCount(convertToIntegerForCSV(deletedCount));
            tblCsvImport.setFailedCount(convertToIntegerForCSV(failedCount));
            tblCsvImport.setLogFileUuid(logFileUuid);

            //String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.MST_COMPONENT_INSPECTION_ITEM_DETAIL);
            tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

            tblCsvImportService.createCsvImpor(tblCsvImport);
            return response;
        }
    }

    /**
     * 複数CSV
     *
     * @param componentInspectionItemCSV
     * @return
     */
    @POST
    @Path("importcsvs")
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionItemsTableList importcsvs(ComponentInspectionSearchCondForCSV componentInspectionItemCSV) {
        String fileUuids = componentInspectionItemCSV.getFileUuid();
        ComponentInspectionItemsTableList response = new ComponentInspectionItemsTableList();
        List<MstComponentInspectionItemsTableResp> componentInspectionItemsTableRespSuccess = new ArrayList();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        List<MstComponentInspectionItemsTableResp> componentInspectionItemsTableRespError = new ArrayList();
        MstComponentInspectionItemsTableResp resp;
        if (StringUtils.isNotEmpty(fileUuids)) {
            for (String fileUuid : fileUuids.split(",")) {
                String componentCode = "";
                try {
                    TblUploadFile tblUploadFile = entityManager.find(TblUploadFile.class, fileUuid);
                    String fileName;
                    String inseptionTypeValue;
                    if (tblUploadFile != null) {
                        fileName = tblUploadFile.getUploadFileName();
                    } else {
                        resp = new MstComponentInspectionItemsTableResp();
                        resp.setComponentCode(componentCode);
                        resp.setErrorReason(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
                        componentInspectionItemsTableRespError.add(resp);
                        response.setComponentInspectionItemsTableRespError(componentInspectionItemsTableRespError);
                        continue;
                    }
                    String[] strArray = fileName.split("_");
                    componentCode = strArray[0];
                    inseptionTypeValue = strArray[1].split("\\.")[0];

                    //部品取得check
                    MstComponent mstComponent = mstComponentService.getMstComponent(componentCode);
                    if (mstComponent == null) {
                        resp = new MstComponentInspectionItemsTableResp();
                        resp.setComponentCode(componentCode);
                        resp.setErrorReason(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
                        componentInspectionItemsTableRespError.add(resp);
                        response.setComponentInspectionItemsTableRespError(componentInspectionItemsTableRespError);
                        continue;
                    }
                    // 検査管理項目作成の場合、納品先を追加　20180925 start
                    if (!StringUtils.isNotBlank(componentInspectionItemCSV.getIncomingCompanyId())) {
                        resp = new MstComponentInspectionItemsTableResp();
                        resp.setComponentCode(componentCode);
                        resp.setErrorReason(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "inspection_item_no_incoming_company"));
                        componentInspectionItemsTableRespError.add(resp);
                        response.setComponentInspectionItemsTableRespError(componentInspectionItemsTableRespError);
                        continue;
                    }
                    // 検査管理項目作成の場合、納品先を追加　20180925 end

                    //部品業種取得check
                    if (!StringUtils.isNotBlank(inseptionTypeValue)) {
                        resp = new MstComponentInspectionItemsTableResp();
                        resp.setComponentCode(componentCode);
                        resp.setErrorReason(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
                        componentInspectionItemsTableRespError.add(resp);
                        response.setComponentInspectionItemsTableRespError(componentInspectionItemsTableRespError);
                        continue;
                    }
                    List<MstComponentInspectType> inseptionTypeList = mstComponentInspectFileService.getTypeSql(loginUser.getLangId(), null, inseptionTypeValue, null, null);
                    MstComponentInspectType mstComponentInspectType;
                    if (inseptionTypeList != null && inseptionTypeList.size() > 0) {
                        mstComponentInspectType = (MstComponentInspectType) inseptionTypeList.get(0);
                    } else {
                        resp = new MstComponentInspectionItemsTableResp();
                        resp.setComponentCode(componentCode);
                        resp.setErrorReason(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
                        componentInspectionItemsTableRespError.add(resp);
                        response.setComponentInspectionItemsTableRespError(componentInspectionItemsTableRespError);
                        continue;
                    }

                    //部品検査項目表マスタ未承認check
                    List<MstComponentInspectionItemsTable> mstComponentInspectionItemsTableList
                            = mstComponentInspectionItemService.getNotApprove(mstComponent.getId(), componentInspectionItemCSV.getOutgoingCompanyId(), componentInspectionItemCSV.getIncomingCompanyId());
                    if (mstComponentInspectionItemsTableList != null && mstComponentInspectionItemsTableList.size() >= 1) {
                        resp = new MstComponentInspectionItemsTableResp();
                        resp.setComponentCode(componentCode);
                        resp.setErrorReason(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "csv_import_for_not_approve_error_msg"));
                        componentInspectionItemsTableRespError.add(resp);
                        response.setComponentInspectionItemsTableRespError(componentInspectionItemsTableRespError);
                        continue;
                    }
                    boolean errorFlag = false;
                    long succeededCount = 0;
                    long addedCount = 0;
                    long updatedCount = 0;
                    long deletedCount = 0;
                    long failedCount = 0;
                    String logFileUuid = IDGenerator.generate();
                    String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
                    if (!csvFile.endsWith(CommonConstants.CSV)) {
                        resp = new MstComponentInspectionItemsTableResp();
                        resp.setComponentCode(componentCode);
                        resp.setErrorReason(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout"));
                        componentInspectionItemsTableRespError.add(resp);
                        response.setComponentInspectionItemsTableRespError(componentInspectionItemsTableRespError);
                        continue;
                    }
                    ArrayList readList = CSVFileUtil.readCsv(csvFile);
                    if (readList.size() <= 1) {
                        resp = new MstComponentInspectionItemsTableResp();
                        resp.setComponentCode(componentCode);
                        resp.setErrorReason(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
                        componentInspectionItemsTableRespError.add(resp);
                        response.setComponentInspectionItemsTableRespError(componentInspectionItemsTableRespError);
                    } else {
                        Date sysDate = new Date();
                        String mstComponentInspectionItemId = IDGenerator.generate();
                        BasicResponse checkResult;
                        Map<String, MstComponentInspectionItemsTableDetail> oldVerDetail = mstComponentInspectionItemService.getOldVersionItemDetails(mstComponent.getId(), componentInspectionItemCSV.getOutgoingCompanyId(), componentInspectionItemCSV.getIncomingCompanyId());

                        String userLangId = loginUser.getLangId();
                        String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
                        Map<String, String> headerMap = getDictionaryList(userLangId);
                        String lineNo = headerMap.get("row_number");
                        String error = headerMap.get("error");
                        String errorContents = headerMap.get("error_detail");
                        // 追加列
                        String revisionSymbol = headerMap.get("revision_symbol");
                        String drawingPage = headerMap.get("drawing_page");
                        String drawingAnnotation = headerMap.get("drawing_annotation");
                        String drawingMentionNo = headerMap.get("drawing_mention_no");
                        String similarMultiitem = headerMap.get("similar_multiitem");
                        String drawingArea = headerMap.get("drawing_area");
                        String pqs = headerMap.get("pqs");
                        String inspectionItemNames = headerMap.get("inspection_item_names");
                        String dimensionValue = headerMap.get("dimension_value");
                        String tolerancePlus = headerMap.get("tolerance_plus");
                        String toleranceMinus = headerMap.get("tolerance_minus");
                        String measurementMethod = headerMap.get("measurement_method");
                        String outgoingTrial = headerMap.get("outgoing_trial_inspection_object");
                        String outgoingProduction = headerMap.get("outgoing_production_inspection_object");
                        String incomingTrial = headerMap.get("incoming_trial_inspection_object");
                        String incomingProduction = headerMap.get("incoming_production_inspection_object");
                        String measurementType = headerMap.get("measurement_type");
                        String dimensionValueErrorMsg = headerMap.get("dimension_value_repeat_error");
                        Map<String, String> logParm = new HashMap<>();
                        logParm.put("lineNo", lineNo);
                        logParm.put("revisionSymbol", revisionSymbol);
                        logParm.put("drawingPage", drawingPage);
                        logParm.put("drawingAnnotation", drawingAnnotation);
                        logParm.put("drawingMentionNo", drawingMentionNo);
                        logParm.put("similarMultiitem", similarMultiitem);
                        logParm.put("drawingArea", drawingArea);
                        logParm.put("pqs", pqs);
                        logParm.put("inspectionItemNames", inspectionItemNames);
                        logParm.put("dimensionValue", dimensionValue);
                        logParm.put("tolerancePlus", tolerancePlus);
                        logParm.put("toleranceMinus", toleranceMinus);
                        logParm.put("measurementMethod", measurementMethod);
                        logParm.put("outgoingTrial", outgoingTrial);
                        logParm.put("outgoingProduction", outgoingProduction);
                        logParm.put("incomingTrial", incomingTrial);
                        logParm.put("incomingProduction", incomingProduction);
                        logParm.put("measurementType", measurementType);
                        logParm.put("error", error);
                        logParm.put("errorContents", errorContents);

                        //Apeng 20180130 add start
                        String processInspetionObject = mstDictionaryService.getDictionaryValue(userLangId, "procedure_internal_inspection");//工程内検査
                        logParm.put("processInspetionObject", processInspetionObject);
                        //Apeng 20180130 add end

                        // CSV取込の場合、非連携フラグを追加 start
                        String additionalFlg = mstDictionaryService.getDictionaryValue(userLangId, "additional_flg");//非連携フラグ
                        logParm.put("additionalFlg", additionalFlg);
                        // CSV取込の場合、非連携フラグを追加 end
                        
                        // 量産第一ロット対象を追加
                        String outgoingFirstMass = mstDictionaryService.getDictionaryValue(userLangId, "outgoing_first_mass_production_object");
                        logParm.put("outgoingFirstMass", outgoingFirstMass);
                        String incomingFirstMass = mstDictionaryService.getDictionaryValue(userLangId, "incoming_first_mass_production_object");
                        logParm.put("incomingFirstMass", incomingFirstMass);

                        List<MstComponentInspectionItemsTableDetail> mstComponentInspectionItemDetailList = new ArrayList<>();
                        FileUtil fu = new FileUtil();
                        Integer measurementQuantity = 0;
                        Integer visuallyQuantity = 0;
                        String checkMsg = "";
                        String strRevisionSymbol;
                        String strDrawingPage;
                        String strDrawingAnnotation;
                        String strDrawingMentionNo;
                        String strSimilarMultiitem;
                        String strDrawingArea;
                        String strPqs;
                        String strInspectionItemName;
                        Map<String, String> resultMap = new HashMap<>();
                        String itemtableDetailMethodId;
                        String containsKey;
                        int comListSize;
                        for (int i = 1; i < readList.size(); i++) {
                            ArrayList comList = (ArrayList) readList.get(i);
                            comListSize = comList.size();
                            if (comListSize != 17 && comListSize != 18 && comListSize != 19 && comListSize != 21 && comListSize != 22) {

                                String errorMessage = mstDictionaryService.getDictionaryValue(userLangId, "csv_column_error_msg");

                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(
                                        lineNo, i, "", "", error, 1, errorContents, errorMessage));
                                errorFlag = true;
                                checkMsg = errorMessage;
                                break;
                            }

                            strRevisionSymbol = convertToStringForCSV(comList.get(0));
                            strDrawingPage = convertToStringForCSV(comList.get(1));
                            strDrawingAnnotation = convertToStringForCSV(comList.get(2));
                            strDrawingMentionNo = convertToStringForCSV(comList.get(3));
                            strSimilarMultiitem = convertToStringForCSV(comList.get(4));
                            strDrawingArea = convertToStringForCSV(comList.get(5));
                            strPqs = convertToStringForCSV(comList.get(6));
                            strInspectionItemName = convertToStringForCSV(comList.get(7));

                            //寸法ID Apeng 20180111 add
                            itemtableDetailMethodId = (strDrawingPage == null ? "NULL" : strDrawingPage)
                                    + (strDrawingAnnotation == null ? "NULL" : strDrawingAnnotation)
                                    + (strDrawingMentionNo == null ? "NULL" : strDrawingMentionNo)
                                    + (strSimilarMultiitem == null ? "NULL" : strSimilarMultiitem);
                            containsKey = itemtableDetailMethodId.concat("|");
                            if (resultMap.containsKey(containsKey)) {
                                //エラー情報をログファイルに記入
                                fu.writeInfoToFile(logFile, fu.outValue(
                                        lineNo, i, "", "", error, 1, errorContents, dimensionValueErrorMsg));
                                errorFlag = true;
                                checkMsg = dimensionValueErrorMsg;
                                continue;
                            }
                            resultMap.put(containsKey, containsKey);

                            MstComponentInspectionItemsTableDetail mstComponentInspectionItemDetail = new MstComponentInspectionItemsTableDetail();
                            mstComponentInspectionItemDetail.setId(IDGenerator.generate());
                            mstComponentInspectionItemDetail.setComponentInspectionItemsTableId(mstComponentInspectionItemId);
                            if (i < 10) {
                                mstComponentInspectionItemDetail.setInspectionItemSno("ITEM00" + i);
                            } else if (i >= 100) {
                                mstComponentInspectionItemDetail.setInspectionItemSno("ITEM" + i);
                            } else {
                                mstComponentInspectionItemDetail.setInspectionItemSno("ITEM0" + i);
                            }
                            // 追加列
                            mstComponentInspectionItemDetail.setRevisionSymbol(strRevisionSymbol);
                            mstComponentInspectionItemDetail.setDrawingPage(strDrawingPage);
                            mstComponentInspectionItemDetail.setDrawingAnnotation(strDrawingAnnotation);
                            mstComponentInspectionItemDetail.setDrawingMentionNo(strDrawingMentionNo);
                            mstComponentInspectionItemDetail.setSimilarMultiitem(strSimilarMultiitem);
                            mstComponentInspectionItemDetail.setDrawingArea(strDrawingArea);
                            mstComponentInspectionItemDetail.setPqs(strPqs);
                            mstComponentInspectionItemDetail.setInspectionItemName(strInspectionItemName);
                            mstComponentInspectionItemDetail.setDimensionValue(convertDoubleToBigDecimalForCSV(comList.get(8)));
                            mstComponentInspectionItemDetail.setTolerancePlus(convertDoubleToBigDecimalForCSV(comList.get(9)));
                            mstComponentInspectionItemDetail.setToleranceMinus(convertDoubleToBigDecimalForCSV(comList.get(10)));
                            mstComponentInspectionItemDetail.setMeasurementMethod(convertToStringForCSV(comList.get(11)));
                            mstComponentInspectionItemDetail.setOutgoingTrialInspectionObject(convertToCharForCSV(comList.get(12)));
                            mstComponentInspectionItemDetail.setOutgoingProductionInspectionObject(convertToCharForCSV(comList.get(13)));
                            mstComponentInspectionItemDetail.setIncomingTrialInspectionObject(convertToCharForCSV(comList.get(14)));
                            mstComponentInspectionItemDetail.setIncomingProductionInspectionObject(convertToCharForCSV(comList.get(15)));
                            mstComponentInspectionItemDetail.setMeasurementType(convertToIntegerForCSV(comList.get(16)));
                            mstComponentInspectionItemDetail.setCreateDate(sysDate);
                            mstComponentInspectionItemDetail.setCreateUserUuid(loginUser.getUserUuid());
                            mstComponentInspectionItemDetail.setUpdateDate(sysDate);
                            mstComponentInspectionItemDetail.setUpdateUserUuid(loginUser.getUserUuid());
                            String dimId = mstComponentInspectionItemService.toDimensionId(mstComponentInspectionItemDetail);
                            if(oldVerDetail.containsKey(dimId)) {
                                mstComponentInspectionItemDetail.setEnableThAlert(oldVerDetail.get(dimId).isEnableThAlert());
                            }

                            //Apeng 20180130 add start
                            if (comListSize == 17) {
                                mstComponentInspectionItemDetail.setProcessInspetionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                                mstComponentInspectionItemDetail.setAdditionalFlg(CommonConstants.ITEMS_TABLE_ADDITIONAL_FLG_YES.charAt(0));
                                mstComponentInspectionItemDetail.setOutgoingFirstMassProductionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                                mstComponentInspectionItemDetail.setIncomingFirstMassProductionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                            } else if (comListSize == 18) {
                                mstComponentInspectionItemDetail.setProcessInspetionObject(convertToCharForCSV(comList.get(17)));
                                mstComponentInspectionItemDetail.setAdditionalFlg(CommonConstants.ITEMS_TABLE_ADDITIONAL_FLG_YES.charAt(0));
                                mstComponentInspectionItemDetail.setOutgoingFirstMassProductionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                                mstComponentInspectionItemDetail.setIncomingFirstMassProductionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                            } else if (comListSize == 19) {
                                mstComponentInspectionItemDetail.setProcessInspetionObject(convertToCharForCSV(comList.get(17)));
                                mstComponentInspectionItemDetail.setAdditionalFlg(convertToCharForCSV(comList.get(18)));
                                mstComponentInspectionItemDetail.setOutgoingFirstMassProductionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                                mstComponentInspectionItemDetail.setIncomingFirstMassProductionObject(CommonConstants.INSPECTION_TARGET_NO.charAt(0));
                            } else {
                                mstComponentInspectionItemDetail.setProcessInspetionObject(convertToCharForCSV(comList.get(17)));
                                mstComponentInspectionItemDetail.setAdditionalFlg(convertToCharForCSV(comList.get(18)));
                                mstComponentInspectionItemDetail.setOutgoingFirstMassProductionObject(convertToCharForCSV(comList.get(19)));
                                mstComponentInspectionItemDetail.setIncomingFirstMassProductionObject(convertToCharForCSV(comList.get(20)));
                            }
                            if(comListSize > 21) {
                                mstComponentInspectionItemDetail.setEnableThAlert("1".equals(comList.get(21)));
                            }
                            //Apeng 20180130 add end

                            checkResult = componentInspectionCSVCheck(mstComponentInspectionItemDetail, userLangId, fu, logFile, i, logParm, true);
                            if (checkResult.isError()) {
                                errorFlag = true;
                                if (StringUtils.isEmpty(checkMsg)) {
                                    checkMsg = checkResult.getErrorMessage();
                                }
                                continue;
                            }
                            if (mstComponentInspectionItemDetail.getMeasurementType() != null
                                    && mstComponentInspectionItemDetail.getMeasurementType() == CommonConstants.MEASUREMENT_TYPE_VISUAL) {
                                visuallyQuantity++;
                            } else {
                                measurementQuantity++;
                            }
                            mstComponentInspectionItemDetailList.add(mstComponentInspectionItemDetail);
                        }
                        if (!errorFlag) {
                            addedCount = readList.size() - 1;
                            succeededCount = addedCount;

                            MstComponentInspectionItemsTable mstComponentInspectionItem = new MstComponentInspectionItemsTable();
                            mstComponentInspectionItem.setId(mstComponentInspectionItemId);
                            mstComponentInspectionItem.setComponentId(mstComponent.getId());
                            mstComponentInspectionItem.setOutgoingCompanyId(componentInspectionItemCSV.getOutgoingCompanyId());
                            if (StringUtils.isNotEmpty(componentInspectionItemCSV.getIncomingCompanyId())) {
                                mstComponentInspectionItem.setIncomingCompanyId(componentInspectionItemCSV.getIncomingCompanyId());
                            } else {
                                resp = new MstComponentInspectionItemsTableResp();
                                resp.setComponentCode(componentCode);
                                resp.setErrorReason(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "inspection_item_no_incoming_company"));
                                componentInspectionItemsTableRespError.add(resp);
                                response.setComponentInspectionItemsTableRespError(componentInspectionItemsTableRespError);
                                continue;
                            }
                            Integer version = mstComponentInspectionItemService.getMaxVersion(
                                    mstComponent.getId(), componentInspectionItemCSV.getOutgoingCompanyId(), componentInspectionItemCSV.getIncomingCompanyId());
                            mstComponentInspectionItem.setVersion(version == null ? 1 : version + 1);
                            mstComponentInspectionItem.setEntryPersonUuid(loginUser.getUserUuid());
                            mstComponentInspectionItem.setEntryPersonName(mstUserService.getMstUser(loginUser.getUserid()).getUserName());
                            // check whether the record is batch process target
                            if (!Objects.equals(mstComponentInspectionItem.getOutgoingCompanyId(), mstComponentInspectionItem.getIncomingCompanyId())) {
                                mstComponentInspectionItem.setInspBatchUpdateStatus(CommonConstants.INSP_BATCH_UPDATE_STATUS_ITEM_NOT_PUSH);
                            }
                            mstComponentInspectionItem.setCreateDate(sysDate);
                            mstComponentInspectionItem.setUpdateDate(sysDate);
                            mstComponentInspectionItem.setCreateUserUuid(loginUser.getUserUuid());
                            mstComponentInspectionItem.setUpdateUserUuid(loginUser.getUserUuid());
                            mstComponentInspectionItem.setItemApproveStatus(CommonConstants.ITEM_APPROVE_UNTREATED);
                            mstComponentInspectionItem.setInspectTypeId(mstComponentInspectType.getId());
                            mstComponentInspectionItem.setInspectTypeDictKey(mstComponentInspectType.getDictKey());
                            MstComponentInspectionItemsTable prev = mstComponentInspectionItemService.getOldVersionItem(mstComponentInspectionItem);
                            if(prev != null) {
                                mstComponentInspectionItem.setMeasSampleRatio(prev.getMeasSampleRatio());
                                mstComponentInspectionItem.setVisSampleRatio(prev.getVisSampleRatio());
                            } else {
                                BigDecimal measSampleRatio = new BigDecimal(cnfSystemService.findByKey("system", "component_insp_measure_sampling_quantity").getConfigValue());
                                mstComponentInspectionItem.setMeasSampleRatio(measSampleRatio);
                                BigDecimal visSampleRatio = new BigDecimal(cnfSystemService.findByKey("system", "component_insp_visual_sampling_quantity").getConfigValue());
                                mstComponentInspectionItem.setVisSampleRatio(visSampleRatio);
                            }
                            mstComponentInspectionItemService.createMstComponentInspectionItem(mstComponentInspectionItem);

                            // 非連携管理項目をコピュー 20180918 start
                            // 非連携項目コピュー用のバージョンを取得する
                            copyItemsDetail(mstComponentInspectionItemDetailList, mstComponentInspectionItemId, mstComponent.getId(), componentInspectionItemCSV.getOutgoingCompanyId(), componentInspectionItemCSV.getIncomingCompanyId(), addedCount, resultMap);
                            // 非連携管理項目をコピュー 20180918 end

                            for (int i = 0; i < mstComponentInspectionItemDetailList.size(); i++) {
                                MstComponentInspectionItemsTableDetail detail = mstComponentInspectionItemDetailList.get(i);
                                this.mstComponentInspectionItemService.setInspectionItemDetail(detail);
                            }
                            resp = new MstComponentInspectionItemsTableResp();
                            resp.setComponentCode(componentCode);
                            resp.setComponentName(mstComponent.getComponentName());
                            resp.setMeasurementQuantity(measurementQuantity);
                            resp.setVisuallyQuantity(visuallyQuantity);
                            componentInspectionItemsTableRespSuccess.add(resp);
                            response.setComponentInspectionItemsTableRespSuccess(componentInspectionItemsTableRespSuccess);
                        } else {
                            failedCount = readList.size() - 1;
                            resp = new MstComponentInspectionItemsTableResp();
                            resp.setComponentCode(componentCode);
                            resp.setErrorReason(checkMsg);
                            componentInspectionItemsTableRespError.add(resp);
                            response.setComponentInspectionItemsTableRespError(componentInspectionItemsTableRespError);
                        }
                        response.setTotalCount(readList.size() - 1);
                        response.setSucceededCount(succeededCount);
                        response.setAddedCount(addedCount);
                        response.setUpdatedCount(updatedCount);
                        response.setDeletedCount(deletedCount);
                        response.setFailedCount(failedCount);
                        response.setLog(logFileUuid);

                        //アップロードログをテーブルに書き出し
                        TblCsvImport tblCsvImport = new TblCsvImport();
                        tblCsvImport.setImportUuid(IDGenerator.generate());
                        tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
                        tblCsvImport.setImportDate(new Date());
                        tblCsvImport.setImportTable(CommonConstants.MST_COMPONENT_INSPECTION_ITEM_DETAIL);
                        tblUploadFile = new TblUploadFile();
                        tblUploadFile.setFileUuid(fileUuid);
                        tblCsvImport.setUploadFileUuid(tblUploadFile);
                        MstFunction mstFunction = new MstFunction();
                        mstFunction.setId(CommonConstants.FUN_ID_INSPECTION_ITEM);
                        tblCsvImport.setFunctionId(mstFunction);
                        tblCsvImport.setRecordCount(readList.size() - 1);
                        tblCsvImport.setSuceededCount(convertToIntegerForCSV(succeededCount));
                        tblCsvImport.setAddedCount(convertToIntegerForCSV(addedCount));
                        tblCsvImport.setUpdatedCount(convertToIntegerForCSV(updatedCount));
                        tblCsvImport.setDeletedCount(convertToIntegerForCSV(deletedCount));
                        tblCsvImport.setFailedCount(convertToIntegerForCSV(failedCount));
                        tblCsvImport.setLogFileUuid(logFileUuid);

                        tblCsvImport.setLogFileName(FileUtil.getLogFileName(headerMap.get("component_inspection_item")));

                        tblCsvImportService.createCsvImpor(tblCsvImport);

                    }
                } catch (Exception e) {
                    resp = new MstComponentInspectionItemsTableResp();
                    resp.setComponentCode(componentCode);
                    resp.setErrorReason(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
                    componentInspectionItemsTableRespError.add(resp);
                    response.setComponentInspectionItemsTableRespError(componentInspectionItemsTableRespError);
                }
            }
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null");
            response.setErrorMessage(msg);
            return response;
        }

        return response;
    }

    /**
     * Get search result count
     *
     * @param componentInspectionItemCSV
     * @return
     */
    @POST
    @Path("/check")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse importCSVCheck(ComponentInspectionSearchCondForCSV componentInspectionItemCSV) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if(!StringUtils.isNotEmpty(componentInspectionItemCSV.getIncomingCompanyId())) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "inspection_item_no_incoming_company"));
            return response;
        }
        List<MstComponentInspectionItemsTable> mstComponentInspectionItemsTableList
                = this.mstComponentInspectionItemService.getNotApprove(
                        componentInspectionItemCSV.getComponentId(), componentInspectionItemCSV.getOutgoingCompanyId(), componentInspectionItemCSV.getIncomingCompanyId());
        if (mstComponentInspectionItemsTableList != null && mstComponentInspectionItemsTableList.size() >= 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "csv_import_for_not_approve_error_msg"));
        }
        return response;
    }

    /**
     * Push external inspection items table
     *
     * @param dataList
     * @return
     */
    @POST
    @Path("/extdata/push")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionItemsTableRespVo pushExternalInspectionItemsTable(List<ComponentInspectionItemsTableForBatch> dataList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstApiUser mstApiUser = this.mstApiUserService.getApiMstUser(loginUser.getUserid());
        String incomingCompanyId = mstApiUser.getCompanyId();

        return this.mstComponentInspectionItemService.updateInspetionItemsByBatch(dataList, incomingCompanyId);
    }

    /*  private methods
    |========================================================================*/
    /**
     * Convert to ComponentInspectionItemSearchCond
     *
     * @param component
     * @param componentCode
     * @param componentName
     * @param componentInspectType
     * @param outgoingCompanyName
     * @param incomingCompanyName
     * @param itemApproveStatus
     * @param withHistory
     * @return
     */
    private ComponentInspectionItemsTableSearchCond convertToSearchCondition(String component, String componentCode, String componentName, String componentInspectType,
            String outgoingCompanyName, String incomingCompanyName, String itemApproveStatus, String withHistory) {
        ComponentInspectionItemsTableSearchCond searchCondition = new ComponentInspectionItemsTableSearchCond();
        searchCondition.setComponent(component);
        searchCondition.setComponentCode(componentCode);
        searchCondition.setComponentName(componentName);
        searchCondition.setComponentInspectType(componentInspectType);
        searchCondition.setOutgoingCompanyName(outgoingCompanyName);
        searchCondition.setIncomingCompanyName(incomingCompanyName);
        searchCondition.setItemApproveStatus(itemApproveStatus);
        searchCondition.setWithHistory(withHistory);
        
        MstCompany selfCompany = this.mstCompanyService.getSelfCompany();
        if (selfCompany != null) {
            searchCondition.setMyCompanyId(selfCompany.getId());
        }
        return searchCondition;
    }

    /**
     * Convert to String For CSV
     *
     * @param column
     * @return
     */
    private String convertToStringForCSV(Object column) {
        if (column == null) {
            return null;
        }
        try {
            return String.valueOf(column);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Convert to Long For CSV
     *
     * @param column
     * @return
     */
    private Long convertToLongForCSV(Object column) {
        if (column == null) {
            return null;
        }
        try {
            return Long.valueOf(convertToStringForCSV(column));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Convert to Double For CSV
     *
     * @param column
     * @return
     */
    private Double convertToDoubleForCSV(Object column) {
        if (column == null) {
            return null;
        }
        try {
            return Double.parseDouble(convertToStringForCSV(column));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Convert to Integer For CSV
     *
     * @param column
     * @return
     */
    private Integer convertToIntegerForCSV(Object column) {
        if (column == null) {
            return null;
        }
        try {
            return Integer.parseInt(convertToStringForCSV(column));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Convert to BigDecimal For CSV
     *
     * @param column
     * @return
     */
    private BigDecimal convertLongToBigDecimalForCSV(Object column) {
        if (column == null) {
            return null;
        }
        try {
            return BigDecimal.valueOf(convertToLongForCSV(column));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Convert to BigDecimal For CSV
     *
     * @param column
     * @return
     */
    private BigDecimal convertDoubleToBigDecimalForCSV(Object column) {
        if (column == null) {
            return null;
        }
        try {
            return BigDecimal.valueOf(convertToDoubleForCSV(column));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Convert to Character For CSV
     *
     * @param column
     * @return
     */
    private Character convertToCharForCSV(Object column) {
        if (column == null) {
            return null;
        }
        try {
            return convertToStringForCSV(column).charAt(0);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 最大桁数チェック
     *
     * @param val
     * @return
     */
    private boolean checkMaxLength(String val) {
        if (StringUtils.isEmpty(val)) {
            return true;
        } else if (val.length() > MAX_VALUE_LENGTH) {
            return false;
        }
        return true;
    }
    
    /**
     * 文字列長をチェック。
     * @param val チェック対象文字列
     * @param length 許容する最大長さ
     * @return valの長さがlength以下だった場合true。
     */
    private boolean checkMaxLength(String val, int length) {
        return StringUtils.isEmpty(val) || val.length() <= length;
    }

    /**
     * Component Inspection CSVC heck
     *
     * @param mstComponentInspectionItemDetail
     * @param userLangId
     * @param fu
     * @param logParm
     * @return
     */
    private BasicResponse componentInspectionCSVCheck(MstComponentInspectionItemsTableDetail mstComponentInspectionItemDetail,
            String userLangId, FileUtil fu, String logFile, int index, Map<String, String> logParm, boolean complexFlag) {
        BasicResponse response = new BasicResponse();

        int tmpType = mstComponentInspectionItemDetail.getMeasurementType() == null ? 9 : mstComponentInspectionItemDetail.getMeasurementType();
        String errorMsg = "";
        // 非空白且つ長さチェック
        //-----------------------------Apeng 20180111 update start-------------------------------
        if (StringUtils.isNotEmpty(mstComponentInspectionItemDetail.getRevisionSymbol()) && !checkMaxLength(mstComponentInspectionItemDetail.getRevisionSymbol())) {
            errorMsg = fu.outValue(
                    logParm.get("lineNo"), index, logParm.get("revisionSymbol"), "",
                    logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "revision_symbol_error_msg"));
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }

        if (StringUtils.isNotEmpty(mstComponentInspectionItemDetail.getDrawingPage()) && !checkMaxLength(mstComponentInspectionItemDetail.getDrawingPage())) {
            if(!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("drawingPage"), "",
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "drawing_page_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        if (StringUtils.isNotEmpty(mstComponentInspectionItemDetail.getDrawingAnnotation()) && !checkMaxLength(mstComponentInspectionItemDetail.getDrawingAnnotation())) {
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("drawingAnnotation"), "",
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "drawing_annotation_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        if (StringUtils.isNotEmpty(mstComponentInspectionItemDetail.getDrawingMentionNo()) && !checkMaxLength(mstComponentInspectionItemDetail.getDrawingMentionNo())) {
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("drawingMentionNo"), "",
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "drawing_mention_no_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        if (StringUtils.isNotEmpty(mstComponentInspectionItemDetail.getSimilarMultiitem()) && !checkMaxLength(mstComponentInspectionItemDetail.getSimilarMultiitem())) {
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("similarMultiitem"), "",
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "similar_multiitem_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        if (StringUtils.isNotEmpty(mstComponentInspectionItemDetail.getDrawingArea()) && !checkMaxLength(mstComponentInspectionItemDetail.getDrawingArea())) {
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("drawingArea"), "",
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "drawing_area_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        if (StringUtils.isNotEmpty(mstComponentInspectionItemDetail.getPqs()) && !checkMaxLength(mstComponentInspectionItemDetail.getPqs())) {
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("pqs"), "",
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "pqs_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        if (StringUtils.isNotEmpty(mstComponentInspectionItemDetail.getInspectionItemName()) && !checkMaxLength(mstComponentInspectionItemDetail.getInspectionItemName(), 2000)) {
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("inspectionItemNames"), "",
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "inspection_item_names_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        //-----------------------------Apeng 20180111 update end-------------------------------
        if (CommonConstants.MEASUREMENT_TYPE_MEASURE == tmpType
                && mstComponentInspectionItemDetail.getDimensionValue() == null) {
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("dimensionValue"), "",
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "dimension_value_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        if (CommonConstants.MEASUREMENT_TYPE_MEASURE == tmpType
                && mstComponentInspectionItemDetail.getTolerancePlus() == null) {
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("tolerancePlus"), "",
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "tolerance_plus_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        if (CommonConstants.MEASUREMENT_TYPE_MEASURE == tmpType
                && mstComponentInspectionItemDetail.getToleranceMinus() == null) {
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("toleranceMinus"), "",
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "tolerance_minus_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        if (StringUtils.isBlank(mstComponentInspectionItemDetail.getMeasurementMethod()) || !checkMaxLength(mstComponentInspectionItemDetail.getMeasurementMethod())) {
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("measurementMethod"), "",
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "measurement_method_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        
        if (null == mstComponentInspectionItemDetail.getOutgoingTrialInspectionObject()
                || (CommonConstants.INSPECTION_TARGET_YES.charAt(0) != mstComponentInspectionItemDetail.getOutgoingTrialInspectionObject()
                && CommonConstants.INSPECTION_TARGET_NO.charAt(0) != mstComponentInspectionItemDetail.getOutgoingTrialInspectionObject())) {
            String outgoingTrial = convertToStringForCSV(mstComponentInspectionItemDetail.getOutgoingTrialInspectionObject());
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("outgoingTrial"), outgoingTrial,
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "outgoing_trial_inspection_object_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        if (null == mstComponentInspectionItemDetail.getOutgoingProductionInspectionObject()
                || (CommonConstants.INSPECTION_TARGET_YES.charAt(0) != mstComponentInspectionItemDetail.getOutgoingProductionInspectionObject()
                && CommonConstants.INSPECTION_TARGET_NO.charAt(0) != mstComponentInspectionItemDetail.getOutgoingProductionInspectionObject())) {
            String outgoingProduction = convertToStringForCSV(mstComponentInspectionItemDetail.getOutgoingProductionInspectionObject());
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("outgoingProduction"), outgoingProduction,
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "outgoing_production_inspection_object_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        if (null == mstComponentInspectionItemDetail.getIncomingTrialInspectionObject()
                || (CommonConstants.INSPECTION_TARGET_YES.charAt(0) != mstComponentInspectionItemDetail.getIncomingTrialInspectionObject()
                && CommonConstants.INSPECTION_TARGET_NO.charAt(0) != mstComponentInspectionItemDetail.getIncomingTrialInspectionObject())) {
            String incomingTrial = convertToStringForCSV(mstComponentInspectionItemDetail.getIncomingTrialInspectionObject());
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("incomingTrial"), incomingTrial,
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "incoming_trial_inspection_object_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        if (null == mstComponentInspectionItemDetail.getIncomingProductionInspectionObject()
                || (CommonConstants.INSPECTION_TARGET_YES.charAt(0) != mstComponentInspectionItemDetail.getIncomingProductionInspectionObject()
                && CommonConstants.INSPECTION_TARGET_NO.charAt(0) != mstComponentInspectionItemDetail.getIncomingProductionInspectionObject())) {
            String incomingProduction = convertToStringForCSV(mstComponentInspectionItemDetail.getIncomingProductionInspectionObject());
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("incomingProduction"), incomingProduction,
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "incoming_production_inspection_object_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        if (mstComponentInspectionItemDetail.getMeasurementType() == null
                || (CommonConstants.MEASUREMENT_TYPE_MEASURE != mstComponentInspectionItemDetail.getMeasurementType()
                && CommonConstants.MEASUREMENT_TYPE_VISUAL != mstComponentInspectionItemDetail.getMeasurementType())) {
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("measurementType"), "",
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "measurement_type_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        
        //Apeng 20180130 add start
        if (null == mstComponentInspectionItemDetail.getProcessInspetionObject()
                || (CommonConstants.INSPECTION_TARGET_YES.charAt(0) != mstComponentInspectionItemDetail.getProcessInspetionObject()
                && CommonConstants.INSPECTION_TARGET_NO.charAt(0) != mstComponentInspectionItemDetail.getProcessInspetionObject())) {
            String processInspetionObject = convertToStringForCSV(mstComponentInspectionItemDetail.getProcessInspetionObject());
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("processInspetionObject"), processInspetionObject,
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "procedure_internal_inspection_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        //Apeng 20180130 add end
        
        // 量産第一ロット対象をチェックする
        if (null == mstComponentInspectionItemDetail.getOutgoingFirstMassProductionObject()
                || (CommonConstants.INSPECTION_TARGET_YES.charAt(0) != mstComponentInspectionItemDetail.getOutgoingFirstMassProductionObject()
                && CommonConstants.INSPECTION_TARGET_NO.charAt(0) != mstComponentInspectionItemDetail.getOutgoingFirstMassProductionObject())) {
            String outgoingFirstMass = convertToStringForCSV(mstComponentInspectionItemDetail.getOutgoingFirstMassProductionObject());
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("outgoingFirstMass"), outgoingFirstMass,
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "outgoing_first_mass_object_error_msg"));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        
        if (null == mstComponentInspectionItemDetail.getIncomingFirstMassProductionObject()
                || (CommonConstants.INSPECTION_TARGET_YES.charAt(0) != mstComponentInspectionItemDetail.getIncomingFirstMassProductionObject()
                && CommonConstants.INSPECTION_TARGET_NO.charAt(0) != mstComponentInspectionItemDetail.getIncomingFirstMassProductionObject())) {
            String incomingFirstMass = convertToStringForCSV(mstComponentInspectionItemDetail.getIncomingFirstMassProductionObject());
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("incomingFirstMass"), incomingFirstMass,
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "incoming_first_mass_object_error_msg "));
            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        }
        
        // CSV取込の場合、非連携フラグを追加 start
        if (null == mstComponentInspectionItemDetail.getAdditionalFlg() || (CommonConstants.INSPECTION_TARGET_YES.charAt(0) != mstComponentInspectionItemDetail.getAdditionalFlg()
                && CommonConstants.INSPECTION_TARGET_NO.charAt(0) != mstComponentInspectionItemDetail.getAdditionalFlg())) {
            String additionalFlg = convertToStringForCSV(mstComponentInspectionItemDetail.getAdditionalFlg());
            if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                errorMsg = fu.outValue(
                        logParm.get("lineNo"), index, logParm.get("additionalFlg"), additionalFlg,
                        logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "additional_flg_error_msg"));

            }
            fu.writeInfoToFile(logFile, errorMsg);
            response.setError(true);
            response.setErrorMessage(errorMsg);
        } else if (null != mstComponentInspectionItemDetail.getAdditionalFlg() && CommonConstants.INSPECTION_TARGET_YES.charAt(0) == mstComponentInspectionItemDetail.getAdditionalFlg()) { 

            // 非連携検査項目を工程内検査以外で対象とすることはできません
            if (null != mstComponentInspectionItemDetail.getOutgoingTrialInspectionObject()
                    && null != mstComponentInspectionItemDetail.getOutgoingProductionInspectionObject()
                    && null != mstComponentInspectionItemDetail.getIncomingTrialInspectionObject()
                    && null != mstComponentInspectionItemDetail.getIncomingProductionInspectionObject()
                    && null != mstComponentInspectionItemDetail.getOutgoingFirstMassProductionObject()
                    && null != mstComponentInspectionItemDetail.getIncomingFirstMassProductionObject()) {

                if (CommonConstants.INSPECTION_TARGET_YES.charAt(0) == mstComponentInspectionItemDetail.getOutgoingTrialInspectionObject()
                        || CommonConstants.INSPECTION_TARGET_YES.charAt(0) == mstComponentInspectionItemDetail.getOutgoingProductionInspectionObject()
                        || CommonConstants.INSPECTION_TARGET_YES.charAt(0) == mstComponentInspectionItemDetail.getIncomingTrialInspectionObject()
                        || CommonConstants.INSPECTION_TARGET_YES.charAt(0) == mstComponentInspectionItemDetail.getIncomingProductionInspectionObject()
                        || CommonConstants.INSPECTION_TARGET_YES.charAt(0) == mstComponentInspectionItemDetail.getOutgoingFirstMassProductionObject()
                        || CommonConstants.INSPECTION_TARGET_YES.charAt(0) == mstComponentInspectionItemDetail.getIncomingFirstMassProductionObject()) {

                    String additionalFlg = convertToStringForCSV(mstComponentInspectionItemDetail.getAdditionalFlg());
                    if (!complexFlag || StringUtils.isEmpty(errorMsg)) {
                        errorMsg = fu.outValue(
                                logParm.get("lineNo"), index, logParm.get("additionalFlg"), additionalFlg,
                                logParm.get("error"), 1, logParm.get("errorContents"), mstDictionaryService.getDictionaryValue(userLangId, "additional_item_error_msg"));
                    }
                    fu.writeInfoToFile(logFile, errorMsg);
                    response.setError(true);
                    response.setErrorMessage(errorMsg);
                }
            }
        }
        // CSV取込の場合、非連携フラグを追加 end
        
        return response;
    }

    private Map<String, String> getDictionaryList(String langId) {
        // ヘッダー種取得
        List<String> dictKeyList = Arrays.asList("row_number", "error", "error_detail", "revision_symbol", "drawing_page", "drawing_annotation", "drawing_mention_no",
                "similar_multiitem", "drawing_area", "pqs", "inspection_item_names", "dimension_value", "tolerance_plus", "tolerance_minus", "measurement_method",
                "outgoing_trial_inspection_object", "outgoing_production_inspection_object", "incoming_trial_inspection_object", "incoming_production_inspection_object",
                "measurement_type", "component_inspection_item", "dimension_value_repeat_error", "procedure_internal_inspection", "additional_flg");
        Map<String, String> headerMap = FileUtil.getDictionaryList(mstDictionaryService, langId, dictKeyList);
        return headerMap;
    }
    
    /**
     * 使用されている非連携管理項目の判断
     * 
     * @param componentInspectionItemsTableId
     * @param inspectionItemSno
     * @return 
     */
    @GET
    @Path("used")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse getTblComponentInspectionResultExist(
            @QueryParam("componentInspectionItemsTableId") String componentInspectionItemsTableId,
            @QueryParam("inspectionItemSno") String inspectionItemSno
    ){
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentInspectionItemService.getTblComponentInspectionResultExist(componentInspectionItemsTableId,inspectionItemSno,loginUser.getLangId());
    }
    
     /**
     * 非連携管理項目のコピュー
     * 
     * @param mstComponentInspectionItemDetailList
     * @param itemTableId
     * @param componentId
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param count
     * @param resultMap
     */
    public void copyItemsDetail(List<MstComponentInspectionItemsTableDetail> mstComponentInspectionItemDetailList, String itemTableId, String componentId, String outgoingCompanyId, String incomingCompanyId, long count, Map<String, String> resultMap) {

        Integer copyVersion = mstComponentInspectionItemService.getCopyVersion(componentId, outgoingCompanyId, incomingCompanyId);

        if (copyVersion > 0) {
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
                    .setParameter("outgoingCompanyId", outgoingCompanyId)
                    .setParameter("incomingCompanyId", incomingCompanyId)
                    .setParameter("version", copyVersion)
                    .setParameter("additionalFlg", CommonConstants.ITEMS_TABLE_ADDITIONAL_FLG_NO.charAt(0)).getResultList();

            if (oldItemsDetailList.size() > 0) {
                long itemCount = count;
                Date nowDate = new Date();
                for (MstComponentInspectionItemsTableDetail detail : oldItemsDetailList) {

                    // 寸法IDの重複チェックを追加 20180928 start
                    String itemtableDetailMethodId;
                    itemtableDetailMethodId = (detail.getDrawingPage() == null ? "NULL" : detail.getDrawingPage())
                            + (detail.getDrawingAnnotation() == null ? "NULL" : detail.getDrawingAnnotation())
                            + (detail.getDrawingMentionNo() == null ? "NULL" : detail.getDrawingMentionNo())
                            + (detail.getSimilarMultiitem() == null ? "NULL" : detail.getSimilarMultiitem());
                    String containsKey = itemtableDetailMethodId.concat("|");
                    if (resultMap.containsKey(containsKey)) {
                        continue;
                    }
                    // 寸法IDの重複チェックを追加 20180928 end

                    StringBuilder itemSno = new StringBuilder();

                    itemSno.append("ITEM");
                    itemSno.append(FileUtil.addLeftZeroForNum(String.valueOf(itemCount + 1), 3));
                    MstComponentInspectionItemsTableDetail itemDetail = new MstComponentInspectionItemsTableDetail();

                    itemDetail.setInspectionItemSno(itemSno.toString());
                    itemDetail.setLocalSeq(0);
                    itemDetail.setComponentInspectionItemsTableId(itemTableId);
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
                    itemDetail.setOutgoingFirstMassProductionObject(detail.getOutgoingFirstMassProductionObject());
                    itemDetail.setIncomingFirstMassProductionObject(detail.getIncomingFirstMassProductionObject());

                    mstComponentInspectionItemDetailList.add(itemDetail);
                    itemCount++;
                }
            }
        }
    }
    
    /**
     * Get master component inspection item list
     *
     * @param componentInspectionItemsTableInfoList
     * @return
     */
    @POST
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getInspectionResultZip(List<ComponentInspectionItemsTableInfo> componentInspectionItemsTableInfoList) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        return mstComponentInspectionItemService.getInspectionItemDownloadZip(componentInspectionItemsTableInfoList, loginUser.getLangId());
    }
    
    // KM-978　非連携項目のCSV取込 20181122 start
    /**
     * 非連携項目のCSV取込
     *
     * @param componentInspectionItemCSV
     * @return
     */
    @POST
    @Path("/importadditioncsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public ComponentInspectionItemImportCSVResp importNoAdditionCsv(ComponentInspectionSearchCondForCSV componentInspectionItemCSV) {
        ComponentInspectionItemImportCSVResp response = new ComponentInspectionItemImportCSVResp();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        boolean errorFlag = false;
        int succeededCount = 0;
        int addedCount = 0;
        int updatedCount = 0;
        int deletedCount = 0;
        int failedCount = 0;
        int totalCount = 0;
        String logFileUuid = IDGenerator.generate();
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, componentInspectionItemCSV.getFileUuid());
        if (!csvFile.endsWith(CommonConstants.CSV)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
            response.setErrorMessage(msg);
            return response;
        }
        MstComponentInspectionItemsTable mstComponentInspectionItem;
        try {
            mstComponentInspectionItem = this.entityManager
                    .createNamedQuery("MstComponentInspectionItemsTable.findById", MstComponentInspectionItemsTable.class)
                    .setParameter("id", componentInspectionItemCSV.getComponentInspectionItemsTableId()).getSingleResult();
        } catch (NoResultException e) {
            mstComponentInspectionItem = null;
        }
        
        if (null == mstComponentInspectionItem) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found");
            response.setErrorMessage(msg);
            return response;
        }
                
        CSVFileUtil csvFileUtil = null;
        List readList = new ArrayList();
        try {
            csvFileUtil = new CSVFileUtil(csvFile);
            boolean readEnd = false;
            do {
                String readLine = csvFileUtil.readLine();
                if (StringUtils.isEmpty(readLine)) {
                    readEnd = true;
                } else {
                    readList.add(CSVFileUtil.fromCSVLinetoArray(readLine));
                }
            } while (!readEnd);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }
        if (readList.size() <= 1) {
            return response;
        } else {
            Date sysDate = new Date();
            BasicResponse checkResult;

            String userLangId = loginUser.getLangId();
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
            String lineNo = mstDictionaryService.getDictionaryValue(userLangId, "row_number");
            String error = mstDictionaryService.getDictionaryValue(userLangId, "error");
            String errorContents = mstDictionaryService.getDictionaryValue(userLangId, "error_detail");
            // 追加列
            String revisionSymbol = mstDictionaryService.getDictionaryValue(userLangId, "revision_symbol");
            String drawingPage = mstDictionaryService.getDictionaryValue(userLangId, "drawing_page");
            String drawingAnnotation = mstDictionaryService.getDictionaryValue(userLangId, "drawing_annotation");
            String drawingMentionNo = mstDictionaryService.getDictionaryValue(userLangId, "drawing_mention_no");
            String similarMultiitem = mstDictionaryService.getDictionaryValue(userLangId, "similar_multiitem");
            String drawingArea = mstDictionaryService.getDictionaryValue(userLangId, "drawing_area");
            String pqs = mstDictionaryService.getDictionaryValue(userLangId, "pqs");

            String inspectionItemNames = mstDictionaryService.getDictionaryValue(userLangId, "inspection_item_names");
            String dimensionValue = mstDictionaryService.getDictionaryValue(userLangId, "dimension_value");
            String tolerancePlus = mstDictionaryService.getDictionaryValue(userLangId, "tolerance_plus");
            String toleranceMinus = mstDictionaryService.getDictionaryValue(userLangId, "tolerance_minus");
            String measurementMethod = mstDictionaryService.getDictionaryValue(userLangId, "measurement_method");
            String outgoingTrial = mstDictionaryService.getDictionaryValue(userLangId, "outgoing_trial_inspection_object");
            String outgoingProduction = mstDictionaryService.getDictionaryValue(userLangId, "outgoing_production_inspection_object");
            String incomingTrial = mstDictionaryService.getDictionaryValue(userLangId, "incoming_trial_inspection_object");
            String incomingProduction = mstDictionaryService.getDictionaryValue(userLangId, "incoming_production_inspection_object");
            String measurementType = mstDictionaryService.getDictionaryValue(userLangId, "measurement_type");
            String dimensionValueErrorMsg = mstDictionaryService.getDictionaryValue(userLangId, "dimension_value_repeat_error");
            String fileName = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), CommonConstants.MST_COMPONENT_INSPECTION_ITEM_DETAIL);
            Map<String, String> logParm = new HashMap<>();
            logParm.put("lineNo", lineNo);
            logParm.put("revisionSymbol", revisionSymbol);
            logParm.put("drawingPage", drawingPage);
            logParm.put("drawingAnnotation", drawingAnnotation);
            logParm.put("drawingMentionNo", drawingMentionNo);
            logParm.put("similarMultiitem", similarMultiitem);
            logParm.put("drawingArea", drawingArea);
            logParm.put("pqs", pqs);
            logParm.put("inspectionItemNames", inspectionItemNames);
            logParm.put("dimensionValue", dimensionValue);
            logParm.put("tolerancePlus", tolerancePlus);
            logParm.put("toleranceMinus", toleranceMinus);
            logParm.put("measurementMethod", measurementMethod);
            logParm.put("outgoingTrial", outgoingTrial);
            logParm.put("outgoingProduction", outgoingProduction);
            logParm.put("incomingTrial", incomingTrial);
            logParm.put("incomingProduction", incomingProduction);
            logParm.put("measurementType", measurementType);
            logParm.put("error", error);
            logParm.put("errorContents", errorContents);
            
            String processInspetionObject = mstDictionaryService.getDictionaryValue(userLangId, "procedure_internal_inspection");//工程内検査
            logParm.put("processInspetionObject", processInspetionObject);
            String additionalFlg = mstDictionaryService.getDictionaryValue(userLangId, "additional_flg");//非連携フラグ
            logParm.put("additionalFlg", additionalFlg);

            List<MstComponentInspectionItemsTableDetail> mstComponentInspectionItemDetailList = new ArrayList<>();
            FileUtil fu = new FileUtil();
            Map<String, String> resultMap = new HashMap<>();
            String containsKey;
            for (int i = 1; i < readList.size(); i++) {
                ArrayList comList = (ArrayList) readList.get(i);
                int comListSize = comList.size();
                if (comListSize != 22) {
                    //エラー情報をログファイルに記入
                    fu.writeInfoToFile(logFile, fu.outValue(
                            lineNo, i, "", "", error, 1, errorContents, mstDictionaryService.getDictionaryValue(userLangId, "csv_column_error_msg")));
                    errorFlag = true;
                    continue;
                }
                
                // 非連携項目のみ
                if ( null == convertToCharForCSV(comList.get(18)) || CommonConstants.ITEMS_TABLE_ADDITIONAL_FLG_NO.charAt(0) != convertToCharForCSV(comList.get(18))) {
                    continue;
                }
                totalCount++;
                
                String strRevisionSymbol = convertToStringForCSV(comList.get(0));
                String strDrawingPage = convertToStringForCSV(comList.get(1));
                String strDrawingAnnotation = convertToStringForCSV(comList.get(2));
                String strDrawingMentionNo = convertToStringForCSV(comList.get(3));
                String strSimilarMultiitem = convertToStringForCSV(comList.get(4));
                String strDrawingArea = convertToStringForCSV(comList.get(5));
                String strPqs = convertToStringForCSV(comList.get(6));
                String strInspectionItemName = convertToStringForCSV(comList.get(7));

                // 寸法ID重複チェック add
                String itemtableDetailMethodId = (strDrawingPage == null ? "NULL" : strDrawingPage)
                        + (strDrawingAnnotation == null ? "NULL" : strDrawingAnnotation)
                        + (strDrawingMentionNo == null ? "NULL" : strDrawingMentionNo)
                        + (strSimilarMultiitem == null ? "NULL" : strSimilarMultiitem);
                containsKey = itemtableDetailMethodId.concat("|");
                if (resultMap.containsKey(containsKey)) {
                    //エラー情報をログファイルに記入
                    fu.writeInfoToFile(logFile, fu.outValue(
                            lineNo, i, "", "", error, 1, errorContents, dimensionValueErrorMsg));
                    errorFlag = true;
                    continue;
                }
                resultMap.put(containsKey, containsKey);

                MstComponentInspectionItemsTableDetail mstComponentInspectionItemDetail = new MstComponentInspectionItemsTableDetail();
                mstComponentInspectionItemDetail.setId(IDGenerator.generate());
                
                mstComponentInspectionItemDetail.setRevisionSymbol(strRevisionSymbol);
                mstComponentInspectionItemDetail.setDrawingPage(strDrawingPage);
                mstComponentInspectionItemDetail.setDrawingAnnotation(strDrawingAnnotation);
                mstComponentInspectionItemDetail.setDrawingMentionNo(strDrawingMentionNo);
                mstComponentInspectionItemDetail.setSimilarMultiitem(strSimilarMultiitem);
                mstComponentInspectionItemDetail.setDrawingArea(strDrawingArea);
                mstComponentInspectionItemDetail.setPqs(strPqs);
                mstComponentInspectionItemDetail.setInspectionItemName(strInspectionItemName);
                mstComponentInspectionItemDetail.setDimensionValue(convertDoubleToBigDecimalForCSV(comList.get(8)));
                mstComponentInspectionItemDetail.setTolerancePlus(convertDoubleToBigDecimalForCSV(comList.get(9)));
                mstComponentInspectionItemDetail.setToleranceMinus(convertDoubleToBigDecimalForCSV(comList.get(10)));
                mstComponentInspectionItemDetail.setMeasurementMethod(convertToStringForCSV(comList.get(11)));
                mstComponentInspectionItemDetail.setOutgoingTrialInspectionObject(convertToCharForCSV(comList.get(12)));
                mstComponentInspectionItemDetail.setOutgoingProductionInspectionObject(convertToCharForCSV(comList.get(13)));
                mstComponentInspectionItemDetail.setIncomingTrialInspectionObject(convertToCharForCSV(comList.get(14)));
                mstComponentInspectionItemDetail.setIncomingProductionInspectionObject(convertToCharForCSV(comList.get(15)));
                mstComponentInspectionItemDetail.setMeasurementType(convertToIntegerForCSV(comList.get(16)));
                mstComponentInspectionItemDetail.setCreateDate(sysDate);
                mstComponentInspectionItemDetail.setCreateUserUuid(loginUser.getUserUuid());
                mstComponentInspectionItemDetail.setUpdateDate(sysDate);
                mstComponentInspectionItemDetail.setUpdateUserUuid(loginUser.getUserUuid());
                mstComponentInspectionItemDetail.setProcessInspetionObject(convertToCharForCSV(comList.get(17)));
                mstComponentInspectionItemDetail.setAdditionalFlg(convertToCharForCSV(comList.get(18)));
                mstComponentInspectionItemDetail.setOutgoingFirstMassProductionObject(convertToCharForCSV(comList.get(19)));
                mstComponentInspectionItemDetail.setIncomingFirstMassProductionObject(convertToCharForCSV(comList.get(20)));
                mstComponentInspectionItemDetail.setEnableThAlert("1".equals(comList.get(21)));
                
                checkResult = componentInspectionCSVCheck(mstComponentInspectionItemDetail, userLangId, fu, logFile, i, logParm, false);
                if (checkResult.isError()) {
                    errorFlag = true;
                    continue;
                }
                mstComponentInspectionItemDetailList.add(mstComponentInspectionItemDetail);
            }
            if (!errorFlag) {
                List<MstComponentInspectionItemsTableDetail> oldItemsDetailList = this.entityManager
                        .createNamedQuery("MstComponentInspectionItemsTableDetail.findByComponentInspectionItemsTableId", MstComponentInspectionItemsTableDetail.class)
                        .setParameter("componentInspectionItemsTableId", mstComponentInspectionItem.getId()).getResultList();
                
                //測定タイプに対して、最大のlocalSeqを取得する
                int maxMeasureLocalSeq = getMaxLocalSeq(mstComponentInspectionItem.getId(), CommonConstants.MEASUREMENT_TYPE_MEASURE);
                int maxVisualLocalSeq = getMaxLocalSeq(mstComponentInspectionItem.getId(), CommonConstants.MEASUREMENT_TYPE_VISUAL);

                // 非連携項目の寸法ID
                Map<String, MstComponentInspectionItemsTableDetail> additionResultMap = new HashMap<>();
                // 連携項目の寸法ID
                Map<String, MstComponentInspectionItemsTableDetail> oldResultMap = new HashMap<>();
                String oldItemtableDetailMethodId;
                String oldContainsKey;
                String itemSeq;
                int itemCount = 0;
                // 最大のitemSeqを取得する
                try {
                    itemSeq = this.entityManager
                            .createQuery("SELECT Max(mDetail.inspectionItemSno) FROM MstComponentInspectionItemsTableDetail mDetail "
                                    + "WHERE mDetail.componentInspectionItemsTableId = :id ", String.class)
                            .setParameter("id", mstComponentInspectionItem.getId()).getSingleResult();
                } catch (NoResultException e) {
                    itemSeq = null;
                }
                if (null != itemSeq) {
                    itemCount = Integer.valueOf(itemSeq.replaceFirst("ITEM0*", ""));
                }

                for (MstComponentInspectionItemsTableDetail detail : oldItemsDetailList) {

                    // 存在のレコードにの寸法IDの重複チェック
                    oldItemtableDetailMethodId = (detail.getDrawingPage() == null ? "NULL" : detail.getDrawingPage())
                            + (detail.getDrawingAnnotation() == null ? "NULL" : detail.getDrawingAnnotation())
                            + (detail.getDrawingMentionNo() == null ? "NULL" : detail.getDrawingMentionNo())
                            + (detail.getSimilarMultiitem() == null ? "NULL" : detail.getSimilarMultiitem());

                    oldContainsKey = oldItemtableDetailMethodId.concat("|");

                    if (CommonConstants.ITEMS_TABLE_ADDITIONAL_FLG_NO.charAt(0) != detail.getAdditionalFlg()) {
                        // 連携の項目
                        oldResultMap.put(oldContainsKey, detail);
                    } else {
                        // 非連携の項目
                        additionResultMap.put(oldContainsKey, detail);
                    }
                }

                for (int i = 0; i < mstComponentInspectionItemDetailList.size(); i++) {
                    MstComponentInspectionItemsTableDetail csvDetail = mstComponentInspectionItemDetailList.get(i);
                    
                    String methodId;
                    methodId = (csvDetail.getDrawingPage() == null ? "NULL" : csvDetail.getDrawingPage())
                            + (csvDetail.getDrawingAnnotation() == null ? "NULL" : csvDetail.getDrawingAnnotation())
                            + (csvDetail.getDrawingMentionNo() == null ? "NULL" : csvDetail.getDrawingMentionNo())
                            + (csvDetail.getSimilarMultiitem() == null ? "NULL" : csvDetail.getSimilarMultiitem());
                    String key = methodId.concat("|");
                    
                    if (oldResultMap.containsKey(key)) {// 寸法IDが元の連携項目と重複するため、CSVは取り込めない
                        continue;
                    }

                    if (additionResultMap.containsKey(key)) { // 寸法IDが元の非連携項目と重複するため、項目を更新する

                        MstComponentInspectionItemsTableDetail oldDetail = additionResultMap.get(key);

                        oldDetail.setRevisionSymbol(csvDetail.getRevisionSymbol());
                        oldDetail.setDrawingArea(csvDetail.getDrawingArea());
                        oldDetail.setPqs(csvDetail.getPqs());
                        oldDetail.setInspectionItemName(csvDetail.getInspectionItemName());
                        oldDetail.setDimensionValue(csvDetail.getDimensionValue());
                        oldDetail.setTolerancePlus(csvDetail.getTolerancePlus());
                        oldDetail.setToleranceMinus(csvDetail.getToleranceMinus());
                        oldDetail.setMeasurementMethod(csvDetail.getMeasurementMethod());
                        oldDetail.setOutgoingTrialInspectionObject(csvDetail.getOutgoingTrialInspectionObject());
                        oldDetail.setOutgoingProductionInspectionObject(csvDetail.getOutgoingProductionInspectionObject());
                        oldDetail.setIncomingTrialInspectionObject(csvDetail.getIncomingTrialInspectionObject());
                        oldDetail.setIncomingProductionInspectionObject(csvDetail.getIncomingProductionInspectionObject());
                        oldDetail.setMeasurementType(csvDetail.getMeasurementType());
                        oldDetail.setUpdateDate(sysDate);
                        oldDetail.setUpdateUserUuid(loginUser.getUserUuid());
                        oldDetail.setProcessInspetionObject(csvDetail.getProcessInspetionObject());
                        oldDetail.setEnableThAlert(csvDetail.isEnableThAlert());
                        updatedCount++;
                        entityManager.merge(oldDetail);

                    } else {// 寸法IDが重複しない場合、項目を登録する
                        
                        if(CommonConstants.MEASUREMENT_TYPE_MEASURE == csvDetail.getMeasurementType()){// 測定
                            maxMeasureLocalSeq++;
                            csvDetail.setLocalSeq(maxMeasureLocalSeq);
                        }else if(CommonConstants.MEASUREMENT_TYPE_VISUAL == csvDetail.getMeasurementType()){// 目視
                            maxVisualLocalSeq++;
                            csvDetail.setLocalSeq(maxVisualLocalSeq);
                        }

                        StringBuilder itemSno = new StringBuilder();
                        itemSno.append("ITEM");
                        itemCount++;
                        itemSno.append(FileUtil.addLeftZeroForNum(String.valueOf(itemCount), 3));
                        csvDetail.setInspectionItemSno(itemSno.toString());
                        csvDetail.setComponentInspectionItemsTableId(mstComponentInspectionItem.getId());
                        this.mstComponentInspectionItemService.setInspectionItemDetail(csvDetail);
                        addedCount++;
                    }

                }
                response.setInspectionItemId(mstComponentInspectionItem.getId());
                succeededCount = updatedCount + addedCount;
            } else {
                failedCount = totalCount;
            }
            response.setTotalCount(totalCount);
            response.setSucceededCount(succeededCount);
            response.setAddedCount(addedCount);
            response.setUpdatedCount(updatedCount);
            response.setDeletedCount(deletedCount);
            response.setFailedCount(failedCount);
            response.setLog(logFileUuid);

            //アップロードログをテーブルに書き出し
            TblCsvImport tblCsvImport = new TblCsvImport();
            tblCsvImport.setImportUuid(IDGenerator.generate());
            tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
            tblCsvImport.setImportDate(new Date());
            tblCsvImport.setImportTable(CommonConstants.MST_COMPONENT_INSPECTION_ITEM_DETAIL);
            TblUploadFile tblUploadFile = new TblUploadFile();
            tblUploadFile.setFileUuid(componentInspectionItemCSV.getFileUuid());
            tblCsvImport.setUploadFileUuid(tblUploadFile);
            MstFunction mstFunction = new MstFunction();
            mstFunction.setId(CommonConstants.FUN_ID_INSPECTION_ITEM);
            tblCsvImport.setFunctionId(mstFunction);
            tblCsvImport.setRecordCount(totalCount);
            tblCsvImport.setSuceededCount(convertToIntegerForCSV(succeededCount));
            tblCsvImport.setAddedCount(convertToIntegerForCSV(addedCount));
            tblCsvImport.setUpdatedCount(convertToIntegerForCSV(updatedCount));
            tblCsvImport.setDeletedCount(convertToIntegerForCSV(deletedCount));
            tblCsvImport.setFailedCount(convertToIntegerForCSV(failedCount));
            tblCsvImport.setLogFileUuid(logFileUuid);

            tblCsvImport.setLogFileName(FileUtil.getLogFileName(fileName));

            tblCsvImportService.createCsvImpor(tblCsvImport);
            return response;
        }
    }
    
    private int getMaxLocalSeq(String componentInspectionItemsTableId, int measurementType) {
        int maxLocalSeq = this.entityManager
                .createQuery("SELECT Max(mDetail.localSeq) FROM MstComponentInspectionItemsTableDetail mDetail "
                        + "WHERE mDetail.componentInspectionItemsTableId = :id "
                        + "AND mDetail.measurementType = :measurementType ", Integer.class)
                .setParameter("id", componentInspectionItemsTableId)
                .setParameter("measurementType", measurementType).getSingleResult();
        return maxLocalSeq;
    }

}
