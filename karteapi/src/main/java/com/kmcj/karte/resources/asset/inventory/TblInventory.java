/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_inventory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblInventory.findAll", query = "SELECT t FROM TblInventory t"),
    @NamedQuery(name = "TblInventory.findByUuid", query = "SELECT t FROM TblInventory t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TblInventory.deleteByUuid", query = "DELETE FROM TblInventory t WHERE t.uuid = :uuid")
})
@Cacheable(value = false)
public class TblInventory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MGMT_COMPANY_COUNT")
    private int mgmtCompanyCount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RECEIVED_MGMT_COMPANY_COUNT")
    private int receivedMgmtCompanyCount;
    @Column(name = "REGISTRATION_DATE")
    @Temporal(TemporalType.DATE)
    private Date registrationDate;
    @Column(name = "REQUESTED_DATE")
    @Temporal(TemporalType.DATE)
    private Date requestedDate;
    @Column(name = "FINAL_DUE_DATE")
    @Temporal(TemporalType.DATE)
    private Date finalDueDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private int status;
    @Column(name = "COMPLETED_DATE")
    @Temporal(TemporalType.DATE)
    private Date completedDate;
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

    public TblInventory() {
    }

    public TblInventory(String uuid) {
        this.uuid = uuid;
    }

    public TblInventory(String uuid, String name, int mgmtCompanyCount, int receivedMgmtCompanyCount, int status) {
        this.uuid = uuid;
        this.name = name;
        this.mgmtCompanyCount = mgmtCompanyCount;
        this.receivedMgmtCompanyCount = receivedMgmtCompanyCount;
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMgmtCompanyCount() {
        return mgmtCompanyCount;
    }

    public void setMgmtCompanyCount(int mgmtCompanyCount) {
        this.mgmtCompanyCount = mgmtCompanyCount;
    }

    public int getReceivedMgmtCompanyCount() {
        return receivedMgmtCompanyCount;
    }

    public void setReceivedMgmtCompanyCount(int receivedMgmtCompanyCount) {
        this.receivedMgmtCompanyCount = receivedMgmtCompanyCount;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Date getFinalDueDate() {
        return finalDueDate;
    }

    public void setFinalDueDate(Date finalDueDate) {
        this.finalDueDate = finalDueDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
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
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblInventory)) {
            return false;
        }
        TblInventory other = (TblInventory) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.inventory.TblInventory[ uuid=" + uuid + " ]";
    }
    
}
