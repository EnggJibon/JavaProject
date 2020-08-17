/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author h.ishihara
 */
@Embeddable
public class TblCircuitBoardWorkAssignmentPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CIRCUIT_BOARD_INSPECTION_RESULT_SUM_ID")
    private String circuitBoardInspectionResultSumId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ASSIGNMENT_CODE")
    private String assignmentCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "USER_UUID")
    private String userUuid;  
    
    public TblCircuitBoardWorkAssignmentPK(){        
    }

       // @JoinColumn(name = "CIRCUIT_BOARD_INSPECTION_RESULT_SUM_ID", referencedColumnName = "CIRCUIT_BOARD_INSPECTION_RESULT_SUM_ID", insertable = false, updatable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="CIRCUIT_BOARD_INSPECTION_RESULT_SUM_ID")
//    private TblCircuitBoardInspectionResultSum tblCircuitBoardInspectionResultSum;
//    
    /**
     * @return the circuitBoardInspectionResultId
     */
    public String getCircuitBoardInspectionResultSumId() {
        return circuitBoardInspectionResultSumId;
    }

    /**
     * @param circuitBoardInspectionResultSumId
     */
    public void setCircuitBoardInspectionResultSumId(String circuitBoardInspectionResultSumId) {
        this.circuitBoardInspectionResultSumId = circuitBoardInspectionResultSumId;
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (circuitBoardInspectionResultSumId != null ? circuitBoardInspectionResultSumId.hashCode() : 0);
        hash += (assignmentCode != null ? assignmentCode.hashCode() : 0);
        hash += (userUuid != null ? userUuid.hashCode() : 0);
        return hash;
    }
    
        @Override
    public boolean equals(Object object) {
        if (!(object instanceof TblCircuitBoardWorkAssignmentPK)) {
            return false;
        }
        TblCircuitBoardWorkAssignmentPK other = (TblCircuitBoardWorkAssignmentPK) object;
        if ((this.circuitBoardInspectionResultSumId == null && other.circuitBoardInspectionResultSumId != null) || (this.circuitBoardInspectionResultSumId != null && !this.circuitBoardInspectionResultSumId.equals(other.circuitBoardInspectionResultSumId))) {
            return false;
        }
        if ((this.assignmentCode == null && other.assignmentCode != null) || (this.assignmentCode != null && !this.assignmentCode.equals(other.assignmentCode))) {
            return false;
        }
        if ((this.userUuid == null && other.userUuid != null) || (this.userUuid != null && !this.userUuid.equals(other.userUuid))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.inspection.TblCircuitBoardWorkAssignmentPK[ circuitBoardInspectionResultId=" + circuitBoardInspectionResultSumId + ", assignmentCode=" + assignmentCode + ", userUuid=" + userUuid + " ]";
    }

//    /**
//     * @return the tblCircuitBoardInspectionResultSum
//     */
//    public TblCircuitBoardInspectionResultSum getTblCircuitBoardInspectionResultSum() {
//        return tblCircuitBoardInspectionResultSum;
//    }
//
//    /**
//     * @param tblCircuitBoardInspectionResultSum the tblCircuitBoardInspectionResultSum to set
//     */
//    public void setTblCircuitBoardInspectionResultSum(TblCircuitBoardInspectionResultSum tblCircuitBoardInspectionResultSum) {
//        this.tblCircuitBoardInspectionResultSum = tblCircuitBoardInspectionResultSum;
//    }
}
