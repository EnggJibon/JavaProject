/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.spec;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMoldSpecList extends BasicResponse {

    private List<MstMoldSpec> mstMoldSpec;
    
    private List<MstMoldSpecDeatil> mstMoldSpecDeatil;
    

    /**
     * @return the mstMoldSpec
     */
    public List<MstMoldSpec> getMstMoldSpec() {
        return mstMoldSpec;
    }

    /**
     * @param mstMoldSpec the mstMoldSpec to set
     */
    public void setMstMoldSpec(List<MstMoldSpec> mstMoldSpec) {
        this.mstMoldSpec = mstMoldSpec;
    }

    /**
     * @return the mstMoldSpecDeatil
     */
    public List<MstMoldSpecDeatil> getMstMoldSpecDeatil() {
        return mstMoldSpecDeatil;
    }

    /**
     * @param mstMoldSpecDeatil the mstMoldSpecDeatil to set
     */
    public void setMstMoldSpecDeatil(List<MstMoldSpecDeatil> mstMoldSpecDeatil) {
        this.mstMoldSpecDeatil = mstMoldSpecDeatil;
    }

}
