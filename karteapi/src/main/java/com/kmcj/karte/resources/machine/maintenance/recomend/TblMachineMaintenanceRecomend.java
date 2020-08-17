/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.maintenance.recomend;

import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtn;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
@Table(name = "tbl_machine_maintenance_recomend")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineMaintenanceRecomend.findById", query = "SELECT t FROM TblMachineMaintenanceRecomend t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineMaintenanceRecomend.deleteByMainteCycleId", query = "DELETE FROM TblMachineMaintenanceRecomend t WHERE t.mainteCycleId = :mainteCycleId AND t.maintainedFlag = 0"),
    @NamedQuery(name = "TblMachineMaintenanceRecomend.deleteByMainteMachineUuid", query = "DELETE FROM TblMachineMaintenanceRecomend t WHERE t.machineUuid = :machineUuid AND t.maintainedFlag = 0"),
    @NamedQuery(name = "TblMachineMaintenanceRecomend.delete", query = "DELETE FROM TblMachineMaintenanceRecomend t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineMaintenanceRecomend.chkExists", query = "SELECT t FROM TblMachineMaintenanceRecomend t WHERE t.machineUuid = :machineUuid AND t.alertMainteType = :alertMainteType AND t.maintainedFlag = :maintainedFlag"),
    @NamedQuery(name = "TblMachineMaintenanceRecomend.findRecomendList", query = "SELECT machineRecomend FROM TblMachineMaintenanceRecomend machineRecomend JOIN FETCH machineRecomend.mstMachine mstMachine WHERE machineRecomend.alertMainteType = 2 AND machineRecomend.maintainedFlag = 0 AND (mstMachine.mainteStatus = 0 OR mstMachine.mainteStatus IS NULL)"),
    @NamedQuery(name = "TblMachineMaintenanceRecomend.findByUuid", query = "SELECT machineRecomend FROM TblMachineMaintenanceRecomend machineRecomend WHERE machineRecomend.maintainedFlag = 0 AND machineRecomend.machineUuid = :machineUuid")
})
@Cacheable(value = false)
public class TblMachineMaintenanceRecomend implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "MAINTE_CYCLE_ID")
    private String mainteCycleId;
    @Size(max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @Column(name = "ALERT_MAINTE_TYPE")
    private Integer alertMainteType;
    @Column(name = "MAINTAINED_FLAG")
    private Integer maintainedFlag;
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
    
    // KM-360 メンテナンスサイクル判定でヒットした条件がわかるようにする
    @Column(name = "HIT_CONDITION")
    private int hitCondition;

    /**
     * 結合テーブル定義
     */
    // メンテナンスサイクルパターン
    @PrimaryKeyJoinColumn(name = "MAINTE_CYCLE_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblMaintenanceCyclePtn tblMaintenanceCyclePtn;

    // 設備マスタ
    @PrimaryKeyJoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID")
    @ManyToOne
    private MstMachine mstMachine;

    public TblMachineMaintenanceRecomend() {
    }

    public TblMachineMaintenanceRecomend(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public Integer getAlertMainteType() {
        return alertMainteType;
    }

    public void setAlertMainteType(Integer alertMainteType) {
        this.alertMainteType = alertMainteType;
    }

    public Integer getMaintainedFlag() {
        return maintainedFlag;
    }

    public void setMaintainedFlag(Integer maintainedFlag) {
        this.maintainedFlag = maintainedFlag;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineMaintenanceRecomend)) {
            return false;
        }
        TblMachineMaintenanceRecomend other = (TblMachineMaintenanceRecomend) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.maintenance.recomend.TblMachineMaintenanceRecomend[ id=" + id + " ]";
    }

    /**
     * @return the mainteCycleId
     */
    public String getMainteCycleId() {
        return mainteCycleId;
    }

    /**
     * @param mainteCycleId the mainteCycleId to set
     */
    public void setMainteCycleId(String mainteCycleId) {
        this.mainteCycleId = mainteCycleId;
    }

    /**
     * @return the tblMaintenanceCyclePtn
     */
    public TblMaintenanceCyclePtn getTblMaintenanceCyclePtn() {
        return tblMaintenanceCyclePtn;
    }

    /**
     * @param tblMaintenanceCyclePtn the tblMaintenanceCyclePtn to set
     */
    public void setTblMaintenanceCyclePtn(TblMaintenanceCyclePtn tblMaintenanceCyclePtn) {
        this.tblMaintenanceCyclePtn = tblMaintenanceCyclePtn;
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
     * @return the hitCondition
     */
    public int getHitCondition() {
        return hitCondition;
    }

    /**
     * @param hitCondition the hitCondition to set
     */
    public void setHitCondition(int hitCondition) {
        this.hitCondition = hitCondition;
    }
}
