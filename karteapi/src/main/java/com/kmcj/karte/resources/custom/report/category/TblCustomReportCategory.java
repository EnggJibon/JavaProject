/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report.category;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author c.darvin
 */
@Entity
@Table(name = "tbl_custom_report_category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCustomReportCategory.findAll", query = "SELECT t FROM TblCustomReportCategory t"),
    @NamedQuery(name = "TblCustomReportCategory.findById", query = "SELECT t FROM TblCustomReportCategory t WHERE t.id = :id"),
    @NamedQuery(name = "TblCustomReportCategory.findByName", query = "SELECT t FROM TblCustomReportCategory t WHERE t.name = :name"),
    @NamedQuery(name = "TblCustomReportCategory.deleteById", query = "DELETE FROM TblCustomReportCategory t WHERE t.id = :id")})
public class TblCustomReportCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "NAME")
    private String name;
    
    @Transient
    private boolean modified = false;   // 更新対象制御
    @Transient
    private boolean added = false;

    public TblCustomReportCategory() {
    }

    public TblCustomReportCategory(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * @return the added
     */
    public boolean isAdded() {
        return added;
    }

    /**
     * @param added the added to set
     */
    public void setAdded(boolean added) {
        this.added = added;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblCustomReportCategory)) {
            return false;
        }
        TblCustomReportCategory other = (TblCustomReportCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.custom.report.category.TblCustomReportCategory[ id=" + id + " ]";
    }
    
}
