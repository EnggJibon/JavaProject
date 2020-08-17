/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.request.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.asset.inventory.request.detail.id.TblInventoryRequestDetailIdVo;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblInventoryRequestDetailVo extends BasicResponse {

    private String uuid;

    private String inventoryRequestId;

    private String assetNo;

    private String branchNo;

    private String assetType;//資産種類

    private String assetName;//資産名称

    private String installationSite;

    private String itemCode;

    private String itemName;

    private String mainMoldId; //代表金型ID

    private String mainMachineId; //代表設備ID

    private int moldMachineType;

    private int existence;
    
    private String existenceStr;

    private String noExistenceReason;

    private Date createDate;

    private Date updateDate;

    private String createUserUuid;

    private String updateUserUuid;

    private List<TblInventoryRequestDetailIdVo> tblInventoryRequestDetailIdVos;
    
    private String inventoryId;
    
    private Date sendResponseDate;

    private int changeLocation;

    private String newLocation;

    private String newLocationAddress;

    

    /**
     * 1=有 2=無
     */
    private int linkflag;

    public TblInventoryRequestDetailVo() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getInventoryRequestId() {
        return inventoryRequestId;
    }

    public void setInventoryRequestId(String inventoryRequestId) {
        this.inventoryRequestId = inventoryRequestId;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public String getInstallationSite() {
        return installationSite;
    }

    public void setInstallationSite(String installationSite) {
        this.installationSite = installationSite;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getMoldMachineType() {
        return moldMachineType;
    }

    public void setMoldMachineType(int moldMachineType) {
        this.moldMachineType = moldMachineType;
    }

    public int getExistence() {
        return existence;
    }

    public void setExistence(int existence) {
        this.existence = existence;
    }

    public String getNoExistenceReason() {
        return noExistenceReason;
    }

    public void setNoExistenceReason(String noExistenceReason) {
        this.noExistenceReason = noExistenceReason;
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
     * @return the mainMoldId
     */
    public String getMainMoldId() {
        return mainMoldId;
    }

    /**
     * @param mainMoldId the mainMoldId to set
     */
    public void setMainMoldId(String mainMoldId) {
        this.mainMoldId = mainMoldId;
    }

    /**
     * @return the mainMachineId
     */
    public String getMainMachineId() {
        return mainMachineId;
    }

    /**
     * @param mainMachineId the mainMachineId to set
     */
    public void setMainMachineId(String mainMachineId) {
        this.mainMachineId = mainMachineId;
    }

    /**
     * @return the tblInventoryRequestDetailIdVos
     */
    public List<TblInventoryRequestDetailIdVo> getTblInventoryRequestDetailIdVos() {
        return tblInventoryRequestDetailIdVos;
    }

    /**
     * @param tblInventoryRequestDetailIdVos the tblInventoryRequestDetailIdVos
     * to set
     */
    public void setTblInventoryRequestDetailIdVos(List<TblInventoryRequestDetailIdVo> tblInventoryRequestDetailIdVos) {
        this.tblInventoryRequestDetailIdVos = tblInventoryRequestDetailIdVos;
    }

    /**
     * @return the linkflag
     */
    public int getLinkflag() {
        return linkflag;
    }

    /**
     * @param linkflag the linkflag to set
     */
    public void setLinkflag(int linkflag) {
        this.linkflag = linkflag;
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
     * @return the sendResponseDate
     */
    public Date getSendResponseDate() {
        return sendResponseDate;
    }

    /**
     * @param sendResponseDate the sendResponseDate to set
     */
    public void setSendResponseDate(Date sendResponseDate) {
        this.sendResponseDate = sendResponseDate;
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

}
