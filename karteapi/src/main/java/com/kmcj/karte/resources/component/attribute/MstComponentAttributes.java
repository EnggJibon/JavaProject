/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.attribute;

import com.kmcj.karte.BasicResponse;

/**
 *
 * @author jiangxs
 */
public class MstComponentAttributes  extends BasicResponse{
 
    private String id;
    //部品属性コード
    private String attrCode;
    //部品属性名称
    private String attrName;
     //部品属性値
    private String attrValue;
    //属性タイプ
    private Integer attrType;
    //連番
    private int seq; 
    //ファイルリンクパターン
    private String fileLinkPtnId;
    private String deleteFlag;
    private String fileLinkPtnName;
    private String linkString;
        private Integer componentType;
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
     * @return the attrCode
     */
    public String getAttrCode() {
        return attrCode;
    }

    /**
     * @param attrCode the attrCode to set
     */
    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    /**
     * @return the attrName
     */
    public String getAttrName() {
        return attrName;
    }

    /**
     * @param attrName the attrName to set
     */
    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    /**
     * @return the attrType
     */
    public Integer getAttrType() {
        return attrType;
    }

    /**
     * @param attrType the attrType to set
     */
    public void setAttrType(Integer attrType) {
        this.attrType = attrType;
    }

    /**
     * @return the seq
     */
    public int getSeq() {
        return seq;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(int seq) {
        this.seq = seq;
    }

    /**
     * @return the fileLinkPtnId
     */
    public String getFileLinkPtnId() {
        return fileLinkPtnId;
    }

    /**
     * @param fileLinkPtnId the fileLinkPtnId to set
     */
    public void setFileLinkPtnId(String fileLinkPtnId) {
        this.fileLinkPtnId = fileLinkPtnId;
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
     * @return the componentType
     */
    public Integer getComponentType() {
        return componentType;
    }

    /**
     * @param componentType the componentType to set
     */
    public void setComponentType(Integer componentType) {
        this.componentType = componentType;
    }

    /**
     * @return the fileLinkPtnName
     */
    public String getFileLinkPtnName() {
        return fileLinkPtnName;
    }

    /**
     * @param fileLinkPtnName the fileLinkPtnName to set
     */
    public void setFileLinkPtnName(String fileLinkPtnName) {
        this.fileLinkPtnName = fileLinkPtnName;
    }

    /**
     * @return the linkString
     */
    public String getLinkString() {
        return linkString;
    }

    /**
     * @param linkString the linkString to set
     */
    public void setLinkString(String linkString) {
        this.linkString = linkString;
    }

    /**
     * @return the attrValue
     */
    public String getAttrValue() {
        return attrValue;
    }

    /**
     * @param attrValue the attrValue to set
     */
    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }


    
}
