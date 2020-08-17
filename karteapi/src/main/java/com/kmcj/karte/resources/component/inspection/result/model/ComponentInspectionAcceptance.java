package com.kmcj.karte.resources.component.inspection.result.model;

public class ComponentInspectionAcceptance {
    private String componentCode;
    private String outgoingCompanyCode;
    private String firstFlag;
    private String inspectType;
    private String orderNumber;
    private String itemNumber;
    private Integer quantity;
    private String lotNumber;
    private Integer incomingMeasSamplingQuantity;
    private Integer incomingVisualSamplingQuantity;
    private Character massFlg;

    private String material01;
    private String material02;
    private String material03;

    private Integer cavityCnt;
    private Integer cavityStartNum;
    private String cavityPrefix;
    
    private String componentInspectionItemsTableId;
    private String incomingCompanyId;

    public Integer getCavityCnt() {
        return cavityCnt;
    }

    public Integer getCavityStartNum() {
        return cavityStartNum;
    }

    public String getCavityPrefix() {
        return cavityPrefix;
    }

    public void setCavityCnt(Integer cavityCnt) {
        this.cavityCnt = cavityCnt;
    }

    public void setCavityStartNum(Integer cavityStartNum) {
        this.cavityStartNum = cavityStartNum;
    }

    public void setCavityPrefix(String cavityPrefix) {
        this.cavityPrefix = cavityPrefix;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentCode() {
        return this.componentCode;
    }

    public String getComponentInspectionItemsTableId() {
        return componentInspectionItemsTableId;
    }

    public String getIncomingCompanyId() {
        return incomingCompanyId;
    }
    public void setOutgoingCompanyCode(String outgoingCompanyCode) { this.outgoingCompanyCode = outgoingCompanyCode; }

    public String getOutgoingCompanyCode() { return this.outgoingCompanyCode; }

    public void setInspectType(String inspectType) { this.inspectType = inspectType; }

    public String getInspectType() { return this.inspectType; }

    public void setOrderNumber(String orderNumber) {  this.orderNumber = orderNumber; }

    public String getOrderNumber() { return this.orderNumber; }

    public void setItemNumber(String itemNumber) { this.itemNumber = itemNumber; }

    public String getItemNumber() { return this.itemNumber; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getQuantity() { return this.quantity; }

    public void setIncomingMeasSamplingQuantity(Integer incomingMeasSamplingQuantity) { this.incomingMeasSamplingQuantity = incomingMeasSamplingQuantity; }

    public Integer getIncomingMeasSamplingQuantity() { return this.incomingMeasSamplingQuantity; }

    public void setIncomingVisualSamplingQuantity(Integer incomingVisualSamplingQuantity) { this.incomingVisualSamplingQuantity = incomingVisualSamplingQuantity; }

    public Integer getIncomingVisualSamplingQuantity() { return this.incomingVisualSamplingQuantity; }

    public void setFirstFlag(String firstFlag) { this.firstFlag = firstFlag; }

    public String getFirstFlag() { return this.firstFlag; }

    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }

    public String getLotNumber() { return this.lotNumber; }

    /**
     * @return the massFlg
     */
    public Character getMassFlg() {
        return massFlg;
    }

    /**
     * @param massFlg the massFlg to set
     */
    public void setMassFlg(Character massFlg) {
        this.massFlg = massFlg;
    }

    public String getMaterial01() {
        return material01;
    }

    public void setMaterial01(String material01) {
        this.material01 = material01;
    }

    public String getMaterial02() {
        return material02;
    }

    public void setMaterial02(String material02) {
        this.material02 = material02;
    }

    public String getMaterial03() {
        return material03;
    }

    public void setMaterial03(String material03) {
        this.material03 = material03;
    }
}
