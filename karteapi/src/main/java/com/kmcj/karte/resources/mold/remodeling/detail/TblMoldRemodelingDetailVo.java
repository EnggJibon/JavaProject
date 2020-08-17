/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.remodeling.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttributes;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationVo;
import com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResultVo;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMoldRemodelingDetailVo extends BasicResponse {
    
    private List<MstMoldComponentRelationVo> mstMoldComponentRelationVos;
    private List<MstMoldAttributes> attributes;
    private List<TblMoldRemodelingDetail> tblMoldRemodelingDetails;
    private List<TblMoldRemodelingInspectionResultVo> moldRemodelingInspectionResultVos;
    private List<TblMoldRemodelingDetailImageFileVo> moldRemodelingDetailImageFileVos;
    
    private String moldUuid;
    private String maintenanceId;
    private int seq;
    private String id;
    private String moldId;
    private String moldName;
    private String mainteType;
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
    private String createDate;
    private String updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    private String specName;
    private String specHisId;
    private String deleteFlag;

    public TblMoldRemodelingDetailVo() {
    }

    public String getMaintenanceId() {
        return maintenanceId;
    }

    public int getSeq() {
        return seq;
    }

    public String getId() {
        return id;
    }

    

    public void setTask(String task) {
        this.task = task;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public String getMoldId() {
        return moldId;
    }

    public String getMoldName() {
        return moldName;
    }

    public String getMainteType() {
        return mainteType;
    }

    public String getMainteTypeText() {
        return mainteTypeText;
    }

    public String getReport() {
        return report;
    }

    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public void setMoldName(String moldName) {
        this.moldName = moldName;
    }

    public void setMainteType(String mainteType) {
        this.mainteType = mainteType;
    }

    public void setMainteTypeText(String mainteTypeText) {
        this.mainteTypeText = mainteTypeText;
    }

    public void setReport(String report) {
        this.report = report;
    }

    /**
     * @param maintenanceId the maintenanceId to set
     */
    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(int seq) {
        this.seq = seq;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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
     * @return the taskCategory3Text
     */
    public String getTaskCategory3Text() {
        return taskCategory3Text;
    }

    /**
     * @param taskCategory3Text the taskCategory3Text to set
     */
    public void setTaskCategory3Text(String taskCategory3Text) {
        this.taskCategory3Text = taskCategory3Text;
    }

    /**
     * @return the task
     */
    public String getTask() {
        return task;
    }

    /**
     * @return the createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @return the updateDate
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * @return the createUserUuid
     */
    public String getCreateUserUuid() {
        return createUserUuid;
    }

    /**
     * @return the updateUserUuid
     */
    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public List<MstMoldComponentRelationVo> getMstMoldComponentRelationVos() {
        return mstMoldComponentRelationVos;
    }

    public void setMstMoldComponentRelationVos(List<MstMoldComponentRelationVo> mstMoldComponentRelationVos) {
        this.mstMoldComponentRelationVos = mstMoldComponentRelationVos;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public List<MstMoldAttributes> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<MstMoldAttributes> attributes) {
        this.attributes = attributes;
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

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
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

    public List<TblMoldRemodelingDetail> getTblMoldRemodelingDetails() {
        return tblMoldRemodelingDetails;
    }

    public void setTblMoldRemodelingDetails(List<TblMoldRemodelingDetail> tblMoldRemodelingDetails) {
        this.tblMoldRemodelingDetails = tblMoldRemodelingDetails;
    }

    /**
     * @return the moldRemodelingInspectionResultVo
     */
    public List<TblMoldRemodelingInspectionResultVo> getMoldRemodelingInspectionResultVos() {
        return moldRemodelingInspectionResultVos;
    }

    /**
     * @param moldRemodelingInspectionResultVos the moldRemodelingInspectionResultVo to set
     */
    public void setMoldRemodelingInspectionResultVos(List<TblMoldRemodelingInspectionResultVo> moldRemodelingInspectionResultVos) {
        this.moldRemodelingInspectionResultVos = moldRemodelingInspectionResultVos;
    }

    public List<TblMoldRemodelingDetailImageFileVo> getMoldRemodelingDetailImageFileVos() {
        return moldRemodelingDetailImageFileVos;
    }

    public void setMoldRemodelingDetailImageFileVos(List<TblMoldRemodelingDetailImageFileVo> moldRemodelingDetailImageFileVos) {
        this.moldRemodelingDetailImageFileVos = moldRemodelingDetailImageFileVos;
    }
    
}
