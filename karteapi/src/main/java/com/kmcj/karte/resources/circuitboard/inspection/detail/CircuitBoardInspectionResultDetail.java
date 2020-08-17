/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection.detail;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author h.ishihara
 */
public class CircuitBoardInspectionResultDetail extends BasicResponse {
    private String circuitBoardInspectionResultId;
    private String circuitBoardComponentId;
    private String defectiveItemId;
    private Date inspectionDate;
    private String repairContent;
    //private String serialNumber;
    private Integer defectNum;

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
     * @return the circuitBoardComponentId
     */
    public String getCircuitBoardComponentId() {
        return circuitBoardComponentId;
    }

    /**
     * @param circuitBoardComponentId the circuitBoardComponentId to set
     */
    public void setCircuitBoardComponentId(String circuitBoardComponentId) {
        this.circuitBoardComponentId = circuitBoardComponentId;
    }

    /**
     * @return the cefectiveItemId
     */
    public String getDefectiveItemId() {
        return defectiveItemId;
    }

    /**
     * @param cefectiveItemId the cefectiveItemId to set
     */
    public void setDefectiveItemId(String cefectiveItemId) {
        this.defectiveItemId = cefectiveItemId;
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
     * @return the repairContent
     */
    public String getRepairContent() {
        return repairContent;
    }

    /**
     * @param repairContent the repairContent to set
     */
    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent;
    }

    /**
     * @return the defectNumResult
     */
    public Integer getDefectNum() {
        return defectNum;
    }

    /**
     * @param defectNumResult the defectNumResult to set
     */
    public void setDefectNum(Integer defectNumResult) {
        this.defectNum = defectNumResult;
    }

    /**
     * @return the serialNumber
     */
//    public String getSerialNumber() {
//        return serialNumber;
//    }
//
//    /**
//     * @param serialNumber the serialNumber to set
//     */
//    public void setSerialNumber(String serialNumber) {
//        this.serialNumber = serialNumber;
//    }



}
