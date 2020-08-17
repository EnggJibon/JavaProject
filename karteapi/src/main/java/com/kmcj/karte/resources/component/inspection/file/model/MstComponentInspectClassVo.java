/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.file.model;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author zf
 */
public class MstComponentInspectClassVo  extends BasicResponse {
    
    private String id;
    
    private Character massFlg;
    
    private String  massFlgName;//Apeng 20180202 add
    
    private Integer seq;
    
    private String dictKey;
    
    private String dictValue;
    
    private Date createDate;
    
    private Date updateDate;
    
    private String createUserUuid;
    
    private String updateUserUuid;
    
    private String operationFlag; // 1:delete,3:update,4:add
    
    private String deleteFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Character getMassFlg() {
        return massFlg;
    }

    public void setMassFlg(Character massFlg) {
        this.massFlg = massFlg;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
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
     * @return the deleteFlag
     */
    public String getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * @param deleteFlag the deleteFlag to set
     */
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * @return the massFlgName
     */
    public String getMassFlgName() {
        return massFlgName;
    }

    /**
     * @param massFlgName the massFlgName to set
     */
    public void setMassFlgName(String massFlgName) {
        this.massFlgName = massFlgName;
    }
    
    
    
    
}
