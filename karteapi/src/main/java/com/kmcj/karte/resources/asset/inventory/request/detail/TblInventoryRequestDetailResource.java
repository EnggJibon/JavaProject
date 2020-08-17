/*
 * To ch
nge this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.request.detail;

import com.kmcj.karte.BasicResponse;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.asset.inventory.request.detail.id.TblInventoryRequestDetailIdVoList;
import javax.ws.rs.QueryParam;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("inventory/request/detail")
public class TblInventoryRequestDetailResource {

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblInventoryRequestDetailService tblInventoryRequestDetailService;

    public TblInventoryRequestDetailResource() {
    }

    /**
     * T0025_資産棚卸実依頼明細_初期化
     *
     * @param inventoryRequestId
     * @return
     */
    @GET
    @Path("{inventoryRequestId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblInventoryRequestDetailVoList getTblInventoryRequestDetailList(@PathParam("inventoryRequestId") String inventoryRequestId) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblInventoryRequestDetailService.getTblInventoryRequestDetailList(inventoryRequestId, loginUser);
    }

    /**
     * T0025_資産棚卸実依頼明細_保存ボタン
     *
     * @param tblInventoryRequestDetailVoList
     * @return
     */
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse saveTblInventoryRequestDetail(TblInventoryRequestDetailVoList tblInventoryRequestDetailVoList) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblInventoryRequestDetailService.saveTblInventoryRequestDetail(tblInventoryRequestDetailVoList, loginUser);
    }

    /**
     * T0025_資産棚卸実依頼明細_リンクをクリックする
     * <P>
     * ダイアログで関連の金型、設備のID、名称、棚卸結果を一覧で表示。
     *
     * @param requestDetailId
     * @param moldMachineType
     * @return
     */
    @GET
    @Path("dialog")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblInventoryRequestDetailIdVoList getTblInventoryRequestDetail(
            @QueryParam("requestDetailId") String requestDetailId,
            @QueryParam("moldMachineType") String moldMachineType
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblInventoryRequestDetailService.getTblInventoryRequestDetailList(requestDetailId,moldMachineType,loginUser.getLangId());
    }

}
