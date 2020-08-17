package com.kmcj.karte.resources.component.inspection.result.model;

public class ComponentInspectionExtCompleteInput {
    private String id;
    private Integer incomingInspectionResult;
    private String incomingInspectionComment;
    private Integer inspectionStatus;

    public void setId(String id) { this.id = id; }
    public String getId() { return this.id; }
    public void setIncomingInspectionResult(Integer inspectionResult) { this.incomingInspectionResult = inspectionResult; }
    public Integer getIncomingInspectionResult() { return this.incomingInspectionResult; }
    public void setIncomingInspectionComment(String inspectionComment) { this.incomingInspectionComment = inspectionComment; }
    public String getIncomingInspectionComment() { return this.incomingInspectionComment; }
    public void setInspectionStatus(Integer inspectionStatus) { this.inspectionStatus = inspectionStatus; }
    public Integer getInspectionStatus() { return this.inspectionStatus; }
}