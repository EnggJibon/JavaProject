/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.stock;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.component.MstComponent;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblStockVoList extends BasicResponse {

    private List<TblStockVo> tblStockVos;
    private long count;
    private int pageNumber;
    private int pageTotal;


//     /**
//     * @return the tblStockVos
//     */
//    public List<TblStock> getTblStockVos() {
//        return tblStocks;
//    }
//     /**
//     * @param tblStockVos the tblStockVos to set
//     */
//    public void setTblStockVos(List<TblStock> tblStockVos) {
//        this.tblStocks = tblStocks;
//    }

    public List<TblStockVo> getTblStockVos() {
        return tblStockVos;
    }

    public void setTblStockVos(List<TblStockVo> tblStockVos) {
        this.tblStockVos = tblStockVos;
    }

    /**
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * @return the pageNumber
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * @param pageNumber the pageNumber to set
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * @return the pageTotal
     */
    public int getPageTotal() {
        return pageTotal;
    }

    /**
     * @param pageTotal the pageTotal to set
     */
    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }
}
