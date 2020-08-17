/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material.lot;

import com.kmcj.karte.BasicResponse;
import java.math.BigDecimal;

/**
 *
 * @author admin
 */
public class TblMaterialLotVo extends BasicResponse {

    private String uuid;

    private String lotNo;

    private BigDecimal lotQuantity;

    private BigDecimal stockQuantity;

    private String createDate;

    private String updateDate;

    private String createUserUuid;

    private String updateUserUuid;

    private String materialId;
            
    private String materialCode;
    
    private String materialName;

    private String lotIssueDate;

    private String lotIssueDateCsv;
    
    private String operationFlag; // 1:delete,3:update,4:add
    
    private int status;

    private String statusText;

    private String remarks01;

    private String remarks02;

    private String remarks03;

    private String remarks04;

    private String remarks05;

    private String materialStockId;

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
     * @return the lotQuantity
     */
    public BigDecimal getLotQuantity() {
        return lotQuantity;
    }

    /**
     * @param lotQuantity the lotQuantity to set
     */
    public void setLotQuantity(BigDecimal lotQuantity) {
        this.lotQuantity = lotQuantity;
    }

    /**
     * @return the stockQuantity
     */
    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    /**
     * @param stockQuantity the stockQuantity to set
     */
    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
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
     * @return the updateDate
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(String updateDate) {
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
     * @return the materialId
     */
    public String getMaterialId() {
        return materialId;
    }

    /**
     * @param materialId the materialId to set
     */
    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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
     * @return the materialCode
     */
    public String getMaterialCode() {
        return materialCode;
    }

    /**
     * @param materialCode the materialCode to set
     */
    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    /**
     * @return the materialName
     */
    public String getMaterialName() {
        return materialName;
    }

    /**
     * @param materialName the materialName to set
     */
    public void setMaterialName(String materialName) {
        this.materialName = materialName;
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
     * @return the materialStockId
     */
    public String getMaterialStockId() {
        return materialStockId;
    }

    /**
     * @param materialStockId the materialStockId to set
     */
    public void setMaterialStockId(String materialStockId) {
        this.materialStockId = materialStockId;
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

}
