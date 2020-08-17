/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.suspension;

import com.kmcj.karte.resources.production.TblProduction;
import com.kmcj.karte.resources.work.TblWork;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author zds
 */
@Entity
@Table(name = "tbl_production_suspension")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblProductionSuspension.findAll", query = "SELECT t FROM TblProductionSuspension t"),
    @NamedQuery(name = "TblProductionSuspension.findById", query = "SELECT t FROM TblProductionSuspension t WHERE t.id = :id"),
    @NamedQuery(name = "TblProductionSuspension.findByStartDatetime", query = "SELECT t FROM TblProductionSuspension t WHERE t.startDatetime = :startDatetime"),
    @NamedQuery(name = "TblProductionSuspension.findByStartDatetimeStz", query = "SELECT t FROM TblProductionSuspension t WHERE t.startDatetimeStz = :startDatetimeStz"),
    @NamedQuery(name = "TblProductionSuspension.findByWorkIdAndEndDatetimeIsNull", query = "SELECT t FROM TblProductionSuspension t WHERE t.workId = :workId AND t.endDatetime IS NULL ORDER BY t.startDatetime DESC "),
    @NamedQuery(name = "TblProductionSuspension.findByProductionIdAndEndDatetimeIsNull", query = "SELECT t FROM TblProductionSuspension t WHERE t.productionId = :productionId AND t.endDatetime IS NULL ORDER BY t.startDatetime DESC "),
    @NamedQuery(name = "TblProductionSuspension.findByEndDatetime", query = "SELECT t FROM TblProductionSuspension t WHERE t.endDatetime = :endDatetime"),
    @NamedQuery(name = "TblProductionSuspension.findByEndDatetimeStz", query = "SELECT t FROM TblProductionSuspension t WHERE t.endDatetimeStz = :endDatetimeStz"),
    @NamedQuery(name = "TblProductionSuspension.findBySuspendedTimeMinutes", query = "SELECT t FROM TblProductionSuspension t WHERE t.suspendedTimeMinutes = :suspendedTimeMinutes"),
    @NamedQuery(name = "TblProductionSuspension.findBySuspendReason", query = "SELECT t FROM TblProductionSuspension t WHERE t.suspendReason = :suspendReason"),
    @NamedQuery(name = "TblProductionSuspension.findBySuspendReasonText", query = "SELECT t FROM TblProductionSuspension t WHERE t.suspendReasonText = :suspendReasonText"),
    @NamedQuery(name = "TblProductionSuspension.findByWorkId", query = "SELECT t FROM TblProductionSuspension t WHERE t.workId = :workId"),
    @NamedQuery(name = "TblProductionSuspension.findByCreateDate", query = "SELECT t FROM TblProductionSuspension t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblProductionSuspension.findByUpdateDate", query = "SELECT t FROM TblProductionSuspension t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblProductionSuspension.findByCreateUserUuid", query = "SELECT t FROM TblProductionSuspension t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblProductionSuspension.findByUpdateUserUuid", query = "SELECT t FROM TblProductionSuspension t WHERE t.updateUserUuid = :updateUserUuid")})
@Cacheable(value = false)
public class TblProductionSuspension implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "PRODUCTION_ID")
    private String productionId;
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
    @Column(name = "SUSPENDED_TIME_MINUTES")
    private Integer suspendedTimeMinutes;
    @Column(name = "SUSPEND_REASON")
    private Integer suspendReason;
    @Size(max = 100)
    @Column(name = "SUSPEND_REASON_TEXT")
    private String suspendReasonText;
    @Size(max = 45)
    @Column(name = "WORK_ID")
    private String workId;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private TblProduction tblProduction;
    public TblProduction getTblProduction() {
        return this.tblProduction;
    }
    public void setTblProduction(TblProduction tblProduction) {
        this.tblProduction = tblProduction;
    }
    
    @PrimaryKeyJoinColumn(name = "WORK_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblWork tblWork;

    public TblWork getTblWork() {
        return tblWork;
    }

    public void setTblWork(TblWork tblWork) {
        this.tblWork = tblWork;
    }
    
    @Transient
    private Integer interruptionFlag;    // 判定フラグ
    
    @Transient
    private Integer workEnd;    // 作業終了フラグ
    
    
    public TblProductionSuspension() {
    }

    public TblProductionSuspension(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
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

    public Integer getSuspendedTimeMinutes() {
        return suspendedTimeMinutes;
    }

    public void setSuspendedTimeMinutes(Integer suspendedTimeMinutes) {
        this.suspendedTimeMinutes = suspendedTimeMinutes;
    }

    public Integer getSuspendReason() {
        return suspendReason;
    }

    public void setSuspendReason(Integer suspendReason) {
        this.suspendReason = suspendReason;
    }

    public String getSuspendReasonText() {
        return suspendReasonText;
    }

    public void setSuspendReasonText(String suspendReasonText) {
        this.suspendReasonText = suspendReasonText;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
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

        if (!(object instanceof TblProductionSuspension)) {
            return false;
        }
        TblProductionSuspension other = (TblProductionSuspension) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.production.suspension.TblProductionSuspension[ id=" + id + " ]";
    }

    /**
     * @return the interruptionFlag
     */
    public Integer getInterruptionFlag() {
        return interruptionFlag;
    }

    /**
     * @param interruptionFlag the interruptionFlag to set
     */
    public void setInterruptionFlag(Integer interruptionFlag) {
        this.interruptionFlag = interruptionFlag;
    }

    /**
     * @return the workEnd
     */
    public Integer getWorkEnd() {
        return workEnd;
    }

    /**
     * @param workEnd the workEnd to set
     */
    public void setWorkEnd(Integer workEnd) {
        this.workEnd = workEnd;
    }

}
