package com.kmcj.karte.resources.circuitboard.productionline.machine;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by xiaozhou.wang on 2017/08/09.
 * Updated by MinhDTB
 */
@Path("productionlinemachine")
@RequestScoped
public class MstProductionLineMachineResource {

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstProductionLineMachineService mstProductionLineMachineService;

    @GET
    @Path("{productionLineId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstProductionLineMachineList getMstProductionLineMachineList(@PathParam("productionLineId") String productionLineId) {
        return mstProductionLineMachineService.getMstProductionLineMachineList(productionLineId);
    }
}
