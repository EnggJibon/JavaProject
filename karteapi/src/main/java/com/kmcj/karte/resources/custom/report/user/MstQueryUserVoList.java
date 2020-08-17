/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.custom.report.user;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author admin
 */
public class MstQueryUserVoList extends BasicResponse {

    private List<MstQueryUserVo> mstQueryUserVoList;

    /**
     * @return the mstQueryUserVoList
     */
    public List<MstQueryUserVo> getMstQueryUserVoList() {
        return mstQueryUserVoList;
    }

    /**
     * @param mstQueryUserVoList the mstQueryUserVoList to set
     */
    public void setMstQueryUserVoList(List<MstQueryUserVo> mstQueryUserVoList) {
        this.mstQueryUserVoList = mstQueryUserVoList;
    }

}
