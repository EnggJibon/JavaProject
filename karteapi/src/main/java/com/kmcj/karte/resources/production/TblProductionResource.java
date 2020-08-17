/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kmcj.karte.resources.work.*;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.component.lot.TblComponentLot;
import com.kmcj.karte.resources.component.lot.TblComponentLotService;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationService;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVoList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.direction.TblDirection;
import com.kmcj.karte.resources.direction.TblDirectionService;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.machine.MstMachineVo;
import com.kmcj.karte.resources.machine.daily.report.TblMachineDailyReportService;
import com.kmcj.karte.resources.machine.daily.report.TblMachineDailyReportVo;
import com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetail;
import com.kmcj.karte.resources.machine.dailyreport2.MachineDailyReport2Service;
import com.kmcj.karte.resources.machine.dailyreport2.PeriodProductionUpdater;
import com.kmcj.karte.resources.machine.dailyreport2.TotalUpdater;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtnService;
import com.kmcj.karte.resources.material.stock.TblMaterialStockService;
import com.kmcj.karte.resources.mold.MstMoldDetail;
import com.kmcj.karte.resources.mold.MstMoldService;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.production.defect.TblProductionDefectService;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import com.kmcj.karte.resources.production.detail.TblProductionDetailList;
import com.kmcj.karte.resources.production.detail.TblProductionDetailService;
import com.kmcj.karte.resources.production.detail.TblProductionDetailVo;
import com.kmcj.karte.resources.production.lot.balance.TblProductionLotBalance;
import com.kmcj.karte.resources.production.lot.balance.TblProductionLotBalanceService;
import com.kmcj.karte.resources.production.lot.balance.TblProductionLotBalanceVo;
import com.kmcj.karte.resources.production.machine.proc.cond.TblProductionMachineProcCond;
import com.kmcj.karte.resources.production.machine.proc.cond.TblProductionMachineProcCondHolder;
import com.kmcj.karte.resources.production.machine.proc.cond.TblProductionMachineProcCondService;
import com.kmcj.karte.resources.production.suspension.TblProductionSuspensionList;
import com.kmcj.karte.resources.production.suspension.TblProductionSuspensionVo;
import com.kmcj.karte.resources.production.suspension.TblProductionSuspensionlService;
import com.kmcj.karte.resources.stock.TblStockService;
import com.kmcj.karte.resources.work.close.entry.TblWorkCloseEntryService;
import com.kmcj.karte.resources.work.phase.MstWorkPhase;
import com.kmcj.karte.resources.work.phase.MstWorkPhaseService;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.apache.commons.lang.StringUtils;

/**
 * 作業実績テーブルリソース
 *
 * @author t.ariki
 */
@RequestScoped
@Path("production")
public class TblProductionResource {

    @Context
    private UriInfo context;

    @Context
    ContainerRequestContext requestContext;

    @Inject
    MstDictionaryService mstDictionaryService;

    @Inject
    private CnfSystemService cnfSystemService;

    @Inject
    MstChoiceService mstChoiceService;

    @Inject
    TblWorkService tblWorkService;

    @Inject
    TblWorkCloseEntryService tblWorkCloseEntryService;

    @Inject
    MstWorkPhaseService mstWorkPhaseService;

    @Inject
    MstMoldService mstMoldService;

    @Inject
    TblDirectionService tblDirectionService;

    @Inject
    MstComponentService mstComponentService;

    @Inject
    TblProductionService tblProductionService;

    @Inject
    TblProductionDetailService tblProductionDetailService;

    @Inject
    TblProductionLotBalanceService tblProductionLotBalanceService;

    @Inject
    MstProcedureService mstProcedureService;

    @Inject
    TblProductionMachineProcCondService tblProductionMachineProcCondService;

    @Inject
    TblMachineDailyReportService tblMachineDailyReportService;

    @Inject
    TblMaintenanceCyclePtnService tblMaintenanceCyclePtnService;
    
    @Inject
    MstMachineService mstMachineService;
    
    @Inject
    TblProductionSuspensionlService tblProductionSuspensionlService;
    
    @Inject
    private TblStockService tblStockService;
    
    @Inject
    private TblMaterialStockService tblMaterialStockService;
    
    @Inject
    private TblComponentLotRelationService tblComponentLotRelationService;
    
    @Inject
    private TblComponentLotService tblComponentLotService;
    
    @Inject
    private MachineDailyReport2Service machineDailyReport2Service;
    
    @Inject
    private TblProductionDefectService tblProductionDefectService;

    private Logger logger = Logger.getLogger(TblProductionResource.class.getName());

    @Inject
    private PeriodProductionUpdater periodProductionUpdater;

    @Inject
    private TotalUpdater totalUpdater;

    private final static String COLUMN_SEPARATOR = ", ";

    private static final String LINE_END = "\r\n"; 

    public TblProductionResource() {
    }

    /**
     * 生産計画に関連づく生産実績明細の件数取得
     *
     * @param productionPlanId
     * @return
     */
    @GET
    @Path("linkedwithplan/count")
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getProductionsCountLinkedWithPlan(
            @QueryParam("productionPlanId") String productionPlanId) {
        return tblProductionDetailService.getProductionsCountLinkedWithPlan(productionPlanId);
    }

    /**
     * 生産計画に関連づく生産実績明細取得
     *
     * @param productionPlanId
     * @return
     */
    @GET
    @Path("linkedwithplan")
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionDetailList getProductionsLinkedWithPlan(
            @QueryParam("productionPlanId") String productionPlanId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblProductionDetailList tblProductionDetailList = tblProductionDetailService.getProductionsLinkedWithPlan(productionPlanId);

        /**
         * 部署名のために部署リストを選択肢マスタより取得しておく
         */
        MstChoiceList departments = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");

        /**
         * 作業内容名称取得のために選択肢マスタより取得しておく
         */
        MstChoiceList workCategories = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_work.work_category");

        /*
         * 各種IDをコード値に変換して設定
         */
        ArrayList<TblProductionDetail> codeBindedTblProductionDetails = new ArrayList<>(); // 結果再格納用リスト
        if (!tblProductionDetailList.getTblProductionDetails().isEmpty()) {
            for (TblProductionDetail tblProductionDetail : tblProductionDetailList.getTblProductionDetails()) {
                if (tblProductionDetail.getTblProduction() != null) {
                    setOuterCodesForHeader(tblProductionDetail.getTblProduction(), loginUser, departments, workCategories);
                }
                setOuterCodesForDetails(tblProductionDetail, loginUser);
                codeBindedTblProductionDetails.add(tblProductionDetail);
            }
        }
        TblProductionDetailList response = new TblProductionDetailList();
        response.setTblProductionDetails(codeBindedTblProductionDetails);
        return response;
    }

    /**
     * 生産実績テーブル条件検索(生産実績明細行取得)
     *
     * @param componentCode
     * @param productionDateFrom
     * @param productionDateTo
     * @param directionCode
     * @param userId
     * @param userName
     * @param department
     * @param moldId
     * @param moldType
     * @param machineId
     * @param workStartDateTime
     * @param nextDayWorkStartDateTime
     * @return
     */
    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionDetailList getProductionsByCondition(
            @QueryParam("componentCode") String componentCode // 部品コード
            , @QueryParam("productionDateFrom") String productionDateFrom // 生産日From yyyy/MM/dd
            , @QueryParam("productionDateTo") String productionDateTo // 生産日To yyyy/MM/dd
            , @QueryParam("directionCode") String directionCode // 手配番号
            , @QueryParam("userId") String userId // 生産者ID
            , @QueryParam("userName") String userName // 生産者氏名
            , @QueryParam("department") Integer department // 生産場所
            , @QueryParam("moldId") String moldId // 金型ID
            , @QueryParam("moldType") Integer moldType //  金型種類
            , @QueryParam("machineId") String machineId // 設備ID
            , @QueryParam("workStartDateTime") String workStartDateTime // 選択日付の業務開始時刻
            , @QueryParam("nextDayWorkStartDateTime") String nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
    ) {

        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        // クエリパラムログ出力
        logger.log(Level.FINE, "QueryParams'{'"
                + ", componentCode:{0}, productionDateFrom:{1}, productionDateTo:{2}, directionCode:{3}, userId:{4}, userName:{5}, department:{6}, moldId:{7}, moldType:{8}}", new Object[]{componentCode, productionDateFrom, productionDateTo, directionCode, userId, userName, department, moldId, moldType}
        );
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        // 日付項目をDate型(yyyy/MM/dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        java.util.Date formatProductionDateFrom = null;
        java.util.Date formatProductionDateTo = null;

        SimpleDateFormat sdf1 = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        java.util.Date formatWorkStartDateTime = null;
        java.util.Date formatNextDayWorkStartDateTime = null;
        try {
            if (productionDateFrom != null && !"".equals(productionDateFrom)) {
                formatProductionDateFrom = sdf.parse(productionDateFrom);
            }
            if (productionDateTo != null && !"".equals(productionDateTo)) {
                formatProductionDateTo = sdf.parse(productionDateTo);
            }
            if (workStartDateTime != null && !"".equals(workStartDateTime)) {
                formatWorkStartDateTime = sdf1.parse(workStartDateTime);
            }
            if (nextDayWorkStartDateTime != null && !"".equals(nextDayWorkStartDateTime)) {
                formatNextDayWorkStartDateTime = sdf1.parse(nextDayWorkStartDateTime);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblProductionResource.class.getName()).log(Level.WARNING, null, "日付形式不正 productioDateFrom[" + productionDateFrom + "], productioDateTo[" + productionDateTo + "]");
        }
        // データ取得
        TblProductionDetailList tblProductionDetailList = tblProductionDetailService.getProductionDetailsByCondition(
                componentCode, formatProductionDateFrom, formatProductionDateTo, directionCode, userId, userName, department, moldId, moldType, machineId, formatWorkStartDateTime, formatNextDayWorkStartDateTime
        );

        /**
         * 部署名のために部署リストを選択肢マスタより取得しておく
         */
        MstChoiceList departments = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");

        /**
         * 作業内容名称取得のために選択肢マスタより取得しておく
         */
        MstChoiceList workCategories = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_work.work_category");

        /*
         * 各種IDをコード値に変換して設定
         */
        ArrayList<TblProductionDetail> codeBindedTblProductionDetails = new ArrayList<>(); // 結果再格納用リスト
        if (!tblProductionDetailList.getTblProductionDetails().isEmpty()) {
            for (TblProductionDetail tblProductionDetail : tblProductionDetailList.getTblProductionDetails()) {
                if (tblProductionDetail.getTblProduction() != null) {
                    setOuterCodesForHeader(tblProductionDetail.getTblProduction(), loginUser, departments, workCategories);
                }
                setOuterCodesForDetails(tblProductionDetail, loginUser);
                codeBindedTblProductionDetails.add(tblProductionDetail);
            }
        }
        TblProductionDetailList response = new TblProductionDetailList();
        response.setTblProductionDetails(codeBindedTblProductionDetails);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 生産実績テーブル条件検索件数取得(生産実績明細行取得)
     *
     * @param componentCode
     * @param productionDateFrom
     * @param productionDateTo
     * @param directionCode
     * @param userId
     * @param userName
     * @param department
     * @param moldId
     * @param moldType
     * @param machineId
     * @param workStartDateTime
     * @param nextDayWorkStartDateTime
     * @return
     */
    @GET
    @Path("search/count")
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getProductionsCountByCondition(
            @QueryParam("componentCode") String componentCode // 部品コード
            , @QueryParam("productionDateFrom") String productionDateFrom // 生産日From yyyy/MM/dd
            , @QueryParam("productionDateTo") String productionDateTo // 生産日To yyyy/MM/dd
            , @QueryParam("directionCode") String directionCode // 手配番号
            , @QueryParam("userId") String userId // 生産者ID
            , @QueryParam("userName") String userName // 生産者氏名
            , @QueryParam("department") Integer department // 生産場所
            , @QueryParam("moldId") String moldId // 金型ID
            , @QueryParam("moldType") Integer moldType //  金型種類
            , @QueryParam("machineId") String machineId // 設備ID
            , @QueryParam("workStartDateTime") String workStartDateTime // 選択日付の業務開始時刻
            , @QueryParam("nextDayWorkStartDateTime") String nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
    ) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
// 日付項目をDate型(yyyy/MM/dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        java.util.Date formatProductionDateFrom = null;
        java.util.Date formatProductionDateTo = null;

        SimpleDateFormat sdf1 = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        java.util.Date formatWorkStartDateTime = null;
        java.util.Date formatNextDayWorkStartDateTime = null;
        try {
            if (productionDateFrom != null && !"".equals(productionDateFrom)) {
                formatProductionDateFrom = sdf.parse(productionDateFrom);
            }
            if (productionDateTo != null && !"".equals(productionDateTo)) {
                formatProductionDateTo = sdf.parse(productionDateTo);
            }
            if (workStartDateTime != null && !"".equals(workStartDateTime)) {
                formatWorkStartDateTime = sdf1.parse(workStartDateTime);
            }
            if (nextDayWorkStartDateTime != null && !"".equals(nextDayWorkStartDateTime)) {
                formatNextDayWorkStartDateTime = sdf1.parse(nextDayWorkStartDateTime);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblProductionResource.class.getName()).log(Level.WARNING, null, "日付形式不正 productioDateFrom[" + productionDateFrom + "], productioDateTo[" + productionDateTo + "]");
        }

        // 件数を取得
        CountResponse response = tblProductionDetailService.getProductionDetailsCountByCondition(
                componentCode, formatProductionDateFrom, formatProductionDateTo, directionCode, userId, userName, department, moldId, moldType, machineId, formatWorkStartDateTime, formatNextDayWorkStartDateTime
        );

        // システム上限を取得
        CnfSystem cnf = cnfSystemService.findByKey("system", "max_list_record_count");
        long sysCount = Long.parseLong(cnf.getConfigValue());

        if (response.getCount() > sysCount) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
            msg = String.format(msg, sysCount);
            response.setErrorMessage(msg);
        }
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    
    
    /**
     * 生産実績テーブル条件検索(生産実績明細行取得)
     *
     * @param componentCode
     * @param productionDateFrom
     * @param productionDateTo
     * @param directionCode
     * @param userId
     * @param userName
     * @param department
     * @param moldId
     * @param moldType
     * @param machineId
     * @param machineType
     * @param workStartDateTime
     * @param nextDayWorkStartDateTime
     * @return
     */
    @GET
    @Path("prodsearch")
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionDetailList getProductionsByCond(
            @QueryParam("componentCode") String componentCode // 部品コード
            , @QueryParam("productionDateFrom") String productionDateFrom // 生産日From yyyy/MM/dd
            , @QueryParam("productionDateTo") String productionDateTo // 生産日To yyyy/MM/dd
            , @QueryParam("directionCode") String directionCode // 手配番号
            , @QueryParam("userId") String userId // 生産者ID
            , @QueryParam("userName") String userName // 生産者氏名
            , @QueryParam("department") Integer department // 生産場所
            , @QueryParam("moldId") String moldId // 金型ID
            , @QueryParam("moldType") Integer moldType //  金型種類
            , @QueryParam("machineId") String machineId // 設備ID
            , @QueryParam("machineType") Integer machineType 
            , @QueryParam("workStartDateTime") String workStartDateTime // 選択日付の業務開始時刻
            , @QueryParam("nextDayWorkStartDateTime") String nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
    ) {

        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        // クエリパラムログ出力
        logger.log(Level.FINE, "QueryParams'{'"
                + ", componentCode:{0}, productionDateFrom:{1}, productionDateTo:{2}, directionCode:{3}, userId:{4}, userName:{5}, department:{6}, moldId:{7}, moldType:{8}, machineId:{9}, machineType:{10}}", new Object[]{componentCode, productionDateFrom, productionDateTo, directionCode, userId, userName, department, moldId, moldType, machineId, machineType}
        );
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        // 日付項目をDate型(yyyy/MM/dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        java.util.Date formatProductionDateFrom = null;
        java.util.Date formatProductionDateTo = null;

        SimpleDateFormat sdf1 = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        java.util.Date formatWorkStartDateTime = null;
        java.util.Date formatNextDayWorkStartDateTime = null;
        try {
            if (productionDateFrom != null && !"".equals(productionDateFrom)) {
                formatProductionDateFrom = sdf.parse(productionDateFrom);
            }
            if (productionDateTo != null && !"".equals(productionDateTo)) {
                formatProductionDateTo = sdf.parse(productionDateTo);
            }
            if (workStartDateTime != null && !"".equals(workStartDateTime)) {
                formatWorkStartDateTime = sdf1.parse(workStartDateTime);
            }
            if (nextDayWorkStartDateTime != null && !"".equals(nextDayWorkStartDateTime)) {
                formatNextDayWorkStartDateTime = sdf1.parse(nextDayWorkStartDateTime);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblProductionResource.class.getName()).log(Level.WARNING, null, "日付形式不正 productioDateFrom[" + productionDateFrom + "], productioDateTo[" + productionDateTo + "]");
        }
        // データ取得
        TblProductionDetailList tblProductionDetailList = tblProductionDetailService.getProductionDetailsByCond(
                componentCode, formatProductionDateFrom, formatProductionDateTo, directionCode, userId, userName, department, moldId, moldType, machineId, machineType, formatWorkStartDateTime, formatNextDayWorkStartDateTime
        );

        /**
         * 部署名のために部署リストを選択肢マスタより取得しておく
         */
        MstChoiceList departments = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");

        /**
         * 作業内容名称取得のために選択肢マスタより取得しておく
         */
        MstChoiceList workCategories = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_work.work_category");

        /*
         * 各種IDをコード値に変換して設定
         */
        ArrayList<TblProductionDetail> codeBindedTblProductionDetails = new ArrayList<>(); // 結果再格納用リスト
        if (!tblProductionDetailList.getTblProductionDetails().isEmpty()) {
            for (TblProductionDetail tblProductionDetail : tblProductionDetailList.getTblProductionDetails()) {
                if (tblProductionDetail.getTblProduction() != null) {
                    setOuterCodesForHeader(tblProductionDetail.getTblProduction(), loginUser, departments, workCategories);
                }
                setOuterCodesForDetails(tblProductionDetail, loginUser);
                codeBindedTblProductionDetails.add(tblProductionDetail);
            }
        }
        TblProductionDetailList response = new TblProductionDetailList();
        response.setTblProductionDetails(codeBindedTblProductionDetails);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    

    /**
     * 生産実績テーブルCSV出力
     *
     * @param componentCode
     * @param productionDateFrom
     * @param productionDateTo
     * @param directionCode
     * @param userId
     * @param userName
     * @param department
     * @param moldId
     * @param moldType
     * @param machineId
     * @param workStartDateTime
     * @param nextDayWorkStartDateTime
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getProductionsCSV(
            @QueryParam("componentCode") String componentCode // 部品コード
            , @QueryParam("productionDateFrom") String productionDateFrom // 生産日From yyyy/MM/dd
            , @QueryParam("productionDateTo") String productionDateTo // 生産日To yyyy/MM/dd
            , @QueryParam("directionCode") String directionCode // 手配番号
            , @QueryParam("userId") String userId // 生産者ID
            , @QueryParam("userName") String userName // 生産者氏名
            , @QueryParam("department") Integer department // 生産場所
            , @QueryParam("moldId") String moldId // 金型ID
            , @QueryParam("moldType") Integer moldType //  金型種類
            , @QueryParam("machineId") String machineId // 設備ID
            , @QueryParam("machineType") Integer machineType
            , @QueryParam("workStartDateTime") String workStartDateTime // 選択日付の業務開始時刻
            , @QueryParam("nextDayWorkStartDateTime") String nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
    ) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        // データ取得
        TblProductionDetailList tblProductionDetailList = getProductionsByCond(
                componentCode, productionDateFrom, productionDateTo, directionCode, userId, userName, department, moldId, moldType, machineId, machineType, workStartDateTime, nextDayWorkStartDateTime
        );

        /**
         * データが取得できなかった場合はエラー返却
         */
        FileReponse response = new FileReponse();
        /*
        if (tblProductionDetailList.getTblProductionDetails() == null || tblProductionDetailList.getTblProductionDetails().isEmpty()) {
            setApplicationError(response, loginUser, "mst_error_record_not_found", "生産実績明細データCSV出力対象無し");
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }*/

 /*
         * 作成したCSV出力テーブルのIDを設定したファイルレスポンスを返却
         */
        response = tblProductionDetailService.getProductionsCSV(tblProductionDetailList, loginUser);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /*
     * 生産実績一覧取得(生産実績行取得)
     *
     *@param department
     *
     * @return
     */
    @GET
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionList getProductionHeaders(@QueryParam("department") String department) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        // データ取得
        TblProductionList tblProductionList = tblProductionService.getProductionHeaders(department);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return tblProductionList;
    }

    /*
     * 生産実績一覧取得(1行目の明細付き)
     *
     *@param department
     *
     * @return
     */
    @GET
    @Path("header")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionList getProductionHeadersWithFirstDetail(@QueryParam("department") String department) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        // データ取得
        TblProductionList tblProductionList = tblProductionService.getProductionHeadersWithFirstDetail(department);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return tblProductionList;
    }
    
    /**
     * 設備ログ照会用ロット番号により生産実績取得
     *
     * @param lotNumber
     * @return
     */
    @GET
    @Path("search/lotNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionDetail getProductionByLotNumber(@QueryParam("lotNumber") String lotNumber) {
        TblProductionDetailList tblProductionDetailList = tblProductionDetailService.getProductionByLotNumber(lotNumber);
        if (tblProductionDetailList.getTblProductionDetails() != null && !tblProductionDetailList.getTblProductionDetails().isEmpty()) {
            return tblProductionDetailList.getTblProductionDetails().get(0);
        }
        return null;
    }

    /**
     * 生産実績1件取得
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionVo getProductionById(@PathParam("id") String id) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        logger.log(Level.FINE, "PathParam'{'id:{0}'}'", id);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        TblProductionVo response = new TblProductionVo();

        // データ取得
        TblProductionList tblProductionList = tblProductionService.getProductionById(id);
        if (tblProductionList == null || tblProductionList.getTblProductions().isEmpty()) {
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            logger.log(Level.FINE, "  <--- [[{0}]] End data not exists", methodName);
            return response;
        }
        TblProduction tblProduction = tblProductionList.getTblProductions().get(0);

        /**
         * 部署名のために部署リストを選択肢マスタより取得しておく
         */
        MstChoiceList departments = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");

        /**
         * 作業内容名称取得のために選択肢マスタより取得しておく
         */
        MstChoiceList workCategories = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_work.work_category");

        // 名称など取得設定
        setOuterCodesForHeader(tblProduction, loginUser, departments, workCategories);
        // VOクラスに詰め替えを行う
        BeanCopyUtil.copyFields(tblProduction, response);
        if (tblProduction.getMstMachine() != null) {
            response.setMachineId(tblProduction.getMstMachine().getMachineId());
            response.setMachineName(tblProduction.getMstMachine().getMachineName());
            response.setMachineType(tblProduction.getMstMachine().getMachineType());
        }

        // 生産実績部分のコード値などを名称変換
        Collection<TblProductionDetail> tblProductionDetails = tblProduction.getTblProductionDetailCollection();
        ArrayList<TblProductionDetailVo> TblProductionDetailVos = new ArrayList<>();
        // 生産実績明細
        if (tblProductionDetails != null && !tblProductionDetails.isEmpty()) {
            for (TblProductionDetail tblProductionDetail : tblProductionDetails) {
                TblProductionDetailVo tblProductionDetailVo = new TblProductionDetailVo();
                // 名称など取得設定
                setOuterCodesForDetails(tblProductionDetail, loginUser);
                // VOクラスに詰め替えを行う
                BeanCopyUtil.copyFields(tblProductionDetail, tblProductionDetailVo);
                // 材料コード、名称、グレード、材質、所要数量分子、所要数量分母
                tblProductionService.getComponentMaterialInfo(tblProductionDetailVo, tblProductionDetail);
                TblProductionDetailVos.add(tblProductionDetailVo);

                // 生産実績ロット残高
                Collection<TblProductionLotBalance> tblProductionLotBalances = tblProductionDetail.getTblProductionLotBalanceCollection();
                ArrayList<TblProductionLotBalanceVo> TblProductionLotBalanceVos = new ArrayList<>();
                if (tblProductionLotBalances != null && !tblProductionLotBalances.isEmpty()) {
                    for (TblProductionLotBalance tblProductionLotBalance : tblProductionLotBalances) {
                        TblProductionLotBalanceVo tblProductionLotBalanceVo = new TblProductionLotBalanceVo();
                        // VOクラスに詰め替えを行う
                        BeanCopyUtil.copyFields(tblProductionLotBalance, tblProductionLotBalanceVo);
                        TblProductionLotBalanceVos.add(tblProductionLotBalanceVo);
                    }
                    tblProductionDetailVo.setTblProductionLotBalanceVos(TblProductionLotBalanceVos);
                }

                //設備成形条件
                Collection<TblProductionMachineProcCond> procConds = tblProductionDetail.getTblProductionMachineProcCond();
                tblProductionDetailVo.setTblProductionMachineProcConds((List) procConds);
                
                // 前部品工程番号取得
                MstProcedure prevMstProcedure = mstProcedureService.getPrevProcedureCode(tblProductionDetail.getComponentId(), tblProductionDetail.getProcedureCode());
                if (prevMstProcedure != null) {
                    // 部品ロット番号リスト
                    TblProductionList lotNumberList = tblProductionService.getProcuctionLotListByPrevProcedureId(tblProductionDetail.getComponentId(), prevMstProcedure.getId(), tblProduction.getProductionDate());
                    if (lotNumberList != null) {
                        tblProductionDetailVo.setComponentLotNumberList(lotNumberList.getProductions());
                    }
                }
            }
            response.setTblProductionDetailVos(TblProductionDetailVos);
        }
        logger.log(Level.FINE, "  <--- [[{0}]] End data not exists", methodName);
        return response;
    }
    
   /**
    * スマホ版生産登録、生産実績詳細画面
    * @param id
    * @return
    */
    @GET
    @Path("detail/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionVo getProduction(@PathParam("id") String id) {
    	TblProductionVo response = getProductionById(id);
        response.setStartDatetimeStr(new FileUtil().getDateTimeFormatForStr(response.getStartDatetime()));
        response.setEndDatetimeStr(new FileUtil().getDateTimeFormatForStr(response.getEndDatetime()));
        response.setProductionDateStr(new FileUtil().getDateTimeFormatForStr(response.getProductionDate()));
        
    	if(!StringUtils.isEmpty(response.getWorkPhaseId())) {
    		MstWorkPhase mstWorkPhase = mstWorkPhaseService.getMstWorkPhaseById(response.getWorkPhaseId());
        	response.setMstWorkPhase(mstWorkPhase);
    	}
    	return response;
    }
    
    /**
     * 生産開始時の設備利用チェック
     * 
     * @param machineId
     * @param machineUuid
     * @return 
     */
    @GET
    @Path("conflictcheck")
    @Produces(MediaType.APPLICATION_JSON)
    public String conflictCheck(@QueryParam("machineId") String machineId, @QueryParam("machineUuid") String machineUuid) {
//        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        Gson gson = new GsonBuilder().setDateFormat(DateFormat.DATETIME_FORMAT).create();
        Map<String, Object> response = new HashMap<>();
        boolean result = false;
        int count = 0;
        TblProduction production = tblProductionService.getMachineUsingProducing(machineId, machineUuid /**, loginUser.getUserUuid() **/);
        if (production != null) {
            count++;
            result = true;
            response.put("machineUseStartDatetime", production.getStartDatetime());
            response.put("personUuid", production.getPersonUuid());
            response.put("personId", production.getMstUser() == null ? "" : production.getMstUser().getUserId());
            response.put("personName", production.getMstUser() == null ? "" : production.getMstUser().getUserName());
        }
        response.put("hasConflict", result);
        response.put("count", count);
        return gson.toJson(response);
    }
    
    /**
     * 生産開始時の金型利用チェック
     * 
     * @param moldId
     * @param moldUuid
     * @return 
     */
    @GET
    @Path("mold/conflictcheck")
    @Produces(MediaType.APPLICATION_JSON)
    public String moldConflictCheck(@QueryParam("moldId") String moldId, @QueryParam("moldUuid") String moldUuid) {
//        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        Gson gson = new GsonBuilder().setDateFormat(DateFormat.DATETIME_FORMAT).create();
        Map<String, Object> response = new HashMap<>();
        boolean result = false;
        int count = 0;
        TblProduction production = tblProductionService.getMoldUsingProducing(moldId, moldUuid /**, loginUser.getUserUuid()**/);
        if (production != null) {
            count++;
            result = true;
            response.put("moldUseStartDatetime", production.getStartDatetime());
            response.put("personUuid", production.getPersonUuid());
            response.put("personId", production.getMstUser() == null ? "" : production.getMstUser().getUserId());
            response.put("personName", production.getMstUser() == null ? "" : production.getMstUser().getUserName());
        }
        response.put("hasConflict", result);
        response.put("count", count);
        return gson.toJson(response);
    }
    
    /**
     * 生産開始時の手配テーブルと異なる部品チェック
     * 
     * @param tblProductionVo
     * @return 
     */
    @POST
    @Path("productioncomponentcheck")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse componentByProductionCheck(TblProductionVo tblProductionVo) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        // 手配テーブルと異なる部品チェック行う
        response = chkComponentByProduction(tblProductionVo, loginUser, response);
        return response;
    }

    /**
     * 生産開始登録
     *
     * @param tblProductionVo
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public BasicResponse postProduction(TblProductionVo tblProductionVo) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        
        // 生産開始登録ボタンのメンテサイクルチェック行う
        response = chkProductionMainteCycle(tblProductionVo, loginUser, response, true);
        if (response.getErrorCode() != null && !"".equals(response.getErrorCode())) {
            return response;
        }
        /**
         * 基本的なチェック
         */
        // 生産実績明細がパラメータとして存在するか
        List<TblProductionDetailVo> tblProductionDetailVos = tblProductionVo.getTblProductionDetailVos();
        if (tblProductionDetailVos == null || tblProductionDetailVos.isEmpty()) {
            setApplicationError(response, loginUser, "msg_error_no_processing_record", "生産実績明細データ POSTパラメータ無し");
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }

        /**
         * ロット番号を採番 ※生産実績ロット残高の工程番号にも採番した数値と同じ値を設定する
         */
        String lotNumber;
        String componentLotNumber = tblProductionDetailVos.get(0).getlotNumber();
        if (StringUtils.isNotEmpty(componentLotNumber)) {
            lotNumber = componentLotNumber;
        } else {
            lotNumber = tblProductionLotBalanceService.makeNewLotNumber(tblProductionVo.getPrevLotNumber());
        }

        /*
         * 生産実績登録値設定および入力チェック
         */
        TblProduction tblProduction = tblProductionService.setCreateDataForPoductionStart(response, tblProductionVo, lotNumber, loginUser);
        if (response.isError()) {
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }

        List<TblProductionMachineProcCondHolder> tblProductionMachineProcCondHolders = new ArrayList<>();

        /*
         * 生産実績明細登録値設定および入力チェック
         */
        ArrayList<TblProductionDetail> tblProductionDetails = new ArrayList<>();
        for (TblProductionDetailVo tblProductionDetailVo : tblProductionDetailVos) {
            TblProductionDetail tblProductionDetail = tblProductionDetailService.setCreateDataForPoductionStart(
                    response, tblProduction, tblProductionDetailVo, tblProductionVo.getPrevLotNumber(), loginUser
            );
            //成形条件を保持していればリストに確保する
            if (tblProductionDetailVo.getTblProductionMachineProcConds() != null && tblProductionDetailVo.getTblProductionMachineProcConds().size() > 0) {
                TblProductionMachineProcCondHolder holder = new TblProductionMachineProcCondHolder();
                holder.setTblProductionDetail(tblProductionDetail);
                holder.setTblProductionMachineProcConds(tblProductionDetailVo.getTblProductionMachineProcConds());
                tblProductionMachineProcCondHolders.add(holder);
            }
            if (response.isError()) {
                logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                return response;
            }
            tblProductionDetails.add(tblProductionDetail);
        }

        /*
         * 生産実績ロット残高登録値設定および入力チェック
         */
        ArrayList<TblProductionLotBalance> tblProductionLotBalances = new ArrayList<>();
        for (TblProductionDetail tblProductionDetail : tblProductionDetails) {
            TblProductionLotBalance tblProductionLotBalance = tblProductionLotBalanceService.setCreateDataForPoductionStart(
                    response, tblProduction, tblProductionDetail, lotNumber, loginUser
            );
            if (response.isError()) {
                logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                return response;
            }
            tblProductionLotBalances.add(tblProductionLotBalance);
        }

        /**
         * 登録開始
         */
        // 生産実績登録
        tblProduction = tblProductionService.createTblProduction(tblProduction, loginUser);
        // 生産実績明細およびロット残高
        for (int i = 0; i < tblProductionDetails.size(); i++) {
            // 生産実績明細登録
            TblProductionDetail tblProductionDetail = tblProductionDetails.get(i);
            tblProductionDetail.setProductionId(tblProduction);
            tblProductionDetail = tblProductionDetailService.createTblProductionDetail(tblProductionDetail, loginUser);

            // 生産実績ロット残高登録
            TblProductionLotBalance tblProductionLotBalance = tblProductionLotBalances.get(i);
            tblProductionLotBalance.setProductionDetailId(tblProductionDetail);
            if (tblProductionLotBalance.isAdded()) {
                tblProductionLotBalance = tblProductionLotBalanceService.createTblProductionLotBalance(tblProductionLotBalance, loginUser);
            }
        }

        //成形条件を登録
        for (TblProductionMachineProcCondHolder holder : tblProductionMachineProcCondHolders) {
            TblProductionDetail detail = holder.getTblProductionDetail();
            for (TblProductionMachineProcCond procCond : holder.getTblProductionMachineProcConds()) {
                procCond.setCreateDate(new java.util.Date());
                procCond.setCreateUserUuid(loginUser.getUserUuid());
                procCond.setUpdateDate(procCond.getCreateDate());
                procCond.setUpdateUserUuid(procCond.getCreateUserUuid());
                procCond.setId(null);
                procCond.setProductionDetailId(detail.getId());
                tblProductionMachineProcCondService.createProductionMachineProcCond(procCond);
            }
        }

        //response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_added"));
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 生産終了更新登録
     * 生産中画面の生産終了から呼ばれる。旧機械日報の生産終了はサポートしない
     * 生産中画面の生産終了は設備Appを使わないときのみ有効化される
     * (システム設定でproduction_end_requires_machine_daily_report = 0のときのみ)
     * よって設備マスタに対する更新は行わない。設備UUIDは存在しない。
     *
     * @param tblProductionVo
     * @return
     */
    @POST
    @Path("end")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionList postProductionEnd(TblProductionVo tblProductionVo) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblProductionList response = new TblProductionList();

        /**
         * 基本的なチェック
         */
        // 生産実績明細がパラメータとして存在するか
        List<TblProductionDetailVo> tblProductionDetailVos = tblProductionVo.getTblProductionDetailVos();
        if (tblProductionDetailVos == null || tblProductionDetailVos.isEmpty()) {
            setApplicationError(response, loginUser, "msg_error_no_processing_record", "生産実績明細データ POSTパラメータ無し");
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }
        
        /*
         * 生産実績更新値設定および入力チェック
         */
        // 更新対象のデータを取得
        TblProduction tblProduction = tblProductionService.getProductionSingleById(tblProductionVo.getId());
        if (tblProduction == null) {
            setApplicationError(response, loginUser, "msg_error_data_deleted", "TblProduction");
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }
        if (tblProduction.getStatus() == 9 && (null == tblProductionVo.getIsEdit() || tblProductionVo.getIsEdit() == 0)) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_production_end"));
            return response;
        }
        tblProductionVo.setNetProducintTimeMinutesBeforeUpd(FileUtil.getIntegerValue(tblProduction.getNetProducintTimeMinutes()));
        tblProductionVo.setShotCountBeforeUpd(tblProduction.getShotCount());
        tblProductionVo.setDisposedShotCountBeforeUpd(tblProduction.getDisposedShotCount());
        
        // 更新対象データ設定
        tblProduction = tblProductionService.setUpdateDataForPoductionEnd(response, tblProduction, tblProductionVo, null, loginUser);
        if (response.isError()) {
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }

        // 生産した部品が構成部品持つ場合、構成部品ロット選択ダイアログを表示する
        TblProductionList tblProductionList = tblProductionService.checkComponentStructure(tblProductionVo);
        if (tblProductionList != null && tblProductionList.getMstComponentStructureVoList() != null && !tblProductionList.getMstComponentStructureVoList().isEmpty()) {
            return tblProductionList;
         }

        List<TblProductionMachineProcCondHolder> tblProductionMachineProcCondHolders = new ArrayList<>();
        /*
         * 生産実績明細更新値設定および入力チェック
         */
        ArrayList<TblProductionDetail> tblProductionDetails = new ArrayList<>();
        for (TblProductionDetailVo tblProductionDetailVo : tblProductionDetailVos) {
            //生産実績明細IDでデータを特定
            TblProductionDetail tblProductionDetail = tblProductionDetailService.getProductionDetailSingleById(tblProductionDetailVo.getId());
            //削除の場合
            if (tblProductionDetailVo.isDeleted() && tblProductionDetail != null) {
                tblProductionDetailService.deleteTblProductionDetail(tblProductionDetail, loginUser);
            } else {
                // 更新or登録分岐
                // 新規の場合(生産実績のIDおよび部品コードで明細行が取得できなかった場合)
                if (tblProductionDetail == null) {
                    tblProductionDetail = tblProductionDetailService.setCreateDataPoductionEnd(
                            response, tblProduction, tblProductionVo, tblProductionDetailVo,
                            null, tblProduction.getPrevLotNumber(), loginUser
                    );
                    if (response.isError()) {
                        logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                        return response;
                    }
                } // 更新の場合
                else {
                    // 更新対象データ設定
                    tblProductionDetail = tblProductionDetailService.setUpdateDataForPoductionEnd(
                            response, tblProduction, tblProductionVo, tblProductionDetail, 
                            tblProductionDetailVo, null, loginUser
                    );
                    if (response.isError()) {
                        logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                        return response;
                    }
                }
                //成形条件を保持していればリストに確保する
                if (tblProductionDetailVo.getTblProductionMachineProcConds() != null && tblProductionDetailVo.getTblProductionMachineProcConds().size() > 0) {
                    TblProductionMachineProcCondHolder holder = new TblProductionMachineProcCondHolder();
                    holder.setTblProductionDetail(tblProductionDetail);
                    holder.setTblProductionMachineProcConds(tblProductionDetailVo.getTblProductionMachineProcConds());
                    tblProductionMachineProcCondHolders.add(holder);
                }
                tblProductionDetails.add(tblProductionDetail);
            }
            
            try {
                // 生産不具合テーブル更新
                BasicResponse basicResponse = tblProductionDefectService.postProductionDefects(tblProductionDetailVo.getTblProductionDefectList(), DateFormat.dateToStr(tblProduction.getEndDatetime(), DateFormat.DATETIME_FORMAT), loginUser.getUserUuid());
                if (basicResponse.isError()) {
                    logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                    response.setError(true);
                    response.setErrorCode(basicResponse.getErrorCode());
                    response.setErrorMessage(basicResponse.getErrorMessage());
                    return response;
                }
            } catch (Exception ex) {
                Logger.getLogger(TblProductionResource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /*
         * 生産実績ロット残高登録値設定および入力チェック
         */
        ArrayList<TblProductionLotBalance> tblProductionLotBalances = new ArrayList<>();
        for (TblProductionDetail tblProductionDetail : tblProductionDetails) {
            TblProductionLotBalance tblProductionLotBalance = new TblProductionLotBalance();
            // 明細行が登録対象の場合は新規作成
            if (tblProductionDetail.isAdded()) {
                // 登録対象データ設定
                tblProductionLotBalance = tblProductionLotBalanceService.setCreateDataForPoductionEnd(
                        response, tblProduction, tblProductionDetail, tblProductionLotBalance, loginUser
                );
                if (response.isError()) {
                    logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                    return response;
                }
                tblProductionLotBalances.add(tblProductionLotBalance);
            } // 明細行が更新対象の場合は既存データを取得
            else if (tblProductionDetail.isModified()) {
                // 生産実績明細のIDから更新対象のデータを取得
                tblProductionLotBalance = tblProductionLotBalanceService.getProductionBalanceSingleByDetailId(tblProductionDetail);

                /*
                 * 生産実績明細が更新対象だが残高が取得できない場合は
                 * 生産開始入力時に最終工程のため残高を作成しなかったレコード
                 * 空オブジェクトを設定し更新・登録処理を回避する(added = false && modified = false)
                 */
                if (tblProductionLotBalance == null) {
                    tblProductionLotBalance = new TblProductionLotBalance();
                } else {
                    // 更新対象データ設定
                    tblProductionLotBalance = tblProductionLotBalanceService.setUpdateDataForPoductionEnd(
                            response, tblProduction, tblProductionDetail, tblProductionLotBalance, loginUser
                    );
                    if (response.isError()) {
                        logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                        return response;
                    }
                }
                tblProductionLotBalances.add(tblProductionLotBalance);
            }
        }
        
        //期間別生産実績更新準備
        periodProductionUpdater.setLoginUser(loginUser);
        periodProductionUpdater.prepare();

        /**
         * 更新・登録開始
         */
        // 生産実績更新
        tblProduction.setStatus(9); // 生産完了に更新
        tblProduction = tblProductionService.updateTblProduction(tblProduction, loginUser);
        // 生産実績明細およびロット残高
        for (int i = 0; i < tblProductionDetails.size(); i++) {
            /*
             * 生産実績明細の処理
             */
            // 生産実績明細更新・登録
            TblProductionDetail tblProductionDetail = tblProductionDetails.get(i);
            tblProductionDetail.setProductionId(tblProduction);
            // 登録の場合
            if (tblProductionDetail.isAdded()) {
                tblProductionDetail = tblProductionDetailService.createTblProductionDetail(tblProductionDetail, loginUser);
                //金型期間別生産実績の更新
                if (tblProduction.getMoldUuid() != null) {
                    periodProductionUpdater.addMoldProductionPeriod(
                        tblProduction.getMoldUuid(), tblProductionDetail.getComponentId(), tblProduction.getProductionDate(),
                        tblProductionDetail.getCompleteCount());
                }
            } // 更新の場合
            else if (tblProductionDetail.isModified()) {
                tblProductionDetail = tblProductionDetailService.updateTblProductionDetail(tblProductionDetail, loginUser);
                if (tblProduction.getMoldUuid() != null) {
                    periodProductionUpdater.addMoldProductionPeriod(
                        tblProduction.getMoldUuid(), tblProductionDetail.getComponentId(), tblProduction.getProductionDate(), 
                        tblProductionDetail.getCompleteCount() - tblProductionDetail.getCompleteCountBeforeUpd());
                }
            }
            
            /*
             * 生産実績ロット残高の処理
             *   同ループ内の明細行の前ロット残高IDを判定
             *   ①前ロット残高IDが設定されてい無い場合は以下の値を設定
             *       1.残高        = 生産実績明細.完成数
             *       2.次工程完成数 = 0
             *       3.当初完成数    = 生産実績明細.完成数
             *   ②前ロット残高IDが設定されている場合は前残高を取得
             *     前ロット残高の更新値
             *       1.前残高.残高 = 残高 - 生産実績明細.完成数
             *       2.前残高.次工程完成数 = 当初完成数 - 更新後の残高
             *       3.当初完成数 更新無し
             *     当該ループ中の残高の更新値
             *       1.残高 = 前ロット残高.次工程完成数
             *       2.次工程完成数 = 0
             *       3.当初完成数 = 前ロット残高.次工程完成数
             */
            // 生産実績ロット残高登録
            TblProductionLotBalance tblProductionLotBalance = tblProductionLotBalances.get(i);
            tblProductionLotBalance.setProductionDetailId(tblProductionDetail);
            // 明細の前ロット残高IDで前残高を取得
            TblProductionLotBalance preTblProductionLotBalance = tblProductionLotBalanceService.getProductionBalanceSingleById(tblProductionDetail.getPrevLotBalanceId());

            // 前ロット残高無し
            if (preTblProductionLotBalance == null) {
                tblProductionLotBalance.setBalance(tblProductionDetail.getCompleteCount()); // 残高に完成数を設定
                tblProductionLotBalance.setNextCompleteCount(0); // 次工程完成数に0を設定
                tblProductionLotBalance.setFirstCompleteCount(tblProductionDetail.getCompleteCount()); // 当初完成数に完成数を設定
            } // 前ロット残高有り
            else {
                // 前ロット残高の設定
                // 残高が-1の考慮
                if (preTblProductionLotBalance.getBalance() < 0) {
                    preTblProductionLotBalance.setBalance(0);
                }
                int preBalance = preTblProductionLotBalance.getBalance();   // 親ロットの残高
                int completeCount = tblProductionDetail.getCompleteCount(); // 今ロットでの完了数

                preTblProductionLotBalance.setBalance(preTblProductionLotBalance.getBalance() - tblProductionDetail.getCompleteCount()); // 残高に「残高 - 生産実績明細.完成数」を設定
                preTblProductionLotBalance.setNextCompleteCount(preTblProductionLotBalance.getFirstCompleteCount() - preTblProductionLotBalance.getBalance());// 次工程完成数に「当初完成数 - 更新後の残高」を設定

                // 当ループ中の残高の設定
                if (completeCount > preBalance) {
                    completeCount = preBalance;
                }
                tblProductionLotBalance.setBalance(completeCount); // 残高に前ロット残高.次工程完成数を設定
                tblProductionLotBalance.setNextCompleteCount(0); // 次工程完成数に0を設定
                tblProductionLotBalance.setFirstCompleteCount(completeCount); // 当初完成数に前ロット残高.次工程完成数を設定

                // 前ロット残高を更新
                preTblProductionLotBalance = tblProductionLotBalanceService.updateTblProductionLotBalance(preTblProductionLotBalance, loginUser);
            }

            // 当ループ中の残高
            // 登録の場合
            if (tblProductionLotBalance.isAdded()) {
                tblProductionLotBalance = tblProductionLotBalanceService.createTblProductionLotBalance(tblProductionLotBalance, loginUser);
            } // 更新の場合
            else if (tblProductionLotBalance.isModified()) {
                tblProductionLotBalance = tblProductionLotBalanceService.updateTblProductionLotBalance(tblProductionLotBalance, loginUser);
            } // 上記以外は最終工程の制御により残高が作成されなかった明細行
            else {
            }
        }
        //金型期間別生産数テーブル更新
        periodProductionUpdater.updateDB();

        //成形条件を登録
        for (TblProductionMachineProcCondHolder holder : tblProductionMachineProcCondHolders) {
            TblProductionDetail detail = holder.getTblProductionDetail();
            //生産実績明細ID単位ですべて削除してからINSERT
            tblProductionMachineProcCondService.deleteProductionMachineProcCondByDetailId(detail.getId());
            for (TblProductionMachineProcCond procCond : holder.getTblProductionMachineProcConds()) {
                procCond.setCreateDate(new java.util.Date());
                procCond.setCreateUserUuid(loginUser.getUserUuid());
                procCond.setUpdateDate(procCond.getCreateDate());
                procCond.setUpdateUserUuid(procCond.getCreateUserUuid());
                procCond.setProductionDetailId(detail.getId());
                if (procCond.getId() != null && procCond.getId().trim().equals("")) {
                    //新規データの場合
                    procCond.setId(null);
                }
                tblProductionMachineProcCondService.createProductionMachineProcCond(procCond);
            }
        }

        // 在庫管理更新 2017/11 Add S
        if (StringUtils.isEmpty(tblProductionVo.getMachineUuid()) || (!tblProductionVo.getProductionEndFlg())) {
            for (TblProductionDetail tblProductionDetail : tblProductionDetails) {
                // 当該工程番号の前工程番号を取得する
                MstProcedure prevMstProcedure = mstProcedureService.getPrevProcedureCode(tblProductionDetail.getComponentId(), tblProductionDetail.getMstProcedure().getProcedureCode());
                if (tblProductionDetail.getMstProcedure() != null) {
                    // 部品ロット関連テーブルから構成部品リスト取得
                    TblComponentLotRelationVoList tblComponentLotRelationVoList = tblComponentLotRelationService.getTblComponentLotRelationList(tblProductionDetail.getId(), null);
                    tblProductionDetail.setTblComponentLotRelationVoList(tblComponentLotRelationVoList);
                    BasicResponse basicResponse = tblStockService.doTblStock(
                        tblProductionDetail.getMstComponent().getComponentCode(),
                        tblProductionDetail.getMstProcedure(),
                        prevMstProcedure,
                        CommonConstants.STORE,
                        tblProductionDetail.getCompleteCount() - tblProductionDetail.getCompleteCountBeforeUpd(),
                        DateFormat.getCurrentDateTime(),
                        tblProduction.getLotNumber(),
                        0,
                        null,
                        CommonConstants.SHIPMENT_NO,
                        tblProductionDetail.getTblComponentLotRelationVoList(),
                        loginUser.getUserUuid(),
                        loginUser.getLangId()
                    );

                    if (basicResponse.isError()) {
                        logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                        return response;
                    }
                }
                
                // 生産終了時関連テーブルに登録処理外す、生産実績明細IDにより部品ロット関連テーブルに持てばよいです。機械日報時登録された。
//                // 部品ロット関連テーブルを登録する
//                if (tblProductionDetail.getMstProcedure() != null && tblProductionDetail.getTblComponentLotRelationVoList() != null && tblProductionDetail.getTblComponentLotRelationVoList().getTblComponentLotRelationVos() != null && !tblProductionDetail.getTblComponentLotRelationVoList().getTblComponentLotRelationVos().isEmpty()) {
//                    TblComponentLot tblComponentLot = tblComponentLotService.getSingleResultTblComponentLot(tblProductionDetail.getMstComponent().getComponentCode(), tblProductionDetail.getMstProcedure().getProcedureCode(), tblProduction.getLotNumber());
//                    if (tblComponentLot != null) {
//                        tblProductionService.updateComponentLotRelation(tblProductionDetail, tblComponentLot, null, loginUser.getUserUuid());
//                    }
//                }
                
                // 材料在庫更新 2017/12 Add S
                if (StringUtils.isNotEmpty(tblProductionDetail.getMaterial01Id())) {
                    BasicResponse materialResponse = tblMaterialStockService.doMaterialStock(tblProductionDetail.getMaterial01Code(),
                        tblProductionDetail.getMaterial01LotNo(),
                        CommonConstants.DELIVERY,
                        tblProductionDetail.getMaterial01Amount().add(tblProductionDetail.getMaterial01PurgedAmount()),
                        DateFormat.getCurrentDateTime(),
                        tblProductionDetail.getId(),
                        0,
                        null,
                        loginUser.getUserUuid(),
                        loginUser.getLangId(),
                        false);
                    
                    if (materialResponse.isError()) {
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(materialResponse.getErrorMessage());

                        return response;
                    }
                }
                if (StringUtils.isNotEmpty(tblProductionDetail.getMaterial02Id())) {
                    BasicResponse materialResponse = tblMaterialStockService.doMaterialStock(tblProductionDetail.getMaterial02Code(),
                        tblProductionDetail.getMaterial02LotNo(),
                        CommonConstants.DELIVERY,
                        tblProductionDetail.getMaterial02Amount().add(tblProductionDetail.getMaterial02PurgedAmount()),
                        DateFormat.getCurrentDateTime(),
                        tblProductionDetail.getId(),
                        0,
                        null,
                        loginUser.getUserUuid(),
                        loginUser.getLangId(),
                        false);
                    
                    if (materialResponse.isError()) {
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(materialResponse.getErrorMessage());

                        return response;
                    }
                }
                if (StringUtils.isNotEmpty(tblProductionDetail.getMaterial03Id())) {
                    BasicResponse materialResponse = tblMaterialStockService.doMaterialStock(tblProductionDetail.getMaterial03Code(),
                        tblProductionDetail.getMaterial03LotNo(),
                        CommonConstants.DELIVERY,
                        tblProductionDetail.getMaterial03Amount().add(tblProductionDetail.getMaterial03PurgedAmount()),
                        DateFormat.getCurrentDateTime(),
                        tblProductionDetail.getId(),
                        0,
                        null,
                        loginUser.getUserUuid(),
                        loginUser.getLangId(),
                        false);
                    
                    if (materialResponse.isError()) {
                        response.setError(true);
                        response.setErrorCode(ErrorMessages.E201_APPLICATION);
                        response.setErrorMessage(materialResponse.getErrorMessage());

                        return response;
                    }
                }
                // 材料在庫更新 2017/12 Add E
            }
        }
        // 在庫管理更新 2017/11 Add E
        
        // 生産実績実稼働時間と中断時間更新
        // 生産実績の中断時間計算する
        TblProductionSuspensionList tblProductionSuspensionList = tblProductionSuspensionlService.getProductionSuspensionListByProductionId(tblProductionVo.getId(), loginUser);
        Integer totalSuspendedTimeMinutes = 0;
        // 該当生産実績の全て中断履歴取得
        List<TblProductionSuspensionVo> tblProductionSuspensionVos = tblProductionSuspensionList.getTblProductionSuspensionVo();
        // 中断あるの場合、すべての中断時間を加算する
        if (tblProductionSuspensionVos != null && !tblProductionSuspensionVos.isEmpty()) {
            for (TblProductionSuspensionVo vo : tblProductionSuspensionVos) {
                totalSuspendedTimeMinutes += Integer.valueOf(vo.getSuspendedTimeMinutes());
            }
            tblProduction.setSuspendedTimeMinutes(totalSuspendedTimeMinutes);
        } else {
            tblProduction.setSuspendedTimeMinutes(0);
        }
        tblProduction.setNetProducintTimeMinutes(FileUtil.getIntegerValue(tblProduction.getProducingTimeMinutes()) - tblProduction.getSuspendedTimeMinutes());
        tblProductionService.updateTblProduction(tblProduction, loginUser);
        
        //最終生産日、累計生産時間、累計ショット数更新
        if (tblProduction.getMoldUuid() != null) {
            totalUpdater.addMoldCounts(tblProduction.getMoldUuid(), 
                tblProduction.getShotCount() + tblProduction.getDisposedShotCount(),
                tblProduction.getNetProducintTimeMinutes());
        }
        if (tblProduction.getMachineUuid() != null) {
            totalUpdater.addMachineCounts(tblProduction.getMachineUuid(),
                tblProduction.getShotCount() + tblProduction.getDisposedShotCount(),
                tblProduction.getNetProducintTimeMinutes());
        }
        totalUpdater.update(DateFormat.strToDate(DateFormat.dateToStr(tblProduction.getEndDatetime(), DateFormat.DATE_FORMAT)));

        // 生産終了ボタンのメンテサイクルチェック行う
        tblProductionVo.setIsStart(false);
        BasicResponse basicResponse = chkProductionMainteCycle(tblProductionVo, loginUser, response, false);
        if (basicResponse.getErrorCode() != null && !"".equals(basicResponse.getErrorCode())) {
            return response;
        }

        //response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_added"));
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    
    /**
     * 生産終了取消更新登録
     *
     * @param tblProductionVo
     * @return
     */
    @POST
    @Path("end/cancel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postProductionEndCancel(TblProductionVo tblProductionVo) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();

        /*
         * 生産実績更新値設定および入力チェック
         */
        // 更新対象のデータを取得
        TblProduction tblProduction = tblProductionService.getProductionSingleById(tblProductionVo.getId());
        if (tblProduction == null) {
            setApplicationError(response, loginUser, "msg_error_data_deleted", "TblProduction");
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }

        TblProduction tblProductionCopy = new TblProduction();
        BeanCopyUtil.copyFields(tblProduction, tblProductionCopy);
        // 更新対象データ設定
        tblProduction.setStatus(0);//生産中に更新
        tblProduction.setEndDatetime(null);
        tblProduction.setEndDatetimeStz(null);
        tblProductionService.updateTblProduction(tblProduction, loginUser);
        
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 作業実績テーブル1件削除(ID指定)
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public BasicResponse deleteProduction(@PathParam("id") String id) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        // 削除データチェック
        tblProductionService.checkDeleteData(response, id, loginUser);
        if (response.isError()) {
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }
        
        // 削除する生産実績の設備がある場合
        // 生産実績テーブル自体の存在チェック
        TblMachineDailyReportVo tblMachineDailyReportVo = null;
        TblProduction deleteProduction = tblProductionService.getProductionSingleById(id);
        if (deleteProduction != null && deleteProduction.getMachineUuid() != null) {
            // 機械日報情報取得
            tblMachineDailyReportVo = tblMachineDailyReportService.getDailyReports(id, loginUser);
            if (tblMachineDailyReportVo.getMachineDailyReportVos() != null && !tblMachineDailyReportVo.getMachineDailyReportVos().isEmpty()) {
                for (TblMachineDailyReportVo machineDailyReportVo : tblMachineDailyReportVo.getMachineDailyReportVos()) {
                    tblMachineDailyReportService.calculateProductionPeriodForDelete(machineDailyReportVo.getId(), loginUser);
                }
            }
        }
        
        // 在庫管理更新 2017/11 Add S
        // 機械日報がある場合、機械日報詳細により、在庫管理テーブルと履歴テーブルを更新/登録する
        // 機械日報がない場合、生産実績詳細により、在庫管理テーブルと履歴テーブルを更新/登録する
        if (tblMachineDailyReportVo != null && tblMachineDailyReportVo.getMachineDailyReportVos() != null && !tblMachineDailyReportVo.getMachineDailyReportVos().isEmpty()) {
            for (TblMachineDailyReportVo machineDailyReportVo : tblMachineDailyReportVo.getMachineDailyReportVos()) {
                List<TblMachineDailyReportDetail> deleteDailyReportDetailList = tblMachineDailyReportService.getDailyReportDetailsByMacReportId(machineDailyReportVo.getId());
                String lotNumber = deleteProduction == null ? "" : deleteProduction.getLotNumber();
                
                // 部品ロット関連テーブルから構成部品リスト取得
                TblComponentLotRelationVoList tblComponentLotRelationVoList = tblComponentLotRelationService.getTblComponentLotRelationList(null, machineDailyReportVo.getId());
                for (TblMachineDailyReportDetail deleteDetail : deleteDailyReportDetailList) {
                    if (deleteDetail.getMstProcedure() == null) continue;
                    MstProcedure prevMstProcedure = mstProcedureService.getPrevProcedureCode(deleteDetail.getComponentId(), deleteDetail.getMstProcedure().getProcedureCode());
                    // 休日以外
                    if (!"0".equals(machineDailyReportVo.getNoRegistrationFlag())) {
                        
                        response = tblStockService.doTblStock(
                            deleteDetail.getMstComponent().getComponentCode(),
                            deleteDetail.getMstProcedure(),
                            prevMstProcedure,
                            CommonConstants.DELIVERY_DISCARD,
                            deleteDetail.getCompleteCount(),
                            DateFormat.getCurrentDateTime(),
                            lotNumber,
                            0,
                            null,
                            CommonConstants.SHIPMENT_NO,
                            tblComponentLotRelationVoList,
                            loginUser.getUserUuid(),
                            loginUser.getLangId()
                        );

                        if (response.isError()) {
                            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                            return response;
                        }
                    }

                    // 材料在庫更新 2017/12 Add S
                    if (!"0".equals(machineDailyReportVo.getNoRegistrationFlag()) && StringUtils.isNotEmpty(deleteDetail.getMaterial01Id())) {
                        response = tblMaterialStockService.doMaterialStock(deleteDetail.getMstMaterial01() == null? "" : deleteDetail.getMstMaterial01().getMaterialCode(),
                            deleteDetail.getMaterial01LotNo(),
                            CommonConstants.STORE_DISCARD,
                            deleteDetail.getMaterial01Amount().add(deleteDetail.getMaterial01PurgedAmount()),
                            DateFormat.getCurrentDateTime(),
                            deleteDetail.getTblMachineDailyReportDetailPK().getProductionDetailId(),
                            0,
                            null,
                            loginUser.getUserUuid(),
                            loginUser.getLangId(),
                            false);

                        if (response.isError()) {
                            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                            return response;
                        }
                    }
                    if (!"0".equals(machineDailyReportVo.getNoRegistrationFlag()) && StringUtils.isNotEmpty(deleteDetail.getMaterial02Id())) {
                        response = tblMaterialStockService.doMaterialStock(deleteDetail.getMstMaterial02() == null ? "" : deleteDetail.getMstMaterial02().getMaterialCode(),
                            deleteDetail.getMaterial02LotNo(),
                            CommonConstants.STORE_DISCARD,
                            deleteDetail.getMaterial02Amount().add(deleteDetail.getMaterial02PurgedAmount()),
                            DateFormat.getCurrentDateTime(),
                            deleteDetail.getTblMachineDailyReportDetailPK().getProductionDetailId(),
                            0,
                            null,
                            loginUser.getUserUuid(),
                            loginUser.getLangId(),
                            false);

                        if (response.isError()) {
                            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                            return response;
                        }
                    }
                    if (!"0".equals(machineDailyReportVo.getNoRegistrationFlag()) && StringUtils.isNotEmpty(deleteDetail.getMaterial03Id())) {
                        response = tblMaterialStockService.doMaterialStock(deleteDetail.getMstMaterial03() == null ? "" : deleteDetail.getMstMaterial03().getMaterialCode(),
                            deleteDetail.getMaterial03LotNo(),
                            CommonConstants.STORE_DISCARD,
                            deleteDetail.getMaterial03Amount().add(deleteDetail.getMaterial03PurgedAmount()),
                            DateFormat.getCurrentDateTime(),
                            deleteDetail.getTblMachineDailyReportDetailPK().getProductionDetailId(),
                            0,
                            null,
                            loginUser.getUserUuid(),
                            loginUser.getLangId(),
                            false);

                        if (response.isError()) {
                            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                            return response;
                        }
                    }
                    // 材料在庫更新 2017/12 Add E
                }
            }
        } else {
            if (deleteProduction != null && deleteProduction.getTblProductionDetailCollection() != null && !deleteProduction.getTblProductionDetailCollection().isEmpty()) {
                for (TblProductionDetail deleteProductionDetail : deleteProduction.getTblProductionDetailCollection()) {
                    if (deleteProductionDetail.getMstProcedure() == null) continue; //kitaoji added for irregular data which has deleted mst_procedure
                    MstProcedure prevMstProcedure = mstProcedureService.getPrevProcedureCode(deleteProductionDetail.getComponentId(), deleteProductionDetail.getMstProcedure().getProcedureCode());
                    
                    // 部品ロット関連テーブルから構成部品リスト取得
                    TblComponentLotRelationVoList tblComponentLotRelationVoList = tblComponentLotRelationService.getTblComponentLotRelationList(deleteProductionDetail.getId(), null);

                    response = tblStockService.doTblStock(
                    deleteProductionDetail.getMstComponent().getComponentCode(),
                    deleteProductionDetail.getMstProcedure(),
                    prevMstProcedure,
                    CommonConstants.DELIVERY_DISCARD,
                    deleteProductionDetail.getCompleteCount(),
                    DateFormat.getCurrentDateTime(),
                    deleteProduction.getLotNumber(),
                    0,
                    null,
                    CommonConstants.SHIPMENT_NO,
                    tblComponentLotRelationVoList,
                    loginUser.getUserUuid(),
                    loginUser.getLangId());

                    if (response.isError()) {
                        logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                        return response;
                    }
                    
                    
                    // 材料在庫更新 2017/12 Add S
                    if (StringUtils.isNotEmpty(deleteProductionDetail.getMaterial01Id())) {
                        response = tblMaterialStockService.doMaterialStock(deleteProductionDetail.getMstMaterial01() == null? "" : deleteProductionDetail.getMstMaterial01().getMaterialCode(),
                            deleteProductionDetail.getMaterial01LotNo(),
                            CommonConstants.STORE_DISCARD,
                            deleteProductionDetail.getMaterial01Amount().add(deleteProductionDetail.getMaterial01PurgedAmount()),
                            DateFormat.getCurrentDateTime(),
                            deleteProductionDetail.getId(),
                            0,
                            null,
                            loginUser.getUserUuid(),
                            loginUser.getLangId(),
                            false);

                        if (response.isError()) {
                            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                            return response;
                        }
                    }
                    if (StringUtils.isNotEmpty(deleteProductionDetail.getMaterial02Id())) {
                        response = tblMaterialStockService.doMaterialStock(deleteProductionDetail.getMstMaterial02() == null ? "" : deleteProductionDetail.getMstMaterial02().getMaterialCode(),
                            deleteProductionDetail.getMaterial02LotNo(),
                            CommonConstants.STORE_DISCARD,
                            deleteProductionDetail.getMaterial02Amount().add(deleteProductionDetail.getMaterial02PurgedAmount()),
                            DateFormat.getCurrentDateTime(),
                            deleteProductionDetail.getId(),
                            0,
                            null,
                            loginUser.getUserUuid(),
                            loginUser.getLangId(),
                            false);

                        if (response.isError()) {
                            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                            return response;
                        }
                    }
                    if (StringUtils.isNotEmpty(deleteProductionDetail.getMaterial03Id())) {
                        response = tblMaterialStockService.doMaterialStock(deleteProductionDetail.getMstMaterial03() == null ? "" : deleteProductionDetail.getMstMaterial03().getMaterialCode(),
                            deleteProductionDetail.getMaterial03LotNo(),
                            CommonConstants.STORE_DISCARD,
                            deleteProductionDetail.getMaterial03Amount().add(deleteProductionDetail.getMaterial03PurgedAmount()),
                            DateFormat.getCurrentDateTime(),
                            deleteProductionDetail.getId(),
                            0,
                            null,
                            loginUser.getUserUuid(),
                            loginUser.getLangId(),
                            false);

                        if (response.isError()) {
                            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                            return response;
                        }
                    }
                    // 材料在庫更新 2017/12 Add E
                }
            }
        }
        // 在庫管理更新 2017/11 Add E
        
        //機械日報2を使っていない場合のために期間別生産実績テーブル更新準備
        periodProductionUpdater.setLoginUser(loginUser);
        periodProductionUpdater.prepare();
        if (deleteProduction != null && deleteProduction.getTblProductionDetailCollection() != null && !deleteProduction.getTblProductionDetailCollection().isEmpty() && deleteProduction.getMoldUuid() != null) {
            for (TblProductionDetail tblProductionDetail: deleteProduction.getTblProductionDetailCollection()) {
                periodProductionUpdater.addMoldProductionPeriod(
                    deleteProduction.getMoldUuid(), tblProductionDetail.getComponentId(), deleteProduction.getProductionDate(),
                    tblProductionDetail.getCompleteCount() * (-1));
            }
        }
        
        // 削除
        tblProductionService.deleteTblProduction(id, loginUser);
        //機械日報2から削除
        boolean dailyReport2Deleted = machineDailyReport2Service.deleteProduction(id, loginUser);
        
        // 最終生産日、累計生産時間、累計ショット数、メンテナンス後生産時間、メンテナンス後ショット数更新
        if (!dailyReport2Deleted) { //機械日報2を使っているときは機械日報2で累計も更新されているのでここでは行わない
            //tblProductionService.updateMoldOrMachineForProductionDel(response, deleteProduction, tblMachineDailyReportVo, loginUser);
            if (deleteProduction.getMoldUuid() != null) {
                totalUpdater.addMoldCounts(
                    deleteProduction.getMoldUuid(), 
                    (deleteProduction.getShotCount() + deleteProduction.getDisposedShotCount()) * (-1), 
                    deleteProduction.getNetProducintTimeMinutes() == null ? 0 : deleteProduction.getNetProducintTimeMinutes() * (-1));
            }
            if (deleteProduction.getMachineUuid() != null) {
                totalUpdater.addMachineCounts(
                    deleteProduction.getMachineUuid(), 
                    (deleteProduction.getShotCount() + deleteProduction.getDisposedShotCount()) * (-1), 
                    deleteProduction.getNetProducintTimeMinutes() == null ? 0 : deleteProduction.getNetProducintTimeMinutes() * (-1));
            }
            totalUpdater.update(null);
            //機械日報2を使っていないときは期間別生産実績テーブルから生産数を控除
            periodProductionUpdater.updateDB();
        }

        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 生産実績の各IDからコード値や名称を設定
     *
     * @param tblProductionDetail
     * @param loginUser
     */
    private void setOuterCodesForHeader(TblProduction tblProduction, LoginUser loginUser, MstChoiceList departments, MstChoiceList workCategories) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        // ユーザーマスタ
        if (tblProduction.getMstUser() != null) {
            tblProduction.setUserId(tblProduction.getMstUser().getUserId());
            tblProduction.setUserName(tblProduction.getMstUser().getUserName());
            // 生産場所名設定
            if (tblProduction.getProdDepartment() != null) {
                if (departments != null && departments.getMstChoice() != null) {
                    tblProduction.setDepartment(tblProduction.getProdDepartment());
                    for (MstChoice department : departments.getMstChoice()) {
                        // 部署コード(SEQ)が一致する場合は名称設定
                        if (tblProduction.getProdDepartment() == Integer.parseInt(department.getMstChoicePK().getSeq())) {
                            tblProduction.setDepartmentName(department.getChoice());
                        }
                    }
                }
            }
        }

        // 手配テーブル
        if (tblProduction.getTblDirection() != null) {
            tblProduction.setDirectionCode(tblProduction.getTblDirection().getDirectionCode());
        }
        // 金型マスタ
        if (tblProduction.getMstMold() != null) {
            tblProduction.setMoldId(tblProduction.getMstMold().getMoldId());
            tblProduction.setMoldName(tblProduction.getMstMold().getMoldName());
            tblProduction.setMoldType(tblProduction.getMstMold().getMoldType());
        }
        // 選択肢マスタ.作業内容名称設定
        if (tblProduction.getProductionPhase() != null && tblProduction.getWorkCategory() != null) {
            if (workCategories != null && workCategories.getMstChoice() != null) {
                for (MstChoice workCategory : workCategories.getMstChoice()) {
                    if (workCategory.getParentSeq() != null && workCategory.getMstChoicePK().getSeq() != null) {
                        // 親SEQおよびSEQが一致する場合は名称設定
                        if (//tblProduction.getProductionPhase() == Integer.parseInt(workCategory.getParentSeq())
                                //&& 
                                tblProduction.getWorkCategory() == Integer.parseInt(workCategory.getMstChoicePK().getSeq())) {
                            tblProduction.setWorkCategoryName(workCategory.getChoice());
                        }
                    }
                }
            }
        }
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
    }

    /**
     * 生産実績明細の各IDからコード値や名称を設定
     *
     * @param tblProductionDetail
     * @param loginUser
     */
    private void setOuterCodesForDetails(TblProductionDetail tblProductionDetail, LoginUser loginUser) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        /*
         * SQL発行時点で取得できているマスタ項目を
         *   生産実績および明細へ設定
         */
        // 生産実績明細のマスタ項目設定
        if (tblProductionDetail.getMstComponent() != null) {
            tblProductionDetail.setComponentCode(tblProductionDetail.getMstComponent().getComponentCode());
            tblProductionDetail.setComponentName(tblProductionDetail.getMstComponent().getComponentName());
            tblProductionDetail.setComponentType(tblProductionDetail.getMstComponent().getComponentType());
        }
        // 工程マスタ(部品ごとの製造手順)
        if (tblProductionDetail.getMstProcedure() != null) {
            tblProductionDetail.setProcedureCode(tblProductionDetail.getMstProcedure().getProcedureCode());
            tblProductionDetail.setProcedureName(tblProductionDetail.getMstProcedure().getProcedureName());
        }
        // 材料マスタ01
        if (tblProductionDetail.getMstMaterial01() != null) {
            tblProductionDetail.setMaterial01Name(tblProductionDetail.getMstMaterial01().getMaterialName());
        }
        // 材料マスタ02
        if (tblProductionDetail.getMstMaterial02() != null) {
            tblProductionDetail.setMaterial02Name(tblProductionDetail.getMstMaterial02().getMaterialName());
        }
        // 材料マスタ03
        if (tblProductionDetail.getMstMaterial03() != null) {
            tblProductionDetail.setMaterial03Name(tblProductionDetail.getMstMaterial03().getMaterialName());
        }
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
    }

    private BasicResponse setApplicationError(BasicResponse response, LoginUser loginUser, String dictKey, String errorContentsName) {
        setBasicResponseError(
                response, true, ErrorMessages.E201_APPLICATION, mstDictionaryService.getDictionaryValue(loginUser.getLangId(), dictKey)
        );
        String logMessage = response.getErrorMessage() + ":" + errorContentsName;
        logger.log(Level.FINE, logMessage);
        return response;
    }

    private BasicResponse setBasicResponseError(BasicResponse response, boolean error, String errorCode, String errorMessage) {
        response.setError(error);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        return response;
    }

    /**
     * バッチで生産実績テーブルデータを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extproduction")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionList getExtProductionsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return tblProductionService.getExtProductionsByBatch(latestExecutedDate, moldUuid);
    }

    /**
     * バッチで設備生産実績テーブルデータを取得
     *
     * @param latestExecutedDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachineproduction")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionList getExtMachineProductionsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("machineUuid") String machineUuid) {
        return tblProductionService.getExtMachineProductionsByBatch(latestExecutedDate, machineUuid);
    }

    /**
     * 金型詳細でその金型の生産履歴取得
     *
     * @param moldId
     * @return
     */
    @GET
    @Path("history/{moldId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionList getProductionHistory(@PathParam("moldId") String moldId, @QueryParam("pageNumber") int pageNumber, @QueryParam("pageSize") int pageSize) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        logger.log(Level.FINE, "PathParam'{'moldId:{0}'}'", moldId);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        // データ取得
        TblProductionList tblProductionHistoryList = tblProductionService.getProductionHistoryByMoldId(moldId, loginUser, pageNumber, pageSize);
        logger.log(Level.FINE, "  <--- [[{0}]] End data not exists", methodName);
        return tblProductionHistoryList;
    }

    /**
     * 設備詳細でその設備の生産履歴取得
     *
     * @param machineId
     * @return
     */
    @GET
    @Path("history/machine/{machineId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionList getMachineProductionHistory(@PathParam("machineId") String machineId, @QueryParam("pageNumber") int pageNumber, @QueryParam("pageSize") int pageSize) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.INFO, "  ---> [[{0}]] Start", methodName);
        logger.log(Level.INFO, "PathParam'{'moldId:{0}'}'", machineId);

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        // データ取得
        TblProductionList tblProductionHistoryList = tblProductionService.getProductionHistoryByMachineId(machineId, loginUser, pageNumber, pageSize);
        logger.log(Level.INFO, "  <--- [[{0}]] End data not exists", methodName);
        return tblProductionHistoryList;
    }

    /**
     * 機械日報画面作業実績明細情報を取得
     *
     * @param init(初期化：0；検索：1)
     * @param productionDate
     * @param machineId
     * @param department
     * @return
     */
    @GET
    @Path("dailyreport")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionList getProductionDetailListByDailyReport(@QueryParam("init") String init, @QueryParam("productionDate") String productionDate, @QueryParam("machineId") String machineId, @QueryParam("department") String department) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        TblProductionList response = tblProductionService.getProductionDetailListByDailyReport(init, productionDate, machineId, department, loginUser);

        return response;
    }

    /**
     * 生産開始の手配テーブルと異なる部品チェック処理
     * @param tblProductionVo
     * @param loginUser
     * @param response
     * @return 
     */
    private BasicResponse chkComponentByProduction(TblProductionVo tblProductionVo, LoginUser loginUser, BasicResponse response) {
        List<TblDirection> tblDirectionList = new ArrayList<>();
        if (StringUtils.isEmpty(tblProductionVo.getDirectionCode())) {
            return response;
        }
        tblDirectionList = tblDirectionService.getTblDirectionsByDirectionCode(tblProductionVo.getDirectionCode());

        List<String> componentIdList = new ArrayList<>();
        for (TblDirection tblDirection : tblDirectionList) {
            if (StringUtils.isNotEmpty(tblDirection.getComponentId())) {
                componentIdList.add(tblDirection.getComponentId());
            }
        }

        List<TblProductionDetailVo> tblProductionDetailVos = tblProductionVo.getTblProductionDetailVos();
        List<String> componentCodeList = new ArrayList<>();
        if (null != tblProductionDetailVos) {
            for (TblProductionDetailVo tblProductionDetailVo : tblProductionDetailVos) {
                if (!componentIdList.contains(tblProductionDetailVo.getComponentId())) {
                    componentCodeList.add(tblProductionDetailVo.getComponentCode());
                }
            }
        }
        if (0 < componentCodeList.size()) {
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_not_belong_direction");
            setBasicResponseError(response, true, ErrorMessages.E201_APPLICATION, msg.replace("%PartCode%", LINE_END + String.join(COLUMN_SEPARATOR, componentCodeList)));
        }
            return response;
    }

    /**
     * 生産開始登録ボタンのメンテサイクルチェック処理
     * @param tblProductionVo
     * @param loginUser
     * @param response
     * @return 
     */
    private BasicResponse chkProductionMainteCycle(TblProductionVo tblProductionVo, LoginUser loginUser, BasicResponse response, boolean startEndFlg) {
        // Iterator4.2メンテサイクルチェック start
        // 「開始しますか」フラグ、Trueだったら、生産開始のロジックに行く
        if (!tblProductionVo.getIsStart()) {
            Map<String, String> msgMap = null;
            // 金型指定された場合、サイクルパターンチェック行う
            if (tblProductionVo.getMoldId() != null && !"".equals(tblProductionVo.getMoldId())) {
                List<MstMoldDetail> chkTargetList = new ArrayList<>();
                // もらった金型IDにより金型マスタオブジェクト取得
                MstMoldDetail mold = mstMoldService.getMoldByMoldId(tblProductionVo.getMoldId(), loginUser);
                if (mold.getErrorCode() != null && !"".equals(mold.getErrorCode())) {
                    return mold;
                }
                tblProductionService.setMoldChkParams(mold);
                chkTargetList.add(mold);
                // サイクルパターンチェック
                tblMaintenanceCyclePtnService.chkMoldMainte(chkTargetList);
                for (MstMoldDetail moldDetail : chkTargetList) {
                    if (moldDetail.getMsgFlg() == 1 || moldDetail.getMsgFlg() == 2) {
                        List<String> dictKeyList;
                        if (startEndFlg) {
                            dictKeyList = Arrays.asList("msg_confirm_maintenance_time", "msg_confirm_maintenance_time_expired", "mold", "machine");
                        } else {
                            dictKeyList = Arrays.asList("msg_maintenance_time", "msg_maintenance_time_expired", "mold", "machine");
                        }
                        msgMap = FileUtil.getDictionaryList(mstDictionaryService, loginUser.getLangId(), dictKeyList);
                        // アラート条件到達
                        String msg;
                        if (moldDetail.getMsgFlg() == 1) {
                            if (startEndFlg) {
                                msg = String.format(msgMap.get("msg_confirm_maintenance_time"), msgMap.get("mold"));
                            } else {
                                msg = String.format(msgMap.get("msg_maintenance_time"), msgMap.get("mold"));
                            }
                            setBasicResponseError(response, false, ErrorMessages.E201_APPLICATION, msg);
                            return response;
                        // メンテ条件到達
                        } else if (moldDetail.getMsgFlg() == 2) {
                            if (startEndFlg) {
                                msg = String.format(msgMap.get("msg_confirm_maintenance_time_expired"), msgMap.get("mold"));
                            } else {
                                msg = String.format(msgMap.get("msg_maintenance_time_expired"), msgMap.get("mold"));
                            }
                            setBasicResponseError(response, false, ErrorMessages.E201_APPLICATION, msg);
                            return response;
                        }
                    }
                }

                tblMaintenanceCyclePtnService.chkMoldPartMaintRecommend(chkTargetList);
                for (MstMoldDetail moldDetail : chkTargetList) {
                    // only check if start the production
                    if (moldDetail.getMsgFlg() == 3 && startEndFlg) {
                        List<String> dictKeyList = Arrays.asList("msg_maintenance_time_expired", "mold");
                        msgMap = FileUtil.getDictionaryList(mstDictionaryService, loginUser.getLangId(), dictKeyList);
                        String msg = String.format(msgMap.get("msg_maintenance_time_expired"), msgMap.get("mold"));
                        setBasicResponseError(response, false, ErrorMessages.E201_APPLICATION, msg);
                        return response; 
                    }
                }
            }
            // 設備指定された場合、サイクルパターンチェック行う
            if (tblProductionVo.getMachineId() != null && !"".equals(tblProductionVo.getMachineId())) {
                List<MstMachineVo> chkTargetList = new ArrayList<>();
                // もらった設備IDにより設備マスタオブジェクト取得
                MstMachineVo machine = mstMachineService.getMachineByMachineId(tblProductionVo.getMachineId(), loginUser);
                if (machine.getErrorCode() != null && !"".equals(machine.getErrorCode())) {
                    return machine;
                }
                tblProductionService.setMachineChkParams(machine);
                chkTargetList.add(machine);
                // サイクルパターンチェック
                tblMaintenanceCyclePtnService.chkMachineMainte(chkTargetList);
                for (MstMachineVo machineVo : chkTargetList) {
                    if (machineVo.getMsgFlg() == 1 || machineVo.getMsgFlg() == 2) {
                        if (msgMap == null) {
                            List<String> dictKeyList;
                            if (startEndFlg) {
                                dictKeyList = Arrays.asList("msg_confirm_maintenance_time", "msg_confirm_maintenance_time_expired", "mold", "machine");
                            } else {
                                dictKeyList = Arrays.asList("msg_maintenance_time", "msg_maintenance_time_expired", "mold", "machine");
                            }
                            msgMap = FileUtil.getDictionaryList(mstDictionaryService, loginUser.getLangId(), dictKeyList);
                        }
                        // アラート条件到達
                        if (machineVo.getMsgFlg() == 1) {
                            String msg;
                            if (startEndFlg) {
                                msg = String.format(msgMap.get("msg_confirm_maintenance_time"), msgMap.get("machine"));
                            } else {
                                msg = String.format(msgMap.get("msg_maintenance_time"), msgMap.get("machine"));
                            }
                            setBasicResponseError(response, false, ErrorMessages.E201_APPLICATION, msg);
                            return response;
                        // メンテ条件到達
                        } else if (machineVo.getMsgFlg() == 2) {
                            String msg;
                            if (startEndFlg) {
                                msg = String.format(msgMap.get("msg_confirm_maintenance_time_expired"), msgMap.get("machine"));
                            } else {
                                msg = String.format(msgMap.get("msg_maintenance_time_expired"), msgMap.get("machine"));
                            }
                            setBasicResponseError(response, false, ErrorMessages.E201_APPLICATION, msg);
                            return response;
                        }
                    }
                }
            }
        }
        
        return response;
        // Iterator4.2メンテサイクルチェック end
    }
    
    /**
     * 生産実績テーブル条件検索(生産実績明細行取得)
     *
     * @param componentCode
     * @param productionDateFrom
     * @param productionDateTo
     * @param directionCode
     * @param userId
     * @param userName
     * @param department
     * @param moldId
     * @param moldType
     * @param machineId
     * @param machineType
     * @param workStartDateTime
     * @param nextDayWorkStartDateTime
     * @param finalProcedureOnly
     * @param sidx
     * @param sord
     * @return
     */
    @GET
    @Path("searchbypage")
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionDetailList getProductionsByConditionPage(
            @QueryParam("componentCode") String componentCode // 部品コード
            , @QueryParam("productionDateFrom") String productionDateFrom // 生産日From yyyy/MM/dd
            , @QueryParam("productionDateTo") String productionDateTo // 生産日To yyyy/MM/dd
            , @QueryParam("directionCode") String directionCode // 手配番号
            , @QueryParam("userId") String userId // 生産者ID
            , @QueryParam("userName") String userName // 生産者氏名
            , @QueryParam("department") Integer department // 部署
            , @QueryParam("moldId") String moldId // 金型ID
            , @QueryParam("moldType") Integer moldType //  金型種類
            , @QueryParam("machineId") String machineId // 設備ID
            , @QueryParam("machineType") Integer machineType
            , @QueryParam("workStartDateTime") String workStartDateTime // 選択日付の業務開始時刻
            , @QueryParam("nextDayWorkStartDateTime") String nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
            , @QueryParam("finalProcedureOnly") int finalProcedureOnly
            , @QueryParam("sidx") String sidx // ソートキー
            , @QueryParam("sord") String sord // ソート順
            , @QueryParam("pageNumber") int pageNumber // ページNo
            , @QueryParam("pageSize") int pageSize // ページSize
    ) {

        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        // クエリパラムログ出力
        logger.log(Level.FINE, "QueryParams'{'"
                + ", componentCode:{0}, productionDateFrom:{1}, productionDateTo:{2}, directionCode:{3}, userId:{4}, userName:{5}, department:{6}, moldId:{7}, moldType:{8}}", new Object[]{componentCode, productionDateFrom, productionDateTo, directionCode, userId, userName, department, moldId, moldType}
        );
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        // 日付項目をDate型(yyyy/MM/dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        java.util.Date formatProductionDateFrom = null;
        java.util.Date formatProductionDateTo = null;

        SimpleDateFormat sdf1 = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        java.util.Date formatWorkStartDateTime = null;
        java.util.Date formatNextDayWorkStartDateTime = null;
        try {
            if (productionDateFrom != null && !"".equals(productionDateFrom)) {
                formatProductionDateFrom = sdf.parse(productionDateFrom);
            }
            if (productionDateTo != null && !"".equals(productionDateTo)) {
                formatProductionDateTo = sdf.parse(productionDateTo);
            }
            if (workStartDateTime != null && !"".equals(workStartDateTime)) {
                formatWorkStartDateTime = sdf1.parse(workStartDateTime);
            }
            if (nextDayWorkStartDateTime != null && !"".equals(nextDayWorkStartDateTime)) {
                formatNextDayWorkStartDateTime = sdf1.parse(nextDayWorkStartDateTime);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblProductionResource.class.getName()).log(Level.WARNING, null, "日付形式不正 productioDateFrom[" + productionDateFrom + "], productioDateTo[" + productionDateTo + "]");
        }
        // データ取得
        TblProductionDetailList tblProductionDetailList = tblProductionDetailService
                .getProductionDetailsByConditionPage(componentCode, formatProductionDateFrom, formatProductionDateTo,
                        directionCode, userId, userName, department, moldId, moldType, machineId, machineType,
                        formatWorkStartDateTime, formatNextDayWorkStartDateTime, finalProcedureOnly, sidx, sord, pageNumber, pageSize,
                        true);

        /**
         * 部署名のために部署リストを選択肢マスタより取得しておく
         */
        MstChoiceList departments = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");

        /**
         * 作業内容名称取得のために選択肢マスタより取得しておく
         */
        MstChoiceList workCategories = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_work.work_category");

        /*
         * 各種IDをコード値に変換して設定
         */
        ArrayList<TblProductionDetail> codeBindedTblProductionDetails = new ArrayList<>(); // 結果再格納用リスト
        if (!tblProductionDetailList.getTblProductionDetails().isEmpty()) {
            for (TblProductionDetail tblProductionDetail : tblProductionDetailList.getTblProductionDetails()) {
                if (tblProductionDetail.getTblProduction() != null) {
                    setOuterCodesForHeader(tblProductionDetail.getTblProduction(), loginUser, departments, workCategories);
                }
                setOuterCodesForDetails(tblProductionDetail, loginUser);
                codeBindedTblProductionDetails.add(tblProductionDetail);
            }
        }
        TblProductionDetailList response = new TblProductionDetailList();
        response.setCount(tblProductionDetailList.getCount());
        response.setPageNumber(tblProductionDetailList.getPageNumber());
        response.setPageTotal(tblProductionDetailList.getPageTotal());
        response.setTblProductionDetails(codeBindedTblProductionDetails);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    
    /**
     * スマホ版生産登録、生産中一覧画面Hear取得用
     * @param department
     * @param machineId
     * @param moldId
     * @param componentCode
     * @return
     */
    @GET
    @Path("header/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionList getProductionForMobile(@QueryParam("department") String department,
        @QueryParam("machineId") String machineId,
        @QueryParam("moldId") String moldId,
        @QueryParam("componentCode") String componentCode) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        // データ取得
        TblProductionList productionList = tblProductionService.getProductionForMobile(department, machineId, moldId, componentCode);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return productionList;
    }
    
    /**
     * 部品コード、前工程番号から生産実績ロット番号リスト取得
     * 
     * @param componentId
     * @param prevProcedureId
     * @param productionDate
     * @return 
     */
    @GET
    @Path("lot/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblProductionList getProcuctionLotListByPrevProcedureId(@QueryParam("componentId") String componentId,
        @QueryParam("prevProcedureId") String prevProcedureId,
        @QueryParam("productionDate") String productionDate) {
        
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        java.util.Date formatProductionDate = null;
        try {
            if (productionDate != null && !"".equals(productionDate)) {
                formatProductionDate = sdf.parse(productionDate);
            }
        } catch (ParseException ex) {
            Logger.getLogger(TblProductionResource.class.getName()).log(Level.WARNING, null, "日付形式不正 productioDate[" + formatProductionDate + "]");
        }
        TblProductionList productionList = tblProductionService.getProcuctionLotListByPrevProcedureId(componentId, prevProcedureId, formatProductionDate);
        return productionList;
    }
    
    /**
     * 生産実績取込
     *
     * @param fileUuid
     * @return an instance of ImportResultResponse
     */
    @POST
    @Path("importproduction/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ImportResultResponse postProductionCsv(@PathParam("fileUuid") String fileUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblProductionService.postProductionCsv(fileUuid, loginUser);
    }
}
