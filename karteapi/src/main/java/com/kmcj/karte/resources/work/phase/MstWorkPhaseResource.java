/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work.phase;

import com.kmcj.karte.constants.RequestParameters;
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
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.ws.rs.PathParam;

/**
 * 作業工程マスタリソース
 * @author t.ariki
 */
@RequestScoped
@Path("work/phase")
public class MstWorkPhaseResource {
    
    @Context
    private UriInfo context;
    
    @Context
    private ContainerRequestContext requestContext;
   
    @Inject
    private MstWorkPhaseService mstWorkPhaseService;

    public MstWorkPhaseResource() {}
    
    /**
     * 作業工程マスタ取得
     * @return 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MstWorkPhaseList getWorkPhases() {
        return mstWorkPhaseService.getMstWorkPhases();
    }

    /**
     * 作業工程マスタ取得
     * @return 
     */
    @GET
    @Path("next/{workPhaseId}")
    @Produces(MediaType.APPLICATION_JSON)
    public MstWorkPhaseList getWorkPhases(@PathParam("workPhaseId") String workPhaseId) {
        return mstWorkPhaseService.getNextMstWorkPhases(workPhaseId);
    }
    
    /**
     * 作業工程マスタ反映
     * @param mstWorkPhaseList
     * @return 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstWorkPhaseList replaceWorkPhases(MstWorkPhaseList mstWorkPhaseList) {
        //System.out.println("NEXT WORK PHASE IDs:" + mstWorkPhaseList.getMstWorkPhases().get(0).getNextWorkPhaseIds().get(0));
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstWorkPhaseService.replaceWorkPhases(mstWorkPhaseList, loginUser);
    }
}
