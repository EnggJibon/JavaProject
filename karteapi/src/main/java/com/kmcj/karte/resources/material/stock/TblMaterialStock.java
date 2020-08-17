/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material.stock;

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
@Table(name = "tbl_material_stock")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMaterialStock.findAll", query = "SELECT t FROM TblMaterialStock t"),
    @NamedQuery(name = "TblMaterialStock.findByUuid", query = "SELECT t FROM TblMaterialStock t WHERE t.uuid = :uuid")
})
@Cacheable(value = false)
public class TblMaterialStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STOCK_QTY")
    private BigDecimal stockQty;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STOCK_UNIT")
    private int stockUnit;
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
    
    @Column(name = "MATERIAL_ID")
    private String materialId;
    
    @PrimaryKeyJoinColumn(name = "MATERIAL_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMaterial mstMaterial;

    public TblMaterialStock() {
    }

    public TblMaterialStock(String uuid) {
        this.uuid = uuid;
    }

    public TblMaterialStock(String uuid, BigDecimal stockQty, int stockUnit) {
        this.uuid = uuid;
        this.stockQty = stockQty;
        this.stockUnit = stockUnit;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BigDecimal getStockQty() {
        return stockQty;
    }

    public void setStockQty(BigDecimal stockQty) {
        this.stockQty = stockQty;
    }

    public int getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(int stockUnit) {
        this.stockUnit = stockUnit;
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
        if (!(object instanceof TblMaterialStock)) {
            return false;
        }
        TblMaterialStock other = (TblMaterialStock) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.material.stock.TblMaterialStock[ uuid=" + uuid + " ]";
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
