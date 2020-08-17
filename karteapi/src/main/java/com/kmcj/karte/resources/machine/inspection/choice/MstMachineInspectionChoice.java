/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.inspection.choice;

import com.kmcj.karte.resources.machine.inspection.item.MstMachineInspectionItem;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * @author admin
 */
@Entity
@Table(name = "mst_machine_inspection_choice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMachineInspectionChoice.findAll", query = "SELECT m FROM MstMachineInspectionChoice m"),
    @NamedQuery(name = "MstMachineInspectionChoice.findById", query = "SELECT m FROM MstMachineInspectionChoice m WHERE m.id = :id"),
    @NamedQuery(name = "MstMachineInspectionChoice.findBySeq", query = "SELECT m FROM MstMachineInspectionChoice m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstMachineInspectionChoice.findByChoice", query = "SELECT m FROM MstMachineInspectionChoice m WHERE m.choice = :choice"),
    @NamedQuery(name = "MstMachineInspectionChoice.findByCreateDate", query = "SELECT m FROM MstMachineInspectionChoice m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMachineInspectionChoice.findByUpdateDate", query = "SELECT m FROM MstMachineInspectionChoice m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMachineInspectionChoice.findByCreateUserUuid", query = "SELECT m FROM MstMachineInspectionChoice m WHERE m.createUserUuid = :createUserUuid"),
    
    @NamedQuery(name = "MstMachineInspectionChoice.findByInspectionItemId", query = "SELECT m FROM MstMachineInspectionChoice m WHERE m.inspectionItemId = :inspectionItemId order by m.seq "),
    @NamedQuery(name = "MstMachineInspectionChoice.deleteByInspectionItemId", query = "DELETE FROM MstMachineInspectionChoice c WHERE c.inspectionItemId = :inspectionItemId "),
    
    @NamedQuery(name = "MstMachineInspectionChoice.findByUpdateUserUuid", query = "SELECT m FROM MstMachineInspectionChoice m WHERE m.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class MstMachineInspectionChoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
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
    @JoinColumn(name = "INSPECTION_ITEM_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachineInspectionItem mstMachineInspectionItem;
    @Column(name = "INSPECTION_ITEM_ID")
    private String inspectionItemId;

    public MstMachineInspectionChoice() {
    }

    public MstMachineInspectionChoice(String id) {
        this.id = id;
    }

    public MstMachineInspectionChoice(String id, int seq) {
        this.id = id;
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public MstMachineInspectionItem getMstMachineInspectionItem() {
        return mstMachineInspectionItem;
    }

    public void setMstMachineInspectionItem(MstMachineInspectionItem mstMachineInspectionItem) {
        this.mstMachineInspectionItem = mstMachineInspectionItem;
    }

    public void setInspectionItemId(String inspectionItemId) {
        this.inspectionItemId = inspectionItemId;
    }

    public String getInspectionItemId() {
        return inspectionItemId;
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
        if (!(object instanceof MstMachineInspectionChoice)) {
            return false;
        }
        MstMachineInspectionChoice other = (MstMachineInspectionChoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.MstMachineInspectionChoice[ id=" + id + " ]";
    }

}
