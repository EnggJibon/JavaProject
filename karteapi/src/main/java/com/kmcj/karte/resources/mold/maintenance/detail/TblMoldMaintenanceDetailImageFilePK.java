/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.detail;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author zds
 */
@Embeddable
public class TblMoldMaintenanceDetailImageFilePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MAINTENANCE_DETAIL_ID")
    private String maintenanceDetailId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;

    public TblMoldMaintenanceDetailImageFilePK() {
    }

    public TblMoldMaintenanceDetailImageFilePK(String maintenanceDetailId, int seq) {
        this.maintenanceDetailId = maintenanceDetailId;
        this.seq = seq;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (maintenanceDetailId != null ? maintenanceDetailId.hashCode() : 0);
        hash += (int) seq;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldMaintenanceDetailImageFilePK)) {
            return false;
        }
        TblMoldMaintenanceDetailImageFilePK other = (TblMoldMaintenanceDetailImageFilePK) object;
        if ((this.maintenanceDetailId == null && other.maintenanceDetailId != null) || (this.maintenanceDetailId != null && !this.maintenanceDetailId.equals(other.maintenanceDetailId))) {
            return false;
        }
        if (this.seq != other.seq) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetailImageFilePK[ maintenanceDetailId=" + maintenanceDetailId + ", seq=" + seq + " ]";
    }
    
}
