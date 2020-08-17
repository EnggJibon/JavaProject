/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.batch.externalmold.choice;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "ext_mst_choice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ExtMstChoice.findAll", query = "SELECT e FROM ExtMstChoice e"),
    @NamedQuery(name = "ExtMstChoice.findById", query = "SELECT e FROM ExtMstChoice e WHERE e.extMstChoicePK.id = :id"),
    @NamedQuery(name = "ExtMstChoice.findByCompanyId", query = "SELECT e FROM ExtMstChoice e WHERE e.extMstChoicePK.companyId = :companyId"),
    @NamedQuery(name = "ExtMstChoice.findByCategory", query = "SELECT e FROM ExtMstChoice e WHERE e.category = :category"),
    @NamedQuery(name = "ExtMstChoice.findByLangId", query = "SELECT e FROM ExtMstChoice e WHERE e.langId = :langId"),
    @NamedQuery(name = "ExtMstChoice.findBySeq", query = "SELECT e FROM ExtMstChoice e WHERE e.seq = :seq"),
    @NamedQuery(name = "ExtMstChoice.findByChoice", query = "SELECT e FROM ExtMstChoice e WHERE e.choice = :choice"),
    @NamedQuery(name = "ExtMstChoice.findByParentSeq", query = "SELECT e FROM ExtMstChoice e WHERE e.parentSeq = :parentSeq"),
    @NamedQuery(name = "ExtMstChoice.findByCreateDate", query = "SELECT e FROM ExtMstChoice e WHERE e.createDate = :createDate"),
    @NamedQuery(name = "ExtMstChoice.findByUpdateDate", query = "SELECT e FROM ExtMstChoice e WHERE e.updateDate = :updateDate"),
    @NamedQuery(name = "ExtMstChoice.findByCreateUserUuid", query = "SELECT e FROM ExtMstChoice e WHERE e.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "ExtMstChoice.findByUpdateUserUuid", query = "SELECT e FROM ExtMstChoice e WHERE e.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "ExtMstChoice.findByComAndCategoryAndSeqAndLang", query = "SELECT m FROM ExtMstChoice m WHERE m.category = :category AND m.extMstChoicePK.companyId = :companyId and m.seq = :seq AND m.langId = :langId"),
    @NamedQuery(name = "ExtMstChoice.deleteByCompanyId", query = "DELETE FROM ExtMstChoice e WHERE e.extMstChoicePK.companyId = :companyId "),
    @NamedQuery(name = "ExtMstChoice.findByCategoryList", query = "SELECT m FROM ExtMstChoice m WHERE m.langId = :langId AND m.extMstChoicePK.companyId = :companyId AND m.category IN :categoryList ORDER BY m.seq"),
})
@Cacheable(value = false)
public class ExtMstChoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ExtMstChoicePK extMstChoicePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "CATEGORY")
    private String category;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "LANG_ID")
    private String langId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private String seq;
    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
    @Column(name = "CHOICE")
    private String choice;
    @Column(name = "PARENT_SEQ")
    private String parentSeq;
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

    public ExtMstChoice() {
    }

    public ExtMstChoice(ExtMstChoicePK extMstChoicePK) {
        this.extMstChoicePK = extMstChoicePK;
    }

    public ExtMstChoice(ExtMstChoicePK extMstChoicePK, String category, String langId, String seq, String choice) {
        this.extMstChoicePK = extMstChoicePK;
        this.category = category;
        this.langId = langId;
        this.seq = seq;
        this.choice = choice;
    }

    public ExtMstChoice(String id, String companyId) {
        this.extMstChoicePK = new ExtMstChoicePK(id, companyId);
    }

    public ExtMstChoicePK getExtMstChoicePK() {
        return extMstChoicePK;
    }

    public void setExtMstChoicePK(ExtMstChoicePK extMstChoicePK) {
        this.extMstChoicePK = extMstChoicePK;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getParentSeq() {
        return parentSeq;
    }

    public void setParentSeq(String parentSeq) {
        this.parentSeq = parentSeq;
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
        hash += (extMstChoicePK != null ? extMstChoicePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ExtMstChoice)) {
            return false;
        }
        ExtMstChoice other = (ExtMstChoice) object;
        if ((this.extMstChoicePK == null && other.extMstChoicePK != null) || (this.extMstChoicePK != null && !this.extMstChoicePK.equals(other.extMstChoicePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.batch.externalmold.ExtMstChoice[ extMstChoicePK=" + extMstChoicePK + " ]";
    }
    
}
