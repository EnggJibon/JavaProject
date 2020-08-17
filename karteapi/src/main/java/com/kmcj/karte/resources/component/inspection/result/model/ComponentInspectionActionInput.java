/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

/**
 * Component inspection action input model.
 *
 * @author jacky
 */
public class ComponentInspectionActionInput {

    private Boolean action;
    private Integer inspectionType;
    private String comment;
    private String privateComment; //検査コメント(非公開)
    //2017.10.10
    private String inspectionResultIds;
    //20171030
    private String drawingFileUuid; //図面ファイルUUID
    private String proofFileUuid; //材料証明ファイルUUID
    private String rohsProofFileUuid; //RoHS適用証明ファイルUUID
    private String packageSpecFileUuid; //包装仕様書ファイルUUID
    private String qcPhaseFileUuid; //QC工程表ファイルUUID
    private String componentId;
    private String firstFlag;
    private String operationFlag; // 1:delete,3:update,4:add
    //-----------Apeng 20171107 add------------
    private String productionLotNum;
    
    private String batchFlag;

    private String fileConfirmStatus;

    private String material01;
    private String material02;
    private String material03;
    
    private String inspectDate;
    
    public Boolean getAction() {
        return action;
    }

    public void setAction(Boolean action) {
        this.action = action;
    }

    public Integer getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(Integer inspectionType) {
        this.inspectionType = inspectionType;
    }

    public String getComment() {
        return comment;
    }

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
     * @return the batchFlag
     */
    public String getBatchFlag() {
        return batchFlag;
    }

    /**
     * @param batchFlag the batchFlag to set
     */
    public void setBatchFlag(String batchFlag) {
        this.batchFlag = batchFlag;
    }

    public String getFileConfirmStatus() {
        return fileConfirmStatus;
    }

    public void setFileConfirmStatus(String fileConfirmStatus) {
        this.fileConfirmStatus = fileConfirmStatus;
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

    public String getInspectDate() {
        return inspectDate;
    }

    public void setInspectDate(String inspectDate) {
        this.inspectDate = inspectDate;
    }
}
