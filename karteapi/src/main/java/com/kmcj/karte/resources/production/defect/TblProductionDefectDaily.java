/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.defect;

import com.kmcj.karte.resources.machine.dailyreport2.TblMachineDailyReport2ProdDetail;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 */
@Entity
@Table(name = "tbl_production_defect_daily")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblProductionDefectDaily.findAll", query = "SELECT t FROM TblProductionDefectDaily t"),
    @NamedQuery(name = "TblProductionDefectDaily.findById", query = "SELECT t FROM TblProductionDefectDaily t WHERE t.id = :id"),
    @NamedQuery(name = "TblProductionDefectDaily.findByDefectSeq", query = "SELECT t FROM TblProductionDefectDaily t WHERE t.defectSeq = :defectSeq"),
    @NamedQuery(name = "TblProductionDefectDaily.findByQuantity", query = "SELECT t FROM TblProductionDefectDaily t WHERE t.quantity = :quantity"),
    @NamedQuery(name = "TblProductionDefectDaily.findByMdr2ProdDetailId", query = "SELECT t FROM TblProductionDefectDaily t WHERE t.mdr2ProdDetailId = :mdr2ProdDetailId"),
    @NamedQuery(name = "TblProductionDefectDaily.findByCreateDate", query = "SELECT t FROM TblProductionDefectDaily t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblProductionDefectDaily.findByUpdateDate", query = "SELECT t FROM TblProductionDefectDaily t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblProductionDefectDaily.findByCreateUserUuid", query = "SELECT t FROM TblProductionDefectDaily t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblProductionDefectDaily.findByUpdateUserUuid", query = "SELECT t FROM TblProductionDefectDaily t WHERE t.updateUserUuid = :updateUserUuid")})
public class TblProductionDefectDaily implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTION_DEFECT_ID")
    private String productionDetectId;
    @Column(name = "DEFECT_DATE")
    @Temporal(TemporalType.DATE)
    private Date defectDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEFECT_SEQ")
    private int defectSeq;
    @Basic(optional = false)
    @NotNull
    @Column(name = "QUANTITY")
    private int quantity;
    @Basic(optional = false)
    @Column(name = "MDR2_PROD_DETAIL_ID")
    private String mdr2ProdDetailId;
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
    @JoinColumn(name = "PRODUCTION_DEFECT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TblProductionDefect productionDefectId;
    @JoinColumn(name = "MDR2_PROD_DETAIL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TblMachineDailyReport2ProdDetail reportProdDetail;
    
    public TblProductionDefectDaily() {
    }

    public TblProductionDefectDaily(String id) {
        this.id = id;
    }

    public TblProductionDefectDaily(String id, int defectSeq, int quantity) {
        this.id = id;
        this.defectSeq = defectSeq;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getProductionDetectId() {
        return productionDetectId;
    }

    public void setProductionDetectId(String productionDetectId) {
        this.productionDetectId = productionDetectId;
    }
    
    public Date getDefectDate() {
        return defectDate;
    }

    public void setDefectDate(Date defectDate) {
        this.defectDate = defectDate;
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

    public String getMdr2ProdDetailId() {
        return mdr2ProdDetailId;
    }

    public void setMdr2ProdDetailId(String mdr2ProdDetailId) {
        this.mdr2ProdDetailId = mdr2ProdDetailId;
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

    public TblProductionDefect getProductionDefectId() {
        return productionDefectId;
    }

    public void setProductionDefectId(TblProductionDefect productionDefectId) {
        this.productionDefectId = productionDefectId;
    }

    @XmlTransient
    public TblMachineDailyReport2ProdDetail getReportProdDetail() {
        return reportProdDetail;
    }

    public void setReportProdDetail(TblMachineDailyReport2ProdDetail reportProdDetail) {
        this.reportProdDetail = reportProdDetail;
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
        if (!(object instanceof TblProductionDefectDaily)) {
            return false;
        }
        TblProductionDefectDaily other = (TblProductionDefectDaily) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.production.defect.TblProductionDefectDaily[ id=" + id + " ]";
    }
    
}
