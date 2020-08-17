/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.maintenance.remodeling;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailVo;
import com.kmcj.karte.resources.mold.issue.TblIssueVo;
import com.kmcj.karte.util.FileUtil;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("machine/maintenance/remodeling")
public class TblMachineMaintenanceRemodelingResource {
    
    public TblMachineMaintenanceRemodelingResource(){
        
    }
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private TblMachineMaintenanceRemodelingService tblMachineMaintenanceRemodelingService;
    
    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private CnfSystemService cnfSystemService;
    
    
    /**
     * 設備メンテナンス開始入力
     * @param issueVo
     * @return 
     */
    @POST
    @Path("start")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceRemodelingVo postMachineMaintenanceStart(TblIssueVo issueVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineMaintenanceRemodelingService.postMachineMaintenanceStart(issueVo, loginUser);
    }
    
    /**
     * 設備メンテナンス開始入力2
     * @param tblMachineMaintenanceRemodelingVo
     * @return 
     */
    @POST
    @Path("startmainte")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceRemodelingVo postMachineMaintenanceStart(TblMachineMaintenanceRemodelingVo tblMachineMaintenanceRemodelingVo) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineMaintenanceRemodelingService.postMachineMaintenanceStart2(tblMachineMaintenanceRemodelingVo, loginUser);
    }
    
    /**
     * 設備改造開始入力
     * @param tblMachineMaintenanceRemodelingVo
     * @return 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineRemodelingDetailVo postRemodeling(TblMachineMaintenanceRemodelingVo tblMachineMaintenanceRemodelingVo) {
        
        TblMachineRemodelingDetailVo basicResponse = new TblMachineRemodelingDetailVo();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        String machineId = tblMachineMaintenanceRemodelingVo.getMachineId();
        //外部データチェック
        if (machineId != null && !"".equals(machineId)) {
            BasicResponse response = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, machineId, "", loginUser);
            if (response.isError()) {
                basicResponse.setError(response.isError());
                basicResponse.setErrorCode(response.getErrorCode());
                basicResponse.setErrorMessage(response.getErrorMessage());
                return basicResponse;
            }
        }
        
        int stuats = tblMachineMaintenanceRemodelingService.getMachineMainteStatus(machineId);
        //メンテイ状態チェック
        if (stuats == 1) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.MSG201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_under_maintenance"));
            return basicResponse;
        } else if (stuats == 2) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.MSG201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_remodeling"));
            return basicResponse;
        }
        
        basicResponse = tblMachineMaintenanceRemodelingService.changeMachineMainteStatus(tblMachineMaintenanceRemodelingVo, loginUser);
        return basicResponse;
    }
    
    
    /**
     * 設備メンテナンス照会 設備メンテナンス改造件数取得
     *
     * @param mainteDateStart //メンテナンス日start
     * @param mainteDateEnd //メンテナンス日end
     * @param mainteOrRemodel //改造・メンテナンス区分
     * @param machineId //設備ID
     * @param machineName //設備名称
     * @param mainteStatus
     * @param department
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("mainteDateStart") String mainteDateStart, 
            @QueryParam("mainteDateEnd") String mainteDateEnd, 
            @QueryParam("mainteOrRemodel") String mainteOrRemodel, 
            @QueryParam("machineId") String machineId, 
            @QueryParam("machineName") String machineName, 
            @QueryParam("mainteStatus") String mainteStatus,
            @QueryParam("department") String department) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMachineMaintenanceRemodelingVo queryVo = new TblMachineMaintenanceRemodelingVo();
        FileUtil fu = new FileUtil();
        queryVo.setMainteDateStart(null == mainteDateStart ? null : fu.getDateParseForDate(mainteDateStart));
        queryVo.setMainteDateEnd(null == mainteDateEnd ? null : fu.getDateParseForDate(mainteDateEnd));
        queryVo.setMainteOrRemodel(null == mainteOrRemodel ? null : Integer.parseInt(mainteOrRemodel));
        //----20170825　選択リスト「所属」の追加----
        MstMachine mstmachine = new MstMachine();
        mstmachine.setDepartment(null == department ? null : Integer.parseInt(department));
        queryVo.setMstMachine(mstmachine);
        //-------------------------------------
        queryVo.setMachineId(machineId);
        queryVo.setMachineName(machineName);
        queryVo.setMainteStatus(mainteStatus);
        CountResponse count = (CountResponse) tblMachineMaintenanceRemodelingService.getRecordCount(queryVo, loginUser);
        CnfSystem cnf = cnfSystemService.findByKey("system", "max_list_record_count");
        long sysCount = Long.parseLong(cnf.getConfigValue());
        if (count.getCount() > sysCount) {
            count.setError(true);
            count.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
            msg = String.format(msg, sysCount);
            count.setErrorMessage(msg);
        }

        return count;
    }
    
    
    /**
     * 設備メンテナンス改造情報を取得
     *
     * @param mainteDateStart //メンテナンス日start
     * @param mainteDateEnd //メンテナンス日end
     * @param mainteOrRemodel //改造・メンテナンス区分
     * @param machineId
     * @param machineName //設備名称
     * @param mainteStatus
     * @param department
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceRemodelingVo getMaintenanceRemodeling(@QueryParam("mainteDateStart") String mainteDateStart,
            @QueryParam("mainteDateEnd") String mainteDateEnd,
            @QueryParam("mainteOrRemodel") String mainteOrRemodel,
            @QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
            @QueryParam("mainteStatus") String mainteStatus,
            @QueryParam("department") String department,
            @QueryParam("orderKey") String orderKey // 1:金型メンテナンス開始日時の昇順
    ){
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMachineMaintenanceRemodelingVo queryVo = new TblMachineMaintenanceRemodelingVo();
        FileUtil fu = new FileUtil();
        queryVo.setMainteDateStart(null == mainteDateStart ? null : fu.getDateParseForDate(mainteDateStart));
        queryVo.setMainteDateEnd(null == mainteDateEnd ? null : fu.getDateParseForDate(mainteDateEnd));
        queryVo.setMainteOrRemodel(null == mainteOrRemodel ? null : Integer.parseInt(mainteOrRemodel));
        //----20170825　選択リスト「所属」の追加----
        MstMachine mstmachine = new MstMachine();
        mstmachine.setDepartment(null == department ? null : Integer.parseInt(department));
        queryVo.setMstMachine(mstmachine);
        //-------------------------------------
        queryVo.setMachineId(machineId);
        queryVo.setMachineName(machineName);
        queryVo.setMainteStatus(mainteStatus);
        queryVo.setOrderKey(orderKey);
        BasicResponse br = tblMachineMaintenanceRemodelingService.getMaintenanceRemodeling(queryVo, loginUser);
        return (TblMachineMaintenanceRemodelingVo) br;
    }
    
    
    /**
     * 設備メンテナンス照会	設備メンテナンス削除
     *
     * @param id
     * @return
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteMaintenanceRemodeling(@QueryParam("id") String id) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineMaintenanceRemodelingService.deleteMaintenanceRemodeling(id, loginUser);
    }
    
    
    /**
     * 設備メンテナンス照会CSV出力
     *
     * @param mainteDateStart //メンテナンス日start
     * @param mainteDateEnd //メンテナンス日end
     * @param mainteOrRemodel //改造・メンテナンス区分
     * @param machineId //設備ID
     * @param machineName //設備名称
     * @param mainteStatus
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMachinesCSV(@QueryParam("mainteDateStart") String mainteDateStart,
            @QueryParam("mainteDateEnd") String mainteDateEnd,
            @QueryParam("mainteOrRemodel") String mainteOrRemodel,
            @QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
            @QueryParam("mainteStatus") String mainteStatus,
            @QueryParam("department") String department) {
        FileUtil fu = new FileUtil();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMachineMaintenanceRemodelingVo queryVo = new TblMachineMaintenanceRemodelingVo();
        if(mainteDateStart != null && !"".equals(mainteDateStart)){
            queryVo.setMainteDateStart("".equals(mainteDateStart) ? null : fu.getDateParseForDate(mainteDateStart));
        }
        if(mainteDateEnd != null && !"".equals(mainteDateEnd)){
            queryVo.setMainteDateEnd("".equals(mainteDateEnd) ? null : fu.getDateParseForDate(mainteDateEnd));
        }
        if(mainteOrRemodel != null && !"".equals(mainteOrRemodel)){
            queryVo.setMainteOrRemodel("".equals(mainteOrRemodel) ? null : Integer.parseInt(mainteOrRemodel));
        }
        if(machineId != null && !"".equals(machineId)){
            queryVo.setMachineId(machineId);
        }
        if(machineName != null && !"".equals(machineName)){
            queryVo.setMachineName(machineName);
        }
        if(mainteStatus != null && !"".equals(mainteStatus)){
            queryVo.setMainteStatus(mainteStatus);
        }
        MstMachine mstMachine = new MstMachine();
        mstMachine.setDepartment(null == department ? null : Integer.parseInt(department));
        queryVo.setMstMachine(mstMachine);

        FileReponse fr = tblMachineMaintenanceRemodelingService.getTblMachineMaintenanceRemodelingOutputCsv(queryVo, loginUser);
        return fr;
    }
    
    
    /**
     * 設備メンテナンス詳細 設備メンテナンス改造詳細情報を取得
     *
     * @param maintenanceId
     * @return
     */
    @GET
    @Path("detail/{maintenanceId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceRemodelingVo getMaintenanceDetail(@PathParam("maintenanceId") String maintenanceId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineMaintenanceRemodelingService.getMachineMainteOrRemodelDetails(maintenanceId, CommonConstants.MAINTEORREMODEL_MAINTE, loginUser);
    }
    
    
    /**
     * バッチで設備メンテナンス改造テーブルデータを取得
     *
     * @param latestExecutedDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachinemaintenanceremodeling")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceRemodelingList getExtMachineMaintenanceRemodelingsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("machineUuid") String machineUuid) {
        return tblMachineMaintenanceRemodelingService.getExtMachineMaintenanceRemodelingsByBatch(latestExecutedDate, machineUuid);
    }
    
    /**
     * 設備メンテナンス改造情報を取得
     *
     * @param mainteDateStart //メンテナンス日start
     * @param mainteDateEnd //メンテナンス日end
     * @param mainteOrRemodel //改造・メンテナンス区分
     * @param machineId
     * @param machineName //設備名称
     * @param mainteStatus
     * @param department
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param machineUuid
     * @return
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineMaintenanceRemodelingVo getMaintenanceRemodelingByPage(@QueryParam("mainteDateStart") String mainteDateStart,
            @QueryParam("mainteDateEnd") String mainteDateEnd,
            @QueryParam("mainteOrRemodel") String mainteOrRemodel,
            @QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
            @QueryParam("mainteStatus") String mainteStatus,
            @QueryParam("department") String department, 
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize, // ページSize
            @QueryParam("machineUuid") String machineUuid
            ) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMachineMaintenanceRemodelingVo queryVo = new TblMachineMaintenanceRemodelingVo();
        FileUtil fu = new FileUtil();
        queryVo.setMainteDateStart(null == mainteDateStart ? null : fu.getDateParseForDate(mainteDateStart));
        queryVo.setMainteDateEnd(null == mainteDateEnd ? null : fu.getDateParseForDate(mainteDateEnd));
        queryVo.setMainteOrRemodel(null == mainteOrRemodel ? null : Integer.parseInt(mainteOrRemodel));
        //----20170825　選択リスト「所属」の追加----
        MstMachine mstmachine = new MstMachine();
        mstmachine.setDepartment(null == department ? null : Integer.parseInt(department));
        queryVo.setMstMachine(mstmachine);
        //-------------------------------------
        queryVo.setMachineId(machineId);
        queryVo.setMachineName(machineName);
        queryVo.setMachineUuid(machineUuid);
        queryVo.setMainteStatus(mainteStatus);
        BasicResponse br = tblMachineMaintenanceRemodelingService.getMaintenanceRemodelingByPage(queryVo, loginUser, sidx,
                sord, pageNumber, pageSize, true);
        return (TblMachineMaintenanceRemodelingVo) br;
    }
}
