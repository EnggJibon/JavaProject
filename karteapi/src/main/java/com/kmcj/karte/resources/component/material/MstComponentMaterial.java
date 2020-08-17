/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.material;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.material.MstMaterial;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "mst_component_material")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstComponentMaterial.findAll", query = "SELECT m FROM MstComponentMaterial m"),
    @NamedQuery(name = "MstComponentMaterial.findById", query = "SELECT m FROM MstComponentMaterial m WHERE m.id = :id"),
    @NamedQuery(name = "MstComponentMaterial.findByComponentId", query = "SELECT m FROM MstComponentMaterial m WHERE m.componentId = :componentId"),
    @NamedQuery(name = "MstComponentMaterial.findByProcedureCode", query = "SELECT m FROM MstComponentMaterial m WHERE m.proceduerCode = :proceduerCode"),
    @NamedQuery(name = "MstComponentMaterial.findByMaterialId", query = "SELECT m FROM MstComponentMaterial m WHERE m.materialId = :materialId"),
    @NamedQuery(name = "MstComponentMaterial.findByNumerator", query = "SELECT m FROM MstComponentMaterial m WHERE m.numerator = :numerator"),
    @NamedQuery(name = "MstComponentMaterial.findByDenominator", query = "SELECT m FROM MstComponentMaterial m WHERE m.denominator = :denominator"),
    @NamedQuery(name = "MstComponentMaterial.findByStartDate", query = "SELECT m FROM MstComponentMaterial m WHERE m.startDate = :startDate"),
    @NamedQuery(name = "MstComponentMaterial.findByEndDate", query = "SELECT m FROM MstComponentMaterial m WHERE m.endDate = :endDate"),
    @NamedQuery(name = "MstComponentMaterial.findByCreateDate", query = "SELECT m FROM MstComponentMaterial m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstComponentMaterial.findByUpdateDate", query = "SELECT m FROM MstComponentMaterial m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstComponentMaterial.findByCreateUserUuid", query = "SELECT m FROM MstComponentMaterial m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstComponentMaterial.findByUpdateUserUuid", query = "SELECT m FROM MstComponentMaterial m WHERE m.updateUserUuid = :updateUserUuid"),
    //DELETE
    @NamedQuery(name = "MstComponentMaterial.delete", query = "DELETE FROM MstComponentMaterial m WHERE m.id = :id"),
    @NamedQuery(name = "MstComponentMaterial.findByComponentIdAndMaterialIdAndProcedureCode", query = "SELECT m FROM MstComponentMaterial m WHERE m.componentId = :componentId AND m.proceduerCode = :proceduerCode AND m.materialId = :materialId "),
    @NamedQuery(name = "MstComponentMaterial.findByUniqueKey", 
            query = "SELECT m FROM MstComponentMaterial m WHERE m.componentId = :componentId AND m.proceduerCode = :proceduerCode AND m.materialId = :materialId AND m.startDate = :startDate ")
})
@Cacheable(value = false)
public class MstComponentMaterial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "PROCEDUER_CODE")
    private String proceduerCode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "NUMERATOR")
    private BigDecimal numerator;
    @Column(name = "DENOMINATOR")
    private BigDecimal denominator;
    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;
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
    
    @Column(name = "COMPONENT_ID")
    private String componentId;
    
    @Column(name = "MATERIAL_ID")
    private String materialId;
    
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID" ,insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    
    @JoinColumn(name = "MATERIAL_ID", referencedColumnName = "ID" ,insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMaterial mstMaterial;
    
    public MstComponentMaterial() {
    }

    public MstComponentMaterial(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProceduerCode() {
        return proceduerCode;
    }

    public void setProceduerCode(String proceduerCode) {
        this.proceduerCode = proceduerCode;
    }

    public BigDecimal getNumerator() {
        return numerator;
    }

    public void setNumerator(BigDecimal numerator) {
        this.numerator = numerator;
    }

    public BigDecimal getDenominator() {
        return denominator;
    }

    public void setDenominator(BigDecimal denominator) {
        this.denominator = denominator;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
        if (!(object instanceof MstComponentMaterial)) {
            return false;
        }
        MstComponentMaterial other = (MstComponentMaterial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.material.MstComponentMaterial[ id=" + id + " ]";
    }

    /**
     * @return the componentId
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * @param componentId the componentId to set
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the materialId
     */
    public String getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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
     * @return the mstMaterial
     */
    public MstMaterial getMstMaterial() {
        return mstMaterial;
    }

    /**
     * @param mstMaterial the mstMaterial to set
     */
    public void setMstMaterial(MstMaterial mstMaterial) {
        this.mstMaterial = mstMaterial;
    }
}
