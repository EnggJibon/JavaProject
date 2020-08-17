/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.order;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@Path("moldpartorder")
@RequestScoped
public class MoldPartOrderResource {

    @Context
    private UriInfo context;
    @Context
    private ContainerRequestContext requestContext;
    @Inject
    private MoldPartOrderService moldPartOrderService;

    /**
     * Creates a new instance of MoldPartOrderResource
     */
    public MoldPartOrderResource() {
    }

    @POST
    //@Path("order")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldPartStockOrder(MoldPartOrderList moldPartOrderList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return moldPartOrderService.upsertMoldPartOrders(moldPartOrderList, loginUser);
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteMoldPartStockOrder(MoldPartOrderList moldPartOrderList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return moldPartOrderService.deleteMoldPartOrders(moldPartOrderList, loginUser);
    }

    /**
     * Retrieves representation of an instance of com.kmcj.karte.resources.mold.part.order.MoldPartOrderResource
     * @param department
     * @param moldId
     * @param moldPartCode
     * @param storageCode
     * @param orderJobNo
     * @param orderedByMe
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MoldPartOrderList getMoldPartOrder(
        @QueryParam("department") int department,
        @QueryParam("moldId") String moldId,
        @QueryParam("moldPartCode") String moldPartCode,
        @QueryParam("storageCode") String storageCode,
        @QueryParam("orderJobNo") String orderJobNo,
        @QueryParam("orderedByMe") boolean orderedByMe,
        @QueryParam("receivedFlg") int receivedFlg
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return moldPartOrderService.getMoldPartOrders(department, moldId, moldPartCode, storageCode, orderJobNo, orderedByMe, receivedFlg, loginUser);
    }

    @GET
    @Path("download/excel")
    @Produces("application/octet-stream;charset=UTF-8")
    public Response downloadMoldPartOrder(
        @QueryParam("department") int department,
        @QueryParam("moldId") String moldId,
        @QueryParam("moldPartCode") String moldPartCode,
        @QueryParam("storageCode") String storageCode,
        @QueryParam("orderJobNo") String orderJobNo,
        @QueryParam("orderedByMe") boolean orderedByMe,
        @QueryParam("receivedFlg") int receivedFlg
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        Workbook wb = moldPartOrderService.createMoldPartOrderExcel(department, moldId, moldPartCode, storageCode, orderJobNo, orderedByMe, receivedFlg, loginUser);
        StreamingOutput streamout = outputStream -> wb.write(outputStream);
        Response.ResponseBuilder response = Response.ok(streamout);
        String fileName = moldPartOrderService.getExcelFileName(loginUser);
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException ex) {throw new RuntimeException(ex);}
        response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xlsx\"");
        return response.build();
    }

    
    /**
     * PUT method for updating or creating an instance of MoldPartOrderResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
