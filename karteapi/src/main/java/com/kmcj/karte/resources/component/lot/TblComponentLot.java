/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.lot;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.stock.TblStock;
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
@Table(name = "tbl_component_lot")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblComponentLot.findAll", query = "SELECT t FROM TblComponentLot t"),    
    @NamedQuery(name = "TblComponentLot.findByLotNo", query = "SELECT t FROM TblComponentLot t WHERE t.lotNo = :lotNo"),
    @NamedQuery(name = "TblComponentLot.findByComponentId", query = "SELECT t FROM TblComponentLot t WHERE t.componentId = :componentId and t.procedureCode = :procedureCode and t.stockQty >= 0 order by t.lotIssueDate asc"),
    @NamedQuery(name = "TblComponentLot.findByComponentIdForStructure", query = "SELECT t FROM TblComponentLot t WHERE t.componentId = :componentId and t.stockQty > 0 order by t.lotNo asc"),
    @NamedQuery(name = "TblComponentLot.findByComponentIdAndLotNumber", query = "SELECT t FROM TblComponentLot t WHERE t.componentId = :componentId and t.procedureCode = :procedureCode and t.lotNo = :lotNo"),
})
@Cacheable(value = false)
public class TblComponentLot implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Size(max = 45)
    @Column(name = "LOT_NO")
    private String lotNo;
    @Column(name = "LOT_ISSUE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lotIssueDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOT_QTY")
    private int lotQty;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STOCK_QTY")
    private int stockQty;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private int status;
    @Size(max = 45)
    @Column(name = "PRODUCTION_DETAIL_ID")
    private String productionDetailId;
    @Size(max = 200)
    @Column(name = "REMARKS01")
    private String remarks01;
    @Size(max = 200)
    @Column(name = "REMARKS02")
    private String remarks02;
    @Size(max = 200)
    @Column(name = "REMARKS03")
    private String remarks03;
    @Size(max = 200)
    @Column(name = "REMARKS04")
    private String remarks04;
    @Size(max = 200)
    @Column(name = "REMARKS05")
    private String remarks05;
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
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Column(name = "STOCK_ID")
    private String stockId;
    @Size(max = 45)
    @Column(name = "PROCEDURE_CODE")
    private String procedureCode;
    
    // 在庫管理テーブル
    @PrimaryKeyJoinColumn(name = "STOCK_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblStock tblStock;

    // 部品マスタ
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;

    public TblComponentLot() {
    }

    public TblComponentLot(String uuid) {
        this.uuid = uuid;
    }

    public TblComponentLot(String uuid, int lotQty, int stockQty, int status) {
        this.uuid = uuid;
        this.lotQty = lotQty;
        this.stockQty = stockQty;
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public Date getLotIssueDate() {
        return lotIssueDate;
    }

    public void setLotIssueDate(Date lotIssueDate) {
        this.lotIssueDate = lotIssueDate;
    }

    public int getLotQty() {
        return lotQty;
    }

    public void setLotQty(int lotQty) {
        this.lotQty = lotQty;
    }

    public int getStockQty() {
        return stockQty;
    }

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getProductionDetailId() {
        return productionDetailId;
    }

    public void setProductionDetailId(String productionDetailId) {
        this.productionDetailId = productionDetailId;
    }

    public String getRemarks01() {
        return remarks01;
    }

    public void setRemarks01(String remarks01) {
        this.remarks01 = remarks01;
    }

    public String getRemarks02() {
        return remarks02;
    }

    public void setRemarks02(String remarks02) {
        this.remarks02 = remarks02;
    }

    public String getRemarks03() {
        return remarks03;
    }

    public void setRemarks03(String remarks03) {
        this.remarks03 = remarks03;
    }

    public String getRemarks04() {
        return remarks04;
    }

    public void setRemarks04(String remarks04) {
        this.remarks04 = remarks04;
    }

    public String getRemarks05() {
        return remarks05;
    }

    public void setRemarks05(String remarks05) {
        this.remarks05 = remarks05;
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
        if (!(object instanceof TblComponentLot)) {
            return false;
        }
        TblComponentLot other = (TblComponentLot) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.lot.TblComponentLot[ uuid=" + uuid + " ]";
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
     * @return the mstComponent
     */
    public MstComponent getMstComponent() {
        return mstComponent;
    }

    /**
     * @param mstComponent the mstComponent to set
     */
    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }
    
}
