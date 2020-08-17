/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.location.history;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class TblMachineLocationHistoryList extends BasicResponse {

    private List<TblMachineLocationHistory> tblMachineLocationHistorys;
    private List<TblMachineLocationHistoryVo> tblMachineLocationHistoryVos;

    public TblMachineLocationHistoryList() {
    }

    public List<TblMachineLocationHistory> getTblMachineLocationHistorys() {
        return tblMachineLocationHistorys;
    }

    public List<TblMachineLocationHistoryVo> getTblMachineLocationHistoryVos() {
        return tblMachineLocationHistoryVos;
    }

    public void setTblMachineLocationHistorys(List<TblMachineLocationHistory> tblMachineLocationHistorys) {
        this.tblMachineLocationHistorys = tblMachineLocationHistorys;
    }

    public void setTblMachineLocationHistoryVos(List<TblMachineLocationHistoryVo> tblMachineLocationHistoryVos) {
        this.tblMachineLocationHistoryVos = tblMachineLocationHistoryVos;
    }

}
