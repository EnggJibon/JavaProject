package com.kmcj.karte.resources.component.inspection.result.model;

public class ComponentInspectionDetailColumnInput {
    private  String userId;
    private String screenId;
    private String gridId;
    private String columnId;
    private int colWidth;
    private int colOrder;

    public void setUserId(String userId) { this.userId = userId; }
    public String getUserId() { return this.userId; }
    public void setScreenId(String screenId) { this.screenId = screenId; }
    public String getScreenId() { return this.screenId; }
    public void setGridId(String gridId) { this.gridId = gridId; }
    public String getGridId() { return this.gridId; }
    public void setColumnId(String columnId) { this.columnId = columnId; }
    public String getColumnId() { return this.columnId; }
    public void setColWidth(int colWidth) { this.colWidth = colWidth; }
    public int getColWidth() { return this.colWidth; }
    public void setColOrder(int colOrder) { this.colOrder = colOrder; }
    public int getColOrder() { return this.colOrder; }
}
