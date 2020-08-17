/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.inspection.result.TblMoldInspectionResult;
import com.kmcj.karte.resources.mold.inspection.result.TblMoldInspectionResultPK;
import com.kmcj.karte.resources.mold.inspection.result.TblMoldInspectionResultVo;
import com.kmcj.karte.resources.mold.issue.TblIssue;
import com.kmcj.karte.resources.mold.maintenance.part.TblMoldMaintenancePart;
import com.kmcj.karte.resources.mold.maintenance.part.TblMoldMaintenancePartPK;
import com.kmcj.karte.resources.mold.maintenance.part.TblMoldMaintenancePartService;
import com.kmcj.karte.resources.mold.maintenance.recomend.TblMoldMaintenanceRecomend;
import com.kmcj.karte.resources.mold.maintenance.recomend.TblMoldMaintenanceRecomendList;
import com.kmcj.karte.resources.mold.maintenance.recomend.TblMoldMaintenanceRecomendService;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodeling;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodelingService;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodelingVo;
import com.kmcj.karte.resources.mold.part.maintenance.recommend.TblMoldPartMaintenanceRecommend;
import com.kmcj.karte.resources.mold.part.maintenance.recommend.TblMoldPartMaintenanceRecommendService;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRel;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRelService;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStock;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStockService;
import com.kmcj.karte.resources.mold.part.stock.inout.MoldPartStockInOut;
import com.kmcj.karte.resources.mold.part.stock.inout.MoldPartStockInOutService;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetail;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailImageFile;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailImageFilePK;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailImageFileVo;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailPK;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailVo;
import com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResult;
import com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResultPK;
import com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResultVo;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.TimezoneConverter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
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
public class TblMoldMaintenanceDetailService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private TblMoldMaintenanceRemodelingService tblMoldMaintenanceRemodelingService;
    @Inject
    private TblMoldMaintenanceRecomendService tblMoldMaintenanceRecomendService;
    @Inject
    private CnfSystemService cnfSystemService;
    @Inject
    private TblMoldMaintenancePartService tblMoldMaintenancePartService;
    @Inject
    private TblMoldPartMaintenanceRecommendService tblMoldPartMaintenanceRecommendService;
    @Inject
    private MstMoldPartRelService mstMoldPartRelService;
    @Inject
    private MoldPartStockService moldPartStockService;
    @Inject
    private MoldPartStockInOutService moldPartStockInOutService;

    /**
     *
     * @param maintenanceId
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse startCancelMoldMaintenance(String maintenanceId, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

        TblMoldMaintenanceRemodeling aMaintenanceRemodeling = entityManager.find(TblMoldMaintenanceRemodeling.class, maintenanceId);
        MstMold aMstMold = null;
        Query moldQuery = entityManager.createNamedQuery("MstMold.findByUuid");
        moldQuery.setParameter("uuid", aMaintenanceRemodeling.getMoldUuid());
        List<MstMold> molds = moldQuery.getResultList();
        if (null != molds && !molds.isEmpty()) {
            aMstMold = molds.get(0);
        }
        if (null != aMstMold) {
            //外部データがチェック add 2017-1-16 13:57:30 jiangxiaosong
            basicResponse = FileUtil.checkExternal(entityManager, mstDictionaryService, aMstMold.getMoldId(), loginUser);
            if(basicResponse.isError()){
                return basicResponse;
            }
            
            if (null != aMstMold.getMainteStatus() && aMstMold.getMainteStatus() == CommonConstants.MAINTE_STATUS_NORMAL) {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_can_not_cancel"));
                return basicResponse;
            } else if (null != aMstMold.getMainteStatus() && aMstMold.getMainteStatus() == 2) {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_remodeling"));
                return basicResponse;
            } else {
                //金型マスタ 更新 状態をメンテナンス中から通常に戻す
                aMstMold.setMainteStatus(CommonConstants.MAINTE_STATUS_NORMAL); //状態をメンテナンス中から通常に戻す
                aMstMold.setUpdateDate(new Date());
                aMstMold.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.merge(aMstMold);

                //金型メンテナンス詳細 削除
                Query maintenanceDetailQuery = entityManager.createQuery("SELECT d from TblMoldMaintenanceDetail d where d.tblMoldMaintenanceDetailPK.maintenanceId = :maintenanceId ");
                maintenanceDetailQuery.setParameter("maintenanceId", maintenanceId);
                List<TblMoldMaintenanceDetail> details = maintenanceDetailQuery.getResultList();
                if (null != details && !details.isEmpty()) {
                    for (int i = 0; i < details.size(); i++) {
                        TblMoldMaintenanceDetail aDetail = details.get(i);

                        //金型点検結果 削除
                        Query delInspectionResultlQuery = entityManager.createNamedQuery("TblMoldInspectionResult.deleteByMaintenanceDetailId");
                        delInspectionResultlQuery.setParameter("maintenanceDetailId", aDetail.getId());
                        delInspectionResultlQuery.executeUpdate();

//                    entityManager.remove(aDetail); //cascade delete
                    }
                }
                //金型部品在庫の調整
                moldPartStockInOutService.cancelMaintenance(maintenanceId, loginUser);
                //金型改造メンテナンス 削除
                entityManager.remove(aMaintenanceRemodeling);
            }
        }

        return basicResponse;
    }

    /**
     *
     * @param moldId
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse resumptionMoldMaintenance(String moldId, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

        MstMold aMstMold = entityManager.find(MstMold.class, moldId);
        if (null != aMstMold) {
            //外部データがチェック add 2017-1-16 13:57:30 jiangxiaosong
            basicResponse = FileUtil.checkExternal(entityManager, mstDictionaryService, aMstMold.getMoldId(), loginUser);
            if(basicResponse.isError()){
                return basicResponse;
            }
            
            if (null != aMstMold.getMainteStatus() && aMstMold.getMainteStatus() == CommonConstants.MAINTE_STATUS_NORMAL) {
                aMstMold.setMainteStatus(CommonConstants.MAINTE_STATUS_MAINTE); //メンテナンス中
                aMstMold.setUpdateDate(new Date());
                aMstMold.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.merge(aMstMold);

                TblMoldMaintenanceRemodelingVo tblMoldMaintenanceRemodelingVo = (TblMoldMaintenanceRemodelingVo) tblMoldMaintenanceRemodelingService.getMoldmainteOrRemodelDetail(moldId, CommonConstants.MAINTEORREMODEL_MAINTE, loginUser);
                String id = tblMoldMaintenanceRemodelingVo.getId();

                StringBuilder sql = new StringBuilder(" UPDATE TblMoldMaintenanceRemodeling t SET ");
//                sql.append(" t.startDatetime = :startDatetime, ");
//                sql.append(" t.startDatetimeStz = :startDatetimeStz, ");
                sql.append(" t.endDatetime = :endDatetime, ");
                sql.append(" t.endDatetimeStz = :endDatetimeStz, ");
                sql.append(" t.updateDate = :updateDate, ");
                sql.append(" t.updateUserUuid = :updateUserUuid ");
                sql.append(" WHERE t.id = :id ");

                Query query = entityManager.createQuery(sql.toString());

//                query.setParameter("startDatetime", TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
//                query.setParameter("startDatetimeStz", new Date());
                query.setParameter("endDatetime", null);
                query.setParameter("endDatetimeStz", null);
                query.setParameter("updateDate", new Date());
                query.setParameter("updateUserUuid", loginUser.getUserUuid());
                query.setParameter("id", id);

                query.executeUpdate();

                //  KM-358	メンテナンス開始画面仕様変更
                //  再開＞開始時には対策ステータスを対応中にする。
                if (StringUtils.isNotEmpty(tblMoldMaintenanceRemodelingVo.getIssueId())) {
                    TblIssue issue = entityManager.find(TblIssue.class, tblMoldMaintenanceRemodelingVo.getIssueId());
                    issue.setMeasureStatus(CommonConstants.ISSUE_MEASURE_STATUS_RESOLVING);
                    issue.setUpdateDate(new Date());
                    issue.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.merge(issue);
                }
                
            } else if (null != aMstMold.getMainteStatus() && aMstMold.getMainteStatus() == 2) {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_remodeling"));
                return basicResponse;
            } else {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_end"));
                return basicResponse;
            }
        }

        return basicResponse;
    }
    
    /**
     *
     * @param moldMaintenanceRemodelingVo
     * @param type
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postMoldMaintenanceDetailes(TblMoldMaintenanceRemodelingVo moldMaintenanceRemodelingVo, int type, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        basicResponse = FileUtil.checkExternal(entityManager, mstDictionaryService, moldMaintenanceRemodelingVo.getMoldId(), loginUser);
        if(basicResponse.isError()){
            return basicResponse;
        }
        
        //金型マスタ 更新 「通常」状態を解除
        // メンテナンスの一次保存機能 2018/09/13 -S
        MstMold aMstMold = null;
        if (0 == moldMaintenanceRemodelingVo.getTemporarilySaved()) {
            aMstMold = entityManager.find(MstMold.class, moldMaintenanceRemodelingVo.getMoldId());

            aMstMold.setMainteStatus(CommonConstants.MAINTE_STATUS_NORMAL);
            // 4.2 対応　BY LiuYoudong S
            CnfSystem cnfSystem = cnfSystemService.findByKey("system", "business_start_time");
            aMstMold.setLastMainteDate(DateFormat.strToDate(DateFormat.getBusinessDate(DateFormat.dateToStr(new Date(), DateFormat.DATETIME_FORMAT), cnfSystem))); // 最終メンテナンス日
            if (moldMaintenanceRemodelingVo.isResetAfterMainteTotalProducingTimeHourFlag()) {
                aMstMold.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO); // メンテナンス後生産時間
            }
            if (moldMaintenanceRemodelingVo.isResetAfterMainteTotalShotCountFlag()) {
                aMstMold.setAfterMainteTotalShotCount(0); // メンテナンス後ショット数
            }
            // 4.2 対応　BY LiuYoudong E
            aMstMold.setUpdateDate(new Date());
            aMstMold.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(aMstMold);
        }
        // メンテナンスの一次保存機能 2018/09/13 -E

        //金型改造メンテナンス 更新 メンテナンス分類、報告事項、終了日時をセット
        TblMoldMaintenanceRemodeling moldMaintenanceRemodeling = entityManager.find(TblMoldMaintenanceRemodeling.class, moldMaintenanceRemodelingVo.getMaintenanceId());
        if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {
            moldMaintenanceRemodeling.setMainteType(moldMaintenanceRemodelingVo.getMainteType());
        } else if (type == CommonConstants.MAINTEORREMODEL_REMODEL) {
            moldMaintenanceRemodeling.setRemodelingType(moldMaintenanceRemodelingVo.getMainteType()); //js名用誤り
        }
        moldMaintenanceRemodeling.setWorkingTimeMinutes(moldMaintenanceRemodelingVo.getWorkingTimeMinutes());//所要時間 20170901 Apeng add
        
        moldMaintenanceRemodeling.setReport(moldMaintenanceRemodelingVo.getReport());
        moldMaintenanceRemodeling.setDirectionId(moldMaintenanceRemodelingVo.getDirectionId());
        //手配・工事番号のコード直接保存のとき
        if (moldMaintenanceRemodelingVo.getDirectionId() == null && moldMaintenanceRemodelingVo.getDirectionCode() != null) {
            moldMaintenanceRemodeling.setDirectionCode(moldMaintenanceRemodelingVo.getDirectionCode());
        }
        moldMaintenanceRemodeling.setUpdateUserUuid(loginUser.getUserUuid());
        if(moldMaintenanceRemodelingVo.getStartDatetime() != null){
            moldMaintenanceRemodeling.setStartDatetime(DateFormat.strToDatetime(moldMaintenanceRemodelingVo.getStartDatetime()));
        }
        if (moldMaintenanceRemodeling.getStartDatetime() != null) {
            moldMaintenanceRemodeling.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), moldMaintenanceRemodeling.getStartDatetime()));
        }
        // メンテナンスの一次保存機能 2018/09/13 -S
        if (0 == moldMaintenanceRemodelingVo.getTemporarilySaved()) {

            if(moldMaintenanceRemodelingVo.getEndDatetime() != null){
                moldMaintenanceRemodeling.setEndDatetime(DateFormat.strToDatetime(moldMaintenanceRemodelingVo.getEndDatetime()));
            }
            if (moldMaintenanceRemodeling.getEndDatetime() != null) {
                moldMaintenanceRemodeling.setEndDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), moldMaintenanceRemodeling.getEndDatetime()));
            }
//        moldMaintenanceRemodeling.setEndDatetime(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
//        moldMaintenanceRemodeling.setEndDatetimeStz(new Date());
        }
        // メンテナンスの一次保存機能 2018/09/13 -E
        moldMaintenanceRemodeling.setUpdateDate(new Date());
        moldMaintenanceRemodeling.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(moldMaintenanceRemodeling);
        
        if (null != moldMaintenanceRemodelingVo.getIssueId() && !"".equals(moldMaintenanceRemodelingVo.getIssueId().trim())) {
            TblIssue tblIssue = entityManager.find(TblIssue.class, moldMaintenanceRemodelingVo.getIssueId());
            if (tblIssue != null) {
                try {
                    String measureStatus = moldMaintenanceRemodelingVo.getMeasureStatus();
                    tblIssue.setMeasureStatus(Integer.parseInt(measureStatus));
                    if (String.valueOf(CommonConstants.ISSUE_MEASURE_STATUS_TEMPORARILY_RESOLVED).equals(measureStatus)
                            || String.valueOf(CommonConstants.ISSUE_MEASURE_STATUS_PERMANENTLY_RESOLVED).equals(measureStatus)
                            || String.valueOf(CommonConstants.ISSUE_MEASURE_STATUS_COMPLETED).equals(measureStatus)) {
                        tblIssue.setMeasuerCompletedDate(new Date());
                    }
                } catch (NumberFormatException e) {

                }

                tblIssue.setUpdateDate(new Date());
                tblIssue.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.merge(tblIssue);
            }
        }
        
        List detailVos = null;
        if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {
            //金型メンテナンス詳細 追加
            detailVos = moldMaintenanceRemodelingVo.getMoldMaintenanceDetailVo();

        } else if (type == CommonConstants.MAINTEORREMODEL_REMODEL) {
            detailVos = moldMaintenanceRemodelingVo.getMoldRemodelingDetailVo();
        }

        if (null != detailVos && !detailVos.isEmpty()) {
            if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {                
                List<TblMoldMaintenanceDetailVo> newMaintenanceDetailVo = new ArrayList<>();
                for(int i=0; i<detailVos.size();i++){
                    TblMoldMaintenanceDetailVo moldMaintenanceDetailVo = (TblMoldMaintenanceDetailVo) detailVos.get(i);
                    if(!"1".equals(moldMaintenanceDetailVo.getDeleteFlag())){
                        newMaintenanceDetailVo.add(moldMaintenanceDetailVo);
                    }
                }
                
                Query query = entityManager.createQuery("DELETE FROM TblMoldMaintenanceDetail t WHERE t.tblMoldMaintenanceDetailPK.maintenanceId = :maintenanceId");
                query.setParameter("maintenanceId", moldMaintenanceRemodeling.getId());
                query.executeUpdate();
                
                
                for (int i = 0; i < newMaintenanceDetailVo.size();  i++) {
                    TblMoldMaintenanceDetailVo moldMaintenanceDetailVo = (TblMoldMaintenanceDetailVo) newMaintenanceDetailVo.get(i);

                    TblMoldMaintenanceDetail moldMaintenanceDetail = new TblMoldMaintenanceDetail();
                    if (null != moldMaintenanceDetailVo.getMainteReasonCategory1() && !moldMaintenanceDetailVo.getMainteReasonCategory1().equals("")) {
                        moldMaintenanceDetail.setMainteReasonCategory1(Integer.parseInt(moldMaintenanceDetailVo.getMainteReasonCategory1()));
                        moldMaintenanceDetail.setMainteReasonCategory1Text(moldMaintenanceDetailVo.getMainteReasonCategory1Text());

                        if (null != moldMaintenanceDetailVo.getMainteReasonCategory2() && !moldMaintenanceDetailVo.getMainteReasonCategory2().equals("")) {
                            moldMaintenanceDetail.setMainteReasonCategory2(Integer.parseInt(moldMaintenanceDetailVo.getMainteReasonCategory2()));
                            moldMaintenanceDetail.setMainteReasonCategory2Text(moldMaintenanceDetailVo.getMainteReasonCategory2Text());

                            if (null != moldMaintenanceDetailVo.getMainteReasonCategory3() && !moldMaintenanceDetailVo.getMainteReasonCategory3().equals("")) {
                                moldMaintenanceDetail.setMainteReasonCategory3(Integer.parseInt(moldMaintenanceDetailVo.getMainteReasonCategory3()));
                                moldMaintenanceDetail.setMainteReasonCategory3Text(moldMaintenanceDetailVo.getMainteReasonCategory3Text());
                            } else {
                                moldMaintenanceDetail.setMainteReasonCategory3(null);
                                moldMaintenanceDetail.setMainteReasonCategory3Text(null);
                            }
                        } else {
                            moldMaintenanceDetail.setMainteReasonCategory2(null);
                            moldMaintenanceDetail.setMainteReasonCategory2Text(null);
                            moldMaintenanceDetail.setMainteReasonCategory3(null);
                            moldMaintenanceDetail.setMainteReasonCategory3Text(null);
                        }
                    } else {
                        moldMaintenanceDetail.setMainteReasonCategory1(null);
                        moldMaintenanceDetail.setMainteReasonCategory1Text(null);
                        moldMaintenanceDetail.setMainteReasonCategory2(null);
                        moldMaintenanceDetail.setMainteReasonCategory2Text(null);
                        moldMaintenanceDetail.setMainteReasonCategory3(null);
                        moldMaintenanceDetail.setMainteReasonCategory3Text(null);
                    }
                    moldMaintenanceDetail.setManiteReason(moldMaintenanceDetailVo.getManiteReason());

                    if (null != moldMaintenanceDetailVo.getTaskCategory1() && !moldMaintenanceDetailVo.getTaskCategory1().equals("")) {
                        moldMaintenanceDetail.setTaskCategory1(Integer.parseInt(moldMaintenanceDetailVo.getTaskCategory1()));
                        moldMaintenanceDetail.setTaskCategory1Text(moldMaintenanceDetailVo.getTaskCategory1Text());
                        if (null != moldMaintenanceDetailVo.getTaskCategory2() && !moldMaintenanceDetailVo.getTaskCategory2().equals("")) {
                            moldMaintenanceDetail.setTaskCategory2(Integer.parseInt(moldMaintenanceDetailVo.getTaskCategory2()));
                            moldMaintenanceDetail.setTaskCategory2Text(moldMaintenanceDetailVo.getTaskCategory2Text());
                            if (null != moldMaintenanceDetailVo.getTaskCategory3() && !moldMaintenanceDetailVo.getTaskCategory3().equals("")) {
                                moldMaintenanceDetail.setTaskCategory3(Integer.parseInt(moldMaintenanceDetailVo.getTaskCategory3()));
                                moldMaintenanceDetail.setTaskCategory3Text(moldMaintenanceDetailVo.getTaskCategory3Text());
                            } else {
                                moldMaintenanceDetail.setTaskCategory3(null);
                                moldMaintenanceDetail.setTaskCategory3Text(null);
                            }
                        } else {
                            moldMaintenanceDetail.setTaskCategory2(null);
                            moldMaintenanceDetail.setTaskCategory2Text(null);
                            moldMaintenanceDetail.setTaskCategory3(null);
                            moldMaintenanceDetail.setTaskCategory3Text(null);
                        }
                    } else {
                        moldMaintenanceDetail.setTaskCategory1(null);
                        moldMaintenanceDetail.setTaskCategory1Text(null);
                        moldMaintenanceDetail.setTaskCategory2(null);
                        moldMaintenanceDetail.setTaskCategory2Text(null);
                        moldMaintenanceDetail.setTaskCategory3(null);
                        moldMaintenanceDetail.setTaskCategory3Text(null);
                    }
                    moldMaintenanceDetail.setTask(moldMaintenanceDetailVo.getTask());

                    if (null != moldMaintenanceDetailVo.getMeasureDirectionCategory1() && !moldMaintenanceDetailVo.getMeasureDirectionCategory1().equals("")) {
                        moldMaintenanceDetail.setMeasureDirectionCategory1(Integer.parseInt(moldMaintenanceDetailVo.getMeasureDirectionCategory1()));
                        moldMaintenanceDetail.setMeasureDirectionCategory1Text(moldMaintenanceDetailVo.getMeasureDirectionCategory1Text());
                        if (null != moldMaintenanceDetailVo.getMeasureDirectionCategory2() && !moldMaintenanceDetailVo.getMeasureDirectionCategory2().equals("")) {
                            moldMaintenanceDetail.setMeasureDirectionCategory2(Integer.parseInt(moldMaintenanceDetailVo.getMeasureDirectionCategory2()));
                            moldMaintenanceDetail.setMeasureDirectionCategory2Text(moldMaintenanceDetailVo.getMeasureDirectionCategory2Text());
                            if (null != moldMaintenanceDetailVo.getMeasureDirectionCategory3() && !moldMaintenanceDetailVo.getMeasureDirectionCategory3().equals("")) {
                                moldMaintenanceDetail.setMeasureDirectionCategory3(Integer.parseInt(moldMaintenanceDetailVo.getMeasureDirectionCategory3()));
                                moldMaintenanceDetail.setMeasureDirectionCategory3Text(moldMaintenanceDetailVo.getMeasureDirectionCategory3Text());
                            } else {
                                moldMaintenanceDetail.setMeasureDirectionCategory3(null);
                                moldMaintenanceDetail.setMeasureDirectionCategory3Text(null);
                            }
                        } else {
                            moldMaintenanceDetail.setMeasureDirectionCategory2(null);
                            moldMaintenanceDetail.setMeasureDirectionCategory2Text(null);
                            moldMaintenanceDetail.setMeasureDirectionCategory3(null);
                            moldMaintenanceDetail.setMeasureDirectionCategory3Text(null);
                        }
                    } else {
                        moldMaintenanceDetail.setMeasureDirectionCategory1(null);
                        moldMaintenanceDetail.setMeasureDirectionCategory1Text(null);
                        moldMaintenanceDetail.setMeasureDirectionCategory2(null);
                        moldMaintenanceDetail.setMeasureDirectionCategory2Text(null);
                        moldMaintenanceDetail.setMeasureDirectionCategory3(null);
                        moldMaintenanceDetail.setMeasureDirectionCategory3Text(null);
                    }
                    moldMaintenanceDetail.setMeasureDirection(moldMaintenanceDetailVo.getMeasureDirection());

                    TblMoldMaintenanceDetailPK pk = new TblMoldMaintenanceDetailPK();
                    pk.setMaintenanceId(moldMaintenanceRemodeling.getId());
                    pk.setSeq(moldMaintenanceDetailVo.getSeq());
                    moldMaintenanceDetail.setTblMoldMaintenanceDetailPK(pk);
                    moldMaintenanceDetail.setId(IDGenerator.generate());
                    moldMaintenanceDetail.setCreateDate(new Date());
                    moldMaintenanceDetail.setCreateUserUuid(loginUser.getUserUuid());
                    moldMaintenanceDetail.setUpdateDate(new Date());
                    moldMaintenanceDetail.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.persist(moldMaintenanceDetail);

                    //金型点検結果 追加
                    List<TblMoldInspectionResultVo> moldInspectionResultVos = moldMaintenanceRemodelingVo.getMoldInspectionResultVo();
                    if (null == moldInspectionResultVos || moldInspectionResultVos.isEmpty()) {
                        moldInspectionResultVos = moldMaintenanceDetailVo.getMoldInspectionResultVos();
                    }
                    if (null != moldInspectionResultVos && !moldInspectionResultVos.isEmpty()) {
                        List<TblMoldInspectionResultVo> newInspectionResultVo = new ArrayList<>();
                        Query delInspectionResultQuery = entityManager.createNamedQuery("TblMoldInspectionResult.deleteByMaintenanceDetailId");
                        if (null != moldMaintenanceDetailVo.getId() && null != moldMaintenanceRemodelingVo.getMoldInspectionResultVo() && !moldMaintenanceRemodelingVo.getMoldInspectionResultVo().isEmpty()) {
                            delInspectionResultQuery.setParameter("maintenanceDetailId", moldMaintenanceDetailVo.getId());

                            for (TblMoldInspectionResultVo aMoldInspectionResultVo : moldInspectionResultVos) {
                                if (aMoldInspectionResultVo.getMaintenanceDetailId().equals(moldMaintenanceDetailVo.getId())) {
                                    newInspectionResultVo.add(aMoldInspectionResultVo);
                                }
                            }
                        } else {
                            delInspectionResultQuery.setParameter("maintenanceDetailId", moldMaintenanceDetail.getId());
                            newInspectionResultVo = moldInspectionResultVos;
                        }
                        delInspectionResultQuery.executeUpdate();
                        for (TblMoldInspectionResultVo aMoldInspectionResultVo : newInspectionResultVo) {
                            TblMoldInspectionResult aResult = new TblMoldInspectionResult();
                            aResult.setUpdateDate(new Date());
                            aResult.setUpdateUserUuid(loginUser.getUserUuid());
                            aResult.setInspectionResult(aMoldInspectionResultVo.getInspectionResult());
                            aResult.setInspectionResultText(aMoldInspectionResultVo.getInspectionResultText());

                            aResult.setId(IDGenerator.generate());
                            TblMoldInspectionResultPK resultPK = new TblMoldInspectionResultPK();
                            resultPK.setInspectionItemId(aMoldInspectionResultVo.getInspectionItemId());
                            resultPK.setMaintenanceDetailId(moldMaintenanceDetail.getId());
                            resultPK.setSeq(aMoldInspectionResultVo.getSeq());
                            aResult.setTblMoldInspectionResultPK(resultPK);

                            aResult.setCreateDate(new Date());
                            aResult.setCreateUserUuid(loginUser.getUserUuid());
                            entityManager.persist(aResult);
                        }
                    }

                    
                    //金型ImageFile 追加
                    List<TblMoldMaintenanceDetailImageFileVo> moldMaintenanceDetailImageFileVos = moldMaintenanceRemodelingVo.getImageFileVos();
                    if (null == moldMaintenanceDetailImageFileVos || moldMaintenanceDetailImageFileVos.isEmpty()){
                        moldMaintenanceDetailImageFileVos = moldMaintenanceDetailVo.getMoldMaintenanceDetailImageFileVos();
                    }
                    if (null != moldMaintenanceDetailImageFileVos && !moldMaintenanceDetailImageFileVos.isEmpty()) {
                        List<TblMoldMaintenanceDetailImageFileVo> newMoldMaintenanceDetailImageFileVos = new ArrayList<>();
                        Query delImageFileQuery = entityManager.createNamedQuery("TblMoldMaintenanceDetailImageFile.deleteByMaintenanceDetailId");
                        if (null != moldMaintenanceDetailVo.getId() && null != moldMaintenanceRemodelingVo.getImageFileVos() && !moldMaintenanceRemodelingVo.getImageFileVos().isEmpty()) {
                            delImageFileQuery.setParameter("maintenanceDetailId", moldMaintenanceDetailVo.getId());

                            for (TblMoldMaintenanceDetailImageFileVo aMoldMaintenanceDetailImageFileVo : moldMaintenanceDetailImageFileVos) {
                                if (aMoldMaintenanceDetailImageFileVo.getMaintenanceDetailId().equals(moldMaintenanceDetailVo.getId())) {
                                    newMoldMaintenanceDetailImageFileVos.add(aMoldMaintenanceDetailImageFileVo);
                                }
                            }
                        } else {
                            delImageFileQuery.setParameter("maintenanceDetailId", moldMaintenanceDetail.getId());
                            newMoldMaintenanceDetailImageFileVos = moldMaintenanceDetailImageFileVos;
                        }
                        delImageFileQuery.executeUpdate();
                        for (TblMoldMaintenanceDetailImageFileVo aImageFileVo : newMoldMaintenanceDetailImageFileVos) {
                            TblMoldMaintenanceDetailImageFile aImageFile = new TblMoldMaintenanceDetailImageFile();
                            aImageFile.setUpdateDate(new Date());
                            aImageFile.setUpdateUserUuid(loginUser.getUserUuid());
                            aImageFile.setCreateDate(new Date());
                            aImageFile.setCreateDateUuid(loginUser.getUserUuid());

                            TblMoldMaintenanceDetailImageFilePK aPK = new TblMoldMaintenanceDetailImageFilePK();                            
                            aPK.setMaintenanceDetailId(moldMaintenanceDetail.getId());
                            aPK.setSeq(Integer.parseInt(aImageFileVo.getSeq()));
                            aImageFile.setTblMoldMaintenanceDetailImageFilePK(aPK);

                            if (null != aImageFileVo.getFileExtension() && !aImageFileVo.getFileExtension().isEmpty()) {
                                aImageFile.setFileExtension(aImageFileVo.getFileExtension());
                            } else {
                                aImageFile.setFileExtension(null);
                            }
                            if (null != aImageFileVo.getFileType() && !aImageFileVo.getFileType().isEmpty()) {
                                aImageFile.setFileType(Integer.parseInt(aImageFileVo.getFileType()));
                            } else {
                                aImageFile.setFileType(null);
                            }
                            if (null != aImageFileVo.getFileUuid() && !aImageFileVo.getFileUuid().isEmpty()) {
                                aImageFile.setFileUuid(aImageFileVo.getFileUuid());
                            }
                            if (null != aImageFileVo.getRemarks() && !aImageFileVo.getRemarks().isEmpty()) {
                                aImageFile.setRemarks(aImageFileVo.getRemarks());
                            } else {
                                aImageFile.setRemarks(null);
                            }
                            if (null != aImageFileVo.getTakenDateStzStr() && !aImageFileVo.getTakenDateStzStr().isEmpty()) {
                                aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStzStr()));
                            } else {
                                aImageFile.setTakenDateStz(null);
                            }
                            if (null != aImageFileVo.getTakenDateStr() && !aImageFileVo.getTakenDateStr().isEmpty()) {
                                aImageFile.setTakenDate(new FileUtil().getDateTimeParseForDate(DateFormat.dateTimeFormat(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()), loginUser.getJavaZoneId())));
                                aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()));
                            } else {
                                aImageFile.setTakenDate(null);
                                aImageFile.setTakenDateStz(null);
                            }
                            if (null != aImageFileVo.getThumbnailFileUuid() && !aImageFileVo.getThumbnailFileUuid().isEmpty()) {
                                aImageFile.setThumbnailFileUuid(aImageFileVo.getThumbnailFileUuid());
                            } else {
                                aImageFile.setThumbnailFileUuid(null);
                            }

                            entityManager.persist(aImageFile);
                        }
                    }
                }
            } else if (type == CommonConstants.MAINTEORREMODEL_REMODEL) {                
                List<TblMoldRemodelingDetailVo> newRemodelingDetailVo = new ArrayList<>();
                for(int i=0; i<detailVos.size();i++){
                    TblMoldRemodelingDetailVo moldRemodelingDetailVo = (TblMoldRemodelingDetailVo) detailVos.get(i);
                    if(!"1".equals(moldRemodelingDetailVo.getDeleteFlag())){
                        newRemodelingDetailVo.add(moldRemodelingDetailVo);
                    }
                }
                
                Query query = entityManager.createQuery("DELETE FROM TblMoldRemodelingDetail t WHERE t.tblMoldRemodelingDetailPK.maintenanceId = :maintenanceId");
                query.setParameter("maintenanceId", moldMaintenanceRemodeling.getId());
                query.executeUpdate();
                
                for (int i = 0; i < newRemodelingDetailVo.size(); i++) {
                    TblMoldRemodelingDetailVo moldRemodelingDetailVo = (TblMoldRemodelingDetailVo) newRemodelingDetailVo.get(i);

                    TblMoldRemodelingDetail moldRemodelingDetail = new TblMoldRemodelingDetail();
                    if (null != moldRemodelingDetailVo.getRemodelReasonCategory1() && !moldRemodelingDetailVo.getRemodelReasonCategory1().equals("")) {
                        moldRemodelingDetail.setRemodelReasonCategory1(Integer.parseInt(moldRemodelingDetailVo.getRemodelReasonCategory1()));
                        moldRemodelingDetail.setRemodelReasonCategory1Text(moldRemodelingDetailVo.getRemodelReasonCategory1Text());

                        if (null != moldRemodelingDetailVo.getRemodelReasonCategory2() && !moldRemodelingDetailVo.getRemodelReasonCategory2().equals("")) {
                            moldRemodelingDetail.setRemodelReasonCategory2(Integer.parseInt(moldRemodelingDetailVo.getRemodelReasonCategory2()));
                            moldRemodelingDetail.setRemodelReasonCategory2Text(moldRemodelingDetailVo.getRemodelReasonCategory2Text());

                            if (null != moldRemodelingDetailVo.getRemodelReasonCategory3() && !moldRemodelingDetailVo.getRemodelReasonCategory3().equals("")) {
                                moldRemodelingDetail.setRemodelReasonCategory3(Integer.parseInt(moldRemodelingDetailVo.getRemodelReasonCategory3()));
                                moldRemodelingDetail.setRemodelReasonCategory3Text(moldRemodelingDetailVo.getRemodelReasonCategory3Text());
                            } else {
                                moldRemodelingDetail.setRemodelReasonCategory3(null);
                                moldRemodelingDetail.setRemodelReasonCategory3Text(null);
                            }
                        } else {
                            moldRemodelingDetail.setRemodelReasonCategory2(null);
                            moldRemodelingDetail.setRemodelReasonCategory2Text(null);
                            moldRemodelingDetail.setRemodelReasonCategory3(null);
                            moldRemodelingDetail.setRemodelReasonCategory3Text(null);
                        }
                    } else {
                        moldRemodelingDetail.setRemodelReasonCategory1(null);
                        moldRemodelingDetail.setRemodelReasonCategory1Text(null);
                        moldRemodelingDetail.setRemodelReasonCategory2(null);
                        moldRemodelingDetail.setRemodelReasonCategory2Text(null);
                        moldRemodelingDetail.setRemodelReasonCategory3(null);
                        moldRemodelingDetail.setRemodelReasonCategory3Text(null);
                    }
                    moldRemodelingDetail.setRemodelReason(moldRemodelingDetailVo.getRemodelReason());

                    if (null != moldRemodelingDetailVo.getTaskCategory1() && !moldRemodelingDetailVo.getTaskCategory1().equals("")) {
                        moldRemodelingDetail.setTaskCategory1(Integer.parseInt(moldRemodelingDetailVo.getTaskCategory1()));
                        moldRemodelingDetail.setTaskCategory1Text(moldRemodelingDetailVo.getTaskCategory1Text());
                        if (null != moldRemodelingDetailVo.getTaskCategory2() && !moldRemodelingDetailVo.getTaskCategory2().equals("")) {
                            moldRemodelingDetail.setTaskCategory2(Integer.parseInt(moldRemodelingDetailVo.getTaskCategory2()));
                            moldRemodelingDetail.setTaskCategory2Text(moldRemodelingDetailVo.getTaskCategory2Text());
                            if (null != moldRemodelingDetailVo.getTaskCategory3() && !moldRemodelingDetailVo.getTaskCategory3().equals("")) {
                                moldRemodelingDetail.setTaskCategory3(Integer.parseInt(moldRemodelingDetailVo.getTaskCategory3()));
                                moldRemodelingDetail.setTaskCategory3Text(moldRemodelingDetailVo.getTaskCategory3Text());
                            } else {
                                moldRemodelingDetail.setTaskCategory3(null);
                                moldRemodelingDetail.setTaskCategory3Text(null);
                            }
                        } else {
                            moldRemodelingDetail.setTaskCategory2(null);
                            moldRemodelingDetail.setTaskCategory2Text(null);
                            moldRemodelingDetail.setTaskCategory3(null);
                            moldRemodelingDetail.setTaskCategory3Text(null);
                        }
                    } else {
                        moldRemodelingDetail.setTaskCategory1(null);
                        moldRemodelingDetail.setTaskCategory1Text(null);
                        moldRemodelingDetail.setTaskCategory2(null);
                        moldRemodelingDetail.setTaskCategory2Text(null);
                        moldRemodelingDetail.setTaskCategory3(null);
                        moldRemodelingDetail.setTaskCategory3Text(null);
                    }
                    moldRemodelingDetail.setTask(moldRemodelingDetailVo.getTask());

                    if (null != moldRemodelingDetailVo.getRemodelDirectionCategory1() && !moldRemodelingDetailVo.getRemodelDirectionCategory1().equals("")) {
                        moldRemodelingDetail.setRemodelDirectionCategory1(Integer.parseInt(moldRemodelingDetailVo.getRemodelDirectionCategory1()));
                        moldRemodelingDetail.setRemodelDirectionCategory1Text(moldRemodelingDetailVo.getRemodelDirectionCategory1Text());
                        if (null != moldRemodelingDetailVo.getRemodelDirectionCategory2() && !moldRemodelingDetailVo.getRemodelDirectionCategory2().equals("")) {
                            moldRemodelingDetail.setRemodelDirectionCategory2(Integer.parseInt(moldRemodelingDetailVo.getRemodelDirectionCategory2()));
                            moldRemodelingDetail.setRemodelDirectionCategory2Text(moldRemodelingDetailVo.getRemodelDirectionCategory2Text());
                            if (null != moldRemodelingDetailVo.getRemodelDirectionCategory3() && !moldRemodelingDetailVo.getRemodelDirectionCategory3().equals("")) {
                                moldRemodelingDetail.setRemodelDirectionCategory3(Integer.parseInt(moldRemodelingDetailVo.getRemodelDirectionCategory3()));
                                moldRemodelingDetail.setRemodelDirectionCategory3Text(moldRemodelingDetailVo.getRemodelDirectionCategory3Text());
                            } else {
                                moldRemodelingDetail.setRemodelDirectionCategory3(null);
                                moldRemodelingDetail.setRemodelDirectionCategory3Text(null);
                            }
                        } else {
                            moldRemodelingDetail.setRemodelDirectionCategory2(null);
                            moldRemodelingDetail.setRemodelDirectionCategory2Text(null);
                            moldRemodelingDetail.setRemodelDirectionCategory3(null);
                            moldRemodelingDetail.setRemodelDirectionCategory3Text(null);
                        }
                    } else {
                        moldRemodelingDetail.setRemodelDirectionCategory1(null);
                        moldRemodelingDetail.setRemodelDirectionCategory1Text(null);
                        moldRemodelingDetail.setRemodelDirectionCategory2(null);
                        moldRemodelingDetail.setRemodelDirectionCategory2Text(null);
                        moldRemodelingDetail.setRemodelDirectionCategory3(null);
                        moldRemodelingDetail.setRemodelDirectionCategory3Text(null);
                    }
                    moldRemodelingDetail.setRemodelDirection(moldRemodelingDetailVo.getRemodelDirection());

//                    if (null == moldRemodelingDetail.getId()) {
                        TblMoldRemodelingDetailPK pk = new TblMoldRemodelingDetailPK();
                        pk.setMaintenanceId(moldMaintenanceRemodeling.getId());
                        pk.setSeq(moldRemodelingDetailVo.getSeq());
                        moldRemodelingDetail.setTblMoldRemodelingDetailPK(pk);
                        moldRemodelingDetail.setId(IDGenerator.generate());
                        moldRemodelingDetail.setCreateDate(new Date());
                        moldRemodelingDetail.setCreateUserUuid(loginUser.getUserUuid());
                        moldRemodelingDetail.setUpdateDate(new Date());
                        moldRemodelingDetail.setUpdateUserUuid(loginUser.getUserUuid());
                        entityManager.persist(moldRemodelingDetail);
//                    } else {
//                        moldRemodelingDetail.setUpdateDate(new Date());
//                        moldRemodelingDetail.setUpdateUserUuid(loginUser.getUserUuid());
//                        entityManager.merge(moldRemodelingDetail);
//                    }

                    //金型点検結果 追加
                    List<TblMoldRemodelingInspectionResultVo> moldRemodelingInspectionResultVos = moldMaintenanceRemodelingVo.getMoldRemodelingInspectionResultVo();
                    if (null == moldRemodelingInspectionResultVos || moldRemodelingInspectionResultVos.isEmpty()) {
                        moldRemodelingInspectionResultVos = moldRemodelingDetailVo.getMoldRemodelingInspectionResultVos();
                    }
                    if (null != moldRemodelingInspectionResultVos && !moldRemodelingInspectionResultVos.isEmpty()) {
                        List<TblMoldRemodelingInspectionResultVo> newRemodelingInspectionResultVo = new ArrayList<>();
                        Query delInspectionResultQuery = entityManager.createQuery("DELETE FROM TblMoldRemodelingInspectionResult t WHERE t.tblMoldRemodelingInspectionResultPK.remodelingDetailId = :remodelingDetailId");
                        if (null != moldRemodelingDetailVo.getId() && null != moldMaintenanceRemodelingVo.getMoldRemodelingInspectionResultVo() && !moldMaintenanceRemodelingVo.getMoldRemodelingInspectionResultVo().isEmpty()) {
                            delInspectionResultQuery.setParameter("remodelingDetailId", moldRemodelingDetailVo.getId());
                            for (TblMoldRemodelingInspectionResultVo aMoldRemodelingInspectionResultVo : moldRemodelingInspectionResultVos) {
                                if (aMoldRemodelingInspectionResultVo.getRemodelingDetailId().equals(moldRemodelingDetailVo.getId())) {
                                    newRemodelingInspectionResultVo.add(aMoldRemodelingInspectionResultVo);
                                }
                            }
                        } else {
                            delInspectionResultQuery.setParameter("remodelingDetailId", moldRemodelingDetail.getId());
                            newRemodelingInspectionResultVo = moldRemodelingInspectionResultVos;
                        }
                        delInspectionResultQuery.executeUpdate();

                        for (TblMoldRemodelingInspectionResultVo aMoldRemodelingInspectionResultVo : newRemodelingInspectionResultVo) {
                            TblMoldRemodelingInspectionResult aResult = new TblMoldRemodelingInspectionResult();
                            aResult.setUpdateDate(new Date());
                            aResult.setUpdateUserUuid(loginUser.getUserUuid());
                            aResult.setInspectionResult(aMoldRemodelingInspectionResultVo.getInspectionResult());
                            aResult.setInspectionResultText(aMoldRemodelingInspectionResultVo.getInspectionResultText());

                            aResult.setId(IDGenerator.generate());
                            TblMoldRemodelingInspectionResultPK resultPK = new TblMoldRemodelingInspectionResultPK();
                            resultPK.setInspectionItemId(aMoldRemodelingInspectionResultVo.getInspectionItemId());
                            resultPK.setRemodelingDetailId(moldRemodelingDetail.getId());
                            resultPK.setSeq(aMoldRemodelingInspectionResultVo.getSeq());
                            aResult.setTblMoldRemodelingInspectionResultPK(resultPK);

                            aResult.setCreateDate(new Date());
                            aResult.setCreateUserUuid(loginUser.getUserUuid());
                            entityManager.persist(aResult);
                        }
                    }                    
                    
                    //金型ImageFile 追加
                    List<TblMoldRemodelingDetailImageFileVo> moldRemodelingDetailImageFileVos = moldMaintenanceRemodelingVo.getRimageFileVos();
                    if (null == moldRemodelingDetailImageFileVos || moldRemodelingDetailImageFileVos.isEmpty()) {
                        moldRemodelingDetailImageFileVos = moldRemodelingDetailVo.getMoldRemodelingDetailImageFileVos();
                    }
                    if (null != moldRemodelingDetailImageFileVos && !moldRemodelingDetailImageFileVos.isEmpty()) {
                        List<TblMoldRemodelingDetailImageFileVo> newMoldRemodelingDetailImageFileVos = new ArrayList<>();
                        Query delImageFileQuery = entityManager.createNamedQuery("TblMoldRemodelingDetailImageFile.deleteByRemodelingDetailId");
                        if (null != moldRemodelingDetailVo.getId() && null != moldMaintenanceRemodelingVo.getRimageFileVos() && !moldMaintenanceRemodelingVo.getRimageFileVos().isEmpty()) {
                            delImageFileQuery.setParameter("remodelingDetailId", moldRemodelingDetailVo.getId());

                            for (TblMoldRemodelingDetailImageFileVo aMoldRemodelingDetailImageFileVo : moldRemodelingDetailImageFileVos) {
                                if (aMoldRemodelingDetailImageFileVo.getRemodelingDetailId().equals(moldRemodelingDetailVo.getId())) {
                                    newMoldRemodelingDetailImageFileVos.add(aMoldRemodelingDetailImageFileVo);
                                }
                            }
                        } else {
                            delImageFileQuery.setParameter("remodelingDetailId", moldRemodelingDetail.getId());
                            newMoldRemodelingDetailImageFileVos = moldRemodelingDetailImageFileVos;
                        }
                        delImageFileQuery.executeUpdate();
                        for (TblMoldRemodelingDetailImageFileVo aImageFileVo : newMoldRemodelingDetailImageFileVos) {
                            TblMoldRemodelingDetailImageFile aImageFile = new TblMoldRemodelingDetailImageFile();
                            aImageFile.setUpdateDate(new Date());
                            aImageFile.setUpdateUserUuid(loginUser.getUserUuid());
                            aImageFile.setCreateDate(new Date());
                            aImageFile.setCreateDateUuid(loginUser.getUserUuid());

                            TblMoldRemodelingDetailImageFilePK aPK = new TblMoldRemodelingDetailImageFilePK();
                            aPK.setRemodelingDetailId(moldRemodelingDetail.getId());
                            aPK.setSeq(Integer.parseInt(aImageFileVo.getSeq()));
                            aImageFile.setTblMoldRemodelingDetailImageFilePK(aPK);

                            if (null != aImageFileVo.getFileExtension() && !aImageFileVo.getFileExtension().isEmpty()) {
                                aImageFile.setFileExtension(aImageFileVo.getFileExtension());
                            } else {
                                aImageFile.setFileExtension(null);
                            }
                            if (null != aImageFileVo.getFileType() && !aImageFileVo.getFileType().isEmpty()) {
                                aImageFile.setFileType(Integer.parseInt(aImageFileVo.getFileType()));
                            } else {
                                aImageFile.setFileType(null);
                            }
                            if (null != aImageFileVo.getFileUuid() && !aImageFileVo.getFileUuid().isEmpty()) {
                                aImageFile.setFileUuid(aImageFileVo.getFileUuid());
                            }
                            if (null != aImageFileVo.getRemarks() && !aImageFileVo.getRemarks().isEmpty()) {
                                aImageFile.setRemarks(aImageFileVo.getRemarks());
                            } else {
                                aImageFile.setRemarks(null);
                            }
                            if (null != aImageFileVo.getTakenDateStzStr() && !aImageFileVo.getTakenDateStzStr().isEmpty()) {
                                aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStzStr()));
                            } else {
                                aImageFile.setTakenDateStz(null);
                            }
                            if (null != aImageFileVo.getTakenDateStr() && !aImageFileVo.getTakenDateStr().isEmpty()) {
                                aImageFile.setTakenDate(new FileUtil().getDateTimeParseForDate(DateFormat.dateTimeFormat(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()), loginUser.getJavaZoneId())));
                                aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()));
                            } else {
                                aImageFile.setTakenDate(null);
                                aImageFile.setTakenDateStz(null);
                            }
                            if (null != aImageFileVo.getThumbnailFileUuid() && !aImageFileVo.getThumbnailFileUuid().isEmpty()) {
                                aImageFile.setThumbnailFileUuid(aImageFileVo.getThumbnailFileUuid());
                            } else {
                                aImageFile.setThumbnailFileUuid(null);
                            }

                            entityManager.persist(aImageFile);
                        }
                    }
                }
            }
        }
        
        // Iteration4.2 金型メンテナンス候補更新 Start
        // メンテナンスの一次保存機能 2018/09/13 -S
        if (aMstMold != null && aMstMold.getUuid() != null && !"".equals(aMstMold.getUuid())) {
            TblMoldMaintenanceRecomendList tblMoldMaintenanceRecomendList = tblMoldMaintenanceRecomendService.getMoldMaintenanceRecommendByUuid(aMstMold.getUuid());
            if (tblMoldMaintenanceRecomendList != null && !tblMoldMaintenanceRecomendList.getTblMoldMaintenanceRecomendList().isEmpty()) {
                for (TblMoldMaintenanceRecomend tblMoldMaintenanceRecomend : tblMoldMaintenanceRecomendList.getTblMoldMaintenanceRecomendList()) {
                    tblMoldMaintenanceRecomendService.updateTblMoldMaintenanceRecomend(tblMoldMaintenanceRecomend, loginUser);
                }
            }
        }
        
        boolean stockIsAvailable = cnfSystemService.findByKey("mold", "mold_part_stock_available").getConfigValue().equals("1");
        
        // update mold Part Rel Maint Logic 2019/07/26
        if (null != moldMaintenanceRemodelingVo.getTblMoldlMaintenanceDetailPRVos()
                && !moldMaintenanceRemodelingVo.getTblMoldlMaintenanceDetailPRVos().isEmpty()) {

            for (int i = 0; i < moldMaintenanceRemodelingVo.getTblMoldlMaintenanceDetailPRVos().size(); i++) {
                TblMoldMaintenanceDetailPR moldMaintDetailPR =  moldMaintenanceRemodelingVo.getTblMoldlMaintenanceDetailPRVos().get(i);
                if (!moldMaintDetailPR.getReplaceIsChecked() && !moldMaintDetailPR.getRepairIsChecked() && !moldMaintDetailPR.getPartialReplaceIsChecked()) {
                    continue;
                }
                
                TblMoldMaintenancePart moldMaintPart = getMaintPart(moldMaintenanceRemodelingVo.getMaintenanceId(), moldMaintDetailPR.getMoldPartRelId(), loginUser.getUserUuid());
                moldMaintPart.setDisposeQuantity(moldMaintDetailPR.getDisposeQty());
                moldMaintPart.setRecycleQuantity(moldMaintDetailPR.getRecycleQty());
                Integer replaceRepairFlag = moldMaintDetailPR.getReplaceIsChecked() ? 1 : 2;
                replaceRepairFlag = moldMaintDetailPR.getPartialReplaceIsChecked() ? 3 : replaceRepairFlag;
                moldMaintPart.setReplaceOrRepair(replaceRepairFlag);
                if(moldMaintDetailPR.getReplaceStock() != null) {
                    moldMaintPart.setMoldPartStockId(moldMaintDetailPR.getReplaceStock().getId());
                }
                moldMaintPart.setReplaceQuantity(moldMaintDetailPR.getReplaceQty());
                moldMaintPart.setNewPartQuantity(moldMaintDetailPR.getFromNewQty());
                moldMaintPart.setUsedQuantity(moldMaintDetailPR.getFromUsedQty());
                
                if(0 == moldMaintenanceRemodelingVo.getTemporarilySaved()) {
                    endMaintPart(moldMaintPart, loginUser.getUserUuid(), stockIsAvailable);
                }
            }
        } else if(0 == moldMaintenanceRemodelingVo.getTemporarilySaved()) {
            tblMoldMaintenancePartService.getMaintPartsInMoldMaint(moldMaintenanceRemodelingVo.getMaintenanceId()).stream()
                .filter(moldMaintPart->moldMaintPart.getReplaceOrRepair() != 0)
                .forEach(moldMaintPart->{
                    endMaintPart(moldMaintPart, loginUser.getUserUuid(), stockIsAvailable);
                });
        }
        return basicResponse;
    }
    
    @Transactional
    private void endMaintPart(TblMoldMaintenancePart moldMaintPart, String useruuid, boolean stockIsAvailable) {
        MstMoldPartRel moldPartRel = entityManager.find(MstMoldPartRel.class, moldMaintPart.getTblMoldMaintenancePartPK().getMoldPartRelId());
        Date updatedDate =  new java.util.Date();
        if(1 == moldMaintPart.getReplaceOrRepair()) {
            moldMaintPart.setShotCntAtManit(moldPartRel.getAftRplShotCnt());
            moldPartRel.setAftRplProdTimeHour(BigDecimal.ZERO);
            moldPartRel.setAftRplShotCnt(0);
            moldPartRel.setAftRprProdTimeHour(BigDecimal.ZERO);
            moldPartRel.setAftRprShotCnt(0);
            moldPartRel.setLastRplDatetime(updatedDate);
            moldPartRel.setLastRprDatetime(updatedDate);
        } else if(2 == moldMaintPart.getReplaceOrRepair()) {
            moldMaintPart.setShotCntAtManit(moldPartRel.getAftRprShotCnt());
            moldPartRel.setAftRprProdTimeHour(BigDecimal.ZERO);
            moldPartRel.setAftRprShotCnt(0);
            moldPartRel.setLastRprDatetime(updatedDate);
        } else if(3 == moldMaintPart.getReplaceOrRepair()) {
            moldMaintPart.setShotCntAtManit(moldPartRel.getAftRplShotCnt());
        } else {
            return;
        }
        String moldPartMaintRecommendSql = "SELECT mpManitRecommend FROM TblMoldPartMaintenanceRecommend mpManitRecommend"
            + " WHERE mpManitRecommend.moldUuid = :moldUuid AND mpManitRecommend.moldPartRelId = :moldPartRelId"
            + " AND mpManitRecommend.maintainedFlag = 0";
        entityManager.createQuery(moldPartMaintRecommendSql, TblMoldPartMaintenanceRecommend.class)
            .setParameter("moldUuid", moldPartRel.getMoldUuid())
            .setParameter("moldPartRelId", moldPartRel.getId()).getResultList().stream()
            .filter(recommend->!(1 == recommend.getReplaceOrRepair() && 2 == moldMaintPart.getReplaceOrRepair()))
            .forEach(recommend->{
                recommend.setMaintainedFlag(1);
            });
        
        if(stockIsAvailable && 2 != moldMaintPart.getReplaceOrRepair()) {
            reduceStock(moldPartRel.getMoldUuid(), moldPartRel.getMoldPartId(), moldMaintPart, useruuid);
        }
    }
    
    private TblMoldMaintenancePart getMaintPart(String maintenanceId, String moldPartRelId, String useruuid) {
        return entityManager.createNamedQuery("TblMoldMaintenancePart.findByMoldMaintenancePartId", TblMoldMaintenancePart.class)
            .setParameter("maintenanceId", maintenanceId)
            .setParameter("moldPartRelId", moldPartRelId).getResultList().stream().findFirst().orElseGet(()->{
                TblMoldMaintenancePart moldMaintPart = new TblMoldMaintenancePart();
                TblMoldMaintenancePartPK tblMoldMaintenancePartPK = new TblMoldMaintenancePartPK();
                tblMoldMaintenancePartPK.setMaintenanceId(maintenanceId);
                tblMoldMaintenancePartPK.setMoldPartRelId(moldPartRelId);
                moldMaintPart.setMoldMaintenancePartPK(tblMoldMaintenancePartPK);
                moldMaintPart.setCreateDate(new java.util.Date());
                moldMaintPart.setCreateUserUuid(useruuid);
                moldMaintPart.setUpdateDate(new java.util.Date());
                moldMaintPart.setUpdateUserUuid(useruuid);
                moldMaintPart.setShotCntAtManit(0);
                return entityManager.merge(moldMaintPart);
            });
    }
    
    @Transactional
    private void reduceStock(String molduuid, String moldPartId, TblMoldMaintenancePart moldMaintPart, String useruuid) {
        MoldPartStock stock;
        if(StringUtils.isEmpty(moldMaintPart.getMoldPartStockId())) {
            stock = moldPartStockService.getMoldPartStockWithLock(moldPartId, molduuid).orElseGet(()->{
                Date now = new Date();
                MoldPartStock newStock = new MoldPartStock();
                newStock.setId(IDGenerator.generate());
                newStock.setBasicUpdateDate(now);
                newStock.setBasicUpdateUserUuid(useruuid);
                newStock.setCreateDate(now);
                newStock.setCreateUserUuid(useruuid);
                newStock.setUpdateDate(now);
                newStock.setUpdateUserUuid(useruuid);
                newStock.setMoldPartId(moldPartId);
                newStock.setMoldUuid(molduuid);
                return entityManager.merge(newStock);
            });
            moldMaintPart.setMoldPartStockId(stock.getId());
        } else {
            stock = moldPartStockService.getMoldPartStockWithLock(moldMaintPart.getMoldPartStockId());
        }
        int dif = moldMaintPart.getNewPartQuantity() * -1;
        int usedDif = moldMaintPart.getRecycleQuantity() - moldMaintPart.getUsedQuantity();
        stock.setStock(stock.getStock() + dif);
        stock.setUsedStock(stock.getUsedStock() + usedDif);
        if(stock.getStatus() == MoldPartStock.Status.NORMAL && stock.getStock() <= stock.getOrderPoint()) {
            stock.setStatus(MoldPartStock.Status.ORDER_REQ);
        }
        stock.setMaitenanceId(moldMaintPart.getTblMoldMaintenancePartPK().getMaintenanceId());
        stock.setUpdateDate(new Date());
        stock.setUpdateUserUuid(useruuid);
        addStockIO(stock, dif, usedDif, moldMaintPart.getTblMoldMaintenancePartPK().getMaintenanceId(), useruuid);
    }
    
    @Transactional
    private void addStockIO(MoldPartStock stock, int dif, int usedDif, String maintId, String useruuid) {
        MoldPartStockInOut io = new MoldPartStockInOut();
        Date now = new Date();
        io.setId(IDGenerator.generate());
        io.setMoldPartStockId(stock.getId());
        io.setMoldMaintId(maintId);
        io.setIoEvent(MoldPartStockInOut.IoEvent.EXCHANGE);
        io.setStock(stock.getStock());
        io.setNewStockIo(dif);
        io.setUsedStock(stock.getUsedStock());
        io.setUsedStockIo(usedDif);
        io.setCreateDate(now);
        io.setCreateUserUuid(useruuid);
        io.setIoDate(now);
        io.setUpdateDate(now);
        io.setUpdateUserUuid(useruuid);
        entityManager.persist(io);
    }

    /**
     *
     * @param moldMaintenanceRemodelingVo
     * @param type
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postMoldMaintenanceDetailes2(TblMoldMaintenanceRemodelingVo moldMaintenanceRemodelingVo, int type, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

//        basicResponse = FileUtil.checkExternal(entityManager, mstDictionaryService, moldMaintenanceRemodelingVo.getMoldId(), loginUser);
//        if (basicResponse.isError()) {
//            return basicResponse;
//        }

        //金型改造メンテナンス 更新 メンテナンス分類、報告事項、
        TblMoldMaintenanceRemodeling moldMaintenanceRemodeling = entityManager.find(TblMoldMaintenanceRemodeling.class, moldMaintenanceRemodelingVo.getMaintenanceId());
        if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {
            moldMaintenanceRemodeling.setMainteType(moldMaintenanceRemodelingVo.getMainteType());
        } else if (type == CommonConstants.MAINTEORREMODEL_REMODEL) {
            moldMaintenanceRemodeling.setRemodelingType(moldMaintenanceRemodelingVo.getMainteType()); //js名用誤り
        }
        moldMaintenanceRemodeling.setWorkingTimeMinutes(moldMaintenanceRemodelingVo.getWorkingTimeMinutes());//所要時間 20170901 Apeng add

        moldMaintenanceRemodeling.setReport(moldMaintenanceRemodelingVo.getReport());
        moldMaintenanceRemodeling.setDirectionId(moldMaintenanceRemodelingVo.getDirectionId());
        //手配・工事番号のコード直接保存のとき
        if (moldMaintenanceRemodelingVo.getDirectionId() == null && moldMaintenanceRemodelingVo.getDirectionCode() != null) {
            moldMaintenanceRemodeling.setDirectionCode(moldMaintenanceRemodelingVo.getDirectionCode());
        }
        moldMaintenanceRemodeling.setUpdateUserUuid(loginUser.getUserUuid());

        moldMaintenanceRemodeling.setUpdateDate(new Date());
        moldMaintenanceRemodeling.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(moldMaintenanceRemodeling);

        if (null != moldMaintenanceRemodelingVo.getIssueId() && !"".equals(moldMaintenanceRemodelingVo.getIssueId().trim())) {
            TblIssue tblIssue = entityManager.find(TblIssue.class, moldMaintenanceRemodelingVo.getIssueId());
            if (tblIssue != null) {
                try {
                    String measureStatus = moldMaintenanceRemodelingVo.getMeasureStatus();
                    tblIssue.setMeasureStatus(Integer.parseInt(measureStatus));
                    if (String.valueOf(CommonConstants.ISSUE_MEASURE_STATUS_TEMPORARILY_RESOLVED).equals(measureStatus)
                            || String.valueOf(CommonConstants.ISSUE_MEASURE_STATUS_PERMANENTLY_RESOLVED).equals(measureStatus)
                            || String.valueOf(CommonConstants.ISSUE_MEASURE_STATUS_COMPLETED).equals(measureStatus)) {
                        tblIssue.setMeasuerCompletedDate(new Date());
                    }
                } catch (NumberFormatException e) {

                }

                tblIssue.setUpdateDate(new Date());
                tblIssue.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.merge(tblIssue);
            }
        }

        List detailVos = null;
        if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {
            //金型メンテナンス詳細 追加
            detailVos = moldMaintenanceRemodelingVo.getMoldMaintenanceDetailVo();

        } else if (type == CommonConstants.MAINTEORREMODEL_REMODEL) {
            detailVos = moldMaintenanceRemodelingVo.getMoldRemodelingDetailVo();
        }

        if (null != detailVos && !detailVos.isEmpty()) {
            if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {
                List<TblMoldMaintenanceDetailVo> newMaintenanceDetailVo = new ArrayList<>();
                for (int i = 0; i < detailVos.size(); i++) {
                    TblMoldMaintenanceDetailVo moldMaintenanceDetailVo = (TblMoldMaintenanceDetailVo) detailVos.get(i);
                    if (!"1".equals(moldMaintenanceDetailVo.getDeleteFlag())) {
                        newMaintenanceDetailVo.add(moldMaintenanceDetailVo);
                    }
                }

                Query query = entityManager.createQuery("DELETE FROM TblMoldMaintenanceDetail t WHERE t.tblMoldMaintenanceDetailPK.maintenanceId = :maintenanceId");
                query.setParameter("maintenanceId", moldMaintenanceRemodeling.getId());
                query.executeUpdate();

                for (int i = 0; i < newMaintenanceDetailVo.size(); i++) {
                    TblMoldMaintenanceDetailVo moldMaintenanceDetailVo = (TblMoldMaintenanceDetailVo) newMaintenanceDetailVo.get(i);

                    TblMoldMaintenanceDetail moldMaintenanceDetail = new TblMoldMaintenanceDetail();
                    if (null != moldMaintenanceDetailVo.getMainteReasonCategory1() && !moldMaintenanceDetailVo.getMainteReasonCategory1().equals("")) {
                        moldMaintenanceDetail.setMainteReasonCategory1(Integer.parseInt(moldMaintenanceDetailVo.getMainteReasonCategory1()));
                        moldMaintenanceDetail.setMainteReasonCategory1Text(moldMaintenanceDetailVo.getMainteReasonCategory1Text());

                        if (null != moldMaintenanceDetailVo.getMainteReasonCategory2() && !moldMaintenanceDetailVo.getMainteReasonCategory2().equals("")) {
                            moldMaintenanceDetail.setMainteReasonCategory2(Integer.parseInt(moldMaintenanceDetailVo.getMainteReasonCategory2()));
                            moldMaintenanceDetail.setMainteReasonCategory2Text(moldMaintenanceDetailVo.getMainteReasonCategory2Text());

                            if (null != moldMaintenanceDetailVo.getMainteReasonCategory3() && !moldMaintenanceDetailVo.getMainteReasonCategory3().equals("")) {
                                moldMaintenanceDetail.setMainteReasonCategory3(Integer.parseInt(moldMaintenanceDetailVo.getMainteReasonCategory3()));
                                moldMaintenanceDetail.setMainteReasonCategory3Text(moldMaintenanceDetailVo.getMainteReasonCategory3Text());
                            } else {
                                moldMaintenanceDetail.setMainteReasonCategory3(null);
                                moldMaintenanceDetail.setMainteReasonCategory3Text(null);
                            }
                        } else {
                            moldMaintenanceDetail.setMainteReasonCategory2(null);
                            moldMaintenanceDetail.setMainteReasonCategory2Text(null);
                            moldMaintenanceDetail.setMainteReasonCategory3(null);
                            moldMaintenanceDetail.setMainteReasonCategory3Text(null);
                        }
                    } else {
                        moldMaintenanceDetail.setMainteReasonCategory1(null);
                        moldMaintenanceDetail.setMainteReasonCategory1Text(null);
                        moldMaintenanceDetail.setMainteReasonCategory2(null);
                        moldMaintenanceDetail.setMainteReasonCategory2Text(null);
                        moldMaintenanceDetail.setMainteReasonCategory3(null);
                        moldMaintenanceDetail.setMainteReasonCategory3Text(null);
                    }
                    moldMaintenanceDetail.setManiteReason(moldMaintenanceDetailVo.getManiteReason());

                    if (null != moldMaintenanceDetailVo.getTaskCategory1() && !moldMaintenanceDetailVo.getTaskCategory1().equals("")) {
                        moldMaintenanceDetail.setTaskCategory1(Integer.parseInt(moldMaintenanceDetailVo.getTaskCategory1()));
                        moldMaintenanceDetail.setTaskCategory1Text(moldMaintenanceDetailVo.getTaskCategory1Text());
                        if (null != moldMaintenanceDetailVo.getTaskCategory2() && !moldMaintenanceDetailVo.getTaskCategory2().equals("")) {
                            moldMaintenanceDetail.setTaskCategory2(Integer.parseInt(moldMaintenanceDetailVo.getTaskCategory2()));
                            moldMaintenanceDetail.setTaskCategory2Text(moldMaintenanceDetailVo.getTaskCategory2Text());
                            if (null != moldMaintenanceDetailVo.getTaskCategory3() && !moldMaintenanceDetailVo.getTaskCategory3().equals("")) {
                                moldMaintenanceDetail.setTaskCategory3(Integer.parseInt(moldMaintenanceDetailVo.getTaskCategory3()));
                                moldMaintenanceDetail.setTaskCategory3Text(moldMaintenanceDetailVo.getTaskCategory3Text());
                            } else {
                                moldMaintenanceDetail.setTaskCategory3(null);
                                moldMaintenanceDetail.setTaskCategory3Text(null);
                            }
                        } else {
                            moldMaintenanceDetail.setTaskCategory2(null);
                            moldMaintenanceDetail.setTaskCategory2Text(null);
                            moldMaintenanceDetail.setTaskCategory3(null);
                            moldMaintenanceDetail.setTaskCategory3Text(null);
                        }
                    } else {
                        moldMaintenanceDetail.setTaskCategory1(null);
                        moldMaintenanceDetail.setTaskCategory1Text(null);
                        moldMaintenanceDetail.setTaskCategory2(null);
                        moldMaintenanceDetail.setTaskCategory2Text(null);
                        moldMaintenanceDetail.setTaskCategory3(null);
                        moldMaintenanceDetail.setTaskCategory3Text(null);
                    }
                    moldMaintenanceDetail.setTask(moldMaintenanceDetailVo.getTask());

                    if (null != moldMaintenanceDetailVo.getMeasureDirectionCategory1() && !moldMaintenanceDetailVo.getMeasureDirectionCategory1().equals("")) {
                        moldMaintenanceDetail.setMeasureDirectionCategory1(Integer.parseInt(moldMaintenanceDetailVo.getMeasureDirectionCategory1()));
                        moldMaintenanceDetail.setMeasureDirectionCategory1Text(moldMaintenanceDetailVo.getMeasureDirectionCategory1Text());
                        if (null != moldMaintenanceDetailVo.getMeasureDirectionCategory2() && !moldMaintenanceDetailVo.getMeasureDirectionCategory2().equals("")) {
                            moldMaintenanceDetail.setMeasureDirectionCategory2(Integer.parseInt(moldMaintenanceDetailVo.getMeasureDirectionCategory2()));
                            moldMaintenanceDetail.setMeasureDirectionCategory2Text(moldMaintenanceDetailVo.getMeasureDirectionCategory2Text());
                            if (null != moldMaintenanceDetailVo.getMeasureDirectionCategory3() && !moldMaintenanceDetailVo.getMeasureDirectionCategory3().equals("")) {
                                moldMaintenanceDetail.setMeasureDirectionCategory3(Integer.parseInt(moldMaintenanceDetailVo.getMeasureDirectionCategory3()));
                                moldMaintenanceDetail.setMeasureDirectionCategory3Text(moldMaintenanceDetailVo.getMeasureDirectionCategory3Text());
                            } else {
                                moldMaintenanceDetail.setMeasureDirectionCategory3(null);
                                moldMaintenanceDetail.setMeasureDirectionCategory3Text(null);
                            }
                        } else {
                            moldMaintenanceDetail.setMeasureDirectionCategory2(null);
                            moldMaintenanceDetail.setMeasureDirectionCategory2Text(null);
                            moldMaintenanceDetail.setMeasureDirectionCategory3(null);
                            moldMaintenanceDetail.setMeasureDirectionCategory3Text(null);
                        }
                    } else {
                        moldMaintenanceDetail.setMeasureDirectionCategory1(null);
                        moldMaintenanceDetail.setMeasureDirectionCategory1Text(null);
                        moldMaintenanceDetail.setMeasureDirectionCategory2(null);
                        moldMaintenanceDetail.setMeasureDirectionCategory2Text(null);
                        moldMaintenanceDetail.setMeasureDirectionCategory3(null);
                        moldMaintenanceDetail.setMeasureDirectionCategory3Text(null);
                    }
                    moldMaintenanceDetail.setMeasureDirection(moldMaintenanceDetailVo.getMeasureDirection());

                    TblMoldMaintenanceDetailPK pk = new TblMoldMaintenanceDetailPK();
                    pk.setMaintenanceId(moldMaintenanceRemodeling.getId());
                    pk.setSeq(moldMaintenanceDetailVo.getSeq());
                    moldMaintenanceDetail.setTblMoldMaintenanceDetailPK(pk);
                    moldMaintenanceDetail.setId(IDGenerator.generate());
                    moldMaintenanceDetail.setCreateDate(new Date());
                    moldMaintenanceDetail.setCreateUserUuid(loginUser.getUserUuid());
                    moldMaintenanceDetail.setUpdateDate(new Date());
                    moldMaintenanceDetail.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.persist(moldMaintenanceDetail);

                    //金型点検結果 追加
                    List<TblMoldInspectionResultVo> moldInspectionResultVos = moldMaintenanceRemodelingVo.getMoldInspectionResultVo();
                    if (null == moldInspectionResultVos || moldInspectionResultVos.isEmpty()) {
                        moldInspectionResultVos = moldMaintenanceDetailVo.getMoldInspectionResultVos();
                    }
                    if (null != moldInspectionResultVos && !moldInspectionResultVos.isEmpty()) {
                        List<TblMoldInspectionResultVo> newInspectionResultVo = new ArrayList<>();
                        Query delInspectionResultQuery = entityManager.createNamedQuery("TblMoldInspectionResult.deleteByMaintenanceDetailId");
                        if (null != moldMaintenanceDetailVo.getId() && null != moldMaintenanceRemodelingVo.getMoldInspectionResultVo() && !moldMaintenanceRemodelingVo.getMoldInspectionResultVo().isEmpty()) {
                            delInspectionResultQuery.setParameter("maintenanceDetailId", moldMaintenanceDetailVo.getId());

                            for (TblMoldInspectionResultVo aMoldInspectionResultVo : moldInspectionResultVos) {
                                if (aMoldInspectionResultVo.getMaintenanceDetailId().equals(moldMaintenanceDetailVo.getId())) {
                                    newInspectionResultVo.add(aMoldInspectionResultVo);
                                }
                            }
                        } else {
                            delInspectionResultQuery.setParameter("maintenanceDetailId", moldMaintenanceDetail.getId());
                            newInspectionResultVo = moldInspectionResultVos;
                        }
                        delInspectionResultQuery.executeUpdate();
                        for (TblMoldInspectionResultVo aMoldInspectionResultVo : newInspectionResultVo) {
                            TblMoldInspectionResult aResult = new TblMoldInspectionResult();
                            aResult.setUpdateDate(new Date());
                            aResult.setUpdateUserUuid(loginUser.getUserUuid());
                            aResult.setInspectionResult(aMoldInspectionResultVo.getInspectionResult());
                            aResult.setInspectionResultText(aMoldInspectionResultVo.getInspectionResultText());

                            aResult.setId(IDGenerator.generate());
                            TblMoldInspectionResultPK resultPK = new TblMoldInspectionResultPK();
                            resultPK.setInspectionItemId(aMoldInspectionResultVo.getInspectionItemId());
                            resultPK.setMaintenanceDetailId(moldMaintenanceDetail.getId());
                            resultPK.setSeq(aMoldInspectionResultVo.getSeq());
                            aResult.setTblMoldInspectionResultPK(resultPK);

                            aResult.setCreateDate(new Date());
                            aResult.setCreateUserUuid(loginUser.getUserUuid());
                            entityManager.persist(aResult);
                        }
                    }

                    //金型ImageFile 追加
                    List<TblMoldMaintenanceDetailImageFileVo> moldMaintenanceDetailImageFileVos = moldMaintenanceRemodelingVo.getImageFileVos();
                    if (null == moldMaintenanceDetailImageFileVos || moldMaintenanceDetailImageFileVos.isEmpty()) {
                        moldMaintenanceDetailImageFileVos = moldMaintenanceDetailVo.getMoldMaintenanceDetailImageFileVos();
                    }
                    if (null != moldMaintenanceDetailImageFileVos && !moldMaintenanceDetailImageFileVos.isEmpty()) {
                        List<TblMoldMaintenanceDetailImageFileVo> newMoldMaintenanceDetailImageFileVos = new ArrayList<>();
                        Query delImageFileQuery = entityManager.createNamedQuery("TblMoldMaintenanceDetailImageFile.deleteByMaintenanceDetailId");
                        if (null != moldMaintenanceDetailVo.getId() && null != moldMaintenanceRemodelingVo.getImageFileVos() && !moldMaintenanceRemodelingVo.getImageFileVos().isEmpty()) {
                            delImageFileQuery.setParameter("maintenanceDetailId", moldMaintenanceDetailVo.getId());

                            for (TblMoldMaintenanceDetailImageFileVo aMoldMaintenanceDetailImageFileVo : moldMaintenanceDetailImageFileVos) {
                                if (aMoldMaintenanceDetailImageFileVo.getMaintenanceDetailId().equals(moldMaintenanceDetailVo.getId())) {
                                    newMoldMaintenanceDetailImageFileVos.add(aMoldMaintenanceDetailImageFileVo);
                                }
                            }
                        } else {
                            delImageFileQuery.setParameter("maintenanceDetailId", moldMaintenanceDetail.getId());
                            newMoldMaintenanceDetailImageFileVos = moldMaintenanceDetailImageFileVos;
                        }
                        delImageFileQuery.executeUpdate();
                        for (TblMoldMaintenanceDetailImageFileVo aImageFileVo : newMoldMaintenanceDetailImageFileVos) {
                            TblMoldMaintenanceDetailImageFile aImageFile = new TblMoldMaintenanceDetailImageFile();
                            aImageFile.setUpdateDate(new Date());
                            aImageFile.setUpdateUserUuid(loginUser.getUserUuid());
                            aImageFile.setCreateDate(new Date());
                            aImageFile.setCreateDateUuid(loginUser.getUserUuid());

                            TblMoldMaintenanceDetailImageFilePK aPK = new TblMoldMaintenanceDetailImageFilePK();
                            aPK.setMaintenanceDetailId(moldMaintenanceDetail.getId());
                            aPK.setSeq(Integer.parseInt(aImageFileVo.getSeq()));
                            aImageFile.setTblMoldMaintenanceDetailImageFilePK(aPK);

                            if (null != aImageFileVo.getFileExtension() && !aImageFileVo.getFileExtension().isEmpty()) {
                                aImageFile.setFileExtension(aImageFileVo.getFileExtension());
                            } else {
                                aImageFile.setFileExtension(null);
                            }
                            if (null != aImageFileVo.getFileType() && !aImageFileVo.getFileType().isEmpty()) {
                                aImageFile.setFileType(Integer.parseInt(aImageFileVo.getFileType()));
                            } else {
                                aImageFile.setFileType(null);
                            }
                            if (null != aImageFileVo.getFileUuid() && !aImageFileVo.getFileUuid().isEmpty()) {
                                aImageFile.setFileUuid(aImageFileVo.getFileUuid());
                            }
                            if (null != aImageFileVo.getRemarks() && !aImageFileVo.getRemarks().isEmpty()) {
                                aImageFile.setRemarks(aImageFileVo.getRemarks());
                            } else {
                                aImageFile.setRemarks(null);
                            }
                            if (null != aImageFileVo.getTakenDateStzStr() && !aImageFileVo.getTakenDateStzStr().isEmpty()) {
                                aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStzStr()));
                            } else {
                                aImageFile.setTakenDateStz(null);
                            }
                            if (null != aImageFileVo.getTakenDateStr() && !aImageFileVo.getTakenDateStr().isEmpty()) {
                                aImageFile.setTakenDate(new FileUtil().getDateTimeParseForDate(DateFormat.dateTimeFormat(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()), loginUser.getJavaZoneId())));
                                aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()));
                            } else {
                                aImageFile.setTakenDate(null);
                                aImageFile.setTakenDateStz(null);
                            }
                            if (null != aImageFileVo.getThumbnailFileUuid() && !aImageFileVo.getThumbnailFileUuid().isEmpty()) {
                                aImageFile.setThumbnailFileUuid(aImageFileVo.getThumbnailFileUuid());
                            } else {
                                aImageFile.setThumbnailFileUuid(null);
                            }

                            entityManager.persist(aImageFile);
                        }
                    }
                }
            } else if (type == CommonConstants.MAINTEORREMODEL_REMODEL) {
                List<TblMoldRemodelingDetailVo> newRemodelingDetailVo = new ArrayList<>();
                for (int i = 0; i < detailVos.size(); i++) {
                    TblMoldRemodelingDetailVo moldRemodelingDetailVo = (TblMoldRemodelingDetailVo) detailVos.get(i);
                    if (!"1".equals(moldRemodelingDetailVo.getDeleteFlag())) {
                        newRemodelingDetailVo.add(moldRemodelingDetailVo);
                    }
                }

                Query query = entityManager.createQuery("DELETE FROM TblMoldRemodelingDetail t WHERE t.tblMoldRemodelingDetailPK.maintenanceId = :maintenanceId");
                query.setParameter("maintenanceId", moldMaintenanceRemodeling.getId());
                query.executeUpdate();

                for (int i = 0; i < newRemodelingDetailVo.size(); i++) {
                    TblMoldRemodelingDetailVo moldRemodelingDetailVo = (TblMoldRemodelingDetailVo) newRemodelingDetailVo.get(i);

                    TblMoldRemodelingDetail moldRemodelingDetail = new TblMoldRemodelingDetail();
                    if (null != moldRemodelingDetailVo.getRemodelReasonCategory1() && !moldRemodelingDetailVo.getRemodelReasonCategory1().equals("")) {
                        moldRemodelingDetail.setRemodelReasonCategory1(Integer.parseInt(moldRemodelingDetailVo.getRemodelReasonCategory1()));
                        moldRemodelingDetail.setRemodelReasonCategory1Text(moldRemodelingDetailVo.getRemodelReasonCategory1Text());

                        if (null != moldRemodelingDetailVo.getRemodelReasonCategory2() && !moldRemodelingDetailVo.getRemodelReasonCategory2().equals("")) {
                            moldRemodelingDetail.setRemodelReasonCategory2(Integer.parseInt(moldRemodelingDetailVo.getRemodelReasonCategory2()));
                            moldRemodelingDetail.setRemodelReasonCategory2Text(moldRemodelingDetailVo.getRemodelReasonCategory2Text());

                            if (null != moldRemodelingDetailVo.getRemodelReasonCategory3() && !moldRemodelingDetailVo.getRemodelReasonCategory3().equals("")) {
                                moldRemodelingDetail.setRemodelReasonCategory3(Integer.parseInt(moldRemodelingDetailVo.getRemodelReasonCategory3()));
                                moldRemodelingDetail.setRemodelReasonCategory3Text(moldRemodelingDetailVo.getRemodelReasonCategory3Text());
                            } else {
                                moldRemodelingDetail.setRemodelReasonCategory3(null);
                                moldRemodelingDetail.setRemodelReasonCategory3Text(null);
                            }
                        } else {
                            moldRemodelingDetail.setRemodelReasonCategory2(null);
                            moldRemodelingDetail.setRemodelReasonCategory2Text(null);
                            moldRemodelingDetail.setRemodelReasonCategory3(null);
                            moldRemodelingDetail.setRemodelReasonCategory3Text(null);
                        }
                    } else {
                        moldRemodelingDetail.setRemodelReasonCategory1(null);
                        moldRemodelingDetail.setRemodelReasonCategory1Text(null);
                        moldRemodelingDetail.setRemodelReasonCategory2(null);
                        moldRemodelingDetail.setRemodelReasonCategory2Text(null);
                        moldRemodelingDetail.setRemodelReasonCategory3(null);
                        moldRemodelingDetail.setRemodelReasonCategory3Text(null);
                    }
                    moldRemodelingDetail.setRemodelReason(moldRemodelingDetailVo.getRemodelReason());

                    if (null != moldRemodelingDetailVo.getTaskCategory1() && !moldRemodelingDetailVo.getTaskCategory1().equals("")) {
                        moldRemodelingDetail.setTaskCategory1(Integer.parseInt(moldRemodelingDetailVo.getTaskCategory1()));
                        moldRemodelingDetail.setTaskCategory1Text(moldRemodelingDetailVo.getTaskCategory1Text());
                        if (null != moldRemodelingDetailVo.getTaskCategory2() && !moldRemodelingDetailVo.getTaskCategory2().equals("")) {
                            moldRemodelingDetail.setTaskCategory2(Integer.parseInt(moldRemodelingDetailVo.getTaskCategory2()));
                            moldRemodelingDetail.setTaskCategory2Text(moldRemodelingDetailVo.getTaskCategory2Text());
                            if (null != moldRemodelingDetailVo.getTaskCategory3() && !moldRemodelingDetailVo.getTaskCategory3().equals("")) {
                                moldRemodelingDetail.setTaskCategory3(Integer.parseInt(moldRemodelingDetailVo.getTaskCategory3()));
                                moldRemodelingDetail.setTaskCategory3Text(moldRemodelingDetailVo.getTaskCategory3Text());
                            } else {
                                moldRemodelingDetail.setTaskCategory3(null);
                                moldRemodelingDetail.setTaskCategory3Text(null);
                            }
                        } else {
                            moldRemodelingDetail.setTaskCategory2(null);
                            moldRemodelingDetail.setTaskCategory2Text(null);
                            moldRemodelingDetail.setTaskCategory3(null);
                            moldRemodelingDetail.setTaskCategory3Text(null);
                        }
                    } else {
                        moldRemodelingDetail.setTaskCategory1(null);
                        moldRemodelingDetail.setTaskCategory1Text(null);
                        moldRemodelingDetail.setTaskCategory2(null);
                        moldRemodelingDetail.setTaskCategory2Text(null);
                        moldRemodelingDetail.setTaskCategory3(null);
                        moldRemodelingDetail.setTaskCategory3Text(null);
                    }
                    moldRemodelingDetail.setTask(moldRemodelingDetailVo.getTask());

                    if (null != moldRemodelingDetailVo.getRemodelDirectionCategory1() && !moldRemodelingDetailVo.getRemodelDirectionCategory1().equals("")) {
                        moldRemodelingDetail.setRemodelDirectionCategory1(Integer.parseInt(moldRemodelingDetailVo.getRemodelDirectionCategory1()));
                        moldRemodelingDetail.setRemodelDirectionCategory1Text(moldRemodelingDetailVo.getRemodelDirectionCategory1Text());
                        if (null != moldRemodelingDetailVo.getRemodelDirectionCategory2() && !moldRemodelingDetailVo.getRemodelDirectionCategory2().equals("")) {
                            moldRemodelingDetail.setRemodelDirectionCategory2(Integer.parseInt(moldRemodelingDetailVo.getRemodelDirectionCategory2()));
                            moldRemodelingDetail.setRemodelDirectionCategory2Text(moldRemodelingDetailVo.getRemodelDirectionCategory2Text());
                            if (null != moldRemodelingDetailVo.getRemodelDirectionCategory3() && !moldRemodelingDetailVo.getRemodelDirectionCategory3().equals("")) {
                                moldRemodelingDetail.setRemodelDirectionCategory3(Integer.parseInt(moldRemodelingDetailVo.getRemodelDirectionCategory3()));
                                moldRemodelingDetail.setRemodelDirectionCategory3Text(moldRemodelingDetailVo.getRemodelDirectionCategory3Text());
                            } else {
                                moldRemodelingDetail.setRemodelDirectionCategory3(null);
                                moldRemodelingDetail.setRemodelDirectionCategory3Text(null);
                            }
                        } else {
                            moldRemodelingDetail.setRemodelDirectionCategory2(null);
                            moldRemodelingDetail.setRemodelDirectionCategory2Text(null);
                            moldRemodelingDetail.setRemodelDirectionCategory3(null);
                            moldRemodelingDetail.setRemodelDirectionCategory3Text(null);
                        }
                    } else {
                        moldRemodelingDetail.setRemodelDirectionCategory1(null);
                        moldRemodelingDetail.setRemodelDirectionCategory1Text(null);
                        moldRemodelingDetail.setRemodelDirectionCategory2(null);
                        moldRemodelingDetail.setRemodelDirectionCategory2Text(null);
                        moldRemodelingDetail.setRemodelDirectionCategory3(null);
                        moldRemodelingDetail.setRemodelDirectionCategory3Text(null);
                    }
                    moldRemodelingDetail.setRemodelDirection(moldRemodelingDetailVo.getRemodelDirection());

                    TblMoldRemodelingDetailPK pk = new TblMoldRemodelingDetailPK();
                    pk.setMaintenanceId(moldMaintenanceRemodeling.getId());
                    pk.setSeq(moldRemodelingDetailVo.getSeq());
                    moldRemodelingDetail.setTblMoldRemodelingDetailPK(pk);
                    moldRemodelingDetail.setId(IDGenerator.generate());
                    moldRemodelingDetail.setCreateDate(new Date());
                    moldRemodelingDetail.setCreateUserUuid(loginUser.getUserUuid());
                    moldRemodelingDetail.setUpdateDate(new Date());
                    moldRemodelingDetail.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.persist(moldRemodelingDetail);

                    //金型点検結果 追加
                    List<TblMoldRemodelingInspectionResultVo> moldRemodelingInspectionResultVos = moldMaintenanceRemodelingVo.getMoldRemodelingInspectionResultVo();
                    if (null == moldRemodelingInspectionResultVos || moldRemodelingInspectionResultVos.isEmpty()) {
                        moldRemodelingInspectionResultVos = moldRemodelingDetailVo.getMoldRemodelingInspectionResultVos();
                    }
                    if (null != moldRemodelingInspectionResultVos && !moldRemodelingInspectionResultVos.isEmpty()) {
                        List<TblMoldRemodelingInspectionResultVo> newRemodelingInspectionResultVo = new ArrayList<>();
                        Query delInspectionResultQuery = entityManager.createQuery("DELETE FROM TblMoldRemodelingInspectionResult t WHERE t.tblMoldRemodelingInspectionResultPK.remodelingDetailId = :remodelingDetailId");
                        if (null != moldRemodelingDetailVo.getId() && null != moldMaintenanceRemodelingVo.getMoldRemodelingInspectionResultVo() && !moldMaintenanceRemodelingVo.getMoldRemodelingInspectionResultVo().isEmpty()) {
                            delInspectionResultQuery.setParameter("remodelingDetailId", moldRemodelingDetailVo.getId());
                            for (TblMoldRemodelingInspectionResultVo aMoldRemodelingInspectionResultVo : moldRemodelingInspectionResultVos) {
                                if (aMoldRemodelingInspectionResultVo.getRemodelingDetailId().equals(moldRemodelingDetailVo.getId())) {
                                    newRemodelingInspectionResultVo.add(aMoldRemodelingInspectionResultVo);
                                }
                            }
                        } else {
                            delInspectionResultQuery.setParameter("remodelingDetailId", moldRemodelingDetail.getId());
                            newRemodelingInspectionResultVo = moldRemodelingInspectionResultVos;
                        }
                        delInspectionResultQuery.executeUpdate();

                        for (TblMoldRemodelingInspectionResultVo aMoldRemodelingInspectionResultVo : newRemodelingInspectionResultVo) {
                            TblMoldRemodelingInspectionResult aResult = new TblMoldRemodelingInspectionResult();
                            aResult.setUpdateDate(new Date());
                            aResult.setUpdateUserUuid(loginUser.getUserUuid());
                            aResult.setInspectionResult(aMoldRemodelingInspectionResultVo.getInspectionResult());
                            aResult.setInspectionResultText(aMoldRemodelingInspectionResultVo.getInspectionResultText());

                            aResult.setId(IDGenerator.generate());
                            TblMoldRemodelingInspectionResultPK resultPK = new TblMoldRemodelingInspectionResultPK();
                            resultPK.setInspectionItemId(aMoldRemodelingInspectionResultVo.getInspectionItemId());
                            resultPK.setRemodelingDetailId(moldRemodelingDetail.getId());
                            resultPK.setSeq(aMoldRemodelingInspectionResultVo.getSeq());
                            aResult.setTblMoldRemodelingInspectionResultPK(resultPK);

                            aResult.setCreateDate(new Date());
                            aResult.setCreateUserUuid(loginUser.getUserUuid());
                            entityManager.persist(aResult);
                        }
                    }

                    //金型ImageFile 追加
                    List<TblMoldRemodelingDetailImageFileVo> moldRemodelingDetailImageFileVos = moldMaintenanceRemodelingVo.getRimageFileVos();
                    if (null == moldRemodelingDetailImageFileVos || moldRemodelingDetailImageFileVos.isEmpty()) {
                        moldRemodelingDetailImageFileVos = moldRemodelingDetailVo.getMoldRemodelingDetailImageFileVos();
                    }
                    if (null != moldRemodelingDetailImageFileVos && !moldRemodelingDetailImageFileVos.isEmpty()) {
                        List<TblMoldRemodelingDetailImageFileVo> newMoldRemodelingDetailImageFileVos = new ArrayList<>();
                        Query delImageFileQuery = entityManager.createNamedQuery("TblMoldRemodelingDetailImageFile.deleteByRemodelingDetailId");
                        if (null != moldRemodelingDetailVo.getId() && null != moldMaintenanceRemodelingVo.getRimageFileVos() && !moldMaintenanceRemodelingVo.getRimageFileVos().isEmpty()) {
                            delImageFileQuery.setParameter("remodelingDetailId", moldRemodelingDetailVo.getId());

                            for (TblMoldRemodelingDetailImageFileVo aMoldRemodelingDetailImageFileVo : moldRemodelingDetailImageFileVos) {
                                if (aMoldRemodelingDetailImageFileVo.getRemodelingDetailId().equals(moldRemodelingDetailVo.getId())) {
                                    newMoldRemodelingDetailImageFileVos.add(aMoldRemodelingDetailImageFileVo);
                                }
                            }
                        } else {
                            delImageFileQuery.setParameter("remodelingDetailId", moldRemodelingDetail.getId());
                            newMoldRemodelingDetailImageFileVos = moldRemodelingDetailImageFileVos;
                        }
                        delImageFileQuery.executeUpdate();
                        for (TblMoldRemodelingDetailImageFileVo aImageFileVo : newMoldRemodelingDetailImageFileVos) {
                            TblMoldRemodelingDetailImageFile aImageFile = new TblMoldRemodelingDetailImageFile();
                            aImageFile.setUpdateDate(new Date());
                            aImageFile.setUpdateUserUuid(loginUser.getUserUuid());
                            aImageFile.setCreateDate(new Date());
                            aImageFile.setCreateDateUuid(loginUser.getUserUuid());

                            TblMoldRemodelingDetailImageFilePK aPK = new TblMoldRemodelingDetailImageFilePK();
                            aPK.setRemodelingDetailId(moldRemodelingDetail.getId());
                            aPK.setSeq(Integer.parseInt(aImageFileVo.getSeq()));
                            aImageFile.setTblMoldRemodelingDetailImageFilePK(aPK);

                            if (null != aImageFileVo.getFileExtension() && !aImageFileVo.getFileExtension().isEmpty()) {
                                aImageFile.setFileExtension(aImageFileVo.getFileExtension());
                            } else {
                                aImageFile.setFileExtension(null);
                            }
                            if (null != aImageFileVo.getFileType() && !aImageFileVo.getFileType().isEmpty()) {
                                aImageFile.setFileType(Integer.parseInt(aImageFileVo.getFileType()));
                            } else {
                                aImageFile.setFileType(null);
                            }
                            if (null != aImageFileVo.getFileUuid() && !aImageFileVo.getFileUuid().isEmpty()) {
                                aImageFile.setFileUuid(aImageFileVo.getFileUuid());
                            }
                            if (null != aImageFileVo.getRemarks() && !aImageFileVo.getRemarks().isEmpty()) {
                                aImageFile.setRemarks(aImageFileVo.getRemarks());
                            } else {
                                aImageFile.setRemarks(null);
                            }
                            if (null != aImageFileVo.getTakenDateStzStr() && !aImageFileVo.getTakenDateStzStr().isEmpty()) {
                                aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStzStr()));
                            } else {
                                aImageFile.setTakenDateStz(null);
                            }
                            if (null != aImageFileVo.getTakenDateStr() && !aImageFileVo.getTakenDateStr().isEmpty()) {
                                aImageFile.setTakenDate(new FileUtil().getDateTimeParseForDate(DateFormat.dateTimeFormat(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()), loginUser.getJavaZoneId())));
                                aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()));
                            } else {
                                aImageFile.setTakenDate(null);
                                aImageFile.setTakenDateStz(null);
                            }
                            if (null != aImageFileVo.getThumbnailFileUuid() && !aImageFileVo.getThumbnailFileUuid().isEmpty()) {
                                aImageFile.setThumbnailFileUuid(aImageFileVo.getThumbnailFileUuid());
                            } else {
                                aImageFile.setThumbnailFileUuid(null);
                            }

                            entityManager.persist(aImageFile);
                        }
                    }
                }
            }
        }

        return basicResponse;
    }

    /**
     * バッチで金型メンテナンス詳細データを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMoldMaintenanceDetailVo getExtMoldMaintenanceDetailsByBatch(String latestExecutedDate, String moldUuid) {
        TblMoldMaintenanceDetailVo resList = new TblMoldMaintenanceDetailVo();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblMoldMaintenanceDetail t join MstApiUser u on u.companyId = t.tblMoldMaintenanceRemodeling.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.tblMoldMaintenanceRemodeling.mstMold.uuid = :moldUuid ");
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
        List<TblMoldMaintenanceDetail> tmpList = query.getResultList();
        for (TblMoldMaintenanceDetail tblMoldMaintenanceDetail : tmpList) {
            tblMoldMaintenanceDetail.setTblMoldMaintenanceRemodeling(null);
        }
        resList.setTblMoldMaintenanceDetails(tmpList);
        return resList;
    }

    /**
     * バッチで金型メンテナンス詳細データを更新
     *
     * @param moldMaintenanceDetails
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldMaintenanceDetailsByBatch(List<TblMoldMaintenanceDetail> moldMaintenanceDetails) {
        BasicResponse response = new BasicResponse();
        if (moldMaintenanceDetails != null && !moldMaintenanceDetails.isEmpty()) {
            for (TblMoldMaintenanceDetail aMoldMaintenanceDetail : moldMaintenanceDetails) {
                TblMoldMaintenanceRemodeling moldMaintenanceRemodeling = entityManager.find(TblMoldMaintenanceRemodeling.class, aMoldMaintenanceDetail.getTblMoldMaintenanceDetailPK().getMaintenanceId());
                if (null != moldMaintenanceRemodeling) {
                    
                    List<TblMoldMaintenanceDetail> oldMoldMaintenanceDetails = entityManager.createQuery("SELECT t FROM TblMoldMaintenanceDetail t WHERE t.tblMoldMaintenanceDetailPK.maintenanceId = :maintenanceId and t.tblMoldMaintenanceDetailPK.seq = :seq ")
                            .setParameter("maintenanceId", aMoldMaintenanceDetail.getTblMoldMaintenanceDetailPK().getMaintenanceId())
                            .setParameter("seq", aMoldMaintenanceDetail.getTblMoldMaintenanceDetailPK().getSeq())
                            .setMaxResults(1)
                            .getResultList();

                    TblMoldMaintenanceDetail newDetail;
                    if (null != oldMoldMaintenanceDetails && !oldMoldMaintenanceDetails.isEmpty()) {
                        newDetail = oldMoldMaintenanceDetails.get(0);
                    } else {
                        newDetail = new TblMoldMaintenanceDetail();
                        TblMoldMaintenanceDetailPK pk = new TblMoldMaintenanceDetailPK();
                        pk.setMaintenanceId(aMoldMaintenanceDetail.getTblMoldMaintenanceDetailPK().getMaintenanceId());
                        pk.setSeq(aMoldMaintenanceDetail.getTblMoldMaintenanceDetailPK().getSeq());
                        newDetail.setTblMoldMaintenanceDetailPK(pk);
                        
                    }
                    newDetail.setId(aMoldMaintenanceDetail.getId());
                    newDetail.setMainteReasonCategory1(aMoldMaintenanceDetail.getMainteReasonCategory1());
                    newDetail.setMainteReasonCategory1Text(aMoldMaintenanceDetail.getMainteReasonCategory1Text());
                    newDetail.setMainteReasonCategory2(aMoldMaintenanceDetail.getMainteReasonCategory2());
                    newDetail.setMainteReasonCategory2Text(aMoldMaintenanceDetail.getMainteReasonCategory2Text());
                    newDetail.setMainteReasonCategory3(aMoldMaintenanceDetail.getMainteReasonCategory3());
                    newDetail.setMainteReasonCategory3Text(aMoldMaintenanceDetail.getMainteReasonCategory3Text());
                    newDetail.setManiteReason(aMoldMaintenanceDetail.getManiteReason());
                    newDetail.setMeasureDirectionCategory1(aMoldMaintenanceDetail.getMeasureDirectionCategory1());
                    newDetail.setMeasureDirectionCategory1Text(aMoldMaintenanceDetail.getMeasureDirectionCategory1Text());
                    newDetail.setMeasureDirectionCategory2(aMoldMaintenanceDetail.getMeasureDirectionCategory2());
                    newDetail.setMeasureDirectionCategory2Text(aMoldMaintenanceDetail.getMeasureDirectionCategory2Text());
                    newDetail.setMeasureDirectionCategory3(aMoldMaintenanceDetail.getMeasureDirectionCategory3());
                    newDetail.setMeasureDirectionCategory3Text(aMoldMaintenanceDetail.getMeasureDirectionCategory3Text());
                    newDetail.setMeasureDirection(aMoldMaintenanceDetail.getMeasureDirection());
                    newDetail.setTaskCategory1(aMoldMaintenanceDetail.getTaskCategory1());
                    newDetail.setTaskCategory1Text(aMoldMaintenanceDetail.getTaskCategory1Text());
                    newDetail.setTaskCategory2(aMoldMaintenanceDetail.getTaskCategory2());
                    newDetail.setTaskCategory2Text(aMoldMaintenanceDetail.getTaskCategory2Text());
                    newDetail.setTaskCategory3(aMoldMaintenanceDetail.getTaskCategory3());
                    newDetail.setTaskCategory3Text(aMoldMaintenanceDetail.getTaskCategory3Text());
                    newDetail.setTask(aMoldMaintenanceDetail.getTask());

                    newDetail.setCreateDate(aMoldMaintenanceDetail.getCreateDate());
                    newDetail.setCreateUserUuid(aMoldMaintenanceDetail.getCreateUserUuid());
                    newDetail.setUpdateDate(new Date());
                    newDetail.setUpdateUserUuid(aMoldMaintenanceDetail.getUpdateUserUuid());

                    if (null != oldMoldMaintenanceDetails && !oldMoldMaintenanceDetails.isEmpty()) {
                        entityManager.merge(newDetail);
                    } else {
                        entityManager.persist(newDetail);
                    }
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }

    /**
     * バッチで金型メンテナンス詳細ImageFileデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMoldMaintenanceDetailVo getExtMoldMaintenanceDetailImageFilesByBatch(String latestExecutedDate, String moldUuid) {
        TblMoldMaintenanceDetailVo resList = new TblMoldMaintenanceDetailVo();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblMoldMaintenanceDetailImageFile t join MstApiUser u on u.companyId = t.tblMoldMaintenanceDetail.tblMoldMaintenanceRemodeling.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and t.tblMoldMaintenanceDetail.tblMoldMaintenanceRemodeling.mstMold.uuid = :moldUuid ");
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
        List<TblMoldMaintenanceDetailImageFile> tmpList = query.getResultList();
        List<TblMoldMaintenanceDetailImageFileVo> resVos = new ArrayList<>();
        FileUtil fu = new FileUtil();
        for (TblMoldMaintenanceDetailImageFile aDetailImageFile : tmpList) {
            TblMoldMaintenanceDetailImageFileVo aVo = new TblMoldMaintenanceDetailImageFileVo();
            if (null != aDetailImageFile.getCreateDate()) {
                aVo.setCreateDate(aDetailImageFile.getCreateDate());
                aVo.setCreateDateStr(fu.getDateTimeFormatForStr(aDetailImageFile.getCreateDate()));
            }            
            aVo.setCreateDateUuid(aDetailImageFile.getCreateDateUuid());
            if (null != aDetailImageFile.getFileExtension()) {
                aVo.setFileExtension(aDetailImageFile.getFileExtension());
            }
            if (null != aDetailImageFile.getFileType()) {
                aVo.setFileType("" + aDetailImageFile.getFileType());
            }
            aVo.setFileUuid(aDetailImageFile.getFileUuid());
            aVo.setMaintenanceDetailId(aDetailImageFile.getTblMoldMaintenanceDetailImageFilePK().getMaintenanceDetailId());
            aVo.setSeq("" + aDetailImageFile.getTblMoldMaintenanceDetailImageFilePK().getSeq());
            if (null != aDetailImageFile.getRemarks()) {
                aVo.setRemarks(aDetailImageFile.getRemarks());
            }
            if (null != aDetailImageFile.getTakenDate()) {
                aVo.setTakenDate(aDetailImageFile.getTakenDate());
                aVo.setTakenDateStr(fu.getDateTimeFormatForStr(aDetailImageFile.getTakenDate()));                
            }
            if (null != aDetailImageFile.getTakenDateStz()) {
                aVo.setTakenDateStz(aDetailImageFile.getTakenDateStz());
                aVo.setTakenDateStzStr(fu.getDateTimeFormatForStr(aDetailImageFile.getTakenDateStz()));                
            }
            
            if (null != aDetailImageFile.getThumbnailFileUuid()) {
                aVo.setThumbnailFileUuid(aDetailImageFile.getThumbnailFileUuid());
            }
            resVos.add(aVo);
        }
        resList.setMoldMaintenanceDetailImageFileVos(resVos);
        return resList;
    }
    
    /**
     * バッチで金型メンテナンス詳細ImageFileデータを更新
     *
     * @param imageFileVos
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldMaintenanceDetailImageFilesByBatch(List<TblMoldMaintenanceDetailImageFileVo> imageFileVos) {
        BasicResponse response = new BasicResponse();
        if (imageFileVos != null && !imageFileVos.isEmpty()) {
            FileUtil fu = new FileUtil();
            for (TblMoldMaintenanceDetailImageFileVo aVo : imageFileVos) {
                List<TblMoldMaintenanceDetail> moldMaintenanceDetails = entityManager.createQuery("from TblMoldMaintenanceDetail t where t.id = :maintenanceDetailId ")
                        .setParameter("maintenanceDetailId", aVo.getMaintenanceDetailId())
                        .setMaxResults(1)
                        .getResultList();
                if (null != moldMaintenanceDetails && !moldMaintenanceDetails.isEmpty()) {
                    List<TblMoldMaintenanceDetailImageFile> oldImageFiles = entityManager.createQuery("SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.tblMoldMaintenanceDetailImageFilePK.maintenanceDetailId = :maintenanceDetailId and t.tblMoldMaintenanceDetailImageFilePK.seq = :seq ")
                            .setParameter("maintenanceDetailId", aVo.getMaintenanceDetailId())
                            .setParameter("seq", Integer.parseInt(aVo.getSeq()))
                            .setMaxResults(1)
                            .getResultList();

                    TblMoldMaintenanceDetailImageFile newImageFile;
                    if (null != oldImageFiles && !oldImageFiles.isEmpty()) {
                        newImageFile = oldImageFiles.get(0);
                    } else {
                        newImageFile = new TblMoldMaintenanceDetailImageFile();
                        TblMoldMaintenanceDetailImageFilePK pk = new TblMoldMaintenanceDetailImageFilePK();
                        pk.setMaintenanceDetailId(aVo.getMaintenanceDetailId());
                        pk.setSeq(Integer.parseInt(aVo.getSeq()));
                        newImageFile.setTblMoldMaintenanceDetailImageFilePK(pk);

                    }
                    newImageFile.setFileUuid(aVo.getFileUuid());
                    if (null != aVo.getFileExtension() && !aVo.getFileExtension().isEmpty()) {
                        newImageFile.setFileExtension(aVo.getFileExtension());
                    } else {
                        newImageFile.setFileExtension(null);
                    }
                    if (null != aVo.getFileType()) {
                        newImageFile.setFileType(Integer.parseInt(aVo.getFileType()));
                    } else {
                        newImageFile.setFileType(null);
                    }
                    newImageFile.setRemarks(aVo.getRemarks());
                    if (null != aVo.getTakenDateStr()) {
                        newImageFile.setTakenDate(fu.getDateTimeParseForDate(aVo.getTakenDateStr()));
                    } else {
                        newImageFile.setTakenDate(null);
                    }
                    if (null != aVo.getTakenDateStzStr()) {
                        newImageFile.setTakenDateStz(fu.getDateTimeParseForDate(aVo.getTakenDateStzStr()));
                    } else {
                        newImageFile.setTakenDateStz(null);
                    }
                    if (null != aVo.getThumbnailFileUuid() && !aVo.getThumbnailFileUuid().isEmpty()) {
                        newImageFile.setThumbnailFileUuid(aVo.getThumbnailFileUuid());
                    } else {
                        newImageFile.setThumbnailFileUuid(null);
                    }

                    newImageFile.setCreateDate(fu.getDateTimeParseForDate(aVo.getCreateDateStr()));
                    newImageFile.setCreateDateUuid(aVo.getCreateDateUuid());
                    newImageFile.setUpdateDate(new Date());
                    newImageFile.setUpdateUserUuid(aVo.getUpdateUserUuid());

                    if (null != oldImageFiles && !oldImageFiles.isEmpty()) {
                        entityManager.merge(newImageFile);
                    } else {
                        entityManager.persist(newImageFile);
                    }
                }
            }
        }
        response.setError(false);
        response.setErrorCode(ErrorMessages.E201_APPLICATION);
//            response.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_updated"));
        return response;
    }
}
