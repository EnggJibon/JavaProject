/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.lot.balance;

import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TblProductionLotBalanceエンティティ
 * @author t.ariki
 */
@Entity
@Table(name = "tbl_production_lot_balance")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblProductionLotBalance.findAll", query = "SELECT t FROM TblProductionLotBalance t"),
    @NamedQuery(name = "TblProductionLotBalance.findById", query = "SELECT t FROM TblProductionLotBalance t WHERE t.id = :id"),
    @NamedQuery(name = "TblProductionLotBalance.findByLotNumber", query = "SELECT t FROM TblProductionLotBalance t WHERE t.lotNumber = :lotNumber"),
    @NamedQuery(name = "TblProductionLotBalance.findByProcessNumber", query = "SELECT t FROM TblProductionLotBalance t WHERE t.processNumber = :processNumber"),
    @NamedQuery(name = "TblProductionLotBalance.findByComponentId", query = "SELECT t FROM TblProductionLotBalance t WHERE t.componentId = :componentId"),
    @NamedQuery(name = "TblProductionLotBalance.findByFirstCompleteCount", query = "SELECT t FROM TblProductionLotBalance t WHERE t.firstCompleteCount = :firstCompleteCount"),
    @NamedQuery(name = "TblProductionLotBalance.findByNextCompleteCount", query = "SELECT t FROM TblProductionLotBalance t WHERE t.nextCompleteCount = :nextCompleteCount"),
    @NamedQuery(name = "TblProductionLotBalance.findByBalance", query = "SELECT t FROM TblProductionLotBalance t WHERE t.balance = :balance"),
    @NamedQuery(name = "TblProductionLotBalance.findByCreateDate", query = "SELECT t FROM TblProductionLotBalance t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblProductionLotBalance.findByUpdateDate", query = "SELECT t FROM TblProductionLotBalance t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblProductionLotBalance.findByCreateUserUuid", query = "SELECT t FROM TblProductionLotBalance t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblProductionLotBalance.findByUpdateUserUuid", query = "SELECT t FROM TblProductionLotBalance t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblProductionLotBalance.deleteByProductionDetail", query = "delete FROM TblProductionLotBalance t WHERE t.productionDetailId = :productionDetailId")
})
@Cacheable(value = false)
public class TblProductionLotBalance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "LOT_NUMBER")
    private String lotNumber;
    @Column(name = "PROCESS_NUMBER")
    private Integer processNumber;
    @Size(max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Column(name = "FIRST_COMPLETE_COUNT")
    private Integer firstCompleteCount;
    @Column(name = "NEXT_COMPLETE_COUNT")
    private Integer nextCompleteCount;
    @Column(name = "BALANCE")
    private Integer balance;
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
    @JoinColumn(name = "PRODUCTION_DETAIL_ID", referencedColumnName = "ID")
    @ManyToOne
    private TblProductionDetail productionDetailId;
    
    /*
     * 一括反映時制御フラグ
     */
    @Transient
    private boolean deleted = false;    // 削除対象制御
    @Transient
    private boolean modified = false;   // 更新対象制御
    @Transient
    private boolean added = false;      // 登録対象制御

    public TblProductionLotBalance() {
    }

    public TblProductionLotBalance(String id) {
        this.id = id;
    }

    public TblProductionLotBalance(String id, String lotNumber) {
        this.id = id;
        this.lotNumber = lotNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public Integer getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(Integer processNumber) {
        this.processNumber = processNumber;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public Integer getFirstCompleteCount() {
        return firstCompleteCount;
    }

    public void setFirstCompleteCount(Integer firstCompleteCount) {
        this.firstCompleteCount = firstCompleteCount;
    }

    public Integer getNextCompleteCount() {
        return nextCompleteCount;
    }

    public void setNextCompleteCount(Integer nextCompleteCount) {
        this.nextCompleteCount = nextCompleteCount;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
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

    public TblProductionDetail getProductionDetailId() {
        return productionDetailId;
    }

    public void setProductionDetailId(TblProductionDetail productionDetailId) {
        this.productionDetailId = productionDetailId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
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
        if (!(object instanceof TblProductionLotBalance)) {
            return false;
        }
        TblProductionLotBalance other = (TblProductionLotBalance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.production.TblProductionLotBalance[ id=" + id + " ]";
    }
    
}
