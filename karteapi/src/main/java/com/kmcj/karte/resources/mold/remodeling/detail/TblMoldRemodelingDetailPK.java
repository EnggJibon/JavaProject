/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.remodeling.detail;

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
public class TblMoldRemodelingDetailPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MAINTENANCE_ID")
    private String maintenanceId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;

    public TblMoldRemodelingDetailPK() {
    }

    public TblMoldRemodelingDetailPK(String maintenanceId, int seq) {
        this.maintenanceId = maintenanceId;
        this.seq = seq;
    }

    public String getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
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
        hash += (maintenanceId != null ? maintenanceId.hashCode() : 0);
        hash += (int) seq;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldRemodelingDetailPK)) {
            return false;
        }
        TblMoldRemodelingDetailPK other = (TblMoldRemodelingDetailPK) object;
        if ((this.maintenanceId == null && other.maintenanceId != null) || (this.maintenanceId != null && !this.maintenanceId.equals(other.maintenanceId))) {
            return false;
        }
        if (this.seq != other.seq) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailPK[ maintenanceId=" + maintenanceId + ", seq=" + seq + " ]";
    }
    
}
