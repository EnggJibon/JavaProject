/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.user.mail.reception;

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
 * @author admin
 */
@Entity
@Table(name = "mst_user_mail_reception")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstUserMailReception.findAll", query = "SELECT m FROM MstUserMailReception m"),
    @NamedQuery(name = "MstUserMailReception.deleteByUserUuid", query = "DELETE FROM MstUserMailReception m WHERE m.userUuid = :userUuid"),
    @NamedQuery(name = "MstUserMailReception.findByUserEventUuid", query = "SELECT m FROM MstUserMailReception m WHERE m.userUuid = :userUuid AND m.eventUuid = :eventUuid"),
    @NamedQuery(name = "MstUserMailReception.findByUserUuid", query = "SELECT m FROM MstUserMailReception m WHERE m.userUuid = :userUuid")
})
@Cacheable(value = false)
public class MstUserMailReception implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "USER_UUID")
    private String userUuid;
    
    @Column(name = "EVENT_UUID")
    private String eventUuid;
    
    @Column(name = "RECEPTION_FLG")
    private int receptionFlg;

    public MstUserMailReception() {
    }

    public MstUserMailReception(String userUuid) {
        this.userUuid = userUuid;
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
        hash += (userUuid != null ? userUuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstUserMailReception)) {
            return false;
        }
        MstUserMailReception other = (MstUserMailReception) object;
        if ((this.userUuid == null && other.userUuid != null) || (this.userUuid != null && !this.userUuid.equals(other.userUuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.user.mail.reception.MstUserMailReception[ userUuid=" + userUuid + " ]";
    }

    /**
     * @return the eventUuid
     */
    public String getEventUuid() {
        return eventUuid;
    }

    /**
     * @param eventUuid the eventUuid to set
     */
    public void setEventUuid(String eventUuid) {
        this.eventUuid = eventUuid;
    }

    /**
     * @return the receptionFlg
     */
    public int getReceptionFlg() {
        return receptionFlg;
    }

    /**
     * @param receptionFlg the receptionFlg to set
     */
    public void setReceptionFlg(int receptionFlg) {
        this.receptionFlg = receptionFlg;
    }
    
}
