/*
 * To ch
nge this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.stock.detail;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("stock/detail")
public class TblStockDetailResource {

    @Context
    private ContainerRequestContext requestContext;

    /**
     * Creates a new instance of TblStockDetailResource
     */
    public TblStockDetailResource() {
    }

    @Inject
    private TblStockDetailService tblStockDetailService;

    /**
     * 在庫履歴画面描画
     *
     * @param componentId
     * @param procedureCode
     * @param moveDateFrom
     * @param moveDateTo
     * @param searchFlg
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return an instance of TblStockDetailVoList
     */
    @GET
    @Path("{componentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblStockDetailVoList getTblStockDetailVoList(
            @PathParam("componentId") String componentId,
            @QueryParam("procedureCode") String procedureCode,
            @QueryParam("moveDateFrom") String moveDateFrom,
            @QueryParam("moveDateTo") String moveDateTo,
            @QueryParam("searchFlg") int searchFlg,
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblStockDetailService.getTblStockDetailVoList(componentId, procedureCode, moveDateFrom, moveDateTo, loginUser.getLangId(), searchFlg, true, sidx, sord, pageNumber, pageSize);
    }

    /**
     * 在庫履歴画面CSV出力時
     *
     * @param componentId
     * @param procedureCode
     * @param moveDateFrom
     * @param moveDateTo
     * @return an instance of FileReponse
     */
    @GET
    @Path("exportcsv/{componentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getTblStockDetailCsv(@PathParam("componentId") String componentId, @QueryParam("procedureCode") String procedureCode, @QueryParam("moveDateFrom") String moveDateFrom, @QueryParam("moveDateTo") String moveDateTo
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblStockDetailService.getTblStockDetailVoCsv(componentId, procedureCode, moveDateFrom, moveDateTo, loginUser);
    }

}
