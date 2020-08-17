/*
 * To ch
nge this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mgmt.location;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("mgmt/location")
public class MstMgmtLocationResource {

    /**
     * Creates a new instance of MstMgmtLocationResource
     */
    public MstMgmtLocationResource() {
    }

    @Inject
    private MstMgmtLocationService mstMgmtLocationService;

    /**
     * 設置先コードで補助用
     *
     * @param mgmtLocationCode
     * @param mgmtLocationName
     * @return an instance of MstMgmtLocationList
     */
    @GET
    @Path("like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMgmtLocationList getLikeMstMgmtLocationList(@QueryParam("mgmtLocationCode") String mgmtLocationCode,
            @QueryParam("mgmtLocationName") String mgmtLocationName) {
        return mstMgmtLocationService.getMstMgmtLocationList(mgmtLocationCode, mgmtLocationName, true);
    }

    /**
     * 設置先コードを決めたとき
     *
     * @param mgmtLocationCode
     * @param mgmtLocationName
     * @return an instance of MstMgmtLocationList
     */
    @GET
    @Path("equal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMgmtLocationList getEqualMstMgmtLocationList(@QueryParam("mgmtLocationCode") String mgmtLocationCode,
            @QueryParam("mgmtLocationName") String mgmtLocationName) {
        return mstMgmtLocationService.getMstMgmtLocationList(mgmtLocationCode, mgmtLocationName, true);
    }

}
