/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.apiuser;

import com.kmcj.karte.BasicResponse;

/**
 *
 * @author ksen
 */
public class MstApiUserVo extends BasicResponse {

    private String companyId;
    private String companyCode;
    private String companyName;
    private String userId;
    private String userName;
    private String hashedPassword;
    private String mailAddress;
    private String langId;
    private String langName;
    private String timezone;
    private String timeZoneName;
    private String validFlg;
    private String passwordChangedAt;

    public MstApiUserVo() {
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public String getLangId() {
        return langId;
    }

    public String getLangName() {
        return langName;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public String getValidFlg() {
        return validFlg;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public void setLangName(String langName) {
        this.langName = langName;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public void setTimeZoneName(String timeZoneName) {
        this.timeZoneName = timeZoneName;
    }

    public void setValidFlg(String validFlg) {
        this.validFlg = validFlg;
    }

    public String getPasswordChangedAt() {
        return passwordChangedAt;
    }

    public void setPasswordChangedAt(String passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
    }

}
