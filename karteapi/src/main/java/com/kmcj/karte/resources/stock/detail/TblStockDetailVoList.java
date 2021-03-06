/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.stock.detail;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblStockDetailVoList extends BasicResponse {

    private List<TblStockDetailVo> tblStockDetailVos;

    private String moveDateFrom; //初期表示は当日から１ヶ月前と当日を表示

    private String moveDateTo;   //初期表示は当日から１ヶ月前と当日を表示
    
    private long count;

    private int pageNumber;

    private int pageTotal;

    /**
     * @return the tblStockDetailVos
     */
    public List<TblStockDetailVo> getTblStockDetailVos() {
        return tblStockDetailVos;
    }

    /**
     * @param tblStockDetailVos the tblStockDetailVos to set
     */
    public void setTblStockDetailVos(List<TblStockDetailVo> tblStockDetailVos) {
        this.tblStockDetailVos = tblStockDetailVos;
    }

    /**
     * @return the moveDateFrom
     */
    public String getMoveDateFrom() {
        return moveDateFrom;
    }

    /**
     * @param moveDateFrom the moveDateFrom to set
     */
    public void setMoveDateFrom(String moveDateFrom) {
        this.moveDateFrom = moveDateFrom;
    }

    /**
     * @return the moveDateTo
     */
    public String getMoveDateTo() {
        return moveDateTo;
    }

    /**
     * @param moveDateTo the moveDateTo to set
     */
    public void setMoveDateTo(String moveDateTo) {
        this.moveDateTo = moveDateTo;
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
