/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.proccond.spec;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineProcCondSpecList extends BasicResponse {

    private List<MstMachineProcCondSpec> mstMachineProcCondSpecs;

    private List<MstMachineProcCondSpecVo> mstMachineProcCondSpecVos;

    public MstMachineProcCondSpecList() {
    }

    public List<MstMachineProcCondSpec> getMstMachineProcCondSpecs() {
        return mstMachineProcCondSpecs;
    }

    public List<MstMachineProcCondSpecVo> getMstMachineProcCondSpecVos() {
        return mstMachineProcCondSpecVos;
    }

    public void setMstMachineProcCondSpecs(List<MstMachineProcCondSpec> mstMachineProcCondSpecs) {
        this.mstMachineProcCondSpecs = mstMachineProcCondSpecs;
    }

    public void setMstMachineProcCondSpecVos(List<MstMachineProcCondSpecVo> mstMachineProcCondSpecVos) {
        this.mstMachineProcCondSpecVos = mstMachineProcCondSpecVos;
    }

}
