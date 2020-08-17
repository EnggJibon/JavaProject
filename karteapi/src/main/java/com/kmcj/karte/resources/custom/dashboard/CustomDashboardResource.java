/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.dashboard;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.ObjectResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * カスタムダッシュボード系API
 *
 * @author t.takasaki
 */
@Path("customdashboard")
@RequestScoped
public class CustomDashboardResource {

    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private CustomDashboardService ctDashboardService;

    @GET
    @Path("getornewdashboard/{dashboardId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<TblCtDashboard> getDashboard(@PathParam("dashboardId") String dashboardId) {
        return new ObjectResponse<>(ctDashboardService.getDashboard(dashboardId));
    }
    
    @GET
    @Path("authedlist")
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<List<TblCtDashboard>> getAuthedDashboards() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return new ObjectResponse<>(ctDashboardService.getAuthedDashboards(loginUser.getAuthId()));
    }
    
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<List<TblCtDashboard>> getAllDashboards() {
        return new ObjectResponse<>(ctDashboardService.getAllDashboards());
    }
    
    @PUT
    @Path("dashboard")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putDashboard(DashboardDef dashboardDef) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if(dashboardDef.getDashboard().getCreateDate() == null) {
            dashboardDef.getDashboard().setCreateDate(new Date());
            dashboardDef.getDashboard().setCreateUserUUID(loginUser.getUserUuid());
        }
        dashboardDef.getDashboard().setUpdateDate(new Date());
        dashboardDef.getDashboard().setUpdateUserUUID(loginUser.getUserUuid());
        ctDashboardService.putDashboard(dashboardDef);
        return new BasicResponse();
    }
    
    @DELETE
    @Path("dashboard/{dashboardId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse delDashboard(@PathParam("dashboardId") String dashboardId) {
        ctDashboardService.delDashboard(dashboardId);
        return new BasicResponse();
    }
    
    @GET
    @Path("allwidget")
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<List<TblCtWidget>> getAllWidgets() {
        return new ObjectResponse<>(ctDashboardService.getAllWidgets());
    }
    
    @POST
    @Path("widgetlist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<List<TblCtWidget>> getWidgets(List<String> ids) {
        return new ObjectResponse<>(ctDashboardService.getWidgets(ids));
    }
    
    @GET
    @Path("getornewwidget/{widgetid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<TblCtWidget> getWidget(@PathParam("widgetid") String widgetid) {
        return new ObjectResponse<>(ctDashboardService.getOrNewWidget(widgetid));
    }
    
    @PUT
    @Path("widget")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putWidget(TblCtWidget widget) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if(widget.getCreateDate() == null) {
            widget.setCreateDate(new Date());
            widget.setCreateUserUUID(loginUser.getUserUuid());
        }
        widget.setUpdateDate(new Date());
        widget.setUpdateUserUUID(loginUser.getUserUuid());
        ctDashboardService.putWidget(widget);
        return new BasicResponse();
    }
    
    @DELETE
    @Path("widget/{widgetid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse delWidget(@PathParam("widgetid") String widgetid) {
        ctDashboardService.delWidget(widgetid);
        return new BasicResponse();
    }
    
    @GET
    @Path("layout/{dashboardId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<List<TblCtDashboardLayout>> getDashboardLayout(@PathParam("dashboardId") String dashboardId) {
        return new ObjectResponse<>(ctDashboardService.getDashboardLayout(dashboardId));
    }
    
    @GET
    @Path("filterparam/{dashboardId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<List<TblCtDashboardFilter>> getDashboardFilterParam(@PathParam("dashboardId") String dashboardId) {
        return new ObjectResponse<>(ctDashboardService.getDashboardFilterParam(dashboardId));
    }
    
    @GET
    @Path("auth/{dashboardId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectResponse<List<TblCtDashboardAuth>> getDashboardAuth(@PathParam("dashboardId") String dashboardId) {
        return new ObjectResponse<>(ctDashboardService.getDashboardAuth(dashboardId));
    }
}