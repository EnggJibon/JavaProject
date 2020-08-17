/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.lot;

import com.kmcj.karte.BasicResponse;

public class TblComponentLotVo extends BasicResponse {

    private String uuid;

    private String lotNo;

    private String lotIssueDate;
    
    private String lotIssueDateCsv;

    private String procedureCode;

    private int lotQty;

    private int stockQty;

    private int status;
    private String statusText;

    private String remarks01;

    private String remarks02;

    private String remarks03;

    private String remarks04;

    private String remarks05;

    private String componentId;
    
    private String componentCode;
    
    private String componentName;

    private String stockId;

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
     * @return the lotNo
     */
    public String getLotNo() {
        return lotNo;
    }

    /**
     * @param lotNo the lotNo to set
     */
    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    /**
     * @return the lotIssueDate
     */
    public String getLotIssueDate() {
        return lotIssueDate;
    }

    /**
     * @param lotIssueDate the lotIssueDate to set
     */
    public void setLotIssueDate(String lotIssueDate) {
        this.lotIssueDate = lotIssueDate;
    }

    /**
     * @return the lotQty
     */
    public int getLotQty() {
        return lotQty;
    }

    /**
     * @param lotQty the lotQty to set
     */
    public void setLotQty(int lotQty) {
        this.lotQty = lotQty;
    }

    /**
     * @return the stockQty
     */
    public int getStockQty() {
        return stockQty;
    }

    /**
     * @param stockQty the stockQty to set
     */
    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    /**
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the remarks01
     */
    public String getRemarks01() {
        return remarks01;
    }

    /**
     * @param remarks01 the remarks01 to set
     */
    public void setRemarks01(String remarks01) {
        this.remarks01 = remarks01;
    }

    /**
     * @return the remarks02
     */
    public String getRemarks02() {
        return remarks02;
    }

    /**
     * @param remarks02 the remarks02 to set
     */
    public void setRemarks02(String remarks02) {
        this.remarks02 = remarks02;
    }

    /**
     * @return the remarks03
     */
    public String getRemarks03() {
        return remarks03;
    }

    /**
     * @param remarks03 the remarks03 to set
     */
    public void setRemarks03(String remarks03) {
        this.remarks03 = remarks03;
    }

    /**
     * @return the remarks04
     */
    public String getRemarks04() {
        return remarks04;
    }

    /**
     * @param remarks04 the remarks04 to set
     */
    public void setRemarks04(String remarks04) {
        this.remarks04 = remarks04;
    }

    /**
     * @return the remarks05
     */
    public String getRemarks05() {
        return remarks05;
    }

    /**
     * @param remarks05 the remarks05 to set
     */
    public void setRemarks05(String remarks05) {
        this.remarks05 = remarks05;
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
     * @return the stockId
     */
    public String getStockId() {
        return stockId;
    }

    /**
     * @param stockId the stockId to set
     */
    public void setStockId(String stockId) {
        this.stockId = stockId;
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
     * @return the statusText
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * @param statusText the statusText to set
     */
    public void setStatusText(String statusText) {
        this.statusText = statusText;
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
     * @return the lotIssueDateCsv
     */
    public String getLotIssueDateCsv() {
        return lotIssueDateCsv;
    }

    /**
     * @param lotIssueDateCsv the lotIssueDateCsv to set
     */
    public void setLotIssueDateCsv(String lotIssueDateCsv) {
        this.lotIssueDateCsv = lotIssueDateCsv;
    }

}
