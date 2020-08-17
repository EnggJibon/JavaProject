package com.kmcj.karte.resources.circuitboard.productionline;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.IndexsResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.circuitboard.productionline.machine.MstProductionLineMachineList;
import com.kmcj.karte.resources.machine.MstMachineList;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by xiaozhou.wang on 2017/08/09.
 * Updated by MinhDTB on 2018/03/15
 */
@Path("productionline")
@RequestScoped
public class MstProductionLineResource {

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstProductionLineService mstProductionLineService;

    public MstProductionLineResource() {
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstProductionLineList getMstProductionLineList(@QueryParam("productionLineName") String productionLineName) {
        return mstProductionLineService.getMstProductionLineList(productionLineName);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) 
    public BasicResponse updateMstProductionLines(List<MstProductionLine> mstProductionLines) {
//    public IndexsResponse<MstProductionLine> updateMstProductionLines(List<MstProductionLine> mstProductionLines) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstProductionLineService.updateMstProductionLines(mstProductionLines, loginUser);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse addMstProductionLines(List<MstProductionLine> mstProductionLines) {
//    public IndexsResponse<MstProductionLine> addMstProductionLines(List<MstProductionLine> mstProductionLines) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstProductionLineService.addMstProductionLines(mstProductionLines, loginUser);
    }

    @DELETE
    @Path("{productionLineId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteMstProductionLine(@PathParam("productionLineId") String productionLineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstProductionLineService.deleteProductionLine(productionLineId, loginUser);
    }

    @GET
    @Path("data")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstProductionLineData getLocations() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstProductionLineService.getData(loginUser);
    }

    @GET
    @Path("machine")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMachineList getMstMachineList(@QueryParam("machineName") String machineName) {
        return mstProductionLineService.getMstMachineList(machineName);
    }
}
