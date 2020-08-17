package com.kmcj.karte.resources.mold.remodeling.inspection;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Embeddable
public class TblMoldRemodelingInspectionResultPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "REMODELING_DETAIL_ID")
    private String remodelingDetailId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INSPECTION_ITEM_ID")
    private String inspectionItemId;

    public TblMoldRemodelingInspectionResultPK() {
    }

    public TblMoldRemodelingInspectionResultPK(String remodelingDetailId, int seq, String inspectionItemId) {
        this.remodelingDetailId = remodelingDetailId;
        this.seq = seq;
        this.inspectionItemId = inspectionItemId;
    }

    public String getRemodelingDetailId() {
        return remodelingDetailId;
    }

    public void setRemodelingDetailId(String remodelingDetailId) {
        this.remodelingDetailId = remodelingDetailId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getInspectionItemId() {
        return inspectionItemId;
    }

    public void setInspectionItemId(String inspectionItemId) {
        this.inspectionItemId = inspectionItemId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (remodelingDetailId != null ? remodelingDetailId.hashCode() : 0);
        hash += (int) seq;
        hash += (inspectionItemId != null ? inspectionItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldRemodelingInspectionResultPK)) {
            return false;
        }
        TblMoldRemodelingInspectionResultPK other = (TblMoldRemodelingInspectionResultPK) object;
        if ((this.remodelingDetailId == null && other.remodelingDetailId != null) || (this.remodelingDetailId != null && !this.remodelingDetailId.equals(other.remodelingDetailId))) {
            return false;
        }
        if (this.seq != other.seq) {
            return false;
        }
        if ((this.inspectionItemId == null && other.inspectionItemId != null) || (this.inspectionItemId != null && !this.inspectionItemId.equals(other.inspectionItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResultPK[ remodelingDetailId=" + remodelingDetailId + ", seq=" + seq + ", inspectionItemId=" + inspectionItemId + " ]";
    }
    
}
