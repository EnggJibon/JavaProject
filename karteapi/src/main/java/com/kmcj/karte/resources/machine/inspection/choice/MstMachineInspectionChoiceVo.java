/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.inspection.choice;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.machine.inspection.item.MstMachineInspectionItem;
import com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoice;
import com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoiceVo;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */

public class MstMachineInspectionChoiceVo extends BasicResponse {

    
    private String id;
    
    private String seq;
    
    private String choice;
    
    private Date createDate;
    private String createDateStr;
   
    private Date updateDate;
    private String updateDateStr;
    
    private String createUserUuid;
   
    private String updateUserUuid;
    
    private MstMachineInspectionItem mstMachineInspectionItem;
    private String mstMachineInspectionItemId;
    
    private String inspectionItemId;
    private String inspectionItemSeq;
    
    private String operationFlag;
    
    private List<MstMachineInspectionChoice> machineInspectionChoices;
    private List<MstMachineInspectionChoiceVo> machineInspectionChoiceVos;
    

    public MstMachineInspectionChoiceVo() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public void setChoice(String choice) {
        this.choice = choice;
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

    public void setMstMachineInspectionItem(MstMachineInspectionItem mstMachineInspectionItem) {
        this.mstMachineInspectionItem = mstMachineInspectionItem;
    }

    public void setMstMachineInspectionItemId(String mstMachineInspectionItemId) {
        this.mstMachineInspectionItemId = mstMachineInspectionItemId;
    }

    public void setInspectionItemId(String inspectionItemId) {
        this.inspectionItemId = inspectionItemId;
    }

    public String getId() {
        return id;
    }

    public String getSeq() {
        return seq;
    }

    public String getChoice() {
        return choice;
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

    public MstMachineInspectionItem getMstMachineInspectionItem() {
        return mstMachineInspectionItem;
    }

    public String getMstMachineInspectionItemId() {
        return mstMachineInspectionItemId;
    }

    public String getInspectionItemId() {
        return inspectionItemId;
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

    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    public String getOperationFlag() {
        return operationFlag;
    }

    public void setInspectionItemSeq(String inspectionItemSeq) {
        this.inspectionItemSeq = inspectionItemSeq;
    }

    public String getInspectionItemSeq() {
        return inspectionItemSeq;
    }

    /**
     * @return the machineInspectionChoices
     */
    public List<MstMachineInspectionChoice> getMachineInspectionChoices() {
        return machineInspectionChoices;
    }

    /**
     * @param machineInspectionChoices the machineInspectionChoices to set
     */
    public void setMachineInspectionChoices(List<MstMachineInspectionChoice> machineInspectionChoices) {
        this.machineInspectionChoices = machineInspectionChoices;
    }

    /**
     * @return the machineInspectionChoiceVos
     */
    public List<MstMachineInspectionChoiceVo> getMachineInspectionChoiceVos() {
        return machineInspectionChoiceVos;
    }

    /**
     * @param machineInspectionChoiceVos the machineInspectionChoiceVos to set
     */
    public void setMachineInspectionChoiceVos(List<MstMachineInspectionChoiceVo> machineInspectionChoiceVos) {
        this.machineInspectionChoiceVos = machineInspectionChoiceVos;
    }
    
}
