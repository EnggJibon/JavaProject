/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.maintenance.cycleptn;

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
public class TblMaintenanceCyclePtnPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "TYPE")
    private int type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "CYCLE_CODE")
    private String cycleCode;

    public TblMaintenanceCyclePtnPK() {
    }

    public TblMaintenanceCyclePtnPK(int type, String cycleCode) {
        this.type = type;
        this.cycleCode = cycleCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCycleCode() {
        return cycleCode;
    }

    public void setCycleCode(String cycleCode) {
        this.cycleCode = cycleCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) type;
        hash += (cycleCode != null ? cycleCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMaintenanceCyclePtnPK)) {
            return false;
        }
        TblMaintenanceCyclePtnPK other = (TblMaintenanceCyclePtnPK) object;
        if (this.type != other.type) {
            return false;
        }
        if ((this.cycleCode == null && other.cycleCode != null) || (this.cycleCode != null && !this.cycleCode.equals(other.cycleCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtnPK[ type=" + type + ", cycleCode=" + cycleCode + " ]";
    }
    
}
