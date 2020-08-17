/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.lot.relation;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
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
import javax.ws.rs.core.UriInfo;

/**
 * po
 *
 * @author admin
 */
@RequestScoped
@Path("component/lot/relation")
public class TblComponentLotRelationResource {

    @Context
    private UriInfo context;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblComponentLotRelationService tblComponentLotRelationService;

    public TblComponentLotRelationResource() {
    }

    /**
     *
     * @param componentCode
     * @param subComponentCode
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblComponentLotRelationVoList getTblComponentLotRelationList(
            @QueryParam("componentCode") String componentCode,
            @QueryParam("subComponentCode") String subComponentCode,
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {

        return tblComponentLotRelationService.getTblComponentLotRelationList(componentCode, subComponentCode, sidx, sord, pageNumber, pageSize, true);
    }

    /**
     *
     * @param componentCode
     * @param subComponentCode
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getTblComponentLotRelationCsv(
            @QueryParam("componentCode") String componentCode,
            @QueryParam("subComponentCode") String subComponentCode
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentLotRelationService.getTblComponentLotRelationCsv(componentCode, subComponentCode, loginUser.getLangId(), loginUser.getUserUuid());
    }

    /**
     *
     * @param tblComponentLotRelationVoList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postTblComponentLotRelation(
            TblComponentLotRelationVoList tblComponentLotRelationVoList
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentLotRelationService.postTblComponentLotRelation(tblComponentLotRelationVoList, loginUser.getLangId(), loginUser.getUserUuid());
    }

}
