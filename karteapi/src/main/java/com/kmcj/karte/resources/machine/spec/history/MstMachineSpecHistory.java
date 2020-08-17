package com.kmcj.karte.resources.machine.spec.history;

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
@Table(name = "mst_machine_spec_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMachineSpecHistory.findAll", query = "SELECT m FROM MstMachineSpecHistory m"),
    @NamedQuery(name = "MstMachineSpecHistory.findById", query = "SELECT m FROM MstMachineSpecHistory m WHERE m.id = :id"),
    @NamedQuery(name = "MstMachineSpecHistory.findByStartDate", query = "SELECT m FROM MstMachineSpecHistory m WHERE m.startDate = :startDate"),
    @NamedQuery(name = "MstMachineSpecHistory.findByEndDate", query = "SELECT m FROM MstMachineSpecHistory m WHERE m.endDate = :endDate"),
    @NamedQuery(name = "MstMachineSpecHistory.findByMachineSpecName", query = "SELECT m FROM MstMachineSpecHistory m WHERE m.machineSpecName = :machineSpecName"),
    @NamedQuery(name = "MstMachineSpecHistory.findByCreateDate", query = "SELECT m FROM MstMachineSpecHistory m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMachineSpecHistory.findByUpdateDate", query = "SELECT m FROM MstMachineSpecHistory m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMachineSpecHistory.findByCreateUserUuid", query = "SELECT m FROM MstMachineSpecHistory m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMachineSpecHistory.findByUpdateUserUuid", query = "SELECT m FROM MstMachineSpecHistory m WHERE m.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class MstMachineSpecHistory implements Serializable {

    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Size(max = 50)
    @Column(name = "MACHINE_SPEC_NAME")
    private String machineSpecName;
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
    
    public MstMachineSpecHistory() {
    }

    public MstMachineSpecHistory(String id) {
        this.id = id;
    }

    public MstMachineSpecHistory(String id, Date startDate, Date endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getMachineSpecName() {
        return machineSpecName;
    }

    public void setMachineSpecName(String machineSpecName) {
        this.machineSpecName = machineSpecName;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMachineSpecHistory)) {
            return false;
        }
        MstMachineSpecHistory other = (MstMachineSpecHistory) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.MstMachineSpecHistory[ id=" + getId() + " ]";
    }
}
