/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item;

import com.google.gson.Gson;
import com.kmcj.karte.util.XmlDateAdapter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "mst_component_inspection_items_table_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstComponentInspectionItemsTableDetail.findById", query = "SELECT m FROM MstComponentInspectionItemsTableDetail m WHERE m.id = :id"),
    @NamedQuery(name = "MstComponentInspectionItemsTableDetail.findByComponentInspectionItemsTableId", query = "SELECT m FROM MstComponentInspectionItemsTableDetail m WHERE m.componentInspectionItemsTableId = :componentInspectionItemsTableId"),
    @NamedQuery(name = "MstComponentInspectionItemsTableDetail.deleteMstComponentInspectionItemsTableDetail",
            query = "DELETE FROM MstComponentInspectionItemsTableDetail t"
                    + " WHERE t.componentInspectionItemsTableId = :componentInspectionItemsTableId AND t.additionalFlg = :additionalFlg")})
public class MstComponentInspectionItemsTableDetail implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "INSPECTION_ITEM_SNO")
    private String inspectionItemSno;

    @Column(name = "REVISION_SYMBOL")
    private String revisionSymbol;
    @Column(name = "DRAWING_PAGE")
    private String drawingPage;
    @Column(name = "DRAWING_ANNOTATION")
    private String drawingAnnotation;
    @Column(name = "DRAWING_MENTION_NO")
    private String drawingMentionNo;
    @Column(name = "SIMILAR_MULTIITEM")
    private String similarMultiitem;
    @Column(name = "DRAWING_AREA")
    private String drawingArea;
    @Column(name = "PQS")
    private String pqs;
    @Column(name = "INSPECTION_ITEM_NAME")
    private String inspectionItemName;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "MEASUREMENT_TYPE")
    private Integer measurementType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "MEASUREMENT_METHOD")
    private String measurementMethod;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "DIMENSION_VALUE")
    private BigDecimal dimensionValue;
    @Column(name = "TOLERANCE_PLUS")
    private BigDecimal tolerancePlus;
    @Column(name = "TOLERANCE_MINUS")
    private BigDecimal toleranceMinus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "OUTGOING_TRIAL_INSPECTION_OBJECT")
    private Character outgoingTrialInspectionObject;
    @Basic(optional = false)
    @NotNull
    @Column(name = "OUTGOING_PRODUCTION_INSPECTION_OBJECT")
    private Character outgoingProductionInspectionObject;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INCOMING_TRIAL_INSPECTION_OBJECT")
    private Character incomingTrialInspectionObject;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INCOMING_PRODUCTION_INSPECTION_OBJECT")
    private Character incomingProductionInspectionObject;
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
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "LOCAL_SEQ")
    private int localSeq;
    //Apeng 20180130 add start
    @Basic(optional = false)
    @NotNull
    @Column(name = "PROCESS_INSPECTION_OBJECT")
    private Character processInspetionObject;//工程内検査対象
    @Basic(optional = false)
    @NotNull
    @Column(name = "ADDITIONAL_FLG")
    private Character additionalFlg;
    @Column(name = "ENABLE_TH_ALERT")
    private boolean enableThAlert = true;
    //Apeng 20180130 add end
    @Basic(optional = false)
    @NotNull
    @Column(name = "OUTGOING_FIRST_MASS_PRODUCTION_OBJECT")
    private Character outgoingFirstMassProductionObject;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INCOMING_FIRST_MASS_PRODUCTION_OBJECT")
    private Character incomingFirstMassProductionObject;

    public MstComponentInspectionItemsTableDetail() {
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

    public String getInspectionItemSno() {
        return inspectionItemSno;
    }

    public void setInspectionItemSno(String inspectionItemSno) {
        this.inspectionItemSno = inspectionItemSno;
    }

    public String getRevisionSymbol() {
        return revisionSymbol;
    }

    public void setRevisionSymbol(String revisionSymbol) {
        this.revisionSymbol = revisionSymbol;
    }

    public String getDrawingPage() {
        return drawingPage;
    }

    public void setDrawingPage(String drawingPage) {
        this.drawingPage = drawingPage;
    }

    public String getDrawingAnnotation() {
        return drawingAnnotation;
    }

    public void setDrawingAnnotation(String drawingAnnotation) {
        this.drawingAnnotation = drawingAnnotation;
    }

    public String getDrawingMentionNo() {
        return drawingMentionNo;
    }

    public void setDrawingMentionNo(String drawingMentionNo) {
        this.drawingMentionNo = drawingMentionNo;
    }

    public String getSimilarMultiitem() {
        return similarMultiitem;
    }

    public void setSimilarMultiitem(String similarMultiitem) {
        this.similarMultiitem = similarMultiitem;
    }

    public String getDrawingArea() {
        return drawingArea;
    }

    public void setDrawingArea(String drawingArea) {
        this.drawingArea = drawingArea;
    }

    public String getPqs() {
        return pqs;
    }

    public void setPqs(String pqs) {
        this.pqs = pqs;
    }

    public String getInspectionItemName() {
        return inspectionItemName;
    }

    public void setInspectionItemName(String inspectionItemName) {
        this.inspectionItemName = inspectionItemName;
    }

    public Integer getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(Integer measurementType) {
        this.measurementType = measurementType;
    }

    public String getMeasurementMethod() {
        return measurementMethod;
    }

    public void setMeasurementMethod(String measurementMethod) {
        this.measurementMethod = measurementMethod;
    }

    public BigDecimal getDimensionValue() {
        return dimensionValue;
    }

    public void setDimensionValue(BigDecimal dimensionValue) {
        this.dimensionValue = dimensionValue;
    }

    public BigDecimal getTolerancePlus() {
        return tolerancePlus;
    }

    public void setTolerancePlus(BigDecimal tolerancePlus) {
        this.tolerancePlus = tolerancePlus;
    }

    public BigDecimal getToleranceMinus() {
        return toleranceMinus;
    }

    public void setToleranceMinus(BigDecimal toleranceMinus) {
        this.toleranceMinus = toleranceMinus;
    }

    public Character getOutgoingTrialInspectionObject() {
        return outgoingTrialInspectionObject;
    }

    public void setOutgoingTrialInspectionObject(Character outgoingTrialInspectionObject) {
        this.outgoingTrialInspectionObject = outgoingTrialInspectionObject;
    }

    public Character getOutgoingProductionInspectionObject() {
        return outgoingProductionInspectionObject;
    }

    public void setOutgoingProductionInspectionObject(Character outgoingProductionInspectionObject) {
        this.outgoingProductionInspectionObject = outgoingProductionInspectionObject;
    }

    public Character getIncomingTrialInspectionObject() {
        return incomingTrialInspectionObject;
    }

    public void setIncomingTrialInspectionObject(Character incomingTrialInspectionObject) {
        this.incomingTrialInspectionObject = incomingTrialInspectionObject;
    }

    public Character getIncomingProductionInspectionObject() {
        return incomingProductionInspectionObject;
    }

    public void setIncomingProductionInspectionObject(Character incomingProductionInspectionObject) {
        this.incomingProductionInspectionObject = incomingProductionInspectionObject;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstComponentInspectionItemsTableDetail)) {
            return false;
        }
        MstComponentInspectionItemsTableDetail other = (MstComponentInspectionItemsTableDetail) object;
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
     * @return the localSeq
     */
    public int getLocalSeq() {
        return localSeq;
    }

    /**
     * @param localSeq the localSeq to set
     */
    public void setLocalSeq(int localSeq) {
        this.localSeq = localSeq;
    }

    /**
     * @return the processInspetionObject
     */
    public Character getProcessInspetionObject() {
        return processInspetionObject;
    }

    /**
     * @param processInspetionObject the processInspetionObject to set
     */
    public void setProcessInspetionObject(Character processInspetionObject) {
        this.processInspetionObject = processInspetionObject;
    }

    /**
     * @return the additionalFlg
     */
    public Character getAdditionalFlg() {
        return additionalFlg;
    }

    /**
     * @param additionalFlg the additionalFlg to set
     */
    public void setAdditionalFlg(Character additionalFlg) {
        this.additionalFlg = additionalFlg;
    }

    public boolean isEnableThAlert() {
        return enableThAlert;
    }

    public void setEnableThAlert(boolean enableThAlert) {
        this.enableThAlert = enableThAlert;
    }

    public Character getOutgoingFirstMassProductionObject() {
        return outgoingFirstMassProductionObject;
    }

    public void setOutgoingFirstMassProductionObject(Character outgoingFirstMassProductionObject) {
        this.outgoingFirstMassProductionObject = outgoingFirstMassProductionObject;
    }

    public Character getIncomingFirstMassProductionObject() {
        return incomingFirstMassProductionObject;
    }

    public void setIncomingFirstMassProductionObject(Character incomingFirstMassProductionObject) {
        this.incomingFirstMassProductionObject = incomingFirstMassProductionObject;
    }

}
