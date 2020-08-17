/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work;
import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 作業実績テーブルResponse
 * @author t.ariki
 */
public class TblWorkList extends BasicResponse {    
    private List<TblWork> tblWorks;
    private long count;
    private int pageNumber;
    private int pageTotal;
    
    public TblWorkList() {
        tblWorks = new ArrayList<>();
    }
    /**
     * 
     * @return the tblWorks
     */
    public List<TblWork> getTblWorks() {
        return tblWorks;
    }
    /**
     * @param tblWorks the tblWorks to set
     */
    public void setTblWorks(List<TblWork> tblWorks) {
        this.tblWorks = tblWorks;
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
