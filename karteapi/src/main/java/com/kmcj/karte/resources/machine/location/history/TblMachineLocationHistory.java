package com.kmcj.karte.resources.machine.location.history;

import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.machine.*;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "tbl_machine_location_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMachineLocationHistory.findAll", query = "SELECT t FROM TblMachineLocationHistory t"),
    @NamedQuery(name = "TblMachineLocationHistory.findById", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.id = :id"),
    @NamedQuery(name = "TblMachineLocationHistory.findByStartDate", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.startDate = :startDate"),
    @NamedQuery(name = "TblMachineLocationHistory.findByEndDate", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.endDate = :endDate"),
    @NamedQuery(name = "TblMachineLocationHistory.findByChangeReason", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.changeReason = :changeReason"),
    @NamedQuery(name = "TblMachineLocationHistory.findByChangeReasonText", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.changeReasonText = :changeReasonText"),
    @NamedQuery(name = "TblMachineLocationHistory.findByCompanyName", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.companyName = :companyName"),
    @NamedQuery(name = "TblMachineLocationHistory.findByLocationName", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.locationName = :locationName"),
    @NamedQuery(name = "TblMachineLocationHistory.findByInstllationSiteName", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.instllationSiteName = :instllationSiteName"),
    @NamedQuery(name = "TblMachineLocationHistory.findByCreateDate", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMachineLocationHistory.findByUpdateDate", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMachineLocationHistory.findByCreateUserUuid", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMachineLocationHistory.findByUpdateUserUuid", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblMachineLocationHistory.countByMachineUuid", query = "SELECT COUNT(t) FROM TblMachineLocationHistory t where t.machineUuid = :machineUuid"),
    @NamedQuery(name = "TblMachineLocationHistory.findByMachineUuidAndEnddate", query = "SELECT t.id FROM TblMachineLocationHistory t JOIN FETCH MstMachine WHERE t.mstMachine.machineId = :machineId and t.endDate = :endDate"),
    @NamedQuery(name = "TblMachineLocationHistory.updateMachineLocationHistoriesByEndDate", query = "UPDATE TblMachineLocationHistory t SET "
            + "t.endDate = :endDate,"
            + "t.updateDate = :updateDate,"
            + "t.updateUserUuid = :updateUserUuid "
            + "WHERE t.id = :id"),
    //  　会社マスタ削除する前にＦＫチェックＳＱＬ文
    @NamedQuery(name = "TblMachineLocationHistory.findFkByCompanyId", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.companyId = :companyId"),
    // 　所在地マスタ削除する前にＦＫチェックＳＱＬ文
    @NamedQuery(name = "TblMachineLocationHistory.findFkByLocationId", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.locationId = :locationId"),
    // 　設置場所マスタ削除する前にＦＫチェックＳＱＬ文
    @NamedQuery(name = "TblMachineLocationHistory.findFkByInstallationSiteId", query = "SELECT t FROM TblMachineLocationHistory t WHERE t.instllationSiteId = :instllationSiteId")

})
@Cacheable(value = false)
public class TblMachineLocationHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "CHANGE_REASON")
    private Integer changeReason;
    @Size(max = 100)
    @Column(name = "CHANGE_REASON_TEXT")
    private String changeReasonText;
    @Size(max = 100)
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Size(max = 100)
    @Column(name = "LOCATION_NAME")
    private String locationName;
    @Size(max = 100)
    @Column(name = "INSTLLATION_SITE_NAME")
    private String instllationSiteName;
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
    @JoinColumn(name = "MACHINE_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstMachine mstMachine;
    @Column(name = "MACHINE_UUID")
    private String machineUuid;
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstCompany mstCompany;
    @Column(name = "COMPANY_ID")
    private String companyId;
    @JoinColumn(name = "INSTLLATION_SITE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstInstallationSite mstInstallationSite;
    @Column(name = "INSTLLATION_SITE_ID")
    private String instllationSiteId;
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstLocation mstLocation;
    @Column(name = "LOCATION_ID")
    private String locationId;

    public TblMachineLocationHistory() {
    }

    public TblMachineLocationHistory(String id) {
        this.id = id;
    }

    public TblMachineLocationHistory(String id, Date startDate, Date endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(Integer changeReason) {
        this.changeReason = changeReason;
    }

    public String getChangeReasonText() {
        return changeReasonText;
    }

    public void setChangeReasonText(String changeReasonText) {
        this.changeReasonText = changeReasonText;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getInstllationSiteName() {
        return instllationSiteName;
    }

    public void setInstllationSiteName(String instllationSiteName) {
        this.instllationSiteName = instllationSiteName;
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

    public MstMachine getMstMachine() {
        return mstMachine;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMstMachine(MstMachine mstMachine) {
        this.mstMachine = mstMachine;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public MstCompany getMstCompany() {
        return mstCompany;
    }

    public String getCompanyId() {
        return companyId;
    }

    public MstInstallationSite getMstInstallationSite() {
        return mstInstallationSite;
    }

    public String getInstllationSiteId() {
        return instllationSiteId;
    }

    public MstLocation getMstLocation() {
        return mstLocation;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setMstCompany(MstCompany mstCompany) {
        this.mstCompany = mstCompany;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setMstInstallationSite(MstInstallationSite mstInstallationSite) {
        this.mstInstallationSite = mstInstallationSite;
    }

    public void setInstllationSiteId(String instllationSiteId) {
        this.instllationSiteId = instllationSiteId;
    }

    public void setMstLocation(MstLocation mstLocation) {
        this.mstLocation = mstLocation;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
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
        if (!(object instanceof TblMachineLocationHistory)) {
            return false;
        }
        TblMachineLocationHistory other = (TblMachineLocationHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.machine.TblMachineLocationHistory[ id=" + id + " ]";
    }

}
