package com.kmcj.karte.resources.circuitboard.procedure;

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
@Path("circuitboard/procedure")
@RequestScoped
public class MstCircuitBoardProcedureResource {

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstCircuitBoardProcedureService mstCircuitBoardProcedureService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    public MstCircuitBoardProcedureResource() {
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstCircuitBoardProcedureList getMstCircuitBoardProcedure() {
        return this.mstCircuitBoardProcedureService.getMstCircuitBoardProcedureSubstrate();
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MstCircuitBoardProcedure> getMstCircuitBoardProcedureAll() {
        return mstCircuitBoardProcedureService.getMstCircuitBoardProcedure();
    }

    @GET
    @Path("uniquedata")
    @Produces(MediaType.APPLICATION_JSON)
    public MstCircuitBoardProcedure getMstCircuitBoardProcedureData(@QueryParam("procedureName") String procedureName, 
           @QueryParam("displayDefect") int displayDefect, 
           @QueryParam("displayManhour") int displayManhour) {
        return mstCircuitBoardProcedureService.getEnabledProcedureData(procedureName, displayDefect, displayManhour);
    }
    
    @GET
    @Path("defect")
    @Produces(MediaType.APPLICATION_JSON)
    public ProcedureData getProceduresForDefectListDisplay() {
        return this.mstCircuitBoardProcedureService.getProceduresForDefectListDisplay();
    }

    @PUT
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SubstrateProcedureResponse addSubstrateProcedure(List<MstCircuitBoardProcedure> dataSubstrate) {
        SubstrateProcedureResponse response = new SubstrateProcedureResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        Boolean allValid = true;

        for (MstCircuitBoardProcedure procedure : dataSubstrate) {
            MstCircuitBoardProcedure checkProcedureNameExist = mstCircuitBoardProcedureService.checkProcedureNameExist(procedure.getProcedureName());
            if (checkProcedureNameExist != null) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "substrate_procedure_name_exists"));
                response.setProcedureName(procedure.getProcedureName());
                response.setId(checkProcedureNameExist.getId());
                allValid = false;
                break;
            }
        }

        if (allValid) {
            //execute insert data
            Map listUID = this.mstCircuitBoardProcedureService.insertManyProcedure(dataSubstrate, loginUser);
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
    public SubstrateProcedureResponse updateSubstrateProcedure(List<MstCircuitBoardProcedure> dataSubstrate) {
        SubstrateProcedureResponse response = new SubstrateProcedureResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        this.mstCircuitBoardProcedureService.updateManyProcedure(dataSubstrate, loginUser);
        return response;
    }

    @DELETE
    @Path("delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteSubstrateProcedure(@PathParam("id") String id) {
        BasicResponse response = new BasicResponse();
        id = FileUtil.getDecode(id);
        this.mstCircuitBoardProcedureService.deleteProcedure(id);
        return response;
    }

    @GET
    @Path("manhour")
    @Produces(MediaType.APPLICATION_JSON)
    public ProcedureData get() {
        return this.mstCircuitBoardProcedureService.getProceduresForForManhourDisplay();
    }

}
