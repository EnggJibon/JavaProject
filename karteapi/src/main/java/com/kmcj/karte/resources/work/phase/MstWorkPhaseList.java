/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.work.phase;
import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 作業工程マスタResponse
 * @author t.ariki
 */
public class MstWorkPhaseList extends BasicResponse {    
    private List<MstWorkPhase> mstWorkPhases;
    
    public MstWorkPhaseList() {
        mstWorkPhases = new ArrayList<>();
    }
    /**
     * @return the mstWorkPhases
     */
    public List<MstWorkPhase> getMstWorkPhases() {
        return mstWorkPhases;
    }
    /**
     * @param mstWorkPhases the mstWorkPhases to set
     */
    public void setMstWorkPhases(List<MstWorkPhase> mstWorkPhases) {
        this.mstWorkPhases = mstWorkPhases;
    }
}
