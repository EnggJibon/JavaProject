/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mgmt.location;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
@Table(name = "mst_mgmt_location")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMgmtLocation.findAll", query = "SELECT m FROM MstMgmtLocation m"),
    @NamedQuery(name = "MstMgmtLocation.findByMgmtLocationCode", query = "SELECT m FROM MstMgmtLocation m WHERE m.mgmtLocationCode = :mgmtLocationCode")

})
public class MstMgmtLocation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MGMT_LOCATION_CODE")
    private String mgmtLocationCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MGMT_LOCATION_NAME")
    private String mgmtLocationName;
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

    public MstMgmtLocation() {
    }

    public MstMgmtLocation(String mgmtLocationCode) {
        this.mgmtLocationCode = mgmtLocationCode;
    }

    public MstMgmtLocation(String mgmtLocationCode, String mgmtLocationName) {
        this.mgmtLocationCode = mgmtLocationCode;
        this.mgmtLocationName = mgmtLocationName;
    }

    public String getMgmtLocationCode() {
        return mgmtLocationCode;
    }

    public void setMgmtLocationCode(String mgmtLocationCode) {
        this.mgmtLocationCode = mgmtLocationCode;
    }

    public String getMgmtLocationName() {
        return mgmtLocationName;
    }

    public void setMgmtLocationName(String mgmtLocationName) {
        this.mgmtLocationName = mgmtLocationName;
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
        hash += (mgmtLocationCode != null ? mgmtLocationCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMgmtLocation)) {
            return false;
        }
        MstMgmtLocation other = (MstMgmtLocation) object;
        if ((this.mgmtLocationCode == null && other.mgmtLocationCode != null) || (this.mgmtLocationCode != null && !this.mgmtLocationCode.equals(other.mgmtLocationCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mgmt.location.MstMgmtLocation[ mgmtLocationCode=" + mgmtLocationCode + " ]";
    }
    
}
