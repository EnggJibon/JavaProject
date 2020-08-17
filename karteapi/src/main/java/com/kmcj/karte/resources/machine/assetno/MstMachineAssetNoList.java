/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.assetno;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineAssetNoList extends BasicResponse {

    private List<MstMachineAssetNo> mstMachineAssetNos;
    private List<MstMachineAssetNoVo> mstMachineAssetNoVos;

    public MstMachineAssetNoList() {
    }

    public void setMstMachineAssetNos(List<MstMachineAssetNo> mstMachineAssetNos) {
        this.mstMachineAssetNos = mstMachineAssetNos;
    }

    public void setMstMachineAssetNoVos(List<MstMachineAssetNoVo> mstMachineAssetNoVos) {
        this.mstMachineAssetNoVos = mstMachineAssetNoVos;
    }

    public List<MstMachineAssetNo> getMstMachineAssetNos() {
        return mstMachineAssetNos;
    }

    public List<MstMachineAssetNoVo> getMstMachineAssetNoVos() {
        return mstMachineAssetNoVos;
    }
}
