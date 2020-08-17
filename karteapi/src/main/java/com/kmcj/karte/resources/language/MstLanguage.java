/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.language;

import com.kmcj.karte.resources.choice.MstChoice;
import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "mst_language")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstLanguage.findAll", query = "SELECT m FROM MstLanguage m ORDER BY m.seq"),
    @NamedQuery(name = "MstLanguage.findDefault", query = "SELECT m FROM MstLanguage m WHERE m.systemDefault = 1"),
    @NamedQuery(name = "MstLanguage.findById", query = "SELECT m FROM MstLanguage m WHERE m.id = :id"),
    @NamedQuery(name = "MstLanguage.findByLang", query = "SELECT m FROM MstLanguage m WHERE m.lang = :lang"),
    @NamedQuery(name = "MstLanguage.findBySeq", query = "SELECT m FROM MstLanguage m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstLanguage.findByCreateDate", query = "SELECT m FROM MstLanguage m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstLanguage.findByUpdateDate", query = "SELECT m FROM MstLanguage m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstLanguage.findByCreateUserUuid", query = "SELECT m FROM MstLanguage m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstLanguage.findByUpdateUserUuid", query = "SELECT m FROM MstLanguage m WHERE m.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class MstLanguage implements Serializable {

    @OneToMany(mappedBy = "mstLanguage")
    private Collection<MstUser> mstUserCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mstLanguage")
    private Collection<MstChoice> mstChoiceCollection;

    @Basic(optional = false)
    @NotNull
    @Column(name = "SYSTEM_DEFAULT")
    private int systemDefault;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "LANG")
    private String lang;
    @Column(name = "SEQ")
    private Integer seq;
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Size(max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Size(max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;

    public MstLanguage() {
    }

    public MstLanguage(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @XmlTransient    
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @XmlTransient    
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @XmlTransient    
    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    @XmlTransient    
    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
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
        if (!(object instanceof MstLanguage)) {
            return false;
        }
        MstLanguage other = (MstLanguage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.language.MstLanguage[ id=" + id + " ]";
    }

    public int getSystemDefault() {
        return systemDefault;
    }

    public void setSystemDefault(int systemDefault) {
        this.systemDefault = systemDefault;
    }

    @XmlTransient
    public Collection<MstChoice> getMstChoiceCollection() {
        return mstChoiceCollection;
    }

    public void setMstChoiceCollection(Collection<MstChoice> mstChoiceCollection) {
        this.mstChoiceCollection = mstChoiceCollection;
    }

    @XmlTransient
    public Collection<MstUser> getMstUserCollection() {
        return mstUserCollection;
    }

    public void setMstUserCollection(Collection<MstUser> mstUserCollection) {
        this.mstUserCollection = mstUserCollection;
    }

    
}
