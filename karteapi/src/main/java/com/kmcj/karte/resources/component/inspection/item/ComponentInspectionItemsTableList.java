/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item;

import com.kmcj.karte.ImportResultResponse;
import java.util.List;

/**
 * 
 * @author Apeng
 */
public class ComponentInspectionItemsTableList  extends ImportResultResponse {

    private List<MstComponentInspectionItemsTableResp> componentInspectionItemsTableRespSuccess;
    private List<MstComponentInspectionItemsTableResp> componentInspectionItemsTableRespError;

    /**
     * @return the componentInspectionItemsTableRespSuccess
     */
    public List<MstComponentInspectionItemsTableResp> getComponentInspectionItemsTableRespSuccess() {
        return componentInspectionItemsTableRespSuccess;
    }

    /**
     * @param componentInspectionItemsTableRespSuccess the componentInspectionItemsTableRespSuccess to set
     */
    public void setComponentInspectionItemsTableRespSuccess(List<MstComponentInspectionItemsTableResp> componentInspectionItemsTableRespSuccess) {
        this.componentInspectionItemsTableRespSuccess = componentInspectionItemsTableRespSuccess;
    }

    /**
     * @return the componentInspectionItemsTableRespError
     */
    public List<MstComponentInspectionItemsTableResp> getComponentInspectionItemsTableRespError() {
        return componentInspectionItemsTableRespError;
    }

    /**
     * @param componentInspectionItemsTableRespError the componentInspectionItemsTableRespError to set
     */
    public void setComponentInspectionItemsTableRespError(List<MstComponentInspectionItemsTableResp> componentInspectionItemsTableRespError) {
        this.componentInspectionItemsTableRespError = componentInspectionItemsTableRespError;
    }
    
}
