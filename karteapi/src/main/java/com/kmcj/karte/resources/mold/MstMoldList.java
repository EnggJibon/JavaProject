/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMoldList extends BasicResponse {
    private List <MstMold> mstMold;
    
    private List <MstMoldAutoComplete> mstMoldAutoComplete;

    /**
     * @return the mstMoldComponentRelation
     */
    public List <MstMold> getMstMold() {
        return mstMold;
    }

    /**
     * @param mstMold the mstMoldComponentRelation to set
     */
    public void setMstMold(List <MstMold> mstMold) {
        this.mstMold = mstMold;
    }       

    /**
     * @return the mstMoldAutoComplete
     */
    public List <MstMoldAutoComplete> getMstMoldAutoComplete() {
        return mstMoldAutoComplete;
    }

    /**
     * @param mstMoldAutoComplete the mstMoldAutoComplete to set
     */
    public void setMstMoldAutoComplete(List <MstMoldAutoComplete> mstMoldAutoComplete) {
        this.mstMoldAutoComplete = mstMoldAutoComplete;
    }
}
