/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material.lot;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
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

/**
 * 材料ロット リソース
 *
 * @author admin
 */
@RequestScoped
@Path("material/lot")
public class TblMaterialLotResource {

    public TblMaterialLotResource() {
    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblMaterialLotService tblMaterialLotService;

    /**
     * 選択した材料の在庫ロットを一覧表示する。
     *
     * @param materialId
     * @param searchFlg
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return an instance of TblMaterialLotVoList
     */
    @GET
    @Path("{materialId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMaterialLotVoList initTblMaterialLotList(
            @PathParam("materialId") String materialId, //
            @QueryParam("searchFlg") int searchFlg, //
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialLotService.getTblMaterialLotVoList(materialId, "", "", 0, "", loginUser.getLangId(), searchFlg, true, sidx, sord, pageNumber, pageSize);
    }
    
    /**
     * 検索　画面の材料コード、材料名で材料ロットテーブルを検索して表示する。
     *
     * @param materialCode
     * @param materialName
     * @param status
     * @param remarks
     * @param searchFlg
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return an instance of TblMaterialLotVoList
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMaterialLotVoList getTblMaterialLotList(
            @QueryParam("materialCode") String materialCode, //
            @QueryParam("materialName") String materialName, //
            @QueryParam("status") int status, //
            @QueryParam("remarks") String remarks, //
            @QueryParam("searchFlg") int searchFlg, //
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialLotService.getTblMaterialLotVoList("", materialCode, materialName, status, remarks, loginUser.getLangId(), searchFlg, true, sidx, sord, pageNumber, pageSize);
    }

    /**
     * 選択した材料の在庫ロットをCSV出力
     *
     * @param materialCode
     * @param materialName
     * @param status
     * @param remarks
     * @return an instance of FileReponse
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getTblMaterialLotCsv(
            @QueryParam("materialCode") String materialCode, //
            @QueryParam("materialName") String materialName, //
            @QueryParam("status") int status, //
            @QueryParam("remarks") String remarks //
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialLotService.getTblMaterialLotVoCsv(materialCode, materialName, status, remarks, loginUser);
    }

    /**
     * 材料ロットの追加・変更・削除
     *
     * @param tblMaterialLotVos
     * @return an instance of BasicResponse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateTblMaterialLot(
            TblMaterialLotVoList tblMaterialLotVos
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialLotService.updateTblMaterialLot(tblMaterialLotVos, loginUser.getUserUuid(), loginUser.getLangId());
    }

    /**
     * 入庫
     *
     * @param tblMaterialLotVo
     * @return an instance of BasicResponse
     */
    @POST
    @Path("store")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postTblMaterialLotStore(TblMaterialLotVo tblMaterialLotVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialLotService.postTblMaterialLotStoreDelivery(tblMaterialLotVo, CommonConstants.STORE, loginUser);
    }

    /**
     * 出庫
     *
     * @param tblMaterialLotVo
     * @return an instance of BasicResponse
     */
    @POST
    @Path("delivery")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postTblMaterialLotDelivery(TblMaterialLotVo tblMaterialLotVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialLotService.postTblMaterialLotStoreDelivery(tblMaterialLotVo, CommonConstants.DELIVERY, loginUser);
    }

    /**
     * CSV入庫
     *
     * @param fileUuid
     * @return an instance of ImportResultResponse
     */
    @POST
    @Path("store/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postTblMaterialLotCsvStore(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialLotService.postTblMaterialLotCsvStoreDelivery(fileUuid, CommonConstants.STORE, loginUser);
    }

    /**
     * CSV出庫
     *
     * @param fileUuid
     * @return an instance of ImportResultResponse
     */
    @POST
    @Path("delivery/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postTblMaterialLotCsvDelivery(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMaterialLotService.postTblMaterialLotCsvStoreDelivery(fileUuid, CommonConstants.DELIVERY, loginUser);
    }

    /**
     *
     * ロット番号の初期値は材料のロット番号を当日（yyyymmdd）で検索して最大の値に対して後ろ2桁に「＋１」した番号
     * 例：20180201-05を取得したら20180201-06にする。 。
     *
     * @param materialId
     * @return
     */
    @GET
    @Path("number")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMaterialLotVo getMaxLotNumber(@QueryParam("materialId") String materialId) {
        TblMaterialLotVo tblMaterialLotVo = new TblMaterialLotVo();
        tblMaterialLotVo.setLotNo(tblMaterialLotService.makeNewLotNumber(materialId));
        return tblMaterialLotVo;
    }

    /**
     * スマホ生産開始材料ロット番号リスト取得
     *
     * @param materialId
     * @return an instance of TblMaterialLotVoList
     */
    @GET
    @Path("mobile/{materialId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMaterialLotVoList getProductionTblMaterialLotList(
            @PathParam("materialId") String materialId
    ) {
        return tblMaterialLotService.getProductionTblMaterialLotList(materialId);
    }
}
