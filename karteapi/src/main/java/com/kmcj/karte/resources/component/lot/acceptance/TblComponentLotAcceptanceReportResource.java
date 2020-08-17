/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.lot.acceptance;

import com.google.gson.Gson;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("component/lot/acceptance/report")
public class TblComponentLotAcceptanceReportResource {

    public TblComponentLotAcceptanceReportResource() {

    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblComponentLotAcceptanceReportService tblComponentLotAcceptanceReportService;

    /**
     * ロット受入レポート
     *
     * @param incomingCompanyId
     * @param componentCode
     * @param periodFlag
     * @param reportDateStart
     * @param reportDateEnd
     * @param cavityPrefix
     * @param cavityNum
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblComponentLotAcceptanceReportList getLotAcceptanceReportDataList(
            @QueryParam("incomingCompanyId") String incomingCompanyId, //入荷会社
            @QueryParam("componentCode") String componentCode, //部品コード
            @QueryParam("periodFlag") String periodFlag, //期間種類 0:日別,1:週別,2:月別
            @QueryParam("reportDateStart") String reportDateStart, //検索開始日
            @QueryParam("reportDateEnd") String reportDateEnd, //検索終了日
            @QueryParam("cavityPrefix") String cavityPrefix,
            @QueryParam("cavityNum") int cavityNum
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblComponentLotAcceptanceReportList tblComponentLotAcceptanceReportList = tblComponentLotAcceptanceReportService.getLotAcceptanceReportDataList(incomingCompanyId, componentCode, periodFlag, reportDateStart, reportDateEnd, cavityPrefix, cavityNum, loginUser);
        Gson gson = new Gson();
        String resultJson = gson.toJson(tblComponentLotAcceptanceReportList.getResultListObj());
        tblComponentLotAcceptanceReportList.setResultList(resultJson);
        return tblComponentLotAcceptanceReportList;
    }

    /**
     * ロット受入レポートデータ出力
     * 
     * @param incomingCompanyId
     * @param componentCode
     * @param periodFlag
     * @param reportDateStart
     * @param reportDateEnd
     * @param cavityPrefix
     * @param cavityNum
     * @return 
     */
    @GET
    @Path("download")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getLotAcceptanceReportDataExcel(@QueryParam("incomingCompanyId") String incomingCompanyId, //入荷会社
            @QueryParam("componentCode") String componentCode, //部品コード
            @QueryParam("periodFlag") String periodFlag, //期間種類 0:日別,1:週別,2:月別
            @QueryParam("reportDateStart") String reportDateStart, //検索開始日
            @QueryParam("reportDateEnd") String reportDateEnd, //検索終了日
            @QueryParam("cavityPrefix") String cavityPrefix,
            @QueryParam("cavityNum") int cavityNum
    ) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblComponentLotAcceptanceReportService.getLotAcceptanceReportDataExcel(incomingCompanyId, componentCode, periodFlag, reportDateStart, reportDateEnd, cavityPrefix, cavityNum, loginUser);
    }

}
