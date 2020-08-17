/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.attribute;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineAttributeList extends BasicResponse {

    private List<MstMachineAttribute> mstMachineAttributes;
    private List<MstMachineAttributeVo> mstMachineAttributeVos;

    public MstMachineAttributeList() {
    }

    public List<MstMachineAttribute> getMstMachineAttributes() {
        return mstMachineAttributes;
    }

    public List<MstMachineAttributeVo> getMstMachineAttributeVos() {
        return mstMachineAttributeVos;
    }

    public void setMstMachineAttributes(List<MstMachineAttribute> mstMachineAttributes) {
        this.mstMachineAttributes = mstMachineAttributes;
    }

    public void setMstMachineAttributeVos(List<MstMachineAttributeVo> mstMachineAttributeVos) {
        this.mstMachineAttributeVos = mstMachineAttributeVos;
    }

}
