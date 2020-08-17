/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.inventory;

import java.math.BigDecimal;

/**
 *
 * @author zds
 */
public class InventoryInfoCreateVo {

    private String uuid;

    private String assetNo;//資産番号

    private String branchNo;//補助番号

    private String assetTypeText;//資産種類

    private String assetName;//資産名称

    private String mgmtCompanyCode;//管理先コード

    private String mgmtCompanyName;//管理先名称

    private String mgmtLocationCode;//所在先コード

    private String mgmtLocationName;//所在先名称

    private String vendorCode;//ベンダーコード

    private String itemCode;//品目コード

    private String itemName;//品目名称

    private String acquisitionTypeText;//取得区分

    private String acquisitionDate;//取得日

    private String acquisitionYyyymm;//取得年月

    private BigDecimal acquisitionAmount;//取得金額

    private BigDecimal monthBookValue;//今月簿価

    private BigDecimal periodBookValue;//期初簿価

    private String currencyCode;//通貨コード

    private int decimalPlaces;//小数桁数

    private String moldCount;//型数

    private String purchasingGroup;//購買グループ

    private String commonInformation;//共通情報

    private String assetClass;//資産クラス

    private String usingSection;//使用部門

    private String costCenter;//原価センタ

    private int mgmtRegion;//管理地域

    private int assetMoldCount;//金型数

    private int assetMachineCount;//設備数

    private int assetCount;//資産数

    private String contactName;  //担当者氏名

    private String companyName;  //会社名称

    private String locationName;  //所在地名称

    private String department; //所属

    private String position;//役割

    private String telNo;//電話番号

    private String mailAddress;// メールアドレス

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
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
     * @return the assetTypeText
     */
    public String getAssetTypeText() {
        return assetTypeText;
    }

    /**
     * @param assetTypeText the assetTypeText to set
     */
    public void setAssetTypeText(String assetTypeText) {
        this.assetTypeText = assetTypeText;
    }

    /**
     * @return the assetName
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     * @param assetName the assetName to set
     */
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    /**
     * @return the mgmtCompanyCode
     */
    public String getMgmtCompanyCode() {
        return mgmtCompanyCode;
    }

    /**
     * @param mgmtCompanyCode the mgmtCompanyCode to set
     */
    public void setMgmtCompanyCode(String mgmtCompanyCode) {
        this.mgmtCompanyCode = mgmtCompanyCode;
    }

    /**
     * @return the mgmtCompanyName
     */
    public String getMgmtCompanyName() {
        return mgmtCompanyName;
    }

    /**
     * @param mgmtCompanyName the mgmtCompanyName to set
     */
    public void setMgmtCompanyName(String mgmtCompanyName) {
        this.mgmtCompanyName = mgmtCompanyName;
    }

    /**
     * @return the mgmtLocationCode
     */
    public String getMgmtLocationCode() {
        return mgmtLocationCode;
    }

    /**
     * @param mgmtLocationCode the mgmtLocationCode to set
     */
    public void setMgmtLocationCode(String mgmtLocationCode) {
        this.mgmtLocationCode = mgmtLocationCode;
    }

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
     * @return the vendorCode
     */
    public String getVendorCode() {
        return vendorCode;
    }

    /**
     * @param vendorCode the vendorCode to set
     */
    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
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
     * @return the acquisitionTypeText
     */
    public String getAcquisitionTypeText() {
        return acquisitionTypeText;
    }

    /**
     * @param acquisitionTypeText the acquisitionTypeText to set
     */
    public void setAcquisitionTypeText(String acquisitionTypeText) {
        this.acquisitionTypeText = acquisitionTypeText;
    }

    /**
     * @return the acquisitionDate
     */
    public String getAcquisitionDate() {
        return acquisitionDate;
    }

    /**
     * @param acquisitionDate the acquisitionDate to set
     */
    public void setAcquisitionDate(String acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
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
    public BigDecimal getAcquisitionAmount() {
        return acquisitionAmount;
    }

    /**
     * @param acquisitionAmount the acquisitionAmount to set
     */
    public void setAcquisitionAmount(BigDecimal acquisitionAmount) {
        this.acquisitionAmount = acquisitionAmount;
    }

    /**
     * @return the monthBookValue
     */
    public BigDecimal getMonthBookValue() {
        return monthBookValue;
    }

    /**
     * @param monthBookValue the monthBookValue to set
     */
    public void setMonthBookValue(BigDecimal monthBookValue) {
        this.monthBookValue = monthBookValue;
    }

    /**
     * @return the periodBookValue
     */
    public BigDecimal getPeriodBookValue() {
        return periodBookValue;
    }

    /**
     * @param periodBookValue the periodBookValue to set
     */
    public void setPeriodBookValue(BigDecimal periodBookValue) {
        this.periodBookValue = periodBookValue;
    }

    /**
     * @return the decimalPlaces
     */
    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    /**
     * @param decimalPlaces the decimalPlaces to set
     */
    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
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

    /**
     * @return the assetClass
     */
    public String getAssetClass() {
        return assetClass;
    }

    /**
     * @param assetClass the assetClass to set
     */
    public void setAssetClass(String assetClass) {
        this.assetClass = assetClass;
    }

    /**
     * @return the usingSection
     */
    public String getUsingSection() {
        return usingSection;
    }

    /**
     * @param usingSection the usingSection to set
     */
    public void setUsingSection(String usingSection) {
        this.usingSection = usingSection;
    }

    /**
     * @return the costCenter
     */
    public String getCostCenter() {
        return costCenter;
    }

    /**
     * @param costCenter the costCenter to set
     */
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * @return the mgmtRegion
     */
    public int getMgmtRegion() {
        return mgmtRegion;
    }

    /**
     * @param mgmtRegion the mgmtRegion to set
     */
    public void setMgmtRegion(int mgmtRegion) {
        this.mgmtRegion = mgmtRegion;
    }

    /**
     * @return the assetMoldCount
     */
    public int getAssetMoldCount() {
        return assetMoldCount;
    }

    /**
     * @param assetMoldCount the assetMoldCount to set
     */
    public void setAssetMoldCount(int assetMoldCount) {
        this.assetMoldCount = assetMoldCount;
    }

    /**
     * @return the assetMachineCount
     */
    public int getAssetMachineCount() {
        return assetMachineCount;
    }

    /**
     * @param assetMachineCount the assetMachineCount to set
     */
    public void setAssetMachineCount(int assetMachineCount) {
        this.assetMachineCount = assetMachineCount;
    }

    /**
     * @return the contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName the contactName to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @return the telNo
     */
    public String getTelNo() {
        return telNo;
    }

    /**
     * @param telNo the telNo to set
     */
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    /**
     * @return the mailAddress
     */
    public String getMailAddress() {
        return mailAddress;
    }

    /**
     * @param mailAddress the mailAddress to set
     */
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
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
     * @return the assetCount
     */
    public int getAssetCount() {
        return assetCount;
    }

    /**
     * @param assetCount the assetCount to set
     */
    public void setAssetCount(int assetCount) {
        this.assetCount = assetCount;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

}
