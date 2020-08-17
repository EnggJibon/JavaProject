/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.location.history;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachineService;
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
@Path("machine/location/history")
public class TblMachineLocationHistoryResource {

    public TblMachineLocationHistoryResource() {

    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblMachineLocationHistoryService tblMachineLocationHistoryService;

    @Inject
    private MstMachineService mstMachineService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * 設備所在履歴照会 設備所在履歴マスタ複数取得
     *
     * @param machineId
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineLocationHistoryVo getMachineLocationHistories(@QueryParam("machineId") String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMachineLocationHistoryVo tblMachineLocationHistoryList = new TblMachineLocationHistoryVo();

        if (null == machineId || "".equals(machineId)) {
            return tblMachineLocationHistoryList;
        }
        tblMachineLocationHistoryList = tblMachineLocationHistoryService.getMachineLocationHistories(FileUtil.getDecode(machineId), loginUser);
        return tblMachineLocationHistoryList;
    }

    /**
     * 設備移動登録 保存ボタン 設備マスタを更新し、新たな設備移動履歴を作成する
     *
     * @param tblMachineLocationHistoryVoList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMachineLocationHistory(List<TblMachineLocationHistoryVo> tblMachineLocationHistoryVoList) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        if (tblMachineLocationHistoryVoList != null && tblMachineLocationHistoryVoList.size() > 0) {

            for (TblMachineLocationHistoryVo tblMachineLocationHistoryVo : tblMachineLocationHistoryVoList) {

                String machineId = tblMachineLocationHistoryVo.getMstMachine().getMachineId();//設備ID
                if (!mstMachineService.getMstMachineExistCheck(machineId)) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                    return response;
                }
                tblMachineLocationHistoryService.creatMachineLocationHistoriesByChang(tblMachineLocationHistoryVo, loginUser, mstMachineService);
            }
        }
        return response;
    }

    /**
     * バッチで設備所在履歴テーブルデータを取得
     *
     * @param latestExecutedDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachinelocationhistory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineLocationHistoryList getExtMachineLocationHistorysByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("machineUuid") String machineUuid) {
        return tblMachineLocationHistoryService.getExtMachineLocationHistorysByBatch(latestExecutedDate, machineUuid);
    }
}
