/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.file;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Apeng
 */
@Embeddable
public class MstComponentInspectFilePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INSPECT_TYPE_ID")
    private String inspectTypeId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INSPECT_CLASS_ID")
    private String inspectClassId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "OWNER_COMPANY_ID")
    private String ownerCompanyId = "SELF";

    public MstComponentInspectFilePK() {
    }

    public MstComponentInspectFilePK(String inspectTypeId, String inspectClassId) {
        this.inspectTypeId = inspectTypeId;
        this.inspectClassId = inspectClassId;
    }

    public String getInspectTypeId() {
        return inspectTypeId;
    }

    public void setInspectTypeId(String inspectTypeId) {
        this.inspectTypeId = inspectTypeId;
    }

    public String getInspectClassId() {
        return inspectClassId;
    }

    public void setInspectClassId(String inspectClassId) {
        this.inspectClassId = inspectClassId;
    }

    public String getOwnerCompanyId() {
        return ownerCompanyId;
    }

    public void setOwnerCompanyId(String ownerCompanyId) {
        this.ownerCompanyId = ownerCompanyId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.inspectTypeId);
        hash = 37 * hash + Objects.hashCode(this.inspectClassId);
        hash = 37 * hash + Objects.hashCode(this.ownerCompanyId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MstComponentInspectFilePK other = (MstComponentInspectFilePK) obj;
        if (!Objects.equals(this.inspectTypeId, other.inspectTypeId)) {
            return false;
        }
        if (!Objects.equals(this.inspectClassId, other.inspectClassId)) {
            return false;
        }
        if (!Objects.equals(this.ownerCompanyId, other.ownerCompanyId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.inspection.file.MstComponentInspectFilePK[ inspectTypeId=" + inspectTypeId + ", inspectClassId=" + inspectClassId + " ]";
    }
    
}
