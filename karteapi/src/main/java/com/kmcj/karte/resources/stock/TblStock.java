/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.stock;

import com.kmcj.karte.resources.component.MstComponent;
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
@Table(name = "tbl_stock")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblStock.findAll", query = "SELECT t FROM TblStock t"),
    @NamedQuery(name = "TblStock.findByUuid", query = "SELECT t FROM TblStock t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblStock.findByComponentAndProcedure", query = "SELECT t FROM TblStock t WHERE t.componentId = :componentId And t.procedureCode = :procedureCode")
})
@Cacheable(value = false)
public class TblStock implements Serializable {

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
    @Column(name = "STOCK_QUANTITY")
    private long stockQuantity;
    @Column(name = "MOVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date moveDate;
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
//    @OneToMany(mappedBy = "tblStock")
//    private Collection<TblStockDetail> tblStockDetailCollection;

    /**
     * 部品マスタ
     */
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;

    public TblStock() {
    }

    public TblStock(String uuid) {
        this.uuid = uuid;
    }

    public TblStock(String uuid, int stockQuantity) {
        this.uuid = uuid;
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

    public MstComponent getMstComponent() {
        return mstComponent;
    }

    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }
    
    public Date getMoveDate() {
        return moveDate;
    }

    public void setMoveDate(Date moveDate) {
        this.moveDate = moveDate;
    }

//    @XmlTransient
//    public Collection<TblStockDetail> getTblStockDetailCollection() {
//        return tblStockDetailCollection;
//    }
//
//    public void setTblStockDetailCollection(Collection<TblStockDetail> tblStockDetailCollection) {
//        this.tblStockDetailCollection = tblStockDetailCollection;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof TblStock)) {
            return false;
        }
        TblStock other = (TblStock) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.stock.TblStock[ uuid=" + uuid + " ]";
    }
}
