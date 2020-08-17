/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.po.shipment;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.po.TblPoVo;

public class TblShipmentVo extends BasicResponse {

    private String uuid;

    private String shipDate;

    private String productionStartDatetime;

    private String productionEndDatetime;

    private int shipQuantity;

    private int completeQuantity;

    private int linkFlag; //1:show link
    
    private long stockQuantity;

    private String productionLotNumber;
    
    private String productionId;
    
    private String productionDetailId;

    private String componentId;

    private String componentCode;
    
    private String componentName;

    private String machineName;

    private String moldId;

    private TblPoVo tblPoVo;
    //-------------Apeng 20471106 add------------
    private Integer poNumberCount;
    private String poNumber;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public String getProductionLotNumber() {
        return productionLotNumber;
    }

    public void setProductionLotNumber(String productionLotNumber) {
        this.productionLotNumber = productionLotNumber;
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
     * @return the tblPoVo
     */
    public TblPoVo getTblPoVo() {
        return tblPoVo;
    }

    /**
     * @param tblPoVo the tblPoVo to set
     */
    public void setTblPoVo(TblPoVo tblPoVo) {
        this.tblPoVo = tblPoVo;
    }

    /**
     * @return the shipQuantity
     */
    public int getShipQuantity() {
        return shipQuantity;
    }

    /**
     * @param shipQuantity the shipQuantity to set
     */
    public void setShipQuantity(int shipQuantity) {
        this.shipQuantity = shipQuantity;
    }

    /**
     * @param completeQuantity the completeQuantity to set
     */
    public void setCompleteQuantity(int completeQuantity) {
        this.completeQuantity = completeQuantity;
    }

    /**
     * @return the componentCode
     */
    public String getComponentCode() {
        return componentCode;
    }

    /**
     * @param componentCode the componentCode to set
     */
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    /**
     * @return the completeQuantity
     */
    public int getCompleteQuantity() {
        return completeQuantity;
    }

    /**
     * @return the linkFlag
     */
    public int getLinkFlag() {
        return linkFlag;
    }

    /**
     * @param linkFlag the linkFlag to set
     */
    public void setLinkFlag(int linkFlag) {
        this.linkFlag = linkFlag;
    }

    /**
     * @return the productionStartDatetime
     */
    public String getProductionStartDatetime() {
        return productionStartDatetime;
    }

    /**
     * @param productionStartDatetime the productionStartDatetime to set
     */
    public void setProductionStartDatetime(String productionStartDatetime) {
        this.productionStartDatetime = productionStartDatetime;
    }

    /**
     * @return the productionEndDatetime
     */
    public String getProductionEndDatetime() {
        return productionEndDatetime;
    }

    /**
     * @param productionEndDatetime the productionEndDatetime to set
     */
    public void setProductionEndDatetime(String productionEndDatetime) {
        this.productionEndDatetime = productionEndDatetime;
    }

    /**
     * @return the machineName
     */
    public String getMachineName() {
        return machineName;
    }

    /**
     * @param machineName the machineName to set
     */
    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    /**
     * @return the moldId
     */
    public String getMoldId() {
        return moldId;
    }

    /**
     * @param moldId the moldId to set
     */
    public void setMoldId(String moldId) {
        this.moldId = moldId;
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
     * @return the poNumberCount
     */
    public Integer getPoNumberCount() {
        return poNumberCount;
    }

    /**
     * @param poNumberCount the poNumberCount to set
     */
    public void setPoNumberCount(Integer poNumberCount) {
        this.poNumberCount = poNumberCount;
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
     * @return the poNumber
     */
    public String getPoNumber() {
        return poNumber;
    }

    /**
     * @param poNumber the poNumber to set
     */
    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

}
