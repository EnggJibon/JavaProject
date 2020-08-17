/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.location.history;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class TblMoldLocationHistoryList extends BasicResponse {
    private List<TblMoldLocationHistory> TblMoldLocationHistory;
    private List<TblMoldLocationHistorys> tblMoldLocationHistorys;

    /**
     * @return the TblMoldLocationHistory
     */
    public List<TblMoldLocationHistory> getTblMoldLocationHistory() {
        return TblMoldLocationHistory;
    }

    /**
     * @param TblMoldLocationHistory the TblMoldLocationHistory to set
     */
    public void setTblMoldLocationHistory(List<TblMoldLocationHistory> TblMoldLocationHistory) {
        this.TblMoldLocationHistory = TblMoldLocationHistory;
    }

    public List<TblMoldLocationHistorys> getTblMoldLocationHistorys() {
        return tblMoldLocationHistorys;
    }

    public void setTblMoldLocationHistorys(List<TblMoldLocationHistorys> tblMoldLocationHistorys) {
        this.tblMoldLocationHistorys = tblMoldLocationHistorys;
    }    
}
