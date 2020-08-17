/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset;

import com.kmcj.karte.resources.asset.relation.TblMoldMachineAssetRelation;
import com.kmcj.karte.resources.contact.MstContact;
import com.kmcj.karte.resources.currency.MstCurrency;
import com.kmcj.karte.resources.item.MstItem;
import com.kmcj.karte.resources.mgmt.company.MstMgmtCompany;
import com.kmcj.karte.resources.mgmt.location.MstMgmtLocation;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
@Table(name = "mst_asset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstAsset.findAll", query = "SELECT m FROM MstAsset m"),
    /** KM-336 棚卸実施登録で抽出条件追加*/
    @NamedQuery(name = "MstAsset.findAssetClass", query = "SELECT DISTINCT m.assetClass FROM MstAsset m WHERE (m.assetClass IS NOT NULL AND m.assetClass <> '') ORDER BY m.assetClass "),
    @NamedQuery(name = "MstAsset.findByUuid", query = "SELECT m FROM MstAsset m WHERE m.uuid = :uuid"),
    @NamedQuery(name = "MstAsset.deleteByUuid", query = "DELETE FROM MstAsset m WHERE m.uuid = :uuid"),
    @NamedQuery(name = "MstAsset.findByPK", query = "SELECT m FROM MstAsset m WHERE m.mstAssetPK.assetNo = :assetNo AND m.mstAssetPK.branchNo = :branchNo"),
    @NamedQuery(name = "MstAsset.findByPKAndItemCode", query = "SELECT m FROM MstAsset m WHERE m.mstAssetPK.assetNo = :assetNo AND m.mstAssetPK.branchNo = :branchNo AND m.itemCode = :itemCode"),
    @NamedQuery(name = "MstAsset.findDisposalAssetByMgmtCompanyCode", query = "SELECT ma FROM MstAsset ma WHERE ma.mgmtCompanyCode IS NOT NULL AND ma.disposalStatus = 0 GROUP BY ma.mgmtCompanyCode")
})
@Cacheable(value = false)
public class MstAsset implements Serializable {

    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @EmbeddedId
    private MstAssetPK mstAssetPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ASSET_TYPE")
    private int assetType;
    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 100)
    @Size(max = 100)
    @Column(name = "ASSET_NAME")
    private String assetName;
    @Size(max = 100)
    @Column(name = "MGMT_COMPANY_CODE")
    private String mgmtCompanyCode;
    @Size(max = 100)
    @Column(name = "MGMT_LOCATION_CODE")
    private String mgmtLocationCode;
    @Size(max = 100)
    @Column(name = "VENDOR_CODE")
    private String vendorCode;
    @Size(max = 100)
    @Column(name = "ITEM_CODE")
    private String itemCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACQUISITION_TYPE")
    private int acquisitionType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ACQUISITION_AMOUNT")
    private BigDecimal acquisitionAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MONTH_BOOK_VALUE")
    private BigDecimal monthBookValue;
    @Size(max = 100)
    @Column(name = "PURCHASING_GROUP")
    private String purchasingGroup;
    @Size(max = 100)
    @Column(name = "COMMON_INFORMATION")
    private String commonInformation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MOLD_MACHINE_TYPE")
    private int moldMachineType;
    
    @Size(max = 6)
    @Column(name = "ACQUISITION_YYYYMM")
    private String acquisitionYyyymm;
    @Column(name = "ACQUISITION_DATE")
    @Temporal(TemporalType.DATE)
    private Date acquisitionDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PERIOD_BOOK_VALUE")
    private BigDecimal periodBookValue;
    @Size(max = 5)
    @Column(name = "CURRENCY_CODE")
    private String currencyCode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MOLD_COUNT")
    private int moldCount;
    @Size(max = 100)
    @Column(name = "ASSET_CLASS")
    private String assetClass;
    @Size(max = 100)
    @Column(name = "USING_SECTION")
    private String usingSection;
    @Size(max = 100)
    @Column(name = "COST_CENTER")
    private String costCenter;
    
    @Column(name = "MGMT_REGION")
    private int mgmtRegion;
    
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "DISPOSAL_STATUS")
    private int disposalStatus;
    
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "mstAsset")
    private Collection<TblMoldMachineAssetRelation> tblMoldMachineAssetRelationVos;
    
    /**
     * 管理先マスタ
     */
    @PrimaryKeyJoinColumn(name = "MGMT_COMPANY_CODE", referencedColumnName = "MGMT_COMPANY_CODE")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMgmtCompany mstMgmtCompany;
    /**
     * 所在先マスタ
     */
    @PrimaryKeyJoinColumn(name = "MGMT_LOCATION_CODE", referencedColumnName = "MGMT_LOCATION_CODE")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMgmtLocation mstMgmtLocation;
    /**
     * 品目マスタ
     */
    @PrimaryKeyJoinColumn(name = "ITEM_CODE", referencedColumnName = "ITEM_CODE")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstItem mstItem;
    
    @JoinColumn(name = "MGMT_COMPANY_CODE", referencedColumnName = "MGMT_COMPANY_CODE", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstContact mstContact;
    
    /**
     * 通貨マスタ
     */
    @PrimaryKeyJoinColumn(name = "CURRENCY_CODE", referencedColumnName = "CURRENCY_CODE")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCurrency mstCurrency;
    public MstAsset() {
    }

    public MstAsset(MstAssetPK mstAssetPK) {
        this.mstAssetPK = mstAssetPK;
    }

    public MstAsset(MstAssetPK mstAssetPK, String uuid, int assetType, String assetName, int acquisitionType, BigDecimal acquisitionAmount, BigDecimal monthBookValue, int typeCount, int moldMachineType) {
        this.mstAssetPK = mstAssetPK;
        this.uuid = uuid;
        this.assetType = assetType;
        this.assetName = assetName;
        this.acquisitionType = acquisitionType;
        this.acquisitionAmount = acquisitionAmount;
        this.monthBookValue = monthBookValue;
        this.moldMachineType = moldMachineType;
    }

    public MstAsset(String assetNo, String branchNo) {
        this.mstAssetPK = new MstAssetPK(assetNo, branchNo);
    }

    public MstAssetPK getMstAssetPK() {
        return mstAssetPK;
    }

    public void setMstAssetPK(MstAssetPK mstAssetPK) {
        this.mstAssetPK = mstAssetPK;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getAssetType() {
        return assetType;
    }

    public void setAssetType(int assetType) {
        this.assetType = assetType;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getMgmtCompanyCode() {
        return mgmtCompanyCode;
    }

    public void setMgmtCompanyCode(String mgmtCompanyCode) {
        this.mgmtCompanyCode = mgmtCompanyCode;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getAcquisitionType() {
        return acquisitionType;
    }

    public void setAcquisitionType(int acquisitionType) {
        this.acquisitionType = acquisitionType;
    }

    public BigDecimal getAcquisitionAmount() {
        return acquisitionAmount;
    }

    public void setAcquisitionAmount(BigDecimal acquisitionAmount) {
        this.acquisitionAmount = acquisitionAmount;
    }

    public BigDecimal getMonthBookValue() {
        return monthBookValue;
    }

    public void setMonthBookValue(BigDecimal monthBookValue) {
        this.monthBookValue = monthBookValue;
    }

    public String getPurchasingGroup() {
        return purchasingGroup;
    }

    public void setPurchasingGroup(String purchasingGroup) {
        this.purchasingGroup = purchasingGroup;
    }

    public String getCommonInformation() {
        return commonInformation;
    }

    public void setCommonInformation(String commonInformation) {
        this.commonInformation = commonInformation;
    }

    public int getMoldMachineType() {
        return moldMachineType;
    }

    public void setMoldMachineType(int moldMachineType) {
        this.moldMachineType = moldMachineType;
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
        hash += (getMstAssetPK() != null ? getMstAssetPK().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstAsset)) {
            return false;
        }
        MstAsset other = (MstAsset) object;
        if ((this.getMstAssetPK() == null && other.getMstAssetPK() != null) || (this.getMstAssetPK() != null && !this.mstAssetPK.equals(other.mstAssetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.asset.MstAsset[ mstAssetPK=" + getMstAssetPK() + " ]";
    }

    /**
     * @return the tblMoldMachineAssetRelationVos
     */
    public Collection<TblMoldMachineAssetRelation> getTblMoldMachineAssetRelationVos() {
        return tblMoldMachineAssetRelationVos;
    }

    /**
     * @param tblMoldMachineAssetRelationVos the tblMoldMachineAssetRelationVos to set
     */
    public void setTblMoldMachineAssetRelationVos(Collection<TblMoldMachineAssetRelation> tblMoldMachineAssetRelationVos) {
        this.tblMoldMachineAssetRelationVos = tblMoldMachineAssetRelationVos;
    }

    public String getAcquisitionYyyymm() {
        return acquisitionYyyymm;
    }

    public void setAcquisitionYyyymm(String acquisitionYyyymm) {
        this.acquisitionYyyymm = acquisitionYyyymm;
    }

    public Date getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(Date acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public BigDecimal getPeriodBookValue() {
        return periodBookValue;
    }

    public void setPeriodBookValue(BigDecimal periodBookValue) {
        this.periodBookValue = periodBookValue;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public int getMoldCount() {
        return moldCount;
    }

    public void setMoldCount(int moldCount) {
        this.moldCount = moldCount;
    }

    public String getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(String assetClass) {
        this.assetClass = assetClass;
    }

    public String getUsingSection() {
        return usingSection;
    }

    public void setUsingSection(String usingSection) {
        this.usingSection = usingSection;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * @return the mgmtLocationCode
     */
    public String getMgmtLocationCode() {
        return mgmtLocationCode;
    }

    /**
     * @param mgmtLocationCode the mgmtLocationCode to set
     */
    public void setMgmtLocationCode(String mgmtLocationCode) {
        this.mgmtLocationCode = mgmtLocationCode;
    }

    /**
     * @return the mgmtRegion
     */
    public int getMgmtRegion() {
        return mgmtRegion;
    }

    /**
     * @param mgmtRegion the mgmtRegion to set
     */
    public void setMgmtRegion(int mgmtRegion) {
        this.mgmtRegion = mgmtRegion;
    }

    /**
     * @return the mstMgmtCompany
     */
    public MstMgmtCompany getMstMgmtCompany() {
        return mstMgmtCompany;
    }

    /**
     * @param mstMgmtCompany the mstMgmtCompany to set
     */
    public void setMstMgmtCompany(MstMgmtCompany mstMgmtCompany) {
        this.mstMgmtCompany = mstMgmtCompany;
    }

    /**
     * @return the mstMgmtLocation
     */
    public MstMgmtLocation getMstMgmtLocation() {
        return mstMgmtLocation;
    }

    /**
     * @param mstMgmtLocation the mstMgmtLocation to set
     */
    public void setMstMgmtLocation(MstMgmtLocation mstMgmtLocation) {
        this.mstMgmtLocation = mstMgmtLocation;
    }

    /**
     * @return the mstItem
     */
    public MstItem getMstItem() {
        return mstItem;
    }

    /**
     * @param mstItem the mstItem to set
     */
    public void setMstItem(MstItem mstItem) {
        this.mstItem = mstItem;
    }

    /**
     * @return the mstCurrency
     */
    public MstCurrency getMstCurrency() {
        return mstCurrency;
    }

    /**
     * @param mstCurrency the mstCurrency to set
     */
    public void setMstCurrency(MstCurrency mstCurrency) {
        this.mstCurrency = mstCurrency;
    }

    public int getDisposalStatus() {
        return disposalStatus;
    }

    public void setDisposalStatus(int disposalStatus) {
        this.disposalStatus = disposalStatus;
    }

    public MstContact getMstContact() {
        return mstContact;
    }

    public void setMstContact(MstContact mstContact) {
        this.mstContact = mstContact;
    }
    
}
