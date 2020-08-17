/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mgmt.location;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMgmtLocationList extends BasicResponse {

    private List<MstMgmtLocation> mstMgmtLocations;

    /**
     * @return the mstMgmtLocations
     */
    public List<MstMgmtLocation> getMstMgmtLocations() {
        return mstMgmtLocations;
    }

    /**
     * @param mstMgmtLocations the mstMgmtLocations to set
     */
    public void setMstMgmtLocations(List<MstMgmtLocation> mstMgmtLocations) {
        this.mstMgmtLocations = mstMgmtLocations;
    }

}
