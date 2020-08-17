package com.kmcj.karte.resources.machine.proccond;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.machine.*;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineProcCondVo extends BasicResponse {

    private String id;

//    private String machineProcCondName;
    private String componentId;
    private String componentName;
    private String componentCode;

    private int seq;

    private Date createDate;
    private String createDateStr;

    private Date updateDate;
    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private MstMachine mstMachine;
    private String machineName;
    private String machineId;
    private String attrId;
    private Integer attrType;
    private String attrValue;
    private String machineProcCondAttributeCode;
    private String machineProcCondAttributeName;
    private String machineProcCondId;

    private String machineUuid;

    private List<MstMachineProcCondVo> mstMachineProcCondVos;
    private MstMachineProcCond mstMachineProcCond;

    private String operationFlag;

    public MstMachineProcCondVo() {
    }

    public MstMachineProcCondVo(String id) {
        this.id = id;
    }

    public MstMachineProcCondVo(String id, int seq) {
        this.id = id;
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getMachineProcCondName() {
//        return machineProcCondName;
//    }
//
//    public void setMachineProcCondName(String machineProcCondName) {
//        this.machineProcCondName = machineProcCondName;
//    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
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

    public List<MstMachineProcCondVo> getMstMachineProcCondVos() {
        return mstMachineProcCondVos;
    }

    public void setMstMachineProcCondVos(List<MstMachineProcCondVo> mstMachineProcCondVos) {
        this.mstMachineProcCondVos = mstMachineProcCondVos;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public MstMachineProcCond getMstMachineProcCond() {
        return mstMachineProcCond;
    }

    public void setMstMachineProcCond(MstMachineProcCond mstMachineProcCond) {
        this.mstMachineProcCond = mstMachineProcCond;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getAttrId() {
        return attrId;
    }

    public Integer getAttrType() {
        return attrType;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public void setAttrType(Integer attrType) {
        this.attrType = attrType;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getOperationFlag() {
        return operationFlag;
    }

    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    public String getMachineProcCondAttributeCode() {
        return machineProcCondAttributeCode;
    }

    public String getMachineProcCondAttributeName() {
        return machineProcCondAttributeName;
    }

    public String getMachineProcCondId() {
        return machineProcCondId;
    }

    public void setMachineProcCondAttributeCode(String machineProcCondAttributeCode) {
        this.machineProcCondAttributeCode = machineProcCondAttributeCode;
    }

    public void setMachineProcCondAttributeName(String machineProcCondAttributeName) {
        this.machineProcCondAttributeName = machineProcCondAttributeName;
    }

    public void setMachineProcCondId(String machineProcCondId) {
        this.machineProcCondId = machineProcCondId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentCode() {
        return componentCode;
    }
    
}
