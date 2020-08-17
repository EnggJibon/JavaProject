/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mgmt.company;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_mgmt_company")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMgmtCompany.findAll", query = "SELECT m FROM MstMgmtCompany m"),
    @NamedQuery(name = "MstMgmtCompany.findByMgmtCompanyCode", query = "SELECT m FROM MstMgmtCompany m WHERE m.mgmtCompanyCode = :mgmtCompanyCode")
})
@Cacheable(value = false)
public class MstMgmtCompany implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MGMT_COMPANY_CODE")
    private String mgmtCompanyCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MGMT_COMPANY_NAME")
    private String mgmtCompanyName;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;

    public MstMgmtCompany() {
    }

    public MstMgmtCompany(String mgmtCompanyCode) {
        this.mgmtCompanyCode = mgmtCompanyCode;
    }

    public MstMgmtCompany(String mgmtCompanyCode, String mgmtCompanyName) {
        this.mgmtCompanyCode = mgmtCompanyCode;
        this.mgmtCompanyName = mgmtCompanyName;
    }

    public String getMgmtCompanyCode() {
        return mgmtCompanyCode;
    }

    public void setMgmtCompanyCode(String mgmtCompanyCode) {
        this.mgmtCompanyCode = mgmtCompanyCode;
    }

    public String getMgmtCompanyName() {
        return mgmtCompanyName;
    }

    public void setMgmtCompanyName(String mgmtCompanyName) {
        this.mgmtCompanyName = mgmtCompanyName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mgmtCompanyCode != null ? mgmtCompanyCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMgmtCompany)) {
            return false;
        }
        MstMgmtCompany other = (MstMgmtCompany) object;
        if ((this.mgmtCompanyCode == null && other.mgmtCompanyCode != null) || (this.mgmtCompanyCode != null && !this.mgmtCompanyCode.equals(other.mgmtCompanyCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mgmt.company.MstMgmtCompany[ mgmtCompanyCode=" + mgmtCompanyCode + " ]";
    }
    
}
