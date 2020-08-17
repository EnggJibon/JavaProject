/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.location;

import com.kmcj.karte.BasicResponse;
/**
 *
 * @author c.darvin
 */
public class MstLocationResp extends BasicResponse {
    
    private MstLocation mstLocation;

    /**
     * @return the mstLocation
     */
    public MstLocation getMstLocation() {
        return mstLocation;
    }

    /**
     * @param mstLocation the mstLocation to set
     */
    public void setMstLocation(MstLocation mstLocation) {
        this.mstLocation = mstLocation;
    }
    
}
