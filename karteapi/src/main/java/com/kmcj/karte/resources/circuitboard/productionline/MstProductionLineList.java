package com.kmcj.karte.resources.circuitboard.productionline;

import com.kmcj.karte.BasicResponse;

import java.util.List;

/**
 * Created by MinhDTB on 2018/03/01
 */
public class MstProductionLineList extends BasicResponse {

    private List<MstProductionLine> mstProductionLines;


    public List<MstProductionLine> getMstProductionLines() {
        return mstProductionLines;
    }

    public void setMstProductionLines(List<MstProductionLine> mstProductionLines) {
        this.mstProductionLines = mstProductionLines;
    }
}
