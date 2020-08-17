/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.location;
import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstLocationList extends BasicResponse {

    private List< MstLocation> mstLocations;

    /**
     * @return the mstLocations
     */
    public List< MstLocation> getMstLocations() {
        return mstLocations;
    }

    /**
     * @param mstLocations the mstLocations to set
     */
    public void setMstLocations(List< MstLocation> mstLocations) {
        this.mstLocations = mstLocations;
    }
}
