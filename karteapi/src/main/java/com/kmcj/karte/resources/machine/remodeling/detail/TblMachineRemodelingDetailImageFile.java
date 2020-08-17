package com.kmcj.karte.resources.machine.remodeling.detail;

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
@Table(name = "tbl_machine_remodeling_detail_image_file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findAll", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findByFileUuid", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.fileUuid = :fileUuid"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findByRemodelingDetailId", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.tblMachineRemodelingDetailImageFilePK.remodelingDetailId = :remodelingDetailId"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.deleteByRemodelingDetailId", query = "DELETE FROM TblMachineRemodelingDetailImageFile t WHERE t.tblMachineRemodelingDetailImageFilePK.remodelingDetailId = :remodelingDetailId"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findBySeq", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.tblMachineRemodelingDetailImageFilePK.seq = :seq"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findByFileType", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.fileType = :fileType"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findByFileExtension", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.fileExtension = :fileExtension"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findByTakenDate", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.takenDate = :takenDate"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findByTakenDateStz", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.takenDateStz = :takenDateStz"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findByRemarks", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.remarks = :remarks"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findByThumbnailFileUuid", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.thumbnailFileUuid = :thumbnailFileUuid"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findByCreateDate", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findByUpdateDate", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findByCreateDateUuid", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.createDateUuid = :createDateUuid"),
    @NamedQuery(name = "TblMachineRemodelingDetailImageFile.findByUpdateUserUuid", query = "SELECT t FROM TblMachineRemodelingDetailImageFile t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblMachineRemodelingDetailImageFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMachineRemodelingDetailImageFilePK tblMachineRemodelingDetailImageFilePK;
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
    private TblMachineRemodelingDetail tblMachineRemodelingDetail;

    public TblMachineRemodelingDetailImageFile() {
    }

    public TblMachineRemodelingDetailImageFile(TblMachineRemodelingDetailImageFilePK tblMachineRemodelingDetailImageFilePK) {
        this.tblMachineRemodelingDetailImageFilePK = tblMachineRemodelingDetailImageFilePK;
    }

    public TblMachineRemodelingDetailImageFile(TblMachineRemodelingDetailImageFilePK tblMachineRemodelingDetailImageFilePK, String fileUuid) {
        this.tblMachineRemodelingDetailImageFilePK = tblMachineRemodelingDetailImageFilePK;
        this.fileUuid = fileUuid;
    }

    public TblMachineRemodelingDetailImageFile(String remodelingDetailId, int seq) {
        this.tblMachineRemodelingDetailImageFilePK = new TblMachineRemodelingDetailImageFilePK(remodelingDetailId, seq);
    }

    public TblMachineRemodelingDetailImageFilePK getTblMachineRemodelingDetailImageFilePK() {
        return tblMachineRemodelingDetailImageFilePK;
    }

    public void setTblMachineRemodelingDetailImageFilePK(TblMachineRemodelingDetailImageFilePK tblMachineRemodelingDetailImageFilePK) {
        this.tblMachineRemodelingDetailImageFilePK = tblMachineRemodelingDetailImageFilePK;
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
        hash += (tblMachineRemodelingDetailImageFilePK != null ? tblMachineRemodelingDetailImageFilePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineRemodelingDetailImageFile)) {
            return false;
        }
        TblMachineRemodelingDetailImageFile other = (TblMachineRemodelingDetailImageFile) object;
        if ((this.tblMachineRemodelingDetailImageFilePK == null && other.tblMachineRemodelingDetailImageFilePK != null) || (this.tblMachineRemodelingDetailImageFilePK != null && !this.tblMachineRemodelingDetailImageFilePK.equals(other.tblMachineRemodelingDetailImageFilePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailImageFile[ tblMachineRemodelingDetailImageFilePK=" + tblMachineRemodelingDetailImageFilePK + " ]";
    }

    /**
     * @return the tblMachineRemodelingDetail
     */
    public TblMachineRemodelingDetail getTblMachineRemodelingDetail() {
        return tblMachineRemodelingDetail;
    }

    /**
     * @param tblMachineRemodelingDetail the tblMachineRemodelingDetail to set
     */
    public void setTblMachineRemodelingDetail(TblMachineRemodelingDetail tblMachineRemodelingDetail) {
        this.tblMachineRemodelingDetail = tblMachineRemodelingDetail;
    }
    
}
