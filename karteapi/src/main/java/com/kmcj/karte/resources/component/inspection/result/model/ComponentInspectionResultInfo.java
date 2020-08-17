/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.component.inspection.item.model.ComponentInspectionItemsFileDetail;
import com.kmcj.karte.resources.component.inspection.item.model.MstComponentInspectionItemsTableClassVo;
import com.kmcj.karte.resources.component.inspection.referencefile.model.ComponentInspectionReferenceFile;
import com.kmcj.karte.resources.component.inspection.result.SampleGroupPlotDataInfo;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionSampleName;
import java.math.BigDecimal;

import java.util.Date;
import java.util.List;

import static com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResult.AbortFlg;

/**
 * Component inspection result info model.
 *
 * @author duanlin
 */
public class ComponentInspectionResultInfo extends BasicResponse{

    private String componentInspectionItemsTableId;
    private String componentInspectionResultId;

    private String componentId;
    private String componentCode;
    private String componentName;
    private Integer componentType;
    private String firstFlag;
    private String firstFlagName;
    private String poNumber;
    private String itemNumber;
    private String productionLotNum;
    private Integer quantity;
    private Integer cavityCnt;
    private Integer cavityStartNum;
    private String cavityPrefix;
    private Integer cavityNum;
    private String outgoingCompanyName;
    private String incomingCompanyName;
    private String outgoingCompanyId;
    private String incomingCompanyId;

    private Integer inspectionStatus; //    private Integer inspectionStatus; 
    private Integer outgoingMeasSamplingQuantity;
    private Integer outgoingVisualSamplingQuantity;
    private BigDecimal measSamplingRatio;
    private BigDecimal visSamplingRatio;
    private String outgoingInspectionPersonName;
    private String outgoingInspectionDate;
    private Integer outgoingInspectionResult;//
    private String outgoingInspectionApprovePersonName;
    private String outgoingInspectionApproveDate;
    private String outgoingInspectionComment;

    private Integer incomingMeasSamplingQuantity;
    private Integer incomingVisualSamplingQuantity;
    private String incomingInspectionPersonName;
    private String incomingInspectionDate;
    private Integer incomingInspectionResult;//
    private String incomingInspectionApprovePersonName;
    private String incomingInspectionApproveDate;
    private String incomingInspectionComment;

    private String acceptancePersonName;
    private String acceptanceDate;
    private String exemptionApprovePersonName;
    private String exemptionApproveDate;
    
    private AbortFlg abortFlg = AbortFlg.RUNNING;

    private List<ComponentInspectionItemResultDetail> outgoingMeasureResultDetails;
    private List<ComponentInspectionItemResultDetail> outgoingVisualResultDetails;
    private List<ComponentInspectionItemResultDetail> incomingMeasureResultDetails;
    private List<ComponentInspectionItemResultDetail> incomingVisualResultDetails;

    //-------------------Apeng 20171010 add start---------------------------
    private String outgoingConfirmerUuid; //出荷検査確認者UUID
    private String outgoingConfirmerName; //出荷検査確認者NAME
    private String outgoingPrivateComment; //出荷検査コメント(非公開)
    private String outgoingConfirmDate; //出荷検査確認日
    private String incomingConfirmerUuid; //入荷検査確認者UUID
    private String incomingConfirmerName; //入荷検査確認者NAME
    private String incomingPrivateComment; //入荷検査コメント(非公開)
    private String incomingConfirmDate; //入荷検査確認日
    //-------------------Apeng 20171010 add end---------------------------

    //-------------------Apeng 20171031 add start---------------------------
    private ComponentInspectionReferenceFile componentInspectionReferenceFile;
    private MstComponentInspectionItemsTableClassVo mstComponentInspectionItemsTableClassVo;
    private String fileNotEnough;
    private int massFlg;
    //-------------------Apeng 20171131 add end---------------------------
    private String inspBatchUpdateStatus;

    //Apeng 20180201 add start
    private List<TblComponentInspectionSampleName> tblComponentInspectionSampleNameMeasures;
    private List<TblComponentInspectionSampleName> tblComponentInspectionSampleNameVisuals;
    private String dataRelationTgt;
    //Apeng 20180201 add end

    //Apeng 20180212 add start
    private String outgoingCompanyCode;
    private String incomingCompanyCode;
    //Apeng 20180212 add end

    private String fileConfirmStatus;

    //ACV start 20180510
    private String outgoingApproveResult;
    private String outgoingConfirmResult;
    private String incomingApproveResult;
    private String incomingConfirmResult;
    //ACV end 20180510

    //ACV start 20180608
    private String material01;
    private String material02;
    private String material03;
    //ACV end 20180608

    private Date createDate;
    private Date updateDate;
    
    private String componentInspectTypeId;
    private String componentInspectTypeName;
    
    private String fileConfirmStatusDisplay;
    
    private String fileInputStatusDisplay;
    private String fileApproverId;
    private String fileApproverName;

    private List<ComponentInspectionItemsFileDetail> componentInspectionItemsFileDetails;

    private String cpGoal;
    private String cpkGoal;
    private String ccGoal;
    private BigDecimal processCapabilityindexMax;
    private BigDecimal processCapabilityindexMin;
    private BigDecimal sampleMean;
    private BigDecimal rbar;
    private BigDecimal XLCL;
    private BigDecimal XUCL;
    private BigDecimal RLCL;
    private BigDecimal RUCL;
    private int n;
    private BigDecimal A2;
    private BigDecimal D3;
    private BigDecimal D4;
    private BigDecimal rangeMax;
    private BigDecimal meanMax;
    private BigDecimal rangeMin;
    private BigDecimal meanMin;
    private String inspectionItemSno;
    private List<String> cavList;
    private long count;
    private List<ComponentInspectionItemResultDetail> inspectionItemMeasureResultDetails;
    private List<SampleGroupPlotDataInfo> sampleGroupPlotDataInfoList;

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

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Integer getComponentType() {
        return componentType;
    }

    public void setComponentType(Integer componentType) {
        this.componentType = componentType;
    }

    public String getFirstFlag() {
        return firstFlag;
    }

    public void setFirstFlag(String firstFlag) {
        this.firstFlag = firstFlag;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOutgoingCompanyName() {
        return outgoingCompanyName;
    }

    public void setOutgoingCompanyName(String outgoingCompanyName) {
        this.outgoingCompanyName = outgoingCompanyName;
    }

    public String getIncomingCompanyName() {
        return incomingCompanyName;
    }

    public void setIncomingCompanyName(String incomingCompanyName) {
        this.incomingCompanyName = incomingCompanyName;
    }

    public Integer getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(Integer inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
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

    public BigDecimal getMeasSamplingRatio() {
        return measSamplingRatio;
    }

    public void setMeasSamplingRatio(BigDecimal measSamplingRatio) {
        this.measSamplingRatio = measSamplingRatio;
    }

    public BigDecimal getVisSamplingRatio() {
        return visSamplingRatio;
    }

    public void setVisSamplingRatio(BigDecimal visSamplingRatio) {
        this.visSamplingRatio = visSamplingRatio;
    }

    public String getOutgoingInspectionPersonName() {
        return outgoingInspectionPersonName;
    }

    public void setOutgoingInspectionPersonName(String outgoingInspectionPersonName) {
        this.outgoingInspectionPersonName = outgoingInspectionPersonName;
    }

    public String getOutgoingInspectionDate() {
        return outgoingInspectionDate;
    }

    public void setOutgoingInspectionDate(String outgoingInspectionDate) {
        this.outgoingInspectionDate = outgoingInspectionDate;
    }

    public Integer getOutgoingInspectionResult() {
        return outgoingInspectionResult;
    }

    public void setOutgoingInspectionResult(Integer outgoingInspectionResult) {
        this.outgoingInspectionResult = outgoingInspectionResult;
    }

    public String getOutgoingInspectionApprovePersonName() {
        return outgoingInspectionApprovePersonName;
    }

    public void setOutgoingInspectionApprovePersonName(String outgoingInspectionApprovePersonName) {
        this.outgoingInspectionApprovePersonName = outgoingInspectionApprovePersonName;
    }

    public String getOutgoingInspectionApproveDate() {
        return outgoingInspectionApproveDate;
    }

    public void setOutgoingInspectionApproveDate(String outgoingInspectionApproveDate) {
        this.outgoingInspectionApproveDate = outgoingInspectionApproveDate;
    }

    public String getOutgoingInspectionComment() {
        return outgoingInspectionComment;
    }

    public void setOutgoingInspectionComment(String outgoingInspectionComment) {
        this.outgoingInspectionComment = outgoingInspectionComment;
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

    public String getIncomingInspectionPersonName() {
        return incomingInspectionPersonName;
    }

    public void setIncomingInspectionPersonName(String incomingInspectionPersonName) {
        this.incomingInspectionPersonName = incomingInspectionPersonName;
    }

    public String getIncomingInspectionDate() {
        return incomingInspectionDate;
    }

    public void setIncomingInspectionDate(String incomingInspectionDate) {
        this.incomingInspectionDate = incomingInspectionDate;
    }

    public Integer getIncomingInspectionResult() {
        return incomingInspectionResult;
    }

    public void setIncomingInspectionResult(Integer incomingInspectionResult) {
        this.incomingInspectionResult = incomingInspectionResult;
    }

    public String getIncomingInspectionApprovePersonName() {
        return incomingInspectionApprovePersonName;
    }

    public void setIncomingInspectionApprovePersonName(String incomingInspectionApprovePersonName) {
        this.incomingInspectionApprovePersonName = incomingInspectionApprovePersonName;
    }

    public String getIncomingInspectionApproveDate() {
        return incomingInspectionApproveDate;
    }

    public void setIncomingInspectionApproveDate(String incomingInspectionApproveDate) {
        this.incomingInspectionApproveDate = incomingInspectionApproveDate;
    }

    public String getIncomingInspectionComment() {
        return incomingInspectionComment;
    }

    public void setIncomingInspectionComment(String incomingInspectionComment) {
        this.incomingInspectionComment = incomingInspectionComment;
    }

    public String getAcceptancePersonName() {
        return acceptancePersonName;
    }

    public void setAcceptancePersonName(String acceptancePersonName) {
        this.acceptancePersonName = acceptancePersonName;
    }

    public String getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(String acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public String getExemptionApprovePersonName() {
        return exemptionApprovePersonName;
    }

    public void setExemptionApprovePersonName(String exemptionApprovePersonName) {
        this.exemptionApprovePersonName = exemptionApprovePersonName;
    }

    public String getExemptionApproveDate() {
        return exemptionApproveDate;
    }

    public void setExemptionApproveDate(String exemptionApproveDate) {
        this.exemptionApproveDate = exemptionApproveDate;
    }

    public AbortFlg getAbortFlg() {
        return abortFlg;
    }

    public void setAbortFlg(AbortFlg abortFlg) {
        this.abortFlg = abortFlg;
    }

    public List<ComponentInspectionItemResultDetail> getOutgoingMeasureResultDetails() {
        return outgoingMeasureResultDetails;
    }

    public void setOutgoingMeasureResultDetails(List<ComponentInspectionItemResultDetail> outgoingMeasureResultDetails) {
        this.outgoingMeasureResultDetails = outgoingMeasureResultDetails;
    }

    public List<ComponentInspectionItemResultDetail> getIncomingMeasureResultDetails() {
        return incomingMeasureResultDetails;
    }

    public void setIncomingMeasureResultDetails(List<ComponentInspectionItemResultDetail> incomingMeasureResultDetails) {
        this.incomingMeasureResultDetails = incomingMeasureResultDetails;
    }

    public List<ComponentInspectionItemResultDetail> getOutgoingVisualResultDetails() {
        return outgoingVisualResultDetails;
    }

    public void setOutgoingVisualResultDetails(List<ComponentInspectionItemResultDetail> outgoingVisualResultDetails) {
        this.outgoingVisualResultDetails = outgoingVisualResultDetails;
    }

    public List<ComponentInspectionItemResultDetail> getIncomingVisualResultDetails() {
        return incomingVisualResultDetails;
    }

    public void setIncomingVisualResultDetails(List<ComponentInspectionItemResultDetail> incomingVisualResultDetails) {
        this.incomingVisualResultDetails = incomingVisualResultDetails;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the outgoingConfirmerUuid
     */
    public String getOutgoingConfirmerUuid() {
        return outgoingConfirmerUuid;
    }

    /**
     * @param outgoingConfirmerUuid the outgoingConfirmerUuid to set
     */
    public void setOutgoingConfirmerUuid(String outgoingConfirmerUuid) {
        this.outgoingConfirmerUuid = outgoingConfirmerUuid;
    }

    /**
     * @return the outgoingConfirmerName
     */
    public String getOutgoingConfirmerName() {
        return outgoingConfirmerName;
    }

    /**
     * @param outgoingConfirmerName the outgoingConfirmerName to set
     */
    public void setOutgoingConfirmerName(String outgoingConfirmerName) {
        this.outgoingConfirmerName = outgoingConfirmerName;
    }

    /**
     * @return the incomingConfirmerUuid
     */
    public String getIncomingConfirmerUuid() {
        return incomingConfirmerUuid;
    }

    /**
     * @param incomingConfirmerUuid the incomingConfirmerUuid to set
     */
    public void setIncomingConfirmerUuid(String incomingConfirmerUuid) {
        this.incomingConfirmerUuid = incomingConfirmerUuid;
    }

    /**
     * @return the incomingConfirmerName
     */
    public String getIncomingConfirmerName() {
        return incomingConfirmerName;
    }

    /**
     * @param incomingConfirmerName the incomingConfirmerName to set
     */
    public void setIncomingConfirmerName(String incomingConfirmerName) {
        this.incomingConfirmerName = incomingConfirmerName;
    }

    /**
     * @return the outgoingPrivateComment
     */
    public String getOutgoingPrivateComment() {
        return outgoingPrivateComment;
    }

    /**
     * @param outgoingPrivateComment the outgoingPrivateComment to set
     */
    public void setOutgoingPrivateComment(String outgoingPrivateComment) {
        this.outgoingPrivateComment = outgoingPrivateComment;
    }

    /**
     * @return the incomingPrivateComment
     */
    public String getIncomingPrivateComment() {
        return incomingPrivateComment;
    }

    /**
     * @param incomingPrivateComment the incomingPrivateComment to set
     */
    public void setIncomingPrivateComment(String incomingPrivateComment) {
        this.incomingPrivateComment = incomingPrivateComment;
    }

    /**
     * @return the outgoingConfirmDate
     */
    public String getOutgoingConfirmDate() {
        return outgoingConfirmDate;
    }

    /**
     * @param outgoingConfirmDate the outgoingConfirmDate to set
     */
    public void setOutgoingConfirmDate(String outgoingConfirmDate) {
        this.outgoingConfirmDate = outgoingConfirmDate;
    }

    /**
     * @return the incomingConfirmDate
     */
    public String getIncomingConfirmDate() {
        return incomingConfirmDate;
    }

    /**
     * @param incomingConfirmDate the incomingConfirmDate to set
     */
    public void setIncomingConfirmDate(String incomingConfirmDate) {
        this.incomingConfirmDate = incomingConfirmDate;
    }

    /**
     * @return the componentInspectionReferenceFile
     */
    public ComponentInspectionReferenceFile getComponentInspectionReferenceFile() {
        return componentInspectionReferenceFile;
    }

    /**
     * @param componentInspectionReferenceFile the componentInspectionReferenceFile to set
     */
    public void setComponentInspectionReferenceFile(ComponentInspectionReferenceFile componentInspectionReferenceFile) {
        this.componentInspectionReferenceFile = componentInspectionReferenceFile;
    }

    /**
     * @return the outgoingCompanyId
     */
    public String getOutgoingCompanyId() {
        return outgoingCompanyId;
    }

    /**
     * @param outgoingCompanyId the outgoingCompanyId to set
     */
    public void setOutgoingCompanyId(String outgoingCompanyId) {
        this.outgoingCompanyId = outgoingCompanyId;
    }

    /**
     * @return the massFlg
     */
    public int getMassFlg() {
        return massFlg;
    }

    /**
     * @param massFlg the massFlg to set
     */
    public void setMassFlg(int massFlg) {
        this.massFlg = massFlg;
    }

    /**
     * @return the incomingCompanyId
     */
    public String getIncomingCompanyId() {
        return incomingCompanyId;
    }

    /**
     * @param incomingCompanyId the incomingCompanyId to set
     */
    public void setIncomingCompanyId(String incomingCompanyId) {
        this.incomingCompanyId = incomingCompanyId;
    }

    /**
     * @return the firstFlagName
     */
    public String getFirstFlagName() {
        return firstFlagName;
    }

    /**
     * @param firstFlagName the firstFlagName to set
     */
    public void setFirstFlagName(String firstFlagName) {
        this.firstFlagName = firstFlagName;
    }

    /**
     * @return the fileNotEnough
     */
    public String getFileNotEnough() {
        return fileNotEnough;
    }

    /**
     * @param fileNotEnough the fileNotEnough to set
     */
    public void setFileNotEnough(String fileNotEnough) {
        this.fileNotEnough = fileNotEnough;
    }

    /**
     * @return the inspBatchUpdateStatus
     */
    public String getInspBatchUpdateStatus() {
        return inspBatchUpdateStatus;
    }

    /**
     * @param inspBatchUpdateStatus the inspBatchUpdateStatus to set
     */
    public void setInspBatchUpdateStatus(String inspBatchUpdateStatus) {
        this.inspBatchUpdateStatus = inspBatchUpdateStatus;
    }

    /**
     * @return the mstComponentInspectionItemsTableClassVo
     */
    public MstComponentInspectionItemsTableClassVo getMstComponentInspectionItemsTableClassVo() {
        return mstComponentInspectionItemsTableClassVo;
    }

    /**
     * @param mstComponentInspectionItemsTableClassVo the mstComponentInspectionItemsTableClassVo to set
     */
    public void setMstComponentInspectionItemsTableClassVo(MstComponentInspectionItemsTableClassVo mstComponentInspectionItemsTableClassVo) {
        this.mstComponentInspectionItemsTableClassVo = mstComponentInspectionItemsTableClassVo;
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
     * @return the tblComponentInspectionSampleNameMeasures
     */
    public List<TblComponentInspectionSampleName> getTblComponentInspectionSampleNameMeasures() {
        return tblComponentInspectionSampleNameMeasures;
    }

    /**
     * @param tblComponentInspectionSampleNameMeasures the tblComponentInspectionSampleNameMeasures to set
     */
    public void setTblComponentInspectionSampleNameMeasures(List<TblComponentInspectionSampleName> tblComponentInspectionSampleNameMeasures) {
        this.tblComponentInspectionSampleNameMeasures = tblComponentInspectionSampleNameMeasures;
    }

    /**
     * @return the tblComponentInspectionSampleNameVisuals
     */
    public List<TblComponentInspectionSampleName> getTblComponentInspectionSampleNameVisuals() {
        return tblComponentInspectionSampleNameVisuals;
    }

    /**
     * @param tblComponentInspectionSampleNameVisuals the tblComponentInspectionSampleNameVisuals to set
     */
    public void setTblComponentInspectionSampleNameVisuals(List<TblComponentInspectionSampleName> tblComponentInspectionSampleNameVisuals) {
        this.tblComponentInspectionSampleNameVisuals = tblComponentInspectionSampleNameVisuals;
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
     * @return the outgoingCompanyCode
     */
    public String getOutgoingCompanyCode() {
        return outgoingCompanyCode;
    }

    /**
     * @param outgoingCompanyCode the outgoingCompanyCode to set
     */
    public void setOutgoingCompanyCode(String outgoingCompanyCode) {
        this.outgoingCompanyCode = outgoingCompanyCode;
    }

    /**
     * @return the incomingCompanyCode
     */
    public String getIncomingCompanyCode() {
        return incomingCompanyCode;
    }

    /**
     * @param incomingCompanyCode the incomingCompanyCode to set
     */
    public void setIncomingCompanyCode(String incomingCompanyCode) {
        this.incomingCompanyCode = incomingCompanyCode;
    }

    public String getFileConfirmStatus() {
        return fileConfirmStatus;
    }

    public void setFileConfirmStatus(String fileConfirmStatus) {
        this.fileConfirmStatus = fileConfirmStatus;
    }

    public String getOutgoingApproveResult() {
        return outgoingApproveResult;
    }

    public void setOutgoingApproveResult(String outgoingApproveResult) {
        this.outgoingApproveResult = outgoingApproveResult;
    }

    public String getOutgoingConfirmResult() {
        return outgoingConfirmResult;
    }

    public void setOutgoingConfirmResult(String outgoingConfirmResult) {
        this.outgoingConfirmResult = outgoingConfirmResult;
    }

    public String getIncomingApproveResult() {
        return incomingApproveResult;
    }

    public void setIncomingApproveResult(String incomingApproveResult) {
        this.incomingApproveResult = incomingApproveResult;
    }

    public String getIncomingConfirmResult() {
        return incomingConfirmResult;
    }

    public void setIncomingConfirmResult(String incomingConfirmResult) {
        this.incomingConfirmResult = incomingConfirmResult;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public List<ComponentInspectionItemsFileDetail> getComponentInspectionItemsFileDetails() {
        return componentInspectionItemsFileDetails;
    }

    public void setComponentInspectionItemsFileDetails(List<ComponentInspectionItemsFileDetail> componentInspectionItemsFileDetails) {
        this.componentInspectionItemsFileDetails = componentInspectionItemsFileDetails;
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

    public String getComponentInspectTypeId() {
        return componentInspectTypeId;
    }

    public String getComponentInspectTypeName() {
        return componentInspectTypeName;
    }

    public void setComponentInspectTypeId(String componentInspectTypeId) {
        this.componentInspectTypeId = componentInspectTypeId;
    }

    public void setComponentInspectTypeName(String componentInspectTypeName) {
        this.componentInspectTypeName = componentInspectTypeName;
    }

    public String getFileConfirmStatusDisplay() {
        return fileConfirmStatusDisplay;
    }

    public void setFileConfirmStatusDisplay(String fileConfirmStatusDisplay) {
        this.fileConfirmStatusDisplay = fileConfirmStatusDisplay;
    }

    public String getFileInputStatusDisplay() {
        return fileInputStatusDisplay;
    }

    public void setFileInputStatusDisplay(String fileInputStatusDisplay) {
        this.fileInputStatusDisplay = fileInputStatusDisplay;
    }

    public String getFileApproverId() {
        return fileApproverId;
    }

    public void setFileApproverId(String fileApproverId) {
        this.fileApproverId = fileApproverId;
    }

    public String getFileApproverName() {
        return fileApproverName;
    }

    public void setFileApproverName(String fileApproverName) {
        this.fileApproverName = fileApproverName;
    }

    public String getCpGoal() {
        return cpGoal;
    }

    public void setCpGoal(String cpGoal) {
        this.cpGoal = cpGoal;
    }

    public String getCpkGoal() {
        return cpkGoal;
    }

    public void setCpkGoal(String cpkGoal) {
        this.cpkGoal = cpkGoal;
    }

    public String getCcGoal() {
        return ccGoal;
    }

    public void setCcGoal(String ccGoal) {
        this.ccGoal = ccGoal;
    }

    public BigDecimal getProcessCapabilityindexMax() {
        return processCapabilityindexMax;
    }

    public void setProcessCapabilityindexMax(BigDecimal processCapabilityindexMax) {
        this.processCapabilityindexMax = processCapabilityindexMax;
    }

    public BigDecimal getProcessCapabilityindexMin() {
        return processCapabilityindexMin;
    }

    public void setProcessCapabilityindexMin(BigDecimal processCapabilityindexMin) {
        this.processCapabilityindexMin = processCapabilityindexMin;
    }

    public BigDecimal getSampleMean() {
        return sampleMean;
    }

    public void setSampleMean(BigDecimal sampleMean) {
        this.sampleMean = sampleMean;
    }

    public BigDecimal getRbar() {
        return rbar;
    }

    public void setRbar(BigDecimal rbar) {
        this.rbar = rbar;
    }

    public BigDecimal getXLCL() {
        return XLCL;
    }

    public void setXLCL(BigDecimal XLCL) {
        this.XLCL = XLCL;
    }

    public BigDecimal getXUCL() {
        return XUCL;
    }

    public void setXUCL(BigDecimal XUCL) {
        this.XUCL = XUCL;
    }

    public BigDecimal getRLCL() {
        return RLCL;
    }

    public void setRLCL(BigDecimal RLCL) {
        this.RLCL = RLCL;
    }

    public BigDecimal getRUCL() {
        return RUCL;
    }

    public void setRUCL(BigDecimal RUCL) {
        this.RUCL = RUCL;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public BigDecimal getA2() {
        return A2;
    }

    public void setA2(BigDecimal A2) {
        this.A2 = A2;
    }

    public BigDecimal getD3() {
        return D3;
    }

    public void setD3(BigDecimal D3) {
        this.D3 = D3;
    }

    public BigDecimal getD4() {
        return D4;
    }

    public void setD4(BigDecimal D4) {
        this.D4 = D4;
    }

    public BigDecimal getRangeMax() {
        return rangeMax;
    }

    public void setRangeMax(BigDecimal rangeMax) {
        this.rangeMax = rangeMax;
    }

    public BigDecimal getMeanMax() {
        return meanMax;
    }

    public void setMeanMax(BigDecimal meanMax) {
        this.meanMax = meanMax;
    }

    public BigDecimal getRangeMin() {
        return rangeMin;
    }

    public void setRangeMin(BigDecimal rangeMin) {
        this.rangeMin = rangeMin;
    }

    public BigDecimal getMeanMin() {
        return meanMin;
    }

    public void setMeanMin(BigDecimal meanMin) {
        this.meanMin = meanMin;
    }

    public String getInspectionItemSno() {
        return inspectionItemSno;
    }

    public void setInspectionItemSno(String inspectionItemSno) {
        this.inspectionItemSno = inspectionItemSno;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<ComponentInspectionItemResultDetail> getInspectionItemMeasureResultDetails() {
        return inspectionItemMeasureResultDetails;
    }

    public void setInspectionItemMeasureResultDetails(List<ComponentInspectionItemResultDetail> inspectionItemMeasureResultDetails) {
        this.inspectionItemMeasureResultDetails = inspectionItemMeasureResultDetails;
    }

    public List<SampleGroupPlotDataInfo> getSampleGroupPlotDataInfoList() {
        return sampleGroupPlotDataInfoList;
    }

    public void setSampleGroupPlotDataInfoList(List<SampleGroupPlotDataInfo> sampleGroupPlotDataInfoList) {
        this.sampleGroupPlotDataInfoList = sampleGroupPlotDataInfoList;
    }

    public List<String> getCavList() {
        return cavList;
    }

    public void setCavList(List<String> cavList) {
        this.cavList = cavList;
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

    public Integer getCavityNum() {
        return cavityNum;
    }

    public void setCavityNum(Integer cavityNum) {
        this.cavityNum = cavityNum;
    }
}
