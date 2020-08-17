/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report.user;

import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.language.MstLanguage;
import com.kmcj.karte.resources.timezone.MstTimezone;
import com.kmcj.karte.util.DateFormat;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_query_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstQueryUser.findByUserId", query = "SELECT m FROM MstQueryUser m JOIN FETCH m.mstTimezone WHERE m.userId = :userId"),
    @NamedQuery(name = "MstQueryUser.findByUuId", query = "SELECT m FROM MstQueryUser m JOIN FETCH m.mstTimezone WHERE m.uuid = :uuid"),
    @NamedQuery(name = "MstQueryUser.delete", query = "DELETE FROM MstQueryUser m WHERE m.uuid = :uuid"),
    @NamedQuery(name = "MstQueryUser.updatePassword", query
            = "UPDATE MstQueryUser m SET m.hashedPassword = :hashedPassword, m.passwordChangedAt = :passwordChangedAt "
            + "WHERE m.userId = :userId")
})
@Cacheable(value = false)
public class MstQueryUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "USER_NAME")
    private String userName;
    @Size(max = 100)
    @Column(name = "MAIL_ADDRESS")
    private String mailAddress;
    @Size(max = 256)
    @Column(name = "HASHED_PASSWORD")
    private String hashedPassword;
    @Column(name = "PASSWORD_CHANGED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordChangedAt;
    @Column(name = "PASSWORD_EXPIRES_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordExpiresAt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALID_FLG")
    private int validFlg;
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

    @Column(name = "LANG_ID")
    private String langId;

    @Column(name = "TIMEZONE")
    private String timezone;

    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompany;

    @JoinColumn(name = "TIMEZONE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstTimezone mstTimezone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LANG_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private MstLanguage mstLanguage;
    
    @Transient
    private String resetPassword;
    
    @Transient
    private String displayTimeZone = "Asia/Tokyo";
    
    @Transient
    private String passwordChangedAtLocalTime;

//    @OneToMany(mappedBy = "queryUserId")
//    private Collection<TblReportQueryUser> tblReportQueryUserCollection;
    public MstQueryUser() {
    }

    public MstQueryUser(String userId) {
        this.userId = userId;
    }

    public MstQueryUser(String userId, String uuid, int validFlg) {
        this.userId = userId;
        this.uuid = uuid;
        this.validFlg = validFlg;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Date getPasswordChangedAt() {
        return passwordChangedAt;
    }

    public void setPasswordChangedAt(Date passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
    }

    public Date getPasswordExpiresAt() {
        return passwordExpiresAt;
    }

    public void setPasswordExpiresAt(Date passwordExpiresAt) {
        this.passwordExpiresAt = passwordExpiresAt;
    }

    public int getValidFlg() {
        return validFlg;
    }

    public void setValidFlg(int validFlg) {
        this.validFlg = validFlg;
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
    
    @XmlElement
    public String getResetPassword() {
        return resetPassword;
    }
    
    public void setResetPassword(String resetPassword) {
        this.resetPassword = resetPassword;
    }
    
    public void setDisplayTimeZone(String displayTimeZone) {
        this.displayTimeZone = displayTimeZone;
        if (this.passwordChangedAt != null) {
            this.passwordChangedAtLocalTime
                    = DateFormat.dateTimeFormat(this.passwordChangedAt, this.displayTimeZone);
        }
    }

//    @XmlTransient
//    public Collection<TblReportQueryUser> getTblReportQueryUserCollection() {
//        return tblReportQueryUserCollection;
//    }
//
//    public void setTblReportQueryUserCollection(Collection<TblReportQueryUser> tblReportQueryUserCollection) {
//        this.tblReportQueryUserCollection = tblReportQueryUserCollection;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof MstQueryUser)) {
            return false;
        }
        MstQueryUser other = (MstQueryUser) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.custom.report.user.MstQueryUser[ userId=" + userId + " ]";
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
     * @return the langId
     */
    public String getLangId() {
        return langId;
    }

    /**
     * @param langId the langId to set
     */
    public void setLangId(String langId) {
        this.langId = langId;
    }

    /**
     * @return the timezone
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * @param timezone the timezone to set
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
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
     * @return the mstTimezone
     */
    public MstTimezone getMstTimezone() {
        return mstTimezone;
    }

    /**
     * @param mstTimezone the mstTimezone to set
     */
    public void setMstTimezone(MstTimezone mstTimezone) {
        this.mstTimezone = mstTimezone;
    }

    /**
     * @return the mstLanguage
     */
    public MstLanguage getMstLanguage() {
        return mstLanguage;
    }

    /**
     * @param mstLanguage the mstLanguage to set
     */
    public void setMstLanguage(MstLanguage mstLanguage) {
        this.mstLanguage = mstLanguage;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

}
