/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.choice;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "mst_choice_category")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstChoiceCategory.findAll", query = "SELECT m FROM MstChoiceCategory m"),
    @NamedQuery(name = "MstChoiceCategory.findById", query = "SELECT m FROM MstChoiceCategory m WHERE m.id = :id"),
    @NamedQuery(name = "MstChoiceCategory.findByCategory", query = "SELECT m FROM MstChoiceCategory m WHERE m.category = :category AND m.canAddDelete = 1"),
    @NamedQuery(name = "MstChoiceCategory.findByCanAddDelete", query = "SELECT m FROM MstChoiceCategory m WHERE m.canAddDelete = :canAddDelete"),
    @NamedQuery(name = "MstChoiceCategory.findByCategoryNameDictKey", query = "SELECT m FROM MstChoiceCategory m WHERE m.categoryNameDictKey = :categoryNameDictKey"),
    @NamedQuery(name = "MstChoiceCategory.findBySeq", query = "SELECT m FROM MstChoiceCategory m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstChoiceCategory.findByTableId", query = "SELECT m FROM MstChoiceCategory m WHERE m.tableId = :tableId"),
    @NamedQuery(name = "MstChoiceCategory.findByFieldId", query = "SELECT m FROM MstChoiceCategory m WHERE m.fieldId = :fieldId"),
    @NamedQuery(name = "MstChoiceCategory.findByCreateDate", query = "SELECT m FROM MstChoiceCategory m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstChoiceCategory.findByUpdateDate", query = "SELECT m FROM MstChoiceCategory m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstChoiceCategory.findByCreateUserUuid", query = "SELECT m FROM MstChoiceCategory m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstChoiceCategory.findByUpdateUserUuid", query = "SELECT m FROM MstChoiceCategory m WHERE m.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class MstChoiceCategory implements Serializable {

    @Size(max = 200)
    @Column(name = "PARENT_CATEGORY")
    private String parentCategory;

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "CATEGORY")
    private String category;
    @Size(max = 45)
    @Column(name = "CAN_ADD_DELETE")
    private String canAddDelete;
    @Size(max = 45)
    @Column(name = "CATEGORY_NAME_DICT_KEY")
    private String categoryNameDictKey;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
    @Size(max = 100)
    @Column(name = "TABLE_ID")
    private String tableId;
    @Size(max = 100)
    @Column(name = "FIELD_ID")
    private String fieldId;
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
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "mstChoiceCategory")
    private Collection<MstChoice> mstChoiceCollection;

    public MstChoiceCategory() {
    }

    public MstChoiceCategory(String category) {
        this.category = category;
    }

    public MstChoiceCategory(String category, String id, int seq) {
        this.category = category;
        this.id = id;
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCanAddDelete() {
        return canAddDelete;
    }

    public void setCanAddDelete(String canAddDelete) {
        this.canAddDelete = canAddDelete;
    }

    public String getCategoryNameDictKey() {
        return categoryNameDictKey;
    }

    public void setCategoryNameDictKey(String categoryNameDictKey) {
        this.categoryNameDictKey = categoryNameDictKey;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
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
    public Collection<MstChoice> getMstChoiceCollection() {
        return mstChoiceCollection;
    }

    public void setMstChoiceCollection(Collection<MstChoice> mstChoiceCollection) {
        this.mstChoiceCollection = mstChoiceCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (category != null ? category.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstChoiceCategory)) {
            return false;
        }
        MstChoiceCategory other = (MstChoiceCategory) object;
        if ((this.category == null && other.category != null) || (this.category != null && !this.category.equals(other.category))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.choice.MstChoiceCategory[ category=" + category + " ]";
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }
    
}
