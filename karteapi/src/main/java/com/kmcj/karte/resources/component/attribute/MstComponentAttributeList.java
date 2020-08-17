/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.attribute;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstComponentAttributeList  extends BasicResponse{
    private List <MstComponentAttribute> mstComponentAttributes;
    
    private List <MstComponentAttributes> mstComponentAttributesList;

    /**
     * @return the mstComponentAttributes
     */
    public List <MstComponentAttribute> getMstComponentAttributes() {
        return mstComponentAttributes;
    }

    /**
     * @param mstComponentAttributes the mstComponentAttributes to set
     */
    public void setMstComponentAttributes(List <MstComponentAttribute> mstComponentAttributes) {
        this.mstComponentAttributes = mstComponentAttributes;
    }

    /**
     * @return the mstComponentAttributesList
     */
    public List <MstComponentAttributes> getMstComponentAttributesList() {
        return mstComponentAttributesList;
    }

    /**
     * @param mstComponentAttributesList the mstComponentAttributesList to set
     */
    public void setMstComponentAttributesList(List <MstComponentAttributes> mstComponentAttributesList) {
        this.mstComponentAttributesList = mstComponentAttributesList;
    }
    
    
}
