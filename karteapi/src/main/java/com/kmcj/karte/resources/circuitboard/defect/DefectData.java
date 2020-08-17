/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.defect;

import java.util.List;
import com.kmcj.karte.BasicResponse;

/**
 *
 * @author bacpd
 */
public class DefectData extends BasicResponse {
   private  List<MstCircuitBoardDefect> defectList = null;
   private String defectName = "";
    private String id = "";

    /**
     * @return the defectList
     */
    public List<MstCircuitBoardDefect> getDefectList() {
        return defectList;
    }

    /**
     * @param defectList the defectList to set
     */
    public void setDefectList(List<MstCircuitBoardDefect> defectList) {
        this.defectList = defectList;
    }

    public void setDefectName(String newDefectName) {
        this.defectName = newDefectName;
    }

    public String getDefectName() {
        return this.defectName;
    }

    public void setId(String ids) {
        this.id = ids;
    }

    public String getId() {
        return this.id;
    }
}
