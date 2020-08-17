/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.conf;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author f.kitaoji
 */
@Embeddable
public class CnfSystemPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "CONFIG_GROUP")
    private String configGroup;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CONFIG_KEY")
    private String configKey;

    public CnfSystemPK() {
    }

    public CnfSystemPK(String configGroup, String configKey) {
        this.configGroup = configGroup;
        this.configKey = configKey;
    }

    public String getConfigGroup() {
        return configGroup;
    }

    public void setConfigGroup(String configGroup) {
        this.configGroup = configGroup;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (configGroup != null ? configGroup.hashCode() : 0);
        hash += (configKey != null ? configKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CnfSystemPK)) {
            return false;
        }
        CnfSystemPK other = (CnfSystemPK) object;
        if ((this.configGroup == null && other.configGroup != null) || (this.configGroup != null && !this.configGroup.equals(other.configGroup))) {
            return false;
        }
        if ((this.configKey == null && other.configKey != null) || (this.configKey != null && !this.configKey.equals(other.configKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.conf.CnfSystemPK[ configGroup=" + configGroup + ", configKey=" + configKey + " ]";
    }
    
}
