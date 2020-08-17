/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.maintenance.detail;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineMaintenanceDetailList extends BasicResponse {

    private List<TblMachineMaintenanceDetail> tblMachineMaintenanceDetails;

    private List<TblMachineMaintenanceDetailVo> tblMachineMaintenanceDetailVos;

    public TblMachineMaintenanceDetailList() {
    }

    public List<TblMachineMaintenanceDetail> getTblMachineMaintenanceDetails() {
        return tblMachineMaintenanceDetails;
    }

    public List<TblMachineMaintenanceDetailVo> getTblMachineMaintenanceDetailVos() {
        return tblMachineMaintenanceDetailVos;
    }

    public void setTblMachineMaintenanceDetails(List<TblMachineMaintenanceDetail> tblMachineMaintenanceDetails) {
        this.tblMachineMaintenanceDetails = tblMachineMaintenanceDetails;
    }

    public void setTblMachineMaintenanceDetailVos(List<TblMachineMaintenanceDetailVo> tblMachineMaintenanceDetailVos) {
        this.tblMachineMaintenanceDetailVos = tblMachineMaintenanceDetailVos;
    }

}
