/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblCustomReportQueryVoList extends BasicResponse {

    private List<TblCustomReportQueryVo> tblCustomReportQueryVos;

    /**
     * @return the tblCustomReportQueryVos
     */
    public List<TblCustomReportQueryVo> getTblCustomReportQueryVos() {
        return tblCustomReportQueryVos;
    }

    /**
     * @param tblCustomReportQueryVos the tblCustomReportQueryVos to set
     */
    public void setTblCustomReportQueryVos(List<TblCustomReportQueryVo> tblCustomReportQueryVos) {
        this.tblCustomReportQueryVos = tblCustomReportQueryVos;
    }

}
