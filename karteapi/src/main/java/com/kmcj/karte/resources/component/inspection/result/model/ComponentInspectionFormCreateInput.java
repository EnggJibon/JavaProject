/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

/**
 * Component inspection form create input model.
 * 
 * @author duanlin
 */
public class ComponentInspectionFormCreateInput {
    private String componentInspectionItemsTableId;
    private String componentInspectionResultId;

    private String firstFlag;
    private Integer inspectionType;
    private String poNumber;
    private String itemNumber;//Apeng 20171222 add
    private String productionLotNum;
    private String componentId;
    private String outgoingCompanyId;
    private String incomingCompanyId;
    private Integer cavityCnt;
    private Integer cavityStartNum;
    private String cavityPrefix;
    private Integer quantity;
    private Integer outgoingMeasSamplingQuantity;
    private Integer outgoingVisualSamplingQuantity;
    private Integer incomingMeasSamplingQuantity;
    private Integer incomingVisualSamplingQuantity;
    
    private String drawingFileUuid; //図面ファイルUUID
    private String proofFileUuid; //材料証明ファイルUUID
    private String rohsProofFileUuid; //RoHS適用証明ファイルUUID
    private String packageSpecFileUuid; //包装仕様書ファイルUUID
    private String qcPhaseFileUuid; //QC工程表ファイルUUID
    private Character drawingFlg;
    private Character proofFlg;
    private Character rohsProofFlg;
    private Character packageSpecFlg;
    private Character qcPhaseFlg;
    private Integer massFlg;
    private String dictkey;

    private String material01;
    private String material02;
    private String material03;

    private String dataRelationTgt;
    
    public String getComponentInspectionItemsTableId() {
        return componentInspectionItemsTableId;
    }

    public void setComponentInspectionItemsTableId(String componentInspectionItemsTableId) {
        this.componentInspectionItemsTableId = componentInspectionItemsTableId;
    }

    public String getComponentInspectionResultId() {
        return componentInspectionResultId;
    }

    public void setComponentInspectionResultId(String componentInspectionResultId) {
        this.componentInspectionResultId = componentInspectionResultId;
    }

    public String getFirstFlag() {
        return firstFlag;
    }

    public void setFirstFlag(String firstFlag) {
        this.firstFlag = firstFlag;
    }

    public Integer getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(Integer inspectionType) {
        this.inspectionType = inspectionType;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getProductionLotNum() {
        return productionLotNum;
    }

    public void setProductionLotNum(String productionLotNum) {
        this.productionLotNum = productionLotNum;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getOutgoingCompanyId() {
        return outgoingCompanyId;
    }

    public void setOutgoingCompanyId(String outgoingCompanyId) {
        this.outgoingCompanyId = outgoingCompanyId;
    }

    public String getIncomingCompanyId() {
        return incomingCompanyId;
    }

    public void setIncomingCompanyId(String incomingCompanyId) {
        this.incomingCompanyId = incomingCompanyId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getOutgoingMeasSamplingQuantity() {
        return outgoingMeasSamplingQuantity;
    }

    public void setOutgoingMeasSamplingQuantity(Integer outgoingMeasSamplingQuantity) {
        this.outgoingMeasSamplingQuantity = outgoingMeasSamplingQuantity;
    }

    public Integer getOutgoingVisualSamplingQuantity() {
        return outgoingVisualSamplingQuantity;
    }

    public void setOutgoingVisualSamplingQuantity(Integer outgoingVisualSamplingQuantity) {
        this.outgoingVisualSamplingQuantity = outgoingVisualSamplingQuantity;
    }

    public Integer getIncomingMeasSamplingQuantity() {
        return incomingMeasSamplingQuantity;
    }

    public void setIncomingMeasSamplingQuantity(Integer incomingMeasSamplingQuantity) {
        this.incomingMeasSamplingQuantity = incomingMeasSamplingQuantity;
    }

    public Integer getIncomingVisualSamplingQuantity() {
        return incomingVisualSamplingQuantity;
    }

    public void setIncomingVisualSamplingQuantity(Integer incomingVisualSamplingQuantity) {
        this.incomingVisualSamplingQuantity = incomingVisualSamplingQuantity;
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
     * @return the drawingFlg
     */
    public Character getDrawingFlg() {
        return drawingFlg;
    }

    /**
     * @param drawingFlg the drawingFlg to set
     */
    public void setDrawingFlg(Character drawingFlg) {
        this.drawingFlg = drawingFlg;
    }

    /**
     * @return the proofFlg
     */
    public Character getProofFlg() {
        return proofFlg;
    }

    /**
     * @param proofFlg the proofFlg to set
     */
    public void setProofFlg(Character proofFlg) {
        this.proofFlg = proofFlg;
    }

    /**
     * @return the rohsProofFlg
     */
    public Character getRohsProofFlg() {
        return rohsProofFlg;
    }

    /**
     * @param rohsProofFlg the rohsProofFlg to set
     */
    public void setRohsProofFlg(Character rohsProofFlg) {
        this.rohsProofFlg = rohsProofFlg;
    }

    /**
     * @return the packageSpecFlg
     */
    public Character getPackageSpecFlg() {
        return packageSpecFlg;
    }

    /**
     * @param packageSpecFlg the packageSpecFlg to set
     */
    public void setPackageSpecFlg(Character packageSpecFlg) {
        this.packageSpecFlg = packageSpecFlg;
    }

    /**
     * @return the qcPhaseFlg
     */
    public Character getQcPhaseFlg() {
        return qcPhaseFlg;
    }

    /**
     * @param qcPhaseFlg the qcPhaseFlg to set
     */
    public void setQcPhaseFlg(Character qcPhaseFlg) {
        this.qcPhaseFlg = qcPhaseFlg;
    }

    /**
     * @return the dictkey
     */
    public String getDictkey() {
        return dictkey;
    }

    /**
     * @param dictkey the dictkey to set
     */
    public void setDictkey(String dictkey) {
        this.dictkey = dictkey;
    }

    /**
     * @return the massFlg
     */
    public Integer getMassFlg() {
        return massFlg;
    }

    /**
     * @param massFlg the massFlg to set
     */
    public void setMassFlg(Integer massFlg) {
        this.massFlg = massFlg;
    }

    /**
     * @return the itemNumber
     */
    public String getItemNumber() {
        return itemNumber;
    }

    /**
     * @param itemNumber the itemNumber to set
     */
    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
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
