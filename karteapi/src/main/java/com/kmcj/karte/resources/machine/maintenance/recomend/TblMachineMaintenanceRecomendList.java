/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.maintenance.recomend;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineMaintenanceRecomendList extends BasicResponse {

    private List<TblMachineMaintenanceRecomend> tblMachineMaintenanceRecomendList;

    /**
     * @return the tblMachineMaintenanceRecomendList
     */
    public List<TblMachineMaintenanceRecomend> getTblMachineMaintenanceRecomendList() {
        return tblMachineMaintenanceRecomendList;
    }

    /**
     * @param tblMachineMaintenanceRecomendList the
     * tblMachineMaintenanceRecomendList to set
     */
    public void setTblMachineMaintenanceRecomendList(List<TblMachineMaintenanceRecomend> tblMachineMaintenanceRecomendList) {
        this.tblMachineMaintenanceRecomendList = tblMachineMaintenanceRecomendList;
    }

}
