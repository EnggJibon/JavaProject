/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.defect.procedure.detail;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.circuitboard.procedure.MstCircuitBoardProcedure;
import java.util.List;

/**
 *
 * @author Apeng
 */
public class DefectProcedureList extends BasicResponse {
    
    private List<DefectProcedureVo> procedureDefectVos;
    
    private List<MstCircuitBoardProcedure> mstCircuitBoardProcedures;
    
    private long count;
    private int pageNumber;
    private int pageTotal;

    /**
     * @return the procedureDefectVos
     */
    public List<DefectProcedureVo> getDefectProcedureVos() {
        return getProcedureDefectVos();
    }

    /**
     * @param procedureDefectVos the procedureDefectVos to set
     */
    public void setDefectProcedureVos(List<DefectProcedureVo> procedureDefectVos) {
        this.setProcedureDefectVos(procedureDefectVos);
    }

    /**
     * @return the mstCircuitBoardProcedures
     */
    public List<MstCircuitBoardProcedure> getMstCircuitBoardProcedures() {
        return mstCircuitBoardProcedures;
    }

    /**
     * @param mstCircuitBoardProcedures the mstCircuitBoardProcedures to set
     */
    public void setMstCircuitBoardProcedures(List<MstCircuitBoardProcedure> mstCircuitBoardProcedures) {
        this.mstCircuitBoardProcedures = mstCircuitBoardProcedures;
    }

    /**
     * @return the procedureDefectVos
     */
    public List<DefectProcedureVo> getProcedureDefectVos() {
        return procedureDefectVos;
    }

    /**
     * @param procedureDefectVos the procedureDefectVos to set
     */
    public void setProcedureDefectVos(List<DefectProcedureVo> procedureDefectVos) {
        this.procedureDefectVos = procedureDefectVos;
    }

    /**
     * @return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(long count) {
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
