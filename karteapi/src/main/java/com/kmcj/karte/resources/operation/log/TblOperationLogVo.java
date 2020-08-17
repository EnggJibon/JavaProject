/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.operation.log;

import com.kmcj.karte.BasicResponse;

/**
 *
 * @author kmc
 */
public class TblOperationLogVo extends BasicResponse  {
    
        private String createDate; //操作日時

        private String uuid;
        private String userId; //ユーザーID
        private String userName; //ユーザー名称

        private String dictKey; //文言キー
        private String dictValue; //文言(操作)

        private String operationParm; //パラメーター
        
        private String screenType; //画面種別
        
    /**
     * @return the createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    
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
     * @return the userId
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

    /**
     * @return the dictKey
     */
    public String getDictKey() {
        return dictKey;
    }
        
    /**
     * @param dictKey the dictKey to set
     */
    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    /**
     * @return the dictValue
     */
    public String getDictValue() {
        return dictValue;
    }
        
    /**
     * @param dictValue the dictValue to set
     */
    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    /**
     * @return the operationParm
     */
    public String getOperationParm() {
        return operationParm;
    }
        
    /**
     * @param operationParm the operationParm to set
     */
    public void setOperationParm(String operationParm) {
        this.operationParm = operationParm;
    }

    /**
     * @return the screenType
     */
    public String getScreenType() {
        return screenType;
    }
        
    /**
     * @param screenType the screenType to set
     */
    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }
    
}
