/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.assetmatching;

import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.resources.asset.MstAsset;
import com.kmcj.karte.resources.asset.match.*;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
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
public class AssetMatchingService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    @Inject
    private CnfSystemService cnfSystemService;

    private int isMarked;

    private int matchingLength;

    /**
     * 資産照合結果バッチテーブルの更新
     *
     * @param batchStatus
     * @param errorMsg
     * @param id
     * @return
     */
    @Transactional
    public String insertTblAssetMatchingBatch(int batchStatus, String errorMsg, String id) {

        TblAssetMatchingBatch tblAssetMatchingBatch;
        if (StringUtils.isEmpty(id)) {
            tblAssetMatchingBatch = new TblAssetMatchingBatch();
            tblAssetMatchingBatch.setId(IDGenerator.generate());
            tblAssetMatchingBatch.setStartDate(new Date());
            tblAssetMatchingBatch.setBatchStatus(batchStatus);
            tblAssetMatchingBatch.setErrorMessage(errorMsg);
            entityManager.persist(tblAssetMatchingBatch);
        } else {
            tblAssetMatchingBatch = entityManager.find(TblAssetMatchingBatch.class, id);
            tblAssetMatchingBatch.setEndDate(new Date());
            tblAssetMatchingBatch.setBatchStatus(batchStatus);
            tblAssetMatchingBatch.setErrorMessage(errorMsg);
            entityManager.merge(tblAssetMatchingBatch);
        }
        entityManager.flush();
        entityManager.clear();

        return tblAssetMatchingBatch.getId();
    }

    /**
     * <P>
     * 照合用部品ワークテーブル
     * <P>
     * 照合用資産ワークテーブル
     */
    @Transactional
    public void deleteWkTableByBatch() {
        // データをクリアする
        Query delWkAssetMatching = entityManager.createNamedQuery("WkAssetMatching.delete");
        delWkAssetMatching.executeUpdate();

        Query delWkAssetMatchingComponent = entityManager.createNamedQuery("WkAssetMatchingComponent.delete");
        delWkAssetMatchingComponent.executeUpdate();

        Query delTblAssetMatchingResult = entityManager.createNamedQuery("TblAssetMatchingResult.delete");
        delTblAssetMatchingResult.executeUpdate();

        Query delTblAssetMatchingResultDetail = entityManager.createNamedQuery("TblAssetMatchingResultDetail.delete");
        delTblAssetMatchingResultDetail.executeUpdate();
    }

    /**
     *
     * 照合用資産ワークテーブル
     * @return 
     */
    @Transactional
    public boolean updateWkAssetMatchingByBatch() {
        StringBuilder sqlBuilder = new StringBuilder();
        //　資産マスタの照合未確認データ抽出SQL
        sqlBuilder.append("SELECT t  FROM MstAsset t  WHERE t.moldMachineType = 0 ");

        Query query = entityManager.createQuery(sqlBuilder.toString());
        List list = query.getResultList();
        if (list == null || list.size() <= 0) {
            return true; //not found data
        }

        // 資産照合で品目コードから英数字以外の文字を除外フラグを取得
        isMarked = Integer.valueOf(cnfSystemService.findByKey("system", "item_component_matching_mark_exception").getConfigValue());

        // 資産照合で品目コードと部品コードのマッチング桁数
        matchingLength = Integer.valueOf(cnfSystemService.findByKey("system", "item_component_matching_length").getConfigValue());

        WkAssetMatching wkAssetMatching;
        int i = 0;
        for (Object obj : list) {
            MstAsset mstAsset = (MstAsset) obj;
            wkAssetMatching = new WkAssetMatching();
            wkAssetMatching.setAssetId(mstAsset.getUuid());
            String itemCode = null;
            if (StringUtils.isNotEmpty(mstAsset.getItemCode())) {
                itemCode = mstAsset.getItemCode();
                // 記号を除外かどうか判定 (0:除外しない、1:除外する)  
                if (1 == isMarked) {
                    itemCode = FileUtil.getSimpleStr(mstAsset.getItemCode());
                }
                // 桁数を取得
                if (itemCode.length() > matchingLength) {
                    itemCode = itemCode.substring(0, matchingLength);
                }
            }
            wkAssetMatching.setItemCode(itemCode);
            entityManager.persist(wkAssetMatching);

            // 50件毎にDBへ登録する
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            i++;
        }
        entityManager.flush();
        entityManager.clear();
        return false;
    }

    /**
     *
     * 照合用部品ワークテーブル
     */
    @Transactional
    public void updateWkAssetMatchingComponentByBatch() {

        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append(" SELECT m  FROM MstMoldComponentRelation m  "
                + " JOIN FETCH m.mstComponent mc  "
                + " JOIN FETCH m.mstMold  "
                + " ORDER BY mc.componentCode ASC ");

        Query query = entityManager.createQuery(sqlBuilder.toString());

        List list = query.getResultList();

        WkAssetMatchingComponent wkAssetMatchingComponent;

        int i = 0;
        for (Object obj : list) {
            MstMoldComponentRelation mstMoldComponentRelation = (MstMoldComponentRelation) obj;

            wkAssetMatchingComponent = new WkAssetMatchingComponent();

            wkAssetMatchingComponent.setMoldUuid(mstMoldComponentRelation.getMstMoldComponentRelationPK().getMoldUuid());
            wkAssetMatchingComponent.setComponentUuid(mstMoldComponentRelation.getMstMoldComponentRelationPK().getComponentId());

            String componentCode = mstMoldComponentRelation.getMstComponent().getComponentCode();

            // 記号を除外かどうか判定  (0:除外しない、1:除外する)
            if (1 == isMarked) {
                componentCode = FileUtil.getSimpleStr(componentCode);
            }
            // 桁数を取得
            if (componentCode.length() > matchingLength) {
                componentCode = componentCode.substring(0, matchingLength);
            }

            wkAssetMatchingComponent.setComponentCode(componentCode);
            entityManager.persist(wkAssetMatchingComponent);

            // 50件毎にDBへ登録する
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            i++;
        }
        entityManager.flush();
        entityManager.clear();

    }

    /**
     * @param batchId
     * 資産照合結果テーブルの更新
     */
    @Transactional
    public void updateTblAssetMatchingResult(String batchId) {
        
        // 資産照合結果詳細テーブルから照合結果を取得　資産ＩＤをでGROUP BY 金型数を取得
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append(" SELECT wkAssetMatching.assetId, "
                + " COUNT(resultDetail.tblAssetMatchingResultDetailPK.moldUuid) "
                + " FROM WkAssetMatching wkAssetMatching "
                + " LEFT JOIN TblAssetMatchingResultDetail resultDetail "
                + " ON  wkAssetMatching.assetId =  resultDetail.tblAssetMatchingResultDetailPK.assetId "
                + " GROUP BY  wkAssetMatching.assetId ");

        Query query = entityManager.createQuery(sqlBuilder.toString());

        List list = query.getResultList();

        TblAssetMatchingResult tblAssetMatchingResult;
        int i = 0;
        for (Object obj : list) {
            Object[] objs = (Object[]) obj;

            tblAssetMatchingResult = new TblAssetMatchingResult();

            tblAssetMatchingResult.setAssetId(String.valueOf(objs[0]));
            int count = Integer.parseInt(String.valueOf(objs[1]));
            tblAssetMatchingResult.setMatchingResult(count > 2 ? 2 : count);
            tblAssetMatchingResult.setBatchId(batchId);
            entityManager.persist(tblAssetMatchingResult);
            // 50件毎にDBへ登録する
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            i++;
        }
        entityManager.flush();
        entityManager.clear();

    }

    /**
     * 資産照合結果詳細テーブルの更新
     */
    @Transactional
    public void updateTblAssetMatchingResultDetail() {

        // ワークテーブルを結合し、照合結果を取得する
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append(" SELECT DISTINCT wkAssetMatching.assetId,wkAssetMatchingComponent.moldUuid  FROM WkAssetMatching wkAssetMatching  "
                + " JOIN FETCH wkAssetMatching.wkAssetMatchingComponent wkAssetMatchingComponent "
                + " ORDER BY wkAssetMatching.assetId,wkAssetMatchingComponent.moldUuid ");

        Query query = entityManager.createQuery(sqlBuilder.toString());

        List list = query.getResultList();
        TblAssetMatchingResultDetail tblAssetMatchingResultDetail;
        int i = 0;
        for (Object obj : list) {
            Object[] array = (Object[]) obj;

            tblAssetMatchingResultDetail = new TblAssetMatchingResultDetail();
            TblAssetMatchingResultDetailPK tblAssetMatchingResultDetailPK = new TblAssetMatchingResultDetailPK();
            tblAssetMatchingResultDetailPK.setAssetId(String.valueOf(array[0]));
            tblAssetMatchingResultDetailPK.setMoldUuid(String.valueOf(array[1]));
            tblAssetMatchingResultDetail.setTblAssetMatchingResultDetailPK(tblAssetMatchingResultDetailPK);
            entityManager.persist(tblAssetMatchingResultDetail);
            // 50件毎にDBへ登録する
            if (i % 50 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            i++;
        }
        entityManager.flush();
        entityManager.clear();

    }
}
