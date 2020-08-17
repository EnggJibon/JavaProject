/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report;

/**
 *
 * @author admin
 */
public class TblCustomReportQueryParamVo {

    private String paramId;

    private long reportId;

    private String paramName;

    private String paramValue;
        
    /**
     * @return the paramId
     */
    public String getParamId() {
        return paramId;
    }

    /**
     * @param paramId the paramId to set
     */
    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    /**
     * @return the reportId
     */
    public long getReportId() {
        return reportId;
    }

    /**
     * @param reportId the reportId to set
     */
    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    /**
     * @return the paramName
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * @param paramName the paramName to set
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    /**
     * @return the paramValue
     */
    public String getParamValue() {
        return paramValue;
    }

    /**
     * @param paramValue the paramValue to set
     */
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

}
