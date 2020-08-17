package com.kmcj.karte.batch.externalmachine.choice;

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
@Table(name = "ext_mst_machine_inspection_choice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExtMstMachineInspectionChoice.findAll", query = "SELECT e FROM ExtMstMachineInspectionChoice e"),
    @NamedQuery(name = "ExtMstMachineInspectionChoice.findById", query = "SELECT e FROM ExtMstMachineInspectionChoice e WHERE e.id = :id"),
    @NamedQuery(name = "ExtMstMachineInspectionChoice.findByInspectionItemId", query = "SELECT e FROM ExtMstMachineInspectionChoice e WHERE e.inspectionItemId = :inspectionItemId"),
    @NamedQuery(name = "ExtMstMachineInspectionChoice.findBySeq", query = "SELECT e FROM ExtMstMachineInspectionChoice e WHERE e.seq = :seq"),
    @NamedQuery(name = "ExtMstMachineInspectionChoice.findByChoice", query = "SELECT e FROM ExtMstMachineInspectionChoice e WHERE e.choice = :choice"),
    @NamedQuery(name = "ExtMstMachineInspectionChoice.findByCreateDate", query = "SELECT e FROM ExtMstMachineInspectionChoice e WHERE e.createDate = :createDate"),
    @NamedQuery(name = "ExtMstMachineInspectionChoice.findByUpdateDate", query = "SELECT e FROM ExtMstMachineInspectionChoice e WHERE e.updateDate = :updateDate"),
    @NamedQuery(name = "ExtMstMachineInspectionChoice.findByCreateUserUuid", query = "SELECT e FROM ExtMstMachineInspectionChoice e WHERE e.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "ExtMstMachineInspectionChoice.findByUpdateUserUuid", query = "SELECT e FROM ExtMstMachineInspectionChoice e WHERE e.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class ExtMstMachineInspectionChoice implements Serializable {

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

    public ExtMstMachineInspectionChoice() {
    }

    public ExtMstMachineInspectionChoice(String id) {
        this.id = id;
    }

    public ExtMstMachineInspectionChoice(String id, String inspectionItemId, int seq) {
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
        if (!(object instanceof ExtMstMachineInspectionChoice)) {
            return false;
        }
        ExtMstMachineInspectionChoice other = (ExtMstMachineInspectionChoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.batch.externalmachine.choice.ExtMstMachineInspectionChoice[ id=" + id + " ]";
    }
    
}
