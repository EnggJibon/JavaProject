/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.stock;

import com.kmcj.karte.resources.mold.part.stock.maint.MoldMaintenance;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.part.MstMoldPart;
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
@Table(name = "tbl_mold_part_stock")
@XmlRootElement
@Cacheable(value = false)
@NamedQueries({
    @NamedQuery(name = "MoldPartStock.findById", query = "SELECT m FROM MoldPartStock m WHERE m.id = :id"),
    @NamedQuery(name = "MoldPartStock.findByMoldPartId", query = "SELECT m FROM MoldPartStock m WHERE m.moldPartId = :moldPartId AND m.moldUuid = :moldUuid"),
    @NamedQuery(name = "MoldPartStock.findByDep", query = "SELECT m FROM MoldPartStock m JOIN FETCH m.mold WHERE m.mold.department = :department AND m.deleteFlg = 0 order by m.storageCode, m.mold.moldName, m.moldPart.moldPartName"),
    @NamedQuery(name = "MoldPartStock.getChangeList.CREATE", query = "SELECT m FROM MoldPartStock m JOIN FETCH m.mold JOIN FETCH m.moldPart WHERE m.mold.department = :department AND m.createDate > :from AND m.createDate < :to order by m.createDate, m.mold.moldName, m.moldPart.moldPartName"),
    @NamedQuery(name = "MoldPartStock.getChangeList.UPDATE", query = "SELECT m FROM MoldPartStock m JOIN FETCH m.mold JOIN FETCH m.moldPart WHERE m.mold.department = :department AND m.basicUpdateDate > :from AND m.basicUpdateDate < :to order by m.basicUpdateDate, m.mold.moldName, m.moldPart.moldPartName"),
    @NamedQuery(name = "MoldPartStock.getChangeList.DELETE", query = "SELECT m FROM MoldPartStock m JOIN FETCH m.mold JOIN FETCH m.moldPart WHERE m.mold.department = :department AND m.deleteDate > :from AND m.deleteDate < :to AND m.deleteFlg = 1 order by m.deleteDate, m.mold.moldName, m.moldPart.moldPartName"),
    @NamedQuery(name = "MoldPartStock.findByMoldPartIds", query = "SELECT m FROM MoldPartStock m WHERE m.moldPartId in :moldPartIds"),
    @NamedQuery(name = "MoldPartStock.getOrderRequired", query = "SELECT m FROM MoldPartStock m JOIN FETCH m.mold JOIN FETCH m.moldPart WHERE m.status = 1 AND m.id IN(SELECT p.moldPartStockId FROM TblMoldMaintenancePart p WHERE p.tblMoldMaintenancePartPK.maintenanceId = :maintenanceId and p.replaceOrRepair <> 2)")
})
public class MoldPartStock implements Serializable {
    public static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    
    @Size(max = 45)
    @Column(name = "MOLD_PART_ID")
    private String moldPartId;
    
    @Size(max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    
    @Size(max = 45)
    @Column(name = "STORAGE_CODE")
    private String storageCode;
    
    @Column(name = "ORDER_POINT")
    private int orderPoint;
    
    @Column(name = "ORDER_UNIT")
    private int orderUnit;
    
    @Column(name = "STOCK")
    private int stock;
    
    @Column(name = "USED_STOCK")
    private int usedStock;
    
    @Column(name = "STATUS")
    private int status;
    
    @Size(max = 400)
    @Column(name = "ORDER_JOB_NO")
    private String orderJobNo;
    
    @Size(max = 45)
    @Column(name = "ORDER_PERSON_UUID")
    private String orderPersonUuid;
    
    @Column(name = "DELETE_FLG")
    private Integer deleteFlg = 0;
    
    @Size(max = 45)
    @Column(name = "DELETE_USER_UUID")
    private String deleteUserUuid;
    
    @Column(name = "DELETE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;
    
    @Column(name = "BASIC_UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date basicUpdateDate;
    
    @Size(max = 45)
    @Column(name = "BASIC_UPDATE_USER_UUID")
    private String basicUpdateUserUuid;
    
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
    @Column(name = "MAINTENANCE_ID")
    private String maitenanceId;


    @JoinColumn(name = "MOLD_PART_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMoldPart moldPart;
    
    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mold;
    
    @JoinColumn(name = "BASIC_UPDATE_USER_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUserMin basicUpdateUser;
    
    @JoinColumn(name = "CREATE_USER_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUserMin createUser;
    
    @JoinColumn(name = "UPDATE_USER_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUserMin updateUser;

    @JoinColumn(name = "MAINTENANCE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MoldMaintenance moldMaintenance;

    /**
     * @return the maitenanceId
     */
    public String getMaitenanceId() {
        return maitenanceId;
    }

    /**
     * @param maitenanceId the maitenanceId to set
     */
    public void setMaitenanceId(String maitenanceId) {
        this.maitenanceId = maitenanceId;
    }

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
    
    /** Constants of MoldPartStock.status*/
    public static class Status {
        /** 通常*/
        public static final int NORMAL = 0;
        /** 要発注*/
        public static final int ORDER_REQ = 1;
        /** 納品待ち*/
        public static final int DELI_WT = 2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoldPartId() {
        return moldPartId;
    }

    public void setMoldPartId(String moldPartId) {
        this.moldPartId = moldPartId;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public int getOrderPoint() {
        return orderPoint;
    }

    public void setOrderPoint(int orderPoint) {
        this.orderPoint = orderPoint;
    }

    public int getOrderUnit() {
        return orderUnit;
    }

    public void setOrderUnit(int orderUnit) {
        this.orderUnit = orderUnit;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderJobNo() {
        return orderJobNo;
    }

    public void setOrderJobNo(String orderJobNo) {
        this.orderJobNo = orderJobNo;
    }

    public String getOrderPersonUuid() {
        return orderPersonUuid;
    }

    public void setOrderPersonUuid(String orderPersonUuid) {
        this.orderPersonUuid = orderPersonUuid;
    }

    public Integer getDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(Integer deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

    public String getDeleteUserUuid() {
        return deleteUserUuid;
    }

    public void setDeleteUserUuid(String deleteUserUuid) {
        this.deleteUserUuid = deleteUserUuid;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public Date getBasicUpdateDate() {
        return basicUpdateDate;
    }

    public void setBasicUpdateDate(Date basicUpdateDate) {
        this.basicUpdateDate = basicUpdateDate;
    }

    public String getBasicUpdateUserUuid() {
        return basicUpdateUserUuid;
    }

    public void setBasicUpdateUserUuid(String basicUpdateUserUuid) {
        this.basicUpdateUserUuid = basicUpdateUserUuid;
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

    public MstMoldPart getMoldPart() {
        return moldPart;
    }

    public void setMoldPart(MstMoldPart moldPart) {
        this.moldPart = moldPart;
    }

    public MstMold getMold() {
        return mold;
    }

    public void setMold(MstMold mold) {
        this.mold = mold;
    }

    public MstUserMin getBasicUpdateUser() {
        return basicUpdateUser;
    }

    public void setBasicUpdateUser(MstUserMin basicUpdateUser) {
        this.basicUpdateUser = basicUpdateUser;
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
