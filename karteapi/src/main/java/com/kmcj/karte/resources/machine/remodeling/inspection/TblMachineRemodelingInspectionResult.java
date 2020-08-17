package com.kmcj.karte.resources.machine.remodeling.inspection;

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
@Table(name = "tbl_machine_remodeling_inspection_result")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineRemodelingInspectionResult.findAll", query = "SELECT t FROM TblMachineRemodelingInspectionResult t"),
    @NamedQuery(name = "TblMachineRemodelingInspectionResult.findById", query = "SELECT t FROM TblMachineRemodelingInspectionResult t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineRemodelingInspectionResult.findByRemodelingDetailId", query = "SELECT t FROM TblMachineRemodelingInspectionResult t WHERE t.tblMachineRemodelingInspectionResultPK.remodelingDetailId = :remodelingDetailId"),
    @NamedQuery(name = "TblMachineRemodelingInspectionResult.findBySeq", query = "SELECT t FROM TblMachineRemodelingInspectionResult t WHERE t.tblMachineRemodelingInspectionResultPK.seq = :seq"),
    @NamedQuery(name = "TblMachineRemodelingInspectionResult.findByInspectionItemId", query = "SELECT t FROM TblMachineRemodelingInspectionResult t WHERE t.tblMachineRemodelingInspectionResultPK.inspectionItemId = :inspectionItemId"),
    @NamedQuery(name = "TblMachineRemodelingInspectionResult.findByInspectionResult", query = "SELECT t FROM TblMachineRemodelingInspectionResult t WHERE t.inspectionResult = :inspectionResult"),
    @NamedQuery(name = "TblMachineRemodelingInspectionResult.findByInspectionResultText", query = "SELECT t FROM TblMachineRemodelingInspectionResult t WHERE t.inspectionResultText = :inspectionResultText"),
    @NamedQuery(name = "TblMachineRemodelingInspectionResult.findByCreateDate", query = "SELECT t FROM TblMachineRemodelingInspectionResult t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMachineRemodelingInspectionResult.findByUpdateDate", query = "SELECT t FROM TblMachineRemodelingInspectionResult t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMachineRemodelingInspectionResult.findByCreateUserUuid", query = "SELECT t FROM TblMachineRemodelingInspectionResult t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMachineRemodelingInspectionResult.findByUpdateUserUuid", query = "SELECT t FROM TblMachineRemodelingInspectionResult t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblMachineRemodelingInspectionResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMachineRemodelingInspectionResultPK tblMachineRemodelingInspectionResultPK;
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

    public TblMachineRemodelingInspectionResult() {
    }

    public TblMachineRemodelingInspectionResult(TblMachineRemodelingInspectionResultPK tblMachineRemodelingInspectionResultPK) {
        this.tblMachineRemodelingInspectionResultPK = tblMachineRemodelingInspectionResultPK;
    }

    public TblMachineRemodelingInspectionResult(TblMachineRemodelingInspectionResultPK tblMachineRemodelingInspectionResultPK, String id) {
        this.tblMachineRemodelingInspectionResultPK = tblMachineRemodelingInspectionResultPK;
        this.id = id;
    }

    public TblMachineRemodelingInspectionResult(String remodelingDetailId, int seq, String inspectionItemId) {
        this.tblMachineRemodelingInspectionResultPK = new TblMachineRemodelingInspectionResultPK(remodelingDetailId, seq, inspectionItemId);
    }

    public TblMachineRemodelingInspectionResultPK getTblMachineRemodelingInspectionResultPK() {
        return tblMachineRemodelingInspectionResultPK;
    }

    public void setTblMachineRemodelingInspectionResultPK(TblMachineRemodelingInspectionResultPK tblMachineRemodelingInspectionResultPK) {
        this.tblMachineRemodelingInspectionResultPK = tblMachineRemodelingInspectionResultPK;
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

    public void setMstMachineInspectionItem(MstMachineInspectionItem mstMachineInspectionItem) {
        this.mstMachineInspectionItem = mstMachineInspectionItem;
    }

    public MstMachineInspectionItem getMstMachineInspectionItem() {
        return mstMachineInspectionItem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblMachineRemodelingInspectionResultPK != null ? tblMachineRemodelingInspectionResultPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineRemodelingInspectionResult)) {
            return false;
        }
        TblMachineRemodelingInspectionResult other = (TblMachineRemodelingInspectionResult) object;
        if ((this.tblMachineRemodelingInspectionResultPK == null && other.tblMachineRemodelingInspectionResultPK != null) || (this.tblMachineRemodelingInspectionResultPK != null && !this.tblMachineRemodelingInspectionResultPK.equals(other.tblMachineRemodelingInspectionResultPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResult[ tblMachineRemodelingInspectionResultPK=" + tblMachineRemodelingInspectionResultPK + " ]";
    }

}
