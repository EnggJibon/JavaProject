/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.detail;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;
import javax.ws.rs.QueryParam;

/**
 * 作業実績テーブルリソース
 *
 * @author t.ariki
 */
@RequestScoped
@Path("productiondetail")
public class TblProductionDetailResource {

    @Context
    private UriInfo context;

    @Context
    ContainerRequestContext requestContext;

    @Inject
    TblProductionDetailService tblProductionDetailService;

    private Logger logger = Logger.getLogger(TblProductionDetailResource.class.getName());

    public TblProductionDetailResource() {
    }

    /**
     * バッチで生産実績テーブルデータを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extproductiondetail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionDetailList getExtProductionDetailsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return tblProductionDetailService.getExtProductionDetailsByBatch(latestExecutedDate, moldUuid);
    }

    /**
     * バッチで設備生産実績テーブルデータを取得
     *
     * @param latestExecutedDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachineproductiondetail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionDetailList getExtMachineProductionDetailsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String machineUuid) {
        return tblProductionDetailService.getExtMachineProductionDetailsByBatch(latestExecutedDate, machineUuid);
    }

}
