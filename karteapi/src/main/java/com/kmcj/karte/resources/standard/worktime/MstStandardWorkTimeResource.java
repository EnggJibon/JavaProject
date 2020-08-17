/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.standard.worktime;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;

/**
 * 標準業務時間マスタリソース
 *
 * @author zds
 */
@RequestScoped
@Path("standard/worktime")
public class MstStandardWorkTimeResource {

    @Context
    ContainerRequestContext requestContext;

    @Inject
    MstDictionaryService mstDictionaryService;

    @Inject
    MstStandardWorkTimeService mstStandardWorkTimeService;

    private Logger logger = Logger.getLogger(MstStandardWorkTimeResource.class.getName());

    public MstStandardWorkTimeResource() {
    }

    /**
     * 標準業務時間マスタ情報を取得
     *
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MstStandardWorkTimeList getMstStandardWorkTimes() {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstStandardWorkTimeList response = mstStandardWorkTimeService.getMstStandardWorkTimes(loginUser);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);

        return response;
    }

    /**
     * 標準業務時間マスタ情報を更新
     *
     * @param mstStandardWorkTimes
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMstStandardWorkTimes(MstStandardWorkTimeList mstStandardWorkTimes) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();

        List<MstStandardWorkTime> replacement = new ArrayList<>();
        /*
         * 入力チェックおよび値設定
         */
        // リスト重複チェック
        for (int i = 0; i < mstStandardWorkTimes.getMstStandardWorkTime().size(); i++) {
            MstStandardWorkTime mstStandardWorkTime1 = mstStandardWorkTimes.getMstStandardWorkTime().get(i);
            if (mstStandardWorkTime1 != null) {
                if (!"system_default".equals(mstStandardWorkTime1.getId()) && !"1".equals(mstStandardWorkTime1.getOperationFlag())) {
                    for (int j = mstStandardWorkTimes.getMstStandardWorkTime().size() - 1; j > i; j--) {
                        MstStandardWorkTime mstStandardWorkTime2 = mstStandardWorkTimes.getMstStandardWorkTime().get(j);
                        if (mstStandardWorkTime2 != null) {
                            if (mstStandardWorkTime1.getCompanyId().equals(mstStandardWorkTime2.getCompanyId())
                                    && Objects.equals(mstStandardWorkTime1.getDepartment(), mstStandardWorkTime2.getDepartment())) {
                                response.setError(true);
                                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                                return response;
                            }
                        }
                    }
                }
            }
        }

        for (MstStandardWorkTime mstStandardWorkTime : mstStandardWorkTimes.getMstStandardWorkTime()) {
            //登録可能チェック
            if (mstStandardWorkTime != null) {
                if ("4".equals(mstStandardWorkTime.getOperationFlag())) {
                    mstStandardWorkTimeService.setCreateData(response, mstStandardWorkTime, loginUser);
                    if (response.isError()) {
                        logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                        return response;
                    }
                    replacement.add(mstStandardWorkTime);
                } // 更新可能チェック
                else if ("3".equals(mstStandardWorkTime.getOperationFlag())) {
                    response = mstStandardWorkTimeService.setUpdateData(response, mstStandardWorkTime, loginUser);
                    if (response.isError()) {
                        logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                        return response;
                    }
                    replacement.add(mstStandardWorkTime);
                } // 削除可能チェック
                else if ("1".equals(mstStandardWorkTime.getOperationFlag())) {
                    if (mstStandardWorkTime.getId() != null && !"".equals(mstStandardWorkTime.getId())) {
                        response = mstStandardWorkTimeService.setDeleteData(response, mstStandardWorkTime.getId(), loginUser);
                    } else {
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
                        return response;
                    }
                    if (response.isError()) {
                        logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                        return response;
                    }
                    replacement.add(mstStandardWorkTime);
                } else {
                    //nothing
                }
            }
        }

        /*
         * 一括反映
         */
        for (MstStandardWorkTime mstStandardWorkTime : replacement) {
            if ("4".equals(mstStandardWorkTime.getOperationFlag())) {
                mstStandardWorkTimeService.createMstStandardWorkTime(mstStandardWorkTime, loginUser);
            } else if ("3".equals(mstStandardWorkTime.getOperationFlag())) {
                mstStandardWorkTimeService.updateMstStandardWorkTime(mstStandardWorkTime, loginUser);
            } else if ("1".equals(mstStandardWorkTime.getOperationFlag())) {
                if (mstStandardWorkTime.getId() != null && !"".equals(mstStandardWorkTime.getId())) {
                    mstStandardWorkTimeService.deleteMstStandardWorkTime(mstStandardWorkTime.getId());
                }
            } else {
                //nothing
            }

        }
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 標準業務時間マスタ情報を取得
     *
     * @return
     */
    @GET
    @Path("byuser")
    @Produces(MediaType.APPLICATION_JSON)
    public MstStandardWorkTimeList getMstStandardWorkTimesByUser() {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstStandardWorkTimeList response = mstStandardWorkTimeService.getStandardWorkTimesByLoginUser(loginUser);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);

        return response;
    }

    /**
     * 標準業務時間マスタ情報を取得
     *
     * @param userId
     * @return
     */
    @GET
    @Path("byuserid")
    @Produces(MediaType.APPLICATION_JSON)
    public MstStandardWorkTimeList getMstStandardWorkTimesByUserId(
            @QueryParam("userId") String userId) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        MstStandardWorkTimeList response = mstStandardWorkTimeService.getStandardWorkTimesByUserId(userId);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);

        return response;
    }
}
