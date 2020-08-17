/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.procedure.retention;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author zds
 */
public class ProcedureRetentionList extends BasicResponse {
    
    private List<ProcedureRetentionVo> procedureRetentionVos;
    
    private long count;
    private int pageNumber;
    private int pageTotal;

    /**
     * @return the procedureRetentionVos
     */
    public List<ProcedureRetentionVo> getProcedureRetentionVos() {
        return procedureRetentionVos;
    }

    /**
     * @param procedureRetentionVos the procedureRetentionVos to set
     */
    public void setProcedureRetentionVos(List<ProcedureRetentionVo> procedureRetentionVos) {
        this.procedureRetentionVos = procedureRetentionVos;
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
