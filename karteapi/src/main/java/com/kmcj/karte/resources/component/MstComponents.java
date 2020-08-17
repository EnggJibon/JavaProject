/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstComponents extends BasicResponse {
    
    private String id;
    private String imgFilePath01;
    private String imgFilePath02;
    private String imgFilePath03;
    private String imgFilePath04;
    private String imgFilePath05;
    private String imgFilePath06;
    private String imgFilePath07;
    private String imgFilePath08;
    private String imgFilePath09;
    private String imgFilePath10;
    private int isCircuitBoard;
    
    /**  KM-147 対応　*/
    private String createDate;
    private String updateDate;
    private String createUserName;
    private String updateUserName;
    
    private String unitPrice;

    private String currencyCode;

    private int stockUnit;
    
    private int isPurchasedPart;//購入部品フラグ
    
    /** 2017/12/06 基板追加項 */
    private int snLength;
    private String snFixedValue;
    private int snFixedLength;
    
    private String componentCode;
    private String componentName;
    private String componentType;
    private int attrId;
    private String  attrValue;
    
    private String processesNotRegistered;
    
    private List<MstComponents> mstComponentVos;
    
    public MstComponents (){
    }
    
    public MstComponents(String componentCode, String componentName, String componentType, String processesNotRegistered) {
        this.componentCode = componentCode;
        this.componentName = componentName;
        this.componentType = componentType;
        this.processesNotRegistered = processesNotRegistered;
    }

    /**
     * @return the componentCode
     */
    public String getComponentCode() {
        return componentCode;
    }

    /**
     * @param componentCode the componentCode to set
     */
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    /**
     * @return the componentName
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param componentName the componentName to set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * @return the componentType
     */
    public String getComponentType() {
        return componentType;
    }

    /**
     * @param componentType the componentType to set
     */
    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    /**
     * @return the attrId
     */
    public int getAttrId() {
        return attrId;
    }

    /**
     * @param attrId the attrId to set
     */
    public void setAttrId(int attrId) {
        this.attrId = attrId;
    }

    /**
     * @return the attrValue
     */
    public String getAttrValue() {
        return attrValue;
    }

    /**
     * @param attrValue the attrValue to set
     */
    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
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
     * @return the imgFilePath01
     */
    public String getImgFilePath01() {
        return imgFilePath01;
    }

    /**
     * @param imgFilePath01 the imgFilePath01 to set
     */
    public void setImgFilePath01(String imgFilePath01) {
        this.imgFilePath01 = imgFilePath01;
    }

    /**
     * @return the imgFilePath02
     */
    public String getImgFilePath02() {
        return imgFilePath02;
    }

    /**
     * @param imgFilePath02 the imgFilePath02 to set
     */
    public void setImgFilePath02(String imgFilePath02) {
        this.imgFilePath02 = imgFilePath02;
    }

    /**
     * @return the imgFilePath03
     */
    public String getImgFilePath03() {
        return imgFilePath03;
    }

    /**
     * @param imgFilePath03 the imgFilePath03 to set
     */
    public void setImgFilePath03(String imgFilePath03) {
        this.imgFilePath03 = imgFilePath03;
    }

    /**
     * @return the imgFilePath04
     */
    public String getImgFilePath04() {
        return imgFilePath04;
    }

    /**
     * @param imgFilePath04 the imgFilePath04 to set
     */
    public void setImgFilePath04(String imgFilePath04) {
        this.imgFilePath04 = imgFilePath04;
    }

    /**
     * @return the imgFilePath05
     */
    public String getImgFilePath05() {
        return imgFilePath05;
    }

    /**
     * @param imgFilePath05 the imgFilePath05 to set
     */
    public void setImgFilePath05(String imgFilePath05) {
        this.imgFilePath05 = imgFilePath05;
    }

    /**
     * @return the imgFilePath06
     */
    public String getImgFilePath06() {
        return imgFilePath06;
    }

    /**
     * @param imgFilePath06 the imgFilePath06 to set
     */
    public void setImgFilePath06(String imgFilePath06) {
        this.imgFilePath06 = imgFilePath06;
    }

    /**
     * @return the imgFilePath07
     */
    public String getImgFilePath07() {
        return imgFilePath07;
    }

    /**
     * @param imgFilePath07 the imgFilePath07 to set
     */
    public void setImgFilePath07(String imgFilePath07) {
        this.imgFilePath07 = imgFilePath07;
    }

    /**
     * @return the imgFilePath08
     */
    public String getImgFilePath08() {
        return imgFilePath08;
    }

    /**
     * @param imgFilePath08 the imgFilePath08 to set
     */
    public void setImgFilePath08(String imgFilePath08) {
        this.imgFilePath08 = imgFilePath08;
    }

    /**
     * @return the imgFilePath09
     */
    public String getImgFilePath09() {
        return imgFilePath09;
    }

    /**
     * @param imgFilePath09 the imgFilePath09 to set
     */
    public void setImgFilePath09(String imgFilePath09) {
        this.imgFilePath09 = imgFilePath09;
    }

    /**
     * @return the imgFilePath10
     */
    public String getImgFilePath10() {
        return imgFilePath10;
    }

    /**
     * @param imgFilePath10 the imgFilePath10 to set
     */
    public void setImgFilePath10(String imgFilePath10) {
        this.imgFilePath10 = imgFilePath10;
    }
    
    /**
     * @return the isCircuitBoard
     */
    public int getIsCircuitBoard() {
        return isCircuitBoard;
    }

    /**
     * @param isCircuitBoard the isCircuitBoard to set
     */
    public void setIsCircuitBoard(int isCircuitBoard) {
        this.isCircuitBoard = isCircuitBoard;
    }

    public String getProcessesNotRegistered() {
        return processesNotRegistered;
    }

    public void setProcessesNotRegistered(String processesNotRegistered) {
        this.processesNotRegistered = processesNotRegistered;
    }

    public List<MstComponents> getMstComponentVos() {
        return mstComponentVos;
    }

    public void setMstComponentVos(List<MstComponents> mstComponentVos) {
        this.mstComponentVos = mstComponentVos;
    }

    /**
     * @return the createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the updateDate
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the createUserName
     */
    public String getCreateUserName() {
        return createUserName;
    }

    /**
     * @param createUserName the createUserName to set
     */
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    /**
     * @return the updateUserName
     */
    public String getUpdateUserName() {
        return updateUserName;
    }

    /**
     * @param updateUserName the updateUserName to set
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    /**
     * @return the unitPrice
     */
    public String getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the currencyCode
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * @param currencyCode the currencyCode to set
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * @return the stockUnit
     */
    public int getStockUnit() {
        return stockUnit;
    }

    /**
     * @param stockUnit the stockUnit to set
     */
    public void setStockUnit(int stockUnit) {
        this.stockUnit = stockUnit;
    }

    /**
     * @return the snLength
     */
    public int getSnLength() {
        return snLength;
    }

    /**
     * @param snLength the snLength to set
     */
    public void setSnLength(int snLength) {
        this.snLength = snLength;
    }

    /**
     * @return the snFixedValue
     */
    public String getSnFixedValue() {
        return snFixedValue;
    }

    /**
     * @param snFixedValue the snFixedValue to set
     */
    public void setSnFixedValue(String snFixedValue) {
        this.snFixedValue = snFixedValue;
    }

    /**
     * @return the snFixedLength
     */
    public int getSnFixedLength() {
        return snFixedLength;
    }

    /**
     * @param snFixedLength the snFixedLength to set
     */
    public void setSnFixedLength(int snFixedLength) {
        this.snFixedLength = snFixedLength;
    }

    /**
     * @return the isPurchasedPart
     */
    public int getIsPurchasedPart() {
        return isPurchasedPart;
    }

    /**
     * @param isPurchasedPart the isPurchasedPart to set
     */
    public void setIsPurchasedPart(int isPurchasedPart) {
        this.isPurchasedPart = isPurchasedPart;
    }

}
