/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.machine.proc.cond;

import com.kmcj.karte.resources.production.detail.TblProductionDetail;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "tbl_production_machine_proc_cond")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblProductionMachineProcCond.findAll", query = "SELECT t FROM TblProductionMachineProcCond t"),
    @NamedQuery(name = "TblProductionMachineProcCond.findById", query = "SELECT t FROM TblProductionMachineProcCond t WHERE t.id = :id"),
    @NamedQuery(name = "TblProductionMachineProcCond.findByAttrValue", query = "SELECT t FROM TblProductionMachineProcCond t WHERE t.attrValue = :attrValue"),
    @NamedQuery(name = "TblProductionMachineProcCond.findByMasterAttrValue", query = "SELECT t FROM TblProductionMachineProcCond t WHERE t.masterAttrValue = :masterAttrValue"),
    @NamedQuery(name = "TblProductionMachineProcCond.findByCreateDate", query = "SELECT t FROM TblProductionMachineProcCond t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblProductionMachineProcCond.findByUpdateDate", query = "SELECT t FROM TblProductionMachineProcCond t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblProductionMachineProcCond.findByCreateUserUuid", query = "SELECT t FROM TblProductionMachineProcCond t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblProductionMachineProcCond.findByUpdateUserUuid", query = "SELECT t FROM TblProductionMachineProcCond t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblProductionMachineProcCond.deleteByDetailId", query = "DELETE FROM TblProductionMachineProcCond t WHERE t.productionDetailId = :productionDetailId"),
})
@Cacheable(value = false)
public class TblProductionMachineProcCond implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 100)
    @Column(name = "ATTR_VALUE")
    private String attrValue;
    @Size(max = 100)
    @Column(name = "MASTER_ATTR_VALUE")
    private String masterAttrValue;
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

    @Size(max = 45)
    @Column(name = "PRODUCTION_DETAIL_ID")
    private String productionDetailId;
    @Size(max = 45)
    @Column(name = "ATTR_ID")
    private String attrId;

    @JoinColumn(name = "PRODUCTION_DETAIL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private TblProductionDetail tblProductionDetail;
        

    public TblProductionMachineProcCond() {
    }

    public TblProductionMachineProcCond(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getMasterAttrValue() {
        return masterAttrValue;
    }

    public void setMasterAttrValue(String masterAttrValue) {
        this.masterAttrValue = masterAttrValue;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblProductionMachineProcCond)) {
            return false;
        }
        TblProductionMachineProcCond other = (TblProductionMachineProcCond) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.production.machine.proc.cond.TblProductionMachineProcCond[ id=" + id + " ]";
    }

    /**
     * @return the productionDetailId
     */
    public String getProductionDetailId() {
        return productionDetailId;
    }

    /**
     * @param productionDetailId the productionDetailId to set
     */
    public void setProductionDetailId(String productionDetailId) {
        this.productionDetailId = productionDetailId;
    }

    /**
     * @return the attrId
     */
    public String getAttrId() {
        return attrId;
    }

    /**
     * @param attrId the attrId to set
     */
    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    /**
     * @return the tblProductionDetail
     */
    @XmlTransient
    public TblProductionDetail getTblProductionDetail() {
        return tblProductionDetail;
    }

    /**
     * @param tblProductionDetail the tblProductionDetail to set
     */
    public void setTblProductionDetail(TblProductionDetail tblProductionDetail) {
        this.tblProductionDetail = tblProductionDetail;
    }
    
}
