/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.externalmold.choice;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author admin
 */
@Entity
@Table(name = "ext_mst_mold_inspection_choice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExtMstMoldInspectionChoice.findAll", query = "SELECT e FROM ExtMstMoldInspectionChoice e"),
    @NamedQuery(name = "ExtMstMoldInspectionChoice.findById", query = "SELECT e FROM ExtMstMoldInspectionChoice e WHERE e.id = :id"),
    @NamedQuery(name = "ExtMstMoldInspectionChoice.findByInspectionItemId", query = "SELECT e FROM ExtMstMoldInspectionChoice e WHERE e.inspectionItemId = :inspectionItemId"),
    @NamedQuery(name = "ExtMstMoldInspectionChoice.findBySeq", query = "SELECT e FROM ExtMstMoldInspectionChoice e WHERE e.seq = :seq"),
    @NamedQuery(name = "ExtMstMoldInspectionChoice.findByChoice", query = "SELECT e FROM ExtMstMoldInspectionChoice e WHERE e.choice = :choice"),
    @NamedQuery(name = "ExtMstMoldInspectionChoice.findByCreateDate", query = "SELECT e FROM ExtMstMoldInspectionChoice e WHERE e.createDate = :createDate"),
    @NamedQuery(name = "ExtMstMoldInspectionChoice.findByUpdateDate", query = "SELECT e FROM ExtMstMoldInspectionChoice e WHERE e.updateDate = :updateDate"),
    @NamedQuery(name = "ExtMstMoldInspectionChoice.findByCreateUserUuid", query = "SELECT e FROM ExtMstMoldInspectionChoice e WHERE e.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "ExtMstMoldInspectionChoice.findByUpdateUserUuid", query = "SELECT e FROM ExtMstMoldInspectionChoice e WHERE e.updateUserUuid = :updateUserUuid")
})
@Cacheable(value = false)
public class ExtMstMoldInspectionChoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "INSPECTION_ITEM_ID")
    private String inspectionItemId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
    @Size(max = 100)
    @Column(name = "CHOICE")
    private String choice;
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

    public ExtMstMoldInspectionChoice() {
    }

    public ExtMstMoldInspectionChoice(String id) {
        this.id = id;
    }

    public ExtMstMoldInspectionChoice(String id, String inspectionItemId, int seq) {
        this.id = id;
        this.inspectionItemId = inspectionItemId;
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInspectionItemId() {
        return inspectionItemId;
    }

    public void setInspectionItemId(String inspectionItemId) {
        this.inspectionItemId = inspectionItemId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
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
        if (!(object instanceof ExtMstMoldInspectionChoice)) {
            return false;
        }
        ExtMstMoldInspectionChoice other = (ExtMstMoldInspectionChoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.ExtMstMoldInspectionChoice[ id=" + id + " ]";
    }
    
}
