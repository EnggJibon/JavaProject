/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.external;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author jiangxs
 */
public class MstExternalDataGetSettingVo extends BasicResponse {
    
    private String id;
    private String userId;
    private String encryptedPassword;
    private String apiBaseUrl;
    private Integer validFlg;
    private Date latestExecutedDate;
    private String companyId;
    private String companyCode;
    private String companyName;

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
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the encryptedPassword
     */
    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    /**
     * @param encryptedPassword the encryptedPassword to set
     */
    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    /**
     * @return the apiBaseUrl
     */
    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    /**
     * @param apiBaseUrl the apiBaseUrl to set
     */
    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    /**
     * @return the validFlg
     */
    public Integer getValidFlg() {
        return validFlg;
    }

    /**
     * @param validFlg the validFlg to set
     */
    public void setValidFlg(Integer validFlg) {
        this.validFlg = validFlg;
    }

    /**
     * @return the latestExecutedDate
     */
    public Date getLatestExecutedDate() {
        return latestExecutedDate;
    }

    /**
     * @param latestExecutedDate the latestExecutedDate to set
     */
    public void setLatestExecutedDate(Date latestExecutedDate) {
        this.latestExecutedDate = latestExecutedDate;
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
    
    
}
