/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.production;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.mold.MstMold;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
@Table(name = "tbl_machine_production_per_week")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineProductionForWeek.findAll", query = "SELECT t FROM TblMachineProductionForWeek t"),
    @NamedQuery(name = "TblMachineProductionForWeek.findByMachineUuid", query = "SELECT t FROM TblMachineProductionForWeek t WHERE t.tblMachineProductionForWeekPK.machineUuid = :machineUuid"),
    @NamedQuery(name = "TblMachineProductionForWeek.findByComponentId", query = "SELECT t FROM TblMachineProductionForWeek t WHERE t.tblMachineProductionForWeekPK.componentId = :componentId"),
    @NamedQuery(name = "TblMachineProductionForWeek.findByPk", query = "SELECT t FROM TblMachineProductionForWeek t WHERE t.tblMachineProductionForWeekPK.machineUuid = :machineUuid AND t.tblMachineProductionForWeekPK.componentId = :componentId AND t.tblMachineProductionForWeekPK.moldUuid = :moldUuid AND t.tblMachineProductionForWeekPK.productionDateStart = :productionDateStart AND t.tblMachineProductionForWeekPK.productionDateEnd = :productionDateEnd"),
    @NamedQuery(name = "TblMachineProductionForWeek.findByProductionDateStart", query = "SELECT t FROM TblMachineProductionForWeek t WHERE t.tblMachineProductionForWeekPK.productionDateStart = :productionDateStart"),
    @NamedQuery(name = "TblMachineProductionForWeek.findByProductionDateEnd", query = "SELECT t FROM TblMachineProductionForWeek t WHERE t.tblMachineProductionForWeekPK.productionDateEnd = :productionDateEnd")
})
@Cacheable(value = false)
public class TblMachineProductionForWeek implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMachineProductionForWeekPK tblMachineProductionForWeekPK;
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
    
    // 設備マスタ
    @PrimaryKeyJoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;

    // 金型マスタ
    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mstMold;

    // 部品マスタ
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    

    public TblMachineProductionForWeek() {
    }

    public TblMachineProductionForWeek(TblMachineProductionForWeekPK tblMachineProductionForWeekPK) {
        this.tblMachineProductionForWeekPK = tblMachineProductionForWeekPK;
    }

    public TblMachineProductionForWeek(TblMachineProductionForWeekPK tblMachineProductionForWeekPK, long completedCount) {
        this.tblMachineProductionForWeekPK = tblMachineProductionForWeekPK;
        this.completedCount = completedCount;
    }

    public TblMachineProductionForWeek(String machineUuid, String componentId, String moldUuid, Date productionDateStart, Date productionDateEnd) {
        this.tblMachineProductionForWeekPK = new TblMachineProductionForWeekPK(machineUuid, componentId, moldUuid, productionDateStart, productionDateEnd);
    }

    public TblMachineProductionForWeekPK getTblMachineProductionForWeekPK() {
        return tblMachineProductionForWeekPK;
    }

    public void setTblMachineProductionForWeekPK(TblMachineProductionForWeekPK tblMachineProductionForWeekPK) {
        this.tblMachineProductionForWeekPK = tblMachineProductionForWeekPK;
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
        hash += (tblMachineProductionForWeekPK != null ? tblMachineProductionForWeekPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineProductionForWeek)) {
            return false;
        }
        TblMachineProductionForWeek other = (TblMachineProductionForWeek) object;
        if ((this.tblMachineProductionForWeekPK == null && other.tblMachineProductionForWeekPK != null) || (this.tblMachineProductionForWeekPK != null && !this.tblMachineProductionForWeekPK.equals(other.tblMachineProductionForWeekPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.production.TblMachineProductionForWeek[ tblMachineProductionForWeekPK=" + tblMachineProductionForWeekPK + " ]";
    }

    /**
     * @return the mstComponent
     */
    public MstComponent getMstComponent() {
        return mstComponent;
    }

    /**
     * @param mstComponent the mstComponent to set
     */
    public void setMstComponent(MstComponent mstComponent) {
        this.mstComponent = mstComponent;
    }

    /**
     * @return the mstMachine
     */
    public MstMachine getMstMachine() {
        return mstMachine;
    }

    /**
     * @param mstMachine the mstMachine to set
     */
    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
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

}
