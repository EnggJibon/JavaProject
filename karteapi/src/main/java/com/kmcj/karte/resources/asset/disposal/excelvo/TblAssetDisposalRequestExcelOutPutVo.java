/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.disposal.excelvo;

import com.kmcj.karte.excelhandle.annotation.Cell;
import com.kmcj.karte.excelhandle.annotation.Excel;
import com.kmcj.karte.excelhandle.annotation.Select;

/**
 * 廃棄機能エクセルデータ出力用
 *
 * @author admin
 */
@Excel(isNeedSequence = false)
public class TblAssetDisposalRequestExcelOutPutVo {

    @Cell(columnNum = "1")
    private String requestNo; //申請番号

    @Cell(columnNum = "2")
    private String requestDate;//申請日

    @Cell(columnNum = "3")
    private String internalStatusText;//ステータス

    @Cell(columnNum = "4")
    private String fromCompanyName;//会社名称

    @Cell(columnNum = "5")
    private String requestUserName;//申請者氏名

    @Cell(columnNum = "6")
    private String moldMachineTypeText;//金型・設備区分

    @Cell(columnNum = "7")
    private String itemCode;//品目コード

    @Cell(columnNum = "8")
    private String itemName;//品目名称

    @Cell(columnNum = "9")
    private String itemCode2;//品目コード2

    @Cell(columnNum = "10")
    private String itemName2;//品目名称2

    @Cell(columnNum = "11")
    private String existenceText;//現物有無

    @Cell(columnNum = "12")
    private String disposalRequestReasonText;//廃棄理由

    @Cell(columnNum = "13")
    private String assetNo;//資産番号

    @Cell(columnNum = "14")
    private String branchNo;//補助番号

    @Cell(columnNum = "15")
    private String mgmtRegion;//管理地域

    @Cell(columnNum = "16")
    private String mgmtCompanyCode;//管理先コード

    @Cell(columnNum = "17")
    private String mgmtCompanyName;//管理先名称

    @Cell(columnNum = "18")
    private String mgmtLocationName;//設置場所名称

    @Cell(columnNum = "19")
    private String vendorCode;//ベンダーコード

    @Cell(columnNum = "20")
    private String acquisitionTypeText;//取得区分

    @Cell(columnNum = "21")
    private String eolConfirmationText;//EOL確認
    
    @Cell(columnNum = "21")
    @Select
    private String[] eolConfirmationArray;//EOL確認`リスト

    @Cell(columnNum = "22")
    private String disposalJudgmentText;//廃棄可否判断
    
    @Cell(columnNum = "22")
    @Select
    private String[] disposalJudgmentArray;//廃棄可否判断

    @Cell(columnNum = "23")
    private String judgmentReason;//廃棄可否判断理由

    @Cell(columnNum = "24")
    private String receiveRejectReasonText;//受付却下理由
    
    @Cell(columnNum = "24")
    @Select
    private String[] receiveRejectReasonArray;//受付却下理由

    @Cell(columnNum = "25")
    private String receiveDateStr;//受付日

    @Cell(columnNum = "26")
    private String receiveUserName;//受付者

    @Cell(columnNum = "27")
    private String assetClass;//資産クラス

    @Cell(columnNum = "28")
    private String itemVerConfirmationText;//品目コードVer確認
    
    @Cell(columnNum = "28")
    @Select
    private String[] itemVerConfirmationArray;//品目コードVer確認

    @Cell(columnNum = "29")
    private String oemDestinationText;//OEM先
    
    @Cell(columnNum = "29")
    @Select
    private String[] oemDestinationArray;//OEM先

    @Cell(columnNum = "30")
    private String oemAssetNo;//OEM資産番号

    @Cell(columnNum = "31")
    private String usingSection;//使用部門

    @Cell(columnNum = "32")
    private String acquisitionDateStr;//取得日

    @Cell(columnNum = "33")
    private String periodBookValue;//期初簿価

    @Cell(columnNum = "34")
    private String costCenter;//原価センター

    @Cell(columnNum = "35")
    private String assetMgmtConfirmText;//資産管理部門確認
    
    @Cell(columnNum = "35")
    @Select
    private String[] assetMgmtConfirmArray;//資産管理部門確認

    @Cell(columnNum = "36")
    private String assetMgmtConfirmDateStr;//確認日

    @Cell(columnNum = "37")
    private String assetMgmtConfirmUserName;//確認者

    @Cell(columnNum = "38")
    private String apDisposalJudgmentText;//AP廃棄可否判断
    
    @Cell(columnNum = "38")
    @Select
    private String[] apDisposalJudgmentArray;//AP廃棄可否判断

    @Cell(columnNum = "39")
    private String apRejectReasonText;//AP却下理由
    
    @Cell(columnNum = "39")
    @Select
    private String[] apRejectReasonArray;//AP却下理由

    @Cell(columnNum = "40")
    private String apConfirmDateStr;//AP確認日

    @Cell(columnNum = "41")
    private String apConfirmUserName;//AP確認者

    @Cell(columnNum = "42")
    private String apSupplyRemainingMonth;//AP供給期限残月数

    @Cell(columnNum = "43")
    private String apFinalBulkOrderText;//AP最終まとめ発注
    
    @Cell(columnNum = "43")
    @Select
    private String[] apFinalBulkOrderArray;//AP最終まとめ発注

    @Cell(columnNum = "44")
    private String finalReplyText;//最終回答
    
    @Cell(columnNum = "44")
    @Select
    private String[] finalReplyArray;//最終回答

    @Cell(columnNum = "45")
    private String finalRejectReasonText;//最終却下理由
    
    @Cell(columnNum = "45")
    @Select
    private String[] finalRejectReasonArray;//最終却下理由

    @Cell(columnNum = "46")
    private String finalReplyDateStr;//最終回答日

    @Cell(columnNum = "47")
    private String finalReplyUserName;//最終回答者

    @Cell(columnNum = "48")
    private String docRequestDateStr;//稟議書申請日

    @Cell(columnNum = "49")
    private String docApprovalDateStr;//稟議書承認日

    @Cell(columnNum = "50")
    private String disposalReportSentDateStr;//廃棄報告書送付日

    @Cell(columnNum = "51")
    private String disposalReportReceiptDateStr;//廃棄報告書受領日

    @Cell(columnNum = "52")
    private String disposalProcessingCompletionDateStr;//廃棄処理完了日

    @Cell(columnNum = "53")
    private String remarks;//備考

    public TblAssetDisposalRequestExcelOutPutVo() {
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getInternalStatusText() {
        return internalStatusText;
    }

    public void setInternalStatusText(String internalStatusText) {
        this.internalStatusText = internalStatusText;
    }

    public String getFromCompanyName() {
        return fromCompanyName;
    }

    public void setFromCompanyName(String fromCompanyName) {
        this.fromCompanyName = fromCompanyName;
    }

    public String getRequestUserName() {
        return requestUserName;
    }

    public void setRequestUserName(String requestUserName) {
        this.requestUserName = requestUserName;
    }

    public String getMoldMachineTypeText() {
        return moldMachineTypeText;
    }

    public void setMoldMachineTypeText(String moldMachineTypeText) {
        this.moldMachineTypeText = moldMachineTypeText;
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

    public String getExistenceText() {
        return existenceText;
    }

    public void setExistenceText(String existenceText) {
        this.existenceText = existenceText;
    }

    public String getDisposalRequestReasonText() {
        return disposalRequestReasonText;
    }

    public void setDisposalRequestReasonText(String disposalRequestReasonText) {
        this.disposalRequestReasonText = disposalRequestReasonText;
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

    public String getMgmtRegion() {
        return mgmtRegion;
    }

    public void setMgmtRegion(String mgmtRegion) {
        this.mgmtRegion = mgmtRegion;
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

    public String getAcquisitionTypeText() {
        return acquisitionTypeText;
    }

    public void setAcquisitionTypeText(String acquisitionTypeText) {
        this.acquisitionTypeText = acquisitionTypeText;
    }

    public String getEolConfirmationText() {
        return eolConfirmationText;
    }

    public void setEolConfirmationText(String eolConfirmationText) {
        this.eolConfirmationText = eolConfirmationText;
    }

    public String[] getEolConfirmationArray() {
        return eolConfirmationArray;
    }

    public void setEolConfirmationArray(String[] eolConfirmationArray) {
        this.eolConfirmationArray = eolConfirmationArray;
    }

    public String getDisposalJudgmentText() {
        return disposalJudgmentText;
    }

    public void setDisposalJudgmentText(String disposalJudgmentText) {
        this.disposalJudgmentText = disposalJudgmentText;
    }

    public String[] getDisposalJudgmentArray() {
        return disposalJudgmentArray;
    }

    public void setDisposalJudgmentArray(String[] disposalJudgmentArray) {
        this.disposalJudgmentArray = disposalJudgmentArray;
    }

    public String getJudgmentReason() {
        return judgmentReason;
    }

    public void setJudgmentReason(String judgmentReason) {
        this.judgmentReason = judgmentReason;
    }

    public String getReceiveRejectReasonText() {
        return receiveRejectReasonText;
    }

    public void setReceiveRejectReasonText(String receiveRejectReasonText) {
        this.receiveRejectReasonText = receiveRejectReasonText;
    }

    public String[] getReceiveRejectReasonArray() {
        return receiveRejectReasonArray;
    }

    public void setReceiveRejectReasonArray(String[] receiveRejectReasonArray) {
        this.receiveRejectReasonArray = receiveRejectReasonArray;
    }

    public String getReceiveDateStr() {
        return receiveDateStr;
    }

    public void setReceiveDateStr(String receiveDateStr) {
        this.receiveDateStr = receiveDateStr;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(String assetClass) {
        this.assetClass = assetClass;
    }

    public String getItemVerConfirmationText() {
        return itemVerConfirmationText;
    }

    public void setItemVerConfirmationText(String itemVerConfirmationText) {
        this.itemVerConfirmationText = itemVerConfirmationText;
    }

    public String[] getItemVerConfirmationArray() {
        return itemVerConfirmationArray;
    }

    public void setItemVerConfirmationArray(String[] itemVerConfirmationArray) {
        this.itemVerConfirmationArray = itemVerConfirmationArray;
    }

    public String getOemDestinationText() {
        return oemDestinationText;
    }

    public void setOemDestinationText(String oemDestinationText) {
        this.oemDestinationText = oemDestinationText;
    }

    public String[] getOemDestinationArray() {
        return oemDestinationArray;
    }

    public void setOemDestinationArray(String[] oemDestinationArray) {
        this.oemDestinationArray = oemDestinationArray;
    }

    public String getOemAssetNo() {
        return oemAssetNo;
    }

    public void setOemAssetNo(String oemAssetNo) {
        this.oemAssetNo = oemAssetNo;
    }

    public String getUsingSection() {
        return usingSection;
    }

    public void setUsingSection(String usingSection) {
        this.usingSection = usingSection;
    }

    public String getAcquisitionDateStr() {
        return acquisitionDateStr;
    }

    public void setAcquisitionDateStr(String acquisitionDateStr) {
        this.acquisitionDateStr = acquisitionDateStr;
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

    public String getAssetMgmtConfirmText() {
        return assetMgmtConfirmText;
    }

    public void setAssetMgmtConfirmText(String assetMgmtConfirmText) {
        this.assetMgmtConfirmText = assetMgmtConfirmText;
    }

    public String[] getAssetMgmtConfirmArray() {
        return assetMgmtConfirmArray;
    }

    public void setAssetMgmtConfirmArray(String[] assetMgmtConfirmArray) {
        this.assetMgmtConfirmArray = assetMgmtConfirmArray;
    }

    public String getAssetMgmtConfirmDateStr() {
        return assetMgmtConfirmDateStr;
    }

    public void setAssetMgmtConfirmDateStr(String assetMgmtConfirmDateStr) {
        this.assetMgmtConfirmDateStr = assetMgmtConfirmDateStr;
    }

    public String getAssetMgmtConfirmUserName() {
        return assetMgmtConfirmUserName;
    }

    public void setAssetMgmtConfirmUserName(String assetMgmtConfirmUserName) {
        this.assetMgmtConfirmUserName = assetMgmtConfirmUserName;
    }

    public String getApDisposalJudgmentText() {
        return apDisposalJudgmentText;
    }

    public void setApDisposalJudgmentText(String apDisposalJudgmentText) {
        this.apDisposalJudgmentText = apDisposalJudgmentText;
    }

    public String[] getApDisposalJudgmentArray() {
        return apDisposalJudgmentArray;
    }

    public void setApDisposalJudgmentArray(String[] apDisposalJudgmentArray) {
        this.apDisposalJudgmentArray = apDisposalJudgmentArray;
    }

    public String getApRejectReasonText() {
        return apRejectReasonText;
    }

    public void setApRejectReasonText(String apRejectReasonText) {
        this.apRejectReasonText = apRejectReasonText;
    }

    public String[] getApRejectReasonArray() {
        return apRejectReasonArray;
    }

    public void setApRejectReasonArray(String[] apRejectReasonArray) {
        this.apRejectReasonArray = apRejectReasonArray;
    }

    public String getApConfirmDateStr() {
        return apConfirmDateStr;
    }

    public void setApConfirmDateStr(String apConfirmDateStr) {
        this.apConfirmDateStr = apConfirmDateStr;
    }

    public String getApConfirmUserName() {
        return apConfirmUserName;
    }

    public void setApConfirmUserName(String apConfirmUserName) {
        this.apConfirmUserName = apConfirmUserName;
    }

    public String getApSupplyRemainingMonth() {
        return apSupplyRemainingMonth;
    }

    public void setApSupplyRemainingMonth(String apSupplyRemainingMonth) {
        this.apSupplyRemainingMonth = apSupplyRemainingMonth;
    }

    public String getApFinalBulkOrderText() {
        return apFinalBulkOrderText;
    }

    public void setApFinalBulkOrderText(String apFinalBulkOrderText) {
        this.apFinalBulkOrderText = apFinalBulkOrderText;
    }

    public String[] getApFinalBulkOrderArray() {
        return apFinalBulkOrderArray;
    }

    public void setApFinalBulkOrderArray(String[] apFinalBulkOrderArray) {
        this.apFinalBulkOrderArray = apFinalBulkOrderArray;
    }

    public String getFinalReplyText() {
        return finalReplyText;
    }

    public void setFinalReplyText(String finalReplyText) {
        this.finalReplyText = finalReplyText;
    }

    public String[] getFinalReplyArray() {
        return finalReplyArray;
    }

    public void setFinalReplyArray(String[] finalReplyArray) {
        this.finalReplyArray = finalReplyArray;
    }

    public String getFinalRejectReasonText() {
        return finalRejectReasonText;
    }

    public void setFinalRejectReasonText(String finalRejectReasonText) {
        this.finalRejectReasonText = finalRejectReasonText;
    }

    public String[] getFinalRejectReasonArray() {
        return finalRejectReasonArray;
    }

    public void setFinalRejectReasonArray(String[] finalRejectReasonArray) {
        this.finalRejectReasonArray = finalRejectReasonArray;
    }

    public String getFinalReplyDateStr() {
        return finalReplyDateStr;
    }

    public void setFinalReplyDateStr(String finalReplyDateStr) {
        this.finalReplyDateStr = finalReplyDateStr;
    }

    public String getFinalReplyUserName() {
        return finalReplyUserName;
    }

    public void setFinalReplyUserName(String finalReplyUserName) {
        this.finalReplyUserName = finalReplyUserName;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
