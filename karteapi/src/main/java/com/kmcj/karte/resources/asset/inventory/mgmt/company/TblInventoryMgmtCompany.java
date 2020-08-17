/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.mgmt.company;

import com.kmcj.karte.resources.asset.inventory.TblInventory;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_inventory_mgmt_company")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblInventoryMgmtCompany.findAll", query = "SELECT t FROM TblInventoryMgmtCompany t"),
    @NamedQuery(name = "TblInventoryMgmtCompany.findByPK", query = "SELECT t FROM TblInventoryMgmtCompany t WHERE t.tblInventoryMgmtCompanyPK.inventoryId = :inventoryId AND t.tblInventoryMgmtCompanyPK.mgmtCompanyCode = :mgmtCompanyCode"),
    @NamedQuery(name = "TblInventoryMgmtCompany.findByInventoryId", query = "SELECT t FROM TblInventoryMgmtCompany t WHERE t.tblInventoryMgmtCompanyPK.inventoryId = :inventoryId"),
    @NamedQuery(name = "TblInventoryMgmtCompany.findByMgmtCompanyCode", query = "SELECT t FROM TblInventoryMgmtCompany t WHERE t.tblInventoryMgmtCompanyPK.mgmtCompanyCode = :mgmtCompanyCode"),
    @NamedQuery(name = "TblInventoryMgmtCompany.findByInventoryIdOrderByDueDate", query = "SELECT t FROM TblInventoryMgmtCompany t WHERE t.tblInventoryMgmtCompanyPK.inventoryId = :inventoryId ORDER BY t.dueDate DESC"),
    @NamedQuery(name = "TblInventoryMgmtCompany.findAllRequested", query = "SELECT COUNT(1) FROM TblInventoryMgmtCompany t WHERE t.tblInventoryMgmtCompanyPK.inventoryId = :inventoryId AND t.requestedDate IS NULL"),
    @NamedQuery(name = "TblInventoryMgmtCompany.findAllReceived", query = "SELECT COUNT(1) FROM TblInventoryMgmtCompany t WHERE t.tblInventoryMgmtCompanyPK.inventoryId = :inventoryId AND t.receivedDate IS NULL"),
    @NamedQuery(name = "TblInventoryMgmtCompany.findSingleByInventoryId", query = "SELECT t FROM TblInventoryMgmtCompany t WHERE t.tblInventoryMgmtCompanyPK.inventoryId = :inventoryId GROUP BY t.tblInventoryMgmtCompanyPK.mgmtCompanyCode")
})
@Cacheable(value = false)
public class TblInventoryMgmtCompany implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblInventoryMgmtCompanyPK tblInventoryMgmtCompanyPK;
    @Size(max = 100)
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Size(max = 100)
    @Column(name = "LOCATION_NAME")
    private String locationName;
    @Size(max = 100)
    @Column(name = "DEPARTMENT")
    private String department;
    @Size(max = 25)
    @Column(name = "TEL_NO")
    private String telNo;
    @Size(max = 100)
    @Column(name = "POSITION")
    private String position;
    @Size(max = 100)
    @Column(name = "CONTACT_NAME")
    private String contactName;
    @Size(max = 100)
    @Column(name = "MAIL_ADDRESS")
    private String mailAddress;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INVENTORY_ASSET_COUNT")
    private int inventoryAssetCount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INVENTORY_MOLD_ASSET_COUNT")
    private int inventoryMoldAssetCount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INVENTORY_MACHINE_ASSET_COUNT")
    private int inventoryMachineAssetCount;
    @Column(name = "DUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date dueDate;
    @Column(name = "REQUESTED_DATE")
    @Temporal(TemporalType.DATE)
    private Date requestedDate;
    @Column(name = "RECEIVED_DATE")
    @Temporal(TemporalType.DATE)
    private Date receivedDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MGMT_LOCATION_CHANGED_FLG")
    private int mgmtLocationChangedFlg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MGMT_COMPANY_CHANGED_FLG")
    private int mgmtCompanyChangedFlg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXISTENCE_DIFF_FLG")
    private int existenceDiffFlg;
    @Size(max = 45)
    @Column(name = "FILE_UUID")
    private String fileUuid;
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
    
    @Transient
    private String dueDateStr;

    /**
     * 結合テーブル定義
     */
    // 棚卸実施
    @PrimaryKeyJoinColumn(name = "INVENTORY_ID", referencedColumnName = "UUID")  
    @ManyToOne
    private TblInventory tblInventory;
    public TblInventory getTblInventory() {
        return this.tblInventory;
    }
    public void setTblInventory(TblInventory tblInventory) {
        this.tblInventory = tblInventory;
    }
    
    public TblInventoryMgmtCompany() {
    }

    public TblInventoryMgmtCompany(TblInventoryMgmtCompanyPK tblInventoryMgmtCompanyPK) {
        this.tblInventoryMgmtCompanyPK = tblInventoryMgmtCompanyPK;
    }

    public TblInventoryMgmtCompany(TblInventoryMgmtCompanyPK tblInventoryMgmtCompanyPK, int inventoryAssetCount, int inventoryMoldAssetCount, int inventoryMachineAssetCount, int mgmtLocationChangedFlg, int mgmtCompanyChangedFlg, int existenceDiffFlg) {
        this.tblInventoryMgmtCompanyPK = tblInventoryMgmtCompanyPK;
        this.inventoryAssetCount = inventoryAssetCount;
        this.inventoryMoldAssetCount = inventoryMoldAssetCount;
        this.inventoryMachineAssetCount = inventoryMachineAssetCount;
        this.mgmtLocationChangedFlg = mgmtLocationChangedFlg;
        this.mgmtCompanyChangedFlg = mgmtCompanyChangedFlg;
        this.existenceDiffFlg = existenceDiffFlg;
    }

    public TblInventoryMgmtCompany(String inventoryId, String mgmtCompanyCode) {
        this.tblInventoryMgmtCompanyPK = new TblInventoryMgmtCompanyPK(inventoryId, mgmtCompanyCode);
    }

    public TblInventoryMgmtCompanyPK getTblInventoryMgmtCompanyPK() {
        return tblInventoryMgmtCompanyPK;
    }

    public void setTblInventoryMgmtCompanyPK(TblInventoryMgmtCompanyPK tblInventoryMgmtCompanyPK) {
        this.tblInventoryMgmtCompanyPK = tblInventoryMgmtCompanyPK;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public int getInventoryAssetCount() {
        return inventoryAssetCount;
    }

    public void setInventoryAssetCount(int inventoryAssetCount) {
        this.inventoryAssetCount = inventoryAssetCount;
    }

    public int getInventoryMoldAssetCount() {
        return inventoryMoldAssetCount;
    }

    public void setInventoryMoldAssetCount(int inventoryMoldAssetCount) {
        this.inventoryMoldAssetCount = inventoryMoldAssetCount;
    }

    public int getInventoryMachineAssetCount() {
        return inventoryMachineAssetCount;
    }

    public void setInventoryMachineAssetCount(int inventoryMachineAssetCount) {
        this.inventoryMachineAssetCount = inventoryMachineAssetCount;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public int getMgmtLocationChangedFlg() {
        return mgmtLocationChangedFlg;
    }

    public void setMgmtLocationChangedFlg(int mgmtLocationChangedFlg) {
        this.mgmtLocationChangedFlg = mgmtLocationChangedFlg;
    }

    public int getMgmtCompanyChangedFlg() {
        return mgmtCompanyChangedFlg;
    }

    public void setMgmtCompanyChangedFlg(int mgmtCompanyChangedFlg) {
        this.mgmtCompanyChangedFlg = mgmtCompanyChangedFlg;
    }

    public int getExistenceDiffFlg() {
        return existenceDiffFlg;
    }

    public void setExistenceDiffFlg(int existenceDiffFlg) {
        this.existenceDiffFlg = existenceDiffFlg;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
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
        hash += (tblInventoryMgmtCompanyPK != null ? tblInventoryMgmtCompanyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblInventoryMgmtCompany)) {
            return false;
        }
        TblInventoryMgmtCompany other = (TblInventoryMgmtCompany) object;
        if ((this.tblInventoryMgmtCompanyPK == null && other.tblInventoryMgmtCompanyPK != null) || (this.tblInventoryMgmtCompanyPK != null && !this.tblInventoryMgmtCompanyPK.equals(other.tblInventoryMgmtCompanyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.inventory.mgmt.company.TblInventoryMgmtCompany[ tblInventoryMgmtCompanyPK=" + tblInventoryMgmtCompanyPK + " ]";
    }

    /**
     * @return the dueDateStr
     */
    public String getDueDateStr() {
        return dueDateStr;
    }

    /**
     * @param dueDateStr the dueDateStr to set
     */
    public void setDueDateStr(String dueDateStr) {
        this.dueDateStr = dueDateStr;
    }
    
}
