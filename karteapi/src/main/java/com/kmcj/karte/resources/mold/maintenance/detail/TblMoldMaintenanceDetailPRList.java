/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.detail;

import com.kmcj.karte.BasicResponse;
import java.util.List;

public class TblMoldMaintenanceDetailPRList extends BasicResponse {

    private List<TblMoldMaintenanceDetailPR> tblMoldlMaintenanceDetailPRs;

    public List<TblMoldMaintenanceDetailPR> getTblMoldlMaintenanceDetailPRs() {
        return tblMoldlMaintenanceDetailPRs;
    }

    public void setTblMoldlMaintenanceDetailPRs(List<TblMoldMaintenanceDetailPR> tblMoldlMaintenanceDetailPRs) {
        this.tblMoldlMaintenanceDetailPRs = tblMoldlMaintenanceDetailPRs;
    }


}
