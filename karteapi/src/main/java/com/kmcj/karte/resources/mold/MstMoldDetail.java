/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationVo;
import com.kmcj.karte.resources.mold.inventory.TblMoldInventorys;
import com.kmcj.karte.resources.mold.issue.TblIssueVo;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistory;
import com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistorys;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodelingVo;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRelDetail;
import com.kmcj.karte.resources.mold.proccond.MstMoldProcCond;
import com.kmcj.karte.resources.mold.proccond.MstMoldProcConds;
import com.kmcj.karte.resources.mold.proccond.attribute.MstMoldProcCondAttribute;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpec;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpecPK;
import com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpecs;
import com.kmcj.karte.resources.mold.spec.MstMoldSpec;
import com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistorys;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class MstMoldDetail extends BasicResponse {

    //金型マスタ
    private MstMold mstMold;
    //部品マスタ
    private List<MstComponent> mstComponentList;

    //金型加工条件属性マスタを取得する
    private List<MstMoldProcCondAttribute> mstMoldProcCondAttributeList;

    //金型属性マスターを取得する
    private List<MstMoldSpec> mstMoldSpec;

    //金型加工条件属性マスタを取得する
    private List<MstMoldProcCondSpec> mstMoldProcCondSpec;
    
    private MstMoldProcCondSpecPK mstMoldProcCondSpecPK;
    
    private List<TblMoldInventorys> tblMoldInventorys;
    
    private List<MstMoldProcCondSpecs> mstMoldProcCondSpecVos;

    private List<MstMoldProcCond> mstMoldProcCond;
    
    private List<TblIssueVo> tblIssueVos;

    private List<TblMoldLocationHistorys> tblMoldLocationHistorys;

    private List<TblMoldLocationHistory> tblMoldLocationHistory;

    private List<MstMoldSpecHistorys> mstMoldSpecHistorys;

    private List<MstMoldProcConds> mstMoldProcConds;
    
    private List<MstMoldComponentRelationVo> MstMoldComponentRelationVo;
    
    private List<TblMoldMaintenanceRemodelingVo> tblMoldMaintenanceRemodelingVos;
    
    private List<MstMoldPartRelDetail> mstMoldPartRels;

    //会社データ
    private List<MstCompany> mstCompanyList;
    private String moldUuid;
    private String moldId;
    private String moldName;
    private Integer moldType;
    private String moldTypeText;
    private String mainAssetNo;
    private Date moldCreatedDate;
    private String moldCreatedDateStr;
    private Date createdDate;
    private String createdDateStr;
    private String createUserUuid;
    private Date inspectedDate;
    private String inspectedDateStr;
    private String ownerCompanyId;
    private String department;
    private String departmentName;
    private String ownerCompanyName;
    private Date installedDate;
    private String installedDateStr;
    private String companyId;
    private String companyName;
    private String locationId;
    private String locationName;
    private String instllationSiteId;
    private String instllationSiteName;
    private Integer status;
    private Date statusChangedDate;
    private String statusChangedDateStr;
    private Integer mainteStatus;
    private String mainteStatusText;
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
    
    private String latestInventoryId;
    private String totalShotCount;
    private Date updateDate;
    private String updateDateStr;
    private String updateUserUuid;
    private int externalFlg; 
    
    /**  KM-147 対応　*/
    private String createUserName;
    private String updateUserName;

    private String useYearCount;
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
    private int msgFlg; // 0:表示不要;1:アラート表示;2:メンテナンス表示
    private String actualMainteCycleId;
    private String mainteReasonText;
    
    /**  KM-360 対応　*/
    private int hitCondition;


    /**
     * @return the mstMold
     */
    public MstMold getMstMold() {
        return mstMold;
    }

    /**
     * @param mstMold the mstMold to set
     */
    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }

    /**
     * @return the mstComponentList
     */
    public List<MstComponent> getMstComponentList() {
        return mstComponentList;
    }

    /**
     * @param mstComponentList the mstComponentList to set
     */
    public void setMstComponentList(List<MstComponent> mstComponentList) {
        this.mstComponentList = mstComponentList;
    }

    /**
     * @return the mstMoldProcCondAttributeList
     */
    public List<MstMoldProcCondAttribute> getMstMoldProcCondAttributeList() {
        return mstMoldProcCondAttributeList;
    }

    /**
     * @param mstMoldProcCondAttributeList the mstMoldProcCondAttributeList to
     * set
     */
    public void setMstMoldProcCondAttributeList(List<MstMoldProcCondAttribute> mstMoldProcCondAttributeList) {
        this.mstMoldProcCondAttributeList = mstMoldProcCondAttributeList;
    }

    /**
     * @return the mstCompanyList
     */
    public List<MstCompany> getMstCompanyList() {
        return mstCompanyList;
    }

    /**
     * @param mstCompanyList the mstCompanyList to set
     */
    public void setMstCompanyList(List<MstCompany> mstCompanyList) {
        this.mstCompanyList = mstCompanyList;
    }

    /**
     * @return the moldId
     */
    public String getMoldId() {
        return moldId;
    }

    /**
     * @param moldId the moldId to set
     */
    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    /**
     * @return the moldName
     */
    public String getMoldName() {
        return moldName;
    }

    /**
     * @param moldName the moldName to set
     */
    public void setMoldName(String moldName) {
        this.moldName = moldName;
    }

    /**
     * @return the moldType
     */
    public Integer getMoldType() {
        return moldType;
    }

    /**
     * @param moldType the moldType to set
     */
    public void setMoldType(Integer moldType) {
        this.moldType = moldType;
    }

    public String getMoldTypeText() {
        return moldTypeText;
    }

    public void setMoldTypeText(String moldTypeText) {
        this.moldTypeText = moldTypeText;
    }

    /**
     * @return the mainAssetNo
     */
    public String getMainAssetNo() {
        return mainAssetNo;
    }

    /**
     * @param mainAssetNo the mainAssetNo to set
     */
    public void setMainAssetNo(String mainAssetNo) {
        this.mainAssetNo = mainAssetNo;
    }

    /**
     * @return the moldCreatedDate
     */
    public Date getMoldCreatedDate() {
        return moldCreatedDate;
    }

    /**
     * @param moldCreatedDate the moldCreatedDate to set
     */
    public void setMoldCreatedDate(Date moldCreatedDate) {
        this.moldCreatedDate = moldCreatedDate;
    }

    /**
     * @return the inspectedDate
     */
    public Date getInspectedDate() {
        return inspectedDate;
    }

    /**
     * @param inspectedDate the inspectedDate to set
     */
    public void setInspectedDate(Date inspectedDate) {
        this.inspectedDate = inspectedDate;
    }

    /**
     * @return the ownerCompanyName
     */
    public String getOwnerCompanyName() {
        return ownerCompanyName;
    }

    /**
     * @param ownerCompanyName the ownerCompanyName to set
     */
    public void setOwnerCompanyName(String ownerCompanyName) {
        this.ownerCompanyName = ownerCompanyName;
    }

    /**
     * @return the installedDate
     */
    public Date getInstalledDate() {
        return installedDate;
    }

    /**
     * @param installedDate the installedDate to set
     */
    public void setInstalledDate(Date installedDate) {
        this.installedDate = installedDate;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName the locationName to set
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * @return the instllationSiteName
     */
    public String getInstllationSiteName() {
        return instllationSiteName;
    }

    /**
     * @param instllationSiteName the instllationSiteName to set
     */
    public void setInstllationSiteName(String instllationSiteName) {
        this.instllationSiteName = instllationSiteName;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the statusChangedDate
     */
    public Date getStatusChangedDate() {
        return statusChangedDate;
    }

    /**
     * @param statusChangedDate the statusChangedDate to set
     */
    public void setStatusChangedDate(Date statusChangedDate) {
        this.statusChangedDate = statusChangedDate;
    }

    /**
     * @return the mstMoldSpec
     */
    public List<MstMoldSpec> getMstMoldSpec() {
        return mstMoldSpec;
    }

    /**
     * @param mstMoldSpec the mstMoldSpec to set
     */
    public void setMstMoldSpec(List<MstMoldSpec> mstMoldSpec) {
        this.mstMoldSpec = mstMoldSpec;
    }

    /**
     * @return the mstMoldProcCondSpec
     */
    public List<MstMoldProcCondSpec> getMstMoldProcCondSpec() {
        return mstMoldProcCondSpec;
    }

    /**
     * @param mstMoldProcCondSpec the mstMoldProcCondSpec to set
     */
    public void setMstMoldProcCondSpec(List<MstMoldProcCondSpec> mstMoldProcCondSpec) {
        this.mstMoldProcCondSpec = mstMoldProcCondSpec;
    }

    /**
     * @return the mstMoldProcCond
     */
    public List<MstMoldProcCond> getMstMoldProcCond() {
        return mstMoldProcCond;
    }

    /**
     * @param mstMoldProcCond the mstMoldProcCond to set
     */
    public void setMstMoldProcCond(List<MstMoldProcCond> mstMoldProcCond) {
        this.mstMoldProcCond = mstMoldProcCond;
    }

    /**
     * @return the tblMoldLocationHistorys
     */
    public List<TblMoldLocationHistorys> getTblMoldLocationHistorys() {
        return tblMoldLocationHistorys;
    }

    /**
     * @param tblMoldLocationHistorys the tblMoldLocationHistorys to set
     */
    public void setTblMoldLocationHistorys(List<TblMoldLocationHistorys> tblMoldLocationHistorys) {
        this.tblMoldLocationHistorys = tblMoldLocationHistorys;
    }

    /**
     * @return the tblMoldLocationHistory
     */
    public List<TblMoldLocationHistory> getTblMoldLocationHistory() {
        return tblMoldLocationHistory;
    }

    /**
     * @param tblMoldLocationHistory the tblMoldLocationHistory to set
     */
    public void setTblMoldLocationHistory(List<TblMoldLocationHistory> tblMoldLocationHistory) {
        this.tblMoldLocationHistory = tblMoldLocationHistory;
    }

    /**
     * @return the mstMoldSpecHistorys
     */
    public List<MstMoldSpecHistorys> getMstMoldSpecHistorys() {
        return mstMoldSpecHistorys;
    }

    /**
     * @param mstMoldSpecHistorys the mstMoldSpecHistorys to set
     */
    public void setMstMoldSpecHistorys(List<MstMoldSpecHistorys> mstMoldSpecHistorys) {
        this.mstMoldSpecHistorys = mstMoldSpecHistorys;
    }

    /**
     * @return the mstMoldProcConds
     */
    public List<MstMoldProcConds> getMstMoldProcConds() {
        return mstMoldProcConds;
    }

    /**
     * @param mstMoldProcConds the mstMoldProcConds to set
     */
    public void setMstMoldProcConds(List<MstMoldProcConds> mstMoldProcConds) {
        this.mstMoldProcConds = mstMoldProcConds;
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
     * @return the companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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
     * @return the moldUuid
     */
    public String getMoldUuid() {
        return moldUuid;
    }

    /**
     * @param moldUuid the moldUuid to set
     */
    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public String getImgFilePath01() {
        return imgFilePath01;
    }

    public String getImgFilePath02() {
        return imgFilePath02;
    }

    public String getImgFilePath03() {
        return imgFilePath03;
    }

    public String getImgFilePath04() {
        return imgFilePath04;
    }

    public String getImgFilePath05() {
        return imgFilePath05;
    }

    public String getImgFilePath06() {
        return imgFilePath06;
    }

    public String getImgFilePath07() {
        return imgFilePath07;
    }

    public String getImgFilePath08() {
        return imgFilePath08;
    }

    public String getImgFilePath09() {
        return imgFilePath09;
    }

    public String getImgFilePath10() {
        return imgFilePath10;
    }

    public void setImgFilePath01(String imgFilePath01) {
        this.imgFilePath01 = imgFilePath01;
    }

    public void setImgFilePath02(String imgFilePath02) {
        this.imgFilePath02 = imgFilePath02;
    }

    public void setImgFilePath03(String imgFilePath03) {
        this.imgFilePath03 = imgFilePath03;
    }

    public void setImgFilePath04(String imgFilePath04) {
        this.imgFilePath04 = imgFilePath04;
    }

    public void setImgFilePath05(String imgFilePath05) {
        this.imgFilePath05 = imgFilePath05;
    }

    public void setImgFilePath06(String imgFilePath06) {
        this.imgFilePath06 = imgFilePath06;
    }

    public void setImgFilePath07(String imgFilePath07) {
        this.imgFilePath07 = imgFilePath07;
    }

    public void setImgFilePath08(String imgFilePath08) {
        this.imgFilePath08 = imgFilePath08;
    }

    public void setImgFilePath09(String imgFilePath09) {
        this.imgFilePath09 = imgFilePath09;
    }

    public void setImgFilePath10(String imgFilePath10) {
        this.imgFilePath10 = imgFilePath10;
    }

    public MstMoldProcCondSpecPK getMstMoldProcCondSpecPK() {
        return mstMoldProcCondSpecPK;
    }

    public List<MstMoldProcCondSpecs> getMstMoldProcCondSpecVos() {
        return mstMoldProcCondSpecVos;
    }

    public void setMstMoldProcCondSpecPK(MstMoldProcCondSpecPK mstMoldProcCondSpecPK) {
        this.mstMoldProcCondSpecPK = mstMoldProcCondSpecPK;
    }

    public void setMstMoldProcCondSpecVos(List<MstMoldProcCondSpecs> mstMoldProcCondSpecVos) {
        this.mstMoldProcCondSpecVos = mstMoldProcCondSpecVos;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the MstMoldComponentRelationVo
     */
    public List<MstMoldComponentRelationVo> getMstMoldComponentRelationVo() {
        return MstMoldComponentRelationVo;
    }

    /**
     * @param MstMoldComponentRelationVo the MstMoldComponentRelationVo to set
     */
    public void setMstMoldComponentRelationVo(List<MstMoldComponentRelationVo> MstMoldComponentRelationVo) {
        this.MstMoldComponentRelationVo = MstMoldComponentRelationVo;
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

    /**
     * @return the externalFlg
     */
    public int getExternalFlg() {
        return externalFlg;
    }

    /**
     * @param externalFlg the externalFlg to set
     */
    public void setExternalFlg(int externalFlg) {
        this.externalFlg = externalFlg;
    }

    /**
     * @return the mainteStatusText
     */
    public String getMainteStatusText() {
        return mainteStatusText;
    }

    /**
     * @param mainteStatusText the mainteStatusText to set
     */
    public void setMainteStatusText(String mainteStatusText) {
        this.mainteStatusText = mainteStatusText;
    }

    public String getCreatedDateStr() {
        return createdDateStr;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreatedDateStr(String createdDateStr) {
        this.createdDateStr = createdDateStr;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
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

    public void setInspectedDateStr(String inspectedDateStr) {
        this.inspectedDateStr = inspectedDateStr;
    }

    public void setInstalledDateStr(String installedDateStr) {
        this.installedDateStr = installedDateStr;
    }

    public void setStatusChangedDateStr(String statusChangedDateStr) {
        this.statusChangedDateStr = statusChangedDateStr;
    }

    public String getLatestInventoryId() {
        return latestInventoryId;
    }

    public void setLatestInventoryId(String latestInventoryId) {
        this.latestInventoryId = latestInventoryId;
    }

    public String getMoldCreatedDateStr() {
        return moldCreatedDateStr;
    }

    public void setMoldCreatedDateStr(String moldCreatedDateStr) {
        this.moldCreatedDateStr = moldCreatedDateStr;
    }

    public String getTotalShotCount() {
        return totalShotCount;
    }

    public void setTotalShotCount(String totalShotCount) {
        this.totalShotCount = totalShotCount;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    
    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }
        
    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }
    public List<TblMoldInventorys> getTblMoldInventorys() {
        return tblMoldInventorys;
    }

    public void setTblMoldInventorys(List<TblMoldInventorys> tblMoldInventorys) {
        this.tblMoldInventorys = tblMoldInventorys;
    }

    public List<TblIssueVo> getTblIssueVos() {
        return tblIssueVos;
    }

    public void setTblIssueVos(List<TblIssueVo> tblIssueVos) {
        this.tblIssueVos = tblIssueVos;
    }

    public List<TblMoldMaintenanceRemodelingVo> getTblMoldMaintenanceRemodelingVos() {
        return tblMoldMaintenanceRemodelingVos;
    }

    public void setTblMoldMaintenanceRemodelingVos(List<TblMoldMaintenanceRemodelingVo> tblMoldMaintenanceRemodelingVos) {
        this.tblMoldMaintenanceRemodelingVos = tblMoldMaintenanceRemodelingVos;
    }

    public List<MstMoldPartRelDetail> getMstMoldPartRels() {
        return mstMoldPartRels;
    }

    public void setMstMoldPartRels(List<MstMoldPartRelDetail> mstMoldPartRels) {
        this.mstMoldPartRels = mstMoldPartRels;
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
     * @return the reportFilePathName01
     */
    public String getReportFilePathName01() {
        return reportFilePathName01;
    }

    /**
     * @param reportFilePathName01 the reportFilePathName01 to set
     */
    public void setReportFilePathName01(String reportFilePathName01) {
        this.reportFilePathName01 = reportFilePathName01;
    }

    /**
     * @return the reportFilePathName02
     */
    public String getReportFilePathName02() {
        return reportFilePathName02;
    }

    /**
     * @param reportFilePathName02 the reportFilePathName02 to set
     */
    public void setReportFilePathName02(String reportFilePathName02) {
        this.reportFilePathName02 = reportFilePathName02;
    }

    /**
     * @return the reportFilePathName03
     */
    public String getReportFilePathName03() {
        return reportFilePathName03;
    }

    /**
     * @param reportFilePathName03 the reportFilePathName03 to set
     */
    public void setReportFilePathName03(String reportFilePathName03) {
        this.reportFilePathName03 = reportFilePathName03;
    }

    /**
     * @return the reportFilePathName04
     */
    public String getReportFilePathName04() {
        return reportFilePathName04;
    }

    /**
     * @param reportFilePathName04 the reportFilePathName04 to set
     */
    public void setReportFilePathName04(String reportFilePathName04) {
        this.reportFilePathName04 = reportFilePathName04;
    }

    /**
     * @return the reportFilePathName05
     */
    public String getReportFilePathName05() {
        return reportFilePathName05;
    }

    /**
     * @param reportFilePathName05 the reportFilePathName05 to set
     */
    public void setReportFilePathName05(String reportFilePathName05) {
        this.reportFilePathName05 = reportFilePathName05;
    }

    /**
     * @return the reportFilePathName06
     */
    public String getReportFilePathName06() {
        return reportFilePathName06;
    }

    /**
     * @param reportFilePathName06 the reportFilePathName06 to set
     */
    public void setReportFilePathName06(String reportFilePathName06) {
        this.reportFilePathName06 = reportFilePathName06;
    }

    /**
     * @return the reportFilePathName07
     */
    public String getReportFilePathName07() {
        return reportFilePathName07;
    }

    /**
     * @param reportFilePathName07 the reportFilePathName07 to set
     */
    public void setReportFilePathName07(String reportFilePathName07) {
        this.reportFilePathName07 = reportFilePathName07;
    }

    /**
     * @return the reportFilePathName08
     */
    public String getReportFilePathName08() {
        return reportFilePathName08;
    }

    /**
     * @param reportFilePathName08 the reportFilePathName08 to set
     */
    public void setReportFilePathName08(String reportFilePathName08) {
        this.reportFilePathName08 = reportFilePathName08;
    }

    /**
     * @return the reportFilePathName09
     */
    public String getReportFilePathName09() {
        return reportFilePathName09;
    }

    /**
     * @param reportFilePathName09 the reportFilePathName09 to set
     */
    public void setReportFilePathName09(String reportFilePathName09) {
        this.reportFilePathName09 = reportFilePathName09;
    }

    /**
     * @return the reportFilePathName10
     */
    public String getReportFilePathName10() {
        return reportFilePathName10;
    }

    /**
     * @param reportFilePathName10 the reportFilePathName10 to set
     */
    public void setReportFilePathName10(String reportFilePathName10) {
        this.reportFilePathName10 = reportFilePathName10;
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

    /**
     * @return the departmentName
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @param departmentName the departmentName to set
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }


    public String getUseYearCount() {
        return useYearCount;
    }

    public Date getLastProductionDate() {
        return lastProductionDate;
    }

    public String getLastProductionDateStr() {
        return lastProductionDateStr;
    }

    public BigDecimal getTotalProducingTimeHour() {
        return totalProducingTimeHour;
    }

    public Date getLastMainteDate() {
        return lastMainteDate;
    }

    public String getLastMainteDateStr() {
        return lastMainteDateStr;
    }

    public BigDecimal getAfterMainteTotalProducingTimeHour() {
        return afterMainteTotalProducingTimeHour;
    }

    public Integer getAfterMainteTotalShotCount() {
        return afterMainteTotalShotCount;
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

    public void setUseYearCount(String useYearCount) {
        this.useYearCount = useYearCount;
    }

    public void setLastProductionDate(Date lastProductionDate) {
        this.lastProductionDate = lastProductionDate;
    }

    public void setLastProductionDateStr(String lastProductionDateStr) {
        this.lastProductionDateStr = lastProductionDateStr;
    }

    public void setTotalProducingTimeHour(BigDecimal totalProducingTimeHour) {
        this.totalProducingTimeHour = totalProducingTimeHour;
    }

    public void setLastMainteDate(Date lastMainteDate) {
        this.lastMainteDate = lastMainteDate;
    }

    public void setLastMainteDateStr(String lastMainteDateStr) {
        this.lastMainteDateStr = lastMainteDateStr;
    }

    public void setAfterMainteTotalProducingTimeHour(BigDecimal afterMainteTotalProducingTimeHour) {
        this.afterMainteTotalProducingTimeHour = afterMainteTotalProducingTimeHour;
    }

    public void setAfterMainteTotalShotCount(Integer afterMainteTotalShotCount) {
        this.afterMainteTotalShotCount = afterMainteTotalShotCount;
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

    public int getSendFlg() {
        return sendFlg;
    }

    public void setSendFlg(int sendFlg) {
        this.sendFlg = sendFlg;
    }

    public String getActualMainteCycleId() {
        return actualMainteCycleId;
    }

    public void setActualMainteCycleId(String actualMainteCycleId) {
        this.actualMainteCycleId = actualMainteCycleId;
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
