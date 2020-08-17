/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.production;

import com.kmcj.karte.BasicResponse;

/**
 * 金型期間生産実績テーブル JSON送受信用クラス
 *
 * @author lyd
 */
public class TblMoldProductionPeriodDetailVo extends BasicResponse {

    // 期間別テーブル
    private String productionDate;    // 生産日
    private long completedCount;  // 完成数

    /**
     * @return the productionDate
     */
    public String getProductionDate() {
        return productionDate;
    }

    /**
     * @param productionDate the productionDate to set
     */
    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    /**
     * @return the completedCount
     */
    public long getCompletedCount() {
        return completedCount;
    }

    /**
     * @param completedCount the completedCount to set
     */
    public void setCompletedCount(long completedCount) {
        this.completedCount = completedCount;
    }

}
