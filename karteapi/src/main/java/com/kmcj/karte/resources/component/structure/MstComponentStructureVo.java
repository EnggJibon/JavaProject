/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.structure;

import com.kmcj.karte.BasicResponse;

/**
 *
 * @author admin
 */
public class MstComponentStructureVo extends BasicResponse {

    private String uuid;

    private int quantity;

    private String componentCode;

    private String rootComponentCode;
    
    private String parentComponentCode;

    private String createUserUuid;

    private String updateUserUuid;

    private String componentId;

    private String rootComponentId;

    private String parentComponentId;

    private String operationFlag; // 1:delete,3:update,4:add
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

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
     * @return the rootComponentId
     */
    public String getRootComponentId() {
        return rootComponentId;
    }

    /**
     * @param rootComponentId the rootComponentId to set
     */
    public void setRootComponentId(String rootComponentId) {
        this.rootComponentId = rootComponentId;
    }

    /**
     * @return the parentComponentId
     */
    public String getParentComponentId() {
        return parentComponentId;
    }

    /**
     * @param parentComponentId the parentComponentId to set
     */
    public void setParentComponentId(String parentComponentId) {
        this.parentComponentId = parentComponentId;
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
     * @return the rootComponentCode
     */
    public String getRootComponentCode() {
        return rootComponentCode;
    }

    /**
     * @param rootComponentCode the rootComponentCode to set
     */
    public void setRootComponentCode(String rootComponentCode) {
        this.rootComponentCode = rootComponentCode;
    }

    /**
     * @return the parentComponentCode
     */
    public String getParentComponentCode() {
        return parentComponentCode;
    }

    /**
     * @param parentComponentCode the parentComponentCode to set
     */
    public void setParentComponentCode(String parentComponentCode) {
        this.parentComponentCode = parentComponentCode;
    }
}
