/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.conf.application;

import java.io.Serializable;
import javax.persistence.Basic;
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
 * @author m.jibon
 */
@Entity
@Table(name = "cnf_application")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CnfApplication.findAll", query = "SELECT c FROM CnfApplication c"),
    @NamedQuery(name = "CnfApplication.findById", query = "SELECT c FROM CnfApplication c WHERE c.id = :id"),
    @NamedQuery(name = "CnfApplication.findByConfigKey", query = "SELECT c FROM CnfApplication c WHERE c.configKey = :configKey"),
    @NamedQuery(name = "CnfApplication.findByConfigValue", query = "SELECT c FROM CnfApplication c WHERE c.configValue = :configValue"),
    @NamedQuery(name = "CnfApplication.findByMemo", query = "SELECT c FROM CnfApplication c WHERE c.memo = :memo")})
public class CnfApplication implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CONFIG_KEY")
    private String configKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "CONFIG_VALUE")
    private String configValue;
    @Size(max = 100)
    @Column(name = "MEMO")
    private String memo;

    public CnfApplication() {
    }

    public CnfApplication(String configKey) {
        this.configKey = configKey;
    }

    public CnfApplication(String configKey, String id, String configValue) {
        this.configKey = configKey;
        this.id = id;
        this.configValue = configValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (configKey != null ? configKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CnfApplication)) {
            return false;
        }
        CnfApplication other = (CnfApplication) object;
        if ((this.configKey == null && other.configKey != null) || (this.configKey != null && !this.configKey.equals(other.configKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.conf.application.CnfApplication[ configKey=" + configKey + " ]";
    }
    
}
