/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.lot.relation;

import com.kmcj.karte.BasicResponse;

public class TblComponentLotRelationVo extends BasicResponse {

    private String uuid;

    private String productionDetailId;//生産実績明細ID
    
    private String macReportId;//機械日報ID

    private String componentId; //部品ID
    
    private String componentCode; //部品コード
    
    private String componentName; //部品名称
    
    private String procedureCode;
    
    private String componentLotId; //部品マスタ部品ID
    
    private String componentLotNo; //部品ロット番号

    private String subComponentId;//構成部品ID
    
    private String subComponentCode;//構成部品Code
    
    private String subComponentName;//構成部品Name
    
    private String subProcedureCode;

    private String subComponentLotId; //構成部品ロットID
    
    private String subComponentLotNo; //構成部品ロット番号
    
    private String createDate; //登録日
    
    private String operationFlag; // 1:delete,3:update,4:add

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the productionDetailId
     */
    public String getProductionDetailId() {
        return productionDetailId;
    }

    /**
     * @param productionDetailId the productionDetailId to set
     */
    public void setProductionDetailId(String productionDetailId) {
        this.productionDetailId = productionDetailId;
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
     * @return the componentLotId
     */
    public String getComponentLotId() {
        return componentLotId;
    }

    /**
     * @param componentLotId the componentLotId to set
     */
    public void setComponentLotId(String componentLotId) {
        this.componentLotId = componentLotId;
    }

    /**
     * @return the subComponentId
     */
    public String getSubComponentId() {
        return subComponentId;
    }

    /**
     * @param subComponentId the subComponentId to set
     */
    public void setSubComponentId(String subComponentId) {
        this.subComponentId = subComponentId;
    }

    /**
     * @return the subComponentLotId
     */
    public String getSubComponentLotId() {
        return subComponentLotId;
    }

    /**
     * @param subComponentLotId the subComponentLotId to set
     */
    public void setSubComponentLotId(String subComponentLotId) {
        this.subComponentLotId = subComponentLotId;
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
     * @return the componentLotNo
     */
    public String getComponentLotNo() {
        return componentLotNo;
    }

    /**
     * @param componentLotNo the componentLotNo to set
     */
    public void setComponentLotNo(String componentLotNo) {
        this.componentLotNo = componentLotNo;
    }

    /**
     * @return the subComponentCode
     */
    public String getSubComponentCode() {
        return subComponentCode;
    }

    /**
     * @param subComponentCode the subComponentCode to set
     */
    public void setSubComponentCode(String subComponentCode) {
        this.subComponentCode = subComponentCode;
    }

    /**
     * @return the subComponentName
     */
    public String getSubComponentName() {
        return subComponentName;
    }

    /**
     * @param subComponentName the subComponentName to set
     */
    public void setSubComponentName(String subComponentName) {
        this.subComponentName = subComponentName;
    }

    /**
     * @return the subComponentLotNo
     */
    public String getSubComponentLotNo() {
        return subComponentLotNo;
    }

    /**
     * @param subComponentLotNo the subComponentLotNo to set
     */
    public void setSubComponentLotNo(String subComponentLotNo) {
        this.subComponentLotNo = subComponentLotNo;
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
     * @return the subProcedureCode
     */
    public String getSubProcedureCode() {
        return subProcedureCode;
    }

    /**
     * @param subProcedureCode the subProcedureCode to set
     */
    public void setSubProcedureCode(String subProcedureCode) {
        this.subProcedureCode = subProcedureCode;
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
     * @return the createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the macReportId
     */
    public String getMacReportId() {
        return macReportId;
    }

    /**
     * @param macReportId the macReportId to set
     */
    public void setMacReportId(String macReportId) {
        this.macReportId = macReportId;
    }

}

