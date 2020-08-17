/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.procedure;

import java.util.List;

/**
 * @author bacpd
 */
public class ProcedureData {
    private List<MstCircuitBoardProcedure> procedureList = null;

    /**
     * @return the procedureList
     */
    public List<MstCircuitBoardProcedure> getProcedureList() {
        return procedureList;
    }

    /**
     * @param procedureList the procedureList to set
     */
    public void setProcedureList(List<MstCircuitBoardProcedure> procedureList) {
        this.procedureList = procedureList;
    }
}
