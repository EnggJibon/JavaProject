/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.po;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.production.TblProductionResource;
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
@Path("po")
public class TblPoResource {

    @Context
    private UriInfo context;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblPoService tblPoService;

    public TblPoResource() {
    }

    /**
     * PO別生産実績照会画面、検索ボタンを押下時
     *
     * @param orderNumber
     * @param deliveryDestName
     * @param componentCode
     * @param shipDateFrom
     * @param shipDateTo
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblPoVoList getTblPoList(
            @QueryParam("orderNumber") String orderNumber //発注番号
            , @QueryParam("deliveryDestName") String deliveryDestName//納品先
            , @QueryParam("componentCode") String componentCode// 部品コード
            , @QueryParam("shipDateFrom") String shipDateFrom// 出荷日From
            , @QueryParam("shipDateTo") String shipDateTo // 出荷日To
            , @QueryParam("sidx") String sidx // ソートキー
            , @QueryParam("sord") String sord // ソート順
            , @QueryParam("pageNumber") int pageNumber // ページNo
            , @QueryParam("pageSize") int pageSize // ページSize
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

        return tblPoService.getPoList(orderNumber, deliveryDestName, componentCode, formatShipDateFrom, formatShipDateTo, sidx, sord, pageNumber, pageSize);
    }

    /**
     * POテーブル1件削除(ID指定)
     *
     * @param uuid
     * @return
     */
    @DELETE
    @Path("{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deletePo(@PathParam("uuid") String uuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblPoService.deleteTblPo(uuid, loginUser);
    }

    /**
     * QR<BR>
     * 納品先、発注番号、アイテムナンバーでPOテーブルを検索し、すでに存在ずれば受注日、受注数量、納期は表示する。
     *
     * @param orderNumber
     * @param deliveryDestId
     * @param itemNumber
     * @return
     */
    @GET
    @Path("load")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblPoVo getTblPoByByUniqueKey(
                @QueryParam("orderNumber") String orderNumber //発注番号
            , @QueryParam("deliveryDestId") String deliveryDestId//納品先
            , @QueryParam("itemNumber") String itemNumber// アイテムナンバー

    ) {

        return tblPoService.load(orderNumber, deliveryDestId, itemNumber);
    }

}
