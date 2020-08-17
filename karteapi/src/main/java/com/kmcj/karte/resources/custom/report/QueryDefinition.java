/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report;

/**
 *
 * @author zdsoft
 */
public class QueryDefinition {
    private Long reportId;
    
    private String reportName;
    
    private String description;
    
    private String query;
    
    private String categoryId;
    
    private String categoryName;
    
    private Parameter parameters;

    public Long getReportId() {
        return reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public String getDescription() {
        return description;
    }

    public String getQuery() {
        return query;
    }
    
    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
    
    public Parameter getParameters() {
        return parameters;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuery(String query) {
        this.query = query;
    }
    
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setParameters(Parameter parameters) {
        this.parameters = parameters;
    }
}
