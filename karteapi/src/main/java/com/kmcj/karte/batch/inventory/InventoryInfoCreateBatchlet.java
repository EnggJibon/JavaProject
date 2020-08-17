/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.inventory;

import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author xiaozhou.wang
 */
@Named("inventoryInfoCreateBatchlet")
@Dependent
public class InventoryInfoCreateBatchlet extends AbstractBatchlet {

    @Inject
    private InventoryInfoCreateService inventoryInfoCreateService;

    @Inject
    @BatchProperty(name = "inventoryId")
    private String inventoryId;

    @Override
    public String process() {

        try {

            //　棚卸実施明細データを作成
            inventoryInfoCreateService.insertTblInventoryDetail(inventoryId);

            // 管理先テーブルデータを作成
            int mgmtCompanyCount = inventoryInfoCreateService.insertTblInventoryMgmtCompany(inventoryId);

            // 管理先ごとに依頼表を作成
            inventoryInfoCreateService.createInventoryRequestFile(inventoryId);

            // 資産データ抽出バッチ処理完了時
            inventoryInfoCreateService.updateInventoryStatus(inventoryId, mgmtCompanyCount);

        } catch (Exception ex) {

            inventoryInfoCreateService.deleteInventoryByException(inventoryId);

            ex.printStackTrace();

        }
        return BatchStatus.COMPLETED.toString();
    }
}
