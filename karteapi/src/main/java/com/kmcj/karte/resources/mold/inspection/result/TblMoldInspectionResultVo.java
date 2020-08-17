/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inspection.result;

import com.kmcj.karte.BasicResponse;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMoldInspectionResultVo extends BasicResponse {
    private String maintenanceDetailId;
    private int seq;
    private String resultType;
    private String inspectionItemId;
    private String inspectionItemName;
    private String id;
    private String inspectionResult;
    private String inspectionResultText;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    private List<TblMoldInspectionResultVo> moldInspectionResultVos;
    private TblMoldInspectionResult tblMoldInspectionResult;

    public TblMoldInspectionResultVo() {
    }

    public String getMaintenanceDetailId() {
        return maintenanceDetailId;
    }

    public int getSeq() {
        return seq;
    }

    public String getInspectionItemId() {
        return inspectionItemId;
    }

    public String getId() {
        return id;
    }

    public String getInspectionResult() {
        return inspectionResult;
    }

    public String getInspectionResultText() {
        return inspectionResultText;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public List<TblMoldInspectionResultVo> getMoldInspectionResultVos() {
        return moldInspectionResultVos;
    }

    public void setMaintenanceDetailId(String maintenanceDetailId) {
        this.maintenanceDetailId = maintenanceDetailId;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setInspectionItemId(String inspectionItemId) {
        this.inspectionItemId = inspectionItemId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInspectionResult(String inspectionResult) {
        this.inspectionResult = inspectionResult;
    }

    public void setInspectionResultText(String inspectionResultText) {
        this.inspectionResultText = inspectionResultText;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setMoldInspectionResultVos(List<TblMoldInspectionResultVo> moldInspectionResultVos) {
        this.moldInspectionResultVos = moldInspectionResultVos;
    }

    public String getInspectionItemName() {
        return inspectionItemName;
    }

    public void setInspectionItemName(String inspectionItemName) {
        this.inspectionItemName = inspectionItemName;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public TblMoldInspectionResult getTblMoldInspectionResult() {
        return tblMoldInspectionResult;
    }

    public void setTblMoldInspectionResult(TblMoldInspectionResult tblMoldInspectionResult) {
        this.tblMoldInspectionResult = tblMoldInspectionResult;
    }
}
