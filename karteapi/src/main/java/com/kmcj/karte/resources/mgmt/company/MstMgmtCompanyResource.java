/*
 * To ch
nge this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mgmt.company;

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
@Path("mgmt/company")
public class MstMgmtCompanyResource {

    /**
     * Creates a new instance of MstMgmtCompanyResource
     */
    public MstMgmtCompanyResource() {
    }

    @Inject
    private MstMgmtCompanyService mstMgmtCompanyService;

    /**
     * 管理先コードで補助用
     *
     * @param mgmtCompanyCode
     * @param mgmtCompanyName
     * @return an instance of MstMgmtCompanyList
     */
    @GET
    @Path("like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMgmtCompanyList getLikeMstMgmtCompanyList(@QueryParam("mgmtCompanyCode") String mgmtCompanyCode,
            @QueryParam("mgmtCompanyName") String mgmtCompanyName) {
        return mstMgmtCompanyService.getMstMgmtCompanyList(mgmtCompanyCode, mgmtCompanyName, true);
    }

    /**
     * 管理先コードを決めたとき
     *
     * @param mgmtCompanyCode
     * @param mgmtCompanyName
     * @return an instance of MstMgmtCompanyList
     */
    @GET
    @Path("equal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMgmtCompanyList getEqualMstMgmtCompanyList(@QueryParam("mgmtCompanyCode") String mgmtCompanyCode,
            @QueryParam("mgmtCompanyName") String mgmtCompanyName) {
        return mstMgmtCompanyService.getMstMgmtCompanyList(mgmtCompanyCode, mgmtCompanyName, false);
    }

}
