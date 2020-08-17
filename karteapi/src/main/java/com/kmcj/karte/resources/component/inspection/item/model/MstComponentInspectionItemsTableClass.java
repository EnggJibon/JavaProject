/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item.model;

import com.kmcj.karte.resources.component.inspection.file.MstComponentInspectLang;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Apeng
 */
@Entity
@Table(name = "mst_component_inspection_items_table_class")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstComponentInspectionItemsTableClass.deleteByComponentInspectionItemsTableId", query = "DELETE FROM MstComponentInspectionItemsTableClass m WHERE m.componentInspectionItemsTableId = :componentInspectionItemsTableId")
})
public class MstComponentInspectionItemsTableClass implements Serializable {

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
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_INSPECT_CLASS_ID")
    private String componentInspectClassId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DICT_KEY")
    private String dictKey;
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
    @Column(name = "MASS_FLG")
    private Character massFlg;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;

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

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DRAWING_NAME")
    private String drawingName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PROOF_NAME")
    private String proofName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ROHS_PROOF_NAME")
    private String rohsProofName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PACKAGE_SPEC_NAME")
    private String packageSpecName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "QC_PHASE_NAME")
    private String qcPhaseName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_06_NAME")
    private String file06Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_07_NAME")
    private String file07Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_08_NAME")
    private String file08Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_09_NAME")
    private String file09Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_10_NAME")
    private String file10Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_11_NAME")
    private String file11Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_12_NAME")
    private String file12Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_13_NAME")
    private String file13Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_14_NAME")
    private String file14Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_15_NAME")
    private String file15Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_16_NAME")
    private String file16Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_17_NAME")
    private String file17Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_18_NAME")
    private String file18Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_19_NAME")
    private String file19Name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_20_NAME")
    private String file20Name;

    /*
     * 辞書マスタ
     */
    @PrimaryKeyJoinColumn(name = "DICT_KEY", referencedColumnName = "DICT_KEY")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponentInspectLang mstComponentInspectLang;

    public MstComponentInspectionItemsTableClass() {
    }

    public MstComponentInspectionItemsTableClass(String id) {
        this.id = id;
    }

    public MstComponentInspectionItemsTableClass(String id, String componentInspectionItemsTableId, String componentInspectClassId, Character drawingFlg, Character proofFlg, Character rohsProofFlg, Character packageSpecFlg, Character qcPhaseFlg) {
        this.id = id;
        this.componentInspectionItemsTableId = componentInspectionItemsTableId;
        this.componentInspectClassId = componentInspectClassId;
        this.drawingFlg = drawingFlg;
        this.proofFlg = proofFlg;
        this.rohsProofFlg = rohsProofFlg;
        this.packageSpecFlg = packageSpecFlg;
        this.qcPhaseFlg = qcPhaseFlg;
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

    public String getComponentInspectClassId() {
        return componentInspectClassId;
    }

    public void setComponentInspectClassId(String componentInspectClassId) {
        this.componentInspectClassId = componentInspectClassId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstComponentInspectionItemsTableClass)) {
            return false;
        }
        MstComponentInspectionItemsTableClass other = (MstComponentInspectionItemsTableClass) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.inspection.file.MstComponentInspectionItemsTableClass[ id=" + id + " ]";
    }

    /**
     * @return the massFlg
     */
    public Character getMassFlg() {
        return massFlg;
    }

    /**
     * @param massFlg the massFlg to set
     */
    public void setMassFlg(Character massFlg) {
        this.massFlg = massFlg;
    }

    /**
     * @return the dictKey
     */
    public String getDictKey() {
        return dictKey;
    }

    /**
     * @param dictKey the dictKey to set
     */
    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
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

    /**
     * @return the seq
     */
    public int getSeq() {
        return seq;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Character getFile06Flg() { return file06Flg; }

    public void setFile06Flg(Character file06Flg) { this.file06Flg = file06Flg; }

    public Character getFile07Flg() { return file07Flg; }

    public void setFile07Flg(Character file07Flg) { this.file07Flg = file07Flg; }

    public Character getFile08Flg() { return file08Flg; }

    public void setFile08Flg(Character file08Flg) { this.file08Flg = file08Flg; }

    public Character getFile09Flg() { return file09Flg; }

    public void setFile09Flg(Character file09Flg) { this.file09Flg = file09Flg; }

    public Character getFile10Flg() { return file10Flg; }

    public void setFile10Flg(Character file10Flg) { this.file10Flg = file10Flg; }

    public Character getFile11Flg() { return file11Flg; }

    public void setFile11Flg(Character file11Flg) { this.file11Flg = file11Flg; }

    public Character getFile12Flg() { return file12Flg; }

    public void setFile12Flg(Character file12Flg) { this.file12Flg = file12Flg; }

    public Character getFile13Flg() { return file13Flg; }

    public void setFile13Flg(Character file13Flg) { this.file13Flg = file13Flg; }

    public Character getFile14Flg() { return file14Flg; }

    public void setFile14Flg(Character file14Flg) { this.file14Flg = file14Flg; }

    public Character getFile15Flg() { return file15Flg; }

    public void setFile15Flg(Character file15Flg) { this.file15Flg = file15Flg; }

    public Character getFile16Flg() { return file16Flg; }

    public void setFile16Flg(Character file16Flg) { this.file16Flg = file16Flg; }

    public Character getFile17Flg() { return file17Flg; }

    public void setFile17Flg(Character file17Flg) { this.file17Flg = file17Flg; }

    public Character getFile18Flg() { return file18Flg; }

    public void setFile18Flg(Character file18Flg) { this.file18Flg = file18Flg; }

    public Character getFile19Flg() { return file19Flg; }

    public void setFile19Flg(Character file19Flg) { this.file19Flg = file19Flg; }

    public Character getFile20Flg() { return file20Flg; }

    public void setFile20Flg(Character file20Flg) { this.file20Flg = file20Flg; }

    public String getDrawingName() { return drawingName; }

    public void setDrawingName(String drawingName) { this.drawingName = drawingName; }

    public String getProofName() { return proofName; }

    public void setProofName(String proofName) { this.proofName = proofName; }

    public String getRohsProofName() { return rohsProofName; }

    public void setRohsProofName(String rohsProofName) { this.rohsProofName = rohsProofName; }

    public String getPackageSpecName() { return packageSpecName; }

    public void setPackageSpecName(String packageSpecName) { this.packageSpecName = packageSpecName; }

    public String getQcPhaseName() { return qcPhaseName; }

    public void setQcPhaseName(String qcPhaseName) { this.qcPhaseName = qcPhaseName; }

    public String getFile06Name() { return file06Name; }

    public void setFile06Name(String file06Name) { this.file06Name = file06Name; }

    public String getFile07Name() { return file07Name; }

    public void setFile07Name(String file07Name) { this.file07Name = file07Name; }

    public String getFile08Name() { return file08Name; }

    public void setFile08Name(String file08Name) { this.file08Name = file08Name; }

    public String getFile09Name() { return file09Name; }

    public void setFile09Name(String file09Name) { this.file09Name = file09Name; }

    public String getFile10Name() { return file10Name; }

    public void setFile10Name(String file10Name) { this.file10Name = file10Name; }

    public String getFile11Name() { return file11Name; }

    public void setFile11Name(String file11Name) { this.file11Name = file11Name; }

    public String getFile12Name() { return file12Name; }

    public void setFile12Name(String file12Name) { this.file12Name = file12Name; }

    public String getFile13Name() { return file13Name; }

    public void setFile13Name(String file13Name) { this.file13Name = file13Name; }

    public String getFile14Name() { return file14Name; }

    public void setFile14Name(String file14Name) { this.file14Name = file14Name; }

    public String getFile15Name() { return file15Name; }

    public void setFile15Name(String file15Name) { this.file15Name = file15Name; }

    public String getFile16Name() { return file16Name; }

    public void setFile16Name(String file16Name) { this.file16Name = file16Name; }

    public String getFile17Name() { return file17Name; }

    public void setFile17Name(String file17Name) { this.file17Name = file17Name; }

    public String getFile18Name() { return file18Name; }

    public void setFile18Name(String file18Name) { this.file18Name = file18Name; }

    public String getFile19Name() { return file19Name; }

    public void setFile19Name(String file19Name) { this.file19Name = file19Name; }

    public String getFile20Name() { return file20Name; }

    public void setFile20Name(String file20Name) { this.file20Name = file20Name; }
}
