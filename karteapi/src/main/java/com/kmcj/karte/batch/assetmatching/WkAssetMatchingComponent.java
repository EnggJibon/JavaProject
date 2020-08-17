/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.assetmatching;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zds
 */
@Entity
@Table(name = "wk_asset_matching_component")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WkAssetMatchingComponent.findByComponentCode", query = "SELECT w FROM WkAssetMatchingComponent w WHERE w.componentCode = :componentCode"),
    @NamedQuery(name = "WkAssetMatchingComponent.delete", query = "DELETE FROM WkAssetMatchingComponent")
})
@Cacheable(value = false)
public class WkAssetMatchingComponent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 45)
    @Column(name = "COMPONENT_CODE")
    private String componentCode;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "COMPONENT_UUID")
    private String componentUuid;

    public WkAssetMatchingComponent() {
    }

    public WkAssetMatchingComponent(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public String getComponentCode() {
        return componentCode;
    }

    public void setComponentCode(String componentCode) {
        this.componentCode = componentCode;
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
        hash += (moldUuid != null ? moldUuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WkAssetMatchingComponent)) {
            return false;
        }
        WkAssetMatchingComponent other = (WkAssetMatchingComponent) object;
        if ((this.moldUuid == null && other.moldUuid != null) || (this.moldUuid != null && !this.moldUuid.equals(other.moldUuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.batch.assetmatching.WkAssetMatchingComponent[ moldUuid=" + moldUuid + " ]";
    }

    /**
     * @return the componentUuid
     */
    public String getComponentUuid() {
        return componentUuid;
    }

    /**
     * @param componentUuid the componentUuid to set
     */
    public void setComponentUuid(String componentUuid) {
        this.componentUuid = componentUuid;
    }
    
}
