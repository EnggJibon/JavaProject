/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Component inspection items table info model.
 * 
 * @author duanlin
 */
public class ComponentInspectionItemsTableInfo {

    private String componentInspectionItemsTableId;

    private String componentId;
    private String componentCode;
    private String componentName;
    private Integer componentType;
    private String outgoingCompanyId;
    private String outgoingCompanyName;
    private String incomingCompanyId;
    private String incomingCompanyName;
    private String version;

    private String entryPersonName;
    private String approvePersonName;

    private Date approveDate;
    private Date applyStartDate;
    private Date applyEndDate;
    
    private String inseptionTypeId;
    private String inseptionTypeKey;
    private String inseptionTypeValue;
    
    private List<ComponentInspectionItemsTableDetail> inspectionItemsTableDetails;
    private List<ComponentInspectionItemsFileDetail> inspectionItemsFileDetails;

    private Integer actionFlg = 0;
    private Integer fileFlg;
    private String itemFileId;
    private String itemFileUuid;
    
    private BigDecimal measSampleRatio;
    private BigDecimal visSampleRatio;
    
    private String itemApproveComment;
    private Integer itemApproveStatus;
    private String itemApproveStatusText;

    public String getComponentInspectionItemsTableId() {
        return componentInspectionItemsTableId;
    }

    public void setComponentInspectionItemsTableId(String componentInspectionItemsTableId) {
        this.componentInspectionItemsTableId = componentInspectionItemsTableId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Integer getComponentType() {
        return componentType;
    }

    public void setComponentType(Integer componentType) {
        this.componentType = componentType;
    }

    public String getOutgoingCompanyId() {
        return outgoingCompanyId;
    }

    public void setOutgoingCompanyId(String outgoingCompanyId) {
        this.outgoingCompanyId = outgoingCompanyId;
    }

    public String getIncomingCompanyId() {
        return incomingCompanyId;
    }

    public void setIncomingCompanyId(String incomingCompanyId) {
        this.incomingCompanyId = incomingCompanyId;
    }

    public String getOutgoingCompanyName() {
        return outgoingCompanyName;
    }

    public void setOutgoingCompanyName(String outgoingCompanyName) {
        this.outgoingCompanyName = outgoingCompanyName;
    }

    public String getIncomingCompanyName() {
        return incomingCompanyName;
    }

    public void setIncomingCompanyName(String incomingCompanyName) {
        this.incomingCompanyName = incomingCompanyName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEntryPersonName() {
        return entryPersonName;
    }

    public void setEntryPersonName(String entryPersonName) {
        this.entryPersonName = entryPersonName;
    }

    public String getApprovePersonName() {
        return approvePersonName;
    }

    public void setApprovePersonName(String approvePersonName) {
        this.approvePersonName = approvePersonName;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public Date getApplyStartDate() {
        return applyStartDate;
    }

    public void setApplyStartDate(Date applyStartDate) {
        this.applyStartDate = applyStartDate;
    }

    public Date getApplyEndDate() {
        return applyEndDate;
    }

    public void setApplyEndDate(Date applyEndDate) {
        this.applyEndDate = applyEndDate;
    }

    public List<ComponentInspectionItemsTableDetail> getInspectionItemsTableDetails() {
        return inspectionItemsTableDetails;
    }

    public void setInspectionItemsTableDetails(List<ComponentInspectionItemsTableDetail> inspectionItemsTableDetails) {
        this.inspectionItemsTableDetails = inspectionItemsTableDetails;
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

    public List<ComponentInspectionItemsFileDetail> getInspectionItemsFileDetails() {
        return inspectionItemsFileDetails;
    }

    public void setInspectionItemsFileDetails(List<ComponentInspectionItemsFileDetail> inspectionItemsFileDetails) {
        this.inspectionItemsFileDetails = inspectionItemsFileDetails;
    }

    public Integer getFileFlg() {
        return fileFlg;
    }

    public void setFileFlg(Integer fileFlg) {
        this.fileFlg = fileFlg;
    }

    public String getItemFileId() {
        return itemFileId;
    }

    public void setItemFileId(String itemFileId) {
        this.itemFileId = itemFileId;
    }

    public String getItemFileUuid() {
        return itemFileUuid;
    }

    public void setItemFileUuid(String itemFileUuid) {
        this.itemFileUuid = itemFileUuid;
    }

    public Integer getActionFlg() {
        return actionFlg;
    }

    public void setActionFlg(Integer actionFlg) {
        this.actionFlg = actionFlg;
    }

    public BigDecimal getMeasSampleRatio() {
        return measSampleRatio;
    }

    public void setMeasSampleRatio(BigDecimal measSampleRatio) {
        this.measSampleRatio = measSampleRatio;
    }

    public BigDecimal getVisSampleRatio() {
        return visSampleRatio;
    }

    public void setVisSampleRatio(BigDecimal visSampleRatio) {
        this.visSampleRatio = visSampleRatio;
    }

    public String getItemApproveComment() {
        return itemApproveComment;
    }

    public Integer getItemApproveStatus() {
        return itemApproveStatus;
    }

    public String getItemApproveStatusText() {
        return itemApproveStatusText;
    }

    public void setItemApproveComment(String itemApproveComment) {
        this.itemApproveComment = itemApproveComment;
    }

    public void setItemApproveStatus(Integer itemApproveStatus) {
        this.itemApproveStatus = itemApproveStatus;
    }

    public void setItemApproveStatusText(String itemApproveStatusText) {
        this.itemApproveStatusText = itemApproveStatusText;
    }
    
}
