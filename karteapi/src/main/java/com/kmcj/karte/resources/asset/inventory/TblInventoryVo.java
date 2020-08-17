/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author admin
 */
public class TblInventoryVo extends BasicResponse {

    private String uuid;

    private String name;

    private int mgmtCompanyCount;

    private int receivedMgmtCompanyCount;

    private Date registrationDate;

    private Date requestedDate;

    private Date finalDueDate;

    private int status;
    
    private String statusText;

    private Date completedDate;

    private Date createDate;

    private Date updateDate;

    private String createUserUuid;

    private String updateUserUuid;

    public TblInventoryVo() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMgmtCompanyCount() {
        return mgmtCompanyCount;
    }

    public void setMgmtCompanyCount(int mgmtCompanyCount) {
        this.mgmtCompanyCount = mgmtCompanyCount;
    }

    public int getReceivedMgmtCompanyCount() {
        return receivedMgmtCompanyCount;
    }

    public void setReceivedMgmtCompanyCount(int receivedMgmtCompanyCount) {
        this.receivedMgmtCompanyCount = receivedMgmtCompanyCount;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Date getFinalDueDate() {
        return finalDueDate;
    }

    public void setFinalDueDate(Date finalDueDate) {
        this.finalDueDate = finalDueDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    /**
     * @return the statusText
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * @param statusText the statusText to set
     */
    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

}
