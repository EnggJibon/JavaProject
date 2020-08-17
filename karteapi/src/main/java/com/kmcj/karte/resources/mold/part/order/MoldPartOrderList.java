/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.order;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class MoldPartOrderList extends BasicResponse {
    private List<MoldPartOrder> moldPartOrders;

    /**
     * @return the moldPartOrders
     */
    public List<MoldPartOrder> getMoldPartOrders() {
        return moldPartOrders;
    }

    /**
     * @param moldPartOrders the moldPartOrders to set
     */
    public void setMoldPartOrders(List<MoldPartOrder> moldPartOrders) {
        this.moldPartOrders = moldPartOrders;
    }
}
