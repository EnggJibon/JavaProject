/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result;

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
@Table(name = "tbl_component_inspection_result_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblComponentInspectionResultDetail.findAll", query = "SELECT t FROM TblComponentInspectionResultDetail t"),
    @NamedQuery(name = "TblComponentInspectionResultDetail.findById", query = "SELECT t FROM TblComponentInspectionResultDetail t WHERE t.id = :id"),
    @NamedQuery(name = "TblComponentInspectionResultDetail.findByComponentInspectionResultId", query = "SELECT t FROM TblComponentInspectionResultDetail t WHERE t.componentInspectionResultId = :componentInspectionResultId ORDER BY t.inspectionType, t.inspectionItemSno"),
    @NamedQuery(name = "TblComponentInspectionResultDetail.findByComponentInspectionResultIdAndInspectionTypeList",
            query = "SELECT t FROM TblComponentInspectionResultDetail t"
                    + " WHERE t.componentInspectionResultId = :componentInspectionResultId AND t.inspectionType IN :inspectionType"
                    + " ORDER BY t.inspectionType, t.inspectionItemSno, t.seq"),
    @NamedQuery(name = "TblComponentInspectionResultDetail.findByComponentInspectionResultIdAndInspectionType",
            query = "SELECT t FROM TblComponentInspectionResultDetail t"
                    + " WHERE t.componentInspectionResultId = :componentInspectionResultId AND t.inspectionType = :inspectionType"
                    + " ORDER BY t.inspectionType, t.inspectionItemSno, t.seq"),
    @NamedQuery(name = "TblComponentInspectionResultDetail.deleteByComponentInspectionResultIdAndInspectionType",
            query = "DELETE FROM TblComponentInspectionResultDetail t"
                    + " WHERE t.componentInspectionResultId = :componentInspectionResultId AND t.inspectionType = :inspectionType"),
    @NamedQuery(name = "TblComponentInspectionResultDetail.updateSeqVisualResult", 
            query = "UPDATE TblComponentInspectionResultDetail t SET t.seqVisualResult = :seqVisualResult, t.updateUserUuid = :updateUserUuid, t.updateDate = :updateDate WHERE t.id = :id"),
    @NamedQuery(name = "TblComponentInspectionResultDetail.updateItemResult", 
            query = "UPDATE TblComponentInspectionResultDetail t SET t.itemResult = :itemResult, t.updateUserUuid = :updateUserUuid, t.updateDate = :updateDate,t.itemResultAuto = :itemResultAuto WHERE t.id = :id")})
public class TblComponentInspectionResultDetail implements Serializable {

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
    @Column(name = "COMPONENT_INSPECTION_RESULT_ID")
    private String componentInspectionResultId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INSPECTION_TYPE")
    private Integer inspectionType;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "INSPECTION_ITEM_SNO")
    private String inspectionItemSno;
//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "MEASUREMENT_TYPE")
//    private Integer measurementType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private Integer seq;
    @Column(name = "ITEM_RESULT")
    private Integer itemResult;
    @Column(name = "SEQ_MEASUREMENT_RESULT")
    private BigDecimal seqMeasurementResult;
    @Column(name = "SEQ_VISUAL_RESULT")
    private Integer seqVisualResult;
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
    
    //2017.10.16
    @Column(name = "ITEM_RESULT_AUTO")
    private Integer itemResultAuto; //自動判定
    @Column(name = "ITEM_RESULT_MANUAL")
    private Integer itemResultManual; //手動判定
    @Size(max = 200)
    @Column(name = "MAN_JUDGE_COMMENT")
    private String manJudgeComment; //手動判定コメント
    @Size(max = 200)
    @Column(name = "NOTE")
    private String note; //注記
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "CAVITY_NUM")
    private Integer cavityNum;// CAV番号

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

    public TblComponentInspectionResultDetail() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getInspectionItemSno() {
        return inspectionItemSno;
    }

    public void setInspectionItemSno(String inspectionItemSno) {
        this.inspectionItemSno = inspectionItemSno;
    }

//    public Integer getMeasurementType() {
//        return measurementType;
//    }
//
//    public void setMeasurementType(Integer measurementType) {
//        this.measurementType = measurementType;
//    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getItemResult() {
        return itemResult;
    }

    public void setItemResult(Integer itemResult) {
        this.itemResult = itemResult;
    }

    public BigDecimal getSeqMeasurementResult() {
        return seqMeasurementResult;
    }

    public void setSeqMeasurementResult(BigDecimal seqMeasurementResult) {
        this.seqMeasurementResult = seqMeasurementResult;
    }

    public Integer getSeqVisualResult() {
        return seqVisualResult;
    }

    public void setSeqVisualResult(Integer seqVisualResult) {
        this.seqVisualResult = seqVisualResult;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getCavityNum() {
        return cavityNum;
    }

    public void setCavityNum(Integer cavityNum) {
        this.cavityNum = cavityNum;
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
        if (!(object instanceof TblComponentInspectionResultDetail)) {
            return false;
        }
        TblComponentInspectionResultDetail other = (TblComponentInspectionResultDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
    
}
