/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.proccond.spec;

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
public class MstMachineProcCondSpecPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MACHINE_PROC_COND_ID")
    private String machineProcCondId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ATTR_ID")
    private String attrId;

    public MstMachineProcCondSpecPK() {
    }

    public MstMachineProcCondSpecPK(String machineProcCondId, String attrId) {
        this.machineProcCondId = machineProcCondId;
        this.attrId = attrId;
    }

    public String getMachineProcCondId() {
        return machineProcCondId;
    }

    public void setMachineProcCondId(String machineProcCondId) {
        this.machineProcCondId = machineProcCondId;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (machineProcCondId != null ? machineProcCondId.hashCode() : 0);
        hash += (attrId != null ? attrId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpecPK)) {
            return false;
        }
        com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpecPK other = (com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpecPK) object;
        if ((this.machineProcCondId == null && other.machineProcCondId != null) || (this.machineProcCondId != null && !this.machineProcCondId.equals(other.machineProcCondId))) {
            return false;
        }
        if ((this.attrId == null && other.attrId != null) || (this.attrId != null && !this.attrId.equals(other.attrId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpecPK[ machineProcCondId=" + machineProcCondId + ", attrId=" + attrId + " ]";
    }
}
