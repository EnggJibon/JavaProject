/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.detail;

import com.kmcj.karte.resources.mold.maintenance.remodeling.*;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.MstMoldDetail;
import com.kmcj.karte.resources.mold.MstMoldService;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
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
 * @author admin
 */
@RequestScoped
@Path("mold/maintenance")
public class TblMoldMaintenanceDetailResource {

    public TblMoldMaintenanceDetailResource() {

    }

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private TblMoldMaintenanceRemodelingService tblMoldMaintenanceRemodelingService;

    @Inject
    private TblMoldMaintenanceDetailService tblMoldMaintenanceDetailService;

    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private MstMoldService mstMoldService;

    /**
     * TT0007	金型名称と最新の金型改造データを取得し、一覧に表示する。（１コードまたはデータなし）
     *
     * @param moldId
     * @return
     */
    @GET
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceRemodelingVo getMoldMaintenanceDetail(@QueryParam("moldId") String moldId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return (TblMoldMaintenanceRemodelingVo) tblMoldMaintenanceRemodelingService.getMoldmainteOrRemodelDetail(moldId, CommonConstants.MAINTEORREMODEL_MAINTE, loginUser);

    }

    /**
     * TT0007	金型メンテナンス終了入力 金型メンテナンス詳細を取得
     *
     * @param id
     * @param moldId
     * @return
     */
    @GET
    @Path("detailes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceRemodelingVo getMoldMaintenanceDetails(@QueryParam("id") String id, @QueryParam("moldId") String moldId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldMaintenanceRemodelingService.getMoldmainteOrRemodelDetails(id, moldId, CommonConstants.MAINTEORREMODEL_MAINTE, loginUser);
    }

    /**
     * 
     *
     * @param moldMaintenanceRemodelingVo
     * @return
     */
    @POST
    @Path("detailes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldMaintenanceDetailes(TblMoldMaintenanceRemodelingVo moldMaintenanceRemodelingVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        Query query = entityManager.createNamedQuery("TblMoldMaintenanceRemodeling.findById");
        query.setParameter("id", moldMaintenanceRemodelingVo.getMaintenanceId());
        List list = query.getResultList();
        if(list != null && list.size() > 0){
            response = tblMoldMaintenanceDetailService.postMoldMaintenanceDetailes(moldMaintenanceRemodelingVo,CommonConstants.MAINTEORREMODEL_MAINTE, loginUser);
        }else{
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        return response;
    }

    /**
     * TT0007	画面から入力されたメンテナンス結果をデータベースに更新する
     *
     * @param moldMaintenanceRemodelingVo
     * @return
     */
    @POST
    @Path("startdetailes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldMaintenanceDetailes2(TblMoldMaintenanceRemodelingVo moldMaintenanceRemodelingVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        Query query = entityManager.createNamedQuery("TblMoldMaintenanceRemodeling.findById");
        query.setParameter("id", moldMaintenanceRemodelingVo.getMaintenanceId());
        List list = query.getResultList();
        if (list != null && list.size() > 0) {
            response = tblMoldMaintenanceDetailService.postMoldMaintenanceDetailes2(moldMaintenanceRemodelingVo, CommonConstants.MAINTEORREMODEL_MAINTE, loginUser);
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        return response;
    }
    
    
    /**
     * TT0007	金型メンテナンス終了入力 選択された金型メンテナンスの開始をなかったことにするため、データベースからレコードを削除する。
     *
     * @param moldId
     * @return
     */
    @PUT
    @Path("startcancel/{moldId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMaintenanceStartCancel(@PathParam("moldId") String moldId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldMaintenanceDetailService.startCancelMoldMaintenance(moldId, loginUser);
    }

     /**
     * TT0007 金型メンテナンス終了入力
     * 金型メンテナンスステータスチェックして
     * @param moldId
     * @return 
     */
    @GET
    @Path("checkmaintestatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse getCheckMainteStatus(@QueryParam("moldId") String moldId) {
        BasicResponse response = new BasicResponse();
        LoginUser user = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMoldDetail mstMoldDetail = mstMoldService.getMoldByMoldId(moldId, user.getLangId());
        if (mstMoldDetail != null && mstMoldDetail.getMstMold() != null && mstMoldDetail.getMstMold().getMainteStatus() != null ) {
            int mainteStatus = mstMoldDetail.getMstMold().getMainteStatus();
            if (mainteStatus == 0) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_end"));
            }
            
            if (mainteStatus == 2) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_mold_remodeling"));
            }
            
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_no_processing_record"));
        }
        return response;
    }
    
    /**
     * TT0007	金型メンテナンス終了入力 終了している金型メンテナンスのステータスをメンテナンス中に戻す
     *
     * @param moldId
     * @return
     */
    @PUT
    @Path("resumption")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putMaintenanceResumption(@QueryParam("moldId") String moldId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblMoldMaintenanceDetailService.resumptionMoldMaintenance(moldId, loginUser);
    }
    
    /**
     * バッチで金型メンテナンス詳細データを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extmoldmaintenancedetail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceDetailVo getExtMoldMaintenanceDetailsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return tblMoldMaintenanceDetailService.getExtMoldMaintenanceDetailsByBatch(latestExecutedDate, moldUuid);
    }
    
    /**
     * バッチで金型メンテナンス詳細イメージファイルデータを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extmoldmaintenancedetailimagefile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceDetailVo getExtMoldMaintenanceDetailImageFilesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return tblMoldMaintenanceDetailService.getExtMoldMaintenanceDetailImageFilesByBatch(latestExecutedDate, moldUuid);
    }
}
