/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.daily.report;

import com.kmcj.karte.resources.machine.MstMachine;
import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.user.MstUser;
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
 * @author zds
 */
@Entity
@Table(name = "tbl_machine_daily_report")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineDailyReport.findAll", query = "SELECT t FROM TblMachineDailyReport t"),
    @NamedQuery(name = "TblMachineDailyReport.findById", query = "SELECT t FROM TblMachineDailyReport t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineDailyReport.delete", query = "DELETE FROM TblMachineDailyReport t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineDailyReport.findFkByMachineUuid", query = "SELECT t FROM TblMachineDailyReport t WHERE t.machineUuid = :machineUuid"),
    @NamedQuery(name = "TblMachineDailyReport.findByProductionId", query = "SELECT t FROM TblMachineDailyReport t WHERE t.tblMachineDailyReportPK.productionId = :productionId"),
    @NamedQuery(name = "TblMachineDailyReport.findByProductionIdAndDate", query = "SELECT t FROM TblMachineDailyReport t JOIN FETCH t.tblProduction WHERE t.tblMachineDailyReportPK.productionId = :productionId AND t.tblMachineDailyReportPK.productionDate = :productionDate "),
//    @NamedQuery(name = "TblMachineDailyReport.findByProductionIdAndStartDate", query = "SELECT t FROM TblMachineDailyReport t WHERE t.tblMachineDailyReportPK.productionId = :productionId AND t.startDatetime = :startDatetime "),
    @NamedQuery(name = "TblMachineDailyReport.findByReporterUuid", query = "SELECT t FROM TblMachineDailyReport t WHERE t.reporterUuid = :reporterUuid"),
    @NamedQuery(name = "TblMachineDailyReport.findByProductionDate", query = "SELECT t FROM TblMachineDailyReport t WHERE t.tblMachineDailyReportPK.productionDate = :productionDate"),
    @NamedQuery(name = "TblMachineDailyReport.findByStartDatetime", query = "SELECT t FROM TblMachineDailyReport t WHERE t.startDatetime = :startDatetime"),
    @NamedQuery(name = "TblMachineDailyReport.findByStartDatetimeStz", query = "SELECT t FROM TblMachineDailyReport t WHERE t.startDatetimeStz = :startDatetimeStz"),
    @NamedQuery(name = "TblMachineDailyReport.findByEndDatetime", query = "SELECT t FROM TblMachineDailyReport t WHERE t.endDatetime = :endDatetime"),
    @NamedQuery(name = "TblMachineDailyReport.findByEndDatetimeStz", query = "SELECT t FROM TblMachineDailyReport t WHERE t.endDatetimeStz = :endDatetimeStz"),
    @NamedQuery(name = "TblMachineDailyReport.findByProducingTimeMinutes", query = "SELECT t FROM TblMachineDailyReport t WHERE t.producingTimeMinutes = :producingTimeMinutes"),
    @NamedQuery(name = "TblMachineDailyReport.findBySuspendedTimeMinutes", query = "SELECT t FROM TblMachineDailyReport t WHERE t.suspendedTimeMinutes = :suspendedTimeMinutes"),
    @NamedQuery(name = "TblMachineDailyReport.findByNetProducintTimeMinutes", query = "SELECT t FROM TblMachineDailyReport t WHERE t.netProducintTimeMinutes = :netProducintTimeMinutes"),
    @NamedQuery(name = "TblMachineDailyReport.findByShotCount", query = "SELECT t FROM TblMachineDailyReport t WHERE t.shotCount = :shotCount"),
    @NamedQuery(name = "TblMachineDailyReport.findByDisposedShotCount", query = "SELECT t FROM TblMachineDailyReport t WHERE t.disposedShotCount = :disposedShotCount"),
    @NamedQuery(name = "TblMachineDailyReport.findByMessageForNextDay", query = "SELECT t FROM TblMachineDailyReport t WHERE t.messageForNextDay = :messageForNextDay"),
    @NamedQuery(name = "TblMachineDailyReport.findByCreateDate", query = "SELECT t FROM TblMachineDailyReport t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMachineDailyReport.findByUpdateDate", query = "SELECT t FROM TblMachineDailyReport t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMachineDailyReport.findByCreateUserUuid", query = "SELECT t FROM TblMachineDailyReport t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMachineDailyReport.findByUpdateUserUuid", query = "SELECT t FROM TblMachineDailyReport t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblMachineDailyReport.findMaxMoldEndDateTimeForNotEndProdunction", query = "SELECT t FROM TblMachineDailyReport t JOIN FETCH t.tblProduction WHERE t.tblProduction.moldUuid = :moldUuid AND t.tblProduction.endDatetime IS NULL ORDER BY t.endDatetime DESC"),
    @NamedQuery(name = "TblMachineDailyReport.findMaxMachineEndDateTimeForNotEndProdunction", query = "SELECT t FROM TblMachineDailyReport t JOIN FETCH t.tblProduction WHERE t.machineUuid = :machineUuid AND t.tblProduction.endDatetime IS NULL ORDER BY t.endDatetime DESC"),
})
@Cacheable(value = false)
public class TblMachineDailyReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMachineDailyReportPK tblMachineDailyReportPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Size(max = 45)
    @Column(name = "REPORTER_UUID")
    private String reporterUuid;
    @Column(name = "START_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDatetime;
    @Column(name = "START_DATETIME_STZ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDatetimeStz;
    @Column(name = "END_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDatetime;
    @Column(name = "END_DATETIME_STZ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDatetimeStz;
    @Column(name = "PRODUCING_TIME_MINUTES")
    private Integer producingTimeMinutes;
    @Column(name = "SUSPENDED_TIME_MINUTES")
    private Integer suspendedTimeMinutes;
    @Column(name = "NET_PRODUCINT_TIME_MINUTES")
    private Integer netProducintTimeMinutes;
    @Size(max = 45)
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SHOT_COUNT")
    private int shotCount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DISPOSED_SHOT_COUNT")
    private int disposedShotCount;
    @Size(max = 100)
    @Column(name = "MESSAGE_FOR_NEXT_DAY")
    private String messageForNextDay;
    @NotNull
    @Column(name = "NO_REGISTRATION_FLAG")
    private int noRegistrationFlag;
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

    /**
     * 結合テーブル定義
     */
    // 生産実績
    @PrimaryKeyJoinColumn(name = "PRODUCTION_ID", referencedColumnName = "ID")  
    @ManyToOne
    private TblProduction tblProduction;
    public TblProduction getTblProduction() {
        return this.tblProduction;
    }
    public void setTblProduction(TblProduction tblProduction) {
        this.tblProduction = tblProduction;
    }
    
    /**
     * 結合テーブル定義
     */
    // ユーザーマスタ
    @PrimaryKeyJoinColumn(name = "REPORTER_UUID", referencedColumnName = "UUID")
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstUser;
    public MstUser getMstUser() {
        return this.mstUser;
    }
    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }
    
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
    
    
    
    public TblMachineDailyReport() {
    }

    public TblMachineDailyReport(TblMachineDailyReportPK tblMachineDailyReportPK) {
        this.tblMachineDailyReportPK = tblMachineDailyReportPK;
    }

    public TblMachineDailyReport(TblMachineDailyReportPK tblMachineDailyReportPK, String id, int shotCount, int disposedShotCount) {
        this.tblMachineDailyReportPK = tblMachineDailyReportPK;
        this.id = id;
        this.shotCount = shotCount;
        this.disposedShotCount = disposedShotCount;
    }

    public TblMachineDailyReport(String productionId, Date productionDate) {
        this.tblMachineDailyReportPK = new TblMachineDailyReportPK(productionId, productionDate);
    }

    public TblMachineDailyReportPK getTblMachineDailyReportPK() {
        return tblMachineDailyReportPK;
    }

    public void setTblMachineDailyReportPK(TblMachineDailyReportPK tblMachineDailyReportPK) {
        this.tblMachineDailyReportPK = tblMachineDailyReportPK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReporterUuid() {
        return reporterUuid;
    }

    public void setReporterUuid(String reporterUuid) {
        this.reporterUuid = reporterUuid;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Date getStartDatetimeStz() {
        return startDatetimeStz;
    }

    public void setStartDatetimeStz(Date startDatetimeStz) {
        this.startDatetimeStz = startDatetimeStz;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Date getEndDatetimeStz() {
        return endDatetimeStz;
    }

    public void setEndDatetimeStz(Date endDatetimeStz) {
        this.endDatetimeStz = endDatetimeStz;
    }

    public Integer getProducingTimeMinutes() {
        return producingTimeMinutes;
    }

    public void setProducingTimeMinutes(Integer producingTimeMinutes) {
        this.producingTimeMinutes = producingTimeMinutes;
    }

    public Integer getSuspendedTimeMinutes() {
        return suspendedTimeMinutes;
    }

    public void setSuspendedTimeMinutes(Integer suspendedTimeMinutes) {
        this.suspendedTimeMinutes = suspendedTimeMinutes;
    }

    public Integer getNetProducintTimeMinutes() {
        return netProducintTimeMinutes;
    }

    public void setNetProducintTimeMinutes(Integer netProducintTimeMinutes) {
        this.netProducintTimeMinutes = netProducintTimeMinutes;
    }

    /**
     * @return the machineUuid
     */
    public String getMachineUuid() {
        return machineUuid;
    }

    /**
     * @param machineUuid the machineUuid to set
     */
    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }
    
    public int getShotCount() {
        return shotCount;
    }

    public void setShotCount(int shotCount) {
        this.shotCount = shotCount;
    }

    public int getDisposedShotCount() {
        return disposedShotCount;
    }

    public void setDisposedShotCount(int disposedShotCount) {
        this.disposedShotCount = disposedShotCount;
    }

    public String getMessageForNextDay() {
        return messageForNextDay;
    }

    public void setMessageForNextDay(String messageForNextDay) {
        this.messageForNextDay = messageForNextDay;
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
        hash += (tblMachineDailyReportPK != null ? tblMachineDailyReportPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof TblMachineDailyReport)) {
            return false;
        }
        TblMachineDailyReport other = (TblMachineDailyReport) object;
        if ((this.tblMachineDailyReportPK == null && other.tblMachineDailyReportPK != null) || (this.tblMachineDailyReportPK != null && !this.tblMachineDailyReportPK.equals(other.tblMachineDailyReportPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.daily.report.TblMachineDailyReport[ tblMachineDailyReportPK=" + tblMachineDailyReportPK + " ]";
    }

    /**
     * @return the noRegistrationFlag
     */
    public int getNoRegistrationFlag() {
        return noRegistrationFlag;
    }

    /**
     * @param noRegistrationFlag the noRegistrationFlag to set
     */
    public void setNoRegistrationFlag(int noRegistrationFlag) {
        this.noRegistrationFlag = noRegistrationFlag;
    }
    
}
