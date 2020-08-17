package com.kmcj.karte.resources.machine.proccond.spec;

import com.kmcj.karte.resources.machine.proccond.MstMachineProcCond;
import com.kmcj.karte.resources.machine.proccond.attribute.MstMachineProcCondAttribute;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "mst_machine_proc_cond_spec")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMachineProcCondSpec.findAll", query = "SELECT m FROM MstMachineProcCondSpec m"),
    @NamedQuery(name = "MstMachineProcCondSpec.findById", query = "SELECT m FROM MstMachineProcCondSpec m WHERE m.id = :id"),
    @NamedQuery(name = "MstMachineProcCondSpec.findByMachineProcCondId", query = "SELECT m FROM MstMachineProcCondSpec m WHERE m.mstMachineProcCondSpecPK.machineProcCondId = :machineProcCondId"),
    @NamedQuery(name = "MstMachineProcCondSpec.findByAttrId", query = "SELECT m FROM MstMachineProcCondSpec m WHERE m.mstMachineProcCondSpecPK.attrId = :attrId"),
    @NamedQuery(name = "MstMachineProcCondSpec.findByAttrValue", query = "SELECT m FROM MstMachineProcCondSpec m WHERE m.attrValue = :attrValue"),
    @NamedQuery(name = "MstMachineProcCondSpec.findByCreateDate", query = "SELECT m FROM MstMachineProcCondSpec m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMachineProcCondSpec.findByUpdateDate", query = "SELECT m FROM MstMachineProcCondSpec m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMachineProcCondSpec.findByCreateUserUuid", query = "SELECT m FROM MstMachineProcCondSpec m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMachineProcCondSpec.findByUpdateUserUuid", query = "SELECT m FROM MstMachineProcCondSpec m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMachineProcCondSpec.findByMachineProcCondIdAndAttrId", query = "SELECT m FROM MstMachineProcCondSpec m WHERE m.mstMachineProcCondSpecPK.machineProcCondId = :machineProcCondId and m.mstMachineProcCondSpecPK.attrId = :attrId ")
})
@Cacheable(value = false)
public class MstMachineProcCondSpec implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstMachineProcCondSpecPK mstMachineProcCondSpecPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 100)
    @Column(name = "ATTR_VALUE")
    private String attrValue;
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
    @JoinColumn(name = "ATTR_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachineProcCondAttribute mstMachineProcCondAttribute;
    @JoinColumn(name = "MACHINE_PROC_COND_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachineProcCond mstMachineProcCond;

    public MstMachineProcCondSpec() {
    }

    public MstMachineProcCondSpec(MstMachineProcCondSpecPK mstMachineProcCondSpecPK) {
        this.mstMachineProcCondSpecPK = mstMachineProcCondSpecPK;
    }

    public MstMachineProcCondSpec(MstMachineProcCondSpecPK mstMachineProcCondSpecPK, String id) {
        this.mstMachineProcCondSpecPK = mstMachineProcCondSpecPK;
        this.id = id;
    }

    public MstMachineProcCondSpec(String machineProcCondId, String attrId) {
        this.mstMachineProcCondSpecPK = new MstMachineProcCondSpecPK(machineProcCondId, attrId);
    }

    public MstMachineProcCondSpecPK getMstMachineProcCondSpecPK() {
        return mstMachineProcCondSpecPK;
    }

    public void setMstMachineProcCondSpecPK(MstMachineProcCondSpecPK mstMachineProcCondSpecPK) {
        this.mstMachineProcCondSpecPK = mstMachineProcCondSpecPK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
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

    public MstMachineProcCondAttribute getMstMachineProcCondAttribute() {
        return mstMachineProcCondAttribute;
    }

    public void setMstMachineProcCondAttribute(MstMachineProcCondAttribute mstMachineProcCondAttribute) {
        this.mstMachineProcCondAttribute = mstMachineProcCondAttribute;
    }

    public MstMachineProcCond getMstMachineProcCond() {
        return mstMachineProcCond;
    }

    public void setMstMachineProcCond(MstMachineProcCond mstMachineProcCond) {
        this.mstMachineProcCond = mstMachineProcCond;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mstMachineProcCondSpecPK != null ? mstMachineProcCondSpecPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpec)) {
            return false;
        }
        com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpec other = (com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpec) object;
        if ((this.mstMachineProcCondSpecPK == null && other.mstMachineProcCondSpecPK != null) || (this.mstMachineProcCondSpecPK != null && !this.mstMachineProcCondSpecPK.equals(other.mstMachineProcCondSpecPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpec[ mstMachineProcCondSpecPK=" + mstMachineProcCondSpecPK + " ]";
    }

}
