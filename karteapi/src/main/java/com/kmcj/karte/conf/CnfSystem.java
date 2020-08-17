/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.conf;

import com.kmcj.karte.resources.dictionary.MstDictionary;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "cnf_system")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CnfSystem.findAll", query = "SELECT c FROM CnfSystem c ORDER BY c.seq"),
    @NamedQuery(name = "CnfSystem.findByConfigGroup", query = "SELECT c FROM CnfSystem c WHERE c.cnfSystemPK.configGroup = :configGroup"),
    @NamedQuery(name = "CnfSystem.findByConfigKey", query = "SELECT c FROM CnfSystem c WHERE c.cnfSystemPK.configGroup = :configGroup AND c.cnfSystemPK.configKey = :configKey"),
    @NamedQuery(name = "CnfSystem.findByConfigValue", query = "SELECT c FROM CnfSystem c WHERE c.configValue = :configValue"),
    @NamedQuery(name = "CnfSystem.findByMemo", query = "SELECT c FROM CnfSystem c WHERE c.memo = :memo"),
    @NamedQuery(name = "CnfSystem.findByCreateDate", query = "SELECT c FROM CnfSystem c WHERE c.createDate = :createDate"),
    @NamedQuery(name = "CnfSystem.update", query = 
        "UPDATE CnfSystem c SET c.configValue = :configValue, c.updateDate = :updateDate, c.updateUserUuid = :updateUserUuid WHERE c.cnfSystemPK.configGroup = :configGroup AND c.cnfSystemPK.configKey = :configKey"),
    @NamedQuery(name = "CnfSystem.findByDictKey", query = "SELECT c FROM CnfSystem c WHERE c.dictKey = :dictKey"),
    @NamedQuery(name = "CnfSystem.findByGroupDictKey", query = "SELECT c FROM CnfSystem c WHERE c.groupDictKey = :groupDictKey")
})
@Cacheable(value = false)
public class CnfSystem implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "SEQ")
    private Integer seq;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    
    @Size(max = 100)
    @Column(name = "DICT_KEY")
    private String dictKey;
    
    @Size(max = 100)
    @Column(name = "GROUP_DICT_KEY")
    private String groupDictKey;
    
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CnfSystemPK cnfSystemPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "CONFIG_VALUE")
    private String configValue;
    @Size(max = 100)
    @Column(name = "MEMO")
    private String memo;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Transient
    private String cnfSystemKey;
    @Transient
    private boolean modified = false;
    @Transient
    private String dictVal;
    @Transient
    private String groupDictVal;

    public CnfSystem() {
    }

    public CnfSystem(CnfSystemPK cnfSystemPK) {
        this.cnfSystemPK = cnfSystemPK;
    }

    public CnfSystem(CnfSystemPK cnfSystemPK, String configValue) {
        this.cnfSystemPK = cnfSystemPK;
        this.configValue = configValue;
    }

    public CnfSystem(String configGroup, String configKey) {
        this.cnfSystemPK = new CnfSystemPK(configGroup, configKey);
    }

    public CnfSystemPK getCnfSystemPK() {
        return cnfSystemPK;
    }

    public void setCnfSystemPK(CnfSystemPK cnfSystemPK) {
        this.cnfSystemPK = cnfSystemPK;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cnfSystemPK != null ? cnfSystemPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CnfSystem)) {
            return false;
        }
        CnfSystem other = (CnfSystem) object;
        if ((this.cnfSystemPK == null && other.cnfSystemPK != null) || (this.cnfSystemPK != null && !this.cnfSystemPK.equals(other.cnfSystemPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.conf.CnfSystem[ cnfSystemPK=" + cnfSystemPK + " ]";
    }

    /**
     * @return the cnfSystemKey
     */
    @XmlElement
    public String getCnfSystemKey() {
        return this.cnfSystemPK.getConfigGroup() + "." + this.cnfSystemPK.getConfigKey();  //confSystemKey;
    }

    /**
     * @param cnfSystemKey the cnfSystemKey to set
     */
    public void setCnfSystemKey(String cnfSystemKey) {
        this.cnfSystemKey = cnfSystemKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    /**
     * @return the modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }
    
    public String getDictKey() {
        return dictKey;
    }
    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }
    
    public String getGroupDictKey() {
        return groupDictKey;
    }
    public void setGroupDictKey(String groupDictKey) {
        this.groupDictKey = groupDictKey;
    }
    public String getDictVal() {
        return dictVal;
    }
    public void setDictVal(String dictVal) {
        this.dictVal = dictVal;
    }
    public String getGroupDictVal() {
        return groupDictVal;
    }
    public void setGroupDictVal(String groupDictVal) {
        this.groupDictVal = groupDictVal;
    }
}
