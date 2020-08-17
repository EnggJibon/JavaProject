/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold;

import com.kmcj.karte.ExcludeInObjectRsponse;
import com.kmcj.karte.resources.mold.inventory.TblMoldInventory;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.direction.TblDirection;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.mold.issue.TblIssue;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtn;
import com.kmcj.karte.resources.mold.assetno.MstMoldAssetNo;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelation;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistory;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodeling;
import com.kmcj.karte.resources.mold.proccond.MstMoldProcCond;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistory;
import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_mold")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMold.findAll", query = "SELECT m FROM MstMold m"),
    @NamedQuery(name = "MstMold.findByUuid", query = "SELECT m FROM MstMold m WHERE m.uuid = :uuid"),
    @NamedQuery(name = "MstMold.findByMoldId", query = "SELECT m FROM MstMold m WHERE m.moldId = :moldId"),
    @NamedQuery(name = "MstMold.findLikeMoldId", query = "SELECT m FROM MstMold m WHERE m.moldId like :moldId"),
    @NamedQuery(name = "MstMold.findByMoldName", query = "SELECT m FROM MstMold m WHERE m.moldName = :moldName"),
    @NamedQuery(name = "MstMold.findByMoldType", query = "SELECT m FROM MstMold m WHERE m.moldType = :moldType"),
    @NamedQuery(name = "MstMold.findByMainAssetNo", query = "SELECT m FROM MstMold m WHERE m.mainAssetNo = :mainAssetNo"),
    @NamedQuery(name = "MstMold.findByMoldCreatedDate", query = "SELECT m FROM MstMold m WHERE m.moldCreatedDate = :moldCreatedDate"),
    @NamedQuery(name = "MstMold.findByInspectedDate", query = "SELECT m FROM MstMold m WHERE m.inspectedDate = :inspectedDate"),
    @NamedQuery(name = "MstMold.findByInstalledDate", query = "SELECT m FROM MstMold m WHERE m.installedDate = :installedDate"),
    @NamedQuery(name = "MstMold.findByCompanyName", query = "SELECT m FROM MstMold m WHERE m.companyName = :companyName"),
    @NamedQuery(name = "MstMold.findByLocationName", query = "SELECT m FROM MstMold m WHERE m.locationName = :locationName"),
    @NamedQuery(name = "MstMold.findByInstllationSiteName", query = "SELECT m FROM MstMold m WHERE m.instllationSiteName = :instllationSiteName"),
    @NamedQuery(name = "MstMold.findByStatus", query = "SELECT m FROM MstMold m WHERE m.status = :status"),
    @NamedQuery(name = "MstMold.findByStatusChangedDate", query = "SELECT m FROM MstMold m WHERE m.statusChangedDate = :statusChangedDate"),
    @NamedQuery(name = "MstMold.findByTotalShotCount", query = "SELECT m FROM MstMold m WHERE m.totalShotCount = :totalShotCount"),
    @NamedQuery(name = "MstMold.findByImgFilePath01", query = "SELECT m FROM MstMold m WHERE m.imgFilePath01 = :imgFilePath01"),
    @NamedQuery(name = "MstMold.findByImgFilePath02", query = "SELECT m FROM MstMold m WHERE m.imgFilePath02 = :imgFilePath02"),
    @NamedQuery(name = "MstMold.findByImgFilePath03", query = "SELECT m FROM MstMold m WHERE m.imgFilePath03 = :imgFilePath03"),
    @NamedQuery(name = "MstMold.findByImgFilePath04", query = "SELECT m FROM MstMold m WHERE m.imgFilePath04 = :imgFilePath04"),
    @NamedQuery(name = "MstMold.findByImgFilePath05", query = "SELECT m FROM MstMold m WHERE m.imgFilePath05 = :imgFilePath05"),
    @NamedQuery(name = "MstMold.findByImgFilePath06", query = "SELECT m FROM MstMold m WHERE m.imgFilePath06 = :imgFilePath06"),
    @NamedQuery(name = "MstMold.findByImgFilePath07", query = "SELECT m FROM MstMold m WHERE m.imgFilePath07 = :imgFilePath07"),
    @NamedQuery(name = "MstMold.findByImgFilePath08", query = "SELECT m FROM MstMold m WHERE m.imgFilePath08 = :imgFilePath08"),
    @NamedQuery(name = "MstMold.findByImgFilePath09", query = "SELECT m FROM MstMold m WHERE m.imgFilePath09 = :imgFilePath09"),
    @NamedQuery(name = "MstMold.findByImgFilePath10", query = "SELECT m FROM MstMold m WHERE m.imgFilePath10 = :imgFilePath10"),
    @NamedQuery(name = "MstMold.findByCreateDate", query = "SELECT m FROM MstMold m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMold.findTblMoldInventoryByid", query = "SELECT t FROM MstMold t LEFT JOIN FETCH t.tblMoldInventory mi LEFT JOIN FETCH t.mstMoldComponentRelationCollection WHERE t.moldId = :moldId order by mi.inventoryDate DESC "),
    @NamedQuery(name = "MstMold.findByUpdateDate", query = "SELECT m FROM MstMold m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMold.findByCreateUserUuid", query = "SELECT m FROM MstMold m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMold.findByUpdateUserUuid", query = "SELECT m FROM MstMold m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMold.delete", query = "DELETE FROM MstMold m WHERE m.moldId = :moldId"),
    @NamedQuery(name = "MstMold.findByMoldIdAndName", query = "SELECT m FROM MstMold m WHERE m.moldId = :moldId and m.moldName = :moldName"),
    @NamedQuery(name = "MstMold.updateAssetNoByMoldId", query = "UPDATE MstMold m SET m.mainAssetNo = :mainAssetNo WHERE m.moldId = :moldId "),
    @NamedQuery(name = "MstMold.updateByMoldId", query = "UPDATE MstMold m SET "
            + "m.moldName = :moldName,"
            + "m.moldType = :moldType,"
            + "m.mainAssetNo = :mainAssetNo,"
            + "m.moldCreatedDate = :moldCreatedDate,"
            + "m.inspectedDate = :inspectedDate,"
            + "m.department = :department,"
            + "m.ownerCompanyId = :ownerCompanyId,"
            + "m.installedDate = :installedDate,"
            + "m.companyId = :companyId,"
            + "m.locationId = :locationId,"
            + "m.instllationSiteId = :instllationSiteId,"
            + "m.companyName = :companyName,"
            + "m.locationName = :locationName,"
            + "m.instllationSiteName = :instllationSiteName,"
            + "m.status = :status,"
            + "m.statusChangedDate = :statusChangedDate,"
            + "m.imgFilePath01 = :imgFilePath01, "
            + "m.imgFilePath02 = :imgFilePath02, "
            + "m.imgFilePath03 = :imgFilePath03, "
            + "m.imgFilePath04 = :imgFilePath04, "
            + "m.imgFilePath05 = :imgFilePath05, "
            + "m.imgFilePath06 = :imgFilePath06, "
            + "m.imgFilePath07 = :imgFilePath07, "
            + "m.imgFilePath08 = :imgFilePath08, "
            + "m.imgFilePath09 = :imgFilePath09, "
            + "m.imgFilePath10 = :imgFilePath10, "
            
            + "m.lastProductionDate = :lastProductionDate, "
            + "m.totalProducingTimeHour = :totalProducingTimeHour, "
            + "m.totalShotCount = :totalShotCount, "
            + "m.lastMainteDate = :lastMainteDate, "
            + "m.afterMainteTotalShotCount = :afterMainteTotalShotCount, "
            + "m.afterMainteTotalProducingTimeHour = :afterMainteTotalProducingTimeHour, "
            + "m.mainteCycleId01 = :mainteCycleId01, "
            + "m.mainteCycleId02 = :mainteCycleId02, "
            + "m.mainteCycleId03 = :mainteCycleId03, "
            
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid "
            + "WHERE m.moldId = :moldId"),
    @NamedQuery(name = "MstMold.updateByMoldIdForLocationHistory", query = "UPDATE MstMold m SET "
            + "m.installedDate = :installedDate,"
            + "m.companyId = :companyId,"
            + "m.locationId = :locationId,"
            + "m.instllationSiteId = :instllationSiteId,"
            + "m.companyName = :companyName,"
            + "m.locationName = :locationName,"
            + "m.instllationSiteName = :instllationSiteName,"
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid "
            + "WHERE m.moldId = :moldId"),
    @NamedQuery(name = "MstMold.updateByMoldIdForTblMoldInventory", query = "UPDATE MstMold m SET "
            + "m.companyId = :companyId,"
            + "m.companyName = :companyName,"
            + "m.locationId = :locationId,"
            + "m.locationName = :locationName,"
            + "m.instllationSiteId = :instllationSiteId,"
            + "m.instllationSiteName = :instllationSiteName,"
            + "m.latestInventoryId = :latestInventoryId,"
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid "
            + "WHERE m.moldId = :moldId"),
    @NamedQuery(name = "MstMold.findFkByCompanyId", query = "SELECT m FROM MstMold m WHERE m.companyId = :companyId "),
    @NamedQuery(name = "MstMold.findFkByOwnerCompanyId", query = "SELECT m FROM MstMold m WHERE m.ownerCompanyId = :companyId "),
    @NamedQuery(name = "MstMold.findFkByLocationId", query = "SELECT m FROM MstMold m WHERE m.locationId = :locationId "),
    @NamedQuery(name = "MstMold.findFkByInstallationSiteId", query = "SELECT m FROM MstMold m WHERE m.instllationSiteId = :instllationSiteId ")
})
@Cacheable(value = false)
public class MstMold implements Serializable {

    @ExcludeInObjectRsponse
    @OneToMany(mappedBy = "mstMold")
    private Collection<TblDirection> tblDirectionCollection;
    
    @ExcludeInObjectRsponse
    @OneToMany(mappedBy = "mstMold",cascade = CascadeType.REMOVE)
    private Collection<TblIssue> tblIssueCollection;

    @ExcludeInObjectRsponse
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "mstMold")
    private Collection<TblMoldMaintenanceRemodeling> tblMoldMaintenanceRemodelingCollection;

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

    @ExcludeInObjectRsponse
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "mstMold")
    @OrderBy(value = " endDate desc ")
    private Collection<MstMoldSpecHistory> mstMoldSpecHistoryCollection;

    @ExcludeInObjectRsponse
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "mstMold")
    private Collection<TblMoldLocationHistory> tblMoldLocationHistoryCollection;

    @ExcludeInObjectRsponse
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "mstMold")
    //@OrderBy(value = " mstComponent ASC,createDate ASC ")// 課題KM-78対応
    private Collection<MstMoldComponentRelation> mstMoldComponentRelationCollection;

    @ExcludeInObjectRsponse
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "mstMold")
    private Collection<MstMoldProcCond> mstMoldProcCondCollection;

    @ExcludeInObjectRsponse
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "mstMold")
    private Collection<MstMoldAssetNo> mstMoldAssetNoCollection;

    @ExcludeInObjectRsponse
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "mstMold")
    @OrderBy(value = " inventoryDate ASC ")
    private Collection<TblMoldInventory> tblMoldInventoryCollection;

    private static long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MOLD_ID")
    private String moldId;
    @Size(max = 100)
    @Column(name = "MOLD_NAME")
    private String moldName;
    @Column(name = "MOLD_TYPE")
    private Integer moldType;
    @Size(max = 45)
    @Column(name = "MAIN_ASSET_NO")
    private String mainAssetNo;
    @Column(name = "MOLD_CREATED_DATE")
    @Temporal(TemporalType.DATE)
    private Date moldCreatedDate;
    @Size(max = 45)
    @Column(name = "OWNER_COMPANY_ID")
    private String ownerCompanyId;
    @Column(name = "INSPECTED_DATE")
    @Temporal(TemporalType.DATE)
    private Date inspectedDate;
    @Column(name = "INSTALLED_DATE")
    @Temporal(TemporalType.DATE)
    private Date installedDate;
    @Size(max = 45)
    @Column(name = "COMPANY_ID")
    private String companyId;
    @Size(max = 100)
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Size(max = 45)
    @Column(name = "LOCATION_ID")
    private String locationId;
    @Size(max = 100)
    @Column(name = "LOCATION_NAME")
    private String locationName;
    @Size(max = 45)
    @Column(name = "INSTLLATION_SITE_ID")
    private String instllationSiteId;
    @Size(max = 100)
    @Column(name = "INSTLLATION_SITE_NAME")
    private String instllationSiteName;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "STATUS_CHANGED_DATE")
    @Temporal(TemporalType.DATE)
    private Date statusChangedDate;
    @Column(name = "MAINTE_STATUS")
    private Integer mainteStatus;
    @Column(name = "TOTAL_SHOT_COUNT")
    private Integer totalShotCount;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH01")
    private String imgFilePath01;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH02")
    private String imgFilePath02;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH03")
    private String imgFilePath03;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH04")
    private String imgFilePath04;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH05")
    private String imgFilePath05;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH06")
    private String imgFilePath06;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH07")
    private String imgFilePath07;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH08")
    private String imgFilePath08;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH09")
    private String imgFilePath09;
    @Size(max = 256)
    @Column(name = "IMG_FILE_PATH10")
    private String imgFilePath10;
    
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH01")
    private String reportFilePath01;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH02")
    private String reportFilePath02;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH03")
    private String reportFilePath03;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH04")
    private String reportFilePath04;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH05")
    private String reportFilePath05;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH06")
    private String reportFilePath06;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH07")
    private String reportFilePath07;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH08")
    private String reportFilePath08;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH09")
    private String reportFilePath09;
    @Size(max = 256)
    @Column(name = "REPORT_FILE_PATH10")
    private String reportFilePath10;
    
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
    @Column(name = "LATEST_INVENTORY_ID")
    private String latestInventoryId;

    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompanyByCompanyId;

    @JoinColumn(name = "INSTLLATION_SITE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstInstallationSite mstInstallationSite;

    @JoinColumn(name = "LATEST_INVENTORY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblMoldInventory tblMoldInventory;

    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstLocation mstLocation;

    @JoinColumn(name = "OWNER_COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompanyByOwnerCompanyId;
    
    @Column(name = "DEPARTMENT")
    private Integer department;
    @Column(name = "LAST_PRODUCTION_DATE")
    @Temporal(TemporalType.DATE)
    private Date lastProductionDate;
    @Column(name = "TOTAL_PRODUCING_TIME_HOUR")
    private BigDecimal totalProducingTimeHour;
    @Column(name = "LAST_MAINTE_DATE")
    @Temporal(TemporalType.DATE)
    private Date lastMainteDate;
    @Column(name = "AFTER_MAINTE_TOTAL_PRODUCING_TIME_HOUR")
    private BigDecimal afterMainteTotalProducingTimeHour;
    @Column(name = "AFTER_MAINTE_TOTAL_SHOT_COUNT")
    private Integer afterMainteTotalShotCount;
    @Column(name = "MAINTE_CYCLE_ID_01")
    private String mainteCycleId01;
    @Column(name = "MAINTE_CYCLE_ID_02")
    private String mainteCycleId02;
    @Column(name = "MAINTE_CYCLE_ID_03")
    private String mainteCycleId03;
    
    @Transient
    private String lastMainteDateStr;
    
    @Column(name = "INVENTORY_STATUS")
    private int inventoryStatus;
    
    /**
     * 結合テーブル定義
     */
    // メンテナンスサイクルパターン
    @PrimaryKeyJoinColumn(name = "MAINTE_CYCLE_ID_01", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblMaintenanceCyclePtn blMaintenanceCyclePtn01;

    @PrimaryKeyJoinColumn(name = "MAINTE_CYCLE_ID_02", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblMaintenanceCyclePtn blMaintenanceCyclePtn02;

    @PrimaryKeyJoinColumn(name = "MAINTE_CYCLE_ID_03", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblMaintenanceCyclePtn blMaintenanceCyclePtn03;
    
    // ユーザーマスタ
    @PrimaryKeyJoinColumn(name = "CREATE_USER_UUID", referencedColumnName = "UUID")
    //@ManyToOne(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstCreateUser;
    
    // ユーザーマスタ
    @PrimaryKeyJoinColumn(name = "UPDATE_USER_UUID", referencedColumnName = "UUID")
    //@ManyToOne(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstUpdateUser;

    public MstMold() {
    }

    public MstMold(String moldId) {
        this.moldId = moldId;
    }

    public MstMold(String moldId, String uuid) {
        this.moldId = moldId;
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMoldId() {
        return moldId;
    }

    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public String getMoldName() {
        return moldName;
    }

    public void setMoldName(String moldName) {
        this.moldName = moldName;
    }

    public Integer getMoldType() {
        return moldType;
    }

    public void setMoldType(Integer moldType) {
        this.moldType = moldType;
    }

    public String getMainAssetNo() {
        return mainAssetNo;
    }

    public void setMainAssetNo(String mainAssetNo) {
        this.mainAssetNo = mainAssetNo;
    }

    public java.util.Date getMoldCreatedDate() {
        return moldCreatedDate;
    }

    public void setMoldCreatedDate(java.util.Date moldCreatedDate) {
        this.moldCreatedDate = moldCreatedDate;
    }

    public java.util.Date getInspectedDate() {
        return inspectedDate;
    }

    public void setInspectedDate(java.util.Date inspectedDate) {
        this.inspectedDate = inspectedDate;
    }

    public java.util.Date getInstalledDate() {
        return installedDate;
    }

    public void setInstalledDate(java.util.Date installedDate) {
        this.installedDate = installedDate;
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

    public String getInstllationSiteName() {
        return instllationSiteName;
    }

    public void setInstllationSiteName(String instllationSiteName) {
        this.instllationSiteName = instllationSiteName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public java.util.Date getStatusChangedDate() {
        return statusChangedDate;
    }

    public void setStatusChangedDate(java.util.Date statusChangedDate) {
        this.statusChangedDate = statusChangedDate;
    }

    public Integer getTotalShotCount() {
        return totalShotCount;
    }

    public void setTotalShotCount(Integer totalShotCount) {
        this.totalShotCount = totalShotCount;
    }

    public String getImgFilePath01() {
        return imgFilePath01;
    }

    public void setImgFilePath01(String imgFilePath01) {
        this.imgFilePath01 = imgFilePath01;
    }

    public String getImgFilePath02() {
        return imgFilePath02;
    }

    public void setImgFilePath02(String imgFilePath02) {
        this.imgFilePath02 = imgFilePath02;
    }

    public String getImgFilePath03() {
        return imgFilePath03;
    }

    public void setImgFilePath03(String imgFilePath03) {
        this.imgFilePath03 = imgFilePath03;
    }

    public String getImgFilePath04() {
        return imgFilePath04;
    }

    public void setImgFilePath04(String imgFilePath04) {
        this.imgFilePath04 = imgFilePath04;
    }

    public String getImgFilePath05() {
        return imgFilePath05;
    }

    public void setImgFilePath05(String imgFilePath05) {
        this.imgFilePath05 = imgFilePath05;
    }

    public String getImgFilePath06() {
        return imgFilePath06;
    }

    public void setImgFilePath06(String imgFilePath06) {
        this.imgFilePath06 = imgFilePath06;
    }

    public String getImgFilePath07() {
        return imgFilePath07;
    }

    public void setImgFilePath07(String imgFilePath07) {
        this.imgFilePath07 = imgFilePath07;
    }

    public String getImgFilePath08() {
        return imgFilePath08;
    }

    public void setImgFilePath08(String imgFilePath08) {
        this.imgFilePath08 = imgFilePath08;
    }

    public String getImgFilePath09() {
        return imgFilePath09;
    }

    public void setImgFilePath09(String imgFilePath09) {
        this.imgFilePath09 = imgFilePath09;
    }

    public String getImgFilePath10() {
        return imgFilePath10;
    }

    public void setImgFilePath10(String imgFilePath10) {
        this.imgFilePath10 = imgFilePath10;
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

    @XmlTransient
    public Collection<TblMoldInventory> getTblMoldInventoryCollection() {
        return tblMoldInventoryCollection;
    }

    public void setTblMoldInventoryCollection(Collection<TblMoldInventory> tblMoldInventoryCollection) {
        this.tblMoldInventoryCollection = tblMoldInventoryCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getMoldId() != null ? getMoldId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMold)) {
            return false;
        }
        MstMold other = (MstMold) object;
        if ((this.getMoldId() == null && other.getMoldId() != null) || (this.getMoldId() != null && !this.moldId.equals(other.moldId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.MstMold[ moldId=" + getMoldId() + " ]";
    }

    @XmlTransient
    public Collection<MstMoldAssetNo> getMstMoldAssetNoCollection() {
        return mstMoldAssetNoCollection;
    }

    public void setMstMoldAssetNoCollection(Collection<MstMoldAssetNo> mstMoldAssetNoCollection) {
        this.mstMoldAssetNoCollection = mstMoldAssetNoCollection;
    }

    @XmlTransient
    public Collection<MstMoldProcCond> getMstMoldProcCondCollection() {
        return mstMoldProcCondCollection;
    }

    public void setMstMoldProcCondCollection(Collection<MstMoldProcCond> mstMoldProcCondCollection) {
        this.mstMoldProcCondCollection = mstMoldProcCondCollection;
    }

    @XmlTransient
    public Collection<MstMoldComponentRelation> getMstMoldComponentRelationCollection() {
        return mstMoldComponentRelationCollection;
    }

    public void setMstMoldComponentRelationCollection(Collection<MstMoldComponentRelation> mstMoldComponentRelationCollection) {
        this.mstMoldComponentRelationCollection = mstMoldComponentRelationCollection;
    }

    @XmlTransient
    public Collection<TblMoldLocationHistory> getTblMoldLocationHistoryCollection() {
        return tblMoldLocationHistoryCollection;
    }

    public void setTblMoldLocationHistoryCollection(Collection<TblMoldLocationHistory> tblMoldLocationHistoryCollection) {
        this.tblMoldLocationHistoryCollection = tblMoldLocationHistoryCollection;
    }

    @XmlTransient
    public Collection<MstMoldSpecHistory> getMstMoldSpecHistoryCollection() {
        return mstMoldSpecHistoryCollection;
    }

    public void setMstMoldSpecHistoryCollection(Collection<MstMoldSpecHistory> mstMoldSpecHistoryCollection) {
        this.mstMoldSpecHistoryCollection = mstMoldSpecHistoryCollection;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @return the ownerCompanyId
     */
    public String getOwnerCompanyId() {
        return ownerCompanyId;
    }

    /**
     * @param ownerCompanyId the ownerCompanyId to set
     */
    public void setOwnerCompanyId(String ownerCompanyId) {
        this.ownerCompanyId = ownerCompanyId;
    }

    /**
     * @return the locationId
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the mstLocation
     */
    public MstLocation getMstLocation() {
        return mstLocation;
    }

    /**
     * @param mstLocation the mstLocation to set
     */
    public void setMstLocation(MstLocation mstLocation) {
        this.mstLocation = mstLocation;
    }

    /**
     * @return the instllationSiteId
     */
    public String getInstllationSiteId() {
        return instllationSiteId;
    }

    /**
     * @param instllationSiteId the instllationSiteId to set
     */
    public void setInstllationSiteId(String instllationSiteId) {
        this.instllationSiteId = instllationSiteId;
    }

    /**
     * @return the mstInstallationSite
     */
    public MstInstallationSite getMstInstallationSite() {
        return mstInstallationSite;
    }

    /**
     * @param mstInstallationSite the mstInstallationSite to set
     */
    public void setMstInstallationSite(MstInstallationSite mstInstallationSite) {
        this.mstInstallationSite = mstInstallationSite;
    }

    /**
     * @return the mstCompanyByCompanyId
     */
    public MstCompany getMstCompanyByCompanyId() {
        return mstCompanyByCompanyId;
    }

    /**
     * @param mstCompanyByCompanyId the mstCompanyByCompanyId to set
     */
    public void setMstCompanyByCompanyId(MstCompany mstCompanyByCompanyId) {
        this.mstCompanyByCompanyId = mstCompanyByCompanyId;
    }

    /**
     * @return the mstCompanyByOwnerCompanyId
     */
    public MstCompany getMstCompanyByOwnerCompanyId() {
        return mstCompanyByOwnerCompanyId;
    }

    /**
     * @param mstCompanyByOwnerCompanyId the mstCompanyByOwnerCompanyId to set
     */
    public void setMstCompanyByOwnerCompanyId(MstCompany mstCompanyByOwnerCompanyId) {
        this.mstCompanyByOwnerCompanyId = mstCompanyByOwnerCompanyId;
    }

    @XmlTransient
    public Collection<TblMoldMaintenanceRemodeling> getTblMoldMaintenanceRemodelingCollection() {
        return tblMoldMaintenanceRemodelingCollection;
    }

    public void setTblMoldMaintenanceRemodelingCollection(Collection<TblMoldMaintenanceRemodeling> tblMoldMaintenanceRemodelingCollection) {
        this.tblMoldMaintenanceRemodelingCollection = tblMoldMaintenanceRemodelingCollection;
    }

    /**
     * @return the latestInventoryId
     */
    public String getLatestInventoryId() {
        return latestInventoryId;
    }

    /**
     * @param latestInventoryId the latestInventoryId to set
     */
    public void setLatestInventoryId(String latestInventoryId) {
        this.latestInventoryId = latestInventoryId;
    }

    /**
     * @return the tblMoldInventory
     */
    @XmlTransient
    public TblMoldInventory getTblMoldInventory() {
        return tblMoldInventory;
    }

    /**
     * @param tblMoldInventory the tblMoldInventory to set
     */
    public void setTblMoldInventory(TblMoldInventory tblMoldInventory) {
        this.tblMoldInventory = tblMoldInventory;
    }

    @XmlTransient
    public Collection<TblIssue> getTblIssueCollection() {
        return tblIssueCollection;
    }

    public void setTblIssueCollection(Collection<TblIssue> tblIssueCollection) {
        this.tblIssueCollection = tblIssueCollection;
    }

    /**
     * @return the mainteStatus
     */
    public Integer getMainteStatus() {
        return mainteStatus;
    }

    /**
     * @param mainteStatus the mainteStatus to set
     */
    public void setMainteStatus(Integer mainteStatus) {
        this.mainteStatus = mainteStatus;
    }

    @XmlTransient
    public Collection<TblDirection> getTblDirectionCollection() {
        return tblDirectionCollection;
    }

    public void setTblDirectionCollection(Collection<TblDirection> tblDirectionCollection) {
        this.tblDirectionCollection = tblDirectionCollection;
    }

    /**
     * @return the reportFilePath01
     */
    public String getReportFilePath01() {
        return reportFilePath01;
    }

    /**
     * @param reportFilePath01 the reportFilePath01 to set
     */
    public void setReportFilePath01(String reportFilePath01) {
        this.reportFilePath01 = reportFilePath01;
    }

    /**
     * @return the reportFilePath02
     */
    public String getReportFilePath02() {
        return reportFilePath02;
    }

    /**
     * @param reportFilePath02 the reportFilePath02 to set
     */
    public void setReportFilePath02(String reportFilePath02) {
        this.reportFilePath02 = reportFilePath02;
    }

    /**
     * @return the reportFilePath03
     */
    public String getReportFilePath03() {
        return reportFilePath03;
    }

    /**
     * @param reportFilePath03 the reportFilePath03 to set
     */
    public void setReportFilePath03(String reportFilePath03) {
        this.reportFilePath03 = reportFilePath03;
    }

    /**
     * @return the reportFilePath04
     */
    public String getReportFilePath04() {
        return reportFilePath04;
    }

    /**
     * @param reportFilePath04 the reportFilePath04 to set
     */
    public void setReportFilePath04(String reportFilePath04) {
        this.reportFilePath04 = reportFilePath04;
    }

    /**
     * @return the reportFilePath05
     */
    public String getReportFilePath05() {
        return reportFilePath05;
    }

    /**
     * @param reportFilePath05 the reportFilePath05 to set
     */
    public void setReportFilePath05(String reportFilePath05) {
        this.reportFilePath05 = reportFilePath05;
    }

    /**
     * @return the reportFilePath06
     */
    public String getReportFilePath06() {
        return reportFilePath06;
    }

    /**
     * @param reportFilePath06 the reportFilePath06 to set
     */
    public void setReportFilePath06(String reportFilePath06) {
        this.reportFilePath06 = reportFilePath06;
    }

    /**
     * @return the reportFilePath07
     */
    public String getReportFilePath07() {
        return reportFilePath07;
    }

    /**
     * @param reportFilePath07 the reportFilePath07 to set
     */
    public void setReportFilePath07(String reportFilePath07) {
        this.reportFilePath07 = reportFilePath07;
    }

    /**
     * @return the reportFilePath08
     */
    public String getReportFilePath08() {
        return reportFilePath08;
    }

    /**
     * @param reportFilePath08 the reportFilePath08 to set
     */
    public void setReportFilePath08(String reportFilePath08) {
        this.reportFilePath08 = reportFilePath08;
    }

    /**
     * @return the reportFilePath09
     */
    public String getReportFilePath09() {
        return reportFilePath09;
    }

    /**
     * @param reportFilePath09 the reportFilePath09 to set
     */
    public void setReportFilePath09(String reportFilePath09) {
        this.reportFilePath09 = reportFilePath09;
    }

    /**
     * @return the reportFilePath10
     */
    public String getReportFilePath10() {
        return reportFilePath10;
    }

    /**
     * @param reportFilePath10 the reportFilePath10 to set
     */
    public void setReportFilePath10(String reportFilePath10) {
        this.reportFilePath10 = reportFilePath10;
    }

    /**
     * @return the mstCreateUser
     */
    public MstUser getMstCreateUser() {
        return mstCreateUser;
    }

    /**
     * @param mstCreateUser the mstCreateUser to set
     */
    public void setMstCreateUser(MstUser mstCreateUser) {
        this.mstCreateUser = mstCreateUser;
    }

    /**
     * @return the mstUpdateUser
     */
    public MstUser getMstUpdateUser() {
        return mstUpdateUser;
    }

    /**
     * @param mstUpdateUser the mstUpdateUser to set
     */
    public void setMstUpdateUser(MstUser mstUpdateUser) {
        this.mstUpdateUser = mstUpdateUser;
    }

    /**
     * @return the department
     */
    public Integer getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(Integer department) {
        this.department = department;
    }

    public Date getLastProductionDate() {
        return lastProductionDate;
    }

    public void setLastProductionDate(Date lastProductionDate) {
        this.lastProductionDate = lastProductionDate;
    }

    public Date getLastMainteDate() {
        return lastMainteDate;
    }

    public void setLastMainteDate(Date lastMainteDate) {
        this.lastMainteDate = lastMainteDate;
    }

    public Integer getAfterMainteTotalShotCount() {
        return afterMainteTotalShotCount;
    }

    public void setAfterMainteTotalShotCount(Integer afterMainteTotalShotCount) {
        this.afterMainteTotalShotCount = afterMainteTotalShotCount;
    }

    /**
     * @return the mainteCycleId01
     */
    public String getMainteCycleId01() {
        return mainteCycleId01;
    }

    /**
     * @param mainteCycleId01 the mainteCycleId01 to set
     */
    public void setMainteCycleId01(String mainteCycleId01) {
        this.mainteCycleId01 = mainteCycleId01;
    }

    /**
     * @return the mainteCycleId02
     */
    public String getMainteCycleId02() {
        return mainteCycleId02;
    }

    /**
     * @param mainteCycleId02 the mainteCycleId02 to set
     */
    public void setMainteCycleId02(String mainteCycleId02) {
        this.mainteCycleId02 = mainteCycleId02;
    }

    /**
     * @return the mainteCycleId03
     */
    public String getMainteCycleId03() {
        return mainteCycleId03;
    }

    /**
     * @param mainteCycleId03 the mainteCycleId03 to set
     */
    public void setMainteCycleId03(String mainteCycleId03) {
        this.mainteCycleId03 = mainteCycleId03;
    }

    /**
     * @return the blMaintenanceCyclePtn01
     */
    public TblMaintenanceCyclePtn getBlMaintenanceCyclePtn01() {
        return blMaintenanceCyclePtn01;
    }

    /**
     * @param blMaintenanceCyclePtn01 the blMaintenanceCyclePtn01 to set
     */
    public void setBlMaintenanceCyclePtn01(TblMaintenanceCyclePtn blMaintenanceCyclePtn01) {
        this.blMaintenanceCyclePtn01 = blMaintenanceCyclePtn01;
    }

    /**
     * @return the blMaintenanceCyclePtn02
     */
    public TblMaintenanceCyclePtn getBlMaintenanceCyclePtn02() {
        return blMaintenanceCyclePtn02;
    }

    /**
     * @param blMaintenanceCyclePtn02 the blMaintenanceCyclePtn02 to set
     */
    public void setBlMaintenanceCyclePtn02(TblMaintenanceCyclePtn blMaintenanceCyclePtn02) {
        this.blMaintenanceCyclePtn02 = blMaintenanceCyclePtn02;
    }

    /**
     * @return the blMaintenanceCyclePtn03
     */
    public TblMaintenanceCyclePtn getBlMaintenanceCyclePtn03() {
        return blMaintenanceCyclePtn03;
    }

    /**
     * @param blMaintenanceCyclePtn03 the blMaintenanceCyclePtn03 to set
     */
    public void setBlMaintenanceCyclePtn03(TblMaintenanceCyclePtn blMaintenanceCyclePtn03) {
        this.blMaintenanceCyclePtn03 = blMaintenanceCyclePtn03;
    }

    /**
     * @return the totalProducingTimeHour
     */
    public BigDecimal getTotalProducingTimeHour() {
        return totalProducingTimeHour;
    }

    /**
     * @param totalProducingTimeHour the totalProducingTimeHour to set
     */
    public void setTotalProducingTimeHour(BigDecimal totalProducingTimeHour) {
        this.totalProducingTimeHour = totalProducingTimeHour;
    }

    /**
     * @return the afterMainteTotalProducingTimeHour
     */
    public BigDecimal getAfterMainteTotalProducingTimeHour() {
        return afterMainteTotalProducingTimeHour;
    }

    /**
     * @param afterMainteTotalProducingTimeHour the afterMainteTotalProducingTimeHour to set
     */
    public void setAfterMainteTotalProducingTimeHour(BigDecimal afterMainteTotalProducingTimeHour) {
        this.afterMainteTotalProducingTimeHour = afterMainteTotalProducingTimeHour;
    }
    
    /**
     * @return the lastMainteDateStr
     */
    public String getLastMainteDateStr() {
        return lastMainteDateStr;
    }

    /**
     * @param lastMainteDateStr the lastMainteDateStr to set
     */
    public void setLastMainteDateStr(String lastMainteDateStr) {
        this.lastMainteDateStr = lastMainteDateStr;
    }

    /**
     * @return the inventoryStatus
     */
    public int getInventoryStatus() {
        return inventoryStatus;
    }

    /**
     * @param inventoryStatus the inventoryStatus to set
     */
    public void setInventoryStatus(int inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }
}
