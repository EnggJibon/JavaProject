/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.plan.appropriation;

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
 * @author f.kitaoji
 */
@Entity
@Table(name = "tbl_production_plan_deduction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblProductionPlanDeduction.findAll", query = "SELECT t FROM TblProductionPlanDeduction t"),
    @NamedQuery(name = "TblProductionPlanDeduction.findByProductionPlanAppropriationId", query = "SELECT t FROM TblProductionPlanDeduction t WHERE t.productionPlanAppropriationId = :productionPlanAppropriationId"),
    @NamedQuery(name = "TblProductionPlanDeduction.findByProductionDetailId", query = "SELECT t FROM TblProductionPlanDeduction t WHERE t.productionDetailId = :productionDetailId"),
    @NamedQuery(name = "TblProductionPlanDeduction.findByCreateDate", query = "SELECT t FROM TblProductionPlanDeduction t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblProductionPlanDeduction.findByUpdateDate", query = "SELECT t FROM TblProductionPlanDeduction t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblProductionPlanDeduction.findByCreateUserUuid", query = "SELECT t FROM TblProductionPlanDeduction t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblProductionPlanDeduction.findByUpdateUserUuid", query = "SELECT t FROM TblProductionPlanDeduction t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblProductionPlanDeduction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCTION_PLAN_APPROPRIATION_ID")
    private String productionPlanAppropriationId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCTION_DETAIL_ID")
    private String productionDetailId;
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

    public TblProductionPlanDeduction() {
    }

    public TblProductionPlanDeduction(String productionPlanAppropriationId) {
        this.productionPlanAppropriationId = productionPlanAppropriationId;
    }

    public TblProductionPlanDeduction(String productionPlanAppropriationId, String productionDetailId) {
        this.productionPlanAppropriationId = productionPlanAppropriationId;
        this.productionDetailId = productionDetailId;
    }

    public String getProductionPlanAppropriationId() {
        return productionPlanAppropriationId;
    }

    public void setProductionPlanAppropriationId(String productionPlanAppropriationId) {
        this.productionPlanAppropriationId = productionPlanAppropriationId;
    }

    public String getProductionDetailId() {
        return productionDetailId;
    }

    public void setProductionDetailId(String productionDetailId) {
        this.productionDetailId = productionDetailId;
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
        hash += (productionPlanAppropriationId != null ? productionPlanAppropriationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblProductionPlanDeduction)) {
            return false;
        }
        TblProductionPlanDeduction other = (TblProductionPlanDeduction) object;
        if ((this.productionPlanAppropriationId == null && other.productionPlanAppropriationId != null) || (this.productionPlanAppropriationId != null && !this.productionPlanAppropriationId.equals(other.productionPlanAppropriationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.production.plan.appropriation.TblProductionPlanDeduction[ productionPlanAppropriationId=" + productionPlanAppropriationId + " ]";
    }
    
}
