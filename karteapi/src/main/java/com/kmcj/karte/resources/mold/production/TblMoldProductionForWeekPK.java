/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.production;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author admin
 */
@Embeddable
public class TblMoldProductionForWeekPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTION_DATE_START")
    @Temporal(TemporalType.DATE)
    private Date productionDateStart;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTION_DATE_END")
    @Temporal(TemporalType.DATE)
    private Date productionDateEnd;

    public TblMoldProductionForWeekPK() {
    }

    public TblMoldProductionForWeekPK(String componentId, String moldUuid, Date productionDateStart, Date productionDateEnd) {
        this.componentId = componentId;
        this.moldUuid = moldUuid;
        this.productionDateStart = productionDateStart;
        this.productionDateEnd = productionDateEnd;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public Date getProductionDateStart() {
        return productionDateStart;
    }

    public void setProductionDateStart(Date productionDateStart) {
        this.productionDateStart = productionDateStart;
    }

    public Date getProductionDateEnd() {
        return productionDateEnd;
    }

    public void setProductionDateEnd(Date productionDateEnd) {
        this.productionDateEnd = productionDateEnd;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (componentId != null ? componentId.hashCode() : 0);
        hash += (moldUuid != null ? moldUuid.hashCode() : 0);
        hash += (productionDateStart != null ? productionDateStart.hashCode() : 0);
        hash += (productionDateEnd != null ? productionDateEnd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldProductionForWeekPK)) {
            return false;
        }
        TblMoldProductionForWeekPK other = (TblMoldProductionForWeekPK) object;
        if ((this.componentId == null && other.componentId != null) || (this.componentId != null && !this.componentId.equals(other.componentId))) {
            return false;
        }
        if ((this.moldUuid == null && other.moldUuid != null) || (this.moldUuid != null && !this.moldUuid.equals(other.moldUuid))) {
            return false;
        }
        if ((this.productionDateStart == null && other.productionDateStart != null) || (this.productionDateStart != null && !this.productionDateStart.equals(other.productionDateStart))) {
            return false;
        }
        if ((this.productionDateEnd == null && other.productionDateEnd != null) || (this.productionDateEnd != null && !this.productionDateEnd.equals(other.productionDateEnd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.production.TblMoldProductionForWeekPK[ componentId=" + componentId + ", moldUuid=" + moldUuid + ", productionDateStart=" + productionDateStart + ", productionDateEnd=" + productionDateEnd + " ]";
    }
    
}
