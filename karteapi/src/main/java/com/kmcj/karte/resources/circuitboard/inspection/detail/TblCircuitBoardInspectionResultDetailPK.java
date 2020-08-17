/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection.detail;

import com.kmcj.karte.resources.circuitboard.defect.MstCircuitBoardDefect;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author liujiyong
 */
@Embeddable
public class TblCircuitBoardInspectionResultDetailPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CIRCUIT_BOARD_INSPECTION_RESULT_ID")
    private String circuitBoardInspectionResultId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CIRCUIT_BOARD_COMPONENT_ID")
    private String circuitBoardComponentId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DEFECTIVE_ITEM_ID")
    private String defectiveItemId;
        
    public TblCircuitBoardInspectionResultDetailPK() {
    }

    public TblCircuitBoardInspectionResultDetailPK(String circuitBoardInspectionResultId, String circuitBoardComponentId, String defectiveItemId) {
        this.circuitBoardInspectionResultId = circuitBoardInspectionResultId;
        this.circuitBoardComponentId = circuitBoardComponentId;
        this.defectiveItemId = defectiveItemId;
    }

    public String getCircuitBoardInspectionResultId() {
        return circuitBoardInspectionResultId;
    }

    public void setCircuitBoardInspectionResultId(String circuitBoardInspectionResultId) {
        this.circuitBoardInspectionResultId = circuitBoardInspectionResultId;
    }

    public String getCircuitBoardComponentId() {
        return circuitBoardComponentId;
    }

    public void setCircuitBoardComponentId(String circuitBoardComponentId) {
        this.circuitBoardComponentId = circuitBoardComponentId;
    }

    public String getDefectiveItemId() {
        return defectiveItemId;
    }

    public void setDefectiveItemId(String defectiveItemId) {
        this.defectiveItemId = defectiveItemId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (circuitBoardInspectionResultId != null ? circuitBoardInspectionResultId.hashCode() : 0);
        hash += (circuitBoardComponentId != null ? circuitBoardComponentId.hashCode() : 0);
        hash += (defectiveItemId != null ? defectiveItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblCircuitBoardInspectionResultDetailPK)) {
            return false;
        }
        TblCircuitBoardInspectionResultDetailPK other = (TblCircuitBoardInspectionResultDetailPK) object;
        if ((this.circuitBoardInspectionResultId == null && other.circuitBoardInspectionResultId != null) || (this.circuitBoardInspectionResultId != null && !this.circuitBoardInspectionResultId.equals(other.circuitBoardInspectionResultId))) {
            return false;
        }
        if ((this.circuitBoardComponentId == null && other.circuitBoardComponentId != null) || (this.circuitBoardComponentId != null && !this.circuitBoardComponentId.equals(other.circuitBoardComponentId))) {
            return false;
        }
        if ((this.defectiveItemId == null && other.defectiveItemId != null) || (this.defectiveItemId != null && !this.defectiveItemId.equals(other.defectiveItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.filedef.TblCircuitBoardInspectionResultDetailPK[ circuitBoardInspectionResultId=" + circuitBoardInspectionResultId + ", circuitBoardComponentId=" + circuitBoardComponentId + ", defectiveItemId=" + defectiveItemId + " ]";
    }    
}
