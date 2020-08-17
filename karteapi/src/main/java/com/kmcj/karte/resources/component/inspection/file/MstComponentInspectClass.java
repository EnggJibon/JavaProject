/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.file;

import com.kmcj.karte.util.XmlDateAdapter;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Apeng
 */
@Entity
@Table(name = "mst_component_inspect_class")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstComponentInspectClass.findById", query = "SELECT m FROM MstComponentInspectClass m WHERE m.pk.id = :id AND m.pk.ownerCompanyId = :ownerCompanyId"),
    @NamedQuery(name = "MstComponentInspectClass.findByOwner", query = "SELECT m FROM MstComponentInspectClass m JOIN FETCH m.mstComponentInspectLang WHERE m.pk.ownerCompanyId = :ownerCompanyId and m.mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId order by m.seq"),
    @NamedQuery(name = "MstComponentInspectClass.findByMassFlg", query = "SELECT m FROM MstComponentInspectClass m WHERE m.massFlg = :massFlg AND m.pk.ownerCompanyId = 'SELF'"),
    @NamedQuery(name = "MstComponentInspectClass.findByDictKey", query = "SELECT m FROM MstComponentInspectClass m WHERE m.dictKey = :dictKey AND m.pk.ownerCompanyId = 'SELF'"),
    @NamedQuery(name = "MstComponentInspectClass.deleteByClassId", query = "DELETE  FROM MstComponentInspectClass m WHERE m.pk.id = :classId AND m.pk.ownerCompanyId = 'SELF'"),
    @NamedQuery(name = "MstComponentInspectClass.findNotPushed", query = "SELECT c FROM MstComponentInspectClass c WHERE c.pk.ownerCompanyId = 'SELF' AND NOT EXISTS(SELECT b FROM MstInspectExtBatchStatus b WHERE b.pk.id = c.pk.id AND b.pk.companyId = :companyId AND b.batUpdStatus = :batUpdStatus)")
})
public class MstComponentInspectClass implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private PK pk;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MASS_FLG")
    private Character massFlg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
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

    /*
     * 辞書マスタ
     */
    @PrimaryKeyJoinColumn(name = "DICT_KEY", referencedColumnName = "DICT_KEY")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponentInspectLang mstComponentInspectLang;

    public MstComponentInspectClass() {
        this.pk = new PK();
    }

    public MstComponentInspectClass(String id) {
        this.pk.id = id;
    }

    public MstComponentInspectClass(String id, Character massFlg, String dictKey, Date createDate, Date updateDate, String createUserUuid, String updateUserUuid) {
        this.pk.id = id;
        this.massFlg = massFlg;
        this.dictKey = dictKey;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.createUserUuid = createUserUuid;
        this.updateUserUuid = updateUserUuid;
    }

    public PK getPk() {
        return pk;
    }

    public void setPk(PK pk) {
        this.pk = pk;
    }

    public Character getMassFlg() {
        return massFlg;
    }

    public void setMassFlg(Character massFlg) {
        this.massFlg = massFlg;
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

    @Override
    public int hashCode() {
        return pk.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstComponentInspectClass)) {
            return false;
        }
        MstComponentInspectClass other = (MstComponentInspectClass) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
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

    /**
     * @return the seq
     */
    public int getSeq() {
        return seq;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Embeddable
    public static class PK implements Serializable {
        @NotNull
        @Size(min = 1, max = 45)
        @Column(name = "ID")
        private String id;
        @NotNull
        @Column(name = "OWNER_COMPANY_ID")
        private String ownerCompanyId = "SELF";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOwnerCompanyId() {
            return ownerCompanyId;
        }

        public void setOwnerCompanyId(String ownerCompanyId) {
            this.ownerCompanyId = ownerCompanyId;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 29 * hash + Objects.hashCode(this.id);
            hash = 29 * hash + Objects.hashCode(this.ownerCompanyId);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PK other = (PK) obj;
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            if (!Objects.equals(this.ownerCompanyId, other.ownerCompanyId)) {
                return false;
            }
            return true;
        }
    }
}
