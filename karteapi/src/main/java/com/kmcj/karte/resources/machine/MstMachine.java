package com.kmcj.karte.resources.machine;

import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.machine.inventory.TblMachineInventory;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtn;
import com.kmcj.karte.resources.sigma.MstSigma;
import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "mst_machine")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMachine.findAll", query = "SELECT m FROM MstMachine m"),
    @NamedQuery(name = "MstMachine.findByUuid", query = "SELECT m FROM MstMachine m WHERE m.uuid = :uuid"),
    @NamedQuery(name = "MstMachine.findByMachineId", query = "SELECT m FROM MstMachine m WHERE m.machineId = :machineId"),
    @NamedQuery(name = "MstMachine.findByMachineName", query = "SELECT m FROM MstMachine m WHERE m.machineName = :machineName"),
    @NamedQuery(name = "MstMachine.findByMacKey", query = "SELECT m FROM MstMachine m WHERE m.macKey = :macKey"),
    @NamedQuery(name = "MstMachine.findByMachineType", query = "SELECT m FROM MstMachine m WHERE m.machineType = :machineType"),
    @NamedQuery(name = "MstMachine.findByMainAssetNo", query = "SELECT m FROM MstMachine m WHERE m.mainAssetNo = :mainAssetNo"),
    @NamedQuery(name = "MstMachine.findByMachineCreatedDate", query = "SELECT m FROM MstMachine m WHERE m.machineCreatedDate = :machineCreatedDate"),
    @NamedQuery(name = "MstMachine.findByInspectedDate", query = "SELECT m FROM MstMachine m WHERE m.inspectedDate = :inspectedDate"),
    @NamedQuery(name = "MstMachine.findByInstalledDate", query = "SELECT m FROM MstMachine m WHERE m.installedDate = :installedDate"),
    @NamedQuery(name = "MstMachine.findByCompanyName", query = "SELECT m FROM MstMachine m WHERE m.companyName = :companyName"),
    @NamedQuery(name = "MstMachine.findByLocationName", query = "SELECT m FROM MstMachine m WHERE m.locationName = :locationName"),
    @NamedQuery(name = "MstMachine.findByInstllationSiteName", query = "SELECT m FROM MstMachine m WHERE m.instllationSiteName = :instllationSiteName"),
    @NamedQuery(name = "MstMachine.findByStatus", query = "SELECT m FROM MstMachine m WHERE m.status = :status"),
    @NamedQuery(name = "MstMachine.findByStatusChangedDate", query = "SELECT m FROM MstMachine m WHERE m.statusChangedDate = :statusChangedDate"),
    @NamedQuery(name = "MstMachine.findByTotalShotCount", query = "SELECT m FROM MstMachine m WHERE m.totalShotCount = :totalShotCount"),
    @NamedQuery(name = "MstMachine.findByImgFilePath01", query = "SELECT m FROM MstMachine m WHERE m.imgFilePath01 = :imgFilePath01"),
    @NamedQuery(name = "MstMachine.findByImgFilePath02", query = "SELECT m FROM MstMachine m WHERE m.imgFilePath02 = :imgFilePath02"),
    @NamedQuery(name = "MstMachine.findByImgFilePath03", query = "SELECT m FROM MstMachine m WHERE m.imgFilePath03 = :imgFilePath03"),
    @NamedQuery(name = "MstMachine.findByImgFilePath04", query = "SELECT m FROM MstMachine m WHERE m.imgFilePath04 = :imgFilePath04"),
    @NamedQuery(name = "MstMachine.findByImgFilePath05", query = "SELECT m FROM MstMachine m WHERE m.imgFilePath05 = :imgFilePath05"),
    @NamedQuery(name = "MstMachine.findByImgFilePath06", query = "SELECT m FROM MstMachine m WHERE m.imgFilePath06 = :imgFilePath06"),
    @NamedQuery(name = "MstMachine.findByImgFilePath07", query = "SELECT m FROM MstMachine m WHERE m.imgFilePath07 = :imgFilePath07"),
    @NamedQuery(name = "MstMachine.findByImgFilePath08", query = "SELECT m FROM MstMachine m WHERE m.imgFilePath08 = :imgFilePath08"),
    @NamedQuery(name = "MstMachine.findByImgFilePath09", query = "SELECT m FROM MstMachine m WHERE m.imgFilePath09 = :imgFilePath09"),
    @NamedQuery(name = "MstMachine.findByImgFilePath10", query = "SELECT m FROM MstMachine m WHERE m.imgFilePath10 = :imgFilePath10"),
    @NamedQuery(name = "MstMachine.findByMainteStatus", query = "SELECT m FROM MstMachine m WHERE m.mainteStatus = :mainteStatus"),
    @NamedQuery(name = "MstMachine.findByCreateDate", query = "SELECT m FROM MstMachine m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMachine.findByUpdateDate", query = "SELECT m FROM MstMachine m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMachine.findByCreateUserUuid", query = "SELECT m FROM MstMachine m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMachine.findByUpdateUserUuid", query = "SELECT m FROM MstMachine m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMachine.delete", query = "DELETE FROM MstMachine m WHERE m.machineId = :machineId"),
    @NamedQuery(name = "MstMachine.findByMachineIdAndName", query = "SELECT m FROM MstMachine m WHERE m.machineId = :machineId and m.machineName = :machineName"),
    @NamedQuery(name = "MstMachine.updateAssetNoByMachineId", query = "UPDATE MstMachine m SET m.mainAssetNo = :mainAssetNo WHERE m.machineId = :machineId "),
    @NamedQuery(name = "MstMachine.updateByMachineId", query = "UPDATE MstMachine m SET m.machineName = :machineName,m.machineType = :machineType,m.mainAssetNo = :mainAssetNo,m.machineCreatedDate = :machineCreatedDate,m.inspectedDate = :inspectedDate,m.ownerCompanyId = :ownerCompanyId,m.installedDate = :installedDate,m.companyId = :companyId,m.locationId = :locationId,m.instllationSiteId = :instllationSiteId,m.companyName = :companyName,m.locationName = :locationName,m.instllationSiteName = :instllationSiteName,m.status = :status,m.statusChangedDate = :statusChangedDate,m.imgFilePath01 = :imgFilePath01, "
            + "m.imgFilePath02 = :imgFilePath02, "
            + "m.imgFilePath03 = :imgFilePath03, "
            + "m.imgFilePath04 = :imgFilePath04, "
            + "m.imgFilePath05 = :imgFilePath05, "
            + "m.imgFilePath06 = :imgFilePath06, "
            + "m.imgFilePath07 = :imgFilePath07, "
            + "m.imgFilePath08 = :imgFilePath08, "
            + "m.imgFilePath09 = :imgFilePath09, "
            + "m.imgFilePath10 = :imgFilePath10, "
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid "
            + "WHERE m.machineId = :machineId"),
    @NamedQuery(name = "MstMachine.updateByMachineIdForLocationHistory", query = "UPDATE MstMachine m SET "
            + "m.installedDate = :installedDate,"
            + "m.companyId = :companyId,"
            + "m.locationId = :locationId,"
            + "m.instllationSiteId = :instllationSiteId,"
            + "m.companyName = :companyName,"
            + "m.locationName = :locationName,"
            + "m.instllationSiteName = :instllationSiteName,"
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid "
            + "WHERE m.machineId = :machineId"),
    @NamedQuery(name = "MstMachine.updateByMachineIdForTblMachineInventory", query = "UPDATE MstMachine m SET "
            + "m.companyId = :companyId,"
            + "m.companyName = :companyName,"
            + "m.locationId = :locationId,"
            + "m.locationName = :locationName,"
            + "m.instllationSiteId = :instllationSiteId,"
            + "m.instllationSiteName = :instllationSiteName,"
            + "m.latestInventoryId = :latestInventoryId,"
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid "
            + "WHERE m.machineId = :machineId"),
    
    //  　会社マスタ削除する前にＦＫチェックＳＱＬ文
    @NamedQuery(name = "MstMachine.findFkByCompanyId", query = "SELECT m FROM MstMachine m WHERE m.companyId = :companyId "),
    @NamedQuery(name = "MstMachine.findFkByOwnerCompanyId", query = "SELECT m FROM MstMachine m WHERE m.ownerCompanyId = :companyId "),
    // 　所在地マスタ削除する前にＦＫチェックＳＱＬ文
    @NamedQuery(name = "MstMachine.findFkByLocationId", query = "SELECT m FROM MstMachine m WHERE m.locationId = :locationId "),
    // 　設置場所マスタ削除する前にＦＫチェックＳＱＬ文
    @NamedQuery(name = "MstMachine.findFkByInstallationSiteId", query = "SELECT m FROM MstMachine m WHERE m.instllationSiteId = :instllationSiteId "),
    
    @NamedQuery(name = "MstMachine.findTblMachineInventoryByid", query = "SELECT t FROM MstMachine t LEFT JOIN FETCH t.latestTblMachineInventory mi WHERE t.machineId = :machineId order by mi.inventoryDate DESC "),
    @NamedQuery(name = "MstMachine.findByMachineIdList", query = "SELECT m FROM MstMachine m WHERE m.machineId IN :machineIdList")
})
@Cacheable(value = false)
public class MstMachine implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MACHINE_ID")
    private String machineId;
    @Size(max = 100)
    @Column(name = "MACHINE_NAME")
    private String machineName;
    @Column(name = "MACHINE_TYPE")
    private Integer machineType;
    @Size(max = 45)
    @Column(name = "MAIN_ASSET_NO")
    private String mainAssetNo;
    @Column(name = "MACHINE_CREATED_DATE")
    @Temporal(TemporalType.DATE)
    private Date machineCreatedDate;
    @Column(name = "INSPECTED_DATE")
    @Temporal(TemporalType.DATE)
    private Date inspectedDate;
    @Column(name = "INSTALLED_DATE")
    @Temporal(TemporalType.DATE)
    private Date installedDate;
    @Size(max = 100)
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Size(max = 100)
    @Column(name = "LOCATION_NAME")
    private String locationName;
    @Size(max = 100)
    @Column(name = "INSTLLATION_SITE_NAME")
    private String instllationSiteName;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "STATUS_CHANGED_DATE")
    @Temporal(TemporalType.DATE)
    private Date statusChangedDate;
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
    @Column(name = "MAINTE_STATUS")
    private Integer mainteStatus;
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
    @JoinColumn(name = "LATEST_INVENTORY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblMachineInventory latestTblMachineInventory;
    @Column(name = "LATEST_INVENTORY_ID")
    private String latestInventoryId;

    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompany;
    @Column(name = "COMPANY_ID")
    private String companyId;
    @JoinColumn(name = "INSTLLATION_SITE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstInstallationSite mstInstallationSite;
    @Column(name = "INSTLLATION_SITE_ID")
    private String instllationSiteId;
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstLocation mstLocation;
    @Column(name = "LOCATION_ID")
    private String locationId;
    @JoinColumn(name = "OWNER_COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany ownerMstCompany;
    @Column(name = "OWNER_COMPANY_ID")
    private String ownerCompanyId;

    @Size(max = 50)
    @Column(name = "MAC_KEY")
    private String macKey;
    @Column(name = "DEPARTMENT")
    private Integer department;
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
    @JoinColumn(name = "SIGMA_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstSigma mstSigma;
    @Column(name = "SIGMA_ID")
    private String sigmaId;

    @Column(name = "BASE_CYCLE_TIME")
    private BigDecimal baseCycleTime;
    @Size(max = 45)
    @Column(name = "MACHINE_CD")
    private String machineCd;
    @Size(max = 45)
    @Column(name = "STRAGE_LOCATION_CD")
    private String strageLocationCd;
    @Size(max = 50)
    @Column(name = "CHARGE_CD")
    private String chargeCd;
    @Size(max = 50)
    @Column(name = "OPERATOR_CD")
    private String operatorCd;
    
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

    @Column(name = "INVENTORY_STATUS")
    private int inventoryStatus;
    
    @Transient
    private String lastMainteDateStr;

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
    
    public MstMachine() {
    }

    public MstMachine(String machineId) {
        this.machineId = machineId;
    }

    public MstMachine(String machineId, String uuid) {
        this.machineId = machineId;
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public Integer getMachineType() {
        return machineType;
    }

    public void setMachineType(Integer machineType) {
        this.machineType = machineType;
    }

    public String getMainAssetNo() {
        return mainAssetNo;
    }

    public void setMainAssetNo(String mainAssetNo) {
        this.mainAssetNo = mainAssetNo;
    }

    public Date getMachineCreatedDate() {
        return machineCreatedDate;
    }

    public void setMachineCreatedDate(Date machineCreatedDate) {
        this.machineCreatedDate = machineCreatedDate;
    }

    public Date getInspectedDate() {
        return inspectedDate;
    }

    public void setInspectedDate(Date inspectedDate) {
        this.inspectedDate = inspectedDate;
    }

    public Date getInstalledDate() {
        return installedDate;
    }

    public void setInstalledDate(Date installedDate) {
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

    public Date getStatusChangedDate() {
        return statusChangedDate;
    }

    public void setStatusChangedDate(Date statusChangedDate) {
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

    public Integer getMainteStatus() {
        return mainteStatus;
    }

    public void setMainteStatus(Integer mainteStatus) {
        this.mainteStatus = mainteStatus;
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

    public void setLatestTblMachineInventory(TblMachineInventory latestTblMachineInventory) {
        this.latestTblMachineInventory = latestTblMachineInventory;
    }

    public void setLatestInventoryId(String latestInventoryId) {
        this.latestInventoryId = latestInventoryId;
    }
    @XmlTransient
    public TblMachineInventory getLatestTblMachineInventory() {
        return latestTblMachineInventory;
    }

    public String getLatestInventoryId() {
        return latestInventoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (machineId != null ? machineId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMachine)) {
            return false;
        }
        MstMachine other = (MstMachine) object;
        if ((this.machineId == null && other.machineId != null) || (this.machineId != null && !this.machineId.equals(other.machineId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.MstMachine[ machineId=" + machineId + " ]";
    }

    public void setMstCompany(MstCompany mstCompany) {
        this.mstCompany = mstCompany;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setMstInstallationSite(MstInstallationSite mstInstallationSite) {
        this.mstInstallationSite = mstInstallationSite;
    }

    public void setInstllationSiteId(String instllationSiteId) {
        this.instllationSiteId = instllationSiteId;
    }

    public void setMstLocation(MstLocation mstLocation) {
        this.mstLocation = mstLocation;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public void setOwnerMstCompany(MstCompany ownerMstCompany) {
        this.ownerMstCompany = ownerMstCompany;
    }

    public void setOwnerCompanyId(String ownerCompanyId) {
        this.ownerCompanyId = ownerCompanyId;
    }

    public MstCompany getMstCompany() {
        return mstCompany;
    }

    public String getCompanyId() {
        return companyId;
    }

    public MstInstallationSite getMstInstallationSite() {
        return mstInstallationSite;
    }

    public String getInstllationSiteId() {
        return instllationSiteId;
    }

    public MstLocation getMstLocation() {
        return mstLocation;
    }

    public String getLocationId() {
        return locationId;
    }

    public MstCompany getOwnerMstCompany() {
        return ownerMstCompany;
    }

    public String getOwnerCompanyId() {
        return ownerCompanyId;
    }

    public String getMacKey() {
        return macKey;
    }

    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public String getReportFilePath01() {
        return reportFilePath01;
    }

    public void setReportFilePath01(String reportFilePath01) {
        this.reportFilePath01 = reportFilePath01;
    }

    public String getReportFilePath02() {
        return reportFilePath02;
    }

    public void setReportFilePath02(String reportFilePath02) {
        this.reportFilePath02 = reportFilePath02;
    }

    public String getReportFilePath03() {
        return reportFilePath03;
    }

    public void setReportFilePath03(String reportFilePath03) {
        this.reportFilePath03 = reportFilePath03;
    }

    public String getReportFilePath04() {
        return reportFilePath04;
    }

    public void setReportFilePath04(String reportFilePath04) {
        this.reportFilePath04 = reportFilePath04;
    }

    public String getReportFilePath05() {
        return reportFilePath05;
    }

    public void setReportFilePath05(String reportFilePath05) {
        this.reportFilePath05 = reportFilePath05;
    }

    public String getReportFilePath06() {
        return reportFilePath06;
    }

    public void setReportFilePath06(String reportFilePath06) {
        this.reportFilePath06 = reportFilePath06;
    }

    public String getReportFilePath07() {
        return reportFilePath07;
    }

    public void setReportFilePath07(String reportFilePath07) {
        this.reportFilePath07 = reportFilePath07;
    }

    public String getReportFilePath08() {
        return reportFilePath08;
    }

    public void setReportFilePath08(String reportFilePath08) {
        this.reportFilePath08 = reportFilePath08;
    }

    public String getReportFilePath09() {
        return reportFilePath09;
    }

    public void setReportFilePath09(String reportFilePath09) {
        this.reportFilePath09 = reportFilePath09;
    }

    public String getReportFilePath10() {
        return reportFilePath10;
    }

    public void setReportFilePath10(String reportFilePath10) {
        this.reportFilePath10 = reportFilePath10;
    }

    public void setMstSigma(MstSigma mstSigma) {
        this.mstSigma = mstSigma;
    }

    public void setSigmaId(String sigmaId) {
        this.sigmaId = sigmaId;
    }

    public MstSigma getMstSigma() {
        return mstSigma;
    }

    public String getSigmaId() {
        return sigmaId;
    }

    public BigDecimal getBaseCycleTime() {
        return baseCycleTime;
    }

    public void setBaseCycleTime(BigDecimal baseCycleTime) {
        this.baseCycleTime = baseCycleTime;
    }

    public String getMachineCd() {
        return machineCd;
    }

    public void setMachineCd(String machineCd) {
        this.machineCd = machineCd;
    }

    public String getStrageLocationCd() {
        return strageLocationCd;
    }

    public void setStrageLocationCd(String strageLocationCd) {
        this.strageLocationCd = strageLocationCd;
    }

    public String getChargeCd() {
        return chargeCd;
    }

    public void setChargeCd(String chargeCd) {
        this.chargeCd = chargeCd;
    }

    public String getOperatorCd() {
        return operatorCd;
    }

    public void setOperatorCd(String operatorCd) {
        this.operatorCd = operatorCd;
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

    public Date getLastProductionDate() {
        return lastProductionDate;
    }

    public void setLastProductionDate(Date lastProductionDate) {
        this.lastProductionDate = lastProductionDate;
    }

    public BigDecimal getTotalProducingTimeHour() {
        return totalProducingTimeHour;
    }

    public void setTotalProducingTimeHour(BigDecimal totalProducingTimeHour) {
        this.totalProducingTimeHour = totalProducingTimeHour;
    }

    public Date getLastMainteDate() {
        return lastMainteDate;
    }

    public void setLastMainteDate(Date lastMainteDate) {
        this.lastMainteDate = lastMainteDate;
    }

    public BigDecimal getAfterMainteTotalProducingTimeHour() {
        return afterMainteTotalProducingTimeHour;
    }

    public void setAfterMainteTotalProducingTimeHour(BigDecimal afterMainteTotalProducingTimeHour) {
        this.afterMainteTotalProducingTimeHour = afterMainteTotalProducingTimeHour;
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
