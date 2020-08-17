/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.detail;

import com.kmcj.karte.resources.work.*;
import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVo;
import com.kmcj.karte.resources.component.company.MstComponentCompanyVoList;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.files.TblCsvExport;
import com.kmcj.karte.resources.files.TblCsvExportService;
import com.kmcj.karte.resources.function.MstFunction;
import com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetail;
import com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetailList;
import com.kmcj.karte.resources.material.MstMaterial;
import com.kmcj.karte.resources.material.MstMaterialService;
import com.kmcj.karte.resources.procedure.MstProcedure;
import com.kmcj.karte.resources.procedure.MstProcedureService;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.production.TblProductionVo;
import com.kmcj.karte.resources.production.lot.balance.TblProductionLotBalance;
import com.kmcj.karte.resources.production.lot.balance.TblProductionLotBalanceService;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import com.kmcj.karte.util.Pager;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
//import com.kmcj.karte.resources.production.plan.appropriation.TblProductionPlanAppropriation;

/**
 * 作業実績テーブルサービス
 * @author t.ariki
 */
@Dependent
public class TblProductionDetailService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private TblCsvExportService tblCsvExportService;
    
    @Inject
    private MstComponentService mstComponentService;
    
    @Inject
    MstProcedureService mstProcedureService;
    
    @Inject
    TblProductionLotBalanceService tblProductionLotBalanceService;
    
    @Inject
    MstMaterialService mstMaterialService;
    
    private Logger logger = Logger.getLogger(TblProductionDetailService.class.getName());
    
    private final static Map<String, String> orderKey;

    static {
        orderKey = new HashMap<>();
        orderKey.put("productionId.productionDate", " ORDER BY tp.productionDate ");// 生産日
        orderKey.put("productionId.startDatetime", " ORDER BY tp.startDatetime ");// 開始時刻
        orderKey.put("productionId.endDatetime", " ORDER BY tp.endDatetime ");// 終了時刻
        orderKey.put("productionId.mstUser.userId", " ORDER BY mu.userId ");// 生産者ＩＤ
        orderKey.put("productionId.mstUser.userName", " ORDER BY mu.userName ");// 生産者氏名
//        orderKey.put("productionId.mstUser.department", " ORDER BY mu.department ");// 部署
        orderKey.put("productionId.prodDepartment", " ORDER BY tp.prodDepartment ");// 生産場所
        orderKey.put("componentCode", " ORDER BY mc.componentCode ");// 部品コード
        orderKey.put("componentName", " ORDER BY mc.componentName ");// 部品名称
        orderKey.put("procedureCode", " ORDER BY mp.procedureCode ");// 部品工程番号
        orderKey.put("procedureName", " ORDER BY mp.procedureName ");// 部品工程名称
        orderKey.put("productionId.lotNumber", " ORDER BY tp.lotNumber ");// ロット番号
        orderKey.put("productionId.prevLotNumber", " ORDER BY tp.prevLotNumber ");// 前ロット番号
        orderKey.put("productionId.directionCode", " ORDER BY td.directionCode ");// directionCode
        orderKey.put("productionId.shotCount", " ORDER BY tp.shotCount ");// ショット数
        orderKey.put("productionId.disposedShotCount", " ORDER BY tp.disposedShotCount ");// 捨てショット数
        orderKey.put("countPerShot", " ORDER BY tblProductionDetail.countPerShot ");// 取り数
        orderKey.put("defectCount", " ORDER BY tblProductionDetail.defectCount ");// 不良数
        orderKey.put("completeCount", " ORDER BY tblProductionDetail.completeCount ");// 完成数
        orderKey.put("tblProduction.moldId", " ORDER BY mold.moldId ");// 金型ＩＤ
        orderKey.put("tblProduction.moldName", " ORDER BY mold.moldName ");// 金型名称
        orderKey.put("tblProduction.moldType", " ORDER BY mold.moldType ");// 金型種類
        orderKey.put("productionId.mstMachine.machineId", " ORDER BY machine.machineId ");// 設備ＩＤ
        orderKey.put("productionId.mstMachine.machineName", " ORDER BY machine.machineName ");// 設備名称
        orderKey.put("tblProduction.mstMachine.machineType", " ORDER BY machine.machineType ");
        orderKey.put("material01Name", " ORDER BY mm1.materialName ");// 材料01名称
        orderKey.put("material01LotNo", " ORDER BY tblProductionDetail.material01LotNo ");// 材料01ロット番号
        orderKey.put("material01Amount", " ORDER BY tblProductionDetail.material01Amount ");// 材料01使用量
        orderKey.put("material01PurgedAmount", " ORDER BY tblProductionDetail.material01PurgedAmount ");// 材料01ページ量
        orderKey.put("material02Name", " ORDER BY mm2.materialName ");// 材料02名称
        orderKey.put("material02LotNo", " ORDER BY tblProductionDetail.material02LotNo ");// 材料02ロット番号
        orderKey.put("material02Amount", " ORDER BY tblProductionDetail.material02Amount ");// 材料02使用量
        orderKey.put("material02PurgedAmount", " ORDER BY tblProductionDetail.material02PurgedAmount ");// 材料02ページ量
        orderKey.put("material03Name", " ORDER BY mm3.materialName ");// 材料03名称
        orderKey.put("material03LotNo", " ORDER BY tblProductionDetail.material03LotNo ");// 材料03ロット番号
        orderKey.put("material03Amount", " ORDER BY tblProductionDetail.material03Amount ");// 材料03使用量
        orderKey.put("material03PurgedAmount", " ORDER BY tblProductionDetail.material03PurgedAmount ");// 材料03ページ量
        orderKey.put("productionId.netProducintTimeMinutes", " ORDER BY tp.netProducintTimeMinutes ");// 実稼動時間
        orderKey.put("productionId.suspendedTimeMinutes", " ORDER BY tp.suspendedTimeMinutes ");// 中断時間

    }
    
    /**
     * 生産計画に関連づく生産実績明細の件数取得
     * @param productionPlanId
     * @return TblProductionDetailList
     */
    public CountResponse getProductionsCountLinkedWithPlan(String productionPlanId) {
         CountResponse response = new CountResponse();
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT COUNT(tblProductionDetail) FROM TblProductionDetail tblProductionDetail"
                        + " WHERE EXISTS ( "
                        +       " SELECT planApp FROM TblProductionPlanAppropriation planApp"
                        +       " WHERE planApp.productionPlanId = :productionPlanId"
                        +       " AND planApp.productionDetailId = tblProductionDetail.id)"
        );
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("productionPlanId", productionPlanId);
        Long count = (Long)query.getSingleResult();
        response.setCount(count.longValue());
        return response;
    }

        
    /**
     * 生産計画に関連づく生産実績明細取得
     * @param productionPlanId
     * @return TblProductionDetailList
     */
    public TblProductionDetailList getProductionsLinkedWithPlan(String productionPlanId) {
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT tblProductionDetail FROM TblProductionDetail tblProductionDetail"
                        + " JOIN FETCH tblProductionDetail.tblProduction"
                        + " LEFT JOIN FETCH tblProductionDetail.mstComponent"
                        + " LEFT JOIN FETCH tblProductionDetail.mstProcedure"
                        + " LEFT JOIN FETCH tblProductionDetail.mstMaterial01"
                        + " LEFT JOIN FETCH tblProductionDetail.mstMaterial02"
                        + " LEFT JOIN FETCH tblProductionDetail.mstMaterial03"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.mstUser"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.tblDirection"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.mstMold"
                        + " WHERE EXISTS ( "
                        +       " SELECT planApp FROM TblProductionPlanAppropriation planApp"
                        +       " WHERE planApp.productionPlanId = :productionPlanId"
                        +       " AND planApp.productionDetailId = tblProductionDetail.id)"
        );
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("productionPlanId", productionPlanId);
        List list = query.getResultList();
        
        TblProductionDetailList response = new TblProductionDetailList();
        response.setTblProductionDetails(list);
        return response;
    }

    /**
     * 生産実績テーブル検索条件指定検索 (件数取得)
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
    public CountResponse getProductionDetailsCountByCondition(
           String componentCode     // 部品コード
           ,java.util.Date productionDateFrom // 生産日From yyyy-mm-dd
           ,java.util.Date productionDateTo // 生産日To yyyy-mm-dd
           ,String directionCode // 手配番号
           ,String userId // 生産者ID
           ,String userName // 生産者氏名
           ,Integer department // 生産場所
           ,String moldId // 金型ID
           ,Integer moldType // 金型種類
           ,String machineId //設備ID
           ,java.util.Date workStartDateTime // 選択日付の業務開始時刻
           ,java.util.Date nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
    ){
        List list = getProductionDetailsByCondition(componentCode, productionDateFrom, productionDateTo
            , directionCode, userId, userName, department, moldId, moldType, machineId, workStartDateTime, nextDayWorkStartDateTime, "count");
        
        CountResponse count = new CountResponse();
        count.setCount(((Long) list.get(0)));
        return count;
    }
    /**
     * 生産実績テーブル検索条件指定検索 (実レコード取得用ラッパー)
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
    public TblProductionDetailList getProductionDetailsByCondition(
            String componentCode     // 部品コード
           ,java.util.Date productionDateFrom // 生産日From yyyy-mm-dd
           ,java.util.Date productionDateTo // 生産日To yyyy-mm-dd
           ,String directionCode // 手配番号
           ,String userId // 生産者ID
           ,String userName // 生産者氏名
           ,Integer department // 生産場所
           ,String moldId // 金型ID
           ,Integer moldType // 金型種類
           ,String machineId //設備ID
           ,java.util.Date workStartDateTime // 選択日付の業務開始時刻
           ,java.util.Date nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
    ) {
        List list = getProductionDetailsByCondition(componentCode, productionDateFrom, productionDateTo
            , directionCode, userId, userName, department, moldId, moldType, machineId, workStartDateTime, nextDayWorkStartDateTime, "");
        
        TblProductionDetailList response = new TblProductionDetailList();
        response.setTblProductionDetails(list);
        return response;
    }    
    /**
     * 生産実績テーブル検索条件指定検索
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
     * @param flag
     * @return 
     */
    public List getProductionDetailsByCondition(
            String componentCode     // 部品コード
           ,java.util.Date productionDateFrom // 生産日From yyyy-mm-dd
           ,java.util.Date productionDateTo // 生産日To yyyy-mm-dd
           ,String directionCode // 手配番号
           ,String userId // 生産者ID
           ,String userName // 生産者氏名
           ,Integer department // 生産場所
           ,String moldId // 金型ID
           ,Integer moldType // 金型種類
           ,String machineId //設備ID
           ,java.util.Date workStartDateTime // 選択日付の業務開始時刻
           ,java.util.Date nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
           ,String flag     // カウント取得時のフラグ
    ) {
        /* 
         * JPQLで条件定義
         *   結合条件はエンティティクラスに定義済み
         */
        StringBuilder sql = new StringBuilder();
        if("count".equals(flag)){
            sql = sql.append("SELECT count(tblProductionDetail) FROM TblProductionDetail tblProductionDetail");
        } else {
            sql = sql.append("SELECT tblProductionDetail FROM TblProductionDetail tblProductionDetail");
        }
        sql = sql.append(" JOIN FETCH tblProductionDetail.tblProduction"
                        + " LEFT JOIN FETCH tblProductionDetail.mstComponent"
                        + " LEFT JOIN FETCH tblProductionDetail.mstProcedure"
                        + " LEFT JOIN FETCH tblProductionDetail.mstMaterial01"
                        + " LEFT JOIN FETCH tblProductionDetail.mstMaterial02"
                        + " LEFT JOIN FETCH tblProductionDetail.mstMaterial03"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.mstUser"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.tblDirection"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.mstMold"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.mstMachine"
                        + " WHERE 1=1 "
        );
        // 部品コード 部分一致
        if (componentCode != null && !"".equals(componentCode)) {
            sql.append(" and tblProductionDetail.mstComponent.componentCode like :componentCode ");
        }
        // 生産日From   同日以上
        if (productionDateFrom != null) {
            sql = sql.append(" and tblProductionDetail.tblProduction.productionDate >= :productionDateFrom ");
        }
        // 生産日From   同日以下
        if (productionDateTo != null) {
            sql = sql.append(" and tblProductionDetail.tblProduction.productionDate <= :productionDateTo ");
        }
        // 手配番号 部分一致
        if (directionCode != null && !"".equals(directionCode)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.tblDirection.directionCode like :directionCode ");
        }
        // 生産者ID 部分一致
        if (userId != null && !"".equals(userId)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstUser.userId like :userId ");
        }
        // 生産者氏名 部分一致
        if (userName != null && !"".equals(userName)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstUser.userName like :userName ");
        }
        // 生産場所 完全一致
        if (department != null && department != 0) {
            //sql = sql.append(" and tblProductionDetail.tblProduction.mstUser.department = :department ");
            sql = sql.append(" and tblProductionDetail.tblProduction.prodDepartment = :department ");
        }
        // 金型ID 部分一致
        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstMold.moldId like :moldId ");
        }
        // 金型種類 完全一致
        if (moldType != null && moldType != 0) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstMold.moldType = :moldType ");
        }
       
        // 設備ID＝選択されている設備ID
        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstMachine.machineId = :machineId ");
        }
        
        if (nextDayWorkStartDateTime != null) {
            // 開始日時<=選択日付の翌日の業務開始時刻
            sql = sql.append(" and (tblProductionDetail.tblProduction.startDatetime <= :startDatetime ");
        }
        
        if (workStartDateTime != null) {
            // 終了日時>=選択日付の業務開始時刻
            sql = sql.append(" and tblProductionDetail.tblProduction.endDatetime >= :endDatetime  ");
        }
        
        if (nextDayWorkStartDateTime != null) {
            // 開始日時<=選択日付の翌日の業務開始時刻且つ終了日時 IS NULL
            sql = sql.append(" or (tblProductionDetail.tblProduction.endDatetime is null and tblProductionDetail.tblProduction.startDatetime <= :startDatetime)) ");
        }
        
        // 生産日　降順, 開始時刻 降順
        sql = sql.append(" ORDER BY tblProductionDetail.tblProduction.productionDate DESC, tblProductionDetail.tblProduction.startDatetime DESC ");
        Query query = entityManager.createQuery(sql.toString());
        
        /*
         * パラメータバインド
         */
        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode+  "%");
        }
        if (productionDateFrom != null) {
            query.setParameter("productionDateFrom", productionDateFrom);
        }
        if (productionDateTo != null) {
            query.setParameter("productionDateTo", productionDateTo);
        }
        if (directionCode != null && !"".equals(directionCode)) {
            query.setParameter("directionCode", "%" + directionCode+  "%");
        }
        if (userId != null && !"".equals(userId)) {
            query.setParameter("userId", "%" + userId+  "%");
        }
        if (userName != null && !"".equals(userName)) {
            query.setParameter("userName", "%" + userName+  "%");
        }
        if (department != null && department != 0) {
            query.setParameter("department", department);
        }
        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", "%" + moldId+  "%");
        }
        // 金型種類 完全一致
        if (moldType != null && moldType != 0) {
            query.setParameter("moldType", moldType);
        }
        // 設備ID
        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId",  machineId.trim());
        }
        
        if (nextDayWorkStartDateTime != null) {
             query.setParameter("startDatetime", nextDayWorkStartDateTime);
        }
        
        if (workStartDateTime != null) {
             query.setParameter("endDatetime", workStartDateTime);
        }
        
        List list = query.getResultList();
        return list;
    }
    
    
    /**
     * 生産実績テーブル検索条件指定検索 (実レコード取得用ラッパー)
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
    public TblProductionDetailList getProductionDetailsByCond(
            String componentCode     // 部品コード
           ,java.util.Date productionDateFrom // 生産日From yyyy-mm-dd
           ,java.util.Date productionDateTo // 生産日To yyyy-mm-dd
           ,String directionCode // 手配番号
           ,String userId // 生産者ID
           ,String userName // 生産者氏名
           ,Integer department // 生産場所
           ,String moldId // 金型ID
           ,Integer moldType // 金型種類
           ,String machineId //設備ID
           ,Integer machineType
           ,java.util.Date workStartDateTime // 選択日付の業務開始時刻
           ,java.util.Date nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
    ) {
        List list = getProductionDetailsByCond(componentCode, productionDateFrom, productionDateTo
            , directionCode, userId, userName, department, moldId, moldType, machineId, machineType, workStartDateTime, nextDayWorkStartDateTime, "");
        
        TblProductionDetailList response = new TblProductionDetailList();
        response.setTblProductionDetails(list);
        return response;
    }
    
    /**
     * 生産実績テーブル検索条件指定検索
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
     * @param flag
     * @return 
     */
    public List getProductionDetailsByCond(
            String componentCode     // 部品コード
           ,java.util.Date productionDateFrom // 生産日From yyyy-mm-dd
           ,java.util.Date productionDateTo // 生産日To yyyy-mm-dd
           ,String directionCode // 手配番号
           ,String userId // 生産者ID
           ,String userName // 生産者氏名
           ,Integer department // 生産場所
           ,String moldId // 金型ID
           ,Integer moldType // 金型種類
           ,String machineId //設備ID
           ,Integer machineType // 金型種類 
           ,java.util.Date workStartDateTime // 選択日付の業務開始時刻
           ,java.util.Date nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
           ,String flag     // カウント取得時のフラグ
    ) {
        /* 
         * JPQLで条件定義
         *   結合条件はエンティティクラスに定義済み
         */
        StringBuilder sql = new StringBuilder();
        if("count".equals(flag)){
            sql = sql.append("SELECT count(tblProductionDetail) FROM TblProductionDetail tblProductionDetail");
        } else {
            sql = sql.append("SELECT tblProductionDetail FROM TblProductionDetail tblProductionDetail");
        }
        sql = sql.append(" JOIN FETCH tblProductionDetail.tblProduction"
                        + " LEFT JOIN FETCH tblProductionDetail.mstComponent"
                        + " LEFT JOIN FETCH tblProductionDetail.mstProcedure"
                        + " LEFT JOIN FETCH tblProductionDetail.mstMaterial01"
                        + " LEFT JOIN FETCH tblProductionDetail.mstMaterial02"
                        + " LEFT JOIN FETCH tblProductionDetail.mstMaterial03"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.mstUser"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.tblDirection"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.mstMold"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.mstMachine"
                        + " WHERE 1=1 "
        );
        // 部品コード 部分一致
        if (componentCode != null && !"".equals(componentCode)) {
            sql.append(" and tblProductionDetail.mstComponent.componentCode like :componentCode ");
        }
        // 生産日From   同日以上
        if (productionDateFrom != null) {
            sql = sql.append(" and tblProductionDetail.tblProduction.productionDate >= :productionDateFrom ");
        }
        // 生産日From   同日以下
        if (productionDateTo != null) {
            sql = sql.append(" and tblProductionDetail.tblProduction.productionDate <= :productionDateTo ");
        }
        // 手配番号 部分一致
        if (directionCode != null && !"".equals(directionCode)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.tblDirection.directionCode like :directionCode ");
        }
        // 生産者ID 部分一致
        if (userId != null && !"".equals(userId)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstUser.userId like :userId ");
        }
        // 生産者氏名 部分一致
        if (userName != null && !"".equals(userName)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstUser.userName like :userName ");
        }
        // 生産場所 完全一致
        if (department != null && department != 0) {
            //sql = sql.append(" and tblProductionDetail.tblProduction.mstUser.department = :department ");
            sql = sql.append(" and tblProductionDetail.tblProduction.prodDepartment = :department ");
        }
        // 金型ID 部分一致
        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstMold.moldId like :moldId ");
        }
        // 金型種類 完全一致
        if (moldType != null && moldType != 0) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstMold.moldType = :moldType ");
        }
       
        // 設備ID＝選択されている設備ID
        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstMachine.machineId like :machineId ");
        }
        
        if (machineType != null && machineType != 0) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstMachine.machineType = :machineType ");
        }
        
        if (nextDayWorkStartDateTime != null) {
            // 開始日時<=選択日付の翌日の業務開始時刻
            sql = sql.append(" and (tblProductionDetail.tblProduction.startDatetime <= :startDatetime ");
        }
        
        if (workStartDateTime != null) {
            // 終了日時>=選択日付の業務開始時刻
            sql = sql.append(" and tblProductionDetail.tblProduction.endDatetime >= :endDatetime  ");
        }
        
        if (nextDayWorkStartDateTime != null) {
            // 開始日時<=選択日付の翌日の業務開始時刻且つ終了日時 IS NULL
            sql = sql.append(" or (tblProductionDetail.tblProduction.endDatetime is null and tblProductionDetail.tblProduction.startDatetime <= :startDatetime)) ");
        }
        
        // 生産日　降順, 開始時刻 降順
        sql = sql.append(" ORDER BY tblProductionDetail.tblProduction.productionDate DESC, tblProductionDetail.tblProduction.startDatetime DESC ");
        Query query = entityManager.createQuery(sql.toString());
        
        /*
         * パラメータバインド
         */
        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode+  "%");
        }
        if (productionDateFrom != null) {
            query.setParameter("productionDateFrom", productionDateFrom);
        }
        if (productionDateTo != null) {
            query.setParameter("productionDateTo", productionDateTo);
        }
        if (directionCode != null && !"".equals(directionCode)) {
            query.setParameter("directionCode", "%" + directionCode+  "%");
        }
        if (userId != null && !"".equals(userId)) {
            query.setParameter("userId", "%" + userId+  "%");
        }
        if (userName != null && !"".equals(userName)) {
            query.setParameter("userName", "%" + userName+  "%");
        }
        if (department != null && department != 0) {
            query.setParameter("department", department);
        }
        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", "%" + moldId+  "%");
        }
        // 金型種類 完全一致
        if (moldType != null && moldType != 0) {
            query.setParameter("moldType", moldType);
        }
        // 設備ID
        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", "%" + machineId + "%");
        }
        
        if (machineType != null && machineType != 0) {
            query.setParameter("machineType", machineType);
        }
        
        if (nextDayWorkStartDateTime != null) {
             query.setParameter("startDatetime", nextDayWorkStartDateTime);
        }
        
        if (workStartDateTime != null) {
             query.setParameter("endDatetime", workStartDateTime);
        }
        
        List list = query.getResultList();
        return list;
    }
    
    /**
     * 生産実績明細取得(TblProductionDetailリスト型で返却)
     * 
     * @param productionId
     * @return 
     */
    public List<TblProductionDetail> getProductionDetailsByProductionId(String productionId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t FROM TblProductionDetail t JOIN FETCH t.tblProduction WHERE t.productionId.id = :productionId");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("productionId", productionId);
        List list = query.getResultList();
        return list;
    }
    
    /**
     * 生産実績明細1件取得(TblProductionDetail型で返却)
     * @param id
     * @return 
     */
    public TblProductionDetail getProductionDetailSingleById(String id) {
        Query query = entityManager.createNamedQuery("TblProductionDetail.findById");
        query.setParameter("id", id);
        try {
            return (TblProductionDetail)query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     * 生産実績明細を生産実績IDと部品IDで検索し1件取得(TblProductionDetail型で返却)
     * @param productionId
     * @param componentCode
     * @return
     */
    public TblProductionDetail getProductionDetailSingleByProducttionIdAndComponent(TblProduction productionId, String componentCode) {
        // 部品コードより部品IDを取得
        MstComponent mstComponent = mstComponentService.getMstComponent(componentCode);
        // 部品マスタが取得できない場合はNULL返却
        if (mstComponent == null) {
            return null;
        }
        StringBuilder sql;
        sql = new StringBuilder(
                "SELECT tblProductionDetail FROM TblProductionDetail tblProductionDetail WHERE 1=1 "
        );
        sql.append(" AND tblProductionDetail.productionId = :productionId ");
        sql.append(" AND tblProductionDetail.componentId = :componentId ");
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("productionId", productionId);
        query.setParameter("componentId", mstComponent.getId());
        try {
            return (TblProductionDetail)query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    /**
     * 生産実績テーブルCSV出力
     * @param tblProductionDetailList
     * @param loginUser
     * @return 
     */
    public FileReponse getProductionsCSV(TblProductionDetailList tblProductionDetailList, LoginUser loginUser) {
        /*
         * Header用意
         */
        String langId = loginUser.getLangId();
        String productionDate = mstDictionaryService.getDictionaryValue(langId, "production_date");
        String productionStartTime = mstDictionaryService.getDictionaryValue(langId, "production_start_time");
        String productionEndTime = mstDictionaryService.getDictionaryValue(langId, "production_end_time");
        String productionUserId = mstDictionaryService.getDictionaryValue(langId, "production_user_id");
        String productionUserName = mstDictionaryService.getDictionaryValue(langId, "production_user_name");
//        String workUserDepartment = mstDictionaryService.getDictionaryValue(langId, "work_user_department");
        String prodDepartment = mstDictionaryService.getDictionaryValue(langId, "prod_department");
        String componenCode = mstDictionaryService.getDictionaryValue(langId, "component_code");
        String componentName = mstDictionaryService.getDictionaryValue(langId, "component_name");
        String procedureCode = mstDictionaryService.getDictionaryValue(langId, "procedure_code");
        String procedureName = mstDictionaryService.getDictionaryValue(langId, "procedure_name");
        String lotNumber = mstDictionaryService.getDictionaryValue(langId, "lot_number");
        String prevLotNumber = mstDictionaryService.getDictionaryValue(langId, "prev_lot_number");
        // directionCode KM-1048
        String directionCode = mstDictionaryService.getDictionaryValue(langId, "direction_code");
        String shotCount = mstDictionaryService.getDictionaryValue(langId, "shot_count");
        String countPerShot = mstDictionaryService.getDictionaryValue(langId, "count_per_shot");
        String defectCount = mstDictionaryService.getDictionaryValue(langId, "defect_count");
        String completeCount = mstDictionaryService.getDictionaryValue(langId, "complete_count");
        String moldId = mstDictionaryService.getDictionaryValue(langId, "mold_id");
        String moldName = mstDictionaryService.getDictionaryValue(langId, "mold_name");
        String moldType = mstDictionaryService.getDictionaryValue(langId, "mold_type");
        String machineId = mstDictionaryService.getDictionaryValue(langId, "machine_id");
        String machineName = mstDictionaryService.getDictionaryValue(langId, "machine_name");
        String material01Name = mstDictionaryService.getDictionaryValue(langId, "material_name_with_parameter").replace("%s", "01");
        String material01LotNo = mstDictionaryService.getDictionaryValue(langId, "material_lot_no_with_parameter").replace("%s", "01");;
        String material01Amount = mstDictionaryService.getDictionaryValue(langId, "material_amount_with_parameter").replace("%s", "01");;
        String material01PurgedAmount = mstDictionaryService.getDictionaryValue(langId, "material_purged_amount_with_parameter").replace("%s", "01");
        String material02Name = mstDictionaryService.getDictionaryValue(langId, "material_name_with_parameter").replace("%s", "02");
        String material02LotNo = mstDictionaryService.getDictionaryValue(langId, "material_lot_no_with_parameter").replace("%s", "02");
        String material02Amount = mstDictionaryService.getDictionaryValue(langId, "material_amount_with_parameter").replace("%s", "02");
        String material02PurgedAmount = mstDictionaryService.getDictionaryValue(langId, "material_purged_amount_with_parameter").replace("%s", "02");
        String material03Name = mstDictionaryService.getDictionaryValue(langId, "material_name_with_parameter").replace("%s", "03");
        String material03LotNo = mstDictionaryService.getDictionaryValue(langId, "material_lot_no_with_parameter").replace("%s", "03");
        String material03Amount = mstDictionaryService.getDictionaryValue(langId, "material_amount_with_parameter").replace("%s", "03");
        String material03PurgedAmount = mstDictionaryService.getDictionaryValue(langId, "material_purged_amount_with_parameter").replace("%s", "03");
        String suspendedTimeMinutes = mstDictionaryService.getDictionaryValue(langId, "suspended_time_minutes"); //中断時間
        String netProducintTimeMinutes = mstDictionaryService.getDictionaryValue(langId, "net_producint_time_minutes"); //実稼働時間
        
        FileReponse fr = new FileReponse();

        /*
         * Header設定
         */
        ArrayList csvOutHeadList = new ArrayList();
        csvOutHeadList.add(productionDate);
        csvOutHeadList.add(productionStartTime);
        csvOutHeadList.add(productionEndTime);
        csvOutHeadList.add(productionUserId);
        csvOutHeadList.add(productionUserName);
//        csvOutHeadList.add(workUserDepartment);
        csvOutHeadList.add(prodDepartment);
        csvOutHeadList.add(componenCode);
        csvOutHeadList.add(componentName);
        csvOutHeadList.add(procedureCode);
        csvOutHeadList.add(procedureName);
        csvOutHeadList.add(lotNumber);
        csvOutHeadList.add(prevLotNumber);
        // directionCode KM-1048
        csvOutHeadList.add(directionCode);
        csvOutHeadList.add(shotCount);
        csvOutHeadList.add(countPerShot);
        csvOutHeadList.add(defectCount);
        csvOutHeadList.add(completeCount);
        csvOutHeadList.add(moldId);
        csvOutHeadList.add(moldName);
        csvOutHeadList.add(moldType);
        csvOutHeadList.add(machineId);
        csvOutHeadList.add(machineName);
        csvOutHeadList.add(material01Name);
        csvOutHeadList.add(material01LotNo);
        csvOutHeadList.add(material01Amount);
        csvOutHeadList.add(material01PurgedAmount);
        csvOutHeadList.add(material02Name);
        csvOutHeadList.add(material02LotNo);
        csvOutHeadList.add(material02Amount);
        csvOutHeadList.add(material02PurgedAmount);
        csvOutHeadList.add(material03Name);
        csvOutHeadList.add(material03LotNo);
        csvOutHeadList.add(material03Amount);
        csvOutHeadList.add(material03PurgedAmount);
        csvOutHeadList.add(netProducintTimeMinutes);
        csvOutHeadList.add(suspendedTimeMinutes);
        
        // 出力データ準備
        ArrayList<ArrayList> gLineList = new ArrayList<>();
        gLineList.add(csvOutHeadList);
        
        // CSV明細用
        ArrayList<ArrayList> contents = new ArrayList<ArrayList>();
        // 行用
        ArrayList<String> line;
        
        // 日付変換用
        SimpleDateFormat sdfDate = new SimpleDateFormat(DateFormat.DATE_FORMAT);
        SimpleDateFormat sdfHm = new SimpleDateFormat("HH:mm");
        
        List<TblProductionDetail> tblProductionDetails = tblProductionDetailList.getTblProductionDetails();
        if (tblProductionDetails != null && !tblProductionDetails.isEmpty()) {
            for (TblProductionDetail tblProductionDetail : tblProductionDetails) {
                line = new ArrayList<String>();
                // 生産日
                if (tblProductionDetail.getProductionId().getProductionDate() != null) {
                    String outputProductionDate = sdfDate.format(tblProductionDetail.getProductionId().getProductionDate());
                    line.add(outputProductionDate);
                } else {
                    line.add("");
                }
                // 開始時刻
                if (tblProductionDetail.getProductionId().getStartDatetime() != null) {
                    String outputStartDatetime = sdfHm.format(tblProductionDetail.getProductionId().getStartDatetime()); 
                    line.add(outputStartDatetime);
                } else {
                    line.add("");
                }
                // 終了時刻
                if (tblProductionDetail.getProductionId().getEndDatetime() != null) {
                    String outputEndDatetime = sdfHm.format(tblProductionDetail.getProductionId().getEndDatetime()); 
                    line.add(outputEndDatetime);
                } else {
                    line.add("");
                }
                // 生産者ID
                line.add(tblProductionDetail.getProductionId().getUserId());
                // 生産者氏名
                line.add(tblProductionDetail.getProductionId().getUserName());
                // 生産場所
                line.add(tblProductionDetail.getProductionId().getDepartmentName());
                // 部品コード
                line.add(tblProductionDetail.getComponentCode());
                // 部品名称
                line.add(tblProductionDetail.getComponentName());
                // 工番
                line.add(tblProductionDetail.getProcedureCode());
                // 工程名称
                line.add(tblProductionDetail.getProcedureName());
                // ロット番号
                line.add(tblProductionDetail.getProductionId().getLotNumber());
                // 前ロット番号
                line.add(tblProductionDetail.getProductionId().getPrevLotNumber());             
                // directionCode KM-1048
                line.add(tblProductionDetail.getProductionId().getDirectionCode());
                // ショット数
                line.add(Integer.toString(tblProductionDetail.getProductionId().getShotCount()));
                // 取り数
                line.add(Integer.toString(tblProductionDetail.getCountPerShot()));
                // 不良数
                line.add(Integer.toString(tblProductionDetail.getDefectCount()));
                // 完成数
                line.add(Integer.toString(tblProductionDetail.getCompleteCount()));
                // 金型ID
                line.add(tblProductionDetail.getProductionId().getMoldId());
                // 金型名称
                line.add(tblProductionDetail.getProductionId().getMoldName());
                // 金型種別
                if (tblProductionDetail.getProductionId().getMoldType() != null) {
                    line.add(Integer.toString(tblProductionDetail.getProductionId().getMoldType()));
                } else {
                    line.add("");
                }
                // 設備ID
                if (tblProductionDetail.getProductionId().getMstMachine() != null) {
                    line.add(tblProductionDetail.getProductionId().getMstMachine().getMachineId());
                }
                else {
                    line.add("");
                }
                // 設備名称
                if (tblProductionDetail.getProductionId().getMstMachine() != null) {
                    line.add(tblProductionDetail.getProductionId().getMstMachine().getMachineName());
                }
                else {
                    line.add("");
                }
                // 材料01名称
                line.add(tblProductionDetail.getMaterial01Name());
                // 材料01ロット番号
                line.add(tblProductionDetail.getMaterial01LotNo());
                // 材料01使用量
                if (tblProductionDetail.getMaterial01Amount() != null) {
                    line.add(tblProductionDetail.getMaterial01Amount().toString());
                } else {
                    line.add("");
                }
                // 材料01パージ量
                if (tblProductionDetail.getMaterial01PurgedAmount() != null) {
                    line.add(tblProductionDetail.getMaterial01PurgedAmount().toString());
                } else {
                    line.add("");
                }
                // 材料02名称
                line.add(tblProductionDetail.getMaterial02Name());
                // 材料02ロット番号
                line.add(tblProductionDetail.getMaterial02LotNo());
                // 材料02使用量
                if (tblProductionDetail.getMaterial02Amount() != null) {
                    line.add(tblProductionDetail.getMaterial02Amount().toString());
                } else {
                    line.add("");
                }
                // 材料02パージ量
                if (tblProductionDetail.getMaterial02PurgedAmount() != null) {
                    line.add(tblProductionDetail.getMaterial02PurgedAmount().toString());
                } else {
                    line.add("");
                }
                // 材料03名称
                line.add(tblProductionDetail.getMaterial03Name());
                // 材料03ロット番号
                line.add(tblProductionDetail.getMaterial03LotNo());
                // 材料03使用量
                if (tblProductionDetail.getMaterial03Amount() != null) {
                    line.add(tblProductionDetail.getMaterial03Amount().toString());
                } else {
                    line.add("");
                }
                // 材料03パージ量
                if (tblProductionDetail.getMaterial03PurgedAmount() != null) {
                    line.add(tblProductionDetail.getMaterial03PurgedAmount().toString());
                } else {
                    line.add("");
                }
                
                // 実稼働時間
                if (tblProductionDetail.getProductionId().getNetProducintTimeMinutes() != null) {
                    line.add(tblProductionDetail.getProductionId().getNetProducintTimeMinutes().toString());
                } else {
                    line.add("");
                }
                
                // 中断時間
                if (tblProductionDetail.getProductionId().getSuspendedTimeMinutes() != null) {
                    line.add(tblProductionDetail.getProductionId().getSuspendedTimeMinutes().toString());
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

        //CSVファイル出力
        String uuid = IDGenerator.generate();
        String outCsvPath = FileUtil.outCsvFile(kartePropertyService, uuid);
        CSVFileUtil.writeCsv(outCsvPath, gLineList);

        TblCsvExport tblCsvExport = new TblCsvExport();
        tblCsvExport.setFileUuid(uuid);
        //tblCsvExport.setExportTable(CommonConstants.TBL_MST_MOLD);
        tblCsvExport.setExportTable(CommonConstants.TBL_PRODUCTION);
        tblCsvExport.setExportDate(new java.util.Date());
        MstFunction mstFunction = new MstFunction();
        //mstFunction.setId(CommonConstants.FUN_ID_MOLD);
        mstFunction.setId(CommonConstants.FUN_ID_PRODUCTION_LIST);
        tblCsvExport.setFunctionId(mstFunction);
        tblCsvExport.setExportUserUuid(loginUser.getUserUuid());
        String fileName = mstDictionaryService.getDictionaryValue(langId, "production_list");
        tblCsvExport.setClientFileName(FileUtil.getCsvFileName(fileName));
        tblCsvExportService.createTblCsvExport(tblCsvExport);
        //csvファイルのUUIDをリターンする
        fr.setFileUuid(uuid);
        return fr;
    }
    
    /**
     * 生産開始入力登録データ設定
     * @param response
     * @param tblProduction
     * @param tblProductionDetailVo
     * @param prevLotNumber
     * @param loginUser
     * @return 
     */
    public TblProductionDetail setCreateDataForPoductionStart(
            BasicResponse response
           ,TblProduction tblProduction
           ,TblProductionDetailVo tblProductionDetailVo
           ,String prevLotNumber
           ,LoginUser loginUser
    ) {
        TblProductionDetail tblProductionDetail = new TblProductionDetail();
        
        /**
         * 登録更新時の共通項目設定
         */
        setCommonData(response, tblProduction, tblProductionDetail, tblProductionDetailVo, loginUser);
        if (response.isError()) {
            return tblProductionDetail;
        }

        // 前ロット残高ID
        // 前ロット番号が指定されている場合は生産実績ロット残高テーブルのIDを設定する
        TblProductionLotBalance prevProductionLotBalance = tblProductionLotBalanceService.getByLotNumber(prevLotNumber, tblProductionDetail.getComponentId());
        if (prevProductionLotBalance != null) {
            tblProductionDetail.setPrevLotBalanceId(prevProductionLotBalance.getId());
        }

        /*
         * その他登録必須項目変換および設定
         */
        // 取り数
        tblProductionDetail.setCountPerShot(tblProductionDetailVo.getCountPerShot());
        // 不良数
        tblProductionDetail.setDefectCount(0);
        // 完成数
        tblProductionDetail.setCompleteCount(0);
        // 計画充当数
        tblProductionDetail.setPlanAppropriatedCount(0);
        // 計画未充当数 生産実績．完成数と同じ値を設定
        tblProductionDetail.setPlanNotAppropriatedCount(tblProductionDetailVo.getCompleteCount());
        
        // 材料01～材料03の項目
        setMaterials(tblProductionDetail, tblProductionDetailVo);
        
        // 制御フラグを設定
        tblProductionDetail.setAdded(true);     // 登録
        tblProductionDetail.setModified(false);
        tblProductionDetail.setDeleted(false);
        
        return tblProductionDetail;
    }
    
    /**
     * 生産終了入力登録データ設定
     * @param response
     * @param tblProduction
     * @param tblProductionVo
     * @param tblProductionDetailVo
     * @param tblMachineDailyReportDetailList
     * @param prevLotNumber
     * @param loginUser
     * @return 
     */
    public TblProductionDetail setCreateDataPoductionEnd(
            BasicResponse response
           ,TblProduction tblProduction
           ,TblProductionVo tblProductionVo
           ,TblProductionDetailVo tblProductionDetailVo
           ,TblMachineDailyReportDetailList tblMachineDailyReportDetailList
           ,String prevLotNumber
           ,LoginUser loginUser
    ) {
        TblProductionDetail tblProductionDetail = new TblProductionDetail();

        /**
         * 登録更新時の共通項目設定
         */
        setCommonData(response, tblProduction, tblProductionDetail, tblProductionDetailVo, loginUser);
        if (response.isError()) {
            return tblProductionDetail;
        }
        
        // 前ロット残高ID
        // 前ロット番号が指定されている場合は生産実績ロット残高テーブルのIDを設定する
        TblProductionLotBalance prevProductionLotBalance = tblProductionLotBalanceService.getByLotNumber(prevLotNumber, tblProductionDetail.getComponentId());
        if (prevProductionLotBalance != null) {
            tblProductionDetail.setPrevLotBalanceId(prevProductionLotBalance.getId());
        }

        /*
         * その他登録必須項目変換および設定
         */
        // 機械日報から生産終了時、取り数、不良数、完成数は機械日報から加算する
        if (tblProductionVo.getProductionEndFlg() && tblMachineDailyReportDetailList != null) {
            int countPerShot = 0, defectCount = 0, completeCount = 0;
            if (tblMachineDailyReportDetailList.getTblMachineDailyReportDetails() != null) {
                for (TblMachineDailyReportDetail detail : tblMachineDailyReportDetailList.getTblMachineDailyReportDetails()) {
                    if (tblProductionDetailVo.getComponentId() != null && tblProductionDetailVo.getComponentId().equals(detail.getComponentId())) {
                        countPerShot += detail.getCountPerShot();
                        defectCount += detail.getDefectCount();
                        completeCount += detail.getCompleteCount();
                    }
                }
            }
            // 取り数
            tblProductionDetail.setCountPerShot(countPerShot);
            // 不良数
            tblProductionDetail.setDefectCount(defectCount);
            // 完成数
            tblProductionDetail.setCompleteCount(completeCount);
            // 計画未充当数 生産実績．完成数と同じ値を設定
            tblProductionDetail.setPlanNotAppropriatedCount(completeCount);
        } else {
            /**
             * 取り数・不良数・完成数を画面パラメータから設定
             */
            // 取り数
            tblProductionDetail.setCountPerShot(tblProductionDetailVo.getCountPerShot());
            // 不良数
            tblProductionDetail.setDefectCount(tblProductionDetailVo.getDefectCount());
            // 完成数
            tblProductionDetail.setCompleteCount(tblProductionDetailVo.getCompleteCount());
            // 計画未充当数 生産実績．完成数と同じ値を設定
            tblProductionDetail.setPlanNotAppropriatedCount(tblProductionDetailVo.getCompleteCount());
        }
        // 計画充当数
        tblProductionDetail.setPlanAppropriatedCount(0);
        
        // 材料01～材料03の項目
        setMaterials(tblProductionDetail, tblProductionDetailVo);

        // 制御フラグを設定
        tblProductionDetail.setAdded(true);     // 登録
        tblProductionDetail.setModified(false);
        tblProductionDetail.setDeleted(false);
        
        return tblProductionDetail;
    }
    
    /**
     * 生産終了入力更新データ設定
     * @param response
     * @param tblProduction
     * @param tblProductionVo
     * @param tblProductionDetail
     * @param tblProductionDetailVo
     * @param tblMachineDailyReportDetailList
     * @param loginUser
     * @return 
     */
    public TblProductionDetail setUpdateDataForPoductionEnd(
            BasicResponse response
           ,TblProduction tblProduction
           ,TblProductionVo tblProductionVo
           ,TblProductionDetail tblProductionDetail
           ,TblProductionDetailVo tblProductionDetailVo
           ,TblMachineDailyReportDetailList tblMachineDailyReportDetailList
           ,LoginUser loginUser
    ) { 
        /**
         * 登録更新時の共通項目設定
         */
        setCommonData(response, tblProduction, tblProductionDetail, tblProductionDetailVo, loginUser);
        if (response.isError()) {
            return tblProductionDetail;
        }
        
        // 機械日報から生産終了時、取り数、不良数、完成数は機械日報から加算する
        if (tblProductionVo.getProductionEndFlg() && tblMachineDailyReportDetailList != null) {
            int countPerShot = 0, defectCount = 0, completeCount = 0;
            if (tblMachineDailyReportDetailList.getTblMachineDailyReportDetails() != null) {
                for (TblMachineDailyReportDetail detail : tblMachineDailyReportDetailList.getTblMachineDailyReportDetails()) {
                    if (tblProductionDetailVo.getComponentId().equals(detail.getComponentId())) {
                        countPerShot += detail.getCountPerShot();
                        defectCount += detail.getDefectCount();
                        completeCount += detail.getCompleteCount();
                    }
                }
            }
            // 取り数
            tblProductionDetail.setCountPerShot(countPerShot);
            // 不良数
            tblProductionDetail.setDefectCount(defectCount);
            // 完成数
            tblProductionDetail.setCompleteCount(completeCount);
        } else {
            /**
             * 取り数・不良数・完成数を画面パラメータから設定
             */
            // 取り数
            tblProductionDetail.setCountPerShot(tblProductionDetailVo.getCountPerShot());
            // 不良数
            tblProductionDetail.setDefectCount(tblProductionDetailVo.getDefectCount());
            // 更新前完成数
            tblProductionDetail.setCompleteCountBeforeUpd(tblProductionDetail.getCompleteCount());
            // 完成数
            tblProductionDetail.setCompleteCount(tblProductionDetailVo.getCompleteCount());
        }
// 残高テーブルの更新・登録制御時に残高を判定して再設定させる
//        // 計画充当数
//        tblProductionDetail.setPlanAppropriatedCount(0);
//        // 計画未充当数 生産実績．完成数と同じ値を設定
//        tblProductionDetail.setPlanNotAppropriatedCount(tblProductionDetailVo.getCompleteCount());
        
        /*
         * その他登録必須項目変換および設定
         */
        // 材料01～材料03の項目
        setMaterials(tblProductionDetail, tblProductionDetailVo);
        
        tblProductionDetail.setTblComponentLotRelationVoList(tblProductionDetailVo.getTblComponentLotRelationVoList());
        
        // 制御フラグを設定
        tblProductionDetail.setAdded(false);     
        tblProductionDetail.setModified(true); // 更新
        tblProductionDetail.setDeleted(false);
        
        return tblProductionDetail;
    }
    
    /**
     * 登録更新時の共通項目を設定
     * @param response
     * @param tblProduction
     * @param tblProductionDetail
     * @param tblProductionDetailVo
     * @param loginUser
     * @return 
     */
    public TblProductionDetail setCommonData(
        BasicResponse response
       ,TblProduction tblProduction
       ,TblProductionDetail tblProductionDetail
       ,TblProductionDetailVo tblProductionDetailVo
       ,LoginUser loginUser
    ) {
        /**
         * 必須チェックおよび値設定
         */
        // 部品ID componentCode ⇒ componentId
        if (tblProductionDetailVo.getComponentCode() == null || "".equals(tblProductionDetailVo.getComponentCode())) {
            setApplicationError(response, loginUser, "msg_error_not_null", "componentCode");
            return tblProductionDetail;
        } else {
            // 部品マスタ存在チェック(部品マスタ.部品コードによる存在チェック)および値セット
            MstComponent mstComponent = mstComponentService.getMstComponent(tblProductionDetailVo.getComponentCode());
            if (mstComponent == null) {
                setApplicationError(response, loginUser, "mst_error_record_not_found", "部品マスタ存在チェックエラー");
                return tblProductionDetail;
            } else {
                tblProductionDetail.setComponentId(mstComponent.getId());
                tblProductionDetail.setComponentCode(tblProductionDetailVo.getComponentCode());
            }
        }

        // 部品IDチェック
        if (tblProductionDetailVo.getComponentCode() == null || "".equals(tblProductionDetailVo.getComponentCode())) {
            setApplicationError(response, loginUser, "msg_error_not_null", "procedureCode");
            return tblProductionDetail;
        // 工番ID procedureCode ⇒ procedureId
        } else {
            // 取数が0の場合は工番関連の確認は行わない
            if(tblProductionDetailVo.getProcedureCode() == null || "".equals(tblProductionDetailVo.getProcedureCode())) {
                //setApplicationError(response, loginUser, "msg_error_not_null", "componentCode");
                if (tblProductionDetailVo.getCountPerShot() > 0) {
                    response.setError(true);
                    response.setErrorCode(ErrorMessages.E201_APPLICATION);
                    String errorMessage = String.format(
                        mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_not_null_with_item"),                    
                        mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "procedure_code"));
                    response.setErrorMessage(errorMessage);
                    return tblProductionDetail;
                }
            } else {
                MstProcedure mstProcedure = mstProcedureService.getMstProcedureByComponentIdAndProcedureCode(tblProductionDetail.getComponentId(), tblProductionDetailVo.getProcedureCode());
                if (mstProcedure == null) {
                    setApplicationError(response, loginUser, "mst_error_record_not_found", "工程マスタ（部品ごとの製造手順）存在チェックエラー");
                    return tblProductionDetail;
                } else {
                    tblProductionDetail.setProcedureId(mstProcedure.getId());
                    tblProductionDetail.setProcedureCode(tblProductionDetailVo.getProcedureCode());
                }
            }
        }
        return tblProductionDetail;
    }

    /**
     * 登録更新時の材料の項目をを設定
     * @param tblProductionDetail
     * @param tblProductionDetailVo
     * @return 
     */
    public TblProductionDetail setMaterials(TblProductionDetail tblProductionDetail, TblProductionDetailVo tblProductionDetailVo) {
        // 材料01
        tblProductionDetail.setMaterial01Code(tblProductionDetailVo.getMaterial01Code()); // 材料01_コード
        tblProductionDetail.setMaterial01Id(tblProductionDetailVo.getMaterial01Id()); // 材料01_ID
        tblProductionDetail.setMaterial01LotNo(tblProductionDetailVo.getMaterial01LotNo()); // 材料01_ロット番号
        tblProductionDetail.setMaterial01Amount(tblProductionDetailVo.getMaterial01Amount()); // 材料01_使用量
        tblProductionDetail.setMaterial01PurgedAmount(tblProductionDetailVo.getMaterial01PurgedAmount()); // 材料01_パージ量
        // 材料コードをIDに変換
        if (tblProductionDetail.getMaterial01Code() != null && !"".equals(tblProductionDetail.getMaterial01Code())) {
            MstMaterial mstMaterial01 = mstMaterialService.getMstMaterialByCode(tblProductionDetail.getMaterial01Code());
            if (mstMaterial01 != null) {
                tblProductionDetail.setMaterial01Id(mstMaterial01.getId());
            }
            if (tblProductionDetailVo.getMaterial01Amount() == null) {
                tblProductionDetail.setMaterial01Amount(new BigDecimal(BigInteger.ZERO));
            }
            if (tblProductionDetailVo.getMaterial01PurgedAmount() == null) {
                tblProductionDetail.setMaterial01PurgedAmount(new BigDecimal(BigInteger.ZERO));
            }
        }
        
        // 材料02
        tblProductionDetail.setMaterial02Code(tblProductionDetailVo.getMaterial02Code()); // 材料02_コード
        tblProductionDetail.setMaterial02Id(tblProductionDetailVo.getMaterial02Id()); // 材料02_ID
        tblProductionDetail.setMaterial02LotNo(tblProductionDetailVo.getMaterial02LotNo()); // 材料02_ロット番号
        tblProductionDetail.setMaterial02Amount(tblProductionDetailVo.getMaterial02Amount()); // 材料02_使用量
        tblProductionDetail.setMaterial02PurgedAmount(tblProductionDetailVo.getMaterial02PurgedAmount()); // 材料02_パージ量
        // 材料コードをIDに変換
        if (tblProductionDetail.getMaterial02Code() != null && !"".equals(tblProductionDetail.getMaterial02Code())) {
            MstMaterial mstMaterial02 = mstMaterialService.getMstMaterialByCode(tblProductionDetail.getMaterial02Code());
            if (mstMaterial02 != null) {
                tblProductionDetail.setMaterial02Id(mstMaterial02.getId());
            }
            if (tblProductionDetailVo.getMaterial02Amount() == null) {
                tblProductionDetail.setMaterial02Amount(new BigDecimal(BigInteger.ZERO));
            }
            if (tblProductionDetailVo.getMaterial02PurgedAmount() == null) {
                tblProductionDetail.setMaterial02PurgedAmount(new BigDecimal(BigInteger.ZERO));
            }
        }
        
        // 材料03
        tblProductionDetail.setMaterial03Code(tblProductionDetailVo.getMaterial03Code()); // 材料03_コード
        tblProductionDetail.setMaterial03Id(tblProductionDetailVo.getMaterial03Id()); // 材料03_ID
        tblProductionDetail.setMaterial03LotNo(tblProductionDetailVo.getMaterial03LotNo()); // 材料03_ロット番号
        tblProductionDetail.setMaterial03Amount(tblProductionDetailVo.getMaterial03Amount()); // 材料03_使用量
        tblProductionDetail.setMaterial03PurgedAmount(tblProductionDetailVo.getMaterial03PurgedAmount()); // 材料03_パージ量
        // 材料コードをIDに変換
        if (tblProductionDetail.getMaterial03Code() != null && !"".equals(tblProductionDetail.getMaterial03Code())) {
            MstMaterial mstMaterial03 = mstMaterialService.getMstMaterialByCode(tblProductionDetail.getMaterial03Code());
            if (mstMaterial03 != null) {
                tblProductionDetail.setMaterial03Id(mstMaterial03.getId());
            }
            if (tblProductionDetailVo.getMaterial03Amount() == null) {
                tblProductionDetail.setMaterial03Amount(new BigDecimal(BigInteger.ZERO));
            }
            if (tblProductionDetailVo.getMaterial03PurgedAmount() == null) {
                tblProductionDetail.setMaterial03PurgedAmount(new BigDecimal(BigInteger.ZERO));
            }
        }
        return tblProductionDetail;
    }
    
    /**
     * 生産実績明細テーブル登録
     * @param tblProductionDetail
     * @param loginUser
     * @return 
     */
    @Transactional
    public TblProductionDetail createTblProductionDetail(TblProductionDetail tblProductionDetail, LoginUser loginUser) {
        // 登録処理時の強制設定パラメータセット
        tblProductionDetail.setId(IDGenerator.generate());
        tblProductionDetail.setCreateDate(new java.util.Date());
        tblProductionDetail.setCreateUserUuid(loginUser.getUserUuid());
        tblProductionDetail.setUpdateDate(tblProductionDetail.getCreateDate());
        tblProductionDetail.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.persist(tblProductionDetail);
        return tblProductionDetail;
    }
    
    /**
     * 生産実績明細テーブル更新
     * @param tblProductionDetail
     * @param loginUser
     * @return 
     */
    @Transactional
    public TblProductionDetail updateTblProductionDetail(TblProductionDetail tblProductionDetail, LoginUser loginUser) {
        tblProductionDetail.setUpdateDate(new java.util.Date());
        tblProductionDetail.setUpdateUserUuid(loginUser.getUserUuid());
        entityManager.merge(tblProductionDetail);
        return tblProductionDetail;
    }
    
    /**
     * 生産実績明細テーブル削除
     * @param tblProductionDetail
     * @param loginUser
     * @return 
     */
    @Transactional
    public void deleteTblProductionDetail(TblProductionDetail tblProductionDetail, LoginUser loginUser) {
        final String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.log(Level.FINE, "  ---> [[{0}]] Start", methodName);
        
        // 前ロット残高IDがあれば完成数等を調整
        if ( tblProductionDetail.getPrevLotBalanceId() != null){
            int completeCount = tblProductionDetail.getCompleteCount();
            TblProductionLotBalance balance = tblProductionLotBalanceService.getProductionBalanceSingleById(tblProductionDetail.getPrevLotBalanceId());
            balance.setNextCompleteCount(balance.getNextCompleteCount() - completeCount);
            balance.setBalance(balance.getBalance() + completeCount);
           tblProductionLotBalanceService.updateTblProductionLotBalance(balance, loginUser);
        }
        // 対応するロット残高を削除 
        tblProductionLotBalanceService.deleteTblProductionLotBalanceByProductionDetail(tblProductionDetail, loginUser);
        
        //生産実績明細削除
        try{
            Query query = entityManager.createNamedQuery("TblProductionDetail.delete");
            query.setParameter("id", tblProductionDetail.getId());
            query.executeUpdate();
        }catch(Exception e){
            logger.log(Level.WARNING, e.toString());
            throw e;
        }
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
    
    /**
     * バッチで生産実績明細テーブルデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @param moldUuid
     * @return
     */
    public TblProductionDetailList getExtProductionDetailsByBatch(String latestExecutedDate, String moldUuid) {
        TblProductionDetailList resList = new TblProductionDetailList();
        List<TblProductionDetailVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct pd FROM TblProductionDetail pd JOIN MstApiUser u on u.companyId = pd.tblProduction.mstMold.ownerCompanyId where 1 = 1 ");
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            sql.append(" and pd.tblProduction.mstMold.uuid = :moldUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (pd.updateDate > :latestExecutedDate or pd.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != moldUuid && !moldUuid.trim().equals("")) {
            query.setParameter("moldUuid", moldUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblProductionDetail> tmpList = query.getResultList();
        for (TblProductionDetail productionDetail : tmpList) {
            TblProductionDetailVo aRes = new TblProductionDetailVo();
            MstComponent mstComponent = null;
            if (null != (mstComponent = productionDetail.getMstComponent())) {
                aRes.setComponentCode(mstComponent.getComponentCode());
            }
            productionDetail.setMstComponent(null);
            productionDetail.setMstMaterial01(null);
            productionDetail.setMstMaterial02(null);
            productionDetail.setMstMaterial03(null);
            productionDetail.setMstProcedure(null);
            TblProduction tblProduction = null;
            if (null != (tblProduction = productionDetail.getTblProduction())) {
                aRes.setProductionId(tblProduction.getId());
            }
            productionDetail.setTblProduction(null);
            productionDetail.setProductionId(null);//TODO same as TblProduction?
            productionDetail.setTblProductionLotBalanceCollection(null);
                      
            aRes.setTblProductionDetail(productionDetail);
            resVo.add(aRes);
        }
        resList.setTblProductionDetailVos(resVo);
        return resList;
    }
    
    /**
     * バッチで生産実績明細テーブルデータを取得
     *
     * @param latestExecutedDate　最新取得日時
     * @param machineUuid
     * @return
     */
    public TblProductionDetailList getExtMachineProductionDetailsByBatch(String latestExecutedDate, String machineUuid) {
        TblProductionDetailList resList = new TblProductionDetailList();
        List<TblProductionDetailVo> resVo = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT distinct pd FROM TblProductionDetail pd JOIN MstApiUser u on u.companyId = pd.tblProduction.mstMachine.ownerCompanyId where 1 = 1 ");
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            sql.append(" and pd.tblProduction.mstMachine.uuid = :machineUuid ");
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            sql.append(" and (pd.updateDate > :latestExecutedDate or pd.updateDate is null) ");
        }
        Query query = entityManager.createQuery(sql.toString());
        if (null != machineUuid && !machineUuid.trim().equals("")) {
            query.setParameter("moldUuid", machineUuid);
        }
        if (null != latestExecutedDate && !latestExecutedDate.trim().equals("")) {
            latestExecutedDate = latestExecutedDate.replace("-", " ");
            query.setParameter("latestExecutedDate", new FileUtil().getDateTimeParseForDate(latestExecutedDate));
        }
        List<TblProductionDetail> tmpList = query.getResultList();
        for (TblProductionDetail productionDetail : tmpList) {
            TblProductionDetailVo aRes = new TblProductionDetailVo();
            MstComponent mstComponent = null;
            if (null != (mstComponent = productionDetail.getMstComponent())) {
                aRes.setComponentCode(mstComponent.getComponentCode());
            }
            productionDetail.setMstComponent(null);
            productionDetail.setMstMaterial01(null);
            productionDetail.setMstMaterial02(null);
            productionDetail.setMstMaterial03(null);
            productionDetail.setMstProcedure(null);
            TblProduction tblProduction = null;
            if (null != (tblProduction = productionDetail.getTblProduction())) {
                aRes.setProductionId(tblProduction.getId());
            }
            productionDetail.setTblProduction(null);
            productionDetail.setProductionId(null);//TODO same as TblProduction?
            productionDetail.setTblProductionLotBalanceCollection(null);
                      
            aRes.setTblProductionDetail(productionDetail);
            resVo.add(aRes);
        }
        resList.setTblProductionDetailVos(resVo);
        return resList;
    }
    
    /**
     * バッチで生産実績明細テーブルデータを更新
     *
     * @param productionDetailVos
     * @param mstComponentCompanyVoList
     * @return
     */
    @Transactional
    public BasicResponse updateExtProductionDetailsByBatch(List<TblProductionDetailVo> productionDetailVos, MstComponentCompanyVoList mstComponentCompanyVoList) {
        BasicResponse response = new BasicResponse();

        if (productionDetailVos != null && !productionDetailVos.isEmpty()) {

            for (TblProductionDetailVo productionDetailVo : productionDetailVos) {
                // 生産実績明細更新する際に、生産実績を存在チェックする
                TblProduction tblProduction = entityManager.find(TblProduction.class, productionDetailVo.getProductionId());
                if (null == tblProduction) {
                    //該当生産実績が存在していない場合、更新しません。
                    continue;
                }
                
                List<TblProductionDetail> oldProductionDetails = entityManager.createQuery("SELECT t FROM TblProductionDetail t WHERE t.id=:id ")
                        .setParameter("id", productionDetailVo.getTblProductionDetail().getId())
                        .setMaxResults(1)
                        .getResultList();
                TblProductionDetail newProductionDetail;
                if (null != oldProductionDetails && !oldProductionDetails.isEmpty()) {
                    newProductionDetail = oldProductionDetails.get(0);
                } else {
                    newProductionDetail = new TblProductionDetail();
                    //生産実績明細IDセット
                    newProductionDetail.setId(productionDetailVo.getTblProductionDetail().getId());
                    //生産実績をセット
                    newProductionDetail.setProductionId(tblProduction);
                   
                }
                
                //自社のComponentIDに変換
                Query query = entityManager.createNamedQuery("MstComponent.findByComponentCode");
                query.setParameter("componentCode", productionDetailVo.getComponentCode());
                List<MstComponent> mstComponentList = query.getResultList();
                
                if (null != mstComponentList && !mstComponentList.isEmpty()) {
                    newProductionDetail.setComponentId(mstComponentList.get(0).getId());
                } else if (mstComponentCompanyVoList.getMstComponentCompanyVos().size() > 0) {
                    for (MstComponentCompanyVo mstComponentCompanyVo : mstComponentCompanyVoList.getMstComponentCompanyVos()) {
                        if (StringUtils.isNotEmpty(productionDetailVo.getComponentCode()) && StringUtils.isNotEmpty(mstComponentCompanyVo.getOtherComponentCode())) {
                            if (productionDetailVo.getComponentCode().equals(mstComponentCompanyVo.getComponentCode())) {
                                // 先方部品により自社の部品IDを置換
                                query.setParameter("componentCode", mstComponentCompanyVo.getOtherComponentCode());
                                List<MstComponent> otherMstComponentList = query.getResultList();
                                if (null != otherMstComponentList && !otherMstComponentList.isEmpty()) {
                                    newProductionDetail.setComponentId(otherMstComponentList.get(0).getId());
                                    break;
                                }
                            }
                        }
                    }
                }

                String procedureId = productionDetailVo.getTblProductionDetail().getProcedureId();
                if (StringUtils.isNotEmpty(procedureId)) {
                    newProductionDetail.setProcedureId(procedureId);
                } else {
                    newProductionDetail.setProcedureId(null);
                }
                newProductionDetail.setCountPerShot(productionDetailVo.getTblProductionDetail().getCountPerShot());
                newProductionDetail.setDefectCount(productionDetailVo.getTblProductionDetail().getDefectCount());
                newProductionDetail.setCompleteCount(productionDetailVo.getTblProductionDetail().getCompleteCount());
                newProductionDetail.setMaterial01Id(productionDetailVo.getTblProductionDetail().getMaterial01Id());
                newProductionDetail.setMaterial01LotNo(productionDetailVo.getTblProductionDetail().getMaterial01LotNo());
                newProductionDetail.setMaterial01Amount(productionDetailVo.getTblProductionDetail().getMaterial01Amount());
                newProductionDetail.setMaterial01PurgedAmount(productionDetailVo.getTblProductionDetail().getMaterial01PurgedAmount());
                newProductionDetail.setMaterial02Id(productionDetailVo.getTblProductionDetail().getMaterial02Id());
                newProductionDetail.setMaterial02LotNo(productionDetailVo.getTblProductionDetail().getMaterial02LotNo());
                newProductionDetail.setMaterial02Amount(productionDetailVo.getTblProductionDetail().getMaterial02Amount());
                newProductionDetail.setMaterial02PurgedAmount(productionDetailVo.getTblProductionDetail().getMaterial02PurgedAmount());
                newProductionDetail.setMaterial03Id(productionDetailVo.getTblProductionDetail().getMaterial03Id());
                newProductionDetail.setMaterial03LotNo(productionDetailVo.getTblProductionDetail().getMaterial03LotNo());
                newProductionDetail.setMaterial03Amount(productionDetailVo.getTblProductionDetail().getMaterial03Amount());
                newProductionDetail.setMaterial03PurgedAmount(productionDetailVo.getTblProductionDetail().getMaterial03PurgedAmount());
                newProductionDetail.setPrevLotBalanceId(productionDetailVo.getTblProductionDetail().getPrevLotBalanceId());
                newProductionDetail.setPlanAppropriatedCount(FileUtil.getIntegerValue(productionDetailVo.getTblProductionDetail().getPlanAppropriatedCount()));
                newProductionDetail.setPlanNotAppropriatedCount(FileUtil.getIntegerValue(productionDetailVo.getTblProductionDetail().getPlanNotAppropriatedCount()));
                newProductionDetail.setCreateDate(productionDetailVo.getTblProductionDetail().getCreateDate());
                newProductionDetail.setCreateUserUuid(productionDetailVo.getTblProductionDetail().getCreateUserUuid());
                newProductionDetail.setUpdateDate(new Date());
                newProductionDetail.setUpdateUserUuid(productionDetailVo.getTblProductionDetail().getUpdateUserUuid());

                if (null != oldProductionDetails && !oldProductionDetails.isEmpty()) {
                    entityManager.merge(newProductionDetail);
                } else {
                    entityManager.persist(newProductionDetail);
                }

            }
        }
        response.setError(false);
        return response;
    }
    
    /**
     * 設備ログ照会用ロット番号により生産実績取得
     * @param lotNumber
     * @return 
     */
    public TblProductionDetailList getProductionByLotNumber(String lotNumber) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT tblProductionDetail FROM TblProductionDetail tblProductionDetail");
        sql.append(" JOIN FETCH tblProductionDetail.tblProduction");
        sql.append(" LEFT JOIN FETCH tblProductionDetail.mstComponent");
        sql.append(" LEFT JOIN FETCH tblProductionDetail.tblProduction.mstMachine");
        sql.append(" WHERE tblProductionDetail.tblProduction.lotNumber = :lotNumber ");
        
        Query query = entityManager.createQuery(sql.toString());
        query.setParameter("lotNumber", lotNumber);
        List list = query.getResultList();
        
        TblProductionDetailList response = new TblProductionDetailList();
        response.setTblProductionDetails(list);
        return response;
    }
    
    /**
     * 生産実績テーブル検索条件指定検索 (実レコード取得用ラッパー)
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
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @param isPage
     * @return 
     */
    public TblProductionDetailList getProductionDetailsByConditionPage(
            String componentCode     // 部品コード
           ,java.util.Date productionDateFrom // 生産日From yyyy-mm-dd
           ,java.util.Date productionDateTo // 生産日To yyyy-mm-dd
           ,String directionCode // 手配番号
           ,String userId // 生産者ID
           ,String userName // 生産者氏名
           ,Integer department // 生産場所
           ,String moldId // 金型ID
           ,Integer moldType // 金型種類
           ,String machineId //設備ID
           ,Integer machineType
           ,java.util.Date workStartDateTime // 選択日付の業務開始時刻
           ,java.util.Date nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
           ,int finalProcedureOnly // 最終工程フラグ
           ,String sidx
           ,String sord
           ,int pageNumber
           ,int pageSize
           ,boolean isPage
    ) {
        
        TblProductionDetailList response = new TblProductionDetailList();
        
        if (isPage) {

            List count = getProductionDetailsByConditionPageSql(componentCode, productionDateFrom, productionDateTo,
                    directionCode, userId, userName, department, moldId, moldType, machineId, machineType, workStartDateTime,
                    nextDayWorkStartDateTime, finalProcedureOnly, sidx, sord, pageNumber, pageSize, true);

            // ページをめぐる
            Pager pager = new Pager();
            response.setPageNumber(pageNumber);
            long counts = (long) count.get(0);
            response.setCount(counts);
            response.setPageTotal(pager.getTotalPage(pageSize, Integer.parseInt("" + counts)));

        }

        List list = getProductionDetailsByConditionPageSql(componentCode, productionDateFrom, productionDateTo,
                directionCode, userId, userName, department, moldId, moldType, machineId, machineType, workStartDateTime,
                nextDayWorkStartDateTime, finalProcedureOnly, sidx, sord, pageNumber, pageSize, false);
        
        response.setTblProductionDetails(list);
        return response;
    }
    
    /**
     * 生産実績テーブル検索条件指定検索
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
     * @param pageNumber
     * @param pageSize
     * @param isCount
     * @return 
     */
    public List getProductionDetailsByConditionPageSql(
            String componentCode     // 部品コード
           ,java.util.Date productionDateFrom // 生産日From yyyy-mm-dd
           ,java.util.Date productionDateTo // 生産日To yyyy-mm-dd
           ,String directionCode // 手配番号
           ,String userId // 生産者ID
           ,String userName // 生産者氏名
           ,Integer department // 生産場所
           ,String moldId // 金型ID
           ,Integer moldType // 金型種類
           ,String machineId //設備ID
           ,Integer machineType
           ,java.util.Date workStartDateTime // 選択日付の業務開始時刻
           ,java.util.Date nextDayWorkStartDateTime // 選択日付の翌日の業務開始時刻
           ,int finalProcedureOnly // 最終工程フラグ
           ,String sidx
           ,String sord
           ,int pageNumber
           ,int pageSize
           ,boolean isCount
    ) {
        /* 
         * JPQLで条件定義
         *   結合条件はエンティティクラスに定義済み
         */
        StringBuilder sql = new StringBuilder();
        if (isCount) {
            sql = sql.append("SELECT count(tblProductionDetail) FROM TblProductionDetail tblProductionDetail");
        } else {
            sql = sql.append("SELECT tblProductionDetail FROM TblProductionDetail tblProductionDetail");
        }
        sql = sql.append(" JOIN FETCH tblProductionDetail.tblProduction tp"
                        + " LEFT JOIN FETCH tblProductionDetail.mstComponent mc"
                        + " LEFT JOIN FETCH tblProductionDetail.mstProcedure mp"
                        + " LEFT JOIN FETCH tblProductionDetail.mstMaterial01 mm1"
                        + " LEFT JOIN FETCH tblProductionDetail.mstMaterial02 mm2"
                        + " LEFT JOIN FETCH tblProductionDetail.mstMaterial03 mm3"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.mstUser mu"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.tblDirection td"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.mstMold mold"
                        + " LEFT JOIN FETCH tblProductionDetail.tblProduction.mstMachine machine"
                        + " WHERE 1=1 "
        );
        // 部品コード 部分一致
        if (componentCode != null && !"".equals(componentCode)) {
            sql.append(" and tblProductionDetail.mstComponent.componentCode like :componentCode ");
        }
        // 生産日From   同日以上
        if (productionDateFrom != null) {
            sql = sql.append(" and tblProductionDetail.tblProduction.productionDate >= :productionDateFrom ");
        }
        // 生産日From   同日以下
        if (productionDateTo != null) {
            sql = sql.append(" and tblProductionDetail.tblProduction.productionDate <= :productionDateTo ");
        }
        // 手配番号 部分一致
        if (directionCode != null && !"".equals(directionCode)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.tblDirection.directionCode like :directionCode ");
        }
        // 生産者ID 部分一致
        if (userId != null && !"".equals(userId)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstUser.userId like :userId ");
        }
        // 生産者氏名 部分一致
        if (userName != null && !"".equals(userName)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstUser.userName like :userName ");
        }
        // 生産場所 完全一致
        if (department != null && department != 0) {
            // sql = sql.append(" and tblProductionDetail.tblProduction.mstUser.department = :department ");
            sql = sql.append(" and tblProductionDetail.tblProduction.prodDepartment = :department ");
        }
        // 金型ID 部分一致
        if (moldId != null && !"".equals(moldId)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstMold.moldId like :moldId ");
        }
        // 金型種類 完全一致
        if (moldType != null && moldType != 0) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstMold.moldType = :moldType ");
        }
       
        // 設備ID＝選択されている設備ID
        if (machineId != null && !"".equals(machineId)) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstMachine.machineId like :machineId ");
        }
        
        if (machineType != null && machineType != 0) {
            sql = sql.append(" and tblProductionDetail.tblProduction.mstMachine.machineType = :machineType ");
        }
        
        if (nextDayWorkStartDateTime != null) {
            // 開始日時<=選択日付の翌日の業務開始時刻
            sql = sql.append(" and (tblProductionDetail.tblProduction.startDatetime <= :startDatetime ");
        }
        
        if (workStartDateTime != null) {
            // 終了日時>=選択日付の業務開始時刻
            sql = sql.append(" and tblProductionDetail.tblProduction.endDatetime >= :endDatetime  ");
        }
        
        if (nextDayWorkStartDateTime != null) {
            // 開始日時<=選択日付の翌日の業務開始時刻且つ終了日時 IS NULL
            sql = sql.append(" or (tblProductionDetail.tblProduction.endDatetime is null and tblProductionDetail.tblProduction.startDatetime <= :startDatetime)) ");
        }
        
        if (finalProcedureOnly == 1) {
            sql.append(" and mp.finalFlg = 1 ");
        }
        
        if (!isCount) {

            if (StringUtils.isNotEmpty(sidx)) {
                
                String sortStr = orderKey.get(sidx) + " " + sord;
                
                sql.append(sortStr);

            } else {

                // 生産日 降順, 開始時刻 降順
                sql = sql.append(
                        " ORDER BY tp.productionDate DESC, tp.startDatetime DESC ");

            }

        }
        
        Query query = entityManager.createQuery(sql.toString());
        
        /*
         * パラメータバインド
         */
        if (componentCode != null && !"".equals(componentCode)) {
            query.setParameter("componentCode", "%" + componentCode+  "%");
        }
        if (productionDateFrom != null) {
            query.setParameter("productionDateFrom", productionDateFrom);
        }
        if (productionDateTo != null) {
            query.setParameter("productionDateTo", productionDateTo);
        }
        if (directionCode != null && !"".equals(directionCode)) {
            query.setParameter("directionCode", "%" + directionCode+  "%");
        }
        if (userId != null && !"".equals(userId)) {
            query.setParameter("userId", "%" + userId+  "%");
        }
        if (userName != null && !"".equals(userName)) {
            query.setParameter("userName", "%" + userName+  "%");
        }
        if (department != null && department != 0) {
            query.setParameter("department", department);
        }
        if (moldId != null && !"".equals(moldId)) {
            query.setParameter("moldId", "%" + moldId+  "%");
        }
        // 金型種類 完全一致
        if (moldType != null && moldType != 0) {
            query.setParameter("moldType", moldType);
        }
        // 設備ID
        if (machineId != null && !"".equals(machineId)) {
            query.setParameter("machineId", "%" + machineId + "%");
        }
        if (machineType != null && machineType != 0) {
            query.setParameter("machineType", machineType);
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
        
        List list = query.getResultList();
        return list;
    }
}
