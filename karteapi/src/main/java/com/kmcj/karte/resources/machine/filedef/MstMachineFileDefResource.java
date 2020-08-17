/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.filedef;

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
@Path("machine/file/def")
public class MstMachineFileDefResource {
    
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstMachineFileDefService mstMachineFileDefService;
    
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    public MstMachineFileDefResource(){
        
    }
    
    
    /**
     * M1102 設備ログ項目設定
     * データ取得
     * @param machineId
     * @return 
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineFileDefList getMstMachineFileDef(@QueryParam("machineId")String machineId){
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMachineFileDefList mstMachineFileDefList = new MstMachineFileDefList();
        mstMachineFileDefList = mstMachineFileDefService.getMstMachineFileDef(machineId,loginUser);
        return mstMachineFileDefList;
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
    public FileReponse getMstMachineFileDefExcel(@QueryParam("machineId") String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
         FileReponse response = mstMachineFileDefService.getMachineFileDefOutputExcel(machineId, loginUser);
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
    public ImportResultResponse postMachineFileDefInputExcel(@PathParam("fileUuid") String fileUuid, @QueryParam("machineId") String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        try {
            return mstMachineFileDefService.postMachineFileDefInputExcel(fileUuid, machineId, loginUser);
        } catch (Exception ex) {
            ImportResultResponse importResultResponse = new ImportResultResponse();
            importResultResponse.setError(true);
            importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            importResultResponse.setErrorMessage(
            mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_upload_file_type_invalid"));
            return importResultResponse;
        }
    }
    /**
     * M1102 設備ログ項目設定
     * 画面で入力された値を用いて設備ログ項目設定テーブルへ更新を行う。
     * @param mstMachineFileDefList
     * @return 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMstMachineFileDef(MstMachineFileDefList mstMachineFileDefList){
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        response = mstMachineFileDefService.postMstMachineFileDef(mstMachineFileDefList,loginUser);
        return response;
    }
    
}
