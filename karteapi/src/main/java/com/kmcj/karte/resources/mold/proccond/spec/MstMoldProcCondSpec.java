/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.proccond.spec;

import com.kmcj.karte.resources.mold.proccond.MstMoldProcCond;
import com.kmcj.karte.resources.mold.proccond.attribute.MstMoldProcCondAttribute;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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

/**
 *
 * @author CH
 */
@Entity
@Table(name = "mst_mold_proc_cond_spec")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMoldProcCondSpec.findAll", query = "SELECT m FROM MstMoldProcCondSpec m"),
    @NamedQuery(name = "MstMoldProcCondSpec.findById", query = "SELECT m FROM MstMoldProcCondSpec m WHERE m.id = :id"),
    @NamedQuery(name = "MstMoldProcCondSpec.findByMoldProcCondId", query = "SELECT m FROM MstMoldProcCondSpec m WHERE m.mstMoldProcCondSpecPK.moldProcCondId = :moldProcCondId"),
    @NamedQuery(name = "MstMoldProcCondSpec.findByAttrId", query = "SELECT m FROM MstMoldProcCondSpec m WHERE m.mstMoldProcCondSpecPK.attrId = :attrId"),
    @NamedQuery(name = "MstMoldProcCondSpec.findByAttrValue", query = "SELECT m FROM MstMoldProcCondSpec m WHERE m.attrValue = :attrValue"),
    @NamedQuery(name = "MstMoldProcCondSpec.findByCreateDate", query = "SELECT m FROM MstMoldProcCondSpec m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMoldProcCondSpec.findByUpdateDate", query = "SELECT m FROM MstMoldProcCondSpec m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMoldProcCondSpec.findByCreateUserUuid", query = "SELECT m FROM MstMoldProcCondSpec m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMoldProcCondSpec.findByUpdateUserUuid", query = "SELECT m FROM MstMoldProcCondSpec m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMoldProcCondSpec.findFkByAttrId", query = "SELECT m FROM MstMoldProcCondSpec m WHERE m.mstMoldProcCondSpecPK.attrId = :attrId"),
    @NamedQuery(name = "MstMoldProcCondSpec.findByMoldProcCondIdAndAttrId", query = "SELECT m FROM MstMoldProcCondSpec m "
            + "WHERE m.mstMoldProcCondSpecPK.moldProcCondId = :moldProcCondId AND m.mstMoldProcCondSpecPK.attrId = :attrId"),
    @NamedQuery(name = "MstMoldProcCondSpec.updateMoldProcCondSpec", query = "UPDATE MstMoldProcCondSpec m SET m.attrValue = :attrValue,m.updateDate = :updateDate,m.updateUserUuid = :updateUserUuid WHERE m.mstMoldProcCondSpecPK.attrId = :attrId and m.mstMoldProcCondSpecPK.moldProcCondId = :moldProcCondId")
})
@Cacheable(value = false)
public class MstMoldProcCondSpec implements Serializable {

    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @EmbeddedId
    private MstMoldProcCondSpecPK mstMoldProcCondSpecPK;
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
//    @Size(min = 1, max = 45)
//    @Column(name = "MOLD_PROC_COND_ID")
//    private String moldProcCondId;
    @PrimaryKeyJoinColumn(name = "ATTR_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private MstMoldProcCondAttribute mstMoldProcCondAttribute;
//    @JoinColumn(name = "MOLD_PROC_COND_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @PrimaryKeyJoinColumn(name = "MOLD_PROC_COND_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private MstMoldProcCond mstMoldProcCond;

    public MstMoldProcCondSpec() {
    }

    public MstMoldProcCondSpec(MstMoldProcCondSpecPK mstMoldProcCondSpecPK) {
        this.mstMoldProcCondSpecPK = mstMoldProcCondSpecPK;
    }

    public MstMoldProcCondSpec(MstMoldProcCondSpecPK mstMoldProcCondSpecPK, String id) {
        this.mstMoldProcCondSpecPK = mstMoldProcCondSpecPK;
        this.id = id;
    }

    public MstMoldProcCondSpec(String moldProcCondId, String attrId) {
        this.mstMoldProcCondSpecPK = new MstMoldProcCondSpecPK(moldProcCondId, attrId);
    }

    public MstMoldProcCondSpecPK getMstMoldProcCondSpecPK() {
        return mstMoldProcCondSpecPK;
    }

    public void setMstMoldProcCondSpecPK(MstMoldProcCondSpecPK mstMoldProcCondSpecPK) {
        this.mstMoldProcCondSpecPK = mstMoldProcCondSpecPK;
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

    public MstMoldProcCondAttribute getMstMoldProcCondAttribute() {
        return mstMoldProcCondAttribute;
    }

    public void setMstMoldProcCondAttribute(MstMoldProcCondAttribute mstMoldProcCondAttribute) {
        this.mstMoldProcCondAttribute = mstMoldProcCondAttribute;
    }

    public MstMoldProcCond getMstMoldProcCond() {
        return mstMoldProcCond;
    }

    public void setMstMoldProcCond(MstMoldProcCond mstMoldProcCond) {
        this.mstMoldProcCond = mstMoldProcCond;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getMstMoldProcCondSpecPK() != null ? getMstMoldProcCondSpecPK().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMoldProcCondSpec)) {
            return false;
        }
        MstMoldProcCondSpec other = (MstMoldProcCondSpec) object;
        if ((this.getMstMoldProcCondSpecPK() == null && other.getMstMoldProcCondSpecPK() != null) || (this.getMstMoldProcCondSpecPK() != null && !this.mstMoldProcCondSpecPK.equals(other.mstMoldProcCondSpecPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpec[ mstMoldProcCondSpecPK=" + getMstMoldProcCondSpecPK() + " ]";
    }

    /**
     * @return the moldProcCondId
     */
//    public String getMoldProcCondId() {
//        return moldProcCondId;
//    }
//
//    /**
//     * @param moldProcCondId the moldProcCondId to set
//     */
//    public void setMoldProcCondId(String moldProcCondId) {
//        this.moldProcCondId = moldProcCondId;
//    }

}
