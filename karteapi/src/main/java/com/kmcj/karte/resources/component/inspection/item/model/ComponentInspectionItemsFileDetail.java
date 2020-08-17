package com.kmcj.karte.resources.component.inspection.item.model;

import java.util.Date;

public class ComponentInspectionItemsFileDetail {
    private String id;
    private String componentId;
    private String outgoingCompanyId;
    private String incomingCompanyId;
    private String fileUuid;
    private Date uploadDate;
    private String fileName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getOutgoingCompanyId() {
        return outgoingCompanyId;
    }

    public void setOutgoingCompanyId(String outgoingCompanyId) {
        this.outgoingCompanyId = outgoingCompanyId;
    }

    public String getIncomingCompanyId() {
        return incomingCompanyId;
    }

    public void setIncomingCompanyId(String incomingCompanyId) {
        this.incomingCompanyId = incomingCompanyId;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
