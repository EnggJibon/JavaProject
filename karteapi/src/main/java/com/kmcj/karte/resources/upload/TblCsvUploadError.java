/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.upload;

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
@Table(name = "tbl_csv_upload_error")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCsvUploadError.findAll", query = "SELECT t FROM TblCsvUploadError t")
    , @NamedQuery(name = "TblCsvUploadError.findById", query = "SELECT t FROM TblCsvUploadError t WHERE t.id = :id")
    , @NamedQuery(name = "TblCsvUploadError.findByErrorDatetime", query = "SELECT t FROM TblCsvUploadError t WHERE t.errorDatetime = :errorDatetime")
    , @NamedQuery(name = "TblCsvUploadError.findByProcessType", query = "SELECT t FROM TblCsvUploadError t WHERE t.processType = :processType")
})
@Cacheable(value = false)
public class TblCsvUploadError implements Serializable {

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
    @Size(max = 45)
    @Column(name = "PROCESS_TYPE")
    private String processType;
    @Size(max = 45)
    @Column(name = "LOG_FILE_UUID")
    private String logFileUuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ERROR_TYPE")
    private int errorType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SOLVED_FLG")
    private int solvedFlg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EMAIL_SENT_FLG")
    private int emailSentFlg;
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

    public TblCsvUploadError() {
    }

    public TblCsvUploadError(String id) {
        this.id = id;
    }

    public TblCsvUploadError(String id, int errorType, int solvedFlg, int emailSentFlg) {
        this.id = id;
        this.errorType = errorType;
        this.solvedFlg = solvedFlg;
        this.emailSentFlg = emailSentFlg;
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

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getLogFileUuid() {
        return logFileUuid;
    }

    public void setLogFileUuid(String logFileUuid) {
        this.logFileUuid = logFileUuid;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public int getSolvedFlg() {
        return solvedFlg;
    }

    public void setSolvedFlg(int solvedFlg) {
        this.solvedFlg = solvedFlg;
    }

    public int getEmailSentFlg() {
        return emailSentFlg;
    }

    public void setEmailSentFlg(int emailSentFlg) {
        this.emailSentFlg = emailSentFlg;
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
        if (!(object instanceof TblCsvUploadError)) {
            return false;
        }
        TblCsvUploadError other = (TblCsvUploadError) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.upload.TblCsvUploadError[ id=" + id + " ]";
    }

}
