/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inventory;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.location.MstLocation;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMoldInventorys extends BasicResponse {

    private String id;
    private String moldUuid;
    private String moldId;
    private String moldName;
    private String mainAssetNo;
    private String companyId;
    private String companyCode;
    private String instllationSiteId;
    private String instllationSiteCode;
    private String locationId;
    private String locationCode;
    private String ownerCompanyId;
    private String companyName;
    private String ownerCompanyName;
    private String locationName;
    private String instllationSiteName;
    private String installedDate;
    private String inventoryDate;
    private String inventoryResult;
    private String latestInventoryDate;
    private String inventoryDateSzt;
    private String latestInventoryDateStart;
    private String latestInventoryDateEnd;
    private String moldConfirmMethod;
    private String siteConfirmMethod;
    private String moldType;
    private String status;
    private String statusName;
    private String moldInspectedDate;
    private String moldInstalledDate;
    private String moldCreateDate;
    private String moldMainAssetNo;
    private String personUuid;
    private String personName;
    private String remarks;
    private String inputType;
    private String hisFlag;
    private Date createDate;
    private String createUserUuid;
    private String updateUserUuid;
    private int externalFlg;

    private MstCompany mstCompany;
    private MstInstallationSite mstInstallationSite;
    private MstLocation mstLocation;

    private String InventoryId;
    private String imgFilePath;
    private String fileType;
    private String takenDate;
    private String takenDateStz;
    
    //5.02 add
    private String departmentChange;
    private String department;
    private String departmentText;
    private String barcodeReprint;
    private String assetDamaged;
    private String notInUse;

//    private MstMold mold;
    private List<TblMoldInventorys> moldInventorys;
    private List<MstComponent> components;

    public TblMoldInventorys() {
    }

    public TblMoldInventorys(String moldId, String moldName, String ownerCompanyName, String companyName, String locationName, String instllationSiteName, String latestInventoryDateStart, String latestInventoryDateEnd, String status) {
        this.moldId = moldId;
        this.moldName = moldName;
        this.ownerCompanyName = ownerCompanyName;
        this.companyName = companyName;
        this.locationName = locationName;
        this.instllationSiteName = instllationSiteName;
        this.latestInventoryDateStart = latestInventoryDateStart;
        this.latestInventoryDateEnd = latestInventoryDateEnd;
        this.status = status;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public String getMoldId() {
        return moldId;
    }

    public String getMoldName() {
        return moldName;
    }

    public String getMainAssetNo() {
        return mainAssetNo;
    }

    public String getOwnerCompanyId() {
        return ownerCompanyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getOwnerCompanyName() {
        return ownerCompanyName;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getInstllationSiteName() {
        return instllationSiteName;
    }

    public String getInventoryDate() {
        return inventoryDate;
    }

    public String getInventoryResult() {
        return inventoryResult;
    }

    public String getLatestInventoryDate() {
        return latestInventoryDate;
    }

    public String getLatestInventoryDateStart() {
        return latestInventoryDateStart;
    }

    public String getLatestInventoryDateEnd() {
        return latestInventoryDateEnd;
    }

    public String getMoldConfirmMethod() {
        return moldConfirmMethod;
    }

    public String getSiteConfirmMethod() {
        return siteConfirmMethod;
    }

    public String getMoldType() {
        return moldType;
    }

    public String getStatus() {
        return status;
    }

    public String getMoldInspectedDate() {
        return moldInspectedDate;
    }

    public String getMoldCreateDate() {
        return moldCreateDate;
    }

    public String getMoldMainAssetNo() {
        return moldMainAssetNo;
    }

    public List<TblMoldInventorys> getMoldInventorys() {
        return moldInventorys;
    }

    public List<MstComponent> getComponents() {
        return components;
    }

    public String getPersonUuid() {
        return personUuid;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getInputType() {
        return inputType;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public void setMoldName(String moldName) {
        this.moldName = moldName;
    }

    public void setMainAssetNo(String mainAssetNo) {
        this.mainAssetNo = mainAssetNo;
    }

    public void setOwnerCompanyId(String ownerCompanyId) {
        this.ownerCompanyId = ownerCompanyId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setOwnerCompanyName(String ownerCompanyName) {
        this.ownerCompanyName = ownerCompanyName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setInstllationSiteName(String instllationSiteName) {
        this.instllationSiteName = instllationSiteName;
    }

    public void setInventoryDate(String inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public void setInventoryResult(String inventoryResult) {
        this.inventoryResult = inventoryResult;
    }

    public void setLatestInventoryDate(String latestInventoryDate) {
        this.latestInventoryDate = latestInventoryDate;
    }

    public String getInventoryDateSzt() {
        return inventoryDateSzt;
    }

    public void setInventoryDateSzt(String inventoryDateSzt) {
        this.inventoryDateSzt = inventoryDateSzt;
    }

    public void setLatestInventoryDateStart(String latestInventoryDateStart) {
        this.latestInventoryDateStart = latestInventoryDateStart;
    }

    public void setLatestInventoryDateEnd(String latestInventoryDateEnd) {
        this.latestInventoryDateEnd = latestInventoryDateEnd;
    }

    public void setMoldConfirmMethod(String moldConfirmMethod) {
        this.moldConfirmMethod = moldConfirmMethod;
    }

    public void setSiteConfirmMethod(String siteConfirmMethod) {
        this.siteConfirmMethod = siteConfirmMethod;
    }

    public void setMoldType(String moldType) {
        this.moldType = moldType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMoldInspectedDate(String moldInspectedDate) {
        this.moldInspectedDate = moldInspectedDate;
    }

    public void setMoldCreateDate(String moldCreateDate) {
        this.moldCreateDate = moldCreateDate;
    }

    public void setMoldMainAssetNo(String moldMainAssetNo) {
        this.moldMainAssetNo = moldMainAssetNo;
    }

    public void setMoldInventorys(List<TblMoldInventorys> moldInventorys) {
        this.moldInventorys = moldInventorys;
    }

    public void setComponents(List<MstComponent> components) {
        this.components = components;
    }

    public void setPersonUuid(String personUuid) {
        this.personUuid = personUuid;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public MstCompany getMstCompany() {
        return mstCompany;
    }

    public void setMstCompany(MstCompany mstCompany) {
        this.mstCompany = mstCompany;
    }

    public MstInstallationSite getMstInstallationSite() {
        return mstInstallationSite;
    }

    public void setMstInstallationSite(MstInstallationSite mstInstallationSite) {
        this.mstInstallationSite = mstInstallationSite;
    }

    public MstLocation getMstLocation() {
        return mstLocation;
    }

    public void setMstLocation(MstLocation mstLocation) {
        this.mstLocation = mstLocation;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }    

    public void setInstllationSiteCode(String instllationSiteCode) {
        this.instllationSiteCode = instllationSiteCode;
    }

    public String getInstllationSiteCode() {
        return instllationSiteCode;
    }

    public String getInstllationSiteId() {
        return instllationSiteId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setInstllationSiteId(String instllationSiteId) {
        this.instllationSiteId = instllationSiteId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the InventoryId
     */
    public String getInventoryId() {
        return InventoryId;
    }

    /**
     * @param InventoryId the InventoryId to set
     */
    public void setInventoryId(String InventoryId) {
        this.InventoryId = InventoryId;
    }

    /**
     * @return the statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * @param statusName the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * @return the moldInstalledDate
     */
    public String getMoldInstalledDate() {
        return moldInstalledDate;
    }

    /**
     * @param moldInstalledDate the moldInstalledDate to set
     */
    public void setMoldInstalledDate(String moldInstalledDate) {
        this.moldInstalledDate = moldInstalledDate;
    }

    /**
     * @return the installedDate
     */
    public String getInstalledDate() {
        return installedDate;
    }

    /**
     * @param installedDate the installedDate to set
     */
    public void setInstalledDate(String installedDate) {
        this.installedDate = installedDate;
    }

    /**
     * @return the hisFlag
     */
    public String getHisFlag() {
        return hisFlag;
    }

    /**
     * @param hisFlag the hisFlag to set
     */
    public void setHisFlag(String hisFlag) {
        this.hisFlag = hisFlag;
    }

    /**
     * @return the externalFlg
     */
    public int getExternalFlg() {
        return externalFlg;
    }

    /**
     * @param externalFlg the externalFlg to set
     */
    public void setExternalFlg(int externalFlg) {
        this.externalFlg = externalFlg;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    /**
     * @return the imgFilePath
     */
    public String getImgFilePath() {
        return imgFilePath;
    }

    /**
     * @param imgFilePath the imgFilePath to set
     */
    public void setImgFilePath(String imgFilePath) {
        this.imgFilePath = imgFilePath;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the takenDate
     */
    public String getTakenDate() {
        return takenDate;
    }

    /**
     * @param takenDate the takenDate to set
     */
    public void setTakenDate(String takenDate) {
        this.takenDate = takenDate;
    }

    /**
     * @return the takenDateStz
     */
    public String getTakenDateStz() {
        return takenDateStz;
    }

    /**
     * @param takenDateStz the takenDateStz to set
     */
    public void setTakenDateStz(String takenDateStz) {
        this.takenDateStz = takenDateStz;
    }

    /**
     * @return the departmentChange
     */
    public String getDepartmentChange() {
        return departmentChange;
    }

    /**
     * @param departmentChange the departmentChange to set
     */
    public void setDepartmentChange(String departmentChange) {
        this.departmentChange = departmentChange;
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
     * @return the departmentText
     */
    public String getDepartmentText() {
        return departmentText;
    }

    /**
     * @param departmentText the departmentText to set
     */
    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    /**
     * @return the barcodeReprint
     */
    public String getBarcodeReprint() {
        return barcodeReprint;
    }

    /**
     * @param barcodeReprint the barcodeReprint to set
     */
    public void setBarcodeReprint(String barcodeReprint) {
        this.barcodeReprint = barcodeReprint;
    }

    /**
     * @return the assetDamaged
     */
    public String getAssetDamaged() {
        return assetDamaged;
    }

    /**
     * @param assetDamaged the assetDamaged to set
     */
    public void setAssetDamaged(String assetDamaged) {
        this.assetDamaged = assetDamaged;
    }

    /**
     * @return the notInUse
     */
    public String getNotInUse() {
        return notInUse;
    }

    /**
     * @param notInUse the notInUse to set
     */
    public void setNotInUse(String notInUse) {
        this.notInUse = notInUse;
    }

    /**
     * @return the latestInventoryId
     */
}
