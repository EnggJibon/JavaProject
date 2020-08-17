/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.production;

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
public class TblMachineProductionForDayPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
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
    @Column(name = "PRODUCTION_DATE")
    @Temporal(TemporalType.DATE)
    private Date productionDate;

    public TblMachineProductionForDayPK() {
    }

    public TblMachineProductionForDayPK(String machineUuid, String componentId, String moldUuid, Date productionDate) {
        this.machineUuid = machineUuid;
        this.componentId = componentId;
        this.moldUuid = moldUuid;
        this.productionDate = productionDate;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (machineUuid != null ? machineUuid.hashCode() : 0);
        hash += (componentId != null ? componentId.hashCode() : 0);
        hash += (moldUuid != null ? moldUuid.hashCode() : 0);
        hash += (productionDate != null ? productionDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineProductionForDayPK)) {
            return false;
        }
        TblMachineProductionForDayPK other = (TblMachineProductionForDayPK) object;
        if ((this.machineUuid == null && other.machineUuid != null) || (this.machineUuid != null && !this.machineUuid.equals(other.machineUuid))) {
            return false;
        }
        if ((this.componentId == null && other.componentId != null) || (this.componentId != null && !this.componentId.equals(other.componentId))) {
            return false;
        }
         if ((this.moldUuid == null && other.moldUuid != null) || (this.moldUuid != null && !this.moldUuid.equals(other.moldUuid))) {
            return false;
        }
        if ((this.productionDate == null && other.productionDate != null) || (this.productionDate != null && !this.productionDate.equals(other.productionDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.production.TblMachineProductionForDayPK[ machineUuid=" + machineUuid + ", componentId=" + componentId + ", productionDate=" + productionDate + " ]";
    }

    
    
}
