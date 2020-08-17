/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.stock;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author admin
 */
public class TblProductionStockVo extends BasicResponse {

    private String uuid;

    private String procedureCode;

    private int stockQuantity;

    private Date createDate;

    private Date updateDate;

    private String createUserUuid;

    private String updateUserUuid;

    private String componentId;

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
    public int getStockQuantity() {
        return stockQuantity;
    }

    /**
     * @param stockQuantity the stockQuantity to set
     */
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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

}
