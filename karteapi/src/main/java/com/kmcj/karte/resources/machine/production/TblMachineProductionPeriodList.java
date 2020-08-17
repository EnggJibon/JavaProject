/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.production;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.graphical.GraphicalItemInfo;
import java.util.List;

/**
 * 金型期間生産実績テーブル JSON送受信用クラス
 *
 * @author lyd
 */
public class TblMachineProductionPeriodList extends BasicResponse {
    
    /**
     * CSV出力条件用
     */
    // 設備マスタ
   
    private String machineId;    // 設備ID
    private String machineName;  // 設備名称
    private Integer machineType; // 設備種類
    private Integer department; // 所属
    private String periodFlag; // 期間種類
    // 部品マスタ
    private List<String> moldUuid;
    private List<String> machineUuid;
    private List<String> componentId;
    private String componentCode;
    private String componentName;
    
    // サーバー時刻
    private String productionDateStart;
    private String productionDateEnd;
    
    // グラフ
    private GraphicalItemInfo graphicalItemInfo;
    
    private String headerStr;
    
    private List<String> paramList; //parameter concatenation
    
    
    private List<TblMachineProductionPeriodVo> tblMachineProductionPeriodVos;

    /**
     * @return the tblMachineProductionPeriodVos
     */
    public List<TblMachineProductionPeriodVo> getTblMachineProductionPeriodVos() {
        return tblMachineProductionPeriodVos;
    }

    /**
     * @param tblMachineProductionPeriodVos the tblMachineProductionPeriodVos to set
     */
    public void setTblMachineProductionPeriodVos(List<TblMachineProductionPeriodVo> tblMachineProductionPeriodVos) {
        this.tblMachineProductionPeriodVos = tblMachineProductionPeriodVos;
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
    public Integer getMachineType() {
        return machineType;
    }

    /**
     * @param machineType the machineType to set
     */
    public void setMachineType(Integer machineType) {
        this.machineType = machineType;
    }

    /**
     * @return the department
     */
    public Integer getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(Integer department) {
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
     * @return the componentCode
     */
    public String getComponentCode() {
        return componentCode;
    }

    /**
     * @param componentCode the componentCode to set
     */
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    /**
     * @return the componentName
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param componentName the componentName to set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
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
     * @return the machineUuid
     */
    public List<String> getMachineUuid() {
        return machineUuid;
    }

    /**
     * @param machineUuid the machineUuid to set
     */
    public void setMachineUuid(List<String> machineUuid) {
        this.machineUuid = machineUuid;
    }

    /**
     * @return the componentId
     */
    public List<String> getComponentId() {
        return componentId;
    }

    /**
     * @param componentId the componentId to set
     */
    public void setComponentId(List<String> componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the moldUuid
     */
    public List<String> getMoldUuid() {
        return moldUuid;
    }

    /**
     * @param moldUuid the moldUuid to set
     */
    public void setMoldUuid(List<String> moldUuid) {
        this.moldUuid = moldUuid;
    }

    /**
     * @return the paramList
     */
    public List<String> getParamList() {
        return paramList;
    }

    /**
     * @param paramList the paramList to set
     */
    public void setParamList(List<String> paramList) {
        this.paramList = paramList;
    }

}
