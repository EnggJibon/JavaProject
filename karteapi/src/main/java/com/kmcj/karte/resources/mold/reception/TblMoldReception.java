/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.reception;

import com.kmcj.karte.resources.company.MstCompany;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
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
@Table(name = "tbl_mold_reception")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldReception.findAll", query = "SELECT t FROM TblMoldReception t"),
    @NamedQuery(name = "TblMoldReception.findByUuid", query = "SELECT t FROM TblMoldReception t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblMoldReception.findByMoldId", query = "SELECT t FROM TblMoldReception t WHERE t.moldId = :moldId"),
    @NamedQuery(name = "TblMoldReception.deleteByUuid", query = "DELETE FROM TblMoldReception t WHERE t.uuid = :receptionId"),
    @NamedQuery(name = "TblMoldReception.deleteByMoldId", query = "DELETE FROM TblMoldReception t WHERE t.moldId = :moldId")
})
@Cacheable(value = false)
public class TblMoldReception implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Size(max = 45)
    @Column(name = "OWNER_COMPANY_ID")
    private String ownerCompanyId;
    @Size(max = 45)
    @Column(name = "MOLD_ID")
    private String moldId;
    @Size(max = 100)
    @Column(name = "MOLD_NAME")
    private String moldName;
    @Size(max = 45)
    @Column(name = "OTHER_COMPONENT_CODE")
    private String otherComponentCode;
    @Size(max = 100)
    @Column(name = "OTHER_COMPONENT_NAME")
    private String otherComponentName;
    @Column(name = "RECEPTION_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receptionTime;
    @Size(max = 100)
    @Column(name = "OWNER_CONTACT_NAME")
    private String ownerContactName;
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

    
    /**
     * 結合テーブル定義
     */
    // 会社マスタ
    @PrimaryKeyJoinColumn(name = "OWNER_COMPANY_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompany;
    
    public TblMoldReception() {
    }

    public TblMoldReception(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOwnerCompanyId() {
        return ownerCompanyId;
    }

    public void setOwnerCompanyId(String ownerCompanyId) {
        this.ownerCompanyId = ownerCompanyId;
    }

    public String getMoldId() {
        return moldId;
    }

    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public String getMoldName() {
        return moldName;
    }

    public void setMoldName(String moldName) {
        this.moldName = moldName;
    }

    public String getOtherComponentCode() {
        return otherComponentCode;
    }

    public void setOtherComponentCode(String otherComponentCode) {
        this.otherComponentCode = otherComponentCode;
    }

    public String getOtherComponentName() {
        return otherComponentName;
    }

    public void setOtherComponentName(String otherComponentName) {
        this.otherComponentName = otherComponentName;
    }

    public Date getReceptionTime() {
        return receptionTime;
    }

    public void setReceptionTime(Date receptionTime) {
        this.receptionTime = receptionTime;
    }

    public String getOwnerContactName() {
        return ownerContactName;
    }

    public void setOwnerContactName(String ownerContactName) {
        this.ownerContactName = ownerContactName;
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
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldReception)) {
            return false;
        }
        TblMoldReception other = (TblMoldReception) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.reception.TblMoldReception[ uuid=" + uuid + " ]";
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
    
}
