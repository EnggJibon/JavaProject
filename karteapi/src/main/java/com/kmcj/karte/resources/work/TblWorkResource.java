/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
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
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.direction.TblDirectionService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.machine.MstMachineVo;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtnService;
import com.kmcj.karte.resources.mold.MstMoldDetail;
import com.kmcj.karte.resources.mold.MstMoldService;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.production.TblProductionService;
import com.kmcj.karte.resources.production.suspension.TblProductionSuspension;
import com.kmcj.karte.resources.production.suspension.TblProductionSuspensionlService;
import com.kmcj.karte.resources.work.close.entry.TblWorkCloseEntry;
import com.kmcj.karte.resources.work.close.entry.TblWorkCloseEntryList;
import com.kmcj.karte.resources.work.close.entry.TblWorkCloseEntryService;
import com.kmcj.karte.resources.work.phase.MstWorkPhaseService;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.apache.commons.lang.StringUtils;

/**
 * 作業実績テーブルリソース
 * @author t.ariki
 */
@RequestScoped
@Path("work")
public class TblWorkResource {
    
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
    TblProductionSuspensionlService tblProductionSuspensionlService;
    
    @Inject
    MstMoldService mstMoldService;
    
    @Inject
    TblDirectionService tblDirectionService;
    
    @Inject
    MstComponentService mstComponentService;
    
    @Inject
    private TblProductionService tblProductionService;
    
    @Inject
    TblMaintenanceCyclePtnService tblMaintenanceCyclePtnService;
    
    @Inject
    MstMachineService mstMachineService;
    
    private Logger logger = Logger.getLogger(TblWorkResource.class.getName());

    public TblWorkResource() {}
    
    /**
     * 作業実績テーブルリスト取得(ログインユーザー指定)
     * @param notFinished
     * @return 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TblWorkList getWorksByLoginUserId(@QueryParam("notFinished") boolean notFinished) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        
        // データ取得
        TblWorkList tblWorks = tblWorkService.getWorksByLoginUserId(loginUser.getUserUuid(), notFinished);
        
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
        ArrayList<TblWork> codeBindedTblWorks = new ArrayList<>(); // 結果再格納用リスト
        if (!tblWorks.getTblWorks().isEmpty()) {
            for (TblWork tblWork : tblWorks.getTblWorks()) {
                setOuterCodes(tblWork, loginUser, departments, workCategories);
                codeBindedTblWorks.add(tblWork);
            }
        }
        
        TblWorkList response = new TblWorkList();
        response.setTblWorks(codeBindedTblWorks);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    
    /**
     * 作業実績テーブル1件取得(ID指定)
     * @param id
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TblWorkList getWorkById(@PathParam("id") String id) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        
        // データ取得
        TblWorkList tblWorks = tblWorkService.getWorkById(id);
        
        /**
         * 部署名のために部署リストを選択肢マスタより取得しておく
         */
        MstChoiceList departments = mstChoiceService.getChoice(loginUser.getLangId() ,"mst_user.department");
        
        /**
         * 作業内容名称取得のために選択肢マスタより取得しておく
         */
        MstChoiceList workCategories = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_work.work_category");

        /*
         * 各種IDをコード値に変換して設定
         */
        ArrayList<TblWork> codeBindedTblWorks = new ArrayList<>(); // 結果再格納用リスト
        if (!tblWorks.getTblWorks().isEmpty()) {
            for (TblWork tblWork : tblWorks.getTblWorks()) {
                setOuterCodes(tblWork, loginUser, departments, workCategories);
                codeBindedTblWorks.add(tblWork);
            }
        }
        TblWorkList response = new TblWorkList();
        response.setTblWorks(codeBindedTblWorks);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 作業実績テーブルを検索条件指定で検索し取得
     * @param userId
     * @param workingDateFrom yyyy/MM/dd
     * @param workingDateTo yyyy/MM/dd
     * @param userName
     * @param department
     * @param procCd
     * @param locked
     * @param machineId
     * @param workStartDateTime
     * @param nextDayWorkStartDateTime
     * @return 
     */
    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public TblWorkList getWorksByCondition(
            @QueryParam("userId") String userId     // ユーザーID
           ,@QueryParam("workingDateFrom") String workingDateFrom // 作業日From yyyy/MM/dd
           ,@QueryParam("workingDateTo") String workingDateTo // 作業日To yyyy/MM/dd
           ,@QueryParam("userName") String userName // 作業者氏名
           ,@QueryParam("department") Integer department // 部署
           ,@QueryParam("procCd") String procCd // 所属工程コード
           ,@QueryParam("locked") Integer locked // ロック 
           ,@QueryParam("machineId") String machineId // 設備ID
           ,@QueryParam("workStartDateTime") String workStartDateTime // 選択日付の業務開始時刻
           ,@QueryParam("nextDayWorkStartDateTime") String nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
           
    ) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        // クエリパラムログ出力
        logger.log(Level.FINE, "QueryParams'{'"
                + ", userId:{0}, workingDateFrom:{1}, workingDateTo:{2}, userName:{3}, department:{4}, procCd:{5}, locked:{6}}"
                , new Object[]{userId, workingDateFrom, workingDateTo, userName, department, procCd, locked}
        );
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        
        // 日付項目をDate型(yyyy/MM/dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        java.util.Date formatWorkingDateFrom= null;
        java.util.Date formatWorkingDateTo= null;
        
        SimpleDateFormat sdf1 = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        java.util.Date formatWorkStartDateTime = null;
        java.util.Date formatNextDayWorkStartDateTime = null;
        try {
            if (workingDateFrom != null && !"".equals(workingDateFrom)) {
                formatWorkingDateFrom = sdf.parse(workingDateFrom);
            }
            if (workingDateTo != null && !"".equals(workingDateTo)) {
                formatWorkingDateTo = sdf.parse(workingDateTo);
            }
            if (workStartDateTime != null && !"".equals(workStartDateTime)) {
                formatWorkStartDateTime = sdf1.parse(workStartDateTime);
            }
            if (nextDayWorkStartDateTime != null && !"".equals(nextDayWorkStartDateTime)) {
                formatNextDayWorkStartDateTime = sdf1.parse(nextDayWorkStartDateTime);
            }
        } catch (ParseException ex) {     
           Logger.getLogger(TblWorkResource.class.getName()).log(Level.WARNING, null, "日付形式不正 workingDateFrom[" + workingDateFrom + "], workingDateTo[" + workingDateTo + "]"); 
        }
        // データ取得
        TblWorkList tblWorks = tblWorkService.getWorkByCondition(
            userId
           ,formatWorkingDateFrom
           ,formatWorkingDateTo
           ,userName
           ,department
           ,procCd
           ,locked
           ,machineId
           ,formatWorkStartDateTime
           ,formatNextDayWorkStartDateTime
        );

        /**
         * 部署名のために部署リストを選択肢マスタより取得しておく
         */
        MstChoiceList departments = mstChoiceService.getChoice(loginUser.getLangId() ,"mst_user.department");
        
        /**
         * 作業内容名称取得のために選択肢マスタより取得しておく
         */
        MstChoiceList workCategories = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_work.work_category");
        
        /*
         * 各種IDをコード値に変換して設定
         */
        ArrayList<TblWork> codeBindedTblWorks = new ArrayList<>(); // 結果再格納用リスト
        if (!tblWorks.getTblWorks().isEmpty()) {
            for (TblWork tblWork : tblWorks.getTblWorks()) {
                setOuterCodes(tblWork, loginUser, departments, workCategories);
                codeBindedTblWorks.add(tblWork);
            }
        }
        TblWorkList response = new TblWorkList();
        response.setTblWorks(codeBindedTblWorks);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    
    @GET
    @Path("conflictcheck")
    @Produces(MediaType.APPLICATION_JSON)
    public String conflictCheck(@QueryParam("machineId") String machineId, @QueryParam("machineUuid") String machineUuid) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        Gson gson = new GsonBuilder().setDateFormat(DateFormat.DATETIME_FORMAT).create();
        Map<String, Object> response = new HashMap<>();
        boolean result = false;
        int count = 0;
        //他人が設備を使っているか
        TblWork work = tblWorkService.getMachineUsingWork(machineId, machineUuid, loginUser.getUserUuid());
        if (work != null) {
            count++;
            result = true;
            response.put("machineUserId", work.getPersonUuid());
            response.put("machineUserName", work.getMstUser() != null ? work.getMstUser().getUserName() : "");
            response.put("machineUseStartDatetime", work.getStartDatetime());
        }
        response.put("hasConflict", result);
        response.put("count", count);
        return gson.toJson(response);
    }

    @GET
    @Path("mold/conflictcheck")
    @Produces(MediaType.APPLICATION_JSON)
    public String moldConflictCheck(@QueryParam("moldId") String moldId, @QueryParam("moldUuid") String moldUuid) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        Gson gson = new GsonBuilder().setDateFormat(DateFormat.DATETIME_FORMAT).create();
        Map<String, Object> response = new HashMap<>();
        boolean result = false;
        int count = 0;
        //他人が金型を使っているか
        TblWork work = tblWorkService.getMoldUsingWork(moldId, moldUuid, loginUser.getUserUuid());
        if (work != null) {
            count++;
            result = true;
            response.put("moldUserId", work.getPersonUuid());
            response.put("moldUserName", work.getMstUser() != null ? work.getMstUser().getUserName() : "");
            response.put("moldUseStartDatetime", work.getStartDatetime());
        }
        response.put("hasConflict", result);
        response.put("count", count);
        return gson.toJson(response);
    }

    /**
     * 作業実績テーブル条件検索件数取得
     * @param userId
     * @param workingDateFrom
     * @param workingDateTo
     * @param userName
     * @param department
     * @param procCd
     * @param locked
     * @param machineId
     * @param workStartDateTime
     * @param nextDayWorkStartDateTime
     * @return 
     */
    @GET
    @Path("search/count")
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getWorksCountByCondition(
            @QueryParam("userId") String userId     // ユーザーID
           ,@QueryParam("workingDateFrom") String workingDateFrom // 作業日From yyyy/MM/dd
           ,@QueryParam("workingDateTo") String workingDateTo // 作業日To yyyy/MM/dd
           ,@QueryParam("userName") String userName // 作業者氏名
           ,@QueryParam("department") Integer department // 部署
           ,@QueryParam("procCd") String procCd // 所属工程コード
           ,@QueryParam("locked") Integer locked // ロック
           ,@QueryParam("machineId") String machineId // 設備ID
           ,@QueryParam("workStartDateTime") String workStartDateTime // 選択日付の業務開始時刻
           ,@QueryParam("nextDayWorkStartDateTime") String nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
    ) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        // 日付項目をDate型(yyyy/MM/dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        java.util.Date formatWorkingDateFrom = null;
        java.util.Date formatWorkingDateTo = null;
        
        SimpleDateFormat sdf1 = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        java.util.Date formatWorkStartDateTime = null;
        java.util.Date formatNextDayWorkStartDateTime = null;
        try {
            if (workingDateFrom != null && !"".equals(workingDateFrom)) {
                formatWorkingDateFrom = sdf.parse(workingDateFrom);
            }
            if (workingDateTo != null && !"".equals(workingDateTo)) {
                formatWorkingDateTo = sdf.parse(workingDateTo);
            }
            if (workStartDateTime != null && !"".equals(workStartDateTime)) {
                formatWorkStartDateTime = sdf1.parse(workStartDateTime);
            }
            if (nextDayWorkStartDateTime != null && !"".equals(nextDayWorkStartDateTime)) {
                formatNextDayWorkStartDateTime = sdf1.parse(nextDayWorkStartDateTime);
            }
        } catch (ParseException ex) {     
           Logger.getLogger(TblWorkResource.class.getName()).log(Level.WARNING, null, "日付形式不正 workingDateFrom[" + workingDateFrom + "], workingDateTo[" + workingDateTo + "]"); 
        }
        
        // データ取得
        CountResponse count = tblWorkService.getWorksCountByCondition( 
             userId
            ,formatWorkingDateFrom
            ,formatWorkingDateTo
            ,userName
            ,department
            ,procCd
            ,locked
            ,machineId
            ,formatWorkStartDateTime
            ,formatNextDayWorkStartDateTime
                
        );
        
        // システム上限を取得
        CnfSystem cnf = cnfSystemService.findByKey("system", "max_list_record_count");
        long sysCount = Long.parseLong(cnf.getConfigValue());
        
        if (count.getCount() > sysCount) {
            count.setError(true);
            count.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
            msg = String.format(msg, sysCount);
            count.setErrorMessage(msg);
        }
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return count;
    }
    
    /**
     * 作業実績テーブルCSV出力
     * @param userId
     * @param workingDateFrom
     * @param workingDateTo
     * @param userName
     * @param department
     * @param procCd
     * @param locked
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
            @QueryParam("userId") String userId     // ユーザーID
           ,@QueryParam("workingDateFrom") String workingDateFrom // 作業日From yyyy/MM/dd
           ,@QueryParam("workingDateTo") String workingDateTo // 作業日To yyyy/MM/dd
           ,@QueryParam("userName") String userName // 作業者氏名
           ,@QueryParam("department") Integer department // 部署
           ,@QueryParam("procCd") String procCd // 所属工程コード
           ,@QueryParam("locked") Integer locked // ロック 
           ,@QueryParam("machineId") String machineId // 設備ID
           ,@QueryParam("workStartDateTime") String workStartDateTime // 選択日付の業務開始時刻
           ,@QueryParam("nextDayWorkStartDateTime") String nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
    ) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        
        // データ取得
        TblWorkList tblWorkList = getWorksByCondition( 
             userId
            ,workingDateFrom
            ,workingDateTo
            ,userName
            ,department
            ,procCd
            ,locked
            ,machineId
            ,workStartDateTime
            ,nextDayWorkStartDateTime
        );
        
        /**
         * データが取得できなかった場合はエラー返却
         */
        FileReponse response = new FileReponse();
        /*
        if (tblWorkList.getTblWorks() == null || tblWorkList.getTblWorks().isEmpty()) {
            setApplicationError(response, loginUser, "mst_error_record_not_found", "作業実績データCSV出力対象無し");
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }
        */
        /*
         * 作成したCSV出力テーブルのIDを設定したファイルレスポンスを返却
         */
        response = tblWorkService.getWorksCSV(tblWorkList, loginUser);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    
    /**
     * 作業実績テーブル作業日指定検索
     * @param workingDate yyyy/MM/dd
     * @return 
     */
    @GET
    @Path("search/workingDate")
    @Produces(MediaType.APPLICATION_JSON)
    public TblWorkList getWorksByWorkingDate(@QueryParam("workingDate") String workingDate) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        // クエリパラムログ出力
        logger.log(Level.FINE, "QueryParams'{'workingDate:{0}}" , new Object[]{workingDate});
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        
        TblWorkList response = new TblWorkList();
        
        if (workingDate == null || "".equals(workingDate)) {
            setApplicationError(response, loginUser, "msg_error_not_null", "workingDate is not found : " + workingDate);
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }
        
        // 日付項目をDate型(yyyy/MM/dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        java.util.Date formatWorkingDate = null;
        try {
            formatWorkingDate = sdf.parse(workingDate);
        } catch (ParseException ex) {     
            logger.log(Level.WARNING, null, "日付形式不正 workingDate[" + workingDate + "]"); 
            setApplicationError(response, loginUser, "msg_error_not_null", "workingDate is not found : " + workingDate);
            return response;
        }
        // データ取得
        TblWorkList tblWorks = tblWorkService.getWorkByWorkingDate(formatWorkingDate, loginUser);
        
        /**
         * 部署名のために部署リストを選択肢マスタより取得しておく
         */
        MstChoiceList departments = mstChoiceService.getChoice(loginUser.getLangId() ,"mst_user.department");
        
        /**
         * 作業内容名称取得のために選択肢マスタより取得しておく
         */
        MstChoiceList workCategories = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_work.work_category");

        /*
         * 各種IDをコード値に変換して設定
         */
        ArrayList<TblWork> codeBindedTblWorks = new ArrayList<>(); // 結果再格納用リスト
        if (!tblWorks.getTblWorks().isEmpty()) {
            for (TblWork tblWork : tblWorks.getTblWorks()) {
                setOuterCodes(tblWork, loginUser, departments, workCategories);
                codeBindedTblWorks.add(tblWork);
            }
        }
        
        response.setTblWorks(codeBindedTblWorks);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    
    /**
     * 作業実績テーブル登録
     * @param tblWork
     * @return 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postWork(TblWork tblWork) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        TblProduction tblProduction = null;
        if (tblWork.getProductionId() != null && !tblWork.getProductionId().equals("")) {
            // 一時中断の場合
            tblProduction = tblProductionService.getProductionSingleById(tblWork.getProductionId());
            if (tblProduction == null) {
                setApplicationError(response, loginUser, "msg_error_data_deleted", "TblProduction");
                return response;
            }
            if (tblProduction.getStatus() != null && (tblProduction.getStatus() == 1 || tblProduction.getStatus() == 2)) {
                // すでに中断している場合は何もしない。
                setApplicationError(response, loginUser, "msg_error_suspend", "TblProduction");
                return response;
            }
            // 終了の場合
            if (tblProduction.getStatus() != null && tblProduction.getStatus() == 9) {
                // すでに終了している場合は何もしない。
                setApplicationError(response, loginUser, "msg_production_end", "TblProduction");
                return response;
            }
        }
        
        // 作業開始登録ボタンのメンテサイクルチェック行う
        response = chkWorkMainteCycle(tblWork, loginUser, response, true);
        if (response.getErrorCode() != null && !"".equals(response.getErrorCode())) {
            return response;
        }
        
        // 作業実績テーブル登録チェックおよび登録値設定
        tblWorkService.setCreateData(response, tblWork, loginUser);
        if (response.isError()) {
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }
        
        // 作業実績テーブル登録
        String workId = tblWorkService.createTblWork(tblWork, loginUser);
        
        // 作業開始画面に遷移し、作業登録がされたら、選択されている生産実績を一時中断状態にし、生産中断履歴テーブルに中断時刻と作業IDを記録する。]
        if (StringUtils.isNotEmpty(tblWork.getProductionId()) && tblProduction != null) {
            TblProductionSuspension tblProductionSuspension = new TblProductionSuspension();
            tblProductionSuspension.setWorkEnd(0);
            tblProductionSuspension.setWorkId(workId);
            tblProductionSuspension.setInterruptionFlag(2);
            tblProductionSuspension.setProductionId(tblWork.getProductionId());
            response = tblProductionSuspensionlService.changeStatus(tblProductionSuspension, tblProduction, loginUser);
            if (!response.getErrorMessage().isEmpty()) {
                logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
                return response;
            }
        }

        //response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_added"));
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    
    /**
     * 作業実績テーブル更新
     * @param tblWork
     * @return 
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putWork(TblWork tblWork) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        
        TblWork updateTblWork = tblWorkService.setUpdateData(response, tblWork, loginUser);
        if (response.isError()) {
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }
        
        // 作業実績テーブル更新
        tblWorkService.updateTblWork(updateTblWork, loginUser);
        
        // 作業終了により一時中断を解除する。 
        if (StringUtils.isNotEmpty(tblWork.getId())) {
            
            TblProductionSuspension tblProductionSuspensionTemp = tblProductionSuspensionlService.getWorkIdAndEndDatetimeIsNull(tblWork.getId());
            if (null != tblProductionSuspensionTemp && StringUtils.isNotEmpty(tblProductionSuspensionTemp.getProductionId())) {

                TblProductionSuspension tblProductionSuspension = new TblProductionSuspension();
                tblProductionSuspension.setWorkEnd(1);
                tblProductionSuspension.setInterruptionFlag(2);
                tblProductionSuspension.setWorkId(tblWork.getId());
                tblProductionSuspension.setProductionId(tblProductionSuspensionTemp.getProductionId());
                response = tblProductionSuspensionlService.changeStatus(tblProductionSuspension, null, loginUser);
            }
            if (response.isError()) {
                logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
                return response;
            }
        }
        
        // 作業開始登録ボタンのメンテサイクルチェック行う
        tblWork.setIsStart(false);
        response = chkWorkMainteCycle(tblWork, loginUser, response, false);
        if (response.getErrorCode() != null && !"".equals(response.getErrorCode())) {
            return response;
        }
        
        //response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    
    /**
     * 作業実績テーブル1件削除(ID指定)
     * @param id
     * @return 
     */
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteWork(@PathParam("id") String id) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        
        tblWorkService.setDeleteData(response, id, loginUser);
        if (response.isError()) {
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }
        
        // 作業実績テーブル削除
        tblWorkService.deleteTblWork(id);
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 作業実績テーブル一括反映
     * @param tblWorkList
     * @return 
     */
    @POST
    @Path("replace")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse replaceWorks(TblWorkList tblWorkList) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        
        ArrayList<TblWork> replacement = new ArrayList<>();
        /*
         * 入力チェックおよび値設定
         */
        for (TblWork tblWork : tblWorkList.getTblWorks()) {
            // 登録可能チェック
            if (tblWork.isAdded()) {
                tblWorkService.setCreateData(response, tblWork, loginUser);
                if (response.isError()) {
                    logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                    return response;
                }
                replacement.add(tblWork);
            }

            // 更新可能チェック
            if (tblWork.isModified()) {
                TblWork updateTblWork = tblWorkService.setUpdateData(response, tblWork, loginUser);
                if (response.isError()) {
                    logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                    return response;
                }
                replacement.add(updateTblWork);
            }

            // 削除可能チェック
            if (tblWork.isDeleted()) {
                tblWorkService.setDeleteData(response, tblWork.getId(), loginUser);
                if (response.isError()) {
                    logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                    return response;
                }
                replacement.add(tblWork);
            }
        }
        
        /*
         * 一括反映
         */
        for (TblWork tblWork : replacement) {
            if (tblWork.isAdded()) {
                tblWorkService.createTblWork(tblWork, loginUser);
            }
            if (tblWork.isModified()) {
                tblWorkService.updateTblWork(tblWork, loginUser);
            }
            if (tblWork.isDeleted()) {
                tblWorkService.deleteTblWork(tblWork.getId());
            }
        }
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }

    /**
     * 作業実績テーブルロック更新
     * @param tblWork
     * @return 
     */
    @PUT
    @Path("lock")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putLock(TblWork tblWork) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        
        TblWork updateTblWork = tblWorkService.setUpdateForLock(response, tblWork, loginUser);
        if (response.isError()) {
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }
        // 作業実績テーブル更新
        tblWorkService.updateTblWork(updateTblWork, loginUser);
        
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    
    /**
     * 作業実績テーブルロック一括更新
     * @param tblWorkList
     * @return 
     */
    @PUT
    @Path("lockall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse putLockAll(TblWorkList tblWorkList) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();
        
        for (TblWork tblWork : tblWorkList.getTblWorks()) {
            TblWork updateTblWork = tblWorkService.setUpdateForLock(response, tblWork, loginUser);
            // 画面パラメータのロックを設定
            updateTblWork.setLocked(tblWork.getLocked());
            if (response.isError()) {
                logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
                return response;
            }
            // 作業実績テーブル更新
            tblWorkService.updateTblWork(updateTblWork, loginUser);
        }
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    
    /**
     * 作業実績テーブル入力締め
     * @param closedDate
     * @return 
     */
    @POST
    @Path("close")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse closeWorks(java.util.Date closedDate) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        // クエリパラムログ出力
        logger.log(Level.FINE, "PostPram'{closedDate:{0}}", new Object[]{closedDate});
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        BasicResponse response = new BasicResponse();

        /*
         * 日付判定
         */
        if (closedDate == null) {
            setApplicationError(response, loginUser, "msg_error_cutoff_not_changed", "closedDate is null");
            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
            return response;
        }
//        // 最新締め日を取得
//        TblWorkCloseEntry latest = tblWorkCloseEntryService.getLatest();
//        if (latest != null) {
//            // 最新の締め日より同日含む過去の場合はエラー
//            if (latest.getClosedDate().compareTo(closedDate) >= 0) {
//                setApplicationError(response, loginUser, "work_non_enterable", "closedDate:" + closedDate);
//                logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{methodName, response.getErrorMessage()});
//                return response;
//            }
//        }
        
        /*
         * 作業入力締めテーブル操作
         */
        // 作業入力締めテーブルのデータを全て取得
        TblWorkCloseEntryList tblWorkCloseEntryList = tblWorkCloseEntryService.getAll();
        for (TblWorkCloseEntry tblWorkCloseEntry : tblWorkCloseEntryList.getTblWorkCloseEntries()) {
            // 最新フラグが立っているデータを0に更新
            if (tblWorkCloseEntry.getLatestFlg() == 1) {
                tblWorkCloseEntry.setLatestFlg(0);
                tblWorkCloseEntryService.updateTblWorkCloseEntry(tblWorkCloseEntry, loginUser);
            }
        }
        // 最新レコード登録
        TblWorkCloseEntry registTblWorkCloseEntry = new TblWorkCloseEntry();
        registTblWorkCloseEntry.setClosedDate(closedDate);  // 入力締日
        registTblWorkCloseEntry.setLatestFlg(1);            // 最新フラグ
        tblWorkCloseEntryService.createTblWorkCloseEntry(registTblWorkCloseEntry, loginUser);
        
        /*
         * 作業実績テーブル操作
         */
        // 締め日以前の作業実績をすべて取得
        TblWorkList tblWorkList = tblWorkService.getCloseTarget(closedDate);
        for (TblWork tblWork : tblWorkList.getTblWorks()) {
            // ロック状態に更新
            tblWork.setLocked(1);
            tblWorkService.updateTblWork(tblWork, loginUser);
        }
        
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
    
    /**
     * TblWorkの各種コード値からIDや名称を取得し設定する
     *  作業工程ID ⇒ 作業工程マスタ.選択肢連番
     *  作業内容 ⇒ 選択肢マスタ.作業内容名称
     *  手配・工事ID ⇒ 手配テーブル.手配・工事番号
     *  部品ID ⇒ 部品マスタ.部品コード
     *  金型UUID ⇒ 金型マスタ.金型ID
     *  @param tblWork 
     */
    private void setOuterCodes(TblWork tblWork, LoginUser loginUser, MstChoiceList departments, MstChoiceList workCategories) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        // ユーザーマスタ
        if (tblWork.getMstUser() != null) {
            tblWork.setUserId(tblWork.getMstUser().getUserId());
            tblWork.setUserName(tblWork.getMstUser().getUserName());
            // 部署名設定
            if (tblWork.getMstUser().getDepartment() != null) {
                if (departments != null && departments.getMstChoice() != null) {
                    tblWork.setDepartment(Integer.parseInt(tblWork.getMstUser().getDepartment()));
                    for (MstChoice department : departments.getMstChoice()) {
                        // 部署コード(SEQ)が一致する場合は名称設定
                        if (tblWork.getDepartment() == Integer.parseInt(department.getMstChoicePK().getSeq())) {
                            tblWork.setDepartmentName(department.getChoice());
                        }
                    }
                }
            }
        }
        
        // 作業工程マスタ.選択肢連番取得
        if (tblWork.getMstWorkPhase() != null) {
            tblWork.setWorkPhaseChoiceSeq(tblWork.getMstWorkPhase().getChoiceSeq());
            tblWork.setWorkPhaseName(tblWork.getMstWorkPhase().getWorkPhaseName());
        }
        
        // 選択肢マスタ.作業内容名称設定
        if (tblWork.getMstWorkPhase() != null && tblWork.getWorkCategory() != null) {
            if (workCategories != null && workCategories.getMstChoice() != null) {
                for (MstChoice workCategory : workCategories.getMstChoice()) {
                    if (workCategory.getParentSeq() != null && workCategory.getMstChoicePK().getSeq() != null) {
                        // 親SEQおよびSEQが一致する場合は名称設定
                        if (//tblWork.getWorkPhaseChoiceSeq() == Integer.parseInt(workCategory.getParentSeq())
                                //&& 
                                tblWork.getWorkCategory() == Integer.parseInt(workCategory.getMstChoicePK().getSeq()))
                         {
                            tblWork.setWorkCategoryName(workCategory.getChoice());
                        }
                    }
                }
            }
        }

        // 手配テーブル.手配・工事番号取得
        if (tblWork.getTblDirection() != null) {
            tblWork.setDirectionCode(tblWork.getTblDirection().getDirectionCode());
        }

        // 部品マスタ.部品コード取得
        if (tblWork.getMstComponent() != null) {
            tblWork.setComponentCode(tblWork.getMstComponent().getComponentCode());
            tblWork.setComponentName(tblWork.getMstComponent().getComponentName());
        }
        
        // 金型マスタ.金型ID取得
        if (tblWork.getMstMold() != null) {
            tblWork.setMoldId(tblWork.getMstMold().getMoldId());
            tblWork.setMoldName(tblWork.getMstMold().getMoldName());
        }
        tblWork.setStartDatetimeStr(new FileUtil().getDateTimeFormatForStr(tblWork.getStartDatetime()));

        tblWork.setEndDatetimeStr(new FileUtil().getDateTimeFormatForStr(tblWork.getEndDatetime()));
        
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
    }
    
    private BasicResponse setApplicationError(BasicResponse response, LoginUser loginUser, String dictKey, String errorContentsName) {
        setBasicResponseError(
                response
               ,true
               ,ErrorMessages.E201_APPLICATION
               ,mstDictionaryService.getDictionaryValue(loginUser.getLangId(), dictKey)
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
     * 作業開始/終了登録ボタンのメンテサイクルチェック処理
     * @param tblProductionVo
     * @param loginUser
     * @param response
     * @return 
     */
    private BasicResponse chkWorkMainteCycle(TblWork tblWork, LoginUser loginUser, BasicResponse response, boolean startEndFlg) {
        // Iterator4.2メンテサイクルチェック start
        // 「開始しますか」フラグ、Trueだったら、生産開始のロジックに行く
        if (!tblWork.getIsStart()) {
            Map<String, String> msgMap = null;
            // 金型指定された場合、サイクルパターンチェック行う
            if (tblWork.getMoldId() != null && !"".equals(tblWork.getMoldId())) {
                List<MstMoldDetail> chkTargetList = new ArrayList<>();
                // もらった金型IDにより金型マスタオブジェクト取得
                MstMoldDetail mold = mstMoldService.getMoldByMoldId(tblWork.getMoldId(), loginUser);
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
                    if (moldDetail.getMsgFlg() == 3 && !startEndFlg) {
                        List<String> dictKeyList = Arrays.asList("msg_maintenance_time_expired", "mold");
                        msgMap = FileUtil.getDictionaryList(mstDictionaryService, loginUser.getLangId(), dictKeyList);
                        String msg = String.format(msgMap.get("msg_maintenance_time_expired"), msgMap.get("mold"));
                        setBasicResponseError(response, false, ErrorMessages.E201_APPLICATION, msg);
                        return response;
                    }
                }
            }
            // 設備指定された場合、サイクルパターンチェック行う
            if (tblWork.getMachineUuid() != null && !"".equals(tblWork.getMachineUuid())) {
                List<MstMachineVo> chkTargetList = new ArrayList<>();
                // もらった設備IDにより設備マスタオブジェクト取得
                MstMachine machine = mstMachineService.getMstMachineByUuid(tblWork.getMachineUuid());
                if (machine == null) {
                    setBasicResponseError(response, false, ErrorMessages.E201_APPLICATION, "msg_error_data_deleted");
                    return response;
                }
                MstMachineVo mstMachineVo = new MstMachineVo();
                mstMachineVo.setMainteCycleCode01(machine.getMainteCycleId01());
                mstMachineVo.setMainteCycleCode02(machine.getMainteCycleId02());
                mstMachineVo.setMainteCycleCode03(machine.getMainteCycleId03());
                mstMachineVo.setAfterMainteTotalProducingTimeHour(machine.getAfterMainteTotalProducingTimeHour());
                mstMachineVo.setAfterMainteTotalShotCount(machine.getAfterMainteTotalShotCount());
                mstMachineVo.setLastMainteDate(machine.getLastMainteDate());
                mstMachineVo.setUuid(machine.getUuid());

                chkTargetList.add(mstMachineVo);
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
     * 作業開始の登録ボタン押下時にメンテ後初回利用判定API
     *
     * @param moldId
     * @return
     */
    @GET
    @Path("mold/firstuseaftermainte/{moldId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse checkFirstUseAfterMainte(@PathParam("moldId") String moldId) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblWorkService.checkFirstUseAfterMainte(moldId, loginUser.getLangId(), loginUser);
    }
    
    /**
     * 作業実績テーブルを検索条件指定で検索し取得
     * @param userId
     * @param workingDateFrom yyyy/MM/dd
     * @param workingDateTo yyyy/MM/dd
     * @param userName
     * @param department
     * @param procCd
     * @param locked
     * @param machineId
     * @param workStartDateTime
     * @param nextDayWorkStartDateTime
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return 
     */
    @GET
    @Path("searchpage")
    @Produces(MediaType.APPLICATION_JSON)
    public TblWorkList getWorksByConditionPage(
            @QueryParam("userId") String userId     // ユーザーID
           ,@QueryParam("workingDateFrom") String workingDateFrom // 作業日From yyyy/MM/dd
           ,@QueryParam("workingDateTo") String workingDateTo // 作業日To yyyy/MM/dd
           ,@QueryParam("userName") String userName // 作業者氏名
           ,@QueryParam("department") Integer department // 部署
           ,@QueryParam("procCd") String procCd // 所属工程コード
           ,@QueryParam("locked") Integer locked // ロック 
           ,@QueryParam("machineId") String machineId // 設備ID
           ,@QueryParam("workStartDateTime") String workStartDateTime // 選択日付の業務開始時刻
           ,@QueryParam("nextDayWorkStartDateTime") String nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
           ,@QueryParam("sidx") String sidx // ソートキー
           ,@QueryParam("sord") String sord // ソート順
           ,@QueryParam("pageNumber") int pageNumber // ページNo
           ,@QueryParam("pageSize") int pageSize // ページSize
           
    ) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        // クエリパラムログ出力
        logger.log(Level.FINE, "QueryParams'{'"
                + ", userId:{0}, workingDateFrom:{1}, workingDateTo:{2}, userName:{3}, department:{4}, procCd:{5}, locked:{6}}"
                , new Object[]{userId, workingDateFrom, workingDateTo, userName, department, procCd, locked}
        );
        
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        
        // 日付項目をDate型(yyyy/MM/dd)に変換
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        java.util.Date formatWorkingDateFrom= null;
        java.util.Date formatWorkingDateTo= null;
        
        SimpleDateFormat sdf1 = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        java.util.Date formatWorkStartDateTime = null;
        java.util.Date formatNextDayWorkStartDateTime = null;
        try {
            if (workingDateFrom != null && !"".equals(workingDateFrom)) {
                formatWorkingDateFrom = sdf.parse(workingDateFrom);
            }
            if (workingDateTo != null && !"".equals(workingDateTo)) {
                formatWorkingDateTo = sdf.parse(workingDateTo);
            }
            if (workStartDateTime != null && !"".equals(workStartDateTime)) {
                formatWorkStartDateTime = sdf1.parse(workStartDateTime);
            }
            if (nextDayWorkStartDateTime != null && !"".equals(nextDayWorkStartDateTime)) {
                formatNextDayWorkStartDateTime = sdf1.parse(nextDayWorkStartDateTime);
            }
        } catch (ParseException ex) {     
           Logger.getLogger(TblWorkResource.class.getName()).log(Level.WARNING, null, "日付形式不正 workingDateFrom[" + workingDateFrom + "], workingDateTo[" + workingDateTo + "]"); 
        }
        // データ取得
        TblWorkList tblWorks = tblWorkService.getWorkByConditionPage(
            userId
           ,formatWorkingDateFrom
           ,formatWorkingDateTo
           ,userName
           ,department
           ,procCd
           ,locked
           ,machineId
           ,formatWorkStartDateTime
           ,formatNextDayWorkStartDateTime
           ,sidx
           ,sord
           ,pageNumber
           ,pageSize
           ,true
        );

        /**
         * 部署名のために部署リストを選択肢マスタより取得しておく
         */
        MstChoiceList departments = mstChoiceService.getChoice(loginUser.getLangId() ,"mst_user.department");
        
        /**
         * 作業内容名称取得のために選択肢マスタより取得しておく
         */
        MstChoiceList workCategories = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_work.work_category");
        
        /*
         * 各種IDをコード値に変換して設定
         */
        ArrayList<TblWork> codeBindedTblWorks = new ArrayList<>(); // 結果再格納用リスト
        if (!tblWorks.getTblWorks().isEmpty()) {
            for (TblWork tblWork : tblWorks.getTblWorks()) {
                setOuterCodes(tblWork, loginUser, departments, workCategories);
                codeBindedTblWorks.add(tblWork);
            }
        }
        TblWorkList response = new TblWorkList();
        response.setCount(tblWorks.getCount());
        response.setPageNumber(tblWorks.getPageNumber());
        response.setPageTotal(tblWorks.getPageTotal());
        response.setTblWorks(codeBindedTblWorks);
        logger.log(Level.FINE, "  <--- [[{0}]] End", methodName);
        return response;
    }
}
