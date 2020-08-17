/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.issue;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author jiangxs
 */
public class TblIssueImageFileVo extends BasicResponse {
    
    private String issueId;
    private int seq;
    private String fileUuid;
    private Integer fileType;
    private String fileExtension;
    private String takenDate;
    private String takenDateStz;
    private String remarks;
    private String thumbnailFileUuid;
    private Date createDate;
    private Date updateDate;
    private String createDateUuid;
    private String updateUserUuid;

    /**
     * @return the issueId
     */
    public String getIssueId() {
        return issueId;
    }

    /**
     * @param issueId the issueId to set
     */
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    /**
     * @return the seq
     */
    public int getSeq() {
        return seq;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(int seq) {
        this.seq = seq;
    }

    /**
     * @return the fileUuid
     */
    public String getFileUuid() {
        return fileUuid;
    }

    /**
     * @param fileUuid the fileUuid to set
     */
    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    /**
     * @return the fileType
     */
    public Integer getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the fileExtension
     */
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * @param fileExtension the fileExtension to set
     */
    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    

    /**
     * @return the remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the updateDate
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the createDateUuid
     */
    public String getCreateDateUuid() {
        return createDateUuid;
    }

    /**
     * @param createDateUuid the createDateUuid to set
     */
    public void setCreateDateUuid(String createDateUuid) {
        this.createDateUuid = createDateUuid;
    }

    /**
     * @return the updateUserUuid
     */
    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    /**
     * @param updateUserUuid the updateUserUuid to set
     */
    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
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

    /**
     * @return the takenDate
     */
    public String getTakenDate() {
        return takenDate;
    }

    /**
     * @param takenDate the takenDate to set
     */
    public void setTakenDate(String takenDate) {
        this.takenDate = takenDate;
    }

    /**
     * @return the takenDateStz
     */
    public String getTakenDateStz() {
        return takenDateStz;
    }

    /**
     * @param takenDateStz the takenDateStz to set
     */
    public void setTakenDateStz(String takenDateStz) {
        this.takenDateStz = takenDateStz;
    }
}
