/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item.model;

/**
 * Component inspection items info For import CSV.
 * 
 * @author zhoushuai
 */
public class ComponentInspectionSearchCondForCSV {

    private String componentId;
    private String inseptionTypeId;
    private String inseptionTypeKey;
    private String inseptionTypeValue;
    private String outgoingCompanyId;
    private String fileUuid;
    private String incomingCompanyId;
    private String componentInspectionItemsTableId;

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getOutgoingCompanyId() {
        return outgoingCompanyId;
    }

    public void setOutgoingCompanyId(String outgoingCompanyId) {
        this.outgoingCompanyId = outgoingCompanyId;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    /**
     * @return the inseptionTypeId
     */
    public String getInseptionTypeId() {
        return inseptionTypeId;
    }

    /**
     * @param inseptionTypeId the inseptionTypeId to set
     */
    public void setInseptionTypeId(String inseptionTypeId) {
        this.inseptionTypeId = inseptionTypeId;
    }

    /**
     * @return the inseptionTypeKey
     */
    public String getInseptionTypeKey() {
        return inseptionTypeKey;
    }

    /**
     * @param inseptionTypeKey the inseptionTypeKey to set
     */
    public void setInseptionTypeKey(String inseptionTypeKey) {
        this.inseptionTypeKey = inseptionTypeKey;
    }

    /**
     * @return the inseptionTypeValue
     */
    public String getInseptionTypeValue() {
        return inseptionTypeValue;
    }

    /**
     * @param inseptionTypeValue the inseptionTypeValue to set
     */
    public void setInseptionTypeValue(String inseptionTypeValue) {
        this.inseptionTypeValue = inseptionTypeValue;
    }

    public String getIncomingCompanyId() {
        return incomingCompanyId;
    }

    public void setIncomingCompanyId(String incomingCompanyId) {
        this.incomingCompanyId = incomingCompanyId;
    }

    public String getComponentInspectionItemsTableId() {
        return componentInspectionItemsTableId;
    }

    public void setComponentInspectionItemsTableId(String componentInspectionItemsTableId) {
        this.componentInspectionItemsTableId = componentInspectionItemsTableId;
    }
    
}
