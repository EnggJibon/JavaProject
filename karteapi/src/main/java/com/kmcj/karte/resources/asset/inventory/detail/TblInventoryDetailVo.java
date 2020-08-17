/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.detail;

import com.kmcj.karte.BasicResponse;

/**
 *
 * @author admin
 */
public class TblInventoryDetailVo extends BasicResponse {

    /** 資産番号 */
    private String assetNo;

    /** 補助番号 */
    private String branchNo;

    /** 資産種類 */
    private int assetType;
    
    /** 資産種類文字列 */
    private String assetTypeStr;

    /** 資産名称 */
    private String assetName;

    /** 所在先コード */
    private String mgmtLocationCode;
    
    /** 設置場所 */
    private String mgmtLocationName;
    
    /** 管理先コード */
    private String mgmtCompanyCode;
    
    /** 管理先名称 */
    private String mgmtCompanyName;
    
    /** 品目コード */
    private String itemCode;
    
    /** 品目名称 */
    private String itemName;
    
    /** 取得区分 */
    private int acquisitionType;
    
    /** 取得区分文字列 */
    private String acquisitionTypeStr;
    
    /** 取得年月 */
    private String acquisitionYyyymm;
    
    /** 現品有無 */
    private int existence;
    
    /** 現品有無文字列 */
    private String existenceStr;
    
    /** 無理由 */
    private String noExistenceReason;
    
    /** 変更後所在先コード */
    private String newMgmtLocationCode;
    
    /** 変更後設置場所 */
    private String newMgmtLocationName;
    
    /** 変更後管理先コード */
    private String newMgmtCompanyCode;
    
    /** 変更後管理先名称 */
    private String newMgmtCompanyName;
    
    private int changeLocation;

    private String newLocation;

    private String newLocationAddress;
    
    
    private int newAddedMgmtLocation;

    /**
     * @return the newAddedMgmtLocation
     */
    public int getNewAddedMgmtLocation() {
        return newAddedMgmtLocation;
    }

    /**
     * @param newAddedMgmtLocation the newAddedMgmtLocation to set
     */
    public void setNewAddedMgmtLocation(int newAddedMgmtLocation) {
        this.newAddedMgmtLocation = newAddedMgmtLocation;
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
     * @return the assetTypeStr
     */
    public String getAssetTypeStr() {
        return assetTypeStr;
    }

    /**
     * @param assetTypeStr the assetTypeStr to set
     */
    public void setAssetTypeStr(String assetTypeStr) {
        this.assetTypeStr = assetTypeStr;
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
     * @return the acquisitionTypeStr
     */
    public String getAcquisitionTypeStr() {
        return acquisitionTypeStr;
    }

    /**
     * @param acquisitionTypeStr the acquisitionTypeStr to set
     */
    public void setAcquisitionTypeStr(String acquisitionTypeStr) {
        this.acquisitionTypeStr = acquisitionTypeStr;
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
     * @return the existence
     */
    public int getExistence() {
        return existence;
    }

    /**
     * @param existence the existence to set
     */
    public void setExistence(int existence) {
        this.existence = existence;
    }

    /**
     * @return the existenceStr
     */
    public String getExistenceStr() {
        return existenceStr;
    }

    /**
     * @param existenceStr the existenceStr to set
     */
    public void setExistenceStr(String existenceStr) {
        this.existenceStr = existenceStr;
    }

    /**
     * @return the noExistenceReason
     */
    public String getNoExistenceReason() {
        return noExistenceReason;
    }

    /**
     * @param noExistenceReason the noExistenceReason to set
     */
    public void setNoExistenceReason(String noExistenceReason) {
        this.noExistenceReason = noExistenceReason;
    }

    /**
     * @return the newMgmtLocationCode
     */
    public String getNewMgmtLocationCode() {
        return newMgmtLocationCode;
    }

    /**
     * @param newMgmtLocationCode the newMgmtLocationCode to set
     */
    public void setNewMgmtLocationCode(String newMgmtLocationCode) {
        this.newMgmtLocationCode = newMgmtLocationCode;
    }

    /**
     * @return the newMgmtLocationName
     */
    public String getNewMgmtLocationName() {
        return newMgmtLocationName;
    }

    /**
     * @param newMgmtLocationName the newMgmtLocationName to set
     */
    public void setNewMgmtLocationName(String newMgmtLocationName) {
        this.newMgmtLocationName = newMgmtLocationName;
    }

    /**
     * @return the newMgmtCompanyCode
     */
    public String getNewMgmtCompanyCode() {
        return newMgmtCompanyCode;
    }

    /**
     * @param newMgmtCompanyCode the newMgmtCompanyCode to set
     */
    public void setNewMgmtCompanyCode(String newMgmtCompanyCode) {
        this.newMgmtCompanyCode = newMgmtCompanyCode;
    }

    /**
     * @return the newMgmtCompanyName
     */
    public String getNewMgmtCompanyName() {
        return newMgmtCompanyName;
    }

    /**
     * @param newMgmtCompanyName the newMgmtCompanyName to set
     */
    public void setNewMgmtCompanyName(String newMgmtCompanyName) {
        this.newMgmtCompanyName = newMgmtCompanyName;
    }

    /**
     * @return the changeLocation
     */
    public int getChangeLocation() {
        return changeLocation;
    }

    /**
     * @param changeLocation the changeLocation to set
     */
    public void setChangeLocation(int changeLocation) {
        this.changeLocation = changeLocation;
    }

    /**
     * @return the newLocation
     */
    public String getNewLocation() {
        return newLocation;
    }

    /**
     * @param newLocation the newLocation to set
     */
    public void setNewLocation(String newLocation) {
        this.newLocation = newLocation;
    }

    /**
     * @return the newLocationAddress
     */
    public String getNewLocationAddress() {
        return newLocationAddress;
    }

    /**
     * @param newLocationAddress the newLocationAddress to set
     */
    public void setNewLocationAddress(String newLocationAddress) {
        this.newLocationAddress = newLocationAddress;
    }

    public String getMgmtCompanyCode() {
        return mgmtCompanyCode;
    }

    public void setMgmtCompanyCode(String mgmtCompanyCode) {
        this.mgmtCompanyCode = mgmtCompanyCode;
    }

    public String getMgmtCompanyName() {
        return mgmtCompanyName;
    }

    public void setMgmtCompanyName(String mgmtCompanyName) {
        this.mgmtCompanyName = mgmtCompanyName;
    }
    
}
