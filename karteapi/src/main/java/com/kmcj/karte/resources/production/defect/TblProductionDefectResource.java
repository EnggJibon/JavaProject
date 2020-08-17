/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.defect;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
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

/**
 *
 */
@RequestScoped
@Path("productiondefect")
public class TblProductionDefectResource {
    
    @Inject
    private TblProductionDefectService tblProductionDefectService;
    
    @Context
    private ContainerRequestContext requestContext;
    
    /**
     * 生産不良数取得
     * @param productionDetailId 生産明細ID
     * @return 
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionDefectList getProductionDefectList(@QueryParam("productionDetailId") String productionDetailId) {
        return tblProductionDefectService.getProductionDefectList(productionDetailId);
    }

    /**
     * 生産不良数登録
     * @param tblProductionDefectList 生産不具合リスト
     * @return 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postProductionDefects(TblProductionDefectList tblProductionDefectList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblProductionDefectService.postProductionDefects(tblProductionDefectList == null ? null : tblProductionDefectList.getProductionDefects(), null, loginUser.getUserUuid());
    }
}
