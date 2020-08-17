/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.fileimportjob;

import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author kmc
 */
@RequestScoped
@Path("fileimportjob")
public class FileImportJobResource {
    
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private TblUploadFileService tblUploadFileService;

    /**
     * Creates a new instance of MstUserResource
     */
    public FileImportJobResource() {
    }
    
    /**
     * すべてのCSVアップロードファイルを取得する
     * @return an instance of MstUserList
     */
    @GET
    @Path("uploadfile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblUploadFileList getUploadFile() {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblUploadFileService.getByFileTypeTblUploadFile("csv", loginUser.getLangId());
    }

        
    @POST
    @Path("searchuploaddate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TblUploadFileList getSearchUploadDate(String uploadDate) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblUploadFileService.getByFileTypeUploadDateTblUploadFile(loginUser.getLangId(), uploadDate, "csv");
    }

}
