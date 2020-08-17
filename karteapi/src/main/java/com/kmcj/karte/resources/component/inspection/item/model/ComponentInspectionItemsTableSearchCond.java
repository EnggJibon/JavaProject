/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item.model;

import org.apache.commons.lang.StringUtils;

/**
 * Master component inspection items table search condition
 * 
 * @author duanlin
 */
public class ComponentInspectionItemsTableSearchCond {
    private String component;
    private String componentCode;
    private String componentName;
    private String componentInspectType;
    private String outgoingCompanyName;
    private String incomingCompanyName;
    private String itemApproveStatus;
    private String withHistory;
    private String myCompanyId;

    public String getComponentCode() {
        return StringUtils.trim(componentCode);
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentName() {
        return StringUtils.trim(componentName);
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getOutgoingCompanyName() {
        return StringUtils.trim(outgoingCompanyName);
    }

    public void setOutgoingCompanyName(String outgoingCompanyName) {
        this.outgoingCompanyName = outgoingCompanyName;
    }

    public String getIncomingCompanyName() {
        return StringUtils.trim(incomingCompanyName);
    }

    public void setIncomingCompanyName(String incomingCompanyName) {
        this.incomingCompanyName = incomingCompanyName;
    }

    public String isWithHistory() {
        return withHistory;
    }

    public void setWithHistory(String withHistory) {
        this.withHistory = withHistory;
    }

    public String getMyCompanyId() {
        return myCompanyId;
    }

    public void setMyCompanyId(String myCompanyId) {
        this.myCompanyId = myCompanyId;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getComponentInspectType() {
        return componentInspectType;
    }

    public void setComponentInspectType(String componentInspectType) {
        this.componentInspectType = componentInspectType;
    }

    public String getItemApproveStatus() {
        return itemApproveStatus;
    }

    public void setItemApproveStatus(String itemApproveStatus) {
        this.itemApproveStatus = itemApproveStatus;
    }
    
}
