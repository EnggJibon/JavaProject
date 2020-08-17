/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.disposal;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("asset/disposal")
public class TblAssetDisposalResource {

    public TblAssetDisposalResource() {
    }

    @Inject
    private TblAssetDisposalService tblAssetDisposalService;

    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * <P>
     * T0026_資産廃棄申請登録
     * <P>
     * T0027_資産廃棄申請受付登録
     * <P>
     * T0028_資産廃棄申請確認登録
     * <P>
     * T0029_資産廃棄申請AP確認登録
     * <P>
     * T0030_資産廃棄処理登録
     * <P>
     * 初期化処理及び画面表示項目制御を取得
     *
     * @param functionId
     * @param searchFlg
     *            (1：検索画面；0：詳細画面)
     * @return
     */
    @GET
    @Path("request/init")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblAssetDisposalRequestVo getAssetDisposalInit(@QueryParam("functionId") String functionId,
            @QueryParam("searchFlg") int searchFlg) {

        TblAssetDisposalRequestVo ｔblAssetDisposalRequestVo = new TblAssetDisposalRequestVo();

        // 画面タイトル制御ルールを取得する
        ｔblAssetDisposalRequestVo.setControlTitleStr(AssetDisposalControlConstant.assetDisposalControlConstantMap
                .get(CommonConstants.ASSET_DISPOSAL_REQUEST_REGISTRATION_TITLE + functionId));

        // 画面ボタン制御ルールを取得する
        ｔblAssetDisposalRequestVo.setControlButtonStr(AssetDisposalControlConstant.assetDisposalControlConstantMap
                .get(CommonConstants.ASSET_DISPOSAL_REQUEST_REGISTRATION_BUTTON + functionId));

        // 検索画面の場合
        if (1 == searchFlg) {

            // 検索条件制御ルールを取得する
            ｔblAssetDisposalRequestVo
                    .setControlSearchConditionStr(AssetDisposalControlConstant.assetDisposalControlConstantMap
                            .get(CommonConstants.ASSET_DISPOSAL_REQUEST_REGISTRATION_CONDITION + functionId));
        }

        // 画面一覧の制御ルールを取得する
        ｔblAssetDisposalRequestVo.setControlDetailItemStr(AssetDisposalControlConstant.assetDisposalControlConstantMap
                .get(CommonConstants.ASSET_DISPOSAL_REQUEST_REGISTRATION_DETAIL_ITEM + functionId));

        return ｔblAssetDisposalRequestVo;
    }

    /**
     * <P>
     * T0026_資産廃棄申請登録_検索ボタン
     * <P>
     * T0027_資産廃棄申請受付登録_検索ボタン
     * <P>
     * T0028_資産廃棄申請確認登録_検索ボタン
     * <p>
     * 
     * @param requestDateFrom
     * @param requestDateTo
     * @param requestNo
     * @param disposalCompletionBefore
     * @param status
     * @param assetNo
     * @param itemCode
     * @param functionId
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Path("request/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblAssetDisposalRequestVoList getAssetDisposalList(@QueryParam("requestDateFrom") String requestDateFrom, // 申請日FROM
            @QueryParam("requestDateTo") String requestDateTo, // 申請日TO
            @QueryParam("requestNo") String requestNo, // 申請番号
            @QueryParam("disposalCompletionBefore") int disposalCompletionBefore, // 廃棄処理完了前

            @QueryParam("status") int status, // ステータス

            @QueryParam("assetNo") String assetNo, // 資産番号
            @QueryParam("itemCode") String itemCode, // 品目コード

            @QueryParam("itemCode") String assetDisposalUuid, // 資産廃棄UUID

            @QueryParam("functionId") String functionId,

            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize// ページSize
    ) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        boolean toComapanyFlg = true;

        if (CommonConstants.ASSET_DISPOSAL_REQUEST_REGISTRATION.equals(functionId)) {

            toComapanyFlg = false;

        }

        return tblAssetDisposalService.getAssetDisposalList(requestDateFrom, requestDateTo, requestNo,
                disposalCompletionBefore, status, assetNo, itemCode, sidx, sord, pageNumber, pageSize, null,
                loginUser.getLangId(), toComapanyFlg, true, true);
    }

    /**
     * <P>
     * T0026_資産廃棄申請登録_詳細ボタン
     *
     * @param assetDisposalUuid
     * @return
     */
    @GET
    @Path("request/detail/{assetDisposalUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblAssetDisposalRequestVoList getAssetDisposalById(
            @PathParam("assetDisposalUuid") String assetDisposalUuid) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblAssetDisposalService.getAssetDisposalList(null, null, null, 0, 0, null, null, null, null, 0, 0,
                assetDisposalUuid, loginUser.getLangId(), null, false, true);
    }

    /**
     * <P>
     * 資産廃棄管理詳細画面へ遷移可否をチェックする
     *
     * @param assetDisposalUuid
     * @param functionId
     * @return
     * @throws Exception
     */
    @GET
    @Path("request/detailjump/{assetDisposalUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse chkAssetDisposalDetailJump(@PathParam("assetDisposalUuid") String assetDisposalUuid,
            @QueryParam("functionId") String functionId) throws Exception {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblAssetDisposalService.chkAssetDisposalDetailJump(assetDisposalUuid, functionId, loginUser.getLangId());
    }

    /**
     * <P>
     * T0026_資産廃棄申請登録_保存_(更新の場合)
     *
     *
     * @param tblAssetDisposalRequestVo
     * @return
     * @throws Exception
     */
    @POST
    @Path("request/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblAssetDisposalRequestVo putAssetDisposalRequest(TblAssetDisposalRequestVo tblAssetDisposalRequestVo)
            throws Exception {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        TblAssetDisposalRequestVo response = tblAssetDisposalService.updateTblAssetDisposalRequest(
                tblAssetDisposalRequestVo, loginUser.getUserid(), loginUser.getUserUuid(), loginUser.getLangId(), true);

        // 廃棄送信APIをコール
        if (StringUtils.isNotEmpty(tblAssetDisposalService.getSendFunctionId())
                && null != tblAssetDisposalService.getSendRequestUuid()) {

            tblAssetDisposalService.chkSendAssetDisposalMailInfo(tblAssetDisposalService.getSendFunctionId(),
                    tblAssetDisposalService.getSendRequestUuid());

        }

        return response;
    }

    /**
     * <p>
     * T0026_資産廃棄申請登録_保存_(追加の場合)
     *
     * @param tblAssetDisposalRequestVo
     * @return
     * @throws Exception
     */
    @POST
    @Path("request/insert")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblAssetDisposalRequestVo postTblAssetDisposalRequest(TblAssetDisposalRequestVo tblAssetDisposalRequestVo)
            throws Exception {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        TblAssetDisposalRequestVo response = tblAssetDisposalService.createTblAssetDisposalRequest(
                tblAssetDisposalRequestVo, loginUser.getUserid(), loginUser.getUserUuid(), true);

        // 廃棄送信APIをコール
        if (StringUtils.isNotEmpty(tblAssetDisposalService.getSendFunctionId())
                && null != tblAssetDisposalService.getSendRequestUuid()) {

            tblAssetDisposalService.chkSendAssetDisposalMailInfo(tblAssetDisposalService.getSendFunctionId(),
                    tblAssetDisposalService.getSendRequestUuid());

        }

        return response;
    }

    /**
     * <p>
     * T0027_資産廃棄申請受付登録_申請フォーム出力
     *
     * @param functionId
     *
     * @return
     * @throws Exception
     */
    @GET
    @Path("request/form/export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getExcelFormExport(@QueryParam("functionId") String functionId) throws Exception {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblAssetDisposalService.getExcelFormExport(functionId, loginUser.getLangId(), loginUser.getUserUuid());
    }

    /**
     * <p>
     * T0027_資産廃棄申請受付登録_申請フォーム取込
     *
     * @return
     * @throws Exception
     */
    @POST
    @Path("request/form/import/{functionId}/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postExcelFormImport(@PathParam("functionId") String functionId,
            @PathParam("fileUuid") String fileUuid) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        ImportResultResponse response = new ImportResultResponse();
        
        try {

            response = tblAssetDisposalService.postExcelFormImport(fileUuid, functionId, loginUser.getUserid(),
                    loginUser.getUserUuid(), loginUser.getLangId());

        } catch (Exception e) {

            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(),
                    "msg_error_upload_file_type_invalid"));
            return response;
        }
        
        // 廃棄送信APIをコール
        if (StringUtils.isNotEmpty(tblAssetDisposalService.getSendFunctionId())
                && null != tblAssetDisposalService.getSendRequestUuid()) {

            tblAssetDisposalService.chkSendAssetDisposalMailInfo(tblAssetDisposalService.getSendFunctionId(),
                    tblAssetDisposalService.getSendRequestUuid());

        }

        return response;
    }

    /**
     * <p>
     * T0027_資産廃棄申請受付登録_EXCEL出力
     *
     * @return
     * @throws Exception
     */
    @GET
    @Path("request/excel/export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getExcelExport(@QueryParam("requestDateFrom") String requestDateFrom, // 申請日FROM
            @QueryParam("requestDateTo") String requestDateTo, // 申請日TO
            @QueryParam("requestNo") String requestNo, // 申請番号
            @QueryParam("disposalCompletionBefore") int disposalCompletionBefore, // 廃棄処理完了前

            @QueryParam("status") int status, // ステータス

            @QueryParam("assetNo") String assetNo, // 資産番号
            @QueryParam("itemCode") String itemCode, // 品目コード
            @QueryParam("functionId") String functionId// 品目コード
    ) throws Exception {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        TblAssetDisposalRequestVoList tblAssetDisposalRequestVoList = tblAssetDisposalService.getAssetDisposalList(
                requestDateFrom, requestDateTo, requestNo, disposalCompletionBefore, status, assetNo, itemCode, null,
                null, 0, 0, null, loginUser.getLangId(), true, false, false);

        return tblAssetDisposalService.getExcelExport(functionId,
                tblAssetDisposalRequestVoList.getTblAssetDisposalRequestVos(), loginUser.getLangId(),
                loginUser.getUserUuid());
    }

    /**
     * <p>
     * T0027_資産廃棄申請受付登録_EXCEL取込
     *
     * @return
     * @throws Exception
     */
    @POST
    @Path("request/excel/import/{functionId}/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postExcelImport(@PathParam("functionId") String functionId,
            @PathParam("fileUuid") String fileUuid){

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        ImportResultResponse response = new ImportResultResponse();

        try {

            response = tblAssetDisposalService.postExcelImport(fileUuid, functionId, loginUser.getUserid(),
                    loginUser.getUserUuid(), loginUser.getLangId());

        } catch (Exception e) {

            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(),
                    "msg_error_upload_file_type_invalid"));
            return response;
        }

        // 廃棄送信APIをコール
        if (StringUtils.isNotEmpty(tblAssetDisposalService.getSendFunctionId())
                && null != tblAssetDisposalService.getSendRequestUuid()) {

            tblAssetDisposalService.chkSendAssetDisposalMailInfo(tblAssetDisposalService.getSendFunctionId(),
                    tblAssetDisposalService.getSendRequestUuid());

        }

        return response;
    }

    /**
     * <p>
     * T0030_資産廃棄処理登録_廃棄データ出力
     * 
     * @param functionId
     *
     * @return
     * @throws Exception
     */
    @GET
    @Path("request/data/export")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getDataExport(@QueryParam("functionId") String functionId) throws Exception {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblAssetDisposalService.getAssetDisposalExport(functionId, loginUser.getLangId(),
                loginUser.getUserUuid());
    }

    /**
     * <p>
     * T0030_資産廃棄処理登録_廃棄データ再出力
     * 
     * @param functionId
     * @param completeDateFrom
     * @param completeDateTo
     *
     * @return
     * @throws Exception
     */
    @GET
    @Path("request/data/exportagain")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getDataExportAgain(@QueryParam("functionId") String functionId,
            @QueryParam("completeDateFrom") String completeDateFrom,
            @QueryParam("completeDateTo") String completeDateTo) throws Exception {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblAssetDisposalService.getAssetDisposalExportAgain(functionId, completeDateFrom, completeDateTo,
                loginUser.getLangId(), loginUser.getUserUuid());
    }

    /**
     * <p>
     * サプライヤーから未送信の資産廃棄データを取得
     *
     * @return
     */
    @GET
    @Path("extdata/get")
    @Produces(MediaType.APPLICATION_JSON)
    public TblAssetDisposalRequestVoList getExternalAssetDisposalList() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblAssetDisposalService.getExternalAssetDisposalList(loginUser);
    }

    /**
     * <p>
     * サプライヤーから未送信が資産所有会社サーバーに送信完了
     *
     * @param tblAssetDisposalRequestVoList
     * @return
     */
    @POST
    @Path("extdata/push")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse pushExternalAssetDisposalList(TblAssetDisposalRequestVoList tblAssetDisposalRequestVoList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblAssetDisposalService.pushGetExternalAssetDisposalByBatch(tblAssetDisposalRequestVoList, loginUser);
    }

    /**
     * <p>
     * アクションにより使用会社の社外ステータスを更新する
     *
     * @param tblAssetDisposalRequest
     * @return
     */
    @POST
    @Path("extdata/push/one")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse pushExternalAssetDisposal(TblAssetDisposalRequest tblAssetDisposalRequest) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblAssetDisposalService.pushGetExternalAssetDisposalByBatch(tblAssetDisposalRequest, loginUser);
    }
}
