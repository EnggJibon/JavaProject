/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.issue;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.batch.externalmold.choice.ExtMstChoiceService;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.files.TblUploadFileService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
 * 打上
 *
 * @author jiangxs
 */
@RequestScoped
@Path("mold/issue")
public class TblIssueResource {

    public TblIssueResource() {

    }

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private TblIssueService tblIssueService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private CnfSystemService cnfSystemService;

    @Inject
    private TblUploadFileService tblUploadFileService;
    
    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private ExtMstChoiceService extMstChoiceService;
    
    // 打上場所
    public static final String USER_DEPARTMENT = "mst_user.department";
    // 打上工程
    public static final String ISSUE_REPORT_PHASE = "tbl_issue.report_phase";
    // 打上大分類
    public static final String ISSUE_REPORT_CATEGORY1 = "tbl_issue.report_category1";
    // 打上中分類
    public static final String ISSUE_REPORT_CATEGORY2 = "tbl_issue.report_category2";
    // 打上小分類
    public static final String ISSUE_REPORT_CATEGORY3 = "tbl_issue.report_category3";

    // 打上用選択肢
    public static final String[] ISSUE_ARRAY = { USER_DEPARTMENT, ISSUE_REPORT_PHASE, ISSUE_REPORT_CATEGORY1,
            ISSUE_REPORT_CATEGORY2, ISSUE_REPORT_CATEGORY3 };

    /**
     * 異常一覧 異常情報件数取得
     *
     * @param measureStatus
     * @param measureStatusOperand // Added parameter "measureStatusOperand"  for Issue: KM-598
     * @param department
     * @param reportDate
     * @param reportDateForm
     * @param reportDateTo
     * @param measureDueDate
     * @param measureDueDateForm
     * @param measureDueDateTo
     * @param moldId
     * @param moldName
     * @param componentCode
     * @param componentName
     * @param machineId
     * @param machineName
     * @param mainTenanceId
     * @param reportPersonName
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("noMoldMainte") int noMoldMainte,
            @QueryParam("noMachineMainte") int noMachineMainte,
            @QueryParam("measureStatus") int measureStatus,
            @QueryParam("measureStatusOperand") int measureStatusOperand,// Added parameter "measureStatusOperand"  for Issue: KM-598
            @QueryParam("department") String department,
            @QueryParam("reportDate") String reportDate,
            @QueryParam("reportDateForm") String reportDateForm,
            @QueryParam("reportDateTo") String reportDateTo,
            @QueryParam("measureDueDate") String measureDueDate,
            @QueryParam("measureDueDateForm") String measureDueDateForm,
            @QueryParam("measureDueDateTo") String measureDueDateTo,
            @QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
            @QueryParam("mainTenanceId") String mainTenanceId,
            @QueryParam("reportPersonName") String reportPersonName // KM-359 打上一覧に報告者追加
    ) {
        // Added parameter "measureStatusOperand"  for Issue: KM-598
        CountResponse count = tblIssueService.getIssueCount(noMoldMainte,noMachineMainte,measureStatus,measureStatusOperand, department, reportDate, reportDateForm, reportDateTo,
                measureDueDate, measureDueDateForm, measureDueDateTo, moldId,
                moldName, componentCode, componentName, machineId, machineName,
                mainTenanceId, reportPersonName, false);

        CnfSystem cnf = cnfSystemService.findByKey("system", "max_list_record_count");
        long sysCount = Long.parseLong(cnf.getConfigValue());

        if (count.getCount() > sysCount) {
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            count.setError(true);
            count.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
            msg = String.format(msg, sysCount);
            count.setErrorMessage(msg);
        }
        return count;
    }

    /**
     * 異常一覧,通常打上,金型メンテナンス開始入力 異常情報複数取得
     *
     * @param measureStatus
     * @param measureStatusOperand
     * @param department
     * @param reportDate
     * @param reportDateFrom
     * @param reportDateTo
     * @param measureDueDate
     * @param measureDueDateFrom
     * @param measureDueDateTo
     * @param moldId
     * @param moldName
     * @param componentCode
     * @param componentName
     * @param machineId
     * @param machineName
     * @param mainTenanceId
     * @param reportPersonName
     * @param sortByMeasureDueDate
     * @param reportPhase
     * @param reportCategory1
     * @param reportCategory2
     * @param reportCategory3
     * @param memo01
     * @param memo02
     * @param memo03
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblIssueList getIssues(@QueryParam("noMoldMainte") int noMoldMainte,
            @QueryParam("noMachineMainte") int noMachineMainte,
            @QueryParam("measureStatus") int measureStatus,
            @QueryParam("measureStatusOperand") int measureStatusOperand,// Added parameter "measureStatusOperand"  for Issue: KM-598
            @QueryParam("department") String department,
            @QueryParam("reportDate") String reportDate,
            @QueryParam("reportDateForm") String reportDateFrom,
            @QueryParam("reportDateTo") String reportDateTo,
            @QueryParam("measureDueDate") String measureDueDate,
            @QueryParam("measureDueDateFrom") String measureDueDateFrom,
            @QueryParam("measureDueDateTo") String measureDueDateTo,
            @QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
            @QueryParam("mainTenanceId") String mainTenanceId,
            @QueryParam("reportPersonName") String reportPersonName,
            @QueryParam("orderByDueDate") boolean orderByDueDate,
            @QueryParam("issueReportPhase") int reportPhase,
            @QueryParam("issueReportCategory1") int reportCategory1,
            @QueryParam("issueReportCategory2") int reportCategory2,
            @QueryParam("issueReportCategory3") int reportCategory3,
            @QueryParam("memo01") String memo01,
            @QueryParam("memo02") String memo02,
            @QueryParam("memo03") String memo03,
            @QueryParam("happenedAt") String happenedAt
             // KM-359 打上一覧に報告者追加 @QueryParam("sortByMeasureDueDate") boolean sortByMeasureDueDate
    ) {
        // Added parameter "measureStatusOperand"  for Issue: KM-598
        List<TblIssue> list = tblIssueService.getIssues(noMoldMainte, noMachineMainte, measureStatus,measureStatusOperand, department, reportDate,
                reportDateFrom, reportDateTo, measureDueDate, measureDueDateFrom, measureDueDateTo,
                moldId, moldName, componentCode, componentName, machineId, machineName, mainTenanceId,reportPersonName, orderByDueDate,
                reportPhase, reportCategory1, reportCategory2, reportCategory3, memo01, memo02, memo03, happenedAt);//sortByMeasureDueDate
        TblIssueList response = new TblIssueList();
        List<TblIssueVo> TblIssueVoList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        TblIssueVo tblIssueVo;
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        Map<String, String> measureStatusChoiceMap = null;
        if (list != null && list.size() > 0) {
            measureStatusChoiceMap = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{"tbl_issue.measure_status"});
        }
        for (int i = 0; i < list.size(); i++) {
            TblIssue tblIssue = list.get(i);
            tblIssueVo = new TblIssueVo();
            tblIssueVo.setId(tblIssue.getId());
            tblIssueVo.setReportDate(tblIssue.getReportDate() == null ? "" : sdf.format(tblIssue.getReportDate()));
            tblIssueVo.setMeasureDueDate(tblIssue.getMeasureDueDate() == null ? "" : sdf.format(tblIssue.getMeasureDueDate()));
            tblIssueVo.setMeasureStatus(null == tblIssue.getMeasureStatus() ? CommonConstants.ISSUE_MEASURE_STATUS_NOTYET : tblIssue.getMeasureStatus());
            if (measureStatusChoiceMap != null) {
                tblIssueVo.setMeasureStatusText(measureStatusChoiceMap.get("tbl_issue.measure_status" + tblIssueVo.getMeasureStatus()));
            } else {
                tblIssueVo.setMeasureStatusText("");
            }
            boolean excFlag = false;
            // 金型
            if (tblIssue.getMstMold() != null) {
                // 金型が外部管理されているかどうかチェック
                if (FileUtil.checkExternal(entityManager, mstDictionaryService, tblIssue.getMstMold().getMoldId(), loginUser).isError()) {
                    // 金型が外部管理されている
                    excFlag = true;
                }
                tblIssueVo.setMoldUuid(tblIssue.getMstMold().getUuid());
                tblIssueVo.setMoldId(tblIssue.getMstMold().getMoldId());
                tblIssueVo.setMoldName(tblIssue.getMstMold().getMoldName());
                tblIssueVo.setMoldInstallationSiteName(tblIssue.getMstMold().getInstllationSiteName());
            } else {
                tblIssueVo.setMoldUuid("");
                tblIssueVo.setMoldId("");
                tblIssueVo.setMoldName("");
                tblIssueVo.setMoldInstallationSiteName("");
            }
            // 部品
            if (tblIssue.getMstComponent() != null) {
                tblIssueVo.setComponentId(tblIssue.getMstComponent().getId());
                tblIssueVo.setComponentCode(tblIssue.getMstComponent().getComponentCode());
                tblIssueVo.setComponentName(tblIssue.getMstComponent().getComponentName());
            } else {
                tblIssueVo.setComponentId("");
                tblIssueVo.setComponentCode("");
                tblIssueVo.setComponentName("");
            }

            // 設備
            if (tblIssue.getMstMachine() != null) {
                if (excFlag != true) {
                    String conpanyId = tblIssue.getMstMachine().getCompanyId();
                    if (conpanyId != null && !"".equals(conpanyId)) {
                        // 金型が外部管理されているかどうかチェック
                        if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", conpanyId, loginUser).isError()) {
                            // 金型が外部管理されている
                            excFlag = true;
                        }
                    }
                }
                tblIssueVo.setMachineUuid(tblIssue.getMstMachine().getUuid());
                tblIssueVo.setMachineId(tblIssue.getMstMachine().getMachineId());
                tblIssueVo.setMachineName(tblIssue.getMstMachine().getMachineName());
            } else {
                tblIssueVo.setMachineUuid("");
                tblIssueVo.setMachineId("");
                tblIssueVo.setMachineName("");

            }
            if (excFlag == true) {
                tblIssueVo.setExternalFlg(1);
            } else {
                tblIssueVo.setExternalFlg(0);
            }
            tblIssueVo.setQuantity(tblIssue.getQuantity());
            tblIssueVo.setShotCountAtIssue(tblIssue.getShotCountAtIssue());
            tblIssueVo.setMainteType(tblIssue.getMainteType());
            tblIssueVo.setReportDepartment(tblIssue.getReportDepartment() == null ? "" : String.valueOf(tblIssue.getReportDepartment()));
            tblIssueVo.setReportDepartmentName(tblIssue.getReportDepartmentName());
            tblIssueVo.setReportPhase(tblIssue.getReportPhase() == null ? "" : String.valueOf(tblIssue.getReportPhase()));
            tblIssueVo.setReportPhaseText(tblIssue.getReportPhaseText());
            tblIssueVo.setReportCategory1(tblIssue.getReportCategory1() == null ? "" : String.valueOf(tblIssue.getReportCategory1()));
            tblIssueVo.setReportCategory1Text(tblIssue.getReportCategory1Text());
            tblIssueVo.setReportCategory2(tblIssue.getReportCategory2() == null ? "" : String.valueOf(tblIssue.getReportCategory2()));
            tblIssueVo.setReportCategory2Text(tblIssue.getReportCategory2Text());
            tblIssueVo.setReportCategory3(tblIssue.getReportCategory3() == null ? "" : String.valueOf(tblIssue.getReportCategory3()));
            tblIssueVo.setReportCategory3Text(tblIssue.getReportCategory3Text());
            tblIssueVo.setMemo01(tblIssue.getMemo01());
            tblIssueVo.setMemo02(tblIssue.getMemo02());
            tblIssueVo.setMemo03(tblIssue.getMemo03());
            tblIssueVo.setHappenedAt(tblIssue.getHappenedAt() == null ? "" : sdf.format(tblIssue.getHappenedAt()));
            tblIssueVo.setIssue(tblIssue.getIssue() == null ? "" : tblIssue.getIssue());
            tblIssueVo.setMeasureSummary(tblIssue.getMeasureSummary() == null ? "" : tblIssue.getMeasureSummary());
            tblIssueVo.setMeasuerCompletedDate(tblIssue.getMeasuerCompletedDate() == null ? "" : sdf.format(tblIssue.getMeasuerCompletedDate()));
            // 金型メンテ日
            if (tblIssue.getTblMoldMaintenanceRemodeling() != null) {
                tblIssueVo.setMainteDate(tblIssue.getTblMoldMaintenanceRemodeling().getMainteDate() == null ? "" : sdf.format(tblIssue.getTblMoldMaintenanceRemodeling().getMainteDate()));
                if (tblIssue.getTblMoldMaintenanceRemodeling().getMstUser() != null) {
                    tblIssueVo.setMoldMaintenancePersonName(tblIssue.getTblMoldMaintenanceRemodeling().getMstUser().getUserName());
                } else {
                    tblIssueVo.setMoldMaintenancePersonName("");
                }
            } else {
                tblIssueVo.setMainteDate(null);
                tblIssueVo.setMoldMaintenancePersonName("");
            }

            // 設備メンテ日
            if (tblIssue.getTblMachineMaintenanceRemodeling() != null) {
                tblIssueVo.setMachineMainteDate(tblIssue.getTblMachineMaintenanceRemodeling().getMainteDate() == null ? "" : sdf.format(tblIssue.getTblMachineMaintenanceRemodeling().getMainteDate()));
                if (tblIssue.getTblMachineMaintenanceRemodeling().getMstUser() != null) {
                    tblIssueVo.setMachineMaintenancePersonName(tblIssue.getTblMachineMaintenanceRemodeling().getMstUser().getUserName());
                } else {
                    tblIssueVo.setMachineMaintenancePersonName("");
                }
            } else {
                tblIssueVo.setMachineMainteDate(null);
                tblIssueVo.setMachineMaintenancePersonName("");
            }

            //報告者
            if (tblIssue.getMstUser() != null) {
                tblIssueVo.setIssueReportPersonName(tblIssue.getMstUser().getUserName());
            } else {
                tblIssueVo.setIssueReportPersonName("");
            }

            TblIssueVoList.add(tblIssueVo);
        }
        response.setTblIssueVoList(TblIssueVoList);
        return response;
    }

    /**
     * 通常打上,異常一覧 削除してよいか確認ダイアログを表示して、選択されている異常データを削除する。
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteIssue(@PathParam("id") String id) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        // 存在チェック
        if (tblIssueService.checkIssue(id)) {
            //外部データがチェック add 2017-1-16 10:49:00 jiangxiaosong
            TblIssue tblIssue = entityManager.find(TblIssue.class, id);
            if (tblIssue.getMstMold() != null) {
                response = FileUtil.checkExternal(entityManager, mstDictionaryService, tblIssue.getMstMold().getMoldId(), loginUser);
                if (response.isError()) {
                    return response;
                }
            }

            if (tblIssue.getMstMachine() != null) {
                String companyId = tblIssue.getMstMachine().getCompanyId();
                if (null != companyId && !"".equals(companyId)) {
                    response = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", companyId, loginUser);
                    if (response.isError()) {
                        return response;
                    }
                }
            }
            // 削除を行う
            tblIssueService.deleteIssue(id);
        } else {
            //　他人により削除された可能性が発生
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
        }
        return response;
    }

    /**
     * 通常打上,異常詳細 異常IDを保持して、通常打上画面に編集モードで詳細情報を画面に表示する
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblIssueList getIssue(@PathParam("id") String id) {
        TblIssueVo tblIssueVo;
        TblIssueList response = new TblIssueList();
        List<TblIssueVo> tblIssueVoList = new ArrayList();
        TblIssueImageFileVo tblIssueImageFileVos;
        List<TblIssueImageFileVo> TblIssueImageFileVoList = new ArrayList<>();
        List<TblIssueImageFile> tblIssueImageFiles = new ArrayList<>();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        List<TblIssue> tblIssueList = tblIssueService.getIssueById(id);
        if (tblIssueList == null || tblIssueList.isEmpty()) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return response;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);

        TblIssue tblIssue = tblIssueList.get(0);
        tblIssueVo = new TblIssueVo();
        tblIssueVo.setReportDate(tblIssue.getReportDate() == null ? "" : sdf.format(tblIssue.getReportDate()));
        tblIssueVo.setHappenedAt(tblIssue.getHappenedAt() == null ? "" : sdf.format(tblIssue.getHappenedAt()));
        tblIssueVo.setMeasureStatus(tblIssue.getMeasureStatus());
        boolean extFlag = false;
        String companyUuid = null;
        if (tblIssue.getMstMold() != null) {
            if (FileUtil.checkExternal(entityManager, mstDictionaryService, tblIssue.getMstMold().getMoldId(), loginUser).isError()) {
                extFlag = true;
                companyUuid = tblIssue.getMstMold().getCompanyId();
            }
            tblIssueVo.setMoldUuid(tblIssue.getMstMold().getUuid());
            tblIssueVo.setMoldId(tblIssue.getMstMold().getMoldId());
            tblIssueVo.setMoldName(tblIssue.getMstMold().getMoldName());
            tblIssueVo.setAfterMainteTotalShotCount(tblIssue.getMstMold().getAfterMainteTotalShotCount() == null ? 0 : tblIssue.getMstMold().getAfterMainteTotalShotCount());
        } else {
            tblIssueVo.setMoldUuid("");
            tblIssueVo.setMoldId("");
            tblIssueVo.setMoldName("");
            tblIssueVo.setExternalFlg(0);
        }
        if (tblIssue.getMstComponent() != null) {
            tblIssueVo.setComponentId(tblIssue.getMstComponent().getId());
            tblIssueVo.setComponentCode(tblIssue.getMstComponent().getComponentCode());
            tblIssueVo.setComponentName(tblIssue.getMstComponent().getComponentName());
        } else {
            tblIssueVo.setComponentId("");
            tblIssueVo.setComponentCode("");
            tblIssueVo.setComponentName("");
        }
        // ロット番号
        tblIssueVo.setLotNumber(tblIssue.getLotNumber());
        // 工程
        tblIssueVo.setProcedureId(tblIssue.getProcedureId());
        tblIssueVo.setProcedureCode(tblIssue.getMstProcedure() == null ? "" : tblIssue.getMstProcedure().getProcedureCode());
        tblIssueVo.setProcedureName(tblIssue.getMstProcedure() == null ? "" : tblIssue.getMstProcedure().getProcedureName());
        if (tblIssue.getMstMachine() != null) {
            if (extFlag != true) {
                String companyId = tblIssue.getMstMachine().getCompanyId();
                if (null != companyId && !"".equals(companyId)) {
                    if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", companyId, loginUser).isError()) {
                        extFlag = true;
                        companyUuid = companyId;
                    }
                }
            }
            tblIssueVo.setMachineUuid(tblIssue.getMstMachine().getUuid());
            tblIssueVo.setMachineId(tblIssue.getMstMachine().getMachineId());
            tblIssueVo.setMachineName(tblIssue.getMstMachine().getMachineName());
        } else {
            tblIssueVo.setMachineUuid("");
            tblIssueVo.setMachineId("");
            tblIssueVo.setMachineName("");
        }

        if (extFlag == true) {
            tblIssueVo.setExternalFlg(1);
            Map<String, String> extChoiceMap = extMstChoiceService.getChoiceMap(companyUuid, loginUser.getLangId(), ISSUE_ARRAY);
            tblIssueVo.setReportDepartment(tblIssue.getReportDepartment() == null ? "" : String.valueOf(tblIssue.getReportDepartment()));
            tblIssueVo.setReportDepartmentName(
                    FileUtil.getStr(extChoiceMap.get(USER_DEPARTMENT + String.valueOf(tblIssue.getReportDepartment()))));
            tblIssueVo.setReportPhase(tblIssue.getReportPhase() == null ? "" : String.valueOf(tblIssue.getReportPhase()));
            tblIssueVo.setReportPhaseText(FileUtil.getStr(extChoiceMap.get(ISSUE_REPORT_PHASE + String.valueOf(tblIssue.getReportPhase()))));
            tblIssueVo.setReportCategory1(tblIssue.getReportCategory1() == null ? "" : String.valueOf(tblIssue.getReportCategory1()));
            tblIssueVo.setReportCategory1Text(FileUtil.getStr(extChoiceMap.get(ISSUE_REPORT_CATEGORY1 + String.valueOf(tblIssue.getReportCategory1()))));
            tblIssueVo.setReportCategory2(tblIssue.getReportCategory2() == null ? "" : String.valueOf(tblIssue.getReportCategory2()));
            tblIssueVo.setReportCategory2Text(FileUtil.getStr(extChoiceMap.get(ISSUE_REPORT_CATEGORY2 + String.valueOf(tblIssue.getReportCategory2()))));
            tblIssueVo.setReportCategory3(tblIssue.getReportCategory3() == null ? "" : String.valueOf(tblIssue.getReportCategory3()));
            tblIssueVo.setReportCategory3Text(FileUtil.getStr(extChoiceMap.get(ISSUE_REPORT_CATEGORY3 + String.valueOf(tblIssue.getReportCategory3()))));
        } else {
            tblIssueVo.setExternalFlg(0);
            tblIssueVo.setReportDepartment(tblIssue.getReportDepartment() == null ? "" : String.valueOf(tblIssue.getReportDepartment()));
            tblIssueVo.setReportDepartmentName(tblIssue.getReportDepartmentName() == null ? "" : tblIssue.getReportDepartmentName());
            tblIssueVo.setReportPhase(tblIssue.getReportPhase() == null ? "" : String.valueOf(tblIssue.getReportPhase()));
            tblIssueVo.setReportPhaseText(tblIssue.getReportPhaseText() == null ? "" : tblIssue.getReportPhaseText());
            tblIssueVo.setReportCategory1(tblIssue.getReportCategory1() == null ? "" : String.valueOf(tblIssue.getReportCategory1()));
            tblIssueVo.setReportCategory1Text(tblIssue.getReportCategory1Text() == null ? "" : tblIssue.getReportCategory1Text());
            tblIssueVo.setReportCategory2(tblIssue.getReportCategory2() == null ? "" : String.valueOf(tblIssue.getReportCategory2()));
            tblIssueVo.setReportCategory2Text(tblIssue.getReportCategory2Text() == null ? "" : tblIssue.getReportCategory2Text());
            tblIssueVo.setReportCategory3(tblIssue.getReportCategory3() == null ? "" : String.valueOf(tblIssue.getReportCategory3()));
            tblIssueVo.setReportCategory3Text(tblIssue.getReportCategory3Text() == null ? "" : tblIssue.getReportCategory3Text());
        }
        tblIssueVo.setMemo01(tblIssue.getMemo01()== null ? "" : tblIssue.getMemo01());
        tblIssueVo.setMemo02(tblIssue.getMemo02()== null ? "" : tblIssue.getMemo02());
        tblIssueVo.setMemo03(tblIssue.getMemo03()== null ? "" : tblIssue.getMemo03());
        tblIssueVo.setQuantity(tblIssue.getQuantity());
        tblIssueVo.setShotCountAtIssue(tblIssue.getShotCountAtIssue());
        tblIssueVo.setMainteType(tblIssue.getMainteType());
        tblIssueVo.setIssue(tblIssue.getIssue() == null ? "" : tblIssue.getIssue());
        tblIssueVo.setMeasureDueDate(tblIssue.getMeasureDueDate() == null ? "" : sdf.format(tblIssue.getMeasureDueDate()));
        tblIssueVo.setReportPersonUuid(tblIssue.getReportPersonUuid() == null ? "" : tblIssue.getReportPersonUuid());
        tblIssueVo.setReportPersonName(tblIssue.getMstUser() == null ? "" : tblIssue.getMstUser().getUserName());
        if (tblIssue.getTblMoldMaintenanceRemodeling() != null) {
            tblIssueVo.setMainteOrRemodel(tblIssue.getTblMoldMaintenanceRemodeling().getMainteOrRemodel() == null ? 0 : tblIssue.getTblMoldMaintenanceRemodeling().getMainteOrRemodel());
        } else {
            tblIssueVo.setMainteOrRemodel(0);
        }
        tblIssueVo.setMeasureSummary(tblIssue.getMeasureSummary() == null ? "" : tblIssue.getMeasureSummary());
        tblIssueVo.setMeasuerCompletedDate(tblIssue.getMeasuerCompletedDate() == null ? "" : sdf.format(tblIssue.getMeasuerCompletedDate()));

        // 金型メンテ日　と　メンテＩＤ
        if (tblIssue.getTblMoldMaintenanceRemodeling() != null) {
            tblIssueVo.setMainteDate(tblIssue.getTblMoldMaintenanceRemodeling().getMainteDate() == null ? "" : sdf.format(tblIssue.getTblMoldMaintenanceRemodeling().getMainteDate()));
        } else {
            tblIssueVo.setMainteDate(null);
        }
        tblIssueVo.setMainTenanceId(tblIssue.getMainTenanceId());

        // 設備メンテ日
        if (tblIssue.getTblMachineMaintenanceRemodeling() != null) {
            tblIssueVo.setMachineMainteDate(tblIssue.getTblMachineMaintenanceRemodeling().getMainteDate() == null ? "" : sdf.format(tblIssue.getTblMachineMaintenanceRemodeling().getMainteDate()));
        } else {
            tblIssueVo.setMachineMainteDate(null);
        }
        tblIssueVo.setMachineMainTenanceId(tblIssue.getMachineMainTenanceId());

        String strReportFilePath01 = tblIssue.getReportFilePath01();
        if (null != strReportFilePath01 && !"".equals(strReportFilePath01)) {
            tblIssueVo.setReportFilePath01(strReportFilePath01);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath01);
            if (null != tblUploadFile) {
                tblIssueVo.setReportFilePath01Name(tblUploadFile.getUploadFileName());
            } else {
                tblIssueVo.setReportFilePath01Name("");
            }
        } else {
            tblIssueVo.setReportFilePath01("");
            tblIssueVo.setReportFilePath01Name("");
        }

        String strReportFilePath02 = tblIssue.getReportFilePath02();
        if (null != strReportFilePath02 && !"".equals(strReportFilePath02)) {
            tblIssueVo.setReportFilePath02(strReportFilePath02);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath02);
            if (null != tblUploadFile) {
                tblIssueVo.setReportFilePath02Name(tblUploadFile.getUploadFileName());
            } else {
                tblIssueVo.setReportFilePath02Name("");
            }
        } else {
            tblIssueVo.setReportFilePath02("");
            tblIssueVo.setReportFilePath02Name("");
        }

        String strReportFilePath03 = tblIssue.getReportFilePath03();
        if (null != strReportFilePath03 && !"".equals(strReportFilePath03)) {
            tblIssueVo.setReportFilePath03(strReportFilePath03);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath03);
            if (null != tblUploadFile) {
                tblIssueVo.setReportFilePath03Name(tblUploadFile.getUploadFileName());
            } else {
                tblIssueVo.setReportFilePath03Name("");
            }
        } else {
            tblIssueVo.setReportFilePath03("");
            tblIssueVo.setReportFilePath03Name("");
        }

        String strReportFilePath04 = tblIssue.getReportFilePath04();
        if (null != strReportFilePath04 && !"".equals(strReportFilePath04)) {
            tblIssueVo.setReportFilePath04(strReportFilePath04);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath04);
            if (null != tblUploadFile) {
                tblIssueVo.setReportFilePath04Name(tblUploadFile.getUploadFileName());
            } else {
                tblIssueVo.setReportFilePath04Name("");
            }
        } else {
            tblIssueVo.setReportFilePath04("");
            tblIssueVo.setReportFilePath04Name("");
        }

        String strReportFilePath05 = tblIssue.getReportFilePath05();
        if (null != strReportFilePath05 && !"".equals(strReportFilePath05)) {
            tblIssueVo.setReportFilePath05(strReportFilePath05);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath05);
            if (null != tblUploadFile) {
                tblIssueVo.setReportFilePath05Name(tblUploadFile.getUploadFileName());
            } else {
                tblIssueVo.setReportFilePath05Name("");
            }
        } else {
            tblIssueVo.setReportFilePath05("");
            tblIssueVo.setReportFilePath05Name("");
        }

        String strReportFilePath06 = tblIssue.getReportFilePath06();
        if (null != strReportFilePath06 && !"".equals(strReportFilePath06)) {
            tblIssueVo.setReportFilePath06(strReportFilePath06);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath06);
            if (null != tblUploadFile) {
                tblIssueVo.setReportFilePath06Name(tblUploadFile.getUploadFileName());
            } else {
                tblIssueVo.setReportFilePath06Name("");
            }
        } else {
            tblIssueVo.setReportFilePath06("");
            tblIssueVo.setReportFilePath06Name("");
        }

        String strReportFilePath07 = tblIssue.getReportFilePath07();
        if (null != strReportFilePath07 && !"".equals(strReportFilePath07)) {
            tblIssueVo.setReportFilePath07(strReportFilePath07);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath07);
            if (null != tblUploadFile) {
                tblIssueVo.setReportFilePath07Name(tblUploadFile.getUploadFileName());
            } else {
                tblIssueVo.setReportFilePath07Name("");
            }
        } else {
            tblIssueVo.setReportFilePath07("");
            tblIssueVo.setReportFilePath07Name("");
        }

        String strReportFilePath08 = tblIssue.getReportFilePath08();
        if (null != strReportFilePath08 && !"".equals(strReportFilePath08)) {
            tblIssueVo.setReportFilePath08(strReportFilePath08);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath08);
            if (null != tblUploadFile) {
                tblIssueVo.setReportFilePath08Name(tblUploadFile.getUploadFileName());
            } else {
                tblIssueVo.setReportFilePath08Name("");
            }
        } else {
            tblIssueVo.setReportFilePath08("");
            tblIssueVo.setReportFilePath08Name("");
        }

        String strReportFilePath09 = tblIssue.getReportFilePath09();
        if (null != strReportFilePath09 && !"".equals(strReportFilePath09)) {
            tblIssueVo.setReportFilePath09(strReportFilePath09);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath09);
            if (null != tblUploadFile) {
                tblIssueVo.setReportFilePath09Name(tblUploadFile.getUploadFileName());
            } else {
                tblIssueVo.setReportFilePath09Name("");
            }
        } else {
            tblIssueVo.setReportFilePath09("");
            tblIssueVo.setReportFilePath09Name("");
        }

        String strReportFilePath10 = tblIssue.getReportFilePath10();
        if (null != strReportFilePath10 && !"".equals(strReportFilePath10)) {
            tblIssueVo.setReportFilePath10(strReportFilePath10);
            TblUploadFile tblUploadFile = tblUploadFileService.getTblUploadFile(strReportFilePath10);
            if (null != tblUploadFile) {
                tblIssueVo.setReportFilePath10Name(tblUploadFile.getUploadFileName());
            } else {
                tblIssueVo.setReportFilePath10Name("");
            }
        } else {
            tblIssueVo.setReportFilePath10("");
            tblIssueVo.setReportFilePath10Name("");
        }

        if (tblIssue.getTblIssueImageFile() != null && tblIssue.getTblIssueImageFile().size() > 0) {
            Iterator<TblIssueImageFile> tblIssueImageFile = tblIssue.getTblIssueImageFile().iterator();
            while (tblIssueImageFile.hasNext()) {
                TblIssueImageFile input = tblIssueImageFile.next();
                tblIssueImageFiles.add(input);
            }
            for (int i = 0; i < tblIssueImageFiles.size(); i++) {
                tblIssueImageFileVos = new TblIssueImageFileVo();
                TblIssueImageFile out = tblIssueImageFiles.get(i);
                tblIssueImageFileVos.setFileUuid(out.getFileUuid() == null ? "" : out.getFileUuid());
                tblIssueImageFileVos.setFileType(out.getFileType() == null ? 0 : out.getFileType());
                tblIssueImageFileVos.setFileExtension(out.getFileExtension() == null ? "" : out.getFileExtension());
                tblIssueImageFileVos.setRemarks(out.getRemarks() == null ? "" : out.getRemarks());
                tblIssueImageFileVos.setThumbnailFileUuid(out.getThumbnailFileUuid() == null ? "" : out.getThumbnailFileUuid());
                tblIssueImageFileVos.setIssueId(out.getTblIssueImageFilePK().getIssueId());
                tblIssueImageFileVos.setSeq(out.getTblIssueImageFilePK().getSeq());
                if (out.getTakenDate() != null) {
                    tblIssueImageFileVos.setTakenDate(sdf.format(out.getTakenDate()));
                }
                TblIssueImageFileVoList.add(tblIssueImageFileVos);
            }
            tblIssueVo.setTblIssueImageFileVoList(TblIssueImageFileVoList);
        }

        tblIssueVoList.add(tblIssueVo);
        response.setTblIssueVoList(tblIssueVoList);
        return response;
    }

    /**
     * 異常一覧 CSV出力 異常情報テーブルから検索条件にあてはまるデータを検索しCSVファイルに出力する。
     * 出力順は打上の降順。検索条件が指定されていない場合は全件を出力する。
     *
     * @param measureStatus
     * @param department
     * @param reportDate
     * @param reportDateFrom
     * @param reportDateTo
     * @param measureDueDate
     * @param measureDueDateFrom
     * @param measureDueDateTo
     * @param moldId
     * @param moldName
     * @param componentCode
     * @param componentName
     * @param machineId
     * @param machineName
     * @param measureStatusOperand
     * @param reportPersonName
     * @param reportPhase
     * @param reportCategory1
     * @param reportCategory2
     * @param reportCategory3
     * @param memo01
     * @param memo02
     * @param memo03
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getIssuesCsv(@QueryParam("measureStatus") int measureStatus,
            @QueryParam("measureStatusOperand") int measureStatusOperand, // Added parameter "measureStatusOperand"  for Issue: KM-743
            @QueryParam("department") String department,
            @QueryParam("reportDate") String reportDate,
            @QueryParam("reportDateForm") String reportDateFrom,
            @QueryParam("reportDateTo") String reportDateTo,
            @QueryParam("measureDueDate") String measureDueDate,
            @QueryParam("measureDueDateFrom") String measureDueDateFrom,
            @QueryParam("measureDueDateTo") String measureDueDateTo,
            @QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
            @QueryParam("reportPersonName") String reportPersonName, // KM-359 打上一覧に報告者追加
            @QueryParam("issueReportPhase") int reportPhase,
            @QueryParam("issueReportCategory1") int reportCategory1,
            @QueryParam("issueReportCategory2") int reportCategory2,
            @QueryParam("issueReportCategory3") int reportCategory3,
            @QueryParam("memo01") String memo01,
            @QueryParam("memo02") String memo02,
            @QueryParam("memo03") String memo03,
            @QueryParam("happenedAt") String happenedAt
    ) {
        // Added parameter "measureStatusOperand"  for Issue: KM-743
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        tblIssueService.outMoldTypeOfChoice(loginUser.getLangId());
        return tblIssueService.getIssuesCsv(measureStatus,measureStatusOperand, department, reportDate, reportDateFrom, reportDateTo,
                measureDueDate, measureDueDateFrom, measureDueDateTo, moldId, moldName,componentCode, componentName, machineId, machineName, reportPersonName, 
                reportPhase, reportCategory1, reportCategory2, reportCategory3, memo01, memo02, memo03, happenedAt, loginUser);
    }

    /**
     * 通常打上 異常情報を登録
     *
     * @param tblIssueVo
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblIssueVo postIssue(TblIssueVo tblIssueVo) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        TblIssueVo tblIssueVos = tblIssueService.createIssue(tblIssueVo, loginUser);
        return tblIssueVos;
    }

    /**
     * バッチで異常テーブルデータを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extissue")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblIssueList getExtChoicesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return tblIssueService.getExtIssuesByBatch(latestExecutedDate, moldUuid);
    }

    /**
     * バッチで異常テーブルデータを取得
     *
     * @param latestExecutedDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachineissue")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblIssueList getExtMachineIssuesByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("machineUuid") String machineUuid) {
        return tblIssueService.getExtMachineIssuesByBatch(latestExecutedDate, machineUuid);
    }
    
    /**
     * 異常一覧,通常打上,金型メンテナンス開始入力 異常情報複数取得(ページ表示)
     *
     * @param measureStatus
     * @param measureStatusOperand // Added parameter "measureStatusOperand"  for Issue: KM-743
     * @param department
     * @param reportDate
     * @param reportDateFrom
     * @param reportDateTo
     * @param measureDueDate
     * @param measureDueDateFrom
     * @param measureDueDateTo
     * @param moldId
     * @param moldName
     * @param componentCode
     * @param componentName
     * @param machineId
     * @param machineName
     * @param mainTenanceId
     * @param reportPersonName
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param reportPhase
     * @param reportCategory1
     * @param reportCategory2
     * @param reportCategory3
     * @param memo01
     * @param memo02
     * @param memo03
     * @param happenedAt
     * @param moldUuid
     * @param machineUuid
     * @return
     */
    @GET
    @Path("getissues")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblIssueList getIssuesByPage(@QueryParam("measureStatus") int measureStatus,
            @QueryParam("measureStatusOperand") int measureStatusOperand, // Added parameter "measureStatusOperand"  for Issue: KM-743
            @QueryParam("department") String department,
            @QueryParam("reportDate") String reportDate,
            @QueryParam("reportDateForm") String reportDateFrom,
            @QueryParam("reportDateTo") String reportDateTo,
            @QueryParam("measureDueDate") String measureDueDate,
            @QueryParam("measureDueDateFrom") String measureDueDateFrom,
            @QueryParam("measureDueDateTo") String measureDueDateTo,
            @QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("componentCode") String componentCode,
            @QueryParam("componentName") String componentName,
            @QueryParam("machineId") String machineId,
            @QueryParam("machineName") String machineName,
            @QueryParam("mainTenanceId") String mainTenanceId,
            @QueryParam("reportPersonName") String reportPersonName, // KM-359 打上一覧に報告者追加
            @QueryParam("sidx") String sidx, // ソートキー
            @QueryParam("sord") String sord, // ソート順
            @QueryParam("pageNumber") int pageNumber, // ページNo
            @QueryParam("pageSize") int pageSize, // ページSize
            @QueryParam("issueReportPhase") int reportPhase,
            @QueryParam("issueReportCategory1") int reportCategory1,
            @QueryParam("issueReportCategory2") int reportCategory2,
            @QueryParam("issueReportCategory3") int reportCategory3,
            @QueryParam("memo01") String memo01,
            @QueryParam("memo02") String memo02,
            @QueryParam("memo03") String memo03,
            @QueryParam("happenedAt") String happenedAt,
            @QueryParam("moldUuid") String moldUuid,
            @QueryParam("machineUuid") String machineUuid
    ) {
                
        TblIssueList response = new TblIssueList();
        List<TblIssueVo> TblIssueVoList = new ArrayList<>();
        // Added parameter "measureStatusOperand"  for Issue: KM-743
        List<TblIssue> list = tblIssueService.getIssuesByPage(measureStatus,measureStatusOperand, department, reportDate,
                reportDateFrom, reportDateTo, measureDueDate, measureDueDateFrom, measureDueDateTo,
                moldId, moldName, componentCode, componentName, machineId, machineName, mainTenanceId, reportPersonName, response, sidx, sord,
                pageNumber, pageSize, true, reportPhase, reportCategory1, reportCategory2, reportCategory3, memo01, memo02, memo03, happenedAt, moldUuid, machineUuid);
        
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        
        // 選択肢Map
        Map<String, String> choiceMap = new HashMap<String, String>();

        choiceMap = mstChoiceService.getChoiceMap(loginUser.getLangId(), ISSUE_ARRAY);
        
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        TblIssueVo tblIssueVo;
        
        for (int i = 0; i < list.size(); i++) {
            TblIssue tblIssue = list.get(i);
            tblIssueVo = new TblIssueVo();
            tblIssueVo.setId(tblIssue.getId());
            tblIssueVo.setReportDate(tblIssue.getReportDate() == null ? "" : sdf.format(tblIssue.getReportDate()));
            tblIssueVo.setHappenedAt(tblIssue.getHappenedAt() == null ? "" : sdf.format(tblIssue.getHappenedAt()));
            tblIssueVo.setMeasureDueDate(tblIssue.getMeasureDueDate() == null ? "" : sdf.format(tblIssue.getMeasureDueDate()));
            tblIssueVo.setMeasureStatus(null == tblIssue.getMeasureStatus() ? CommonConstants.ISSUE_MEASURE_STATUS_NOTYET : tblIssue.getMeasureStatus());
            boolean excFlag = false;
            String companyId = null;
            // 金型
            if (tblIssue.getMstMold() != null) {
                // 金型が外部管理されているかどうかチェック
                if (FileUtil.checkExternal(entityManager, mstDictionaryService, tblIssue.getMstMold().getMoldId(), loginUser).isError()) {
                    // 金型が外部管理されている
                    companyId = tblIssue.getMstMold().getCompanyId();
                    excFlag = true;
                }
                tblIssueVo.setMoldUuid(tblIssue.getMstMold().getUuid());
                tblIssueVo.setMoldId(tblIssue.getMstMold().getMoldId());
                tblIssueVo.setMoldName(tblIssue.getMstMold().getMoldName());
                tblIssueVo.setMoldInstallationSiteName(tblIssue.getMstMold().getInstllationSiteName());
            } else {
                tblIssueVo.setMoldUuid("");
                tblIssueVo.setMoldId("");
                tblIssueVo.setMoldName("");
                tblIssueVo.setMoldInstallationSiteName("");
            }
            // 部品
            if (tblIssue.getMstComponent() != null) {
                tblIssueVo.setComponentId(tblIssue.getMstComponent().getId());
                tblIssueVo.setComponentCode(tblIssue.getMstComponent().getComponentCode());
                tblIssueVo.setComponentName(tblIssue.getMstComponent().getComponentName());
            } else {
                tblIssueVo.setComponentId("");
                tblIssueVo.setComponentCode("");
                tblIssueVo.setComponentName("");
            }
            // ロット番号
            tblIssueVo.setLotNumber(tblIssue.getLotNumber());
            // 工程
            tblIssueVo.setProcedureId(tblIssue.getProcedureId());
            tblIssueVo.setProcedureCode(tblIssue.getMstProcedure() == null ? "" : tblIssue.getMstProcedure().getProcedureCode());
            tblIssueVo.setProcedureName(tblIssue.getMstProcedure() == null ? "" : tblIssue.getMstProcedure().getProcedureName());

            // 設備
            if (tblIssue.getMstMachine() != null) {
                if (excFlag != true) {
                    String conpanyId = tblIssue.getMstMachine().getCompanyId();
                    if (conpanyId != null && !"".equals(conpanyId)) {
                        // 金型が外部管理されているかどうかチェック
                        if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", conpanyId, loginUser).isError()) {
                            // 金型が外部管理されている
                            excFlag = true;
                            companyId = conpanyId;
                        }
                    }
                }
                tblIssueVo.setMachineUuid(tblIssue.getMstMachine().getUuid());
                tblIssueVo.setMachineId(tblIssue.getMstMachine().getMachineId());
                tblIssueVo.setMachineName(tblIssue.getMstMachine().getMachineName());
            } else {
                tblIssueVo.setMachineUuid("");
                tblIssueVo.setMachineId("");
                tblIssueVo.setMachineName("");

            }
            if (excFlag == true) {
                tblIssueVo.setExternalFlg(1);

                Map<String, String> extChoiceMap = extMstChoiceService.getChoiceMap(companyId, loginUser.getLangId(), ISSUE_ARRAY);
                tblIssueVo.setReportDepartment(tblIssue.getReportDepartment() == null ? "" : String.valueOf(tblIssue.getReportDepartment()));
                tblIssueVo.setReportDepartmentName(
                        FileUtil.getStr(extChoiceMap.get(USER_DEPARTMENT + String.valueOf(tblIssue.getReportDepartment()))));

                tblIssueVo.setReportPhase(tblIssue.getReportPhase() == null ? "" : String.valueOf(tblIssue.getReportPhase()));
                tblIssueVo.setReportPhaseText(FileUtil.getStr(extChoiceMap.get(ISSUE_REPORT_PHASE + String.valueOf(tblIssue.getReportPhase()))));
                tblIssueVo.setReportCategory1(tblIssue.getReportCategory1() == null ? "" : String.valueOf(tblIssue.getReportCategory1()));
                tblIssueVo.setReportCategory1Text(FileUtil.getStr(extChoiceMap.get(ISSUE_REPORT_CATEGORY1 + String.valueOf(tblIssue.getReportCategory1()))));
                tblIssueVo.setReportCategory2(tblIssue.getReportCategory2() == null ? "" : String.valueOf(tblIssue.getReportCategory2()));
                tblIssueVo.setReportCategory2Text(FileUtil.getStr(extChoiceMap.get(ISSUE_REPORT_CATEGORY2 + String.valueOf(tblIssue.getReportCategory2()))));
                tblIssueVo.setReportCategory3(tblIssue.getReportCategory3() == null ? "" : String.valueOf(tblIssue.getReportCategory3()));
                tblIssueVo.setReportCategory3Text(FileUtil.getStr(extChoiceMap.get(ISSUE_REPORT_CATEGORY3 + String.valueOf(tblIssue.getReportCategory3()))));
            } else {
                tblIssueVo.setExternalFlg(0);
                tblIssueVo.setReportDepartment(tblIssue.getReportDepartment() == null ? "" : String.valueOf(tblIssue.getReportDepartment()));
                tblIssueVo.setReportDepartmentName(
                        FileUtil.getStr(choiceMap.get(USER_DEPARTMENT + String.valueOf(tblIssue.getReportDepartment()))));

                tblIssueVo.setReportPhase(tblIssue.getReportPhase() == null ? "" : String.valueOf(tblIssue.getReportPhase()));
                tblIssueVo.setReportPhaseText(FileUtil.getStr(choiceMap.get(ISSUE_REPORT_PHASE + String.valueOf(tblIssue.getReportPhase()))));
                tblIssueVo.setReportCategory1(tblIssue.getReportCategory1() == null ? "" : String.valueOf(tblIssue.getReportCategory1()));
                tblIssueVo.setReportCategory1Text(FileUtil.getStr(choiceMap.get(ISSUE_REPORT_CATEGORY1 + String.valueOf(tblIssue.getReportCategory1()))));
                tblIssueVo.setReportCategory2(tblIssue.getReportCategory2() == null ? "" : String.valueOf(tblIssue.getReportCategory2()));
                tblIssueVo.setReportCategory2Text(FileUtil.getStr(choiceMap.get(ISSUE_REPORT_CATEGORY2 + String.valueOf(tblIssue.getReportCategory2()))));
                tblIssueVo.setReportCategory3(tblIssue.getReportCategory3() == null ? "" : String.valueOf(tblIssue.getReportCategory3()));
                tblIssueVo.setReportCategory3Text(FileUtil.getStr(choiceMap.get(ISSUE_REPORT_CATEGORY3 + String.valueOf(tblIssue.getReportCategory3()))));
            }
            
            tblIssueVo.setMemo01(tblIssue.getMemo01() == null ? "" : tblIssue.getMemo01());
            tblIssueVo.setMemo02(tblIssue.getMemo02() == null ? "" : tblIssue.getMemo02());
            tblIssueVo.setMemo03(tblIssue.getMemo03() == null ? "" : tblIssue.getMemo03());
            tblIssueVo.setIssue(tblIssue.getIssue() == null ? "" : tblIssue.getIssue());
            tblIssueVo.setMeasureSummary(tblIssue.getMeasureSummary() == null ? "" : tblIssue.getMeasureSummary());
            tblIssueVo.setMeasuerCompletedDate(tblIssue.getMeasuerCompletedDate() == null ? "" : sdf.format(tblIssue.getMeasuerCompletedDate()));
            // 金型メンテ日
            if (tblIssue.getTblMoldMaintenanceRemodeling() != null) {
                tblIssueVo.setMainteDate(tblIssue.getTblMoldMaintenanceRemodeling().getMainteDate() == null ? "" : sdf.format(tblIssue.getTblMoldMaintenanceRemodeling().getMainteDate()));
                if (tblIssue.getTblMoldMaintenanceRemodeling().getMstUser() != null) {
                    tblIssueVo.setMoldMaintenancePersonName(tblIssue.getTblMoldMaintenanceRemodeling().getMstUser().getUserName());
                } else {
                    tblIssueVo.setMoldMaintenancePersonName("");
                }
            } else {
                tblIssueVo.setMainteDate(null);
                tblIssueVo.setMoldMaintenancePersonName("");
            }

            // 設備メンテ日
            if (tblIssue.getTblMachineMaintenanceRemodeling() != null) {
                tblIssueVo.setMachineMainteDate(tblIssue.getTblMachineMaintenanceRemodeling().getMainteDate() == null ? "" : sdf.format(tblIssue.getTblMachineMaintenanceRemodeling().getMainteDate()));
                if (tblIssue.getTblMachineMaintenanceRemodeling().getMstUser() != null) {
                    tblIssueVo.setMachineMaintenancePersonName(tblIssue.getTblMachineMaintenanceRemodeling().getMstUser().getUserName());
                } else {
                    tblIssueVo.setMachineMaintenancePersonName("");
                }
            } else {
                tblIssueVo.setMachineMainteDate(null);
                tblIssueVo.setMachineMaintenancePersonName("");
            }

            //報告者
            if (tblIssue.getMstUser() != null) {
                tblIssueVo.setIssueReportPersonName(tblIssue.getMstUser().getUserName());
            } else {
                tblIssueVo.setIssueReportPersonName("");
            }
            tblIssueVo.setQuantity(tblIssue.getQuantity());
            tblIssueVo.setShotCountAtIssue(tblIssue.getShotCountAtIssue());
            TblIssueVoList.add(tblIssueVo);
        }
        response.setTblIssueVoList(TblIssueVoList);
        return response;
    }
}
