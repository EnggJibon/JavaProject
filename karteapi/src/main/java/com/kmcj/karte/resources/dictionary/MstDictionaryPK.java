/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.dictionary;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author f.kitaoji
 */
@Embeddable
public class MstDictionaryPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "LANG_ID")
    private String langId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DICT_KEY")
    private String dictKey;

    public MstDictionaryPK() {
    }

    public MstDictionaryPK(String langId, String dictKey) {
        this.langId = langId;
        this.dictKey = dictKey;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (langId != null ? langId.hashCode() : 0);
        hash += (dictKey != null ? dictKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstDictionaryPK)) {
            return false;
        }
        MstDictionaryPK other = (MstDictionaryPK) object;
        if ((this.langId == null && other.langId != null) || (this.langId != null && !this.langId.equals(other.langId))) {
            return false;
        }
        if ((this.dictKey == null && other.dictKey != null) || (this.dictKey != null && !this.dictKey.equals(other.dictKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.dictionary.MstDictionaryPK[ langId=" + langId + ", dictKey=" + dictKey + " ]";
    }
    
}
