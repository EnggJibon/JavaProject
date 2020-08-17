/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material.stock.detail;

import com.kmcj.karte.BasicResponse;
import java.math.BigDecimal;

/**
 *
 * @author admin
 */
public class TblMaterialStockDetailVo extends BasicResponse {

    private String uuid;

    private String stockType;
    
    private BigDecimal stockQuantity;

    private BigDecimal moveQuantity;
    
    private String materialLotNo;
    
    private String moveDate;

    private String createDate;

    private String createUserUuid;

    private String updateUserUuid;

    private String materialId;

    private String materialLotId;

    private String productionDetailId;

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
     * @return the stockType
     */
    public String getStockType() {
        return stockType;
    }

    /**
     * @param stockType the stockType to set
     */
    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    /**
     * @return the moveQuantity
     */
    public BigDecimal getMoveQuantity() {
        return moveQuantity;
    }

    /**
     * @param moveQuantity the moveQuantity to set
     */
    public void setMoveQuantity(BigDecimal moveQuantity) {
        this.moveQuantity = moveQuantity;
    }

    /**
     * @return the createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the createUserUuid
     */
    public String getCreateUserUuid() {
        return createUserUuid;
    }

    /**
     * @param createUserUuid the createUserUuid to set
     */
    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    /**
     * @return the updateUserUuid
     */
    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    /**
     * @param updateUserUuid the updateUserUuid to set
     */
    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
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
     * @return the materialLotId
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    /**
     * @param materialLotId the materialLotId to set
     */
    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
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
     * @return the moveDate
     */
    public String getMoveDate() {
        return moveDate;
    }

    /**
     * @param moveDate the moveDate to set
     */
    public void setMoveDate(String moveDate) {
        this.moveDate = moveDate;
    }

    /**
     * @return the stockQuantity
     */
    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    /**
     * @param stockQuantity the stockQuantity to set
     */
    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
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

}
