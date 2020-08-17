/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.remodeling.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.mold.maintenance.remodeling.*;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.MstMoldDetail;
import com.kmcj.karte.resources.mold.MstMoldService;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetailService;
import com.kmcj.karte.util.FileUtil;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
 * @author admin
 */
@RequestScoped
@Path("mold/remodeling")
public class TblMoldRemodelingDetailResource {

    public TblMoldRemodelingDetailResource() {

    }

    @Inject
    private TblMoldRemodelingDetailService tblMoldRemodelingDetailService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private TblMoldMaintenanceRemodelingService tblMoldMaintenanceRemodelingService;
    
    @Inject
    private TblMoldMaintenanceDetailService tblMoldMaintenanceDetailService;
    
    @Inject
    private MstMoldService mstMoldService;
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    

    /**
     * T0004_金型改造登録_画面描画時
     *
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceRemodelingVo getRemodelings() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMoldMaintenanceRemodelingVo tblMoldMaintenanceRemodelingVo = tblMoldRemodelingDetailService.getMoldMaintenanceRemodelings(loginUser);
        return tblMoldMaintenanceRemodelingVo;
    }
    
    /**
     * T0004_金型改造登録_削除
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteRemodeling(@PathParam("id") String id) {
        LoginUser user = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse basicResponse = new BasicResponse();
        if (!tblMoldRemodelingDetailService.getRemodelingExistCheck(id)) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
        } else {
            tblMoldRemodelingDetailService.deleteRemodeling(user, id);
        }
        return basicResponse;
    }
    
    
    /**
     * TT0009 金型改造終了入力
     * 金型名称と最新の金型改造データを取得し、一覧に表示する。（１コードまたはデータなし）
     * 金型改造詳細を取得
     * @param moldId
     * @param mainteOrRemodel
     * @return 
     */
    @GET
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceRemodelingVo getMoldRemodelingDetail(@QueryParam("moldId") String moldId, @QueryParam("mainteOrRemodel") int mainteOrRemodel) {
        LoginUser user = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMoldMaintenanceRemodelingVo response = (TblMoldMaintenanceRemodelingVo)tblMoldMaintenanceRemodelingService.getMoldmainteOrRemodelDetail(moldId, CommonConstants.MAINTEORREMODEL_REMODEL, user);
        return response;
    }
    
    /**
     * T0008    金型メンテナンス詳細	
     * 金型改造詳細情報を取得
     * @param id
     * @return 
     */
    @GET
    @Path("detail/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceRemodelingVo getMaintenanceDetail(@PathParam("id") String id) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return  tblMoldMaintenanceRemodelingService.getMoldmainteOrRemodelDetails(id,null, CommonConstants.MAINTEORREMODEL_REMODEL, loginUser);
    }
    
    
    /**
     * TT0009 金型改造終了入力
     * 再開 終了している金型改造のステータスを改造中に戻す。
     * 終了していない金型改造の場合は、処理を行えない。
     * @param moldId
     * @param mainteOrRemodel
     * @return 
     */
    @PUT
    @Path("resumption")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putRemodelingResumption(@QueryParam("moldId") String moldId, @QueryParam("mainteOrRemodel") int mainteOrRemodel){
        
        BasicResponse response = new BasicResponse();
        LoginUser user = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        response = FileUtil.checkExternal(entityManager, mstDictionaryService, moldId, user);
        if(response.isError()){
            return response;
        }
        MstMoldDetail mstMoldDetail = mstMoldService.getMoldByMoldId(moldId, user.getLangId());
        if (null != mstMoldDetail && null != mstMoldDetail.getMstMold() && null != mstMoldDetail.getMstMold().getMainteStatus() && mstMoldDetail.getMstMold().getMainteStatus() == 0) {
            tblMoldRemodelingDetailService.putRemodelingResumption(moldId, CommonConstants.MAINTE_STATUS_REMODELING, user);
        } else if (null != mstMoldDetail && null != mstMoldDetail.getMstMold() && null != mstMoldDetail.getMstMold().getMainteStatus() && mstMoldDetail.getMstMold().getMainteStatus() == 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_mold_under_maintenance"));
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_not_end"));
        }
        return response;
    }
    
    
    /**
     * TT0009 金型改造終了入力
     * 開始取消 
     * 選択された金型改造の開始をなかったことにするため、データベースからレコードを削除する。
     * ただし、終了している金型改造の場合は、処理を行えない。
     * @param moldid
     * @return 
     */
    @PUT
    @Path("startcancel/{moldid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putRemodelingStartCancel(@PathParam("moldid") String moldid) {
        BasicResponse response = new BasicResponse();
        LoginUser user = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        response = FileUtil.checkExternal(entityManager, mstDictionaryService, moldid, user);
        if(response.isError()){
            return response;
        }
        MstMoldDetail mstMoldDetail = mstMoldService.getMoldByMoldId(moldid, user.getLangId());
        if (null != mstMoldDetail && null != mstMoldDetail.getMstMold() && null != mstMoldDetail.getMstMold().getMainteStatus() && mstMoldDetail.getMstMold().getMainteStatus() == 1) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_mold_under_maintenance"));
        } else if (null != mstMoldDetail && null != mstMoldDetail.getMstMold() && null != mstMoldDetail.getMstMold().getMainteStatus() && mstMoldDetail.getMstMold().getMainteStatus() == 2) {
           response =  tblMoldRemodelingDetailService.putRemodelingStartCancel(moldid, user);
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_can_not_cancel"));
        }
        return response;
    }
    
    /**
     * T0004_新仕様登録画面_画面描画時
     * TT0009_(終了登録画面)_への遷移する方法を確認_画面描画時（再開から再度、終了が指示された場合）										
     * @param moldId
     * @param id
     * @return 
     */
    @GET
    @Path("detailes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceRemodelingVo getMoldRemodelingDetailes(@QueryParam("moldId") String moldId, @QueryParam("id") String id) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldRemodelingDetailService.getTblMoldRemodelingDetailByMoldId(loginUser, moldId, id);
    }
    
    /**
     * T0004_TblMoldMaintenanceRemodelingDetail_画面で入力された情報をデータベースに登録する。									
     * @param tblMoldRemodelingDetailVo
     * @return
     */
    @POST
    @Path("result/registration")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldRemodelingResultRegistration(TblMoldRemodelingDetailVo tblMoldRemodelingDetailVo){
        LoginUser loginUser=(LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);  
        return tblMoldRemodelingDetailService.postMoldRemodelingResultRegistration(tblMoldRemodelingDetailVo,loginUser);
    }
    
    
    
    
    
    /**
     * TT0009 金型改造終了入力
     * 登録 
     * 金型改造内容を登録し、金型のメンテ状態を戻すために、データベースを更新する。 
     * @param tblMoldMaintenanceRemodelingVo
     * @return 
     */
    @POST
    @Path("detailes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldRemodelingDetailes(TblMoldMaintenanceRemodelingVo tblMoldMaintenanceRemodelingVo){

         LoginUser user = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
         BasicResponse response = tblMoldRemodelingDetailService.postMoldRemodelingDetailes(tblMoldMaintenanceRemodelingVo, user);
         return response;
    }
    
    /**
     * 
     * @param moldMaintenanceRemodelingVo
     * @return 
     */
    @POST
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldMaintenanceDetailes(TblMoldMaintenanceRemodelingVo moldMaintenanceRemodelingVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
         Query query = entityManager.createNamedQuery("TblMoldMaintenanceRemodeling.findById");
        query.setParameter("id", moldMaintenanceRemodelingVo.getMaintenanceId());
        List list = query.getResultList();
        if(list != null && list.size() > 0){
            response = tblMoldMaintenanceDetailService.postMoldMaintenanceDetailes(moldMaintenanceRemodelingVo,CommonConstants.MAINTEORREMODEL_REMODEL, loginUser);
        }else{
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        return response;
    }
    
    /**
     *
     * @param moldMaintenanceRemodelingVo
     * @return
     */
    @POST
    @Path("startdetailes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldMaintenanceDetailes2(TblMoldMaintenanceRemodelingVo moldMaintenanceRemodelingVo) {
        LoginUser user = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = tblMoldRemodelingDetailService.postMoldRemodelingDetailes2(moldMaintenanceRemodelingVo, user);
        return response;
    }
    
    /**
     * TT0009 金型改造終了入力
     * 金型改造ステータスチェックして
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
            
            if (mainteStatus == 1) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_mold_under_maintenance"));
            }
            
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_no_processing_record"));
        }
        return response;
    }
    
    /**
     * バッチで金型改造詳細データを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extmoldremodelingdetail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldRemodelingDetailVo getExtMoldRemodelingDetailsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return tblMoldRemodelingDetailService.getExtMoldRemodelingDetailsByBatch(latestExecutedDate, moldUuid);
    }
    
    /**
     * バッチで金型改造ImageFileデータを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extmoldremodelingdetailimagefile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldRemodelingDetailVo getExtMoldRemodelingDetailImageFilesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return tblMoldRemodelingDetailService.getExtMoldRemodelingDetailImageFilesByBatch(latestExecutedDate, moldUuid);
    }
}
