/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.operation.log;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author kmc
 */
public class TblOperationLogList extends BasicResponse  {
    private long count;
    private int pageNumber;
    private int pageTotal;

    private List<TblOperationLogVo> tblOperationLogList;

    /**
     * @return the mstOperationList
     */
    public List<TblOperationLogVo> getTblOperationLogList() {
        return tblOperationLogList;
    }

    /**
     * @param tblOperationLogList the tblOperationLogList to set
     */
    public void setTblOperationLogList(List<TblOperationLogVo> tblOperationLogList) {
        this.tblOperationLogList = tblOperationLogList;
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
    
    
}
