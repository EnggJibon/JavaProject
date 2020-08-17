/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.choice;

import com.kmcj.karte.resources.language.MstLanguage;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "mst_choice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstChoice.findAll", query = "SELECT m FROM MstChoice m"),
    @NamedQuery(name = "MstChoice.findById", query = "SELECT m FROM MstChoice m WHERE m.id = :id"),
    @NamedQuery(name = "MstChoice.findByCategoryOnly", query = "SELECT m FROM MstChoice m WHERE m.mstChoicePK.category = :category"),
    @NamedQuery(name = "MstChoice.findByCategory", query = "SELECT m FROM MstChoice m WHERE m.mstChoicePK.langId = :langId AND m.mstChoicePK.category = :category ORDER BY m.displaySeq"),
    @NamedQuery(name = "MstChoice.findByCategoryList", query = "SELECT m FROM MstChoice m WHERE m.mstChoicePK.langId = :langId AND m.mstChoicePK.category IN :categoryList ORDER BY m.displaySeq"),
    @NamedQuery(name = "MstChoice.findByPK", query = "SELECT m FROM MstChoice m WHERE m.mstChoicePK.langId = :langId AND m.mstChoicePK.category = :category AND m.mstChoicePK.seq = :seq"),
    @NamedQuery(name = "MstChoice.findByCategoryAndLangIdAndChoice", query = "SELECT m FROM MstChoice m WHERE m.mstChoicePK.langId = :langId AND m.mstChoicePK.category = :category AND m.choice = :choice"),
    @NamedQuery(name = "MstChoice.findByDisplaySeq", query = "SELECT m FROM MstChoice m WHERE m.displaySeq = :displaySeq"),
    @NamedQuery(name = "MstChoice.findByDeleteFlg", query = "SELECT m FROM MstChoice m WHERE m.deleteFlg = :deleteFlg"),
    @NamedQuery(name = "MstChoice.findBySeq", query = "SELECT m FROM MstChoice m WHERE m.mstChoicePK.seq = :seq"),
    @NamedQuery(name = "MstChoice.findByChoice", query = "SELECT m FROM MstChoice m WHERE m.choice = :choice"),
    @NamedQuery(name = "MstChoice.DeleteByCategoryAndLangId", query = "DELETE FROM MstChoice m WHERE m.mstChoicePK.category = :category and m.mstChoicePK.langId = :langId "),
    @NamedQuery(name = "MstChoice.DeleteByPK", query = "DELETE FROM MstChoice m WHERE m.mstChoicePK.category = :category and m.mstChoicePK.langId = :langId and m.mstChoicePK.seq = :seq "),
    @NamedQuery(name = "MstChoice.DeleteById", query = "DELETE FROM MstChoice m WHERE m.id = :id "),
    @NamedQuery(name = "MstChoice.findByCategoryAndLangIdAndParentseq", query = "Select c FROM MstChoice c WHERE c.mstChoiceCategory.parentCategory = :category and c.mstChoicePK.langId = :langId AND c.parentSeq = :parentSeq "),
    @NamedQuery(name = "MstChoice.findByCategoryAndLangIdAndParentseqAndSeq", query = "Select c FROM MstChoice c WHERE c.mstChoicePK.category = :category and c.mstChoicePK.langId = :langId AND c.parentSeq = :parentSeq AND c.mstChoicePK.seq = :seq "),
    @NamedQuery(name = "MstChoice.findByCategoryAndLangId", query = "Select c FROM MstChoice c WHERE c.mstChoiceCategory.parentCategory = :category and c.mstChoicePK.langId = :langId "),
    @NamedQuery(name = "MstChoice.findByCategoryAndSeq", query = "SELECT m FROM MstChoice m WHERE m.mstChoicePK.category = :category AND m.mstChoicePK.seq = :seq "),
    @NamedQuery(name = "MstChoice.findByCategoryAndDisplaySeq", query = "SELECT m FROM MstChoice m WHERE m.mstChoicePK.category = :category AND m.displaySeq = :displaySeq "),
    @NamedQuery(name = "MstChoice.findByCategoryAndParentSeq", query = "SELECT m FROM MstChoice m WHERE m.mstChoicePK.category = :category AND m.parentSeq = :parentSeq"),
    @NamedQuery(name = "MstChoice.findByCreateDate", query = "SELECT m FROM MstChoice m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstChoice.findByUpdateDate", query = "SELECT m FROM MstChoice m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstChoice.findByCreateUserUuid", query = "SELECT m FROM MstChoice m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstChoice.findByUpdateUserUuid", query = "SELECT m FROM MstChoice m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstChoice.findByChoiceSeq", query = "SELECT m FROM MstChoice m WHERE m.mstChoicePK.category = :category and m.mstChoicePK.langId = :langId and m.choice = :choice")})
@Cacheable(value = false)
public class MstChoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MstChoicePK mstChoicePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(max = 100)
    @Column(name = "CHOICE")
    private String choice;
//    @Basic(optional = false)
//    @NotNull
//    @Size(max = 45)
//    @Column(name = "SEQ")
//    private String seq;
    @Column(name = "DISPLAY_SEQ")
    private Integer displaySeq;
    @Size(max = 500)
    @Column(name = "PARENT_SEQ")
    private String parentSeq;
    @Size(max = 45)
    @Column(name = "LANG_ID", insertable = false, updatable = false)
    private String langId;
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
    @JoinColumn(name = "CATEGORY", referencedColumnName = "CATEGORY", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MstChoiceCategory mstChoiceCategory;
    @JoinColumn(name = "LANG_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MstLanguage mstLanguage;
    @Column(name = "DELETE_FLG")
    private Integer deleteFlg;
    
    @Transient
    private boolean isError; //csvチェック　エラー有無　判定用
        
    public MstChoice() {
    }

    public MstChoice(MstChoicePK mstChoicePK) {
        this.mstChoicePK = mstChoicePK;
    }

    public MstChoice(MstChoicePK mstChoicePK, String id, String choice) {
        this.mstChoicePK = mstChoicePK;
        this.id = id;
        this.choice = choice;
    }

    public MstChoice(String category, String langId, String seq) {
        this.mstChoicePK = new MstChoicePK(category, langId, seq);
    }

    public MstChoicePK getMstChoicePK() {
        return mstChoicePK;
    }

    public void setMstChoicePK(MstChoicePK mstChoicePK) {
        this.mstChoicePK = mstChoicePK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public Integer getDisplaySeq() {
        return displaySeq;
    }

    public void setDisplaySeq(Integer displaySeq) {
        this.displaySeq = displaySeq;
    }
//    
//    public String getSeq() {
//        return seq;
//    }
//
//    public void setSeq(String seq) {
//        this.seq = seq;
//    }

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

    public MstChoiceCategory getMstChoiceCategory() {
        return mstChoiceCategory;
    }

    public void setMstChoiceCategory(MstChoiceCategory mstChoiceCategory) {
        this.mstChoiceCategory = mstChoiceCategory;
    }

    public MstLanguage getMstLanguage() {
        return mstLanguage;
    }

    public void setMstLanguage(MstLanguage mstLanguage) {
        this.mstLanguage = mstLanguage;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mstChoicePK != null ? mstChoicePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstChoice)) {
            return false;
        }
        MstChoice other = (MstChoice) object;
        if ((this.mstChoicePK == null && other.mstChoicePK != null) || (this.mstChoicePK != null && !this.mstChoicePK.equals(other.mstChoicePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.choice.MstChoice[ mstChoicePK=" + mstChoicePK + " ]";
    }

    /**
     * @return the parentSeq
     */
    public String getParentSeq() {
        return parentSeq;
    }

    /**
     * @param parentSeq the parentSeq to set
     */
    public void setParentSeq(String parentSeq) {
        this.parentSeq = parentSeq;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }
    
   /**
     * @return the isError
     */
    public boolean isError() {
        return isError;
    }

    /**
     * @param isError the isError to set
     */
    public void setIsError(boolean isError) {
        this.isError = isError;
    }
    
    public Integer getDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(Integer deleteFlg) {
        this.deleteFlg = deleteFlg;
    }
    
}
