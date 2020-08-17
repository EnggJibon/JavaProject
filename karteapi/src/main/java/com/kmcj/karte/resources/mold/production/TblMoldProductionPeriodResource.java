/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.production;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author lyd
 */
@RequestScoped
@Path("mold/production/period")
public class TblMoldProductionPeriodResource {

    public TblMoldProductionPeriodResource() {

    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblMoldProductionPeriodService tblMoldProductionPeriodService;

    /**
     * 金型期間別生産実績テーブル条件検索(金型期間別生産実績明細行取得)
     *
     * @param moldId
     * @param moldName
     * @param moldType
     * @param department
     * @param componentCode
     * @param componentName
     * @param periodFlag
     * @param productionDateStart
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldProductionPeriodList getMoldProductionPeriodDataList(
            @QueryParam("moldId") String moldId, //金型ID
            @QueryParam("moldName") String moldName, //金型名称
            @QueryParam("moldType") String moldType, //金型種類
            @QueryParam("department") String department, //所属
            @QueryParam("componentCode") String componentCode, //部品コード
            @QueryParam("componentName") String componentName, //部品名称
            @QueryParam("periodFlag") String periodFlag, //期間種類 0:日別,1:週別,2:月別
            @QueryParam("productionDateStart") String productionDateStart //検索開始日
    ) {

        Integer formatMoldType = null;
        Integer formatDepartment = null;
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        try {
            // 金型種類
            if (moldType != null && !"".equals(moldType)) {
                formatMoldType = Integer.parseInt(moldType);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            // 所属
            if (department != null && !"".equals(department)) {
                formatDepartment = Integer.parseInt(department);
            }
        } catch (NumberFormatException e) {
            // nothing
        }

        return tblMoldProductionPeriodService.getMoldProductionPeriodDataList(moldId, moldName, formatMoldType, formatDepartment,
                componentCode, componentName, periodFlag, productionDateStart, loginUser);
    }

    /**
     * 金型期間別生産実績テーブル条件検索(金型期間別生産実績明細行取得)
     *
     * @param tblMoldProductionPeriodList
     * @return
     */
    @POST
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldProductionPeriodList getMoldProductionPeriodDataListBeforeOrAfter(TblMoldProductionPeriodList tblMoldProductionPeriodList) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        List<String> paramList;
        String periodFlag;
        String productionDateStart;
        if (tblMoldProductionPeriodList != null
                && tblMoldProductionPeriodList.getParamList() != null
                && !"".equals(tblMoldProductionPeriodList.getProductionDateStart())) {
            
                paramList = tblMoldProductionPeriodList.getParamList();
                productionDateStart = tblMoldProductionPeriodList.getProductionDateStart();
                periodFlag = tblMoldProductionPeriodList.getPeriodFlag();
        } else {
            return new TblMoldProductionPeriodList();
        }
        
        return tblMoldProductionPeriodService.getMoldProductionPeriodDataListBeforeOrAfter(paramList,
                periodFlag, productionDateStart, tblMoldProductionPeriodList, loginUser);
    }

    /**
     * 金型期間別生産実績データをグラフ用
     * @param tblMoldProductionPeriodList
     * @return 
     */
    @POST
    @Path("graph")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldProductionPeriodList getMoldProductionPeriodGraphDataList(TblMoldProductionPeriodList tblMoldProductionPeriodList) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        List<String> paramList;
        String periodFlag;
        String productionDateStart;
        String productionDateEnd;
        if (tblMoldProductionPeriodList.getParamList() != null
                && !"".equals(tblMoldProductionPeriodList.getProductionDateEnd())) {

                paramList = tblMoldProductionPeriodList.getParamList();
                productionDateStart = tblMoldProductionPeriodList.getProductionDateStart();
                productionDateEnd = tblMoldProductionPeriodList.getProductionDateEnd();
                periodFlag = tblMoldProductionPeriodList.getPeriodFlag();
        } else {
            return new TblMoldProductionPeriodList();
        }
        
        return tblMoldProductionPeriodService.getMoldProductionPeriodGraphDataList(paramList, periodFlag,
                productionDateStart, productionDateEnd, tblMoldProductionPeriodList, loginUser);
    }

    /**
     * 金型期間別生産実績データ出力
     * @param tblMoldProductionPeriodList
     * @return 
     */
    @POST
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse postMoldProductionPeriodDataToCsv(TblMoldProductionPeriodList tblMoldProductionPeriodList) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        String headerStr = tblMoldProductionPeriodList.getHeaderStr();

        return tblMoldProductionPeriodService.postMoldProductionPeriodDataToCsv(tblMoldProductionPeriodList, loginUser, tblMoldProductionPeriodList.getProductionDateStart(), headerStr);
    }

}
