package com.kmcj.karte.resources.machine.spec;

import com.kmcj.karte.resources.machine.attribute.MstMachineAttribute;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistory;
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
@Table(name = "mst_machine_spec")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMachineSpec.findAll", query = "SELECT m FROM MstMachineSpec m"),
    @NamedQuery(name = "MstMachineSpec.findById", query = "SELECT m FROM MstMachineSpec m WHERE m.id = :id"),
    @NamedQuery(name = "MstMachineSpec.findByMachineSpecHstId", query = "SELECT m FROM MstMachineSpec m WHERE m.mstMachineSpecPK.machineSpecHstId = :machineSpecHstId"),
    @NamedQuery(name = "MstMachineSpec.findByMachineListSpec", query = "SELECT m1 FROM MstMachineSpec m1 LEFT JOIN MstMachineAttribute m ON m.id = m1.mstMachineSpecPK.attrId WHERE m1.mstMachineSpecPK.machineSpecHstId = :machineSpecHstId and m.machineType = :machineType and m.externalFlg = :externalFlg order by m.seq "),
    @NamedQuery(name = "MstMachineSpec.findByMachineSpecHstIdAndAttrId", query = "SELECT m FROM MstMachineSpec m WHERE m.mstMachineSpecPK.machineSpecHstId = :machineSpecHstId AND m.mstMachineSpecPK.attrId = :attrId "),
    // 属性削除する前に、仕様にその属性ＩＤＦＫチェック
    @NamedQuery(name = "MstMachineSpec.findByAttrId", query = "SELECT m FROM MstMachineSpec m WHERE m.mstMachineSpecPK.attrId = :attrId"),
    @NamedQuery(name = "MstMachineSpec.findByAttrValue", query = "SELECT m FROM MstMachineSpec m WHERE m.attrValue = :attrValue"),
    @NamedQuery(name = "MstMachineSpec.findByCreateDate", query = "SELECT m FROM MstMachineSpec m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMachineSpec.findByUpdateDate", query = "SELECT m FROM MstMachineSpec m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMachineSpec.findByCreateUserUuid", query = "SELECT m FROM MstMachineSpec m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMachineSpec.findByUpdateUserUuid", query = "SELECT m FROM MstMachineSpec m WHERE m.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class MstMachineSpec implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstMachineSpecPK mstMachineSpecPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 256)
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
    private MstMachineAttribute mstMachineAttribute;
    @JoinColumn(name = "MACHINE_SPEC_HST_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachineSpecHistory mstMachineSpecHistory;

    public MstMachineSpec() {
    }

    public MstMachineSpec(MstMachineSpecPK mstMachineSpecPK) {
        this.mstMachineSpecPK = mstMachineSpecPK;
    }

    public MstMachineSpec(MstMachineSpecPK mstMachineSpecPK, String id) {
        this.mstMachineSpecPK = mstMachineSpecPK;
        this.id = id;
    }

    public MstMachineSpec(String machineSpecHstId, String attrId) {
        this.mstMachineSpecPK = new MstMachineSpecPK(machineSpecHstId, attrId);
    }

    public MstMachineSpecPK getMstMachineSpecPK() {
        return mstMachineSpecPK;
    }

    public void setMstMachineSpecPK(MstMachineSpecPK mstMachineSpecPK) {
        this.mstMachineSpecPK = mstMachineSpecPK;
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

    public MstMachineAttribute getMstMachineAttribute() {
        return mstMachineAttribute;
    }

    public void setMstMachineAttribute(MstMachineAttribute mstMachineAttribute) {
        this.mstMachineAttribute = mstMachineAttribute;
    }

    public MstMachineSpecHistory getMstMachineSpecHistory() {
        return mstMachineSpecHistory;
    }

    public void setMstMachineSpecHistory(MstMachineSpecHistory mstMachineSpecHistory) {
        this.mstMachineSpecHistory = mstMachineSpecHistory;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mstMachineSpecPK != null ? mstMachineSpecPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMachineSpec)) {
            return false;
        }
        MstMachineSpec other = (MstMachineSpec) object;
        if ((this.mstMachineSpecPK == null && other.mstMachineSpecPK != null) || (this.mstMachineSpecPK != null && !this.mstMachineSpecPK.equals(other.mstMachineSpecPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.MstMachineSpec[ mstMachineSpecPK=" + mstMachineSpecPK + " ]";
    }

}
