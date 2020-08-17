/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.location.history;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.machine.*;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineLocationHistoryVo extends BasicResponse {

    private String id;

    private Date startDate;
    private String startDateStr;

    private Date endDate;
    private String endDateStr;

    private Integer changeReason;

    private String changeReasonText;

    private String companyName;

    private String locationName;

    private String instllationSiteName;

    private Date createDate;
    private String createDateStr;

    private Date updateDate;
    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private MstMachine mstMachine;
    private String machineId;
    private String machineName;

    private String machineUuid;

    private MstCompany mstCompany;
    private String companyCode;

    private String companyId;

    private MstInstallationSite mstInstallationSite;

    private String instllationSiteId;

    private MstLocation mstLocation;

    private String locationId;

    private List<TblMachineLocationHistoryVo> tblMachineLocationHistoryVos;
    
    private String operationFlag;

    public TblMachineLocationHistoryVo() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setChangeReason(Integer changeReason) {
        this.changeReason = changeReason;
    }

    public void setChangeReasonText(String changeReasonText) {
        this.changeReasonText = changeReasonText;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setInstllationSiteName(String instllationSiteName) {
        this.instllationSiteName = instllationSiteName;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
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

    public String getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getChangeReason() {
        return changeReason;
    }

    public String getChangeReasonText() {
        return changeReasonText;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getInstllationSiteName() {
        return instllationSiteName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public MstMachine getMstMachine() {
        return mstMachine;
    }

    public String getMachineUuid() {
        return machineUuid;
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

    public String getStartDateStr() {
        return startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public List<TblMachineLocationHistoryVo> getTblMachineLocationHistoryVos() {
        return tblMachineLocationHistoryVos;
    }

    public void setTblMachineLocationHistoryVos(List<TblMachineLocationHistoryVo> tblMachineLocationHistoryVos) {
        this.tblMachineLocationHistoryVos = tblMachineLocationHistoryVos;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getOperationFlag() {
        return operationFlag;
    }

    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

}
