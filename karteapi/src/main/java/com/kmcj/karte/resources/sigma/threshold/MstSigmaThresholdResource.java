/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.threshold;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jiangxs
 */
@RequestScoped
@Path("threshold")
public class MstSigmaThresholdResource {
    
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstSigmaThresholdService mstSigmaThresholdService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    
    public MstSigmaThresholdResource(){
        
    }
    
    /**
     * M1103 設備ログ閾値設定
     * 部品データ取得
     * @param machineId
     * @return 
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstSigmaThresholdList getMstMachineThreshold(@QueryParam("machineId")String machineId){
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstSigmaThresholdList response = new MstSigmaThresholdList();
        response = mstSigmaThresholdService.getMstMachineThreshold(machineId,loginUser);
        return response;
    }
    
    /**
     * M1103 設備ログ閾値設定
     * データ取得
     * @param machineId
     * @param componentId
     * @return 
     */
    @GET
    @Path("getthreshold")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstSigmaThresholdList getMstMachineThresholds(@QueryParam("machineId")String machineId,@QueryParam("componentId")String componentId){
        MstSigmaThresholdList response = new MstSigmaThresholdList();
        response = mstSigmaThresholdService.getMstMachineThresholds(machineId,componentId);
        return response;
    }
    
    
    /**
     * M1103 設備ログ閾値設定
     * 画面で入力された値を用いて設備ログ閾値設定テーブルへ追加・更新を行う。
     * @param mstSigmaThresholdList
     * @return 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMstMachineThreshold(MstSigmaThresholdList mstSigmaThresholdList){
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        response = mstSigmaThresholdService.postMstMachineThreshold(mstSigmaThresholdList,loginUser);
        return response;
    }
     /**
     *
     * @param machineId
     * @return 
     */
    @GET
    @Path("exportexcel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMstSigmaThresholdExcel(@QueryParam("machineId")String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
         FileReponse response = mstSigmaThresholdService.getMstSigmaThresholdOutputExcel(machineId, loginUser);
        return response;
    }
	/**
     *
     * @param fileUuid
     * @param machineId
     * @return
     */
    @POST
    @Path("importexcel/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postMstSigmaThresholdExcel(@PathParam("fileUuid") String fileUuid, @QueryParam("machineId")String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        try {
            return mstSigmaThresholdService.postSigmaThresholdExcel(fileUuid, machineId, loginUser);
        } catch (Exception ex) {
            ImportResultResponse importResultResponse = new ImportResultResponse();
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            importResultResponse.setErrorMessage(
            mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_upload_file_type_invalid"));
            return importResultResponse;
        }
    }
}


