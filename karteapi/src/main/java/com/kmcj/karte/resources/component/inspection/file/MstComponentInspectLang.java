/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.file;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.kmcj.karte.util.XmlDateAdapter;

/**
 *
 * @author Apeng
 */
@Entity
@Table(name = "mst_component_inspect_lang")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstComponentInspectLang.findAll", query = "SELECT m FROM MstComponentInspectLang m"),
    @NamedQuery(name = "MstComponentInspectLang.findByPk", query = "SELECT m FROM MstComponentInspectLang m WHERE m.mstComponentInspectLangPK.dictKey = :dictKey AND m.mstComponentInspectLangPK.langId = :langId"),
    @NamedQuery(name = "MstComponentInspectLang.findByDictKey", query = "SELECT m FROM MstComponentInspectLang m WHERE m.mstComponentInspectLangPK.dictKey = :dictKey"),
    @NamedQuery(name = "MstComponentInspectLang.findByDictKeys", query = "SELECT m FROM MstComponentInspectLang m WHERE m.mstComponentInspectLangPK.dictKey IN :dictKeys"),
    @NamedQuery(name = "MstComponentInspectLang.deleteByDictKey", query = "DELETE FROM MstComponentInspectLang m WHERE m.mstComponentInspectLangPK.dictKey = :dictKey"),
    @NamedQuery(name = "MstComponentInspectLang.findByLangId", query = "SELECT m FROM MstComponentInspectLang m WHERE m.mstComponentInspectLangPK.langId = :langId"),
    @NamedQuery(name = "MstComponentInspectLang.findByDictValue", query = "SELECT m FROM MstComponentInspectLang m WHERE m.dictValue = :dictValue"),
    @NamedQuery(name = "MstComponentInspectLang.findByCreateDate", query = "SELECT m FROM MstComponentInspectLang m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstComponentInspectLang.findByUpdateDate", query = "SELECT m FROM MstComponentInspectLang m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstComponentInspectLang.findByCreateUserUuid", query = "SELECT m FROM MstComponentInspectLang m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstComponentInspectLang.findByUpdateUserUuid", query = "SELECT m FROM MstComponentInspectLang m WHERE m.updateUserUuid = :updateUserUuid")})
public class MstComponentInspectLang implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private MstComponentInspectLangPK mstComponentInspectLangPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DICT_VALUE")
    private String dictValue;
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

    public MstComponentInspectLang() {
    }

    public MstComponentInspectLang(MstComponentInspectLangPK mstComponentInspectLangPK) {
        this.mstComponentInspectLangPK = mstComponentInspectLangPK;
    }

    public MstComponentInspectLang(MstComponentInspectLangPK mstComponentInspectLangPK, String dictValue, Date createDate, Date updateDate, String createUserUuid, String updateUserUuid) {
        this.mstComponentInspectLangPK = mstComponentInspectLangPK;
        this.dictValue = dictValue;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.createUserUuid = createUserUuid;
        this.updateUserUuid = updateUserUuid;
    }

    public MstComponentInspectLang(String dictKey, String langId) {
        this.mstComponentInspectLangPK = new MstComponentInspectLangPK(dictKey, langId);
    }

    public MstComponentInspectLangPK getMstComponentInspectLangPK() {
        return mstComponentInspectLangPK;
    }

    public void setMstComponentInspectLangPK(MstComponentInspectLangPK mstComponentInspectLangPK) {
        this.mstComponentInspectLangPK = mstComponentInspectLangPK;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
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
        hash += (mstComponentInspectLangPK != null ? mstComponentInspectLangPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstComponentInspectLang)) {
            return false;
        }
        MstComponentInspectLang other = (MstComponentInspectLang) object;
        if ((this.mstComponentInspectLangPK == null && other.mstComponentInspectLangPK != null) || (this.mstComponentInspectLangPK != null && !this.mstComponentInspectLangPK.equals(other.mstComponentInspectLangPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.component.inspection.file.MstComponentInspectLang[ mstComponentInspectLangPK=" + mstComponentInspectLangPK + " ]";
    }
    
}
