/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.authctrl;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class MstAuthCtrlList extends BasicResponse {
    private List<MstAuthCtrl> mstAuthCtrlList;

    /**
     * @return the mstAuthCtrlList
     */
    public List<MstAuthCtrl> getMstAuthCtrlList() {
        return mstAuthCtrlList;
    }

    /**
     * @param mstAuthCtrlList the mstAuthCtrlList to set
     */
    public void setMstAuthCtrlList(List<MstAuthCtrl> mstAuthCtrlList) {
        this.mstAuthCtrlList = mstAuthCtrlList;
    }
    
}
