/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part;

import java.util.List;

/**
 *
 * @author BnK Win10 2010
 */
public class MstMoldPartDetailList {

    private List<MstMoldPartDetail> mstMoldParts;
    private long totalCount = 0;
    
    public MstMoldPartDetailList() {
    }

    public List<MstMoldPartDetail> getMstMoldParts() {
        return mstMoldParts;
    }

    public void setMstMoldParts(List<MstMoldPartDetail> mstMoldParts) {
        this.mstMoldParts = mstMoldParts;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
