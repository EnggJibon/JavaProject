package com.kmcj.karte.resources.common.search;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("cond/search")
@RequestScoped
public class TblSearchCondMemoryResource {

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblSearchCondMemoryService tblSearchCondMemoryService;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblSearchCondMemoryValueList getSearchCondMemory(@QueryParam("screenId") String screenId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblSearchCondMemoryService.getTblSearchCondMemoryValueList(screenId, loginUser);
    }

    @POST
    @Path("{screenId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse setSearchCondMemory(@PathParam("screenId") String screenId, List<TblSearchCondMemoryValue> tblSearchCondMemoryValues) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblSearchCondMemoryService.setSearchCondMemory(screenId, tblSearchCondMemoryValues, loginUser);
    }
    
    @POST
    @Path("replace/{screenId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse replaceCondMemory(@PathParam("screenId") String screenId, List<TblSearchCondMemoryValue> tblSearchCondMemoryValues) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblSearchCondMemoryService.replaceCondMemory(screenId, tblSearchCondMemoryValues, loginUser);
    }
    
    @DELETE
    @Path("{screenId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteCondMemory(@PathParam("screenId") String screenId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblSearchCondMemoryService.deleteCondMemory(screenId, loginUser);
    }
}

