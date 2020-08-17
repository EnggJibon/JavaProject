package com.kmcj.karte.resources.component.inspection.result.model;

import com.kmcj.karte.BasicResponse;
import java.util.List;

public class ComponentInspectionDetailColumnResponse extends BasicResponse {
    private List<TblGridColumnMemory> listInspectionDetailColumn;

    public void setListInspectionDetailColumn(List<TblGridColumnMemory> listInspectionDetailColumn) {
        this.listInspectionDetailColumn = listInspectionDetailColumn;
    }

    public List<TblGridColumnMemory> getListInspectionDetailColumn() {
        return this.listInspectionDetailColumn;
    }
}
