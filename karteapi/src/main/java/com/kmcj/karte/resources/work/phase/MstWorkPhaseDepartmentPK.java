/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work.phase;

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
public class MstWorkPhaseDepartmentPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "WORK_PHASE_ID")
    private String workPhaseId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEPARTMENT")
    private int department;

    public MstWorkPhaseDepartmentPK() {
    }

    public MstWorkPhaseDepartmentPK(String workPhaseId, int department) {
        this.workPhaseId = workPhaseId;
        this.department = department;
    }

    public String getWorkPhaseId() {
        return workPhaseId;
    }

    public void setWorkPhaseId(String workPhaseId) {
        this.workPhaseId = workPhaseId;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (workPhaseId != null ? workPhaseId.hashCode() : 0);
        hash += (int) department;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstWorkPhaseDepartmentPK)) {
            return false;
        }
        MstWorkPhaseDepartmentPK other = (MstWorkPhaseDepartmentPK) object;
        if ((this.workPhaseId == null && other.workPhaseId != null) || (this.workPhaseId != null && !this.workPhaseId.equals(other.workPhaseId))) {
            return false;
        }
        if (this.department != other.department) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.work.phase.MstWorkPhaseDepartmentPK[ workPhaseId=" + workPhaseId + ", department=" + department + " ]";
    }
    
}
