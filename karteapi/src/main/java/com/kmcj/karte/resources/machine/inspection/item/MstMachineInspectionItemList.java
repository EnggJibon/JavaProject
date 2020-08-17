/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.inspection.item;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineInspectionItemList extends BasicResponse {

    private List<MstMachineInspectionItem> mstMachineInspectionItems;

    private List<MstMachineInspectionItemVo> mstMachineInspectionItemVos;

    public MstMachineInspectionItemList() {
    }

    public void setMstMachineInspectionItems(List<MstMachineInspectionItem> mstMachineInspectionItems) {
        this.mstMachineInspectionItems = mstMachineInspectionItems;
    }

    public void setMstMachineInspectionItemVos(List<MstMachineInspectionItemVo> mstMachineInspectionItemVos) {
        this.mstMachineInspectionItemVos = mstMachineInspectionItemVos;
    }

    public List<MstMachineInspectionItem> getMstMachineInspectionItems() {
        return mstMachineInspectionItems;
    }

    public List<MstMachineInspectionItemVo> getMstMachineInspectionItemVos() {
        return mstMachineInspectionItemVos;
    }

}
