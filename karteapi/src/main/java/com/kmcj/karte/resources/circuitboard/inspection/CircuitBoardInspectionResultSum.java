/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import com.kmcj.karte.BasicResponse;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author h.ishihara
 */
public class CircuitBoardInspectionResultSum extends BasicResponse {
    private String circuitBoardInspectionResultSumId;
    private String inspectorId;
    private String componentId;
    private String procedureId;
    private Date inspectionDate;
    private Integer inspectedItemNum;
    private Integer defectiveItemNum;
    private Integer repairedItemNum;
    private Integer discardedItemNum;
    private String repairerId;
    private Integer recheckedItemNum;

    private String inspectionDateStr;
    private String inspectorName;
    private String componentName;
    private String operators;
    private Integer defectivePartNum;
    private BigDecimal ppmDefectiveItemNum;
    private BigDecimal ppmDefectivePartNum;
    private String defectiveItemDetail;
    

    /**
     * @return the circuitBoardInspectionResultSumId
     */
    public String getCircuitBoardInspectionResultSumId() {
        return circuitBoardInspectionResultSumId;
    }

    /**
     * @param circuitBoardInspectionResultSumId the circuitBoardInspectionResultSumId to set
     */
    public void setCircuitBoardInspectionResultSumId(String circuitBoardInspectionResultSumId) {
        this.circuitBoardInspectionResultSumId = circuitBoardInspectionResultSumId;
    }

    /**
     * @return the inspectorId
     */
    public String getInspectorId() {
        return inspectorId;
    }

    /**
     * @param inspectorId the inspectorId to set
     */
    public void setInspectorId(String inspectorId) {
        this.inspectorId = inspectorId;
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
     * @return the procedureId
     */
    public String getProcedureId() {
        return procedureId;
    }

    /**
     * @param procedureId the procedureId to set
     */
    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    /**
     * @return the inspectionDate
     */
    public Date getInspectionDate() {
        return inspectionDate;
    }

    /**
     * @param inspectionDate the inspectionDate to set
     */
    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    /**
     * @return the inspectedItemNum
     */
    public Integer getInspectedItemNum() {
        return inspectedItemNum;
    }

    /**
     * @param inspectedItemNum the inspectedItemNum to set
     */
    public void setInspectedItemNum(Integer inspectedItemNum) {
        this.inspectedItemNum = inspectedItemNum;
    }

    /**
     * @return the defectiveItemNum
     */
    public Integer getDefectiveItemNum() {
        return defectiveItemNum;
    }

    /**
     * @param defectiveItemNum the defectiveItemNum to set
     */
    public void setDefectiveItemNum(Integer defectiveItemNum) {
        this.defectiveItemNum = defectiveItemNum;
    }

    /**
     * @return the repairedItemNum
     */
    public Integer getRepairedItemNum() {
        return repairedItemNum;
    }

    /**
     * @param repairedItemNum the repairedItemNum to set
     */
    public void setRepairedItemNum(Integer repairedItemNum) {
        this.repairedItemNum = repairedItemNum;
    }

    /**
     * @return the discardedItemNum
     */
    public Integer getDiscardedItemNum() {
        return discardedItemNum;
    }

    /**
     * @param discardedItemNum the discardedItemNum to set
     */
    public void setDiscardedItemNum(Integer discardedItemNum) {
        this.discardedItemNum = discardedItemNum;
    }

    /**
     * @return the repairerId
     */
    public String getRepairerId() {
        return repairerId;
    }

    /**
     * @param repairerId the repairerId to set
     */
    public void setRepairerId(String repairerId) {
        this.repairerId = repairerId;
    }

    /**
     * @return the recheckedItemNum
     */
    public Integer getRecheckedItemNum() {
        return recheckedItemNum;
    }

    /**
     * @param recheckedItemNum the recheckedItemNum to set
     */
    public void setRecheckedItemNum(Integer recheckedItemNum) {
        this.recheckedItemNum = recheckedItemNum;
    }

    /**
     * @return the inspectorName
     */
    public String getInspectorName() {
        return inspectorName;
    }

    /**
     * @param inspectorName the inspectorName to set
     */
    public void setInspectorName(String inspectorName) {
        this.inspectorName = inspectorName;
    }

    /**
     * @return the operators
     */
    public String getOperators() {
        return operators;
    }

    /**
     * @param operators the operators to set
     */
    public void setOperators(String operators) {
        this.operators = operators;
    }

    /**
     * @return the defectivPartNum
     */
    public Integer getDefectivePartNum() {
        return defectivePartNum;
    }

    /**
     * @param defectivPartNum the defectivPartNum to set
     */
    public void setDefectivePartNum(Integer defectivPartNum) {
        this.defectivePartNum = defectivPartNum;
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
     * @return the ppmDefectiveItemNum
     */
    public BigDecimal getPpmDefectiveItemNum() {
        return ppmDefectiveItemNum;
    }

    /**
     * @param ppmDefectiveItemNum the ppmDefectiveItemNum to set
     */
    public void setPpmDefectiveItemNum(BigDecimal ppmDefectiveItemNum) {
        this.ppmDefectiveItemNum = ppmDefectiveItemNum;
    }

    /**
     * @return the ppmDefectivePartNum
     */
    public BigDecimal getPpmDefectivePartNum() {
        return ppmDefectivePartNum;
    }

    /**
     * @param ppmDefectivePartNum the ppmDefectivePartNum to set
     */
    public void setPpmDefectivePartNum(BigDecimal ppmDefectivePartNum) {
        this.ppmDefectivePartNum = ppmDefectivePartNum;
    }

    /**
     * @return the defectiveItemDetail
     */
    public String getDefectiveItemDetail() {
        return defectiveItemDetail;
    }

    /**
     * @param defectiveItemDetail the defectiveItemDetail to set
     */
    public void setDefectiveItemDetail(String defectiveItemDetail) {
        this.defectiveItemDetail = defectiveItemDetail;
    }

}
