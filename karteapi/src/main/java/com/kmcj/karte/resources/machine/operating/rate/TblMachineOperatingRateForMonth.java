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
@Table(name = "tbl_machine_operating_rate_per_month")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineOperatingRateForMonth.findAll", query = "SELECT t FROM TblMachineOperatingRateForMonth t"),
    @NamedQuery(name = "TblMachineOperatingRateForMonth.findByMachineUuid", query = "SELECT t FROM TblMachineOperatingRateForMonth t WHERE t.tblMachineOperatingRateForMonthPK.machineUuid = :machineUuid"),
    @NamedQuery(name = "TblMachineOperatingRateForMonth.findByPk", query = "SELECT t FROM TblMachineOperatingRateForMonth t WHERE t.tblMachineOperatingRateForMonthPK.machineUuid = :machineUuid AND t.tblMachineOperatingRateForMonthPK.productionMonth = :productionMonth"),
    @NamedQuery(name = "TblMachineOperatingRateForMonth.findByProductionMonth", query = "SELECT t FROM TblMachineOperatingRateForMonth t WHERE t.tblMachineOperatingRateForMonthPK.productionMonth = :productionMonth")
   })
@Cacheable(value = false)
public class TblMachineOperatingRateForMonth implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMachineOperatingRateForMonthPK tblMachineOperatingRateForMonthPK;
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

    public TblMachineOperatingRateForMonth() {
    }

    public TblMachineOperatingRateForMonth(TblMachineOperatingRateForMonthPK tblMachineOperatingRateForMonthPK) {
        this.tblMachineOperatingRateForMonthPK = tblMachineOperatingRateForMonthPK;
    }

    public TblMachineOperatingRateForMonth(TblMachineOperatingRateForMonthPK tblMachineOperatingRateForMonthPK, long operatingTime) {
        this.tblMachineOperatingRateForMonthPK = tblMachineOperatingRateForMonthPK;
        this.operatingTime = operatingTime;
    }

    public TblMachineOperatingRateForMonth(String machineUuid, String productionMonth) {
        this.tblMachineOperatingRateForMonthPK = new TblMachineOperatingRateForMonthPK(machineUuid, productionMonth);
    }

    public TblMachineOperatingRateForMonthPK getTblMachineOperatingRateForMonthPK() {
        return tblMachineOperatingRateForMonthPK;
    }

    public void setTblMachineOperatingRateForMonthPK(TblMachineOperatingRateForMonthPK tblMachineOperatingRateForMonthPK) {
        this.tblMachineOperatingRateForMonthPK = tblMachineOperatingRateForMonthPK;
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
        hash += (tblMachineOperatingRateForMonthPK != null ? tblMachineOperatingRateForMonthPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineOperatingRateForMonth)) {
            return false;
        }
        TblMachineOperatingRateForMonth other = (TblMachineOperatingRateForMonth) object;
        if ((this.tblMachineOperatingRateForMonthPK == null && other.tblMachineOperatingRateForMonthPK != null) || (this.tblMachineOperatingRateForMonthPK != null && !this.tblMachineOperatingRateForMonthPK.equals(other.tblMachineOperatingRateForMonthPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateForMonth[ tblMachineOperatingRateForMonthPK=" + tblMachineOperatingRateForMonthPK + " ]";
    }
    
}
