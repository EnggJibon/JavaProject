package com.kmcj.karte.resources.component.inspection.defect;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author penggd
 */
public class TblComponentInspectionDefectList extends BasicResponse {

    private List<TblComponentInspectionDefect> tblComponentInspectionDefectList;
    private List<TblComponentInspectionDefect> tblComponentInspectionDefectMTypeList;
    private List<TblComponentInspectionDefect> tblComponentInspectionDefecParetotList;
    
    private int maxQuantityByGropu;
    private int maxPareto;
    private List<String> cavList;

    public List<TblComponentInspectionDefect> getTblComponentInspectionDefectList() {
        return tblComponentInspectionDefectList;
    }

    public void setTblComponentInspectionDefectList(List<TblComponentInspectionDefect> tblComponentInspectionDefectList) {
        this.tblComponentInspectionDefectList = tblComponentInspectionDefectList;
    }

    public List<TblComponentInspectionDefect> getTblComponentInspectionDefectMTypeList() {
        return tblComponentInspectionDefectMTypeList;
    }

    public void setTblComponentInspectionDefectMTypeList(List<TblComponentInspectionDefect> tblComponentInspectionDefectMTypeList) {
        this.tblComponentInspectionDefectMTypeList = tblComponentInspectionDefectMTypeList;
    }

    public List<TblComponentInspectionDefect> getTblComponentInspectionDefecParetotList() {
        return tblComponentInspectionDefecParetotList;
    }

    public void setTblComponentInspectionDefecParetotList(List<TblComponentInspectionDefect> tblComponentInspectionDefecParetotList) {
        this.tblComponentInspectionDefecParetotList = tblComponentInspectionDefecParetotList;
    }

    public int getMaxQuantityByGropu() {
        return maxQuantityByGropu;
    }

    public void setMaxQuantityByGropu(int maxQuantityByGropu) {
        this.maxQuantityByGropu = maxQuantityByGropu;
    }

    public int getMaxPareto() {
        return maxPareto;
    }

    public void setMaxPareto(int maxPareto) {
        this.maxPareto = maxPareto;
    }

    public List<String> getCavList() {
        return cavList;
    }

    public void setCavList(List<String> cavList) {
        this.cavList = cavList;
    }

}
