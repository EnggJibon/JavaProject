/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspectorManhours.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author h.ishihara
 */
public class InspectorManhour {
    //private String inspectorManhourId;
    private String inspectorUuid;
    private String componentId;
    private String procedureId;
    private Date inspectionDate;
    private BigDecimal downTime;
    private BigDecimal manhoursForRepairing;
    private BigDecimal resultManhours;
    private Integer outputManhours;
    private Double efficiency;
    private String commentText;
    
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    
//    private String componentName;
//    private String procedureName;

//    /**
//     * @return the insepectorManhourId
//     */
//    public String getInsepectorManhourId() {
//        return inspectorManhourId;
//    }
//
//    /**
//     * @param insepectorManhourId the insepectorManhourId to set
//     */
//    public void setInsepectorManhourId(String insepectorManhourId) {
//        this.inspectorManhourId = insepectorManhourId;
//    }

    /**
     * @return the inspectorUuid
     */
    public String getInspectorUuid() {
        return inspectorUuid;
    }

    /**
     * @param inspectorUuid the inspectorUuid to set
     */
    public void setInspectorUuid(String inspectorUuid) {
        this.inspectorUuid = inspectorUuid;
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
     * @return the inspectionDate
     */
    public Date getInspectionDate() {
        return inspectionDate;
    }

    /**
     * @param inspectionDate the inspectionDate to set
     */
    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    /**
     * @return the downTime
     */
    public BigDecimal getDownTime() {
        return downTime;
    }

    /**
     * @param downTime the downTime to set
     */
    public void setDownTime(BigDecimal downTime) {
        this.downTime = downTime;
    }

    /**
     * @return the manhoursForRepairing
     */
    public BigDecimal getManhoursForRepairing() {
        return manhoursForRepairing;
    }

    /**
     * @param manhoursForRepairing the manhoursForRepairing to set
     */
    public void setManhoursForRepairing(BigDecimal manhoursForRepairing) {
        this.manhoursForRepairing = manhoursForRepairing;
    }

    /**
     * @return the resultManhours
     */
    public BigDecimal getResultManhours() {
        return resultManhours;
    }

    /**
     * @param resultManhours the resultManhours to set
     */
    public void setResultManhours(BigDecimal resultManhours) {
        this.resultManhours = resultManhours;
    }

    /**
     * @return the outputManhours
     */
    public Integer getOutputManhours() {
        return outputManhours;
    }

    /**
     * @param outputManhours the output to set
     */
    public void setOutputManhours(Integer outputManhours) {
        this.outputManhours = outputManhours;
    }

    /**
     * @return the efficiency
     */
    public Double getEfficiency() {
        return efficiency;
    }

    /**
     * @param efficiency the efficiency to set
     */
    public void setEfficiency(Double efficiency) {
        this.efficiency = efficiency;
    }

    /**
     * @return the comment
     */
    public String getCommentText() {
        return commentText;
    }

    /**
     * @param comment the comment to set
     */
    public void setCommentText(String comment) {
        this.commentText = comment;
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
}
