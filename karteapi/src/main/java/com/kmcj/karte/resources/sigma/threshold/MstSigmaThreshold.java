package com.kmcj.karte.resources.sigma.threshold;

import com.kmcj.karte.resources.machine.filedef.MstMachineFileDef;
import com.kmcj.karte.resources.machine.*;
import com.kmcj.karte.resources.component.MstComponent;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "mst_sigma_threshold")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstSigmaThreshold.findAll", query = "SELECT m FROM MstSigmaThreshold m"),
    @NamedQuery(name = "MstSigmaThreshold.findById", query = "SELECT m FROM MstSigmaThreshold m WHERE m.id = :id"),
    @NamedQuery(name = "MstSigmaThreshold.findByMaxVal", query = "SELECT m FROM MstSigmaThreshold m WHERE m.maxVal = :maxVal"),
    @NamedQuery(name = "MstSigmaThreshold.findByMinVal", query = "SELECT m FROM MstSigmaThreshold m WHERE m.minVal = :minVal"),
    @NamedQuery(name = "MstSigmaThreshold.findByCreateDate", query = "SELECT m FROM MstSigmaThreshold m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstSigmaThreshold.findByUpdateDate", query = "SELECT m FROM MstSigmaThreshold m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstSigmaThreshold.findByCreateUserUuid", query = "SELECT m FROM MstSigmaThreshold m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstSigmaThreshold.findByUpdateUserUuid", query = "SELECT m FROM MstSigmaThreshold m WHERE m.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class MstSigmaThreshold implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MAX_VAL")
    private BigDecimal maxVal;
    @Column(name = "MIN_VAL")
    private BigDecimal minVal;
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
    @JoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    @Column(name = "COMPONENT_ID")
    private String componentId;
    @JoinColumn(name = "FILE_DEF_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MstMachineFileDef mstMachineFileDef;
    @Column(name = "FILE_DEF_ID")
    private String fileDefId;

    public MstSigmaThreshold() {
    }

    public MstSigmaThreshold(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(BigDecimal maxVal) {
        this.maxVal = maxVal;
    }

    public BigDecimal getMinVal() {
        return minVal;
    }

    public void setMinVal(BigDecimal minVal) {
        this.minVal = minVal;
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

    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public void setMstMachineFileDef(MstMachineFileDef mstMachineFileDef) {
        this.mstMachineFileDef = mstMachineFileDef;
    }

    public void setFileDefId(String fileDefId) {
        this.fileDefId = fileDefId;
    }

    public MstComponent getMstComponent() {
        return mstComponent;
    }

    public String getComponentId() {
        return componentId;
    }

    public MstMachineFileDef getMstMachineFileDef() {
        return mstMachineFileDef;
    }

    public String getFileDefId() {
        return fileDefId;
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
        if (!(object instanceof MstSigmaThreshold)) {
            return false;
        }
        MstSigmaThreshold other = (MstSigmaThreshold) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.MstSigmaThreshold[ id=" + id + " ]";
    }

}
