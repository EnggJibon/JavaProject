/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.attribute;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class MstMoldAttributes extends BasicResponse{
    private String id;
    //金型属性コード
    private String attrCode;
    //金型属性名称
    private String attrName;
     //金型属性名称
    private String attrValue;
    //属性タイプ
    private Integer attrType;
    //連番
    private int seq; 
    //ファイルリンクパターン
    private String fileLinkPtnId;
    
    private String fileLinkPtnName;
    
    //ファイルリンクパターンString
    private String linkString;
    //削除flag
    private String deleteFlag;
    //金型種類
    private Integer moldType;
    
    private String attrId;
    private String moldSpecHstId;
    private int externalFlg;
    private List<MstMoldAttributes> mstMoldAttributes;

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
     * @return the moldType
     */
    public Integer getMoldType() {
        return moldType;
    }

    /**
     * @param moldType the moldType to set
     */
    public void setMoldType(Integer moldType) {
        this.moldType = moldType;
    }

    public List<MstMoldAttributes> getMstMoldAttributes() {
        return mstMoldAttributes;
    }

    public void setMstMoldAttributes(List<MstMoldAttributes> mstMoldAttributes) {
        this.mstMoldAttributes = mstMoldAttributes;
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

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    /**
     * @return the moldSpecHstId
     */
    public String getMoldSpecHstId() {
        return moldSpecHstId;
    }

    /**
     * @param moldSpecHstId the moldSpecHstId to set
     */
    public void setMoldSpecHstId(String moldSpecHstId) {
        this.moldSpecHstId = moldSpecHstId;
    }

    /**
     * @return the externalFlg
     */
    public int getExternalFlg() {
        return externalFlg;
    }

    /**
     * @param externalFlg the externalFlg to set
     */
    public void setExternalFlg(int externalFlg) {
        this.externalFlg = externalFlg;
    }
    
    
}
