/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.stock;

import com.kmcj.karte.BasicResponse;

/**
 *
 * @author f.kitaoji
 */
public class MoldPartStockRes extends BasicResponse {
    private MoldPartStock moldPartStock;

    /**
     * @return the moldPartStock
     */
    public MoldPartStock getMoldPartStock() {
        return moldPartStock;
    }

    /**
     * @param moldPartStock the moldPartStock to set
     */
    public void setMoldPartStock(MoldPartStock moldPartStock) {
        this.moldPartStock = moldPartStock;
    }
}
