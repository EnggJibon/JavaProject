/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report.user;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.ErrorMessages;
import javax.ws.rs.PathParam;
import javax.ws.rs.PUT;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.custom.report.TblCustomReportQuery;
import com.kmcj.karte.resources.custom.report.TblCustomReportQueryService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.FileUtil;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang.StringUtils;

/**
 * レポートクエリユーザーマスタ リソース
 *
 * @author admin
 */
@RequestScoped
@Path("custom/report/query/user")
public class MstQueryUserResource {

    @Inject
    private TblCustomReportQueryService tblCustomReportQueryService;

    public MstQueryUserResource() {
    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstQueryUserService mstQueryUserService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * レポートクエリユーザーマスタ件数を取得する
     *
     * @param userId
     * @param userName
     * @param companyCode
     * @param companyName
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("userId") String userId,
            @QueryParam("userName") String userName,
            @QueryParam("companyCode") String companyCode,
            @QueryParam("companyName") String companyName) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstQueryUserService.getCountResponse(userId, userName, companyCode, companyName, loginUser.getLangId());
    }

    /**
     * レポートクエリユーザーマスタを取得する
     *
     * @param userId
     * @param userName
     * @param companyCode
     * @param companyName
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstQueryUserVoList getMstQueryUserList(@QueryParam("userId") String userId,
            @QueryParam("userName") String userName,
            @QueryParam("companyCode") String companyCode,
            @QueryParam("companyName") String companyName) {
        return mstQueryUserService.getMstQueryUserVoList(userId, userName, companyCode, companyName);
    }

    /**
     * IDを指定してレポートクエリユーザーマスタを取得し、編集する
     *
     * @param userId
     * @return an instance of MstQueryUserVo
     */
    @GET
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstQueryUserVo getSingleMstQueryUser(@PathParam("userId") String userId) {
        return mstQueryUserService.getSingleMstQueryUserVo(FileUtil.getDecode(userId));
    }

    /**
     * 既存のIDを指定してレポートクエリユーザーマスタを更新する
     *
     * @param mstQueryUser
     * @return an instance of BasicResponse
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putMstQueryUser(MstQueryUser mstQueryUser) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstQueryUserService.updateMstQueryUser(mstQueryUser, loginUser);
    }

    /**
     * 新規のAPIユーザーマスターを追加する
     *
     * @param mstQueryUser
     * @return an instance of BasicResponse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMstQueryUser(MstQueryUser mstQueryUser) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstQueryUserService.createMstQueryUser(mstQueryUser, loginUser);
    }

    /**
     * IDを指定してAPIユーザーマスターを1件削除する
     *
     * @param uuid
     * @return an instance of BasicResponse
     */
    @DELETE
    @Path("{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteMstQueryUser(@PathParam("uuid") String uuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstQueryUserService.deleteMstQueryUser(uuid, loginUser.getLangId());
    }

    /**
     * パスワードをリセットする
     *
     * @param mstQueryUser
     * @return an instance of MstQueryUserVo
     */
    @POST
    @Path("pwdreset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstQueryUserVo resetPassword(MstQueryUser mstQueryUser) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstQueryUserService.resetPassword(mstQueryUser, loginUser);
    }

    /**
     * 公開ユーザ一覧を取得
     *
     * @param　reportId
     * @return an instance of TblReportQueryUserVoList
     */
    @GET
    @Path("openuser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblReportQueryUserVoList getOpenUserList(@QueryParam("reportId") Long reportId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstQueryUserService.getOpenUserList(reportId, loginUser);
    }

    /**
     * 認証ダイアログ
     *
     * @param tblReportQueryUserVo
     * @return
     */
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse login(TblReportQueryUserVo tblReportQueryUserVo
    ) {
        
        if (null == tblReportQueryUserVo.getReportId() && StringUtils.isNotEmpty(tblReportQueryUserVo.getReportName())) {
            String reportName = FileUtil.getDecode(tblReportQueryUserVo.getReportName());
            int flag = tblCustomReportQueryService.checkReportName(reportName);
            if (2 == flag) {
                FileReponse fileReponse = new FileReponse();
                fileReponse.setError(true);
                fileReponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                fileReponse.setErrorMessage(mstDictionaryService.getDictionaryValue(tblReportQueryUserVo.getLangId(), "msg_report_name_multiple_records_found"));
                return fileReponse;
            } else if (1 == flag) {
                TblCustomReportQuery tblCustomReportQuery = tblCustomReportQueryService.getEntityByReportName(reportName);
                if (tblCustomReportQuery != null) {
                    tblReportQueryUserVo.setReportId(tblCustomReportQuery.getReportId());
                }
            }
        }

        return mstQueryUserService.validateQueryUser(tblReportQueryUserVo, tblCustomReportQueryService);
    }

}
