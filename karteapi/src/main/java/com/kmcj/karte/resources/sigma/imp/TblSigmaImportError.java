/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.imp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Basic;
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
@Table(name = "tbl_sigma_import_error")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSigmaImportError.findAll", query = "SELECT t FROM TblSigmaImportError t"),
    @NamedQuery(name = "TblSigmaImportError.findById", query = "SELECT t FROM TblSigmaImportError t WHERE t.id = :id"),
    @NamedQuery(name = "TblSigmaImportError.findByErrorDatetime", query = "SELECT t FROM TblSigmaImportError t WHERE t.errorDatetime = :errorDatetime"),
    @NamedQuery(name = "TblSigmaImportError.findByMacKey", query = "SELECT t FROM TblSigmaImportError t WHERE t.macKey = :macKey"),
    @NamedQuery(name = "TblSigmaImportError.findBySigmaGunshiId", query = "SELECT t FROM TblSigmaImportError t WHERE t.sigmaGunshiId = :sigmaGunshiId"),
    @NamedQuery(name = "TblSigmaImportError.findByMachineUuid", query = "SELECT t FROM TblSigmaImportError t WHERE t.machineUuid = :machineUuid"),
    @NamedQuery(name = "TblSigmaImportError.findByLogFileUuid", query = "SELECT t FROM TblSigmaImportError t WHERE t.logFileUuid = :logFileUuid"),
    @NamedQuery(name = "TblSigmaImportError.findByGunshiCsvFileUuid", query = "SELECT t FROM TblSigmaImportError t WHERE t.gunshiCsvFileUuid = :gunshiCsvFileUuid"),
    @NamedQuery(name = "TblSigmaImportError.findBySolvedFlg", query = "SELECT t FROM TblSigmaImportError t WHERE t.solvedFlg = :solvedFlg"),
    @NamedQuery(name = "TblSigmaImportError.findByMailFlg", query = "SELECT t FROM TblSigmaImportError t WHERE t.mailFlg = :mailFlg"),
    @NamedQuery(name = "TblSigmaImportError.findByCreateDate", query = "SELECT t FROM TblSigmaImportError t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblSigmaImportError.findByUpdateDate", query = "SELECT t FROM TblSigmaImportError t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblSigmaImportError.findByCreateUserUuid", query = "SELECT t FROM TblSigmaImportError t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblSigmaImportError.findByUpdateUserUuid", query = "SELECT t FROM TblSigmaImportError t WHERE t.updateUserUuid = :updateUserUuid")})
public class TblSigmaImportError implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "ERROR_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date errorDatetime;
    @Size(max = 50)
    @NotNull
    @Column(name = "MAC_KEY")
    private String macKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "SIGMA_GUNSHI_ID")
    private String sigmaGunshiId;
    @Size(max = 45)
    @NotNull
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @Size(max = 45)
    @Column(name = "LOG_FILE_UUID")
    private String logFileUuid;
    @Size(max = 45)
    @Column(name = "GUNSHI_CSV_FILE_UUID")
    private String gunshiCsvFileUuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SOLVED_FLG")
    private int solvedFlg;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "MAIL_FLG")
    private int mailFlg;

    public TblSigmaImportError() {
    }

    public TblSigmaImportError(String id) {
        this.id = id;
    }

    public TblSigmaImportError(String id, String sigmaGunshiId, int solvedFlg) {
        this.id = id;
        this.sigmaGunshiId = sigmaGunshiId;
        this.solvedFlg = solvedFlg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getErrorDatetime() {
        return errorDatetime;
    }

    public void setErrorDatetime(Date errorDatetime) {
        this.errorDatetime = errorDatetime;
    }

    public String getMacKey() {
        return macKey;
    }

    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }

    public String getSigmaGunshiId() {
        return sigmaGunshiId;
    }

    public void setSigmaGunshiId(String sigmaGunshiId) {
        this.sigmaGunshiId = sigmaGunshiId;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public String getLogFileUuid() {
        return logFileUuid;
    }

    public void setLogFileUuid(String logFileUuid) {
        this.logFileUuid = logFileUuid;
    }

    public String getGunshiCsvFileUuid() {
        return gunshiCsvFileUuid;
    }

    public void setGunshiCsvFileUuid(String gunshiCsvFileUuid) {
        this.gunshiCsvFileUuid = gunshiCsvFileUuid;
    }

    public int getSolvedFlg() {
        return solvedFlg;
    }

    public void setSolvedFlg(int solvedFlg) {
        this.solvedFlg = solvedFlg;
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
    
    public int getMailFlg() {
        return mailFlg;
    }

    public void setMailFlg(int mailFlg) {
        this.mailFlg = mailFlg;
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
        if (!(object instanceof TblSigmaImportError)) {
            return false;
        }
        TblSigmaImportError other = (TblSigmaImportError) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.sigma.imp.TblSigmaImportError[ id=" + id + " ]";
    }

    void setErrorDatetime(SimpleDateFormat sdfDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setCreateDate(SimpleDateFormat sdfDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setUpdateDate(SimpleDateFormat sdfDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
