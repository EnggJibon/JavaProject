/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.targetppm;

import com.kmcj.karte.resources.circuitboard.inspectorManhours.TblInspectorManhoursResouce;
import com.kmcj.karte.resources.circuitboard.point.CircuitBoardPointData;
import com.kmcj.karte.resources.circuitboard.point.MstCircuitBoardPointService;
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
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author h.ishihara
 */
@RequestScoped
@Path("circuitBoardTargetPpm")
public class MstCircuitBoardTargetPpmResouce {
    @Inject
    private MstCircuitBoardTargetPpmService  mstCircuitBoardTargetPpmService;
      
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CircuitBoardTargetPpmData getCircuitBoardBySerialNumber(){
       return this.mstCircuitBoardTargetPpmService.getTargetPpms();
    }
    
    @Path("baseDate")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CircuitBoardTargetPpmData getCircuitBoardBySerialNumber(@QueryParam("baseDate") String strBaseDate){
               SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);       
    
        Date baseDate = null;
        try {
            baseDate = sdf.parse(strBaseDate);
        } catch (ParseException ex) {
            Logger.getLogger(TblInspectorManhoursResouce.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return this.mstCircuitBoardTargetPpmService.getTargetPpmsByBaseDate(baseDate);
    }
}
