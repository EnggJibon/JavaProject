/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.production;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
@Path("machine/production/period")
public class TblMachineProductionPeriodResource {

    public TblMachineProductionPeriodResource() {

    }

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblMachineProductionPeriodService tblMachineProductionPeriodService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * 金型期間別生産実績テーブル条件検索(金型期間別生産実績明細行取得)
     *
     * @param machineId
     * @param machineName
     * @param machineType
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
    public TblMachineProductionPeriodList getMachineProductionPeriodDataList(
            @QueryParam("machineId") String machineId, //設備ID
            @QueryParam("machineName") String machineName, //設備名称
            @QueryParam("machineType") String machineType, //設備種類
            @QueryParam("department") String department, //所属
            @QueryParam("componentCode") String componentCode, //部品コード
            @QueryParam("componentName") String componentName, //部品名称
            @QueryParam("periodFlag") String periodFlag, //期間種類 0:日別,1:週別,2:月別
            @QueryParam("productionDateStart") String productionDateStart
    ) {

        Integer formatMachineType = null;
        Integer formatDepartment = null;

        try {
            // 金型種類
            if (machineType != null && !"".equals(machineType)) {
                formatMachineType = Integer.parseInt(machineType);
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

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineProductionPeriodService.getMachineProductionPeriodDataList(machineId, machineName, formatMachineType, formatDepartment,
                componentCode, componentName, periodFlag, productionDateStart, loginUser);
    }

    /**
     * 金型期間別生産実績テーブル条件検索(金型期間別生産実績明細行取得)
     *
     * @param tblMachineProductionPeriodList
     * @return
     */
    @POST
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineProductionPeriodList getMachineProductionPeriodDataSearchList(TblMachineProductionPeriodList tblMachineProductionPeriodList
    ) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        List<String> paramList;
        String periodFlag;
        String productionDateStart;
        if (tblMachineProductionPeriodList != null) {
            periodFlag = tblMachineProductionPeriodList.getPeriodFlag();
            productionDateStart = tblMachineProductionPeriodList.getProductionDateStart();
            paramList = tblMachineProductionPeriodList.getParamList();
        } else {
            return new TblMachineProductionPeriodList();
        }

        return tblMachineProductionPeriodService.getMachineProductionPeriodDataSearchList(paramList, periodFlag, productionDateStart, tblMachineProductionPeriodList, loginUser);
    }

    /**
     * 金型期間別生産実績データをグラフ用
     *
     * @param tblMachineProductionPeriodList
     * @return
     */
    @POST
    @Path("graph")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineProductionPeriodList getMachinedProductionPeriodGraphDataList(TblMachineProductionPeriodList tblMachineProductionPeriodList) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        List<String> paramList = new ArrayList<>();
        String periodFlag = "";
        String productionDateStart = "";
        String productionDateEnd = "";
        if (tblMachineProductionPeriodList != null) {
            paramList = tblMachineProductionPeriodList.getParamList();
            periodFlag = tblMachineProductionPeriodList.getPeriodFlag();
            productionDateStart = tblMachineProductionPeriodList.getProductionDateStart();
            productionDateEnd = tblMachineProductionPeriodList.getProductionDateEnd();
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        Date formatOperatingDateStart = null;
        Date formatOperatingDateEnd = null;
        try {
            // 検索開始日
            if (productionDateStart != null && !"".equals(productionDateStart)) {
                formatOperatingDateStart = sdf.parse(productionDateStart);
            }
        } catch (ParseException e) {
            // nothing
        }
        try {
            // 検索终了日
            if (productionDateEnd != null && !"".equals(productionDateEnd)) {
                formatOperatingDateEnd = sdf.parse(productionDateEnd);
            }
        } catch (ParseException e) {
            // nothing
        }
        TblMachineProductionPeriodList checkList = new TblMachineProductionPeriodList();
        if (formatOperatingDateStart == null || formatOperatingDateEnd == null) {

            checkList.setError(true);
            checkList.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item");
            String production_date = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "production_date");
            checkList.setErrorMessage(String.format(msg, production_date));
            return checkList;
        } else {
            return tblMachineProductionPeriodService.getMachineProductionPeriodGraphDataList(
                    tblMachineProductionPeriodList,
                    paramList,
                    periodFlag,
                    productionDateStart,
                    productionDateEnd,
                    loginUser);
        }
    }

    /**
     * 金型期間別生産実績データ出力
     *
     * @param tblMoldProductionPeriodList
     * @return
     */
    @POST
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse postMachineProductionPeriodDataToCsv(TblMachineProductionPeriodList tblMoldProductionPeriodList) {

        String headerStr = tblMoldProductionPeriodList.getHeaderStr();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblMachineProductionPeriodService.postMachineProductionPeriodDataToCsv(tblMoldProductionPeriodList, loginUser, tblMoldProductionPeriodList.getProductionDateStart(), headerStr);
    }

}
