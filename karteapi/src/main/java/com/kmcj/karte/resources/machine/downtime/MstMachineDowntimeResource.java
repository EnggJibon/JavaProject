/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.downtime;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@RequestScoped
@Path("machine/downtime")
public class MstMachineDowntimeResource {

    @Context
    private UriInfo context;

    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstMachineDowntimeService mstMachineDowntimeService;

    /**
     * Creates a new instance of MstMachineDowntimeResource
     */
    public MstMachineDowntimeResource() {
    }

    /**
     * @return an instance of java.lang.String
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineDowntimeList getMstMachineDowntime() {
        MstMachineDowntimeList list = mstMachineDowntimeService.getMstMachineDowntimeList();
        return list;
    }

    /**
     * @param mstMachineDowntimeList representation for the resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineDowntimeList updateMstMachineDowntime(MstMachineDowntimeList mstMachineDowntimeList) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        mstMachineDowntimeService.updateMstMachineDowntime(mstMachineDowntimeList, loginUser);
        return mstMachineDowntimeList;
    }
}
