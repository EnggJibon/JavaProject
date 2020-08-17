/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2.bulk.models;

import com.kmcj.karte.util.DateFormat;
//import com.kmcj.karte.util.XmlDateAdapter3;
import java.util.Date;
import java.util.List;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
//import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author f.kitaoji
 */
@XmlRootElement(name="machineDailyReport")
public class BulkMDReport {

    //@XmlJavaTypeAdapter(XmlDateAdapter3.class)
    //private Date reportDate;
    private String reportDate;
    private String machineId;
    private String reportPersonId;
    @Transient
    private Date reportDateValue = null;

    @XmlElement(name="details")
    private List<BulkMDReportDetail> details;
    
    public BulkMDReport() {
        
    }

    /**
     * @return the reportDate
     */
    public String getReportDate() {
        return reportDate;
    }
    
    @XmlTransient
    public java.util.Date getReportDateValue() {
        if (reportDateValue == null) {
            reportDateValue = DateFormat.hyphenStrToDate(this.reportDate);
        }
        return reportDateValue;
    }

    /**
     * @param reportDate the reportDate to set
     */
    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    /**
     * @return the machineId
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machineId to set
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * @return the details
     */
    public List<BulkMDReportDetail> getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(List<BulkMDReportDetail> details) {
        this.details = details;
    }

    /**
     * @return the reportPersonId
     */
    public String getReportPersonId() {
        return reportPersonId;
    }

    /**
     * @param reportPersonId the reportPersonId to set
     */
    public void setReportPersonId(String reportPersonId) {
        this.reportPersonId = reportPersonId;
    }
    
}
