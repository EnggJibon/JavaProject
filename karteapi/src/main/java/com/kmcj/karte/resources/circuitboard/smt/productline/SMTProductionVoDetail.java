package com.kmcj.karte.resources.circuitboard.smt.productline;

import java.math.BigDecimal;

/**
 * Created by xiaozhou.wang on 2017/08/17.
 */
public class SMTProductionVoDetail {

    private static final long serialVersionUID = -1L;

    private String machineName;

    //運転時間
    private BigDecimal workTime;

    //運転準時間
    private BigDecimal operationTime;

    //基板待ち時間ﾛｰﾀﾞｰ
    private BigDecimal loadTime;

    //基板待ち時間ｱﾝﾛｰﾀﾞｰ
    private BigDecimal unloadTime;

    //ﾒﾝﾃﾅﾝｽ時間
    private BigDecimal maintenanceTime;

    //ﾄﾗﾌﾞﾙ停止時間
    private BigDecimal troubleStopTime;

    //部品切れ時間
    private BigDecimal componentClothTime;

    //部品切れ回数
    private BigDecimal componentClothNumber;

    //実装ﾀｸﾄ（装置）
    private BigDecimal diviceTakt;

    //電源時間
    private BigDecimal powerTime;

    //部品切れ時間/回
    private BigDecimal componentClothRate;

    //ﾀｸﾄ（電源/台数）
    private BigDecimal powerRateTakt;

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public BigDecimal getWorkTime() {
        return workTime;
    }

    public void setWorkTime(BigDecimal workTime) {
        this.workTime = workTime;
    }

    public BigDecimal getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(BigDecimal operationTime) {
        this.operationTime = operationTime;
    }

    public BigDecimal getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(BigDecimal loadTime) {
        this.loadTime = loadTime;
    }

    public BigDecimal getUnloadTime() {
        return unloadTime;
    }

    public void setUnloadTime(BigDecimal unloadTime) {
        this.unloadTime = unloadTime;
    }

    public BigDecimal getMaintenanceTime() {
        return maintenanceTime;
    }

    public void setMaintenanceTime(BigDecimal maintenanceTime) {
        this.maintenanceTime = maintenanceTime;
    }

    public BigDecimal getTroubleStopTime() {
        return troubleStopTime;
    }

    public void setTroubleStopTime(BigDecimal troubleStopTime) {
        this.troubleStopTime = troubleStopTime;
    }

    public BigDecimal getComponentClothTime() {
        return componentClothTime;
    }

    public void setComponentClothTime(BigDecimal componentClothTime) {
        this.componentClothTime = componentClothTime;
    }

    public BigDecimal getComponentClothNumber() {
        return componentClothNumber;
    }

    public void setComponentClothNumber(BigDecimal componentClothNumber) {
        this.componentClothNumber = componentClothNumber;
    }

    public BigDecimal getDiviceTakt() {
        return diviceTakt;
    }

    public void setDiviceTakt(BigDecimal diviceTakt) {
        this.diviceTakt = diviceTakt;
    }

    public BigDecimal getPowerTime() {
        return powerTime;
    }

    public void setPowerTime(BigDecimal powerTime) {
        this.powerTime = powerTime;
    }

    public BigDecimal getComponentClothRate() {
        return componentClothRate;
    }

    public void setComponentClothRate(BigDecimal componentClothRate) {
        this.componentClothRate = componentClothRate;
    }

    public BigDecimal getPowerRateTakt() {
        return powerRateTakt;
    }

    public void setPowerRateTakt(BigDecimal powerRateTakt) {
        this.powerRateTakt = powerRateTakt;
    }

}
