/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.defect;

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

/**
 * @author bacpd
 */
@RequestScoped
@Path("defect")
public class MstCircuitBoardDefectResource {
    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstCircuitBoardDefectService mstCircuitBoardDefectService;

    public MstCircuitBoardDefectResource() {
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DefectData getDefects() {
        return this.mstCircuitBoardDefectService.getDefects();
    }

    @PUT
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public DefectData addDefectSubstrate(List<MstCircuitBoardDefect> dataSubstrate) {
        DefectData response = new DefectData();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        Boolean allValid = true;

        for (MstCircuitBoardDefect defectSubstrate : dataSubstrate) {
            MstCircuitBoardDefect checkDefectNameExist = mstCircuitBoardDefectService.checkDefectNameExist(defectSubstrate.getDefectName());
            if (checkDefectNameExist != null) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "substrate_defect_name_exists"));
                response.setDefectName(defectSubstrate.getDefectName());
                response.setId(checkDefectNameExist.getId());
                allValid = false;
                break;
            }
        }

        if (allValid) {
            //execute insert data
            Map listUID = this.mstCircuitBoardDefectService.insertManyDefect(dataSubstrate, loginUser);
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
    public DefectData updateSubstrateProcedure(List<MstCircuitBoardDefect> dataSubstrate) {
        DefectData response = new DefectData();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        this.mstCircuitBoardDefectService.updateManyDefect(dataSubstrate, loginUser);
        return response;
    }

    @DELETE
    @Path("delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteSubstrateDefect(@PathParam("id") String id) {
        BasicResponse response = new BasicResponse();
        id = FileUtil.getDecode(id);
        this.mstCircuitBoardDefectService.deleteDefect(id);
        return response;
    }
}
