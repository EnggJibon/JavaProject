/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.operating.rate;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Embeddable
public class TblMachineOperatingRateForMonthPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "PRODUCTION_MONTH")
    private String productionMonth;

    public TblMachineOperatingRateForMonthPK() {
    }

    public TblMachineOperatingRateForMonthPK(String machineUuid, String productionMonth) {
        this.machineUuid = machineUuid;
        this.productionMonth = productionMonth;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public String getProductionMonth() {
        return productionMonth;
    }

    public void setProductionMonth(String productionMonth) {
        this.productionMonth = productionMonth;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (machineUuid != null ? machineUuid.hashCode() : 0);
        hash += (productionMonth != null ? productionMonth.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineOperatingRateForMonthPK)) {
            return false;
        }
        TblMachineOperatingRateForMonthPK other = (TblMachineOperatingRateForMonthPK) object;
        if ((this.machineUuid == null && other.machineUuid != null) || (this.machineUuid != null && !this.machineUuid.equals(other.machineUuid))) {
            return false;
        }
        if ((this.productionMonth == null && other.productionMonth != null) || (this.productionMonth != null && !this.productionMonth.equals(other.productionMonth))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateForMonthPK[ machineUuid=" + machineUuid + ", productionMonth=" + productionMonth + " ]";
    }
    
}
