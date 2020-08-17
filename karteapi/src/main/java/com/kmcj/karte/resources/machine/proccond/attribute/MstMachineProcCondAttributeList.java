/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.proccond.attribute;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineProcCondAttributeList extends BasicResponse {

    private List<MstMachineProcCondAttribute> mstMachineProcCondAttributes;
    private List<MstMachineProcCondAttributeVo> mstMachineProcCondAttributeVos;

    public MstMachineProcCondAttributeList() {
    }

    public void setMstMachineProcCondAttributes(List<MstMachineProcCondAttribute> mstMachineProcCondAttributes) {
        this.mstMachineProcCondAttributes = mstMachineProcCondAttributes;
    }

    public void setMstMachineProcCondAttributeVos(List<MstMachineProcCondAttributeVo> mstMachineProcCondAttributeVos) {
        this.mstMachineProcCondAttributeVos = mstMachineProcCondAttributeVos;
    }

    public List<MstMachineProcCondAttribute> getMstMachineProcCondAttributes() {
        return mstMachineProcCondAttributes;
    }

    public List<MstMachineProcCondAttributeVo> getMstMachineProcCondAttributeVos() {
        return mstMachineProcCondAttributeVos;
    }

}
