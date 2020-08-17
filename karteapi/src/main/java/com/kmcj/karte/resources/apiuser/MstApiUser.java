/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.apiuser;

import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.timezone.MstTimezone;
import com.kmcj.karte.util.DateFormat;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "mst_api_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstApiUser.findAll", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone"),
    @NamedQuery(name = "MstApiUser.findByUuid", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.uuid = :uuid"),
    @NamedQuery(name = "MstApiUser.findByUserId", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.userId = :userId"),
    @NamedQuery(name = "MstApiUser.findByUserName", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.userName = :userName"),
    @NamedQuery(name = "MstApiUser.findByMailAddress", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.mailAddress = :mailAddress"),
    @NamedQuery(name = "MstApiUser.findByTimezone", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.timezone = :timezone"),
    @NamedQuery(name = "MstApiUser.findByLangId", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.langId = :langId"),
    @NamedQuery(name = "MstApiUser.findByHashedPassword", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.hashedPassword = :hashedPassword"),
    @NamedQuery(name = "MstApiUser.findByPasswordChangedAt", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.passwordChangedAt = :passwordChangedAt"),
    @NamedQuery(name = "MstApiUser.findByPasswordExpiresAt", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.passwordExpiresAt = :passwordExpiresAt"),
    @NamedQuery(name = "MstApiUser.findByValidFlg", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.validFlg = :validFlg"),
    @NamedQuery(name = "MstApiUser.findByCreateDate", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstApiUser.findByUpdateDate", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstApiUser.findByCreateUserUuid", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstApiUser.findByCompanyId", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.mstCompany.id = :companyId"),
    @NamedQuery(name = "MstApiUser.findByUpdateUserUuid", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.updateUserUuid = :updateUserUuid"),

    @NamedQuery(name = "MstApiUser.delete", query = "DELETE FROM MstApiUser m WHERE m.userId = :userId"),

    @NamedQuery(name = "MstApiUser.findFkByCompanyId", query = "SELECT m FROM MstApiUser m JOIN FETCH m.mstTimezone WHERE m.mstCompany.id = :companyId"),

    @NamedQuery(name = "MstApiUser.updateByUserId", query
            = "UPDATE MstApiUser m SET m.companyId = :companyId, m.hashedPassword = :hashedPassword, "
            + "m.mailAddress = :mailAddress, m.langId = :langId, m.timezone = :timezone, m.validFlg = :validFlg,"
            + "m.updateDate = :updateDate,m.updateUserUuid = :updateUserUuid "
            + "WHERE m.userId = :userId"),
    @NamedQuery(name = "MstApiUser.updatePassword", query
            = "UPDATE MstApiUser m SET m.hashedPassword = :hashedPassword, m.passwordChangedAt = :passwordChangedAt "
            + "WHERE m.userId = :userId")
})
@Cacheable(value = false)
public class MstApiUser implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "USER_NAME")
    private String userName;
    @Size(max = 100)
    @Column(name = "MAIL_ADDRESS")
    private String mailAddress;
    @Size(max = 50)
    @Column(name = "TIMEZONE")
    private String timezone;
    @Size(max = 45)
    @Column(name = "LANG_ID")
    private String langId;
    @Size(max = 256)
    @Column(name = "HASHED_PASSWORD")
    private String hashedPassword;
    @Column(name = "PASSWORD_CHANGED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordChangedAt;
    @Column(name = "PASSWORD_EXPIRES_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordExpiresAt;
    @Column(name = "VALID_FLG")
    private Integer validFlg;
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

    @Transient
    private String resetPassword;

    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstCompany mstCompany;

    @JoinColumn(name = "TIMEZONE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstTimezone mstTimezone;

    @Transient
    private String displayTimeZone = "Asia/Tokyo";

    @Transient
    private String passwordChangedAtLocalTime;

    public MstApiUser() {
    }

    public MstApiUser(String userId) {
        this.userId = userId;
    }

    public MstApiUser(String userId, String uuid, String userName) {
        this.userId = userId;
        this.uuid = uuid;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
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

    public Integer getValidFlg() {
        return validFlg;
    }

    public void setValidFlg(Integer validFlg) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstApiUser)) {
            return false;
        }
        MstApiUser other = (MstApiUser) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.apiuser.MstApiUser[ userId=" + userId + " ]";
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
     * @return the resetPassword
     */
    @XmlElement
    public String getResetPassword() {
        return resetPassword;
    }

    /**
     * @param resetPassword the resetPassword to set
     */
    public void setResetPassword(String resetPassword) {
        this.resetPassword = resetPassword;
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
     * @param displayTimeZone the displayTimeZone to set
     */
    public void setDisplayTimeZone(String displayTimeZone) {
        this.displayTimeZone = displayTimeZone;
        if (this.passwordChangedAt != null) {
            this.passwordChangedAtLocalTime
                    = DateFormat.dateTimeFormat(this.passwordChangedAt, this.displayTimeZone);
        }
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

}
