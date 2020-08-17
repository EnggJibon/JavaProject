package com.kmcj.karte.resources.machine.history;

import com.kmcj.karte.BasicResponse;
import java.math.BigDecimal;

/**
 *
 * @author admin
 */
public class TblMachineHistoryVo extends BasicResponse {

    private String machineId; //設備ID
    private String machineName; //設備名称
    private String machineUuid; //設備名称
    private String macKey; //連携コード
    private String power; //状態
    private String firstEventNo;
    private String workTime; //生産時間HH:MM
    private long workTimeSec; //生産時間(s)
    private String calcMachineReport; //稼働率
    private String stopCnt; //停止回数
    private String startDate;
    private String workPfm; //設備生産効率
    private String opeTime; //実稼働時間
    private String lastEventNo;
    private String endDate;
    private String syncDate; //最終同期時刻
    private BigDecimal baseCycleTime; //基準サイクル時間
    private String status;
    private int failureCnt;

    private String shotCnt; //ショット数

    private String lastEventDate;
    
    private int department;
    
    private TblMachineHistory tblMachineHistory;

    public TblMachineHistoryVo() {
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public void setFirstEventNo(String firstEventNo) {
        this.firstEventNo = firstEventNo;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setLastEventNo(String lastEventNo) {
        this.lastEventNo = lastEventNo;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setShotCnt(String shotCnt) {
        this.shotCnt = shotCnt;
    }

    public void setLastEventDate(String lastEventDate) {
        this.lastEventDate = lastEventDate;
    }

    public String getMachineName() {
        return machineName;
    }

    public String getFirstEventNo() {
        return firstEventNo;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getLastEventNo() {
        return lastEventNo;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public String getShotCnt() {
        return shotCnt;
    }

    public String getLastEventDate() {
        return lastEventDate;
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
     * @return the macKey
     */
    public String getMacKey() {
        return macKey;
    }

    /**
     * @param macKey the macKey to set
     */
    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }


    /**
     * @return the workTime
     */
    public String getWorkTime() {
        return workTime;
    }

    /**
     * @param workTime the workTime to set
     */
    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    /**
     * @return the calcMachineReport
     */
    public String getCalcMachineReport() {
        return calcMachineReport;
    }

    /**
     * @param calcMachineReport the calcMachineReport to set
     */
    public void setCalcMachineReport(String calcMachineReport) {
        this.calcMachineReport = calcMachineReport;
    }

    /**
     * @return the stopCnt
     */
    public String getStopCnt() {
        return stopCnt;
    }

    /**
     * @param stopCnt the stopCnt to set
     */
    public void setStopCnt(String stopCnt) {
        this.stopCnt = stopCnt;
    }

    /**
     * @return the workPfm
     */
    public String getWorkPfm() {
        return workPfm;
    }

    /**
     * @param workPfm the workPfm to set
     */
    public void setWorkPfm(String workPfm) {
        this.workPfm = workPfm;
    }

    /**
     * @return the opeTime
     */
    public String getOpeTime() {
        return opeTime;
    }

    /**
     * @param opeTime the opeTime to set
     */
    public void setOpeTime(String opeTime) {
        this.opeTime = opeTime;
    }

    /**
     * @return the syncDate
     */
    public String getSyncDate() {
        return syncDate;
    }

    /**
     * @param syncDate the syncDate to set
     */
    public void setSyncDate(String syncDate) {
        this.syncDate = syncDate;
    }

    /**
     * @return the power
     */
    public String getPower() {
        return power;
    }

    /**
     * @param power the power to set
     */
    public void setPower(String power) {
        this.power = power;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    /**
     * @return the baseCycleTime
     */
    public BigDecimal getBaseCycleTime() {
        return baseCycleTime;
    }

    /**
     * @param baseCycleTime the baseCycleTime to set
     */
    public void setBaseCycleTime(BigDecimal baseCycleTime) {
        this.baseCycleTime = baseCycleTime;
    }

    /**
     * @return the failureCnt
     */
    public int getFailureCnt() {
        return failureCnt;
    }

    /**
     * @param failureCnt the failureCnt to set
     */
    public void setFailureCnt(int failureCnt) {
        this.failureCnt = failureCnt;
    }

    /**
     * @return the department
     */
    public int getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(int department) {
        this.department = department;
    }
    
    /**
     * @return the workTimeSec
     */
    public long getWorkTimeSec() {
        return workTimeSec;
    }

    /**
     * @param workTimeSec the workTimeSec to set
     */
    public void setWorkTimeSec(long workTimeSec) {
        this.workTimeSec = workTimeSec;
    }

    public TblMachineHistory getTblMachineHistory() {
        return tblMachineHistory;
    }

    public void setTblMachineHistory(TblMachineHistory tblMachineHistory) {
        this.tblMachineHistory = tblMachineHistory;
    }

}
