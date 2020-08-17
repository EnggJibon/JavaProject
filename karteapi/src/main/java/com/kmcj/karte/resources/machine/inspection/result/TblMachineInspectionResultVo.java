package com.kmcj.karte.resources.machine.inspection.result;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.machine.inspection.item.MstMachineInspectionItem;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineInspectionResultVo extends BasicResponse {

    private TblMachineInspectionResultPK tblMachineInspectionResultPK;

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
    
    private String maintenanceDetailId;
    
    private int seq;
    
    private List<TblMachineInspectionResultVo> machineInspectionResultVos;
    private TblMachineInspectionResult tblMachineInspectionResult;

    public TblMachineInspectionResultVo() {
    }

    public TblMachineInspectionResultPK getTblMachineInspectionResultPK() {
        return tblMachineInspectionResultPK;
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

    public MstMachineInspectionItem getMstMachineInspectionItem() {
        return mstMachineInspectionItem;
    }

    public String getMstMachineInspectionItemId() {
        return mstMachineInspectionItemId;
    }

    public void setTblMachineInspectionResultPK(TblMachineInspectionResultPK tblMachineInspectionResultPK) {
        this.tblMachineInspectionResultPK = tblMachineInspectionResultPK;
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

    public void setMstMachineInspectionItem(MstMachineInspectionItem mstMachineInspectionItem) {
        this.mstMachineInspectionItem = mstMachineInspectionItem;
    }

    public void setMstMachineInspectionItemId(String mstMachineInspectionItemId) {
        this.mstMachineInspectionItemId = mstMachineInspectionItemId;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public String getUpdateDateStr() {
        return updateDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public void setUpdateDateStr(String updateDateStr) {
        this.updateDateStr = updateDateStr;
    }

    /**
     * @return the maintenanceDetailId
     */
    public String getMaintenanceDetailId() {
        return maintenanceDetailId;
    }

    /**
     * @param maintenanceDetailId the maintenanceDetailId to set
     */
    public void setMaintenanceDetailId(String maintenanceDetailId) {
        this.maintenanceDetailId = maintenanceDetailId;
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
     * @return the machineInspectionResultVos
     */
    public List<TblMachineInspectionResultVo> getMachineInspectionResultVos() {
        return machineInspectionResultVos;
    }

    /**
     * @param machineInspectionResultVos the machineInspectionResultVos to set
     */
    public void setMachineInspectionResultVos(List<TblMachineInspectionResultVo> machineInspectionResultVos) {
        this.machineInspectionResultVos = machineInspectionResultVos;
    }

    /**
     * @return the tblMachineInspectionResult
     */
    public TblMachineInspectionResult getTblMachineInspectionResult() {
        return tblMachineInspectionResult;
    }

    /**
     * @param tblMachineInspectionResult the tblMachineInspectionResult to set
     */
    public void setTblMachineInspectionResult(TblMachineInspectionResult tblMachineInspectionResult) {
        this.tblMachineInspectionResult = tblMachineInspectionResult;
    }

}
