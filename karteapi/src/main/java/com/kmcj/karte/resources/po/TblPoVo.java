/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.po;

import com.kmcj.karte.BasicResponse;
import java.math.BigDecimal;
import java.util.Date;

public class TblPoVo extends BasicResponse {

    private int editFlag;

    private String uuid;
    
    private BigDecimal unitPrice;

    private String deliveryDestId;

    private String deliveryDestName;

    private String orderNumber;

    private String itemNumber;

    private String componentId;

    private String componentCode;

    private String componentName;

    private int quantity;

    private String orderDate;

    private String dueDate;
    
    private String createUserUuid;
    private Date createDate;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDeliveryDestId() {
        return deliveryDestId;
    }

    public void setDeliveryDestId(String deliveryDestId) {
        this.deliveryDestId = deliveryDestId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the orderDate
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * @param orderDate the orderDate to set
     */
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * @return the dueDate
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
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
     * @return the deliveryDestName
     */
    public String getDeliveryDestName() {
        return deliveryDestName;
    }

    /**
     * @param deliveryDestName the deliveryDestName to set
     */
    public void setDeliveryDestName(String deliveryDestName) {
        this.deliveryDestName = deliveryDestName;
    }

    /**
     * @return the editFlag
     */
    public int getEditFlag() {
        return editFlag;
    }

    /**
     * @param editFlag the editFlag to set
     */
    public void setEditFlag(int editFlag) {
        this.editFlag = editFlag;
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

}
