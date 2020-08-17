/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.request.detail;

import com.kmcj.karte.BasicResponse;
import java.util.List;
import java.util.Set;

/**
 *
 * @author admin
 */
public class TblInventoryRequestDetailVoList extends BasicResponse {

    private List<TblInventoryRequestDetailVo> tblInventoryRequestDetailVos;
    
    private Set<String> requestUuids;

    /**
     * @return the tblInventoryRequestDetailVos
     */
    public List<TblInventoryRequestDetailVo> getTblInventoryRequestDetailVos() {
        return tblInventoryRequestDetailVos;
    }

    /**
     * @param tblInventoryRequestDetailVos the tblInventoryRequestDetailVos to
     * set
     */
    public void setTblInventoryRequestDetailVos(List<TblInventoryRequestDetailVo> tblInventoryRequestDetailVos) {
        this.tblInventoryRequestDetailVos = tblInventoryRequestDetailVos;
    }

    /**
     * @return the requestUuids
     */
    public Set<String> getRequestUuids() {
        return requestUuids;
    }

    /**
     * @param requestUuids the requestUuids to set
     */
    public void setRequestUuids(Set<String> requestUuids) {
        this.requestUuids = requestUuids;
    }

}
