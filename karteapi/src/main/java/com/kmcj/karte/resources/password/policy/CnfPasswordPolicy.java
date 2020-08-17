/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.password.policy;

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
@Table(name = "cnf_password_policy")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CnfPasswordPolicy.findAll", query = "SELECT c FROM CnfPasswordPolicy c"),
    @NamedQuery(name = "CnfPasswordPolicy.findById", query = "SELECT c FROM CnfPasswordPolicy c WHERE c.policyId = :policyId")
})
@Cacheable(value = false)
public class CnfPasswordPolicy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String policyId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "PW_VALID_PERIOD")
    private int pwValidPeriod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MINIMUM_LENGTH")
    private int minimumLength;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PW_COMPLEXITY")
    private int pwComplexity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NO_REUSE_HISTORY")
    private int noReuseHistory;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FAIL_COUNT_TO_LOCK")
    private int failCountToLock;
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

    public CnfPasswordPolicy() {
    }

    public CnfPasswordPolicy(String policyId) {
        this.policyId = policyId;
    }

    public CnfPasswordPolicy(String policyId, int pwValidPeriod, int minimumLength, int pwComplexity, int noReuseHistory, int failCountToLock) {
        this.policyId = policyId;
        this.pwValidPeriod = pwValidPeriod;
        this.minimumLength = minimumLength;
        this.pwComplexity = pwComplexity;
        this.noReuseHistory = noReuseHistory;
        this.failCountToLock = failCountToLock;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public int getPwValidPeriod() {
        return pwValidPeriod;
    }

    public void setPwValidPeriod(int pwValidPeriod) {
        this.pwValidPeriod = pwValidPeriod;
    }

    public int getMinimumLength() {
        return minimumLength;
    }

    public void setMinimumLength(int minimumLength) {
        this.minimumLength = minimumLength;
    }

    public int getPwComplexity() {
        return pwComplexity;
    }

    public void setPwComplexity(int pwComplexity) {
        this.pwComplexity = pwComplexity;
    }

    public int getNoReuseHistory() {
        return noReuseHistory;
    }

    public void setNoReuseHistory(int noReuseHistory) {
        this.noReuseHistory = noReuseHistory;
    }

    public int getFailCountToLock() {
        return failCountToLock;
    }

    public void setFailCountToLock(int failCountToLock) {
        this.failCountToLock = failCountToLock;
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
        hash += (policyId != null ? policyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CnfPasswordPolicy)) {
            return false;
        }
        CnfPasswordPolicy other = (CnfPasswordPolicy) object;
        if ((this.policyId == null && other.policyId != null) || (this.policyId != null && !this.policyId.equals(other.policyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.password.policy.CnfPasswordPolicy[ policyId=" + policyId + " ]";
    }
    
}
