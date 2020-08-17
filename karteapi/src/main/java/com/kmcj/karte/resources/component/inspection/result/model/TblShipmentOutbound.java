/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.kmcj.karte.util.XmlDateAdapter;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author penggd
 */
@Entity
@Table(name = "tbl_shipment_outbound")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblShipmentOutbound.findAll", query = "SELECT t FROM TblShipmentOutbound t"),
    @NamedQuery(name = "TblShipmentOutbound.findByUuid", query = "SELECT t FROM TblShipmentOutbound t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblShipmentOutbound.findByPoId", query = "SELECT t FROM TblShipmentOutbound t WHERE t.poId = :poId"),
    @NamedQuery(name = "TblShipmentOutbound.findByShipDate", query = "SELECT t FROM TblShipmentOutbound t WHERE t.shipDate = :shipDate"),
    @NamedQuery(name = "TblShipmentOutbound.findByQuantity", query = "SELECT t FROM TblShipmentOutbound t WHERE t.quantity = :quantity"),
    @NamedQuery(name = "TblShipmentOutbound.findByProductionLotNumber", query = "SELECT t FROM TblShipmentOutbound t WHERE t.productionLotNumber = :productionLotNumber"),
    @NamedQuery(name = "TblShipmentOutbound.findByCreateDate", query = "SELECT t FROM TblShipmentOutbound t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblShipmentOutbound.findByUpdateDate", query = "SELECT t FROM TblShipmentOutbound t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblShipmentOutbound.findByCreateUserName", query = "SELECT t FROM TblShipmentOutbound t WHERE t.createUserName = :createUserName"),
    @NamedQuery(name = "TblShipmentOutbound.findByUpdateUserName", query = "SELECT t FROM TblShipmentOutbound t WHERE t.updateUserName = :updateUserName")})
public class TblShipmentOutbound implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PO_ID")
    private String poId;
    @Column(name = "SHIP_DATE")
    @Temporal(TemporalType.DATE)
    private Date shipDate;
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Size(max = 45)
    @Column(name = "PRODUCTION_LOT_NUMBER")
    private String productionLotNumber;
    @Size(max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_NAME")
    private String createUserName;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_NAME")
    private String updateUserName;
    
    @PrimaryKeyJoinColumn(name = "PO_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblPoOutbound tblPoOutbound;

    public TblShipmentOutbound() {
    }

    public TblShipmentOutbound(String uuid) {
        this.uuid = uuid;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
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
        if (!(object instanceof TblShipmentOutbound)) {
            return false;
        }
        TblShipmentOutbound other = (TblShipmentOutbound) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.inspection.result.model.TblShipmentOutbound[ uuid=" + uuid + " ]";
    }

    /**
     * @return the tblPoOutbound
     */
    public TblPoOutbound getTblPoOutbound() {
        return tblPoOutbound;
    }

    /**
     * @param tblPoOutbound the tblPoOutbound to set
     */
    public void setTblPoOutbound(TblPoOutbound tblPoOutbound) {
        this.tblPoOutbound = tblPoOutbound;
    }
    
}
