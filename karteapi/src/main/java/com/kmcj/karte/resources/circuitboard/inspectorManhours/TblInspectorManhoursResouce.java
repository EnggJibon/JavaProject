/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspectorManhours;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.circuitboard.inspectorManhours.model.InspectorManhour;
import com.kmcj.karte.resources.circuitboard.inspectorManhours.model.InspectorManhourList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import com.kmcj.karte.util.FileUtil;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;

/**
 *
 * @author h.ishihara
 */
@RequestScoped
@Path("inspectorHours")
public class TblInspectorManhoursResouce {
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private TblInspectorManhoursService tblsInspectorManhoursService;
    
    public TblInspectorManhoursResouce(){
    }
    
    /**
     *
     * @param inspectorUuid
     * @param inspectionDateStr
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspectorManhourList getInspectorManhours(
                                                        @QueryParam("inspectorUuid") String inspectorUuid,
                                                        @QueryParam("inspectionDate") String inspectionDateStr
    ){
    SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);       
    
    Date inspectionDate = null;
        try {
            inspectionDate = sdf.parse(inspectionDateStr);
        } catch (ParseException ex) {
            Logger.getLogger(TblInspectorManhoursResouce.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        
        InspectorManhourList list = this.tblsInspectorManhoursService.getInsInspectorManhourList(inspectorUuid, inspectionDate);
        
        return list;
    }
    
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public InspectorManhourList getInspectorManhours(
//                                                        @PathParam("inspectorUuid") String inspectorUuid,
//                                                        @PathParam("inspectionDate") Date inspectionDate,
//                                                        @PathParam("procedureId") String procedureId,
//                                                        @PathParam("componentId") String componentId){
//        InspectorManhourList list = this.tblsInspectorManhoursService.getInsInspectorManhourList(inspectorUuid, componentId, procedureId, inspectionDate);
//        
//        return list;
//    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postInspectorManhours(List<InspectorManhour> manHourList){
    //public BasicResponse postInspectorManhours(InspectorManhour manhour){
        //List<InspectorManhour> manHourList = null;
        BasicResponse response = new BasicResponse();
        if (manHourList == null || manHourList.size() < 1) {
            this.setErrorInfo(response, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist");
            return response;            
        }
         // get login user info.
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        this.tblsInspectorManhoursService.updateInspectorManhours(manHourList, loginUser);
        return response;
    }
    
    @POST
    @Path("single")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postInspectorManhour(InspectorManhour manHour){
        BasicResponse response = new BasicResponse();
         // get login user info.
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        this.tblsInspectorManhoursService.updateInspectorManhour(manHour, loginUser);
        return response;
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
    
    @DELETE
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteInspectorManhour(@QueryParam("inspectorUuid") String inspectorID,
                                                   @QueryParam("componentID") String componentID,  
                                                   @QueryParam("procedureID") String procedureID,  
                                                   @QueryParam("inspectionDate") String inspectionDate) {
        BasicResponse response = new BasicResponse();
        this.tblsInspectorManhoursService.deleteInspectorManhour(inspectorID, componentID, procedureID, DateFormat.strToDate(inspectionDate));
        return response;
    }
}
