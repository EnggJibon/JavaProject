package com.kmcj.karte.resources.sigma;

import com.kmcj.karte.BasicResponse;

/**
 *
 * @author admin
 */
public class MstSigmaVo extends BasicResponse {

    private String id;

    private String sigmaCode;

    private String filesPath;
    
    private String backupFilesPath;

    private String machineName;

    private String ipAddress;
    
    private int countErrorNotice;
    
    private int errorNoticeInterval;

    private String validFlg;

    private String createDate;

    private String updateDate;

    private String createUserUuid;

    private String updateUserUuid;
    
    private String operationFlag;
    
    private String gunshiUser;
    private String gunshiPassword;

    public MstSigmaVo() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSigmaCode(String sigmaCode) {
        this.sigmaCode = sigmaCode;
    }

    public void setFilesPath(String filesPath) {
        this.filesPath = filesPath;
    }

    public void setBackupFilesPath(String backupFilesPath) {
        this.backupFilesPath = backupFilesPath;
    }
    
    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setCountErrorNotice(int countErrorNotice) {
        this.countErrorNotice = countErrorNotice;
    }
    
    public void setErrorNoticeInterval(int errorNoticeInterval) {
        this.errorNoticeInterval = errorNoticeInterval;
    }
 
    public void setValidFlg(String validFlg) {
        this.validFlg = validFlg;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public String getId() {
        return id;
    }

    public String getSigmaCode() {
        return sigmaCode;
    }

    public String getFilesPath() {
        return filesPath;
    }
    
    public String getBackupFilesPath() {
        return backupFilesPath;
    }

    public String getMachineName() {
        return machineName;
    }

    public String getIpAddress() {
        return ipAddress;
    }
       
    public int getCountErrorNotice() {
        return countErrorNotice;
    }

    public int getErrorNoticeInterval() {
        return errorNoticeInterval;
    }

    public String getValidFlg() {
        return validFlg;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    /**
     * @return the operationFlag
     */
    public String getOperationFlag() {
        return operationFlag;
    }

    /**
     * @param operationFlag the operationFlag to set
     */
    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    /**
     * @return the gunshiUser
     */
    public String getGunshiUser() {
        return gunshiUser;
    }

    /**
     * @param gunshiUser the gunshiUser to set
     */
    public void setGunshiUser(String gunshiUser) {
        this.gunshiUser = gunshiUser;
    }

    /**
     * @return the gunshiPassword
     */
    public String getGunshiPassword() {
        return gunshiPassword;
    }

    /**
     * @param gunshiPassword the gunshiPassword to set
     */
    public void setGunshiPassword(String gunshiPassword) {
        this.gunshiPassword = gunshiPassword;
    }

}
