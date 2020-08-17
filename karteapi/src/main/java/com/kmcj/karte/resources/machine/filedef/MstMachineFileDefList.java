/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.filedef;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineFileDefList extends BasicResponse {

    private List<MstMachineFileDef> mstMachineFileDefs;

    private List<MstMachineFileDefVo> mstMachineFileDefVos;

    public MstMachineFileDefList() {
    }

    public void setMstMachineFileDefs(List<MstMachineFileDef> mstMachineFileDefs) {
        this.mstMachineFileDefs = mstMachineFileDefs;
    }

    public void setMstMachineFileDefVos(List<MstMachineFileDefVo> mstMachineFileDefVos) {
        this.mstMachineFileDefVos = mstMachineFileDefVos;
    }

    public List<MstMachineFileDef> getMstMachineFileDefs() {
        return mstMachineFileDefs;
    }

    public List<MstMachineFileDefVo> getMstMachineFileDefVos() {
        return mstMachineFileDefVos;
    }

}
