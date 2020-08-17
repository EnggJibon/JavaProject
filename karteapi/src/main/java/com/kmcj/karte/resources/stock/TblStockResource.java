/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.stock;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("stock")
public class TblStockResource {

    @Context
    private UriInfo context;

    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    MstProcedureService mstProcedureService;

    /**
     * Creates a new instance of TblStockResource
     */
    public TblStockResource() {
    }

    @Inject
    private TblStockService tblStockService;

    /**
     * 検索
     *
     * @param componentCode
     * @param isPurchasedPart
     * @param finalProcedureOnly
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblStockVoList getTblStockVoList(
            @QueryParam("componentCode") String componentCode,
            @QueryParam("isPurchasedPart") int isPurchasedPart,
            @QueryParam("finalProcedureOnly") int finalProcedureOnly,
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize // ページSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblStockService.getTblStockVoList(componentCode, isPurchasedPart, finalProcedureOnly, sidx, sord, pageNumber, pageSize, true, loginUser.getLangId());
    }

    /**
     * 在庫数を取得
     *
     * @param componentId
     * @return an instance of TblStockVo
     */
    @GET
    @Path("quantity")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblStockVo getTblStockQuantity(@QueryParam("componentId") String componentId) {
        return tblStockService.getTblStockQuantity(componentId);
    }

    /**
     * CSV出力
     *
     * @param componentCode
     * @param isPurchasedPart
     * @param finalProcedureOnly
     * @return an instance of TblStockVoList
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getTblStockVoListCsv(
            @QueryParam("componentCode") String componentCode,
            @QueryParam("isPurchasedPart") int isPurchasedPart,
            @QueryParam("finalProcedureOnly") int finalProcedureOnly
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblStockService.getTblStockVoListCsv(componentCode, isPurchasedPart, finalProcedureOnly, loginUser);
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
    public ImportResultResponse postTblStockVoListCsv(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblStockService.postTblStockVoListCsv(fileUuid, loginUser);
    }

    /**
     * 登録
     *
     * @return an instance of TblStockVoList update
     * @param tblStockVos
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postTblStockVoList(TblStockVoList tblStockVos) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblStockService.updateTblStock(tblStockVos, loginUser.getUserUuid());
    }
    
//    /**
//     * 入庫
//     *
//     * @return an instance of TblStockVo update
//     * @param tblStockVo
//     */
//    @POST
//    @Path("store")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public BasicResponse postTblStockStore(TblStockVo tblStockVo) {
//        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
//        return tblStockService.doTblStock(
//            tblStockVo.getComponentCode(),
//            tblStockVo.getProcedureCode(),
//            CommonConstants.STORE,
//            tblStockVo.getStockQuantity(),
//            tblStockVo.getStockChangeDate(),
//            null,
//            CommonConstants.SHIPMENT_NO,
//            loginUser.getUserUuid(),
//            loginUser.getLangId());
//    }
//    
//    /**
//     * 出庫
//     *
//     * @return an instance of TblStockVo update
//     * @param tblStockVo
//     */
//    @POST
//    @Path("delivery")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public BasicResponse postTblStockDelivery(TblStockVo tblStockVo) {
//        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
//        return tblStockService.doTblStock(
//            tblStockVo.getComponentCode(),
//            tblStockVo.getProcedureCode(),
//            CommonConstants.DELIVERY,
//            tblStockVo.getStockQuantity(),
//            tblStockVo.getStockChangeDate(),
//            null,
//            CommonConstants.SHIPMENT_NO,
//            loginUser.getUserUuid(),
//            loginUser.getLangId());
//    }
//    
//    /**
//     * CSV入庫
//     *
//     * @param fileUuid
//     * @return an instance of ImportResultResponse
//     */
//    @POST
//    @Path("store/{fileUuid}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public ImportResultResponse postTblStockStoreCsv(@PathParam("fileUuid") String fileUuid) {
//        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
//        return tblStockService.postTblStockStoreCsv(fileUuid, CommonConstants.STORE, loginUser);
//    }
//    
//    /**
//     * CSV入庫
//     *
//     * @param fileUuid
//     * @return an instance of ImportResultResponse
//     */
//    @POST
//    @Path("delivery/{fileUuid}")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public ImportResultResponse postTblStockDeliveryCsv(@PathParam("fileUuid") String fileUuid) {
//        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
//        return tblStockService.postTblStockStoreCsv(fileUuid, CommonConstants.DELIVERY, loginUser);
//    }

    /**
     * 在庫計上API
     * <P>
     * 在庫計上クラスと同じ処理をAPIとしてAjaxから呼び出しが出来るようにPATHの定義をして実装する。
     *
     * @param componentId
     * @param componentCode
     * @param procedureCode
     * @param stockType
     * @param quantity
     * @param stockChangeDate
     * @param lotNumber
     * @param shipmentFlg
     * @return an instance of BasicResponse
     */
    @GET
    @Path("record")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse doTblStock(@QueryParam("componentId") String componentId //部品コード
            , @QueryParam("componentCode") String componentCode //部品コード
            , @QueryParam("procedureCode") String procedureCode //工程番号
            , @QueryParam("stockType") int stockType //1>入庫：STORE, 2>出庫：DELIVERY, 3>入庫赤伝票：STORE-DISCARD, 4>出庫赤伝票：DELIVERY-DISCARD
            , @QueryParam("quantity") long quantity //数量
            , @QueryParam("stockChangeDate") String stockChangeDate //在庫変更日
            , @QueryParam("lotNumber") String lotNumber //製造ロット番号
            , @QueryParam("shipmentFlg") int shipmentFlg //出荷有無フラグ0：無し、1：有り
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstProcedure mstProcedure = mstProcedureService.getMstProcedureByComponentIdAndProcedureCode(componentId, procedureCode);
        return tblStockService.doTblStock(componentCode, mstProcedure, null, stockType, quantity, stockChangeDate, lotNumber, 0, null,shipmentFlg, null, loginUser.getUserUuid(), loginUser.getLangId());
    }
}
