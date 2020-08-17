/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.attribute;

import com.kmcj.karte.resources.filelinkptn.MstFileLinkPtn;
import com.kmcj.karte.resources.mold.spec.MstMoldSpec;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author admin
 */
@Entity
@Table(name = "mst_mold_attribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMoldAttribute.findAll", query = "SELECT m FROM MstMoldAttribute m"),
    @NamedQuery(name = "MstMoldAttribute.findById", query = "SELECT m FROM MstMoldAttribute m WHERE m.id = :id"),
    @NamedQuery(name = "MstMoldAttribute.findByMoldType", query = "SELECT m FROM MstMoldAttribute m WHERE m.moldType = :moldType Order By m.seq"),
    @NamedQuery(name = "MstMoldAttribute.findByMoldTypeAndAttrCode", query = "SELECT m FROM MstMoldAttribute m WHERE m.moldType = :moldType And m.attrCode = :attrCode"),
    @NamedQuery(name = "MstMoldAttribute.findByAttrCode", query = "SELECT m FROM MstMoldAttribute m WHERE m.attrCode = :attrCode And m.moldType = :moldType And m.externalFlg = :externalFlg "),
    //データがattrCodeOnly
    @NamedQuery(name = "MstMoldAttribute.findByAttrCodes", query = "SELECT m FROM MstMoldAttribute m WHERE m.attrCode = :attrCode And m.externalFlg = :externalFlg"),
    @NamedQuery(name = "MstMoldAttribute.findByAttrName", query = "SELECT m FROM MstMoldAttribute m WHERE m.attrName = :attrName"),
    @NamedQuery(name = "MstMoldAttribute.findByAttrType", query = "SELECT m FROM MstMoldAttribute m WHERE m.attrType = :attrType"),
    @NamedQuery(name = "MstMoldAttribute.findBySeq", query = "SELECT m FROM MstMoldAttribute m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstMoldAttribute.findByCreateDate", query = "SELECT m FROM MstMoldAttribute m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMoldAttribute.findByUpdateDate", query = "SELECT m FROM MstMoldAttribute m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMoldAttribute.findByCreateUserUuid", query = "SELECT m FROM MstMoldAttribute m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMoldAttribute.findByUpdateUserUuid", query = "SELECT m FROM MstMoldAttribute m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMoldAttribute.findByFileLinkPtnId", query = "SELECT m FROM MstMoldAttribute m WHERE m.fileLinkPtnId = :fileLinkPtnId"),
    @NamedQuery(name = "MstMoldAttribute.findFkByfileLinkPtnId", query = "SELECT count(m) FROM MstMoldAttribute m JOIN FETCH m.mstFileLinkPtn_MoldAttr  WHERE m.fileLinkPtnId = :id"),
    @NamedQuery(name = "MstMoldAttribute.deleteByattrCode", query = "DELETE FROM MstMoldAttribute m WHERE m.id = :id "),
    @NamedQuery(name = "MstMoldAttribute.findByExternalFlg", query = "SELECT m FROM MstMoldAttribute m WHERE m.externalFlg = :externalFlg"),
    @NamedQuery(name = "MstMoldAttribute.updateBymoldType", query = "UPDATE MstMoldAttribute m SET "
            + "m.seq = :seq,"
            + "m.attrName = :attrName,"
            + "m.attrType = :attrType,"
            + "m.fileLinkPtnId = :fileLinkPtnId,"
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid "
            + "WHERE m.id = :id AND m.externalFlg = :externalFlg"),})
@Cacheable(value = false)
public class MstMoldAttribute implements Serializable {

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mstMoldAttribute")
    private Collection<MstMoldSpec> mstMoldSpecCollection;

    private static long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MOLD_TYPE")
    private int moldType;
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
    @Size(max = 45)
    @Column(name = "FILE_LINK_PTN_ID")
    private String fileLinkPtnId;
    @JoinColumn(name = "FILE_LINK_PTN_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstFileLinkPtn mstFileLinkPtn_MoldAttr;

    public MstMoldAttribute() {
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
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMoldAttribute)) {
            return false;
        }
        MstMoldAttribute other = (MstMoldAttribute) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.MstMoldAttribute[ id=" + getId() + " ]";
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
     * @return the mstFileLinkPtn_MoldAttr
     */
    public MstFileLinkPtn getMstFileLinkPtn_MoldAttr() {
        return mstFileLinkPtn_MoldAttr;
    }

    /**
     * @param mstFileLinkPtn_MoldAttr the mstFileLinkPtn_MoldAttr to set
     */
    public void setMstFileLinkPtn_MoldAttr(MstFileLinkPtn mstFileLinkPtn_MoldAttr) {
        this.mstFileLinkPtn_MoldAttr = mstFileLinkPtn_MoldAttr;
    }

    @XmlTransient
    public Collection<MstMoldSpec> getMstMoldSpecCollection() {
        return mstMoldSpecCollection;
    }

    public void setMstMoldSpecCollection(Collection<MstMoldSpec> mstMoldSpecCollection) {
        this.mstMoldSpecCollection = mstMoldSpecCollection;
    }

    /**
     * @return the moldType
     */
    public int getMoldType() {
        return moldType;
    }

    /**
     * @param moldType the moldType to set
     */
    public void setMoldType(int moldType) {
        this.moldType = moldType;
    }

    /**
     * @return the attrCode
     */
    public String getAttrCode() {
        return attrCode;
    }

    /**
     * @param attrCode the attrCode to set
     */
    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    /**
     * @return the externalFlg
     */
    public Integer getExternalFlg() {
        return externalFlg;
    }

    /**
     * @param externalFlg the externalFlg to set
     */
    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
    }

}
