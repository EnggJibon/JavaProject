/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.rel;

import com.kmcj.karte.resources.mold.part.MstMoldPart;

/**
 *
 * @author BnK Win10 2010
 */
public class MstMoldPartRelVO {
    
    private String location;
    
    private MstMoldPart mstMoldPart;

    public MstMoldPartRelVO() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MstMoldPart getMstMoldPart() {
        return mstMoldPart;
    }

    public void setMstMoldPart(MstMoldPart mstMoldPart) {
        this.mstMoldPart = mstMoldPart;
    }
}
