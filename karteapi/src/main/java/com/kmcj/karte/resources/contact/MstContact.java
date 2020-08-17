/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.contact;

import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.mgmt.company.MstMgmtCompany;
import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_contact")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstContact.findAll", query = "SELECT m FROM MstContact m"),//JOIN FETCH m.mstLocation LEFT JOIN FETCH m.mstCompany ORDER BY m.uuid
    @NamedQuery(name = "MstContact.findByUuid", query = "SELECT m FROM MstContact m WHERE m.uuid = :uuid"),
    @NamedQuery(name = "MstContact.deleteByUuid", query = "DELETE FROM MstContact m WHERE m.uuid = :uuid"),
    @NamedQuery(name = "MstContact.findByMgmtCompanyCode", query = "SELECT m FROM MstContact m WHERE m.mgmtCompanyCode = :mgmtCompanyCode AND m.assetManagementFlg = :assetManagementFlg"),
})
@Cacheable(value = false)
public class MstContact implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Size(max = 100)
    @Column(name = "MGMT_COMPANY_CODE")
    private String mgmtCompanyCode;
    @Size(max = 100)
    @Column(name = "CONTACT_NAME")
    private String contactName;
    @Size(max = 100)
    @Column(name = "DEPARTMENT")
    private String department;
    @Size(max = 100)
    @Column(name = "POSITION")
    private String position;
    @Size(max = 25)
    @Column(name = "TEL_NO")
    private String telNo;
    @Size(max = 100)
    @Column(name = "MAIL_ADDRESS")
    private String mailAddress;
    @Column(name = "ASSET_MANAGEMENT_FLG")
    private int assetManagementFlg;
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

    @Column(name = "COMPANY_ID")
    private String companyId;
    
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompany;
    
    @Column(name = "LOCATION_ID")
    private String locationId;
    
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstLocation mstLocation;
    
    @JoinColumn(name = "MGMT_COMPANY_CODE", referencedColumnName = "MGMT_COMPANY_CODE", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMgmtCompany mstMgmtCompany;
    
    @Transient
    private String companyCode;

    @Transient
    private String locationCode;
    
    public MstContact() {
    }

    public MstContact(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMgmtCompanyCode() {
        return mgmtCompanyCode;
    }

    public void setMgmtCompanyCode(String mgmtCompanyCode) {
        this.mgmtCompanyCode = mgmtCompanyCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public int getAssetManagementFlg() {
        return assetManagementFlg;
    }

    public void setAssetManagementFlg(int assetManagementFlg) {
        this.assetManagementFlg = assetManagementFlg;
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


    public MstLocation getMstLocation() {
        return mstLocation;
    }
    public void setMstLocation(MstLocation mstLocation) {
        this.mstLocation = mstLocation;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstContact)) {
            return false;
        }
        MstContact other = (MstContact) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.contact.MstContact[ uuid=" + uuid + " ]";
    }

    /**
     * @return the mailAddress
     */
    public String getMailAddress() {
        return mailAddress;
    }

    /**
     * @param mailAddress the mailAddress to set
     */
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
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
     * @return the companyCode
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * @param companyCode the companyCode to set
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    /**
     * @return the locationCode
     */
    public String getLocationCode() {
        return locationCode;
    }

    /**
     * @param locationCode the locationCode to set
     */
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    String setAssetManagementFlg(MstContact mstContact) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
