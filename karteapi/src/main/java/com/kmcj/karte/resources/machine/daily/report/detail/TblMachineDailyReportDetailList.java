/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.daily.report.detail;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author liujiyong
 */
public class TblMachineDailyReportDetailList extends BasicResponse {
    
    private List<TblMachineDailyReportDetail> tblMachineDailyReportDetails;
    
    private long count;
    private int pageNumber;
    private int pageTotal;
    
    public TblMachineDailyReportDetailList() {
    }
    
    public void setTblMachineDailyReportDetails(List<TblMachineDailyReportDetail> tblMachineDailyReportDetails) {
        this.tblMachineDailyReportDetails = tblMachineDailyReportDetails;
    }

    public List<TblMachineDailyReportDetail> getTblMachineDailyReportDetails() {
        return tblMachineDailyReportDetails;
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
