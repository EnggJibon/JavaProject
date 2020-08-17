package com.kmcj.karte.resources.production.plan;

import com.kmcj.karte.BasicResponse;
import java.util.List;

public class TblProductionPlanVo extends BasicResponse {

    private String id;
    private String quantity;
    private String procedureDueDate;
    private String uncompletedCount;
    private String completedCount;
    private String componentId;
    private String procedureId;
    private String directionId;
    private String componentCode;
    private String componentName;
    private String procedureCode;
    private String procedureName;
    private String directionCode;
    private String directionName;
    private String createDate;
    private String updateDate;
    private String createUserUuid;
    private String updateUserUuid;

    private List<TblProductionPlanVo> tblProductionPlanVos;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUncompletedCount() {
        return uncompletedCount;
    }

    public void setUncompletedCount(String uncompletedCount) {
        this.uncompletedCount = uncompletedCount;
    }

    public String getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(String completedCount) {
        this.completedCount = completedCount;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public TblProductionPlanVo() {
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
     * @return the componentId
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * @param componentId the componentId to set
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the procedureId
     */
    public String getProcedureId() {
        return procedureId;
    }

    /**
     * @param procedureId the procedureId to set
     */
    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    /**
     * @return the directionId
     */
    public String getDirectionId() {
        return directionId;
    }

    /**
     * @param directionId the directionId to set
     */
    public void setDirectionId(String directionId) {
        this.directionId = directionId;
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
     * @return the procedureName
     */
    public String getProcedureName() {
        return procedureName;
    }

    /**
     * @param procedureName the procedureName to set
     */
    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    /**
     * @return the directionName
     */
    public String getDirectionName() {
        return directionName;
    }

    /**
     * @param directionName the directionName to set
     */
    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    /**
     * @return the tblProductionPlanVos
     */
    public List<TblProductionPlanVo> getTblProductionPlanVos() {
        return tblProductionPlanVos;
    }

    /**
     * @param tblProductionPlanVos the tblProductionPlanVos to set
     */
    public void setTblProductionPlanVos(List<TblProductionPlanVo> tblProductionPlanVos) {
        this.tblProductionPlanVos = tblProductionPlanVos;
    }

    /**
     * @return the procedureDueDate
     */
    public String getProcedureDueDate() {
        return procedureDueDate;
    }

    /**
     * @param procedureDueDate the procedureDueDate to set
     */
    public void setProcedureDueDate(String procedureDueDate) {
        this.procedureDueDate = procedureDueDate;
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
     * @return the procedureCode
     */
    public String getProcedureCode() {
        return procedureCode;
    }

    /**
     * @param procedureCode the procedureCode to set
     */
    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    /**
     * @return the directionCode
     */
    public String getDirectionCode() {
        return directionCode;
    }

    /**
     * @param directionCode the directionCode to set
     */
    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
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

}
