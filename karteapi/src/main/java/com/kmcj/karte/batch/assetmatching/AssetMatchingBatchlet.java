/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.assetmatching;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author xiaozhou.wang
 */
@Named
@Dependent
public class AssetMatchingBatchlet extends AbstractBatchlet {

    @Inject
    private AssetMatchingService assetMatchingService;

    @Override
    public String process() {
        String batchId = null;
        try {
            // 照合バッチスタートを宣言
            batchId = assetMatchingService.insertTblAssetMatchingBatch(1, null, null);

            //ワークテーブルをクリアする
            assetMatchingService.deleteWkTableByBatch();

            //照合用資産ワークテーブルを作成
            boolean resultFlg = assetMatchingService.updateWkAssetMatchingByBatch();

            // 未照合データがなければ、処理終了
            if (resultFlg) {
                // 照合バッチ正常終了を宣言
                assetMatchingService.insertTblAssetMatchingBatch(2, null, batchId);
                return null;
            }

            // 照合用部品ワークテーブル
            assetMatchingService.updateWkAssetMatchingComponentByBatch();

            // 資産照合結果明細テーブル
            assetMatchingService.updateTblAssetMatchingResultDetail();

            // 資産照合結果テーブル
            assetMatchingService.updateTblAssetMatchingResult(batchId);

            // 照合バッチ正常終了を宣言
            assetMatchingService.insertTblAssetMatchingBatch(2, null, batchId);

        } catch (Exception e) {
            e.printStackTrace();
            assetMatchingService.insertTblAssetMatchingBatch(3, e.getMessage(), batchId);
        }

        return BatchStatus.COMPLETED.toString();
    }
}
