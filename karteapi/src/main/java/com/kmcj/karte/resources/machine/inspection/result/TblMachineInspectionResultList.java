/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.inspection.result;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineInspectionResultList extends BasicResponse {

    private List<TblMachineInspectionResult> tblMachineInspectionResults;

    private List<TblMachineInspectionResultVo> tblMachineInspectionResultVos;

    public TblMachineInspectionResultList() {
    }

    public void setTblMachineInspectionResults(List<TblMachineInspectionResult> tblMachineInspectionResults) {
        this.tblMachineInspectionResults = tblMachineInspectionResults;
    }

    public void setTblMachineInspectionResultVos(List<TblMachineInspectionResultVo> tblMachineInspectionResultVos) {
        this.tblMachineInspectionResultVos = tblMachineInspectionResultVos;
    }

    public List<TblMachineInspectionResult> getTblMachineInspectionResults() {
        return tblMachineInspectionResults;
    }

    public List<TblMachineInspectionResultVo> getTblMachineInspectionResultVos() {
        return tblMachineInspectionResultVos;
    }

}
