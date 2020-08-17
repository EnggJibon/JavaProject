/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inventory;

import java.util.List;

/**
 *
 * @author zdsoft
 */
public class OutputErrorInfo {

    private String companyName;//会社名称
    private String locationName;//所在地名称
    private String installationSiteName;//設置場所名称
    private String departmentName;//所属名称
    private String moldId;//
    private String moldName;//
    private String qrPlateInfo;

    private int locationConfirm;//2:所在不明,1:所属不明,3:所在不明&所属不明

    private List<OutputErrorInfo> outputErrorInfos;

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
     * @return the outputErrorInfos
     */
    public List<OutputErrorInfo> getOutputErrorInfos() {
        return outputErrorInfos;
    }

    /**
     * @param outputErrorInfos the outputErrorInfos to set
     */
    public void setOutputErrorInfos(List<OutputErrorInfo> outputErrorInfos) {
        this.outputErrorInfos = outputErrorInfos;
    }

    /**
     * @return the locationConfirm
     */
    public int getLocationConfirm() {
        return locationConfirm;
    }

    /**
     * @param locationConfirm the locationConfirm to set
     */
    public void setLocationConfirm(int locationConfirm) {
        this.locationConfirm = locationConfirm;
    }

    /**
     * @return the qrPlateInfo
     */
    public String getQrPlateInfo() {
        return qrPlateInfo;
    }

    /**
     * @param qrPlateInfo the qrPlateInfo to set
     */
    public void setQrPlateInfo(String qrPlateInfo) {
        this.qrPlateInfo = qrPlateInfo;
    }

}
