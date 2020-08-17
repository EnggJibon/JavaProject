/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.productionplanupdate;

import com.kmcj.karte.batch.externalmold.delete.TblDeletedKey;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import com.kmcj.karte.resources.production.plan.TblProductionPlan;
import com.kmcj.karte.resources.production.plan.appropriation.TblProductionPlanAppropriation;
import com.kmcj.karte.resources.production.plan.appropriation.TblProductionPlanDeduction;
import com.kmcj.karte.util.IDGenerator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author f.kitaoji
 */
@Named
@Dependent
public class ProductionPlanUpdateBatchlet extends AbstractBatchlet {

// ジョブ名称を取得するためにJobContextをインジェクション
    @Inject
    JobContext jobContext;

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    private Logger logger = Logger.getLogger(ProductionPlanUpdateBatchlet.class.getName());
    private final static String BATCH_NAME = "ProductionPlanUpdateBatch";
    private final static Level logLevel = Level.FINE;

    @Override
    @Transactional
    public String process() throws Exception {
        logger.log(Level.INFO, "ProductionPlanUpdateBatchlet: Started.");
        
        //初期処理
        Query query, planQuery, planQueryZero;
        //生産計画テーブルのNULLをゼロに
        query = entityManager.createQuery("UPDATE TblProductionPlan t SET t.completedCount = 0 WHERE t.completedCount IS NULL");
        query.executeUpdate();
        query = entityManager.createQuery("UPDATE TblProductionPlan t SET t.uncompletedCount = 0 WHERE t.uncompletedCount IS NULL");
        query.executeUpdate();
        //生産計画テーブルの未完成数量を再計算
        query = entityManager.createQuery("UPDATE TblProductionPlan t SET t.uncompletedCount = t.quantity - t.completedCount");
        query.executeUpdate();
        //生産実績明細テーブルのNULLをゼロに
        query = entityManager.createQuery("UPDATE TblProductionDetail t SET t.planNotAppropriatedCount = 0 WHERE t.planNotAppropriatedCount IS NULL");
        query.executeUpdate();
        query = entityManager.createQuery("UPDATE TblProductionDetail t SET t.planAppropriatedCount = 0 WHERE t.planAppropriatedCount IS NULL");
        query.executeUpdate();
        //生産実績明細の計画未充当数を再計算
        query = entityManager.createQuery("UPDATE TblProductionDetail t SET t.planNotAppropriatedCount = t.completeCount - t.planAppropriatedCount");
        int cnt = query.executeUpdate();
        logger.log(logLevel, "ProductionPlanUpdateBatchlet: TblProductionDetail planNotAppropriatedCount was updated. (" + cnt + " records)");

        //手配番号のある生産実績の処理
        logger.log(logLevel, "ProductionPlanUpdateBatchlet: Apply production with direction ID.");
        //生産実績明細から計画未充当数がゼロでなく、手配IDを保持するものを取得しループ
        query = entityManager.createQuery("SELECT t FROM TblProductionDetail t JOIN FETCH t.tblProduction p WHERE t.planNotAppropriatedCount != 0 AND p.directionId IS NOT NULL");
        //生産計画テーブルから手配IDと部品と工程が等しく、未完成数量がゼロより大きいものを取得するクエリ。プラス充当用
        planQuery = entityManager.createQuery(
            "SELECT t FROM TblProductionPlan t WHERE t.directionId = :directionId AND t.uncompletedCount > 0 AND t.componentId = :componentId AND t.procedureId = :procedureId ORDER BY t.procedureDueDate");
        //生産計画テーブルから手配IDと部品と工程が等しく、未完成数量がゼロのものを取得するクエリ。マイナス充当用
        planQueryZero = entityManager.createQuery(
            "SELECT t FROM TblProductionPlan t WHERE t.directionId = :directionId AND t.uncompletedCount >= 0 AND t.componentId = :componentId AND t.procedureId = :procedureId ORDER BY t.procedureDueDate DESC");
        List<TblProductionDetail> productionDetailsDirection = query.getResultList();
        //生産実績明細カーソルループ
        for (TblProductionDetail productionDetail : productionDetailsDirection) {
            if (productionDetail.getComponentId() != null && productionDetail.getProcedureId() != null) {
                List<TblProductionPlan> productionPlans;
                if (productionDetail.getPlanNotAppropriatedCount() > 0) {
                    //生産実績明細の計画未充当数がゼロより大きいとき：生産計画テーブルから部品と工程が等しく、未完成数量がゼロより大きいものを取得
                    //プラスの充当を実行
                    planQuery.setParameter("directionId", productionDetail.getTblProduction().getDirectionId());
                    planQuery.setParameter("componentId", productionDetail.getComponentId());
                    planQuery.setParameter("procedureId", productionDetail.getProcedureId());
                    productionPlans = planQuery.getResultList();
                }
                else {
                    //生産実績明細の計画未充当数がゼロより小さいとき：生産計画テーブルから部品と工程が等しく、未完成数量がゼロのものを取得
                    //マイナスの充当を実行
                    planQueryZero.setParameter("directionId", productionDetail.getTblProduction().getDirectionId());
                    planQueryZero.setParameter("componentId", productionDetail.getComponentId());
                    planQueryZero.setParameter("procedureId", productionDetail.getProcedureId());
                    productionPlans = planQueryZero.getResultList();
                }
                //生産計画への充当
                for (TblProductionPlan productionPlan : productionPlans) {
                    //充当数の算出
                    int appropriateCnt;
                    if (productionPlan.getUncompletedCount() <= productionDetail.getPlanNotAppropriatedCount()) {
                        appropriateCnt = productionPlan.getUncompletedCount();
                    }
                    else {
                        appropriateCnt = productionDetail.getPlanNotAppropriatedCount();
                    }
                    if (productionPlan.getCompletedCount() + appropriateCnt < 0) {
                        appropriateCnt = productionPlan.getCompletedCount() * (-1);
                    }
                    //生産計画テーブルの完成数量、未完成数量をUPDATE
                    productionPlan.setUncompletedCount(productionPlan.getUncompletedCount() - appropriateCnt);
                    productionPlan.setCompletedCount(productionPlan.getCompletedCount() + appropriateCnt);
                    entityManager.merge(productionPlan);
                    //生産実績明細の計画充当数、計画未充当数をUPDATE
                    productionDetail.setPlanNotAppropriatedCount(productionDetail.getPlanNotAppropriatedCount() - appropriateCnt);
                    productionDetail.setPlanAppropriatedCount(productionDetail.getPlanAppropriatedCount() + appropriateCnt);
                    entityManager.merge(productionDetail);
                    //充当結果を生産実績計画充当テーブルへ保存
                    TblProductionPlanAppropriation planApp = new TblProductionPlanAppropriation();
                    planApp.setId(IDGenerator.generate());
                    planApp.setProductionPlanId(productionPlan.getId());
                    planApp.setProductionDetailId(productionDetail.getId());
                    planApp.setAppropriatedCount(appropriateCnt);
                    planApp.setCreateDate(new java.util.Date());
                    planApp.setCreateUserUuid(BATCH_NAME);
                    planApp.setUpdateDate(new java.util.Date());
                    planApp.setUpdateUserUuid(BATCH_NAME);
                    entityManager.persist(planApp);
                    
                    if (productionDetail.getPlanNotAppropriatedCount() <= 0) {
                        break;
                    }
                }
            }
        }
        
        //手配番号のない生産実績の処理
        logger.log(logLevel, "ProductionPlanUpdateBatchlet: Apply production without direction ID.");
        //生産実績明細から計画未充当数がゼロでないものを取得しループ
        query = entityManager.createQuery("SELECT t FROM TblProductionDetail t JOIN FETCH t.tblProduction p WHERE t.planNotAppropriatedCount != 0 AND p.directionId IS NULL");
        //生産計画テーブルから部品と工程が等しく、未完成数量がゼロより大きいものを取得するクエリ。プラス充当用
        planQuery = entityManager.createQuery(
            "SELECT t FROM TblProductionPlan t WHERE t.directionId IS NULL AND t.uncompletedCount > 0 AND t.componentId = :componentId AND t.procedureId = :procedureId ORDER BY t.procedureDueDate");
        //生産計画テーブルから部品と工程が等しく、未完成数量がゼロ以上のものを取得するクエリ。マイナス充当用
        planQueryZero = entityManager.createQuery(
            "SELECT t FROM TblProductionPlan t WHERE t.directionId IS NULL AND t.uncompletedCount >= 0 AND t.componentId = :componentId AND t.procedureId = :procedureId ORDER BY t.procedureDueDate DESC");
        List<TblProductionDetail> productionDetails = query.getResultList();
        //生産実績明細カーソルループ
        for (TblProductionDetail productionDetail : productionDetails) {
            if (productionDetail.getComponentId() != null && productionDetail.getProcedureId() != null) {
                List<TblProductionPlan> productionPlans;
                if (productionDetail.getPlanNotAppropriatedCount() > 0) {
                    //生産実績明細の計画未充当数がゼロより大きいとき：生産計画テーブルから部品と工程が等しく、未完成数量がゼロより大きいものを取得
                    //プラスの充当を実行
                    planQuery.setParameter("componentId", productionDetail.getComponentId());
                    planQuery.setParameter("procedureId", productionDetail.getProcedureId());
                    productionPlans = planQuery.getResultList();
                }
                else {
                    //生産実績明細の計画未充当数がゼロより小さいとき：生産計画テーブルから部品と工程が等しく、未完成数量がゼロのものを取得
                    //マイナスの充当を実行
                    planQueryZero.setParameter("componentId", productionDetail.getComponentId());
                    planQueryZero.setParameter("procedureId", productionDetail.getProcedureId());
                    productionPlans = planQueryZero.getResultList();
                }
                //生産計画への充当
                for (TblProductionPlan productionPlan : productionPlans) {
                    //充当数の算出
                    int appropriateCnt;
                    if (productionPlan.getUncompletedCount() <= productionDetail.getPlanNotAppropriatedCount()) {
                        appropriateCnt = productionPlan.getUncompletedCount();
                    }
                    else {
                        appropriateCnt = productionDetail.getPlanNotAppropriatedCount();
                    }
                    if (productionPlan.getCompletedCount() + appropriateCnt < 0) {
                        appropriateCnt = productionPlan.getCompletedCount() * (-1);
                    }
                    //生産計画テーブルの完成数量、未完成数量をUPDATE
                    productionPlan.setUncompletedCount(productionPlan.getUncompletedCount() - appropriateCnt);
                    productionPlan.setCompletedCount(productionPlan.getCompletedCount() + appropriateCnt);
                    entityManager.merge(productionPlan);
                    //生産実績明細の計画充当数、計画未充当数をUPDATE
                    productionDetail.setPlanNotAppropriatedCount(productionDetail.getPlanNotAppropriatedCount() - appropriateCnt);
                    productionDetail.setPlanAppropriatedCount(productionDetail.getPlanAppropriatedCount() + appropriateCnt);
                    entityManager.merge(productionDetail);
                    //充当結果を生産実績計画充当テーブルへ保存
                    TblProductionPlanAppropriation planApp = new TblProductionPlanAppropriation();
                    planApp.setId(IDGenerator.generate());
                    planApp.setProductionPlanId(productionPlan.getId());
                    planApp.setProductionDetailId(productionDetail.getId());
                    planApp.setAppropriatedCount(appropriateCnt);
                    planApp.setCreateDate(new java.util.Date());
                    planApp.setCreateUserUuid(BATCH_NAME);
                    planApp.setUpdateDate(new java.util.Date());
                    planApp.setUpdateUserUuid(BATCH_NAME);
                    entityManager.persist(planApp);
                    
                    if (productionDetail.getPlanNotAppropriatedCount() <= 0) {
                        break;
                    }
                }
            }
        }
        //生産実績取り消し分の控除
        //削除キーテーブルから削除された生産実績明細を取得
        logger.log(logLevel, "ProductionPlanUpdateBatchlet: Handle deleted production details.");
        query = entityManager.createQuery(
                "SELECT t FROM TblDeletedKey t WHERE t.tblDeletedKeyPK.tblName = :tblName AND t.tblDeletedKeyPK.deletedKey NOT IN (SELECT t.productionDetailId FROM TblProductionPlanDeduction t)");
        query.setParameter("tblName", "TblProductionDetail");
        Query appQuery = entityManager.createQuery(
                "SELECT t FROM TblProductionPlanAppropriation t WHERE t.productionDetailId = :productionDetailId");
        Query deductPlanQuery = entityManager.createQuery(
                "UPDATE TblProductionPlan t SET t.uncompletedCount = t.uncompletedCount + :uncompletedCount, t.completedCount = t.completedCount - :completedCount WHERE t.id = :id");
        List<TblDeletedKey> deletedKeys = query.getResultList();
        for (TblDeletedKey deletedKey : deletedKeys) {
            appQuery.setParameter("productionDetailId", deletedKey.getTblDeletedKeyPK().getDeletedKey());
            List<TblProductionPlanAppropriation> planApps = appQuery.getResultList();
            for (TblProductionPlanAppropriation planApp : planApps) {
                deductPlanQuery.setParameter("uncompletedCount", planApp.getAppropriatedCount());
                deductPlanQuery.setParameter("completedCount", planApp.getAppropriatedCount());
                deductPlanQuery.setParameter("id", planApp.getProductionPlanId());
                deductPlanQuery.executeUpdate();
                TblProductionPlanDeduction planDeduct = new TblProductionPlanDeduction();
                planDeduct.setProductionPlanAppropriationId(planApp.getId());
                planDeduct.setProductionDetailId(planApp.getProductionDetailId());
                planDeduct.setCreateDate(new java.util.Date());
                planDeduct.setCreateUserUuid(BATCH_NAME);
                planDeduct.setUpdateDate(new java.util.Date());
                planDeduct.setUpdateUserUuid(BATCH_NAME);
                entityManager.persist(planDeduct);
            }
        }
        logger.log(Level.INFO, "ProductionPlanUpdateBatchlet: Ended.");
        return "SUCCESS";
    }    
    
}
