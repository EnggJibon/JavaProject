/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.remodeling.detail;

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
@Table(name = "tbl_mold_remodeling_detail_image_file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findAll", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findByFileUuid", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.fileUuid = :fileUuid"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findByRemodelingDetailId", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.tblMoldRemodelingDetailImageFilePK.remodelingDetailId = :remodelingDetailId"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.deleteByRemodelingDetailId", query = "DELETE FROM TblMoldRemodelingDetailImageFile t WHERE t.tblMoldRemodelingDetailImageFilePK.remodelingDetailId = :remodelingDetailId"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findBySeq", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.tblMoldRemodelingDetailImageFilePK.seq = :seq"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findByFileType", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.fileType = :fileType"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findByFileExtension", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.fileExtension = :fileExtension"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findByTakenDate", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.takenDate = :takenDate"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findByTakenDateStz", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.takenDateStz = :takenDateStz"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findByRemarks", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.remarks = :remarks"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findByThumbnailFileUuid", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.thumbnailFileUuid = :thumbnailFileUuid"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findByCreateDate", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findByUpdateDate", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findByCreateDateUuid", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.createDateUuid = :createDateUuid"),
    @NamedQuery(name = "TblMoldRemodelingDetailImageFile.findByUpdateUserUuid", query = "SELECT t FROM TblMoldRemodelingDetailImageFile t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblMoldRemodelingDetailImageFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMoldRemodelingDetailImageFilePK tblMoldRemodelingDetailImageFilePK;
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

    @PrimaryKeyJoinColumn(name = "REMODELING_DETAIL_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblMoldRemodelingDetail tblMoldRemodelingDetail;
    
    public TblMoldRemodelingDetailImageFile() {
    }

    public TblMoldRemodelingDetailImageFile(TblMoldRemodelingDetailImageFilePK tblMoldRemodelingDetailImageFilePK) {
        this.tblMoldRemodelingDetailImageFilePK = tblMoldRemodelingDetailImageFilePK;
    }

    public TblMoldRemodelingDetailImageFile(TblMoldRemodelingDetailImageFilePK tblMoldRemodelingDetailImageFilePK, String fileUuid) {
        this.tblMoldRemodelingDetailImageFilePK = tblMoldRemodelingDetailImageFilePK;
        this.fileUuid = fileUuid;
    }

    public TblMoldRemodelingDetailImageFile(String remodelingDetailId, int seq) {
        this.tblMoldRemodelingDetailImageFilePK = new TblMoldRemodelingDetailImageFilePK(remodelingDetailId, seq);
    }

    public TblMoldRemodelingDetailImageFilePK getTblMoldRemodelingDetailImageFilePK() {
        return tblMoldRemodelingDetailImageFilePK;
    }

    public void setTblMoldRemodelingDetailImageFilePK(TblMoldRemodelingDetailImageFilePK tblMoldRemodelingDetailImageFilePK) {
        this.tblMoldRemodelingDetailImageFilePK = tblMoldRemodelingDetailImageFilePK;
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
        hash += (tblMoldRemodelingDetailImageFilePK != null ? tblMoldRemodelingDetailImageFilePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldRemodelingDetailImageFile)) {
            return false;
        }
        TblMoldRemodelingDetailImageFile other = (TblMoldRemodelingDetailImageFile) object;
        if ((this.tblMoldRemodelingDetailImageFilePK == null && other.tblMoldRemodelingDetailImageFilePK != null) || (this.tblMoldRemodelingDetailImageFilePK != null && !this.tblMoldRemodelingDetailImageFilePK.equals(other.tblMoldRemodelingDetailImageFilePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailImageFile[ tblMoldRemodelingDetailImageFilePK=" + tblMoldRemodelingDetailImageFilePK + " ]";
    }

    /**
     * @return the tblMoldRemodelingDetail
     */
    public TblMoldRemodelingDetail getTblMoldRemodelingDetail() {
        return tblMoldRemodelingDetail;
    }

    /**
     * @param tblMoldRemodelingDetail the tblMoldRemodelingDetail to set
     */
    public void setTblMoldRemodelingDetail(TblMoldRemodelingDetail tblMoldRemodelingDetail) {
        this.tblMoldRemodelingDetail = tblMoldRemodelingDetail;
    }
    
}
