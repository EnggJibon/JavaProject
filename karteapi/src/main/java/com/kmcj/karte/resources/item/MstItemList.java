/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.item;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstItemList extends BasicResponse {

    private List<MstItem> mstItems;

    /**
     * @return the mstItems
     */
    public List<MstItem> getMstItems() {
        return mstItems;
    }

    /**
     * @param mstItems the mstItems to set
     */
    public void setMstItems(List<MstItem> mstItems) {
        this.mstItems = mstItems;
    }

}
