package com.kmcj.karte.resources.production.suspension;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author zds
 */
public class TblProductionSuspensionVo extends BasicResponse {

    private String id;

    private String productionId;

    private Date startDatetime;

    private Date startDatetimeStz;

    private Date endDatetime;

    private Date endDatetimeStz;

    private String startDatetimeStr;

    private String startDatetimeStzStr;

    private String endDatetimeStr;

    private String endDatetimeStzStr;

    private String suspendedTimeMinutes;

    private String suspendReason;
    
    private String suspendReasonType;

    private String suspendReasonText;

    private String workId;
    
    private String workName;

    private Date createDate;

    private Date updateDate;

    private String createDateStr;

    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;
    
    private String workingDate;
    
    private String userId;
    
    private String userName;

    public String getId() {
        return id;
    }

    public String getProductionId() {
        return productionId;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public Date getStartDatetimeStz() {
        return startDatetimeStz;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public Date getEndDatetimeStz() {
        return endDatetimeStz;
    }

    public String getStartDatetimeStr() {
        return startDatetimeStr;
    }

    public String getStartDatetimeStzStr() {
        return startDatetimeStzStr;
    }

    public String getEndDatetimeStr() {
        return endDatetimeStr;
    }

    public String getEndDatetimeStzStr() {
        return endDatetimeStzStr;
    }

    public String getSuspendedTimeMinutes() {
        return suspendedTimeMinutes;
    }

    public String getSuspendReason() {
        return suspendReason;
    }

    public String getSuspendReasonText() {
        return suspendReasonText;
    }

    public String getWorkId() {
        return workId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public void setStartDatetimeStz(Date startDatetimeStz) {
        this.startDatetimeStz = startDatetimeStz;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public void setEndDatetimeStz(Date endDatetimeStz) {
        this.endDatetimeStz = endDatetimeStz;
    }

    public void setStartDatetimeStr(String startDatetimeStr) {
        this.startDatetimeStr = startDatetimeStr;
    }

    public void setStartDatetimeStzStr(String startDatetimeStzStr) {
        this.startDatetimeStzStr = startDatetimeStzStr;
    }

    public void setEndDatetimeStr(String endDatetimeStr) {
        this.endDatetimeStr = endDatetimeStr;
    }

    public void setEndDatetimeStzStr(String endDatetimeStzStr) {
        this.endDatetimeStzStr = endDatetimeStzStr;
    }

    public void setSuspendedTimeMinutes(String suspendedTimeMinutes) {
        this.suspendedTimeMinutes = suspendedTimeMinutes;
    }

    public void setSuspendReason(String suspendReason) {
        this.suspendReason = suspendReason;
    }

    public void setSuspendReasonText(String suspendReasonText) {
        this.suspendReasonText = suspendReasonText;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    /**
     * @return the workName
     */
    public String getWorkName() {
        return workName;
    }

    /**
     * @param workName the workName to set
     */
    public void setWorkName(String workName) {
        this.workName = workName;
    }

    /**
     * @return the suspendReasonType
     */
    public String getSuspendReasonType() {
        return suspendReasonType;
    }

    /**
     * @param suspendReasonType the suspendReasonType to set
     */
    public void setSuspendReasonType(String suspendReasonType) {
        this.suspendReasonType = suspendReasonType;
    }

    /**
     * @return the workingDate
     */
    public String getWorkingDate() {
        return workingDate;
    }

    /**
     * @param workingDate the workingDate to set
     */
    public void setWorkingDate(String workingDate) {
        this.workingDate = workingDate;
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
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

}
