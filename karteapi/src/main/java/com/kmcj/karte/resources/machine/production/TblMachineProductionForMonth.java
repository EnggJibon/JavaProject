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
@Table(name = "tbl_machine_production_per_month")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineProductionForMonth.findAll", query = "SELECT t FROM TblMachineProductionForMonth t"),
    @NamedQuery(name = "TblMachineProductionForMonth.findByMachineUuid", query = "SELECT t FROM TblMachineProductionForMonth t WHERE t.tblMachineProductionForMonthPK.machineUuid = :machineUuid"),
    @NamedQuery(name = "TblMachineProductionForMonth.findByComponentId", query = "SELECT t FROM TblMachineProductionForMonth t WHERE t.tblMachineProductionForMonthPK.componentId = :componentId"),
    @NamedQuery(name = "TblMachineProductionForMonth.findByPk", query = "SELECT t FROM TblMachineProductionForMonth t WHERE t.tblMachineProductionForMonthPK.machineUuid = :machineUuid AND t.tblMachineProductionForMonthPK.componentId = :componentId AND t.tblMachineProductionForMonthPK.moldUuid = :moldUuid AND t.tblMachineProductionForMonthPK.productionMonth = :productionMonth"),
    @NamedQuery(name = "TblMachineProductionForMonth.findByProductionMonth", query = "SELECT t FROM TblMachineProductionForMonth t WHERE t.tblMachineProductionForMonthPK.productionMonth = :productionMonth")
    })
@Cacheable(value = false)
public class TblMachineProductionForMonth implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMachineProductionForMonthPK tblMachineProductionForMonthPK;
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
    
    @PrimaryKeyJoinColumn(name = "COMPONENT_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstComponent mstComponent;
    
    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMold mstMold;
    
    @PrimaryKeyJoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;

    public TblMachineProductionForMonth() {
    }

    public TblMachineProductionForMonth(TblMachineProductionForMonthPK tblMachineProductionForMonthPK) {
        this.tblMachineProductionForMonthPK = tblMachineProductionForMonthPK;
    }

    public TblMachineProductionForMonth(TblMachineProductionForMonthPK tblMachineProductionForMonthPK, long completedCount) {
        this.tblMachineProductionForMonthPK = tblMachineProductionForMonthPK;
        this.completedCount = completedCount;
    }

    public TblMachineProductionForMonth(String machineUuid, String componentId, String moldUuid, String productionMonth) {
        this.tblMachineProductionForMonthPK = new TblMachineProductionForMonthPK(machineUuid, componentId, moldUuid, productionMonth);
    }

    public TblMachineProductionForMonthPK getTblMachineProductionForMonthPK() {
        return tblMachineProductionForMonthPK;
    }

    public void setTblMachineProductionForMonthPK(TblMachineProductionForMonthPK tblMachineProductionForMonthPK) {
        this.tblMachineProductionForMonthPK = tblMachineProductionForMonthPK;
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
        hash += (tblMachineProductionForMonthPK != null ? tblMachineProductionForMonthPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineProductionForMonth)) {
            return false;
        }
        TblMachineProductionForMonth other = (TblMachineProductionForMonth) object;
        if ((this.tblMachineProductionForMonthPK == null && other.tblMachineProductionForMonthPK != null) || (this.tblMachineProductionForMonthPK != null && !this.tblMachineProductionForMonthPK.equals(other.tblMachineProductionForMonthPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.production.TblMachineProductionForMonth[ tblMachineProductionForMonthPK=" + tblMachineProductionForMonthPK + " ]";
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
    
}
