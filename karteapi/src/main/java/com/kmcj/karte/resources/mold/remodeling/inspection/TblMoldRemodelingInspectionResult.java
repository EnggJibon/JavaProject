package com.kmcj.karte.resources.mold.remodeling.inspection;

import com.kmcj.karte.resources.mold.inspection.item.MstMoldInspectionItem;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetail;
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
@Table(name = "tbl_mold_remodeling_inspection_result")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldRemodelingInspectionResult.findAll", query = "SELECT t FROM TblMoldRemodelingInspectionResult t"),
    @NamedQuery(name = "TblMoldRemodelingInspectionResult.findById", query = "SELECT t FROM TblMoldRemodelingInspectionResult t WHERE t.id = :id"),
    @NamedQuery(name = "TblMoldRemodelingInspectionResult.findByRemodelingDetailId", query = "SELECT t FROM TblMoldRemodelingInspectionResult t WHERE t.tblMoldRemodelingInspectionResultPK.remodelingDetailId = :remodelingDetailId"),
    @NamedQuery(name = "TblMoldRemodelingInspectionResult.findBySeq", query = "SELECT t FROM TblMoldRemodelingInspectionResult t WHERE t.tblMoldRemodelingInspectionResultPK.seq = :seq"),
    @NamedQuery(name = "TblMoldRemodelingInspectionResult.findByInspectionItemId", query = "SELECT t FROM TblMoldRemodelingInspectionResult t WHERE t.tblMoldRemodelingInspectionResultPK.inspectionItemId = :inspectionItemId"),
    @NamedQuery(name = "TblMoldRemodelingInspectionResult.findByInspectionResult", query = "SELECT t FROM TblMoldRemodelingInspectionResult t WHERE t.inspectionResult = :inspectionResult"),
    @NamedQuery(name = "TblMoldRemodelingInspectionResult.findByInspectionResultText", query = "SELECT t FROM TblMoldRemodelingInspectionResult t WHERE t.inspectionResultText = :inspectionResultText"),
    @NamedQuery(name = "TblMoldRemodelingInspectionResult.findByCreateDate", query = "SELECT t FROM TblMoldRemodelingInspectionResult t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMoldRemodelingInspectionResult.findByUpdateDate", query = "SELECT t FROM TblMoldRemodelingInspectionResult t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMoldRemodelingInspectionResult.findByCreateUserUuid", query = "SELECT t FROM TblMoldRemodelingInspectionResult t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMoldRemodelingInspectionResult.findByUpdateUserUuid", query = "SELECT t FROM TblMoldRemodelingInspectionResult t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblMoldRemodelingInspectionResult implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMoldRemodelingInspectionResultPK tblMoldRemodelingInspectionResultPK;
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
    private MstMoldInspectionItem mstMoldInspectionItem;

    public TblMoldRemodelingInspectionResult() {
    }

    public TblMoldRemodelingInspectionResult(TblMoldRemodelingInspectionResultPK tblMoldRemodelingInspectionResultPK) {
        this.tblMoldRemodelingInspectionResultPK = tblMoldRemodelingInspectionResultPK;
    }

    public TblMoldRemodelingInspectionResult(TblMoldRemodelingInspectionResultPK tblMoldRemodelingInspectionResultPK, String id) {
        this.tblMoldRemodelingInspectionResultPK = tblMoldRemodelingInspectionResultPK;
        this.id = id;
    }

    public TblMoldRemodelingInspectionResult(String remodelingDetailId, int seq, String inspectionItemId) {
        this.tblMoldRemodelingInspectionResultPK = new TblMoldRemodelingInspectionResultPK(remodelingDetailId, seq, inspectionItemId);
    }

    public TblMoldRemodelingInspectionResultPK getTblMoldRemodelingInspectionResultPK() {
        return tblMoldRemodelingInspectionResultPK;
    }

    public void setTblMoldRemodelingInspectionResultPK(TblMoldRemodelingInspectionResultPK tblMoldRemodelingInspectionResultPK) {
        this.tblMoldRemodelingInspectionResultPK = tblMoldRemodelingInspectionResultPK;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInspectionResult(String inspectionResult) {
        this.inspectionResult = inspectionResult;
    }

    public void setInspectionResultText(String inspectionResultText) {
        this.inspectionResultText = inspectionResultText;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setMstMoldInspectionItem(MstMoldInspectionItem mstMoldInspectionItem) {
        this.mstMoldInspectionItem = mstMoldInspectionItem;
    }

    public String getId() {
        return id;
    }

    public String getInspectionResult() {
        return inspectionResult;
    }

    public String getInspectionResultText() {
        return inspectionResultText;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public MstMoldInspectionItem getMstMoldInspectionItem() {
        return mstMoldInspectionItem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblMoldRemodelingInspectionResultPK != null ? tblMoldRemodelingInspectionResultPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldRemodelingInspectionResult)) {
            return false;
        }
        TblMoldRemodelingInspectionResult other = (TblMoldRemodelingInspectionResult) object;
        if ((this.tblMoldRemodelingInspectionResultPK == null && other.tblMoldRemodelingInspectionResultPK != null) || (this.tblMoldRemodelingInspectionResultPK != null && !this.tblMoldRemodelingInspectionResultPK.equals(other.tblMoldRemodelingInspectionResultPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResult[ tblMoldRemodelingInspectionResultPK=" + tblMoldRemodelingInspectionResultPK + " ]";
    }

}
