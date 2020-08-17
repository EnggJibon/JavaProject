/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result;

import java.math.BigDecimal;

/**
 *
 * @author wangfang
 */
public class SampleGroupPlotDataInfo {
    private int groupNumber;
    private BigDecimal range;
    private BigDecimal sampleGroupMean;
    private String productionLotNum;

    public SampleGroupPlotDataInfo() {
    }
    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public BigDecimal getSampleGroupMean() {
        return sampleGroupMean;
    }

    public void setSampleGroupMean(BigDecimal sampleGroupMean) {
        this.sampleGroupMean = sampleGroupMean;
    }

    public BigDecimal getRange() {
        return range;
    }

    public void setRange(BigDecimal range) {
        this.range = range;
    }

    public String getProductionLotNum() {
        return productionLotNum;
    }

    public void setProductionLotNum(String productionLotNum) {
        this.productionLotNum = productionLotNum;
    }

}
