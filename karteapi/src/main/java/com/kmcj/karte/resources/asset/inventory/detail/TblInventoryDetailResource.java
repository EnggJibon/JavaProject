/*
 * To ch
nge this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("inventory/detail")
public class TblInventoryDetailResource {

    /**
     * Creates a new instance of TblInventoryResource
     */
    public TblInventoryDetailResource() {
    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblInventoryDetailService tblInventoryDetailService;

    /**
     * T0023_資産棚卸回収結果登録_初期表示
     *
     * @param inventoryId
     * @param mgmtCompanyCode
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblInventoryDetailVoList getTblInventoryDetailList(
            @QueryParam("inventoryId") String inventoryId,
            @QueryParam("mgmtCompanyCode") String mgmtCompanyCode,
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblInventoryDetailService.getTblInventoryDetailList(inventoryId, mgmtCompanyCode, pageNumber, pageSize, loginUser);
    }

    /**
     * T0022_資産棚卸依頼先一覧_保存ボタン
     *
     * @param tblInventoryDetailVoList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postTblInventoryDetailList(TblInventoryDetailVoList tblInventoryDetailVoList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblInventoryDetailService.postTblInventoryDetailList(tblInventoryDetailVoList, loginUser);
    }
}
