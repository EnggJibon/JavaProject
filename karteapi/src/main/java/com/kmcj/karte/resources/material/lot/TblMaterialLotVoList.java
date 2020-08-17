/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material.lot;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMaterialLotVoList extends BasicResponse {

    private List<TblMaterialLotVo> tblMaterialLotVo;

    private List<TblMaterialLot> tblMaterialLots;

    private long count;

    private int pageNumber;

    private int pageTotal;

    /**
     * @return the tblMaterialLotVo
     */
    public List<TblMaterialLotVo> getTblMaterialLotVo() {
        return tblMaterialLotVo;
    }

    /**
     * @param tblMaterialLotVo the tblMaterialLotVo to set
     */
    public void setTblMaterialLotVo(List<TblMaterialLotVo> tblMaterialLotVo) {
        this.tblMaterialLotVo = tblMaterialLotVo;
    }

    public List<TblMaterialLot> getTblMaterialLots() {
        return tblMaterialLots;
    }

    public void setTblMaterialLots(List<TblMaterialLot> tblMaterialLots) {
        this.tblMaterialLots = tblMaterialLots;
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
