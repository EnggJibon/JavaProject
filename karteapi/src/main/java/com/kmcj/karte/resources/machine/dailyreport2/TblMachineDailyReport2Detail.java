/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.dailyreport2;

import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.machine.downtime.MstMachineDowntime;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.work.TblWork;
import com.kmcj.karte.resources.work.phase.MstWorkPhase;
import com.kmcj.karte.util.TimezoneConverter;
import com.kmcj.karte.util.XmlDateAdapter2;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author f.kitaoji
 */
@Entity
@Table(name = "tbl_machine_daily_report2_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineDailyReport2Detail.findAll", query = "SELECT t FROM TblMachineDailyReport2Detail t"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findById", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByStartDatetime", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.startDatetime = :startDatetime"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByStartDatetimeStz", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.startDatetimeStz = :startDatetimeStz"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByEndDatetime", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.endDatetime = :endDatetime"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByEndDatetimeStz", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.endDatetimeStz = :endDatetimeStz"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByDurationMinitues", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.durationMinitues = :durationMinitues"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByOperatingFlg", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.operatingFlg = :operatingFlg"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByDetailType", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.detailType = :detailType"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByWorkId", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.workId = :workId"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByProductionId", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.productionId = :productionId"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByShotCount", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.shotCount = :shotCount"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByDisposedShotCount", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.disposedShotCount = :disposedShotCount"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByWorkPhaseId", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.workPhaseId = :workPhaseId"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByDowntimeComment", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.downtimeComment = :downtimeComment"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByCreateDate", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByUpdateDate", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByCreateUserUuid", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMachineDailyReport2Detail.findByUpdateUserUuid", query = "SELECT t FROM TblMachineDailyReport2Detail t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblMachineDailyReport2Detail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "START_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter2.class)
    private Date startDatetime;
    @Column(name = "START_DATETIME_STZ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDatetimeStz;
    @Column(name = "END_DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlJavaTypeAdapter(XmlDateAdapter2.class)
    private Date endDatetime;
    @Column(name = "END_DATETIME_STZ")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDatetimeStz;
    @Column(name = "DURATION_MINITUES")
    private int durationMinitues;
    @Column(name = "OPERATING_FLG")
    private int operatingFlg;
    @Column(name = "DETAIL_TYPE")
    private int detailType;
    @Size(max = 45)
    @Column(name = "WORK_ID")
    private String workId;
    @Size(max = 45)
    @Column(name = "PRODUCTION_ID")
    private String productionId;
    @Column(name = "PRODUCTION_END_FLG")
    private int productionEndFlg;
    @Column(name = "SHOT_COUNT")
    private int shotCount;
    @Column(name = "DISPOSED_SHOT_COUNT")
    private int disposedShotCount;
    @Size(max = 45)
    @Column(name = "WORK_PHASE_ID")
    private String workPhaseId;
    @Size(max = 45)
    @Column(name = "FIRST_COMPONENT_ID")
    private String firstComponentId;
    @Size(max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
    @Size(max = 45)
    @Column(name = "WORKER_UUID")
    private String workerUuid;
    @Size(max = 45)
    @Column(name = "MACHINE_DOWNTIME_ID")
    private String machineDowntimeId;
    @Size(max = 200)
    @Column(name = "DOWNTIME_COMMENT")
    private String downtimeComment;
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
    @Column(name = "REPORT_ID")
    private String reportId;
    @JoinColumn(name = "REPORT_ID", referencedColumnName = "ID",  insertable = false, updatable = false)
    @ManyToOne
    private TblMachineDailyReport2 report;
    @OneToMany(mappedBy = "reportDetail")
    @XmlElement(name = "machineDailyReportProdDetails")
    private List<TblMachineDailyReport2ProdDetail> tblMachineDailyReport2ProdDetailList;

    @JoinColumn(name = "WORK_PHASE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstWorkPhase mstWorkPhase;
    
    @JoinColumn(name = "MACHINE_DOWNTIME_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstMachineDowntime mstMachineDowntime;

    @JoinColumn(name = "WORKER_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne
    private MstUser mstUser;

    @JoinColumn(name = "FIRST_COMPONENT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstComponent mstComponent;

    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne
    private MstMold mstMold;

    //@JoinColumn(name = "WORK_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    //@ManyToOne
    //private TblWork tblWork;

    @Transient
    private String work;
    @Transient
    private String workerName;
    @Transient
    private String firstComponentCode;
    @Transient
    private String firstComponentName;
    @Transient
    private String moldId;
    @Transient
    private String moldName;
    @Transient
    private int workEndFlg;
    
    @Transient
    private boolean deleted = false;    // 削除対象制御
    @Transient
    private boolean modified = false;   // 更新対象制御
    @Transient
    private boolean added = false;      // 登録対象制御
    
    @Transient
    private Integer productionProdDepartment;
    @Transient
    private String directionCode;
    
    @Transient
    private String startDatetimeStr;

    @Transient
    private String endDatetimeStr;
    
    @Transient
    private String lotNumber;

    public String getStartDatetimeStr() {
        return startDatetimeStr;
    }

    public void setStartDatetimeStr(String startDatetimeStr) {
        this.startDatetimeStr = startDatetimeStr;
    }

    public String getEndDatetimeStr() {
        return endDatetimeStr;
    }

    public void setEndDatetimeStr(String endDatetimeStr) {
        this.endDatetimeStr = endDatetimeStr;
    }
    
    public TblMachineDailyReport2Detail() {
    }

    public TblMachineDailyReport2Detail(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getDurationMinitues() {
        return durationMinitues;
    }

    public void setDurationMinitues(int durationMinitues) {
        this.durationMinitues = durationMinitues;
    }

    public int getOperatingFlg() {
        return operatingFlg;
    }

    public void setOperatingFlg(int operatingFlg) {
        this.operatingFlg = operatingFlg;
    }

    public int getDetailType() {
        return detailType;
    }

    public void setDetailType(int detailType) {
        this.detailType = detailType;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
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

    public String getWorkPhaseId() {
        return workPhaseId;
    }

    public void setWorkPhaseId(String workPhaseId) {
        this.workPhaseId = workPhaseId;
    }

    public String getDowntimeComment() {
        return downtimeComment;
    }

    public void setDowntimeComment(String downtimeComment) {
        this.downtimeComment = downtimeComment;
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

    @XmlTransient
    public TblMachineDailyReport2 getReport() {
        return report;
    }

    public void setReport(TblMachineDailyReport2 report) {
        this.report = report;
    }

    //@XmlTransient
    public List<TblMachineDailyReport2ProdDetail> getTblMachineDailyReport2ProdDetailList() {
        return tblMachineDailyReport2ProdDetailList;
    }

    public void setTblMachineDailyReport2ProdDetailList(List<TblMachineDailyReport2ProdDetail> tblMachineDailyReport2ProdDetailList) {
        this.tblMachineDailyReport2ProdDetailList = tblMachineDailyReport2ProdDetailList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't tblWork in the case the id fields are not set
        if (!(object instanceof TblMachineDailyReport2Detail)) {
            return false;
        }
        TblMachineDailyReport2Detail other = (TblMachineDailyReport2Detail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.dailyreport2.TblMachineDailyReport2Detail[ id=" + id + " ]";
    }

    /**
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * @return the modified
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * @return the added
     */
    public boolean isAdded() {
        return added;
    }

    /**
     * @param added the added to set
     */
    public void setAdded(boolean added) {
        this.added = added;
    }

    /**
     * @return the tblWork
     */
//    public TblWork getTblWork() {
//        return tblWork;
//    }

    /**
     * @param tblWork the tblWork to set
     */
//    public void setTblWork(TblWork tblWork) {
//        this.tblWork = tblWork;
//    }

    /**
     * @return the work
     */
    public String getWork() {
        return work;
    }

    /**
     * @return the mstWorkPhase
     */
    @XmlTransient
    public MstWorkPhase getMstWorkPhase() {
        return mstWorkPhase;
    }

    /**
     * @param mstWorkPhase the mstWorkPhase to set
     */
    public void setMstWorkPhase(MstWorkPhase mstWorkPhase) {
        this.mstWorkPhase = mstWorkPhase;
    }

    /**
     * @param work the work to set
     */
    public void setWork(String work) {
        this.work = work;
    }

    /**
     * @return the machineDowntimeId
     */
    public String getMachineDowntimeId() {
        return machineDowntimeId;
    }

    /**
     * @param machineDowntimeId the machineDowntimeId to set
     */
    public void setMachineDowntimeId(String machineDowntimeId) {
        this.machineDowntimeId = machineDowntimeId;
    }

    /**
     * @return the mstMachineDowntime
     */
    @XmlTransient
    public MstMachineDowntime getMstMachineDowntime() {
        return mstMachineDowntime;
    }

    /**
     * @param mstMachineDowntime the mstMachineDowntime to set
     */
    public void setMstMachineDowntime(MstMachineDowntime mstMachineDowntime) {
        this.mstMachineDowntime = mstMachineDowntime;
    }

    /**
     * @return the workerUuid
     */
    public String getWorkerUuid() {
        return workerUuid;
    }

    /**
     * @param workerUuid the workerUuid to set
     */
    public void setWorkerUuid(String workerUuid) {
        this.workerUuid = workerUuid;
    }

    /**
     * @return the mstUser
     */
    @XmlTransient
    public MstUser getMstUser() {
        return mstUser;
    }

    /**
     * @param mstUser the mstUser to set
     */
    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }

    /**
     * @return the workerName
     */
    public String getWorkerName() {
        return workerName;
    }

    /**
     * @param workerName the workerName to set
     */
    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    /**
     * @return the mstComponent
     */
    @XmlTransient
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
    @XmlTransient
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
     * @return the firstComponentId
     */
    public String getFirstComponentId() {
        return firstComponentId;
    }

    /**
     * @param firstComponentId the firstComponentId to set
     */
    public void setFirstComponentId(String firstComponentId) {
        this.firstComponentId = firstComponentId;
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

    /**
     * @return the firstComponentCode
     */
    public String getFirstComponentCode() {
        return firstComponentCode;
    }

    /**
     * @param firstComponentCode the firstComponentCode to set
     */
    public void setFirstComponentCode(String firstComponentCode) {
        this.firstComponentCode = firstComponentCode;
    }

    /**
     * @return the firstComponentName
     */
    public String getFirstComponentName() {
        return firstComponentName;
    }

    /**
     * @param firstComponentName the firstComponentName to set
     */
    public void setFirstComponentName(String firstComponentName) {
        this.firstComponentName = firstComponentName;
    }

    /**
     * @return the moldId
     */
    public String getMoldId() {
        return moldId;
    }

    /**
     * @param moldId the moldId to set
     */
    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    /**
     * @return the moldName
     */
    public String getMoldName() {
        return moldName;
    }

    /**
     * @param moldName the moldName to set
     */
    public void setMoldName(String moldName) {
        this.moldName = moldName;
    }
    
    public void sortProdByComponentCode() {
        if (this.tblMachineDailyReport2ProdDetailList == null) return;
        //EclipseLinkのバグ回避のため、別のListに移してからソート。
        List<TblMachineDailyReport2ProdDetail> prodList = new ArrayList<>();
        for (TblMachineDailyReport2ProdDetail prod : tblMachineDailyReport2ProdDetailList) {
            prodList.add(prod);
        }
        //部品コードでソート
        Collections.sort(prodList, new Comparator<TblMachineDailyReport2ProdDetail>(){
            @Override
            public int compare(TblMachineDailyReport2ProdDetail a, TblMachineDailyReport2ProdDetail b){
                if (a.getComponentCode() == null && b.getComponentCode() == null) return 0;
                else if (a.getComponentCode() == null && b.getComponentCode() != null) return 1;
                else if (a.getComponentCode() != null && b.getComponentCode() == null) return -1;
                else return a.getComponentCode().compareTo( b.getComponentCode() );  
            }
        });
        //元のリストに戻す
        tblMachineDailyReport2ProdDetailList = prodList;
    }

    public void formatJson(boolean sortProdDetailByComponentCode, Date inReportDate) {
        if (this.getMstWorkPhase() != null) {
            this.setWork(this.getMstWorkPhase().getWorkPhaseName());
        }
        else if (this.getMstMachineDowntime() != null) {
            this.setWork(this.getMstMachineDowntime().getDowntimeReason());
        }
        if (this.getMstUser() != null) {
            this.setWorkerName(this.getMstUser().getUserName());
        }
        if (this.getMstComponent() != null){
            this.setFirstComponentCode(this.getMstComponent().getComponentCode());
            this.setFirstComponentName(this.getMstComponent().getComponentName());
        }
        if (this.getMstMold() != null){
            this.setMoldId(this.getMstMold().getMoldId());
            this.setMoldName(this.getMstMold().getMoldName());
        }
        if (this.tblMachineDailyReport2ProdDetailList != null) {
            for (TblMachineDailyReport2ProdDetail prod : this.tblMachineDailyReport2ProdDetailList) {
                prod.formatJson(inReportDate);
            }
        }
        if (sortProdDetailByComponentCode) {
            sortProdByComponentCode();
        }
        
    }
    
    public void formatJson(Date inReportDate) {
        formatJson(true, inReportDate);
    }
    
    public void calcDurationMinutes() {
        long diffMills = this.endDatetime.getTime() - this.startDatetime.getTime();
        long diffMinutes = diffMills / (1000 * 60);
        this.durationMinitues = (int)diffMinutes;
    }
    
    public void setStzTimes(String userTimeZone) {
        if (userTimeZone == null) return;
        if (this.startDatetime != null) {
            this.startDatetimeStz = TimezoneConverter.toSystemDefaultZoneTime(userTimeZone, startDatetime);
        }
        if (this.endDatetime != null) {
            this.endDatetimeStz = TimezoneConverter.toSystemDefaultZoneTime(userTimeZone, endDatetime);
        }
    }

    /**
     * @return the reportId
     */
    public String getReportId() {
        return reportId;
    }

    /**
     * @param reportId the reportId to set
     */
    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    /**
     * @return the productionEndFlg
     */
    public int getProductionEndFlg() {
        return productionEndFlg;
    }

    /**
     * @param productionEndFlg the productionEndFlg to set
     */
    public void setProductionEndFlg(int productionEndFlg) {
        this.productionEndFlg = productionEndFlg;
    }

    /**
     * @return the workEndFlg
     */
    public int getWorkEndFlg() {
        return workEndFlg;
    }

    /**
     * @param workEndFlg the workEndFlg to set
     */
    public void setWorkEndFlg(int workEndFlg) {
        this.workEndFlg = workEndFlg;
    }

    public Integer getProductionProdDepartment() {
        return productionProdDepartment;
    }

    public void setProductionProdDepartment(Integer productionProdDepartment) {
        this.productionProdDepartment = productionProdDepartment;
    }

    public String getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }
    
}
