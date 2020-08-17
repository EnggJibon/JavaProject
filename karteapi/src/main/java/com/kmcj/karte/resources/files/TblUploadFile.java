/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.files;

import com.kmcj.karte.resources.function.MstFunction;
import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_upload_file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblUploadFile.findAll", query = "SELECT t FROM TblUploadFile t"),
    @NamedQuery(name = "TblUploadFile.findByFileUuid", query = "SELECT t FROM TblUploadFile t WHERE t.fileUuid = :fileUuid"),
    @NamedQuery(name = "TblUploadFile.findByUploadUserUuid", query = "SELECT t FROM TblUploadFile t WHERE t.uploadUserUuid = :uploadUserUuid"),
    @NamedQuery(name = "TblUploadFile.findByUploadDate", query = "SELECT t FROM TblUploadFile t WHERE t.uploadDate = :uploadDate"),
    @NamedQuery(name = "TblUploadFile.findByFileType", query = "SELECT t FROM TblUploadFile t WHERE t.fileType = :fileType")})
@Cacheable(value = false)
public class TblUploadFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "FILE_UUID")
    private String fileUuid;
    @Size(max = 45)
    @Column(name = "UPLOAD_USER_UUID")
    private String uploadUserUuid;
    @Column(name = "UPLOAD_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;
    @Size(max = 6)
    @Column(name = "FILE_TYPE")
    private String fileType;
    @JoinColumn(name = "FUNCTION_ID", referencedColumnName = "ID")
    @ManyToOne
    private MstFunction functionId;
    @OneToMany(mappedBy = "uploadFileUuid")
    private Collection<TblCsvImport> tblCsvImportCollection;
    
    @Column(name = "UPLOAD_FILE_NAME")
    private String uploadFileName;

    public TblUploadFile() {
    }

    public TblUploadFile(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public String getUploadUserUuid() {
        return uploadUserUuid;
    }

    public void setUploadUserUuid(String uploadUserUuid) {
        this.uploadUserUuid = uploadUserUuid;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public MstFunction getFunctionId() {
        return functionId;
    }

    public void setFunctionId(MstFunction functionId) {
        this.functionId = functionId;
    }

    @XmlTransient
    public Collection<TblCsvImport> getTblCsvImportCollection() {
        return tblCsvImportCollection;
    }

    public void setTblCsvImportCollection(Collection<TblCsvImport> tblCsvImportCollection) {
        this.tblCsvImportCollection = tblCsvImportCollection;
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
        if (!(object instanceof TblUploadFile)) {
            return false;
        }
        TblUploadFile other = (TblUploadFile) object;
        if ((this.fileUuid == null && other.fileUuid != null) || (this.fileUuid != null && !this.fileUuid.equals(other.fileUuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.files.TblUploadFile[ fileUuid=" + fileUuid + " ]";
    }

    /**
     * @return the uploadFileName
     */
    public String getUploadFileName() {
        return uploadFileName;
    }

    /**
     * @param uploadFileName the uploadFileName to set
     */
    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }
    
}
