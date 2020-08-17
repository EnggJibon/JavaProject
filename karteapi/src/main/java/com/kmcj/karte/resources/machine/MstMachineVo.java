package com.kmcj.karte.resources.machine;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.machine.inventory.TblMachineInventory;
import com.kmcj.karte.resources.machine.location.history.TblMachineLocationHistoryVo;
import com.kmcj.karte.resources.machine.proccond.MstMachineProcCondVo;
import com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpecVo;
import com.kmcj.karte.resources.machine.spec.MstMachineSpec;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineVo extends BasicResponse {

    private String uuid;

    private String machineId;

    private String machineName;

    private String machineUuid;

    private Integer machineType;

    private String mainAssetNo;

    private Date machineCreatedDate;
    private String machineCreatedDateStr;

    private Date inspectedDate;
    private String inspectedDateStr;

    private Date installedDate;
    private String installedDateStr;

    private String companyName;

    private String locationName;

    private String instllationSiteName;

    private Integer status;

    private Date statusChangedDate;
    private String statusChangedDateStr;

    private String totalShotCount;

    private String imgFilePath01;

    private String imgFilePath02;

    private String imgFilePath03;

    private String imgFilePath04;

    private String imgFilePath05;

    private String imgFilePath06;

    private String imgFilePath07;

    private String imgFilePath08;

    private String imgFilePath09;

    private String imgFilePath10;

    private String reportFilePath01;
    private String reportFilePath02;
    private String reportFilePath03;
    private String reportFilePath04;
    private String reportFilePath05;
    private String reportFilePath06;
    private String reportFilePath07;
    private String reportFilePath08;
    private String reportFilePath09;
    private String reportFilePath10;
    
    private String reportFilePathName01;
    private String reportFilePathName02;
    private String reportFilePathName03;
    private String reportFilePathName04;
    private String reportFilePathName05;
    private String reportFilePathName06;
    private String reportFilePathName07;
    private String reportFilePathName08;
    private String reportFilePathName09;
    private String reportFilePathName10;

    private Integer mainteStatus;

    private Date createDate;
    private String createDateStr;

    private Date updateDate;
    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private TblMachineInventory latestTblMachineInventory;

    private String latestInventoryId;

    private MstCompany mstCompany;

    private String companyId;

    private MstInstallationSite mstInstallationSite;

    private String instllationSiteId;

    private MstLocation mstLocation;

    private String locationId;

    private MstCompany ownerMstCompany;
    private String ownerCompanyName;
    private String ownerCompanyId;

    private Date CreatedDate;
    private String CreatedDateStr;

    private MstMachine mstMachine;

    private List<MstComponent> mstComponents;
    private List<MstMachineProcCondVo> mstMachineProcCondVos;
    private List<MstMachineProcCondSpecVo> mstMachineProcCondSpecVos;
    private List<TblMachineLocationHistoryVo> tblMachineLocationHistoryVos;
    private List<MstMachineSpec> mstMachineSpec;

    private Integer externalFlg;

    private String machineTypeText;
    private String mainteStatusText;

    private Integer department;
    private String departmentId;
    private String departmentName;
    private String sigmaId;
    private String sigmaCode;
    private String macKey;

    private String baseCycleTime;

    private String machineCd;

    private String strageLocationCd;

    private String chargeCd;

    private String operatorCd;
    
    /**  KM-147 対応　*/
    private String createUserName;
    private String updateUserName;
    
    /**
     * 4.2ライルサイクル管理 対応
     */
    private Date lastProductionDate;
    private String lastProductionDateStr;
    private BigDecimal totalProducingTimeHour;
    private Date lastMainteDate;
    private String lastMainteDateStr;
    private BigDecimal afterMainteTotalProducingTimeHour;
    private Integer afterMainteTotalShotCount;
    private String mainteCycleId01;
    private String mainteCycleId02;
    private String mainteCycleId03;
    private String mainteCycleCode01;
    private String mainteCycleCode02;
    private String mainteCycleCode03;
    private int sendFlg;// 0:送信不要;1:アラート送信;2:メンテナンス送信
    private int msgFlg;// 0:表示不要;1:アラート表示;2:メンテナンス表示
    private String actualMainteCycleId;
    private String mainteReasonText;
    // KM-360 対応 start
    private int hitCondition;
    // KM-360 対応 end

    public MstMachineVo() {
    }

    public MstMachineVo(String machineId) {
        this.machineId = machineId;
    }

    public MstMachineVo(String machineId, String uuid) {
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

    public String getTotalShotCount() {
        return totalShotCount;
    }

    public void setTotalShotCount(String totalShotCount) {
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

    public void setReportFilePath01(String reportFilePath01) {
        this.reportFilePath01 = reportFilePath01;
    }

    public void setReportFilePath02(String reportFilePath02) {
        this.reportFilePath02 = reportFilePath02;
    }

    public void setReportFilePath03(String reportFilePath03) {
        this.reportFilePath03 = reportFilePath03;
    }

    public void setReportFilePath04(String reportFilePath04) {
        this.reportFilePath04 = reportFilePath04;
    }

    public void setReportFilePath05(String reportFilePath05) {
        this.reportFilePath05 = reportFilePath05;
    }

    public void setReportFilePath06(String reportFilePath06) {
        this.reportFilePath06 = reportFilePath06;
    }

    public void setReportFilePath07(String reportFilePath07) {
        this.reportFilePath07 = reportFilePath07;
    }

    public void setReportFilePath08(String reportFilePath08) {
        this.reportFilePath08 = reportFilePath08;
    }

    public void setReportFilePath09(String reportFilePath09) {
        this.reportFilePath09 = reportFilePath09;
    }

    public void setReportFilePath10(String reportFilePath10) {
        this.reportFilePath10 = reportFilePath10;
    }

    public String getReportFilePath01() {
        return reportFilePath01;
    }

    public String getReportFilePath02() {
        return reportFilePath02;
    }

    public String getReportFilePath03() {
        return reportFilePath03;
    }

    public String getReportFilePath04() {
        return reportFilePath04;
    }

    public String getReportFilePath05() {
        return reportFilePath05;
    }

    public String getReportFilePath06() {
        return reportFilePath06;
    }

    public String getReportFilePath07() {
        return reportFilePath07;
    }

    public String getReportFilePath08() {
        return reportFilePath08;
    }

    public String getReportFilePath09() {
        return reportFilePath09;
    }

    public String getReportFilePath10() {
        return reportFilePath10;
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

    public TblMachineInventory getLatestTblMachineInventory() {
        return latestTblMachineInventory;
    }

    public String getLatestInventoryId() {
        return latestInventoryId;
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

    public void setMachineCreatedDateStr(String machineCreatedDateStr) {
        this.machineCreatedDateStr = machineCreatedDateStr;
    }

    public void setInspectedDateStr(String inspectedDateStr) {
        this.inspectedDateStr = inspectedDateStr;
    }

    public void setInstalledDateStr(String installedDateStr) {
        this.installedDateStr = installedDateStr;
    }

    public void setStatusChangedDateStr(String statusChangedDateStr) {
        this.statusChangedDateStr = statusChangedDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public String getMachineCreatedDateStr() {
        return machineCreatedDateStr;
    }

    public String getInspectedDateStr() {
        return inspectedDateStr;
    }

    public String getInstalledDateStr() {
        return installedDateStr;
    }

    public String getStatusChangedDateStr() {
        return statusChangedDateStr;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public String getCreatedDateStr() {
        return CreatedDateStr;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public void setCreatedDate(Date CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    public void setCreatedDateStr(String CreatedDateStr) {
        this.CreatedDateStr = CreatedDateStr;
    }

    public MstMachine getMstMachine() {
        return mstMachine;
    }

    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public List<MstComponent> getMstComponents() {
        return mstComponents;
    }

    public void setMstComponents(List<MstComponent> mstComponents) {
        this.mstComponents = mstComponents;
    }

    public Integer getExternalFlg() {
        return externalFlg;
    }

    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
    }

    public String getMachineTypeText() {
        return machineTypeText;
    }

    public void setMachineTypeText(String machineTypeText) {
        this.machineTypeText = machineTypeText;
    }

    public String getOwnerCompanyName() {
        return ownerCompanyName;
    }

    public void setOwnerCompanyName(String ownerCompanyName) {
        this.ownerCompanyName = ownerCompanyName;
    }

    public String getMainteStatusText() {
        return mainteStatusText;
    }

    public void setMainteStatusText(String mainteStatusText) {
        this.mainteStatusText = mainteStatusText;
    }

    public List<MstMachineProcCondVo> getMstMachineProcCondVos() {
        return mstMachineProcCondVos;
    }

    public void setMstMachineProcCondVos(List<MstMachineProcCondVo> mstMachineProcCondVos) {
        this.mstMachineProcCondVos = mstMachineProcCondVos;
    }

    public List<MstMachineProcCondSpecVo> getMstMachineProcCondSpecVos() {
        return mstMachineProcCondSpecVos;
    }

    public void setMstMachineProcCondSpecVos(List<MstMachineProcCondSpecVo> mstMachineProcCondSpecVos) {
        this.mstMachineProcCondSpecVos = mstMachineProcCondSpecVos;
    }

    public List<TblMachineLocationHistoryVo> getTblMachineLocationHistoryVos() {
        return tblMachineLocationHistoryVos;
    }

    public void setTblMachineLocationHistoryVos(List<TblMachineLocationHistoryVo> tblMachineLocationHistoryVos) {
        this.tblMachineLocationHistoryVos = tblMachineLocationHistoryVos;
    }

    public void setMstMachineSpec(List<MstMachineSpec> mstMachineSpec) {
        this.mstMachineSpec = mstMachineSpec;
    }

    public List<MstMachineSpec> getMstMachineSpec() {
        return mstMachineSpec;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setSigmaId(String sigmaId) {
        this.sigmaId = sigmaId;
    }

    public String getSigmaId() {
        return sigmaId;
    }

    public void setSigmaCode(String sigmaCode) {
        this.sigmaCode = sigmaCode;
    }

    public String getSigmaCode() {
        return sigmaCode;
    }

    public String getBaseCycleTime() {
        return baseCycleTime;
    }

    public String getMachineCd() {
        return machineCd;
    }

    public String getStrageLocationCd() {
        return strageLocationCd;
    }

    public String getChargeCd() {
        return chargeCd;
    }

    public String getOperatorCd() {
        return operatorCd;
    }

    public void setBaseCycleTime(String baseCycleTime) {
        this.baseCycleTime = baseCycleTime;
    }

    public void setMachineCd(String machineCd) {
        this.machineCd = machineCd;
    }

    public void setStrageLocationCd(String strageLocationCd) {
        this.strageLocationCd = strageLocationCd;
    }

    public void setChargeCd(String chargeCd) {
        this.chargeCd = chargeCd;
    }

    public void setOperatorCd(String operatorCd) {
        this.operatorCd = operatorCd;
    }

    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }

    public String getMacKey() {
        return macKey;
    }

    public void setReportFilePathName01(String reportFilePathName01) {
        this.reportFilePathName01 = reportFilePathName01;
    }

    public void setReportFilePathName02(String reportFilePathName02) {
        this.reportFilePathName02 = reportFilePathName02;
    }

    public void setReportFilePathName03(String reportFilePathName03) {
        this.reportFilePathName03 = reportFilePathName03;
    }

    public void setReportFilePathName04(String reportFilePathName04) {
        this.reportFilePathName04 = reportFilePathName04;
    }

    public void setReportFilePathName05(String reportFilePathName05) {
        this.reportFilePathName05 = reportFilePathName05;
    }

    public void setReportFilePathName06(String reportFilePathName06) {
        this.reportFilePathName06 = reportFilePathName06;
    }

    public void setReportFilePathName07(String reportFilePathName07) {
        this.reportFilePathName07 = reportFilePathName07;
    }

    public void setReportFilePathName08(String reportFilePathName08) {
        this.reportFilePathName08 = reportFilePathName08;
    }

    public void setReportFilePathName09(String reportFilePathName09) {
        this.reportFilePathName09 = reportFilePathName09;
    }

    public void setReportFilePathName10(String reportFilePathName10) {
        this.reportFilePathName10 = reportFilePathName10;
    }

    public String getReportFilePathName01() {
        return reportFilePathName01;
    }

    public String getReportFilePathName02() {
        return reportFilePathName02;
    }

    public String getReportFilePathName03() {
        return reportFilePathName03;
    }

    public String getReportFilePathName04() {
        return reportFilePathName04;
    }

    public String getReportFilePathName05() {
        return reportFilePathName05;
    }

    public String getReportFilePathName06() {
        return reportFilePathName06;
    }

    public String getReportFilePathName07() {
        return reportFilePathName07;
    }

    public String getReportFilePathName08() {
        return reportFilePathName08;
    }

    public String getReportFilePathName09() {
        return reportFilePathName09;
    }

    public String getReportFilePathName10() {
        return reportFilePathName10;
    }

    /**
     * @return the createUserName
     */
    public String getCreateUserName() {
        return createUserName;
    }

    /**
     * @param createUserName the createUserName to set
     */
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    /**
     * @return the updateUserName
     */
    public String getUpdateUserName() {
        return updateUserName;
    }

    /**
     * @param updateUserName the updateUserName to set
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getActualMainteCycleId() {
        return actualMainteCycleId;
    }

    public BigDecimal getAfterMainteTotalProducingTimeHour() {
        return afterMainteTotalProducingTimeHour;
    }

    public Integer getAfterMainteTotalShotCount() {
        return afterMainteTotalShotCount;
    }

    public Date getLastMainteDate() {
        return lastMainteDate;
    }

    public String getLastMainteDateStr() {
        return lastMainteDateStr;
    }

    public Date getLastProductionDate() {
        return lastProductionDate;
    }

    public String getLastProductionDateStr() {
        return lastProductionDateStr;
    }

    public String getMainteCycleId01() {
        return mainteCycleId01;
    }

    public String getMainteCycleId02() {
        return mainteCycleId02;
    }

    public String getMainteCycleId03() {
        return mainteCycleId03;
    }

    public int getSendFlg() {
        return sendFlg;
    }

    public BigDecimal getTotalProducingTimeHour() {
        return totalProducingTimeHour;
    }

    public void setActualMainteCycleId(String actualMainteCycleId) {
        this.actualMainteCycleId = actualMainteCycleId;
    }

    public void setAfterMainteTotalProducingTimeHour(BigDecimal afterMainteTotalProducingTimeHour) {
        this.afterMainteTotalProducingTimeHour = afterMainteTotalProducingTimeHour;
    }

    public void setAfterMainteTotalShotCount(Integer afterMainteTotalShotCount) {
        this.afterMainteTotalShotCount = afterMainteTotalShotCount;
    }

    public void setLastMainteDate(Date lastMainteDate) {
        this.lastMainteDate = lastMainteDate;
    }

    public void setLastMainteDateStr(String lastMainteDateStr) {
        this.lastMainteDateStr = lastMainteDateStr;
    }

    public void setLastProductionDate(Date lastProductionDate) {
        this.lastProductionDate = lastProductionDate;
    }

    public void setLastProductionDateStr(String lastProductionDateStr) {
        this.lastProductionDateStr = lastProductionDateStr;
    }

    public void setMainteCycleId01(String mainteCycleId01) {
        this.mainteCycleId01 = mainteCycleId01;
    }

    public void setMainteCycleId02(String mainteCycleId02) {
        this.mainteCycleId02 = mainteCycleId02;
    }

    public void setMainteCycleId03(String mainteCycleId03) {
        this.mainteCycleId03 = mainteCycleId03;
    }

    public void setSendFlg(int sendFlg) {
        this.sendFlg = sendFlg;
    }

    public void setTotalProducingTimeHour(BigDecimal totalProducingTimeHour) {
        this.totalProducingTimeHour = totalProducingTimeHour;
    }

    public int getMsgFlg() {
        return msgFlg;
    }

    public void setMsgFlg(int msgFlg) {
        this.msgFlg = msgFlg;
    }
    
    public String getMainteReasonText() {
        return mainteReasonText;
    }

    public void setMainteReasonText(String mainteReasonText) {
        this.mainteReasonText = mainteReasonText;
    }

    /**
     * @return the mainteCycleCode01
     */
    public String getMainteCycleCode01() {
        return mainteCycleCode01;
    }

    /**
     * @param mainteCycleCode01 the mainteCycleCode01 to set
     */
    public void setMainteCycleCode01(String mainteCycleCode01) {
        this.mainteCycleCode01 = mainteCycleCode01;
    }

    /**
     * @return the mainteCycleCode02
     */
    public String getMainteCycleCode02() {
        return mainteCycleCode02;
    }

    /**
     * @param mainteCycleCode02 the mainteCycleCode02 to set
     */
    public void setMainteCycleCode02(String mainteCycleCode02) {
        this.mainteCycleCode02 = mainteCycleCode02;
    }

    /**
     * @return the mainteCycleCode03
     */
    public String getMainteCycleCode03() {
        return mainteCycleCode03;
    }

    /**
     * @param mainteCycleCode03 the mainteCycleCode03 to set
     */
    public void setMainteCycleCode03(String mainteCycleCode03) {
        this.mainteCycleCode03 = mainteCycleCode03;
    }

    public int getHitCondition() {
        return hitCondition;
    }

    public void setHitCondition(int hitCondition) {
        this.hitCondition = hitCondition;
    }
    
}
