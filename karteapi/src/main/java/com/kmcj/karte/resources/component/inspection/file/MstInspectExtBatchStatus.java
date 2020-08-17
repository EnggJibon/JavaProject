/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.file;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * mst_component_inspect_typeに対応するEntityClass
 * @author t.takasaki
 */
@Entity
@XmlRootElement
@Table(name = "mst_inspect_ext_batch_status")
@NamedQueries({
    @NamedQuery(name = "MstInspectExtBatchStatus.findById", query = "SELECT b FROM MstInspectExtBatchStatus b WHERE b.pk.id = :id")
})
public class MstInspectExtBatchStatus implements Serializable {
    @EmbeddedId
    private PK pk;
    @NotNull
    @Column(name = "BAT_UPD_STATUS")
    @Enumerated(EnumType.STRING)
    private BatUpdStatus batUpdStatus;
    
    public MstInspectExtBatchStatus() {}
    
    public MstInspectExtBatchStatus(String id, String companyId) {
        this.pk = new PK();
        pk.setId(id);
        pk.setCompanyId(companyId);
    }

    public PK getPk() {
        return pk;
    }

    public void setPk(PK pk) {
        this.pk = pk;
    }

    public BatUpdStatus getBatUpdStatus() {
        return batUpdStatus;
    }

    public void setBatUpdStatus(BatUpdStatus batUpdStatus) {
        this.batUpdStatus = batUpdStatus;
    }
    
    @Embeddable
    public static class PK implements Serializable {
        @NotNull
        @Size(min = 1, max = 45)
        @Column(name = "ID")
        private String id;
        @NotNull
        @Column(name = "COMPANY_ID")
        private String companyId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 41 * hash + Objects.hashCode(this.id);
            hash = 41 * hash + Objects.hashCode(this.companyId);
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
            final PK other = (PK) obj;
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            if (!Objects.equals(this.companyId, other.companyId)) {
                return false;
            }
            return true;
        }
    }
}
