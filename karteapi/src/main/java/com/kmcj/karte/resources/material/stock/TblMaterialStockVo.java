/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material.stock;

import com.kmcj.karte.BasicResponse;
import java.math.BigDecimal;

public class TblMaterialStockVo extends BasicResponse {

    private String uuid;

    private BigDecimal stockQty;

    private int stockUnit;
    
    private String stockUnitText;

    private String materialId;

    private String materialCode;//材料コード

    private String materialName;//材料名

    private String updateDateTime;//更新日

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the stockQty
     */
    public BigDecimal getStockQty() {
        return stockQty;
    }

    /**
     * @param stockQty the stockQty to set
     */
    public void setStockQty(BigDecimal stockQty) {
        this.stockQty = stockQty;
    }

    /**
     * @return the stockUnit
     */
    public int getStockUnit() {
        return stockUnit;
    }

    /**
     * @param stockUnit the stockUnit to set
     */
    public void setStockUnit(int stockUnit) {
        this.stockUnit = stockUnit;
    }

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
     * @return the materialCode
     */
    public String getMaterialCode() {
        return materialCode;
    }

    /**
     * @param materialCode the materialCode to set
     */
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    /**
     * @return the materialName
     */
    public String getMaterialName() {
        return materialName;
    }

    /**
     * @param materialName the materialName to set
     */
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    /**
     * @return the updateDateTimeStr
     */
    public String getUpdateDateTime() {
        return updateDateTime;
    }

    /**
     * @param updateDateTime the updateDateTime to set
     */
    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    /**
     * @return the stockUnitText
     */
    public String getStockUnitText() {
        return stockUnitText;
    }

    /**
     * @param stockUnitText the stockUnitText to set
     */
    public void setStockUnitText(String stockUnitText) {
        this.stockUnitText = stockUnitText;
    }

}
