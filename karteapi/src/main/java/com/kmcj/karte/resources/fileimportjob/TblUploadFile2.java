/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.fileimportjob;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_upload_file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "importTblUploadFile.findAll", query = "SELECT t FROM TblUploadFile2 t"),
    @NamedQuery(name = "importTblUploadFile.findByFileUuid", query = "SELECT t FROM TblUploadFile2 t WHERE t.fileUuid = :fileUuid"),
    @NamedQuery(name = "importTblUploadFile.findByUploadUserUuid", query = "SELECT t FROM TblUploadFile2 t WHERE t.uploadUserUuid = :uploadUserUuid"),
    @NamedQuery(name = "importTblUploadFile.findByUploadDate", query = "SELECT t FROM TblUploadFile2 t WHERE t.uploadDate = :uploadDate"),
    @NamedQuery(name = "importTblUploadFile.findByFileTypeOrderByUploadDate", query = "SELECT t FROM TblUploadFile2 t JOIN FETCH t.mstFunction LEFT JOIN FETCH t.tblCsvImport WHERE t.fileType = :fileType ORDER BY t.uploadDate desc"),
    @NamedQuery(name = "importTblUploadFile.findByFileTypeUploadDateOrderByUploadDate", query = "SELECT t FROM TblUploadFile2 t JOIN FETCH t.mstFunction LEFT JOIN FETCH t.tblCsvImport WHERE t.fileType = :fileType and t.uploadDate >= :uploadDate ORDER BY t.uploadDate desc")
})
@Cacheable(value = false)
public class TblUploadFile2 implements Serializable {

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
    private MstFunction mstFunction;
   
    @JoinColumn(name = "FILE_UUID", referencedColumnName = "UPLOAD_FILE_UUID", insertable = false, updatable = false)
    @OneToOne
    private TblCsvImport2 tblCsvImport;

    public TblUploadFile2() {
    }

    public TblUploadFile2(String fileUuid) {
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
        return mstFunction;
    }

    public void setFunctionId(MstFunction mstFunction) {
        this.mstFunction = mstFunction;
    }

    @XmlElement
    public TblCsvImport2 getTblCsvImport() {
        return tblCsvImport;
    }

    public void setTblCsvImport(TblCsvImport2 tblCsvImport) {
        this.tblCsvImport = tblCsvImport;
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
        if (!(object instanceof TblUploadFile2)) {
            return false;
        }
        TblUploadFile2 other = (TblUploadFile2) object;
        if ((this.fileUuid == null && other.fileUuid != null) || (this.fileUuid != null && !this.fileUuid.equals(other.fileUuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.fileimportjob.TblUploadFile2[ fileUuid=" + fileUuid + " ]";
    }
    
}
