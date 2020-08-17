/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report.user;

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
@Table(name = "tbl_report_query_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblReportQueryUser.findAll", query = "SELECT t FROM TblReportQueryUser t"),
    @NamedQuery(name = "TblReportQueryUser.findByReportId", query = "SELECT t FROM TblReportQueryUser t "
            + " JOIN FETCH t.mstQueryUser mstQueryUser "
            + " WHERE t.reportId = :reportId "
            + " AND mstQueryUser.userId = :userId "),
    @NamedQuery(name = "TblReportQueryUser.deleteByReportId", query = "DELETE FROM TblReportQueryUser t WHERE t.reportId = :reportId")
})
@Cacheable(value = false)
public class TblReportQueryUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "UUID")
    private String uuid;
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

    @Column(name = "REPORT_ID")
    private Long reportId;

    @Column(name = "QUERY_USER_ID")
    private String queryUserId;

    @JoinColumn(name = "QUERY_USER_ID", referencedColumnName = "UUID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstQueryUser mstQueryUser;

    public TblReportQueryUser() {
    }

    public TblReportQueryUser(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public MstQueryUser getMstQueryUser() {
        return mstQueryUser;
    }

    public void setMstQueryUser(MstQueryUser mstQueryUser) {
        this.mstQueryUser = mstQueryUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uuid != null ? uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof TblReportQueryUser)) {
            return false;
        }
        TblReportQueryUser other = (TblReportQueryUser) object;
        if ((this.uuid == null && other.uuid != null) || (this.uuid != null && !this.uuid.equals(other.uuid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.custom.report.user.TblReportQueryUser[ uuid=" + uuid + " ]";
    }

    /**
     * @return the queryUserId
     */
    public String getQueryUserId() {
        return queryUserId;
    }

    /**
     * @param queryUserId the queryUserId to set
     */
    public void setQueryUserId(String queryUserId) {
        this.queryUserId = queryUserId;
    }

    /**
     * @return the reportId
     */
    public Long getReportId() {
        return reportId;
    }

    /**
     * @param reportId the reportId to set
     */
    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

}
