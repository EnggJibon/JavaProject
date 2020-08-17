/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.po;

import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.po.shipment.TblShipment;
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
@Table(name = "tbl_po")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblPo.findAll", query = "SELECT t FROM TblPo t"),
    @NamedQuery(name = "TblPo.findByUuid", query = "SELECT t FROM TblPo t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblPo.findByUniqueKey", query = "SELECT t FROM TblPo t WHERE t.deliveryDestId = :deliveryDestId "
            + " AND t.orderNumber = :orderNumber "
            + " AND t.itemNumber = :itemNumber "),
    @NamedQuery(name = "TblPo.delete", query = "DELETE FROM TblPo t WHERE t.uuid = :uuid")
})
@Cacheable(value = false)
public class TblPo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Size(max = 45)
    @Column(name = "DELIVERY_DEST_ID")
    private String deliveryDestId;
    @Size(max = 45)
    @Column(name = "ORDER_NUMBER")
    private String orderNumber;
    @Size(max = 45)
    @Column(name = "ITEM_NUMBER")
    private String itemNumber;
    @Size(max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private int quantity;
    @Column(name = "ORDER_DATE")
    @Temporal(TemporalType.DATE)
    private Date orderDate;
    @Column(name = "DUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date dueDate;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice;
    //----------------Apeng 20171205 ADD start-------------
    @Size(max = 10)
    @Column(name = "BATCH_UPDATE_STATUS")
    private String batchUpdateStatus;
    //----------------Apeng 20171205 ADD end-------------
    
    // 
    @PrimaryKeyJoinColumn(name = "UUID", referencedColumnName = "PO_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblShipment tblShipment;

    // 部品マスタ
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    
    // 会社マスタ
    @PrimaryKeyJoinColumn(name = "DELIVERY_DEST_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompany;

    public MstComponent getMstComponent() {
        return this.mstComponent;
    }

    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }
    
    public TblPo() {
    }

    public TblPo(String uuid) {
        this.uuid = uuid;
    }

    public TblPo(String uuid, int quantity) {
        this.uuid = uuid;
        this.quantity = quantity;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDeliveryDestId() {
        return deliveryDestId;
    }

    public void setDeliveryDestId(String deliveryDestId) {
        this.deliveryDestId = deliveryDestId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
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
        if (!(object instanceof TblPo)) {
            return false;
        }
        TblPo other = (TblPo) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.po.TblPo[ uuid=" + uuid + " ]";
    }

    /**
     * @return the mstCompany
     */
    public MstCompany getMstCompany() {
        return mstCompany;
    }

    /**
     * @param mstCompany the mstCompany to set
     */
    public void setMstCompany(MstCompany mstCompany) {
        this.mstCompany = mstCompany;
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
     * @return the unitPrice
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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
