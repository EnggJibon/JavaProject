/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author h.ishihara
 */
public class InspectionResultSum extends BasicResponse {
    private List<CircuitBoardInspectionResultSum> inspectionResultSumList = null;

    /**
     * @return the inspectionResultSumList
     */
    public List<CircuitBoardInspectionResultSum> getInspectionResultSumList() {
        return inspectionResultSumList;
    }

    /**
     * @param inspectionResultSumList the inspectionResultSumList to set
     */
    public void setInspectionResultSumList(List<CircuitBoardInspectionResultSum> inspectionResultSumList) {
        this.inspectionResultSumList = inspectionResultSumList;
    }
}
