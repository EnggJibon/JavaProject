/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.maintenance.remodeling;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineMaintenanceRemodelingList extends BasicResponse {

    private List<TblMachineMaintenanceRemodeling> tblMachineMaintenanceRemodelings;

    private List<TblMachineMaintenanceRemodelingVo> tblMachineMaintenanceRemodelingVos;

    public TblMachineMaintenanceRemodelingList() {
    }

    public void setTblMachineMaintenanceRemodelings(List<TblMachineMaintenanceRemodeling> tblMachineMaintenanceRemodelings) {
        this.tblMachineMaintenanceRemodelings = tblMachineMaintenanceRemodelings;
    }

    public void setTblMachineMaintenanceRemodelingVos(List<TblMachineMaintenanceRemodelingVo> tblMachineMaintenanceRemodelingVos) {
        this.tblMachineMaintenanceRemodelingVos = tblMachineMaintenanceRemodelingVos;
    }

    public List<TblMachineMaintenanceRemodeling> getTblMachineMaintenanceRemodelings() {
        return tblMachineMaintenanceRemodelings;
    }

    public List<TblMachineMaintenanceRemodelingVo> getTblMachineMaintenanceRemodelingVos() {
        return tblMachineMaintenanceRemodelingVos;
    }

}
