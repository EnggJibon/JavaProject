/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.inspection.item;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 *
 * @author jiangxs
 */
public class MstMoldInspectionItemList extends BasicResponse{
    private List<MstMoldInspectionItemVo> mstMoldInspectionItemVoList;
    private List<MstMoldInspectionItem> mstMoldInspectionItemList;

    /**
     * @return the mstMoldInspectionItemVoList
     */
    public List<MstMoldInspectionItemVo> getMstMoldInspectionItemVoList() {
        return mstMoldInspectionItemVoList;
    }

    /**
     * @param mstMoldInspectionItemVoList the mstMoldInspectionItemVoList to set
     */
    public void setMstMoldInspectionItemVoList(List<MstMoldInspectionItemVo> mstMoldInspectionItemVoList) {
        this.mstMoldInspectionItemVoList = mstMoldInspectionItemVoList;
    }

    public List<MstMoldInspectionItem> getMstMoldInspectionItemList() {
        return mstMoldInspectionItemList;
    }

    public void setMstMoldInspectionItemList(List<MstMoldInspectionItem> mstMoldInspectionItemList) {
        this.mstMoldInspectionItemList = mstMoldInspectionItemList;
    }
}
