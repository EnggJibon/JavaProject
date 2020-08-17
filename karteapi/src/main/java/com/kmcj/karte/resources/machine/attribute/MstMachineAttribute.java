package com.kmcj.karte.resources.machine.attribute;

import com.kmcj.karte.resources.filelinkptn.MstFileLinkPtn;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "mst_machine_attribute")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMachineAttribute.findAll", query = "SELECT m FROM MstMachineAttribute m"),
    @NamedQuery(name = "MstMachineAttribute.findById", query = "SELECT m FROM MstMachineAttribute m WHERE m.id = :id"),
    @NamedQuery(name = "MstMachineAttribute.deleteById", query = "DELETE FROM MstMachineAttribute m WHERE m.id = :id"),    
    @NamedQuery(name = "MstMachineAttribute.findByMachineType", query = "SELECT m FROM MstMachineAttribute m WHERE m.machineType = :machineType"),
    @NamedQuery(name = "MstMachineAttribute.findByMachineTypeAndAttrCode", query = "SELECT m FROM MstMachineAttribute m WHERE m.machineType = :machineType And m.attrCode = :attrCode"),
    @NamedQuery(name = "MstMachineAttribute.findByAttrCode", query = "SELECT m FROM MstMachineAttribute m WHERE m.attrCode = :attrCode And m.machineType = :machineType And m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstMachineAttribute.findByAttrCodes", query = "SELECT m FROM MstMachineAttribute m WHERE m.attrCode = :attrCode And m.externalFlg = :externalFlg "),
    @NamedQuery(name = "MstMachineAttribute.findByAttrName", query = "SELECT m FROM MstMachineAttribute m WHERE m.attrName = :attrName"),
    @NamedQuery(name = "MstMachineAttribute.findByAttrType", query = "SELECT m FROM MstMachineAttribute m WHERE m.attrType = :attrType"),
    @NamedQuery(name = "MstMachineAttribute.findBySeq", query = "SELECT m FROM MstMachineAttribute m WHERE m.seq = :seq"),
    @NamedQuery(name = "MstMachineAttribute.findByCreateDate", query = "SELECT m FROM MstMachineAttribute m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMachineAttribute.findByUpdateDate", query = "SELECT m FROM MstMachineAttribute m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMachineAttribute.findByCreateUserUuid", query = "SELECT m FROM MstMachineAttribute m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMachineAttribute.findByUpdateUserUuid", query = "SELECT m FROM MstMachineAttribute m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMachineAttribute.findByExternalFlg", query = "SELECT m FROM MstMachineAttribute m WHERE m.externalFlg = :externalFlg"),
    
    // ファイルリンク削除する前に、設備属性ＦＫチェック
    @NamedQuery(name = "MstMachineAttribute.findByFileLinkPtnId", query = "SELECT m FROM MstMachineAttribute m WHERE m.fileLinkPtnId = :fileLinkPtnId"),  
    
    @NamedQuery(name = "MstMachineAttribute.updateByMachineType", query = "UPDATE MstMachineAttribute m SET m.seq = :seq,"
            + "m.attrName = :attrName,"
            + "m.attrType = :attrType,"
            + "m.fileLinkPtnId = :fileLinkPtnId,"
            + "m.updateDate = :updateDate,m.updateUserUuid = :updateUserUuid "
            + "WHERE m.id = :id AND m.externalFlg = :externalFlg")
})
@Cacheable(value = false)
public class MstMachineAttribute implements Serializable {

    @JoinColumn(name = "FILE_LINK_PTN_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstFileLinkPtn mstFileLinkPtn;
    @Column(name = "FILE_LINK_PTN_ID")
    private String fileLinkPtnId;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MACHINE_TYPE")
    private int machineType;
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

    public MstMachineAttribute() {
    }

    public MstMachineAttribute(String id) {
        this.id = id;
    }

    public MstMachineAttribute(String id, int machineType, String attrCode, int seq) {
        this.id = id;
        this.machineType = machineType;
        this.attrCode = attrCode;
        this.seq = seq;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMachineType() {
        return machineType;
    }

    public void setMachineType(int machineType) {
        this.machineType = machineType;
    }

    public String getAttrCode() {
        return attrCode;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
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

    public Integer getExternalFlg() {
        return externalFlg;
    }

    public void setExternalFlg(Integer externalFlg) {
        this.externalFlg = externalFlg;
    }

    public MstFileLinkPtn getMstFileLinkPtn() {
        return mstFileLinkPtn;
    }

    public String getFileLinkPtnId() {
        return fileLinkPtnId;
    }

    public void setMstFileLinkPtn(MstFileLinkPtn mstFileLinkPtn) {
        this.mstFileLinkPtn = mstFileLinkPtn;
    }

    public void setFileLinkPtnId(String fileLinkPtnId) {
        this.fileLinkPtnId = fileLinkPtnId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MstMachineAttribute)) {
            return false;
        }
        MstMachineAttribute other = (MstMachineAttribute) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.MstMachineAttribute[ id=" + id + " ]";
    }

}
