package com.kmcj.karte.resources.component.registration;

import java.math.BigDecimal;
import java.util.List;

public class MstComponentRegistrationList {
    private String componentCode = "";
    private String componentName = "";
    private Integer componentType = null;
    private Integer isCircuitBoard = 0;
    private BigDecimal unitPrice = new BigDecimal("0.0");
    private String currencyCode = "";
    private Integer stockUnit = 0;
    private Integer snLength = 0;
    private String snFixedValue = "";
    private Integer snFixedLength = 0;
    private Integer isPurchasedPart = 0;
    private List<MstComponentSpecList> mstComponentSpecCollection;

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentType(Integer componentType) {
        this.componentType = componentType;
    }

    public Integer getComponentType() {
        return this.componentType;
    }

    public void setIsCircuitBoard(Integer isCircuitBoard) {
        this.isCircuitBoard = isCircuitBoard;
    }

    public Integer getIsCircuitBoard() {
        return this.isCircuitBoard;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setStockUnit(Integer stockUnit) {
        this.stockUnit = stockUnit;
    }

    public Integer getStockUnit() {
        return this.stockUnit;
    }

    public void setSnLength(Integer snLength) {
        this.snLength = snLength;
    }

    public Integer getSnLength() {
        return this.snLength;
    }

    public void setSnFixedValue(String snFixedValue) {
        this.snFixedValue = snFixedValue;
    }

    public String getSnFixedValue() {
        return this.snFixedValue;
    }

    public void setSnFixedLength(Integer snFixedLength) {
        this.snFixedLength = snFixedLength;
    }

    public Integer getSnFixedLength() {
        return this.snFixedLength;
    }

    public void setIsPurchasedPart(Integer isPurchasedPart) {
        this.isPurchasedPart = isPurchasedPart;
    }

    public Integer getIsPurchasedPart() {
        return this.isPurchasedPart;
    }

    public void setMstComponentSpecCollection(List<MstComponentSpecList> mstComponentSpecCollection) {
        this.mstComponentSpecCollection = mstComponentSpecCollection;
    }

    public List<MstComponentSpecList> getMstComponentSpecCollection() {
        return this.mstComponentSpecCollection;
    }
}
