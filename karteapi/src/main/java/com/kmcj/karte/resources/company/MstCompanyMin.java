/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.company;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 会社の選択肢の候補として使うためのCompanyの最小構成。
 * @author t.takasaki
 */
@Entity
@Table(name = "mst_company")
@XmlRootElement
@Cacheable(value = false)
@NamedQueries({
    @NamedQuery(
        name = "MstCompanyMin.findCodeOrName", 
        query = "SELECT m FROM MstCompanyMin m where (m.companyCode like :codeOrName OR m.companyName like :codeOrName) and m.externalFlg = :externalFlg Order by m.companyCode")
})
public class MstCompanyMin implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPANY_CODE")
    private String companyCode;
    
    @Size(max = 100)
    @Column(name = "COMPANY_NAME")
    private String companyName;
    
    @Column(name = "EXTERNAL_FLG")
    private Integer externalFlg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getExternalFlg() {
        return externalFlg;
    }

    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
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
        if (!(object instanceof MstCompanyMin)) {
            return false;
        }
        MstCompanyMin other = (MstCompanyMin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.company.MstCompanySmall[ id=" + id + " ]";
    }
    
}
