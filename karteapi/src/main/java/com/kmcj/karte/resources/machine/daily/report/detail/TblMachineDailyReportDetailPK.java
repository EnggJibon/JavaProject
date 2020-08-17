/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.daily.report.detail;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author zds
 */
@Embeddable
public class TblMachineDailyReportDetailPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MAC_REPORT_ID")
    private String macReportId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCTION_DETAIL_ID")
    private String productionDetailId;

    public TblMachineDailyReportDetailPK() {
    }

    public TblMachineDailyReportDetailPK(String macReportId, String productionDetailId) {
        this.macReportId = macReportId;
        this.productionDetailId = productionDetailId;
    }

    public String getMacReportId() {
        return macReportId;
    }

    public void setMacReportId(String macReportId) {
        this.macReportId = macReportId;
    }

    public String getProductionDetailId() {
        return productionDetailId;
    }

    public void setProductionDetailId(String productionDetailId) {
        this.productionDetailId = productionDetailId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (macReportId != null ? macReportId.hashCode() : 0);
        hash += (productionDetailId != null ? productionDetailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineDailyReportDetailPK)) {
            return false;
        }
        TblMachineDailyReportDetailPK other = (TblMachineDailyReportDetailPK) object;
        if ((this.macReportId == null && other.macReportId != null) || (this.macReportId != null && !this.macReportId.equals(other.macReportId))) {
            return false;
        }
        if ((this.productionDetailId == null && other.productionDetailId != null) || (this.productionDetailId != null && !this.productionDetailId.equals(other.productionDetailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.daily.report.detail.TblMachineDailyReportDetailPK[ macReportId=" + macReportId + ", productionDetailId=" + productionDetailId + " ]";
    }
    
}
