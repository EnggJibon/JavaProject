/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.remodeling.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.maintenance.detail.TblMachineMaintenanceDetailService;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodelingService;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodelingVo;
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
 * @author jiangxs
 */
@RequestScoped
@Path("machine/remodeling")
public class TblMachineRemodelingDetailResource {
    
    public TblMachineRemodelingDetailResource(){
        
    }
    
    @Inject
    private TblMachineRemodelingDetailService tblMachineRemodelingDetailService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private TblMachineMaintenanceRemodelingService tblMachineMaintenanceRemodelingService;
    
    @Inject
    private TblMachineMaintenanceDetailService tblMachineMaintenanceDetailService;   
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    
    /**
     * 設備改造終了入力
     * 設備名称と最新の設備改造データを取得し、一覧に表示する。（１コードまたはデータなし）
     * 設備改造詳細を取得
     * @param machineId
     * @param mainteOrRemodel
     * @return 
     */
    @GET
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceRemodelingVo getMachineRemodelingDetail(@QueryParam("machineId") String machineId, @QueryParam("mainteOrRemodel") int mainteOrRemodel) {
        LoginUser user = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMachineMaintenanceRemodelingVo response = (TblMachineMaintenanceRemodelingVo)tblMachineMaintenanceRemodelingService.getMachinemainteOrRemodelDetail(machineId, CommonConstants.MAINTEORREMODEL_REMODEL, user);
        return response;
    }
    
    /**
     * 設備メンテナンス詳細	
     * 設備改造詳細情報を取得
     * @param maintenanceid
     * @return 
     */
    @GET
    @Path("detail/{maintenanceid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceRemodelingVo getMaintenanceDetail(@PathParam("maintenanceid") String maintenanceid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return  tblMachineMaintenanceRemodelingService.getMachineMainteOrRemodelDetails(maintenanceid, CommonConstants.MAINTEORREMODEL_REMODEL, loginUser);
    }
    
    
    /**
     * 設備改造終了入力
     * 再開 終了している設備改造のステータスを改造中に戻す。
     * 終了していない設備改造の場合は、処理を行えない。
     * @param machineId
     * @param mainteOrRemodel
     * @return 
     */
    @PUT
    @Path("resumption")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putRemodelingResumption(@QueryParam("machineId") String machineId, @QueryParam("mainteOrRemodel") int mainteOrRemodel){
        
        BasicResponse response = new BasicResponse();
        LoginUser user = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        response = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, machineId, "", user);
        if(response.isError()){
            return response;
        }

        MstMachine mstMachine = entityManager.find(MstMachine.class, machineId);

        if (null != mstMachine) {

            // メンテ中であれば、再開キャンセル不可
            if (null != mstMachine.getMainteStatus() && mstMachine.getMainteStatus() == 1) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_mold_under_maintenance"));
            } else if (null != mstMachine.getMainteStatus() && mstMachine.getMainteStatus() == 2) {
                // すでに改造中であれば、再開キャンセル不可
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_not_end"));
            } else {
                tblMachineRemodelingDetailService.putRemodelingResumption(machineId, CommonConstants.MAINTE_STATUS_REMODELING, user);
            }
        } else {
             // 設備存在していない
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
        }
        
        
        return response;
    }
    
    
    /**
     * 設備改造終了入力
     * 開始取消 
     * 選択された設備改造の開始をなかったことにするため、データベースからレコードを削除する。
     * ただし、終了している設備改造の場合は、処理を行えない。
     * @param machineid
     * @return 
     */
    @PUT
    @Path("startcancel/{machineid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putRemodelingStartCancel(@PathParam("machineid") String machineid) {
        BasicResponse response = new BasicResponse();
        LoginUser user = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        response = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, machineid, "", user);
        if(response.isError()){
            return response;
        }
        MstMachine mstMachine = entityManager.find(MstMachine.class, machineid);
        
        if (null != mstMachine) {
            // メンテ中であれば、再開キャンセル不可
            if (null != mstMachine.getMainteStatus() && mstMachine.getMainteStatus() == 1) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_mold_under_maintenance"));
            } else if (null != mstMachine.getMainteStatus() && mstMachine.getMainteStatus() == 0) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_error_can_not_cancel"));
            } else {
                tblMachineRemodelingDetailService.putRemodelingStartCancel(machineid, user);
            }
        } else {
            // 設備存在していない
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
        }
         
        return response;
    }
    
    
    /**
     * 設備 改造終了入力
     * 登録 
     * 設備改造内容を登録し、設備のメンテ状態を戻すために、データベースを更新する。 
     * @param tblMachineMaintenanceRemodelingVo
     * @return 
     */
    @POST
    @Path("detailes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMachineRemodelingDetailes(TblMachineMaintenanceRemodelingVo tblMachineMaintenanceRemodelingVo){

         LoginUser user = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
         BasicResponse response = tblMachineRemodelingDetailService.postMachineRemodelingDetailes(tblMachineMaintenanceRemodelingVo, user);
         return response;
    }
    
    
    /**
     * 
     * @param machineMaintenanceRemodelingVo
     * @return 
     */
    @POST
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMachineMaintenanceDetailes(TblMachineMaintenanceRemodelingVo machineMaintenanceRemodelingVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
         Query query = entityManager.createNamedQuery("TblMachineMaintenanceRemodeling.findById");
        query.setParameter("id", machineMaintenanceRemodelingVo.getId());
        List list = query.getResultList();
        if(list != null && list.size() > 0){
            response = tblMachineMaintenanceDetailService.postMachineMaintenanceDetailes(machineMaintenanceRemodelingVo,CommonConstants.MAINTEORREMODEL_REMODEL, loginUser);
        }else{
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        return response;
    }
    
    
    /**
     * 改造開始詳細編集
     *
     * @param machineMaintenanceRemodelingVo
     * @return
     */
    @POST
    @Path("startdetailes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMachineMaintenanceDetailes2(TblMachineMaintenanceRemodelingVo machineMaintenanceRemodelingVo) {

        LoginUser user = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = tblMachineRemodelingDetailService.postMachineRemodelingDetailes2(machineMaintenanceRemodelingVo, user);
        return response;

    }
    
    
    
    /**
     * 設備改造終了入力
     * 設備改造ステータスチェックして
     * @param machineId
     * @return 
     */
    @GET
    @Path("checkmaintestatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse getCheckMainteStatus(@QueryParam("machineId") String machineId) {
        BasicResponse response = new BasicResponse();
        LoginUser user = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        MstMachine mstMachine = entityManager.find(MstMachine.class, machineId);
        if (mstMachine != null && mstMachine.getMainteStatus() != null ) {
            int mainteStatus = mstMachine.getMainteStatus();
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
     * 設備改造登録_画面描画時
     *
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceRemodelingVo getRemodelings() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMachineMaintenanceRemodelingVo tblMachineMaintenanceRemodelingVo = tblMachineRemodelingDetailService.getMachineMaintenanceRemodelings(loginUser);
        return tblMachineMaintenanceRemodelingVo;
    }
    
    
    /**
     * 設備改造登録_削除
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
        if (!tblMachineRemodelingDetailService.getRemodelingExistCheck(id)) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
        } else {
            tblMachineRemodelingDetailService.deleteRemodeling(user, id);
        }
        return basicResponse;
    }
    
    
    /**
     * 新仕様登録画面_画面描画時
     * (終了登録画面)_への遷移する方法を確認_画面描画時（再開から再度、終了が指示された場合）										
     * @param maintenanceId
     * @return 
     */
    @GET
    @Path("detailes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceRemodelingVo getMachineRemodelingDetailes(@QueryParam("maintenanceId") String maintenanceId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineRemodelingDetailService.getTblMachineRemodelingDetailByMachineId(loginUser, maintenanceId);
    }
    
    
    /**
     * TblMachineMaintenanceRemodelingDetail_画面で入力された情報をデータベースに登録する。									
     * @param tblMachineRemodelingDetailVo
     * @return
     */
    @POST
    @Path("result/registration")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMachineRemodelingResultRegistration(TblMachineRemodelingDetailVo tblMachineRemodelingDetailVo){
        LoginUser loginUser=(LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);  
        return tblMachineRemodelingDetailService.postMachineRemodelingResultRegistration(tblMachineRemodelingDetailVo,loginUser);
    }
    
    
    /**
     * バッチで設備改造詳細データを取得
     *
     * @param latestExecutedDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachineremodelingdetail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineRemodelingDetailVo getExtMoldRemodelingDetailsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("machineUuid") String machineUuid) {
        return tblMachineRemodelingDetailService.getExtMachineRemodelingDetailsByBatch(latestExecutedDate, machineUuid);
    }
    
    /**
     * バッチで設備改造ImageFileデータを取得
     *
     * @param latestExecutedDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachineremodelingdetailimagefile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineRemodelingDetailVo getExtMoldRemodelingDetailImageFilesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("machineUuid") String machineUuid) {
        return tblMachineRemodelingDetailService.getExtMachineRemodelingDetailImageFilesByBatch(latestExecutedDate, machineUuid);
    }
}
