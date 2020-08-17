/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.detail;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zds
 */
@Entity
@Table(name = "tbl_mold_maintenance_detail_image_file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findAll", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findByFileUuid", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.fileUuid = :fileUuid"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findByMaintenanceDetailId", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.tblMoldMaintenanceDetailImageFilePK.maintenanceDetailId = :maintenanceDetailId"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.deleteByMaintenanceDetailId", query = "DELETE FROM TblMoldMaintenanceDetailImageFile t WHERE t.tblMoldMaintenanceDetailImageFilePK.maintenanceDetailId = :maintenanceDetailId"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findBySeq", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.tblMoldMaintenanceDetailImageFilePK.seq = :seq"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findByFileType", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.fileType = :fileType"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findByFileExtension", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.fileExtension = :fileExtension"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findByTakenDate", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.takenDate = :takenDate"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findByTakenDateStz", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.takenDateStz = :takenDateStz"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findByRemarks", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.remarks = :remarks"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findByThumbnailFileUuid", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.thumbnailFileUuid = :thumbnailFileUuid"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findByCreateDate", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findByUpdateDate", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findByCreateDateUuid", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.createDateUuid = :createDateUuid"),
    @NamedQuery(name = "TblMoldMaintenanceDetailImageFile.findByUpdateUserUuid", query = "SELECT t FROM TblMoldMaintenanceDetailImageFile t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblMoldMaintenanceDetailImageFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMoldMaintenanceDetailImageFilePK tblMoldMaintenanceDetailImageFilePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "FILE_UUID")
    private String fileUuid;
    @Column(name = "FILE_TYPE")
    private Integer fileType;
    @Size(max = 10)
    @Column(name = "FILE_EXTENSION")
    private String fileExtension;
    @Column(name = "TAKEN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenDate;
    @Column(name = "TAKEN_DATE_STZ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenDateStz;
    @Size(max = 200)
    @Column(name = "REMARKS")
    private String remarks;
    @Size(max = 45)
    @Column(name = "THUMBNAIL_FILE_UUID")
    private String thumbnailFileUuid;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_DATE_UUID")
    private String createDateUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;

    @PrimaryKeyJoinColumn(name = "MAINTENANCE_DETAIL_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblMoldMaintenanceDetail tblMoldMaintenanceDetail;
        
    public TblMoldMaintenanceDetailImageFile() {
    }

    public TblMoldMaintenanceDetailImageFile(TblMoldMaintenanceDetailImageFilePK tblMoldMaintenanceDetailImageFilePK) {
        this.tblMoldMaintenanceDetailImageFilePK = tblMoldMaintenanceDetailImageFilePK;
    }

    public TblMoldMaintenanceDetailImageFile(TblMoldMaintenanceDetailImageFilePK tblMoldMaintenanceDetailImageFilePK, String fileUuid) {
        this.tblMoldMaintenanceDetailImageFilePK = tblMoldMaintenanceDetailImageFilePK;
        this.fileUuid = fileUuid;
    }

    public TblMoldMaintenanceDetailImageFile(String maintenanceDetailId, int seq) {
        this.tblMoldMaintenanceDetailImageFilePK = new TblMoldMaintenanceDetailImageFilePK(maintenanceDetailId, seq);
    }

    public TblMoldMaintenanceDetailImageFilePK getTblMoldMaintenanceDetailImageFilePK() {
        return tblMoldMaintenanceDetailImageFilePK;
    }

    public void setTblMoldMaintenanceDetailImageFilePK(TblMoldMaintenanceDetailImageFilePK tblMoldMaintenanceDetailImageFilePK) {
        this.tblMoldMaintenanceDetailImageFilePK = tblMoldMaintenanceDetailImageFilePK;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Date getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(Date takenDate) {
        this.takenDate = takenDate;
    }

    public Date getTakenDateStz() {
        return takenDateStz;
    }

    public void setTakenDateStz(Date takenDateStz) {
        this.takenDateStz = takenDateStz;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getThumbnailFileUuid() {
        return thumbnailFileUuid;
    }

    public void setThumbnailFileUuid(String thumbnailFileUuid) {
        this.thumbnailFileUuid = thumbnailFileUuid;
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

    public String getCreateDateUuid() {
        return createDateUuid;
    }

    public void setCreateDateUuid(String createDateUuid) {
        this.createDateUuid = createDateUuid;
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
        hash += (tblMoldMaintenanceDetailImageFilePK != null ? tblMoldMaintenanceDetailImageFilePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldMaintenanceDetailImageFile)) {
            return false;
        }
        TblMoldMaintenanceDetailImageFile other = (TblMoldMaintenanceDetailImageFile) object;
        if ((this.tblMoldMaintenanceDetailImageFilePK == null && other.tblMoldMaintenanceDetailImageFilePK != null) || (this.tblMoldMaintenanceDetailImageFilePK != null && !this.tblMoldMaintenanceDetailImageFilePK.equals(other.tblMoldMaintenanceDetailImageFilePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetailImageFile[ tblMoldMaintenanceDetailImageFilePK=" + tblMoldMaintenanceDetailImageFilePK + " ]";
    }

    /**
     * @return the tblMoldMaintenanceDetail
     */
    public TblMoldMaintenanceDetail getTblMoldMaintenanceDetail() {
        return tblMoldMaintenanceDetail;
    }

    /**
     * @param tblMoldMaintenanceDetail the tblMoldMaintenanceDetail to set
     */
    public void setTblMoldMaintenanceDetail(TblMoldMaintenanceDetail tblMoldMaintenanceDetail) {
        this.tblMoldMaintenanceDetail = tblMoldMaintenanceDetail;
    }
    
}
