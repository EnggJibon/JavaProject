/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class MstMoldProcConds extends BasicResponse {

    private String moldUuid;
    private String attrValue;
    private String moldProcCondAttributeCode;
    private String moldProcCondAttributeName;
    private String moldProcCondName;
    private String attrId;
    private String moldProcCondId;
    private String Id;
    private String deleteFlag;
    private int attrType;
    private String moldId;
    private String moldName;
    
    private List<MstMoldProcConds> moldProcCondVos;
    private MstMoldProcCond mstMoldProcCond;

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
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
     * @return the moldProcCondAttributeCode
     */
    public String getMoldProcCondAttributeCode() {
        return moldProcCondAttributeCode;
    }

    /**
     * @param moldProcCondAttributeCode the moldProcCondAttributeCode to set
     */
    public void setMoldProcCondAttributeCode(String moldProcCondAttributeCode) {
        this.moldProcCondAttributeCode = moldProcCondAttributeCode;
    }

    /**
     * @return the moldProcCondName
     */
    public String getMoldProcCondName() {
        return moldProcCondName;
    }

    /**
     * @param moldProcCondName the moldProcCondName to set
     */
    public void setMoldProcCondName(String moldProcCondName) {
        this.moldProcCondName = moldProcCondName;
    }

    /**
     * @return the attrId
     */
    public String getAttrId() {
        return attrId;
    }

    /**
     * @param attrId the attrId to set
     */
    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    /**
     * @return the moldProcCondId
     */
    public String getMoldProcCondId() {
        return moldProcCondId;
    }

    /**
     * @param moldProcCondId the moldProcCondId to set
     */
    public void setMoldProcCondId(String moldProcCondId) {
        this.moldProcCondId = moldProcCondId;
    }

    /**
     * @return the Id
     */
    public String getId() {
        return Id;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(String Id) {
        this.Id = Id;
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
     * @return the attrType
     */
    public int getAttrType() {
        return attrType;
    }

    /**
     * @param attrType the attrType to set
     */
    public void setAttrType(int attrType) {
        this.attrType = attrType;
    }

    /**
     * @return the moldName
     */
    public String getMoldName() {
        return moldName;
    }

    /**
     * @param moldName the moldName to set
     */
    public void setMoldName(String moldName) {
        this.moldName = moldName;
    }

    public List<MstMoldProcConds> getMoldProcCondVos() {
        return moldProcCondVos;
    }

    public void setMoldProcCondVos(List<MstMoldProcConds> moldProcCondVos) {
        this.moldProcCondVos = moldProcCondVos;
    }

    /**
     * @return the moldProcCondAttributeName
     */
    public String getMoldProcCondAttributeName() {
        return moldProcCondAttributeName;
    }

    /**
     * @param moldProcCondAttributeName the moldProcCondAttributeName to set
     */
    public void setMoldProcCondAttributeName(String moldProcCondAttributeName) {
        this.moldProcCondAttributeName = moldProcCondAttributeName;
    }

    public MstMoldProcCond getMstMoldProcCond() {
        return mstMoldProcCond;
    }

    public void setMstMoldProcCond(MstMoldProcCond mstMoldProcCond) {
        this.mstMoldProcCond = mstMoldProcCond;
    }

    public String getMoldId() {
        return moldId;
    }

    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }
    
}
