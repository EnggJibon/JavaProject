/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author admin
 */
public class MstMoldAutoComplete extends BasicResponse {

    private String uuid;
    private String moldId;
    private String moldName;
    
    private String componentId;
    private String componentCode;
    private String componentName;
    
    private String machineUuid;
    private String machineId;
    private String machineName;
    
    private Date installedDate;

    private String companyId;
    private String companyName;

    private String locationId;
    private String locationName;

    private String instllationSiteId;
    private String instllationSiteName;
    
    private String moldType;
    private String machineType;
    private String externalFlag;
    
    private int afterMainteTotalShotCount;

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
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
     * @return the moldType
     */
    public String getMoldType() {
        return moldType;
    }

    /**
     * @param moldType the moldType to set
     */
    public void setMoldType(String moldType) {
        this.moldType = moldType;
    }

    /**
     * @return the externalFlag
     */
    public String getExternalFlag() {
        return externalFlag;
    }

    /**
     * @param externalFlag the externalFlag to set
     */
    public void setExternalFlag(String externalFlag) {
        this.externalFlag = externalFlag;
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
     * @return the machineType
     */
    public String getMachineType() {
        return machineType;
    }

    /**
     * @param machineType the machineType to set
     */
    public void setMachineType(String machineType) {
        this.machineType = machineType;
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
}
