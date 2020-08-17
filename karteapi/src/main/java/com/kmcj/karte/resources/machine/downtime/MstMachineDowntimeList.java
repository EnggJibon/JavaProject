/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.downtime;

import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author f.kitaoji
 */
public class MstMachineDowntimeList  extends BasicResponse {
    private List<MstMachineDowntime> mstMachineDowntimes;
    
    public void MstMachineDowntimeList() {
        setMstMachineDowntimes(new ArrayList<>());
    }

    /**
     * @return the mstMachineDowntimes
     */
    public List<MstMachineDowntime> getMstMachineDowntimes() {
        return mstMachineDowntimes;
    }

    /**
     * @param mstMachineDowntimes the mstMachineDowntimes to set
     */
    public void setMstMachineDowntimes(List<MstMachineDowntime> mstMachineDowntimes) {
        this.mstMachineDowntimes = mstMachineDowntimes;
    }
    
}
