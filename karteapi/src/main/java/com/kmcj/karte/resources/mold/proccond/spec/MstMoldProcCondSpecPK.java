/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond.spec;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author CH
 */
@Embeddable
public class MstMoldProcCondSpecPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MOLD_PROC_COND_ID")
    private String moldProcCondId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ATTR_ID")
    private String attrId;

    public MstMoldProcCondSpecPK() {
    }

    public MstMoldProcCondSpecPK(String moldProcCondId, String attrId) {
        this.moldProcCondId = moldProcCondId;
        this.attrId = attrId;
    }

    public String getMoldProcCondId() {
        return moldProcCondId;
    }

    public void setMoldProcCondId(String moldProcCondId) {
        this.moldProcCondId = moldProcCondId;
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
        hash += (moldProcCondId != null ? moldProcCondId.hashCode() : 0);
        hash += (attrId != null ? attrId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMoldProcCondSpecPK)) {
            return false;
        }
        MstMoldProcCondSpecPK other = (MstMoldProcCondSpecPK) object;
        if ((this.moldProcCondId == null && other.moldProcCondId != null) || (this.moldProcCondId != null && !this.moldProcCondId.equals(other.moldProcCondId))) {
            return false;
        }
        if ((this.attrId == null && other.attrId != null) || (this.attrId != null && !this.attrId.equals(other.attrId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpecPK[ moldProcCondId=" + moldProcCondId + ", attrId=" + attrId + " ]";
    }
    
}
