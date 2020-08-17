package com.kmcj.karte.resources.machine.maintenance.recomend;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("machine/maintenance/recomend")
public class TblMachineMaintenanceRecomendResource {

    @Inject
    private TblMachineMaintenanceRecomendService tblMachineMaintenanceRecomendService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * 設備メンテ開始候補リスト取得
     * @return 
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceRecomendList getMachineMaintenanceRecommendList() {
        return tblMachineMaintenanceRecomendService.getMachineMaintenanceRecommendList();
    }
}
