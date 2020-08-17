/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.file;

import com.kmcj.karte.util.XmlDateAdapter;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
 * @author Apeng
 */
@Entity
@Table(name = "mst_component_inspect_file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstComponentInspectFile.deleteByInspectTypeId", query = "DELETE FROM MstComponentInspectFile m WHERE m.mstComponentInspectFilePK.inspectTypeId = :inspectTypeId AND m.mstComponentInspectFilePK.ownerCompanyId = 'SELF'"),
    @NamedQuery(name = "MstComponentInspectFile.deleteByInspectClassId", query = "DELETE FROM MstComponentInspectFile m WHERE m.mstComponentInspectFilePK.inspectClassId = :inspectClassId AND m.mstComponentInspectFilePK.ownerCompanyId = 'SELF'"),
    @NamedQuery(name = "MstComponentInspectFile.findByPK", query = "SELECT m FROM MstComponentInspectFile m WHERE m.mstComponentInspectFilePK.inspectTypeId = :inspectTypeId and m.mstComponentInspectFilePK.inspectClassId = :inspectClassId AND m.mstComponentInspectFilePK.ownerCompanyId = :ownerCompanyId"),
    @NamedQuery(name = "MstComponentInspectFile.findByClassOrType", query = "SELECT m FROM MstComponentInspectFile m WHERE (m.mstComponentInspectFilePK.inspectTypeId in :inspectTypeIds or m.mstComponentInspectFilePK.inspectClassId in :inspectClassIds) AND m.mstComponentInspectFilePK.ownerCompanyId = :ownerCompanyId")
})
public class MstComponentInspectFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private MstComponentInspectFilePK mstComponentInspectFilePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DRAWING_FLG")
    private Character drawingFlg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PROOF_FLG")
    private Character proofFlg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ROHS_PROOF_FLG")
    private Character rohsProofFlg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PACKAGE_SPEC_FLG")
    private Character packageSpecFlg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QC_PHASE_FLG")
    private Character qcPhaseFlg;
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
    @Column(name = "FILE_06_FLG")
    private Character file06Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_07_FLG")
    private Character file07Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_08_FLG")
    private Character file08Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_09_FLG")
    private Character file09Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_10_FLG")
    private Character file10Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_11_FLG")
    private Character file11Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_12_FLG")
    private Character file12Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_13_FLG")
    private Character file13Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_14_FLG")
    private Character file14Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_15_FLG")
    private Character file15Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_16_FLG")
    private Character file16Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_17_FLG")
    private Character file17Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_18_FLG")
    private Character file18Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_19_FLG")
    private Character file19Flg = '2';
    @Basic(optional = false)
    @NotNull
    @Column(name = "FILE_20_FLG")
    private Character file20Flg = '2';
    @JoinColumns({
        @JoinColumn(name = "INSPECT_CLASS_ID", referencedColumnName = "ID", insertable = false, updatable = false),
        @JoinColumn(name = "OWNER_COMPANY_ID", referencedColumnName = "OWNER_COMPANY_ID", insertable = false, updatable = false)
    })
    @ManyToOne(optional = false)
    private MstComponentInspectClass mstComponentInspectClass;
    @JoinColumn(name = "INSPECT_TYPE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MstComponentInspectType mstComponentInspectType;

    public MstComponentInspectFile() {
    }

    public MstComponentInspectFile(MstComponentInspectFilePK mstComponentInspectFilePK) {
        this.mstComponentInspectFilePK = mstComponentInspectFilePK;
    }

    public MstComponentInspectFile(MstComponentInspectFilePK mstComponentInspectFilePK, Character drawingFlg, Character proofFlg, Character rohsProofFlg, Character packageSpecFlg, Character qcPhaseFlg, Date createDate, Date updateDate, String createUserUuid, String updateUserUuid) {
        this.mstComponentInspectFilePK = mstComponentInspectFilePK;
        this.drawingFlg = drawingFlg;
        this.proofFlg = proofFlg;
        this.rohsProofFlg = rohsProofFlg;
        this.packageSpecFlg = packageSpecFlg;
        this.qcPhaseFlg = qcPhaseFlg;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.createUserUuid = createUserUuid;
        this.updateUserUuid = updateUserUuid;
    }

    public MstComponentInspectFile(String inspectTypeId, String inspectClassId) {
        this.mstComponentInspectFilePK = new MstComponentInspectFilePK(inspectTypeId, inspectClassId);
    }

    public MstComponentInspectFilePK getMstComponentInspectFilePK() {
        return mstComponentInspectFilePK;
    }

    public void setMstComponentInspectFilePK(MstComponentInspectFilePK mstComponentInspectFilePK) {
        this.mstComponentInspectFilePK = mstComponentInspectFilePK;
    }

    public Character getDrawingFlg() {
        return drawingFlg;
    }

    public void setDrawingFlg(Character drawingFlg) {
        this.drawingFlg = drawingFlg;
    }

    public Character getProofFlg() {
        return proofFlg;
    }

    public void setProofFlg(Character proofFlg) {
        this.proofFlg = proofFlg;
    }

    public Character getRohsProofFlg() {
        return rohsProofFlg;
    }

    public void setRohsProofFlg(Character rohsProofFlg) {
        this.rohsProofFlg = rohsProofFlg;
    }

    public Character getPackageSpecFlg() {
        return packageSpecFlg;
    }

    public void setPackageSpecFlg(Character packageSpecFlg) {
        this.packageSpecFlg = packageSpecFlg;
    }

    public Character getQcPhaseFlg() {
        return qcPhaseFlg;
    }

    public void setQcPhaseFlg(Character qcPhaseFlg) {
        this.qcPhaseFlg = qcPhaseFlg;
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

    public void setFile06Flg(Character file06Flg) { this.file06Flg = file06Flg; }

    public Character getFile06Flg() { return  this.file06Flg; }

    public void setFile07Flg(Character file07Flg) { this.file07Flg = file07Flg; }

    public Character getFile07Flg() { return this.file07Flg; }

    public void setFile08Flg(Character file08Flg) { this.file08Flg = file08Flg; }

    public Character getFile08Flg() { return this.file08Flg; }

    public void setFile09Flg(Character file09Flg) { this.file09Flg = file09Flg; }

    public Character getFile09Flg() { return this.file09Flg; }

    public void setFile10Flg(Character file10Flg) { this.file10Flg = file10Flg; }

    public Character getFile10Flg() { return this.file10Flg; }

    public void setFile11Flg(Character file11Flg) { this.file11Flg = file11Flg; }

    public Character getFile11Flg() { return this.file11Flg; }

    public void setFile12Flg(Character file12Flg) { this.file12Flg = file12Flg; }

    public Character getFile12Flg() { return this.file12Flg; }

    public void setFile13Flg(Character file13Flg) { this.file13Flg = file13Flg; }

    public Character getFile13Flg() { return this.file13Flg; }

    public void setFile14Flg(Character file14Flg) { this.file14Flg = file14Flg; }

    public Character getFile14Flg() { return this.file14Flg; }

    public void setFile15Flg(Character file15Flg) { this.file15Flg = file15Flg; }

    public Character getFile15Flg() { return this.file15Flg; }

    public void setFile16Flg(Character file16Flg) { this.file16Flg = file16Flg; }

    public Character getFile16Flg() { return this.file16Flg; }

    public void setFile17Flg(Character file17Flg) { this.file17Flg = file17Flg; }

    public Character getFile17Flg() { return this.file17Flg; }

    public void setFile18Flg(Character file18Flg) { this.file18Flg = file18Flg; }

    public Character getFile18Flg() { return this.file18Flg; }

    public void setFile19Flg(Character file19Flg) { this.file19Flg = file19Flg; }

    public Character getFile19Flg() { return this.file19Flg; }

    public void setFile20Flg(Character file20Flg) { this.file20Flg = file20Flg; }

    public Character getFile20Flg() { return  this.file20Flg; }

    public MstComponentInspectClass getMstComponentInspectClass() {
        return mstComponentInspectClass;
    }

    public void setMstComponentInspectClass(MstComponentInspectClass mstComponentInspectClass) {
        this.mstComponentInspectClass = mstComponentInspectClass;
    }

    public MstComponentInspectType getMstComponentInspectType() {
        return mstComponentInspectType;
    }

    public void setMstComponentInspectType(MstComponentInspectType mstComponentInspectType) {
        this.mstComponentInspectType = mstComponentInspectType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mstComponentInspectFilePK != null ? mstComponentInspectFilePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstComponentInspectFile)) {
            return false;
        }
        MstComponentInspectFile other = (MstComponentInspectFile) object;
        if ((this.mstComponentInspectFilePK == null && other.mstComponentInspectFilePK != null) || (this.mstComponentInspectFilePK != null && !this.mstComponentInspectFilePK.equals(other.mstComponentInspectFilePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.inspection.file.MstComponentInspectFile[ mstComponentInspectFilePK=" + mstComponentInspectFilePK + " ]";
    }
    
}
