package com.kmcj.karte.resources.machine.remodeling.inspection;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.machine.inspection.item.MstMachineInspectionItem;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineRemodelingInspectionResultVo extends BasicResponse {

    private TblMachineRemodelingInspectionResultPK tblMachineRemodelingInspectionResultPK;

    private String id;

    private String inspectionResult;

    private String inspectionResultText;

    private Date createDate;
    private String createDateStr;

    private Date updateDate;
    private String updateDateStr;

    private String createUserUuid;

    private String updateUserUuid;

    private MstMachineInspectionItem mstMachineInspectionItem;
    private String mstMachineInspectionItemId;
    private String mstMachineinspectionItemName;
    
    private String resultType;
    
    private String remodelingDetailId;
    
    private int seq;
    
    private List<TblMachineRemodelingInspectionResultVo> machineRemodelingInspectionResultVo;
    private TblMachineRemodelingInspectionResult tblMachineRemodelingInspectionResult;

    public TblMachineRemodelingInspectionResultVo() {
    }

    /**
     * @return the tblMachineRemodelingInspectionResultPK
     */
    public TblMachineRemodelingInspectionResultPK getTblMachineRemodelingInspectionResultPK() {
        return tblMachineRemodelingInspectionResultPK;
    }

    /**
     * @param tblMachineRemodelingInspectionResultPK the tblMachineRemodelingInspectionResultPK to set
     */
    public void setTblMachineRemodelingInspectionResultPK(TblMachineRemodelingInspectionResultPK tblMachineRemodelingInspectionResultPK) {
        this.tblMachineRemodelingInspectionResultPK = tblMachineRemodelingInspectionResultPK;
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
     * @return the createDateStr
     */
    public String getCreateDateStr() {
        return createDateStr;
    }

    /**
     * @param createDateStr the createDateStr to set
     */
    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
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
     * @return the updateDateStr
     */
    public String getUpdateDateStr() {
        return updateDateStr;
    }

    /**
     * @param updateDateStr the updateDateStr to set
     */
    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
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
     * @return the mstMachineInspectionItem
     */
    public MstMachineInspectionItem getMstMachineInspectionItem() {
        return mstMachineInspectionItem;
    }

    /**
     * @param mstMachineInspectionItem the mstMachineInspectionItem to set
     */
    public void setMstMachineInspectionItem(MstMachineInspectionItem mstMachineInspectionItem) {
        this.mstMachineInspectionItem = mstMachineInspectionItem;
    }

    /**
     * @return the mstMachineInspectionItemId
     */
    public String getMstMachineInspectionItemId() {
        return mstMachineInspectionItemId;
    }

    /**
     * @param mstMachineInspectionItemId the mstMachineInspectionItemId to set
     */
    public void setMstMachineInspectionItemId(String mstMachineInspectionItemId) {
        this.mstMachineInspectionItemId = mstMachineInspectionItemId;
    }

    /**
     * @return the mstMachineinspectionItemName
     */
    public String getMstMachineinspectionItemName() {
        return mstMachineinspectionItemName;
    }

    /**
     * @param mstMachineinspectionItemName the mstMachineinspectionItemName to set
     */
    public void setMstMachineinspectionItemName(String mstMachineinspectionItemName) {
        this.mstMachineinspectionItemName = mstMachineinspectionItemName;
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
     * @return the machineRemodelingInspectionResultVo
     */
    public List<TblMachineRemodelingInspectionResultVo> getMachineRemodelingInspectionResultVo() {
        return machineRemodelingInspectionResultVo;
    }

    /**
     * @param machineRemodelingInspectionResultVo the machineRemodelingInspectionResultVo to set
     */
    public void setMachineRemodelingInspectionResultVo(List<TblMachineRemodelingInspectionResultVo> machineRemodelingInspectionResultVo) {
        this.machineRemodelingInspectionResultVo = machineRemodelingInspectionResultVo;
    }

    /**
     * @return the tblMachineRemodelingInspectionResult
     */
    public TblMachineRemodelingInspectionResult getTblMachineRemodelingInspectionResult() {
        return tblMachineRemodelingInspectionResult;
    }

    /**
     * @param tblMachineRemodelingInspectionResult the tblMachineRemodelingInspectionResult to set
     */
    public void setTblMachineRemodelingInspectionResult(TblMachineRemodelingInspectionResult tblMachineRemodelingInspectionResult) {
        this.tblMachineRemodelingInspectionResult = tblMachineRemodelingInspectionResult;
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

    

}
