/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.maintenance.cycleptn;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMaintenanceCyclePtnList extends BasicResponse {
    private List<TblMaintenanceCyclePtn> tblMaintenanceCyclePtnList;

    /**
     * @return the tblMaintenanceCyclePtnList
     */
    public List<TblMaintenanceCyclePtn> getTblMaintenanceCyclePtnList() {
        return tblMaintenanceCyclePtnList;
    }

    /**
     * @param tblMaintenanceCyclePtnList the tblMaintenanceCyclePtnList to set
     */
    public void setTblMaintenanceCyclePtnList(List<TblMaintenanceCyclePtn> tblMaintenanceCyclePtnList) {
        this.tblMaintenanceCyclePtnList = tblMaintenanceCyclePtnList;
    }
    
    
    
}
