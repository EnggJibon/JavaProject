package com.kmcj.karte.resources.circuitboard.smt.cassette;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiaozhou.wang on 2017/08/08.
 */
public class SMTCassetteVo implements Serializable {

    private static final long serialVersionUID = -1L;

    // 生産ライン
    private String productionLineName;

    // 生産ライン
    private String productionLineNo;

    // 設備ID
    private String machineId;

    // ログID
    private String eventNo;
    
    //ヘッドNo
    private String headno;
    
    //ノズルNo
    private String injectorno;

    // ログ生成日時
    private Date createDate;

    // 設備No
    private String machineNo;

    // 設備名称
    private String machineName;

    // 部品名称
    private String componentName;

    // 吸着回数
    private BigDecimal adhesionNumber;

    // 吸着エラー回数
    private BigDecimal adhesionErrorNumber;

    // 立ち吸着エラー回数
    private BigDecimal leavingAdhesionErrorNumber;

    // 部品認識エラー回数1
    private BigDecimal componentRecognitionErrorNumber1;

    // 部品認識エラー回数2
    private BigDecimal componentRecognitionErrorNumber2;

    // 部品認識エラー回数3
    private BigDecimal componentRecognitionErrorNumber3;

    // エラー合計
    private BigDecimal totalErrors;

    // エラー率
    private BigDecimal errorRate;

    // 吸着エラー率
    private BigDecimal adhesionErrorRate;

    // 立ち吸着エラー率
    private BigDecimal leavingAdhesionErrorRate;

    // 認識エラー1率
    private BigDecimal recognitionErrorRate1;

    // 認識エラー2率
    private BigDecimal recognitionErrorRate2;

    // 認識エラー3率
    private BigDecimal recognitionErrorRate3;

    // 自動機
    private String autoMachine;

    // グラフ
    private String graph;

    public String getProductionLineName() {
        return productionLineName;
    }

    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
    }

    public String getProductionLineNo() {
        return productionLineNo;
    }

    public void setProductionLineNo(String productionLineNo) {
        this.productionLineNo = productionLineNo;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public BigDecimal getAdhesionNumber() {
        return adhesionNumber;
    }

    public void setAdhesionNumber(BigDecimal adhesionNumber) {
        this.adhesionNumber = adhesionNumber;
    }

    public BigDecimal getAdhesionErrorNumber() {
        return adhesionErrorNumber;
    }

    public void setAdhesionErrorNumber(BigDecimal adhesionErrorNumber) {
        this.adhesionErrorNumber = adhesionErrorNumber;
    }

    public BigDecimal getLeavingAdhesionErrorNumber() {
        return leavingAdhesionErrorNumber;
    }

    public void setLeavingAdhesionErrorNumber(BigDecimal leavingAdhesionErrorNumber) {
        this.leavingAdhesionErrorNumber = leavingAdhesionErrorNumber;
    }

    public BigDecimal getComponentRecognitionErrorNumber1() {
        return componentRecognitionErrorNumber1;
    }

    public void setComponentRecognitionErrorNumber1(BigDecimal componentRecognitionErrorNumber1) {
        this.componentRecognitionErrorNumber1 = componentRecognitionErrorNumber1;
    }

    public BigDecimal getComponentRecognitionErrorNumber2() {
        return componentRecognitionErrorNumber2;
    }

    public void setComponentRecognitionErrorNumber2(BigDecimal componentRecognitionErrorNumber2) {
        this.componentRecognitionErrorNumber2 = componentRecognitionErrorNumber2;
    }

    public BigDecimal getComponentRecognitionErrorNumber3() {
        return componentRecognitionErrorNumber3;
    }

    public void setComponentRecognitionErrorNumber3(BigDecimal componentRecognitionErrorNumber3) {
        this.componentRecognitionErrorNumber3 = componentRecognitionErrorNumber3;
    }

    public BigDecimal getTotalErrors() {
        return totalErrors;
    }

    public void setTotalErrors(BigDecimal totalErrors) {
        this.totalErrors = totalErrors;
    }

    public BigDecimal getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(BigDecimal errorRate) {
        this.errorRate = errorRate;
    }

    public BigDecimal getAdhesionErrorRate() {
        return adhesionErrorRate;
    }

    public void setAdhesionErrorRate(BigDecimal adhesionErrorRate) {
        this.adhesionErrorRate = adhesionErrorRate;
    }

    public BigDecimal getLeavingAdhesionErrorRate() {
        return leavingAdhesionErrorRate;
    }

    public void setLeavingAdhesionErrorRate(BigDecimal leavingAdhesionErrorRate) {
        this.leavingAdhesionErrorRate = leavingAdhesionErrorRate;
    }

    public BigDecimal getRecognitionErrorRate1() {
        return recognitionErrorRate1;
    }

    public void setRecognitionErrorRate1(BigDecimal recognitionErrorRate1) {
        this.recognitionErrorRate1 = recognitionErrorRate1;
    }

    public BigDecimal getRecognitionErrorRate2() {
        return recognitionErrorRate2;
    }

    public void setRecognitionErrorRate2(BigDecimal recognitionErrorRate2) {
        this.recognitionErrorRate2 = recognitionErrorRate2;
    }

    public BigDecimal getRecognitionErrorRate3() {
        return recognitionErrorRate3;
    }

    public void setRecognitionErrorRate3(BigDecimal recognitionErrorRate3) {
        this.recognitionErrorRate3 = recognitionErrorRate3;
    }

    public String getAutoMachine() {
        return autoMachine;
    }

    public void setAutoMachine(String autoMachine) {
        this.autoMachine = autoMachine;
    }

    public String getGraph() {
        return graph;
    }

    public void setGraph(String graph) {
        this.graph = graph;
    }
      public String getHeadno() {
        return headno;
    }

    public void setHeadno(String headno) {
        this.headno = headno;
    }

    public String getInjectorno() {
        return injectorno;
    }

    public void setInjectorno(String injectorno) {
        this.injectorno = injectorno;
    }
}
