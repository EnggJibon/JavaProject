package com.kmcj.karte.resources.machine.remodeling.detail;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author zds
 */
public class TblMachineRemodelingDetailImageFileVo extends BasicResponse {

    private String remodelingDetailId;
    private String seq;
    private String fileUuid;
    private String fileType;
    private String fileExtension;
    private Date takenDate;
    private Date takenDateStz;
    private String takenDateStr;
    private String takenDateStzStr;
    private String remarks;
    private String thumbnailFileUuid;
    private Date createDate;
    private Date updateDate;
    private String createDateStr;
    private String updateDateStr;
    private String createDateUuid;
    private String updateUserUuid;

    private TblMachineRemodelingDetail tblMachineRemodelingDetail;

    public TblMachineRemodelingDetailImageFileVo() {
    }

    public String getRemodelingDetailId() {
        return remodelingDetailId;
    }

    public String getSeq() {
        return seq;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public String getFileType() {
        return fileType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public Date getTakenDate() {
        return takenDate;
    }

    public Date getTakenDateStz() {
        return takenDateStz;
    }

    public String getTakenDateStr() {
        return takenDateStr;
    }

    public String getTakenDateStzStr() {
        return takenDateStzStr;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getThumbnailFileUuid() {
        return thumbnailFileUuid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public String getCreateDateUuid() {
        return createDateUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public TblMachineRemodelingDetail getTblMachineRemodelingDetail() {
        return tblMachineRemodelingDetail;
    }

    public void setRemodelingDetailId(String remodelingDetailId) {
        this.remodelingDetailId = remodelingDetailId;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public void setTakenDate(Date takenDate) {
        this.takenDate = takenDate;
    }

    public void setTakenDateStz(Date takenDateStz) {
        this.takenDateStz = takenDateStz;
    }

    public void setTakenDateStr(String takenDateStr) {
        this.takenDateStr = takenDateStr;
    }

    public void setTakenDateStzStr(String takenDateStzStr) {
        this.takenDateStzStr = takenDateStzStr;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setThumbnailFileUuid(String thumbnailFileUuid) {
        this.thumbnailFileUuid = thumbnailFileUuid;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public void setCreateDateUuid(String createDateUuid) {
        this.createDateUuid = createDateUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setTblMachineRemodelingDetail(TblMachineRemodelingDetail tblMachineRemodelingDetail) {
        this.tblMachineRemodelingDetail = tblMachineRemodelingDetail;
    }

}
