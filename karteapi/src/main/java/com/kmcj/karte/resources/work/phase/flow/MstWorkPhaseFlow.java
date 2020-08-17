/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work.phase.flow;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kmc
 */
@Entity
@Table(name = "mst_work_phase_flow")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstWorkPhaseFlow.findAll", query = "SELECT m FROM MstWorkPhaseFlow m"),
    @NamedQuery(name = "MstWorkPhaseFlow.findByWorkPhaseId", query = "SELECT m FROM MstWorkPhaseFlow m WHERE m.workPhaseId = :workPhaseId"),
    @NamedQuery(name = "MstWorkPhaseFlow.findByNextWorkPhaseId", query = "SELECT m FROM MstWorkPhaseFlow m WHERE m.nextWorkPhaseId = :nextWorkPhaseId"),
    @NamedQuery(name = "MstWorkPhaseFlow.findByCreateDate", query = "SELECT m FROM MstWorkPhaseFlow m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstWorkPhaseFlow.findByUpdateDate", query = "SELECT m FROM MstWorkPhaseFlow m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstWorkPhaseFlow.findByCreateUserUuid", query = "SELECT m FROM MstWorkPhaseFlow m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstWorkPhaseFlow.findByUpdateUserUuid", query = "SELECT m FROM MstWorkPhaseFlow m WHERE m.updateUserUuid = :updateUserUuid"),
    //DELETE
    @NamedQuery(name = "MstWorkPhaseFlow.delete", query = "DELETE FROM MstWorkPhaseFlow m WHERE m.workPhaseId = :workPhaseId"),
})
public class MstWorkPhaseFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "WORK_PHASE_ID")
    private String workPhaseId;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NEXT_WORK_PHASE_ID")
    private String nextWorkPhaseId;
    
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
    
    public MstWorkPhaseFlow() {
    }

    public MstWorkPhaseFlow(String workPhaseId, String nextWorkPhaseId) {
        this.workPhaseId = workPhaseId;
        this.nextWorkPhaseId = nextWorkPhaseId;
    }

    public String getWorkPhaseId() {
        return workPhaseId;
    }

    public void setWorkPhaseId(String workPhaseId) {
        this.workPhaseId = workPhaseId;
    }

    public String getNextWorkPhaseId() {
        return nextWorkPhaseId;
    }

    public void setNextWorkPhaseId(String nextWorkPhaseId) {
        this.nextWorkPhaseId = nextWorkPhaseId;
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
        hash += (workPhaseId != null ? workPhaseId.hashCode() : 0);
        hash += (nextWorkPhaseId != null ? nextWorkPhaseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstWorkPhaseFlow)) {
            return false;
        }
        MstWorkPhaseFlow other = (MstWorkPhaseFlow) object;
        if ((this.workPhaseId == null && other.workPhaseId != null) || (this.workPhaseId != null && !this.workPhaseId.equals(other.workPhaseId))) {
            return false;
        }
        if ((this.nextWorkPhaseId == null && other.nextWorkPhaseId != null) || (this.nextWorkPhaseId != null && !this.nextWorkPhaseId.equals(other.nextWorkPhaseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.work.phase.flow.MstWorkPhaseFlowPK[ workPhaseId=" + workPhaseId + ", nextWorkPhaseId=" + nextWorkPhaseId + " ]";
    }
}
