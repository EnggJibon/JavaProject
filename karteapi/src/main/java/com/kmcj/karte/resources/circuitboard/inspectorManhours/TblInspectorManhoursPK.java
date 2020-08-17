/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.inspectorManhours;

import com.google.gson.Gson;
import com.kmcj.karte.resources.component.inspection.visualimage.TblComponentInspectionVisualImageFilePK;
import com.kmcj.karte.util.XmlDateAdapter;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author h.ishihara
 */
@Embeddable
public class TblInspectorManhoursPK implements Serializable  {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INSPECTOR_UUID")
    private String inspectorUuid;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
        
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PROCEDURE_ID")
    private String procedureId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "INSPECTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date inspectionDate;
        
    public TblInspectorManhoursPK(){
        
    }

    /**
     * @return the inspectorUuid
     */
    public String getInspectorUuid() {
        return inspectorUuid;
    }

    /**
     * @param inspectorUuid the inspectorUuid to set
     */
    public void setInspectorUuid(String inspectorUuid) {
        this.inspectorUuid = inspectorUuid;
    }
    
     /**
     * @return the componentId
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * @param componentId the componentId to set
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * @return the procedureId
     */
    public String getProcedureId() {
        return procedureId;
    }

    /**
     * @param procedureId the procedureId to set
     */
    public void setProcedureId(String procedureId) {
        this.procedureId = procedureId;
    }

    /**
     * @return the inspectionDate
     */
    public Date getInspectionDate() {
        return inspectionDate;
    }

    /**
     * @param inspectionDate the inspectionDate to set
     */
    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (inspectorUuid != null ? inspectorUuid.hashCode() : 0);
        hash += (componentId != null ? componentId.hashCode() : 0);
        hash += (procedureId != null ? procedureId.hashCode() : 0);
        hash += (inspectionDate != null ? inspectionDate.hashCode() : 0);
        return hash;
    }
    
        @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblComponentInspectionVisualImageFilePK)) {
            return false;
        }
        TblInspectorManhoursPK other = (TblInspectorManhoursPK) object;
        if ((this.inspectorUuid == null && other.inspectorUuid != null) || (this.inspectorUuid != null && !this.inspectorUuid.equals(other.inspectorUuid))) {
            return false;
        }

        if ((this.componentId == null && other.componentId != null) || (this.componentId != null && !this.componentId.equals(other.componentId))) {
            return false;
        }

        if ((this.procedureId == null && other.procedureId != null) || (this.procedureId != null && !this.procedureId.equals(other.procedureId))) {
            return false;
        }
              
        if ((this.inspectionDate == null && other.inspectionDate != null) || (this.inspectionDate != null && !this.inspectionDate.equals(other.inspectionDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
