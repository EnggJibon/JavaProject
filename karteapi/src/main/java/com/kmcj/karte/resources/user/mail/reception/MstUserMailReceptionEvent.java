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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_user_mail_reception_event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstUserMailReceptionEvent.findAll", query = "SELECT m FROM MstUserMailReceptionEvent m ORDER BY m.seq "),
    @NamedQuery(name = "MstUserMailReceptionEvent.findByUuid", query = "SELECT m FROM MstUserMailReceptionEvent m WHERE m.uuid = :uuid"),
    @NamedQuery(name = "MstUserMailReceptionEvent.findByEventKey", query = "SELECT m FROM MstUserMailReceptionEvent m WHERE m.eventKey = :eventKey"),
    @NamedQuery(name = "MstUserMailReceptionEvent.findByDescriptionKey", query = "SELECT m FROM MstUserMailReceptionEvent m WHERE m.descriptionKey = :descriptionKey"),
    @NamedQuery(name = "MstUserMailReceptionEvent.findBySeq", query = "SELECT m FROM MstUserMailReceptionEvent m WHERE m.seq = :seq")
})
@Cacheable(value = false)
public class MstUserMailReceptionEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
    @Size(max = 100)
    @Column(name = "EVENT_DICT_KEY")
    private String eventKey;
    @Size(max = 100)
    @Column(name = "DESCRIPTION_DICT_KEY")
    private String descriptionKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
    @Column(name = "REF_DEPARTMENT")
    private int refDepartment;
    
    @Transient
    private int receptionFlg;

    public MstUserMailReceptionEvent() {
    }

    public MstUserMailReceptionEvent(String uuid) {
        this.uuid = uuid;
    }

    public MstUserMailReceptionEvent(String uuid, int seq) {
        this.uuid = uuid;
        this.seq = seq;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstUserMailReceptionEvent)) {
            return false;
        }
        MstUserMailReceptionEvent other = (MstUserMailReceptionEvent) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.user.mail.reception.MstUserMailReceptionEvent[ uuid=" + uuid + " ]";
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

    /**
     * @return the refDepartment
     */
    public int getRefDepartment() {
        return refDepartment;
    }

    /**
     * @param refDepartment the refDepartment to set
     */
    public void setRefDepartment(int refDepartment) {
        this.refDepartment = refDepartment;
    }
    
}
