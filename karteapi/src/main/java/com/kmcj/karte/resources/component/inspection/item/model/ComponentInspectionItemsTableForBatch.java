/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item.model;

import com.kmcj.karte.resources.component.inspection.item.MstComponentInspectionItemsTable;
import com.kmcj.karte.resources.component.inspection.item.MstComponentInspectionItemsTableDetail;
import java.util.List;

/**
 * Component inspection items table response model.
 * 
 * @author duanlin
 */
public class ComponentInspectionItemsTableForBatch {
    private String componentCode;
    
    private MstComponentInspectionItemsTable inspectionItemsTable;
    private List<MstComponentInspectionItemsTableDetail> inspectionItemsTableDetails;

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }

    public MstComponentInspectionItemsTable getInspectionItemsTable() {
        return inspectionItemsTable;
    }

    public void setInspectionItemsTable(MstComponentInspectionItemsTable inspectionItemsTable) {
        this.inspectionItemsTable = inspectionItemsTable;
    }

    public List<MstComponentInspectionItemsTableDetail> getInspectionItemsTableDetails() {
        return inspectionItemsTableDetails;
    }

    public void setInspectionItemsTableDetails(List<MstComponentInspectionItemsTableDetail> inspectionItemsTableDetails) {
        this.inspectionItemsTableDetails = inspectionItemsTableDetails;
    }
}
