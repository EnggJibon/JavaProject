/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.spec;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineSpecList extends BasicResponse {

    private List<MstMachineSpec> mstMachineSpecs;

    private List<MstMachineSpecVo> mstMachineSpecVos;

    public MstMachineSpecList() {
    }

    public void setMstMachineSpecs(List<MstMachineSpec> mstMachineSpecs) {
        this.mstMachineSpecs = mstMachineSpecs;
    }

    public void setMstMachineSpecVos(List<MstMachineSpecVo> mstMachineSpecVos) {
        this.mstMachineSpecVos = mstMachineSpecVos;
    }

    public List<MstMachineSpec> getMstMachineSpecs() {
        return mstMachineSpecs;
    }

    public List<MstMachineSpecVo> getMstMachineSpecVos() {
        return mstMachineSpecVos;
    }

}
