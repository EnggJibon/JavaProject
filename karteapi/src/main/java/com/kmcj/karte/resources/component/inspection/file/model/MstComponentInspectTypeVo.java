/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.file.model;

import com.kmcj.karte.BasicResponse;
import java.util.Date;
import java.util.List;

/**
 *
 * @author zf
 */
public class MstComponentInspectTypeVo extends BasicResponse{
    
    private String id;
   
    private Integer seq;
    
    private String dictKey;
    
    private String dictValue;
    
    private String langKey;
    
    private Date createDate;
    
    private Date updateDate;
    
    private String createUserUuid;
    
    private String updateUserUuid;
    
    private String operationFlag; // 1:delete,3:update,4:add
    
    private String deleteFlag;
    
    private List<MstComponentInspectFileVo> mstComponentInspectFileVos;
    
    public MstComponentInspectTypeVo() {
    }

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
     * @return the seq
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
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
     * @return the langKey
     */
    public String getLangKey() {
        return langKey;
    }

    /**
     * @param langKey the langKey to set
     */
    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the createUserUuid
     */
    public String getCreateUserUuid() {
        return createUserUuid;
    }

    /**
     * @param createUserUuid the createUserUuid to set
     */
    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    /**
     * @return the updateUserUuid
     */
    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    /**
     * @param updateUserUuid the updateUserUuid to set
     */
    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
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
     * @return the mstComponentInspectFileVos
     */
    public List<MstComponentInspectFileVo> getMstComponentInspectFileVos() {
        return mstComponentInspectFileVos;
    }

    /**
     * @param mstComponentInspectFileVos the mstComponentInspectFileVos to set
     */
    public void setMstComponentInspectFileVos(List<MstComponentInspectFileVo> mstComponentInspectFileVos) {
        this.mstComponentInspectFileVos = mstComponentInspectFileVos;
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
    
}
