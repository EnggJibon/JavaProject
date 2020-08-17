/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.detail;
import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 生産実績明細テーブルResponse
 * @author t.ariki
 */
public class TblProductionDetailList extends BasicResponse {    
    private List<TblProductionDetail> tblProductionDetails;
    private List<TblProductionDetailVo> tblProductionDetailVos;
    private long count;
    private int pageNumber;
    private int pageTotal;
    
    public TblProductionDetailList() {
        tblProductionDetails = new ArrayList<>();
    }
    public List<TblProductionDetail> getTblProductionDetails() {
        return tblProductionDetails;
    }
    public void setTblProductionDetails(List<TblProductionDetail> tblProductionDetails) {
        this.tblProductionDetails = tblProductionDetails;
    }

    public List<TblProductionDetailVo> getTblProductionDetailVos() {
        return tblProductionDetailVos;
    }

    public void setTblProductionDetailVos(List<TblProductionDetailVo> tblProductionDetailVos) {
        this.tblProductionDetailVos = tblProductionDetailVos;
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
