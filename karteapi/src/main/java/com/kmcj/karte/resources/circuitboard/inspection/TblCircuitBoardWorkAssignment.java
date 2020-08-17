/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author h.ishihara
 */
@Entity
@Table(name = "tbl_circuit_board_worker_assignment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCircuitBoardWorkAssignment.findByCircuitBoardInspectionResultSumId", query = "SELECT t FROM TblCircuitBoardWorkAssignment t WHERE t.tblCircuitBoardWorkAssignmentPK.circuitBoardInspectionResultSumId = :resultSumId"),
    @NamedQuery(name = "TblCircuitBoardWorkAssignment.DeleteByCircuitBoardInspectionResultSumId", query = "DELETE FROM TblCircuitBoardWorkAssignment t WHERE t.tblCircuitBoardWorkAssignmentPK.circuitBoardInspectionResultSumId = :resultSumId")
})
public class TblCircuitBoardWorkAssignment implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private TblCircuitBoardWorkAssignmentPK tblCircuitBoardWorkAssignmentPK;
    
    @JoinColumn(name = "CIRCUIT_BOARD_INSPECTION_RESULT_SUM_ID", referencedColumnName = "CIRCUIT_BOARD_INSPECTION_RESULT_SUM_ID", updatable = false, insertable = false)
    @ManyToOne(optional = false)
    private TblCircuitBoardInspectionResultSum tblCircuitBoardInspectionResultSum;
        
    public TblCircuitBoardWorkAssignment(){        
    }
    
    @PrimaryKeyJoinColumn(name = "USER_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstUser;
        
    public TblCircuitBoardWorkAssignment(TblCircuitBoardWorkAssignmentPK tblCircuitBoardWorkAssignmentPK){        
        this.tblCircuitBoardWorkAssignmentPK = tblCircuitBoardWorkAssignmentPK;
    }

    /**
     * @return the tblCircuitBoardWorkAssignmentPK
     */
    public TblCircuitBoardWorkAssignmentPK getTblCircuitBoardWorkAssignmentPK() {
        return tblCircuitBoardWorkAssignmentPK;
    }

    /**
     * @param tblCircuitBoardWorkAssignmentPK the tblCircuitBoardWorkAssignmentPK to set
     */
    public void setTblCircuitBoardWorkAssignmentPK(TblCircuitBoardWorkAssignmentPK tblCircuitBoardWorkAssignmentPK) {
        this.tblCircuitBoardWorkAssignmentPK = tblCircuitBoardWorkAssignmentPK;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblCircuitBoardWorkAssignmentPK != null ? tblCircuitBoardWorkAssignmentPK.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblCircuitBoardWorkAssignment)) {
            return false;
        }
        TblCircuitBoardWorkAssignment other = (TblCircuitBoardWorkAssignment) object;
        if ((this.tblCircuitBoardWorkAssignmentPK == null && other.tblCircuitBoardWorkAssignmentPK != null) || (this.tblCircuitBoardWorkAssignmentPK != null && !this.tblCircuitBoardWorkAssignmentPK.equals(other.tblCircuitBoardWorkAssignmentPK))) {
            return false;
        }
        return true;
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

    /**
     * @return the mstUser
     */
    public MstUser getMstUser() {
        return mstUser;
    }

    /**
     * @param mstUser the mstUser to set
     */
    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }
}
