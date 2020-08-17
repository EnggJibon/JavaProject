/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.attribute;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.filelinkptn.MstFileLinkPtn;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineAttributeVo extends BasicResponse {

    private MstFileLinkPtn mstFileLinkPtn;
    private String mstFileLinkPtnId;

    private String fileLinkPtnId;

    private static final long serialVersionUID = 1L;

    private String id;

    private int machineType;
    
    private String attrId;

    private String attrCode;

    private String attrName;

    private String attrValue;

    private Integer attrType;

    private int seq;

    private Date createDate;
    private String createDateStr;

    private Date updateDate;

    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private Integer externalFlg;

    private List<MstMachineAttributeVo> mstMachineAttributeVos;

    private String machineSpecHstId;

    private String operationFlag;

    private String fileLinkPtnName;

    private String linkString;

    public MstMachineAttributeVo() {
    }

    public MstMachineAttributeVo(String id) {
        this.id = id;
    }

    public void setMstFileLinkPtn(MstFileLinkPtn mstFileLinkPtn) {
        this.mstFileLinkPtn = mstFileLinkPtn;
    }

    public void setMstFileLinkPtnId(String mstFileLinkPtnId) {
        this.mstFileLinkPtnId = mstFileLinkPtnId;
    }

    public void setFileLinkPtnId(String fileLinkPtnId) {
        this.fileLinkPtnId = fileLinkPtnId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMachineType(int machineType) {
        this.machineType = machineType;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public void setAttrType(Integer attrType) {
        this.attrType = attrType;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
    }

    public MstFileLinkPtn getMstFileLinkPtn() {
        return mstFileLinkPtn;
    }

    public String getMstFileLinkPtnId() {
        return mstFileLinkPtnId;
    }

    public String getFileLinkPtnId() {
        return fileLinkPtnId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public int getMachineType() {
        return machineType;
    }

    public String getAttrCode() {
        return attrCode;
    }

    public String getAttrName() {
        return attrName;
    }

    public Integer getAttrType() {
        return attrType;
    }

    public int getSeq() {
        return seq;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public Integer getExternalFlg() {
        return externalFlg;
    }

    public List<MstMachineAttributeVo> getMstMachineAttributeVos() {
        return mstMachineAttributeVos;
    }

    public void setMstMachineAttributeVos(List<MstMachineAttributeVo> mstMachineAttributeVos) {
        this.mstMachineAttributeVos = mstMachineAttributeVos;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getMachineSpecHstId() {
        return machineSpecHstId;
    }

    public void setMachineSpecHstId(String machineSpecHstId) {
        this.machineSpecHstId = machineSpecHstId;
    }

    public String getOperationFlag() {
        return operationFlag;
    }

    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    public String getFileLinkPtnName() {
        return fileLinkPtnName;
    }

    public String getLinkString() {
        return linkString;
    }

    public void setFileLinkPtnName(String fileLinkPtnName) {
        this.fileLinkPtnName = fileLinkPtnName;
    }

    public void setLinkString(String linkString) {
        this.linkString = linkString;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

}
