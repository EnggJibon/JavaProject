/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.conf;

import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
@Path("cnfsystem")
public class CnfSystemResource {

    @Context
    private UriInfo context;
    
    @Inject
    private CnfSystemService cnfSystemService;
    
    @Context
    private ContainerRequestContext requestContext;

    /**
     * Creates a new instance of CnfSystemResource
     */
    public CnfSystemResource() {
    }

    /**
     * Retrieves representation of an instance of com.kmcj.karte.conf.CnfSystemResource
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CnfSystemList getCnfSystemValues(List<String> params) {
        CnfSystemList response = new CnfSystemList();
        for (String cnfKey: params) {
            int commaIndex = cnfKey.indexOf(".");
            String configGroup = cnfKey.substring(0, commaIndex);
            String configKey = cnfKey.substring(commaIndex + 1);
            CnfSystem cnfSystem = cnfSystemService.findByKey(configGroup, configKey);
            response.getCnfSystems().add(cnfSystem);
        }
        return response; //params.get(0);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CnfSystemList getAllCnfSystem() {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        return cnfSystemService.getAllCnfSystem(loginUser);
    }

    @POST
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CnfSystemList updateCnfSystemValues(CnfSystemList cnfSystemList) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        cnfSystemList.setError(false);
        cnfSystemList.setErrorCode(null);
        cnfSystemList.setErrorMessage(null);
        try {
            cnfSystemService.updateCnfSystemValues(cnfSystemList, loginUser);
        }
        catch (Exception e) {
            cnfSystemList.setError(true);
            cnfSystemList.setErrorCode(ErrorMessages.E301_DATABASE);
            cnfSystemList.setErrorMessage(e.toString());
        }
        return cnfSystemList;
    }
    
    /**
     * PUT method for updating or creating an instance of CnfSystemResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
    
    /**
     * Σ軍師データ取得
     * DENSHIKARTEの部分はcnf_systeテーブルから取得
     * @param configGroup
     * @param configKey
     * @return 
     */
    @GET
    @Path("key")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CnfSystem getCnfSystemByPK(@QueryParam("configGroup") String configGroup, 
            @QueryParam("configKey") String configKey) {
        // 取得条件：config_group and config_keyで
        // config_valueを取得する
        return cnfSystemService.findByKey(configGroup,configKey);
    }
}
