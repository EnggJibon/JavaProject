/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.part.stock.maint;

import com.kmcj.karte.resources.user.MstUserMin;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_mold_maintenance_remodeling")
@XmlRootElement
@Cacheable(value = false)
public class MoldMaintenance implements Serializable {

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
    @Size(max = 45)
    @Column(name = "ISSUE_ID")
    private String issueId;
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
    @Size(max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;

    @Size(max = 45)
    @Column(name = "MOLD_SPEC_HST_ID")
    private String moldSpecHstIdStr;
    
    @Size(max = 45)
    @Column(name = "DIRECTION_ID")
    private String directionId;

    @Size(max = 45)
    @Column(name = "DIRECTION_CODE")
    private String directionCode;
    
    // KM-361 メンテナンス所要時間の追加
    @Column(name = "WORKING_TIME_MINUTES")
    private int workingTimeMinutes;
    
    public String getMoldSpecHstIdStr() {
        return moldSpecHstIdStr;
    }

    public void setMoldSpecHstIdStr(String moldSpecHstIdStr) {
        this.moldSpecHstIdStr = moldSpecHstIdStr;
    }

    @JoinColumn(name = "CREATE_USER_UUID", referencedColumnName = "UUID", insertable = false, updatable = false) 
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUserMin createUser; 

    public MstUserMin getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(MstUserMin createUser) {
        this.createUser = createUser;
    }

    @JoinColumn(name = "UPDATE_USER_UUID", referencedColumnName = "UUID", insertable = false, updatable = false) 
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUserMin updateUser; 

    public MstUserMin getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(MstUserMin updateUser) {
        this.updateUser = updateUser;
    }

    @OneToMany(mappedBy = "moldMaintenance")
    @OrderBy(value = " seq ASC ")
    private List<MoldMaintenanceDetail> moldMaintenanceDetails;
    
    @Transient
    private String maintenanceReason;
    

    public MoldMaintenance() {
    }

    public MoldMaintenance(String id) {
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

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public Integer getMainteType() {
        return mainteType;
    }

    public void setMainteType(Integer mainteType) {
        this.mainteType = mainteType;
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

    public String getMoldUuid() {
        return moldUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public Integer getRemodelingType() {
        return remodelingType;
    }

    public void setRemodelingType(Integer remodelingType) {
        this.remodelingType = remodelingType;
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
        if (!(object instanceof MoldMaintenance)) {
            return false;
        }
        MoldMaintenance other = (MoldMaintenance) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.part.stock.MoldMaintenanceRemodeling[ id=" + getId() + " ]";
    }

    /**
     * @return the directionId
     */
    public String getDirectionId() {
        return directionId;
    }

    /**
     * @param directionId the directionId to set
     */
    public void setDirectionId(String directionId) {
        this.directionId = directionId;
    }

    /**
     * @return the directionCode
     */
    public String getDirectionCode() {
        return directionCode;
    }

    /**
     * @param directionCode the directionCode to set
     */
    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
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

    void setStartDatetime(String javaZoneId, Date strToDate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the moldMaintenanceDetails
     */
    public List<MoldMaintenanceDetail> getMoldMaintenanceDetails() {
        return moldMaintenanceDetails;
    }

    /**
     * @param moldMaintenanceDetails the moldMaintenanceDetails to set
     */
    public void setMoldMaintenanceDetails(List<MoldMaintenanceDetail> moldMaintenanceDetails) {
        this.moldMaintenanceDetails = moldMaintenanceDetails;
    }

    /**
     * @return the maintenanceReason
     */
    public String getMaintenanceReason() {
        return maintenanceReason;
    }

    /**
     * @param maintenanceReason the maintenanceReason to set
     */
    public void setMaintenanceReason(String maintenanceReason) {
        this.maintenanceReason = maintenanceReason;
    }


}
