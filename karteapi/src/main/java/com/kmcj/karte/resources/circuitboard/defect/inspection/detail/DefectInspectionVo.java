/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.defect.inspection.detail;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author admin
 */
public class DefectInspectionVo extends BasicResponse {

    private String productCode; // 製品コード
    private String productName; // 機種名
    private String productionLineNo; //ラインNo
    private String productionLineName; //ライン名称
    private String automaticMachineType; //自動機タイプ
    private String automaticMachineTypeName; //自動機タイプ名称
    private String circuitBoardCode; //基板コード
    private String circuitBoardName; //基板名称
    private String serialNumber; //シリアルナンバー
    private String surface; //面
    private String componentName; //部品名
    private String inspectionNgCode; //検査機判定NGコード
    private String operationResult; //オペレーター判定結果
    private String inspectionDate; //検査日
    private String inspectionDateStr;
    private int checkNumber; //検査数
    private int aSideBadNumber; //A面不良数
    private int bSideBadNumber; //B面不良数

    /**
     * @return the productCode
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the productionLineNo
     */
    public String getProductionLineNo() {
        return productionLineNo;
    }

    /**
     * @param productionLineNo the productionLineNo to set
     */
    public void setProductionLineNo(String productionLineNo) {
        this.productionLineNo = productionLineNo;
    }

    /**
     * @return the productionLineName
     */
    public String getProductionLineName() {
        return productionLineName;
    }

    /**
     * @param productionLineName the productionLineName to set
     */
    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
    }

    /**
     * @return the automaticMachineType
     */
    public String getAutomaticMachineType() {
        return automaticMachineType;
    }

    /**
     * @param automaticMachineType the automaticMachineType to set
     */
    public void setAutomaticMachineType(String automaticMachineType) {
        this.automaticMachineType = automaticMachineType;
    }

    /**
     * @return the automaticMachineTypeName
     */
    public String getAutomaticMachineTypeName() {
        return automaticMachineTypeName;
    }

    /**
     * @param automaticMachineTypeName the automaticMachineTypeName to set
     */
    public void setAutomaticMachineTypeName(String automaticMachineTypeName) {
        this.automaticMachineTypeName = automaticMachineTypeName;
    }

    /**
     * @return the circuitBoardCode
     */
    public String getCircuitBoardCode() {
        return circuitBoardCode;
    }

    /**
     * @param circuitBoardCode the circuitBoardCode to set
     */
    public void setCircuitBoardCode(String circuitBoardCode) {
        this.circuitBoardCode = circuitBoardCode;
    }

    /**
     * @return the circuitBoardName
     */
    public String getCircuitBoardName() {
        return circuitBoardName;
    }

    /**
     * @param circuitBoardName the circuitBoardName to set
     */
    public void setCircuitBoardName(String circuitBoardName) {
        this.circuitBoardName = circuitBoardName;
    }

    /**
     * @return the serialNumber
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber the serialNumber to set
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @return the surface
     */
    public String getSurface() {
        return surface;
    }

    /**
     * @param surface the surface to set
     */
    public void setSurface(String surface) {
        this.surface = surface;
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
     * @return the inspectionNgCode
     */
    public String getInspectionNgCode() {
        return inspectionNgCode;
    }

    /**
     * @param inspectionNgCode the inspectionNgCode to set
     */
    public void setInspectionNgCode(String inspectionNgCode) {
        this.inspectionNgCode = inspectionNgCode;
    }

    /**
     * @return the operationResult
     */
    public String getOperationResult() {
        return operationResult;
    }

    /**
     * @param operationResult the operationResult to set
     */
    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }

    /**
     * @return the inspectionDate
     */
    public String getInspectionDate() {
        return inspectionDate;
    }

    /**
     * @param inspectionDate the inspectionDate to set
     */
    public void setInspectionDate(String inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    /**
     * @return the inspectionDateStr
     */
    public String getInspectionDateStr() {
        return inspectionDateStr;
    }

    /**
     * @param inspectionDateStr the inspectionDateStr to set
     */
    public void setInspectionDateStr(String inspectionDateStr) {
        this.inspectionDateStr = inspectionDateStr;
    }

    /**
     * @return the checkNumber
     */
    public int getCheckNumber() {
        return checkNumber;
    }

    /**
     * @param checkNumber the checkNumber to set
     */
    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }

    /**
     * @return the aSideBadNumber
     */
    public int getaSideBadNumber() {
        return aSideBadNumber;
    }

    /**
     * @param aSideBadNumber the aSideBadNumber to set
     */
    public void setaSideBadNumber(int aSideBadNumber) {
        this.aSideBadNumber = aSideBadNumber;
    }

    /**
     * @return the bSideBadNumber
     */
    public int getbSideBadNumber() {
        return bSideBadNumber;
    }

    /**
     * @param bSideBadNumber the bSideBadNumber to set
     */
    public void setbSideBadNumber(int bSideBadNumber) {
        this.bSideBadNumber = bSideBadNumber;
    }
}
