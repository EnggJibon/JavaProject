/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset.disposal;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblAssetDisposalRequestVoList extends BasicResponse {
    
    private long count;
    private int pageNumber;
    private int pageTotal;
    
    private List<TblAssetDisposalRequestVo> tblAssetDisposalRequestVos;


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

    /**
     * @return the tblAssetDisposalRequestVos
     */
    public List<TblAssetDisposalRequestVo> getTblAssetDisposalRequestVos() {
        return tblAssetDisposalRequestVos;
    }

    /**
     * @param tblAssetDisposalRequestVos the tblAssetDisposalRequestVos to set
     */
    public void setTblAssetDisposalRequestVos(List<TblAssetDisposalRequestVo> tblAssetDisposalRequestVos) {
        this.tblAssetDisposalRequestVos = tblAssetDisposalRequestVos;
    }
    
}
