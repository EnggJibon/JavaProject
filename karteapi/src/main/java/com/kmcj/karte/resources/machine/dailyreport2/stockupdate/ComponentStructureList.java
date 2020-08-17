/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2.stockupdate;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.component.structure.MstComponentStructureVoList;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class ComponentStructureList  extends BasicResponse {

    private List<MstComponentStructureVoList> mstComponentStructureVoList;

    /**
     * @return the mstComponentStructureVoList
     */
    public List<MstComponentStructureVoList> getMstComponentStructureVoList() {
        return mstComponentStructureVoList;
    }

    /**
     * @param mstComponentStructureVoList the mstComponentStructureVoList to set
     */
    public void setMstComponentStructureVoList(List<MstComponentStructureVoList> mstComponentStructureVoList) {
        this.mstComponentStructureVoList = mstComponentStructureVoList;
    }
    
}
