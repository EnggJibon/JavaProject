package com.kmcj.karte.resources.asset.inventory.request;

import com.kmcj.karte.BasicResponse;
import java.util.Date;

public class TblInventoryRequestVo extends BasicResponse {

    private String id;
    
    private String inventoryName;////棚卸実施名称

    private String requestCompanyId;
    
    private String inventoryCompanyName;//棚卸実施会社名称

    private Date requestDate;
    
    private String requestDateStr;

    private Date dueDate;
    
    private String dueDateStr;

    private int assetCount;

    private int status;
    
    private String statusText;

    private Date sendResponseDate;
    
    private String sendResponseDateStr;

    private String inventoryId;

    private Date createDate;

    private Date updateDate;

    private String createUserUuid;

    private String updateUserUuid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestCompanyId() {
        return requestCompanyId;
    }

    public void setRequestCompanyId(String requestCompanyId) {
        this.requestCompanyId = requestCompanyId;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getAssetCount() {
        return assetCount;
    }

    public void setAssetCount(int assetCount) {
        this.assetCount = assetCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getSendResponseDate() {
        return sendResponseDate;
    }

    public void setSendResponseDate(Date sendResponseDate) {
        this.sendResponseDate = sendResponseDate;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public String getRequestDateStr() {
        return requestDateStr;
    }

    public void setRequestDateStr(String requestDateStr) {
        this.requestDateStr = requestDateStr;
    }

    public String getDueDateStr() {
        return dueDateStr;
    }

    public void setDueDateStr(String dueDateStr) {
        this.dueDateStr = dueDateStr;
    }

    public String getSendResponseDateStr() {
        return sendResponseDateStr;
    }

    public void setSendResponseDateStr(String sendResponseDateStr) {
        this.sendResponseDateStr = sendResponseDateStr;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    /**
     * @return the inventoryName
     */
    public String getInventoryName() {
        return inventoryName;
    }

    /**
     * @param inventoryName the inventoryName to set
     */
    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    /**
     * @return the inventoryCompanyName
     */
    public String getInventoryCompanyName() {
        return inventoryCompanyName;
    }

    /**
     * @param inventoryCompanyName the inventoryCompanyName to set
     */
    public void setInventoryCompanyName(String inventoryCompanyName) {
        this.inventoryCompanyName = inventoryCompanyName;
    }
    
}
