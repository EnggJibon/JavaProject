/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

import com.kmcj.karte.constants.CommonConstants;
import java.math.BigDecimal;
import java.util.List;

/**
 * Result detail of one inspection item model.
 *
 * @author duanlin
 */
public class ComponentInspectionItemResultDetail {

    private String inspectionResultDetailId;
    private Integer inspectionType;
    private String inspectionItemSno;
    private String inspectionItemName;
    private Integer measurementType;
    private String measurementMethod;
    private BigDecimal dimensionValue;
    private BigDecimal tolerancePlus;
    private BigDecimal toleranceMinus;
    private Integer itemResult;
    private Integer cavityNum;
    private boolean enableThAlert = true;

    private String revisionSymbol;
    private String drawingPage;
    private String drawingAnnotation;
    private String drawingMentionNo;
    private String similarMultiitem;
    private String drawingArea;
    private String pqs;
    
    private Integer itemResultAuto; //自動判定
    private Integer itemResultManual; //手動判定
    private String manJudgeComment; //手動判定コメント
    private Integer itemResultOutgoing;
    private Integer localSeq;
    private String note; //注記
    private String itemtableDetailMethodId; //寸法ID
    private String dimensionCode; //記号
    private String dimensionNo; //No.
    private int groupNumber;
    private BigDecimal sampleMean;
    private BigDecimal sampleStdDev;
    private BigDecimal sampleSize;
    private BigDecimal sampleCp;
    private BigDecimal sampleCpk;
    private BigDecimal sampleCc;

    List<SamplingInspectionResult> samplingInspectionResults;
    private List<SamplingInspectionOutgoingResult> samplingInspectionOutgoingResults;
    private List<SeqInspectionOutgoingResult> seqInspectionOutgoingResults;
    
    //Apeng 20180208 add start
    private String itemTableDetailAdditionalFlg;
    //Apeng 20180208 add end

    public boolean isVisualSpection() {
        return CommonConstants.MEASUREMENT_TYPE_VISUAL == measurementType;
    }

    public String getInspectionResultDetailId() {
        return inspectionResultDetailId;
    }

    public void setInspectionResultDetailId(String inspectionResultDetailId) {
        this.inspectionResultDetailId = inspectionResultDetailId;
    }

    public Integer getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(Integer inspectionType) {
        this.inspectionType = inspectionType;
    }

    public String getInspectionItemSno() {
        return inspectionItemSno;
    }

    public void setInspectionItemSno(String inspectionItemSno) {
        this.inspectionItemSno = inspectionItemSno;
    }

    public String getInspectionItemName() {
        return inspectionItemName;
    }

    public void setInspectionItemName(String inspectionItemName) {
        this.inspectionItemName = inspectionItemName;
    }

    public Integer getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(Integer measurementType) {
        this.measurementType = measurementType;
    }

    public String getMeasurementMethod() {
        return measurementMethod;
    }

    public void setMeasurementMethod(String measurementMethod) {
        this.measurementMethod = measurementMethod;
    }

    public BigDecimal getDimensionValue() {
        return dimensionValue;
    }

    public void setDimensionValue(BigDecimal dimensionValue) {
        this.dimensionValue = dimensionValue;
    }

    public BigDecimal getTolerancePlus() {
        return tolerancePlus;
    }

    public void setTolerancePlus(BigDecimal tolerancePlus) {
        this.tolerancePlus = tolerancePlus;
    }

    public BigDecimal getToleranceMinus() {
        return toleranceMinus;
    }

    public void setToleranceMinus(BigDecimal toleranceMinus) {
        this.toleranceMinus = toleranceMinus;
    }

    public Integer getItemResult() {
        return itemResult;
    }

    public void setItemResult(Integer itemResult) {
        this.itemResult = itemResult;
    }

    public String getRevisionSymbol() {
        return revisionSymbol;
    }

    public void setRevisionSymbol(String revisionSymbol) {
        this.revisionSymbol = revisionSymbol;
    }

    public String getDrawingPage() {
        return drawingPage;
    }

    public void setDrawingPage(String drawingPage) {
        this.drawingPage = drawingPage;
    }

    public String getDrawingAnnotation() {
        return drawingAnnotation;
    }

    public void setDrawingAnnotation(String drawingAnnotation) {
        this.drawingAnnotation = drawingAnnotation;
    }

    public String getDrawingMentionNo() {
        return drawingMentionNo;
    }

    public void setDrawingMentionNo(String drawingMentionNo) {
        this.drawingMentionNo = drawingMentionNo;
    }

    public String getSimilarMultiitem() {
        return similarMultiitem;
    }

    public void setSimilarMultiitem(String similarMultiitem) {
        this.similarMultiitem = similarMultiitem;
    }

    public String getDrawingArea() {
        return drawingArea;
    }

    public void setDrawingArea(String drawingArea) {
        this.drawingArea = drawingArea;
    }

    public String getPqs() {
        return pqs;
    }

    public void setPqs(String pqs) {
        this.pqs = pqs;
    }

    public List<SamplingInspectionResult> getSamplingInspectionResults() {
        return samplingInspectionResults;
    }

    public void setSamplingInspectionResults(List<SamplingInspectionResult> samplingInspectionResults) {
        this.samplingInspectionResults = samplingInspectionResults;
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

    /**
     * @return the itemResultOutgoing
     */
    public Integer getItemResultOutgoing() {
        return itemResultOutgoing;
    }

    /**
     * @param itemResultOutgoing the itemResultOutgoing to set
     */
    public void setItemResultOutgoing(Integer itemResultOutgoing) {
        this.itemResultOutgoing = itemResultOutgoing;
    }

    /**
     * @return the localSeq
     */
    public Integer getLocalSeq() {
        return localSeq;
    }

    /**
     * @param localSeq the localSeq to set
     */
    public void setLocalSeq(Integer localSeq) {
        this.localSeq = localSeq;
    }

    /**
     * @return the samplingInspectionOutgoingResults
     */
    public List<SamplingInspectionOutgoingResult> getSamplingInspectionOutgoingResults() {
        return samplingInspectionOutgoingResults;
    }

    /**
     * @param samplingInspectionOutgoingResults the samplingInspectionOutgoingResults to set
     */
    public void setSamplingInspectionOutgoingResults(List<SamplingInspectionOutgoingResult> samplingInspectionOutgoingResults) {
        this.samplingInspectionOutgoingResults = samplingInspectionOutgoingResults;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the seqInspectionOutgoingResults
     */
    public List<SeqInspectionOutgoingResult> getSeqInspectionOutgoingResults() {
        return seqInspectionOutgoingResults;
    }

    /**
     * @param seqInspectionOutgoingResults the seqInspectionOutgoingResults to set
     */
    public void setSeqInspectionOutgoingResults(List<SeqInspectionOutgoingResult> seqInspectionOutgoingResults) {
        this.seqInspectionOutgoingResults = seqInspectionOutgoingResults;
    }

    /**
     * @return the itemtableDetailMethodId
     */
    public String getItemtableDetailMethodId() {
        return itemtableDetailMethodId;
    }

    /**
     * @param itemtableDetailMethodId the itemtableDetailMethodId to set
     */
    public void setItemtableDetailMethodId(String itemtableDetailMethodId) {
        this.itemtableDetailMethodId = itemtableDetailMethodId;
    }

    /**
     *
     * @param dimensionCode
     */
    public void setDimensionCode(String dimensionCode) {
        this.dimensionCode = dimensionCode;
    }

    /**
     *
     * @return dimensionCode
     */
    public String getDimensionCode() { return this.dimensionCode; }

    /**
     *
     * @param dimensionNo
     */
    public void setDimensionNo(String dimensionNo) {
        this.dimensionNo = dimensionNo;
    }

    /**
     *
     * @return dimensionNo
     */
    public String getDimensionNo() { return this.dimensionNo; }

    /**
     * @return the itemTableDetailAdditionalFlg
     */
    public String getItemTableDetailAdditionalFlg() {
        return itemTableDetailAdditionalFlg;
    }

    /**
     * @param itemTableDetailAdditionalFlg the itemTableDetailAdditionalFlg to set
     */
    public void setItemTableDetailAdditionalFlg(String itemTableDetailAdditionalFlg) {
        this.itemTableDetailAdditionalFlg = itemTableDetailAdditionalFlg;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public BigDecimal getSampleMean() {
        return sampleMean;
    }

    public void setSampleMean(BigDecimal sampleMean) {
        this.sampleMean = sampleMean;
    }

    public BigDecimal getSampleStdDev() {
        return sampleStdDev;
    }

    public void setSampleStdDev(BigDecimal sampleStdDev) {
        this.sampleStdDev = sampleStdDev;
    }

    public BigDecimal getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(BigDecimal sampleSize) {
        this.sampleSize = sampleSize;
    }

    public BigDecimal getSampleCp() {
        return sampleCp;
    }

    public void setSampleCp(BigDecimal sampleCp) {
        this.sampleCp = sampleCp;
    }

    public BigDecimal getSampleCpk() {
        return sampleCpk;
    }

    public void setSampleCpk(BigDecimal sampleCpk) {
        this.sampleCpk = sampleCpk;
    }

    public BigDecimal getSampleCc() {
        return sampleCc;
    }

    public void setSampleCc(BigDecimal sampleCc) {
        this.sampleCc = sampleCc;
    }

    public Integer getCavityNum() {
        return cavityNum;
    }

    public void setCavityNum(Integer cavityNum) {
        this.cavityNum = cavityNum;
    }

    public boolean isEnableThAlert() {
        return enableThAlert;
    }

    public void setEnableThAlert(boolean enableThAlert) {
        this.enableThAlert = enableThAlert;
    }

    /* inner class
    |========================================================================*/
    public static class SamplingInspectionResult {

        private Integer seq;
        private BigDecimal seqMeasurementResult;
        private Integer seqVisualResult;
        private BigDecimal seqMeasurementResultOutgoing;
        private Integer seqVisualResultOutgoing;
        private Integer cavityNum;

        public Integer getSeq() {
            return seq;
        }

        public void setSeq(Integer seq) {
            this.seq = seq;
        }

        public BigDecimal getSeqMeasurementResult() {
            return seqMeasurementResult;
        }

        public void setSeqMeasurementResult(BigDecimal seqMeasurementResult) {
            this.seqMeasurementResult = seqMeasurementResult;
        }

        public Integer getSeqVisualResult() {
            return seqVisualResult;
        }

        public void setSeqVisualResult(Integer seqVisualResult) {
            this.seqVisualResult = seqVisualResult;
        }

        /**
         * @return the seqMeasurementResultOutgoing
         */
        public BigDecimal getSeqMeasurementResultOutgoing() {
            return seqMeasurementResultOutgoing;
        }

        /**
         * @param seqMeasurementResultOutgoing the seqMeasurementResultOutgoing to set
         */
        public void setSeqMeasurementResultOutgoing(BigDecimal seqMeasurementResultOutgoing) {
            this.seqMeasurementResultOutgoing = seqMeasurementResultOutgoing;
        }

        /**
         * @return the seqVisualResultOutgoing
         */
        public Integer getSeqVisualResultOutgoing() {
            return seqVisualResultOutgoing;
        }

        /**
         * @param seqVisualResultOutgoing the seqVisualResultOutgoing to set
         */
        public void setSeqVisualResultOutgoing(Integer seqVisualResultOutgoing) {
            this.seqVisualResultOutgoing = seqVisualResultOutgoing;
        }

        public Integer getCavityNum() {
            return cavityNum;
        }

        public void setCavityNum(Integer cavityNum) {
            this.cavityNum = cavityNum;
        }
    }
    public static class SamplingInspectionOutgoingResult {

        private Integer seq;
        private BigDecimal seqMeasurementResult;
        private Integer seqVisualResult;
        private BigDecimal seqMeasurementResultOutgoing;
        private Integer seqVisualResultOutgoing;

        public Integer getSeq() {
            return seq;
        }

        public void setSeq(Integer seq) {
            this.seq = seq;
        }

        public BigDecimal getSeqMeasurementResult() {
            return seqMeasurementResult;
        }

        public void setSeqMeasurementResult(BigDecimal seqMeasurementResult) {
            this.seqMeasurementResult = seqMeasurementResult;
        }

        public Integer getSeqVisualResult() {
            return seqVisualResult;
        }

        public void setSeqVisualResult(Integer seqVisualResult) {
            this.seqVisualResult = seqVisualResult;
        }

        /**
         * @return the seqMeasurementResultOutgoing
         */
        public BigDecimal getSeqMeasurementResultOutgoing() {
            return seqMeasurementResultOutgoing;
        }

        /**
         * @param seqMeasurementResultOutgoing the seqMeasurementResultOutgoing to set
         */
        public void setSeqMeasurementResultOutgoing(BigDecimal seqMeasurementResultOutgoing) {
            this.seqMeasurementResultOutgoing = seqMeasurementResultOutgoing;
        }

        /**
         * @return the seqVisualResultOutgoing
         */
        public Integer getSeqVisualResultOutgoing() {
            return seqVisualResultOutgoing;
        }

        /**
         * @param seqVisualResultOutgoing the seqVisualResultOutgoing to set
         */
        public void setSeqVisualResultOutgoing(Integer seqVisualResultOutgoing) {
            this.seqVisualResultOutgoing = seqVisualResultOutgoing;
        }
    }
    
    public static class SeqInspectionOutgoingResult {
        private String inspectionItemSno;
        private Integer inspectionType;
        private String inspectionResultDetailId;

        /**
         * @return the inspectionItemSno
         */
        public String getInspectionItemSno() {
            return inspectionItemSno;
        }

        /**
         * @param inspectionItemSno the inspectionItemSno to set
         */
        public void setInspectionItemSno(String inspectionItemSno) {
            this.inspectionItemSno = inspectionItemSno;
        }

        /**
         * @return the inspectionType
         */
        public Integer getInspectionType() {
            return inspectionType;
        }

        /**
         * @param inspectionType the inspectionType to set
         */
        public void setInspectionType(Integer inspectionType) {
            this.inspectionType = inspectionType;
        }

        /**
         * @return the inspectionResultDetailId
         */
        public String getInspectionResultDetailId() {
            return inspectionResultDetailId;
        }

        /**
         * @param inspectionResultDetailId the inspectionResultDetailId to set
         */
        public void setInspectionResultDetailId(String inspectionResultDetailId) {
            this.inspectionResultDetailId = inspectionResultDetailId;
        }
        
    }

}
