/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.log.backup;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
 * @author c.darvin
 */
@Entity
@Table(name = "tbl_sigma_log_backup")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSigmaLogBackup.findAll", query = "SELECT t FROM TblSigmaLogBackup t"),
    @NamedQuery(name = "TblSigmaLogBackup.findByGunshiId", query = "SELECT t FROM TblSigmaLogBackup t WHERE t.tblSigmaLogBackupPK.gunshiId = :gunshiId"),
    @NamedQuery(name = "TblSigmaLogBackup.findByFolderName", query = "SELECT t FROM TblSigmaLogBackup t WHERE t.tblSigmaLogBackupPK.folderName = :folderName"),
    @NamedQuery(name = "TblSigmaLogBackup.findByReImportedDate", query = "SELECT t FROM TblSigmaLogBackup t WHERE t.reImportedDate = :reImportedDate"),
    @NamedQuery(name = "TblSigmaLogBackup.findByProcStatus", query = "SELECT t FROM TblSigmaLogBackup t WHERE t.procStatus = :procStatus"),
    @NamedQuery(name = "TblSigmaLogBackup.findByAvailableFlg", query = "SELECT t FROM TblSigmaLogBackup t WHERE t.availableFlg = :availableFlg"),
    @NamedQuery(name = "TblSigmaLogBackup.findByCreateDate", query = "SELECT t FROM TblSigmaLogBackup t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblSigmaLogBackup.findByUpdateDate", query = "SELECT t FROM TblSigmaLogBackup t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblSigmaLogBackup.findByCreateUserUuid", query = "SELECT t FROM TblSigmaLogBackup t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblSigmaLogBackup.findByUpdateUserUuid", query = "SELECT t FROM TblSigmaLogBackup t WHERE t.updateUserUuid = :updateUserUuid")})
public class TblSigmaLogBackup implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblSigmaLogBackupPK tblSigmaLogBackupPK;
    @Column(name = "RE_IMPORTED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reImportedDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PROC_STATUS")
    private int procStatus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AVAILABLE_FLG")
    private int availableFlg;
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
    private String gunshiId;
    @Transient
    private String folderName;
    
    public TblSigmaLogBackup() {
    }

    public TblSigmaLogBackup(TblSigmaLogBackupPK tblSigmaLogBackupPK) {
        this.tblSigmaLogBackupPK = tblSigmaLogBackupPK;
    }

    public TblSigmaLogBackup(TblSigmaLogBackupPK tblSigmaLogBackupPK, int procStatus, int availableFlg) {
        this.tblSigmaLogBackupPK = tblSigmaLogBackupPK;
        this.procStatus = procStatus;
        this.availableFlg = availableFlg;
    }

    public TblSigmaLogBackup(String gunshiId, String folderName) {
        this.tblSigmaLogBackupPK = new TblSigmaLogBackupPK(gunshiId, folderName);
    }
    
    @XmlTransient
    public TblSigmaLogBackupPK getTblSigmaLogBackupPK() {
        return tblSigmaLogBackupPK;
    }

    public void setTblSigmaLogBackupPK(TblSigmaLogBackupPK tblSigmaLogBackupPK) {
        this.tblSigmaLogBackupPK = tblSigmaLogBackupPK;
    }

    public Date getReImportedDate() {
        return reImportedDate;
    }

    public void setReImportedDate(Date reImportedDate) {
        this.reImportedDate = reImportedDate;
    }

    public int getProcStatus() {
        return procStatus;
    }

    public void setProcStatus(int procStatus) {
        this.procStatus = procStatus;
    }

    public int getAvailableFlg() {
        return availableFlg;
    }

    public void setAvailableFlg(int availableFlg) {
        this.availableFlg = availableFlg;
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
    
    public void formatJson(){
        if(this.getTblSigmaLogBackupPK() != null){
            this.setGunshiId(this.getTblSigmaLogBackupPK().getGunshiId());
        }
        if(this.getTblSigmaLogBackupPK() != null){
            this.setFolderName(this.getTblSigmaLogBackupPK().getFolderName());
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblSigmaLogBackupPK != null ? tblSigmaLogBackupPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSigmaLogBackup)) {
            return false;
        }
        TblSigmaLogBackup other = (TblSigmaLogBackup) object;
        if ((this.tblSigmaLogBackupPK == null && other.tblSigmaLogBackupPK != null) || (this.tblSigmaLogBackupPK != null && !this.tblSigmaLogBackupPK.equals(other.tblSigmaLogBackupPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.sigma.log.backup.TblSigmaLogBackup[ tblSigmaLogBackupPK=" + tblSigmaLogBackupPK + " ]";
    }
    
}
