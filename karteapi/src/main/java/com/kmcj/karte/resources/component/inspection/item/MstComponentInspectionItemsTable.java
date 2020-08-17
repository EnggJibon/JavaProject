/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item;

import com.google.gson.Gson;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectLang;
import com.kmcj.karte.util.XmlDateAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author duanlin
 */
@Entity
@Table(name = "mst_component_inspection_items_table")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "MstComponentInspectionItemsTable.findAll", query = "SELECT m FROM MstComponentInspectionItemsTable m"),
        @NamedQuery(name = "MstComponentInspectionItemsTable.findById", query = "SELECT m FROM MstComponentInspectionItemsTable m WHERE m.id = :id"),
        @NamedQuery(name = "MstComponentInspectionItemsTable.findByComponentId", query = "SELECT m FROM MstComponentInspectionItemsTable m WHERE m.componentId = :componentId"),
        @NamedQuery(name = "MstComponentInspectionItemsTable.findByOutgoingCompanyId", query = "SELECT m FROM MstComponentInspectionItemsTable m WHERE m.outgoingCompanyId = :outgoingCompanyId"),
        @NamedQuery(name = "MstComponentInspectionItemsTable.findByIncomingCompanyId", query = "SELECT m FROM MstComponentInspectionItemsTable m WHERE m.incomingCompanyId = :incomingCompanyId"),
        @NamedQuery(name = "MstComponentInspectionItemsTable.findByinspectTypeId", query = "SELECT m FROM MstComponentInspectionItemsTable m WHERE m.inspectTypeId = :inspectTypeId"),
        @NamedQuery(name = "MstComponentInspectionItemsTable.findByCondition", query = "SELECT m FROM MstComponentInspectionItemsTable m WHERE m.componentId = :componentId AND m.outgoingCompanyId = :outgoingCompanyId AND m.incomingCompanyId = :incomingCompanyId AND m.version = :version"),
        @NamedQuery(name = "MstComponentInspectionItemsTable.updateApplyEndDateById", query = "UPDATE MstComponentInspectionItemsTable m SET "
                + "m.applyEndDate = :applyEndDate,"
                + "m.updateDate = :updateDate,"
                + "m.updateUserUuid = :updateUserUuid  WHERE m.id = :id")
})
public class MstComponentInspectionItemsTable implements Serializable {

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
    @Column(name = "VERSION")
    private Integer version;
    @Size(max = 45)
    @Column(name = "ENTRY_PERSON_UUID")
    private String entryPersonUuid;
    @Size(max = 45)
    @Column(name = "ENTRY_PERSON_NAME")
    private String entryPersonName;
     @Size(max = 500)
    @Column(name = "ITEM_APPROVE_COMMENT")
    private String itemApproveComment;
    @Size(max = 45)
    @Column(name = "APPROVE_PERSON_UUID")
    private String approvePersonUuid;
    @Size(max = 45)
    @Column(name = "APPROVE_PERSON_NAME")
    private String approvePersonName;
    @Column(name = "ITEM_APPROVE_STATUS")
    private Integer itemApproveStatus;
    @Column(name = "APPROVE_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date approveDate;
    @Column(name = "APPLY_START_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date applyStartDate;
    @Column(name = "APPLY_END_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date applyEndDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date updateDate;
    @Size(max = 2)
    @Column(name = "INSP_BATCH_UPDATE_STATUS")
    private String inspBatchUpdateStatus;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    
    @Size(max = 45)
    @Column(name = "INSPECT_TYPE_ID")
    private String inspectTypeId;//Apeng 20171023 add
    
    @Size(max = 45)
    @Column(name = "INSPECT_TYPE_DICT_KEY")
    private String inspectTypeDictKey;//Apeng 20171023 add
    
    @Column(name = "MEAS_SAMPLE_RATIO")
    private BigDecimal measSampleRatio;
    
    @Column(name = "VIS_SAMPLE_RATIO")
    private BigDecimal visSampleRatio;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private MstComponent mstComponent;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "INCOMING_COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private MstCompany mstCompanyIncoming;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "OUTGOING_COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private MstCompany mstCompanyOutgoing;
    
    /*
     * 辞書マスタ
     */
    @PrimaryKeyJoinColumn(name = "INSPECT_TYPE_DICT_KEY", referencedColumnName = "DICT_KEY")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponentInspectLang mstComponentInspectLang;
    
    public MstComponentInspectionItemsTable() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getEntryPersonUuid() {
        return entryPersonUuid;
    }

    public void setEntryPersonUuid(String entryPersonUuid) {
        this.entryPersonUuid = entryPersonUuid;
    }

    public String getEntryPersonName() {
        return entryPersonName;
    }

    public void setEntryPersonName(String entryPersonName) {
        this.entryPersonName = entryPersonName;
    }

    public String getApprovePersonUuid() {
        return approvePersonUuid;
    }

    public void setApprovePersonUuid(String approvePersonUuid) {
        this.approvePersonUuid = approvePersonUuid;
    }

    public String getApprovePersonName() {
        return approvePersonName;
    }

    public void setApprovePersonName(String approvePersonName) {
        this.approvePersonName = approvePersonName;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public Date getApplyStartDate() {
        return applyStartDate;
    }

    public void setApplyStartDate(Date applyStartDate) {
        this.applyStartDate = applyStartDate;
    }

    public Date getApplyEndDate() {
        return applyEndDate;
    }

    public void setApplyEndDate(Date applyEndDate) {
        this.applyEndDate = applyEndDate;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstComponentInspectionItemsTable)) {
            return false;
        }
        MstComponentInspectionItemsTable other = (MstComponentInspectionItemsTable) object;
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
     * @return the inspectTypeId
     */
    public String getInspectTypeId() {
        return inspectTypeId;
    }

    /**
     * @param inspectTypeId the inspectTypeId to set
     */
    public void setInspectTypeId(String inspectTypeId) {
        this.inspectTypeId = inspectTypeId;
    }

    /**
     * @return the inspectTypeDictKey
     */
    public String getInspectTypeDictKey() {
        return inspectTypeDictKey;
    }

    /**
     * @param inspectTypeDictKey the inspectTypeDictKey to set
     */
    public void setInspectTypeDictKey(String inspectTypeDictKey) {
        this.inspectTypeDictKey = inspectTypeDictKey;
    }

    public BigDecimal getMeasSampleRatio() {
        return measSampleRatio;
    }

    public void setMeasSampleRatio(BigDecimal measSampleRatio) {
        this.measSampleRatio = measSampleRatio;
    }

    public BigDecimal getVisSampleRatio() {
        return visSampleRatio;
    }

    public void setVisSampleRatio(BigDecimal visSampleRatio) {
        this.visSampleRatio = visSampleRatio;
    }

    /**
     * @return the mstComponentInspectLang
     */
    public MstComponentInspectLang getMstComponentInspectLang() {
        return mstComponentInspectLang;
    }

    /**
     * @param mstComponentInspectLang the mstComponentInspectLang to set
     */
    public void setMstComponentInspectLang(MstComponentInspectLang mstComponentInspectLang) {
        this.mstComponentInspectLang = mstComponentInspectLang;
    }

    public String getItemApproveComment() {
        return itemApproveComment;
    }

    public Integer getItemApproveStatus() {
        return itemApproveStatus;
    }

    public void setItemApproveComment(String itemApproveComment) {
        this.itemApproveComment = itemApproveComment;
    }

    public void setItemApproveStatus(Integer itemApproveStatus) {
        this.itemApproveStatus = itemApproveStatus;
    }
    
}
