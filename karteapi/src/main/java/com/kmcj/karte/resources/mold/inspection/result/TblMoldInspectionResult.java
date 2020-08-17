/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inspection.result;

import com.kmcj.karte.resources.mold.inspection.item.*;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "tbl_mold_inspection_result")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldInspectionResult.findAll", query = "SELECT t FROM TblMoldInspectionResult t"),
    @NamedQuery(name = "TblMoldInspectionResult.findById", query = "SELECT t FROM TblMoldInspectionResult t WHERE t.id = :id"),
    @NamedQuery(name = "TblMoldInspectionResult.findByMaintenanceDetailId", query = "SELECT t FROM TblMoldInspectionResult t WHERE t.tblMoldInspectionResultPK.maintenanceDetailId = :maintenanceDetailId"),
    @NamedQuery(name = "TblMoldInspectionResult.findBySeq", query = "SELECT t FROM TblMoldInspectionResult t WHERE t.tblMoldInspectionResultPK.seq = :seq"),
    @NamedQuery(name = "TblMoldInspectionResult.findByInspectionItemId", query = "SELECT t FROM TblMoldInspectionResult t WHERE t.tblMoldInspectionResultPK.inspectionItemId = :inspectionItemId"),
    @NamedQuery(name = "TblMoldInspectionResult.findByInspectionResult", query = "SELECT t FROM TblMoldInspectionResult t WHERE t.inspectionResult = :inspectionResult"),
    @NamedQuery(name = "TblMoldInspectionResult.findByInspectionResultText", query = "SELECT t FROM TblMoldInspectionResult t WHERE t.inspectionResultText = :inspectionResultText"),
    @NamedQuery(name = "TblMoldInspectionResult.findByCreateDate", query = "SELECT t FROM TblMoldInspectionResult t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMoldInspectionResult.findByUpdateDate", query = "SELECT t FROM TblMoldInspectionResult t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMoldInspectionResult.findByCreateUserUuid", query = "SELECT t FROM TblMoldInspectionResult t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMoldInspectionResult.findByUpdateUserUuid", query = "SELECT t FROM TblMoldInspectionResult t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblMoldInspectionResult.deleteByMaintenanceDetailId", query = "DELETE FROM TblMoldInspectionResult t WHERE t.tblMoldInspectionResultPK.maintenanceDetailId = :maintenanceDetailId")
})
@Cacheable(value = false)
public class TblMoldInspectionResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMoldInspectionResultPK tblMoldInspectionResultPK;
    @Basic(optional = false)
    @NotNull
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
    @ManyToOne(optional = false)
    private MstMoldInspectionItem mstMoldInspectionItem;

    public TblMoldInspectionResult() {
    }

    public TblMoldInspectionResult(TblMoldInspectionResultPK tblMoldInspectionResultPK) {
        this.tblMoldInspectionResultPK = tblMoldInspectionResultPK;
    }

    public TblMoldInspectionResult(TblMoldInspectionResultPK tblMoldInspectionResultPK, String id) {
        this.tblMoldInspectionResultPK = tblMoldInspectionResultPK;
        this.id = id;
    }

    public TblMoldInspectionResult(String maintenanceDetailId, int seq, String inspectionItemId) {
        this.tblMoldInspectionResultPK = new TblMoldInspectionResultPK(maintenanceDetailId, seq, inspectionItemId);
    }

    public TblMoldInspectionResultPK getTblMoldInspectionResultPK() {
        return tblMoldInspectionResultPK;
    }

    public void setTblMoldInspectionResultPK(TblMoldInspectionResultPK tblMoldInspectionResultPK) {
        this.tblMoldInspectionResultPK = tblMoldInspectionResultPK;
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

    public MstMoldInspectionItem getMstMoldInspectionItem() {
        return mstMoldInspectionItem;
    }

    public void setMstMoldInspectionItem(MstMoldInspectionItem mstMoldInspectionItem) {
        this.mstMoldInspectionItem = mstMoldInspectionItem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblMoldInspectionResultPK != null ? tblMoldInspectionResultPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldInspectionResult)) {
            return false;
        }
        TblMoldInspectionResult other = (TblMoldInspectionResult) object;
        if ((this.tblMoldInspectionResultPK == null && other.tblMoldInspectionResultPK != null) || (this.tblMoldInspectionResultPK != null && !this.tblMoldInspectionResultPK.equals(other.tblMoldInspectionResultPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.inspection.result.TblMoldInspectionResult[ tblMoldInspectionResultPK=" + tblMoldInspectionResultPK + " ]";
    }
    
}
