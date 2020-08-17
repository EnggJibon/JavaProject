/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.function;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_function")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstFunction.findAll", query = "SELECT m FROM MstFunction m ORDER BY m.seq"),
    @NamedQuery(name = "MstFunction.findById", query = "SELECT m FROM MstFunction m WHERE m.id = :id"),
    @NamedQuery(name = "MstFunction.findBySeq", query = "SELECT m FROM MstFunction m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstFunction.findByDictKey", query = "SELECT m FROM MstFunction m WHERE m.dictKey = :dictKey"),
    @NamedQuery(name = "MstFunction.findByJpFunctionName", query = "SELECT m FROM MstFunction m WHERE m.jpFunctionName = :jpFunctionName"),
    @NamedQuery(name = "MstFunction.findByCreateDate", query = "SELECT m FROM MstFunction m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstFunction.findByUpdateDate", query = "SELECT m FROM MstFunction m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstFunction.findByCreateUserUuid", query = "SELECT m FROM MstFunction m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstFunction.findByUpdateUserUuid", query = "SELECT m FROM MstFunction m WHERE m.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class MstFunction implements Serializable {

    @Size(max = 45)
    @Column(name = "SCREEN_TYPE")
    private String screenType;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "SEQ")
    private Integer seq;
    @Size(max = 45)
    @Column(name = "DICT_KEY")
    private String dictKey;
    @Size(max = 45)
    @Column(name = "JP_FUNCTION_NAME")
    private String jpFunctionName;
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
    
    @Transient
    private String dictFunctionName;

    public MstFunction() {
    }

    public MstFunction(String id) {
        this.id = id;
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

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    public String getJpFunctionName() {
        return jpFunctionName;
    }

    public void setJpFunctionName(String jpFunctionName) {
        this.jpFunctionName = jpFunctionName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstFunction)) {
            return false;
        }
        MstFunction other = (MstFunction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.files.MstFunction[ id=" + id + " ]";
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    /**
     * @return the dictFunctionName
     */
    public String getDictFunctionName() {
        return dictFunctionName;
    }

    /**
     * @param dictFunctionName the dictFunctionName to set
     */
    public void setDictFunctionName(String dictFunctionName) {
        this.dictFunctionName = dictFunctionName;
    }
    
}
