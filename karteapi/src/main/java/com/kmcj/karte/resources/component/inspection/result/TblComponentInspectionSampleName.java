/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result;

import java.io.Serializable;
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

import com.kmcj.karte.util.XmlDateAdapter;

/**
 *検査結果の取り数に設定された名称
 * 
 * @author Apeng
 */
@Entity
@Table(name = "tbl_component_inspection_sample_name")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblComponentInspectionSampleName.findAll", query = "SELECT t FROM TblComponentInspectionSampleName t"),
    @NamedQuery(name = "TblComponentInspectionSampleName.findById", query = "SELECT t FROM TblComponentInspectionSampleName t WHERE t.id = :id"),
    @NamedQuery(name = "TblComponentInspectionSampleName.findByComponentInspectionResultId", query = "SELECT t FROM TblComponentInspectionSampleName t WHERE t.componentInspectionResultId = :componentInspectionResultId"),
    @NamedQuery(name = "TblComponentInspectionSampleName.findByComponentInspectionResultIdAndInspectionType", query = "SELECT t FROM TblComponentInspectionSampleName t WHERE t.componentInspectionResultId = :componentInspectionResultId and t.inspectionType = :inspectionType"),
    @NamedQuery(name = "TblComponentInspectionSampleName.findByComponentInspectionResultIdAndInspectionTypes", query = "SELECT t FROM TblComponentInspectionSampleName t WHERE t.componentInspectionResultId = :componentInspectionResultId and t.inspectionType IN :inspectionType"),
})
public class TblComponentInspectionSampleName implements Serializable {

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
    @Column(name = "MEASUREMENT_TYPE")
    private Integer measurementType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "INSPECTION_TYPE")
    private int inspectionType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date createDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date updateDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "CAVITY_NUM")
    private Integer cavityNum;// CAV番号

    public TblComponentInspectionSampleName() {
    }

    public TblComponentInspectionSampleName(String id) {
        this.id = id;
    }

    public TblComponentInspectionSampleName(String id, String componentInspectionResultId, int inspectionType, int seq, String name, Date createDate, Date updateDate, String createUserUuid, String updateUserUuid) {
        this.id = id;
        this.componentInspectionResultId = componentInspectionResultId;
        this.inspectionType = inspectionType;
        this.measurementType = measurementType;
        this.seq = seq;
        this.name = name;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.createUserUuid = createUserUuid;
        this.updateUserUuid = updateUserUuid;
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

    public int getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(int inspectionType) {
        this.inspectionType = inspectionType;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof TblComponentInspectionSampleName)) {
            return false;
        }
        TblComponentInspectionSampleName other = (TblComponentInspectionSampleName) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionSampleName[ id=" + id + " ]";
    }

    /**
     * @return the measurementType
     */
    public Integer getMeasurementType() {
        return measurementType;
    }

    /**
     * @param measurementType the measurementType to set
     */
    public void setMeasurementType(Integer measurementType) {
        this.measurementType = measurementType;
    }
    
}
