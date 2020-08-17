package com.kmcj.karte.resources.machine.maintenance.detail;

import com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodeling;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_machine_maintenance_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineMaintenanceDetail.findAll", query = "SELECT t FROM TblMachineMaintenanceDetail t"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findById", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMaintenanceId", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.tblMachineMaintenanceDetailPK.maintenanceId = :maintenanceId"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findBySeq", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.tblMachineMaintenanceDetailPK.seq = :seq"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMainteReasonCategory1", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.mainteReasonCategory1 = :mainteReasonCategory1"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMainteReasonCategory1Text", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.mainteReasonCategory1Text = :mainteReasonCategory1Text"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMainteReasonCategory2", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.mainteReasonCategory2 = :mainteReasonCategory2"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMainteReasonCategory2Text", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.mainteReasonCategory2Text = :mainteReasonCategory2Text"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMainteReasonCategory3", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.mainteReasonCategory3 = :mainteReasonCategory3"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMainteReasonCategory3Text", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.mainteReasonCategory3Text = :mainteReasonCategory3Text"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByManiteReason", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.maniteReason = :maniteReason"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMeasureDirectionCategory1", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.measureDirectionCategory1 = :measureDirectionCategory1"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMeasureDirectionCategory1Text", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.measureDirectionCategory1Text = :measureDirectionCategory1Text"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMeasureDirectionCategory2", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.measureDirectionCategory2 = :measureDirectionCategory2"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMeasureDirectionCategory2Text", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.measureDirectionCategory2Text = :measureDirectionCategory2Text"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMeasureDirectionCategory3", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.measureDirectionCategory3 = :measureDirectionCategory3"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMeasureDirectionCategory3Text", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.measureDirectionCategory3Text = :measureDirectionCategory3Text"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByMeasureDirection", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.measureDirection = :measureDirection"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByTaskCategory1", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.taskCategory1 = :taskCategory1"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByTaskCategory1Text", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.taskCategory1Text = :taskCategory1Text"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByTaskCategory2", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.taskCategory2 = :taskCategory2"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByTaskCategory2Text", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.taskCategory2Text = :taskCategory2Text"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByTaskCategory3", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.taskCategory3 = :taskCategory3"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByTaskCategory3Text", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.taskCategory3Text = :taskCategory3Text"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByTask", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.task = :task"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByCreateDate", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByUpdateDate", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByCreateUserUuid", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMachineMaintenanceDetail.findByUpdateUserUuid", query = "SELECT t FROM TblMachineMaintenanceDetail t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblMachineMaintenanceDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMachineMaintenanceDetailPK tblMachineMaintenanceDetailPK;
    @Size(max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "MAINTE_REASON_CATEGORY1")
    private Integer mainteReasonCategory1;
    @Size(max = 100)
    @Column(name = "MAINTE_REASON_CATEGORY1_TEXT")
    private String mainteReasonCategory1Text;
    @Column(name = "MAINTE_REASON_CATEGORY2")
    private Integer mainteReasonCategory2;
    @Size(max = 100)
    @Column(name = "MAINTE_REASON_CATEGORY2_TEXT")
    private String mainteReasonCategory2Text;
    @Column(name = "MAINTE_REASON_CATEGORY3")
    private Integer mainteReasonCategory3;
    @Size(max = 100)
    @Column(name = "MAINTE_REASON_CATEGORY3_TEXT")
    private String mainteReasonCategory3Text;
    @Size(max = 200)
    @Column(name = "MANITE_REASON")
    private String maniteReason;
    @Column(name = "MEASURE_DIRECTION_CATEGORY1")
    private Integer measureDirectionCategory1;
    @Size(max = 100)
    @Column(name = "MEASURE_DIRECTION_CATEGORY1_TEXT")
    private String measureDirectionCategory1Text;
    @Column(name = "MEASURE_DIRECTION_CATEGORY2")
    private Integer measureDirectionCategory2;
    @Size(max = 100)
    @Column(name = "MEASURE_DIRECTION_CATEGORY2_TEXT")
    private String measureDirectionCategory2Text;
    @Column(name = "MEASURE_DIRECTION_CATEGORY3")
    private Integer measureDirectionCategory3;
    @Size(max = 100)
    @Column(name = "MEASURE_DIRECTION_CATEGORY3_TEXT")
    private String measureDirectionCategory3Text;
    @Size(max = 200)
    @Column(name = "MEASURE_DIRECTION")
    private String measureDirection;
    @Column(name = "TASK_CATEGORY1")
    private Integer taskCategory1;
    @Size(max = 100)
    @Column(name = "TASK_CATEGORY1_TEXT")
    private String taskCategory1Text;
    @Column(name = "TASK_CATEGORY2")
    private Integer taskCategory2;
    @Size(max = 100)
    @Column(name = "TASK_CATEGORY2_TEXT")
    private String taskCategory2Text;
    @Column(name = "TASK_CATEGORY3")
    private Integer taskCategory3;
    @Size(max = 100)
    @Column(name = "TASK_CATEGORY3_TEXT")
    private String taskCategory3Text;
    @Size(max = 200)
    @Column(name = "TASK")
    private String task;
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
    @JoinColumn(name = "MAINTENANCE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblMachineMaintenanceRemodeling tblMachineMaintenanceRemodeling;

    public TblMachineMaintenanceDetail() {
    }

    public TblMachineMaintenanceDetail(TblMachineMaintenanceDetailPK tblMachineMaintenanceDetailPK) {
        this.tblMachineMaintenanceDetailPK = tblMachineMaintenanceDetailPK;
    }

    public TblMachineMaintenanceDetail(String maintenanceId, int seq) {
        this.tblMachineMaintenanceDetailPK = new TblMachineMaintenanceDetailPK(maintenanceId, seq);
    }

    public TblMachineMaintenanceDetailPK getTblMachineMaintenanceDetailPK() {
        return tblMachineMaintenanceDetailPK;
    }

    public void setTblMachineMaintenanceDetailPK(TblMachineMaintenanceDetailPK tblMachineMaintenanceDetailPK) {
        this.tblMachineMaintenanceDetailPK = tblMachineMaintenanceDetailPK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMainteReasonCategory1() {
        return mainteReasonCategory1;
    }

    public void setMainteReasonCategory1(Integer mainteReasonCategory1) {
        this.mainteReasonCategory1 = mainteReasonCategory1;
    }

    public String getMainteReasonCategory1Text() {
        return mainteReasonCategory1Text;
    }

    public void setMainteReasonCategory1Text(String mainteReasonCategory1Text) {
        this.mainteReasonCategory1Text = mainteReasonCategory1Text;
    }

    public Integer getMainteReasonCategory2() {
        return mainteReasonCategory2;
    }

    public void setMainteReasonCategory2(Integer mainteReasonCategory2) {
        this.mainteReasonCategory2 = mainteReasonCategory2;
    }

    public String getMainteReasonCategory2Text() {
        return mainteReasonCategory2Text;
    }

    public void setMainteReasonCategory2Text(String mainteReasonCategory2Text) {
        this.mainteReasonCategory2Text = mainteReasonCategory2Text;
    }

    public Integer getMainteReasonCategory3() {
        return mainteReasonCategory3;
    }

    public void setMainteReasonCategory3(Integer mainteReasonCategory3) {
        this.mainteReasonCategory3 = mainteReasonCategory3;
    }

    public String getMainteReasonCategory3Text() {
        return mainteReasonCategory3Text;
    }

    public void setMainteReasonCategory3Text(String mainteReasonCategory3Text) {
        this.mainteReasonCategory3Text = mainteReasonCategory3Text;
    }

    public String getManiteReason() {
        return maniteReason;
    }

    public void setManiteReason(String maniteReason) {
        this.maniteReason = maniteReason;
    }

    public Integer getMeasureDirectionCategory1() {
        return measureDirectionCategory1;
    }

    public void setMeasureDirectionCategory1(Integer measureDirectionCategory1) {
        this.measureDirectionCategory1 = measureDirectionCategory1;
    }

    public String getMeasureDirectionCategory1Text() {
        return measureDirectionCategory1Text;
    }

    public void setMeasureDirectionCategory1Text(String measureDirectionCategory1Text) {
        this.measureDirectionCategory1Text = measureDirectionCategory1Text;
    }

    public Integer getMeasureDirectionCategory2() {
        return measureDirectionCategory2;
    }

    public void setMeasureDirectionCategory2(Integer measureDirectionCategory2) {
        this.measureDirectionCategory2 = measureDirectionCategory2;
    }

    public String getMeasureDirectionCategory2Text() {
        return measureDirectionCategory2Text;
    }

    public void setMeasureDirectionCategory2Text(String measureDirectionCategory2Text) {
        this.measureDirectionCategory2Text = measureDirectionCategory2Text;
    }

    public Integer getMeasureDirectionCategory3() {
        return measureDirectionCategory3;
    }

    public void setMeasureDirectionCategory3(Integer measureDirectionCategory3) {
        this.measureDirectionCategory3 = measureDirectionCategory3;
    }

    public String getMeasureDirectionCategory3Text() {
        return measureDirectionCategory3Text;
    }

    public void setMeasureDirectionCategory3Text(String measureDirectionCategory3Text) {
        this.measureDirectionCategory3Text = measureDirectionCategory3Text;
    }

    public String getMeasureDirection() {
        return measureDirection;
    }

    public void setMeasureDirection(String measureDirection) {
        this.measureDirection = measureDirection;
    }

    public Integer getTaskCategory1() {
        return taskCategory1;
    }

    public void setTaskCategory1(Integer taskCategory1) {
        this.taskCategory1 = taskCategory1;
    }

    public String getTaskCategory1Text() {
        return taskCategory1Text;
    }

    public void setTaskCategory1Text(String taskCategory1Text) {
        this.taskCategory1Text = taskCategory1Text;
    }

    public Integer getTaskCategory2() {
        return taskCategory2;
    }

    public void setTaskCategory2(Integer taskCategory2) {
        this.taskCategory2 = taskCategory2;
    }

    public String getTaskCategory2Text() {
        return taskCategory2Text;
    }

    public void setTaskCategory2Text(String taskCategory2Text) {
        this.taskCategory2Text = taskCategory2Text;
    }

    public Integer getTaskCategory3() {
        return taskCategory3;
    }

    public void setTaskCategory3(Integer taskCategory3) {
        this.taskCategory3 = taskCategory3;
    }

    public String getTaskCategory3Text() {
        return taskCategory3Text;
    }

    public void setTaskCategory3Text(String taskCategory3Text) {
        this.taskCategory3Text = taskCategory3Text;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
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

    public TblMachineMaintenanceRemodeling getTblMachineMaintenanceRemodeling() {
        return tblMachineMaintenanceRemodeling;
    }

    public void setTblMachineMaintenanceRemodeling(TblMachineMaintenanceRemodeling tblMachineMaintenanceRemodeling) {
        this.tblMachineMaintenanceRemodeling = tblMachineMaintenanceRemodeling;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblMachineMaintenanceDetailPK != null ? tblMachineMaintenanceDetailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMachineMaintenanceDetail)) {
            return false;
        }
        TblMachineMaintenanceDetail other = (TblMachineMaintenanceDetail) object;
        if ((this.tblMachineMaintenanceDetailPK == null && other.tblMachineMaintenanceDetailPK != null) || (this.tblMachineMaintenanceDetailPK != null && !this.tblMachineMaintenanceDetailPK.equals(other.tblMachineMaintenanceDetailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.TblMachineMaintenanceDetail[ tblMachineMaintenanceDetailPK=" + tblMachineMaintenanceDetailPK + " ]";
    }

}
