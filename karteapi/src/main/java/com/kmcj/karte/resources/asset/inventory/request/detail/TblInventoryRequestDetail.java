/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.request.detail;

import com.kmcj.karte.resources.asset.inventory.request.TblInventoryRequest;
import com.kmcj.karte.resources.asset.inventory.request.detail.id.TblInventoryRequestDetailId;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "tbl_inventory_request_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblInventoryRequestDetail.findAll", query = "SELECT t FROM TblInventoryRequestDetail t"),
    @NamedQuery(name = "TblInventoryRequestDetail.findByUuid", query = "SELECT t FROM TblInventoryRequestDetail t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblInventoryRequestDetail.findByAssetNo", query = "SELECT t FROM TblInventoryRequestDetail t WHERE t.assetNo = :assetNo"),
    @NamedQuery(name = "TblInventoryRequestDetail.findByBranchNo", query = "SELECT t FROM TblInventoryRequestDetail t WHERE t.branchNo = :branchNo"),
    @NamedQuery(name = "TblInventoryRequestDetail.findByInventoryRequestId", query = "SELECT t FROM TblInventoryRequestDetail t WHERE t.inventoryRequestId = :inventoryRequestId"),
    @NamedQuery(name = "TblInventoryRequestDetail.findExternalStatusIsUnsent", query = "SELECT t FROM TblInventoryRequestDetail t JOIN FETCH t.tblInventoryRequest request"
            + " JOIN FETCH MstApiUser apiUser ON request.requestCompanyId = apiUser.companyId"
            + " WHERE request.status = :status AND apiUser.userId = :apiUserId"
            + " ORDER BY request.inventoryId")
})
@Cacheable(value = false)
public class TblInventoryRequestDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 棚卸依頼明細IDテーブル
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblInventoryRequestDetail")
    private Collection<TblInventoryRequestDetailId> tblInventoryRequestDetailIds;

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Size(max = 45)
    @Column(name = "INVENTORY_REQUEST_ID")
    private String inventoryRequestId;
    @Size(max = 45)
    @Column(name = "ASSET_NO")
    private String assetNo;
    @Size(max = 45)
    @Column(name = "BRANCH_NO")
    private String branchNo;
    @Size(max = 100)
    @Column(name = "ASSET_TYPE")
    private String assetType;//資産種類の選択肢保持
    @Size(max = 100)
    @Column(name = "ASSET_NAME")
    private String assetName;
    @Size(max = 100)
    @Column(name = "INSTALLATION_SITE")
    private String installationSite;
    @Size(max = 100)
    @Column(name = "ITEM_CODE")
    private String itemCode;
    @Size(max = 100)
    @Column(name = "ITEM_NAME")
    private String itemName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MOLD_MACHINE_TYPE")
    private int moldMachineType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXISTENCE")
    private int existence;
    @Size(max = 200)
    @Column(name = "NO_EXISTENCE_REASON")
    private String noExistenceReason;

    /**
     * KM-342 add start lyd
     */
    @Column(name = "CHANGE_LOCATION")
    private int changeLocation;

    @Column(name = "NEW_LOCATION")
    private String newLocation;

    @Column(name = "NEW_LOCATION_ADDRESS")
    private String newLocationAddress;
    /**
     * KM-342 add end lyd
     */
    
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

    /**
     * 結合テーブル
     */
    @PrimaryKeyJoinColumn(name = "INVENTORY_REQUEST_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblInventoryRequest tblInventoryRequest;

    public TblInventoryRequest getTblInventoryRequest() {
        return this.tblInventoryRequest;
    }

    public void setTblInventoryRequest(TblInventoryRequest tblInventoryRequest) {
        this.tblInventoryRequest = tblInventoryRequest;
    }

    public TblInventoryRequestDetail() {
    }

    public TblInventoryRequestDetail(String uuid) {
        this.uuid = uuid;
    }

    public TblInventoryRequestDetail(String uuid, int moldMachineType, int existence) {
        this.uuid = uuid;
        this.moldMachineType = moldMachineType;
        this.existence = existence;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getInventoryRequestId() {
        return inventoryRequestId;
    }

    public void setInventoryRequestId(String inventoryRequestId) {
        this.inventoryRequestId = inventoryRequestId;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public String getInstallationSite() {
        return installationSite;
    }

    public void setInstallationSite(String installationSite) {
        this.installationSite = installationSite;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getMoldMachineType() {
        return moldMachineType;
    }

    public void setMoldMachineType(int moldMachineType) {
        this.moldMachineType = moldMachineType;
    }

    public int getExistence() {
        return existence;
    }

    public void setExistence(int existence) {
        this.existence = existence;
    }

    public String getNoExistenceReason() {
        return noExistenceReason;
    }

    public void setNoExistenceReason(String noExistenceReason) {
        this.noExistenceReason = noExistenceReason;
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

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof TblInventoryRequestDetail)) {
            return false;
        }
        TblInventoryRequestDetail other = (TblInventoryRequestDetail) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.inventory.request.detail.TblInventoryRequestDetail[ uuid=" + uuid + " ]";
    }

//    /**
//     * @return the mstAsset
//     */
//    public MstAsset getMstAsset() {
//        return mstAsset;
//    }
//
//    /**
//     * @param mstAsset the mstAsset to set
//     */
//    public void setMstAsset(MstAsset mstAsset) {
//        this.mstAsset = mstAsset;
//    }
    /**
     * @return the assetType
     */
    public String getAssetType() {
        return assetType;
    }

    /**
     * @param assetType the assetType to set
     */
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    /**
     * @return the tblInventoryRequestDetailIds
     */
    public Collection<TblInventoryRequestDetailId> getTblInventoryRequestDetailIds() {
        return tblInventoryRequestDetailIds;
    }

    /**
     * @param tblInventoryRequestDetailIds the tblInventoryRequestDetailIds to
     * set
     */
    public void setTblInventoryRequestDetailIds(Collection<TblInventoryRequestDetailId> tblInventoryRequestDetailIds) {
        this.tblInventoryRequestDetailIds = tblInventoryRequestDetailIds;
    }

    /**
     * @return the changeLocation
     */
    public int getChangeLocation() {
        return changeLocation;
    }

    /**
     * @param changeLocation the changeLocation to set
     */
    public void setChangeLocation(int changeLocation) {
        this.changeLocation = changeLocation;
    }

    /**
     * @return the newLocation
     */
    public String getNewLocation() {
        return newLocation;
    }

    /**
     * @param newLocation the newLocation to set
     */
    public void setNewLocation(String newLocation) {
        this.newLocation = newLocation;
    }

    /**
     * @return the newLocationAddress
     */
    public String getNewLocationAddress() {
        return newLocationAddress;
    }

    /**
     * @param newLocationAddress the newLocationAddress to set
     */
    public void setNewLocationAddress(String newLocationAddress) {
        this.newLocationAddress = newLocationAddress;
    }


}
