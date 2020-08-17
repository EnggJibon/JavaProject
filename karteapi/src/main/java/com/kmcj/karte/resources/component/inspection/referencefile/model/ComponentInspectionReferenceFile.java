/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.referencefile.model;

import com.kmcj.karte.BasicResponse;

import java.util.Date;

/**
 * Component inspection reference file
 * 
 * @author duanlin
 */
public class ComponentInspectionReferenceFile extends BasicResponse {
    private String componentId;
    private String componentInspectionResultId; //部品検査結果UUID
    private String componentInspectionItemsTableId; //部品検査項目表マスタID

    private String drawingFileUuid; //図面ファイルUUID
    private String proofFileUuid; //材料証明ファイルUUID
    private String rohsProofFileUuid; //RoHS適用証明ファイルUUID
    private String packageSpecFileUuid; //包装仕様書ファイルUUID
    private String qcPhaseFileUuid; //QC工程表ファイルUUID
    private Integer inspectionStatus;
    private Boolean existFlag;
    private String firstFlag;//检查区分ID

    private String drawingFileStatus; //図面ファイルステータス　'DEFAULT'：初期状態　'CONFIRMED'：確認済　'DENIED'：否認済　'SKIP'：確認不要
    private String proofFileStatus; //材料証明ファイルステータス　'DEFAULT'：初期状態　'CONFIRMED'：確認済　'DENIED'：否認済　'SKIP'：確認不要
    private String rohsProofFileStatus; //ROHSファイルステータス　'DEFAULT'：初期状態　'CONFIRMED'：確認済　'DENIED'：否認済　'SKIP'：確認不要
    private String packageSpecFileStatus; //包装仕様書ファイルステータス　'DEFAULT'：初期状態　'CONFIRMED'：確認済　'DENIED'：否認済　'SKIP'：確認不要
    private String qcPhaseFileStatus; //QC工程表ファイルステータス　'DEFAULT'：初期状態　'CONFIRMED'：確認済　'DENIED'：否認済　'SKIP'：確認不要

    private Date drawingFileDate; //図面ファイル登録日
    private Date proofFileDate; //材料証明ファイル登録日
    private Date rohsProofFileDate; //ROHSファイル登録日
    private Date packageSpecFileDate; //包装仕様書ファイル登録日
    private Date qcPhaseFileDate; //QC工程表ファイル登録日

    private String file06FileUuid;
    private String file07FileUuid;
    private String file08FileUuid;
    private String file09FileUuid;
    private String file10FileUuid;
    private String file11FileUuid;
    private String file12FileUuid;
    private String file13FileUuid;
    private String file14FileUuid;
    private String file15FileUuid;
    private String file16FileUuid;
    private String file17FileUuid;
    private String file18FileUuid;
    private String file19FileUuid;
    private String file20FileUuid;

    private String file06FileStatus;
    private String file07FileStatus;
    private String file08FileStatus;
    private String file09FileStatus;
    private String file10FileStatus;
    private String file11FileStatus;
    private String file12FileStatus;
    private String file13FileStatus;
    private String file14FileStatus;
    private String file15FileStatus;
    private String file16FileStatus;
    private String file17FileStatus;
    private String file18FileStatus;
    private String file19FileStatus;
    private String file20FileStatus;

    private Date file06FileDate;
    private Date file07FileDate;
    private Date file08FileDate;
    private Date file09FileDate;
    private Date file10FileDate;
    private Date file11FileDate;
    private Date file12FileDate;
    private Date file13FileDate;
    private Date file14FileDate;
    private Date file15FileDate;
    private Date file16FileDate;
    private Date file17FileDate;
    private Date file18FileDate;
    private Date file19FileDate;
    private Date file20FileDate;

    private Integer actionFileType = 0;
    private Integer saveFileType = 0;
    private String inspecFileUuid;
    
    private String drawingFileConfirmerId;
    private String drawingFileConfirmerName;
    private String proofFileConfirmerId;
    private String proofFileConfirmerName;
    private String rohsProofFileConfirmerId;
    private String rohsProofFileConfirmerName;
    private String packageSpecFileConfirmerId;
    private String packageSpecFileConfirmerName;
    private String qcPhaseFileConfirmerId;
    private String qcPhaseFileConfirmerName;
    private String file06FileConfirmerId;
    private String file06FileConfirmerName;
    private String file07FileConfirmerId;
    private String file07FileConfirmerName;
    private String file08FileConfirmerId;
    private String file08FileConfirmerName;
    private String file09FileConfirmerId;
    private String file09FileConfirmerName;
    private String file10FileConfirmerId;
    private String file10FileConfirmerName;
    private String file11FileConfirmerId;
    private String file11FileConfirmerName;
    private String file12FileConfirmerId;
    private String file12FileConfirmerName;
    private String file13FileConfirmerId;
    private String file13FileConfirmerName;
    private String file14FileConfirmerId;
    private String file14FileConfirmerName;
    private String file15FileConfirmerId;
    private String file15FileConfirmerName;
    private String file16FileConfirmerId;
    private String file16FileConfirmerName;
    private String file17FileConfirmerId;
    private String file17FileConfirmerName;
    private String file18FileConfirmerId;
    private String file18FileConfirmerName;
    private String file19FileConfirmerId;
    private String file19FileConfirmerName;
    private String file20FileConfirmerId;
    private String file20FileConfirmerName;
    private Integer inspectionType;

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentInspectionResultId() {
        return componentInspectionResultId; 
    }

    public void setComponentInspectionResultId(String componentInspectionResultId) {
        this.componentInspectionResultId = componentInspectionResultId;
    }

    public String getDrawingFileUuid() {
        return drawingFileUuid == null ? "" : drawingFileUuid;
    }

    public void setDrawingFileUuid(String drawingFileUuid) {
        this.drawingFileUuid = drawingFileUuid;
    }

    public String getProofFileUuid() {
        return proofFileUuid == null ? "" : proofFileUuid;
    }

    public void setProofFileUuid(String proofFileUuid) {
        this.proofFileUuid = proofFileUuid;
    }

    public String getRohsProofFileUuid() {
        return rohsProofFileUuid == null ? "" : rohsProofFileUuid;
    }

    public void setRohsProofFileUuid(String rohsProofFileUuid) {
        this.rohsProofFileUuid = rohsProofFileUuid;
    }

    public String getPackageSpecFileUuid() {
        return packageSpecFileUuid == null ? "" : packageSpecFileUuid;
    }

    public void setPackageSpecFileUuid(String packageSpecFileUuid) {
        this.packageSpecFileUuid = packageSpecFileUuid;
    }

    public String getQcPhaseFileUuid() {
        return qcPhaseFileUuid == null ? "" : qcPhaseFileUuid;
    }

    public void setQcPhaseFileUuid(String qcPhaseFileUuid) {
        this.qcPhaseFileUuid = qcPhaseFileUuid;
    }

    public Boolean getExistFlag() {
        return existFlag;
    }

    public void setExistFlag(Boolean existFlag) {
        this.existFlag = existFlag;
    }

    /**
     * @return the inspectionStatus
     */
    public Integer getInspectionStatus() {
        return inspectionStatus;
    }

    /**
     * @param inspectionStatus the inspectionStatus to set
     */
    public void setInspectionStatus(Integer inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }

    /**
     * @return the firstFlag
     */
    public String getFirstFlag() {
        return firstFlag;
    }

    /**
     * @param firstFlag the firstFlag to set
     */
    public void setFirstFlag(String firstFlag) {
        this.firstFlag = firstFlag;
    }

    /**
     * @return the componentInspectionItemsTableId
     */
    public String getComponentInspectionItemsTableId() {
        return componentInspectionItemsTableId;
    }

    /**
     * @param componentInspectionItemsTableId the componentInspectionItemsTableId to set
     */
    public void setComponentInspectionItemsTableId(String componentInspectionItemsTableId) {
        this.componentInspectionItemsTableId = componentInspectionItemsTableId;
    }

    public Date getDrawingFileDate() {
        return drawingFileDate == null ? null : drawingFileDate;
    }

    public Date getPackageSpecFileDate() {
        return packageSpecFileDate;
    }

    public Date getProofFileDate() {
        return proofFileDate;
    }

    public Date getQcPhaseFileDate() {
        return qcPhaseFileDate;
    }

    public Date getRohsProofFileDate() {
        return rohsProofFileDate;
    }

    public void setDrawingFileDate(Date drawingFileDate) {
        this.drawingFileDate = drawingFileDate;
    }

    public void setPackageSpecFileDate(Date packageSpecFileDate) {
        this.packageSpecFileDate = packageSpecFileDate;
    }

    public void setProofFileDate(Date proofFileDate) {
        this.proofFileDate = proofFileDate;
    }

    public void setQcPhaseFileDate(Date qcPhaseFileDate) {
        this.qcPhaseFileDate = qcPhaseFileDate;
    }

    public void setRohsProofFileDate(Date rohsProofFileDate) {
        this.rohsProofFileDate = rohsProofFileDate;
    }

    public String getDrawingFileStatus() {
        return drawingFileStatus;
    }

    public String getPackageSpecFileStatus() {
        return packageSpecFileStatus;
    }

    public String getProofFileStatus() {
        return proofFileStatus;
    }

    public String getQcPhaseFileStatus() {
        return qcPhaseFileStatus;
    }

    public String getRohsProofFileStatus() {
        return rohsProofFileStatus;
    }

    public void setDrawingFileStatus(String drawingFileStatus) {
        this.drawingFileStatus = drawingFileStatus;
    }

    public void setPackageSpecFileStatus(String packageSpecFileStatus) {
        this.packageSpecFileStatus = packageSpecFileStatus;
    }

    public void setProofFileStatus(String proofFileStatus) {
        this.proofFileStatus = proofFileStatus;
    }

    public void setRohsProofFileStatus(String rohsProofFileStatus) {
        this.rohsProofFileStatus = rohsProofFileStatus;
    }

    public void setQcPhaseFileStatus(String qcPhaseFileStatus) {
        this.qcPhaseFileStatus = qcPhaseFileStatus;
    }

    public String getFile06FileUuid() {
        return file06FileUuid;
    }

    public void setFile06FileUuid(String file06FileUuid) {
        this.file06FileUuid = file06FileUuid;
    }

    public String getFile07FileUuid() {
        return file07FileUuid;
    }

    public void setFile07FileUuid(String file07FileUuid) {
        this.file07FileUuid = file07FileUuid;
    }

    public String getFile08FileUuid() {
        return file08FileUuid;
    }

    public void setFile08FileUuid(String file08FileUuid) {
        this.file08FileUuid = file08FileUuid;
    }

    public String getFile09FileUuid() {
        return file09FileUuid;
    }

    public void setFile09FileUuid(String file09FileUuid) {
        this.file09FileUuid = file09FileUuid;
    }

    public String getFile10FileUuid() {
        return file10FileUuid;
    }

    public void setFile10FileUuid(String file10FileUuid) {
        this.file10FileUuid = file10FileUuid;
    }

    public String getFile11FileUuid() {
        return file11FileUuid;
    }

    public void setFile11FileUuid(String file11FileUuid) {
        this.file11FileUuid = file11FileUuid;
    }

    public String getFile12FileUuid() {
        return file12FileUuid;
    }

    public void setFile12FileUuid(String file12FileUuid) {
        this.file12FileUuid = file12FileUuid;
    }

    public String getFile13FileUuid() {
        return file13FileUuid;
    }

    public void setFile13FileUuid(String file13FileUuid) {
        this.file13FileUuid = file13FileUuid;
    }

    public String getFile14FileUuid() {
        return file14FileUuid;
    }

    public void setFile14FileUuid(String file14FileUuid) {
        this.file14FileUuid = file14FileUuid;
    }

    public String getFile15FileUuid() {
        return file15FileUuid;
    }

    public void setFile15FileUuid(String file15FileUuid) {
        this.file15FileUuid = file15FileUuid;
    }

    public String getFile16FileUuid() {
        return file16FileUuid;
    }

    public void setFile16FileUuid(String file16FileUuid) {
        this.file16FileUuid = file16FileUuid;
    }

    public String getFile17FileUuid() {
        return file17FileUuid;
    }

    public void setFile17FileUuid(String file17FileUuid) {
        this.file17FileUuid = file17FileUuid;
    }

    public String getFile18FileUuid() {
        return file18FileUuid;
    }

    public void setFile18FileUuid(String file18FileUuid) {
        this.file18FileUuid = file18FileUuid;
    }

    public String getFile19FileUuid() {
        return file19FileUuid;
    }

    public void setFile19FileUuid(String file19FileUuid) {
        this.file19FileUuid = file19FileUuid;
    }

    public String getFile20FileUuid() {
        return file20FileUuid;
    }

    public void setFile20FileUuid(String file20FileUuid) {
        this.file20FileUuid = file20FileUuid;
    }

    public String getFile06FileStatus() {
        return file06FileStatus;
    }

    public void setFile06FileStatus(String file06FileStatus) {
        this.file06FileStatus = file06FileStatus;
    }

    public String getFile07FileStatus() {
        return file07FileStatus;
    }

    public void setFile07FileStatus(String file07FileStatus) {
        this.file07FileStatus = file07FileStatus;
    }

    public String getFile08FileStatus() {
        return file08FileStatus;
    }

    public void setFile08FileStatus(String file08FileStatus) {
        this.file08FileStatus = file08FileStatus;
    }

    public String getFile09FileStatus() {
        return file09FileStatus;
    }

    public void setFile09FileStatus(String file09FileStatus) {
        this.file09FileStatus = file09FileStatus;
    }

    public String getFile10FileStatus() {
        return file10FileStatus;
    }

    public void setFile10FileStatus(String file10FileStatus) {
        this.file10FileStatus = file10FileStatus;
    }

    public String getFile11FileStatus() {
        return file11FileStatus;
    }

    public void setFile11FileStatus(String file11FileStatus) {
        this.file11FileStatus = file11FileStatus;
    }

    public String getFile12FileStatus() {
        return file12FileStatus;
    }

    public void setFile12FileStatus(String file12FileStatus) {
        this.file12FileStatus = file12FileStatus;
    }

    public String getFile13FileStatus() {
        return file13FileStatus;
    }

    public void setFile13FileStatus(String file13FileStatus) {
        this.file13FileStatus = file13FileStatus;
    }

    public String getFile14FileStatus() {
        return file14FileStatus;
    }

    public void setFile14FileStatus(String file14FileStatus) {
        this.file14FileStatus = file14FileStatus;
    }

    public String getFile15FileStatus() {
        return file15FileStatus;
    }

    public void setFile15FileStatus(String file15FileStatus) {
        this.file15FileStatus = file15FileStatus;
    }

    public String getFile16FileStatus() {
        return file16FileStatus;
    }

    public void setFile16FileStatus(String file16FileStatus) {
        this.file16FileStatus = file16FileStatus;
    }

    public String getFile17FileStatus() {
        return file17FileStatus;
    }

    public void setFile17FileStatus(String file17FileStatus) {
        this.file17FileStatus = file17FileStatus;
    }

    public String getFile18FileStatus() {
        return file18FileStatus;
    }

    public void setFile18FileStatus(String file18FileStatus) {
        this.file18FileStatus = file18FileStatus;
    }

    public String getFile19FileStatus() {
        return file19FileStatus;
    }

    public void setFile19FileStatus(String file19FileStatus) {
        this.file19FileStatus = file19FileStatus;
    }

    public String getFile20FileStatus() {
        return file20FileStatus;
    }

    public void setFile20FileStatus(String file20FileStatus) {
        this.file20FileStatus = file20FileStatus;
    }

    public Date getFile07FileDate() {
        return file07FileDate;
    }

    public Date getFile06FileDate() {
        return file06FileDate;
    }

    public void setFile06FileDate(Date file06FileDate) {
        this.file06FileDate = file06FileDate;
    }

    public void setFile07FileDate(Date file07FileDate) {
        this.file07FileDate = file07FileDate;
    }

    public Date getFile08FileDate() {
        return file08FileDate;
    }

    public void setFile08FileDate(Date file08FileDate) {
        this.file08FileDate = file08FileDate;
    }

    public Date getFile09FileDate() {
        return file09FileDate;
    }

    public void setFile09FileDate(Date file09FileDate) {
        this.file09FileDate = file09FileDate;
    }

    public Date getFile10FileDate() {
        return file10FileDate;
    }

    public void setFile10FileDate(Date file10FileDate) {
        this.file10FileDate = file10FileDate;
    }

    public Date getFile11FileDate() {
        return file11FileDate;
    }

    public void setFile11FileDate(Date file11FileDate) {
        this.file11FileDate = file11FileDate;
    }

    public Date getFile12FileDate() {
        return file12FileDate;
    }

    public void setFile12FileDate(Date file12FileDate) {
        this.file12FileDate = file12FileDate;
    }

    public Date getFile13FileDate() {
        return file13FileDate;
    }

    public void setFile13FileDate(Date file13FileDate) {
        this.file13FileDate = file13FileDate;
    }

    public Date getFile14FileDate() {
        return file14FileDate;
    }

    public void setFile14FileDate(Date file14FileDate) {
        this.file14FileDate = file14FileDate;
    }

    public Date getFile15FileDate() {
        return file15FileDate;
    }

    public void setFile15FileDate(Date file15FileDate) {
        this.file15FileDate = file15FileDate;
    }

    public Date getFile16FileDate() {
        return file16FileDate;
    }

    public void setFile16FileDate(Date file16FileDate) {
        this.file16FileDate = file16FileDate;
    }

    public Date getFile17FileDate() {
        return file17FileDate;
    }

    public void setFile17FileDate(Date file17FileDate) {
        this.file17FileDate = file17FileDate;
    }

    public Date getFile18FileDate() {
        return file18FileDate;
    }

    public void setFile18FileDate(Date file18FileDate) {
        this.file18FileDate = file18FileDate;
    }

    public Date getFile19FileDate() {
        return file19FileDate;
    }

    public void setFile19FileDate(Date file19FileDate) {
        this.file19FileDate = file19FileDate;
    }

    public Date getFile20FileDate() {
        return file20FileDate;
    }

    public void setFile20FileDate(Date file20FileDate) {
        this.file20FileDate = file20FileDate;
    }

    public String getDrawingFileConfirmerId() {
        return drawingFileConfirmerId;
    }

    public void setDrawingFileConfirmerId(String drawingFileConfirmerId) {
        this.drawingFileConfirmerId = drawingFileConfirmerId;
    }

    public String getDrawingFileConfirmerName() {
        return drawingFileConfirmerName;
    }

    public void setDrawingFileConfirmerName(String drawingFileConfirmerName) {
        this.drawingFileConfirmerName = drawingFileConfirmerName;
    }

    public String getProofFileConfirmerId() {
        return proofFileConfirmerId;
    }

    public void setProofFileConfirmerId(String proofFileConfirmerId) {
        this.proofFileConfirmerId = proofFileConfirmerId;
    }

    public String getProofFileConfirmerName() {
        return proofFileConfirmerName;
    }

    public void setProofFileConfirmerName(String proofFileConfirmerName) {
        this.proofFileConfirmerName = proofFileConfirmerName;
    }

    public String getRohsProofFileConfirmerId() {
        return rohsProofFileConfirmerId;
    }

    public void setRohsProofFileConfirmerId(String rohsProofFileConfirmerId) {
        this.rohsProofFileConfirmerId = rohsProofFileConfirmerId;
    }

    public String getRohsProofFileConfirmerName() {
        return rohsProofFileConfirmerName;
    }

    public void setRohsProofFileConfirmerName(String rohsProofFileConfirmerName) {
        this.rohsProofFileConfirmerName = rohsProofFileConfirmerName;
    }

    public String getPackageSpecFileConfirmerId() {
        return packageSpecFileConfirmerId;
    }

    public void setPackageSpecFileConfirmerId(String packageSpecFileConfirmerId) {
        this.packageSpecFileConfirmerId = packageSpecFileConfirmerId;
    }

    public String getPackageSpecFileConfirmerName() {
        return packageSpecFileConfirmerName;
    }

    public void setPackageSpecFileConfirmerName(String packageSpecFileConfirmerName) {
        this.packageSpecFileConfirmerName = packageSpecFileConfirmerName;
    }

    public String getQcPhaseFileConfirmerId() {
        return qcPhaseFileConfirmerId;
    }

    public void setQcPhaseFileConfirmerId(String qcPhaseFileConfirmerId) {
        this.qcPhaseFileConfirmerId = qcPhaseFileConfirmerId;
    }

    public String getQcPhaseFileConfirmerName() {
        return qcPhaseFileConfirmerName;
    }

    public void setQcPhaseFileConfirmerName(String qcPhaseFileConfirmerName) {
        this.qcPhaseFileConfirmerName = qcPhaseFileConfirmerName;
    }

    public String getFile06FileConfirmerId() {
        return file06FileConfirmerId;
    }

    public void setFile06FileConfirmerId(String file06FileConfirmerId) {
        this.file06FileConfirmerId = file06FileConfirmerId;
    }

    public String getFile06FileConfirmerName() {
        return file06FileConfirmerName;
    }

    public void setFile06FileConfirmerName(String file06FileConfirmerName) {
        this.file06FileConfirmerName = file06FileConfirmerName;
    }

    public String getFile07FileConfirmerId() {
        return file07FileConfirmerId;
    }

    public void setFile07FileConfirmerId(String file07FileConfirmerId) {
        this.file07FileConfirmerId = file07FileConfirmerId;
    }

    public String getFile07FileConfirmerName() {
        return file07FileConfirmerName;
    }

    public void setFile07FileConfirmerName(String file07FileConfirmerName) {
        this.file07FileConfirmerName = file07FileConfirmerName;
    }

    public String getFile08FileConfirmerId() {
        return file08FileConfirmerId;
    }

    public void setFile08FileConfirmerId(String file08FileConfirmerId) {
        this.file08FileConfirmerId = file08FileConfirmerId;
    }

    public String getFile08FileConfirmerName() {
        return file08FileConfirmerName;
    }

    public void setFile08FileConfirmerName(String file08FileConfirmerName) {
        this.file08FileConfirmerName = file08FileConfirmerName;
    }

    public String getFile09FileConfirmerId() {
        return file09FileConfirmerId;
    }

    public void setFile09FileConfirmerId(String file09FileConfirmerId) {
        this.file09FileConfirmerId = file09FileConfirmerId;
    }

    public String getFile09FileConfirmerName() {
        return file09FileConfirmerName;
    }

    public void setFile09FileConfirmerName(String file09FileConfirmerName) {
        this.file09FileConfirmerName = file09FileConfirmerName;
    }

    public String getFile10FileConfirmerId() {
        return file10FileConfirmerId;
    }

    public void setFile10FileConfirmerId(String file10FileConfirmerId) {
        this.file10FileConfirmerId = file10FileConfirmerId;
    }

    public String getFile10FileConfirmerName() {
        return file10FileConfirmerName;
    }

    public void setFile10FileConfirmerName(String file10FileConfirmerName) {
        this.file10FileConfirmerName = file10FileConfirmerName;
    }

    public String getFile11FileConfirmerId() {
        return file11FileConfirmerId;
    }

    public void setFile11FileConfirmerId(String file11FileConfirmerId) {
        this.file11FileConfirmerId = file11FileConfirmerId;
    }

    public String getFile11FileConfirmerName() {
        return file11FileConfirmerName;
    }

    public void setFile11FileConfirmerName(String file11FileConfirmerName) {
        this.file11FileConfirmerName = file11FileConfirmerName;
    }

    public String getFile12FileConfirmerId() {
        return file12FileConfirmerId;
    }

    public void setFile12FileConfirmerId(String file12FileConfirmerId) {
        this.file12FileConfirmerId = file12FileConfirmerId;
    }

    public String getFile12FileConfirmerName() {
        return file12FileConfirmerName;
    }

    public void setFile12FileConfirmerName(String file12FileConfirmerName) {
        this.file12FileConfirmerName = file12FileConfirmerName;
    }

    public String getFile13FileConfirmerId() {
        return file13FileConfirmerId;
    }

    public void setFile13FileConfirmerId(String file13FileConfirmerId) {
        this.file13FileConfirmerId = file13FileConfirmerId;
    }

    public String getFile13FileConfirmerName() {
        return file13FileConfirmerName;
    }

    public void setFile13FileConfirmerName(String file13FileConfirmerName) {
        this.file13FileConfirmerName = file13FileConfirmerName;
    }

    public String getFile14FileConfirmerId() {
        return file14FileConfirmerId;
    }

    public void setFile14FileConfirmerId(String file14FileConfirmerId) {
        this.file14FileConfirmerId = file14FileConfirmerId;
    }

    public String getFile14FileConfirmerName() {
        return file14FileConfirmerName;
    }

    public void setFile14FileConfirmerName(String file14FileConfirmerName) {
        this.file14FileConfirmerName = file14FileConfirmerName;
    }

    public String getFile15FileConfirmerId() {
        return file15FileConfirmerId;
    }

    public void setFile15FileConfirmerId(String file15FileConfirmerId) {
        this.file15FileConfirmerId = file15FileConfirmerId;
    }

    public String getFile15FileConfirmerName() {
        return file15FileConfirmerName;
    }

    public void setFile15FileConfirmerName(String file15FileConfirmerName) {
        this.file15FileConfirmerName = file15FileConfirmerName;
    }

    public String getFile16FileConfirmerId() {
        return file16FileConfirmerId;
    }

    public void setFile16FileConfirmerId(String file16FileConfirmerId) {
        this.file16FileConfirmerId = file16FileConfirmerId;
    }

    public String getFile16FileConfirmerName() {
        return file16FileConfirmerName;
    }

    public void setFile16FileConfirmerName(String file16FileConfirmerName) {
        this.file16FileConfirmerName = file16FileConfirmerName;
    }

    public String getFile17FileConfirmerId() {
        return file17FileConfirmerId;
    }

    public void setFile17FileConfirmerId(String file17FileConfirmerId) {
        this.file17FileConfirmerId = file17FileConfirmerId;
    }

    public String getFile17FileConfirmerName() {
        return file17FileConfirmerName;
    }

    public void setFile17FileConfirmerName(String file17FileConfirmerName) {
        this.file17FileConfirmerName = file17FileConfirmerName;
    }

    public String getFile18FileConfirmerId() {
        return file18FileConfirmerId;
    }

    public void setFile18FileConfirmerId(String file18FileConfirmerId) {
        this.file18FileConfirmerId = file18FileConfirmerId;
    }

    public String getFile18FileConfirmerName() {
        return file18FileConfirmerName;
    }

    public void setFile18FileConfirmerName(String file18FileConfirmerName) {
        this.file18FileConfirmerName = file18FileConfirmerName;
    }

    public String getFile19FileConfirmerId() {
        return file19FileConfirmerId;
    }

    public void setFile19FileConfirmerId(String file19FileConfirmerId) {
        this.file19FileConfirmerId = file19FileConfirmerId;
    }

    public String getFile19FileConfirmerName() {
        return file19FileConfirmerName;
    }

    public void setFile19FileConfirmerName(String file19FileConfirmerName) {
        this.file19FileConfirmerName = file19FileConfirmerName;
    }

    public String getFile20FileConfirmerId() {
        return file20FileConfirmerId;
    }

    public void setFile20FileConfirmerId(String file20FileConfirmerId) {
        this.file20FileConfirmerId = file20FileConfirmerId;
    }

    public String getFile20FileConfirmerName() {
        return file20FileConfirmerName;
    }

    public void setFile20FileConfirmerName(String file20FileConfirmerName) {
        this.file20FileConfirmerName = file20FileConfirmerName;
    }

    public Integer getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(Integer inspectionType) {
        this.inspectionType = inspectionType;
    }

    public Integer getActionFileType() { return actionFileType; }

    public void setActionFileType(Integer actionFileType) { this.actionFileType = actionFileType; }

    public Integer getSaveFileType() { return saveFileType; }

    public void setSaveFileType(Integer saveFileType) { this.saveFileType = saveFileType; }

    public String getInspecFileUuid() { return inspecFileUuid; }

    public void setInspecFileUuid(String inspecFileUuid) { this.inspecFileUuid = inspecFileUuid; }
}
