/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMoldDetailList extends BasicResponse{

    private List <MstMoldDetail> mstMoldDetail;
    
    private long count;
    private int pageNumber;
    private int pageTotal;

    /**
     * @return the mstMoldDetail
     */
    public List <MstMoldDetail> getMstMoldDetail() {
        return mstMoldDetail;
    }

    /**
     * @param mstMoldDetail the mstMoldDetail to set
     */
    public void setMstMoldDetail(List <MstMoldDetail> mstMoldDetail) {
        this.mstMoldDetail = mstMoldDetail;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }
   
}
