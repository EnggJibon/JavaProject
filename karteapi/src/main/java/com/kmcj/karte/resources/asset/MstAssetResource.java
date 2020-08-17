/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.currency.MstCurrency;
import com.kmcj.karte.resources.currency.MstCurrencyService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("asset")
public class MstAssetResource {

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstAssetService mstAssetService;

    @Inject
    private MstCurrencyService mstCurrencyService;

    public MstAssetResource() {
    }

    /**
     * 資産マスタ複数取得
     *
     * @param assetNo
     * @param assetName
     * @param branchNo
     * @param moldId
     * @param machineId
     * @param mgmtCompanyCode
     * @param itemCode
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstAssetList getAssetList(@QueryParam("assetNo") String assetNo,
            @QueryParam("assetName") String assetName,
            @QueryParam("branchNo") String branchNo,
            @QueryParam("moldId") String moldId,
            @QueryParam("machineId") String machineId,
            @QueryParam("mgmtCompanyCode") String mgmtCompanyCode,
            @QueryParam("itemCode") String itemCode,
            @QueryParam("sidx") String sidx,//order Key
            @QueryParam("sord") String sord,//order 順
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("pageSize") int pageSize
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstAssetService.getMstAssetList(assetNo, assetName, moldId, machineId, mgmtCompanyCode, 
                pageNumber, pageSize, null, loginUser.getLangId(), false, true, itemCode, branchNo, sidx, sord);
    }

    /**
     * 資産マスタ複数取得 equals
     *
     * @param assetNo
     * @param branchNo
     * @param itemCode
     * @return
     */
    @GET
    @Path("equals")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstAssetList getMachines(
            @QueryParam("assetNo") String assetNo,
            @QueryParam("branchNo") String branchNo,
            @QueryParam("itemCode") String itemCode
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstAssetService.getMstAssetEqualsList(assetNo, branchNo, itemCode, loginUser.getLangId());
    }

    /**
     * 資産マスタ削除
     *
     * @param assetId
     * @return
     */
    @DELETE
    @Path("{assetId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteAsset(@PathParam("assetId") String assetId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstAssetService.deleteMstAsset(assetId, loginUser);
    }

    /**
     * 資産マスタ詳細取得
     *
     * @param assetId
     * @return
     */
    @GET
    @Path("detail/{assetId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstAssetList getMstAssetById(@PathParam("assetId") String assetId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstAssetService.getMstAssetList(null, null, null, null, null, 0, 0, assetId, loginUser.getLangId(), true, false, null, null, null, null);
    }

    /**
     * 資産マスタ追加
     *
     * @param mstAssetVo
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstAssetVo postAsset(MstAssetVo mstAssetVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstCurrency mstCurrency = mstCurrencyService.getDefaultCurrencyMstCurrency(mstAssetVo.getCurrencyCode());
        if (mstCurrency != null) {
            mstAssetVo.setCurrencyCode(mstCurrency.getCurrencyCode());
        }
        return mstAssetService.createMstAsset(mstAssetVo, null, loginUser, false);
    }

    /**
     * 資産マスタ更新
     *
     * @param mstAssetVo
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putAsset(MstAssetVo mstAssetVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstCurrency mstCurrency = mstCurrencyService.getDefaultCurrencyMstCurrency(mstAssetVo.getCurrencyCode());
        if (mstCurrency != null) {
            mstAssetVo.setCurrencyCode(mstCurrency.getCurrencyCode());
        }
        return mstAssetService.updateMstAsset(mstAssetVo, null, loginUser,false);
    }

    /**
     * 資産マスタCSV出力
     *
     * @param assetNo
     * @param assetName
     * @param moldId
     * @param machineId
     * @param mgmtCompanyCode
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getAssetCsvOutPut(@QueryParam("assetNo") String assetNo,
            @QueryParam("assetName") String assetName,
            @QueryParam("moldId") String moldId,
            @QueryParam("machineId") String machineId,
            @QueryParam("mgmtCompanyCode") String mgmtCompanyCode
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstAssetService.getAssetCsvOutPut(assetNo, assetName, moldId, machineId, mgmtCompanyCode, loginUser);
    }

    /**
     * 資産マスタCSV取込
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postAssetCsvt(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstCurrency mstCurrency = mstCurrencyService.getDefaultCurrencyMstCurrency(null);
        String currencyCode = "";
        if (mstCurrency != null) {
            currencyCode = mstCurrency.getCurrencyCode();
        }
        return mstAssetService.postAssetCsv(fileUuid, loginUser,currencyCode);
    }
    
    /**
     * 資産クラスを取得
     * <KM-336 棚卸実施登録で抽出条件追加>
     *
     * @return
     */
    @GET
    @Path("assetclass")
    @Produces(MediaType.APPLICATION_JSON)
    public MstAssetList getAssetClass() {
        return mstAssetService.getMstAssetClassList();
    }

}
