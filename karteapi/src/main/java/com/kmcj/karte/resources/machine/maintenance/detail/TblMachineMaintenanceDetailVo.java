package com.kmcj.karte.resources.machine.maintenance.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.machine.inspection.result.TblMachineInspectionResultVo;
import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodeling;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineMaintenanceDetailVo extends BasicResponse {

    private TblMachineMaintenanceDetailPK tblMachineMaintenanceDetailPK;

    private String id;
    
    private String maintenanceId;
    
    private int seq;

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

    private Date createDate;
    private String createDateStr;

    private Date updateDate;
    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private TblMachineMaintenanceRemodeling tblMachineMaintenanceRemodeling;
    private String tblMachineMaintenanceRemodelingId;
    
    private String machineId;
    private String machineName;
    
    private String report;
    private String deleteFlag;
    
    private List<TblMachineInspectionResultVo> machineInspectionResultVos;
    private List<TblMachineMaintenanceDetail> tblMachineMaintenanceDetails;
    
    private List<TblMachineMaintenanceDetailImageFileVo> tblMachineMaintenanceDetailImageFileVos;

    public TblMachineMaintenanceDetailVo() {
    }

    public void setTblMachineMaintenanceDetailPK(TblMachineMaintenanceDetailPK tblMachineMaintenanceDetailPK) {
        this.tblMachineMaintenanceDetailPK = tblMachineMaintenanceDetailPK;
    }

    public void setId(String id) {
        this.id = id;
    }

    

    public void setMainteReasonCategory3Text(String mainteReasonCategory3Text) {
        this.mainteReasonCategory3Text = mainteReasonCategory3Text;
    }

    public void setManiteReason(String maniteReason) {
        this.maniteReason = maniteReason;
    }



    public void setMeasureDirectionCategory1Text(String measureDirectionCategory1Text) {
        this.measureDirectionCategory1Text = measureDirectionCategory1Text;
    }



    public void setMeasureDirectionCategory2Text(String measureDirectionCategory2Text) {
        this.measureDirectionCategory2Text = measureDirectionCategory2Text;
    }



    public void setMeasureDirectionCategory3Text(String measureDirectionCategory3Text) {
        this.measureDirectionCategory3Text = measureDirectionCategory3Text;
    }

    public void setMeasureDirection(String measureDirection) {
        this.measureDirection = measureDirection;
    }



    public void setTaskCategory1Text(String taskCategory1Text) {
        this.taskCategory1Text = taskCategory1Text;
    }



    public void setTaskCategory2Text(String taskCategory2Text) {
        this.taskCategory2Text = taskCategory2Text;
    }



    public void setTaskCategory3Text(String taskCategory3Text) {
        this.taskCategory3Text = taskCategory3Text;
    }

    public void setTask(String task) {
        this.task = task;
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

    public void setTblMachineMaintenanceRemodeling(TblMachineMaintenanceRemodeling tblMachineMaintenanceRemodeling) {
        this.tblMachineMaintenanceRemodeling = tblMachineMaintenanceRemodeling;
    }

    public void setTblMachineMaintenanceRemodelingId(String tblMachineMaintenanceRemodelingId) {
        this.tblMachineMaintenanceRemodelingId = tblMachineMaintenanceRemodelingId;
    }

    public TblMachineMaintenanceDetailPK getTblMachineMaintenanceDetailPK() {
        return tblMachineMaintenanceDetailPK;
    }

    public String getId() {
        return id;
    }

    

    public String getMainteReasonCategory1Text() {
        return mainteReasonCategory1Text;
    }



    public String getMainteReasonCategory2Text() {
        return mainteReasonCategory2Text;
    }



    public String getMainteReasonCategory3Text() {
        return mainteReasonCategory3Text;
    }

    public String getManiteReason() {
        return maniteReason;
    }



    public String getMeasureDirectionCategory1Text() {
        return measureDirectionCategory1Text;
    }



    public String getMeasureDirectionCategory2Text() {
        return measureDirectionCategory2Text;
    }


    public String getMeasureDirectionCategory3Text() {
        return measureDirectionCategory3Text;
    }

    public String getMeasureDirection() {
        return measureDirection;
    }


    public String getTaskCategory1Text() {
        return taskCategory1Text;
    }



    public String getTaskCategory2Text() {
        return taskCategory2Text;
    }



    public String getTaskCategory3Text() {
        return taskCategory3Text;
    }

    public String getTask() {
        return task;
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

    public TblMachineMaintenanceRemodeling getTblMachineMaintenanceRemodeling() {
        return tblMachineMaintenanceRemodeling;
    }

    public String getTblMachineMaintenanceRemodelingId() {
        return tblMachineMaintenanceRemodelingId;
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
     * @return the mainteReasonCategory1
     */
    public String getMainteReasonCategory1() {
        return mainteReasonCategory1;
    }

    /**
     * @param mainteReasonCategory1 the mainteReasonCategory1 to set
     */
    public void setMainteReasonCategory1(String mainteReasonCategory1) {
        this.mainteReasonCategory1 = mainteReasonCategory1;
    }

    /**
     * @param mainteReasonCategory1Text the mainteReasonCategory1Text to set
     */
    public void setMainteReasonCategory1Text(String mainteReasonCategory1Text) {
        this.mainteReasonCategory1Text = mainteReasonCategory1Text;
    }

    /**
     * @return the mainteReasonCategory2
     */
    public String getMainteReasonCategory2() {
        return mainteReasonCategory2;
    }

    /**
     * @param mainteReasonCategory2 the mainteReasonCategory2 to set
     */
    public void setMainteReasonCategory2(String mainteReasonCategory2) {
        this.mainteReasonCategory2 = mainteReasonCategory2;
    }

    /**
     * @param mainteReasonCategory2Text the mainteReasonCategory2Text to set
     */
    public void setMainteReasonCategory2Text(String mainteReasonCategory2Text) {
        this.mainteReasonCategory2Text = mainteReasonCategory2Text;
    }

    /**
     * @return the mainteReasonCategory3
     */
    public String getMainteReasonCategory3() {
        return mainteReasonCategory3;
    }

    /**
     * @param mainteReasonCategory3 the mainteReasonCategory3 to set
     */
    public void setMainteReasonCategory3(String mainteReasonCategory3) {
        this.mainteReasonCategory3 = mainteReasonCategory3;
    }

    /**
     * @return the measureDirectionCategory1
     */
    public String getMeasureDirectionCategory1() {
        return measureDirectionCategory1;
    }

    /**
     * @param measureDirectionCategory1 the measureDirectionCategory1 to set
     */
    public void setMeasureDirectionCategory1(String measureDirectionCategory1) {
        this.measureDirectionCategory1 = measureDirectionCategory1;
    }

    /**
     * @return the measureDirectionCategory2
     */
    public String getMeasureDirectionCategory2() {
        return measureDirectionCategory2;
    }

    /**
     * @param measureDirectionCategory2 the measureDirectionCategory2 to set
     */
    public void setMeasureDirectionCategory2(String measureDirectionCategory2) {
        this.measureDirectionCategory2 = measureDirectionCategory2;
    }

    /**
     * @return the measureDirectionCategory3
     */
    public String getMeasureDirectionCategory3() {
        return measureDirectionCategory3;
    }

    /**
     * @param measureDirectionCategory3 the measureDirectionCategory3 to set
     */
    public void setMeasureDirectionCategory3(String measureDirectionCategory3) {
        this.measureDirectionCategory3 = measureDirectionCategory3;
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
     * @return the machineInspectionResultVos
     */
    public List<TblMachineInspectionResultVo> getMachineInspectionResultVos() {
        return machineInspectionResultVos;
    }

    /**
     * @param machineInspectionResultVos the machineInspectionResultVos to set
     */
    public void setMachineInspectionResultVos(List<TblMachineInspectionResultVo> machineInspectionResultVos) {
        this.machineInspectionResultVos = machineInspectionResultVos;
    }

    /**
     * @return the tblMachineMaintenanceDetails
     */
    public List<TblMachineMaintenanceDetail> getTblMachineMaintenanceDetails() {
        return tblMachineMaintenanceDetails;
    }

    /**
     * @param tblMachineMaintenanceDetails the tblMachineMaintenanceDetails to set
     */
    public void setTblMachineMaintenanceDetails(List<TblMachineMaintenanceDetail> tblMachineMaintenanceDetails) {
        this.tblMachineMaintenanceDetails = tblMachineMaintenanceDetails;
    }

    public List<TblMachineMaintenanceDetailImageFileVo> getTblMachineMaintenanceDetailImageFileVos() {
        return tblMachineMaintenanceDetailImageFileVos;
    }

    public void setTblMachineMaintenanceDetailImageFileVos(List<TblMachineMaintenanceDetailImageFileVo> tblMachineMaintenanceDetailImageFileVos) {
        this.tblMachineMaintenanceDetailImageFileVos = tblMachineMaintenanceDetailImageFileVos;
    }

}
