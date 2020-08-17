package com.kmcj.karte.resources.machine.history;

import com.google.gson.Gson;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.machine.MstMachineVo;
import com.kmcj.karte.resources.production.detail.TblProductionDetailList;
import com.kmcj.karte.resources.production.detail.TblProductionDetailService;
import com.kmcj.karte.resources.sigma.log.TblSigmaLogService;
import com.kmcj.karte.resources.sigma.log.TblSigmaLogVo;
import com.kmcj.karte.resources.work.TblWorkList;
import com.kmcj.karte.resources.work.TblWorkResource;
import com.kmcj.karte.resources.work.TblWorkService;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author penggd
 */
@RequestScoped
@Path("machine/history")
public class TblMachineHistoryResource {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private TblMachineHistoryService tblMachineHistoryService;

    @Inject
    private TblSigmaLogService tblSigmaLogService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstMachineService mstMachineService;
    
    @Inject
    TblWorkService tblWorkService;
    
    @Inject
    TblProductionDetailService tblProductionDetailService;

    public TblMachineHistoryResource() {

    }

    /**
     * T1101_設備稼働状況照会 検索ボタンでデータを取得
     *
     * @param startDate
     * @param endDate
     * @param department
     * @param machineId
     * @return 設備稼働状況照会List
     *
     */
    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TblMachineHistoryList getMachineHistories(@QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("department") String department,
            @QueryParam("machineId") String machineId) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineHistoryService.getTblMachineHistories(startDate, endDate, department, machineId, loginUser);
    }

    /**
     * T1101_設備稼働状況照会 CSVボタンでデータを取得
     *
     * @param machineHistoryList
     * @return CSV File
     */
    @POST
    @Path("exportcsv")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public FileReponse outoutMachineHistoriesToCsv(TblMachineHistoryList machineHistoryList) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMachineHistoryService.outoutMachineHistoriesToCsv(machineHistoryList, loginUser);
    }

    /**
     * T1101_設備稼働状況照会 詳細ボタンでデータを取得
     *
     * @param workDate
     * @param machineId
     * @return 設備稼働状況照会詳細
     *
     */
    @GET
    @Path("detail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TblMachineHistoryList getMachineHistory(@QueryParam("workDate") String workDate,
            @QueryParam("machineId") String machineId) {

        TblMachineHistoryList machineHistoryList = new TblMachineHistoryList();

        // 日付項目をDate型(yyyy/MM/dd) check
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat.DATE_FORMAT);

        if (machineId == null || "".equals(machineId)) {
            machineHistoryList.setError(true);
            machineHistoryList.setErrorCode(ErrorMessages.E201_APPLICATION);
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            String msgWorkDate = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "machine_id"); // 設備ＩＤ
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item");
            msg = String.format(msg, msgWorkDate);
            machineHistoryList.setErrorMessage(msg);
            return machineHistoryList;
        }

        if (workDate == null || "".equals(workDate)) {
            machineHistoryList.setError(true);
            machineHistoryList.setErrorCode(ErrorMessages.E201_APPLICATION);
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            String msgWorkDate = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "working_date"); // 作業日
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item");
            msg = String.format(msg, msgWorkDate);
            machineHistoryList.setErrorMessage(msg);
            return machineHistoryList;
        } else {
            try {
                sdf.parse(workDate);
            } catch (ParseException ex) {
                Logger.getLogger(TblWorkResource.class.getName()).log(Level.WARNING, null, "日付形式不正 workDate[" + workDate + "]");
                machineHistoryList.setError(true);
                machineHistoryList.setErrorCode(ErrorMessages.E201_APPLICATION);
                LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_date_format_invalid");
                machineHistoryList.setErrorMessage(msg);
                return machineHistoryList;
            }
        }
        machineHistoryList = tblMachineHistoryService.getTblMachineHistory(workDate, machineId);
        return machineHistoryList;
    }

    /**
     * deleteMachineHistoryByPK 削除設備稼働履歴情報
     *
     * @param machineUuid
     * @param firstEventNo
     * @param startDate
     * @return
     *
     */
    @DELETE
    @Path("deletemachinehistory/{machineUuid}/{firstEventNo}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CountResponse deleteMachineHistoryByPK(@PathParam("machineUuid") String machineUuid, @PathParam("firstEventNo") String firstEventNo, @QueryParam("startDate") String startDate) {

        CountResponse response = new CountResponse();

        try {

            // 削除件数を設定する
            response.setCount(tblMachineHistoryService.deleteMachineHistoryByPK(machineUuid, firstEventNo, startDate));

        } catch (Exception e) {// 異常処理

            response.setError(true);

            response.setErrorMessage(e.toString());

            response.setCount(0);

            return response;

        }

        return response;
    }

    /**
     * [KM-158] 設備稼動履歴データ登録APIの追加
     *
     * @param macKey
     * @param eventNo
     * @param createDate
     * @return
     */
    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CountResponse create(
            @QueryParam("macKey") String macKey, @QueryParam("eventNo") String eventNo, @QueryParam("createDate") String createDate, @QueryParam("deleteFirst") boolean deleteFirst) {
        MstMachineVo machineVo = mstMachineService.getMachineUuid(macKey);
        if (machineVo != null) {
            if (deleteFirst) {
                //Σ軍師バックアップ再取込に対応。再取込以降の稼動履歴を削除する。
                tblMachineHistoryService.deleteMachineHistory(machineVo.getUuid(), createDate);
                //稼働率テーブルがバッチで再計算されるよう指示する
                tblMachineHistoryService.recalcMachineOperatingRate(machineVo.getUuid());
            }
            return insert(machineVo.getUuid(), eventNo, createDate);
        } else {
            CountResponse countResponse = new CountResponse();
            LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
            countResponse.setError(true);
            countResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            countResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return countResponse;
        }
    }

    /*
     * insert設備稼動履歴データ登録
     *
     * @param machineUuid
     * @param eventNo
     * @param createDate
     * @return
     */
    @POST
    @Path("insert/{machineUuid}/{eventNo}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public CountResponse insert(@PathParam("machineUuid") String machineUuid, @PathParam("eventNo") String eventNo, @QueryParam("createDate") String createDate) {

        CountResponse response = new CountResponse();

        // 登録用のデータを取得
        List<TblMachineHistoryVo> tblMachineHistoryList = getInsertMachineHisList(machineUuid, eventNo, DateFormat.formatDateYear(createDate, DateFormat.DATETIMEMILL_FORMAT));

        int count = 0;

        try {

            List<TblMachineHistory> tblMachineHistoryInsertList = new ArrayList();

            if (tblMachineHistoryList.size() > 0) {

                for (TblMachineHistoryVo tblMachineHistoryVo : tblMachineHistoryList) {

                    TblMachineHistory tblMachineHistory = new TblMachineHistory();

                    BeanCopyUtil.copyFields(tblMachineHistoryVo, tblMachineHistory);

                    // 日時を設定する
                    if (StringUtils.isNotEmpty(tblMachineHistoryVo.getEndDate())) {
                        tblMachineHistory.setEndDate(DateFormat.strToDateMill(tblMachineHistoryVo.getEndDate()));
                    }

                    tblMachineHistory.setLastEventDate(DateFormat.strToDateMill(tblMachineHistoryVo.getLastEventDate()));

                    tblMachineHistory.setTblMachineHistoryPK(new TblMachineHistoryPK());

                    // PKを設定
                    tblMachineHistory.getTblMachineHistoryPK().setMachineUuid(tblMachineHistoryVo.getMachineUuid());
                    tblMachineHistory.getTblMachineHistoryPK().setFirstEventNo(Long.valueOf(tblMachineHistoryVo.getFirstEventNo()));
                    tblMachineHistory.getTblMachineHistoryPK().setStartDate(DateFormat.strToDateMill(tblMachineHistoryVo.getStartDate()));

                    // ショット数を設定
                    tblMachineHistory.setShotCnt(Long.valueOf(tblMachineHistoryVo.getShotCnt()));
                    // 最後のイベント番号
                    tblMachineHistory.setLastEventNo(Long.valueOf(tblMachineHistoryVo.getLastEventNo()));

                    tblMachineHistoryInsertList.add(tblMachineHistory);

                }

                if (tblMachineHistoryInsertList.size() > 0) {

                    count = tblMachineHistoryService.batchInsert(tblMachineHistoryInsertList);

                }

            }

            response.setCount(count);

        } catch (Exception e) {

            response.setError(true);

            response.setErrorMessage(e.toString());

            response.setCount(0);

            return response;

        }

        return response;
    }

    /**
     * バッチで設備履歴テーブルデータを取得
     *
     * @param histStartDate
     * @param machineUuid
     * @return
     */
    @GET
    @Path("extmachinehistory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMachineHistoryList getExtMachineHistorysByBatch(@QueryParam("histStartDate") String histStartDate, @QueryParam("machineUuid") String machineUuid) {
        return tblMachineHistoryService.getExtMachineHistorysByBatch(histStartDate, machineUuid);
    }

    /**
     * 設備稼働履歴登録用のデータを取得
     *
     * @param machineUuid
     * @param histStartDate
     * @param machineUuid
     * @return
     */
    private List<TblMachineHistoryVo> getInsertMachineHisList(String machineUuid, String startEventNo, String createDate) {

        List<TblMachineHistoryVo> tblMachineHistoryVos = new ArrayList();

        TblMachineHistoryVo unCloseMachine = tblMachineHistoryService.getTblMachineHistory(machineUuid, startEventNo, createDate);

        //　不要な設備稼働履歴情報を削除する
        tblMachineHistoryService.deleteMachineHistoryByPK(machineUuid, unCloseMachine.getFirstEventNo(), unCloseMachine.getStartDate());

        // 設備ログ情報を取得する
        List<TblSigmaLogVo> tblSigmaLogVos = tblSigmaLogService.getMachineHis(machineUuid, unCloseMachine.getFirstEventNo(), unCloseMachine.getStartDate());

        TblMachineHistoryVo tblMachineHistoryVo = null;

        boolean isOver = true;

        for (int i = 0; i < tblSigmaLogVos.size(); i++) {

            TblSigmaLogVo sigmaLogVo = tblSigmaLogVos.get(i);

            if (isOver) {

                tblMachineHistoryVo = new TblMachineHistoryVo();
                isOver = false;

                tblMachineHistoryVo.setMachineUuid(machineUuid);
                tblMachineHistoryVo.setFirstEventNo(sigmaLogVo.getEventNo());
                tblMachineHistoryVo.setStartDate(sigmaLogVo.getCreateDate());

                if (StringUtils.isEmpty(sigmaLogVo.getAutoMode())) {

                    tblMachineHistoryVo.setStatus("OFF");
                } else {

                    tblMachineHistoryVo.setStatus(sigmaLogVo.getAutoMode());
                }

            }

            int shotCnt = 0;

            if ("ON".equals(sigmaLogVo.getCycleStart())) {

                shotCnt += 1;
            }

            tblMachineHistoryVo.setLastEventNo(sigmaLogVo.getEventNo());
            tblMachineHistoryVo.setLastEventDate(sigmaLogVo.getCreateDate());

            int next = i + 1;

            if (next < tblSigmaLogVos.size()) {

                TblSigmaLogVo nextSigmaLogVo = tblSigmaLogVos.get(next);

                if (null != nextSigmaLogVo.getAutoMode() && !"".equals(nextSigmaLogVo.getAutoMode()) && !tblMachineHistoryVo.getStatus().equals(nextSigmaLogVo.getAutoMode())) {

                    tblMachineHistoryVo.setEndDate(nextSigmaLogVo.getCreateDate());
                    tblMachineHistoryVo.setShotCnt(String.valueOf(shotCnt));

                    tblMachineHistoryVos.add(tblMachineHistoryVo);
                    isOver = true;
                }
            } else {

                tblMachineHistoryVo.setShotCnt(String.valueOf(shotCnt));
                tblMachineHistoryVos.add(tblMachineHistoryVo);
            }

        }

        return tblMachineHistoryVos;

    }
    
    /**
     * ある設備の指定時間内におけるショット数を取得する
     * @param machineId
     * @param startDateTime
     * @param endDateTime
     * @return 
     */
    @GET
    @Path("shotcount")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getShotCount(@QueryParam("machineId") String machineId,
            @QueryParam("machineUuid") String machineUuid,
            @QueryParam("startDateTime") String startDateTime,
            @QueryParam("endDateTime") String endDateTime
    ) {
        Gson gson = new Gson();
        Map<String, Object> response = new HashMap<>();
        long shotCount = tblMachineHistoryService.getShotCount(machineUuid, machineId, startDateTime, endDateTime);
        response.put("shotCount", shotCount);
        return gson.toJson(response);
    }
    
    /**
     * T1101_設備稼働状況照会 詳細作業/生産実績一覧データ取得
     *
     * @param workStartDateTime
     * @param nextDayWorkStartDateTime
     * @param machineId
     * @return 設備稼働状況照会詳細
     *
     */
    @GET
    @Path("detail/workporduction")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TblMachineHistoryList getMachineHistoryWorkProduction(@QueryParam("machineId") String machineId,
        @QueryParam("workStartDateTime") String workStartDateTime,
        @QueryParam("nextDayWorkStartDateTime") String nextDayWorkStartDateTime) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMachineHistoryList response = new TblMachineHistoryList();
        SimpleDateFormat sdf1 = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);
        java.util.Date formatWorkStartDateTime = null;
        java.util.Date formatNextDayWorkStartDateTime = null;
        try {
            if (workStartDateTime != null && !"".equals(workStartDateTime)) {
                formatWorkStartDateTime = sdf1.parse(workStartDateTime);
            }
            if (nextDayWorkStartDateTime != null && !"".equals(nextDayWorkStartDateTime)) {
                formatNextDayWorkStartDateTime = sdf1.parse(nextDayWorkStartDateTime);
            }
        } catch (ParseException ex) {     
           Logger.getLogger(TblWorkResource.class.getName()).log(Level.WARNING, null, "日付形式不正 workStartDateTime[" + workStartDateTime + "], nextDayWorkStartDateTime[" + nextDayWorkStartDateTime + "]"); 
        }
        
        // 作業実績取得
        TblWorkList tblWorks = tblWorkService.getWorkByCondition(
            null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,machineId
           ,formatWorkStartDateTime
           ,formatNextDayWorkStartDateTime
        );
        
        // 生産実績取得
        TblProductionDetailList tblProductionDetailList = tblProductionDetailService.getProductionDetailsByCondition(
            null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,null
           ,machineId
           ,formatWorkStartDateTime
           ,formatNextDayWorkStartDateTime
        );
        
        // 返却データ設定
        tblMachineHistoryService.setMachineHistoryDetailVo(response, tblWorks, tblProductionDetailList, loginUser.getLangId());
        
        return response;
    }
    
    /**
     * 作業実績/生産実績CSV出力
     * 
     * @param machineId
     * @param workStartDateTime
     * @param nextDayWorkStartDateTime
     * @return 
     */
    @GET
    @Path("exportcsv/workporduction")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getWorkporductionCSV(
            @QueryParam("machineId") String machineId // 設備ID
           ,@QueryParam("workStartDateTime") String workStartDateTime // 選択日付の業務開始時刻
           ,@QueryParam("nextDayWorkStartDateTime") String nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
    ) {
        LoginUser loginUser = (LoginUser)requestContext.getProperty(RequestParameters.LOGINUSER);
        
        TblMachineHistoryList tblMachineHistoryList = getMachineHistoryWorkProduction(machineId, workStartDateTime, nextDayWorkStartDateTime);
        
        /**
         * データが取得できなかった場合はエラー返却
         */
        FileReponse response = new FileReponse();
        /*
         * 作成したCSV出力テーブルのIDを設定したファイルレスポンスを返却
         */
        response = tblMachineHistoryService.getWorkproductionCSV(tblMachineHistoryList, loginUser.getLangId(), loginUser.getUserUuid());
        
        return response;
    }
}
