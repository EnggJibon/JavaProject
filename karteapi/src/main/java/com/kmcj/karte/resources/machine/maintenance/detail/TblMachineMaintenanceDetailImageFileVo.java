package com.kmcj.karte.resources.machine.maintenance.detail;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author zds
 */
public class TblMachineMaintenanceDetailImageFileVo extends BasicResponse {

    private String maintenanceDetailId;
    private String seq;
    private String fileUuid;
    private String fileType;
    private String fileExtension;
    private Date takenDate;
    private String takenDateStr;
    private Date takenDateStz;
    private String takenDateStzStr;
    private String remarks;
    private String thumbnailFileUuid;
    private Date createDate;
    private String createDateStr;
    private Date updateDate;
    private String updateDateStr;
    private String createDateUuid;
    private String updateUserUuid;

    private TblMachineMaintenanceDetail tblMachineMaintenanceDetail;

    public TblMachineMaintenanceDetailImageFileVo() {
    }

    public void setMaintenanceDetailId(String maintenanceDetailId) {
        this.maintenanceDetailId = maintenanceDetailId;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getMaintenanceDetailId() {
        return maintenanceDetailId;
    }

    public String getSeq() {
        return seq;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
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

    public void setTakenDateStr(String takenDateStr) {
        this.takenDateStr = takenDateStr;
    }

    public void setTakenDateStzStr(String takenDateStzStr) {
        this.takenDateStzStr = takenDateStzStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    public void setTblMachineMaintenanceDetail(TblMachineMaintenanceDetail tblMachineMaintenanceDetail) {
        this.tblMachineMaintenanceDetail = tblMachineMaintenanceDetail;
    }

    public String getTakenDateStr() {
        return takenDateStr;
    }

    public String getTakenDateStzStr() {
        return takenDateStzStr;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public TblMachineMaintenanceDetail getTblMachineMaintenanceDetail() {
        return tblMachineMaintenanceDetail;
    }

}
