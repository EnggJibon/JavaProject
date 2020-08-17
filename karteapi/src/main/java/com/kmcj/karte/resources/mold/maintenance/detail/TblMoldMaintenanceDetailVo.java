/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.mold.inspection.result.TblMoldInspectionResultVo;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMoldMaintenanceDetailVo extends BasicResponse {

    private String maintenanceId;
    private int seq;
    private String id;
    private String moldId;
    private String moldName;
    private String mainteType;
    private String mainteTypeText;
    private String report;
    private String mainteReasonCategory1;
    private String mainteReasonCategory1Text;
    private String mainteReasonCategory2;
    private String mainteReasonCategory2Text;
    private String mainteReasonCategory3;
    private String mainteReasonCategory3Text;
    private String maniteReason;
    private String measureDirectionCategory1;
    private String measureDirectionCategory1Text;
    private String measureDirectionCategory2;
    private String measureDirectionCategory2Text;
    private String measureDirectionCategory3;
    private String measureDirectionCategory3Text;
    private String measureDirection;
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
    private String deleteFlag;
    private List<TblMoldMaintenanceDetail> tblMoldMaintenanceDetails;
    private List<TblMoldInspectionResultVo> moldInspectionResultVos;
    private List<TblMoldMaintenanceDetailImageFileVo> moldMaintenanceDetailImageFileVos;

    public TblMoldMaintenanceDetailVo() {
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

    public String getMainteReasonCategory1() {
        return mainteReasonCategory1;
    }

    public String getMainteReasonCategory1Text() {
        return mainteReasonCategory1Text;
    }

    public String getMainteReasonCategory2() {
        return mainteReasonCategory2;
    }

    public String getMainteReasonCategory2Text() {
        return mainteReasonCategory2Text;
    }

    public String getMainteReasonCategory3() {
        return mainteReasonCategory3;
    }

    public String getMainteReasonCategory3Text() {
        return mainteReasonCategory3Text;
    }

    public String getManiteReason() {
        return maniteReason;
    }

    public String getMeasureDirectionCategory1() {
        return measureDirectionCategory1;
    }

    public String getMeasureDirectionCategory1Text() {
        return measureDirectionCategory1Text;
    }

    public String getMeasureDirectionCategory2() {
        return measureDirectionCategory2;
    }

    public String getMeasureDirectionCategory2Text() {
        return measureDirectionCategory2Text;
    }

    public String getMeasureDirectionCategory3() {
        return measureDirectionCategory3;
    }

    public String getMeasureDirectionCategory3Text() {
        return measureDirectionCategory3Text;
    }

    public String getMeasureDirection() {
        return measureDirection;
    }

    public String getTaskCategory1() {
        return taskCategory1;
    }

    public String getTaskCategory1Text() {
        return taskCategory1Text;
    }

    public String getTaskCategory2() {
        return taskCategory2;
    }

    public String getTaskCategory2Text() {
        return taskCategory2Text;
    }

    public String getTaskCategory3() {
        return taskCategory3;
    }

    public String getTaskCategory3Text() {
        return taskCategory3Text;
    }

    public String getTask() {
        return task;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }



    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setMainteReasonCategory1(String mainteReasonCategory1) {
        this.mainteReasonCategory1 = mainteReasonCategory1;
    }

    public void setMainteReasonCategory1Text(String mainteReasonCategory1Text) {
        this.mainteReasonCategory1Text = mainteReasonCategory1Text;
    }

    public void setMainteReasonCategory2(String mainteReasonCategory2) {
        this.mainteReasonCategory2 = mainteReasonCategory2;
    }

    public void setMainteReasonCategory2Text(String mainteReasonCategory2Text) {
        this.mainteReasonCategory2Text = mainteReasonCategory2Text;
    }

    public void setMainteReasonCategory3(String mainteReasonCategory3) {
        this.mainteReasonCategory3 = mainteReasonCategory3;
    }

    public void setMainteReasonCategory3Text(String mainteReasonCategory3Text) {
        this.mainteReasonCategory3Text = mainteReasonCategory3Text;
    }

    public void setManiteReason(String maniteReason) {
        this.maniteReason = maniteReason;
    }

    public void setMeasureDirectionCategory1(String measureDirectionCategory1) {
        this.measureDirectionCategory1 = measureDirectionCategory1;
    }

    public void setMeasureDirectionCategory1Text(String measureDirectionCategory1Text) {
        this.measureDirectionCategory1Text = measureDirectionCategory1Text;
    }

    public void setMeasureDirectionCategory2(String measureDirectionCategory2) {
        this.measureDirectionCategory2 = measureDirectionCategory2;
    }

    public void setMeasureDirectionCategory2Text(String measureDirectionCategory2Text) {
        this.measureDirectionCategory2Text = measureDirectionCategory2Text;
    }

    public void setMeasureDirectionCategory3(String measureDirectionCategory3) {
        this.measureDirectionCategory3 = measureDirectionCategory3;
    }

    public void setMeasureDirectionCategory3Text(String measureDirectionCategory3Text) {
        this.measureDirectionCategory3Text = measureDirectionCategory3Text;
    }

    public void setMeasureDirection(String measureDirection) {
        this.measureDirection = measureDirection;
    }

    public void setTaskCategory1(String taskCategory1) {
        this.taskCategory1 = taskCategory1;
    }

    public void setTaskCategory1Text(String taskCategory1Text) {
        this.taskCategory1Text = taskCategory1Text;
    }

    public void setTaskCategory2(String taskCategory2) {
        this.taskCategory2 = taskCategory2;
    }

    public void setTaskCategory2Text(String taskCategory2Text) {
        this.taskCategory2Text = taskCategory2Text;
    }

    public void setTaskCategory3(String taskCategory3) {
        this.taskCategory3 = taskCategory3;
    }

    public void setTaskCategory3Text(String taskCategory3Text) {
        this.taskCategory3Text = taskCategory3Text;
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

    public List<TblMoldInspectionResultVo> getMoldInspectionResultVos() {
        return moldInspectionResultVos;
    }

    public void setMoldInspectionResultVos(List<TblMoldInspectionResultVo> moldInspectionResultVos) {
        this.moldInspectionResultVos = moldInspectionResultVos;
    }

    public List<TblMoldMaintenanceDetail> getTblMoldMaintenanceDetails() {
        return tblMoldMaintenanceDetails;
    }

    public void setTblMoldMaintenanceDetails(List<TblMoldMaintenanceDetail> tblMoldMaintenanceDetails) {
        this.tblMoldMaintenanceDetails = tblMoldMaintenanceDetails;
    }


    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public List<TblMoldMaintenanceDetailImageFileVo> getMoldMaintenanceDetailImageFileVos() {
        return moldMaintenanceDetailImageFileVos;
    }

    public void setMoldMaintenanceDetailImageFileVos(List<TblMoldMaintenanceDetailImageFileVo> moldMaintenanceDetailImageFileVos) {
        this.moldMaintenanceDetailImageFileVos = moldMaintenanceDetailImageFileVos;
    }
    
}
