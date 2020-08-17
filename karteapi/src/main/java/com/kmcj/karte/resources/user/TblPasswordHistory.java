/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.user;

import com.kmcj.karte.resources.company.MstCompany;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "tbl_password_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblPasswordHistory.findAll", query = "SELECT t FROM TblPasswordHistory t"),
    @NamedQuery(name = "TblPasswordHistory.findByUserUuid", query = "SELECT t FROM TblPasswordHistory t WHERE t.userUuid = :userUuid ORDER BY t.setAt DESC"), //新しい履歴から順番に照合するためのORDER BY句
    @NamedQuery(name = "TblPasswordHistory.findById", query = "SELECT t FROM TblPasswordHistory t WHERE t.id = :id"),
    @NamedQuery(name = "TblPasswordHistory.findBySetAt", query = "SELECT t FROM TblPasswordHistory t WHERE t.setAt = :setAt"),
    @NamedQuery(name = "TblPasswordHistory.findByHashedPassword", query = "SELECT t FROM TblPasswordHistory t WHERE t.hashedPassword = :hashedPassword")}
)
    //@NamedQuery(name = "TblPasswordHistory.findByUserUuid", query = "SELECT t FROM TblPasswordHistory t WHERE t.userUuid = :userUuid")})
@Cacheable(value = false)
public class TblPasswordHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SET_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date setAt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "HASHED_PASSWORD")
    private String hashedPassword;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "USER_UUID")
    private String userUuid;

    public TblPasswordHistory() {
    }

    public TblPasswordHistory(String id) {
        this.id = id;
    }

    public TblPasswordHistory(String id, Date setAt, String hashedPassword, String userUuid) {
        this.id = id;
        this.setAt = setAt;
        this.hashedPassword = hashedPassword;
        this.userUuid = userUuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getSetAt() {
        return setAt;
    }

    public void setSetAt(Date setAt) {
        this.setAt = setAt;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
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
        if (!(object instanceof TblPasswordHistory)) {
            return false;
        }
        TblPasswordHistory other = (TblPasswordHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.user.TblPasswordHistory[ id=" + id + " ]";
    }

}
