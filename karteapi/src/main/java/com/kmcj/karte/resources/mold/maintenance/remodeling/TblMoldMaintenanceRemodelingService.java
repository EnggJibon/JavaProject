/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.remodeling;

import com.kmcj.karte.resources.mold.part.rel.MstMoldPartDetailMaintenance;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.batch.externalmold.choice.ExtMstChoiceService;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;

import com.kmcj.karte.properties.KartePropertyService;

import com.kmcj.karte.constants.ErrorMessages;

import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;

import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;

import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.daily.report.TblMachineDailyReportService;

import com.kmcj.karte.resources.mold.issue.TblIssue;
import com.kmcj.karte.resources.mold.issue.TblIssueVo;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.inspection.result.TblMoldInspectionResult;
import com.kmcj.karte.resources.mold.inspection.result.TblMoldInspectionResultVo;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetail;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetailImageFile;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetailImageFileVo;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetailVo;
import com.kmcj.karte.resources.mold.maintenance.part.TblMoldMaintenancePart;
import com.kmcj.karte.resources.mold.maintenance.part.TblMoldMaintencePartVo;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRel;
//import com.kmcj.karte.resources.mold.part.stock.MoldPartStockService;
import com.kmcj.karte.resources.mold.part.stock.inout.MoldPartStockInOutService;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetail;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailImageFile;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailImageFileVo;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailVo;
import com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResult;
import com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResultVo;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistory;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import com.kmcj.karte.util.TimezoneConverter;
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
 * @author admin
 */
@Dependent
@Transactional
public class TblMoldMaintenanceRemodelingService {

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
    private TblMachineDailyReportService tblMachineDailyReportService;
    
    @Inject
    private CnfSystemService cnfSystemService;
    
    @Inject
    private MoldPartStockInOutService moldPartStockInOutService;
    
    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("mainteDate", " ORDER BY mr.mainteDate ");// 金型メンテナンス日
        orderKey.put("mainteOrRemodelText", " ORDER BY mr.mainteOrRemodel ");// 改造・メンテナンス区分
        orderKey.put("moldId", " ORDER BY mstMold.moldId ");// 金型ＩＤ
        orderKey.put("moldName", " ORDER BY mstMold.moldName ");// 金型名称
        orderKey.put("reportPersonName", " ORDER BY mu.userName ");// 実施者
        orderKey.put("mainteTypeText", " ORDER BY mr.mainteType ");// メンテナンス分類
        orderKey.put("remodelingTypeText", " ORDER BY mr.remodelingType ");// 改造分類
        orderKey.put("startDatetime", " ORDER BY mr.startDatetime ");// 開始日時
        orderKey.put("endDatetime", " ORDER BY mr.endDatetime ");// 終了日時
        orderKey.put("workingTimeMinutes", " ORDER BY mr.workingTimeMinutes ");// 所要時間
        orderKey.put("report", " ORDER BY mr.report ");// 報告事項
        orderKey.put("directionCode", " ORDER BY mr.directionCode ");// 手配・工事番号
        orderKey.put("issueId", "ORDER BY mr.issueId ");//関連issue有無
       
    }

    /**
     *
     * @param issueVo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postMoldMaintenanceStart(TblIssueVo issueVo, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        String moldUuid = issueVo.getMoldUuid();
        Query queryMold = entityManager.createNamedQuery("MstMold.findByUuid");
        queryMold.setParameter("uuid", moldUuid);

        try {
            MstMold mold = (MstMold) queryMold.getSingleResult();
            String moldId = issueVo.getMoldId();
            
            //外部データチェック
            basicResponse = FileUtil.checkExternal(entityManager,mstDictionaryService,moldId, loginUser);
            if(basicResponse.isError()){
                return basicResponse;
            }
            
            //メンテイ状態チェック
            if (null != mold.getMainteStatus() && mold.getMainteStatus() == CommonConstants.MAINTEORREMODEL_MAINTE) {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_under_maintenance"));
                return basicResponse;
            }else if(null != mold.getMainteStatus() && mold.getMainteStatus() == CommonConstants.MAINTEORREMODEL_REMODEL){
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_remodeling"));
                return basicResponse;
            }

            //金型マスタ 更新 状態をメンテナンス中にする
            mold.setMainteStatus(CommonConstants.MAINTE_STATUS_MAINTE);//メンテナンス中
            mold.setUpdateDate(new Date());
            mold.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(mold);
        } catch (NoResultException e) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return basicResponse;
        }
        

        //金型改造メンテナンス 追加
        TblMoldMaintenanceRemodeling aMoldMaintenanceRemodeling = new TblMoldMaintenanceRemodeling();
        aMoldMaintenanceRemodeling.setId(IDGenerator.generate());
        aMoldMaintenanceRemodeling.setMainteOrRemodel(CommonConstants.MAINTEORREMODEL_MAINTE);
        aMoldMaintenanceRemodeling.setCreateDate(new Date());
        aMoldMaintenanceRemodeling.setCreateUserUuid(loginUser.getUserUuid());
        aMoldMaintenanceRemodeling.setMainteDate(new Date());
        if (null != issueVo.getId() && !"".equals(issueVo.getId().trim())) {
            aMoldMaintenanceRemodeling.setIssueId(issueVo.getId());
        }

        aMoldMaintenanceRemodeling.setUpdateDate(new Date());
        aMoldMaintenanceRemodeling.setUpdateUserUuid(loginUser.getUserUuid());
        aMoldMaintenanceRemodeling.setMoldUuid(moldUuid);

        aMoldMaintenanceRemodeling.setStartDatetime(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
        aMoldMaintenanceRemodeling.setStartDatetimeStz(new Date());
        entityManager.persist(aMoldMaintenanceRemodeling);

        //異常テーブル 更新 異常データが選択されたとき
        if (null != issueVo.getId() && !"".equals(issueVo.getId().trim())) {
            TblIssue issue = entityManager.find(TblIssue.class, issueVo.getId());
            issue.setMainTenanceId(aMoldMaintenanceRemodeling.getId());
            issue.setMeasureStatus(CommonConstants.ISSUE_MEASURE_STATUS_RESOLVING);
            issue.setUpdateDate(new Date());
            issue.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(issue);
        }

        basicResponse.setError(false);
        basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
        basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return basicResponse;
    }

    /**
     *
     * @param TblMoldMaintenanceRemodelingVo
     * @param loginUser
     * @return
     */
    @Transactional
    public TblMoldMaintenanceRemodelingVo postMoldMaintenanceStart2(TblMoldMaintenanceRemodelingVo tblMoldMaintenanceRemodelingVo, LoginUser loginUser) {
        TblMoldMaintenanceRemodelingVo result = new TblMoldMaintenanceRemodelingVo();
        
        String moldUuid = tblMoldMaintenanceRemodelingVo.getMoldUuid();
        Query queryMold = entityManager.createNamedQuery("MstMold.findByUuid");
        queryMold.setParameter("uuid", moldUuid);

        try {
            MstMold mold = (MstMold) queryMold.getSingleResult();
            String moldId = tblMoldMaintenanceRemodelingVo.getMoldId();
            
            //外部データチェック
            BasicResponse basicResponse = FileUtil.checkExternal(entityManager,mstDictionaryService,moldId, loginUser);
            if(basicResponse.isError()){
                result.setError(basicResponse.isError());
                result.setErrorCode(basicResponse.getErrorCode());
                result.setErrorMessage(basicResponse.getErrorMessage());
                return result;
            }
            
            //メンテイ状態チェック
            if (null != mold.getMainteStatus() && mold.getMainteStatus() == CommonConstants.MAINTEORREMODEL_MAINTE) {
                result.setError(true);
                result.setErrorCode(ErrorMessages.E201_APPLICATION);
                result.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_under_maintenance"));
                return result;
            }else if(null != mold.getMainteStatus() && mold.getMainteStatus() == CommonConstants.MAINTEORREMODEL_REMODEL){
                result.setError(true);
                result.setErrorCode(ErrorMessages.E201_APPLICATION);
                result.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_remodeling"));
                return result;
            }

            //金型マスタ 更新 状態をメンテナンス中にする
            mold.setMainteStatus(CommonConstants.MAINTE_STATUS_MAINTE);//メンテナンス中
            mold.setUpdateDate(new Date());
            mold.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(mold);
        } catch (NoResultException e) {
            result.setError(true);
            result.setErrorCode(ErrorMessages.E201_APPLICATION);
            result.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found"));
            return result;
        }
        

        //金型改造メンテナンス 追加
        TblMoldMaintenanceRemodeling aMoldMaintenanceRemodeling = new TblMoldMaintenanceRemodeling();
        String maintenanceRemodelingId = IDGenerator.generate();
        aMoldMaintenanceRemodeling.setId(maintenanceRemodelingId);
        aMoldMaintenanceRemodeling.setMainteOrRemodel(CommonConstants.MAINTEORREMODEL_MAINTE);
        aMoldMaintenanceRemodeling.setMainteType(tblMoldMaintenanceRemodelingVo.getMainteType());
        aMoldMaintenanceRemodeling.setDirectionId(tblMoldMaintenanceRemodelingVo.getDirectionId());
        if (tblMoldMaintenanceRemodelingVo.getDirectionId() == null && tblMoldMaintenanceRemodelingVo.getDirectionCode() != null) {
            //手配工事番号をテーブル参照しない設定のときはIDがNULLでコードに値が入った状態でリクエストされるので、コードを直接保存する
            aMoldMaintenanceRemodeling.setDirectionCode(tblMoldMaintenanceRemodelingVo.getDirectionCode());
        }
        aMoldMaintenanceRemodeling.setCreateDate(new Date());
        aMoldMaintenanceRemodeling.setCreateUserUuid(loginUser.getUserUuid());
        aMoldMaintenanceRemodeling.setMainteDate(new Date());
        if (null != tblMoldMaintenanceRemodelingVo.getIssueId() && !"".equals(tblMoldMaintenanceRemodelingVo.getIssueId().trim())) {
            aMoldMaintenanceRemodeling.setIssueId(tblMoldMaintenanceRemodelingVo.getIssueId());
        }

        aMoldMaintenanceRemodeling.setUpdateDate(new Date());
        aMoldMaintenanceRemodeling.setUpdateUserUuid(loginUser.getUserUuid());
        aMoldMaintenanceRemodeling.setMoldUuid(moldUuid);

        aMoldMaintenanceRemodeling.setStartDatetime(DateFormat.strToDatetime(tblMoldMaintenanceRemodelingVo.getStartDatetime()));
        aMoldMaintenanceRemodeling.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), DateFormat.strToDatetime(tblMoldMaintenanceRemodelingVo.getStartDatetime())));
        entityManager.persist(aMoldMaintenanceRemodeling);

        //異常テーブル 更新 異常データが選択されたとき
        if (null != tblMoldMaintenanceRemodelingVo.getIssueId() && !"".equals(tblMoldMaintenanceRemodelingVo.getIssueId().trim())) {
            TblIssue issue = entityManager.find(TblIssue.class, tblMoldMaintenanceRemodelingVo.getIssueId());
            issue.setMainTenanceId(aMoldMaintenanceRemodeling.getId());
            issue.setMeasureStatus(CommonConstants.ISSUE_MEASURE_STATUS_RESOLVING);
            issue.setUpdateDate(new Date());
            issue.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(issue);
        }
        result.setId(maintenanceRemodelingId);
        result.setError(false);
        result.setErrorCode(ErrorMessages.E201_APPLICATION);
        result.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return result;
    }
    
    /**
     * TT0008 金型改造開始入力 金型改造データベースを更新する
     *
     * @param tblMoldMaintenanceRemodelingVo
     * @param user
     * @return 
     */
    @Transactional
    public TblMoldMaintenanceRemodelingVo changeMstMoldMainteStatus(TblMoldMaintenanceRemodelingVo tblMoldMaintenanceRemodelingVo, LoginUser user) {
        TblMoldMaintenanceRemodelingVo basicResponse = new TblMoldMaintenanceRemodelingVo();
        
        String moldId = tblMoldMaintenanceRemodelingVo.getMoldId();
        String moldUuid = tblMoldMaintenanceRemodelingVo.getMoldUuid();
        
        Query queryMold = entityManager.createNamedQuery("MstMold.findByMoldId");
        queryMold.setParameter("moldId", moldId);

        try {
            MstMold mold = (MstMold) queryMold.getSingleResult();
            mold.setMainteStatus(CommonConstants.MAINTE_STATUS_REMODELING);
            mold.setUpdateDate(new Date());
            mold.setUpdateUserUuid(user.getUserUuid());
            entityManager.merge(mold);
        } catch (NoResultException e) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(user.getLangId(), "mst_error_record_not_found"));
            return basicResponse;
        }
        

        //追加金型改造
        TblMoldMaintenanceRemodeling tblMoldMaintenanceRemodeling = new TblMoldMaintenanceRemodeling();
        String maintenanceRemodelingId = IDGenerator.generate();
        tblMoldMaintenanceRemodeling.setId(maintenanceRemodelingId);
        tblMoldMaintenanceRemodeling.setMainteOrRemodel(CommonConstants.MAINTE_STATUS_REMODELING);
        tblMoldMaintenanceRemodeling.setRemodelingType(tblMoldMaintenanceRemodelingVo.getRemodelingType());
        tblMoldMaintenanceRemodeling.setDirectionId(tblMoldMaintenanceRemodelingVo.getDirectionId());
        if (tblMoldMaintenanceRemodelingVo.getDirectionId() == null && tblMoldMaintenanceRemodelingVo.getDirectionCode() != null) {
            //システム設定により手配・工事テーブルを参照しない場合、IDではなく手配・工事番号を直接保存
            tblMoldMaintenanceRemodeling.setDirectionCode(tblMoldMaintenanceRemodelingVo.getDirectionCode());
        }
        tblMoldMaintenanceRemodeling.setMoldUuid(moldUuid);
        tblMoldMaintenanceRemodeling.setMainteDate(new java.util.Date());
        tblMoldMaintenanceRemodeling.setStartDatetime(TimezoneConverter.getLocalTime(user.getJavaZoneId()));        
        tblMoldMaintenanceRemodeling.setStartDatetimeStz(new Date());
        tblMoldMaintenanceRemodeling.setCreateDate(new Date());
        tblMoldMaintenanceRemodeling.setCreateUserUuid(user.getUserUuid());
        tblMoldMaintenanceRemodeling.setUpdateDate(new Date());
        tblMoldMaintenanceRemodeling.setUpdateUserUuid(user.getUserUuid());
        entityManager.persist(tblMoldMaintenanceRemodeling);
        
        basicResponse.setId(maintenanceRemodelingId);
        return basicResponse;
    }
    /**
     * TT0008 金型改造開始入力 金型の状態がすでに改造中のとき確認します
     *
     * @param moldId
     * @return
     */
    public int getMoldMainteStatus(String moldId) {
        Query query = entityManager.createNamedQuery("MstMold.findByMoldId");
        query.setParameter("moldId", moldId);
        try {
            MstMold mstMold = (MstMold) query.getSingleResult();
            return mstMold.getMainteStatus() == null ? 0 : mstMold.getMainteStatus();
        } catch (NoResultException e) {
            return 0;
        }
    }

    /**
     * TT0007 選択されているメンテナンス・改造を削除する。
     *
     * @param id
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteMaintenanceRemodeling(String id, LoginUser loginUser) {
        BasicResponse br = new BasicResponse();
        if (null != id && !id.trim().equals("")) {
            TblMoldMaintenanceRemodeling moldMaintenanceRemodeling = entityManager.find(TblMoldMaintenanceRemodeling.class, id);
            if (null != moldMaintenanceRemodeling) {
                if (null != moldMaintenanceRemodeling.getMoldSpecHstIdStr() && !moldMaintenanceRemodeling.getMoldSpecHstIdStr().trim().equals("")) {
                    br.setError(true);
                    br.setErrorCode(ErrorMessages.E201_APPLICATION);
                    br.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_mold_spec_registration"));
                    return br;
                }
                
                //金型部品在庫の調整
                moldPartStockInOutService.cancelMaintenance(id, loginUser);
                
                MstMold mold = moldMaintenanceRemodeling.getMstMold();
                //金型改造メンテナンステーブル  削除	
                entityManager.remove(moldMaintenanceRemodeling);
                
                // 該当金型の最大終了日時取得
                if (mold != null) {
                    Query query = entityManager.createNamedQuery("TblMoldMaintenanceRemodeling.findMaxEndDateByMoldUuid");
                    query.setParameter("moldUuid", mold.getUuid());
                    query.setMaxResults(1);
                    
                    List<TblMoldMaintenanceRemodeling> maxEndList = query.getResultList();
                    if (maxEndList != null && !maxEndList.isEmpty()) {
                        CnfSystem cnfSystem = cnfSystemService.findByKey("system", "business_start_time");
                        Date businessDate = DateFormat.strToDate(DateFormat.getBusinessDate(DateFormat.dateToStr(maxEndList.get(0).getEndDatetime(), DateFormat.DATETIME_FORMAT), cnfSystem));
                        if (mold.getLastMainteDate() == null || (businessDate != null && mold.getLastMainteDate().compareTo(businessDate) < 0)) {
                            mold.setLastMainteDate(businessDate);
                        }
                    } else {
                        mold.setLastMainteDate(null);
                    }
                    
                    //金型マスタ    更新    改造中、メンテナンス中の場合、状態を通常に戻す
                    mold.setMainteStatus(CommonConstants.MAINTE_STATUS_NORMAL);
                    mold.setUpdateDate(new Date());
                    mold.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.merge(mold);
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
     *
     * @param id
     * @return
     */
    public boolean getRemodelingExistCheck(String id) {
        Query query = entityManager.createNamedQuery("TblMoldMaintenanceRemodeling.findById");
        query.setParameter("id", id);
        try {
            query.getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public BasicResponse getRecordCount(TblMoldMaintenanceRemodelingVo queryVo, LoginUser loginUser) {
        return queryByVo(queryVo, loginUser, "count");
    }

    public BasicResponse getMaintenanceRemodeling(TblMoldMaintenanceRemodelingVo queryVo, LoginUser loginUser) {
        return queryByVo(queryVo, loginUser, "query");
    }

    public BasicResponse queryByVo(TblMoldMaintenanceRemodelingVo vo, LoginUser loginUser, String type) {
        
        StringBuilder sql = new StringBuilder(" SELECT ");
        if ("count".equals(type)) {
            sql.append(" count(mr) FROM TblMoldMaintenanceRemodeling mr JOIN FETCH mr.mstMold mstMold ");
        } else {
            sql.append(" mr FROM TblMoldMaintenanceRemodeling mr JOIN FETCH mr.mstMold mstMold "
                    + "LEFT JOIN FETCH mr.mstUser " 
                    + "LEFT JOIN FETCH mr.tblIssue ");
        }
        sql.append(" where 1=1 ");
        if (null != vo.getMoldId() && !vo.getMoldId().equals("")) {
            sql.append(" and mr.mstMold.moldId like :moldId ");
        }
        if (null != vo.getMoldName() && !vo.getMoldName().equals("")) {
            sql.append(" and mr.mstMold.moldName like :moldName ");
        }
        if (null != vo.getMainteDateStart()) {
            sql.append(" and mr.mainteDate >= :mainteDateStart ");
        }
        //所属のついか--------------------------------
        if (null != vo.getMstMold().getDepartment() && 0 != vo.getMstMold().getDepartment()) {
            sql.append(" and mstMold.department = :department ");
        }
        //-------------------------------------------
        if (null != vo.getMainteDateEnd()) {
            sql.append(" and mr.mainteDate <= :mainteDateEnd ");
        }
        if (null != vo.getMainteOrRemodel() && 0 != vo.getMainteOrRemodel()) {
            sql.append(" and mr.mainteOrRemodel = :mainteOrRemodel ");
        }
        if (null != vo.getMainteStatus() && "1".equals(vo.getMainteStatus())) {
            // メンテ・改造中
            sql.append(" and (mr.mstMold.mainteStatus = :mainteStatus1 or mr.mstMold.mainteStatus = :mainteStatus2 ) and mr.endDatetime IS NULL ");
        } else if (null != vo.getMainteStatus() && "m".equals(vo.getMainteStatus())) {
            // メンテ
            sql.append(" and (mr.mstMold.mainteStatus = :mainteStatus1 ) and mr.endDatetime IS NULL ");
        } else if (null != vo.getMainteStatus() && "r".equals(vo.getMainteStatus())) {
            // 改造中
            sql.append(" and (mr.mstMold.mainteStatus = :mainteStatus2 ) and mr.endDatetime IS NULL ");
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

        if (null != vo.getStartDatetime() && "1".equals(vo.getStartDatetime())) {
            sql.append(" order by mr.startDatetime asc ");//1:金型メンテナンス開始日時の昇順
        } else {
            sql.append(" order by mr.mainteDate desc,mr.startDatetime desc,mr.mstMold.moldId asc ");
        }

        Query query = entityManager.createQuery(sql.toString());
        if (null != vo.getMoldId() && !vo.getMoldId().equals("")) {
            query.setParameter("moldId", "%" + vo.getMoldId() + "%");
        }
        if (null != vo.getMoldName() && !vo.getMoldName().equals("")) {
            query.setParameter("moldName", "%" + vo.getMoldName() + "%");
        }
        if (null != vo.getMainteDateStart()) {
            query.setParameter("mainteDateStart", vo.getMainteDateStart());
        }
        //2017.08.25所属のついか--------------------------------
        if (null != vo.getMstMold().getDepartment() && 0 != vo.getMstMold().getDepartment()) {
            query.setParameter("department", vo.getMstMold().getDepartment());
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
            // メンテ
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
            
            TblMoldMaintenanceRemodelingVo resVo = new TblMoldMaintenanceRemodelingVo();
            List<TblMoldMaintenanceRemodelingVo> moldMaintenanceRemodelingVos = new ArrayList<>();
            List<TblMoldMaintenanceRemodeling> moldMaintenanceRemodelings = query.getResultList();
            if (null != moldMaintenanceRemodelings && !moldMaintenanceRemodelings.isEmpty()) {
                MstChoiceList mainteOrRemodelChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_mold_maintenance_remodeling.mainte_or_remodel");
                MstChoiceList remodelingTypeChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_mold_maintenance_remodeling.remodeling_type");
                MstChoiceList mainteTypeChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_mold_maintenance_remodeling.mainte_type");
                for (int i = 0; i < moldMaintenanceRemodelings.size(); i++) {
                    TblMoldMaintenanceRemodeling aMoldMaintenanceRemodeling = moldMaintenanceRemodelings.get(i);
                    TblMoldMaintenanceRemodelingVo aVo = new TblMoldMaintenanceRemodelingVo();
                    aVo.setId(aMoldMaintenanceRemodeling.getId() == null ? "" : aMoldMaintenanceRemodeling.getId());
                    if (aMoldMaintenanceRemodeling.getMstMold() != null) {
                        //金型ID
                        aVo.setMoldId(aMoldMaintenanceRemodeling.getMstMold().getMoldId());
                        //金型名称
                        aVo.setMoldName(aMoldMaintenanceRemodeling.getMstMold().getMoldName());
                    }else{
                        //金型ID
                        aVo.setMoldId("");
                        //金型名称
                        aVo.setMoldName("");
                    }
                    
                    if (aMoldMaintenanceRemodeling.getIssueId()!= null) {
                        //設備ID
                        aVo.setIssueId(aMoldMaintenanceRemodeling.getIssueId());
                    } else {
                        //設備ID
                        aVo.setIssueId("");
                    }
                    
                    if (aMoldMaintenanceRemodeling.getTblIssue() != null){
                        if (aMoldMaintenanceRemodeling.getTblIssue().getIssue()!= null) {
                            //設備ID
                            aVo.setIssueText(aMoldMaintenanceRemodeling.getTblIssue().getIssue());
                        } else {
                            //設備ID
                            aVo.setIssueText("");
                        }

                        if (aMoldMaintenanceRemodeling.getTblIssue().getReportCategory1Text()!= null) {
                            //設備ID
                            aVo.setIssueReportCategory1Text(aMoldMaintenanceRemodeling.getTblIssue().getReportCategory1Text());
                        } else {
                            //設備ID
                            aVo.setIssueReportCategory1Text("");
                        }

                        if (aMoldMaintenanceRemodeling.getTblIssue().getMeasureDueDate()!= null) {
                            //設備ID
                            aVo.setMeasureDueDate(aMoldMaintenanceRemodeling.getTblIssue().getMeasureDueDate());
                        } else {
                            //設備ID
                            aVo.setMeasureDueDate(null);
                        }

                        if (aMoldMaintenanceRemodeling.getTblIssue().getReportPersonName()!= null) {
                            //設備ID
                            aVo.setReportPersonName(aMoldMaintenanceRemodeling.getTblIssue().getReportPersonName());
                        } else {
                            //設備ID
                            aVo.setReportPersonName("");
                        }
                    }
                    //メンテナンス日
                    if (null != aMoldMaintenanceRemodeling.getMainteDate()) {
                        aVo.setMainteDate(aMoldMaintenanceRemodeling.getMainteDate());
                        aVo.setMainteDateText(DateFormat.dateFormat(aMoldMaintenanceRemodeling.getMainteDate(), loginUser.getLangId()));
                    }
                    //報告事項	
                    aVo.setReport(aMoldMaintenanceRemodeling.getReport() == null ? "" : aMoldMaintenanceRemodeling.getReport());
                    //所要時間
                    aVo.setWorkingTimeMinutes(aMoldMaintenanceRemodeling.getWorkingTimeMinutes());//20170901 Apeng add
                    //工事ID
                    aVo.setDirectionId(aMoldMaintenanceRemodeling.getDirectionId() == null ? "" : aMoldMaintenanceRemodeling.getDirectionId());
                    if(aMoldMaintenanceRemodeling.getTblDirection() != null){
                        aVo.setDirectionCode(aMoldMaintenanceRemodeling.getTblDirection().getDirectionCode() == null ? "" : aMoldMaintenanceRemodeling.getTblDirection().getDirectionCode());
                    }else{
                        if (aMoldMaintenanceRemodeling.getDirectionCode() != null) {
                            aVo.setDirectionCode(aMoldMaintenanceRemodeling.getDirectionCode());
                        } 
                        else {
                            aVo.setDirectionCode("");
                        }
                    }
                    
                    //金型外部データflag
                    boolean externalMoldFlag = false;
                    if(aMoldMaintenanceRemodeling.getMstMold() != null){
                        String moldId = aMoldMaintenanceRemodeling.getMstMold().getMoldId();
                        externalMoldFlag = FileUtil.checkExternal(entityManager, mstDictionaryService, moldId, loginUser).isError();
                        if(externalMoldFlag){
                            aVo.setExternalFlg("1");
                        }else{
                            aVo.setExternalFlg("0");
                        }
                    }

                    //改造・メンテナンス区分
                    if (mainteOrRemodelChoiceList != null && mainteOrRemodelChoiceList.getMstChoice() != null) {
                        if (aMoldMaintenanceRemodeling.getMainteOrRemodel() != null) {
                            aVo.setMainteOrRemodel(aMoldMaintenanceRemodeling.getMainteOrRemodel());
                            for (int momi = 0; momi < mainteOrRemodelChoiceList.getMstChoice().size(); momi++) {
                                MstChoice aMstChoice = mainteOrRemodelChoiceList.getMstChoice().get(momi);
                                if (aMstChoice.getMstChoicePK().getSeq().equals(aMoldMaintenanceRemodeling.getMainteOrRemodel().toString())) {
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
                    
                    //実施者
                    if(aMoldMaintenanceRemodeling.getMstUser() != null) {
                        aVo.setReportPersonName(aMoldMaintenanceRemodeling.getMstUser().getUserName());
                    } else {
                         aVo.setReportPersonName("");
                    }

                    if (externalMoldFlag) {
                        //外部
                        //メンテナンス分類
                        if (null != aMoldMaintenanceRemodeling.getMainteType()) {
                            aVo.setMainteTypeText(extMstChoiceService.getExtMstChoiceText(aMoldMaintenanceRemodeling.getMstMold().getCompanyId(), "tbl_mold_maintenance_remodeling.mainte_type", String.valueOf(aMoldMaintenanceRemodeling.getMainteType()), loginUser.getLangId()));
                        } else {
                            aVo.setMainteTypeText("");
                        }

                        //改造分類	
                        if (null != aMoldMaintenanceRemodeling.getRemodelingType()) {
                            aVo.setRemodelingTypeText(extMstChoiceService.getExtMstChoiceText(aMoldMaintenanceRemodeling.getMstMold().getCompanyId(), "tbl_mold_maintenance_remodeling.remodeling_type", String.valueOf(aMoldMaintenanceRemodeling.getRemodelingType()), loginUser.getLangId()));
                        } else {
                            aVo.setRemodelingTypeText("");
                        }
                    } else {
                        //メンテナンス分類
                        if (null == aMoldMaintenanceRemodeling.getMainteType()) {
                            aVo.setMainteTypeText("");
                        } else {
                            aVo.setRemodelingType(aMoldMaintenanceRemodeling.getMainteType());
                            if (mainteTypeChoiceList != null && mainteTypeChoiceList.getMstChoice() != null) {
                                for (int momi = 0; momi < mainteTypeChoiceList.getMstChoice().size(); momi++) {
                                    MstChoice aMstChoice = mainteTypeChoiceList.getMstChoice().get(momi);
                                    if (aMstChoice.getMstChoicePK().getSeq().equals(aMoldMaintenanceRemodeling.getMainteType().toString())) {
                                        aVo.setMainteTypeText(aMstChoice.getChoice());
                                        break;
                                    }
                                }
                            } else {
                                aVo.setMainteTypeText("");
                            }
                        }
                        
                        //改造分類	
                        if (null == aMoldMaintenanceRemodeling.getRemodelingType()) {
                            aVo.setRemodelingTypeText("");
                        } else {
                            aVo.setRemodelingType(aMoldMaintenanceRemodeling.getRemodelingType());
                            if (remodelingTypeChoiceList != null && remodelingTypeChoiceList.getMstChoice() != null) {
                                for (int momi = 0; momi < remodelingTypeChoiceList.getMstChoice().size(); momi++) {
                                    MstChoice aMstChoice = remodelingTypeChoiceList.getMstChoice().get(momi);
                                    if (aMstChoice.getMstChoicePK().getSeq().equals(aMoldMaintenanceRemodeling.getRemodelingType().toString())) {
                                        aVo.setRemodelingTypeText(aMstChoice.getChoice());
                                        break;
                                    }
                                }
                            } else {
                                aVo.setRemodelingTypeText("");
                            }
                        }
                    }
                    // スマホ版金型メンテナンス add ver.1.9.0
                    List<TblMoldMaintenanceDetailVo> moldMaintenanceDetailVos = new ArrayList<>();
                    if (aMoldMaintenanceRemodeling.getTblMoldMaintenanceDetailCollection() != null && aMoldMaintenanceRemodeling.getTblMoldMaintenanceDetailCollection().size() > 0) {
                        TblMoldMaintenanceDetailVo tblMoldMaintenanceDetailVo = new TblMoldMaintenanceDetailVo();
                        List<TblMoldMaintenanceDetail> resList = new ArrayList(aMoldMaintenanceRemodeling.getTblMoldMaintenanceDetailCollection());
                        // メンテ理由大分類
                        TblMoldMaintenanceDetail tblMoldMaintenanceDetail = resList.get(0);
                        tblMoldMaintenanceDetailVo.setMainteReasonCategory1(String.valueOf(tblMoldMaintenanceDetail.getMainteReasonCategory1()));
                        if (externalMoldFlag) {
                            tblMoldMaintenanceDetailVo.setMainteReasonCategory1Text(
                                    extMstChoiceService.getExtMstChoiceText(
                                            aMoldMaintenanceRemodeling.getMstMold().getCompanyId(),
                                            "tbl_mold_maintenance_detail.remodel_reason_category1",
                                            tblMoldMaintenanceDetailVo.getMainteReasonCategory1(),
                                            loginUser.getLangId()));
                        } else {
                            MstChoice choice = mstChoiceService.getBySeqChoice(
                                    tblMoldMaintenanceDetailVo.getMainteReasonCategory1(),
                                    loginUser.getLangId(),
                                    "tbl_mold_maintenance_detail.mainte_reason_category1");
                            tblMoldMaintenanceDetailVo.setMainteReasonCategory1Text(choice == null ? "" : choice.getChoice());
                        }

                        // メンテ理由
                        tblMoldMaintenanceDetailVo.setManiteReason(tblMoldMaintenanceDetail.getManiteReason());
                        moldMaintenanceDetailVos.add(tblMoldMaintenanceDetailVo);
                    }
                    aVo.setMoldMaintenanceDetailVo(moldMaintenanceDetailVos);
                    //開始日時
                    aVo.setStartDatetime(new FileUtil().getDateTimeFormatForStr(aMoldMaintenanceRemodeling.getStartDatetime()));
                    
                    //終了日時
                    if(aMoldMaintenanceRemodeling.getEndDatetime() != null){
                        aVo.setEndDatetime(aMoldMaintenanceRemodeling.getEndDatetime().compareTo(CommonConstants.SYS_MAX_DATE) == 0 ? "-" : new FileUtil().getDateTimeFormatForStr(aMoldMaintenanceRemodeling.getEndDatetime()));
                    }else{
                        aVo.setEndDatetime("-");
                    }
                    
                    moldMaintenanceRemodelingVos.add(aVo);
                }
            }

            resVo.setMoldMaintenanceRemodelingVo(moldMaintenanceRemodelingVos);
            return resVo;
        }
    }

    /**
     *
     * @param moldId
     * @param type
     * @param loginUser
     * @return
     */
    //@Transactional
    public BasicResponse getMoldmainteOrRemodelDetail(String moldId, int type, LoginUser loginUser) {
        TblMoldMaintenanceRemodelingVo resVo = new TblMoldMaintenanceRemodelingVo();
        StringBuilder sql = new StringBuilder("select mmr from TblMoldMaintenanceRemodeling mmr where 1=1 ");
        sql.append(" and mmr.mstMold.moldId = :moldId and mmr.mainteOrRemodel = :mainteOrRemodel order by mmr.startDatetime desc ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("moldId", moldId);
        query.setParameter("mainteOrRemodel", type);
        query.setMaxResults(1);

        try {
            TblMoldMaintenanceRemodeling aMaintenanceRemodeling = (TblMoldMaintenanceRemodeling) query.getSingleResult();
            resVo.setId(aMaintenanceRemodeling.getId());
            if (aMaintenanceRemodeling.getMstMold() != null) {
                resVo.setMoldId(aMaintenanceRemodeling.getMstMold().getMoldId() == null ? "" : aMaintenanceRemodeling.getMstMold().getMoldId());
                resVo.setMoldName(aMaintenanceRemodeling.getMstMold().getMoldName());
            } else {
                resVo.setMoldId("");
                resVo.setMoldName("");
            }

            resVo.setStartDatetime(null == aMaintenanceRemodeling.getStartDatetime() ? "" : new FileUtil().getDateTimeFormatForStr(aMaintenanceRemodeling.getStartDatetime()));
            if(aMaintenanceRemodeling.getEndDatetime() != null){
                resVo.setEndDatetime(aMaintenanceRemodeling.getEndDatetime().compareTo(CommonConstants.SYS_MAX_DATE) == 0 ? "-" : new FileUtil().getDateTimeFormatForStr(aMaintenanceRemodeling.getEndDatetime()));
            }else{
                resVo.setEndDatetime("-");
            }
            resVo.setDirectionId(aMaintenanceRemodeling.getDirectionId() == null ? "" : aMaintenanceRemodeling.getDirectionId());
            if(aMaintenanceRemodeling.getTblDirection() != null){
                resVo.setDirectionCode(aMaintenanceRemodeling.getTblDirection().getDirectionCode() == null ? "" : aMaintenanceRemodeling.getTblDirection().getDirectionCode());
            }else{
                resVo.setDirectionCode("");
            }
            // KM-358	メンテナンス開始画面仕様変更
            resVo.setIssueId(aMaintenanceRemodeling.getIssueId());
            
        } catch (NoResultException e) {
            return resVo;
        }

        return resVo;
    }

    /**
     *
     * @param maintenanceId
     * @param moldId
     * @param type
     * @param loginUser
     * @return
     */
    //@Transactional
    public TblMoldMaintenanceRemodelingVo getMoldmainteOrRemodelDetails(String maintenanceId, String moldId, int type, LoginUser loginUser) {
        TblMoldMaintenanceRemodelingVo resVo = new TblMoldMaintenanceRemodelingVo();
        List<TblMoldMaintenanceRemodelingVo> moldMaintenanceRemodelingVos = new ArrayList<>();
        
        
        resVo.setMoldMaintenanceRemodelingVo(moldMaintenanceRemodelingVos);
        if (null != maintenanceId) {
            resVo.setMaintenanceId(maintenanceId);
        } else if (null != moldId) {
            resVo.setMoldId(moldId);
        }
        StringBuilder sql = new StringBuilder("select mmr from TblMoldMaintenanceRemodeling mmr where 1=1 and mmr.mainteOrRemodel = :mainteOrRemodel ");
        if (null != maintenanceId) {
            sql.append(" and mmr.id = :maintenanceId ");
        } else if (null != moldId) {
            sql.append(" and mmr.mstMold.moldId = :moldId ");
        }
        sql.append(" order by mmr.startDatetime desc ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("mainteOrRemodel", type);
        if (null != maintenanceId) {
            query.setParameter("maintenanceId", maintenanceId);
        } else if (null != moldId) {
            query.setParameter("moldId", moldId);
            query.setMaxResults(1);
        }

        try {
            TblMoldMaintenanceRemodeling aMaintenanceRemodeling = (TblMoldMaintenanceRemodeling)query.getSingleResult();
            MstMold mold;
            if (aMaintenanceRemodeling != null) {

                    mold = aMaintenanceRemodeling.getMstMold();
                    resVo.setMoldId(mold.getMoldId());
                    if (FileUtil.checkExternal(entityManager, mstDictionaryService, mold.getMoldId(), loginUser).isError()) {
                        resVo.setExternalFlg("1");
                        //メンテ分類
                        if (null != aMaintenanceRemodeling.getMainteType()) {
                            resVo.setMainteType(aMaintenanceRemodeling.getMainteType());
                            resVo.setMainteTypeText(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_maintenance_remodeling.mainte_type", String.valueOf(aMaintenanceRemodeling.getMainteType()), loginUser.getLangId()));
                        } else {
                            resVo.setMainteTypeText("");
                        }
                        
                        //改造分類
                        if (null != aMaintenanceRemodeling.getRemodelingType()) {
                            resVo.setRemodelingType(aMaintenanceRemodeling.getRemodelingType());
                            resVo.setRemodelingTypeText(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_maintenance_remodeling.remodeling_type", String.valueOf(aMaintenanceRemodeling.getRemodelingType()), loginUser.getLangId()));
                        } else {
                            resVo.setRemodelingTypeText("");
                        }

                    } else {
                        resVo.setExternalFlg("0");
                        resVo.setMainteType(aMaintenanceRemodeling.getMainteType());
                        resVo.setRemodelingType(aMaintenanceRemodeling.getRemodelingType());
                    }
                    resVo.setMoldName(mold.getMoldName());
                    resVo.setMaintenanceId(aMaintenanceRemodeling.getId());

                    resVo.setReport(aMaintenanceRemodeling.getReport());
                    resVo.setDirectionId(aMaintenanceRemodeling.getDirectionId());
                    resVo.setWorkingTimeMinutes(aMaintenanceRemodeling.getWorkingTimeMinutes());
                    
                    resVo.setAfterMainteTotalProducingTimeHour(mold.getAfterMainteTotalProducingTimeHour());
                    resVo.setAfterMainteTotalShotCount(mold.getAfterMainteTotalShotCount());
                    
                    if (aMaintenanceRemodeling.getTblDirection() != null) {
                        resVo.setDirectionCode(aMaintenanceRemodeling.getTblDirection().getDirectionCode());
                    } else {
                        if (aMaintenanceRemodeling.getDirectionCode() != null) {
                            resVo.setDirectionCode(aMaintenanceRemodeling.getDirectionCode());
                        }
                        else {
                            resVo.setDirectionCode("");
                        }
                    }
                    if (null != aMaintenanceRemodeling.getIssueId()) {
                        resVo.setIssueId(aMaintenanceRemodeling.getIssueId());
                        TblIssue issue = aMaintenanceRemodeling.getTblIssue();
                        if (null != issue) {
                            resVo.setIssueText(issue.getIssue());
                            resVo.setIssueReportCategory1Id("" + issue.getReportCategory1());
                            resVo.setIssueReportCategory1Text(issue.getReportCategory1Text());
                            resVo.setMeasureStatus("" + issue.getMeasureStatus());
                        } else {
                            resVo.setIssueText("");
                            resVo.setIssueReportCategory1Id("");
                            resVo.setIssueReportCategory1Text("");
                            resVo.setMeasureStatus("");
                        }

                    }
                    if (null != aMaintenanceRemodeling.getMoldSpecHstIdStr()) {
                        MstMoldSpecHistory mmsh = aMaintenanceRemodeling.getMoldSpecHstId();
                        if (null != mmsh) {
                            resVo.setMoldSpecName(mmsh.getMoldSpecName());
                        }else{
                            resVo.setMoldSpecName("");
                        }
                    }else{
                        List<MstMoldSpecHistory> specHistory = new ArrayList(mold.getMstMoldSpecHistoryCollection());
                        if(specHistory.size()>0){
                            resVo.setMoldSpecName(specHistory.get(0).getMoldSpecName());
                        }else{
                            resVo.setMoldSpecName("");
                        }
                    }
                    FileUtil fu = new FileUtil();
                    if (aMaintenanceRemodeling.getStartDatetime() != null) {
                        resVo.setStartDatetime(fu.getDateTimeFormatForStr(aMaintenanceRemodeling.getStartDatetime()));
                    }else{
                        resVo.setStartDatetime("");
                    }
                    if (aMaintenanceRemodeling.getEndDatetime() != null) {
                        //resVo.setEndDatetime(aMaintenanceRemodeling.getEndDatetime().compareTo(CommonConstants.SYS_MAX_DATE) == 0 ? "-" : fu.getDateTimeFormatForStr(aMaintenanceRemodeling.getEndDatetime()));
                        resVo.setEndDatetime(fu.getDateTimeFormatForStr(aMaintenanceRemodeling.getEndDatetime()));
                    } else {
                        resVo.setEndDatetime("");
                    }

                    Query userQuery = entityManager.createNamedQuery("MstUser.findByUserUuid");
                    userQuery.setParameter("uuid", aMaintenanceRemodeling.getCreateUserUuid());
                    try {
                        MstUser users = (MstUser) userQuery.getSingleResult();
                        resVo.setReportPersonName(users.getUserName() == null ? "" : users.getUserName());
                    } catch (NoResultException e) {
                        resVo.setReportPersonName("");
                    }
                    
                    StringBuilder moldPartRel = new StringBuilder(
                            "SELECT moldPartRel FROM MstMoldPartRel moldPartRel"
                          + " WHERE moldPartRel.moldUuid = :moldUuid");
                    Query moldPartRelQuery = entityManager.createQuery(moldPartRel.toString());

                    moldPartRelQuery.setParameter("moldUuid", mold.getUuid());
                    List<MstMoldPartRel> mstMoldPartRels = moldPartRelQuery.getResultList();
                    String moldMaintenancePartSql = 
                            "SELECT moldMaintenancePart FROM TblMoldMaintenancePart moldMaintenancePart"
                          + " WHERE moldMaintenancePart.tblMoldMaintenancePartPK.moldPartRelId = :moldPartRelId"
                          + " AND moldMaintenancePart.tblMoldMaintenancePartPK.maintenanceId = :maintenanceId";
                    
                    List<TblMoldMaintencePartVo> tblMoldMaintencePartVos = new ArrayList<>();
                   
                    for (int i = 0; i < mstMoldPartRels.size(); i++) {
                        TblMoldMaintencePartVo moldMaintPartVO = new TblMoldMaintencePartVo();

                        moldMaintPartVO.setLocation(mstMoldPartRels.get(i).getLocation());
                        if (null != mstMoldPartRels.get(i).getMstMoldPart()) {
                            moldMaintPartVO.setMoldPartId(mstMoldPartRels.get(i).getMstMoldPart().getId());
                            moldMaintPartVO.setMoldPartCode(mstMoldPartRels.get(i).getMstMoldPart().getMoldPartCode());
                            moldMaintPartVO.setMoldPartName(mstMoldPartRels.get(i).getMstMoldPart().getMoldPartName());
                        }
                        
                        Query moldMaintPartQuery = entityManager.createQuery(moldMaintenancePartSql);
                        moldMaintPartQuery.setParameter("moldPartRelId", mstMoldPartRels.get(i).getId());
                        moldMaintPartQuery.setParameter("maintenanceId", maintenanceId);
                        try {
                            TblMoldMaintenancePart moldMaintPart = (TblMoldMaintenancePart) moldMaintPartQuery.getSingleResult();
                            // Minimize the mold maintenance part response
                            moldMaintPartVO.setReplaceOrRepair(moldMaintPart.getReplaceOrRepair());
                            moldMaintPartVO.setShotCntAtManit(moldMaintPart.getShotCntAtManit());
                            moldMaintPartVO.setTblMoldMaintenancePartPK(moldMaintPart.getTblMoldMaintenancePartPK());
                            moldMaintPartVO.setDisposeQuantity(moldMaintPart.getDisposeQuantity());
                            moldMaintPartVO.setRecycleQuantity(moldMaintPart.getRecycleQuantity());
                        
                        } catch (NoResultException e) {
                            // Ignored
                        }
                        tblMoldMaintencePartVos.add(moldMaintPartVO);
                    }
                    resVo.setTblMoldMaintenancePartVO(tblMoldMaintencePartVos);

                    List resList = null;
                    if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {//type is MAINTE
                        List<TblMoldMaintenanceDetailVo> tblMoldMaintenanceDetailVos = new ArrayList<>();
                        resVo.setMoldMaintenanceDetailVo(tblMoldMaintenanceDetailVos);
                        resList = new ArrayList(aMaintenanceRemodeling.getTblMoldMaintenanceDetailCollection());
                    } else if (type == CommonConstants.MAINTEORREMODEL_REMODEL) {//type is REMODEL
                        List<TblMoldRemodelingDetailVo> tblMoldRemodelingDetailVos = new ArrayList<>();
                        resVo.setMoldRemodelingDetailVo(tblMoldRemodelingDetailVos);
                        resList = new ArrayList(aMaintenanceRemodeling.getTblMoldRemodelingDetailCollection());
                    }
                    
                    for (int j = 0; j < resList.size(); j++) {
                        if (resList.get(j) instanceof TblMoldMaintenanceDetail) {
                            TblMoldMaintenanceDetailVo aVo = new TblMoldMaintenanceDetailVo();
                            TblMoldMaintenanceDetail aDetail = (TblMoldMaintenanceDetail) resList.get(j);
                            aVo.setId(aDetail.getId());
                            aVo.setMaintenanceId(aMaintenanceRemodeling.getId());
                            //金型ID	
                            aVo.setMoldId(mold.getMoldId());
                            //金型名称	
                            aVo.setMoldName(mold.getMoldName());
                            
                            //報告事項	
                            aVo.setReport(aMaintenanceRemodeling.getReport() == null ? "" : aMaintenanceRemodeling.getReport());
                            
                            boolean externalMoldFlag = false;
                            if(mold.getMoldId() != null){
                                //String moldId = aMoldMaintenanceRemodeling.getMstMold().getMoldId();
                                externalMoldFlag = FileUtil.checkExternal(entityManager, mstDictionaryService, mold.getMoldId(), loginUser).isError();
                                if(externalMoldFlag){
                                    if (null != aDetail.getMainteReasonCategory1()) {
                                        aVo.setMainteReasonCategory1("" + aDetail.getMainteReasonCategory1());
                                        aVo.setMainteReasonCategory1Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_maintenance_detail.remodel_reason_category1", String.valueOf(aVo.getMainteReasonCategory1()), loginUser.getLangId()));
                                        //aVo.setMainteReasonCategory1Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setMainteReasonCategory1("");
                                        aVo.setMainteReasonCategory1Text("");
                                    }
                                    //メンテ理由中分類
                                    if (null != aDetail.getMainteReasonCategory2()) {
                                        aVo.setMainteReasonCategory2("" + aDetail.getMainteReasonCategory2());
                                        aVo.setMainteReasonCategory2Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_maintenance_detail.remodel_reason_category2", String.valueOf(aVo.getMainteReasonCategory2()), loginUser.getLangId()));
                                        //aVo.setMainteReasonCategory2Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setMainteReasonCategory2("");
                                        aVo.setMainteReasonCategory2Text("");
                                    }
        
                                    //メンテ理由小分類
                                    if (null != aDetail.getMainteReasonCategory3()) {
                                        aVo.setMainteReasonCategory3("" + aDetail.getMainteReasonCategory3());
                                        aVo.setMainteReasonCategory3Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_maintenance_detail.remodel_reason_category3", String.valueOf(aVo.getMainteReasonCategory3()), loginUser.getLangId()));
                                        //aVo.setMainteReasonCategory3Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setMainteReasonCategory3("");
                                        aVo.setMainteReasonCategory3Text("");
                                    }
                                    
                                    //メンテ理由	
                                    aVo.setManiteReason(aDetail.getManiteReason() == null ? "" : aDetail.getManiteReason());
                                    //対策指示大分類	
                                    if (null != aDetail.getMeasureDirectionCategory1()) {
                                        aVo.setMeasureDirectionCategory1("" + aDetail.getTaskCategory1());
                                        aVo.setMeasureDirectionCategory1Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_maintenance_detail.remodel_direction_category1", String.valueOf(aVo.getMeasureDirectionCategory1()), loginUser.getLangId()));
                                        //aVo.setMeasureDirectionCategory1Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setMeasureDirectionCategory1("");
                                        aVo.setMeasureDirectionCategory1Text("");
                                    }

                                    //対策指示中分類
                                    if (null != aDetail.getMeasureDirectionCategory2()) {
                                        aVo.setMeasureDirectionCategory2("" + aDetail.getTaskCategory1());
                                        aVo.setMeasureDirectionCategory2Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_maintenance_detail.remodel_direction_category2", String.valueOf(aVo.getMeasureDirectionCategory2()), loginUser.getLangId()));
                                        //aVo.setMeasureDirectionCategory2Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setMeasureDirectionCategory2("");
                                        aVo.setMeasureDirectionCategory2Text("");
                                    }

                                    //対策指示小分類		
                                    if (null != aDetail.getMeasureDirectionCategory3()) {
                                        aVo.setMeasureDirectionCategory3("" + aDetail.getTaskCategory1());
                                        aVo.setMeasureDirectionCategory3Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_maintenance_detail.remodel_direction_category3", String.valueOf(aVo.getMeasureDirectionCategory3()), loginUser.getLangId()));
                                        //aVo.setMeasureDirectionCategory3Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setMeasureDirectionCategory3("");
                                        aVo.setMeasureDirectionCategory3Text("");
                                    }

                                    //対策指示
                                    aVo.setMeasureDirection(aDetail.getMeasureDirection() == null ? "" : aDetail.getMeasureDirection());
                                    //作業大分類		
                                    if (null != aDetail.getTaskCategory1()) {
                                        aVo.setTaskCategory1("" + aDetail.getTaskCategory1());
                                        aVo.setTaskCategory1Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "mst_mold_inspection_item.task_category1", String.valueOf(aVo.getTaskCategory1()), loginUser.getLangId()));
                                        //aVo.setTaskCategory1Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setTaskCategory1("");
                                        aVo.setTaskCategory1Text("");
                                    }

                                    //作業中分類		
                                    if (null != aDetail.getTaskCategory2()) {
                                        aVo.setTaskCategory2("" + aDetail.getTaskCategory2());
                                        aVo.setTaskCategory2Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "mst_mold_inspection_item.task_category2", String.valueOf(aVo.getTaskCategory2()), loginUser.getLangId()));
                                        //aVo.setTaskCategory2Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setTaskCategory2("");
                                        aVo.setTaskCategory2Text("");
                                    }

                                    //作業小分類	
                                    if (null != aDetail.getTaskCategory3()) {
                                        aVo.setTaskCategory3("" + aDetail.getTaskCategory3());
                                        aVo.setTaskCategory3Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "mst_mold_inspection_item.task_category3", String.valueOf(aVo.getTaskCategory3()), loginUser.getLangId()));
                                        //aVo.setTaskCategory3Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setTaskCategory3("");
                                        aVo.setTaskCategory3Text("");
                                    }
                                    
                                }else{
                                    
                                    if (null != aDetail.getMainteReasonCategory1()) {
                                        //aVo.setMainteReasonCategory1(mstChoiceService.getBySeqChoice(String.valueOf(aVo.getTaskCategory1())), loginUser.getLangId(),"mst_mold_inspection_item.task_category1");
                                        aVo.setMainteReasonCategory1("" + aDetail.getMainteReasonCategory1());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getMainteReasonCategory1()), loginUser.getLangId(), "tbl_mold_maintenance_detail.mainte_reason_category1");
                                        aVo.setMainteReasonCategory1Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setMainteReasonCategory1("");
                                        aVo.setMainteReasonCategory1Text("");
                                    }
                                    //メンテ理由中分類
                                    if (null != aDetail.getMainteReasonCategory2()) {
                                        aVo.setMainteReasonCategory2("" + aDetail.getMainteReasonCategory2());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getMainteReasonCategory2()), loginUser.getLangId(), "tbl_mold_maintenance_detail.mainte_reason_category2");
                                        aVo.setMainteReasonCategory2Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setMainteReasonCategory2("");
                                        aVo.setMainteReasonCategory2Text("");
                                    }
        
                                    //メンテ理由小分類
                                    if (null != aDetail.getMainteReasonCategory3()) {
                                        aVo.setMainteReasonCategory3("" + aDetail.getMainteReasonCategory3());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getMainteReasonCategory3()), loginUser.getLangId(), "tbl_mold_maintenance_detail.mainte_reason_category3");
                                        aVo.setMainteReasonCategory3Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setMainteReasonCategory3("");
                                        aVo.setMainteReasonCategory3Text("");
                                    }
                                    
                                    //メンテ理由	
                                    aVo.setManiteReason(aDetail.getManiteReason() == null ? "" : aDetail.getManiteReason());
                                    //対策指示大分類	
                                    if (null != aDetail.getMeasureDirectionCategory1()) {
                                        aVo.setMeasureDirectionCategory1("" + aDetail.getMeasureDirectionCategory1());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getMeasureDirectionCategory1()), loginUser.getLangId(), "tbl_mold_maintenance_detail.measure_direction_category1");
                                        aVo.setMeasureDirectionCategory1Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setMeasureDirectionCategory1("");
                                        aVo.setMeasureDirectionCategory1Text("");
                                    }

                                    //対策指示中分類
                                    if (null != aDetail.getMeasureDirectionCategory2()) {
                                        aVo.setMeasureDirectionCategory2("" + aDetail.getMeasureDirectionCategory2());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getMeasureDirectionCategory2()), loginUser.getLangId(), "tbl_mold_maintenance_detail.measure_direction_category2");
                                        aVo.setMeasureDirectionCategory2Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setMeasureDirectionCategory2("");
                                        aVo.setMeasureDirectionCategory2Text("");
                                    }

                                    //対策指示小分類		
                                    if (null != aDetail.getMeasureDirectionCategory3()) {
                                        aVo.setMeasureDirectionCategory3("" + aDetail.getMeasureDirectionCategory3());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getMeasureDirectionCategory3()), loginUser.getLangId(), "tbl_mold_maintenance_detail.measure_direction_category3");
                                        aVo.setMeasureDirectionCategory3Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setMeasureDirectionCategory3("");
                                        aVo.setMeasureDirectionCategory3Text("");
                                    }

                                    //対策指示
                                    aVo.setMeasureDirection(aDetail.getMeasureDirection() == null ? "" : aDetail.getMeasureDirection());
                                    //作業大分類		
                                    if (null != aDetail.getTaskCategory1()) {
                                        aVo.setTaskCategory1("" + aDetail.getTaskCategory1());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getTaskCategory1()), loginUser.getLangId(), "mst_mold_inspection_item.task_category1");
                                        aVo.setTaskCategory1Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setTaskCategory1("");
                                        aVo.setTaskCategory1Text("");
                                    }

                                    //作業中分類		
                                    if (null != aDetail.getTaskCategory2()) {
                                        aVo.setTaskCategory2("" + aDetail.getTaskCategory2());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getTaskCategory2()), loginUser.getLangId(), "mst_mold_inspection_item.task_category2");
                                        aVo.setTaskCategory2Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setTaskCategory2("");
                                        aVo.setTaskCategory2Text("");
                                    }

                                    //作業小分類	
                                    if (null != aDetail.getTaskCategory3()) {
                                        aVo.setTaskCategory3("" + aDetail.getTaskCategory3());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getTaskCategory3()), loginUser.getLangId(), "mst_mold_inspection_item.task_category3");
                                        aVo.setTaskCategory3Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setTaskCategory3("");
                                        aVo.setTaskCategory3Text("");
                                    }
                                }
                            }
                            //作業	
                            aVo.setTask(aDetail.getTask());

                            aVo.setSeq(aDetail.getTblMoldMaintenanceDetailPK().getSeq());
                            
                            List<TblMoldInspectionResultVo> inspectionResultVo = new ArrayList<>();
                            
                            StringBuilder sqlResult = new StringBuilder("SELECT t FROM TblMoldInspectionResult t ");
                            sqlResult.append("LEFT JOIN FETCH TblMoldMaintenanceDetail t2 ON ");
                            sqlResult.append("t.tblMoldInspectionResultPK.maintenanceDetailId = t2.id ");
                            sqlResult.append("WHERE t2.id = :id ");

                            Query queryResult = entityManager.createQuery(sqlResult.toString());

                            queryResult.setParameter("id", aDetail.getId());

                            List listResult = queryResult.getResultList();
                            
                            TblMoldInspectionResultVo moldInspectionResultVos = null;
                            for(int i=0;i<listResult.size();i++){
                                TblMoldInspectionResult tblMoldInspectionResult = (TblMoldInspectionResult)listResult.get(i);
                                moldInspectionResultVos = new TblMoldInspectionResultVo();
                                moldInspectionResultVos.setId(tblMoldInspectionResult.getId());
                                moldInspectionResultVos.setInspectionResult(tblMoldInspectionResult.getInspectionResult());
                                moldInspectionResultVos.setInspectionResultText(tblMoldInspectionResult.getInspectionResultText());
                                moldInspectionResultVos.setMaintenanceDetailId(tblMoldInspectionResult.getTblMoldInspectionResultPK().getMaintenanceDetailId());
                                moldInspectionResultVos.setSeq(tblMoldInspectionResult.getTblMoldInspectionResultPK().getSeq());
                                moldInspectionResultVos.setInspectionItemId(tblMoldInspectionResult.getTblMoldInspectionResultPK().getInspectionItemId());
                                if(tblMoldInspectionResult.getMstMoldInspectionItem() != null){
                                    moldInspectionResultVos.setInspectionItemName(tblMoldInspectionResult.getMstMoldInspectionItem().getInspectionItemName());
                                    if(tblMoldInspectionResult.getMstMoldInspectionItem().getResultType() != null){
                                        moldInspectionResultVos.setResultType(String.valueOf(tblMoldInspectionResult.getMstMoldInspectionItem().getResultType()));
                                    }else{
                                        moldInspectionResultVos.setResultType("");
                                    }
                                }else{
                                    moldInspectionResultVos.setInspectionItemName("");
                                    moldInspectionResultVos.setResultType("");
                                }
                                inspectionResultVo.add(moldInspectionResultVos);
                            }
                            aVo.setMoldInspectionResultVos(inspectionResultVo);
                            
                            
                            List<TblMoldMaintenanceDetailImageFileVo> fileResultVos = new ArrayList<>();
                            //TblMoldMaintenanceDetailImageFileVo　検索
                            List<TblMoldMaintenanceDetailImageFile> tmpFileResults = entityManager.createQuery("SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.tblMoldMaintenanceDetailImageFilePK.maintenanceDetailId = :maintenanceDetailId ")
                                    .setParameter("maintenanceDetailId", aDetail.getId())
                                    .getResultList();

                            TblMoldMaintenanceDetailImageFileVo moldMaintenanceDetailImageFileVo = null;
                            for (int i = 0; i < tmpFileResults.size(); i++) {
                                TblMoldMaintenanceDetailImageFile aFile = tmpFileResults.get(i);
                                moldMaintenanceDetailImageFileVo = new TblMoldMaintenanceDetailImageFileVo();
                                moldMaintenanceDetailImageFileVo.setMaintenanceDetailId(aDetail.getId());
                                moldMaintenanceDetailImageFileVo.setSeq("" + aFile.getTblMoldMaintenanceDetailImageFilePK().getSeq());
                                moldMaintenanceDetailImageFileVo.setFileType("" + aFile.getFileType());
                                moldMaintenanceDetailImageFileVo.setFileExtension(aFile.getFileExtension());
                                moldMaintenanceDetailImageFileVo.setFileUuid(aFile.getFileUuid());
                                moldMaintenanceDetailImageFileVo.setRemarks(aFile.getRemarks());
                                moldMaintenanceDetailImageFileVo.setThumbnailFileUuid(aFile.getThumbnailFileUuid());
                                if (null != aFile.getTakenDate()) {
                                    moldMaintenanceDetailImageFileVo.setTakenDateStr(new FileUtil().getDateTimeFormatForStr(aFile.getTakenDate()));
                                }
                                if (null != aFile.getTakenDateStz()) {
                                    moldMaintenanceDetailImageFileVo.setTakenDateStzStr(new FileUtil().getDateTimeFormatForStr(aFile.getTakenDateStz()));
                                }

                                fileResultVos.add(moldMaintenanceDetailImageFileVo);
                            }
                            aVo.setMoldMaintenanceDetailImageFileVos(fileResultVos);
                            
                            
                            resVo.getMoldMaintenanceDetailVo().add(aVo);
                        } else if (resList.get(j) instanceof TblMoldRemodelingDetail) {
                            TblMoldRemodelingDetailVo aVo = new TblMoldRemodelingDetailVo();
                            TblMoldRemodelingDetail aDetail = (TblMoldRemodelingDetail) resList.get(j);
                            aVo.setId(aDetail.getId());
                            //金型ID	
                            aVo.setMoldId(mold.getMoldId());
                            //金型名称	
                            aVo.setMoldName(mold.getMoldName());
                            
                            //報告事項	
                            aVo.setReport(aMaintenanceRemodeling.getReport() == null ? "" : aMaintenanceRemodeling.getReport());
                            //改造理由大分類
                            boolean externalMoldFlag = false;
                            if(mold.getMoldId() != null){
                                //String moldId = aMoldMaintenanceRemodeling.getMstMold().getMoldId();
                                externalMoldFlag = FileUtil.checkExternal(entityManager, mstDictionaryService, mold.getMoldId(), loginUser).isError();
                                if(externalMoldFlag){
                                    if (null != aDetail.getRemodelReasonCategory1()) {
                                        aVo.setRemodelReasonCategory1("" + aDetail.getRemodelReasonCategory1());
                                        aVo.setRemodelReasonCategory1Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_remodeling_detail.remodel_reason_category1", String.valueOf(aVo.getRemodelReasonCategory1()), loginUser.getLangId()));
                                        //aVo.setRemodelReasonCategory1Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setRemodelReasonCategory1("");
                                        aVo.setRemodelReasonCategory1Text("");
                                    }
                                    //メンテ理由中分類
                                    if (null != aDetail.getRemodelReasonCategory2()) {
                                        aVo.setRemodelReasonCategory2("" + aDetail.getRemodelReasonCategory2());
                                        aVo.setRemodelReasonCategory2Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_remodeling_detail.remodel_reason_category2", String.valueOf(aVo.getRemodelReasonCategory2()), loginUser.getLangId()));
                                        //aVo.setRemodelReasonCategory2Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setRemodelReasonCategory2("");
                                        aVo.setRemodelReasonCategory2Text("");
                                    }
        
                                    //メンテ理由小分類
                                    if (null != aDetail.getRemodelReasonCategory3()) {
                                        aVo.setRemodelReasonCategory3("" + aDetail.getRemodelReasonCategory3());
                                        aVo.setRemodelReasonCategory3Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_remodeling_detail.remodel_reason_category3", String.valueOf(aVo.getRemodelReasonCategory3()), loginUser.getLangId()));
                                        //aVo.setRemodelReasonCategory3Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setRemodelReasonCategory3("");
                                        aVo.setRemodelReasonCategory3Text("");
                                    }
                                    
                                    //メンテ理由	
                                    aVo.setRemodelReason(aDetail.getRemodelReason() == null ? "" : aDetail.getRemodelReason());
                                    //対策指示大分類	
                                    if (null != aDetail.getRemodelDirectionCategory1()) {
                                        aVo.setRemodelDirectionCategory1("" + aDetail.getTaskCategory1());
                                        aVo.setRemodelDirectionCategory1Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_remodeling_detail.remodel_direction_category1", String.valueOf(aVo.getRemodelDirectionCategory1()), loginUser.getLangId()));
                                        //aVo.setRemodelDirectionCategory1Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setRemodelDirectionCategory1("");
                                        aVo.setRemodelDirectionCategory1Text("");
                                    }

                                    //対策指示中分類
                                    if (null != aDetail.getRemodelDirectionCategory2()) {
                                        aVo.setRemodelDirectionCategory2("" + aDetail.getTaskCategory1());
                                        aVo.setRemodelDirectionCategory2Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_remodeling_detail.remodel_direction_category2", String.valueOf(aVo.getRemodelDirectionCategory2()), loginUser.getLangId()));
                                        //aVo.setRemodelDirectionCategory2Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setRemodelDirectionCategory2("");
                                        aVo.setRemodelDirectionCategory2Text("");
                                    }

                                    //対策指示小分類		
                                    if (null != aDetail.getRemodelDirectionCategory3()) {
                                        aVo.setRemodelDirectionCategory3("" + aDetail.getTaskCategory1());
                                        aVo.setRemodelDirectionCategory3Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "tbl_mold_remodeling_detail.remodel_direction_category3", String.valueOf(aVo.getRemodelDirectionCategory3()), loginUser.getLangId()));
                                        //aVo.setRemodelDirectionCategory3Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setRemodelDirectionCategory3("");
                                        aVo.setRemodelDirectionCategory3Text("");
                                    }

                                    //対策指示
                                    aVo.setRemodelDirection(aDetail.getRemodelDirection() == null ? "" : aDetail.getRemodelDirection());
                                    //作業大分類		
                                    if (null != aDetail.getTaskCategory1()) {
                                        aVo.setTaskCategory1("" + aDetail.getTaskCategory1());
                                        aVo.setTaskCategory1Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "mst_mold_inspection_item.task_category1", String.valueOf(aVo.getTaskCategory1()), loginUser.getLangId()));
                                        //aVo.setTaskCategory1Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setTaskCategory1("");
                                        aVo.setTaskCategory1Text("");
                                    }

                                    //作業中分類		
                                    if (null != aDetail.getTaskCategory2()) {
                                        aVo.setTaskCategory2("" + aDetail.getTaskCategory2());
                                        aVo.setTaskCategory2Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "mst_mold_inspection_item.task_category2", String.valueOf(aVo.getTaskCategory2()), loginUser.getLangId()));
                                        //aVo.setTaskCategory2Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setTaskCategory2("");
                                        aVo.setTaskCategory2Text("");
                                    }

                                    //作業小分類	
                                    if (null != aDetail.getTaskCategory3()) {
                                        aVo.setTaskCategory3("" + aDetail.getTaskCategory3());
                                        aVo.setTaskCategory3Text(extMstChoiceService.getExtMstChoiceText(mold.getCompanyId(), "mst_mold_inspection_item.task_category3", String.valueOf(aVo.getTaskCategory3()), loginUser.getLangId()));
                                        //aVo.setTaskCategory3Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setTaskCategory3("");
                                        aVo.setTaskCategory3Text("");
                                    }
                                    
                                }else{
                                    
                                    if (null != aDetail.getRemodelReasonCategory1()) {
                                        //aVo.setRemodelReasonCategory1(mstChoiceService.getBySeqChoice(String.valueOf(aVo.getTaskCategory1())), loginUser.getLangId(),"mst_mold_inspection_item.task_category1");
                                        aVo.setRemodelReasonCategory1("" + aDetail.getRemodelReasonCategory1());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getRemodelReasonCategory1()), loginUser.getLangId(), "tbl_mold_remodeling_detail.remodel_reason_category1");
                                        aVo.setRemodelReasonCategory1Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setRemodelReasonCategory1("");
                                        aVo.setRemodelReasonCategory1Text("");
                                    }
                                    //メンテ理由中分類
                                    if (null != aDetail.getRemodelReasonCategory2()) {
                                        aVo.setRemodelReasonCategory2("" + aDetail.getRemodelReasonCategory2());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getRemodelReasonCategory2()), loginUser.getLangId(), "tbl_mold_remodeling_detail.remodel_reason_category2");
                                        aVo.setRemodelReasonCategory2Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setRemodelReasonCategory2("");
                                        aVo.setRemodelReasonCategory2Text("");
                                    }
        
                                    //メンテ理由小分類
                                    if (null != aDetail.getRemodelReasonCategory3()) {
                                        aVo.setRemodelReasonCategory3("" + aDetail.getRemodelReasonCategory3());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getRemodelReasonCategory3()), loginUser.getLangId(), "tbl_mold_remodeling_detail.remodel_reason_category3");
                                        aVo.setRemodelReasonCategory3Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setRemodelReasonCategory3("");
                                        aVo.setRemodelReasonCategory3Text("");
                                    }
                                    
                                    //メンテ理由	
                                    aVo.setRemodelReason(aDetail.getRemodelReason() == null ? "" : aDetail.getRemodelReason());
                                    //対策指示大分類	
                                    if (null != aDetail.getRemodelDirectionCategory1()) {
                                        aVo.setRemodelDirectionCategory1("" + aDetail.getRemodelDirectionCategory1());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getRemodelDirectionCategory1()), loginUser.getLangId(), "tbl_mold_remodeling_detail.remodel_direction_category1");
                                        aVo.setRemodelDirectionCategory1Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setRemodelDirectionCategory1("");
                                        aVo.setRemodelDirectionCategory1Text("");
                                    }

                                    //対策指示中分類
                                    if (null != aDetail.getRemodelDirectionCategory2()) {
                                        aVo.setRemodelDirectionCategory2("" + aDetail.getRemodelDirectionCategory2());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getRemodelDirectionCategory2()), loginUser.getLangId(), "tbl_mold_remodeling_detail.remodel_direction_category2");
                                        aVo.setRemodelDirectionCategory2Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setRemodelDirectionCategory2("");
                                        aVo.setRemodelDirectionCategory2Text("");
                                    }

                                    //対策指示小分類		
                                    if (null != aDetail.getRemodelDirectionCategory3()) {
                                        aVo.setRemodelDirectionCategory3("" + aDetail.getRemodelDirectionCategory3());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getRemodelDirectionCategory3()), loginUser.getLangId(), "tbl_mold_remodeling_detail.remodel_direction_category3");
                                        aVo.setRemodelDirectionCategory3Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setRemodelDirectionCategory3("");
                                        aVo.setRemodelDirectionCategory3Text("");
                                    }

                                    //対策指示
                                    aVo.setRemodelDirection(aDetail.getRemodelDirection() == null ? "" : aDetail.getRemodelDirection());
                                    //作業大分類		
                                    if (null != aDetail.getTaskCategory1()) {
                                        aVo.setTaskCategory1("" + aDetail.getTaskCategory1());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getTaskCategory1()), loginUser.getLangId(), "mst_mold_inspection_item.task_category1");
                                        aVo.setTaskCategory1Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setTaskCategory1("");
                                        aVo.setTaskCategory1Text("");
                                    }

                                    //作業中分類		
                                    if (null != aDetail.getTaskCategory2()) {
                                        aVo.setTaskCategory2("" + aDetail.getTaskCategory2());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getTaskCategory2()), loginUser.getLangId(), "mst_mold_inspection_item.task_category2");
                                        aVo.setTaskCategory2Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setTaskCategory2("");
                                        aVo.setTaskCategory2Text("");
                                    }

                                    //作業小分類	
                                    if (null != aDetail.getTaskCategory3()) {
                                        aVo.setTaskCategory3("" + aDetail.getTaskCategory3());
                                        MstChoice choice = mstChoiceService.getBySeqChoice(String.valueOf(aDetail.getTaskCategory3()), loginUser.getLangId(), "mst_mold_inspection_item.task_category3");
                                        aVo.setTaskCategory3Text(choice == null ? "" : choice.getChoice());
                                    } else {
                                        aVo.setTaskCategory3("");
                                        aVo.setTaskCategory3Text("");
                                    }
                                }
                            }
//                            if (aDetail.getRemodelReasonCategory1() != null) {
//                                aVo.setRemodelReasonCategory1(String.valueOf(aDetail.getRemodelReasonCategory1()));
//                                aVo.setRemodelReasonCategory1Text(aDetail.getRemodelReasonCategory1Text());
//                            } else {
//                                aVo.setRemodelReasonCategory1("");
//                                aVo.setRemodelReasonCategory1Text("");
//                            }
//                            //改造理由中分類
//                            if (aDetail.getRemodelReasonCategory2() != null) {
//                                aVo.setRemodelReasonCategory2(String.valueOf(aDetail.getRemodelReasonCategory2()));
//                                aVo.setRemodelReasonCategory2Text(aDetail.getRemodelReasonCategory2Text());
//                            } else {
//                                aVo.setRemodelReasonCategory2("");
//                                aVo.setRemodelReasonCategory2Text("");
//                            }
//
//                            //改造理由小分類
//                            if (aDetail.getRemodelReasonCategory3() != null) {
//                                aVo.setRemodelReasonCategory3(String.valueOf(aDetail.getRemodelReasonCategory3()));
//                                aVo.setRemodelReasonCategory3Text(aDetail.getRemodelReasonCategory3Text());
//                            } else {
//                                aVo.setRemodelReasonCategory3("");
//                                aVo.setRemodelReasonCategory3Text("");
//                            }
//
//                            //改造理由	
//                            aVo.setRemodelReason(aDetail.getRemodelReason() == null ? "" : aDetail.getRemodelReason());
//                            //改造指示大分類	
//                            if (aDetail.getRemodelDirectionCategory1() != null) {
//                                aVo.setRemodelDirectionCategory1(String.valueOf(aDetail.getRemodelDirectionCategory1()));
//                                aVo.setRemodelDirectionCategory1Text(aDetail.getRemodelDirectionCategory1Text());
//                            } else {
//                                aVo.setRemodelDirectionCategory1("");
//                                aVo.setRemodelDirectionCategory1Text("");
//                            }
//
//                            //改造指示中分類
//                            if (aDetail.getRemodelDirectionCategory2() != null) {
//                                aVo.setRemodelDirectionCategory2(String.valueOf(aDetail.getRemodelDirectionCategory2()));
//                                aVo.setRemodelDirectionCategory2Text(aDetail.getRemodelDirectionCategory2Text());
//                            } else {
//                                aVo.setRemodelDirectionCategory2("");
//                                aVo.setRemodelDirectionCategory2Text("");
//                            }
//
//                            //改造指示小分類
//                            if (aDetail.getRemodelDirectionCategory3() != null) {
//                                aVo.setRemodelDirectionCategory3(String.valueOf(aDetail.getRemodelDirectionCategory3()));
//                                aVo.setRemodelDirectionCategory3Text(aDetail.getRemodelDirectionCategory3Text());
//                            } else {
//                                aVo.setRemodelDirectionCategory3("");
//                                aVo.setRemodelDirectionCategory3Text("");
//                            }
//
//                            //改造指示
//                            aVo.setRemodelDirection(aDetail.getRemodelDirection() == null ? "" : aDetail.getRemodelDirection());
//                            //作業大分類		
//                            if (aDetail.getTaskCategory1() != null) {
//                                aVo.setTaskCategory1(String.valueOf(aDetail.getTaskCategory1()));
//                                aVo.setTaskCategory1Text(aDetail.getTaskCategory1Text());
//                            } else {
//                                aVo.setTaskCategory1("");
//                                aVo.setTaskCategory1Text("");
//                            }
//
//                            //作業中分類	
//                            if (aDetail.getTaskCategory2() != null) {
//                                aVo.setTaskCategory2(String.valueOf(aDetail.getTaskCategory2()));
//                                aVo.setTaskCategory2Text(aDetail.getTaskCategory2Text());
//                            } else {
//                                aVo.setTaskCategory2("");
//                                aVo.setTaskCategory2Text("");
//                            }
//
//                            //作業小分類	
//                            if (aDetail.getTaskCategory3() != null) {
//                                aVo.setTaskCategory3(String.valueOf(aDetail.getTaskCategory3()));
//                                aVo.setTaskCategory3Text(aDetail.getTaskCategory3Text());
//                            } else {
//                                aVo.setTaskCategory3("");
//                                aVo.setTaskCategory3Text("");
//                            }

                            //作業	
                            aVo.setTask(aDetail.getTask() == null ? "" : aDetail.getTask());

                            aVo.setSeq(aDetail.getTblMoldRemodelingDetailPK().getSeq());
                            
                            List<TblMoldRemodelingInspectionResultVo> remodelingInspectionResultVo = new ArrayList<>();
                            
                            StringBuilder sqlResult = new StringBuilder("SELECT t FROM TblMoldRemodelingInspectionResult t ");
                            sqlResult.append("LEFT JOIN FETCH TblMoldRemodelingDetail t2 ON ");
                            sqlResult.append("t.tblMoldRemodelingInspectionResultPK.remodelingDetailId = t2.id ");
                            sqlResult.append("WHERE t2.id = :id ");

                            Query queryResult = entityManager.createQuery(sqlResult.toString());

                            queryResult.setParameter("id", aDetail.getId());

                            List listResult = queryResult.getResultList();
                            
                            TblMoldRemodelingInspectionResultVo remodelingInspectionResultVos = null;
                            for( int i=0;i<listResult.size();i++){
                                TblMoldRemodelingInspectionResult tblMoldRemodelingInspectionResult = (TblMoldRemodelingInspectionResult)listResult.get(i);
                                remodelingInspectionResultVos = new TblMoldRemodelingInspectionResultVo();
                                remodelingInspectionResultVos.setId(tblMoldRemodelingInspectionResult.getId());
                                remodelingInspectionResultVos.setInspectionResult(tblMoldRemodelingInspectionResult.getInspectionResult());
                                remodelingInspectionResultVos.setInspectionResultText(tblMoldRemodelingInspectionResult.getInspectionResultText());
                                remodelingInspectionResultVos.setRemodelingDetailId(tblMoldRemodelingInspectionResult.getTblMoldRemodelingInspectionResultPK().getRemodelingDetailId());
                                remodelingInspectionResultVos.setSeq(tblMoldRemodelingInspectionResult.getTblMoldRemodelingInspectionResultPK().getSeq());
                                remodelingInspectionResultVos.setInspectionItemId(tblMoldRemodelingInspectionResult.getTblMoldRemodelingInspectionResultPK().getInspectionItemId());
                                if(tblMoldRemodelingInspectionResult.getMstMoldInspectionItem() != null){
                                    remodelingInspectionResultVos.setInspectionItemName(tblMoldRemodelingInspectionResult.getMstMoldInspectionItem().getInspectionItemName());
                                    if(tblMoldRemodelingInspectionResult.getMstMoldInspectionItem().getResultType() != null){
                                        remodelingInspectionResultVos.setResultType(String.valueOf(tblMoldRemodelingInspectionResult.getMstMoldInspectionItem().getResultType()));
                                    }else{
                                        remodelingInspectionResultVos.setResultType("");
                                    }
                                }else{
                                    remodelingInspectionResultVos.setInspectionItemName("");
                                    remodelingInspectionResultVos.setResultType("");
                                }
                                
                                remodelingInspectionResultVo.add(remodelingInspectionResultVos);
                            }
                            aVo.setMoldRemodelingInspectionResultVos(remodelingInspectionResultVo);
                            
                            
                            List<TblMoldRemodelingDetailImageFileVo> fileResultVos = new ArrayList<>();
                            //TblMoldRemodelingDetailImageFileVo　検索
                            List<TblMoldRemodelingDetailImageFile> tmpFileResults = entityManager.createQuery("SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.tblMoldRemodelingDetailImageFilePK.remodelingDetailId = :remodelingDetailId ")
                                    .setParameter("remodelingDetailId", aDetail.getId())
                                    .getResultList();

                            TblMoldRemodelingDetailImageFileVo moldRemodelingDetailImageFileVo = null;
                            for (int i = 0; i < tmpFileResults.size(); i++) {
                                TblMoldRemodelingDetailImageFile aFile = tmpFileResults.get(i);
                                moldRemodelingDetailImageFileVo = new TblMoldRemodelingDetailImageFileVo();
                                moldRemodelingDetailImageFileVo.setRemodelingDetailId(aDetail.getId());
                                moldRemodelingDetailImageFileVo.setSeq("" + aFile.getTblMoldRemodelingDetailImageFilePK().getSeq());
                                moldRemodelingDetailImageFileVo.setFileType("" + aFile.getFileType());
                                moldRemodelingDetailImageFileVo.setFileExtension(aFile.getFileExtension());
                                moldRemodelingDetailImageFileVo.setFileUuid(aFile.getFileUuid());
                                moldRemodelingDetailImageFileVo.setRemarks(aFile.getRemarks());
                                moldRemodelingDetailImageFileVo.setThumbnailFileUuid(aFile.getThumbnailFileUuid());
                                if (null != aFile.getTakenDate()) {
                                    moldRemodelingDetailImageFileVo.setTakenDateStr(new FileUtil().getDateTimeFormatForStr(aFile.getTakenDate()));
                                }
                                if (null != aFile.getTakenDateStz()) {
                                    moldRemodelingDetailImageFileVo.setTakenDateStzStr(new FileUtil().getDateTimeFormatForStr(aFile.getTakenDateStz()));
                                }

                                fileResultVos.add(moldRemodelingDetailImageFileVo);
                            }
                            aVo.setMoldRemodelingDetailImageFileVos(fileResultVos);
                            
                            
                            resVo.getMoldRemodelingDetailVo().add(aVo);
                        }
                    }
                }
        } catch (NoResultException e) {
            return resVo;
        }
        return resVo;
    }

    /**
     * CSV出力
     *
     *
     * @param queryVo
     * @param loginUser
     * @return
     */
    public FileReponse getTblMoldMaintenanceRemodelingOutputCsv(TblMoldMaintenanceRemodelingVo queryVo, LoginUser loginUser) {
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        ArrayList headList = new ArrayList();
        ArrayList lineList;
        String langId = loginUser.getLangId();
        FileReponse fr = new FileReponse();
        FileUtil fu = new FileUtil();
        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        String outMoldMainteDate = mstDictionaryService.getDictionaryValue(langId, "mold_mainte_date");
        String outMainteOrRemodel = mstDictionaryService.getDictionaryValue(langId, "mainte_or_remodel");
        String outMoldId = mstDictionaryService.getDictionaryValue(langId, "mold_id");
        String outMoldName = mstDictionaryService.getDictionaryValue(langId, "mold_name");
        String outMoldInventoryPerson = mstDictionaryService.getDictionaryValue(langId, "mold_inventory_person");
        String outMoldMainteType = mstDictionaryService.getDictionaryValue(langId, "mold_mainte_type");
        String outMoldRemodelingType = mstDictionaryService.getDictionaryValue(langId, "mold_remodeling_type");
        String outMaintenanceRemodelingStartDatetime = mstDictionaryService.getDictionaryValue(langId, "maintenance_remodeling_start_datetime");
        String outMaintenanceRemodelingEndDatetime = mstDictionaryService.getDictionaryValue(langId, "maintenance_remodeling_end_datetime");
        String outMaintenanceWorkingTimeMinutes = mstDictionaryService.getDictionaryValue(langId, "maintenance_working_time_minutes");//所要時間 20170901 Apeng add
        String outMoldMaintenanceRemodelingReport = mstDictionaryService.getDictionaryValue(langId, "mold_maintenance_remodeling_report");
        String outDirectionCode = mstDictionaryService.getDictionaryValue(langId, "direction_code");

        /*Head*/
        headList.add(outMoldMainteDate);
        headList.add(outMainteOrRemodel);
        headList.add(outMoldId);
        headList.add(outMoldName);
        headList.add(outMoldInventoryPerson);
        headList.add(outMoldMainteType);
        headList.add(outMoldRemodelingType);
        headList.add(outMaintenanceRemodelingStartDatetime);
        headList.add(outMaintenanceRemodelingEndDatetime);
        headList.add(outMaintenanceWorkingTimeMinutes);//所要時間 20170901 Apeng add
        headList.add(outMoldMaintenanceRemodelingReport);
        headList.add(outDirectionCode);

        gLineList.add(headList);
        //明細データを取得
        BasicResponse br = getMaintenanceRemodeling(queryVo, loginUser);
        TblMoldMaintenanceRemodelingVo vo = (TblMoldMaintenanceRemodelingVo) br;
        List<TblMoldMaintenanceRemodelingVo> resList = vo.getMoldMaintenanceRemodelingVo();
        /*Detail*/
        for (int i = 0; i < resList.size(); i++) {
            lineList = new ArrayList();
            TblMoldMaintenanceRemodelingVo tblMoldMaintenanceRemodelingVo = resList.get(i);
            lineList.add(fu.getDateFormatForStr(tblMoldMaintenanceRemodelingVo.getMainteDate()));
            lineList.add(tblMoldMaintenanceRemodelingVo.getMainteOrRemodelText());
            lineList.add(tblMoldMaintenanceRemodelingVo.getMoldId());
            lineList.add(tblMoldMaintenanceRemodelingVo.getMoldName());
            lineList.add(tblMoldMaintenanceRemodelingVo.getReportPersonName());
            lineList.add(tblMoldMaintenanceRemodelingVo.getMainteTypeText());
            lineList.add(tblMoldMaintenanceRemodelingVo.getRemodelingTypeText());
            lineList.add((tblMoldMaintenanceRemodelingVo.getStartDatetime()));
            lineList.add((tblMoldMaintenanceRemodelingVo.getEndDatetime()));
            lineList.add(String.valueOf(tblMoldMaintenanceRemodelingVo.getWorkingTimeMinutes()));//所要時間 20170901 Apeng add
            lineList.add(tblMoldMaintenanceRemodelingVo.getReport());
            lineList.add(tblMoldMaintenanceRemodelingVo.getDirectionCode());
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
        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        tblCsvExport.setExportTable(CommonConstants.TBL_MOLD_MAINTENANCE_REMODELING);
        tblCsvExport.setExportDate(new Date());
        MstFunction mstFunction = new MstFunction();
        mstFunction.setId(CommonConstants.FUN_ID_MOLD_MAINTENANCE_REMODELING);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, CommonConstants.TBL_MOLD_MAINTENANCE_REMODELING);
        tblCsvExport.setClientFileName(fu.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }

    /**
     * バッチで金型メンテナンス改造テーブルデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @param moldUuid
     * @return
     */
    public TblMoldMaintenanceRemodelingList getExtMoldMaintenanceRemodelingsByBatch(String latestExecutedDate, String moldUuid) {
        TblMoldMaintenanceRemodelingList resList = new TblMoldMaintenanceRemodelingList();
        List<TblMoldMaintenanceRemodelingVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblMoldMaintenanceRemodeling t join MstApiUser u on u.companyId = t.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.mstMold.uuid = :moldUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            query.setParameter("moldUuid", moldUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMoldMaintenanceRemodeling> tmpList = query.getResultList();
        for (TblMoldMaintenanceRemodeling tblMoldMaintenanceRemodeling : tmpList) {
            TblMoldMaintenanceRemodelingVo aRes = new TblMoldMaintenanceRemodelingVo();
            tblMoldMaintenanceRemodeling.setMoldSpecHstId(null);
            aRes.setMoldId(tblMoldMaintenanceRemodeling.getMstMold().getMoldId());
            tblMoldMaintenanceRemodeling.setMstMold(null);
            tblMoldMaintenanceRemodeling.setTblIssue(null);
            tblMoldMaintenanceRemodeling.setTblIssueCollection(null);
            tblMoldMaintenanceRemodeling.setTblMoldMaintenanceDetailCollection(null);
            tblMoldMaintenanceRemodeling.setTblMoldRemodelingDetailCollection(null);
            
            aRes.setTblMoldMaintenanceRemodeling(tblMoldMaintenanceRemodeling);
            resVo.add(aRes);
        }
        resList.setTblMoldMaintenanceRemodelingVoList(resVo);
        return resList;
    }

    /**
     * バッチで金型メンテナンス改造テーブルデータを更新
     *
     * @param moldMaintenanceRemodelingVos
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldMaintenanceRemodelingsByBatch(List<TblMoldMaintenanceRemodelingVo> moldMaintenanceRemodelingVos) {
        BasicResponse response = new BasicResponse();

        if (moldMaintenanceRemodelingVos != null && !moldMaintenanceRemodelingVos.isEmpty()) {
            for (TblMoldMaintenanceRemodelingVo aVo : moldMaintenanceRemodelingVos) {
                if (null != aVo.getTblMoldMaintenanceRemodeling().getIssueId()) {
                    TblIssue issue = entityManager.find(TblIssue.class, aVo.getTblMoldMaintenanceRemodeling().getIssueId());
                    if (null == issue) {
                        continue;
                    }
                }
               
                MstMold ownerMold = entityManager.find(MstMold.class, aVo.getMoldId());
                if (null != ownerMold) {
                    List<TblMoldMaintenanceRemodeling> oldMMRs = entityManager.createQuery("SELECT t FROM TblMoldMaintenanceRemodeling t WHERE 1=1 and t.id=:id ")
                            .setParameter("id", aVo.getTblMoldMaintenanceRemodeling().getId())
                            .setMaxResults(1)
                            .getResultList();
                    TblMoldMaintenanceRemodeling newMoldMaintenanceRemodeling;
                    if (null != oldMMRs && !oldMMRs.isEmpty()) {
                        newMoldMaintenanceRemodeling = oldMMRs.get(0);
                    } else {
                        newMoldMaintenanceRemodeling = new TblMoldMaintenanceRemodeling();
                        newMoldMaintenanceRemodeling.setId(aVo.getTblMoldMaintenanceRemodeling().getId());
                    }
                    //自社の金型UUIDに変換                
                    newMoldMaintenanceRemodeling.setMoldUuid(ownerMold.getUuid());

                    newMoldMaintenanceRemodeling.setMainteOrRemodel(aVo.getTblMoldMaintenanceRemodeling().getMainteOrRemodel());
                    newMoldMaintenanceRemodeling.setIssueId(aVo.getTblMoldMaintenanceRemodeling().getIssueId());
                    newMoldMaintenanceRemodeling.setMainteType(aVo.getTblMoldMaintenanceRemodeling().getMainteType());
                    newMoldMaintenanceRemodeling.setRemodelingType(aVo.getTblMoldMaintenanceRemodeling().getRemodelingType());
                    newMoldMaintenanceRemodeling.setMainteTypeText(aVo.getTblMoldMaintenanceRemodeling().getMainteTypeText());
                    newMoldMaintenanceRemodeling.setMainteDate(aVo.getTblMoldMaintenanceRemodeling().getMainteDate());
                    newMoldMaintenanceRemodeling.setStartDatetime(aVo.getTblMoldMaintenanceRemodeling().getStartDatetime());
                    newMoldMaintenanceRemodeling.setStartDatetimeStz(aVo.getTblMoldMaintenanceRemodeling().getStartDatetimeStz());
                    newMoldMaintenanceRemodeling.setEndDatetime(aVo.getTblMoldMaintenanceRemodeling().getEndDatetime());
                    newMoldMaintenanceRemodeling.setEndDatetimeStz(aVo.getTblMoldMaintenanceRemodeling().getEndDatetimeStz());
                    newMoldMaintenanceRemodeling.setReport(aVo.getTblMoldMaintenanceRemodeling().getReport());
                    newMoldMaintenanceRemodeling.setMoldSpecHstIdStr(aVo.getTblMoldMaintenanceRemodeling().getMoldSpecHstIdStr()); 
                    /// KM-260 LYD 追加　S
                    newMoldMaintenanceRemodeling.setDirectionCode(aVo.getTblMoldMaintenanceRemodeling().getDirectionCode());
                    /// KM-260 LYD 追加　E
                    newMoldMaintenanceRemodeling.setCreateDate(aVo.getTblMoldMaintenanceRemodeling().getCreateDate());
                    newMoldMaintenanceRemodeling.setCreateUserUuid(aVo.getTblMoldMaintenanceRemodeling().getCreateUserUuid());
                    newMoldMaintenanceRemodeling.setUpdateDate(new Date());
                    newMoldMaintenanceRemodeling.setUpdateUserUuid(aVo.getTblMoldMaintenanceRemodeling().getUpdateUserUuid());

                    if (null != oldMMRs && !oldMMRs.isEmpty()) {
                        entityManager.merge(newMoldMaintenanceRemodeling);
                    } else {
                        entityManager.persist(newMoldMaintenanceRemodeling);
                    }
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
    
    public BasicResponse getMaintenanceRemodelingByPage(TblMoldMaintenanceRemodelingVo queryVo, LoginUser loginUser,
            String sidx, String sord, int pageNumber, int pageSize, boolean isPage) {

        TblMoldMaintenanceRemodelingVo resVo = new TblMoldMaintenanceRemodelingVo();

        if (isPage) {

            List count = queryByPage(queryVo, sidx, sord, pageNumber, pageSize, true);

            // ページをめぐる
            Pager pager = new Pager();
            resVo.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            resVo.setCount(counts);
            resVo.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }

        List<TblMoldMaintenanceRemodelingVo> moldMaintenanceRemodelingVos = new ArrayList<>();
        List<TblMoldMaintenanceRemodeling> moldMaintenanceRemodelings = queryByPage(queryVo, sidx, sord, pageNumber,
                pageSize, false);
        if (null != moldMaintenanceRemodelings && !moldMaintenanceRemodelings.isEmpty()) {
            MstChoiceList mainteOrRemodelChoiceList = mstChoiceService.getChoice(loginUser.getLangId(),
                    "tbl_mold_maintenance_remodeling.mainte_or_remodel");
            MstChoiceList remodelingTypeChoiceList = mstChoiceService.getChoice(loginUser.getLangId(),
                    "tbl_mold_maintenance_remodeling.remodeling_type");
            MstChoiceList mainteTypeChoiceList = mstChoiceService.getChoice(loginUser.getLangId(),
                    "tbl_mold_maintenance_remodeling.mainte_type");
            for (int i = 0; i < moldMaintenanceRemodelings.size(); i++) {
                TblMoldMaintenanceRemodeling aMoldMaintenanceRemodeling = moldMaintenanceRemodelings.get(i);
                TblMoldMaintenanceRemodelingVo aVo = new TblMoldMaintenanceRemodelingVo();
                aVo.setId(aMoldMaintenanceRemodeling.getId() == null ? "" : aMoldMaintenanceRemodeling.getId());
                if (aMoldMaintenanceRemodeling.getMstMold() != null) {
                    // 金型ID
                    aVo.setMoldId(aMoldMaintenanceRemodeling.getMstMold().getMoldId());
                    // 金型名称
                    aVo.setMoldName(aMoldMaintenanceRemodeling.getMstMold().getMoldName());
                } else {
                    // 金型ID
                    aVo.setMoldId("");
                    // 金型名称
                    aVo.setMoldName("");
                }

                // メンテナンス日
                if (null != aMoldMaintenanceRemodeling.getMainteDate()) {
                    aVo.setMainteDate(aMoldMaintenanceRemodeling.getMainteDate());
                    aVo.setMainteDateText(
                            DateFormat.dateFormat(aMoldMaintenanceRemodeling.getMainteDate(), loginUser.getLangId()));
                }
                // 報告事項
                aVo.setReport(
                        aMoldMaintenanceRemodeling.getReport() == null ? "" : aMoldMaintenanceRemodeling.getReport());
                // 所要時間
                aVo.setWorkingTimeMinutes(aMoldMaintenanceRemodeling.getWorkingTimeMinutes());// 20170901
                                                                                              // Apeng
                                                                                              // add
                // 工事ID
                aVo.setDirectionId(aMoldMaintenanceRemodeling.getDirectionId() == null ? ""
                        : aMoldMaintenanceRemodeling.getDirectionId());
                if (aMoldMaintenanceRemodeling.getTblDirection() != null) {
                    aVo.setDirectionCode(aMoldMaintenanceRemodeling.getTblDirection().getDirectionCode() == null ? ""
                            : aMoldMaintenanceRemodeling.getTblDirection().getDirectionCode());
                } else {
                    if (aMoldMaintenanceRemodeling.getDirectionCode() != null) {
                        aVo.setDirectionCode(aMoldMaintenanceRemodeling.getDirectionCode());
                    } else {
                        aVo.setDirectionCode("");
                    }
                }

                // 金型外部データflag
                boolean externalMoldFlag = false;
                if (aMoldMaintenanceRemodeling.getMstMold() != null) {
                    String moldId = aMoldMaintenanceRemodeling.getMstMold().getMoldId();
                    externalMoldFlag = FileUtil.checkExternal(entityManager, mstDictionaryService, moldId, loginUser)
                            .isError();
                    if (externalMoldFlag) {
                        aVo.setExternalFlg("1");
                    } else {
                        aVo.setExternalFlg("0");
                    }
                }

                // 改造・メンテナンス区分
                if (mainteOrRemodelChoiceList != null && mainteOrRemodelChoiceList.getMstChoice() != null) {
                    if (aMoldMaintenanceRemodeling.getMainteOrRemodel() != null) {
                        aVo.setMainteOrRemodel(aMoldMaintenanceRemodeling.getMainteOrRemodel());
                        for (int momi = 0; momi < mainteOrRemodelChoiceList.getMstChoice().size(); momi++) {
                            MstChoice aMstChoice = mainteOrRemodelChoiceList.getMstChoice().get(momi);
                            if (aMstChoice.getMstChoicePK().getSeq()
                                    .equals(aMoldMaintenanceRemodeling.getMainteOrRemodel().toString())) {
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
                if (aMoldMaintenanceRemodeling.getMstUser() != null) {
                    aVo.setReportPersonName(aMoldMaintenanceRemodeling.getMstUser().getUserName());
                } else {
                    aVo.setReportPersonName("");
                }

                if (externalMoldFlag) {
                    // 外部
                    // メンテナンス分類
                    if (null != aMoldMaintenanceRemodeling.getMainteType()) {
                        aVo.setMainteTypeText(extMstChoiceService.getExtMstChoiceText(
                                aMoldMaintenanceRemodeling.getMstMold().getCompanyId(),
                                "tbl_mold_maintenance_remodeling.mainte_type",
                                String.valueOf(aMoldMaintenanceRemodeling.getMainteType()), loginUser.getLangId()));
                    } else {
                        aVo.setMainteTypeText("");
                    }

                    // 改造分類
                    if (null != aMoldMaintenanceRemodeling.getRemodelingType()) {
                        aVo.setRemodelingTypeText(extMstChoiceService.getExtMstChoiceText(
                                aMoldMaintenanceRemodeling.getMstMold().getCompanyId(),
                                "tbl_mold_maintenance_remodeling.remodeling_type",
                                String.valueOf(aMoldMaintenanceRemodeling.getRemodelingType()), loginUser.getLangId()));
                    } else {
                        aVo.setRemodelingTypeText("");
                    }
                } else {
                    // メンテナンス分類
                    if (null == aMoldMaintenanceRemodeling.getMainteType()) {
                        aVo.setMainteTypeText("");
                    } else {
                        aVo.setRemodelingType(aMoldMaintenanceRemodeling.getMainteType());
                        if (mainteTypeChoiceList != null && mainteTypeChoiceList.getMstChoice() != null) {
                            for (int momi = 0; momi < mainteTypeChoiceList.getMstChoice().size(); momi++) {
                                MstChoice aMstChoice = mainteTypeChoiceList.getMstChoice().get(momi);
                                if (aMstChoice.getMstChoicePK().getSeq()
                                        .equals(aMoldMaintenanceRemodeling.getMainteType().toString())) {
                                    aVo.setMainteTypeText(aMstChoice.getChoice());
                                    break;
                                }
                            }
                        } else {
                            aVo.setMainteTypeText("");
                        }
                    }

                    // 改造分類
                    if (null == aMoldMaintenanceRemodeling.getRemodelingType()) {
                        aVo.setRemodelingTypeText("");
                    } else {
                        aVo.setRemodelingType(aMoldMaintenanceRemodeling.getRemodelingType());
                        if (remodelingTypeChoiceList != null && remodelingTypeChoiceList.getMstChoice() != null) {
                            for (int momi = 0; momi < remodelingTypeChoiceList.getMstChoice().size(); momi++) {
                                MstChoice aMstChoice = remodelingTypeChoiceList.getMstChoice().get(momi);
                                if (aMstChoice.getMstChoicePK().getSeq()
                                        .equals(aMoldMaintenanceRemodeling.getRemodelingType().toString())) {
                                    aVo.setRemodelingTypeText(aMstChoice.getChoice());
                                    break;
                                }
                            }
                        } else {
                            aVo.setRemodelingTypeText("");
                        }
                    }
                }

                // 開始日時
                aVo.setStartDatetime(
                        new FileUtil().getDateTimeFormatForStr(aMoldMaintenanceRemodeling.getStartDatetime()));

                // 終了日時
                if (aMoldMaintenanceRemodeling.getEndDatetime() != null) {
                    aVo.setEndDatetime(
                            aMoldMaintenanceRemodeling.getEndDatetime().compareTo(CommonConstants.SYS_MAX_DATE) == 0
                                    ? "-"
                                    : new FileUtil()
                                            .getDateTimeFormatForStr(aMoldMaintenanceRemodeling.getEndDatetime()));
                } else {
                    aVo.setEndDatetime("-");
                }
                
                aVo.setIssueId(aMoldMaintenanceRemodeling.getIssueId());

                moldMaintenanceRemodelingVos.add(aVo);
            }
        }

        resVo.setMoldMaintenanceRemodelingVo(moldMaintenanceRemodelingVos);

        return resVo;

    }

    public List queryByPage(TblMoldMaintenanceRemodelingVo vo, String sidx, String sord, int pageNumber, int pageSize,
            boolean isCount) {

        StringBuilder sql = new StringBuilder(" SELECT ");
        if (isCount) {
            sql.append(" count(mr) FROM TblMoldMaintenanceRemodeling mr JOIN FETCH mr.mstMold mstMold ");
        } else {
            sql.append(" mr FROM TblMoldMaintenanceRemodeling mr JOIN FETCH mr.mstMold mstMold "
                    + "LEFT JOIN FETCH mr.mstUser mu ");
        }
        sql.append(" where 1=1 ");
        if (null != vo.getMoldId() && !vo.getMoldId().equals("")) {
            sql.append(" and mr.mstMold.moldId like :moldId ");
        }
        if (null != vo.getMoldName() && !vo.getMoldName().equals("")) {
            sql.append(" and mr.mstMold.moldName like :moldName ");
        }
        if (null != vo.getMoldUuid() && !vo.getMoldUuid().equals("")) {
            sql.append(" and mr.mstMold.uuid = :moldUuid ");
        }
        if (null != vo.getMainteDateStart()) {
            sql.append(" and mr.mainteDate >= :mainteDateStart ");
        }
        // 所属のついか--------------------------------
        if (null != vo.getMstMold().getDepartment() && 0 != vo.getMstMold().getDepartment()) {
            sql.append(" and mstMold.department = :department ");
        }
        // -------------------------------------------
        if (null != vo.getMainteDateEnd()) {
            sql.append(" and mr.mainteDate <= :mainteDateEnd ");
        }
        if (null != vo.getMainteOrRemodel() && 0 != vo.getMainteOrRemodel()) {
            sql.append(" and mr.mainteOrRemodel = :mainteOrRemodel ");
        }
        if (null != vo.getMainteStatus() && "1".equals(vo.getMainteStatus())) {
            // メンテ・改造中
            sql.append(
                    " and (mr.mstMold.mainteStatus = :mainteStatus1 or mr.mstMold.mainteStatus = :mainteStatus2 ) and mr.endDatetime IS NULL ");
        } else if (null != vo.getMainteStatus() && "m".equals(vo.getMainteStatus())) {
            // メンテ
            sql.append(" and (mr.mstMold.mainteStatus = :mainteStatus1 ) and mr.endDatetime IS NULL ");
        } else if (null != vo.getMainteStatus() && "r".equals(vo.getMainteStatus())) {
            // 改造中
            sql.append(" and (mr.mstMold.mainteStatus = :mainteStatus2 ) and mr.endDatetime IS NULL ");
        }
        
        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {

                String sortStr = orderKey.get(sidx) + " " + sord;

                sql.append(sortStr);

            } else {

                sql.append(" order by mr.mainteDate desc,mr.startDatetime desc,mr.mstMold.moldId asc ");

            }

        }
        
        Query query = entityManager.createQuery(sql.toString());
        if (null != vo.getMoldId() && !vo.getMoldId().equals("")) {
            query.setParameter("moldId", "%" + vo.getMoldId() + "%");
        }
        if (null != vo.getMoldName() && !vo.getMoldName().equals("")) {
            query.setParameter("moldName", "%" + vo.getMoldName() + "%");
        }
        if (null != vo.getMoldUuid() && !vo.getMoldUuid().equals("")) {
            query.setParameter("moldUuid", vo.getMoldUuid());
        }
        if (null != vo.getMainteDateStart()) {
            query.setParameter("mainteDateStart", vo.getMainteDateStart());
        }
        // 2017.08.25所属のついか--------------------------------
        if (null != vo.getMstMold().getDepartment() && 0 != vo.getMstMold().getDepartment()) {
            query.setParameter("department", vo.getMstMold().getDepartment());
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
            // メンテ
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
