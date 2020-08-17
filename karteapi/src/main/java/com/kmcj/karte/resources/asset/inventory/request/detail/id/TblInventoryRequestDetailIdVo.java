/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.request.detail.id;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author admin
 */

public class TblInventoryRequestDetailIdVo extends BasicResponse {

    private String uuid;
    
    private String requestDetailId;

    private String moldId;
    
    private String moldName;

    private String machineId;
    
    private String machineName;

    private int mainFlg;

    private int existence;

    private Date createDate;

    private Date updateDate;

    private String createUserUuid;

    private String updateUserUuid;
    
    private String inventoryResultValue;//棚卸结果

    public TblInventoryRequestDetailIdVo() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRequestDetailId() {
        return requestDetailId;
    }

    public void setRequestDetailId(String requestDetailId) {
        this.requestDetailId = requestDetailId;
    }

    public String getMoldId() {
        return moldId;
    }

    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public int getMainFlg() {
        return mainFlg;
    }

    public void setMainFlg(int mainFlg) {
        this.mainFlg = mainFlg;
    }

    public int getExistence() {
        return existence;
    }

    public void setExistence(int existence) {
        this.existence = existence;
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
     * @return the moldName
     */
    public String getMoldName() {
        return moldName;
    }

    /**
     * @param moldName the moldName to set
     */
    public void setMoldName(String moldName) {
        this.moldName = moldName;
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
     * @return the inventoryResultValue
     */
    public String getInventoryResultValue() {
        return inventoryResultValue;
    }

    /**
     * @param inventoryResultValue the inventoryResultValue to set
     */
    public void setInventoryResultValue(String inventoryResultValue) {
        this.inventoryResultValue = inventoryResultValue;
    }

}
