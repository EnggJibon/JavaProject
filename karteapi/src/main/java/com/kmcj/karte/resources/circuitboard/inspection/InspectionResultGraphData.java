/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspection;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.graphical.GraphicalItemInfo;
import java.util.List;

/**
 *
 * @author h.ishihara
 */
public class InspectionResultGraphData extends BasicResponse {
    private GraphicalItemInfo graphicalItemInfo;
    private List<GraphicalItemInfo > graphicalItemInfoList;
    public InspectionResultGraphData(){
        
    }

    /**
     * @return the graphicalItemInfo
     */
    public GraphicalItemInfo getGraphicalItemInfo() {
        return graphicalItemInfo;
    }

    /**
     * @param graphicalItemInfo the graphicalItemInfo to set
     */
    public void setGraphicalItemInfo(GraphicalItemInfo graphicalItemInfo) {
        this.graphicalItemInfo = graphicalItemInfo;
    }
    
    /**
     * @return the graphicalItemInfoList
     */
    public List<GraphicalItemInfo > getGraphicalItemInfoList() {
        return graphicalItemInfoList;
    }

    /**
     * @param graphicalItemInfoList the graphicalItemInfoList to set
     */
    public void setGraphicalItemInfoList(List<GraphicalItemInfo > graphicalItemInfoList) {
        this.graphicalItemInfoList = graphicalItemInfoList;
    }
}
