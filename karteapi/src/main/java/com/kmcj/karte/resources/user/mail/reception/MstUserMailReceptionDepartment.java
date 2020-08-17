/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.user.mail.reception;

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
 * @author f.kitaoji
 */
@Entity
@Table(name = "mst_user_mail_reception_department")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstUserMailReceptionDepartment.findAll", query = "SELECT m FROM MstUserMailReceptionDepartment m"),
    @NamedQuery(name = "MstUserMailReceptionDepartment.findById", query = "SELECT m FROM MstUserMailReceptionDepartment m WHERE m.id = :id"),
    @NamedQuery(name = "MstUserMailReceptionDepartment.findByUserUuid", query = "SELECT m FROM MstUserMailReceptionDepartment m WHERE m.userUuid = :userUuid"),
    @NamedQuery(name = "MstUserMailReceptionDepartment.findByEventUuid", query = "SELECT m FROM MstUserMailReceptionDepartment m WHERE m.eventUuid = :eventUuid"),
    @NamedQuery(name = "MstUserMailReceptionDepartment.findByBelongingDepartment", query = "SELECT m FROM MstUserMailReceptionDepartment m WHERE m.belongingDepartment = :belongingDepartment"),
    @NamedQuery(name = "MstUserMailReceptionDepartment.findByDepartment", query = "SELECT m FROM MstUserMailReceptionDepartment m WHERE m.department = :department"),
    @NamedQuery(name = "MstUserMailReceptionDepartment.findByCreateDate", query = "SELECT m FROM MstUserMailReceptionDepartment m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstUserMailReceptionDepartment.findByUpdateDate", query = "SELECT m FROM MstUserMailReceptionDepartment m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstUserMailReceptionDepartment.findByCreateUserUuid", query = "SELECT m FROM MstUserMailReceptionDepartment m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstUserMailReceptionDepartment.findByUpdateUserUuid", query = "SELECT m FROM MstUserMailReceptionDepartment m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstUserMailReceptionDepartment.deleteByUserUuid", query = "DELETE FROM MstUserMailReceptionDepartment m WHERE m.userUuid = :userUuid"),
})
@Cacheable(value = false)
public class MstUserMailReceptionDepartment implements Serializable {

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
    @Column(name = "USER_UUID")
    private String userUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "EVENT_UUID")
    private String eventUuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BELONGING_DEPARTMENT")
    private int belongingDepartment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEPARTMENT")
    private int department;
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

    public MstUserMailReceptionDepartment() {
    }

    public MstUserMailReceptionDepartment(String id) {
        this.id = id;
    }

    public MstUserMailReceptionDepartment(String id, String userUuid, String eventUuid, int belongingDepartment, int department) {
        this.id = id;
        this.userUuid = userUuid;
        this.eventUuid = eventUuid;
        this.belongingDepartment = belongingDepartment;
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getEventUuid() {
        return eventUuid;
    }

    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }

    public int getBelongingDepartment() {
        return belongingDepartment;
    }

    public void setBelongingDepartment(int belongingDepartment) {
        this.belongingDepartment = belongingDepartment;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstUserMailReceptionDepartment)) {
            return false;
        }
        MstUserMailReceptionDepartment other = (MstUserMailReceptionDepartment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.user.mail.reception.MstUserMailReceptionDepartment[ id=" + id + " ]";
    }
    
}
