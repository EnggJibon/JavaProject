/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.request.detail.id;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblInventoryRequestDetailIdVoList extends BasicResponse {

    private List<TblInventoryRequestDetailIdVo> tblInventoryRequestDetailIdVo;

    /**
     * @return the tblInventoryRequestDetailIdVo
     */
    public List<TblInventoryRequestDetailIdVo> getTblInventoryRequestDetailIdVo() {
        return tblInventoryRequestDetailIdVo;
    }

    /**
     * @param tblInventoryRequestDetailIdVo the tblInventoryRequestDetailIdVo to
     * set
     */
    public void setTblInventoryRequestDetailIdVo(List<TblInventoryRequestDetailIdVo> tblInventoryRequestDetailIdVo) {
        this.tblInventoryRequestDetailIdVo = tblInventoryRequestDetailIdVo;
    }

}
