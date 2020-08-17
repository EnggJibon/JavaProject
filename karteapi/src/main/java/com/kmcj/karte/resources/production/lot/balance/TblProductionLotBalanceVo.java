/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.lot.balance;
import com.kmcj.karte.BasicResponse;
import java.util.Date;

/**
 * 生産実績ロット残高テーブル JSON送受信用クラス
 * @author t.ariki
 */
public class TblProductionLotBalanceVo extends BasicResponse {
    /**
     * テーブル定義と同一内容
     */
    private String id;
    private String lotNumber;
    private Integer processNumber;
    private String productionDetailId;
    private String componentId;
    private Integer firstCompleteCount;
    private Integer nextCompleteCount;
    private Integer balance;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    
    /**
     * 独自定義内容
     */
    // 一括反映時制御フラグ
    private boolean deleted = false;    // 削除対象制御
    private boolean modified = false;   // 更新対象制御
    private boolean added = false;      // 登録対象制御

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public Integer getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(Integer processNumber) {
        this.processNumber = processNumber;
    }

    public String getProductionDetailId() {
        return productionDetailId;
    }

    public void setProductionDetailId(String productionDetailId) {
        this.productionDetailId = productionDetailId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public Integer getFirstCompleteCount() {
        return firstCompleteCount;
    }

    public void setFirstCompleteCount(Integer firstCompleteCount) {
        this.firstCompleteCount = firstCompleteCount;
    }

    public Integer getNextCompleteCount() {
        return nextCompleteCount;
    }

    public void setNextCompleteCount(Integer nextCompleteCount) {
        this.nextCompleteCount = nextCompleteCount;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }
}
