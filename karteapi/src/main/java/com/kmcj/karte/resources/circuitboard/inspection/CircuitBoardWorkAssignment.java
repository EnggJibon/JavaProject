/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import com.kmcj.karte.BasicResponse;

/**
 *
 * @author h.ishihara
 */
public class CircuitBoardWorkAssignment  extends BasicResponse {
    private String circuitBoardInspectionResultId;
    private String assignmentCode;
    private String userUuid;

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
     * @return the assignmentCode
     */
    public String getAssignmentCode() {
        return assignmentCode;
    }

    /**
     * @param assignmentCode the assignmentCode to set
     */
    public void setAssignmentCode(String assignmentCode) {
        this.assignmentCode = assignmentCode;
    }

    /**
     * @return the userUuid
     */
    public String getUserUuid() {
        return userUuid;
    }

    /**
     * @param userUuid the userUuid to set
     */
    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }
}
