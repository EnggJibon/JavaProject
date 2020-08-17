/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.operating.rate;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "tbl_machine_operating_rate_recalc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineOperatingRateRecalc.findAll", query = "SELECT t FROM TblMachineOperatingRateRecalc t"),
    @NamedQuery(name = "TblMachineOperatingRateRecalc.findByMachineUuid", query = "SELECT t FROM TblMachineOperatingRateRecalc t WHERE t.machineUuid = :machineUuid")})
public class TblMachineOperatingRateRecalc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;

    public TblMachineOperatingRateRecalc() {
    }

    public TblMachineOperatingRateRecalc(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (machineUuid != null ? machineUuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineOperatingRateRecalc)) {
            return false;
        }
        TblMachineOperatingRateRecalc other = (TblMachineOperatingRateRecalc) object;
        if ((this.machineUuid == null && other.machineUuid != null) || (this.machineUuid != null && !this.machineUuid.equals(other.machineUuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateRecalc[ machineUuid=" + machineUuid + " ]";
    }
    
}
