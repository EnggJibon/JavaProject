/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.standard.worktime;

import com.kmcj.karte.BasicResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 標準業務時間マスタResponse
 *
 * @author admin
 */
public class MstStandardWorkTimeList extends BasicResponse {

    private List<MstStandardWorkTime> mstStandardWorkTime;

    public MstStandardWorkTimeList() {
        mstStandardWorkTime = new ArrayList<>();
    }

    /**
     * @return the mstStandardWorkTime
     */
    public List<MstStandardWorkTime> getMstStandardWorkTime() {
        return mstStandardWorkTime;
    }

    /**
     * @param mstStandardWorkTime the mstStandardWorkTime to set
     */
    public void setMstStandardWorkTime(List<MstStandardWorkTime> mstStandardWorkTime) {
        this.mstStandardWorkTime = mstStandardWorkTime;
    }

}
