/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.authorization;

import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "mst_auth")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstAuth.findAll", query = "SELECT m FROM MstAuth m ORDER BY m.seq"),
    @NamedQuery(name = "MstAuth.findById", query = "SELECT m FROM MstAuth m WHERE m.id = :id"),
    @NamedQuery(name = "MstAuth.findByGroupName", query = "SELECT m FROM MstAuth m WHERE m.groupName = :groupName"),
    @NamedQuery(name = "MstAuth.findByRemarks", query = "SELECT m FROM MstAuth m WHERE m.remarks = :remarks"),
    @NamedQuery(name = "MstAuth.findBySeq", query = "SELECT m FROM MstAuth m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstAuth.findByCreateDate", query = "SELECT m FROM MstAuth m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstAuth.findByUpdateDate", query = "SELECT m FROM MstAuth m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstAuth.findByCreateUserUuid", query = "SELECT m FROM MstAuth m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstAuth.findByUpdateUserUuid", query = "SELECT m FROM MstAuth m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstAuth.delete", query = "DELETE FROM MstAuth m WHERE m.id = :id"),
    @NamedQuery(name = "MstAuth.update", query = 
        "UPDATE MstAuth m SET m.groupName = :groupName, m.seq = :seq, m.systemDefault = :systemDefault, m.updateDate = :updateDate, m.updateUserUuid = :updateUserUuid WHERE m.id = :id")
})
@Cacheable(value = false)
public class MstAuth implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "SYSTEM_DEFAULT")
    private int systemDefault;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 100)
    @Column(name = "GROUP_NAME")
    private String groupName;
    @Size(max = 200)
    @Column(name = "REMARKS")
    private String remarks;
    @Column(name = "SEQ")
    private Integer seq;
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
    private boolean deleted = false;
    @Transient
    private boolean modified = false;
    @Transient
    private boolean added = false;

    public MstAuth() {
    }

    public MstAuth(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstAuth)) {
            return false;
        }
        MstAuth other = (MstAuth) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.authorization.MstAuth[ id=" + id + " ]";
    }

    public int getSystemDefault() {
        return systemDefault;
    }

    public void setSystemDefault(int systemDefault) {
        this.systemDefault = systemDefault;
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

    
}
