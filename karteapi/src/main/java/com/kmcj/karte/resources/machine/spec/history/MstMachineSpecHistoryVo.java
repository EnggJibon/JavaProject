/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.spec.history;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.machine.*;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineSpecHistoryVo extends BasicResponse {

    private String id;

    private Date startDate;
    private String startDateStr;

    private Date endDate;
    private Long endDateHs;
    private String endDateStr;

    private String machineSpecName;

    private Date createDate;
    private String createDateStr;

    private Date updateDate;
    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private MstMachine mstMachine;
    private String machineId;
    private String machineName;

    private String machineUuid;

    private MstMachineSpecHistory mstMachineSpecHistory;
    private String machineSpecHstId;
    private List<MstMachineSpecHistoryVo> mstMachineSpecHistoryVos;

    private String attrId;
    private String attrCode;
    private String attrName;
    private String attrValue;
    private Integer attrType;
    
    private String instllationSiteName;

    /**
     * @return the instllationSiteName
     */
    public String getInstllationSiteName() {
        return instllationSiteName;
    }

    /**
     * @param instllationSiteName the instllationSiteName to set
     */
    public void setInstllationSiteName(String instllationSiteName) {
        this.instllationSiteName = instllationSiteName;
    }

    public MstMachineSpecHistoryVo() {
    }

    public MstMachineSpecHistoryVo(String id) {
        this.id = id;
    }

    public MstMachineSpecHistoryVo(String id, Date startDate, Date endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getMachineSpecName() {
        return machineSpecName;
    }

    public void setMachineSpecName(String machineSpecName) {
        this.machineSpecName = machineSpecName;
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

    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public MstMachine getMstMachine() {
        return mstMachine;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public MstMachineSpecHistory getMstMachineSpecHistory() {
        return mstMachineSpecHistory;
    }

    public void setMstMachineSpecHistory(MstMachineSpecHistory mstMachineSpecHistory) {
        this.mstMachineSpecHistory = mstMachineSpecHistory;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineId() {
        return machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public Long getEndDateHs() {
        return endDateHs;
    }

    public void setEndDateHs(Long endDateHs) {
        this.endDateHs = endDateHs;
    }

    public String getAttrId() {
        return attrId;
    }

    public String getAttrCode() {
        return attrCode;
    }

    public String getAttrName() {
        return attrName;
    }

    public Integer getAttrType() {
        return attrType;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public void setAttrType(Integer attrType) {
        this.attrType = attrType;
    }

    public List<MstMachineSpecHistoryVo> getMstMachineSpecHistoryVos() {
        return mstMachineSpecHistoryVos;
    }

    public void setMstMachineSpecHistoryVos(List<MstMachineSpecHistoryVo> mstMachineSpecHistoryVos) {
        this.mstMachineSpecHistoryVos = mstMachineSpecHistoryVos;
    }

    public String getMachineSpecHstId() {
        return machineSpecHstId;
    }

    public void setMachineSpecHstId(String machineSpecHstId) {
        this.machineSpecHstId = machineSpecHstId;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

}
