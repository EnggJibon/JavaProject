/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.match;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.asset.MstAssetList;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("asset/matching")
public class TblAssetMatchingResource {

    public TblAssetMatchingResource() {
    }

    @Inject
    private TblAssetMatchingService tblAssetMatchingService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * 画面描画
     *
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("init")
    public TblAssetMatchingResultVoList getInit() {
        return tblAssetMatchingService.getInit();
    }

    /**
     * 資産マスタから照合未確認のデータを一覧に表示。表示順は昇順。
     *
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("search")
    public MstAssetList getAssetMatchingResultList() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblAssetMatchingService.getNotMatchingAsset(loginUser.getLangId());
    }

    /**
     * 照合バッチ起動。バッチステータスをＤＢに保存して、
     *
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("batch")
    public BasicResponse postAssetMatchingBatch() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblAssetMatchingService.startMatchingBatch(loginUser.getLangId());
    }

    /**
     *
     * @param tblAssetMatchingResultVoList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("save")
    public BasicResponse postAsset(TblAssetMatchingResultVoList tblAssetMatchingResultVoList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblAssetMatchingService.postAsset(tblAssetMatchingResultVoList, loginUser);
    }

    /**
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("refresh")
    public TblAssetMatchingResultVoList refreshAssetMatching() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblAssetMatchingService.refreshAssetMatchingBatch(loginUser.getLangId());
    }

}
