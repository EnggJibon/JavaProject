/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.downtime;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "mst_machine_downtime")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMachineDowntime.deleteByKey", query = "DELETE FROM MstMachineDowntime m WHERE m.id = :id "),
    @NamedQuery(name = "MstMachineDowntime.findAll", query = "SELECT m FROM MstMachineDowntime m ORDER BY m.downtimeCode "),
    @NamedQuery(name = "MstMachineDowntime.findById", query = "SELECT m FROM MstMachineDowntime m WHERE m.id = :id"),
    @NamedQuery(name = "MstMachineDowntime.findByDowntimeCode", query = "SELECT m FROM MstMachineDowntime m WHERE m.downtimeCode = :downtimeCode"),
    @NamedQuery(name = "MstMachineDowntime.findByDowntimeReason", query = "SELECT m FROM MstMachineDowntime m WHERE m.downtimeReason = :downtimeReason"),
    @NamedQuery(name = "MstMachineDowntime.findByPlannedFlg", query = "SELECT m FROM MstMachineDowntime m WHERE m.plannedFlg = :plannedFlg")})
@Cacheable(value = false)
public class MstMachineDowntime implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DOWNTIME_CODE")
    private String downtimeCode;
    @Size(max = 50)
    @Column(name = "DOWNTIME_REASON")
    private String downtimeReason;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PLANNED_FLG")
    private int plannedFlg;
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

    @Transient
    private boolean deleted = false;    // 削除対象制御
    @Transient
    private boolean modified = false;   // 更新対象制御
    @Transient
    private boolean added = false;      // 登録対象制御
    

    public MstMachineDowntime() {
    }

    public MstMachineDowntime(String id) {
        this.id = id;
    }

    public MstMachineDowntime(String id, String downtimeCode, int plannedFlg) {
        this.id = id;
        this.downtimeCode = downtimeCode;
        this.plannedFlg = plannedFlg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDowntimeCode() {
        return downtimeCode;
    }

    public void setDowntimeCode(String downtimeCode) {
        this.downtimeCode = downtimeCode;
    }

    public String getDowntimeReason() {
        return downtimeReason;
    }

    public void setDowntimeReason(String downtimeReason) {
        this.downtimeReason = downtimeReason;
    }

    public int getPlannedFlg() {
        return plannedFlg;
    }

    public void setPlannedFlg(int plannedFlg) {
        this.plannedFlg = plannedFlg;
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
        if (!(object instanceof MstMachineDowntime)) {
            return false;
        }
        MstMachineDowntime other = (MstMachineDowntime) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.downtime.MstMachineDowntime[ id=" + id + " ]";
    }

    /**
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * @return the added
     */
    public boolean isAdded() {
        return added;
    }

    /**
     * @param added the added to set
     */
    public void setAdded(boolean added) {
        this.added = added;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the createUserUuid
     */
    public String getCreateUserUuid() {
        return createUserUuid;
    }

    /**
     * @param createUserUuid the createUserUuid to set
     */
    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    /**
     * @return the updateUserUuid
     */
    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    /**
     * @param updateUserUuid the updateUserUuid to set
     */
    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }
    
}
