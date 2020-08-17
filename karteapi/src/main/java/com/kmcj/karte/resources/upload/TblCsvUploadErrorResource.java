/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.upload;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author admin
 */
@RequestScoped
@Path("csv/upload")
public class TblCsvUploadErrorResource {

    @Inject
    private TblCsvUploadErrorService tblCsvUploadErrorService;

    @Context
    private ContainerRequestContext requestContext;
    
    /**
     *
     * @param tblCsvUploadError
     * @return
     */
    @POST
    @Path("error")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse insertCsvUploadError(TblCsvUploadError tblCsvUploadError) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = tblCsvUploadErrorService.createCsvUploadError(tblCsvUploadError, loginUser.getUserUuid());
        return response;
    }

    /**
     *
     * @param fileUuid
     * @param logFileUuid
     * @param errorType
     * @param errorHttpStatusCode
     * @param processType
     * @param intervals
     * @return
     */
    @POST
    @Path("error/report")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse getCsvUploadErrorByType(
            @QueryParam("fileUuid") String fileUuid,
            @QueryParam("logFileUuid") String logFileUuid,
            @QueryParam("errorType") int errorType,
            @QueryParam("errorHttpStatusCode") int errorHttpStatusCode,
            @QueryParam("processType") String processType,
            @QueryParam("intervals") int intervals) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = tblCsvUploadErrorService.getCsvUploadErrorByType(fileUuid, logFileUuid, errorType, errorHttpStatusCode, processType, intervals, loginUser);
        return response;
    }

    /**
     * @param processType
     * @return
     */
    @POST
    @Path("error/solved/report")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse getCsvUploadErroSolvedByType(@QueryParam("processType") String processType) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = tblCsvUploadErrorService.getCsvUploadErroSolvedByType(processType, loginUser);
        return response;
    }

}
