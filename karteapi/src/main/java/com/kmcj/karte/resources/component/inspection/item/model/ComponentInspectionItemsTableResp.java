/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.item.model;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 * Component inspection items table response model.
 * 
 * @author duanlin
 */
public class ComponentInspectionItemsTableResp extends BasicResponse {
    // for detail page
    private ComponentInspectionItemsTableInfo inspectionItemsTableInfo;

    // for list page
    private List<ComponentInspectionItemsTableInfo> inspectionItemsTableHeaderList;
    
    private List<ComponentInspectionItemsTableForBatch> dataForBatch;

    public ComponentInspectionItemsTableInfo getInspectionItemsTableInfo() {
        return inspectionItemsTableInfo;
    }

    public void setInspectionItemsTableInfo(ComponentInspectionItemsTableInfo inspectionItemsTableInfo) {
        this.inspectionItemsTableInfo = inspectionItemsTableInfo;
    }

    public List<ComponentInspectionItemsTableInfo> getInspectionItemsTableHeaderList() {
        return inspectionItemsTableHeaderList;
    }

    public void setInspectionItemsTableHeaderList(List<ComponentInspectionItemsTableInfo> inspectionItemsTableHeaderList) {
        this.inspectionItemsTableHeaderList = inspectionItemsTableHeaderList;
    }

    public List<ComponentInspectionItemsTableForBatch> getDataForBatch() {
        return dataForBatch;
    }

    public void setDataForBatch(List<ComponentInspectionItemsTableForBatch> dataForBatch) {
        this.dataForBatch = dataForBatch;
    }
}
