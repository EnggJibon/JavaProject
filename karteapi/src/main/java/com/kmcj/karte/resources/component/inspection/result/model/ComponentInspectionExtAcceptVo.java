package com.kmcj.karte.resources.component.inspection.result.model;

public class ComponentInspectionExtAcceptVo {
    
    private String componentCode;
    private String inspectType;
    private String poNumber;
    private String quantity;
    private String incomingMeasSamplingQuantity;
    private String incomingVisualSamplingQuantity;
    public String getComponentCode() {
        return componentCode;
    }
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }
    public String getInspectType() {
        return inspectType;
    }
    public void setInspectType(String inspectType) {
        this.inspectType = inspectType;
    }
    public String getPoNumber() {
        return poNumber;
    }
    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }
    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getIncomingMeasSamplingQuantity() {
        return incomingMeasSamplingQuantity;
    }
    public void setIncomingMeasSamplingQuantity(String incomingMeasSamplingQuantity) {
        this.incomingMeasSamplingQuantity = incomingMeasSamplingQuantity;
    }
    public String getIncomingVisualSamplingQuantity() {
        return incomingVisualSamplingQuantity;
    }
    public void setIncomingVisualSamplingQuantity(String incomingVisualSamplingQuantity) {
        this.incomingVisualSamplingQuantity = incomingVisualSamplingQuantity;
    }
        
}
