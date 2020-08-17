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
@Table(name = "tbl_csv_export")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCsvExport.findAll", query = "SELECT t FROM TblCsvExport t"),
    @NamedQuery(name = "TblCsvExport.findByFileUuid", query = "SELECT t FROM TblCsvExport t WHERE t.fileUuid = :fileUuid"),
    @NamedQuery(name = "TblCsvExport.findByExportUserUuid", query = "SELECT t FROM TblCsvExport t WHERE t.exportUserUuid = :exportUserUuid"),
    @NamedQuery(name = "TblCsvExport.findByExportDate", query = "SELECT t FROM TblCsvExport t WHERE t.exportDate = :exportDate"),
    @NamedQuery(name = "TblCsvExport.findByExportTable", query = "SELECT t FROM TblCsvExport t WHERE t.exportTable = :exportTable"),
    @NamedQuery(name = "TblCsvExport.findByClientFileName", query = "SELECT t FROM TblCsvExport t WHERE t.clientFileName = :clientFileName")})
@Cacheable(value = false)
public class TblCsvExport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "FILE_UUID")
    private String fileUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "EXPORT_USER_UUID")
    private String exportUserUuid;
    @Column(name = "EXPORT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date exportDate;
    @Size(max = 100)
    @Column(name = "EXPORT_TABLE")
    private String exportTable;
    @Size(max = 250)
    @Column(name = "CLIENT_FILE_NAME")
    private String clientFileName;
    @JoinColumn(name = "FUNCTION_ID", referencedColumnName = "ID")
    @ManyToOne
    private MstFunction functionId;

    public TblCsvExport() {
    }

    public TblCsvExport(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public TblCsvExport(String fileUuid, String exportUserUuid) {
        this.fileUuid = fileUuid;
        this.exportUserUuid = exportUserUuid;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public String getExportUserUuid() {
        return exportUserUuid;
    }

    public void setExportUserUuid(String exportUserUuid) {
        this.exportUserUuid = exportUserUuid;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public String getExportTable() {
        return exportTable;
    }

    public void setExportTable(String exportTable) {
        this.exportTable = exportTable;
    }

    public String getClientFileName() {
        return clientFileName;
    }

    public void setClientFileName(String clientFileName) {
        this.clientFileName = clientFileName;
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
        hash += (fileUuid != null ? fileUuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblCsvExport)) {
            return false;
        }
        TblCsvExport other = (TblCsvExport) object;
        if ((this.fileUuid == null && other.fileUuid != null) || (this.fileUuid != null && !this.fileUuid.equals(other.fileUuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.files.TblCsvExport[ fileUuid=" + fileUuid + " ]";
    }
    
}
