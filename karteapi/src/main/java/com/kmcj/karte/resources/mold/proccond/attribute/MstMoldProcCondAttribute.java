package com.kmcj.karte.resources.mold.proccond.attribute;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "mst_mold_proc_cond_attribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMoldProcCondAttribute.findAll", query = "SELECT m FROM MstMoldProcCondAttribute m"),
    @NamedQuery(name = "MstMoldProcCondAttribute.findById", query = "SELECT m FROM MstMoldProcCondAttribute m WHERE m.id = :id"),
    @NamedQuery(name = "MstMoldProcCondAttribute.findByMoldType", query = "SELECT m FROM MstMoldProcCondAttribute m WHERE m.moldType = :moldType order by m.seq"),
    @NamedQuery(name = "MstMoldProcCondAttribute.findByAttrCode", query = "SELECT m FROM MstMoldProcCondAttribute m WHERE m.attrCode = :attrCode And m.moldType = :moldType And m.externalFlg = :externalFlg"),
    @NamedQuery(name = "MstMoldProcCondAttribute.findByAttrName", query = "SELECT m FROM MstMoldProcCondAttribute m WHERE m.attrName = :attrName"),
    @NamedQuery(name = "MstMoldProcCondAttribute.findByAttrType", query = "SELECT m FROM MstMoldProcCondAttribute m WHERE m.attrType = :attrType"),
    @NamedQuery(name = "MstMoldProcCondAttribute.findBySeq", query = "SELECT m FROM MstMoldProcCondAttribute m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstMoldProcCondAttribute.findByCreateDate", query = "SELECT m FROM MstMoldProcCondAttribute m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMoldProcCondAttribute.findByUpdateDate", query = "SELECT m FROM MstMoldProcCondAttribute m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMoldProcCondAttribute.findByCreateUserUuid", query = "SELECT m FROM MstMoldProcCondAttribute m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMoldProcCondAttribute.findByExternalFlg", query = "SELECT m FROM MstMoldProcCondAttribute m WHERE m.externalFlg = :externalFlg"),
    @NamedQuery(name = "MstMoldProcCondAttribute.findByUpdateUserUuid", query = "SELECT m FROM MstMoldProcCondAttribute m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMoldProcCondAttribute.deleteByAttrCode", query = "DELETE FROM MstMoldProcCondAttribute m "
            + "         WHERE m.id = :id"),
    @NamedQuery(name = "MstMoldProcCondAttribute.updateMstMoldProcCondAttributeByQuery", query = "UPDATE MstMoldProcCondAttribute m SET "
            + "m.seq = :seq,"
            + "m.attrName = :attrName, "
            + "m.attrType = :attrType, "
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid  "
            + "WHERE  m.id = :id "
            + "and m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstMoldProcCondAttribute.findByMoldTypeAndAttrCode", query = "SELECT m FROM MstMoldProcCondAttribute m "
            + "WHERE m.moldType = :moldType "
            + "and m.attrCode = :attrCode "
            + "and m.externalFlg = :externalFlg")
})
@Cacheable(value = false)
public class MstMoldProcCondAttribute implements Serializable {

    private static long serialVersionUID = 1L;

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
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MOLD_TYPE")
    private int moldType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ATTR_CODE")
    private String attrCode;
    @Size(max = 45)
    @Column(name = "ATTR_NAME")
    private String attrName;
    @Column(name = "ATTR_TYPE")
    private Integer attrType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SEQ")
    private int seq;
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
    @Column(name = "EXTERNAL_FLG")
    private Integer externalFlg;

    public MstMoldProcCondAttribute() {
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public Integer getAttrType() {
        return attrType;
    }

    public void setAttrType(Integer attrType) {
        this.attrType = attrType;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
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
        if (!(object instanceof MstMoldProcCondAttribute)) {
            return false;
        }
        MstMoldProcCondAttribute other = (MstMoldProcCondAttribute) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "test.MstMoldProcCondAttribute[ id=" + getId() + " ]";
    }

    /**
     * @return the moldType
     */
    public int getMoldType() {
        return moldType;
    }

    /**
     * @param moldType the moldType to set
     */
    public void setMoldType(int moldType) {
        this.moldType = moldType;
    }

    /**
     * @return the attrCode
     */
    public String getAttrCode() {
        return attrCode;
    }

    /**
     * @param attrCode the attrCode to set
     */
    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    /**
     * @return the externalFlg
     */
    public Integer getExternalFlg() {
        return externalFlg;
    }

    /**
     * @param externalFlg the externalFlg to set
     */
    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
    }

}
