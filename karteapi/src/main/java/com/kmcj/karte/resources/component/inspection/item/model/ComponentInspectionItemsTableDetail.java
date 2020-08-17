/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item.model;

import java.math.BigDecimal;

/**
 * Component inspection items table detail model.
 *
 * @author duanlin
 */
public class ComponentInspectionItemsTableDetail {

    private String id;
    private String componentInspectionItemsTableId;
    private String inspectionItemSno;
    private String revisionSymbol;
    private String drawingPage;
    private String drawingAnnotation;
    private String drawingMentionNo;
    private String similarMultiitem;
    private String drawingArea;
    private String pqs;
    //private String inspectionItemNum;

    private String inspectionItemName;
    private Integer measurementType;
    private String measurementMethod;
    private BigDecimal dimensionValue;
    private BigDecimal tolerancePlus;
    private BigDecimal toleranceMinus;
    private String outgoingTrialObject;
    private String outgoingProductionObject;
    private String incomingTrialObject;
    private String incomingProductionObject;
    private String outgoingFirstMassProductionObject;
    private String incomingFirstMassProductionObject;
    
    private Integer localSeq;
    private boolean enableThAlert;
    
    //-----------Apeng 20180206 add------------
    private String processInspetionObject;
    private String additionalFlg;
    private String operationFlag; // 1:delete,3:update,4:add
    
    public String getComponentInspectionItemsTableId() {
        return componentInspectionItemsTableId;
    }

    public void setComponentInspectionItemsTableId(String componentInspectionItemsTableId) {
        this.componentInspectionItemsTableId = componentInspectionItemsTableId;
    }

    public String getInspectionItemSno() {
        return inspectionItemSno;
    }

    public void setInspectionItemSno(String inspectionItemSno) {
        this.inspectionItemSno = inspectionItemSno;
    }

    public String getRevisionSymbol() {
        return revisionSymbol;
    }

    public void setRevisionSymbol(String revisionSymbol) {
        this.revisionSymbol = revisionSymbol;
    }

    public String getDrawingPage() {
        return drawingPage;
    }

    public void setDrawingPage(String drawingPage) {
        this.drawingPage = drawingPage;
    }

    public String getDrawingAnnotation() {
        return drawingAnnotation;
    }

    public void setDrawingAnnotation(String drawingAnnotation) {
        this.drawingAnnotation = drawingAnnotation;
    }

    public String getDrawingMentionNo() {
        return drawingMentionNo;
    }

    public void setDrawingMentionNo(String drawingMentionNo) {
        this.drawingMentionNo = drawingMentionNo;
    }

    public String getSimilarMultiitem() {
        return similarMultiitem;
    }

    public void setSimilarMultiitem(String similarMultiitem) {
        this.similarMultiitem = similarMultiitem;
    }

    public String getDrawingArea() {
        return drawingArea;
    }

    public void setDrawingArea(String drawingArea) {
        this.drawingArea = drawingArea;
    }

    public String getPqs() {
        return pqs;
    }

    public void setPqs(String pqs) {
        this.pqs = pqs;
    }

    public String getInspectionItemName() {
        return inspectionItemName;
    }

    public void setInspectionItemName(String inspectionItemName) {
        this.inspectionItemName = inspectionItemName;
    }

    public Integer getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(Integer measurementType) {
        this.measurementType = measurementType;
    }

    public String getMeasurementMethod() {
        return measurementMethod;
    }

    public void setMeasurementMethod(String measurementMethod) {
        this.measurementMethod = measurementMethod;
    }

    public BigDecimal getDimensionValue() {
        return dimensionValue;
    }

    public void setDimensionValue(BigDecimal dimensionValue) {
        this.dimensionValue = dimensionValue;
    }

    public BigDecimal getTolerancePlus() {
        return tolerancePlus;
    }

    public void setTolerancePlus(BigDecimal tolerancePlus) {
        this.tolerancePlus = tolerancePlus;
    }

    public BigDecimal getToleranceMinus() {
        return toleranceMinus;
    }

    public void setToleranceMinus(BigDecimal toleranceMinus) {
        this.toleranceMinus = toleranceMinus;
    }

    public String getOutgoingTrialObject() {
        return outgoingTrialObject;
    }

    public void setOutgoingTrialObject(String outgoingTrialObject) {
        this.outgoingTrialObject = outgoingTrialObject;
    }

    public String getOutgoingProductionObject() {
        return outgoingProductionObject;
    }

    public void setOutgoingProductionObject(String outgoingProductionObject) {
        this.outgoingProductionObject = outgoingProductionObject;
    }

    public String getIncomingTrialObject() {
        return incomingTrialObject;
    }

    public void setIncomingTrialObject(String incomingTrialObject) {
        this.incomingTrialObject = incomingTrialObject;
    }

    public String getIncomingProductionObject() {
        return incomingProductionObject;
    }

    public void setIncomingProductionObject(String incomingProductionObject) {
        this.incomingProductionObject = incomingProductionObject;
    }

    public String getOutgoingFirstMassProductionObject() {
        return outgoingFirstMassProductionObject;
    }

    public void setOutgoingFirstMassProductionObject(String outgoingFirstMassProductionObject) {
        this.outgoingFirstMassProductionObject = outgoingFirstMassProductionObject;
    }

    public String getIncomingFirstMassProductionObject() {
        return incomingFirstMassProductionObject;
    }

    public void setIncomingFirstMassProductionObject(String incomingFirstMassProductionObject) {
        this.incomingFirstMassProductionObject = incomingFirstMassProductionObject;
    }

    /**
     * @return the localSeq
     */
    public Integer getLocalSeq() {
        return localSeq;
    }

    /**
     * @param localSeq the localSeq to set
     */
    public void setLocalSeq(Integer localSeq) {
        this.localSeq = localSeq;
    }

    public boolean isEnableThAlert() {
        return enableThAlert;
    }

    public void setEnableThAlert(boolean enableThAlert) {
        this.enableThAlert = enableThAlert;
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
     * @return the processInspetionObject
     */
    public String getProcessInspetionObject() {
        return processInspetionObject;
    }

    /**
     * @param processInspetionObject the processInspetionObject to set
     */
    public void setProcessInspetionObject(String processInspetionObject) {
        this.processInspetionObject = processInspetionObject;
    }

    /**
     * @return the additionalFlg
     */
    public String getAdditionalFlg() {
        return additionalFlg;
    }

    /**
     * @param additionalFlg the additionalFlg to set
     */
    public void setAdditionalFlg(String additionalFlg) {
        this.additionalFlg = additionalFlg;
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

}
