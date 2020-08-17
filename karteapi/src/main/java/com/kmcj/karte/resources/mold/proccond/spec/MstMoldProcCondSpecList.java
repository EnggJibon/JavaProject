/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond.spec;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class MstMoldProcCondSpecList extends BasicResponse {

    private List<MstMoldProcCondSpec> mstMoldProcCondSpecList;

    /**
     * @return the mstMoldProcCondSpecList
     */
    public List<MstMoldProcCondSpec> getMstMoldProcCondSpecList() {
        return mstMoldProcCondSpecList;
    }

    /**
     * @param mstMoldProcCondSpecList the mstMoldProcCondSpecList to set
     */
    public void setMstMoldProcCondSpecList(List<MstMoldProcCondSpec> mstMoldProcCondSpecList) {
        this.mstMoldProcCondSpecList = mstMoldProcCondSpecList;
    }

}
