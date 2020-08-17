package com.kmcj.karte.resources.circuitboard.productionline;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.location.MstLocation;

import java.util.List;

/**
 * Created by MinhDTB on 2018/03/01
 */
public class MstProductionLineData extends BasicResponse {

    private List<MstLocation> mstLocations;
    private List<MstProductionLineDepartment> mstProductionLineDepartments;

    public List<MstLocation> getMstLocations() {
        return mstLocations;
    }

    public void setMstLocations(List<MstLocation> mstLocations) {
        this.mstLocations = mstLocations;
    }

    public List<MstProductionLineDepartment> getMstProductionLineDepartments() {
        return mstProductionLineDepartments;
    }

    public void setMstProductionLineDepartments(List<MstProductionLineDepartment> mstProductionLineDepartments) {
        this.mstProductionLineDepartments = mstProductionLineDepartments;
    }
}
