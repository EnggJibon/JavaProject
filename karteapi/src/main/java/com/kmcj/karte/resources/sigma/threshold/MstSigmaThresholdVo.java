package com.kmcj.karte.resources.sigma.threshold;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.machine.filedef.MstMachineFileDef;

/**
 *
 * @author admin
 */
public class MstSigmaThresholdVo extends BasicResponse {

    private String id;

    private String maxVal;

    private String minVal;

    private String createDate;

    private String updateDate;

    private String createUserUuid;

    private String updateUserUuid;

    private MstComponent mstComponent;

    private String componentId;
    private String componentCode;
    private String componentName;

    private MstMachineFileDef mstMachineFileDef;

    private String fileDefId;
    
    private String headerLabel;
    
    private String operationFlag;

    public MstSigmaThresholdVo() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMaxVal(String maxVal) {
        this.maxVal = maxVal;
    }

    public void setMinVal(String minVal) {
        this.minVal = minVal;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public void setMstMachineFileDef(MstMachineFileDef mstMachineFileDef) {
        this.mstMachineFileDef = mstMachineFileDef;
    }

    public void setFileDefId(String fileDefId) {
        this.fileDefId = fileDefId;
    }

    public String getId() {
        return id;
    }

    public String getMaxVal() {
        return maxVal;
    }

    public String getMinVal() {
        return minVal;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public MstComponent getMstComponent() {
        return mstComponent;
    }

    public String getComponentId() {
        return componentId;
    }

    public MstMachineFileDef getMstMachineFileDef() {
        return mstMachineFileDef;
    }

    public String getFileDefId() {
        return fileDefId;
    }


    /**
     * @return the headerLabel
     */
    public String getHeaderLabel() {
        return headerLabel;
    }

    /**
     * @param headerLabel the headerLabel to set
     */
    public void setHeaderLabel(String headerLabel) {
        this.headerLabel = headerLabel;
    }

    /**
     * @return the componentCode
     */
    public String getComponentCode() {
        return componentCode;
    }

    /**
     * @param componentCode the componentCode to set
     */
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    /**
     * @return the componentName
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * @param componentName the componentName to set
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
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

}
