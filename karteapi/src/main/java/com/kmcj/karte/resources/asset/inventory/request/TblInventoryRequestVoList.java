/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.inventory.request;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblInventoryRequestVoList extends BasicResponse {

    private List<TblInventoryRequestVo> TblInventoryRequestVos;

    public List<TblInventoryRequestVo> getTblInventoryRequestVos() {
        return TblInventoryRequestVos;
    }

    public void setTblInventoryRequestVos(List<TblInventoryRequestVo> tblInventoryRequestVos) {
        TblInventoryRequestVos = tblInventoryRequestVos;
    }

}
