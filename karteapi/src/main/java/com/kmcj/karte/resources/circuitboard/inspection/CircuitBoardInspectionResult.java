/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import com.kmcj.karte.resources.circuitboard.inspection.detail.CircuitBoardInspectionResultDetail;
import com.kmcj.karte.BasicResponse;
import java.util.Date;
import java.util.List;

/**
 *
 * @author h.ishihara
 */
public class CircuitBoardInspectionResult extends BasicResponse {
    private String circuitBoardInspectionResultId;
    private String inspectorId;
    private String componentId;
    private String procedureId;
    private Date inspectionDate;
    private int inspectionResult;
    private String serialNumber;

    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    
    private CircuitBoardInspectionResultSum resultSum;
    private List<CircuitBoardInspectionResultDetail> resultDetailList;
    private  List<CircuitBoardWorkAssignment> workAssignmentList;
    
    /**
     * @return the circuitBoardInspectionResultId
     */
    public String getCircuitBoardInspectionResultId() {
        return circuitBoardInspectionResultId;
    }

    /**
     * @param circuitBoardInspectionResultId the circuitBoardInspectionResultId to set
     */
    public void setCircuitBoardInspectionResultId(String circuitBoardInspectionResultId) {
        this.circuitBoardInspectionResultId = circuitBoardInspectionResultId;
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
     * @return the inspectionResult
     */
    public int getInspectionResult() {
        return inspectionResult;
    }

    /**
     * @param inspectionResult the inspectionResult to set
     */
    public void setInspectionResult(int inspectionResult) {
        this.inspectionResult = inspectionResult;
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
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the createUserUuid
     */
    public String getCreateUserUuid() {
        return createUserUuid;
    }

    /**
     * @param createUserUuid the createUserUuid to set
     */
    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    /**
     * @return the updateUserUuid
     */
    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    /**
     * @param updateUserUuid the updateUserUuid to set
     */
    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    /**
     * @return the resultSum
     */
    public CircuitBoardInspectionResultSum getResultSum() {
        return resultSum;
    }

    /**
     * @param resultSum the resultSum to set
     */
    public void setResultSum(CircuitBoardInspectionResultSum resultSum) {
        this.resultSum = resultSum;
    }

    /**
     * @return the resultDetailList
     */
    public List<CircuitBoardInspectionResultDetail> getResultDetailList() {
        return resultDetailList;
    }

    /**
     * @param resultDetailList the resultDetailList to set
     */
    public void setResultDetailList(List<CircuitBoardInspectionResultDetail> resultDetailList) {
        this.resultDetailList = resultDetailList;
    }

    /**
     * @return the workAssignmentList
     */
    public List<CircuitBoardWorkAssignment> getWorkAssignmentList() {
        return workAssignmentList;
    }

    /**
     * @param workAssignmentList the workAssignmentList to set
     */
    public void setWorkAssignmentList(List<CircuitBoardWorkAssignment> workAssignmentList) {
        this.workAssignmentList = workAssignmentList;
    }
}
