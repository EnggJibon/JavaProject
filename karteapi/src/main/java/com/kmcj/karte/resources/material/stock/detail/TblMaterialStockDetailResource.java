/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material.stock.detail;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * 材料在庫履歴 リソース
 *
 * @author admin
 */
@RequestScoped
@Path("material/stock/detail")
public class TblMaterialStockDetailResource {

    public TblMaterialStockDetailResource() {
    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblMaterialStockDetailService tblMaterialStockDetailService;

    /**
     * 選択した材料で材料在庫履歴画面を表示する。
     *
     * @param materialId
     * @param moveDateFrom
     * @param moveDateTo
     * @param searchFlg
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return an instance of TblMaterialStockDetailVoList
     */
    @GET
    @Path("{materialId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMaterialStockDetailVoList getTblMaterialStockDetailList(
            @PathParam("materialId") String materialId, //
            @QueryParam("moveDateFrom") String moveDateFrom, //
            @QueryParam("moveDateTo") String moveDateTo,
            @QueryParam("searchFlg") int searchFlg, //
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialStockDetailService.getTblMaterialStockDetailVoList(materialId, moveDateFrom, moveDateTo, loginUser.getLangId(), searchFlg, true, sidx, sord, pageNumber, pageSize);
    }

    /**
     * 選択した材料で材料在庫履歴画面CSV
     *
     * @param materialId
     * @param moveDateFrom
     * @param moveDateTo
     * @return an instance of FileReponse
     */
    @GET
    @Path("exportcsv/{materialId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getTblMaterialStockDetailCsv(
            @PathParam("materialId") String materialId, //
            @QueryParam("moveDateFrom") String moveDateFrom, //
            @QueryParam("moveDateTo") String moveDateTo //
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialStockDetailService.getTblMaterialStockDetailVoCsv(materialId, moveDateFrom, moveDateTo, loginUser);
    }

}
