/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.maintenance.detail;

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
@Table(name = "tbl_machine_maintenance_detail_image_file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findAll", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findByFileUuid", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.fileUuid = :fileUuid"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findByMaintenanceDetailId", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.tblMachineMaintenanceDetailImageFilePK.maintenanceDetailId = :maintenanceDetailId"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.deleteByMaintenanceDetailId", query = "DELETE FROM TblMachineMaintenanceDetailImageFile t WHERE t.tblMachineMaintenanceDetailImageFilePK.maintenanceDetailId = :maintenanceDetailId"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findBySeq", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.tblMachineMaintenanceDetailImageFilePK.seq = :seq"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findByFileType", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.fileType = :fileType"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findByFileExtension", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.fileExtension = :fileExtension"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findByTakenDate", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.takenDate = :takenDate"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findByTakenDateStz", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.takenDateStz = :takenDateStz"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findByRemarks", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.remarks = :remarks"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findByThumbnailFileUuid", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.thumbnailFileUuid = :thumbnailFileUuid"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findByCreateDate", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findByUpdateDate", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findByCreateDateUuid", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.createDateUuid = :createDateUuid"),
    @NamedQuery(name = "TblMachineMaintenanceDetailImageFile.findByUpdateUserUuid", query = "SELECT t FROM TblMachineMaintenanceDetailImageFile t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblMachineMaintenanceDetailImageFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMachineMaintenanceDetailImageFilePK tblMachineMaintenanceDetailImageFilePK;
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
    private TblMachineMaintenanceDetail tblMachineMaintenanceDetail;

    public TblMachineMaintenanceDetailImageFile() {
    }

    public TblMachineMaintenanceDetailImageFile(TblMachineMaintenanceDetailImageFilePK tblMachineMaintenanceDetailImageFilePK) {
        this.tblMachineMaintenanceDetailImageFilePK = tblMachineMaintenanceDetailImageFilePK;
    }

    public TblMachineMaintenanceDetailImageFile(TblMachineMaintenanceDetailImageFilePK tblMachineMaintenanceDetailImageFilePK, String fileUuid) {
        this.tblMachineMaintenanceDetailImageFilePK = tblMachineMaintenanceDetailImageFilePK;
        this.fileUuid = fileUuid;
    }

    public TblMachineMaintenanceDetailImageFile(String maintenanceDetailId, int seq) {
        this.tblMachineMaintenanceDetailImageFilePK = new TblMachineMaintenanceDetailImageFilePK(maintenanceDetailId, seq);
    }

    public TblMachineMaintenanceDetailImageFilePK getTblMachineMaintenanceDetailImageFilePK() {
        return tblMachineMaintenanceDetailImageFilePK;
    }

    public void setTblMachineMaintenanceDetailImageFilePK(TblMachineMaintenanceDetailImageFilePK tblMachineMaintenanceDetailImageFilePK) {
        this.tblMachineMaintenanceDetailImageFilePK = tblMachineMaintenanceDetailImageFilePK;
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
        hash += (tblMachineMaintenanceDetailImageFilePK != null ? tblMachineMaintenanceDetailImageFilePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineMaintenanceDetailImageFile)) {
            return false;
        }
        TblMachineMaintenanceDetailImageFile other = (TblMachineMaintenanceDetailImageFile) object;
        if ((this.tblMachineMaintenanceDetailImageFilePK == null && other.tblMachineMaintenanceDetailImageFilePK != null) || (this.tblMachineMaintenanceDetailImageFilePK != null && !this.tblMachineMaintenanceDetailImageFilePK.equals(other.tblMachineMaintenanceDetailImageFilePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.maintenance.detail.TblMachineMaintenanceDetailImageFile[ tblMachineMaintenanceDetailImageFilePK=" + tblMachineMaintenanceDetailImageFilePK + " ]";
    }

    /**
     * @return the tblMachineMaintenanceDetail
     */
    public TblMachineMaintenanceDetail getTblMachineMaintenanceDetail() {
        return tblMachineMaintenanceDetail;
    }

    /**
     * @param tblMachineMaintenanceDetail the tblMachineMaintenanceDetail to set
     */
    public void setTblMachineMaintenanceDetail(TblMachineMaintenanceDetail tblMachineMaintenanceDetail) {
        this.tblMachineMaintenanceDetail = tblMachineMaintenanceDetail;
    }
    
}
