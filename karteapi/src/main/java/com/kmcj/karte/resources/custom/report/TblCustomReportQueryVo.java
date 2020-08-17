/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.custom.report.user.TblReportQueryUserVo;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblCustomReportQueryVo extends BasicResponse {

    private long reportId;

    private String reportName;

    private String description;

    private String reportSql;
    
    private String categoryId;
    
    private String categoryName;

    private String createDate;

    private String updateDate;

    private String createUserName;

    private String updateUserName;
    
    private int selectedFlag;

    private List<TblCustomReportQueryParamVo> tblCustomReportQueryParamVos;

    private List<TblReportQueryUserVo> tblReportQueryUserVos;

    /**
     * @return the reportId
     */
    public long getReportId() {
        return reportId;
    }

    /**
     * @param reportId the reportId to set
     */
    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    /**
     * @return the reportName
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * @param reportName the reportName to set
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the reportSql
     */
    public String getReportSql() {
        return reportSql;
    }

    /**
     * @param reportSql the reportSql to set
     */
    public void setReportSql(String reportSql) {
        this.reportSql = reportSql;
    }
    
    /**
     * @return the categoryId
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
    /**
     * @return the categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return the tblCustomReportQueryParamVos
     */
    public List<TblCustomReportQueryParamVo> getTblCustomReportQueryParamVos() {
        return tblCustomReportQueryParamVos;
    }

    /**
     * @param tblCustomReportQueryParamVos the tblCustomReportQueryParamVos to
     * set
     */
    public void setTblCustomReportQueryParamVos(List<TblCustomReportQueryParamVo> tblCustomReportQueryParamVos) {
        this.tblCustomReportQueryParamVos = tblCustomReportQueryParamVos;
    }

    /**
     * @return the createDate
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the updateDate
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate the updateDate to set
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return the createUserName
     */
    public String getCreateUserName() {
        return createUserName;
    }

    /**
     * @param createUserName the createUserName to set
     */
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    /**
     * @return the updateUserName
     */
    public String getUpdateUserName() {
        return updateUserName;
    }

    /**
     * @param updateUserName the updateUserName to set
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    /**
     * @return the tblReportQueryUserVos
     */
    public List<TblReportQueryUserVo> getTblReportQueryUserVos() {
        return tblReportQueryUserVos;
    }

    /**
     * @param tblReportQueryUserVos the tblReportQueryUserVos to set
     */
    public void setTblReportQueryUserVos(List<TblReportQueryUserVo> tblReportQueryUserVos) {
        this.tblReportQueryUserVos = tblReportQueryUserVos;
    }

    /**
     * @return the selectedFlag
     */
    public int getSelectedFlag() {
        return selectedFlag;
    }

    /**
     * @param selectedFlag the selectedFlag to set
     */
    public void setSelectedFlag(int selectedFlag) {
        this.selectedFlag = selectedFlag;
    }

}
