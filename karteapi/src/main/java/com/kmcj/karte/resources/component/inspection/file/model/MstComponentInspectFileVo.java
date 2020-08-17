/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.file.model;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 *
 * @author zf
 */
public class MstComponentInspectFileVo extends BasicResponse{
    
        
    private String inspectTypeId;
    
    private String inspectClassId;
    
    private Character drawingFlg;
    
    private Character proofFlg;
    
    private Character rohsProofFlg;
    
    private Character packageSpecFlg;
    
    private Character qcPhaseFlg;
    
    private String dictkey;
    
    private String dictValue;
    
    private String operationFlag; // 1:delete,3:update,4:add
    
    private Date createDate;
    
    private Integer seq;

    //update KM-567
    private Character file06Flg;

    private Character file07Flg;

    private Character file08Flg;

    private Character file09Flg;

    private Character file10Flg;

    private Character file11Flg;

    private Character file12Flg;

    private Character file13Flg;

    private Character file14Flg;

    private Character file15Flg;

    private Character file16Flg;

    private Character file17Flg;

    private Character file18Flg;

    private Character file19Flg;

    private Character file20Flg;
    
    public MstComponentInspectFileVo() {
    }

    /**
     * @return the inspectTypeId
     */
    public String getInspectTypeId() {
        return inspectTypeId;
    }

    /**
     * @param inspectTypeId the inspectTypeId to set
     */
    public void setInspectTypeId(String inspectTypeId) {
        this.inspectTypeId = inspectTypeId;
    }

    /**
     * @return the inspectClassId
     */
    public String getInspectClassId() {
        return inspectClassId;
    }

    /**
     * @param inspectClassId the inspectClassId to set
     */
    public void setInspectClassId(String inspectClassId) {
        this.inspectClassId = inspectClassId;
    }

    /**
     * @return the drawingFlg
     */
    public Character getDrawingFlg() {
        return drawingFlg;
    }

    /**
     * @param drawingFlg the drawingFlg to set
     */
    public void setDrawingFlg(Character drawingFlg) {
        this.drawingFlg = drawingFlg;
    }

    /**
     * @return the proofFlg
     */
    public Character getProofFlg() {
        return proofFlg;
    }

    /**
     * @param proofFlg the proofFlg to set
     */
    public void setProofFlg(Character proofFlg) {
        this.proofFlg = proofFlg;
    }

    /**
     * @return the rohsProofFlg
     */
    public Character getRohsProofFlg() {
        return rohsProofFlg;
    }

    /**
     * @param rohsProofFlg the rohsProofFlg to set
     */
    public void setRohsProofFlg(Character rohsProofFlg) {
        this.rohsProofFlg = rohsProofFlg;
    }

    /**
     * @return the packageSpecFlg
     */
    public Character getPackageSpecFlg() {
        return packageSpecFlg;
    }

    /**
     * @param packageSpecFlg the packageSpecFlg to set
     */
    public void setPackageSpecFlg(Character packageSpecFlg) {
        this.packageSpecFlg = packageSpecFlg;
    }

    /**
     * @return the qcPhaseFlg
     */
    public Character getQcPhaseFlg() {
        return qcPhaseFlg;
    }

    /**
     * @param qcPhaseFlg the qcPhaseFlg to set
     */
    public void setQcPhaseFlg(Character qcPhaseFlg) {
        this.qcPhaseFlg = qcPhaseFlg;
    }

    /**
     * @return the operationFlag
     */
    public String getOperationFlag() {
        return operationFlag;
    }

    /**
     * @param operationFlag the operationFlag to set
     */
    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    /**
     * @return the dictkey
     */
    public String getDictkey() {
        return dictkey;
    }

    /**
     * @param dictkey the dictkey to set
     */
    public void setDictkey(String dictkey) {
        this.dictkey = dictkey;
    }

    /**
     * @return the dictValue
     */
    public String getDictValue() {
        return dictValue;
    }

    /**
     * @param dictValue the dictValue to set
     */
    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
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
     * @return the seq
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public void setFile06Flg(Character file06Flg) { this.file06Flg = file06Flg; }

    public Character getFile06Flg() { return  this.file06Flg; }

    public void setFile07Flg(Character file07Flg) { this.file07Flg = file07Flg; }

    public Character getFile07Flg() { return this.file07Flg; }

    public void setFile08Flg(Character file08Flg) { this.file08Flg = file08Flg; }

    public Character getFile08Flg() { return this.file08Flg; }

    public void setFile09Flg(Character file09Flg) { this.file09Flg = file09Flg; }

    public Character getFile09Flg() { return this.file09Flg; }

    public void setFile10Flg(Character file10Flg) { this.file10Flg = file10Flg; }

    public Character getFile10Flg() { return this.file10Flg; }

    public void setFile11Flg(Character file11Flg) { this.file11Flg = file11Flg; }

    public Character getFile11Flg() { return this.file11Flg; }

    public void setFile12Flg(Character file12Flg) { this.file12Flg = file12Flg; }

    public Character getFile12Flg() { return this.file12Flg; }

    public void setFile13Flg(Character file13Flg) { this.file13Flg = file13Flg; }

    public Character getFile13Flg() { return this.file13Flg; }

    public void setFile14Flg(Character file14Flg) { this.file14Flg = file14Flg; }

    public Character getFile14Flg() { return this.file14Flg; }

    public void setFile15Flg(Character file15Flg) { this.file15Flg = file15Flg; }

    public Character getFile15Flg() { return this.file15Flg; }

    public void setFile16Flg(Character file16Flg) { this.file16Flg = file16Flg; }

    public Character getFile16Flg() { return this.file16Flg; }

    public void setFile17Flg(Character file17Flg) { this.file17Flg = file17Flg; }

    public Character getFile17Flg() { return this.file17Flg; }

    public void setFile18Flg(Character file18Flg) { this.file18Flg = file18Flg; }

    public Character getFile18Flg() { return this.file18Flg; }

    public void setFile19Flg(Character file19Flg) { this.file19Flg = file19Flg; }

    public Character getFile19Flg() { return this.file19Flg; }

    public void setFile20Flg(Character file20Flg) { this.file20Flg = file20Flg; }

    public Character getFile20Flg() { return  this.file20Flg; }
}
