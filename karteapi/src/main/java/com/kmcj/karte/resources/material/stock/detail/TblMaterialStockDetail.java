/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material.stock.detail;

import com.kmcj.karte.resources.material.MstMaterial;
import com.kmcj.karte.resources.material.lot.TblMaterialLot;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
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
@Table(name = "tbl_material_stock_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMaterialStockDetail.findAll", query = "SELECT t FROM TblMaterialStockDetail t"),
    @NamedQuery(name = "TblMaterialStockDetail.findByUuid", query = "SELECT t FROM TblMaterialStockDetail t WHERE t.uuid = :uuid")
})
@Cacheable(value = false)
public class TblMaterialStockDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Size(max = 45)
    @Column(name = "MATERIAL_LOT_NO")
    private String materialLotNo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STOCK_TYPE")
    private int stockType;
    @Column(name = "MOVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date moveDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MOVE_QUANTITY")
    private BigDecimal moveQuantity;
    @Basic(optional = false)
    @Column(name = "STOCK_QUANTITY")
    private BigDecimal stockQuantity;
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

    @Column(name = "MATERIAL_LOT_ID")
    private String materialLotId;

    @Column(name = "PRODUCTION_DETAIL_ID")
    private String productionDetailId;
    
    @Size(max = 45)
    @Column(name = "MATERIAL_STOCK_ID")
    private String materialStockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MATERIAL_LOT_ID", referencedColumnName = "UUID", insertable = false, updatable = false)
    private TblMaterialLot tblMaterialLot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MATERIAL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private MstMaterial mstMaterial;

    @JoinColumn(name = "PRODUCTION_DETAIL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblProductionDetail tblProductionDetail;

    public TblMaterialStockDetail() {
    }

    public TblMaterialStockDetail(String uuid) {
        this.uuid = uuid;
    }

    public TblMaterialStockDetail(String uuid, int stockType, BigDecimal moveQuantity, BigDecimal stockQuantity) {
        this.uuid = uuid;
        this.stockType = stockType;
        this.moveQuantity = moveQuantity;
        this.stockQuantity = stockQuantity;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMaterialLotNo() {
        return materialLotNo;
    }

    public void setMaterialLotNo(String materialLotNo) {
        this.materialLotNo = materialLotNo;
    }

    public int getStockType() {
        return stockType;
    }

    public void setStockType(int stockType) {
        this.stockType = stockType;
    }

    public Date getMoveDate() {
        return moveDate;
    }

    public void setMoveDate(Date moveDate) {
        this.moveDate = moveDate;
    }

    public BigDecimal getMoveQuantity() {
        return moveQuantity;
    }

    public void setMoveQuantity(BigDecimal moveQuantity) {
        this.moveQuantity = moveQuantity;
    }

    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
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
        if (!(object instanceof TblMaterialStockDetail)) {
            return false;
        }
        TblMaterialStockDetail other = (TblMaterialStockDetail) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.material.detail.TblMaterialStockDetail[ uuid=" + uuid + " ]";
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
     * @return the materialLotId
     */
    public String getMaterialLotId() {
        return materialLotId;
    }

    /**
     * @param materialLotId the materialLotId to set
     */
    public void setMaterialLotId(String materialLotId) {
        this.materialLotId = materialLotId;
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
     * @return the tblMaterialLot
     */
    public TblMaterialLot getTblMaterialLot() {
        return tblMaterialLot;
    }

    /**
     * @param tblMaterialLot the tblMaterialLot to set
     */
    public void setTblMaterialLot(TblMaterialLot tblMaterialLot) {
        this.tblMaterialLot = tblMaterialLot;
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

    /**
     * @return the tblProductionDetail
     */
    public TblProductionDetail getTblProductionDetail() {
        return tblProductionDetail;
    }

    /**
     * @param tblProductionDetail the tblProductionDetail to set
     */
    public void setTblProductionDetail(TblProductionDetail tblProductionDetail) {
        this.tblProductionDetail = tblProductionDetail;
    }

    /**
     * @return the materialStockId
     */
    public String getMaterialStockId() {
        return materialStockId;
    }

    /**
     * @param materialStockId the materialStockId to set
     */
    public void setMaterialStockId(String materialStockId) {
        this.materialStockId = materialStockId;
    }
    
}
