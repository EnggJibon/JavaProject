/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.serialnumber;

import java.util.List;

/**
 *
 * @author h.ishihara
 */
public class CircuitboardSerialNumberData {
    private List<MstCircuitBoardSerialNumber> serialNumberList = null;

    /**
     * @return the serialNumberList
     */
    public List<MstCircuitBoardSerialNumber> getSerialNumberList() {
        return serialNumberList;
    }

    /**
     * @param serialNumberList the serialNumberList to set
     */
    public void setSerialNumberList(List<MstCircuitBoardSerialNumber> serialNumberList) {
        this.serialNumberList = serialNumberList;
    }
}
