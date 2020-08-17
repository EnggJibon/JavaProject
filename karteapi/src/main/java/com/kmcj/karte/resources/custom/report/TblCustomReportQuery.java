/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report;

import com.kmcj.karte.resources.user.MstUser;
import com.kmcj.karte.resources.custom.report.category.TblCustomReportCategory;
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_custom_report_query")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCustomReportQuery.findAll", query = "SELECT t FROM TblCustomReportQuery t "
            + "                                                   LEFT JOIN FETCH t.mstCreateUser mstCreateUser "
            + "                                                   LEFT JOIN FETCH t.mstUpdateUser mstUpdateUser "
            + "                                                   LEFT JOIN FETCH t.tblCustomReportCategory tblCustomReportCategory "),
    @NamedQuery(name = "TblCustomReportQuery.findMaxReportId", query = "SELECT MAX(t.reportId) FROM TblCustomReportQuery t"),
    @NamedQuery(name = "TblCustomReportQuery.deleteByReportId", query = "DELETE FROM TblCustomReportQuery t WHERE t.reportId = :reportId"),
    @NamedQuery(name = "TblCustomReportQuery.findByReportId", query = "SELECT t FROM TblCustomReportQuery t WHERE t.reportId = :reportId"),
    @NamedQuery(name = "TblCustomReportQuery.findByReportName", query = "SELECT t FROM TblCustomReportQuery t WHERE t.reportName = :reportName"),
})
@Cacheable(value = false)
public class TblCustomReportQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "REPORT_ID")
    private Long reportId;

    @Column(name = "REPORT_NAME")
    private String reportName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "REPORT_SQL")
    private String reportSql;
    
    @Column(name = "CATEGORY_ID")
    private String categoryId;
    
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Column(name = "CREATE_USER_UUID")
    private String createUserUuid;

    @Column(name = "UPDATE_USER_UUID")
    private String updateUserUuid;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblCustomReportQuery")
    private Collection<TblCustomReportQueryParam> tblCustomReportQueryParamCollection;
    
    /**
     * 結合テーブル定義
     */
    // ユーザーマスタ
    @PrimaryKeyJoinColumn(name = "CREATE_USER_UUID", referencedColumnName = "UUID")
    //@ManyToOne(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstCreateUser;

    // ユーザーマスタ
    @PrimaryKeyJoinColumn(name = "UPDATE_USER_UUID", referencedColumnName = "UUID")
    //@ManyToOne(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MstUser mstUpdateUser;
    
    @PrimaryKeyJoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID")
    //@ManyToOne(optional = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblCustomReportCategory tblCustomReportCategory;
    
    public TblCustomReportQuery() {
    }

    public TblCustomReportQuery(Long reportId) {
        this.reportId = reportId;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportSql() {
        return reportSql;
    }

    public void setReportSql(String reportSql) {
        this.reportSql = reportSql;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return tblCustomReportCategory.getName();
    }

    public void setCategoryName(TblCustomReportCategory tblCustomReportCategory) {
        this.tblCustomReportCategory = tblCustomReportCategory;
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
    public Collection<TblCustomReportQueryParam> getTblCustomReportQueryParamCollection() {
        return tblCustomReportQueryParamCollection;
    }

    public void setTblCustomReportQueryParamCollection(Collection<TblCustomReportQueryParam> tblCustomReportQueryParamCollection) {
        this.tblCustomReportQueryParamCollection = tblCustomReportQueryParamCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportId != null ? reportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof TblCustomReportQuery)) {
            return false;
        }
        TblCustomReportQuery other = (TblCustomReportQuery) object;
        if ((this.reportId == null && other.reportId != null) || (this.reportId != null && !this.reportId.equals(other.reportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.custome.report.TblCustomReportQuery[ reportId=" + reportId + " ]";
    }

    /**
     * @return the mstCreateUser
     */
    public MstUser getMstCreateUser() {
        return mstCreateUser;
    }

    /**
     * @param mstCreateUser the mstCreateUser to set
     */
    public void setMstCreateUser(MstUser mstCreateUser) {
        this.mstCreateUser = mstCreateUser;
    }

    /**
     * @return the mstUpdateUser
     */
    public MstUser getMstUpdateUser() {
        return mstUpdateUser;
    }

    /**
     * @param mstUpdateUser the mstUpdateUser to set
     */
    public void setMstUpdateUser(MstUser mstUpdateUser) {
        this.mstUpdateUser = mstUpdateUser;
    }

}
