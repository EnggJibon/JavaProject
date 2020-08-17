/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.location.history;

import com.kmcj.karte.BasicResponse;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class TblMoldLocationHistorys extends BasicResponse {

    private String id;
    private String startDate;
    private String endDate;
    private Integer changeReason;
    private String changeReasonText;
    private String companyId;
    private String companyCode;
    private String companyName;
    private String locationId;
    private String locationCode;
    private String locationName;
    private String instllationSiteId;
    private String instllationSiteCode;
    private String instllationSiteName;
    private String moldId;
    private String moldName;
    private String deleteFlag;
    private Date createDate;
    private String createUserUuid;
    private String updateUserUuid;
    private String moldUuid;
    private Date installedDate;

    private List<TblMoldLocationHistorys> TblMoldLocationHistoryVos;
    private TblMoldLocationHistory tblMoldLocationHistory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(Integer changeReason) {
        this.changeReason = changeReason;
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
     * @return the deleteFlag
     */
    public String getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * @param deleteFlag the deleteFlag to set
     */
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getInstllationSiteId() {
        return instllationSiteId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public void setInstllationSiteId(String instllationSiteId) {
        this.instllationSiteId = instllationSiteId;
    }

    public List<TblMoldLocationHistorys> getTblMoldLocationHistoryVos() {
        return TblMoldLocationHistoryVos;
    }

    public void setTblMoldLocationHistoryVos(List<TblMoldLocationHistorys> TblMoldLocationHistoryVos) {
        this.TblMoldLocationHistoryVos = TblMoldLocationHistoryVos;
    }

    public String getChangeReasonText() {
        return changeReasonText;
    }

    public void setChangeReasonText(String changeReasonText) {
        this.changeReasonText = changeReasonText;
    }

    public String getMoldId() {
        return moldId;
    }

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

    public TblMoldLocationHistory getTblMoldLocationHistory() {
        return tblMoldLocationHistory;
    }

    public void setTblMoldLocationHistory(TblMoldLocationHistory tblMoldLocationHistory) {
        this.tblMoldLocationHistory = tblMoldLocationHistory;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public String getInstllationSiteCode() {
        return instllationSiteCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public void setInstllationSiteCode(String instllationSiteCode) {
        this.instllationSiteCode = instllationSiteCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public Date getInstalledDate() {
        return installedDate;
    }

    public void setInstalledDate(Date installedDate) {
        this.installedDate = installedDate;
    }
    
}
