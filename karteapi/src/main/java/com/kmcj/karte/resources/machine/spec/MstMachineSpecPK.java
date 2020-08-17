package com.kmcj.karte.resources.machine.spec;

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
public class MstMachineSpecPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MACHINE_SPEC_HST_ID")
    private String machineSpecHstId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ATTR_ID")
    private String attrId;

    public MstMachineSpecPK() {
    }

    public MstMachineSpecPK(String machineSpecHstId, String attrId) {
        this.machineSpecHstId = machineSpecHstId;
        this.attrId = attrId;
    }

    public String getMachineSpecHstId() {
        return machineSpecHstId;
    }

    public void setMachineSpecHstId(String machineSpecHstId) {
        this.machineSpecHstId = machineSpecHstId;
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
        hash += (machineSpecHstId != null ? machineSpecHstId.hashCode() : 0);
        hash += (attrId != null ? attrId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMachineSpecPK)) {
            return false;
        }
        MstMachineSpecPK other = (MstMachineSpecPK) object;
        if ((this.machineSpecHstId == null && other.machineSpecHstId != null) || (this.machineSpecHstId != null && !this.machineSpecHstId.equals(other.machineSpecHstId))) {
            return false;
        }
        if ((this.attrId == null && other.attrId != null) || (this.attrId != null && !this.attrId.equals(other.attrId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.MstMachineSpecPK[ machineSpecHstId=" + machineSpecHstId + ", attrId=" + attrId + " ]";
    }
    
}
