/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.timezone;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class MstTimezoneList extends BasicResponse {
    private List<MstTimezone> mstTimezones;

    /**
     * @return the mstTimezones
     */
    public List<MstTimezone> getMstTimezones() {
        return mstTimezones;
    }

    /**
     * @param mstTimezones the mstTimezones to set
     */
    public void setMstTimezones(List<MstTimezone> mstTimezones) {
        this.mstTimezones = mstTimezones;
    }
    
}
