/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.production;

import com.kmcj.karte.resources.mold.production.*;
import com.kmcj.karte.BasicResponse;
import java.util.Date;
import java.util.List;

/**
 * 金型期間生産実績テーブル JSON送受信用クラス
 *
 * @author lyd
 */
public class TblMachineProductionPeriodVo extends BasicResponse {

    // 設備マスタ
    private String machineId;    // 設備ID
    private String machineName;  // 設備名称
    private String machineUuid;  // 設備UUID
    private Integer machineType; // 設備種類
    private Integer department; // 所属
    
    // 金型マスタ
    private String moldId;    // 金型ID
    private String moldName;  // 金型名称
    private String moldUuid;  // 金型UUID

    // 部品マスタ
    private String componentId;  // 部品ID
    private String componentCode;  // 部品code
    private String componentName;  // 部品名称
    
    private Date productionDate;
    private long total;
    private String totalHeder;
    
        
    // 期間別・完成数
    private List<TblMachineProductionPeriodDetailVo> tblMachineProductionPeriodDetailVos;

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
     * @return the componentId
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * @param componentId the componentId to set
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
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
     * @return the tblMachineProductionPeriodDetailVos
     */
    public List<TblMachineProductionPeriodDetailVo> getTblMachineProductionPeriodDetailVos() {
        return tblMachineProductionPeriodDetailVos;
    }

    /**
     * @param tblMachineProductionPeriodDetailVos
     */
    public void setTblMachineProductionPeriodDetailVos(List<TblMachineProductionPeriodDetailVo> tblMachineProductionPeriodDetailVos) {
        this.tblMachineProductionPeriodDetailVos = tblMachineProductionPeriodDetailVos;
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
     * @return the moldUuid
     */
    public String getMoldUuid() {
        return moldUuid;
    }

    /**
     * @param moldeUuid the moldUuid to set
     */
    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public long getTotal() {
        return total;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * @return the totalHeder
     */
    public String getTotalHeder() {
        return totalHeder;
    }

    /**
     * @param totalHeder the totalHeder to set
     */
    public void setTotalHeder(String totalHeder) {
        this.totalHeder = totalHeder;
    }
       

}
