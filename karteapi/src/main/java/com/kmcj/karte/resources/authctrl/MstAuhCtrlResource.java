/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.authctrl;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@RequestScoped
@Path("authctrl")
public class MstAuhCtrlResource {

    @Context
    private UriInfo context;
    @Context
    private ContainerRequestContext requestContext;
    @Inject
    private MstAuthCtrlService mstAuthCtrlService;
    @Inject
    private MstDictionaryService mstDictionaryService; //msg_error_opp_not_allowed

    /**
     * Creates a new instance of MstAuhCtrlResource
     */
    public MstAuhCtrlResource() {
    }

    /**
     * Retrieves representation of an instance of com.kmcj.karte.resources.authorization.MstAuhCtrlResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("checkfunc/{functionId}")
    public BasicResponse checkFunc(@PathParam("functionId") String functionId) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        if (!mstAuthCtrlService.isAuthorizedFunction(loginUser.getAuthId(), functionId)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E105_OPP_NOT_ALLOWED);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_opp_not_allowed"));
        }
        return response;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("authorizedfuncs")
    public MstAuthCtrlList getAuthorizedFunctions() {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstAuthCtrlService.getAuthorizedFunctions(loginUser.getAuthId());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MstAuthCtrlList getMstAuthCtrlList() {
        return mstAuthCtrlService.getMstAuthCtrls();
    }
    
    @POST
    @Path("replaceall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse replaceMstAuthCtrls(MstAuthCtrlList mstAuthCtrlList) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstAuthCtrlService.replaceMstAuthCtrls(mstAuthCtrlList, loginUser);
    }
    
    
    /**
     * PUT method for updating or creating an instance of MstAuhCtrlResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
