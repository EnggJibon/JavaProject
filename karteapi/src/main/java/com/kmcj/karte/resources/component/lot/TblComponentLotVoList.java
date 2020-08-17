/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.lot;

import java.util.List;

/**
 *
 * @author admin
 */
public class TblComponentLotVoList {

    private List<TblComponentLotVo> tblComponentLotVos;

    private long count;

    private int pageNumber;

    private int pageTotal;

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

    /**
     * @return the tblComponentLotVos
     */
    public List<TblComponentLotVo> getTblComponentLotVos() {
        return tblComponentLotVos;
    }

    /**
     * @param tblComponentLotVos the tblComponentLotVos to set
     */
    public void setTblComponentLotVos(List<TblComponentLotVo> tblComponentLotVos) {
        this.tblComponentLotVos = tblComponentLotVos;
    }

}
