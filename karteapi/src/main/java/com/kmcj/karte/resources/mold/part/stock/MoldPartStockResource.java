/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.stock;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.ObjectResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.mold.part.changehistory.MPChangePrintHistoryService;
import com.kmcj.karte.util.FileUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author t.takasaki
 */
@RequestScoped
@Path("moldpartstock")
public class MoldPartStockResource {

    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MoldPartStockService moldPartStockService;
    
    @Inject
    private MPChangePrintHistoryService mpcPrintHistoryService;

    public MoldPartStockResource() {
    }
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MoldPartStockRes getMoldPartStock(@QueryParam("id") String id) {
        return moldPartStockService.getMoldPartStockById(id);
    }
    
    @GET
    @Path("find")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MoldPartStockRes getMoldPartStockByCompoundKey(@QueryParam("moldUuid") String moldUuid, @QueryParam("moldPartId") String moldPartId) {
        return moldPartStockService.getMoldPartStockByCompoundKey(moldUuid, moldPartId);
    }

    @GET
    @Path("pageview")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MoldPartStockList getMoldPartStockPageView(
            @QueryParam("department") int department,
            @QueryParam("moldId") String moldId,
            @QueryParam("moldPartCode") String moldPartCode,
            @QueryParam("storageCode") String storageCode,
            @QueryParam("statuses") String statuses, //Comma Separated Values
            @QueryParam("deleted") boolean deleted,
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize
    ) {
        //int[] iStatuses = null;
        List<Integer> iStatuses = new ArrayList<>();
        if (statuses != null && !statuses.equals("")) {
            String[] statusArray = statuses.split(",");
            for (int i = 0; i < statusArray.length; i++) {
                iStatuses.add(Integer.parseInt(statusArray[i]));
            }
        }
        return moldPartStockService.getMoldPartStockListForPageView(department, moldId, moldPartCode, storageCode, iStatuses, deleted, pageNumber, pageSize);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldPartStock(MoldPartStock moldPartStock) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return moldPartStockService.upsertMoldtPartStock(moldPartStock, loginUser);
    }
    
    @DELETE
    @Path("logicaldelete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse logicalDeleteMoldPartStock(@QueryParam("id") String id) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return moldPartStockService.logicalDeleteMoldtPartStock(id, loginUser);
    }
    
    @DELETE
    @Path("physicaldelete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse physicalDeleteMoldPartStock(@QueryParam("id") String id) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return moldPartStockService.physicalDeleteMoldtPartStock(id, loginUser);
    }
            
    @POST
    @Path("restore")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse restoreMoldPartStock(@QueryParam("id") String id) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return moldPartStockService.restoreMoldtPartStock(id, loginUser);
    }
    
    @GET
    @Path("orderrequired")
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<List<MoldPartStock>> getOrderReqList(@QueryParam("maintid") String maintid) {
        return new ObjectResponse<>(moldPartStockService.getOrderReqList(maintid));
    }

    @GET
    @Path("orderneeded")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MoldPartStockList getMoldPartStockOrderNeeded(
            @QueryParam("department") int department,
            @QueryParam("moldId") String moldId,
            @QueryParam("moldPartCode") String moldPartCode,
            @QueryParam("storageCode") String storageCode,
            @QueryParam("replacedByMe") boolean replacedByMe
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return moldPartStockService.getMoldPartStockOrderNeeded(department, moldId, moldPartCode, storageCode, replacedByMe, loginUser);
    }

    @GET
    @Path("orderneeded/excel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/octet-stream;charset=UTF-8")
    public Response downloadOrderNeededExcel(
            @QueryParam("department") int department,
            @QueryParam("moldId") String moldId,
            @QueryParam("moldPartCode") String moldPartCode,
            @QueryParam("storageCode") String storageCode,
            @QueryParam("replacedByMe") boolean replacedByMe
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        Workbook wb = moldPartStockService.createExcelOrderNeeded(department, moldId, moldPartCode, storageCode, replacedByMe, loginUser);
        StreamingOutput streamout = outputStream -> wb.write(outputStream);
        Response.ResponseBuilder response = Response.ok(streamout);
        String fileName = moldPartStockService.getOrderNeededExcelName(loginUser);
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException ex) {throw new RuntimeException(ex);}
        response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xlsx\"");
        return response.build();
    }
    
    @GET
    @Path("download/excel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/octet-stream;charset=UTF-8")
    public Response downloadExcel(
            @QueryParam("department") int department,
            @QueryParam("moldId") String moldId,
            @QueryParam("moldPartCode") String moldPartCode,
            @QueryParam("storageCode") String storageCode,
            @QueryParam("statuses") String statuses, //Comma Separated Values
            @QueryParam("deleted") boolean deleted
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        List<Integer> iStatuses = new ArrayList<>();
        if (statuses != null) {
            String[] statusArray = statuses.split(",");
            for (int i = 0; i < statusArray.length; i++) {
                iStatuses.add(Integer.parseInt(statusArray[i]));
            }
        }
        Workbook wb = moldPartStockService.createMoldPartStockExcel(department, moldId, moldPartCode, storageCode, iStatuses, deleted, loginUser);
        StreamingOutput streamout = outputStream -> wb.write(outputStream);
        Response.ResponseBuilder response = Response.ok(streamout);
        String fileName = moldPartStockService.getMoldPartStockExcelName(loginUser);
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException ex) {throw new RuntimeException(ex);}
        response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xlsx\"");
        return response.build();
    }
    
    @POST
    @Path("import/excel/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse importExcel(@PathParam("fileUuid") String fileUuid, @QueryParam("adjustReason") String adjustReason) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return moldPartStockService.importMoldPartStockExcel(fileUuid, adjustReason, loginUser);
    }

    
    @GET
    @Path("download/inventory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/octet-stream;charset=UTF-8")
    public Response downloadInventoryExcel(@QueryParam("department") Integer department, @QueryParam("isUsed") Integer isUsed) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        Workbook wb = moldPartStockService.createStockListExcel(department, isUsed == 1, loginUser.getLangId());
        String docName = moldPartStockService.getDocName("mold_part_stocktake_list", department, loginUser.getLangId());
        StreamingOutput streamout = outputStream -> wb.write(outputStream);
        Response.ResponseBuilder response = Response.ok(streamout);
        response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + docName + "\"");
        return response.build();
    }
    
    @GET
    @Path("download/changelist")
    public Response downloadChangeListExcel(@QueryParam("department") Integer department, @QueryParam("from") String fromStr, @QueryParam("to") String toStr) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        Date from = toDate(fromStr + CommonConstants.SYS_MIN_TIME).orElse(new Date(0L));
        Date to = toDate(toStr + CommonConstants.SYS_MAX_TIME).orElse(new Date(Long.MAX_VALUE));
        
        Workbook wb = moldPartStockService.createChangeListExcel(department, from, to, loginUser.getLangId());
        String docName = moldPartStockService.getDocName("mold_part_stock_change_list", department, loginUser.getLangId());
        mpcPrintHistoryService.addHistory(department, from, to, loginUser);
        StreamingOutput streamout = outputStream -> wb.write(outputStream);
        Response.ResponseBuilder response = Response.ok(streamout);
        response.header(CommonConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + docName + "\"");
        return response.build();
    }
    
    private Optional<Date> toDate(String str) {
        try {
            Date ret = FileUtil.parse(str);
            return ret == null ? Optional.empty() : Optional.of(ret);
        } catch (ParseException ex) {
            return Optional.empty();
        }
    }
}
