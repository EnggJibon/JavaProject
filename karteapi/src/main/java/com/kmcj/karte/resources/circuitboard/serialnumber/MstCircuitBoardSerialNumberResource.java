/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.serialnumber;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
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
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.constants.ErrorMessages;

/**
 *
 * @author h.ishihara
 */
@RequestScoped
@Path("circuitBoardserialNumber")
public class MstCircuitBoardSerialNumberResource {
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private MstCircuitBoardSerialNumberService  mstCircuitBoardSerialNumberService;
    
    @Path("getSerialNumber")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CircuitBoardSerialNumber getIdBySerialNumber(@QueryParam("serialNumber") String serialNumber){
       return this.mstCircuitBoardSerialNumberService.getIdBySerialNumber(serialNumber);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCircuitBoardSerialNumber getCircuitBoardBySerialNumber(@QueryParam("serialNumber") String serialNumber){
       return this.mstCircuitBoardSerialNumberService.getCircuitBoardBySerialNumber(serialNumber);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CircuitBoardSerialNumber postMstCircuitBoardSerialNumber(CircuitBoardSerialNumber serialNumberData){

        //BasicResponse response = new BasicResponse();
        if (serialNumberData == null) {
            this.setErrorInfo(serialNumberData, ErrorMessages.E201_APPLICATION, "msg_error_inspection_data_not_exist");
            return serialNumberData;            
        }
         // get login user info.
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        this.mstCircuitBoardSerialNumberService.updateSerialNumber(serialNumberData, loginUser);
        
        return serialNumberData; // response;
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
