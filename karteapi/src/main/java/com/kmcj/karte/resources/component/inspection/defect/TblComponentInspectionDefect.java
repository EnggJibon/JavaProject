package com.kmcj.karte.resources.component.inspection.defect;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author penggd
 */
@Entity
@Table(name = "tbl_component_inspection_defect")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblComponentInspectionDefect.findAll", query = "SELECT t FROM TblComponentInspectionDefect t"),
    @NamedQuery(name = "TblComponentInspectionDefect.findById", query = "SELECT t FROM TblComponentInspectionDefect t WHERE t.id = :id"),
    @NamedQuery(name = "TblComponentInspectionDefect.findByComponentInspectionResultId", query = "SELECT t FROM TblComponentInspectionDefect t WHERE t.componentInspectionResultId = :componentInspectionResultId ORDER BY t.quantity DESC"),
    @NamedQuery(name = "TblComponentInspectionDefect.findByDefectSeq", query = "SELECT t FROM TblComponentInspectionDefect t WHERE t.defectSeq = :defectSeq"),
    @NamedQuery(name = "TblComponentInspectionDefect.findByMType", query = "SELECT t FROM TblComponentInspectionDefect t WHERE t.mType = :mType"),
    @NamedQuery(name = "TblComponentInspectionDefect.findByQuantity", query = "SELECT t FROM TblComponentInspectionDefect t WHERE t.quantity = :quantity"),
    @NamedQuery(name = "TblComponentInspectionDefect.deleteById", query = "DELETE FROM TblComponentInspectionDefect t WHERE t.id = :id")})
public class TblComponentInspectionDefect implements Serializable {

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
    @Column(name = "DEFECT_SEQ")
    private int defectSeq;
    @Basic(optional = false)
    @NotNull
    @Column(name = "4M_TYPE")
    private int mType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private int quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CAVITY_NUM")
    private Integer cavityNum;

    @Transient
    private String defectSeqName;

    @Transient
    private int quantityByGropu;

    @Transient
    private String pareto;

    @Transient
    private String mTypeName;

    public TblComponentInspectionDefect() {
    }

    public TblComponentInspectionDefect(String id) {
        this.id = id;
    }

    public TblComponentInspectionDefect(String id, int defectSeq, int mType, int quantity) {
        this.id = id;
        this.defectSeq = defectSeq;
        this.mType = mType;
        this.quantity = quantity;
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

    public int getDefectSeq() {
        return defectSeq;
    }

    public void setDefectSeq(int defectSeq) {
        this.defectSeq = defectSeq;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public String getDefectSeqName() {
        return defectSeqName;
    }

    public void setDefectSeqName(String defectSeqName) {
        this.defectSeqName = defectSeqName;
    }

    public int getQuantityByGropu() {
        return quantityByGropu;
    }

    public void setQuantityByGropu(int quantityByGropu) {
        this.quantityByGropu = quantityByGropu;
    }

    public String getPareto() {
        return pareto;
    }

    public void setPareto(String pareto) {
        this.pareto = pareto;
    }

    public String getmTypeName() {
        return mTypeName;
    }

    public void setmTypeName(String mTypeName) {
        this.mTypeName = mTypeName;
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
        if (!(object instanceof TblComponentInspectionDefect)) {
            return false;
        }
        TblComponentInspectionDefect other = (TblComponentInspectionDefect) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.inspection.defect.TblComponentInspectionDefect[ id=" + id + " ]";
    }

}
