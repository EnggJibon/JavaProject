/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.component.relation;

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
public class MstMoldComponentRelationPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;

    public MstMoldComponentRelationPK() {
    }

    public MstMoldComponentRelationPK(String moldUuid, String componentId) {
        this.moldUuid = moldUuid;
        this.componentId = componentId;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (moldUuid != null ? moldUuid.hashCode() : 0);
        hash += (componentId != null ? componentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMoldComponentRelationPK)) {
            return false;
        }
        MstMoldComponentRelationPK other = (MstMoldComponentRelationPK) object;
        if ((this.moldUuid == null && other.moldUuid != null) || (this.moldUuid != null && !this.moldUuid.equals(other.moldUuid))) {
            return false;
        }
        if ((this.componentId == null && other.componentId != null) || (this.componentId != null && !this.componentId.equals(other.componentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationPK[ moldUuid=" + moldUuid + ", componentId=" + componentId + " ]";
    }
    
}
