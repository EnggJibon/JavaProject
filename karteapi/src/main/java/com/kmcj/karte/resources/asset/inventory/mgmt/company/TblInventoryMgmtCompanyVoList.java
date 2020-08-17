/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.mgmt.company;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.asset.inventory.TblInventory;
import com.kmcj.karte.resources.asset.inventory.detail.TblInventoryDetail;
import com.kmcj.karte.resources.asset.inventory.request.detail.TblInventoryRequestDetail;
import com.kmcj.karte.resources.asset.inventory.send.history.TblInventorySendHistory;
import com.kmcj.karte.resources.asset.inventory.send.history.TblInventorySendHistoryAttachment;
import java.util.List;
import java.util.Map;

/**
 *
 * @author admin
 */
public class TblInventoryMgmtCompanyVoList extends BasicResponse {
    
    /** 棚卸管理先一覧用情報リスト */
    private List<TblInventoryMgmtCompany> tblInventoryMgmtCompanys;
    
    /** 棚卸実施送信履歴添付情報リスト */
    private List<TblInventorySendHistoryAttachment> tblInventorySendHistoryAttachments;
    
    /** 棚卸実施送信履歴最大連番情報 */
    private TblInventorySendHistory maxSeqInventorySendHistory;
    
    // ------------------------------サプライヤーへ用----------------------------
    /** 棚卸実施情報 */
    private TblInventory tblInventory;
    
    /** 棚卸実施管理先情報 */
    private TblInventoryMgmtCompany tblInventoryMgmtCompany;
    
    /** 棚卸実施明細情報リスト */
    private List<TblInventoryDetail> tblInventoryDetails;
    // ------------------------------サプライヤーへ用----------------------------
    
    // ------------------------------batch取得用--------------------------------
    /** 棚卸依頼明細情報リスト */
    private List<TblInventoryRequestDetail> externalInventoryRequestDetailList;
    // ------------------------------batch取得用--------------------------------

    /**
     * @return the tblInventoryMgmtCompanys
     */
    public List<TblInventoryMgmtCompany> getTblInventoryMgmtCompanys() {
        return tblInventoryMgmtCompanys;
    }

    /**
     * @param tblInventoryMgmtCompanys the tblInventoryMgmtCompanys to set
     */
    public void setTblInventoryMgmtCompanys(List<TblInventoryMgmtCompany> tblInventoryMgmtCompanys) {
        this.tblInventoryMgmtCompanys = tblInventoryMgmtCompanys;
    }

    /**
     * @return the tblInventorySendHistoryAttachments
     */
    public List<TblInventorySendHistoryAttachment> getTblInventorySendHistoryAttachments() {
        return tblInventorySendHistoryAttachments;
    }

    /**
     * @param tblInventorySendHistoryAttachments the tblInventorySendHistoryAttachments to set
     */
    public void setTblInventorySendHistoryAttachments(List<TblInventorySendHistoryAttachment> tblInventorySendHistoryAttachments) {
        this.tblInventorySendHistoryAttachments = tblInventorySendHistoryAttachments;
    }

    /**
     * @return the maxSeqInventorySendHistory
     */
    public TblInventorySendHistory getMaxSeqInventorySendHistory() {
        return maxSeqInventorySendHistory;
    }

    /**
     * @param maxSeqInventorySendHistory the maxSeqInventorySendHistory to set
     */
    public void setMaxSeqInventorySendHistory(TblInventorySendHistory maxSeqInventorySendHistory) {
        this.maxSeqInventorySendHistory = maxSeqInventorySendHistory;
    }

    /**
     * @return the tblInventory
     */
    public TblInventory getTblInventory() {
        return tblInventory;
    }

    /**
     * @param tblInventory the tblInventory to set
     */
    public void setTblInventory(TblInventory tblInventory) {
        this.tblInventory = tblInventory;
    }

    /**
     * @return the tblInventoryMgmtCompany
     */
    public TblInventoryMgmtCompany getTblInventoryMgmtCompany() {
        return tblInventoryMgmtCompany;
    }

    /**
     * @param tblInventoryMgmtCompany the tblInventoryMgmtCompany to set
     */
    public void setTblInventoryMgmtCompany(TblInventoryMgmtCompany tblInventoryMgmtCompany) {
        this.tblInventoryMgmtCompany = tblInventoryMgmtCompany;
    }

    /**
     * @return the tblInventoryDetails
     */
    public List<TblInventoryDetail> getTblInventoryDetails() {
        return tblInventoryDetails;
    }

    /**
     * @param tblInventoryDetails the tblInventoryDetails to set
     */
    public void setTblInventoryDetails(List<TblInventoryDetail> tblInventoryDetails) {
        this.tblInventoryDetails = tblInventoryDetails;
    }

    /**
     * @return the externalInventoryRequestDetailList
     */
    public List<TblInventoryRequestDetail> getExternalInventoryRequestDetailList() {
        return externalInventoryRequestDetailList;
    }

    /**
     * @param externalInventoryRequestDetailList the externalInventoryRequestDetailList to set
     */
    public void setExternalInventoryRequestDetailList(List<TblInventoryRequestDetail> externalInventoryRequestDetailList) {
        this.externalInventoryRequestDetailList = externalInventoryRequestDetailList;
    }
    
}
