/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.automaticmachine.log;

import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.circuitboard.inspection.InspectionResultSum;
import com.kmcj.karte.resources.circuitboard.serialnumber.MstCircuitBoardSerialNumberService;
import java.text.SimpleDateFormat;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

/**
 *
 * @author h.ishihara
 */
@RequestScoped
@Path("automaticMachineLog")
public class TblAutomaticMachineLogResource {
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
        
    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblAutomaticMachineLogService tblAutomaticMachineLogService;
    
    @Inject
    private MstCircuitBoardSerialNumberService mstCircuitBoardSerialNumberService;
     
    public TblAutomaticMachineLogResource(){
    }
   
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postAutomaticMachinemaLog(AutomaticMachineLogData machineLogres) {

        ImportResultResponse response = new ImportResultResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        try {

            ImportResultResponse machineLogResponse =tblAutomaticMachineLogService.insertAutomaticMachineLogs(machineLogres.getLogs(), loginUser);

            ImportResultResponse serialNumberResponse = mstCircuitBoardSerialNumberService.insertCircuitBoardSerialNumbers(machineLogres.getSerialNumberList(), loginUser);
           
            response.setSucceededCount(machineLogResponse.getSucceededCount() + serialNumberResponse.getSucceededCount());
            response.setFailedCount(machineLogResponse.getFailedCount() + serialNumberResponse.getFailedCount());
            response.setLog(machineLogResponse.getLog() + serialNumberResponse.getLog());

        } catch (Exception e) {
            response.setError(true);
            response.setErrorMessage(e.toString());

            return response;
        }
        return response;
    }
    
    @GET
    @Path("searchQualityData")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspectionResultSum searchInspectionResultSumsByCriteria(
            @QueryParam("productCode") String productCode,
            @QueryParam("componentId") String componentId,
            @QueryParam("componentCode") String componentCode,
            @QueryParam("inspectionDateStart") String dateStart,
            @QueryParam("inspectionDateEnd") String dateEnd,
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        Date inspectionDateStart = null;
        Date inspectionDateEnd = null;

        try {
            FileUtil fu = new FileUtil();  
            if (StringUtils.isNotEmpty(dateStart)) {
//                inspectionDateStart = sdf.parse(dateStart);
                inspectionDateStart = fu.getDateTimeParseForDate(dateStart + CommonConstants.SYS_MIN_TIME);
            }
            if (StringUtils.isNotEmpty(dateEnd)) {
//                inspectionDateEnd = sdf.parse(dateEnd);
                inspectionDateEnd = fu.getDateTimeParseForDate(dateEnd + CommonConstants.SYS_MAX_TIME);
            }
        } catch (Exception ex) {
            Logger.getLogger(TblAutomaticMachineLogResource.class.getName()).log(Level.SEVERE, null, ex); 
        }
//        集計テーブルを変更するためsearchInspectionResultSumsByCriteria2に変更
        List sumList = this.tblAutomaticMachineLogService.searchInspectionResultSumsByCriteria2(productCode, componentCode, inspectionDateStart, inspectionDateEnd,
                pageNumber, pageSize);
//        HashMap<String, Integer> partNumList = null;
//        //CircuitBoardTargetPpmData ppmData = null;
//        if(sumList.size() >0){
//            partNumList = tblCircuitBoardInspectionResultService.searchInspectionResultsByCriteria(productCode,0,null, inspectionDateStart, inspectionDateEnd);
//            //ppmData=mstCircuitBoardTargetPpmService.getTargetPpms();
//        }
//        for (Object obj : sumList) {
//            CircuitBoardInspectionResultSum sum = (CircuitBoardInspectionResultSum) obj;
//            if (partNumList != null) {
//                String key = sum.getInspectorId() + "," + sum.getInspectionDateStr();
//                if (partNumList.containsKey(key)) {
//                    sum.setDefectivePartNum(partNumList.get(key));
//                    if (sum.getDefectivePartNum() != null && sum.getInspectedItemNum() != null && sum.getInspectedItemNum() > 0) {
//                        double partppm = (((double) sum.getDefectivePartNum()) / ((double) sum.getInspectedItemNum())) * 1000000;
//                        sum.setPpmDefectivePartNum(partppm);
//                    }
//                }
//            }
//       
//        }
        InspectionResultSum result = new InspectionResultSum();
        result.setInspectionResultSumList(sumList);

        return result;
    }

       @GET
    @Path("searchQualityMonthlyData")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspectionResultSum searchInspectionMonthlyResultSumsByCriteria(
            @QueryParam("productCode") String productCode,
            @QueryParam("componentId") String componentId,
            @QueryParam("componentCode") String componentCode,
            @QueryParam("inspectionDateStart") String dateStart,
            @QueryParam("inspectionDateEnd") String dateEnd,
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        Date inspectionDateStart = null;
        Date inspectionDateEnd = null;
        try {
            FileUtil fu = new FileUtil();  
            if (StringUtils.isNotEmpty(dateStart)) {
//                inspectionDateStart = sdf.parse(dateStart);
                inspectionDateStart = fu.getDateTimeParseForDate(dateStart + CommonConstants.SYS_MIN_TIME);
            }
            if (StringUtils.isNotEmpty(dateEnd)) {
//                inspectionDateEnd = sdf.parse(dateEnd);
                inspectionDateEnd = fu.getDateTimeParseForDate(dateEnd + CommonConstants.SYS_MAX_TIME);
            }
        } catch (Exception ex) {
            Logger.getLogger(TblAutomaticMachineLogResource.class.getName()).log(Level.SEVERE, null, ex); 
        }
//        集計テーブルを変更するためsearchInspectionResultSumsByCriteria2に変更
        List sumList = this.tblAutomaticMachineLogService.searchInspectionMonthlyResultSumsByCriteria2(productCode, componentCode, inspectionDateStart, inspectionDateEnd,
                pageNumber, pageSize);
//        HashMap<String, Integer> partNumList = null;
//        //CircuitBoardTargetPpmData ppmData = null;
//        if(sumList.size() >0){
//            partNumList = tblCircuitBoardInspectionResultService.searchInspectionResultsByCriteria(productCode,0,null, inspectionDateStart, inspectionDateEnd);
//            //ppmData=mstCircuitBoardTargetPpmService.getTargetPpms();
//        }
//        for (Object obj : sumList) {
//            CircuitBoardInspectionResultSum sum = (CircuitBoardInspectionResultSum) obj;
//            if (partNumList != null) {
//                String key = sum.getInspectorId() + "," + sum.getInspectionDateStr();
//                if (partNumList.containsKey(key)) {
//                    sum.setDefectivePartNum(partNumList.get(key));
//                    if (sum.getDefectivePartNum() != null && sum.getInspectedItemNum() != null && sum.getInspectedItemNum() > 0) {
//                        double partppm = (((double) sum.getDefectivePartNum()) / ((double) sum.getInspectedItemNum())) * 1000000;
//                        sum.setPpmDefectivePartNum(partppm);
//                    }
//                }
//            }
//       
//        }
        InspectionResultSum result = new InspectionResultSum();
        result.setInspectionResultSumList(sumList);

        return result;
    }

}
