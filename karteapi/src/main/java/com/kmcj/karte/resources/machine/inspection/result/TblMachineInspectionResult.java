/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.inspection.result;

import com.kmcj.karte.resources.machine.inspection.item.MstMachineInspectionItem;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_machine_inspection_result")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineInspectionResult.findAll", query = "SELECT t FROM TblMachineInspectionResult t"),
    @NamedQuery(name = "TblMachineInspectionResult.findById", query = "SELECT t FROM TblMachineInspectionResult t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineInspectionResult.findByMaintenanceDetailId", query = "SELECT t FROM TblMachineInspectionResult t WHERE t.tblMachineInspectionResultPK.maintenanceDetailId = :maintenanceDetailId"),
    @NamedQuery(name = "TblMachineInspectionResult.findBySeq", query = "SELECT t FROM TblMachineInspectionResult t WHERE t.tblMachineInspectionResultPK.seq = :seq"),
    @NamedQuery(name = "TblMachineInspectionResult.findByInspectionItemId", query = "SELECT t FROM TblMachineInspectionResult t WHERE t.tblMachineInspectionResultPK.inspectionItemId = :inspectionItemId"),
    @NamedQuery(name = "TblMachineInspectionResult.findByInspectionResult", query = "SELECT t FROM TblMachineInspectionResult t WHERE t.inspectionResult = :inspectionResult"),
    @NamedQuery(name = "TblMachineInspectionResult.findByInspectionResultText", query = "SELECT t FROM TblMachineInspectionResult t WHERE t.inspectionResultText = :inspectionResultText"),
    @NamedQuery(name = "TblMachineInspectionResult.findByCreateDate", query = "SELECT t FROM TblMachineInspectionResult t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMachineInspectionResult.findByUpdateDate", query = "SELECT t FROM TblMachineInspectionResult t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMachineInspectionResult.findByCreateUserUuid", query = "SELECT t FROM TblMachineInspectionResult t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMachineInspectionResult.findByUpdateUserUuid", query = "SELECT t FROM TblMachineInspectionResult t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblMachineInspectionResult.deleteByMaintenanceDetailId", query = "DELETE FROM TblMachineInspectionResult t WHERE t.tblMachineInspectionResultPK.maintenanceDetailId = :maintenanceDetailId")
})
@Cacheable(value = false)
public class TblMachineInspectionResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMachineInspectionResultPK tblMachineInspectionResultPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 100)
    @Column(name = "INSPECTION_RESULT")
    private String inspectionResult;
    @Size(max = 100)
    @Column(name = "INSPECTION_RESULT_TEXT")
    private String inspectionResultText;
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
    @JoinColumn(name = "INSPECTION_ITEM_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachineInspectionItem mstMachineInspectionItem;

    public TblMachineInspectionResult() {
    }

    public TblMachineInspectionResult(TblMachineInspectionResultPK tblMachineInspectionResultPK) {
        this.tblMachineInspectionResultPK = tblMachineInspectionResultPK;
    }

    public TblMachineInspectionResult(TblMachineInspectionResultPK tblMachineInspectionResultPK, String id) {
        this.tblMachineInspectionResultPK = tblMachineInspectionResultPK;
        this.id = id;
    }

    public TblMachineInspectionResult(String maintenanceDetailId, int seq, String inspectionItemId) {
        this.tblMachineInspectionResultPK = new TblMachineInspectionResultPK(maintenanceDetailId, seq, inspectionItemId);
    }

    public TblMachineInspectionResultPK getTblMachineInspectionResultPK() {
        return tblMachineInspectionResultPK;
    }

    public void setTblMachineInspectionResultPK(TblMachineInspectionResultPK tblMachineInspectionResultPK) {
        this.tblMachineInspectionResultPK = tblMachineInspectionResultPK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInspectionResult() {
        return inspectionResult;
    }

    public void setInspectionResult(String inspectionResult) {
        this.inspectionResult = inspectionResult;
    }

    public String getInspectionResultText() {
        return inspectionResultText;
    }

    public void setInspectionResultText(String inspectionResultText) {
        this.inspectionResultText = inspectionResultText;
    }

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

    public MstMachineInspectionItem getMstMachineInspectionItem() {
        return mstMachineInspectionItem;
    }

    public void setMstMachineInspectionItem(MstMachineInspectionItem mstMachineInspectionItem) {
        this.mstMachineInspectionItem = mstMachineInspectionItem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblMachineInspectionResultPK != null ? tblMachineInspectionResultPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineInspectionResult)) {
            return false;
        }
        TblMachineInspectionResult other = (TblMachineInspectionResult) object;
        if ((this.tblMachineInspectionResultPK == null && other.tblMachineInspectionResultPK != null) || (this.tblMachineInspectionResultPK != null && !this.tblMachineInspectionResultPK.equals(other.tblMachineInspectionResultPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.TblMachineInspectionResult[ tblMachineInspectionResultPK=" + tblMachineInspectionResultPK + " ]";
    }

}
