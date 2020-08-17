/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.dictionary;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "mst_dictionary")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstDictionary.findAll", query = "SELECT m FROM MstDictionary m ORDER BY m.mstDictionaryPK.langId, m.seq"),
    @NamedQuery(name = "MstDictionary.findById", query = "SELECT m FROM MstDictionary m WHERE m.id = :id"),
    @NamedQuery(name = "MstDictionary.findByKey", query = "SELECT m FROM MstDictionary m WHERE m.mstDictionaryPK.langId = :langId AND m.mstDictionaryPK.dictKey = :dictKey"),
    @NamedQuery(name = "MstDictionary.findByKeyList", query = "SELECT m FROM MstDictionary m WHERE m.mstDictionaryPK.langId = :langId AND m.mstDictionaryPK.dictKey IN :dictKeyList"),
    @NamedQuery(name = "MstDictionary.findByDictValue", query = "SELECT m FROM MstDictionary m WHERE m.dictValue = :dictValue"),
    @NamedQuery(name = "MstDictionary.findBySeq", query = "SELECT m FROM MstDictionary m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstDictionary.findByCategory", query = "SELECT m FROM MstDictionary m WHERE m.category = :category"),
    @NamedQuery(name = "MstDictionary.findByMasterTable", query = "SELECT m FROM MstDictionary m WHERE m.masterTable = :masterTable"),
    @NamedQuery(name = "MstDictionary.findByMasterField", query = "SELECT m FROM MstDictionary m WHERE m.masterField = :masterField"),
    @NamedQuery(name = "MstDictionary.findByCreateDate", query = "SELECT m FROM MstDictionary m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstDictionary.findByUpdateDate", query = "SELECT m FROM MstDictionary m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstDictionary.findByCreateUserUuid", query = "SELECT m FROM MstDictionary m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstDictionary.findByUpdateUserUuid", query = "SELECT m FROM MstDictionary m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstDictionary.update", query = 
            "UPDATE MstDictionary m SET m.dictValue = :dictValue, m.updateDate = :updateDate, m.updateUserUuid = :updateUserUuid WHERE m.mstDictionaryPK.langId = :langId AND m.mstDictionaryPK.dictKey = :dictKey"),
})
@Cacheable(value = false)
public class MstDictionary implements Serializable {

    @Size(max = 100)
    @Column(name = "PARAM_KEY01")
    private String paramKey01;
    @Size(max = 100)
    @Column(name = "PARAM_KEY02")
    private String paramKey02;
    @Size(max = 100)
    @Column(name = "PARAM_KEY03")
    private String paramKey03;
    @Size(max = 100)
    @Column(name = "PARAM_KEY04")
    private String paramKey04;
    @Size(max = 100)
    @Column(name = "PARAM_KEY05")
    private String paramKey05;
    
    //@Transient
    //private String langId;
    //@Transient
    //private String dictKey;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstDictionaryPK mstDictionaryPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 500)
    @Column(name = "DICT_VALUE")
    private String dictValue;
    @Column(name = "SEQ")
    private Integer seq;
    @Size(max = 45)
    @Column(name = "CATEGORY")
    private String category;
    @Size(max = 100)
    @Column(name = "MASTER_TABLE")
    private String masterTable;
    @Size(max = 100)
    @Column(name = "MASTER_FIELD")
    private String masterField;
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

    @Transient
    private boolean modified = false;
    
    public MstDictionary() {
    }

    public MstDictionary(MstDictionaryPK mstDictionaryPK) {
        this.mstDictionaryPK = mstDictionaryPK;
    }

    public MstDictionary(MstDictionaryPK mstDictionaryPK, String id) {
        this.mstDictionaryPK = mstDictionaryPK;
        this.id = id;
    }

    public MstDictionary(String langId, String dictKey) {
        this.mstDictionaryPK = new MstDictionaryPK(langId, dictKey);
    }

    @XmlTransient
    public MstDictionaryPK getMstDictionaryPK() {
        return mstDictionaryPK;
    }

    public void setMstDictionaryPK(MstDictionaryPK mstDictionaryPK) {
        this.mstDictionaryPK = mstDictionaryPK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @XmlTransient
    public String getMasterTable() {
        return masterTable;
    }

    public void setMasterTable(String masterTable) {
        this.masterTable = masterTable;
    }

    @XmlTransient
    public String getMasterField() {
        return masterField;
    }

    public void setMasterField(String masterField) {
        this.masterField = masterField;
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
        hash += (mstDictionaryPK != null ? mstDictionaryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstDictionary)) {
            return false;
        }
        MstDictionary other = (MstDictionary) object;
        if ((this.mstDictionaryPK == null && other.mstDictionaryPK != null) || (this.mstDictionaryPK != null && !this.mstDictionaryPK.equals(other.mstDictionaryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.dictionary.MstDictionary[ mstDictionaryPK=" + mstDictionaryPK + " ]";
    }

    /**
     * @return the langId
     */
    @XmlElement
    public String getLangId() {
        //return langId;
        return mstDictionaryPK.getLangId();
    }

    /**
     * @return the dictKey
     */
    @XmlElement
    public String getDictKey() {
        return mstDictionaryPK.getDictKey();
        //return dictKey;
    }

    public String getParamKey01() {
        return paramKey01;
    }

    public void setParamKey01(String paramKey01) {
        this.paramKey01 = paramKey01;
    }

    public String getParamKey02() {
        return paramKey02;
    }

    public void setParamKey02(String paramKey02) {
        this.paramKey02 = paramKey02;
    }

    public String getParamKey03() {
        return paramKey03;
    }

    public void setParamKey03(String paramKey03) {
        this.paramKey03 = paramKey03;
    }

    public String getParamKey04() {
        return paramKey04;
    }

    public void setParamKey04(String paramKey04) {
        this.paramKey04 = paramKey04;
    }

    public String getParamKey05() {
        return paramKey05;
    }

    public void setParamKey05(String paramKey05) {
        this.paramKey05 = paramKey05;
    }

    /**
     * @return the modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * @param langId the langId to set
     */
    public void setLangId(String langId) {
//        this.langId = langId;
        if (this.mstDictionaryPK == null) {
            this.mstDictionaryPK = new MstDictionaryPK();
        }
        this.mstDictionaryPK.setLangId(langId);
    }

    /**
     * @param dictKey the dictKey to set
     */
    public void setDictKey(String dictKey) {
//        this.dictKey = dictKey;
        if (this.mstDictionaryPK == null) {
            this.mstDictionaryPK = new MstDictionaryPK();
        }
        this.mstDictionaryPK.setDictKey(dictKey);
    }
    
}
