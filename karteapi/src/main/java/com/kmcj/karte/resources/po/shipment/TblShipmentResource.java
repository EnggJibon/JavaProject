/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.po.shipment;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.IndexsResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.production.TblProductionResource;
import com.kmcj.karte.resources.production.TblProductionService;
import com.kmcj.karte.resources.stock.TblStock;
import com.kmcj.karte.util.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import org.apache.commons.lang.StringUtils;

/**
 * po
 *
 * @author admin
 */
@RequestScoped
@Path("po/shipment")
public class TblShipmentResource {

    @Context
    private UriInfo context;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblShipmentService tblShipmentService;

    @Inject
    private TblProductionService tblProductionService;

    public TblShipmentResource() {
    }

    /**
     * 出荷登録 VB
     *
     * @param tblShipmentVo
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postTblShipment(TblShipmentVo tblShipmentVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblShipmentService.save(tblShipmentVo, loginUser.getUserUuid(), loginUser.getLangId());
    }

    /**
     * PO別生産実績照会画面で一覧を選択する時出荷情報を取得用
     *
     * @param poUuid
     * @param shipDateFrom
     * @param shipDateTo
     * @return
     */
    @GET
    @Path("{poUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblShipmentVoList getTblShipmentByPoid(
              @PathParam("poUuid") String poUuid
            , @QueryParam("shipDateFrom") String shipDateFrom// 出荷日From
            , @QueryParam("shipDateTo") String shipDateTo // 出荷日To
    ) {
        // 日付項目をDate型(yyyy/MM/dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        java.util.Date formatShipDateFrom = null;
        java.util.Date formatShipDateTo = null;
        try {
            if (StringUtils.isNotEmpty(shipDateFrom)) {
                formatShipDateFrom = sdf.parse(shipDateFrom);
            }

        } catch (ParseException ex) {
            Logger.getLogger(TblProductionResource.class.getName()).log(Level.WARNING, null, "日付形式不正 shipDateFrom[" + shipDateFrom + "]");
        }

        try {
            if (StringUtils.isNotEmpty(shipDateTo)) {
                formatShipDateTo = sdf.parse(shipDateTo);
            }

        } catch (ParseException ex) {
            Logger.getLogger(TblProductionResource.class.getName()).log(Level.WARNING, null, "日付形式不正 shipDateTo[" + shipDateTo + "]");
        }
        
        return tblShipmentService.getTblShipmentList(poUuid, formatShipDateFrom, formatShipDateTo);
    }

    /**
     * 出荷テーブル1件削除(ID指定)
     *
     * @param uuid
     * @return
     */
    @DELETE
    @Path("{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteTblShipment(@PathParam("uuid") String uuid) {

        return tblShipmentService.deleteTblShipment(uuid);
    }

    /**
     * 製造ロット番号もオートコンプリート用
     *
     * @param productionLotNumber
     * @return
     */
    @GET
    @Path("lotnumber/like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblShipmentVoList getLike(@QueryParam("productionLotNumber") String productionLotNumber) {

        return tblShipmentService.getList(productionLotNumber, true);
    }

    /**
     * 製造ロット番号もオートコンプリート用
     *
     * @param productionId
     * @return
     */
    @GET
    @Path("lotnumber/equal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblShipmentVoList getEqual(@QueryParam("productionId") String productionId) {

        return tblShipmentService.getList(productionId);
    }
    
    /**
     * 製造ロット関連付け(部品が選択されている場合は部品で絞り込まれたものが候補に現れる)
     *
     * @param componentId
     * @param productionLotNumber
     * @param componentInspectionResultId
     * @param companyId
     * @return
     */
    @GET
    @Path("lotnumber/component/like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblShipmentVoList getTblShipmentByComponentIdLike(
            @QueryParam("componentId") String componentId,
            @QueryParam("productionLotNumber") String productionLotNumber,
            @QueryParam("componentInspectionResultId") String componentInspectionResultId,
            @QueryParam("companyId") String companyId
    ) {

        return tblShipmentService.getTblShipmentByComponentIdLike(productionLotNumber,componentId,companyId,componentInspectionResultId);
    }

    @GET
    @Path("production/component/like")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MaterialList getMaterialLike(@QueryParam("componentId") String componentId,
                                        @QueryParam("productionLotNumber") String productionLotNumber) {
        return tblProductionService.getProductionListByComponentAndLot(componentId, productionLotNumber);
    }
    
    /**
     * 製造ロット関連付け(部品が選択されている場合は部品で絞り込まれたものが候補に現れる)
     *
     * @param componentId
     * @param productionLotNumber
     * @param componentInspectionResultId
     * @param companyId
     * @return
     */
    @GET
    @Path("lotnumber/component/equal")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblShipmentVoList getTblShipmentByComponentIdEqual(
            @QueryParam("componentId") String componentId,
            @QueryParam("productionLotNumber") String productionLotNumber,
            @QueryParam("componentInspectionResultId") String componentInspectionResultId,
            @QueryParam("companyId") String companyId
    ) {

        return tblShipmentService.getTblShipmentByComponentIdEqual(productionLotNumber,componentId,companyId,componentInspectionResultId);
    }
    
    /**
     * 製造ロット番号 オートコンプリート用 スマホ専用
     *
     * @param productionLotNumber
     * @param shipmentComponentCode
     * @return
     */
    @GET
    @Path("lotnumber")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblShipmentVoList getSpLotnumber(
            @QueryParam("productionLotNumber") String productionLotNumber, 
            @QueryParam("shipmentComponentId") String shipmentComponentId
    ) {
        return tblShipmentService.getListForSp(productionLotNumber, shipmentComponentId,true);
    }

    /**
     * 出荷登録部品在庫数量取得
     *
     * @param componentId
     * @return
     */
    @GET
    @Path("stock/{componentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblStock getSpStockQuality(
            @PathParam("componentId") String componentId
    ) {
        return tblShipmentService.getSpStockQuality(componentId);
    }
}
