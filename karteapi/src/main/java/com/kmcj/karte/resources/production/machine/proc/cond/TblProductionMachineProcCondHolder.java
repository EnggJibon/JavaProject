/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.machine.proc.cond;

import com.kmcj.karte.resources.production.detail.TblProductionDetail;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class TblProductionMachineProcCondHolder {
    
    private TblProductionDetail tblProductionDetail;
    private List<TblProductionMachineProcCond> TblProductionMachineProcConds;

    /**
     * @return the tblProductionDetail
     */
    public TblProductionDetail getTblProductionDetail() {
        return tblProductionDetail;
    }

    /**
     * @param tblProductionDetail the tblProductionDetail to set
     */
    public void setTblProductionDetail(TblProductionDetail tblProductionDetail) {
        this.tblProductionDetail = tblProductionDetail;
    }

    /**
     * @return the TblProductionMachineProcConds
     */
    public List<TblProductionMachineProcCond> getTblProductionMachineProcConds() {
        return TblProductionMachineProcConds;
    }

    /**
     * @param TblProductionMachineProcConds the TblProductionMachineProcConds to set
     */
    public void setTblProductionMachineProcConds(List<TblProductionMachineProcCond> TblProductionMachineProcConds) {
        this.TblProductionMachineProcConds = TblProductionMachineProcConds;
    }
    
    
}
