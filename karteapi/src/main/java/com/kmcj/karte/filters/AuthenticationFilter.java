/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.filters;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.Credential;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.authentication.LoginUserStore;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.MstUserService;
import com.kmcj.karte.util.CSVLogFormat;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 *
 * @author f.kitaoji
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
@RequestScoped
public class AuthenticationFilter implements ContainerRequestFilter {
    
    @Inject
    private LoginUserStore loginUserStore;
    
    @Inject
    private MstUserService mstUserService;
    
    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        UriInfo uri = requestContext.getUriInfo();
        //List<Object> resourceList = uri.getMatchedResources();
        Map<String, Cookie> cookies  = requestContext.getCookies();
        /*
        System.out.println("cookies:" + cookies.toString());
        System.out.println("Method : " + requestContext.getMethod());
        System.out.println("Path : " + uri.getPath());
        System.out.println("Absollute Path : " + uri.getAbsolutePath());
        System.out.println("Base Uri : " + uri.getBaseUri());    
        System.out.println("Resource list");
        for (final Object resource : resourceList) {
            System.out.println("-Resource class : " + resource.getClass().getSimpleName());
        }
        System.out.println("APITOKEN:" + requestContext.getHeaderString("APITOKEN"));
        */
        
        // 操作ログ出力
        makeOpeLog(requestContext);

        //クッキーに言語コードがあれば取得
        String cookieLang = null;
        Cookie  tmpCookie = cookies.get(RequestParameters.LANG);
        if (tmpCookie != null) {
            cookieLang = tmpCookie.getValue();
        }
        //ログイン要求の場合はチェック不要（リソースクラス側で認証するため）
        if (uri.getPath().equals("authentication/login") ||
                uri.getPath().equals("authentication/extlogin") ||
                //ログイン画面文言取得の場合もチェック不要
                uri.getPath().equals("dictionary/getloginwords") ||
                //言語マスタ取得の場合もチェック不要
                uri.getPath().equals("language") ||
                //タブレットモジュールバージョンチェックは認証不要
                uri.getPath().equals("files/checkmoduleversion") ||
                //パスワード変更の場合もチェック不要(リソースクラス側で認証)
                uri.getPath().equals("authentication/changepwd") ||
                //パスワード忘れじのリセットも認証不要
                uri.getPath().startsWith("user/pwdselfreset/") ||
                //レポートクエリユーザーマスタ認証不要(リソースクラス側で認証)
                uri.getPath().startsWith("custom/report/query/user/login") ||
                //レポートクエリユーザーマスタ認証不要
                uri.getPath().startsWith("files/download/custom/report/") ||
                //ネイティブアプリバージョン取得APIは認証不要
                uri.getPath().startsWith("files/stocktake/appversion/")
            ) {
            // do nothing
        }
        else {
            String apiToken = requestContext.getHeaderString(RequestParameters.APITOKEN);
            //Request HeaderになければCookieも見る
            if (apiToken == null) {
                Cookie cookie = cookies.get(RequestParameters.APITOKEN);
                if (cookie != null) {
                    apiToken = cookie.getValue();
                }
            }
            String userid = requestContext.getHeaderString(RequestParameters.USERID);
            String password = requestContext.getHeaderString(RequestParameters.PASSWORD);
            if (password == null) password = "";
            BasicResponse resBody = new BasicResponse();
            //Tokenチェック
            if (apiToken != null) {
                LoginUser user = loginUserStore.getUser(apiToken); //LoginUserStore.getInstance().getUser(apiToken);
                if (user == null) {
                    //無効なTokenならAbort
                    resBody.setError(true);
                    resBody.setErrorCode(ErrorMessages.E103_INVALID_TOKEN);
                    resBody.setErrorMessage(mstDictionaryService.getDictionaryValue(cookieLang, "msg_error_invalid_token"));
                    //resBody.setErrorMessage(ErrorMessages.MSG103_INVALID_TOKEN);
                }
                else if (user.isExpired()) {
                    //有効期限切れならAbort
                    resBody.setError(true);
                    resBody.setErrorCode(ErrorMessages.E104_TOKEN_EXPIRED);
                    resBody.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_token_expired"));
                    //resBody.setErrorMessage(ErrorMessages.MSG104_TOKEN_EXPIRED);
                }
                else {
                    //有効なトークンなら有効期限を更新
                    user.updateExpireTime();
                    requestContext.setProperty(RequestParameters.LOGINUSER, user);
                }
                
            }
            //User ID, Passwordによる新規認証要求
            else if (userid != null) {
                    Credential credential = new Credential();
                    credential.setUserid(userid);
                    credential.setPassword(password);
                    MstUser mstUser = credential.validate(mstUserService);
                    if (credential.isValid()) {
                        LoginUser user = new LoginUser(
                                mstUser,
                                credential.getToken(),
                                loginUserStore.getSessionTimeoutMinutes());
                        loginUserStore.addUser(user);
                        //LoginUserStore.getInstance().addUser(user);
                        requestContext.setProperty(RequestParameters.APITOKEN, credential.getToken());
                        requestContext.setProperty(RequestParameters.LOGINUSER, user);
                    }
                    else {
                        resBody.setError(true);
                        resBody.setErrorCode(credential.getErrorCode());
                        resBody.setErrorMessage(credential.getErrorMessage());
                    }
            }   
            //TokenもUserID,Passwordもない場合
            else {
                resBody.setError(true);
                resBody.setErrorCode(ErrorMessages.E105_OPP_NOT_ALLOWED);
                resBody.setErrorMessage(mstDictionaryService.getDictionaryValue(cookieLang, "msg_error_opp_not_allowed"));
                //resBody.setErrorMessage(ErrorMessages.MSG105_OPP_NOT_ALLOWED);
            }
            if (resBody.isError()) {
                Response res = Response.ok(resBody).type(MediaType.APPLICATION_JSON).build();
                requestContext.abortWith(res);
            }
        }
        
    }

    private void makeOpeLog(ContainerRequestContext requestContext){
        // 操作ログ用ディレクトリ存在チェック → ディレクトリ作成
        StringBuilder filePath = new StringBuilder(kartePropertyService.getDocumentDirectory());
        if (!(filePath.toString().substring(filePath.toString().length() - 1).equals(FileUtil.SEPARATOR))){
            filePath.append(FileUtil.SEPARATOR);
        }
        filePath.append(CommonConstants.OPELOG);
        String filePathString = filePath.toString();
        File chkDir = new File(filePathString);
        if (!chkDir.exists()) {
            chkDir.mkdirs();
        }
        
        // 操作ログ用ログファイル存在チェック＆時刻チェック → ログファイル作成＆書き出し
        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHH");
        String logFileName = "opelog_api_" + sdf1.format(date) + ".log";
        filePath.append(FileUtil.SEPARATOR).append(logFileName);
        filePathString = filePath.toString();
        File chkFil = new File(filePathString);
        if (!chkFil.exists()) {
            chgOpeLog(requestContext, filePathString, DateFormat.dateToStrMill(date), false);
        } else {
            chgOpeLog(requestContext, filePathString, DateFormat.dateToStrMill(date), true);
        }
    }
    
    private void chgOpeLog(ContainerRequestContext requestContext, String filePath, String date, Boolean chgFlg){
        String apiToken = requestContext.getHeaderString(RequestParameters.APITOKEN);
        //Request HeaderになければCookieも見る
        if (apiToken == null) {
            Cookie cookie = requestContext.getCookies().get(RequestParameters.APITOKEN);
            if (cookie != null) {
                apiToken = cookie.getValue();
            }
        }
        final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());
        FileHandler fh = null;
        try {
            // 出力ファイルを指定する
            fh = new FileHandler(filePath, chgFlg);
            // 出力フォーマットを指定する
            fh.setFormatter(new CSVLogFormat());
            fh.setEncoding("UTF-8");
            logger.setUseParentHandlers(false);
            logger.addHandler(fh);
            // ログを出力する
            String userid = null;
            if (apiToken != null && !"".equals(apiToken)) {
                LoginUser user = loginUserStore.getUser(apiToken);
                if (user == null) {
                    return;
                }
                else if (user.isExpired() || user.isExternal()) { //APIユーザーは除外する
                    return;
                }
                else {
                    userid = user.getUserid();
                }
            } else {
                userid = requestContext.getHeaderString(RequestParameters.USERID);
            }

            if(userid != null) {
                String uriPath;
                try {
                    uriPath = java.net.URLDecoder.decode(requestContext.getUriInfo().getPath(), "UTF-8");
                } catch (Exception e) {
                    uriPath = requestContext.getUriInfo().getPath();
                }
                String uriParamString = "";
                MultivaluedMap<String, String> uriParams = requestContext.getUriInfo().getQueryParameters();
                for (Map.Entry<String, List<String>> bar : uriParams.entrySet()){
                    if(!"_".equals(bar.getKey())){
                        List varlist = bar.getValue();
                        if (!(uriParamString.equals(""))){
                            uriParamString = uriParamString + "&";
                        }
                        
                        try {
                            uriParamString = uriParamString + bar.getKey() + "=" + java.net.URLDecoder.decode(varlist.get(0).toString(), "UTF-8");
                        } catch (Exception e) {
                            uriParamString = uriParamString + bar.getKey() + "=" + varlist.get(0).toString();
                        }
                    }
                }
                logger.log(Level.INFO, "\"{0}\",\"{1}\",\"{2}\",\"{3}\"\r\n", new Object[]{date, userid, uriPath, uriParamString});
            }
        } catch (Exception e) {
            if (fh != null) {
                fh.close();
            }
            Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, e);
//            e.printStackTrace();
        } finally {
            if (fh != null) {
                logger.setUseParentHandlers(true);
                fh.close();
            }
        }
    }
}
