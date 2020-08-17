/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.dictionary;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.language.MstLanguage;
import com.kmcj.karte.resources.language.MstLanguageService;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@Path("dictionary")
@RequestScoped
public class MstDictionaryResource {

    @Context
    private UriInfo context;
    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private MstLanguageService mstLanguageService;
    @Context
    private ContainerRequestContext requestContext;

    /**
     * Creates a new instance of MstDictionaryResource
     */
    public MstDictionaryResource() {
    }

    @GET
    @Path("getloginwords")
    @Produces(MediaType.APPLICATION_JSON)
    public MstDictionaryList getLoginWords(@QueryParam("lang") String langId) {
        if (langId == null) {
            MstLanguage defLanguage = mstLanguageService.getDefaultLanguage();
            if (defLanguage == null) {
                MstDictionaryList response = new MstDictionaryList();
                response.setError(true);
                response.setErrorCode(ErrorMessages.MSG901_OTHER);
                response.setErrorMessage("Default language was not found.");
                return response;
            }
            langId = defLanguage.getId();
        }
        List<String> dictKeyList = new ArrayList<>();
        dictKeyList.add("application_title");
        dictKeyList.add("user_id");
        dictKeyList.add("password");
        dictKeyList.add("login");
        dictKeyList.add("msg_confirm_exit");
        //PW有効期限切れのときにパスワード変更画面に遷移するため取得文言追加
        dictKeyList.add("msg_error_pwd_expired");
        dictKeyList.add("change_password");
        dictKeyList.add("save");
        dictKeyList.add("cancel");
        dictKeyList.add("current_password");
        dictKeyList.add("new_password");
        dictKeyList.add("ok");
        dictKeyList.add("confirm");
        dictKeyList.add("msg_password_updated");
        //パスワード忘れ時の文言追加
        dictKeyList.add("msg_contact_administrator");
        dictKeyList.add("forgot_password");
        dictKeyList.add("msg_send_initial_password");
        dictKeyList.add("msg_sent_initial_password");
        dictKeyList.add("msg_error_mail_address_not_found");
        dictKeyList.add("msg_set_new_password");
        dictKeyList.add("msg_change_initial_password");
        dictKeyList.add("send");

        return mstDictionaryService.getDictionaryList(langId, dictKeyList);
    }
    
    /**
     * Retrieves representation of an instance of com.kmcj.karte.resources.dictionary.MstDictionaryResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("key/{dictKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public MstDictionaryList getDictionary(@PathParam("dictKey") String dictKey) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstDictionaryService.getDictionary(loginUser.getLangId(), dictKey);
    }
    
    @POST
    @Path("getlist")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MstDictionaryList getDictionaryList(List<String> dictKeyList) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstDictionaryService.getDictionaryList(loginUser.getLangId(), dictKeyList);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MstDictionaryList getAllDictionary() {
        //LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstDictionaryService.getAllDictionary();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse updateDictionary(MstDictionaryList mstDictionaryList) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        try {
            mstDictionaryService.updateDictionary(mstDictionaryList, loginUser);
        }
        catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E301_DATABASE);
            response.setErrorMessage(e.toString());
        }
        return response;
    }

    @POST
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public BasicResponse updateBasicDictionary(MstDictionarySingle mstDictionarySingle) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        try {
            mstDictionaryService.updateDictionarySingle(mstDictionarySingle, loginUser);
        }
        catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E301_DATABASE);
            response.setErrorMessage(e.toString());
        }
        return response;
    }
    
    /**
     * PUT method for updating or creating an instance of MstDictionaryResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
