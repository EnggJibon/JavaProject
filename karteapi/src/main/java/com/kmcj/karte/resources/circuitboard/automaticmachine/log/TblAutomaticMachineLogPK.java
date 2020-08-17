/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.automaticmachine.log;

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
 * @author liujiyong
 */
@Embeddable
public class TblAutomaticMachineLogPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EVENT_NO")
    private long eventNo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    public TblAutomaticMachineLogPK() {
    }

    public TblAutomaticMachineLogPK(String machineUuid, long eventNo, Date createDate) {
        this.machineUuid = machineUuid;
        this.eventNo = eventNo;
        this.createDate = createDate;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public long getEventNo() {
        return eventNo;
    }

    public void setEventNo(long eventNo) {
        this.eventNo = eventNo;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (machineUuid != null ? machineUuid.hashCode() : 0);
        hash += (int) eventNo;
        hash += (createDate != null ? createDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblAutomaticMachineLogPK)) {
            return false;
        }
        TblAutomaticMachineLogPK other = (TblAutomaticMachineLogPK) object;
        if ((this.machineUuid == null && other.machineUuid != null) || (this.machineUuid != null && !this.machineUuid.equals(other.machineUuid))) {
            return false;
        }
        if (this.eventNo != other.eventNo) {
            return false;
        }
        if ((this.createDate == null && other.createDate != null) || (this.createDate != null && !this.createDate.equals(other.createDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.circuitboard.automaticmachine.log.TblAutomaticMachineLogPK[ machineUuid=" + machineUuid + ", eventNo=" + eventNo + ", createDate=" + createDate + " ]";
    }
    
}
