/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item.model;

import com.kmcj.karte.ImportResultResponse;

/**
 *
 * @author zhoushuai
 */
public class ComponentInspectionItemImportCSVResp extends ImportResultResponse {
    
    // MST_COMPONENT_INSPECTION_ITEMS_TABLE.ID (For Web Search)
    private String inspectionItemId;

    public String getInspectionItemId() {
        return inspectionItemId;
    }

    public void setInspectionItemId(String inspectionItemId) {
        this.inspectionItemId = inspectionItemId;
    }

}
