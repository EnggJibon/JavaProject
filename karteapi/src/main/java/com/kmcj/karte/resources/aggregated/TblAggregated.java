/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.aggregated;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
 * @author admin
 */
@Entity
@Table(name = "tbl_aggregated")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblAggregated.findByMacReportDetailId", query = "SELECT t FROM TblAggregated t WHERE t.macReportDetailId = :macReportDetailId"),
    @NamedQuery(name = "TblAggregated.findByProductionDate", query = "SELECT t FROM TblAggregated t WHERE t.productionDate = :productionDate")
})
@Cacheable(value = false)
public class TblAggregated implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MAC_REPORT_DETAIL_ID")
    private String macReportDetailId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTION_DATE")
    @Temporal(TemporalType.DATE)
    private Date productionDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPLETED_COUNT")
    private int completedCount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UPD_DELETE_FLG")
    private int updDeleteFlg;

    public TblAggregated() {
    }

    public TblAggregated(String macReportDetailId) {
        this.macReportDetailId = macReportDetailId;
    }

    public TblAggregated(String macReportDetailId, String id, Date productionDate, int completedCount, int updDeleteFlg) {
        this.macReportDetailId = macReportDetailId;
        this.id = id;
        this.productionDate = productionDate;
        this.completedCount = completedCount;
        this.updDeleteFlg = updDeleteFlg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMacReportDetailId() {
        return macReportDetailId;
    }

    public void setMacReportDetailId(String macReportDetailId) {
        this.macReportDetailId = macReportDetailId;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public int getUpdDeleteFlg() {
        return updDeleteFlg;
    }

    public void setUpdDeleteFlg(int updDeleteFlg) {
        this.updDeleteFlg = updDeleteFlg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (macReportDetailId != null ? macReportDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblAggregated)) {
            return false;
        }
        TblAggregated other = (TblAggregated) object;
        if ((this.macReportDetailId != null && !this.macReportDetailId.equals(other.macReportDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.aggregated.TblAggregated[ macReportDetailId=" + macReportDetailId + " ]";
    }

}
