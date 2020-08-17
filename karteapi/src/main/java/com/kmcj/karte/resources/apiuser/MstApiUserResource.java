/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.apiuser;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.UrlDecodeInterceptor;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.SafeHashGenerator;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
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
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author ksen
 */
@RequestScoped
@Path("apiuser")
public class MstApiUserResource {

    @Context
    private UriInfo context;

    @Inject
    private MstApiUserService mstApiUserService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private CnfSystemService cnfSystemService;

    /**
     * Creates a new instance of MstApiUserResource
     */
    public MstApiUserResource() {
    }

    /**
     * APIユーザーマスター件数を取得する
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
        CountResponse count = mstApiUserService.getApiMstUserCount(userId, userName, companyCode, companyName);
        CnfSystem cnf = cnfSystemService.findByKey("system", "max_list_record_count");
        long sysCount = Long.parseLong(cnf.getConfigValue());
        if (count.getCount() > sysCount) {
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            count.setError(true);
            count.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
            msg = String.format(msg, sysCount);
            count.setErrorMessage(msg);
        }
        return count;
    }


    /**
     * すべてのAPIユーザーマスターを取得する
     * @param userId
     * @param userName
     * @param companyCode
     * @param companyName
     * @return 
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstApiUserList getApiUsers(@QueryParam("userId") String userId,
            @QueryParam("userName") String userName,
            @QueryParam("companyCode") String companyCode,
            @QueryParam("companyName") String companyName) {
        return mstApiUserService.getMstApiUsers(userId, userName, companyCode, companyName);
    }

    /**
     * IDを指定してAPIユーザーマスターを編集する
     *
     * @param userId
     * @return an instance of MstApiUserList
     */
    @GET
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstApiUserVo getApiUser(@PathParam("userId") String userId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        String timeZone = loginUser.getJavaZoneId();
        return mstApiUserService.getSingleMstApiUser(FileUtil.getDecode(userId), timeZone);
    }

    /**
     * 既存のAPIユーザーマスターを更新する
     *
     * @param mstApiUser
     * @return an instance of BasicResponse
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putApiUser(MstApiUser mstApiUser) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        int cnt = mstApiUserService.updateMstApiUserByQuery(mstApiUser, loginUser);
        BasicResponse response = new BasicResponse();
        if (cnt < 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        }
        return response;
    }

    /**
     * 新規のAPIユーザーマスターを追加する
     *
     * @param mstApiUserVo
     * @return an instance of BasicResponse
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postApiUser(MstApiUserVo mstApiUserVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        if (mstApiUserService.getApiMstUser(mstApiUserVo.getUserId()) != null) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
        } else {
            String initialPassword = SafeHashGenerator.getStretchedPassword(mstApiUserVo.getUserId(), mstApiUserVo.getUserId());                    
            MstApiUser mstApiUser = new MstApiUser();
            mstApiUser.setUserId(mstApiUserVo.getUserId());
            mstApiUser.setUserName(mstApiUserVo.getUserName());
            mstApiUser.setMailAddress(mstApiUserVo.getMailAddress());
            mstApiUser.setTimezone(mstApiUserVo.getTimezone());
            mstApiUser.setLangId(mstApiUserVo.getLangId());
            mstApiUser.setCompanyId(mstApiUserVo.getCompanyId());            
            mstApiUser.setHashedPassword(initialPassword);            
            mstApiUser.setPasswordChangedAt(null);
            mstApiUser.setPasswordExpiresAt(null);
            mstApiUser.setValidFlg(Integer.parseInt(mstApiUserVo.getValidFlg()));
            mstApiUser.setCreateUserUuid(loginUser.getUserUuid());
            mstApiUser.setCreateDate(new java.util.Date());
            mstApiUser.setUpdateUserUuid(loginUser.getUserUuid());
            mstApiUser.setUpdateDate(new java.util.Date());
            
            mstApiUserService.createMstApiUser(mstApiUser);
        }
        return response;
    }

    /**
     * IDを指定してAPIユーザーマスターを1件削除する
     *
     * @param userId
     * @return an instance of MstUserList
     */
    @DELETE
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors({UrlDecodeInterceptor.class})
    public BasicResponse deleteApiUser(@PathParam("userId") String userId) {
        BasicResponse response = new BasicResponse();
        int cnt = mstApiUserService.deleteMstApiUser(userId);
        if (cnt < 1) {
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        }
        return response;
    }

    /**
     * パスワードをリセットする
     *
     * @param mstApiUser
     * @return an instance of BasicResponse
     */
    @POST
    @Path("pwdreset")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstApiUserList resetPassword(MstApiUser mstApiUser) {
        int cnt = mstApiUserService.resetPassword(mstApiUser);
        MstApiUserList response = new MstApiUserList();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (cnt < 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
        } else {
            mstApiUser.setDisplayTimeZone(loginUser.getJavaZoneId());
            mstApiUser.setHashedPassword(null);
            response.getMstApiUser().add(mstApiUser);
        }
        return response;
    }
}
