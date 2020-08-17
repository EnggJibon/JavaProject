/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.user;

import com.kmcj.karte.resources.authorization.MstAuth;
import com.kmcj.karte.resources.language.MstLanguage;
import com.kmcj.karte.resources.timezone.MstTimezone;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.choice.MstChoice;
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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "mst_user")
@XmlRootElement
@NamedQueries({
    //SELECT
    @NamedQuery(name = "MstUser.findAll", query = "SELECT m FROM MstUser m JOIN FETCH m.mstLanguage JOIN FETCH m.mstTimezone JOIN FETCH m.mstAuth LEFT JOIN FETCH m.mstCompany ORDER BY m.userId"),
    @NamedQuery(name = "MstUser.findByUserId", query = "SELECT m FROM MstUser m JOIN FETCH m.mstLanguage JOIN FETCH m.mstTimezone JOIN FETCH m.mstAuth LEFT JOIN FETCH m.mstCompany WHERE m.userId = :userId"),
    @NamedQuery(name = "MstUser.findByUserUuid", query = "SELECT m FROM MstUser m WHERE m.uuid = :uuid"),
    @NamedQuery(name = "MstUser.findByDepartment", query = "SELECT m FROM MstUser m WHERE m.department = :department"),
    @NamedQuery(name = "MstUser.findByUserName", query = "SELECT m FROM MstUser m JOIN FETCH m.mstLanguage JOIN FETCH m.mstTimezone JOIN FETCH m.mstAuth LEFT JOIN FETCH m.mstCompany WHERE m.userName = :userName"),
    @NamedQuery(name = "MstUser.countByAuthId", query = "SELECT COUNT(m) FROM MstUser m WHERE m.authId = :authId"),
    @NamedQuery(name = "MstUser.findByValid", query = "SELECT m FROM MstUser m JOIN FETCH m.mstLanguage JOIN FETCH m.mstTimezone JOIN FETCH m.mstAuth LEFT JOIN FETCH m.mstCompany WHERE m.validFlg = :validFlg"),
    @NamedQuery(name = "MstUser.findByUserIdValid", query = "SELECT m FROM MstUser m JOIN FETCH m.mstLanguage JOIN FETCH m.mstTimezone JOIN FETCH m.mstAuth LEFT JOIN FETCH m.mstCompany WHERE m.userId = :userId and m.validFlg = :validFlg"),
    @NamedQuery(name = "MstUser.findByUserNameValid", query = "SELECT m FROM MstUser m JOIN FETCH m.mstLanguage JOIN FETCH m.mstTimezone JOIN FETCH m.mstAuth LEFT JOIN FETCH m.mstCompany WHERE m.userName like :userName and m.validFlg = :validFlg"),
    @NamedQuery(name = "MstUser.findByUserIdNameValid", query = "SELECT m FROM MstUser m JOIN FETCH m.mstLanguage JOIN FETCH m.mstTimezone JOIN FETCH m.mstAuth LEFT JOIN FETCH m.mstCompany WHERE m.userId = :userId and m.userName like :userName and m.validFlg = :validFlg"),
    @NamedQuery(name = "MstUser.countByValid", query = "SELECT COUNT(m) FROM MstUser m WHERE m.validFlg = :validFlg"),
    @NamedQuery(name = "MstUser.countByUserIdValid", query = "SELECT COUNT(m) FROM MstUser m WHERE m.userId = :userId and m.validFlg = :validFlg"),
    @NamedQuery(name = "MstUser.countByUserNameValid", query = "SELECT COUNT(m) FROM MstUser m WHERE m.userName = :userName and m.validFlg = :validFlg"),
    @NamedQuery(name = "MstUser.countByUserIdNameValid", query = "SELECT COUNT(m) FROM MstUser m WHERE m.userId = :userId and m.userName = :userName and m.validFlg = :validFlg"),
    //UPDATE
    @NamedQuery(name = "MstUser.updateByUserId", query = 
        "UPDATE MstUser m SET m.userName = :userName, m.mailAddress = :mailAddress, m.langId = :langId, m.timezone = :timezone, m.authId = :authId, " +
                " m.updateDate = :updateDate, m.updateUserUuid = :updateUserUuid, m.companyId = :companyId, m.validFlg = :validFlg, m.department = :department, m.procCd = :procCd " +
                ",m.indefiniteFlg = :indefiniteFlg, m.passwordExpiresAt = (CASE WHEN :indefiniteFlg2 = 1 THEN NULL ELSE m.passwordExpiresAt END) " +
        "WHERE m.userId = :userId"),
    @NamedQuery(name = "MstUser.updatePassword", query = 
        "UPDATE MstUser m SET m.hashedPassword = :hashedPassword, m.passwordChangedAt = :passwordChangedAt " +
        ",m.initialPasswordFlg = :initialPasswordFlg, m.passwordExpiresAt = :passwordExpiresAt, m.loginFailCount = :loginFailCount, m.accountLocked = :accountLocked " + 
        "WHERE m.userId = :userId"),
    @NamedQuery(name = "MstUser.updateLoginFailCountAndAccountLocked", query = "UPDATE MstUser m SET m.loginFailCount = :loginFailCount,m.accountLocked = :accountLocked WHERE m.userId = :userId"),
    //DELETE
    @NamedQuery(name = "MstUser.delete", query = "DELETE FROM MstUser m WHERE m.userId = :userId")
    })
@Cacheable(value = false)
public class MstUser implements Serializable {

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

    @JoinColumn(name = "AUTH_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstAuth mstAuth;

    @JoinColumn(name = "TIMEZONE", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstTimezone mstTimezone;

    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstCompany mstCompany;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;

    @Size(max = 45)
    @Column(name = "LANG_ID")
    private String langId;
    
    @Transient
    private String passwordChangedAtLocalTime;
    @Transient
    private String resetPassword;
    @Transient
    private String displayTimeZone = "Asia/Tokyo";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LANG_ID", insertable = false, updatable = false)
    private MstLanguage mstLanguage;
    
    @Size(max = 50)
    @Column(name = "TIMEZONE")
    private String timezone;

    @Column(name = "PASSWORD_CHANGED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    //@XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date passwordChangedAt;
    @Column(name = "PASSWORD_EXPIRES_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordExpiresAt;

    private static final long serialVersionUID = 1L;
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
    @Size(max = 45)
    @Column(name = "AUTH_ID")
    private String authId;
    @Size(max = 256)
    @Column(name = "HASHED_PASSWORD")
    private String hashedPassword;
    @Size(max = 45)
    @Column(name = "COMPANY_ID")
    private String companyId;
    @Size(max = 3)
    @Column(name = "VALID_FLG")
    private String validFlg;
    @Size(max = 11)
    @Column(name = "DEPARTMENT")
    private String department;
    @Size(max = 45)
    @Column(name = "PROC_CD")
    private String procCd;

    //Password policy related columns
    @Column(name = "INITIAL_PASSWORD_FLG")
    private Integer initialPasswordFlg;
    @Column(name = "LOGIN_FAIL_COUNT")
    private Integer loginFailCount;
    @Column(name = "INDEFINITE_FLG")
    private Integer indefiniteFlg;
    @Column(name = "ACCOUNT_LOCKED")
    private int accountLocked = 0;
            
    
    public MstUser() {
    }

    public MstUser(String userId) {
        this.userId = userId;
    }

    public MstUser(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
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

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }


    public String getValidFlg() {
        return validFlg;
    }

    public void setValidFlg(String validFlg) {
        this.validFlg = validFlg;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProcCd() {
        return procCd;
    }

    public void setProcCd(String procCd) {
        this.procCd = procCd;
    }

    @XmlTransient
    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the uuid fields are not set
        if (!(object instanceof MstUser)) {
            return false;
        }
        MstUser other = (MstUser) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.user.MstUser[ userId=" + userId + " ]";
    }
    
    //@XmlTransient
    public Date getPasswordChangedAt() {
        return passwordChangedAt;
    }

    /*
    @Transient
    @XmlElement(name="passwordChangedAtString")
    private String passwordChangedAtString;
    
    
    public String getPasswordChangedAtString() {
        passwordChangedAtString = DateFormat.dateTimeFormat(passwordChangedAt, "Asia/Tokyo");
        return passwordChangedAtString;
    }

    public void setPasswordChangedAtString(String passwordChangedAtString) {
        this.passwordChangedAtString = passwordChangedAtString;
    }
*/

    
    public void setPasswordChangedAt(Date passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
    }

    public Date getPasswordExpiresAt() {
        return passwordExpiresAt;
    }

    public void setPasswordExpiresAt(Date passwordExpiresAt) {
        this.passwordExpiresAt = passwordExpiresAt;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * @return the passwordChangedAtLocalTime
     */
    @XmlElement
    public String getPasswordChangedAtLocalTime() {
        return passwordChangedAtLocalTime;
        //return DateFormat.dateTimeFormat(this.passwordChangedAt, this.displayTimeZone);// 
    }

    /**
     * @param passwordChangedAtLocalTime the passwordChangedAtLocalTime to set
     */
    public void setPasswordChangedAtLocalTime(String passwordChangedAtLocalTime) {
        this.passwordChangedAtLocalTime = passwordChangedAtLocalTime;
    }

    /**
     * @param displayTimeZone the displayTimeZone to set
     */
    public void setDisplayTimeZone(String displayTimeZone) {
        this.displayTimeZone = displayTimeZone;
        if (this.passwordChangedAt != null) {
            this.passwordChangedAtLocalTime =
                DateFormat.dateTimeFormat(this.passwordChangedAt, this.displayTimeZone);
        }
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public MstAuth getMstAuth() {
        return mstAuth;
    }

    public void setMstAuth(MstAuth mstAuth) {
        this.mstAuth = mstAuth;
    }

    public MstTimezone getMstTimezone() {
        return mstTimezone;
    }

    public void setMstTimezone(MstTimezone mstTimezone) {
        this.mstTimezone = mstTimezone;
    }

    public MstCompany getMstCompany() {
        return mstCompany;
    }

    public void setMstCompany(MstCompany mstCompany) {
        this.mstCompany = mstCompany;
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
     * @return the initialPasswordFlg
     */
    public Integer getInitialPasswordFlg() {
        return initialPasswordFlg;
    }

    /**
     * @param initialPasswordFlg the initialPasswordFlg to set
     */
    public void setInitialPasswordFlg(Integer initialPasswordFlg) {
        this.initialPasswordFlg = initialPasswordFlg;
    }

    /**
     * @return the loginFailCount
     */
    public Integer getLoginFailCount() {
        return loginFailCount;
    }

    /**
     * @param loginFailCount the loginFailCount to set
     */
    public void setLoginFailCount(Integer loginFailCount) {
        this.loginFailCount = loginFailCount;
    }

    /**
     * @return the indefiniteFlg
     */
    public Integer getIndefiniteFlg() {
        return indefiniteFlg;
    }

    /**
     * @param indefiniteFlg the indefiniteFlg to set
     */
    public void setIndefiniteFlg(Integer indefiniteFlg) {
        this.indefiniteFlg = indefiniteFlg;
    }

    /**
     * @return the accountLocked
     */
    public int getAccountLocked() {
        return accountLocked;
    }

    /**
     * @param accountLocked the accountLocked to set
     */
    public void setAccountLocked(int accountLocked) {
        this.accountLocked = accountLocked;
    }
    
}
