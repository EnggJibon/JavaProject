/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part;

import com.kmcj.karte.BasicResponse;
import java.util.List;

public class MstMoldPartList extends BasicResponse {

    private List<MstMoldPart> mstMoldParts;
    private long totalCount = 0;

    /**
     * @return the mstMoldPartMoldParts
     */
    public List<MstMoldPart> getMstMoldParts() {
        return mstMoldParts;
    }

    /**
     * @param mstMoldParts the mstMoldParts to set
     */
    public void setMstMoldParts(List<MstMoldPart> mstMoldParts) {
        this.mstMoldParts = mstMoldParts;
    }
    
    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

}
