package com.kmcj.karte.resources.circuitboard.procedure;

import com.kmcj.karte.BasicResponse;

import java.util.List;

public class MstCircuitBoardProcedureList extends BasicResponse {
    private List<MstCircuitBoardProcedure> mstCircuitBoardProcedures = null;
    private int pageSize = 0;
    private int pageNumber = 0;
    private int pageTotal = 0;

    /**
     * @return the mstCircuitBoardProcedures
     */
    public List<MstCircuitBoardProcedure> getMstCircuitBoardProcedureList() {
        return mstCircuitBoardProcedures;
    }

    /**
     * @param mstCircuitBoardProcedures set data
     */
    public void setMstCircuitBoardProcedureList(List<MstCircuitBoardProcedure> mstCircuitBoardProcedures) {
        this.mstCircuitBoardProcedures = mstCircuitBoardProcedures;
    }

    public void setPageSize(int newPageSize) {
        this.pageSize = newPageSize;
    }

    public void setPageTotal(int newPageTotal) {
        this.pageTotal = newPageTotal;
    }

    public void setPageNumber(int newPageNumber) {
        this.pageNumber = newPageNumber;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public int getPageTotal() {
        return this.pageTotal;
    }
}
