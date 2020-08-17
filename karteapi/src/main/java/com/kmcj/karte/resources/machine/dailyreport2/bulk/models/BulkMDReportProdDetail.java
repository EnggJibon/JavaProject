/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2.bulk.models;

import java.math.BigDecimal;

/**
 *
 * @author f.kitaoji
 */
public class BulkMDReportProdDetail {
    private String partCode;
    private String procedureCode;
    private int countPerShot;
    private int defectCount;
    private int completeCount;
    private String material01Code;
    private String material01LotNo;
    private BigDecimal material01Amount = BigDecimal.ZERO;
    private BigDecimal material01PurgedAmount = BigDecimal.ZERO;
    private String material02Code;
    private String material02LotNo;
    private BigDecimal material02Amount = BigDecimal.ZERO;
    private BigDecimal material02PurgedAmount = BigDecimal.ZERO;
    private String material03Code;
    private String material03LotNo;
    private BigDecimal material03Amount = BigDecimal.ZERO;
    private BigDecimal material03PurgedAmount = BigDecimal.ZERO;
    
    /**
     * @return the partCode
     */
    public String getPartCode() {
        return partCode;
    }

    /**
     * @param partCode the partCode to set
     */
    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    /**
     * @return the procedureCode
     */
    public String getProcedureCode() {
        return procedureCode;
    }

    /**
     * @param procedureCode the procedureCode to set
     */
    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    /**
     * @return the countPerShot
     */
    public int getCountPerShot() {
        return countPerShot;
    }

    /**
     * @param countPerShot the countPerShot to set
     */
    public void setCountPerShot(int countPerShot) {
        this.countPerShot = countPerShot;
    }

    /**
     * @return the defectCount
     */
    public int getDefectCount() {
        return defectCount;
    }

    /**
     * @param defectCount the defectCount to set
     */
    public void setDefectCount(int defectCount) {
        this.defectCount = defectCount;
    }

    /**
     * @return the completeCount
     */
    public int getCompleteCount() {
        return completeCount;
    }

    /**
     * @param completeCount the completeCount to set
     */
    public void setCompleteCount(int completeCount) {
        this.completeCount = completeCount;
    }

    /**
     * @return the material01Code
     */
    public String getMaterial01Code() {
        return material01Code;
    }

    /**
     * @param material01Code the material01Code to set
     */
    public void setMaterial01Code(String material01Code) {
        this.material01Code = material01Code;
    }

    /**
     * @return the material01LotNo
     */
    public String getMaterial01LotNo() {
        return material01LotNo;
    }

    /**
     * @param material01LotNo the material01LotNo to set
     */
    public void setMaterial01LotNo(String material01LotNo) {
        this.material01LotNo = material01LotNo;
    }

    /**
     * @return the material01Amount
     */
    public BigDecimal getMaterial01Amount() {
        return material01Amount;
    }

    /**
     * @param material01Amount the material01Amount to set
     */
    public void setMaterial01Amount(BigDecimal material01Amount) {
        this.material01Amount = material01Amount;
    }

    /**
     * @return the material01PurgedAmount
     */
    public BigDecimal getMaterial01PurgedAmount() {
        return material01PurgedAmount;
    }

    /**
     * @param material01PurgedAmount the material01PurgedAmount to set
     */
    public void setMaterial01PurgedAmount(BigDecimal material01PurgedAmount) {
        this.material01PurgedAmount = material01PurgedAmount;
    }

    /**
     * @return the material02Code
     */
    public String getMaterial02Code() {
        return material02Code;
    }

    /**
     * @param material02Code the material02Code to set
     */
    public void setMaterial02Code(String material02Code) {
        this.material02Code = material02Code;
    }

    /**
     * @return the material02LotNo
     */
    public String getMaterial02LotNo() {
        return material02LotNo;
    }

    /**
     * @param material02LotNo the material02LotNo to set
     */
    public void setMaterial02LotNo(String material02LotNo) {
        this.material02LotNo = material02LotNo;
    }

    /**
     * @return the material02Amount
     */
    public BigDecimal getMaterial02Amount() {
        return material02Amount;
    }

    /**
     * @param material02Amount the material02Amount to set
     */
    public void setMaterial02Amount(BigDecimal material02Amount) {
        this.material02Amount = material02Amount;
    }

    /**
     * @return the material02PurgedAmount
     */
    public BigDecimal getMaterial02PurgedAmount() {
        return material02PurgedAmount;
    }

    /**
     * @param material02PurgedAmount the material02PurgedAmount to set
     */
    public void setMaterial02PurgedAmount(BigDecimal material02PurgedAmount) {
        this.material02PurgedAmount = material02PurgedAmount;
    }

    /**
     * @return the material03Code
     */
    public String getMaterial03Code() {
        return material03Code;
    }

    /**
     * @param material03Code the material03Code to set
     */
    public void setMaterial03Code(String material03Code) {
        this.material03Code = material03Code;
    }

    /**
     * @return the material03LotNo
     */
    public String getMaterial03LotNo() {
        return material03LotNo;
    }

    /**
     * @param material03LotNo the material03LotNo to set
     */
    public void setMaterial03LotNo(String material03LotNo) {
        this.material03LotNo = material03LotNo;
    }

    /**
     * @return the material03Amount
     */
    public BigDecimal getMaterial03Amount() {
        return material03Amount;
    }

    /**
     * @param material03Amount the material03Amount to set
     */
    public void setMaterial03Amount(BigDecimal material03Amount) {
        this.material03Amount = material03Amount;
    }

    /**
     * @return the material03PurgedAmount
     */
    public BigDecimal getMaterial03PurgedAmount() {
        return material03PurgedAmount;
    }

    /**
     * @param material03PurgedAmount the material03PurgedAmount to set
     */
    public void setMaterial03PurgedAmount(BigDecimal material03PurgedAmount) {
        this.material03PurgedAmount = material03PurgedAmount;
    }
    
}
