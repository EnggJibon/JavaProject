package com.kmcj.karte.resources.component.inspection.file;
import com.kmcj.karte.util.XmlDateAdapter;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Entity
@Table(name = "mst_component_inspect_file_name")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstComponentInspectFileName.updateNameById", query = "UPDATE MstComponentInspectFileName m SET "
            + "m.fileName = :fileName,"
            + "m.updateDate = :updateDate,"
            + "m.updateUserUuid = :updateUserUuid  WHERE m.pk.id = :id AND m.pk.ownerCompanyId = :ownerCompanyId"),
    @NamedQuery(name = "MstComponentInspectFileName.findByOwnerCompanyId", query = "SELECT fn FROM MstComponentInspectFileName fn WHERE fn.pk.ownerCompanyId = :ownerCompanyId"),
    @NamedQuery(name = "MstComponentInspectFileName.findNotPushed", query = "SELECT fn FROM MstComponentInspectFileName fn WHERE fn.pk.ownerCompanyId = 'SELF' AND NOT EXISTS(SELECT b FROM MstInspectExtBatchStatus b WHERE b.pk.id = fn.pk.id AND b.pk.companyId = :companyId AND b.batUpdStatus = :batUpdStatus)")
})
@Cacheable(value = false)
public class MstComponentInspectFileName  implements Serializable {
    @EmbeddedId
    private PK pk;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FILE_NAME")
    private String fileName;
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

    public PK getPk() {
        return pk;
    }

    public void setPk(PK pk) {
        this.pk = pk;
    }

    public String getFileName() { return this.fileName; }

    public void setFileName(String fileName) { this.fileName = fileName; }

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

    public MstComponentInspectFileName() {
        this.pk = new PK();
    }
    
    @Embeddable
    public static class PK implements Serializable {
        @NotNull
        @Size(min = 1, max = 45)
        @Column(name = "ID")
        private String id;
        @NotNull
        @Column(name = "OWNER_COMPANY_ID")
        private String ownerCompanyId = "SELF";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOwnerCompanyId() {
            return ownerCompanyId;
        }

        public void setOwnerCompanyId(String ownerCompanyId) {
            this.ownerCompanyId = ownerCompanyId;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 53 * hash + Objects.hashCode(this.id);
            hash = 53 * hash + Objects.hashCode(this.ownerCompanyId);
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
            final PK other = (PK) obj;
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            if (!Objects.equals(this.ownerCompanyId, other.ownerCompanyId)) {
                return false;
            }
            return true;
        }
    }
}