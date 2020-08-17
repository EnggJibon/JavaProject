
package com.kmcj.karte.resources.license.limit;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "mst_license_limit")
@XmlRootElement
@NamedQueries({

    @NamedQuery(name = "MstLicenseLimit.findAll", query = "SELECT m FROM MstLicenseLimit m"),
    @NamedQuery(name = "MstLicenseLimit.findById", query = "SELECT m FROM MstLicenseLimit m WHERE m.id = :id"),
    @NamedQuery(name = "MstLicenseLimit.findByUserCount", query = "SELECT m FROM MstLicenseLimit m WHERE m.userCount = :userCount"),
    @NamedQuery(name = "MstLicenseLimit.findByMoldCount", query = "SELECT m FROM MstLicenseLimit m WHERE m.moldCount = :moldCount"),
    @NamedQuery(name = "MstLicenseLimit.findByMachineCount", query = "SELECT m FROM MstLicenseLimit m WHERE m.machineCount = :machineCount")})
public class MstLicenseLimit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "USER_COUNT")
    private String userCount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MOLD_COUNT")
    private String moldCount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MACHINE_COUNT")
    private String machineCount;

    public MstLicenseLimit() {
    }

    public MstLicenseLimit(String id) {
        this.id = id;
    }

    public MstLicenseLimit(String id, String userCount, String moldCount, String machineCount) {
        this.id = id;
        this.userCount = userCount;
        this.moldCount = moldCount;
        this.machineCount = machineCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getMoldCount() {
        return moldCount;
    }

    public void setMoldCount(String moldCount) {
        this.moldCount = moldCount;
    }

    public String getMachineCount() {
        return machineCount;
    }

    public void setMachineCount(String machineCount) {
        this.machineCount = machineCount;
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
        if (!(object instanceof MstLicenseLimit)) {
            return false;
        }
        MstLicenseLimit other = (MstLicenseLimit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.license.limit.MstLicenseLimit[ id=" + id + " ]";
    }
    
}
