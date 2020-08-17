/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma.thtransport;

import com.kmcj.karte.resources.component.MstComponent;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "tbl_production_detail")
@XmlRootElement
@Cacheable(value = false)
public class TblThProductionDetail implements Serializable {
    private static final long serialVersionUID = 1L;
       
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @Size(max = 45)
    @Column(name = "PRODUCTION_ID")
    private String productionId;
  
    @Transient
    public String componentCode;
    @Transient
    public String componentName;
       
    
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")  
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "PRODUCTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private TblThProduction production;
         
    @XmlTransient
    public MstComponent getMstComponent() {
        return this.mstComponent;
    }
    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }  
    
    public TblThProductionDetail() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }
    public String getComponentId() {
        return componentId;
    }
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentCode() {
        return componentCode;
    }
    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
    }
    
    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    @XmlTransient
    public TblThProduction getProduction() {
        return production;
    }

    public void setProduction(TblThProduction production) {
        this.production = production;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblThProductionDetail)) {
            return false;
        }
        TblThProductionDetail other = (TblThProductionDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.sigma.thtransport.TblThProductionDetail[ id=" + id + " ]";
    }
  
    public void formatProductionDetailJsonData() {
        if (this.mstComponent != null) {
            this.componentCode = mstComponent.getComponentCode();
            this.componentName = mstComponent.getComponentName();
        }
    }   
}
