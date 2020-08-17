/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.external;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.FileUtil;
import java.util.ArrayList;
import java.util.List;
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
 * @author jiangxs
 */
@RequestScoped
@Path("external")
public class MstExternalDataGetSettingResource {
    
    public MstExternalDataGetSettingResource(){
        
    }
    
    @Inject
    private MstExternalDataGetSettingService mstExternalDataGetSettingService;
    
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * 外部データ取得設定
     * 外部データ取得設定テーブルから全件データを取得し、会社コードの昇順で表示する。
     * @param companyCode
     * @param companyName
     * @return 
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstExternalDataGetSettingList getExternalDataGetSettings(@QueryParam("companyCode")String companyCode,
                                                                        @QueryParam("companyName")String companyName){
        List<MstExternalDataGetSetting> mstExternalDataGetSetting = mstExternalDataGetSettingService.getExternalDataGetSettings(companyCode, companyName);
        MstExternalDataGetSettingList response = new MstExternalDataGetSettingList();
        
        List<MstExternalDataGetSettingVo> mstExternalDataGetSettingVoList = new ArrayList<>();
        MstExternalDataGetSettingVo mstExternalDataGetSettingVo;
        for(int i=0;i<mstExternalDataGetSetting.size();i++){
            MstExternalDataGetSetting input = mstExternalDataGetSetting.get(i);
            mstExternalDataGetSettingVo = new MstExternalDataGetSettingVo();
            mstExternalDataGetSettingVo.setId(input.getId());
            mstExternalDataGetSettingVo.setCompanyId(input.getCompanyId() == null ? "" : input.getCompanyId());
            if(input.getMstCompany() != null ){
                mstExternalDataGetSettingVo.setCompanyCode(input.getMstCompany().getCompanyCode());
                mstExternalDataGetSettingVo.setCompanyName(input.getMstCompany().getCompanyName());
            }else{
                mstExternalDataGetSettingVo.setCompanyCode("");
                mstExternalDataGetSettingVo.setCompanyName("");
            }
            mstExternalDataGetSettingVo.setUserId(input.getUserId() == null ? "" : input.getUserId());
            mstExternalDataGetSettingVo.setEncryptedPassword(input.getEncryptedPassword() == null ? "" : input.getEncryptedPassword());
            mstExternalDataGetSettingVo.setApiBaseUrl(input.getApiBaseUrl() == null ? "" : input.getApiBaseUrl());
            mstExternalDataGetSettingVo.setValidFlg(input.getValidFlg() == null ? 0 : input.getValidFlg());
            mstExternalDataGetSettingVo.setLatestExecutedDate(input.getLatestExecutedDate());
            mstExternalDataGetSettingVoList.add(mstExternalDataGetSettingVo);
        }
        response.setMstExternalDataGetSettingVoList(mstExternalDataGetSettingVoList);
        return response;
    }
    
    
    /**
     * 外部データ取得設定
     * 外部データ取得情報を変更
     * @param id
     * @return 
     */
    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstExternalDataGetSettingVo getExternalDataGetSetting(@PathParam("id") String id){
        MstExternalDataGetSettingVo response = new MstExternalDataGetSettingVo();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if(mstExternalDataGetSettingService.checkExternal(id)){
            MstExternalDataGetSetting mstExternalDataGetSetting = mstExternalDataGetSettingService.getExternalDataGetSetting(id);
            response.setCompanyId(mstExternalDataGetSetting.getCompanyId() == null ? "" : mstExternalDataGetSetting.getCompanyId());
            if(mstExternalDataGetSetting.getMstCompany() != null){
                response.setCompanyName(mstExternalDataGetSetting.getMstCompany().getCompanyName());
            }else{
                response.setCompanyName("");
            }
            response.setApiBaseUrl(mstExternalDataGetSetting.getApiBaseUrl() == null ? "" : mstExternalDataGetSetting.getApiBaseUrl());
            response.setUserId(mstExternalDataGetSetting.getUserId() == null ? "" : mstExternalDataGetSetting.getUserId());
            response.setEncryptedPassword(mstExternalDataGetSetting.getEncryptedPassword() == null ? "" : FileUtil.decrypt(mstExternalDataGetSetting.getEncryptedPassword()));
            response.setValidFlg(mstExternalDataGetSetting.getValidFlg() == null ? 0 : mstExternalDataGetSetting.getValidFlg());
            response.setLatestExecutedDate(mstExternalDataGetSetting.getLatestExecutedDate());
        }else{
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_no_processing_record"));
        }
        return response;
    }
    
    /**
     * 外部データ取得設定
     * 外部データ取得情報を削除
     * @param id
     * @return 
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteExternalDataGetSetting(@PathParam("id") String id) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        if (mstExternalDataGetSettingService.checkExternal(id)) {
            //削除
            mstExternalDataGetSettingService.deleteExternalDataGetSetting(id);
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        }
        return response;
    }
    

    /**
     * 外部データ取得設定 接続テスト
     * @param userid
     * @param password
     * @param url
     * @return 
     */
    @GET
    @Path("verificationurl")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse testExternalDataGetSettingUrl(@QueryParam("userid") String userid,
            @QueryParam("password") String password, @QueryParam("url") String url) {
      
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstExternalDataGetSettingService.testExternalDataGetSettingUrl(userid,password,url,loginUser.getLangId());
     
    }

    /**
     * 外部データ取得設定
     * 入力された外部データ取得設定をデータベースに反映する。パスワードは暗号化して保存する。
     * 追加の場合
     * @param mstExternalDataGetSettingVo
     * @return 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postExternalDataGetSetting(MstExternalDataGetSettingVo mstExternalDataGetSettingVo){
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        mstExternalDataGetSettingService.postExternalDataGetSetting(mstExternalDataGetSettingVo,loginUser);
        return response;
    }
    
    /**
     * 外部データ取得設定
     * 入力された外部データ取得設定をデータベースに反映する。パスワードは暗号化して保存する。
     * 編集の場合
     * @param mstExternalDataGetSettingVo
     * @return 
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putExternalDataGetSetting(MstExternalDataGetSettingVo mstExternalDataGetSettingVo){
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);    
        
        if(mstExternalDataGetSettingService.checkExternal(mstExternalDataGetSettingVo.getId())){
            //更新
            mstExternalDataGetSettingService.putExternalDataGetSetting(mstExternalDataGetSettingVo, loginUser);
        }else{
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_data_modified"));
        }
        return response;
    }
    
    
    /**
     * 更新前回取得日時
     * @param id
     * @return 
     */
    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putLatestExecutedDateNull(@PathParam("id") String id){
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);  
        if(mstExternalDataGetSettingService.checkExternal(id)){
            response = mstExternalDataGetSettingService.putLatestExecutedDateNull(id,loginUser);
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return response;
        }
        return response;
    }
}
