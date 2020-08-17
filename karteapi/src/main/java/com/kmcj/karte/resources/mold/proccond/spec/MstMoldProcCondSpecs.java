/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond.spec;

import java.util.Date;

public class MstMoldProcCondSpecs {

    private MstMoldProcCondSpecPK mstMoldProcCondSpecPK;

    private String id;
    private String attrId;
    private String attrCode;
    private String moldProcCondId;
    private String moldProcCondName;
    private String attrValue;
    private String createDate;
    private String updateDate;
    private String createUserUuid;
    private String updateUserUuid;

    public MstMoldProcCondSpecs() {
    }

    public MstMoldProcCondSpecPK getMstMoldProcCondSpecPK() {
        return mstMoldProcCondSpecPK;
    }

    public String getId() {
        return id;
    }

    public String getAttrId() {
        return attrId;
    }

    public String getAttrCode() {
        return attrCode;
    }

    public String getMoldProcCondId() {
        return moldProcCondId;
    }

    public String getMoldProcCondName() {
        return moldProcCondName;
    }

    public String getAttrValue() {
        return attrValue;
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

    public void setMstMoldProcCondSpecPK(MstMoldProcCondSpecPK mstMoldProcCondSpecPK) {
        this.mstMoldProcCondSpecPK = mstMoldProcCondSpecPK;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    public void setMoldProcCondId(String moldProcCondId) {
        this.moldProcCondId = moldProcCondId;
    }

    public void setMoldProcCondName(String moldProcCondName) {
        this.moldProcCondName = moldProcCondName;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
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

}
