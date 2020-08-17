/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.lot;

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
import javax.ws.rs.core.UriInfo;

/**
 * po
 *
 * @author admin
 */
@RequestScoped
@Path("component/lot")
public class TblComponentLotResource {

    @Context
    private UriInfo context;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblComponentLotService tblComponentLotService;

    public TblComponentLotResource() {
    }

    /**
     * 在庫数登録画面で選択した部品の部品ロットを照会する
     *
     * @param componentId
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return an instance of TblComponentLotVoList
     */
    @GET
    @Path("{componentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblComponentLotVoList getTblComponentLotList(
            @PathParam("componentId") String componentId, //
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentLotService.getTblComponentLotList(componentId, "", 0, "", true, loginUser.getLangId(), sidx, sord, pageNumber, pageSize);
    }

    /**
     * 在庫数登録画面で選択した部品の部品ロットを検索して一覧表示する。。 部品コードを変更して検索検索して表示する。
     *
     * @param componentCode
     * @param status
     * @param remarks
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return an instance of TblComponentLotVoList
     */
    @GET
//    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblComponentLotVoList getTblComponentLotList(
            @QueryParam("componentCode") String componentCode, //
            @QueryParam("status") int status, //
            @QueryParam("remarks") String remarks, //
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentLotService.getTblComponentLotList("", componentCode, status, remarks, true, loginUser.getLangId(), sidx, sord, pageNumber, pageSize);
    }

    /**
     * 一覧に表示している内容をCSVファイルに出力する。
     *
     * @param componentCode
     * @param status
     * @param remarks
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getTblComponentLotCsv(
            @QueryParam("componentCode") String componentCode,
            @QueryParam("status") int status, //
            @QueryParam("remarks") String remarks //
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentLotService.getTblComponentLotCsv(componentCode, status, remarks, loginUser.getLangId(), loginUser.getUserUuid()
        );
    }

    /**
     * 画面で追加・変更・削除した内容を部品ロットテーブルへ更新する。
     *
     * @param tblComponentLotVoList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postTblComponentLot(
            TblComponentLotVoList tblComponentLotVoList
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentLotService.postTblComponentLot(tblComponentLotVoList, loginUser.getLangId(), loginUser.getUserUuid());
    }

    /**
     * 入庫
     *
     * @param tblComponentLotVo
     * @return an instance of BasicResponse
     */
    @POST
    @Path("store")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postTblComponentLotStore(TblComponentLotVo tblComponentLotVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentLotService.postTblComponentLotStoreDelivery(tblComponentLotVo, CommonConstants.STORE, loginUser);
    }

    /**
     * 出庫
     *
     * @param tblComponentLotVo
     * @return an instance of BasicResponse
     */
    @POST
    @Path("delivery")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postTblComponentLotDelivery(TblComponentLotVo tblComponentLotVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentLotService.postTblComponentLotStoreDelivery(tblComponentLotVo, CommonConstants.DELIVERY, loginUser);
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
    public ImportResultResponse postTblComponentLotCsvStore(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentLotService.postTblComponentLotCsvStoreDelivery(fileUuid, CommonConstants.STORE, loginUser);
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
    public ImportResultResponse postTblComponentLotCsvDelivery(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblComponentLotService.postTblComponentLotCsvStoreDelivery(fileUuid, CommonConstants.DELIVERY, loginUser);
    }

    /**
     * 部品ロットテーブルを検索して部品ロット番号を表示。
     *
     * @param componentId
     * @param componentLotNo
     * @return
     */
    @GET
    @Path("like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblComponentLotVoList likeComponentLotNo(
            @QueryParam("componentId") String componentId,
            @QueryParam("componentLotNo") String componentLotNo
    ) {
        return tblComponentLotService.getComponentLotCsv(componentLotNo, componentId, true);
    }

    /**
     * 部品ロットテーブルを検索して部品ロット番号を表示。
     *
     * @param componentId
     * @param componentLotNo
     * @return
     */
    @GET
    @Path("equal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblComponentLotVoList equalComponentLotNo(
            @QueryParam("componentId") String componentId,
            @QueryParam("componentLotNo") String componentLotNo
    ) {
        return tblComponentLotService.getComponentLotCsv(componentLotNo, componentId, false);
    }

    /**
     * 部品ロット番号採番する
     *
     * @param componentId
     * @return
     */
    @GET
    @Path("number")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblComponentLotVo getMaxLotNumber(@QueryParam("componentId") String componentId) {
        TblComponentLotVo tblComponentLotVo = new TblComponentLotVo();

        tblComponentLotVo.setLotNo(tblComponentLotService.makeNewLotNumber(componentId));
        return tblComponentLotVo;
    }

}
