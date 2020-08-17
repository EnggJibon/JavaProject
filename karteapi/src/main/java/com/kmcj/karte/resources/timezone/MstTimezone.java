/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.timezone;

import com.kmcj.karte.resources.user.MstUser;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "mst_timezone")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstTimezone.findAll", query = "SELECT m FROM MstTimezone m ORDER BY m.seq"),
    @NamedQuery(name = "MstTimezone.findUseOnly", query = "SELECT m FROM MstTimezone m WHERE m.useFlg = 1 ORDER BY m.seq"),
    @NamedQuery(name = "MstTimezone.findById", query = "SELECT m FROM MstTimezone m WHERE m.id = :id"),
    @NamedQuery(name = "MstTimezone.findByTimezoneName", query = "SELECT m FROM MstTimezone m WHERE m.timezoneName = :timezoneName"),
    @NamedQuery(name = "MstTimezone.findByJavaZoneId", query = "SELECT m FROM MstTimezone m WHERE m.javaZoneId = :javaZoneId"),
    @NamedQuery(name = "MstTimezone.findBySystemDefault", query = "SELECT m FROM MstTimezone m WHERE m.systemDefault = :systemDefault"),
    @NamedQuery(name = "MstTimezone.findBySeq", query = "SELECT m FROM MstTimezone m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstTimezone.findByUseFlg", query = "SELECT m FROM MstTimezone m WHERE m.useFlg = :useFlg"),
    @NamedQuery(name = "MstTimezone.findByCreateDate", query = "SELECT m FROM MstTimezone m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstTimezone.findByUpdateDate", query = "SELECT m FROM MstTimezone m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstTimezone.findByCreateUserUuid", query = "SELECT m FROM MstTimezone m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstTimezone.findByUpdateUserUuid", query = "SELECT m FROM MstTimezone m WHERE m.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class MstTimezone implements Serializable {

    @OneToMany(mappedBy = "mstTimezone")
    private Collection<MstUser> mstUserCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ID")
    private String id;
    @Size(max = 100)
    @Column(name = "TIMEZONE_NAME")
    private String timezoneName;
    @Size(max = 100)
    @Column(name = "JAVA_ZONE_ID")
    private String javaZoneId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SYSTEM_DEFAULT")
    private int systemDefault;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
    @Basic(optional = false)
    @NotNull
    @Column(name = "USE_FLG")
    private int useFlg;
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

    public MstTimezone() {
    }

    public MstTimezone(String id) {
        this.id = id;
    }

    public MstTimezone(String id, int systemDefault, int seq, int useFlg) {
        this.id = id;
        this.systemDefault = systemDefault;
        this.seq = seq;
        this.useFlg = useFlg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimezoneName() {
        return timezoneName;
    }

    public void setTimezoneName(String timezoneName) {
        this.timezoneName = timezoneName;
    }

    public String getJavaZoneId() {
        return javaZoneId;
    }

    public void setJavaZoneId(String javaZoneId) {
        this.javaZoneId = javaZoneId;
    }

    public int getSystemDefault() {
        return systemDefault;
    }

    public void setSystemDefault(int systemDefault) {
        this.systemDefault = systemDefault;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getUseFlg() {
        return useFlg;
    }

    public void setUseFlg(int useFlg) {
        this.useFlg = useFlg;
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
        if (!(object instanceof MstTimezone)) {
            return false;
        }
        MstTimezone other = (MstTimezone) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.timezone.MstTimezone[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<MstUser> getMstUserCollection() {
        return mstUserCollection;
    }

    public void setMstUserCollection(Collection<MstUser> mstUserCollection) {
        this.mstUserCollection = mstUserCollection;
    }
    
}
