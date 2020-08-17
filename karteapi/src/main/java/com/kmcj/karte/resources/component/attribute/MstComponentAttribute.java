/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.attribute;

import com.kmcj.karte.resources.component.spec.MstComponentSpec;
import com.kmcj.karte.resources.filelinkptn.MstFileLinkPtn;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author zds
 */
@Entity
@Table(name = "mst_component_attribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstComponentAttribute.findAll", query = "SELECT m FROM MstComponentAttribute m"),
    @NamedQuery(name = "MstComponentAttribute.findById", query = "SELECT m FROM MstComponentAttribute m WHERE m.id = :id"),
    @NamedQuery(name = "MstComponentAttribute.findByComponentTypeAndAttrCode", query = "SELECT m FROM MstComponentAttribute m WHERE m.mstComponentAttributePK.componentType = :componentType And m.mstComponentAttributePK.attrCode = :attrCode"),
    @NamedQuery(name = "MstComponentAttribute.findByComponentType", query = "SELECT m FROM MstComponentAttribute m WHERE m.mstComponentAttributePK.componentType = :componentType Order By m.seq"),
    @NamedQuery(name = "MstComponentAttribute.findByAttrCode", query = "SELECT m FROM MstComponentAttribute m WHERE m.mstComponentAttributePK.attrCode = :attrCode and m.mstComponentAttributePK.componentType = :componentType"),
    @NamedQuery(name = "MstComponentAttribute.findByAttrName", query = "SELECT m FROM MstComponentAttribute m WHERE m.attrName = :attrName"),
    @NamedQuery(name = "MstComponentAttribute.findByAttrType", query = "SELECT m FROM MstComponentAttribute m WHERE m.attrType = :attrType"),
    @NamedQuery(name = "MstComponentAttribute.findBySeq", query = "SELECT m FROM MstComponentAttribute m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstComponentAttribute.findByFileLinkPtnId", query = "SELECT m FROM MstComponentAttribute m WHERE m.fileLinkPtnId = :fileLinkPtnId"),
    @NamedQuery(name = "MstComponentAttribute.findByCreateDate", query = "SELECT m FROM MstComponentAttribute m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstComponentAttribute.findByUpdateDate", query = "SELECT m FROM MstComponentAttribute m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstComponentAttribute.findByCreateUserUuid", query = "SELECT m FROM MstComponentAttribute m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstComponentAttribute.findByUpdateUserUuid", query = "SELECT m FROM MstComponentAttribute m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstComponentAttribute.deleteByAttrCode", query = "DELETE FROM MstComponentAttribute m WHERE m.mstComponentAttributePK.attrCode = :attrCode and m.mstComponentAttributePK.componentType = :componentType"),
    @NamedQuery(name = "MstComponentAttribute.updateByComponentType", query = "UPDATE MstComponentAttribute m SET "
            + "m.seq = :seq, "
            + "m.attrName = :attrName, "
            + "m.attrType = :attrType,"
            + "m.fileLinkPtnId = :fileLinkPtnId,"
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid  "
            + "WHERE m.mstComponentAttributePK.attrCode = :attrCode "
            + "and m.mstComponentAttributePK.componentType = :componentType"),
    @NamedQuery(name = "MstComponentAttribute.findFkByfileLinkPtnId", query = "SELECT count(m) FROM MstComponentAttribute m JOIN FETCH m.mstFileLinkPtn_CompAttr  WHERE m.fileLinkPtnId = :id"),
    @NamedQuery(name = "MstComponentAttribute.findByMaxSeq", query = "SELECT max(m.seq) FROM MstComponentAttribute m WHERE m.mstComponentAttributePK.componentType = :componentType"),})
@Cacheable(value = false)
public class MstComponentAttribute implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mstComponentAttribute")
    private Collection<MstComponentSpec> mstComponentSpecCollection;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstComponentAttributePK mstComponentAttributePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
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
    @Size(max = 45)
    @Column(name = "FILE_LINK_PTN_ID")
    private String fileLinkPtnId;
    @JoinColumn(name = "FILE_LINK_PTN_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstFileLinkPtn mstFileLinkPtn_CompAttr;

    public MstComponentAttribute() {
    }

    public MstComponentAttribute(MstComponentAttributePK mstComponentAttributePK) {
        this.mstComponentAttributePK = mstComponentAttributePK;
    }

    public MstComponentAttribute(MstComponentAttributePK mstComponentAttributePK, String id, int seq) {
        this.mstComponentAttributePK = mstComponentAttributePK;
        this.id = id;
        this.seq = seq;
    }

    public MstComponentAttribute(int componentType, String attrCode) {
        this.mstComponentAttributePK = new MstComponentAttributePK(componentType, attrCode);
    }

    public MstComponentAttributePK getMstComponentAttributePK() {
        return mstComponentAttributePK;
    }

    public void setMstComponentAttributePK(MstComponentAttributePK mstComponentAttributePK) {
        this.mstComponentAttributePK = mstComponentAttributePK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mstComponentAttributePK != null ? mstComponentAttributePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstComponentAttribute)) {
            return false;
        }
        MstComponentAttribute other = (MstComponentAttribute) object;
        if ((this.mstComponentAttributePK == null && other.mstComponentAttributePK != null) || (this.mstComponentAttributePK != null && !this.mstComponentAttributePK.equals(other.mstComponentAttributePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.attribute.MstComponentAttribute[ mstComponentAttributePK=" + mstComponentAttributePK + " ]";
    }

    /**
     * @return the fileLinkPtnId
     */
    public String getFileLinkPtnId() {
        return fileLinkPtnId;
    }

    /**
     * @param fileLinkPtnId the fileLinkPtnId to set
     */
    public void setFileLinkPtnId(String fileLinkPtnId) {
        this.fileLinkPtnId = fileLinkPtnId;
    }

    /**
     * @return the mstFileLinkPtn_CompAttr
     */
    public MstFileLinkPtn getMstFileLinkPtn_CompAttr() {
        return mstFileLinkPtn_CompAttr;
    }

    /**
     * @param mstFileLinkPtn_CompAttr the mstFileLinkPtn_CompAttr to set
     */
    public void setMstFileLinkPtn_CompAttr(MstFileLinkPtn mstFileLinkPtn_CompAttr) {
        this.mstFileLinkPtn_CompAttr = mstFileLinkPtn_CompAttr;
    }

    @XmlTransient
    public Collection<MstComponentSpec> getMstComponentSpecCollection() {
        return mstComponentSpecCollection;
    }

    public void setMstComponentSpecCollection(Collection<MstComponentSpec> mstComponentSpecCollection) {
        this.mstComponentSpecCollection = mstComponentSpecCollection;
    }
}
