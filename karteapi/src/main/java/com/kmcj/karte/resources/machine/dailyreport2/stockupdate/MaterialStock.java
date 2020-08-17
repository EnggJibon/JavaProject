/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2.stockupdate;

import java.math.BigDecimal;

/**
 *
 * @author f.kitaoji
 */
public class MaterialStock {

    private String materialId;
    private String productionId;
    private String productionDetailId;
    private String materialLotNo;
    private BigDecimal quantity;

    /**
     * @return the materialId
     */
    public String getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    /**
     * @return the productionId
     */
    public String getProductionId() {
        return productionId;
    }

    /**
     * @param productionId the productionId to set
     */
    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

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
     * @return the materialLotNo
     */
    public String getMaterialLotNo() {
        return materialLotNo;
    }

    /**
     * @param materialLotNo the materialLotNo to set
     */
    public void setMaterialLotNo(String materialLotNo) {
        this.materialLotNo = materialLotNo;
    }

    /**
     * @return the quantity
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    
}
