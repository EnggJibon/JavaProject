/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.inventory;

import java.util.List;
import java.util.Map;

/**
 *
 * @author zds
 */
public class InventoryInfoCreateVoList {
    
    private String inventoryId;

    private List<InventoryInfoCreateVo> inventoryInfoCreateVoList;

    private Map<String, List<InventoryInfoCreateVo>> assetDisposalMap;

    /**
     * @return the inventoryInfoCreateVoList
     */
    public List<InventoryInfoCreateVo> getInventoryInfoCreateVoList() {
        return inventoryInfoCreateVoList;
    }

    /**
     * @param inventoryInfoCreateVoList the inventoryInfoCreateVoList to set
     */
    public void setInventoryInfoCreateVoList(List<InventoryInfoCreateVo> inventoryInfoCreateVoList) {
        this.inventoryInfoCreateVoList = inventoryInfoCreateVoList;
    }

    /**
     * @return the assetDisposalMap
     */
    public Map<String, List<InventoryInfoCreateVo>> getAssetDisposalMap() {
        return assetDisposalMap;
    }

    /**
     * @param assetDisposalMap the assetDisposalMap to set
     */
    public void setAssetDisposalMap(Map<String, List<InventoryInfoCreateVo>> assetDisposalMap) {
        this.assetDisposalMap = assetDisposalMap;
    }

    /**
     * @return the inventoryId
     */
    public String getInventoryId() {
        return inventoryId;
    }

    /**
     * @param inventoryId the inventoryId to set
     */
    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

}
