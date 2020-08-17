/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.installationsite;

import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.inventory.TblMoldInventory;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistory;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_installation_site")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstInstallationSite.findAll", query = "SELECT m FROM MstInstallationSite m"),
    @NamedQuery(name = "MstInstallationSite.findById", query = "SELECT m FROM MstInstallationSite m WHERE m.id = :id"),
    @NamedQuery(name = "MstInstallationSite.findOnlyByInstallationSiteCode", query = "SELECT m FROM MstInstallationSite m WHERE m.installationSiteCode = :installationSiteCode "),
    @NamedQuery(name = "MstInstallationSite.findByInstallationSiteCode", query = "SELECT m FROM MstInstallationSite m WHERE m.installationSiteCode = :installationSiteCode And m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstInstallationSite.findByInstallationSiteName", query = "SELECT m FROM MstInstallationSite m WHERE m.installationSiteName = :installationSiteName And m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstInstallationSite.findByCreateDate", query = "SELECT m FROM MstInstallationSite m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstInstallationSite.findByUpdateDate", query = "SELECT m FROM MstInstallationSite m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstInstallationSite.findByCreateUserUuid", query = "SELECT m FROM MstInstallationSite m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstInstallationSite.findByUpdateUserUuid", query = "SELECT m FROM MstInstallationSite m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstInstallationSite.findByExternalFlg", query = "SELECT m FROM MstInstallationSite m WHERE m.externalFlg = :externalFlg"),
    @NamedQuery(name = "MstInstallationSite.updateByInstallationSiteCode", query = "UPDATE MstInstallationSite m SET "
            + "m.installationSiteName = :installationSiteName,"
            + "m.locationId = :locationId,"
            + "m.updateDate = :updateDate, "
            + "m.updateDate = :updateDate, "
            + "m.updateUserUuid = :updateUserUuid  "
            + "WHERE m.installationSiteCode = :installationSiteCode And m.externalFlg = :externalFlg"),
    @NamedQuery(name = "MstInstallationSite.delete", query = "DELETE FROM MstInstallationSite m WHERE m.installationSiteCode = :installationSiteCode And m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstInstallationSite.findFkByLocationId", query = "SELECT m FROM MstInstallationSite m WHERE m.locationId = :locationId")
})
@Cacheable(value = false)
public class MstInstallationSite implements Serializable {

    @OneToMany(mappedBy = "mstInstallationSite")
    private Collection<TblMoldLocationHistory> tblMoldLocationHistoryCollection;

    @OneToMany(mappedBy = "mstInstallationSite")
    private Collection<TblMoldInventory> tblMoldInventoryCollection;

    @OneToMany(mappedBy = "mstInstallationSite")
    private Collection<MstMold> mstMoldCollection;

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
    @Column(name = "INSTALLATION_SITE_CODE")
    private String installationSiteCode;
    @Size(max = 45)
    @Column(name = "INSTALLATION_SITE_NAME")
    private String installationSiteName;
    @Size(max = 45)
    @Column(name = "LOCATION_ID")
    private String locationId;
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
    @Column(name = "EXTERNAL_FLG")
    private Integer externalFlg;
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID" ,insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstLocation mstLocation;

    public MstInstallationSite() {
    }

    public MstInstallationSite(String installationSiteCode) {
        this.installationSiteCode = installationSiteCode;
    }

    public MstInstallationSite(String installationSiteCode, String id) {
        this.installationSiteCode = installationSiteCode;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstallationSiteCode() {
        return installationSiteCode;
    }

    public void setInstallationSiteCode(String installationSiteCode) {
        this.installationSiteCode = installationSiteCode;
    }

    public String getInstallationSiteName() {
        return installationSiteName;
    }

    public void setInstallationSiteName(String installationSiteName) {
        this.installationSiteName = installationSiteName;
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

   /**
     * @return the locationId
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the mstLocation
     */
    public MstLocation getMstLocation() {
        return mstLocation;
    }

    /**
     * @param mstLocation the mstLocation to set
     */
    public void setMstLocation(MstLocation mstLocation) {
        this.mstLocation = mstLocation;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (installationSiteCode != null ? installationSiteCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstInstallationSite)) {
            return false;
        }
        MstInstallationSite other = (MstInstallationSite) object;
        if ((this.installationSiteCode == null && other.installationSiteCode != null) || (this.installationSiteCode != null && !this.installationSiteCode.equals(other.installationSiteCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.installationsite.MstInstallationSite[ installationSiteCode=" + installationSiteCode + " ]";
    }

    @XmlTransient
    public Collection<MstMold> getMstMoldCollection() {
        return mstMoldCollection;
    }

    public void setMstMoldCollection(Collection<MstMold> mstMoldCollection) {
        this.mstMoldCollection = mstMoldCollection;
    }

    @XmlTransient
    public Collection<TblMoldInventory> getTblMoldInventoryCollection() {
        return tblMoldInventoryCollection;
    }

    public void setTblMoldInventoryCollection(Collection<TblMoldInventory> tblMoldInventoryCollection) {
        this.tblMoldInventoryCollection = tblMoldInventoryCollection;
    }

    @XmlTransient
    public Collection<TblMoldLocationHistory> getTblMoldLocationHistoryCollection() {
        return tblMoldLocationHistoryCollection;
    }

    public void setTblMoldLocationHistoryCollection(Collection<TblMoldLocationHistory> tblMoldLocationHistoryCollection) {
        this.tblMoldLocationHistoryCollection = tblMoldLocationHistoryCollection;
    }

    /**
     * @return the externalFlg
     */
    public Integer getExternalFlg() {
        return externalFlg;
    }

    /**
     * @param externalFlg the externalFlg to set
     */
    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
    }
    
}
