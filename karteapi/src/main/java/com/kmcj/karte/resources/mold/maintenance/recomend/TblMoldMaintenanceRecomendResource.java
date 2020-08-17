package com.kmcj.karte.resources.mold.maintenance.recomend;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("mold/maintenance/recomend")
public class TblMoldMaintenanceRecomendResource {

    @Inject
    private TblMoldMaintenanceRecomendService tblMoldMaintenanceRecomendService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * 金型メンテ開始候補リスト取得
     * @return 
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceRecomendListVO getMoldMaintenanceRecommendList() {
        return tblMoldMaintenanceRecomendService.getMoldMaintenanceRecommendList();
    }
    
    /**
     * @param moldId
     * @param moldName
     * @param department
     * @return 
     */
    @GET
    @Path("part")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMoldPartMaintenanceRecommendList getRecommendMoldParts(@QueryParam("moldId")String moldId, 
            @QueryParam("moldName")String moldName, @QueryParam("department")int department){
        TblMoldMoldPartMaintenanceRecommendList tblMoldPartMaintRecommendList = new TblMoldMoldPartMaintenanceRecommendList();
        tblMoldPartMaintRecommendList = tblMoldMaintenanceRecomendService.getRecommendMoldParts(moldId, moldName, department);
        return tblMoldPartMaintRecommendList;
    }
    
}
