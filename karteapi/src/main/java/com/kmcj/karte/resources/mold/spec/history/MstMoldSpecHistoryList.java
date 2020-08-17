/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.spec.history;
import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMoldSpecHistoryList  extends BasicResponse {
    
    private List <MstMoldSpecHistory> mstMoldSpecHistory;
    
    private List <MstMoldSpecHistorys> mstMoldSpecHistorys;

    /**
     * @return the mstMoldSpecHistory
     */
    public List <MstMoldSpecHistory> getMstMoldSpecHistory() {
        return mstMoldSpecHistory;
    }

    /**
     * @param mstMoldSpecHistory the mstMoldSpecHistory to set
     */
    public void setMstMoldSpecHistory(List <MstMoldSpecHistory> mstMoldSpecHistory) {
        this.mstMoldSpecHistory = mstMoldSpecHistory;
    } 

    /**
     * @return the mstMoldSpecHistorys
     */
    public List <MstMoldSpecHistorys> getMstMoldSpecHistorys() {
        return mstMoldSpecHistorys;
    }

    /**
     * @param mstMoldSpecHistorys the mstMoldSpecHistorys to set
     */
    public void setMstMoldSpecHistorys(List <MstMoldSpecHistorys> mstMoldSpecHistorys) {
        this.mstMoldSpecHistorys = mstMoldSpecHistorys;
    }
    
    
    
}
