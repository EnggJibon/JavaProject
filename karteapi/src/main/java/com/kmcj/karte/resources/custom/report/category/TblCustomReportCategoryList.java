/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report.category;

import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblCustomReportCategoryList extends BasicResponse {

    private List<TblCustomReportCategory> tblCustomReportCategories;

    public void TblCustomReportCategoryList() {
        setTblCustomReportCategories(new ArrayList<>());
    }
    /**
     * @return the tblCustomReportCategories
     */
    public List<TblCustomReportCategory> getTblCustomReportCategories() {
        return tblCustomReportCategories;
    }

    /**
     * @param tblCustomReportCategories the tblCustomReportQueryVos to set
     */
    public void setTblCustomReportCategories(List<TblCustomReportCategory> tblCustomReportCategories) {
        this.tblCustomReportCategories = tblCustomReportCategories;
    }
}
