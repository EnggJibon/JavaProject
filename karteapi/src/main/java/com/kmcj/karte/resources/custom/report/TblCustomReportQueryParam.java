/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "tbl_custom_report_query_param")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCustomReportQueryParam.findAll", query = "SELECT t FROM TblCustomReportQueryParam t"),
    @NamedQuery(name = "TblCustomReportQueryParam.deleteQueryParamByReportId", query = "DELETE FROM TblCustomReportQueryParam t WHERE t.reportId = :reportId")
})
@Cacheable(value = false)
public class TblCustomReportQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String paramId;
    
    @Column(name = "REPORT_ID")
    private Long reportId;
    
    @Column(name = "PARAM_NAME")
    private String paramName;
    
    @Column(name = "PARAM_VALUE")
    private String paramValue;
    
    @JoinColumn(name = "REPORT_ID", referencedColumnName = "REPORT_ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private TblCustomReportQuery tblCustomReportQuery;

    public TblCustomReportQueryParam() {
    }

    public TblCustomReportQueryParam(String paramId) {
        this.paramId = paramId;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paramId != null ? paramId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TblCustomReportQueryParam)) {
            return false;
        }
        TblCustomReportQueryParam other = (TblCustomReportQueryParam) object;
        if ((this.paramId == null && other.paramId != null) || (this.paramId != null && !this.paramId.equals(other.paramId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kmcj.karte.resources.custom.report.TblCustomReportQueryParam[ paramId=" + paramId + " ]";
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

    /**
     * @return the tblCustomReportQuery
     */
    public TblCustomReportQuery getTblCustomReportQuery() {
        return tblCustomReportQuery;
    }

    /**
     * @param tblCustomReportQuery the tblCustomReportQuery to set
     */
    public void setTblCustomReportQuery(TblCustomReportQuery tblCustomReportQuery) {
        this.tblCustomReportQuery = tblCustomReportQuery;
    }
    
}
