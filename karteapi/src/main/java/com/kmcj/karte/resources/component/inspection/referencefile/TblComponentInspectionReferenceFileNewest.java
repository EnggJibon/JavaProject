/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.referencefile;

import com.google.gson.Gson;
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

/**
 *
 * @author duanlin
 */
@Entity
@Table(name = "tbl_component_inspection_reference_file_newest")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblComponentInspectionReferenceFileNewest.findById", query = "SELECT t FROM TblComponentInspectionReferenceFileNewest t WHERE t.id = :id"),
    @NamedQuery(name = "TblComponentInspectionReferenceFileNewest.findByComponentId", query = "SELECT t FROM TblComponentInspectionReferenceFileNewest t WHERE t.componentId = :componentId")})
public class TblComponentInspectionReferenceFileNewest implements Serializable {

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
    @Size(max = 45)
    @Column(name = "DRAWING_FILE_UUID")
    private String drawingFileUuid;
    @Size(max = 45)
    @Column(name = "PROOF_FILE_UUID")
    private String proofFileUuid;
    @Size(max = 45)
    @Column(name = "ROHS_PROOF_FILE_UUID")
    private String rohsProofFileUuid;
    @Size(max = 45)
    @Column(name = "PACKAGE_SPEC_FILE_UUID")
    private String packageSpecFileUuid;
    @Size(max = 45)
    @Column(name = "QC_PHASE_FILE_UUID")
    private String qcPhaseFileUuid;
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

    @Size(max = 45)
    @Column(name = "FILE_06_UUID")
    private String file06Uuid;
    @Size(max = 45)
    @Column(name = "FILE_07_UUID")
    private String file07Uuid;
    @Size(max = 45)
    @Column(name = "FILE_08_UUID")
    private String file08Uuid;
    @Size(max = 45)
    @Column(name = "FILE_09_UUID")
    private String file09Uuid;
    @Size(max = 45)
    @Column(name = "FILE_10_UUID")
    private String file10Uuid;
    @Size(max = 45)
    @Column(name = "FILE_11_UUID")
    private String file11Uuid;
    @Size(max = 45)
    @Column(name = "FILE_12_UUID")
    private String file12Uuid;
    @Size(max = 45)
    @Column(name = "FILE_13_UUID")
    private String file13Uuid;
    @Size(max = 45)
    @Column(name = "FILE_14_UUID")
    private String file14Uuid;
    @Size(max = 45)
    @Column(name = "FILE_15_UUID")
    private String file15Uuid;
    @Size(max = 45)
    @Column(name = "FILE_16_UUID")
    private String file16Uuid;
    @Size(max = 45)
    @Column(name = "FILE_17_UUID")
    private String file17Uuid;
    @Size(max = 45)
    @Column(name = "FILE_18_UUID")
    private String file18Uuid;
    @Size(max = 45)
    @Column(name = "FILE_19_UUID")
    private String file19Uuid;
    @Size(max = 45)
    @Column(name = "FILE_20_UUID")
    private String file20Uuid;
    public TblComponentInspectionReferenceFileNewest() {
    }

    public TblComponentInspectionReferenceFileNewest(String id) {
        this.id = id;
    }

    public TblComponentInspectionReferenceFileNewest(String id, String componentId) {
        this.id = id;
        this.componentId = componentId;
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

    public String getDrawingFileUuid() {
        return drawingFileUuid;
    }

    public void setDrawingFileUuid(String drawingFileUuid) {
        this.drawingFileUuid = drawingFileUuid;
    }

    public String getProofFileUuid() {
        return proofFileUuid;
    }

    public void setProofFileUuid(String proofFileUuid) {
        this.proofFileUuid = proofFileUuid;
    }

    public String getRohsProofFileUuid() {
        return rohsProofFileUuid;
    }

    public void setRohsProofFileUuid(String rohsProofFileUuid) {
        this.rohsProofFileUuid = rohsProofFileUuid;
    }

    public String getPackageSpecFileUuid() {
        return packageSpecFileUuid;
    }

    public void setPackageSpecFileUuid(String packageSpecFileUuid) {
        this.packageSpecFileUuid = packageSpecFileUuid;
    }

    public String getQcPhaseFileUuid() {
        return qcPhaseFileUuid;
    }

    public void setQcPhaseFileUuid(String qcPhaseFileUuid) {
        this.qcPhaseFileUuid = qcPhaseFileUuid;
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

    public String getFile06Uuid() { return file06Uuid; }
    public void setFile06Uuid(String file06Uuid) { this.file06Uuid = file06Uuid; }

    public String getFile07Uuid() { return file07Uuid; }

    public void setFile07Uuid(String file07Uuid) { this.file07Uuid = file07Uuid; }

    public String getFile08Uuid() { return file08Uuid; }

    public void setFile08Uuid(String file08Uuid) { this.file08Uuid = file08Uuid; }

    public String getFile09Uuid() { return file09Uuid; }

    public void setFile09Uuid(String file09Uuid) { this.file09Uuid = file09Uuid; }

    public String getFile10Uuid() { return file10Uuid; }

    public void setFile10Uuid(String file10Uuid) { this.file10Uuid = file10Uuid; }

    public String getFile11Uuid() { return file11Uuid; }

    public void setFile11Uuid(String file11Uuid) {
        this.file11Uuid = file11Uuid;
    }

    public String getFile12Uuid() {
        return file12Uuid;
    }

    public void setFile12Uuid(String file12Uuid) {
        this.file12Uuid = file12Uuid;
    }

    public String getFile13Uuid() {
        return file13Uuid;
    }

    public void setFile13Uuid(String file13Uuid) {
        this.file13Uuid = file13Uuid;
    }

    public String getFile14Uuid() {
        return file14Uuid;
    }

    public void setFile14Uuid(String file14Uuid) {
        this.file14Uuid = file14Uuid;
    }

    public String getFile15Uuid() {
        return file15Uuid;
    }

    public void setFile15Uuid(String file15Uuid) {
        this.file15Uuid = file15Uuid;
    }

    public String getFile16Uuid() {
        return file16Uuid;
    }

    public void setFile16Uuid(String file16Uuid) {
        this.file16Uuid = file16Uuid;
    }

    public String getFile17Uuid() {
        return file17Uuid;
    }

    public void setFile17Uuid(String file17Uuid) {
        this.file17Uuid = file17Uuid;
    }

    public String getFile18Uuid() {
        return file18Uuid;
    }

    public void setFile18Uuid(String file18Uuid) {
        this.file18Uuid = file18Uuid;
    }

    public String getFile19Uuid() {
        return file19Uuid;
    }

    public void setFile19Uuid(String file19Uuid) {
        this.file19Uuid = file19Uuid;
    }

    public String getFile20Uuid() {
        return file20Uuid;
    }
    public void setFile20Uuid(String file20Uuid) {
        this.file20Uuid = file20Uuid;
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
        if (!(object instanceof TblComponentInspectionReferenceFileNewest)) {
            return false;
        }
        TblComponentInspectionReferenceFileNewest other = (TblComponentInspectionReferenceFileNewest) object;
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
