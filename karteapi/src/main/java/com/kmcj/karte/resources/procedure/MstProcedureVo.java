package com.kmcj.karte.resources.procedure;

import com.kmcj.karte.BasicResponse;
import java.util.Date;
import java.util.List;

public class MstProcedureVo extends BasicResponse {

    private String id;
    private String componentId;
    private String procedureCode;
    private String procedureName;
    private String installationSiteId;
    private String seq;
    private String finalFlg;
    private Integer externalFlg;
    private String createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    
    private String componentCode;
    private String componentName;
    private String componentType;
    private String processesNotRegistered;
    private String operationFlag;
    
    private List<MstProcedureVo> mstProcedureVos;
    private MstProcedure mstProcedure;

    public MstProcedureVo() {
    }

    public MstProcedureVo(String id) {
        this.id = id;
    }
    
    public MstProcedureVo(String componentCode, String componentName, String componentType, String processesNotRegistered) {
        this.componentCode = componentCode;
        this.componentName = componentName;
        this.componentType = componentType;
        this.processesNotRegistered = processesNotRegistered;
    }

    public String getId() {
        return id;
    }

    public String getComponentId() {
        return componentId;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public String getSeq() {
        return seq;
    }

    public String getFinalFlg() {
        return finalFlg;
    }

    public Integer getExternalFlg() {
        return externalFlg;
    }

    public String getCreateDate() {
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

    public void setId(String id) {
        this.id = id;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public void setFinalFlg(String finalFlg) {
        this.finalFlg = finalFlg;
    }

    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
    }

    public void setCreateDate(String createDate) {
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

    public String getComponentCode() {
        return componentCode;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getComponentType() {
        return componentType;
    }

    public String getProcessesNotRegistered() {
        return processesNotRegistered;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public void setProcessesNotRegistered(String processesNotRegistered) {
        this.processesNotRegistered = processesNotRegistered;
    }

    public List<MstProcedureVo> getMstProcedureVos() {
        return mstProcedureVos;
    }

    public void setMstProcedureVos(List<MstProcedureVo> mstProcedureVos) {
        this.mstProcedureVos = mstProcedureVos;
    }

    public String getOperationFlag() {
        return operationFlag;
    }

    public void setOperationFlag(String operationFlag) {
        this.operationFlag = operationFlag;
    }

    public MstProcedure getMstProcedure() {
        return mstProcedure;
    }

    public void setMstProcedure(MstProcedure mstProcedure) {
        this.mstProcedure = mstProcedure;
    }

    /**
     * @return the installationSiteId
     */
    public String getInstallationSiteId() {
        return installationSiteId;
    }

    /**
     * @param installationSiteId the installationSiteId to set
     */
    public void setInstallationSiteId(String installationSiteId) {
        this.installationSiteId = installationSiteId;
    }
    
    
}
