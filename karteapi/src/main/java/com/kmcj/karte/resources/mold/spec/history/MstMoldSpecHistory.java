/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.spec.history;

import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodeling;
import com.kmcj.karte.resources.mold.spec.MstMoldSpec;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "mst_mold_spec_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MstMoldSpecHistory.findAll", query = "SELECT m FROM MstMoldSpecHistory m"),
    @NamedQuery(name = "MstMoldSpecHistory.findById", query = "SELECT m FROM MstMoldSpecHistory m WHERE m.id = :id"),
    @NamedQuery(name = "MstMoldSpecHistory.findByStartDate", query = "SELECT m FROM MstMoldSpecHistory m WHERE m.startDate = :startDate"),
    @NamedQuery(name = "MstMoldSpecHistory.findByEndDate", query = "SELECT m FROM MstMoldSpecHistory m WHERE m.endDate = :endDate"),
    @NamedQuery(name = "MstMoldSpecHistory.findByMoldSpecName", query = "SELECT m FROM MstMoldSpecHistory m WHERE m.moldSpecName = :moldSpecName"),
    @NamedQuery(name = "MstMoldSpecHistory.findByCreateDate", query = "SELECT m FROM MstMoldSpecHistory m WHERE m.createDate = :createDate"),
    @NamedQuery(name = "MstMoldSpecHistory.findByUpdateDate", query = "SELECT m FROM MstMoldSpecHistory m WHERE m.updateDate = :updateDate"),
    @NamedQuery(name = "MstMoldSpecHistory.findByCreateUserUuid", query = "SELECT m FROM MstMoldSpecHistory m WHERE m.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "MstMoldSpecHistory.findByUpdateUserUuid", query = "SELECT m FROM MstMoldSpecHistory m WHERE m.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "MstMoldSpecHistory.findMoldSpec", query = "SELECT m.id,m.mstMold.uuid,m.startDate,m.endDate,m.moldSpecName "
            + "FROM MstMoldSpecHistory m "
            + "JOIN FETCH MstMold "
            + "WHERE m.mstMold.uuid = :moldUuid "
            + "AND m.mstMold.moldId = :moldId"),
    @NamedQuery(name = "MstMoldSpecHistory.delete", query = "DELETE FROM MstMoldSpecHistory m WHERE m.mstMold.uuid = :moldUuid"),
    @NamedQuery(name = "MstMoldSpecHistory.findByMoldId", query = "SELECT m FROM MstMoldSpecHistory m WHERE m.mstMold.moldId = :moldId Order by m.endDate desc"),
    @NamedQuery(name = "MstMoldSpecHistory.findByMoldUuid", query = "SELECT m FROM MstMoldSpecHistory m WHERE m.mstMold.uuid = :moldUuid"),
    @NamedQuery(name = "MstMoldSpecHistory.findByIdForNewEndDate", query = "SELECT m.id,m.mstMold.uuid "
            + ",m.startDate"
            + ",MAX(m.endDate) AS endDate"
            + ",m.moldSpecName "
            + "FROM MstMoldSpecHistory m WHERE m.mstMold.uuid = :moldUuid")
})
@Cacheable(value = false)
public class MstMoldSpecHistory implements Serializable {

    @OneToMany(mappedBy = "moldSpecHstId", cascade = CascadeType.REMOVE)
    private Collection<TblMoldMaintenanceRemodeling> tblMoldMaintenanceRemodelingCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mstMoldSpecHistory")
    private Collection<MstMoldSpec> mstMoldSpecCollection;

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
    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Size(max = 50)
    @Column(name = "MOLD_SPEC_NAME")
    private String moldSpecName;
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
    @Size(max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MstMold mstMold;

    public MstMoldSpecHistory() {
    }

    public MstMoldSpecHistory(String id) {
        this.id = id;
    }

    public MstMoldSpecHistory(String id, Date startDate, Date endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getMoldSpecName() {
        return moldSpecName;
    }

    public void setMoldSpecName(String moldSpecName) {
        this.moldSpecName = moldSpecName;
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
        if (!(object instanceof MstMoldSpecHistory)) {
            return false;
        }
        MstMoldSpecHistory other = (MstMoldSpecHistory) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistory[ id=" + getId() + " ]";
    }

    /**
     * @return the mstMold
     */
    public MstMold getMstMold() {
        return mstMold;
    }

    /**
     * @param mstMold the mstMold to set
     */
    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }

    @XmlTransient
    public Collection<TblMoldMaintenanceRemodeling> getTblMoldMaintenanceRemodelingCollection() {
        return tblMoldMaintenanceRemodelingCollection;
    }

    public void setTblMoldMaintenanceRemodelingCollection(Collection<TblMoldMaintenanceRemodeling> tblMoldMaintenanceRemodelingCollection) {
        this.tblMoldMaintenanceRemodelingCollection = tblMoldMaintenanceRemodelingCollection;
    }

    /**
     * @return the moldUuid
     */
    public String getMoldUuid() {
        return moldUuid;
    }

    /**
     * @param moldUuid the moldUuid to set
     */
    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }
    
    @XmlTransient
    public Collection<MstMoldSpec> getMstMoldSpecCollection() {
        return mstMoldSpecCollection;
    }

    public void setMstMoldSpecCollection(Collection<MstMoldSpec> mstMoldSpecCollection) {
        this.mstMoldSpecCollection = mstMoldSpecCollection;
    }

}
