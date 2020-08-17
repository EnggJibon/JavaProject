/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.dashboard;

import com.kmcj.karte.resources.user.MstUserMin;
import com.kmcj.karte.util.XmlDateAdapter;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * カスタムダッシュボードエンティティ
 * @author t.takasaki
 */
@Entity
@Table(name = "tbl_ct_dashboard")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCtDashboard.findById", query = "SELECT m FROM TblCtDashboard m WHERE m.id = :id"),
    @NamedQuery(name = "TblCtDashboard.findAll", query = "SELECT m FROM TblCtDashboard m LEFT OUTER JOIN FETCH m.createUser LEFT OUTER JOIN FETCH m.updateUser order by m.seq"),
    @NamedQuery(name = "TblCtDashboard.findAuthedList", query = "SELECT d FROM TblCtDashboard d join TblCtDashboardAuth a on d.id = a.pk.dashboardId WHERE a.pk.authId = :authId and d.publicFlg = true order by d.seq")
})
public class TblCtDashboard implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public TblCtDashboard() {};
    
    public TblCtDashboard(String id) {
        this.id = id;
    }
    
    @Id
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    
    @NotNull
    @Size(min = 0, max = 100)
    @Column(name = "DASHBOARD_NAME")
    private String name = "";
    
    @NotNull
    @Size(min = 0, max = 255)
    @Column(name = "DESCRIPTION")
    private String description  = "";
    
    @NotNull
    @Column(name = "SEQ")
    private int seq;
    
    @NotNull
    @Column(name = "UPDATE_INTERVAL")
    private int updateInterval;
    
    @NotNull
    @Column(name = "PUBLIC_FLG")
    private boolean publicFlg = false;
    
    @NotNull
    @Column(name = "SHOW_FILTER_FLG")
    private boolean showFilters = false;
    
    @NotNull
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date createDate;
    
    @NotNull
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date updateDate;
    
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUUID;
    
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUUID;
    
    @PrimaryKeyJoinColumn(name = "CREATE_USER_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUserMin createUser;

    @PrimaryKeyJoinColumn(name = "UPDATE_USER_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUserMin updateUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public boolean isPublicFlg() {
        return publicFlg;
    }

    public void setPublicFlg(boolean publicFlg) {
        this.publicFlg = publicFlg;
    }

    public boolean isShowFilters() {
        return showFilters;
    }

    public void setShowFilters(boolean showFilters) {
        this.showFilters = showFilters;
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

    public String getCreateUserUUID() {
        return createUserUUID;
    }

    public void setCreateUserUUID(String createUserUUID) {
        this.createUserUUID = createUserUUID;
    }

    public String getUpdateUserUUID() {
        return updateUserUUID;
    }

    public void setUpdateUserUUID(String updateUserUUID) {
        this.updateUserUUID = updateUserUUID;
    }

    public MstUserMin getCreateUser() {
        return createUser;
    }

    public void setCreateUser(MstUserMin createUser) {
        this.createUser = createUser;
    }

    public MstUserMin getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(MstUserMin updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TblCtDashboard other = (TblCtDashboard) obj;
        return Objects.equals(this.id, other.id);
    }

}
