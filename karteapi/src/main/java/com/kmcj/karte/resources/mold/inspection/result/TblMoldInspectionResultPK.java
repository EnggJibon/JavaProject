/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inspection.result;

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
public class TblMoldInspectionResultPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MAINTENANCE_DETAIL_ID")
    private String maintenanceDetailId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INSPECTION_ITEM_ID")
    private String inspectionItemId;

    public TblMoldInspectionResultPK() {
    }

    public TblMoldInspectionResultPK(String maintenanceDetailId, int seq, String inspectionItemId) {
        this.maintenanceDetailId = maintenanceDetailId;
        this.seq = seq;
        this.inspectionItemId = inspectionItemId;
    }

    public String getMaintenanceDetailId() {
        return maintenanceDetailId;
    }

    public void setMaintenanceDetailId(String maintenanceDetailId) {
        this.maintenanceDetailId = maintenanceDetailId;
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
        hash += (maintenanceDetailId != null ? maintenanceDetailId.hashCode() : 0);
        hash += (int) seq;
        hash += (inspectionItemId != null ? inspectionItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldInspectionResultPK)) {
            return false;
        }
        TblMoldInspectionResultPK other = (TblMoldInspectionResultPK) object;
        if ((this.maintenanceDetailId == null && other.maintenanceDetailId != null) || (this.maintenanceDetailId != null && !this.maintenanceDetailId.equals(other.maintenanceDetailId))) {
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
        return "com.kmcj.karte.resources.mold.inspection.result.TblMoldInspectionResultPK[ maintenanceDetailId=" + maintenanceDetailId + ", seq=" + seq + ", inspectionItemId=" + inspectionItemId + " ]";
    }
    
}
