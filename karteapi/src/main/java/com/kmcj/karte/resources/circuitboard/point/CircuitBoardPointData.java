/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.point;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author h.ishihara
 */
public class CircuitBoardPointData {
    private List<MstCircuitBoardPoint> pointList = null;
    
    public CircuitBoardPointData()
    {
        pointList = new ArrayList<>();
    }

    /**
     * @return the pointList
     */
    public List<MstCircuitBoardPoint> getPointList() {
        return pointList;
    }

    /**
     * @param pointList the pointList to set
     */
    public void setPointList(List<MstCircuitBoardPoint> pointList) {
        this.pointList = pointList;
    }
}
