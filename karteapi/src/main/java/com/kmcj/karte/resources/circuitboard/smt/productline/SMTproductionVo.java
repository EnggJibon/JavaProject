/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.smt.productline;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zf
 */
public class SMTproductionVo {

    // 合計
    // 生産台数
    private long lineMachines;

    //運転時間
    private BigDecimal totalWorkTime = BigDecimal.ZERO;

    //運転準時間
    private BigDecimal totalOperationTime = BigDecimal.ZERO;

    //基板待ち時間ﾛｰﾀﾞｰ
    private BigDecimal totalLoadTime = BigDecimal.ZERO;

    //基板待ち時間ｱﾝﾛｰﾀﾞｰ
    private BigDecimal totalUnloadTime = BigDecimal.ZERO;

    //ﾒﾝﾃﾅﾝｽ時間
    private BigDecimal totalMaintenanceTime = BigDecimal.ZERO;

    //ﾄﾗﾌﾞﾙ停止時間
    private BigDecimal totalTroubleStopTime = BigDecimal.ZERO;

    //部品切れ時間
    private BigDecimal totalComponentClothTime = BigDecimal.ZERO;

    //部品切れ回数
    private BigDecimal totalComponentClothNumber = BigDecimal.ZERO;

    //実装ﾀｸﾄ（装置）
    private BigDecimal totalDiviceTakt = BigDecimal.ZERO;

    //電源時間
    private BigDecimal totalPowerTime = BigDecimal.ZERO;

    //部品切れ時間/回
    private BigDecimal totalComponentClothRate = BigDecimal.ZERO;

    //ﾀｸﾄ（電源/台数）
    private BigDecimal totalPowerRateTakt = BigDecimal.ZERO;

    // 最大
    //運転時間
    private BigDecimal maxWorkTime = BigDecimal.ZERO;

    //運転準時間
    private BigDecimal maxOperationTime = BigDecimal.ZERO;

    //基板待ち時間ﾛｰﾀﾞｰ
    private BigDecimal maxLoadTime = BigDecimal.ZERO;

    //基板待ち時間ｱﾝﾛｰﾀﾞｰ
    private BigDecimal maxUnloadTime = BigDecimal.ZERO;

    //ﾒﾝﾃﾅﾝｽ時間
    private BigDecimal maxMaintenanceTime = BigDecimal.ZERO;

    //ﾄﾗﾌﾞﾙ停止時間
    private BigDecimal maxTroubleStopTime = BigDecimal.ZERO;

    //部品切れ時間
    private BigDecimal maxComponentClothTime = BigDecimal.ZERO;

    //部品切れ回数
    private BigDecimal maxComponentClothNumber = BigDecimal.ZERO;

    //実装ﾀｸﾄ（装置）
    private BigDecimal maxDiviceTakt = BigDecimal.ZERO;

    //電源時間
    private BigDecimal maxPowerTime = BigDecimal.ZERO;

    //部品切れ時間/回
    private BigDecimal maxComponentClothRate = BigDecimal.ZERO;

    //ﾀｸﾄ（電源/台数）
    private BigDecimal maxPowerRateTakt = BigDecimal.ZERO;

    private List<SMTProductionVoDetail> smtProductionVos;

    public long getLineMachines() {
        return lineMachines;
    }

    public void setLineMachines(long lineMachines) {
        this.lineMachines = lineMachines;
    }

    public BigDecimal getTotalWorkTime() {
        return totalWorkTime;
    }

    public void setTotalWorkTime(BigDecimal totalWorkTime) {
        this.totalWorkTime = totalWorkTime;
    }

    public BigDecimal getTotalOperationTime() {
        return totalOperationTime;
    }

    public void setTotalOperationTime(BigDecimal totalOperationTime) {
        this.totalOperationTime = totalOperationTime;
    }

    public BigDecimal getTotalLoadTime() {
        return totalLoadTime;
    }

    public void setTotalLoadTime(BigDecimal totalLoadTime) {
        this.totalLoadTime = totalLoadTime;
    }

    public BigDecimal getTotalUnloadTime() {
        return totalUnloadTime;
    }

    public void setTotalUnloadTime(BigDecimal totalUnloadTime) {
        this.totalUnloadTime = totalUnloadTime;
    }

    public BigDecimal getTotalMaintenanceTime() {
        return totalMaintenanceTime;
    }

    public void setTotalMaintenanceTime(BigDecimal totalMaintenanceTime) {
        this.totalMaintenanceTime = totalMaintenanceTime;
    }

    public BigDecimal getTotalTroubleStopTime() {
        return totalTroubleStopTime;
    }

    public void setTotalTroubleStopTime(BigDecimal totalTroubleStopTime) {
        this.totalTroubleStopTime = totalTroubleStopTime;
    }

    public BigDecimal getTotalComponentClothTime() {
        return totalComponentClothTime;
    }

    public void setTotalComponentClothTime(BigDecimal totalComponentClothTime) {
        this.totalComponentClothTime = totalComponentClothTime;
    }

    public BigDecimal getTotalComponentClothNumber() {
        return totalComponentClothNumber;
    }

    public void setTotalComponentClothNumber(BigDecimal totalComponentClothNumber) {
        this.totalComponentClothNumber = totalComponentClothNumber;
    }

    public BigDecimal getTotalDiviceTakt() {
        return totalDiviceTakt;
    }

    public void setTotalDiviceTakt(BigDecimal totalDiviceTakt) {
        this.totalDiviceTakt = totalDiviceTakt;
    }

    public BigDecimal getTotalPowerTime() {
        return totalPowerTime;
    }

    public void setTotalPowerTime(BigDecimal totalPowerTime) {
        this.totalPowerTime = totalPowerTime;
    }

    public BigDecimal getTotalComponentClothRate() {
        return totalComponentClothRate;
    }

    public void setTotalComponentClothRate(BigDecimal totalComponentClothRate) {
        this.totalComponentClothRate = totalComponentClothRate;
    }

    public BigDecimal getTotalPowerRateTakt() {
        return totalPowerRateTakt;
    }

    public void setTotalPowerRateTakt(BigDecimal totalPowerRateTakt) {
        this.totalPowerRateTakt = totalPowerRateTakt;
    }

    public List<SMTProductionVoDetail> getSmtProductionVos() {
        return smtProductionVos;
    }

    public void setSmtProductionVos(List<SMTProductionVoDetail> smtProductionVos) {
        this.smtProductionVos = smtProductionVos;
    }

    public BigDecimal getMaxWorkTime() {
        return maxWorkTime;
    }

    public void setMaxWorkTime(BigDecimal maxWorkTime) {
        this.maxWorkTime = maxWorkTime;
    }

    public BigDecimal getMaxOperationTime() {
        return maxOperationTime;
    }

    public void setMaxOperationTime(BigDecimal maxOperationTime) {
        this.maxOperationTime = maxOperationTime;
    }

    public BigDecimal getMaxLoadTime() {
        return maxLoadTime;
    }

    public void setMaxLoadTime(BigDecimal maxLoadTime) {
        this.maxLoadTime = maxLoadTime;
    }

    public BigDecimal getMaxUnloadTime() {
        return maxUnloadTime;
    }

    public void setMaxUnloadTime(BigDecimal maxUnloadTime) {
        this.maxUnloadTime = maxUnloadTime;
    }

    public BigDecimal getMaxMaintenanceTime() {
        return maxMaintenanceTime;
    }

    public void setMaxMaintenanceTime(BigDecimal maxMaintenanceTime) {
        this.maxMaintenanceTime = maxMaintenanceTime;
    }

    public BigDecimal getMaxTroubleStopTime() {
        return maxTroubleStopTime;
    }

    public void setMaxTroubleStopTime(BigDecimal maxTroubleStopTime) {
        this.maxTroubleStopTime = maxTroubleStopTime;
    }

    public BigDecimal getMaxComponentClothTime() {
        return maxComponentClothTime;
    }

    public void setMaxComponentClothTime(BigDecimal maxComponentClothTime) {
        this.maxComponentClothTime = maxComponentClothTime;
    }

    public BigDecimal getMaxComponentClothNumber() {
        return maxComponentClothNumber;
    }

    public void setMaxComponentClothNumber(BigDecimal maxComponentClothNumber) {
        this.maxComponentClothNumber = maxComponentClothNumber;
    }

    public BigDecimal getMaxDiviceTakt() {
        return maxDiviceTakt;
    }

    public void setMaxDiviceTakt(BigDecimal maxDiviceTakt) {
        this.maxDiviceTakt = maxDiviceTakt;
    }

    public BigDecimal getMaxPowerTime() {
        return maxPowerTime;
    }

    public void setMaxPowerTime(BigDecimal maxPowerTime) {
        this.maxPowerTime = maxPowerTime;
    }

    public BigDecimal getMaxComponentClothRate() {
        return maxComponentClothRate;
    }

    public void setMaxComponentClothRate(BigDecimal maxComponentClothRate) {
        this.maxComponentClothRate = maxComponentClothRate;
    }

    public BigDecimal getMaxPowerRateTakt() {
        return maxPowerRateTakt;
    }

    public void setMaxPowerRateTakt(BigDecimal maxPowerRateTakt) {
        this.maxPowerRateTakt = maxPowerRateTakt;
    }
}
