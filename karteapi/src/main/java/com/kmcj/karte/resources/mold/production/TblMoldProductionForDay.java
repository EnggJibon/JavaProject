/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.production;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.mold.MstMold;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
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
@Table(name = "tbl_mold_production_per_day")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldProductionForDay.findAll", query = "SELECT t FROM TblMoldProductionForDay t"),
    @NamedQuery(name = "TblMoldProductionForDay.findByComponentId", query = "SELECT t FROM TblMoldProductionForDay t WHERE t.tblMoldProductionForDayPK.componentId = :componentId"),
    @NamedQuery(name = "TblMoldProductionForDay.findByMoldUuid", query = "SELECT t FROM TblMoldProductionForDay t WHERE t.tblMoldProductionForDayPK.moldUuid = :moldUuid"),
    @NamedQuery(name = "TblMoldProductionForDay.findByPk", query = "SELECT t FROM TblMoldProductionForDay t WHERE t.tblMoldProductionForDayPK.moldUuid = :moldUuid AND t.tblMoldProductionForDayPK.componentId = :componentId AND t.tblMoldProductionForDayPK.productionDate = :productionDate"),
    @NamedQuery(name = "TblMoldProductionForDay.findByProductionDate", query = "SELECT t FROM TblMoldProductionForDay t WHERE t.tblMoldProductionForDayPK.productionDate = :productionDate")
   })
@Cacheable(value = false)
public class TblMoldProductionForDay implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMoldProductionForDayPK tblMoldProductionForDayPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPLETED_COUNT")
    private long completedCount;
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
    
    // 金型マスタ
    @PrimaryKeyJoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mstMold;

    public MstMold getMstMold() {
        return this.mstMold;
    }

    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }

    // 部品マスタ
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;

    public MstComponent getMstComponent() {
        return this.mstComponent;
    }

    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }
    
    public TblMoldProductionForDay() {
    }

    public TblMoldProductionForDay(TblMoldProductionForDayPK tblMoldProductionForDayPK) {
        this.tblMoldProductionForDayPK = tblMoldProductionForDayPK;
    }

    public TblMoldProductionForDay(TblMoldProductionForDayPK tblMoldProductionForDayPK, long completedCount) {
        this.tblMoldProductionForDayPK = tblMoldProductionForDayPK;
        this.completedCount = completedCount;
    }

    public TblMoldProductionForDay(String componentId, String moldUuid, Date productionDate) {
        this.tblMoldProductionForDayPK = new TblMoldProductionForDayPK(componentId, moldUuid, productionDate);
    }

    public TblMoldProductionForDayPK getTblMoldProductionForDayPK() {
        return tblMoldProductionForDayPK;
    }

    public void setTblMoldProductionForDayPK(TblMoldProductionForDayPK tblMoldProductionForDayPK) {
        this.tblMoldProductionForDayPK = tblMoldProductionForDayPK;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(long completedCount) {
        this.completedCount = completedCount;
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
        hash += (tblMoldProductionForDayPK != null ? tblMoldProductionForDayPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldProductionForDay)) {
            return false;
        }
        TblMoldProductionForDay other = (TblMoldProductionForDay) object;
        if ((this.tblMoldProductionForDayPK == null && other.tblMoldProductionForDayPK != null) || (this.tblMoldProductionForDayPK != null && !this.tblMoldProductionForDayPK.equals(other.tblMoldProductionForDayPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.production.TblMoldProductionForDay[ tblMoldProductionForDayPK=" + tblMoldProductionForDayPK + " ]";
    }
    
}
