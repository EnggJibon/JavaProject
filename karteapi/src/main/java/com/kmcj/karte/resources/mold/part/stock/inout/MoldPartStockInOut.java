/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.stock.inout;

import com.kmcj.karte.resources.mold.part.stock.maint.MoldMaintenance;
import com.kmcj.karte.resources.mold.part.stock.MoldPartStock;
import com.kmcj.karte.resources.user.MstUserMin;
import java.io.Serializable;
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
 * @author t.takasaki
 */
@Entity
@Table(name = "tbl_mold_part_stock_in_out")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MoldPartStockInOut.findByMaintId", query = "SELECT m FROM MoldPartStockInOut m WHERE m.moldMaintId = :moldMaintId")
})
@Cacheable(value = false)
public class MoldPartStockInOut implements Serializable {
    public static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    
    @Size(max = 45)
    @Column(name = "MOLD_PART_STOCK_ID")
    private String moldPartStockId;
    
    @Column(name = "IO_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ioDate;
    
    @Column(name = "IO_EVENT")
    private int ioEvent;
    
    @Size(max = 200)
    @Column(name = "ADJUST_REASON")
    private String adjustReason;
    
    @Column(name = "NEW_STOCK_IO")
    private int newStockIo;
    
    @Column(name = "USED_STOCK_IO")
    private int usedStockIo;
    
    @Column(name = "STOCK")
    private int stock;
    
    @Column(name = "USED_STOCK")
    private int usedStock;
    
    @Size(max = 45)
    @Column(name = "MOLD_MAINT_ID")
    private String moldMaintId;
    
    @Size(max = 45)
    @Column(name = "ORDER_JOB_NO")
    private String orderJobNo;
    
    @Column(name = "UNIT_PRICE")
    private int unitPrice;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    
    @Column(name = "CANCELED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date canceledDate;
    
    @Size(max = 45)
    @Column(name = "CANCELED_SOURCE_ID")
    private String canceledSourceId;

    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    
    @JoinColumn(name = "MOLD_PART_STOCK_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MoldPartStock moldPartStock;
    
    @JoinColumn(name = "CREATE_USER_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUserMin createUser;
    
    @JoinColumn(name = "UPDATE_USER_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUserMin updateUser;

    @JoinColumn(name = "MOLD_MAINT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MoldMaintenance moldMaintenance;

    /**
     * @return the moldMaintenance
     */
    public MoldMaintenance getMoldMaintenance() {
        return moldMaintenance;
    }

    /**
     * @param moldMaintenance the moldMaintenance to set
     */
    public void setMoldMaintenance(MoldMaintenance moldMaintenance) {
        this.moldMaintenance = moldMaintenance;
    }

    /**
     * @return the canceledDate
     */
    public Date getCanceledDate() {
        return canceledDate;
    }

    /**
     * @param canceledDate the canceledDate to set
     */
    public void setCanceledDate(Date canceledDate) {
        this.canceledDate = canceledDate;
    }

    /**
     * @return the canceledSourceId
     */
    public String getCanceledSourceId() {
        return canceledSourceId;
    }

    /**
     * @param canceledSourceId the canceledSourceId to set
     */
    public void setCanceledSourceId(String canceledSourceId) {
        this.canceledSourceId = canceledSourceId;
    }
    
    public static class IoEvent {
        public static final int NEW = 0;
        public static final int RECEIVE = 1;
        public static final int EXCHANGE = 2;
        public static final int ADJUST = 3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoldPartStockId() {
        return moldPartStockId;
    }

    public void setMoldPartStockId(String moldPartStockId) {
        this.moldPartStockId = moldPartStockId;
    }

    public Date getIoDate() {
        return ioDate;
    }

    public void setIoDate(Date ioDate) {
        this.ioDate = ioDate;
    }

    public int getIoEvent() {
        return ioEvent;
    }

    public void setIoEvent(int ioEvent) {
        this.ioEvent = ioEvent;
    }

    public String getAdjustReason() {
        return adjustReason;
    }

    public void setAdjustReason(String adjustReason) {
        this.adjustReason = adjustReason;
    }

    public int getNewStockIo() {
        return newStockIo;
    }

    public void setNewStockIo(int newStockIo) {
        this.newStockIo = newStockIo;
    }

    public int getUsedStockIo() {
        return usedStockIo;
    }

    public void setUsedStockIo(int usedStockIo) {
        this.usedStockIo = usedStockIo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getUsedStock() {
        return usedStock;
    }

    public void setUsedStock(int usedStock) {
        this.usedStock = usedStock;
    }

    public String getMoldMaintId() {
        return moldMaintId;
    }

    public void setMoldMaintId(String moldMaintId) {
        this.moldMaintId = moldMaintId;
    }

    public String getOrderJobNo() {
        return orderJobNo;
    }

    public void setOrderJobNo(String orderJobNo) {
        this.orderJobNo = orderJobNo;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
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

    public MoldPartStock getMoldPartStock() {
        return moldPartStock;
    }

    public void setMoldPartStock(MoldPartStock moldPartStock) {
        this.moldPartStock = moldPartStock;
    }

    public MstUserMin getCreateUser() {
        return createUser;
    }

    public void setCreateUser(MstUserMin createUser) {
        this.createUser = createUser;
    }

    public MstUserMin getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(MstUserMin updateUser) {
        this.updateUser = updateUser;
    }
}
