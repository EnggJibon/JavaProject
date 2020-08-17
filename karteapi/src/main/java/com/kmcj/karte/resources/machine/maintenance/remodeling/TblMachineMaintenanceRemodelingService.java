package com.kmcj.karte.resources.machine.maintenance.remodeling;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.batch.externalmold.choice.ExtMstChoiceService;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.daily.report.TblMachineDailyReportService;
import com.kmcj.karte.resources.machine.daily.report.TblMachineDailyReportVo;
import com.kmcj.karte.resources.machine.inspection.result.TblMachineInspectionResult;
import com.kmcj.karte.resources.machine.inspection.result.TblMachineInspectionResultVo;
import com.kmcj.karte.resources.machine.maintenance.detail.TblMachineMaintenanceDetail;
import com.kmcj.karte.resources.machine.maintenance.detail.TblMachineMaintenanceDetailImageFile;
import com.kmcj.karte.resources.machine.maintenance.detail.TblMachineMaintenanceDetailImageFileVo;
import com.kmcj.karte.resources.machine.maintenance.detail.TblMachineMaintenanceDetailVo;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetail;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailImageFile;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailImageFileVo;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailVo;
import com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResult;
import com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResultVo;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistory;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistoryService;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistoryVo;
import com.kmcj.karte.resources.mold.issue.TblIssue;
import com.kmcj.karte.resources.mold.issue.TblIssueList;
import com.kmcj.karte.resources.mold.issue.TblIssueVo;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodelingVo;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import com.kmcj.karte.util.TimezoneConverter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author jiangxs
 */
@Dependent
public class TblMachineMaintenanceRemodelingService {
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    @Inject
    private MstChoiceService mstChoiceService;

    @Inject
    private KartePropertyService kartePropertyService;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private TblCsvExportService tblCsvExportService;
       
    @Inject
    private ExtMstChoiceService extMstChoiceService;
    
    @Inject
    private MstMachineSpecHistoryService mstMachineSpecHistoryService;
    
    @Inject
    private TblMachineDailyReportService tblMachineDailyReportService;
    
    @Inject
    private CnfSystemService cnfSystemService;
    
    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("mainteDate", " ORDER BY mr.mainteDate ");// 設備メンテナンス日
        orderKey.put("mainteOrRemodelText", " ORDER BY mr.mainteOrRemodel ");// 改造・メンテナンス区分
        orderKey.put("machineId", " ORDER BY m.machineId ");// 設備ＩＤ
        orderKey.put("machineName", " ORDER BY m.machineName ");// 設備名称
        orderKey.put("reportPersonName", " ORDER BY mu.userName ");// 実施者
        orderKey.put("mainteTypeText", " ORDER BY mr.mainteType ");// メンテナンス分類
        orderKey.put("remodelingTypeText", " ORDER BY mr.remodelingType ");// 改造分類
        orderKey.put("startDatetimeStr", " ORDER BY mr.startDatetime ");// 開始日時
        orderKey.put("endDatetimeStr", " ORDER BY mr.endDatetime ");// 終了日時
        orderKey.put("workingTimeMinutes", " ORDER BY mr.workingTimeMinutes ");// 所要時間
        orderKey.put("report", " ORDER BY mr.report ");// 報告事項
        orderKey.put("tblDirectionCode", " ORDER BY mr.tblDirectionCode ");// 手配・工事番号
       
    }
    
    /**
     * 設備メンテナンス開始入力
     * @param issueVo
     * @param loginUser
     * @return 
     */
    @Transactional
    public TblMachineMaintenanceRemodelingVo postMachineMaintenanceStart(TblIssueVo issueVo, LoginUser loginUser) {
        TblMachineMaintenanceRemodelingVo response = new TblMachineMaintenanceRemodelingVo();
        BasicResponse basicResponse = new BasicResponse();
        String machineId = issueVo.getMachineId();
        MstMachine mstMachine = entityManager.find(MstMachine.class, machineId);
        
        if(mstMachine != null){
            
            //外部データチェック
            basicResponse = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", mstMachine.getCompanyId(), loginUser);
            if(basicResponse.isError()){
                response.setError(basicResponse.isError());
                response.setErrorCode(basicResponse.getErrorCode());
                response.setErrorMessage(basicResponse.getErrorMessage());
                return response;
            }
            
            //メンテイ状態チェック
            if (null != mstMachine.getMainteStatus() && mstMachine.getMainteStatus() == CommonConstants.MAINTEORREMODEL_MAINTE) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_under_maintenance"));
                return response;
            }else if(null != mstMachine.getMainteStatus() && mstMachine.getMainteStatus() == CommonConstants.MAINTEORREMODEL_REMODEL){
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_remodeling"));
                return response;
            }
            
             //設備マスタ 更新 状態をメンテナンス中にする
             mstMachine.setMainteStatus(CommonConstants.MAINTEORREMODEL_MAINTE);
             mstMachine.setUpdateDate(new Date());
             mstMachine.setUpdateUserUuid(loginUser.getUserUuid());
             entityManager.merge(mstMachine);
        }else{
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return response;
        }
        
        //設備メンテナンス 追加
        TblMachineMaintenanceRemodeling newTblMachineMaintenanceRemodeling = new TblMachineMaintenanceRemodeling();
        newTblMachineMaintenanceRemodeling.setId(IDGenerator.generate());
        newTblMachineMaintenanceRemodeling.setMainteOrRemodel(CommonConstants.MAINTEORREMODEL_MAINTE);
        newTblMachineMaintenanceRemodeling.setCreateDate(new Date());
        newTblMachineMaintenanceRemodeling.setCreateUserUuid(loginUser.getUserUuid());
        newTblMachineMaintenanceRemodeling.setMainteDate(new Date());
        if (null != issueVo.getId() && !"".equals(issueVo.getId().trim())) {
            newTblMachineMaintenanceRemodeling.setIssueId(issueVo.getId());
        }

        newTblMachineMaintenanceRemodeling.setUpdateDate(new Date());
        newTblMachineMaintenanceRemodeling.setUpdateUserUuid(loginUser.getUserUuid());
        newTblMachineMaintenanceRemodeling.setMachineUuid(issueVo.getMachineUuid());

        newTblMachineMaintenanceRemodeling.setStartDatetime(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
        newTblMachineMaintenanceRemodeling.setStartDatetimeStz(new Date());
        entityManager.persist(newTblMachineMaintenanceRemodeling);
        entityManager.flush();
        entityManager.clear();
        response.setId(newTblMachineMaintenanceRemodeling.getId());
        response.setMachineId(mstMachine.getMachineId());
        response.setMachineName(mstMachine.getMachineName());
        
        //異常テーブル 更新 異常データが選択されたとき
        if (null != issueVo.getId() && !"".equals(issueVo.getId().trim())) {
            TblIssue issue = entityManager.find(TblIssue.class, issueVo.getId());
            issue.setMachineMainTenanceId(newTblMachineMaintenanceRemodeling.getId());
            issue.setMeasureStatus(CommonConstants.ISSUE_MEASURE_STATUS_RESOLVING);
            issue.setUpdateDate(new Date());
            issue.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(issue);
        }
        
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
    
    /**
     * 設備メンテナンス開始入力
     * @param tblMachineMaintenanceRemodelingVo
     * @param loginUser
     * @return 
     */
    @Transactional
    public TblMachineMaintenanceRemodelingVo postMachineMaintenanceStart2(TblMachineMaintenanceRemodelingVo tblMachineMaintenanceRemodelingVo, LoginUser loginUser) {
        TblMachineMaintenanceRemodelingVo response = new TblMachineMaintenanceRemodelingVo();
        BasicResponse basicResponse = new BasicResponse();
        String machineId = tblMachineMaintenanceRemodelingVo.getMachineId();
        MstMachine mstMachine = entityManager.find(MstMachine.class, machineId);
        
        if(mstMachine != null){
            
            //外部データチェック
            basicResponse = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", mstMachine.getCompanyId(), loginUser);
            if(basicResponse.isError()){
                response.setError(basicResponse.isError());
                response.setErrorCode(basicResponse.getErrorCode());
                response.setErrorMessage(basicResponse.getErrorMessage());
                return response;
            }
            
            //メンテイ状態チェック
            if (null != mstMachine.getMainteStatus() && mstMachine.getMainteStatus() == CommonConstants.MAINTEORREMODEL_MAINTE) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_under_maintenance"));
                return response;
            }else if(null != mstMachine.getMainteStatus() && mstMachine.getMainteStatus() == CommonConstants.MAINTEORREMODEL_REMODEL){
                response.setError(true);
                response.setErrorCode(ErrorMessages.E201_APPLICATION);
                response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_remodeling"));
                return response;
            }
            
             //設備マスタ 更新 状態をメンテナンス中にする
             mstMachine.setMainteStatus(CommonConstants.MAINTEORREMODEL_MAINTE);
             mstMachine.setUpdateDate(new Date());
             mstMachine.setUpdateUserUuid(loginUser.getUserUuid());
             entityManager.merge(mstMachine);
        }else{
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return response;
        }
        
        //設備メンテナンス 追加
        TblMachineMaintenanceRemodeling newTblMachineMaintenanceRemodeling = new TblMachineMaintenanceRemodeling();
        newTblMachineMaintenanceRemodeling.setId(IDGenerator.generate());
        newTblMachineMaintenanceRemodeling.setMainteOrRemodel(CommonConstants.MAINTEORREMODEL_MAINTE);
        newTblMachineMaintenanceRemodeling.setMainteType(tblMachineMaintenanceRemodelingVo.getMainteType());
        newTblMachineMaintenanceRemodeling.setTblDirectionId(tblMachineMaintenanceRemodelingVo.getTblDirectionId());
        //システム設定によりテーブル参照せずに手配・工事番号が直接送られてくるとき
        if (tblMachineMaintenanceRemodelingVo.getTblDirectionId() == null && tblMachineMaintenanceRemodelingVo.getTblDirectionCode() != null) {
            newTblMachineMaintenanceRemodeling.setTblDirectionCode(tblMachineMaintenanceRemodelingVo.getTblDirectionCode());
        }
        newTblMachineMaintenanceRemodeling.setCreateDate(new Date());
        newTblMachineMaintenanceRemodeling.setCreateUserUuid(loginUser.getUserUuid());
        newTblMachineMaintenanceRemodeling.setMainteDate(new Date());
        if (null != tblMachineMaintenanceRemodelingVo.getIssueId() && !"".equals(tblMachineMaintenanceRemodelingVo.getIssueId().trim())) {
            newTblMachineMaintenanceRemodeling.setIssueId(tblMachineMaintenanceRemodelingVo.getIssueId());
        }

        newTblMachineMaintenanceRemodeling.setUpdateDate(new Date());
        newTblMachineMaintenanceRemodeling.setUpdateUserUuid(loginUser.getUserUuid());
        newTblMachineMaintenanceRemodeling.setMachineUuid(tblMachineMaintenanceRemodelingVo.getMachineUuid());

        newTblMachineMaintenanceRemodeling.setStartDatetime(tblMachineMaintenanceRemodelingVo.getStartDatetime());
        newTblMachineMaintenanceRemodeling.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblMachineMaintenanceRemodelingVo.getStartDatetime()));
        
        entityManager.persist(newTblMachineMaintenanceRemodeling);
        entityManager.flush();
        entityManager.clear();
        response.setId(newTblMachineMaintenanceRemodeling.getId());
        response.setMachineId(mstMachine.getMachineId());
        response.setMachineName(mstMachine.getMachineName());
        
        //異常テーブル 更新 異常データが選択されたとき
        if (null != tblMachineMaintenanceRemodelingVo.getIssueId() && !"".equals(tblMachineMaintenanceRemodelingVo.getIssueId().trim())) {
            TblIssue issue = entityManager.find(TblIssue.class, tblMachineMaintenanceRemodelingVo.getIssueId());
            issue.setMachineMainTenanceId(newTblMachineMaintenanceRemodeling.getId());
            issue.setMeasureStatus(CommonConstants.ISSUE_MEASURE_STATUS_RESOLVING);
            issue.setUpdateDate(new Date());
            issue.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(issue);
        }
        
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
    
    /**
     * 設備改造開始入力 設備の状態がすでに改造中のとき確認します
     * @param machineId
     * @return 
     */
    public int getMachineMainteStatus(String machineId) {
        Query query = entityManager.createNamedQuery("MstMachine.findByMachineId");
        query.setParameter("machineId", machineId);
        try {
            MstMachine mstMachine = (MstMachine) query.getSingleResult();
            return mstMachine.getMainteStatus() == null ? 0 : mstMachine.getMainteStatus();
        } catch (NoResultException e) {
            return 0;
        }
    }
    
    
    /**
     * 設備改造開始入力
     * @param tblMachineMaintenanceRemodelingVo
     * @param user
     * @return 
     */
    @Transactional
    public TblMachineRemodelingDetailVo changeMachineMainteStatus(TblMachineMaintenanceRemodelingVo tblMachineMaintenanceRemodelingVo, LoginUser user) {
        TblMachineRemodelingDetailVo response = new TblMachineRemodelingDetailVo();
        
        String machineId = tblMachineMaintenanceRemodelingVo.getMachineId();
        String machineUuid = tblMachineMaintenanceRemodelingVo.getMachineUuid();
        
        MstMachine mstMachine = entityManager.find(MstMachine.class, machineId);
        if(mstMachine != null){
            mstMachine.setMainteStatus(CommonConstants.MAINTE_STATUS_REMODELING);
            mstMachine.setUpdateDate(new Date());
            mstMachine.setUpdateUserUuid(user.getUserUuid());
            entityManager.merge(mstMachine);
        }else{
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
            return response;
        }
        
        //追加設備改造
        TblMachineMaintenanceRemodeling tblMachineMaintenanceRemodeling = new TblMachineMaintenanceRemodeling();
        tblMachineMaintenanceRemodeling.setId(IDGenerator.generate());
        tblMachineMaintenanceRemodeling.setMainteOrRemodel(CommonConstants.MAINTE_STATUS_REMODELING);
        tblMachineMaintenanceRemodeling.setMachineUuid(machineUuid);
        tblMachineMaintenanceRemodeling.setMainteDate(new Date());
        tblMachineMaintenanceRemodeling.setRemodelingType(tblMachineMaintenanceRemodelingVo.getRemodelingType());
        tblMachineMaintenanceRemodeling.setTblDirectionId(tblMachineMaintenanceRemodelingVo.getTblDirectionId());
        //システム設定により手配・工事テーブル参照しないときはコードを直接保存
        if (tblMachineMaintenanceRemodelingVo.getTblDirectionId() == null && tblMachineMaintenanceRemodelingVo.getTblDirectionCode() != null) {
            tblMachineMaintenanceRemodeling.setTblDirectionCode(tblMachineMaintenanceRemodelingVo.getTblDirectionCode());
        }
        tblMachineMaintenanceRemodeling.setStartDatetime(TimezoneConverter.getLocalTime(user.getJavaZoneId()));        
        tblMachineMaintenanceRemodeling.setStartDatetimeStz(new Date());
        tblMachineMaintenanceRemodeling.setCreateDate(new Date());
        tblMachineMaintenanceRemodeling.setCreateUserUuid(user.getUserUuid());
        tblMachineMaintenanceRemodeling.setUpdateDate(new Date());
        tblMachineMaintenanceRemodeling.setUpdateUserUuid(user.getUserUuid());
        entityManager.persist(tblMachineMaintenanceRemodeling);
        
        response.setId(tblMachineMaintenanceRemodeling.getId());
        response.setMachineId(machineId);
        response.setMachineName(mstMachine.getMachineName());
        
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
        response.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "msg_record_updated"));
        return response;
    }
    
    
    /**
     * 設備名称と最新の設備改造データを取得し、一覧に表示する。（１コードまたはデータなし）
     * @param machineId 
     * @param type
     * @param loginUser
     * @return
     */
//    @Transactional
    public TblMachineMaintenanceRemodelingVo getMachinemainteOrRemodelDetail(String machineId, int type, LoginUser loginUser) {
        TblMachineMaintenanceRemodelingVo resVo = new TblMachineMaintenanceRemodelingVo();
        StringBuilder sql = new StringBuilder("select mmr from TblMachineMaintenanceRemodeling mmr join fetch MstMachine m ON mmr.machineUuid = m.uuid join fetch TblDirection t on mmr.tblDirectionId = t.id ");
//        sql.append(" and mmr.mstMold.moldId = :moldId and mmr.mainteOrRemodel = :mainteOrRemodel order by mmr.endDatetime asc,mmr.startDatetime desc ");
        sql.append(" where 1=1 and m.machineId = :machineId and mmr.mainteOrRemodel = :mainteOrRemodel order by mmr.startDatetime desc ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("machineId", machineId);
        query.setParameter("mainteOrRemodel", type);
        query.setMaxResults(1);

        try {
            TblMachineMaintenanceRemodeling aMachineMaintenanceRemodeling = (TblMachineMaintenanceRemodeling) query.getSingleResult();
            resVo.setId(aMachineMaintenanceRemodeling.getId());
            if (aMachineMaintenanceRemodeling.getMstMachine()!= null) {
                resVo.setMachineId(aMachineMaintenanceRemodeling.getMstMachine().getMachineId()== null ? "" : aMachineMaintenanceRemodeling.getMstMachine().getMachineId());
                resVo.setMachineName(aMachineMaintenanceRemodeling.getMstMachine().getMachineName());
            } else {
                resVo.setMachineId("");
                resVo.setMachineName("");
            }
            resVo.setStartDatetimeStr(null == aMachineMaintenanceRemodeling.getStartDatetime() ? "" : new FileUtil().getDateTimeFormatForStr(aMachineMaintenanceRemodeling.getStartDatetime()));
            if(aMachineMaintenanceRemodeling.getEndDatetime() != null){
                resVo.setEndDatetimeStr(aMachineMaintenanceRemodeling.getEndDatetime().compareTo(CommonConstants.SYS_MAX_DATE) == 0 ? "-" : new FileUtil().getDateTimeFormatForStr(aMachineMaintenanceRemodeling.getEndDatetime()));
            }else{
                resVo.setEndDatetimeStr("-");
            }
            resVo.setTblDirectionId(aMachineMaintenanceRemodeling.getTblDirectionId()== null ? "" : aMachineMaintenanceRemodeling.getTblDirectionId());
            if(aMachineMaintenanceRemodeling.getTblDirection() != null){
                resVo.setTblDirectionCode(aMachineMaintenanceRemodeling.getTblDirection().getDirectionCode() == null ? "" : aMachineMaintenanceRemodeling.getTblDirection().getDirectionCode());
            }else{
                resVo.setTblDirectionCode("");
            }
            
            resVo.setIssueId(aMachineMaintenanceRemodeling.getIssueId());
            
        } catch (NoResultException e) {
        }

        return resVo;
    }
    
    
    /**
     * 設備メンテナンス終了入力 
     * 設備メンテナンス詳細を取得 設備改造詳細を取得
     * @param maintenanceId
     * @param type
     * @param loginUser
     * @return
     */
    @Transactional
    public TblMachineMaintenanceRemodelingVo getMachineMainteOrRemodelDetails(String maintenanceId, int type, LoginUser loginUser) {
        
        TblMachineMaintenanceRemodelingVo responsMachineMaintenanceRemodelingVo = new TblMachineMaintenanceRemodelingVo();
        List<TblMachineMaintenanceDetailVo> response = new ArrayList<>();
        List<TblMachineRemodelingDetailVo> response1 = new ArrayList<>();
        
        
        if (null == maintenanceId || "".equals(maintenanceId)) {
            responsMachineMaintenanceRemodelingVo.setError(true);
            responsMachineMaintenanceRemodelingVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            responsMachineMaintenanceRemodelingVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null"));
            return responsMachineMaintenanceRemodelingVo;
        }

        StringBuilder maintenanceRemodelingSql = new StringBuilder("SELECT m FROM TblMachineMaintenanceRemodeling m LEFT JOIN FETCH m.mstMachine WHERE 1=1 ");
        maintenanceRemodelingSql.append(" AND m.id = :maintenanceId AND m.mainteOrRemodel = :mainteOrRemodel ");

        Query maintenanceRemodelingQuery = entityManager.createQuery(maintenanceRemodelingSql.toString());

        maintenanceRemodelingQuery.setParameter("maintenanceId", maintenanceId);
        maintenanceRemodelingQuery.setParameter("mainteOrRemodel", type);

        TblMachineMaintenanceRemodeling tblMachineMaintenanceRemodeling;
        try {
            tblMachineMaintenanceRemodeling = (TblMachineMaintenanceRemodeling) maintenanceRemodelingQuery.getSingleResult();
        } catch (NoResultException ex) {
            responsMachineMaintenanceRemodelingVo.setError(true);
            responsMachineMaintenanceRemodelingVo.setErrorCode(ErrorMessages.E201_APPLICATION);
            responsMachineMaintenanceRemodelingVo.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return responsMachineMaintenanceRemodelingVo;
        }
        responsMachineMaintenanceRemodelingVo.setId(maintenanceId);
        responsMachineMaintenanceRemodelingVo.setMachineUuid(tblMachineMaintenanceRemodeling.getMachineUuid());
        if (tblMachineMaintenanceRemodeling.getMstMachine() != null) {
            //設備ID
            responsMachineMaintenanceRemodelingVo.setMachineId(tblMachineMaintenanceRemodeling.getMstMachine().getMachineId());
            //設備名称	
            responsMachineMaintenanceRemodelingVo.setMachineName(tblMachineMaintenanceRemodeling.getMstMachine().getMachineName());
        } else {
            responsMachineMaintenanceRemodelingVo.setMachineId("");
            responsMachineMaintenanceRemodelingVo.setMachineName("");
        }
        FileUtil fu = new FileUtil();
        if (tblMachineMaintenanceRemodeling.getStartDatetime() != null) {
            responsMachineMaintenanceRemodelingVo.setStartDatetimeStr(fu.getDateTimeFormatForStr(tblMachineMaintenanceRemodeling.getStartDatetime()));
        } else {
            responsMachineMaintenanceRemodelingVo.setStartDatetimeStr("");
        }

        if (tblMachineMaintenanceRemodeling.getEndDatetime() != null) {
            responsMachineMaintenanceRemodelingVo.setEndDatetimeStr(tblMachineMaintenanceRemodeling.getEndDatetime().compareTo(CommonConstants.SYS_MAX_DATE) == 0 ? "-" : new FileUtil().getDateTimeFormatForStr(tblMachineMaintenanceRemodeling.getEndDatetime()));
        } else {
            responsMachineMaintenanceRemodelingVo.setEndDatetimeStr("");
        }
        responsMachineMaintenanceRemodelingVo.setWorkingTimeMinutes(tblMachineMaintenanceRemodeling.getWorkingTimeMinutes());
        
        MstMachine mstMachine = tblMachineMaintenanceRemodeling.getMstMachine();
        
        responsMachineMaintenanceRemodelingVo.setAfterMainteTotalProducingTimeHour(mstMachine.getAfterMainteTotalProducingTimeHour());
        responsMachineMaintenanceRemodelingVo.setAfterMainteTotalShotCount(mstMachine.getAfterMainteTotalShotCount());
                
        //外部データチェック
        if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", mstMachine.getCompanyId(), loginUser).isError()) {
            responsMachineMaintenanceRemodelingVo.setExternalFlg("1");
            //メンテ分類
            if (null != tblMachineMaintenanceRemodeling.getMainteType()) {
                responsMachineMaintenanceRemodelingVo.setMainteType(tblMachineMaintenanceRemodeling.getMainteType());
                responsMachineMaintenanceRemodelingVo.setMainteTypeText(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_maintenance_remodeling.mainte_type", String.valueOf(tblMachineMaintenanceRemodeling.getMainteType()), loginUser.getLangId()));
            } else {
                responsMachineMaintenanceRemodelingVo.setMainteTypeText("");
            }
            
            //改造
            if (null != tblMachineMaintenanceRemodeling.getRemodelingType()) {
                responsMachineMaintenanceRemodelingVo.setRemodelingType(tblMachineMaintenanceRemodeling.getRemodelingType());
                responsMachineMaintenanceRemodelingVo.setRemodelingTypeText(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_maintenance_remodeling.remodeling_type", String.valueOf(tblMachineMaintenanceRemodeling.getRemodelingType()), loginUser.getLangId()));
            } else {
                responsMachineMaintenanceRemodelingVo.setRemodelingTypeText("");
            }

        } else {
            responsMachineMaintenanceRemodelingVo.setExternalFlg("0");
            responsMachineMaintenanceRemodelingVo.setMainteType(tblMachineMaintenanceRemodeling.getMainteType());
            responsMachineMaintenanceRemodelingVo.setRemodelingType(tblMachineMaintenanceRemodeling.getRemodelingType());
        }

        if (null != tblMachineMaintenanceRemodeling.getMstUser()) {
            String userName = tblMachineMaintenanceRemodeling.getMstUser().getUserName();
            responsMachineMaintenanceRemodelingVo.setReportPersonName(userName == null ? "" : userName);
        }

        if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {
            TblIssue issue = tblMachineMaintenanceRemodeling.getTblIssue();
            if (null != issue) {
                responsMachineMaintenanceRemodelingVo.setIssueId(issue.getId());
                responsMachineMaintenanceRemodelingVo.setIssueText(issue.getIssue());
                responsMachineMaintenanceRemodelingVo.setIssueReportCategory1("" + issue.getReportCategory1());
                responsMachineMaintenanceRemodelingVo.setIssueReportCategory1Text(issue.getReportCategory1Text());
                responsMachineMaintenanceRemodelingVo.setMeasureStatus("" + issue.getMeasureStatus());
            } else {
                responsMachineMaintenanceRemodelingVo.setIssueId("");
                responsMachineMaintenanceRemodelingVo.setIssueText("");
                responsMachineMaintenanceRemodelingVo.setIssueReportCategory1("");
                responsMachineMaintenanceRemodelingVo.setIssueReportCategory1Text("");
                responsMachineMaintenanceRemodelingVo.setMeasureStatus("");
            }
        } else if (type == CommonConstants.MAINTEORREMODEL_REMODEL) {
            if (null != responsMachineMaintenanceRemodelingVo.getMstMachineSpecHistory()) {
                MstMachineSpecHistory machineSpecHistory = responsMachineMaintenanceRemodelingVo.getMstMachineSpecHistory();
                if (null != machineSpecHistory) {
                    responsMachineMaintenanceRemodelingVo.setMachineSpecHstName(machineSpecHistory.getMachineSpecName());
                } else {
                    responsMachineMaintenanceRemodelingVo.setMachineSpecHstName("");
                }
            } else {
                MstMachineSpecHistoryVo spechistoryVo = mstMachineSpecHistoryService.getMachineSpecHistoryNamesByMachineUuid(responsMachineMaintenanceRemodelingVo.getMachineUuid());
                if (spechistoryVo != null) {
                    responsMachineMaintenanceRemodelingVo.setMachineSpecHstName(spechistoryVo.getMachineSpecName());
                } else {
                    responsMachineMaintenanceRemodelingVo.setMachineSpecHstName("");
                }
            }
        }

        //報告事項	
        responsMachineMaintenanceRemodelingVo.setReport(tblMachineMaintenanceRemodeling.getReport() == null ? "" : tblMachineMaintenanceRemodeling.getReport());

        if (tblMachineMaintenanceRemodeling.getTblDirection() != null) {
            responsMachineMaintenanceRemodelingVo.setTblDirectionCode(tblMachineMaintenanceRemodeling.getTblDirection().getDirectionCode());
        } else {
            //システム設定により手配・工事テーブル参照しない場合はIDがNULLでもコードが保存されているので取得
            if (tblMachineMaintenanceRemodeling.getTblDirectionCode() != null) {
                responsMachineMaintenanceRemodelingVo.setTblDirectionCode(tblMachineMaintenanceRemodeling.getTblDirectionCode());
            }
            else {
                responsMachineMaintenanceRemodelingVo.setTblDirectionCode("");
            }
        }

        responsMachineMaintenanceRemodelingVo.setTblDirectionId(tblMachineMaintenanceRemodeling.getTblDirectionId());

        StringBuilder sql = new StringBuilder();
        if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {
            // メンテの場合
            sql.append(" SELECT machineMaintenanceDetail FROM TblMachineMaintenanceDetail machineMaintenanceDetail ");
            sql.append(" LEFT JOIN FETCH machineMaintenanceDetail.tblMachineMaintenanceRemodeling  ");
            sql.append(" LEFT JOIN FETCH machineMaintenanceDetail.tblMachineMaintenanceRemodeling.mstUser  ");
            sql.append(" LEFT JOIN FETCH machineMaintenanceDetail.tblMachineMaintenanceRemodeling.tblIssue ");
            sql.append(" WHERE 1=1 AND machineMaintenanceDetail.tblMachineMaintenanceRemodeling.mainteOrRemodel = :mainteOrRemodel ");
            sql.append(" AND machineMaintenanceDetail.tblMachineMaintenanceRemodeling.id = :maintenanceId ");
            //表示順はメンテナンス日の降順、設備IDの昇順
            sql.append(" ORDER BY machineMaintenanceDetail.tblMachineMaintenanceRemodeling.startDatetime DESC , ");
            sql.append(" machineMaintenanceDetail.tblMachineMaintenanceRemodeling.mstMachine.machineId ASC ");
        } else {
            // 改造の場合
            sql.append(" SELECT machineRemodelingDetail FROM TblMachineRemodelingDetail machineRemodelingDetail ");
            sql.append(" JOIN FETCH machineRemodelingDetail.tblMachineMaintenanceRemodeling  ");
            sql.append(" JOIN FETCH machineRemodelingDetail.tblMachineMaintenanceRemodeling.mstUser  ");
            sql.append(" JOIN FETCH machineRemodelingDetail.tblMachineMaintenanceRemodeling.mstMachine ");
            sql.append(" WHERE 1=1 AND machineRemodelingDetail.tblMachineMaintenanceRemodeling.mainteOrRemodel = :mainteOrRemodel ");
            sql.append(" AND machineRemodelingDetail.tblMachineMaintenanceRemodeling.id = :maintenanceId ");
            //表示順はメンテナンス日の降順、設備IDの昇順
            sql.append(" ORDER BY machineRemodelingDetail.tblMachineMaintenanceRemodeling.startDatetime DESC , ");
            sql.append("          machineRemodelingDetail.tblMachineMaintenanceRemodeling.mstMachine.machineId ASC ");
        }

        Query query = entityManager.createQuery(sql.toString());

        query.setParameter("maintenanceId", maintenanceId);
        query.setParameter("mainteOrRemodel", type);

        if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {
            // メンテの場合
            List<TblMachineMaintenanceDetail> machineMaintenanceDetail = query.getResultList();
            if (null != machineMaintenanceDetail && machineMaintenanceDetail.size() > 0) {
                TblMachineMaintenanceDetailVo machineMaintenanceDetailVo;
                for (int j = 0; j < machineMaintenanceDetail.size(); j++) {

                    TblMachineMaintenanceDetail aMachineMaintenanceDetail = (TblMachineMaintenanceDetail) machineMaintenanceDetail.get(j);

                    // 詳細データを取得
                    machineMaintenanceDetailVo = new TblMachineMaintenanceDetailVo();
                    machineMaintenanceDetailVo.setId(aMachineMaintenanceDetail.getId());
                    machineMaintenanceDetailVo.setMaintenanceId(maintenanceId);
                    //メンテ理由大分類
                    
                    boolean externalMoldFlag = false;
                    if(mstMachine.getMachineId() != null){
                        //String moldId = aMoldMaintenanceRemodeling.getMstMold().getMoldId();
                        externalMoldFlag = FileUtil.checkExternal(entityManager, mstDictionaryService, machineMaintenanceDetailVo.getMachineId(), loginUser).isError();
                        if(externalMoldFlag){
                            if (null != aMachineMaintenanceDetail.getMainteReasonCategory1()) {
                                machineMaintenanceDetailVo.setMainteReasonCategory1("" + aMachineMaintenanceDetail.getMainteReasonCategory1());
                                machineMaintenanceDetailVo.setMainteReasonCategory1Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_remodeling_detail.remodel_reason_category1", String.valueOf(machineMaintenanceDetailVo.getMainteReasonCategory1()), loginUser.getLangId()));
                                //aVo.setMainteReasonCategory1Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setMainteReasonCategory1("");
                                machineMaintenanceDetailVo.setMainteReasonCategory1Text("");
                            }
                            //メンテ理由中分類
                            if (null != aMachineMaintenanceDetail.getMainteReasonCategory2()) {
                                machineMaintenanceDetailVo.setMainteReasonCategory2("" + aMachineMaintenanceDetail.getMainteReasonCategory2());
                                machineMaintenanceDetailVo.setMainteReasonCategory2Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_remodeling_detail.remodel_reason_category2", String.valueOf(machineMaintenanceDetailVo.getMainteReasonCategory2()), loginUser.getLangId()));
                                //aVo.setMainteReasonCategory2Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setMainteReasonCategory2("");
                                machineMaintenanceDetailVo.setMainteReasonCategory2Text("");
                            }

                            //メンテ理由小分類
                            if (null != aMachineMaintenanceDetail.getMainteReasonCategory3()) {
                                machineMaintenanceDetailVo.setMainteReasonCategory3("" + aMachineMaintenanceDetail.getMainteReasonCategory3());
                                machineMaintenanceDetailVo.setMainteReasonCategory3Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_remodeling_detail.remodel_reason_category3", String.valueOf(machineMaintenanceDetailVo.getMainteReasonCategory3()), loginUser.getLangId()));
                                //aVo.setMainteReasonCategory3Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setMainteReasonCategory3("");
                                machineMaintenanceDetailVo.setMainteReasonCategory3Text("");
                            }

                            //メンテ理由	
                            machineMaintenanceDetailVo.setManiteReason(aMachineMaintenanceDetail.getManiteReason() == null ? "" : aMachineMaintenanceDetail.getManiteReason());
                            //対策指示大分類	
                            if (null != aMachineMaintenanceDetail.getMeasureDirectionCategory1()) {
                                machineMaintenanceDetailVo.setMeasureDirectionCategory1("" + aMachineMaintenanceDetail.getTaskCategory1());
                                machineMaintenanceDetailVo.setMeasureDirectionCategory1Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_remodeling_detail.remodel_direction_category1", String.valueOf(machineMaintenanceDetailVo.getMeasureDirectionCategory1()), loginUser.getLangId()));
                                //aVo.setMeasureDirectionCategory1Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setMeasureDirectionCategory1("");
                                machineMaintenanceDetailVo.setMeasureDirectionCategory1Text("");
                            }

                            //対策指示中分類
                            if (null != aMachineMaintenanceDetail.getMeasureDirectionCategory2()) {
                                machineMaintenanceDetailVo.setMeasureDirectionCategory2("" + aMachineMaintenanceDetail.getTaskCategory1());
                                machineMaintenanceDetailVo.setMeasureDirectionCategory2Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_remodeling_detail.remodel_direction_category2", String.valueOf(machineMaintenanceDetailVo.getMeasureDirectionCategory2()), loginUser.getLangId()));
                                //aVo.setMeasureDirectionCategory2Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setMeasureDirectionCategory2("");
                                machineMaintenanceDetailVo.setMeasureDirectionCategory2Text("");
                            }

                            //対策指示小分類		
                            if (null != aMachineMaintenanceDetail.getMeasureDirectionCategory3()) {
                                machineMaintenanceDetailVo.setMeasureDirectionCategory3("" + aMachineMaintenanceDetail.getTaskCategory1());
                                machineMaintenanceDetailVo.setMeasureDirectionCategory3Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_remodeling_detail.remodel_direction_category3", String.valueOf(machineMaintenanceDetailVo.getMeasureDirectionCategory3()), loginUser.getLangId()));
                                //aVo.setMeasureDirectionCategory3Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setMeasureDirectionCategory3("");
                                machineMaintenanceDetailVo.setMeasureDirectionCategory3Text("");
                            }

                            //対策指示
                            machineMaintenanceDetailVo.setMeasureDirection(aMachineMaintenanceDetail.getMeasureDirection() == null ? "" : aMachineMaintenanceDetail.getMeasureDirection());
                            //作業大分類		
                            if (null != aMachineMaintenanceDetail.getTaskCategory1()) {
                                machineMaintenanceDetailVo.setTaskCategory1("" + aMachineMaintenanceDetail.getTaskCategory1());
                                machineMaintenanceDetailVo.setTaskCategory1Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "mst_machine_inspection_item.task_category1", String.valueOf(machineMaintenanceDetailVo.getTaskCategory1()), loginUser.getLangId()));
                                //aVo.setTaskCategory1Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setTaskCategory1("");
                                machineMaintenanceDetailVo.setTaskCategory1Text("");
                            }

                            //作業中分類		
                            if (null != aMachineMaintenanceDetail.getTaskCategory2()) {
                                machineMaintenanceDetailVo.setTaskCategory2("" + aMachineMaintenanceDetail.getTaskCategory2());
                                machineMaintenanceDetailVo.setTaskCategory2Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "mst_machine_inspection_item.task_category2", String.valueOf(machineMaintenanceDetailVo.getTaskCategory2()), loginUser.getLangId()));
                                //aVo.setTaskCategory2Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setTaskCategory2("");
                                machineMaintenanceDetailVo.setTaskCategory2Text("");
                            }

                            //作業小分類	
                            if (null != aMachineMaintenanceDetail.getTaskCategory3()) {
                                machineMaintenanceDetailVo.setTaskCategory3("" + aMachineMaintenanceDetail.getTaskCategory3());
                                machineMaintenanceDetailVo.setTaskCategory3Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "mst_machine_inspection_item.task_category3", String.valueOf(machineMaintenanceDetailVo.getTaskCategory3()), loginUser.getLangId()));
                                //aVo.setTaskCategory3Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setTaskCategory3("");
                                machineMaintenanceDetailVo.setTaskCategory3Text("");
                            }

                        }else{

                            if (null != aMachineMaintenanceDetail.getMainteReasonCategory1()) {
                                //aVo.setMainteReasonCategory1(mstChoiceService.getBySeqChoice(String.valueOf(aVo.getTaskCategory1())), loginUser.getLangId(),"mst_machine_inspection_item.task_category1");
                                machineMaintenanceDetailVo.setMainteReasonCategory1("" + aMachineMaintenanceDetail.getMainteReasonCategory1());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aMachineMaintenanceDetail.getMainteReasonCategory1()), loginUser.getLangId(), "tbl_machine_maintenance_detail.mainte_reason_category1");
                                machineMaintenanceDetailVo.setMainteReasonCategory1Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setMainteReasonCategory1("");
                                machineMaintenanceDetailVo.setMainteReasonCategory1Text("");
                            }
                            //メンテ理由中分類
                            if (null != aMachineMaintenanceDetail.getMainteReasonCategory2()) {
                                machineMaintenanceDetailVo.setMainteReasonCategory2("" + aMachineMaintenanceDetail.getMainteReasonCategory2());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aMachineMaintenanceDetail.getMainteReasonCategory2()), loginUser.getLangId(), "tbl_machine_maintenance_detail.mainte_reason_category2");
                                machineMaintenanceDetailVo.setMainteReasonCategory2Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setMainteReasonCategory2("");
                                machineMaintenanceDetailVo.setMainteReasonCategory2Text("");
                            }

                            //メンテ理由小分類
                            if (null != aMachineMaintenanceDetail.getMainteReasonCategory3()) {
                                machineMaintenanceDetailVo.setMainteReasonCategory3("" + aMachineMaintenanceDetail.getMainteReasonCategory3());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aMachineMaintenanceDetail.getMainteReasonCategory3()), loginUser.getLangId(), "tbl_machine_maintenance_detail.mainte_reason_category3");
                                machineMaintenanceDetailVo.setMainteReasonCategory3Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setMainteReasonCategory3("");
                                machineMaintenanceDetailVo.setMainteReasonCategory3Text("");
                            }

                            //メンテ理由	
                            machineMaintenanceDetailVo.setManiteReason(aMachineMaintenanceDetail.getManiteReason() == null ? "" : aMachineMaintenanceDetail.getManiteReason());
                            //対策指示大分類	
                            if (null != aMachineMaintenanceDetail.getMeasureDirectionCategory1()) {
                                machineMaintenanceDetailVo.setMeasureDirectionCategory1("" + aMachineMaintenanceDetail.getMeasureDirectionCategory1());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aMachineMaintenanceDetail.getMeasureDirectionCategory1()), loginUser.getLangId(), "tbl_machine_maintenance_detail.measure_direction_category1");
                                machineMaintenanceDetailVo.setMeasureDirectionCategory1Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setMeasureDirectionCategory1("");
                                machineMaintenanceDetailVo.setMeasureDirectionCategory1Text("");
                            }

                            //対策指示中分類
                            if (null != aMachineMaintenanceDetail.getMeasureDirectionCategory2()) {
                                machineMaintenanceDetailVo.setMeasureDirectionCategory2("" + aMachineMaintenanceDetail.getMeasureDirectionCategory2());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aMachineMaintenanceDetail.getMeasureDirectionCategory2()), loginUser.getLangId(), "tbl_machine_maintenance_detail.measure_direction_category2");
                                machineMaintenanceDetailVo.setMeasureDirectionCategory2Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setMeasureDirectionCategory2("");
                                machineMaintenanceDetailVo.setMeasureDirectionCategory2Text("");
                            }

                            //対策指示小分類		
                            if (null != aMachineMaintenanceDetail.getMeasureDirectionCategory3()) {
                                machineMaintenanceDetailVo.setMeasureDirectionCategory3("" + aMachineMaintenanceDetail.getMeasureDirectionCategory3());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aMachineMaintenanceDetail.getMeasureDirectionCategory3()), loginUser.getLangId(), "tbl_machine_maintenance_detail.measure_direction_category3");
                                machineMaintenanceDetailVo.setMeasureDirectionCategory3Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setMeasureDirectionCategory3("");
                                machineMaintenanceDetailVo.setMeasureDirectionCategory3Text("");
                            }

                            //対策指示
                            machineMaintenanceDetailVo.setMeasureDirection(aMachineMaintenanceDetail.getMeasureDirection() == null ? "" : aMachineMaintenanceDetail.getMeasureDirection());
                            //作業大分類		
                            if (null != aMachineMaintenanceDetail.getTaskCategory1()) {
                                machineMaintenanceDetailVo.setTaskCategory1("" + aMachineMaintenanceDetail.getTaskCategory1());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aMachineMaintenanceDetail.getTaskCategory1()), loginUser.getLangId(), "mst_machine_inspection_item.task_category1");
                                machineMaintenanceDetailVo.setTaskCategory1Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setTaskCategory1("");
                                machineMaintenanceDetailVo.setTaskCategory1Text("");
                            }

                            //作業中分類		
                            if (null != aMachineMaintenanceDetail.getTaskCategory2()) {
                                machineMaintenanceDetailVo.setTaskCategory2("" + aMachineMaintenanceDetail.getTaskCategory2());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aMachineMaintenanceDetail.getTaskCategory2()), loginUser.getLangId(), "mst_machine_inspection_item.task_category2");
                                machineMaintenanceDetailVo.setTaskCategory2Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setTaskCategory2("");
                                machineMaintenanceDetailVo.setTaskCategory2Text("");
                            }

                            //作業小分類	
                            if (null != aMachineMaintenanceDetail.getTaskCategory3()) {
                                machineMaintenanceDetailVo.setTaskCategory3("" + aMachineMaintenanceDetail.getTaskCategory3());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aMachineMaintenanceDetail.getTaskCategory3()), loginUser.getLangId(), "mst_machine_inspection_item.task_category3");
                                machineMaintenanceDetailVo.setTaskCategory3Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineMaintenanceDetailVo.setTaskCategory3("");
                                machineMaintenanceDetailVo.setTaskCategory3Text("");
                            }
                        }
                    }
                    
//                    if (null != aMachineMaintenanceDetail.getMainteReasonCategory1()) {
//                        machineMaintenanceDetailVo.setMainteReasonCategory1("" + aMachineMaintenanceDetail.getMainteReasonCategory1());
//                        machineMaintenanceDetailVo.setMainteReasonCategory1Text(aMachineMaintenanceDetail.getMainteReasonCategory1Text());
//                    } else {
//                        machineMaintenanceDetailVo.setMainteReasonCategory1("");
//                        machineMaintenanceDetailVo.setMainteReasonCategory1Text("");
//                    }
//                    //メンテ理由中分類
//                    if (null != aMachineMaintenanceDetail.getMainteReasonCategory2()) {
//                        machineMaintenanceDetailVo.setMainteReasonCategory2("" + aMachineMaintenanceDetail.getMainteReasonCategory2());
//                        machineMaintenanceDetailVo.setMainteReasonCategory2Text(aMachineMaintenanceDetail.getMainteReasonCategory2Text());
//                    } else {
//                        machineMaintenanceDetailVo.setMainteReasonCategory2("");
//                        machineMaintenanceDetailVo.setMainteReasonCategory2Text("");
//                    }
//
//                    //メンテ理由小分類
//                    if (null != aMachineMaintenanceDetail.getMainteReasonCategory3()) {
//                        machineMaintenanceDetailVo.setMainteReasonCategory3("" + aMachineMaintenanceDetail.getMainteReasonCategory3());
//                        machineMaintenanceDetailVo.setMainteReasonCategory3Text(aMachineMaintenanceDetail.getMainteReasonCategory3Text());
//                    } else {
//                        machineMaintenanceDetailVo.setMainteReasonCategory3("");
//                        machineMaintenanceDetailVo.setMainteReasonCategory3Text("");
//                    }
//
//                    //メンテ理由	
//                    machineMaintenanceDetailVo.setManiteReason(aMachineMaintenanceDetail.getManiteReason() == null ? "" : aMachineMaintenanceDetail.getManiteReason());
//                    //対策指示大分類	
//                    if (null != aMachineMaintenanceDetail.getMeasureDirectionCategory1()) {
//                        machineMaintenanceDetailVo.setMeasureDirectionCategory1("" + aMachineMaintenanceDetail.getMeasureDirectionCategory1());
//                        machineMaintenanceDetailVo.setMeasureDirectionCategory1Text(aMachineMaintenanceDetail.getMeasureDirectionCategory1Text());
//                    } else {
//                        machineMaintenanceDetailVo.setMeasureDirectionCategory1("");
//                        machineMaintenanceDetailVo.setMeasureDirectionCategory1Text("");
//                    }
//
//                    //対策指示中分類
//                    if (null != aMachineMaintenanceDetail.getMeasureDirectionCategory2()) {
//                        machineMaintenanceDetailVo.setMeasureDirectionCategory2("" + aMachineMaintenanceDetail.getMeasureDirectionCategory2());
//                        machineMaintenanceDetailVo.setMeasureDirectionCategory2Text(aMachineMaintenanceDetail.getMeasureDirectionCategory2Text());
//                    } else {
//                        machineMaintenanceDetailVo.setMeasureDirectionCategory2("");
//                        machineMaintenanceDetailVo.setMeasureDirectionCategory2Text("");
//                    }
//
//                    //対策指示小分類		
//                    if (null != aMachineMaintenanceDetail.getMeasureDirectionCategory3()) {
//                        machineMaintenanceDetailVo.setMeasureDirectionCategory3("" + aMachineMaintenanceDetail.getMeasureDirectionCategory3());
//                        machineMaintenanceDetailVo.setMeasureDirectionCategory3Text(aMachineMaintenanceDetail.getMeasureDirectionCategory3Text());
//                    } else {
//                        machineMaintenanceDetailVo.setMeasureDirectionCategory3("");
//                        machineMaintenanceDetailVo.setMeasureDirectionCategory3Text("");
//                    }
//
//                    //対策指示
//                    machineMaintenanceDetailVo.setMeasureDirection(aMachineMaintenanceDetail.getMeasureDirection() == null ? "" : aMachineMaintenanceDetail.getMeasureDirection());
//                    //作業大分類		
//                    if (null != aMachineMaintenanceDetail.getTaskCategory1()) {
//                        machineMaintenanceDetailVo.setTaskCategory1("" + aMachineMaintenanceDetail.getTaskCategory1());
//                        machineMaintenanceDetailVo.setTaskCategory1Text(aMachineMaintenanceDetail.getTaskCategory1Text());
//                    } else {
//                        machineMaintenanceDetailVo.setTaskCategory1("");
//                        machineMaintenanceDetailVo.setTaskCategory1Text("");
//                    }
//
//                    //作業中分類		
//                    if (null != aMachineMaintenanceDetail.getTaskCategory2()) {
//                        machineMaintenanceDetailVo.setTaskCategory2("" + aMachineMaintenanceDetail.getTaskCategory2());
//                        machineMaintenanceDetailVo.setTaskCategory2Text(aMachineMaintenanceDetail.getTaskCategory2Text());
//                    } else {
//                        machineMaintenanceDetailVo.setTaskCategory2("");
//                        machineMaintenanceDetailVo.setTaskCategory2Text("");
//                    }
//
//                    //作業小分類	
//                    if (null != aMachineMaintenanceDetail.getTaskCategory3()) {
//                        machineMaintenanceDetailVo.setTaskCategory3("" + aMachineMaintenanceDetail.getTaskCategory3());
//                        machineMaintenanceDetailVo.setTaskCategory3Text(aMachineMaintenanceDetail.getTaskCategory3Text());
//                    } else {
//                        machineMaintenanceDetailVo.setTaskCategory3("");
//                        machineMaintenanceDetailVo.setTaskCategory3Text("");
//                    }

                    //作業	
                    machineMaintenanceDetailVo.setTask(aMachineMaintenanceDetail.getTask());

                    machineMaintenanceDetailVo.setSeq(aMachineMaintenanceDetail.getTblMachineMaintenanceDetailPK().getSeq());
                    
                    List<TblMachineInspectionResultVo> inspectionResult = new ArrayList<>();
                    
                    //設備点検結果　検索
                    StringBuilder sqlResult = new StringBuilder("SELECT t FROM TblMachineInspectionResult t ");
                    sqlResult.append("LEFT JOIN FETCH TblMachineMaintenanceDetail t2 ON ");
                    sqlResult.append("t.tblMachineInspectionResultPK.maintenanceDetailId = t2.id ");
                    sqlResult.append("WHERE t2.id = :id ");
                    
                    Query queryResult = entityManager.createQuery(sqlResult.toString());
                    
                    queryResult.setParameter("id", aMachineMaintenanceDetail.getId());
                    
                    List listResult = queryResult.getResultList();

                    TblMachineInspectionResultVo tblMachineInspectionResultVo = null;
                    for (int i = 0; i < listResult.size(); i++) {
                        TblMachineInspectionResult tblMachineInspectionResult = (TblMachineInspectionResult) listResult.get(i);
                        tblMachineInspectionResultVo = new TblMachineInspectionResultVo();
                        tblMachineInspectionResultVo.setId(tblMachineInspectionResult.getId());
                        tblMachineInspectionResultVo.setInspectionResult(tblMachineInspectionResult.getInspectionResult() == null ? "" : tblMachineInspectionResult.getInspectionResult());
                        tblMachineInspectionResultVo.setInspectionResultText(tblMachineInspectionResult.getInspectionResultText());
                        tblMachineInspectionResultVo.setMaintenanceDetailId(tblMachineInspectionResult.getTblMachineInspectionResultPK().getMaintenanceDetailId());
                        tblMachineInspectionResultVo.setMstMachineInspectionItemId(tblMachineInspectionResult.getTblMachineInspectionResultPK().getInspectionItemId());
                        tblMachineInspectionResultVo.setSeq(tblMachineInspectionResult.getTblMachineInspectionResultPK().getSeq());
                        if (tblMachineInspectionResult.getMstMachineInspectionItem() != null) {
                            tblMachineInspectionResultVo.setMstMachineinspectionItemName(tblMachineInspectionResult.getMstMachineInspectionItem().getInspectionItemName());
                            if (tblMachineInspectionResult.getMstMachineInspectionItem().getResultType() != null) {
                                tblMachineInspectionResultVo.setResultType(String.valueOf(tblMachineInspectionResult.getMstMachineInspectionItem().getResultType()));
                            } else {
                                tblMachineInspectionResultVo.setResultType("");
                            }
                        } else {
                            tblMachineInspectionResultVo.setMstMachineinspectionItemName("");
                            tblMachineInspectionResultVo.setResultType("");
                        }
                        inspectionResult.add(tblMachineInspectionResultVo);
                    }
                    machineMaintenanceDetailVo.setMachineInspectionResultVos(inspectionResult);
                    
                    
                    
                    List<TblMachineMaintenanceDetailImageFileVo> fileResultVos = new ArrayList<>();                    
                    //TblMachineMaintenanceDetailImageFile　検索
                    List<TblMachineMaintenanceDetailImageFile> tmpFileResults = entityManager.createQuery("SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.tblMachineMaintenanceDetailImageFilePK.maintenanceDetailId = :maintenanceDetailId ")
                            .setParameter("maintenanceDetailId", aMachineMaintenanceDetail.getId())
                            .getResultList();

                    TblMachineMaintenanceDetailImageFileVo machineMaintenanceDetailImageFileVo = null;
                    for (int i = 0; i < tmpFileResults.size(); i++) {
                        TblMachineMaintenanceDetailImageFile aFile = tmpFileResults.get(i);
                        machineMaintenanceDetailImageFileVo = new TblMachineMaintenanceDetailImageFileVo();
                        machineMaintenanceDetailImageFileVo.setMaintenanceDetailId(aMachineMaintenanceDetail.getId());
                        machineMaintenanceDetailImageFileVo.setSeq("" + aFile.getTblMachineMaintenanceDetailImageFilePK().getSeq());
                        machineMaintenanceDetailImageFileVo.setFileType("" + aFile.getFileType());
                        machineMaintenanceDetailImageFileVo.setFileExtension(aFile.getFileExtension());
                        machineMaintenanceDetailImageFileVo.setFileUuid(aFile.getFileUuid());
                        machineMaintenanceDetailImageFileVo.setRemarks(aFile.getRemarks());
                        machineMaintenanceDetailImageFileVo.setThumbnailFileUuid(aFile.getThumbnailFileUuid());
                        if (null != aFile.getTakenDate()) {
                            machineMaintenanceDetailImageFileVo.setTakenDateStr(new FileUtil().getDateTimeFormatForStr(aFile.getTakenDate()));
                        }
                        if (null != aFile.getTakenDateStz()) {
                            machineMaintenanceDetailImageFileVo.setTakenDateStzStr(new FileUtil().getDateTimeFormatForStr(aFile.getTakenDateStz()));
                        }
                        
                        fileResultVos.add(machineMaintenanceDetailImageFileVo);
                    }
                    machineMaintenanceDetailVo.setTblMachineMaintenanceDetailImageFileVos(fileResultVos);
                    
                    response.add(machineMaintenanceDetailVo);
                }
                responsMachineMaintenanceRemodelingVo.setMachineMaintenanceDetailVo(response);
            }

        } else {
            // 改造の場合
            List<TblMachineRemodelingDetail> machineRemodelingDetail = query.getResultList();

            if (null != machineRemodelingDetail && machineRemodelingDetail.size() > 0) {
                TblMachineRemodelingDetailVo machineRemodelingDetailVo;
                
                for (int j = 0; j < machineRemodelingDetail.size(); j++) {

                    machineRemodelingDetailVo = new TblMachineRemodelingDetailVo();
                    TblMachineRemodelingDetail aDetail = (TblMachineRemodelingDetail) machineRemodelingDetail.get(j);

                    // 明細部分取得
                    machineRemodelingDetailVo.setId(aDetail.getId());
//                    //改造理由大分類
                    boolean externalMoldFlag = false;
                    if(mstMachine.getMachineId() != null){
                        //String machineId = aMoldRemodelnanceRemodeling.getMstMold().getMoldId();
                        externalMoldFlag = FileUtil.checkExternal(entityManager, mstDictionaryService, machineRemodelingDetailVo.getMachineId(), loginUser).isError();
                        if(externalMoldFlag){
                            if (null != aDetail.getRemodelDirectionCategory1()) {
                                machineRemodelingDetailVo.setRemodelReasonCategory1("" + aDetail.getRemodelReasonCategory1());
                                machineRemodelingDetailVo.setRemodelReasonCategory1Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_remodeling_detail.remodel_reason_category1", String.valueOf(machineRemodelingDetailVo.getRemodelReasonCategory1()), loginUser.getLangId()));
                                //aVo.setRemodelReasonCategory1Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setRemodelReasonCategory1("");
                                machineRemodelingDetailVo.setRemodelReasonCategory1Text("");
                            }
                            //メンテ理由中分類
                            if (null != aDetail.getRemodelReasonCategory2()) {
                                machineRemodelingDetailVo.setRemodelReasonCategory2("" + aDetail.getRemodelReasonCategory2());
                                machineRemodelingDetailVo.setRemodelReasonCategory2Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_remodeling_detail.remodel_reason_category2", String.valueOf(machineRemodelingDetailVo.getRemodelReasonCategory2()), loginUser.getLangId()));
                                //aVo.setRemodelReasonCategory2Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setRemodelReasonCategory2("");
                                machineRemodelingDetailVo.setRemodelReasonCategory2Text("");
                            }

                            //メンテ理由小分類
                            if (null != aDetail.getRemodelReasonCategory3()) {
                                machineRemodelingDetailVo.setRemodelReasonCategory3("" + aDetail.getRemodelReasonCategory3());
                                machineRemodelingDetailVo.setRemodelReasonCategory3Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_remodeling_detail.remodel_reason_category3", String.valueOf(machineRemodelingDetailVo.getRemodelReasonCategory3()), loginUser.getLangId()));
                                //aVo.setRemodelReasonCategory3Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setRemodelReasonCategory3("");
                                machineRemodelingDetailVo.setRemodelReasonCategory3Text("");
                            }

                            //メンテ理由	
                            machineRemodelingDetailVo.setRemodelReason(aDetail.getRemodelReason() == null ? "" : aDetail.getRemodelReason());
                            //対策指示大分類	
                            if (null != aDetail.getRemodelDirectionCategory1()) {
                                machineRemodelingDetailVo.setRemodelDirectionCategory1("" + aDetail.getTaskCategory1());
                                machineRemodelingDetailVo.setRemodelDirectionCategory1Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_remodeling_detail.remodel_direction_category1", String.valueOf(machineRemodelingDetailVo.getRemodelDirectionCategory1()), loginUser.getLangId()));
                                //aVo.setRemodelDirectionCategory1Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setRemodelDirectionCategory1("");
                                machineRemodelingDetailVo.setRemodelDirectionCategory1Text("");
                            }

                            //対策指示中分類
                            if (null != aDetail.getRemodelDirectionCategory2()) {
                                machineRemodelingDetailVo.setRemodelDirectionCategory2("" + aDetail.getTaskCategory1());
                                machineRemodelingDetailVo.setRemodelDirectionCategory2Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_remodeling_detail.remodel_direction_category2", String.valueOf(machineRemodelingDetailVo.getRemodelDirectionCategory2()), loginUser.getLangId()));
                                //aVo.setRemodelDirectionCategory2Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setRemodelDirectionCategory2("");
                                machineRemodelingDetailVo.setRemodelDirectionCategory2Text("");
                            }

                            //対策指示小分類		
                            if (null != aDetail.getRemodelDirectionCategory3()) {
                                machineRemodelingDetailVo.setRemodelDirectionCategory3("" + aDetail.getTaskCategory1());
                                machineRemodelingDetailVo.setRemodelDirectionCategory3Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "tbl_machine_remodeling_detail.remodel_direction_category3", String.valueOf(machineRemodelingDetailVo.getRemodelDirectionCategory3()), loginUser.getLangId()));
                                //aVo.setRemodelDirectionCategory3Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setRemodelDirectionCategory3("");
                                machineRemodelingDetailVo.setRemodelDirectionCategory3Text("");
                            }

                            //対策指示
                            machineRemodelingDetailVo.setRemodelDirection(aDetail.getRemodelDirection() == null ? "" : aDetail.getRemodelDirection());
                            //作業大分類		
                            if (null != aDetail.getTaskCategory1()) {
                                machineRemodelingDetailVo.setTaskCategory1("" + aDetail.getTaskCategory1());
                                machineRemodelingDetailVo.setTaskCategory1Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "mst_machine_inspection_item.task_category1", String.valueOf(machineRemodelingDetailVo.getTaskCategory1()), loginUser.getLangId()));
                                //aVo.setTaskCategory1Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setTaskCategory1("");
                                machineRemodelingDetailVo.setTaskCategory1Text("");
                            }

                            //作業中分類		
                            if (null != aDetail.getTaskCategory2()) {
                                machineRemodelingDetailVo.setTaskCategory2("" + aDetail.getTaskCategory2());
                                machineRemodelingDetailVo.setTaskCategory2Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "mst_machine_inspection_item.task_category2", String.valueOf(machineRemodelingDetailVo.getTaskCategory2()), loginUser.getLangId()));
                                //aVo.setTaskCategory2Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setTaskCategory2("");
                                machineRemodelingDetailVo.setTaskCategory2Text("");
                            }

                            //作業小分類	
                            if (null != aDetail.getTaskCategory3()) {
                                machineRemodelingDetailVo.setTaskCategory3("" + aDetail.getTaskCategory3());
                                machineRemodelingDetailVo.setTaskCategory3Text(extMstChoiceService.getExtMstChoiceText(mstMachine.getCompanyId(), "mst_machine_inspection_item.task_category3", String.valueOf(machineRemodelingDetailVo.getTaskCategory3()), loginUser.getLangId()));
                                //aVo.setTaskCategory3Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setTaskCategory3("");
                                machineRemodelingDetailVo.setTaskCategory3Text("");
                            }

                        }else{

                            if (null != aDetail.getRemodelReasonCategory1()) {
                                //aVo.setRemodelReasonCategory1(mstChoiceService.getBySeqChoice(String.valueOf(aVo.getTaskCategory1())), loginUser.getLangId(),"mst_machine_inspection_item.task_category1");
                                machineRemodelingDetailVo.setRemodelReasonCategory1("" + aDetail.getRemodelReasonCategory1());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getRemodelReasonCategory1()), loginUser.getLangId(), "tbl_machine_remodeling_detail.remodel_reason_category1");
                                machineRemodelingDetailVo.setRemodelReasonCategory1Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setRemodelReasonCategory1("");
                                machineRemodelingDetailVo.setRemodelReasonCategory1Text("");
                            }
                            //メンテ理由中分類
                            if (null != aDetail.getRemodelReasonCategory2()) {
                                machineRemodelingDetailVo.setRemodelReasonCategory2("" + aDetail.getRemodelReasonCategory2());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getRemodelReasonCategory2()), loginUser.getLangId(), "tbl_machine_remodeling_detail.remodel_reason_category2");
                                machineRemodelingDetailVo.setRemodelReasonCategory2Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setRemodelReasonCategory2("");
                                machineRemodelingDetailVo.setRemodelReasonCategory2Text("");
                            }

                            //メンテ理由小分類
                            if (null != aDetail.getRemodelReasonCategory3()) {
                                machineRemodelingDetailVo.setRemodelReasonCategory3("" + aDetail.getRemodelReasonCategory3());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getRemodelReasonCategory3()), loginUser.getLangId(), "tbl_machine_remodeling_detail.remodel_reason_category3");
                                machineRemodelingDetailVo.setRemodelReasonCategory3Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setRemodelReasonCategory3("");
                                machineRemodelingDetailVo.setRemodelReasonCategory3Text("");
                            }

                            //メンテ理由	
                            machineRemodelingDetailVo.setRemodelReason(aDetail.getRemodelReason() == null ? "" : aDetail.getRemodelReason());
                            //対策指示大分類	
                            if (null != aDetail.getRemodelDirectionCategory1()) {
                                machineRemodelingDetailVo.setRemodelDirectionCategory1("" + aDetail.getRemodelDirectionCategory1());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getRemodelDirectionCategory1()), loginUser.getLangId(), "tbl_machine_remodeling_detail.remodel_direction_category1");
                                machineRemodelingDetailVo.setRemodelDirectionCategory1Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setRemodelDirectionCategory1("");
                                machineRemodelingDetailVo.setRemodelDirectionCategory1Text("");
                            }

                            //対策指示中分類
                            if (null != aDetail.getRemodelDirectionCategory2()) {
                                machineRemodelingDetailVo.setRemodelDirectionCategory2("" + aDetail.getRemodelDirectionCategory2());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getRemodelDirectionCategory2()), loginUser.getLangId(), "tbl_machine_remodeling_detail.remodel_direction_category2");
                                machineRemodelingDetailVo.setRemodelDirectionCategory2Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setRemodelDirectionCategory2("");
                                machineRemodelingDetailVo.setRemodelDirectionCategory2Text("");
                            }

                            //対策指示小分類		
                            if (null != aDetail.getRemodelDirectionCategory3()) {
                                machineRemodelingDetailVo.setRemodelDirectionCategory3("" + aDetail.getRemodelDirectionCategory3());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getRemodelDirectionCategory3()), loginUser.getLangId(), "tbl_machine_remodeling_detail.remodel_direction_category3");
                                machineRemodelingDetailVo.setRemodelDirectionCategory3Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setRemodelDirectionCategory3("");
                                machineRemodelingDetailVo.setRemodelDirectionCategory3Text("");
                            }

                            //対策指示
                            machineRemodelingDetailVo.setRemodelDirection(aDetail.getRemodelDirection() == null ? "" : aDetail.getRemodelDirection());
                            //作業大分類		
                            if (null != aDetail.getTaskCategory1()) {
                                machineRemodelingDetailVo.setTaskCategory1("" + aDetail.getTaskCategory1());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getTaskCategory1()), loginUser.getLangId(), "mst_machine_inspection_item.task_category1");
                                machineRemodelingDetailVo.setTaskCategory1Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setTaskCategory1("");
                                machineRemodelingDetailVo.setTaskCategory1Text("");
                            }

                            //作業中分類		
                            if (null != aDetail.getTaskCategory2()) {
                                machineRemodelingDetailVo.setTaskCategory2("" + aDetail.getTaskCategory2());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getTaskCategory2()), loginUser.getLangId(), "mst_machine_inspection_item.task_category2");
                                machineRemodelingDetailVo.setTaskCategory2Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setTaskCategory2("");
                                machineRemodelingDetailVo.setTaskCategory2Text("");
                            }

                            //作業小分類	
                            if (null != aDetail.getTaskCategory3()) {
                                machineRemodelingDetailVo.setTaskCategory3("" + aDetail.getTaskCategory3());
                                MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getTaskCategory3()), loginUser.getLangId(), "mst_machine_inspection_item.task_category3");
                                machineRemodelingDetailVo.setTaskCategory3Text(choice == null ? "" : choice.getChoice());
                            } else {
                                machineRemodelingDetailVo.setTaskCategory3("");
                                machineRemodelingDetailVo.setTaskCategory3Text("");
                            }
                        }
                    }
//                    if (aDetail.getRemodelReasonCategory1() != null) {
//                        machineRemodelingDetailVo.setRemodelReasonCategory1(String.valueOf(aDetail.getRemodelReasonCategory1()));
//                        machineRemodelingDetailVo.setRemodelReasonCategory1Text(aDetail.getRemodelReasonCategory1Text());
//                    } else {
//                        machineRemodelingDetailVo.setRemodelReasonCategory1("");
//                        machineRemodelingDetailVo.setRemodelReasonCategory1Text("");
//                    }
//                    //改造理由中分類
//                    if (aDetail.getRemodelReasonCategory2() != null) {
//                        machineRemodelingDetailVo.setRemodelReasonCategory2(String.valueOf(aDetail.getRemodelReasonCategory2()));
//                        machineRemodelingDetailVo.setRemodelReasonCategory2Text(aDetail.getRemodelReasonCategory2Text());
//                    } else {
//                        machineRemodelingDetailVo.setRemodelReasonCategory2("");
//                        machineRemodelingDetailVo.setRemodelReasonCategory2Text("");
//                    }
//
//                    //改造理由小分類
//                    if (aDetail.getRemodelReasonCategory3() != null) {
//                        machineRemodelingDetailVo.setRemodelReasonCategory3(String.valueOf(aDetail.getRemodelReasonCategory3()));
//                        machineRemodelingDetailVo.setRemodelReasonCategory3Text(aDetail.getRemodelReasonCategory3Text());
//                    } else {
//                        machineRemodelingDetailVo.setRemodelReasonCategory3("");
//                        machineRemodelingDetailVo.setRemodelReasonCategory3Text("");
//                    }
//
//                    //改造理由	
//                    machineRemodelingDetailVo.setRemodelReason(aDetail.getRemodelReason() == null ? "" : aDetail.getRemodelReason());
//                    //改造指示大分類	
//                    if (aDetail.getRemodelDirectionCategory1() != null) {
//                        machineRemodelingDetailVo.setRemodelDirectionCategory1(String.valueOf(aDetail.getRemodelDirectionCategory1()));
//                        machineRemodelingDetailVo.setRemodelDirectionCategory1Text(aDetail.getRemodelDirectionCategory1Text());
//                    } else {
//                        machineRemodelingDetailVo.setRemodelDirectionCategory1("");
//                        machineRemodelingDetailVo.setRemodelDirectionCategory1Text("");
//                    }
//
//                    //改造指示中分類
//                    if (aDetail.getRemodelDirectionCategory2() != null) {
//                        machineRemodelingDetailVo.setRemodelDirectionCategory2(String.valueOf(aDetail.getRemodelDirectionCategory2()));
//                        machineRemodelingDetailVo.setRemodelDirectionCategory2Text(aDetail.getRemodelDirectionCategory2Text());
//                    } else {
//                        machineRemodelingDetailVo.setRemodelDirectionCategory2("");
//                        machineRemodelingDetailVo.setRemodelDirectionCategory2Text("");
//                    }
//
//                    //改造指示小分類
//                    if (aDetail.getRemodelDirectionCategory3() != null) {
//                        machineRemodelingDetailVo.setRemodelDirectionCategory3(String.valueOf(aDetail.getRemodelDirectionCategory3()));
//                        machineRemodelingDetailVo.setRemodelDirectionCategory3Text(aDetail.getRemodelDirectionCategory3Text());
//                    } else {
//                        machineRemodelingDetailVo.setRemodelDirectionCategory3("");
//                        machineRemodelingDetailVo.setRemodelDirectionCategory3Text("");
//                    }
//
//                    //改造指示
//                    machineRemodelingDetailVo.setRemodelDirection(aDetail.getRemodelDirection() == null ? "" : aDetail.getRemodelDirection());
//                    //作業大分類		
//                    if (aDetail.getTaskCategory1() != null) {
//                        machineRemodelingDetailVo.setTaskCategory1(String.valueOf(aDetail.getTaskCategory1()));
//                        machineRemodelingDetailVo.setTaskCategory1Text(aDetail.getTaskCategory1Text());
//                    } else {
//                        machineRemodelingDetailVo.setTaskCategory1("");
//                        machineRemodelingDetailVo.setTaskCategory1Text("");
//                    }
//
//                    //作業中分類	
//                    if (aDetail.getTaskCategory2() != null) {
//                        machineRemodelingDetailVo.setTaskCategory2(String.valueOf(aDetail.getTaskCategory2()));
//                        machineRemodelingDetailVo.setTaskCategory2Text(aDetail.getTaskCategory2Text());
//                    } else {
//                        machineRemodelingDetailVo.setTaskCategory2("");
//                        machineRemodelingDetailVo.setTaskCategory2Text("");
//                    }
//
//                    //作業小分類	
//                    if (aDetail.getTaskCategory3() != null) {
//                        machineRemodelingDetailVo.setTaskCategory3(String.valueOf(aDetail.getTaskCategory3()));
//                        machineRemodelingDetailVo.setTaskCategory3Text(aDetail.getTaskCategory3Text());
//                    } else {
//                        machineRemodelingDetailVo.setTaskCategory3("");
//                        machineRemodelingDetailVo.setTaskCategory3Text("");
//                    }

                    //作業	
                    machineRemodelingDetailVo.setTask(aDetail.getTask() == null ? "" : aDetail.getTask());

                    machineRemodelingDetailVo.setSeq(aDetail.getTblMachineRemodelingDetailPK().getSeq());
                    
                    List<TblMachineRemodelingInspectionResultVo> remodelingInspectionResult = new ArrayList<>();
                    
                    //設備改造点検結果　検索
                    StringBuilder sqlResult = new StringBuilder("SELECT t FROM TblMachineRemodelingInspectionResult t ");
                    sqlResult.append("LEFT JOIN FETCH TblMachineRemodelingDetail t2 ON ");
                    sqlResult.append("t.tblMachineRemodelingInspectionResultPK.remodelingDetailId = t2.id ");
                    sqlResult.append("WHERE t2.id = :id ");
                    
                    Query queryResult = entityManager.createQuery(sqlResult.toString());
                    
                    queryResult.setParameter("id", aDetail.getId());
                    
                    List listResult = queryResult.getResultList();
                    
                    TblMachineRemodelingInspectionResultVo tblMachineRemodelingInspectionResultVo = null;
                    for(int i=0; i<listResult.size() ;i++){
                        TblMachineRemodelingInspectionResult tblMachineRemodelingInspectionResult = (TblMachineRemodelingInspectionResult)listResult.get(i);
                        tblMachineRemodelingInspectionResultVo = new TblMachineRemodelingInspectionResultVo();
                        tblMachineRemodelingInspectionResultVo.setId(tblMachineRemodelingInspectionResult.getId());
                        tblMachineRemodelingInspectionResultVo.setInspectionResult(tblMachineRemodelingInspectionResult.getInspectionResult() == null ? "" : tblMachineRemodelingInspectionResult.getInspectionResult());
                        tblMachineRemodelingInspectionResultVo.setInspectionResultText(tblMachineRemodelingInspectionResult.getInspectionResultText() == null ? "" : tblMachineRemodelingInspectionResult.getInspectionResultText());
                        tblMachineRemodelingInspectionResultVo.setRemodelingDetailId(tblMachineRemodelingInspectionResult.getTblMachineRemodelingInspectionResultPK().getRemodelingDetailId());
                        tblMachineRemodelingInspectionResultVo.setSeq(tblMachineRemodelingInspectionResult.getTblMachineRemodelingInspectionResultPK().getSeq());
                        tblMachineRemodelingInspectionResultVo.setMstMachineInspectionItemId(tblMachineRemodelingInspectionResult.getTblMachineRemodelingInspectionResultPK().getInspectionItemId());
                        if(tblMachineRemodelingInspectionResult.getMstMachineInspectionItem() != null){
                            tblMachineRemodelingInspectionResultVo.setMstMachineinspectionItemName(tblMachineRemodelingInspectionResult.getMstMachineInspectionItem().getInspectionItemName());
                            if(tblMachineRemodelingInspectionResult.getMstMachineInspectionItem().getResultType() != null){
                                tblMachineRemodelingInspectionResultVo.setResultType(String.valueOf(tblMachineRemodelingInspectionResult.getMstMachineInspectionItem().getResultType()));
                            }else{
                                tblMachineRemodelingInspectionResultVo.setResultType("");
                            }
                        }else{
                            tblMachineRemodelingInspectionResultVo.setMstMachineinspectionItemName("");
                            tblMachineRemodelingInspectionResultVo.setResultType("");
                        }
                        remodelingInspectionResult.add(tblMachineRemodelingInspectionResultVo);
                    }
                    
                    machineRemodelingDetailVo.setMachineRemodelingInspectionResultVo(remodelingInspectionResult);
                    
                    List<TblMachineRemodelingDetailImageFileVo> fileResultVos = new ArrayList<>();
                    //TblMachineMaintenanceDetailImageFile　検索
                    List<TblMachineRemodelingDetailImageFile> tmpFileResults = entityManager.createQuery("SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.tblMachineRemodelingDetailImageFilePK.remodelingDetailId = :remodelingDetailId ")
                            .setParameter("remodelingDetailId", aDetail.getId())
                            .getResultList();

                    TblMachineRemodelingDetailImageFileVo machineRemodelingDetailImageFileVo = null;
                    for (int i = 0; i < tmpFileResults.size(); i++) {
                        TblMachineRemodelingDetailImageFile aFile = tmpFileResults.get(i);
                        machineRemodelingDetailImageFileVo = new TblMachineRemodelingDetailImageFileVo();
                        machineRemodelingDetailImageFileVo.setRemodelingDetailId(aDetail.getId());
                        machineRemodelingDetailImageFileVo.setSeq("" + aFile.getTblMachineRemodelingDetailImageFilePK().getSeq());
                        machineRemodelingDetailImageFileVo.setFileType("" + aFile.getFileType());
                        machineRemodelingDetailImageFileVo.setFileExtension(aFile.getFileExtension());
                        machineRemodelingDetailImageFileVo.setFileUuid(aFile.getFileUuid());
                        machineRemodelingDetailImageFileVo.setRemarks(aFile.getRemarks());
                        machineRemodelingDetailImageFileVo.setThumbnailFileUuid(aFile.getThumbnailFileUuid());
                        if (null != aFile.getTakenDate()) {
                            machineRemodelingDetailImageFileVo.setTakenDateStr(new FileUtil().getDateTimeFormatForStr(aFile.getTakenDate()));
                        }
                        if (null != aFile.getTakenDateStz()) {
                            machineRemodelingDetailImageFileVo.setTakenDateStzStr(new FileUtil().getDateTimeFormatForStr(aFile.getTakenDateStz()));
                        }
                        
                        fileResultVos.add(machineRemodelingDetailImageFileVo);
                    }
                    machineRemodelingDetailVo.setMachineRemodelingDetailImageFileVos(fileResultVos);
                    
                    
                    response1.add(machineRemodelingDetailVo);

                }
                responsMachineMaintenanceRemodelingVo.setMachineRemodelingDetailVo(response1);
            }
        }

        return responsMachineMaintenanceRemodelingVo;
    }

    /**
     * 設備メンテナンス照会 設備メンテナンス改造件数取得
     *
     * @param queryVo
     * @param loginUser
     * @return
     */
    public BasicResponse getRecordCount(TblMachineMaintenanceRemodelingVo queryVo, LoginUser loginUser) {
        return queryByVo(queryVo, loginUser, "count");
    }

    /**
     * 設備メンテナンス改造情報を取得
     *
     * @param queryVo
     * @param loginUser
     * @return
     */
    public BasicResponse getMaintenanceRemodeling(TblMachineMaintenanceRemodelingVo queryVo, LoginUser loginUser) {
        return queryByVo(queryVo, loginUser, "query");
    }

    /**
     * 設備メンテナンス改造データを取得
     *
     * @param vo
     * @param loginUser
     * @param type
     * @return
     */
    public BasicResponse queryByVo(TblMachineMaintenanceRemodelingVo vo, LoginUser loginUser, String type) {
        
        StringBuilder sql = new StringBuilder("SELECT ");
        if ("count".equals(type)) {
            sql.append(" count(mr) FROM TblMachineMaintenanceRemodeling mr JOIN FETCH mr.mstMachine m ");
        } else {
            sql.append(" mr FROM TblMachineMaintenanceRemodeling mr JOIN FETCH mr.mstMachine m "
                    + "LEFT JOIN FETCH mr.mstUser "
                    + "LEFT JOIN FETCH mr.tblIssue ");
        }
        sql.append(" where 1=1 ");
        if (null != vo.getMachineId() && !vo.getMachineId().equals("")) {
            sql.append(" and m.machineId like :machineId ");
        }
        if (null != vo.getMachineName() && !vo.getMachineName().equals("")) {
            sql.append(" and m.machineName like :machineName ");
        }
        if (null != vo.getMainteDateStart()) {
            sql.append(" and mr.mainteDate >= :mainteDateStart ");
        }
        //----20170825　選択リスト「所属」の追加----
        if (null != vo.getMstMachine().getDepartment() && 0 != vo.getMstMachine().getDepartment()) {
            sql.append(" and m.department = :department ");
        }
        //-------------------------------------
        if (null != vo.getMainteDateEnd()) {
            sql.append(" and mr.mainteDate <= :mainteDateEnd ");
        }
        if (null != vo.getMainteOrRemodel() && 0 != vo.getMainteOrRemodel()) {
            sql.append(" and mr.mainteOrRemodel = :mainteOrRemodel ");
        }
        if (null != vo.getMainteStatus() && "1".equals(vo.getMainteStatus())) {
            // メンテ・改造中
            sql.append(" and (m.mainteStatus = :mainteStatus1 or m.mainteStatus = :mainteStatus2 ) and mr.endDatetime IS NULL ");
        } else if (null != vo.getMainteStatus() && "m".equals(vo.getMainteStatus())) {
            // メンテ
            sql.append(" and (m.mainteStatus = :mainteStatus1 ) and mr.endDatetime IS NULL ");
        } else if (null != vo.getMainteStatus() && "r".equals(vo.getMainteStatus())) {
            // 改造中
            sql.append(" and (m.mainteStatus = :mainteStatus2 ) and mr.endDatetime IS NULL ");
        }
        if (null != vo.getIssueId()&& !vo.getIssueId().equals("")) {
            sql.append(" and m.issueId = :issueId ");
        }
        if (null != vo.getIssueText()&& !vo.getIssueText().equals("")) {
            sql.append(" and m.issueText = :issueText ");
        }
        if (null != vo.getIssueReportCategory1Text()&& !vo.getIssueReportCategory1Text().equals("")) {
            sql.append(" and m.IssueReportCategory1Text = :IssueReportCategory1Text ");
        }
        if (null != vo.getMeasureDueDate() && !vo.getMeasureDueDate().equals("")) {
            sql.append(" and m.measureDueDate = :measureDueDate ");
        }
        if (null != vo.getReportPersonName() && !vo.getReportPersonName().equals("")) {
            sql.append(" and m.reportPersonName = :reportPersonName ");
        }
        if (vo.getOrderKey() != null && vo.getOrderKey().equals("1")) {
            sql.append(" order by mr.startDatetime asc ");//1:メンテナンス開始日時の昇順
        }
        else {
            sql.append(" order by mr.mainteDate desc, m.machineId asc ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != vo.getMachineId() && !vo.getMachineId().equals("")) {
            query.setParameter("machineId", "%" + vo.getMachineId() + "%");
        }
        if (null != vo.getMachineName() && !vo.getMachineName().equals("")) {
            query.setParameter("machineName", "%" + vo.getMachineName() + "%");
        }
        if (null != vo.getMainteDateStart()) {
            query.setParameter("mainteDateStart", vo.getMainteDateStart());
        }
        //2017.08.25
        if (null != vo.getMstMachine().getDepartment() && 0 != vo.getMstMachine().getDepartment()) {
            query.setParameter("department", vo.getMstMachine().getDepartment());
        }
        //
        if (null != vo.getMainteDateEnd()) {
            query.setParameter("mainteDateEnd", vo.getMainteDateEnd());
        }
        if (null != vo.getMainteOrRemodel() && 0 != vo.getMainteOrRemodel()) {
            query.setParameter("mainteOrRemodel", vo.getMainteOrRemodel());
        }
        if (null != vo.getIssueId()&& !vo.getIssueId().equals("")) {
            query.setParameter("issueId", "%" + vo.getIssueId()+ "%");
        }
        if (null != vo.getIssueText() && !vo.getIssueText().equals("")) {
            query.setParameter("issue", "%" + vo.getIssueText()+ "%");
        }
        if (null != vo.getIssueReportCategory1Text() && !vo.getIssueReportCategory1Text().equals("")) {
            query.setParameter("IssueReportCategory1Text", "%" + vo.getIssueReportCategory1Text()+ "%");
        }
        if (null != vo.getMeasureDueDate() && !vo.getMeasureDueDate().equals("")) {
            query.setParameter("measureDueDate", "%" + vo.getMeasureDueDate()+ "%");
        }
        if (null != vo.getReportPersonName()&& !vo.getReportPersonName().equals("")) {
            query.setParameter("reportPersonName", "%" + vo.getReportPersonName()+ "%");
        }
        if (null != vo.getMainteStatus() && "1".equals(vo.getMainteStatus())) {
            // メンテ・改造中
            query.setParameter("mainteStatus1", CommonConstants.MAINTE_STATUS_MAINTE);
            query.setParameter("mainteStatus2", CommonConstants.MAINTE_STATUS_REMODELING);
        } else if (null != vo.getMainteStatus() && "m".equals(vo.getMainteStatus())) {
            // メンテ中
            query.setParameter("mainteStatus1", CommonConstants.MAINTE_STATUS_MAINTE);
        } else if (null != vo.getMainteStatus() && "r".equals(vo.getMainteStatus())) {
            // 改造中
            query.setParameter("mainteStatus2", CommonConstants.MAINTE_STATUS_REMODELING);
        }
        
        if ("count".equals(type)) {
            CountResponse count = new CountResponse();
            count.setCount(((Long) query.getSingleResult()));
            return count;
        } else {
            
            
            TblMachineMaintenanceRemodelingVo resVo = new TblMachineMaintenanceRemodelingVo();
            List<TblMachineMaintenanceRemodelingVo> machineMaintenanceRemodelingVos = new ArrayList<>();
            List<TblMachineMaintenanceRemodeling> machineMaintenanceRemodelings = query.getResultList();
            if (null != machineMaintenanceRemodelings && !machineMaintenanceRemodelings.isEmpty()) {
                Map<String, String> mainteChoiceMap = mstChoiceService.getChoiceMap(loginUser.getLangId(), new String[]{"tbl_machine_maintenance_detail.mainte_reason_category1"});
                MstChoiceList mainteOrRemodelChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_machine_maintenance_remodeling.mainte_or_remodel");
                MstChoiceList remodelingTypeChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_machine_maintenance_remodeling.remodeling_type");
                MstChoiceList mainteTypeChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_machine_maintenance_remodeling.mainte_type");
                for (int i = 0; i < machineMaintenanceRemodelings.size(); i++) {
                    TblMachineMaintenanceRemodeling aMachineMaintenanceRemodeling = machineMaintenanceRemodelings.get(i);
                    TblMachineMaintenanceRemodelingVo aVo = new TblMachineMaintenanceRemodelingVo();
                    aVo.setId(aMachineMaintenanceRemodeling.getId() == null ? "" : aMachineMaintenanceRemodeling.getId());
                    if (aMachineMaintenanceRemodeling.getMstMachine() != null) {
                        //設備ID
                        aVo.setMachineId(aMachineMaintenanceRemodeling.getMstMachine().getMachineId());
                        //設備名称
                        aVo.setMachineName(aMachineMaintenanceRemodeling.getMstMachine().getMachineName());
                    } else {
                        //設備ID
                        aVo.setMachineId("");
                        //設備名称
                        aVo.setMachineName("");
                    }
                    if (aMachineMaintenanceRemodeling.getIssueId()!= null) {
                        //設備ID
                        aVo.setIssueId(aMachineMaintenanceRemodeling.getIssueId());
                    } else {
                        //設備ID
                        aVo.setIssueId("");
                    }
                    if (aMachineMaintenanceRemodeling.getTblIssue() != null){
                        if (aMachineMaintenanceRemodeling.getTblIssue().getIssue()!= null) {
                            //設備ID
                            aVo.setIssueText(aMachineMaintenanceRemodeling.getTblIssue().getIssue());
                        } else {
                            //設備ID
                            aVo.setIssueText("");
                        }

                        if (aMachineMaintenanceRemodeling.getTblIssue().getReportCategory1Text()!= null) {
                            //設備ID
                            aVo.setIssueReportCategory1Text(aMachineMaintenanceRemodeling.getTblIssue().getReportCategory1Text());
                        } else {
                            //設備ID
                            aVo.setIssueReportCategory1Text("");
                        }

                        if (aMachineMaintenanceRemodeling.getTblIssue().getMeasureDueDate()!= null) {
                            //設備ID
                            aVo.setMeasureDueDate(aMachineMaintenanceRemodeling.getTblIssue().getMeasureDueDate());
                        } else {
                            //設備ID
                            aVo.setMeasureDueDate(null);
                        }

                        if (aMachineMaintenanceRemodeling.getTblIssue().getReportPersonName()!= null) {
                            //設備ID
                            aVo.setReportPersonName(aMachineMaintenanceRemodeling.getTblIssue().getReportPersonName());
                        } else {
                            //設備ID
                            aVo.setReportPersonName("");
                        }
                    }
                    //メンテナンス日
                    if (null != aMachineMaintenanceRemodeling.getMainteDate()) {
                        aVo.setMainteDate(aMachineMaintenanceRemodeling.getMainteDate());
                        aVo.setMainteDateStr(DateFormat.dateFormat(aMachineMaintenanceRemodeling.getMainteDate(), loginUser.getLangId()));
                    }
                    //報告事項	
                    aVo.setReport(aMachineMaintenanceRemodeling.getReport() == null ? "" : aMachineMaintenanceRemodeling.getReport());
                    //所要時間
                    aVo.setWorkingTimeMinutes(aMachineMaintenanceRemodeling.getWorkingTimeMinutes());//20170901 Apeng add

                    //工事ID
                    aVo.setTblDirectionId(aMachineMaintenanceRemodeling.getTblDirectionId() == null ? "" : aMachineMaintenanceRemodeling.getTblDirectionId());
                    if (aMachineMaintenanceRemodeling.getTblDirection() != null) {
                        aVo.setTblDirectionCode(aMachineMaintenanceRemodeling.getTblDirection().getDirectionCode() == null ? "" : aMachineMaintenanceRemodeling.getTblDirection().getDirectionCode());
                    } else {
                        if (aMachineMaintenanceRemodeling.getTblDirectionCode() != null) {
                            aVo.setTblDirectionCode(aMachineMaintenanceRemodeling.getTblDirectionCode());
                        }
                        else {
                            aVo.setTblDirectionCode("");
                        }
                    }

                    //改造・メンテナンス区分
                    if (mainteOrRemodelChoiceList != null && mainteOrRemodelChoiceList.getMstChoice() != null) {
                        if (aMachineMaintenanceRemodeling.getMainteOrRemodel() != null) {
                            aVo.setMainteOrRemodel(aMachineMaintenanceRemodeling.getMainteOrRemodel());
                            for (int momi = 0; momi < mainteOrRemodelChoiceList.getMstChoice().size(); momi++) {
                                MstChoice aMstChoice = mainteOrRemodelChoiceList.getMstChoice().get(momi);
                                if (aMstChoice.getMstChoicePK().getSeq().equals(aMachineMaintenanceRemodeling.getMainteOrRemodel().toString())) {
                                    aVo.setMainteOrRemodelText(aMstChoice.getChoice());
                                    break;
                                }
                            }
                        } else {
                            aVo.setMainteOrRemodelText("");
                        }
                    } else {
                        aVo.setMainteOrRemodelText("");
                    }

                    // 実施者
                    if (aMachineMaintenanceRemodeling.getMstUser() != null) {
                        aVo.setReportPersonName(aMachineMaintenanceRemodeling.getMstUser().getUserName());
                    } else {
                        aVo.setReportPersonName("");
                    }
                    
                    if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", aMachineMaintenanceRemodeling.getMstMachine().getCompanyId(), loginUser).isError() == true) {
                        //外部
                        aVo.setExternalFlg("1");
                        //メンテナンス分類
                        if (null != aMachineMaintenanceRemodeling.getMainteType()) {
                            aVo.setMainteTypeText(extMstChoiceService.getExtMstChoiceText(aMachineMaintenanceRemodeling.getMstMachine().getCompanyId(), "tbl_machine_maintenance_remodeling.mainte_type", String.valueOf(aMachineMaintenanceRemodeling.getMainteType()), loginUser.getLangId()));
                        } else {
                            aVo.setMainteTypeText("");
                        }

                        //改造分類	
                        if (null != aMachineMaintenanceRemodeling.getRemodelingType()) {
                            aVo.setRemodelingTypeText(extMstChoiceService.getExtMstChoiceText(aMachineMaintenanceRemodeling.getMstMachine().getCompanyId(), "tbl_machine_maintenance_remodeling.remodeling_type", String.valueOf(aMachineMaintenanceRemodeling.getRemodelingType()), loginUser.getLangId()));
                        } else {
                            aVo.setRemodelingTypeText("");
                        }
                    } else {
                        //メンテナンス分類
                        aVo.setExternalFlg("0");
                        if (null == aMachineMaintenanceRemodeling.getMainteType()) {
                            aVo.setMainteTypeText("");
                        } else {
                            aVo.setRemodelingType(aMachineMaintenanceRemodeling.getMainteType());
                            if (mainteTypeChoiceList != null && mainteTypeChoiceList.getMstChoice() != null) {
                                for (int momi = 0; momi < mainteTypeChoiceList.getMstChoice().size(); momi++) {
                                    MstChoice aMstChoice = mainteTypeChoiceList.getMstChoice().get(momi);
                                    if (aMstChoice.getMstChoicePK().getSeq().equals(aMachineMaintenanceRemodeling.getMainteType().toString())) {
                                        aVo.setMainteTypeText(aMstChoice.getChoice());
                                        break;
                                    }
                                }
                            } else {
                                aVo.setMainteTypeText("");
                            }
                        }

                        //改造分類	
                        if (null == aMachineMaintenanceRemodeling.getRemodelingType()) {
                            aVo.setRemodelingTypeText("");
                        } else {
                            aVo.setRemodelingType(aMachineMaintenanceRemodeling.getRemodelingType());
                            if (remodelingTypeChoiceList != null && remodelingTypeChoiceList.getMstChoice() != null) {
                                for (int momi = 0; momi < remodelingTypeChoiceList.getMstChoice().size(); momi++) {
                                    MstChoice aMstChoice = remodelingTypeChoiceList.getMstChoice().get(momi);
                                    if (aMstChoice.getMstChoicePK().getSeq().equals(aMachineMaintenanceRemodeling.getRemodelingType().toString())) {
                                        aVo.setRemodelingTypeText(aMstChoice.getChoice());
                                        break;
                                    }
                                }
                            } else {
                                aVo.setRemodelingTypeText("");
                            }
                        }
                    }
                    
                    //メンテナンス明細レコードの1行目のみ追加。スマホのメンテナンス終了で表示するため
                    List<TblMachineMaintenanceDetailVo> machineMaintenanceDetailVos = new ArrayList<>();
                    if (aMachineMaintenanceRemodeling.getTblMachineMaintenanceDetailCollection() != null && 
                            aMachineMaintenanceRemodeling.getTblMachineMaintenanceDetailCollection().size() > 0) {
                        TblMachineMaintenanceDetailVo tblMachineMaintenanceDetailVo = new TblMachineMaintenanceDetailVo();
                        List<TblMachineMaintenanceDetail> resList = new ArrayList(aMachineMaintenanceRemodeling.getTblMachineMaintenanceDetailCollection());
                        // メンテ理由大分類
                        TblMachineMaintenanceDetail tblMachineMaintenanceDetail = resList.get(0);
                        tblMachineMaintenanceDetailVo.setMainteReasonCategory1Text(
                            mainteChoiceMap.get("tbl_machine_maintenance_detail.mainte_reason_category1" + String.valueOf(tblMachineMaintenanceDetail.getMainteReasonCategory1())));
                        tblMachineMaintenanceDetailVo.setMainteReasonCategory1(String.valueOf(tblMachineMaintenanceDetail.getMainteReasonCategory1()));
                        // メンテ理由
                        tblMachineMaintenanceDetailVo.setManiteReason(tblMachineMaintenanceDetail.getManiteReason());
                        machineMaintenanceDetailVos.add(tblMachineMaintenanceDetailVo);
                    }
                    aVo.setMachineMaintenanceDetailVo(machineMaintenanceDetailVos);

                    //開始日時
                    aVo.setStartDatetimeStr(new FileUtil().getDateTimeFormatForStr(aMachineMaintenanceRemodeling.getStartDatetime()));

                    //終了日時
                    if (aMachineMaintenanceRemodeling.getEndDatetime() != null) {
                        aVo.setEndDatetimeStr(aMachineMaintenanceRemodeling.getEndDatetime().compareTo(CommonConstants.SYS_MAX_DATE) == 0 ? "-" : new FileUtil().getDateTimeFormatForStr(aMachineMaintenanceRemodeling.getEndDatetime()));
                    } else {
                        aVo.setEndDatetimeStr("-");
                    }

                    machineMaintenanceRemodelingVos.add(aVo);
                }
            }

            resVo.setMachineMaintenanceRemodelingVo(machineMaintenanceRemodelingVos);
            return resVo;
        }
    }

    /**
     * 選択されているメンテナンス・改造を削除する。
     *
     * @param id
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteMaintenanceRemodeling(String id, LoginUser loginUser) {
        BasicResponse br = new BasicResponse();
        if (null != id && !id.trim().equals("")) {
            TblMachineMaintenanceRemodeling machineMaintenanceRemodeling = entityManager.find(TblMachineMaintenanceRemodeling.class, id);
            if (null != machineMaintenanceRemodeling) {
                if (null != machineMaintenanceRemodeling.getMachineSpecHstId() && !machineMaintenanceRemodeling.getMachineSpecHstId().trim().equals("")) {
                    br.setError(true);
                    br.setErrorCode(ErrorMessages.E201_APPLICATION);
                    br.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_machine_spec_registration"));
                    return br;
                }

                MstMachine machine = machineMaintenanceRemodeling.getMstMachine();
                //設備改造メンテナンステーブル  削除	
                entityManager.remove(machineMaintenanceRemodeling);

                if (machine != null) {
                    Query query = entityManager.createNamedQuery("TblMachineMaintenanceRemodeling.findMaxEndDateByMachineUuid");
                    query.setParameter("machineUuid", machine.getUuid());
                    query.setMaxResults(1);
                    
                    List<TblMachineMaintenanceRemodeling> maxEndList = query.getResultList();
                    if (maxEndList != null && !maxEndList.isEmpty()) {
                        CnfSystem cnfSystem = cnfSystemService.findByKey("system", "business_start_time");
                        Date businessDate = DateFormat.strToDate(DateFormat.getBusinessDate(DateFormat.dateToStr(maxEndList.get(0).getEndDatetime(), DateFormat.DATETIME_FORMAT), cnfSystem));
                        if (machine.getLastMainteDate() == null || (businessDate != null && machine.getLastMainteDate().compareTo(businessDate) < 0)) {
                            machine.setLastMainteDate(businessDate);
                        }
                    } else {
                        machine.setLastMainteDate(null);
                    }
                    
                    //設備マスタ    更新    改造中、メンテナンス中の場合、状態を通常に戻す
                    machine.setMainteStatus(CommonConstants.MAINTE_STATUS_NORMAL);
                    machine.setUpdateDate(new Date());
                    machine.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.merge(machine);
                }

            } else {
                br.setError(true);
                br.setErrorCode(ErrorMessages.E201_APPLICATION);
                br.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            }
        }

        return br;
    }
    
    
    /**
     * CSV出力
     *
     *
     * @param queryVo
     * @param loginUser
     * @return
     */
    public FileReponse getTblMachineMaintenanceRemodelingOutputCsv(TblMachineMaintenanceRemodelingVo queryVo, LoginUser loginUser) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        String outMachineMainteDate = mstDictionaryService.getDictionaryValue(langId, "machine_mainte_date");
        String outMainteOrRemodel = mstDictionaryService.getDictionaryValue(langId, "mainte_or_remodel");
        String outMachineId = mstDictionaryService.getDictionaryValue(langId, "machine_id");
        String outMachineName = mstDictionaryService.getDictionaryValue(langId, "machine_name");
        String outMachineInventoryPerson = mstDictionaryService.getDictionaryValue(langId, "machine_inventory_person");
        String outMachineMainteType = mstDictionaryService.getDictionaryValue(langId, "machine_mainte_type");
        String outMachineRemodelingType = mstDictionaryService.getDictionaryValue(langId, "machine_remodeling_type");
        String outMaintenanceRemodelingStartDatetime = mstDictionaryService.getDictionaryValue(langId, "maintenance_remodeling_start_datetime");
        String outMaintenanceRemodelingEndDatetime = mstDictionaryService.getDictionaryValue(langId, "maintenance_remodeling_end_datetime");
        String outMaintenanceWorkingTimeMinutes = mstDictionaryService.getDictionaryValue(langId, "maintenance_working_time_minutes");//所要時間 20170901 Apeng add
        String outMachineMaintenanceRemodelingReport = mstDictionaryService.getDictionaryValue(langId, "machine_maintenance_remodeling_report");
        String outDirectionCode = mstDictionaryService.getDictionaryValue(langId, "direction_code");

        /*Head*/
        headList.add(outMachineMainteDate);
        headList.add(outMainteOrRemodel);
        headList.add(outMachineId);
        headList.add(outMachineName);
        headList.add(outMachineInventoryPerson);
        headList.add(outMachineMainteType);
        headList.add(outMachineRemodelingType);
        headList.add(outMaintenanceRemodelingStartDatetime);
        headList.add(outMaintenanceRemodelingEndDatetime);
        headList.add(outMaintenanceWorkingTimeMinutes);//所要時間 20170901 Apeng add
        headList.add(outMachineMaintenanceRemodelingReport);
        headList.add(outDirectionCode);

        gLineList.add(headList);
        //明細データを取得
        BasicResponse br = getMaintenanceRemodeling(queryVo, loginUser);
        TblMachineMaintenanceRemodelingVo vo = (TblMachineMaintenanceRemodelingVo) br;
        List<TblMachineMaintenanceRemodelingVo> resList = vo.getMachineMaintenanceRemodelingVo();
        /*Detail*/
        for (int i = 0; i < resList.size(); i++) {
            lineList = new ArrayList();
            TblMachineMaintenanceRemodelingVo tblMachineMaintenanceRemodelingVo = resList.get(i);
            lineList.add(fu.getDateFormatForStr(tblMachineMaintenanceRemodelingVo.getMainteDate()));
            lineList.add(tblMachineMaintenanceRemodelingVo.getMainteOrRemodelText());
            lineList.add(tblMachineMaintenanceRemodelingVo.getMachineId());
            lineList.add(tblMachineMaintenanceRemodelingVo.getMachineName());
            lineList.add(tblMachineMaintenanceRemodelingVo.getReportPersonName());
            lineList.add(tblMachineMaintenanceRemodelingVo.getMainteTypeText());
            lineList.add(tblMachineMaintenanceRemodelingVo.getRemodelingTypeText());
            lineList.add((tblMachineMaintenanceRemodelingVo.getStartDatetimeStr()));
            lineList.add(tblMachineMaintenanceRemodelingVo.getEndDatetimeStr());
            lineList.add(String.valueOf(tblMachineMaintenanceRemodelingVo.getWorkingTimeMinutes()));//所要時間 20170901 Apeng add
            lineList.add(tblMachineMaintenanceRemodelingVo.getReport());
            lineList.add(tblMachineMaintenanceRemodelingVo.getTblDirectionCode());
            gLineList.add(lineList);
        }

        CSVFileUtil csvFileUtil = null;
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
        //TODO
        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MACHINE_MAINTENANCE_REMODELING);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MACHINE_MAINTENANCE_REMODELING);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, CommonConstants.TBL_MACHINE_MAINTENANCE_REMODELING);
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }
    
    
    /**
     * バッチで設備メンテナンス改造テーブルデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @param machineUuid
     * @return
     */
    public TblMachineMaintenanceRemodelingList getExtMachineMaintenanceRemodelingsByBatch(String latestExecutedDate, String machineUuid) {
        TblMachineMaintenanceRemodelingList resList = new TblMachineMaintenanceRemodelingList();
        List<TblMachineMaintenanceRemodelingVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblMachineMaintenanceRemodeling t join MstApiUser u on u.companyId = t.mstMachine.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and t.mstMachine.uuid = :machineUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMachineMaintenanceRemodeling> tmpList = query.getResultList();
        for (TblMachineMaintenanceRemodeling tblMachineMaintenanceRemodeling : tmpList) {
            TblMachineMaintenanceRemodelingVo aRes = new TblMachineMaintenanceRemodelingVo();
            tblMachineMaintenanceRemodeling.setMstMachineSpecHistory(null);
            aRes.setMachineId(tblMachineMaintenanceRemodeling.getMstMachine().getMachineId());
            tblMachineMaintenanceRemodeling.setMstMachine(null);
            tblMachineMaintenanceRemodeling.setTblIssue(null);
//            tblMachineMaintenanceRemodeling.setTblIssueCollection(null);
//            tblMachineMaintenanceRemodeling.setTblMachineMaintenanceDetailCollection(null);
//            tblMachineMaintenanceRemodeling.setTblMachineRemodelingDetailCollection(null);
            
            aRes.setTblMachineMaintenanceRemodeling(tblMachineMaintenanceRemodeling);
            resVo.add(aRes);
        }
        resList.setTblMachineMaintenanceRemodelingVos(resVo);
        return resList;
    }
    
    
    /**
     * バッチで設備メンテナンス改造テーブルデータを更新
     *
     * @param machineMaintenanceRemodelingVos
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineMaintenanceRemodelingsByBatch(List<TblMachineMaintenanceRemodelingVo> machineMaintenanceRemodelingVos) {
        BasicResponse response = new BasicResponse();

        if (machineMaintenanceRemodelingVos != null && !machineMaintenanceRemodelingVos.isEmpty()) {
            for (TblMachineMaintenanceRemodelingVo aVo : machineMaintenanceRemodelingVos) {
                if (null != aVo.getTblMachineMaintenanceRemodeling().getIssueId()) {
                    TblIssue issue = entityManager.find(TblIssue.class, aVo.getTblMachineMaintenanceRemodeling().getIssueId());
                    if (null == issue) {
                        continue;
                    }
                }

                MstMachine ownerMachine = entityManager.find(MstMachine.class, aVo.getMachineId());
                if (null != ownerMachine) {
                    List<TblMachineMaintenanceRemodeling> oldMMRs = entityManager.createQuery("SELECT t FROM TblMachineMaintenanceRemodeling t WHERE 1=1 and t.id=:id ")
                            .setParameter("id", aVo.getTblMachineMaintenanceRemodeling().getId())
                            .setMaxResults(1)
                            .getResultList();
                    TblMachineMaintenanceRemodeling newMachineMaintenanceRemodeling;
                    if (null != oldMMRs && !oldMMRs.isEmpty()) {
                        newMachineMaintenanceRemodeling = oldMMRs.get(0);
                    } else {
                        newMachineMaintenanceRemodeling = new TblMachineMaintenanceRemodeling();
                        newMachineMaintenanceRemodeling.setId(aVo.getTblMachineMaintenanceRemodeling().getId());
                    }
                    //自社の設備UUIDに変換                
                    newMachineMaintenanceRemodeling.setMachineUuid(ownerMachine.getUuid());

                    newMachineMaintenanceRemodeling.setMainteOrRemodel(aVo.getTblMachineMaintenanceRemodeling().getMainteOrRemodel());
                    newMachineMaintenanceRemodeling.setIssueId(aVo.getTblMachineMaintenanceRemodeling().getIssueId());
                    newMachineMaintenanceRemodeling.setMainteType(aVo.getTblMachineMaintenanceRemodeling().getMainteType());
                    newMachineMaintenanceRemodeling.setRemodelingType(aVo.getTblMachineMaintenanceRemodeling().getRemodelingType());
                    newMachineMaintenanceRemodeling.setMainteTypeText(aVo.getTblMachineMaintenanceRemodeling().getMainteTypeText());
                    newMachineMaintenanceRemodeling.setMainteDate(aVo.getTblMachineMaintenanceRemodeling().getMainteDate());
                    newMachineMaintenanceRemodeling.setStartDatetime(aVo.getTblMachineMaintenanceRemodeling().getStartDatetime());
                    newMachineMaintenanceRemodeling.setStartDatetimeStz(aVo.getTblMachineMaintenanceRemodeling().getStartDatetimeStz());
                    newMachineMaintenanceRemodeling.setEndDatetime(aVo.getTblMachineMaintenanceRemodeling().getEndDatetime());
                    newMachineMaintenanceRemodeling.setEndDatetimeStz(aVo.getTblMachineMaintenanceRemodeling().getEndDatetimeStz());
                    newMachineMaintenanceRemodeling.setReport(aVo.getTblMachineMaintenanceRemodeling().getReport());
                    newMachineMaintenanceRemodeling.setMachineSpecHstId(aVo.getTblMachineMaintenanceRemodeling().getMachineSpecHstId());
                    // KM-260 LYD 追加　S
                    newMachineMaintenanceRemodeling.setTblDirectionCode(aVo.getTblMachineMaintenanceRemodeling().getTblDirectionCode());
                    // KM-260 LYD 追加　E
                    newMachineMaintenanceRemodeling.setCreateDate(aVo.getTblMachineMaintenanceRemodeling().getCreateDate());
                    newMachineMaintenanceRemodeling.setCreateUserUuid(aVo.getTblMachineMaintenanceRemodeling().getCreateUserUuid());
                    newMachineMaintenanceRemodeling.setUpdateDate(new Date());
                    newMachineMaintenanceRemodeling.setUpdateUserUuid(aVo.getTblMachineMaintenanceRemodeling().getUpdateUserUuid());

                    if (null != oldMMRs && !oldMMRs.isEmpty()) {
                        entityManager.merge(newMachineMaintenanceRemodeling);
                    } else {
                        entityManager.persist(newMachineMaintenanceRemodeling);
                    }
                }
            }
        }
        response.setError(false);
        return response;
    }
    
    /**
     * 設備メンテナンス改造情報を取得
     *
     * @param queryVo
     * @param loginUser
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @return
     */
    public BasicResponse getMaintenanceRemodelingByPage(TblMachineMaintenanceRemodelingVo queryVo, LoginUser loginUser,
            String sidx, String sord, int pageNumber, int pageSize, boolean isPage) {
        
        TblMachineMaintenanceRemodelingVo resVo = new TblMachineMaintenanceRemodelingVo();

        if (isPage) {

            List count = queryByPage(queryVo, sidx, sord, pageNumber, pageSize, true);

            // ページをめぐる
            Pager pager = new Pager();
            resVo.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            resVo.setCount(counts);
            resVo.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }
        
        
        List<TblMachineMaintenanceRemodelingVo> machineMaintenanceRemodelingVos = new ArrayList<>();
        List<TblMachineMaintenanceRemodeling> machineMaintenanceRemodelings = queryByPage(queryVo, sidx, sord,
                pageNumber, pageSize, false);
        if (null != machineMaintenanceRemodelings && !machineMaintenanceRemodelings.isEmpty()) {
            MstChoiceList mainteOrRemodelChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_machine_maintenance_remodeling.mainte_or_remodel");
            MstChoiceList remodelingTypeChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_machine_maintenance_remodeling.remodeling_type");
            MstChoiceList mainteTypeChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_machine_maintenance_remodeling.mainte_type");
            for (int i = 0; i < machineMaintenanceRemodelings.size(); i++) {
                TblMachineMaintenanceRemodeling aMachineMaintenanceRemodeling = machineMaintenanceRemodelings.get(i);
                TblMachineMaintenanceRemodelingVo aVo = new TblMachineMaintenanceRemodelingVo();
                aVo.setId(aMachineMaintenanceRemodeling.getId() == null ? "" : aMachineMaintenanceRemodeling.getId());
                if (aMachineMaintenanceRemodeling.getMstMachine() != null) {
                    //設備ID
                    aVo.setMachineId(aMachineMaintenanceRemodeling.getMstMachine().getMachineId());
                    //設備名称
                    aVo.setMachineName(aMachineMaintenanceRemodeling.getMstMachine().getMachineName());
                } else {
                    //設備ID
                    aVo.setMachineId("");
                    //設備名称
                    aVo.setMachineName("");
                }

                //メンテナンス日
                if (null != aMachineMaintenanceRemodeling.getMainteDate()) {
                    aVo.setMainteDate(aMachineMaintenanceRemodeling.getMainteDate());
                    aVo.setMainteDateStr(DateFormat.dateFormat(aMachineMaintenanceRemodeling.getMainteDate(), loginUser.getLangId()));
                }
                //報告事項  
                aVo.setReport(aMachineMaintenanceRemodeling.getReport() == null ? "" : aMachineMaintenanceRemodeling.getReport());
                //所要時間
                aVo.setWorkingTimeMinutes(aMachineMaintenanceRemodeling.getWorkingTimeMinutes());//20170901 Apeng add

                //工事ID
                aVo.setTblDirectionId(aMachineMaintenanceRemodeling.getTblDirectionId() == null ? "" : aMachineMaintenanceRemodeling.getTblDirectionId());
                if (aMachineMaintenanceRemodeling.getTblDirection() != null) {
                    aVo.setTblDirectionCode(aMachineMaintenanceRemodeling.getTblDirection().getDirectionCode() == null ? "" : aMachineMaintenanceRemodeling.getTblDirection().getDirectionCode());
                } else {
                    if (aMachineMaintenanceRemodeling.getTblDirectionCode() != null) {
                        aVo.setTblDirectionCode(aMachineMaintenanceRemodeling.getTblDirectionCode());
                    }
                    else {
                        aVo.setTblDirectionCode("");
                    }
                }

                //改造・メンテナンス区分
                if (mainteOrRemodelChoiceList != null && mainteOrRemodelChoiceList.getMstChoice() != null) {
                    if (aMachineMaintenanceRemodeling.getMainteOrRemodel() != null) {
                        aVo.setMainteOrRemodel(aMachineMaintenanceRemodeling.getMainteOrRemodel());
                        for (int momi = 0; momi < mainteOrRemodelChoiceList.getMstChoice().size(); momi++) {
                            MstChoice aMstChoice = mainteOrRemodelChoiceList.getMstChoice().get(momi);
                            if (aMstChoice.getMstChoicePK().getSeq().equals(aMachineMaintenanceRemodeling.getMainteOrRemodel().toString())) {
                                aVo.setMainteOrRemodelText(aMstChoice.getChoice());
                                break;
                            }
                        }
                    } else {
                        aVo.setMainteOrRemodelText("");
                    }
                } else {
                    aVo.setMainteOrRemodelText("");
                }

                // 実施者
                if (aMachineMaintenanceRemodeling.getMstUser() != null) {
                    aVo.setReportPersonName(aMachineMaintenanceRemodeling.getMstUser().getUserName());
                } else {
                    aVo.setReportPersonName("");
                }
                
                if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", aMachineMaintenanceRemodeling.getMstMachine().getCompanyId(), loginUser).isError() == true) {
                    //外部
                    aVo.setExternalFlg("1");
                    //メンテナンス分類
                    if (null != aMachineMaintenanceRemodeling.getMainteType()) {
                        aVo.setMainteTypeText(extMstChoiceService.getExtMstChoiceText(aMachineMaintenanceRemodeling.getMstMachine().getCompanyId(), "tbl_machine_maintenance_remodeling.mainte_type", String.valueOf(aMachineMaintenanceRemodeling.getMainteType()), loginUser.getLangId()));
                    } else {
                        aVo.setMainteTypeText("");
                    }

                    //改造分類  
                    if (null != aMachineMaintenanceRemodeling.getRemodelingType()) {
                        aVo.setRemodelingTypeText(extMstChoiceService.getExtMstChoiceText(aMachineMaintenanceRemodeling.getMstMachine().getCompanyId(), "tbl_machine_maintenance_remodeling.remodeling_type", String.valueOf(aMachineMaintenanceRemodeling.getRemodelingType()), loginUser.getLangId()));
                    } else {
                        aVo.setRemodelingTypeText("");
                    }
                } else {
                    //メンテナンス分類
                    aVo.setExternalFlg("0");
                    if (null == aMachineMaintenanceRemodeling.getMainteType()) {
                        aVo.setMainteTypeText("");
                    } else {
                        aVo.setRemodelingType(aMachineMaintenanceRemodeling.getMainteType());
                        if (mainteTypeChoiceList != null && mainteTypeChoiceList.getMstChoice() != null) {
                            for (int momi = 0; momi < mainteTypeChoiceList.getMstChoice().size(); momi++) {
                                MstChoice aMstChoice = mainteTypeChoiceList.getMstChoice().get(momi);
                                if (aMstChoice.getMstChoicePK().getSeq().equals(aMachineMaintenanceRemodeling.getMainteType().toString())) {
                                    aVo.setMainteTypeText(aMstChoice.getChoice());
                                    break;
                                }
                            }
                        } else {
                            aVo.setMainteTypeText("");
                        }
                    }

                    //改造分類  
                    if (null == aMachineMaintenanceRemodeling.getRemodelingType()) {
                        aVo.setRemodelingTypeText("");
                    } else {
                        aVo.setRemodelingType(aMachineMaintenanceRemodeling.getRemodelingType());
                        if (remodelingTypeChoiceList != null && remodelingTypeChoiceList.getMstChoice() != null) {
                            for (int momi = 0; momi < remodelingTypeChoiceList.getMstChoice().size(); momi++) {
                                MstChoice aMstChoice = remodelingTypeChoiceList.getMstChoice().get(momi);
                                if (aMstChoice.getMstChoicePK().getSeq().equals(aMachineMaintenanceRemodeling.getRemodelingType().toString())) {
                                    aVo.setRemodelingTypeText(aMstChoice.getChoice());
                                    break;
                                }
                            }
                        } else {
                            aVo.setRemodelingTypeText("");
                        }
                    }
                }

                //開始日時
                aVo.setStartDatetimeStr(new FileUtil().getDateTimeFormatForStr(aMachineMaintenanceRemodeling.getStartDatetime()));

                //終了日時
                if (aMachineMaintenanceRemodeling.getEndDatetime() != null) {
                    aVo.setEndDatetimeStr(aMachineMaintenanceRemodeling.getEndDatetime().compareTo(CommonConstants.SYS_MAX_DATE) == 0 ? "-" : new FileUtil().getDateTimeFormatForStr(aMachineMaintenanceRemodeling.getEndDatetime()));
                } else {
                    aVo.setEndDatetimeStr("-");
                }
                aVo.setIssueId(aMachineMaintenanceRemodeling.getIssueId());
                machineMaintenanceRemodelingVos.add(aVo);
            }
        }

        resVo.setMachineMaintenanceRemodelingVo(machineMaintenanceRemodelingVos);
        
        return resVo;
    }
    
    /**
     * 設備メンテナンス改造データを取得
     *
     * @param vo
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @return
     */
    public List queryByPage(TblMachineMaintenanceRemodelingVo vo, String sidx, String sord, int pageNumber, int pageSize,
            boolean isCount) {

        StringBuilder sql = new StringBuilder("SELECT ");
        if (isCount) {
            sql.append(" count(mr) FROM TblMachineMaintenanceRemodeling mr JOIN FETCH mr.mstMachine m ");
        } else {
            sql.append(" mr FROM TblMachineMaintenanceRemodeling mr JOIN FETCH mr.mstMachine m "
                    + "LEFT JOIN FETCH mr.mstUser mu ");
        }
        sql.append(" where 1=1 ");
        if (null != vo.getMachineId() && !vo.getMachineId().equals("")) {
            sql.append(" and m.machineId like :machineId ");
        }
        if (null != vo.getMachineName() && !vo.getMachineName().equals("")) {
            sql.append(" and m.machineName like :machineName ");
        }
        if (null != vo.getMachineUuid() && !vo.getMachineUuid().equals("")) {
            sql.append(" and mr.mstMachine.uuid = :machineUuid ");
        }
        if (null != vo.getMainteDateStart()) {
            sql.append(" and mr.mainteDate >= :mainteDateStart ");
        }
        //----20170825　選択リスト「所属」の追加----
        if (null != vo.getMstMachine().getDepartment() && 0 != vo.getMstMachine().getDepartment()) {
            sql.append(" and m.department = :department ");
        }
        //-------------------------------------
        if (null != vo.getMainteDateEnd()) {
            sql.append(" and mr.mainteDate <= :mainteDateEnd ");
        }
        if (null != vo.getMainteOrRemodel() && 0 != vo.getMainteOrRemodel()) {
            sql.append(" and mr.mainteOrRemodel = :mainteOrRemodel ");
        }
        if (null != vo.getMainteStatus() && "1".equals(vo.getMainteStatus())) {
            // メンテ・改造中
            sql.append(" and (m.mainteStatus = :mainteStatus1 or m.mainteStatus = :mainteStatus2 ) and mr.endDatetime IS NULL ");
        } else if (null != vo.getMainteStatus() && "m".equals(vo.getMainteStatus())) {
            // メンテ
            sql.append(" and (m.mainteStatus = :mainteStatus1 ) and mr.endDatetime IS NULL ");
        } else if (null != vo.getMainteStatus() && "r".equals(vo.getMainteStatus())) {
            // 改造中
            sql.append(" and (m.mainteStatus = :mainteStatus2 ) and mr.endDatetime IS NULL ");
        }
        
        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                sql.append(sortStr);

            } else {

                sql.append(" order by mr.mainteDate desc, m.machineId asc ");

            }

        }
        
        Query query = entityManager.createQuery(sql.toString());
        if (null != vo.getMachineId() && !vo.getMachineId().equals("")) {
            query.setParameter("machineId", "%" + vo.getMachineId() + "%");
        }
        if (null != vo.getMachineName() && !vo.getMachineName().equals("")) {
            query.setParameter("machineName", "%" + vo.getMachineName() + "%");
        }
        if (null != vo.getMachineUuid() && !vo.getMachineUuid().equals("")) {
            query.setParameter("machineUuid", vo.getMachineUuid());
        }
        if (null != vo.getMainteDateStart()) {
            query.setParameter("mainteDateStart", vo.getMainteDateStart());
        }
        //2017.08.25
        if (null != vo.getMstMachine().getDepartment() && 0 != vo.getMstMachine().getDepartment()) {
            query.setParameter("department", vo.getMstMachine().getDepartment());
        }
        //
        if (null != vo.getMainteDateEnd()) {
            query.setParameter("mainteDateEnd", vo.getMainteDateEnd());
        }
        if (null != vo.getMainteOrRemodel() && 0 != vo.getMainteOrRemodel()) {
            query.setParameter("mainteOrRemodel", vo.getMainteOrRemodel());
        }
        if (null != vo.getMainteStatus() && "1".equals(vo.getMainteStatus())) {
            // メンテ・改造中
            query.setParameter("mainteStatus1", CommonConstants.MAINTE_STATUS_MAINTE);
            query.setParameter("mainteStatus2", CommonConstants.MAINTE_STATUS_REMODELING);
        } else if (null != vo.getMainteStatus() && "m".equals(vo.getMainteStatus())) {
            // メンテ中
            query.setParameter("mainteStatus1", CommonConstants.MAINTE_STATUS_MAINTE);
        } else if (null != vo.getMainteStatus() && "r".equals(vo.getMainteStatus())) {
            // 改造中
            query.setParameter("mainteStatus2", CommonConstants.MAINTE_STATUS_REMODELING);
        }
        
        // 画面改ページを設定する
        if (!isCount) {
            Pager pager = new Pager();
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }

        List list = query.getResultList();

        return list;

    }
}
