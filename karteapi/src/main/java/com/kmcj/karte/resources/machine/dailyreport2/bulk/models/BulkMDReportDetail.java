/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2.bulk.models;

import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.XmlDateAdapter2;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author f.kitaoji
 */
public class BulkMDReportDetail {

    
    private int detailType;
    private String workerId;
    //@XmlJavaTypeAdapter(XmlDateAdapter2.class)
    private String startDatetime;
    //@XmlJavaTypeAdapter(XmlDateAdapter2.class)
    private String endDatetime;
    private int durationMinutes;
    private String workPhaseCode;
    private String partCode;
    private String moldId;
    private int shotCount;
    private int disposedShotCount;
    private String lotNumber;
    private String machineDowntimeCode;
    private String downtimeComment;
    @XmlElement(name="prodDetails")
    private List<BulkMDReportProdDetail> prodDetails;   

    @Transient
    private Date startDatetimeValue = null;
    @Transient
    private Date endDatetimeValue = null;
    
    /**
     * @return the detailType
     */
    public int getDetailType() {
        return detailType;
    }

    /**
     * @param detailType the detailType to set
     */
    public void setDetailType(int detailType) {
        this.detailType = detailType;
    }

    /**
     * @return the workerId
     */
    public String getWorkerId() {
        return workerId;
    }

    /**
     * @param workerId the workerId to set
     */
    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    /**
     * @return the startDatetime
     */
    public String getStartDatetime() {
        return startDatetime;
    }
    
    @XmlTransient
    public Date getStartDatetimeValue() {
        if (this.startDatetimeValue == null) {
            this.startDatetimeValue = DateFormat.strToDatetimeISO(this.startDatetime);
        }
        return this.startDatetimeValue;
    }

    /**
     * @param startDatetime the startDatetime to set
     */
    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    /**
     * @return the endDatetime
     */
    public String getEndDatetime() {
        return endDatetime;
    }
    
    @XmlTransient
    public Date getEndDatetimeValue() {
        if (this.endDatetimeValue == null) {
            this.endDatetimeValue = DateFormat.strToDatetimeISO(this.endDatetime);
        }
        return endDatetimeValue;
    }

    /**
     * @param endDatetime the endDatetime to set
     */
    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    /**
     * @return the durationMinutes
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }

    /**
     * @param durationMinutes the durationMinutes to set
     */
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    /**
     * @return the workPhaseCode
     */
    public String getWorkPhaseCode() {
        return workPhaseCode;
    }

    /**
     * @param workPhaseCode the workPhaseCode to set
     */
    public void setWorkPhaseCode(String workPhaseCode) {
        this.workPhaseCode = workPhaseCode;
    }

    /**
     * @return the moldId
     */
    public String getMoldId() {
        return moldId;
    }

    /**
     * @param moldId the moldId to set
     */
    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    /**
     * @return the shotCount
     */
    public int getShotCount() {
        return shotCount;
    }

    /**
     * @param shotCount the shotCount to set
     */
    public void setShotCount(int shotCount) {
        this.shotCount = shotCount;
    }

    /**
     * @return the disposedShotCount
     */
    public int getDisposedShotCount() {
        return disposedShotCount;
    }

    /**
     * @param disposedShotCount the disposedShotCount to set
     */
    public void setDisposedShotCount(int disposedShotCount) {
        this.disposedShotCount = disposedShotCount;
    }

    /**
     * @return the machineDowntimeCode
     */
    public String getMachineDowntimeCode() {
        return machineDowntimeCode;
    }

    /**
     * @param machineDowntimeCode the machineDowntimeCode to set
     */
    public void setMachineDowntimeCode(String machineDowntimeCode) {
        this.machineDowntimeCode = machineDowntimeCode;
    }

    /**
     * @return the downtimeComment
     */
    public String getDowntimeComment() {
        return downtimeComment;
    }

    /**
     * @param downtimeComment the downtimeComment to set
     */
    public void setDowntimeComment(String downtimeComment) {
        this.downtimeComment = downtimeComment;
    }

    /**
     * @return the prodDetails
     */
    public List<BulkMDReportProdDetail> getProdDetails() {
        return prodDetails;
    }

    /**
     * @param prodDetails the prodDetails to set
     */
    public void setProdDetails(List<BulkMDReportProdDetail> prodDetails) {
        this.prodDetails = prodDetails;
    }

    /**
     * @return the partCode
     */
    public String getPartCode() {
        return partCode;
    }

    /**
     * @param partCode the partCode to set
     */
    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    /**
     * @return the lotNumber
     */
    public String getLotNumber() {
        return lotNumber;
    }

    /**
     * @param lotNumber the lotNumber to set
     */
    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }


}
