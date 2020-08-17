/*
 * To ch
nge this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.request;

import com.kmcj.karte.BasicResponse;
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
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.ws.rs.PathParam;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("inventory/request")
public class TblInventoryRequestResource {

    /**
     * Creates a new instance of TblInventoryRequestResource
     */
    public TblInventoryRequestResource() {
    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblInventoryRequestService tblInventoryRequestService;

    /**
     * T0024_資産棚卸実依頼照会_検索ボタン
     *
     * @param answerFlg
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblInventoryRequestVoList getTblInventoryRequestList(@QueryParam("answerFlg") int answerFlg) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblInventoryRequestService.getTblInventoryRequestList(answerFlg, loginUser);
    }

    /**
     * T0024_資産棚卸実依頼照会_回答送信ボタン
     *
     * @param inventoryRequestId
     * @return
     */
    @POST
    @Path("send/{inventoryRequestId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse sendRequestTblInventoryRequest(@PathParam("inventoryRequestId") String inventoryRequestId) {

        TblInventoryRequest tblInventoryRequest = new TblInventoryRequest();

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        tblInventoryRequest.setUuid(inventoryRequestId);
        tblInventoryRequest.setStatus(CommonConstants.INVENTORY_REQUEST_STATUS_ANSWER_READY);
        tblInventoryRequest.setUpdateUserUuid(loginUser.getUserUuid());

        return tblInventoryRequestService.updTblInventoryRequestStatus(tblInventoryRequest, loginUser.getLangId());
    }

}
