/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.visualimage;

import com.google.gson.Gson;
import com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResultDetail;
import com.kmcj.karte.util.XmlDateAdapter;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author duanlin
 */
@Entity
@Table(name = "tbl_component_inspection_visual_image_file")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblComponentInspectionVisualImageFile.findAll", query = "SELECT t FROM TblComponentInspectionVisualImageFile t"),
    @NamedQuery(name = "TblComponentInspectionVisualImageFile.findByFileUuid", query = "SELECT t FROM TblComponentInspectionVisualImageFile t WHERE t.fileUuid = :fileUuid"),
    @NamedQuery(name = "TblComponentInspectionVisualImageFile.findByComponentInspectionResultDetailId",
            query = "SELECT t FROM TblComponentInspectionVisualImageFile t join TblComponentInspectionResultDetail rd on t.tblComponentInspectionVisualImageFilePK.componentInspectionResultDetailId = rd.id WHERE t.tblComponentInspectionVisualImageFilePK.componentInspectionResultDetailId in (select rd2.id from TblComponentInspectionResultDetail rd1 join TblComponentInspectionResultDetail rd2 on rd2.componentInspectionResultId = rd1.componentInspectionResultId and rd2.inspectionItemSno = rd1.inspectionItemSno and rd2.inspectionType = rd1.inspectionType where rd1.id = :componentInspectionResultDetailId and rd1.id <> rd2.id) ORDER BY t.tblComponentInspectionVisualImageFilePK.seq"),
    @NamedQuery(
        name = "TblComponentInspectionVisualImageFile.findByComponentInspectionResultId", 
        query = "SELECT t FROM TblComponentInspectionVisualImageFile t WHERE t.tblComponentInspectionVisualImageFilePK.componentInspectionResultDetailId in (select rd.id from TblComponentInspectionResultDetail rd where rd.componentInspectionResultId = :componentInspectionResultId and rd.inspectionType = :inspectionType)"
    ),
    @NamedQuery(
        name = "TblComponentInspectionVisualImageFile.findByComponentInspectionResultIdAndInspectTypes", 
        query = "SELECT t FROM TblComponentInspectionVisualImageFile t WHERE t.tblComponentInspectionVisualImageFilePK.componentInspectionResultDetailId in (select rd.id from TblComponentInspectionResultDetail rd where rd.componentInspectionResultId = :componentInspectionResultId and rd.inspectionType IN :inspectionType)"
    ),
    @NamedQuery(name = "TblComponentInspectionVisualImageFile.deleteByItems",
            query = "DELETE FROM TblComponentInspectionVisualImageFile t WHERE t.tblComponentInspectionVisualImageFilePK.componentInspectionResultDetailId IN :componentInspectionResultDetailId ")})

public class TblComponentInspectionVisualImageFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblComponentInspectionVisualImageFilePK tblComponentInspectionVisualImageFilePK;
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
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date takenDate;
    @Column(name = "TAKEN_DATE_STZ")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date takenDateStz;
    @Size(max = 200)
    @Column(name = "REMARKS")
    private String remarks;
    @Size(max = 45)
    @Column(name = "THUMBNAIL_FILE_UUID")
    private String thumbnailFileUuid;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_DATE_UUID")
    private String createDateUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    
    @JoinColumn(name = "COMPONENT_INSPECTION_RESULT_DETAIL_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private TblComponentInspectionResultDetail resultDetail;

    public TblComponentInspectionVisualImageFile() {
    }

    public TblComponentInspectionVisualImageFilePK getTblComponentInspectionVisualImageFilePK() {
        return tblComponentInspectionVisualImageFilePK;
    }

    public void setTblComponentInspectionVisualImageFilePK(TblComponentInspectionVisualImageFilePK tblComponentInspectionVisualImageFilePK) {
        this.tblComponentInspectionVisualImageFilePK = tblComponentInspectionVisualImageFilePK;
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

    public TblComponentInspectionResultDetail getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(TblComponentInspectionResultDetail resultDetail) {
        this.resultDetail = resultDetail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblComponentInspectionVisualImageFilePK != null ? tblComponentInspectionVisualImageFilePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblComponentInspectionVisualImageFile)) {
            return false;
        }
        TblComponentInspectionVisualImageFile other = (TblComponentInspectionVisualImageFile) object;
        if ((this.tblComponentInspectionVisualImageFilePK == null && other.tblComponentInspectionVisualImageFilePK != null) || (this.tblComponentInspectionVisualImageFilePK != null && !this.tblComponentInspectionVisualImageFilePK.equals(other.tblComponentInspectionVisualImageFilePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
    
}
