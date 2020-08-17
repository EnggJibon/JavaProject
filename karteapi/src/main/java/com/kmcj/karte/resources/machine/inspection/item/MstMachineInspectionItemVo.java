/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.inspection.item;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.machine.inspection.choice.MstMachineInspectionChoice;
import com.kmcj.karte.resources.machine.inspection.choice.MstMachineInspectionChoiceVo;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineInspectionItemVo extends BasicResponse {

    private String itemId;

    //作業大分類
    private String taskCategory1;
    private String taskCategory1Text;
    //作業中分類
    private String taskCategory2;
    private String taskCategory2Text;
    //作業小分類
    private String taskCategory3;
    private String taskCategory3Text;

    private String itemSeq;

    private String inspectionItemName;

    private String resultType;

    private Date createDate;
    private String createDateStr;

    private Date updateDate;
    private String updateDateStr;

    private String createDateUuid;

    private String updateUserUuid;

    private Integer externalFlg;
    
    private List<MstMachineInspectionItemVo> mstMachineInspectionItemVos;
    private List<MstMachineInspectionChoiceVo> mstMachineInspectionChoiceVo;
    private List<MstMachineInspectionChoice> mstMachineInspectionChoice;
    
    private String operationFlag;

    public MstMachineInspectionItemVo() {
    }

    public void setTaskCategory1(String taskCategory1) {
        this.taskCategory1 = taskCategory1;
    }

    public void setTaskCategory2(String taskCategory2) {
        this.taskCategory2 = taskCategory2;
    }

    public void setTaskCategory3(String taskCategory3) {
        this.taskCategory3 = taskCategory3;
    }

    public void setInspectionItemName(String inspectionItemName) {
        this.inspectionItemName = inspectionItemName;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateDateUuid(String createDateUuid) {
        this.createDateUuid = createDateUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
    }

    public String getTaskCategory1() {
        return taskCategory1;
    }

    public String getTaskCategory2() {
        return taskCategory2;
    }

    public String getTaskCategory3() {
        return taskCategory3;
    }

    public String getInspectionItemName() {
        return inspectionItemName;
    }

    public String getResultType() {
        return resultType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateDateUuid() {
        return createDateUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public Integer getExternalFlg() {
        return externalFlg;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setItemSeq(String itemSeq) {
        this.itemSeq = itemSeq;
    }

    public String getItemSeq() {
        return itemSeq;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setMstMachineInspectionItemVos(List<MstMachineInspectionItemVo> mstMachineInspectionItemVos) {
        this.mstMachineInspectionItemVos = mstMachineInspectionItemVos;
    }

    public List<MstMachineInspectionItemVo> getMstMachineInspectionItemVos() {
        return mstMachineInspectionItemVos;
    }

    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    public String getOperationFlag() {
        return operationFlag;
    }

    /**
     * @return the taskCategory1Text
     */
    public String getTaskCategory1Text() {
        return taskCategory1Text;
    }

    /**
     * @param taskCategory1Text the taskCategory1Text to set
     */
    public void setTaskCategory1Text(String taskCategory1Text) {
        this.taskCategory1Text = taskCategory1Text;
    }

    /**
     * @return the taskCategory2Text
     */
    public String getTaskCategory2Text() {
        return taskCategory2Text;
    }

    /**
     * @param taskCategory2Text the taskCategory2Text to set
     */
    public void setTaskCategory2Text(String taskCategory2Text) {
        this.taskCategory2Text = taskCategory2Text;
    }

    /**
     * @return the taskCategory3Text
     */
    public String getTaskCategory3Text() {
        return taskCategory3Text;
    }

    /**
     * @param taskCategory3Text the taskCategory3Text to set
     */
    public void setTaskCategory3Text(String taskCategory3Text) {
        this.taskCategory3Text = taskCategory3Text;
    }

    /**
     * @return the mstMachineInspectionChoiceVo
     */
    public List<MstMachineInspectionChoiceVo> getMstMachineInspectionChoiceVo() {
        return mstMachineInspectionChoiceVo;
    }

    /**
     * @param mstMachineInspectionChoiceVo the mstMachineInspectionChoiceVo to set
     */
    public void setMstMachineInspectionChoiceVo(List<MstMachineInspectionChoiceVo> mstMachineInspectionChoiceVo) {
        this.mstMachineInspectionChoiceVo = mstMachineInspectionChoiceVo;
    }

    /**
     * @return the mstMachineInspectionChoice
     */
    public List<MstMachineInspectionChoice> getMstMachineInspectionChoice() {
        return mstMachineInspectionChoice;
    }

    /**
     * @param mstMachineInspectionChoice the mstMachineInspectionChoice to set
     */
    public void setMstMachineInspectionChoice(List<MstMachineInspectionChoice> mstMachineInspectionChoice) {
        this.mstMachineInspectionChoice = mstMachineInspectionChoice;
    }
    
}
