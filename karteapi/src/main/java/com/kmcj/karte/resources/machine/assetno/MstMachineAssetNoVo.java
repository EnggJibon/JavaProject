/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.assetno;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.machine.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author admin
 */
public class MstMachineAssetNoVo extends BasicResponse {

    private String id;

    private String uuid;

    private String assetNo;

    private int mainFlg;

    private Date numberedDate;
    private String numberedDateStr;

    private BigDecimal assetAmount;
    private String assetAmountStr;

    private Date createDate;
    private String createDateStr;

    private Date updateDate;
    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private MstMachine mstMachine;
    private String machineId;
    private String machineUuid;

    private String operationFlag;
    
    public MstMachineAssetNoVo() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMainFlg(int mainFlg) {
        this.mainFlg = mainFlg;
    }

    public void setNumberedDate(Date numberedDate) {
        this.numberedDate = numberedDate;
    }

    public void setAssetAmount(BigDecimal assetAmount) {
        this.assetAmount = assetAmount;
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

    public String getId() {
        return id;
    }

    public int getMainFlg() {
        return mainFlg;
    }

    public Date getNumberedDate() {
        return numberedDate;
    }

    public BigDecimal getAssetAmount() {
        return assetAmount;
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

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public String getMachineId() {
        return machineId;
    }

    public String getAssetAmountStr() {
        return assetAmountStr;
    }

    public void setAssetAmountStr(String assetAmountStr) {
        this.assetAmountStr = assetAmountStr;
    }

    public String getOperationFlag() {
        return operationFlag;
    }

    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    public String getNumberedDateStr() {
        return numberedDateStr;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setNumberedDateStr(String numberedDateStr) {
        this.numberedDateStr = numberedDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

}
