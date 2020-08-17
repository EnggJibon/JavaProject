/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.authentication;

import com.kmcj.karte.resources.apiuser.MstApiUser;
import com.kmcj.karte.resources.user.MstUser;
import java.sql.Timestamp;

/**
 *
 * @author f.kitaoji
 */
public final class LoginUser {
    private final String userUuid;
    private final String userid;
    private final String apiToken;
    private final Timestamp sessionStartTime;
    private final Timestamp sessionExpireTime;
    private final int sessionTimeoutMinutes;
    private final String javaZoneId;
    private final String authId;
    private String langId;
    private final String companyId;
    private final String department;
    private final boolean external;
    private final String procCd;
    
    //private final LoginUserStore loginUserStore;
    
    public LoginUser(MstUser mstUser, String apiToken, int sessionTimeoutMinutes) {
        this.userUuid = mstUser.getUuid();
        this.userid = mstUser.getUserId();
        this.apiToken = apiToken;
        this.javaZoneId = mstUser.getMstTimezone().getJavaZoneId();
        this.authId = mstUser.getAuthId();
        this.langId = mstUser.getLangId();
        this.sessionTimeoutMinutes = sessionTimeoutMinutes;
        this.sessionStartTime = new Timestamp(System.currentTimeMillis());
        this.sessionExpireTime = new Timestamp(System.currentTimeMillis());
        this.updateExpireTime();
        this.companyId = mstUser.getCompanyId();
        this.department = mstUser.getDepartment();
        this.external = false;
        this.procCd=mstUser.getProcCd();
    }
    
    public LoginUser(MstApiUser mstUser, String apiToken, int sessionTimeoutMinutes) {
        this.userUuid = mstUser.getUuid();
        this.userid = mstUser.getUserId();
        this.apiToken = apiToken;
        this.javaZoneId = mstUser.getMstTimezone().getJavaZoneId();
        this.authId = null;
        this.langId = mstUser.getLangId();
        this.sessionTimeoutMinutes = sessionTimeoutMinutes;
        this.sessionStartTime = new Timestamp(System.currentTimeMillis());
        this.sessionExpireTime = new Timestamp(System.currentTimeMillis());
        this.updateExpireTime();
        this.companyId = mstUser.getCompanyId();
        this.department = null;
        this.external = true;
        this.procCd=null;
    }

    public final void updateExpireTime() {
        long interval = sessionTimeoutMinutes * 60 *1000;
        this.sessionExpireTime.setTime(System.currentTimeMillis() + interval);
    }

    public String getUserid() {
        return this.userid;
    }
    
    public String getApiToken() {
        return this.apiToken;
    }
    
    public boolean isExpired() {
        return System.currentTimeMillis() > this.sessionExpireTime.getTime();
    }

    /**
     * @return the sessionStartTime
     */
    public Timestamp getSessionStartTime() {
        return sessionStartTime;
    }

    /**
     * @return the sessionExpireTime
     */
    public Timestamp getSessionExpireTime() {
        return sessionExpireTime;
    }

    /**
     * @return the javaZoneId
     */
    public String getJavaZoneId() {
        return javaZoneId;
    }

    /**
     * @return the langId
     */
    public String getLangId() {
        return langId;
    }

    /**
     * @return the userUuid
     */
    public String getUserUuid() {
        return userUuid;
    }

    /**
     * @param langId the langId to set
     */
    public void setLangId(String langId) {
        this.langId = langId;
    }

    /**
     * @return the authId
     */
    public String getAuthId() {
        return authId;
    }

    /**
     * @return the companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @return the external
     */
    public boolean isExternal() {
        return external;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }
    
    /**
     * @return the procCd
     */
    public String getProcCd() {
        return procCd;
    }
}
