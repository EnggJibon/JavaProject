/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.dashboard;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author t.takasaki
 */
@Entity
@Table(name = "tbl_ct_dashboard_filter")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblCtDashboardFilter.findByDashboardId", query = "SELECT m FROM TblCtDashboardFilter m WHERE m.dashboardId = :dashboardId order by m.seq")
})
public class TblCtDashboardFilter implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /** 検索パラメータタイプ*/
    public enum ParamType {
        /** 部署*/
        DPT,
        /** 日付*/
        DATE,
        /** 文字列*/
        TEXT
    }
    
    /** 一致タイプ*/
    public enum MatchType {
        /** 完全一致*/
        EXACT,
        /** 部分一致*/
        PARTIAL,
        /** 前方一致*/
        FORWORD
    }
    
    @Id
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "ID")
    private String id;

    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DASHBOARD_ID")
    private String dashboardId;
    
    @NotNull
    @Column(name = "SEQ")
    private int seq;
    
    @NotNull
    @Column(name = "PARAM_TYPE")
    @Enumerated(EnumType.STRING)
    private ParamType paramType;
    
    @NotNull
    @Size(min = 0, max = 45)
    @Column(name = "PARAM_NAME")
    private String paramName;
    
    @NotNull
    @Size(min = 0, max = 100)
    @Column(name = "DISP_NAME")
    private String dispName;
    
    @NotNull
    @Column(name = "REQUIRED")
    private boolean required;
    
    @NotNull
    @Size(min = 0, max = 45)
    @Column(name = "CANCEL_PARAM_NAME")
    private String cancelParamName;
    
    @NotNull
    @Column(name = "MATCH_TYPE")
    @Enumerated(EnumType.STRING)
    private MatchType matchType;
    
    @NotNull
    @Column(name = "DEFAULT_VAL")
    private int defaultVal;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(String dashboardId) {
        this.dashboardId = dashboardId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public ParamType getParamType() {
        return paramType;
    }

    public void setParamType(ParamType paramType) {
        this.paramType = paramType;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getDispName() {
        return dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getCancelParamName() {
        return cancelParamName;
    }

    public void setCancelParamName(String cancelParamName) {
        this.cancelParamName = cancelParamName;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public int getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(int defaultVal) {
        this.defaultVal = defaultVal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TblCtDashboardFilter other = (TblCtDashboardFilter) obj;
        return Objects.equals(this.id, other.id);
    }

}
