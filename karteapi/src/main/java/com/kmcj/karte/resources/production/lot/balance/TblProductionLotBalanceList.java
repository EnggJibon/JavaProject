/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.production.lot.balance;
import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 生産実績ロット残高テーブルResponse
 * @author t.ariki
 */
public class TblProductionLotBalanceList extends BasicResponse {    
    private List<TblProductionLotBalance> tblProductionLotBalances;
    
    public TblProductionLotBalanceList() {
        tblProductionLotBalances = new ArrayList<>();
    }
    public List<TblProductionLotBalance> getTblProductionLotBalances() {
        return tblProductionLotBalances;
    }
    public void setTblProductionLotBalances(List<TblProductionLotBalance> tblProductionDetails) {
        this.tblProductionLotBalances = tblProductionDetails;
    }
}
