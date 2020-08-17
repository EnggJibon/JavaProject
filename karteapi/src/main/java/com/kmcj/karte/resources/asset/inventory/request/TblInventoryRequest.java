/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.request;

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

import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.asset.inventory.TblInventory;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_inventory_request")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblInventoryRequest.findAll", query = "SELECT t FROM TblInventoryRequest t"),
    @NamedQuery(name = "TblInventoryRequest.findByUuid", query = "SELECT t FROM TblInventoryRequest t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblInventoryRequest.findByRequestCompanyId", query = "SELECT t FROM TblInventoryRequest t WHERE t.requestCompanyId = :requestCompanyId"),
    @NamedQuery(name = "TblInventoryRequest.findByInventoryId", query = "SELECT t FROM TblInventoryRequest t WHERE t.inventoryId = :inventoryId"),
    @NamedQuery(name = "TblInventoryRequest.findExternalStatusIsUnsent", query = "SELECT t FROM TblInventoryRequest t"
        + " JOIN MstApiUser apiUser ON t.requestCompanyId = apiUser.companyId"
        + " WHERE t.status = :status AND apiUser.userId = :apiUserId")
})
@Cacheable(value = false)
public class TblInventoryRequest implements Serializable {

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
    @Column(name = "REQUEST_COMPANY_ID")
    private String requestCompanyId;
    @Column(name = "REQUEST_DATE")
    @Temporal(TemporalType.DATE)
    private Date requestDate;
    @Column(name = "DUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ASSET_COUNT")
    private int assetCount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private int status;
    @Column(name = "SEND_RESPONSE_DATE")
    @Temporal(TemporalType.DATE)
    private Date sendResponseDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INVENTORY_ID")
    private String inventoryId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NAME")
    private String name;
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

    public TblInventoryRequest() {
    }
    
    /**
     * 結合テーブル定義
     */
    // 会社マスタ
    @PrimaryKeyJoinColumn(name = "REQUEST_COMPANY_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompany;

    public MstCompany getMstCompany() {
        return this.mstCompany;
    }

    public void setMstCompany(MstCompany mstCompany) {
        this.mstCompany = mstCompany;
    }
    
    // 棚卸実施テーブル」
    @PrimaryKeyJoinColumn(name = "INVENTORY_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblInventory tblInventory;

    public TblInventory getTblInventory() {
        return this.tblInventory;
    }

    public void setTblInventory(TblInventory tblInventory) {
        this.tblInventory = tblInventory;
    }

    public TblInventoryRequest(String uuid) {
        this.uuid = uuid;
    }

    public TblInventoryRequest(String uuid, String requestCompanyId, int assetCount, int status, String inventoryId) {
        this.uuid = uuid;
        this.requestCompanyId = requestCompanyId;
        this.assetCount = assetCount;
        this.status = status;
        this.inventoryId = inventoryId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRequestCompanyId() {
        return requestCompanyId;
    }

    public void setRequestCompanyId(String requestCompanyId) {
        this.requestCompanyId = requestCompanyId;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getAssetCount() {
        return assetCount;
    }

    public void setAssetCount(int assetCount) {
        this.assetCount = assetCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getSendResponseDate() {
        return sendResponseDate;
    }

    public void setSendResponseDate(Date sendResponseDate) {
        this.sendResponseDate = sendResponseDate;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof TblInventoryRequest)) {
            return false;
        }
        TblInventoryRequest other = (TblInventoryRequest) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.inventory.request.TblInventoryRequest[ uuid=" + uuid + " ]";
    }
    
}
