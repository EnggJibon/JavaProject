/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.recomend;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMoldMaintenanceRecomendList extends BasicResponse {

    private List<TblMoldMaintenanceRecomend> tblMoldMaintenanceRecomendList;

    /**
     * @return the tblMoldMaintenanceRecomendList
     */
    public List<TblMoldMaintenanceRecomend> getTblMoldMaintenanceRecomendList() {
        return tblMoldMaintenanceRecomendList;
    }

    /**
     * @param tblMoldMaintenanceRecomendList the tblMoldMaintenanceRecomendList
     * to set
     */
    public void setTblMoldMaintenanceRecomendList(List<TblMoldMaintenanceRecomend> tblMoldMaintenanceRecomendList) {
        this.tblMoldMaintenanceRecomendList = tblMoldMaintenanceRecomendList;
    }

}
