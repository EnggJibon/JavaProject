/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inspection.result;

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
@Path("mold/inspection/result")
public class TblMoldInspectionResultResource {

    public TblMoldInspectionResultResource() {

    }

    @Inject
    private TblMoldInspectionResultService tblMoldInspectionResultService;

    @Context
    private ContainerRequestContext requestContext;

    /**
     * T0008	金型メンテナンス詳細 点検情報を取得
     *
     * @param id
     * @param moldId
     * @return
     */
    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldInspectionResultVo getInspectionResults(@PathParam("id") String id, @QueryParam("moldId") String moldId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldInspectionResultService.getInspectionResultsByMaintenanceId(id, moldId, loginUser);
    }

    /**
     * T0008 金型メンテナンス詳細 点検情報を取得
     * 金型メンテナンス詳細一覧グリッドの一行目を選択し、該当する点検結果があれば、表示する。
     * @param id
     * @return 
     */
    @GET
    @Path("choice/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldInspectionResultVo getInspectionResult(@PathParam("id") String id) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldInspectionResultService.getInspectionResult(id, loginUser);
    }
    
    
    /**
     * T0008 金型メンテナンス詳細 全部点検情報を取得
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
    public TblMoldInspectionResultVo getInspectionResultShow(@QueryParam("taskCategory1") String taskCategory1,
            @QueryParam("taskCategory2") String taskCategory2,
            @QueryParam("taskCategory3") String taskCategory3) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldInspectionResultService.getInspectionResultShow(taskCategory1,taskCategory2,taskCategory3,loginUser);
    }
    
    /**
     * バッチで金型点検结果データを取得
     *
     * @param latestExecutedDate
     * @return
     */
    @GET
    @Path("extmoldinspectionresult")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldInspectionResultVo getExtMoldInspectionResultsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate) {
        return tblMoldInspectionResultService.getExtMoldInspectionResultsByBatch(latestExecutedDate);
    }
}
