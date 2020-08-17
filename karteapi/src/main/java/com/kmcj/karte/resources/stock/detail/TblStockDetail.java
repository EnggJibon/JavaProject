/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.stock.detail;

import com.kmcj.karte.resources.component.lot.TblComponentLot;
import com.kmcj.karte.resources.po.TblPo;
import com.kmcj.karte.resources.po.shipment.TblShipment;
import com.kmcj.karte.resources.stock.TblStock;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "tbl_stock_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblStockDetail.findAll", query = "SELECT t FROM TblStockDetail t"),
    @NamedQuery(name = "TblStockDetail.findByUuid", query = "SELECT t FROM TblStockDetail t WHERE t.uuid = :uuid")
})
@Cacheable(value = false)
public class TblStockDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Size(max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Size(max = 45)
    @Column(name = "PROCEDURE_CODE")
    private String procedureCode;
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
    private long moveQuantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MOVE_COST")
    private BigDecimal moveCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STOCK_QUANTITY")
    private long stockQuantity;
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
    @Column(name = "COMPONENT_LOT_ID")
    private String componentLotId;

    @PrimaryKeyJoinColumn(name = "COMPONENT_LOT_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblComponentLot tblComponentLot;

    @Column(name = "STOCK_ID")
    private String stockId;

    @PrimaryKeyJoinColumn(name = "STOCK_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblStock tblStock;

    @Column(name = "SHIPMENT_ID")
    private String shipmentId;

    @PrimaryKeyJoinColumn(name = "SHIPMENT_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblShipment tblShipment;

    public TblStockDetail() {
    }

    public TblStockDetail(String uuid) {
        this.uuid = uuid;
    }

    public TblStockDetail(String uuid, int stockType, int moveQuantity, BigDecimal moveCost, int stockQuantity) {
        this.uuid = uuid;
        this.stockType = stockType;
        this.moveQuantity = moveQuantity;
        this.moveCost = moveCost;
        this.stockQuantity = stockQuantity;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
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

    public long getMoveQuantity() {
        return moveQuantity;
    }

    public void setMoveQuantity(long moveQuantity) {
        this.moveQuantity = moveQuantity;
    }

    public BigDecimal getMoveCost() {
        return moveCost;
    }

    public void setMoveCost(BigDecimal moveCost) {
        this.moveCost = moveCost;
    }

    public long getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(long stockQuantity) {
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

        if (!(object instanceof TblStockDetail)) {
            return false;
        }
        TblStockDetail other = (TblStockDetail) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.stock.detail.TblStockDetail[ uuid=" + uuid + " ]";
    }

    /**
     * @return the stockId
     */
    public String getStockId() {
        return stockId;
    }

    /**
     * @param stockId the stockId to set
     */
    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    /**
     * @return the tblStock
     */
    public TblStock getTblStock() {
        return tblStock;
    }

    /**
     * @param tblStock the tblStock to set
     */
    public void setTblStock(TblStock tblStock) {
        this.tblStock = tblStock;
    }

    /**
     * @return the shipmentId
     */
    public String getShipmentId() {
        return shipmentId;
    }

    /**
     * @param shipmentId the shipmentId to set
     */
    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    /**
     * @return the tblShipment
     */
    public TblShipment getTblShipment() {
        return tblShipment;
    }

    /**
     * @param tblShipment the tblShipment to set
     */
    public void setTblShipment(TblShipment tblShipment) {
        this.tblShipment = tblShipment;
    }

    /**
     * @return the componentLotId
     */
    public String getComponentLotId() {
        return componentLotId;
    }

    /**
     * @param componentLotId the componentLotId to set
     */
    public void setComponentLotId(String componentLotId) {
        this.componentLotId = componentLotId;
    }

    /**
     * @return the tblComponentLot
     */
    public TblComponentLot getTblComponentLot() {
        return tblComponentLot;
    }

    /**
     * @param tblComponentLot the tblComponentLot to set
     */
    public void setTblComponentLot(TblComponentLot tblComponentLot) {
        this.tblComponentLot = tblComponentLot;
    }

}
