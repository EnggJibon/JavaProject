/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.attribute;

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
public class MstComponentAttributePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPONENT_TYPE")
    private int componentType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ATTR_CODE")
    private String attrCode;

    public MstComponentAttributePK() {
    }

    public MstComponentAttributePK(int componentType, String attrCode) {
        this.componentType = componentType;
        this.attrCode = attrCode;
    }

    public int getComponentType() {
        return componentType;
    }

    public void setComponentType(int componentType) {
        this.componentType = componentType;
    }

    public String getAttrCode() {
        return attrCode;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) componentType;
        hash += (attrCode != null ? attrCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstComponentAttributePK)) {
            return false;
        }
        MstComponentAttributePK other = (MstComponentAttributePK) object;
        if (this.componentType != other.componentType) {
            return false;
        }
        if ((this.attrCode == null && other.attrCode != null) || (this.attrCode != null && !this.attrCode.equals(other.attrCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.attribute.MstComponentAttributePK[ componentType=" + componentType + ", attrCode=" + attrCode + " ]";
    }
    
}
