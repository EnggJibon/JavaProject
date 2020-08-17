/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.part;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Embeddable
public class TblMoldMaintenancePartPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MAINTENANCE_ID")
    private String maintenanceId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MOLD_PART_REL_ID")
    private String moldPartRelId;

    public TblMoldMaintenancePartPK() {
    }

    public TblMoldMaintenancePartPK(String maintenanceId, String moldPartRelId) {
        this.maintenanceId = maintenanceId;
        this.moldPartRelId = moldPartRelId;
    }

    public String getMaintenanceId(){
        return maintenanceId;
    }

    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public String getMoldPartRelId() {
        return moldPartRelId;
    }

    public void setMoldPartRelId(String moldPartRelId) {
        this.moldPartRelId = moldPartRelId;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (maintenanceId != null ? maintenanceId.hashCode() : 0);
        hash += (moldPartRelId != null ? moldPartRelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldMaintenancePartPK)) {
            return false;
        }
        TblMoldMaintenancePartPK other = (TblMoldMaintenancePartPK) object;
        if ((this.maintenanceId == null && other.maintenanceId != null) || (this.maintenanceId != null && !this.maintenanceId.equals(other.maintenanceId))) {
            return false;
        }
        if ((this.moldPartRelId == null && other.moldPartRelId != null) || (this.moldPartRelId != null && !this.moldPartRelId.equals(other.moldPartRelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.maintenance.part.TblMoldMaintenancePartPK[ maintenanceId=" + maintenanceId + ", moldPartRelId=" + moldPartRelId + " ]";
    }
    
}
