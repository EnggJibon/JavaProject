/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.material;

import com.kmcj.karte.BasicResponse;
import java.util.List;

/**
 * 材料マスタResponse
 * @author zds
 */
public class MstMaterialList extends BasicResponse {
    private List<MstMaterial> MstMaterialList;
    private long count;
    private int pageNumber;
    private int pageTotal;

    /**
     * @return the MstMaterialList
     */
    public List<MstMaterial> getMstMaterialList() {
        return MstMaterialList;
    }

    /**
     * @param MstMaterialList the MstMaterialList to set
     */
    public void setMstMaterialList(List<MstMaterial> MstMaterialList) {
        this.MstMaterialList = MstMaterialList;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }
    
}
