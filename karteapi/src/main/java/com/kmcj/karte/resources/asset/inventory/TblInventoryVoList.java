/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblInventoryVoList extends BasicResponse {

    private List<TblInventoryVo> tblInventoryVos;

    /**
     * @return the tblInventoryVos
     */
    public List<TblInventoryVo> getTblInventoryVos() {
        return tblInventoryVos;
    }

    /**
     * @param tblInventoryVos the tblInventoryVos to set
     */
    public void setTblInventoryVos(List<TblInventoryVo> tblInventoryVos) {
        this.tblInventoryVos = tblInventoryVos;
    }

}
