/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.inventory;

import java.util.Date;

/**
 *
 * @author admin
 */
public class Machine {

    /**
     * @return the machineId
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machineId to set
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * @return the machineName
     */
    public String getMachineName() {
        return machineName;
    }

    /**
     * @param machineName the machineName to set
     */
    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    /**
     * @return the companyCode
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * @param companyCode the companyCode to set
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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
     * @return the locationCode
     */
    public String getLocationCode() {
        return locationCode;
    }

    /**
     * @param locationCode the locationCode to set
     */
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
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

    /**
     * @return the installationSiteCode
     */
    public String getInstallationSiteCode() {
        return installationSiteCode;
    }

    /**
     * @param installationSiteCode the installationSiteCode to set
     */
    public void setInstallationSiteCode(String installationSiteCode) {
        this.installationSiteCode = installationSiteCode;
    }

    /**
     * @return the installationSiteName
     */
    public String getInstallationSiteName() {
        return installationSiteName;
    }

    /**
     * @param installationSiteName the installationSiteName to set
     */
    public void setInstallationSiteName(String installationSiteName) {
        this.installationSiteName = installationSiteName;
    }

    /**
     * @return the department
     */
    public int getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(int department) {
        this.department = department;
    }

    /**
     * @return the departmentName
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @param departmentName the departmentName to set
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * @return the stocktakeStatus
     */
    public int getStocktakeStatus() {
        return stocktakeStatus;
    }

    /**
     * @param stocktakeStatus the stocktakeStatus to set
     */
    public void setStocktakeStatus(int stocktakeStatus) {
        this.stocktakeStatus = stocktakeStatus;
    }

    /**
     * @return the stocktakeResult
     */
    public int getStocktakeResult() {
        return stocktakeResult;
    }

    /**
     * @param stocktakeResult the stocktakeResult to set
     */
    public void setStocktakeResult(int stocktakeResult) {
        this.stocktakeResult = stocktakeResult;
    }

    /**
     * @return the stocktakeDatetime
     */
    public Date getStocktakeDatetime() {
        return stocktakeDatetime;
    }

    /**
     * @param stocktakeDatetime the stocktakeDatetime to set
     */
    public void setStocktakeDatetime(Date stocktakeDatetime) {
        this.stocktakeDatetime = stocktakeDatetime;
    }

    /**
     * @return the stocktakeMethod
     */
    public int getStocktakeMethod() {
        return stocktakeMethod;
    }

    /**
     * @param stocktakeMethod the stocktakeMethod to set
     */
    public void setStocktakeMethod(int stocktakeMethod) {
        this.stocktakeMethod = stocktakeMethod;
    }

    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the imageFileKey
     */
    public String getImageFileKey() {
        return imageFileKey;
    }

    /**
     * @param imageFileKey the imageFileKey to set
     */
    public void setImageFileKey(String imageFileKey) {
        this.imageFileKey = imageFileKey;
    }

    /**
     * @return the imageTakenDatetime
     */
    public Date getImageTakenDatetime() {
        return imageTakenDatetime;
    }

    /**
     * @param imageTakenDatetime the imageTakenDatetime to set
     */
    public void setImageTakenDatetime(Date imageTakenDatetime) {
        this.imageTakenDatetime = imageTakenDatetime;
    }

    /**
     * @return the changeDepartment
     */
    public int getChangeDepartment() {
        return changeDepartment;
    }

    /**
     * @param changeDepartment the changeDepartment to set
     */
    public void setChangeDepartment(int changeDepartment) {
        this.changeDepartment = changeDepartment;
    }

    /**
     * @return the barcodeReprint
     */
    public int getBarcodeReprint() {
        return barcodeReprint;
    }

    /**
     * @param barcodeReprint the barcodeReprint to set
     */
    public void setBarcodeReprint(int barcodeReprint) {
        this.barcodeReprint = barcodeReprint;
    }

    /**
     * @return the assetDamaged
     */
    public int getAssetDamaged() {
        return assetDamaged;
    }

    /**
     * @param assetDamaged the assetDamaged to set
     */
    public void setAssetDamaged(int assetDamaged) {
        this.assetDamaged = assetDamaged;
    }

    /**
     * @return the notInUse
     */
    public int getNotInUse() {
        return notInUse;
    }

    /**
     * @param notInUse the notInUse to set
     */
    public void setNotInUse(int notInUse) {
        this.notInUse = notInUse;
    }

    /**
     * @return the additionalFlag
     */
    public int getAdditionalFlag() {
        return additionalFlag;
    }

    /**
     * @param addittionalFlag the additionalFlag to set
     */
    public void setAdditionalFlag(int additionalFlag) {
        this.additionalFlag = additionalFlag;
    }

    /**
     * @return the qrPlateInfo
     */
    public String getQrPlateInfo() {
        return qrPlateInfo;
    }

    /**
     * @param qrPlateInfo the qrPlateInfo to set
     */
    public void setQrPlateInfo(String qrPlateInfo) {
        this.qrPlateInfo = qrPlateInfo;
    }

    private String machineId;//設備ID
    private String machineName;//設備名称
    private String companyCode;//会社コード
    private String companyName;//会社名称
    private String locationCode;//所在地コード
    private String locationName;//所在地名称
    private String installationSiteCode;//設置場所コード
    private String installationSiteName;//設置場所名称
    private int department;//所属番号
    private String departmentName;//所属名称
    private int stocktakeStatus;//棚卸ステータス(0:未実施, 1:実施済み)
    private int stocktakeResult;//棚卸結果(0:未実施, 1:所在確認, 2:所在不明)
    private Date stocktakeDatetime;//棚卸日時
    private int stocktakeMethod;//確認方法(0:未実施, 1:手動, 2:QR)
    private String notes;//備考
    private String imageFileKey;//画像ファイルユニークキー *2
    private Date imageTakenDatetime;//画像ファイル撮影日時
    private int changeDepartment;//部署変更(0:無, 1:有)
    private int barcodeReprint;//資産シール再発行要否(0:無, 1:有)
    private int assetDamaged;//故障(0:無, 1:有)
    private int notInUse;//遊休(0:No, 1:Yes)
    private int additionalFlag;//追加フラグ(0:No, Yes)
    private String qrPlateInfo;//QRプレート情報(QRプレートの金型IDより後の文字列)

}
