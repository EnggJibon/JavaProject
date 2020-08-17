package com.kmcj.karte.resources.mold.remodeling.inspection;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMoldRemodelingInspectionResultList extends BasicResponse {

    private List<TblMoldRemodelingInspectionResult> tblMoldRemodelingInspectionResults;
    private List<TblMoldRemodelingInspectionResultVo> tblMoldRemodelingInspectionResultVos;

    public TblMoldRemodelingInspectionResultList() {
    }

    public void setTblMoldRemodelingInspectionResults(List<TblMoldRemodelingInspectionResult> tblMoldRemodelingInspectionResults) {
        this.tblMoldRemodelingInspectionResults = tblMoldRemodelingInspectionResults;
    }

    public void setTblMoldRemodelingInspectionResultVos(List<TblMoldRemodelingInspectionResultVo> tblMoldRemodelingInspectionResultVos) {
        this.tblMoldRemodelingInspectionResultVos = tblMoldRemodelingInspectionResultVos;
    }

    public List<TblMoldRemodelingInspectionResult> getTblMoldRemodelingInspectionResults() {
        return tblMoldRemodelingInspectionResults;
    }

    public List<TblMoldRemodelingInspectionResultVo> getTblMoldRemodelingInspectionResultVos() {
        return tblMoldRemodelingInspectionResultVos;
    }
}
