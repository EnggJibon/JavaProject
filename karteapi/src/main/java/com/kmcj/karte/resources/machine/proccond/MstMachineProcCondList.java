/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.proccond;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineProcCondList extends BasicResponse {

    private List<MstMachineProcCond> mstMachineProcConds;

    private List<MstMachineProcCondVo> mstMachineProcCondVos;

    public MstMachineProcCondList() {
    }

    public void setMstMachineProcConds(List<MstMachineProcCond> mstMachineProcConds) {
        this.mstMachineProcConds = mstMachineProcConds;
    }

    public void setMstMachineProcCondVos(List<MstMachineProcCondVo> mstMachineProcCondVos) {
        this.mstMachineProcCondVos = mstMachineProcCondVos;
    }

    public List<MstMachineProcCond> getMstMachineProcConds() {
        return mstMachineProcConds;
    }

    public List<MstMachineProcCondVo> getMstMachineProcCondVos() {
        return mstMachineProcCondVos;
    }

}
