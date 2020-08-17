package com.kmcj.karte.resources.machine.remodeling.inspection;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineRemodelingInspectionResultList extends BasicResponse {

    private List<TblMachineRemodelingInspectionResult> TblMachineRemodelingInspectionResults;
    private List<TblMachineRemodelingInspectionResultVo> TblMachineRemodelingInspectionResultVos;

    public TblMachineRemodelingInspectionResultList() {
    }

    public List<TblMachineRemodelingInspectionResult> getTblMachineRemodelingInspectionResults() {
        return TblMachineRemodelingInspectionResults;
    }

    public List<TblMachineRemodelingInspectionResultVo> getTblMachineRemodelingInspectionResultVos() {
        return TblMachineRemodelingInspectionResultVos;
    }

    public void setTblMachineRemodelingInspectionResults(List<TblMachineRemodelingInspectionResult> TblMachineRemodelingInspectionResults) {
        this.TblMachineRemodelingInspectionResults = TblMachineRemodelingInspectionResults;
    }

    public void setTblMachineRemodelingInspectionResultVos(List<TblMachineRemodelingInspectionResultVo> TblMachineRemodelingInspectionResultVos) {
        this.TblMachineRemodelingInspectionResultVos = TblMachineRemodelingInspectionResultVos;
    }

}
