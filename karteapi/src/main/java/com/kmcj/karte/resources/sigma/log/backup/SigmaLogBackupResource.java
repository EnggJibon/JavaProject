/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.log.backup;

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
import javax.ws.rs.core.UriInfo;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import javax.ws.rs.QueryParam;
import com.kmcj.karte.resources.authentication.LoginUser;

/**
 *
 * @author c.darvin
 */
@RequestScoped
@Path("/sigma/log/backup")
public class SigmaLogBackupResource {
    
    @Context
    private UriInfo context;

    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private SigmaLogBackupService sigmaLogBackupService;
    
    public SigmaLogBackupResource(){   
    }
    
    @GET
    @Path("/isrequested")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String isRequested(@QueryParam("gunshiId") String gunshiId) {
        
        if (sigmaLogBackupService.isRequested(gunshiId)) {
            return "true";
        } else {
            return "false";
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse sigmaLogBackup(TblSigmaLogBackup tblSigmaLogBackup){
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        try {
            sigmaLogBackupService.sigmaLogBackup(tblSigmaLogBackup, loginUser);
        }
        catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
    
    @POST
    @Path("/available")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse backupAvailable (TblSigmaLogBackupList sigmaLogBackupList){
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        try {
            sigmaLogBackupService.backupAvailable(sigmaLogBackupList, loginUser);
        }
        catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
    
    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse backupList (TblSigmaLogBackupList sigmaLogBackupList){
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        try {
            sigmaLogBackupService.backupList(sigmaLogBackupList, loginUser);
        }
        catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
    
    
    @GET
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblSigmaLogBackupList getList(@QueryParam("gunshiId") String gunshiId){
        
        return sigmaLogBackupService.getList(gunshiId);
    }
    
    @GET
    @Path("/requests")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblSigmaLogBackupList getRequests(@QueryParam("gunshiId") String gunshiId){
        
        return sigmaLogBackupService.getRequests(gunshiId);
    }
}
