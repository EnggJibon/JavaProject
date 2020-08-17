/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.mold.production.TblMoldProductionForDay;
import com.kmcj.karte.resources.mold.production.TblMoldProductionPeriodService;
import com.kmcj.karte.resources.production.detail.TblProductionDetailList;
import com.kmcj.karte.resources.production2.Production;
import com.kmcj.karte.util.DateFormat;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.resources.component.structure.MstComponentStructureVoList;
import com.kmcj.karte.resources.machine.dailyreport2.stockupdate.ComponentStructureList;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@RequestScoped
@Path("machine/dailyreport2")
public class MachineDailyReport2Resource {

    @Context
    private UriInfo context;
    @Context
    private ContainerRequestContext requestContext;
    @Inject
    private MachineDailyReport2Service machineDailyReport2Service; 

    /**
     * Creates a new instance of MachineDailyReport2Resource
     */
    public MachineDailyReport2Resource() {
    }

    /**
     * Retrieves representation of an instance of com.kmcj.karte.resources.machine.dailyreport2.MachineDailyReport2Resource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MachineDailyReport2List getDailyReport2(@QueryParam("reportDate") String reportDate, @QueryParam("department") String department, @QueryParam("machineUuid") String machineUuid) {
        //TODO return proper representation object
        return machineDailyReport2Service.getMachineDailyReport2(reportDate, department, machineUuid);
    }
    
    /**
     * 機械日報2Web一覧V2.0用
     * 
     * @param reportDate
     * @param department
     * @return 
     */
    @GET
    @Path("v2/list")
    @Produces(MediaType.APPLICATION_JSON)
    public MachineDailyReport2List getDailyReport2ForV2(@QueryParam("reportDate") String reportDate, @QueryParam("department") String department) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return machineDailyReport2Service.getMachineDailyReport2ForV2(reportDate, department, loginUser.getLangId());
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("collectdepartment")
    public MachineDailyReport2List collectDepartmentMachineDailyReport2(@QueryParam("reportDate") String reportDate, @QueryParam("department") String department) {
        //TODO return proper representation object
        return machineDailyReport2Service.collectDepartmentMachineDailyReport2(reportDate, department);
    }
    
    @GET
    @Path("exportexcel")
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public FileReponse getDailyReportExcel(@QueryParam("reportDate") String reportDate, @QueryParam("department") String department
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        FileReponse response = machineDailyReport2Service.getDailyReportOutputExcel(reportDate, department, loginUser);
        return response;
    }
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("collect")
    public MachineDailyReport2Res collectDailyReport2(@QueryParam("reportDate") String reportDate, @QueryParam("machineUuid") String machineUuid) {
        //TODO return proper representation object
        return machineDailyReport2Service.collectMachineDailyReport2(reportDate, machineUuid);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("componentstructures")
    public MstComponentStructureVoList getComponentStructures(@QueryParam("componentId") String componentId, @QueryParam("procedureId") String procedureId) {
        //TODO return proper representation object
        return machineDailyReport2Service.getComponentStructures(componentId, procedureId);
    }


    /**
     * PUT method for updating or creating an instance of MachineDailyReport2Resource
     * @param content representation for the resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public BasicResponse updateDailyReport2(TblMachineDailyReport2 report) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        try {
            machineDailyReport2Service.updateMachienDailyReport2(report, loginUser);
        }
        catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
    
    /**
     *　停止時間を削除する
     * @param detailId
     * @return 
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("deletedowntime/{detailId}")
    public BasicResponse deleteDownTime(@PathParam("detailId") String detailId) {
        BasicResponse response = new BasicResponse();
        try {
            machineDailyReport2Service.deleteDetailDownTime(detailId);
        } catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("checkprevdays")
    public String checkPreviousDays(@QueryParam("reportDate") String reportDate, @QueryParam("machineUuid") String machineUuid) {
        return machineDailyReport2Service.checkPreviousDays(reportDate, machineUuid);
    }

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("moldproductionday")
    public TblMoldProductionForDay getProduction(@QueryParam("moldUuid") String moldUuid,
            @QueryParam("componentId") String componentId, @QueryParam("date") String date) {
        //TODO return proper representation object
        //return "{Mold:Hello, Production:Day}";
        java.util.Date pDate = DateFormat.strToDate(date);
        return machineDailyReport2Service.getTblMoldProductionForDay(moldUuid, componentId, pDate);
    }
    
}
