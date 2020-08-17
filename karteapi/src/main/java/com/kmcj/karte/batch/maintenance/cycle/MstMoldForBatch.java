/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.maintenance.cycle;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "mst_mold")
@XmlRootElement
@Cacheable(value = false)
public class MstMoldForBatch implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MOLD_ID")
    private String moldId;
    
    @Column(name = "MAINTE_STATUS")
    private Integer mainteStatus;
    
    @Column(name = "LAST_MAINTE_DATE")
    @Temporal(TemporalType.DATE)
    private Date lastMainteDate;
    
    @Column(name = "AFTER_MAINTE_TOTAL_PRODUCING_TIME_HOUR")
    private BigDecimal afterMainteTotalProducingTimeHour;
    
    @Column(name = "AFTER_MAINTE_TOTAL_SHOT_COUNT")
    private Integer afterMainteTotalShotCount;
    
    @Column(name = "MAINTE_CYCLE_ID_01")
    private String mainteCycleId01;
    
    @Column(name = "MAINTE_CYCLE_ID_02")
    private String mainteCycleId02;
    
    @Column(name = "MAINTE_CYCLE_ID_03")
    private String mainteCycleId03;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMoldId() {
        return moldId;
    }

    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public Integer getMainteStatus() {
        return mainteStatus;
    }

    public void setMainteStatus(Integer mainteStatus) {
        this.mainteStatus = mainteStatus;
    }

    public Date getLastMainteDate() {
        return lastMainteDate;
    }

    public void setLastMainteDate(Date lastMainteDate) {
        this.lastMainteDate = lastMainteDate;
    }

    public BigDecimal getAfterMainteTotalProducingTimeHour() {
        return afterMainteTotalProducingTimeHour;
    }

    public void setAfterMainteTotalProducingTimeHour(BigDecimal afterMainteTotalProducingTimeHour) {
        this.afterMainteTotalProducingTimeHour = afterMainteTotalProducingTimeHour;
    }

    public Integer getAfterMainteTotalShotCount() {
        return afterMainteTotalShotCount;
    }

    public void setAfterMainteTotalShotCount(Integer afterMainteTotalShotCount) {
        this.afterMainteTotalShotCount = afterMainteTotalShotCount;
    }

    public String getMainteCycleId01() {
        return mainteCycleId01;
    }

    public void setMainteCycleId01(String mainteCycleId01) {
        this.mainteCycleId01 = mainteCycleId01;
    }

    public String getMainteCycleId02() {
        return mainteCycleId02;
    }

    public void setMainteCycleId02(String mainteCycleId02) {
        this.mainteCycleId02 = mainteCycleId02;
    }

    public String getMainteCycleId03() {
        return mainteCycleId03;
    }

    public void setMainteCycleId03(String mainteCycleId03) {
        this.mainteCycleId03 = mainteCycleId03;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMoldForBatch)) {
            return false;
        }
        MstMoldForBatch other = (MstMoldForBatch) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.batch.maintenance.cycle.MstMoldForBatch[ id=" + uuid + " ]";
    }
    
}
