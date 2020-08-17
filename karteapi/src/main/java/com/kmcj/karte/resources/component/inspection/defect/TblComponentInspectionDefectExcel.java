/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.defect;

import com.kmcj.karte.excelhandle.annotation.Cell;
import com.kmcj.karte.excelhandle.annotation.Excel;

/**
 * ログ確認一覧Excelダウンロード
 */
@Excel(isNeedSequence = false)
public class TblComponentInspectionDefectExcel {
    
    @Cell(columnNum = "1")
    private String defectSeqName;
    
    @Cell(columnNum = "2")
    private String quantityByGropu;
    
    @Cell(columnNum = "3")
    private String mTypeName;
    
    @Cell(columnNum = "4")
    private String pareto;

    public String getDefectSeqName() {
        return defectSeqName;
    }

    public void setDefectSeqName(String defectSeqName) {
        this.defectSeqName = defectSeqName;
    }

    public String getmTypeName() {
        return mTypeName;
    }

    public void setmTypeName(String mTypeName) {
        this.mTypeName = mTypeName;
    }

    public String getQuantityByGropu() {
        return quantityByGropu;
    }

    public void setQuantityByGropu(String quantityByGropu) {
        this.quantityByGropu = quantityByGropu;
    }

    public String getPareto() {
        return pareto;
    }

    public void setPareto(String pareto) {
        this.pareto = pareto;
    }
    
    
    
}
