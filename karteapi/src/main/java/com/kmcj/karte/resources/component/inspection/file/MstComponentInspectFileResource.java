/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.file;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.apiuser.MstApiUser;
import com.kmcj.karte.resources.apiuser.MstApiUserService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.inspection.file.model.MstComponentInspectClassList;
import com.kmcj.karte.resources.component.inspection.file.model.MstComponentInspectFileList;
import com.kmcj.karte.resources.component.inspection.file.model.MstComponentInspectTypeList;
import com.kmcj.karte.resources.component.inspection.file.model.MstFileSettingsForBat;
import com.kmcj.karte.resources.component.inspection.item.model.MstComponentInspectItemsTableClassList;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Apeng
 */
@RequestScoped
@Path("component/inspection/file")
public class MstComponentInspectFileResource {

    /**
     * Creates a new instance of MstComponentInspectResource
     */
    public MstComponentInspectFileResource() {
    }

    @Inject
    private MstComponentInspectFileService mstComponentInspectService;

    @Inject
    private MstApiUserService mstApiUserService;
    
    @Context
    private ContainerRequestContext requestContext;

    /**
     * 検査ファイル種類設定取得
     *
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentInspectFileList getMstComponentInspectFilelist() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentInspectService.getMstComponentInspectFileVoList(loginUser.getLangId());
    }
    
    /**
     * 部品業種取得
     * @return 
     */
    @GET
    @Path("type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentInspectTypeList getMstComponentInspectTypelist() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentInspectService.getMstComponentInspectTypelist(loginUser.getLangId());
    }

    /**
     * 検査区分編集取得
     *
     * @return
     */
    @GET
    @Path("class")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentInspectClassList getMstComponentInspectClasslist() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentInspectService.getMstComponentInspectClassList(loginUser.getLangId());
    }
    
    /**
     * 検査管理項目に対する検査区分及び、検査区分毎のエビデンスファイル取得
     * 
     * @param componentId
     * @param componentCode
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @return 
     */
    @GET
    @Path("/table/class")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentInspectItemsTableClassList getMstComponentInspectionItemsTableClassList(
            @QueryParam("componentId") String componentId,
            @QueryParam("componentCode") String componentCode,
            @QueryParam("outgoingCompanyId") String outgoingCompanyId,//出荷先
            @QueryParam("incomingCompanyId") String incomingCompanyId// 納品先
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentInspectService.getMstComponentInspectionItemsTableClassList(componentId,componentCode,outgoingCompanyId,incomingCompanyId,loginUser);
    }

    /**
     * 検査区分編集保存
     *
     * @param mstComponentInspectClassList
     * @return
     */
    @PUT
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentInspectClassList saveMstComponentInspectClass(MstComponentInspectClassList mstComponentInspectClassList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentInspectService.saveMstComponentInspectClass(mstComponentInspectClassList, loginUser);
    }

    /**
     * 編集した検査ファイル設定を登録します
     *
     * @param mstComponentInspectFileList
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentInspectFileList enterMstComponentInspect(MstComponentInspectFileList mstComponentInspectFileList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentInspectService.enterMstComponentInspectFile(mstComponentInspectFileList, loginUser);
    }

    /**
     * Get all title file name
     * @return
     */
    @GET
    @Path("/name/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<MstComponentInspectFileName> getMstCircuitBoardProcedureAll() {
        return mstComponentInspectService.getMstInspectFileName();
    }

    /**
     * Update title file name
     * @param inspectFileName
     * @return
     */
    @POST
    @Path("/name/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateSubstrateProcedure(MstComponentInspectFileName inspectFileName) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        this.mstComponentInspectService.updateInspectFileName(inspectFileName, loginUser);
        return response;
    }
    
    /**
     * データ連携バッチから、検査ファイル種類設定系のデータを受け取ります。
     * @param fileSettings
     * @return 
     */
    @POST
    @Path("/extdata/push")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse extPushFileSettings(MstFileSettingsForBat fileSettings) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstApiUser mstApiUser = this.mstApiUserService.getApiMstUser(loginUser.getUserid());
        String ownerCompanyId = mstApiUser.getCompanyId();
        BasicResponse response = new BasicResponse();
        try {
            mstComponentInspectService.extPushFileSettings(fileSettings, ownerCompanyId, loginUser.getUserUuid());
        } catch(Exception e) {
            Logger.getLogger(MstComponentInspectFileResource.class.getName()).log(Level.SEVERE, null, e);
            response.setError(true);
            response.setErrorCode(ErrorMessages.E901_OTHER);
            response.setErrorMessage(ErrorMessages.MSG901_OTHER);
        }
        
        return response;
    }
}
