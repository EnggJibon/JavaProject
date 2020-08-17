/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.remodeling;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class TblMoldMaintenanceRemodelingList extends BasicResponse{
    private List<TblMoldMaintenanceRemodelingVo> tblMoldMaintenanceRemodelingVoList;

    /**
     * @return the tblMoldMaintenanceRemodelingVoList
     */
    public List<TblMoldMaintenanceRemodelingVo> getTblMoldMaintenanceRemodelingVoList() {
        return tblMoldMaintenanceRemodelingVoList;
    }

    /**
     * @param tblMoldMaintenanceRemodelingVoList the tblMoldMaintenanceRemodelingVoList to set
     */
    public void setTblMoldMaintenanceRemodelingVoList(List<TblMoldMaintenanceRemodelingVo> tblMoldMaintenanceRemodelingVoList) {
        this.tblMoldMaintenanceRemodelingVoList = tblMoldMaintenanceRemodelingVoList;
    }
    
    
}
