/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.issue;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class TblIssueVo extends BasicResponse {

    private String id;
    private String reportDate;
    private String happenedAt;
    private String measureDueDate;
    private int measureStatus;
    private String measureStatusText;
    private String moldUuid;
    private String moldId;
    private String moldName;
    private String moldInstallationSiteName;
    private int quantity;
    private int shotCountAtIssue;
    private int mainteType;
    private String componentId;
    private String componentCode;
    private String componentName;
    private String machineUuid;
    private String machineId;
    private String machineName;
    private String reportDepartment;
    private String reportDepartmentName;
    private String reportPhase;
    private String reportPhaseText;
    private String reportCategory1;
    private String reportCategory1Text;
    private String reportCategory2;
    private String reportCategory2Text;
    private String reportCategory3;
    private String reportCategory3Text;
    private String memo01;
    private String memo02;
    private String memo03;
    private String issue;
    private String measureSummary;
    private String measuerCompletedDate;
    private String mainteDate;
    private String mainTenanceId;
    private String reportPersonUuid;
    private String reportPersonName;

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

    private String reportFilePath01Name;
    private String reportFilePath02Name;
    private String reportFilePath03Name;
    private String reportFilePath04Name;
    private String reportFilePath05Name;
    private String reportFilePath06Name;
    private String reportFilePath07Name;
    private String reportFilePath08Name;
    private String reportFilePath09Name;
    private String reportFilePath10Name;

    private String machineMainteDate;
    private String machineMainTenanceId;

    private int mainteOrRemodel;
    private int externalFlg;
    private List<TblIssueImageFileVo> tblIssueImageFileVoList;

    private TblIssue tblIssue;

    private String machineMaintenancePersonName;
    private String moldMaintenancePersonName;
    private String issueReportPersonName;
    private int afterMainteTotalShotCount;
    
    private String lotNumber;
    private String procedureId;
    private String procedureCode;
    private String procedureName;

    /**
     * @return the measureStatus
     */
    public int getMeasureStatus() {
        return measureStatus;
    }

    /**
     * @param measureStatus the measureStatus to set
     */
    public void setMeasureStatus(int measureStatus) {
        this.measureStatus = measureStatus;
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
     * @return the componentCode
     */
    public String getComponentCode() {
        return componentCode;
    }

    /**
     * @param componentCode the componentCode to set
     */
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    /**
     * @return the componentName
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param componentName the componentName to set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * @return the reportDepartmentName
     */
    public String getReportDepartmentName() {
        return reportDepartmentName;
    }

    /**
     * @param reportDepartmentName the reportDepartmentName to set
     */
    public void setReportDepartmentName(String reportDepartmentName) {
        this.reportDepartmentName = reportDepartmentName;
    }

    /**
     * @return the reportPhaseText
     */
    public String getReportPhaseText() {
        return reportPhaseText;
    }

    /**
     * @param reportPhaseText the reportPhaseText to set
     */
    public void setReportPhaseText(String reportPhaseText) {
        this.reportPhaseText = reportPhaseText;
    }

    /**
     * @return the reportCategory1Text
     */
    public String getReportCategory1Text() {
        return reportCategory1Text;
    }

    /**
     * @param reportCategory1Text the reportCategory1Text to set
     */
    public void setReportCategory1Text(String reportCategory1Text) {
        this.reportCategory1Text = reportCategory1Text;
    }

    /**
     * @return the reportCategory2Text
     */
    public String getReportCategory2Text() {
        return reportCategory2Text;
    }

    /**
     * @param reportCategory2Text the reportCategory2Text to set
     */
    public void setReportCategory2Text(String reportCategory2Text) {
        this.reportCategory2Text = reportCategory2Text;
    }

    /**
     * @return the reportCategory3Text
     */
    public String getReportCategory3Text() {
        return reportCategory3Text;
    }

    /**
     * @param reportCategory3Text the reportCategory3Text to set
     */
    public void setReportCategory3Text(String reportCategory3Text) {
        this.reportCategory3Text = reportCategory3Text;
    }
    
    /**
     * @return the memo01
     */
    public String getMemo01() {
        return memo01;
    }

    /**
     * @param memo01 the memo01 to set
     */
    public void setMemo01(String memo01) {
        this.memo01 = memo01;
    }
    
    /**
     * @return the memo02
     */
    public String getMemo02() {
        return memo02;
    }

    /**
     * @param memo02 the memo02 to set
     */
    public void setMemo02(String memo02) {
        this.memo02 = memo02;
    }
    
    /**
     * @return the memo03
     */
    public String getMemo03() {
        return memo03;
    }

    /**
     * @param memo03 the memo02 to set
     */
    public void setMemo03(String memo03) {
        this.memo03 = memo03;
    }

    /**
     * @return the issue
     */
    public String getIssue() {
        return issue;
    }

    /**
     * @param issue the issue to set
     */
    public void setIssue(String issue) {
        this.issue = issue;
    }

    /**
     * @return the measureSummary
     */
    public String getMeasureSummary() {
        return measureSummary;
    }

    /**
     * @param measureSummary the measureSummary to set
     */
    public void setMeasureSummary(String measureSummary) {
        this.measureSummary = measureSummary;
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
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the TblIssueImageFileVoList
     */
    public List<TblIssueImageFileVo> getTblIssueImageFileVoList() {
        return tblIssueImageFileVoList;
    }

    /**
     * @param TblIssueImageFileVoList the TblIssueImageFileVoList to set
     */
    public void setTblIssueImageFileVoList(List<TblIssueImageFileVo> tblIssueImageFileVoList) {
        this.tblIssueImageFileVoList = tblIssueImageFileVoList;
    }

    /**
     * @return the reportPersonUuid
     */
    public String getReportPersonUuid() {
        return reportPersonUuid;
    }

    /**
     * @param reportPersonUuid the reportPersonUuid to set
     */
    public void setReportPersonUuid(String reportPersonUuid) {
        this.reportPersonUuid = reportPersonUuid;
    }

    /**
     * @return the reportPersonName
     */
    public String getReportPersonName() {
        return reportPersonName;
    }

    /**
     * @param reportPersonName the reportPersonName to set
     */
    public void setReportPersonName(String reportPersonName) {
        this.reportPersonName = reportPersonName;
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
     * @return the mainteOrRemodel
     */
    public int getMainteOrRemodel() {
        return mainteOrRemodel;
    }

    /**
     * @param mainteOrRemodel the mainteOrRemodel to set
     */
    public void setMainteOrRemodel(int mainteOrRemodel) {
        this.mainteOrRemodel = mainteOrRemodel;
    }

    /**
     * @return the reportDepartment
     */
    public String getReportDepartment() {
        return reportDepartment;
    }

    /**
     * @param reportDepartment the reportDepartment to set
     */
    public void setReportDepartment(String reportDepartment) {
        this.reportDepartment = reportDepartment;
    }

    /**
     * @return the reportPhase
     */
    public String getReportPhase() {
        return reportPhase;
    }

    /**
     * @param reportPhase the reportPhase to set
     */
    public void setReportPhase(String reportPhase) {
        this.reportPhase = reportPhase;
    }

    /**
     * @return the reportCategory1
     */
    public String getReportCategory1() {
        return reportCategory1;
    }

    /**
     * @param reportCategory1 the reportCategory1 to set
     */
    public void setReportCategory1(String reportCategory1) {
        this.reportCategory1 = reportCategory1;
    }

    /**
     * @return the reportCategory2
     */
    public String getReportCategory2() {
        return reportCategory2;
    }

    /**
     * @param reportCategory2 the reportCategory2 to set
     */
    public void setReportCategory2(String reportCategory2) {
        this.reportCategory2 = reportCategory2;
    }

    /**
     * @return the reportCategory3
     */
    public String getReportCategory3() {
        return reportCategory3;
    }

    /**
     * @param reportCategory3 the reportCategory3 to set
     */
    public void setReportCategory3(String reportCategory3) {
        this.reportCategory3 = reportCategory3;
    }

    /**
     * @return the reportFilePath01Name
     */
    public String getReportFilePath01Name() {
        return reportFilePath01Name;
    }

    /**
     * @param reportFilePath01Name the reportFilePath01Name to set
     */
    public void setReportFilePath01Name(String reportFilePath01Name) {
        this.reportFilePath01Name = reportFilePath01Name;
    }

    /**
     * @return the reportFilePath02Name
     */
    public String getReportFilePath02Name() {
        return reportFilePath02Name;
    }

    /**
     * @param reportFilePath02Name the reportFilePath02Name to set
     */
    public void setReportFilePath02Name(String reportFilePath02Name) {
        this.reportFilePath02Name = reportFilePath02Name;
    }

    /**
     * @return the reportFilePath03Name
     */
    public String getReportFilePath03Name() {
        return reportFilePath03Name;
    }

    /**
     * @param reportFilePath03Name the reportFilePath03Name to set
     */
    public void setReportFilePath03Name(String reportFilePath03Name) {
        this.reportFilePath03Name = reportFilePath03Name;
    }

    /**
     * @return the reportFilePath04Name
     */
    public String getReportFilePath04Name() {
        return reportFilePath04Name;
    }

    /**
     * @param reportFilePath04Name the reportFilePath04Name to set
     */
    public void setReportFilePath04Name(String reportFilePath04Name) {
        this.reportFilePath04Name = reportFilePath04Name;
    }

    /**
     * @return the reportFilePath05Name
     */
    public String getReportFilePath05Name() {
        return reportFilePath05Name;
    }

    /**
     * @param reportFilePath05Name the reportFilePath05Name to set
     */
    public void setReportFilePath05Name(String reportFilePath05Name) {
        this.reportFilePath05Name = reportFilePath05Name;
    }

    /**
     * @return the reportFilePath06Name
     */
    public String getReportFilePath06Name() {
        return reportFilePath06Name;
    }

    /**
     * @param reportFilePath06Name the reportFilePath06Name to set
     */
    public void setReportFilePath06Name(String reportFilePath06Name) {
        this.reportFilePath06Name = reportFilePath06Name;
    }

    /**
     * @return the reportFilePath07Name
     */
    public String getReportFilePath07Name() {
        return reportFilePath07Name;
    }

    /**
     * @param reportFilePath07Name the reportFilePath07Name to set
     */
    public void setReportFilePath07Name(String reportFilePath07Name) {
        this.reportFilePath07Name = reportFilePath07Name;
    }

    /**
     * @return the reportFilePath08Name
     */
    public String getReportFilePath08Name() {
        return reportFilePath08Name;
    }

    /**
     * @param reportFilePath08Name the reportFilePath08Name to set
     */
    public void setReportFilePath08Name(String reportFilePath08Name) {
        this.reportFilePath08Name = reportFilePath08Name;
    }

    /**
     * @return the reportFilePath09Name
     */
    public String getReportFilePath09Name() {
        return reportFilePath09Name;
    }

    /**
     * @param reportFilePath09Name the reportFilePath09Name to set
     */
    public void setReportFilePath09Name(String reportFilePath09Name) {
        this.reportFilePath09Name = reportFilePath09Name;
    }

    /**
     * @return the reportFilePath10Name
     */
    public String getReportFilePath10Name() {
        return reportFilePath10Name;
    }

    /**
     * @param reportFilePath10Name the reportFilePath10Name to set
     */
    public void setReportFilePath10Name(String reportFilePath10Name) {
        this.reportFilePath10Name = reportFilePath10Name;
    }

    /**
     * @return the mainTenanceId
     */
    public String getMainTenanceId() {
        return mainTenanceId;
    }

    /**
     * @param mainTenanceId the mainTenanceId to set
     */
    public void setMainTenanceId(String mainTenanceId) {
        this.mainTenanceId = mainTenanceId;
    }

    /**
     * @return the reportDate
     */
    public String getReportDate() {
        return reportDate;
    }

    /**
     * @param reportDate the reportDate to set
     */
    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    /**
     * @return the happenedAt
     */
    public String getHappenedAt() {
        return happenedAt;
    }

    /**
     * @param happenedAt the happenedAt to set
     */
    public void setHappenedAt(String happenedAt) {
        this.happenedAt = happenedAt;
    }
    
    /**
     * @return the measureDueDate
     */
    public String getMeasureDueDate() {
        return measureDueDate;
    }

    /**
     * @param measureDueDate the measureDueDate to set
     */
    public void setMeasureDueDate(String measureDueDate) {
        this.measureDueDate = measureDueDate;
    }

    /**
     * @return the measuerCompletedDate
     */
    public String getMeasuerCompletedDate() {
        return measuerCompletedDate;
    }

    /**
     * @param measuerCompletedDate the measuerCompletedDate to set
     */
    public void setMeasuerCompletedDate(String measuerCompletedDate) {
        this.measuerCompletedDate = measuerCompletedDate;
    }

    /**
     * @return the mainteDate
     */
    public String getMainteDate() {
        return mainteDate;
    }

    /**
     * @param mainteDate the mainteDate to set
     */
    public void setMainteDate(String mainteDate) {
        this.mainteDate = mainteDate;
    }

    public TblIssue getTblIssue() {
        return tblIssue;
    }

    public void setTblIssue(TblIssue tblIssue) {
        this.tblIssue = tblIssue;
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
     * @return the machineUuid
     */
    public String getMachineUuid() {
        return machineUuid;
    }

    /**
     * @param machineUuid the machineUuid to set
     */
    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    /**
     * @return the machineId
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machineId to set
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * @return the machineName
     */
    public String getMachineName() {
        return machineName;
    }

    /**
     * @param machineName the machineName to set
     */
    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    /**
     * @return the machineMainteDate
     */
    public String getMachineMainteDate() {
        return machineMainteDate;
    }

    /**
     * @param machineMainteDate the machineMainteDate to set
     */
    public void setMachineMainteDate(String machineMainteDate) {
        this.machineMainteDate = machineMainteDate;
    }

    /**
     * @return the machineMainTenanceId
     */
    public String getMachineMainTenanceId() {
        return machineMainTenanceId;
    }

    /**
     * @param machineMainTenanceId the machineMainTenanceId to set
     */
    public void setMachineMainTenanceId(String machineMainTenanceId) {
        this.machineMainTenanceId = machineMainTenanceId;
    }

    /**
     * @return the machineMaintenancePersonName
     */
    public String getMachineMaintenancePersonName() {
        return machineMaintenancePersonName;
    }

    /**
     * @param machineMaintenancePersonName the machineMaintenancePersonName to
     * set
     */
    public void setMachineMaintenancePersonName(String machineMaintenancePersonName) {
        this.machineMaintenancePersonName = machineMaintenancePersonName;
    }

    /**
     * @return the moldMaintenancePersonName
     */
    public String getMoldMaintenancePersonName() {
        return moldMaintenancePersonName;
    }

    /**
     * @param moldMaintenancePersonName the moldMaintenancePersonName to set
     */
    public void setMoldMaintenancePersonName(String moldMaintenancePersonName) {
        this.moldMaintenancePersonName = moldMaintenancePersonName;
    }

    /**
     * @return the issueReportPersonName
     */
    public String getIssueReportPersonName() {
        return issueReportPersonName;
    }

    /**
     * @param issueReportPersonName the issueReportPersonName to set
     */
    public void setIssueReportPersonName(String issueReportPersonName) {
        this.issueReportPersonName = issueReportPersonName;
    }

    /**
     * @return the moldInstallationSiteName
     */
    public String getMoldInstallationSiteName() {
        return moldInstallationSiteName;
    }

    /**
     * @param moldInstallationSiteName the moldInstallationSiteName to set
     */
    public void setMoldInstallationSiteName(String moldInstallationSiteName) {
        this.moldInstallationSiteName = moldInstallationSiteName;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the shotCountAtIssue
     */
    public int getShotCountAtIssue() {
        return shotCountAtIssue;
    }

    /**
     * @param shotCountAtIssue the shotCountAtIssue to set
     */
    public void setShotCountAtIssue(int shotCountAtIssue) {
        this.shotCountAtIssue = shotCountAtIssue;
    }

    /**
     * @return the mainteType
     */
    public int getMainteType() {
        return mainteType;
    }

    /**
     * @param mainteType the mainteType to set
     */
    public void setMainteType(int mainteType) {
        this.mainteType = mainteType;
    }

    /**
     * @return the afterMainteTotalShotCount
     */
    public int getAfterMainteTotalShotCount() {
        return afterMainteTotalShotCount;
    }

    /**
     * @param afterMainteTotalShotCount the afterMainteTotalShotCount to set
     */
    public void setAfterMainteTotalShotCount(int afterMainteTotalShotCount) {
        this.afterMainteTotalShotCount = afterMainteTotalShotCount;
    }

    /**
     * @return the measureStatusText
     */
    public String getMeasureStatusText() {
        return measureStatusText;
    }

    /**
     * @param measureStatusText the measureStatusText to set
     */
    public void setMeasureStatusText(String measureStatusText) {
        this.measureStatusText = measureStatusText;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

}
