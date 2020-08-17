/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.po.shipment;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.po.TblPo;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import java.io.Serializable;
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
@Table(name = "tbl_shipment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblShipment.findAll", query = "SELECT t FROM TblShipment t"),
    @NamedQuery(name = "TblShipment.findByUuid", query = "SELECT t FROM TblShipment t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblShipment.delete", query = "DELETE FROM TblShipment t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblShipment.findByPoId", query = "SELECT t FROM TblShipment t WHERE t.poId = :poId")
})
@Cacheable(value = false)
public class TblShipment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Column(name = "PO_ID")
    private String poId;
    @Column(name = "SHIP_DATE")
    @Temporal(TemporalType.DATE)
    private Date shipDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private int quantity;
    @Size(max = 45)
    @Column(name = "PRODUCTION_LOT_NUMBER")
    private String productionLotNumber;
    @Column(name = "COMPONENT_ID")
    private String componentId;
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
    //----------------Apeng 20171205 ADD start-------------
    @Size(max = 10)
    @Column(name = "BATCH_UPDATE_STATUS")
    private String batchUpdateStatus;
    //----------------Apeng 20171205 ADD end-------------
    
    @PrimaryKeyJoinColumn(name = "PO_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblPo tblPo;

    /**
     * 結合テーブル定義
     */
    // 生産実績明細
    @PrimaryKeyJoinColumn(name = "PRODUCTION_DETAIL_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblProductionDetail tblProductionDetail;

    public TblProductionDetail getTblProductionDetail() {
        return this.tblProductionDetail;
    }

    public void setTblProductionDetail(TblProductionDetail tblProductionDetail) {
        this.tblProductionDetail = tblProductionDetail;
    }

    // 部品マスタ
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;

    public MstComponent getMstComponent() {
        return this.mstComponent;
    }

    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    public TblShipment() {
    }

    public TblShipment(String uuid) {
        this.uuid = uuid;
    }

    public TblShipment(String uuid, int quantity) {
        this.uuid = uuid;
        this.quantity = quantity;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductionLotNumber() {
        return productionLotNumber;
    }

    public void setProductionLotNumber(String productionLotNumber) {
        this.productionLotNumber = productionLotNumber;
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
        if (!(object instanceof TblShipment)) {
            return false;
        }
        TblShipment other = (TblShipment) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.po.shipment.TblShipment[ uuid=" + uuid + " ]";
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
     * @return the poId
     */
    public String getPoId() {
        return poId;
    }

    /**
     * @param poId the poId to set
     */
    public void setPoId(String poId) {
        this.poId = poId;
    }
    
    /**
     * @return the tblPo
     */
    public TblPo getTblPo() {
        return tblPo;
    }

    /**
     * @param tblPo the tblPo to set
     */
    public void setTblPo(TblPo tblPo) {
        this.tblPo = tblPo;
    }

    /**
     * @return the batchUpdateStatus
     */
    public String getBatchUpdateStatus() {
        return batchUpdateStatus;
    }

    /**
     * @param batchUpdateStatus the batchUpdateStatus to set
     */
    public void setBatchUpdateStatus(String batchUpdateStatus) {
        this.batchUpdateStatus = batchUpdateStatus;
    }

}
