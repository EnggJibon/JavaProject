/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inspection.choice;

import com.kmcj.karte.BasicResponse;
import java.util.List;

public class MstMoldInspectionChoiceVo extends BasicResponse {

    private String inspectionItemId;
    private String inspectionItemSeq;
    private String seq;
    private String id;
    private String deleteFlag;
    private String choice;
    private String createDate;
    private String updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    private List<MstMoldInspectionChoice> moldInspectionChoices;
    private List<MstMoldInspectionChoiceVo> moldInspectionChoiceVos;

    public MstMoldInspectionChoiceVo() {
    }

    public String getInspectionItemId() {
        return inspectionItemId;
    }

    public String getSeq() {
        return seq;
    }

    public String getId() {
        return id;
    }

    public String getChoice() {
        return choice;
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

    public void setInspectionItemId(String inspectionItemId) {
        this.inspectionItemId = inspectionItemId;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setChoice(String choice) {
        this.choice = choice;
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

    public List<MstMoldInspectionChoiceVo> getMoldInspectionChoiceVos() {
        return moldInspectionChoiceVos;
    }

    public void setMoldInspectionChoiceVos(List<MstMoldInspectionChoiceVo> moldInspectionChoiceVos) {
        this.moldInspectionChoiceVos = moldInspectionChoiceVos;
    }

    public String getInspectionItemSeq() {
        return inspectionItemSeq;
    }

    public void setInspectionItemSeq(String inspectionItemSeq) {
        this.inspectionItemSeq = inspectionItemSeq;
    }

    public List<MstMoldInspectionChoice> getMoldInspectionChoices() {
        return moldInspectionChoices;
    }

    public void setMoldInspectionChoices(List<MstMoldInspectionChoice> moldInspectionChoices) {
        this.moldInspectionChoices = moldInspectionChoices;
    }
    

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
