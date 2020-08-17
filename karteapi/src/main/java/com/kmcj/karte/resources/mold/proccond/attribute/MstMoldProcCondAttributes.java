/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond.attribute;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class MstMoldProcCondAttributes extends BasicResponse{
    
    public MstMoldProcCondAttributes(){
        
    }
    
    private String id;
    //連番
    private Integer seq;
    //金型種類
    private Integer moldType;
    //金型属性コード
    private String attrCode;
    //金型属性名称
    private String attrName;
    //金型属性名称
    private String attrValue;
    //属性タイプ
    private Integer attrType;
    //deleteflag
    private String deleteFlag;
    
    private int externalFlg;
    
    private List<MstMoldProcCondAttributes> moldProcCondAttributes;

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

    public List<MstMoldProcCondAttributes> getMoldProcCondAttributes() {
        return moldProcCondAttributes;
    }

    public void setMoldProcCondAttributes(List<MstMoldProcCondAttributes> moldProcCondAttributes) {
        this.moldProcCondAttributes = moldProcCondAttributes;
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
