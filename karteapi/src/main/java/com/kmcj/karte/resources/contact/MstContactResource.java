/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.contact;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Context;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("contact")
public class MstContactResource {

    public MstContactResource() {
    }

    @Inject
    private MstContactService mstContactService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * 担当者マスタ複数取得
     *
     * @param companyName
     * @param locationName
     * @param mgmtCompanyCode
     * @param contactName
     * @param companyId
     * @param locationId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstContactList getContactList(
            @QueryParam("companyName") String companyName,//会社名称
            @QueryParam("locationName") String locationName,//所在名称
            @QueryParam("mgmtCompanyCode") String mgmtCompanyCode,//管理先コード
            @QueryParam("contactName") String contactName,//担当者氏名
            @QueryParam("companyId") String companyId,
            @QueryParam("locationId") String locationId,
            
            @QueryParam("sidx") String sidx,//order Key
            @QueryParam("sord") String sord,//order 順
            
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstContactService.getContactList(companyName, locationName, mgmtCompanyCode, contactName, companyId, locationId, pageNumber, pageSize, false, loginUser, false, true, sord, sidx);
    }

    /**
     * 担当者マスタ詳細取得する
     *
     * @param uuid
     * @return
     */
    @GET
    @Path("detail/{contactId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstContactList getContactById(@PathParam("contactId") String uuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstContactService.getMstContactDetailByUuid(uuid, loginUser);
    }

    /**
     * 担当者マスタ情報を修正
     *
     * @param mstContactVo
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putContact(MstContactVo mstContactVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstContactService.updateMstContact(mstContactVo,loginUser);
    }

    /**
     * 担当者マスタ情報を追加
     *
     * @param mstContactVo
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstContactVo postContact(MstContactVo mstContactVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstContactService.createMstContact(mstContactVo, loginUser, "", 0);
    }

    /**
     * 担当者マスタ情報を削除
     *
     * @param uuid
     * @return
     */
    @DELETE
    @Path("{contactId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteContact(@PathParam("contactId") String uuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        return mstContactService.deleteContact(uuid,loginUser);
    }

    /**
     * 担当者マスタ情報をCSV出力
     *
     * @param companyName
     * @param locationName
     * @param mgmtCompanyCode
     * @param contactName
     * @param companyId
     * @param locationId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getContactCSV(
            @QueryParam("companyName") String companyName,
            @QueryParam("locationName") String locationName,
            @QueryParam("mgmtCompanyCode") String mgmtCompanyCode,
            @QueryParam("contactName") String contactName,
            @QueryParam("companyId") String companyId,
            @QueryParam("locationId") String locationId,
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstContactService.getContactCSV(companyName, locationName, mgmtCompanyCode, contactName, companyId, locationId, pageNumber, pageSize, loginUser);
    }

    /**
     * 担当者マスタ情報をCSV取込
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postContactCSV(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        try {
            return mstContactService.postContactCSV(fileUuid, loginUser);
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
