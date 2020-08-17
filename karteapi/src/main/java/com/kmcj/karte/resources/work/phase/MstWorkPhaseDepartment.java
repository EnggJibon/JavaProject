/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work.phase;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_work_phase_department")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstWorkPhaseDepartment.findAll", query = "SELECT m FROM MstWorkPhaseDepartment m"),
    @NamedQuery(name = "MstWorkPhaseDepartment.findByWorkPhaseId", query = "SELECT m FROM MstWorkPhaseDepartment m WHERE m.mstWorkPhaseDepartmentPK.workPhaseId = :workPhaseId"),
    @NamedQuery(name = "MstWorkPhaseDepartment.findIdsByWorkPhaseId", query = "SELECT m.mstWorkPhaseDepartmentPK.department FROM MstWorkPhaseDepartment m WHERE m.mstWorkPhaseDepartmentPK.workPhaseId = :workPhaseId"),
    @NamedQuery(name = "MstWorkPhaseDepartment.findByDepartment", query = "SELECT m FROM MstWorkPhaseDepartment m WHERE m.mstWorkPhaseDepartmentPK.department = :department"),
    @NamedQuery(name = "MstWorkPhaseDepartment.findByCreateDate", query = "SELECT m FROM MstWorkPhaseDepartment m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstWorkPhaseDepartment.findByUpdateDate", query = "SELECT m FROM MstWorkPhaseDepartment m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstWorkPhaseDepartment.findByCreateUserUuid", query = "SELECT m FROM MstWorkPhaseDepartment m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstWorkPhaseDepartment.findByUpdateUserUuid", query = "SELECT m FROM MstWorkPhaseDepartment m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstWorkPhaseDepartment.deleteByWorkPhaseId", query = "DELETE FROM MstWorkPhaseDepartment m WHERE m.mstWorkPhaseDepartmentPK.workPhaseId = :workPhaseId "),
})
public class MstWorkPhaseDepartment implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstWorkPhaseDepartmentPK mstWorkPhaseDepartmentPK;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;

    public MstWorkPhaseDepartment() {
    }

    public MstWorkPhaseDepartment(MstWorkPhaseDepartmentPK mstWorkPhaseDepartmentPK) {
        this.mstWorkPhaseDepartmentPK = mstWorkPhaseDepartmentPK;
    }

    public MstWorkPhaseDepartment(String workPhaseId, int department) {
        this.mstWorkPhaseDepartmentPK = new MstWorkPhaseDepartmentPK(workPhaseId, department);
    }

    public MstWorkPhaseDepartmentPK getMstWorkPhaseDepartmentPK() {
        return mstWorkPhaseDepartmentPK;
    }

    public void setMstWorkPhaseDepartmentPK(MstWorkPhaseDepartmentPK mstWorkPhaseDepartmentPK) {
        this.mstWorkPhaseDepartmentPK = mstWorkPhaseDepartmentPK;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mstWorkPhaseDepartmentPK != null ? mstWorkPhaseDepartmentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstWorkPhaseDepartment)) {
            return false;
        }
        MstWorkPhaseDepartment other = (MstWorkPhaseDepartment) object;
        if ((this.mstWorkPhaseDepartmentPK == null && other.mstWorkPhaseDepartmentPK != null) || (this.mstWorkPhaseDepartmentPK != null && !this.mstWorkPhaseDepartmentPK.equals(other.mstWorkPhaseDepartmentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.work.phase.MstWorkPhaseDepartment[ mstWorkPhaseDepartmentPK=" + mstWorkPhaseDepartmentPK + " ]";
    }
    
}
