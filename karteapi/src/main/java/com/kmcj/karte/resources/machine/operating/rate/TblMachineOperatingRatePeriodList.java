/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.operating.rate;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.graphical.GraphicalItemInfo;
import java.util.List;

/**
 *
 * @author apeng
 */
public class TblMachineOperatingRatePeriodList extends BasicResponse {
    private String machineUuid;
    private String machineId;
    private String machineName;
    private String machineType;
    private String department; // 所属
    private String periodFlag; // 期間種類
    private String productionDateStart;// 検索開始日
    private String productionDateEnd; // 検索终了日
    private List<String> machineUuids;//複数UUID
    private List<TblMachineOperatingRatePeriodVos> tblMachineOperatingRatePeriodVoList;
    
    private String headerStr;//production of the header
    
    private GraphicalItemInfo graphicalItemInfo;

    public TblMachineOperatingRatePeriodList() {
    }

    public List<TblMachineOperatingRatePeriodVos> getTblMachineOperatingRatePeriodVoList() {
        return tblMachineOperatingRatePeriodVoList;
    }

    public void setTblMachineOperatingRatePeriodVoList(List<TblMachineOperatingRatePeriodVos> tblMachineOperatingRatePeriodVoList) {
        this.tblMachineOperatingRatePeriodVoList = tblMachineOperatingRatePeriodVoList;
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
     * @return the machineType
     */
    public String getMachineType() {
        return machineType;
    }

    /**
     * @param machineType the machineType to set
     */
    public void setMachineType(String machineType) {
        this.machineType = machineType;
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
     * @return the periodFlag
     */
    public String getPeriodFlag() {
        return periodFlag;
    }

    /**
     * @param periodFlag the periodFlag to set
     */
    public void setPeriodFlag(String periodFlag) {
        this.periodFlag = periodFlag;
    }

    /**
     * @return the productionDateStart
     */
    public String getProductionDateStart() {
        return productionDateStart;
    }

    /**
     * @param productionDateStart the productionDateStart to set
     */
    public void setProductionDateStart(String productionDateStart) {
        this.productionDateStart = productionDateStart;
    }

    /**
     * @return the graphicalItemInfo
     */
    public GraphicalItemInfo getGraphicalItemInfo() {
        return graphicalItemInfo;
    }

    /**
     * @param graphicalItemInfo the graphicalItemInfo to set
     */
    public void setGraphicalItemInfo(GraphicalItemInfo graphicalItemInfo) {
        this.graphicalItemInfo = graphicalItemInfo;
    }

    /**
     * @return the productionDateEnd
     */
    public String getProductionDateEnd() {
        return productionDateEnd;
    }

    /**
     * @param productionDateEnd the productionDateEnd to set
     */
    public void setProductionDateEnd(String productionDateEnd) {
        this.productionDateEnd = productionDateEnd;
    }

    /**
     * @return the headerStr
     */
    public String getHeaderStr() {
        return headerStr;
    }

    /**
     * @param headerStr the headerStr to set
     */
    public void setHeaderStr(String headerStr) {
        this.headerStr = headerStr;
    }

    /**
     * @return the machineUuids
     */
    public List<String> getMachineUuids() {
        return machineUuids;
    }

    /**
     * @param machineUuids the machineUuids to set
     */
    public void setMachineUuids(List<String> machineUuids) {
        this.machineUuids = machineUuids;
    }
    
}
