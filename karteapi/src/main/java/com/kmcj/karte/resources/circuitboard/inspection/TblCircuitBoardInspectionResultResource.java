/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.circuitboard.inspectorManhours.TblInspectorManhoursResouce;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author h.ishihara
 */
@RequestScoped
@Path("inspectionResult")
public class TblCircuitBoardInspectionResultResource {
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private TblCircuitBoardInspectionResultService tblCircuitBoardInspectionResultService;
    
    public TblCircuitBoardInspectionResultResource(){        
    }
    
        /**
     *
     * @param inspectorId
     * @param componentId
     * @param procedureId
     * @param inspectionDateStr
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspectionResult getInspectionResults(
                                                        @QueryParam("inspectorId") String inspectorId,
                                                        @QueryParam("componentId") String componentId,
                                                        @QueryParam("procedureId") String procedureId,
                                                        @QueryParam("inspectionDate") String inspectionDateStr
    )
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);       
    
        Date inspectionDate = null;
        try {
            inspectionDate = sdf.parse(inspectionDateStr);
        } catch (ParseException ex) {
            Logger.getLogger(TblInspectorManhoursResouce.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        InspectionResult list = this.tblCircuitBoardInspectionResultService.getInspectionResultsByCriteria(inspectorId, componentId, procedureId, inspectionDate);
        
        return list;
    }
    
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspectionResult searchInspectionResults(
                                                        @QueryParam("inspectorId") String inspectorId,
                                                        @QueryParam("inspectionDate") String inspectionDateStr,
                                                        @QueryParam("componentId") String componentId,
                                                        @QueryParam("procedureId") String procedureId
    )
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);       
    
        Date inspectionDate = null;
        try {
            inspectionDate = sdf.parse(inspectionDateStr);
        } catch (ParseException ex) {
            Logger.getLogger(TblInspectorManhoursResouce.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        InspectionResult list = this.tblCircuitBoardInspectionResultService.searchInspectionResultsByCriteria(inspectorId, procedureId, componentId, inspectionDate);
        
        return list;
    }
    
    @GET
    @Path("searchSerialNumber")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspectionResult searchInspectionResultsBySerialNumber(
                                                        @QueryParam("inspectorId") String inspectorId,
                                                        @QueryParam("inspectionDate") String inspectionDateStr,
                                                        @QueryParam("componentId") String componentId,
                                                        @QueryParam("procedureId") String procedureId,
                                                        @QueryParam("serialNumber") String serialNumber
    )
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);       
    
        Date inspectionDate = null;
        try {
            inspectionDate = sdf.parse(inspectionDateStr);
        } catch (ParseException ex) {
            Logger.getLogger(TblInspectorManhoursResouce.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        InspectionResult list = this.tblCircuitBoardInspectionResultService.searchInspectionResultsBySerialNumber(inspectorId, procedureId, componentId, inspectionDate, serialNumber);
        
        return list;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CircuitBoardInspectionResult postCircuitBoardInspectionResult(CircuitBoardInspectionResult circuitBoardInspectionResult){

        //BasicResponse response = new BasicResponse();
        if (circuitBoardInspectionResult == null) {
            this.setErrorInfo(circuitBoardInspectionResult, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist");
            return circuitBoardInspectionResult;            
        }
         // get login user info.
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        this.tblCircuitBoardInspectionResultService.updateInspecttionResult(circuitBoardInspectionResult, loginUser);
        
        return circuitBoardInspectionResult; // response;
    }
    
    private void setErrorInfo(BasicResponse response, String errorCode, String msgDictKey, Object... args) {
        response.setError(true);
        response.setErrorCode(errorCode);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), msgDictKey);
        if (args != null) {
            msg = String.format(msg, args);
        }
        response.setErrorMessage(msg);
    }
}
