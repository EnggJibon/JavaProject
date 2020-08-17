/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.thtransport;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 *
 * @author m.jibon
 */

@RequestScoped
@Path("thtransport")
public class TblProductionThsetResource {
    @Context
    private UriInfo context;

    @Context
    ContainerRequestContext requestContext;
    
    @Inject
    MstDictionaryService mstDictionaryService;
    
    @Inject
    TblProductionThsetService tblProductionThsetService;
    
    private Logger logger = Logger.getLogger(TblProductionThsetResource.class.getName());
    
    
    public TblProductionThsetResource(){
    }
    
    @GET
    @Path("productionStart")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblThProductionList getStartProduction(){
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        TblThProductionList response = tblProductionThsetService.getStartProduction(loginUser);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response ;  
    }
    
    @GET
    @Path("productionEnd")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblThProductionList getEndAndCancelProduction(){
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        TblThProductionList response = tblProductionThsetService.getEndAndCancelProduction(loginUser);
        return response ;  
    }
    
    @POST
    @Path("updateThStatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateThStatus(List<TblProductionThset> tblProductionThsetList ){
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        try {
            tblProductionThsetService.updateTrasportedThreshold(tblProductionThsetList, loginUser);
        } catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
    
    
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse createThThreshold (List<TblProductionThset> tblProductionThsetList){
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        
        try {
            tblProductionThsetService.creareThresholdTransport(tblProductionThsetList, loginUser);
        } catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage()); 
        }
        return response;
    }
        
}
