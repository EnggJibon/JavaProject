/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.file;

import com.kmcj.karte.util.XmlDateAdapter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Apeng
 */
@Entity
@Table(name = "mst_component_inspect_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstComponentInspectType.findAll", query = "SELECT m FROM MstComponentInspectType m"),
    @NamedQuery(name = "MstComponentInspectType.findById", query = "SELECT m FROM MstComponentInspectType m WHERE m.id = :id"),
    @NamedQuery(name = "MstComponentInspectType.findByDictKey", query = "SELECT m FROM MstComponentInspectType m WHERE m.dictKey = :dictKey"),
    @NamedQuery(name = "MstComponentInspectType.deleteByTypeId", query = "DELETE  FROM MstComponentInspectType t WHERE t.id = :typeId"),
    @NamedQuery(name = "MstComponentInspectType.findByCreateDate", query = "SELECT m FROM MstComponentInspectType m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstComponentInspectType.findByUpdateDate", query = "SELECT m FROM MstComponentInspectType m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstComponentInspectType.findByCreateUserUuid", query = "SELECT m FROM MstComponentInspectType m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstComponentInspectType.findByUpdateUserUuid", query = "SELECT m FROM MstComponentInspectType m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstComponentInspectType.findNotPushed", query = "SELECT t FROM MstComponentInspectType t WHERE t.ownerCompanyId is null AND NOT EXISTS(SELECT b FROM MstInspectExtBatchStatus b WHERE b.pk.id = t.id AND b.pk.companyId = :companyId AND b.batUpdStatus = :batUpdStatus)")
})
public class MstComponentInspectType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
//    @Basic(optional = false)
//    @Column(name = "SEQ")
//    private int seq;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DICT_KEY")
    private String dictKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date createDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter.class)
    private Date updateDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mstComponentInspectType")
    private Collection<MstComponentInspectFile> mstComponentInspectFileCollection;
    
    /** 辞書マスタ*/
    @PrimaryKeyJoinColumn(name = "DICT_KEY", referencedColumnName = "DICT_KEY")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponentInspectLang mstComponentInspectLang;
    
    @Column(name = "OWNER_COMPANY_ID")
    private String ownerCompanyId;

    public MstComponentInspectType() {
    }

    public MstComponentInspectType(String id) {
        this.id = id;
    }

    public MstComponentInspectType(String id,String dictKey, Date createDate, Date updateDate, String createUserUuid, String updateUserUuid) {
        this.id = id;
        this.dictKey = dictKey;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.createUserUuid = createUserUuid;
        this.updateUserUuid = updateUserUuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
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

    @XmlTransient
    public Collection<MstComponentInspectFile> getMstComponentInspectFileCollection() {
        return mstComponentInspectFileCollection;
    }

    public void setMstComponentInspectFileCollection(Collection<MstComponentInspectFile> mstComponentInspectFileCollection) {
        this.mstComponentInspectFileCollection = mstComponentInspectFileCollection;
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
        if (!(object instanceof MstComponentInspectType)) {
            return false;
        }
        MstComponentInspectType other = (MstComponentInspectType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.inspection.file.MstComponentInspectType[ id=" + id + " ]";
    }

    /**
     * @return the mstComponentInspectLang
     */
    public MstComponentInspectLang getMstComponentInspectLang() {
        return mstComponentInspectLang;
    }

    /**
     * @param mstComponentInspectLang the mstComponentInspectLang to set
     */
    public void setMstComponentInspectLang(MstComponentInspectLang mstComponentInspectLang) {
        this.mstComponentInspectLang = mstComponentInspectLang;
    }

    public String getOwnerCompanyId() {
        return ownerCompanyId;
    }

    public void setOwnerCompanyId(String ownerCompanyId) {
        this.ownerCompanyId = ownerCompanyId;
    }
}
