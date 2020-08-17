/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.mold.MstMoldAutoComplete;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineList extends BasicResponse {

    private List<MstMachine> mstMachines;

    private List<MstMachineVo> mstMachineVos;
    
    private List<MstMoldAutoComplete> mstMachineAutoComplete;
    
    private long count;
    private int pageNumber;
    private int pageTotal;

    public MstMachineList() {
    }

    public void setMstMachines(List<MstMachine> mstMachines) {
        this.mstMachines = mstMachines;
    }

    public void setMstMachineVos(List<MstMachineVo> mstMachineVos) {
        this.mstMachineVos = mstMachineVos;
    }

    public List<MstMachine> getMstMachines() {
        return mstMachines;
    }

    public List<MstMachineVo> getMstMachineVos() {
        return mstMachineVos;
    }

    /**
     * @return the mstMachineAutoComplete
     */
    public List<MstMoldAutoComplete> getMstMachineAutoComplete() {
        return mstMachineAutoComplete;
    }

    /**
     * @param mstMachineAutoComplete the mstMachineAutoComplete to set
     */
    public void setMstMachineAutoComplete(List<MstMoldAutoComplete> mstMachineAutoComplete) {
        this.mstMachineAutoComplete = mstMachineAutoComplete;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

}
