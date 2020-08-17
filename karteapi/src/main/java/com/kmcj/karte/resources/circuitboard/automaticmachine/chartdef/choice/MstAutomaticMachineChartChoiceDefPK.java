/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.automaticmachine.chartdef.choice;

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
public class MstAutomaticMachineChartChoiceDefPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "AUTOMATIC_MACHINE_TYPE")
    private String automaticMachineType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "LOG_TYPE")
    private String logType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;

    public MstAutomaticMachineChartChoiceDefPK() {
    }

    public MstAutomaticMachineChartChoiceDefPK(String automaticMachineType, String logType, int seq) {
        this.automaticMachineType = automaticMachineType;
        this.logType = logType;
        this.seq = seq;
    }

    public String getAutomaticMachineType() {
        return automaticMachineType;
    }

    public void setAutomaticMachineType(String automaticMachineType) {
        this.automaticMachineType = automaticMachineType;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (automaticMachineType != null ? automaticMachineType.hashCode() : 0);
        hash += (logType != null ? logType.hashCode() : 0);
        hash += (int) seq;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstAutomaticMachineChartChoiceDefPK)) {
            return false;
        }
        MstAutomaticMachineChartChoiceDefPK other = (MstAutomaticMachineChartChoiceDefPK) object;
        if ((this.automaticMachineType == null && other.automaticMachineType != null) || (this.automaticMachineType != null && !this.automaticMachineType.equals(other.automaticMachineType))) {
            return false;
        }
        if ((this.logType == null && other.logType != null) || (this.logType != null && !this.logType.equals(other.logType))) {
            return false;
        }
        if (this.seq != other.seq) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.chartdef.choice.MstAutomaticMachineChartChoiceDefPK[ automaticMachineType=" + automaticMachineType + ", logType=" + logType + ", seq=" + seq + " ]";
    }
    
}
