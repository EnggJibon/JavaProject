package com.kmcj.karte.resources.machine.proccond;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.machine.*;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * @author admin
 */
@Entity
@Table(name = "mst_machine_proc_cond")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMachineProcCond.findAll", query = "SELECT m FROM MstMachineProcCond m"),
    @NamedQuery(name = "MstMachineProcCond.findById", query = "SELECT m FROM MstMachineProcCond m WHERE m.id = :id"),
    @NamedQuery(name = "MstMachineProcCond.findBySeq", query = "SELECT m FROM MstMachineProcCond m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstMachineProcCond.findByCreateDate", query = "SELECT m FROM MstMachineProcCond m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMachineProcCond.findByUpdateDate", query = "SELECT m FROM MstMachineProcCond m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMachineProcCond.findByCreateUserUuid", query = "SELECT m FROM MstMachineProcCond m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMachineProcCond.findByUpdateUserUuid", query = "SELECT m FROM MstMachineProcCond m WHERE m.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class MstMachineProcCond implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
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
    @JoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    @Column(name = "COMPONENT_ID")
    private String componentId;

    public MstMachineProcCond() {
    }

    public MstMachineProcCond(String id) {
        this.id = id;
    }

    public MstMachineProcCond(String id, int seq) {
        this.id = id;
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
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

    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public MstMachine getMstMachine() {
        return mstMachine;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public MstComponent getMstComponent() {
        return mstComponent;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMachineProcCond)) {
            return false;
        }
        MstMachineProcCond other = (MstMachineProcCond) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.MstMachineProcCond[ id=" + id + " ]";
    }
}
