/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inspection.item;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoice;
import com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoiceVo;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class MstMoldInspectionItemVo extends BasicResponse implements Comparable<MstMoldInspectionItemVo>{

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
    private Integer seq;
    private String inspectionItemName;
    private String resultType;
    private String choiceId;
    private String choiceSeq;
    private String chioce;
    private String resultId;
    private String inspectionResult;
    private String inspectionResultText;
    private String resultSeq;
    private String deleteFlag;
    private String changeFlag;
    private String maintenanceDetailId;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;

    private List<MstMoldInspectionItemVo> moldInspectionItemVos;
    private List<MstMoldInspectionChoiceVo> moldInspectionChoiceVo;
    private List<MstMoldInspectionChoice> moldInspectionChoices;

    /**
     * @return the itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * @return the taskCategory1
     */
    public String getTaskCategory1() {
        return taskCategory1;
    }

    /**
     * @param taskCategory1 the taskCategory1 to set
     */
    public void setTaskCategory1(String taskCategory1) {
        this.taskCategory1 = taskCategory1;
    }

    /**
     * @return the taskCategory2
     */
    public String getTaskCategory2() {
        return taskCategory2;
    }

    /**
     * @param taskCategory2 the taskCategory2 to set
     */
    public void setTaskCategory2(String taskCategory2) {
        this.taskCategory2 = taskCategory2;
    }

    /**
     * @return the taskCategory3
     */
    public String getTaskCategory3() {
        return taskCategory3;
    }

    /**
     * @param taskCategory3 the taskCategory3 to set
     */
    public void setTaskCategory3(String taskCategory3) {
        this.taskCategory3 = taskCategory3;
    }

    /**
     * @return the itemSeq
     */
    public String getItemSeq() {
        return itemSeq;
    }

    /**
     * @param itemSeq the itemSeq to set
     */
    public void setItemSeq(String itemSeq) {
        this.itemSeq = itemSeq;
    }

    /**
     * @return the inspectionItemName
     */
    public String getInspectionItemName() {
        return inspectionItemName;
    }

    /**
     * @param inspectionItemName the inspectionItemName to set
     */
    public void setInspectionItemName(String inspectionItemName) {
        this.inspectionItemName = inspectionItemName;
    }

    /**
     * @return the resultType
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * @param resultType the resultType to set
     */
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    /**
     * @return the choiceId
     */
    public String getChoiceId() {
        return choiceId;
    }

    /**
     * @param choiceId the choiceId to set
     */
    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }

    /**
     * @return the choiceSeq
     */
    public String getChoiceSeq() {
        return choiceSeq;
    }

    /**
     * @param choiceSeq the choiceSeq to set
     */
    public void setChoiceSeq(String choiceSeq) {
        this.choiceSeq = choiceSeq;
    }

    /**
     * @return the chioce
     */
    public String getChioce() {
        return chioce;
    }

    /**
     * @param chioce the chioce to set
     */
    public void setChioce(String chioce) {
        this.chioce = chioce;
    }

    /**
     * @return the resultId
     */
    public String getResultId() {
        return resultId;
    }

    /**
     * @param resultId the resultId to set
     */
    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    /**
     * @return the inspectionResult
     */
    public String getInspectionResult() {
        return inspectionResult;
    }

    /**
     * @param inspectionResult the inspectionResult to set
     */
    public void setInspectionResult(String inspectionResult) {
        this.inspectionResult = inspectionResult;
    }

    /**
     * @return the inspectionResultText
     */
    public String getInspectionResultText() {
        return inspectionResultText;
    }

    /**
     * @param inspectionResultText the inspectionResultText to set
     */
    public void setInspectionResultText(String inspectionResultText) {
        this.inspectionResultText = inspectionResultText;
    }

    /**
     * @return the resultSeq
     */
    public String getResultSeq() {
        return resultSeq;
    }

    /**
     * @param resultSeq the resultSeq to set
     */
    public void setResultSeq(String resultSeq) {
        this.resultSeq = resultSeq;
    }

    /**
     * @return the maintenanceDetailId
     */
    public String getMaintenanceDetailId() {
        return maintenanceDetailId;
    }

    /**
     * @param maintenanceDetailId the maintenanceDetailId to set
     */
    public void setMaintenanceDetailId(String maintenanceDetailId) {
        this.maintenanceDetailId = maintenanceDetailId;
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

    public List<MstMoldInspectionItemVo> getMoldInspectionItemVos() {
        return moldInspectionItemVos;
    }

    public void setMoldInspectionItemVos(List<MstMoldInspectionItemVo> moldInspectionItemVos) {
        this.moldInspectionItemVos = moldInspectionItemVos;
    }

    public String getTaskCategory1Text() {
        return taskCategory1Text;
    }

    public String getTaskCategory2Text() {
        return taskCategory2Text;
    }

    public String getTaskCategory3Text() {
        return taskCategory3Text;
    }

    public void setTaskCategory1Text(String taskCategory1Text) {
        this.taskCategory1Text = taskCategory1Text;
    }

    public void setTaskCategory2Text(String taskCategory2Text) {
        this.taskCategory2Text = taskCategory2Text;
    }

    public void setTaskCategory3Text(String taskCategory3Text) {
        this.taskCategory3Text = taskCategory3Text;
    }

    public List<MstMoldInspectionChoiceVo> getMoldInspectionChoiceVo() {
        return moldInspectionChoiceVo;
    }

    public void setMoldInspectionChoiceVo(List<MstMoldInspectionChoiceVo> moldInspectionChoiceVo) {
        this.moldInspectionChoiceVo = moldInspectionChoiceVo;
    }

    public String getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(String changeFlag) {
        this.changeFlag = changeFlag;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }    

    public List<MstMoldInspectionChoice> getMoldInspectionChoices() {
        return moldInspectionChoices;
    }

    public void setMoldInspectionChoices(List<MstMoldInspectionChoice> moldInspectionChoices) {
        this.moldInspectionChoices = moldInspectionChoices;
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
    
    @Override
    public int compareTo(MstMoldInspectionItemVo vo) {
        return this.getSeq() - vo.getSeq();
    }
}
