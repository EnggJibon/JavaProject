/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.machine.inspection.choice;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstMachineInspectionChoiceList extends BasicResponse {

    private List<MstMachineInspectionChoice> mstMachineInspectionChoices;
    private List<MstMachineInspectionChoiceVo> mstMachineInspectionChoiceVos;

    public MstMachineInspectionChoiceList() {
    }

    public void setMstMachineInspectionChoices(List<MstMachineInspectionChoice> mstMachineInspectionChoices) {
        this.mstMachineInspectionChoices = mstMachineInspectionChoices;
    }

    public void setMstMachineInspectionChoiceVos(List<MstMachineInspectionChoiceVo> mstMachineInspectionChoiceVos) {
        this.mstMachineInspectionChoiceVos = mstMachineInspectionChoiceVos;
    }

    public List<MstMachineInspectionChoice> getMstMachineInspectionChoices() {
        return mstMachineInspectionChoices;
    }

    public List<MstMachineInspectionChoiceVo> getMstMachineInspectionChoiceVos() {
        return mstMachineInspectionChoiceVos;
    }
}
