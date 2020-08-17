/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work.close.entry;

import com.kmcj.karte.util.XmlDateAdapter2;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 作業入力締めエンティティ
 * @author t.ariki
 */
@Entity
@Table(name = "tbl_work_close_entry")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblWorkCloseEntry.findAll", query = "SELECT t FROM TblWorkCloseEntry t"),
    @NamedQuery(name = "TblWorkCloseEntry.findById", query = "SELECT t FROM TblWorkCloseEntry t WHERE t.id = :id"),
    @NamedQuery(name = "TblWorkCloseEntry.findByClosedDate", query = "SELECT t FROM TblWorkCloseEntry t WHERE t.closedDate = :closedDate"),
    @NamedQuery(name = "TblWorkCloseEntry.findByLatestFlg", query = "SELECT t FROM TblWorkCloseEntry t WHERE t.latestFlg = :latestFlg"),
    @NamedQuery(name = "TblWorkCloseEntry.findByCreateDate", query = "SELECT t FROM TblWorkCloseEntry t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblWorkCloseEntry.findByUpdateDate", query = "SELECT t FROM TblWorkCloseEntry t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblWorkCloseEntry.findByCreateUserUuid", query = "SELECT t FROM TblWorkCloseEntry t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblWorkCloseEntry.findByUpdateUserUuid", query = "SELECT t FROM TblWorkCloseEntry t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblWorkCloseEntry implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "CLOSED_DATE")
    @Temporal(TemporalType.DATE)
    @XmlJavaTypeAdapter(XmlDateAdapter2.class)
    private Date closedDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LATEST_FLG")
    private int latestFlg;
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

    public TblWorkCloseEntry() {
    }

    public TblWorkCloseEntry(String id) {
        this.id = id;
    }

    public TblWorkCloseEntry(String id, int latestFlg) {
        this.id = id;
        this.latestFlg = latestFlg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public int getLatestFlg() {
        return latestFlg;
    }

    public void setLatestFlg(int latestFlg) {
        this.latestFlg = latestFlg;
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
        if (!(object instanceof TblWorkCloseEntry)) {
            return false;
        }
        TblWorkCloseEntry other = (TblWorkCloseEntry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.work.close.entry.TblWorkCloseEntry[ id=" + id + " ]";
    }
    
}
