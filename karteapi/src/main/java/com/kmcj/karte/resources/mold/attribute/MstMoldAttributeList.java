/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.attribute;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMoldAttributeList  extends BasicResponse{
    private List <MstMoldAttribute> mstMoldAttribute;
    
    private List <MstMoldAttributes> MstMoldAttributes;

    /**
     * @return the mstMoldAttribute
     */
    public List <MstMoldAttribute> getMstMoldAttribute() {
        return mstMoldAttribute;
    }

    /**
     * @param mstMoldAttribute the mstMoldAttribute to set
     */
    public void setMstMoldAttribute(List <MstMoldAttribute> mstMoldAttribute) {
        this.mstMoldAttribute = mstMoldAttribute;
    }

    /**
     * @return the MstMoldAttributes
     */
    public List <MstMoldAttributes> getMstMoldAttributes() {
        return MstMoldAttributes;
    }

    /**
     * @param MstMoldAttributes the MstMoldAttributes to set
     */
    public void setMstMoldAttributes(List <MstMoldAttributes> MstMoldAttributes) {
        this.MstMoldAttributes = MstMoldAttributes;
    }
    
    
}
