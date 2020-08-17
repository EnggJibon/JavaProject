/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.company;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.location.MstLocation;
import java.util.List;

/**
 * 所在３点セット制御
 *
 * @author admin
 */
public class MstCLIAutoComplete extends BasicResponse{

    private String companyId;
    private String companyCode;
    private String companyName;

    private String locationId;
    private String locationCode;
    private String locationName;

    private String installationSiteId;
    private String installationSiteCode;
    private String installationSiteName;
    
    private List<MstCompany> mstCLIAutoCompleteCompany;
    private List<MstLocation> mstCLIAutoCompleteLocation;
    private List<MstInstallationSite> mstCLIAutoCompleteInstallationSite;

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
     * @return the companyCode
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * @param companyCode the companyCode to set
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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
     * @return the locationCode
     */
    public String getLocationCode() {
        return locationCode;
    }

    /**
     * @param locationCode the locationCode to set
     */
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
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
     * @return the installationSiteCode
     */
    public String getInstallationSiteCode() {
        return installationSiteCode;
    }

    /**
     * @param installationSiteCode the installationSiteCode to set
     */
    public void setInstallationSiteCode(String installationSiteCode) {
        this.installationSiteCode = installationSiteCode;
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
     * @return the mstCLIAutoCompleteCompany
     */
    public List<MstCompany> getMstCLIAutoCompleteCompany() {
        return mstCLIAutoCompleteCompany;
    }

    /**
     * @param mstCLIAutoCompleteCompany the mstCLIAutoCompleteCompany to set
     */
    public void setMstCLIAutoCompleteCompany(List<MstCompany> mstCLIAutoCompleteCompany) {
        this.mstCLIAutoCompleteCompany = mstCLIAutoCompleteCompany;
    }

    /**
     * @return the mstCLIAutoCompleteLocation
     */
    public List<MstLocation> getMstCLIAutoCompleteLocation() {
        return mstCLIAutoCompleteLocation;
    }

    /**
     * @param mstCLIAutoCompleteLocation the mstCLIAutoCompleteLocation to set
     */
    public void setMstCLIAutoCompleteLocation(List<MstLocation> mstCLIAutoCompleteLocation) {
        this.mstCLIAutoCompleteLocation = mstCLIAutoCompleteLocation;
    }

    /**
     * @return the mstCLIAutoCompleteInstallationSite
     */
    public List<MstInstallationSite> getMstCLIAutoCompleteInstallationSite() {
        return mstCLIAutoCompleteInstallationSite;
    }

    /**
     * @param mstCLIAutoCompleteInstallationSite the mstCLIAutoCompleteInstallationSite to set
     */
    public void setMstCLIAutoCompleteInstallationSite(List<MstInstallationSite> mstCLIAutoCompleteInstallationSite) {
        this.mstCLIAutoCompleteInstallationSite = mstCLIAutoCompleteInstallationSite;
    }
}
