/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.defect.inspection.detail;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class DefectInspectionList extends BasicResponse {

    private List<DefectInspectionVo> appearanceInspectionDefectVos;
    
    private DefectInspectionVo defectInspectionVo;
    
    private List<DefectInspectionDetailVo> defectInspectionDetailVos;
    
    private long count;
    private int pageNumber;
    private int pageTotal;

    /**
     * @return the appearanceInspectionDefectVos
     */
    public List<DefectInspectionVo> getAppearanceInspectionDefectVos() {
        return appearanceInspectionDefectVos;
    }

    /**
     * @param appearanceInspectionDefectVos the appearanceInspectionDefectVos to
     * set
     */
    public void setAppearanceInspectionDefectVos(List<DefectInspectionVo> appearanceInspectionDefectVos) {
        this.appearanceInspectionDefectVos = appearanceInspectionDefectVos;
    }

    /**
     * @return the defectInspectionVo
     */
    public DefectInspectionVo getDefectInspectionVo() {
        return defectInspectionVo;
    }

    /**
     * @param defectInspectionVo the defectInspectionVo to set
     */
    public void setDefectInspectionVo(DefectInspectionVo defectInspectionVo) {
        this.defectInspectionVo = defectInspectionVo;
    }

    /**
     * @return the defectInspectionDetailVos
     */
    public List<DefectInspectionDetailVo> getDefectInspectionDetailVos() {
        return defectInspectionDetailVos;
    }

    /**
     * @param defectInspectionDetailVos the defectInspectionDetailVos to set
     */
    public void setDefectInspectionDetailVos(List<DefectInspectionDetailVo> defectInspectionDetailVos) {
        this.defectInspectionDetailVos = defectInspectionDetailVos;
    }

    /**
     * @return the count
     */
    public Long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     * @return the pageNumber
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * @param pageNumber the pageNumber to set
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * @return the pageTotal
     */
    public int getPageTotal() {
        return pageTotal;
    }

    /**
     * @param pageTotal the pageTotal to set
     */
    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

}
