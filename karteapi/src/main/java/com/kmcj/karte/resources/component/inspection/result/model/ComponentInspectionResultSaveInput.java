/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

import java.util.List;

/**
 * Component inspection result save request model.
 * 
 * @author duanlin
 */
public class ComponentInspectionResultSaveInput {
    public static final String SAVE_TYPE_TEMPORARY = "temporary";
    public static final String SAVE_TYPE_REGISTER = "register";

    private String componentInspectionResultId;
    private Integer inspectionType;
    private String saveType = SAVE_TYPE_REGISTER;

    private Integer outgoingInspResult;
    private Integer incomingInspResult;
    private String comment; //検査コメント(公開)
    private String privateComment; //検査コメント(非公開)
    private String inspectionResultIds;

    private List<ComponentInspectionItemResultDetail> measureResultDetails;
    private List<ComponentInspectionItemResultDetail> visualResultDetails;

    //2017.10.16
    private Integer itemResultAuto;//自動判定
    private Integer itemResultManual;//手動判定
    private String manJudgeComment;//手動判定コメント
    private String note; //注記
    //20171030
    private String drawingFileUuid; //図面ファイルUUID
    private String proofFileUuid; //材料証明ファイルUUID
    private String rohsProofFileUuid; //RoHS適用証明ファイルUUID
    private String packageSpecFileUuid; //包装仕様書ファイルUUID
    private String qcPhaseFileUuid; //QC工程表ファイルUUID
    private String operationFlag; // 1:delete,3:update,4:add
    private String componentId;
    private String firstFlag;
    
    //-----------Apeng 20171107 add------------
    private String productionLotNum;
    
    private String componentCode;
    private String componentName;
    private Integer quantity;
    
    //-----------Apeng 20180201 add------------
    private Integer measurementType;// 1-測定,2-目視
    private String dataRelationTgt;//データ連携フラグ
    private String componentInspectionItemsTableId;
    private Integer seq;

    private String fileConfirmStatus;
    private Integer isExempt;

    //ACV start 20180608
    private String material01;
    private String material02;
    private String material03;
    //ACV end 20180608
    
    private String fileDeniedFlg;
    private Integer cavityCnt;
    private Integer cavityStartNum;
    private String cavityPrefix;

    public Integer getItemResultAuto() {
        return itemResultAuto;
    }

    public void setItemResultAuto(Integer itemResultAuto) {
        this.itemResultAuto = itemResultAuto;
    }

    public Integer getItemResultManual() {
        return itemResultManual;
    }

    public void setItemResultManual(Integer itemResultManual) {
        this.itemResultManual = itemResultManual;
    }

    public String getManJudgeComment() {
        return manJudgeComment;
    }

    public void setManJudgeComment(String manJudgeComment) {
        this.manJudgeComment = manJudgeComment;
    }
    
    public boolean isTemporarySave() {
        return SAVE_TYPE_TEMPORARY.equals(getSaveType());
    }

    public String getComponentInspectionResultId() {
        return componentInspectionResultId;
    }

    public void setComponentInspectionResultId(String componentInspectionResultId) {
        this.componentInspectionResultId = componentInspectionResultId;
    }

    public Integer getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(Integer inspectionType) {
        this.inspectionType = inspectionType;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public Integer getOutgoingInspResult() {
        return outgoingInspResult;
    }

    public void setOutgoingInspResult(Integer outgoingInspResult) {
        this.outgoingInspResult = outgoingInspResult;
    }

    public Integer getIncomingInspResult() {
        return incomingInspResult;
    }

    public void setIncomingInspResult(Integer incomingInspResult) {
        this.incomingInspResult = incomingInspResult;
    }
    
    public List<ComponentInspectionItemResultDetail> getMeasureResultDetails() {
        return measureResultDetails;
    }

    public void setMeasureResultDetails(List<ComponentInspectionItemResultDetail> measureResultDetails) {
        this.measureResultDetails = measureResultDetails;
    }

    public List<ComponentInspectionItemResultDetail> getVisualResultDetails() {
        return visualResultDetails;
    }

    public void setVisualResultDetails(List<ComponentInspectionItemResultDetail> visualResultDetails) {
        this.visualResultDetails = visualResultDetails;
    }
    /**
     * @return the inspectionResultIds
     */
    public String getInspectionResultIds() {
        return inspectionResultIds;
    }

    /**
     * @param inspectionResultIds the inspectionResultIds to set
     */
    public void setInspectionResultIds(String inspectionResultIds) {
        this.inspectionResultIds = inspectionResultIds;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the privateComment
     */
    public String getPrivateComment() {
        return privateComment;
    }

    /**
     * @param privateComment the privateComment to set
     */
    public void setPrivateComment(String privateComment) {
        this.privateComment = privateComment;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the drawingFileUuid
     */
    public String getDrawingFileUuid() {
        return drawingFileUuid;
    }

    /**
     * @param drawingFileUuid the drawingFileUuid to set
     */
    public void setDrawingFileUuid(String drawingFileUuid) {
        this.drawingFileUuid = drawingFileUuid;
    }

    /**
     * @return the proofFileUuid
     */
    public String getProofFileUuid() {
        return proofFileUuid;
    }

    /**
     * @param proofFileUuid the proofFileUuid to set
     */
    public void setProofFileUuid(String proofFileUuid) {
        this.proofFileUuid = proofFileUuid;
    }

    /**
     * @return the rohsProofFileUuid
     */
    public String getRohsProofFileUuid() {
        return rohsProofFileUuid;
    }

    /**
     * @param rohsProofFileUuid the rohsProofFileUuid to set
     */
    public void setRohsProofFileUuid(String rohsProofFileUuid) {
        this.rohsProofFileUuid = rohsProofFileUuid;
    }

    /**
     * @return the packageSpecFileUuid
     */
    public String getPackageSpecFileUuid() {
        return packageSpecFileUuid;
    }

    /**
     * @param packageSpecFileUuid the packageSpecFileUuid to set
     */
    public void setPackageSpecFileUuid(String packageSpecFileUuid) {
        this.packageSpecFileUuid = packageSpecFileUuid;
    }

    /**
     * @return the qcPhaseFileUuid
     */
    public String getQcPhaseFileUuid() {
        return qcPhaseFileUuid;
    }

    /**
     * @param qcPhaseFileUuid the qcPhaseFileUuid to set
     */
    public void setQcPhaseFileUuid(String qcPhaseFileUuid) {
        this.qcPhaseFileUuid = qcPhaseFileUuid;
    }

    /**
     * @return the operationFlag
     */
    public String getOperationFlag() {
        return operationFlag;
    }

    /**
     * @param operationFlag the operationFlag to set
     */
    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    /**
     * @return the componentId
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * @param componentId the componentId to set
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the firstFlag
     */
    public String getFirstFlag() {
        return firstFlag;
    }

    /**
     * @param firstFlag the firstFlag to set
     */
    public void setFirstFlag(String firstFlag) {
        this.firstFlag = firstFlag;
    }

    /**
     * @return the productionLotNum
     */
    public String getProductionLotNum() {
        return productionLotNum;
    }

    /**
     * @param productionLotNum the productionLotNum to set
     */
    public void setProductionLotNum(String productionLotNum) {
        this.productionLotNum = productionLotNum;
    }

    /**
     * @return the componentCode
     */
    public String getComponentCode() {
        return componentCode;
    }

    /**
     * @param componentCode the componentCode to set
     */
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    /**
     * @return the componentName
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param componentName the componentName to set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the dataRelationTgt
     */
    public String getDataRelationTgt() {
        return dataRelationTgt;
    }

    /**
     * @param dataRelationTgt the dataRelationTgt to set
     */
    public void setDataRelationTgt(String dataRelationTgt) {
        this.dataRelationTgt = dataRelationTgt;
    }

    /**
     * @return the measurementType
     */
    public Integer getMeasurementType() {
        return measurementType;
    }

    /**
     * @param measurementType the measurementType to set
     */
    public void setMeasurementType(Integer measurementType) {
        this.measurementType = measurementType;
    }

    /**
     * @return the componentInspectionItemsTableId
     */
    public String getComponentInspectionItemsTableId() {
        return componentInspectionItemsTableId;
    }

    /**
     * @param componentInspectionItemsTableId the componentInspectionItemsTableId to set
     */
    public void setComponentInspectionItemsTableId(String componentInspectionItemsTableId) {
        this.componentInspectionItemsTableId = componentInspectionItemsTableId;
    }

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

    public String getFileConfirmStatus() { return fileConfirmStatus; }

    public void setFileConfirmStatus(String fileConfirmStatus) { this.fileConfirmStatus = fileConfirmStatus; }

    public Integer getIsExempt() {
        return isExempt;
    }

    public void setIsExempt(Integer isExempt) {
        this.isExempt = isExempt;
    }

    public String getMaterial01() {
        return material01;
    }

    public void setMaterial01(String material01) {
        this.material01 = material01;
    }

    public String getMaterial02() {
        return material02;
    }

    public void setMaterial02(String material02) {
        this.material02 = material02;
    }

    public String getMaterial03() {
        return material03;
    }

    public void setMaterial03(String material03) {
        this.material03 = material03;
    }

    public String getFileDeniedFlg() {
        return fileDeniedFlg;
    }

    public void setFileDeniedFlg(String fileDeniedFlg) {
        this.fileDeniedFlg = fileDeniedFlg;
    }

    public Integer getCavityCnt() {
        return cavityCnt;
    }

    public void setCavityCnt(Integer cavityCnt) {
        this.cavityCnt = cavityCnt;
    }

    public Integer getCavityStartNum() {
        return cavityStartNum;
    }

    public void setCavityStartNum(Integer cavityStartNum) {
        this.cavityStartNum = cavityStartNum;
    }

    public String getCavityPrefix() {
        return cavityPrefix;
    }

    public void setCavityPrefix(String cavityPrefix) {
        this.cavityPrefix = cavityPrefix;
    }

}
