/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.lot.balance;

/**
 *
 * @author f.kitaoji
 */
public class LotBalanceUpdate {
    private String productionDetailId = null;
    private int completeCount = 0;

    /**
     * @return the productionDetailId
     */
    public String getProductionDetailId() {
        return productionDetailId;
    }

    /**
     * @param productionDetailId the productionDetailId to set
     */
    public void setProductionDetailId(String productionDetailId) {
        this.productionDetailId = productionDetailId;
    }

    /**
     * @return the completeCount
     */
    public int getCompleteCount() {
        return completeCount;
    }

    /**
     * @param completeCount the completeCount to set
     */
    public void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }
}
