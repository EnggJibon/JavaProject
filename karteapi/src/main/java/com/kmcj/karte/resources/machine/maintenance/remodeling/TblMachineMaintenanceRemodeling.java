package com.kmcj.karte.resources.machine.maintenance.remodeling;

import com.kmcj.karte.resources.direction.TblDirection;
import com.kmcj.karte.resources.machine.*;
import com.kmcj.karte.resources.machine.maintenance.detail.TblMachineMaintenanceDetail;
import com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetail;
import com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistory;
import com.kmcj.karte.resources.mold.issue.TblIssue;
import com.kmcj.karte.resources.user.MstUser;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_machine_maintenance_remodeling")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findAll", query = "SELECT t FROM TblMachineMaintenanceRemodeling t"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findById", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByMainteOrRemodel", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.mainteOrRemodel = :mainteOrRemodel"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByMainteType", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.mainteType = :mainteType"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByRemodelingType", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.remodelingType = :remodelingType"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByMainteTypeText", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.mainteTypeText = :mainteTypeText"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByMainteDate", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.mainteDate = :mainteDate"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByStartDatetime", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.startDatetime = :startDatetime"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByStartDatetimeStz", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.startDatetimeStz = :startDatetimeStz"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByEndDatetime", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.endDatetime = :endDatetime"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByEndDatetimeStz", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.endDatetimeStz = :endDatetimeStz"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByReport", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.report = :report"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByCreateDate", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByUpdateDate", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByCreateUserUuid", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findByUpdateUserUuid", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblMachineMaintenanceRemodeling.findMaxEndDateByMachineUuid", query = "SELECT t FROM TblMachineMaintenanceRemodeling t WHERE t.machineUuid = :machineUuid And t.endDatetime IS NOT NULL Order By t.endDatetime DESC")
})
@Cacheable(value = false)
public class TblMachineMaintenanceRemodeling implements Serializable {

    private static long serialVersionUID = 1L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "MAINTE_OR_REMODEL")
    private Integer mainteOrRemodel;
    @Column(name = "MAINTE_TYPE")
    private Integer mainteType;
    @Column(name = "REMODELING_TYPE")
    private Integer remodelingType;
    @Size(max = 100)
    @Column(name = "MAINTE_TYPE_TEXT")
    private String mainteTypeText;
    @Column(name = "MAINTE_DATE")
    @Temporal(TemporalType.DATE)
    private Date mainteDate;
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
    @Size(max = 200)
    @Column(name = "REPORT")
    private String report;
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
    @JoinColumn(name = "MACHINE_SPEC_HST_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachineSpecHistory mstMachineSpecHistory;
    @Column(name = "MACHINE_SPEC_HST_ID")
    private String machineSpecHstId;
    @JoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @JoinColumn(name = "ISSUE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblIssue tblIssue;
    @Column(name = "ISSUE_ID")
    private String issueId;
    @JoinColumn(name = "DIRECTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblDirection tblDirection;
    @Column(name = "DIRECTION_ID")
    private String tblDirectionId;
    @Size(max = 45)
    @Column(name = "DIRECTION_CODE")
    private String tblDirectionCode;
    
    // KM-361 メンテナンス所要時間の追加
    @Column(name = "WORKING_TIME_MINUTES")
    private int workingTimeMinutes;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "tblMachineMaintenanceRemodeling")
    @OrderBy(value = " tblMachineRemodelingDetailPK.seq asc ")
    private Collection<TblMachineRemodelingDetail> tblMachineRemodelingDetailCollection;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "tblMachineMaintenanceRemodeling")
    @OrderBy(value = " tblMachineMaintenanceDetailPK.seq asc ")
    private Collection<TblMachineMaintenanceDetail> tblMachineMaintenanceDetailCollection;
    
    /**
     * 結合テーブル定義
     */
    // ユーザーマスタ
    //@PrimaryKeyJoinColumn(name = "UPDATE_USER_UUID", referencedColumnName = "UUID")
    @PrimaryKeyJoinColumn(name = "CREATE_USER_UUID", referencedColumnName = "UUID") //実施者は開始者なのでCREATE_USERが正しい(Kitaoji)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstUser;
    public MstUser getMstUser() {
        return this.mstUser;
    }
    public void setMstUser(MstUser mstUser) {
        this.mstUser = mstUser;
    }
    
    

    public TblMachineMaintenanceRemodeling() {
    }

    public TblMachineMaintenanceRemodeling(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMainteOrRemodel() {
        return mainteOrRemodel;
    }

    public void setMainteOrRemodel(Integer mainteOrRemodel) {
        this.mainteOrRemodel = mainteOrRemodel;
    }

    public Integer getMainteType() {
        return mainteType;
    }

    public void setMainteType(Integer mainteType) {
        this.mainteType = mainteType;
    }

    public Integer getRemodelingType() {
        return remodelingType;
    }

    public void setRemodelingType(Integer remodelingType) {
        this.remodelingType = remodelingType;
    }

    public String getMainteTypeText() {
        return mainteTypeText;
    }

    public void setMainteTypeText(String mainteTypeText) {
        this.mainteTypeText = mainteTypeText;
    }

    public Date getMainteDate() {
        return mainteDate;
    }

    public void setMainteDate(Date mainteDate) {
        this.mainteDate = mainteDate;
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

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
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

    public void setMstMachineSpecHistory(MstMachineSpecHistory mstMachineSpecHistory) {
        this.mstMachineSpecHistory = mstMachineSpecHistory;
    }

    public void setMachineSpecHstId(String machineSpecHstId) {
        this.machineSpecHstId = machineSpecHstId;
    }

    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public MstMachineSpecHistory getMstMachineSpecHistory() {
        return mstMachineSpecHistory;
    }

    public String getMachineSpecHstId() {
        return machineSpecHstId;
    }

    public MstMachine getMstMachine() {
        return mstMachine;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public TblIssue getTblIssue() {
        return tblIssue;
    }

    public String getIssueId() {
        return issueId;
    }

    public TblDirection getTblDirection() {
        return tblDirection;
    }

    public String getTblDirectionId() {
        return tblDirectionId;
    }

    public void setTblIssue(TblIssue tblIssue) {
        this.tblIssue = tblIssue;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public void setTblDirection(TblDirection tblDirection) {
        this.tblDirection = tblDirection;
    }

    public void setTblDirectionId(String tblDirectionId) {
        this.tblDirectionId = tblDirectionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineMaintenanceRemodeling)) {
            return false;
        }
        TblMachineMaintenanceRemodeling other = (TblMachineMaintenanceRemodeling) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodeling[ id=" + getId() + " ]";
    }

//    /**
//     * @return the tblMachineRemodelingDetail
//     */
//    public TblMachineRemodelingDetail getTblMachineRemodelingDetail() {
//        return tblMachineRemodelingDetail;
//    }
//
//    /**
//     * @param tblMachineRemodelingDetail the tblMachineRemodelingDetail to set
//     */
//    public void setTblMachineRemodelingDetail(TblMachineRemodelingDetail tblMachineRemodelingDetail) {
//        this.tblMachineRemodelingDetail = tblMachineRemodelingDetail;
//    }
//
//    /**
//     * @return the tblMachineMaintenanceDetail
//     */
//    public TblMachineMaintenanceDetail getTblMachineMaintenanceDetail() {
//        return tblMachineMaintenanceDetail;
//    }
//
//    /**
//     * @param tblMachineMaintenanceDetail the tblMachineMaintenanceDetail to set
//     */
//    public void setTblMachineMaintenanceDetail(TblMachineMaintenanceDetail tblMachineMaintenanceDetail) {
//        this.tblMachineMaintenanceDetail = tblMachineMaintenanceDetail;
//    }

    /**
     * @return the tblDirectionCode
     */
    public String getTblDirectionCode() {
        return tblDirectionCode;
    }

    /**
     * @param tblDirectionCode the tblDirectionCode to set
     */
    public void setTblDirectionCode(String tblDirectionCode) {
        this.tblDirectionCode = tblDirectionCode;
    }

    /**
     * @return the workingTimeMinutes
     */
    public int getWorkingTimeMinutes() {
        return workingTimeMinutes;
    }

    /**
     * @param workingTimeMinutes the workingTimeMinutes to set
     */
    public void setWorkingTimeMinutes(int workingTimeMinutes) {
        this.workingTimeMinutes = workingTimeMinutes;
    }

    void setStartDatetime(String javaZoneId, Date startDatetime) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the tblMachineRemodelingDetailCollection
     */
    @XmlTransient
    public Collection<TblMachineRemodelingDetail> getTblMachineRemodelingDetailCollection() {
        return tblMachineRemodelingDetailCollection;
    }

    /**
     * @param tblMachineRemodelingDetailCollection the tblMachineRemodelingDetailCollection to set
     */
    public void setTblMachineRemodelingDetailCollection(Collection<TblMachineRemodelingDetail> tblMachineRemodelingDetailCollection) {
        this.tblMachineRemodelingDetailCollection = tblMachineRemodelingDetailCollection;
    }

    /**
     * @return the tblMachineMaintenanceDetailCollection
     */
    @XmlTransient
    public Collection<TblMachineMaintenanceDetail> getTblMachineMaintenanceDetailCollection() {
        return tblMachineMaintenanceDetailCollection;
    }

    /**
     * @param tblMachineMaintenanceDetailCollection the tblMachineMaintenanceDetailCollection to set
     */
    public void setTblMachineMaintenanceDetailCollection(Collection<TblMachineMaintenanceDetail> tblMachineMaintenanceDetailCollection) {
        this.tblMachineMaintenanceDetailCollection = tblMachineMaintenanceDetailCollection;
    }

}
