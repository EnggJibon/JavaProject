/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.excelvo;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.excelhandle.annotation.Cell;
import com.kmcj.karte.excelhandle.annotation.Excel;
import com.kmcj.karte.excelhandle.annotation.Select;

/**
 * 資産棚卸列表
 * @author Apeng
 */
@Excel(isNeedSequence = false, createRowWay = "insert")
public class R0001InventoryRequestVo extends BasicResponse {

    @Cell(name = "MGMT_LOCATION_NAME")
    private String mgmtLocationName;//所在先名称(会社名)

    @Cell(name = "ITEM_CODE")
    private String itemCode;//品目コード

    @Cell(name = "ACQUISITION_TYPE")
    private String acquisitionType;//取得区分
    @Select
    @Cell(name = "ACQUISITION_TYPE")
    private String[] acquisitionTypeArray;

    @Cell(name = "ACQUISITION_YYYYMM")
    private String acquisitionYyyymm;//取得年月

    @Cell(name = "ACQUISITION_AMOUNT")
    private String acquisitionAmount;//取得金額

    @Cell(name = "MONTH_BOOK_VALUE")
    private String monthBookValue;//今月簿価

    @Cell(name = "ASSET_TYPE")
    private String assetType;//資産種類
    @Select
    @Cell(name = "ASSET_TYPE")
    private String[] assetTypeArray;

    @Cell(name = "ITEM_NAME")
    private String itemName;//品目名称

    @Cell(name = "ASSET_NO")
    private String assetNo;//資産番号

    @Cell(name = "BRANCH_NO")
    private String branchNo;//補助番号

    @Cell(name = "PURCHASING_GROUP")
    private String purchasingGroup;//購買グループ

    @Cell(name = "MOLD_COUNT")
    private String moldCount;//型数

    @Cell(name = "COMMON_INFORMATION")
    private String commonInformation;//共通情報
    
    public R0001InventoryRequestVo() {}
    
    /**
     * @return the mgmtLocationName
     */
    public String getMgmtLocationName() {
        return mgmtLocationName;
    }

    /**
     * @param mgmtLocationName the mgmtLocationName to set
     */
    public void setMgmtLocationName(String mgmtLocationName) {
        this.mgmtLocationName = mgmtLocationName;
    }

    /**
     * @return the itemCode
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * @param itemCode the itemCode to set
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * @return the acquisitionType
     */
    public String getAcquisitionType() {
        return acquisitionType;
    }

    /**
     * @param acquisitionType the acquisitionType to set
     */
    public void setAcquisitionType(String acquisitionType) {
        this.acquisitionType = acquisitionType;
    }

    /**
     * @return the acquisitionTypeArray
     */
    public String[] getAcquisitionTypeArray() {
        return acquisitionTypeArray;
    }

    /**
     * @param acquisitionTypeArray the acquisitionTypeArray to set
     */
    public void setAcquisitionTypeArray(String[] acquisitionTypeArray) {
        this.acquisitionTypeArray = acquisitionTypeArray;
    }

    /**
     * @return the acquisitionYyyymm
     */
    public String getAcquisitionYyyymm() {
        return acquisitionYyyymm;
    }

    /**
     * @param acquisitionYyyymm the acquisitionYyyymm to set
     */
    public void setAcquisitionYyyymm(String acquisitionYyyymm) {
        this.acquisitionYyyymm = acquisitionYyyymm;
    }

    /**
     * @return the acquisitionAmount
     */
    public String getAcquisitionAmount() {
        return acquisitionAmount;
    }

    /**
     * @param acquisitionAmount the acquisitionAmount to set
     */
    public void setAcquisitionAmount(String acquisitionAmount) {
        this.acquisitionAmount = acquisitionAmount;
    }

    /**
     * @return the monthBookValue
     */
    public String getMonthBookValue() {
        return monthBookValue;
    }

    /**
     * @param monthBookValue the monthBookValue to set
     */
    public void setMonthBookValue(String monthBookValue) {
        this.monthBookValue = monthBookValue;
    }

    /**
     * @return the assetType
     */
    public String getAssetType() {
        return assetType;
    }

    /**
     * @param assetType the assetType to set
     */
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    /**
     * @return the assetTypeArray
     */
    public String[] getAssetTypeArray() {
        return assetTypeArray;
    }

    /**
     * @param assetTypeArray the assetTypeArray to set
     */
    public void setAssetTypeArray(String[] assetTypeArray) {
        this.assetTypeArray = assetTypeArray;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the assetNo
     */
    public String getAssetNo() {
        return assetNo;
    }

    /**
     * @param assetNo the assetNo to set
     */
    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    /**
     * @return the branchNo
     */
    public String getBranchNo() {
        return branchNo;
    }

    /**
     * @param branchNo the branchNo to set
     */
    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    /**
     * @return the purchasingGroup
     */
    public String getPurchasingGroup() {
        return purchasingGroup;
    }

    /**
     * @param purchasingGroup the purchasingGroup to set
     */
    public void setPurchasingGroup(String purchasingGroup) {
        this.purchasingGroup = purchasingGroup;
    }

    /**
     * @return the moldCount
     */
    public String getMoldCount() {
        return moldCount;
    }

    /**
     * @param moldCount the moldCount to set
     */
    public void setMoldCount(String moldCount) {
        this.moldCount = moldCount;
    }

    /**
     * @return the commonInformation
     */
    public String getCommonInformation() {
        return commonInformation;
    }

    /**
     * @param commonInformation the commonInformation to set
     */
    public void setCommonInformation(String commonInformation) {
        this.commonInformation = commonInformation;
    }
    
}
