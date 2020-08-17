/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author BnK Win10 2010
 */
public class MstMoldPartDetail {
    private String id;
    private String moldPartCode;
    private String moldPartName;
    private String manufacturer;
    private String modelNumber;
    private String memo;
    private BigDecimal unitPrice;
    private BigDecimal usedUnitPrice;
    private Integer intrExtProduct;
    private Integer recyclableFlg;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;

    public MstMoldPartDetail() {
    }

    public MstMoldPartDetail(MstMoldPart mstMoldPart) {
        this.id = mstMoldPart.getId();
        this.moldPartCode = mstMoldPart.getMoldPartCode();
        this.moldPartName = mstMoldPart.getMoldPartName();
        this.manufacturer = mstMoldPart.getManufacturer();
        this.modelNumber = mstMoldPart.getModelNumber();
        this.memo = mstMoldPart.getMemo();
        this.unitPrice = mstMoldPart.getUnitPrice();
        this.usedUnitPrice = mstMoldPart.getUsedUnitPrice();
        this.intrExtProduct = mstMoldPart.getIntrExtProduct();
        this.recyclableFlg = mstMoldPart.getRecyclableFlg();
        this.createDate = mstMoldPart.getCreateDate();
        this.updateDate = mstMoldPart.getUpdateDate();
        this.createUserUuid = mstMoldPart.getCreateUserUuid();
        this.updateUserUuid = mstMoldPart.getUpdateUserUuid();
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoldPartCode() {
        return moldPartCode;
    }

    public void setMoldPartCode(String moldPartCode) {
        this.moldPartCode = moldPartCode;
    }

    public String getMoldPartName() {
        return moldPartName;
    }

    public void setMoldPartName(String moldPartName) {
        this.moldPartName = moldPartName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public BigDecimal getUsedUnitPrice() {
        return usedUnitPrice;
    }

    public void setUsedUnitPrice(BigDecimal usedUnitPrice) {
        this.usedUnitPrice = usedUnitPrice;
    }
    
    public Integer getIntrExtProduct() {
        return intrExtProduct;
    }

    public void setIntrExtProduct(Integer intrExtProduct) {
        this.intrExtProduct = intrExtProduct;
    }
    
    public Integer getRecyclableFlg() {
        return recyclableFlg;
    }

    public void setRecyclableFlg(Integer recyclableFlg) {
        this.recyclableFlg = recyclableFlg;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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
    
    
}
