/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.rel;

import com.kmcj.karte.BasicResponse;
import java.util.List;

public class MstMoldPartRelList extends BasicResponse {

    private List<MstMoldPartRel> mstMoldPartRels;

    /**
     * @return the mstMoldPartRels
     */
    public List<MstMoldPartRel> getMstMoldPartRels() {
        return mstMoldPartRels;
    }

    /**
     * @param mstMoldPartRels the mstMoldPartRels to set
     */
    public void setMstMoldPartRels(List<MstMoldPartRel> mstMoldPartRels) {
        this.mstMoldPartRels = mstMoldPartRels;
    }

}
