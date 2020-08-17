/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author h.ishihara
 */
public class InspectionResult {
    private List<TblCircuitBoardInspectionResult> inspectionResultList = null;
    private List<TblCircuitBoardInspectionResultSum> inspectionResultSumList = null;
    private List<TblCircuitBoardWorkAssignment> workAssignmentList = null;
    private HashMap<String, Integer> detailItemList = null;
    
    public InspectionResult()
    {
        inspectionResultList = new ArrayList<>();
        inspectionResultSumList = new ArrayList<>();
        workAssignmentList = new ArrayList<>();
    }
    
    public List<TblCircuitBoardInspectionResultSum> getInspectionResultSumList() {
        return inspectionResultSumList;
    }

    public void setInspectionResultSumList(List<TblCircuitBoardInspectionResultSum> inspectionResultSumList) {
        this.inspectionResultSumList = inspectionResultSumList;
    }

    /**
     * @return the inspectionResultList
     */
    public List<TblCircuitBoardInspectionResult> getInspectionResultList() {
        return inspectionResultList;
    }

    /**
     * @param inspectionResultList the inspectionResultList to set
     */
    public void setInspectionResultList(List<TblCircuitBoardInspectionResult> inspectionResultList) {
        this.inspectionResultList = inspectionResultList;
    }

    /**
     * @return the workAssignmentList
     */
    public List<TblCircuitBoardWorkAssignment> getWorkAssignmentList() {
        return workAssignmentList;
    }

    /**
     * @param workAssignmentList the workAssignmentList to set
     */
    public void setWorkAssignmentList(List<TblCircuitBoardWorkAssignment> workAssignmentList) {
        this.workAssignmentList = workAssignmentList;
    }

    /**
     * @return the detailItemList
     */
    public HashMap<String, Integer> getDetailItemList() {
        return detailItemList;
    }

    /**
     * @param detailItemList the detailItemList to set
     */
    public void setDetailItemList(HashMap<String, Integer> detailItemList) {
        this.detailItemList = detailItemList;
    }
    
    
}
