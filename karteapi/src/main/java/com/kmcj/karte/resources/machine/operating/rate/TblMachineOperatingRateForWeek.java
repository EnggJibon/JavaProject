/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.operating.rate;

import com.kmcj.karte.resources.machine.MstMachine;
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
@Table(name = "tbl_machine_operating_rate_per_week")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineOperatingRateForWeek.findAll", query = "SELECT t FROM TblMachineOperatingRateForWeek t"),
    @NamedQuery(name = "TblMachineOperatingRateForWeek.findByMachineUuid", query = "SELECT t FROM TblMachineOperatingRateForWeek t WHERE t.tblMachineOperatingRateForWeekPK.machineUuid = :machineUuid"),
    @NamedQuery(name = "TblMachineOperatingRateForWeek.findByPk", query = "SELECT t FROM TblMachineOperatingRateForWeek t WHERE t.tblMachineOperatingRateForWeekPK.machineUuid = :machineUuid AND t.tblMachineOperatingRateForWeekPK.productionDateStart = :productionDateStart AND t.tblMachineOperatingRateForWeekPK.productionDateEnd = :productionDateEnd"),
    @NamedQuery(name = "TblMachineOperatingRateForWeek.findByProductionDateStart", query = "SELECT t FROM TblMachineOperatingRateForWeek t WHERE t.tblMachineOperatingRateForWeekPK.productionDateStart = :productionDateStart"),
    @NamedQuery(name = "TblMachineOperatingRateForWeek.findByProductionDateEnd", query = "SELECT t FROM TblMachineOperatingRateForWeek t WHERE t.tblMachineOperatingRateForWeekPK.productionDateEnd = :productionDateEnd")
    })
@Cacheable(value = false)
public class TblMachineOperatingRateForWeek implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMachineOperatingRateForWeekPK tblMachineOperatingRateForWeekPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "OPERATING_TIME")
    private long operatingTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DEDUCTION_TIME")
    private long deductionTime;
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
    public MstMachine getMstMachine() {
        return this.mstMachine;
    }
    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public TblMachineOperatingRateForWeek() {
    }

    public TblMachineOperatingRateForWeek(TblMachineOperatingRateForWeekPK tblMachineOperatingRateForWeekPK) {
        this.tblMachineOperatingRateForWeekPK = tblMachineOperatingRateForWeekPK;
    }

    public TblMachineOperatingRateForWeek(TblMachineOperatingRateForWeekPK tblMachineOperatingRateForWeekPK, long operatingTime) {
        this.tblMachineOperatingRateForWeekPK = tblMachineOperatingRateForWeekPK;
        this.operatingTime = operatingTime;
    }

    public TblMachineOperatingRateForWeek(String machineUuid, Date productionDateStart, Date productionDateEnd) {
        this.tblMachineOperatingRateForWeekPK = new TblMachineOperatingRateForWeekPK(machineUuid, productionDateStart, productionDateEnd);
    }

    public TblMachineOperatingRateForWeekPK getTblMachineOperatingRateForWeekPK() {
        return tblMachineOperatingRateForWeekPK;
    }

    public void setTblMachineOperatingRateForWeekPK(TblMachineOperatingRateForWeekPK tblMachineOperatingRateForWeekPK) {
        this.tblMachineOperatingRateForWeekPK = tblMachineOperatingRateForWeekPK;
    }

    public long getOperatingTime() {
        return operatingTime;
    }

    public void setOperatingTime(long operatingTime) {
        this.operatingTime = operatingTime;
    }
    
    public long getDeductionTime() {
        return deductionTime;
    }
    public void setDeductionTime(long deductionTime) {
        this.deductionTime = deductionTime;
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
        hash += (tblMachineOperatingRateForWeekPK != null ? tblMachineOperatingRateForWeekPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineOperatingRateForWeek)) {
            return false;
        }
        TblMachineOperatingRateForWeek other = (TblMachineOperatingRateForWeek) object;
        if ((this.tblMachineOperatingRateForWeekPK == null && other.tblMachineOperatingRateForWeekPK != null) || (this.tblMachineOperatingRateForWeekPK != null && !this.tblMachineOperatingRateForWeekPK.equals(other.tblMachineOperatingRateForWeekPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateForWeek[ tblMachineOperatingRateForWeekPK=" + tblMachineOperatingRateForWeekPK + " ]";
    }
    
}
