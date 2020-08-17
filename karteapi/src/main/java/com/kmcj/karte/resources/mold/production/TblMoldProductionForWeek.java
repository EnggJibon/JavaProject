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
@Table(name = "tbl_mold_production_per_week")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldProductionForWeek.findAll", query = "SELECT t FROM TblMoldProductionForWeek t"),
    @NamedQuery(name = "TblMoldProductionForWeek.findByComponentId", query = "SELECT t FROM TblMoldProductionForWeek t WHERE t.tblMoldProductionForWeekPK.componentId = :componentId"),
    @NamedQuery(name = "TblMoldProductionForWeek.findByMoldUuid", query = "SELECT t FROM TblMoldProductionForWeek t WHERE t.tblMoldProductionForWeekPK.moldUuid = :moldUuid"),
    @NamedQuery(name = "TblMoldProductionForWeek.findByPk", query = "SELECT t FROM TblMoldProductionForWeek t WHERE t.tblMoldProductionForWeekPK.moldUuid = :moldUuid AND  t.tblMoldProductionForWeekPK.componentId = :componentId AND t.tblMoldProductionForWeekPK.productionDateStart = :productionDateStart AND t.tblMoldProductionForWeekPK.productionDateEnd = :productionDateEnd"),
    @NamedQuery(name = "TblMoldProductionForWeek.findByProductionDateStart", query = "SELECT t FROM TblMoldProductionForWeek t WHERE t.tblMoldProductionForWeekPK.productionDateStart = :productionDateStart"),
    @NamedQuery(name = "TblMoldProductionForWeek.findByProductionDateEnd", query = "SELECT t FROM TblMoldProductionForWeek t WHERE t.tblMoldProductionForWeekPK.productionDateEnd = :productionDateEnd")
    })
@Cacheable(value = false)
public class TblMoldProductionForWeek implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMoldProductionForWeekPK tblMoldProductionForWeekPK;
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
    
    public TblMoldProductionForWeek() {
    }

    public TblMoldProductionForWeek(TblMoldProductionForWeekPK tblMoldProductionForWeekPK) {
        this.tblMoldProductionForWeekPK = tblMoldProductionForWeekPK;
    }

    public TblMoldProductionForWeek(TblMoldProductionForWeekPK tblMoldProductionForWeekPK, long completedCount) {
        this.tblMoldProductionForWeekPK = tblMoldProductionForWeekPK;
        this.completedCount = completedCount;
    }

    public TblMoldProductionForWeek(String componentId, String moldUuid, Date productionDateStart, Date productionDateEnd) {
        this.tblMoldProductionForWeekPK = new TblMoldProductionForWeekPK(componentId, moldUuid, productionDateStart, productionDateEnd);
    }

    public TblMoldProductionForWeekPK getTblMoldProductionForWeekPK() {
        return tblMoldProductionForWeekPK;
    }

    public void setTblMoldProductionForWeekPK(TblMoldProductionForWeekPK tblMoldProductionForWeekPK) {
        this.tblMoldProductionForWeekPK = tblMoldProductionForWeekPK;
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
        hash += (tblMoldProductionForWeekPK != null ? tblMoldProductionForWeekPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldProductionForWeek)) {
            return false;
        }
        TblMoldProductionForWeek other = (TblMoldProductionForWeek) object;
        if ((this.tblMoldProductionForWeekPK == null && other.tblMoldProductionForWeekPK != null) || (this.tblMoldProductionForWeekPK != null && !this.tblMoldProductionForWeekPK.equals(other.tblMoldProductionForWeekPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.production.TblMoldProductionForWeek[ tblMoldProductionForWeekPK=" + tblMoldProductionForWeekPK + " ]";
    }
    
}
