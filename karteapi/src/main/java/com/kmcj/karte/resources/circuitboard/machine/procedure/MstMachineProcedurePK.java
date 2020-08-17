/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.machine.procedure;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author liujiyong
 */
@Embeddable
public class MstMachineProcedurePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PROCEDURE_ID")
    private String procedureId;

    public MstMachineProcedurePK() {
    }

    public MstMachineProcedurePK(String machineUuid, String procedureId) {
        this.machineUuid = machineUuid;
        this.procedureId = procedureId;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public String getProcedureId() {
        return procedureId;
    }

    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (machineUuid != null ? machineUuid.hashCode() : 0);
        hash += (procedureId != null ? procedureId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMachineProcedurePK)) {
            return false;
        }
        MstMachineProcedurePK other = (MstMachineProcedurePK) object;
        if ((this.machineUuid == null && other.machineUuid != null) || (this.machineUuid != null && !this.machineUuid.equals(other.machineUuid))) {
            return false;
        }
        if ((this.procedureId == null && other.procedureId != null) || (this.procedureId != null && !this.procedureId.equals(other.procedureId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.machine.procedure.MstMachineProcedurePK[ machineUuid=" + machineUuid + ", procedureId=" + procedureId + " ]";
    }
    
}
