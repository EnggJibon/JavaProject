/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ObjectResponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.apiuser.MstApiUser;
import com.kmcj.karte.resources.apiuser.MstApiUserService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectFileService;
import com.kmcj.karte.resources.component.inspection.item.MstComponentInspectionItemsTable;
import com.kmcj.karte.resources.component.inspection.item.MstComponentInspectionItemsTableDetail;
import com.kmcj.karte.resources.component.inspection.item.MstComponentInspectionItemsTableService;
import com.kmcj.karte.resources.component.inspection.item.model.MstComponentInspectItemsTableClassList;
import com.kmcj.karte.resources.component.inspection.item.model.MstComponentInspectionItemsTableClassVo;
import com.kmcj.karte.resources.component.inspection.referencefile.TblComponentInspectionReferenceFileService;
import com.kmcj.karte.resources.component.inspection.referencefile.model.ComponentInspectionReferenceFile;
import com.kmcj.karte.resources.component.inspection.result.model.*;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.Pager;
import org.apache.commons.lang.StringUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResult.AbortFlg;
import com.kmcj.karte.util.FileUtil;

/**
 * Master Component inspection result resource
 *
 * @author duanlin
 */
@RequestScoped
@Path("component/inspection/result")
public class TblComponentInspectionResultResource {

    @Inject
    private CnfSystemService cnfSystemService;
    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private TblComponentInspectionResultService tblComponentInspectionResultService;
    @Inject
    private MstComponentInspectionItemsTableService mstComponentInspectionItemsTableService;
    @Inject
    private MstApiUserService mstApiUserService;
    @Inject
    private MstCompanyService mstCompanyService;
    @Inject
    private MstComponentService mstComponentService;
    @Inject
    private MstComponentInspectFileService mstComponentInspectService;
    @Inject
    private TblComponentInspectionReferenceFileService refFileService;

    @Context
    private ContainerRequestContext requestContext;
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    /**
     * Get search result count
     *
     * @param component
     * @param componentCode
     * @param componentName
     * @param componentInspectType
     * @param poNumber
     * @param outgoingCompanyName
     * @param incomingCompanyName
     * @param inspectionStatus
     * @param fileCondition
     * @param outgoingInspectionResult
     * @param incomingInspectionResult
     * @param inspectionPersonName
     * @param confirmerName
     * @param approvePersonName
     * @param componentInspectionFrom
     * @param componentInspectionTo
     * @param productionLotNum
     * @param checkImplementDateFrom
     * @param checkImplementDateTo
     * @param approveDateFrom
     * @param approveDateTo
     * @param fileConfirmStatus
     * @param functionType
     * @param fileApproverName
     * @return
     */
    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getSearchResultCount(@QueryParam("component") String component,
            @QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("componentInspectType") String componentInspectType,
            @QueryParam("poNumber") String poNumber,
            @QueryParam("outgoingCompanyName") String outgoingCompanyName,
            @QueryParam("incomingCompanyName") String incomingCompanyName,
            @QueryParam("inspectionStatus") String inspectionStatus,
            @QueryParam("fileCondition") String fileCondition,
            @QueryParam("outgoingInspectionResult") String outgoingInspectionResult,
            @QueryParam("incomingInspectionResult") String incomingInspectionResult,
            @QueryParam("inspectionPersonName") String inspectionPersonName,
            @QueryParam("confirmerName") String confirmerName,
            @QueryParam("approvePersonName") String approvePersonName,
            @QueryParam("componentInspectionFrom") String componentInspectionFrom,
            @QueryParam("productionLotNum") String productionLotNum,
            @QueryParam("componentInspectionTo") String componentInspectionTo,
            @QueryParam("checkImplementDateFrom") String checkImplementDateFrom,
            @QueryParam("checkImplementDateTo") String checkImplementDateTo,
            @QueryParam("approveDateFrom") String approveDateFrom,
            @QueryParam("approveDateTo") String approveDateTo,
            @QueryParam("fileConfirmStatus") String fileConfirmStatus,
            @QueryParam("functionType") String functionType,
            @QueryParam("fileApproverName") String fileApproverName) {

        CountResponse response = new CountResponse();

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);

        Date formatComponentInspectionFrom = null;
        Date formatComponentInspectionTo = null;
        Date formatCheckImplementDateFrom = null;
        Date formatCheckImplementDateTo = null;
        Date formatApproveDateFrom = null;
        Date formatApproveDateTo = null;
        try {
            // 検査実施日
            if (StringUtils.isNotEmpty(componentInspectionFrom)) {
                formatComponentInspectionFrom = sdf.parse(componentInspectionFrom + CommonConstants.SYS_MIN_TIME);
            }
            if (StringUtils.isNotEmpty(componentInspectionTo)) {
                formatComponentInspectionTo = sdf.parse(componentInspectionTo + CommonConstants.SYS_MAX_TIME);
            }
            // 検査確認日
            if (StringUtils.isNotEmpty(checkImplementDateFrom)) {
                formatCheckImplementDateFrom = sdf.parse(checkImplementDateFrom + CommonConstants.SYS_MIN_TIME);
            }
            if (StringUtils.isNotEmpty(checkImplementDateTo)) {
                formatCheckImplementDateTo = sdf.parse(checkImplementDateTo + CommonConstants.SYS_MAX_TIME);
            }
            // 検査承認日
            if (StringUtils.isNotEmpty(approveDateFrom)) {
                formatApproveDateFrom = sdf.parse(approveDateFrom + CommonConstants.SYS_MIN_TIME);
            }
            if (StringUtils.isNotEmpty(approveDateTo)) {
                formatApproveDateTo = sdf.parse(approveDateTo + CommonConstants.SYS_MAX_TIME);
            }
        } catch (ParseException e) {
            Logger.getLogger(TblComponentInspectionResultResource.class.getName()).log(Level.INFO, null, e);
        }
        
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        ComponentInspectionResultSearchCond searchCondition = this.convertToSearchCondition(component, componentCode, componentName, componentInspectType,
                poNumber, outgoingCompanyName, incomingCompanyName, inspectionStatus, fileCondition, outgoingInspectionResult, incomingInspectionResult, inspectionPersonName,
                confirmerName, approvePersonName, formatComponentInspectionFrom, formatComponentInspectionTo,
                formatCheckImplementDateFrom, formatCheckImplementDateTo, productionLotNum, formatApproveDateFrom, formatApproveDateTo, fileConfirmStatus, functionType, fileApproverName);
        long count = this.tblComponentInspectionResultService.getSearchCount(searchCondition, loginUser.getLangId());
        response.setCount(count);

        CnfSystem cnfSystem = cnfSystemService.findByKey("system", "max_list_record_count");
        long sysCount = Long.parseLong(cnfSystem.getConfigValue());
        if (count > sysCount) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_confirm_max_record_count", sysCount);
        }
        return response;
    }

    /**
     * Get master component inspection result list
     *
     * @param component
     * @param componentCode
     * @param componentName
     * @param componentInspectType
     * @param poNumber
     * @param outgoingCompanyName
     * @param incomingCompanyName
     * @param inspectionStatus
     * @param fileCondition
     * @param outgoingInspectionResult
     * @param incomingInspectionResult
     * @param inspectionPersonName
     * @param confirmerName
     * @param approvePersonName
     * @param componentInspectionFrom
     * @param componentInspectionTo
     * @param productionLotNum
     * @param checkImplementDateFrom
     * @param checkImplementDateTo
     * @param approveDateFrom
     * @param approveDateTo
     * @param fileConfirmStatus
     * @param functionType
     * @param fileApproverName
     * @param massFlg 検査パターン(初物/量産/工程内)
     * @param sidx ソートキー
     * @param sord ソート方向
     * @param pageNumber ページNo
     * @param pageSize ページSize
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionResultResp getMstInspectionResultList(
            @QueryParam("component") String component,
            @QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("componentInspectType") String componentInspectType,
            @QueryParam("poNumber") String poNumber,
            @QueryParam("outgoingCompanyName") String outgoingCompanyName,
            @QueryParam("incomingCompanyName") String incomingCompanyName,
            @QueryParam("inspectionStatus") String inspectionStatus,
            @QueryParam("fileCondition") String fileCondition,
            @QueryParam("outgoingInspectionResult") String outgoingInspectionResult,
            @QueryParam("incomingInspectionResult") String incomingInspectionResult,
            @QueryParam("inspectionPersonName") String inspectionPersonName,
            @QueryParam("confirmerName") String confirmerName,
            @QueryParam("approvePersonName") String approvePersonName,
            @QueryParam("componentInspectionFrom") String componentInspectionFrom,
            @QueryParam("componentInspectionTo") String componentInspectionTo,
            @QueryParam("productionLotNum") String productionLotNum,
            @QueryParam("checkImplementDateFrom") String checkImplementDateFrom,
            @QueryParam("checkImplementDateTo") String checkImplementDateTo,
            @QueryParam("approveDateFrom") String approveDateFrom,
            @QueryParam("approveDateTo") String approveDateTo,
            @QueryParam("fileConfirmStatus") String fileConfirmStatus,
            @QueryParam("functionType") String functionType,
            @QueryParam("fileApproverName") String fileApproverName,
            @QueryParam("massFlg") String massFlg,
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);

        Date formatComponentInspectionFrom = null;
        Date formatComponentInspectionTo = null;
        Date formatCheckImplementDateFrom = null;
        Date formatCheckImplementDateTo = null;
        Date formatApproveDateFrom = null;
        Date formatApproveDateTo = null;
        try {
            // 検査実施日
            if (StringUtils.isNotEmpty(componentInspectionFrom)) {
                formatComponentInspectionFrom = sdf.parse(componentInspectionFrom + CommonConstants.SYS_MIN_TIME);
            }
            if (StringUtils.isNotEmpty(componentInspectionTo)) {
                formatComponentInspectionTo = sdf.parse(componentInspectionTo + CommonConstants.SYS_MAX_TIME);
            }
            // 検査確認日
            if (StringUtils.isNotEmpty(checkImplementDateFrom)) {
                formatCheckImplementDateFrom = sdf.parse(checkImplementDateFrom + CommonConstants.SYS_MIN_TIME);
            }
            if (StringUtils.isNotEmpty(checkImplementDateTo)) {
                formatCheckImplementDateTo = sdf.parse(checkImplementDateTo + CommonConstants.SYS_MAX_TIME);
            }
            // 検査承認日
            if (StringUtils.isNotEmpty(approveDateFrom)) {
                formatApproveDateFrom = sdf.parse(approveDateFrom + CommonConstants.SYS_MIN_TIME);
            }
            if (StringUtils.isNotEmpty(approveDateTo)) {
                formatApproveDateTo = sdf.parse(approveDateTo + CommonConstants.SYS_MAX_TIME);
            }
        } catch (ParseException e) {
            Logger.getLogger(TblComponentInspectionResultResource.class.getName()).log(Level.INFO, null, e);
        }

        ComponentInspectionResultSearchCond searchCondition = this.convertToSearchCondition(component, componentCode, componentName, componentInspectType,
                poNumber, outgoingCompanyName, incomingCompanyName, inspectionStatus, fileCondition, outgoingInspectionResult, incomingInspectionResult, inspectionPersonName,
                confirmerName, approvePersonName, formatComponentInspectionFrom, formatComponentInspectionTo,
                formatCheckImplementDateFrom, formatCheckImplementDateTo, productionLotNum, formatApproveDateFrom, formatApproveDateTo, fileConfirmStatus, functionType, fileApproverName);
        searchCondition.setMassFlg(massFlg);
        searchCondition.setSidx(sidx);
        searchCondition.setSord(sord);
        searchCondition.setPageNumber(pageNumber);
        searchCondition.setPageSize(pageSize);
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        ComponentInspectionResultResp response = new ComponentInspectionResultResp();
        int count = this.tblComponentInspectionResultService.getSearchCount(searchCondition,loginUser.getLangId());
        response.setCount(count);
        response.setPageNumber(pageNumber);
        Pager pager = new Pager();
        response.setPageTotal(pager.getTotalPage(pageSize, count));
        response.setInspectionResultList(this.tblComponentInspectionResultService.getInspectionResults(searchCondition, false, loginUser.getLangId()));

        return response;
    }

    /**
     * Create outgoing inspection form.
     *
     * @param inputInfo
     * @return
     */
    @POST
    @Path("/outgoing/createform")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionResultResp createOutgoingInspectionForm(ComponentInspectionFormCreateInput inputInfo) {
        ComponentInspectionResultResp response = new ComponentInspectionResultResp();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstCompany selfCompany = this.mstCompanyService.getSelfCompany();
        String outgoingCompanyId = selfCompany.getId();

        // check whether has inspection items table
        String componentInspectionItemsTableId = this.mstComponentInspectionItemsTableService.getCurrentInsepectionItemsTableId(
                inputInfo.getComponentId(), outgoingCompanyId, inputInfo.getIncomingCompanyId());
        if (StringUtils.isEmpty(componentInspectionItemsTableId)) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_component_insp_items_table_not_exist");
            return response;
        }
        inputInfo.setComponentInspectionItemsTableId(componentInspectionItemsTableId);
        //check
        long sysCount = checkInspectionCount(inputInfo);
        if (0 < sysCount) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_less_than_max_insp_detail_cnt");
            response.setErrorMessage(String.format(msg, sysCount));
            return response;
        }
        // create outgoing inspection form
        String inspectionResultId = this.tblComponentInspectionResultService.createOutgoingInspectionForm(inputInfo, loginUser);

        // get created data
        response.setInspectionResultInfo(this.tblComponentInspectionResultService.getInspectionItemDetails(
                inspectionResultId, CommonConstants.INSPECTION_TYPE_OUTGOING, loginUser.getLangId()));

        return response;
    }

    /**
     * Create incoming inspection form.
     *
     * @param inputInfo
     * @return
     */
    @POST
    @Path("/incoming/createform")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionResultResp createIncomingInspectionForm(ComponentInspectionFormCreateInput inputInfo) {

        ComponentInspectionResultResp response = new ComponentInspectionResultResp();

        String componentInspectionResultId = inputInfo.getComponentInspectionResultId();

        TblComponentInspectionResult tblCompInspResult
                = this.tblComponentInspectionResultService.getTblInspectionItemResult(componentInspectionResultId);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (tblCompInspResult == null) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_component_outgoing_inspection_not_exist");
            return response;
        }
        inputInfo.setComponentInspectionItemsTableId(tblCompInspResult.getComponentInspectionItemsTableId());
        inputInfo.setComponentId(tblCompInspResult.getComponentId());
        //check
        long sysCount = checkInspectionCount(inputInfo);
        if (0 < sysCount) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_less_than_max_insp_detail_cnt");
            response.setErrorMessage(String.format(msg, sysCount));
            return response;
        }

        // create incoming inspection form
        this.tblComponentInspectionResultService.createIncomingInspectionForm(inputInfo, loginUser);

        // get created data
        response.setInspectionResultInfo(this.tblComponentInspectionResultService.getInspectionItemDetails(
                componentInspectionResultId, CommonConstants.INSPECTION_TYPE_OUTGOING, loginUser.getLangId()));

        return response;
    }

    /**
     * Get component inspection result details
     *
     * @param inspectionResultId
     * @param inspectionType
     * @return
     */
    @GET
    @Path("/{inspectionResultId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionResultResp getInspectionItemDetails(
            @PathParam("inspectionResultId") String inspectionResultId,
            @QueryParam("inspectionType") Integer inspectionType) {

        ComponentInspectionResultResp response = new ComponentInspectionResultResp();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        ComponentInspectionResultInfo resultInfo = this.tblComponentInspectionResultService.getInspectionItemDetails(inspectionResultId, inspectionType, loginUser.getLangId());
        if (resultInfo == null) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist");
            return response;
        }
        response.setInspectionResultInfo(resultInfo);
        return response;
    }

    /**
     * Update outgoing or incoming confirm inspection results 一括確認/確認
     *
     * @param inputInfo
     * @return
     * @author Apeng 20171010 add
     */
    @PUT
    @Path("/confirm")
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateConfirmInspectionResult(ComponentInspectionActionInput inputInfo) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentInspectionResultService.confirmInspectionResult(inputInfo, loginUser);
    }

    /**
     * Update outgoing or incoming inspection results
     *
     * @param inputInfo
     * @return
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateInspectionResult(ComponentInspectionResultSaveInput inputInfo) {
        BasicResponse response = new BasicResponse();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        inputInfo.setComponentInspectionResultId(inputInfo.getInspectionResultIds());

        boolean result = this.tblComponentInspectionResultService.updateInspectionResult(inputInfo, inputInfo.getInspectionResultIds(), loginUser);
        if (!result) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist");
            return response;
        }
        return response;
    }
    
    @PUT
    @Path("/detail")
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateResultDetail(ComponentInspectionResultSaveInput inputInfo) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        this.tblComponentInspectionResultService.updateInspectionResultDetail(inputInfo, loginUser.getUserUuid());
        return response;
    }

    /**
     * 検査バッチ連携更新ステータス更新
     *
     * @param inputFile
     * @return
     */
    @PUT
    @Path("batch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateInspectionResultBatchStatus(ComponentInspectionReferenceFile inputFile) {
        BasicResponse response = new BasicResponse();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        boolean result = this.tblComponentInspectionResultService.updateInspectionResultBatchStatus(inputFile, loginUser);
        if (!result) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist");
            return response;
        }

        return response;
    }

    /**
     * 部品検査別参照ファイル更新
     *
     * @param inputFile
     * @return
     */
    @PUT
    @Path("file")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionResultFileResponse updateComponentInspectionReferenceFile(ComponentInspectionReferenceFile inputFile) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        if (inputFile.getSaveFileType().equals(2)) { // update inspection result file
            return tblComponentInspectionResultService.updateComponentInspectionResultFile(inputFile, loginUser);
        } else {
            return tblComponentInspectionResultService.updateComponentInspectionReferenceFile(inputFile, loginUser);
        }
    }

    /**
     * Approve outgoing or incoming inspection results 一括承認/承認
     *
     * @param inputInfo
     * @return
     */
    @PUT
    @Path("/approve")
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse approve(ComponentInspectionActionInput inputInfo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentInspectionResultService.approve(inputInfo, loginUser);
    }

    /**
     * Accept outgoing inspection results
     *
     * @param inputInfo
     * @return
     */
    @PUT
    @Path("/accept")
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse accept(ComponentInspectionActionInput inputInfo) {
        BasicResponse response = new BasicResponse();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        boolean result = this.tblComponentInspectionResultService.accept(inputInfo.getInspectionResultIds(),
                inputInfo.getAction(), inputInfo.getComment(), inputInfo.getPrivateComment(), loginUser);
        if (!result) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist");
            return response;
        }
        return response;
    }

    /**
     * Exempt incoming inspection
     *
     * @param inputInfo
     * @return
     */
    @PUT
    @Path("/exempt")
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse exempt(ComponentInspectionActionInput inputInfo) {
        BasicResponse response = new BasicResponse();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        boolean result = this.tblComponentInspectionResultService.exempt(inputInfo.getInspectionResultIds(), inputInfo.getComment(), loginUser);
        if (!result) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist");
            return response;
        }
        return response;
    }

    /**
     * Delete inspection
     *
     * @param inspectionResultIds
     * @return
     */
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteInpsectionForm(List<String> inspectionResultIds) {
        BasicResponse response = new BasicResponse();

        boolean result = true;
        for (String resultId : inspectionResultIds) {
            boolean iresult = this.tblComponentInspectionResultService.deleteInpsectionForm(resultId);
            result = result && iresult;
        }

        if (!result) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist");
            return response;
        }
        return response;
    }

    /**
     * Get external outgoing inspection result
     *
     * @return
     */
    @GET
    @Path("/outgoing/extdata/get")
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionResultResp getExtOutgoingInspectionResult() {
        ComponentInspectionResultResp response = new ComponentInspectionResultResp();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstApiUser mstApiUser = this.mstApiUserService.getApiMstUser(loginUser.getUserid());
        String incomingCompanyId = mstApiUser.getCompanyId();

        List<ComponentInspectionResultInfoForBatch> batchData
                = this.tblComponentInspectionResultService.getExtOutgoingInspectionResult(incomingCompanyId);
        response.setDataForBatch(batchData);
        return response;
    }

    /**
     * Notify get result of external outgoing inspection
     *
     * @param resultIdList
     * @return
     */
    @POST
    @Path("/outgoing/extdata/notify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse notifyHaveGetOutgoingResultData(List<String> resultIdList) {
        return this.tblComponentInspectionResultService.updateInspBatchUpdateStatus(resultIdList, CommonConstants.INSP_BATCH_UPDATE_STATUS_O_RESULT_SENT);
    }

    /**
     * Push external incoming inspection result
     *
     * @param dataList
     * @return
     */
    @POST
    @Path("/incoming/extdata/push")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblComponentInspectionResultVo pushExtIncomingInspectionResult(
            List<ComponentInspectionResultInfoForBatch> dataList) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstApiUser mstApiUser = this.mstApiUserService.getApiMstUser(loginUser.getUserid());
        String incomingCompanyId = mstApiUser.getCompanyId();

        return this.tblComponentInspectionResultService.updateIncomingData(dataList, incomingCompanyId);
    }

    /**
     * 検査結果ダウンロードCSV出力
     *
     * @param component
     * @param componentId
     * @param componentCode
     * @param componentName
     * @param outgoingCompanyId
     * @param outgoingCompanyName
     * @param incomingCompanyId
     * @param incomingCompanyName
     * @param checkImplementDateFrom
     * @param checkImplementDateTo
     * @param inspectType
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getInspectionResultCSV(
            @QueryParam("component") String component,
            @QueryParam("componentId") String componentId,
            @QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("outgoingCompanyId") String outgoingCompanyId,
            @QueryParam("outgoingCompanyName") String outgoingCompanyName,
            @QueryParam("incomingCompanyId") String incomingCompanyId,
            @QueryParam("incomingCompanyName") String incomingCompanyName,
            @QueryParam("checkImplementDateFrom") String checkImplementDateFrom, //検索開始実施日
            @QueryParam("checkImplementDateTo") String checkImplementDateTo, //検索终了実施日
            @QueryParam("inspectionType") String inspectType
    ) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        //実施日From - To
        Date formatCheckImplementDateFrom = null;
        Date formatCheckImplementDateTo = null;
        try {
            // 検査開始実施日
            if (StringUtils.isNotEmpty(checkImplementDateFrom)) {
                formatCheckImplementDateFrom = sdf.parse(checkImplementDateFrom + CommonConstants.SYS_MIN_TIME);
            }

            // 検査终了実施日
            if (StringUtils.isNotEmpty(checkImplementDateTo)) {
                formatCheckImplementDateTo = sdf.parse(checkImplementDateTo + CommonConstants.SYS_MAX_TIME);
            }
        } catch (ParseException e) {
            Logger.getLogger(TblComponentInspectionResultResource.class.getName()).log(Level.INFO, null, e);
        }
        ComponentInspectionResultSearchCond searchCondition = this.convertToSearchCondition(component, componentCode,
                componentName, null, null, incomingCompanyName, outgoingCompanyName, null, null,null, null, null, null, null,
                formatCheckImplementDateFrom, formatCheckImplementDateTo, null, null, null, null, null, null, null, null);
        
        searchCondition.setFunctionType(inspectType);

        return tblComponentInspectionResultService.postTblComponentInspectionResultCSV(searchCondition, componentId, outgoingCompanyId, incomingCompanyId, loginUser);
    }

    /**
     * @param inputInfo
     * @return
     */
    @PUT
    @Path("/po")
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse tblComponentInspectionResultPo(ComponentInspectionActionInput inputInfo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblComponentInspectionResultService.updateTblComponentInspectionResultPo(inputInfo, loginUser);

    }

    @PUT
    @Path("/material")
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse tblComponentInspectionResultMaterial(ComponentInspectionActionInput inputInfo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentInspectionResultService.updateTblComponentInspectionResultMaterial(inputInfo, loginUser);
    }
    
    @PUT
    @Path("/inspectdate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateInspectDate(ComponentInspectionActionInput inputInfo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        tblComponentInspectionResultService.updateInspectDate(inputInfo, loginUser);
        return new BasicResponse();
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
     * @param poNumber
     * @param outgoingCompanyName
     * @param incomingCompanyName
     * @param inspectionStatus
     * @param fileCondition
     * @param outgoingInspectionResult
     * @param incomingInspectionResult
     * @param inspectionPersonName
     * @param confirmerName
     * @param approvePersonName
     * @param componentInspectionFrom
     * @param componentInspectionTo
     * @param checkImplementDateFrom
     * @param checkImplementDateTo
     * @param productionLotNum
     * @param approveDateFrom
     * @param approveDateTo
     * @param fileConfirmStatus
     * @param functionType
     * @param fileApproverName
     * @return
     */
    private ComponentInspectionResultSearchCond convertToSearchCondition(String component, String componentCode, String componentName, String componentInspectType, 
            String poNumber, String outgoingCompanyName, String incomingCompanyName, String inspectionStatus, String fileCondition, String outgoingInspectionResult,
            String incomingInspectionResult, String inspectionPersonName, String confirmerName, String approvePersonName,
            Date componentInspectionFrom, Date componentInspectionTo, Date checkImplementDateFrom,
            Date checkImplementDateTo, String productionLotNum, Date approveDateFrom, Date approveDateTo, String fileConfirmStatus, String functionType, String fileApproverName) {
        ComponentInspectionResultSearchCond searchCondition = new ComponentInspectionResultSearchCond();
        searchCondition.setFunctionType(functionType);
        searchCondition.setComponent(component);
        searchCondition.setComponentCode(componentCode);
        searchCondition.setComponentName(componentName);
        searchCondition.setComponentInspectType(componentInspectType);
        searchCondition.setPoNumber(poNumber);
        searchCondition.setOutgoingCompanyName(outgoingCompanyName);
        searchCondition.setIncomingCompanyName(incomingCompanyName);
        searchCondition.setInspectionStatus(inspectionStatus);
        searchCondition.setFileCondition(fileCondition);
        searchCondition.setOutgoingInspectionResult(outgoingInspectionResult);
        searchCondition.setIncomingInspectionResult(incomingInspectionResult);
        searchCondition.setInspectionPersonName(inspectionPersonName);
        searchCondition.setConfirmerName(confirmerName);
        searchCondition.setApprovePersonName(approvePersonName);
        searchCondition.setComponentInspectionFrom(componentInspectionFrom);
        searchCondition.setComponentInspectionTo(componentInspectionTo);
        searchCondition.setCheckImplementDateFrom(checkImplementDateFrom);
        searchCondition.setCheckImplementDateTo(checkImplementDateTo);
        searchCondition.setApproveDateFrom(approveDateFrom);
        searchCondition.setApproveDateTo(approveDateTo);
        searchCondition.setFileConfirmStatus(fileConfirmStatus);
        searchCondition.setProductionLotNum(productionLotNum);
        // km-976 帳票確認者の検索を追加 20181121 start
        searchCondition.setFileApproverName(fileApproverName);
        // km-976 帳票確認者の検索を追加 20181121 end

        MstCompany selfCompany = this.mstCompanyService.getSelfCompany();
        if (selfCompany != null) {
            searchCondition.setMyCompanyId(selfCompany.getId());
        }
        return searchCondition;
    }

    private void setErrorInfo(BasicResponse response, String errorCode, String msgDictKey, Object... args) {
        response.setError(true);
        response.setErrorCode(errorCode);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), msgDictKey);
        if (args != null) {
            msg = String.format(msg, args);
        }
        response.setErrorMessage(msg);
    }

    //ADDS_M 2018/05/14 bacpd
    @PUT
    @Path("extcomplete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse extComplete(ComponentInspectionExtCompleteInput extCompleteInput) {
        TblComponentInspectionResult currentInspectionResult = tblComponentInspectionResultService.getInspectionResultById(extCompleteInput.getId());
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        if (currentInspectionResult == null) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return response;
        } else {
            if (currentInspectionResult.getInspectionStatus() != CommonConstants.INSPECTION_STATUS_I_INSPECTING && 
                    currentInspectionResult.getInspectionStatus() != CommonConstants.INSPECTION_STATUS_I_AGAIN_REJECTED) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_invalid_inspection_status"));
                return response;
            }

            if (extCompleteInput.getInspectionStatus() != CommonConstants.INSPECTION_STATUS_I_CONFIRM &&
                    extCompleteInput.getInspectionStatus() != CommonConstants.INSPECTION_STATUS_I_APPROVED) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_invalid_inspection_status"));
                return response;
            }

            if (extCompleteInput.getIncomingInspectionResult() == null ||
                    extCompleteInput.getIncomingInspectionResult() == CommonConstants.INSPECTION_RESULT_UNDEFINED ||
                    (extCompleteInput.getIncomingInspectionResult() == CommonConstants.INSPECTION_RESULT_SPECIAL
                            && (extCompleteInput.getIncomingInspectionComment().trim().isEmpty() || extCompleteInput.getIncomingInspectionComment() == null)) ||
                    (extCompleteInput.getInspectionStatus() == CommonConstants.INSPECTION_STATUS_I_APPROVED
                            && extCompleteInput.getIncomingInspectionResult() == CommonConstants.INSPECTION_RESULT_NG
                            && (extCompleteInput.getIncomingInspectionComment().trim().isEmpty() || extCompleteInput.getIncomingInspectionComment() == null)
                    )
            ) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_invalid_inspection_result"));
                return response;
            }

            if (Objects.equals(currentInspectionResult.getFileConfirmStatus(), CommonConstants.FILE_CONFIRM_STATUS_DEFAULT) 
                    && refFileService.isAnyFileReqired(currentInspectionResult.getId())) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_invalid_evidence_file"));
                return response;
            }

            //Update inspection result
             tblComponentInspectionResultService.updateComponentInspectionExtComplete(extCompleteInput, loginUser);
             response.setErrorCode("");
             response.setErrorMessage("");
        }

        return response;
    }

    // ADDSインタフェースを対応 2017/11/2 penggd Start

    /**
     * ADDSインタフェース
     *
     * @param componentInspectionExtAcceptVo
     * @return
     */
    @POST
    @Path("extaccept")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblComponentInspectionResultVo extAccept(ComponentInspectionExtAcceptVo componentInspectionExtAcceptVo) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblComponentInspectionResultService.extAccept(componentInspectionExtAcceptVo.getComponentCode(),
                componentInspectionExtAcceptVo.getInspectType(), componentInspectionExtAcceptVo.getPoNumber(),
                componentInspectionExtAcceptVo.getIncomingMeasSamplingQuantity(),
                componentInspectionExtAcceptVo.getIncomingVisualSamplingQuantity(), loginUser);
    }
    // ADDSインタフェースを対応 2017/11/2 penggd End

    // KM-463 PO検査間のデータ構造変更 2017/12/5 by penggd Start

    /**
     * Get external poInfo
     *
     * @return
     */
    @GET
    @Path("/outgoing/poinfo/get")
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionResultResp getExtOutgoingPoInfo() {
        ComponentInspectionResultResp response = new ComponentInspectionResultResp();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstApiUser mstApiUser = this.mstApiUserService.getApiMstUser(loginUser.getUserid());
        String incomingCompanyId = mstApiUser.getCompanyId();

        ComponentPoInfoForBatch batchData
                = this.tblComponentInspectionResultService.getExtOutgoingPoInfo(incomingCompanyId);
        response.setDataForPoInfoBatch(batchData);
        return response;
    }

    /**
     * tblPoテーブルのバッチ連携ステータスは連携済に更新
     *
     * @param resultIdList
     * @return
     */
    @POST
    @Path("/outgoing/extdata/podone")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateOutgoingPoDataStatus(List<String> resultIdList) {
        return this.tblComponentInspectionResultService.updatePoBatchUpdateStatus(resultIdList,
                CommonConstants.PO_BATCH_UPDATE_STATUS_DONE);
    }

    /**
     * tblＳｈｉｐｍｅｎｔテーブルのバッチ連携ステータスは連携済に更新
     *
     * @param resultIdList
     * @return
     */
    @POST
    @Path("/outgoing/extdata/shipmentdone")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateOutgoingShipmentDataStatus(List<String> resultIdList) {
        return this.tblComponentInspectionResultService.updateShipmentBatchUpdateStatus(resultIdList,
                CommonConstants.PO_BATCH_UPDATE_STATUS_DONE);
    }
    // KM-463 PO検査間のデータ構造変更 2017/12/5 by penggd End

    /**
     * 起動モード作成
     *
     * @param inputInfo
     * @return
     */
    @POST
    @Path("/boot/createform")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionResultResp createBootModelComponentInspectionResult(ComponentInspectionFormCreateInput inputInfo) {
        ComponentInspectionResultResp response = new ComponentInspectionResultResp();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstCompany selfCompany = this.mstCompanyService.getSelfCompany();
        String outgoingCompanyId = selfCompany.getId();

        //部品検査項目表マスタcheck
        String componentInspectionItemsTableId = this.mstComponentInspectionItemsTableService.getCurrentInsepectionItemsTableId(
                inputInfo.getComponentId(), inputInfo.getIncomingCompanyId(), outgoingCompanyId);
        if (StringUtils.isEmpty(componentInspectionItemsTableId)) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_component_insp_items_table_not_exist");
            return response;
        }
        inputInfo.setComponentInspectionItemsTableId(componentInspectionItemsTableId);
        //check
        long sysCount = checkInspectionCount(inputInfo);
        if (0 < sysCount) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_less_than_max_insp_detail_cnt");
            response.setErrorMessage(String.format(msg, sysCount));
            return response;
        }

        //起動モード作成
        String inspectionResultId = this.tblComponentInspectionResultService.createBootModelComponentInspectionResult(inputInfo, loginUser);
        //add inspection result file
        this.tblComponentInspectionResultService.createTblComponentInspectionResultFile(inspectionResultId, inputInfo, loginUser.getUserUuid());
        response.setInspectionResultInfo(this.tblComponentInspectionResultService.getInspectionItemDetails(
                inspectionResultId, CommonConstants.INSPECTION_TYPE_OUTGOING, loginUser.getLangId()));

        return response;
    }


    @PUT
    @Path("incoming/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionAcceptanceResponse createAcceptanceComponentInspection(ComponentInspectionAcceptance inputInfo) {
        ComponentInspectionAcceptanceResponse response = new ComponentInspectionAcceptanceResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstCompany selfCompany = this.mstCompanyService.getSelfCompany();
        String incomingCompanyId = selfCompany.getId();

        //validate attribute
        MstComponent currentComponent = this.mstComponentService.getMstComponentByCode(inputInfo.getComponentCode());
        if (currentComponent == null) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_component_code_not_found"));
            return response;
        }

        MstCompany outgoingCompany = this.mstCompanyService.getMstCompanyByCode(inputInfo.getOutgoingCompanyCode());
        if (outgoingCompany == null) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_outgoing_company_code_not_found"));
            return response;
        }

        //switch ComponentInspectionAcceptance -> ComponentInspectionFormCreateInput
        MstComponentInspectItemsTableClassList listFirstFlag = this.mstComponentInspectService.getMstComponentInspectionItemsTableClassList(currentComponent.getId(), "", outgoingCompany.getId(), "", loginUser);
        Boolean inspectTypeAvailable = false;
        for (MstComponentInspectionItemsTableClassVo dataFirstFlag : listFirstFlag.getMstComponentInspectionItemsTableClassVos()) {
            if (Objects.equals(inputInfo.getInspectType(), dataFirstFlag.getDictValue())) {
                inputInfo.setFirstFlag(dataFirstFlag.getId());
                inputInfo.setMassFlg(dataFirstFlag.getMassFlg());
                inspectTypeAvailable = true;
                break;
            }
        }
        
        if (FileUtil.getStringValue(inputInfo.getCavityPrefix()).length() > 10) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_over_length_with_item");
            response.setErrorMessage(String.format(msg, mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "cav_connector_word"), FileUtil.getStringValue(inputInfo.getCavityPrefix()).length()));
            return response;
        }

        if (!inspectTypeAvailable) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_inspect_type_not_found"));
            return response;
        }

        ComponentInspectionFormCreateInput createInput = new ComponentInspectionFormCreateInput();
        createInput.setFirstFlag(inputInfo.getFirstFlag());
        createInput.setInspectionType(CommonConstants.INSPECTION_TYPE_INCOMING);
        createInput.setPoNumber(inputInfo.getOrderNumber());
        createInput.setItemNumber(inputInfo.getItemNumber());
        createInput.setProductionLotNum(inputInfo.getLotNumber());
        createInput.setQuantity(inputInfo.getQuantity());
        createInput.setComponentId(currentComponent.getId());
        createInput.setIncomingCompanyId(outgoingCompany.getId());
        createInput.setIncomingMeasSamplingQuantity(inputInfo.getIncomingMeasSamplingQuantity());
        createInput.setIncomingVisualSamplingQuantity(inputInfo.getIncomingVisualSamplingQuantity());
        createInput.setMassFlg(Integer.valueOf(inputInfo.getMassFlg().toString()));
        createInput.setMaterial01(StringUtils.isEmpty(inputInfo.getMaterial01()) ? "" : inputInfo.getMaterial01());
        createInput.setMaterial02(StringUtils.isEmpty(inputInfo.getMaterial02()) ? "" : inputInfo.getMaterial02());
        createInput.setMaterial03(StringUtils.isEmpty(inputInfo.getMaterial03()) ? "" : inputInfo.getMaterial03());
        if(null == inputInfo.getCavityStartNum() || 0 == inputInfo.getCavityStartNum()){
            createInput.setCavityStartNum(1);
        }else{
            createInput.setCavityStartNum(inputInfo.getCavityStartNum());
        }
        if(null == inputInfo.getCavityCnt() || 0 == inputInfo.getCavityCnt()){
            createInput.setCavityCnt(1);
        }else{
            createInput.setCavityCnt(inputInfo.getCavityCnt());
        }
        createInput.setCavityPrefix(FileUtil.getStringValue(inputInfo.getCavityPrefix()));

        //部品検査項目表マスタcheck
        MstComponentInspectionItemsTable componentInspectionItemsTable = this.mstComponentInspectionItemsTableService.checkCurrentInspectionItemsTable(
                currentComponent.getId(), outgoingCompany.getId(), incomingCompanyId);
        if (componentInspectionItemsTable == null) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_component_insp_items_table_not_exist");
            return response;
        }
        createInput.setComponentInspectionItemsTableId(componentInspectionItemsTable.getId());

        //check
        long sysCount = checkInspectionCount(createInput);
        if (0 < sysCount) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_less_than_max_insp_detail_cnt");
            response.setErrorMessage(String.format(msg, sysCount));
            return response;
        }
        //起動モード作成
        String  inspectionResultId = this.tblComponentInspectionResultService.createBootModelComponentInspectionResult(createInput, loginUser);
        //add inspection result file
        this.tblComponentInspectionResultService.createTblComponentInspectionResultFile(inspectionResultId, createInput, loginUser.getUserUuid());
        response.setId(inspectionResultId);
        return response;
    }

    /**
     * 検査結果詳細追加/削除抜取数
     *
     * @param inputInfo
     * @return
     */
    @PUT
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateInspectionResultQuantity(ComponentInspectionResultSaveInput inputInfo) {
        BasicResponse response = new BasicResponse();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        boolean result = this.tblComponentInspectionResultService.updateInspectionResultQuantity(inputInfo, loginUser);
        if (!result) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist");
            return response;
        }
        return response;
    }

    /**
     * 測定名称変更
     *
     * @param tblComponentInspectionSampleName
     * @return
     */
    @PUT
    @Path("create/measure")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateInspectionResultMeasureName(TblComponentInspectionSampleName tblComponentInspectionSampleName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentInspectionResultService.updateInspectionResultMeasureName(tblComponentInspectionSampleName, loginUser);
    }

    /**
     * 測定データ参照
     *
     * @param componentCode
     * @param productionLotNum
     * @param inspectionType
     * @return
     */
    @GET
    @Path("measurement/search")
    @Produces(MediaType.APPLICATION_JSON)
    public ComponentInspectionResultResp getMeasurementDateSearch(
            @QueryParam("componentCode") String componentCode,
            @QueryParam("productionLotNum") String productionLotNum,
            @QueryParam("inspectionType") Integer inspectionType) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblComponentInspectionResultService.getMeasurementDateSearch(componentCode, productionLotNum, inspectionType, loginUser.getLangId());
    }

    /**
     * 検査結果の帳票をExcelで出力する
     *
     * @param inspectionResultId
     * @param inspectionType
     * @return
     */
    @GET
    @Path("download/excel/{inspectionResultId}/{inspectionType}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/octet-stream;charset=UTF-8")
    public Response getInspectionResultDownloadExcel(@PathParam("inspectionResultId") String inspectionResultId,
                                                     @PathParam("inspectionType") Integer inspectionType) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentInspectionResultService.getInspectionResultDownloadExcel(inspectionResultId, inspectionType, loginUser.getLangId());
    }

    /**
     * 検査結果対象は複数の場合、圧縮処理を行う
     *
     * @param componentInspectionFormCreateInput
     * @return
     */
    @POST
    @Path("download/zip")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getInspectionResultDownloadZip(ComponentInspectionFormCreateInput componentInspectionFormCreateInput) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentInspectionResultService.getInspectionResultDownloadZip(componentInspectionFormCreateInput, loginUser.getLangId());
    }

    /**
     * KM-812  Function save/update setting by screenId
     * 測定(出荷)/目視(出荷)/測定(受入)/目視(受入)
     * @param detailColumnInputs
     * @return
     */
    @POST
    @Path("grid/column/save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse saveInspectionDetailGridColumn(List<ComponentInspectionDetailColumnInput> detailColumnInputs) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        List<String> listColumnsExists = this.tblComponentInspectionResultService.getListColumnGridExists(detailColumnInputs, loginUser.getUserUuid());
        if (listColumnsExists == null) {
            this.tblComponentInspectionResultService.createTblGridColumnMemory(detailColumnInputs, loginUser.getUserUuid());
        } else {
            for (ComponentInspectionDetailColumnInput itemColumns : detailColumnInputs) {
                if (listColumnsExists.contains(itemColumns.getColumnId())) {
                    //update attributes
                    this.tblComponentInspectionResultService.updateTblGridColumnMemory(itemColumns, loginUser.getUserUuid());
                } else {
                    //insert missing attributes
                    List<ComponentInspectionDetailColumnInput> newColumnInsert = new ArrayList<>();
                    newColumnInsert.add(itemColumns);
                    this.tblComponentInspectionResultService.createTblGridColumnMemory(newColumnInsert, loginUser.getUserUuid());
                }
            }
        }
        return response;
    }


    /**
     * Get setting columns by screenId
     * @param screenId
     * @return
     */
    @GET
    @Path("grid/column/settings")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ComponentInspectionDetailColumnResponse getInspectionDetailGridColumn(@QueryParam("screenId") String screenId) {
        ComponentInspectionDetailColumnResponse response = new ComponentInspectionDetailColumnResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        List<TblGridColumnMemory> listInspection = this.tblComponentInspectionResultService.getSettingDetailColumns(loginUser.getUserUuid(), screenId);
        response.setListInspectionDetailColumn(listInspection);
        return response;
    }
    
    /**
     * <P>
     * 受入検査の際に、納品先が自社するかどうかチェック
     *
     * @param inspectionResultId
     * @return
     */
    @GET
    @Path("outgoing/incomingcompany/check")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse chkIncomingCompany(@QueryParam("inspectionResultId") String inspectionResultId) {
        
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        // 自社を取得する
        MstCompany selfCompany = this.mstCompanyService.getSelfCompany();
        String selfCompanyId = selfCompany.getId();

        return tblComponentInspectionResultService.chkIncomingCompany(inspectionResultId, selfCompanyId, loginUser.getLangId());
    }
 
    /**
     * Get Process capability indexs
     * @param component
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param inspectionType
     * @param inspectClass
     * @param productionLot
     * @param inspectionDateStart
     * @param inspectionDateEnd
     * @param cavityPrefix
     * @param cavityNum
     * @param ischeck
     * @return
     */
    @GET
    @Path("processcapabilityindex")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ComponentInspectionResultInfo getProcessCapabilityIndex(@QueryParam("component") String component,
            @QueryParam("outgoingCompanyId") String outgoingCompanyId,
            @QueryParam("incomingCompanyId") String incomingCompanyId,
            @QueryParam("inspectionType") int inspectionType,
            @QueryParam("inspectClass") String inspectClass,
            @QueryParam("productionLot") String productionLot,
            @QueryParam("inspectionDateStart") String inspectionDateStart,
            @QueryParam("inspectionDateEnd") String inspectionDateEnd,
            @QueryParam("cavityPrefix") String cavityPrefix,
            @QueryParam("cavityNum") int cavityNum,
            @QueryParam("ischeck") boolean ischeck
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentInspectionResultService.getProcesscapabilityIndexInfo( component, outgoingCompanyId, incomingCompanyId, inspectionType, inspectClass, productionLot, inspectionDateStart, inspectionDateEnd, cavityPrefix, cavityNum, ischeck, loginUser);
    }
    
 
    /**
     * Get X-bar/R Chart
     * @param component
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param inspectionType
     * @param inspectClass
     * @param productionLot
     * @param inspectionDateStart
     * @param inspectionDateEnd
     * @param n
     * @param a2
     * @param d3
     * @param d4
     * @param drawingPage
     * @param drawingAnnotation
     * @param drawingMentionNo
     * @param similarMultiitem
     * @param cavityPrefix
     * @param cavityNum
     * @return
     */
    @GET
    @Path("xbarandrchart")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ComponentInspectionResultInfo getXbarAndRChartInfo(@QueryParam("component") String component,
            @QueryParam("outgoingCompanyId") String outgoingCompanyId,
            @QueryParam("incomingCompanyId") String incomingCompanyId,
            @QueryParam("inspectionType") int inspectionType,
            @QueryParam("inspectClass") String inspectClass,
            @QueryParam("productionLot") String productionLot,
            @QueryParam("inspectionDateStart") String inspectionDateStart,
            @QueryParam("inspectionDateEnd") String inspectionDateEnd,
            @QueryParam("n") int n,
            @QueryParam("a2") String a2,
            @QueryParam("d3") String d3,
            @QueryParam("d4") String d4,
            @QueryParam("drawingPage") String drawingPage,
            @QueryParam("drawingAnnotation") String drawingAnnotation,
            @QueryParam("drawingMentionNo") String drawingMentionNo,
            @QueryParam("similarMultiitem") String similarMultiitem,
            @QueryParam("cavityPrefix") String cavityPrefix,
            @QueryParam("cavityNum") int cavityNum
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentInspectionResultService.getXbarAndRChartInfo( component, outgoingCompanyId, incomingCompanyId, inspectionType, inspectClass, productionLot, inspectionDateStart, inspectionDateEnd, n, a2, d3, d4, drawingPage, drawingAnnotation, drawingMentionNo, similarMultiitem, cavityPrefix, cavityNum, loginUser);
    }
 
    /**
     * Get X-bar/R Chart Coefficients
     * @return
     */
    @GET
    @Path("xbarcoefficients")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ComponentInspectionResultInfo getXbarAndRChartCoefficients() {
        return tblComponentInspectionResultService.getXbarAndRChartCoefficients();
    }
    
    @POST
    @Path("/copy/{inspectionResultId}/{inspectionType}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse copyInpsectData(@PathParam("inspectionResultId") String resultId, @PathParam("inspectionType") Integer inspectType) {
        ObjectResponse<String> response = new ObjectResponse<>();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        String newResultId = this.tblComponentInspectionResultService.copyInpsectData(resultId, inspectType, loginUser);
        response.setObj(newResultId);
        return response;
    }
    
    @POST
    @Path("abort/{inspectionResultId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse abort(@PathParam("inspectionResultId") String resultId) {
        BasicResponse resp = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        this.tblComponentInspectionResultService.abort(Arrays.asList(resultId), AbortFlg.ABORTING, loginUser);
        return resp;
    }
    
    @GET
    @Path("extaborting")
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<List<String>> getExtAborting() {
        ObjectResponse<List<String>> resp = new ObjectResponse<>();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstApiUser mstApiUser = this.mstApiUserService.getApiMstUser(loginUser.getUserid());
        String incomingCompanyId = mstApiUser.getCompanyId();
        List<String> abortingIds = this.tblComponentInspectionResultService.getExtAborting(incomingCompanyId);
        resp.setObj(abortingIds);
        return resp;
    }
    
    @POST
    @Path("ackaborted")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse ackAborted(List<String> abortedIds) {
        BasicResponse resp = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstApiUser mstApiUser = this.mstApiUserService.getApiMstUser(loginUser.getUserid());
        if(mstApiUser == null) {
            resp.setError(true);
            resp.setErrorCode(ErrorMessages.E101_INVALID_USERID_PWD);
            resp.setErrorMessage(ErrorMessages.MSG101_INVALID_USERID_PWD);
        } else if(abortedIds.size() > 0) {
            String incomingCompanyId = mstApiUser.getCompanyId();
            this.tblComponentInspectionResultService.ackAborted(abortedIds, incomingCompanyId);
        }
        return resp;
    }

    /**
     * 作成時にデータ数チェック
     * 
     * @param inputInfo
     * @return
     */
    private long checkInspectionCount(ComponentInspectionFormCreateInput inputInfo) {
        List<MstComponentInspectionItemsTableDetail> inspectionItemsTableDetails
                = this.mstComponentInspectionItemsTableService.getMstCompInspItemsTableDetails(inputInfo.getComponentInspectionItemsTableId());
        this.tblComponentInspectionResultService.getInspClass(inputInfo.getFirstFlag(), inputInfo.getIncomingCompanyId()).ifPresent((inspClass) -> {
            inputInfo.setMassFlg(Character.getNumericValue(inspClass.getMassFlg()));
        });
        int measureCount = 0;
        int visualCount = 0;
        if (null != inspectionItemsTableDetails && !inspectionItemsTableDetails.isEmpty()) {
            for (MstComponentInspectionItemsTableDetail itemsTableDetail : inspectionItemsTableDetails) {
                String inspectionTarget = "";
                if (CommonConstants.INSPECTION_TYPE_OUTGOING == inputInfo.getInspectionType()) {
                    if (null != inputInfo.getMassFlg()) {
                        switch (inputInfo.getMassFlg()) {
                            case CommonConstants.INSPECTION_INT_MASS_FLAG:
                                inspectionTarget = itemsTableDetail.getOutgoingTrialInspectionObject().toString();
                                break;
                            case CommonConstants.INSPECTION_PRODUCTION_MASS_FLAG:
                                inspectionTarget = itemsTableDetail.getOutgoingProductionInspectionObject().toString();
                                break;
                            case CommonConstants.INSPECTION_FIRST_MASS_PRODUCTION_FLAG:
                                inspectionTarget = itemsTableDetail.getOutgoingFirstMassProductionObject().toString();
                                break;
                            default:
                                inspectionTarget = itemsTableDetail.getProcessInspetionObject().toString();
                                break;
                        }
                    }
                } else if (null != inputInfo.getMassFlg()) {
                    switch (inputInfo.getMassFlg()) {
                        case CommonConstants.INSPECTION_INT_MASS_FLAG:
                            inspectionTarget = itemsTableDetail.getIncomingTrialInspectionObject().toString();
                            break;
                        case CommonConstants.INSPECTION_PRODUCTION_MASS_FLAG:
                            inspectionTarget = itemsTableDetail.getIncomingProductionInspectionObject().toString();
                            break;
                        case CommonConstants.INSPECTION_FIRST_MASS_PRODUCTION_FLAG:
                            inspectionTarget = itemsTableDetail.getIncomingFirstMassProductionObject().toString();
                            break;
                        default:
                            inspectionTarget = itemsTableDetail.getProcessInspetionObject().toString();
                            break;
                    }
                }
                if (CommonConstants.INSPECTION_TARGET_YES.equals(inspectionTarget)) {
                    if (CommonConstants.MEASUREMENT_TYPE_MEASURE == itemsTableDetail.getMeasurementType()) {
                        measureCount++;
                    } else if (CommonConstants.MEASUREMENT_TYPE_VISUAL == itemsTableDetail.getMeasurementType()) {
                        visualCount++;
                    }
                }
            }
        }
        CnfSystem cnf = cnfSystemService.findByKey("system", "max_insp_detail_cnt");
        long sysCount = Long.parseLong(cnf.getConfigValue());
        int measSamplingQuantity = 0;
        int VisualSamplingQuantity = 0;
        if (CommonConstants.INSPECTION_TYPE_OUTGOING == inputInfo.getInspectionType()) {
            measSamplingQuantity = inputInfo.getOutgoingMeasSamplingQuantity();
            VisualSamplingQuantity = inputInfo.getOutgoingVisualSamplingQuantity();
        }else{
            measSamplingQuantity = inputInfo.getIncomingMeasSamplingQuantity();
            VisualSamplingQuantity = inputInfo.getIncomingVisualSamplingQuantity();
        }
        if (measureCount * measSamplingQuantity * inputInfo.getCavityCnt() + visualCount * VisualSamplingQuantity * inputInfo.getCavityCnt() > sysCount) {
            return sysCount;
        }
        return 0;
    }
}
