/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.batch.externalmold.choice.ExtMstChoiceService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.direction.TblDirection;
import com.kmcj.karte.resources.direction.TblDirectionService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.machine.dailyreport2.MachineDailyReport2Service;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.MstMoldService;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetail;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodeling;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetail;
import com.kmcj.karte.resources.work.close.entry.TblWorkCloseEntry;
import com.kmcj.karte.resources.work.close.entry.TblWorkCloseEntryService;
import com.kmcj.karte.resources.work.phase.MstWorkPhase;
import com.kmcj.karte.resources.work.phase.MstWorkPhaseService;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import com.kmcj.karte.util.TimezoneConverter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;



/**
 * 作業実績テーブルサービス
 * @author t.ariki
 */
@Dependent
public class TblWorkService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private TblCsvExportService tblCsvExportService;
    
    @Inject
    private TblWorkCloseEntryService tblWorkCloseEntryService;
    
    @Inject
    private MstWorkPhaseService mstWorkPhaseService;
    
    @Inject
    private MstMoldService mstMoldService;
    
    @Inject
    private MstMachineService mstMachineService;
    
    @Inject
    private TblDirectionService tblDirectionService;
    
    @Inject
    private MstComponentService mstComponentService;
    
    @Inject
    private MachineDailyReport2Service machineDailyReport2Service;
    
    @Inject
    private MstChoiceService mstChoiceService;
    
    @Inject
    private ExtMstChoiceService extMstChoiceService;
    
    private Logger logger = Logger.getLogger(TblWorkService.class.getName());
    
    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("mstUser.userId", " ORDER BY mu.userId ");// 作業者ＩＤ
        orderKey.put("mstUser.userName", " ORDER BY mu.userName ");// 作業者氏名
        orderKey.put("mstUser.department", " ORDER BY mu.department ");// 部署
        orderKey.put("workingDate", " ORDER BY tblWork.workingDate ");// 作業日
        orderKey.put("startDatetime", " ORDER BY tblWork.startDatetime ");// 開始時刻
        orderKey.put("endDatetime", " ORDER BY tblWork.endDatetime ");// 終了時刻
        orderKey.put("directionCode", " ORDER BY td.directionCode ");// 手配・工事番号
        orderKey.put("mstWorkPhase.workPhaseCode", " ORDER BY mwp.workPhaseCode ");// 工程コード
        orderKey.put("workPhaseName", " ORDER BY mwp.workPhaseName ");// 工程
        orderKey.put("workCategoryName", " ORDER BY tblWork.workCategory ");// 作業内容
        orderKey.put("workingTimeMinutes", " ORDER BY tblWork.workingTimeMinutes ");// 作業時間
        orderKey.put("actualTimeMinutes", " ORDER BY tblWork.actualTimeMinutes ");// 実稼動時間
        orderKey.put("breakTimeMinutes", " ORDER BY tblWork.breakTimeMinutes ");// 休憩時間
        orderKey.put("moldId", " ORDER BY mold.moldId ");// 金型ＩＤ
        orderKey.put("moldName", " ORDER BY mold.moldName ");// 金型名称
        orderKey.put("componentCode", " ORDER BY mc.componentCode ");// 部品コード
        orderKey.put("componentName", " ORDER BY mc.componentName ");// 部品名称
        orderKey.put("mstMachine.machineId", " ORDER BY machine.machineId ");// 設備ＩＤ
        orderKey.put("mstMachine.machineName", " ORDER BY machine.machineName ");// 設備名称
        orderKey.put("procCd", " ORDER BY tblWork.procCd ");// 工程CD
        orderKey.put("mstUser.procCd", " ORDER BY mu.procCd ");// 所属工程CD
        orderKey.put("locked", " ORDER BY tblWork.locked ");// ロック

    }
    
    /**
     * IDによる存在チェック
     * @param id
     * @return
     */
    public boolean isExistsByPK(String id) {
        // 作業実績テーブルチェック
        Query query = entityManager.createNamedQuery("TblWork.findByWorkPhaseId");
        query.setParameter("workPhaseId", id);
        return query.getResultList().size() > 0;
    }
    
    /**
     * 作業実績テーブルリスト取得(作業者UUID指定)
     * @param userUuid
     * @return 
     */
    public TblWorkList getWorksByLoginUserId(String userUuid, boolean notFinished) {
        // 作業実績テーブルデータ取得
        //Query query = entityManager.createNamedQuery("TblWork.findByPersonUuidOrderByStartDatetime");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT tblWork FROM TblWork tblWork ");
        sql.append(" LEFT JOIN FETCH tblWork.mstUser ");
        sql.append(" LEFT JOIN FETCH tblWork.mstWorkPhase ");
        sql.append(" LEFT JOIN FETCH tblWork.tblDirection ");
        sql.append(" LEFT JOIN FETCH tblWork.mstComponent ");
        sql.append(" LEFT JOIN FETCH tblWork.mstMold ");
        sql.append("WHERE 1=1 ");
        sql.append(" AND tblWork.personUuid = :personUuid ");
        if (notFinished) {
            sql.append( "AND tblWork.endDatetime IS NULL ");
        }
        sql.append("ORDER BY tblWork.startDatetime ASC ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("personUuid", userUuid);
        
        logger.log(Level.FINE, "----------------------- query start -----------------");
        List list = query.getResultList();
        logger.log(Level.FINE, "----------------------- query end -----------------");

        TblWorkList response = new TblWorkList();
        response.setTblWorks(list);
        return response;
    }
    /**
     * 作業実績テーブル条件指定検索 件数取得
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
    public CountResponse getWorksCountByCondition(
            String userId     // ユーザーID
           ,java.util.Date workingDateFrom // 作業日From yyyy/mm/dd
           ,java.util.Date workingDateTo // 作業日To yyyy/mm/dd
           ,String userName // 作業者氏名
           ,Integer department // 部署
           ,String procCd // 所属工程コード
           ,Integer locked // ロック
           ,String machineId //設備id
           ,java.util.Date workStartDateTime // 選択日付の業務開始時刻
           ,java.util.Date nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
    ) {
        List list = getWorkByCondition(userId, workingDateFrom, workingDateTo
                , userName, department, procCd, locked, machineId,  workStartDateTime,nextDayWorkStartDateTime, "count");
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }
    
    /**
     * 作業実績テーブル条件指定検索(実レコード取得用ラッパー）
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
    public TblWorkList getWorkByCondition(
            String userId     // ユーザーID
           ,java.util.Date workingDateFrom // 作業日From yyyy/mm/dd
           ,java.util.Date workingDateTo // 作業日To yyyy/mm/dd
           ,String userName // 作業者氏名
           ,Integer department // 部署
           ,String procCd // 所属工程コード
           ,Integer locked // ロック
           ,String machineId
           ,java.util.Date workStartDateTime // 選択日付の業務開始時刻
           ,java.util.Date nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
    ) {
        List list = getWorkByCondition(userId, workingDateFrom, workingDateTo
                , userName, department, procCd, locked, machineId,  workStartDateTime, nextDayWorkStartDateTime, "");
        
        List<TblWork> tblWors = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TblWork iTblWork = (TblWork)list.get(i);
            iTblWork.setUserName(iTblWork.getUserName());
            tblWors.add(iTblWork);
        }
        TblWorkList tblWorkList = new TblWorkList();
        tblWorkList.setTblWorks(tblWors);
        return tblWorkList;
    }
    /**
     * 作業実績テーブル条件指定検索(実検索処理)
     * @param userId
     * @param workingDateFrom
     * @param workingDateTo
     * @param userName
     * @param department
     * @param procCd
     * @param locked
     * @param flag
     * @param machineId
     * @param nextDayWorkStartDateTime
     * @param workStartDateTime
     * @return 
     */
    public List getWorkByCondition(
            String userId     // ユーザーID
           ,java.util.Date workingDateFrom // 作業日From yyyy/mm/dd
           ,java.util.Date workingDateTo // 作業日To yyyy/mm/dd
           ,String userName // 作業者氏名
           ,Integer department // 部署
           ,String procCd // 所属工程コード
           ,Integer locked // ロック
           ,String machineId //設備id
           ,java.util.Date workStartDateTime // 選択日付の業務開始時刻
           ,java.util.Date nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
           ,String flag     // カウント取得時のフラグ

    ) {
        // 作業実績テーブルデータ取得
        StringBuilder sql = new StringBuilder();
        if("count".equals(flag)){
            sql.append("SELECT count(tblWork) FROM TblWork tblWork LEFT JOIN FETCH tblWork.mstUser WHERE 1=1 ");
        } else {
            //sql.append("SELECT tblWork FROM TblWork tblWork LEFT JOIN FETCH tblWork.mstUser WHERE 1=1 ");
            sql.append("SELECT tblWork FROM TblWork tblWork ");
            sql.append(" LEFT JOIN FETCH tblWork.mstUser ");
            sql.append(" LEFT JOIN FETCH tblWork.mstWorkPhase ");
            sql.append(" LEFT JOIN FETCH tblWork.tblDirection ");
            sql.append(" LEFT JOIN FETCH tblWork.mstComponent ");
            sql.append(" LEFT JOIN FETCH tblWork.mstMold ");
            sql.append(" LEFT JOIN FETCH tblWork.mstMachine ");
            sql.append("WHERE 1=1 ");
        }
        
        if (userId != null && !"".equals(userId)) {
            sql = sql.append(" and tblWork.mstUser.userId = :userId ");
        }
        if (workingDateFrom != null) {
            sql = sql.append(" and tblWork.workingDate >= :workingDateFrom ");
        }
        if (workingDateTo != null) {
            sql = sql.append(" and tblWork.workingDate <= :workingDateTo ");
        }
        if (userName != null && !"".equals(userName)) {
            sql = sql.append(" and tblWork.mstUser.userName like :userName ");
        }
        if (department != null && department != 0) {
            try {
                Integer.toString(department);
                sql = sql.append(" and tblWork.mstUser.department = :department ");
            } catch (NumberFormatException numberFormatException) {
                ;
            }
        }
        if (procCd != null && !"".equals(procCd)) {
            sql = sql.append(" and tblWork.mstUser.procCd = :procCd ");
        }
        if (locked != null && locked != 0) {
            sql = sql.append(" and tblWork.locked = :locked ");
        }
        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" and tblWork.mstMachine.machineId = :machineId ");
        }
        if (nextDayWorkStartDateTime != null) {
            // 開始日時<=選択日付の翌日の業務開始時刻
            sql = sql.append(" and (tblWork.startDatetime <= :startDatetime ");
        }
        if (workStartDateTime != null) {
             // 終了日時>=選択日付の業務開始時刻
            sql = sql.append(" and tblWork.endDatetime >= :endDatetime ");
        }
        if (nextDayWorkStartDateTime != null) {
            // 開始日時<=選択日付の翌日の業務開始時刻且つ終了日時 IS NULL
            sql = sql.append(" or (tblWork.endDatetime is null and tblWork.startDatetime <= :startDatetime)) ");
        }
        
        //　作業日、開始日時 昇順
        sql = sql.append(" ORDER BY tblWork.workingDate ASC, tblWork.startDatetime ASC ");
        
        Query query = entityManager.createQuery(sql.toString());
        
        if (userId != null && !"".equals(userId)) {
            query.setParameter("userId", userId.trim());
        }
        if (workingDateFrom != null) {
            query.setParameter("workingDateFrom", workingDateFrom);
        }
        if (workingDateTo != null) {
            query.setParameter("workingDateTo", workingDateTo);
        }
        if (userName != null && !"".equals(userName)) {
            query.setParameter("userName", "%" + userName + "%");
        }
        if (department != null && department != 0) {
            try {
                String bindDepartment = Integer.toString(department);
                query.setParameter("department", bindDepartment);
            } catch (NumberFormatException numberFormatException) {
                ;
            }
        }
        if (procCd != null && !"".equals(procCd)) {
            query.setParameter("procCd", procCd.trim());
        }
        if (locked != null && locked != 0) {
            query.setParameter("locked", locked);
        }
        
        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", machineId.trim());
        }
        
        if (nextDayWorkStartDateTime != null) {
            query.setParameter("startDatetime", nextDayWorkStartDateTime);
        }
        
        if (workStartDateTime != null) {
            query.setParameter("endDatetime", workStartDateTime);
        }

        logger.log(Level.FINE, "----------------------- query start -----------------");
        List list = query.getResultList();
        logger.log(Level.FINE, "----------------------- query end -----------------");
        return list;
    }
    
    /**
     * 作業実績テーブル作業日指定検索
     * @param workingDate
     * @param loginUser
     * @return 
     */
    public TblWorkList getWorkByWorkingDate(java.util.Date workingDate, LoginUser loginUser) {
        // 作業実績テーブルデータ取得
        //sql = new StringBuilder("SELECT t FROM TblWork t LEFT JOIN FETCH MstUser m WHERE 1=1 ");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT tblWork FROM TblWork tblWork ");
        sql.append(" LEFT JOIN FETCH tblWork.mstUser ");
        sql.append(" LEFT JOIN FETCH tblWork.mstWorkPhase ");
        sql.append(" LEFT JOIN FETCH tblWork.tblDirection ");
        sql.append(" LEFT JOIN FETCH tblWork.mstComponent ");
        sql.append(" LEFT JOIN FETCH tblWork.mstMold ");
        sql.append("WHERE 1=1 ");
        if (workingDate != null) {
            sql.append(" and tblWork.workingDate = :workingDate ");
        }
        //sql = sql.append(" and t.personUuid = m.uuid "); // ユーザマスタとの結合条件
        sql.append(" and tblWork.personUuid = :personUuid ");
        //　作業日、開始日時 昇順
        sql = sql.append(" ORDER BY tblWork.workingDate ASC, tblWork.startDatetime ASC ");
        Query query = entityManager.createQuery(sql.toString());
        
        if (workingDate != null) {
            query.setParameter("workingDate", workingDate);
        }
        query.setParameter("personUuid", loginUser.getUserUuid());
        

        logger.log(Level.FINE, "----------------------- query start -----------------");
        List list = query.getResultList();
        logger.log(Level.FINE, "----------------------- query end -----------------");
        
        List<TblWork> tblWors = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TblWork iTblWork = (TblWork)list.get(i);
            iTblWork.setUserName(iTblWork.getUserName());
            tblWors.add(iTblWork);
        }
        TblWorkList tblWorkList = new TblWorkList();
        tblWorkList.setTblWorks(tblWors);
        return tblWorkList;
    }

    /**
     * 作業実績テーブル締め対象データ取得
     * @param closedDate
     * @return 
     */
    public TblWorkList getCloseTarget(java.util.Date closedDate) {
        // 作業実績テーブデータ取得
        StringBuilder sql;
        sql = new StringBuilder("SELECT t FROM TblWork t WHERE 1=1 ");
        sql = sql.append(" and t.workingDate <= :workingDate ");
        sql = sql.append(" and t.locked = :locked ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("workingDate", closedDate);
        query.setParameter("locked", 0);
        
        List list = query.getResultList();
        TblWorkList response = new TblWorkList();
        response.setTblWorks(list);
        return response;
    }
    
    /**
     * 作業実績テーブル登録データ設定
     * @param response
     * @param tblWork
     * @param loginUser 
     */
    public void setCreateData(BasicResponse response, TblWork tblWork, LoginUser loginUser) {
        // 必須チェック
        // 作業日
        if (tblWork.getWorkingDate() == null) {
            setApplicationError(response, loginUser, "msg_error_not_null", "workingDate");
            return;
        }
        // 作業工程SEQ
        else if (tblWork.getWorkPhaseChoiceSeq() == null) {
            setApplicationError(response, loginUser, "msg_error_not_null", "workPhaseChoiceSeq");
            return;
        }
        tblWork.setWorkingDate(DateUtils.truncate(tblWork.getWorkingDate(), Calendar.DAY_OF_MONTH));
        // 締め日チェック
        TblWorkCloseEntry tblWorkCloseEntry = tblWorkCloseEntryService.getLatest();
        if (tblWorkCloseEntry != null) {
            if (tblWork.getWorkingDate().compareTo(tblWorkCloseEntry.getClosedDate()) <= 0) {
                setApplicationError(response, loginUser, "msg_error_work_entry_closed", "workingDate");
                return;
            }
        } else {
            Logger.getLogger(TblWorkResource.class.getName()).log(Level.FINE, "作業入力締めテーブル レコード無しのため締め日チェックOK");
        }
        
        /*
         * マスタ値チェックおよび値設定
         */
        // 作業工程マスタ存在チェック(作業工程マスタ.選択肢連番による存在チェック)および値セット
        MstWorkPhase mstWorkPhase =  mstWorkPhaseService.getMstWorkPhaseByChoiceSeq(tblWork.getWorkPhaseChoiceSeq());
        if (mstWorkPhase == null) {
            setApplicationError(response, loginUser, "mst_error_record_not_found", "作業工程マスタ存在チェックエラー");
            return;
        } else {
            tblWork.setWorkPhaseId(mstWorkPhase.getId());
        }
        
        // 手配・工事番号が入力されている場合は存在チェック
        if (tblWork.getDirectionCode() != null && !"".equals(tblWork.getDirectionCode())) {
            // 手配テーブル存在チェック(手配テーブル.手配・工事番号による存在チェック)および値セット
            TblDirection tblDirection = tblDirectionService.getTblDirectionByDirectionCode(tblWork.getDirectionCode());
            if (tblDirection == null) {
                setApplicationError(response, loginUser, "mst_error_record_not_found", "手配テーブル存在チェックエラー");
                return;
            } else {
                // 手配テーブル.IDを設定
                tblWork.setDirectionId(tblDirection.getId());
            }
        }

        // 部品コードが入力されている場合は存在チェック
        if (tblWork.getComponentCode() != null && !"".equals(tblWork.getComponentCode())) {
            // 部品マスタ存在チェック(部品マスタ.部品コードによる存在チェック)および値セット
            MstComponent mstComponent = mstComponentService.getMstComponent(tblWork.getComponentCode());
            if (mstComponent == null) {
                setApplicationError(response, loginUser, "mst_error_record_not_found", "部品マスタ存在チェックエラー");
                return;
            } else {
                tblWork.setComponentId(mstComponent.getId());
            }
        }
        
        // 金型IDが入力されている場合は存在チェック
        if (tblWork.getMoldId() != null && !"".equals(tblWork.getMoldId())) {
            // 金型マスタ存在チェック(金型マスタ.金型IDによる存在チェック)および値セット
            MstMold mstMold = mstMoldService.getMstMoldByMoldId(tblWork.getMoldId());
            if (mstMold == null) {
                setApplicationError(response, loginUser, "mst_error_record_not_found", "金型マスタ存在チェックエラー");
                return;
            } else {
                // 金型マスタ.UUIDを設定
                tblWork.setMoldUuid(mstMold.getUuid());
            }
        }
        // 設備UUID machineUuid
        if (tblWork.getMachineUuid() != null && !"".equals(tblWork.getMachineUuid())) {
            // 設備マスタ存在チェック(設備マスタ.UUIDによる存在チェック)および値セット
            MstMachine mstMachine = mstMachineService.getMstMachineByUuid(tblWork.getMachineUuid());
            if (mstMachine != null) {
                tblWork.setMachineUuid(mstMachine.getUuid());
            }
            else {
                setApplicationError(response, loginUser, "mst_error_record_not_found", "金型マスタ存在チェックエラー");
            }
        }
        else {
            tblWork.setMachineUuid(null); //空白文字対策
        }
        
        /*
         * 工番ID
         *  生産終了入力時に画面で選択された部品・工番に該当する工番IDがパラメータに設定されてくる
         */
        tblWork.setProcedureId(tblWork.getProcedureId());
        
        /*
         * 登録必須項目変換および設定
         */
        // 作業者UUIDをログインユーザーより取得し設定
        tblWork.setPersonUuid(loginUser.getUserUuid());
        
        /*
         * 開始日時
         */
        FileUtil fu = null;
        if (StringUtils.isNotEmpty(tblWork.getStartDatetimeStr())) {
            fu = new FileUtil();
            Date startDatetime = fu.strDateTimeFormatToDate(tblWork.getStartDatetimeStr());
            tblWork.setStartDatetime(startDatetime);
        }
        // 値がない場合は強制設定
        if (tblWork.getStartDatetime() == null) {
            // ログインユーザーのタイムゾーン
            tblWork.setStartDatetime(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
            // サーバーのタイムゾーン
            tblWork.setStartDatetimeStz(new java.util.Date());
        }
        // 値がある場合はSTZの方を入力値でサーバ時間に変換して設定し直し
        else {
            tblWork.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblWork.getStartDatetime()));
        }
        /*
         * 終了日時
         */
        // 値がない場合は強制設定
        if (tblWork.getEndDatetime() == null) {
            // ログインユーザーのタイムゾーン
            //tblWork.setEndDatetime(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
            // サーバーのタイムゾーン
            //tblWork.setEndDatetimeStz(new java.util.Date());
        }
        // 値がある場合はSTZの方を入力値でサーバ時間に変換して設定し直し
        else {
            tblWork.setEndDatetime(tblWork.getEndDatetime());
            tblWork.setEndDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblWork.getEndDatetime()));
        }
    }
    
    /**
     * 作業実績テーブル更新データ設定
     * @param response
     * @param tblWork
     * @param loginUser
     * @return 
     */
    public TblWork setUpdateData(BasicResponse response, TblWork tblWork, LoginUser loginUser) {
        /**
         * 作業実績テーブル自体の存在およびロック状態チェック
         */
        TblWorkList updateTblWorks = getWorkById(tblWork.getId());
        // 作業実績テーブル自体の存在チェック
        if (updateTblWorks.getTblWorks().isEmpty()) {
            setApplicationError(response, loginUser, "msg_error_data_deleted", "TblWorks");
            return tblWork;
        }
        // ロック状態
        else if (updateTblWorks.getTblWorks().get(0).getLocked() != 0) {
            setApplicationError(response, loginUser, "msg_error_locked", "locked");
            return tblWork;
        }
        
        /*
         * 更新(merge)対象のオブジェクト設定
         */
        TblWork updateTblWork = updateTblWorks.getTblWorks().get(0);
        
        // 更新制御フラグを立てておく
        updateTblWork.setModified(true);
        
        /*
         * 必須チェック
         */
        // 作業日
        if (tblWork.getWorkingDate() == null) {
            setApplicationError(response, loginUser, "msg_error_not_null", "workingDate");
            return tblWork;
        }
        updateTblWork.setWorkingDate(tblWork.getWorkingDate());
        
        // 作業時間(分)
        if (tblWork.getWorkingTimeMinutes() == null) {
            setApplicationError(response, loginUser, "msg_error_not_null", "workingTimeMinutes");
            return tblWork;
        }
        updateTblWork.setWorkingTimeMinutes(tblWork.getWorkingTimeMinutes());
        
        // 実稼働時間（分）
        if (tblWork.getActualTimeMinutes() == null) {
            setApplicationError(response, loginUser, "msg_error_not_null", "actualTimeMinutes");
            return tblWork;
        }
        updateTblWork.setActualTimeMinutes(tblWork.getActualTimeMinutes());

        // 休憩時間（分）
        if (tblWork.getBreakTimeMinutes() == null) {
            setApplicationError(response, loginUser, "msg_error_not_null", "breakTimeMinutes");
            return tblWork;
        }
        updateTblWork.setBreakTimeMinutes(tblWork.getBreakTimeMinutes());
        
        // 作業工程SEQ
        if (tblWork.getWorkPhaseChoiceSeq() == null) {
            setApplicationError(response, loginUser, "msg_error_not_null", "workPhaseChoiceSeq");
            return tblWork;
        }
        updateTblWork.setWorkPhaseChoiceSeq(tblWork.getWorkPhaseChoiceSeq());
        
        // 締め日チェック
        // 更新時は、締日に関わらずロック状態のみで判断するためコメントアウト
        /*
        TblWorkCloseEntry tblWorkCloseEntry = tblWorkCloseEntryService.getLatest();
        if (tblWorkCloseEntry != null) {
            if (tblWork.getWorkingDate().compareTo(tblWorkCloseEntry.getClosedDate()) <= 0) {
                setApplicationError(response, loginUser, "msg_error_work_entry_closed", "workingDate");
                return tblWork;
            }
        } else {
            Logger.getLogger(TblWorkResource.class.getName()).log(Level.FINE, "作業入力締めテーブル レコード無しのため締め日チェックOK");
        }
        */
        /*
         * マスタ値チェックおよび値設定
         */
        // 作業工程マスタ存在チェック(作業工程マスタ.選択肢連番による存在チェック)および値セット
        MstWorkPhase mstWorkPhase =  mstWorkPhaseService.getMstWorkPhaseByChoiceSeq(tblWork.getWorkPhaseChoiceSeq());
        if (mstWorkPhase == null) {
            setApplicationError(response, loginUser, "mst_error_record_not_found", "作業工程マスタ存在チェックエラー");
            return tblWork;
        } else {
            updateTblWork.setWorkPhaseId(mstWorkPhase.getId());
        }
        
        // 設備UUID machineUuid
        if (tblWork.getMachineUuid() != null && !"".equals(tblWork.getMachineUuid())) {
            // 金型マスタ存在チェック(金型マスタ.金型IDによる存在チェック)および値セット
            MstMachine mstMachine = mstMachineService.getMstMachineByUuid(tblWork.getMachineUuid());
            if (mstMachine != null) {
                updateTblWork.setMachineUuid(mstMachine.getUuid());
            }
        }
        else {
            updateTblWork.setMachineUuid(null);
        }
        
        // 手配・工事番号が入力されている場合は存在チェック
        if (tblWork.getDirectionCode() != null && !"".equals(tblWork.getDirectionCode())) {
            // 手配テーブル存在チェック(手配テーブル.手配・工事番号による存在チェック)および値セット
            TblDirection tblDirection = tblDirectionService.getTblDirectionByDirectionCode(tblWork.getDirectionCode());
            if (tblDirection == null) {
                setApplicationError(response, loginUser, "mst_error_record_not_found", "手配テーブル存在チェックエラー");
                return tblWork;
            } else {
                // 手配テーブル.IDを設定
                updateTblWork.setDirectionId(tblDirection.getId());
            }
        } else {
            updateTblWork.setDirectionId(null);
        }

        // 部品コードが入力されている場合は存在チェック
        if (tblWork.getComponentCode() != null && !"".equals(tblWork.getComponentCode())) {
            // 部品マスタ存在チェック(部品マスタ.部品コードによる存在チェック)および値セット
            MstComponent mstComponent = mstComponentService.getMstComponent(tblWork.getComponentCode());
            if (mstComponent == null) {
                setApplicationError(response, loginUser, "mst_error_record_not_found", "部品マスタ存在チェックエラー");
                return tblWork;
            } else {
                updateTblWork.setComponentId(mstComponent.getId());
            }
        } else {
            updateTblWork.setComponentId(null);
        }

        // 金型IDが入力されている場合は存在チェック
        if (tblWork.getMoldId() != null && !"".equals(tblWork.getMoldId())) {
            // 金型マスタ存在チェック(金型マスタ.金型IDによる存在チェック)および値セット
            MstMold mstMold = mstMoldService.getMstMoldByMoldId(tblWork.getMoldId());
            if (mstMold == null) {
                setApplicationError(response, loginUser, "mst_error_record_not_found", "金型マスタ存在チェックエラー");
                return tblWork;
            } else {
                // 金型マスタ.UUIDを設定
                updateTblWork.setMoldUuid(mstMold.getUuid());
            }
        } else {
            updateTblWork.setMoldUuid(null);
        }
        
        /*
         * 任意項目設定
         */
        // 工番ID
        if (tblWork.getProcedureId() != null) {
            updateTblWork.setProcedureId(tblWork.getProcedureId());
        }
        // 作業内容
        if (tblWork.getWorkCategory() != null) {
            updateTblWork.setWorkCategory(tblWork.getWorkCategory());
        }        
        // 所属工程コード
        if (tblWork.getProcCd()!= null) {
            updateTblWork.setProcCd(tblWork.getProcCd());
        }
        // 作業コード
        if (tblWork.getWorkCode()!= null) {
            updateTblWork.setWorkCode(tblWork.getWorkCode());
        }
        
        /*
         * 開始日時
         */
        FileUtil fu = null;
        if (StringUtils.isNotEmpty(tblWork.getStartDatetimeStr())) {
            fu = new FileUtil();
            Date startDatetime = fu.strDateTimeFormatToDate(tblWork.getStartDatetimeStr());
            tblWork.setStartDatetime(startDatetime);
        }
        // 値がない場合は強制設定
        if (tblWork.getStartDatetime() == null) {
            // ログインユーザーのタイムゾーン
            updateTblWork.setStartDatetime(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
            // サーバーのタイムゾーン
            updateTblWork.setStartDatetimeStz(new java.util.Date());
        }
        // 値がある場合はSTZの方を入力値でサーバ時間に変換して設定し直し
        else {
            updateTblWork.setStartDatetime(tblWork.getStartDatetime());
            updateTblWork.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblWork.getStartDatetime()));
        }
        
        /*
         * 終了日時
         */
        if (StringUtils.isNotEmpty(tblWork.getEndDatetimeStr())) {
            fu = new FileUtil();
            Date endDatetime = fu.strDateTimeFormatToDate(tblWork.getEndDatetimeStr());
            tblWork.setEndDatetime(endDatetime);
        }
        // 値がない場合は強制設定
        if (tblWork.getEndDatetime() == null) {
            // ログインユーザーのタイムゾーン
            updateTblWork.setEndDatetime(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
            // サーバーのタイムゾーン
            updateTblWork.setEndDatetimeStz(new java.util.Date());
        }
        // 値がある場合はSTZの方を入力値でサーバ時間に変換して設定し直し
        else {
            updateTblWork.setEndDatetime(tblWork.getEndDatetime());
            updateTblWork.setEndDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblWork.getEndDatetime()));
        }
        return updateTblWork;
    }
    
    /**
     * 作業実績テーブルロック更新用データ設定
     * @param response
     * @param tblWork
     * @param loginUser
     * @return 
     */
    public TblWork setUpdateForLock(BasicResponse response, TblWork tblWork, LoginUser loginUser) {
        /**
         * 作業実績テーブル自体の存在チェック
         */
        TblWorkList updateTblWorks = getWorkById(tblWork.getId());
        // 作業実績テーブル自体の存在チェック
        if (updateTblWorks.getTblWorks().isEmpty()) {
            setApplicationError(response, loginUser, "msg_error_data_deleted", "TblWorks");
            return tblWork;
        }
        
        /*
         * 更新(merge)対象のオブジェクト設定
         */
        TblWork updateTblWork = updateTblWorks.getTblWorks().get(0);
        
        // 更新制御フラグを立てておく
        updateTblWork.setModified(true);
        
        // ロックの更新値設定
        updateTblWork.setLocked(tblWork.getLocked());
        return updateTblWork;
    }
    
    /**
     * 作業実績テーブル削除データ設定
     * @param response
     * @param id
     * @param loginUser 
     */
    public void setDeleteData(BasicResponse response, String id, LoginUser loginUser) {
        /**
         * 作業実績テーブル自体の存在およびロック状態チェック
         */
        TblWorkList updateTblWorks = getWorkById(id);        
        // 作業実績テーブル自体の存在チェック
        if (updateTblWorks.getTblWorks().isEmpty()) {
            setApplicationError(response, loginUser, "msg_error_data_deleted", "TblWorks");
        }
        // ロック状態
        else if (updateTblWorks.getTblWorks().get(0).getLocked() != 0) {
            setApplicationError(response, loginUser, "msg_error_locked", "locked");
        }
        
        // 締め日チェック
        // 削除時は締日に関わらずロック状態のみでチェックを行うためコメントアウト
        /*
        TblWorkCloseEntry tblWorkCloseEntry = tblWorkCloseEntryService.getLatest();
        if (tblWorkCloseEntry != null) {
            if (updateTblWorks.getTblWorks().get(0).getWorkingDate().compareTo(tblWorkCloseEntry.getClosedDate()) <= 0) {
                setApplicationError(response, loginUser, "msg_error_work_entry_closed", "workingDate");
            }
        } else {
            Logger.getLogger(TblWorkResource.class.getName()).log(Level.FINE, "作業入力締めテーブル レコード無しのため締め日チェックOK");
        }
        */
    }
    
    /**
     * 作業実績テーブル1件取得(ID指定)
     * @param id
     * @return 
     */
    public TblWorkList getWorkById(String id) {
        // 作業実績テーブデータ取得
        //Query query = entityManager.createNamedQuery("TblWork.findById");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT tblWork FROM TblWork tblWork ");
        sql.append(" LEFT JOIN FETCH tblWork.mstUser ");
        sql.append(" LEFT JOIN FETCH tblWork.mstWorkPhase ");
        sql.append(" LEFT JOIN FETCH tblWork.tblDirection ");
        sql.append(" LEFT JOIN FETCH tblWork.mstComponent ");
        sql.append(" LEFT JOIN FETCH tblWork.mstMold ");
        sql.append("WHERE 1=1 ");
        sql.append(" and tblWork.id = :id ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("id", id);
        
        List list = query.getResultList();
        
        logger.log(Level.FINE, "----------------------- query start -----------------");
        TblWorkList response = new TblWorkList();
        logger.log(Level.FINE, "----------------------- query end -----------------");
        
        response.setTblWorks(list);
        return response;
    }

    /**
     * 作業実績テーブルCSV出力
     * @param tblWorkList
     * @param loginUser
     * @return 
     */
    public FileReponse getWorksCSV(TblWorkList tblWorkList, LoginUser loginUser) {
        /*
         * Header用意
         */
        String langId = loginUser.getLangId();
        String workUserId = mstDictionaryService.getDictionaryValue(langId, "work_user_id");
        String workUserName = mstDictionaryService.getDictionaryValue(langId, "work_user_name");
        String workUserDepartment = mstDictionaryService.getDictionaryValue(langId, "work_user_department");
        String workingDate = mstDictionaryService.getDictionaryValue(langId, "working_date");
        String workStartTime = mstDictionaryService.getDictionaryValue(langId, "work_start_time");
        String workEndTime = mstDictionaryService.getDictionaryValue(langId, "work_end_time");
        String directionCode = mstDictionaryService.getDictionaryValue(langId, "direction_code");
        String workPhaseCode = mstDictionaryService.getDictionaryValue(langId, "work_phase_code");
        String workPhase = mstDictionaryService.getDictionaryValue(langId, "work_phase");
        String workCategory = mstDictionaryService.getDictionaryValue(langId, "work_category");
        String workingTimeMinutes = mstDictionaryService.getDictionaryValue(langId, "working_time_minutes");
        String workActualTimeMinutes = mstDictionaryService.getDictionaryValue(langId, "work_actual_time_minutes");
        String workBreakTimeMinutes = mstDictionaryService.getDictionaryValue(langId, "work_break_time_minutes");
        String moldId = mstDictionaryService.getDictionaryValue(langId, "mold_id");
        String moldName = mstDictionaryService.getDictionaryValue(langId, "mold_name");
        String componentCode = mstDictionaryService.getDictionaryValue(langId, "component_code");
        String componentName = mstDictionaryService.getDictionaryValue(langId, "component_name");
        String machineId = mstDictionaryService.getDictionaryValue(langId, "machine_id");
        String machineName = mstDictionaryService.getDictionaryValue(langId, "machine_name");
        String workProcCd = mstDictionaryService.getDictionaryValue(langId, "work_proc_cd");
        String procCd = mstDictionaryService.getDictionaryValue(langId, "proc_cd");
        String workCode = mstDictionaryService.getDictionaryValue(langId, "work_code");
        String workRecordLock = mstDictionaryService.getDictionaryValue(langId, "locked");
        
        FileReponse fr = new FileReponse();

        /*
         * Header設定
         */
        ArrayList csvOutHeadList = new ArrayList();
        csvOutHeadList.add(workUserId);
        csvOutHeadList.add(workUserName);
        csvOutHeadList.add(workUserDepartment);
        csvOutHeadList.add(workingDate);
        csvOutHeadList.add(workStartTime);
        csvOutHeadList.add(workEndTime);
        csvOutHeadList.add(directionCode);
        csvOutHeadList.add(workPhaseCode);
        csvOutHeadList.add(workPhase);
        csvOutHeadList.add(workCategory);
        csvOutHeadList.add(workingTimeMinutes);
        csvOutHeadList.add(workActualTimeMinutes);
        csvOutHeadList.add(workBreakTimeMinutes);
        csvOutHeadList.add(moldId);
        csvOutHeadList.add(moldName);
        csvOutHeadList.add(componentCode);
        csvOutHeadList.add(componentName);
        csvOutHeadList.add(machineId);
        csvOutHeadList.add(machineName);
        csvOutHeadList.add(workProcCd);
        csvOutHeadList.add(procCd);
        csvOutHeadList.add(workCode);
        csvOutHeadList.add(workRecordLock);
        
        // 出力データ準備
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(csvOutHeadList);
        
        // CSV明細用
        ArrayList<ArrayList> contents = new ArrayList<ArrayList>();
        // 行用
        ArrayList<String> line;
        
        // 日付変換用
        SimpleDateFormat sdfDate = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        SimpleDateFormat sdfHm = new SimpleDateFormat(DateFormat.DATETIME_FORMAT);// "HH:mm");
        
        List<TblWork> tblWorks = tblWorkList.getTblWorks();
        if (tblWorks != null && !tblWorks.isEmpty()) {
            for (TblWork tblWork : tblWorks) {
                line = new ArrayList<String>();
                // 作業者ID
                line.add(tblWork.getUserId());
                //line.add(tblWork.getPersonUuid());
                // 作業者氏名
                line.add(tblWork.getUserName());
                // 部署
                line.add(tblWork.getDepartmentName());
                // 作業日
                if (tblWork.getWorkingDate()!= null) {
                    String outputWorkingDate = sdfDate.format(tblWork.getWorkingDate());
                    line.add(outputWorkingDate);
                } else {
                    line.add("");
                }
                // 開始時刻
                if (tblWork.getStartDatetime() != null) {
                    String outputStartDatetime = sdfHm.format(tblWork.getStartDatetime()); 
                    line.add(outputStartDatetime);
                } else {
                    line.add("");
                }
                // 終了時刻
                if (tblWork.getEndDatetime() != null) {
                    String outputEndDatetime = sdfHm.format(tblWork.getEndDatetime()); 
                    line.add(outputEndDatetime);
                } else {
                    line.add("");
                }
                // 手配・工事番号
                line.add(tblWork.getDirectionCode());
                //作業工程コード
                if (tblWork.getMstWorkPhase() != null) {
                    line.add(tblWork.getMstWorkPhase().getWorkPhaseCode());
                }
                else {
                    line.add("");
                }
                // 作業工程
                line.add(tblWork.getWorkPhaseName());
                // 作業内容
                line.add(tblWork.getWorkCategoryName());
                // 作業時間
                if (tblWork.getWorkingTimeMinutes() != null) {
                    line.add(Integer.toString(tblWork.getWorkingTimeMinutes()));
                } else {
                    line.add("");
                }
                // 実稼働時間
                if (tblWork.getActualTimeMinutes()!= null) {
                    line.add(Integer.toString(tblWork.getActualTimeMinutes()));
                } else {
                    line.add("");
                }                
                // 休憩時間
                if (tblWork.getBreakTimeMinutes()!= null) {
                    line.add(Integer.toString(tblWork.getBreakTimeMinutes()));
                } else {
                    line.add("");
                }                
                // 金型ID
                line.add(tblWork.getMoldId());
                //金型名称
                line.add(tblWork.getMoldName());
                // 部品コード
                line.add(tblWork.getComponentCode());
                //部品名称
                line.add(tblWork.getComponentName());
                //設備ID
                if (tblWork.getMstMachine() != null) {
                    line.add(tblWork.getMstMachine().getMachineId());
                }
                else {
                    line.add("");
                }
                //設備名称
                if (tblWork.getMstMachine() != null) {
                    line.add(tblWork.getMstMachine().getMachineName());
                }
                else {
                    line.add("");
                }
                // 工程コード
                line.add(tblWork.getProcCd());
                //所属工程コード
                if (tblWork.getMstUser() != null) {
                    line.add(tblWork.getMstUser().getProcCd());
                }
                else {
                    line.add("");
                }
                // 作業コード
                line.add(tblWork.getWorkCode());
                // ロック
                if (tblWork.getLocked() == 1) {
                    line.add(mstDictionaryService.getDictionaryValue(langId, "work_record_locked"));
                } else {
                    line.add("");
                }
                // 行を明細リストに追加
                contents.add(line);
            }
        }

        for (int i = 0; i < contents.size(); i++) {
            gLineList.add(contents.get(i));
        }

        CSVFileUtil csvFileUtil = null;
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        try {
            csvFileUtil = new CSVFileUtil(outCsvPath, "csvOutput");
            Iterator<ArrayList> iter = gLineList.iterator();
            while (iter.hasNext()) {
                csvFileUtil.toCSVLine(iter.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }

        FileUtil fu = new FileUtil();

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_WORK);
        tblCsvExport.setExportDate(new java.util.Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_WORK_LIST);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "work_daily_report_management");
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }
    
    /**
     * 作業実績テーブル登録
     * @param tblWork
     * @param loginUser 
     */
    @Transactional
    public String createTblWork(TblWork tblWork, LoginUser loginUser) {
        // 登録処理時の強制設定パラメータセット
        tblWork.setId(IDGenerator.generate());
        tblWork.setCreateDate(new java.util.Date());
        tblWork.setCreateUserUuid(loginUser.getUserUuid());
        tblWork.setUpdateDate(tblWork.getCreateDate());
        tblWork.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(tblWork);
        //複数部品同時登録
        String firstComponentId = tblWork.getComponentId();
        if (tblWork.getComponentIdArray() != null) {
            for (String componentId: tblWork.getComponentIdArray()) {
                if (componentId != null && !componentId.trim().equals("") && !componentId.equals(firstComponentId)) {
                    TblWork newWork = new TblWork();
                    BeanCopyUtil.copyFields(tblWork, newWork);
                    newWork.setId(IDGenerator.generate());
                    newWork.setComponentId(componentId);
                    entityManager.persist(newWork);
                }
                
            }
        }
        
        return tblWork.getId();
    }
    
    /**
     * 作業実績テーブル更新
     * @param tblWork
     * @param loginUser 
     */
    @Transactional
    public void updateTblWork(TblWork tblWork, LoginUser loginUser) {
        tblWork.setUpdateDate(new java.util.Date());
        tblWork.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(tblWork);
    }
    
    /**
     * 作業テーブル削除
     * @param id 
     */
    @Transactional
    public void deleteTblWork(String id) {      
        //機械日報2からも関連する作業を消す.
        //作業が消されると作業IDがFK制約によりSET NULLされるので、先に行う
        machineDailyReport2Service.deleteWork(id);
        //作業テーブルの削除
        Query query = entityManager.createNamedQuery("TblWork.delete");
        query.setParameter("id", id);
        query.executeUpdate();
    }
    
    /**
     * 作業テーブルのみを削除
     * @param id 
     */
    @Transactional
    public void deleteOnlyTblWork(String id) {      
        //作業テーブルの削除
        Query query = entityManager.createNamedQuery("TblWork.delete");
        query.setParameter("id", id);
        query.executeUpdate();
    }
    
    private BasicResponse setApplicationError(BasicResponse response, LoginUser loginUser, String dictKey, String errorContentsName) {
        setBasicResponseError(
                response
               ,true
               ,ErrorMessages.E201_APPLICATION
               ,mstDictionaryService.getDictionaryValue(loginUser.getLangId(), dictKey)
        );
        String logMessage = response.getErrorMessage() + ":" + errorContentsName;
        Logger.getLogger(TblWorkResource.class.getName()).log(Level.FINE, logMessage);
        return response;
    }
    
    private BasicResponse setBasicResponseError(BasicResponse response, boolean error, String errorCode, String errorMessage) {
        response.setError(error);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        return response;
    }

    public TblWork getMoldUsingWork(String moldId, String moldUuid, String userUuid) {
        TblWork work = null;
        //終了していなくて指定のユーザー以外がその金型を使って行っている作業を取得する
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT t FROM TblWork t JOIN FETCH t.mstMold mold WHERE t.endDatetime IS NULL AND t.personUuid != :personUuid ");
        if (moldUuid != null && !moldUuid.equals("")) {
            //UUIDが指定されていればUUIDを使う
            queryString.append(" AND t.moldUuid = :moldUuid ");
        } else if (moldId != null && !moldId.equals("")) {
            //UUIDがなくて、金型IDが指定されていれば金型IDを使う
            queryString.append(" AND mold.moldId = :moldId ");
        } else {
            return null;
        }
        //String queryString = "SELECT t FROM TblWork t JOIN FETCH t.mstMachine mac WHERE t.endDatetime IS NULL AND t.personUuid != :personUuid AND mac.machineId = :machineId";
        Query query = entityManager.createQuery(queryString.toString());
        query.setParameter("personUuid", userUuid);
        if (moldUuid != null && !moldUuid.equals("")) {
            query.setParameter("moldUuid", moldUuid);
        } else if (moldId != null && !moldId.equals("")) {
            query.setParameter("moldId", moldId);
        }
        List<TblWork> resultList = query.getResultList();
        if (resultList != null && resultList.size() > 0) {
            //複数該当する場合はひとつだけ返す（順不同）
            work = resultList.get(0);
        }
        return work;
    }
    
    public TblWork getMachineUsingWork(String machineId, String machineUuid, String userUuid) {
        TblWork work = null;
        //終了していなくて指定のユーザー以外がその設備を使って行っている作業を取得する
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT t FROM TblWork t JOIN FETCH t.mstMachine mac WHERE t.endDatetime IS NULL AND t.personUuid != :personUuid ");
        if (machineUuid != null && !machineUuid.equals("")) {
            //UUIDが指定されていればUUIDを使う
            queryString.append(" AND t.machineUuid = :machineUuid ");
        }
        else if (machineId != null && !machineId.equals("")) {
            //UUIDがなくて、設備IDが指定されていれば設備IDを使う
            queryString.append(" AND mac.machineId = :machineId ");
        }
        else {
            return null;
        }
        //String queryString = "SELECT t FROM TblWork t JOIN FETCH t.mstMachine mac WHERE t.endDatetime IS NULL AND t.personUuid != :personUuid AND mac.machineId = :machineId";
        Query query = entityManager.createQuery(queryString.toString());
        query.setParameter("personUuid", userUuid);
        if (machineUuid != null && !machineUuid.equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        else if (machineId != null && !machineId.equals("")) {
            query.setParameter("machineId", machineId);
        }
        List<TblWork> resultList = query.getResultList();
        if (resultList != null && resultList.size() > 0) {
            //複数該当する場合はひとつだけ返す（順不同）
            work = resultList.get(0);
        }
        return work;
    }

    /**
     * 金型IDをキーにして金型メンテナンス・改造テーブルの終了日時のMAXより大きい作業開始日時のレコードが作業テーブルにあるかどうかを検索して判定する。
     *
     * @param moldId
     * @param langId
     * @return
     */
    public BasicResponse checkFirstUseAfterMainte(String moldId, String langId, LoginUser loginUser) {
        BasicResponse response = new BasicResponse();
        int categoryValue = 0;
        String categoryCompanyId = "";
        String categoryChoiceText = "";
        String categoryText = "";
        String categoryMessage;
        String reportText;
        
        Query query = entityManager.createQuery(" SELECT MAX(mr.endDatetime), mr.mstMold.lastMainteDate FROM TblMoldMaintenanceRemodeling mr WHERE mr.mstMold.moldId = :moldId ");
        query.setParameter("moldId", moldId);
        Object maxDateTime = null;
        try {
            Object result = query.getSingleResult();
            Object[] arrObj = (Object[]) result;
            // 最大メンテ終了日時がNULL Or 金型の最終メンテナンス日がNULL対象外
            if (arrObj.length != 2 || arrObj[0] == null || arrObj[1] == null) {
                return response;
            } else {
                maxDateTime = arrObj[0];
            }
            
        } catch (NoResultException e) {
            // メンテナンス・改造テーブルレコードなし対象外
            return response;
        }
        
        query = entityManager.createQuery(" SELECT work FROM TblWork work "
                + " JOIN FETCH  work.mstMold "
                + " WHERE work.startDatetime > :maxEndDateTime "
                + "       AND work.mstMold.moldId = :moldId ");
        query.setParameter("moldId", moldId);
        query.setParameter("maxEndDateTime", (Date) maxDateTime);
        List list = query.getResultList();
        //List moldDataList = query.getResultList();
        // 金型メンテナンス・改造テーブルの終了日時のMAXより大きい作業情報がない場合、知らせるメッセージを表示する
            
        Query queryMoldData = entityManager.createQuery("SELECT r FROM TblMoldMaintenanceRemodeling r JOIN FETCH r.mstMold WHERE r.mstMold.moldId = :moldId ORDER BY r.endDatetime DESC");
        queryMoldData.setParameter("moldId", moldId);

        List moldDataList = (List<TblMoldMaintenanceRemodeling>) queryMoldData.getResultList();
        // casts List<Object> -> List<Something>
        TblMoldMaintenanceRemodeling moldData = (TblMoldMaintenanceRemodeling) moldDataList.get(0);
        List moldRemodelDataDetail = (List<TblMoldRemodelingDetail>) moldData.getTblMoldRemodelingDetailCollection();
        List moldDataDetail = (List<TblMoldMaintenanceDetail>) moldData.getTblMoldMaintenanceDetailCollection();
        categoryCompanyId = moldData.getMstMold().getCompanyId();
        if(moldDataDetail.isEmpty()){
            TblMoldRemodelingDetail moldDetail = (TblMoldRemodelingDetail) moldRemodelDataDetail.get(0);
            if(moldDetail.getRemodelReasonCategory1() != null){             
                categoryValue = moldDetail.getRemodelReasonCategory1();
                categoryChoiceText = "tbl_mold_remodeling_detail.remodel_reason_category1";
            }
        }else{
            TblMoldMaintenanceDetail moldDetail = (TblMoldMaintenanceDetail) moldDataDetail.get(0);
            if(moldDetail.getMainteReasonCategory1() != null){
                categoryValue = moldDetail.getMainteReasonCategory1();
                categoryChoiceText = "tbl_mold_maintenance_detail.mainte_reason_category1";
            }
        }
        boolean externalMoldFlag = false;
        externalMoldFlag = FileUtil.checkExternal(entityManager, mstDictionaryService,moldData.getId(),loginUser).isError();
        if(externalMoldFlag){        
            categoryText = extMstChoiceService.getExtMstChoiceText(categoryCompanyId,categoryChoiceText,String.valueOf(categoryValue),langId);
        } else {
            MstChoice categoryChoice = mstChoiceService.getBySeqChoice(String.valueOf(categoryValue),langId,categoryChoiceText);
            if(categoryChoice != null){ 
                categoryText = categoryChoice.getChoice();
            }else{
                categoryText = "";
            }
        }
        if(moldData.getReport() == null){
            reportText = "";
        }else{
            reportText = moldData.getReport();
        }

        String carriageReturn = "\r\n";
        String maintenanceMessage = mstDictionaryService.getDictionaryValue(langId, "msg_first_work_after_mold_maintenance");
        if(moldDataDetail.isEmpty()){
            categoryMessage = mstDictionaryService.getDictionaryValue(langId, "remodel_reason_category1");
        }else{
            categoryMessage = mstDictionaryService.getDictionaryValue(langId, "maintenance_reason_category1");
        }
        String reasonMessage = mstDictionaryService.getDictionaryValue(langId, "mold_maintenance_remodeling_report");
        String firstAfterMaintenance = maintenanceMessage+carriageReturn
                +categoryMessage+": "+categoryText+carriageReturn
                +reasonMessage+": "+reportText;
        if (list.isEmpty()) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(firstAfterMaintenance);
        }
        
        return response;
    }
    
    /**
     * 作業実績テーブル条件指定検索(実レコード取得用ラッパー）
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
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @return 
     */
    public TblWorkList getWorkByConditionPage(
            String userId     // ユーザーID
           ,java.util.Date workingDateFrom // 作業日From yyyy/mm/dd
           ,java.util.Date workingDateTo // 作業日To yyyy/mm/dd
           ,String userName // 作業者氏名
           ,Integer department // 部署
           ,String procCd // 所属工程コード
           ,Integer locked // ロック
           ,String machineId
           ,java.util.Date workStartDateTime // 選択日付の業務開始時刻
           ,java.util.Date nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
           ,String sidx
           ,String sord
           ,int pageNumber
           ,int pageSize
           ,boolean isPage
    ) {
        
        TblWorkList tblWorkList = new TblWorkList();
        
        if (isPage) {

            List count = getWorkByConditionPageSql(userId, workingDateFrom, workingDateTo
                    , userName, department, procCd, locked, machineId,  workStartDateTime, nextDayWorkStartDateTime, sidx, sord, pageNumber,
                    pageSize, true);

            // ページをめぐる
            Pager pager = new Pager();
            tblWorkList.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            tblWorkList.setCount(counts);
            tblWorkList.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }
        
        List list = getWorkByConditionPageSql(userId, workingDateFrom, workingDateTo
                , userName, department, procCd, locked, machineId,  workStartDateTime, nextDayWorkStartDateTime, sidx, sord, pageNumber,
                pageSize, false);
        
        List<TblWork> tblWors = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TblWork iTblWork = (TblWork)list.get(i);
            iTblWork.setUserName(iTblWork.getUserName());
            tblWors.add(iTblWork);
        }
        
        tblWorkList.setTblWorks(tblWors);
        return tblWorkList;
    }
    
    /**
     * 作業実績テーブル条件指定検索(実検索処理)
     * @param userId
     * @param workingDateFrom
     * @param workingDateTo
     * @param userName
     * @param department
     * @param procCd
     * @param locked
     * @param flag
     * @param machineId
     * @param nextDayWorkStartDateTime
     * @param workStartDateTime
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @return 
     */
    public List getWorkByConditionPageSql(
            String userId     // ユーザーID
           ,java.util.Date workingDateFrom // 作業日From yyyy/mm/dd
           ,java.util.Date workingDateTo // 作業日To yyyy/mm/dd
           ,String userName // 作業者氏名
           ,Integer department // 部署
           ,String procCd // 所属工程コード
           ,Integer locked // ロック
           ,String machineId //設備id
           ,java.util.Date workStartDateTime // 選択日付の業務開始時刻
           ,java.util.Date nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
           ,String sidx
           ,String sord
           ,int pageNumber
           ,int pageSize
           ,boolean isCount
    ) {
        // 作業実績テーブルデータ取得
        StringBuilder sql = new StringBuilder();
        if(isCount){
            sql.append("SELECT count(tblWork) FROM TblWork tblWork LEFT JOIN FETCH tblWork.mstUser WHERE 1=1 ");
        } else {
            //sql.append("SELECT tblWork FROM TblWork tblWork LEFT JOIN FETCH tblWork.mstUser WHERE 1=1 ");
            sql.append("SELECT tblWork FROM TblWork tblWork ");
            sql.append(" LEFT JOIN FETCH tblWork.mstUser mu ");
            sql.append(" LEFT JOIN FETCH tblWork.mstWorkPhase mwp ");
            sql.append(" LEFT JOIN FETCH tblWork.tblDirection td ");
            sql.append(" LEFT JOIN FETCH tblWork.mstComponent mc ");
            sql.append(" LEFT JOIN FETCH tblWork.mstMold mold ");
            sql.append(" LEFT JOIN FETCH tblWork.mstMachine machine ");
            sql.append("WHERE 1=1 ");
        }
        
        if (userId != null && !"".equals(userId)) {
            sql = sql.append(" and tblWork.mstUser.userId = :userId ");
        }
        if (workingDateFrom != null) {
            sql = sql.append(" and tblWork.workingDate >= :workingDateFrom ");
        }
        if (workingDateTo != null) {
            sql = sql.append(" and tblWork.workingDate <= :workingDateTo ");
        }
        if (userName != null && !"".equals(userName)) {
            sql = sql.append(" and tblWork.mstUser.userName like :userName ");
        }
        if (department != null && department != 0) {
            try {
                Integer.toString(department);
                sql = sql.append(" and tblWork.mstUser.department = :department ");
            } catch (NumberFormatException numberFormatException) {
                ;
            }
        }
        if (procCd != null && !"".equals(procCd)) {
            sql = sql.append(" and tblWork.mstUser.procCd = :procCd ");
        }
        if (locked != null && locked != 0) {
            sql = sql.append(" and tblWork.locked = :locked ");
        }
        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" and tblWork.mstMachine.machineId = :machineId ");
        }
        if (nextDayWorkStartDateTime != null) {
            // 開始日時<=選択日付の翌日の業務開始時刻
            sql = sql.append(" and (tblWork.startDatetime <= :startDatetime ");
        }
        if (workStartDateTime != null) {
             // 終了日時>=選択日付の業務開始時刻
            sql = sql.append(" and tblWork.endDatetime >= :endDatetime ");
        }
        if (nextDayWorkStartDateTime != null) {
            // 開始日時<=選択日付の翌日の業務開始時刻且つ終了日時 IS NULL
            sql = sql.append(" or (tblWork.endDatetime is null and tblWork.startDatetime <= :startDatetime)) ");
        }
        
        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;
                
                sql.append(sortStr);

            } else {

             //　作業日、開始日時 昇順
                sql = sql.append(" ORDER BY tblWork.workingDate ASC, tblWork.startDatetime ASC ");

            }

        }
                
        Query query = entityManager.createQuery(sql.toString());
        
        if (userId != null && !"".equals(userId)) {
            query.setParameter("userId", userId.trim());
        }
        if (workingDateFrom != null) {
            query.setParameter("workingDateFrom", workingDateFrom);
        }
        if (workingDateTo != null) {
            query.setParameter("workingDateTo", workingDateTo);
        }
        if (userName != null && !"".equals(userName)) {
            query.setParameter("userName", "%" + userName + "%");
        }
        if (department != null && department != 0) {
            try {
                String bindDepartment = Integer.toString(department);
                query.setParameter("department", bindDepartment);
            } catch (NumberFormatException numberFormatException) {
                ;
            }
        }
        if (procCd != null && !"".equals(procCd)) {
            query.setParameter("procCd", procCd.trim());
        }
        if (locked != null && locked != 0) {
            query.setParameter("locked", locked);
        }
        
        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", machineId.trim());
        }
        
        if (nextDayWorkStartDateTime != null) {
            query.setParameter("startDatetime", nextDayWorkStartDateTime);
        }
        
        if (workStartDateTime != null) {
            query.setParameter("endDatetime", workStartDateTime);
        }
        
        // 画面改ページを設定する
        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        logger.log(Level.FINE, "----------------------- query start -----------------");
        List list = query.getResultList();
        logger.log(Level.FINE, "----------------------- query end -----------------");
        return list;
    }
}
