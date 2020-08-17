/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.spec;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttribute;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistory;
import java.util.Date;

/**
 *
 * @author admin
 */
public class MstMachineSpecVo extends BasicResponse {

    private String id;

    private String attrId;
    private String attrValue;

    private Date createDate;
    private String createDateStr;

    private Date updateDate;
    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private MstMachineAttribute mstMachineAttribute;
    private String machineAttrbuteCode;
    private String machineAttrbuteName;
    private Integer machineAttrbuteType;

    private MstMachineSpecHistory mstMachineSpecHistory;

    public MstMachineSpecVo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
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

    public MstMachineAttribute getMstMachineAttribute() {
        return mstMachineAttribute;
    }

    public void setMstMachineAttribute(MstMachineAttribute mstMachineAttribute) {
        this.mstMachineAttribute = mstMachineAttribute;
    }

    public MstMachineSpecHistory getMstMachineSpecHistory() {
        return mstMachineSpecHistory;
    }

    public void setMstMachineSpecHistory(MstMachineSpecHistory mstMachineSpecHistory) {
        this.mstMachineSpecHistory = mstMachineSpecHistory;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public String getMachineAttrbuteCode() {
        return machineAttrbuteCode;
    }

    public String getMachineAttrbuteName() {
        return machineAttrbuteName;
    }

    public Integer getMachineAttrbuteType() {
        return machineAttrbuteType;
    }

    public void setMachineAttrbuteCode(String machineAttrbuteCode) {
        this.machineAttrbuteCode = machineAttrbuteCode;
    }

    public void setMachineAttrbuteName(String machineAttrbuteName) {
        this.machineAttrbuteName = machineAttrbuteName;
    }

    public void setMachineAttrbuteType(Integer machineAttrbuteType) {
        this.machineAttrbuteType = machineAttrbuteType;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

}
