/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset;

import com.kmcj.karte.resources.asset.relation.TblMoldMachineAssetRelationVo;
import com.kmcj.karte.BasicResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author xiaozhou.wang
 */
public class MstAssetVo extends BasicResponse {
    
    private String uuid;

    private String assetNo;//資産番号

    private String branchNo;//補助番号

    private int assetType;//資産種類
    private String assetTypeText;//資産種類

    private String assetName;//資産名称

    private String mgmtCompanyCode;//管理先コード
    private String mgmtCompanyName;//Apeng 20160626 add

    private String mgmtLocationCode;//所在先コード
    private String mgmtLocationName;//Apeng 20160626 add

    private String vendorCode;//ベンダーコード

    private String itemCode;//品目コード
    private String itemName;//Apeng 20160626 add

    private int acquisitionType;//取得区分
    private String acquisitionTypeText;//取得区分

    private String acquisitionDate;//取得日
    private String acquisitionYyyymm;//取得年月
    

    private BigDecimal acquisitionAmount;//取得金額
    private String acquisitionAmountStr;//取得金額

    private BigDecimal monthBookValue;//今月簿価
    private String monthBookValueStr;//今月簿価

    private BigDecimal periodBookValue;//期初簿価Apeng 20160621 add
    private String periodBookValueStr;//期初簿価Apeng 20160621 add
    
    private String currencyCode;//通貨コードApeng 20160621 add
    private String moldCount;//型数Apeng 20160621 update

    private String purchasingGroup;//購買グループ

    private String commonInformation;//共通情報
    
    private String assetClass;//資産クラス
    private String usingSection;//使用部門
    private String costCenter;//原価センタ
    private int mgmtRegion;//管理地域
    private String mgmtRegionStr;//管理地域
    private int moldMachineType;//金型・設備区分
    private String moldMachineTypeText;//金型・設備区分
    private Date updateDate;
    private String updateUserUuid;   
    
    // CSVチェック
    // CSV項目
    private String field;
    // CSV値
    private String value;
    
    private List<TblMoldMachineAssetRelationVo> tblMoldMachineAssetRelationVos;
    
    private String disposalStatusText;
    
    private int disposalStatus;
    
    private int checked;
    
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
     * @return the assetType
     */
    public int getAssetType() {
        return assetType;
    }

    /**
     * @param assetType the assetType to set
     */
    public void setAssetType(int assetType) {
        this.assetType = assetType;
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
     * @return the acquisitionType
     */
    public int getAcquisitionType() {
        return acquisitionType;
    }

    /**
     * @param acquisitionType the acquisitionType to set
     */
    public void setAcquisitionType(int acquisitionType) {
        this.acquisitionType = acquisitionType;
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
     * @return the moldMachineType
     */
    public int getMoldMachineType() {
        return moldMachineType;
    }

    /**
     * @param moldMachineType the moldMachineType to set
     */
    public void setMoldMachineType(int moldMachineType) {
        this.moldMachineType = moldMachineType;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the updateUserUuid
     */
    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    /**
     * @param updateUserUuid the updateUserUuid to set
     */
    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the tblMoldMachineAssetRelationVos
     */
    public List<TblMoldMachineAssetRelationVo> getTblMoldMachineAssetRelationVos() {
        return tblMoldMachineAssetRelationVos;
    }

    /**
     * @param tblMoldMachineAssetRelationVos the tblMoldMachineAssetRelationVos to set
     */
    public void setTblMoldMachineAssetRelationVos(List<TblMoldMachineAssetRelationVo> tblMoldMachineAssetRelationVos) {
        this.tblMoldMachineAssetRelationVos = tblMoldMachineAssetRelationVos;
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
     * @return the moldMachineTypeText
     */
    public String getMoldMachineTypeText() {
        return moldMachineTypeText;
    }

    /**
     * @param moldMachineTypeText the moldMachineTypeText to set
     */
    public void setMoldMachineTypeText(String moldMachineTypeText) {
        this.moldMachineTypeText = moldMachineTypeText;
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
     * @return the acquisitionAmountStr
     */
    public String getAcquisitionAmountStr() {
        return acquisitionAmountStr;
    }

    /**
     * @param acquisitionAmountStr the acquisitionAmountStr to set
     */
    public void setAcquisitionAmountStr(String acquisitionAmountStr) {
        this.acquisitionAmountStr = acquisitionAmountStr;
    }

    /**
     * @return the monthBookValueStr
     */
    public String getMonthBookValueStr() {
        return monthBookValueStr;
    }

    /**
     * @param monthBookValueStr the monthBookValueStr to set
     */
    public void setMonthBookValueStr(String monthBookValueStr) {
        this.monthBookValueStr = monthBookValueStr;
    }

    /**
     * @return the periodBookValueStr
     */
    public String getPeriodBookValueStr() {
        return periodBookValueStr;
    }

    /**
     * @param periodBookValueStr the periodBookValueStr to set
     */
    public void setPeriodBookValueStr(String periodBookValueStr) {
        this.periodBookValueStr = periodBookValueStr;
    }

    /**
     * @return the mgmtRegionStr
     */
    public String getMgmtRegionStr() {
        return mgmtRegionStr;
    }

    /**
     * @param mgmtRegionStr the mgmtRegionStr to set
     */
    public void setMgmtRegionStr(String mgmtRegionStr) {
        this.mgmtRegionStr = mgmtRegionStr;
    }

    /**
     * @return the disposalStatusText
     */
    public String getDisposalStatusText() {
        return disposalStatusText;
    }

    /**
     * @param disposalStatusText the disposalStatusText to set
     */
    public void setDisposalStatusText(String disposalStatusText) {
        this.disposalStatusText = disposalStatusText;
    }

    /**
     * @return the disposalStatus
     */
    public int getDisposalStatus() {
        return disposalStatus;
    }

    /**
     * @param disposalStatus the disposalStatus to set
     */
    public void setDisposalStatus(int disposalStatus) {
        this.disposalStatus = disposalStatus;
    }

    /**
     * @return the checked
     */
    public int getChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(int checked) {
        this.checked = checked;
    }

}