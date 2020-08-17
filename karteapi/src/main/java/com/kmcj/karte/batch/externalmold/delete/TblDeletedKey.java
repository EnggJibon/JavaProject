/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.externalmold.delete;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "tbl_deleted_key")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblDeletedKey.findAll", query = "SELECT t FROM TblDeletedKey t"),
    @NamedQuery(name = "TblDeletedKey.findByTblName", query = "SELECT t FROM TblDeletedKey t WHERE t.tblDeletedKeyPK.tblName = :tblName"),
    @NamedQuery(name = "TblDeletedKey.findByDeletedKey", query = "SELECT t FROM TblDeletedKey t WHERE t.tblDeletedKeyPK.deletedKey = :deletedKey"),
    @NamedQuery(name = "TblDeletedKey.findByDeleteUserUuid", query = "SELECT t FROM TblDeletedKey t WHERE t.deleteUserUuid = :deleteUserUuid"),
    @NamedQuery(name = "TblDeletedKey.findByDeletedDate", query = "SELECT t FROM TblDeletedKey t WHERE t.deletedDate = :deletedDate")})
@Cacheable(value = false)
public class TblDeletedKey implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblDeletedKeyPK tblDeletedKeyPK;
    @Size(max = 45)
    @Column(name = "DELETE_USER_UUID")
    private String deleteUserUuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DELETED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;

    public TblDeletedKey() {
    }

    public TblDeletedKey(TblDeletedKeyPK tblDeletedKeyPK) {
        this.tblDeletedKeyPK = tblDeletedKeyPK;
    }

    public TblDeletedKey(TblDeletedKeyPK tblDeletedKeyPK, Date deletedDate) {
        this.tblDeletedKeyPK = tblDeletedKeyPK;
        this.deletedDate = deletedDate;
    }

    public TblDeletedKey(String tblName, String deletedKey) {
        this.tblDeletedKeyPK = new TblDeletedKeyPK(tblName, deletedKey);
    }

    public TblDeletedKeyPK getTblDeletedKeyPK() {
        return tblDeletedKeyPK;
    }

    public void setTblDeletedKeyPK(TblDeletedKeyPK tblDeletedKeyPK) {
        this.tblDeletedKeyPK = tblDeletedKeyPK;
    }

    public String getDeleteUserUuid() {
        return deleteUserUuid;
    }

    public void setDeleteUserUuid(String deleteUserUuid) {
        this.deleteUserUuid = deleteUserUuid;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblDeletedKeyPK != null ? tblDeletedKeyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblDeletedKey)) {
            return false;
        }
        TblDeletedKey other = (TblDeletedKey) object;
        if ((this.tblDeletedKeyPK == null && other.tblDeletedKeyPK != null) || (this.tblDeletedKeyPK != null && !this.tblDeletedKeyPK.equals(other.tblDeletedKeyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.batch.externalmold.TblDeletedKey[ tblDeletedKeyPK=" + tblDeletedKeyPK + " ]";
    }
    
}
