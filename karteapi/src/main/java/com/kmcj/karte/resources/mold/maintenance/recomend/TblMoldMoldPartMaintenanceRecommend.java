/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.recomend;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author c.darvin
 */

public class TblMoldMoldPartMaintenanceRecommend implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    
    private String moldUuid;
    @Size(max = 45)
    
    private String moldId;
    private String moldName;
    private Date lastMainteDate;
    private int shotCount;
    private BigDecimal totalProducingTimeHour;
    private String mainteCycleId;
    private String mainteCycleName;
    private String location;
    private String moldPartCode;
    private int replaceOrRepair;
    private Date lastReplaceDate;
    private Date lastRepairDate;
    private int replaceShotCount;
    private int repairShotCount;
    private int replaceProdTimeHour;
    private int repairProdTimeHour;
    
    public TblMoldMoldPartMaintenanceRecommend() {
    }

    public TblMoldMoldPartMaintenanceRecommend(String moldUuid) {
        this.moldUuid = moldUuid;
    }
    
    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }
    
    public String getMoldId() {
        return moldId;
    }

    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }
    
    public String getMoldName() {
        return moldName;
    }

    public void setMoldName(String moldName) {
        this.moldName = moldName;
    }
    
    public Date getLastMainteDate() {
        return lastMainteDate;
    }

    public void setLastMainteDate(Date lastMainteDate) {
        this.lastMainteDate = lastMainteDate;
    }
    
    public BigDecimal getTotalProducingTimeHour() {
        return totalProducingTimeHour;
    }

    public void setTotalProducingTimeHour(BigDecimal totalProducingTimeHour) {
        this.totalProducingTimeHour = totalProducingTimeHour;
    }
    
    public int getShotCount() {
        return shotCount;
    }

    public void setShotCount(int shotCount) {
        this.shotCount = shotCount;
    }
    
    public String getMainteCycleId() {
        return mainteCycleId;
    }
    
    public void setMainteCycleId(String mainteCycleId) {
        this.mainteCycleId = mainteCycleId;
    }
    
    public String getMainteCycleName() {
        return mainteCycleName;
    }
    
    public void setMainteCycleName(String mainteCycleName) {
        this.mainteCycleName = mainteCycleName;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getMoldPartCode() {
        return moldPartCode;
    }
    
    public void setMoldPartCode(String moldPartCode) {
        this.moldPartCode = moldPartCode;
    }
    
    public int getReplaceOrRepair() {
        return replaceOrRepair;
    }

    public void setReplaceOrRepair(int replaceOrRepair) {
        this.replaceOrRepair = replaceOrRepair;
    }
    
    public Date getLastReplaceDate() {
        return lastReplaceDate;
    }

    public void setLastReplaceDate(Date lastReplaceDate) {
        this.lastReplaceDate = lastReplaceDate;
    }

    public Date getLastRepairDate() {
        return lastRepairDate;
    }

    public void setLastRepairDate(Date lastRepairDate) {
        this.lastRepairDate = lastRepairDate;
    }
    
    public int getReplaceShotCount() {
        return replaceShotCount;
    }
    
    public void setReplaceShotCount(int replaceShotCount) {
        this.replaceShotCount = replaceShotCount;
    }
    
    public int getRepairShotCount() {
        return repairShotCount;
    }
    
    public void setRepairShotCount(int repairShotCount) {
        this.repairShotCount = repairShotCount;
    }
    
    public int getReplaceProdTimeHour() {
        return replaceProdTimeHour;
    }
    
    public void setReplaceProdTimeHour(int replaceProdTimeHour) {
        this.replaceProdTimeHour = replaceProdTimeHour;
    }
    
    public int getRepairProdTimeHour() {
        return repairProdTimeHour;
    }
    
    public void setRepairProdTimeHour(int repairProdTimeHour) {
        this.repairProdTimeHour = repairProdTimeHour;
    }
    
}
