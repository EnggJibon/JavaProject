/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material.stock;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * 材料在庫数登録
 *
 * @author admin
 */
@RequestScoped
@Path("material/stock")
public class TblMaterialStockResource {

    @Context
    private UriInfo context;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblMaterialStockService tblMaterialStockService;

    public TblMaterialStockResource() {
    }

    /**
     * 材料コードまたは材料名を入力し検索ボタンで材料マスタの材料コードかつ材料名を部分一致で検索する。
     *
     * @param materialCode
     * @param materialName
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMaterialStockVoList getTblMaterialStockList(
            @QueryParam("materialCode") String materialCode,
            @QueryParam("materialName") String materialName,
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialStockService.getTblMaterialStockVoList(loginUser.getLangId(), materialCode, materialName, sidx, sord, pageNumber, pageSize, true, 0);
    }

    /**
     * CSV出力
     *
     * @param materialCode
     * @param materialName
     * @return an instance of TblStockVoList
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getTblMaterialStockListCsv(
            @QueryParam("materialCode") String materialCode,
            @QueryParam("materialName") String materialName
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialStockService.getTblMaterialStockVoListCsv(materialCode, materialName, loginUser);
    }

    /**
     * CSV入力
     *
     * @param fileUuid
     * @return an instance of ImportResultResponse
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postTblStockListCsv(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialStockService.postTblMaterialStockVoListCsv(fileUuid, loginUser);
    }

    /**
     * 登録
     *
     * @return an instance of TblMaterialStock update
     * @param tblMaterialStockVos
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postTblStockList(TblMaterialStockVoList tblMaterialStockVos) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialStockService.updateTblMaterialStock(tblMaterialStockVos, loginUser.getUserUuid());
    }

}
