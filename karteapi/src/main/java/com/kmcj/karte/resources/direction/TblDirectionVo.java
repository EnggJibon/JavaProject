/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.direction;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.component.MstComponent;

/**
 *
 * @author zhx
 */
public class TblDirectionVo extends BasicResponse {

    private String componentId;
    private String moldUuid;
    private String moldId;
    private String moldName;
    private String componentCode;
    private String componentName;
    private String id;
    private String directionCode;
    private String quantity;
    private String dueDate;
    private String directionCategory;
    private String directionCategoryText;
    private String poNumber;
    private String department;
    private String createDate;
    private String updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    private String departmentText;
    private MstComponent mstComponent;
    private String categorytext;
    
    private String noStartAmount;
    private String productionVolume;
    private String completeAmount;
    private String actionFlag;
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
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the directionCode
     */
    public String getDirectionCode() {
        return directionCode;
    }

    /**
     * @param directionCode the directionCode to set
     */
    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
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
     * @return the departmentText
     */
    public String getDepartmentText() {
        return departmentText;
    }

    /**
     * @param departmentText the departmentText to set
     */
    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    /**
     * @return the mstComponent
     */
    public MstComponent getMstComponent() {
        return mstComponent;
    }

    /**
     * @param mstComponent the mstComponent to set
     */
    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    /**
     * @return the quantity
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(String quantity) {
        this.quantity = quantity;
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
     * @return the directionCategory
     */
    public String getDirectionCategory() {
        return directionCategory;
    }

    /**
     * @param directionCategory the directionCategory to set
     */
    public void setDirectionCategory(String directionCategory) {
        this.directionCategory = directionCategory;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
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
     * @return the updateDate
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the categorytext
     */
    public String getCategorytext() {
        return categorytext;
    }

    /**
     * @param categorytext the categorytext to set
     */
    public void setCategorytext(String categorytext) {
        this.categorytext = categorytext;
    }

    

    /**
     * @return the moldName
     */
    public String getMoldName() {
        return moldName;
    }

    /**
     * @param moldName the moldName to set
     */
    public void setMoldName(String moldName) {
        this.moldName = moldName;
    }

    /**
     * @return the noStartAmount
     */
    public String getNoStartAmount() {
        return noStartAmount;
    }

    /**
     * @param noStartAmount the noStartAmount to set
     */
    public void setNoStartAmount(String noStartAmount) {
        this.noStartAmount = noStartAmount;
    }

    /**
     * @return the productionVolume
     */
    public String getProductionVolume() {
        return productionVolume;
    }

    /**
     * @param productionVolume the productionVolume to set
     */
    public void setProductionVolume(String productionVolume) {
        this.productionVolume = productionVolume;
    }

    /**
     * @return the completeAmount
     */
    public String getCompleteAmount() {
        return completeAmount;
    }

    /**
     * @param completeAmount the completeAmount to set
     */
    public void setCompleteAmount(String completeAmount) {
        this.completeAmount = completeAmount;
    }

    /**
     * @return the actionFlag
     */
    public String getActionFlag() {
        return actionFlag;
    }

    /**
     * @param actionFlag the actionFlag to set
     */
    public void setActionFlag(String actionFlag) {
        this.actionFlag = actionFlag;
    }

    /**
     * @return the directionCategoryText
     */
    public String getDirectionCategoryText() {
        return directionCategoryText;
    }

    /**
     * @param directionCategoryText the directionCategoryText to set
     */
    public void setDirectionCategoryText(String directionCategoryText) {
        this.directionCategoryText = directionCategoryText;
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
     * @return the moldUuid
     */
    public String getMoldUuid() {
        return moldUuid;
    }

    /**
     * @param moldUuid the moldUuid to set
     */
    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

}
