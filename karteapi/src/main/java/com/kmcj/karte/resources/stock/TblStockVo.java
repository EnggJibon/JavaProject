/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.stock;

import com.kmcj.karte.BasicResponse;
import java.math.BigDecimal;

/**
 *
 * @author admin
 */
public class TblStockVo extends BasicResponse {

    private String uuid;

    private String procedureCode;

    private String procedureName;

    private long stockQuantity;//在庫数

    private String componentId;//部品Id

    private String componentCode;//部品code

    private BigDecimal unitPrice;

    private BigDecimal stockCost;//在庫金額

    private String currencyUnit;//通貨単位

    private int stockUnit;//

    private String updateDateTime;//更新日時

    private String operationFlag;

    private int isPurchasedPart;//購入部品フラグ
    private String componentName;//部品名称

    private String stockChangeDate; //入出庫日

    private String moveDate;

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
     * @return the procedureCode
     */
    public String getProcedureCode() {
        return procedureCode;
    }

    /**
     * @param procedureCode the procedureCode to set
     */
    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    /**
     * @return the stockQuantity
     */
    public long getStockQuantity() {
        return stockQuantity;
    }

    /**
     * @param stockQuantity the stockQuantity to set
     */
    public void setStockQuantity(long stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public BigDecimal getStockCost() {
        return stockCost;
    }

    public void setStockCost(BigDecimal stockCost) {
        this.stockCost = stockCost;
    }

    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public int getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(int stockUnit) {
        this.stockUnit = stockUnit;
    }

    /**
     * @return the updateDateTime
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
     * @return the unitPrice
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the componentId
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * @param componentId the componentId to set
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the operationFlag
     */
    public String getOperationFlag() {
        return operationFlag;
    }

    /**
     * @param operationFlag the operationFlag to set
     */
    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    /**
     * @return the isPurchasedPart
     */
    public int getIsPurchasedPart() {
        return isPurchasedPart;
    }

    /**
     * @param isPurchasedPart the isPurchasedPart to set
     */
    public void setIsPurchasedPart(int isPurchasedPart) {
        this.isPurchasedPart = isPurchasedPart;
    }

    /**
     * @return the componentName
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param componentName the componentName to set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * @return the stockChangeDate
     */
    public String getStockChangeDate() {
        return stockChangeDate;
    }

    /**
     * @param stockChangeDate the stockChangeDate to set
     */
    public void setStockChangeDate(String stockChangeDate) {
        this.stockChangeDate = stockChangeDate;
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
     * @return the procedureName
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * @param procedureName the procedureName to set
     */
    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

}
