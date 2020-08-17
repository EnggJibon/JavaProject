/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.prifix;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.FileUtil;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author bacpd
 */
@RequestScoped
@Path("circuitNamePrefix")
public class MstCircuitNamePrefixResource {
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private MstCircuitNamePrefixService  mstCircuitNamePrefixService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PrefixData getCircuitNamePrefixForDisplay(){
        return this.mstCircuitNamePrefixService.getCircuitNamePrefixForDisplay();
    }
    
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public PrefixData getAllCircuitNamePrefix(){
        return this.mstCircuitNamePrefixService.getAllCircuitNamePrefix();
    }

    @PUT
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PrefixData addCircuitPrefix(List<MstCircuitNamePrefix> dataPrefix) {
        PrefixData response = new PrefixData();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        Boolean allValid = true;

        for (MstCircuitNamePrefix itemPrefix : dataPrefix) {
            MstCircuitNamePrefix checkIdExist = mstCircuitNamePrefixService.checkPrefixIdExists(itemPrefix.getId());
            if (checkIdExist != null) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "substrate_prefix_id_exists"));
                response.setId(itemPrefix.getId());
                allValid = false;
                break;
            }


            MstCircuitNamePrefix checkPrefixTitleExist = mstCircuitNamePrefixService.checkPrefixExists(itemPrefix.getPrefix());
            if (checkPrefixTitleExist != null) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "substrate_prefix_connector_exists"));
                response.setPrefix(itemPrefix.getPrefix());
                response.setPrefixId(checkPrefixTitleExist.getId());
                allValid = false;
                break;
            }
        }

        if (allValid) {
            //execute insert data
            Map listUID = this.mstCircuitNamePrefixService.insertManyPrefix(dataPrefix, loginUser);
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(listUID);
            response.setId(json);
        }

        return response;
    }

    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PrefixData updateCircuitPrefix(List<MstCircuitNamePrefixUpdate> dataPrefix) {
        PrefixData response = new PrefixData();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        Boolean allValid = true;

        for (MstCircuitNamePrefixUpdate itemPrefix : dataPrefix) {
            if (!Objects.equals(itemPrefix.getPrefixId(), itemPrefix.getId())) {
                MstCircuitNamePrefix checkIdExist = mstCircuitNamePrefixService.checkPrefixIdExists(itemPrefix.getPrefixId());
                if (checkIdExist != null) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "substrate_prefix_id_exists"));
                    response.setId(itemPrefix.getPrefixId());
                    allValid = false;
                    break;
                }
            }
        }

        if (allValid) {
            this.mstCircuitNamePrefixService.updateManyPrefix(dataPrefix, loginUser);
        }

        return response;
    }


    @DELETE
    @Path("delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteSubstrateDefect(@PathParam("id") String id) {
        BasicResponse response = new BasicResponse();
        id = FileUtil.getDecode(id);
        this.mstCircuitNamePrefixService.deletePrefixById(id);
        return response;
    }

}
