/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.imp;

import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@RequestScoped
@Path("sigmaimport")
public class SigmaImportResource {

    @Context
    private UriInfo context;
    
    @Inject
    private SigmaImportService sigmaImportService;
    @Context
    private ContainerRequestContext requestContext;

    /**
     * Creates a new instance of SigmaImportResource
     */
    public SigmaImportResource() {
    }

    /**
     * Retrieves representation of an instance of com.kmcj.karte.resources.sigma.imp.SigmaImportResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("shouldreporterror")
    @Produces(MediaType.APPLICATION_JSON)
    public String shouldReportError(@QueryParam("macKey") String macKey) {
        //TODO return proper representation object
        if (sigmaImportService.shouldReportError(macKey)) {
            return "true";
        }
        else {
            return "false";
        }
    }
    
    @POST
    @Path("error")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse sigmaError(@QueryParam("macKey") String macKey, @QueryParam("logFileUuid") String logFileUuid,
            @QueryParam("gunshiCsvFileUuid") String gunshiCsvFileUuid){
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = sigmaImportService.sigmaError(macKey, logFileUuid, gunshiCsvFileUuid, loginUser);//new BasicResponse();
//        try {
//            sigmaImportService.sigmaError(macKey, logFileUuid, gunshiCsvFileUuid, loginUser);
//        }
//        catch (Exception e) {
//            response.setError(true);
//            response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(e.getMessage());
//        }
        return response;
        //return sigmaImportService.sigmaError(macKey, logFileUuid, gunshiCsvFileUuid);
    }
    
    @POST
    @Path("success")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public BasicResponse recoveryMail(@QueryParam("macKey") String macKey){
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = sigmaImportService.recoveryMail(macKey,loginUser);
        return response;
    }
    
    /**
     * PUT method for updating or creating an instance of SigmaImportResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
