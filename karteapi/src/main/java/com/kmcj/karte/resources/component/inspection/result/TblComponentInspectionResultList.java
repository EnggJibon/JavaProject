/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result;

import com.kmcj.karte.resources.component.inspection.result.model.*;
import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 * 
 * @author Apeng
 */
public class TblComponentInspectionResultList extends BasicResponse {
    
    private List<ComponentInspectionResultInfo> componentInspectionResultInfoList;
    
    /**
     * @return the componentInspectionResultInfoList
     */
    public List<ComponentInspectionResultInfo> getComponentInspectionResultInfoList() {
        return componentInspectionResultInfoList;
    }

    /**
     * @param componentInspectionResultInfoList the componentInspectionResultInfoList to set
     */
    public void setComponentInspectionResultInfoList(List<ComponentInspectionResultInfo> componentInspectionResultInfoList) {
        this.componentInspectionResultInfoList = componentInspectionResultInfoList;
    }
    
}
