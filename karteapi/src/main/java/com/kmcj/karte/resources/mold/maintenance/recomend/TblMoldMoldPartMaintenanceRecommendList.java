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
public class TblMoldMoldPartMaintenanceRecommendList extends BasicResponse {

    private List<TblMoldMoldPartMaintenanceRecommend> tblMoldMoldPartMaintenanceRecommendList;

    /**
     * @return the tblMoldMaintenanceRecomendList
     */
    public List<TblMoldMoldPartMaintenanceRecommend> getTblMoldMoldPartMaintenanceRecommendList() {
        return tblMoldMoldPartMaintenanceRecommendList;
    }

    /**
     * @param tblMoldMaintenanceRecomendList the tblMoldMaintenanceRecomendList
     * to set
     */
    public void setTblMoldMoldPartMaintenanceRecommendList(List<TblMoldMoldPartMaintenanceRecommend> tblMoldMoldPartMaintenanceRecommendList) {
        this.tblMoldMoldPartMaintenanceRecommendList = tblMoldMoldPartMaintenanceRecommendList;
    }

}
