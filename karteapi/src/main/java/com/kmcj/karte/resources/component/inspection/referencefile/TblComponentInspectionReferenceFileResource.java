/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.referencefile;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.inspection.referencefile.model.ComponentInspectionReferenceFile;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Component inspection reference file resource
 * 
 * @author duanlin
 */
@RequestScoped
@Path("component/inspection/referencefile")
public class TblComponentInspectionReferenceFileResource {
    @Inject
    private TblComponentInspectionReferenceFileService tblComponentInspectionReferenceFileService;
    @Context
    private ContainerRequestContext requestContext;
    
    /**
     * Get newest reference files
     * 
     * @param componentId
     * @return 
     */    
    @GET
    @Path("/{componentId}")
    @Produces(MediaType.APPLICATION_JSON)   
    public ComponentInspectionReferenceFile getNewestReferenceFiles(@PathParam("componentId") String componentId) {
        return this.tblComponentInspectionReferenceFileService.getNewestReferenceFile(componentId);
    }
    
    /**
     * Get reference files
     * 
     * @param componentInspectionResultId
     * @param componentId
     * @return 
     */    
    @GET
    @Path("/{componentId}/{componentInspectionResultId}")
    @Produces(MediaType.APPLICATION_JSON)   
    public ComponentInspectionReferenceFile getReferenceFiles(@PathParam("componentId") String componentId, 
            @PathParam("componentInspectionResultId") String componentInspectionResultId) {
        return this.tblComponentInspectionReferenceFileService.getReferenceFile(componentInspectionResultId, componentId);
    }
    
    @POST
    @Path("/{componentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)   
    public BasicResponse update (@PathParam("componentId") String componentId, ComponentInspectionReferenceFile input) { 
        BasicResponse response = new BasicResponse();
        
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        input.setComponentId(componentId);
        this.tblComponentInspectionReferenceFileService.updateFileNewest(input, loginUser.getUserUuid());
        
        return response;
    }
    
}
