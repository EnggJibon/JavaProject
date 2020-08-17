/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.externalmold.delete;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Embeddable
public class TblDeletedKeyPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "TBL_NAME")
    private String tblName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DELETED_KEY")
    private String deletedKey;

    public TblDeletedKeyPK() {
    }

    public TblDeletedKeyPK(String tblName, String deletedKey) {
        this.tblName = tblName;
        this.deletedKey = deletedKey;
    }

    public String getTblName() {
        return tblName;
    }

    public void setTblName(String tblName) {
        this.tblName = tblName;
    }

    public String getDeletedKey() {
        return deletedKey;
    }

    public void setDeletedKey(String deletedKey) {
        this.deletedKey = deletedKey;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblName != null ? tblName.hashCode() : 0);
        hash += (deletedKey != null ? deletedKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblDeletedKeyPK)) {
            return false;
        }
        TblDeletedKeyPK other = (TblDeletedKeyPK) object;
        if (this.tblName != other.tblName) {
            return false;
        }
        if ((this.deletedKey == null && other.deletedKey != null) || (this.deletedKey != null && !this.deletedKey.equals(other.deletedKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.batch.externalmold.TblDeletedKeyPK[ tblName=" + tblName + ", deletedKey=" + deletedKey + " ]";
    }
    
}
