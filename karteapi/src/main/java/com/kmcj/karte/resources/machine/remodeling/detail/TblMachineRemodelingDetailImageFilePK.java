/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.remodeling.detail;

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
public class TblMachineRemodelingDetailImageFilePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "REMODELING_DETAIL_ID")
    private String remodelingDetailId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;

    public TblMachineRemodelingDetailImageFilePK() {
    }

    public TblMachineRemodelingDetailImageFilePK(String remodelingDetailId, int seq) {
        this.remodelingDetailId = remodelingDetailId;
        this.seq = seq;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (remodelingDetailId != null ? remodelingDetailId.hashCode() : 0);
        hash += (int) seq;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineRemodelingDetailImageFilePK)) {
            return false;
        }
        TblMachineRemodelingDetailImageFilePK other = (TblMachineRemodelingDetailImageFilePK) object;
        if ((this.remodelingDetailId == null && other.remodelingDetailId != null) || (this.remodelingDetailId != null && !this.remodelingDetailId.equals(other.remodelingDetailId))) {
            return false;
        }
        if (this.seq != other.seq) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailImageFilePK[ remodelingDetailId=" + remodelingDetailId + ", seq=" + seq + " ]";
    }
    
}
