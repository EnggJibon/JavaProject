/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.files;

import com.kmcj.karte.resources.function.MstFunction;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "tbl_csv_import")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCsvImport.findAll", query = "SELECT t FROM TblCsvImport t"),
    @NamedQuery(name = "TblCsvImport.findByImportUuid", query = "SELECT t FROM TblCsvImport t WHERE t.importUuid = :importUuid"),
    @NamedQuery(name = "TblCsvImport.findByImportUserUuid", query = "SELECT t FROM TblCsvImport t WHERE t.importUserUuid = :importUserUuid"),
    @NamedQuery(name = "TblCsvImport.findByImportTable", query = "SELECT t FROM TblCsvImport t WHERE t.importTable = :importTable"),
    @NamedQuery(name = "TblCsvImport.findByRecordCount", query = "SELECT t FROM TblCsvImport t WHERE t.recordCount = :recordCount"),
    @NamedQuery(name = "TblCsvImport.findBySuceededCount", query = "SELECT t FROM TblCsvImport t WHERE t.suceededCount = :suceededCount"),
    @NamedQuery(name = "TblCsvImport.findByAddedCount", query = "SELECT t FROM TblCsvImport t WHERE t.addedCount = :addedCount"),
    @NamedQuery(name = "TblCsvImport.findByUpdatedCount", query = "SELECT t FROM TblCsvImport t WHERE t.updatedCount = :updatedCount"),
    @NamedQuery(name = "TblCsvImport.findByDeletedCount", query = "SELECT t FROM TblCsvImport t WHERE t.deletedCount = :deletedCount"),
    @NamedQuery(name = "TblCsvImport.findByFailedCount", query = "SELECT t FROM TblCsvImport t WHERE t.failedCount = :failedCount"),
    @NamedQuery(name = "TblCsvImport.findByLogFileUuid", query = "SELECT t FROM TblCsvImport t WHERE t.logFileUuid = :logFileUuid"),
    @NamedQuery(name = "TblCsvImport.findByImportDate", query = "SELECT t FROM TblCsvImport t WHERE t.importDate = :importDate"),
    @NamedQuery(name = "TblCsvImport.findByLogFileName", query = "SELECT t FROM TblCsvImport t WHERE t.logFileName = :logFileName")})
@Cacheable(value = false)
public class TblCsvImport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "IMPORT_UUID")
    private String importUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "IMPORT_USER_UUID")
    private String importUserUuid;
    @Size(max = 100)
    @Column(name = "IMPORT_TABLE")
    private String importTable;
    @Column(name = "RECORD_COUNT")
    private Integer recordCount;
    @Column(name = "SUCEEDED_COUNT")
    private Integer suceededCount;
    @Column(name = "ADDED_COUNT")
    private Integer addedCount;
    @Column(name = "UPDATED_COUNT")
    private Integer updatedCount;
    @Column(name = "DELETED_COUNT")
    private Integer deletedCount;
    @Column(name = "FAILED_COUNT")
    private Integer failedCount;
    @Size(max = 45)
    @Column(name = "LOG_FILE_UUID")
    private String logFileUuid;
    @Column(name = "IMPORT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date importDate;
    @Size(max = 200)
    @Column(name = "LOG_FILE_NAME")
    private String logFileName;
    @JoinColumn(name = "UPLOAD_FILE_UUID", referencedColumnName = "FILE_UUID")
    @ManyToOne
    private TblUploadFile uploadFileUuid;
    @JoinColumn(name = "FUNCTION_ID", referencedColumnName = "ID")
    @ManyToOne
    private MstFunction functionId;

    public TblCsvImport() {
    }

    public TblCsvImport(String importUuid) {
        this.importUuid = importUuid;
    }

    public TblCsvImport(String importUuid, String importUserUuid) {
        this.importUuid = importUuid;
        this.importUserUuid = importUserUuid;
    }

    public String getImportUuid() {
        return importUuid;
    }

    public void setImportUuid(String importUuid) {
        this.importUuid = importUuid;
    }

    public String getImportUserUuid() {
        return importUserUuid;
    }

    public void setImportUserUuid(String importUserUuid) {
        this.importUserUuid = importUserUuid;
    }

    public String getImportTable() {
        return importTable;
    }

    public void setImportTable(String importTable) {
        this.importTable = importTable;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public Integer getSuceededCount() {
        return suceededCount;
    }

    public void setSuceededCount(Integer suceededCount) {
        this.suceededCount = suceededCount;
    }

    public Integer getAddedCount() {
        return addedCount;
    }

    public void setAddedCount(Integer addedCount) {
        this.addedCount = addedCount;
    }

    public Integer getUpdatedCount() {
        return updatedCount;
    }

    public void setUpdatedCount(Integer updatedCount) {
        this.updatedCount = updatedCount;
    }

    public Integer getDeletedCount() {
        return deletedCount;
    }

    public void setDeletedCount(Integer deletedCount) {
        this.deletedCount = deletedCount;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    public String getLogFileUuid() {
        return logFileUuid;
    }

    public void setLogFileUuid(String logFileUuid) {
        this.logFileUuid = logFileUuid;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public TblUploadFile getUploadFileUuid() {
        return uploadFileUuid;
    }

    public void setUploadFileUuid(TblUploadFile uploadFileUuid) {
        this.uploadFileUuid = uploadFileUuid;
    }

    public MstFunction getFunctionId() {
        return functionId;
    }

    public void setFunctionId(MstFunction functionId) {
        this.functionId = functionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (importUuid != null ? importUuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblCsvImport)) {
            return false;
        }
        TblCsvImport other = (TblCsvImport) object;
        if ((this.importUuid == null && other.importUuid != null) || (this.importUuid != null && !this.importUuid.equals(other.importUuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.files.TblCsvImport[ importUuid=" + importUuid + " ]";
    }
    
}
