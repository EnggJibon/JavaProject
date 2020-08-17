/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.choice;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
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
 * @author c.darvin
 */
@Entity
@Table(name = "mst_choice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstChoicePrime.findAll", query = "SELECT m FROM MstChoicePrime m")
})
public class MstChoicePrime implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private Pk pk;
    
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
    
    @Column(name = "DELETE_FLG")
    private Integer deleteFlg;

    public Pk getPk() {
        return pk;
    }

    public void setPk(Pk pk) {
        this.pk = pk;
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

    public String getParentSeq() {
        return parentSeq;
    }

    public void setParentSeq(String parentSeq) {
        this.parentSeq = parentSeq;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
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

    public Integer getDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(Integer deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.pk);
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
        final MstChoicePrime other = (MstChoicePrime) obj;
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        return true;
    }
    
    @Embeddable
    public static class Pk implements Serializable {
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
        @Size(min = 1, max = 45)
        @Column(name = "SEQ")
        private String seq;
        
        public Pk() {}
        
        public Pk(String category, String langId, String seq) {
            this.category = category;
            this.langId = langId;
            this.seq = seq;
        }
        
        public Pk(MstChoicePK pk) {
            this.category = pk.getCategory();
            this.langId = pk.getLangId();
            this.seq = pk.getSeq();
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

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 41 * hash + Objects.hashCode(this.category);
            hash = 41 * hash + Objects.hashCode(this.langId);
            hash = 41 * hash + Objects.hashCode(this.seq);
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
            final Pk other = (Pk) obj;
            if (!Objects.equals(this.category, other.category)) {
                return false;
            }
            if (!Objects.equals(this.langId, other.langId)) {
                return false;
            }
            if (!Objects.equals(this.seq, other.seq)) {
                return false;
            }
            return true;
        }
    }
}
