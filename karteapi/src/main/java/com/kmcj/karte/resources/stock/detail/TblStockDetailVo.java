/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.stock.detail;

import com.kmcj.karte.BasicResponse;
import java.math.BigDecimal;

/**
 *
 * @author admin
 */
public class TblStockDetailVo extends BasicResponse {

    private String uuid;

    private String procedureCode;

    private String stockType;

    private String moveDate;

    private long moveQuantity;

    private BigDecimal moveCost;

    private long stockQuantity;

    private String createUserUuid;

    private String updateUserUuid;

    private String stockId;

    private String shipmentId;

    private String productionLotNumber;

    private String orderNumber; //PO

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
     * @return the moveQuantity
     */
    public long getMoveQuantity() {
        return moveQuantity;
    }

    /**
     * @param moveQuantity the moveQuantity to set
     */
    public void setMoveQuantity(long moveQuantity) {
        this.moveQuantity = moveQuantity;
    }

    /**
     * @return the moveCost
     */
    public BigDecimal getMoveCost() {
        return moveCost;
    }

    /**
     * @param moveCost the moveCost to set
     */
    public void setMoveCost(BigDecimal moveCost) {
        this.moveCost = moveCost;
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
     * @return the stockId
     */
    public String getStockId() {
        return stockId;
    }

    /**
     * @param stockId the stockId to set
     */
    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    /**
     * @return the shipmentId
     */
    public String getShipmentId() {
        return shipmentId;
    }

    /**
     * @param shipmentId the shipmentId to set
     */
    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
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
     * @return the productionLotNumber
     */
    public String getProductionLotNumber() {
        return productionLotNumber;
    }

    /**
     * @param productionLotNumber the productionLotNumber to set
     */
    public void setProductionLotNumber(String productionLotNumber) {
        this.productionLotNumber = productionLotNumber;
    }

    /**
     * @return the orderNumber
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * @param orderNumber the orderNumber to set
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

}
