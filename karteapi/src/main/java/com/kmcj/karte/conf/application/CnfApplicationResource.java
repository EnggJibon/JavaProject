/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.conf.application;

import com.kmcj.karte.conf.application.CnfApplicationService;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
                
/**
 *
 * @author m.jibon
 */

@RequestScoped
@Path("cnfapplication")
public class CnfApplicationResource {   
          
    @Context
    private UriInfo context;
    
    @Inject 
    private CnfApplicationService cnfApplicationService;
    
    @Context
    private ContainerRequestContext requestContext;
    
    /**
     * Creates a new instance of CnfApplicationResource
     */
    public CnfApplicationResource() {
    }
   
    /**
    * Check button visibility in Mold Info screen
    * Retrieve all data from CnfApplication Table
    * @param configKey
    * @return 
    */
    @GET
    @Path("cnfkey")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    
    
    public CnfApplication getRecordByCnfApplicationKey(@QueryParam("configKey") String configKey) {        
       return cnfApplicationService.findByKey(configKey);
    }
    
}
