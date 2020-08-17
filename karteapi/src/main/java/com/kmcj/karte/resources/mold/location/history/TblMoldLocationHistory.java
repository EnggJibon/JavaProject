/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.location.history;

import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.installationsite.MstInstallationSite;
import com.kmcj.karte.resources.location.MstLocation;
import com.kmcj.karte.resources.mold.MstMold;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "tbl_mold_location_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblMoldLocationHistory.findAll", query = "SELECT t FROM TblMoldLocationHistory t"),
    @NamedQuery(name = "TblMoldLocationHistory.findById", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.id = :id"),
    @NamedQuery(name = "TblMoldLocationHistory.findByStartDate", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.startDate = :startDate"),
    @NamedQuery(name = "TblMoldLocationHistory.findByEndDate", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.endDate = :endDate"),
    @NamedQuery(name = "TblMoldLocationHistory.findByChangeReason", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.changeReason = :changeReason"),
    @NamedQuery(name = "TblMoldLocationHistory.findByChangeReasonText", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.changeReasonText = :changeReasonText"),
    @NamedQuery(name = "TblMoldLocationHistory.findByCompanyName", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.companyName = :companyName"),
    @NamedQuery(name = "TblMoldLocationHistory.findByLocationName", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.locationName = :locationName"),
    @NamedQuery(name = "TblMoldLocationHistory.findByInstllationSiteName", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.instllationSiteName = :instllationSiteName"),
    @NamedQuery(name = "TblMoldLocationHistory.findByCreateDate", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.createDate = :createDate"),
    @NamedQuery(name = "TblMoldLocationHistory.findByUpdateDate", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.updateDate = :updateDate"),
    @NamedQuery(name = "TblMoldLocationHistory.findByCreateUserUuid", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.createUserUuid = :createUserUuid"),
    @NamedQuery(name = "TblMoldLocationHistory.findByUpdateUserUuid", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.updateUserUuid = :updateUserUuid"),
    @NamedQuery(name = "TblMoldLocationHistory.findByMoldUuidAndEnddate", query = "SELECT t.id FROM TblMoldLocationHistory t JOIN FETCH MstMold WHERE t.mstMold.moldId = :moldId and t.endDate = :endDate"),
    @NamedQuery(name = "TblMoldLocationHistory.findLatestByMoldUuidAndEnddate", query = "SELECT t FROM TblMoldLocationHistory t JOIN FETCH MstMold WHERE t.mstMold.moldId = :moldId and t.endDate = :endDate"),
    @NamedQuery(name = "TblMoldLocationHistory.findFkByCompanyId", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.companyId = :companyId"),
    @NamedQuery(name = "TblMoldLocationHistory.findFkByLocationId", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.locationId = :locationId"),
    @NamedQuery(name = "TblMoldLocationHistory.findFkByInstallationSiteId", query = "SELECT t FROM TblMoldLocationHistory t WHERE t.instllationSiteId = :instllationSiteId"),
    @NamedQuery(name = "TblMoldLocationHistory.updateTblMoldLocationHistory", query = "UPDATE TblMoldLocationHistory t SET "
            + "t.changeReason = :changeReason,"
            + "t.changeReasonText = :changeReasonText,"
            + "t.companyId = :companyId,"
            + "t.companyName = :companyName,"
            + "t.locationId = :locationId,"
            + "t.locationName = :locationName,"
            + "t.instllationSiteId = :instllationSiteId,"
            + "t.instllationSiteName = :instllationSiteName,"
            + "t.updateDate = :updateDate,"
            + "t.updateUserUuid = :updateUserUuid "
            + "WHERE t.id = :id "),
    @NamedQuery(name = "TblMoldLocationHistory.updateMoldLocationHistoriesByEndDate", query = "UPDATE TblMoldLocationHistory t SET "
            + "t.endDate = :endDate,"
            + "t.updateDate = :updateDate,"
            + "t.updateUserUuid = :updateUserUuid "
            + "WHERE t.id = :id"),
    @NamedQuery(name = "TblMoldLocationHistory.deleteTblMoldLocationHistory", query = "DELETE FROM TblMoldLocationHistory t WHERE t.mstMold.uuid = :moldUuid ")
})
@Cacheable(value = false)
public class TblMoldLocationHistory implements Serializable {

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
    @Size(max = 45)
    @Column(name = "COMPANY_ID")
    private String companyId;
    @Size(max = 100)
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Size(max = 45)
    @Column(name = "LOCATION_ID")
    private String locationId;
    @Size(max = 100)
    @Column(name = "LOCATION_NAME")
    private String locationName;
    @Size(max = 45)
    @Column(name = "INSTLLATION_SITE_ID")
    private String instllationSiteId;
    @Size(max = 100)
    @Column(name = "INSTLLATION_SITE_NAME")
    private String instllationSiteName;
    @Size(max = 45)
    @Column(name = "MOLD_UUID")
    private String moldUuid;
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
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstCompany mstCompany;
    @JoinColumn(name = "INSTLLATION_SITE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstInstallationSite mstInstallationSite;
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne
    private MstLocation mstLocation;
    @JoinColumn(name = "MOLD_UUID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MstMold mstMold;

    public TblMoldLocationHistory() {
    }

    public TblMoldLocationHistory(String id) {
        this.id = id;
    }

    public TblMoldLocationHistory(String id, Date startDate, Date endDate) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblMoldLocationHistory)) {
            return false;
        }
        TblMoldLocationHistory other = (TblMoldLocationHistory) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistory[ id=" + getId() + " ]";
    }

    /**
     * @return the mstMold
     */
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
     * @return the companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the locationId
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the instllationSiteId
     */
    public String getInstllationSiteId() {
        return instllationSiteId;
    }

    /**
     * @param instllationSiteId the instllationSiteId to set
     */
    public void setInstllationSiteId(String instllationSiteId) {
        this.instllationSiteId = instllationSiteId;
    }

    /**
     * @return the mstCompany
     */
    public MstCompany getMstCompany() {
        return mstCompany;
    }

    /**
     * @param mstCompany the mstCompany to set
     */
    public void setMstCompany(MstCompany mstCompany) {
        this.mstCompany = mstCompany;
    }

    /**
     * @return the mstInstallationSite
     */
    public MstInstallationSite getMstInstallationSite() {
        return mstInstallationSite;
    }

    /**
     * @param mstInstallationSite the mstInstallationSite to set
     */
    public void setMstInstallationSite(MstInstallationSite mstInstallationSite) {
        this.mstInstallationSite = mstInstallationSite;
    }

    /**
     * @return the mstLocation
     */
    public MstLocation getMstLocation() {
        return mstLocation;
    }

    /**
     * @param mstLocation the mstLocation to set
     */
    public void setMstLocation(MstLocation mstLocation) {
        this.mstLocation = mstLocation;
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

}
