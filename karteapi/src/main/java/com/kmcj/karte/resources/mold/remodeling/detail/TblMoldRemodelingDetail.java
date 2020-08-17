/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.remodeling.detail;

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
@Table(name = "tbl_mold_remodeling_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldRemodelingDetail.findAll", query = "SELECT t FROM TblMoldRemodelingDetail t"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findById", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.id = :id"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByMaintenanceId", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.tblMoldRemodelingDetailPK.maintenanceId = :maintenanceId"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findBySeq", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.tblMoldRemodelingDetailPK.seq = :seq"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelReasonCategory1", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelReasonCategory1 = :remodelReasonCategory1"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelReasonCategory1Text", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelReasonCategory1Text = :remodelReasonCategory1Text"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelReasonCategory2", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelReasonCategory2 = :remodelReasonCategory2"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelReasonCategory2Text", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelReasonCategory2Text = :remodelReasonCategory2Text"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelReasonCategory3", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelReasonCategory3 = :remodelReasonCategory3"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelReasonCategory3Text", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelReasonCategory3Text = :remodelReasonCategory3Text"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelReason", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelReason = :remodelReason"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelDirectionCategory1", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelDirectionCategory1 = :remodelDirectionCategory1"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelDirectionCategory1Text", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelDirectionCategory1Text = :remodelDirectionCategory1Text"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelDirectionCategory2", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelDirectionCategory2 = :remodelDirectionCategory2"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelDirectionCategory2Text", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelDirectionCategory2Text = :remodelDirectionCategory2Text"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelDirectionCategory3", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelDirectionCategory3 = :remodelDirectionCategory3"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelDirectionCategory3Text", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelDirectionCategory3Text = :remodelDirectionCategory3Text"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByRemodelDirection", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.remodelDirection = :remodelDirection"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByTaskCategory1", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.taskCategory1 = :taskCategory1"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByTaskCategory1Text", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.taskCategory1Text = :taskCategory1Text"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByTaskCategory2", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.taskCategory2 = :taskCategory2"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByTaskCategory2Text", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.taskCategory2Text = :taskCategory2Text"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByTaskCategory3", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.taskCategory3 = :taskCategory3"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByTaskCategory3Text", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.taskCategory3Text = :taskCategory3Text"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByTask", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.task = :task"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByCreateDate", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByUpdateDate", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByCreateUserUuid", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByUpdateUserUuid", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblMoldRemodelingDetail.findByMaintenanceIdAndSeq", query = "SELECT t FROM TblMoldRemodelingDetail t WHERE t.tblMoldRemodelingDetailPK.maintenanceId = :maintenanceId And t.tblMoldRemodelingDetailPK.seq = :seq"),
    @NamedQuery(name = "TblMoldRemodelingDetail.DeleteByMaintenanceId", query = "DELETE FROM TblMoldRemodelingDetail t WHERE t.tblMoldRemodelingDetailPK.maintenanceId = :maintenanceId "),
    @NamedQuery(name = "TblMoldRemodelingDetail.DeleteById", query = "DELETE FROM TblMoldRemodelingDetail t WHERE t.id = :id")
})
@Cacheable(value = false)
public class TblMoldRemodelingDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TblMoldRemodelingDetailPK tblMoldRemodelingDetailPK;
    @Size(max = 45)
    @Column(name = "ID")
    private String id;
    @Column(name = "REMODEL_REASON_CATEGORY1")
    private Integer remodelReasonCategory1;
    @Size(max = 100)
    @Column(name = "REMODEL_REASON_CATEGORY1_TEXT")
    private String remodelReasonCategory1Text;
    @Column(name = "REMODEL_REASON_CATEGORY2")
    private Integer remodelReasonCategory2;
    @Size(max = 100)
    @Column(name = "REMODEL_REASON_CATEGORY2_TEXT")
    private String remodelReasonCategory2Text;
    @Column(name = "REMODEL_REASON_CATEGORY3")
    private Integer remodelReasonCategory3;
    @Size(max = 100)
    @Column(name = "REMODEL_REASON_CATEGORY3_TEXT")
    private String remodelReasonCategory3Text;
    @Size(max = 200)
    @Column(name = "REMODEL_REASON")
    private String remodelReason;
    @Column(name = "REMODEL_DIRECTION_CATEGORY1")
    private Integer remodelDirectionCategory1;
    @Size(max = 100)
    @Column(name = "REMODEL_DIRECTION_CATEGORY1_TEXT")
    private String remodelDirectionCategory1Text;
    @Column(name = "REMODEL_DIRECTION_CATEGORY2")
    private Integer remodelDirectionCategory2;
    @Size(max = 100)
    @Column(name = "REMODEL_DIRECTION_CATEGORY2_TEXT")
    private String remodelDirectionCategory2Text;
    @Column(name = "REMODEL_DIRECTION_CATEGORY3")
    private Integer remodelDirectionCategory3;
    @Size(max = 100)
    @Column(name = "REMODEL_DIRECTION_CATEGORY3_TEXT")
    private String remodelDirectionCategory3Text;
    @Size(max = 200)
    @Column(name = "REMODEL_DIRECTION")
    private String remodelDirection;
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

    public TblMoldRemodelingDetail() {
    }

    public TblMoldRemodelingDetail(TblMoldRemodelingDetailPK tblMoldRemodelingDetailPK) {
        this.tblMoldRemodelingDetailPK = tblMoldRemodelingDetailPK;
    }

    public TblMoldRemodelingDetail(String maintenanceId, int seq) {
        this.tblMoldRemodelingDetailPK = new TblMoldRemodelingDetailPK(maintenanceId, seq);
    }

    public TblMoldRemodelingDetailPK getTblMoldRemodelingDetailPK() {
        return tblMoldRemodelingDetailPK;
    }

    public void setTblMoldRemodelingDetailPK(TblMoldRemodelingDetailPK tblMoldRemodelingDetailPK) {
        this.tblMoldRemodelingDetailPK = tblMoldRemodelingDetailPK;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRemodelReasonCategory1() {
        return remodelReasonCategory1;
    }

    public void setRemodelReasonCategory1(Integer remodelReasonCategory1) {
        this.remodelReasonCategory1 = remodelReasonCategory1;
    }

    public String getRemodelReasonCategory1Text() {
        return remodelReasonCategory1Text;
    }

    public void setRemodelReasonCategory1Text(String remodelReasonCategory1Text) {
        this.remodelReasonCategory1Text = remodelReasonCategory1Text;
    }

    public Integer getRemodelReasonCategory2() {
        return remodelReasonCategory2;
    }

    public void setRemodelReasonCategory2(Integer remodelReasonCategory2) {
        this.remodelReasonCategory2 = remodelReasonCategory2;
    }

    public String getRemodelReasonCategory2Text() {
        return remodelReasonCategory2Text;
    }

    public void setRemodelReasonCategory2Text(String remodelReasonCategory2Text) {
        this.remodelReasonCategory2Text = remodelReasonCategory2Text;
    }

    public Integer getRemodelReasonCategory3() {
        return remodelReasonCategory3;
    }

    public void setRemodelReasonCategory3(Integer remodelReasonCategory3) {
        this.remodelReasonCategory3 = remodelReasonCategory3;
    }

    public String getRemodelReasonCategory3Text() {
        return remodelReasonCategory3Text;
    }

    public void setRemodelReasonCategory3Text(String remodelReasonCategory3Text) {
        this.remodelReasonCategory3Text = remodelReasonCategory3Text;
    }

    public String getRemodelReason() {
        return remodelReason;
    }

    public void setRemodelReason(String remodelReason) {
        this.remodelReason = remodelReason;
    }

    public Integer getRemodelDirectionCategory1() {
        return remodelDirectionCategory1;
    }

    public void setRemodelDirectionCategory1(Integer remodelDirectionCategory1) {
        this.remodelDirectionCategory1 = remodelDirectionCategory1;
    }

    public String getRemodelDirectionCategory1Text() {
        return remodelDirectionCategory1Text;
    }

    public void setRemodelDirectionCategory1Text(String remodelDirectionCategory1Text) {
        this.remodelDirectionCategory1Text = remodelDirectionCategory1Text;
    }

    public Integer getRemodelDirectionCategory2() {
        return remodelDirectionCategory2;
    }

    public void setRemodelDirectionCategory2(Integer remodelDirectionCategory2) {
        this.remodelDirectionCategory2 = remodelDirectionCategory2;
    }

    public String getRemodelDirectionCategory2Text() {
        return remodelDirectionCategory2Text;
    }

    public void setRemodelDirectionCategory2Text(String remodelDirectionCategory2Text) {
        this.remodelDirectionCategory2Text = remodelDirectionCategory2Text;
    }

    public Integer getRemodelDirectionCategory3() {
        return remodelDirectionCategory3;
    }

    public void setRemodelDirectionCategory3(Integer remodelDirectionCategory3) {
        this.remodelDirectionCategory3 = remodelDirectionCategory3;
    }

    public String getRemodelDirectionCategory3Text() {
        return remodelDirectionCategory3Text;
    }

    public void setRemodelDirectionCategory3Text(String remodelDirectionCategory3Text) {
        this.remodelDirectionCategory3Text = remodelDirectionCategory3Text;
    }

    public String getRemodelDirection() {
        return remodelDirection;
    }

    public void setRemodelDirection(String remodelDirection) {
        this.remodelDirection = remodelDirection;
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
        hash += (tblMoldRemodelingDetailPK != null ? tblMoldRemodelingDetailPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldRemodelingDetail)) {
            return false;
        }
        TblMoldRemodelingDetail other = (TblMoldRemodelingDetail) object;
        if ((this.tblMoldRemodelingDetailPK == null && other.tblMoldRemodelingDetailPK != null) || (this.tblMoldRemodelingDetailPK != null && !this.tblMoldRemodelingDetailPK.equals(other.tblMoldRemodelingDetailPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetail[ tblMoldRemodelingDetailPK=" + tblMoldRemodelingDetailPK + " ]";
    }
    
}
