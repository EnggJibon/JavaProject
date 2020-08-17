package com.kmcj.karte.resources.circuitboard.productionline.machine;

import com.kmcj.karte.BasicResponse;

import java.util.List;

/**
 * Created by MinhDTB on 2018/03/01
 */
public class MstProductionLineMachineList extends BasicResponse {

    private List<MstProductionLineMachine> mstProductionLineMachines;


    public List<MstProductionLineMachine> getMstProductionLineMachines() {
        return mstProductionLineMachines;
    }

    public void setMstProductionLineMachines(List<MstProductionLineMachine> mstProductionLineMachines) {
        this.mstProductionLineMachines = mstProductionLineMachines;
    }
}
