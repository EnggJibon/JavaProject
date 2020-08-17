/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.stock.inout;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author t.takasaki
 */
@RequestScoped
@Path("moldpartstockinout")
public class MoldPartStockInOutResource {

    @Context
    private ContainerRequestContext requestContext;
    @Inject
    private MoldPartStockInOutService moldPartStockInOutService;

    public MoldPartStockInOutResource() {
    }

    @GET
    @Path("pageview")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MoldPartStockInOutList getMoldPartStockInOutPageView(
            @QueryParam("moldPartStockId") String moldPartStockId,
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize
    ) {
       LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
       return moldPartStockInOutService.getMoldPartStockInOutListForPageView(moldPartStockId, pageNumber, pageSize, loginUser);
    }

    @POST
    @Path("adjust")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Make io record to adjust stock. 
     * Just send moldPartStockId, ioDate, adjustReason, newStockIo, usedStockIo
     */
    public BasicResponse adjustMoldPartStockIO(MoldPartStockInOut moldPartStockIO) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return moldPartStockInOutService.adjustMoldPartStockInOut(moldPartStockIO, loginUser);
    }

    @GET
    @Path("download/excel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/octet-stream;charset=UTF-8")
    public Response downloadExcel() {
        Response.ResponseBuilder response = Response.ok();
        
        return response.build();
    }
}
