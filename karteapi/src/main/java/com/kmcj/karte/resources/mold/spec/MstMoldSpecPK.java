/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.spec;

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
public class MstMoldSpecPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MOLD_SPEC_HST_ID")
    private String moldSpecHstId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ATTR_ID")
    private String attrId;

    public MstMoldSpecPK() {
    }

    public MstMoldSpecPK(String moldSpecHstId, String attrId) {
        this.moldSpecHstId = moldSpecHstId;
        this.attrId = attrId;
    }

    public String getMoldSpecHstId() {
        return moldSpecHstId;
    }

    public void setMoldSpecHstId(String moldSpecHstId) {
        this.moldSpecHstId = moldSpecHstId;
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
        hash += (moldSpecHstId != null ? moldSpecHstId.hashCode() : 0);
        hash += (attrId != null ? attrId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMoldSpecPK)) {
            return false;
        }
        MstMoldSpecPK other = (MstMoldSpecPK) object;
        if ((this.moldSpecHstId == null && other.moldSpecHstId != null) || (this.moldSpecHstId != null && !this.moldSpecHstId.equals(other.moldSpecHstId))) {
            return false;
        }
        if ((this.attrId == null && other.attrId != null) || (this.attrId != null && !this.attrId.equals(other.attrId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.spec.MstMoldSpecPK[ moldSpecHstId=" + moldSpecHstId + ", attrId=" + attrId + " ]";
    }
    
}
