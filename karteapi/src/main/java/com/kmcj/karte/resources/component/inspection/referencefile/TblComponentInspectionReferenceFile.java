/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.referencefile;

import com.google.gson.Gson;
import com.kmcj.karte.util.XmlDateAdapter;
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

/**
 *
 * @author duanlin
 */
@Entity
@Table(name = "tbl_component_inspection_reference_file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblComponentInspectionReferenceFile.findById", query = "SELECT t FROM TblComponentInspectionReferenceFile t WHERE t.id = :id"),
    @NamedQuery(name = "TblComponentInspectionReferenceFile.findByComponentInspectionResultId", query = "SELECT t FROM TblComponentInspectionReferenceFile t WHERE t.componentInspectionResultId = :componentInspectionResultId"),
    @NamedQuery(name = "TblComponentInspectionReferenceFile.deleteByComponentInspectionResultId", query = "DELETE  FROM TblComponentInspectionReferenceFile t WHERE t.componentInspectionResultId = :componentInspectionResultId")
})
public class TblComponentInspectionReferenceFile implements Serializable {

    public static final String FILE_STATUS_DEFAULT = "DEFAULT";
    public static final String FILE_STATUS_CONFIRMED = "CONFIRMED";
    public static final String FILE_STATUS_DENIED = "DENIED";
    public static final String FILE_STATUS_SKIP = "SKIP";

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
    @Column(name = "DRAWING_FILE_STATUS")
    private String drawingFileStatus = "DEFAULT";
    @Size(max = 45)
    @Column(name = "PROOF_FILE_STATUS")
    private String proofFileStatus = "DEFAULT";
    @Size(max = 45)
    @Column(name = "ROHS_PROOF_FILE_STATUS")
    private String rohsProofFileStatus = "DEFAULT";
    @Size(max = 45)
    @Column(name = "PACKAGE_SPEC_FILE_STATUS")
    private String packageSpecFileStatus = "DEFAULT";
    @Size(max = 45)
    @Column(name = "QC_PHASE_FILE_STATUS")
    private String qcPhaseFileStatus = "DEFAULT";
    @Column(name = "DRAWING_FILE_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date drawingFileDate;
    @Column(name = "PROOF_FILE_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date proofFileDate;
    @Column(name = "ROHS_PROOF_FILE_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date rohsProofFileDate;
    @Column(name = "PACKAGE_SPEC_FILE_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date packageSpecFileDate;
    @Column(name = "QC_PHASE_SPEC_FILE_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date qcPhaseFileDate;

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

    @Size(max = 45)
    @Column(name = "FILE_06_STATUS")
    private String file06Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_07_STATUS")
    private String file07Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_08_STATUS")
    private String file08Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_09_STATUS")
    private String file09Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_10_STATUS")
    private String file10Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_11_STATUS")
    private String file11Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_12_STATUS")
    private String file12Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_13_STATUS")
    private String file13Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_14_STATUS")
    private String file14Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_15_STATUS")
    private String file15Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_16_STATUS")
    private String file16Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_17_STATUS")
    private String file17Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_18_STATUS")
    private String file18Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_19_STATUS")
    private String file19Status = "DEFAULT";
    @Size(max = 45)
    @Column(name = "FILE_20_STATUS")
    private String file20Status = "DEFAULT";

    @Column(name = "FILE_06_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file06Date;
    @Column(name = "FILE_07_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file07Date;
    @Column(name = "FILE_08_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file08Date;
    @Column(name = "FILE_09_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file09Date;
    @Column(name = "FILE_10_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file10Date;
    @Column(name = "FILE_11_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file11Date;
    @Column(name = "FILE_12_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file12Date;
    @Column(name = "FILE_13_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file13Date;
    @Column(name = "FILE_14_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file14Date;
    @Column(name = "FILE_15_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file15Date;
    @Column(name = "FILE_16_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file16Date;
    @Column(name = "FILE_17_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file17Date;
    @Column(name = "FILE_18_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file18Date;
    @Column(name = "FILE_19_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file19Date;
    @Column(name = "FILE_20_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date file20Date;
    
    @Column(name = "DRAWING_FILE_FLG")
    @NotNull
    private Character drawingFileFlg;
    @Column(name = "PROOF_FILE_FLG")
    @NotNull
    private Character proofFileFlg;
    @Column(name = "ROHS_PROOF_FILE_FLG")
    @NotNull
    private Character rohsProofFileFlg;
    @Column(name = "PACKAGE_SPEC_FILE_FLG")
    @NotNull
    private Character packageSpecFileFlg;
    @Column(name = "QC_PHASE_FILE_FLG")
    @NotNull
    private Character qcPhaseFileFlg;
    @Column(name = "FILE_06_FLG")
    @NotNull
    private Character file06Flg;
    @Column(name = "FILE_07_FLG")
    @NotNull
    private Character file07Flg;
    @Column(name = "FILE_08_FLG")
    @NotNull
    private Character file08Flg;
    @Column(name = "FILE_09_FLG")
    @NotNull
    private Character file09Flg;
    @Column(name = "FILE_10_FLG")
    @NotNull
    private Character file10Flg;
    @Column(name = "FILE_11_FLG")
    @NotNull
    private Character file11Flg;
    @Column(name = "FILE_12_FLG")
    @NotNull
    private Character file12Flg;
    @Column(name = "FILE_13_FLG")
    @NotNull
    private Character file13Flg;
    @Column(name = "FILE_14_FLG")
    @NotNull
    private Character file14Flg;
    @Column(name = "FILE_15_FLG")
    @NotNull
    private Character file15Flg;
    @Column(name = "FILE_16_FLG")
    @NotNull
    private Character file16Flg;
    @Column(name = "FILE_17_FLG")
    @NotNull
    private Character file17Flg;
    @Column(name = "FILE_18_FLG")
    @NotNull
    private Character file18Flg;
    @Column(name = "FILE_19_FLG")
    @NotNull
    private Character file19Flg;
    @Column(name = "FILE_20_FLG")
    @NotNull
    private Character file20Flg;
    @NotNull
    @Size(max = 100)
    @Column(name = "DRAWING_FILE_NAME")
    private String drawingFileName;
    @NotNull
    @Size(max = 100)
    @Column(name = "PROOF_FILE_NAME")
    private String proofFileName;
    @NotNull
    @Size(max = 100)
    @Column(name = "ROHS_PROOF_FILE_NAME")
    private String rohsProofFileName;
    @NotNull
    @Size(max = 100)
    @Column(name = "PACKAGE_SPEC_FILE_NAME")
    private String packageSpecFileName;
    @NotNull
    @Size(max = 100)
    @Column(name = "QC_PHASE_FILE_NAME")
    private String qcPhaseFileName;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_06_NAME")
    private String file06Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_07_NAME")
    private String file07Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_08_NAME")
    private String file08Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_09_NAME")
    private String file09Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_10_NAME")
    private String file10Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_11_NAME")
    private String file11Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_12_NAME")
    private String file12Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_13_NAME")
    private String file13Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_14_NAME")
    private String file14Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_15_NAME")
    private String file15Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_16_NAME")
    private String file16Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_17_NAME")
    private String file17Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_18_NAME")
    private String file18Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_19_NAME")
    private String file19Name;
    @NotNull
    @Size(max = 100)
    @Column(name = "FILE_20_NAME")
    private String file20Name;
    @Size(max = 45)
    @Column(name = "DRAWING_FILE_CONFIRMER_ID")
    private String drawingFileConfirmerId;
    @Size(max = 45)
    @Column(name = "DRAWING_FILE_CONFIRMER_NAME")
    private String drawingFileConfirmerName;
    @Size(max = 45)
    @Column(name = "PROOF_FILE_CONFIRMER_ID")
    private String proofFileConfirmerId;
    @Size(max = 45)
    @Column(name = "PROOF_FILE_CONFIRMER_NAME")
    private String proofFileConfirmerName;
    @Size(max = 45)
    @Column(name = "ROHS_PROOF_FILE_CONFIRMER_ID")
    private String rohsProofFileConfirmerId;
    @Size(max = 45)
    @Column(name = "ROHS_PROOF_FILE_CONFIRMER_NAME")
    private String rohsProofFileConfirmerName;
    @Size(max = 45)
    @Column(name = "PACKAGE_SPEC_FILE_CONFIRMER_ID")
    private String packageSpecFileConfirmerId;
    @Size(max = 45)
    @Column(name = "PACKAGE_SPEC_FILE_CONFIRMER_NAME")
    private String packageSpecFileConfirmerName;
    @Size(max = 45)
    @Column(name = "QC_PHASE_FILE_CONFIRMER_ID")
    private String qcPhaseFileConfirmerId;
    @Size(max = 45)
    @Column(name = "QC_PHASE_FILE_CONFIRMER_NAME")
    private String qcPhaseFileConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_06_CONFIRMER_ID")
    private String file06ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_06_CONFIRMER_NAME")
    private String file06ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_07_CONFIRMER_ID")
    private String file07ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_07_CONFIRMER_NAME")
    private String file07ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_08_CONFIRMER_ID")
    private String file08ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_08_CONFIRMER_NAME")
    private String file08ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_09_CONFIRMER_ID")
    private String file09ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_09_CONFIRMER_NAME")
    private String file09ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_10_CONFIRMER_ID")
    private String file10ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_10_CONFIRMER_NAME")
    private String file10ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_11_CONFIRMER_ID")
    private String file11ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_11_CONFIRMER_NAME")
    private String file11ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_12_CONFIRMER_ID")
    private String file12ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_12_CONFIRMER_NAME")
    private String file12ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_13_CONFIRMER_ID")
    private String file13ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_13_CONFIRMER_NAME")
    private String file13ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_14_CONFIRMER_ID")
    private String file14ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_14_CONFIRMER_NAME")
    private String file14ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_15_CONFIRMER_ID")
    private String file15ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_15_CONFIRMER_NAME")
    private String file15ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_16_CONFIRMER_ID")
    private String file16ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_16_CONFIRMER_NAME")
    private String file16ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_17_CONFIRMER_ID")
    private String file17ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_17_CONFIRMER_NAME")
    private String file17ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_18_CONFIRMER_ID")
    private String file18ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_18_CONFIRMER_NAME")
    private String file18ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_19_CONFIRMER_ID")
    private String file19ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_19_CONFIRMER_NAME")
    private String file19ConfirmerName;
    @Size(max = 45)
    @Column(name = "FILE_20_CONFIRMER_ID")
    private String file20ConfirmerId;
    @Size(max = 45)
    @Column(name = "FILE_20_CONFIRMER_NAME")
    private String file20ConfirmerName;
    
    public TblComponentInspectionReferenceFile() {
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

    public String getDrawingFileStatus() {
        return drawingFileStatus;
    }

    public void setDrawingFileStatus(String drawingFileStatus) {
        this.drawingFileStatus = drawingFileStatus;
    }

    public String getProofFileStatus() {
        return proofFileStatus;
    }

    public void setProofFileStatus(String proofFileStatus) {
        this.proofFileStatus = proofFileStatus;
    }

    public String getRohsProofFileStatus() {
        return rohsProofFileStatus;
    }

    public void setRohsProofFileStatus(String rohsProofFileStatus) {
        this.rohsProofFileStatus = rohsProofFileStatus;
    }

    public String getPackageSpecFileStatus() {
        return packageSpecFileStatus;
    }

    public void setPackageSpecFileStatus(String packageSpecFileStatus) {
        this.packageSpecFileStatus = packageSpecFileStatus;
    }

    public String getQcPhaseFileStatus() {
        return qcPhaseFileStatus;
    }

    public void setQcPhaseFileStatus(String qcPhaseFileStatus) {
        this.qcPhaseFileStatus = qcPhaseFileStatus;
    }

    public Date getDrawingFileDate() {
        return drawingFileDate;
    }

    public void setDrawingFileDate(Date drawingFileDate) {
        this.drawingFileDate = drawingFileDate;
    }

    public Date getProofFileDate() {
        return proofFileDate;
    }

    public void setProofFileDate(Date proofFileDate) {
        this.proofFileDate = proofFileDate;
    }

    public Date getRohsProofFileDate() {
        return rohsProofFileDate;
    }

    public void setRohsProofFileDate(Date rohsProofFileDate) {
        this.rohsProofFileDate = rohsProofFileDate;
    }

    public Date getPackageSpecFileDate() {
        return packageSpecFileDate;
    }

    public void setPackageSpecFileDate(Date packageSpecFileDate) {
        this.packageSpecFileDate = packageSpecFileDate;
    }

    public Date getQcPhaseFileDate() {
        return qcPhaseFileDate;
    }

    public void setQcPhaseFileDate(Date qcPhaseFileDate) {
        this.qcPhaseFileDate = qcPhaseFileDate;
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

    public String getFile06Status() {
        return file06Status;
    }

    public void setFile06Status(String file06Status) {
        this.file06Status = file06Status;
    }

    public String getFile07Status() {
        return file07Status;
    }

    public void setFile07Status(String file07Status) {
        this.file07Status = file07Status;
    }

    public String getFile08Status() {
        return file08Status;
    }

    public void setFile08Status(String file08Status) {
        this.file08Status = file08Status;
    }

    public String getFile09Status() {
        return file09Status;
    }

    public void setFile09Status(String file09Status) {
        this.file09Status = file09Status;
    }

    public String getFile10Status() {
        return file10Status;
    }

    public void setFile10Status(String file10Status) {
        this.file10Status = file10Status;
    }

    public String getFile11Status() {
        return file11Status;
    }

    public void setFile11Status(String file11Status) {
        this.file11Status = file11Status;
    }

    public String getFile12Status() {
        return file12Status;
    }

    public void setFile12Status(String file12Status) {
        this.file12Status = file12Status;
    }

    public String getFile13Status() {
        return file13Status;
    }

    public void setFile13Status(String file13Status) {
        this.file13Status = file13Status;
    }

    public String getFile14Status() {
        return file14Status;
    }

    public void setFile14Status(String file14Status) {
        this.file14Status = file14Status;
    }

    public String getFile15Status() {
        return file15Status;
    }

    public void setFile15Status(String file15Status) {
        this.file15Status = file15Status;
    }

    public String getFile16Status() {
        return file16Status;
    }

    public void setFile16Status(String file16Status) {
        this.file16Status = file16Status;
    }

    public String getFile17Status() {
        return file17Status;
    }

    public void setFile17Status(String file17Status) {
        this.file17Status = file17Status;
    }

    public String getFile18Status() {
        return file18Status;
    }

    public void setFile18Status(String file18Status) {
        this.file18Status = file18Status;
    }

    public String getFile19Status() {
        return file19Status;
    }

    public void setFile19Status(String file19Status) {
        this.file19Status = file19Status;
    }

    public String getFile20Status() {
        return file20Status;
    }

    public void setFile20Status(String file20Status) {
        this.file20Status = file20Status;
    }

    public Date getFile06Date() {
        return file06Date;
    }

    public void setFile06Date(Date file06Date) {
        this.file06Date = file06Date;
    }

    public Date getFile07Date() {
        return file07Date;
    }

    public void setFile07Date(Date file07Date) {
        this.file07Date = file07Date;
    }

    public Date getFile08Date() {
        return file08Date;
    }

    public void setFile08Date(Date file08Date) {
        this.file08Date = file08Date;
    }

    public Date getFile09Date() {
        return file09Date;
    }

    public void setFile09Date(Date file09Date) {
        this.file09Date = file09Date;
    }

    public Date getFile10Date() {
        return file10Date;
    }

    public void setFile10Date(Date file10Date) {
        this.file10Date = file10Date;
    }

    public Date getFile11Date() {
        return file11Date;
    }

    public void setFile11Date(Date file11Date) {
        this.file11Date = file11Date;
    }

    public Date getFile12Date() {
        return file12Date;
    }

    public void setFile12Date(Date file12Date) {
        this.file12Date = file12Date;
    }

    public Date getFile13Date() {
        return file13Date;
    }

    public void setFile13Date(Date file13Date) {
        this.file13Date = file13Date;
    }

    public Date getFile14Date() {
        return file14Date;
    }

    public void setFile14Date(Date file14Date) {
        this.file14Date = file14Date;
    }

    public Date getFile15Date() {
        return file15Date;
    }

    public void setFile15Date(Date file15Date) {
        this.file15Date = file15Date;
    }

    public Date getFile16Date() {
        return file16Date;
    }

    public void setFile16Date(Date file16Date) {
        this.file16Date = file16Date;
    }

    public Date getFile17Date() {
        return file17Date;
    }

    public void setFile17Date(Date file17Date) {
        this.file17Date = file17Date;
    }

    public Date getFile18Date() {
        return file18Date;
    }

    public void setFile18Date(Date file18Date) {
        this.file18Date = file18Date;
    }

    public Date getFile19Date() {
        return file19Date;
    }

    public void setFile19Date(Date file19Date) {
        this.file19Date = file19Date;
    }

    public Date getFile20Date() {
        return file20Date;
    }

    public void setFile20Date(Date file20Date) {
        this.file20Date = file20Date;
    }

    public Character getDrawingFileFlg() {
        return drawingFileFlg;
    }

    public void setDrawingFileFlg(Character drawingFileFlg) {
        this.drawingFileFlg = drawingFileFlg;
    }

    public Character getProofFileFlg() {
        return proofFileFlg;
    }

    public void setProofFileFlg(Character proofFileFlg) {
        this.proofFileFlg = proofFileFlg;
    }

    public Character getRohsProofFileFlg() {
        return rohsProofFileFlg;
    }

    public void setRohsProofFileFlg(Character rohsProofFileFlg) {
        this.rohsProofFileFlg = rohsProofFileFlg;
    }

    public Character getPackageSpecFileFlg() {
        return packageSpecFileFlg;
    }

    public void setPackageSpecFileFlg(Character packageSpecFileFlg) {
        this.packageSpecFileFlg = packageSpecFileFlg;
    }

    public Character getQcPhaseFileFlg() {
        return qcPhaseFileFlg;
    }

    public void setQcPhaseFileFlg(Character qcPhaseFileFlg) {
        this.qcPhaseFileFlg = qcPhaseFileFlg;
    }

    public Character getFile06Flg() {
        return file06Flg;
    }

    public void setFile06Flg(Character file06Flg) {
        this.file06Flg = file06Flg;
    }

    public Character getFile07Flg() {
        return file07Flg;
    }

    public void setFile07Flg(Character file07Flg) {
        this.file07Flg = file07Flg;
    }

    public Character getFile08Flg() {
        return file08Flg;
    }

    public void setFile08Flg(Character file08Flg) {
        this.file08Flg = file08Flg;
    }

    public Character getFile09Flg() {
        return file09Flg;
    }

    public void setFile09Flg(Character file09Flg) {
        this.file09Flg = file09Flg;
    }

    public Character getFile10Flg() {
        return file10Flg;
    }

    public void setFile10Flg(Character file10Flg) {
        this.file10Flg = file10Flg;
    }

    public Character getFile11Flg() {
        return file11Flg;
    }

    public void setFile11Flg(Character file11Flg) {
        this.file11Flg = file11Flg;
    }

    public Character getFile12Flg() {
        return file12Flg;
    }

    public void setFile12Flg(Character file12Flg) {
        this.file12Flg = file12Flg;
    }

    public Character getFile13Flg() {
        return file13Flg;
    }

    public void setFile13Flg(Character file13Flg) {
        this.file13Flg = file13Flg;
    }

    public Character getFile14Flg() {
        return file14Flg;
    }

    public void setFile14Flg(Character file14Flg) {
        this.file14Flg = file14Flg;
    }

    public Character getFile15Flg() {
        return file15Flg;
    }

    public void setFile15Flg(Character file15Flg) {
        this.file15Flg = file15Flg;
    }

    public Character getFile16Flg() {
        return file16Flg;
    }

    public void setFile16Flg(Character file16Flg) {
        this.file16Flg = file16Flg;
    }

    public Character getFile17Flg() {
        return file17Flg;
    }

    public void setFile17Flg(Character file17Flg) {
        this.file17Flg = file17Flg;
    }

    public Character getFile18Flg() {
        return file18Flg;
    }

    public void setFile18Flg(Character file18Flg) {
        this.file18Flg = file18Flg;
    }

    public Character getFile19Flg() {
        return file19Flg;
    }

    public void setFile19Flg(Character file19Flg) {
        this.file19Flg = file19Flg;
    }

    public Character getFile20Flg() {
        return file20Flg;
    }

    public void setFile20Flg(Character file20Flg) {
        this.file20Flg = file20Flg;
    }

    public String getDrawingFileName() {
        return drawingFileName;
    }

    public void setDrawingFileName(String drawingFileName) {
        this.drawingFileName = drawingFileName;
    }

    public String getProofFileName() {
        return proofFileName;
    }

    public void setProofFileName(String proofFileName) {
        this.proofFileName = proofFileName;
    }

    public String getRohsProofFileName() {
        return rohsProofFileName;
    }

    public void setRohsProofFileName(String rohsProofFileName) {
        this.rohsProofFileName = rohsProofFileName;
    }

    public String getPackageSpecFileName() {
        return packageSpecFileName;
    }

    public void setPackageSpecFileName(String packageSpecFileName) {
        this.packageSpecFileName = packageSpecFileName;
    }

    public String getQcPhaseFileName() {
        return qcPhaseFileName;
    }

    public void setQcPhaseFileName(String qcPhaseFileName) {
        this.qcPhaseFileName = qcPhaseFileName;
    }

    public String getFile06Name() {
        return file06Name;
    }

    public void setFile06Name(String file06Name) {
        this.file06Name = file06Name;
    }

    public String getFile07Name() {
        return file07Name;
    }

    public void setFile07Name(String file07Name) {
        this.file07Name = file07Name;
    }

    public String getFile08Name() {
        return file08Name;
    }

    public void setFile08Name(String file08Name) {
        this.file08Name = file08Name;
    }

    public String getFile09Name() {
        return file09Name;
    }

    public void setFile09Name(String file09Name) {
        this.file09Name = file09Name;
    }

    public String getFile10Name() {
        return file10Name;
    }

    public void setFile10Name(String file10Name) {
        this.file10Name = file10Name;
    }

    public String getFile11Name() {
        return file11Name;
    }

    public void setFile11Name(String file11Name) {
        this.file11Name = file11Name;
    }

    public String getFile12Name() {
        return file12Name;
    }

    public void setFile12Name(String file12Name) {
        this.file12Name = file12Name;
    }

    public String getFile13Name() {
        return file13Name;
    }

    public void setFile13Name(String file13Name) {
        this.file13Name = file13Name;
    }

    public String getFile14Name() {
        return file14Name;
    }

    public void setFile14Name(String file14Name) {
        this.file14Name = file14Name;
    }

    public String getFile15Name() {
        return file15Name;
    }

    public void setFile15Name(String file15Name) {
        this.file15Name = file15Name;
    }

    public String getFile16Name() {
        return file16Name;
    }

    public void setFile16Name(String file16Name) {
        this.file16Name = file16Name;
    }

    public String getFile17Name() {
        return file17Name;
    }

    public void setFile17Name(String file17Name) {
        this.file17Name = file17Name;
    }

    public String getFile18Name() {
        return file18Name;
    }

    public void setFile18Name(String file18Name) {
        this.file18Name = file18Name;
    }

    public String getFile19Name() {
        return file19Name;
    }

    public void setFile19Name(String file19Name) {
        this.file19Name = file19Name;
    }

    public String getFile20Name() {
        return file20Name;
    }

    public void setFile20Name(String file20Name) {
        this.file20Name = file20Name;
    }

    public String getDrawingFileConfirmerId() {
        return drawingFileConfirmerId;
    }

    public void setDrawingFileConfirmerId(String drawingFileConfirmerId) {
        this.drawingFileConfirmerId = drawingFileConfirmerId;
    }

    public String getDrawingFileConfirmerName() {
        return drawingFileConfirmerName;
    }

    public void setDrawingFileConfirmerName(String drawingFileConfirmerName) {
        this.drawingFileConfirmerName = drawingFileConfirmerName;
    }

    public String getProofFileConfirmerId() {
        return proofFileConfirmerId;
    }

    public void setProofFileConfirmerId(String proofFileConfirmerId) {
        this.proofFileConfirmerId = proofFileConfirmerId;
    }

    public String getProofFileConfirmerName() {
        return proofFileConfirmerName;
    }

    public void setProofFileConfirmerName(String proofFileConfirmerName) {
        this.proofFileConfirmerName = proofFileConfirmerName;
    }

    public String getRohsProofFileConfirmerId() {
        return rohsProofFileConfirmerId;
    }

    public void setRohsProofFileConfirmerId(String rohsProofFileConfirmerId) {
        this.rohsProofFileConfirmerId = rohsProofFileConfirmerId;
    }

    public String getRohsProofFileConfirmerName() {
        return rohsProofFileConfirmerName;
    }

    public void setRohsProofFileConfirmerName(String rohsProofFileConfirmerName) {
        this.rohsProofFileConfirmerName = rohsProofFileConfirmerName;
    }

    public String getPackageSpecFileConfirmerId() {
        return packageSpecFileConfirmerId;
    }

    public void setPackageSpecFileConfirmerId(String packageSpecFileConfirmerId) {
        this.packageSpecFileConfirmerId = packageSpecFileConfirmerId;
    }

    public String getPackageSpecFileConfirmerName() {
        return packageSpecFileConfirmerName;
    }

    public void setPackageSpecFileConfirmerName(String packageSpecFileConfirmerName) {
        this.packageSpecFileConfirmerName = packageSpecFileConfirmerName;
    }

    public String getQcPhaseFileConfirmerId() {
        return qcPhaseFileConfirmerId;
    }

    public void setQcPhaseFileConfirmerId(String qcPhaseFileConfirmerId) {
        this.qcPhaseFileConfirmerId = qcPhaseFileConfirmerId;
    }

    public String getQcPhaseFileConfirmerName() {
        return qcPhaseFileConfirmerName;
    }

    public void setQcPhaseFileConfirmerName(String qcPhaseFileConfirmerName) {
        this.qcPhaseFileConfirmerName = qcPhaseFileConfirmerName;
    }

    public String getFile06ConfirmerId() {
        return file06ConfirmerId;
    }

    public void setFile06ConfirmerId(String file06ConfirmerId) {
        this.file06ConfirmerId = file06ConfirmerId;
    }

    public String getFile06ConfirmerName() {
        return file06ConfirmerName;
    }

    public void setFile06ConfirmerName(String file06ConfirmerName) {
        this.file06ConfirmerName = file06ConfirmerName;
    }

    public String getFile07ConfirmerId() {
        return file07ConfirmerId;
    }

    public void setFile07ConfirmerId(String file07ConfirmerId) {
        this.file07ConfirmerId = file07ConfirmerId;
    }

    public String getFile07ConfirmerName() {
        return file07ConfirmerName;
    }

    public void setFile07ConfirmerName(String file07ConfirmerName) {
        this.file07ConfirmerName = file07ConfirmerName;
    }

    public String getFile08ConfirmerId() {
        return file08ConfirmerId;
    }

    public void setFile08ConfirmerId(String file08ConfirmerId) {
        this.file08ConfirmerId = file08ConfirmerId;
    }

    public String getFile08ConfirmerName() {
        return file08ConfirmerName;
    }

    public void setFile08ConfirmerName(String file08ConfirmerName) {
        this.file08ConfirmerName = file08ConfirmerName;
    }

    public String getFile09ConfirmerId() {
        return file09ConfirmerId;
    }

    public void setFile09ConfirmerId(String file09ConfirmerId) {
        this.file09ConfirmerId = file09ConfirmerId;
    }

    public String getFile09ConfirmerName() {
        return file09ConfirmerName;
    }

    public void setFile09ConfirmerName(String file09ConfirmerName) {
        this.file09ConfirmerName = file09ConfirmerName;
    }

    public String getFile10ConfirmerId() {
        return file10ConfirmerId;
    }

    public void setFile10ConfirmerId(String file10ConfirmerId) {
        this.file10ConfirmerId = file10ConfirmerId;
    }

    public String getFile10ConfirmerName() {
        return file10ConfirmerName;
    }

    public void setFile10ConfirmerName(String file10ConfirmerName) {
        this.file10ConfirmerName = file10ConfirmerName;
    }

    public String getFile11ConfirmerId() {
        return file11ConfirmerId;
    }

    public void setFile11ConfirmerId(String file11ConfirmerId) {
        this.file11ConfirmerId = file11ConfirmerId;
    }

    public String getFile11ConfirmerName() {
        return file11ConfirmerName;
    }

    public void setFile11ConfirmerName(String file11ConfirmerName) {
        this.file11ConfirmerName = file11ConfirmerName;
    }

    public String getFile12ConfirmerId() {
        return file12ConfirmerId;
    }

    public void setFile12ConfirmerId(String file12ConfirmerId) {
        this.file12ConfirmerId = file12ConfirmerId;
    }

    public String getFile12ConfirmerName() {
        return file12ConfirmerName;
    }

    public void setFile12ConfirmerName(String file12ConfirmerName) {
        this.file12ConfirmerName = file12ConfirmerName;
    }

    public String getFile13ConfirmerId() {
        return file13ConfirmerId;
    }

    public void setFile13ConfirmerId(String file13ConfirmerId) {
        this.file13ConfirmerId = file13ConfirmerId;
    }

    public String getFile13ConfirmerName() {
        return file13ConfirmerName;
    }

    public void setFile13ConfirmerName(String file13ConfirmerName) {
        this.file13ConfirmerName = file13ConfirmerName;
    }

    public String getFile14ConfirmerId() {
        return file14ConfirmerId;
    }

    public void setFile14ConfirmerId(String file14ConfirmerId) {
        this.file14ConfirmerId = file14ConfirmerId;
    }

    public String getFile14ConfirmerName() {
        return file14ConfirmerName;
    }

    public void setFile14ConfirmerName(String file14ConfirmerName) {
        this.file14ConfirmerName = file14ConfirmerName;
    }

    public String getFile15ConfirmerId() {
        return file15ConfirmerId;
    }

    public void setFile15ConfirmerId(String file15ConfirmerId) {
        this.file15ConfirmerId = file15ConfirmerId;
    }

    public String getFile15ConfirmerName() {
        return file15ConfirmerName;
    }

    public void setFile15ConfirmerName(String file15ConfirmerName) {
        this.file15ConfirmerName = file15ConfirmerName;
    }

    public String getFile16ConfirmerId() {
        return file16ConfirmerId;
    }

    public void setFile16ConfirmerId(String file16ConfirmerId) {
        this.file16ConfirmerId = file16ConfirmerId;
    }

    public String getFile16ConfirmerName() {
        return file16ConfirmerName;
    }

    public void setFile16ConfirmerName(String file16ConfirmerName) {
        this.file16ConfirmerName = file16ConfirmerName;
    }

    public String getFile17ConfirmerId() {
        return file17ConfirmerId;
    }

    public void setFile17ConfirmerId(String file17ConfirmerId) {
        this.file17ConfirmerId = file17ConfirmerId;
    }

    public String getFile17ConfirmerName() {
        return file17ConfirmerName;
    }

    public void setFile17ConfirmerName(String file17ConfirmerName) {
        this.file17ConfirmerName = file17ConfirmerName;
    }

    public String getFile18ConfirmerId() {
        return file18ConfirmerId;
    }

    public void setFile18ConfirmerId(String file18ConfirmerId) {
        this.file18ConfirmerId = file18ConfirmerId;
    }

    public String getFile18ConfirmerName() {
        return file18ConfirmerName;
    }

    public void setFile18ConfirmerName(String file18ConfirmerName) {
        this.file18ConfirmerName = file18ConfirmerName;
    }

    public String getFile19ConfirmerId() {
        return file19ConfirmerId;
    }

    public void setFile19ConfirmerId(String file19ConfirmerId) {
        this.file19ConfirmerId = file19ConfirmerId;
    }

    public String getFile19ConfirmerName() {
        return file19ConfirmerName;
    }

    public void setFile19ConfirmerName(String file19ConfirmerName) {
        this.file19ConfirmerName = file19ConfirmerName;
    }

    public String getFile20ConfirmerId() {
        return file20ConfirmerId;
    }

    public void setFile20ConfirmerId(String file20ConfirmerId) {
        this.file20ConfirmerId = file20ConfirmerId;
    }

    public String getFile20ConfirmerName() {
        return file20ConfirmerName;
    }

    public void setFile20ConfirmerName(String file20ConfirmerName) {
        this.file20ConfirmerName = file20ConfirmerName;
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
        if (!(object instanceof TblComponentInspectionReferenceFile)) {
            return false;
        }
        TblComponentInspectionReferenceFile other = (TblComponentInspectionReferenceFile) object;
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
