/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.location.history;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.MstMoldService;
import com.kmcj.karte.util.FileUtil;
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
 * @author admin
 */
@RequestScoped
@Path("mold/location/history")
public class TblMoldLocationHistoryResource {

    public TblMoldLocationHistoryResource() {

    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblMoldLocationHistoryService tblMoldLocationHistoryService;

    @Inject
    private MstMoldService mstMoldService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * 金型所在履歴照会 金型所在履歴マスタ複数取得
     *
     * @param moldId
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldLocationHistorys getMoldLocationHistories(@QueryParam("moldId") String moldId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        if (null == moldId || "".equals(moldId)) {
            TblMoldLocationHistorys resTblMoldLocationHistorys = new TblMoldLocationHistorys();
            return resTblMoldLocationHistorys;
        }

        TblMoldLocationHistorys tblMoldLocationHistoryList = tblMoldLocationHistoryService.getMoldLocationHistories(FileUtil.getDecode(moldId), loginUser);
        return tblMoldLocationHistoryList;
    }

    /**
     * 金型移動登録 保存ボタン 金型マスタを更新し、新たな金型移動履歴を作成する
     *
     * @param tblMoldLocationHistoryList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldLocationHistory(List<TblMoldLocationHistorys> tblMoldLocationHistoryList) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        if (tblMoldLocationHistoryList != null && tblMoldLocationHistoryList.size() > 0) {

            for (TblMoldLocationHistorys tblMoldLocationHistorys : tblMoldLocationHistoryList) {

                String moldId = tblMoldLocationHistorys.getMoldId();//金型ID

                if (!mstMoldService.getMstMoldExistCheck(moldId)) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                    return response;
                }

                tblMoldLocationHistoryService.creatMoldLocationHistoriesByChang(tblMoldLocationHistorys, loginUser, mstMoldService);
            }

        }
        return response;
    }

    /**
     * バッチで金型所在履歴テーブルデータを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extmoldlocationhistory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldLocationHistoryList getExtMoldLocationHistorysByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return tblMoldLocationHistoryService.getExtMoldLocationHistorysByBatch(latestExecutedDate, moldUuid);
    }
}
