package com.kmcj.karte.resources.machine.maintenance.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.inspection.result.TblMachineInspectionResult;
import com.kmcj.karte.resources.machine.inspection.result.TblMachineInspectionResultPK;
import com.kmcj.karte.resources.machine.inspection.result.TblMachineInspectionResultVo;
import com.kmcj.karte.resources.machine.maintenance.recomend.TblMachineMaintenanceRecomend;
import com.kmcj.karte.resources.machine.maintenance.recomend.TblMachineMaintenanceRecomendList;
import com.kmcj.karte.resources.machine.maintenance.recomend.TblMachineMaintenanceRecomendService;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodeling;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodelingService;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodelingVo;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetail;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailImageFile;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailImageFilePK;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailImageFileVo;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailPK;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailVo;
import com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResult;
import com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResultPK;
import com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResultVo;
import com.kmcj.karte.resources.mold.issue.TblIssue;
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
 * @author jiangxs
 */
@Dependent
public class TblMachineMaintenanceDetailService {
    
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    @Inject
    private MstDictionaryService mstDictionaryService;
    @Inject
    private TblMachineMaintenanceRemodelingService tblMachineMaintenanceRemodelingService;
    @Inject
    private TblMachineMaintenanceRecomendService tblMachineMaintenanceRecomendService;
    @Inject
    private CnfSystemService cnfSystemService;
    
    /**
     * 設備メンテナンス終了入力
     * 再開 終了している設備メンテナンスのステータスをメンテナンス中に戻す。
     * @param machineId
     * @param loginUser
     * @return 
     */
    @Transactional
    public BasicResponse resumptionMachineMaintenance(String machineId, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        
        MstMachine mstMachine = entityManager.find(MstMachine.class, machineId);
        if(mstMachine != null){
             //外部データがチェック
             if (mstMachine.getCompanyId() != null && !"".equals(mstMachine.getCompanyId())) {
                basicResponse = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", mstMachine.getCompanyId(), loginUser);
                if (basicResponse.isError()) {
                    return basicResponse;
                }
            }
             
             if (null != mstMachine.getMainteStatus() && mstMachine.getMainteStatus() == CommonConstants.MAINTE_STATUS_NORMAL) {
                mstMachine.setMainteStatus(CommonConstants.MAINTE_STATUS_MAINTE); //メンテナンス中
                mstMachine.setUpdateDate(new Date());
                mstMachine.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.merge(mstMachine);

                TblMachineMaintenanceRemodelingVo tblMachineMaintenanceRemodelingVo = (TblMachineMaintenanceRemodelingVo) tblMachineMaintenanceRemodelingService.getMachinemainteOrRemodelDetail(machineId, CommonConstants.MAINTEORREMODEL_MAINTE, loginUser);
                String id = tblMachineMaintenanceRemodelingVo.getId();

                StringBuilder sql = new StringBuilder(" UPDATE TblMachineMaintenanceRemodeling t SET ");
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
                 if (StringUtils.isNotEmpty(tblMachineMaintenanceRemodelingVo.getIssueId())) {
                     TblIssue issue = entityManager.find(TblIssue.class, tblMachineMaintenanceRemodelingVo.getIssueId());
                     issue.setMeasureStatus(CommonConstants.ISSUE_MEASURE_STATUS_RESOLVING);
                     issue.setUpdateDate(new Date());
                     issue.setUpdateUserUuid(loginUser.getUserUuid());
                     entityManager.merge(issue);
                 }
                
            } else if(null != mstMachine.getMainteStatus() && mstMachine.getMainteStatus() == 2) {
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
     * 設備メンテナンス終了入力 
     * 選択された設備メンテナンスの開始をなかったことにするため、データベースからレコードを削除する。
     * @param maintenanceId
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse startCancelMachineMaintenance(String maintenanceId, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

        TblMachineMaintenanceRemodeling aMachineMaintenanceRemodeling = entityManager.find(TblMachineMaintenanceRemodeling.class, maintenanceId);
        MstMachine aMstMachine = null;
        Query machineQuery = entityManager.createNamedQuery("MstMachine.findByUuid");
        machineQuery.setParameter("uuid", aMachineMaintenanceRemodeling.getMachineUuid());
        List<MstMachine> machines = machineQuery.getResultList();
        if (null != machines && !machines.isEmpty()) {
            aMstMachine = machines.get(0);
        }
        if (null != aMstMachine) {
            //外部データがチェック
            if (aMstMachine.getCompanyId() != null && !"".equals(aMstMachine.getCompanyId())) {
                basicResponse = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", aMstMachine.getCompanyId(), loginUser);
                if (basicResponse.isError()) {
                    return basicResponse;
                }
            }
            
            
            if (null != aMstMachine.getMainteStatus() && aMstMachine.getMainteStatus() == CommonConstants.MAINTE_STATUS_NORMAL) {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_can_not_cancel"));
                return basicResponse;
            } else if (null != aMstMachine.getMainteStatus() && aMstMachine.getMainteStatus() == 2) {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_remodeling"));
                return basicResponse;
            } else {
                //設備マスタ 更新 状態をメンテナンス中から通常に戻す
                aMstMachine.setMainteStatus(CommonConstants.MAINTE_STATUS_NORMAL); //状態をメンテナンス中から通常に戻す
                aMstMachine.setUpdateDate(new Date());
                aMstMachine.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.merge(aMstMachine);

                //設備メンテナンス詳細 削除
                Query maintenanceDetailQuery = entityManager.createQuery("SELECT d from TblMachineMaintenanceDetail d where d.tblMachineMaintenanceDetailPK.maintenanceId = :maintenanceId ");
                maintenanceDetailQuery.setParameter("maintenanceId", maintenanceId);
                List<TblMachineMaintenanceDetail> details = maintenanceDetailQuery.getResultList();
                if (null != details && !details.isEmpty()) {
                    for (int i = 0; i < details.size(); i++) {
                        TblMachineMaintenanceDetail aDetail = details.get(i);

                        //設備点検結果 削除
                        Query delInspectionResultlQuery = entityManager.createNamedQuery("TblMachineInspectionResult.deleteByMaintenanceDetailId");
                        delInspectionResultlQuery.setParameter("maintenanceDetailId", aDetail.getId());
                        delInspectionResultlQuery.executeUpdate();

//                    entityManager.remove(aDetail); //cascade delete
                    }
                }

                //設備改造メンテナンス 削除
                entityManager.remove(aMachineMaintenanceRemodeling);
            }
        }

        return basicResponse;
    }
    
    
    /**
     * 設備メンテナンス終了入力 登録
     * @param machineMaintenanceRemodelingVo
     * @param type
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postMachineMaintenanceDetailes(TblMachineMaintenanceRemodelingVo machineMaintenanceRemodelingVo, int type, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

        
        MstMachine aMstMachine = entityManager.find(MstMachine.class, machineMaintenanceRemodelingVo.getMachineId());
        
        if (aMstMachine != null) {
            if (null != aMstMachine.getCompanyId() && !"".equals(aMstMachine.getCompanyId())) {
                basicResponse = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", aMstMachine.getCompanyId(), loginUser);
                if (basicResponse.isError()) {
                    // 外部管理されているため、編集不可
                    return basicResponse;
                }
            }
        } else {
            //該当設備が削除された可能性が発生
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
            return basicResponse;
        }
        // メンテナンスの一次保存機能 2018/09/13 -S
        if (0 == machineMaintenanceRemodelingVo.getTemporarilySaved()) {
            //設備マスタ 更新 「通常」状態を解除
            aMstMachine.setMainteStatus(CommonConstants.MAINTE_STATUS_NORMAL);
            // 4.2 対応　BY LiuYoudong S
            CnfSystem cnfSystem = cnfSystemService.findByKey("system", "business_start_time");
            aMstMachine.setLastMainteDate(DateFormat.strToDate(DateFormat.getBusinessDate(DateFormat.dateToStr(new Date(), DateFormat.DATETIME_FORMAT), cnfSystem))); // 最終メンテナンス日
            if (machineMaintenanceRemodelingVo.isResetAfterMainteTotalProducingTimeHourFlag()) {
                aMstMachine.setAfterMainteTotalProducingTimeHour(BigDecimal.ZERO); // メンテナンス後生産時間
            }
            if (machineMaintenanceRemodelingVo.isResetAfterMainteTotalShotCountFlag()) {
                aMstMachine.setAfterMainteTotalShotCount(0); // メンテナンス後ショット数
            }
            // 4.2 対応　BY LiuYoudong E
            aMstMachine.setUpdateDate(new Date());
            aMstMachine.setUpdateUserUuid(loginUser.getUserUuid());
            entityManager.merge(aMstMachine);
        }
        // メンテナンスの一次保存機能 2018/09/13 -E
        
        //設備改造メンテナンス 更新 メンテナンス分類、報告事項、終了日時をセット
        TblMachineMaintenanceRemodeling machineMaintenanceRemodeling = entityManager.find(TblMachineMaintenanceRemodeling.class, machineMaintenanceRemodelingVo.getId());
        if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {
            machineMaintenanceRemodeling.setMainteType(machineMaintenanceRemodelingVo.getMainteType());
        } else if (type == CommonConstants.MAINTEORREMODEL_REMODEL) {
            machineMaintenanceRemodeling.setRemodelingType(machineMaintenanceRemodelingVo.getRemodelingType());
        }
        
        machineMaintenanceRemodeling.setWorkingTimeMinutes(machineMaintenanceRemodelingVo.getWorkingTimeMinutes());//所要時間 20170901 Apeng add
        machineMaintenanceRemodeling.setReport(machineMaintenanceRemodelingVo.getReport());
        machineMaintenanceRemodeling.setTblDirectionId(machineMaintenanceRemodelingVo.getTblDirectionId());
        //システム設定により手配・工事テーブル参照しない場合はコードを直接保存
        if (machineMaintenanceRemodelingVo.getTblDirectionId() == null && machineMaintenanceRemodelingVo.getTblDirectionCode() != null) {
            machineMaintenanceRemodeling.setTblDirectionCode(machineMaintenanceRemodelingVo.getTblDirectionCode());
        }
        machineMaintenanceRemodeling.setUpdateUserUuid(loginUser.getUserUuid());
        if (machineMaintenanceRemodelingVo.getStartDatetimeStr()!= null) {
            machineMaintenanceRemodeling.setStartDatetime(DateFormat.strToDatetime(machineMaintenanceRemodelingVo.getStartDatetimeStr()));
        } 
        if (machineMaintenanceRemodelingVo.getStartDatetimeStr()!= null) {
            machineMaintenanceRemodeling.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), machineMaintenanceRemodeling.getStartDatetime()));
        }
        // メンテナンスの一次保存機能 2018/09/13 -S
        if (0 == machineMaintenanceRemodelingVo.getTemporarilySaved()) {

            if (machineMaintenanceRemodelingVo.getEndDatetimeStr()!= null) {
                machineMaintenanceRemodeling.setEndDatetime(DateFormat.strToDatetime(machineMaintenanceRemodelingVo.getEndDatetimeStr()));
            }
            if (machineMaintenanceRemodeling.getEndDatetime() != null) {
                machineMaintenanceRemodeling.setEndDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), machineMaintenanceRemodeling.getEndDatetime()));
            }
//        machineMaintenanceRemodeling.setEndDatetime(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
//        machineMaintenanceRemodeling.setEndDatetimeStz(new Date());
        }
        // メンテナンスの一次保存機能 2018/09/13 -E
        machineMaintenanceRemodeling.setUpdateDate(new Date());
        machineMaintenanceRemodeling.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(machineMaintenanceRemodeling);
        
        if (null != machineMaintenanceRemodelingVo.getIssueId() && !"".equals(machineMaintenanceRemodelingVo.getIssueId().trim())) {
            TblIssue tblIssue = entityManager.find(TblIssue.class, machineMaintenanceRemodelingVo.getIssueId());
            if (tblIssue != null) {
                try {
                    String measureStatus = machineMaintenanceRemodelingVo.getMeasureStatus();
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
            //設備メンテナンス詳細 追加
            detailVos = machineMaintenanceRemodelingVo.getMachineMaintenanceDetailVo();

        } else if (type == CommonConstants.MAINTEORREMODEL_REMODEL) {
            detailVos = machineMaintenanceRemodelingVo.getMachineRemodelingDetailVo();
        }

        if (null != detailVos && !detailVos.isEmpty()) {
            if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {                
                List<TblMachineMaintenanceDetailVo> newMaintenanceDetailVo = new ArrayList<>();
                for(int i=0; i<detailVos.size();i++){
                    TblMachineMaintenanceDetailVo machineMaintenanceDetailVo = (TblMachineMaintenanceDetailVo) detailVos.get(i);
                    if(!"1".equals(machineMaintenanceDetailVo.getDeleteFlag())){
                        newMaintenanceDetailVo.add(machineMaintenanceDetailVo);
                    }
                }
                
                Query query = entityManager.createQuery("DELETE FROM TblMachineMaintenanceDetail t WHERE t.tblMachineMaintenanceDetailPK.maintenanceId = :maintenanceId");
                query.setParameter("maintenanceId", machineMaintenanceRemodeling.getId());
                query.executeUpdate();
                
                for (int i = 0; i < newMaintenanceDetailVo.size();  i++) {
                    TblMachineMaintenanceDetailVo machineMaintenanceDetailVo = (TblMachineMaintenanceDetailVo) newMaintenanceDetailVo.get(i);

                    TblMachineMaintenanceDetail machineMaintenanceDetail = new TblMachineMaintenanceDetail();
                    if (null != machineMaintenanceDetailVo.getMainteReasonCategory1() && !machineMaintenanceDetailVo.getMainteReasonCategory1().equals("")) {
                        machineMaintenanceDetail.setMainteReasonCategory1(Integer.parseInt(machineMaintenanceDetailVo.getMainteReasonCategory1()));
                        machineMaintenanceDetail.setMainteReasonCategory1Text(machineMaintenanceDetailVo.getMainteReasonCategory1Text());

                        if (null != machineMaintenanceDetailVo.getMainteReasonCategory2() && !machineMaintenanceDetailVo.getMainteReasonCategory2().equals("")) {
                            machineMaintenanceDetail.setMainteReasonCategory2(Integer.parseInt(machineMaintenanceDetailVo.getMainteReasonCategory2()));
                            machineMaintenanceDetail.setMainteReasonCategory2Text(machineMaintenanceDetailVo.getMainteReasonCategory2Text());

                            if (null != machineMaintenanceDetailVo.getMainteReasonCategory3() && !machineMaintenanceDetailVo.getMainteReasonCategory3().equals("")) {
                                machineMaintenanceDetail.setMainteReasonCategory3(Integer.parseInt(machineMaintenanceDetailVo.getMainteReasonCategory3()));
                                machineMaintenanceDetail.setMainteReasonCategory3Text(machineMaintenanceDetailVo.getMainteReasonCategory3Text());
                            } else {
                                machineMaintenanceDetail.setMainteReasonCategory3(null);
                                machineMaintenanceDetail.setMainteReasonCategory3Text(null);
                            }
                        } else {
                            machineMaintenanceDetail.setMainteReasonCategory2(null);
                            machineMaintenanceDetail.setMainteReasonCategory2Text(null);
                            machineMaintenanceDetail.setMainteReasonCategory3(null);
                            machineMaintenanceDetail.setMainteReasonCategory3Text(null);
                        }
                    } else {
                        machineMaintenanceDetail.setMainteReasonCategory1(null);
                        machineMaintenanceDetail.setMainteReasonCategory1Text(null);
                        machineMaintenanceDetail.setMainteReasonCategory2(null);
                        machineMaintenanceDetail.setMainteReasonCategory2Text(null);
                        machineMaintenanceDetail.setMainteReasonCategory3(null);
                        machineMaintenanceDetail.setMainteReasonCategory3Text(null);
                    }
                    machineMaintenanceDetail.setManiteReason(machineMaintenanceDetailVo.getManiteReason());

                    if (null != machineMaintenanceDetailVo.getTaskCategory1() && !machineMaintenanceDetailVo.getTaskCategory1().equals("")) {
                        machineMaintenanceDetail.setTaskCategory1(Integer.parseInt(machineMaintenanceDetailVo.getTaskCategory1()));
                        machineMaintenanceDetail.setTaskCategory1Text(machineMaintenanceDetailVo.getTaskCategory1Text());
                        if (null != machineMaintenanceDetailVo.getTaskCategory2() && !machineMaintenanceDetailVo.getTaskCategory2().equals("")) {
                            machineMaintenanceDetail.setTaskCategory2(Integer.parseInt(machineMaintenanceDetailVo.getTaskCategory2()));
                            machineMaintenanceDetail.setTaskCategory2Text(machineMaintenanceDetailVo.getTaskCategory2Text());
                            if (null != machineMaintenanceDetailVo.getTaskCategory3() && !machineMaintenanceDetailVo.getTaskCategory3().equals("")) {
                                machineMaintenanceDetail.setTaskCategory3(Integer.parseInt(machineMaintenanceDetailVo.getTaskCategory3()));
                                machineMaintenanceDetail.setTaskCategory3Text(machineMaintenanceDetailVo.getTaskCategory3Text());
                            } else {
                                machineMaintenanceDetail.setTaskCategory3(null);
                                machineMaintenanceDetail.setTaskCategory3Text(null);
                            }
                        } else {
                            machineMaintenanceDetail.setTaskCategory2(null);
                            machineMaintenanceDetail.setTaskCategory2Text(null);
                            machineMaintenanceDetail.setTaskCategory3(null);
                            machineMaintenanceDetail.setTaskCategory3Text(null);
                        }
                    } else {
                        machineMaintenanceDetail.setTaskCategory1(null);
                        machineMaintenanceDetail.setTaskCategory1Text(null);
                        machineMaintenanceDetail.setTaskCategory2(null);
                        machineMaintenanceDetail.setTaskCategory2Text(null);
                        machineMaintenanceDetail.setTaskCategory3(null);
                        machineMaintenanceDetail.setTaskCategory3Text(null);
                    }
                    machineMaintenanceDetail.setTask(machineMaintenanceDetailVo.getTask());

                    if (null != machineMaintenanceDetailVo.getMeasureDirectionCategory1() && !machineMaintenanceDetailVo.getMeasureDirectionCategory1().equals("")) {
                        machineMaintenanceDetail.setMeasureDirectionCategory1(Integer.parseInt(machineMaintenanceDetailVo.getMeasureDirectionCategory1()));
                        machineMaintenanceDetail.setMeasureDirectionCategory1Text(machineMaintenanceDetailVo.getMeasureDirectionCategory1Text());
                        if (null != machineMaintenanceDetailVo.getMeasureDirectionCategory2() && !machineMaintenanceDetailVo.getMeasureDirectionCategory2().equals("")) {
                            machineMaintenanceDetail.setMeasureDirectionCategory2(Integer.parseInt(machineMaintenanceDetailVo.getMeasureDirectionCategory2()));
                            machineMaintenanceDetail.setMeasureDirectionCategory2Text(machineMaintenanceDetailVo.getMeasureDirectionCategory2Text());
                            if (null != machineMaintenanceDetailVo.getMeasureDirectionCategory3() && !machineMaintenanceDetailVo.getMeasureDirectionCategory3().equals("")) {
                                machineMaintenanceDetail.setMeasureDirectionCategory3(Integer.parseInt(machineMaintenanceDetailVo.getMeasureDirectionCategory3()));
                                machineMaintenanceDetail.setMeasureDirectionCategory3Text(machineMaintenanceDetailVo.getMeasureDirectionCategory3Text());
                            } else {
                                machineMaintenanceDetail.setMeasureDirectionCategory3(null);
                                machineMaintenanceDetail.setMeasureDirectionCategory3Text(null);
                            }
                        } else {
                            machineMaintenanceDetail.setMeasureDirectionCategory2(null);
                            machineMaintenanceDetail.setMeasureDirectionCategory2Text(null);
                            machineMaintenanceDetail.setMeasureDirectionCategory3(null);
                            machineMaintenanceDetail.setMeasureDirectionCategory3Text(null);
                        }
                    } else {
                        machineMaintenanceDetail.setMeasureDirectionCategory1(null);
                        machineMaintenanceDetail.setMeasureDirectionCategory1Text(null);
                        machineMaintenanceDetail.setMeasureDirectionCategory2(null);
                        machineMaintenanceDetail.setMeasureDirectionCategory2Text(null);
                        machineMaintenanceDetail.setMeasureDirectionCategory3(null);
                        machineMaintenanceDetail.setMeasureDirectionCategory3Text(null);
                    }
                    machineMaintenanceDetail.setMeasureDirection(machineMaintenanceDetailVo.getMeasureDirection());

                    TblMachineMaintenanceDetailPK pk = new TblMachineMaintenanceDetailPK();
                    pk.setMaintenanceId(machineMaintenanceRemodeling.getId());
                    pk.setSeq((i+1));
                    machineMaintenanceDetail.setTblMachineMaintenanceDetailPK(pk);
                    machineMaintenanceDetail.setId(IDGenerator.generate());
                    machineMaintenanceDetail.setCreateDate(new Date());
                    machineMaintenanceDetail.setCreateUserUuid(loginUser.getUserUuid());
                    machineMaintenanceDetail.setUpdateDate(new Date());
                    machineMaintenanceDetail.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.persist(machineMaintenanceDetail);

                    //設備点検結果 追加
                    List<TblMachineInspectionResultVo> machineInspectionResultVos = machineMaintenanceRemodelingVo.getMachineInspectionResultVo();
                    if (null == machineInspectionResultVos || machineInspectionResultVos.isEmpty()) {
                        machineInspectionResultVos = machineMaintenanceDetailVo.getMachineInspectionResultVos();
                    }
                    if (null != machineInspectionResultVos && !machineInspectionResultVos.isEmpty()) {
                        List<TblMachineInspectionResultVo> newInspectionResultVo = new ArrayList<>();
                        Query delInspectionResultQuery = entityManager.createNamedQuery("TblMachineInspectionResult.deleteByMaintenanceDetailId");
                        if (null != machineMaintenanceDetailVo.getId() && null != machineMaintenanceRemodelingVo.getMachineInspectionResultVo() && !machineMaintenanceRemodelingVo.getMachineInspectionResultVo().isEmpty()) {
                            delInspectionResultQuery.setParameter("maintenanceDetailId", machineMaintenanceDetailVo.getId());

                            for (TblMachineInspectionResultVo aMachineInspectionResultVo : machineInspectionResultVos) {
                                if (aMachineInspectionResultVo.getMaintenanceDetailId().equals(machineMaintenanceDetailVo.getId())) {
                                    newInspectionResultVo.add(aMachineInspectionResultVo);
                                }
                            }
                        } else {
                            delInspectionResultQuery.setParameter("maintenanceDetailId", machineMaintenanceDetail.getId());
                            newInspectionResultVo = machineInspectionResultVos;
                        }
                        delInspectionResultQuery.executeUpdate();
                        for (TblMachineInspectionResultVo aMachineInspectionResultVo : newInspectionResultVo) {
                            TblMachineInspectionResult aResult = new TblMachineInspectionResult();
                            aResult.setUpdateDate(new Date());
                            aResult.setUpdateUserUuid(loginUser.getUserUuid());
                            aResult.setInspectionResult(aMachineInspectionResultVo.getInspectionResult());
                            aResult.setInspectionResultText(aMachineInspectionResultVo.getInspectionResultText());

                            aResult.setId(IDGenerator.generate());
                            TblMachineInspectionResultPK resultPK = new TblMachineInspectionResultPK();
                            resultPK.setInspectionItemId(aMachineInspectionResultVo.getMstMachineInspectionItemId());
                            resultPK.setMaintenanceDetailId(machineMaintenanceDetail.getId());
                            resultPK.setSeq(aMachineInspectionResultVo.getSeq());
                            aResult.setTblMachineInspectionResultPK(resultPK);

                            aResult.setCreateDate(new Date());
                            aResult.setCreateUserUuid(loginUser.getUserUuid());
                            entityManager.persist(aResult);
                        }
                    }

                    
                    //設備ImageFile 追加
                    List<TblMachineMaintenanceDetailImageFileVo> machineMaintenanceDetailImageFileVos = machineMaintenanceRemodelingVo.getImageFileVos();
                    if (null == machineMaintenanceDetailImageFileVos || machineMaintenanceDetailImageFileVos.isEmpty()) {
                        machineMaintenanceDetailImageFileVos = machineMaintenanceDetailVo.getTblMachineMaintenanceDetailImageFileVos();
                    }
                    if (null != machineMaintenanceDetailImageFileVos && !machineMaintenanceDetailImageFileVos.isEmpty()) {
                        List<TblMachineMaintenanceDetailImageFileVo> newMachineMaintenanceDetailImageFileVos = new ArrayList<>();
                        Query delInspectionResultQuery = entityManager.createNamedQuery("TblMachineMaintenanceDetailImageFile.deleteByMaintenanceDetailId");
                        if (null != machineMaintenanceDetailVo.getId() && null != machineMaintenanceRemodelingVo.getImageFileVos() && !machineMaintenanceRemodelingVo.getImageFileVos().isEmpty()) {
                            delInspectionResultQuery.setParameter("maintenanceDetailId", machineMaintenanceDetailVo.getId());

                            for (TblMachineMaintenanceDetailImageFileVo aMachineMaintenanceDetailImageFileVo : machineMaintenanceDetailImageFileVos) {
                                if (aMachineMaintenanceDetailImageFileVo.getMaintenanceDetailId().equals(machineMaintenanceDetailVo.getId())) {
                                    newMachineMaintenanceDetailImageFileVos.add(aMachineMaintenanceDetailImageFileVo);
                                }
                            }
                        } else {
                            delInspectionResultQuery.setParameter("maintenanceDetailId", machineMaintenanceDetail.getId());

                            newMachineMaintenanceDetailImageFileVos = machineMaintenanceDetailImageFileVos;
                        }
                        delInspectionResultQuery.executeUpdate();
                        for (TblMachineMaintenanceDetailImageFileVo aImageFileVo : newMachineMaintenanceDetailImageFileVos) {
                            TblMachineMaintenanceDetailImageFile aImageFile = new TblMachineMaintenanceDetailImageFile();
                            aImageFile.setUpdateDate(new Date());
                            aImageFile.setUpdateUserUuid(loginUser.getUserUuid());
                            aImageFile.setCreateDate(new Date());
                            aImageFile.setCreateDateUuid(loginUser.getUserUuid());

                            TblMachineMaintenanceDetailImageFilePK aPK = new TblMachineMaintenanceDetailImageFilePK();
                            aPK.setMaintenanceDetailId(machineMaintenanceDetail.getId());                            
                            aPK.setSeq(Integer.parseInt(aImageFileVo.getSeq()));
                            aImageFile.setTblMachineMaintenanceDetailImageFilePK(aPK);

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
                            if (null != aImageFileVo.getTakenDateStr() && !aImageFileVo.getTakenDateStr().isEmpty()) {
                                aImageFile.setTakenDate(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()));
                            } else {
                                aImageFile.setTakenDate(null);
                            }
                            if (null != aImageFileVo.getTakenDateStzStr() && !aImageFileVo.getTakenDateStzStr().isEmpty()) {
                                aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStzStr()));
                            } else {
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
                List<TblMachineRemodelingDetailVo> newRemodelingDetailVo = new ArrayList<>();
                for(int i=0; i<detailVos.size();i++){
                    TblMachineRemodelingDetailVo machineRemodelingDetailVo = (TblMachineRemodelingDetailVo) detailVos.get(i);
                    if(!"1".equals(machineRemodelingDetailVo.getDeleteFlag())){
                        newRemodelingDetailVo.add(machineRemodelingDetailVo);
                    }
                }
                
                Query query = entityManager.createQuery("DELETE FROM TblMachineRemodelingDetail t WHERE t.tblMachineRemodelingDetailPK.maintenanceId = :maintenanceId");
                query.setParameter("maintenanceId", machineMaintenanceRemodeling.getId());
                query.executeUpdate();
                
                for (int i = 0; i < newRemodelingDetailVo.size(); i++) {
                    TblMachineRemodelingDetailVo machineRemodelingDetailVo = (TblMachineRemodelingDetailVo) newRemodelingDetailVo.get(i);

                    TblMachineRemodelingDetail machineRemodelingDetail = new TblMachineRemodelingDetail();
                    if (null != machineRemodelingDetailVo.getRemodelReasonCategory1() && !machineRemodelingDetailVo.getRemodelReasonCategory1().equals("")) {
                        machineRemodelingDetail.setRemodelReasonCategory1(Integer.parseInt(machineRemodelingDetailVo.getRemodelReasonCategory1()));
                        machineRemodelingDetail.setRemodelReasonCategory1Text(machineRemodelingDetailVo.getRemodelReasonCategory1Text());

                        if (null != machineRemodelingDetailVo.getRemodelReasonCategory2() && !machineRemodelingDetailVo.getRemodelReasonCategory2().equals("")) {
                            machineRemodelingDetail.setRemodelReasonCategory2(Integer.parseInt(machineRemodelingDetailVo.getRemodelReasonCategory2()));
                            machineRemodelingDetail.setRemodelReasonCategory2Text(machineRemodelingDetailVo.getRemodelReasonCategory2Text());

                            if (null != machineRemodelingDetailVo.getRemodelReasonCategory3() && !machineRemodelingDetailVo.getRemodelReasonCategory3().equals("")) {
                                machineRemodelingDetail.setRemodelReasonCategory3(Integer.parseInt(machineRemodelingDetailVo.getRemodelReasonCategory3()));
                                machineRemodelingDetail.setRemodelReasonCategory3Text(machineRemodelingDetailVo.getRemodelReasonCategory3Text());
                            } else {
                                machineRemodelingDetail.setRemodelReasonCategory3(null);
                                machineRemodelingDetail.setRemodelReasonCategory3Text(null);
                            }
                        } else {
                            machineRemodelingDetail.setRemodelReasonCategory2(null);
                            machineRemodelingDetail.setRemodelReasonCategory2Text(null);
                            machineRemodelingDetail.setRemodelReasonCategory3(null);
                            machineRemodelingDetail.setRemodelReasonCategory3Text(null);
                        }
                    } else {
                        machineRemodelingDetail.setRemodelReasonCategory1(null);
                        machineRemodelingDetail.setRemodelReasonCategory1Text(null);
                        machineRemodelingDetail.setRemodelReasonCategory2(null);
                        machineRemodelingDetail.setRemodelReasonCategory2Text(null);
                        machineRemodelingDetail.setRemodelReasonCategory3(null);
                        machineRemodelingDetail.setRemodelReasonCategory3Text(null);
                    }
                    machineRemodelingDetail.setRemodelReason(machineRemodelingDetailVo.getRemodelReason());

                    if (null != machineRemodelingDetailVo.getTaskCategory1() && !machineRemodelingDetailVo.getTaskCategory1().equals("")) {
                        machineRemodelingDetail.setTaskCategory1(Integer.parseInt(machineRemodelingDetailVo.getTaskCategory1()));
                        machineRemodelingDetail.setTaskCategory1Text(machineRemodelingDetailVo.getTaskCategory1Text());
                        if (null != machineRemodelingDetailVo.getTaskCategory2() && !machineRemodelingDetailVo.getTaskCategory2().equals("")) {
                            machineRemodelingDetail.setTaskCategory2(Integer.parseInt(machineRemodelingDetailVo.getTaskCategory2()));
                            machineRemodelingDetail.setTaskCategory2Text(machineRemodelingDetailVo.getTaskCategory2Text());
                            if (null != machineRemodelingDetailVo.getTaskCategory3() && !machineRemodelingDetailVo.getTaskCategory3().equals("")) {
                                machineRemodelingDetail.setTaskCategory3(Integer.parseInt(machineRemodelingDetailVo.getTaskCategory3()));
                                machineRemodelingDetail.setTaskCategory3Text(machineRemodelingDetailVo.getTaskCategory3Text());
                            } else {
                                machineRemodelingDetail.setTaskCategory3(null);
                                machineRemodelingDetail.setTaskCategory3Text(null);
                            }
                        } else {
                            machineRemodelingDetail.setTaskCategory2(null);
                            machineRemodelingDetail.setTaskCategory2Text(null);
                            machineRemodelingDetail.setTaskCategory3(null);
                            machineRemodelingDetail.setTaskCategory3Text(null);
                        }
                    } else {
                        machineRemodelingDetail.setTaskCategory1(null);
                        machineRemodelingDetail.setTaskCategory1Text(null);
                        machineRemodelingDetail.setTaskCategory2(null);
                        machineRemodelingDetail.setTaskCategory2Text(null);
                        machineRemodelingDetail.setTaskCategory3(null);
                        machineRemodelingDetail.setTaskCategory3Text(null);
                    }
                    machineRemodelingDetail.setTask(machineRemodelingDetailVo.getTask());

                    if (null != machineRemodelingDetailVo.getRemodelDirectionCategory1() && !machineRemodelingDetailVo.getRemodelDirectionCategory1().equals("")) {
                        machineRemodelingDetail.setRemodelDirectionCategory1(Integer.parseInt(machineRemodelingDetailVo.getRemodelDirectionCategory1()));
                        machineRemodelingDetail.setRemodelDirectionCategory1Text(machineRemodelingDetailVo.getRemodelDirectionCategory1Text());
                        if (null != machineRemodelingDetailVo.getRemodelDirectionCategory2() && !machineRemodelingDetailVo.getRemodelDirectionCategory2().equals("")) {
                            machineRemodelingDetail.setRemodelDirectionCategory2(Integer.parseInt(machineRemodelingDetailVo.getRemodelDirectionCategory2()));
                            machineRemodelingDetail.setRemodelDirectionCategory2Text(machineRemodelingDetailVo.getRemodelDirectionCategory2Text());
                            if (null != machineRemodelingDetailVo.getRemodelDirectionCategory3() && !machineRemodelingDetailVo.getRemodelDirectionCategory3().equals("")) {
                                machineRemodelingDetail.setRemodelDirectionCategory3(Integer.parseInt(machineRemodelingDetailVo.getRemodelDirectionCategory3()));
                                machineRemodelingDetail.setRemodelDirectionCategory3Text(machineRemodelingDetailVo.getRemodelDirectionCategory3Text());
                            } else {
                                machineRemodelingDetail.setRemodelDirectionCategory3(null);
                                machineRemodelingDetail.setRemodelDirectionCategory3Text(null);
                            }
                        } else {
                            machineRemodelingDetail.setRemodelDirectionCategory2(null);
                            machineRemodelingDetail.setRemodelDirectionCategory2Text(null);
                            machineRemodelingDetail.setRemodelDirectionCategory3(null);
                            machineRemodelingDetail.setRemodelDirectionCategory3Text(null);
                        }
                    } else {
                        machineRemodelingDetail.setRemodelDirectionCategory1(null);
                        machineRemodelingDetail.setRemodelDirectionCategory1Text(null);
                        machineRemodelingDetail.setRemodelDirectionCategory2(null);
                        machineRemodelingDetail.setRemodelDirectionCategory2Text(null);
                        machineRemodelingDetail.setRemodelDirectionCategory3(null);
                        machineRemodelingDetail.setRemodelDirectionCategory3Text(null);
                    }
                    machineRemodelingDetail.setRemodelDirection(machineRemodelingDetailVo.getRemodelDirection());

                    TblMachineRemodelingDetailPK pk = new TblMachineRemodelingDetailPK();
                    pk.setMaintenanceId(machineMaintenanceRemodeling.getId());
                    pk.setSeq((i + 1));
                    machineRemodelingDetail.setTblMachineRemodelingDetailPK(pk);
                    machineRemodelingDetail.setId(IDGenerator.generate());
                    machineRemodelingDetail.setCreateDate(new Date());
                    machineRemodelingDetail.setCreateUserUuid(loginUser.getUserUuid());
                    machineRemodelingDetail.setUpdateDate(new Date());
                    machineRemodelingDetail.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.persist(machineRemodelingDetail);

                    //設備点検結果 追加
                    List<TblMachineRemodelingInspectionResultVo> machineRemodelingInspectionResultVos = machineMaintenanceRemodelingVo.getRemodelingInspectionResultVo();
                    if (null == machineRemodelingInspectionResultVos || machineRemodelingInspectionResultVos.isEmpty()){
                        machineRemodelingInspectionResultVos = machineRemodelingDetailVo.getMachineRemodelingInspectionResultVo();
                    }
                    if (null != machineRemodelingInspectionResultVos && !machineRemodelingInspectionResultVos.isEmpty()) {
                        List<TblMachineRemodelingInspectionResultVo> newRemodelingInspectionResultVo = new ArrayList<>();
                        Query delInspectionResultQuery = entityManager.createQuery("DELETE FROM TblMachineRemodelingInspectionResult t WHERE t.tblMachineRemodelingInspectionResultPK.remodelingDetailId = :remodelingDetailId");
                        if (null != machineRemodelingDetailVo.getId()&& null != machineMaintenanceRemodelingVo.getRemodelingInspectionResultVo() && !machineMaintenanceRemodelingVo.getRemodelingInspectionResultVo().isEmpty()) {
                            delInspectionResultQuery.setParameter("remodelingDetailId", machineRemodelingDetailVo.getId());

                            for (TblMachineRemodelingInspectionResultVo aMachineRemodelingInspectionResultVo : machineRemodelingInspectionResultVos) {
                                if (aMachineRemodelingInspectionResultVo.getRemodelingDetailId().equals(machineRemodelingDetailVo.getId())) {
                                    newRemodelingInspectionResultVo.add(aMachineRemodelingInspectionResultVo);
                                }
                            }
                        } else {
                            delInspectionResultQuery.setParameter("remodelingDetailId", machineRemodelingDetail.getId());
                            newRemodelingInspectionResultVo = machineRemodelingInspectionResultVos;
                        }
                        delInspectionResultQuery.executeUpdate();

                        for (TblMachineRemodelingInspectionResultVo aMachineRemodelingInspectionResultVo : newRemodelingInspectionResultVo) {
                            TblMachineRemodelingInspectionResult aResult = new TblMachineRemodelingInspectionResult();
                            aResult.setUpdateDate(new Date());
                            aResult.setUpdateUserUuid(loginUser.getUserUuid());
                            aResult.setInspectionResult(aMachineRemodelingInspectionResultVo.getInspectionResult());
                            aResult.setInspectionResultText(aMachineRemodelingInspectionResultVo.getInspectionResultText());

                            aResult.setId(IDGenerator.generate());
                            TblMachineRemodelingInspectionResultPK resultPK = new TblMachineRemodelingInspectionResultPK();
                            resultPK.setInspectionItemId(aMachineRemodelingInspectionResultVo.getMstMachineInspectionItemId());
                            resultPK.setRemodelingDetailId(machineRemodelingDetail.getId());
                            resultPK.setSeq(aMachineRemodelingInspectionResultVo.getSeq());
                            aResult.setTblMachineRemodelingInspectionResultPK(resultPK);

                            aResult.setCreateDate(new Date());
                            aResult.setCreateUserUuid(loginUser.getUserUuid());
                            entityManager.persist(aResult);
                        }
                    }
                    
                    
                    //設備ImageFile 追加
                    List<TblMachineRemodelingDetailImageFileVo> machineRemodelingDetailImageFileVos = machineMaintenanceRemodelingVo.getRimageFileVos();
                    if (null == machineRemodelingDetailImageFileVos || machineRemodelingDetailImageFileVos.isEmpty()){
                        machineRemodelingDetailImageFileVos = machineRemodelingDetailVo.getMachineRemodelingDetailImageFileVos();
                    }
                    if (null != (machineRemodelingDetailImageFileVos = machineMaintenanceRemodelingVo.getRimageFileVos()) && !machineRemodelingDetailImageFileVos.isEmpty()) {
                        List<TblMachineRemodelingDetailImageFileVo> newMachineRemodelingDetailImageFileVos = new ArrayList<>();
                        Query delImageFileQuery = entityManager.createNamedQuery("TblMachineRemodelingDetailImageFile.deleteByRemodelingDetailId");
                        if (null != machineRemodelingDetailVo.getId() && null != machineMaintenanceRemodelingVo.getRimageFileVos() && !machineMaintenanceRemodelingVo.getRimageFileVos().isEmpty()) {
                            delImageFileQuery.setParameter("remodelingDetailId", machineRemodelingDetailVo.getId());
                            
                            for (TblMachineRemodelingDetailImageFileVo aMachineRemodelingDetailImageFileVo : machineRemodelingDetailImageFileVos) {
                                if (aMachineRemodelingDetailImageFileVo.getRemodelingDetailId().equals(machineRemodelingDetailVo.getId())) {
                                    newMachineRemodelingDetailImageFileVos.add(aMachineRemodelingDetailImageFileVo);
                                }
                            }
                        } else {
                            delImageFileQuery.setParameter("remodelingDetailId", machineRemodelingDetail.getId());
                            newMachineRemodelingDetailImageFileVos = machineRemodelingDetailImageFileVos;
                        }
                        delImageFileQuery.executeUpdate();

                        for (TblMachineRemodelingDetailImageFileVo aImageFileVo : newMachineRemodelingDetailImageFileVos) {
                            TblMachineRemodelingDetailImageFile aImageFile = new TblMachineRemodelingDetailImageFile();
                            aImageFile.setUpdateDate(new Date());
                            aImageFile.setUpdateUserUuid(loginUser.getUserUuid());
                            aImageFile.setCreateDate(new Date());
                            aImageFile.setCreateDateUuid(loginUser.getUserUuid());

                            TblMachineRemodelingDetailImageFilePK aPK = new TblMachineRemodelingDetailImageFilePK();
                            aPK.setRemodelingDetailId(machineRemodelingDetail.getId());
                            aPK.setSeq(Integer.parseInt(aImageFileVo.getSeq()));
                            aImageFile.setTblMachineRemodelingDetailImageFilePK(aPK);

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
                            if (null != aImageFileVo.getTakenDateStr() && !aImageFileVo.getTakenDateStr().isEmpty()) {
                                aImageFile.setTakenDate(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()));
                            } else {
                                aImageFile.setTakenDate(null);
                            }
                            if (null != aImageFileVo.getTakenDateStzStr() && !aImageFileVo.getTakenDateStzStr().isEmpty()) {
                                aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStzStr()));
                            } else {
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
        
        // Iteration4.2 設備メンテナンス候補更新 Start
		// メンテナンスの一次保存機能 2018/09/13 -S
        if (0 == machineMaintenanceRemodelingVo.getTemporarilySaved() && aMstMachine.getUuid() != null && !"".equals(aMstMachine.getUuid())) {
            TblMachineMaintenanceRecomendList tblMachineMaintenanceRecomendList  = tblMachineMaintenanceRecomendService.getMachineMaintenanceRecommendByUuid(aMstMachine.getUuid());
            if (tblMachineMaintenanceRecomendList != null && !tblMachineMaintenanceRecomendList.getTblMachineMaintenanceRecomendList().isEmpty()) {
                for (TblMachineMaintenanceRecomend tblMachineMaintenanceRecomend : tblMachineMaintenanceRecomendList.getTblMachineMaintenanceRecomendList()) {
                    tblMachineMaintenanceRecomendService.updateTblMachineMaintenanceRecomend(tblMachineMaintenanceRecomend, loginUser);
                }
            }
        }
		// メンテナンスの一次保存機能 2018/09/13 -E
        // Iteration4.2 設備メンテナンス候補更新 End

        //        basicResponse.setError(true);
        //        basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
        //        basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_end"));
        //        return basicResponse;
        return basicResponse;
    }


    /**
     * 設備メンテナンス開始入力 登録
     *
     * @param machineMaintenanceRemodelingVo
     * @param type
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse postMachineMaintenanceDetailes2(TblMachineMaintenanceRemodelingVo machineMaintenanceRemodelingVo, int type, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();

//        MstMachine aMstMachine = entityManager.find(MstMachine.class, machineMaintenanceRemodelingVo.getMachineId());
//        if (aMstMachine != null) {
//            if (null != aMstMachine.getCompanyId() && !"".equals(aMstMachine.getCompanyId())) {
//                basicResponse = FileUtil.checkMachineExternal(entityManager, mstDictionaryService, "", aMstMachine.getCompanyId(), loginUser);
//                if (basicResponse.isError()) {
//                    // 外部管理されているため、編集不可
//                    return basicResponse;
//                }
//            }
//        } else {
//            //該当設備が削除された可能性が発生
//            basicResponse.setError(true);
//            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
//            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_data_deleted"));
//            return basicResponse;
//        }

        //設備改造メンテナンス 更新 メンテナンス分類、報告事項、
        TblMachineMaintenanceRemodeling machineMaintenanceRemodeling = entityManager.find(TblMachineMaintenanceRemodeling.class, machineMaintenanceRemodelingVo.getId());
        if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {
            machineMaintenanceRemodeling.setMainteType(machineMaintenanceRemodelingVo.getMainteType());
        } else if (type == CommonConstants.MAINTEORREMODEL_REMODEL) {
            machineMaintenanceRemodeling.setRemodelingType(machineMaintenanceRemodelingVo.getRemodelingType());
        }

        machineMaintenanceRemodeling.setWorkingTimeMinutes(machineMaintenanceRemodelingVo.getWorkingTimeMinutes());//所要時間 20170901 Apeng add
        machineMaintenanceRemodeling.setReport(machineMaintenanceRemodelingVo.getReport());
        machineMaintenanceRemodeling.setTblDirectionId(machineMaintenanceRemodelingVo.getTblDirectionId());
        //システム設定により手配・工事テーブル参照しない場合はコードを直接保存
        if (machineMaintenanceRemodelingVo.getTblDirectionId() == null && machineMaintenanceRemodelingVo.getTblDirectionCode() != null) {
            machineMaintenanceRemodeling.setTblDirectionCode(machineMaintenanceRemodelingVo.getTblDirectionCode());
        }
        
        machineMaintenanceRemodeling.setUpdateDate(new Date());
        machineMaintenanceRemodeling.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(machineMaintenanceRemodeling);

        if (null != machineMaintenanceRemodelingVo.getIssueId() && !"".equals(machineMaintenanceRemodelingVo.getIssueId().trim())) {
            TblIssue tblIssue = entityManager.find(TblIssue.class, machineMaintenanceRemodelingVo.getIssueId());
            if (tblIssue != null) {
                try {
                    String measureStatus = machineMaintenanceRemodelingVo.getMeasureStatus();
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
            //設備メンテナンス詳細 追加
            detailVos = machineMaintenanceRemodelingVo.getMachineMaintenanceDetailVo();

        } else if (type == CommonConstants.MAINTEORREMODEL_REMODEL) {
            detailVos = machineMaintenanceRemodelingVo.getMachineRemodelingDetailVo();
        }

        if (null != detailVos && !detailVos.isEmpty()) {
            if (type == CommonConstants.MAINTEORREMODEL_MAINTE) {
                List<TblMachineMaintenanceDetailVo> newMaintenanceDetailVo = new ArrayList<>();
                for (int i = 0; i < detailVos.size(); i++) {
                    TblMachineMaintenanceDetailVo machineMaintenanceDetailVo = (TblMachineMaintenanceDetailVo) detailVos.get(i);
                    if (!"1".equals(machineMaintenanceDetailVo.getDeleteFlag())) {
                        newMaintenanceDetailVo.add(machineMaintenanceDetailVo);
                    }
                }

                Query query = entityManager.createQuery("DELETE FROM TblMachineMaintenanceDetail t WHERE t.tblMachineMaintenanceDetailPK.maintenanceId = :maintenanceId");
                query.setParameter("maintenanceId", machineMaintenanceRemodeling.getId());
                query.executeUpdate();

                for (int i = 0; i < newMaintenanceDetailVo.size(); i++) {
                    TblMachineMaintenanceDetailVo machineMaintenanceDetailVo = (TblMachineMaintenanceDetailVo) newMaintenanceDetailVo.get(i);

                    TblMachineMaintenanceDetail machineMaintenanceDetail = new TblMachineMaintenanceDetail();
                    if (null != machineMaintenanceDetailVo.getMainteReasonCategory1() && !machineMaintenanceDetailVo.getMainteReasonCategory1().equals("")) {
                        machineMaintenanceDetail.setMainteReasonCategory1(Integer.parseInt(machineMaintenanceDetailVo.getMainteReasonCategory1()));
                        machineMaintenanceDetail.setMainteReasonCategory1Text(machineMaintenanceDetailVo.getMainteReasonCategory1Text());

                        if (null != machineMaintenanceDetailVo.getMainteReasonCategory2() && !machineMaintenanceDetailVo.getMainteReasonCategory2().equals("")) {
                            machineMaintenanceDetail.setMainteReasonCategory2(Integer.parseInt(machineMaintenanceDetailVo.getMainteReasonCategory2()));
                            machineMaintenanceDetail.setMainteReasonCategory2Text(machineMaintenanceDetailVo.getMainteReasonCategory2Text());

                            if (null != machineMaintenanceDetailVo.getMainteReasonCategory3() && !machineMaintenanceDetailVo.getMainteReasonCategory3().equals("")) {
                                machineMaintenanceDetail.setMainteReasonCategory3(Integer.parseInt(machineMaintenanceDetailVo.getMainteReasonCategory3()));
                                machineMaintenanceDetail.setMainteReasonCategory3Text(machineMaintenanceDetailVo.getMainteReasonCategory3Text());
                            } else {
                                machineMaintenanceDetail.setMainteReasonCategory3(null);
                                machineMaintenanceDetail.setMainteReasonCategory3Text(null);
                            }
                        } else {
                            machineMaintenanceDetail.setMainteReasonCategory2(null);
                            machineMaintenanceDetail.setMainteReasonCategory2Text(null);
                            machineMaintenanceDetail.setMainteReasonCategory3(null);
                            machineMaintenanceDetail.setMainteReasonCategory3Text(null);
                        }
                    } else {
                        machineMaintenanceDetail.setMainteReasonCategory1(null);
                        machineMaintenanceDetail.setMainteReasonCategory1Text(null);
                        machineMaintenanceDetail.setMainteReasonCategory2(null);
                        machineMaintenanceDetail.setMainteReasonCategory2Text(null);
                        machineMaintenanceDetail.setMainteReasonCategory3(null);
                        machineMaintenanceDetail.setMainteReasonCategory3Text(null);
                    }
                    machineMaintenanceDetail.setManiteReason(machineMaintenanceDetailVo.getManiteReason());

                    if (null != machineMaintenanceDetailVo.getTaskCategory1() && !machineMaintenanceDetailVo.getTaskCategory1().equals("")) {
                        machineMaintenanceDetail.setTaskCategory1(Integer.parseInt(machineMaintenanceDetailVo.getTaskCategory1()));
                        machineMaintenanceDetail.setTaskCategory1Text(machineMaintenanceDetailVo.getTaskCategory1Text());
                        if (null != machineMaintenanceDetailVo.getTaskCategory2() && !machineMaintenanceDetailVo.getTaskCategory2().equals("")) {
                            machineMaintenanceDetail.setTaskCategory2(Integer.parseInt(machineMaintenanceDetailVo.getTaskCategory2()));
                            machineMaintenanceDetail.setTaskCategory2Text(machineMaintenanceDetailVo.getTaskCategory2Text());
                            if (null != machineMaintenanceDetailVo.getTaskCategory3() && !machineMaintenanceDetailVo.getTaskCategory3().equals("")) {
                                machineMaintenanceDetail.setTaskCategory3(Integer.parseInt(machineMaintenanceDetailVo.getTaskCategory3()));
                                machineMaintenanceDetail.setTaskCategory3Text(machineMaintenanceDetailVo.getTaskCategory3Text());
                            } else {
                                machineMaintenanceDetail.setTaskCategory3(null);
                                machineMaintenanceDetail.setTaskCategory3Text(null);
                            }
                        } else {
                            machineMaintenanceDetail.setTaskCategory2(null);
                            machineMaintenanceDetail.setTaskCategory2Text(null);
                            machineMaintenanceDetail.setTaskCategory3(null);
                            machineMaintenanceDetail.setTaskCategory3Text(null);
                        }
                    } else {
                        machineMaintenanceDetail.setTaskCategory1(null);
                        machineMaintenanceDetail.setTaskCategory1Text(null);
                        machineMaintenanceDetail.setTaskCategory2(null);
                        machineMaintenanceDetail.setTaskCategory2Text(null);
                        machineMaintenanceDetail.setTaskCategory3(null);
                        machineMaintenanceDetail.setTaskCategory3Text(null);
                    }
                    machineMaintenanceDetail.setTask(machineMaintenanceDetailVo.getTask());

                    if (null != machineMaintenanceDetailVo.getMeasureDirectionCategory1() && !machineMaintenanceDetailVo.getMeasureDirectionCategory1().equals("")) {
                        machineMaintenanceDetail.setMeasureDirectionCategory1(Integer.parseInt(machineMaintenanceDetailVo.getMeasureDirectionCategory1()));
                        machineMaintenanceDetail.setMeasureDirectionCategory1Text(machineMaintenanceDetailVo.getMeasureDirectionCategory1Text());
                        if (null != machineMaintenanceDetailVo.getMeasureDirectionCategory2() && !machineMaintenanceDetailVo.getMeasureDirectionCategory2().equals("")) {
                            machineMaintenanceDetail.setMeasureDirectionCategory2(Integer.parseInt(machineMaintenanceDetailVo.getMeasureDirectionCategory2()));
                            machineMaintenanceDetail.setMeasureDirectionCategory2Text(machineMaintenanceDetailVo.getMeasureDirectionCategory2Text());
                            if (null != machineMaintenanceDetailVo.getMeasureDirectionCategory3() && !machineMaintenanceDetailVo.getMeasureDirectionCategory3().equals("")) {
                                machineMaintenanceDetail.setMeasureDirectionCategory3(Integer.parseInt(machineMaintenanceDetailVo.getMeasureDirectionCategory3()));
                                machineMaintenanceDetail.setMeasureDirectionCategory3Text(machineMaintenanceDetailVo.getMeasureDirectionCategory3Text());
                            } else {
                                machineMaintenanceDetail.setMeasureDirectionCategory3(null);
                                machineMaintenanceDetail.setMeasureDirectionCategory3Text(null);
                            }
                        } else {
                            machineMaintenanceDetail.setMeasureDirectionCategory2(null);
                            machineMaintenanceDetail.setMeasureDirectionCategory2Text(null);
                            machineMaintenanceDetail.setMeasureDirectionCategory3(null);
                            machineMaintenanceDetail.setMeasureDirectionCategory3Text(null);
                        }
                    } else {
                        machineMaintenanceDetail.setMeasureDirectionCategory1(null);
                        machineMaintenanceDetail.setMeasureDirectionCategory1Text(null);
                        machineMaintenanceDetail.setMeasureDirectionCategory2(null);
                        machineMaintenanceDetail.setMeasureDirectionCategory2Text(null);
                        machineMaintenanceDetail.setMeasureDirectionCategory3(null);
                        machineMaintenanceDetail.setMeasureDirectionCategory3Text(null);
                    }
                    machineMaintenanceDetail.setMeasureDirection(machineMaintenanceDetailVo.getMeasureDirection());

                    TblMachineMaintenanceDetailPK pk = new TblMachineMaintenanceDetailPK();
                    pk.setMaintenanceId(machineMaintenanceRemodeling.getId());
                    pk.setSeq((i + 1));
                    machineMaintenanceDetail.setTblMachineMaintenanceDetailPK(pk);
                    machineMaintenanceDetail.setId(IDGenerator.generate());
                    machineMaintenanceDetail.setCreateDate(new Date());
                    machineMaintenanceDetail.setCreateUserUuid(loginUser.getUserUuid());
                    machineMaintenanceDetail.setUpdateDate(new Date());
                    machineMaintenanceDetail.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.persist(machineMaintenanceDetail);

                    //設備点検結果 追加
                    List<TblMachineInspectionResultVo> machineInspectionResultVos = machineMaintenanceRemodelingVo.getMachineInspectionResultVo();
                    if (null == machineInspectionResultVos || machineInspectionResultVos.isEmpty()) {
                        machineInspectionResultVos = machineMaintenanceDetailVo.getMachineInspectionResultVos();
                    }
                    if (null != machineInspectionResultVos && !machineInspectionResultVos.isEmpty()) {
                        List<TblMachineInspectionResultVo> newInspectionResultVo = new ArrayList<>();
                        Query delInspectionResultQuery = entityManager.createNamedQuery("TblMachineInspectionResult.deleteByMaintenanceDetailId");
                        if (null != machineMaintenanceDetailVo.getId() && null != machineMaintenanceRemodelingVo.getMachineInspectionResultVo() && !machineMaintenanceRemodelingVo.getMachineInspectionResultVo().isEmpty()) {
                            delInspectionResultQuery.setParameter("maintenanceDetailId", machineMaintenanceDetailVo.getId());

                            for (TblMachineInspectionResultVo aMachineInspectionResultVo : machineInspectionResultVos) {
                                if (aMachineInspectionResultVo.getMaintenanceDetailId().equals(machineMaintenanceDetailVo.getId())) {
                                    newInspectionResultVo.add(aMachineInspectionResultVo);
                                }
                            }
                        } else {
                            delInspectionResultQuery.setParameter("maintenanceDetailId", machineMaintenanceDetail.getId());
                            newInspectionResultVo = machineInspectionResultVos;
                        }
                        delInspectionResultQuery.executeUpdate();
                        for (TblMachineInspectionResultVo aMachineInspectionResultVo : newInspectionResultVo) {
                            TblMachineInspectionResult aResult = new TblMachineInspectionResult();
                            aResult.setUpdateDate(new Date());
                            aResult.setUpdateUserUuid(loginUser.getUserUuid());
                            aResult.setInspectionResult(aMachineInspectionResultVo.getInspectionResult());
                            aResult.setInspectionResultText(aMachineInspectionResultVo.getInspectionResultText());

                            aResult.setId(IDGenerator.generate());
                            TblMachineInspectionResultPK resultPK = new TblMachineInspectionResultPK();
                            resultPK.setInspectionItemId(aMachineInspectionResultVo.getMstMachineInspectionItemId());
                            resultPK.setMaintenanceDetailId(machineMaintenanceDetail.getId());
                            resultPK.setSeq(aMachineInspectionResultVo.getSeq());
                            aResult.setTblMachineInspectionResultPK(resultPK);

                            aResult.setCreateDate(new Date());
                            aResult.setCreateUserUuid(loginUser.getUserUuid());
                            entityManager.persist(aResult);
                        }
                    }

                    //設備ImageFile 追加
                    List<TblMachineMaintenanceDetailImageFileVo> machineMaintenanceDetailImageFileVos = machineMaintenanceRemodelingVo.getImageFileVos();
                    if (null == machineMaintenanceDetailImageFileVos || machineMaintenanceDetailImageFileVos.isEmpty()) {
                        machineMaintenanceDetailImageFileVos = machineMaintenanceDetailVo.getTblMachineMaintenanceDetailImageFileVos();
                    }
                    if (null != machineMaintenanceDetailImageFileVos && !machineMaintenanceDetailImageFileVos.isEmpty()) {
                        List<TblMachineMaintenanceDetailImageFileVo> newMachineMaintenanceDetailImageFileVos = new ArrayList<>();
                        Query delInspectionResultQuery = entityManager.createNamedQuery("TblMachineMaintenanceDetailImageFile.deleteByMaintenanceDetailId");
                        if (null != machineMaintenanceDetailVo.getId() && null != machineMaintenanceRemodelingVo.getImageFileVos() && !machineMaintenanceRemodelingVo.getImageFileVos().isEmpty()) {
                            delInspectionResultQuery.setParameter("maintenanceDetailId", machineMaintenanceDetailVo.getId());

                            for (TblMachineMaintenanceDetailImageFileVo aMachineMaintenanceDetailImageFileVo : machineMaintenanceDetailImageFileVos) {
                                if (aMachineMaintenanceDetailImageFileVo.getMaintenanceDetailId().equals(machineMaintenanceDetailVo.getId())) {
                                    newMachineMaintenanceDetailImageFileVos.add(aMachineMaintenanceDetailImageFileVo);
                                }
                            }
                        } else {
                            delInspectionResultQuery.setParameter("maintenanceDetailId", machineMaintenanceDetail.getId());

                            newMachineMaintenanceDetailImageFileVos = machineMaintenanceDetailImageFileVos;
                        }
                        delInspectionResultQuery.executeUpdate();
                        for (TblMachineMaintenanceDetailImageFileVo aImageFileVo : newMachineMaintenanceDetailImageFileVos) {
                            TblMachineMaintenanceDetailImageFile aImageFile = new TblMachineMaintenanceDetailImageFile();
                            aImageFile.setUpdateDate(new Date());
                            aImageFile.setUpdateUserUuid(loginUser.getUserUuid());
                            aImageFile.setCreateDate(new Date());
                            aImageFile.setCreateDateUuid(loginUser.getUserUuid());

                            TblMachineMaintenanceDetailImageFilePK aPK = new TblMachineMaintenanceDetailImageFilePK();
                            aPK.setMaintenanceDetailId(machineMaintenanceDetail.getId());
                            aPK.setSeq(Integer.parseInt(aImageFileVo.getSeq()));
                            aImageFile.setTblMachineMaintenanceDetailImageFilePK(aPK);

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
                            if (null != aImageFileVo.getTakenDateStr() && !aImageFileVo.getTakenDateStr().isEmpty()) {
                                aImageFile.setTakenDate(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()));
                            } else {
                                aImageFile.setTakenDate(null);
                            }
                            if (null != aImageFileVo.getTakenDateStzStr() && !aImageFileVo.getTakenDateStzStr().isEmpty()) {
                                aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStzStr()));
                            } else {
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
                List<TblMachineRemodelingDetailVo> newRemodelingDetailVo = new ArrayList<>();
                for (int i = 0; i < detailVos.size(); i++) {
                    TblMachineRemodelingDetailVo machineRemodelingDetailVo = (TblMachineRemodelingDetailVo) detailVos.get(i);
                    if (!"1".equals(machineRemodelingDetailVo.getDeleteFlag())) {
                        newRemodelingDetailVo.add(machineRemodelingDetailVo);
                    }
                }

                Query query = entityManager.createQuery("DELETE FROM TblMachineRemodelingDetail t WHERE t.tblMachineRemodelingDetailPK.maintenanceId = :maintenanceId");
                query.setParameter("maintenanceId", machineMaintenanceRemodeling.getId());
                query.executeUpdate();

                for (int i = 0; i < newRemodelingDetailVo.size(); i++) {
                    TblMachineRemodelingDetailVo machineRemodelingDetailVo = (TblMachineRemodelingDetailVo) newRemodelingDetailVo.get(i);

                    TblMachineRemodelingDetail machineRemodelingDetail = new TblMachineRemodelingDetail();
                    if (null != machineRemodelingDetailVo.getRemodelReasonCategory1() && !machineRemodelingDetailVo.getRemodelReasonCategory1().equals("")) {
                        machineRemodelingDetail.setRemodelReasonCategory1(Integer.parseInt(machineRemodelingDetailVo.getRemodelReasonCategory1()));
                        machineRemodelingDetail.setRemodelReasonCategory1Text(machineRemodelingDetailVo.getRemodelReasonCategory1Text());

                        if (null != machineRemodelingDetailVo.getRemodelReasonCategory2() && !machineRemodelingDetailVo.getRemodelReasonCategory2().equals("")) {
                            machineRemodelingDetail.setRemodelReasonCategory2(Integer.parseInt(machineRemodelingDetailVo.getRemodelReasonCategory2()));
                            machineRemodelingDetail.setRemodelReasonCategory2Text(machineRemodelingDetailVo.getRemodelReasonCategory2Text());

                            if (null != machineRemodelingDetailVo.getRemodelReasonCategory3() && !machineRemodelingDetailVo.getRemodelReasonCategory3().equals("")) {
                                machineRemodelingDetail.setRemodelReasonCategory3(Integer.parseInt(machineRemodelingDetailVo.getRemodelReasonCategory3()));
                                machineRemodelingDetail.setRemodelReasonCategory3Text(machineRemodelingDetailVo.getRemodelReasonCategory3Text());
                            } else {
                                machineRemodelingDetail.setRemodelReasonCategory3(null);
                                machineRemodelingDetail.setRemodelReasonCategory3Text(null);
                            }
                        } else {
                            machineRemodelingDetail.setRemodelReasonCategory2(null);
                            machineRemodelingDetail.setRemodelReasonCategory2Text(null);
                            machineRemodelingDetail.setRemodelReasonCategory3(null);
                            machineRemodelingDetail.setRemodelReasonCategory3Text(null);
                        }
                    } else {
                        machineRemodelingDetail.setRemodelReasonCategory1(null);
                        machineRemodelingDetail.setRemodelReasonCategory1Text(null);
                        machineRemodelingDetail.setRemodelReasonCategory2(null);
                        machineRemodelingDetail.setRemodelReasonCategory2Text(null);
                        machineRemodelingDetail.setRemodelReasonCategory3(null);
                        machineRemodelingDetail.setRemodelReasonCategory3Text(null);
                    }
                    machineRemodelingDetail.setRemodelReason(machineRemodelingDetailVo.getRemodelReason());

                    if (null != machineRemodelingDetailVo.getTaskCategory1() && !machineRemodelingDetailVo.getTaskCategory1().equals("")) {
                        machineRemodelingDetail.setTaskCategory1(Integer.parseInt(machineRemodelingDetailVo.getTaskCategory1()));
                        machineRemodelingDetail.setTaskCategory1Text(machineRemodelingDetailVo.getTaskCategory1Text());
                        if (null != machineRemodelingDetailVo.getTaskCategory2() && !machineRemodelingDetailVo.getTaskCategory2().equals("")) {
                            machineRemodelingDetail.setTaskCategory2(Integer.parseInt(machineRemodelingDetailVo.getTaskCategory2()));
                            machineRemodelingDetail.setTaskCategory2Text(machineRemodelingDetailVo.getTaskCategory2Text());
                            if (null != machineRemodelingDetailVo.getTaskCategory3() && !machineRemodelingDetailVo.getTaskCategory3().equals("")) {
                                machineRemodelingDetail.setTaskCategory3(Integer.parseInt(machineRemodelingDetailVo.getTaskCategory3()));
                                machineRemodelingDetail.setTaskCategory3Text(machineRemodelingDetailVo.getTaskCategory3Text());
                            } else {
                                machineRemodelingDetail.setTaskCategory3(null);
                                machineRemodelingDetail.setTaskCategory3Text(null);
                            }
                        } else {
                            machineRemodelingDetail.setTaskCategory2(null);
                            machineRemodelingDetail.setTaskCategory2Text(null);
                            machineRemodelingDetail.setTaskCategory3(null);
                            machineRemodelingDetail.setTaskCategory3Text(null);
                        }
                    } else {
                        machineRemodelingDetail.setTaskCategory1(null);
                        machineRemodelingDetail.setTaskCategory1Text(null);
                        machineRemodelingDetail.setTaskCategory2(null);
                        machineRemodelingDetail.setTaskCategory2Text(null);
                        machineRemodelingDetail.setTaskCategory3(null);
                        machineRemodelingDetail.setTaskCategory3Text(null);
                    }
                    machineRemodelingDetail.setTask(machineRemodelingDetailVo.getTask());

                    if (null != machineRemodelingDetailVo.getRemodelDirectionCategory1() && !machineRemodelingDetailVo.getRemodelDirectionCategory1().equals("")) {
                        machineRemodelingDetail.setRemodelDirectionCategory1(Integer.parseInt(machineRemodelingDetailVo.getRemodelDirectionCategory1()));
                        machineRemodelingDetail.setRemodelDirectionCategory1Text(machineRemodelingDetailVo.getRemodelDirectionCategory1Text());
                        if (null != machineRemodelingDetailVo.getRemodelDirectionCategory2() && !machineRemodelingDetailVo.getRemodelDirectionCategory2().equals("")) {
                            machineRemodelingDetail.setRemodelDirectionCategory2(Integer.parseInt(machineRemodelingDetailVo.getRemodelDirectionCategory2()));
                            machineRemodelingDetail.setRemodelDirectionCategory2Text(machineRemodelingDetailVo.getRemodelDirectionCategory2Text());
                            if (null != machineRemodelingDetailVo.getRemodelDirectionCategory3() && !machineRemodelingDetailVo.getRemodelDirectionCategory3().equals("")) {
                                machineRemodelingDetail.setRemodelDirectionCategory3(Integer.parseInt(machineRemodelingDetailVo.getRemodelDirectionCategory3()));
                                machineRemodelingDetail.setRemodelDirectionCategory3Text(machineRemodelingDetailVo.getRemodelDirectionCategory3Text());
                            } else {
                                machineRemodelingDetail.setRemodelDirectionCategory3(null);
                                machineRemodelingDetail.setRemodelDirectionCategory3Text(null);
                            }
                        } else {
                            machineRemodelingDetail.setRemodelDirectionCategory2(null);
                            machineRemodelingDetail.setRemodelDirectionCategory2Text(null);
                            machineRemodelingDetail.setRemodelDirectionCategory3(null);
                            machineRemodelingDetail.setRemodelDirectionCategory3Text(null);
                        }
                    } else {
                        machineRemodelingDetail.setRemodelDirectionCategory1(null);
                        machineRemodelingDetail.setRemodelDirectionCategory1Text(null);
                        machineRemodelingDetail.setRemodelDirectionCategory2(null);
                        machineRemodelingDetail.setRemodelDirectionCategory2Text(null);
                        machineRemodelingDetail.setRemodelDirectionCategory3(null);
                        machineRemodelingDetail.setRemodelDirectionCategory3Text(null);
                    }
                    machineRemodelingDetail.setRemodelDirection(machineRemodelingDetailVo.getRemodelDirection());

                    TblMachineRemodelingDetailPK pk = new TblMachineRemodelingDetailPK();
                    pk.setMaintenanceId(machineMaintenanceRemodeling.getId());
                    pk.setSeq((i + 1));
                    machineRemodelingDetail.setTblMachineRemodelingDetailPK(pk);
                    machineRemodelingDetail.setId(IDGenerator.generate());
                    machineRemodelingDetail.setCreateDate(new Date());
                    machineRemodelingDetail.setCreateUserUuid(loginUser.getUserUuid());
                    machineRemodelingDetail.setUpdateDate(new Date());
                    machineRemodelingDetail.setUpdateUserUuid(loginUser.getUserUuid());
                    entityManager.persist(machineRemodelingDetail);

                    //設備点検結果 追加
                    List<TblMachineRemodelingInspectionResultVo> machineRemodelingInspectionResultVos = machineMaintenanceRemodelingVo.getRemodelingInspectionResultVo();
                    if (null == machineRemodelingInspectionResultVos || machineRemodelingInspectionResultVos.isEmpty()) {
                        machineRemodelingInspectionResultVos = machineRemodelingDetailVo.getMachineRemodelingInspectionResultVo();
                    }
                    if (null != machineRemodelingInspectionResultVos && !machineRemodelingInspectionResultVos.isEmpty()) {
                        List<TblMachineRemodelingInspectionResultVo> newRemodelingInspectionResultVo = new ArrayList<>();
                        Query delInspectionResultQuery = entityManager.createQuery("DELETE FROM TblMachineRemodelingInspectionResult t WHERE t.tblMachineRemodelingInspectionResultPK.remodelingDetailId = :remodelingDetailId");
                        if (null != machineRemodelingDetailVo.getId() && null != machineMaintenanceRemodelingVo.getRemodelingInspectionResultVo() && !machineMaintenanceRemodelingVo.getRemodelingInspectionResultVo().isEmpty()) {
                            delInspectionResultQuery.setParameter("remodelingDetailId", machineRemodelingDetailVo.getId());

                            for (TblMachineRemodelingInspectionResultVo aMachineRemodelingInspectionResultVo : machineRemodelingInspectionResultVos) {
                                if (aMachineRemodelingInspectionResultVo.getRemodelingDetailId().equals(machineRemodelingDetailVo.getId())) {
                                    newRemodelingInspectionResultVo.add(aMachineRemodelingInspectionResultVo);
                                }
                            }
                        } else {
                            delInspectionResultQuery.setParameter("remodelingDetailId", machineRemodelingDetail.getId());
                            newRemodelingInspectionResultVo = machineRemodelingInspectionResultVos;
                        }
                        delInspectionResultQuery.executeUpdate();

                        for (TblMachineRemodelingInspectionResultVo aMachineRemodelingInspectionResultVo : newRemodelingInspectionResultVo) {
                            TblMachineRemodelingInspectionResult aResult = new TblMachineRemodelingInspectionResult();
                            aResult.setUpdateDate(new Date());
                            aResult.setUpdateUserUuid(loginUser.getUserUuid());
                            aResult.setInspectionResult(aMachineRemodelingInspectionResultVo.getInspectionResult());
                            aResult.setInspectionResultText(aMachineRemodelingInspectionResultVo.getInspectionResultText());

                            aResult.setId(IDGenerator.generate());
                            TblMachineRemodelingInspectionResultPK resultPK = new TblMachineRemodelingInspectionResultPK();
                            resultPK.setInspectionItemId(aMachineRemodelingInspectionResultVo.getMstMachineInspectionItemId());
                            resultPK.setRemodelingDetailId(machineRemodelingDetail.getId());
                            resultPK.setSeq(aMachineRemodelingInspectionResultVo.getSeq());
                            aResult.setTblMachineRemodelingInspectionResultPK(resultPK);

                            aResult.setCreateDate(new Date());
                            aResult.setCreateUserUuid(loginUser.getUserUuid());
                            entityManager.persist(aResult);
                        }
                    }

                    //設備ImageFile 追加
                    List<TblMachineRemodelingDetailImageFileVo> machineRemodelingDetailImageFileVos = machineMaintenanceRemodelingVo.getRimageFileVos();
                    if (null == machineRemodelingDetailImageFileVos || machineRemodelingDetailImageFileVos.isEmpty()) {
                        machineRemodelingDetailImageFileVos = machineRemodelingDetailVo.getMachineRemodelingDetailImageFileVos();
                    }
                    if (null != (machineRemodelingDetailImageFileVos = machineMaintenanceRemodelingVo.getRimageFileVos()) && !machineRemodelingDetailImageFileVos.isEmpty()) {
                        List<TblMachineRemodelingDetailImageFileVo> newMachineRemodelingDetailImageFileVos = new ArrayList<>();
                        Query delImageFileQuery = entityManager.createNamedQuery("TblMachineRemodelingDetailImageFile.deleteByRemodelingDetailId");
                        if (null != machineRemodelingDetailVo.getId() && null != machineMaintenanceRemodelingVo.getRimageFileVos() && !machineMaintenanceRemodelingVo.getRimageFileVos().isEmpty()) {
                            delImageFileQuery.setParameter("remodelingDetailId", machineRemodelingDetailVo.getId());

                            for (TblMachineRemodelingDetailImageFileVo aMachineRemodelingDetailImageFileVo : machineRemodelingDetailImageFileVos) {
                                if (aMachineRemodelingDetailImageFileVo.getRemodelingDetailId().equals(machineRemodelingDetailVo.getId())) {
                                    newMachineRemodelingDetailImageFileVos.add(aMachineRemodelingDetailImageFileVo);
                                }
                            }
                        } else {
                            delImageFileQuery.setParameter("remodelingDetailId", machineRemodelingDetail.getId());
                            newMachineRemodelingDetailImageFileVos = machineRemodelingDetailImageFileVos;
                        }
                        delImageFileQuery.executeUpdate();

                        for (TblMachineRemodelingDetailImageFileVo aImageFileVo : newMachineRemodelingDetailImageFileVos) {
                            TblMachineRemodelingDetailImageFile aImageFile = new TblMachineRemodelingDetailImageFile();
                            aImageFile.setUpdateDate(new Date());
                            aImageFile.setUpdateUserUuid(loginUser.getUserUuid());
                            aImageFile.setCreateDate(new Date());
                            aImageFile.setCreateDateUuid(loginUser.getUserUuid());

                            TblMachineRemodelingDetailImageFilePK aPK = new TblMachineRemodelingDetailImageFilePK();
                            aPK.setRemodelingDetailId(machineRemodelingDetail.getId());
                            aPK.setSeq(Integer.parseInt(aImageFileVo.getSeq()));
                            aImageFile.setTblMachineRemodelingDetailImageFilePK(aPK);

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
                            if (null != aImageFileVo.getTakenDateStr() && !aImageFileVo.getTakenDateStr().isEmpty()) {
                                aImageFile.setTakenDate(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStr()));
                            } else {
                                aImageFile.setTakenDate(null);
                            }
                            if (null != aImageFileVo.getTakenDateStzStr() && !aImageFileVo.getTakenDateStzStr().isEmpty()) {
                                aImageFile.setTakenDateStz(new FileUtil().getDateTimeParseForDate(aImageFileVo.getTakenDateStzStr()));
                            } else {
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
     * バッチで設備メンテナンス詳細データを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMachineMaintenanceDetailVo getExtMachineMaintenanceDetailsByBatch(String latestExecutedDate, String machineUuid) {
        TblMachineMaintenanceDetailVo resList = new TblMachineMaintenanceDetailVo();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblMachineMaintenanceDetail t join MstApiUser u on u.companyId = t.tblMachineMaintenanceRemodeling.mstMachine.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and t.tblMachineMaintenanceRemodeling.mstMachine.uuid = :machineUuid ");
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
        List<TblMachineMaintenanceDetail> tmpList = query.getResultList();
        for (TblMachineMaintenanceDetail tblMachineMaintenanceDetail : tmpList) {
            tblMachineMaintenanceDetail.setTblMachineMaintenanceRemodeling(null);
        }
        resList.setTblMachineMaintenanceDetails(tmpList);
        return resList;
    }
    
    /**
     * バッチで設備メンテナンス詳細データを更新
     *
     * @param machineMaintenanceDetails
     * @return
     */
    @Transactional
    public BasicResponse updateExtMachineMaintenanceDetailsByBatch(List<TblMachineMaintenanceDetail> machineMaintenanceDetails) {
        BasicResponse response = new BasicResponse();
        if (machineMaintenanceDetails != null && !machineMaintenanceDetails.isEmpty()) {
            for (TblMachineMaintenanceDetail aMachineMaintenanceDetail : machineMaintenanceDetails) {
                TblMachineMaintenanceRemodeling macheineMaintenanceRemodeling = entityManager.find(TblMachineMaintenanceRemodeling.class, aMachineMaintenanceDetail.getTblMachineMaintenanceDetailPK().getMaintenanceId());
                if (null != macheineMaintenanceRemodeling) {
                    
                    List<TblMachineMaintenanceDetail> oldMachineMaintenanceDetails = entityManager.createQuery("SELECT t FROM TblMachineMaintenanceDetail t WHERE t.tblMachineMaintenanceDetailPK.maintenanceId = :maintenanceId and t.tblMachineMaintenanceDetailPK.seq = :seq ")
                            .setParameter("maintenanceId", aMachineMaintenanceDetail.getTblMachineMaintenanceDetailPK().getMaintenanceId())
                            .setParameter("seq", aMachineMaintenanceDetail.getTblMachineMaintenanceDetailPK().getSeq())
                            .setMaxResults(1)
                            .getResultList();

                    TblMachineMaintenanceDetail newDetail;
                    if (null != oldMachineMaintenanceDetails && !oldMachineMaintenanceDetails.isEmpty()) {
                        newDetail = oldMachineMaintenanceDetails.get(0);
                    } else {
                        newDetail = new TblMachineMaintenanceDetail();
                        TblMachineMaintenanceDetailPK pk = new TblMachineMaintenanceDetailPK();
                        pk.setMaintenanceId(aMachineMaintenanceDetail.getTblMachineMaintenanceDetailPK().getMaintenanceId());
                        pk.setSeq(aMachineMaintenanceDetail.getTblMachineMaintenanceDetailPK().getSeq());
                        newDetail.setTblMachineMaintenanceDetailPK(pk);
                        
                    }
                    newDetail.setId(aMachineMaintenanceDetail.getId());
                    newDetail.setMainteReasonCategory1(aMachineMaintenanceDetail.getMainteReasonCategory1());
                    newDetail.setMainteReasonCategory1Text(aMachineMaintenanceDetail.getMainteReasonCategory1Text());
                    newDetail.setMainteReasonCategory2(aMachineMaintenanceDetail.getMainteReasonCategory2());
                    newDetail.setMainteReasonCategory2Text(aMachineMaintenanceDetail.getMainteReasonCategory2Text());
                    newDetail.setMainteReasonCategory3(aMachineMaintenanceDetail.getMainteReasonCategory3());
                    newDetail.setMainteReasonCategory3Text(aMachineMaintenanceDetail.getMainteReasonCategory3Text());
                    newDetail.setManiteReason(aMachineMaintenanceDetail.getManiteReason());
                    newDetail.setMeasureDirectionCategory1(aMachineMaintenanceDetail.getMeasureDirectionCategory1());
                    newDetail.setMeasureDirectionCategory1Text(aMachineMaintenanceDetail.getMeasureDirectionCategory1Text());
                    newDetail.setMeasureDirectionCategory2(aMachineMaintenanceDetail.getMeasureDirectionCategory2());
                    newDetail.setMeasureDirectionCategory2Text(aMachineMaintenanceDetail.getMeasureDirectionCategory2Text());
                    newDetail.setMeasureDirectionCategory3(aMachineMaintenanceDetail.getMeasureDirectionCategory3());
                    newDetail.setMeasureDirectionCategory3Text(aMachineMaintenanceDetail.getMeasureDirectionCategory3Text());
                    newDetail.setMeasureDirection(aMachineMaintenanceDetail.getMeasureDirection());
                    newDetail.setTaskCategory1(aMachineMaintenanceDetail.getTaskCategory1());
                    newDetail.setTaskCategory1Text(aMachineMaintenanceDetail.getTaskCategory1Text());
                    newDetail.setTaskCategory2(aMachineMaintenanceDetail.getTaskCategory2());
                    newDetail.setTaskCategory2Text(aMachineMaintenanceDetail.getTaskCategory2Text());
                    newDetail.setTaskCategory3(aMachineMaintenanceDetail.getTaskCategory3());
                    newDetail.setTaskCategory3Text(aMachineMaintenanceDetail.getTaskCategory3Text());
                    newDetail.setTask(aMachineMaintenanceDetail.getTask());

                    newDetail.setCreateDate(aMachineMaintenanceDetail.getCreateDate());
                    newDetail.setCreateUserUuid(aMachineMaintenanceDetail.getCreateUserUuid());
                    newDetail.setUpdateDate(new Date());
                    newDetail.setUpdateUserUuid(aMachineMaintenanceDetail.getUpdateUserUuid());

                    if (null != oldMachineMaintenanceDetails && !oldMachineMaintenanceDetails.isEmpty()) {
                        entityManager.merge(newDetail);
                    } else {
                        entityManager.persist(newDetail);
                    }
                }
            }
        }
        response.setError(false);
        return response;
    }

    /**
     * バッチで設備メンテナンス詳細ImageFileデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblMachineMaintenanceDetailVo getExtMachineMaintenanceDetailImageFilesByBatch(String latestExecutedDate, String machienUuid) {
        TblMachineMaintenanceDetailVo resList = new TblMachineMaintenanceDetailVo();
        StringBuilder sql = new StringBuilder("SELECT distinct t FROM TblMachineMaintenanceDetailImageFile t join MstApiUser u on u.companyId = t.tblMachineMaintenanceDetail.tblMachineMaintenanceRemodeling.mstMachine.ownerCompanyId WHERE 1 = 1 ");
        if (null != machienUuid && !machienUuid.trim().equals("")) {
            sql.append(" and t.tblMachineMaintenanceDetail.tblMachineMaintenanceRemodeling.mstMachine.uuid = :machienUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (t.updateDate > :latestExecutedDate or t.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != machienUuid && !machienUuid.trim().equals("")) {
            query.setParameter("machienUuid", machienUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblMachineMaintenanceDetailImageFile> tmpList = query.getResultList();
        List<TblMachineMaintenanceDetailImageFileVo> resVos = new ArrayList<>();
        FileUtil fu = new FileUtil();
        for (TblMachineMaintenanceDetailImageFile aDetailImageFile : tmpList) {
            TblMachineMaintenanceDetailImageFileVo aVo = new TblMachineMaintenanceDetailImageFileVo();
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
            aVo.setMaintenanceDetailId(aDetailImageFile.getTblMachineMaintenanceDetailImageFilePK().getMaintenanceDetailId());
            aVo.setSeq("" + aDetailImageFile.getTblMachineMaintenanceDetailImageFilePK().getSeq());
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
        resList.setTblMachineMaintenanceDetailImageFileVos(resVos);
        return resList;
    }
    
    /**
     * バッチで設備メンテナンス詳細ImageFileデータを更新
     *
     * @param imageFileVos
     * @return
     */
    @Transactional
    public BasicResponse updateExtMoldMaintenanceDetailImageFilesByBatch(List<TblMachineMaintenanceDetailImageFileVo> imageFileVos) {
        BasicResponse response = new BasicResponse();
        if (imageFileVos != null && !imageFileVos.isEmpty()) {
            FileUtil fu = new FileUtil();
            for (TblMachineMaintenanceDetailImageFileVo aVo : imageFileVos) {
                List<TblMachineMaintenanceDetail> machineMaintenanceDetails = entityManager.createQuery("from TblMachineMaintenanceDetail t where t.id = :maintenanceDetailId ")
                        .setParameter("maintenanceDetailId", aVo.getMaintenanceDetailId())
                        .setMaxResults(1)
                        .getResultList();
                if (null != machineMaintenanceDetails && !machineMaintenanceDetails.isEmpty()) {
                    List<TblMachineMaintenanceDetailImageFile> oldImageFiles = entityManager.createQuery("SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.tblMachineMaintenanceDetailImageFilePK.maintenanceDetailId = :maintenanceDetailId and t.tblMachineMaintenanceDetailImageFilePK.seq = :seq ")
                            .setParameter("maintenanceDetailId", aVo.getMaintenanceDetailId())
                            .setParameter("seq", Integer.parseInt(aVo.getSeq()))
                            .setMaxResults(1)
                            .getResultList();

                    TblMachineMaintenanceDetailImageFile newImageFile;
                    if (null != oldImageFiles && !oldImageFiles.isEmpty()) {
                        newImageFile = oldImageFiles.get(0);
                    } else {
                        newImageFile = new TblMachineMaintenanceDetailImageFile();
                        TblMachineMaintenanceDetailImageFilePK pk = new TblMachineMaintenanceDetailImageFilePK();
                        pk.setMaintenanceDetailId(aVo.getMaintenanceDetailId());
                        pk.setSeq(Integer.parseInt(aVo.getSeq()));
                        newImageFile.setTblMachineMaintenanceDetailImageFilePK(pk);

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
        return response;
    }
}
