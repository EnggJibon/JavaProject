/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.visualimage;

import com.google.gson.Gson;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author duanlin
 */
@Embeddable
public class TblComponentInspectionVisualImageFilePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_INSPECTION_RESULT_DETAIL_ID")
    private String componentInspectionResultDetailId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private Integer seq;

    public TblComponentInspectionVisualImageFilePK() {
    }

    public String getComponentInspectionResultDetailId() {
        return componentInspectionResultDetailId;
    }

    public void setComponentInspectionResultDetailId(String componentInspectionResultDetailId) {
        this.componentInspectionResultDetailId = componentInspectionResultDetailId;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (componentInspectionResultDetailId != null ? componentInspectionResultDetailId.hashCode() : 0);
        hash += (int) seq;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblComponentInspectionVisualImageFilePK)) {
            return false;
        }
        TblComponentInspectionVisualImageFilePK other = (TblComponentInspectionVisualImageFilePK) object;
        if ((this.componentInspectionResultDetailId == null && other.componentInspectionResultDetailId != null) || (this.componentInspectionResultDetailId != null && !this.componentInspectionResultDetailId.equals(other.componentInspectionResultDetailId))) {
            return false;
        }
        if (this.seq != other.seq) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
    
}
