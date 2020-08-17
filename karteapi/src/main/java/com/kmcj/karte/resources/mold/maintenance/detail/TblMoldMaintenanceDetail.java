/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.detail;

import com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodeling;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "tbl_mold_maintenance_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldMaintenanceDetail.findAll", query = "SELECT t FROM TblMoldMaintenanceDetail t"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findById", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.id = :id"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMaintenanceId", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.tblMoldMaintenanceDetailPK.maintenanceId = :maintenanceId"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findBySeq", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.tblMoldMaintenanceDetailPK.seq = :seq"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMainteReasonCategory1", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.mainteReasonCategory1 = :mainteReasonCategory1"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMainteReasonCategory1Text", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.mainteReasonCategory1Text = :mainteReasonCategory1Text"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMainteReasonCategory2", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.mainteReasonCategory2 = :mainteReasonCategory2"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMainteReasonCategory2Text", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.mainteReasonCategory2Text = :mainteReasonCategory2Text"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMainteReasonCategory3", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.mainteReasonCategory3 = :mainteReasonCategory3"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMainteReasonCategory3Text", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.mainteReasonCategory3Text = :mainteReasonCategory3Text"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByManiteReason", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.maniteReason = :maniteReason"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMeasureDirectionCategory1", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.measureDirectionCategory1 = :measureDirectionCategory1"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMeasureDirectionCategory1Text", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.measureDirectionCategory1Text = :measureDirectionCategory1Text"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMeasureDirectionCategory2", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.measureDirectionCategory2 = :measureDirectionCategory2"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMeasureDirectionCategory2Text", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.measureDirectionCategory2Text = :measureDirectionCategory2Text"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMeasureDirectionCategory3", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.measureDirectionCategory3 = :measureDirectionCategory3"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMeasureDirectionCategory3Text", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.measureDirectionCategory3Text = :measureDirectionCategory3Text"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByMeasureDirection", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.measureDirection = :measureDirection"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByTaskCategory1", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.taskCategory1 = :taskCategory1"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByTaskCategory1Text", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.taskCategory1Text = :taskCategory1Text"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByTaskCategory2", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.taskCategory2 = :taskCategory2"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByTaskCategory2Text", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.taskCategory2Text = :taskCategory2Text"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByTaskCategory3", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.taskCategory3 = :taskCategory3"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByTaskCategory3Text", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.taskCategory3Text = :taskCategory3Text"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByTask", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.task = :task"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByCreateDate", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByUpdateDate", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByCreateUserUuid", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.findByUpdateUserUuid", query = "SELECT t FROM TblMoldMaintenanceDetail t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.deleteByMaintenanceId", query = "DELETE FROM TblMoldMaintenanceDetail t WHERE t.tblMoldMaintenanceDetailPK.maintenanceId = :maintenanceId"),
    @NamedQuery(name = "TblMoldMaintenanceDetail.deleteById", query = "DELETE FROM TblMoldMaintenanceDetail t WHERE t.id = :id")
})
@Cacheable(value = false)
public class TblMoldMaintenanceDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMoldMaintenanceDetailPK tblMoldMaintenanceDetailPK;
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
    @ManyToOne(optional = false)
    private TblMoldMaintenanceRemodeling tblMoldMaintenanceRemodeling;

    public TblMoldMaintenanceDetail() {
    }

    public TblMoldMaintenanceDetail(TblMoldMaintenanceDetailPK tblMoldMaintenanceDetailPK) {
        this.tblMoldMaintenanceDetailPK = tblMoldMaintenanceDetailPK;
    }

    public TblMoldMaintenanceDetail(String maintenanceId, int seq) {
        this.tblMoldMaintenanceDetailPK = new TblMoldMaintenanceDetailPK(maintenanceId, seq);
    }

    public TblMoldMaintenanceDetailPK getTblMoldMaintenanceDetailPK() {
        return tblMoldMaintenanceDetailPK;
    }

    public void setTblMoldMaintenanceDetailPK(TblMoldMaintenanceDetailPK tblMoldMaintenanceDetailPK) {
        this.tblMoldMaintenanceDetailPK = tblMoldMaintenanceDetailPK;
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

    public TblMoldMaintenanceRemodeling getTblMoldMaintenanceRemodeling() {
        return tblMoldMaintenanceRemodeling;
    }

    public void setTblMoldMaintenanceRemodeling(TblMoldMaintenanceRemodeling tblMoldMaintenanceRemodeling) {
        this.tblMoldMaintenanceRemodeling = tblMoldMaintenanceRemodeling;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tblMoldMaintenanceDetailPK != null ? tblMoldMaintenanceDetailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldMaintenanceDetail)) {
            return false;
        }
        TblMoldMaintenanceDetail other = (TblMoldMaintenanceDetail) object;
        if ((this.tblMoldMaintenanceDetailPK == null && other.tblMoldMaintenanceDetailPK != null) || (this.tblMoldMaintenanceDetailPK != null && !this.tblMoldMaintenanceDetailPK.equals(other.tblMoldMaintenanceDetailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetail[ tblMoldMaintenanceDetailPK=" + tblMoldMaintenanceDetailPK + " ]";
    }

}
