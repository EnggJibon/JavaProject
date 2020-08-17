/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.remodeling.detail;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineRemodelingDetailList extends BasicResponse {

    private List<TblMachineRemodelingDetail> tblMachineRemodelingDetails;

    private List<TblMachineRemodelingDetailVo> tblMachineRemodelingDetailVos;

    public TblMachineRemodelingDetailList() {
    }

    public List<TblMachineRemodelingDetail> getTblMachineRemodelingDetails() {
        return tblMachineRemodelingDetails;
    }

    public List<TblMachineRemodelingDetailVo> getTblMachineRemodelingDetailVos() {
        return tblMachineRemodelingDetailVos;
    }

    public void setTblMachineRemodelingDetails(List<TblMachineRemodelingDetail> tblMachineRemodelingDetails) {
        this.tblMachineRemodelingDetails = tblMachineRemodelingDetails;
    }

    public void setTblMachineRemodelingDetailVos(List<TblMachineRemodelingDetailVo> tblMachineRemodelingDetailVos) {
        this.tblMachineRemodelingDetailVos = tblMachineRemodelingDetailVos;
    }

}
