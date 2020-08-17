/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.visualimage.model;

import java.util.List;

/**
 * Visual images of one inspection item model.
 * 
 * @author duanlin
 */
public class ComponentInspectionItemVisualImages {
    private String inspectionResultDetailId;
    private String inspectionItemName;
    private Integer seq;
    /** 目視検査結果　0:未設定　1:OK　2:NG*/
    private Integer visualResult;
    private Integer itemResultAuto; //自動判定
    private Integer itemResultManual; //手動判定
    private String manJudgeComment; //手動判定コメント
    
    List<InspectionVisualImage> inspectionVisualImage;

    public String getInspectionResultDetailId() {
        return inspectionResultDetailId;
    }

    public void setInspectionResultDetailId(String inspectionResultDetailId) {
        this.inspectionResultDetailId = inspectionResultDetailId;
    }

    public String getInspectionItemName() {
        return inspectionItemName;
    }

    public void setInspectionItemName(String inspectionItemName) {
        this.inspectionItemName = inspectionItemName;
    }

    public List<InspectionVisualImage> getInspectionVisualImage() {
        return inspectionVisualImage;
    }

    public void setInspectionVisualImage(List<InspectionVisualImage> inspectionVisualImage) {
        this.inspectionVisualImage = inspectionVisualImage;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getVisualResult() {
        return visualResult;
    }

    public void setVisualResult(Integer visualResult) {
        this.visualResult = visualResult;
    }

    /**
     * @return the itemResultAuto
     */
    public Integer getItemResultAuto() {
        return itemResultAuto;
    }

    /**
     * @param itemResultAuto the itemResultAuto to set
     */
    public void setItemResultAuto(Integer itemResultAuto) {
        this.itemResultAuto = itemResultAuto;
    }

    /**
     * @return the itemResultManual
     */
    public Integer getItemResultManual() {
        return itemResultManual;
    }

    /**
     * @param itemResultManual the itemResultManual to set
     */
    public void setItemResultManual(Integer itemResultManual) {
        this.itemResultManual = itemResultManual;
    }

    /**
     * @return the manJudgeComment
     */
    public String getManJudgeComment() {
        return manJudgeComment;
    }

    /**
     * @param manJudgeComment the manJudgeComment to set
     */
    public void setManJudgeComment(String manJudgeComment) {
        this.manJudgeComment = manJudgeComment;
    }

    /* inner class
    |========================================================================*/
    public static class InspectionVisualImage {
        
        
        private Integer seq;
        private String fileUuid;
        private Integer fileType;
        private String fileExtension;
        private String takenDate;
        private String takenDateStz;
        private String remarks;
        private String thumbnailFileUuid;
        private Integer detailSeq;

        public Integer getSeq() {
            return seq;
        }

        public void setSeq(Integer seq) {
            this.seq = seq;
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

        public String getTakenDate() {
            return takenDate;
        }

        public void setTakenDate(String takenDate) {
            this.takenDate = takenDate;
        }

        public String getTakenDateStz() {
            return takenDateStz;
        }

        public void setTakenDateStz(String takenDateStz) {
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

        public Integer getDetailSeq() {
            return detailSeq;
        }

        public void setDetailSeq(Integer detailSeq) {
            this.detailSeq = detailSeq;
        }
    }

}
