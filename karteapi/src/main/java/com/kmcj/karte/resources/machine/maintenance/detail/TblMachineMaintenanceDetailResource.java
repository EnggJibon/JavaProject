/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.maintenance.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodelingService;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodelingVo;
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
 * @author jiangxs
 */
@RequestScoped
@Path("machine/maintenance")
public class TblMachineMaintenanceDetailResource {
    
    public TblMachineMaintenanceDetailResource(){
        
    }
    
    @Inject
    private TblMachineMaintenanceDetailService tblMachineMaintenanceDetailService;
    
    @Inject
    private TblMachineMaintenanceRemodelingService tblMachineMaintenanceRemodelingService;
    
    @Context
    private ContainerRequestContext requestContext;
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    
    /**
     * 設備メンテナンス終了入力
     * 再開 終了している設備メンテナンスのステータスをメンテナンス中に戻す。
     * @param machineId
     * @return 
     */
    @PUT
    @Path("resumption")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putMaintenanceResumption(@QueryParam("machineId") String machineId){
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineMaintenanceDetailService.resumptionMachineMaintenance(machineId, loginUser);
    }
    
    
    /**
     * 設備メンテナンス終了入力 
     * 選択された設備メンテナンスの開始をなかったことにするため、データベースからレコードを削除する。
     * @param machineId
     * @return
     */
    @PUT
    @Path("startcancel/{machineId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMaintenanceStartCancel(@PathParam("machineId") String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineMaintenanceDetailService.startCancelMachineMaintenance(machineId, loginUser);
    }
    
    /**
     * 設備名称と最新の設備改造データを取得し、一覧に表示する。（１コードまたはデータなし）
     * @param machineId
     * @return 
     */
    @GET
    @Path("detail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceRemodelingVo getMachineMaintenanceDetail(@QueryParam("machineId") String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return (TblMachineMaintenanceRemodelingVo) tblMachineMaintenanceRemodelingService.getMachinemainteOrRemodelDetail(machineId, CommonConstants.MAINTEORREMODEL_MAINTE, loginUser);
    }
    
    
    /**
     * 設備メンテ終了入力
     * 設備ステータスチェックして
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
     * 設備メンテナンス終了入力 
     * 設備メンテナンス詳細を取得 設備改造詳細を取得
     * @param maintenanceId
     * @return
     */
    @GET
    @Path("detailes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceRemodelingVo getMachineMaintenanceDetails(@QueryParam("maintenanceId") String maintenanceId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineMaintenanceRemodelingService.getMachineMainteOrRemodelDetails(maintenanceId, CommonConstants.MAINTEORREMODEL_MAINTE, loginUser);
    }
    
    
    /**
     * 設備メンテナンス終了入力 登録
     * @param mahineMaintenanceRemodelingVo
     * @return 
     */
    @POST
    @Path("detailes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMachineMaintenanceDetailes(TblMachineMaintenanceRemodelingVo mahineMaintenanceRemodelingVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        Query query = entityManager.createNamedQuery("TblMachineMaintenanceRemodeling.findById");
        query.setParameter("id", mahineMaintenanceRemodelingVo.getId());
        List list = query.getResultList();
        if(list != null && list.size() > 0){
            response = tblMachineMaintenanceDetailService.postMachineMaintenanceDetailes(mahineMaintenanceRemodelingVo,CommonConstants.MAINTEORREMODEL_MAINTE, loginUser);
        }else{
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        return response;
    }
    
    /**
     * 設備メンテナンス開始入力 登録
     *
     * @param mahineMaintenanceRemodelingVo
     * @return
     */
    @POST
    @Path("startdetailes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMachineMaintenanceDetailes2(TblMachineMaintenanceRemodelingVo mahineMaintenanceRemodelingVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        Query query = entityManager.createNamedQuery("TblMachineMaintenanceRemodeling.findById");
        query.setParameter("id", mahineMaintenanceRemodelingVo.getId());
        List list = query.getResultList();
        if (list != null && list.size() > 0) {
            response = tblMachineMaintenanceDetailService.postMachineMaintenanceDetailes2(mahineMaintenanceRemodelingVo, CommonConstants.MAINTEORREMODEL_MAINTE, loginUser);
        } else {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }
        return response;
    }
    
    
    /**
     * バッチで設備メンテナンス詳細データを取得
     *
     * @param latestExecutedDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachinemaintenancedetail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceDetailVo getExtMachineMaintenanceDetailsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("machineUuid") String machineUuid) {
        return tblMachineMaintenanceDetailService.getExtMachineMaintenanceDetailsByBatch(latestExecutedDate, machineUuid);
    }
    
    /**
     * バッチで設備メンテナンス詳細イメージファイルデータを取得
     *
     * @param latestExecutedDate
     * @param machienUuid
     * @return
     */
    @GET
    @Path("extmachinemaintenancedetailimagefile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceDetailVo getExtMachineMaintenanceDetailImageFilesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("machienUuid") String machienUuid) {
        return tblMachineMaintenanceDetailService.getExtMachineMaintenanceDetailImageFilesByBatch(latestExecutedDate, machienUuid);
    }
}
