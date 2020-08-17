/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.inventory;

/**
 *
 * @author admin
 */
public class SearchCondition {

    private String companyId;//会社ID
    private String locationId;//所在地ID
    private String installationSiteId;//設置場所ID
    private String ownerCompanyId;//所有会社ID
    private int department;//所属Seq
    private String companyName;//会社名称
    private String locationName;//所在地名称
    private String installationSiteName;//設置場所名称
    private String departmentName;//所属名称
    private int inventoryNotDoneFlg;//棚卸未実施

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
     * @return the installationSiteId
     */
    public String getInstallationSiteId() {
        return installationSiteId;
    }

    /**
     * @param installationSiteId the installationSiteId to set
     */
    public void setInstallationSiteId(String installationSiteId) {
        this.installationSiteId = installationSiteId;
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
     * @return the department
     */
    public int getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(int department) {
        this.department = department;
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
     * @return the installationSiteName
     */
    public String getInstallationSiteName() {
        return installationSiteName;
    }

    /**
     * @param installationSiteName the installationSiteName to set
     */
    public void setInstallationSiteName(String installationSiteName) {
        this.installationSiteName = installationSiteName;
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
     * @return the inventoryNotDoneFlg
     */
    public int getInventoryNotDoneFlg() {
        return inventoryNotDoneFlg;
    }

    /**
     * @param inventoryNotDoneFlg the inventoryNotDoneFlg to set
     */
    public void setInventoryNotDoneFlg(int inventoryNotDoneFlg) {
        this.inventoryNotDoneFlg = inventoryNotDoneFlg;
    }

}
