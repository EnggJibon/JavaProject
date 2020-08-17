package com.kmcj.karte.resources.machine.proccond.attribute;

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
@Table(name = "mst_machine_proc_cond_attribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMachineProcCondAttribute.findAll", query = "SELECT m FROM MstMachineProcCondAttribute m"),
    @NamedQuery(name = "MstMachineProcCondAttribute.findById", query = "SELECT m FROM MstMachineProcCondAttribute m WHERE m.id = :id"),
    @NamedQuery(name = "MstMachineProcCondAttribute.findByMachineType", query = "SELECT m FROM MstMachineProcCondAttribute m WHERE m.machineType = :machineType"),
    @NamedQuery(name = "MstMachineProcCondAttribute.findByAttrCode", query = "SELECT m FROM MstMachineProcCondAttribute m WHERE m.attrCode = :attrCode"),
    @NamedQuery(name = "MstMachineProcCondAttribute.findByAttrCodeAndMachineType", query = "SELECT m FROM MstMachineProcCondAttribute m WHERE m.externalFlg = :externalFlg AND m.attrCode = :attrCode AND m.machineType = :machineType "),
    @NamedQuery(name = "MstMachineProcCondAttribute.findByAttrName", query = "SELECT m FROM MstMachineProcCondAttribute m WHERE m.attrName = :attrName"),
    @NamedQuery(name = "MstMachineProcCondAttribute.findByAttrType", query = "SELECT m FROM MstMachineProcCondAttribute m WHERE m.attrType = :attrType"),
    @NamedQuery(name = "MstMachineProcCondAttribute.findBySeq", query = "SELECT m FROM MstMachineProcCondAttribute m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstMachineProcCondAttribute.findByCreateDate", query = "SELECT m FROM MstMachineProcCondAttribute m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMachineProcCondAttribute.findByUpdateDate", query = "SELECT m FROM MstMachineProcCondAttribute m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMachineProcCondAttribute.findByCreateUserUuid", query = "SELECT m FROM MstMachineProcCondAttribute m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMachineProcCondAttribute.findByUpdateUserUuid", query = "SELECT m FROM MstMachineProcCondAttribute m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMachineProcCondAttribute.findByExternalFlg", query = "SELECT m FROM MstMachineProcCondAttribute m WHERE m.externalFlg = :externalFlg"),
    @NamedQuery(name = "MstMachineProcCondAttribute.deleteById", query = "DELETE FROM MstMachineProcCondAttribute m WHERE m.id = :id"),
    @NamedQuery(name = "MstMachineProcCondAttribute.updateMstMachineProcCondAttributeByQuery", query = "UPDATE MstMachineProcCondAttribute m SET "
            + "m.seq = :seq,"
            + "m.attrName = :attrName, "
            + "m.attrType = :attrType, "
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid  "
            + "WHERE  m.id = :id "
            + "and m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstMachineProcCondAttribute.findByMachineTypeAndAttrCode", query = "SELECT m FROM MstMachineProcCondAttribute m "
            + "WHERE m.machineType = :machineType "
            + "and m.attrCode = :attrCode "
            + "and m.externalFlg = :externalFlg")
})
@Cacheable(value = false)
public class MstMachineProcCondAttribute implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MACHINE_TYPE")
    private int machineType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ATTR_CODE")
    private String attrCode;
    @Size(max = 45)
    @Column(name = "ATTR_NAME")
    private String attrName;
    @Column(name = "ATTR_TYPE")
    private Integer attrType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
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
    @Column(name = "EXTERNAL_FLG")
    private Integer externalFlg;

    public MstMachineProcCondAttribute() {
    }

    public MstMachineProcCondAttribute(String id) {
        this.id = id;
    }

    public MstMachineProcCondAttribute(String id, int machineType, String attrCode, int seq) {
        this.id = id;
        this.machineType = machineType;
        this.attrCode = attrCode;
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMachineType() {
        return machineType;
    }

    public void setMachineType(int machineType) {
        this.machineType = machineType;
    }

    public String getAttrCode() {
        return attrCode;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public Integer getAttrType() {
        return attrType;
    }

    public void setAttrType(Integer attrType) {
        this.attrType = attrType;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
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

    public Integer getExternalFlg() {
        return externalFlg;
    }

    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
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
        if (!(object instanceof MstMachineProcCondAttribute)) {
            return false;
        }
        MstMachineProcCondAttribute other = (MstMachineProcCondAttribute) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.MstMachineProcCondAttribute[ id=" + id + " ]";
    }

}
