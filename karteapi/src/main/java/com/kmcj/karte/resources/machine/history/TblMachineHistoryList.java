package com.kmcj.karte.resources.machine.history;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineHistoryList extends BasicResponse {

    private String startDate;
    private String endDate;
    private String department;
    private String machineId;
    
    private List<TblMachineHistory> tblMachineHistorys;

    private List<TblMachineHistoryVo> tblMachineHistoryVos;
    
    private List<TblMachineHistoryDetailVo> tblMachineHistoryDetailVos;

    public TblMachineHistoryList() {
    }

    public void setTblMachineHistorys(List<TblMachineHistory> tblMachineHistorys) {
        this.tblMachineHistorys = tblMachineHistorys;
    }

    public void setTblMachineHistoryVos(List<TblMachineHistoryVo> tblMachineHistoryVos) {
        this.tblMachineHistoryVos = tblMachineHistoryVos;
    }

    public List<TblMachineHistory> getTblMachineHistorys() {
        return tblMachineHistorys;
    }

    public List<TblMachineHistoryVo> getTblMachineHistoryVos() {
        return tblMachineHistoryVos;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
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
     * @return the tblMachineHistoryDetailVos
     */
    public List<TblMachineHistoryDetailVo> getTblMachineHistoryDetailVos() {
        return tblMachineHistoryDetailVos;
    }

    /**
     * @param tblMachineHistoryDetailVos the tblMachineHistoryDetailVos to set
     */
    public void setTblMachineHistoryDetailVos(List<TblMachineHistoryDetailVo> tblMachineHistoryDetailVos) {
        this.tblMachineHistoryDetailVos = tblMachineHistoryDetailVos;
    }
}
