/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.stock;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblProductionStockVoList extends BasicResponse {

    private List<TblProductionStockVo> tblProductionStockVos;

    /**
     * @return the tblProductionStockVos
     */
    public List<TblProductionStockVo> getTblProductionStockVos() {
        return tblProductionStockVos;
    }

    /**
     * @param tblProductionStockVos the tblProductionStockVos to set
     */
    public void setTblProductionStockVos(List<TblProductionStockVo> tblProductionStockVos) {
        this.tblProductionStockVos = tblProductionStockVos;
    }

}
