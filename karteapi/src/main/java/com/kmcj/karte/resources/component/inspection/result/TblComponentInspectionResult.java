/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result;

import com.google.gson.Gson;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectLang;
import com.kmcj.karte.resources.component.inspection.item.MstComponentInspectionItemsTable;
import com.kmcj.karte.resources.component.inspection.item.model.MstComponentInspectionItemsTableClass;
import com.kmcj.karte.util.XmlDateAdapter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

/**
 * @author duanlin
 */
@Entity
@Table(name = "tbl_component_inspection_result")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblComponentInspectionResult.findAll", query = "SELECT t FROM TblComponentInspectionResult t"),
    @NamedQuery(name = "TblComponentInspectionResult.findById", query = "SELECT t FROM TblComponentInspectionResult t WHERE t.id = :id"),
    @NamedQuery(name = "TblComponentInspectionResult.findLatestByProductionLotNum", query = "SELECT t FROM TblComponentInspectionResult t WHERE t.productionLotNum IN :productionLotNums ORDER BY t.createDate ASC"),
    @NamedQuery(name = "TblComponentInspectionResult.findByLotNum", query = "SELECT t FROM TblComponentInspectionResult t WHERE t.componentId = :componentId AND t.productionLotNum = :productionLotNum AND t.outgoingApproveResult = :outgoingApproveResult order by t.createDate desc"),
    @NamedQuery(name = "TblComponentInspectionResult.findByAbortFlg", query = "SELECT t FROM TblComponentInspectionResult t WHERE t.incomingCompanyId = :incomingCompanyId and t.abortFlg = :abortFlg"),
    @NamedQuery(name = "TblComponentInspectionResult.findByIds", query = "SELECT t FROM TblComponentInspectionResult t WHERE t.id in :ids")
})

public class TblComponentInspectionResult implements Serializable {

    /**
     * tbl_component_inspection_result.FILE_CONFIRM_STATUSの顔ぶれ定義
     */
    public static class FileConfirmStatus {
        /** 未確認*/
        public static final String DEFAULT = "01_DEFAULT";
        /** 確認*/
        public static final String CONFIRMED = "03_CONFIRMED";
        /** 否認*/
        public static final String DENIED = "02_DENIED";
    }
    
    /** 検査確認ステータス*/
    public enum ConfirmResult {
        /** 未確認*/
        UNTREATED,
        /** 確認済*/
        CONFIRMED,
        /** 否認*/
        DENIED
    }
    
    /** 検査承認結果*/
    public enum ApproveResult {
        /** 未確認*/
        UNTREATED,
        /** 承認済*/
        APPROVED,
        /** 否認*/
        DENIED
    }
    
    public enum AbortFlg {
        /** 初期状態。*/
        RUNNING,
        /** 中止中。<br>サプライヤ側で中止され、拠点側に反映されていない状態*/
        ABORTING,
        /** 中止。<br>中止状態が拠点側に反映された状態*/
        ABORTED
    }

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_INSPECTION_ITEMS_TABLE_ID")
    private String componentInspectionItemsTableId;
    @Size(min = 1, max = 45)
    @NotNull
    @Column(name = "FIRST_FLAG")
    private String firstFlag;
    @Basic(optional = false)
    @Size(min = 1, max = 45)
    @Column(name = "INSP_CLASS_DICT_KEY")
    private String inspClassDictKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INSPECT_PTN")
    private Character inspectPtn;
    @Size(min = 1, max = 45)
    @Column(name = "PO_NUMBER")
    private String poNumber;
    @Size(min = 1, max = 100)
    @Column(name = "PRODUCTION_LOT_NUM")
    private String productionLotNum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "OUTGOING_COMPANY_ID")
    private String outgoingCompanyId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INCOMING_COMPANY_ID")
    private String incomingCompanyId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private Integer quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INSPECTION_STATUS")
    private Integer inspectionStatus;
    @Column(name = "OUTGOING_MEAS_SAMPLING_QUANTITY")
    private Integer outgoingMeasSamplingQuantity;
    @Column(name = "OUTGOING_VISUAL_SAMPLING_QUANTITY")
    private Integer outgoingVisualSamplingQuantity;
    @Size(max = 45)
    @Column(name = "OUTGOING_INSPECTION_PERSON_UUID")
    private String outgoingInspectionPersonUuid;
    @Size(max = 45)
    @Column(name = "OUTGOING_INSPECTION_PERSON_NAME")
    private String outgoingInspectionPersonName;
    @Column(name = "OUTGOING_INSPECTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date outgoingInspectionDate;
    @Column(name = "OUTGOING_INSPECTION_RESULT")
    private Integer outgoingInspectionResult;
    @Size(max = 45)
    @Column(name = "OUTGOING_INSPECTION_APPROVE_PERSON_UUID")
    private String outgoingInspectionApprovePersonUuid;
    @Size(max = 45)
    @Column(name = "OUTGOING_INSPECTION_APPROVE_PERSON_NAME")
    private String outgoingInspectionApprovePersonName;
    @Column(name = "OUTGOING_INSPECTION_APPROVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date outgoingInspectionApproveDate;
    @Size(max = 500)
    @Column(name = "OUTGOING_INSPECTION_COMMENT")
    private String outgoingInspectionComment;
    @Column(name = "INCOMING_MEAS_SAMPLING_QUANTITY")
    private Integer incomingMeasSamplingQuantity;
    @Column(name = "INCOMING_VISUAL_SAMPLING_QUANTITY")
    private Integer incomingVisualSamplingQuantity;
    @Size(max = 45)
    @Column(name = "INCOMING_INSPECTION_PERSON_UUID")
    private String incomingInspectionPersonUuid;
    @Size(max = 45)
    @Column(name = "INCOMING_INSPECTION_PERSON_NAME")
    private String incomingInspectionPersonName;
    @Column(name = "INCOMING_INSPECTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date incomingInspectionDate;
    @Column(name = "INCOMING_INSPECTION_RESULT")
    private Integer incomingInspectionResult;
    @Size(max = 45)
    @Column(name = "INCOMING_INSPECTION_APPROVE_PERSON_UUID")
    private String incomingInspectionApprovePersonUuid;
    @Size(max = 45)
    @Column(name = "INCOMING_INSPECTION_APPROVE_PERSON_NAME")
    private String incomingInspectionApprovePersonName;
    @Column(name = "INCOMING_INSPECTION_APPROVE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date incomingInspectionApproveDate;
    @Size(max = 500)
    @Column(name = "INCOMING_INSPECTION_COMMENT")
    private String incomingInspectionComment;
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    @Column(name = "INCOMING_INSPECTION_WEEK")
    private Date incomingInspectionWeek;
    @Column(name = "INCOMING_INSPECTION_MONTH")
    private Integer incomingInspectionMonth;
    @Size(max = 45)
    @Column(name = "ACCEPTANCE_PERSON_UUID")
    private String acceptancePersonUuid;
    @Size(max = 45)
    @Column(name = "ACCEPTANCE_PERSON_NAME")
    private String acceptancePersonName;
    @Column(name = "ACCEPTANCE_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date acceptanceDate;
    @Size(max = 45)
    @Column(name = "EXEMPTION_APPROVE_PERSON_UUID")
    private String exemptionApprovePersonUuid;
    @Size(max = 45)
    @Column(name = "EXEMPTION_APPROVE_PERSON_NAME")
    private String exemptionApprovePersonName;
    @Column(name = "EXEMPTION_APPROVE_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date exemptionApproveDate;
    @Size(max = 2)
    @Column(name = "INSP_BATCH_UPDATE_STATUS")
    private String inspBatchUpdateStatus;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    @Size(max = 45)
    @Column(name = "FILE_CONFIRM_STATUS")
    private String fileConfirmStatus = "01_DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_APPROVER_ID")
    private String fileApproverId;
    @Size(max = 45)
    @Column(name = "FILE_APPROVER_NAME")
    private String fileApproverName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private MstComponent mstComponent;

    @ManyToOne(optional = false)
    @JoinColumn(name = "INCOMING_COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private MstCompany mstCompanyIncoming;

    @ManyToOne(optional = false)
    @JoinColumn(name = "OUTGOING_COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private MstCompany mstCompanyOutgoing;

    @ManyToOne(optional = false)
    @JoinColumns({
        @JoinColumn(name = "COMPONENT_INSPECTION_ITEMS_TABLE_ID", referencedColumnName = "COMPONENT_INSPECTION_ITEMS_TABLE_ID", insertable = false, updatable = false),
        @JoinColumn(name = "FIRST_FLAG", referencedColumnName = "COMPONENT_INSPECT_CLASS_ID", insertable = false, updatable = false)
    })
    private MstComponentInspectionItemsTableClass mstComponentInspItemsTblClass;
    
    @PrimaryKeyJoinColumn(name = "INSP_CLASS_DICT_KEY", referencedColumnName = "DICT_KEY")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponentInspectLang classLang;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "COMPONENT_INSPECTION_ITEMS_TABLE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private MstComponentInspectionItemsTable mstTable;

    //-------------------Apeng 20171010 add start---------------------------
    @Size(max = 500)
    @Column(name = "OUTGOING_PRIVATE_COMMENT")
    private String outgoingPrivateComment; //出荷検査コメント(非公開)
    @Size(max = 45)
    @Column(name = "OUTGOING_CONFIRMER_UUID")
    private String outgoingConfirmerUuid; //出荷検査確認者UUID
    @Size(max = 45)
    @Column(name = "OUTGOING_CONFIRMER_NAME")
    private String outgoingConfirmerName; //出荷検査確認者NAME
    @Column(name = "OUTGOING_CONFIRM_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date outgoingConfirmDate; //出荷検査確認日
    @Size(max = 500)
    @Column(name = "INCOMING_PRIVATE_COMMENT")
    private String incomingPrivateComment; //入荷検査コメント(非公開)
    @Size(max = 45)
    @Column(name = "INCOMING_CONFIRMER_UUID")
    private String incomingConfirmerUuid; //入荷検査確認者UUID
    @Size(max = 45)
    @Column(name = "INCOMING_CONFIRMER_NAME")
    private String incomingConfirmerName; //入荷検査確認者NAME
    @Column(name = "INCOMING_CONFIRM_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date incomingConfirmDate; //入荷検査確認日
    //-------------------Apeng 20171010 add end---------------------------

    //ACV start 20180510
    @Column(name = "OUTGOING_APPROVE_RESULT")
    @Enumerated(EnumType.STRING)
    private ApproveResult outgoingApproveResult = ApproveResult.UNTREATED;
    @Column(name = "OUTGOING_COMFIRM_RESULT")
    @Enumerated(EnumType.STRING)
    private ConfirmResult outgoingConfirmResult = ConfirmResult.UNTREATED;
    @Column(name = "INCOMING_APPROVE_RESULT")
    @Enumerated(EnumType.STRING)
    private ApproveResult incomingApproveResult = ApproveResult.UNTREATED;
    @Column(name = "INCOMING_CONFIRM_RESULT")
    @Enumerated(EnumType.STRING)
    private ConfirmResult incomingConfirmResult = ConfirmResult.UNTREATED;
    //ACV end 20180510

    //ACV start 20180608
    @Size(max = 100)
    @Column(name = "MATERIAL_01")
    private String material01 = "";
    @Size(max = 100)
    @Column(name = "MATERIAL_02")
    private String material02 = "";
    @Size(max = 100)
    @Column(name = "MATERIAL_03")
    private String material03 = "";
    //ACV end 20180608

    //Apeng 20180131 add start
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATA_RELATION_TGT")
    private Character dataRelationTgt;//データ連携フラグ
    //Apeng 20180131 add end
    
    @Column(name = "ABORT_FLG")
    @Enumerated(EnumType.STRING)
    private AbortFlg abortFlg = AbortFlg.RUNNING;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "CAVITY_CNT")
    private Integer cavityCnt;//CAV数量
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "CAVITY_START_NUM")
    private Integer cavityStartNum;//CAV開始番号
    
    @Size(max = 10)
    @Column(name = "CAVITY_PREFIX")
    private String cavityPrefix;//CAVプレフィックス

    public TblComponentInspectionResult() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComponentInspectionItemsTableId() {
        return componentInspectionItemsTableId;
    }

    public void setComponentInspectionItemsTableId(String componentInspectionItemsTableId) {
        this.componentInspectionItemsTableId = componentInspectionItemsTableId;
    }

    public String getFirstFlag() {
        return firstFlag;
    }

    public void setFirstFlag(String firstFlag) {
        this.firstFlag = firstFlag;
    }

    public String getInspClassDictKey() {
        return inspClassDictKey;
    }

    public void setInspClassDictKey(String inspClassDictKey) {
        this.inspClassDictKey = inspClassDictKey;
    }

    public Character getInspectPtn() {
        return inspectPtn;
    }

    public void setInspectPtn(Character inspectPtn) {
        this.inspectPtn = inspectPtn;
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

    public String getOutgoingInspectionPersonUuid() {
        return outgoingInspectionPersonUuid;
    }

    public void setOutgoingInspectionPersonUuid(String outgoingInspectionPersonUuid) {
        this.outgoingInspectionPersonUuid = outgoingInspectionPersonUuid;
    }

    public String getOutgoingInspectionPersonName() {
        return outgoingInspectionPersonName;
    }

    public void setOutgoingInspectionPersonName(String outgoingInspectionPersonName) {
        this.outgoingInspectionPersonName = outgoingInspectionPersonName;
    }

    public Date getOutgoingInspectionDate() {
        return outgoingInspectionDate;
    }

    public void setOutgoingInspectionDate(Date outgoingInspectionDate) {
        this.outgoingInspectionDate = outgoingInspectionDate;
    }

    public Integer getOutgoingInspectionResult() {
        return outgoingInspectionResult;
    }

    public void setOutgoingInspectionResult(Integer outgoingInspectionResult) {
        this.outgoingInspectionResult = outgoingInspectionResult;
    }

    public String getOutgoingInspectionApprovePersonUuid() {
        return outgoingInspectionApprovePersonUuid;
    }

    public void setOutgoingInspectionApprovePersonUuid(String outgoingInspectionApprovePersonUuid) {
        this.outgoingInspectionApprovePersonUuid = outgoingInspectionApprovePersonUuid;
    }

    public String getOutgoingInspectionApprovePersonName() {
        return outgoingInspectionApprovePersonName;
    }

    public void setOutgoingInspectionApprovePersonName(String outgoingInspectionApprovePersonName) {
        this.outgoingInspectionApprovePersonName = outgoingInspectionApprovePersonName;
    }

    public Date getOutgoingInspectionApproveDate() {
        return outgoingInspectionApproveDate;
    }

    public void setOutgoingInspectionApproveDate(Date outgoingInspectionApproveDate) {
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

    public String getIncomingInspectionPersonUuid() {
        return incomingInspectionPersonUuid;
    }

    public void setIncomingInspectionPersonUuid(String incomingInspectionPersonUuid) {
        this.incomingInspectionPersonUuid = incomingInspectionPersonUuid;
    }

    public String getIncomingInspectionPersonName() {
        return incomingInspectionPersonName;
    }

    public void setIncomingInspectionPersonName(String incomingInspectionPersonName) {
        this.incomingInspectionPersonName = incomingInspectionPersonName;
    }

    public Date getIncomingInspectionDate() {
        return incomingInspectionDate;
    }

    public void setIncomingInspectionDate(Date incomingInspectionDate) {
        this.incomingInspectionDate = incomingInspectionDate;
    }

    public Integer getIncomingInspectionResult() {
        return incomingInspectionResult;
    }

    public void setIncomingInspectionResult(Integer incomingInspectionResult) {
        this.incomingInspectionResult = incomingInspectionResult;
    }

    public String getIncomingInspectionApprovePersonUuid() {
        return incomingInspectionApprovePersonUuid;
    }

    public void setIncomingInspectionApprovePersonUuid(String incomingInspectionApprovePersonUuid) {
        this.incomingInspectionApprovePersonUuid = incomingInspectionApprovePersonUuid;
    }

    public String getIncomingInspectionApprovePersonName() {
        return incomingInspectionApprovePersonName;
    }

    public void setIncomingInspectionApprovePersonName(String incomingInspectionApprovePersonName) {
        this.incomingInspectionApprovePersonName = incomingInspectionApprovePersonName;
    }

    public Date getIncomingInspectionApproveDate() {
        return incomingInspectionApproveDate;
    }

    public void setIncomingInspectionApproveDate(Date incomingInspectionApproveDate) {
        this.incomingInspectionApproveDate = incomingInspectionApproveDate;
    }

    public String getIncomingInspectionComment() {
        return incomingInspectionComment;
    }

    public void setIncomingInspectionComment(String incomingInspectionComment) {
        this.incomingInspectionComment = incomingInspectionComment;
    }
    
    public Date getIncomingInspectionWeek() {
        return incomingInspectionWeek;
    }

    public void setIncomingInspectionWeek(Date incomingInspectionWeek) {
        this.incomingInspectionWeek = incomingInspectionWeek;
    }
    
    public Integer getIncomingInspectionMonth() {
        return incomingInspectionMonth;
    }

    public void setIncomingInspectionMonth(Integer incomingInspectionMonth) {
        this.incomingInspectionMonth = incomingInspectionMonth;
    }

    public String getAcceptancePersonUuid() {
        return acceptancePersonUuid;
    }

    public void setAcceptancePersonUuid(String acceptancePersonUuid) {
        this.acceptancePersonUuid = acceptancePersonUuid;
    }

    public String getAcceptancePersonName() {
        return acceptancePersonName;
    }

    public void setAcceptancePersonName(String acceptancePersonName) {
        this.acceptancePersonName = acceptancePersonName;
    }

    public Date getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(Date acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public String getExemptionApprovePersonUuid() {
        return exemptionApprovePersonUuid;
    }

    public void setExemptionApprovePersonUuid(String exemptionApprovePersonUuid) {
        this.exemptionApprovePersonUuid = exemptionApprovePersonUuid;
    }

    public String getExemptionApprovePersonName() {
        return exemptionApprovePersonName;
    }

    public void setExemptionApprovePersonName(String exemptionApprovePersonName) {
        this.exemptionApprovePersonName = exemptionApprovePersonName;
    }

    public Date getExemptionApproveDate() {
        return exemptionApproveDate;
    }

    public void setExemptionApproveDate(Date exemptionApproveDate) {
        this.exemptionApproveDate = exemptionApproveDate;
    }

    public String getInspBatchUpdateStatus() {
        return inspBatchUpdateStatus;
    }

    public void setInspBatchUpdateStatus(String inspBatchUpdateStatus) {
        this.inspBatchUpdateStatus = inspBatchUpdateStatus;
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

    public MstComponent getMstComponent() {
        return mstComponent;
    }

    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    public MstCompany getMstCompanyIncoming() {
        return mstCompanyIncoming;
    }

    public void setMstCompanyIncoming(MstCompany mstCompanyIncoming) {
        this.mstCompanyIncoming = mstCompanyIncoming;
    }

    public MstCompany getMstCompanyOutgoing() {
        return mstCompanyOutgoing;
    }

    public void setMstCompanyOutgoing(MstCompany mstCompanyOutgoing) {
        this.mstCompanyOutgoing = mstCompanyOutgoing;
    }

    public MstComponentInspectionItemsTableClass getMstComponentInspItemsTblClass() {
        return mstComponentInspItemsTblClass;
    }

    public void setMstComponentInspItemsTblClass(MstComponentInspectionItemsTableClass mstComponentInspItemsTblClass) {
        this.mstComponentInspItemsTblClass = mstComponentInspItemsTblClass;
    }

    public MstComponentInspectLang getClassLang() {
        return classLang;
    }

    public void setClassLang(MstComponentInspectLang classLang) {
        this.classLang = classLang;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblComponentInspectionResult)) {
            return false;
        }
        TblComponentInspectionResult other = (TblComponentInspectionResult) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
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
     * @return the outgoingConfirmDate
     */
    public Date getOutgoingConfirmDate() {
        return outgoingConfirmDate;
    }

    /**
     * @param outgoingConfirmDate the outgoingConfirmDate to set
     */
    public void setOutgoingConfirmDate(Date outgoingConfirmDate) {
        this.outgoingConfirmDate = outgoingConfirmDate;
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
     * @return the incomingConfirmDate
     */
    public Date getIncomingConfirmDate() {
        return incomingConfirmDate;
    }

    /**
     * @param incomingConfirmDate the incomingConfirmDate to set
     */
    public void setIncomingConfirmDate(Date incomingConfirmDate) {
        this.incomingConfirmDate = incomingConfirmDate;
    }

    /**
     * @return the dataRelationTgt
     */
    public Character getDataRelationTgt() {
        return dataRelationTgt;
    }

    /**
     * @param dataRelationTgt the dataRelationTgt to set
     */
    public void setDataRelationTgt(Character dataRelationTgt) {
        this.dataRelationTgt = dataRelationTgt;
    }

    public void setFileConfirmStatus(String fileConfirmStatus) {
        this.fileConfirmStatus = fileConfirmStatus;
    }

    public String getFileConfirmStatus() {
        return fileConfirmStatus;
    }

    public ApproveResult getOutgoingApproveResult() {
        return outgoingApproveResult;
    }

    public void setOutgoingApproveResult(ApproveResult outgoingApproveResult) {
        this.outgoingApproveResult = outgoingApproveResult;
    }

    public ConfirmResult getOutgoingConfirmResult() {
        return outgoingConfirmResult;
    }

    public void setOutgoingConfirmResult(ConfirmResult outgoingConfirmResult) {
        this.outgoingConfirmResult = outgoingConfirmResult;
    }

    public ApproveResult getIncomingApproveResult() {
        return incomingApproveResult;
    }

    public void setIncomingApproveResult(ApproveResult incomingApproveResult) {
        this.incomingApproveResult = incomingApproveResult;
    }

    public ConfirmResult getIncomingConfirmResult() {
        return incomingConfirmResult;
    }

    public void setIncomingConfirmResult(ConfirmResult incomingConfirmResult) {
        this.incomingConfirmResult = incomingConfirmResult;
    }

    public MstComponentInspectionItemsTable getMstTable() {
        return mstTable;
    }

    public void setMstTable(MstComponentInspectionItemsTable mstTable) {
        this.mstTable = mstTable;
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

    public AbortFlg getAbortFlg() {
        return abortFlg;
    }

    public void setAbortFlg(AbortFlg abortFlg) {
        this.abortFlg = abortFlg;
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
