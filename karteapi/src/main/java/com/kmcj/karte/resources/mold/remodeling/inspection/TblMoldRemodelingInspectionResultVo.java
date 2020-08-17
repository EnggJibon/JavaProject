package com.kmcj.karte.resources.mold.remodeling.inspection;

import com.kmcj.karte.BasicResponse;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMoldRemodelingInspectionResultVo extends BasicResponse {

    private TblMoldRemodelingInspectionResultPK tblMoldRemodelingInspectionResultPK;
    private String remodelingDetailId;
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
    private List<TblMoldRemodelingInspectionResultVo> moldRemodelingInspectionResultVos;
    private TblMoldRemodelingInspectionResult tblMoldRemodelingInspectionResult;

    public TblMoldRemodelingInspectionResultVo() {
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
     * @return the resultType
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * @param resultType the resultType to set
     */
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    /**
     * @return the inspectionItemId
     */
    public String getInspectionItemId() {
        return inspectionItemId;
    }

    /**
     * @param inspectionItemId the inspectionItemId to set
     */
    public void setInspectionItemId(String inspectionItemId) {
        this.inspectionItemId = inspectionItemId;
    }

    /**
     * @return the inspectionItemName
     */
    public String getInspectionItemName() {
        return inspectionItemName;
    }

    /**
     * @param inspectionItemName the inspectionItemName to set
     */
    public void setInspectionItemName(String inspectionItemName) {
        this.inspectionItemName = inspectionItemName;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the inspectionResult
     */
    public String getInspectionResult() {
        return inspectionResult;
    }

    /**
     * @param inspectionResult the inspectionResult to set
     */
    public void setInspectionResult(String inspectionResult) {
        this.inspectionResult = inspectionResult;
    }

    /**
     * @return the inspectionResultText
     */
    public String getInspectionResultText() {
        return inspectionResultText;
    }

    /**
     * @param inspectionResultText the inspectionResultText to set
     */
    public void setInspectionResultText(String inspectionResultText) {
        this.inspectionResultText = inspectionResultText;
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
     * @return the createUserUuid
     */
    public String getCreateUserUuid() {
        return createUserUuid;
    }

    /**
     * @param createUserUuid the createUserUuid to set
     */
    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
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
     * @return the tblMoldRemodelingInspectionResult
     */
    public TblMoldRemodelingInspectionResult getTblMoldRemodelingInspectionResult() {
        return tblMoldRemodelingInspectionResult;
    }

    /**
     * @param tblMoldRemodelingInspectionResult the tblMoldRemodelingInspectionResult to set
     */
    public void setTblMoldRemodelingInspectionResult(TblMoldRemodelingInspectionResult tblMoldRemodelingInspectionResult) {
        this.tblMoldRemodelingInspectionResult = tblMoldRemodelingInspectionResult;
    }

    /**
     * @return the tblMoldRemodelingInspectionResultPK
     */
    public TblMoldRemodelingInspectionResultPK getTblMoldRemodelingInspectionResultPK() {
        return tblMoldRemodelingInspectionResultPK;
    }

    /**
     * @param tblMoldRemodelingInspectionResultPK the tblMoldRemodelingInspectionResultPK to set
     */
    public void setTblMoldRemodelingInspectionResultPK(TblMoldRemodelingInspectionResultPK tblMoldRemodelingInspectionResultPK) {
        this.tblMoldRemodelingInspectionResultPK = tblMoldRemodelingInspectionResultPK;
    }

    /**
     * @return the remodelingDetailId
     */
    public String getRemodelingDetailId() {
        return remodelingDetailId;
    }

    /**
     * @param remodelingDetailId the remodelingDetailId to set
     */
    public void setRemodelingDetailId(String remodelingDetailId) {
        this.remodelingDetailId = remodelingDetailId;
    }

    /**
     * @return the moldRemodelingInspectionResultVo
     */
    public List<TblMoldRemodelingInspectionResultVo> getMoldRemodelingInspectionResultVos() {
        return moldRemodelingInspectionResultVos;
    }

    /**
     * @param moldRemodelingInspectionResultVos the moldRemodelingInspectionResultVo to set
     */
    public void setMoldRemodelingInspectionResultVos(List<TblMoldRemodelingInspectionResultVo> moldRemodelingInspectionResultVos) {
        this.moldRemodelingInspectionResultVos = moldRemodelingInspectionResultVos;
    }

    
    
}
