package com.kmcj.karte.resources.machine.inspection.result;

import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
@Path("machine/inspection/result")
public class TblMachineInspectionResultResource {
    
    public TblMachineInspectionResultResource(){
        
    }
    
    @Inject
    private TblMachineInspectionResultService tblMachineInspectionResultService;
    
    @Context
    private ContainerRequestContext requestContext;
    
    
    /**
     * 設備メンテナンス詳細 点検情報を取得
     *
     * @param id
     * @param machineId
     * @return
     */
    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineInspectionResultVo getInspectionResults(@PathParam("id") String id, @QueryParam("machineId") String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineInspectionResultService.getInspectionResultsByMaintenanceId(id, machineId, loginUser);
    }
    
    
    /**
     * 設備メンテナンス詳細 点検情報を取得
     * 設備メンテナンス詳細一覧グリッドの一行目を選択し、該当する点検結果があれば、表示する。
     * @param id
     * @return 
     */
    @GET
    @Path("choice/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineInspectionResultVo getInspectionResult(@PathParam("id") String id) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineInspectionResultService.getInspectionResult(id, loginUser);
    }
    
    
    /**
     * 設備メンテナンス詳細 全部点検情報を取得
     * 点検項目マスタから作業大分類、作業中分類、作業小分類に該当する点検項目が定義されているか検索し、
     * 存在すれば、点検結果一覧に点検項目を表示する。
     * @param taskCategory1
     * @param taskCategory2
     * @param taskCategory3
     * @return 
     */
    @GET
    @Path("show")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineInspectionResultVo getInspectionResultShow(@QueryParam("taskCategory1") String taskCategory1,
            @QueryParam("taskCategory2") String taskCategory2,
            @QueryParam("taskCategory3") String taskCategory3) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineInspectionResultService.getInspectionResultShow(taskCategory1,taskCategory2,taskCategory3,loginUser);
    }
    
    
    /**
     * バッチで設備点検结果データを取得
     *
     * @param latestExecutedDate
     * @return
     */
    @GET
    @Path("extmachineinspectionresult")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineInspectionResultVo getExtMachineInspectionResultsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {
        return tblMachineInspectionResultService.getExtMachineInspectionResultsByBatch(latestExecutedDate);
    }
}
