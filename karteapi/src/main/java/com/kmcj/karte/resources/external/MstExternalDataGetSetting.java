/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.external;

import com.kmcj.karte.resources.company.MstCompany;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_external_data_get_setting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstExternalDataGetSetting.findAll", query = "SELECT m FROM MstExternalDataGetSetting m"),
    @NamedQuery(name = "MstExternalDataGetSetting.findById", query = "SELECT m FROM MstExternalDataGetSetting m WHERE m.id = :id"),
    @NamedQuery(name = "MstExternalDataGetSetting.findByUserId", query = "SELECT m FROM MstExternalDataGetSetting m WHERE m.userId = :userId"),
    @NamedQuery(name = "MstExternalDataGetSetting.findByEncryptedPassword", query = "SELECT m FROM MstExternalDataGetSetting m WHERE m.encryptedPassword = :encryptedPassword"),
    @NamedQuery(name = "MstExternalDataGetSetting.findByApiBaseUrl", query = "SELECT m FROM MstExternalDataGetSetting m WHERE m.apiBaseUrl = :apiBaseUrl"),
    @NamedQuery(name = "MstExternalDataGetSetting.findMoldByExternalDataCompanyId", query = "SELECT m FROM MstMold m WHERE m.companyId = :companyId "),
    @NamedQuery(name = "MstExternalDataGetSetting.findMachineByExternalDataCompanyId", query = "SELECT m FROM MstMachine m WHERE m.companyId = :companyId "),
    @NamedQuery(name = "MstExternalDataGetSetting.findByValidFlg", query = "SELECT DISTINCT e FROM MstExternalDataGetSetting e JOIN MstMold m ON m.companyId = e.companyId WHERE e.validFlg = :validFlg ORDER BY e.latestExecutedDate asc "),
    @NamedQuery(name = "MstExternalDataGetSetting.findMachinesByValidFlg", query = "SELECT DISTINCT e FROM MstExternalDataGetSetting e JOIN MstMachine m ON m.companyId = e.companyId WHERE e.validFlg = :validFlg ORDER BY e.latestExecutedDate asc "),
    @NamedQuery(name = "MstExternalDataGetSetting.findByLatestExecutedDate", query = "SELECT m FROM MstExternalDataGetSetting m WHERE m.latestExecutedDate = :latestExecutedDate"),
    @NamedQuery(name = "MstExternalDataGetSetting.findByCreateDate", query = "SELECT m FROM MstExternalDataGetSetting m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstExternalDataGetSetting.findByUpdateDate", query = "SELECT m FROM MstExternalDataGetSetting m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstExternalDataGetSetting.findByCreateUserUuid", query = "SELECT m FROM MstExternalDataGetSetting m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstExternalDataGetSetting.findByCompanyId", query = "SELECT m FROM MstExternalDataGetSetting m WHERE m.companyId = :companyId"),
    @NamedQuery(name = "MstExternalDataGetSetting.findByUpdateUserUuid", query = "SELECT m FROM MstExternalDataGetSetting m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstExternalDataGetSetting.deleteExternal", query = "DELETE FROM MstExternalDataGetSetting m WHERE m.id = :id ")
})
@Cacheable(value = false)
public class MstExternalDataGetSetting implements Serializable {

    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "USER_ID")
    private String userId;
    @Size(max = 256)
    @Column(name = "ENCRYPTED_PASSWORD")
    private String encryptedPassword;
    @Size(max = 256)
    @Column(name = "API_BASE_URL")
    private String apiBaseUrl;
    @Column(name = "VALID_FLG")
    private Integer validFlg;
    @Column(name = "LATEST_EXECUTED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date latestExecutedDate;
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
    
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompany;
    
    @Size(max = 45)
    @Column(name = "COMPANY_ID")
    private String companyId;

    public MstExternalDataGetSetting() {
    }

    public MstExternalDataGetSetting(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public Integer getValidFlg() {
        return validFlg;
    }

    public void setValidFlg(Integer validFlg) {
        this.validFlg = validFlg;
    }

    public Date getLatestExecutedDate() {
        return latestExecutedDate;
    }

    public void setLatestExecutedDate(Date latestExecutedDate) {
        this.latestExecutedDate = latestExecutedDate;
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
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstExternalDataGetSetting)) {
            return false;
        }
        MstExternalDataGetSetting other = (MstExternalDataGetSetting) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.external.MstExternalDataGetSetting[ id=" + getId() + " ]";
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
    
}
