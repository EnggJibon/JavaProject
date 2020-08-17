package com.kmcj.karte.resources.machine.history;

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
public class TblMachineHistoryPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FIRST_EVENT_NO")
    private long firstEventNo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    public TblMachineHistoryPK() {
    }

    public TblMachineHistoryPK(String machineUuid, long firstEventNo, Date startDate) {
        this.machineUuid = machineUuid;
        this.firstEventNo = firstEventNo;
        this.startDate = startDate;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public long getFirstEventNo() {
        return firstEventNo;
    }

    public void setFirstEventNo(long firstEventNo) {
        this.firstEventNo = firstEventNo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (machineUuid != null ? machineUuid.hashCode() : 0);
        hash += (int) firstEventNo;
        hash += (startDate != null ? startDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineHistoryPK)) {
            return false;
        }
        TblMachineHistoryPK other = (TblMachineHistoryPK) object;
        if ((this.machineUuid == null && other.machineUuid != null) || (this.machineUuid != null && !this.machineUuid.equals(other.machineUuid))) {
            return false;
        }
        if (this.firstEventNo != other.firstEventNo) {
            return false;
        }
        if ((this.startDate == null && other.startDate != null) || (this.startDate != null && !this.startDate.equals(other.startDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.history.TblMachineHistoryPK[ machineUuid=" + machineUuid + ", firstEventNo=" + firstEventNo + ", startDate=" + startDate + " ]";
    }
    
}
