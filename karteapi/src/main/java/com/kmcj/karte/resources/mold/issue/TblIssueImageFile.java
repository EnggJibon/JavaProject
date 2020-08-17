/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.issue;

import com.kmcj.karte.resources.mold.issue.TblIssue;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "tbl_issue_image_file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblIssueImageFile.findAll", query = "SELECT t FROM TblIssueImageFile t"),
    @NamedQuery(name = "TblIssueImageFile.findByFileUuid", query = "SELECT t FROM TblIssueImageFile t WHERE t.fileUuid = :fileUuid"),
    @NamedQuery(name = "TblIssueImageFile.findByIssueId", query = "SELECT t FROM TblIssueImageFile t WHERE t.tblIssueImageFilePK.issueId = :issueId"),
    @NamedQuery(name = "TblIssueImageFile.findBySeq", query = "SELECT t FROM TblIssueImageFile t WHERE t.tblIssueImageFilePK.seq = :seq"),
    @NamedQuery(name = "TblIssueImageFile.findByFileType", query = "SELECT t FROM TblIssueImageFile t WHERE t.fileType = :fileType"),
    @NamedQuery(name = "TblIssueImageFile.findByFileExtension", query = "SELECT t FROM TblIssueImageFile t WHERE t.fileExtension = :fileExtension"),
    @NamedQuery(name = "TblIssueImageFile.findByTakenDate", query = "SELECT t FROM TblIssueImageFile t WHERE t.takenDate = :takenDate"),
    @NamedQuery(name = "TblIssueImageFile.findByTakenDateStz", query = "SELECT t FROM TblIssueImageFile t WHERE t.takenDateStz = :takenDateStz"),
    @NamedQuery(name = "TblIssueImageFile.findByRemarks", query = "SELECT t FROM TblIssueImageFile t WHERE t.remarks = :remarks"),
    @NamedQuery(name = "TblIssueImageFile.findByCreateDate", query = "SELECT t FROM TblIssueImageFile t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblIssueImageFile.findByUpdateDate", query = "SELECT t FROM TblIssueImageFile t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblIssueImageFile.findByCreateDateUuid", query = "SELECT t FROM TblIssueImageFile t WHERE t.createDateUuid = :createDateUuid"),
    @NamedQuery(name = "TblIssueImageFile.findByUpdateUserUuid", query = "SELECT t FROM TblIssueImageFile t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblIssueImageFile.deleteByIssueId", query = "DELETE FROM TblIssueImageFile t WHERE t.tblIssueImageFilePK.issueId = :issueId ")
//    @NamedQuery(name = "TblIssueImageFile.updateIssueImageFile", query = "UPDATE TblIssueImageFile t SET "
//            + "t.fileUuid = :fileUuid,t.tblIssueImageFilePK.seq =:seq,"
//            + "t.fileExtension =:fileExtension,t.fileType = :fileType,"
//            + "t.remarks = :remarks,t.takenDate =:takenDate,"
//            + "t.takenDateStz =:takenDateStz,t.updateDate =:updateDate,"
//            + "t.updateUserUuid = :updateUserUuid WHERE t.tblIssueImageFilePK.issueId =:issueId "
//            + " And t.tblIssueImageFilePK.seq =:seq ")
})
public class TblIssueImageFile implements Serializable {

    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @EmbeddedId
    private TblIssueImageFilePK tblIssueImageFilePK;
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
    @JoinColumn(name = "ISSUE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TblIssue tblIssue;

    public TblIssueImageFile() {
    }

    public TblIssueImageFile(TblIssueImageFilePK tblIssueImageFilePK) {
        this.tblIssueImageFilePK = tblIssueImageFilePK;
    }

    public TblIssueImageFile(TblIssueImageFilePK tblIssueImageFilePK, String fileUuid) {
        this.tblIssueImageFilePK = tblIssueImageFilePK;
        this.fileUuid = fileUuid;
    }

    public TblIssueImageFile(String issueId, int seq) {
        this.tblIssueImageFilePK = new TblIssueImageFilePK(issueId, seq);
    }

    public TblIssueImageFilePK getTblIssueImageFilePK() {
        return tblIssueImageFilePK;
    }

    public void setTblIssueImageFilePK(TblIssueImageFilePK tblIssueImageFilePK) {
        this.tblIssueImageFilePK = tblIssueImageFilePK;
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
        hash += (getTblIssueImageFilePK() != null ? getTblIssueImageFilePK().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblIssueImageFile)) {
            return false;
        }
        TblIssueImageFile other = (TblIssueImageFile) object;
        if ((this.getTblIssueImageFilePK() == null && other.getTblIssueImageFilePK() != null) || (this.getTblIssueImageFilePK() != null && !this.tblIssueImageFilePK.equals(other.tblIssueImageFilePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.issue.TblIssueImageFile[ tblIssueImageFilePK=" + getTblIssueImageFilePK() + " ]";
    }

    /**
     * @return the tblIssue
     */
    public TblIssue getTblIssue() {
        return tblIssue;
    }

    /**
     * @param tblIssue the tblIssue to set
     */
    public void setTblIssue(TblIssue tblIssue) {
        this.tblIssue = tblIssue;
    }

    /**
     * @return the thumbnailFileUuid
     */
    public String getThumbnailFileUuid() {
        return thumbnailFileUuid;
    }

    /**
     * @param thumbnailFileUuid the thumbnailFileUuid to set
     */
    public void setThumbnailFileUuid(String thumbnailFileUuid) {
        this.thumbnailFileUuid = thumbnailFileUuid;
    }
}
