/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.filelinkptn;

import com.kmcj.karte.resources.component.attribute.MstComponentAttribute;
import com.kmcj.karte.resources.mold.attribute.MstMoldAttribute;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
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
@Table(name = "mst_file_link_ptn")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstFileLinkPtn.findAll", query = "SELECT m FROM MstFileLinkPtn m"),
    @NamedQuery(name = "MstFileLinkPtn.findById", query = "SELECT m FROM MstFileLinkPtn m WHERE m.id = :id"),
    @NamedQuery(name = "MstFileLinkPtn.findByFileLinkPtnName", query = "SELECT m FROM MstFileLinkPtn m WHERE m.fileLinkPtnName = :fileLinkPtnName"),
    @NamedQuery(name = "MstFileLinkPtn.findByLinkString", query = "SELECT m FROM MstFileLinkPtn m WHERE m.linkString = :linkString"),
    @NamedQuery(name = "MstFileLinkPtn.findByPurpose", query = "SELECT m FROM MstFileLinkPtn m WHERE m.purpose = :purpose"),
    @NamedQuery(name = "MstFileLinkPtn.findByCreateDate", query = "SELECT m FROM MstFileLinkPtn m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstFileLinkPtn.findByUpdateDate", query = "SELECT m FROM MstFileLinkPtn m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstFileLinkPtn.findByCreateUserUuid", query = "SELECT m FROM MstFileLinkPtn m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstFileLinkPtn.findByUpdateUserUuid", query = "SELECT m FROM MstFileLinkPtn m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstFileLinkPtn.deleteById", query = "DELETE FROM MstFileLinkPtn m WHERE m.id = :id"),
            @NamedQuery(name = "MstFileLinkPtn.updateById", query = "UPDATE MstFileLinkPtn m SET "
            + "m.purpose = :purpose, "
            + "m.linkString = :linkString, "
            + "m.fileLinkPtnName = :fileLinkPtnName,"
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid  WHERE m.id = :id")
})
    
@Cacheable(value = false)
public class MstFileLinkPtn implements Serializable {

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

    @OneToMany(mappedBy = "mstFileLinkPtn_MoldAttr")
    private Collection<MstMoldAttribute> mstMoldAttributeCollection;

    @OneToMany(mappedBy = "mstFileLinkPtn_CompAttr")
    private Collection<MstComponentAttribute> mstComponentAttributeCollection;

    private static long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "FILE_LINK_PTN_NAME")
    private String fileLinkPtnName;
    @Size(max = 256)
    @Column(name = "LINK_STRING")
    private String linkString;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PURPOSE")
    private int purpose;
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

    public MstFileLinkPtn() {
    }

    public MstFileLinkPtn(String id) {
        this.id = id;
    }

    public MstFileLinkPtn(String id, String fileLinkPtnName, int purpose) {
        this.id = id;
        this.fileLinkPtnName = fileLinkPtnName;
        this.purpose = purpose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileLinkPtnName() {
        return fileLinkPtnName;
    }

    public void setFileLinkPtnName(String fileLinkPtnName) {
        this.fileLinkPtnName = fileLinkPtnName;
    }

    public String getLinkString() {
        return linkString;
    }

    public void setLinkString(String linkString) {
        this.linkString = linkString;
    }

    public int getPurpose() {
        return purpose;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
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
        if (!(object instanceof MstFileLinkPtn)) {
            return false;
        }
        MstFileLinkPtn other = (MstFileLinkPtn) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.filelinkptn.MstFileLinkPtn[ id=" + getId() + " ]";
    }

    @XmlTransient
    public Collection<MstComponentAttribute> getMstComponentAttributeCollection() {
        return mstComponentAttributeCollection;
    }

    public void setMstComponentAttributeCollection(Collection<MstComponentAttribute> mstComponentAttributeCollection) {
        this.mstComponentAttributeCollection = mstComponentAttributeCollection;
    }

    /**
     * @return the mstMoldAttributeCollection
     */
    public Collection<MstMoldAttribute> getMstMoldAttributeCollection() {
        return mstMoldAttributeCollection;
    }

    /**
     * @param mstMoldAttributeCollection the mstMoldAttributeCollection to set
     */
    public void setMstMoldAttributeCollection(Collection<MstMoldAttribute> mstMoldAttributeCollection) {
        this.mstMoldAttributeCollection = mstMoldAttributeCollection;
    }
    
}
