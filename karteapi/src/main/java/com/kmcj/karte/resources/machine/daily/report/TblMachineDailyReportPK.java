/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.daily.report;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author zds
 */
@Embeddable
public class TblMachineDailyReportPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCTION_ID")
    private String productionId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTION_DATE")
    @Temporal(TemporalType.DATE)
    private Date productionDate;

    public TblMachineDailyReportPK() {
    }

    public TblMachineDailyReportPK(String productionId, Date productionDate) {
        this.productionId = productionId;
        this.productionDate = productionDate;
    }

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productionId != null ? productionId.hashCode() : 0);
        hash += (productionDate != null ? productionDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineDailyReportPK)) {
            return false;
        }
        TblMachineDailyReportPK other = (TblMachineDailyReportPK) object;
        if ((this.productionId == null && other.productionId != null) || (this.productionId != null && !this.productionId.equals(other.productionId))) {
            return false;
        }
        if ((this.productionDate == null && other.productionDate != null) || (this.productionDate != null && !this.productionDate.equals(other.productionDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.daily.report.TblMachineDailyReportPK[ productionId=" + productionId + ", productionDate=" + productionDate + " ]";
    }
    
}
