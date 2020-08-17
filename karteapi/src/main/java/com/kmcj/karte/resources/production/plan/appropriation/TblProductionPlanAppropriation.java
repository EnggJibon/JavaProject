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
@Table(name = "tbl_production_plan_appropriation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblProductionPlanAppropriation.findAll", query = "SELECT t FROM TblProductionPlanAppropriation t"),
    @NamedQuery(name = "TblProductionPlanAppropriation.findById", query = "SELECT t FROM TblProductionPlanAppropriation t WHERE t.id = :id"),
    @NamedQuery(name = "TblProductionPlanAppropriation.findByProductionPlanId", query = "SELECT t FROM TblProductionPlanAppropriation t WHERE t.productionPlanId = :productionPlanId"),
    @NamedQuery(name = "TblProductionPlanAppropriation.findByProductionDetailId", query = "SELECT t FROM TblProductionPlanAppropriation t WHERE t.productionDetailId = :productionDetailId"),
    @NamedQuery(name = "TblProductionPlanAppropriation.findByAppropriatedCount", query = "SELECT t FROM TblProductionPlanAppropriation t WHERE t.appropriatedCount = :appropriatedCount"),
    @NamedQuery(name = "TblProductionPlanAppropriation.findByCreateDate", query = "SELECT t FROM TblProductionPlanAppropriation t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblProductionPlanAppropriation.findByUpdateDate", query = "SELECT t FROM TblProductionPlanAppropriation t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblProductionPlanAppropriation.findByCreateUserUuid", query = "SELECT t FROM TblProductionPlanAppropriation t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblProductionPlanAppropriation.findByUpdateUserUuid", query = "SELECT t FROM TblProductionPlanAppropriation t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblProductionPlanAppropriation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCTION_PLAN_ID")
    private String productionPlanId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCTION_DETAIL_ID")
    private String productionDetailId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "APPROPRIATED_COUNT")
    private int appropriatedCount;
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

    public TblProductionPlanAppropriation() {
    }

    public TblProductionPlanAppropriation(String id) {
        this.id = id;
    }

    public TblProductionPlanAppropriation(String id, String productionPlanId, String productionDetailId, int appropriatedCount) {
        this.id = id;
        this.productionPlanId = productionPlanId;
        this.productionDetailId = productionDetailId;
        this.appropriatedCount = appropriatedCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductionPlanId() {
        return productionPlanId;
    }

    public void setProductionPlanId(String productionPlanId) {
        this.productionPlanId = productionPlanId;
    }

    public String getProductionDetailId() {
        return productionDetailId;
    }

    public void setProductionDetailId(String productionDetailId) {
        this.productionDetailId = productionDetailId;
    }

    public int getAppropriatedCount() {
        return appropriatedCount;
    }

    public void setAppropriatedCount(int appropriatedCount) {
        this.appropriatedCount = appropriatedCount;
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
        if (!(object instanceof TblProductionPlanAppropriation)) {
            return false;
        }
        TblProductionPlanAppropriation other = (TblProductionPlanAppropriation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.production.plan.appropriation.TblProductionPlanAppropriation[ id=" + id + " ]";
    }
    
}
