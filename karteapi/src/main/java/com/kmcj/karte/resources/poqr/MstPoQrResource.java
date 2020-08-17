/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.poqr;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
@Path("poqr")
public class MstPoQrResource {

    @Context
    private UriInfo context;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstPoQrService mstPoQrService;

    public MstPoQrResource() {
    }

    /**
     *
     * web画面初期化用
     * <Br/>
     * すべてのPOQRマスタを取得する
     *
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstPoQrVoList getPoQrList() {

        return mstPoQrService.getPoQrList(false);
    }

    /**
     *
     * PoQrマスタを一括更新する
     *
     * @param mstPoQrVos
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMstPoQrVoList(MstPoQrVoList mstPoQrVos) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstPoQrService.postPoQr(mstPoQrVos, loginUser.getUserUuid(), loginUser.getLangId());
    }

    /**
     *
     * 納品先がIS NOT NULLのレコードを取得する
     *
     * @return
     */
    @GET
    @Path("load")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstPoQrVoList getPoQrListByDeliveryDestIdIsNotNull() {

        return mstPoQrService.getPoQrList(true);
    }

}
