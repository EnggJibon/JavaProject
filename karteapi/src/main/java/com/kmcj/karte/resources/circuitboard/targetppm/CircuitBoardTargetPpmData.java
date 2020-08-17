/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.targetppm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author h.ishihara
 */
public class CircuitBoardTargetPpmData {
    private List<MstCircuitBoardTargetPpm> targetPpmList = null;
    
    public CircuitBoardTargetPpmData()
    {
        targetPpmList = new ArrayList<>();
    }

    /**
     * @return the targetPpmList
     */
    public List<MstCircuitBoardTargetPpm> getTargetPpmList() {
        return targetPpmList;
    }

    /**
     * @param targetPpmList the targetPpmList to set
     */
    public void setTargetPpmList(List<MstCircuitBoardTargetPpm> targetPpmList) {
        this.targetPpmList = targetPpmList;
    }
}
