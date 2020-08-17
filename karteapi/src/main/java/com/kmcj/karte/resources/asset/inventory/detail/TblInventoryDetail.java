/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.detail;

import com.kmcj.karte.resources.asset.MstAsset;
import com.kmcj.karte.resources.asset.inventory.TblInventory;
import com.kmcj.karte.resources.mgmt.company.MstMgmtCompany;
import com.kmcj.karte.resources.mgmt.location.MstMgmtLocation;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "tbl_inventory_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblInventoryDetail.findAll", query = "SELECT t FROM TblInventoryDetail t"),
    @NamedQuery(name = "TblInventoryDetail.findByInventoryId", query = "SELECT t FROM TblInventoryDetail t JOIN FETCH t.mstAsset WHERE t.tblInventoryDetailPK.inventoryId = :inventoryId")
})
@Cacheable(value = false)
public class TblInventoryDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblInventoryDetailPK tblInventoryDetailPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXISTENCE")
    private int existence;
    @Size(max = 200)
    @Column(name = "NO_EXISTENCE_REASON")
    private String noExistenceReason;
    @Size(max = 100)
    @Column(name = "NEW_MGMT_LOCATION_CODE")
    private String newMgmtLocationCode;
    @Size(max = 100)
    @Column(name = "NEW_MGMT_COMPANY_CODE")
    private String newMgmtCompanyCode;
    
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
    
    /**
     * KM-343 add start lyd
     */
    @Column(name = "NEW_ADDED_MGMT_LOCATION")
    private int newAddedMgmtLocation;

    /**
     * KM-343 add end lyd
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
    
    @Transient
    private String assetTypeStr;
    @Transient
    private String mgmtLocationNameStr;
    @Transient
    private String itemNameStr;
    
    /**
     * 結合テーブル定義
     */
    // 資産マスタ
    @PrimaryKeyJoinColumn(name = "ASSET_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstAsset mstAsset;
    public MstAsset getMstAsset() {
        return this.mstAsset;
    }
    public void setMstAsset(MstAsset mstAsset) {
        this.mstAsset = mstAsset;
    }
    /**
     * 管理先マスタ
     */
    @PrimaryKeyJoinColumn(name = "NEW_MGMT_COMPANY_CODE", referencedColumnName = "MGMT_COMPANY_CODE")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMgmtCompany mstMgmtCompany;
    public MstMgmtCompany getMstMgmtCompany() {
        return this.mstMgmtCompany;
    }
    public void setMstMgmtCompany(MstMgmtCompany mstMgmtCompany) {
        this.mstMgmtCompany = mstMgmtCompany;
    }
    /**
     * 所在先マスタ
     */
    @PrimaryKeyJoinColumn(name = "NEW_MGMT_LOCATION_CODE", referencedColumnName = "MGMT_LOCATION_CODE")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMgmtLocation mstMgmtLocation;
    public MstMgmtLocation getMstMgmtLocation() {
        return this.mstMgmtLocation;
    }
    public void setMstMgmtLocation(MstMgmtLocation mstMgmtLocation) {
        this.mstMgmtLocation = mstMgmtLocation;
    }
    // 資産マスタ
    @PrimaryKeyJoinColumn(name = "INVENTORY_ID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblInventory tblInventory;
    public TblInventory getTblInventory() {
        return tblInventory;
    }

    /**
     * @return the newAddedMgmtLocation
     */
    public int getNewAddedMgmtLocation() {
        return newAddedMgmtLocation;
    }

    /**
     * @param newAddedMgmtLocation the newAddedMgmtLocation to set
     */
    public void setNewAddedMgmtLocation(int newAddedMgmtLocation) {
        this.newAddedMgmtLocation = newAddedMgmtLocation;
    }
    
    public void setTblInventory(TblInventory tblInventory) {
        this.tblInventory = tblInventory;
    }
    
    public TblInventoryDetail() {
    }

    public TblInventoryDetail(TblInventoryDetailPK tblInventoryDetailPK) {
        this.tblInventoryDetailPK = tblInventoryDetailPK;
    }

    public TblInventoryDetail(TblInventoryDetailPK tblInventoryDetailPK, int existence) {
        this.tblInventoryDetailPK = tblInventoryDetailPK;
        this.existence = existence;
    }

    public TblInventoryDetail(String inventoryId, String assetId) {
        this.tblInventoryDetailPK = new TblInventoryDetailPK(inventoryId, assetId);
    }

    public TblInventoryDetailPK getTblInventoryDetailPK() {
        return tblInventoryDetailPK;
    }

    public void setTblInventoryDetailPK(TblInventoryDetailPK tblInventoryDetailPK) {
        this.tblInventoryDetailPK = tblInventoryDetailPK;
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

    public String getNewMgmtLocationCode() {
        return newMgmtLocationCode;
    }

    public void setNewMgmtLocationCode(String newMgmtLocationCode) {
        this.newMgmtLocationCode = newMgmtLocationCode;
    }

    public String getNewMgmtCompanyCode() {
        return newMgmtCompanyCode;
    }

    public void setNewMgmtCompanyCode(String newMgmtCompanyCode) {
        this.newMgmtCompanyCode = newMgmtCompanyCode;
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
        hash += (tblInventoryDetailPK != null ? tblInventoryDetailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblInventoryDetail)) {
            return false;
        }
        TblInventoryDetail other = (TblInventoryDetail) object;
        if ((this.tblInventoryDetailPK == null && other.tblInventoryDetailPK != null) || (this.tblInventoryDetailPK != null && !this.tblInventoryDetailPK.equals(other.tblInventoryDetailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.inventory.detail.TblInventoryDetail[ tblInventoryDetailPK=" + tblInventoryDetailPK + " ]";
    }

    /**
     * @return the assetTypeStr
     */
    public String getAssetTypeStr() {
        return assetTypeStr;
    }

    /**
     * @param assetTypeStr the assetTypeStr to set
     */
    public void setAssetTypeStr(String assetTypeStr) {
        this.assetTypeStr = assetTypeStr;
    }

    /**
     * @return the mgmtLocationNameStr
     */
    public String getMgmtLocationNameStr() {
        return mgmtLocationNameStr;
    }

    /**
     * @param mgmtLocationNameStr the mgmtLocationNameStr to set
     */
    public void setMgmtLocationNameStr(String mgmtLocationNameStr) {
        this.mgmtLocationNameStr = mgmtLocationNameStr;
    }

    /**
     * @return the itemNameStr
     */
    public String getItemNameStr() {
        return itemNameStr;
    }

    /**
     * @param itemNameStr the itemNameStr to set
     */
    public void setItemNameStr(String itemNameStr) {
        this.itemNameStr = itemNameStr;
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
