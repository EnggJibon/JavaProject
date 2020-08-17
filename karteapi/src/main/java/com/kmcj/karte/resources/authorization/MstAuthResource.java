/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.authorization;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@Path("authorization")
@RequestScoped
public class MstAuthResource {

    @Context
    private UriInfo context;
    @Context
    private ContainerRequestContext requestContext;
   
    @Inject
    private MstAuthService mstAuthService;
    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * Creates a new instance of MstAuthResource
     */
    public MstAuthResource() {
    }

    /**
     * Retrieves representation of an instance of com.kmcj.karte.resources.authorization.MstAuthResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MstAuthList getMstAuth() {
        return mstAuthService.getMstAuths();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstAuthList updateMstAuth(MstAuthList mstAuthList) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        if (mstAuthService.checkCanDelete(mstAuthList)) {
            return mstAuthService.updateMstAuth(mstAuthList, loginUser);
        }
        else {
            mstAuthList.setError(true);
            mstAuthList.setErrorCode(ErrorMessages.E201_APPLICATION);
            mstAuthList.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_cannot_delete_used_record"));
            return mstAuthList;
        }
    }

    /**
     * PUT method for updating or creating an instance of MstAuthResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
