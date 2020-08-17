/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.spec.history;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineSpecHistoryList extends BasicResponse {
    
    private List<MstMachineSpecHistory> mstMachineSpecHistorys;

    private List<MstMachineSpecHistoryVo> mstMachineSpecHistoryVos;

    public MstMachineSpecHistoryList() {
    }

    public void setMstMachineSpecHistorys(List<MstMachineSpecHistory> mstMachineSpecHistorys) {
        this.mstMachineSpecHistorys = mstMachineSpecHistorys;
    }

    public void setMstMachineSpecHistoryVos(List<MstMachineSpecHistoryVo> mstMachineSpecHistoryVos) {
        this.mstMachineSpecHistoryVos = mstMachineSpecHistoryVos;
    }

    public List<MstMachineSpecHistory> getMstMachineSpecHistorys() {
        return mstMachineSpecHistorys;
    }

    public List<MstMachineSpecHistoryVo> getMstMachineSpecHistoryVos() {
        return mstMachineSpecHistoryVos;
    }

}
