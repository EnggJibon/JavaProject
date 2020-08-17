/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.defect;

import com.kmcj.karte.resources.production2.ProductionDetail;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 */
@Entity
@Table(name = "tbl_production_defect")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblProductionDefect.findAll", query = "SELECT t FROM TblProductionDefect t"),
    @NamedQuery(name = "TblProductionDefect.findById", query = "SELECT t FROM TblProductionDefect t WHERE t.id = :id"),
    @NamedQuery(name = "TblProductionDefect.findByProductionDetailId", query = "SELECT t FROM TblProductionDefect t WHERE t.productionDetailId = :productionDetailId"),
    @NamedQuery(name = "TblProductionDefect.findByDefectSeq", query = "SELECT t FROM TblProductionDefect t WHERE t.defectSeq = :defectSeq"),
    @NamedQuery(name = "TblProductionDefect.findByQuantity", query = "SELECT t FROM TblProductionDefect t WHERE t.quantity = :quantity"),
    @NamedQuery(name = "TblProductionDefect.findByCreateDate", query = "SELECT t FROM TblProductionDefect t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblProductionDefect.findByUpdateDate", query = "SELECT t FROM TblProductionDefect t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblProductionDefect.findByCreateUserUuid", query = "SELECT t FROM TblProductionDefect t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblProductionDefect.findByUpdateUserUuid", query = "SELECT t FROM TblProductionDefect t WHERE t.updateUserUuid = :updateUserUuid")})
public class TblProductionDefect implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @Column(name = "PRODUCTION_DETAIL_ID")
    private String productionDetailId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_SEQ")
    private int defectSeq;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private int quantity;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productionDefectId")
    private Collection<TblProductionDefectDaily> tblProductionDefectDailyCollection;
    @JoinColumn(name = "PRODUCTION_DETAIL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductionDetail productionDetail;

    public TblProductionDefect() {
    }

    public TblProductionDefect(String id) {
        this.id = id;
    }

    public TblProductionDefect(String id, int defectSeq, int quantity) {
        this.id = id;
        this.defectSeq = defectSeq;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductionDetailId() {
        return productionDetailId;
    }

    public void setProductionDetailId(String productionDetailId) {
        this.productionDetailId = productionDetailId;
    }
    
    public int getDefectSeq() {
        return defectSeq;
    }

    public void setDefectSeq(int defectSeq) {
        this.defectSeq = defectSeq;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    @XmlTransient
    public Collection<TblProductionDefectDaily> getTblProductionDefectDailyCollection() {
        return tblProductionDefectDailyCollection;
    }

    public void setTblProductionDefectDailyCollection(Collection<TblProductionDefectDaily> tblProductionDefectDailyCollection) {
        this.tblProductionDefectDailyCollection = tblProductionDefectDailyCollection;
    }
    
    public ProductionDetail getProductionDetail() {
        return productionDetail;
    }

    public void setProductionDetail(ProductionDetail productionDetail) {
        this.productionDetail = productionDetail;
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
        if (!(object instanceof TblProductionDefect)) {
            return false;
        }
        TblProductionDefect other = (TblProductionDefect) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.production.defect.TblProductionDefect[ id=" + id + " ]";
    }
    
}
