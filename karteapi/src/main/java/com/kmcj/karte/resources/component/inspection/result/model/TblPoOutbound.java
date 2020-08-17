/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author penggd
 */
@Entity
@Table(name = "tbl_po_outbound")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblPoOutbound.findAll", query = "SELECT t FROM TblPoOutbound t"),
    @NamedQuery(name = "TblPoOutbound.findByUuid", query = "SELECT t FROM TblPoOutbound t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblPoOutbound.findByDeliverySrcId", query = "SELECT t FROM TblPoOutbound t WHERE t.deliverySrcId = :deliverySrcId"),
    @NamedQuery(name = "TblPoOutbound.findByOrderNo", query = "SELECT t FROM TblPoOutbound t WHERE t.orderNo = :orderNo"),
    @NamedQuery(name = "TblPoOutbound.findByItemNo", query = "SELECT t FROM TblPoOutbound t WHERE t.itemNo = :itemNo"),
    @NamedQuery(name = "TblPoOutbound.findByUniqueKey", query = "SELECT t FROM TblPoOutbound t WHERE t.deliverySrcId = :deliverySrcId AND t.orderNo = :orderNo AND t.itemNo = :itemNo"),
    @NamedQuery(name = "TblPoOutbound.findByQuantity", query = "SELECT t FROM TblPoOutbound t WHERE t.quantity = :quantity"),
    @NamedQuery(name = "TblPoOutbound.findByUnitPrice", query = "SELECT t FROM TblPoOutbound t WHERE t.unitPrice = :unitPrice"),
    @NamedQuery(name = "TblPoOutbound.findByOrderDate", query = "SELECT t FROM TblPoOutbound t WHERE t.orderDate = :orderDate"),
    @NamedQuery(name = "TblPoOutbound.findByDueDate", query = "SELECT t FROM TblPoOutbound t WHERE t.dueDate = :dueDate"),
    @NamedQuery(name = "TblPoOutbound.findByCreateDate", query = "SELECT t FROM TblPoOutbound t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblPoOutbound.findByUpdateDate", query = "SELECT t FROM TblPoOutbound t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblPoOutbound.findByCreateUserName", query = "SELECT t FROM TblPoOutbound t WHERE t.createUserName = :createUserName"),
    @NamedQuery(name = "TblPoOutbound.findByUpdateUserName", query = "SELECT t FROM TblPoOutbound t WHERE t.updateUserName = :updateUserName")})
public class TblPoOutbound implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DELIVERY_SRC_ID")
    private String deliverySrcId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ORDER_NO")
    private String orderNo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ITEM_NO")
    private String itemNo;
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private int quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice;
    @Column(name = "ORDER_DATE")
    @Temporal(TemporalType.DATE)
    private Date orderDate;
    @Column(name = "DUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date dueDate;
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


    public TblPoOutbound() {
    }

    public TblPoOutbound(String uuid) {
        this.uuid = uuid;
    }

    public TblPoOutbound(String uuid, String deliverySrcId, String orderNo, String itemNo, int quantity, BigDecimal unitPrice) {
        this.uuid = uuid;
        this.deliverySrcId = deliverySrcId;
        this.orderNo = orderNo;
        this.itemNo = itemNo;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDeliverySrcId() {
        return deliverySrcId;
    }

    public void setDeliverySrcId(String deliverySrcId) {
        this.deliverySrcId = deliverySrcId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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
        if (!(object instanceof TblPoOutbound)) {
            return false;
        }
        TblPoOutbound other = (TblPoOutbound) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.inspection.result.model.TblPoOutbound[ uuid=" + uuid + " ]";
    }
    
}
