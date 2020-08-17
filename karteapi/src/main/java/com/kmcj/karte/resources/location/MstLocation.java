/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.location;

import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.mgmt.company.MstMgmtCompany;
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
import javax.persistence.PrimaryKeyJoinColumn;
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
@Table(name = "mst_location")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstLocation.findAll", query = "SELECT m FROM MstLocation m"),
    @NamedQuery(name = "MstLocation.findById", query = "SELECT m FROM MstLocation m WHERE m.id = :id"),
    @NamedQuery(name = "MstLocation.findOnlyByLocationCode", query = "SELECT m FROM MstLocation m WHERE m.locationCode = :locationCode And m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstLocation.findByLocationCode", query = "SELECT m FROM MstLocation m WHERE m.locationCode = :locationCode And m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstLocation.findByLocationName", query = "SELECT m FROM MstLocation m WHERE m.locationName = :locationName And m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstLocation.findByZipCode", query = "SELECT m FROM MstLocation m WHERE m.zipCode = :zipCode"),
    @NamedQuery(name = "MstLocation.findByAddress", query = "SELECT m FROM MstLocation m WHERE m.address = :address"),
    @NamedQuery(name = "MstLocation.findByTelNo", query = "SELECT m FROM MstLocation m WHERE m.telNo = :telNo"),
    @NamedQuery(name = "MstLocation.findByCreateDate", query = "SELECT m FROM MstLocation m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstLocation.findByUpdateDate", query = "SELECT m FROM MstLocation m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstLocation.findByCreateUserUuid", query = "SELECT m FROM MstLocation m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstLocation.findByUpdateUserUuid", query = "SELECT m FROM MstLocation m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstLocation.findByExternalFlg", query = "SELECT m FROM MstLocation m WHERE m.externalFlg = :externalFlg"),
    @NamedQuery(name = "MstLocation.updateByLocationCode", query = "UPDATE MstLocation m SET "
            + "m.locationName = :locationName, "
            + "m.zipCode = :zipCode, "
            + "m.address = :address, "
            + "m.telNo = :telNo, "
            + "m.companyId = :companyId, "
            + "m.updateDate = :updateDate, "
            + "m.updateUserUuid = :updateUserUuid, "
            + "m.mgmtCompanyCode = :mgmtCompanyCode "
            + " WHERE m.locationCode = :locationCode And m.externalFlg = :externalFlg"
    ),
    @NamedQuery(name = "MstLocation.delete", query = "DELETE FROM MstLocation m WHERE m.locationCode = :locationCode And m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstLocation.findFkByCompanyId", query = "SELECT m FROM MstLocation m  WHERE m.mstCompany.id = :companyId")
})

@Cacheable(value = false)
public class MstLocation implements Serializable {

    @OneToMany(mappedBy = "mstLocation")
    private Collection<TblMoldLocationHistory> tblMoldLocationHistoryCollection;

    @OneToMany(mappedBy = "mstLocation")
    private Collection<TblMoldInventory> tblMoldInventoryCollection;

    @OneToMany(mappedBy = "mstLocation")
    private Collection<MstMold> mstMoldCollection;

    @OneToMany(mappedBy = "mstLocation")
    private Collection<MstInstallationSite> mstInstallationSiteCollection;

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
    @Column(name = "LOCATION_CODE")
    private String locationCode;
    @Size(max = 100)
    @Column(name = "LOCATION_NAME")
    private String locationName;
    @Size(max = 15)
    @Column(name = "ZIP_CODE")
    private String zipCode;
    @Size(max = 100)
    @Column(name = "ADDRESS")
    private String address;
    @Size(max = 25)
    @Column(name = "TEL_NO")
    private String telNo;
    @Size(max = 45)
    @Column(name = "COMPANY_ID")
    private String companyId;
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
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID",insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompany;
    
    // Ite.5 Add S
    @Column(name = "MGMT_COMPANY_CODE")
    private String mgmtCompanyCode;
    // Ite.5 Add E
    
    /**
     * 管理先マスタ
     */
    @PrimaryKeyJoinColumn(name = "MGMT_COMPANY_CODE", referencedColumnName = "MGMT_COMPANY_CODE")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMgmtCompany mstMgmtCompany;

    public MstLocation() {
    }

    public MstLocation(String locationCode) {
        this.locationCode = locationCode;
    }

    public MstLocation(String locationCode, String id) {
        this.locationCode = locationCode;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
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
        hash += (locationCode != null ? locationCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstLocation)) {
            return false;
        }
        MstLocation other = (MstLocation) object;
        if ((this.locationCode == null && other.locationCode != null) || (this.locationCode != null && !this.locationCode.equals(other.locationCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.location.MstLocation[ locationCode=" + locationCode + " ]";
    }

    /**
     * @return the companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the mstCompany
     */
    public MstCompany getMstCompany() {
        return mstCompany;
    }

    /**
     * @param mstCompany the mstCompany to set
     */
    public void setMstCompany(MstCompany mstCompany) {
        this.mstCompany = mstCompany;
    }

    @XmlTransient
    public Collection<MstInstallationSite> getMstInstallationSiteCollection() {
        return mstInstallationSiteCollection;
    }

    public void setMstInstallationSiteCollection(Collection<MstInstallationSite> mstInstallationSiteCollection) {
        this.mstInstallationSiteCollection = mstInstallationSiteCollection;
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

    /**
     * @return the mgmtCompanyCode
     */
    public String getMgmtCompanyCode() {
        return mgmtCompanyCode;
    }

    /**
     * @param mgmtCompanyCode the mgmtCompanyCode to set
     */
    public void setMgmtCompanyCode(String mgmtCompanyCode) {
        this.mgmtCompanyCode = mgmtCompanyCode;
    }

    /**
     * @return the mstMgmtCompany
     */
    public MstMgmtCompany getMstMgmtCompany() {
        return mstMgmtCompany;
    }

    /**
     * @param mstMgmtCompany the mstMgmtCompany to set
     */
    public void setMstMgmtCompany(MstMgmtCompany mstMgmtCompany) {
        this.mstMgmtCompany = mstMgmtCompany;
    }
}
