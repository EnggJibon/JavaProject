/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2.bulk;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.machine.dailyreport2.bulk.models.BulkMDReport;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@RequestScoped
@Path("bulk/machdailyreport")
public class BulkMachDailyReportResource {

    @Context
    private UriInfo context;
    @Context
    private ContainerRequestContext requestContext;
    @Inject
    private BulkMachDailyReportService bulkMachDailyReportService;

    /**
     * Creates a new instance of BulkMachDailyReportResource
     */
    public BulkMachDailyReportResource() {
    }

    /**
     * Retrieves representation of an instance of com.kmcj.karte.resources.machine.dailyreport2.bulk.BulkMachDailyReportResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postJson(BulkMDReport report) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        try {
            return bulkMachDailyReportService.createMachineDailyReport(report, loginUser);
        }
        catch (Exception e) {
            BasicResponse response = new BasicResponse();
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return response;
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteMDReport(@QueryParam("reportDate") String reportDate, @QueryParam("machineId") String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        try {
            return bulkMachDailyReportService.deleteMachineDailyReport(reportDate, machineId, loginUser);
        }
        catch (Exception e) {
            BasicResponse response = new BasicResponse();
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
            e.printStackTrace();
            return response;
        }
    }
    
    /**
     * PUT method for updating or creating an instance of BulkMachDailyReportResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
