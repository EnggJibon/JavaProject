/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.company;

import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.component.MstComponent;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_component_company")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstComponentCompany.findAll", query = "SELECT m FROM MstComponentCompany m"),
    //外部連携バッチ用
    @NamedQuery(name = "MstComponentCompany.findMstComponentCompanyByBatch", query = "SELECT mstComponentCompany FROM MstComponentCompany mstComponentCompany"
            + " JOIN FETCH mstComponentCompany.mstComponent "
            + " JOIN MstApiUser apiUser ON mstComponentCompany.mstComponentCompanyPK.companyId = apiUser.companyId " // PAIユーザ同じの会社
            + " WHERE 1 = 1 "
            + " AND mstComponentCompany.mstComponentCompanyPK.companyId = :companyId "
            + " AND apiUser.userId = :apiUserId "),
    
    @NamedQuery(name = "MstComponentCompany.findByComponentId", query = "SELECT m FROM MstComponentCompany m JOIN FETCH m.mstCompany WHERE m.mstComponentCompanyPK.componentId = :componentId"),
    @NamedQuery(name = "MstComponentCompany.findByCompanyId", query = "SELECT m FROM MstComponentCompany m WHERE m.mstComponentCompanyPK.companyId = :companyId"),
    @NamedQuery(name = "MstComponentCompany.deleteByComponentId", query = "DELETE FROM MstComponentCompany m WHERE m.mstComponentCompanyPK.componentId = :componentId"),
    @NamedQuery(name = "MstComponentCompany.findByCompanyIdAndComponentCode", query = "SELECT m FROM MstComponentCompany m WHERE m.mstComponentCompanyPK.companyId = :companyId AND m.otherComponentCode = :componentCode"),
    @NamedQuery(name = "MstComponentCompany.findByPk", query = "SELECT m FROM MstComponentCompany m WHERE m.mstComponentCompanyPK.companyId = :companyId AND m.mstComponentCompanyPK.componentId = :componentId"),
    @NamedQuery(name = "MstComponentCompany.deleteComponentCompanyByPK", query = "DELETE FROM MstComponentCompany m WHERE m.mstComponentCompanyPK.companyId = :companyId AND m.mstComponentCompanyPK.componentId = :componentId")
})
@Cacheable(value = false)
public class MstComponentCompany implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstComponentCompanyPK mstComponentCompanyPK;
    @Size(max = 100)
    @Column(name = "OTHER_COMPONENT_CODE")
    private String otherComponentCode;
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
    @PrimaryKeyJoinColumn(name = "COMPANY_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompany;
    //部品
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    
    public MstComponentCompany() {
    }

    public MstComponentCompany(MstComponentCompanyPK mstComponentCompanyPK) {
        this.mstComponentCompanyPK = mstComponentCompanyPK;
    }

    public MstComponentCompany(String componentId, String companyId) {
        this.mstComponentCompanyPK = new MstComponentCompanyPK(componentId, companyId);
    }

    public MstComponentCompanyPK getMstComponentCompanyPK() {
        return mstComponentCompanyPK;
    }

    public void setMstComponentCompanyPK(MstComponentCompanyPK mstComponentCompanyPK) {
        this.mstComponentCompanyPK = mstComponentCompanyPK;
    }

    public String getOtherComponentCode() {
        return otherComponentCode;
    }

    public void setOtherComponentCode(String otherComponentCode) {
        this.otherComponentCode = otherComponentCode;
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
        hash += (mstComponentCompanyPK != null ? mstComponentCompanyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstComponentCompany)) {
            return false;
        }
        MstComponentCompany other = (MstComponentCompany) object;
        if ((this.mstComponentCompanyPK == null && other.mstComponentCompanyPK != null) || (this.mstComponentCompanyPK != null && !this.mstComponentCompanyPK.equals(other.mstComponentCompanyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.company.MstComponentCompany[ mstComponentCompanyPK=" + mstComponentCompanyPK + " ]";
    }

    /**
     * @return the mstComponent
     */
    public MstComponent getMstComponent() {
        return mstComponent;
    }

    /**
     * @param mstComponent the mstComponent to set
     */
    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
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
