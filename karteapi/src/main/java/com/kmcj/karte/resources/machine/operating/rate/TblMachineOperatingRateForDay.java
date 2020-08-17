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
@Table(name = "tbl_machine_operating_rate_per_day")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineOperatingRateForDay.findAll", query = "SELECT t FROM TblMachineOperatingRateForDay t"),
    @NamedQuery(name = "TblMachineOperatingRateForDay.findByMachineUuid", query = "SELECT t FROM TblMachineOperatingRateForDay t WHERE t.tblMachineOperatingRateForDayPK.machineUuid = :machineUuid"),
    @NamedQuery(name = "TblMachineOperatingRateForDay.findByPk", query = "SELECT t FROM TblMachineOperatingRateForDay t WHERE t.tblMachineOperatingRateForDayPK.machineUuid = :machineUuid AND t.tblMachineOperatingRateForDayPK.productionDate = :productionDate"),
    @NamedQuery(name = "TblMachineOperatingRateForDay.findByProductionDate", query = "SELECT t FROM TblMachineOperatingRateForDay t WHERE t.tblMachineOperatingRateForDayPK.productionDate = :productionDate")
})
@Cacheable(value = false)
public class TblMachineOperatingRateForDay implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMachineOperatingRateForDayPK tblMachineOperatingRateForDayPK;
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

    
    public TblMachineOperatingRateForDay() {
    }

    public TblMachineOperatingRateForDay(TblMachineOperatingRateForDayPK tblMachineOperatingRateForDayPK) {
        this.tblMachineOperatingRateForDayPK = tblMachineOperatingRateForDayPK;
    }

    public TblMachineOperatingRateForDay(TblMachineOperatingRateForDayPK tblMachineOperatingRateForDayPK, long operatingTime) {
        this.tblMachineOperatingRateForDayPK = tblMachineOperatingRateForDayPK;
        this.operatingTime = operatingTime;
    }

    public TblMachineOperatingRateForDay(String machineUuid, Date productionDate) {
        this.tblMachineOperatingRateForDayPK = new TblMachineOperatingRateForDayPK(machineUuid, productionDate);
    }

    public TblMachineOperatingRateForDayPK getTblMachineOperatingRateForDayPK() {
        return tblMachineOperatingRateForDayPK;
    }

    public void setTblMachineOperatingRateForDayPK(TblMachineOperatingRateForDayPK tblMachineOperatingRateForDayPK) {
        this.tblMachineOperatingRateForDayPK = tblMachineOperatingRateForDayPK;
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
        hash += (tblMachineOperatingRateForDayPK != null ? tblMachineOperatingRateForDayPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineOperatingRateForDay)) {
            return false;
        }
        TblMachineOperatingRateForDay other = (TblMachineOperatingRateForDay) object;
        if ((this.tblMachineOperatingRateForDayPK == null && other.tblMachineOperatingRateForDayPK != null) || (this.tblMachineOperatingRateForDayPK != null && !this.tblMachineOperatingRateForDayPK.equals(other.tblMachineOperatingRateForDayPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateForDay[ tblMachineOperatingRateForDayPK=" + tblMachineOperatingRateForDayPK + " ]";
    }
    
}
