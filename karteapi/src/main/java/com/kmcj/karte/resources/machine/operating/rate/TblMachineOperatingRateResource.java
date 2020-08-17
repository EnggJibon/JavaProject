package com.kmcj.karte.resources.machine.operating.rate;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
 * 設備
 *
 * @author apeng
 */
@RequestScoped
@Path("machine/operating/rate")
public class TblMachineOperatingRateResource {

    public TblMachineOperatingRateResource() {
    }

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblMachineOperatingRateService tblMachineOperatingRateService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    /**
     * 設備期間別稼働率照会取得
     *
     * @param machineId //設備ID
     * @param machineName //設備名称
     * @param machineType //設備種類
     * @param department //所属
     * @param periodFlag//集計方式のタイプ,0:日,1:周,2:月
     * @param productionDateStart//検索開始日
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineOperatingRatePeriodList getMachineOperatingRateDataList(
            @QueryParam("machineId") String machineId, //設備ID
            @QueryParam("machineName") String machineName, //設備名称
            @QueryParam("machineType") String machineType, //設備種類
            @QueryParam("department") String department, //所属
            @QueryParam("periodFlag") String periodFlag, //集計方式のタイプ
            @QueryParam("productionDateStart") String productionDateStart //検索開始日
    ) {
        Integer formatMachineType = null;
        Integer formatDepartment = null;

        try {
            if (machineType != null && !"".equals(machineType)) {
                formatMachineType = Integer.parseInt(machineType);
            }
        } catch (NumberFormatException e) {
            // nothing
        }
        try {
            if (department != null && !"".equals(department)) {
                formatDepartment = Integer.parseInt(department);
            }
        } catch (NumberFormatException e) {
            // nothing
        }

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        // 最後生産日From-To
        Date formatOperatingDateStart = null;
        try {
            // 検索開始日
            if (productionDateStart != null && !"".equals(productionDateStart)) {
                formatOperatingDateStart = sdf.parse(productionDateStart);
            }
        } catch (ParseException e) {
            // nothing
        }

        TblMachineOperatingRatePeriodList tblMachineOperatingRatePeriodList = tblMachineOperatingRateService.getTblMachinePeriodList(machineId,
                machineName,
                formatMachineType,
                formatDepartment,
                periodFlag,
                formatOperatingDateStart,
                loginUser);

        return tblMachineOperatingRatePeriodList;
    }

    /**
     * 設備期間別稼働率照会取得
     *
     * @param tblMachineOperatingRatePeriodList
     * @return
     */
    @POST
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineOperatingRatePeriodList getMachineOperatingRateDataSearchList(TblMachineOperatingRatePeriodList tblMachineOperatingRatePeriodList) {

        List<String> machineUuids = new ArrayList();
        String machineId = "";
        String machineName = "";
        String periodFlag = "";
        String workDate = "";
        if (tblMachineOperatingRatePeriodList != null) {
            machineUuids = tblMachineOperatingRatePeriodList.getMachineUuids(); //複数UUID
            periodFlag = tblMachineOperatingRatePeriodList.getPeriodFlag(); //集計方式のタイプ,0:日,1:周,2:月
            workDate = tblMachineOperatingRatePeriodList.getProductionDateStart();//検索開始日
        }

        // 最後生産日From
        Date formatOperatingDateStart = null;
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        try {
            // 開始期間
            if (workDate != null && !"".equals(workDate)) {
                formatOperatingDateStart = sdf.parse(workDate);
            }
        } catch (ParseException e) {
            // nothing
        }
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblMachineOperatingRateService.getMachineOperatingRateDataSearchList(machineUuids, formatOperatingDateStart, null, periodFlag, loginUser, null, machineId, machineName, null, null);
    }

    /**
     * 設備期間別稼働率テーブル条件検索(設備期間別稼働率明細行取得グラフ作成)
     *
     * @param tblMachineOperatingRatePeriodList
     * @return
     */
    @POST
    @Path("graph")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineOperatingRatePeriodList getMachineOperatingRateGraphDataList(TblMachineOperatingRatePeriodList tblMachineOperatingRatePeriodList) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        List<String> machineUuids = new ArrayList();
        String periodFlag = "";
        String productionDateStart = "";
        String productionDateEnd = "";
        String machineId = "";
        String machineName = "";
        String machineType = "";
        String department = "";
        if (tblMachineOperatingRatePeriodList != null) {
            machineUuids = tblMachineOperatingRatePeriodList.getMachineUuids(); //複数UUID
            periodFlag = tblMachineOperatingRatePeriodList.getPeriodFlag(); //集計方式のタイプ,0:日,1:周,2:月
            productionDateStart = tblMachineOperatingRatePeriodList.getProductionDateStart();//検索開始日
            productionDateEnd = tblMachineOperatingRatePeriodList.getProductionDateEnd();//検索终了日
//            machineId = tblMachineOperatingRatePeriodList.getMachineId();//設備ID
//            machineName = tblMachineOperatingRatePeriodList.getMachineName();//設備名称
//            machineType = tblMachineOperatingRatePeriodList.getMachineType();//設備種類
//            department = tblMachineOperatingRatePeriodList.getDepartment();//所属
        }

        Integer formatMachineType = null;
        Integer formatDepartment = null;

//        try {
//            if (machineType != null && !"".equals(machineType)) {
//                formatMachineType = Integer.parseInt(machineType);
//            }
//        } catch (NumberFormatException e) {
//            // nothing
//        }
//        try {
//            if (department != null && !"".equals(department)) {
//                formatDepartment = Integer.parseInt(department);
//            }
//        } catch (NumberFormatException e) {
//            // nothing
//        }
        // 最後生産日From - To
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
        TblMachineOperatingRatePeriodList checkList = new TblMachineOperatingRatePeriodList();
        if (formatOperatingDateStart == null || formatOperatingDateEnd == null) {
            checkList.setError(true);
            checkList.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item");
            String production_date = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "production_date");
            checkList.setErrorMessage(String.format(msg, production_date));
            return checkList;
        } else {
            return tblMachineOperatingRateService.getMachineOperatingRateGraphDataList(machineUuids, periodFlag, formatOperatingDateStart, formatOperatingDateEnd, loginUser, machineId, machineName, formatMachineType, formatDepartment);
        }

    }

    /**
     * 設備期間別生産実績データ出力
     *
     * @param tblMachineOperatingRatePeriodList
     * @return
     */
    @POST
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse postMachineOperatingRateDataToCsv(TblMachineOperatingRatePeriodList tblMachineOperatingRatePeriodList) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblMachineOperatingRateService.postMachineOperatingRateDataToCsv(tblMachineOperatingRatePeriodList, loginUser);
    }
}
