/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection.detail;

import com.kmcj.karte.resources.circuitboard.defect.MstCircuitBoardDefect;
import com.kmcj.karte.resources.circuitboard.inspection.TblCircuitBoardInspectionResult;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author liujiyong
 */
@Entity
@Table(name = "tbl_circuit_board_inspection_result_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCircuitBoardInspectionResultDetail.findAll", query = "SELECT t FROM TblCircuitBoardInspectionResultDetail t"),
    @NamedQuery(name = "TblCircuitBoardInspectionResultDetail.findByCircuitBoardInspectionResultId", query = "SELECT t FROM TblCircuitBoardInspectionResultDetail t WHERE t.tblCircuitBoardInspectionResultDetailPK.circuitBoardInspectionResultId = :circuitBoardInspectionResultId"),
    @NamedQuery(name = "TblCircuitBoardInspectionResultDetail.findByCircuitBoardComponentId", query = "SELECT t FROM TblCircuitBoardInspectionResultDetail t WHERE t.tblCircuitBoardInspectionResultDetailPK.circuitBoardComponentId = :circuitBoardComponentId"),
    @NamedQuery(name = "TblCircuitBoardInspectionResultDetail.findByDefectiveItemId", query = "SELECT t FROM TblCircuitBoardInspectionResultDetail t WHERE t.tblCircuitBoardInspectionResultDetailPK.defectiveItemId = :defectiveItemId"),
    @NamedQuery(name = "TblCircuitBoardInspectionResultDetail.findByRepairContent", query = "SELECT t FROM TblCircuitBoardInspectionResultDetail t WHERE t.repairContent = :repairContent")})
public class TblCircuitBoardInspectionResultDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblCircuitBoardInspectionResultDetailPK tblCircuitBoardInspectionResultDetailPK;
    @Size(max = 1000)
    @Column(name = "REPAIR_CONTENT")
    private String repairContent;
    @Column(name = "DEFECT_NUM")
    private Integer DefectNum;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
        
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;

    
    @PrimaryKeyJoinColumn(name = "DEFECTIVE_ITEM_ID", referencedColumnName = "ID")
    @ManyToOne
    private MstCircuitBoardDefect mstCircuitBoardDefect;
    
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }
        
    public Integer getDefectNum() {
        return DefectNum;
    }

    public void setDefectNum(Integer DefectNum) {
        this.DefectNum = DefectNum;
    }
    
    
    @PrimaryKeyJoinColumn(name = "CIRCUIT_BOARD_INSPECTION_RESULT_ID", referencedColumnName = "CIRCUIT_BOARD_INSPECTION_RESULT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblCircuitBoardInspectionResult tblCircuitBoardInspectionResult;

    public TblCircuitBoardInspectionResultDetail() {
    }

    public TblCircuitBoardInspectionResultDetail(TblCircuitBoardInspectionResultDetailPK tblCircuitBoardInspectionResultDetailPK) {
        this.tblCircuitBoardInspectionResultDetailPK = tblCircuitBoardInspectionResultDetailPK;
    }

    public TblCircuitBoardInspectionResultDetail(String circuitBoardInspectionResultId, String circuitBoardComponentId, String defectiveItemId) {
        this.tblCircuitBoardInspectionResultDetailPK = new TblCircuitBoardInspectionResultDetailPK(circuitBoardInspectionResultId, circuitBoardComponentId, defectiveItemId);
    }

    public TblCircuitBoardInspectionResultDetailPK getTblCircuitBoardInspectionResultDetailPK() {
        return tblCircuitBoardInspectionResultDetailPK;
    }

    public void setTblCircuitBoardInspectionResultDetailPK(TblCircuitBoardInspectionResultDetailPK tblCircuitBoardInspectionResultDetailPK) {
        this.tblCircuitBoardInspectionResultDetailPK = tblCircuitBoardInspectionResultDetailPK;
    }

    public String getRepairContent() {
        return repairContent;
    }

    public void setRepairContent(String repairContent) {
        this.repairContent = repairContent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblCircuitBoardInspectionResultDetailPK != null ? tblCircuitBoardInspectionResultDetailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblCircuitBoardInspectionResultDetail)) {
            return false;
        }
        TblCircuitBoardInspectionResultDetail other = (TblCircuitBoardInspectionResultDetail) object;
        if ((this.tblCircuitBoardInspectionResultDetailPK == null && other.tblCircuitBoardInspectionResultDetailPK != null) || (this.tblCircuitBoardInspectionResultDetailPK != null && !this.tblCircuitBoardInspectionResultDetailPK.equals(other.tblCircuitBoardInspectionResultDetailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.filedef.TblCircuitBoardInspectionResultDetail[ tblCircuitBoardInspectionResultDetailPK=" + tblCircuitBoardInspectionResultDetailPK + " ]";
    }

    /**
     * @return the tblCircuitBoardInspectionResult
     */
    @XmlTransient
    public TblCircuitBoardInspectionResult getTblCircuitBoardInspectionResult() {
        return tblCircuitBoardInspectionResult;
    }

    /**
     * @param tblCircuitBoardInspectionResult the tblCircuitBoardInspectionResult to set
     */
    public void setTblCircuitBoardInspectionResult(TblCircuitBoardInspectionResult tblCircuitBoardInspectionResult) {
        this.tblCircuitBoardInspectionResult = tblCircuitBoardInspectionResult;
    }

    /**
     * @return the mstCircuitBoardDefect
     */
    public MstCircuitBoardDefect getMstCircuitBoardDefect() {
        return mstCircuitBoardDefect;
    }

    /**
     * @param mstCircuitBoardDefect the mstCircuitBoardDefect to set
     */
    public void setMstCircuitBoardDefect(MstCircuitBoardDefect mstCircuitBoardDefect) {
        this.mstCircuitBoardDefect = mstCircuitBoardDefect;
    }
    
}
