/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.authentication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.apiuser.MstApiUser;
import com.kmcj.karte.resources.apiuser.MstApiUserService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.password.policy.CnfPasswordPolicy;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.MstUserService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.SafeHashGenerator;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@RequestScoped
@Path("authentication")
public class AuthenticationResource {

    @Context
    private UriInfo context;
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private LoginUserStore loginUserStore;
    
    @Inject
    private MstUserService mstUserService;
    
    @Inject
    private MstApiUserService mstApiUserService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    /**
     * Creates a new instance of AuthenticationResource
     */
    public AuthenticationResource() {
    }

    @POST
    @Path("logoff")
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse doLogoff() {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        loginUserStore.deleteUser(loginUser.getApiToken());
        return response;
    }
    
    @POST
    @Path("extlogin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Credential doExtLogin(Credential credential, @QueryParam("lang") String langId) {
        MstApiUser mstApiUser = credential.validateExt(mstApiUserService);
        credential.setPassword("*****"); //Hide password
        if (credential.isValid()) {
            credential.setUserName(mstApiUser.getUserName());
            LoginUser user = new LoginUser(
                    mstApiUser,
                    credential.getToken(),
                    loginUserStore.getSessionTimeoutMinutesExt());
            //言語が明示的に送られて来たら上書きする
            if (langId != null) {
                user.setLangId(langId);
            }
            loginUserStore.addUser(user);
            credential.setLangId(user.getLangId());
        }
        else {
            String errorMessageDictKey;
            if (credential.getErrorCode().equals(ErrorMessages.E101_INVALID_USERID_PWD)) {
                errorMessageDictKey = "msg_error_invalid_userid_pwd";
            }
            else {
                errorMessageDictKey = "msg_error_pwd_expired";
            }
            String pLangId = null;
            if (langId != null) {
                pLangId = langId;
            }
            else if (mstApiUser != null) {
                pLangId = mstApiUser.getLangId();
            }
            String errorMessage = mstDictionaryService.getDictionaryValue(pLangId, errorMessageDictKey);
            credential.setErrorMessage(errorMessage);
        }
        return credential;
    }
    
    @GET
    @Path("whoami")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String whoAmI() {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        MstUser user = mstUserService.getMstUserByUuid(loginUser.getUserUuid());
        Gson gson = new GsonBuilder().setDateFormat(DateFormat.DATETIME_FORMAT).create();
        Map<String, Object> response = new HashMap<>();
        response.put("userUuid", loginUser.getUserUuid());
        response.put("userId", loginUser.getUserid());
        response.put("authId", loginUser.getAuthId());
        response.put("langId", loginUser.getLangId());
        response.put("department", loginUser.getDepartment());
        response.put("companyId", loginUser.getCompanyId());
        response.put("procCd", loginUser.getProcCd());
        response.put("userName", user == null ? "" : user.getUserName());
        return gson.toJson(response);
    }
    
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Credential doLogin(Credential credential, @QueryParam("lang") String langId) {
        MstUser mstUser = credential.validate(mstUserService);
        credential.setPassword("*****"); //Hide password
        if (mstUser != null && mstUser.getAccountLocked() == 1) {
            //アカウントロックされていたらエラー
            credential.setValid(false);
            credential.setError(true);
            credential.setErrorCode(ErrorMessages.E106_ACCOUNT_LOCKED);
        }
        if (credential.isValid()) {
            credential.setUserName(mstUser.getUserName());
            credential.setDepartment(mstUser.getDepartment());
            LoginUser user = new LoginUser(
                    mstUser,
                    credential.getToken(),
                    loginUserStore.getSessionTimeoutMinutes());
            //言語が明示的に送られて来たら上書きする
            if (langId != null) {
                user.setLangId(langId);
            }
            loginUserStore.addUser(user);
            credential.setLangId(user.getLangId());
            if (mstUser.getInitialPasswordFlg() != null && mstUser.getInitialPasswordFlg() == 1) {
                credential.setInitialPassword(true);
            }
            //ログイン失敗回数、アカウントロックをクリア
            mstUserService.updateLoginFailCountAndAccountLocked(mstUser, 0, false);
        }
        else {
            String errorMessageDictKey;
            if (credential.getErrorCode().equals(ErrorMessages.E101_INVALID_USERID_PWD)) {
                errorMessageDictKey = "msg_error_invalid_userid_pwd";
                if (mstUser != null) {
                    //パスワードポリシーを取得
                    CnfPasswordPolicy passwordPolicy = mstUserService.getCnfPasswordPolicyService().getCnfPasswordPolicySetting();
                    //ログイン失敗回数、アカウントロックを更新
                    int loginFailCount = mstUser.getLoginFailCount() + 1;
                    boolean accountLocked = false;
                    if (passwordPolicy.getFailCountToLock() > 0 && loginFailCount >= passwordPolicy.getFailCountToLock()) {
                        //パスワードポリシーにログイン失敗回数が定義されていてそれに達した場合はアカウントロック
                        accountLocked = true;
                    }
                    mstUserService.updateLoginFailCountAndAccountLocked(mstUser, loginFailCount, accountLocked);
                }
            }
            else if (credential.getErrorCode().equals(ErrorMessages.E106_ACCOUNT_LOCKED)) {
                errorMessageDictKey = "msg_error_account_locked";
            }
            else {
                errorMessageDictKey = "msg_error_pwd_expired";
            }
            String pLangId = null;
            if (langId != null) {
                pLangId = langId;
            }
            else if (mstUser != null) {
                pLangId = mstUser.getLangId();
            }
            String errorMessage = mstDictionaryService.getDictionaryValue(pLangId, errorMessageDictKey);
            credential.setErrorMessage(errorMessage);
        }
        return credential;
    }
    
    @POST
    @Path("changepwd")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Credential changePassword(Credential credential, @QueryParam("lang") String langId) {
        MstUser mstUser = mstUserService.getMstUser(credential.getUserid());
        //言語判定(優先順位 1:ログインユーザー、2:クエリパラメータ、3:ユーザーマスタ。全部NULLのときはシステムデフォルト言語)
        String pLangId = null;
        if (langId != null) {
            //パラメータから来ていたらそれを使う
            pLangId = langId;
        }
        else {
            if (mstUser != null && mstUser.getLangId() != null) {
                //ユーザーマスタの言語
                pLangId = mstUser.getLangId();
            }
        }
        LoginUser oldUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        if (oldUser != null) {
            //ログインユーザーがセッションにあるならログインユーザーの言語
            pLangId = oldUser.getLangId();
        }
        //IDチェック
        if (mstUser == null) {
            credential.setValid(false);
            credential.setError(true);
            credential.setErrorCode(ErrorMessages.E101_INVALID_USERID_PWD);
            credential.setErrorMessage(mstDictionaryService.getDictionaryValue(pLangId, "msg_error_invalid_userid_pwd"));
            credential.setPassword("*****"); //Hide password
            credential.setNewPassword("*****"); //Hide password
            return credential;
        }
        //PWチェック
        if (mstUser.getHashedPassword() != null && !mstUser.getHashedPassword().equals("")) {
            boolean passwordCorrect = SafeHashGenerator.getStretchedPassword(
                credential.getPassword(), credential.getUserid()).equals(mstUser.getHashedPassword());
            if (!passwordCorrect) {
                credential.setValid(false);
                credential.setError(true);
                credential.setErrorCode(ErrorMessages.E101_INVALID_USERID_PWD);
                credential.setErrorMessage(mstDictionaryService.getDictionaryValue(pLangId, "msg_error_invalid_userid_pwd"));
                credential.setPassword("*****"); //Hide password
                credential.setNewPassword("*****"); //Hide password
                return credential;
            }
        }
        //パスワードポリシー準拠チェック
        BasicResponse policyJudge = mstUserService.getCnfPasswordPolicyService().checkPassword(
            mstUser, credential.getNewPassword(), pLangId);
        if (policyJudge.isError()) {
            credential.setValid(false);
            credential.setError(true);
            credential.setErrorCode(policyJudge.getErrorCode());
            credential.setErrorMessage(policyJudge.getErrorMessage());
            credential.setPassword("*****"); //Hide password
            credential.setNewPassword("*****"); //Hide password
            return credential;
        }
        //パスワード変更実行
        credential.changePassword(mstUser, mstUserService);
        credential.setPassword("*****"); //Hide password
        credential.setNewPassword("*****"); //Hide password
        if (credential.isValid()) {
            LoginUser user = new LoginUser(
                    mstUser,
                    credential.getToken(),
                    loginUserStore.getSessionTimeoutMinutes());
            loginUserStore.addUser(user);
        }
        return credential;
    }
    
    
    /**
     * Retrieves representation of an instance of com.kmcj.karte.AuthenticationResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of AuthenticationResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
