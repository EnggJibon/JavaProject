/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond.attribute;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMoldProcCondAttributeList  extends BasicResponse{
    private List <MstMoldProcCondAttribute> mstMoldProcCondAttribute;

    /**
     * @return the mstMoldProcCondAttribute
     */
    public List <MstMoldProcCondAttribute> getMstMoldProcCondAttribute() {
        return mstMoldProcCondAttribute;
    }

    /**
     * @param mstMoldProcCondAttribute the mstMoldProcCondAttribute to set
     */
    public void setMstMoldProcCondAttribute(List <MstMoldProcCondAttribute> mstMoldProcCondAttribute) {
        this.mstMoldProcCondAttribute = mstMoldProcCondAttribute;
    }
    
}
