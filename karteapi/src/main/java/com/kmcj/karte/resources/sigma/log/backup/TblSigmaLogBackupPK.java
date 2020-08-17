/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.log.backup;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author c.darvin
 */
@Embeddable
public class TblSigmaLogBackupPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "GUNSHI_ID")
    private String gunshiId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "FOLDER_NAME")
    private String folderName;

    public TblSigmaLogBackupPK() {
    }

    public TblSigmaLogBackupPK(String gunshiId, String folderName) {
        this.gunshiId = gunshiId;
        this.folderName = folderName;
    }

    public String getGunshiId() {
        return gunshiId;
    }

    public void setGunshiId(String gunshiId) {
        this.gunshiId = gunshiId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gunshiId != null ? gunshiId.hashCode() : 0);
        hash += (folderName != null ? folderName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSigmaLogBackupPK)) {
            return false;
        }
        TblSigmaLogBackupPK other = (TblSigmaLogBackupPK) object;
        if ((this.gunshiId == null && other.gunshiId != null) || (this.gunshiId != null && !this.gunshiId.equals(other.gunshiId))) {
            return false;
        }
        if ((this.folderName == null && other.folderName != null) || (this.folderName != null && !this.folderName.equals(other.folderName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.sigma.log.backup.TblSigmaLogBackupPK[ gunshiId=" + gunshiId + ", folderName=" + folderName + " ]";
    }
    
}
