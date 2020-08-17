/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.circuitboard.targetppm.MstCircuitBoardTargetPpmService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author h.ishihara
 */
@RequestScoped
@Path("inspectionResultSum")
public class TblCircuitBoardInspectionResultSumResource {
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private TblCircuitBoardInspectionResultSumService tblCircuitBoardInspectionResultSumService;
    
    @Inject
    private TblCircuitBoardInspectionResultService tblCircuitBoardInspectionResultService;
    
    @Inject
    private MstCircuitBoardTargetPpmService  mstCircuitBoardTargetPpmService;
    
    public TblCircuitBoardInspectionResultSumResource(){        
    }
    
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspectionResult searchInspectionResults(
                                                        @QueryParam("inspectorId") String inspectorId,
                                                        @QueryParam("procedureId") String procedureId,
                                                        @QueryParam("inspectionDate") String inspectionDateStr
    )
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);       
    
        FileUtil fu = new FileUtil();  
        Date inspectionDate = null;
        try {
//            inspectionDate = sdf.parse(inspectionDateStr);
            inspectionDate = fu.getDateTimeParseForDate(inspectionDateStr + CommonConstants.SYS_MIN_TIME);
        } catch (Exception ex) {
            Logger.getLogger(TblCircuitBoardInspectionResultSumResource.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        InspectionResult result = this.tblCircuitBoardInspectionResultSumService.searchInspectionResultSumsByCriteria(inspectorId, procedureId, inspectionDate);
        
        return result;
    }
    
    @GET
    @Path("searchDetail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspectionResultSum searchInspectionResultSumsByCriteria(
            @QueryParam("productCode") String productCode,
            @QueryParam("componentId") String componentId,
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
            Logger.getLogger(TblCircuitBoardInspectionResultSumResource.class.getName()).log(Level.SEVERE, null, ex); 
        }
        List sumList = this.tblCircuitBoardInspectionResultSumService.searchInspectionResultSumsByCriteria(productCode, componentId, inspectionDateStart, inspectionDateEnd,
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
    @Path("searchResult")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String,HashMap<String, Integer>> searchInspectionResultByCriteria(
            @QueryParam("productCode") String productCode,
            @QueryParam("productionLineNo") int productionLineNo,
            @QueryParam("componentId") String componentId,
            @QueryParam("inspectionDateStart") String dateStart,
            @QueryParam("inspectionDateEnd") String dateEnd) {
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
            Logger.getLogger(TblCircuitBoardInspectionResultSumResource.class.getName()).log(Level.SEVERE, null, ex); 
        }
        HashMap<String,HashMap<String, Integer>> result = this.tblCircuitBoardInspectionResultService.searchInspectionResultsByCriteria(productCode, productionLineNo, componentId, inspectionDateStart, inspectionDateEnd);

        return result;
    }
    
    @GET
    @Path("searchMonthlyDetail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspectionResultSum searchInspectionMonthlyResultSumsByCriteria(
            @QueryParam("productCode") String productCode,
            @QueryParam("componentId") String componentId,
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
            Logger.getLogger(TblCircuitBoardInspectionResultSumResource.class.getName()).log(Level.SEVERE, null, ex); 
        }
        List sumList = this.tblCircuitBoardInspectionResultSumService.searchInspectionMonthlyResultSumsByCriteria(productCode,componentId, inspectionDateStart, inspectionDateEnd,
                pageNumber, pageSize);
//        HashMap<String, Integer> partNumList = null;
//        if(sumList.size() >0){
//            partNumList = tblCircuitBoardInspectionResultService.searchInspectionResultsByCriteria(productCode,0,null, inspectionDateStart, inspectionDateEnd, true);
//        }
//        for (Object obj : sumList) {
//            CircuitBoardInspectionResultSum sum = (CircuitBoardInspectionResultSum) obj;
//            if (partNumList != null) {
//                String key = sum.getInspectionDateStr();
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
    @Path("exportDailyResultCsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getInspectionResultSumsCSV(
            @QueryParam("productCode") String productCode, 
            @QueryParam("componentCode") String componentCode,
            @QueryParam("inspectionDateStart") String inspectionDateStart,
            @QueryParam("inspectionDateEnd") String inspectionDateEnd
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);

        Date formatedInspectionDateStart = null;
        Date formatedInspectionDateEnd = null;
        try {
            FileUtil fu = new FileUtil();  
            // 検査開始日
            if (StringUtils.isNotEmpty(inspectionDateStart)) {
//                formatedInspectionDateStart = sdf.parse(inspectionDateStart);
                formatedInspectionDateStart = fu.getDateTimeParseForDate(inspectionDateStart + CommonConstants.SYS_MIN_TIME);
            }

            // 検査终了日
            if (StringUtils.isNotEmpty(inspectionDateEnd)) {
//                formatedInspectionDateEnd = sdf.parse(inspectionDateEnd);
                formatedInspectionDateEnd = fu.getDateTimeParseForDate(inspectionDateEnd + CommonConstants.SYS_MAX_TIME);
            }
        } catch (Exception e) {
            FileReponse response = new FileReponse();
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
            return response;
        }
        return tblCircuitBoardInspectionResultSumService.getInspectionResultSumsCSV(productCode,componentCode, formatedInspectionDateStart, formatedInspectionDateEnd, loginUser);
        
    }
    
    @GET
    @Path("exportMonthlyResultCsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getInspectionMonthlyResultSumsCSV(
            @QueryParam("productCode") String productCode, 
            @QueryParam("componentCode") String componentCode,
            @QueryParam("inspectionDateStart") String inspectionDateStart,
            @QueryParam("inspectionDateEnd") String inspectionDateEnd
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);

        Date formatedInspectionDateStart = null;
        Date formatedInspectionDateEnd = null;
        try {
            FileUtil fu = new FileUtil();  
            // 検査開始日
            if (StringUtils.isNotEmpty(inspectionDateStart)) {
//                formatedInspectionDateStart = sdf.parse(inspectionDateStart);
                formatedInspectionDateStart = fu.getDateTimeParseForDate(inspectionDateStart + CommonConstants.SYS_MIN_TIME);
            }

            // 検査终了日
            if (StringUtils.isNotEmpty(inspectionDateEnd)) {
//                formatedInspectionDateEnd = sdf.parse(inspectionDateEnd);
                formatedInspectionDateEnd = fu.getDateTimeParseForDate(inspectionDateEnd + CommonConstants.SYS_MAX_TIME);
            }
        } catch (Exception e) {
            FileReponse response = new FileReponse();
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
            return response;
        }
        return tblCircuitBoardInspectionResultSumService.getInspectionMonthlyResultSumsCSV(productCode,componentCode, formatedInspectionDateStart, formatedInspectionDateEnd, loginUser);
        
    }
    
    @GET
    @Path("monthlyGraphData")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspectionResultGraphData getMonthlyGraphData(
            //@QueryParam("productCode") String productCode, 
            @QueryParam("componentCode") String componentCode,
            @QueryParam("inspectionDateStart") String inspectionDateStart,
            @QueryParam("inspectionDateEnd") String inspectionDateEnd
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);

        Date formatedInspectionDateStart = null;
        Date formatedInspectionDateEnd = null;
        try {
            FileUtil fu = new FileUtil();  
            // 検査開始日
            if (StringUtils.isNotEmpty(inspectionDateStart)) {
//                formatedInspectionDateStart = sdf.parse(inspectionDateStart);
                formatedInspectionDateStart = fu.getDateTimeParseForDate(inspectionDateStart + CommonConstants.SYS_MIN_TIME);
            }

            // 検査终了日
            if (StringUtils.isNotEmpty(inspectionDateEnd)) {
//                formatedInspectionDateEnd = sdf.parse(inspectionDateEnd);
                formatedInspectionDateEnd = fu.getDateTimeParseForDate(inspectionDateEnd + CommonConstants.SYS_MAX_TIME);
            }
        } catch (Exception e) {
            InspectionResultGraphData response = new InspectionResultGraphData();
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid"));
            return response;
        }
        return tblCircuitBoardInspectionResultSumService.getMonthlyGraphData(componentCode, formatedInspectionDateStart, formatedInspectionDateEnd, loginUser.getLangId());
        
    }
//    @GET
//    @Path("searchResult")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public HashMap<String, Integer> searchInspectionResultByCriteria(
//            @QueryParam("productCode") String productCode,
//            @QueryParam("productionLineNo") int productionLineNo,
//            @QueryParam("componentId") String componentId,
//            @QueryParam("inspectionDateStart") String dateStart,
//            @QueryParam("inspectionDateEnd") String dateEnd) {
//        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
//        Date inspectionDateStart = null;
//        Date inspectionDateEnd = null;
//        try {
//            inspectionDateStart = sdf.parse(dateStart);
//            inspectionDateEnd = sdf.parse(dateEnd);
//        } catch (Exception ex) {
//            Logger.getLogger(TblCircuitBoardInspectionResultSumResource.class.getName()).log(Level.SEVERE, null, ex); 
//        }
//        HashMap<String, Integer> result = this.tblCircuitBoardInspectionResultService.searchInspectionResultsByCriteria(productCode, productionLineNo, componentId, inspectionDateStart, inspectionDateEnd);
//
//        return result;
//    }
    @GET
    @Path("workAssignment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspectionResult searchWorkAssignments(@QueryParam("resultSumId") String resultSumId){
        InspectionResult result = this.tblCircuitBoardInspectionResultSumService.searchWorkAssignments(resultSumId);
        return result;
    }
}
