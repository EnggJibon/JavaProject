/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report.user;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblReportQueryUserVoList extends BasicResponse {

    private List<TblReportQueryUserVo> tblReportQueryUserVoList;

    /**
     * @return the tblReportQueryUserVoList
     */
    public List<TblReportQueryUserVo> getTblReportQueryUserVoList() {
        return tblReportQueryUserVoList;
    }

    /**
     * @param tblReportQueryUserVoList the tblReportQueryUserVoList to set
     */
    public void setTblReportQueryUserVoList(List<TblReportQueryUserVo> tblReportQueryUserVoList) {
        this.tblReportQueryUserVoList = tblReportQueryUserVoList;
    }

}
