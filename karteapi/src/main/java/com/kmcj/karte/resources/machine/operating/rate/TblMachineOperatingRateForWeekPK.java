/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.operating.rate;

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
 * @author admin
 */
@Embeddable
public class TblMachineOperatingRateForWeekPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTION_DATE_START")
    @Temporal(TemporalType.DATE)
    private Date productionDateStart;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTION_DATE_END")
    @Temporal(TemporalType.DATE)
    private Date productionDateEnd;

    public TblMachineOperatingRateForWeekPK() {
    }

    public TblMachineOperatingRateForWeekPK(String machineUuid, Date productionDateStart, Date productionDateEnd) {
        this.machineUuid = machineUuid;
        this.productionDateStart = productionDateStart;
        this.productionDateEnd = productionDateEnd;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public Date getProductionDateStart() {
        return productionDateStart;
    }

    public void setProductionDateStart(Date productionDateStart) {
        this.productionDateStart = productionDateStart;
    }

    public Date getProductionDateEnd() {
        return productionDateEnd;
    }

    public void setProductionDateEnd(Date productionDateEnd) {
        this.productionDateEnd = productionDateEnd;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (machineUuid != null ? machineUuid.hashCode() : 0);
        hash += (productionDateStart != null ? productionDateStart.hashCode() : 0);
        hash += (productionDateEnd != null ? productionDateEnd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineOperatingRateForWeekPK)) {
            return false;
        }
        TblMachineOperatingRateForWeekPK other = (TblMachineOperatingRateForWeekPK) object;
        if ((this.machineUuid == null && other.machineUuid != null) || (this.machineUuid != null && !this.machineUuid.equals(other.machineUuid))) {
            return false;
        }
        if ((this.productionDateStart == null && other.productionDateStart != null) || (this.productionDateStart != null && !this.productionDateStart.equals(other.productionDateStart))) {
            return false;
        }
        if ((this.productionDateEnd == null && other.productionDateEnd != null) || (this.productionDateEnd != null && !this.productionDateEnd.equals(other.productionDateEnd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateForWeekPK[ machineUuid=" + machineUuid + ", productionDateStart=" + productionDateStart + ", productionDateEnd=" + productionDateEnd + " ]";
    }
    
}
