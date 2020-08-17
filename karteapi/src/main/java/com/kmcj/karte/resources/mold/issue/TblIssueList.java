/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.issue;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class TblIssueList extends BasicResponse {
    
    private List<TblIssueVo> tblIssueVoList;
    
    private long count;
    private int pageNumber;
    private int pageTotal;

    /**
     * @return the tblIssueVoList
     */
    public List<TblIssueVo> getTblIssueVoList() {
        return tblIssueVoList;
    }

    /**
     * @param tblIssueVoList the tblIssueVoList to set
     */
    public void setTblIssueVoList(List<TblIssueVo> tblIssueVoList) {
        this.tblIssueVoList = tblIssueVoList;
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
