/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.spec;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.attribute.MstComponentAttribute;
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
 * @author admin
 */
@Entity
@Table(name = "mst_component_spec")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstComponentSpec.findAll", query = "SELECT m FROM MstComponentSpec m"),
    @NamedQuery(name = "MstComponentSpec.findById", query = "SELECT m FROM MstComponentSpec m WHERE m.id = :id"),
    @NamedQuery(name = "MstComponentSpec.findByComponentId", query = "SELECT m FROM MstComponentSpec m WHERE m.mstComponentSpecPK.componentId = :componentId"),
    @NamedQuery(name = "MstComponentSpec.findByAttrId", query = "SELECT m FROM MstComponentSpec m WHERE m.mstComponentSpecPK.attrId = :attrId"),
    @NamedQuery(name = "MstComponentSpec.findByAttrIdOnly", query = "SELECT m.mstComponentSpecPK.attrId FROM MstComponentSpec m WHERE m.mstComponentSpecPK.attrId = :attrId"),
    @NamedQuery(name = "MstComponentSpec.findByAttrValue", query = "SELECT m FROM MstComponentSpec m WHERE m.attrValue = :attrValue"),
    @NamedQuery(name = "MstComponentSpec.findByCreateDate", query = "SELECT m FROM MstComponentSpec m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstComponentSpec.findByUpdateDate", query = "SELECT m FROM MstComponentSpec m WHERE m.updateDate = :updateDate"),
    // 部品仕様取得
    @NamedQuery(name = "MstComponentSpec.findByComponentUuid", query = "SELECT m FROM MstComponentSpec m LEFT JOIN MstComponentAttribute a ON m.mstComponentSpecPK.attrId = a.id WHERE m.mstComponentSpecPK.componentId = :componentId And a.mstComponentAttributePK.componentType = :componentType ORDER BY a.seq"),
    
    @NamedQuery(name = "MstComponentSpec.findByCreateUserUuid", query = "SELECT m FROM MstComponentSpec m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstComponentSpec.findByUpdateUserUuid", query = "SELECT m FROM MstComponentSpec m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstComponentSpec.update", query = "UPDATE MstComponentSpec m SET "
            + "m.attrValue = :attrValue,"
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid  "
            + "WHERE m.mstComponentSpecPK.componentId = :componentId "
            + "and m.mstComponentSpecPK.attrId = :attrId "),
    @NamedQuery(name = "MstComponentSpec.updateRegistration", query = "UPDATE MstComponentSpec m SET "
            + "m.attrValue = :attrValue, "
            + "m.updateDate = :updateDate, "
            + "m.updateUserUuid = :updateUserUuid  "
            + "WHERE m.mstComponentSpecPK.componentId = :componentId "
            + "and m.mstComponentSpecPK.attrId = :attrId "),
    @NamedQuery(name = "MstComponentSpec.deleteByComponentIdAndAttrId", query = "DELETE FROM MstComponentSpec m "
            + "WHERE m.mstComponentSpecPK.componentId = :componentId "
            + "and m.mstComponentSpecPK.attrId = :attrId"),
    @NamedQuery(name = "MstComponentSpec.deleteByComponentId", query = "DELETE FROM MstComponentSpec m "
            + "WHERE m.mstComponentSpecPK.componentId = :componentId "),
    @NamedQuery(name = "MstComponentSpec.findByComponentIdAndAttrId", query = "SELECT m.mstComponentSpecPK.componentId"
            + ",m.mstComponentSpecPK.attrId "
            + "FROM MstComponentSpec m "
            + "WHERE m.mstComponentSpecPK.componentId = :componentId "
            + "and m.mstComponentSpecPK.attrId = :attrId "),
    @NamedQuery(name = "MstComponentSpec.findByComponentIdAndAttrId1", query = "SELECT m "
            + "FROM MstComponentSpec m "
            + "WHERE m.mstComponentSpecPK.componentId = :componentId "
            + "and m.mstComponentSpecPK.attrId = :attrId "),
//    @NamedQuery(name = "MstComponentSpec.findByComponentType", query = "SELECT m "
//            + "FROM MstComponentSpec m "
//            + "LEFT JOIN FETCH m.mstComponentAttribute a"
//            + "LEFT JOIN FETCH m.mstComponentAttribute.mstFileLinkPtn_CompAttr c"
//            + "WHERE m.mstComponentSpecPK.attrId = m.mstComponentAttribute.id "
//            + "and m.mstComponentAttribute.mstComponentAttributePK.componentType = :componentType ")

})
@Cacheable(value = false)
public class MstComponentSpec implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstComponentSpecPK mstComponentSpecPK;
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
    //@JoinColumn(name = "ATTR_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @PrimaryKeyJoinColumn(name = "ATTR_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private MstComponentAttribute mstComponentAttribute;
//    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private MstComponent mstComponent;

    public MstComponentSpec() {
    }

    public MstComponentSpec(MstComponentSpecPK mstComponentSpecPK) {
        this.mstComponentSpecPK = mstComponentSpecPK;
    }

    public MstComponentSpec(MstComponentSpecPK mstComponentSpecPK, String id) {
        this.mstComponentSpecPK = mstComponentSpecPK;
        this.id = id;
    }

    public MstComponentSpec(String componentId, String attrId) {
        this.mstComponentSpecPK = new MstComponentSpecPK(componentId, attrId);
    }

    public MstComponentSpecPK getMstComponentSpecPK() {
        return mstComponentSpecPK;
    }

    public void setMstComponentSpecPK(MstComponentSpecPK mstComponentSpecPK) {
        this.mstComponentSpecPK = mstComponentSpecPK;
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

    public MstComponentAttribute getMstComponentAttribute() {
        return mstComponentAttribute;
    }

    public void setMstComponentAttribute(MstComponentAttribute mstComponentAttribute) {
        this.mstComponentAttribute = mstComponentAttribute;
    }

    public MstComponent getMstComponent() {
        return mstComponent;
    }

    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mstComponentSpecPK != null ? mstComponentSpecPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstComponentSpec)) {
            return false;
        }
        MstComponentSpec other = (MstComponentSpec) object;
        if ((this.mstComponentSpecPK == null && other.mstComponentSpecPK != null) || (this.mstComponentSpecPK != null && !this.mstComponentSpecPK.equals(other.mstComponentSpecPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.spec.MstComponentSpec[ mstComponentSpecPK=" + mstComponentSpecPK + " ]";
    }

}
