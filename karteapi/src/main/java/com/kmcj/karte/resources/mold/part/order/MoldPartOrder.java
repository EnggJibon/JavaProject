/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.order;

import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStock;
import com.kmcj.karte.resources.user.MstUserMin;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
 * @author f.kitaoji
 */
@Entity
@Table(name = "tbl_mold_part_order")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MoldPartOrder.findAll", query = "SELECT m FROM MoldPartOrder m"),
    @NamedQuery(name = "MoldPartOrder.findById", query = "SELECT m FROM MoldPartOrder m WHERE m.id = :id"),
    @NamedQuery(name = "MoldPartOrder.findByOrderJobNo", query = "SELECT m FROM MoldPartOrder m WHERE m.orderJobNo = :orderJobNo"),
    @NamedQuery(name = "MoldPartOrder.findByOrderUserUuid", query = "SELECT m FROM MoldPartOrder m WHERE m.orderUserUuid = :orderUserUuid"),
    @NamedQuery(name = "MoldPartOrder.findByStockAtOrder", query = "SELECT m FROM MoldPartOrder m WHERE m.stockAtOrder = :stockAtOrder"),
    @NamedQuery(name = "MoldPartOrder.findByUsedStockAtOrder", query = "SELECT m FROM MoldPartOrder m WHERE m.usedStockAtOrder = :usedStockAtOrder"),
    @NamedQuery(name = "MoldPartOrder.findByOrderNo", query = "SELECT m FROM MoldPartOrder m WHERE m.orderNo = :orderNo"),
    @NamedQuery(name = "MoldPartOrder.findByReceivedFlg", query = "SELECT m FROM MoldPartOrder m WHERE m.receivedFlg = :receivedFlg"),
    @NamedQuery(name = "MoldPartOrder.findByStockInId", query = "SELECT m FROM MoldPartOrder m WHERE m.stockInId = :stockInId"),
    @NamedQuery(name = "MoldPartOrder.findByCreateDate", query = "SELECT m FROM MoldPartOrder m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MoldPartOrder.findByUpdateDate", query = "SELECT m FROM MoldPartOrder m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MoldPartOrder.findByCreateUserUuid", query = "SELECT m FROM MoldPartOrder m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MoldPartOrder.findByUpdateUserUuid", query = "SELECT m FROM MoldPartOrder m WHERE m.updateUserUuid = :updateUserUuid")})
public class MoldPartOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ORDER_JOB_NO")
    private String orderJobNo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ORDER_USER_UUID")
    private String orderUserUuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STOCK_AT_ORDER")
    private int stockAtOrder;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USED_STOCK_AT_ORDER")
    private int usedStockAtOrder;
    @Size(max = 45)
    @Column(name = "ORDER_NO")
    private String orderNo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RECEIVED_FLG")
    private int receivedFlg;
    @Size(max = 45)
    @Column(name = "STOCK_IN_ID")
    private String stockInId;
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
    @Column(name = "MOLD_PART_STOCK_ID")
    private String moldPartStockId;
    @JoinColumn(name = "MOLD_PART_STOCK_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MoldPartStock moldPartStock;

    @JoinColumn(name = "ORDER_USER_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUserMin orderUser;
    
    public MoldPartOrder() {
    }

    public MoldPartOrder(String id) {
        this.id = id;
    }

    public MoldPartOrder(String id, String orderJobNo, String orderUserUuid, int stockAtOrder, int usedStockAtOrder, int receivedFlg) {
        this.id = id;
        this.orderJobNo = orderJobNo;
        this.orderUserUuid = orderUserUuid;
        this.stockAtOrder = stockAtOrder;
        this.usedStockAtOrder = usedStockAtOrder;
        this.receivedFlg = receivedFlg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderJobNo() {
        return orderJobNo;
    }

    public void setOrderJobNo(String orderJobNo) {
        this.orderJobNo = orderJobNo;
    }

    public String getOrderUserUuid() {
        return orderUserUuid;
    }

    public void setOrderUserUuid(String orderUserUuid) {
        this.orderUserUuid = orderUserUuid;
    }

    public int getStockAtOrder() {
        return stockAtOrder;
    }

    public void setStockAtOrder(int stockAtOrder) {
        this.stockAtOrder = stockAtOrder;
    }

    public int getUsedStockAtOrder() {
        return usedStockAtOrder;
    }

    public void setUsedStockAtOrder(int usedStockAtOrder) {
        this.usedStockAtOrder = usedStockAtOrder;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getReceivedFlg() {
        return receivedFlg;
    }

    public void setReceivedFlg(int receivedFlg) {
        this.receivedFlg = receivedFlg;
    }

    public String getStockInId() {
        return stockInId;
    }

    public void setStockInId(String stockInId) {
        this.stockInId = stockInId;
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
        if (!(object instanceof MoldPartOrder)) {
            return false;
        }
        MoldPartOrder other = (MoldPartOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.part.order.MoldPartOrder[ id=" + id + " ]";
    }

    /**
     * @return the moldPartStockId
     */
    public String getMoldPartStockId() {
        return moldPartStockId;
    }

    /**
     * @param moldPartStockId the moldPartStockId to set
     */
    public void setMoldPartStockId(String moldPartStockId) {
        this.moldPartStockId = moldPartStockId;
    }

    /**
     * @return the moldPartStock
     */
    public MoldPartStock getMoldPartStock() {
        return moldPartStock;
    }

    /**
     * @param moldPartStock the moldPartStock to set
     */
    public void setMoldPartStock(MoldPartStock moldPartStock) {
        this.moldPartStock = moldPartStock;
    }

    /**
     * @return the orderUser
     */
    public MstUserMin getOrderUser() {
        return orderUser;
    }

    /**
     * @param orderUser the orderUser to set
     */
    public void setOrderUser(MstUserMin orderUser) {
        this.orderUser = orderUser;
    }
    
}
