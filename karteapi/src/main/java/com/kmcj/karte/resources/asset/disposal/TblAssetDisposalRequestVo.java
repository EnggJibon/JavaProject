/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.disposal;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.asset.MstAssetVo;

import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblAssetDisposalRequestVo extends BasicResponse {

    private String uuid;

    private Date requestDate;
    
    private String requestDateStr;

    private String requestNo;

    private String fromCompanyId;

    private String fromCompanyName;

    private String toCompanyId;

    private String toCompanyName;

    private String requestUserName;

    private String requestMailAddress;

    private int moldMachineType;

    private String moldMachineTypeText;

    private String itemCode;

    private String itemName;

    private String itemCode2;

    private String itemName2;

    private int existence;

    private String existenceText;

    private int disposalRequestReason;

    private String disposalRequestReasonText;

    private String disposalRequestReasonOther;

    private String assetNo;

    private List<MstAssetVo> assetNoList; // 複数の追加時

    private String branchNo;

    private int eolConfirmation;

    private String eolConfirmationText;

    private String judgmentReason;

    private int receiveRejectReason;

    private String receiveRejectReasonText;

    private Date receiveDate;
    
    private String receiveDateStr;

    private int itemVerConfirmation;

    private String itemVerConfirmationText;

    private int oemDestination;

    private String oemDestinationText;
    
    private int disposalJudgment;
    
    private String disposalJudgmentText;
    
    private String oemAssetNo;

    private int assetMgmtConfirm;

    private String assetMgmtConfirmText;

    private Date assetMgmtConfirmDate;
    
    private String assetMgmtConfirmDateStr;

    private int apDisposalJudgment;

    private String apDisposalJudgmentText;

    private int apSupplyRemainingMonth;

    private int apFinalBulkOrder;

    private String apFinalBulkOrderText;

    private int apRejectReason;

    private String apRejectReasonText;

    private Date apConfirmDate;
    
    private String apConfirmDateStr;

    private int finalReply;

    private String finalReplyText;

    private int finalRejectReason;

    private String finalRejectReasonText;

    private Date finalReplyDate;
    
    private String finalReplyDateStr;

    private Date docRequestDate;
    
    private String docRequestDateStr;

    private Date docApprovalDate;
    
    private String docApprovalDateStr;

    private Date disposalReportSentDate;
    
    private String disposalReportSentDateStr;

    private Date disposalReportReceiptDate;
    
    private String disposalReportReceiptDateStr;

    private Date disposalProcessingCompletionDate;
    
    private String disposalProcessingCompletionDateStr;

    private String remarks;

    private int internalStatus;

    private String internalStatusText;

    private int externalStatus;

    private String externalStatusText;

    private String rejectReasonText;

    private Date createDate;

    private Date updateDate;

    private String createUserUuid;

    private String updateUserUuid;

    private String receiveUserUuid;
    private String receiveUserName;

    private String assetMgmtConfirmUserUuid;
    private String assetMgmtConfirmUserName;

    private String apConfirmUserUuid;
    private String apConfirmUserName;

    private String finalReplyUserUuid;
    private String finalReplyUserName;

    // 資産マスタの情報 Start
    // 管理地域コード
    private int mgmtRegion;
    // 管理地域名称
    private String mgmtRegionText;
    // 管理先コード
    private String mgmtCompanyCode;
    // 管理先名称
    private String mgmtCompanyName;
    // 所在先コード
    private String mgmtLocationCode;
    // 所在先名称
    private String mgmtLocationName;
    // ベンダーコード
    private String vendorCode;
    // 取得区分
    private int acquisitionType;
    // 取得区分名称
    private String acquisitionTypeText;
    // 資産クラス
    private String assetClass;
    // 使用部門
    private String usingSection;
    // 取得日
    private Date acquisitionDate;
    
    private String acquisitionDateStr;
    // 期初簿価
    private String periodBookValue;
    // 原価センタ
    private String costCenter;
    // 資産マスタの情報 End

    // 画面タイトル
    private String controlTitleStr;

    // 画面ボタン
    private String controlButtonStr;

    // 画面検索条件
    private String controlSearchConditionStr;

    // 画面明細項目
    private String controlDetailItemStr;

    // 画面権限番号
    private String functionId;

    public TblAssetDisposalRequestVo() {
    }

    /**
     * @return the disposalRequestReasonOther
     */
    public String getDisposalRequestReasonOther() {
        return disposalRequestReasonOther;
    }

    /**
     * @param disposalRequestReasonOther
     *            the disposalRequestReasonOther to set
     */
    public void setDisposalRequestReasonOther(String disposalRequestReasonOther) {
        this.disposalRequestReasonOther = disposalRequestReasonOther;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getFromCompanyId() {
        return fromCompanyId;
    }

    public void setFromCompanyId(String fromCompanyId) {
        this.fromCompanyId = fromCompanyId;
    }

    public String getToCompanyId() {
        return toCompanyId;
    }

    public void setToCompanyId(String toCompanyId) {
        this.toCompanyId = toCompanyId;
    }

    public String getRequestUserName() {
        return requestUserName;
    }

    public void setRequestUserName(String requestUserName) {
        this.requestUserName = requestUserName;
    }

    public String getRequestMailAddress() {
        return requestMailAddress;
    }

    public void setRequestMailAddress(String requestMailAddress) {
        this.requestMailAddress = requestMailAddress;
    }

    public int getMoldMachineType() {
        return moldMachineType;
    }

    public void setMoldMachineType(int moldMachineType) {
        this.moldMachineType = moldMachineType;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode2() {
        return itemCode2;
    }

    public void setItemCode2(String itemCode2) {
        this.itemCode2 = itemCode2;
    }

    public String getItemName2() {
        return itemName2;
    }

    public void setItemName2(String itemName2) {
        this.itemName2 = itemName2;
    }

    public int getExistence() {
        return existence;
    }

    public void setExistence(int existence) {
        this.existence = existence;
    }

    public int getDisposalRequestReason() {
        return disposalRequestReason;
    }

    public void setDisposalRequestReason(int disposalRequestReason) {
        this.disposalRequestReason = disposalRequestReason;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public int getEolConfirmation() {
        return eolConfirmation;
    }

    public void setEolConfirmation(int eolConfirmation) {
        this.eolConfirmation = eolConfirmation;
    }

    public String getJudgmentReason() {
        return judgmentReason;
    }

    public void setJudgmentReason(String judgmentReason) {
        this.judgmentReason = judgmentReason;
    }

    public int getReceiveRejectReason() {
        return receiveRejectReason;
    }

    public void setReceiveRejectReason(int receiveRejectReason) {
        this.receiveRejectReason = receiveRejectReason;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public int getItemVerConfirmation() {
        return itemVerConfirmation;
    }

    public void setItemVerConfirmation(int itemVerConfirmation) {
        this.itemVerConfirmation = itemVerConfirmation;
    }

    public int getOemDestination() {
        return oemDestination;
    }

    public void setOemDestination(int oemDestination) {
        this.oemDestination = oemDestination;
    }

    public String getOemAssetNo() {
        return oemAssetNo;
    }

    public void setOemAssetNo(String oemAssetNo) {
        this.oemAssetNo = oemAssetNo;
    }

    public int getAssetMgmtConfirm() {
        return assetMgmtConfirm;
    }

    public void setAssetMgmtConfirm(int assetMgmtConfirm) {
        this.assetMgmtConfirm = assetMgmtConfirm;
    }

    public Date getAssetMgmtConfirmDate() {
        return assetMgmtConfirmDate;
    }

    public void setAssetMgmtConfirmDate(Date assetMgmtConfirmDate) {
        this.assetMgmtConfirmDate = assetMgmtConfirmDate;
    }

    public int getApDisposalJudgment() {
        return apDisposalJudgment;
    }

    public void setApDisposalJudgment(int apDisposalJudgment) {
        this.apDisposalJudgment = apDisposalJudgment;
    }

    public int getApSupplyRemainingMonth() {
        return apSupplyRemainingMonth;
    }

    public void setApSupplyRemainingMonth(int apSupplyRemainingMonth) {
        this.apSupplyRemainingMonth = apSupplyRemainingMonth;
    }

    public int getApFinalBulkOrder() {
        return apFinalBulkOrder;
    }

    public void setApFinalBulkOrder(int apFinalBulkOrder) {
        this.apFinalBulkOrder = apFinalBulkOrder;
    }

    public int getApRejectReason() {
        return apRejectReason;
    }

    public void setApRejectReason(int apRejectReason) {
        this.apRejectReason = apRejectReason;
    }

    public Date getApConfirmDate() {
        return apConfirmDate;
    }

    public void setApConfirmDate(Date apConfirmDate) {
        this.apConfirmDate = apConfirmDate;
    }

    public int getFinalReply() {
        return finalReply;
    }

    public void setFinalReply(int finalReply) {
        this.finalReply = finalReply;
    }

    public int getFinalRejectReason() {
        return finalRejectReason;
    }

    public void setFinalRejectReason(int finalRejectReason) {
        this.finalRejectReason = finalRejectReason;
    }

    public Date getFinalReplyDate() {
        return finalReplyDate;
    }

    public void setFinalReplyDate(Date finalReplyDate) {
        this.finalReplyDate = finalReplyDate;
    }

    public Date getDocRequestDate() {
        return docRequestDate;
    }

    public void setDocRequestDate(Date docRequestDate) {
        this.docRequestDate = docRequestDate;
    }

    public Date getDocApprovalDate() {
        return docApprovalDate;
    }

    public void setDocApprovalDate(Date docApprovalDate) {
        this.docApprovalDate = docApprovalDate;
    }

    public Date getDisposalReportSentDate() {
        return disposalReportSentDate;
    }

    public void setDisposalReportSentDate(Date disposalReportSentDate) {
        this.disposalReportSentDate = disposalReportSentDate;
    }

    public Date getDisposalReportReceiptDate() {
        return disposalReportReceiptDate;
    }

    public void setDisposalReportReceiptDate(Date disposalReportReceiptDate) {
        this.disposalReportReceiptDate = disposalReportReceiptDate;
    }

    public Date getDisposalProcessingCompletionDate() {
        return disposalProcessingCompletionDate;
    }

    public void setDisposalProcessingCompletionDate(Date disposalProcessingCompletionDate) {
        this.disposalProcessingCompletionDate = disposalProcessingCompletionDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getInternalStatus() {
        return internalStatus;
    }

    public void setInternalStatus(int internalStatus) {
        this.internalStatus = internalStatus;
    }

    public int getExternalStatus() {
        return externalStatus;
    }

    public void setExternalStatus(int externalStatus) {
        this.externalStatus = externalStatus;
    }

    public String getRejectReasonText() {
        return rejectReasonText;
    }

    public void setRejectReasonText(String rejectReasonText) {
        this.rejectReasonText = rejectReasonText;
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

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    /**
     * @return the moldMachineTypeText
     */
    public String getMoldMachineTypeText() {
        return moldMachineTypeText;
    }

    /**
     * @param moldMachineTypeText
     *            the moldMachineTypeText to set
     */
    public void setMoldMachineTypeText(String moldMachineTypeText) {
        this.moldMachineTypeText = moldMachineTypeText;
    }

    /**
     * @return the existenceText
     */
    public String getExistenceText() {
        return existenceText;
    }

    /**
     * @param existenceText
     *            the existenceText to set
     */
    public void setExistenceText(String existenceText) {
        this.existenceText = existenceText;
    }

    /**
     * @return the disposalRequestReasonText
     */
    public String getDisposalRequestReasonText() {
        return disposalRequestReasonText;
    }

    /**
     * @param disposalRequestReasonText
     *            the disposalRequestReasonText to set
     */
    public void setDisposalRequestReasonText(String disposalRequestReasonText) {
        this.disposalRequestReasonText = disposalRequestReasonText;
    }

    /**
     * @return the eolConfirmationText
     */
    public String getEolConfirmationText() {
        return eolConfirmationText;
    }

    /**
     * @param eolConfirmationText
     *            the eolConfirmationText to set
     */
    public void setEolConfirmationText(String eolConfirmationText) {
        this.eolConfirmationText = eolConfirmationText;
    }

    /**
     * @return the receiveRejectReasonText
     */
    public String getReceiveRejectReasonText() {
        return receiveRejectReasonText;
    }

    /**
     * @param receiveRejectReasonText
     *            the receiveRejectReasonText to set
     */
    public void setReceiveRejectReasonText(String receiveRejectReasonText) {
        this.receiveRejectReasonText = receiveRejectReasonText;
    }

    /**
     * @return the itemVerConfirmationText
     */
    public String getItemVerConfirmationText() {
        return itemVerConfirmationText;
    }

    /**
     * @param itemVerConfirmationText
     *            the itemVerConfirmationText to set
     */
    public void setItemVerConfirmationText(String itemVerConfirmationText) {
        this.itemVerConfirmationText = itemVerConfirmationText;
    }

    /**
     * @return the oemDestinationText
     */
    public String getOemDestinationText() {
        return oemDestinationText;
    }

    /**
     * @param oemDestinationText
     *            the oemDestinationText to set
     */
    public void setOemDestinationText(String oemDestinationText) {
        this.oemDestinationText = oemDestinationText;
    }

    /**
     * @return the assetMgmtConfirmText
     */
    public String getAssetMgmtConfirmText() {
        return assetMgmtConfirmText;
    }

    /**
     * @param assetMgmtConfirmText
     *            the assetMgmtConfirmText to set
     */
    public void setAssetMgmtConfirmText(String assetMgmtConfirmText) {
        this.assetMgmtConfirmText = assetMgmtConfirmText;
    }

    /**
     * @return the apDisposalJudgmentText
     */
    public String getApDisposalJudgmentText() {
        return apDisposalJudgmentText;
    }

    /**
     * @param apDisposalJudgmentText
     *            the apDisposalJudgmentText to set
     */
    public void setApDisposalJudgmentText(String apDisposalJudgmentText) {
        this.apDisposalJudgmentText = apDisposalJudgmentText;
    }

    /**
     * @return the apFinalBulkOrderText
     */
    public String getApFinalBulkOrderText() {
        return apFinalBulkOrderText;
    }

    /**
     * @param apFinalBulkOrderText
     *            the apFinalBulkOrderText to set
     */
    public void setApFinalBulkOrderText(String apFinalBulkOrderText) {
        this.apFinalBulkOrderText = apFinalBulkOrderText;
    }

    /**
     * @return the apRejectReasonText
     */
    public String getApRejectReasonText() {
        return apRejectReasonText;
    }

    /**
     * @param apRejectReasonText
     *            the apRejectReasonText to set
     */
    public void setApRejectReasonText(String apRejectReasonText) {
        this.apRejectReasonText = apRejectReasonText;
    }

    /**
     * @return the finalReplyText
     */
    public String getFinalReplyText() {
        return finalReplyText;
    }

    /**
     * @param finalReplyText
     *            the finalReplyText to set
     */
    public void setFinalReplyText(String finalReplyText) {
        this.finalReplyText = finalReplyText;
    }

    /**
     * @return the finalRejectReasonText
     */
    public String getFinalRejectReasonText() {
        return finalRejectReasonText;
    }

    /**
     * @param finalRejectReasonText
     *            the finalRejectReasonText to set
     */
    public void setFinalRejectReasonText(String finalRejectReasonText) {
        this.finalRejectReasonText = finalRejectReasonText;
    }

    /**
     * @return the internalStatusText
     */
    public String getInternalStatusText() {
        return internalStatusText;
    }

    /**
     * @param internalStatusText
     *            the internalStatusText to set
     */
    public void setInternalStatusText(String internalStatusText) {
        this.internalStatusText = internalStatusText;
    }

    /**
     * @return the externalStatusText
     */
    public String getExternalStatusText() {
        return externalStatusText;
    }

    /**
     * @param externalStatusText
     *            the externalStatusText to set Detail
     */
    public void setExternalStatusText(String externalStatusText) {
        this.externalStatusText = externalStatusText;
    }

    /**
     * @return the assetNoList
     */
    public List<MstAssetVo> getAssetNoList() {
        return assetNoList;
    }

    /**
     * @param assetNoList
     *            the assetNoList to set
     */
    public void setAssetNoList(List<MstAssetVo> assetNoList) {
        this.assetNoList = assetNoList;
    }

    public String getControlTitleStr() {
        return controlTitleStr;
    }

    public void setControlTitleStr(String controlTitleStr) {
        this.controlTitleStr = controlTitleStr;
    }

    public String getControlButtonStr() {
        return controlButtonStr;
    }

    public void setControlButtonStr(String controlButtonStr) {
        this.controlButtonStr = controlButtonStr;
    }

    public String getControlSearchConditionStr() {
        return controlSearchConditionStr;
    }

    public void setControlSearchConditionStr(String controlSearchConditionStr) {
        this.controlSearchConditionStr = controlSearchConditionStr;
    }

    public String getControlDetailItemStr() {
        return controlDetailItemStr;
    }

    public void setControlDetailItemStr(String controlDetailItemStr) {
        this.controlDetailItemStr = controlDetailItemStr;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    /**
     * @return the receiveUserUuid
     */
    public String getReceiveUserUuid() {
        return receiveUserUuid;
    }

    /**
     * @param receiveUserUuid
     *            the receiveUserUuid to set
     */
    public void setReceiveUserUuid(String receiveUserUuid) {
        this.receiveUserUuid = receiveUserUuid;
    }

    /**
     * @return the assetMgmtConfirmUserUuid
     */
    public String getAssetMgmtConfirmUserUuid() {
        return assetMgmtConfirmUserUuid;
    }

    /**
     * @param assetMgmtConfirmUserUuid
     *            the assetMgmtConfirmUserUuid to set
     */
    public void setAssetMgmtConfirmUserUuid(String assetMgmtConfirmUserUuid) {
        this.assetMgmtConfirmUserUuid = assetMgmtConfirmUserUuid;
    }

    /**
     * @return the apConfirmUserUuid
     */
    public String getApConfirmUserUuid() {
        return apConfirmUserUuid;
    }

    /**
     * @param apConfirmUserUuid
     *            the apConfirmUserUuid to set
     */
    public void setApConfirmUserUuid(String apConfirmUserUuid) {
        this.apConfirmUserUuid = apConfirmUserUuid;
    }

    /**
     * @return the finalReplyUserUuid
     */
    public String getFinalReplyUserUuid() {
        return finalReplyUserUuid;
    }

    /**
     * @param finalReplyUserUuid
     *            the finalReplyUserUuid to set
     */
    public void setFinalReplyUserUuid(String finalReplyUserUuid) {
        this.finalReplyUserUuid = finalReplyUserUuid;
    }

    /**
     * @return the receiveUserName
     */
    public String getReceiveUserName() {
        return receiveUserName;
    }

    /**
     * @param receiveUserName
     *            the receiveUserName to set
     */
    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    /**
     * @return the assetMgmtConfirmUserName
     */
    public String getAssetMgmtConfirmUserName() {
        return assetMgmtConfirmUserName;
    }

    /**
     * @param assetMgmtConfirmUserName
     *            the assetMgmtConfirmUserName to set
     */
    public void setAssetMgmtConfirmUserName(String assetMgmtConfirmUserName) {
        this.assetMgmtConfirmUserName = assetMgmtConfirmUserName;
    }

    /**
     * @return the apConfirmUserName
     */
    public String getApConfirmUserName() {
        return apConfirmUserName;
    }

    /**
     * @param apConfirmUserName
     *            the apConfirmUserName to set
     */
    public void setApConfirmUserName(String apConfirmUserName) {
        this.apConfirmUserName = apConfirmUserName;
    }

    /**
     * @return the finalReplyUserName
     */
    public String getFinalReplyUserName() {
        return finalReplyUserName;
    }

    /**
     * @param finalReplyUserName
     *            the finalReplyUserName to set
     */
    public void setFinalReplyUserName(String finalReplyUserName) {
        this.finalReplyUserName = finalReplyUserName;
    }

    public String getFromCompanyName() {
        return fromCompanyName;
    }

    public void setFromCompanyName(String fromCompanyName) {
        this.fromCompanyName = fromCompanyName;
    }

    public String getToCompanyName() {
        return toCompanyName;
    }

    public void setToCompanyName(String toCompanyName) {
        this.toCompanyName = toCompanyName;
    }

    public int getMgmtRegion() {
        return mgmtRegion;
    }

    public void setMgmtRegion(int mgmtRegion) {
        this.mgmtRegion = mgmtRegion;
    }

    public String getMgmtRegionText() {
        return mgmtRegionText;
    }

    public void setMgmtRegionText(String mgmtRegionText) {
        this.mgmtRegionText = mgmtRegionText;
    }

    public String getMgmtCompanyCode() {
        return mgmtCompanyCode;
    }

    public void setMgmtCompanyCode(String mgmtCompanyCode) {
        this.mgmtCompanyCode = mgmtCompanyCode;
    }

    public String getMgmtCompanyName() {
        return mgmtCompanyName;
    }

    public void setMgmtCompanyName(String mgmtCompanyName) {
        this.mgmtCompanyName = mgmtCompanyName;
    }

    public String getMgmtLocationCode() {
        return mgmtLocationCode;
    }

    public void setMgmtLocationCode(String mgmtLocationCode) {
        this.mgmtLocationCode = mgmtLocationCode;
    }

    public String getMgmtLocationName() {
        return mgmtLocationName;
    }

    public void setMgmtLocationName(String mgmtLocationName) {
        this.mgmtLocationName = mgmtLocationName;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public int getAcquisitionType() {
        return acquisitionType;
    }

    public void setAcquisitionType(int acquisitionType) {
        this.acquisitionType = acquisitionType;
    }

    public String getAcquisitionTypeText() {
        return acquisitionTypeText;
    }

    public void setAcquisitionTypeText(String acquisitionTypeText) {
        this.acquisitionTypeText = acquisitionTypeText;
    }

    public String getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(String assetClass) {
        this.assetClass = assetClass;
    }

    public String getUsingSection() {
        return usingSection;
    }

    public void setUsingSection(String usingSection) {
        this.usingSection = usingSection;
    }

    public Date getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(Date acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getPeriodBookValue() {
        return periodBookValue;
    }

    public void setPeriodBookValue(String periodBookValue) {
        this.periodBookValue = periodBookValue;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getAssetMgmtConfirmDateStr() {
        return assetMgmtConfirmDateStr;
    }

    public void setAssetMgmtConfirmDateStr(String assetMgmtConfirmDateStr) {
        this.assetMgmtConfirmDateStr = assetMgmtConfirmDateStr;
    }

    public String getApConfirmDateStr() {
        return apConfirmDateStr;
    }

    public void setApConfirmDateStr(String apConfirmDateStr) {
        this.apConfirmDateStr = apConfirmDateStr;
    }

    public String getFinalReplyDateStr() {
        return finalReplyDateStr;
    }

    public void setFinalReplyDateStr(String finalReplyDateStr) {
        this.finalReplyDateStr = finalReplyDateStr;
    }

    public String getDocRequestDateStr() {
        return docRequestDateStr;
    }

    public void setDocRequestDateStr(String docRequestDateStr) {
        this.docRequestDateStr = docRequestDateStr;
    }

    public String getDocApprovalDateStr() {
        return docApprovalDateStr;
    }

    public void setDocApprovalDateStr(String docApprovalDateStr) {
        this.docApprovalDateStr = docApprovalDateStr;
    }

    public String getDisposalReportSentDateStr() {
        return disposalReportSentDateStr;
    }

    public void setDisposalReportSentDateStr(String disposalReportSentDateStr) {
        this.disposalReportSentDateStr = disposalReportSentDateStr;
    }

    public String getDisposalReportReceiptDateStr() {
        return disposalReportReceiptDateStr;
    }

    public void setDisposalReportReceiptDateStr(String disposalReportReceiptDateStr) {
        this.disposalReportReceiptDateStr = disposalReportReceiptDateStr;
    }

    public String getDisposalProcessingCompletionDateStr() {
        return disposalProcessingCompletionDateStr;
    }

    public void setDisposalProcessingCompletionDateStr(String disposalProcessingCompletionDateStr) {
        this.disposalProcessingCompletionDateStr = disposalProcessingCompletionDateStr;
    }

    public String getAcquisitionDateStr() {
        return acquisitionDateStr;
    }

    public void setAcquisitionDateStr(String acquisitionDateStr) {
        this.acquisitionDateStr = acquisitionDateStr;
    }

    public String getRequestDateStr() {
        return requestDateStr;
    }

    public void setRequestDateStr(String requestDateStr) {
        this.requestDateStr = requestDateStr;
    }

    /**
     * @return the disposalJudgment
     */
    public int getDisposalJudgment() {
        return disposalJudgment;
    }

    /**
     * @param disposalJudgment the disposalJudgment to set
     */
    public void setDisposalJudgment(int disposalJudgment) {
        this.disposalJudgment = disposalJudgment;
    }

    /**
     * @return the disposalJudgmentText
     */
    public String getDisposalJudgmentText() {
        return disposalJudgmentText;
    }

    /**
     * @param disposalJudgmentText the disposalJudgmentText to set
     */
    public void setDisposalJudgmentText(String disposalJudgmentText) {
        this.disposalJudgmentText = disposalJudgmentText;
    }

    public String getReceiveDateStr() {
        return receiveDateStr;
    }

    public void setReceiveDateStr(String receiveDateStr) {
        this.receiveDateStr = receiveDateStr;
    }

}
