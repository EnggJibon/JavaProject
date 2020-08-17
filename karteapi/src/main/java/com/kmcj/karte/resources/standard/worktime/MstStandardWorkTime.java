/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.standard.worktime;

import com.kmcj.karte.resources.company.MstCompany;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_standard_work_time")
@XmlRootElement
@NamedQueries({
    //システムデフォルト設定(ID=system_default)のレコードを先頭に表示、それ以外は会社コード、所属の選択肢連番の昇順で表示する。
    //2016-12-27 15:15:14 for liukaisen
    @NamedQuery(name = "MstStandardWorkTime.findAll", query = "SELECT t FROM MstStandardWorkTime t LEFT OUTER JOIN MstCompany m ON t.companyId = m.id ORDER by (case when t.id = 'system_default' then 0 else 1 end), m.companyCode,t.department ASC"),
    
    @NamedQuery(name = "MstStandardWorkTime.findById", query = "SELECT m FROM MstStandardWorkTime m WHERE m.id = :id"),
    @NamedQuery(name = "MstStandardWorkTime.findBycompanyIdDepartment", query = "SELECT m FROM MstStandardWorkTime m WHERE m.companyId = :companyId AND m.department = :department"),
    @NamedQuery(name = "MstStandardWorkTime.findByDepartment", query = "SELECT m FROM MstStandardWorkTime m WHERE m.department = :department"),
    @NamedQuery(name = "MstStandardWorkTime.findByWorkTimeMinutes", query = "SELECT m FROM MstStandardWorkTime m WHERE m.workTimeMinutes = :workTimeMinutes"),
    @NamedQuery(name = "MstStandardWorkTime.findByCreateDate", query = "SELECT m FROM MstStandardWorkTime m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstStandardWorkTime.findByUpdateDate", query = "SELECT m FROM MstStandardWorkTime m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstStandardWorkTime.findByCreateUserUuid", query = "SELECT m FROM MstStandardWorkTime m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstStandardWorkTime.findByUpdateUserUuid", query = "SELECT m FROM MstStandardWorkTime m WHERE m.updateUserUuid = :updateUserUuid"),
    
    @NamedQuery(name = "MstStandardWorkTime.delete", query = "DELETE FROM MstStandardWorkTime m WHERE m.id = :id ")
})
@Cacheable(value = false)
public class MstStandardWorkTime implements Serializable {

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
    @Column(name = "DEPARTMENT")
    private Integer department;
    @Column(name = "WORK_TIME_MINUTES")
    private Integer workTimeMinutes;
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
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID" ,insertable = false, updatable = false)
    @ManyToOne
    private MstCompany mstCompany;
    
    @Column(name = "COMPANY_ID")
    private String companyId;
    
    //operationFlag
    @Transient
    private String operationFlag;
    
    //systemDefault
    @Transient
    private String systemDefault;
    
    public MstStandardWorkTime() {
    }

    public MstStandardWorkTime(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public Integer getWorkTimeMinutes() {
        return workTimeMinutes;
    }

    public void setWorkTimeMinutes(Integer workTimeMinutes) {
        this.workTimeMinutes = workTimeMinutes;
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
        if (!(object instanceof MstStandardWorkTime)) {
            return false;
        }
        MstStandardWorkTime other = (MstStandardWorkTime) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.standard.worktime.MstStandardWorkTime[ id=" + getId() + " ]";
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

    /**
     * @return the operationFlag
     */
    public String getOperationFlag() {
        return operationFlag;
    }

    /**
     * @param operationFlag the operationFlag to set
     */
    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    /**
     * @return the systemDefault
     */
    public String getSystemDefault() {
        return systemDefault;
    }

    /**
     * @param systemDefault the systemDefault to set
     */
    public void setSystemDefault(String systemDefault) {
        this.systemDefault = systemDefault;
    }
    
}
