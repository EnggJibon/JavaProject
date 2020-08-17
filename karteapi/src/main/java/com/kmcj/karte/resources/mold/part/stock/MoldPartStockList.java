/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.stock;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class MoldPartStockList extends BasicResponse {
    
    private int totalCount;
    private List<MoldPartStock> moldPartStocks;

    /**
     * @return the moldPartStocks
     */
    public List<MoldPartStock> getMoldPartStocks() {
        return moldPartStocks;
    }

    /**
     * @param moldPartStocks the moldPartStocks to set
     */
    public void setMoldPartStocks(List<MoldPartStock> moldPartStocks) {
        this.moldPartStocks = moldPartStocks;
    }

    /**
     * @return the totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * @param totalCount the totalCount to set
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    
}
