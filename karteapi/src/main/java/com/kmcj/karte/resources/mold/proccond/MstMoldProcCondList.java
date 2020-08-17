/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class MstMoldProcCondList extends BasicResponse {

    private List<MstMoldProcCond> mstMoldProcCondList;
    
    private List<MstMoldProcConds> mstMoldProcConds;

    /**
     * @return the mstMoldProcCondList
     */
    public List<MstMoldProcCond> getMstMoldProcCondList() {
        return mstMoldProcCondList;
    }

    /**
     * @param mstMoldProcCondList the mstMoldProcCondList to set
     */
    public void setMstMoldProcCondList(List<MstMoldProcCond> mstMoldProcCondList) {
        this.mstMoldProcCondList = mstMoldProcCondList;
    }

    /**
     * @return the mstMoldProcConds
     */
    public List<MstMoldProcConds> getMstMoldProcConds() {
        return mstMoldProcConds;
    }

    /**
     * @param mstMoldProcConds the mstMoldProcConds to set
     */
    public void setMstMoldProcConds(List<MstMoldProcConds> mstMoldProcConds) {
        this.mstMoldProcConds = mstMoldProcConds;
    }
    
    

}
