/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.result.model;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 * 
 * @author Apeng
 */
public class TblComponentInspectionResultPoList extends BasicResponse {

    private List<TblComponentInspectionResultPoVo> tblComponentInspectionResultPoVos;

    public TblComponentInspectionResultPoList() {
    }

    /**
     * @return the tblComponentInspectionResultPoVos
     */
    public List<TblComponentInspectionResultPoVo> getTblComponentInspectionResultPoVos() {
        return tblComponentInspectionResultPoVos;
    }

    /**
     * @param tblComponentInspectionResultPoVos the tblComponentInspectionResultPoVos to set
     */
    public void setTblComponentInspectionResultPoVos(List<TblComponentInspectionResultPoVo> tblComponentInspectionResultPoVos) {
        this.tblComponentInspectionResultPoVos = tblComponentInspectionResultPoVos;
    }
}
