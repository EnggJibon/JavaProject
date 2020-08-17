/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.stock.inout;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class MoldPartStockInOutList extends BasicResponse {
    private int totalCount;
    private List <MoldPartStockInOut> moldPartStockInOutList;

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

    /**
     * @return the moldPartStockInOutList
     */
    public List <MoldPartStockInOut> getMoldPartStockInOutList() {
        return moldPartStockInOutList;
    }

    /**
     * @param moldPartStockInOutList the moldPartStockInOutList to set
     */
    public void setMoldPartStockInOutList(List <MoldPartStockInOut> moldPartStockInOutList) {
        this.moldPartStockInOutList = moldPartStockInOutList;
    }
}
