/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.asset;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstAssetList extends BasicResponse {
    
    private long count;
    private int pageNumber;
    private int pageTotal;
    
    private List<MstAssetVo> mstAssetList;

    /**
     * @return the mstAssetList
     */
    public List<MstAssetVo> getMstAssetList() {
        return mstAssetList;
    }

    /**
     * @param mstAssetList the mstAssetList to set
     */
    public void setMstAssetList(List<MstAssetVo> mstAssetList) {
        this.mstAssetList = mstAssetList;
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
