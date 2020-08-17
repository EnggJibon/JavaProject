/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production;

import com.kmcj.karte.IndexsResponse;
import com.kmcj.karte.resources.material.MstMaterial;
import com.kmcj.karte.resources.po.shipment.MaterialList;
import com.kmcj.karte.resources.work.*;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.ImportResultResponse;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.choice.MstChoiceList;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.component.lot.TblComponentLot;
import com.kmcj.karte.resources.component.lot.TblComponentLotService;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelation;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationService;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVo;
import com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationVoList;
import com.kmcj.karte.resources.component.material.MstComponentMaterial;
import com.kmcj.karte.resources.component.material.MstComponentMaterialService;
import com.kmcj.karte.resources.component.structure.MstComponentStructure;
import com.kmcj.karte.resources.component.structure.MstComponentStructureVoList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.direction.TblDirection;
import com.kmcj.karte.resources.direction.TblDirectionService;
import com.kmcj.karte.resources.files.TblCsvImport;
import com.kmcj.karte.resources.files.TblCsvImportService;
import com.kmcj.karte.resources.files.TblUploadFile;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.machine.MstMachineService;
import com.kmcj.karte.resources.machine.MstMachineVo;
import com.kmcj.karte.resources.machine.daily.report.TblMachineDailyReport;
import com.kmcj.karte.resources.machine.daily.report.TblMachineDailyReportVo;
import com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetail;
import com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetailList;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodeling;
import com.kmcj.karte.resources.material.MstMaterialList;
import com.kmcj.karte.resources.material.MstMaterialService;
import com.kmcj.karte.resources.material.stock.TblMaterialStockService;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.MstMoldDetail;
import com.kmcj.karte.resources.mold.MstMoldService;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodeling;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import com.kmcj.karte.resources.production.detail.TblProductionDetailService;
import com.kmcj.karte.resources.production.detail.TblProductionDetailVo;
import com.kmcj.karte.resources.production2.Production;
import com.kmcj.karte.resources.production2.ProductionDetail;
import com.kmcj.karte.resources.stock.TblStockService;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.user.MstUserService;
import com.kmcj.karte.resources.work.phase.MstWorkPhase;
import com.kmcj.karte.resources.work.phase.MstWorkPhaseService;
import com.kmcj.karte.util.BeanCopyUtil;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import com.kmcj.karte.util.TimezoneConverter;
import java.math.BigDecimal;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 * 作業実績テーブルサービス
 *
 * @author t.ariki
 */
@Dependent
public class TblProductionService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstMoldService mstMoldService;

    @Inject
    private MstMachineService mstMachineService;

    @Inject
    private MstWorkPhaseService mstWorkPhaseService;

    @Inject
    private TblDirectionService tblDirectionService;

    @Inject
    private CnfSystemService cnfSystemService;

    @Inject
    private TblProductionDetailService tblProductionDetailService;

    @Inject
    private MstChoiceService mstChoiceService;
    
    @Inject
    private MstComponentMaterialService mstComponentMaterialService;
    
    @Inject
    private MstProcedureService mstProcedureService;
    
    @Inject
    private TblStockService tblStockService;
    
    @Inject
    private TblComponentLotService tblComponentLotService;
    
    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private MstComponentService mstComponentService;
    
    @Inject
    private TblCsvImportService tblCsvImportService;
    
    @Inject
    private MstUserService mstUserService;
    
    @Inject
    private MstMaterialService mstMaterialService;
    
    @Inject
    private TblMaterialStockService tblMaterialStockService;

    private Logger logger = Logger.getLogger(TblProductionService.class.getName());

    /**
     * 生産実績-生産実績明細-生産実績ロット残高をすべて結合して全取得
     *
     * @return
     */
    public TblProductionList getProductions() {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT tblProduction FROM TblProduction tblProduction"
                + " JOIN FETCH TblProductionDetail tblProductionDetail"
                + " JOIN FETCH TblProductionLotBalance tblProductionLotBalance"
                + " WHERE 1=1 "
        );
        Query query = entityManager.createQuery(sql.toString());
        List list = query.getResultList();
        TblProductionList response = new TblProductionList();
        response.setTblProductions(list);
        return response;
    }

    /**
     * 生産実績一覧取得(生産実績行取得)
     *
     * @param department
     * @return
     */
    public TblProductionList getProductionHeaders(String department) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT tblProduction FROM TblProduction tblProduction "
                + " JOIN FETCH tblProduction.mstUser "
                + " LEFT JOIN FETCH tblProduction.mstMold "
                + " LEFT JOIN FETCH tblProduction.mstMachine "
                + " LEFT JOIN FETCH tblProduction.tblDirection "
                + " LEFT JOIN FETCH tblProduction.mstWorkPhase "
                + " WHERE 1=1 "
        );
        sql.append(" AND tblProduction.endDatetime IS NULL ");

        // 生産場所選択の場合
        if (StringUtils.isNotEmpty(department)) {
//            sql.append(" AND tblProduction.mstUser.department = :department");
            sql.append(" AND tblProduction.prodDepartment = :department");
        }

        sql.append(" ORDER BY tblProduction.startDatetime ASC ");
        Query query = entityManager.createQuery(sql.toString());

        // 生産場所選択の場合
        if (StringUtils.isNotEmpty(department)) {
            query.setParameter("department", department);
        }
        List list = query.getResultList();
        TblProductionList response = new TblProductionList();
        response.setTblProductions(list);
        return response;
    }

    /**
     * 生産実績一覧取得(1行目の明細付き)
     *
     * @param department
     * @return
     */
    public TblProductionList getProductionHeadersWithFirstDetail(String department) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT tblProductionDetail FROM ProductionDetail  tblProductionDetail "
                + " JOIN FETCH tblProductionDetail.productionId  "
                + " JOIN FETCH tblProductionDetail.mstComponent "
                + " LEFT JOIN FETCH tblProductionDetail.productionId.mstUser "
                + " WHERE 1=1 "
        );
        sql.append(" AND tblProductionDetail.productionId.endDatetime IS NULL ");

        // 生産場所選択の場合
        if (StringUtils.isNotEmpty(department) && !"0".equals(department)) {
//            sql.append(" AND tblProductionDetail.productionId.mstUser.department = :department");
            sql.append(" AND tblProductionDetail.productionId.prodDepartment = :department");
        }

        sql.append(" ORDER BY tblProductionDetail.productionId.startDatetime ASC, tblProductionDetail.mstComponent.componentCode ASC ");
        Query query = entityManager.createQuery(sql.toString());

        // 生産場所選択の場合
        if (StringUtils.isNotEmpty(department) && !"0".equals(department)) {
            query.setParameter("department", Integer.parseInt(department));
        }
        List list = query.getResultList();
        TblProductionList response = new TblProductionList();
        ProductionDetail prevDetail = null;
        for (Object obj: list) {
            ProductionDetail detail = (ProductionDetail)obj;
            boolean canAdd = false;
            if (prevDetail == null) {
                //response.getTblProductions().add(detail.getTblProduction());
                canAdd = true;
            }
            else {
                if (!prevDetail.getProductionId().equals(detail.getProductionId())) {
                    canAdd = true;
                }
            }
            if (canAdd) {
                TblProduction production = new TblProduction();
                BeanCopyUtil.copyFields(detail.getProductionId(), production);
                if (detail.getProductionId().getMstUser() != null) {
                    production.setMstUser(new MstUser());
                    BeanCopyUtil.copyFields(detail.getProductionId().getMstUser(), production.getMstUser());
                }
                if (detail.getProductionId().getMstMold() != null) {
                    production.setMstMold(new MstMold());
                    BeanCopyUtil.copyFields(detail.getProductionId().getMstMold(), production.getMstMold());
                }
                if (detail.getProductionId().getMstMachine() != null) {
                    production.setMstMachine(new MstMachine());
                    BeanCopyUtil.copyFields(detail.getProductionId().getMstMachine(), production.getMstMachine());
                }
                if (detail.getProductionId().getTblDirection() != null) {
                    production.setTblDirection(new TblDirection());
                    BeanCopyUtil.copyFields(detail.getProductionId().getTblDirection(), production.getTblDirection());
                }
                if (detail.getProductionId().getMstWorkPhase() != null) {
                    production.setMstWorkPhase(new MstWorkPhase());
                    BeanCopyUtil.copyFields(detail.getProductionId().getMstWorkPhase(), production.getMstWorkPhase());
                }
                TblProductionDetail firstDetail = new TblProductionDetail();
                BeanCopyUtil.copyFields(detail, firstDetail);
                if (detail.getMstComponent() != null) {
                    firstDetail.setMstComponent(new MstComponent());
                    BeanCopyUtil.copyFields(detail.getMstComponent(), firstDetail.getMstComponent());
                }
                production.setFirstDetail(firstDetail);
                response.getTblProductions().add(production);
            }
                                
            prevDetail = detail;
        }
        //response.setTblProductions(list);
        return response;
    }

    /**
     * 生産実績1件取得
     *
     * @param id
     * @return
     */
    public TblProductionList getProductionById(String id) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT DISTINCT tblProduction FROM TblProduction tblProduction"
                + " JOIN FETCH tblProduction.tblProductionDetailCollection dtl"
                + " JOIN FETCH dtl.tblProductionLotBalanceCollection"
                + " LEFT JOIN FETCH dtl.tblProductionMachineProcCond "
                + " WHERE 1=1 "
        );
        sql.append(" and tblProduction.id = :id ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("id", id);
        List list = query.getResultList();
        TblProductionList response = new TblProductionList();
        response.setTblProductions(list);
        return response;
    }

    /**
     * 生産実績1件取得(TblProduction型で返却)
     *
     * @param id
     * @return
     */
    public TblProduction getProductionSingleById(String id) {
        Query query = entityManager.createNamedQuery("TblProduction.findById");
        query.setParameter("id", id);
        try {
            return (TblProduction) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Long getProductionsCountByPrevLotNumber(String prevLotNumber) {
        StringBuilder sql;
        sql = new StringBuilder("SELECT count(t) FROM TblProduction t WHERE t.prevLotNumber = :prevLotNumber");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("prevLotNumber", prevLotNumber);
        List list = query.getResultList();

        return (Long) list.get(0);
    }

    /**
     * 生産実績テーブル登録
     *
     * @param tblProduction
     * @param loginUser
     * @return
     */
    @Transactional
    public TblProduction createTblProduction(TblProduction tblProduction, LoginUser loginUser) {
        // 登録処理時の強制設定パラメータセット
        tblProduction.setId(IDGenerator.generate());
        tblProduction.setCreateDate(new java.util.Date());
        tblProduction.setCreateUserUuid(loginUser.getUserUuid());
        tblProduction.setUpdateDate(tblProduction.getCreateDate());
        tblProduction.setUpdateUserUuid(loginUser.getUserUuid());
        tblProduction.setStatus(0);
        entityManager.persist(tblProduction);
        return tblProduction;
    }

    /**
     * 生産実績テーブル更新
     *
     * @param tblProduction
     * @param loginUser
     * @return
     */
    @Transactional
    public TblProduction updateTblProduction(TblProduction tblProduction, LoginUser loginUser) {
        tblProduction.setUpdateDate(new java.util.Date());
        tblProduction.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(tblProduction);
        return tblProduction;
    }
    
    /**
     * 最終生産日、累計生産時間、累計ショット数、メンテナンス後生産時間、メンテナンス後ショット数更新
     * 
     * @param response
     * @param deleteProduction
     * @param tblMachineDailyReportVo
     * @param loginUser 
     */
//    @Transactional
//    public void updateMoldOrMachineForProductionDel(BasicResponse response, TblProduction deleteProduction, TblMachineDailyReportVo tblMachineDailyReportVo, LoginUser loginUser) {
//        if (deleteProduction == null) {
//            setApplicationError(response, loginUser, "msg_error_data_deleted", "TblProduction");
//            return;
//        }
//        // 金型詳細情報を取得する
//        MstMold mold = deleteProduction.getMstMold();
//        // 設備詳細情報を取得する
//        MstMachine machine = deleteProduction.getMstMachine();
//        Query query;
//        if (mold != null) {
//            // 最終生産日、累計生産時間、累計ショット数更新
//            if (deleteProduction.getMoldUuid() != null && !"".equals(deleteProduction.getMoldUuid())) {
//                
//                // 最終生産日設定
//                mold.setLastProductionDate(getLastProductionDateAfterDelete(deleteProduction, 0));
//                
//                query = entityManager.createNamedQuery("TblMoldMaintenanceRemodeling.findMaxEndDateByMoldUuid");
//                query.setParameter("moldUuid", mold.getUuid());
//                query.setMaxResults(1);
//
//                List<TblMoldMaintenanceRemodeling> maxMainteEndList = query.getResultList();
//                int shotCount = 0, disposedShotCount = 0, netProducintTimeMinutes = 0;
//                // 終了した生産実績を削除する場合、生産実績のショット数、実稼働時間を使う計算する
//                if (deleteProduction.getEndDatetime() != null) {
//                    // 最大終了時間メンテナンス情報が存在する且つ削除した生産実績の終了時間はメンテナンス情報の最大終了時間より大きい場合、メンテナンス後ショット数、生産時間を計算する
//                    if (maxMainteEndList != null && !maxMainteEndList.isEmpty()) {
//                        if (deleteProduction.getEndDatetime().compareTo(maxMainteEndList.get(0).getEndDatetime()) > 0) {
//                            // 金型のみ指定され、生産実績を削除する場合、生産実績のショット数、実稼働時間を使う計算する
//                            if (machine == null) {
//                                mold.setAfterMainteTotalProducingTimeHour(FileUtil.getIntegerValue(mold.getAfterMainteTotalProducingTimeHour()) - FileUtil.getIntegerValue(deleteProduction.getNetProducintTimeMinutes()) / 60);
//                                mold.setAfterMainteTotalShotCount(FileUtil.getIntegerValue(mold.getAfterMainteTotalShotCount()) - deleteProduction.getShotCount() - deleteProduction.getDisposedShotCount());
//                                // 最大終了時間メンテナンス情報より大きい機械日報から
//                            } else {
//                                for (TblMachineDailyReportVo dailyReport : tblMachineDailyReportVo.getMachineDailyReportVos()) {
//                                    if (dailyReport.getEndDatetime().compareTo(maxMainteEndList.get(0).getEndDatetime()) > 0) {
//                                        if (dailyReport.getShotCount() != null && !"".equals(dailyReport.getShotCount())) {
//                                            shotCount = Integer.parseInt(dailyReport.getShotCount());
//                                        }
//                                        if (dailyReport.getDisposedShotCount() != null && !"".equals(dailyReport.getDisposedShotCount())) {
//                                            disposedShotCount = Integer.parseInt(dailyReport.getDisposedShotCount());
//                                        }
//                                        if (dailyReport.getNetProducintTimeMinutes() != null && !"".equals(dailyReport.getNetProducintTimeMinutes())) {
//                                            netProducintTimeMinutes = Integer.parseInt(dailyReport.getNetProducintTimeMinutes());
//                                        }
//                                        mold.setAfterMainteTotalProducingTimeHour(FileUtil.getIntegerValue(mold.getAfterMainteTotalProducingTimeHour()) - netProducintTimeMinutes / 60);
//                                        mold.setAfterMainteTotalShotCount(FileUtil.getIntegerValue(mold.getAfterMainteTotalShotCount()) - shotCount - disposedShotCount);
//                                    }
//                                }
//                            }
//                        }
//                    } else {
//                        // 最大終了時間メンテナンス情報が存在しない場合、削除した生産実績によりメンテナンス後ショット数、生産時間を計算する
//                        mold.setAfterMainteTotalProducingTimeHour(FileUtil.getIntegerValue(mold.getAfterMainteTotalProducingTimeHour()) - FileUtil.getIntegerValue(deleteProduction.getNetProducintTimeMinutes()) / 60);
//                        mold.setAfterMainteTotalShotCount(FileUtil.getIntegerValue(mold.getAfterMainteTotalShotCount()) - deleteProduction.getShotCount() - deleteProduction.getDisposedShotCount());
//                    }
//                    mold.setTotalProducingTimeHour(FileUtil.getIntegerValue(mold.getTotalProducingTimeHour()) - FileUtil.getIntegerValue(deleteProduction.getNetProducintTimeMinutes()) / 60);
//                    mold.setTotalShotCount(FileUtil.getIntegerValue(mold.getTotalShotCount()) - deleteProduction.getShotCount() - deleteProduction.getDisposedShotCount());
//                    mold.setUpdateDate(new Date());
//                    mold.setUpdateUserUuid(loginUser.getUserUuid());
//                    entityManager.merge(mold);
//                } else {
//                    // 終了していない生産実績を削除する場合、登録した機械日報により、金型マスタ情報更新する
//                    // 終了していない生産実績の日報が未登録の場合、何もしない
//                    if (tblMachineDailyReportVo != null && tblMachineDailyReportVo.getMachineDailyReportVos() != null && !tblMachineDailyReportVo.getMachineDailyReportVos().isEmpty()) {
//                        // 最大終了時間メンテナンス情報が存在する且つ機械日報存在する且つ機械日報の終了時間がメンテナンス情報最大終了時間より大きい場合、メンテナンス後ショット数、生産時間を計算する
//                        // メンテナンス情報最大終了時間より小さい場合、計算しない
//                        if (maxMainteEndList != null && !maxMainteEndList.isEmpty()) {
//                            for (TblMachineDailyReportVo dailyReport : tblMachineDailyReportVo.getMachineDailyReportVos()) {
//                                if (dailyReport.getShotCount() != null && !"".equals(dailyReport.getShotCount())) {
//                                    shotCount = Integer.parseInt(dailyReport.getShotCount());
//                                }
//                                if (dailyReport.getDisposedShotCount() != null && !"".equals(dailyReport.getDisposedShotCount())) {
//                                    disposedShotCount = Integer.parseInt(dailyReport.getDisposedShotCount());
//                                }
//                                if (dailyReport.getNetProducintTimeMinutes() != null && !"".equals(dailyReport.getNetProducintTimeMinutes())) {
//                                    netProducintTimeMinutes = Integer.parseInt(dailyReport.getNetProducintTimeMinutes());
//                                }
//                                if (dailyReport.getEndDatetime().compareTo(maxMainteEndList.get(0).getEndDatetime()) > 0) {
//                                    mold.setAfterMainteTotalProducingTimeHour(FileUtil.getIntegerValue(mold.getAfterMainteTotalProducingTimeHour()) - netProducintTimeMinutes / 60);
//                                    mold.setAfterMainteTotalShotCount(FileUtil.getIntegerValue(mold.getAfterMainteTotalShotCount()) - shotCount - disposedShotCount);
//                                }
//                                mold.setTotalProducingTimeHour(FileUtil.getIntegerValue(mold.getTotalProducingTimeHour()) - netProducintTimeMinutes / 60);
//                                mold.setTotalShotCount(FileUtil.getIntegerValue(mold.getTotalShotCount()) - shotCount - disposedShotCount);
//                            }
//                            // 最大終了時間メンテナンス情報が存在しない場合、機械日報により金型マスタ情報更新する
//                        } else {
//                            for (TblMachineDailyReportVo dailyReport : tblMachineDailyReportVo.getMachineDailyReportVos()) {
//                                if (dailyReport.getShotCount() != null && !"".equals(dailyReport.getShotCount())) {
//                                    shotCount = Integer.parseInt(dailyReport.getShotCount());
//                                }
//                                if (dailyReport.getDisposedShotCount() != null && !"".equals(dailyReport.getDisposedShotCount())) {
//                                    disposedShotCount = Integer.parseInt(dailyReport.getDisposedShotCount());
//                                }
//                                if (dailyReport.getNetProducintTimeMinutes() != null && !"".equals(dailyReport.getNetProducintTimeMinutes())) {
//                                    netProducintTimeMinutes = Integer.parseInt(dailyReport.getNetProducintTimeMinutes());
//                                }
//                                mold.setAfterMainteTotalProducingTimeHour(FileUtil.getIntegerValue(mold.getAfterMainteTotalProducingTimeHour()) - netProducintTimeMinutes / 60);
//                                mold.setAfterMainteTotalShotCount(FileUtil.getIntegerValue(mold.getAfterMainteTotalShotCount()) - shotCount - disposedShotCount);
//                                mold.setTotalProducingTimeHour(FileUtil.getIntegerValue(mold.getTotalProducingTimeHour()) - netProducintTimeMinutes / 60);
//                                mold.setTotalShotCount(FileUtil.getIntegerValue(mold.getTotalShotCount()) - shotCount - disposedShotCount);
//                            }
//                        }
//                        mold.setUpdateDate(new Date());
//                        mold.setUpdateUserUuid(loginUser.getUserUuid());
//                        entityManager.merge(mold);
//                    }
//                }
//            }
//        }
//        
//        // 上記金型ロジックと一緒
//        if (machine != null) {
//            if (deleteProduction.getMachineUuid()!= null && !"".equals(deleteProduction.getMachineUuid())) {
//                
//                machine.setLastProductionDate(getLastProductionDateAfterDelete(deleteProduction, 1));
//                
//                query = entityManager.createNamedQuery("TblMachineMaintenanceRemodeling.findMaxEndDateByMachineUuid");
//                query.setParameter("machineUuid", machine.getUuid());
//                query.setMaxResults(1);
//
//                List<TblMachineMaintenanceRemodeling> maxMainteEndList = query.getResultList();
//                
//                int shotCount = 0, disposedShotCount = 0, netProducintTimeMinutes = 0;
//                if (deleteProduction.getEndDatetime() != null) {
//                    if (maxMainteEndList != null && !maxMainteEndList.isEmpty()) {
//                        if (deleteProduction.getEndDatetime().compareTo(maxMainteEndList.get(0).getEndDatetime()) > 0) {
//                            for (TblMachineDailyReportVo dailyReport : tblMachineDailyReportVo.getMachineDailyReportVos()) {
//                                if (dailyReport.getEndDatetime().compareTo(maxMainteEndList.get(0).getEndDatetime()) > 0) {
//                                    if (dailyReport.getShotCount() != null && !"".equals(dailyReport.getShotCount())) {
//                                        shotCount = Integer.parseInt(dailyReport.getShotCount());
//                                    }
//                                    if (dailyReport.getDisposedShotCount() != null && !"".equals(dailyReport.getDisposedShotCount())) {
//                                        disposedShotCount = Integer.parseInt(dailyReport.getDisposedShotCount());
//                                    }
//                                    if (dailyReport.getNetProducintTimeMinutes() != null && !"".equals(dailyReport.getNetProducintTimeMinutes())) {
//                                        netProducintTimeMinutes = Integer.parseInt(dailyReport.getNetProducintTimeMinutes());
//                                    }
//                                    machine.setAfterMainteTotalProducingTimeHour(FileUtil.getIntegerValue(machine.getAfterMainteTotalProducingTimeHour()) - netProducintTimeMinutes / 60);
//                                    machine.setAfterMainteTotalShotCount(FileUtil.getIntegerValue(machine.getAfterMainteTotalShotCount()) - shotCount - disposedShotCount);
//                                }
//                            }
//                        }
//                    } else {
//                        machine.setAfterMainteTotalProducingTimeHour(FileUtil.getIntegerValue(machine.getAfterMainteTotalProducingTimeHour()) - FileUtil.getIntegerValue(deleteProduction.getNetProducintTimeMinutes()) / 60);
//                        machine.setAfterMainteTotalShotCount(FileUtil.getIntegerValue(machine.getAfterMainteTotalShotCount()) - deleteProduction.getShotCount() - deleteProduction.getDisposedShotCount());
//                    }
//                    machine.setTotalProducingTimeHour(FileUtil.getIntegerValue(machine.getTotalProducingTimeHour()) - FileUtil.getIntegerValue(deleteProduction.getNetProducintTimeMinutes()) / 60);
//                    machine.setTotalShotCount(FileUtil.getIntegerValue(machine.getTotalShotCount()) - deleteProduction.getShotCount() - deleteProduction.getDisposedShotCount());
//                    machine.setUpdateDate(new Date());
//                    machine.setUpdateUserUuid(loginUser.getUserUuid());
//                    entityManager.merge(machine);
//                } else {
//                    if (tblMachineDailyReportVo != null && tblMachineDailyReportVo.getMachineDailyReportVos() != null && !tblMachineDailyReportVo.getMachineDailyReportVos().isEmpty()) {
//                        if (maxMainteEndList != null && !maxMainteEndList.isEmpty()) {
//                            for (TblMachineDailyReportVo dailyReport : tblMachineDailyReportVo.getMachineDailyReportVos()) {
//                                if (dailyReport.getShotCount() != null && !"".equals(dailyReport.getShotCount())) {
//                                    shotCount = Integer.parseInt(dailyReport.getShotCount());
//                                }
//                                if (dailyReport.getDisposedShotCount() != null && !"".equals(dailyReport.getDisposedShotCount())) {
//                                    disposedShotCount = Integer.parseInt(dailyReport.getDisposedShotCount());
//                                }
//                                if (dailyReport.getNetProducintTimeMinutes() != null && !"".equals(dailyReport.getNetProducintTimeMinutes())) {
//                                    netProducintTimeMinutes = Integer.parseInt(dailyReport.getNetProducintTimeMinutes());
//                                }
//                                if (dailyReport.getEndDatetime().compareTo(maxMainteEndList.get(0).getEndDatetime()) > 0) {
//                                    machine.setAfterMainteTotalProducingTimeHour(FileUtil.getIntegerValue(machine.getAfterMainteTotalProducingTimeHour()) - netProducintTimeMinutes / 60);
//                                    machine.setAfterMainteTotalShotCount(FileUtil.getIntegerValue(machine.getAfterMainteTotalShotCount()) - shotCount - disposedShotCount);
//                                }
//                                machine.setTotalProducingTimeHour(FileUtil.getIntegerValue(machine.getTotalProducingTimeHour()) - netProducintTimeMinutes / 60);
//                                machine.setTotalShotCount(FileUtil.getIntegerValue(machine.getTotalShotCount()) - shotCount - disposedShotCount);
//                            }
//                        } else {
//                            for (TblMachineDailyReportVo dailyReport : tblMachineDailyReportVo.getMachineDailyReportVos()) {
//                                if (dailyReport.getShotCount() != null && !"".equals(dailyReport.getShotCount())) {
//                                    shotCount = Integer.parseInt(dailyReport.getShotCount());
//                                }
//                                if (dailyReport.getDisposedShotCount() != null && !"".equals(dailyReport.getDisposedShotCount())) {
//                                    disposedShotCount = Integer.parseInt(dailyReport.getDisposedShotCount());
//                                }
//                                if (dailyReport.getNetProducintTimeMinutes() != null && !"".equals(dailyReport.getNetProducintTimeMinutes())) {
//                                    netProducintTimeMinutes = Integer.parseInt(dailyReport.getNetProducintTimeMinutes());
//                                }
//                                machine.setAfterMainteTotalProducingTimeHour(FileUtil.getIntegerValue(machine.getAfterMainteTotalProducingTimeHour()) - netProducintTimeMinutes / 60);
//                                machine.setAfterMainteTotalShotCount(FileUtil.getIntegerValue(machine.getAfterMainteTotalShotCount()) - shotCount - disposedShotCount);
//                                machine.setTotalProducingTimeHour(FileUtil.getIntegerValue(machine.getTotalProducingTimeHour()) - netProducintTimeMinutes / 60);
//                                machine.setTotalShotCount(FileUtil.getIntegerValue(machine.getTotalShotCount()) - shotCount - disposedShotCount);
//                            }
//                        }
//                        machine.setUpdateDate(new Date());
//                        machine.setUpdateUserUuid(loginUser.getUserUuid());
//                        entityManager.merge(machine);
//                    }
//                }
//            }
//        }
//    }
    
    /**
     * 最終生産日取得(生産実績、機械日報削除時)
     * 
     * @param deleteProduction
     * @param moldMachineFlg 0:金型、1:設備
     * @return 最終生産日
     */
    public Date getLastProductionDateAfterDelete(TblProduction deleteProduction, int moldMachineFlg) {
        Query query;
        List<TblProduction> maxEndList;
        List<TblMachineDailyReport> maxEndDailyReportList;
        if (moldMachineFlg == 0) {
            // 該当金型すでに終了した生産実績の最大終了日時取得
            query = entityManager.createNamedQuery("TblProduction.findMaxEndDateByMoldUuid");
            query.setParameter("moldUuid", deleteProduction.getMoldUuid());
            query.setMaxResults(1);
            maxEndList = query.getResultList();

            // 該当金型終了していない生産実績の最大機械日報終了日時取得
            query = entityManager.createNamedQuery("TblMachineDailyReport.findMaxMoldEndDateTimeForNotEndProdunction");
            query.setParameter("moldUuid", deleteProduction.getMoldUuid());
            query.setMaxResults(1);
            maxEndDailyReportList = query.getResultList();
        } else {
            // 該当設備すでに終了した生産実績の最大終了日時取得
            query = entityManager.createNamedQuery("TblProduction.findMaxEndDateByMachineUuid");
            query.setParameter("machineUuid", deleteProduction.getMachineUuid());
            query.setMaxResults(1);
            maxEndList = query.getResultList();

            // 該当設備終了していない生産実績の最大機械日報終了日時取得
            query = entityManager.createNamedQuery("TblMachineDailyReport.findMaxMachineEndDateTimeForNotEndProdunction");
            query.setParameter("machineUuid", deleteProduction.getMachineUuid());
            query.setMaxResults(1);
            maxEndDailyReportList = query.getResultList();
        }

        if (maxEndList != null && !maxEndList.isEmpty()) {
            CnfSystem cnfSystem = cnfSystemService.findByKey("system", "business_start_time");
            Date businessDate = DateFormat.strToDate(DateFormat.getBusinessDate(DateFormat.dateToStr(maxEndList.get(0).getEndDatetime(), DateFormat.DATETIME_FORMAT), cnfSystem));
            if (maxEndDailyReportList != null && !maxEndDailyReportList.isEmpty() && businessDate != null && businessDate.compareTo(maxEndDailyReportList.get(0).getTblMachineDailyReportPK().getProductionDate()) < 0) {
                return maxEndDailyReportList.get(0).getTblMachineDailyReportPK().getProductionDate();
            } else {
                return businessDate;
            }
        } else {
            if (maxEndDailyReportList != null && !maxEndDailyReportList.isEmpty()) {
                return maxEndDailyReportList.get(0).getTblMachineDailyReportPK().getProductionDate();
            } else {
                return null;
            }
        }
    }

    /**
     * 生産入力登録データ設定
     *
     * @param response
     * @param tblProductionVo
     * @param lotNumber
     * @param loginUser
     * @return
     */
    public TblProduction setCreateDataForPoductionStart(
            BasicResponse response, TblProductionVo tblProductionVo, String lotNumber, LoginUser loginUser
    ) {
        TblProduction tblProduction = new TblProduction();

        /**
         * 必須チェックおよび値設定
         */
        // 生産日
        if (tblProductionVo.getProductionDate() == null) {
            setApplicationError(response, loginUser, "msg_error_not_null", "productionDate");
            return tblProduction;
        }
        tblProduction.setProductionDate(tblProductionVo.getProductionDate());

        // 手配・工事ID directionCode ⇒ moldId
        if (tblProductionVo.getDirectionCode() != null && !"".equals(tblProductionVo.getDirectionCode())) {
            // 手配テーブル存在チェック(手配テーブル.手配・工事番号による存在チェック)および値セット
            TblDirection tblDirection = tblDirectionService.getTblDirectionByDirectionCode(tblProductionVo.getDirectionCode());
            if (tblDirection != null) {
                tblProduction.setDirectionId(tblDirection.getId());
                tblProduction.setDirectionCode(tblProductionVo.getDirectionCode());
            }
        }

        // 金型UUID moldId ⇒ moldUuid
        if (tblProductionVo.getMoldId() != null && !"".equals(tblProductionVo.getMoldId())) {
            // 金型マスタ存在チェック(金型マスタ.金型IDによる存在チェック)および値セット
            MstMold mstMold = mstMoldService.getMstMoldByMoldId(tblProductionVo.getMoldId());
            if (mstMold != null) {
                tblProduction.setMoldUuid(mstMold.getUuid());
                tblProduction.setMoldId(tblProductionVo.getMoldId());
            }
        }

        // 設備UUID machineUuid
        if (tblProductionVo.getMachineUuid() != null && !"".equals(tblProductionVo.getMachineUuid())) {
            // 金型マスタ存在チェック(金型マスタ.金型IDによる存在チェック)および値セット
            MstMachine mstMachine = mstMachineService.getMstMachineByUuid(tblProductionVo.getMachineUuid());
            if (mstMachine != null) {
                tblProduction.setMachineUuid(mstMachine.getUuid());
            }
        }

        // 作業工程Id workPhaseId
        if (tblProductionVo.getWorkPhaseId() != null && !"".equals(tblProductionVo.getWorkPhaseId())) {
            // 金型マスタ存在チェック(金型マスタ.金型IDによる存在チェック)および値セット
            MstWorkPhase mstWorkPhase = mstWorkPhaseService.getMstWorkPhaseByChoiceSeq(tblProductionVo.getWorkPhaseChoiceSeq());
            if (mstWorkPhase != null) {
                tblProduction.setWorkPhaseId(mstWorkPhase.getId());
            }
        }

        /*
         * その他登録必須項目変換および設定
         */
        // 前ロット番号
        tblProduction.setPrevLotNumber(tblProductionVo.getPrevLotNumber());
        // ロット番号
        tblProduction.setLotNumber(lotNumber);
        // ショット数
        tblProduction.setShotCount(0);
        // 捨てショット数
        tblProduction.setDisposedShotCount(tblProductionVo.getDisposedShotCount());
        // 生産者UUIDをログインユーザーより取得し設定
        tblProduction.setPersonUuid(loginUser.getUserUuid());

        // 開始日時
        // 値がない場合は強制設定
        if (tblProductionVo.getStartDatetime() == null) {
            // ログインユーザーのタイムゾーン
            tblProduction.setStartDatetime(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
            // サーバーのタイムゾーン
            tblProduction.setStartDatetimeStz(new java.util.Date());
        } // 値がある場合はSTZの方を入力値でサーバ時間に変換して設定し直し
        else {
            tblProduction.setStartDatetime(tblProductionVo.getStartDatetime());
            tblProduction.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblProduction.getStartDatetime()));
        }
        
        // 生産場所
        if (tblProductionVo.getProdDepartment() != null && tblProductionVo.getProdDepartment() != 0) {
            tblProduction.setProdDepartment(tblProductionVo.getProdDepartment()); 
        } else {
            tblProduction.setProdDepartment(loginUser.getDepartment() == null ? null : Integer.valueOf(loginUser.getDepartment()));
        }
        return tblProduction;
    }

    /**
     * 生産入力更新データ設定
     *
     * @param response
     * @param tblProduction
     * @param tblProductionVo
     * @param tblMachineDailyReportDetailList
     * @param loginUser
     * @return
     */
    public TblProduction setUpdateDataForPoductionEnd(
            BasicResponse response, TblProduction tblProduction, TblProductionVo tblProductionVo, 
            TblMachineDailyReportDetailList tblMachineDailyReportDetailList, LoginUser loginUser
    ) {
        /**
         * 必須チェックおよび値設定
         */
        // 生産日
        if (tblProductionVo.getProductionDate() == null) {
            setApplicationError(response, loginUser, "msg_error_not_null", "productionDate");
            return tblProduction;
        }
        tblProduction.setProductionDate(tblProductionVo.getProductionDate());

        // 開始日時
        if (tblProductionVo.getStartDatetime() == null) {
            setApplicationError(response, loginUser, "msg_error_not_null", "startDatetime");
            return tblProduction;
        }
        tblProduction.setStartDatetime(tblProductionVo.getStartDatetime());

        // 終了日時
        if (tblProductionVo.getEndDatetime() == null) {
            setApplicationError(response, loginUser, "msg_error_not_null", "endDatetime");
            return tblProduction;
        }
        tblProduction.setEndDatetime(tblProductionVo.getEndDatetime());

        /**
         * コード値変換
         */
        // 手配・工事ID directionCode ⇒ moldId
        tblProduction.setDirectionId(tblProductionVo.getDirectionId());
        if (tblProductionVo.getDirectionCode() != null && !"".equals(tblProductionVo.getDirectionCode())) {
            // 手配テーブル存在チェック(手配テーブル.手配・工事番号による存在チェック)および値セット
            TblDirection tblDirection = tblDirectionService.getTblDirectionByDirectionCode(tblProductionVo.getDirectionCode());
            if (tblDirection != null) {
                tblProduction.setDirectionId(tblDirection.getId());
                tblProduction.setDirectionCode(tblProductionVo.getDirectionCode());
            }
        }

        // 金型UUID moldId ⇒ moldUuid
        tblProduction.setMoldUuid(tblProductionVo.getMoldUuid());
        if (tblProductionVo.getMoldId() != null && !"".equals(tblProductionVo.getMoldId())) {
            // 金型マスタ存在チェック(金型マスタ.金型IDによる存在チェック)および値セット
            MstMold mstMold = mstMoldService.getMstMoldByMoldId(tblProductionVo.getMoldId());
            if (mstMold != null) {
                tblProduction.setMoldUuid(mstMold.getUuid());
                tblProduction.setMoldId(tblProductionVo.getMoldId());
            }
        }

        // 設備UUID machineUuid
        if (tblProductionVo.getMachineUuid() != null && !"".equals(tblProductionVo.getMachineUuid())) {
            // 金型マスタ存在チェック(金型マスタ.金型IDによる存在チェック)および値セット
            MstMachine mstMachine = mstMachineService.getMstMachineByUuid(tblProductionVo.getMachineUuid());
            if (mstMachine != null) {
                tblProduction.setMachineUuid(mstMachine.getUuid());
            }
        }

        // 作業工程Id workPhaseId
        if (tblProductionVo.getWorkPhaseChoiceSeq() != null && !"".equals(tblProductionVo.getWorkPhaseChoiceSeq())) {
            // 金型マスタ存在チェック(金型マスタ.金型IDによる存在チェック)および値セット
            MstWorkPhase mstWorkPhase = mstWorkPhaseService.getMstWorkPhaseByChoiceSeq(tblProductionVo.getWorkPhaseChoiceSeq());
            if (mstWorkPhase != null) {
                tblProduction.setWorkPhaseId(mstWorkPhase.getId());
            }
        }

        /*
         * その他更新項目設定
         */
        // 機械日報から生産終了時、ショット数と捨てショット数は機械日報から加算する
        if (tblProductionVo.getProductionEndFlg() && tblMachineDailyReportDetailList != null) {
            int shotCount = 0, disposedShotCount = 0, producingTimeMinutes = 0;
            if (tblMachineDailyReportDetailList.getTblMachineDailyReportDetails() != null && !tblMachineDailyReportDetailList.getTblMachineDailyReportDetails().isEmpty()) {
                Date oldProductionDate = DateFormat.strToDate("1900/01/01");
                for (TblMachineDailyReportDetail detail : tblMachineDailyReportDetailList.getTblMachineDailyReportDetails()) {
                    if (detail.getTblMachineDailyReport().getTblMachineDailyReportPK().getProductionDate().compareTo(oldProductionDate) != 0) {
                        shotCount += detail.getTblMachineDailyReport().getShotCount();
                        disposedShotCount += detail.getTblMachineDailyReport().getDisposedShotCount();
                        producingTimeMinutes += (FileUtil.getIntegerValue(detail.getTblMachineDailyReport().getProducingTimeMinutes()));
                        oldProductionDate = detail.getTblMachineDailyReport().getTblMachineDailyReportPK().getProductionDate();
                    }
                }
            }
            // ショット数
            tblProduction.setShotCount(shotCount);
            // 捨てショット数
            tblProduction.setDisposedShotCount(disposedShotCount);
            // 生産時間(分)
            tblProduction.setProducingTimeMinutes(producingTimeMinutes);
        } else {
            // ショット数
            tblProduction.setShotCount(tblProductionVo.getShotCount());
            // 捨てショット数
            tblProduction.setDisposedShotCount(tblProductionVo.getDisposedShotCount());
            // 生産時間(分)
            tblProduction.setProducingTimeMinutes(tblProductionVo.getProducingTimeMinutes());
        }

        // 開始日時
        // 値がない場合は強制設定
        if (tblProduction.getStartDatetime() == null) {
            // ログインユーザーのタイムゾーン
            tblProduction.setStartDatetime(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
            // サーバーのタイムゾーン
            tblProduction.setStartDatetimeStz(new java.util.Date());
        } // 値がある場合はSTZの方を入力値でサーバ時間に変換して設定し直し
        else {
            tblProduction.setStartDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblProduction.getStartDatetime()));
        }

        // 終了日時
        // 値がない場合は強制設定
        if (tblProduction.getEndDatetime() == null) {
            // ログインユーザーのタイムゾーン
            tblProduction.setEndDatetime(TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()));
            // サーバーのタイムゾーン
            tblProduction.setEndDatetimeStz(new java.util.Date());
        } // 値がある場合はSTZの方を入力値でサーバ時間に変換して設定し直し
        else {
            tblProduction.setEndDatetimeStz(TimezoneConverter.toSystemDefaultZoneTime(loginUser.getJavaZoneId(), tblProduction.getEndDatetime()));
        }
        
        //set netProducintTimeMinutes suspendedTimeMinutes
        tblProduction.setNetProducintTimeMinutes(tblProductionVo.getNetProducintTimeMinutes());
        tblProduction.setSuspendedTimeMinutes(tblProductionVo.getSuspendedTimeMinutes());
        
        // 製造ロット番号(一行目部品ロット番号で設定)
        // 生産実績詳細がないパターンがResourceで判断された
        tblProduction.setLotNumber(tblProductionVo.getTblProductionDetailVos().get(0).getlotNumber());
        
//        tblProduction.setStatus(9);
        
        return tblProduction;
    }

    private BasicResponse setApplicationError(BasicResponse response, LoginUser loginUser, String dictKey, String errorContentsName) {
        setBasicResponseError(
                response, true, ErrorMessages.E201_APPLICATION, mstDictionaryService.getDictionaryValue(loginUser.getLangId(), dictKey)
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

    /**
     * 金型詳細でその金型の生産履歴取得
     *
     * @param moldId
     * @param loginUser
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public TblProductionList getProductionHistoryByMoldId(String moldId, LoginUser loginUser, int pageNumber, int pageSize) {
        TblProductionList response = new TblProductionList();
        Pager pager = new Pager();

        int isExternal = 0;
        if (FileUtil.checkExternal(entityManager, mstDictionaryService, moldId, loginUser).isError()) {
            isExternal = 1;
        }
        
        Query countQuery = makeMoldQuery(true, isExternal, moldId, pageNumber, pageSize);
        int recCount = ((Long)countQuery.getSingleResult()).intValue();
        Query recordQuery = makeMoldQuery(false, isExternal, moldId, pageNumber, pageSize);
        
        List list = recordQuery.getResultList();
        response.setCount(recCount);
        response.setPageNumber(pageNumber);
        response.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + recCount)));

        List<TblProductionVo> tblProductionVoList = new ArrayList<>();
        TblProductionVo tblProductionVo;
        for (int i = 0; i < list.size(); i++) {
            TblProductionDetail tblProductionDetail = (TblProductionDetail) list.get(i);
            TblProduction tblProduction = tblProductionDetail.getTblProduction();
            tblProductionVo = new TblProductionVo();
            tblProductionVo.setId(tblProductionDetail.getId());
            Date productionDate = tblProduction.getProductionDate();
            if (null != productionDate) {
                tblProductionVo.setProductionDate(productionDate); //生産日
                tblProductionVo.setProductionDateStr(new FileUtil().getDateFormatForStr(productionDate));
            } else {
                tblProductionVo.setProductionDate(null); //生産日
                tblProductionVo.setProductionDateStr("");
            }
            Date startDatetime = tblProduction.getStartDatetime();
            if (null != startDatetime) {
                tblProductionVo.setStartDatetimeStr(new FileUtil().getDateTimeFormatForStr(startDatetime));
                tblProductionVo.setStartDatetimeStzStr(new FileUtil().getDateTimeFormatForStr(startDatetime));
            } else {
                tblProductionVo.setStartDatetimeStr("-");
                tblProductionVo.setStartDatetimeStzStr("-");
            }
            if (null != tblProduction.getEndDatetime()) {
                tblProductionVo.setEndDatetimeStr(new FileUtil().getDateTimeFormatForStr(tblProduction.getEndDatetime()));
                tblProductionVo.setEndDatetimeStzStr(new FileUtil().getDateTimeFormatForStr(tblProduction.getEndDatetimeStz()));
            } else {
                tblProductionVo.setEndDatetimeStr("-");
                tblProductionVo.setEndDatetimeStzStr("-");
            }
            MstProcedure mstProcedure = tblProductionDetail.getMstProcedure();
            if (null != mstProcedure) {
                tblProductionVo.setProcedureId(mstProcedure.getId());
                tblProductionVo.setProcedureName(mstProcedure.getProcedureName());  //工程名称
            } else {
                tblProductionVo.setProcedureId("");
                tblProductionVo.setProcedureName("");  //工程名称
            }

            tblProductionVo.setProcCd(tblProduction.getProcCd());
            tblProductionVo.setPrevLotNumber(tblProduction.getLotNumber() == null ? "" : tblProduction.getLotNumber()); //ロット番号
            tblProductionVo.setShotCount(tblProduction.getShotCount()); //ショット数

            if (tblProductionDetail.getMstComponent() != null) {
                tblProductionVo.setComponentCode(tblProductionDetail.getMstComponent().getComponentCode()); //部品コード
                tblProductionVo.setComponentName(tblProductionDetail.getMstComponent().getComponentName()); //部品名称
            } else {
                tblProductionVo.setComponentCode("");
                tblProductionVo.setComponentName("");
            }
            tblProductionVoList.add(tblProductionVo);
        }

        response.setTblProductionVo(tblProductionVoList);
        return response;
    }

    private Query makeMoldQuery(boolean countQuery, int isExternal, String moldId, int pageNumber, int pageSize){
        StringBuilder sql;
        Pager pager = new Pager();
        if(countQuery){
            sql = new StringBuilder("SELECT count(tblProductionDetail.id) ");
        } else {
            sql = new StringBuilder("SELECT tblProductionDetail ");
        }
        sql = sql.append(
                "FROM TblProductionDetail tblProductionDetail"
                + " JOIN FETCH tblProductionDetail.tblProduction"
                + " LEFT JOIN FETCH tblProductionDetail.mstComponent"
                + " LEFT JOIN FETCH tblProductionDetail.mstProcedure"
                + " LEFT JOIN FETCH tblProductionDetail.tblProduction.mstMold"
                + " WHERE 1=1 AND tblProductionDetail.tblProduction.mstMold.moldId = :moldId"
                + " AND tblProductionDetail.mstProcedure.externalFlg = :externalFlg Order By tblProductionDetail.tblProduction.productionDate DESC "
        );

        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("moldId", moldId)
                .setParameter("externalFlg", isExternal);
        //システム設定の一覧画面最大件数をデータ取得の上限とする。 
        if(!countQuery && pageNumber > 0 && pageSize > 0){
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }
        
        return query;
    };
    
    /**
     * 設備詳細でその設備の生産履歴取得
     *
     * @param machineId
     * @param loginUser
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public TblProductionList getProductionHistoryByMachineId(String machineId, LoginUser loginUser, int pageNumber, int pageSize) {
        TblProductionList response = new TblProductionList();
        Pager pager = new Pager();
        
        int isExternal = 0;
        if (FileUtil.checkMachineExternal(entityManager, mstDictionaryService, machineId, "", loginUser).isError()) {
            isExternal = 1;
        }
        
        Query countQuery = makeMachineQuery(true, isExternal, machineId, pageNumber, pageSize);
        int recCount = ((Long)countQuery.getSingleResult()).intValue();
        Query recordQuery = makeMachineQuery(false, isExternal, machineId, pageNumber, pageSize);
        
        List list = recordQuery.getResultList();
        response.setCount(recCount);
        response.setPageNumber(pageNumber);
        response.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + recCount)));
        
        List<TblProductionVo> tblProductionVoList = new ArrayList<>();
        TblProductionVo tblProductionVo;
        for (int i = 0; i < list.size(); i++) {
            TblProductionDetail tblProductionDetail = (TblProductionDetail) list.get(i);
            TblProduction tblProduction = tblProductionDetail.getTblProduction();
            tblProductionVo = new TblProductionVo();
            tblProductionVo.setId(tblProductionDetail.getId());
            Date productionDate = tblProduction.getProductionDate();
            if (null != productionDate) {
                tblProductionVo.setProductionDate(productionDate); //生産日
                tblProductionVo.setProductionDateStr(new FileUtil().getDateFormatForStr(productionDate));
            } else {
                tblProductionVo.setProductionDate(null); //生産日
                tblProductionVo.setProductionDateStr("");
            }
            Date startDatetime = tblProduction.getStartDatetime();
            if (null != startDatetime) {
                tblProductionVo.setStartDatetimeStr(new FileUtil().getDateTimeFormatForStr(startDatetime));
                tblProductionVo.setStartDatetimeStzStr(new FileUtil().getDateTimeFormatForStr(startDatetime));
            } else {
                tblProductionVo.setStartDatetimeStr("-");
                tblProductionVo.setStartDatetimeStzStr("-");
            }
            if (null != tblProduction.getEndDatetime()) {
                tblProductionVo.setEndDatetimeStr(new FileUtil().getDateTimeFormatForStr(tblProduction.getEndDatetime()));
                tblProductionVo.setEndDatetimeStzStr(new FileUtil().getDateTimeFormatForStr(tblProduction.getEndDatetimeStz()));
            } else {
                tblProductionVo.setEndDatetimeStr("-");
                tblProductionVo.setEndDatetimeStzStr("-");
            }
            MstProcedure mstProcedure = tblProductionDetail.getMstProcedure();
            if (null != mstProcedure) {
                tblProductionVo.setProcedureId(mstProcedure.getId());
                tblProductionVo.setProcedureName(mstProcedure.getProcedureName());  //工程名称
            } else {
                tblProductionVo.setProcedureId("");
                tblProductionVo.setProcedureName("");  //工程名称
            }

            tblProductionVo.setProcCd(tblProduction.getProcCd());
            tblProductionVo.setPrevLotNumber(tblProduction.getLotNumber() == null ? "" : tblProduction.getLotNumber()); //ロット番号
            tblProductionVo.setShotCount(tblProduction.getShotCount()); //ショット数

            if (tblProductionDetail.getMstComponent() != null) {
                tblProductionVo.setComponentCode(tblProductionDetail.getMstComponent().getComponentCode()); //部品コード
                tblProductionVo.setComponentName(tblProductionDetail.getMstComponent().getComponentName()); //部品名称
            } else {
                tblProductionVo.setComponentCode("");
                tblProductionVo.setComponentName("");
            }
            tblProductionVoList.add(tblProductionVo);
        }

        response.setTblProductionVo(tblProductionVoList);
        return response;
    }

    private Query makeMachineQuery(boolean countQuery, int isExternal, String machineId, int pageNumber, int pageSize){
        StringBuilder sql;
        Pager pager = new Pager();
        if(countQuery){
            sql = new StringBuilder("SELECT count(tblProductionDetail.id) ");
        } else {
            sql = new StringBuilder("SELECT tblProductionDetail ");
        }
        sql = sql.append(
                "FROM TblProductionDetail tblProductionDetail"
                + " JOIN FETCH tblProductionDetail.tblProduction"
                + " LEFT JOIN FETCH tblProductionDetail.mstComponent"
                + " LEFT JOIN FETCH tblProductionDetail.mstProcedure"
                + " LEFT JOIN FETCH tblProductionDetail.tblProduction.mstMachine"
                + " WHERE 1=1 AND tblProductionDetail.tblProduction.mstMachine.machineId = :machineId"
                + " AND tblProductionDetail.mstProcedure.externalFlg = :externalFlg Order By tblProductionDetail.tblProduction.productionDate DESC "
        );
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("machineId", machineId)
                .setParameter("externalFlg", isExternal);
        //システム設定の一覧画面最大件数をデータ取得の上限とする。 
        if(!countQuery && pageNumber > 0 && pageSize > 0){
            query.setFirstResult(pager.getStartRow(pageNumber, pageSize));
            query.setMaxResults(pageSize);
        }
        
        return query;
    };
    
    /**
     * バッチで生産実績データを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblProductionList getExtProductionsByBatch(String latestExecutedDate, String moldUuid) {
        TblProductionList resList = new TblProductionList();
        List<TblProductionVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct p FROM TblProduction p JOIN MstApiUser u on u.companyId = p.mstMold.ownerCompanyId WHERE 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and p.mstMold.uuid = :moldUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (p.updateDate > :latestExecutedDate or p.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            query.setParameter("moldUuid", moldUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblProduction> tmpList = query.getResultList();
        for (TblProduction production : tmpList) {
            TblProductionVo aRes = new TblProductionVo();

            aRes.setMoldId(production.getMstMold().getMoldId());
            production.setMstMold(null);
            production.setMstUser(null);
            production.setTblDirection(null);
            production.setTblProductionDetailCollection(null);
            aRes.setTblProduction(production);
            resVo.add(aRes);
        }
        resList.setTblProductionVo(resVo);
        return resList;
    }

    /**
     * バッチで設備生産実績データを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @return
     */
    TblProductionList getExtMachineProductionsByBatch(String latestExecutedDate, String machineUuid) {
        TblProductionList resList = new TblProductionList();
        List<TblProductionVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct p FROM TblProduction p JOIN MstApiUser u on u.companyId = p.mstMachine.ownerCompanyId WHERE 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and p.mstMachine.uuid = :machineUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (p.updateDate > :latestExecutedDate or p.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblProduction> tmpList = query.getResultList();
        for (TblProduction production : tmpList) {
            TblProductionVo aRes = new TblProductionVo();

            if (null != production.getMstMold()) {
                aRes.setMoldId(production.getMstMold().getMoldId());
            }
            production.setMstMold(null);
            if (null != production.getMstMachine()) {
                aRes.setMachineId(production.getMstMachine().getMachineId());
            }
            production.setMstMachine(null);

            production.setMstUser(null);
            production.setTblDirection(null);
            production.setTblProductionDetailCollection(null);
            aRes.setTblProduction(production);
            resVo.add(aRes);
        }
        resList.setTblProductionVo(resVo);
        return resList;
    }

    /**
     * バッチで生産実績データを更新
     *
     * @param procedureVos
     * @return
     */
    @Transactional
    public BasicResponse updateExtProductionsByBatch(List<TblProductionVo> procedureVos) {
        
        BasicResponse response = new BasicResponse();

        if (procedureVos != null && !procedureVos.isEmpty()) {
            for (TblProductionVo productionVo : procedureVos) {
                List<TblProduction> oldProductions = entityManager.createQuery("SELECT t FROM TblProduction t WHERE 1=1 and t.id=:id ")
                        .setParameter("id", productionVo.getTblProduction().getId())
                        .setMaxResults(1)
                        .getResultList();
                
                TblProduction newProduction;
                if (null != oldProductions && !oldProductions.isEmpty()) {
                    newProduction = oldProductions.get(0);
                    
                } else {
                    newProduction = new TblProduction();
                    newProduction.setId(productionVo.getTblProduction().getId());
                }

                //自社の金型UUIDに変換 
                if (StringUtils.isNotEmpty(productionVo.getMoldId())) {
                    MstMold ownerMold = entityManager.find(MstMold.class, productionVo.getMoldId());
                    if (null == ownerMold) {
                        continue;
                    } else {
                        newProduction.setMoldUuid(ownerMold.getUuid());
                    }
                }
                
                // 自社の設備UUIDに変換
                if (null != productionVo.getMachineId() && !"".equals(productionVo.getMachineId().trim())) {
                    MstMachine ownerMachine = entityManager.find(MstMachine.class, productionVo.getMachineId());
                    if (null == ownerMachine) {
                        continue;
                    } else {
                        newProduction.setMachineUuid(ownerMachine.getUuid());
                    }
                }
                
                newProduction.setDirectionId(null);
                newProduction.setProductionDate(productionVo.getTblProduction().getProductionDate());
                newProduction.setStartDatetime(productionVo.getTblProduction().getStartDatetime());
                newProduction.setStartDatetimeStz(productionVo.getTblProduction().getStartDatetimeStz());
                newProduction.setEndDatetime(productionVo.getTblProduction().getEndDatetime());
                newProduction.setEndDatetimeStz(productionVo.getTblProduction().getEndDatetimeStz());
                newProduction.setProducingTimeMinutes(FileUtil.getIntegerValue(productionVo.getTblProduction().getProducingTimeMinutes()));
                newProduction.setPersonUuid(productionVo.getTblProduction().getPersonUuid());
                newProduction.setProductionPhase(FileUtil.getIntegerValue(productionVo.getTblProduction().getProductionPhase()));
                newProduction.setWorkCategory(FileUtil.getIntegerValue(productionVo.getTblProduction().getWorkCategory()));
                newProduction.setWorkCode(productionVo.getTblProduction().getWorkCode());
                newProduction.setProcCd(productionVo.getTblProduction().getProcCd());
                newProduction.setPrevLotNumber(productionVo.getTblProduction().getPrevLotNumber());
                newProduction.setLotNumber(productionVo.getTblProduction().getLotNumber());
                newProduction.setShotCount(productionVo.getTblProduction().getShotCount());
                newProduction.setDisposedShotCount(productionVo.getTblProduction().getDisposedShotCount());
                newProduction.setCreateDate(productionVo.getTblProduction().getCreateDate());
                newProduction.setCreateUserUuid(productionVo.getTblProduction().getCreateUserUuid());
                newProduction.setUpdateDate(new Date());
                newProduction.setUpdateUserUuid(productionVo.getTblProduction().getUpdateUserUuid());
                
                if (null != oldProductions && !oldProductions.isEmpty()) {
                    entityManager.merge(newProduction);
                } else {
                    entityManager.persist(newProduction);
                }
            }
        }
        response.setError(false);

        return response;
    }

    /**
     * 生産実績テーブル削除データチェック
     *
     * @param response
     * @param id
     * @param loginUser
     */
    public void checkDeleteData(BasicResponse response, String id, LoginUser loginUser) {
        // 生産実績テーブル自体の存在チェック
        TblProduction deleteProduction = getProductionSingleById(id);
        if (deleteProduction == null) {
            setApplicationError(response, loginUser, "msg_error_data_deleted", "TblProduction");
            return;
        }

        // 親ロットに指定されているかチェック
        Long childCount = getProductionsCountByPrevLotNumber(deleteProduction.getLotNumber());
        if (childCount > 0) {
            setApplicationError(response, loginUser, "msg_cannot_delete_used_record", "TblProduction");
            return;
        }
    }
    
    /**
     * 生産実績テーブル削除データチェック
     *
     * @param tblProductionVo
     * @return 
     */
    public TblProductionList checkComponentStructure(TblProductionVo tblProductionVo) {
        TblProductionList tblProductionList = null;
        // 構成部品取得フラグ
        if (tblProductionVo.getStructureFlg() == 0) {
            
            List<MstComponentStructureVoList> structureList = new ArrayList();
            
            for (TblProductionDetailVo detailVo : tblProductionVo.getTblProductionDetailVos()) {
                // 完成数が0より大きい
                if (detailVo.getCompleteCount() > 0) {
                    MstProcedure maxMstProcedureCode = mstProcedureService.getMaxProcedureCode(detailVo.getComponentId());
                    // 工程番号（工番）が部品コードの中で最大の時だけ実施する
                    if (detailVo.getProcedureCode().equals(maxMstProcedureCode == null ? "" : maxMstProcedureCode.getProcedureCode())) {
                        // 日報指定した部品の下位階層部品リスト取得
                        List<MstComponentStructure> list = tblStockService.getStructureListByParentComponentId(detailVo.getComponentId());
                        if (list != null && !list.isEmpty()) {
                            tblProductionList = new TblProductionList();
                            for (MstComponentStructure cs : list) {
                                // 下位階層部品のロット番号取得
                                List<TblComponentLot> componentLots = entityManager.createNamedQuery("TblComponentLot.findByComponentIdForStructure").setParameter("componentId", cs.getComponentId()).getResultList();
                                cs.setTblComponentLotList(componentLots);
                            }
                            MstComponentStructureVoList structureVoList = new MstComponentStructureVoList();
                            structureVoList.setMstComponentStructures(list);
                            structureList.add(structureVoList);
                        }
                    }
                }
            }
            
            // 下位階層部品存在している場合、構成部品ロット選択ダイアログ表示
            if (tblProductionList != null && !structureList.isEmpty()) {
                tblProductionList.setMstComponentStructureVoList(structureList);
            }
        }
        return tblProductionList;
    }

    /**
     * 生産実績削除
     *
     * @param id
     */
    @Transactional
    public void deleteTblProduction(String id, LoginUser loginUser) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);

        TblProduction deleteProduction = getProductionSingleById(id);
        // 詳細テーブル削除（残高テーブルの削除、親ロットの残高調整込み）
        Collection<TblProductionDetail> list = deleteProduction.getTblProductionDetailCollection();
        for (TblProductionDetail detail : list) {
            tblProductionDetailService.deleteTblProductionDetail(detail, loginUser);
        }

        // 生産実績テーブル削除
        Query query = entityManager.createNamedQuery("TblProduction.delete");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    /**
     * 機械日報画面作業実績明細情報を取得
     *
     * @param init(初期化：0；検索：1)
     * @param productionDate
     * @param machineId
     * @param department
     * @param loginUser
     * @return
     */
    public TblProductionList getProductionDetailListByDailyReport(String init, String productionDate, String machineId, String department, LoginUser loginUser) {

        TblProductionList response = new TblProductionList();

        List<TblProductionDetail> tempObject = new ArrayList();

        List<TblProductionVo> tblProductionVos = new ArrayList();

        StringBuilder sql = new StringBuilder();

        // 画面初期化の場合
        if ("0".equals(init)) {

            sql.append("SELECT t FROM TblProductionDetail t JOIN FETCH t.mstComponent c JOIN FETCH t.productionId.mstMachine m JOIN FETCH t.productionId.mstUser u WHERE t.productionId.endDatetimeStz IS NULL");

            // 所属ID設定の場合
            if (StringUtils.isNotEmpty(department)) {

                sql.append(" AND m.department = :department");

            }
            
            sql.append(" ORDER BY t.productionId.id, c.componentCode");

            Query query = entityManager.createQuery(sql.toString());

            if (StringUtils.isNotEmpty(department)) {
                query.setParameter("department", Integer.valueOf(department));
            }

            tempObject = query.getResultList();

        } else if ("1".equals(init)) {// 画面検索の場合

            sql.append("SELECT t FROM TblProductionDetail t JOIN FETCH t.mstComponent c JOIN FETCH t.productionId.mstMachine m JOIN FETCH t.productionId.mstUser u WHERE 1=1");

            // 所属ID設定の場合
            if (StringUtils.isNotEmpty(department)) {

                sql.append(" AND m.department = :department");

            }

            // 設備ID設定の場合
            if (StringUtils.isNotEmpty(machineId)) {

                sql.append(" AND m.machineId = :machineId");

            }

            // 生産日設定の場合
            if (StringUtils.isNotEmpty(productionDate)) {

                //sql.append(" AND  t.tblProduction.startDatetime > :productionDate AND t.tblProduction.startDatetime < :afterProductionDate");
                //画面で指定された日付以降を取得するよう変更 kitaoji
                sql.append(" AND  t.productionId.startDatetime > :productionDate ");
            }

            sql.append(" ORDER BY t.productionId.id, c.componentCode");
            
            Query query = entityManager.createQuery(sql.toString());

            if (StringUtils.isNotEmpty(department)) {
                query.setParameter("department", Integer.valueOf(department));
            }

            if (StringUtils.isNotEmpty(machineId)) {
                query.setParameter("machineId", machineId);
            }

            if (StringUtils.isNotEmpty(productionDate)) {
                query.setParameter("productionDate", DateFormat.strToDate(productionDate));
                //画面で指定された日付以降を取得するよう変更 kitaoji
                //query.setParameter("afterProductionDate", DateFormat.getAfterDay(DateFormat.strToDate(productionDate)));
            }

            tempObject = query.getResultList();
        }

        // 検索結果＞0の場合
        if (tempObject.size() > 0) {

            String tempProductionId = "";
            
            // 生産実績状態choiceリスト取得
            MstChoiceList statusList = mstChoiceService.getChoice(loginUser.getLangId(), "tbl_production.status");
            
            // 営業日取得（yyyy/mm/dd HH:mm:ss）
            String currentDateTime = DateFormat.getCurrentDateTime();
            // 業務システム設定時間取得
            CnfSystem cnfSystem = cnfSystemService.findByKey("system", "business_start_time");
            String businessDate = DateFormat.getBusinessDate(currentDateTime, cnfSystem);
            // 作成済み日報の最大の日付取得
            List<Object[]> machineDailyReports = entityManager.createQuery("select r.tblMachineDailyReportPK.productionId, MAX(r.tblMachineDailyReportPK.productionDate) from TblMachineDailyReport r group by r.tblMachineDailyReportPK.productionId")
                    .getResultList();
            // 未作成
            String notCreated = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "not_created");

            boolean isFind = false;
            for (int i = 0; i < tempObject.size(); i++) {

                TblProductionVo tblProductionVo = new TblProductionVo();

                TblProductionDetail tempProduction = (TblProductionDetail) tempObject.get(i);

                if (0 == i || !tempProductionId.equals(tempProduction.getProductionId().getId())) {

                    tempProductionId = tempProduction.getProductionId().getId();

                    BeanCopyUtil.copyFields(tempProduction.getProductionId(), tblProductionVo);
                    BeanCopyUtil.copyFields(tempProduction.getMstComponent(), tblProductionVo);
                    tblProductionVo.setComponentId(tempProduction.getMstComponent().getId());

                    tblProductionVo.setStartDatetimeStr(DateFormat.dateToStr(tblProductionVo.getStartDatetime(), DateFormat.DATETIME_FORMAT));
                    tblProductionVo.setStartDatetimeStzStr(DateFormat.dateToStr(tblProductionVo.getStartDatetimeStz(), DateFormat.DATETIME_FORMAT));
                    tblProductionVo.setEndDatetimeStr(DateFormat.dateToStr(tblProductionVo.getEndDatetime(), DateFormat.DATETIME_FORMAT));
                    tblProductionVo.setEndDatetimeStzStr(DateFormat.dateToStr(tblProductionVo.getEndDatetimeStz(), DateFormat.DATETIME_FORMAT));

                    // 状態名称を取得
                    if (statusList.getMstChoice() != null && !statusList.getMstChoice().isEmpty()) {
                        for (MstChoice mstChoice : statusList.getMstChoice()) {
                            if (mstChoice.getMstChoicePK().getSeq().equals(String.valueOf(tblProductionVo.getStatus()))) {
                                tblProductionVo.setStatusText(mstChoice.getChoice());
                                break;
                            }
                        }
                    } else {
                        tblProductionVo.setStatusText(null);
                    }

                    // 設備名称を取得
                    tblProductionVo.setMachineName(tempProduction.getProductionId().getMstMachine().getMachineName());

                    // 生産者名称取得
                    tblProductionVo.setPersonName(tempProduction.getProductionId().getMstUser().getUserName());

                    tblProductionVo.setId(tempProduction.getTblProduction().getId());

                    if (machineDailyReports != null && !machineDailyReports.isEmpty()) {
                        isFind = false;
                        for (Object[] tblMachineDailyReport : machineDailyReports) {
                            if (tblMachineDailyReport[0].equals(tempProductionId)) {
                                tblProductionVo.setMaxDailyReportMMDD(DateFormat.dateToStr((Date) tblMachineDailyReport[1], DateFormat.DATE_FORMAT_MONTHDAY));
                                if (FileUtil.getDaysOfTwo((Date) tblMachineDailyReport[1], DateFormat.strToDate(businessDate)) > 1 && tempProduction.getProductionId().getStatus() != 9) {
                                    tblProductionVo.setMaxDailyReportColor(1);
                                } else {
                                    tblProductionVo.setMaxDailyReportColor(0);
                                }
                                isFind = true;
                                break;
                            }
                        }
                        if (!isFind) {
                            if (tempProduction.getProductionId().getStartDatetime().compareTo(DateFormat.strToDate(businessDate)) >= 0) {
                                tblProductionVo.setMaxDailyReportMMDD("-");
                                tblProductionVo.setMaxDailyReportColor(0);
                            } else {
                                tblProductionVo.setMaxDailyReportMMDD(notCreated);
                                tblProductionVo.setMaxDailyReportColor(1);
                            }
                        }
                    } else if (tempProduction.getProductionId().getStartDatetime().compareTo(DateFormat.strToDate(businessDate)) >= 0) {
                        tblProductionVo.setMaxDailyReportMMDD("-");
                        tblProductionVo.setMaxDailyReportColor(0);
                    } else {
                        tblProductionVo.setMaxDailyReportMMDD(notCreated);
                        tblProductionVo.setMaxDailyReportColor(1);
                    }

                    tblProductionVos.add(tblProductionVo);
                }
            }
        }

        // 機械日報のソート処理
        Collections.sort(tblProductionVos, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                TblProductionVo obj1 = (TblProductionVo) o1;
                TblProductionVo obj2 = (TblProductionVo) o2;

                if (obj1.getMachineName().equals(obj2.getMachineName())) {
                    return obj1.getStartDatetimeStr().compareTo(obj2.getStartDatetimeStr());
                } else {
                    return obj1.getMachineName().compareTo(obj2.getMachineName());
                }
            }
        });

        response.setTblProductionVo(tblProductionVos);

        return response;

    }

    /**
     * 
     * @param mold 
     */
    public void setMoldChkParams(MstMoldDetail mold) {
        mold.setMainteCycleId01(mold.getMstMold().getMainteCycleId01());
        mold.setMainteCycleId02(mold.getMstMold().getMainteCycleId02());
        mold.setMainteCycleId03(mold.getMstMold().getMainteCycleId03());
        mold.setAfterMainteTotalProducingTimeHour(mold.getMstMold().getAfterMainteTotalProducingTimeHour());
        mold.setLastMainteDate(mold.getMstMold().getLastMainteDate());
        mold.setAfterMainteTotalShotCount(mold.getMstMold().getAfterMainteTotalShotCount());
        mold.setMoldUuid(mold.getMstMold().getUuid());
    }
    
    /**
     * 
     * @param machine 
     */
    public void setMachineChkParams(MstMachineVo machine) {
        machine.setMainteCycleId01(machine.getMstMachine().getMainteCycleId01());
        machine.setMainteCycleId02(machine.getMstMachine().getMainteCycleId02());
        machine.setMainteCycleId03(machine.getMstMachine().getMainteCycleId03());
        machine.setAfterMainteTotalProducingTimeHour(machine.getMstMachine().getAfterMainteTotalProducingTimeHour());
        machine.setLastMainteDate(machine.getMstMachine().getLastMainteDate());
        machine.setAfterMainteTotalShotCount(machine.getMstMachine().getAfterMainteTotalShotCount());
        machine.setMachineUuid(machine.getMstMachine().getUuid());
    }
    
    /**
     * 部品材料マスタ情報を取得
     *
     * @param tblProductionDetailVo
     * @param tblProductionDetail
     */
    public void getComponentMaterialInfo(TblProductionDetailVo tblProductionDetailVo, TblProductionDetail tblProductionDetail) {

        String componentId = tblProductionDetailVo.getComponentId();

        // 部品ID存在の場合
        if (StringUtils.isNotEmpty(componentId)) {
            String material01Id = tblProductionDetailVo.getMaterial01Id();
            // 材料01ID存在の場合
            if (StringUtils.isNotEmpty(material01Id)) {
                List<MstComponentMaterial> MstComponentMaterialList = mstComponentMaterialService.getComponentmaterialInfoByCondition(componentId, material01Id, "");
                if (MstComponentMaterialList.size() > 0) {
                    MstComponentMaterial mstComponentMaterial = MstComponentMaterialList.get(0);
                    tblProductionDetailVo.setNumerator01(mstComponentMaterial.getNumerator());
                    tblProductionDetailVo.setDenominator01(mstComponentMaterial.getDenominator());
                    tblProductionDetailVo.setMaterial01Id(mstComponentMaterial.getMstMaterial().getId());
                    tblProductionDetailVo.setMaterial01Code(mstComponentMaterial.getMstMaterial().getMaterialCode());
                    tblProductionDetailVo.setMaterial01Name(mstComponentMaterial.getMstMaterial().getMaterialName());
                    tblProductionDetailVo.setMaterial01Type(mstComponentMaterial.getMstMaterial().getMaterialType());
                    tblProductionDetailVo.setMaterial01Grade(mstComponentMaterial.getMstMaterial().getMaterialGrade());
                } else {
                    if (tblProductionDetail != null && tblProductionDetail.getMstMaterial01() != null) {
                        tblProductionDetailVo.setMaterial01Code(tblProductionDetail.getMstMaterial01().getMaterialCode());
                        tblProductionDetailVo.setMaterial01Name(tblProductionDetail.getMstMaterial01().getMaterialName());
                        tblProductionDetailVo.setMaterial01Type(tblProductionDetail.getMstMaterial01().getMaterialType());
                        tblProductionDetailVo.setMaterial01Grade(tblProductionDetail.getMstMaterial01().getMaterialGrade());
                    }
                }
            }

            String material02Id = tblProductionDetailVo.getMaterial02Id();
            // 材料02ID存在の場合
            if (StringUtils.isNotEmpty(material02Id)) {
                List<MstComponentMaterial> MstComponentMaterialList = mstComponentMaterialService.getComponentmaterialInfoByCondition(componentId, material02Id, "");
                if (MstComponentMaterialList.size() > 0) {
                    MstComponentMaterial mstComponentMaterial = MstComponentMaterialList.get(0);
                    tblProductionDetailVo.setNumerator02(mstComponentMaterial.getNumerator());
                    tblProductionDetailVo.setDenominator02(mstComponentMaterial.getDenominator());
                    tblProductionDetailVo.setMaterial02Id(mstComponentMaterial.getMstMaterial().getId());
                    tblProductionDetailVo.setMaterial02Code(mstComponentMaterial.getMstMaterial().getMaterialCode());
                    tblProductionDetailVo.setMaterial02Name(mstComponentMaterial.getMstMaterial().getMaterialName());
                    tblProductionDetailVo.setMaterial02Type(mstComponentMaterial.getMstMaterial().getMaterialType());
                    tblProductionDetailVo.setMaterial02Grade(mstComponentMaterial.getMstMaterial().getMaterialGrade());
                } else {
                    if (tblProductionDetail != null && tblProductionDetail.getMstMaterial02() != null) {
                        tblProductionDetailVo.setMaterial02Code(tblProductionDetail.getMstMaterial02().getMaterialCode());
                        tblProductionDetailVo.setMaterial02Name(tblProductionDetail.getMstMaterial02().getMaterialName());
                        tblProductionDetailVo.setMaterial02Type(tblProductionDetail.getMstMaterial02().getMaterialType());
                        tblProductionDetailVo.setMaterial02Grade(tblProductionDetail.getMstMaterial02().getMaterialGrade());
                    }
                }
            }

            String material03Id = tblProductionDetailVo.getMaterial03Id();
            // 材料01ID存在の場合
            if (StringUtils.isNotEmpty(material03Id)) {
                List<MstComponentMaterial> MstComponentMaterialList = mstComponentMaterialService.getComponentmaterialInfoByCondition(componentId, material03Id, "");
                if (MstComponentMaterialList.size() > 0) {
                    MstComponentMaterial mstComponentMaterial = MstComponentMaterialList.get(0);
                    tblProductionDetailVo.setNumerator03(mstComponentMaterial.getNumerator());
                    tblProductionDetailVo.setDenominator03(mstComponentMaterial.getDenominator());
                    tblProductionDetailVo.setMaterial03Id(mstComponentMaterial.getMstMaterial().getId());
                    tblProductionDetailVo.setMaterial03Code(mstComponentMaterial.getMstMaterial().getMaterialCode());
                    tblProductionDetailVo.setMaterial03Name(mstComponentMaterial.getMstMaterial().getMaterialName());
                    tblProductionDetailVo.setMaterial03Type(mstComponentMaterial.getMstMaterial().getMaterialType());
                    tblProductionDetailVo.setMaterial03Grade(mstComponentMaterial.getMstMaterial().getMaterialGrade());
                } else {
                    if (tblProductionDetail != null && tblProductionDetail.getMstMaterial03() != null) {
                        tblProductionDetailVo.setMaterial03Code(tblProductionDetail.getMstMaterial03().getMaterialCode());
                        tblProductionDetailVo.setMaterial03Name(tblProductionDetail.getMstMaterial03().getMaterialName());
                        tblProductionDetailVo.setMaterial03Type(tblProductionDetail.getMstMaterial03().getMaterialType());
                        tblProductionDetailVo.setMaterial03Grade(tblProductionDetail.getMstMaterial03().getMaterialGrade());
                    }
                }
            }
        }
    }
    
    /**
     * 生産開始時の設備利用チェック
     * 
     * @param machineId
     * @param machineUuid
     * @param userUuid
     * @return 
     */
    public TblProduction getMachineUsingProducing(String machineId, String machineUuid /**, String userUuid **/) {
        TblProduction production = null;
        //終了していなくてその設備を使って行っている生産を取得する
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT t FROM TblProduction t JOIN FETCH t.mstMachine mac WHERE t.endDatetime IS NULL ");
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
        
        Query query = entityManager.createQuery(queryString.toString());
        if (machineUuid != null && !machineUuid.equals("")) {
            query.setParameter("machineUuid", machineUuid);
        }
        else if (machineId != null && !machineId.equals("")) {
            query.setParameter("machineId", machineId);
        }
        List<TblProduction> resultList = query.getResultList();
        if (resultList != null && resultList.size() > 0) {
            //複数該当する場合はひとつだけ返す（順不同）
            production = resultList.get(0);
        }
        return production;
    }
    
    /**
     * 生産開始時の金型利用チェック
     * 
     * @param moldId
     * @param moldUuid
     * @param userUuid
     * @return 
     */
    public TblProduction getMoldUsingProducing(String moldId, String moldUuid /**, String userUuid **/) {
        TblProduction production = null;
        //終了していなくてその金型を使って行っている生産を取得する
        StringBuilder queryString = new StringBuilder();
        queryString.append("SELECT t FROM TblProduction t JOIN FETCH t.mstMold mold WHERE t.endDatetime IS NULL ");
        if (moldUuid != null && !moldUuid.equals("")) {
            //UUIDが指定されていればUUIDを使う
            queryString.append(" AND t.moldUuid = :moldUuid ");
        }
        else if (moldId != null && !moldId.equals("")) {
            //UUIDがなくて、金型IDが指定されていれば金型IDを使う
            queryString.append(" AND mold.moldId = :moldId ");
        }
        else {
            return null;
        }
        
        Query query = entityManager.createQuery(queryString.toString());
        if (moldUuid != null && !moldUuid.equals("")) {
            query.setParameter("moldUuid", moldUuid);
        }
        else if (moldId != null && !moldId.equals("")) {
            query.setParameter("moldId", moldId);
        }
        List<TblProduction> resultList = query.getResultList();
        if (resultList != null && resultList.size() > 0) {
            //複数該当する場合はひとつだけ返す（順不同）
            production = resultList.get(0);
        }
        return production;
    }
    
    @Transactional
    public void updateComponentLotRelation(TblProductionDetail tblProductionDetail, TblComponentLot tblComponentLot, String macReportId, String userUuid) {
        if (tblProductionDetail.getTblComponentLotRelationVoList() != null && tblProductionDetail.getTblComponentLotRelationVoList().getTblComponentLotRelationVos() != null && !tblProductionDetail.getTblComponentLotRelationVoList().getTblComponentLotRelationVos().isEmpty()) {
            for (TblComponentLotRelationVo relationVo : tblProductionDetail.getTblComponentLotRelationVoList().getTblComponentLotRelationVos()) {
                String subComponentLotId = null;
                if (StringUtils.isNotEmpty(relationVo.getSubComponentLotNo())) {
                    TblComponentLot subComponentLot = tblComponentLotService.getSingleResultTblComponentLot(relationVo.getSubComponentCode(), relationVo.getSubProcedureCode(), relationVo.getSubComponentLotNo());
                    if (subComponentLot == null) {
                        continue;
                    } else {
                        subComponentLotId = subComponentLot.getUuid();
                    }
                }
                MstProcedure maxMstProcedureCode = mstProcedureService.getFinalProcedureByComponentId(relationVo.getSubComponentId());
                
                TblComponentLotRelation tblComponentLotRelation = new TblComponentLotRelation();
                tblComponentLotRelation.setUuid(IDGenerator.generate());
                tblComponentLotRelation.setComponentId(tblProductionDetail.getComponentId());
                tblComponentLotRelation.setProcedureCode(tblProductionDetail.getMstProcedure().getProcedureCode());
                tblComponentLotRelation.setComponentLotId(tblComponentLot.getUuid());
                tblComponentLotRelation.setSubComponentId(relationVo.getSubComponentId());
                tblComponentLotRelation.setSubProcedureCode(maxMstProcedureCode == null ? "" : maxMstProcedureCode.getProcedureCode());
                tblComponentLotRelation.setSubComponentLotId(subComponentLotId);
                tblComponentLotRelation.setProductionDetailId(tblProductionDetail.getId());
                tblComponentLotRelation.setMacReportId(macReportId);
                tblComponentLotRelation.setCreateDate(new Date());
                tblComponentLotRelation.setCreateUserUuid(userUuid);
                tblComponentLotRelation.setUpdateDate(tblComponentLotRelation.getCreateDate());
                tblComponentLotRelation.setUpdateUserUuid(userUuid);
                entityManager.persist(tblComponentLotRelation);
            }
        }
    }

    public MaterialList getProductionListByComponentAndLot(String componentId, String productionLotNumber) {
        MaterialList response = new MaterialList();
        List<String> materials = new ArrayList<>();
        List<String> materialProduct = new ArrayList<>();

        // get list materials component
        if (StringUtils.isNotEmpty(componentId)) {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT c FROM MstComponentMaterial c JOIN FETCH c.mstMaterial m WHERE 1 = 1 ");
            sql.append("AND c.componentId = :componentId ");
            TypedQuery<MstComponentMaterial> query = entityManager.createQuery(sql.toString(), MstComponentMaterial.class)
                    .setParameter("componentId", componentId);

            List<MstComponentMaterial> list = query.getResultList();

            for (MstComponentMaterial detail : list) {
                MstMaterial material = detail.getMstMaterial();
                if (material != null) {
                    materials.add(material.getMaterialName());
                }
            }
        }

        // get materials by product lot
        if (StringUtils.isNotEmpty(componentId) && StringUtils.isNotEmpty(productionLotNumber)) {
            StringBuilder sqlProduct = new StringBuilder();
            sqlProduct.append("SELECT t FROM TblProductionDetail t JOIN FETCH t.tblProduction p JOIN FETCH t.mstComponent c " +
                    "WHERE  c.id = :componentId AND p.lotNumber = :productionLotNumber ");

            TblProductionDetail tblProductionDetail = entityManager.createQuery(sqlProduct.toString(), TblProductionDetail.class)
                    .setParameter("componentId", componentId)
                    .setParameter("productionLotNumber", productionLotNumber)
                    .getResultList().stream().findFirst().orElse(null);

            if (tblProductionDetail != null) {
                MstMaterial material1 = tblProductionDetail.getMstMaterial01();
                if (material1 != null) {
                    materialProduct.add(material1.getMaterialName());
                }

                MstMaterial material2 = tblProductionDetail.getMstMaterial02();
                if (material2 != null) {
                    materialProduct.add(material2.getMaterialName());
                }

                MstMaterial material3 = tblProductionDetail.getMstMaterial03();
                if (material3 != null) {
                    materialProduct.add(material3.getMaterialName());
                }
            }
        }

        response.setDatas(materials);
        response.setMaterialProduct(materialProduct);
        return response;
    }
    
    public TblProductionList getProductionForMobile(String department, String machineId, String moldId, String componentCode) {
    	TblProductionList response = new TblProductionList();
    	StringBuilder sql;
        sql = new StringBuilder(
                "SELECT detail FROM ProductionDetail detail "
                + " JOIN FETCH detail.mstComponent "
                + " JOIN FETCH detail.productionId  "
//                + " LEFT JOIN FETCH detail.productionId.mstUser "
                + " WHERE 1=1 "
        );
        sql.append(" AND detail.productionId.endDatetime IS NULL ");

        if (StringUtils.isNotEmpty(department)) {
//            sql.append(" AND detail.productionId.mstUser.department = :department");
            sql.append(" AND detail.productionId.prodDepartment = :department");
        }
        if (StringUtils.isNotEmpty(machineId)) {
            sql.append(" AND detail.productionId.mstMachine.machineId = :machineId");
        }
        if (StringUtils.isNotEmpty(moldId)) {
            sql.append(" AND detail.productionId.mstMold.moldId = :moldId");
        }
        if (StringUtils.isNotEmpty(componentCode)) {
            sql.append(" AND detail.mstComponent.componentCode = :componentCode");
        }

        sql.append(" ORDER BY detail.productionId.startDatetime ASC ");
        Query query = entityManager.createQuery(sql.toString());

        if (StringUtils.isNotEmpty(department)) {
            query.setParameter("department", Integer.valueOf(department));
        }
        if (StringUtils.isNotEmpty(machineId)) {
            query.setParameter("machineId", machineId);
        }
        if (StringUtils.isNotEmpty(moldId)) {
            query.setParameter("moldId", moldId);
        }
        if (StringUtils.isNotEmpty(componentCode)) {
            query.setParameter("componentCode", componentCode);
        }
        List<ProductionDetail> list = query.getResultList();
        Production production = new Production();
        Map<String, Production> productionMap = new LinkedHashMap<>();
        for(ProductionDetail detail : list) {
        	if(productionMap.containsKey(detail.getProductionId().getId())){
        		productionMap.get(detail.getProductionId().getId()).getProductionDetails().add(detail);
        	}else {
        		production = detail.getProductionId();
        		production.getProductionDetails().add(detail);
        		productionMap.put(detail.getProductionId().getId(), production);
        	}
        }
        Iterator iter = productionMap.entrySet().iterator();
        List<Production> result = new ArrayList<>();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();         
            result.add((Production) entry.getValue());
        }
        response.setProductions(result);
    	return response;
    }
    
    public TblProductionList getProcuctionLotListByPrevProcedureId(String componentId, String prevProcedureId, Date productionDate) {
        TblProductionList response = new TblProductionList();
        if (StringUtils.isNotEmpty(componentId) && StringUtils.isNotEmpty(prevProcedureId) && productionDate != null) {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT detail FROM ProductionDetail detail ");
            sql.append(" JOIN FETCH detail.productionId p ");
            sql.append(" WHERE detail.productionId.productionDate >= :productionDate ");
            sql.append(" AND detail.componentId = :componentId ");
            sql.append(" AND detail.procedureId = :procedureId ");
            sql.append(" ORDER BY p.lotNumber asc ");
            
            Query query = entityManager.createQuery(sql.toString());
            
            // 生産日の過去10日間の日付取得
            Date targetDate = DateFormat.getBeforeDays(productionDate, 10);
            
            query.setParameter("productionDate", targetDate);
            query.setParameter("componentId", componentId);
            query.setParameter("procedureId", prevProcedureId);
            
            List<ProductionDetail> details = query.getResultList();
            if (details != null && !details.isEmpty()) {
                List<Production> productions = new ArrayList<>();
                for (ProductionDetail detail : details) {
                    productions.add(detail.getProductionId());
                }
                response.setProductions(productions);
            }
        }
        return response;
    }
    
    /**
     * 生産実績取込
     * 
     * @param fileUuid
     * @param loginUser
     * @return
     */
    @Transactional
    public ImportResultResponse postProductionCsv(String fileUuid, LoginUser loginUser) {
        ImportResultResponse importResultResponse = new ImportResultResponse();

        //①CSVファイルを取込み
        long succeededCount;
        long addedCount = 0;
        long updatedCount = 0;
        long failedCount = 0;
        long deletedCount = 0;
        try {
            String logFileUuid = IDGenerator.generate();
            importResultResponse.setLog(logFileUuid);
            String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);

            if (!csvFile.endsWith(CommonConstants.CSV)) {
                importResultResponse.setError(true);
                importResultResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_wrong_csv_layout");
                importResultResponse.setErrorMessage(msg);
                return importResultResponse;
            }

            ArrayList<List<String>> readList = CSVFileUtil.readCsv(csvFile);
            if (readList.size() <= 1) {
                return importResultResponse;
            } else {

                String userLangId = loginUser.getLangId();
                String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);

                ArrayList dictKeyList = new ArrayList();

                dictKeyList.add("row_number");
                dictKeyList.add("error_detail");
                dictKeyList.add("error");
                dictKeyList.add("msg_error_wrong_csv_layout");
                dictKeyList.add("mst_error_record_not_found");
                dictKeyList.add("msg_error_value_invalid");
                dictKeyList.add("msg_error_not_null");
                dictKeyList.add("msg_error_over_length");
                dictKeyList.add("msg_error_date_format_invalid");
                dictKeyList.add("msg_error_not_isnumber");
                dictKeyList.add("msg_error_num_over_zero");

                dictKeyList.add("db_process");
                dictKeyList.add("msg_data_modified");
                dictKeyList.add("msg_record_added");
                dictKeyList.add("msg_record_deleted");

                dictKeyList.add("production_import");

                dictKeyList.add("production_date");
                dictKeyList.add("production_start_time");
                dictKeyList.add("production_end_time");
                dictKeyList.add("production_user_id");
                dictKeyList.add("prod_department");
                dictKeyList.add("machine_id");
                dictKeyList.add("mold_id");
                dictKeyList.add("component_code");
                dictKeyList.add("procedure_code");
                dictKeyList.add("lot_number");
                dictKeyList.add("count_per_shot");
                dictKeyList.add("complete_count");
                dictKeyList.add("defect_count");
                dictKeyList.add("prev_lot_number");
                dictKeyList.add("direction_code");
                dictKeyList.add("shot_count");
                dictKeyList.add("material_name_with_parameter");
                dictKeyList.add("material_lot_no_with_parameter");
                dictKeyList.add("material_amount_with_parameter");
                dictKeyList.add("material_purged_amount_with_parameter");
                dictKeyList.add("net_producint_time_minutes");
                dictKeyList.add("suspended_time_minutes");

                Map<String, String> dictMap = mstDictionaryService.getDictionaryHashMap(userLangId, dictKeyList);

                Map<String, Integer> colIndexMap = new HashMap<>();

                for (int j = 0; j < readList.get(0).size(); j++) {
                    if (readList.get(0).get(j).trim().equals(dictMap.get("production_date"))) {
                        colIndexMap.put("production_date", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("production_start_time"))) {
                        colIndexMap.put("production_start_time", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("production_end_time"))) {
                        colIndexMap.put("production_end_time", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("production_user_id"))) {
                        colIndexMap.put("production_user_id", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("prod_department"))) {
                        colIndexMap.put("prod_department", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("machine_id"))) {
                        colIndexMap.put("machine_id", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("mold_id"))) {
                        colIndexMap.put("mold_id", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("component_code"))) {
                        colIndexMap.put("component_code", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("procedure_code"))) {
                        colIndexMap.put("procedure_code", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("lot_number"))) {
                        colIndexMap.put("lot_number", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("count_per_shot"))) {
                        colIndexMap.put("count_per_shot", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("complete_count"))) {
                        colIndexMap.put("complete_count", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("defect_count"))) {
                        colIndexMap.put("defect_count", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("prev_lot_number"))) {
                        colIndexMap.put("prev_lot_number", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("direction_code"))) {
                        colIndexMap.put("direction_code", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("shot_count"))) {
                        colIndexMap.put("shot_count", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_name_with_parameter").replace("%s", "01"))) {
                        colIndexMap.put("material_name_with_parameter_01", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_name_with_parameter").replace("%s", "02"))) {
                        colIndexMap.put("material_name_with_parameter_02", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_name_with_parameter").replace("%s", "03"))) {
                        colIndexMap.put("material_name_with_parameter_03", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_lot_no_with_parameter").replace("%s", "01"))) {
                        colIndexMap.put("material_lot_no_with_parameter_01", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_lot_no_with_parameter").replace("%s", "02"))) {
                        colIndexMap.put("material_lot_no_with_parameter_02", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_lot_no_with_parameter").replace("%s", "03"))) {
                        colIndexMap.put("material_lot_no_with_parameter_03", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_amount_with_parameter").replace("%s", "01"))) {
                        colIndexMap.put("material_amount_with_parameter_01", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_amount_with_parameter").replace("%s", "02"))) {
                        colIndexMap.put("material_amount_with_parameter_02", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_amount_with_parameter").replace("%s", "03"))) {
                        colIndexMap.put("material_amount_with_parameter_03", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_purged_amount_with_parameter").replace("%s", "01"))) {
                        colIndexMap.put("material_purged_amount_with_parameter_01", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_purged_amount_with_parameter").replace("%s", "02"))) {
                        colIndexMap.put("material_purged_amount_with_parameter_02", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("material_purged_amount_with_parameter").replace("%s", "03"))) {
                        colIndexMap.put("material_purged_amount_with_parameter_03", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("net_producint_time_minutes"))) {
                        colIndexMap.put("net_producint_time_minutes", j);
                    } else if (readList.get(0).get(j).trim().equals(dictMap.get("suspended_time_minutes"))) {
                        colIndexMap.put("suspended_time_minutes", j);
                    }
                }

                MstUser mstPerson;
                MstMachine mstMachine = null;
                MstMold mstMold = null;
                MstComponent mstComponent;
                MstProcedure mstProcedure;
                TblDirection tblDirection = null;
                MstMaterial mstMaterial01 = null;
                MstMaterial mstMaterial02 = null;
                MstMaterial mstMaterial03 = null;
                String productionKey = "";
                TblProduction production = null;

                FileUtil fileUtil = new FileUtil();
                Map<String, Integer> inMap = new HashMap<>();
                MstChoiceList mstChoiceList = mstChoiceService.getChoice(loginUser.getLangId(), "mst_user.department");

                for (MstChoice mstChoice : mstChoiceList.getMstChoice()) {
                    inMap.put(mstChoice.getChoice(), Integer.parseInt(mstChoice.getMstChoicePK().getSeq()));
                }
                
                String productionDate = null, productionStartDatetime = null, productionEndDatetime = null, productionUserId = null, prodDepartment = null, machineId = null, moldId = null, lotNumber = null, prevLotNumber = null, shotCount = null, 
                    directionCode = null, componentCode = null, procedureCode = null, countPerShot = null, completeCount = null, defectCount = null,
                    materialName01 = null, materialName02 = null, materialName03 = null, materialLotNo01 = null, materialLotNo02 = null, materialLotNo03 = null, materialAmount01 = null, materialAmount02 = null, materialAmount03 = null, 
                    materialPurgedAmount01 = null, materialPurgedAmount02 = null, materialPurgedAmount03 = null, netProducintTimeMinutes = null, suspendedTimeMinutes = null;
                for (int i = 1; i < readList.size(); i++) {
                    ArrayList comList = (ArrayList) readList.get(i);

                    if (colIndexMap.get("production_date") != null) {
                        productionDate = String.valueOf(comList.get(colIndexMap.get("production_date"))).trim();
                    }
                    if (colIndexMap.get("production_start_time") != null) {
                        productionStartDatetime = String.valueOf(comList.get(colIndexMap.get("production_start_time"))).trim();
                    }
                    if (colIndexMap.get("production_end_time") != null) {
                        productionEndDatetime = String.valueOf(comList.get(colIndexMap.get("production_end_time"))).trim();
                    }
                    if (colIndexMap.get("production_user_id") != null) {
                        productionUserId = String.valueOf(comList.get(colIndexMap.get("production_user_id"))).trim();
                    }
                    if (colIndexMap.get("prod_department") != null) {
                        prodDepartment = String.valueOf(comList.get(colIndexMap.get("prod_department"))).trim();
                    }
                    if (colIndexMap.get("machine_id") != null) {
                        machineId = String.valueOf(comList.get(colIndexMap.get("machine_id"))).trim();
                    }
                    if (colIndexMap.get("mold_id") != null) {
                        moldId = String.valueOf(comList.get(colIndexMap.get("mold_id"))).trim();
                    }
                    if (colIndexMap.get("lot_number") != null) {
                        lotNumber = String.valueOf(comList.get(colIndexMap.get("lot_number"))).trim();
                    }
                    if (colIndexMap.get("prev_lot_number") != null) {
                        prevLotNumber = String.valueOf(comList.get(colIndexMap.get("prev_lot_number"))).trim();
                    }
                    if (colIndexMap.get("direction_code") != null) {
                        directionCode = String.valueOf(comList.get(colIndexMap.get("direction_code"))).trim();
                    }
                    if (colIndexMap.get("shot_count") != null) {
                        shotCount = String.valueOf(comList.get(colIndexMap.get("shot_count"))).trim();
                    }
                    if (colIndexMap.get("component_code") != null) {
                        componentCode = String.valueOf(comList.get(colIndexMap.get("component_code"))).trim();
                    }
                    if (colIndexMap.get("procedure_code") != null) {
                        procedureCode = String.valueOf(comList.get(colIndexMap.get("procedure_code"))).trim();
                    }
                    if (colIndexMap.get("count_per_shot") != null) {
                        countPerShot = String.valueOf(comList.get(colIndexMap.get("count_per_shot"))).trim();
                    }
                    if (colIndexMap.get("complete_count") != null) {
                        completeCount = String.valueOf(comList.get(colIndexMap.get("complete_count"))).trim();
                    }
                    if (colIndexMap.get("defect_count") != null) {
                        defectCount = String.valueOf(comList.get(colIndexMap.get("defect_count"))).trim();
                    }
                    if (colIndexMap.get("material_name_with_parameter_01") != null) {
                        materialName01 = String.valueOf(comList.get(colIndexMap.get("material_name_with_parameter_01"))).trim();
                    }
                    if (colIndexMap.get("material_name_with_parameter_02") != null) {
                        materialName02 = String.valueOf(comList.get(colIndexMap.get("material_name_with_parameter_02"))).trim();
                    }
                    if (colIndexMap.get("material_name_with_parameter_03") != null) {
                        materialName03 = String.valueOf(comList.get(colIndexMap.get("material_name_with_parameter_03"))).trim();
                    }
                    if (colIndexMap.get("material_lot_no_with_parameter_01") != null) {
                        materialLotNo01 = String.valueOf(comList.get(colIndexMap.get("material_lot_no_with_parameter_01"))).trim();
                    }
                    if (colIndexMap.get("material_lot_no_with_parameter_02") != null) {
                        materialLotNo02 = String.valueOf(comList.get(colIndexMap.get("material_lot_no_with_parameter_02"))).trim();
                    }
                    if (colIndexMap.get("material_lot_no_with_parameter_03") != null) {
                        materialLotNo03 = String.valueOf(comList.get(colIndexMap.get("material_lot_no_with_parameter_03"))).trim();
                    }
                    if (colIndexMap.get("material_amount_with_parameter_01") != null) {
                        materialAmount01 = String.valueOf(comList.get(colIndexMap.get("material_amount_with_parameter_01"))).trim();
                    }
                    if (colIndexMap.get("material_amount_with_parameter_02") != null) {
                        materialAmount02 = String.valueOf(comList.get(colIndexMap.get("material_amount_with_parameter_02"))).trim();
                    }
                    if (colIndexMap.get("material_amount_with_parameter_03") != null) {
                        materialAmount03 = String.valueOf(comList.get(colIndexMap.get("material_amount_with_parameter_03"))).trim();
                    }
                    if (colIndexMap.get("material_purged_amount_with_parameter_01") != null) {
                        materialPurgedAmount01 = String.valueOf(comList.get(colIndexMap.get("material_purged_amount_with_parameter_01"))).trim();
                    }
                    if (colIndexMap.get("material_purged_amount_with_parameter_02") != null) {
                        materialPurgedAmount02 = String.valueOf(comList.get(colIndexMap.get("material_purged_amount_with_parameter_02"))).trim();
                    }
                    if (colIndexMap.get("material_purged_amount_with_parameter_03") != null) {
                        materialPurgedAmount03 = String.valueOf(comList.get(colIndexMap.get("material_purged_amount_with_parameter_03"))).trim();
                    }
                    if (colIndexMap.get("net_producint_time_minutes") != null) {
                        netProducintTimeMinutes = String.valueOf(comList.get(colIndexMap.get("net_producint_time_minutes"))).trim();
                    }
                    if (colIndexMap.get("suspended_time_minutes") != null) {
                        suspendedTimeMinutes = String.valueOf(comList.get(colIndexMap.get("suspended_time_minutes"))).trim();
                    }
                    
                    if (StringUtils.isEmpty(productionDate)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("production_date"), productionDate, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (StringUtils.isEmpty(productionStartDatetime)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("production_start_time"), productionStartDatetime, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (StringUtils.isEmpty(productionEndDatetime)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("production_end_time"), productionEndDatetime, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (!fileUtil.dateCheck(productionDate)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("production_date"), productionDate, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_date_format_invalid")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (productionStartDatetime.length() > 5) {
                        productionStartDatetime = productionStartDatetime.substring(0, 5);
                    }
                    if (DateFormat.strToDatetime(productionDate + " "  + productionStartDatetime + DateFormat.TIME_SECONDS) == null) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("production_start_time"), productionStartDatetime, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_date_format_invalid")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (productionEndDatetime.length() > 5) {
                        productionEndDatetime = productionEndDatetime.substring(0, 5);
                    }
                    if (DateFormat.strToDatetime(productionDate + " "  + productionEndDatetime + DateFormat.TIME_SECONDS) == null) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("production_end_time"), productionEndDatetime, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_date_format_invalid")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (StringUtils.isEmpty(lotNumber)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("lot_number"), lotNumber, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    if (fileUtil.maxLangthCheck(lotNumber, 100)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("lot_number"), lotNumber, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    int department = 0;

                    if (StringUtils.isEmpty(prodDepartment)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("prod_department"), prodDepartment, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    } else {
                        if (inMap.get(prodDepartment) == null) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("prod_department"), prodDepartment, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                            failedCount = failedCount + 1;
                            continue;
                        } else {
                            department = inMap.get(prodDepartment);
                        }
                    }

                    if (StringUtils.isEmpty(countPerShot)) {
                        countPerShot = "0";
                    } else {
                        try {
                            Integer.valueOf(countPerShot);
                        } catch (NumberFormatException e) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("count_per_shot"), countPerShot, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_num_over_zero")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }
                    
                    if (StringUtils.isEmpty(completeCount)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("complete_count"), completeCount, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    } else {
                        try {
                            Integer.valueOf(completeCount);
                        } catch (NumberFormatException e) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("complete_count"), completeCount, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_num_over_zero")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }
                    
                    if (StringUtils.isEmpty(defectCount)) {
                        defectCount = "0";
                    } else {
                        try {
                            Integer.valueOf(defectCount);
                        } catch (NumberFormatException e) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("defect_count"), defectCount, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_num_over_zero")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }
                    
                    if (StringUtils.isEmpty(shotCount)) {
                        shotCount = "0";
                    } else {
                        try {
                            Integer.valueOf(shotCount);
                        } catch (NumberFormatException e) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("shot_count"), shotCount, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_num_over_zero")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }
                    
                    if (StringUtils.isNotEmpty(netProducintTimeMinutes)) {
                        if (fileUtil.maxLangthCheck(netProducintTimeMinutes, 5)) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("net_producint_time_minutes"), netProducintTimeMinutes, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                        try {
                            Integer.valueOf(netProducintTimeMinutes);
                        } catch (NumberFormatException e) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("net_producint_time_minutes"), netProducintTimeMinutes, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_num_over_zero")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }
                    
                    if (StringUtils.isNotEmpty(suspendedTimeMinutes)) {
                        if (fileUtil.maxLangthCheck(suspendedTimeMinutes, 5)) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("suspended_time_minutes"), suspendedTimeMinutes, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                        try {
                            Integer.valueOf(suspendedTimeMinutes);
                        } catch (NumberFormatException e) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("suspended_time_minutes"), suspendedTimeMinutes, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_num_over_zero")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }
                    
                    if (StringUtils.isNotEmpty(prevLotNumber) && fileUtil.maxLangthCheck(prevLotNumber, 100)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("prev_lot_number"), prevLotNumber, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (StringUtils.isNotEmpty(materialLotNo01) && fileUtil.maxLangthCheck(materialLotNo01, 100)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_lot_no_with_parameter").replace("%s", "01"), materialLotNo01, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (StringUtils.isNotEmpty(materialLotNo02) && fileUtil.maxLangthCheck(materialLotNo02, 100)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_lot_no_with_parameter").replace("%s", "02"), materialLotNo02, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (StringUtils.isNotEmpty(materialLotNo03) && fileUtil.maxLangthCheck(materialLotNo03, 100)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_lot_no_with_parameter").replace("%s", "03"), materialLotNo03, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (StringUtils.isNotEmpty(materialAmount01) && !fileUtil.isDouble(materialAmount01)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_amount_with_parameter").replace("%s", "01"), materialAmount01, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (StringUtils.isNotEmpty(materialAmount02) && !fileUtil.isDouble(materialAmount02)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_amount_with_parameter").replace("%s", "02"), materialAmount02, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (StringUtils.isNotEmpty(materialAmount03) && !fileUtil.isDouble(materialAmount03)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_amount_with_parameter").replace("%s", "01"), materialAmount03, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (StringUtils.isNotEmpty(materialPurgedAmount01) && !fileUtil.isDouble(materialPurgedAmount01)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_purged_amount_with_parameter").replace("%s", "01"), materialPurgedAmount01, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (StringUtils.isNotEmpty(materialPurgedAmount02) && !fileUtil.isDouble(materialPurgedAmount02)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_purged_amount_with_parameter").replace("%s", "02"), materialPurgedAmount02, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }
                    
                    if (StringUtils.isNotEmpty(materialPurgedAmount03) && !fileUtil.isDouble(materialPurgedAmount03)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("material_purged_amount_with_parameter").replace("%s", "03"), materialPurgedAmount03, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_over_length")));
                        failedCount = failedCount + 1;
                        continue;
                    }

                    if (StringUtils.isEmpty(componentCode)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    } else {
                        mstComponent = mstComponentService.getMstComponent(componentCode);
                        if (mstComponent == null) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }
                    
                    if (StringUtils.isEmpty(procedureCode)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("procedure_code"), procedureCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    } else {
                        mstProcedure = mstProcedureService.getMstProcedureByComponentIdAndProcedureCode(mstComponent.getId(), procedureCode);
                        if (mstProcedure == null) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("procedure_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }
                    
                    if (StringUtils.isNotEmpty(directionCode)) {
                        tblDirection = tblDirectionService.getTblDirectionByDirectionCode(directionCode);
                        if (tblDirection == null) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("direction_code"), directionCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }
                    
                    if (StringUtils.isEmpty(productionUserId)) {
                        //エラー情報をログファイルに記入
                        fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("production_user_id"), productionUserId, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("msg_error_not_null")));
                        failedCount = failedCount + 1;
                        continue;
                    } else {
                        mstPerson = mstUserService.getMstUser(productionUserId);
                        if (mstPerson == null) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("production_user_id"), productionUserId, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }
                    
                    if (StringUtils.isNotEmpty(machineId)) {
                        mstMachine = mstMachineService.getMstMachineByMachineId(machineId);
                        if (mstMachine == null) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("machine_id"), machineId, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }
                    
                    if (StringUtils.isNotEmpty(moldId)) {
                        mstMold = mstMoldService.getMstMoldByMoldId(moldId);
                        if (mstMold == null) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("mold_id"), moldId, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                            failedCount = failedCount + 1;
                            continue;
                        }
                    }
                    
                    if (StringUtils.isNotEmpty(materialName01)) {
                        MstMaterialList materialList01 = mstMaterialService.getMaterialByCodeOrName(null, materialName01);
                        if (materialList01 == null || materialList01.getMstMaterialList() == null || materialList01.getMstMaterialList().isEmpty() || materialList01.getMstMaterialList().get(0) == null) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("procedure_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                            failedCount = failedCount + 1;
                            continue;
                        } else {
                            mstMaterial01 = materialList01.getMstMaterialList().get(0);
                        }
                    }
                    
                    if (StringUtils.isNotEmpty(materialName02)) {
                        MstMaterialList materialList02 = mstMaterialService.getMaterialByCodeOrName(null, materialName02);
                        if (materialList02 == null || materialList02.getMstMaterialList() == null || materialList02.getMstMaterialList().isEmpty() || materialList02.getMstMaterialList().get(0) == null) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("procedure_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                            failedCount = failedCount + 1;
                            continue;
                        } else {
                            mstMaterial02 = materialList02.getMstMaterialList().get(0);
                        }
                    }
                    
                    if (StringUtils.isNotEmpty(materialName03)) {
                        MstMaterialList materialList03 = mstMaterialService.getMaterialByCodeOrName(null, materialName03);
                        if (materialList03 == null || materialList03.getMstMaterialList() == null || materialList03.getMstMaterialList().isEmpty() || materialList03.getMstMaterialList().get(0) == null) {
                            //エラー情報をログファイルに記入
                            fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("procedure_code"), componentCode, dictMap.get("error"), 1, dictMap.get("error_detail"), dictMap.get("mst_error_record_not_found")));
                            failedCount = failedCount + 1;
                            continue;
                        } else {
                            mstMaterial03 = materialList03.getMstMaterialList().get(0);
                        }
                    }

                    String endDatetimeStr = productionDate + " " + productionEndDatetime + DateFormat.TIME_SECONDS;
                    if (!productionKey.equals(productionDate + productionStartDatetime + productionEndDatetime)) {
                        // 生産実績登録
                        production = new TblProduction();
                        production.setId(IDGenerator.generate());
                        production.setProductionDate(DateFormat.strToDate(productionDate)); // 生産日
                        production.setStartDatetime(DateFormat.strToDatetime(productionDate + " " + productionStartDatetime + DateFormat.TIME_SECONDS)); // 開始日時
                        production.setEndDatetime(DateFormat.strToDatetime(endDatetimeStr)); // 終了日時
                        production.setPersonUuid(mstPerson.getUuid()); // 生産者UUID
                        production.setProdDepartment(department); // 生産場所
                        production.setMachineUuid(mstMachine == null ? null : mstMachine.getUuid()); // 設備UUIID
                        production.setMoldUuid(mstMold == null ? null : mstMold.getUuid()); // 金型UUID
                        production.setLotNumber(StringUtils.stripToNull(lotNumber)); // ロット番号
                        production.setDirectionId(tblDirection == null ? null : tblDirection.getId());
                        production.setPrevLotNumber(StringUtils.stripToNull(prevLotNumber)); // 前ロット番号
                        production.setShotCount(Integer.valueOf(shotCount)); // ショット数
                        production.setStatus(9); // 状態
                        production.setNetProducintTimeMinutes(StringUtils.isEmpty(netProducintTimeMinutes) ? 0 : Integer.valueOf(netProducintTimeMinutes)); // 実稼動時間
                        production.setSuspendedTimeMinutes(StringUtils.isEmpty(suspendedTimeMinutes) ? 0 : Integer.valueOf(suspendedTimeMinutes)); // 中断時間
                        production.setCreateDate(new Date());
                        production.setCreateUserUuid(loginUser.getUserUuid());
                        production.setUpdateDate(production.getCreateDate());
                        production.setUpdateUserUuid(loginUser.getUserUuid());

                        entityManager.persist(production);
                        
                        productionKey = productionDate + productionStartDatetime + productionEndDatetime;
                    }
                    
                    // 生産実績詳細登録
                    TblProductionDetail productionDetail = new TblProductionDetail();
                    productionDetail.setId(IDGenerator.generate());
                    productionDetail.setProductionId(production);
                    productionDetail.setComponentId(mstComponent.getId());
                    productionDetail.setProcedureId(mstProcedure.getId());
                    productionDetail.setCountPerShot(Integer.valueOf(countPerShot));
                    productionDetail.setCompleteCount(Integer.valueOf(completeCount));
                    productionDetail.setDefectCount(Integer.valueOf(defectCount));
                    productionDetail.setMaterial01Id(mstMaterial01 == null ? null : mstMaterial01.getId());
                    productionDetail.setMaterial01LotNo(materialLotNo01);
                    productionDetail.setMaterial01Amount(StringUtils.isEmpty(materialAmount01) ? null : new BigDecimal(materialAmount01));
                    productionDetail.setMaterial01PurgedAmount(StringUtils.isEmpty(materialPurgedAmount01) ? null : new BigDecimal(materialPurgedAmount01));
                    productionDetail.setMaterial02Id(mstMaterial02 == null ? null : mstMaterial02.getId());
                    productionDetail.setMaterial02LotNo(materialLotNo02);
                    productionDetail.setMaterial02Amount(StringUtils.isEmpty(materialAmount02) ? null : new BigDecimal(materialAmount02));
                    productionDetail.setMaterial02PurgedAmount(StringUtils.isEmpty(materialPurgedAmount02) ? null : new BigDecimal(materialPurgedAmount02));
                    productionDetail.setMaterial03Id(mstMaterial03 == null ? null : mstMaterial03.getId());
                    productionDetail.setMaterial03LotNo(materialLotNo03);
                    productionDetail.setMaterial03Amount(StringUtils.isEmpty(materialAmount03) ? null : new BigDecimal(materialAmount03));
                    productionDetail.setMaterial03PurgedAmount(StringUtils.isEmpty(materialPurgedAmount03) ? null : new BigDecimal(materialPurgedAmount03));
                    productionDetail.setCreateDate(new Date());
                    productionDetail.setCreateUserUuid(loginUser.getUserUuid());
                    productionDetail.setUpdateDate(productionDetail.getCreateDate());
                    productionDetail.setUpdateUserUuid(loginUser.getUserUuid());
                    
                    entityManager.persist(productionDetail);

                    //エラー情報をログファイルに記入
                    fileUtil.writeInfoToFile(logFile, fileUtil.outValue(dictMap.get("row_number"), i, dictMap.get("component_code"), componentCode, dictMap.get("error"), 0, dictMap.get("db_process"), dictMap.get("msg_data_modified")));
                    addedCount = addedCount + 1;
                    
                    // 当該工程番号の前工程番号を取得する
                    MstProcedure prevMstProcedure = mstProcedureService.getPrevProcedureCode(mstComponent.getId(), mstProcedure.getProcedureCode());
                    // 部品在庫計算
                    BasicResponse basicResponse = tblStockService.doTblStock(
                        mstComponent.getComponentCode(),
                        mstProcedure,
                        prevMstProcedure,
                        CommonConstants.STORE,
                        productionDetail.getCompleteCount(),
                        endDatetimeStr,
                        production == null ? "" : production.getLotNumber(),
                        0,
                        null,
                        CommonConstants.SHIPMENT_NO,
                        productionDetail.getTblComponentLotRelationVoList(),
                        loginUser.getUserUuid(),
                        loginUser.getLangId()
                    );

                    if (basicResponse.isError()) {
                        logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{Thread.currentThread().getStackTrace()[1].getMethodName(), basicResponse.getErrorMessage()});
                    }

                    if (StringUtils.isNotEmpty(productionDetail.getMaterial01Id())) {
                        BasicResponse materialResponse = tblMaterialStockService.doMaterialStock(mstMaterial01 == null ? null : mstMaterial01.getMaterialCode(),
                            productionDetail.getMaterial01LotNo(),
                            CommonConstants.DELIVERY,
                            productionDetail.getMaterial01Amount().add(productionDetail.getMaterial01PurgedAmount()),
                            endDatetimeStr,
                            productionDetail.getId(),
                            0,
                            null,
                            loginUser.getUserUuid(),
                            loginUser.getLangId(),
                            false);

                        if (materialResponse.isError()) {
                            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{Thread.currentThread().getStackTrace()[1].getMethodName(), basicResponse.getErrorMessage()});
                        }
                    }
                    if (StringUtils.isNotEmpty(productionDetail.getMaterial02Id())) {
                        BasicResponse materialResponse = tblMaterialStockService.doMaterialStock(mstMaterial02 == null ? null : mstMaterial02.getMaterialCode(),
                            productionDetail.getMaterial02LotNo(),
                            CommonConstants.DELIVERY,
                            productionDetail.getMaterial02Amount().add(productionDetail.getMaterial02PurgedAmount()),
                            endDatetimeStr,
                            productionDetail.getId(),
                            0,
                            null,
                            loginUser.getUserUuid(),
                            loginUser.getLangId(),
                            false);

                        if (materialResponse.isError()) {
                            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{Thread.currentThread().getStackTrace()[1].getMethodName(), basicResponse.getErrorMessage()});
                        }
                    }
                    if (StringUtils.isNotEmpty(productionDetail.getMaterial03Id())) {
                        BasicResponse materialResponse = tblMaterialStockService.doMaterialStock(mstMaterial03 == null ? null : mstMaterial03.getMaterialCode(),
                            productionDetail.getMaterial03LotNo(),
                            CommonConstants.DELIVERY,
                            productionDetail.getMaterial03Amount().add(productionDetail.getMaterial03PurgedAmount()),
                            endDatetimeStr,
                            productionDetail.getId(),
                            0,
                            null,
                            loginUser.getUserUuid(),
                            loginUser.getLangId(),
                            false);

                        if (materialResponse.isError()) {
                            logger.log(Level.FINE, "  <--- [[{0}]] End has error [[{1}]]", new Object[]{Thread.currentThread().getStackTrace()[1].getMethodName(), basicResponse.getErrorMessage()});
                        }
                    }
                }

                // リターン情報
                succeededCount = addedCount + updatedCount + deletedCount;
                importResultResponse.setTotalCount(readList.size() - 1);
                importResultResponse.setSucceededCount(succeededCount);
                importResultResponse.setAddedCount(addedCount);
                importResultResponse.setUpdatedCount(updatedCount);
                importResultResponse.setDeletedCount(deletedCount);
                importResultResponse.setFailedCount(failedCount);
                importResultResponse.setLog(logFileUuid);

                //アップロードログをテーブルに書き出し
                TblCsvImport tblCsvImport = new TblCsvImport();
                tblCsvImport.setImportUuid(IDGenerator.generate());
                tblCsvImport.setImportUserUuid(loginUser.getUserUuid());
                tblCsvImport.setImportDate(new Date());
                tblCsvImport.setImportTable("tbl_production");

                TblUploadFile tblUploadFile = new TblUploadFile();
                tblUploadFile.setFileUuid(fileUuid);
                tblCsvImport.setUploadFileUuid(tblUploadFile);
                MstFunction mstFunction = new MstFunction();
                mstFunction.setId(CommonConstants.FUN_ID_PRODUCTION_LIST);
                tblCsvImport.setFunctionId(mstFunction);
                tblCsvImport.setRecordCount(readList.size() - 1);
                tblCsvImport.setSuceededCount(Integer.parseInt(String.valueOf(succeededCount)));
                tblCsvImport.setAddedCount(Integer.parseInt(String.valueOf(addedCount)));
                tblCsvImport.setUpdatedCount(Integer.parseInt(String.valueOf(updatedCount)));
                tblCsvImport.setDeletedCount(Integer.parseInt(String.valueOf(deletedCount)));
                tblCsvImport.setFailedCount(Integer.parseInt(String.valueOf(failedCount)));
                tblCsvImport.setLogFileUuid(logFileUuid);
                tblCsvImport.setLogFileName(FileUtil.getLogFileName(dictMap.get("production_import")));

                tblCsvImportService.createCsvImpor(tblCsvImport);

            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }

        return importResultResponse;
    }
}
