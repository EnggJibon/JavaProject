/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.mgmt.company;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author admin
 */
public class TblInventoryMgmtCompanyVo extends BasicResponse {

    private String inventoryId;
    
    private String mgmtCompanyCode;

    private String companyName;

    private String locationName;

    private String department;
    
    private String telNo;

    private String position;

    private String contactName;

    private String mailAddress;
    
    private int inventoryAssetCount;
    
    private int inventoryMoldAssetCount;
    
    private int inventoryMachineAssetCount;
    
    private Date dueDate;
    
    private Date requestedDate;
    
    private Date receivedDate;
    
    private int installationChangedFlg;
    
    private int actualDiffChangeFlg;
    
    private Date createDate;
    
    private Date updateDate;
    
    private String createUserUuid;
    
    private String updateUserUuid;

    public TblInventoryMgmtCompanyVo() {
    }

    /**
     * @return the inventoryId
     */
    public String getInventoryId() {
        return inventoryId;
    }

    /**
     * @param inventoryId the inventoryId to set
     */
    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
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
     * @return the inventoryAssetCount
     */
    public int getInventoryAssetCount() {
        return inventoryAssetCount;
    }

    /**
     * @param inventoryAssetCount the inventoryAssetCount to set
     */
    public void setInventoryAssetCount(int inventoryAssetCount) {
        this.inventoryAssetCount = inventoryAssetCount;
    }

    /**
     * @return the inventoryMoldAssetCount
     */
    public int getInventoryMoldAssetCount() {
        return inventoryMoldAssetCount;
    }

    /**
     * @param inventoryMoldAssetCount the inventoryMoldAssetCount to set
     */
    public void setInventoryMoldAssetCount(int inventoryMoldAssetCount) {
        this.inventoryMoldAssetCount = inventoryMoldAssetCount;
    }

    /**
     * @return the inventoryMachineAssetCount
     */
    public int getInventoryMachineAssetCount() {
        return inventoryMachineAssetCount;
    }

    /**
     * @param inventoryMachineAssetCount the inventoryMachineAssetCount to set
     */
    public void setInventoryMachineAssetCount(int inventoryMachineAssetCount) {
        this.inventoryMachineAssetCount = inventoryMachineAssetCount;
    }

    /**
     * @return the dueDate
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return the requestedDate
     */
    public Date getRequestedDate() {
        return requestedDate;
    }

    /**
     * @param requestedDate the requestedDate to set
     */
    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    /**
     * @return the receivedDate
     */
    public Date getReceivedDate() {
        return receivedDate;
    }

    /**
     * @param receivedDate the receivedDate to set
     */
    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    /**
     * @return the installationChangedFlg
     */
    public int getInstallationChangedFlg() {
        return installationChangedFlg;
    }

    /**
     * @param installationChangedFlg the installationChangedFlg to set
     */
    public void setInstallationChangedFlg(int installationChangedFlg) {
        this.installationChangedFlg = installationChangedFlg;
    }

    /**
     * @return the actualDiffChangeFlg
     */
    public int getActualDiffChangeFlg() {
        return actualDiffChangeFlg;
    }

    /**
     * @param actualDiffChangeFlg the actualDiffChangeFlg to set
     */
    public void setActualDiffChangeFlg(int actualDiffChangeFlg) {
        this.actualDiffChangeFlg = actualDiffChangeFlg;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
     * @return the createUserUuid
     */
    public String getCreateUserUuid() {
        return createUserUuid;
    }

    /**
     * @param createUserUuid the createUserUuid to set
     */
    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
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

}
