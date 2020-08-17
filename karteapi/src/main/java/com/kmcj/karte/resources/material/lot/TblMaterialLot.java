/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material.lot;

import com.kmcj.karte.resources.material.MstMaterial;
import com.kmcj.karte.resources.material.stock.TblMaterialStock;
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
@Table(name = "tbl_material_lot")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMaterialLot.findAll", query = "SELECT t FROM TblMaterialLot t"),
    @NamedQuery(name = "TblMaterialLot.findByUuid", query = "SELECT t FROM TblMaterialLot t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblMaterialLot.findByMaterialId", query = "SELECT t FROM TblMaterialLot t WHERE t.materialId = :materialId and t.stockQuantity >= 0 order by t.lotIssueDate asc"),
    @NamedQuery(name = "TblMaterialLot.findByMaterialIdAndLotNumber", query = "SELECT t FROM TblMaterialLot t WHERE t.materialId = :materialId and t.lotNo = :lotNo")
})
@Cacheable(value = false)
public class TblMaterialLot implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Size(max = 45)
    @Column(name = "LOT_NO")
    private String lotNo;
    @Basic(optional = false)
    @Column(name = "LOT_QUANTITY")
    private BigDecimal lotQuantity;
    @Basic(optional = false)
    @Column(name = "STOCK_QUANTITY")
    private BigDecimal stockQuantity;
    @Column(name = "LOT_ISSUE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lotIssueDate;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MATERIAL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private MstMaterial mstMaterial;

    @Column(name = "STATUS")
    private int status;
    @Column(name = "REMARKS01")
    private String remarks01;
    @Column(name = "REMARKS02")
    private String remarks02;
    @Column(name = "REMARKS03")
    private String remarks03;
    @Column(name = "REMARKS04")
    private String remarks04;
    @Column(name = "REMARKS05")
    private String remarks05;

    @Column(name = "MATERIAL_STOCK_ID")
    private String materialStockId;

    @PrimaryKeyJoinColumn(name = "MATERIAL_STOCK_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblMaterialStock tblMaterialStock;

    public TblMaterialLot() {
    }

    public TblMaterialLot(String uuid) {
        this.uuid = uuid;
    }

    public TblMaterialLot(String uuid, BigDecimal lotQuantity, BigDecimal stockQuantity) {
        this.uuid = uuid;
        this.lotQuantity = lotQuantity;
        this.stockQuantity = stockQuantity;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public BigDecimal getLotQuantity() {
        return lotQuantity;
    }

    public void setLotQuantity(BigDecimal lotQuantity) {
        this.lotQuantity = lotQuantity;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMaterialLot)) {
            return false;
        }
        TblMaterialLot other = (TblMaterialLot) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.material.lot.TblMaterialLot[ uuid=" + uuid + " ]";
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

    /**
     * @return the lotIssueDate
     */
    public Date getLotIssueDate() {
        return lotIssueDate;
    }

    /**
     * @param lotIssueDate the lotIssueDate to set
     */
    public void setLotIssueDate(Date lotIssueDate) {
        this.lotIssueDate = lotIssueDate;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the remarks01
     */
    public String getRemarks01() {
        return remarks01;
    }

    /**
     * @param remarks01 the remarks01 to set
     */
    public void setRemarks01(String remarks01) {
        this.remarks01 = remarks01;
    }

    /**
     * @return the remarks02
     */
    public String getRemarks02() {
        return remarks02;
    }

    /**
     * @param remarks02 the remarks02 to set
     */
    public void setRemarks02(String remarks02) {
        this.remarks02 = remarks02;
    }

    /**
     * @return the remarks03
     */
    public String getRemarks03() {
        return remarks03;
    }

    /**
     * @param remarks03 the remarks03 to set
     */
    public void setRemarks03(String remarks03) {
        this.remarks03 = remarks03;
    }

    /**
     * @return the remarks04
     */
    public String getRemarks04() {
        return remarks04;
    }

    /**
     * @param remarks04 the remarks04 to set
     */
    public void setRemarks04(String remarks04) {
        this.remarks04 = remarks04;
    }

    /**
     * @return the remarks05
     */
    public String getRemarks05() {
        return remarks05;
    }

    /**
     * @param remarks05 the remarks05 to set
     */
    public void setRemarks05(String remarks05) {
        this.remarks05 = remarks05;
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

    /**
     * @return the tblMaterialStock
     */
    public TblMaterialStock getTblMaterialStock() {
        return tblMaterialStock;
    }

    /**
     * @param tblMaterialStock the tblMaterialStock to set
     */
    public void setTblMaterialStock(TblMaterialStock tblMaterialStock) {
        this.tblMaterialStock = tblMaterialStock;
    }
    
}
