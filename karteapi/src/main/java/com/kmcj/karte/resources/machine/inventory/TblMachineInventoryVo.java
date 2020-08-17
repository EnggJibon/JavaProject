/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.inventory;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponents;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.machine.*;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineInventoryVo extends BasicResponse {

    private String id;

    private Date inventoryDate;
    private String inventoryDateStr;

    private Date inventoryDateSzt;
    private String inventoryDateSztStr;

    private String installedDateStr;

    private String personUuid;

    private String personName;

    private String inventoryResult;

    private String siteConfirmMethod;

    private String machineConfirmMethod;

    private String companyName;

    private String locationName;

    private String instllationSiteName;

    private String remarks;

    private String inputType;

    private Date createDate;
    private String createDateStr;

    private Date updateDate;
    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private MstMachine mstMachine;
    private String machineId;
    private String machineName;
    private String machineCreateDateStr;
    private String machineInstalledDateStr;
    private String machineInspectedDateStr;
    private String machineMainAssetNo;

    private String machineUuid;

    private MstCompany mstCompany;
    private String companyCode;
    private String ownerCompanyId;
    private String ownerCompanyName;

    private String companyId;

    private MstInstallationSite mstInstallationSite;
    private String instllationSiteCode;

    private String instllationSiteId;

    private MstLocation mstLocation;
    private String locationCode;

    private String locationId;

    private String inventoryId;

    private String hisFlag;

    private String status;

    private String departmentChange;

    private String department;
    
    private String departmentText;

    private String barcodeReprint;

    private String assetDamaged;

    private String notInUse;

    private List<MstComponent> mstComponents;
    private List<MstComponents> mstComponentVos;
    private List<TblMachineInventoryVo> tblMachineInventoryVos;
    private Integer externalFlg;
    private String latestInventoryDateStartStr;
    private String latestInventoryDateEndStr;
    private String latestInventoryDate;
    private String latestTblMachineInventory;
    
    private String imgFilePath;
    private String fileType;
    private String takenDate;
    private String takenDateStz;

    public TblMachineInventoryVo() {
    }

    public TblMachineInventoryVo(String machineId, String machineName, String ownerCompanyName, String companyName, String locationName, String instllationSiteName, String latestInventoryDateStartStr, String latestInventoryDateEndStr, String status) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.ownerCompanyName = ownerCompanyName;
        this.companyName = companyName;
        this.locationName = locationName;
        this.instllationSiteName = instllationSiteName;
        this.latestInventoryDateStartStr = latestInventoryDateStartStr;
        this.latestInventoryDateEndStr = latestInventoryDateEndStr;
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInventoryDate(Date inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    public void setInventoryDateSzt(Date inventoryDateSzt) {
        this.inventoryDateSzt = inventoryDateSzt;
    }

    public void setPersonUuid(String personUuid) {
        this.personUuid = personUuid;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setInventoryResult(String inventoryResult) {
        this.inventoryResult = inventoryResult;
    }

    public void setSiteConfirmMethod(String siteConfirmMethod) {
        this.siteConfirmMethod = siteConfirmMethod;
    }

    public void setMachineConfirmMethod(String machineConfirmMethod) {
        this.machineConfirmMethod = machineConfirmMethod;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setInstllationSiteName(String instllationSiteName) {
        this.instllationSiteName = instllationSiteName;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public void setMstCompany(MstCompany mstCompany) {
        this.mstCompany = mstCompany;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setMstInstallationSite(MstInstallationSite mstInstallationSite) {
        this.mstInstallationSite = mstInstallationSite;
    }

    public void setInstllationSiteId(String instllationSiteId) {
        this.instllationSiteId = instllationSiteId;
    }

    public void setMstLocation(MstLocation mstLocation) {
        this.mstLocation = mstLocation;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getId() {
        return id;
    }

    public Date getInventoryDate() {
        return inventoryDate;
    }

    public Date getInventoryDateSzt() {
        return inventoryDateSzt;
    }

    public String getPersonUuid() {
        return personUuid;
    }

    public String getPersonName() {
        return personName;
    }

    public String getInventoryResult() {
        return inventoryResult;
    }

    public String getSiteConfirmMethod() {
        return siteConfirmMethod;
    }

    public String getMachineConfirmMethod() {
        return machineConfirmMethod;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getInstllationSiteName() {
        return instllationSiteName;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getInputType() {
        return inputType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public MstMachine getMstMachine() {
        return mstMachine;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public MstCompany getMstCompany() {
        return mstCompany;
    }

    public String getCompanyId() {
        return companyId;
    }

    public MstInstallationSite getMstInstallationSite() {
        return mstInstallationSite;
    }

    public String getInstllationSiteId() {
        return instllationSiteId;
    }

    public MstLocation getMstLocation() {
        return mstLocation;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getInventoryDateStr() {
        return inventoryDateStr;
    }

    public String getInventoryDateSztStr() {
        return inventoryDateSztStr;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setInventoryDateStr(String inventoryDateStr) {
        this.inventoryDateStr = inventoryDateStr;
    }

    public void setInventoryDateSztStr(String inventoryDateSztStr) {
        this.inventoryDateSztStr = inventoryDateSztStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getHisFlag() {
        return hisFlag;
    }

    public void setHisFlag(String hisFlag) {
        this.hisFlag = hisFlag;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationCode() {
        return locationCode;
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

    public void setMachineCreateDateStr(String machineCreateDateStr) {
        this.machineCreateDateStr = machineCreateDateStr;
    }

    public String getMachineCreateDateStr() {
        return machineCreateDateStr;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public void setMachineInstalledDateStr(String machineInstalledDateStr) {
        this.machineInstalledDateStr = machineInstalledDateStr;
    }

    public void setOwnerCompanyId(String ownerCompanyId) {
        this.ownerCompanyId = ownerCompanyId;
    }

    public void setOwnerCompanyName(String ownerCompanyName) {
        this.ownerCompanyName = ownerCompanyName;
    }

    public String getMachineInstalledDateStr() {
        return machineInstalledDateStr;
    }

    public String getOwnerCompanyId() {
        return ownerCompanyId;
    }

    public String getOwnerCompanyName() {
        return ownerCompanyName;
    }

    public void setMachineInspectedDateStr(String machineInspectedDateStr) {
        this.machineInspectedDateStr = machineInspectedDateStr;
    }

    public String getMachineInspectedDateStr() {
        return machineInspectedDateStr;
    }

    public void setMachineMainAssetNo(String machineMainAssetNo) {
        this.machineMainAssetNo = machineMainAssetNo;
    }

    public String getMachineMainAssetNo() {
        return machineMainAssetNo;
    }

    public void setMstComponents(List<MstComponent> mstComponents) {
        this.mstComponents = mstComponents;
    }

    public List<MstComponent> getMstComponents() {
        return mstComponents;
    }

    public void setTblMachineInventoryVos(List<TblMachineInventoryVo> tblMachineInventoryVos) {
        this.tblMachineInventoryVos = tblMachineInventoryVos;
    }

    public List<TblMachineInventoryVo> getTblMachineInventoryVos() {
        return tblMachineInventoryVos;
    }

    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
    }

    public Integer getExternalFlg() {
        return externalFlg;
    }

    public String getInstalledDateStr() {
        return installedDateStr;
    }

    public void setInstalledDateStr(String installedDateStr) {
        this.installedDateStr = installedDateStr;
    }

    public String getLatestInventoryDateStartStr() {
        return latestInventoryDateStartStr;
    }

    public String getLatestInventoryDateEndStr() {
        return latestInventoryDateEndStr;
    }

    public void setLatestInventoryDateStartStr(String latestInventoryDateStartStr) {
        this.latestInventoryDateStartStr = latestInventoryDateStartStr;
    }

    public void setLatestInventoryDateEndStr(String latestInventoryDateEndStr) {
        this.latestInventoryDateEndStr = latestInventoryDateEndStr;
    }

    public String getLatestInventoryDate() {
        return latestInventoryDate;
    }

    public String getLatestTblMachineInventory() {
        return latestTblMachineInventory;
    }

    public void setLatestInventoryDate(String latestInventoryDate) {
        this.latestInventoryDate = latestInventoryDate;
    }

    public void setLatestTblMachineInventory(String latestTblMachineInventory) {
        this.latestTblMachineInventory = latestTblMachineInventory;
    }

    public List<MstComponents> getMstComponentVos() {
        return mstComponentVos;
    }

    public void setMstComponentVos(List<MstComponents> mstComponentVos) {
        this.mstComponentVos = mstComponentVos;
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

    public String getImgFilePath() {
        return imgFilePath;
    }

    public void setImgFilePath(String imgFilePath) {
        this.imgFilePath = imgFilePath;
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
}
