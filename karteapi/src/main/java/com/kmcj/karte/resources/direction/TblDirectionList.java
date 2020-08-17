/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.direction;
import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 手配テーブルテーブルResponse
 * @author t.ariki
 */
public class TblDirectionList extends BasicResponse {    
    private List<TblDirection> tblDirections;
    private List<TblDirectionVo> TblDirectionVoList;
    private long count;
    private int pageNumber;
    private int pageTotal;
    public TblDirectionList() {
        tblDirections = new ArrayList<>();
    }
    /**
     * 
     * @return the tblDirections
     */
    public List<TblDirection> getTblDirections() {
        return tblDirections;
    }
    /**
     * @param tblDirections the tblDirections to set
     */
    public void setTblDirections(List<TblDirection> tblDirections) {
        this.tblDirections = tblDirections;
    }

    /**
     * @return the TblDirectionVoList
     */
    public List<TblDirectionVo> getTblDirectionVoList() {
        return TblDirectionVoList;
    }

    /**
     * @param TblDirectionVoList the TblDirectionVoList to set
     */
    public void setTblDirectionVoList(List<TblDirectionVo> TblDirectionVoList) {
        this.TblDirectionVoList = TblDirectionVoList;
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
