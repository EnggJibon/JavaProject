/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.maintenance.cycle;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author t.takasaki
 */
@Entity
@Table(name = "mst_mold_part_rel")
@XmlRootElement
@Cacheable(value = false)
public class MstMoldPartForBatch implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    
    @Column(name = "LOCATION")
    private String localtion;
    
    @Column(name = "MOLD_PART_ID")
    private String moldPartId;
    
    @Column(name = "RPL_CL_SHOT_CNT")
    private Integer rplClShotCnt;
    
    @Column(name = "RPL_CL_PROD_TIME_HOUR")
    private Integer rplClProdTimeHour;
    
    @Column(name = "RPL_CL_LAPPSED_DAY")
    private Integer rplClLappsedDay;
    
    @Column(name = "RPR_CL_SHOT_CNT")
    private Integer rprClShotCnt;
    
    @Column(name = "RPR_CL_PROD_TIME_HOUR")
    private Integer rprClProdTimeHour;
    
    @Column(name = "RPR_CL_LAPPSED_DAY")
    private Integer rprClLappsedDay;
    
    @Column(name = "AFT_RPL_SHOT_CNT")
    private Integer aftRplShotCnt;
    
    @Column(name = "AFT_RPL_PROD_TIME_HOUR")
    private Integer aftRplProdTimeHour;
    
    @Column(name = "LAST_RPL_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastRplDateTime;
    
    @Column(name = "AFT_RPR_SHOT_CNT")
    private Integer aftRprShotCnt;
    
    @Column(name = "AFT_RPR_PROD_TIME_HOUR")
    private Integer aftRprProdTimeHour;
    
    @Column(name = "LAST_RPR_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastRprDateTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public String getLocaltion() {
        return localtion;
    }

    public String getMoldPartId() {
        return moldPartId;
    }

    public Integer getRplClShotCnt() {
        return rplClShotCnt;
    }

    public Integer getRplClProdTimeHour() {
        return rplClProdTimeHour;
    }

    public Integer getRplClLappsedDay() {
        return rplClLappsedDay;
    }

    public Integer getRprClShotCnt() {
        return rprClShotCnt;
    }

    public Integer getRprClProdTimeHour() {
        return rprClProdTimeHour;
    }

    public Integer getRprClLappsedDay() {
        return rprClLappsedDay;
    }

    public Integer getAftRplShotCnt() {
        return aftRplShotCnt;
    }

    public Integer getAftRplProdTimeHour() {
        return aftRplProdTimeHour;
    }

    public Date getLastRplDateTime() {
        return lastRplDateTime;
    }

    public Integer getAftRprShotCnt() {
        return aftRprShotCnt;
    }

    public Integer getAftRprProdTimeHour() {
        return aftRprProdTimeHour;
    }

    public Date getLastRprDateTime() {
        return lastRprDateTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public void setLocaltion(String localtion) {
        this.localtion = localtion;
    }

    public void setMoldPartId(String moldPartId) {
        this.moldPartId = moldPartId;
    }

    public void setRplClShotCnt(Integer rplClShotCnt) {
        this.rplClShotCnt = rplClShotCnt;
    }

    public void setRplClProdTimeHour(Integer rplClProdTimeHour) {
        this.rplClProdTimeHour = rplClProdTimeHour;
    }

    public void setRplClLappsedDay(Integer rplClLappsedDay) {
        this.rplClLappsedDay = rplClLappsedDay;
    }

    public void setRprClShotCnt(Integer rprClShotCnt) {
        this.rprClShotCnt = rprClShotCnt;
    }

    public void setRprClProdTimeHour(Integer rprClProdTimeHour) {
        this.rprClProdTimeHour = rprClProdTimeHour;
    }

    public void setRprClLappsedDay(Integer rprClLappsedDay) {
        this.rprClLappsedDay = rprClLappsedDay;
    }

    public void setAftRplShotCnt(Integer aftRplShotCnt) {
        this.aftRplShotCnt = aftRplShotCnt;
    }

    public void setAftRplProdTimeHour(Integer aftRplProdTimeHour) {
        this.aftRplProdTimeHour = aftRplProdTimeHour;
    }

    public void setLastRplDateTime(Date lastRplDateTime) {
        this.lastRplDateTime = lastRplDateTime;
    }

    public void setAftRprShotCnt(Integer aftRprShotCnt) {
        this.aftRprShotCnt = aftRprShotCnt;
    }

    public void setAftRprProdTimeHour(Integer aftRprProdTimeHour) {
        this.aftRprProdTimeHour = aftRprProdTimeHour;
    }

    public void setLastRprDateTime(Date lastRprDateTime) {
        this.lastRprDateTime = lastRprDateTime;
    }

    @Override
    public String toString() {
        return "MstMoldPartForBatch{" + "id=" + id + '}';
    }

}
