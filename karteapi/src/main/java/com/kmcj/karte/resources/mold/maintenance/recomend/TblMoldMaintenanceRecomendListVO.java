/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.recomend;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.mold.part.maintenance.recommend.TblMoldPartMaintenanceRecommend;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMoldMaintenanceRecomendListVO extends BasicResponse {

    private List<TblMoldMaintenanceRecomendVO> tblMoldMaintenanceRecomendList;
    /**
     * @return the tblMoldMaintenanceRecomendList
     */
    public List<TblMoldMaintenanceRecomendVO> getTblMoldMaintenanceRecomendList() {
        return tblMoldMaintenanceRecomendList;
    }

    /**
     * @param tblMoldMaintenanceRecomendList the tblMoldMaintenanceRecomendList
     * to set
     */
    public void setTblMoldMaintenanceRecomendList(List<TblMoldMaintenanceRecomendVO> tblMoldMaintenanceRecomendList) {
        this.tblMoldMaintenanceRecomendList = tblMoldMaintenanceRecomendList;
    }
}
