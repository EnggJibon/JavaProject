/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.remodeling.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.machine.attribute.MstMachineAttributeVo;
import com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResultVo;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineRemodelingDetailVo extends BasicResponse {

    private TblMachineRemodelingDetailPK tblMachineRemodelingDetailPK;

    private String id;
    
    private String maintenanceId;
    
    private String moldId;
    private String moldName;
    private String mainteType;
    
    private int seq;
    
    private String machineId;
    private String machineUuid;    
    private String machineName;
    
    private String mainteTypeText;
    private String remodelingType;
    private String remodelingTypeText;
    private String report;
    
    private String remodelReasonCategory1;

    private String remodelReasonCategory1Text;

    private String remodelReasonCategory2;

    private String remodelReasonCategory2Text;

    private String remodelReasonCategory3;

    private String remodelReasonCategory3Text;

    private String remodelReason;

    private String remodelDirectionCategory1;

    private String remodelDirectionCategory1Text;

    private String remodelDirectionCategory2;

    private String remodelDirectionCategory2Text;

    private String remodelDirectionCategory3;

    private String remodelDirectionCategory3Text;

    private String remodelDirection;

    private String taskCategory1;

    private String taskCategory1Text;

    private String taskCategory2;

    private String taskCategory2Text;

    private String taskCategory3;

    private String taskCategory3Text;

    private String task;

    private Date createDate;
    private String createDateStr;

    private Date updateDate;
    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;
    
    private String specName;
    private String specHisId;
    private String deleteFlag;

    private List<TblMachineRemodelingDetail> tblMachineRemodelingDetails;
    private List<TblMachineRemodelingInspectionResultVo> machineRemodelingInspectionResultVo;
    private List<TblMachineRemodelingDetailImageFileVo> machineRemodelingDetailImageFileVos;
    private List<MstMachineAttributeVo> attributes;

    public TblMachineRemodelingDetailVo() {
    }

    public TblMachineRemodelingDetailVo(TblMachineRemodelingDetailPK tblMachineRemodelingDetailPK) {
        this.tblMachineRemodelingDetailPK = tblMachineRemodelingDetailPK;
    }

    public TblMachineRemodelingDetailVo(String maintenanceId, int seq) {
        this.tblMachineRemodelingDetailPK = new TblMachineRemodelingDetailPK(maintenanceId, seq);
    }

    public TblMachineRemodelingDetailPK getTblMachineRemodelingDetailPK() {
        return tblMachineRemodelingDetailPK;
    }

    public void setTblMachineRemodelingDetailPK(TblMachineRemodelingDetailPK tblMachineRemodelingDetailPK) {
        this.tblMachineRemodelingDetailPK = tblMachineRemodelingDetailPK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    

    public String getTaskCategory3Text() {
        return taskCategory3Text;
    }

    public void setTaskCategory3Text(String taskCategory3Text) {
        this.taskCategory3Text = taskCategory3Text;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
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

    /**
     * @return the maintenanceId
     */
    public String getMaintenanceId() {
        return maintenanceId;
    }

    /**
     * @param maintenanceId the maintenanceId to set
     */
    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    /**
     * @return the seq
     */
    public int getSeq() {
        return seq;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(int seq) {
        this.seq = seq;
    }

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
     * @return the mainteTypeText
     */
    public String getMainteTypeText() {
        return mainteTypeText;
    }

    /**
     * @param mainteTypeText the mainteTypeText to set
     */
    public void setMainteTypeText(String mainteTypeText) {
        this.mainteTypeText = mainteTypeText;
    }

    /**
     * @return the remodelingType
     */
    public String getRemodelingType() {
        return remodelingType;
    }

    /**
     * @param remodelingType the remodelingType to set
     */
    public void setRemodelingType(String remodelingType) {
        this.remodelingType = remodelingType;
    }

    /**
     * @return the remodelingTypeText
     */
    public String getRemodelingTypeText() {
        return remodelingTypeText;
    }

    /**
     * @param remodelingTypeText the remodelingTypeText to set
     */
    public void setRemodelingTypeText(String remodelingTypeText) {
        this.remodelingTypeText = remodelingTypeText;
    }

    /**
     * @return the report
     */
    public String getReport() {
        return report;
    }

    /**
     * @param report the report to set
     */
    public void setReport(String report) {
        this.report = report;
    }

    /**
     * @return the specName
     */
    public String getSpecName() {
        return specName;
    }

    /**
     * @param specName the specName to set
     */
    public void setSpecName(String specName) {
        this.specName = specName;
    }

    /**
     * @return the specHisId
     */
    public String getSpecHisId() {
        return specHisId;
    }

    /**
     * @param specHisId the specHisId to set
     */
    public void setSpecHisId(String specHisId) {
        this.specHisId = specHisId;
    }

    /**
     * @return the deleteFlag
     */
    public String getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * @param deleteFlag the deleteFlag to set
     */
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * @return the remodelReasonCategory1
     */
    public String getRemodelReasonCategory1() {
        return remodelReasonCategory1;
    }

    /**
     * @param remodelReasonCategory1 the remodelReasonCategory1 to set
     */
    public void setRemodelReasonCategory1(String remodelReasonCategory1) {
        this.remodelReasonCategory1 = remodelReasonCategory1;
    }

    /**
     * @return the remodelReasonCategory1Text
     */
    public String getRemodelReasonCategory1Text() {
        return remodelReasonCategory1Text;
    }

    /**
     * @param remodelReasonCategory1Text the remodelReasonCategory1Text to set
     */
    public void setRemodelReasonCategory1Text(String remodelReasonCategory1Text) {
        this.remodelReasonCategory1Text = remodelReasonCategory1Text;
    }

    /**
     * @return the remodelReasonCategory2
     */
    public String getRemodelReasonCategory2() {
        return remodelReasonCategory2;
    }

    /**
     * @param remodelReasonCategory2 the remodelReasonCategory2 to set
     */
    public void setRemodelReasonCategory2(String remodelReasonCategory2) {
        this.remodelReasonCategory2 = remodelReasonCategory2;
    }

    /**
     * @return the remodelReasonCategory2Text
     */
    public String getRemodelReasonCategory2Text() {
        return remodelReasonCategory2Text;
    }

    /**
     * @param remodelReasonCategory2Text the remodelReasonCategory2Text to set
     */
    public void setRemodelReasonCategory2Text(String remodelReasonCategory2Text) {
        this.remodelReasonCategory2Text = remodelReasonCategory2Text;
    }

    /**
     * @return the remodelReasonCategory3
     */
    public String getRemodelReasonCategory3() {
        return remodelReasonCategory3;
    }

    /**
     * @param remodelReasonCategory3 the remodelReasonCategory3 to set
     */
    public void setRemodelReasonCategory3(String remodelReasonCategory3) {
        this.remodelReasonCategory3 = remodelReasonCategory3;
    }

    /**
     * @return the remodelReasonCategory3Text
     */
    public String getRemodelReasonCategory3Text() {
        return remodelReasonCategory3Text;
    }

    /**
     * @param remodelReasonCategory3Text the remodelReasonCategory3Text to set
     */
    public void setRemodelReasonCategory3Text(String remodelReasonCategory3Text) {
        this.remodelReasonCategory3Text = remodelReasonCategory3Text;
    }

    /**
     * @return the remodelReason
     */
    public String getRemodelReason() {
        return remodelReason;
    }

    /**
     * @param remodelReason the remodelReason to set
     */
    public void setRemodelReason(String remodelReason) {
        this.remodelReason = remodelReason;
    }

    /**
     * @return the remodelDirectionCategory1
     */
    public String getRemodelDirectionCategory1() {
        return remodelDirectionCategory1;
    }

    /**
     * @param remodelDirectionCategory1 the remodelDirectionCategory1 to set
     */
    public void setRemodelDirectionCategory1(String remodelDirectionCategory1) {
        this.remodelDirectionCategory1 = remodelDirectionCategory1;
    }

    /**
     * @return the remodelDirectionCategory1Text
     */
    public String getRemodelDirectionCategory1Text() {
        return remodelDirectionCategory1Text;
    }

    /**
     * @param remodelDirectionCategory1Text the remodelDirectionCategory1Text to set
     */
    public void setRemodelDirectionCategory1Text(String remodelDirectionCategory1Text) {
        this.remodelDirectionCategory1Text = remodelDirectionCategory1Text;
    }

    /**
     * @return the remodelDirectionCategory2
     */
    public String getRemodelDirectionCategory2() {
        return remodelDirectionCategory2;
    }

    /**
     * @param remodelDirectionCategory2 the remodelDirectionCategory2 to set
     */
    public void setRemodelDirectionCategory2(String remodelDirectionCategory2) {
        this.remodelDirectionCategory2 = remodelDirectionCategory2;
    }

    /**
     * @return the remodelDirectionCategory2Text
     */
    public String getRemodelDirectionCategory2Text() {
        return remodelDirectionCategory2Text;
    }

    /**
     * @param remodelDirectionCategory2Text the remodelDirectionCategory2Text to set
     */
    public void setRemodelDirectionCategory2Text(String remodelDirectionCategory2Text) {
        this.remodelDirectionCategory2Text = remodelDirectionCategory2Text;
    }

    /**
     * @return the remodelDirectionCategory3
     */
    public String getRemodelDirectionCategory3() {
        return remodelDirectionCategory3;
    }

    /**
     * @param remodelDirectionCategory3 the remodelDirectionCategory3 to set
     */
    public void setRemodelDirectionCategory3(String remodelDirectionCategory3) {
        this.remodelDirectionCategory3 = remodelDirectionCategory3;
    }

    /**
     * @return the remodelDirectionCategory3Text
     */
    public String getRemodelDirectionCategory3Text() {
        return remodelDirectionCategory3Text;
    }

    /**
     * @param remodelDirectionCategory3Text the remodelDirectionCategory3Text to set
     */
    public void setRemodelDirectionCategory3Text(String remodelDirectionCategory3Text) {
        this.remodelDirectionCategory3Text = remodelDirectionCategory3Text;
    }

    /**
     * @return the remodelDirection
     */
    public String getRemodelDirection() {
        return remodelDirection;
    }

    /**
     * @param remodelDirection the remodelDirection to set
     */
    public void setRemodelDirection(String remodelDirection) {
        this.remodelDirection = remodelDirection;
    }

    /**
     * @return the taskCategory1
     */
    public String getTaskCategory1() {
        return taskCategory1;
    }

    /**
     * @param taskCategory1 the taskCategory1 to set
     */
    public void setTaskCategory1(String taskCategory1) {
        this.taskCategory1 = taskCategory1;
    }

    /**
     * @return the taskCategory1Text
     */
    public String getTaskCategory1Text() {
        return taskCategory1Text;
    }

    /**
     * @param taskCategory1Text the taskCategory1Text to set
     */
    public void setTaskCategory1Text(String taskCategory1Text) {
        this.taskCategory1Text = taskCategory1Text;
    }

    /**
     * @return the taskCategory2
     */
    public String getTaskCategory2() {
        return taskCategory2;
    }

    /**
     * @param taskCategory2 the taskCategory2 to set
     */
    public void setTaskCategory2(String taskCategory2) {
        this.taskCategory2 = taskCategory2;
    }

    /**
     * @return the taskCategory2Text
     */
    public String getTaskCategory2Text() {
        return taskCategory2Text;
    }

    /**
     * @param taskCategory2Text the taskCategory2Text to set
     */
    public void setTaskCategory2Text(String taskCategory2Text) {
        this.taskCategory2Text = taskCategory2Text;
    }

    /**
     * @return the taskCategory3
     */
    public String getTaskCategory3() {
        return taskCategory3;
    }

    /**
     * @param taskCategory3 the taskCategory3 to set
     */
    public void setTaskCategory3(String taskCategory3) {
        this.taskCategory3 = taskCategory3;
    }

    /**
     * @return the moldId
     */
    public String getMoldId() {
        return moldId;
    }

    /**
     * @param moldId the moldId to set
     */
    public void setMoldId(String moldId) {
        this.moldId = moldId;
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
     * @return the mainteType
     */
    public String getMainteType() {
        return mainteType;
    }

    /**
     * @param mainteType the mainteType to set
     */
    public void setMainteType(String mainteType) {
        this.mainteType = mainteType;
    }

    /**
     * @return the machineUuid
     */
    public String getMachineUuid() {
        return machineUuid;
    }

    /**
     * @param machineUuid the machineUuid to set
     */
    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    /**
     * @return the attributes
     */
    public List<MstMachineAttributeVo> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(List<MstMachineAttributeVo> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the tblMachineRemodelingDetails
     */
    public List<TblMachineRemodelingDetail> getTblMachineRemodelingDetails() {
        return tblMachineRemodelingDetails;
    }

    /**
     * @param tblMachineRemodelingDetails the tblMachineRemodelingDetails to set
     */
    public void setTblMachineRemodelingDetails(List<TblMachineRemodelingDetail> tblMachineRemodelingDetails) {
        this.tblMachineRemodelingDetails = tblMachineRemodelingDetails;
    }

    /**
     * @return the machineRemodelingInspectionResultVo
     */
    public List<TblMachineRemodelingInspectionResultVo> getMachineRemodelingInspectionResultVo() {
        return machineRemodelingInspectionResultVo;
    }

    /**
     * @param machineRemodelingInspectionResultVo the machineRemodelingInspectionResultVo to set
     */
    public void setMachineRemodelingInspectionResultVo(List<TblMachineRemodelingInspectionResultVo> machineRemodelingInspectionResultVo) {
        this.machineRemodelingInspectionResultVo = machineRemodelingInspectionResultVo;
    }

    public void setMachineRemodelingDetailImageFileVos(List<TblMachineRemodelingDetailImageFileVo> machineRemodelingDetailImageFileVos) {
        this.machineRemodelingDetailImageFileVos = machineRemodelingDetailImageFileVos;
    }

    public List<TblMachineRemodelingDetailImageFileVo> getMachineRemodelingDetailImageFileVos() {
        return machineRemodelingDetailImageFileVos;
    }

}
