/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.assetno;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author hangju
 */
@RequestScoped
@Path("mold/assetno")
public class MstMoldAssetNoResource {

    public MstMoldAssetNoResource() {
    }

    @Inject
    private MstMoldAssetNoService mstMoldAsseNoService;

    @Context
    private ContainerRequestContext requestContext;
    /**
     *
     * 金型資産番号 金型資産番号マスタ取得
     *
     * @param moldId
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstMoldAssetNoList getMoldAssetNos(@QueryParam("moldId") String moldId) {
     return mstMoldAsseNoService.getMoldAssetNos(moldId);
    }

    /**
     *
     * 金型資産番号 金型資産番号更新.追加・削除する
     *
     * @param mstMoldAssetNoList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldAssetNos(List<MstMoldAssetNos> mstMoldAssetNoList) {
       
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldAsseNoService.postMoldAssetNos(mstMoldAssetNoList, loginUser);
    }

}
