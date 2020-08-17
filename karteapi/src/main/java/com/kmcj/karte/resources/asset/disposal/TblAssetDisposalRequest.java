/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.disposal;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.kmcj.karte.resources.asset.MstAsset;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.user.MstUser;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_asset_disposal_request")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "TblAssetDisposalRequest.findAll", query = "SELECT t FROM TblAssetDisposalRequest t"),
        @NamedQuery(name = "TblAssetDisposalRequest.findByRequestNoAndToCompanyId", query = "SELECT t FROM TblAssetDisposalRequest t WHERE t.requestNo = :requestNo AND t.toCompanyId = :toCompanyId "),
        @NamedQuery(name = "TblAssetDisposalRequest.findExternalStatusIsUnsent", query = "SELECT assetDisposalRequest FROM TblAssetDisposalRequest assetDisposalRequest "
                + "JOIN MstApiUser apiUser ON assetDisposalRequest.toCompanyId = apiUser.companyId" // 申請先会社とPAIユーザ同じの会社
                + " WHERE assetDisposalRequest.externalStatus = :externalStatus AND apiUser.userId = :apiUserId"),
        @NamedQuery(name = "TblAssetDisposalRequest.findByUuid", query = "SELECT t FROM TblAssetDisposalRequest t WHERE t.uuid = :uuid") })
@Cacheable(value = false)
public class TblAssetDisposalRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Column(name = "REQUEST_DATE")
    @Temporal(TemporalType.DATE)
    private Date requestDate;
    @Size(max = 45)
    @Column(name = "REQUEST_NO")
    private String requestNo;
    @Size(max = 45)
    @Column(name = "FROM_COMPANY_ID")
    private String fromCompanyId;
    @Size(max = 45)
    @Column(name = "TO_COMPANY_ID")
    private String toCompanyId;
    @Size(max = 100)
    @Column(name = "REQUEST_USER_NAME")
    private String requestUserName;
    @Size(max = 100)
    @Column(name = "REQUEST_MAIL_ADDRESS")
    private String requestMailAddress;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MOLD_MACHINE_TYPE")
    private int moldMachineType;
    @Size(max = 100)
    @Column(name = "ITEM_CODE")
    private String itemCode;
    @Size(max = 100)
    @Column(name = "ITEM_NAME")
    private String itemName;
    @Size(max = 100)
    @Column(name = "ITEM_CODE2")
    private String itemCode2;
    @Size(max = 100)
    @Column(name = "ITEM_NAME2")
    private String itemName2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXISTENCE")
    private int existence;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DISPOSAL_REQUEST_REASON")
    private int disposalRequestReason;

    @Column(name = "DISPOSAL_REQUEST_REASON_OTHER")
    private String disposalRequestReasonOther;

    @Size(max = 45)
    @Column(name = "ASSET_NO")
    private String assetNo;
    @Size(max = 45)
    @Column(name = "BRANCH_NO")
    private String branchNo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EOL_CONFIRMATION")
    private int eolConfirmation;
    @NotNull
    @Column(name = "DISPOSAL_JUDGMENT")
    private int disposalJudgment;
    @Size(max = 256)
    @Column(name = "JUDGMENT_REASON")
    private String judgmentReason;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RECEIVE_REJECT_REASON")
    private int receiveRejectReason;
    @Column(name = "RECEIVE_DATE")
    @Temporal(TemporalType.DATE)
    private Date receiveDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ITEM_VER_CONFIRMATION")
    private int itemVerConfirmation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "OEM_DESTINATION")
    private int oemDestination;
    @Size(max = 45)
    @Column(name = "OEM_ASSET_NO")
    private String oemAssetNo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ASSET_MGMT_CONFIRM")
    private int assetMgmtConfirm;
    @Column(name = "ASSET_MGMT_CONFIRM_DATE")
    @Temporal(TemporalType.DATE)
    private Date assetMgmtConfirmDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AP_DISPOSAL_JUDGMENT")
    private int apDisposalJudgment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AP_SUPPLY_REMAINING_MONTH")
    private int apSupplyRemainingMonth;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AP_FINAL_BULK_ORDER")
    private int apFinalBulkOrder;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AP_REJECT_REASON")
    private int apRejectReason;
    @Column(name = "AP_CONFIRM_DATE")
    @Temporal(TemporalType.DATE)
    private Date apConfirmDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FINAL_REPLY")
    private int finalReply;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FINAL_REJECT_REASON")
    private int finalRejectReason;
    @Column(name = "FINAL_REPLY_DATE")
    @Temporal(TemporalType.DATE)
    private Date finalReplyDate;
    @Column(name = "DOC_REQUEST_DATE")
    @Temporal(TemporalType.DATE)
    private Date docRequestDate;
    @Column(name = "DOC_APPROVAL_DATE")
    @Temporal(TemporalType.DATE)
    private Date docApprovalDate;
    @Column(name = "DISPOSAL_REPORT_SENT_DATE")
    @Temporal(TemporalType.DATE)
    private Date disposalReportSentDate;
    @Column(name = "DISPOSAL_REPORT_RECEIPT_DATE")
    @Temporal(TemporalType.DATE)
    private Date disposalReportReceiptDate;
    @Column(name = "DISPOSAL_PROCESSING_COMPLETION_DATE")
    @Temporal(TemporalType.DATE)
    private Date disposalProcessingCompletionDate;
    @Size(max = 256)
    @Column(name = "REMARKS")
    private String remarks;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INTERNAL_STATUS")
    private int internalStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EXTERNAL_STATUS")
    private int externalStatus;
    @Basic(optional = false)
    @Column(name = "REJECT_REASON_TEXT")
    private String rejectReasonText;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    @Column(name = "RECEIVE_USER_UUID")
    private String receiveUserUuid;
    @Column(name = "ASSET_MGMT_CONFIRM_USER_UUID")
    private String assetMgmtConfirmUserUuid;
    @Column(name = "AP_CONFIRM_USER_UUID")
    private String apConfirmUserUuid;
    @Column(name = "FINAL_REPLY_USER_UUID")
    private String finalReplyUserUuid;
    @Column(name = "EXPORT_FLG")
    private int exportFlg;
    
    @Column(name = "MAIL_SENT_REGISTRATION")
    private int mailSentRegistration;
    @Column(name = "MAIL_SENT_RECEPTION")
    private int mailSentReception;
    @Column(name = "MAIL_SENT_CONFIRMATION")
    private int mailSentConfirmation;
    @Column(name = "MAIL_SENT_AP_CONFIRMATION")
    private int mailSentApConfirmation;
    @Column(name = "MAIL_SENT_FINAL_CONFIRMATION")
    private int mailSentFinalConfirmation;
    
    
    public TblAssetDisposalRequest() {
    }

    public TblAssetDisposalRequest(String uuid) {
        this.uuid = uuid;
    }

    /**
     * 結合テーブル定義
     */
    // ユーザーマスタ(受付者Join)
    @PrimaryKeyJoinColumn(name = "RECEIVE_USER_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser receiveMstUser;

    // ユーザーマスタ(確認者Join)
    @PrimaryKeyJoinColumn(name = "ASSET_MGMT_CONFIRM_USER_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser assetMgmtConfirmMstUser;

    // ユーザーマスタ(AP確認者Join)
    @PrimaryKeyJoinColumn(name = "AP_CONFIRM_USER_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser apConfirmMstUser;

    // ユーザーマスタ(AP確認者Join)
    @PrimaryKeyJoinColumn(name = "FINAL_REPLY_USER_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser finalReplyMstUser;

    // 申請者会社マスタ
    @PrimaryKeyJoinColumn(name = "FROM_COMPANY_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany fromMstCompany;

    // 申請先会社マスタ
    @PrimaryKeyJoinColumn(name = "TO_COMPANY_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany toMstCompany;

    // 資産マスタ
    @JoinColumns({
            @JoinColumn(name = "ASSET_NO", referencedColumnName = "ASSET_NO", insertable = false, updatable = false),
            @JoinColumn(name = "BRANCH_NO", referencedColumnName = "BRANCH_NO", insertable = false, updatable = false) })
    @ManyToOne(fetch = FetchType.LAZY)
    private MstAsset mstAsset;

    public TblAssetDisposalRequest(String uuid, int moldMachineType, int existence, int disposalRequestReason,
            int eolConfirmation, int receiveRejectReason, int itemVerConfirmation, int oemDestination,
            int assetMgmtConfirm, int apDisposalJudgment, int apSupplyRemainingMonth, int apFinalBulkOrder,
            int apRejectReason, int finalReply, int finalRejectReason, int internalStatus, int externalStatus,
            String rejectReasonText) {
        this.uuid = uuid;
        this.moldMachineType = moldMachineType;
        this.existence = existence;
        this.disposalRequestReason = disposalRequestReason;
        this.eolConfirmation = eolConfirmation;
        this.receiveRejectReason = receiveRejectReason;
        this.itemVerConfirmation = itemVerConfirmation;
        this.oemDestination = oemDestination;
        this.assetMgmtConfirm = assetMgmtConfirm;
        this.apDisposalJudgment = apDisposalJudgment;
        this.apSupplyRemainingMonth = apSupplyRemainingMonth;
        this.apFinalBulkOrder = apFinalBulkOrder;
        this.apRejectReason = apRejectReason;
        this.finalReply = finalReply;
        this.finalRejectReason = finalRejectReason;
        this.internalStatus = internalStatus;
        this.externalStatus = externalStatus;
        this.rejectReasonText = rejectReasonText;
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

    public MstCompany getFromMstCompany() {
        return fromMstCompany;
    }

    public void setFromMstCompany(MstCompany fromMstCompany) {
        this.fromMstCompany = fromMstCompany;
    }

    public MstCompany getToMstCompany() {
        return toMstCompany;
    }

    public void setToMstCompany(MstCompany toMstCompany) {
        this.toMstCompany = toMstCompany;
    }

    public MstAsset getMstAsset() {
        return mstAsset;
    }

    public void setMstAsset(MstAsset mstAsset) {
        this.mstAsset = mstAsset;
    }

    public MstUser getReceiveMstUser() {
        return receiveMstUser;
    }

    public void setReceiveMstUser(MstUser receiveMstUser) {
        this.receiveMstUser = receiveMstUser;
    }

    public MstUser getAssetMgmtConfirmMstUser() {
        return assetMgmtConfirmMstUser;
    }

    public void setAssetMgmtConfirmMstUser(MstUser assetMgmtConfirmMstUser) {
        this.assetMgmtConfirmMstUser = assetMgmtConfirmMstUser;
    }

    public MstUser getApConfirmMstUser() {
        return apConfirmMstUser;
    }

    public void setApConfirmMstUser(MstUser apConfirmMstUser) {
        this.apConfirmMstUser = apConfirmMstUser;
    }

    public MstUser getFinalReplyMstUser() {
        return finalReplyMstUser;
    }

    public void setFinalReplyMstUser(MstUser finalReplyMstUser) {
        this.finalReplyMstUser = finalReplyMstUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof TblAssetDisposalRequest)) {
            return false;
        }
        TblAssetDisposalRequest other = (TblAssetDisposalRequest) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.asset.disposal.TblAssetDisposalRequest[ uuid=" + uuid + " ]";
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
     * @return the exportFlg
     */
    public int getExportFlg() {
        return exportFlg;
    }

    /**
     * @param exportFlg the exportFlg to set
     */
    public void setExportFlg(int exportFlg) {
        this.exportFlg = exportFlg;
    }

    /**
     * @return the mailSentRegistration
     */
    public int getMailSentRegistration() {
        return mailSentRegistration;
    }

    /**
     * @param mailSentRegistration the mailSentRegistration to set
     */
    public void setMailSentRegistration(int mailSentRegistration) {
        this.mailSentRegistration = mailSentRegistration;
    }

    /**
     * @return the mailSentReception
     */
    public int getMailSentReception() {
        return mailSentReception;
    }

    /**
     * @param mailSentReception the mailSentReception to set
     */
    public void setMailSentReception(int mailSentReception) {
        this.mailSentReception = mailSentReception;
    }

    /**
     * @return the mailSentConfirmation
     */
    public int getMailSentConfirmation() {
        return mailSentConfirmation;
    }

    /**
     * @param mailSentConfirmation the mailSentConfirmation to set
     */
    public void setMailSentConfirmation(int mailSentConfirmation) {
        this.mailSentConfirmation = mailSentConfirmation;
    }

    /**
     * @return the mailSentApConfirmation
     */
    public int getMailSentApConfirmation() {
        return mailSentApConfirmation;
    }

    /**
     * @param mailSentApConfirmation the mailSentApConfirmation to set
     */
    public void setMailSentApConfirmation(int mailSentApConfirmation) {
        this.mailSentApConfirmation = mailSentApConfirmation;
    }

    /**
     * @return the mailSentFinalConfirmation
     */
    public int getMailSentFinalConfirmation() {
        return mailSentFinalConfirmation;
    }

    /**
     * @param mailSentFinalConfirmation the mailSentFinalConfirmation to set
     */
    public void setMailSentFinalConfirmation(int mailSentFinalConfirmation) {
        this.mailSentFinalConfirmation = mailSentFinalConfirmation;
    }


}
